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

package com.aurel.track.report.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.user.avatar.AvatarBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TBudgetBean.BUDGET_TYPE;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.THistoryTransactionBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.item.FileDiffTO;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.budgetCost.BudgetBL;
import com.aurel.track.item.budgetCost.ExpenseBL;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.item.history.FlatHistoryBean;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.item.history.HistoryTransactionBL;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.mobile.MobileBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.StringArrayParameterUtils;
import com.aurel.track.vc.VersionControlMap;
/**
 * This shows the last activities in the system.
 *
 * @author Adi
 *
 */
public class ActivityStream extends TimePeriodDashboardView {
	private static final Logger LOGGER = LogManager.getLogger(ActivityStream.class);

	public static interface CONFIGURATION_PARAMETERS {
		static String PERSONS = "changedByPersons";
		static String SELECTED_PERSON = "selectedChangedByPerson";
		static String CHANGE_TYPES = "changeTypes";
		static String SELECTED_CHANGE_TYPE = "selectedChangeType";
		static String GRID_VIEW = "gridView";
	}

	protected static final int MAX_LIMIT = 1000;
	protected static final int DEFAULT_MAX_NUMBER_OF_ISSUES_TO_SHOW = 100;

	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		Locale locale = user.getLocale();
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendIntegerValue(sb, RENDERING_PARAMS.REFRESH_RATE, parseInteger(parameters, RENDERING_PARAMS.REFRESH_RATE));
		sb.append(getDatasourceConfig(parameters, entityId, entityType, locale));
		sb.append(getTimePeriodConfig(parameters, locale));
		JSONUtility.appendIntegerStringBeanList(sb,CONFIGURATION_PARAMETERS.CHANGE_TYPES, HistoryLoaderBL.getPossibleHistoryFields(locale));
		JSONUtility.appendIntegerListAsArray(sb,CONFIGURATION_PARAMETERS.SELECTED_CHANGE_TYPE,
				StringArrayParameterUtils.splitSelectionAsInteger(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_CHANGE_TYPE)));
		List<TPersonBean> persons = PersonBL.loadPersons();
		JSONUtility.appendILabelBeanList(sb,CONFIGURATION_PARAMETERS.PERSONS, (List)persons);
		JSONUtility.appendIntegerListAsArray(sb,CONFIGURATION_PARAMETERS.SELECTED_PERSON,
				StringArrayParameterUtils.splitSelectionAsInteger(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_PERSON)));
		JSONUtility.appendBooleanValue(sb, CONFIGURATION_PARAMETERS.GRID_VIEW, parseBoolean(parameters, CONFIGURATION_PARAMETERS.GRID_VIEW));
		String maxIssuesToShowStr= parameters.get("maxIssuesToShow");
		Integer maxIssuesToShow=null;
		if(maxIssuesToShowStr!=null){
			try{
				maxIssuesToShow=Integer.parseInt(maxIssuesToShowStr);
			}catch (Exception e){}
		}
		if(maxIssuesToShow==null){
			maxIssuesToShow=DEFAULT_MAX_NUMBER_OF_ISSUES_TO_SHOW;
		}
		JSONUtility.appendIntegerValue(sb,"maxIssuesToShow", maxIssuesToShow);

		return sb.toString();
	}

	protected boolean isUseConfig(){
		return true;
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID, Map<String, Object> session, Map<String, String> parameters,
										 Integer projectID, Integer releaseID, Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
		TPersonBean user = (TPersonBean)session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();
		Locale locale=(Locale)session.get(Constants.LOCALE_KEY);
		boolean gridView=parseBoolean(parameters, CONFIGURATION_PARAMETERS.GRID_VIEW);
		//If mobile client then JSON data must be returned /not HTML one/
		if(gridView || MobileBL.isMobileApp(session)){

			 if(MobileBL.isMobileApp(session)) {
					List<FlatHistoryBean> beanList = getActivityItemStreamsHTML(parameters, projectID, releaseID, user, locale);
					Set<Integer>personIDsSet = new HashSet<Integer>();
					for (Iterator<FlatHistoryBean> iterator = beanList.iterator(); iterator.hasNext();) {
						FlatHistoryBean aBean = iterator.next();
						if(aBean != null && aBean.getPersonID() != null) {
							personIDsSet.add(aBean.getPersonID());
						}else {
							//beans withoout personID needs to be removed otherwise Teamgeist fails.
							iterator.remove();
						}
					}
					JSONUtility.appendJSONValue(sb, "items", ActivityStreamJSON.encodeActivityHTMLData(beanList));
					Map<Integer, String>personIDToAvatarCheckSum = AvatarBL.getAvatarsCheckSum(personIDsSet);
					JSONUtility.appendJSONValue(sb, "checkSums", ActivityStreamJSON.encodeAvatarCheckSum(personIDToAvatarCheckSum));

			 }else {
				 List<ActivityStreamItem> activityStreamItems = getActivityItemStreamsForGrid(parameters, projectID, releaseID, user, locale);
				 JSONUtility.appendJSONValue(sb, "items", ActivityStreamJSON.encodeActivityItemList(activityStreamItems));
			 }
		}
		JSONUtility.appendBooleanValue(sb, CONFIGURATION_PARAMETERS.GRID_VIEW,gridView);
		return sb.toString();
	}

	/**
	 * Gets the activity stream items
	 * @param parameters
	 * @param projectID
	 * @param releaseID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private List<ActivityStreamItem> getActivityItemStreamsForGrid(Map<String, String> parameters,
			 Integer projectID, Integer releaseID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		//List<TWorkItemBean> workItemBeans = getWorkItemBeans(parameters, projectID, releaseID, personBean, locale);
		//Map<Integer, TWorkItemBean> workItemBeansMap = GeneralUtils.createMapFromList(workItemBeans);
		int limit=DEFAULT_MAX_NUMBER_OF_ISSUES_TO_SHOW;
		String maxIssuesToShowStr=parameters.get("maxIssuesToShow");
		if(maxIssuesToShowStr!=null&&maxIssuesToShowStr.length()>0){
			try{
				limit=Integer.parseInt(maxIssuesToShowStr);
				if(limit<=0){
					limit=DEFAULT_MAX_NUMBER_OF_ISSUES_TO_SHOW;
				}
				if(limit>MAX_LIMIT){
					limit=MAX_LIMIT;
				}
			}catch (Exception e) {
				limit=DEFAULT_MAX_NUMBER_OF_ISSUES_TO_SHOW;
			}
		}
		FilterUpperTO filterUpperTO = getFilterUpperTO(parameters, projectID, releaseID, personBean, locale, true, true);
		RACIBean raciBean = getRACIBean(parameters, projectID, releaseID, personBean, filterUpperTO);
		Integer periodType = parseInteger(parameters, TIMEPERIOD_PARAMETERS.SELECTED_PERIOD_TYPE, PERIOD_TYPE.DAYS_BEFORE);
		Date dateFrom=DateTimeUtils.getInstance().parseISODate(parameters.get(TIMEPERIOD_PARAMETERS.DATE_FROM));
		Date dateTo=DateTimeUtils.getInstance().parseISODate(parameters.get(TIMEPERIOD_PARAMETERS.DATE_TO));
		Integer daysBefore=parseInteger(parameters, TIMEPERIOD_PARAMETERS.DAYS_BEFORE,30);
		List<Integer> changeTypes = StringArrayParameterUtils.splitSelectionAsInteger(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_CHANGE_TYPE));
		List<Integer> changedByPersons = StringArrayParameterUtils.splitSelectionAsInteger(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_PERSON));
		dateFrom = getDateFrom(periodType, dateFrom, daysBefore);
		dateTo = getDateTo(periodType, dateTo);
		boolean noExplicitChangeTypes = changeTypes==null || changeTypes.isEmpty();
		//expense
		List<TCostBean> costBeans = null;
		if (noExplicitChangeTypes || changeTypes.contains(SystemFields.INTEGER_COST_HISTORY)) {
			costBeans = ExpenseBL.loadActivityStreamCosts(filterUpperTO, raciBean, personBean.getObjectID(), limit, dateFrom, dateTo, changedByPersons);
			if (changeTypes.contains(SystemFields.INTEGER_COST_HISTORY)) {
				changeTypes.remove(SystemFields.INTEGER_COST_HISTORY);
			}
		}
		//plan/budget
		List<TBudgetBean> budgetOrPlanBeans = null;
		boolean includePlan = noExplicitChangeTypes || changeTypes.contains(SystemFields.INTEGER_PLAN_HISTORY);
		boolean includeBudget = noExplicitChangeTypes || changeTypes.contains(SystemFields.INTEGER_BUDGET_HISTORY);
		if (includeBudget || includePlan) {
			Boolean plan = null;
			if (!(includeBudget && includePlan)) {
				plan = Boolean.valueOf(includePlan);
			}
			if (changeTypes!=null && changeTypes.contains(SystemFields.INTEGER_PLAN_HISTORY)) {
				changeTypes.remove(SystemFields.INTEGER_PLAN_HISTORY);
			}
			if (changeTypes!=null && changeTypes.contains(SystemFields.INTEGER_BUDGET_HISTORY)) {
				changeTypes.remove(SystemFields.INTEGER_BUDGET_HISTORY);
			}
			budgetOrPlanBeans = BudgetBL.loadActivityStreamBugetsPlans(filterUpperTO, raciBean, personBean.getObjectID(), limit, dateFrom, dateTo, changedByPersons, plan);
			if (budgetOrPlanBeans!=null) {
				for (TBudgetBean budgetBean : budgetOrPlanBeans) {
					TPersonBean changedByPerson = LookupContainer.getPersonBean(budgetBean.getChangedByID());
					if (changedByPerson!=null) {
						budgetBean.setChangedByName(personBean.getLabel());
					} else {
						budgetBean.setChangedByName(" "); // TODO This should never happen, but it does
					}

				}
			}
		}
		//version control
		List<VCActivityItem> vcActivityItems = null;
		if (noExplicitChangeTypes || changeTypes.contains(SystemFields.INTEGER_VERSION_CONTROL)) {
			if (changeTypes.contains(SystemFields.INTEGER_VERSION_CONTROL)) {
				changeTypes.remove(SystemFields.INTEGER_VERSION_CONTROL);
			}
			List<TWorkItemBean> workItemBeans = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
			List<Integer> workItemIDs = GeneralUtils.createIntegerListFromBeanList(workItemBeans);
			vcActivityItems=VcBL.getLastRevisionsByItems(workItemIDs, limit, locale,false);
		}
		//item history
		List<THistoryTransactionBean> historyTransactions = null;
		if (noExplicitChangeTypes || !changeTypes.isEmpty()) {
			historyTransactions = HistoryTransactionBL.loadActivityStreamHistoryTransactions(filterUpperTO, raciBean,
					personBean.getObjectID(), limit, dateFrom, dateTo, changeTypes, changedByPersons);
			HistoryLoaderBL.addPersonNamesToHistoryTransactionBeans(historyTransactions);
		}
		Map<Integer, String> fieldsConfigLabelsMap = null;
		if (changeTypes!=null && !changeTypes.isEmpty()) {
			fieldsConfigLabelsMap = FieldRuntimeBL.getLocalizedDefaultFieldLabels(changeTypes, locale);
		} else {
			fieldsConfigLabelsMap = new HashMap<Integer, String>();
			List<TFieldConfigBean> fieldConfigurationBeans = FieldRuntimeBL.getLocalizedDefaultFieldConfigs(locale);
			if (fieldConfigurationBeans!=null) {
				for (TFieldConfigBean fieldConfigBean : fieldConfigurationBeans) {
					fieldsConfigLabelsMap.put(fieldConfigBean.getField(), fieldConfigBean.getLabel());
				}
			}
		}
		return createActivityItems(historyTransactions, costBeans, budgetOrPlanBeans, vcActivityItems, limit, locale, fieldsConfigLabelsMap, changeTypes);
		//}
		//return null;
	}

	/**
	 * Gets the activity stream items
	 * @param parameters
	 * @param projectID
	 * @param releaseID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	public List<FlatHistoryBean> getActivityItemStreamsHTML( Map<String, String> parameters,
			 Integer projectID, Integer releaseID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		int limit=DEFAULT_MAX_NUMBER_OF_ISSUES_TO_SHOW;
		String maxIssuesToShowStr=parameters.get("maxIssuesToShow");
		if(maxIssuesToShowStr!=null&&maxIssuesToShowStr.length()>0){
			try{
				limit=Integer.parseInt(maxIssuesToShowStr);
				if(limit<=0){
					limit=DEFAULT_MAX_NUMBER_OF_ISSUES_TO_SHOW;
				}
				if(limit>MAX_LIMIT){
					limit=MAX_LIMIT;
				}
			}catch (Exception e) {
				limit=DEFAULT_MAX_NUMBER_OF_ISSUES_TO_SHOW;
			}
		}
		FilterUpperTO filterUpperTO = getFilterUpperTO(parameters, projectID, releaseID, personBean, locale, true, true);
		RACIBean raciBean = getRACIBean(parameters, projectID, releaseID, personBean, filterUpperTO);
		List<TWorkItemBean> workItemBeans = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
		List<Integer> workItemIDs = GeneralUtils.createIntegerListFromBeanList(workItemBeans);
		Map<Integer, TWorkItemBean> workItemBeansMap = GeneralUtils.createMapFromList(workItemBeans);
		if (workItemBeans!=null && !workItemBeans.isEmpty()) {
			Integer periodType = parseInteger(parameters, TIMEPERIOD_PARAMETERS.SELECTED_PERIOD_TYPE, PERIOD_TYPE.DAYS_BEFORE);
			Date dateFrom=DateTimeUtils.getInstance().parseISODate(parameters.get(TIMEPERIOD_PARAMETERS.DATE_FROM));
			Date dateTo=DateTimeUtils.getInstance().parseISODate(parameters.get(TIMEPERIOD_PARAMETERS.DATE_TO));
			Integer daysBefore=parseInteger(parameters, TIMEPERIOD_PARAMETERS.DAYS_BEFORE,30);
			List<Integer> changeTypes = StringArrayParameterUtils.splitSelectionAsInteger(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_CHANGE_TYPE));
			List<Integer> changedByPersons = StringArrayParameterUtils.splitSelectionAsInteger(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_PERSON));
			dateFrom = getDateFrom(periodType, dateFrom, daysBefore);
			dateTo = getDateTo(periodType, dateTo);
			boolean noExplicitChangeTypes = changeTypes==null || changeTypes.isEmpty();
			//expense
			List<TCostBean> costBeans = null;
			if (noExplicitChangeTypes || changeTypes.contains(SystemFields.INTEGER_COST_HISTORY)) {
				costBeans = ExpenseBL.loadActivityStreamCosts(filterUpperTO, raciBean, personBean.getObjectID(), limit, dateFrom, dateTo, changedByPersons);
				/*costBeans = ExpenseBL.loadCostsForWorkItems(workItemIDArr,
						workItemBeansMap, dateFrom, dateTo, personBean.getObjectID(), GeneralUtils.createIntegerArrFromCollection(changedByPersons), null);*/
				if (changeTypes.contains(SystemFields.INTEGER_COST_HISTORY)) {
					changeTypes.remove(SystemFields.INTEGER_COST_HISTORY);
				}
			}
			//plan/budget
			List<TBudgetBean> budgetOrPlanBeans = null;
			boolean includePlan = noExplicitChangeTypes || changeTypes.contains(SystemFields.INTEGER_PLAN_HISTORY);
			boolean includeBudget = noExplicitChangeTypes || changeTypes.contains(SystemFields.INTEGER_BUDGET_HISTORY);
			if (includeBudget || includePlan) {
				Boolean plan = null;
				if (!(includeBudget && includePlan)) {
					plan = Boolean.valueOf(includePlan);
				}
				if (changeTypes!=null && changeTypes.contains(SystemFields.INTEGER_PLAN_HISTORY)) {
					changeTypes.remove(SystemFields.INTEGER_PLAN_HISTORY);
				}
				if (changeTypes!=null && changeTypes.contains(SystemFields.INTEGER_BUDGET_HISTORY)) {
					changeTypes.remove(SystemFields.INTEGER_BUDGET_HISTORY);
				}
				budgetOrPlanBeans = BudgetBL.loadActivityStreamBugetsPlans(filterUpperTO, raciBean, personBean.getObjectID(), limit, dateFrom, dateTo, changedByPersons, plan);
				if (budgetOrPlanBeans!=null) {
					for (TBudgetBean budgetBean : budgetOrPlanBeans) {
						TPersonBean changedByPerson = LookupContainer.getPersonBean(budgetBean.getChangedByID());
						if (changedByPerson!=null) {
							budgetBean.setChangedByName(personBean.getLabel());
						} else {
							budgetBean.setChangedByName("");	// TODO This should never happen, but it does
						}
					}
				}
			}
			//version control
			List<VCActivityItem> vcActivityItems = null;
			if (noExplicitChangeTypes || changeTypes.contains(SystemFields.INTEGER_VERSION_CONTROL)) {
				if (changeTypes.contains(SystemFields.INTEGER_VERSION_CONTROL)) {
					changeTypes.remove(SystemFields.INTEGER_VERSION_CONTROL);
				}
				vcActivityItems=VcBL.getLastRevisionsByItems(workItemIDs, limit, locale,false);
			}
			//item history
			List<FlatHistoryBean> flatHistoryBeans = null;
			if (noExplicitChangeTypes || !changeTypes.isEmpty()) {
				List<THistoryTransactionBean> historyTransactionBeanList = HistoryTransactionBL.loadActivityStreamHistoryTransactions(filterUpperTO, raciBean,
						personBean.getObjectID(), limit, dateFrom, dateTo, changeTypes, changedByPersons);
				List<Integer> transactionIDs = new LinkedList<Integer>();
				for (THistoryTransactionBean historyTransactionBean : historyTransactionBeanList) {
					transactionIDs.add(historyTransactionBean.getObjectID());
				}
				List<TFieldChangeBean> fieldChangeBeanList=FieldChangeBL.loadByTransactionIDS(transactionIDs);
				Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> allHistoryMap =
						HistoryLoaderBL.getWorkItemsHistory(historyTransactionBeanList, fieldChangeBeanList, null,
								changedByPersons, locale, false, LONG_TEXT_TYPE.ISFULLHTML, true,  personBean.getObjectID());
				flatHistoryBeans = HistoryLoaderBL.getFlatHistoryValuesListForWorkItems(allHistoryMap, workItemBeansMap, locale);
			}
			Map<Integer, String> fieldsConfigLabelsMap = null;
			if (changeTypes!=null && !changeTypes.isEmpty()) {
				fieldsConfigLabelsMap = FieldRuntimeBL.getLocalizedDefaultFieldLabels(changeTypes, locale);
			} else {
				fieldsConfigLabelsMap = new HashMap<Integer, String>();
				List<TFieldConfigBean> fieldConfigurationBeans = FieldRuntimeBL.getLocalizedDefaultFieldConfigs(locale);
				if (fieldConfigurationBeans!=null) {
					for (TFieldConfigBean fieldConfigBean : fieldConfigurationBeans) {
						fieldsConfigLabelsMap.put(fieldConfigBean.getField(), fieldConfigBean.getLabel());
					}
				}
			}
			return createActivityItemsForHTML(workItemBeans, flatHistoryBeans, costBeans, budgetOrPlanBeans, vcActivityItems, limit, locale, fieldsConfigLabelsMap, changeTypes);
		}
		return null;
	}


	/**
	 * Gets the expense activity items
	 * @param costBeans
	 * @param locale
	 * @return
	 */
	private List<ActivityStreamItem> getActivityItemsFromExpenses(List<TCostBean> costBeans, Locale locale) {
		List<ActivityStreamItem> activityStreamItems = null;
		String expenseLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.COST, locale);
		if (costBeans!=null) {
			activityStreamItems = new LinkedList<ActivityStream.ActivityStreamItem>();
			for (TCostBean costBean : costBeans) {
				ActivityStreamItem activityStreaItem=new ActivityStreamItem();
				activityStreaItem.setTransactionID(costBean.getObjectID());
				activityStreaItem.setChangeByName(costBean.getChangedByName());
				Date lastEdit = costBean.getLastEdit();
				activityStreaItem.setDate(lastEdit);
				activityStreaItem.setDateFormatted(formatDateAgo(lastEdit, locale));
				activityStreaItem.setItemID(costBean.getWorkItemID());
				activityStreaItem.setChanges(expenseLabel);
				activityStreamItems.add(activityStreaItem);
			}
		}
		return activityStreamItems;
	}

	/**
	 * Gets the budget/plan activity items
	 * @param budgetBeans
	 * @param locale
	 * @return
	 */
	private List<ActivityStreamItem> getActivityItemsFromBudgetPlan(List<TBudgetBean> budgetBeans, Locale locale) {
		List<ActivityStreamItem> activityStreamItems = null;
		String planLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.PLAN, locale);
		String budgetLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.BUDGET, locale);
		if (budgetBeans!=null) {
			activityStreamItems = new LinkedList<ActivityStream.ActivityStreamItem>();
			for (TBudgetBean budgetBean : budgetBeans) {
				ActivityStreamItem activityStreaItem=new ActivityStreamItem();
				activityStreaItem.setTransactionID(budgetBean.getObjectID());
				activityStreaItem.setChangeByName(budgetBean.getChangedByName());
				Date lastEdit = budgetBean.getLastEdit();
				activityStreaItem.setDate(lastEdit);
				activityStreaItem.setDateFormatted(formatDateAgo(lastEdit, locale));
				activityStreaItem.setItemID(budgetBean.getWorkItemID());
				Integer type = budgetBean.getBudgetType();
				String fieldName = null;
				if (type==null || BUDGET_TYPE.PLANNED_VALUE.equals(type)) {
					fieldName = planLabel;
				} else {
					fieldName = budgetLabel;
				}
				activityStreaItem.setChanges(fieldName);
				activityStreamItems.add(activityStreaItem);
			}
		}
		return activityStreamItems;
	}

	/**
	 * Gets the version control activity items
	 * @param vcActivityItems
	 * @param locale
	 * @return
	 */
	private List<ActivityStreamItem> getActivityItemsFromVersionControl(List<VCActivityItem> vcActivityItems, Locale locale)  {
		List<ActivityStreamItem> validActivityItems=new ArrayList<ActivityStreamItem>();
		for (int i = 0; i < vcActivityItems.size(); i++) {
			VCActivityItem vcActivityItem=vcActivityItems.get(i);
			Set<Integer>  workItemdIDs=VcBL.getWorkItemIDs(vcActivityItem.getRevisionComment(),VersionControlMap.prefixIssueNumber);
			if(workItemdIDs!=null){
				Iterator<Integer> it=workItemdIDs.iterator();
				while (it.hasNext()) {
					Integer key = it.next();
					ActivityStreamItem activityStreamItem=new ActivityStreamItem();
					activityStreamItem.setChangeByName(vcActivityItem.getRevisionAuthor());
					activityStreamItem.setChanges(localizeText("activityStream.vcCommit", locale) + ": " + vcActivityItem.getRevisionNo());
					activityStreamItem.setDate(vcActivityItem.getDate());
					activityStreamItem.setIconName("versionControl.png ");
					activityStreamItem.setDateFormatted(formatDateAgo(vcActivityItem.getDate(), locale));
					activityStreamItem.setItemID(key);
					activityStreamItem.setRepository(vcActivityItem.getRepository());
					activityStreamItem.setRevisionNo(vcActivityItem.getRevisionNo());
					activityStreamItem.setRevisionComment(vcActivityItem.getRevisionComment());
					activityStreamItem.setChangedPaths(vcActivityItem.getChangedPaths());
					validActivityItems.add(activityStreamItem);
				}
			}
		}
		return validActivityItems;
	}

	private List<FlatHistoryBean> getActivityItemsFromVersionControlForHTML(List<VCActivityItem> vcActivityItems, Locale locale)  {
		List<FlatHistoryBean> validActivityItems=new ArrayList<FlatHistoryBean>();
		for (int i = 0; i < vcActivityItems.size(); i++) {
			VCActivityItem vcActivityItem=vcActivityItems.get(i);
			Set<Integer>  workItemdIDs=VcBL.getWorkItemIDs(vcActivityItem.getRevisionComment(),VersionControlMap.prefixIssueNumber);
			if(workItemdIDs!=null){
				Iterator<Integer> it=workItemdIDs.iterator();
				while (it.hasNext()) {
					Integer key = it.next();
					FlatHistoryBean flatHistoryBean=new FlatHistoryBean();
					flatHistoryBean.setChangedByName(vcActivityItem.getRevisionAuthor());
					flatHistoryBean.setRenderType(FlatHistoryBean.RENDER_TYPE.VERSION_CONTROL);
					flatHistoryBean.setLastEdit(vcActivityItem.getDate());
					flatHistoryBean.setIconName("versionControl.png ");
					flatHistoryBean.setDateFormatted(formatDateAgo(vcActivityItem.getDate(), locale));
					flatHistoryBean.setWorkItemID(key);
					flatHistoryBean.setRepository(vcActivityItem.getRepository());
					flatHistoryBean.setRevisionNo(vcActivityItem.getRevisionNo());
					flatHistoryBean.setRevisionComment(vcActivityItem.getRevisionComment());
					flatHistoryBean.setChangedPaths(vcActivityItem.getChangedPaths());
					validActivityItems.add(flatHistoryBean);
				}
			}
		}
		return validActivityItems;
	}

	/**
	 * Get the history activity items
	 * @param historyTransactionList
	 * @param changeTypes
	 * @param fieldsConfigMap
	 * @param locale
	 * @return
	 */
	private List<ActivityStreamItem> getActivityItemsFromHistory(List<THistoryTransactionBean> historyTransactionList,
			List<Integer> changeTypes, Map<Integer, String> fieldsConfigMap, Locale locale)  {
		List<ActivityStreamItem> historyActivityStreams = null;
		if (historyTransactionList!=null) {
			Set<Integer> pseudoHistoryFields = HistoryLoaderBL.getPseudoHistoryFields();
			List<Integer> htIDs=new ArrayList<Integer>();
			historyActivityStreams = new LinkedList<ActivityStream.ActivityStreamItem>();
			for (THistoryTransactionBean historyTransaction : historyTransactionList) {
				htIDs.add(historyTransaction.getObjectID());
				String name=historyTransaction.getChangedByName();
				Date lastEdit=historyTransaction.getLastEdit();
				Integer itemID=historyTransaction.getWorkItem();
				ActivityStreamItem activityStreaItem=new ActivityStreamItem();
				activityStreaItem.setTransactionID(historyTransaction.getObjectID());
				activityStreaItem.setChangeByName(name);
				activityStreaItem.setDate(lastEdit);
				activityStreaItem.setDateFormatted(formatDateAgo(lastEdit, locale));
				activityStreaItem.setItemID(itemID);
				historyActivityStreams.add(activityStreaItem);
			}
			List<TFieldChangeBean> fieldChangeBeans=FieldChangeBL.loadByTransactionIDS(htIDs);
			Map<Integer,List<TFieldChangeBean>> mapFieldChanges=new HashMap<Integer, List<TFieldChangeBean>>();
			List<TFieldChangeBean> list;
			for (int i = 0; i < fieldChangeBeans.size(); i++) {
				TFieldChangeBean fcb=fieldChangeBeans.get(i);
				list=mapFieldChanges.get(fcb.getHistoryTransaction());
				if(list==null){
					list=new ArrayList<TFieldChangeBean>();
				}
				list.add(fcb);
				mapFieldChanges.put(fcb.getHistoryTransaction(),list);
			}
			for (ActivityStreamItem activityStreamItem : historyActivityStreams) {
				List<TFieldChangeBean> fieldChangeBeanList=mapFieldChanges.get(activityStreamItem.getTransactionID());
				StringBuffer sb=new StringBuffer();
				TFieldChangeBean fcb;
				//FIXME fix me!
				if(fieldChangeBeanList!=null) {
					Set<Integer> fieldSet = new HashSet<Integer>();
					for (int j = 0; j < fieldChangeBeanList.size(); j++) {
						fcb=fieldChangeBeanList.get(j);
						Integer fieldID=fcb.getFieldKey();
						if (changeTypes==null || changeTypes.isEmpty() || changeTypes.contains(fieldID)) {
							//if any change type
							if (!fieldSet.contains(fieldID)) {
								//avoid adding cascading field changes more than one times (once for each parameterValue)
								fieldSet.add(fieldID);
								String fieldLabel = fieldsConfigMap.get(fieldID);
								if(j>0){
									sb.append("; ");
								}
								if(fieldLabel!=null){
									sb.append(fieldLabel);
								}
								else{
									if (pseudoHistoryFields.contains(fieldID)) {
										sb.append(LocalizeUtil.getLocalizedTextFromApplicationResources(HistoryLoaderBL.getHistoryFieldKey(fieldID), locale));
									}
								}
							}
						}
					}
					int type = HistoryLoaderBL.getType(fieldSet);
					activityStreamItem.setIconName(HistoryLoaderBL.getIconByType(type));
				}else{
					LOGGER.info("No history found for transaction : "+activityStreamItem.getTransactionID());
				}
				activityStreamItem.setChanges(sb.toString());
			}
		}
		return historyActivityStreams;
	}

	/**
	 * Creates the activity items for grid view
	 * @param historyTransactionList
	 * @param expenseList
	 * @param budgetPlanList
	 * @param vcActivityItems
	 * @param limit
	 * @param locale
	 * @param fieldsConfigMap
	 * @param changeTypes
	 * @return
	 */
	private List<ActivityStreamItem> createActivityItems(List<THistoryTransactionBean> historyTransactionList,
			List<TCostBean> expenseList, List<TBudgetBean> budgetPlanList,
			List<VCActivityItem> vcActivityItems, int limit, Locale locale, Map<Integer, String> fieldsConfigMap, List<Integer> changeTypes){
		List<ActivityStreamItem> allActivityItems=new ArrayList<ActivityStreamItem>();
		if (historyTransactionList!=null) {
			allActivityItems.addAll(getActivityItemsFromHistory(historyTransactionList, changeTypes, fieldsConfigMap, locale));
		}
		if (expenseList!=null) {
			allActivityItems.addAll(getActivityItemsFromExpenses(expenseList, locale));
		}
		if (budgetPlanList!=null) {
			allActivityItems.addAll(getActivityItemsFromBudgetPlan(budgetPlanList, locale));
		}
		if (vcActivityItems!=null) {
			allActivityItems.addAll(getActivityItemsFromVersionControl(vcActivityItems, locale));
		}
		Collections.sort(allActivityItems);
		if (allActivityItems.size()>limit) {
			allActivityItems = allActivityItems.subList(0, limit);
		}
		Set<Integer> itemIDSet = new HashSet<Integer>();
		for (ActivityStreamItem activityStreamItem : allActivityItems) {
			Integer workItemID =  activityStreamItem.getItemID();
			itemIDSet.add(workItemID);
		}
		List<TWorkItemBean> workItemBeans = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(itemIDSet));
		Map<Integer, TWorkItemBean> itemsMap=new HashMap<Integer, TWorkItemBean>();
		for (TWorkItemBean workItemBean : workItemBeans) {
			itemsMap.put(workItemBean.getObjectID(), workItemBean);
		}
		Map<Integer, String> projectSpecificItemIDsMap = null;
		boolean useProjectSpecificID = false;
		if (ApplicationBean.getApplicationBean().getSiteBean().getProjectSpecificIDsOn()) {
			useProjectSpecificID = true;
			projectSpecificItemIDsMap = ItemBL.getProjectSpecificIssueIDsMap(workItemBeans);
		}
		//validate and limit
		List<ActivityStreamItem> validActivityItems=new LinkedList<ActivityStreamItem>();
		for (ActivityStreamItem activityStreamItem:allActivityItems) {
			TWorkItemBean workItemBean=itemsMap.get(activityStreamItem.getItemID());
			if (workItemBean!=null){
				activityStreamItem.setItemTitle(workItemBean.getSynopsis());
				TProjectBean projectBean = LookupContainer.getProjectBean(workItemBean.getProjectID());
				if (projectBean!=null) {
					activityStreamItem.setProject(projectBean.getLabel());
				}
				String itemID = null;
				if (useProjectSpecificID) {
					itemID = projectSpecificItemIDsMap.get(workItemBean.getObjectID());
				} else {
					itemID = workItemBean.getObjectID().toString();
				}
				activityStreamItem.setItemPrefixID(itemID);
				validActivityItems.add(activityStreamItem);
				/*if(validActivityItems.size()==limit){
					break;
				}*/
			}
		}
		return validActivityItems;
	}

	/**
	 * Creates the activity items for html view
	 * @param workItemBeans
	 * @param historyTransactionList
	 * @param expenseList
	 * @param budgetPlanList
	 * @param vcActivityItems
	 * @param limit
	 * @param locale
	 * @param fieldsConfigMap
	 * @param changeTypes
	 * @return
	 */
	public List<FlatHistoryBean> createActivityItemsForHTML(List<TWorkItemBean> workItemBeans, List<FlatHistoryBean> historyTransactionList,
			List<TCostBean> expenseList, List<TBudgetBean> budgetPlanList,
			List<VCActivityItem> vcActivityItems, int limit, Locale locale, Map<Integer, String> fieldsConfigMap, List<Integer> changeTypes) {
		List<FlatHistoryBean> allActivityItems=new ArrayList<FlatHistoryBean>();
		Map<Integer, TWorkItemBean> itemsMap=new HashMap<Integer, TWorkItemBean>();
		Map<Integer, Integer> itemIDToProjectIDMap = new HashMap<Integer, Integer>();
		Set<Integer> projectIDs = new HashSet<Integer>();
		List<Integer> itemIDs = new LinkedList<Integer>();
		if (workItemBeans!=null) {
			for (TWorkItemBean workItemBean : workItemBeans) {
				Integer itemID = workItemBean.getObjectID();
				itemIDs.add(itemID);
				Integer projectID = workItemBean.getProjectID();
				itemsMap.put(itemID, workItemBean);
				itemIDToProjectIDMap.put(itemID, projectID);
				projectIDs.add(projectID);
			}
		}
		Map<Integer, String> projectSpecificItemIDsMap = null;
		boolean useProjectSpecificID = false;
		if(ApplicationBean.getApplicationBean().getSiteBean().getProjectSpecificIDsOn()) {
			useProjectSpecificID = true;
			projectSpecificItemIDsMap = ItemBL.getProjectSpecificIssueIDsMap(workItemBeans);
		}
		if (historyTransactionList!=null) {
			allActivityItems.addAll(historyTransactionList);
		}
		if (expenseList!=null) {
			allActivityItems.addAll(ExpenseBL.initCostFlatHistoryBeans(expenseList, itemsMap, locale));
		}
		if (vcActivityItems!=null) {
			allActivityItems.addAll(getActivityItemsFromVersionControlForHTML(vcActivityItems, locale));
		}
		Collections.sort(allActivityItems);
		if (allActivityItems.size()>limit) {
			allActivityItems = allActivityItems.subList(0, limit);
		}
		//validate and limit
		List<FlatHistoryBean> validActivityItems=new ArrayList<FlatHistoryBean>();
		for (FlatHistoryBean flatHistoryBean:allActivityItems) {
			TWorkItemBean workItemBean=itemsMap.get(flatHistoryBean.getWorkItemID());
			if(workItemBean!=null){
				TProjectBean projectBean = LookupContainer.getProjectBean(workItemBean.getProjectID());//projectsMap.get(workItemBean.getProjectID());
				if (projectBean!=null) {
					flatHistoryBean.setProject(projectBean.getLabel());
				}
				flatHistoryBean.setTitle(workItemBean.getSynopsis());
				String itemID = null;
				if (useProjectSpecificID) {
					itemID = projectSpecificItemIDsMap.get(workItemBean.getObjectID());
				} else {
					itemID = workItemBean.getObjectID().toString();
				}
				flatHistoryBean.setItemID(itemID);
				flatHistoryBean.setDateFormatted(formatDateAgo(flatHistoryBean.getLastEdit(), locale));
				validActivityItems.add(flatHistoryBean);
				/*if(validActivityItems.size()==limit){
					break;
				}*/
			}
		}
		return validActivityItems;
	}

	protected String formatDateAgo(Date d, Locale locale){
		String result="";
		long time=d.getTime();
		long now=new Date().getTime();
		long milise=now-time;
		long minutes=milise/60000;
		if(minutes<1){
			result=localizeText("activityStream.minuteAgo", locale);
		}else if(minutes<60){
			result=localizeText("activityStream.minutesAgo", locale,new Object[]{minutes});
		}else{
			long hours=minutes/60;
			if(hours<2){
				result=localizeText("activityStream.hourAgo", locale);
			}else if(hours<25){
				result=localizeText("activityStream.hoursAgo", locale,new Object[]{hours});
			}else{
				long days=hours/24;
				if(days<2){
					result=localizeText("activityStream.dayAgo", locale);
				}else if(days<32){
					result=localizeText("activityStream.daysAgo", locale,new Object[]{days});
				}else{
					long months=days/31;
					if(months<2){
						result=localizeText("activityStream.monthAgo", locale);
					}else if(months<12){
						result=localizeText("activityStream.monthsAgo", locale,new Object[]{months});
					}else{
						long years=months/12;
						if(years<2){
							result=localizeText("activityStream.yearAgo", locale);
						}else {
							result=localizeText("activityStream.yearsAgo", locale,new Object[]{years});
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 *
	 * Transfer object for a grid row in activity stream
	 *
	 */
	public static class ActivityStreamItem implements Comparable<ActivityStreamItem>{
		private Date date;
		private String dateFormatted;
		private Integer itemID;
		private String itemPrefixID;
		private String itemTitle;
		private String changeByName;
		private String changes;
		private Integer transactionID;
		private Integer projectID;
		private String project;
		private String iconName;


		//version control
		private String revisionNo;
		private String revisionComment;
		private String repository;
		private List<FileDiffTO> changedPaths;


		public ActivityStreamItem(){
			this.iconName="customField.gif";
		}

		@Override
		public int compareTo(ActivityStreamItem activityItem) {
			return activityItem.date.compareTo(this.date);
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null) {
				return false;
			}
			if(!(obj instanceof ActivityStreamItem)) {
				return false;
			}
			if( ((ActivityStreamItem)obj).getDate().compareTo(this.date) == 0) {
				return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((date == null) ? 0 : date.hashCode());
			return result;
		}


		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}

		public Integer getItemID() {
			return itemID;
		}

		public void setItemPrefixID(String itemPrefixID) {
			this.itemPrefixID = itemPrefixID;
		}

		public String getItemPrefixID() {
			return itemPrefixID;
		}

		public void setItemID(Integer itemID) {
			this.itemID = itemID;
		}

		public String getItemTitle() {
			return itemTitle;
		}

		public void setItemTitle(String itemTitle) {
			this.itemTitle = itemTitle;
		}

		public String getChangeByName() {
			return changeByName;
		}

		public void setChangeByName(String changeByName) {
			this.changeByName = changeByName;
		}

		public String getChanges() {
			return changes;
		}

		public void setChanges(String changes) {
			this.changes = changes;
		}

		public String getDateFormatted() {
			return dateFormatted;
		}

		public void setDateFormatted(String dateFormatted) {
			this.dateFormatted = dateFormatted;
		}

		public Integer getTransactionID() {
			return transactionID;
		}

		public void setTransactionID(Integer transactionID) {
			this.transactionID = transactionID;
		}
		public Integer getProjectID() {
			return projectID;
		}

		public void setProjectID(Integer projectID) {
			this.projectID = projectID;
		}

		public String getProject() {
			return project;
		}

		public void setProject(String project) {
			this.project = project;
		}

		public String getIconName() {
			return iconName;
		}

		public void setIconName(String iconName) {
			this.iconName = iconName;
		}

		public String getRevisionNo() {
			return revisionNo;
		}

		public void setRevisionNo(String revisionNo) {
			this.revisionNo = revisionNo;
		}

		public String getRevisionComment() {
			return revisionComment;
		}

		public void setRevisionComment(String revisionComment) {
			this.revisionComment = revisionComment;
		}

		public String getRepository() {
			return repository;
		}

		public void setRepository(String repository) {
			this.repository = repository;
		}

		public List<FileDiffTO> getChangedPaths() {
			return changedPaths;
		}

		public void setChangedPaths(List<FileDiffTO> changedPaths) {
			this.changedPaths = changedPaths;
		}
	}
}
