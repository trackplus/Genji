/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.admin.user.person;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.admin.customize.lists.BlobBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.ProjectConfigBL;
import com.aurel.track.admin.user.department.DepartmentBL;
import com.aurel.track.admin.user.person.feature.UserFeatureBL;
import com.aurel.track.admin.user.profile.ProfileJSON;
import com.aurel.track.admin.user.userLevel.UserLevelBL;
import com.aurel.track.beans.TExportTemplateBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TUserFeatureBean;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PersonDAO;
import com.aurel.track.dbase.jobs.LdapSynchronizerJob;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.json.JSONUtility.JSON_FIELDS;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.listFields.NotLocalizedListIndexer;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.dashboard.assign.DashboardAssignBL;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.LocaleHandler;
import com.trackplus.license.LicensedFeature;

/**
 * Configuring the person
 * @author Tamas Ruff
 *
 */
public class PersonConfigBL {
	private static final Logger LOGGER = LogManager.getLogger(PersonConfigBL.class);
	private static PersonDAO personDAO = DAOFactory.getFactory().getPersonDAO();

	/**
	 * Load all users or clients
	 * @param locale
	 * @return
	 */
	static String loadAll(Locale locale, boolean isUser) {
		List<TPersonBean> personBeanList = null;
		if (isUser) {
			personBeanList = PersonBL.loadAllUsers();
		} else {
			personBeanList = PersonBL.loadAllClients();
		}
		if (personBeanList != null) {
			List<IntegerStringBean> userLevelList = UserLevelBL.getUserLevelsList(locale);
			Map<Integer, String> userLevelMap = GeneralUtils.createIntegerStringMapFromIntegerStringList(userLevelList);
			Map<Integer, String> departmentMap = DepartmentBL.getDepartmentMap();
			Map<Integer, List<TUserFeatureBean>> userFeaturesMap = UserFeatureBL.getAllUserFeaturesMap();
			List<LabelValueBean> languages = LocaleHandler.getPossibleLocales();
			Map<String, String> languagesMap = new HashMap<String, String>();
			for (LabelValueBean labelValueBean : languages) {
				languagesMap.put(labelValueBean.getValue(), labelValueBean.getLabel());
			}
			Map<Integer, TPersonBean> personsMap = GeneralUtils.createMapFromList(personBeanList);
			return ProfileJSON.encodePersonListJSON(personBeanList, personsMap, userLevelMap, departmentMap, languagesMap, userFeaturesMap, locale);
		}
		return null;
	}

	/**
	 * Gets the available user levels
	 * @param locale
	 * @return
	 */
	static String getUserConfigs(Locale locale) {
		List<IntegerStringBean> userLevelList = UserLevelBL.getUserLevelsList(locale);
		List<LicensedFeature> licensedFeatures = UserFeatureBL.getLicensedFeatures();
		Map<String, Integer> actuallyUsedFeatureMap = UserFeatureBL.getActuallyUsedFeaturesMap(licensedFeatures);
		return ProfileJSON.getConfigValues(userLevelList, licensedFeatures, actuallyUsedFeatureMap);
	}



	/**
	 * Deletes a person without dependency or offers the replacement list
	 * @param personID
	 * @return
	 */
	static String delete(List<Integer> selectedPersonIDList) {
		if (selectedPersonIDList!=null && !selectedPersonIDList.isEmpty()) {
			if (hasDependentPersonData(selectedPersonIDList)){
				return JSONUtility.encodeJSONFailure(null, JSONUtility.DELETE_ERROR_CODES.NEED_REPLACE);
			} else {
				for (Integer personID : selectedPersonIDList) {
					deletePerson(personID);
				}
				ApplicationBean.getApplicationBean().setActualUsers();
			}
		}
		return JSONUtility.encodeJSONSuccess();
	}

