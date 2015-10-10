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

package com.aurel.track.itemNavigator;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.ClobBL;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.FilterFacade;
import com.aurel.track.admin.customize.category.filter.FilterFacadeFactory;
import com.aurel.track.admin.customize.category.filter.MenuitemFilterBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacadeFactory;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemLinksUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadResponsibleItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.CssStyleBean;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.lists.systemOption.PriorityBL;
import com.aurel.track.admin.customize.lists.systemOption.SeverityBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TBasketBean;
import com.aurel.track.beans.TCLOBBean;
import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TLastExecutedQueryBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TMenuitemQueryBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TPersonBasketBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PersonBasketDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.massOperation.MassOperationBL;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.operation.BasketBL;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuBL;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuJSON;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuTO;
import com.aurel.track.itemNavigator.lastExecuted.LastExecutedBL;
import com.aurel.track.itemNavigator.layout.LayoutTO;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.itemNavigator.navigator.Navigator;
import com.aurel.track.itemNavigator.viewPlugin.ViewDescriptorBL;
import com.aurel.track.itemNavigator.viewPlugin.ViewPluginBL;
import com.aurel.track.mobile.MobileBL;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.dashboard.BasePluginDashboardBL;
import com.aurel.track.report.dashboardConfig.IPluginDashboard;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanExpandContext;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.resources.ResourceBundleManager;
import com.aurel.track.screen.dashboard.bl.DashboardUtil;
import com.aurel.track.screen.dashboard.bl.runtime.DashboardFieldRuntimeBL;
import com.aurel.track.tql.TqlBL;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.StringArrayParameterUtils;

/**
 * @author adib
 */
public class ItemNavigatorBL {
	private static final Logger LOGGER = LogManager.getLogger(ItemNavigatorBL.class);

	public static final String USED_STATUS_LIST="usedStatusList";
	public static final int MAX_ISSUE_NO=1000;

	public static interface QUERY_TYPE {
		public static int INSTANT= 0;
		public static int SAVED = 1;
		public static int DASHBOARD = 2;
		//public static int PREDEFINED = 3;
		public static int BASKET = 4;
		public static int PROJECT_RELEASE= 5;
		public static int SCHEDULED= 6;
		public static int LUCENE_SEARCH= 7;
		public static int STATUS= 8;
		public static int PSEUDO=1001;
	}

	public static final int SECTION_STATUS=QUERY_TYPE.STATUS;

	public static interface PSEUD_QUERY {
		public static int WIKI=1;
	}
	//public static final String ISSUE_LIST_VIEW_WBS="com.trackplus.itemNavigator.WBSViewPlugin";


	//private static LastExecutedQueryDAO lastExecutedQueryDAO = DAOFactory.getFactory().getLastExecutedQueryDAO();




	public static boolean hasQueryParameters(Integer queryType,Integer queryID){
		return false;
	}
	public static String prepareInitData(ApplicationBean appBean,TPersonBean personBean,Locale locale,QueryContext queryContext,Map<String, Object> session, boolean lite, Integer workItemID,Integer actionID,boolean forceAllItems,boolean useIssueTypeIcon){
		Navigator nav=null;
		List<ILabelBean> usedStatusList = getUsedStatusList(personBean, locale, session);
		nav =ItemNavigatorFilterBL.createNavigator(personBean, usedStatusList, locale);
		return prepareInitData(nav, appBean, personBean, locale, queryContext, session, lite,workItemID,actionID,forceAllItems,useIssueTypeIcon);
	}

	public static List<ILabelBean> getUsedStatusList(TPersonBean personBean, Locale locale, Map<String, Object> session) {
		List<ILabelBean> usedStatusList=(List<ILabelBean>)session.get(USED_STATUS_LIST);
		if(usedStatusList==null){
			List<TProjectBean> projectList = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
			Integer[] projectTypeIDs = ProjectTypesBL.getProjectTypeIDsForProjects(projectList);
			usedStatusList= StatusBL.loadAllowedByProjectTypesAndIssueTypes(projectTypeIDs, null, locale);
			session.put(USED_STATUS_LIST,usedStatusList);
		}
		return usedStatusList;
	}

