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


package com.aurel.track.fieldType.runtime.system.select;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.ProjectPickerBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.exchange.track.NameMappingBL;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.ProjectBulkSetter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.fieldChange.apply.SelectFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.SystemSingleTreeFieldChangeConfig;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ITreeSelect;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.design.ProjectPickerMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.report.execute.SimpleTreeNode;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.HierarchicalBeanUtils;
import com.aurel.track.util.TreeNode;

public class SystemProjectRT extends SystemSelectBaseRT implements ITreeSelect {
		
	
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
	/**
	 * Preprocess a custom attribute before save:
	 * implemented when some extra save is needed for ex. by extensible select
	 * or a system field should be calculated before save (wbs)
	 * @param fieldID
	 * @param parameterCode
	 * @param validConfig
	 * @param fieldSettings
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param contextInformation
	 */
	@Override
	public void processBeforeSave(Integer fieldID, Integer parameterCode, Integer validConfig, Map<String, Object> fieldSettings,
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, Map<String, Object> contextInformation) {
		workItemDAO.projectChanged(workItemBean, workItemBeanOriginal);
	}
		
	
	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_PROJECT;
	}
	
	/**
	 * Whether the datasource is tree or list
	 * @return
	 */
	public boolean isTree() {
		return true;
	}
	
	/**
	 * Gets the show value for a select
	 */
	public String getShowValue(Object value, WorkItemContext workItemContext,Integer fieldID){
		if (value!=null) {
			TProjectBean projectBean = LookupContainer.getProjectBean((Integer)value);
			if (projectBean!=null) {
				return projectBean.getLabel();
			}
		}
		return "";
	}
	
	/**
	 * Gets the complete hierarchy of the involved values by completing the eventual holes between
	 * @param involvedValues
	 * @return
	 */
	public List<SimpleTreeNode> getSimpleTreeNodesWithCompletePath(List<Integer> involvedValues) {
		return HierarchicalBeanUtils.getSimpleProjectTreeWithCompletedPath(
				involvedValues, SystemFields.INTEGER_PROJECT, new HashMap<Integer, SimpleTreeNode>());
	}
	
	/**
	 * Loads the edit datasource for projects
	 * Actually never called
	 * @param selectContext
	 * @return
	 */
	public List loadEditDataSource(SelectContext selectContext){
		return (List)ProjectBL.loadProjectsWithModifyIssueRight(selectContext.getPersonID());
	}
	
	/**
	 * Loads the create datasource for projects:
	 * all projects where the current user has create rights
	 * @param selectContext
	 * @return
	 */
	public List loadCreateDataSource(SelectContext selectContext){
		return (List)ProjectBL.loadProjectsWithCreateIssueRight(selectContext.getPersonID());
	}

	/**
	 * Gets the context with actual item data for injecting content into the labels
	 * @param fieldID
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public Map<String, Object> getLabelContext(Integer fieldID, Object value, Locale locale) {
		Map<String, Object> labelContextMap = null;
		Integer selectedValue = null;
		if (value!=null) {
			try {
				selectedValue = (Integer)value;
			} catch(Exception ex) {
				
			}
			if (selectedValue!=null) {
				TProjectBean projectBean = LookupContainer.getProjectBean(selectedValue);
				if (projectBean!=null) {
					labelContextMap = new HashMap<String, Object>();
					String description = projectBean.getDescription();
					if (description!=null && description.length()>0) {
						labelContextMap.put("description", description);
					}
				}
			}
		}
		return labelContextMap;
	}
	/**
	 * Loads the datasource for the matcher
	 * used by select fields to get the possible values
	 * It will be called from both field expressions and upper selects 
	 * The value can be a list for simple select or a map of lists for composite selects or a tree
	 * @param matcherValue
	 * @param matcherDatasourceContext the data source may be project dependent. 
	 * @param parameterCode for composite selects
	 * @return the datasource (list or tree)
	 */	
	public Object getMatcherDataSource(IMatcherValue matcherValue, MatcherDatasourceContext matcherDatasourceContext, Integer parameterCode) {
		Set<Integer> selectedProjectIDsSet = new HashSet<Integer>();
		if (matcherValue!=null && matcherValue.getValue()!=null) {
			Integer[] selectedProjects = (Integer[])matcherValue.getValue();
			selectedProjectIDsSet = GeneralUtils.createSetFromIntegerArr(selectedProjects);
		}
		return ProjectPickerBL.getTreeNodesForRead(selectedProjectIDsSet, true, matcherDatasourceContext.isWithParameter(),
				matcherDatasourceContext.getPersonBean(), 
				matcherDatasourceContext.getLocale());	
	}
	
	
	/**
	 * Design time matcher object for configuring the matcher
	 * It is the same for system and custom select fields
	 * But the runtime matchers and the matcher converter differ 
	 * for system and custom fields
	 * @param fieldID 
	 */
	@Override
	public IMatcherDT getMatcherDT(Integer fieldID) {
		return new ProjectPickerMatcherDT(fieldID);
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new ProjectBulkSetter(fieldID);
	}
	
	/**
	 * Loads the datasource for the mass operation
	 * used mainly by select fields to get 
	 * the all possible options for a field (system or custom select) 
	 * It also sets a value if not yet selected
	 * The value can be a List for simple select or a Map of lists for composite selects  
	 * @param massOperationContext
	 * @param massOperationValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public void loadBulkOperationDataSource(MassOperationContext massOperationContext,
			MassOperationValue massOperationValue,
			Integer parameterCode, TPersonBean personBean, Locale locale) {
		List<TreeNode> projectTree = ProjectPickerBL.getTreeNodesForCreateModify(null, false, personBean, locale);
        massOperationValue.setPossibleValues(projectTree);
        massOperationValue.setValue(getInitValue(massOperationContext, massOperationValue.getFieldID(),
        		null, (Integer)massOperationValue.getValue(), projectTree));
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new SystemSingleTreeFieldChangeConfig(fieldID);
	}
	
	/**
	 * Gets the IActivityExecute object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new SelectFieldChangeApply(fieldID, false);
	}
	
	/**
	 * Loads the datasource and value for configuring the field change
	 * @param workflowContext
	 * @param fieldChangeValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 */
	public void loadFieldChangeDatasourceAndValue(WorkflowContext workflowContext,
			FieldChangeValue fieldChangeValue, 
			Integer parameterCode, TPersonBean personBean, Locale locale) {
		List<TreeNode> projectTree = ProjectPickerBL.getTreeNodesForCreateModify(null, false, personBean, locale);
		fieldChangeValue.setPossibleValues(projectTree);
		fieldChangeValue.setValue(getInitValue(null, fieldChangeValue.getFieldID(),
        		null, (Integer)fieldChangeValue.getValue(), projectTree));
	}
	
	/**
	 * Gets the value for the field by best effort:
	 * If not yet set sets the select value to the actual value 
	 * of the first selected workItemBean's corresponding field value	 
	 * (Avoid setting the value automatically to the first one)
	 * If already set verify whether the previous value is still valid
	 * according to the new possible values list 
	 * @param massOperationContext
	 * @param fieldID
	 * @param paramaterCode
	 * @param actualValue
	 * @param possibleValues
	 * @return
	 */
	protected Integer getInitValue(MassOperationContext massOperationContext, 
			Integer fieldID, Integer paramaterCode, Integer actualValue, List<TreeNode> possibleValues) {
		if (possibleValues==null || possibleValues.isEmpty()) {
			return null;
		}
		if (actualValue==null && massOperationContext!=null) {
			TWorkItemBean firstWorkItemBean = massOperationContext.getFirstSelectedWorkItemBean();
			if (firstWorkItemBean!=null) {
				try {
					actualValue = (Integer)firstWorkItemBean.getAttribute(fieldID, paramaterCode);			
				} catch (Exception e) {
				}
			}
		}
		if (possibleValues!=null && !possibleValues.isEmpty()) {
			if (ProjectBL.idExists(actualValue, possibleValues)) {
				return actualValue;
			} else {
				return Integer.valueOf(possibleValues.get(0).getId());
			}
		}
		return null;
	}
	
	/**
	 * Get the ILabelBean by primary key 
	 * @return
	 */
	@Override
	public ILabelBean getLabelBean(Integer optionID, Locale locale) {
		if (optionID!=null && 
				optionID.equals(MatcherContext.PARAMETER)) {
			TProjectBean projectBean = new TProjectBean();
			projectBean.setLabel(MatcherContext.getLocalizedParameter(locale));
			projectBean.setObjectID(optionID);
			return projectBean;
		}
		return ProjectBL.loadByPrimaryKey(optionID);
	}
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.PROJECT;
	}
		
	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	public ISerializableLabelBean getNewSerializableLabelBean() {
		return new TProjectBean();
	}
	
	/**
	 * Gets the ID by the label
	 * @param fieldID
	 * @param projectID
	 * @param issueTypeID
	 * @param locale
	 * @param label
	 * @param lookupBeansMap
	 * @param componentPartsMap
	 * @return
	 */
	public Integer getLookupIDByLabel(Integer fieldID,
			Integer projectID, Integer issueTypeID, 
			Locale locale, String label, 
			Map<String, ILabelBean> lookupBeansMap, Map<Integer, Integer> componentPartsMap) {
		Integer objectID = NameMappingBL.getExactMatch(label, lookupBeansMap);
		if (objectID!=null) {
			return objectID;
		}
		TProjectBean projectBean = ProjectBL.loadByLabel(label);
		if (projectBean!=null) {
			return projectBean.getObjectID();
		}
		return null;
	}
	
	/**
	 * Gets the datasource for all possible list entries 
	 * @param fieldID
	 * @return
	 */
	public List<ILabelBean> getDataSource(Integer fieldID) {
		return (List)ProjectBL.loadAll();
	}
}
