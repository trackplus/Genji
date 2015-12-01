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

package com.aurel.track.admin.customize.category.filter.execute.loadItems;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.beans.TRoleFieldBean.ACCESSFLAG;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.fieldType.runtime.callbackInterfaces.IRemoteFiltering;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ComputedValuesLoaderBL;
import com.aurel.track.report.execute.ConsultedInformedLoaderBL;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ShowableWorkItem;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
/**
 * This class offers a lot of methods to get a specific set of issues, based on given criteria. 
 * It offers utility methods to construct Torque <code>Criteria</code> objects that are helpful
 * when filtering for issues. 
 *
 */
public class ReportBeanLoader {
	private static final Logger LOGGER = LogManager.getLogger(ReportBeanLoader.class);
	
	/**
	 * Prepares an issue ready for display from a standard <code>TWorkItemBean</code>. The
	 * <code>TWorkItemBean</code> contains references to other entities like priorities and
	 * statuses, rather then the strings like "high" or "hoch". This method replaces the
	 * references by proper localized strings and returns a bean that can directly be used
	 * for display purposes. Fields that are not supported are set to "Unavailable".
	 * @param workItemBean the bean to be prepared for display 
	 * @param personID in case this person must not see certain fields
	 * @param locale the locale to be used for displayed strings
	 * @return
	 */
	public static ShowableWorkItem getReportBean(TWorkItemBean workItemBean, Integer personID, Locale locale) {
		ShowableWorkItem showableWorkItem = new ShowableWorkItem();
		showableWorkItem.setWorkItemBean(workItemBean);		
		Map<String, Object> attributeValueBeanMap = ItemBL.loadWorkItemCustomAttributes(workItemBean);
		Map<Integer, Integer> restrictedFieldsMap = AccessBeans.getFieldRestrictions(
				personID, workItemBean.getProjectID(), workItemBean.getListTypeID(), true);
		String unavailable = LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.unavailable", locale);
		List<TFieldBean> systemFieldBeans = FieldBL.loadSystem();
		Map<Integer, String> showValues = new HashMap<Integer, String>();
		Map<Integer, Comparable> sortOrderValuesMap=new HashMap<Integer, Comparable>();
		List<TWorkItemBean> workItemBeanList=new ArrayList<TWorkItemBean>();
		workItemBeanList.add(workItemBean);
		//state flag
		TStateBean stateBean = LookupContainer.getStatusBean(workItemBean.getStateID(), locale);
		showableWorkItem.setStateFlag(stateBean.getStateflag());
		//system fields
		for (TFieldBean fieldBean : systemFieldBeans) {
			Integer fieldID = fieldBean.getObjectID();
			Integer fieldRestriction = (Integer)restrictedFieldsMap.get(fieldID);
			if (fieldRestriction!= null && fieldRestriction.intValue()==TRoleFieldBean.ACCESSFLAG.NOACCESS) {
				showValues.put(fieldID, unavailable);
			} else {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				Object attributeValue = workItemBean.getAttribute(fieldID);
				showValues.put(fieldID, fieldTypeRT.getShowValue(attributeValue, locale));
			}
		}
		//custom fields
		if (attributeValueBeanMap!=null) {
			Iterator<String> itrAttributeBeans = attributeValueBeanMap.keySet().iterator();
			while (itrAttributeBeans.hasNext()) {
				String mergeKey = itrAttributeBeans.next();
				Integer[] keyComponents = MergeUtil.getParts(mergeKey);
				Integer fieldID = keyComponents[0];
				Integer fieldRestriction = (Integer)restrictedFieldsMap.get(fieldID);
				if (fieldRestriction!= null && fieldRestriction.intValue()==TRoleFieldBean.ACCESSFLAG.NOACCESS) {
					//no read access for this field
					showValues.put(fieldID, unavailable);
				} else {
					Integer parameterCode = keyComponents[1];
					if (parameterCode==null || new Integer(1).equals(parameterCode)) {
						//load the not composites and the composite if it is the first part
						//(in order to avoid loading the composite again and again for each of his part)
						IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						Object attributeValue = workItemBean.getAttribute(fieldID);
						showValues.put(fieldID, fieldTypeRT.getShowValue(attributeValue, locale));
					}
				}
			}
		}
		showableWorkItem.setShowValuesMap(showValues);
		showableWorkItem.setSortOrderValuesMap(sortOrderValuesMap);
		return showableWorkItem;
	}
	
