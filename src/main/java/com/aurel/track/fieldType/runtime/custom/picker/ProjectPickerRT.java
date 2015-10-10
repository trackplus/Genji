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



package com.aurel.track.fieldType.runtime.custom.picker;

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
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.ProjectPickerBulkSetter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.fieldChange.apply.MultipleTreeFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.MultipleTreeFieldChangeConfig;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.SerializableBeanAllowedContext;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.design.ProjectPickerMatcherDT;
import com.aurel.track.fieldType.runtime.system.select.SystemProjectRT;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.util.TreeNode;

/**
 * Class for project picker runtime
 * @author Tamas Ruff
 *
 */
public class ProjectPickerRT extends CustomTreePickerRT {
	
	/**
	 * Loads the settings for a specific field like default value(s),
	 * datasources (for lists) etc. by editing an existing issue
	 * It might differ from loadCreateSettings because it could be need 
	 * to add existing entries in the list (also when right already revoked)
	 * @param selectContext 
	 * @return
	 */
	public List loadEditDataSource(SelectContext selectContext)	{
		TPersonBean personBean = LookupContainer.getPersonBean(selectContext.getPersonID());
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		Object pickedProject=workItemBean.getAttribute(selectContext.getFieldID());	
		Set<Integer> projectIDSet = null;
        if (pickedProject!=null) {
        	Object[] pickedProjectArr = (Object[])pickedProject;
        	projectIDSet = new HashSet<Integer>();
        	for (int i = 0; i < pickedProjectArr.length; i++) {
        		projectIDSet.add((Integer)pickedProjectArr[i]);
			}
        }
        return ProjectPickerBL.getTreeNodesForCreateModify(projectIDSet, true, personBean, selectContext.getLocale());
	}
	
	/**
	 * Returns the datasource for a userPicker field by creating a new issue.  
	 * When the list is empty this field should be set explicitly to null in the workItem. 
	 * (An empty HTML list will not send request parameter and it will remain the original value. This should be avoided)  
	 * @param selectContext
	 * @return
	 */
	public List loadCreateDataSource(SelectContext selectContext) {
		TPersonBean personBean = LookupContainer.getPersonBean(selectContext.getPersonID());
		 return ProjectPickerBL.getTreeNodesForCreateModify(null, true, personBean, selectContext.getLocale());
	}
	
	/**
	 * Gets the flat datasource
	 * @param personID
	 * @return
	 */
	public List<ILabelBean> getFlatDataSource(Integer personID) {
		return  (List)ProjectBL.loadUsedProjectsFlat(personID);
	}
	
	/**
	 * Identifies the fieldID of the fieldType the picker is based of
	 * A project picker is based on projects 
	 * @return
	 */	
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_PROJECT;
	}
	
	/**
	 * Gets the label bean for an objectID
	 * @param objectID
	 * @param locale
	 * @return
	 */
	protected ILabelBean lookupLabelBean(Integer objectID, Locale locale) {
		return LookupContainer.getNotLocalizedLabelBean(getSystemOptionType(), objectID);
	}
	
	/**
	 * Loads the datasource for the matcher
	 * used by select fields to get the possible values
	 * It will be called from both field expressions and upper selects 
	 * The value can be a List for simple select or a Map of lists for composite selects
	 * @param matcherValue
	 * @param matcherDatasourceContext the data source may be project dependent. 
	 * @param parameterCode for composite selects
	 * @return the datasource (list or tree)
	 */	
	public Object getMatcherDataSource(IMatcherValue matcherValue, MatcherDatasourceContext matcherDatasourceContext, Integer parameterCode) {
		return new SystemProjectRT().getMatcherDataSource(matcherValue, matcherDatasourceContext, parameterCode);
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
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new ProjectPickerBulkSetter(fieldID);
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
        List<TreeNode> projectTree = ProjectPickerBL.getTreeNodesForCreateModify(null, true, personBean, locale);
        massOperationValue.setPossibleValues(projectTree);
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new MultipleTreeFieldChangeConfig(fieldID);
	}
	
	/**
	 * Gets the IFieldChangeApply object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new MultipleTreeFieldChangeApply(fieldID);
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
		 List<TreeNode> projectTree = ProjectPickerBL.getTreeNodesForCreateModify(null, true, personBean, locale);
		 fieldChangeValue.setPossibleValues(projectTree);
	}
	
	/**
	 * Get the ILabelBean by primary key 
	 * @param personID
	 * @return
	 */
	@Override
	public ILabelBean getLabelBean(Integer optionID, Locale locale) {
		return new SystemProjectRT().getLabelBean(optionID, locale);
	}

	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	@Override
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
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable
	 * @return
	 */
	public boolean isGroupable() {
		//not groupable because it is a multiple select tree
		return false;
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
		return new SystemProjectRT().getLookupIDByLabel(fieldID, projectID, issueTypeID, locale, label, lookupBeansMap, componentPartsMap);
	}
	
	/**
	 * Whether the lookupID found by label is allowed in 
	 * the context of serializableBeanAllowedContext
	 * In excel the lookup entries are not limited by the user interface controls
	 * This method should return false if the lookupID
	 * is not allowed (for ex. a person without manager role was set as manager) 
	 * @param objectID
	 * @param serializableBeanAllowedContext
	 * @return
	 */
	public boolean lookupBeanAllowed(Integer objectID, 
			SerializableBeanAllowedContext serializableBeanAllowedContext) {
		return true;
	}
	
	/**
	 * Gets the datasource for all possible list entries 
	 * @param fieldID
	 * @return
	 */
	public List<ILabelBean> getDataSource(Integer fieldID) {
		return new SystemProjectRT().getDataSource(fieldID);
	}


	
}
