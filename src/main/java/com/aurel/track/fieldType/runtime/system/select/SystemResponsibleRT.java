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
import com.aurel.track.admin.project.ProjectBL;
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

public class SystemResponsibleRT extends SystemPersonBaseRT{
	
	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		//return SystemFields.INTEGER_RESPONSIBLE;
		return SystemFields.INTEGER_PERSON;
	}
	
	/**
	 * Loads the settings for a specific field like default value(s),
	 * datasources (for lists) etc. by editing an existing issue
	 * It might differ from loadCreateSettings because it could be need 
	 * to add existing entries in the list (also when right already revoked)
	 * @param selectContext
	 * @return
	 */
	public List loadEditDataSource(SelectContext selectContext) {
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		boolean accessLevelFlag = workItemBean.isAccessLevelFlag();
		Integer loggedInPersonID = selectContext.getPersonID();
		Integer actualResponsiblePersonID = workItemBean.getResponsibleID();
		if (accessLevelFlag) {
			//responsible list contains only the originator
			List<ILabelBean> responsibleList = new LinkedList<ILabelBean>();
			TPersonBean personBean = LookupContainer.getPersonBean(loggedInPersonID);
			if (personBean!=null) {
				responsibleList.add(personBean);
			}
			return responsibleList;
		} else {
			TProjectBean projectBean = LookupContainer.getProjectBean(workItemBean.getProjectID());
			if (projectBean!=null && projectBean.isPrivate()) {
				List<ILabelBean> responsibleList = new LinkedList<ILabelBean>();
				TPersonBean personBean = LookupContainer.getPersonBean(loggedInPersonID);
				if (personBean!=null) {
					responsibleList.add(personBean);
				}
				if (actualResponsiblePersonID!=null && !actualResponsiblePersonID.equals(loggedInPersonID)) {
					TPersonBean actualResponsiblePersonBean = LookupContainer.getPersonBean(actualResponsiblePersonID);
					if (actualResponsiblePersonBean!=null) {
						responsibleList.add(actualResponsiblePersonBean);
					}
				}
				return responsibleList;
			}
			//Limit the responsibles to those that in the new status have the 
			//right to leave that status according to the workflow
			return (List)PersonBL.loadResponsiblesByProjectAndIssueType(
					workItemBean.getProjectID(), workItemBean.getListTypeID(), actualResponsiblePersonID);
		}	
	}
	
	/**
	 * Loads the settings for a specific field like default value(s),
	 * datasources (for lists) etc.
	 * @param selectContext
	 * @return
	 */
	public List loadCreateDataSource(SelectContext selectContext) {
		List dataSource;
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		Integer loggedInPersonID = selectContext.getPersonID();
		boolean accessLevelFlag = workItemBean.isAccessLevelFlag();
		Integer projectDefaultResponsible = null;
		if (accessLevelFlag) {
			//responsible list contains only the originator
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
				//the workItemBean.getResponsibleID() may be already set in FieldsManagerRT.setDefaultValues() 
				//i.e. either by setDefaultAttribute() or by inheriting from the last workItemBean, 
				//If it was set by setDefaultAttribute() it should be sure that he (the default responsible for the project) 
				//is dynamically included in the list even if he has no explicit responsible right for the project/issueType
				//but if it was set as a result of the previous workItem created by the user
				//and he was revoked the responsible right then he should not be included
				if (workItemBean.getResponsibleID()!=null) {
					//the responsible was set in setDefaultValues():
					//either by setDefaultAttribute or by inheriting from the last workItemBean 
					if (projectBean!=null) {
						projectDefaultResponsible = projectBean.getDefaultOwnerID();
					}				
					if (projectDefaultResponsible!=null && projectDefaultResponsible.equals(workItemBean.getResponsibleID())) {
						//set by setDefaultAttribute() include also the project default responsible in the dataSource
						dataSource = PersonBL.loadResponsiblesByProjectAndIssueType(
								workItemBean.getProjectID(), workItemBean.getListTypeID(), workItemBean.getResponsibleID());
					} else {
						//set according to the previous workItem created by the user, do not include it explicitly
						dataSource = PersonBL.loadResponsiblesByProjectAndIssueType(
								workItemBean.getProjectID(), workItemBean.getListTypeID());
					}
				} else {
					//responsible was not set at all in setDefaultValues()
					dataSource = PersonBL.loadResponsiblesByProjectAndIssueType(
							workItemBean.getProjectID(), workItemBean.getListTypeID());
				}
			}
		}
		if (dataSource!=null && !dataSource.isEmpty()) {
			if (workItemBean.getResponsibleID()==null) {
				//responsible was not set at all in setDefaultValues(): 
				//set explicitly to the first entry from the list
				//Reason: if the tab where this field is present is not selected before saving the issue
				//this field would not be submitted by tab change or save 
				//and it would result an issue without responsible
				TPersonBean personBean = (TPersonBean)dataSource.get(0);
				workItemBean.setResponsibleID(personBean.getObjectID());
			} else {
				//verify whether the value set in setDefaultValues() is contained in the dataSource list
				Iterator<ILabelBean> iterator = dataSource.iterator();
				boolean found = false; 
				boolean foundDefaultResponsible = false;
				while (iterator.hasNext()) {
					TPersonBean personBean = (TPersonBean) iterator.next();
					if (personBean.getObjectID().equals(workItemBean.getResponsibleID())) {
						found=true;
						break;
					}
					if (projectDefaultResponsible!=null && projectDefaultResponsible.equals(personBean.getObjectID())) {
						foundDefaultResponsible = true;
					}
				}
				if (!found) {
					//the value set in setDefaultValues() is not found in the resulting list
					if (foundDefaultResponsible) {
						//overwrite the inherited value from the previous workItem to the project default value
						workItemBean.setResponsibleID(projectDefaultResponsible);
					} else {
						//set the first from the list
						TPersonBean personBean = (TPersonBean)dataSource.get(0);
						workItemBean.setResponsibleID(personBean.getObjectID());
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
			workItemBean.setResponsibleID(projectBean.getDefaultOwnerID());
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
		List<ILabelBean> responsiblesList = new LinkedList<ILabelBean>();
		Integer[] projects = matcherDatasourceContext.getProjectIDs();
		Integer[] ancestorProjects = matcherDatasourceContext.getAncestorProjectIDs();
		if (projects!=null && projects.length>0) {
			//get the persons who have responsible role in any selected project
			int[] arrRights = new int[] { AccessFlagIndexes.RESPONSIBLE, AccessFlagIndexes.PROJECTADMIN };
			Set<Integer> responsiblesByRoleSet = AccessBeans.getPersonSetByProjectsRights(ancestorProjects, arrRights);
			List<TPersonBean> responsiblesByRole = PersonBL.getDirectAndIndirectPersonsAndGroups(
					GeneralUtils.createIntegerListFromCollection(responsiblesByRoleSet), true, true, null);
			//get the persons who are responsibles in existing issues: they are not necessary 
			//a subset of the persons with responsible role because it could happen that somebody
			//is a responsible on an issue but his responsible role was revoked for the project 
			List<TPersonBean> responsiblesInIssues = PersonBL.loadUsedResonsiblesByProjects(matcherDatasourceContext.getPersonBean().getObjectID(), projects);
			Set<TPersonBean> responsibles = new TreeSet<TPersonBean>();
			responsibles.addAll(responsiblesByRole);
			responsibles.addAll(responsiblesInIssues);
			responsiblesList.addAll(GeneralUtils.createListFromSet(responsibles));
		}
		//add the symbolic user as the first entry in the list
		Locale locale = matcherDatasourceContext.getLocale();
		responsiblesList.add(0, getLabelBean(MatcherContext.LOGGED_USER_SYMBOLIC, locale));
		if (matcherDatasourceContext.isWithParameter()) {
			responsiblesList.add(getLabelBean(MatcherContext.PARAMETER, locale));
		}						
		if (matcherDatasourceContext.isInitValueIfNull() && matcherValue!=null) {
			Object value = matcherValue.getValue();
			if (value==null) {
				matcherValue.setValue(new Integer[] {responsiblesList.get(0).getObjectID()});
			}
		}
		return responsiblesList;
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		IBulkSetter bulkSetter = super.getBulkSetterDT(fieldID);
		//although the possible responsibles are responsibles in all selected projects
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
		int[] responsibleRights = new int[] { AccessFlagIndexes.RESPONSIBLE, AccessFlagIndexes.PROJECTADMIN };
		Set<Integer> responsiblesByRoleSet = AccessBeans.getPersonSetWithRightInAllOfTheProjects(
				massOperationContext.getInvolvedProjectsIDs(), responsibleRights);
		List responsiblesByRole = PersonBL.getDirectAndIndirectPersonsAndGroups(
				GeneralUtils.createIntegerListFromCollection(responsiblesByRoleSet), true, true, null);
		massOperationValue.setPossibleValues(responsiblesByRole);
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
		List<TPersonBean> responsibleList = null;
		Integer projectID = workflowContext.getProjectID();
		if (projectID==null) {
			responsibleList = PersonBL.loadActivePersonsAndGroups();
		} else {
			int[] responsibleRights = new int[] { AccessFlagIndexes.RESPONSIBLE, AccessFlagIndexes.PROJECTADMIN};	
			Set<Integer> responsiblesByRoleSet = AccessBeans.getPersonSetWithRightInAllOfTheProjects(
					new Integer[] {projectID}, responsibleRights);
			responsibleList = PersonBL.getDirectAndIndirectPersonsAndGroups(
					GeneralUtils.createIntegerListFromCollection(responsiblesByRoleSet), true, true, (Integer)fieldChangeValue.getValue());
		}
		fieldChangeValue.setPossibleValues(responsibleList);
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
			Integer responsible = workItemBeanOriginal.getResponsibleID(); 
			if (responsible!=null && responsible.equals(objectID)) {
				return true;
			}
		}
		return AccessBeans.hasPersonRightInProjectForIssueType(
				objectID,
				serializableBeanAllowedContext.getProjectID(),
				serializableBeanAllowedContext.getIssueTypeID(), 
				AccessFlagIndexes.RESPONSIBLE, false, false);					
	}
}
