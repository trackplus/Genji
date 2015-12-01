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

package com.aurel.track.item.possibleOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.treeConfig.field.CustomListsConfigBL;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.bulkSetters.BulkTranformContext;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ISelect;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.lock.ItemLockBL;
import com.aurel.track.item.massOperation.MassOperationFieldsBL;
import com.aurel.track.item.massOperation.MassOperationRightsBL;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;

/**
 * Computes the possible field values for lists
 * @author Tamas
 *
 */
public class PossibleFieldOptionsBL {

	/**
	 * Gets the possible options pro fieldID
	 * @param issueIDs
	 * @param fieldIDs
	 * @param personID
	 * @param locale
	 * @param partialValidOptionsMap
	 * @return
	 */
	static Map<Integer, List<Integer>> getPossibleOptionsMap(List<Integer> issueIDs, Set<Integer> fieldIDs, Integer personID, Locale locale, Map<Integer, Set<Integer>> partialValidOptionsMap) {
		List<TWorkItemBean> workItemBeans = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(GeneralUtils.createIntArrFromIntegerCollection(issueIDs), personID, true, true, false);
		Map<Integer, Set<Integer>> projectIssueTypeContexts = ItemBL.getProjectIssueTypeContexts(workItemBeans);
		Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap = 
				FieldRuntimeBL.loadFieldConfigsInContextsAndTargetProjectAndIssueType(
					projectIssueTypeContexts, fieldIDs, locale, null, null);
		Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap =
				FieldRuntimeBL.getFieldSettingsForFieldConfigs(projectsIssueTypesFieldConfigsMap);
		Map<Integer, WorkItemContext> workItemContextsMap = FieldsManagerRT.createImportContext(
				workItemBeans, fieldIDs, projectsIssueTypesFieldConfigsMap, projectsIssueTypesFieldSettingsMap, null, null, personID, locale);
		return getPossibleOptionsMap(workItemContextsMap, workItemBeans, projectIssueTypeContexts, projectsIssueTypesFieldConfigsMap,
				projectsIssueTypesFieldSettingsMap, issueIDs, fieldIDs, personID, locale, partialValidOptionsMap);
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
	private static Map<Integer, List<Integer>> getPossibleOptionsMap(
			Map<Integer, WorkItemContext> workItemContextsMap,
			List<TWorkItemBean> selectedWorkItemBeans, Map<Integer, Set<Integer>> projectIssueTypeContexts, 
			Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap,
			Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap,
			List<Integer> selectedIssueList, Set<Integer> selectedFieldsSet,
			Integer personID, Locale locale, Map<Integer, Set<Integer>> partialValidOptionsMap) {
		Map<Integer, List<Integer>> possibleOptionsMap = new HashMap<Integer, List<Integer>>();
		SelectContext selectContext = new SelectContext();
		selectContext.setPersonID(personID);
		selectContext.setCreate(false);
		selectContext.setLocale(locale);
		
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
		//for each selected issue
		if (selectedIssueList!=null && selectedFieldsSet!=null) {
			for (Integer workItemKey : selectedIssueList) {
				WorkItemContext workItemContext = workItemContextsMap.get(workItemKey);
				if (workItemContext!=null) {
					//gather the map of real changes
					//gather the list of errors caused by prohibited operations
					TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
					selectContext.setWorkItemBean(workItemBean);
					for (Integer fieldID : selectedFieldsSet) {
						IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						if (fieldTypeRT!=null && fieldTypeRT.isLookup()) {
							Integer projectID = workItemBean.getProjectID();
							Integer issueTypeID = workItemBean.getListTypeID();
							ISelect selectFieldType = (ISelect)fieldTypeRT;
							selectContext.setFieldID(fieldID);
							selectContext.setFieldConfigBean(FieldRuntimeBL.getFieldConfigForProjectIssueTypeField(
								projectsIssueTypesFieldConfigsMap, projectID, issueTypeID, fieldID));
							selectContext.setFieldSettings(FieldRuntimeBL.getFieldSettingsForProjectIssueTypeField(
								projectsIssueTypesFieldSettingsMap, projectID, issueTypeID, fieldID));
							List<ILabelBean> allowedIBeanList = selectContext.getDatasourceCache(fieldID, projectID, issueTypeID);
							if (allowedIBeanList==null) {
								allowedIBeanList = selectFieldType.loadEditDataSource(selectContext);
								selectContext.addDatasourceCache(fieldID, projectID, issueTypeID, allowedIBeanList);
								List<Integer> actualFieldOptionsList = GeneralUtils.createIntegerListFromBeanList(allowedIBeanList);
								List<Integer> previousTotalFieldOptionsList = possibleOptionsMap.get(fieldID);
								//total match
								if (previousTotalFieldOptionsList==null) {
									possibleOptionsMap.put(fieldID, actualFieldOptionsList);
								} else {
									previousTotalFieldOptionsList.retainAll(actualFieldOptionsList);
								}
								//partial match
								Set<Integer> previousPartialValidOptionsSet = partialValidOptionsMap.get(fieldID);
								if (previousPartialValidOptionsSet==null) {
									partialValidOptionsMap.put(fieldID, GeneralUtils.createSetFromIntegerArr(GeneralUtils.createIntegerArrFromCollection(actualFieldOptionsList)));
								} else {
									previousPartialValidOptionsSet.addAll(actualFieldOptionsList);
								}
							}
						}
					}
				}
			}
			for (Map.Entry<Integer, List<Integer>> entry : possibleOptionsMap.entrySet()) {
				Integer fieldID = entry.getKey();
				List<Integer> validOptions = entry.getValue();
				if (validOptions!=null) {
					Set<Integer> previousPartialValidOptionsSet = partialValidOptionsMap.get(fieldID);
					if (previousPartialValidOptionsSet!=null) {
						previousPartialValidOptionsSet.removeAll(validOptions);
						if (previousPartialValidOptionsSet.isEmpty()) {
							partialValidOptionsMap.remove(fieldID);
						}
					}
				}
				
			}
		}
		//return with the prohibited errors before saving any issue 
		return possibleOptionsMap;
	}
}
