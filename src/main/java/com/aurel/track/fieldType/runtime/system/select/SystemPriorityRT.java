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


package com.aurel.track.fieldType.runtime.system.select;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.lists.systemOption.PriorityBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.exchange.track.NameMappingBL;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.SerializableBeanAllowedContext;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.resources.LocalizeUtil;

public class SystemPriorityRT extends SystemSelectBaseLocalizedRT{
	
	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_PRIORITY;
	}
	
	/**
	 * Loads the edit datasource for priorities
	 * @param selectContext
	 * @return
	 */
	@Override
	public List loadEditDataSource(SelectContext selectContext) {
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		List<TPriorityBean> dataSource = PriorityBL.loadByProjectAndIssueType(workItemBean.getProjectID(),
				workItemBean.getListTypeID(), workItemBean.getPriorityID());
		return LocalizeUtil.localizeDropDownList(dataSource, selectContext.getLocale());
	}
	
	/**
	 * Loads the create datasource for priorities	
	 * @param selectContext
	 * @return
	 */
	@Override
	public List loadCreateDataSource(SelectContext selectContext) {
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		List<TPriorityBean> dataSource = PriorityBL.loadByProjectAndIssueType(workItemBean.getProjectID(),
				 workItemBean.getListTypeID(), null);
		TPriorityBean priorityBean;
		if (dataSource!=null && !dataSource.isEmpty()) {
			if (workItemBean.getPriorityID()==null) {
				//priority was not set in setDefaultValues(): 
				//set explicitely to the first entry from the list
				//Reason: if the tab where this field is present is not selected before saving the issue
				//this field would not be submitted by tab change or save 
				//and it would result an issue without priority
				priorityBean = (TPriorityBean)dataSource.get(0);
				workItemBean.setPriorityID(priorityBean.getObjectID());
			} else {
				//verify whether the value set in setDefaultValues() is contained in the dataSource list
				Iterator<TPriorityBean> iterator = dataSource.iterator();
				boolean found = false; 
				while (iterator.hasNext()) {
					priorityBean = iterator.next();
					if (priorityBean.getObjectID().equals(workItemBean.getPriorityID())) {
						found=true;
						break;
					}
				}
				if (!found) {
					//the value set in setDefaultValues() is not found in the resulting list
					//set the first from the list
					priorityBean  = (TPriorityBean)dataSource.get(0);
					workItemBean.setPriorityID(priorityBean.getObjectID());
				}
			}
			
		}
		return LocalizeUtil.localizeDropDownList(dataSource, selectContext.getLocale());
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
	@Override
	public Object getMatcherDataSource(IMatcherValue matcherValue, MatcherDatasourceContext matcherDatasourceContext, Integer parameterCode) {
		List<ILabelBean> datasource;
		Integer[] projectTypeIDs = ProjectTypesBL.getProjectTypeIDsForProjectIDs(matcherDatasourceContext.getProjectIDs());
		if (projectTypeIDs == null || projectTypeIDs.length==0) {
			datasource = (List)PriorityBL.loadAll();
		} else {
			datasource = (List)PriorityBL.loadAllowedByProjectTypesAndIssueTypes(projectTypeIDs, matcherDatasourceContext.getItemTypeIDs());
		}
		Locale locale = matcherDatasourceContext.getLocale();
		LocalizeUtil.localizeDropDownList(datasource, locale);
		if (matcherDatasourceContext.isWithParameter()) {
			datasource.add(getLabelBean(MatcherContext.PARAMETER, locale));
		}
		if (matcherDatasourceContext.isInitValueIfNull() && matcherValue!=null) {
			Object value = matcherValue.getValue();
			if (value==null && datasource!=null && !datasource.isEmpty()) {
				matcherValue.setValue(new Integer[] {datasource.get(0).getObjectID()});
			}
		}
		return datasource;
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		IBulkSetter bulkSetter = super.getBulkSetterDT(fieldID);
		//should be verified against project types
		bulkSetter.setSelectValueSurelyAllowed(false);
		return bulkSetter;
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
		List<IBeanID> datasource = (List)PriorityBL.loadAll(locale);
		massOperationValue.setPossibleValues(datasource);
		massOperationValue.setValue(getBulkSelectValue(massOperationContext,
				massOperationValue.getFieldID(), null, 
				(Integer)massOperationValue.getValue(), 
				(List<IBeanID>)massOperationValue.getPossibleValues()));
	}
	
	/**
	 * Loads the datasource and value for configuring the field change
	 * @param workflowContext
	 * @param fieldChangeValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 */
	@Override
	public void loadFieldChangeDatasourceAndValue(WorkflowContext workflowContext,
			FieldChangeValue fieldChangeValue, 
			Integer parameterCode, TPersonBean personBean, Locale locale) {
		List<TPriorityBean> datasource = null;
		Integer itemTypeID = workflowContext.getItemTypeID();
		Integer projectID = workflowContext.getProjectID();
		Integer projectTypeID = workflowContext.getProjectTypeID();
		if (projectTypeID==null && projectID!=null) {
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				projectTypeID = projectBean.getProjectType();
			}
		}
		if (projectTypeID==null || itemTypeID==null) {
			datasource = PriorityBL.loadAll();
		} else {
			datasource = PriorityBL.loadByProjectAndIssueType(projectID, itemTypeID, (Integer)fieldChangeValue.getValue());
		}
		fieldChangeValue.setPossibleValues(LocalizeUtil.localizeDropDownList(datasource, locale));
		fieldChangeValue.setValue(getBulkSelectValue(null,
				fieldChangeValue.getFieldID(), null, 
				(Integer)fieldChangeValue.getValue(), 
				(List<IBeanID>)fieldChangeValue.getPossibleValues()));
	}
	
	/**
	 * Get the ILabelBean by primary key 
	 * @return
	 */
	@Override
	public ILabelBean getLabelBean(Integer optionID, Locale locale) {
		if (optionID!=null && 
				optionID.equals(MatcherContext.PARAMETER)) {
			TPriorityBean priorityBean = new TPriorityBean();
			priorityBean.setLabel(MatcherContext.getLocalizedParameter(locale));
			priorityBean.setObjectID(optionID);
			return priorityBean;
		}
		return PriorityBL.loadByPrimaryKey(optionID);
	}
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	@Override
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.PRIORITY;
	}
	
	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	@Override
	public ISerializableLabelBean getNewSerializableLabelBean() {
		return new TPriorityBean();
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
	@Override
	public Integer getLookupIDByLabel(Integer fieldID,
			Integer projectID, Integer issueTypeID, 
			Locale locale, String label, 
			Map<String, ILabelBean> lookupBeansMap, Map<Integer, Integer> componentPartsMap) {
		Integer objectID = NameMappingBL.getExactMatch(label, lookupBeansMap);
		if (objectID!=null) {
			return objectID;
		}
		TPriorityBean priorityBean = null;
		Integer primaryKey = LocalizeUtil.getDropDownPrimaryKeyFromLocalizedText(
				new TPriorityBean().getKeyPrefix(), label, locale);
		if (primaryKey!=null) {
			priorityBean = LookupContainer.getPriorityBean(primaryKey);
			if (priorityBean!=null) {
				return primaryKey;
			}
		}
		List<TPriorityBean> priorityBeans = PriorityBL.loadByLabel(label); 
		if (priorityBeans!=null && !priorityBeans.isEmpty()) {
			priorityBean =  priorityBeans.get(0);
		}		
		if (priorityBean!=null) {
			return priorityBean.getObjectID();
		}
		return null;
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
	@Override
	public boolean lookupBeanAllowed(Integer objectID, 
			SerializableBeanAllowedContext serializableBeanAllowedContext) {
		TWorkItemBean workItemBeanOriginal = serializableBeanAllowedContext.getWorkItemBeanOriginal();
		if (workItemBeanOriginal!=null) {
			Integer priority = workItemBeanOriginal.getPriorityID(); 
			if (priority!=null && priority.equals(objectID)) {
				return true;
			}
		}
		List<TPriorityBean> priorityBeansList = PriorityBL.loadByProjectAndIssueType(
				serializableBeanAllowedContext.getProjectID(), 
				serializableBeanAllowedContext.getIssueTypeID(), null);
		if (priorityBeansList!=null) {
			Iterator<TPriorityBean> iterator = priorityBeansList.iterator();
			while (iterator.hasNext()) {
				TPriorityBean priorityBean = iterator.next();
				if (priorityBean.getObjectID().equals(objectID)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Gets the datasource for all possible list entries 
	 * @param fieldID
	 * @return
	 */
	@Override
	public List<ILabelBean> getDataSource(Integer fieldID) {
		return (List)PriorityBL.loadAll();
	}
}
