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

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.exchange.track.NameMappingBL;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.IssueTypeBulkSetter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.resources.LocalizeUtil;

public class SystemIssueTypeRT extends SystemSelectBaseLocalizedRT{
	
	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_ISSUETYPE;
	}

	/**
	 * Loads the edit data source for issueType
	 * Actually never called	 
	 * @param selectContext
	 * @return
	 */
	@Override
	public List loadEditDataSource(SelectContext selectContext) {
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		Integer person = selectContext.getPersonID();
		return IssueTypeBL.loadByPersonAndProjectAndCreateRight(person, workItemBean.getProjectID(),
							workItemBean.getListTypeID(), workItemBean.getSuperiorworkitem(), selectContext.getLocale());
	}
	
	/**
	 * Loads the create data source for issueType	
	 * @param selectContext
	 * @return
	 */
	@Override
	public List loadCreateDataSource(SelectContext selectContext) {
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		Integer person = selectContext.getPersonID();
		return IssueTypeBL.loadByPersonAndProjectAndCreateRight(person, workItemBean.getProjectID(),
							workItemBean.getListTypeID(), workItemBean.getSuperiorworkitem(), selectContext.getLocale());
	}
	
	/**
	 * Gets the show value for a select
	 */
	public String getShowValue(Object value, WorkItemContext workItemContext,Integer fieldID){
		if (value!=null) {
			TListTypeBean itemTypeBean = LookupContainer.getItemTypeBean((Integer)value, workItemContext.getLocale());
			if (itemTypeBean!=null) {
				return itemTypeBean.getLabel();
			}
		}
		return "";
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
			datasource = (List)IssueTypeBL.loadAllSelectable();
		} else {
			datasource = (List)IssueTypeBL.loadAllowedByProjectTypes(projectTypeIDs);
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
		return new IssueTypeBulkSetter(fieldID);
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
		
		Integer[] involvedProjects = massOperationContext.getInvolvedProjectsIDs();
		List datasource = IssueTypeBL.getByProjectsAndPerson(involvedProjects, massOperationContext.getPersonID(), locale, true);
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
		List<TListTypeBean> datasource = null;
		Integer projectID = workflowContext.getProjectID();
		Integer projectTypeID = workflowContext.getProjectTypeID();
		if (projectTypeID==null && projectID!=null) {
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				projectTypeID = projectBean.getProjectType();
			}
		}
		if (projectTypeID==null) {
			datasource = IssueTypeBL.loadAllSelectable();
		} else {
			datasource = IssueTypeBL.loadAllowedByProjectType(projectTypeID);
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
			TListTypeBean issueTypeBean = new TListTypeBean();
			issueTypeBean.setLabel(MatcherContext.getLocalizedParameter(locale));
			issueTypeBean.setObjectID(optionID);
			return issueTypeBean;
		}
		return IssueTypeBL.loadByPrimaryKey(optionID);
	}
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	@Override
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.LISTTYPE;
	}
	
	
	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	@Override
	public ISerializableLabelBean getNewSerializableLabelBean() {
		return new TListTypeBean();
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
		TListTypeBean issueTypeBean = null;
		Integer primaryKey = LocalizeUtil.getDropDownPrimaryKeyFromLocalizedText(
				new TListTypeBean().getKeyPrefix(), label, locale);
		if (primaryKey!=null) {
			issueTypeBean = LookupContainer.getItemTypeBean(primaryKey);
			if (issueTypeBean!=null) {
				return primaryKey;
			}
		}		
		List<TListTypeBean> issueTypeBeans = IssueTypeBL.loadByLabel(label); 
		if (issueTypeBeans!=null && !issueTypeBeans.isEmpty()) {
			issueTypeBean =  issueTypeBeans.get(0);
		}
		if (issueTypeBean!=null) {
			return issueTypeBean.getObjectID();
		}
		return null;
	}
	
	/**
	 * Gets the datasource for all possible list entries 
	 * @param fieldID
	 * @return
	 */
	@Override
	public List<ILabelBean> getDataSource(Integer fieldID) {
		return (List)IssueTypeBL.loadAll();
	}
}
