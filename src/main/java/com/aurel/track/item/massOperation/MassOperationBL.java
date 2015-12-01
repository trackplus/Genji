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
import java.util.SortedSet;
import java.util.TreeSet;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.treeConfig.field.CustomListsConfigBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.fieldType.bulkSetters.BulkRelations;
import com.aurel.track.fieldType.bulkSetters.BulkTranformContext;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.WatcherSelectBulkSetter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.item.lock.ItemLockBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;

/**
 * Main logic for mass operation
 * @author Tamas
 *
 */
public class MassOperationBL {
	static public Integer CONSULTANT_FIELDID = Integer.valueOf(-1);
	static public Integer INFORMANT_FIELDID = Integer.valueOf(-2);
	
	private static String SELECTED_FIELD_BASE_NAME = "selectedFieldMap";
	private static String SELECTED_FIELD_BASE_ITEM_ID = "selectedFieldItemId";
	private static String SETTER_RELATION_MAP_NAME = "setterRelationMap";
	private static String SETTER_RELATION_BASE_ITEM_ID = "setterRelationItemId";
	public static String VALUE_BASE_NAME = "displayValueMap";
	public static String VALUE_BASE_ITEMID = "valueItemID";
	
	public static String BULK_OPERATION_RELATION_PREFIX = "itemov.massOperation.relation.";


	public static MassOperationContext initMassOperationContextByReportBeans(List<ReportBean> reportBeanList, Integer projectID, Integer issueTypeID) {
		List<TWorkItemBean> workItemBeans = new LinkedList<TWorkItemBean>();
		if (reportBeanList!=null) {
			for (ReportBean reportBean : reportBeanList) {
				TWorkItemBean workItemBean = reportBean.getWorkItemBean();
				workItemBeans.add(workItemBean);
			}
		}
		return initMassOperationContext(workItemBeans,projectID,issueTypeID);
	}
	
	/**
	 * Initialize the massOperationContext: set involved projects, project-issueType contexts, projectsMap
	 * @param selectedWorkItemBeans
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	public static MassOperationContext initMassOperationContext(
			List<TWorkItemBean> selectedWorkItemBeans, Integer projectID, Integer issueTypeID) {
		MassOperationContext massOperationContext = new MassOperationContext();
		Integer[] involvedProjectIDs;
		//the project field was either checked or unchecked or the selected project was changed
		if (projectID!=null) {
			//the project field was checked or the selected project was changed in checked mode
			involvedProjectIDs = new Integer[] {projectID};
		} else {
			//first rendering or the project was not touched or the project field was unchecked
			//get all the projects the selected issues are member of
			involvedProjectIDs = GeneralUtils.createIntegerArrFromCollection(getInvolvedProjectIDs(selectedWorkItemBeans));
		}
		massOperationContext.setInvolvedProjectsIDs(involvedProjectIDs);
		Map<Integer, Set<Integer>> projectIssueTypeContexts = null;
		//the issueType field was either checked or unchecked or the selected issueType was changed
		if (issueTypeID!=null) {
			//the issueType field was checked or the selected issueType was changed in checked mode
			projectIssueTypeContexts = getProjectIssueTypeContexts(involvedProjectIDs, issueTypeID);
		} else {
			//first rendering or the issueType was not touched or the issueType field was unchecked
			//get all the projects the selected issues are member of
			projectIssueTypeContexts = getProjectIssueTypeContexts(selectedWorkItemBeans, projectID);
		}
		massOperationContext.setProjectIssueTypeContexts(projectIssueTypeContexts);
		return massOperationContext;
	}
	
	/**
	 * Gets all combinations for project issue type in the selected workItemBeans
	 * @param selectedWorkItemBeans
	 * @param submittedProjectID
	 * @return
	 */
	private static Map<Integer, Set<Integer>> getProjectIssueTypeContexts(
			List<TWorkItemBean> selectedWorkItemBeans, Integer submittedProjectID) {
		Map<Integer, Set<Integer>> projectsIssueTypesMap = new HashMap<Integer, Set<Integer>>();
		for (TWorkItemBean workItemBean : selectedWorkItemBeans) {
			Integer projectID = null;
			if (submittedProjectID==null) {
				projectID = workItemBean.getProjectID();
			} else {
				projectID = submittedProjectID;
			}
			Integer issueTypeID = workItemBean.getListTypeID();
			Set<Integer> issueTypesSet = projectsIssueTypesMap.get(projectID);
			if (issueTypesSet == null) {
				issueTypesSet = new HashSet<Integer>();
				projectsIssueTypesMap.put(projectID, issueTypesSet);
			}
			issueTypesSet.add(issueTypeID);
		}
		return projectsIssueTypesMap;
	}

	/**
	 * Gets all combinations for project issue type in the involved project
	 * @param involvedProjectsIDs
	 * @param issueTypeID
	 * @return
	 */
	private static Map<Integer, Set<Integer>> getProjectIssueTypeContexts(Integer[] involvedProjectsIDs, Integer issueTypeID) {
		Map<Integer, Set<Integer>> projectsIssueTypesMap = new HashMap<Integer, Set<Integer>>();
		for (Integer projectID : involvedProjectsIDs) {
			Set<Integer> issueTypesSet = new HashSet<Integer>();
			projectsIssueTypesMap.put(projectID, issueTypesSet);
			issueTypesSet.add(issueTypeID);
		}
		return projectsIssueTypesMap;
	}
	
