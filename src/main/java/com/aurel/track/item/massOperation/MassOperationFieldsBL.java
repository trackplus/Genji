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

package com.aurel.track.item.massOperation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.customize.treeConfig.field.CustomListsConfigBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.admin.customize.treeConfig.screen.ScreenConfigBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.custom.select.CustomSelectBaseRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.util.GeneralUtils;

/**
 * Helper class for getting the present fields according to the workItems selected 
 * @author Tamas
 *
 */
public class MassOperationFieldsBL {
	private static final Logger LOGGER = LogManager.getLogger(MassOperationFieldsBL.class);

	/**
	 * Get present fields set
	 * @param massOperationContext
	 * @param selectedWorkItemBeans
	 * @param projectRefresh
	 * @param issueTypeRefresh
	 * @param personBean
	 * @return
	 */
	public static Set<Integer> getPresentFieldsOnFormsSet(MassOperationContext massOperationContext, TPersonBean personBean) {
		Map<Integer, Set<Integer>> projectIssueTypeContexts = massOperationContext.getProjectIssueTypeContexts();
		//Map<Integer, TProjectBean> projectsMap = massOperationContext.getProjectsMap(); 
		//get the screens assigned for actions in each context
		Map<Integer, Map<Integer, Map<Integer, Integer>>> screensForActionsInContexts = 
			getScreensForActionsInContexts(projectIssueTypeContexts/*, projectsMap*/);
		//fields in each involved screen
		Map<Integer, List<Integer>> fieldsForScreens = getFieldsListForScreens(screensForActionsInContexts);
		//get the intersection of the fields present in each context (in any of the action screens)
		Set<Integer> presentFieldsSet = getFieldsPresentInEachConext(screensForActionsInContexts, fieldsForScreens);
		//do we have field restrictions on any field 
		Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions =
				AccessBeans.getFieldRestrictions(personBean.getObjectID(), projectIssueTypeContexts, null, true);
		for (Integer projectID : projectIssueTypeContexts.keySet()) {
			Set<Integer> issueTypes = projectIssueTypeContexts.get(projectID);
			Map<Integer, Map<Integer, Integer>> issueTypeFieldRestrictions = fieldRestrictions.get(projectID);
			if (issueTypeFieldRestrictions!=null) {
				if (issueTypes!=null) {
					for (Integer issueType : issueTypeFieldRestrictions.keySet()) {
						Map<Integer, Integer> fieldRestrictionMap = issueTypeFieldRestrictions.get(issueType);
						if (fieldRestrictionMap!=null) {
							//remove mass operation fields with restriction in any project/issueType combination 
							presentFieldsSet.removeAll(fieldRestrictionMap.keySet());
						}
					}
				}
			}
		}
		
		//remove the read only system fields
		for (Iterator<Integer> iterator = presentFieldsSet.iterator(); iterator.hasNext();) {
			Integer fieldID = iterator.next();
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null && (fieldTypeRT.isReadOnly() || fieldTypeRT.isComputed(fieldID, null))) {
				//the read only custom fields are not available for mass operation
				iterator.remove();
				continue;
			}
		}
		//add the project and issueType even if they are not present one any form
		presentFieldsSet.add(SystemFields.INTEGER_PROJECT);
		presentFieldsSet.add(SystemFields.INTEGER_ISSUETYPE);
		//parent may be present or not but it can be set outside the form anyway (Choose parent) so we add it for mass opeartion
		presentFieldsSet.add(SystemFields.INTEGER_SUPERIORWORKITEM);
		return presentFieldsSet;
	}
	
	/**
	 * Gets the extra field which might not directly present on the form
	 * @param selectedWorkItemBeans
	 * @param projectIssueTypeContexts
	 * @param projectRefresh
	 * @param issueTypeRefresh
	 * @param personBean
	 * @return
	 */
	static Set<Integer> getExtraFieldsNotOnForms(List<TWorkItemBean> selectedWorkItemBeans,
			Map<Integer, Set<Integer>> projectIssueTypeContexts, TPersonBean personBean) {
		Set<Integer> extraFieldsSet = new HashSet<Integer>();		
		if (!FieldDesignBL.isDeprecated(SystemFields.INTEGER_ACCESSLEVEL)) {
			boolean isOriginatorForEach = true;
			for (Iterator<TWorkItemBean> iterator = selectedWorkItemBeans.iterator(); iterator.hasNext();) {
				TWorkItemBean workItemBean = iterator.next();
				if (!personBean.getObjectID().equals(workItemBean.getOriginatorID())) {
					isOriginatorForEach = false;
					break;
				}
			}
			if (isOriginatorForEach) {
				extraFieldsSet.add(SystemFields.INTEGER_ACCESSLEVEL);
			}
		}
		//add archive level if the user is administrator in all projects
		boolean projectAdminInAllProjects = personBean.isSys() || 
			MassOperationRightsBL.hasRightInAllContexts(projectIssueTypeContexts, 
				personBean.getObjectID(), AccessFlagIndexes.PROJECTADMIN);
		if (projectAdminInAllProjects) {
			extraFieldsSet.add(SystemFields.INTEGER_ARCHIVELEVEL);
		}
		//the screens may not contain comment field explicitly
		extraFieldsSet.add(SystemFields.INTEGER_COMMENT);
		return extraFieldsSet;
	}
	
	/**
	 * Get the custom list fields
	 * @param presentFieldsSet
	 * @param includeCompositeLists
	 * @return
	 */
	public static List<Integer> getCustomListFieldIDs(Set<Integer> presentFieldsSet, boolean includeMultiple, boolean includeCompositeLists) {
		//get the custom selects fields
		List<Integer> customListFieldIDs = new LinkedList<Integer>();
		for (Integer fieldID  : presentFieldsSet) {
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null) {
				if (fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION) {
					//simple select
					if (includeMultiple) {
						customListFieldIDs.add(fieldID);
					} else {
						CustomSelectBaseRT customSelectBaseRT = (CustomSelectBaseRT)fieldTypeRT;
						if (!customSelectBaseRT.isReallyMultiple()) {
							customListFieldIDs.add(fieldID);
						}
					}
					
				} else {
					if (includeCompositeLists && fieldTypeRT.isComposite()) {
						//composite select
						IFieldTypeRT firstChild = ((CustomCompositeBaseRT)fieldTypeRT).getCustomFieldType(Integer.valueOf(1));
						if (firstChild!=null && firstChild.getValueType()==ValueType.CUSTOMOPTION) {
							customListFieldIDs.add(fieldID);
						}
					}
				}
			}
		}
		return customListFieldIDs;
	}
	
	/**
	 * Get the fields changed after projectRefresh or issyeType refresh 
	 * @param presentFieldsSet
	 * @param customListFieldIDs
	 * @param projectRefresh
	 * @param issueTypeRefresh
	 * @return
	 */
	static Set<Integer> leaveContextDependentFields(Set<Integer> presentFieldsSet, List<Integer> customListFieldIDs,
			boolean projectRefresh, boolean issueTypeRefresh) {
		if (projectRefresh || issueTypeRefresh) {
			//by project refresh leave only the project dependent fields: custom lists and release
			boolean hasReleaseNoticed = presentFieldsSet.contains(SystemFields.INTEGER_RELEASENOTICED);
			boolean hasAffectsRelease = presentFieldsSet.contains(SystemFields.INTEGER_RELEASESCHEDULED);
			presentFieldsSet.retainAll(customListFieldIDs);
			if (hasReleaseNoticed) {
				presentFieldsSet.add(SystemFields.INTEGER_RELEASENOTICED);
			}
			if (hasAffectsRelease) {
				presentFieldsSet.add(SystemFields.INTEGER_RELEASESCHEDULED);
			}
		}
		return presentFieldsSet;
	}
	
	
	
	/**
	 * Gets the fields available for all selected workItems
	 * @param involvedProjectBeansList
	 * @param projectIssueTypeContexts
	 * @param personBean
	 * @param locale
	 * @param customSelectFieldIDToListID output parameter used later in MassOperationContext
	 * @param requiredFields output parameter used for getting the required fields
	 * @return
	 */
	static void configureContext(MassOperationContext massOperationContext,
			Integer submittedProjectID, Integer submittedIssueTypeID, Set<Integer> presentFieldsSet,
			List<Integer> customListFieldIDs, TPersonBean personBean, Locale locale) {
		Map<Integer, Set<Integer>> projectIssueTypeContexts = massOperationContext.getProjectIssueTypeContexts();
		//get the field configurations in each context
		Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsToIssueTypesToFieldConfigsMap = 
			FieldRuntimeBL.loadFieldConfigsInContextsAndTargetProjectAndIssueType(
					projectIssueTypeContexts, presentFieldsSet,
					locale, submittedProjectID, submittedIssueTypeID);
		//load the required fields set	
		Set<Integer> requiredFields = addHardcodedRequiredFields(presentFieldsSet);
		addFieldsRequiredInAnyContext(projectsToIssueTypesToFieldConfigsMap, requiredFields);
		massOperationContext.setRequiredFields(requiredFields);
		Map<Integer, Map<Integer, List<TFieldConfigBean>>> fieldIDToListIDToFieldConfigs = CustomListsConfigBL.getMostSpecificLists(customListFieldIDs, projectIssueTypeContexts);
		Map<Integer, Map<Integer, String>> fieldIDToListIDLabels = CustomListsConfigBL.getFieldIDsToListIDToLabelsMap(fieldIDToListIDToFieldConfigs, locale, true);
		massOperationContext.setFieldIDToListIDToLabels(fieldIDToListIDLabels);
	}
	
	
	
	/**
	 * Gets the involved issue types (independently of the project) 
	 * @param projectIssueTypeContexts
	 * @return
	 */
	static List<Integer> getInvolvedIssueTypesList(Map<Integer, Set<Integer>> projectIssueTypeContexts) {
		Set<Integer> involvedIssueTypesSet = new HashSet<Integer>();
		for (Integer projectID : projectIssueTypeContexts.keySet()) {
			Set<Integer> projectIssueTypes = projectIssueTypeContexts.get(projectID);
			involvedIssueTypesSet.addAll(projectIssueTypes);
		}
		return GeneralUtils.createIntegerListFromCollection(involvedIssueTypesSet);
	}
	
	/**
	 * Enforce an order based on logic and importance of fields 
	 * (not alphabetically because the project dependent fields should be below project)
	 * @param presentFieldsSet
	 * @param customListFieldIDs
	 * @return
	 */
	static List<Integer> enforceOrder(Set<Integer> presentFieldsSet, List<Integer> customListFieldIDs) {
		List<Integer> fieldIDs = new LinkedList<Integer>();
		//project and issue type will be the first but later
		fieldIDs.add(SystemFields.INTEGER_PROJECT);
		fieldIDs.add(SystemFields.INTEGER_RELEASENOTICED);
		fieldIDs.add(SystemFields.INTEGER_RELEASESCHEDULED);
		fieldIDs.add(SystemFields.INTEGER_ISSUETYPE);
		//the custom lists come after the item type: although it can be only project specific which would involve to be before issue type but 
		//it can also be issue type specific in the sense that it might appear only on an issue type specific screen independently of project
		fieldIDs.addAll(customListFieldIDs);
		fieldIDs.add(SystemFields.INTEGER_STATE);
		fieldIDs.add(SystemFields.INTEGER_MANAGER);
		fieldIDs.add(SystemFields.INTEGER_RESPONSIBLE);
		fieldIDs.add(SystemFields.INTEGER_PRIORITY);
		fieldIDs.add(SystemFields.INTEGER_SEVERITY);
		fieldIDs.add(SystemFields.INTEGER_SYNOPSIS);
		fieldIDs.add(SystemFields.INTEGER_BUILD);
		fieldIDs.add(SystemFields.INTEGER_STARTDATE);
		fieldIDs.add(SystemFields.INTEGER_ENDDATE);
		fieldIDs.add(SystemFields.INTEGER_TOP_DOWN_START_DATE);
		fieldIDs.add(SystemFields.INTEGER_TOP_DOWN_END_DATE);
		fieldIDs.add(SystemFields.INTEGER_SUPERIORWORKITEM);
		fieldIDs.add(SystemFields.INTEGER_ACCESSLEVEL);
		fieldIDs.add(SystemFields.INTEGER_ARCHIVELEVEL);
		fieldIDs.add(SystemFields.INTEGER_DESCRIPTION);
		fieldIDs.add(SystemFields.INTEGER_COMMENT);
		List<Integer> presentFieldLists = new LinkedList<Integer>();
		for (Integer fieldID : fieldIDs) {
			if (presentFieldsSet.contains(fieldID)) {
				presentFieldLists.add(fieldID);
				presentFieldsSet.remove(fieldID);
			}
		}
		//add those not explicitly specified for order (mainly the custom fields)
		presentFieldLists.addAll(presentFieldsSet);		
		return presentFieldLists;
	}
	
	/**
	 * Gets the fields available for all selected workItems
	 * @param projectIssueTypeContexts
	 * @param personID
	 * @return
	 */
	public static List<Integer> getAvailablePseudoFields( 
			Map<Integer, Set<Integer>> projectIssueTypeContexts, Integer personID) {
		List<Integer> presentFields = new LinkedList<Integer>();
		//add consulted/informed pseudo fields if the user has "view consulted/informed" right in each involved project/issue type
		/*boolean viewConsultedInformedInAllProjects = MassOperationRightsBL.hasRightInAllContexts(projectIssueTypeContexts, 
				personID, AccessFlagMigrationIndexes.VIEWCONSULTANTSINFORMANTANDOTHERWATCHERS);*/
		boolean modifyWatchersRestricted = AccessBeans.getFieldRestrictedInAnyContext(
				personID, projectIssueTypeContexts, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS, true);
		if (!modifyWatchersRestricted) {
			//it is not necessary to have also "modify consulted/informed" right in each involved  
			//project/issue type because the "me"-operations should be mainly available also by 
			//"view consulted/informed" right: it should have at least right for "add/remove me as informed" 
			//(also "add me as informed" is enabled because the selectedWorkItemBeans are readable)
			//and "remove me as consulted". But it should have the "add me as consulted" right 
			//only if he has "modify any" right in each involved project/issue: but for this 
			//the consulted will not be ignored but eventually the content of the consultant field by add will be empty
			presentFields.add(MassOperationBL.CONSULTANT_FIELDID);
			presentFields.add(MassOperationBL.INFORMANT_FIELDID);
		}	
		return presentFields;
	}

	/**
	 * Add the fields which are required at field level ('hardcoded' required field)
	 * @param presentFields
	 * @param requiredFields
	 */
	private static Set<Integer> addHardcodedRequiredFields(Set<Integer> presentFields) {
		Set<Integer> requiredFields = new HashSet<Integer>();
		List<TFieldBean> presentFieldBeans = FieldBL.loadByFieldIDs(presentFields.toArray());
		if (presentFieldBeans!=null) {
			for (TFieldBean fieldBean : presentFieldBeans) {
				if (fieldBean.isRequiredString()) {
					requiredFields.add(fieldBean.getObjectID());
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("The fieldID " + fieldBean.getObjectID() + " is hardcoded as required");
					}
				}
			}
		}
		return requiredFields;
	}
	
	/**
	 * Add the fields which are required at field config level ('user defined' required field)
	 * @param projectsToIssueTypesToFieldConfigsMap
	 * @param requiredFields
	 */
	private static void addFieldsRequiredInAnyContext(
			Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsToIssueTypesToFieldConfigsMap, 
			Set<Integer> requiredFields) {
		for (Map<Integer, Map<Integer, TFieldConfigBean>> issueTypeToFieldConfigsMap : projectsToIssueTypesToFieldConfigsMap.values()) {
			for (Map<Integer, TFieldConfigBean> fieldConfigsMap : issueTypeToFieldConfigsMap.values()) {
				for (TFieldConfigBean fieldConfigBean : fieldConfigsMap.values()) {
					Integer fieldID = fieldConfigBean.getField();
					if (fieldConfigBean.isRequiredString() && !requiredFields.contains(fieldID)) {
						requiredFields.add(fieldID);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("The fieldID " + fieldID + " is required in at least one field config context");
						}
					}
				} 
			}
		}
	}
	
	/**
	 * Get the screens for action map pro project and issueType
	 * @param workItemBeanList
	 * @param loaderResourceBundleMessages
	 * @return
	 */
	private static Map<Integer, Map<Integer, Map<Integer, Integer>>> getScreensForActionsInContexts(
			Map<Integer, Set<Integer>> projectsToIssueTypes) {
		Map<Integer, Map<Integer, Map<Integer, Integer>>> projectsIssueTypesScreensForActionsMap = 
			new HashMap<Integer, Map<Integer, Map<Integer, Integer>>>();
		for (Integer projectID : projectsToIssueTypes.keySet()) {
			Set<Integer> issueTypeSet = projectsToIssueTypes.get(projectID);
			Map<Integer, Map<Integer, Integer>> issueTypesScreensForActionsMap = new HashMap<Integer, Map<Integer, Integer>>();
			projectsIssueTypesScreensForActionsMap.put(projectID, issueTypesScreensForActionsMap);
			for (Integer issueTypeID : issueTypeSet) {
				Map<Integer, Integer> screenForActionMap = ScreenConfigBL.getScreenForAction(projectID, issueTypeID);
				issueTypesScreensForActionsMap.put(issueTypeID, screenForActionMap);
			}
		}
		return projectsIssueTypesScreensForActionsMap;
	}
	
	/**
	 * Gets the fieldIDs contained in each screen
	 * @param screensForActionProProjectAndIssueType
	 * @return
	 */
	private static Map<Integer, List<Integer>> getFieldsListForScreens(
			Map<Integer, Map<Integer, Map<Integer, Integer>>> screensForActionProProjectAndIssueType) {
		Map<Integer, List<Integer>> fieldListForScreensMap = new HashMap<Integer, List<Integer>>();
		for (Integer projectID: screensForActionProProjectAndIssueType.keySet()) {
			Map<Integer, Map<Integer, Integer>> screensForActionProIssueType = screensForActionProProjectAndIssueType.get(projectID);
			if (screensForActionProIssueType!=null) {
				for (Integer issueTypeID : screensForActionProIssueType.keySet()) {
					Map<Integer, Integer> screensForAction = screensForActionProIssueType.get(issueTypeID);
					if (screensForAction!=null) {
						for (Integer actionID : screensForAction.keySet()) {
							Integer screenID = screensForAction.get(actionID);
							if (!fieldListForScreensMap.containsKey(screenID)) {
								List<TFieldBean> presentFieldsBeans = FieldBL.loadAllFields(screenID);
								fieldListForScreensMap.put(screenID, GeneralUtils.createIntegerListFromBeanList(presentFieldsBeans));
							}
						}
					}
				}
			}
		}
		return fieldListForScreensMap;
	}
	
	/**
	 * Gets the fields which are present in each context (project, issueType) in any of the actions screens
	 * @param screensForActionProProjectAndIssueType
	 * @param fieldsListForScreens
	 * @return
	 */
	private static Set<Integer> getFieldsPresentInEachConext(
			Map<Integer, Map<Integer, Map<Integer, Integer>>> screensForActionProProjectAndIssueType, 
			Map<Integer, List<Integer>> fieldsListForScreens) {
		Set<Integer> commonFieldIDs = null;
		for (Integer projectID : screensForActionProProjectAndIssueType.keySet()) {
			Map<Integer, Map<Integer, Integer>> screensForActionProIssueType = screensForActionProProjectAndIssueType.get(projectID);
			if (screensForActionProIssueType!=null) {
				for (Integer issueTypeID : screensForActionProIssueType.keySet()) {
					Map<Integer, Integer> screensForAction = screensForActionProIssueType.get(issueTypeID);
					Set<Integer> contextFieldIDs = new HashSet<Integer>();
					if (screensForAction!=null) {
						for (Integer actionID : screensForAction.keySet()) {
							Integer screenID = screensForAction.get(actionID);
							List<Integer> fields = fieldsListForScreens.get(screenID);
							//make the union of fields from each action screen from the same context
							if (fields!=null) {
								contextFieldIDs.addAll(fields);
							}
						}
					}
					if (commonFieldIDs==null) {
						//first context fields
						commonFieldIDs = contextFieldIDs;
					} else {
						//makes the intersection of the fields (retain only those which are present in each context in any of the action screens)
						commonFieldIDs.retainAll(contextFieldIDs);
					}
				}
			}
		}
		if (commonFieldIDs==null) {
			commonFieldIDs = new HashSet<Integer>();
		}
		return commonFieldIDs;
	}
}