	public static String prepareInitData(Navigator nav,ApplicationBean appBean,TPersonBean personBean,Locale locale,QueryContext queryContext,Map<String, Object> session, boolean lite, Integer workItemID,Integer actionID,boolean forceAllItems,boolean useIssueTypeIcon){
		List<IssueListViewDescriptor> descriptors = null;
		Integer lastSelectedNavigator=null;
		String lastSelectedView=null;
		IssueListViewPlugin plugin=null;
		boolean plainData=false;
		boolean includeLongFields=false;
		List<FilterInMenuTO> lastQueries=null;
		String extraJSON=null;
		boolean paginate=false;
		boolean gantt=false;
		if (lite) {
			plugin=new BaseIssueListViewPlugin();
		} else {
			descriptors = ViewDescriptorBL.getViewDescriptors(personBean/*, appBean*/);
			IssueListViewDescriptor descriptor = ViewDescriptorBL.getDescriptor(personBean, descriptors, MobileBL.isMobileApp(session), queryContext);
			lastSelectedView = descriptor.getId();
			/*
			lastSelectedView= personBean.getLastSelectedView();
			if(lastSelectedView!=null) {
				//verify lastSelectedView
				boolean found = false;
				for (int i = 0; i < descriptors.size(); i++) {
					IssueListViewDescriptor issueListViewDescriptor = (IssueListViewDescriptor) descriptors.get(i);
					if (issueListViewDescriptor.getId().equals(lastSelectedView)){
						found=true;
						break;
					}
				}
				if(!found){
					lastSelectedView=null;
				}
			}
			if(lastSelectedView==null){
				lastSelectedView=descriptors.get(0).getId();
				personBean.setLastSelectedView(lastSelectedView);
				PersonBL.saveSimple(personBean);
			}
			IssueListViewDescriptor descriptor;
			//If client not mobile or tablet last selected view JSON is returned
			if(!MobileBL.isMobileApp(session)) {
				descriptor = ViewDescriptorBL.getDescriptor(lastSelectedView);
			}else {
				String wbsView = ISSUE_LIST_VIEW_WBS;
				descriptor = ViewDescriptorBL.getDescriptor(wbsView);
			}
			if(descriptor==null){
				lastSelectedView=descriptors.get(0).getId();
				descriptor=ViewDescriptorBL.getDescriptor(lastSelectedView);
			}*/
			plugin=ViewPluginBL.getPlugin(descriptor.getTheClassName());
			plainData=descriptor.isPlainData();
			if (plugin!=null && plainData) {
				paginate=personBean.isPaginate();
			}
			includeLongFields=descriptor.isUseLongFields();
			gantt = descriptor.isGantt();
			lastQueries=FilterInMenuBL.getLastExecutedQueries(personBean,locale);
			//session.put(FilterBL.LAST_EXECUTED_FILTERS, lastQueries);
			session.put(FilterBL.LAST_EXECUTED_FILTERS_JSON, FilterInMenuJSON.encodeFiltersInMenu(lastQueries));
			lastSelectedNavigator=(Integer)session.get(ItemNavigatorAction.LAST_SELECTED_NAVIGATOR);
		}
		List<ReportBean> reportBeanList =null;
		boolean tooManyItems=false;
		if(!paginate) {
			try{
				if (gantt) {
					reportBeanList = ItemNavigatorBL.executeGanttQuery(personBean, locale, queryContext);
				} else {
					reportBeanList = ItemNavigatorBL.executeQuery(personBean, locale, queryContext);
				}
			}catch (TooManyItemsToLoadException e){
				tooManyItems=true;
				LOGGER.info("Number of items to load " + e.getItemCount());
			}
		}
		extraJSON= plugin.getExtraJSON(session,reportBeanList, queryContext, false);
		Set<Integer> exclusiveShortFields=plugin.getExclusiveShortFields(personBean, queryContext);
		boolean includePercentDone=plugin.includePercentDone();
		LayoutTO layoutTO = null;
		if(exclusiveShortFields!=null){
			layoutTO = NavigatorLayoutBL.loadExclusiveFields(exclusiveShortFields, locale);
		}else{
			layoutTO = NavigatorLayoutBL.loadLayout(personBean, locale, queryContext.getQueryType(), queryContext.getQueryID(), false);
		}
		int count=reportBeanList==null?0:reportBeanList.size();
		int totalCount=count;
		Integer overflowItems=0;
		if(forceAllItems==false&& count>MAX_ISSUE_NO){
			overflowItems=count;
			count=MAX_ISSUE_NO;
			reportBeanList= ItemNavigatorBL.cutItems(reportBeanList, MAX_ISSUE_NO);
		}
		ReportBeanExpandContext reportBeanExpandContext=ReportBeanExpandContext.getExpandContext(session);
		reportBeanExpandContext.setGroupingSorting(reportBeanExpandContext, layoutTO);
		reportBeanExpandContext=plugin.updateReportBeanExpandContext(personBean,reportBeanList, reportBeanExpandContext, session, queryContext, false);
		ReportBeans reportBeans=new ReportBeans(reportBeanList, locale, reportBeanExpandContext, true, plainData);
		if(reportBeanList!=null) {
			int issueCount = reportBeanList.size();
			reportBeanList.clear();
			reportBeanList = null;
		}
		if(paginate) {
			totalCount=-1;
			count=-1;
		}
		Integer queryFieldCSS=ItemNavigatorBL.getQueryFieldListCss(personBean.getObjectID(),queryContext);
		Map<Integer,String> cssRules=null;
		if(queryFieldCSS!=null){
			cssRules=ItemNavigatorBL.getCssRules(queryFieldCSS);
		}

		boolean showDragDropInfoMsg = false;
		if(session.get("showDragDropInfoMsg") == null) {
			session.put("showDragDropInfoMsg", true);
			showDragDropInfoMsg = true;
		}
		String initData=ItemNavigatorJSON.encodeJSON(layoutTO, reportBeans, nav,lastSelectedNavigator, lastQueries,totalCount, count,overflowItems, descriptors, lastSelectedView,
				appBean, locale,queryContext,personBean,workItemID,actionID,
				queryFieldCSS,cssRules,includeLongFields,plainData,extraJSON,exclusiveShortFields,includePercentDone,tooManyItems, showDragDropInfoMsg,useIssueTypeIcon);
		int issueCount = reportBeans.getCount();
		reportBeanList = null;
		return initData;
	}

