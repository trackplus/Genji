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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSONException;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.parameters.FilterSelectsParametersUtil;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.ListOptionIconBL;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.admin.server.siteConfig.SiteConfigBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ISelect;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.ItemSaveBL;
import com.aurel.track.item.ItemSaveJSON;
import com.aurel.track.item.massOperation.MassOperationBL;
import com.aurel.track.item.massOperation.MassOperationException;
import com.aurel.track.item.operation.BasketBL;
import com.aurel.track.item.operation.ItemOperationException;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuBL;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuJSON;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuTO;
import com.aurel.track.itemNavigator.lastExecuted.LastExecutedBL;
import com.aurel.track.itemNavigator.layout.LayoutTO;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.itemNavigator.layout.group.LayoutGroupsBL;
import com.aurel.track.itemNavigator.layout.group.SortFieldTO;
import com.aurel.track.itemNavigator.navigator.MenuItem;
import com.aurel.track.itemNavigator.navigator.Navigator;
import com.aurel.track.itemNavigator.navigator.NavigatorJSON;
import com.aurel.track.itemNavigator.navigator.Section;
import com.aurel.track.itemNavigator.viewPlugin.ViewDescriptorBL;
import com.aurel.track.itemNavigator.viewPlugin.ViewPluginBL;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.mobile.MobileBL;
import com.aurel.track.move.GanttTaskBean;
import com.aurel.track.move.ItemMoveBL;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.ReportItemJSON;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanExpandContext;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.StringArrayParameterUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author adib
 */