	/**
	 * Gets the MassOperationExpressions to be sent to the client as JSON
	 * @param servletResponse
     * @param issueIDsArr
	 * @param personBean
	 * @param locale
	 * @param projectRefresh
	 * @param projectID not null only by project or project change
	 * @param issueTypeRefresh
	 * @param issueTypeID not null only by issueType change
	 * @param selectedFieldMap not null only by project or issueType change
	 * @param setterRelationMap not null only by project or issueType change
	 * @param displayValueMap not null only by project or issueType change
     * @param bulkCopy
	 * @return
	 */
	static String getMassOperationExpressions(int[] issueIDsArr, TPersonBean personBean, Locale locale,
			boolean projectRefresh, Integer projectID, boolean issueTypeRefresh, Integer issueTypeID,
			Map<String, Boolean> selectedFieldMap, Map<String, Integer> setterRelationMap,
			Map<String, String> displayValueMap, boolean bulkCopy) {
		
		List<TWorkItemBean> selectedWorkItemBeans = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(issueIDsArr, personBean.getObjectID(), true, true, false);
		MassOperationContext massOperationContext = initMassOperationContext(selectedWorkItemBeans, projectID, issueTypeID);
		Map<Integer, Set<Integer>> projectIssueTypeContexts = massOperationContext.getProjectIssueTypeContexts();
		/*
		 * get the fields which are available on forms for all selected workItems 
		 */
		Set<Integer> presentFieldsOnFormsSet = MassOperationFieldsBL.getPresentFieldsOnFormsSet(massOperationContext, personBean);
		/*
		 * get the custom list fields from the available fields
		 */
		List<Integer> customListFieldIDs = MassOperationFieldsBL.getCustomListFieldIDs(presentFieldsOnFormsSet, true, true);
		if (!projectRefresh && !issueTypeRefresh) {
			//first rendering (not project or issueType refresh): add extra fields
			presentFieldsOnFormsSet.addAll(MassOperationFieldsBL.getExtraFieldsNotOnForms(
					selectedWorkItemBeans, projectIssueTypeContexts, personBean));
		} else {
			//leave only the context dependent fields
			presentFieldsOnFormsSet = MassOperationFieldsBL.leaveContextDependentFields(
					presentFieldsOnFormsSet, customListFieldIDs, projectRefresh, issueTypeRefresh);
		}
		MassOperationFieldsBL.configureContext(massOperationContext,
				projectID, issueTypeID, presentFieldsOnFormsSet, customListFieldIDs, personBean, locale);
		List<Integer> presentFieldIDs = MassOperationFieldsBL.enforceOrder(presentFieldsOnFormsSet, customListFieldIDs);
		Integer person = personBean.getObjectID();
		massOperationContext.setPersonID(person);
		if (selectedWorkItemBeans!=null && !selectedWorkItemBeans.isEmpty()) {
			massOperationContext.setFirstSelectedWorkItemBean(selectedWorkItemBeans.get(0));
		}
		
		Set<Integer> presentFieldsSet = GeneralUtils.createIntegerSetFromIntegerList(presentFieldIDs);
		Map<Integer, String> fieldLabelsMap = getFieldLabelsForContext(
				projectIssueTypeContexts, presentFieldsSet, person, locale);
		/**
		 * Prepare the MassOperationExpressions list
		 */
		List<MassOperationExpressionTO> massOperationExpressions = new LinkedList<MassOperationExpressionTO>();
		for (Integer fieldID : presentFieldIDs) {
			Boolean fieldSelected = null;
			if (projectRefresh || projectID!=null) {
				//select the project dependent fields if a project is selected (now or already)
				fieldSelected = (projectID!=null);
			} else {
				if (selectedFieldMap!=null) {
					//issueTypeRefresh: leave the actual selection
					fieldSelected = selectedFieldMap.get(getKeyPrefix(fieldID));
				}
			}
			Integer setterRelation = null;
			if (setterRelationMap!=null) {
				setterRelation = setterRelationMap.get(getKeyPrefix(fieldID));
			}
			MassOperationExpressionTO massOperationExpression =	createOrRefreshMassOperationExpression(massOperationContext, fieldID, 
						fieldLabelsMap.get(fieldID), fieldSelected, setterRelation, displayValueMap, personBean, locale);
			if (massOperationExpression!=null) {
				massOperationExpressions.add(massOperationExpression);
			}
		}
		//add pseudo field massOperationExpressions (for the moment only watchers)
		List<Integer> watcherFields =  MassOperationFieldsBL.getAvailablePseudoFields(projectIssueTypeContexts, person);
		if (watcherFields.contains(CONSULTANT_FIELDID) || watcherFields.contains(INFORMANT_FIELDID)) {
			List<Integer> selectedWorkItemIDsList = GeneralUtils.createIntegerListFromBeanList(selectedWorkItemBeans);
			if (watcherFields.contains(CONSULTANT_FIELDID)) {
				massOperationExpressions.add(createWatcherMassOperationExpression(
					massOperationContext, projectIssueTypeContexts, CONSULTANT_FIELDID, 
					selectedWorkItemIDsList, personBean, locale));
			}
			if (watcherFields.contains(INFORMANT_FIELDID)) {
				massOperationExpressions.add(createWatcherMassOperationExpression(
					massOperationContext, projectIssueTypeContexts, INFORMANT_FIELDID, 
					selectedWorkItemIDsList, personBean, locale));
			}
		}
		return MassOperationJSON.getBulkExpressionListJSON(massOperationExpressions, 
				projectRefresh || issueTypeRefresh, selectedWorkItemBeans.size());
	}	

	public static Integer[] getInvolvedProjectIDs(int[] issueIDsArr,Integer personID) {
		List<TWorkItemBean> selectedWorkItemBeans = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(issueIDsArr, personID, true, true, false);
		return GeneralUtils.createIntegerArrFromCollection(getInvolvedProjectIDs(selectedWorkItemBeans));
	}
	static List<Integer> getInvolvedProjectIDs(List<TWorkItemBean> workItemBeanList) {
		Set<Integer> projectIDsSet = new HashSet<Integer>();
		for (Iterator<TWorkItemBean> iterator = workItemBeanList.iterator(); iterator.hasNext();) {
			TWorkItemBean workItemBean = iterator.next();
			projectIDsSet.add(workItemBean.getProjectID());
		}
		return GeneralUtils.createListFromCollection(projectIDsSet);
	}
	
	static Map<Integer, TProjectBean> getInvolvedProjectsMap(Integer[] involvedProjectsIDs) {
		return GeneralUtils.createMapFromList(ProjectBL.loadByProjectIDs(
			GeneralUtils.createListFromIntArr(involvedProjectsIDs)));
	}
		