	/**
	 * Whether a person has dependent data
	 * @param oldPersonID
	 * @return
	 */
	public static boolean hasDependentPersonData(List<Integer> oldPersonIDs) {
		return personDAO.hasDependentPersonData(oldPersonIDs) ||
				AttributeValueBL.isSystemOptionAttribute(oldPersonIDs, SystemFields.INTEGER_PERSON) || //personDAO.hasDependencyInUserPicker(oldPersonIDs) ||
				FieldChangeBL.isSystemOptionInHistory(oldPersonIDs, SystemFields.INTEGER_PERSON, true) ||
				FieldChangeBL.isSystemOptionInHistory(oldPersonIDs, SystemFields.INTEGER_PERSON, false);
				/*personDAO.hasDependencyInHistory(oldPersonIDs, true) ||
				personDAO.hasDependencyInHistory(oldPersonIDs, false);*/
	}

	/**
	 * Delete a person without dependences
	 * @param personID
	 */
	public static void deletePerson(Integer personID) {
		TPersonBean personBean = PersonBL.loadByPrimaryKey(personID);
		Integer iconKey = null;
		if (personBean!=null) {
			iconKey = personBean.getIconKey();
		}
		personDAO.deletePerson(personID);
		if (iconKey!=null) {
			BlobBL.delete(iconKey);
		}
		LookupContainer.resetPersonMap();
		//delete from lucene index
		NotLocalizedListIndexer.getInstance().deleteByKeyAndType(personID,
				LuceneUtil.LOOKUPENTITYTYPES.PERSONNAME);
		//cache and possible lucene update in other nodes
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PERSON, personID, ClusterMarkChangesBL.getChangeTypeByDelete());
	}

	/**
	 * Delete the person specific entities
	 * Those which should not be kept for replace
	 * @param personID
	 */
	private static void deletePrivateEntities(Integer personID) {
		//the following entities might be added as private or project or public
		//delete only the private ones, and replace later the remaining project and public entities
		DAOFactory.getFactory().getNotifyTriggerDAO().deleteOwnTriggers(personID);
		DAOFactory.getFactory().getQueryRepositoryDAO().deletePrivateTreeQueries(personID);
		DAOFactory.getFactory().getFilterCategoryDAO().deletePrivateFilterCategories(personID);
		List<TExportTemplateBean> templateBeans = DAOFactory.getFactory().getExportTemplateDAO().loadPrivate(personID);
		if (templateBeans!=null) {
			for (TExportTemplateBean exportTemplateBean : templateBeans) {
				deletePrivate(exportTemplateBean.getObjectID());
			}
		}
		DAOFactory.getFactory().getReportCategoryDAO().deletePrivateReportCategories(personID);
	}

	private static void deletePrivateProjects(Integer personID, Integer loggedUser) {
		List<TProjectBean> proviteProjects = ProjectBL.getPrivateProject(personID);
		if (proviteProjects!=null) {
			for (TProjectBean projectBean : proviteProjects) {
				ProjectConfigBL.deleteProject(projectBean.getObjectID(), true, LookupContainer.getPersonBean(loggedUser), Locale.ENGLISH);
			}
		}
	}

	public static boolean deletePrivate(Integer templateId){
		ReportBL.delete(templateId);
		return deleteDirectory(ReportBL.getDirTemplate(templateId));
	}

	private static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	/**
	 * Creates the JSON string for replacement triggers
	 * @param selectedPersonIDList
	 * @param errorMessage
	 * @param locale
	 * @return
	 */
	static String prepareReplacement(List<Integer> selectedPersonIDList, String errorMessage, Locale locale) {
		String replacementEntity = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.user.lbl.user", locale);
		String personName = null;
		List<TPersonBean> replacementPersonList = prepareReplacementPersons(selectedPersonIDList);
		if (selectedPersonIDList!=null && !selectedPersonIDList.isEmpty()) {
			if (selectedPersonIDList.size()==1) {
				//delete only one person
				TPersonBean personBean = PersonBL.loadByPrimaryKey(selectedPersonIDList.get(0));
				if (personBean!=null) {
					personName = personBean.getName();
				}
				return JSONUtility.createReplacementListJSON(true, personName, replacementEntity, replacementEntity, (List)replacementPersonList, errorMessage, locale);
			} else {
				//delete more than one person
				int totalNumber = selectedPersonIDList.size();
				String entities = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.user.lbl.users", locale);
				return JSONUtility.createReplacementListJSON(true, totalNumber, entities, replacementEntity, (List)replacementPersonList, errorMessage, locale);
			}
		}
		return JSONUtility.createReplacementListJSON(true, personName, replacementEntity, replacementEntity, (List)replacementPersonList, errorMessage, locale);
	}

	/**
	 * Prepares the replacement triggers
	 * @param personID
	 * @return
	 */
	public static List<TPersonBean> prepareReplacementPersons(List<Integer> personIDs) {
		List<TPersonBean> replacementPersonList = PersonBL.loadPersons();
		if (replacementPersonList!=null && personIDs!=null) {
			Iterator<TPersonBean> iterator = replacementPersonList.iterator();
			while (iterator.hasNext()) {
				TPersonBean personBean = iterator.next();
				if (personIDs.contains(personBean.getObjectID())) {
					iterator.remove();
				}
			}
		}
		return replacementPersonList;
	}

	/**
	 * Replaces the dependences with a new personID and
	 * deletes the old personID from the TPerson table
	 * @param oldPersonID
	 * @param newPersonID
	 */
	/*public static void replaceAndDeletePerson(String selectedPersonIDs, Integer newPersonID) {
		if (newPersonID!=null && selectedPersonIDs!=null) {
			List<Integer> selectedPersonIDList = getPersonIDList(selectedPersonIDs);
			replaceAndDeletePerson(selectedPersonIDList,newPersonID);
		}
	}*/

	public static void replaceAndDeletePerson(List<Integer> selectedPersonIDList, Integer newPersonID, Integer loggedUser) {
		if (newPersonID!=null && selectedPersonIDList!=null && !selectedPersonIDList.isEmpty()) {
			for (Integer oldPersonID : selectedPersonIDList) {
				//delete the private entities before replacement
				deletePrivateEntities(oldPersonID);
				deletePrivateProjects(oldPersonID, loggedUser);
				personDAO.replacePerson(oldPersonID, newPersonID);
				personDAO.replaceUserPicker(oldPersonID, newPersonID);
				personDAO.replaceHistoryPerson(oldPersonID, newPersonID, true);
				personDAO.replaceHistoryPerson(oldPersonID, newPersonID, false);
				deletePerson(oldPersonID);
			}
			ApplicationBean.getApplicationBean().setActualUsers();
		}
	}

	/**
	 * Gets the selected personIDs form the comma separated string
	 * @param selectedPersonIDs
	 * @return
	 */
	public static List<Integer> getPersonIDList(String selectedPersonIDs) {
		if (selectedPersonIDs!=null) {
			return GeneralUtils.createIntegerListFromStringArr(selectedPersonIDs.split(","));
		}
		return new LinkedList<Integer>();
	}

	/**
	 * Activate/deactivate the selected persons
	 * @param selectedPersonIDs
	 * @param locale
	 * @param deactivate
	 * @return
	 */
	static String activateDeactivate(String selectedPersonIDs, Locale locale, boolean deactivate) {
		if (selectedPersonIDs!=null) {
			List<Integer> selectedPersonIDList = getPersonIDList(selectedPersonIDs);
//			if(deactivate == false) {
//				PersonConfigBL.setToken(selectedPersonIDs);
//			}
			if (selectedPersonIDList!=null && !selectedPersonIDList.isEmpty()) {
				if (deactivate) {
					if (allSysAdminsInvolved(selectedPersonIDList)) {
						String errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.user.manage.err.deactivateAdmins", locale);
						return JSONUtility.encodeJSONFailure(errorMessage);
					}
				} else {

					String usersExceededMessage = usersExceeded(selectedPersonIDList, locale, null, true);
					if (usersExceededMessage!=null) {
						return JSONUtility.encodeJSONFailure(usersExceededMessage);
					}
				}
				PersonBL.activateDeactivatePersons(selectedPersonIDList, deactivate);
				ApplicationBean appBean = ApplicationBean.getApplicationBean();
			 	appBean.setActualUsers();
			}
		}
	 	return JSONUtility.encodeJSONSuccess();
	}

	/**
	 * Change the licensed feature for a person
	 * @param selectedPersonIDs
	 * @param locale
	 * @param deactivate
	 * @return
	 */
	static String changeFeature(Integer personID, String featureID, boolean featureValue, Locale locale) {
		if (personID!=null && featureID!=null) {
			UserFeatureBL.actualizeUserFeature(personID, featureID, featureValue);
		}
		int actualyHavingFeature = UserFeatureBL.countUsersWithFeature(featureID, true);
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, ProfileJSON.JSON_FIELDS.activeWithFeature, actualyHavingFeature);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.SUCCESS, true, true);
		sb.append("}");
		return sb.toString();
	}

	static String cokpitAssignment(String selectedPersonIDs, Locale locale, Integer dashboardID) {
		if (selectedPersonIDs!=null&&dashboardID!=null) {
			List<Integer> selectedPersonIDList = getPersonIDList(selectedPersonIDs);
			List<TPersonBean> personBeanList=PersonBL.loadByKeys(selectedPersonIDList);
			for(TPersonBean user:personBeanList){
				DashboardAssignBL.resetDashboard(user, dashboardID);
			}
		}
		return JSONUtility.encodeJSONSuccess();
	}

	/**
	 * Sync LDAP
	 * @return
	 */
	static String syncLdap() {
		try {
			LdapSynchronizerJob.synchronizeWithLdap();
		} catch (Exception e) {
			LOGGER.error("Problem with running scheduler " + e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return JSONUtility.encodeJSONSuccess();
	}

	/**
	 * Verifies if LDAP is activated
	 * @return
	 */
	static String getLdapIsOn() {
		Boolean isLdapOn = ApplicationBean.getApplicationBean().getSiteBean().getIsLDAPOnBool();
		return JSONUtility.encodeJSONBoolean(isLdapOn);
	}

	/**
	 * Set the user level for the selected persons
	 * @param selectedPersonIDs
	 * @param locale
	 * @param userLevel
	 * @return
	 */
	static String setUserLevel(String selectedPersonIDs, TPersonBean currentUser, Locale locale, Integer userLevel) {
		if (selectedPersonIDs!=null) {
			List<Integer> selectedPersonIDList = getPersonIDList(selectedPersonIDs);
			String systemAdminChangeError = getSystemAdminChangeError(currentUser, selectedPersonIDList, locale);
			if (systemAdminChangeError!=null) {
				return JSONUtility.encodeJSONFailure(systemAdminChangeError);
			}
			String resetTheLastSystemAdmin = resetLastSystemAdmin(userLevel, selectedPersonIDList, locale);
			if (resetTheLastSystemAdmin!=null) {
				return JSONUtility.encodeJSONFailure(resetTheLastSystemAdmin);
			}
			String usersExceededMessage = usersExceeded(selectedPersonIDList, locale, userLevel, false);
			if (usersExceededMessage!=null) {
				return JSONUtility.encodeJSONFailure(usersExceededMessage);
			}
			ApplicationBean appBean = ApplicationBean.getApplicationBean();
			PersonBL.setUserLevelPersons(selectedPersonIDList, userLevel);
			appBean.setActualUsers();
		}
	 	return JSONUtility.encodeJSONSuccess();

	}

	/**
	 * The last system administrator cannot be revoked
	 * @param userLevel
	 * @param selectedPersonIDList
	 * @param locale
	 * @return
	 */
	private static String resetLastSystemAdmin(Integer userLevel, List<Integer> selectedPersonIDList, Locale locale) {
		if (!TPersonBean.USERLEVEL.SYSADMIN.equals(userLevel)) {
			if (allSysAdminsInvolved(selectedPersonIDList)) {
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.user.manage.err.revokeAdmins", locale);
			}
		}
		return null;
	}

	/**
	 * Whether a non system administrator (probably system manager) tries to change a system administrator
	 * @param currentUser
	 * @param selectedPersonIDList
	 * @param locale
	 * @return
	 */
	private static String getSystemAdminChangeError(TPersonBean currentUser, List<Integer> selectedPersonIDList, Locale locale) {
		Integer currentUserUserLevel = currentUser.getUserLevel();
		if (currentUserUserLevel==null || !currentUserUserLevel.equals(TPersonBean.USERLEVEL.SYSADMIN)) {
			for (Integer selectedPersonID : selectedPersonIDList) {
				TPersonBean selectedPersonBean = LookupContainer.getPersonBean(selectedPersonID);
				if (selectedPersonBean!=null) {
					Integer selectedPersonUserLevel = selectedPersonBean.getUserLevel();
					if (selectedPersonUserLevel!=null && selectedPersonUserLevel.equals(TPersonBean.USERLEVEL.SYSADMIN)) {
						return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.user.manage.err.systemAdmin", locale);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Whether the number of a user category (all, full, gantt) exceeded the number allowed in license
	 * @param selectedPersonIDList
	 * @param locale
	 * @param activateDisabledUsers
	 * @return
	 */
	public static String usersExceeded(List<Integer> selectedPersonIDList, Locale locale, Integer newUserLevel, boolean activateDisabledUsers) {
		ApplicationBean appBean = ApplicationBean.getApplicationBean();
		//max allowed values
		int maxNumberOfFullUsers = appBean.getMaxNumberOfFullUsers();
		int maxNumberOfEasyUsers = appBean.getMaxNumberOfLimitedUsers();
		//actual values
		int actualFullActive = appBean.getFullActive();
		int actualEasyActive = appBean.getLimitedActive();
		String exceedAllActiveMessage = activeUsersExceeded(actualFullActive + actualEasyActive, selectedPersonIDList,
				maxNumberOfFullUsers + maxNumberOfEasyUsers , locale, newUserLevel, false, activateDisabledUsers);
		if (exceedAllActiveMessage!=null) {
			LOGGER.info("Total number of active users exceeded");
			return exceedAllActiveMessage;
		}
		String exceedFullActiveMessage = activeUsersExceeded(actualFullActive, selectedPersonIDList,
				maxNumberOfFullUsers, locale, newUserLevel, true, activateDisabledUsers);
		if (exceedFullActiveMessage!=null) {
			LOGGER.info("Total number of active full users exceeded");
			return exceedFullActiveMessage;
		}

		if (newUserLevel!=null && newUserLevel.equals(TPersonBean.USERLEVEL.CLIENT)) {
			LOGGER.info("Remove licensed features for client users");
			UserFeatureBL.deleteFeaturesForPersons(selectedPersonIDList);
		}
		if (activateDisabledUsers) {
			List<LicensedFeature> licensedFeatures = UserFeatureBL.getLicensedFeatures();
			if (licensedFeatures!=null) {
				for (LicensedFeature licensedFeature : licensedFeatures) {
					String featureExceeded = UserFeatureBL.featureExceeded(selectedPersonIDList, licensedFeature.getNumberOfUsers(), licensedFeature.getFeatureId(), licensedFeature.getFeatureName(),  locale);
					if (featureExceeded!=null) {
						return featureExceeded;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Whether the number of users is exceeded
	 * @param actualActive
	 * @param selectedPersonIDList
	 * @param maxActive
	 * @param locale
	 * @param newUserLevel
	 * @param onlyFull only full active users or all active users
	 * @param activateDisabledUsers wheter the users will be activated or the user level is set
	 * @return error message if exceeded
	 */
	private static String activeUsersExceeded(int actualActive, List<Integer> selectedPersonIDList, int maxActive,
			Locale locale, Integer newUserLevel, boolean onlyFull, boolean activateDisabledUsers) {
		int numberOfPersonsToChange = 0;
		boolean addNewPerson = false;
		if (selectedPersonIDList==null || selectedPersonIDList.isEmpty()) {
			//add a new person
			numberOfPersonsToChange = 1;
			addNewPerson = true;
		} else {
			numberOfPersonsToChange = selectedPersonIDList.size();
		}
		if (actualActive + numberOfPersonsToChange > maxActive) {
			//possible exceeding
			int countActiveToAdd = 0;
			if (addNewPerson && addInCount(onlyFull, newUserLevel)) {
				//add a new person
				countActiveToAdd++;
			} else {
				//set user level for users or enable users
				List<TPersonBean> selectedPersons = PersonBL.loadByKeys(selectedPersonIDList);
				if (selectedPersons!=null) {
					for (TPersonBean personBean : selectedPersons) {
						Integer personUserLevel = personBean.getUserLevel();
						boolean disabled = personBean.isDisabled();
						if (activateDisabledUsers) {
							//enable selected users
							if (disabled && addInCount(onlyFull, personUserLevel)) {
								//count only if the user to enable was disabled before
								countActiveToAdd++;
							}
						} else {
							if (!disabled && newUserLevel!=null && !newUserLevel.equals(TPersonBean.USERLEVEL.CLIENT)) {
								//change user level of the selected active (not disabled) users
								if (onlyFull) {
									//count only the users which are about to be changed from easy to full
									if (personUserLevel==null || TPersonBean.USERLEVEL.CLIENT.equals(personUserLevel)) {
										countActiveToAdd++;
									}
								}
							}
						}
					}
				}
			}
			if (countActiveToAdd>0 && actualActive + countActiveToAdd > maxActive) {
				//real exceeding
				String errorKey = null;
				if (onlyFull) {
					errorKey = "admin.user.manage.err.fullUsersExceed";
				} else {
					errorKey= "admin.user.manage.err.usersExceed";
				}
				return LocalizeUtil.getParametrizedString(errorKey, new Object[] {maxActive, actualActive}, locale);
			}
		}
		return null;
	}

	/**
	 * Whether it should be counted
	 * @param onlyFull
	 * @param newUserLeve
	 * @return
	 */
	private static boolean addInCount(boolean onlyFull, Integer newUserLevel) {
		if (onlyFull) {
			//count only the full users
			if (newUserLevel==null || !newUserLevel.equals(TPersonBean.USERLEVEL.CLIENT)) {
				return true;
			} else {
				return false;
			}
		} else {
			//count all users
			return true;
		}
	}



	/**
	 * Whether all system admins are selected
	 * @param selectedPersonIDList
	 * @return
	 */
	private static boolean allSysAdminsInvolved(List<Integer> selectedPersonIDList) {
		List<TPersonBean> personBeanList = PersonBL.loadByKeys(selectedPersonIDList);
		int adminsToRevoke = 0;
		for (Iterator<TPersonBean> iterator = personBeanList.iterator(); iterator.hasNext();) {
			TPersonBean personBean = iterator.next();
			if (personBean.isSysAdmin()) {
				adminsToRevoke++;
			}
		}
		if (adminsToRevoke>0) {
			List<TPersonBean> sysAdminPersons = PersonBL.loadSystemAdmins();
			return adminsToRevoke==sysAdminPersons.size();
		}
		return false;
	}

}