public class ItemNavigatorAction extends ActionSupport implements Preparable,SessionAware,
                                 ServletRequestAware, ServletResponseAware, ApplicationAware {

	private static final long serialVersionUID = 340L;
	private static final Logger LOGGER = LogManager.getLogger(ItemNavigatorAction.class);

	public static final String LAST_SELECTED_NAVIGATOR="issueNavigator.lastSelectedNavigator";

	protected Map<String,Object> session;
	protected Map<String, Object> application;
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;

	protected TPersonBean personBean;
	protected Locale locale;

	private boolean hasInitData=true;
	private String initData;

	//for item drop on tree navigator
	protected String nodeType;
	protected Integer nodeObjectID;
	protected Integer workItemID;
	private Integer fieldID;
	private Integer value;
	protected Integer actionID;
	private String workItems;
	private Integer targetWorkItemID;
	private boolean before;

	private String viewID;
	private Integer objectID;

	private Map<String,String> params;
	private Map<String,String> dashboardParams;
	private Integer queryContextID;

	private String ganttGranularity;

	//QUERY
	private Integer queryID;
	private Integer queryType;
	private boolean fromSession=false;
	private boolean lite=false;

	//plan
	private Date startDate;
	private Date endDate;

	private boolean removeViolatedLinksAndCascade;
	private String wrongDependencies;
	protected boolean forceAllItems;

	private boolean mobilApplication = false;
	private int starts;
	private int limit;

	private boolean validateRelationshipsGantt;
	private boolean ganttConfigValue;

	private String ganttConfigNameToSave;


	private String layoutCls="com.trackplus.layout.ItemNavigatorLayout";
	private String pageTitle="menu.findItems";

	private String html;
	private String format;
	private String orientation;
	private String range;
	private String fileFormat;
	private String gntPDFLocation;

	private boolean isProject;
	private Integer projectID;
	private Integer issueTypeID;
	private String ganttTaskStore;
	private String ganttDependencyStore;
	private String ganttDependenciesToRemove;

	private boolean validateBeforeSave;

	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
	protected boolean isUseIssueTypeIcon(){
		return false;
	}
	private QueryContext createQueryContext(){
		QueryContext queryContext=null;
		if(queryContextID!=null){
			queryContext=ItemNavigatorBL.loadQueryContext(queryContextID,locale);
			if (queryContext==null) {
				queryContext=createDefaultQueryContext();
			}
			//queryType=queryContext.getQueryType();
			//queryID=queryContext.getQueryID();
		}else{
			if(queryType!=null){
				queryContext=new QueryContext();
				queryContext.setQueryType(queryType);
				queryContext.setQueryID(queryID);
				String queryName=ItemNavigatorBL.getQueryName(queryType,queryID,-1, locale);
				queryContext.setQueryName(queryName);
				queryContext.setDashboardParams(dashboardParams);
			}else{
				//execute last query
				queryContext=ItemNavigatorBL.loadLastQuery(personBean.getObjectID(),locale);
				if(queryContext==null){
					//no last query use default
					queryContext=createDefaultQueryContext();

				}
			}
		}
		return queryContext;
	}
	private QueryContext createDefaultQueryContext(){
		QueryContext queryContext=new QueryContext();
		queryContext.setQueryType(ItemNavigatorBL.QUERY_TYPE.SAVED);
		queryContext.setQueryID(PredefinedQueryBL.PREDEFINED_QUERY.MY_ITEMS);
		String queryName=ItemNavigatorBL.getQueryName(ItemNavigatorBL.QUERY_TYPE.SAVED,PredefinedQueryBL.PREDEFINED_QUERY.MY_ITEMS,null, locale);
		queryContext.setQueryName(queryName);
		return queryContext;
	}

	@Override
	public String execute() {
		long start = setStartTime("queryType="+queryType+", queryID="+queryID);
		QueryContext queryContext = initQueryContext();
		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		List<ILabelBean> usedStatusList = ItemNavigatorBL.getUsedStatusList(personBean, locale, session);
		Navigator nav = intNavigator(personBean, locale, usedStatusList);
		initData = prepareInitData(queryContext, appBean, nav);
		printTime("execute", start);
		if(lite){
			return "lite";
		}
		if(mobilApplication) {
			try{
				ServletActionContext.getResponse().addHeader("Access-Control-Allow-Origin", "*");
				JSONUtility.encodeJSON(ServletActionContext.getResponse(), initData);
				return null;
			}catch(Exception ex){
				LOGGER.error(ex.getMessage(),ex);
			}
		}
		return SUCCESS;
	}

	protected QueryContext initQueryContext() {
		QueryContext queryContext=createQueryContext();
		Integer queryContextID= LastExecutedBL.storeLastExecutedQuery(personBean.getObjectID(), queryContext);
		queryContext.setId(queryContextID);
		return queryContext;
	}

	protected String prepareInitData(QueryContext queryContext, ApplicationBean appBean, Navigator nav) {
		return ItemNavigatorBL.prepareInitData(nav, appBean, personBean, locale, queryContext, session, lite, workItemID, actionID, forceAllItems,isUseIssueTypeIcon());
	}

	protected Navigator intNavigator(TPersonBean personBean, Locale locale, List<ILabelBean> usedStatusList) {
		Navigator nav=ItemNavigatorFilterBL.createNavigator(personBean, usedStatusList, locale);
		return nav;
	}
	public String executePreviousQuery(){

		long startp = setStartTime("executePreviousQuery(): queryContextID="+queryContextID);
		QueryContext queryContext=ItemNavigatorBL.loadQueryContext(queryContextID,locale);
		if(queryContext!=null){
			LastExecutedBL.storeLastExecutedQuery(personBean.getObjectID(),queryContext);
		}
		boolean includeLayout = personBean.isEnableQueryLayout();
		executeInternal(queryContext,includeLayout, ReportBeanExpandContext.getExpandContext(session), false);

		printTime("executePreviousQuery()",startp);
		return null;
	}

	/**
	 * Saves the current layout as standard
	 * @return
	 */
	public String saveAsStandardLayout(){
		QueryContext queryContext = createQueryContext();
		NavigatorLayoutBL.saveAsStandardLayout(personBean, locale, queryContext.getQueryType(), queryContext.getQueryID());
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}

	/**
	 * Saves the current layout as filter with view layout
	 * @return
	 */
	public String saveAsFilterLayout() {
		QueryContext queryContext = createQueryContext();
		Integer filterType = queryContext.getQueryType();
		Integer filterID = queryContext.getQueryID();
		NavigatorLayoutBL.saveAsFilterLayout(personBean, locale, filterType, filterID);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}

	/**
	 * Restore to standard layout
	 * @return
	 */
	public String useStandardLayout(){
		QueryContext queryContext = createQueryContext();
		NavigatorLayoutBL.restoreToDefault(personBean, queryContext.getQueryType(), queryContext.getQueryID());
		executeInternal(queryContext, true, ReportBeanExpandContext.getExpandContext(session), false);
		return null;
	}

	public String emptyTrashBasket(){
		BasketBL.emptyTrash(personBean.getObjectID());
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}



	private ReportBeans calculateReportBeans(Map<String,Object> session){
		QueryContext queryContext = createQueryContext();
		List<ReportBean> reportBeansDB = null;
		try{
			reportBeansDB=ItemNavigatorBL.executeQuery(personBean,locale,queryContext);
		}catch (TooManyItemsToLoadException ex){
			LOGGER.error(ExceptionUtils.getStackTrace(ex),ex);
		}
		List<ReportBean> reportBeanList=null;
		if(nodeType!=null&&nodeType.trim().length()>0){
			reportBeanList=ItemNavigatorFilterBL.filter(reportBeansDB,nodeType,nodeObjectID);
		}else{
			reportBeanList=reportBeansDB;
		}
		List<GroupFieldTO> groupFields = new LinkedList<GroupFieldTO>();
		SortFieldTO sortFieldTO = LayoutGroupsBL.loadGroupFields(personBean, locale, queryContext.getQueryType(), queryContext.getQueryID(), groupFields);
		ReportBeanExpandContext reportBeanExpandContext = ReportBeanExpandContext.getExpandContext(session);
		reportBeanExpandContext.setGroupBy(groupFields);
		reportBeanExpandContext.setSortFieldTO(sortFieldTO);
		return new ReportBeans(reportBeanList, locale, reportBeanExpandContext, false,false);
	}
	public String refresh(){
		QueryContext queryContext=createQueryContext();
		return executeInternal(queryContext,false, ReportBeanExpandContext.getExpandContext(session), true);
	}
	public String reload(){
		QueryContext queryContext=createQueryContext();
		return executeInternal(queryContext,true, ReportBeanExpandContext.getExpandContext(session), true);
	}
	public String navigate(){
		long used = getUsedMemory();
		QueryContext queryContext=createQueryContext();
		List<ReportBean> reportBeansDB = null;
		try{
			reportBeansDB=ItemNavigatorBL.executeQuery(personBean,locale,queryContext);
		}catch (TooManyItemsToLoadException e){
			LOGGER.info("Number of items to load " + e.getItemCount());
		}
		int totalCount=reportBeansDB==null?0:reportBeansDB.size();
		List<ReportBean> reportBeanList=null;
		if(nodeType!=null&&nodeType.trim().length()>0){
			reportBeanList=ItemNavigatorFilterBL.filter(reportBeansDB,nodeType,nodeObjectID);
		}else{
			reportBeanList=reportBeansDB;
		}
		int count=reportBeanList==null?0:reportBeanList.size();
		List<IssueListViewDescriptor> descriptors = ViewDescriptorBL.getViewDescriptors(personBean);
		IssueListViewDescriptor descriptor = ViewDescriptorBL.getDescriptor(personBean, descriptors, MobileBL.isMobileApp(session), queryContext);
		boolean includeLongFields=false;
		boolean plainData=false;
		IssueListViewPlugin plugin=null;
		if(descriptor!=null){
			includeLongFields=descriptor.isUseLongFields();
			plainData=descriptor.isPlainData();
			plugin=ViewPluginBL.getPlugin(descriptor.getTheClassName());
		}
		String listViewData = "";
		Set<Integer> exclusiveShortFields=null;
		boolean includePercentDone=false;
		if(plugin!=null){
			listViewData = plugin.getExtraJSON(session,reportBeanList, queryContext, true);
			exclusiveShortFields=plugin.getExclusiveShortFields(personBean, queryContext);
			includePercentDone=plugin.includePercentDone();
		}
		LayoutTO layoutTO = null;
		if(exclusiveShortFields!=null){
			layoutTO = NavigatorLayoutBL.loadExclusiveFields(exclusiveShortFields, locale);
		}else{
			layoutTO = NavigatorLayoutBL.loadLayout(personBean, locale, queryContext.getQueryType(), queryContext.getQueryID(), true);
		}
		ReportBeanExpandContext reportBeanExpandContext=ReportBeanExpandContext.getExpandContext(session);
		reportBeanExpandContext.setGroupBy(layoutTO.getGroupFields());
		reportBeanExpandContext.setSortFieldTO(layoutTO.getSortField());
		if(plugin!=null){
			reportBeanExpandContext=plugin.updateReportBeanExpandContext(personBean,reportBeanList, reportBeanExpandContext, session, queryContext, true);
		}
		reportBeanList= ItemNavigatorBL.cutItems(reportBeanList, starts<0?0:starts,limit);
		ReportBeans reportBeans=new ReportBeans(reportBeanList, locale, reportBeanExpandContext, true, plainData);
		boolean  useProjectSpecificID=false;
		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
			useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
		}
		Integer queryFieldCSS=ItemNavigatorBL.getQueryFieldListCss(personBean.getObjectID(),queryContext);
		Map<Integer,String> cssRules=null;
		if(queryFieldCSS!=null){
			cssRules=ItemNavigatorBL.getCssRules(queryFieldCSS);
		}
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendJSONValue(sb,"issues",ReportItemJSON.encodeReportBeans(reportBeans, layoutTO, includeLongFields, locale,useProjectSpecificID,personBean,queryFieldCSS,plainData,exclusiveShortFields,includePercentDone,isUseIssueTypeIcon()));
			JSONUtility.appendIntegerValue(sb,"count",count);
			JSONUtility.appendIntegerValue(sb,"totalCount",totalCount,true);
			sb.append("}");
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e), e);
		}
		checkMemory(used);
		return null;
	}

	protected String executeInternal(QueryContext queryContext,boolean  includeLayout,
			ReportBeanExpandContext reportBeanExpandContext, boolean forcePersonLayout){
		if(queryContext==null){
			JSONUtility.encodeJSONFailure(servletResponse, "Query not exist anymore!", 1);
			return null;
		}
		List<IssueListViewDescriptor> descriptors = ViewDescriptorBL.getViewDescriptors(personBean);
		IssueListViewDescriptor descriptor = ViewDescriptorBL.getDescriptor(personBean, descriptors, MobileBL.isMobileApp(session), queryContext);

		//If client not mobile or tablet last selected view JSON is returned
		/*if(!MobileBL.isMobileApp(session)) {
			descriptor = ViewDescriptorBL.getIssueListViewDescriptor(personBean);
		}else {
			//if client is mobile or tablet WBS views JSON is returned
			List<IssueListViewDescriptor> descriptors= ViewDescriptorBL.getIssueListViewDescriptors();
			String wbsView = descriptors.get(2).getId();
			descriptor = ViewDescriptorBL.getDescriptor(wbsView);
		}*/

		boolean includeLongFields=false;
		boolean plainData=false;
		boolean paginate=false;
		boolean gantt=false;
		IssueListViewPlugin plugin=null;
		if(descriptor!=null){
			includeLongFields=descriptor.isUseLongFields();
			plainData=descriptor.isPlainData();
			LOGGER.debug("List view descriptor id="+descriptor.getId()+" theClassName="+descriptor.getTheClassName());
			plugin=ViewPluginBL.getPlugin(descriptor.getTheClassName());
			if(plugin==null){
				LOGGER.warn("list view plugin is null");
			}else {
				LOGGER.debug("list view plugin=" + plugin.getClass());
			}
			if (plugin!=null && plainData){
				paginate=personBean.isPaginate();
			}
			gantt = descriptor.isGantt();
		} else {
			LOGGER.warn("List view descriptor is null");
		}

		List<ReportBean> reportBeanList=null;
		int totalCount =0;
		boolean tooManyItems=false;
		if(!paginate) {
			List<ReportBean> reportBeansDB = null;
			try{
				Integer filterID=queryContext.getQueryID();
				Integer filterType=queryContext.getQueryType();
				if(fromSession==true){
					FilterUpperTO filterUpperTO=(FilterUpperTO)session.get(FilterBL.FILTER_UPPER_APPLY_EXECUTE + filterID + "_" + filterType);
					QNode qNode=(QNode)session.get(FilterBL.TREE_APPLY_EXECUTE + filterID + "_" + filterType);
					if (gantt) {
						reportBeansDB = ItemNavigatorBL.executeGanttInstantQuery(filterUpperTO, qNode, personBean, locale);
					} else {
						reportBeansDB = TreeFilterExecuterFacade.getInstantTreeFilterReportBeanListNoReport(filterUpperTO, qNode, null, personBean, locale, null, null);
					}
				}else{
					session.remove(FilterBL.FILTER_UPPER_APPLY_EXECUTE + filterID + "_" + filterType);
					//session.remove(FilterBL.FILTER_UPPER_APPLY_EDIT + filterID + "_" + filterType);
					session.remove(FilterBL.TREE_APPLY_EXECUTE + filterID + "_" + filterType);
					//session.remove(FilterBL.TREE_APPLY_EDIT + filterID + "_" + filterType);
					if (gantt) {
						reportBeansDB = ItemNavigatorBL.executeGanttQuery(personBean, locale, queryContext);
					} else {
						reportBeansDB = ItemNavigatorBL.executeQuery(personBean, locale, queryContext);
					}
				}
			}catch (TooManyItemsToLoadException e){
				LOGGER.info("Number of items to load " + e.getItemCount());
				tooManyItems=true;
			}
			totalCount = reportBeansDB == null ? 0 : reportBeansDB.size();
			if (nodeType != null && nodeType.trim().length() > 0) {
				reportBeanList = ItemNavigatorFilterBL.filter(reportBeansDB, nodeType, nodeObjectID);
			} else {
				reportBeanList = reportBeansDB;
			}
		}


		String listViewData = "";
		Set<Integer> exclusiveShortFields=null;
		boolean includePercentDone=false;
		if(plugin!=null){
			listViewData = plugin.getExtraJSON(session,reportBeanList, queryContext, forcePersonLayout);
			exclusiveShortFields=plugin.getExclusiveShortFields(personBean, queryContext);
			includePercentDone=plugin.includePercentDone();
		}
		LayoutTO layoutTO = null;
		if(exclusiveShortFields!=null){
			layoutTO = NavigatorLayoutBL.loadExclusiveFields(exclusiveShortFields, locale);
		}else{
			layoutTO = NavigatorLayoutBL.loadLayout(personBean, locale, queryContext.getQueryType(), queryContext.getQueryID(), forcePersonLayout);
		}
		reportBeanExpandContext = reportBeanExpandContext.setGroupingSorting(reportBeanExpandContext, layoutTO);
		if (plugin!=null) {
			reportBeanExpandContext=plugin.updateReportBeanExpandContext(personBean,reportBeanList, reportBeanExpandContext, session, queryContext, forcePersonLayout);
		}
		LOGGER.debug("listViewData="+listViewData);

		int count=reportBeanList==null?0:reportBeanList.size();
		if(paginate) {
			totalCount=-1;
			count=-1;
		}
		Integer overflowItems=0;
		if(forceAllItems==false&&count>ItemNavigatorBL.MAX_ISSUE_NO){
			overflowItems=count;
			count=ItemNavigatorBL.MAX_ISSUE_NO;
			reportBeanList= ItemNavigatorBL.cutItems(reportBeanList, ItemNavigatorBL.MAX_ISSUE_NO);
		}
		ReportBeans reportBeans =new ReportBeans(reportBeanList, locale, reportBeanExpandContext, true, plainData);
		int issueCount = 0;
		if (reportBeanList!=null) {
			issueCount=reportBeanList.size();
			reportBeanList.clear();
			reportBeanList=null;
		}
		List<FilterInMenuTO> lastQueries=FilterInMenuBL.getLastExecutedQueries(personBean, locale);
		//session.put(FilterBL.LAST_EXECUTED_FILTERS, lastQueries);
		session.put(FilterBL.LAST_EXECUTED_FILTERS_JSON, FilterInMenuJSON.encodeFiltersInMenu(lastQueries));

		boolean  useProjectSpecificID=false;
		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
			useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
		}
		Integer queryFieldCSS=ItemNavigatorBL.getQueryFieldListCss(personBean.getObjectID(),queryContext);
		Map<Integer,String> cssRules=null;
		if(queryFieldCSS!=null){
			cssRules=ItemNavigatorBL.getCssRules(queryFieldCSS);
		}

		boolean isFilterView=false;
		boolean maySaveFilterLayout=false;
		TQueryRepositoryBean  queryRepositoryBean=NavigatorLayoutBL.getSavedQuery(queryContext.getQueryID(), queryContext.getQueryType());
		if(queryRepositoryBean!=null){
			String viewID = NavigatorLayoutBL.getSavedItemFilterView(queryContext.getQueryID(), queryContext.getQueryType());
			isFilterView=viewID!=null && !viewID.equals("");
			if (isFilterView) {
				maySaveFilterLayout=NavigatorLayoutBL.isModifiable(queryRepositoryBean, personBean);
			}
		}
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb,JSONUtility.JSON_FIELDS.SUCCESS,true);
			sb.append("\"data\":{");
			if(includeLayout){
				JSONUtility.appendJSONValue(sb, "layout", ReportItemJSON.encodeLayout(layoutTO, useProjectSpecificID));
			}
			JSONUtility.appendBooleanValue(sb, "isFilterView", isFilterView);
			JSONUtility.appendBooleanValue(sb, "maySaveFilterLayout", maySaveFilterLayout);
			JSONUtility.appendBooleanValue(sb, "summaryItemsBehavior", ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior());
			JSONUtility.appendBooleanValue(sb,"tooManyItems",tooManyItems);
			JSONUtility.appendJSONValue(sb,"issues",ReportItemJSON.encodeReportBeans(reportBeans, layoutTO, includeLongFields, locale,useProjectSpecificID,personBean,queryFieldCSS,plainData,exclusiveShortFields,includePercentDone,isUseIssueTypeIcon()));

			reportBeans=null;
			JSONUtility.appendStringParametersMap(sb, "dashboardParams", queryContext.getDashboardParams());
			JSONUtility.appendJSONValue(sb, "queryContext", ItemNavigatorJSON.encodeQueryContext(queryContext));
			JSONUtility.appendJSONValue(sb, "listViewData", listViewData);

			/* Gantt HOLIDAYS config
			 * The holidays array contains the free days dates.
			 * This code block will be used when Genji calendar is implemented.
			 * This is sample for useing.
			 *
			 * */
			StringBuilder holidays = new StringBuilder();
			holidays.append("[");