	public static List<ReportBean> cutItems(List<ReportBean> reportBeansList,int maxIssuesNo){
		List<ReportBean> result=new ArrayList<ReportBean>();
		if (reportBeansList.size()>maxIssuesNo) {
			Iterator<ReportBean> iterator = reportBeansList.iterator();
			int i=0;
			while (iterator.hasNext() && i<maxIssuesNo) {
				ReportBean reportBean = iterator.next();
				i++;
				result.add(reportBean);
			}
		} else {
			result=reportBeansList;
		}
		return result;
	}
	public static List<ReportBean> cutItems(List<ReportBean> reportBeansList, int start, int limit){
		if(limit==0){
			limit=10;
		}
		List<ReportBean> result=new ArrayList<ReportBean>();
		if (reportBeansList!=null) {
			if (reportBeansList.size()>limit&&start<reportBeansList.size()) {
				Iterator<ReportBean> iterator = reportBeansList.iterator();
				for(int i=start;i<reportBeansList.size();i++){
					if(result.size()==limit){
						break;
					}
					ReportBean reportBean =reportBeansList.get(i);
					result.add(reportBean);
				}
			} else {
				result=reportBeansList;
			}
		}
		return result;
	}


	private static List<ReportBean> executeDashboardQuery(Integer queryType,Integer queryID,TPersonBean personBean,Locale locale,
			 Map<String,String> filterParams) throws TooManyItemsToLoadException {
		String dashboardIDStr=filterParams.get(BasePluginDashboardBL.DASHBOARD_ID);
		Integer dashboardID=null;
		try{
			dashboardID=Integer.valueOf(dashboardIDStr);
		}catch (Exception ex){}
		TDashboardFieldBean dashboardItem=(TDashboardFieldBean) DashboardFieldRuntimeBL.getInstance().loadField(dashboardID);
		if(dashboardItem==null){
			LOGGER.info("Dashboard with id:"+dashboardID+" not found anymore in DB");
			return null;
		}
		IPluginDashboard dashboard= DashboardUtil.getPlugin(dashboardItem);
		Map<String,String> configParamsMap=dashboardItem.getParametres();
		if(dashboard!=null){
			return dashboard.getIssues(configParamsMap,filterParams,personBean,locale);
		}
		return null;
	}

	public static ReportBeans executeQuery(TPersonBean personBean,Locale locale,QueryContext queryContext,
			ReportBeanExpandContext reportBeanExpandContext, boolean plainData){
		List<ReportBean> reportBeanList;
		try{
			reportBeanList= executeQuery(personBean, locale, queryContext);
		}catch (TooManyItemsToLoadException e){
			LOGGER.info("Number of items to load " + e.getItemCount());
			reportBeanList=new ArrayList<ReportBean>();
		}
		return new ReportBeans(reportBeanList, locale, reportBeanExpandContext, true, plainData);
	}

	/**
	 * Gets the reportBeans for a query context
	 * @param personBean
	 * @param locale
	 * @param queryContext
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	public static List<ReportBean> executeQuery(TPersonBean personBean,Locale locale,QueryContext queryContext) throws TooManyItemsToLoadException{
		Integer queryType=queryContext.getQueryType();
		Integer queryID=queryContext.getQueryID();
		List<ReportBean> reportBeanList = new ArrayList<ReportBean>();
		if(queryType!=null){
			switch (queryType.intValue()){
				case QUERY_TYPE.INSTANT:{
					reportBeanList = executeInstantFilter(personBean, locale, queryContext);
					break;
				}
				case QUERY_TYPE.SAVED:{
					reportBeanList = executeSavedFilter(personBean,locale,queryContext);
					break;
				}
				case QUERY_TYPE.DASHBOARD:{
					reportBeanList = executeDashboardQuery(queryType, queryID, personBean, locale, queryContext.getDashboardParams());
					break;
				}
				case QUERY_TYPE.BASKET:{
					reportBeanList = executeBasketQuery(queryID, personBean, locale);
					break;
				}
				case QUERY_TYPE.STATUS:{
					reportBeanList = executeStatusQuery(queryID, personBean, locale);
					break;
				}
				case QUERY_TYPE.PROJECT_RELEASE:{
					reportBeanList = executeProjectReleaseQuery(queryID, personBean, locale);
					break;
				}
				case QUERY_TYPE.SCHEDULED:{
					reportBeanList =executeScheduledQuery(queryID, personBean, locale);
					break;
				}
				case QUERY_TYPE.LUCENE_SEARCH:{
					reportBeanList = executeLuceneSearch(queryContext.getFilterExpression(), personBean, locale);
					break;
				}
			}
		}
		return reportBeanList;
	}
	
	/**
	 * Gets the reportBeans for a query context extended with Gantt related report beans 
	 * @param personBean
	 * @param locale
	 * @param queryContext
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	public static List<ReportBean> executeGanttQuery(TPersonBean personBean,Locale locale,QueryContext queryContext) throws TooManyItemsToLoadException{
		List<TWorkItemBean> baseWorkItemBeans = getQueryItems(personBean, locale, queryContext);
		return loadGanttItems(baseWorkItemBeans, personBean, locale);
	}
	
	/**
	 * Gets the reportBeans for an instant filter extended with Gantt related report beans 
	 * @param filterUpperTO
	 * @param qNode
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	public static List<ReportBean> executeGanttInstantQuery(FilterUpperTO filterUpperTO, QNode qNode, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		List<TWorkItemBean> baseWorkItemBeans = TreeFilterExecuterFacade.getInstantTreeFilterWorkItemBeans(filterUpperTO, qNode, null, personBean, locale, false, null, null, null, null);
		return loadGanttItems(baseWorkItemBeans, personBean, locale);
	}
	
	/**
	 * Loads the base items extended with Gantt related report beans
	 * @param baseWorkItemBeans
	 * @return
	 */
	private static List<ReportBean> loadGanttItems(List<TWorkItemBean> baseWorkItemBeans, TPersonBean personBean, Locale locale) {
		Set<Integer> baseItemIDs = GeneralUtils.createIntegerSetFromBeanList(baseWorkItemBeans);
		Set<Integer> allItemIDs = new HashSet<Integer>();
		LoadItemLinksUtil.loadAllDependentItems(baseWorkItemBeans, null, null, allItemIDs);
		int[] workItemIDs=GeneralUtils.createIntArrFromSet(allItemIDs);
		if(workItemIDs!=null && workItemIDs.length>0){
			List<ReportBean> reportBeanList = LoadItemIDListItems.getReportBeansByWorkItemIDs(workItemIDs, true, personBean.getObjectID(), locale, true);
			for (ReportBean reportBean : reportBeanList) {
				//mark the extended items
				if (!baseItemIDs.contains(reportBean.getWorkItemBean().getObjectID())) {
					reportBean.setExtendedItem(true);
				}
			}
			return reportBeanList;
		}
		return null;
	}
	
