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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
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
import com.aurel.track.util.GeneralUtils;

public class SystemManagerRT extends SystemPersonBaseRT{
		
	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_PERSON;
	}
	
	/**
	 * Loads the edit datasource for managers
	 * @param selectContext
	 * @return
	 */
	public List loadEditDataSource(SelectContext selectContext){
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		boolean accessLevelFlag = workItemBean.isAccessLevelFlag();
		Integer loggedInPersonID = selectContext.getPersonID();
		Integer actualManagerPersonID = workItemBean.getOwnerID();
		if (accessLevelFlag) {
			//manager list contains only the originator
			List<ILabelBean> managerList = new LinkedList<ILabelBean>();
			TPersonBean personBean = LookupContainer.getPersonBean(loggedInPersonID);
			if (personBean!=null) {
				managerList.add(personBean);
			}
			return managerList; 
		} else {
			TProjectBean projectBean = LookupContainer.getProjectBean(workItemBean.getProjectID());
			if (projectBean!=null && projectBean.isPrivate()) {
				List<ILabelBean> managerList = new LinkedList<ILabelBean>();
				TPersonBean personBean = LookupContainer.getPersonBean(loggedInPersonID);
				if (personBean!=null) {
					managerList.add(personBean);
				}
				if (actualManagerPersonID!=null && !actualManagerPersonID.equals(loggedInPersonID)) {
					TPersonBean actualManagerPersonBean = LookupContainer.getPersonBean(actualManagerPersonID);
					if (actualManagerPersonBean!=null) {
						managerList.add(actualManagerPersonBean);
					}
				}
				return managerList;
			}
			return (List)PersonBL.loadManagersByProjectAndIssueType(workItemBean.getProjectID(), 
					workItemBean.getListTypeID(), actualManagerPersonID);
		}
	}
	
	/**
	 * Loads the create datasource for managers	
	 * @param selectContext
	 * @return
	 */
	public List loadCreateDataSource(SelectContext selectContext) {
		List dataSource = null;
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		boolean accessLevelFlag = workItemBean.isAccessLevelFlag();
		Integer projectDefaultManager = null;
		Integer loggedInPersonID = selectContext.getPersonID();
		if (accessLevelFlag) {
			//private item: manager list contains only the logged in user
			dataSource = new LinkedList<ILabelBean>();
			TPersonBean personBean = LookupContainer.getPersonBean(loggedInPersonID);
			if (personBean!=null) {
				dataSource.add(personBean);
			}
		} else {
			TProjectBean projectBean = LookupContainer.getProjectBean(workItemBean.getProjectID());
			if (projectBean!=null && projectBean.isPrivate()) {
				//item in private project: manager list contains only the logged in user
				dataSource = new LinkedList<ILabelBean>();
				TPersonBean personBean = LookupContainer.getPersonBean(loggedInPersonID);
				if (personBean!=null) {
					dataSource.add(personBean);
				}
			} else {
				//the workItemBean.getOwnerID() may be already set in FieldsManagerRT.setDefaultValues() 
				//i.e. either by setDefaultAttribute() or by inheriting from the last workItemBean, 
				//If it was set by setDefaultAttribute() it should be sure that he (the default manager for the project) 
				//is dynamically included in the list even if he has no explicit manager right for the project/issueType
				//but if it was set as a result of the previous workItem created by the user
				//and he was revoked the manager right then he should not be included			
				if (workItemBean.getOwnerID()!=null) {
					//the manager was set in setDefaultValues():
					//either by setDefaultAttribute or by inheriting from the last workItemBean 
					//TProjectBean projectBean = LookupContainer.getProjectBean(workItemBean.getProjectID());
					if (projectBean!=null) {
						projectDefaultManager = projectBean.getDefaultManagerID();
					}
					if (projectDefaultManager!=null && projectDefaultManager.equals(workItemBean.getOwnerID())) {
						//set by setDefaultAttribute() include also the project default manager in the dataSource
						dataSource = PersonBL.loadManagersByProjectAndIssueType(
								workItemBean.getProjectID(), workItemBean.getListTypeID(), workItemBean.getOwnerID());
					} else {
						//set according to the previous workItem created by the user, do not include it explicitly
						dataSource = PersonBL.loadManagersByProjectAndIssueType(
								workItemBean.getProjectID(), workItemBean.getListTypeID());
					}
				} else {
					//manager was not set at all in setDefaultValues()
					dataSource = PersonBL.loadManagersByProjectAndIssueType(
							workItemBean.getProjectID(), workItemBean.getListTypeID());
				}
			}
		}
		if (dataSource!=null && !dataSource.isEmpty()) {
			if (workItemBean.getOwnerID()==null) {
				//manager was not set at all in setDefaultValues(): 
				//set explicitly to the first entry from the list
				//Reason: if the tab where this field is present is not selected before saving the issue
				//this field would not be submitted by tab change or save 
				//and it would result an issue without manager
				TPersonBean personBean = (TPersonBean)dataSource.get(0);
				workItemBean.setOwnerID(personBean.getObjectID());
			} else {
				//verify whether the value set in setDefaultValues() is contained in the dataSource list
				Iterator<ILabelBean> iterator = dataSource.iterator();
				boolean found = false; 
				boolean foundDefaultManager = false;
				while (iterator.hasNext()) {
					TPersonBean personBean = (TPersonBean) iterator.next();
					if (personBean.getObjectID().equals(workItemBean.getOwnerID())) {
						found=true;
						break;
					}
					if (projectDefaultManager!=null && projectDefaultManager.equals(personBean.getObjectID())) {
						foundDefaultManager = true;
					}
				}
				if (!found) {
					//the value set in setDefaultValues() is not found in the resulting list
					if (foundDefaultManager) {
						//overwrite the inherited value from the previous workItem to the project default value
						workItemBean.setOwnerID(projectDefaultManager);
					} else {
						//set the first from the list
						TPersonBean personBean = (TPersonBean)dataSource.get(0);
						workItemBean.setOwnerID(personBean.getObjectID());
					}
				}
			}
		}	
		return dataSource;
	}
	
	/**
	 * Sets the default value in the workItemBean for a field (and parameterCode)
	 * according to the validConfig and fieldSettings
	 * Here only the selected project is important
	 * @param fieldID 
	 * @param parameterCode 
	 * @param validConfig
	 * @param fieldSettings
	 * @param workItemBean
	 */
	@Override
	public void setDefaultAttribute(Integer fieldID, Integer parameterCode, 
			Integer validConfig, Map<String, Object> fieldSettings, TWorkItemBean workItemBean) {
		TProjectBean projectBean = LookupContainer.getProjectBean(workItemBean.getProjectID());
		if (projectBean!=null) {
			workItemBean.setOwnerID(projectBean.getDefaultManagerID());
		}
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
		List<ILabelBean> managersList = new LinkedList<ILabelBean>();
		Integer[] projects = matcherDatasourceContext.getProjectIDs();
		if (projects!=null && projects.length>0) {
			//get the persons who have manager role in any selected project 
			int[] arrRights = new int[] { AccessFlagIndexes.MANAGER, AccessFlagIndexes.PROJECTADMIN };
			Set<Integer> managersByRoleSet = AccessBeans.getPersonSetByProjectsRights(matcherDatasourceContext.getAncestorProjectIDs(), arrRights);
			List<TPersonBean> managersByRole = PersonBL.getDirectAndIndirectPersons(GeneralUtils.createIntegerListFromCollection(managersByRoleSet), true, true, null);
			//get the persons who are managers in existing issues: they are not necessary 
			//a subset of the persons with managers role because it could happen that somebody
			//is a manager on an issue but his manager role was revoked for the project 
			List<TPersonBean> managersInIssues = PersonBL.loadUsedManagersByProjects(matcherDatasourceContext.getPersonBean().getObjectID(), projects);
			Set<TPersonBean> managers = new TreeSet<TPersonBean>();
			managers.addAll(managersByRole);
			managers.addAll(managersInIssues);
			managersList.addAll(GeneralUtils.createListFromSet(managers));
		}
		//add the symbolic user as the first entry in the list
		Locale locale = matcherDatasourceContext.getLocale();
		managersList.add(0, new SystemManagerRT().getLabelBean(MatcherContext.LOGGED_USER_SYMBOLIC, locale));
		if (matcherDatasourceContext.isWithParameter()) {
			managersList.add(new SystemManagerRT().getLabelBean(MatcherContext.PARAMETER, locale));
		}
		if (matcherDatasourceContext.isInitValueIfNull() && matcherValue!=null) {
			Object value = matcherValue.getValue();
			if (value==null) {
				matcherValue.setValue(new Integer[] {managersList.get(0).getObjectID()});
			}
		}
		return managersList;
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		IBulkSetter bulkSetter = super.getBulkSetterDT(fieldID);
		//although the possible managers are managers in all selected projects
		//pro issue type they are not verified
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
		int[] managerRights = new int[] { AccessFlagIndexes.MANAGER, AccessFlagIndexes.PROJECTADMIN };		
		Set<Integer> managersByRoleSet = AccessBeans.getPersonSetWithRightInAllOfTheProjects(
				massOperationContext.getInvolvedProjectsIDs(), managerRights);
		List managersByRole = PersonBL.getDirectAndIndirectPersons(
				GeneralUtils.createIntegerListFromCollection(managersByRoleSet), true, true, null);
		massOperationValue.setPossibleValues(managersByRole);
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
		List<TPersonBean> managersList = null;
		Integer projectID = workflowContext.getProjectID();
		if (projectID==null) {
			managersList = PersonBL.loadActivePersons();
		} else {
			int[] managerRights = new int[] { AccessFlagIndexes.MANAGER, AccessFlagIndexes.PROJECTADMIN};	
			Set<Integer> managersByRoleSet = AccessBeans.getPersonSetWithRightInAllOfTheProjects(
					new Integer[] {projectID}, managerRights);
			managersList = PersonBL.getDirectAndIndirectPersons(
					GeneralUtils.createIntegerListFromCollection(managersByRoleSet), true, true, (Integer)fieldChangeValue.getValue());
		}
		fieldChangeValue.setPossibleValues(managersList);
		fieldChangeValue.setValue(getBulkSelectValue(null,
				fieldChangeValue.getFieldID(), null, 
				(Integer)fieldChangeValue.getValue(), 
				(List<IBeanID>)fieldChangeValue.getPossibleValues()));
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
			Integer manager = workItemBeanOriginal.getOwnerID(); 
			if (manager!=null && manager.equals(objectID)) {
				return true;
			}
		}
		return AccessBeans.hasPersonRightInProjectForIssueType(
				objectID, 
				serializableBeanAllowedContext.getProjectID(), 
				serializableBeanAllowedContext.getIssueTypeID(), 
				AccessFlagIndexes.MANAGER, false, false);					
	}
}
