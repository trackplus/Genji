/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */


package com.aurel.track.accessControl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessControlBL;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.group.GroupConfigBL;
import com.aurel.track.admin.user.group.GroupMemberBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.beans.TRoleListTypeBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.custom.picker.UserPickerRT;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.fieldType.types.system.text.SystemTextBoxDate.HIERARCHICAL_BEHAVIOR_OPTIONS;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.item.workflow.execute.StatusWorkflow;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.PropertiesHelper;
import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;

/**
 * This class provides an AccessBeans ArrayList mainly for managing user access
 * rights and access to specific issues. It loads all role/project combinations
 * for one person from table TACL in the database and stores the access rights
 * in a hashtable for each project the key being the project id, and the value
 * being an another map with access flag values
 *
 * Furthermore, this class checks to what list types a user has access in a
 * specific project. It creates a hash set containing entries where projectid
 * and list type id are concatenated strings.
 *
 */
public class AccessBeans implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String theProject = "The project ";
	private static final String addItem = "Add item ";
	private static final String noRole = "No role found for person ";
	private static final String remaining = " remaining to verify";

	public static final char ONVALUE = '1';
	public static final char OFFVALUE = '0';

	private static final Logger LOGGER = LogManager.getLogger(AccessBeans.class);

	private AccessBeans() {
	}

	/**
	 * Gets the array with the person and the groups the person is member of
	 *
	 * @param personID
	 * @return
	 */
	public static List<Integer> getMeAndMyGroups(Integer personID) {
		List<Integer> personIDs = new LinkedList<Integer>();
		if (personID != null) {
			personIDs.add(personID);
			List<TPersonBean> groupBeans = PersonBL
					.loadGroupsForPerson(personID);
			if (groupBeans != null && !groupBeans.isEmpty()) {
				LOGGER.debug("Number of groups found for person " + personID
						+ ": " + groupBeans.size());
				personIDs.addAll(GeneralUtils
						.createIntegerListFromBeanList(groupBeans));
			}
		}
		return personIDs;
	}

	/**
	 * Gets a list with personID and the persons substituted by personID
	 *
	 * @param personID
	 * @return
	 */
	public static List<Integer> getMeAndSubstituted(TPersonBean personBean) {
		List<Integer> personIDs = new LinkedList<Integer>();
		if (personBean != null) {
			Integer personID = personBean.getObjectID();
			personIDs.add(personID);
			List<Integer> substitutePersons = personBean
					.getSubstitutedPersons();
			if (substitutePersons == null) {
				LOGGER.debug("Get the substitute persons from the database");
				substitutePersons = PersonBL.loadSubstitutedPersons(personID);
				if (substitutePersons != null) {
					// set even if it is of length 0 to avoid loading from DB
					// next time
					personBean.setSubstitutedPersons(substitutePersons);
					LOGGER.debug("Number of substitute persons found for person "
							+ personID + ": " + substitutePersons.size());
				}
			}
			if (substitutePersons != null) {
				personIDs.addAll(substitutePersons);
			}
		}
		return personIDs;
	}

	/**
	 * Gets a list with personID and the persons substituted by personID
	 *
	 * @param personID
	 * @return
	 */
	public static List<Integer> getMeAndSubstituted(Integer personID) {
		TPersonBean personBean = LookupContainer.getPersonBean(personID);
		return getMeAndSubstituted(personBean);
	}

	/**
	 * Gets a list with personID, the persons substituted by personID and all
	 * groups they belong to
	 *
	 * @param personID
	 * @return
	 */
	public static List<Integer> getMeAndSubstitutedAndGroups(Integer personID) {
		List<Integer> personIDs = getMeAndSubstituted(personID);
		List<TPersonBean> groupsForPersons = PersonBL
				.loadGroupsForPersons(personIDs);
		if (groupsForPersons != null && !groupsForPersons.isEmpty()) {
			LOGGER.debug("Number of groups found for person (and substitutes)"
					+ personID + ": " + groupsForPersons.size());
			personIDs.addAll(GeneralUtils
					.createIntegerListFromBeanList(groupsForPersons));
		}
		return personIDs;
	}

	/**
	 * Creates an array of like strings for extendedAccessKeys
	 *
	 * @param arrFlagIndexes
	 *            value from AccessFlagMigrationIndexes
	 * @return
	 */
	public static String[] likeFilterString(int[] arrFlagIndexes) {
		String[] filterStrings = new String[arrFlagIndexes.length];
		StringBuilder flags;
		for (int i = 0; i < arrFlagIndexes.length; i++) {
			flags = new StringBuilder();
			for (int j = 0; j < arrFlagIndexes[i]; j++) {
				flags.append('?');
			}
			flags.append(ONVALUE);
			flags.append('*');
			filterStrings[i] = flags.toString();
		}
		return filterStrings;
	}

	/**
	 * Creates an array of like strings for extendedAccessKeys
	 *
	 * @return
	 */
	public static String anyRightFilterString() {
		return "*" + ONVALUE + "*";
	}

	/**
	 * Whether is allowed to create new issue in a project for a listType In
	 * create phase no raci roles are involved only explicit roles count
	 *
	 * @param personID
	 * @param projectID
	 * @param listTypeID
	 * @return
	 */
	public static boolean isAllowedToCreate(Integer personID,
			Integer projectID, Integer listTypeID) {
		return hasPersonRightInProjectForIssueType(personID, projectID,
				listTypeID, AccessFlagIndexes.CREATETASK, true, true);
	}

	/**
	 * Checks whether the person logged on has read access to the item
	 *
	 * @param workItemBean
	 * @param personID
	 * @return
	 */
	public static boolean isAllowedToRead(TWorkItemBean workItemBean,
			Integer personID) {
		if (personID == null) {
			LOGGER.warn("Not allowed to read, personID not specified");
			return false;
		}
		if (workItemBean == null) {
			LOGGER.warn("Not allowed to read, workItemBean not specified");
			return false;
		}
		// whether the access level permits the read (not private for another
		// person)
		if (workItemBean.isAccessLevelFlag()
				&& !workItemBean.getOriginatorID().equals(personID)) {
			LOGGER.debug("Not allowed to read private workItem "
					+ workItemBean.getObjectID());
			return false;
		}

		Integer project = workItemBean.getProjectID();
		if (project == null) {
			LOGGER.warn("Not allowed to read, no project for workItem");
			return false;
		} else {
			if (SystemStatusBL.getStatusFlag(project,
					TSystemStateBean.ENTITYFLAGS.PROJECTSTATE) == TSystemStateBean.STATEFLAGS.CLOSED) {
				LOGGER.debug("Not allowed to read the workItem "
						+ workItemBean.getObjectID() + " from a closed project");
				return false;
			}
		}
		if (workItemBean.isArchivedOrDeleted()) {
			TPersonBean personBean = LookupContainer.getPersonBean(personID);
			if (!PersonBL.isProjectAdmin(personID, project)
					&& (personBean == null || !personBean.isSys())) {
				LOGGER.debug("Not allowed to read the archived/deleted workItem "
						+ workItemBean.getObjectID());
				return false;
			}
		}
		return isAllowed(workItemBean, personID, AccessFlagIndexes.READANYTASK,
				null);
	}

	/**
	 * Checks whether the person logged on has modify access to the item
	 * (directly or through raci role)
	 *
	 * @param workItemBean
	 * @return
	 */
	public static boolean isAllowedToChange(TWorkItemBean workItemBean,
			Integer personID) {
		if (personID == null) {
			LOGGER.warn("Not allowed to change, personID not specified");
			return false;
		}
		if (workItemBean == null) {
			LOGGER.warn("Not allowed to change, workItemBean not specified");
			return false;
		}
		// whether the access level forbids to read and implicitly to write
		// (private for another person)
		if (workItemBean.isAccessLevelFlag()
				&& !workItemBean.getOriginatorID().equals(personID)) {
			LOGGER.debug("Not allowed to change private workItem "
					+ workItemBean.getObjectID());
			return false;
		}
		Integer project = workItemBean.getProjectID();
		if (project == null) {
			LOGGER.warn("Not allowed to modify, no project for workItem");
			return false;
		} else {
			int projectStatusFlag = SystemStatusBL.getStatusFlag(project,
					TSystemStateBean.ENTITYFLAGS.PROJECTSTATE);
			if (projectStatusFlag != TSystemStateBean.STATEFLAGS.ACTIVE) {
				LOGGER.debug("Not allowed to change the workItem "
						+ workItemBean.getObjectID()
						+ " from a closed or inactive project");
				return false;
			}
		}
		if (workItemBean.isArchivedOrDeleted()) {
			TPersonBean personBean = LookupContainer.getPersonBean(personID);
			if (!PersonBL.isProjectAdmin(personID, project)
					&& (personBean == null || !personBean.isSys())) {
				LOGGER.debug("Not allowed to change the archived/deleted workItem "
						+ workItemBean.getObjectID());
				return false;
			}
		}
		Integer statusID = workItemBean.getStateID();
		TStateBean stateBean = LookupContainer.getStatusBean(statusID);
		if (stateBean != null
				&& stateBean.getStateflag() != null
				&& stateBean.getStateflag().intValue() == TStateBean.STATEFLAGS.DISABLED) {
			List<TStateBean> stateBeanList = StatusWorkflow.loadStatesTo(
					project, workItemBean.getListTypeID(), statusID, personID,
					workItemBean, null);
			if (stateBeanList.size() == 1) {
				LOGGER.debug("According to the workflow changing the disabled workItem is not allowed "
						+ workItemBean.getObjectID());
				return false;
			}
		}
		return isAllowed(workItemBean, personID,
				AccessFlagIndexes.MODIFYANYTASK, RaciRole.CONSULTANT);
	}

	/**
	 * Gets the "on behalf of" persons from the workItem's custom attributes
	 *
	 * @param workItemBean
	 */
	private static Set<Integer> getOnBehalfOfPersons(TWorkItemBean workItemBean) {
		Set<Integer> onBehalfOfPersons = null;
		Map<String, Object> customAttributes = workItemBean
				.getCustomAttributeValues();
		if (customAttributes == null) {
			// if the custom attributes are not yet loaded load only the pickers
			customAttributes = AttributeValueBL
					.loadWorkItemCustomUserPickerAttributes(workItemBean);
		}
		if (customAttributes != null && !customAttributes.isEmpty()) {
			for (String mergeKey : customAttributes.keySet()) {
				Object fieldValue = customAttributes.get(mergeKey);
				if (fieldValue != null) {
					Integer[] keyComponents = MergeUtil.getParts(mergeKey
							.substring(TWorkItemBean.PREFIX_LETTER.length()));
					Integer fieldID = keyComponents[0];
					IFieldTypeRT fieldTypeRT = FieldTypeManager
							.getFieldTypeRT(fieldID);
					if (fieldTypeRT.isUserPicker()) {
						UserPickerRT userPickerRT = (UserPickerRT) fieldTypeRT;
						if (userPickerRT.inheritsOriginatorRole()) {
							if (onBehalfOfPersons == null) {
								onBehalfOfPersons = new HashSet<Integer>();
							}
							Object[] fieldValues = null;
							try {
								fieldValues = (Object[]) fieldValue;
								if (fieldValues != null
										&& fieldValues.length > 0) {
									onBehalfOfPersons
											.add((Integer) fieldValues[0]);
								}
							} catch (Exception e) {
								LOGGER.info(e.getMessage());
								LOGGER.debug(ExceptionUtils.getStackTrace(e));
							}
						}
					}
				}
			}
		}
		return onBehalfOfPersons;
	}

	/**
	 * Returns all direct on behalf persons but load all on behalf persons
	 * (direct an through group) into onBehalfPersonsAll
	 *
	 * @param onBehalfOfPersonsOrGroups
	 * @param onBehalfPersonsAll
	 * @return
	 */
	private static Set<Integer> getOnBehalfPersonsDirect(
			Set<Integer> onBehalfOfPersonsOrGroups,
			Set<Integer> onBehalfPersonsAll) {
		Set<Integer> onBehalfPersonsDirect = new HashSet<Integer>();
		Set<Integer> onBehalfPersonsThroughGroup = new HashSet<Integer>();
		List<Integer> onBehalfGroups = new LinkedList<Integer>();
		if (onBehalfOfPersonsOrGroups != null
				&& !onBehalfOfPersonsOrGroups.isEmpty()) {
			List<TPersonBean> onBehalfPersonBeans = PersonBL
					.loadByKeys(GeneralUtils
							.createIntegerListFromCollection(onBehalfOfPersonsOrGroups));
			if (onBehalfPersonBeans != null) {
				for (TPersonBean personBean : onBehalfPersonBeans) {
					Integer personID = personBean.getObjectID();
					if (personBean.isGroup()) {
						onBehalfGroups.add(personID);
						LOGGER.trace("On behalf group " + personBean.getName()
								+ " (" + personBean.getObjectID() + ")  found");
					} else {
						onBehalfPersonsDirect.add(personID);
						LOGGER.trace("On behalf person " + personBean.getName()
								+ " (" + personBean.getObjectID() + ")  found");
					}
				}
				if (!onBehalfGroups.isEmpty()) {
					List<TPersonBean> onBehalfPersonsThroughGroupBeans = PersonBL
							.loadPersonsForGroups(onBehalfGroups);
					if (onBehalfPersonsThroughGroupBeans != null
							&& !onBehalfPersonsThroughGroupBeans.isEmpty()) {
						onBehalfPersonsThroughGroup = GeneralUtils
								.createIntegerSetFromBeanList(onBehalfPersonsThroughGroupBeans);
					}
				}
			}
		}
		onBehalfPersonsAll.addAll(onBehalfPersonsDirect);
		onBehalfPersonsAll.addAll(onBehalfPersonsThroughGroup);
		return onBehalfPersonsDirect;
	}

	/**
	 * Whether the person (directly or as substitute) has a right in the item
	 *
	 * @param workItemBean
	 * @param personID
	 * @param permissionFlag
	 * @param watcherRole
	 * @return
	 */
	private static boolean isAllowed(TWorkItemBean workItemBean,
			Integer personID, int permissionFlag, String watcherRole) {
		if (personID == null) {
			LOGGER.debug("Not allowed, personID not specified");
			return false;
		}
		List<Integer> meAndSubstitutes = getMeAndSubstituted(personID);
		Integer workItemID = workItemBean.getObjectID();
		Integer originatorID = workItemBean.getOriginatorID();
		Integer responsibleID = workItemBean.getResponsibleID();
		Integer managerID = workItemBean.getOwnerID();
		Integer project = workItemBean.getProjectID();
		Integer listType = workItemBean.getListTypeID();
		boolean isOriginator = meAndSubstitutes.contains(originatorID);
		boolean isReponsible = meAndSubstitutes.contains(responsibleID);
		boolean isManager = meAndSubstitutes.contains(managerID);
		if (isOriginator || isReponsible || isManager) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Permission " + permissionFlag + " for workItem "
						+ workItemID + " through raci role");
			}
			return true;
		}
		if (GroupMemberBL.isAnyPersonMemberInGroup(meAndSubstitutes,
				responsibleID)) {
			// responsible through group
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Permission " + permissionFlag + " for workItem "
						+ workItemID + " through person member in group");
			}
			return true;
		}
		if (ConsInfBL.hasRaciRole(workItemBean.getObjectID(), meAndSubstitutes,
				watcherRole)) {
			// consultant/informant
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Permission " + permissionFlag + " for workItem "
						+ workItemID + " through watchers");
			}
			return true;
		}
		// try explicit modify role
		if (hasPersonRightInProjectForIssueType(personID, project, listType,
				permissionFlag, true, true)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Permission " + permissionFlag + " for workItem "
						+ workItemID + " through explicit project role");
			}
			return true;
		}
		Set<Integer> onBehalfOfPersonsOrGroups = getOnBehalfOfPersons(workItemBean);
		Set<Integer> onBehalfPersonsAll = new HashSet<Integer>();
		Set<Integer> onBehalfPersonsDirect = getOnBehalfPersonsDirect(
				onBehalfOfPersonsOrGroups, onBehalfPersonsAll);
		if (hasCommon(meAndSubstitutes, onBehalfPersonsAll)) {
			LOGGER.debug("Permission " + permissionFlag + " for workItem "
					+ workItemID + " through on behalf of user picker");
			return true;
		}
		Map<Integer, List<Integer>> reciprocGroupsMap = getRaciprocGroupsMap(meAndSubstitutes);
		Set<Integer> originatorPersons = new HashSet<Integer>();
		originatorPersons.add(originatorID);
		if (onBehalfPersonsDirect != null) {
			originatorPersons.addAll(onBehalfPersonsDirect);
		}
		if (isReciprocByGroup(
				reciprocGroupsMap.get(SystemFields.INTEGER_ORIGINATOR),
				originatorPersons)) {
			LOGGER.debug("Permission " + permissionFlag + " for workItem "
					+ workItemID + " through group reciproc originator flag");
			return true;
		}
		if (isReciprocByGroup(
				reciprocGroupsMap.get(SystemFields.INTEGER_MANAGER), managerID)) {
			LOGGER.debug("Permission " + permissionFlag + " for workItem "
					+ workItemID + " through group reciproc manager flag");
			return true;
		}
		if (isReciprocByGroup(
				reciprocGroupsMap.get(SystemFields.INTEGER_RESPONSIBLE),
				responsibleID)) {
			LOGGER.debug("Permission " + permissionFlag + " for workItem "
					+ workItemID + " through group reciproc responsible flag");
			return true;
		}
		return false;
	}

	/**
	 * Checks whether the person logged on may close the item
	 *
	 * @param workItemBean
	 * @param personID
	 * @return
	 */
	public static boolean isAllowedToClose(TWorkItemBean workItemBean,
			Integer personID) {
		if (personID == null) {
			LOGGER.debug("Not allowed to close, personID not specified");
			return false;
		}
		List<Integer> meAndSubstitutes = getMeAndSubstituted(personID);
		if (workItemBean == null) {
			LOGGER.debug("Not allowed to close, workItemBean not specified");
			return false;
		}
		Integer project = workItemBean.getProjectID();
		if (project == null) {
			LOGGER.debug("Not allowed to close, no project for workItem");
			return false;
		}
		Integer workItemID = workItemBean.getObjectID();
		Integer listType = workItemBean.getListTypeID();
		Integer originatorID = workItemBean.getOriginatorID();
		Integer reponsibleID = workItemBean.getResponsibleID();
		Integer managerID = workItemBean.getOwnerID();
		boolean isReponsible = meAndSubstitutes.contains(reponsibleID);
		boolean isOriginator = meAndSubstitutes.contains(originatorID);
		boolean isManager = meAndSubstitutes.contains(managerID);
		boolean closeIfOrigMan = true;
		boolean closeIfResp = true;
		// explicit "close any" right
		if (hasPersonRightInProjectForIssueType(personID, project, listType,
				AccessFlagIndexes.CLOSEANYTASK, true, true)) {
			LOGGER.debug("'Close any' perimission for workItem " + workItemID);
			return true;
		}
		// originator or manager and explicit "close if man. or orig." right
		if (isOriginator || isManager) {
			if (hasPersonRightInProjectForIssueType(personID, project,
					listType, AccessFlagIndexes.CLOSETASKIFMANAGERORORIGINATOR,
					true, true)) {
				LOGGER.debug("'Close if manager or originator' perimission for workItem "
						+ workItemID);
				return true;
			} else {
				closeIfOrigMan = false;
			}
		}
		// responsible (direct or through group) and explicit "close if resp."
		// right
		if (isReponsible
				|| GroupMemberBL.isPersonMemberInGroup(personID, reponsibleID)) {
			if (hasPersonRightInProjectForIssueType(personID, project,
					listType, AccessFlagIndexes.CLOSETASKIFRESPONSIBLE, true,
					true)) {
				LOGGER.debug("'Close if responsible' perimission for workItem "
						+ workItemID);
				return true;
			} else {
				closeIfResp = false;
			}
		}
		// on behalf
		Set<Integer> onBehalfPersonsDirect = null;
		if (!isOriginator && closeIfOrigMan) {
			Set<Integer> onBehalfOfPersonsOrGroups = getOnBehalfOfPersons(workItemBean);
			Set<Integer> onBehalfPersonsAll = new HashSet<Integer>();
			onBehalfPersonsDirect = getOnBehalfPersonsDirect(
					onBehalfOfPersonsOrGroups, onBehalfPersonsAll);
			if (onBehalfPersonsAll != null && !onBehalfPersonsAll.isEmpty()
					&& onBehalfPersonsAll.contains(personID) && closeIfOrigMan) {
				if (hasPersonRightInProjectForIssueType(personID, project,
						listType,
						AccessFlagIndexes.CLOSETASKIFMANAGERORORIGINATOR, true,
						true)) {
					LOGGER.debug("'Close if manager or originator' perimission for workItem "
							+ workItemID + " by on behalf of " + personID);
					return true;
				} else {
					closeIfOrigMan = false;
				}
			}
		}
		Map<Integer, List<Integer>> reciprocGroupsMap = getRaciprocGroupsMap(meAndSubstitutes);
		if (!isOriginator && closeIfOrigMan) {
			Set<Integer> originatorIDs = new HashSet<Integer>();
			originatorIDs.add(originatorID);
			if (onBehalfPersonsDirect != null) {
				originatorIDs.addAll(onBehalfPersonsDirect);
			}
			isOriginator = isReciprocByGroup(
					reciprocGroupsMap.get(SystemFields.INTEGER_ORIGINATOR),
					originatorIDs);
			if (isOriginator
					&& hasPersonRightInProjectForIssueType(personID, project,
							listType,
							AccessFlagIndexes.CLOSETASKIFMANAGERORORIGINATOR,
							true, true)) {
				LOGGER.debug("'Close if manager or originator' perimission for workItem "
						+ workItemID + " originator by reciproc group");
				return true;
			}
		}
		if (!isManager && closeIfOrigMan) {
			isManager = isReciprocByGroup(
					reciprocGroupsMap.get(SystemFields.INTEGER_MANAGER),
					managerID);
			if (isManager
					&& hasPersonRightInProjectForIssueType(personID, project,
							listType,
							AccessFlagIndexes.CLOSETASKIFMANAGERORORIGINATOR,
							true, true)) {
				LOGGER.debug("'Close if manager or originator' perimission for workItem "
						+ workItemID + " manager by reciproc group");
				return true;
			}
		}
		if (!isReponsible && closeIfResp) {
			isReponsible = isReciprocByGroup(
					reciprocGroupsMap.get(SystemFields.INTEGER_RESPONSIBLE),
					reponsibleID);
			if (isReponsible
					&& hasPersonRightInProjectForIssueType(personID, project,
							listType, AccessFlagIndexes.CLOSETASKIFRESPONSIBLE,
							true, true)) {
				LOGGER.debug("'Close if responsible' perimission for workItem "
						+ workItemID + " responsible by reciproc group");
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the groups the person is member of having the corresponding right
	 * flag set
	 *
	 * @param personID
	 */
	public static Map<Integer, List<Integer>> getRaciprocGroupsMap(
			List<Integer> personIDs) {
		Map<Integer, List<Integer>> reciprocGroupsMap = new HashMap<Integer, List<Integer>>();
		List<TPersonBean> groupsForPerson = PersonBL
				.loadGroupsForPersons(personIDs);
		if (groupsForPerson != null) {
			for (TPersonBean groupBean : groupsForPerson) {
				Integer groupID = groupBean.getObjectID();
				addToMap(groupID, reciprocGroupsMap, true, ALL_PERSONGROUPS);
				boolean isOrginator = PropertiesHelper.getBooleanProperty(
						groupBean.getPreferences(),
						GroupConfigBL.GROUP_FLAGS.ORIGINATOR);
				addToMap(groupID, reciprocGroupsMap, isOrginator,
						SystemFields.INTEGER_ORIGINATOR);
				boolean isManager = PropertiesHelper.getBooleanProperty(
						groupBean.getPreferences(),
						GroupConfigBL.GROUP_FLAGS.MANAGER);
				addToMap(groupID, reciprocGroupsMap, isManager,
						SystemFields.INTEGER_MANAGER);
				boolean isResponsible = PropertiesHelper.getBooleanProperty(
						groupBean.getPreferences(),
						GroupConfigBL.GROUP_FLAGS.RESPONSIBLE);
				addToMap(groupID, reciprocGroupsMap, isResponsible,
						SystemFields.INTEGER_RESPONSIBLE);
			}
		}
		return reciprocGroupsMap;
	}

	public static final Integer ALL_PERSONGROUPS = Integer.valueOf(0);

	/**
	 * Add group to raci map
	 *
	 * @param groupID
	 * @param reciprocGroupsMap
	 * @param checked
	 * @param raciField
	 */
	private static void addToMap(Integer groupID,
			Map<Integer, List<Integer>> reciprocGroupsMap, boolean checked,
			Integer raciField) {
		if (checked) {
			List<Integer> raciList = reciprocGroupsMap.get(raciField);
			if (raciList == null) {
				raciList = new LinkedList<Integer>();
				reciprocGroupsMap.put(raciField, raciList);
			}
			raciList.add(groupID);
		}
	}

	/**
	 * Whether any person from raciPersons is member of any group from
	 * reciprocGroups
	 *
	 * @param reciprocGroups
	 * @param raciPersons
	 * @return
	 */
	private static boolean isReciprocByGroup(List<Integer> reciprocGroups,
			Set<Integer> raciPersons) {
		if (reciprocGroups != null && !reciprocGroups.isEmpty()
				&& raciPersons != null && !raciPersons.isEmpty()) {
			return GroupMemberBL.isAnyPersonMemberInAnyGroup(
					GeneralUtils.createIntegerListFromCollection(raciPersons),
					reciprocGroups);
		} else {
			return false;
		}
	}

	/**
	 * Whether any person from raciPersons is member of any group from
	 * reciprocGroups
	 *
	 * @param reciprocGroups
	 * @param raciPerson
	 * @return
	 */
	private static boolean isReciprocByGroup(List<Integer> reciprocGroups,
			Integer raciPerson) {
		if (reciprocGroups != null && !reciprocGroups.isEmpty()
				&& raciPerson != null) {
			return GroupMemberBL.isPersonMemberInAnyGroup(raciPerson,
					reciprocGroups);
		} else {
			return false;
		}
	}

	/**
	 * Gets the restrictions for budget, plan and expenses
	 *
	 * @param personID
	 * @param projectID
	 * @param listTypeID
	 * @return
	 */
	public static Map<Integer, Integer> getBudgetPlanExpenseRestrictions(
			Integer personID, Integer projectID, Integer listTypeID) {
		List<Integer> fields = new LinkedList<Integer>();
		fields.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
		fields.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET);
		fields.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES);
		fields.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
		return getFieldRestrictions(personID, projectID, listTypeID, fields,
				true);
	}

	/**
	 * Whether the user has any right in the accounting tab
	 *
	 * @param personID
	 * @param projectID
	 * @param listTypeID
	 * @return
	 */
	public static boolean showAccountingTab(Integer personID,
			Integer projectID, Integer listTypeID) {
		Map<Integer, Integer> fieldRestrictions = getBudgetPlanExpenseRestrictions(
				personID, projectID, listTypeID);
		if (fieldRestrictions == null) {
			return true;
		}
		Integer planRestrictions = fieldRestrictions.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
		if (planRestrictions == null
				|| planRestrictions.intValue() != TRoleFieldBean.ACCESSFLAG.NOACCESS) {
			// right to read or modify plan
			return true;
		}
		boolean budgetActive = ApplicationBean.getInstance().getBudgetActive();
		Integer budgetRestrictions = fieldRestrictions.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET);
		if (budgetActive
				&& (budgetRestrictions == null || budgetRestrictions.intValue() != TRoleFieldBean.ACCESSFLAG.NOACCESS)) {
			// right to read or modify budget
			return true;
		}
		Integer myExpensesRestrictions = fieldRestrictions.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES);
		if (myExpensesRestrictions == null) {
			// right to add own expense
			return true;
		}
		Integer totalExpenseRestrictions = fieldRestrictions.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
		if (totalExpenseRestrictions == null
				|| totalExpenseRestrictions.intValue() != TRoleFieldBean.ACCESSFLAG.NOACCESS) {
			// right to see other person's expenses
			return true;
		}
		return false;
	}

	/**
	 * Whether the user can see all expenses It is supposed that this code is
	 * called only if the read access to the issue is already assured
	 *
	 * @param personID
	 * @param projectID
	 * @param listTypeID
	 * @return
	 */
	public static boolean viewAllExpenses(Integer personID, Integer projectID,
			Integer listTypeID) {
		List<Integer> fields = new LinkedList<Integer>();
		fields.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
		Map<Integer, Integer> fieldRestrictions = getFieldRestrictions(
				personID, projectID, listTypeID, fields, false);
		return fieldRestrictions == null
				|| !fieldRestrictions
						.containsKey(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
	}

	/**
	 * Whether the watchers tab is allowed It is supposed that this code is
	 * called only if the read access to the issue is already assured
	 *
	 * @param personID
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	public static boolean watchersTabAllowed(Integer personID,
			Integer projectID, Integer issueTypeID) {
		List<Integer> fields = new LinkedList<Integer>();
		fields.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS);
		Map<Integer, Integer> fieldRestrictions = getFieldRestrictions(
				personID, projectID, issueTypeID, fields, false);
		return fieldRestrictions == null
				|| !fieldRestrictions
						.containsKey(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS);
	}

	/****************************************** Person set with right in projects ******************************************/

	/**
	 * Gets the status IDs assigned to the projectType for an issueType
	 *
	 * @param projectID
	 * @param rights
	 * @return
	 */
	public static Set<Integer> getPersonSetByProjectRights(Integer projectID,
			int[] rights) {
		Integer[] projectIDs = GeneralUtils
				.createIntegerArrFromCollection(ProjectBL
						.getAncestorProjects(projectID));
		return getPersonSetByProjectsRights(projectIDs, rights);
	}

	/**
	 * Gets the status IDs assigned to the projectType for an issueType
	 *
	 * @param projects
	 * @param rights
	 * @return
	 */
	public static Set<Integer> getPersonSetByProjectsRights(Integer[] projects,
			int[] rights) {
		List<TAccessControlListBean> accessControlListBeans = AccessControlBL
				.loadByProjectsAndRights(projects, rights);
		return getPersonsFromAcList(accessControlListBeans);
	}

	/**
	 * Load a list of AccessControlListBeans by projects, roles and listType
	 *
	 * @param projectID
	 * @param roles
	 * @param listType
	 *            if null do not filter by
	 * @return
	 */
	public static Set<Integer> loadByProjectsRolesListType(Integer projectID,
			Object[] roles, Integer listType) {
		List<TAccessControlListBean> accessControlListBeans = AccessControlBL
				.loadByProjectsRolesListType(
						ProjectBL.getAncestorProjects(projectID), roles,
						listType);
		return getPersonsFromAcList(accessControlListBeans);
	}

	/**
	 * Returns a set of personIDs which have one of the specified rights in all
	 * projects
	 *
	 * @param projects
	 * @param arrRights
	 *            an array of rights, null means any right
	 * @return
	 */
	public static Set<Integer> getPersonSetWithRightInAllOfTheProjects(
			Integer[] projects, int[] arrRights) {
		Set<Integer> personIDs = new HashSet<Integer>();
		if (projects == null || projects.length == 0) {
			return personIDs;
		}
		// get the results for first project
		personIDs = getPersonsFromAcList(AccessControlBL
				.loadByProjectsAndRights(GeneralUtils
						.createIntegerArrFromCollection(ProjectBL
								.getAncestorProjects(projects[0])), arrRights));
		if (personIDs == null || personIDs.isEmpty()) {
			return personIDs;
		}
		// verify whether all the persons found for the first project appear
		// also in the next projects
		// if not remove it from the result
		for (int i = 1; i < projects.length; i++) {
			Set<Integer> personIDsForProject = getPersonsFromAcList(AccessControlBL
					.loadByProjectsAndRights(GeneralUtils
							.createIntegerArrFromCollection(ProjectBL
									.getAncestorProjects(projects[i])),
							arrRights));
			if (personIDsForProject == null || personIDsForProject.isEmpty()) {
				return new HashSet<Integer>();
			}
			personIDs.retainAll(personIDsForProject);
		}
		return personIDs;
	}

	/**
	 * Gets the distinct roleIDs from a list of TAccessControlListBean beans
	 *
	 * @param acList
	 * @return
	 */
	static Set<Integer> getPersonsFromAcList(List<TAccessControlListBean> acList) {
		Set<Integer> personSet = new HashSet<Integer>();
		if (acList != null) {
			for (TAccessControlListBean accessControlListBean : acList) {
				personSet.add(accessControlListBean.getPersonID());
			}
		}
		return personSet;
	}

	/**
	 * Gather the explicitly restricted fields (by project roles) and the bottom
	 * up date change restrictions
	 * @param workItemContext
	 * @return
	 */
	public static Map<Integer, Integer> getRestrictedFieldsAndBottomUpDates(WorkItemContext workItemContext) {
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		Set<Integer> presentFieldIDs = workItemContext.getPresentFieldIDs();
		// explicit role descriptions
		Map<Integer, Integer> fieldRestrictions = getFieldRestrictions(
				workItemContext.getPerson(), workItemBean.getProjectID(),
				workItemBean.getListTypeID(), true);
		Map<Integer, TFieldConfigBean> fieldConfigs = workItemContext.getFieldConfigs();
		Map<String, Object> fieldSettings = workItemContext.getFieldSettings();
		// make the dates of the parent workItems read only if not yet
		// restricted and set as compute bottom up
		if (workItemBean.getObjectID() != null) {
			Set<Integer> possibleBottomUpFields = FieldRuntimeBL.getPossibleBottomUpFields();
			Set<Integer> restrictedBottomUpDates = new HashSet<Integer>();
			for (Integer fieldID : possibleBottomUpFields) {
				boolean presentNotRestrictedField = presentFieldIDs.contains(fieldID)
						&& !fieldRestrictions.containsKey(fieldID);
				if (presentNotRestrictedField) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					if (fieldConfigs != null) {
						TFieldConfigBean fieldConfigBean = fieldConfigs.get(fieldID);
						if (fieldTypeRT.getHierarchicalBehavior(fieldID,
								fieldConfigBean, fieldSettings.get(MergeUtil.mergeKey(fieldID, null))) == HIERARCHICAL_BEHAVIOR_OPTIONS.COMPUTE_BOTTOM_UP) {
							restrictedBottomUpDates.add(fieldID);
							if (SystemFields.INTEGER_STARTDATE.equals(fieldID) || SystemFields.INTEGER_ENDDATE.equals(fieldID)) {
								restrictedBottomUpDates.add(SystemFields.INTEGER_DURATION);
							} else {
								restrictedBottomUpDates.add(SystemFields.INTEGER_TOP_DOWN_DURATION);
							}
						}
					}
				}
			}
			if (restrictedBottomUpDates != null
					&& !restrictedBottomUpDates.isEmpty()) {
				if (ItemBL.hasChildren(workItemBean.getObjectID())) {
					for (Integer fieldID : restrictedBottomUpDates) {
						fieldRestrictions.put(fieldID, TRoleFieldBean.ACCESSFLAG.READ_ONLY);
					}
				}
			}
		}
		return fieldRestrictions;
	}

	/**
	 * Returns the fields restrictions for a person on a certain project and
	 * issueType By convention a field will be returned only if it is restricted
	 * either no read (and consequently no modify) or no modify right
	 *
	 * @param personID
	 * @param projectID
	 *            can be null, in this case get the field restrictions for all
	 *            roles the user has in any project
	 * @param issueTypeID
	 *            can be null, same as by projectID
	 * @param edit
	 *            whether we are in an editing (edit/create issue) mode or only
	 *            read only mode (print issue, email sending)
	 */
	public static Map<Integer, Integer> getFieldRestrictions(Integer personID,
			Integer projectID, Integer issueTypeID, boolean edit) {
		return getFieldRestrictions(personID, projectID, issueTypeID, null,
				edit);
	}

	/**
	 * Returns the fields restrictions for a person for a certain project and
	 * issueType A field will be returned only if it is restricted (either no
	 * read or no modify right)
	 *
	 * @param personID
	 * @param projectID
	 *            can be null, in this case get the field restrictions for all
	 *            roles the user has in any project
	 * @param issueTypeID
	 *            can be null, same as by projectID
	 * @param fieldIDs
	 *            can be null, if specified search for restrictions only for
	 *            these fields
	 * @param edit
	 *            whether we are in an editing (edit/create issue) mode or only
	 *            read only mode (print issue, email sending)
	 * @return a map with fieldID to restriction
	 */
	public static Map<Integer, Integer> getFieldRestrictions(Integer personID,
			Integer projectID, Integer issueTypeID, List<Integer> fieldIDs,
			boolean edit) {
		Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
		if (personID == null) {
			// person can be null for example by POP3 email submission
			// the submitter gets an email by creating of the issue,
			// but then no restrictions are needed
			return resultMap;
		}
		// get all hidden and read only fields for all roles
		Map<Integer, Set<Integer>> hiddenFields = new HashMap<Integer, Set<Integer>>();
		Map<Integer, Set<Integer>> readOnlyFields = new HashMap<Integer, Set<Integer>>();
		FieldsRestrictionsToRoleBL.getRestrictedFieldsToRoles(fieldIDs,
				hiddenFields, readOnlyFields);
		if (!hasFieldRestrictions(hiddenFields, readOnlyFields, fieldIDs, edit)) {
			return resultMap;
		}
		List<Integer> personIDs = getMeAndSubstitutedAndGroups(personID);
		Set<Integer> roles = null;
		if (projectID != null) {
			// roles for project and issueType
			roles = getRolesForPersonInProjectForIssueType(personIDs,
					projectID, issueTypeID);
		} else {
			// roles in any projects for any issueType
			roles = getAllRolesForPerson(personIDs);
		}
		if (roles == null || roles.isEmpty()) {
			// all roles revoked from the project (only RACI role) -> no
			// roleFlag restriction
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(noRole + personID
						+ " in project " + projectID + " issueType "
						+ issueTypeID);
			}
			return resultMap;
		}
		if (edit) {
			Set<Integer> allRolesWithHiddenOrReadOnlyFields = new HashSet<Integer>();
			for (Set<Integer> rolesWithHiddenFields : hiddenFields.values()) {
				allRolesWithHiddenOrReadOnlyFields
						.addAll(rolesWithHiddenFields);
			}
			for (Set<Integer> rolesWithReadOnlyFields : readOnlyFields.values()) {
				allRolesWithHiddenOrReadOnlyFields
						.addAll(rolesWithReadOnlyFields);
			}
			if (allRolesWithHiddenOrReadOnlyFields.containsAll(roles)) {
				// each role a user has contains either hidden or read only
				// field restrictions
				addRestrictions(resultMap, roles, hiddenFields,
						TRoleFieldBean.ACCESSFLAG.NOACCESS);
				addRestrictions(resultMap, roles, readOnlyFields,
						TRoleFieldBean.ACCESSFLAG.READ_ONLY);
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Role without restriction found for person "
							+ personID + " in project " + projectID
							+ " issueType " + issueTypeID);
				}
				return resultMap;
			}
		} else {
			Set<Integer> allRolesWithHiddenFields = new HashSet<Integer>();
			for (Set<Integer> rolesWithHiddenFields : hiddenFields.values()) {
				allRolesWithHiddenFields.addAll(rolesWithHiddenFields);
			}
			if (allRolesWithHiddenFields.containsAll(roles)) {
				// each role a user has contains hidden field restrictions
				addRestrictions(resultMap, roles, hiddenFields,
						TRoleFieldBean.ACCESSFLAG.NOACCESS);
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Role without restriction found for person "
							+ personID + " in project " + projectID
							+ " issueType " + issueTypeID);
				}
				return resultMap;
			}
		}
		return resultMap;
	}

	/**
	 * Whether any role contains fieldRestrictions
	 *
	 * @param hiddenFields
	 * @param readOnlyFields
	 * @param fieldIDs
	 * @param edit
	 * @return
	 */
	private static boolean hasFieldRestrictions(
			Map<Integer, Set<Integer>> hiddenFields,
			Map<Integer, Set<Integer>> readOnlyFields, List<Integer> fieldIDs,
			boolean edit) {
		if (edit) {
			if (hiddenFields.isEmpty() && readOnlyFields.isEmpty()) {
				// if none of the roles in the system has hidden or read only
				// restrictions for any field
				return false;
			} else {
				if (fieldIDs != null
						&& !hasFieldRestrictionInAnyRole(readOnlyFields,
								fieldIDs)
						&& !hasFieldRestrictionInAnyRole(hiddenFields, fieldIDs)) {
					// if none of the roles in the system has hidden field or
					// read only restrictions for fieldIDs
					return false;
				}
			}
		} else {
			if (hiddenFields.isEmpty()) {
				// if none of the roles in the system has hidden restrictions
				// for any field
				return false;
			} else {
				if (fieldIDs != null
						&& !hasFieldRestrictionInAnyRole(hiddenFields, fieldIDs)) {
					// if none of the roles in the system has hidden field
					// restrictions for fieldIDs
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Whether any of the the fieldIDs fields are restricted in any role
	 *
	 * @param restrictedFieldsMap
	 * @param fieldIDs
	 * @return
	 */
	private static boolean hasFieldRestrictionInAnyRole(
			Map<Integer, Set<Integer>> restrictedFieldsMap,
			List<Integer> fieldIDs) {
		if (restrictedFieldsMap != null) {

			for (Integer fieldID : fieldIDs) {
				if (restrictedFieldsMap.containsKey(fieldID)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Add restrictions
	 *
	 * @param resultMap
	 * @param roles
	 * @param restrictedFieldsToRoles
	 * @param restriction
	 */
	static void addRestrictions(Map<Integer, Integer> resultMap,
			Set<Integer> roles,
			Map<Integer, Set<Integer>> restrictedFieldsToRoles,
			Integer restriction) {
		for (Integer fieldID : restrictedFieldsToRoles.keySet()) {
			Set<Integer> restrictedRoles = restrictedFieldsToRoles.get(fieldID);
			if (restrictedRoles != null && restrictedRoles.containsAll(roles)) {
				// field is restricted in all roles
				resultMap.put(fieldID, restriction);
			}
		}
	}

	/**
	 * Gets the roles for person in project for issueType
	 *
	 * @param personID
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	private static Set<Integer> getRolesForPersonInProjectForIssueType(
			List<Integer> personIDs, Integer projectID, Integer issueTypeID) {
		Set<Integer> roles = new HashSet<Integer>();
		if (projectID == null || personIDs == null) {
			return new HashSet<Integer>();
		}
		List<Integer> ancestorProjects = ProjectBL
				.getAncestorProjects(projectID);
		List<TAccessControlListBean> acList = AccessControlBL
				.loadByPersonsAndProjects(personIDs, ancestorProjects);
		// all roles for person
		Set<Integer> allRoles = getRolesFromAcList(acList);
		if (issueTypeID == null) {
			return allRoles;
		}
		List<TRoleBean> rolesWithListType = RoleBL
				.loadWithExplicitIssueType(allRoles.toArray());
		// gather the roles with explicit listTypes (not necessarily
		// issueTypeID!)
		Set<Integer> roleWithListTypeSet = new HashSet<Integer>();
		if (rolesWithListType != null && !rolesWithListType.isEmpty()) {
			for (TRoleBean roleBean : rolesWithListType) {
				roleWithListTypeSet.add(roleBean.getObjectID());
			}
		}
		// gather the role without listTypes in a Set
		for (Integer roleID : allRoles) {
			if (!roleWithListTypeSet.contains(roleID)) {
				roles.add(roleID);
			}
		}
		// search for roles with explicit listTypes
		if (roleWithListTypeSet != null && !roleWithListTypeSet.isEmpty()) {
			List<TRoleListTypeBean> roleListTypeBeans = RoleBL
					.loadByRolesAndListType(roleWithListTypeSet.toArray(),
							issueTypeID);
			if (roleListTypeBeans != null && !roleListTypeBeans.isEmpty()) {
				for (TRoleListTypeBean roleListTypeBean : roleListTypeBeans) {
					roles.add(roleListTypeBean.getRole());
				}
			}
		}
		return roles;
	}

	/**
	 * Gets the roles for person in any project
	 *
	 * @param personIDs
	 * @return
	 */
	private static Set<Integer> getAllRolesForPerson(List<Integer> personIDs) {
		Set<Integer> roles = new HashSet<Integer>();
		int[] projectStatusFlags = new int[] {
				TSystemStateBean.STATEFLAGS.ACTIVE,
				TSystemStateBean.STATEFLAGS.INACTIVE };
		if (personIDs != null && !personIDs.isEmpty()) {
			List<TAccessControlListBean> accessControlListBeans = AccessControlBL
					.loadByPersonsAndProjectStatusFlag(personIDs,
							projectStatusFlags);
			return getRolesFromAcList(accessControlListBeans);
		}
		return roles;
	}

	/**
	 * Whether the field is restricted in any project to issueType context
	 *
	 * @param personID
	 * @param projectToIssueTypesMap
	 * @param fieldID
	 * @param edit
	 * @return
	 */
	public static boolean getFieldRestrictedInAnyContext(Integer personID,
			Map<Integer, Set<Integer>> projectToIssueTypesMap, Integer fieldID,
			boolean edit) {
		List<Integer> fieldIDs = new LinkedList<Integer>();
		fieldIDs.add(fieldID);
		Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions = AccessBeans
				.getFieldRestrictions(personID, projectToIssueTypesMap,
						fieldIDs, edit);
		if (fieldRestrictions != null && !fieldRestrictions.isEmpty()) {
			for (Integer projectID : projectToIssueTypesMap.keySet()) {
				Set<Integer> issueTypeIDsSet = projectToIssueTypesMap
						.get(projectID);
				if (issueTypeIDsSet != null) {
					for (Integer issueTypeID : issueTypeIDsSet) {
						Map<Integer, Map<Integer, Integer>> issueTypeRestrictions = fieldRestrictions
								.get(projectID);
						Map<Integer, Integer> restrictedFields = null;
						if (issueTypeRestrictions != null) {
							restrictedFields = issueTypeRestrictions
									.get(issueTypeID);
							if (restrictedFields != null
									&& !restrictedFields.isEmpty()) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gets the field restrictions for a list of workItems
	 *
	 * @param personID
	 * @param workItemBeanList
	 * @param fieldIDs
	 * @param edit
	 * @return
	 */
	public static Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(
			Integer personID, List<TWorkItemBean> workItemBeanList,
			List<Integer> fieldIDs, boolean edit) {
		Map<Integer, Set<Integer>> projectToIssueTypesMap = AccessBeans
				.getProjectToIssueTypesMap(workItemBeanList);
		return getFieldRestrictions(personID, projectToIssueTypesMap, fieldIDs,
				edit);
	}

	/**
	 * Returns the fields restrictions for a person for a projects and issue
	 * types By convention a field will be returned only if it is restricted
	 * (either no read (and consequently no modify) or no modify right)
	 *
	 * @param personID
	 * @param projectToIssueTypesMap
	 * @param fieldIDs
	 * @param edit
	 *            whether we are in an editing (edit/create issue) mode or only
	 *            read only mode (print issue, email sending)
	 */
	public static Map<Integer, Map<Integer, Map<Integer, Integer>>> getFieldRestrictions(
			Integer personID,
			Map<Integer, Set<Integer>> projectToIssueTypesMap,
			List<Integer> fieldIDs, boolean edit) {
		Map<Integer, Map<Integer, Map<Integer, Integer>>> resultMap = new HashMap<Integer, Map<Integer, Map<Integer, Integer>>>();
		if (personID == null) {
			// person can be null for example by POP3 email submission
			// the submitter gets an email by creating of the issue,
			// but then no restrictions are needed
			return resultMap;
		}
		Map<Integer, Set<Integer>> hiddenFields = new HashMap<Integer, Set<Integer>>();
		Map<Integer, Set<Integer>> readOnlyFields = new HashMap<Integer, Set<Integer>>();
		FieldsRestrictionsToRoleBL.getRestrictedFieldsToRoles(fieldIDs,
				hiddenFields, readOnlyFields);
		if (!hasFieldRestrictions(hiddenFields, readOnlyFields, fieldIDs, edit)) {
			return resultMap;
		}
		/**
		 * Get the role for any project/issueType combinations
		 */
		Map<Integer, Map<Integer, Set<Integer>>> rolesForProjectIssueType = getRolesForPersonInProjectsForIssueTypes(
				personID, projectToIssueTypesMap);
		for (Integer projectID : projectToIssueTypesMap.keySet()) {
			Map<Integer, Map<Integer, Integer>> projectRestrictions = new HashMap<Integer, Map<Integer, Integer>>();
			resultMap.put(projectID, projectRestrictions);
			Set<Integer> issueTypesSet = projectToIssueTypesMap.get(projectID);
			Map<Integer, Set<Integer>> projectRoles = rolesForProjectIssueType
					.get(projectID);
			if (projectRoles != null) {
				for (Integer issueTypeID : issueTypesSet) {
					Map<Integer, Integer> projectIssueTypeRestrictions = new HashMap<Integer, Integer>();
					projectRestrictions.put(issueTypeID,
							projectIssueTypeRestrictions);
					Set<Integer> rolesWithIssueType = projectRoles
							.get(issueTypeID);
					Set<Integer> rolesWithoutIssueType = projectRoles
							.get(NO_ISSUETYPE_RESTRICTION);
					Set<Integer> roles = new HashSet<Integer>();
					if (rolesWithIssueType != null
							&& !rolesWithIssueType.isEmpty()) {
						roles.addAll(rolesWithIssueType);
					}
					if (rolesWithoutIssueType != null
							&& !rolesWithoutIssueType.isEmpty()) {
						roles.addAll(rolesWithoutIssueType);
					}
					if (!roles.isEmpty()) {
						if (edit) {
							Set<Integer> allRolesWithHiddenOrReadOnlyFields = new HashSet<Integer>();
							for (Set<Integer> rolesWithHiddenFields : hiddenFields
									.values()) {
								allRolesWithHiddenOrReadOnlyFields
										.addAll(rolesWithHiddenFields);
							}
							for (Set<Integer> rolesWithReadOnlyFields : readOnlyFields
									.values()) {
								allRolesWithHiddenOrReadOnlyFields
										.addAll(rolesWithReadOnlyFields);
							}
							if (allRolesWithHiddenOrReadOnlyFields
									.containsAll(roles)) {
								addRestrictions(projectIssueTypeRestrictions,
										roles, hiddenFields,
										TRoleFieldBean.ACCESSFLAG.NOACCESS);
								addRestrictions(projectIssueTypeRestrictions,
										roles, readOnlyFields,
										TRoleFieldBean.ACCESSFLAG.READ_ONLY);
							} else {
								LOGGER.debug("Role without restriction found for person "
										+ personID
										+ " project "
										+ projectID
										+ " and issueType " + issueTypeID);
								return resultMap;
							}
						} else {
							Set<Integer> allRolesWithHiddenFields = new HashSet<Integer>();
							for (Set<Integer> rolesWithHiddenFields : hiddenFields
									.values()) {
								allRolesWithHiddenFields
										.addAll(rolesWithHiddenFields);
							}
							if (allRolesWithHiddenFields.containsAll(roles)) {
								addRestrictions(projectIssueTypeRestrictions,
										roles, hiddenFields,
										TRoleFieldBean.ACCESSFLAG.NOACCESS);
							} else {
								LOGGER.debug("Role without restriction found for person "
										+ personID
										+ " project "
										+ projectID
										+ " and issueType " + issueTypeID);
								return resultMap;
							}
						}
					} else {
						LOGGER.debug(noRole + personID
								+ " in project " + projectID + " and issueType"
								+ issueTypeID);
					}
				}
			} else {
				LOGGER.debug(noRole + personID
						+ " in project " + projectID);
			}
		}
		return resultMap;
	}

	private static Integer NO_ISSUETYPE_RESTRICTION = Integer.valueOf(-100);

	/**
	 * Gets the roles for person in project for issueType
	 *
	 * @param personID
	 * @param projectToIssueTypesMap
	 * @return
	 */
	private static Map<Integer, Map<Integer, Set<Integer>>> getRolesForPersonInProjectsForIssueTypes(
			Integer personID, Map<Integer, Set<Integer>> projectToIssueTypesMap) {
		if (projectToIssueTypesMap == null
				|| projectToIssueTypesMap.isEmpty() || personID == null) {
			return new HashMap<Integer, Map<Integer, Set<Integer>>>();
		}
		/**
		 * Gets the ancestor projects hierarchy as a map
		 */
		Map<Integer, Integer> ancestorProjectHierarchy = ProjectBL
				.getAncestorProjectHierarchy(GeneralUtils
						.createIntegerArrFromCollection(projectToIssueTypesMap
								.keySet()));
		/**
		 * Get the projects and ancestor projects set
		 */
		Set<Integer> allProjectsSet = ancestorProjectHierarchy.keySet();
		List<Integer> personIDs = getMeAndSubstitutedAndGroups(personID);
		/**
		 * Gets the access control beans for all projects (direct and ancestor)
		 */
		List<TAccessControlListBean> acList = AccessControlBL
				.loadByPersonsAndProjects(personIDs,
						GeneralUtils.createListFromCollection(allProjectsSet));
		// all involved roles for person
		Set<Integer> allRoles = getRolesFromAcList(acList);
		/**
		 * Get the list type set for each involved role
		 */
		Map<Integer, Set<Integer>> rolesToListTypes = new HashMap<Integer, Set<Integer>>();
		List<TRoleListTypeBean> roleListTypeBeans = RoleBL
				.loadByRolesAndListType(allRoles.toArray(), null);
		if (roleListTypeBeans != null && !roleListTypeBeans.isEmpty()) {
			for (TRoleListTypeBean roleListTypeBean : roleListTypeBeans) {
				Integer roleID = roleListTypeBean.getRole();
				Integer issueType = roleListTypeBean.getListType();
				Set<Integer> issueTypes = rolesToListTypes.get(roleID);
				if (issueTypes == null) {
					issueTypes = new HashSet<Integer>();
					rolesToListTypes.put(roleID, issueTypes);
				}
				issueTypes.add(issueType);
			}
		}
		/**
		 * Build up the result map
		 */
		Map<Integer, Map<Integer, Set<Integer>>> resultMap = new HashMap<Integer, Map<Integer, Set<Integer>>>();
		/**
		 * Get the roles set for each involved project
		 */
		Map<Integer, Set<Integer>> rolesForProjects = new HashMap<Integer, Set<Integer>>();
		for (TAccessControlListBean accessControlListBean : acList) {
			Integer projectID = accessControlListBean.getProjectID();
			Integer roleID = accessControlListBean.getRoleID();
			Set<Integer> roleIDs = rolesForProjects.get(projectID);
			if (roleIDs == null) {
				roleIDs = new HashSet<Integer>();
				rolesForProjects.put(projectID, roleIDs);
			}
			roleIDs.add(roleID);
		}
		/**
		 * For each possible project/issueType gather the roles
		 */
		for (Integer projectID : projectToIssueTypesMap.keySet()) {
			// get direct roles in project
			Set<Integer> roleIDs = rolesForProjects.get(projectID);
			if (roleIDs == null) {
				// no direct role for project
				roleIDs = new HashSet<Integer>();
			}
			Integer parentProject = ancestorProjectHierarchy.get(projectID);
			while (parentProject != null) {
				// add the roles from ancestor projects
				Set<Integer> parentRoles = rolesForProjects.get(parentProject);
				if (parentRoles != null) {
					roleIDs.addAll(parentRoles);
				}
				parentProject = ancestorProjectHierarchy.get(parentProject);
			}
			Map<Integer, Set<Integer>> projectRolesMap = new HashMap<Integer, Set<Integer>>();
			resultMap.put(projectID, projectRolesMap);
			for (Integer roleID : roleIDs) {
				if (rolesToListTypes.containsKey(roleID)) {
					// role with issue type(s)
					Set<Integer> issueTypesForRole = rolesToListTypes
							.get(roleID);
					for (Integer issueTypeID : issueTypesForRole) {
						Set<Integer> rolesForProjectAndIssueType = projectRolesMap
								.get(issueTypeID);
						if (rolesForProjectAndIssueType == null) {
							rolesForProjectAndIssueType = new HashSet<Integer>();
							projectRolesMap.put(issueTypeID,
									rolesForProjectAndIssueType);
						}
						rolesForProjectAndIssueType.add(roleID);
					}
				} else {
					// role without issue type restrictions
					Set<Integer> rolesForProjectAndIssueType = projectRolesMap
							.get(NO_ISSUETYPE_RESTRICTION);
					if (rolesForProjectAndIssueType == null) {
						rolesForProjectAndIssueType = new HashSet<Integer>();
						projectRolesMap.put(NO_ISSUETYPE_RESTRICTION,
								rolesForProjectAndIssueType);
					}
					rolesForProjectAndIssueType.add(roleID);
				}
			}
		}
		return resultMap;
	}

	/****************************************** Role set for persons in projects ******************************************/

	/**
	 * Gets the list of accessControlListBeans by person (including groups the
	 * person is member of), project (also ancestor projects) and right flags
	 *
	 * @param personID
	 * @param projectID
	 * @param rights
	 * @return
	 */
	private static List<TAccessControlListBean> loadByPersonProjectsRight(
			Integer personID, Integer projectID, int[] rights,
			boolean includeSubstituted) {
		List<Integer> ancestorProjects = ProjectBL
				.getAncestorProjects(projectID);
		List<Integer> personIDs = null;
		if (includeSubstituted) {
			personIDs = getMeAndSubstitutedAndGroups(personID);
		} else {
			personIDs = getMeAndMyGroups(personID);
		}
		return AccessControlBL.loadByPersonProjectsRight(personIDs,
				ancestorProjects, rights);
	}

	/**
	 * Gets the list of accessControlListBeans by person (including groups the
	 * person is member of), project (also ancestor projects) and right flags
	 *
	 * @param personID
	 * @param projectIDs
	 * @param rights
	 * @return
	 */
	public static List<TAccessControlListBean> loadByPersonAndRight(
			Integer personID, int[] rights, boolean includeSubstituted) {
		List<Integer> personIDs;
		if (includeSubstituted) {
			personIDs = getMeAndSubstitutedAndGroups(personID);
		} else {
			personIDs = getMeAndMyGroups(personID);
		}
		return AccessControlBL.loadByPersonRightInAnyProjectWithStatusFlag(
				personIDs, rights, new int[] {
						TSystemStateBean.STATEFLAGS.ACTIVE,
						TSystemStateBean.STATEFLAGS.INACTIVE });
	}

	/**
	 * Gets the roles with right for persons in a project
	 *
	 * @param personID
	 * @param projectID
	 * @param arrRights
	 * @return
	 */
	public static Set<Integer> getRolesWithRightForPersonInProject(
			Integer personID, Integer projectID, int[] arrRights) {
		if (projectID == null || personID == null) {
			return new HashSet<Integer>();
		}
		List<TAccessControlListBean> acList = loadByPersonProjectsRight(
				personID, projectID, arrRights, true);
		return AccessBeans.getRolesFromAcList(acList);
	}

	/**
	 * Gets the status IDs assigned to the projectType for an issueType
	 *
	 * @param projectID
	 * @param rights
	 * @return
	 */
	public static Set<Integer> getRolesSetByProjectRights(Integer projectID,
			int[] rights) {
		Integer[] projectIDs = GeneralUtils
				.createIntegerArrFromCollection(ProjectBL
						.getAncestorProjects(projectID));
		List<TAccessControlListBean> accessControlListBeans = AccessControlBL
				.loadByProjectsAndRights(projectIDs, rights);
		return getRolesFromAcList(accessControlListBeans);
	}

	/**
	 * Gets the distinct roleIDs from a list of TAccessControlListBean beans
	 *
	 * @param acList
	 * @return
	 */
	private static Set<Integer> getRolesFromAcList(
			List<TAccessControlListBean> acList) {
		Set<Integer> roleSet = new HashSet<Integer>();
		if (acList != null) {
			for (TAccessControlListBean accessControlListBean : acList) {
				roleSet.add(accessControlListBean.getRoleID());
			}
		}
		return roleSet;
	}

	/**
	 * Returns whether a person has a specific right in a project for a listType
	 *
	 * @param personID
	 * @param projectID
	 * @return
	 */
	public static boolean isPersonProjectAdminForProject(Integer personID,
			Integer projectID, boolean includeSubstitutedPersons) {
		if (projectID == null || personID == null) {
			return false;
		}
		List<TAccessControlListBean> acList = loadByPersonProjectsRight(
				personID, projectID,
				new int[] { AccessFlagIndexes.PROJECTADMIN },
				includeSubstitutedPersons);
		// no right found at all
		if (acList == null || acList.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Returns whether a person has a specific right in a project for a listType
	 *
	 * @param personID
	 * @param projectID
	 * @param issueTypeID
	 *            if null, it does not matter (for example project admin role
	 *            doesn't depend on list type)
	 * @param right
	 *            the right flag
	 * @param projectAdmin
	 *            whether as project admin she has the right automatically
	 * @return
	 */
	public static boolean hasPersonRightInProjectForIssueType(Integer personID,
			Integer projectID, Integer issueTypeID, int right,
			boolean projectAdmin, boolean includeSubstitutedPersons) {
		if (projectID == null || personID == null) {
			return false;
		}
		int[] arrRights;
		if (projectAdmin && right != AccessFlagIndexes.PROJECTADMIN) {
			arrRights = new int[] { right, AccessFlagIndexes.PROJECTADMIN };
		} else {
			arrRights = new int[] { right };
		}
		List<TAccessControlListBean> acList = loadByPersonProjectsRight(
				personID, projectID, arrRights, includeSubstitutedPersons);
		// no right found at all
		if (acList == null || acList.isEmpty()) {
			return false;
		}
		// list type does not matter
		if (issueTypeID == null) {
			return true;
		}
		// Gather the found roleIDs in a Set because one could have the same
		// right direct and through groups
		Set<Integer> roles = getRolesFromAcList(acList);

		Object[] roleIDs = roles.toArray();

		// does at least one right have an implicit access to the list type?
		List<TRoleBean> issueTypeRoles = RoleBL
				.loadWithExplicitIssueType(roleIDs);
		// at least one right has no restrictions for listType
		if (issueTypeRoles != null && issueTypeRoles.size() < roles.size()) {
			return true;
		}
		// search for explicit rights for a listType
		List<TRoleListTypeBean> roleListTypeBeans = RoleBL
				.loadByRolesAndListType(roleIDs, issueTypeID);
		return roleListTypeBeans != null && !roleListTypeBeans.isEmpty();
	}

	/**
	 * Returns whether a person has a specific right in a project for a listType
	 *
	 * @param personID
	 * @param projectID
	 * @param issueTypeID
	 *            if null, it does not matter (for example project admin role
	 *            doesn't depend on list type)
	 * @param role
	 * @return
	 */
	public static boolean hasPersonRoleInProjectForIssueType(Integer personID,
			Integer projectID, Integer issueTypeID, Integer role) {
		if (projectID == null || personID == null) {
			return false;
		}
		List<TAccessControlListBean> acList = loadByPersonProjectsRole(
				personID, projectID, role);
		// no right found at all
		if (acList == null || acList.isEmpty()) {
			return false;
		}
		// list type does not matter
		if (issueTypeID == null) {
			return true;
		}
		// Gather the found roleIDs in a Set because one could have the same
		// right direct and through groups
		Set<Integer> roles = getRolesFromAcList(acList);
		Object[] roleIDs = roles.toArray();
		// does at least one right have an implicit access to the list type?
		List<TRoleBean> issueTypeRoles = RoleBL
				.loadWithExplicitIssueType(roleIDs);
		// at least one right has no restrictions for listType
		if (issueTypeRoles != null && issueTypeRoles.size() < roles.size()) {
			return true;
		}
		// search for explicit rights for a listType
		List<TRoleListTypeBean> roleListTypeBeans = RoleBL
				.loadByRolesAndListType(roleIDs, issueTypeID);
		return roleListTypeBeans != null && !roleListTypeBeans.isEmpty();
	}

	/**
	 * Gets the list of accessControlListBeans by person (including groups the
	 * person is member of), project (also ancestor projects) and role
	 *
	 * @param personID
	 * @param projectID
	 * @param role
	 * @return
	 */
	private static List<TAccessControlListBean> loadByPersonProjectsRole(
			Integer personID, Integer projectID, Integer role) {
		List<Integer> ancestorProjects = ProjectBL
				.getAncestorProjects(projectID);
		List<Integer> personIDs = getMeAndSubstitutedAndGroups(personID);
		return AccessControlBL.loadByPersonProjectsRole(personIDs,
				ancestorProjects, role);
	}

	/**
	 * Gets the "projectsIDs to issueTypeID set" map the person has rights in
	 *
	 * @param personIDs
	 * @param projects
	 * @param arrRights
	 * @return
	 */
	public static Map<Integer, Set<Integer>> getProjectsToIssueTypesWithRoleForPerson(
			List<Integer> personIDs, Integer[] projects, int[] arrRights) {
		List<Record> assignedProjectAndIssueTypeRecords = AccessControlBL.getProjectIssueTypeRecords(personIDs, projects, arrRights);
		Map<Integer, Set<Integer>> projectIssueTypeMap = new HashMap<Integer, Set<Integer>>();
		for (Record record : assignedProjectAndIssueTypeRecords) {
			try {
				Integer projectID = record.getValue(1).asIntegerObj();
				Integer issueTypeID = null;
				if (record.getValue(2) != null) {
					issueTypeID = record.getValue(2).asIntegerObj();
				}
				// check if projectID is already a key
				Set<Integer> issueTypeSet = projectIssueTypeMap.get(projectID);
				if (issueTypeSet == null) {
					// create new HashSet for this key
					issueTypeSet = new HashSet<Integer>();
					projectIssueTypeMap.put(projectID, issueTypeSet);
				}
				// add the issueTypeID. It could be null which
				// means no issueType restrictions for that project
				issueTypeSet.add(issueTypeID);
			} catch (DataSetException e) {
				LOGGER.error("Reading the result record failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return projectIssueTypeMap;
	}

	/**
	 * Verifies if a person has one of the specific rights for a list of
	 * workItemBeans. Specific rights means either a role with any arrRights or
	 * a RACI role If the person does not have the right, the workItemBean is
	 * removed form the list
	 *
	 * @param personID
	 *            , the id of the person
	 * @param workItemBeans
	 *            , list of reportBean which needs o be checked
	 * @param editFlagNeeded
	 *            whether the edit flag should be set for workItems
	 *
	 */
	public static List<TWorkItemBean> filterWorkItemBeans(Integer personID,
			List<TWorkItemBean> workItemBeans, boolean editFlagNeeded) {
		List<TWorkItemBean> workItemBeansPassed = new LinkedList<TWorkItemBean>();
		Set<Integer> workItemIDsPassed = new HashSet<Integer>();
		Map<Integer, TWorkItemBean> notEditableMap = new HashMap<Integer, TWorkItemBean>();
		Date start = null;
		if (LOGGER.isInfoEnabled()) {
			start = new Date();
		}
		if (personID == null || workItemBeans == null
				|| workItemBeans.isEmpty()) {
			LOGGER.debug("PersonID " + personID + " and number of items "
					+ workItemBeans.size());
			return new LinkedList<TWorkItemBean>();
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Person " + personID
					+ ": total number of items before filter "
					+ workItemBeans.size());
		}
		List<Integer> meAndSubstitutedIDs = getMeAndSubstituted(personID);
		/**
		 * First loop: pass the direct RACI (my) items
		 */
		Iterator<TWorkItemBean> workItemBeansItr = workItemBeans.iterator();
		while (workItemBeansItr.hasNext()) {
			TWorkItemBean workItemBean = workItemBeansItr.next();
			Integer workItemID = workItemBean.getObjectID();
			Integer originatorID = workItemBean.getOriginatorID();
			if (workItemBean.isAccessLevelFlag()
					&& !originatorID.equals(personID)) {
				// check the access level (private) flag: independently of any
				// other assigned roles or RACI roles
				// a workItem with accessLevel set to ACCESS_LEVEL_PRIVATE is
				// readable only when the person logged is the originator
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Remove item " + workItemID
							+ ": private issue");
				}
				workItemBeansItr.remove();
				continue;
			}
			Integer responsibleID = workItemBean.getResponsibleID();
			Integer managerID = workItemBean.getOwnerID();
			// pass the workItem and set as editable if it is my item
			if (meAndSubstitutedIDs.contains(originatorID)
					|| meAndSubstitutedIDs.contains(managerID)
					|| meAndSubstitutedIDs.contains(responsibleID)) {
				if (LOGGER.isTraceEnabled()) {
					LOGGER.trace(addItem + workItemID + ": direct RACI");
				}
				if (editFlagNeeded) {
					workItemBean.setEditable(true);
				}
				workItemBeansPassed.add(workItemBean);
				workItemIDsPassed.add(workItemBean.getObjectID());
				workItemBeansItr.remove();
			}
		}
		int noOfDirectRACIItems = workItemBeansPassed.size();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Number of direct RACI items: " + noOfDirectRACIItems
					+ ", " + workItemBeans.size() + remaining);
		}
		/**
		 * Second loop: pass by project roles for person
		 */
		List<Integer> meAndSubstitutedAndGroups = null;
		int noOfProjectRightItems = 0;
		if (!workItemBeans.isEmpty()) {
			meAndSubstitutedAndGroups = getMeAndSubstitutedAndGroups(personID);
			Set<Integer> projectsDirectlyInvolved = new HashSet<Integer>();
			for (TWorkItemBean workItemBean : workItemBeans) {
				Integer projectID = workItemBean.getProjectID();
				projectsDirectlyInvolved.add(projectID);
			}
			List<TProjectBean> projectBeansInvolved = ProjectBL
					.loadByProjectIDs(GeneralUtils
							.createListFromSet(projectsDirectlyInvolved));
			Map<Integer, TProjectBean> involvedProjectsMap = new HashMap<Integer, TProjectBean>();
			Map<Integer, Integer> childToParentProject = ProjectBL
					.getChildToParentMap(projectBeansInvolved,
							involvedProjectsMap);
			Integer[] ancestorProjects = GeneralUtils
					.createIntegerArrFromCollection(involvedProjectsMap
							.keySet());
			// get the array with groupIDs the person is member of (the personID
			// is also contained)

			// projects-issueTypes with project admin right for person

			// projects/issueTypes with projectAdmin roles

			// projects/issueTypes with read any roles

			int[] readAnyRights = new int[] { AccessFlagIndexes.READANYTASK,
					AccessFlagIndexes.PROJECTADMIN };
			// projects-issueTypes with read right for person
			Map<Integer, Set<Integer>> projectToIssueTypesWithReadRight = getProjectsToIssueTypesWithRoleForPerson(
					meAndSubstitutedAndGroups, ancestorProjects, readAnyRights);
			// projects-issueTypes with edit right for person
			Map<Integer, Set<Integer>> projectToIssueTypesWithEditRight = null;
			if (editFlagNeeded) {
				int[] editAnyRights;
				editAnyRights = new int[] { AccessFlagIndexes.MODIFYANYTASK,
						AccessFlagIndexes.PROJECTADMIN };
				projectToIssueTypesWithEditRight = getProjectsToIssueTypesWithRoleForPerson(
						meAndSubstitutedAndGroups, ancestorProjects,
						editAnyRights);
			}
			workItemBeansItr = workItemBeans.iterator();
			while (workItemBeansItr.hasNext()) {
				TWorkItemBean workItemBean = workItemBeansItr.next();
				Integer workItemID = workItemBean.getObjectID();
				Integer projectID = workItemBean.getProjectID();
				Integer issueType = workItemBean.getListTypeID();
				// leave the archived/deleted workItems only if the person is
				// project admin
				if (editFlagNeeded
						&& hasExplicitRight(personID, workItemID, projectID,
								issueType, projectToIssueTypesWithEditRight,
								childToParentProject, "edit")) {
					// explicit edit right: pass and set as editable
					if (LOGGER.isTraceEnabled()) {
						String projectName = null;
						ILabelBean projectBean = LookupContainer
								.getProjectBean(projectID);
						if (projectBean != null) {
							projectName = projectBean.getLabel();
						}
						LOGGER.trace(addItem + workItemID + " for user "
								+ personID
								+ ": 'modify any' project right for project "
								+ projectID + " (" + projectName + ")");
					}
					if (!workItemIDsPassed.contains(workItemBean.getObjectID())) {
						workItemIDsPassed.add(workItemBean.getObjectID());
						workItemBeansPassed.add(workItemBean);
						workItemBean.setEditable(true);
					}
					workItemBeansItr.remove();
				} else {
					if (hasExplicitRight(personID, workItemID, projectID,
							issueType, projectToIssueTypesWithReadRight,
							childToParentProject, "read")) {
						// explicit read right: pass but do not set as editable
						if (!workItemIDsPassed.contains(workItemBean
								.getObjectID())) {
							workItemIDsPassed.add(workItemBean.getObjectID());
							workItemBeansPassed.add(workItemBean);
							if (LOGGER.isTraceEnabled()) {
								String projectName = null;
								ILabelBean projectBean = LookupContainer
										.getProjectBean(projectID);
								if (projectBean != null) {
									projectName = projectBean.getLabel();
								}
								LOGGER.trace(addItem
										+ workItemID
										+ ": 'read any' project right for project "
										+ projectID + " (" + projectName + ")");
							}
						}
						if (editFlagNeeded) {
							// although it is readable but not editable though
							// project level roles: if edit flag is needed do
							// not remove
							// to remain in set by searching for indirect RACI
							// and possibly set it editable later
							notEditableMap.put(workItemID, workItemBean);
						} else {
							// remove to do not take care about in next loops
							workItemBeansItr.remove();
						}
					}
				}
			}
			noOfProjectRightItems = workItemBeansPassed.size()
					- noOfDirectRACIItems;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Number of items in projects with role for person: "
						+ noOfProjectRightItems
						+ ", "
						+ workItemBeans.size() + remaining);
			}
		}
		/**
		 * Third loop: pass by indirect RACI persons: responsible through group,
		 * reciprocal RACI through group membership in the same group
		 */
		int noOfIndirectRACIItems = 0;
		Set<Integer> reciprocOriginators = null;
		if (!workItemBeans.isEmpty()) {
			Map<Integer, List<Integer>> reciprocGroups = getRaciprocGroupsMap(meAndSubstitutedIDs);
			List<Integer> allGroupsForPerson = reciprocGroups
					.get(ALL_PERSONGROUPS);
			List<TPersonBean> originatorsInReciprocGroups = PersonBL
					.getIndirectPersons(
							reciprocGroups.get(SystemFields.ORIGINATOR), false,
							null);
			reciprocOriginators = GeneralUtils
					.createIntegerSetFromIntegerList(GeneralUtils
							.createIntegerListFromBeanList(originatorsInReciprocGroups));
			List<TPersonBean> managersInReciprocGroups = PersonBL
					.getIndirectPersons(
							reciprocGroups.get(SystemFields.MANAGER), false,
							null);
			Set<Integer> reciprocManagers = GeneralUtils
					.createIntegerSetFromIntegerList(GeneralUtils
							.createIntegerListFromBeanList(managersInReciprocGroups));
			List<TPersonBean> responsiblesInReciprocGroups = PersonBL
					.getIndirectPersons(
							reciprocGroups.get(SystemFields.RESPONSIBLE),
							false, null);
			Set<Integer> reciprocResponsibles = GeneralUtils
					.createIntegerSetFromIntegerList(GeneralUtils
							.createIntegerListFromBeanList(responsiblesInReciprocGroups));
			workItemBeansItr = workItemBeans.iterator();
			while (workItemBeansItr.hasNext()) {
				TWorkItemBean workItemBean = workItemBeansItr.next();
				Integer workItemID = workItemBean.getObjectID();
				Integer originatorID = workItemBean.getOriginatorID();
				Integer responsibleID = workItemBean.getResponsibleID();
				Integer managerID = workItemBean.getOwnerID();
				if ((allGroupsForPerson != null && allGroupsForPerson
						.contains(responsibleID))
						|| reciprocOriginators.contains(originatorID)
						|| reciprocManagers.contains(managerID)
						|| reciprocResponsibles.contains(responsibleID)) {
					// indirect RACI: set as editable
					if (LOGGER.isTraceEnabled()) {
						if (allGroupsForPerson != null
								&& allGroupsForPerson.contains(responsibleID)) {
							LOGGER.trace(addItem + workItemID
									+ ": responsible through group");
						} else {
							if (reciprocOriginators.contains(originatorID)) {
								LOGGER.trace(addItem + workItemID
										+ ": reciproc originator through group");
							} else {
								if (reciprocManagers.contains(managerID)) {
									LOGGER.trace(addItem
											+ workItemID
											+ ": reciproc manager through group");
								} else {
									if (reciprocResponsibles
											.contains(responsibleID)) {
										LOGGER.trace(addItem
												+ workItemID
												+ ": reciproc responsible through group");
									}
								}
							}
						}
					}
					// set it to editable anyway but add to the passed list if
					// it was not yet added as read only (to avoid adding the
					// item two times)
					workItemBeansItr.remove();
					if (!workItemIDsPassed.contains(workItemBean.getObjectID())) {
						workItemIDsPassed.add(workItemBean.getObjectID());
						workItemBeansPassed.add(workItemBean);
					}
					if (editFlagNeeded) {
						workItemBean.setEditable(true);
						if (notEditableMap.containsKey(workItemID)) {
							notEditableMap.remove(workItemID);
						}
					}
				}
			}
			noOfIndirectRACIItems = workItemBeansPassed.size()
					- noOfDirectRACIItems - noOfProjectRightItems;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Number of indirect RACI items: "
						+ noOfIndirectRACIItems + ", "
						+ workItemBeans.size() + remaining);
			}
		}
		/**
		 * Fourth loop: pass by watcher person
		 */
		int noOfWatcherItems = 0;
		if (!workItemBeans.isEmpty()) {
			List<Integer> workItemIDs = GeneralUtils
					.createIntegerListFromBeanList(workItemBeans);
			List<TNotifyBean> watcherBeans = ConsInfBL
					.getByWorkItemsAndPersons(workItemIDs,
							meAndSubstitutedAndGroups);
			if (watcherBeans != null && !watcherBeans.isEmpty()) {
				Set<Integer> consultedPersons = new HashSet<Integer>();
				Set<Integer> informedPersons = new HashSet<Integer>();
				for (TNotifyBean notifyBean : watcherBeans) {
					Integer workItemID = notifyBean.getWorkItem();
					String raciRole = notifyBean.getRaciRole();
					if (raciRole != null) {
						if (RaciRole.CONSULTANT.equals(raciRole)) {
							consultedPersons.add(workItemID);
						} else {
							if (RaciRole.INFORMANT.equals(raciRole)) {
								informedPersons.add(workItemID);
							}
						}
					}
				}
				workItemBeansItr = workItemBeans.iterator();
				while (workItemBeansItr.hasNext()) {
					TWorkItemBean workItemBean = workItemBeansItr.next();
					Integer workItemID = workItemBean.getObjectID();
					if (consultedPersons.contains(workItemID)) {
						// consulted: pass and set editable
						if (LOGGER.isTraceEnabled()) {
							LOGGER.trace(addItem + workItemID
									+ ": consulted");
						}
						// set it to editable anyway but add to the passed list
						// if it was not yet added as read only (to avoid adding
						// the item two times)
						workItemBeansItr.remove();
						if (!workItemIDsPassed.contains(workItemBean
								.getObjectID())) {
							workItemIDsPassed.add(workItemBean.getObjectID());
							workItemBeansPassed.add(workItemBean);
						}
						if (editFlagNeeded) {
							workItemBean.setEditable(true);
							if (notEditableMap.containsKey(workItemID)) {
								notEditableMap.remove(workItemID);
							}
						}
					} else {
						// not already "only" readable through project roles
						if (informedPersons.contains(workItemID)) {
							if (!workItemIDsPassed.contains(workItemBean
									.getObjectID())) {
								workItemIDsPassed.add(workItemBean
										.getObjectID());
								workItemBeansPassed.add(workItemBean);
							}
							// informed: pass but do not set as editable
							// although it is readable but not editable: if edit
							// flag is needed do not remove
							// to remain in set by searching for indirect RACI
							// and possibly set it editable later
							if (LOGGER.isTraceEnabled()) {
								LOGGER.trace(addItem + workItemID
										+ ": informed");
							}

							if (editFlagNeeded) {
								// although it is readable but not editable: if
								// edit flag is needed do not remove
								// to remain in set by searching for indirect
								// RACI and possibly set it editable later
								notEditableMap.put(workItemID, workItemBean);
							} else {
								// remove to do not take care about in next
								// loops
								workItemBeansItr.remove();
							}
						}

					}
				}
			}
			noOfWatcherItems = workItemBeansPassed.size() - noOfDirectRACIItems
					- noOfProjectRightItems - noOfIndirectRACIItems;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Number of watcher items " + noOfWatcherItems
						+ ", " + workItemBeans.size() + remaining);
			}
		}
		/**
		 * Fifth loop: pass by on behalf persons
		 */
		int noOfOnBehalfItems = 0;
		if (!workItemBeans.isEmpty()) {
			List<Integer> workItemIDs = GeneralUtils
					.createIntegerListFromBeanList(workItemBeans);
			List<TAttributeValueBean> userPickerAttributes = AttributeValueBL
					.getUserPickerAttributesByWorkItems(workItemIDs);
			Map<Integer, List<TAttributeValueBean>> userPickerMap = null;
			if (userPickerAttributes != null && !userPickerAttributes.isEmpty()) {
				userPickerMap = new HashMap<Integer, List<TAttributeValueBean>>();
				for (TAttributeValueBean attributeValueBean : userPickerAttributes) {
					Integer workItemID = attributeValueBean.getWorkItem();
					List<TAttributeValueBean> attributeValueBeansList = userPickerMap
							.get(workItemID);
					if (attributeValueBeansList == null) {
						attributeValueBeansList = new LinkedList<TAttributeValueBean>();
						userPickerMap.put(workItemID, attributeValueBeansList);
					}
					attributeValueBeansList.add(attributeValueBean);
				}
				workItemBeansItr = workItemBeans.iterator();
				while (workItemBeansItr.hasNext()) {
					TWorkItemBean workItemBean = workItemBeansItr.next();
					Integer workItemID = workItemBean.getObjectID();
					Set<Integer> onBehalfOfSet = null;
					List<TAttributeValueBean> attributeValueBeansList = userPickerMap
							.get(workItemID);
					if (attributeValueBeansList != null) {
						for (TAttributeValueBean attributeValueBean : attributeValueBeansList) {
							Integer fieldID = attributeValueBean.getField();
							IFieldTypeRT fieldTypeRT = FieldTypeManager
									.getFieldTypeRT(fieldID);
							if (fieldTypeRT.isUserPicker()) {
								UserPickerRT userPickerRT = (UserPickerRT) fieldTypeRT;
								if (userPickerRT.inheritsOriginatorRole()) {
									if (onBehalfOfSet == null) {
										onBehalfOfSet = new HashSet<Integer>();
									}
									onBehalfOfSet.add(attributeValueBean
											.getSystemOptionID());
								}
							}
						}
					}
					Set<Integer> onBehalfPersonsAll = new HashSet<Integer>();
					Set<Integer> onBehalfPersonsDirect = getOnBehalfPersonsDirect(
							onBehalfOfSet, onBehalfPersonsAll);
					if (hasCommon(meAndSubstitutedIDs, onBehalfPersonsAll)
							|| hasCommon(reciprocOriginators,
									onBehalfPersonsDirect)) {
						// indirect RACI: set as editable
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(addItem + workItemID
									+ ": on behalf of");
						}
						// set it to editable anyway but add to the passed list
						// if it was not yet added as read only (to avoid adding
						// the item two times)
						workItemBeansItr.remove();
						if (!workItemIDsPassed.contains(workItemBean
								.getObjectID())) {
							workItemIDsPassed.add(workItemBean.getObjectID());
							workItemBeansPassed.add(workItemBean);
						}
						if (editFlagNeeded) {
							workItemBean.setEditable(true);
							if (notEditableMap.containsKey(workItemID)) {
								notEditableMap.remove(workItemID);
							}
						}
					}
				}
			}
			noOfOnBehalfItems = workItemBeansPassed.size()
					- noOfDirectRACIItems - noOfProjectRightItems
					- noOfIndirectRACIItems - noOfWatcherItems;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Number of in behalf of person items "
						+ noOfOnBehalfItems + ", "
						+ workItemBeans.size() +  remaining);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			// workItemBeans contain the
			// 1. not readable and
			// 2. might contain also the only readable but not editable items
			// (these items are passed already)
			// The difference is the really not readable items
			LOGGER.debug("Number of items remained with no read right "
					+ (workItemBeans.size() - notEditableMap.size()));
			LOGGER.debug("Number of items passed " + workItemBeansPassed.size());
			Date end = new Date();
			LOGGER.debug("Removing not accessible items lasted "
					+ Long.toString(end.getTime() - start.getTime())
					+ " ms");
		}
		return workItemBeansPassed;
	}

	/**
	 * Has common persons
	 *
	 * @param reciprocPersonsSet
	 * @param onBehalfOfSet
	 * @return
	 */
	private static boolean hasCommon(Collection<Integer> reciprocPersonsSet,
			Set<Integer> onBehalfOfSet) {
		if (reciprocPersonsSet == null || onBehalfOfSet == null) {
			return false;
		}
		for (Integer personID : onBehalfOfSet) {
			if (reciprocPersonsSet.contains(personID)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Whether a workItem belongs to a project or project/issueType with an
	 * explicit right
	 *
	 * @param personID
	 * @param workItemID
	 * @param projectID
	 * @param issueType
	 * @param projectIssueTypeMap
	 * @param childToParentProject
	 * @return
	 */
	public static boolean hasExplicitRight(Integer personID,
			Integer workItemID, Integer projectID, Integer issueType,
			Map<Integer, Set<Integer>> projectIssueTypeMap,
			Map<Integer, Integer> childToParentProject, String rightName) {
		if (projectIssueTypeMap == null || projectID == null) {
			return false;
		}
		// get the Set for list-types for project:
		Set<Integer> listSet = projectIssueTypeMap.get(projectID);
		if (listSet != null
				&& (listSet.contains(issueType) || listSet.contains(null))) {
			return true;
		} else {
			Integer parentProject = childToParentProject.get(projectID);
			if (parentProject == null) {
				if (LOGGER.isTraceEnabled()) {
					String projectName = null;
					TProjectBean projectBean = LookupContainer
							.getProjectBean(projectID);
					if (projectBean != null) {
						projectName = projectBean.getLabel();
					}
					String issueTypeName = null;
					if (issueType != null) {
						TListTypeBean issueTypeBean = LookupContainer
								.getItemTypeBean(issueType);
						if (issueTypeBean != null) {
							issueTypeName = issueTypeBean.getLabel();
						}
					}
					LOGGER.trace("User " + personID + " has no " + rightName
							+ " right for item " + workItemID + " in project "
							+ projectID + " (" + projectName + ")"
							+ " issueType " + issueType + " (" + issueTypeName
							+ ")");
				}
				return false;
			} else {
				return hasExplicitRight(personID, workItemID, parentProject,
						issueType, projectIssueTypeMap, childToParentProject,
						rightName);
			}
		}
	}

	/**
	 * Gets the item type limitations for a project directly or inherited from
	 * ancestor projects
	 *
	 * @param projectID
	 * @param selectedItemTypeIDsSet
	 * @param projectIssueTypeMap
	 * @param childToParentProjectIDMap
	 * @return
	 */
	public static Set<Integer> getItemTypeLimitations(Integer projectID,
			Set<Integer> selectedItemTypeIDsSet,
			Map<Integer, Set<Integer>> projectIssueTypeMap,
			Map<Integer, Integer> childToParentProjectIDMap) {
		if (projectIssueTypeMap == null || projectID == null) {
			return null;
		}
		String projectLabel = null;
		if (LOGGER.isDebugEnabled()) {
			projectLabel = LookupContainer.getNotLocalizedLabelBeanLabel(
					SystemFields.INTEGER_PROJECT, projectID);
		}
		// get the Set for list-types for project:
		Set<Integer> roleRestrictedItemTypeIDSet = projectIssueTypeMap
				.get(projectID);
		if (roleRestrictedItemTypeIDSet != null) {
			if (roleRestrictedItemTypeIDSet.contains(null)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(theProject + projectLabel
							+ " has role without item type limitation");
				}
				// no limitation on item types not needed to look at the parent
				// projects' roles
				return roleRestrictedItemTypeIDSet;
			} else {
				Set<Integer> parentItemTypeLimitations = null;
				Integer parentProjectID = childToParentProjectIDMap
						.get(projectID);
				String parentProjectLabel = null;
				if (parentProjectID != null) {
					parentItemTypeLimitations = getItemTypeLimitations(
							parentProjectID, selectedItemTypeIDsSet,
							projectIssueTypeMap, childToParentProjectIDMap);
					if (LOGGER.isDebugEnabled()) {
						parentProjectLabel = LookupContainer
								.getNotLocalizedLabelBeanLabel(
										SystemFields.INTEGER_PROJECT,
										parentProjectID);
					}
					if (parentItemTypeLimitations != null
							&& parentItemTypeLimitations.contains(null)) {
						LOGGER.debug("The parent project " + parentProjectLabel
								+ " has role without item type limitation");
						return parentItemTypeLimitations;
					}
				}
				if (selectedItemTypeIDsSet == null
						|| selectedItemTypeIDsSet.isEmpty()) {
					LOGGER.debug(theProject
							+ projectLabel
							+ " has role with item type limitation (no selected item type in filter)");
					if (parentItemTypeLimitations != null) {
						roleRestrictedItemTypeIDSet
								.addAll(parentItemTypeLimitations);
						LOGGER.debug(theProject
								+ projectLabel
								+ " inherits role with item type limitation (no selected item type in filter) from parent project "
								+ parentProjectLabel);
					}
					return roleRestrictedItemTypeIDSet;
				} else {
					// clone the roleRestrictedItemTypeIDSet because retainAll
					// modifies the set
					Set<Integer> intersection = new HashSet<Integer>(
							roleRestrictedItemTypeIDSet);
					LOGGER.debug(theProject
							+ projectLabel
							+ " has role with item type limitation (with selected item type in filter)");
					intersection.retainAll(selectedItemTypeIDsSet);
					if (parentItemTypeLimitations != null) {
						LOGGER.debug(theProject
								+ projectLabel
								+ " inherits role with item type limitation (with selected item type in filter) from parent project "
								+ parentProjectLabel);
						intersection.addAll(parentItemTypeLimitations);
					}
					return intersection;
				}
			}
		} else {
			LOGGER.debug(theProject + projectLabel + " has no direct role ");
			Integer parentProjectID = childToParentProjectIDMap.get(projectID);
			if (parentProjectID != null) {
				Set<Integer> parentItemTypeLimitations = getItemTypeLimitations(
						parentProjectID, selectedItemTypeIDsSet,
						projectIssueTypeMap, childToParentProjectIDMap);
				if (parentItemTypeLimitations != null) {
					String parentProjectLabel = null;
					if (LOGGER.isDebugEnabled()) {
						parentProjectLabel = LookupContainer
								.getNotLocalizedLabelBeanLabel(
										SystemFields.INTEGER_PROJECT,
										parentProjectID);
					}
					if (parentItemTypeLimitations.contains(null)) {
						LOGGER.debug("The parent project " + parentProjectLabel
								+ " has role without item type limitation");
						return parentItemTypeLimitations;
					} else {
						LOGGER.debug(theProject
								+ projectLabel
								+ " inherits role  with item type limitation from parent project "
								+ parentProjectLabel);
						return parentItemTypeLimitations;
					}
				}
			}
			return null;
		}
	}

	/**
	 * Get the all project-issueTypes for a list of workitems
	 *
	 * @param workItemBeanList
	 * @return
	 */
	public static Map<Integer, Set<Integer>> getProjectToIssueTypesMap(
			Collection<TWorkItemBean> workItemBeanList) {
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap<Integer, Set<Integer>>();
		if (workItemBeanList != null) {
			for (TWorkItemBean workItemBean : workItemBeanList) {
				Integer projectID = workItemBean.getProjectID();
				Integer issueTypeID = workItemBean.getListTypeID();
				Set<Integer> issueTypes = projectToIssueTypesMap.get(projectID);
				if (issueTypes == null) {
					issueTypes = new HashSet<Integer>();
					projectToIssueTypesMap.put(projectID, issueTypes);
				}
				issueTypes.add(issueTypeID);
			}
		}
		return projectToIssueTypesMap;
	}

	/**
	 * Get the all project-issueTypes for a list of workitems
	 *
	 * @param reportBeanWithHistoryList
	 * @return
	 */
	private static Map<Integer, Set<Integer>> getProjectToIssueTypesMap(
			List<ReportBeanWithHistory> reportBeanWithHistoryList) {
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap<Integer, Set<Integer>>();
		if (reportBeanWithHistoryList != null) {
			for (ReportBean reportBean : reportBeanWithHistoryList) {
				TWorkItemBean workItemBean = reportBean.getWorkItemBean();
				Integer projectID = workItemBean.getProjectID();
				Integer issueTypeID = workItemBean.getListTypeID();
				Set<Integer> issueTypes = projectToIssueTypesMap.get(projectID);
				if (issueTypes == null) {
					issueTypes = new HashSet<Integer>();
					projectToIssueTypesMap.put(projectID, issueTypes);
				}
				issueTypes.add(issueTypeID);
			}
		}
		return projectToIssueTypesMap;
	}

	/**
	 * Filters out the cost beans from the reportBeanWithHistory objects when it
	 * is not the person's own cost and she has no right to see the others'
	 * costs
	 *
	 * @param personID
	 * @param reportBeanWithHistoryList
	 */
	public static void filterCostBeans(Integer personID,
			List<ReportBeanWithHistory> reportBeanWithHistoryList) {
		if (personID == null || reportBeanWithHistoryList == null
				|| reportBeanWithHistoryList.isEmpty()) {
			return;
		}
		Map<Integer, Set<Integer>> projectToIssueTypesMap = getProjectToIssueTypesMap(reportBeanWithHistoryList);
		List<Integer> fieldIDs = new LinkedList<Integer>();
		fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
		Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions = getFieldRestrictions(
				personID, projectToIssueTypesMap, fieldIDs, false);
		for (ReportBeanWithHistory reportBean : reportBeanWithHistoryList) {
			TWorkItemBean workItemBean = reportBean.getWorkItemBean();
			List<TCostBean> costBeans = reportBean.getCosts();
			// get the category label
			if (costBeans != null && !costBeans.isEmpty()) {
				Integer projectID = workItemBean.getProjectID();
				Integer issueTypeID = workItemBean.getListTypeID();
				Map<Integer, Map<Integer, Integer>> issueTypeRestrictions = fieldRestrictions
						.get(projectID);
				Map<Integer, Integer> hiddenFields = null;
				if (issueTypeRestrictions != null) {
					hiddenFields = issueTypeRestrictions.get(issueTypeID);
				}
				if (hiddenFields != null
						&& hiddenFields
								.containsKey(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES)) {
					Iterator<TCostBean> iterator = costBeans.iterator();
					while (iterator.hasNext()) {
						TCostBean costBean = iterator.next();
						if (!personID.equals(costBean.getChangedByID())) {
							iterator.remove();
						}
					}
				}
			}
		}
	}

	/**
	 * Filters out the cost beans from the reportBeanWithHistory objects when it
	 * is not the person's own cost and he/she has no right to see the others'
	 * costs
	 *
	 * @param costBeans
	 * @param personID
	 * @param workItemBeansMap
	 */
	public static void filterCostBeans(List<TCostBean> costBeans,
			Integer personID, Map<Integer, TWorkItemBean> workItemBeansMap) {
		if (personID == null || costBeans == null || costBeans.isEmpty()) {
			return;
		}
		Map<Integer, Set<Integer>> projectToIssueTypesMap = getProjectToIssueTypesMap(workItemBeansMap
				.values());
		List<Integer> fieldIDs = new LinkedList<Integer>();
		fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
		Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions = getFieldRestrictions(
				personID, projectToIssueTypesMap, fieldIDs, false);
		for (Iterator<TCostBean> itrCostBeans = costBeans.iterator(); itrCostBeans
				.hasNext();) {
			TCostBean costBean = itrCostBeans.next();
			Integer changedBy = costBean.getChangedByID();
			if (changedBy != null && !changedBy.equals(personID)) {
				// my expenses are always visible
				Integer workItemID = costBean.getWorkItemID();
				TWorkItemBean workItemBean = workItemBeansMap.get(workItemID);
				Integer projectID = workItemBean.getProjectID();
				Integer issueTypeID = workItemBean.getListTypeID();
				Map<Integer, Map<Integer, Integer>> issueTypeRestrictions = fieldRestrictions
						.get(projectID);
				Map<Integer, Integer> hiddenFields = null;
				if (issueTypeRestrictions != null) {
					hiddenFields = issueTypeRestrictions.get(issueTypeID);
				}
				if (hiddenFields != null
						&& hiddenFields
								.containsKey(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES)) {
					itrCostBeans.remove();
				}
			}
		}
	}

	/**
	 * Filters out the cost beans from the reportBeanWithHistory objects when it
	 * is not the person's own cost and he/she has no right to see the others'
	 * costs
	 *
	 * @param budgetBeans
	 * @param personID
	 * @param workItemBeansMap
	 */
	public static void filterBudgetBeans(List<TBudgetBean> budgetBeans,
			Integer personID, Map<Integer, TWorkItemBean> workItemBeansMap) {
		if (personID == null || budgetBeans == null || budgetBeans.isEmpty()) {
			return;
		}
		Map<Integer, Set<Integer>> projectToIssueTypesMap = getProjectToIssueTypesMap(workItemBeansMap
				.values());
		List<Integer> fieldIDs = new LinkedList<Integer>();
		fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
		fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET);
		Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions = getFieldRestrictions(
				personID, projectToIssueTypesMap, fieldIDs, false);
		for (Iterator<TBudgetBean> iterator = budgetBeans.iterator(); iterator
				.hasNext();) {
			TBudgetBean budgetBean = iterator.next();
			Integer changedBy = budgetBean.getChangedByID();
			if (changedBy != null && !changedBy.equals(personID)) {
				Integer budgetType = budgetBean.getBudgetType();
				Integer workItemID = budgetBean.getWorkItemID();
				TWorkItemBean workItemBean = workItemBeansMap.get(workItemID);
				Integer projectID = workItemBean.getProjectID();
				Integer issueTypeID = workItemBean.getListTypeID();
				Map<Integer, Map<Integer, Integer>> issueTypeRestrictions = fieldRestrictions
						.get(projectID);
				Map<Integer, Integer> hiddenFields = null;
				if (issueTypeRestrictions != null) {
					hiddenFields = issueTypeRestrictions.get(issueTypeID);
				}
				if (hiddenFields != null) {
					if ((budgetType == null || TBudgetBean.BUDGET_TYPE.PLANNED_VALUE
							.equals(budgetType))
							&& hiddenFields
									.containsKey(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN)) {
						iterator.remove();
					} else {
						if ((TBudgetBean.BUDGET_TYPE.BUDGET.equals(budgetType))
								&& hiddenFields
										.containsKey(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET)) {
							iterator.remove();
						}
					}
				}
			}
		}
	}

	public static final int NUMBER_OF_ACCESS_FLAGS = 11;

	/**
	 * The positional constants from the extendedAccessKey for permission
	 * definitions. Free positions between defined positions are intentional.
	 *
	 */
	public static class AccessFlagIndexes {

		private AccessFlagIndexes(){
		}

		/**
		 * Permission to read any issue.
		 */
		public static final int READANYTASK = 0;

		/**
		 * Permission to modify any issue.
		 */
		public static final int MODIFYANYTASK = 1;

		/**
		 * Permission to create issues.
		 */
		public static final int CREATETASK = 2;

		/**
		 * Permission to close any task.
		 */
		public static final int CLOSEANYTASK = 3;

		/**
		 * Permission to close a task if the user is the current responsible.
		 */
		public static final int CLOSETASKIFRESPONSIBLE = 4;

		/**
		 * Permission to close a task if the user is the current manager or
		 * original author.
		 */
		public static final int CLOSETASKIFMANAGERORORIGINATOR = 5;

		/**
		 * Not really a permission, but rather a flag to include this user in
		 * the selection box for managers.
		 */
		public static final int MANAGER = 6;

		/**
		 * Not really a permission, but rather a flag to include this user in
		 * the selection box for responsibles.
		 */
		public static final int RESPONSIBLE = 7;

		/**
		 * Not really a permission, but rather a flag to include this user in
		 * the selection box for consulted.
		 */
		public static final int CONSULTANT = 8;

		/**
		 * Not really a permission, but rather a flag to include this user in
		 * the selection box for informed.
		 */
		public static final int INFORMANT = 9;

		/**
		 * This user has project administrator rights, which comprise a whole
		 * lot of specific permissions like being able to assign roles to users
		 * in this project, and so on.
		 */
		public static final int PROJECTADMIN = 10;

		/**
		 * Permission to add, modify, and delete budgets (planned values).
		 *
		 * @deprecated
		 */
		@Deprecated
		public static final int ADDMODIFYDELETEBUDGET = 12;

		/**
		 * Permission to add, modify, and delete due dates.
		 *
		 * @deprecated
		 */
		@Deprecated
		public static final int ADDMODIFYDELETEDUEDATES = 13;

		/**
		 * Permission to modify the users own work and cost entries.
		 *
		 * @deprecated
		 */
		@Deprecated
		public final static int ADDMODIFYDELETEOWNHOURSCOST = 14;

		/**
		 * Permission to view all work and cost entries, not just the users own.
		 *
		 * @deprecated
		 */
		@Deprecated
		public static final int VIEWALLHOURSCOST = 15;

		/**
		 * Permission to assign a task to a new responsible or manager.
		 *
		 * @deprecated
		 */
		@Deprecated
		public static final int ASSIGNTASKTORESPONSIBLEORMANAGER = 16;

		/**
		 * @deprecated
		 */
		@Deprecated
		public static final int MODIFYCONSULTANTSINFORMANTANDOTHERWATCHERS = 17;

		/**
		 * Permission to modify the list of consulted and informed.
		 *
		 * @deprecated
		 */
		public static final int VIEWCONSULTANTSINFORMANTANDOTHERWATCHERS = 18;


		/**
		 * Permission to modify the issue title (synopsis) and description.
		 * @deprecated
		 */
		@Deprecated
		public static final int MODIFYSYNOPSISORDESCRIPTION = 20;

	}
}