	/**
	 * Gets the item beans for a query context
	 * @param personBean
	 * @param locale
	 * @param queryContext
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	public static List<TWorkItemBean> getQueryItems(TPersonBean personBean, Locale locale, QueryContext queryContext) throws TooManyItemsToLoadException{
		Integer queryType=queryContext.getQueryType();
		Integer queryID=queryContext.getQueryID();
		List<TWorkItemBean> itemBeanList = null;
		if(queryType!=null){
			switch (queryType.intValue()){
				case QUERY_TYPE.INSTANT:{
					itemBeanList = getInstantFilterItems(personBean, locale, queryContext);
					break;
				}
				case QUERY_TYPE.SAVED:{
					itemBeanList = getSavedFilterItems(personBean,locale,queryContext);
					break;
				}
				case QUERY_TYPE.DASHBOARD:{
					List<ReportBean> reportBeanList = executeDashboardQuery(queryType, queryID, personBean, locale, queryContext.getDashboardParams());
					if (reportBeanList!=null) {
						itemBeanList = new ArrayList<TWorkItemBean>(reportBeanList.size());
						for (ReportBean reportBean : reportBeanList) {
							itemBeanList.add(reportBean.getWorkItemBean());
						}
					}
					break;
				}
				case QUERY_TYPE.BASKET:{
					itemBeanList = getBasketItems(queryID, personBean, locale);
					break;
				}
				case QUERY_TYPE.STATUS:{
					itemBeanList = getStatusItems(queryID, personBean, locale);
					break;
				}
				case QUERY_TYPE.PROJECT_RELEASE:{
					itemBeanList = getProjectReleaseItems(queryID, personBean, locale);
					break;
				}
				case QUERY_TYPE.SCHEDULED:{
					itemBeanList = getScheduledItems(queryID, personBean, locale);
					break;
				}
				case QUERY_TYPE.LUCENE_SEARCH:{
					itemBeanList = getLuceneItems(queryContext.getFilterExpression(), personBean, locale);
					break;
				}
			}
		}
		return itemBeanList;
	}

	
	
	/**
	 * Returns the reportBeans of an instant filter
	 * @param personBean
	 * @param locale
	 * @param queryContext
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static List<ReportBean> executeInstantFilter(TPersonBean personBean, Locale locale,
			QueryContext queryContext) throws TooManyItemsToLoadException {
		FilterExecuterFacade filterExecuterFacade = FilterExecuterFacadeFactory.getInstance().getFilterExecuterFacade(queryContext.getQueryID());
		if (filterExecuterFacade!=null) {
			List<ErrorData> errors = new LinkedList<ErrorData>();
			return filterExecuterFacade.getInstantFilterReportBeanList(queryContext.getFilterExpression(), null, locale,
					personBean, errors, null, null, false);
		}
		return null;
	}
	
	/**
	 * Returns the item beans of an instant filter
	 * @param personBean
	 * @param locale
	 * @param queryContext
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static List<TWorkItemBean> getInstantFilterItems(TPersonBean personBean, Locale locale,
			QueryContext queryContext) throws TooManyItemsToLoadException {
		FilterExecuterFacade filterExecuterFacade = FilterExecuterFacadeFactory.getInstance().getFilterExecuterFacade(queryContext.getQueryID());
		if (filterExecuterFacade!=null) {
			List<ErrorData> errors = new LinkedList<ErrorData>();
			return filterExecuterFacade.getInstantFilterWorkItemBeans(queryContext.getFilterExpression(), null, locale, personBean, errors, false, null, null, null, null);
		}
		return null;
	}
	
	

	/**
	 * Returns the reportBeans of a saved filter
	 * @param personBean
	 * @param locale
	 * @param queryContext
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static List<ReportBean> executeSavedFilter(TPersonBean personBean, Locale locale,
			QueryContext queryContext) throws TooManyItemsToLoadException{
		List<ErrorData> errors = new LinkedList<ErrorData>();
		return TreeFilterExecuterFacade.getSavedFilterReportBeanList(queryContext.getQueryID(),
				locale,personBean, errors, null, null);

	}

	/**
	 * Returns the item beans of a saved filter
	 * @param personBean
	 * @param locale
	 * @param queryContext
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static List<TWorkItemBean> getSavedFilterItems(TPersonBean personBean, Locale locale,
			QueryContext queryContext) throws TooManyItemsToLoadException{
		List<ErrorData> errors = new LinkedList<ErrorData>();
		return TreeFilterExecuterFacade.getSavedFilterWorkItemBeans(queryContext.getQueryID(), locale, personBean, errors, false);
	}
	
	/**
	 * Returns the reportBeans of a basket
	 * @param basketID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static List<ReportBean> executeBasketQuery(Integer basketID,TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		int[] workItemIDs = getBasketItemIDs(basketID, personBean, locale);
		return LoadItemIDListItems.getReportBeansByWorkItemIDs(workItemIDs, false, personBean.getObjectID(), locale, true);
	}
	
	/**
	 * Returns the item beans of a basket
	 * @param basketID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static List<TWorkItemBean> getBasketItems(Integer basketID,TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		int[] workItemIDs = getBasketItemIDs(basketID, personBean, locale);
		return LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(workItemIDs, personBean.getObjectID(), false, false, false);
	}
	
	/**
	 * Returns the reportBeans of a basket
	 * @param basketID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static int[] getBasketItemIDs(Integer basketID,TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		Set<Integer> workItemIDsSet=new HashSet<Integer>();
		if(basketID==null||basketID==TBasketBean.BASKET_TYPES.IN_BASKET){
			List<TWorkItemBean> respItems=LoadResponsibleItems.loadResponsibleWorkItems(personBean);
			List<TWorkItemBean> items=BasketBL.filterWorkItems(respItems,TBasketBean.BASKET_TYPES.IN_BASKET,personBean.getObjectID());
			if(items!=null && !items.isEmpty()){
				for (int i=0;i<items.size();i++){
					workItemIDsSet.add(items.get(i).getObjectID());
				}
			}
		}else{
			PersonBasketDAO personBasketDAO=DAOFactory.getFactory().getPersonBasketDAO();
			List<TPersonBasketBean> personBasketList=personBasketDAO.loadByBasketAndPerson(basketID,personBean.getObjectID());
			if(personBasketList!=null){
				TPersonBasketBean personBasketBean;
				for(int i=0;i<personBasketList.size();i++){
					personBasketBean=personBasketList.get(i);
					workItemIDsSet.add(personBasketBean.getWorkItem());
				}
			}
		}
		return GeneralUtils.createIntArrFromSet(workItemIDsSet);
	}
	
	
	
	/**
	 * Returns the reportBeans of a status
	 * @param statusID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static List<ReportBean> executeStatusQuery(Integer statusID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		filterUpperTO.setSelectedStates(new Integer[] {statusID});
		return TreeFilterExecuterFacade.getInstantTreeFilterReportBeanListNoReport(filterUpperTO, null, null, personBean, locale, null, null);
	}
	
	/**
	 * Returns the item beans of a status
	 * @param statusID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static List<TWorkItemBean> getStatusItems(Integer statusID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		filterUpperTO.setSelectedStates(new Integer[] {statusID});
		return TreeFilterExecuterFacade.getInstantTreeFilterWorkItemBeans(filterUpperTO, null, null, personBean, locale, false, null, null, null, null);
	}
	
	/**
	 * Returns the reportBeans of a project/release
	 * @param entityID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static List<ReportBean> executeProjectReleaseQuery(Integer entityID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		if (entityID!=null){
			FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(entityID, true, true, false);
			return LoadTreeFilterItems.getTreeFilterReportBeans(filterUpperTO, null, null, personBean, locale);
		}
		return null;
	}
	
	/**
	 * Returns the item beans of a project/release
	 * @param entityID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	private static List<TWorkItemBean> getProjectReleaseItems(Integer entityID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException{
		if (entityID!=null){
			FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(entityID, true, true, false);
			return LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
		}
		return null;
	}
	
	/**
	 * FIXME implement
	 * Returns the reportBeans of a scheduled query
	 * @param queryID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	private static List<ReportBean> executeScheduledQuery(Integer queryID,TPersonBean personBean, Locale locale){
		List<ReportBean> reportBeansList=null;
		if(queryID==null){
			return null;
		}
		switch(queryID){
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.TODAY:{
				Date today= DateTimeUtils.getToday();
				break;
			}
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.TOMORROW:{
				Date tomorrow=DateTimeUtils.getTomorrow();
				break;
			}
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_WEEK:{
				Date startOfNextWeek= DateTimeUtils.getStartOfNextWeek();
				Date endOfNextWeek= DateTimeUtils.getEndOfNextWeek();

				break;
			}
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_MONTH:{
				Date startOfNextMonth= DateTimeUtils.getStartOfNextMonth();
				Date endOfNextMonth= DateTimeUtils.getEndOfNextMonth();
				break;
			}
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.NOT_SCHEDULED:{
				Date startDate=null;
				Date endDate=null;
				break;
			}
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.OVERDUE:{
				/*if(endDate!=null&&endDate.getTime()<DateTimeUtils.getToday().getTime()){
					result.add(reportBean);
				}*/
				break;
			}
		}
		return reportBeansList;
	}

	/**
	 * FIXME implement
	 * Returns the item beans of a scheduled query
	 * @param queryID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	private static List<TWorkItemBean> getScheduledItems(Integer queryID,TPersonBean personBean, Locale locale){
		List<TWorkItemBean> itemBeansList=null;
		if(queryID==null){
			return null;
		}
		switch(queryID){
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.TODAY:{
				Date today= DateTimeUtils.getToday();
				break;
			}
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.TOMORROW:{
				Date tomorrow=DateTimeUtils.getTomorrow();
				break;
			}
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_WEEK:{
				Date startOfNextWeek= DateTimeUtils.getStartOfNextWeek();
				Date endOfNextWeek= DateTimeUtils.getEndOfNextWeek();

				break;
			}
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_MONTH:{
				Date startOfNextMonth= DateTimeUtils.getStartOfNextMonth();
				Date endOfNextMonth= DateTimeUtils.getEndOfNextMonth();
				break;
			}
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.NOT_SCHEDULED:{
				Date startDate=null;
				Date endDate=null;
				break;
			}
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.OVERDUE:{
				/*if(endDate!=null&&endDate.getTime()<DateTimeUtils.getToday().getTime()){
					result.add(reportBean);
				}*/
				break;
			}
		}
		return itemBeansList;
	}

	/**
	 * Returns the reportBeans of a full text search expression
	 * @param querySearchExpression
	 * @param personBean
	 * @param locale
	 * @return
	 */
	private static List<ReportBean> executeLuceneSearch(String querySearchExpression, TPersonBean personBean, Locale locale) {
		List<ErrorData> errorList=new ArrayList<ErrorData>();
		int[] workItemIDs=TqlBL.luceneQuery(querySearchExpression, false, locale, null, errorList);
		if(workItemIDs!=null&&workItemIDs.length>0){
			return LoadItemIDListItems.getReportBeansByWorkItemIDs(workItemIDs, false, personBean.getObjectID(), locale, true);
		}
		return null;
	}
	
	/**
	 * Returns the item beans of a full text search expression
	 * @param querySearchExpression
	 * @param personBean
	 * @param locale
	 * @return
	 */
	private static List<TWorkItemBean> getLuceneItems(String querySearchExpression, TPersonBean personBean, Locale locale) {
		List<ErrorData> errorList=new ArrayList<ErrorData>();
		int[] workItemIDs=TqlBL.luceneQuery(querySearchExpression, false, locale, null, errorList);
		if(workItemIDs!=null&&workItemIDs.length>0){
			return LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(workItemIDs, personBean.getObjectID(), false, false, false);
		}
		return null;
	}
	
	public static List<TPersonBean> getResponsibles(String workItems, TPersonBean personBean, Locale locale){
		IFieldTypeRT responsibleFieldType = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_RESPONSIBLE);
		MassOperationContext massOperationContext=new MassOperationContext();
		int[] issueIDsArr= StringArrayParameterUtils.splitSelectionAsIntArray(workItems);
		Integer[] involvedProjectsIDs= MassOperationBL.getInvolvedProjectIDs(issueIDsArr,personBean.getObjectID());
		massOperationContext.setInvolvedProjectsIDs(involvedProjectsIDs);
		MassOperationValue massOperationValue=new MassOperationValue(SystemFields.INTEGER_RESPONSIBLE);
		Integer parameterCode=null;
		responsibleFieldType.loadBulkOperationDataSource(massOperationContext,
				massOperationValue,
				parameterCode,personBean,locale);
		List<TPersonBean> responsibles= (List<TPersonBean> )massOperationValue.getPossibleValues();
		return responsibles;
	}

	public static String getQueryName(Integer queryType,Integer queryID,Integer queryContextID, Locale locale){
		String queryName="";
		if(queryType!=null){
			switch (queryType.intValue()){
				case QUERY_TYPE.INSTANT:{
					queryName=getText("menu.findItems.instantFilter",locale)/*+" "+queryContextID*/;
					break;
				}
				case QUERY_TYPE.SAVED:{
					queryName=getFilterTitle(queryID, locale);
					break;
				}
				case QUERY_TYPE.BASKET:{
					TBasketBean basketBean= BasketBL.getBasketByID(queryID);
					if(basketBean!=null){
						queryName=LocalizeUtil.localizeDropDownEntry(basketBean, locale);
					}
					break;
				}
				case QUERY_TYPE.STATUS:{
					TStateBean stateBean=LookupContainer.getStatusBean(queryID);
					if(stateBean!=null){
						queryName=LocalizeUtil.localizeDropDownEntry(stateBean, locale);
					}
					break;
				}
				case QUERY_TYPE.PROJECT_RELEASE:{
					LOGGER.debug("Execute project or release query: "+queryID);
					if(queryID>0){
						Integer releaseID=queryID;
						TReleaseBean releaseBean=LookupContainer.getReleaseBean(releaseID);
						if(releaseBean!=null){
							TProjectBean projectBean=LookupContainer.getProjectBean(releaseBean.getProjectID());
							if (projectBean!=null) {
								queryName=projectBean.getLabel()+" "+releaseBean.getLabel();
							}
						}
					}else{
						Integer projectID = Integer.valueOf(queryID.intValue()*-1);
						TProjectBean projectBean=LookupContainer.getProjectBean(projectID);
						if(projectBean!=null){
							queryName=projectBean.getLabel();
						}
					}
					break;
				}
				case QUERY_TYPE.SCHEDULED:{
					switch (queryID){
						case ItemNavigatorFilterBL.SCHEDULE_TYPE.TODAY:{
							queryName=getText("itemov.lbl.scheduled.today",locale);
							break;
						}
						case ItemNavigatorFilterBL.SCHEDULE_TYPE.TOMORROW:{
							queryName=getText("itemov.lbl.scheduled.tomorrow",locale);
							break;
						}
						case ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_WEEK:{
							queryName=getText("itemov.lbl.scheduled.nextWeek",locale);
							break;
						}
						case ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_MONTH:{
							queryName=getText("itemov.lbl.scheduled.nextMonth",locale);
							break;
						}
						case ItemNavigatorFilterBL.SCHEDULE_TYPE.NOT_SCHEDULED:{
							queryName=getText("itemov.lbl.scheduled.notScheduled",locale);
							break;
						}
						case ItemNavigatorFilterBL.SCHEDULE_TYPE.OVERDUE:{
							queryName=getText("itemov.lbl.scheduled.overdue",locale);
							break;
						}
					}
					break;
				}
				case QUERY_TYPE.DASHBOARD:{
					String queryTitle=null;
					if(queryID!=null){
						TDashboardFieldBean dashboardItem=(TDashboardFieldBean) DashboardFieldRuntimeBL.getInstance().loadField(queryID);
						if(dashboardItem!=null){
							String title=dashboardItem.getParametres().get("title");
							if(title==null||title.trim().length()==0){
								DashboardDescriptor desc=DashboardUtil.getDescriptor(dashboardItem);
								title=desc.getLabel();
							}
							queryTitle=title;
						}
					}
					if(queryTitle!=null){
						queryName=getDashboardText(queryTitle,locale);
					}else{
						queryName=getText("menu.cockpit",locale);
					}
					break;
				}
				case QUERY_TYPE.LUCENE_SEARCH:{
					String luceneExpression = null;
					TLastExecutedQueryBean lastExecutedQueryBean = LastExecutedBL.loadByPrimaryKey(queryContextID);
					if (lastExecutedQueryBean!=null) {
						TCLOBBean clobBean = ClobBL.loadByPrimaryKey(lastExecutedQueryBean.getQueryClob());
						if (clobBean!=null) {
							luceneExpression = clobBean.getCLOBValue();
						}
					}
					if (luceneExpression==null) {
						luceneExpression = "";
					}
					queryName=luceneExpression;
					break;
				}
			}
		}
		return queryName;
	}



	public static String getFilterTitle(Integer filterId, Locale locale){
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		ILabelBean labelBean = filterFacade.getByKey(filterId, locale);
		String filterTitle=null;
		if (labelBean!=null) {
			filterTitle = labelBean.getLabel();
		}
		return filterTitle;
	}



	public static  QueryContext loadLastQuery(Integer personID,Locale locale){
		TLastExecutedQueryBean lastExecutedQueryBean=LastExecutedBL.loadLastByPerson(personID);
		if(lastExecutedQueryBean==null){
			return null;
		}
		QueryContext queryContext=queryBeanToQueryContext(lastExecutedQueryBean,locale);
		return queryContext;
	}
	public static Integer getQueryFieldListCss(Integer personID, QueryContext queryContext){
		Integer result=null;
		if (queryContext.getQueryType()==QUERY_TYPE.SAVED){
			TMenuitemQueryBean menuitemQueryBean = MenuitemFilterBL.loadByPersonAndQuery(personID, queryContext.getQueryID());
			if(menuitemQueryBean!=null){
				result=menuitemQueryBean.getCSSStyleField();
				if(result!=null&&result.intValue()==0){
					result=null;
				}
			}
		}
		return result;
	}
	//TODO refactor this method
	public static Map<Integer,String> getCssRules(Integer fieldListID){
		Map<Integer,String> result=new HashMap<Integer, String>();
		String css;
		switch (fieldListID){
			case ListBL.RESOURCE_TYPES.STATUS:{
				List<TStateBean> beanList=StatusBL.loadAll();
				TStateBean bean;
				for(int i=0;i<beanList.size();i++){
					bean=beanList.get(i);
					if(bean.getCSSStyle()!=null){
						css=CssStyleBean.decodeCssStyle(bean.getCSSStyle()).getCssStyle();
						result.put(bean.getObjectID(),css);
					}
				}
				break;
			}
			case ListBL.RESOURCE_TYPES.PRIORITY:{
				List<TPriorityBean> beanList=PriorityBL.loadAll();
				TPriorityBean bean;
				for(int i=0;i<beanList.size();i++){
					bean=beanList.get(i);
					if(bean.getCSSStyle()!=null){
						css=CssStyleBean.decodeCssStyle(bean.getCSSStyle()).getCssStyle();
						result.put(bean.getObjectID(),css);
					}
				}
				break;
			}
			case ListBL.RESOURCE_TYPES.SEVERITY:{
				List<TSeverityBean> beanList=SeverityBL.loadAll();
				TSeverityBean bean;
				for(int i=0;i<beanList.size();i++){
					bean=beanList.get(i);
					if(bean.getCSSStyle()!=null){
						css=CssStyleBean.decodeCssStyle(bean.getCSSStyle()).getCssStyle();
						result.put(bean.getObjectID(),css);
					}
				}
				break;
			}
			case ListBL.RESOURCE_TYPES.ISSUETYPE:{
				List<TListTypeBean> beanList=IssueTypeBL.loadAllSelectable();
				TListTypeBean bean;
				for(int i=0;i<beanList.size();i++){
					bean=beanList.get(i);
					if(bean.getCSSStyle()!=null){
						css=CssStyleBean.decodeCssStyle(bean.getCSSStyle()).getCssStyle();
						result.put(bean.getObjectID(),css);
					}
				}
				break;
			}
			default:{
				//custom select
				List<TOptionBean> beanList = OptionBL.loadByListID(fieldListID);
				if(beanList!=null && !beanList.isEmpty()){
					TOptionBean bean;
					for(int i=0;i<beanList.size();i++){
						bean=beanList.get(i);
						if(bean.getCSSStyle()!=null){
							css=CssStyleBean.decodeCssStyle(bean.getCSSStyle()).getCssStyle();
							result.put(bean.getObjectID(),css);
						}
					}
				}
			}
		}
		return result;
	}
	public static QueryContext loadQueryContext(Integer queryContextID,Locale locale){
		LOGGER.debug("Loading previous query:"+queryContextID);
		TLastExecutedQueryBean lastExecutedQueryBean=LastExecutedBL.loadByPrimaryKey(queryContextID);
		if(lastExecutedQueryBean==null){
			return null;
		}
		QueryContext queryContext=queryBeanToQueryContext(lastExecutedQueryBean,locale);
		return queryContext;
	}

	private static QueryContext queryBeanToQueryContext(TLastExecutedQueryBean lastExecutedQueryBean,Locale locale){
		QueryContext queryContext=new QueryContext();
		queryContext.setQueryType(lastExecutedQueryBean.getQueryType());
		queryContext.setQueryID(lastExecutedQueryBean.getQueryKey());
		queryContext.setId(lastExecutedQueryBean.getObjectID());
		String queryName=ItemNavigatorBL.getQueryName(lastExecutedQueryBean.getQueryType(),lastExecutedQueryBean.getQueryKey(),queryContext.getId(), locale);
		queryContext.setQueryName(queryName);
		TCLOBBean clobBean = null;
		switch (lastExecutedQueryBean.getQueryType()){
			case QUERY_TYPE.INSTANT:
			//case QUERY_TYPE.SAVED:
			case QUERY_TYPE.LUCENE_SEARCH:
				clobBean = ClobBL.loadByPrimaryKey(lastExecutedQueryBean.getQueryClob());
				if (clobBean!=null) {
					queryContext.setFilterExpression(clobBean.getCLOBValue());
				}
				break;
			case QUERY_TYPE.DASHBOARD:{
				Map<String,String> dashParams=new HashMap<String, String>();
				clobBean = ClobBL.loadByPrimaryKey(lastExecutedQueryBean.getQueryClob());
				if(clobBean!=null){
					dashParams=decodeDashboardParams(clobBean.getCLOBValue());
					queryContext.setDashboardParams(dashParams);
				}
				break;
			}
		}
		return queryContext;
	}


	private static Map<String,String> decodeDashboardParams(String params){
		Map<String,String> dashParams=new HashMap<String, String>();
		Properties properties=new Properties();
		if(params!=null){
			try {
				properties.load(new StringReader(params));
			} catch (Exception e) {
				LOGGER.warn("Problem with loading preferences "  + e.getMessage(), e);
			}
		}
		dashParams.putAll((Map) properties);
		return dashParams;

	}




	public static String getText(String s, Locale locale){
		return LocalizeUtil.getLocalizedTextFromApplicationResources(s, locale);
	}
	private static String getDashboardText(String s, Locale locale){
		return LocalizeUtil.getLocalizedText(s, locale, ResourceBundleManager.DASHBOARD_RESOURCES);
	}
}
