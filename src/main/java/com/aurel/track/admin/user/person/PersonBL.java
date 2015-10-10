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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessControlBL;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.category.filter.MenuitemFilterBL;
import com.aurel.track.admin.customize.mailTemplate.MailTemplateBL;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.user.department.DepartmentBL;
import com.aurel.track.admin.user.person.feature.UserFeatureBL;
import com.aurel.track.admin.user.profile.ProfileBL;
import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PersonDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.listFields.NotLocalizedListIndexer;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.emailHandling.MailSender;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.IEventSubscriber;
import com.opensymphony.xwork2.ActionContext;
import com.trackplus.license.LicenseManager;
import com.trackplus.license.LicensedFeature;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class PersonBL {
	private static PersonDAO personDAO = DAOFactory.getFactory().getPersonDAO();
	private static final Logger LOGGER = LogManager.getLogger(PersonBL.class);

	public static String USER_ICON_CLASS = "user16";
	public static String GROUP_ICON_CLASS = "group16";

	public static TPersonBean loadByPrimaryKey(Integer objectID) {
		return personDAO.loadByPrimaryKey(objectID);
	}

	/**
	 * Load all persons substituted by a person
	 * @param substituteID
	 * @return
	 */
	public static List<Integer> loadSubstitutedPersons(Integer substituteID) {
		return personDAO.loadSubstitutedPersons(substituteID);
	}

	/**
	 * Gets the persons who need reminder e-mails
	 * @param date
	 * @return
	 */
	public static List<TPersonBean> loadDailyReminderPersons(Date date) {
		return personDAO.loadDailyReminderPersons(date);
	}

	/**
	 * Gets the persons who need basket reminder e-mails
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static List<TPersonBean> loadBasketReminderPersons(Date fromDate, Date toDate) {
		return personDAO.loadBasketReminderPersons(fromDate, toDate);
	}

	/**
	 * Load all system administrators
	 */
	public static List<TPersonBean> loadSystemAdmins() {
		return personDAO.loadSystemAdmins();
	}

	/**
	 * Load the personBean either by firstName and lastName or by loginName (for group)
	 * @param label
	 * @return
	 */
	public static TPersonBean loadByLabel(String label) {
		if (label==null) {
			return null;
		}
		String[] parts = label.split("\\s|,");
		if (parts==null) {
			return null;
		}
		TPersonBean personBean;
		if (parts.length>1) {
			personBean = personDAO.loadByFirstNameLastName(parts[1].trim(), parts[0].trim());
			if (personBean!=null) {
				return personBean;
			}
		}
		personBean = loadByLoginName(label.trim());
		if (personBean!=null) {
			return personBean;
		}
		return loadGroupByName(label.trim());
	}


	/**
	 * Loads a user by primary key and returns the TPersonBean set with
	 * isSysAdmin, isProjAdmin and isGuest flags
	 * @param loginName
	 * @return
	 */
	public static TPersonBean loadByLoginName(String loginName) {
		return personDAO.loadByLoginName(loginName);
	}

	public static TPersonBean loadGroupByName(String loginName) {
		return personDAO.loadGroupByName(loginName);
	}

	/**
	 * Gets the person bean(s) by e-mail
	 * @param email
	 * @return
	 */
	public static List<TPersonBean> loadByEmail(String email) {
		return personDAO.loadByEmail(email);
	}

	/**
	 * Gets the person bean by Token password
	 * @param tokenPasswd the system generated expiration token for a newly registered user
	 * @return
	 */
	public static TPersonBean loadByTokenPasswd(String tokenPasswd) {
		return personDAO.loadByTokenPasswd(tokenPasswd);
	}

	/**
	 * Gets the person bean by Token password
	 * @param tokenPasswd the system generated expiration token for a new password request
	 * @return
	 */
	public static TPersonBean loadByForgotPasswordToken(String tokenPasswd) {
		return personDAO.loadByForgotPasswordToken(tokenPasswd);
	}

	/**
	 * Whether a combination of lastname, firstname and email already exists
	 * @param lastname
	 * @param firstname
	 * @param email
	 * @param personKey
	 * @return
	 */
	public static boolean nameAndEmailExist(String lastname, String firstname, String email, Integer personKey) {
		return personDAO.nameAndEmailExist(lastname, firstname, email, personKey);
	}

	/**
	 * Activate or deactivate the persons
	 * @param persons
	 * @param deactivate
	 */
	public static void activateDeactivatePersons(List<Integer> persons, boolean deactivate) {
		if (persons!=null) {
			personDAO.activateDeactivatePersons(persons, deactivate);
			for (Integer personID : persons) {
				LookupContainer.reloadPerson(personID);
				//cache update in other cluster nodes (sortOrder, icon) but no lucene update is implied
				ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PERSON, personID, ClusterBL.CHANGE_TYPE.UPDATE_CACHE);
			}
		}
	}

	/**
	 * Set the user level for persons
	 * @param persons
	 * @param userLevel
	 */
	public static void setUserLevelPersons(List<Integer> persons, Integer userLevel) {
		if (persons!=null) {
			personDAO.setUserLevelPersons(persons, userLevel);
			for (Integer personID : persons) {
				LookupContainer.reloadPerson(personID);
				//cache update in other cluster nodes (sortOrder, icon) but no lucene update is implied
				ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PERSON, personID, ClusterBL.CHANGE_TYPE.UPDATE_CACHE);
			}
		}
	}

	/**
	 * Get the persons with any RACI role (informant or consultant)
	 * for a workItemKey
	 * @param workItemKey
	 * @return
	 */
	public static List<TPersonBean> loadNotifyThroughRaci(Integer workItemKey) {
		return personDAO.loadNotifyThroughRaci(workItemKey);
	}

	/**
	 * Get the persons who have at least one work in a project or release and listType
	 * @param entityID
	 * @param entityType
	 * @param listType
	 * @return
	 */
	public static List<TPersonBean> getPersonsWithWorkInProject(Integer entityID, int entityType, Integer listType) {
		return personDAO.getPersonsWithWorkInProject(entityID, entityType, listType);
	}

	/**
	 * Whether the db and ldap persons are synchonized
	 * @param dbPersonBean
	 * @param ldapPerson
	 * @return
	 */
	public static boolean isLdapPersonSame(TPersonBean dbPersonBean, TPersonBean ldapPerson) {
		String ldapFirstName = ldapPerson.getFirstName();
		if (ldapFirstName!=null && !"".equals(ldapFirstName) && EqualUtils.notEqual(dbPersonBean.getFirstName(), ldapPerson.getFirstName())) {
			//do not overwrite the track+ firstName with an empty ldap firstName
			LOGGER.debug("Genji firstname: " + dbPersonBean.getFirstName() + "- ldap firstname: " + ldapPerson.getFirstName());
			return false;
		}
		String ldapLastName = ldapPerson.getLastName();
		if (ldapLastName!=null && !"".equals(ldapLastName) && EqualUtils.notEqual(dbPersonBean.getLastName(), ldapLastName)) {
			//do not overwrite the track+ lastName with an empty ldap lastName
			LOGGER.debug("Genji lastname: " + dbPersonBean.getLastName() + "- ldap lastname: " + ldapPerson.getLastName());
			return false;
		}
		String ldapEmail = ldapPerson.getEmail();
		if (ldapEmail!=null && !"".equals(ldapEmail) && EqualUtils.notEqual(dbPersonBean.getEmail(), ldapEmail)) {
			//do not overwrite the track+ email with an empty ldap email
			LOGGER.debug("Genji e-mail: " + dbPersonBean.getEmail() + "- ldap e-mail: " + ldapPerson.getEmail());
			return false;
		}
		String ldapPhone = ldapPerson.getPhone();
		if (ldapPhone!=null && !"".equals(ldapPhone) && EqualUtils.notEqual(dbPersonBean.getPhone(), ldapPhone)) {
			//do not overwrite the track+ phone with an empty ldap phone
			LOGGER.debug("Genji phone: " + dbPersonBean.getPhone() + "- ldap phone: " + ldapPerson.getPhone());
			return false;
		}
		return true;
	}

	/**
	 * Update ldap attributes on an existing person
	 * @param personBean
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @return
	 */
	public static TPersonBean updateLdapPerson(TPersonBean personBean, String firstName, String lastName, String email, String phone) {
		if (personBean!=null) {
			if (firstName!=null &&  !"".equals(firstName)) {
				//do not overwrite the track+ firstName with an empty ldap firstName
				personBean.setFirstName(firstName);
			}
			if (lastName!=null &&  !"".equals(lastName)) {
				//do not overwrite the track+ lastName with an empty ldap lastName
				personBean.setLastName(lastName);
			}
			if (email!=null &&  !"".equals(email)) {
				//do not overwrite the track+ email with an empty ldap e-mail
				personBean.setEmail(email);
			}
			if (phone!=null && !"".equals(phone)) {
				//do not overwrite the track+ phone with an empty ldap phone
				personBean.setPhone(phone);
			}
		}
		return personBean;
	}

	/**
	 * Create a new person from ldap
	 * @param loginName
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @return
	 */
	public static TPersonBean createLdapPerson(String loginName, String firstName, String lastName, String email, String phone) {
		TPersonBean personBean = new TPersonBean();
		personBean.setLoginName(loginName);
		personBean.setFirstName(firstName);
		personBean.setLastName(lastName);
		personBean.setEmail(email);
		personBean.setPhone(phone);
		personBean.setIsLdapUser();
		personBean.setPrefEmailType("HTML");
		personBean.setPrefLocale("");
		personBean.setEnableQueryLayout(true);
		personBean.setUserLevel(TPersonBean.USERLEVEL.FULL);
		personBean.setDepartmentID(DepartmentBL.getDefaultDepartment());
		return personBean;
	}

	/**
	 * Creates a new user
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @return
	 */
	public static TPersonBean createMsProjectImportNewUser(String username, String firstName, String lastName, String email) {
		TPersonBean personBean = new TPersonBean();
		personBean.setLoginName(username);
		personBean.setFirstName(firstName);
		personBean.setLastName(lastName);
		personBean.setEmail(email);
		String pass = RandomStringUtils.randomAlphanumeric(8);
		personBean.setPasswd(pass);
		personBean.setPrefEmailType("HTML");
		personBean.setPrefLocale("");
		personBean.setEnableQueryLayout(true);
		personBean.setUserLevel(TPersonBean.USERLEVEL.FULL);
		personBean.setDepartmentID(DepartmentBL.getDefaultDepartment());
		return personBean;
	}

	/**
	 * Saves a personBean in the TPerson table
	 * @param personBean
	 * @return
	 */
	public static Integer save(TPersonBean personBean) {
		boolean isNew = personBean.getObjectID() == null;
		Integer personID = saveSimple(personBean);
		if (isNew) {
			personBean.setObjectID(personID);
			NotLocalizedListIndexer.getInstance().addLabelBean(
					personBean, LuceneUtil.LOOKUPENTITYTYPES.PERSONNAME, isNew);
		} else {
			NotLocalizedListIndexer.getInstance().updateLabelBean(
					personBean, LuceneUtil.LOOKUPENTITYTYPES.PERSONNAME);
		}
		//cache and possible lucene update in other cluster nodes
		//otherwise it is already marked in saveSimple
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PERSON, personID, ClusterMarkChangesBL.getChangeTypeByAddOrUpdate(isNew));
		return personID;
	}

	/**
	 * Saves a personBean
	 * @param personBean
	 * @return
	 */
	public static Integer saveSimple(TPersonBean personBean) {
		Integer newPk= personDAO.save(personBean);
		LookupContainer.reloadPerson(newPk);
		//cache update in other cluster nodes (sortOrder, icon) but no lucene update is implied
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PERSON, newPk, ClusterBL.CHANGE_TYPE.UPDATE_CACHE);
		//TODO refactor this code
		if (ActionContext.getContext()!=null) {
			Map<String,Object> session=ActionContext.getContext().getSession();
			if (session!=null) {
				TPersonBean logonPerson = (TPersonBean) session.get(Constants.USER_KEY);
				if(logonPerson!=null){
					if(logonPerson.getObjectID().equals(newPk)){
						PersonBL.setLicensedFeatures(personBean);
						session.put(Constants.USER_KEY,personBean);
					}
				}
			}
		}
		return newPk;
	}

	/**
	 * Set the licensed featurs map for user
	 * @param personBean
	 */
	public static void setLicensedFeatures(TPersonBean personBean) {
		LicenseManager licenseManager = ApplicationBean.getApplicationBean().getLicenseManager();
		if (licenseManager!=null) {
			List<LicensedFeature> licensedFeatures = licenseManager.getLicensedFeatures(true);
			if (licensedFeatures!=null) {
				Map<String, Boolean> licensedFeaturesMap = new HashMap<String, Boolean>();
				for (LicensedFeature licensedFeature : licensedFeatures) {
					String featureID = licensedFeature.getFeatureId();
					licensedFeaturesMap.put(featureID, Boolean.valueOf(UserFeatureBL.hasUserFeature(personBean.getObjectID(), featureID)));
				}
				personBean.setLicensedFeaturesMap(licensedFeaturesMap);
			}
		}
	}
	
	/**
	 * Saves a person and if it is a new person then adds also the standard filter subscriptions
	 * @param personBean
	 * @return
	 */
	public static Integer saveAndAddMenuFilters(TPersonBean personBean) {
		boolean isNew = personBean.getObjectID()==null;
		Integer personID = save(personBean);
		if (isNew) {
			List<Integer> filterIDs = MenuitemFilterBL.getFilterIDsToSubscribe();
			MenuitemFilterBL.subscribePersonsToFilters(personID, filterIDs);
		}
		return personID;
	}

	/**
	 * @return List of Persons with the specific keys
	 * @throws Exception
	 */
	public static List<TPersonBean> loadByKeys(List<Integer> personIDs) {
		return personDAO.loadByKeys(personIDs);
	}


	/**
	 * Load all persons
	 * @return
	 */
	public static List<TPersonBean> loadPersons() {
		return personDAO.loadPersons();
	}

	/**
	 * Load all clients
	 * @return
	 */
	public static List<TPersonBean> loadAllClients() {
		return personDAO.loadAllClients();
	}

	/**
	 * Load all users except clients
	 * @return
	 */
	public static List<TPersonBean> loadAllUsers() {
		return personDAO.loadAllUsers();
	}



	/**
	 * Load the persons as map
	 * @return
	 */
	public static Map<Integer,TPersonBean> loadPersonsAsMap() {
		List<TPersonBean> personBeanList= personDAO.loadPersons();
		Map<Integer,TPersonBean> map=new HashMap<Integer, TPersonBean>();
		if(personBeanList!=null && !personBeanList.isEmpty()){
			for(TPersonBean personBean:personBeanList){
				map.put(personBean.getObjectID(),personBean);
			}
		}
		return map;
	}

	/**
	 * Loads the active persons
	 * @param actualValue
	 * @return
	 */
	public static List<TPersonBean> loadActivePersons() {
		return personDAO.loadActivePersons();
	}

	/**
	 * Load the active persons and groups
	 * @return
	 */
	public static List<TPersonBean> loadActivePersonsAndGroups() {
		return personDAO.loadActivePersonsAndGroups();
	}

	/**
	 * Load all groups
	 * @return
	 */
	public static List<TPersonBean> loadGroups() {
		return personDAO.loadGroups();
	}

	/**
	 * Loads all persons and groups
	 * @return
	 */
	public static List<TPersonBean> loadPersonsAndGroups() {
		return personDAO.loadPersonsAndGroups();
	}

	/**
	 * Loads the persons with any of the specified roles in a project
	 * @param projectIDs
	 * @param roleIDs
	 * @return
	 */
	public static List<TPersonBean> loadByProjectAndRoles(List<Integer> projectIDs, List<Integer> roleIDs) {
		return personDAO.loadByProjectAndRoles(projectIDs, roleIDs);
	}

	/**
	 * Gets a list of personBeans ordered by isGroup, lastname, firstname, loginname
	 * @param objectIDs
	 * @return
	 */
	public static List<TPersonBean> loadSortedPersonsOrGroups(List<Integer> objectIDs) {
		return personDAO.loadSortedPersonsOrGroups(objectIDs);
	}

	public static boolean isProjectAdmin(Integer personID, Integer projectID) {
		return AccessBeans.isPersonProjectAdminForProject(personID, projectID, true);
	}

	/**
	 * Loads a user by primary key and returns the TPersonBean set with
	 * isSysAdmin, isProjAdmin and isGuest flags
	 * @param loginName
	 * @return
	 */
	public static TPersonBean loadByLoginNameWithRights(String loginName) {
		TPersonBean personBean = loadByLoginName(loginName);
		if (personBean==null || personBean.getObjectID()==null) {
			return null;
		}
		if (personBean.isSys()) {
			personBean.setProjAdmin(true);
		} else {
			if (AccessControlBL.hasPersonRightInNonPrivateProject(personBean.getObjectID(),
					new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN})) {
				personBean.setProjAdmin(true);
			}
			personBean.setAnonimous(isAnonymous(personBean));
		}
		return personBean;
	}

	public static boolean isAnonymous(TPersonBean personBean) {
		return ApplicationBean.getInstance().getSiteBean().getAutomaticGuestLogin() && personBean!=null &&
				TPersonBean.GUEST_USER.equals(personBean.getLoginName()) &&
			!personBean.isDisabled();
	}

	public static TPersonBean getAnonymousIfActive() {
		if (!ApplicationBean.getApplicationBean().getSiteBean().getAutomaticGuestLogin()) {
			return null;
		}
		TPersonBean personBean = loadByLoginNameWithRights(TPersonBean.GUEST_USER);
		if (personBean!=null && !personBean.isDisabled()) {
				//not deleted
				return personBean;
		}
		return null;
	}

	public static Integer countFullInactive() {
		return personDAO.countUsers(true, false);
	}

	public static Integer countFullActive() {
		return personDAO.countUsers(false, false);
	}

	public static Integer countLimitedActive() {
		return personDAO.countUsers(false, true);
	}

	public static Integer countLimitedInactive() {
		return personDAO.countUsers(true, true);
	}

	/**
	 * Count the user by user levels
	 * @param userLevels
	 * @param disabled
	 * @return
	 */
	public static int countByUserLevels(List<Integer> userLevels, boolean disabled) {
		return personDAO.countByUserLevels(userLevels, disabled);
	}

	/**
	 * Gets the list of logged in users
	 * @return
	 */
	public static List<TPersonBean> getLoggedInUsers() {
		return personDAO.getLoggedInUsers();
	}

	/**
	 * Returns the real persons which are directly present in the list
	 * The list may contain both personID-s and groupID-s
	 * @param personIDs
	 * @param excludeInactive
	 * @param currentOption
	 * @return
	 */
	public static List<TPersonBean> getDirectPersons(List<Integer> personIDs, boolean excludeInactive, Integer currentOption) {
		return personDAO.getDirectPersons(personIDs, excludeInactive, currentOption);
	}


	/**
	 * Returns the real persons which are indirectly (through group) present in the list
	 * The list may contain both personID-s and groupID-s
	 * @param personIDs
	 * @param excludeInactive
	 * @param currentOption
	 *@return
	 */
	public static List<TPersonBean> getIndirectPersons(List<Integer> personIDs,
			boolean excludeInactive, Integer currentOption) {
		return personDAO.getIndirectPersons(personIDs, excludeInactive, currentOption);
	}

	/**
	 * Returns the real groups which are directly present in the list
	 * The list may contain both personID-s and groupID-s
	 * @param personIDs
	 * @param excludeInactive
	 * @return
	 */
	public static List<TPersonBean> getDirectGroups(List<Integer> personIDs, boolean excludeInactive) {
		return personDAO.getDirectGroups(personIDs, excludeInactive);
	}

	/**
	 * Returns the real persons which are directly or indirectly (through group) present in the object array
	 * The object array may contain both personID-s and groupID-s
	 * @param personKeys
	 * @param excludeDirectInactive
	 * @param excludeIndirectInactive
	 * @return
	 */
	public static List<TPersonBean> getDirectAndIndirectPersons(List<Integer> personIDs,
			boolean excludeDirectInactive, boolean excludeIndirectInactive, Integer currentOption) {
		List<TPersonBean> personsDirect = getDirectPersons(personIDs, excludeDirectInactive, currentOption);
		List<TPersonBean> personsIndirect = getIndirectPersons(personIDs, excludeIndirectInactive, currentOption);
		Set<Integer> personsSet = new HashSet<Integer>();
		personsSet.addAll(GeneralUtils.createIntegerListFromBeanList(personsDirect));
		personsSet.addAll(GeneralUtils.createIntegerListFromBeanList(personsIndirect));
		return loadByKeys(GeneralUtils.createIntegerListFromCollection(personsSet));
	}

	/**
	 * Returns the real persons which are directly or indirectly (through group) present in
	 * the object array and the real groups which are directly present in the object array
	 * The object array may contain both personID-s and groupID-s
	 * @param personKeys
	 * @param excludeDirectInactive
	 * @param excludeIndirectInactive
	 * @return
	 */
	public static List<TPersonBean> getDirectAndIndirectPersonsAndGroups(List<Integer> personIDs,
			boolean excludeDirectInactive, boolean excludeIndirectInactive, Integer currentOption) {
		List<TPersonBean> personsDirect = getDirectPersons(personIDs, excludeDirectInactive, currentOption);
		List<TPersonBean> personsIndirect = getIndirectPersons(personIDs, excludeIndirectInactive, currentOption);
		List<TPersonBean> groups = getDirectGroups(personIDs, excludeDirectInactive);
		Set<Integer> personsSet = new HashSet<Integer>();
		personsSet.addAll(GeneralUtils.createIntegerListFromBeanList(personsDirect));
		personsSet.addAll(GeneralUtils.createIntegerListFromBeanList(personsIndirect));
		personsSet.addAll(GeneralUtils.createIntegerListFromBeanList(groups));
		return loadByKeys(GeneralUtils.createIntegerListFromCollection(personsSet));
	}

	/**
	 * Get the personBeans form a list of personIDs
	 * depending on person's category
	 * @param persons
	 * @param personCategory
	 * @param excludeDirectInactive
	 * @param excludeIndirectInactive
	 * @return
	 */
	public static List<TPersonBean> getPersonsByCategory(Set<Integer> persons, int personCategory,
			boolean excludeDirectInactive, boolean excludeIndirectInactive, Integer currentOption) {
		if (currentOption!=null) {
			if (persons== null) {
				persons = new HashSet<Integer>();
			}
			persons.add(currentOption);
		}
		List<Integer> personIDList = GeneralUtils.createIntegerListFromCollection(persons);
		switch (personCategory) {
		case TPersonBean.PERSON_CATEGORY.ALL:
			return getDirectAndIndirectPersonsAndGroups(personIDList,
					excludeDirectInactive, excludeIndirectInactive, currentOption);
		case TPersonBean.PERSON_CATEGORY.ALL_DIRECT:
			return loadByKeys(personIDList);
		case TPersonBean.PERSON_CATEGORY.ALL_PERSONS:
			return getDirectAndIndirectPersons(personIDList,
					excludeDirectInactive, excludeIndirectInactive, currentOption);
		case TPersonBean.PERSON_CATEGORY.DIRECT_PERSONS:
			return getDirectPersons(personIDList, excludeDirectInactive, currentOption);
		case TPersonBean.PERSON_CATEGORY.GROUPS:
			return getDirectGroups(personIDList, excludeDirectInactive);
		case TPersonBean.PERSON_CATEGORY.INDIRECT_PERSONS:
			return getIndirectPersons(personIDList, excludeIndirectInactive, currentOption);
		default:
			return loadByKeys(personIDList);
		}
	}

	/**
	 * Load the persons belonging to a group
	 * @param groupID
	 * @return
	 */
	public static List<TPersonBean> loadPersonsForGroup(Integer groupID) {
		return personDAO.loadPersonsForGroup(groupID);
	}

	/**
	 * Load the persons belonging to any group from an array of groups
	 * @param groupKeys
	 * @return
	 */
	public static List<TPersonBean> loadPersonsForGroups(List<Integer> groupKeys) {
		return personDAO.loadPersonsForGroups(groupKeys);
	}

	/**
	 * Load the groups a person belongings to
	 * @param personID
	 * @return
	 */
	public static List<TPersonBean> loadGroupsForPerson(Integer personID) {
		return personDAO.loadGroupsForPerson(personID);
	}

	/**
	 * Load the groups any person from personIDs belongs to
	 * @param personIDs
	 * @return
	 */
	public static List<TPersonBean> loadGroupsForPersons(List<Integer> personIDs) {
		return personDAO.loadGroupsForPersons(personIDs);
	}

	/**
	 * Load the persons from a department
	 * @param departmentID
	 * @return
	 */
	public static List<TPersonBean> loadPersonsForDepartment(Integer departmentID) {
		return personDAO.loadPersonsForDepartment(departmentID);
	}

	/**
	 * @return List of personbeans from departments
	 * @throws Exception
	 */
	public static List<TPersonBean> loadByDepartments(Integer[] departmentKeys) {
		return personDAO.loadByDepartments(departmentKeys);
	}


	/**
	 * Loads the persons from the departments
	 * @param departmentIDs
	 * @param currentOptions
	 * @return
	 */
	public static List<TPersonBean> loadByDepartments(List<Integer> departmentIDs, Integer[] currentOptions) {
		return personDAO.loadByDepartments(departmentIDs, currentOptions);
	}

	/***********************************************************************************/
	/*******************************Watcher methods*************************************/
	/***********************************************************************************/
	/**
	 * Gets the direct consulted persons or groups
	 * @param workItemID
	 * @return
	 */
	public static  List<TPersonBean> getDirectConsultants(Integer workItemID) {
		return personDAO.getDirectConsultants(workItemID);
	}

	/**
	 * Gets the direct informed persons or groups
	 * @param workItemID
	 * @return
	 */
	public static  List<TPersonBean> getDirectInformants(Integer workItemID) {
		return personDAO.getDirectInformants(workItemID);
	}

	/**
	 * Get the direct RACI persons or groups
	 * @param workItemKey
	 * @param group
	 * @param raciRole
	 * @return
	 */
	public static List<TPersonBean> getDirectRaciPersons(Integer workItemKey, boolean group, String raciRole) {
		return personDAO.getDirectRaciPersons(workItemKey, group, raciRole);
	}

	/**
	 * Returns the direct consultant persons (no groups)
	 * @param workItemKey
	 * @return
	 */
	public static List<TPersonBean> getDirectConsultantPersons(Integer workItemKey) {
		return personDAO.getDirectRaciPersons(workItemKey, false, RaciRole.CONSULTANT);
	}

	/**
	 * Returns the direct informant persons (no groups)
	 * @param workItemKey
	 * @return
	 */
	public static List<TPersonBean> getDirectInformantPersons(Integer workItemKey) {
		return personDAO.getDirectRaciPersons(workItemKey, false, RaciRole.INFORMANT);
	}

	/**
	 * Returns the direct consultant groups (no persons)
	 * @param workItemKey
	 * @return
	 */
	public static List<TPersonBean> getDirectConsultantGroups(Integer workItemKey) {
		return personDAO.getDirectRaciPersons(workItemKey, true, RaciRole.CONSULTANT);
	}

	/**
	 * Returns the direct informant groups (no persons)
	 * @param workItemKey
	 * @return
	 */
	public static List<TPersonBean> getDirectInformantGroups(Integer workItemKey) {
		return personDAO.getDirectRaciPersons(workItemKey, true, RaciRole.INFORMANT);
	}

	/**
	 * Load the persons/groups which are consultants/informants
	 * for at least one workItem from the array
	 * @param workItemIDs
	 * @param raciRole
	 * @return
	 */
	public static List<TPersonBean> loadUsedConsultantsInformantsByWorkItemIDs(List<Integer> workItemIDs, String raciRole) {
		return personDAO.loadUsedConsultantsInformantsByWorkItemIDs(workItemIDs, raciRole);
	}

	/**
	 * Gets the persons who added attachment for any of the workItems
	 * @param workItemIDs
	 * @return
	 */
	public static List<TPersonBean> getAttachmentPersons(int[] workItemIDs) {
		return personDAO.getAttachmentPersons(workItemIDs);
	}

	/**
	 * Returns the persons having explicit automail settings in any of the projects having a trigger set with at least one observer field
	 * @param personIDs
	 * @param actionType
	 * @param isCreated
	 * @return
	 */
	public static List<TPersonBean> getObserverPersonsInProjects(List<Integer> personIDs, List<Integer> projects, Integer actionType) {
		return personDAO.getObserverPersonsInProjects(personIDs, projects, actionType);
	}

	/**
	 * Load the persons (direct or indirect)
	 * with manager role for a project and issueType
	 * @param project
	 * @param listType
	 * @return
	 */
	public static List<TPersonBean> loadManagersByProjectAndIssueType(Integer project, Integer listType) {
		return getPersonsWithRightInProjectAndListType(
				project, listType, AccessBeans.AccessFlagIndexes.MANAGER, false, TPersonBean.PERSON_CATEGORY.ALL_PERSONS, null, true, true);
	}

	/**
	 * Load the persons (direct or indirect)
	 * with manager role for a project and issueType including manager anyway
	 * @param project
	 * @param listType
	 * @param manager it should be inculded anyway
	 * @return
	 */
	public static List<TPersonBean> loadManagersByProjectAndIssueType(Integer project, Integer listType, Integer manager) {
		return getPersonsWithRightInProjectAndListType(
				project, listType, AccessBeans.AccessFlagIndexes.MANAGER, false, TPersonBean.PERSON_CATEGORY.ALL_PERSONS, manager, true, true);
	}

	/**
	 * Load the persons (direct or indirect) and groups
	 * with responsible role for a project and issueType
	 * @param project
	 * @param listType
	 * @return
	 */
	public static List<TPersonBean> loadResponsiblesByProjectAndIssueType(Integer project, Integer listType) {
		return getPersonsWithRightInProjectAndListType(
				project, listType, AccessBeans.AccessFlagIndexes.RESPONSIBLE, false, TPersonBean.PERSON_CATEGORY.ALL, null, true, true);
	}

	/**
	 * Load the persons (direct or indirect) and groups with
	 * responsible role for a project and issueType including responsible anyway
	 * @param project
	 * @param listType
	 * @param responsible it should be included anyway
	 * @return
	 */
	public static List<TPersonBean> loadResponsiblesByProjectAndIssueType(Integer project, Integer listType, Integer responsible) {
		return getPersonsWithRightInProjectAndListType(
				project, listType, AccessBeans.AccessFlagIndexes.RESPONSIBLE, false, TPersonBean.PERSON_CATEGORY.ALL, responsible, true, true);
	}

	/**
	 * Load the persons and groups with consultant role for a project and issueType
	 * @param project
	 * @param listType
	 * @return
	 */
	public static List<TPersonBean> loadConsultantPersonsAndGroupsByProjectAndIssueType(Integer project, Integer listType) {
		List<TPersonBean> persons = loadConsultantPersonsByProjectAndIssueType(project, listType);
		if (persons==null) {
			persons = new LinkedList<TPersonBean>();
		}
		List<TPersonBean> groups = loadConsultantGroupsByProjectAndIssueType(project, listType);
		if (groups!=null) {
			persons.addAll(groups);
		}
		return persons;
	}

	/**
	 * Load the persons with consultant role for a project and issueType
	 * @param project
	 * @param listType
	 * @return
	 */
	public static List<TPersonBean> loadConsultantPersonsByProjectAndIssueType(Integer project, Integer listType) {
		return getPersonsWithRightInProjectAndListType(
				project, listType, AccessBeans.AccessFlagIndexes.CONSULTANT, false, TPersonBean.PERSON_CATEGORY.ALL_PERSONS, null, true, true);
	}

	/**
	 * Load the groups with consultant role for a project and issueType
	 * @param project
	 * @param listType
	 * @return
	 */
	public static List<TPersonBean> loadConsultantGroupsByProjectAndIssueType(Integer project, Integer listType) {
		return getPersonsWithRightInProjectAndListType(
				project, listType, AccessBeans.AccessFlagIndexes.CONSULTANT, false, TPersonBean.PERSON_CATEGORY.GROUPS, null, true, true);
	}

	/**
	 * Load the persons and groups with consultant role for a project and issueType
	 * @param project
	 * @param listType
	 * @return
	 */
	public static List<TPersonBean> loadInformantPersonsAndGroupsByProjectAndIssueType(Integer project, Integer listType) {
		List<TPersonBean> persons = loadInformantPersonsByProjectAndIssueType(project, listType);
		if (persons==null) {
			persons = new LinkedList<TPersonBean>();
		}
		List<TPersonBean> groups = loadInformantGroupsByProjectAndIssueType(project, listType);
		if (groups!=null) {
			persons.addAll(groups);
		}
		return persons;
	}

	/**
	 * Load the persons (direct) with informant role for a project and issueType
	 * @param project
	 * @param listType
	 * @return
	 */
	public static List<TPersonBean> loadInformantPersonsByProjectAndIssueType(Integer project, Integer listType) {
		return getPersonsWithRightInProjectAndListType(
				project, listType, AccessBeans.AccessFlagIndexes.INFORMANT, false, TPersonBean.PERSON_CATEGORY.ALL_PERSONS, null, true, true);
	}

	/**
	 * Load the groups with informant role for a project and issueType
	 * @param project
	 * @param listType
	 * @return
	 */
	public static List<TPersonBean> loadInformantGroupsByProjectAndIssueType(Integer project, Integer listType) {
		return getPersonsWithRightInProjectAndListType(
				project, listType, AccessBeans.AccessFlagIndexes.INFORMANT, false, TPersonBean.PERSON_CATEGORY.GROUPS, null, true, true);
	}



	/**
	 * Gets the persons with right in a project for a listType
	 * @param projectID
	 * @param listTypeID might be null, then just in the project
	 * @param right
	 * @param projectAdmin
	 * @param personCategory
	 * @param currentPerson
	 * @param excludeDirectInactive
	 * @param excludeIndirectInactive
	 * @return
	 */
	public static List<TPersonBean> getPersonsWithRightInProjectAndListType(
			Integer projectID, Integer listTypeID,
			int right, boolean projectAdmin, int personCategory, Integer currentPerson,
			boolean excludeDirectInactive, boolean excludeIndirectInactive) {
		int[] arrRights;
		if (projectAdmin && right!=AccessFlagIndexes.PROJECTADMIN) {
			arrRights = new int[] { right, AccessFlagIndexes.PROJECTADMIN };
		} else {
			arrRights = new int[] { right };
		}
		List<TPersonBean> results = new LinkedList<TPersonBean>();
		if (projectID==null) {
			return results;
		}
		//no list type specified
		if (listTypeID==null) {
			return getPersonsWithRightInProject(projectID, arrRights, currentPerson, personCategory, excludeDirectInactive, excludeIndirectInactive);
		}
		//no role found at all
		Set<Integer> roleSet = AccessBeans.getRolesSetByProjectRights(projectID, arrRights);
		Set<Integer> personSet = new HashSet<Integer>();
		if (roleSet!=null && !roleSet.isEmpty()) {
			//gather the involved roleIDs in a Set
			//get the roles which explicit listTypes
			Object[] roleIDs = roleSet.toArray();
			List<TRoleBean> rolesWithListType = RoleBL.loadWithExplicitIssueType(roleIDs);
			//gather the roles with explicit listTypes in a Set
			Set<Integer> roleWithListTypeSet = new HashSet<Integer>();
			if (rolesWithListType!=null && !rolesWithListType.isEmpty()) {
				for (TRoleBean roleBean : rolesWithListType) {
					roleWithListTypeSet.add(roleBean.getObjectID());
				}
			}
			//gather the role with implicit listTypes in a Set
			Set<Integer> roleWithoutListTypeSet = new HashSet<Integer>();
			for (Integer roleID : roleSet) {
				if (!roleWithListTypeSet.contains(roleID)) {
					roleWithoutListTypeSet.add(roleID);
				}
			}
			//roles with explicit listTypes
			Object[] arrRoleWithListType = roleWithListTypeSet.toArray();
			//roles without explicit listTypes
			Object[] arrRoleWithoutListType = roleWithoutListTypeSet.toArray();
			//search for roles without explicit listTypes
			if (arrRoleWithoutListType!=null && arrRoleWithoutListType.length>0) {
				personSet.addAll(AccessBeans.loadByProjectsRolesListType(projectID, arrRoleWithoutListType, null));
			}
			//search for roles with explicit listTypes
			if (arrRoleWithListType!=null && arrRoleWithListType.length>0) {
				personSet.addAll(AccessBeans.loadByProjectsRolesListType(projectID, arrRoleWithListType, listTypeID));
			}
		}

		if (currentPerson!=null) {
			personSet.add(currentPerson);
		}
		//excludeDirectInactive is forced to false because if was filtered previously and
		//the actual person could be inactive but should be included even than
		return PersonBL.getPersonsByCategory(personSet, personCategory, true, excludeIndirectInactive, currentPerson);
	}

	/**
	 * Returns the person IDs which have one of the specified rights in a project
	 * @param project
	 * @param arrRights an array of rights, null means any right
	 * @param person
	 * @param excludeDirectInactive
	 * @param excludeIndirectInactive
	 * @return
	 */
	public static List<TPersonBean> getPersonsWithRightInProject(Integer projectID, int[] arrRights, Integer currentPerson,
			int personCategory, boolean excludeDirectInactive, boolean excludeIndirectInactive) {
		Set<Integer> personIDs = AccessBeans.getPersonSetByProjectRights(projectID, arrRights);
		if (currentPerson!=null) {
			personIDs.add(currentPerson);
		}
		//excludeDirectInactive is forced to false because if was filtered previously and
		//the actual person could be inactive but should be included even than
		return PersonBL.getPersonsByCategory(personIDs, personCategory,
				true, excludeIndirectInactive, currentPerson);
	}


	/**
	 * Loads the persons which have at least one role from the list of roles
	 * for a project and issueType
	 * @param selectedRoleIDs
	 * @param projectID
	 * @param issueTypeID
	 * @param currentOptions these options should be included even if the roles was revoked already
	 * @return
	 */
	private static Set<Integer> loadPersonsByRoles(List<Integer> selectedRoleIDs,
			Integer projectID, Integer issueTypeID, Integer[] currentOptions) {
		List<Integer> filteredRoleIDs = RoleBL.filterRolesByIssueType(selectedRoleIDs, issueTypeID);
		if (filteredRoleIDs==null || filteredRoleIDs.isEmpty()) {
			return new HashSet<Integer>();
		}
		Set<Integer> foundPersons = AccessBeans.loadByProjectsRolesListType(
				projectID, filteredRoleIDs.toArray(), null);
		if (currentOptions!=null && currentOptions.length>0) {
			//add the current options even if the user is deactivated
			for (int i = 0; i < currentOptions.length; i++) {
				foundPersons.add(currentOptions[i]);
			}
		}
		return foundPersons;
	}

	/**
	 * Loads the persons which have at least one role from the list of roles
	 * @param roleIDs
	 * @param projectID
	 * @param issueTypeID
	 * @param currentOption
	 */
	public static List<TPersonBean> loadAllPersonsAndGroupsByRoles(List<Integer> roleIDs, Integer projectID, Integer issueTypeID, Integer[] currentOptions) {
		Set<Integer> personIDs = loadPersonsByRoles(roleIDs, projectID, issueTypeID, currentOptions);
		//the deactivated direct users are already filtered and currentOptions
		//could be a deactivated user but that should be preserved
		Integer currentOption = null;
		if (currentOptions!=null && currentOptions.length>0) {
			currentOption = currentOptions[0];
		}
		return PersonBL.getPersonsByCategory(personIDs, TPersonBean.PERSON_CATEGORY.ALL, true, true, currentOption);
	}

	/**
	 * Gets the users which has entered effort/cost to the workItem
	 * as a list of LabelValueBeans
	 * @param workItemKey
	 * @return
	 */
	public static List<TPersonBean> loadShowEffortCostUserList(Integer workItemKey) {
		return personDAO.loadPersonsWithEffort(workItemKey);
	}

	/**
	 * Load the persons which are managers
	 * for at least one workItem in any of the projects
	 * @param projects
	 * @return
	 */
	public static List<TPersonBean> loadUsedManagersByProjects(Integer person, Integer[] projects) {
		return personDAO.loadUsedManagersByProjects(person, projects);
	}

	/**
	 * Load the persons/groups which are responsibles
	 * for at least one workItem in any of the projects
	 * @param projects
	 * @return
	 */
	public static List<TPersonBean> loadUsedResonsiblesByProjects(Integer person, Integer[] projects) {
		return personDAO.loadUsedResonsiblesByProjects(person, projects);
	}

	/**
	 * Load the persons which are originators
	 * for at least one workItem in any of the projects
	 * @param projects
	 * @return
	 */
	public static List<TPersonBean> loadUsedOriginatorsByProjects(Integer person, Integer[] projects) {
		return personDAO.loadUsedOriginatorsByProjects(person, projects);
	}

	/**
	 * Load the persons which are last edited persons
	 * for at least one workItem in any of the projects
	 * @param projects
	 * @return
	 */
	public static List<TPersonBean> loadUsedLastEditedByProjects(Integer[] projects) {
		return personDAO.loadUsedLastEditedByProjects(projects);
	}

	/**
	 * Load the persons which are picked persons
	 * for at least one workItem in any of the projects
	 * @param projects
	 * @return
	 */
	public static List<TPersonBean> loadUsedUserPickersByProjects(Integer fieldID, Integer[] projects) {
		return personDAO.loadUsedUserPickersByProjects(fieldID, projects);
	}

	/**
	 * Load the persons/groups which are consultants/informants
	 * for at least one workItem in any of the projects
	 * @param projects
	 * @param raciRole
	 * @return
	 */
	public static List<TPersonBean> loadUsedConsultantsInformantsByProjects(Integer[] projects, String raciRole) {
		return personDAO.loadUsedConsultantsInformantsByProjects(projects, raciRole);
	}

	/**
	 * Set this user property for all real users.
	 * Field: homePage
	 * Value: "cockpit" or "itemNavigator"
	 */
	public static void setUserProperty(String field, String value) {
		List<TPersonBean> users = loadPersons();
		for (TPersonBean user: users) {
			if ("homePage".equals(field)) { // value can be "cockpit" or "itemNavigator"
				user.setHomePage(value);
			}
			if ("layoutEnabled".equals(field)) {
				if ("true".equals(value)) {
					user.setEnableQueryLayout(true);
				} else {
					user.setEnableQueryLayout(false);
				}
			}
			if ("alwaysSaveAttachment".equals(field)) {
				if ("true".equals(value)) {
					user.setAlwaysSaveAttachment(true);
				} else {
					user.setAlwaysSaveAttachment(false);
				}
			}
			if ("hoursPerWorkday".equals(field)) {
				user.setHoursPerWorkDay(new Double(value));
			}
			if ("hourlyWage".equals(field)) {
				user.setHourlyWage(new Double(value));
			}
			if ("extraHourWage".equals(field)) {
				user.setExtraHourWage(new Double(value));
			}
			if ("autoloadTime".equals(field)) {
				user.setAutoLoadTime(new Integer(value));
			}
			if ("maxAssignedItems".equals(field)) {
				user.setMaxAssignedItems(new Integer(value));
			}
			if ("prefEmailType".equals(field)) {
				if ("html".equals(value.toLowerCase())) {
					user.setPrefEmailType(value);
				} else {
					user.setPrefEmailType("plain");
				}
			}
			if ("sessionTimeout".equals(field)) {
				user.setSessionTimeoutMinutes(new Integer(value));
			}
			saveSimple(user);
		}
	}

	/**
	 * Loads the number of persons in each department
	 * @param departmentIDs the list with department object ids for which the number of
	 * persons belonging to it is to be retrieved
	 * @return a map with key = department object id, value = number of persons
	 */
	public static Map<Integer, Integer> loadNumberOfPersonsInDepartments(List<Integer> departmentIDs) {
		return personDAO.loadNumberOfPersonsInDepartments(departmentIDs);
	}

	/**
	 * Remove the tokens for the forgot password links in case somebody tried to reset the password
	 * for another person
	 *
	 */
	public static void cancelForgotPasswordTokens() {
		personDAO.cancelForgotPasswordTokens();
		return;
	}

	/**
	 * Remove users that have registered themselves and that have not confirmed their registration
	 * within the grace period.
	 *
	 */
	public static void removeUnconfirmedUsers() {
		personDAO.removeUnconfirmedUsers();

	}

	public static boolean remianAdminUserAfterDelete(String selectedPersonIDs) {
		List<String> personsToDeleteList = Arrays.asList(selectedPersonIDs.split(","));
		Set<Integer> personsToDeleteSet = new HashSet<Integer>();
		for(String idItem : personsToDeleteList) {
			personsToDeleteSet.add( Integer.parseInt(idItem));
		}


		List<TPersonBean> personBeanList = PersonBL.loadSystemAdmins();
		Set<Integer>allSysAdminSet = new HashSet<Integer>();
		for(TPersonBean aPerson : personBeanList) {
			allSysAdminSet.add(aPerson.getObjectID());
		}

		if(personsToDeleteSet.containsAll(allSysAdminSet)) {
			return false;
		}else {
			return true;
		}
	}

	/**
	 * 	The following method creates a client user, based on name, and email address.
	 * The password will be set by accessing an URL by newly created client user.
	 * @param name
	 * @param emailAddress
	 * @return
	 */
	public static TPersonBean saveAnonymousUserAsClientFromEmail(String name, String emailAddress, Locale locale) {
		TPersonBean person = new TPersonBean();
		if (name != null && !"".equals(name)) {
			String nameparts[] = name.split("\\s+");
			String firstName = "";
			String lastName = "";
			for (int i=0; i < nameparts.length-1; ++i) {
				firstName = firstName + " " + nameparts[i];
			}
			firstName = firstName.trim();
			person.setFirstName(firstName);
			firstName = firstName.toLowerCase();

			lastName = nameparts[nameparts.length-1];
			person.setLastName(lastName);
			lastName = lastName.toLowerCase();

			String loginName = firstName + "." + lastName;
			TPersonBean checkedPerson = PersonBL.loadByLoginName(loginName);
			if(checkedPerson != null) {
				String tmpLoginName = loginName;
				int index = 1;
				while (checkedPerson != null) {
					tmpLoginName = loginName + Integer.toString(index);
					checkedPerson = PersonBL.loadByLoginName(tmpLoginName);
					index++;
				}
				loginName = tmpLoginName;
			}
			person.setUserLevel(TPersonBean.USERLEVEL.CLIENT);
			person.setLoginName(loginName);
			person.setEmail(emailAddress);
			person.setPrefEmailType("HTML");
			person.setHomePage("cockpit");
			if (locale!=null) {
				//person.setLocale(locale);
				person.setPrefLocale(locale.toString());
			}
			Integer id = save(person);
			person = PersonBL.loadByPrimaryKey(id);
			LOGGER.debug("New client user has been created from sent mail userName: " + person.getUsername() + " firstname: " +
							person.getFirstName() + " lastname: " + person.getLastName() + " e-mail: " + person.getEmail());
			ProfileBL.addNewClientToGroups(id);
			return person;
		}
		LOGGER.debug("Creating new client user from sent mail failed!");
		return null;
	}

	/**
	 * The following method sends a mail to person using mail template.
	 * The mail contains: the login name and URL where the client must
	 * set a password.
	 * @param personBean
	 * @return
	 */
	public static boolean sendEmailToNewlyCreatedClientUser(TPersonBean personBean){
		boolean emailSent = false;
		StringWriter w = new StringWriter();
		Template template = null;
		Map<String,String> root = new HashMap<String,String>();
		TMailTemplateDefBean mailTemplateDefBean = MailTemplateBL.getSystemMailTemplateDefBean(IEventSubscriber.EVENT_POST_USER_CREATED_BY_EMAIL, personBean);
		String subject;

		if(mailTemplateDefBean!=null){
			String templateStr = mailTemplateDefBean.getMailBody();
			subject = mailTemplateDefBean.getMailSubject();
			try {
				template = new Template("name", new StringReader(templateStr), new Configuration());
			} catch (IOException e) {
				LOGGER.debug("Loading the template ClientCreatedByEmail.xml failed with " + e.getMessage(), e);
			}
		} else {
			LOGGER.error("Can't get mail template ClientCreatedByEmail.xml!");
			return false;
		}

		if (template==null) {
			LOGGER.error("No valid template found for client user created after sent mail. The dafault: ClientCreatedByEmail.xml");
			return false;
		}
		TSiteBean siteBean = ApplicationBean.getApplicationBean().getSiteBean();
		//The path starts with a "/" character but does not end with a "/"
		String contextPath=ApplicationBean.getApplicationBean().getServletContext().getContextPath();
		String siteURL=siteBean.getServerURL();
		if(siteURL==null){
			siteURL="";
		}else if(siteURL.endsWith("/")){
			siteURL=siteURL.substring(0,siteURL.lastIndexOf("/"));
		}
		Date texpDate = new Date(new Date().getTime() + 8 * 3600*1000); // The URL is valid 4 hours for setting the mail.
		personBean.setTokenExpDate(texpDate);
		String tokenPasswd = DigestUtils.md5Hex(Long.toString(texpDate.getTime()));
		personBean.setForgotPasswordKey(tokenPasswd);
		ApplicationBean appBean = ApplicationBean.getApplicationBean();
		if (!appBean.getSiteBean().getIsDemoSiteBool() || personBean.getIsSysAdmin()) {
			PersonBL.saveSimple(personBean);
		}
		String confirmURL = siteURL + contextPath+"/resetPassword!confirm.action?ctk=" + personBean.getForgotPasswordKey();
		root.put("loginname", personBean.getUsername());
		root.put("firstname", personBean.getFirstName());
		root.put("lastname", personBean.getLastName());
		root.put("confirmUrl", confirmURL);

		try {
			template.process(root, w);
		} catch (Exception e) {
			LOGGER.error("Processing create client user from sent mail  " + template.getName() + " failed with " + e.getMessage(), e);
		}
		w.flush();
		String messageBody = w.toString();

		try {
			// Send mail to newly created client user
			MailSender ms = new MailSender(new InternetAddress(siteBean.getTrackEmail(), siteBean.getEmailPersonalName()),new InternetAddress(personBean.getEmail(), personBean.getFullName()), subject, messageBody, mailTemplateDefBean.isPlainEmail());
			emailSent=ms.send(); // wait for sending email
			LOGGER.debug("emailSend: " + emailSent);
			EventPublisher evp = EventPublisher.getInstance();
			if (evp != null) {
				List<Integer> events = new LinkedList<Integer>();
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_USER_CREATED_BY_EMAIL));
				evp.notify(events, personBean);
			}
		}
		catch (Exception t) {
			LOGGER.error("SendMail newly created client user failed from sent mail: ", t);
		}
		return emailSent;
	}
}