	/**
	 * Populates the list of reporteans
	 * @param workItemBeanList
	 * @param attributeValueBeanList
	 * @param PERSON_BEAN
	 * @param locale
	 * @param notifyBeansList
	 * @return
	 */
	public static List<ReportBean> populateReportBeans(List<TWorkItemBean> workItemBeanList,
				List<TAttributeValueBean> attributeValueBeanList, Integer personID,
			Locale locale, List<TNotifyBean> notifyBeansList,
			List<TComputedValuesBean> myExpenseList, List<TComputedValuesBean> totalExpenseList,
			List<TComputedValuesBean> budgetAndPlanList, List<TActualEstimatedBudgetBean> remainingPlanList,
			List<TAttachmentBean> attachmentList, List<TWorkItemLinkBean> itemLinkList, Set<Integer> parentIDsSet) {
		TPersonBean personBean = LookupContainer.getPersonBean(personID);
		LocalLookupContainer localLookupContainer = ItemBL.getItemHierarchyContainer(workItemBeanList);
		List<ReportBean> reportBeanList = new ArrayList<ReportBean>();
		Date start = null;
		if (LOGGER.isDebugEnabled()) {
			start = new Date();
		}
		Integer daysLead = null;
		if (personBean!=null) {
			daysLead = personBean.getEmailLead();
		}
		Set<Integer> customOptionIDs = new HashSet<Integer>();
		Map<Integer, Set<Integer>> externalOptionsSetByField = new HashMap<Integer, Set<Integer>>();
		Map<Integer, Map<String, Object>> workItemsAttributesMap =
				AttributeValueBL.prepareAttributeValueMapForWorkItems(attributeValueBeanList, customOptionIDs, externalOptionsSetByField);
		//gets the custom option lookups
		List<TOptionBean> optionBeans = OptionBL.loadByKeys(GeneralUtils.createIntegerArrFromCollection(customOptionIDs));
		localLookupContainer.setCustomOptionsMap(GeneralUtils.createMapFromList(optionBeans));
		//gets the external option lookups
		if (externalOptionsSetByField!=null && !externalOptionsSetByField.isEmpty()) {
			Map<Integer, Map<Integer, IntegerStringBean>> externalOptionsMap = new HashMap<Integer, Map<Integer,IntegerStringBean>>();
			for (Integer fieldID : externalOptionsSetByField.keySet()) {
				Set<Integer> externalOptionsForField = externalOptionsSetByField.get(fieldID);
				if (externalOptionsForField!=null) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					if (fieldTypeRT.getValueType()==ValueType.EXTERNALID) {
						IRemoteFiltering remoteFiltering = (IRemoteFiltering)fieldTypeRT;
						Map<Integer, IntegerStringBean> externalOptionsList = remoteFiltering.getExternalOptionsMap(
								GeneralUtils.createIntegerListFromCollection(externalOptionsForField));
						externalOptionsMap.put(fieldID, externalOptionsList);
					}
				}
			}
			localLookupContainer.setExternalOptionsMap(externalOptionsMap);
		}
		Map<Integer, Map<String, Set<Integer>>> consInfIDsMap = ConsultedInformedLoaderBL.getConsInfMap(notifyBeansList);
		Map<Integer, TProjectTypeBean> projectTypeMap = GeneralUtils.createMapFromList(ProjectTypesBL.loadAll());
		/**
		 * Gets the project to issueTypes map from the item result set 
		 */
		Map<Integer, Set<Integer>> projectToIssueTypesMap = AccessBeans.getProjectToIssueTypesMap(workItemBeanList);
		Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions =
				AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, null, false);
		Map<Integer, ProjectAccountingTO> projectAccountingTOMap = new HashMap<Integer, ProjectAccountingTO>();
		Map<Integer, TWorkItemBean> workItemBeansMap = GeneralUtils.createMapFromList(workItemBeanList);
		//my expense time show values and sortOrder values
		Map<Integer, String> myExpenseTimeShowValuesMap = new HashMap<Integer, String>();
		Map<Integer, String> myExpenseTimeShowISOValuesMap = new HashMap<Integer, String>();
		Map<Integer, Double> myExpenseTimeSortOrderValuesMap = new HashMap<Integer, Double>();
		//my expense cost show values and sortOrder values
		Map<Integer, String> myExpenseCostShowValuesMap = new HashMap<Integer, String>();
		Map<Integer, String> myExpenseCostShowISOValuesMap = new HashMap<Integer, String>();
		Map<Integer, Double> myExpenseCostSortOrderValuesMap = new HashMap<Integer, Double>();
		if (myExpenseList!=null) {
			List<TComputedValuesBean> myTimes = new LinkedList<TComputedValuesBean>();
			List<TComputedValuesBean> myCosts = new LinkedList<TComputedValuesBean>();
			for (TComputedValuesBean computedValuesBean : myExpenseList) {
				Integer effortType = computedValuesBean.getEffortType();
				if (effortType!=null && effortType.intValue()==TComputedValuesBean.EFFORTTYPE.TIME) {
					myTimes.add(computedValuesBean);
				} else {
					myCosts.add(computedValuesBean);
				}
			}
			ComputedValuesLoaderBL.loadComputedValueMaps(myTimes, workItemBeansMap, 
					fieldRestrictions, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES, projectAccountingTOMap, locale,
					myExpenseTimeShowValuesMap, myExpenseTimeShowISOValuesMap, myExpenseTimeSortOrderValuesMap);
			ComputedValuesLoaderBL.loadComputedValueMaps(myCosts, workItemBeansMap, 
					fieldRestrictions, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES, projectAccountingTOMap, locale,
					myExpenseCostShowValuesMap, myExpenseCostShowISOValuesMap, myExpenseCostSortOrderValuesMap);
			
		}
		//total expense time show values and sortOrder values
		Map<Integer, String> totalExpenseTimeShowValuesMap = new HashMap<Integer, String>(); 
		Map<Integer, String> totalExpenseTimeShowISOValuesMap = new HashMap<Integer, String>();
		Map<Integer, Double> totalExpenseTimeSortOrderValuesMap = new HashMap<Integer, Double>();
		//total expense cost show values and sortOrder values
		Map<Integer, String> totalExpenseCostShowValuesMap = new HashMap<Integer, String>();
		Map<Integer, String> totalExpenseCostShowISOValuesMap = new HashMap<Integer, String>();
		Map<Integer, Double> totalExpenseCostSortOrderValuesMap = new HashMap<Integer, Double>();
		if (totalExpenseList!=null) {
			List<TComputedValuesBean> totalTimes = new LinkedList<TComputedValuesBean>();
			List<TComputedValuesBean> totalCosts = new LinkedList<TComputedValuesBean>();
			for (TComputedValuesBean computedValuesBean : totalExpenseList) {
				Integer effortType = computedValuesBean.getEffortType();
				if (effortType!=null && effortType.intValue()==TComputedValuesBean.EFFORTTYPE.TIME) {
					totalTimes.add(computedValuesBean);
				} else {
					totalCosts.add(computedValuesBean);
				}
			}
			ComputedValuesLoaderBL.loadComputedValueMaps(totalTimes, workItemBeansMap, 
					fieldRestrictions, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES, projectAccountingTOMap, locale,
					totalExpenseTimeShowValuesMap, totalExpenseTimeShowISOValuesMap, totalExpenseTimeSortOrderValuesMap);
			ComputedValuesLoaderBL.loadComputedValueMaps(totalCosts, workItemBeansMap, 
					fieldRestrictions, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES, projectAccountingTOMap, locale,
					totalExpenseCostShowValuesMap, totalExpenseCostShowISOValuesMap, totalExpenseCostSortOrderValuesMap);
		}
		//total budget time show values and sortOrder values
		boolean budgetActive = ApplicationBean.getInstance().getBudgetActive();
		Map<Integer, String> budgetTimeShowValuesMap = new HashMap<Integer, String>(); 
		Map<Integer, String> budgetTimeShowISOValuesMap = new HashMap<Integer, String>();
		Map<Integer, Double> budgetTimeSortOrderValuesMap = new HashMap<Integer, Double>();
		//total budget cost show values and sortOrder values
		Map<Integer, String> budgetCostShowValuesMap = new HashMap<Integer, String>();
		Map<Integer, String> budgetCostShowISOValuesMap = new HashMap<Integer, String>();
		Map<Integer, Double> budgetCostSortOrderValuesMap = new HashMap<Integer, Double>();
		//total plan time show values and sortOrder values
		Map<Integer, String> totalPlanTimeShowValuesMap = new HashMap<Integer, String>(); 
		Map<Integer, String> totalPlanTimeShowISOValuesMap = new HashMap<Integer, String>();
		Map<Integer, Double> totalPlanTimeSortOrderValuesMap = new HashMap<Integer, Double>();
		//total plan cost show values and sortOrder values
		Map<Integer, String> totalPlanCostShowValuesMap = new HashMap<Integer, String>();
		Map<Integer, String> totalPlanCostShowISOValuesMap = new HashMap<Integer, String>();
		Map<Integer, Double> totalPlanCostSortOrderValuesMap = new HashMap<Integer, Double>();
		if (budgetAndPlanList!=null) {
			List<TComputedValuesBean> timePlan = new LinkedList<TComputedValuesBean>();
			List<TComputedValuesBean> costPlan = new LinkedList<TComputedValuesBean>();
			List<TComputedValuesBean> timeBudget = new LinkedList<TComputedValuesBean>();
			List<TComputedValuesBean> costBudget = new LinkedList<TComputedValuesBean>();
			for (TComputedValuesBean computedValuesBean : budgetAndPlanList) {
				Integer effortType = computedValuesBean.getEffortType();
				Integer computedValueType = computedValuesBean.getComputedValueType();
				if (effortType!=null && computedValueType!=null) {
					if (effortType.intValue()==TComputedValuesBean.EFFORTTYPE.TIME) {
						if (computedValueType.intValue()==TComputedValuesBean.COMPUTEDVALUETYPE.PLAN) {
							timePlan.add(computedValuesBean);
						} else {
							timeBudget.add(computedValuesBean);
						}
					} else {
						if (computedValueType.intValue()==TComputedValuesBean.COMPUTEDVALUETYPE.PLAN) {
							costPlan.add(computedValuesBean);
						} else {
							costBudget.add(computedValuesBean);
						}
					}
				}
			}
			ComputedValuesLoaderBL.loadComputedValueMaps(timePlan, workItemBeansMap, 
					fieldRestrictions, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN, projectAccountingTOMap, locale,
					totalPlanTimeShowValuesMap, totalPlanTimeShowISOValuesMap, totalPlanTimeSortOrderValuesMap);
			ComputedValuesLoaderBL.loadComputedValueMaps(costPlan, workItemBeansMap, 
					fieldRestrictions, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN, projectAccountingTOMap, locale,
					totalPlanCostShowValuesMap, totalPlanCostShowISOValuesMap, totalPlanCostSortOrderValuesMap);
			ComputedValuesLoaderBL.loadComputedValueMaps(timeBudget, workItemBeansMap, 
					fieldRestrictions, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET, projectAccountingTOMap, locale,
					budgetTimeShowValuesMap, budgetTimeShowISOValuesMap, budgetTimeSortOrderValuesMap);
			ComputedValuesLoaderBL.loadComputedValueMaps(costBudget, workItemBeansMap, 
					fieldRestrictions, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET, projectAccountingTOMap, locale,
					budgetCostShowValuesMap, budgetCostShowISOValuesMap, budgetCostSortOrderValuesMap);
		}
		
		//remaining budget show values and sortOrder values
		Map<Integer, String> remainingBudgetTimeShowValuesMap = new HashMap<Integer, String>();
		Map<Integer, String> remainingBudgetTimeShowISOValuesMap = new HashMap<Integer, String>();
		Map<Integer, Double> remainingBudgetTimeSortOrderValuesMap = new HashMap<Integer, Double>();
		Map<Integer, String> remainingBudgetCostShowValuesMap = new HashMap<Integer, String>();
		Map<Integer, String> remainingBudgetCostShowISOValuesMap = new HashMap<Integer, String>();
		Map<Integer, Double> remainingBudgetCostSortOrderValuesMap = new HashMap<Integer, Double>();
		ComputedValuesLoaderBL.setRemainingBudgetValuesMap(remainingPlanList, workItemBeansMap, locale,
				remainingBudgetTimeShowValuesMap,remainingBudgetTimeShowISOValuesMap, remainingBudgetTimeSortOrderValuesMap, 
				remainingBudgetCostShowValuesMap, remainingBudgetCostShowISOValuesMap, remainingBudgetCostSortOrderValuesMap, projectAccountingTOMap);
		List<TFieldBean> systemFieldBeans = FieldBL.loadSystem();
		
		Set<Integer> projectIDs = projectToIssueTypesMap.keySet();
		Map<Integer, Boolean> projectLinkingMap = new HashMap<Integer, Boolean>();
		Map<Integer, Boolean> budgetPlanExpenseMap = new HashMap<Integer, Boolean>();
		for (Integer projectID : projectIDs) {
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				boolean linking = projectBean.isLinkingActive();
				projectLinkingMap.put(projectID, Boolean.valueOf(linking));
				ProjectAccountingTO projectAccountingTO = ProjectBL.getProjectAccountingTO(projectID);
				boolean budgetPlanExpense = projectAccountingTO.isWorkTracking() || projectAccountingTO.isCostTracking();
				budgetPlanExpenseMap.put(projectID, budgetPlanExpense);
			}
		}
		String unavailable = LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.unavailable", locale);
		//summary flag used only for XML export: TODO get them only if XML export 
		boolean summaryItemsBehavior = ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior();
		//get attachments
		Map<Integer, List<TAttachmentBean>> attachmentsMap = AttachBL.getGroupedAttachemnts(attachmentList);
		for (TWorkItemBean workItemBean : workItemBeanList) {
			Integer workItemID = workItemBean.getObjectID();
			Integer projectID = workItemBean.getProjectID();
			Map<Integer, String> showValuesMap = new HashMap<Integer, String>();
			Map<Integer, String> showISOValues = new HashMap<Integer, String>();
			Map<Integer, Comparable> sortOrderValuesMap = new HashMap<Integer, Comparable>();
			Map<Integer, Map<Integer, Integer>> issueTypeRestrictions = fieldRestrictions.get(projectID);
			Map<Integer, Integer> restrictedFields = null;
			if (issueTypeRestrictions!=null) {
				restrictedFields = issueTypeRestrictions.get(workItemBean.getListTypeID());
			}
			/****************************/
			/******system fields*********/
			/****************************/
			for (TFieldBean fieldBean : systemFieldBeans) {
				Integer fieldID = fieldBean.getObjectID();
				if (isHidden(restrictedFields, fieldID)) {
					//no read access for this field
					showValuesMap.put(fieldID, unavailable);
					sortOrderValuesMap.put(fieldID, null);
				} else {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					if (fieldTypeRT!=null) {
						if (fieldTypeRT.isComputed(fieldID, null)) {
							//compute duration
							fieldTypeRT.processDefaultValue(fieldID, null, null, null, workItemBean);
						}
						Object attributeValue = workItemBean.getAttribute(fieldID, null);
						String showValue = fieldTypeRT.getShowValue(fieldID, null, attributeValue, workItemID, localLookupContainer, locale);
						showValuesMap.put(fieldID, showValue);
						sortOrderValuesMap.put(fieldID, fieldTypeRT.getSortOrderValue(fieldID, null, attributeValue, workItemID, localLookupContainer));
					}
				}
			}
			/****************************/
			/******custom fields*********/
			/****************************/
			Map<String, Object> attributeValueBeanMap = workItemsAttributesMap.get(workItemID);
			if (attributeValueBeanMap!=null) {
				Iterator<String> itrAttributeBeans = attributeValueBeanMap.keySet().iterator();
				while (itrAttributeBeans.hasNext()) {
					String mergeKey = itrAttributeBeans.next();
					Integer[] keyComponents = MergeUtil.getParts(mergeKey);
					Integer fieldID = keyComponents[0];
					if (isHidden(restrictedFields, fieldID)) {
						//no read access for this field
						showValuesMap.put(fieldID, unavailable);
						sortOrderValuesMap.put(fieldID, null);
					} else {
						Integer parameterCode = keyComponents[1];
						if (parameterCode==null || new Integer(1).equals(parameterCode)) {
							//load the not composites and the composite if it is the first part
							//(in order to avoid loading the composite again and again for each of his part)
							IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
							if (fieldTypeRT!=null) {
								fieldTypeRT.processLoad(fieldID, null, 
										workItemBean, attributeValueBeanMap);
								Object attributeValue = workItemBean.getAttribute(fieldID);
								String showValue = fieldTypeRT.getShowValue(fieldID, null, attributeValue, workItemID, localLookupContainer, locale);
								showValuesMap.put(fieldID, showValue);
								sortOrderValuesMap.put(fieldID, fieldTypeRT.getSortOrderValue(fieldID, parameterCode, attributeValue, workItemID, localLookupContainer));
							}
						}
					}
				}
			}
			ReportBean reportBean = new ReportBean();
			reportBean.setWorkItemBean(workItemBean);
			reportBean.setEditable(workItemBean.isEditable());
			Boolean linking = projectLinkingMap.get(projectID);
			if (linking!=null) {
				reportBean.setLinking(linking.booleanValue() && workItemBean.isEditable());
			}
			Boolean budgetPlanExpense = budgetPlanExpenseMap.get(projectID);
			if (budgetPlanExpense==null || !budgetPlanExpense.booleanValue()) {
				if (restrictedFields==null) {
					restrictedFields = new HashMap<Integer, Integer>();
				}
				//project do not defines budget/plan/expenses at all: restrict them even if the user would have edit right on this fields by role
				restrictedFields.put(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES, Integer.valueOf(ACCESSFLAG.READ_ONLY));
				restrictedFields.put(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN, Integer.valueOf(ACCESSFLAG.READ_ONLY));
				restrictedFields.put(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET, Integer.valueOf(ACCESSFLAG.READ_ONLY));
			}
			if (restrictedFields!=null) {
				reportBean.setNotEditableFields(restrictedFields.keySet());
			}
			reportBean.setShowValuesMap(showValuesMap);
			reportBean.setSortOrderValuesMap(sortOrderValuesMap);
			reportBean.setShowISOValuesMap(showISOValues);
			//set the projectType label
			ProjectAccountingTO projectAccountingTO = null;
			if (projectID!=null) {
				TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
				if (projectBean!=null) {
					TProjectTypeBean projectTypeBean = projectTypeMap.get(projectBean.getProjectType());
					reportBean.setProjectType(projectTypeBean.getLabel());
				}
				projectAccountingTO = projectAccountingTOMap.get(projectID);
			}
			/****************************/
			/******pseudo fields*********/
			/****************************/
			//add pseudo fields and the corresponding show values and sort order values
			//TODO in the future get each pseudo field only if it is selected to be shown
			//set the consultant/informant ids
			//set the real values for consulted and informed
			if (!isHidden(restrictedFields, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS)) {
				Map<String, Set<Integer>> consInfForWorkItem = consInfIDsMap.get(workItemID);
				if (consInfForWorkItem!=null) {
					reportBean.setConsultedList(consInfForWorkItem.get(RaciRole.CONSULTANT));
					reportBean.setInformedList(consInfForWorkItem.get(RaciRole.INFORMANT));
					//set the show- and order values for consulted and informed
					ConsultedInformedLoaderBL.getConsultedInformedValues(workItemID, consInfIDsMap, 
							showValuesMap, sortOrderValuesMap);
				}
			}
			//sort order is the same as their real field counterpart
			sortOrderValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL, 
					sortOrderValuesMap.get(SystemFields.INTEGER_ISSUETYPE));
			//add status symbol and set setOnPlan() flag
			//the state isn't a required field at database schema level! 
			//Although it should always have a value it might be null.
			Integer statusID = workItemBean.getStateID();
			if (statusID!=null) {
				TStateBean stateBean = LookupContainer.getStatusBean(statusID, locale);
				if (stateBean!=null) {
					reportBean.setStateFlag(stateBean.getStateflag());
					//sort order is the same as their real field counterpart
					sortOrderValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.STATUS_SYMBOL, 
							sortOrderValuesMap.get(SystemFields.INTEGER_STATE));
					int bottomUpDateDueFlag = workItemBean.calculateBottomUpDueDateOnPlan(stateBean.getStateflag(), daysLead);
					reportBean.setBottomUpDateDueFlag(bottomUpDateDueFlag);
					reportBean.setCommittedDateConflict(TWorkItemBean.isDateConflict(bottomUpDateDueFlag));
					int topDownDateDueFlag = workItemBean.calculateTopDownDueDateOnPlan(stateBean.getStateflag(), daysLead);
					reportBean.setTopDownDateDueFlag(topDownDateDueFlag);
					reportBean.setTargetDateConflict(TWorkItemBean.isDateConflict(topDownDateDueFlag));
				}
			}
			//sort order is the same as their real field counterpart
			sortOrderValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.PRIORITY_SYMBOL, 
					sortOrderValuesMap.get(SystemFields.INTEGER_PRIORITY));
			//add severity symbol: because severity is not mandatory, first test whether it is present
			Integer severity = workItemBean.getSeverityID();
			if (severity!=null) {
				//sort order is the same as their real field counterpart
				sortOrderValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.SEVERITY_SYMBOL, 
						sortOrderValuesMap.get(SystemFields.INTEGER_SEVERITY));
			}
			//add private issue symbol:
			if (!isHidden(restrictedFields, SystemFields.INTEGER_ACCESSLEVEL)) {
				boolean privateIssue = workItemBean.isAccessLevelFlag();
				if (privateIssue) {
					showValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL, "lock.gif");
				} else {
					showValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL, "unlock.gif");
				}
				sortOrderValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL, Boolean.valueOf(privateIssue));
			}
			if (!isHidden(restrictedFields, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES)) {
				//my expense time show values and sortOrder values
				ComputedValuesLoaderBL.setShowAndSortOrderValues(workItemID, showValuesMap, showISOValues, sortOrderValuesMap,
						myExpenseTimeShowValuesMap, myExpenseTimeShowISOValuesMap, myExpenseTimeSortOrderValuesMap,
						TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME);
				
				//my expense cost show values and sortOrder values
				ComputedValuesLoaderBL.setShowAndSortOrderValues(workItemID, showValuesMap, showISOValues, sortOrderValuesMap,
						myExpenseCostShowValuesMap, myExpenseCostShowISOValuesMap, myExpenseCostSortOrderValuesMap,
						TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST);
			}
			if (!isHidden(restrictedFields, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES)) {
				//total expense time show values and sortOrder values
				ComputedValuesLoaderBL.setShowAndSortOrderValues(workItemID, showValuesMap, showISOValues, sortOrderValuesMap,
						totalExpenseTimeShowValuesMap, totalExpenseTimeShowISOValuesMap, totalExpenseTimeSortOrderValuesMap,
						TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME);
				
				//total expense cost show values and sortOrder values
				ComputedValuesLoaderBL.setShowAndSortOrderValues(workItemID, showValuesMap, showISOValues, sortOrderValuesMap,
						totalExpenseCostShowValuesMap, totalExpenseCostShowISOValuesMap, totalExpenseCostSortOrderValuesMap,
						TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST);
			}
			if (!isHidden(restrictedFields, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN)) {
				//total budget time show values and sortOrder values
				ComputedValuesLoaderBL.setShowAndSortOrderValues(workItemID, showValuesMap, showISOValues, sortOrderValuesMap,
						totalPlanTimeShowValuesMap, totalPlanTimeShowISOValuesMap, totalPlanTimeSortOrderValuesMap,
						TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME);
				
				//total budget cost show values and sortOrder values
				ComputedValuesLoaderBL.setShowAndSortOrderValues(workItemID, showValuesMap, showISOValues, sortOrderValuesMap,
						totalPlanCostShowValuesMap, totalPlanCostShowISOValuesMap, totalPlanCostSortOrderValuesMap,
						TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST);
			
				//remaining budget time show values and sortOrder values
				ComputedValuesLoaderBL.setShowAndSortOrderValues(workItemID, showValuesMap, showISOValues, sortOrderValuesMap,
						remainingBudgetTimeShowValuesMap, remainingBudgetTimeShowISOValuesMap, remainingBudgetTimeSortOrderValuesMap,
						TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME);
				
				//remaining budget cost show values and sortOrder values
				ComputedValuesLoaderBL.setShowAndSortOrderValues(workItemID, showValuesMap, showISOValues, sortOrderValuesMap,
						remainingBudgetCostShowValuesMap, remainingBudgetCostShowISOValuesMap, remainingBudgetCostSortOrderValuesMap,
						TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST);
			}
			if (budgetActive && (!isHidden(restrictedFields, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET))) {
				//total budget time show values and sortOrder values
				ComputedValuesLoaderBL.setShowAndSortOrderValues(workItemID, showValuesMap, showISOValues, sortOrderValuesMap,
						budgetTimeShowValuesMap, budgetTimeShowValuesMap, budgetTimeSortOrderValuesMap, 
						TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME);
				
				//total budget cost show values and sortOrder values
				ComputedValuesLoaderBL.setShowAndSortOrderValues(workItemID, showValuesMap, showISOValues, sortOrderValuesMap,
						budgetCostShowValuesMap, budgetCostShowISOValuesMap, budgetCostSortOrderValuesMap,
						TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST);
			}
			//attachment
			if (!isHidden(restrictedFields, TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL)) {
				List<TAttachmentBean> attachList=attachmentsMap.get(workItemID);
				Integer attachments=null;
				StringBuffer attachmentIds=new StringBuffer();
				if(attachList!=null && !attachList.isEmpty()){
					attachments=Integer.valueOf(attachList.size());
					for (int i = 0; i < attachList.size(); i++) {
						TAttachmentBean tAttachmentBean = attachList.get(i);
						attachmentIds.append(tAttachmentBean.getObjectID());
						if(i<attachList.size()-1){
							attachmentIds.append(";");
						}
					}
				}
				showValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL, attachments==null?"":attachments.toString());
				sortOrderValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL,attachments==null?Integer.valueOf(0):attachments);
				reportBean.setAttachmentIds(attachmentIds.toString());
			}
			//set the budget plan flag
			boolean workTracking = false;
			boolean costTracking = false;
			if (projectAccountingTO!=null) {
				workTracking = projectAccountingTO.isWorkTracking();
				costTracking = projectAccountingTO.isCostTracking();
			}
			reportBean.setWithinBudgetAndPlan(budgetActive, workTracking, costTracking);
			if (summaryItemsBehavior && parentIDsSet!=null) {
				reportBean.setSummary(parentIDsSet.contains(workItemID));
			}
			if (!reportBean.isTargetDateConflict()) {
				boolean targetStartDateHidden = false;
				boolean targetEndDateHidden = false;
				if (restrictedFields!=null) {
					targetStartDateHidden = isHidden(restrictedFields, SystemFields.INTEGER_TOP_DOWN_START_DATE);
					targetEndDateHidden = isHidden(restrictedFields, SystemFields.INTEGER_TOP_DOWN_END_DATE);
				}
				reportBean.setTargetDateConflict(workItemBean.calculateTargetDateConflict(targetStartDateHidden, targetEndDateHidden));
			}
			reportBeanList.add(reportBean);
		}
		if (itemLinkList!=null && !itemLinkList.isEmpty()) {
			LoadItemLinksUtil.addAllLinkedWorkItemsToReportBeans(reportBeanList, itemLinkList, personID, locale);
		}
		if (LOGGER.isDebugEnabled() && start!=null) {
			Date end = new Date();
			LOGGER.debug("Populating " + reportBeanList.size() + " ReportBeans lasted " + new Long(end.getTime()-start.getTime()).toString() + " ms");
		}
		return reportBeanList;
	}
	
	/**
	 * Whether the field is hidden
	 * @param restrictedFields
	 * @param fieldID
	 * @return
	 */
	private static boolean isHidden(Map<Integer, Integer> restrictedFields, Integer fieldID) {
		if (restrictedFields!=null && restrictedFields.containsKey(fieldID)) {
			Integer accessFlag = restrictedFields.get(fieldID);
			if (accessFlag!=null && accessFlag.intValue()==ACCESSFLAG.NOACCESS) {
				return true;
			}
		}
		return false;
	}
	
	/** 
	 * Populates a list of reportBeans
	 * @param reportBeanList  list of workItemBeans
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static List<ReportBean> addISOValuesToReportBeans(List<ReportBean> reportBeanList, Integer personID, Locale locale) {
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap<Integer, Set<Integer>>();
		List<TWorkItemBean> workItemBeansList = new LinkedList<TWorkItemBean>();
		int[] workItemIDs = null;
		if (reportBeanList!=null) {
			workItemIDs = new int[reportBeanList.size()];
			for (int i = 0; i < reportBeanList.size(); i++) {
				ReportBean reportBean = reportBeanList.get(i);
				TWorkItemBean workItemBean = reportBean.getWorkItemBean();
				workItemIDs[i] = workItemBean.getObjectID();
				Integer projectID = workItemBean.getProjectID();
				Integer issueTypeID = workItemBean.getListTypeID();
				Set<Integer> issueTypes = projectToIssueTypesMap.get(projectID);
				if (issueTypes==null) {
					issueTypes = new HashSet<Integer>();
					projectToIssueTypesMap.put(projectID, issueTypes);
				}
				issueTypes.add(issueTypeID);
			}
		} else {
			return reportBeanList;
		}
		LocalLookupContainer localLookupContainer = ItemBL.getItemHierarchyContainer(workItemBeansList);
		List<TAttributeValueBean> attributeValueBeanList = AttributeValueBL.loadByWorkItemKeys(workItemIDs);
		Set<Integer> customOptionIDs = new HashSet<Integer>();
		Map<Integer, Set<Integer>> externalOptionsSetByField = new HashMap<Integer, Set<Integer>>();
		AttributeValueBL.prepareAttributeValueMapForWorkItems(attributeValueBeanList, customOptionIDs, externalOptionsSetByField);
		//gets the custom option lookups
		List<TOptionBean> optionBeans = OptionBL.loadByKeys(GeneralUtils.createIntegerArrFromCollection(customOptionIDs));
		localLookupContainer.setCustomOptionsMap(GeneralUtils.createMapFromList(optionBeans));
		Date start = null;
		if (LOGGER.isDebugEnabled()) {
			start = new Date();
		}
		Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions =
				AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, null, false);
		Iterator<ReportBean> itrReportBeans = reportBeanList.iterator();
		List<TFieldBean> systemFieldBeans = FieldBL.loadSystem();
		while (itrReportBeans.hasNext()) {
			ReportBean reportBean = itrReportBeans.next();
			TWorkItemBean workItemBean = reportBean.getWorkItemBean();
			Map<Integer, String> showValuesMap = reportBean.getShowValuesMap();
			Map<Integer, String> showISOValues = reportBean.getShowISOValuesMap();
			if (showISOValues==null) {
				showISOValues = new HashMap<Integer, String>();
				reportBean.setShowISOValuesMap(showISOValues);
			}
			Map<Integer, Map<Integer, Integer>> issueTypeRestrictions = fieldRestrictions.get(workItemBean.getProjectID());
			Map<Integer, Integer> hiddenFields = null;
			if (issueTypeRestrictions!=null) {
				hiddenFields = issueTypeRestrictions.get(workItemBean.getListTypeID());
			}
			/****************************/
			/******system fields*********/
			/****************************/
			for (TFieldBean fieldBean : systemFieldBeans) {
				Integer fieldID = fieldBean.getObjectID();
				if (hiddenFields==null || !hiddenFields.containsKey(fieldID)) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					if (fieldTypeRT!=null) {
						Object attributeValue = workItemBean.getAttribute(fieldID, null);
						String showValue = (String)showValuesMap.get(fieldID);
						if (fieldTypeRT.isoDiffersFromLocaleSpecific()) {
							String showISOValue = fieldTypeRT.getShowISOValue(fieldID, null, 
								attributeValue, workItemBean.getObjectID(), localLookupContainer, locale);
							//add showISOValue to showISOValues map only if differs from showValue to save space!
							if (EqualUtils.valueModified(showValue, showISOValue)) {
								showISOValues.put(fieldID, showISOValue);
							}
						}
					}
				}
			}
			/****************************/
			/******custom fields*********/
			/****************************/
			if (workItemBean.getCustomAttributeValues()!=null) {
				Iterator<String> itrAttributeBeans = workItemBean.getCustomAttributeValues().keySet().iterator();
				while (itrAttributeBeans.hasNext()) {
					String mergeKey = (String) itrAttributeBeans.next();
					Integer[] keyComponents = MergeUtil.getParts(mergeKey.substring(TWorkItemBean.PREFIX_LETTER.length()));
					Integer fieldID = keyComponents[0];
					if (hiddenFields==null || !hiddenFields.containsKey(fieldID)) {
						Integer parameterCode = keyComponents[1];
						if (parameterCode==null || Integer.valueOf(1).equals(parameterCode)) {
							//load the not composites and the composite if it is the first part
							//(in order to avoid loading the composite again and again for each of his part)
							IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
							if (fieldTypeRT!=null) {
								Object attributeValue = workItemBean.getAttribute(fieldID);
								String showValue = (String)showValuesMap.get(fieldID);
								String showISOValue = fieldTypeRT.getShowISOValue(fieldID, null, attributeValue, 
									workItemBean.getObjectID(), localLookupContainer, locale);
								//add showISOValue to showISOValues map only if differs from showValue to save space!
								if (EqualUtils.valueModified(showValue, showISOValue)) {
									showISOValues.put(fieldID, showISOValue);
								}
								if (attributeValue!=null && (fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION || fieldTypeRT.isComposite())) {
									Object[] customOptions = null; 
									if (fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION) {
										try {
											customOptions = (Object[])attributeValue;
										} catch (Exception e) {
										}
									} else {
										Map<Integer, Object[]> selectedComposite = (Map<Integer, Object[]>)attributeValue;
										if (selectedComposite!=null) {
											customOptions = selectedComposite.get(selectedComposite.size());
										}
									}
									if (customOptions!=null && customOptions.length>0) {
										showISOValues.put(Integer.valueOf(-fieldID), getIconsParameters(customOptions));
									}
								}
							}
						}
					}
				}
			}
			if (hiddenFields==null || !hiddenFields.containsKey(SystemFields.INTEGER_ISSUETYPE)) {
				if(workItemBean.getListTypeID()!=null) {
					showISOValues.put(TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL,
						getIconParameters(workItemBean.getListTypeID(), TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL));
				}
			}
			if (hiddenFields==null || !hiddenFields.containsKey(SystemFields.INTEGER_STATE)) {
				if (workItemBean.getStateID()!=null) {
					showISOValues.put(TReportLayoutBean.PSEUDO_COLUMNS.STATUS_SYMBOL,
						getIconParameters(workItemBean.getStateID(), TReportLayoutBean.PSEUDO_COLUMNS.STATUS_SYMBOL));
				}
			}
			if (hiddenFields==null || !hiddenFields.containsKey(SystemFields.INTEGER_PRIORITY)) {
				if(workItemBean.getPriorityID()!=null){
					showISOValues.put(TReportLayoutBean.PSEUDO_COLUMNS.PRIORITY_SYMBOL,
							getIconParameters(workItemBean.getPriorityID(), TReportLayoutBean.PSEUDO_COLUMNS.PRIORITY_SYMBOL));
				}
			}
			if (hiddenFields==null || !hiddenFields.containsKey(SystemFields.INTEGER_SEVERITY)) {
				if (workItemBean.getSeverityID()!=null) {
					showISOValues.put(TReportLayoutBean.PSEUDO_COLUMNS.SEVERITY_SYMBOL,
							getIconParameters(workItemBean.getSeverityID(), TReportLayoutBean.PSEUDO_COLUMNS.SEVERITY_SYMBOL));
				}
			}
		}
		if (LOGGER.isDebugEnabled() && start!=null) {
			Date end = new Date();
			LOGGER.debug("Adding ISO values for " + reportBeanList.size() + " ReportBeans lasted " + new Long(end.getTime()-start.getTime()).toString() + " ms");
		}
		return reportBeanList;
	}
	
	/**
	 * Add icon parameters for generation the icons in jasper reports for single select (system) fields 
	 * @param optionID
	 * @param fieldID
	 * @return
	 */
	private static String getIconParameters(Integer optionID, Integer fieldID) {
		StringBuilder stringBuilder = new StringBuilder();
		if (optionID!=null) {
			stringBuilder.append("optionID=" + optionID);
			if (fieldID!=null) {
				stringBuilder.append("&fieldID=" + fieldID);
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Add icon parameters for generation the icons in jasper reports for multiple select (custom select) fields
	 * @param optionIDs
	 * @return
	 */
	private static String getIconsParameters(Object[] optionIDs) {
		if (optionIDs!=null && optionIDs.length>0) {
			List<Integer> optionList = new LinkedList<Integer>();
			for (Object object : optionIDs) {
				if (object!=null) {
					try {
						optionList.add(Integer.valueOf(object.toString()));
					} catch (Exception e) {
					}
				}
			}
			//FIXME to add multiple options if multiple select (how to handle multiple images in jasper reports?)
			return "optionID=" + optionList.get(0);
		}
		return "";
	}
}
