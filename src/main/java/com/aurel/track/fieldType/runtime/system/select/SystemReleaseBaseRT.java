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

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.exchange.track.NameMappingBL;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.ReleaseBulkSetter;
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
import com.aurel.track.fieldType.runtime.matchers.design.ReleasePickerMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;

public abstract class SystemReleaseBaseRT extends SystemSelectBaseRT implements ITreeSelect {

	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_RELEASE;
	}
	
	/**
	 * Whether the datasource is tree or list
	 * @return
	 */
	public boolean isTree() {
		return true;
	}
	
	/**
	 * Loads the edit datasource for releases noticed
	 * @param selectContext
	 * @return
	 */
	public List loadEditDataSource(SelectContext selectContext){
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		Integer releaseID = workItemBean.getReleaseNoticedID();
		TPersonBean personBean = LookupContainer.getPersonBean(selectContext.getPersonID());
		Set<Integer> selectedReleases = null;
		if (releaseID!=null) {
			selectedReleases = new HashSet<Integer>();
			selectedReleases.add(releaseID);
		}
		return getProjectDataSource(workItemBean.getProjectID(), selectedReleases, personBean, selectContext.getLocale());
	}
	
	/**
	 * Loads the create datasource for releases noticed
	 * @param selectContext
	 * @return
	 */
	public List loadCreateDataSource(SelectContext selectContext){
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		TPersonBean personBean = LookupContainer.getPersonBean(selectContext.getPersonID());
		return getProjectDataSource(workItemBean.getProjectID(), null, personBean, selectContext.getLocale());
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
				TReleaseBean releaseBean = LookupContainer.getReleaseBean(selectedValue);
				if (releaseBean!=null) {
					labelContextMap = new HashMap<String, Object>();
					Date dueDate = releaseBean.getDueDate();
					if (dueDate!=null) {
						labelContextMap.put("dueDate", DateTimeUtils.getInstance().formatGUIDate(dueDate, locale));
					}
					String description = releaseBean.getDescription();
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
		List<TreeNode> datasource = new LinkedList<TreeNode>();
		Set<Integer> selectedReleaseIDsSet = new HashSet<Integer>();
		//boolean useChecked = !initValueIfNull;
		//TODO allow to select more than one entry in lower part?
		boolean useChecked = true;
		if (matcherValue!=null && matcherValue.getValue()!=null) {
			Integer[] selectedValues = (Integer[])matcherValue.getValue();
			selectedReleaseIDsSet = GeneralUtils.createSetFromIntegerArr(selectedValues);
		}
		Integer[] projects = matcherDatasourceContext.getProjectIDs();
		if (projects!=null && projects.length>0) {
			List<Integer> projectIDList = GeneralUtils.createListFromIntArr(projects);
			datasource = ReleaseBL.getReleaseNodesEager(matcherDatasourceContext.getPersonBean(),
					projectIDList, matcherDatasourceContext.isShowClosed(), true, true, true,
					selectedReleaseIDsSet, useChecked, false, matcherDatasourceContext.isWithParameter(), null, matcherDatasourceContext.getLocale());
		}
		return datasource;
	}
	
	public String getShowValue(Object value, WorkItemContext workItemContext,Integer fieldID){
		if(value!=null){
			TReleaseBean releaseBean = LookupContainer.getReleaseBean((Integer)value);
			if(releaseBean!=null){
				return releaseBean.getLabel();
			}
		}
		return "";
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
		return new ReleasePickerMatcherDT(fieldID);
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new ReleaseBulkSetter(fieldID);
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
		Map<Integer, List<TreeNode>> possibleValues = new HashMap<Integer, List<TreeNode>>();
		massOperationValue.setPossibleValues(possibleValues);
		Map<Integer, Integer> value = (Map<Integer, Integer>)massOperationValue.getValue();
		if (value == null) {
			value = new HashMap<Integer, Integer>();
			massOperationValue.setValue(value);
		}
		Map<Integer, String> valueLabelMap = new HashMap<Integer, String>();
		massOperationValue.setValueLabelMap(valueLabelMap);
		Integer[] projectIDs = massOperationContext.getInvolvedProjectsIDs();
		//retain only the settings for the actual involved projects 
		//(if project is selected for mass operation then only the values for that project should remain)
		value.keySet().retainAll(GeneralUtils.createSetFromIntegerArr(projectIDs));
		if (projectIDs!=null && projectIDs.length>0) {
			for (int i = 0; i < projectIDs.length; i++) {
				Integer projectID = projectIDs[i];
				TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
				if (projectBean!=null) {
					valueLabelMap.put(projectID, projectBean.getLabel());
				}
				List<TreeNode> datasource = getProjectDataSource(projectID, null, personBean, locale);
				if (datasource!=null) {
					//not the real datasource: get only the plain releases to initialize the picker with the first available release
					//the real datasource is coming form the release picker which knows how to get the datasource for itself					
					possibleValues.put(projectIDs[i], datasource);
					/*value.put(projectID, getBulkSelectValue(massOperationContext,
						massOperationValue.getFieldID(), null, 
						value.get(projectID), (List)datasource));*/
					value.put(projectID, getInitValue(massOperationContext,
							massOperationValue.getFieldID(), null, 
							value.get(projectID), (List)datasource));
					
				}
			}
		}
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
		List<TreeNode> releaseTree = null;
		Integer projectID = workflowContext.getProjectID();
		if (projectID!=null) {
			releaseTree = getProjectDataSource(projectID, null, personBean, locale);
			fieldChangeValue.setValue(getInitValue(null, fieldChangeValue.getFieldID(),
	        		null, (Integer)fieldChangeValue.getValue(), releaseTree));
		} else {
			releaseTree = getGlobalDataSource(personBean, locale);
		}
		fieldChangeValue.setPossibleValues(releaseTree);
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
		if (actualValue==null) {
			if (massOperationContext!=null) {
				TWorkItemBean firstWorkItemBean = massOperationContext.getFirstSelectedWorkItemBean();
				if (firstWorkItemBean!=null) {
					try {
						actualValue = (Integer)firstWorkItemBean.getAttribute(fieldID, paramaterCode);			
					} catch (Exception e) {
					}
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
	 * Gets the treeNode datasource for a project
	 * @param projectID
	 * @param selectedReleasesID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	protected abstract List<TreeNode> getProjectDataSource(Integer projectID, Set<Integer> selectedReleasesID, TPersonBean personBean, Locale locale);
	
	/**
	 * Gets the treeNode datasource for a project
	 * @param projectID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	protected abstract List<TreeNode> getGlobalDataSource(TPersonBean personBean, Locale locale);
	
	/**
	 * Get the ILabelBean by primary key 
	 * @return
	 */
	@Override
	public ILabelBean getLabelBean(Integer optionID, Locale locale) {
		if (optionID!=null && 
				optionID.equals(MatcherContext.PARAMETER)) {
			TReleaseBean releaseBean = new TReleaseBean();
			releaseBean.setLabel(MatcherContext.getLocalizedParameter(locale));
			releaseBean.setObjectID(optionID);
			return releaseBean;
		}
		return ReleaseBL.loadByPrimaryKey(optionID);
	}	
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.RELEASE;
	}
	
	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	public ISerializableLabelBean getNewSerializableLabelBean() {
		return new TReleaseBean();
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
		TReleaseBean releaseBean = ReleaseBL.loadByProjectAndLabel(projectID, label);
		if (releaseBean!=null) {
			return releaseBean.getObjectID();
		}
		return null;
	}
	
	/**
	 * Gets the datasource for all possible list entries 
	 * @param fieldID
	 * @return
	 */
	public List<ILabelBean> getDataSource(Integer fieldID) {
		return (List)ReleaseBL.loadAll();
	}
}
