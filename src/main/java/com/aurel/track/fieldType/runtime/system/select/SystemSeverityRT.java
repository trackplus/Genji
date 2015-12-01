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

import com.aurel.track.admin.customize.lists.systemOption.SeverityBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TSeverityBean;
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

public class SystemSeverityRT extends SystemSelectBaseLocalizedRT{
	
	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_SEVERITY;
	}
	
	/**
	 * Loads the edit datasource for severities
	 * @param selectContext
	 * @return
	 */
	@Override
	public List loadEditDataSource(SelectContext selectContext) {
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		List<TSeverityBean> dataSource= SeverityBL.loadByProjectAndIssueType(workItemBean.getProjectID(), 
				workItemBean.getListTypeID(), workItemBean.getSeverityID());
		return LocalizeUtil.localizeDropDownList(dataSource, selectContext.getLocale());
	}
	
	/**
	 * Loads the create datasource for severities
	 * @param selectContext
	 * @return
	 */
	@Override
	public List loadCreateDataSource(SelectContext selectContext) {
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		List<TSeverityBean> dataSource = SeverityBL.loadByProjectAndIssueType(workItemBean.getProjectID(),
				 workItemBean.getListTypeID(), null);
		TSeverityBean severityBean;
		if (dataSource!=null && !dataSource.isEmpty()) {
			if (workItemBean.getSeverityID()==null) {
				//severity was not set in setDefaultValues(): 
				//set explicitely to the first entry from the list
				//Reason: if the tab where this field is present is not selected before saving the issue
				//this field would not be submitted by tab change or save 
				//and it would result an issue without severity
				severityBean = (TSeverityBean)dataSource.get(0);
				workItemBean.setSeverityID(severityBean.getObjectID());
			} else {
				//verify whether the value set in setDefaultValues() is contained in the dataSource list
				Iterator<TSeverityBean> iterator = dataSource.iterator();
				boolean found = false; 
				while (iterator.hasNext()) {
					severityBean = iterator.next();
					if (severityBean.getObjectID().equals(workItemBean.getSeverityID())) {
						found=true;
						break;
					}
				}
				if (!found) {
					//the value set in setDefaultValues() is not found in the resulting list
					//set the first from the list
					severityBean  = (TSeverityBean)dataSource.get(0);
					workItemBean.setSeverityID(severityBean.getObjectID());
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
			datasource = (List)SeverityBL.loadAll();
		} else {
			datasource = (List)SeverityBL.loadAllowedByProjectTypesAndIssueTypes(projectTypeIDs, matcherDatasourceContext.getItemTypeIDs());
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
	 * @param personBeanD
	 * @param locale
	 * @return
	 */
	@Override
	public void loadBulkOperationDataSource(MassOperationContext massOperationContext,
			MassOperationValue massOperationValue,
			Integer parameterCode, TPersonBean personBeanD, Locale locale) {
		List<IBeanID> datasource = (List)SeverityBL.loadAll(locale);
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
		List<TSeverityBean> datasource = null;
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
			datasource = SeverityBL.loadAll();
		} else {
			datasource = SeverityBL.loadByProjectAndIssueType(projectID, itemTypeID, (Integer)fieldChangeValue.getValue());
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
			TSeverityBean severityBean = new TSeverityBean();
			severityBean.setLabel(MatcherContext.getLocalizedParameter(locale));
			severityBean.setObjectID(optionID);
			return severityBean;
		}
		return SeverityBL.loadByPrimaryKey(optionID);
	}	
		
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	@Override
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.SEVERITY;
	}
		
	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	@Override
	public ISerializableLabelBean getNewSerializableLabelBean() {
		return new TSeverityBean();
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
		TSeverityBean severityBean = null;
		Integer primaryKey = LocalizeUtil.getDropDownPrimaryKeyFromLocalizedText(
				new TSeverityBean().getKeyPrefix(), label, locale);
		if (primaryKey!=null) {
			severityBean = LookupContainer.getSeverityBean(primaryKey);
			if (severityBean!=null) {
				return primaryKey;
			}
		}
		List<TSeverityBean> severityBeans = SeverityBL.loadByLabel(label);
		if (severityBeans!=null && !severityBeans.isEmpty()) {
			severityBean =  severityBeans.get(0);
		}	
		if (severityBean!=null) {
			return severityBean.getObjectID();
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
			Integer severity = workItemBeanOriginal.getSeverityID(); 
			if (severity!=null && severity.equals(objectID)) {
				return true;
			}
		}
		List<TSeverityBean> severityBeansList = SeverityBL.loadByProjectAndIssueType(
				serializableBeanAllowedContext.getProjectID(), 
				serializableBeanAllowedContext.getIssueTypeID(), null);
		if (severityBeansList!=null) {
			Iterator<TSeverityBean> iterator = severityBeansList.iterator();
			while (iterator.hasNext()) {
				TSeverityBean severityBean = iterator.next();
				if (severityBean.getObjectID().equals(objectID)) {
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
		return (List)SeverityBL.loadAll();
	}
}
