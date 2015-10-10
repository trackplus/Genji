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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TStateBean;
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
import com.aurel.track.item.workflow.execute.StatusWorkflow;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

public class SystemStateRT extends SystemSelectBaseLocalizedRT{

	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_STATE;
	}
	
	/**
	 * Loads the edit data source for state list 
	 * @param selectContext
	 * @return
	 */
	public List loadEditDataSource(SelectContext selectContext) {
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		Integer person = selectContext.getPersonID();
		List<TStateBean> dataSource;
		if (workItemBean.isAccessLevelFlag()) {
			//for private issue do not make workflow limitations
			dataSource = StatusBL.getByProjectTypeIssueTypeAssignments(workItemBean.getProjectID(),
					workItemBean.getListTypeID(), workItemBean.getStateID());
		} else {
			dataSource = StatusWorkflow.loadStatesTo(workItemBean.getProjectID(),
					workItemBean.getListTypeID(), workItemBean.getStateID(), person, workItemBean, null);
			}
		return LocalizeUtil.localizeDropDownList(dataSource, selectContext.getLocale());
	}
	
	/**
	 * Loads the create data source for state list
	 * The list should contain a single value, 
	 * consequently the initial entry can't be changed 
	 * even if it will be (accidentally) shown in the create issue screen	
	 * @param selectContext
	 * @return
	 */
	public List loadCreateDataSource(SelectContext selectContext) {
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		List<TStateBean> dataSource = StatusWorkflow.loadInitialStates(workItemBean.getProjectID(),
				workItemBean.getListTypeID(), workItemBean, selectContext.getPersonID(), null);
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
	public Object getMatcherDataSource(IMatcherValue matcherValue, MatcherDatasourceContext matcherDatasourceContext, Integer parameterCode) {
		List<TStateBean> datasource;
		Integer[] projectTypeIDs = ProjectTypesBL.getProjectTypeIDsForProjectIDs(matcherDatasourceContext.getProjectIDs());
		if (projectTypeIDs == null || projectTypeIDs.length==0) {
			datasource = (List)StatusBL.loadAll();
		} else {
			datasource =(List)StatusBL.loadAllowedByProjectTypesAndIssueTypes(projectTypeIDs, matcherDatasourceContext.getItemTypeIDs());
		}
		Locale locale = matcherDatasourceContext.getLocale();
		LocalizeUtil.localizeDropDownList(datasource, locale);
		if (matcherDatasourceContext.isWithParameter()) {
			datasource.add((TStateBean)getLabelBean(MatcherContext.PARAMETER, locale));
		}	
		if (matcherValue!=null) {
			if (matcherDatasourceContext.isInitValueIfNull()) {
				//from field expression
				Object value = matcherValue.getValue();
				if (value==null && datasource!=null && !datasource.isEmpty()) {
					matcherValue.setValue(new Integer[] {datasource.get(0).getObjectID()});
				}
			} else {
				//from upper select
				if (matcherDatasourceContext.isFirstLoad()) {
					//select the not closed states
					List<Integer> notClosedStates = new ArrayList<Integer>(); 
					for ( int i = 0; i < datasource.size(); i++) { 
						TStateBean stateBean = datasource.get(i); 
						Integer stateFlag = stateBean.getStateflag();
						//stateflag null for $Parameter 
						if (stateFlag!=null && TStateBean.STATEFLAGS.CLOSED!=stateFlag.intValue() ) {
							notClosedStates.add(stateBean.getObjectID());
						}
					}
					Integer[] selectedStates = GeneralUtils.createIntegerArrFromCollection(notClosedStates);
					matcherValue.setValue(selectedStates);
				}
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
		//should be verified against project types and workflow
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
		List<IBeanID> datasource = (List)StatusBL.loadAll(locale);
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
	public void loadFieldChangeDatasourceAndValue(WorkflowContext workflowContext,
			FieldChangeValue fieldChangeValue, 
			Integer parameterCode, TPersonBean personBean, Locale locale) {
		List<TStateBean> datasource = null;
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
			datasource = StatusBL.loadAll();
		} else {
			datasource = StatusBL.getByProjectTypeIssueTypeAssignments(projectTypeID, itemTypeID, (Integer)fieldChangeValue.getValue());
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
			TStateBean stateBean = new TStateBean();
			stateBean.setLabel(MatcherContext.getLocalizedParameter(locale));
			stateBean.setObjectID(optionID);
			return stateBean;
		}
		return StatusBL.loadByPrimaryKey(optionID);
	}
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.STATE;
	}
	
	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	public ISerializableLabelBean getNewSerializableLabelBean() {
		return new TStateBean();
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
		TStateBean stateBean = null;
		Integer primaryKey = LocalizeUtil.getDropDownPrimaryKeyFromLocalizedText(
				new TStateBean().getKeyPrefix(), label, locale);
		if (primaryKey!=null) {
			stateBean = LookupContainer.getStatusBean(primaryKey);
			if (stateBean!=null) {
				return primaryKey;
			}
		}
		List<TStateBean> stateBeans = StatusBL.loadByLabel(label); 
		if (stateBeans!=null && !stateBeans.isEmpty()) {
			stateBean =  stateBeans.get(0);
		}
		if (stateBean!=null) {
			return stateBean.getObjectID();
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
		List<TStateBean> stateBeansList=null;
		Integer projectID = serializableBeanAllowedContext.getProjectID();
		Integer issueTypeID = serializableBeanAllowedContext.getIssueTypeID();
		if (projectID==null || issueTypeID==null) {
			//normally we should return false here but then the error message would confuse the user
			//so we return true and by a next validating phase the issue the error message will be the real one: 
			//project or issueType is missing
			return true;
		}
		if (serializableBeanAllowedContext.isNew()) {
			stateBeansList = StatusWorkflow.loadInitialStates(projectID, issueTypeID,
					null, serializableBeanAllowedContext.getPersonID(), null);
		} else {
			TWorkItemBean workItemBeanOriginal = serializableBeanAllowedContext.getWorkItemBeanOriginal();
			if (workItemBeanOriginal!=null) {
				Integer originalStatus = workItemBeanOriginal.getStateID(); 
				if (originalStatus!=null && originalStatus.equals(objectID)) {
					return true;
				}
				stateBeansList = StatusWorkflow.loadStatesTo(projectID, issueTypeID,
						originalStatus, serializableBeanAllowedContext.getPersonID(), workItemBeanOriginal, null);
			}
		}
		if (stateBeansList!=null) {
			for (TStateBean stateBean : stateBeansList) {
				if (stateBean.getObjectID().equals(objectID)) {
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
	public List<ILabelBean> getDataSource(Integer fieldID) {
		return (List)StatusBL.loadAll();
	}
}