	/**
	 * Gets the best matching fieldLabels from contexts the selected issues are from
	 * @param projectIssueTypeContexts
	 * @param presentFieldIDsSet
	 * @param personID
	 * @param locale
	 * @return
	 */
	static Map<Integer, String> getFieldLabelsForContext(Map<Integer, Set<Integer>> projectIssueTypeContexts, 
			Set<Integer> presentFieldIDsSet, Integer personID, Locale locale) {
		Map<Integer, String> fieldLabelForField = new HashMap<Integer, String>();
		Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap =
			FieldRuntimeBL.loadFieldConfigsInContextsAndTargetProjectAndIssueType(
				projectIssueTypeContexts, presentFieldIDsSet, locale, null, null);
		Map<Integer, Set<String>> fieldLabelSetForField = new HashMap<Integer, Set<String>>();
		Set<Integer> fieldConfigIDSet = new HashSet<Integer>();
		for (Integer projectID : projectsIssueTypesFieldConfigsMap.keySet()) {
			Map<Integer, Map<Integer, TFieldConfigBean>> issueTypeToFieldConfigs = projectsIssueTypesFieldConfigsMap.get(projectID);
			if (issueTypeToFieldConfigs!=null) {
				for (Integer issueTypeID : issueTypeToFieldConfigs.keySet()) {
					Map<Integer, TFieldConfigBean> fieldConfigsForField = issueTypeToFieldConfigs.get(issueTypeID);
					if (fieldConfigsForField!=null) {
						for (Integer fieldID : fieldConfigsForField.keySet()) {
							TFieldConfigBean fieldConfigBean = fieldConfigsForField.get(fieldID);
							if (fieldConfigBean!=null && !fieldConfigIDSet.contains(fieldConfigBean.getObjectID())) {
								//if the same fieldConfigBean was already used for a previous 
								//project/issue type context no reason to make string processing
								Set<String> fieldLabels = fieldLabelSetForField.get(fieldID);
								if (fieldLabels==null) {
									fieldLabels = new HashSet<String>();
									fieldLabelSetForField.put(fieldID, fieldLabels);
								}
								String fieldLabel = fieldConfigBean.getLabel();
								if (fieldLabel!=null) {
									fieldLabels.add(fieldLabel.trim());
								}
								fieldConfigIDSet.add(fieldConfigBean.getObjectID());
							}
						}
					}
				}
			}
		}
		//add the alternative labels in different contexts
		for (Integer fieldID : fieldLabelSetForField.keySet()) {
			Set<String> labelSet = fieldLabelSetForField.get(fieldID);
			StringBuffer labelBuffer = new StringBuffer();
			for (Iterator<String> itrLabels = labelSet.iterator(); itrLabels.hasNext();) {
				labelBuffer.append(itrLabels.next());
				if (itrLabels.hasNext()) {
					labelBuffer.append("/");
				}
			}
			fieldLabelForField.put(fieldID, labelBuffer.toString());
		}
		return fieldLabelForField;
	}
	
	
	/**
	 * Get the name of the control (used by submit)
	 * @param mapBaseName
	 * @param fieldID
	 * @return
	 */
	static String getControlName(String mapBaseName, Integer fieldID) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(mapBaseName).append(".").append(getKeyPrefix(fieldID));
		return stringBuilder.toString();
	}
	
	/**
	 * Get the ext js itemnId of the control (. not allowed)
	 * @param mapBaseName
	 * @param fieldID
	 * @return
	 */
	public static String getControlItemId(String mapBaseName, Integer fieldID) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(mapBaseName).append(getKeyPrefix(fieldID));
		return stringBuilder.toString();
	}
	
	/**
	 * Prefixes for creating the keys in the maps because the maps with keys containing
	 * negative numbers are not intercepted correctly by the struts  
	 */
	private static String FIELD = "field";
	private static String PSEUDOFIELD = "pseudoField";
	
	/**
	 * The prefix for the map keys
	 * Values for keys containing negative numbers (hyphens) do not populate the map correctly
	 * (struts2 bug?), so the negative numbers will be transformed to positive numbers 
	 * @return
	 */
	public static String getKeyPrefix(Integer fieldID) {
		StringBuilder stringBuilder = new StringBuilder();
		if (fieldID.intValue()>0) {
			stringBuilder.append(FIELD);
			stringBuilder.append(fieldID);
		} else {
			stringBuilder.append(PSEUDOFIELD);
			stringBuilder.append(Integer.valueOf(-fieldID.intValue()));
		}
		return stringBuilder.toString();
	}
	
	/**
	 * The prefix for the map keys
	 * Values for keys containing negative numbers (hyphens) do not populate the map correctly
	 * (struts2 bug?), so the negative numbers will be transformed to positive numbers 
	 * @return
	 */
	public static Integer getKeyInteger(String keyStr) {
		Integer intValue = null;
		if (keyStr!=null) {
			if (keyStr.startsWith(FIELD)) {
				String strInt = keyStr.substring(FIELD.length());
				intValue = Integer.valueOf(strInt);
			} else {
				if (keyStr.startsWith(PSEUDOFIELD)) {
					String strInt = keyStr.substring(PSEUDOFIELD.length());
					intValue = Integer.valueOf("-" + strInt);
				}
			}
		}
		return intValue;
	}
	
	/**
	 * Creates or refreshes a field specific MassOperationExpression 
	 * Possible options and consequently bulk value might be changed after an new set of 
	 * selected issues or even after a project refresh 
	 * @param massOperationContext
	 * @param fieldID
	 * @param fieldLabel
	 * @param fieldSelected
	 * @param setterRelation
	 * @param displayValueMap
	 * @param personBean
	 * @param locale
	 * @return
	 */
	private static MassOperationExpressionTO createOrRefreshMassOperationExpression(
			MassOperationContext massOperationContext, Integer fieldID, String fieldLabel, 
			Boolean fieldSelected, Integer setterRelation, Map<String, String> displayValueMap,
			TPersonBean personBean, Locale locale) {
		MassOperationExpressionTO massOperationExpression = new MassOperationExpressionTO(fieldID);
		boolean fieldIsRequired = massOperationContext.getRequiredFields().contains(fieldID);
		massOperationExpression.setFieldName(getControlName(SELECTED_FIELD_BASE_NAME, fieldID));
		massOperationExpression.setFieldItemId(getControlItemId(SELECTED_FIELD_BASE_ITEM_ID, fieldID));
		massOperationExpression.setRelationName(getControlName(SETTER_RELATION_MAP_NAME, fieldID));
		massOperationExpression.setRelationItemId(getControlItemId(SETTER_RELATION_BASE_ITEM_ID, fieldID));
		massOperationExpression.setValueName(getControlName(VALUE_BASE_NAME, fieldID));
		massOperationExpression.setValueItemId(getControlItemId(VALUE_BASE_ITEMID, fieldID));
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		if (fieldTypeRT!=null) {
			IBulkSetter bulkSetterDT = fieldTypeRT.getBulkSetterDT(fieldID);
			if (bulkSetterDT!=null) {
				//initialize a new one: first time in the session or after a new selection of workItems 
				//when the field appears as new on the context specific screens
				//massOperationExpression.setFieldID(fieldID);			
				massOperationExpression.setFieldDisabled(fieldSelected==null || !fieldSelected.booleanValue());
				//independently whether is new or existing the possible options and consequently bulk value 
				//might be changed after an new set of selected issues or even after a project refresh.
				//even more between two mass operations the field configurations might be changed which might affect the fieldLabel,
				//fieldIsRequired and consequently also the possibleRelations. So this field should be also actualized
				massOperationExpression.setFieldLabel(fieldLabel);
				massOperationExpression.setValueRequired(fieldIsRequired);
				List<Integer> possibleRelations = bulkSetterDT.getPossibleRelations(fieldIsRequired);
				if (possibleRelations!=null) {
					List<IntegerStringBean> setterRelations = LocalizeUtil.getLocalizedList(
							BULK_OPERATION_RELATION_PREFIX, possibleRelations, locale);
					massOperationExpression.setSetterRelations(setterRelations);
					if (possibleRelations!=null && setterRelations!=null && !setterRelations.isEmpty()) {
						//sets the first relation if no one is selected (first time in the session) ||
						//verify whether the previous selection is still valid (after possible field configuration)
						massOperationExpression.setRelationID(setterRelation);
						if (massOperationExpression.getRelationID()==null || 
								(massOperationExpression.getRelationID()!=null && 
										!possibleRelations.contains(massOperationExpression.getRelationID()))) {
							massOperationExpression.setRelationID(setterRelations.get(0).getValue());
						}
					}
					//set the relation because the bulkValueJSP depends on the matcher   
					bulkSetterDT.setRelation(massOperationExpression.getRelationID().intValue());
				}
				if (displayValueMap!=null) {
					massOperationExpression.setValue(bulkSetterDT.fromDisplayString(displayValueMap, locale));
				}
				fieldTypeRT.loadBulkOperationDataSource(massOperationContext, massOperationExpression, null, personBean, locale);
				massOperationExpression.setBulkValueTemplate(bulkSetterDT.getSetterValueControlClass());
				massOperationExpression.setJsonConfig(bulkSetterDT.getSetterValueJsonConfig(VALUE_BASE_NAME,
						massOperationExpression.getValue(), massOperationExpression.getPossibleValues(),
						massOperationExpression.getValueLabelMap(), massOperationExpression.isFieldDisabled(), personBean, locale));
			}
		}
		return massOperationExpression;
	}
	
	
	/**
	 * Prepares a watcher specific MassOperationExpression
	 * @param massOperationContext
     * @param projectIssueTypeContexts
	 * @param fieldID
     * @param selectedWorkItemIDsList
	 * @param personBean
	 * @param locale
	 * @return
	 */
	static MassOperationExpressionTO createWatcherMassOperationExpression(MassOperationContext massOperationContext,
			Map<Integer, Set<Integer>> projectIssueTypeContexts, Integer fieldID, 
			List<Integer> selectedWorkItemIDsList,
			TPersonBean personBean, Locale locale) {
		MassOperationExpressionTO massOperationExpression = new MassOperationExpressionTO(fieldID);
		String fieldLabel = "";
		massOperationExpression.setFieldName(getControlName(SELECTED_FIELD_BASE_NAME, fieldID));
		massOperationExpression.setFieldItemId(getControlItemId(SELECTED_FIELD_BASE_ITEM_ID, fieldID));
		massOperationExpression.setRelationName(getControlName(SETTER_RELATION_MAP_NAME, fieldID));
		massOperationExpression.setRelationItemId(getControlItemId(SETTER_RELATION_BASE_ITEM_ID, fieldID));
		massOperationExpression.setValueName(getControlName(VALUE_BASE_NAME, fieldID));
		massOperationExpression.setValueItemId(getControlItemId(VALUE_BASE_ITEMID, fieldID));
		if (CONSULTANT_FIELDID.equals(fieldID)) {
			fieldLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(
					"item.tabs.watchers.lbl.header.consultants", locale);
		} else {
			fieldLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(
					"item.tabs.watchers.lbl.header.informants", locale);
		}
		massOperationExpression.setFieldLabel(fieldLabel);
		IBulkSetter bulkSetterDT = new WatcherSelectBulkSetter(fieldID);
		List<Integer> possibleRelations = bulkSetterDT.getPossibleRelations(false);
		if (massOperationExpression.getRelationID()==null) {
			//sets the first relationID if no one is selected
			massOperationExpression.setRelationID(Integer.valueOf(possibleRelations.get(0)));
		}
		bulkSetterDT.setRelation(massOperationExpression.getRelationID());
		List<IntegerStringBean> setterRelations = LocalizeUtil.getLocalizedList(
				BULK_OPERATION_RELATION_PREFIX, possibleRelations, locale);
		massOperationExpression.setSetterRelations(setterRelations);
		List<TPersonBean> possibleValues = MassOperationWatchersBL.getPossibleWatchers(fieldID, massOperationExpression.getRelationID(),
				projectIssueTypeContexts, personBean, massOperationContext.getInvolvedProjectsIDs(), selectedWorkItemIDsList);
		massOperationExpression.setPossibleValues(possibleValues);
		massOperationExpression.setBulkValueTemplate(bulkSetterDT.getSetterValueControlClass());
		massOperationExpression.setJsonConfig(bulkSetterDT.getSetterValueJsonConfig(VALUE_BASE_NAME,
				massOperationExpression.getValue(), massOperationExpression.getPossibleValues(),
				massOperationExpression.getValueLabelMap(), massOperationExpression.isFieldDisabled(), personBean, locale));
		return massOperationExpression;
	}
		
	/**
	 * Get the selected fields cleaned up from struts2 needed keyPrefixes
	 * @param selectedFieldMap
	 * @param setterRelationMap
	 * @return
	 */
	private static SortedSet<Integer> getSelectedFieldsCleaned(Map<String, Boolean> selectedFieldMap, Map<Integer, Integer> setterRelationMap) {
		SortedSet<Integer> selectedFieldsSet = new TreeSet<Integer>();
		if (selectedFieldMap!=null) {
			for (String key : selectedFieldMap.keySet()) {
				Boolean selected = selectedFieldMap.get(key);
				if (selected!=null && selected.booleanValue()) {
					selectedFieldsSet.add(getKeyInteger(key));
				}
			}
		}
		if (setterRelationMap!=null) {
			for (Integer fieldID : setterRelationMap.keySet()) {
				if (!selectedFieldsSet.contains(fieldID)) {
					selectedFieldsSet.add(fieldID);
				}
			}
		}
		return selectedFieldsSet;
	}
	
	/**
	 * Get the selected setter relations cleaned up from struts2 needed keyPrefixes
	 * @param setterRelationMap
	 * @return
	 */
	private static Map<Integer, Integer> getSetterRelationMapCleaned(Map<String, Integer> setterRelationMap) {
		Map<Integer, Integer> cleanedSetterRelationMap = new HashMap<Integer, Integer>();
		if (setterRelationMap!=null) {
			for (String key : setterRelationMap.keySet()) {
				Integer fieldID = getKeyInteger(key);
				Integer setterRelation = setterRelationMap.get(key);
				cleanedSetterRelationMap.put(fieldID, setterRelation);
			}
		}
		return cleanedSetterRelationMap;
	}
	
	/**
	 * Gets the field values as objects from the displayValueMap
	 * @param displayValueMap
	 * @param setterRelationMap
	 * @param selectedFieldsSet
	 * @param locale
	 * @return
	 */
	private static Map<Integer, Object> getFieldValues(Map<String, String> displayValueMap,
			Map<Integer, Integer> setterRelationMap, Set<Integer> selectedFieldsSet, Locale locale) {
		Map<Integer, Object> fieldValues = new HashMap<Integer, Object>();
		if (displayValueMap!=null) {
			Set<Integer> pseudoFields = getPseudoFields();
			for (Integer fieldID : selectedFieldsSet) {
				Object value = null;
				if (!pseudoFields.contains(fieldID)) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					if (fieldTypeRT!=null) {
						IBulkSetter bulkSetter = fieldTypeRT.getBulkSetterDT(fieldID);
						Integer relation = setterRelationMap.get(fieldID/*getKeyPrefix(fieldID)*/);
						bulkSetter.setRelation(relation);
						if (bulkSetter!=null) {
							value = bulkSetter.fromDisplayString(displayValueMap, locale);
						}
					}
				} else {
					value = displayValueMap.get(getKeyPrefix(fieldID));
				}
				if (value!=null) {
					fieldValues.put(fieldID, value);
				}
			}
		}
		return fieldValues;
	}

	public static void saveExtern(int[] issueIDsArr, Integer fieldID, Object value,
			Integer personID, Locale locale,boolean confirmSave) throws MassOperationException {
		SortedSet<Integer> selectedFieldsSet=new TreeSet<Integer>();
		selectedFieldsSet.add(fieldID);
		Map<Integer, Integer> setterRelationMap=new HashMap<Integer, Integer>();
		setterRelationMap.put(fieldID, BulkRelations.SET_TO);
		Map<Integer, Object> fieldValueMap=new HashMap<Integer, Object>();
		fieldValueMap.put(fieldID,value);
		save(issueIDsArr, selectedFieldsSet, setterRelationMap, fieldValueMap,
				false, confirmSave, false, false, false, false, personID, locale, false);
	}
	
	static void saveFromAction(int[] issueIDsArr, Map<String, Boolean> selectedFieldMap,
			Map<String, Integer> setterRelationMap, Map<String, String> displayValueMap, boolean bulkCopy, boolean confirmSave,
			boolean deepCopy, boolean copyAttachments, boolean copyChildren, boolean copyWatchers,
			Integer personID, Locale locale, boolean useProjectSpecificID) throws MassOperationException {
		Map<Integer, Integer> setterRelationMapCleaned = getSetterRelationMapCleaned(setterRelationMap);
		SortedSet<Integer> selectedFieldsSet = getSelectedFieldsCleaned(selectedFieldMap, setterRelationMapCleaned);
		Map<Integer, Object> fieldValueMap = getFieldValues(displayValueMap, setterRelationMapCleaned, selectedFieldsSet, locale);
		save(issueIDsArr, selectedFieldsSet, setterRelationMapCleaned, fieldValueMap,
				bulkCopy, confirmSave, deepCopy, copyAttachments, copyChildren, copyWatchers, personID, locale, useProjectSpecificID);
	}
	
	/**
	 * Save the modifications for the selected issues
	 * @return
	 */
	static void save(int[] issueIDsArr, SortedSet<Integer> selectedFieldsSet,
			Map<Integer, Integer> setterRelationMap, Map<Integer, Object> fieldValueMap, boolean bulkCopy, boolean confirmSave,
			boolean deepCopy, boolean copyAttachments, boolean copyChildren, boolean copyWatchers,
			Integer personID, Locale locale, boolean useProjectSpecificID) throws MassOperationException {
		List<TWorkItemBean> selectedWorkItemBeans = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(issueIDsArr, personID, true, true, false);
		Map<Integer, List<Integer>> selectedChildrenMap = new HashMap<Integer, List<Integer>>();
		List<ReportBean> reportBeanList = LoadItemIDListItems.getReportBeansByWorkItemIDs(issueIDsArr, false, personID, locale, false, false, false, false, false, false, false, false, false);
		ReportBeans reportBeans = new ReportBeans(reportBeanList, locale, null, true);
		List<Integer> selectedIssueList;
		Set<Integer> selectedChildren = null;
		if (bulkCopy) {
			if (copyChildren) {
				selectedChildren = new HashSet<Integer>();
			}
			selectedIssueList = getSelectedIssueListParentsFirst(reportBeans, copyChildren, selectedChildren);
		} else {
			selectedIssueList = getSelectedIssueListChildrenFirst(reportBeans, selectedChildrenMap);
		}
		//is the project selected?
		Integer selectedProjectID = null;
		if (selectedFieldsSet.contains(SystemFields.INTEGER_PROJECT)) {
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_PROJECT);
			IBulkSetter bulkSetter = fieldTypeRT.getBulkSetterDT(SystemFields.INTEGER_PROJECT);
			Integer relation = setterRelationMap.get(SystemFields.INTEGER_PROJECT);
			bulkSetter.setRelation(relation);
			selectedProjectID = (Integer)fieldValueMap.get(SystemFields.INTEGER_PROJECT);
		}
		//is the issueType selected?
		Integer selectedIssueTypeID = null;
		if (selectedFieldsSet.contains(SystemFields.INTEGER_ISSUETYPE)) {
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_ISSUETYPE);
			IBulkSetter bulkSetter = fieldTypeRT.getBulkSetterDT(SystemFields.INTEGER_ISSUETYPE);
			Integer relation = setterRelationMap.get(SystemFields.INTEGER_ISSUETYPE);
			bulkSetter.setRelation(relation);
			selectedIssueTypeID = (Integer)fieldValueMap.get(SystemFields.INTEGER_ISSUETYPE);
		}
		
		Integer extraDateField = null;
		if (selectedFieldsSet.contains(SystemFields.INTEGER_STARTDATE) ^ selectedFieldsSet.contains(SystemFields.INTEGER_ENDDATE)) {
			Integer fieldID = null;
			if (selectedFieldsSet.contains(SystemFields.INTEGER_STARTDATE)) {
				fieldID = SystemFields.INTEGER_STARTDATE;
			} else {
				fieldID = SystemFields.INTEGER_ENDDATE;
			}
			Map<Integer, Object> dateWithAdjustBothDates = null;
			try {
				dateWithAdjustBothDates = (Map<Integer, Object>)fieldValueMap.get(fieldID);
				if (dateWithAdjustBothDates!=null) {
					try {
						Boolean adjustBothDates = (Boolean)dateWithAdjustBothDates.get(Integer.valueOf(1));
						if (adjustBothDates!=null && adjustBothDates.booleanValue()) {
							if (selectedFieldsSet.contains(SystemFields.INTEGER_STARTDATE)) {
								extraDateField = SystemFields.INTEGER_ENDDATE;
							} else {
								extraDateField = SystemFields.INTEGER_STARTDATE;
							}
							selectedFieldsSet.add(extraDateField);
						}
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
			}
		}
		/**
		 * get all project/issueType combinations according to the selected issues
		 */
		MassOperationContext massOperationContext = initMassOperationContext(
				selectedWorkItemBeans, selectedProjectID, selectedIssueTypeID);
		Map<Integer, Set<Integer>> projectIssueTypeContexts = massOperationContext.getProjectIssueTypeContexts();
		
		//get the most specific TFieldConfigBean in each context for each field
		Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap = 
			FieldRuntimeBL.loadFieldConfigsInContextsAndTargetProjectAndIssueType(
				projectIssueTypeContexts, selectedFieldsSet, locale, selectedProjectID, selectedIssueTypeID);
		//get the fieldSettings for each fieldConfigBean
		Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap =
			FieldRuntimeBL.getFieldSettingsForFieldConfigs(projectsIssueTypesFieldConfigsMap);
		Map<Integer, WorkItemContext> workItemContextsMap = FieldsManagerRT.createImportContext(selectedWorkItemBeans, 
				getRealFields(selectedFieldsSet), 
			projectsIssueTypesFieldConfigsMap, projectsIssueTypesFieldSettingsMap, 
			selectedProjectID, selectedIssueTypeID, personID, locale);
		//get the (possible empty) prohibitedChangesMap map
		Map<Integer, Map<Integer, ErrorData>> prohibitedChangesMap = preprocessSave(
				workItemContextsMap, selectedWorkItemBeans, 
				projectIssueTypeContexts, projectsIssueTypesFieldConfigsMap, projectsIssueTypesFieldSettingsMap,
				selectedIssueList, selectedFieldsSet, extraDateField, setterRelationMap, fieldValueMap, 
				selectedProjectID, selectedIssueTypeID, bulkCopy, personID, locale);
		//if errors found by preprocess show them to the user
		List<String> prohibitedChangesList = new LinkedList<String>();
		Map<Integer, String> issueIDsMap = null;
		if (useProjectSpecificID) {
			issueIDsMap = ItemBL.getProjectSpecificIssueIDsMap(selectedWorkItemBeans);
		}
		if (prohibitedChangesMap!=null && !prohibitedChangesMap.isEmpty() && confirmSave==false) {
			Map<Integer, Set<ErrorData>> errorsMap = 
				getProhibitedChangesProField(prohibitedChangesMap, issueIDsMap);
			for (Integer fieldID : errorsMap.keySet()) {
				Set<ErrorData> errorDataSet = errorsMap.get(fieldID);
				if (errorDataSet!=null && !errorDataSet.isEmpty()) {
					for (ErrorData errorData : errorDataSet) {
						String errorMessage = ErrorHandlerJSONAdapter.createMessage(errorData, locale);
						prohibitedChangesList.add(errorMessage);
					}
				}
			}
			if (selectedWorkItemBeans.size()>prohibitedChangesMap.size()) {
				int numberOfIssuesAllowed = selectedWorkItemBeans.size()-prohibitedChangesMap.size();
				StringBuilder stringBuilder = new StringBuilder();
				for (TWorkItemBean workItemBean : selectedWorkItemBeans) {
					Integer workItemID = workItemBean.getObjectID();
					if (!prohibitedChangesMap.containsKey(workItemBean.getObjectID())) {
						if (stringBuilder.length()!=0) {
							stringBuilder.append(", ");
						} else {
							stringBuilder.append(" ");
						}
						String itemID  = null;
						if (useProjectSpecificID) {
							itemID = issueIDsMap.get(workItemID);
						}
						if (itemID==null) {
							itemID = workItemID.toString();
						}
						stringBuilder.append(itemID);
					}
				}
				String allowedMessage = LocalizeUtil.getParametrizedString("itemov.massOperation.warning.numberOfAllowed",
						new Object[] {numberOfIssuesAllowed}, locale) + stringBuilder.toString();
				//add the confirmation message for the rest of issues (when at least one issue is allowed to change)
				
				throw new MassOperationException(JSONUtility.EDIT_ERROR_CODES.NEED_CONFIRMATION,
						prohibitedChangesList, allowedMessage);
			} else {
				throw new MassOperationException(prohibitedChangesList);
			}
		} else {
			if (confirmSave) {
				//change confirmed for the workItems without errors: remove workItems with prohibited changes
				for (Iterator<TWorkItemBean> iterator = selectedWorkItemBeans.iterator(); iterator.hasNext();) {
					TWorkItemBean workItemBean = iterator.next();
					if (prohibitedChangesMap.containsKey(workItemBean.getObjectID())) {
						iterator.remove();
					}
				}
			}
			/**
			 * Validate each workItemBean
			 */
			boolean hasValidationErrors = false;
			List<String> validationErrors = new LinkedList<String>();
			for (Integer workItemID : selectedIssueList) {
				WorkItemContext workItemContext = workItemContextsMap.get(workItemID);
				TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
				Integer parentID = workItemBean.getSuperiorworkitem();
				boolean parentIsSelected = parentID!=null && selectedIssueList.contains(parentID);
				List<ErrorData> validationErrorsList = FieldsManagerRT.validate(workItemBean,
						workItemContext, false, bulkCopy, selectedChildrenMap.get(workItemID), parentIsSelected, false);
				if (validationErrorsList!=null && !validationErrorsList.isEmpty()) {
					hasValidationErrors = true;
					String itemID  = null;
					if (useProjectSpecificID) {
						itemID = issueIDsMap.get(workItemID);
					}
					if (itemID==null) {
						itemID = workItemID.toString();
					}
					String validationMessage = LocalizeUtil.getParametrizedString("itemov.massOperation.err.validation", new Object[] {itemID}, locale) + " " + 
					getApplicationResourcesErrorList(validationErrorsList, locale);
					validationErrors.add(validationMessage);
				}
			}
			if (hasValidationErrors) {
				throw new MassOperationException(validationErrors);
			} else {
				//no prohibited/not validating problem found or some prohibited/not validating found but confirmChange set to change the rest
				List<String> saveErrors = new LinkedList<String>();
				Map<Integer, List<ErrorData>> errorsMap =
						saveDetails(workItemContextsMap, selectedFieldsSet, 
						selectedIssueList, selectedChildren, setterRelationMap, fieldValueMap, bulkCopy, deepCopy,
						copyAttachments, copyChildren, copyWatchers, personID, locale);
				//if errors found during one by one save: show them for the user
				if (errorsMap!=null && !errorsMap.isEmpty()) {
					Map<Integer, TFieldConfigBean> fieldConfigMap = FieldRuntimeBL.getLocalizedDefaultFieldConfigsMap(locale);
					for (Integer workItemID : errorsMap.keySet()) {
						List<ErrorData> errorList = errorsMap.get(workItemID);
						String errorMessage = null;
						String itemID  = null;
						if (useProjectSpecificID) {
							itemID = issueIDsMap.get(workItemID);
						}
						if (itemID==null) {
							itemID = workItemID.toString();
						}
						if (bulkCopy) {
							errorMessage = LocalizeUtil.getParametrizedString("itemov.massOperation.err.copyChange", new Object[] {itemID}, locale) + " " + 
								getApplicationResourcesErrorList(errorList, locale);
						} else {
							errorMessage = LocalizeUtil.getParametrizedString("itemov.massOperation.err.saveChange", new Object[] {itemID}, locale) + " " + 
									getApplicationResourcesErrorList(errorList, locale);
						}
						saveErrors.add(errorMessage);
					}
					throw new MassOperationException(saveErrors);
				}
			}
		}
	}
	
	private static String getApplicationResourcesErrorList(List<ErrorData> errorList, Locale locale){	   
		StringBuffer stringBuffer = new StringBuffer();
		for (ErrorData errorData : errorList) {
			stringBuffer.append(ErrorHandlerJSONAdapter.createMessage(errorData, locale)).append('\n');
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Verifies those conditions which by editing a single issue are already limited by rights, 
	 * a limited set of available options (e.g. states) etc.
     * @param massOperationContext
	 * @param workItemContextsMap
	 * @param selectedWorkItemBeans
	 * @param projectIssueTypeContexts
	 * @param projectsIssueTypesFieldConfigsMap
	 * @param selectedIssueList
	 * @param selectedFieldsSet
     * @param extraDateField
     * @param setterRelationMap
     * @param fieldValueMap
	 * @param selectedProjectID
	 * @param selectedIssueTypeID
	 * @param bulkCopy
	 * @param personID
	 * @param locale
	 * @return
	 */
	private static Map<Integer, Map<Integer, ErrorData>> preprocessSave(
			Map<Integer, WorkItemContext> workItemContextsMap,
			List<TWorkItemBean> selectedWorkItemBeans, Map<Integer, Set<Integer>> projectIssueTypeContexts, 
			Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap,
			Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap,
			List<Integer> selectedIssueList, Set<Integer> selectedFieldsSet, Integer extraDateField,
			Map<Integer, Integer> setterRelationMap,
			Map<Integer, Object> fieldValueMap,
			Integer selectedProjectID, Integer selectedIssueTypeID,
			boolean bulkCopy, Integer personID, Locale locale) {
		Map<Integer, Map<Integer, ErrorData>> prohibitedChangesMap = 
			new HashMap<Integer, Map<Integer, ErrorData>>();
		SelectContext selectContext = new SelectContext();
		selectContext.setPersonID(personID);
		selectContext.setCreate(false);
		selectContext.setLocale(locale);
		boolean isProjectMove = false;
		Integer projectID = null;
		if (selectedFieldsSet.contains(SystemFields.INTEGER_PROJECT)) {
			//set the projectID if selected
			projectID = selectedProjectID;
			isProjectMove = true;
		}
		boolean isIssueTypeMove = false;
		Integer issueTypeID = null;
		if (selectedFieldsSet.contains(SystemFields.INTEGER_ISSUETYPE)) {
			//set the issueTypeID if selected
			issueTypeID = selectedIssueTypeID;
			isIssueTypeMove = true;
		}
		/**
		 * prepare some explicit roles pro context 
		 */
		//create right is important by move (project/issueType) or bulk copy
		Map<Integer, Map<Integer, Boolean>> createRightInContexts = null;
		if (bulkCopy || isProjectMove || isIssueTypeMove) {
			createRightInContexts = MassOperationRightsBL.loadRightInContextsByTargetProjectAndIssueType(
					projectIssueTypeContexts, personID, AccessFlagIndexes.CREATETASK, projectID, issueTypeID);
		}
		BulkTranformContext bulkTranformContext = new BulkTranformContext();
		bulkTranformContext.setSelectedWorkItems(selectedWorkItemBeans);
		bulkTranformContext.setSelectedFieldsSet(selectedFieldsSet);
		bulkTranformContext.setValueCache(new HashMap<Integer, Object>());
		/**
		 * set the fieldToProjectToIssueTypeToListMap for custom list fields
		 */
		List<Integer> customListFieldIDs = MassOperationFieldsBL.getCustomListFieldIDs(selectedFieldsSet, true, true);
		bulkTranformContext.setFieldToProjectToIssueTypeToListMap(
				CustomListsConfigBL.getListsInContext(projectIssueTypeContexts, customListFieldIDs));
		Map<Integer, Integer> lockedItemsMap = ItemLockBL.getLockedIssues(selectedIssueList);
		//for each selected issue
		for (Integer workItemKey : selectedIssueList) {
			WorkItemContext workItemContext = workItemContextsMap.get(workItemKey);
			if (workItemContext!=null) {
				//gather the map of real changes
				//gather the list of errors caused by prohibited operations
				Map<Integer, ErrorData> workItemProhibitedChangesMap = new HashMap<Integer, ErrorData>(); 
				TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
				//set to the actual project only if not isProjectMove 
				if (!isProjectMove) { 
					projectID = workItemBean.getProjectID();
				}
				//set to the actual project only if not isIssueTypeMove
				if (!isIssueTypeMove) {
					issueTypeID = workItemBean.getListTypeID();
				}
				if (lockedItemsMap!=null && lockedItemsMap.keySet().contains(workItemKey)) {
					Integer lockedByPersonID = lockedItemsMap.get(workItemKey);
					if (lockedByPersonID!=null && !lockedByPersonID.equals(personID)) {
						//not allowed to edit this workItem because it is locked by another person
						workItemProhibitedChangesMap.put(SystemFields.INTEGER_ISSUENO, 
								new ErrorData("itemov.massOperation.err.locked"));
						//does not really make sense to continue with this workItem (for finding other prohibited changes)
						prohibitedChangesMap.put(workItemKey, workItemProhibitedChangesMap);
						continue;
					}
				}
				if ((bulkCopy || isProjectMove || isIssueTypeMove) && !MassOperationRightsBL.hasRightInContext(createRightInContexts, projectID, issueTypeID)) {
					//no create right for this workItem in the new project or the new issueType
					workItemProhibitedChangesMap.put(SystemFields.INTEGER_ISSUENO, 
							new ErrorData("itemov.massOperation.err.noCreateRole"));
					//does not really make sense to continue with this workItem (for finding other prohibited changes)
					prohibitedChangesMap.put(workItemKey, workItemProhibitedChangesMap);
					continue;
				}
				if (!bulkCopy && !AccessBeans.isAllowedToChange(workItemBean, personID)) {
					//no edit right for this workItem
					workItemProhibitedChangesMap.put(SystemFields.INTEGER_ISSUENO, 
							new ErrorData("itemov.massOperation.err.noEditRole"));
					//does not make sense to continue with this workItem (for finding other prohibited changes)
					prohibitedChangesMap.put(workItemKey, workItemProhibitedChangesMap);
					continue;
				}
				//create a workItemBeanCopy for configuring the selectContext
				//If move (project/issueType change) takes place 
				//the project/issueType should be actualized because some datasources depend on this fields
				TWorkItemBean workItemBeanCopy = workItemBean.copyShallow();
				if (isProjectMove || isIssueTypeMove) {
					workItemBeanCopy.setProjectID(projectID);
					workItemBeanCopy.setListTypeID(issueTypeID);
				}
				selectContext.setWorkItemBean(workItemBeanCopy);
				Set<Integer> pseudoFields = getPseudoFields();
				//stores the changes for the current workItemBean
				for (Integer fieldID : selectedFieldsSet) {
					//if field is really selected for bulk operation
					if (selectedFieldsSet.contains(fieldID) && !pseudoFields.contains(fieldID) &&
							(extraDateField==null || !fieldID.equals(extraDateField))) {
						IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						if (fieldTypeRT!=null) {
							IBulkSetter bulkSetter = fieldTypeRT.getBulkSetterDT(fieldID);
							Integer relation = setterRelationMap.get(fieldID);
							bulkSetter.setRelation(relation);
							if (bulkSetter!=null) {
								selectContext.setFieldID(fieldID);
								selectContext.setFieldConfigBean(FieldRuntimeBL.getFieldConfigForProjectIssueTypeField(
										projectsIssueTypesFieldConfigsMap, workItemBeanCopy.getProjectID(), 
										workItemBeanCopy.getListTypeID(), fieldID));
								selectContext.setFieldSettings(FieldRuntimeBL.getFieldSettingsForProjectIssueTypeField(
										projectsIssueTypesFieldSettingsMap, workItemBeanCopy.getProjectID(), workItemBeanCopy.getListTypeID(), fieldID));
								Object value = fieldValueMap.get(fieldID);
								ErrorData errorData = bulkSetter.setWorkItemAttribute(workItemBean,
										fieldID, null, bulkTranformContext, selectContext, value);
								if (errorData!=null) {
									workItemProhibitedChangesMap.put(fieldID, errorData);
								}
							}
						}
					}
				}
				if (selectedFieldsSet.contains(SystemFields.INTEGER_SUPERIORWORKITEM)) {
					Integer parentID = workItemBean.getSuperiorworkitem();
					if(parentID!=null && workItemKey.intValue()==parentID.intValue()){
						workItemProhibitedChangesMap.put(SystemFields.INTEGER_SUPERIORWORKITEM, 
								new ErrorData("itemov.massOperation.err.recursiveParentReference"));
					}
				}
				if (!workItemProhibitedChangesMap.isEmpty()) {
					prohibitedChangesMap.put(workItemKey, workItemProhibitedChangesMap);
				}
			}
		}
		//return with the prohibited errors before saving any issue 
		return prohibitedChangesMap;
	}
	
	/**
	 * Reorganize the map by workItems to a map by fields 
	 * @param prohibitedChangesProWorkItem: 
	 * 	-	key: workItemID
	 * 	-	value: a map: 
	 * 				-	key fieldID
	 * 				-	value ErrorData object
	 * @return a map:
	 * 	-	key: fieldID
	 * 	-	value: a map: 
	 * 				-	key workItemID
	 * 				-	value ErrorData object
	 */
	static Map<Integer, Set<ErrorData>> getProhibitedChangesProField(
			Map<Integer, Map<Integer, ErrorData>> prohibitedChangesProWorkItem, Map<Integer, String> issueIDsMap) {
		Map<Integer, Map<ErrorData, List<Integer>>> prohibitedChangesProField = new HashMap<Integer, Map<ErrorData,List<Integer>>>();
		for (Integer workItemID : prohibitedChangesProWorkItem.keySet()) {
			Map<Integer, ErrorData> fieldsToErrorDataForWorkItem = prohibitedChangesProWorkItem.get(workItemID);
			if (fieldsToErrorDataForWorkItem!=null) {
				Iterator<Integer> iteratorFields = fieldsToErrorDataForWorkItem.keySet().iterator();
				while (iteratorFields.hasNext()) {
					Integer fieldID = iteratorFields.next();
					ErrorData errorData = fieldsToErrorDataForWorkItem.get(fieldID);
					if (errorData!=null) {
						Map<ErrorData, List<Integer>> errorDataToWorkItemsForField = 
							prohibitedChangesProField.get(fieldID);
						if (errorDataToWorkItemsForField==null) {
							errorDataToWorkItemsForField = new HashMap<ErrorData, List<Integer>>();
							prohibitedChangesProField.put(fieldID, errorDataToWorkItemsForField);
						}
						List<Integer> workItemList = errorDataToWorkItemsForField.get(errorData);
						if (workItemList==null) {
							workItemList = new LinkedList<Integer>();
							errorDataToWorkItemsForField.put(errorData, workItemList);
						}
						workItemList.add(workItemID);
					}
				}
			}
		}
		//gather the list of workItems into a string, to be shown to the user
		Map<Integer, Set<ErrorData>> prohibitedChanges = new HashMap<Integer, Set<ErrorData>>();
		for (Integer fieldID : prohibitedChangesProField.keySet()) {
			Set<ErrorData> errorDataSet = new HashSet<ErrorData>();
			Map<ErrorData, List<Integer>> errorDataToWorkItemsForField = 
				prohibitedChangesProField.get(fieldID);
			Iterator<ErrorData> iteratorErrorData = errorDataToWorkItemsForField.keySet().iterator();
			while (iteratorErrorData.hasNext()) {
				ErrorData errorData = iteratorErrorData.next();
				List<Integer> workItemIDs = errorDataToWorkItemsForField.get(errorData);
				StringBuffer stringBuffer = new StringBuffer();
				for (Iterator<Integer> iterator = workItemIDs.iterator(); iterator.hasNext();) {
					Integer workItemID = iterator.next();
					String itemID = null;
					if (issueIDsMap!=null) {
						itemID = issueIDsMap.get(workItemID);
					}
					if (itemID==null) {
						itemID = workItemID.toString();
					}
					stringBuffer.append(itemID);
					if (iterator.hasNext()) {
						stringBuffer.append(", ");
					}
				}
				errorData.addResourceParameterAsFirst(stringBuffer.toString());
				errorDataSet.add(errorData);
			}
			prohibitedChanges.put(fieldID, errorDataSet);
		}
		return prohibitedChanges;
	}	
	
	/**
	 * Get the selectedIssueList from the selectedIssueMap
	 * Important is the order of the issues, the child issues should be saved before the parent issue
	 * (especially important by closing issues, because a parent issue can't be closed if it has open children) 
	 * @param reportBeans
	 * @param selectedChildrenMap
	 * @return
	 */
	static List<Integer> getSelectedIssueListChildrenFirst(ReportBeans reportBeans, 
			Map<Integer, List<Integer>> selectedChildrenMap) {
		List<Integer> selectedIssueList = new LinkedList<Integer>();
		//all report beans in a flat list, but parents first
		List<ReportBean> parentFirstFlatList = reportBeans.getItems();
		Iterator<ReportBean> iterator = parentFirstFlatList.iterator();
		while (iterator.hasNext()) {
			ReportBean reportBean = iterator.next();
			TWorkItemBean workItemBean = reportBean.getWorkItemBean();
			Integer workItemID = workItemBean.getObjectID();
			//add as first
			selectedIssueList.add(0, workItemID);
			Integer parentID = workItemBean.getSuperiorworkitem();
			if (parentID!=null) {
				List<Integer> selectedChildrenList = selectedChildrenMap.get(parentID);
				if (selectedChildrenList==null) {
					selectedChildrenList = new LinkedList<Integer>();
					selectedChildrenMap.put(parentID, selectedChildrenList);
				}
				selectedChildrenList.add(workItemID);
			}
		}
		return selectedIssueList;
	}
		
	
	/**
	 * Get the selectedIssueList from the selectedIssueMap
	 * Important is the order of the issues, the parent issues should be saved 
	 * before the child issue to set the parent relationships and if copy with children filter out 
	 * those selected workItems which are children of other selected issues to avoid copying the same issue two times  
	 * @param reportBeans
	 * @param copyChildren
	 * @param selectedChildren gather the children which are also selected. They will be copied as "first class" workItems 
	 * (i.e. it will not be excluded from the selected issue list and copied as any other not selected children of his parent)
	 * because it should be also affected by the eventual editing before copy. This Set will be used to exclude it from the 
	 * copying of the children of his parent
	 * @return
	 */
	static List<Integer> getSelectedIssueListParentsFirst(ReportBeans reportBeans, 
			boolean copyChildren, Set<Integer> selectedChildren) {
		List<Integer> selectedIssueList = new LinkedList<Integer>();
		//all report beans in a flat list, but parents first
		List<ReportBean> parentFirstFlatList = reportBeans.getItems();
		if (parentFirstFlatList!=null) {
			Set<Integer> descendentSet = new HashSet<Integer>();
			for (ReportBean reportBean : parentFirstFlatList) {
				Integer workItemID = reportBean.getWorkItemBean().getObjectID();
				if (copyChildren) {
					if (descendentSet.contains(workItemID)) {
						selectedChildren.add(workItemID);
					}
					descendentSet.addAll(reportBean.getDescendentsSet());
				}
				selectedIssueList.add(workItemID);
			}
		}
		return selectedIssueList;
	}
	
	/**
	 * Save the gathered allowed changes to the database 
	 * @param workItemContextsMap
	 * @param selectedFieldsSet
     * @param selectedChildren
     * @param setterRelationMap
     * @param fieldValueMap
     * @param bulkCopy
     * @param deepCopy
     * @param copyAttachments
     * @param copyChildren
     * @param copyWatchers
	 * @param person
	 * @param locale 
	 */	
	private static Map<Integer, List<ErrorData>> saveDetails(Map<Integer, WorkItemContext> workItemContextsMap,
			Set<Integer> selectedFieldsSet,
			List<Integer> selectedIssueList, Set<Integer> selectedChildren, 
			Map<Integer, Integer> setterRelationMap,
			Map<Integer, Object> fieldValueMap,
			boolean bulkCopy, 
			boolean deepCopy, boolean copyAttachments, boolean copyChildren, boolean copyWatchers,
			Integer person, Locale locale) {
		Map<Integer, List<ErrorData>> errorsMap = new HashMap<Integer, List<ErrorData>>();
		List<Integer> copiedIssueList = new LinkedList<Integer>();
		Map<Integer, Integer> originalToCopyWorkItemIDs = new HashMap<Integer, Integer>();
		for (Integer workItemID : selectedIssueList) {
			//is the workItem selected for change
			WorkItemContext workItemContext = workItemContextsMap.get(workItemID);
			if (workItemContext!=null) {
				List<ErrorData> errorList = new LinkedList<ErrorData>();
				Integer newWorkItemID = null;
				if (bulkCopy) {
					TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
					workItemBean.setDeepCopy(deepCopy);
					workItemBean.setCopyAttachments(copyAttachments);
					workItemBean.setCopyWatchers(copyWatchers);
					workItemBean.setCopyChildren(copyChildren);
					newWorkItemID = FieldsManagerRT.copy(workItemContext, selectedChildren, originalToCopyWorkItemIDs, errorList, true, false);
					copiedIssueList.add(newWorkItemID);
				} else {
					//validate was done already
					FieldsManagerRT.saveWithoutValidate(workItemContext, errorList, false);
				}
				if(!errorList.isEmpty()) {
					if (newWorkItemID!=null) {
						//probably never (if the copy is created than probably no error occurred)
						errorsMap.put(newWorkItemID, errorList);
					} else {
						errorsMap.put(workItemID, errorList);
					}
				}
			}
		}
		//add/remove watchers (consulted/informed)
		if (!copyWatchers) {
			if (selectedFieldsSet.contains(CONSULTANT_FIELDID) || 
					selectedFieldsSet.contains(INFORMANT_FIELDID)) {
				Iterator<Integer> iterator;
				if (bulkCopy) {
					iterator = copiedIssueList.iterator();
				} else {
					iterator = selectedIssueList.iterator();
				}
				while (iterator.hasNext()) {
					Integer workItemID = iterator.next();
					//add/remove consulted
					if (selectedFieldsSet.contains(CONSULTANT_FIELDID)) {
						MassOperationWatchersBL.addRemoveWatcher(workItemID, setterRelationMap, fieldValueMap, CONSULTANT_FIELDID, RaciRole.CONSULTANT);										
					}
					//add/remove informed
					if (selectedFieldsSet.contains(INFORMANT_FIELDID)) {
						MassOperationWatchersBL.addRemoveWatcher(workItemID, setterRelationMap, fieldValueMap, INFORMANT_FIELDID, RaciRole.INFORMANT);					
					}
				}
			}
		}
		return errorsMap;
	}

	/**
	 * Get the real (system and later custom) fields used as presentFields in workItemContext
	 * @param selectedFieldsSet
	 * @return
	 */
	static Set<Integer> getRealFields(Set<Integer> selectedFieldsSet) {
		Set<Integer> realFields = new HashSet<Integer>();
		if (selectedFieldsSet!=null) {
			for (Integer fieldID : selectedFieldsSet) {
				if (!CONSULTANT_FIELDID.equals(fieldID) && 
						!INFORMANT_FIELDID.equals(fieldID) ) {
					realFields.add(fieldID);
				}
			}
		}
		return realFields;
	}
	
	/**
	 * Get the real (system and later custom) fields used as presentFields in workItemContext
	 * @return
	 */
	static Set<Integer> getPseudoFields() {
		Set<Integer> pseudoFields = new HashSet<Integer>();
		pseudoFields.add(CONSULTANT_FIELDID);
		pseudoFields.add(INFORMANT_FIELDID);
		return pseudoFields;
	}
}