//			Calendar cal = Calendar.getInstance();
//			cal.set(Calendar.YEAR, 2010);
//			cal.set(Calendar.MONTH, 0);
//			cal.set(Calendar.DAY_OF_MONTH, 1);
//			cal.set(Calendar.HOUR_OF_DAY,0);
//			cal.set(Calendar.MINUTE, 0);
//			cal.set(Calendar.SECOND, 0);
//			Date d = cal.getTime();
//			WorkDaysConfig.exceptionFromWorkDays.clear();
//			WorkDaysConfigImplementation.add("Exception", d);
//			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
//			for(Map.Entry<Date, String> entry: WorkDaysConfig.exceptionFromWorkDays.entrySet()) {
//				holidays.append("{");
//				JSONUtility.appendStringValue(holidays, "Date", dateFormatter.format(entry.getKey()));
//				JSONUtility.appendStringValue(holidays, "Cls", "gnt-national-holiday");
//				JSONUtility.appendStringValue(holidays, "Name", entry.getValue(), true);
//				holidays.append("}");
//			}
			holidays.append("]");
			JSONUtility.appendJSONValue(sb, "holidays", holidays.toString());
			JSONUtility.appendBooleanValue(sb, "isPrintItemEditable", personBean.isPrintItemEditable());
			JSONUtility.appendBooleanValue(sb, "isActiveTopDownDate", ApplicationBean.getInstance().getBudgetActive());
			JSONUtility.appendBooleanValue(sb, "showBaseline", ApplicationBean.getInstance().getSiteBean().getShowBaseline());
			JSONUtility.appendBooleanValue(sb, "showBoth", ApplicationBean.getInstance().getSiteBean().getShowBothGantt());
			JSONUtility.appendBooleanValue(sb, "highlightCriticalPath", ApplicationBean.getInstance().getSiteBean().getHighlightCriticalPathGantt());
			JSONUtility.appendBooleanValue(sb, "validateRelationships", ApplicationBean.getInstance().getSiteBean().getValidateRelationshipsGantt());

			/*StringBuilder milestoneWorkItems = new StringBuilder();
			if(reportBeanList != null) {
				int[] workItemIDs = new int[reportBeanList.size()];
				for(int i = 0; i < reportBeanList.size(); i++) {
					workItemIDs[i] = reportBeanList.get(i).getWorkItemBean().getObjectID();
				}
				MsProjectTaskDAO msProjTaskDao = new TMSProjectTaskPeer();
				List<Integer> milestones = msProjTaskDao.getMilestones(workItemIDs);
				if(milestones != null) {
					for(int i = 0; i < milestones.size(); i++) {
						if (i == milestones.size() - 1) {
							milestoneWorkItems.append(milestones.get(i).toString());
						}else {
							milestoneWorkItems.append(milestones.get(i).toString() + ",");
						}
					}
				}
				JSONUtility.appendStringValue(sb, "milestoneWorkitems", milestoneWorkItems.toString());
			}*/

			List<Integer> toolTipLocalizedFields = new ArrayList<Integer>();
			toolTipLocalizedFields.add(SystemFields.SYNOPSIS);
			toolTipLocalizedFields.add(SystemFields.INTEGER_ISSUENO);
			toolTipLocalizedFields.add(SystemFields.STARTDATE);
			toolTipLocalizedFields.add(SystemFields.ENDDATE);
			toolTipLocalizedFields.add(SystemFields.STATE);
			toolTipLocalizedFields.add(SystemFields.RESPONSIBLE);

			Map<Integer, String> labelsMap = FieldRuntimeBL.getLocalizedDefaultFieldLabels(toolTipLocalizedFields, locale);
			StringBuilder labelSB = new StringBuilder();
			labelSB.append("{");
			JSONUtility.appendStringValue(labelSB, "name", labelsMap.get(SystemFields.SYNOPSIS));
			JSONUtility.appendStringValue(labelSB, "issue", labelsMap.get(SystemFields.INTEGER_ISSUENO));
			JSONUtility.appendStringValue(labelSB, "start", labelsMap.get(SystemFields.STARTDATE));
			JSONUtility.appendStringValue(labelSB, "end", labelsMap.get(SystemFields.ENDDATE));
			JSONUtility.appendStringValue(labelSB, "state", labelsMap.get(SystemFields.STATE));
			JSONUtility.appendStringValue(labelSB, "responsible", labelsMap.get(SystemFields.RESPONSIBLE), true);
			labelSB.append("}");
			/* ENDS of Gantt HOLIDAYS/MILESTONE config */
			JSONUtility.appendJSONValue(sb, "localizedToolTipLabels", labelSB.toString());

			if(cssRules!=null){
				JSONUtility.appendIntegerValue(sb,"queryFieldCSS",queryFieldCSS);
				JSONUtility.appendJSONValue(sb,"cssRules",ItemNavigatorJSON.encodeCssRules(cssRules));
			}
			JSONUtility.appendJSONValue(sb, "lastQueries", FilterInMenuJSON.encodeFiltersInMenu(lastQueries));
			JSONUtility.appendIntegerValue(sb,"totalCount",totalCount);
			JSONUtility.appendIntegerValue(sb,"overflowItems",overflowItems);
			JSONUtility.appendIntegerValue(sb,"count",count,true);
			sb.append("}");
			sb.append("}");
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e), e);
		}
		return null;
	}


	public String expandNode(){
		LOGGER.debug("expandNode(): nodeType="+nodeType+", nodeObjectID="+nodeObjectID);
		List<MenuItem> children=ItemNavigatorFilterBL.expandNode(nodeType, nodeObjectID, personBean, locale);
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(NavigatorJSON.encodeMenuList(children));
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e), e);
		}
		return null;
	}


	//TODO use generic filterByNode
	//Used in TABLET client
	public String filterByProject(){

		long usedMemory = getUsedMemory();

		Integer projectID=nodeObjectID;
		QueryContext queryContext=new QueryContext();
		queryContext.setQueryType(QUERY_TYPE.PROJECT_RELEASE);
		queryContext.setQueryID(projectID);
		Integer id=LastExecutedBL.storeLastExecutedQuery(personBean.getObjectID(), queryContext);
		queryContext.setId(id);
		String queryName=ItemNavigatorBL.getQueryName(QUERY_TYPE.PROJECT_RELEASE, nodeObjectID,  null, locale);
		queryContext.setQueryName(queryName);
		executeInternal(queryContext,true,ReportBeanExpandContext.getExpandContext(session), false);

		checkMemory(usedMemory);

		return null;
	}


	//TODO use generic filterByNode
	//Used in TABLET client
	public String filterByFilter(){

		long usedMemory = getUsedMemory();

		Integer projectID=nodeObjectID;
		queryType=ItemNavigatorBL.QUERY_TYPE.SAVED;
		QueryContext queryContext=new QueryContext();
		queryContext.setQueryType(queryType);
		queryContext.setQueryID(projectID);
		Integer id=LastExecutedBL.storeLastExecutedQuery(personBean.getObjectID(), queryContext);
		queryContext.setId(id);
		String queryName=ItemNavigatorBL.getQueryName(queryType, nodeObjectID,  null, locale);

		queryContext.setQueryName(queryName);
		executeInternal(queryContext,true,ReportBeanExpandContext.getExpandContext(session), false);

		checkMemory(usedMemory);

		return null;
	}

	//TODO This method is for tablet client.
	//Returns all projects with subprojects.
	public String getAllProjectHierarchy() {
		JSONUtility.encodeJSON(servletResponse, JSONUtility.getTreeHierarchyJSON(ReleaseBL.getReleaseNodesEager(personBean, null, true, true, true, true, null, false, true, false, null, locale)));
		return null;
	}

	public String getLastSelectedForTablet() {
		LOGGER.debug("->execute(): queryType="+queryType+", queryID="+queryID);

		QueryContext queryContext=createQueryContext();
		Integer queryContextID=LastExecutedBL.storeLastExecutedQuery(personBean.getObjectID(),queryContext);
		queryContext.setId(queryContextID);

		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		initData = ItemNavigatorBL.prepareInitData(appBean, personBean, locale, queryContext, session, lite, workItemID, actionID, forceAllItems,isUseIssueTypeIcon());
		JSONUtility.encodeJSON(servletResponse, initData);
		return null;
	}

	//This method returns a JSON object for tablet client with available filters: label, id
	public String getFilters() {
		JSONUtility.encodeJSON(servletResponse, session.get(FilterBL.MY_MENU_FILTERS_JSON).toString());
		return null;
	}

	//This method returnes a JSON object for tablet client with available status list. (used in accordion)
	public String getNavigatorStatusList() {
		List<ILabelBean> usedStatusList=(List<ILabelBean>)session.get(ItemNavigatorBL.USED_STATUS_LIST);

		if(usedStatusList==null){
			List<TProjectBean> projectList = ProjectBL.loadUsedProjectsFlat(personBean.getObjectID());
			Integer[] projectTypeIDs = ProjectTypesBL.getProjectTypeIDsForProjects(projectList);
			usedStatusList=StatusBL.loadAllowedByProjectTypesAndIssueTypes(projectTypeIDs, null, locale);
		}

		Section statusSection=null;
		if(usedStatusList!=null && !usedStatusList.isEmpty()){
			statusSection=new Section();
			statusSection.setName(LocalizeUtil.getLocalizedEntity(LocalizationKeyPrefixes.FIELD_LABEL_KEY_PREFIX, SystemFields.INTEGER_STATE, locale));
			statusSection.setId("section_status");
			statusSection.setIconCls(BasketBL.ICON_CLS.BASKET_ICON);
			List<MenuItem> menuItems=new ArrayList<MenuItem>();
			for(ILabelBean labelBean:usedStatusList){
				menuItems.add(ItemNavigatorFilterBL.createQueryNodeStatus(labelBean));
			}
			statusSection.setMenu(menuItems);
		}
		StringBuilder sb = new StringBuilder();
		if(statusSection != null) {
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, "success", true);
			String availableStatuses = NavigatorJSON.encodeSection(statusSection);
			JSONUtility.appendJSONValue(sb, "data", availableStatuses, true);
			sb.append("}");
		}else {
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, "success", false, true);
			sb.append("}");
		}
		JSONUtility.encodeJSON(servletResponse, sb.toString());
		return null;
	}

	public String filterByNode(){

		long usedMemory = getUsedMemory();

		long startp = setStartTime("filterByNode(): nodeType="+nodeType+", nodeObjectID="+nodeObjectID);
		int nodeTypePrefix= ItemNavigatorFilterBL.getNodeTypePrefix(nodeType);
		QueryContext queryContext=null;
		//boolean  includeLayout=false;
		if(nodeTypePrefix==ItemNavigatorFilterBL.NODE_TYPE_PREFIX.QUERY){
			LOGGER.debug("Filter by query");
			//includeLayout=personBean.isEnableQueryLayout();
			int queryTypeFromNode=ItemNavigatorFilterBL.getNodeTypeInt(nodeType);
			queryID=nodeObjectID;
			switch (queryTypeFromNode){
				/*case ItemNavigatorFilterBL.NODE_TYPE.QUERY_PREDEFINED:{
					queryType=ItemNavigatorBL.QUERY_TYPE.PREDEFINED;
					break;
				}*/
				case ItemNavigatorFilterBL.NODE_TYPE.QUERY_FILTER:{
					queryType=ItemNavigatorBL.QUERY_TYPE.SAVED;
					boolean containsParameters=FilterSelectsParametersUtil.savedFilterContainsParameter(queryID, personBean, locale);
					if(containsParameters){
						LOGGER.debug("Filter contains parameters");
						return encodeIncludeParameters();
					}
					break;
				}
				case ItemNavigatorFilterBL.NODE_TYPE.QUERY_BASKET:{
					queryType=ItemNavigatorBL.QUERY_TYPE.BASKET;
					break;
				}
				case ItemNavigatorFilterBL.NODE_TYPE.QUERY_STATUS:{
					queryType=ItemNavigatorBL.QUERY_TYPE.STATUS;
					break;
				}
				case ItemNavigatorFilterBL.NODE_TYPE.QUERY_PROJECT_RELEASE:{
					queryType=ItemNavigatorBL.QUERY_TYPE.PROJECT_RELEASE;
					break;
				}
				case ItemNavigatorFilterBL.NODE_TYPE.QUERY_SCHEDULED:{
					queryType=ItemNavigatorBL.QUERY_TYPE.SCHEDULED;
					break;
				}
			}
			queryContext=new QueryContext();
			queryContext.setQueryType(queryType);
			queryContext.setQueryID(queryID);
			Integer id=LastExecutedBL.storeLastExecutedQuery(personBean.getObjectID(), queryContext);
			queryContext.setId(id);
			String queryName=ItemNavigatorBL.getQueryName(queryType,queryID,id, locale);
			queryContext.setQueryName(queryName);
		}else{
			queryContext=ItemNavigatorBL.loadQueryContext(queryContextID,locale);
		}
		executeInternal(queryContext,true,/*includeLayout*/ReportBeanExpandContext.getExpandContext(session), false);

		printTime("filterByNode",startp);
		checkMemory(usedMemory);

		return null;
	}

	private String encodeIncludeParameters(){
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb,JSONUtility.JSON_FIELDS.SUCCESS,true);
			sb.append("\"data\":{");
			JSONUtility.appendBooleanValue(sb,"includeParameters", true, true);
			sb.append("}");
			sb.append("}");
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e), e);
		}
		return null;
	}

	public String canDropOnNode(){
		LOGGER.debug("canDropOnNode: workItems="+workItems);
		int[] workItemsIDs= StringArrayParameterUtils.splitSelectionAsIntArray(workItems);
		Set<TWorkItemBean> invalidItems= ItemNavigatorFilterBL.canDropItemOnNode(nodeType, nodeObjectID, workItemsIDs, personBean.getObjectID(), locale);
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(ItemNavigatorJSON.encodeJSONCanDropOnNode(invalidItems));
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e), e);
		}
		return null;
	}
	public String dropOnNode(){
		LOGGER.debug("dropOnNode: workItems="+workItems);
		int[] workItemsIDs= StringArrayParameterUtils.splitSelectionAsIntArray(workItems);
		try {
			ItemNavigatorFilterBL.dropItemOnNode(nodeType,nodeObjectID,workItemsIDs,personBean.getObjectID(),locale,params);
			JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		} catch (ItemOperationException e) {
			if(LOGGER.isDebugEnabled()){
				LOGGER.error(ExceptionUtils.getStackTrace(e), e);
			}
			try {
				JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
				PrintWriter out = ServletActionContext.getResponse().getWriter();
				out.println(ItemNavigatorJSON.encodeException(e,locale));
			} catch (IOException ioEx) {
				LOGGER.error(ExceptionUtils.getStackTrace(ioEx),ioEx);
			}
		}catch (Exception e){
			LOGGER.error(ExceptionUtils.getStackTrace(e), e);
		}

		return null;
	}

	public String getResponsibles(){
		List responsibles=ItemNavigatorBL.getResponsibles(workItems,personBean,locale);
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(JSONUtility.encodeILabelBeanList(responsibles));
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e), e);
		}
		return null;
	}

	public String loadComboDataByFieldID() {
		List<ILabelBean>dataSource = new ArrayList<ILabelBean>();
		if(fieldID != null) {
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			ISelect selectFieldType = (ISelect)fieldTypeRT;

			SelectContext selectContext = new SelectContext();
			selectContext.setPersonID(personBean.getObjectID());
			selectContext.setCreate(false);
			selectContext.setLocale(locale);
			selectContext.setFieldID(fieldID);
			String selectedID = "";
			WorkItemContext workItemContext = null;
			boolean isNew = workItemID == null ? true :  false;
			ItemNavigatorTaskBean ganttBean = new ItemNavigatorTaskBean(isNew, workItemID, projectID, issueTypeID, null);
			if(workItemID != null) {
				workItemContext = FieldsManagerRT.getWorkItemContext(ganttBean, personBean, locale, true);
				selectContext.setWorkItemBean(workItemContext.getWorkItemBean());
				selectContext.setFieldConfigBean(workItemContext.getFieldConfigs().get(fieldID));
				dataSource = selectFieldType.loadEditDataSource(selectContext);
				if (workItemContext.getWorkItemBean().getAttribute(fieldID) !=null) {
					if(fieldTypeRT.isCustom()) {
						Object[] selectedValuesIntArr = (Object[]) workItemContext.getWorkItemBean().getAttribute(fieldID);
						if(selectedValuesIntArr.length > 0) {
							selectedID = selectedValuesIntArr[0].toString();
						}
					}else {
						Object selectedValue = workItemContext.getWorkItemBean().getAttribute(fieldID);
						selectedID = selectedValue.toString();
					}
				}
			}else {
				workItemContext = FieldsManagerRT.getWorkItemContext(ganttBean, personBean, locale, true);
				selectContext.setFieldConfigBean(workItemContext.getFieldConfigs().get(fieldID));
				selectContext.setWorkItemBean(workItemContext.getWorkItemBean());
				dataSource = selectFieldType.loadCreateDataSource(selectContext);
				if(dataSource != null && !dataSource.isEmpty()) {
					selectedID = Integer.toString(dataSource.get(0).getObjectID());
				}
			}
			String storeData = JSONUtility.encodeILabelBeanList(dataSource);
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			JSONUtility.appendJSONValue(sb, "storeData", storeData);
			//If there is no item selected in combo box then must be set to null, otherwise will produce JSON error.
			//Because: empty value in json is not supported: {"selectedID":}. We are adding null value instead, and client side will be handled like: {"selectedID": null}
			if("".equals(selectedID)) {
				selectedID = "null";
			}
			JSONUtility.appendJSONValue(sb, "selectedID", selectedID, true);
			sb.append("}");
			JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
		}
		return null;
	}

	public void loadFieldValue() {
		TWorkItemBean workItemBean = null;
		String fieldValueOrID = "";
		if(workItemID != null) {
			try {
				workItemBean = ItemBL.loadWorkItem(workItemID);
				fieldValueOrID = workItemBean.getAttribute(fieldID).toString();
			}catch(Exception ex) {
				LOGGER.debug(ex.getMessage(),ex);
			}

		}
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "value", fieldValueOrID, true);
		sb.append("}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
	}

	public String storeLastSelectedView(){
		personBean.setLastSelectedView(viewID);
		PersonBL.saveSimple(personBean);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}

	public String storeLastSelectedNavigator(){
		session.put(LAST_SELECTED_NAVIGATOR,objectID);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}

	public String indent() {
		LOGGER.debug("Indent: workItemID="+workItemID+" targetWorkItemID="+targetWorkItemID);
		List<ErrorData> errorList = new ArrayList<ErrorData>();
		//indent is possible only when there is a previous sibling
		workItemID= FieldsManagerRT.saveOneField(personBean.getObjectID(), workItemID, locale, false,
				SystemFields.INTEGER_SUPERIORWORKITEM,
				targetWorkItemID, true, null, errorList);

		if (!errorList.isEmpty()) {
			JSONUtility.encodeJSONFailure(servletResponse, "", 1);
		}else{
			JSONUtility.encodeJSONSuccess(servletResponse);
		}
		return null;
	}
	public String outdent() {
		LOGGER.debug("outdent: workItemID="+workItemID);
		List<ErrorData> errorList = new ArrayList<ErrorData>();
		TWorkItemBean workItemBean = null;
		try {
			workItemBean = DAOFactory.getFactory().getWorkItemDAO().loadByPrimaryKey(workItemID);
		} catch (ItemLoaderException e) {
			LOGGER.warn("Loading the workItemBean by outdent for key " + workItemID + " failed with " + e.getMessage(),e);
		}
		ReportBeans reportBeans=calculateReportBeans(session);
		if (workItemBean!=null) {
			Integer parentID = workItemBean.getSuperiorworkitem();
			if (parentID!=null) {
					TWorkItemBean parentWorkItemBean = null;
					try {
						parentWorkItemBean = DAOFactory.getFactory().getWorkItemDAO().loadByPrimaryKey(parentID);
					} catch (ItemLoaderException e) {
						LOGGER.warn("Loading the parent workItemBean by outdent for key " + parentID + " failed with " + e.getMessage(),e);
					}
					Integer grandParent = null;
					if (parentWorkItemBean!=null) {
						grandParent = parentWorkItemBean.getSuperiorworkitem();
					}
					Map<String, Object> contextInformation = new HashMap<String, Object>();
					contextInformation.put(TWorkItemBean.OUTDENT, Boolean.TRUE);
					List<Integer> nextSiblingIDs = new ArrayList<Integer>();
					List<ReportBean> nextSiblings = reportBeans.getNextSiblings(workItemID);
					for (Iterator<ReportBean> iterator = nextSiblings.iterator(); iterator.hasNext();) {
						ReportBean nextSibling = iterator.next();
						nextSiblingIDs.add(nextSibling.getWorkItemBean().getObjectID());
					}
					contextInformation.put(TWorkItemBean.NEXT_SIBLINGS, nextSiblingIDs);
					workItemID=FieldsManagerRT.saveOneField(personBean.getObjectID(), workItemID, locale, false,
							SystemFields.INTEGER_SUPERIORWORKITEM, grandParent, true, contextInformation, errorList);
				}
		}
		if (!errorList.isEmpty()) {
			JSONUtility.encodeJSONFailure(servletResponse, "", 1);
		}else{
			JSONUtility.encodeJSONSuccess(servletResponse);
		}
		return null;
	}
	public String changeOneField(){
		LOGGER.debug("Change One field:fieldID="+fieldID);
		int[] workItemsIDs= StringArrayParameterUtils.splitSelectionAsIntArray(workItems);
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		Object massValue=value;
		//prepare the values for specific (project dependent) lists as expected by the massOperation
		if(fieldID.equals(SystemFields.INTEGER_RELEASE)){
			TReleaseBean releaseBean= LookupContainer.getReleaseBean(value);
			Integer projectID=releaseBean.getProjectID();
			Map<Integer, Integer> targetValue=new HashMap<Integer,Integer>();
			targetValue.put(projectID,value);
			massValue=targetValue;
		} else {
			if (fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION) {
				TOptionBean optionBean = OptionBL.loadByPrimaryKey(value);
				Integer listID = optionBean.getList();
				SortedMap<Integer, Integer[]> customOptionValue = new TreeMap<Integer,Integer[]>();
				customOptionValue.put(listID, new Integer[] {value});
				massValue=customOptionValue;
			}
		}
		boolean confirmSave=false;
		String confirmSaveStr=null;
		if(params!=null){
			confirmSaveStr=params.get("confirmSave");
		}
		if(confirmSaveStr!=null && "true".equalsIgnoreCase(confirmSaveStr)){
			confirmSave=true;
		}
		try{
			MassOperationBL.saveExtern(workItemsIDs, fieldID, massValue, personBean.getObjectID(), locale, confirmSave);
		}catch (MassOperationException ex){
			ItemOperationException e= new ItemOperationException(ItemOperationException.TYPE_MASS_OPERATION,ex);
			JSONUtility.encodeJSON(ServletActionContext.getResponse(), ItemNavigatorJSON.encodeException(e,locale));
			return null;
		}
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}
	public String changeWBS() {
		LOGGER.debug("changeWBS: workItems="+workItems+", targetWorkItemID="+targetWorkItemID+", before="+before);
		int[] workItemsIDs= StringArrayParameterUtils.splitSelectionAsIntArray(workItems);
		for(int i=workItemsIDs.length-1;i>=0;i--){
			DAOFactory.getFactory().getWorkItemDAO().dropNearWorkItem(workItemsIDs[i], targetWorkItemID, before);
		}
		return null;
	}


	public String serveIconStream() {
		Integer listID = null;
		if (nodeType!=null) {
			listID = (-1)*Integer.valueOf(nodeType.replaceAll("ItemOperationSelect_", ""));
		}
		ListOptionIconBL.downloadForField(servletRequest, servletResponse, listID, nodeObjectID);
		return null;
	}

	public String removeParent(){
		if(workItemID!=null){
			List<ErrorData> errorList = new ArrayList<ErrorData>();
			FieldsManagerRT.saveOneField(personBean.getObjectID(), workItemID, locale, false,
					SystemFields.INTEGER_SUPERIORWORKITEM, null, false, null, errorList);
		}
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(JSONUtility.encodeJSONSuccess());
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e), e);
		}
		return null;
	}

	public String saveGanttSpecificConfigs() {
		if(ganttConfigNameToSave != null) {
			if(ganttConfigNameToSave.equals(TSiteBean.VALIDATE_RELATIONSHIPS_GANTT)) {
				ApplicationBean.getInstance().getSiteBean().setValidateRelationshipsGantt(ganttConfigValue);
			}
			if(ganttConfigNameToSave.equals(TSiteBean.SHOW_BOTH_GANTT)) {
				ApplicationBean.getInstance().getSiteBean().setShowBothGantt(ganttConfigValue);
			}

			if(ganttConfigNameToSave.equals(TSiteBean.SHOW_BASELINE)) {
				ApplicationBean.getInstance().getSiteBean().setShowBaseline(ganttConfigValue);
			}

			if(ganttConfigNameToSave.equals(TSiteBean.HIGHLIGHT_CRITICAL_PATH_GANTT)) {
				ApplicationBean.getInstance().getSiteBean().setHighlightCriticalPathGantt(ganttConfigValue);
			}

			List<ControlError> tmperr = new LinkedList<ControlError>();
			SiteConfigBL.saveTSite(ApplicationBean.getInstance().getSiteBean(),
					   ApplicationBean.getInstance(), tmperr, application);
			if(tmperr.isEmpty()) {
				JSONUtility.encodeJSONSuccess(servletResponse);
			}else {
				JSONUtility.encodeJSONFailure(1);
			}
		}
		return null;
	}


	public String closeWorkItem() {
		TWorkItemBean workItemBean = null;
		WorkItemContext ctx = null;

		ctx= ItemBL.editWorkItem(workItemID, personBean.getObjectID(), locale,true);
		if(ctx != null) {
			workItemBean = ctx.getWorkItemBean();
			if(workItemBean != null) {
				Integer[] projectIDs = {workItemBean.getProjectID()};
				Integer[] issueTypeIDs = {workItemBean.getListTypeID()};

				List<TStateBean>states = StatusBL.loadAllowedByProjectTypesAndIssueTypes(projectIDs, issueTypeIDs);
				TStateBean closedState = null;
				for (TStateBean aState : states) {
					if(aState.getStateflag().intValue() == TStateBean.STATEFLAGS.CLOSED) {
						closedState = aState;
					}
				}
				if(closedState != null) {
					List<ErrorData> errorList = new ArrayList<ErrorData>();
					workItemBean.setStateID(closedState.getObjectID());
					ItemBL.saveWorkItem(ctx, errorList, false);
					StringBuilder sb = new StringBuilder();
					if(!errorList.isEmpty()) {
						sb.append(ItemSaveJSON.encodeErrorData(errorList, ItemSaveBL.ERROR_CODE.GENERAL, locale));
					}else {
						sb.append("{");
						JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
						JSONUtility.appendIntegerValue(sb, "closedStateID", closedState.getObjectID());
						JSONUtility.appendStringValue(sb, "closedStateName", closedState.getLabel(), true);
						sb.append("}");
					}
					JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
				}
			}
		}
		return null;
	}

	private long setStartTime(String function) {
		long tm = new Date().getTime();
		LOGGER.debug("--> Begin time measurement of " + function);
		return tm;
	}

	private void printTime(String function, long startTime) {
		long tm = new Date().getTime();
		if (tm - startTime > 1000) {
			LOGGER.info("Possible system overload!");
		}
		LOGGER.debug("<-- End time measurement of " + function + ": " + (tm - startTime) + " ms");
	}

	private long getUsedMemory() {
		Runtime rt = Runtime.getRuntime();
		long mbyte = 1024L * 1024;
		return (rt.totalMemory() - rt.freeMemory())/mbyte;
	}

	private void checkMemory(long usedOld) {
		Runtime rt = Runtime.getRuntime();
		long mbyte = 1024L * 1024;
		long used = (rt.totalMemory() - rt.freeMemory()) / mbyte;
		if (rt.freeMemory() / mbyte < 100 ) {
			LOGGER.warn("Less than 100 MByte free memory available.");
		}
		LOGGER.debug("Used memory Delta: " + ( used - usedOld) + " MByte");
		LOGGER.debug("Used memory total: " + used + " MByte");
	}

	public String storeGranularityAndZoom() {
		session.put("ganttGranularity", ganttGranularity);
		return null;
	}

	public boolean isHasInitData() {
		return hasInitData;
	}
	public String getInitData() {
		return initData;
	}
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Integer getNodeObjectID() {
		return nodeObjectID;
	}

	public void setNodeObjectID(Integer nodeObjectID) {
		this.nodeObjectID = nodeObjectID;
	}

	public Integer getWorkItemID() {
		return workItemID;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public Map getApplication() {
		return application;
	}

	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String getViewID() {
		return viewID;
	}

	public void setViewID(String viewID) {
		this.viewID = viewID;
	}

	//Only parameters IN
	public void setQueryID(Integer queryID) {
		this.queryID = queryID;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public Map<String, String> getDashboardParams() {
		return dashboardParams;
	}

	public void setDashboardParams(Map<String, String> dashboardParams) {
		this.dashboardParams = dashboardParams;
	}

	public void setTargetWorkItemID(Integer targetWorkItemID) {
		this.targetWorkItemID = targetWorkItemID;
	}

	public void setBefore(boolean before) {
		this.before = before;
	}

	public void setWorkItems(String workItems) {
		this.workItems = workItems;
	}

	public Integer getQueryContextID() {
		return queryContextID;
	}

	public void setQueryContextID(Integer queryContextID) {
		this.queryContextID = queryContextID;
	}

	public void setActionID(Integer actionID) {
		this.actionID = actionID;
	}

	public boolean isLite() {
		return lite;
	}

	public void setLite(boolean lite) {
		this.lite = lite;
	}

	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getGanttGranularity() {
		return ganttGranularity;
	}
	public void setGanttGranularity(String ganttGranularity) {
		this.ganttGranularity = ganttGranularity;
	}

	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public boolean isRemoveViolatedLinksAndCascade() {
		return removeViolatedLinksAndCascade;
	}
	public void setRemoveViolatedLinksAndCascade(
			boolean removeViolatedLinksAndCascade) {
		this.removeViolatedLinksAndCascade = removeViolatedLinksAndCascade;
	}
	public String getWrongDependencies() {
		return wrongDependencies;
	}
	public void setWrongDependencies(String wrongDependencies) {
		this.wrongDependencies = wrongDependencies;
	}

	public void setForceAllItems(boolean forceAllItems) {
		this.forceAllItems = forceAllItems;
	}

	public boolean isMobilApplication() {
		return mobilApplication;
	}
	public void setMobilApplication(boolean mobilApplication) {
		this.mobilApplication = mobilApplication;
	}

	public void setStart(int start) {
		this.starts = start;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public boolean isValidateRelationshipsGantt() {
		return validateRelationshipsGantt;
	}
	public void setValidateRelationshipsGantt(boolean validateRelationshipsGantt) {
		this.validateRelationshipsGantt = validateRelationshipsGantt;
	}

	public String getGanttConfigNameToSave() {
		return ganttConfigNameToSave;
	}

	public void setGanttConfigNameToSave(String ganttConfigNameToSave) {
		this.ganttConfigNameToSave = ganttConfigNameToSave;
	}

	public boolean isGanttConfigValue() {
		return ganttConfigValue;
	}
	public void setGanttConfigValue(boolean ganttConfigValue) {
		this.ganttConfigValue = ganttConfigValue;
	}

	public void setFromSession(boolean fromSession) {
		this.fromSession = fromSession;
	}

	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
    public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}

	public String getGntPDFLocation() {
		return gntPDFLocation;
	}
	public void setGntPDFLocation(String gntPDFLocation) {
		this.gntPDFLocation = gntPDFLocation;
	}

	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public boolean isProject() {
		return isProject;
	}
	public void setIsProject(boolean isProject) {
		this.isProject = isProject;
	}
	public String getGanttTaskStore() {
		return ganttTaskStore;
	}
	public void setGanttTaskStore(String ganttTaskStore) {
		this.ganttTaskStore = ganttTaskStore;
	}
	public Integer getIssueTypeID() {
		return issueTypeID;
	}
	public void setIssueTypeID(Integer issueTypeID) {
		this.issueTypeID = issueTypeID;
	}

	public boolean isValidateBeforeSave() {
		return validateBeforeSave;
	}
	public void setValidateBeforeSave(boolean validateBeforeSave) {
		this.validateBeforeSave = validateBeforeSave;
	}

	public String getGanttDependencyStore() {
		return ganttDependencyStore;
	}
	public void setGanttDependencyStore(String ganttDependencyStore) {
		this.ganttDependencyStore = ganttDependencyStore;
	}
	public String getGanttDependenciesToRemove() {
		return ganttDependenciesToRemove;
	}
	public void setGanttDependenciesToRemove(String ganttDependenciesToRemove) {
		this.ganttDependenciesToRemove = ganttDependenciesToRemove;
	}
}
