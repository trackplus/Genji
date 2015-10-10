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

package com.aurel.track.report.datasource;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;

import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.ReportBeanLoader;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.report.execute.IDescriptionAttributes;
import com.aurel.track.admin.customize.category.report.execute.ReportBeansToXML;
import com.aurel.track.admin.customize.category.report.execute.ReportExporter;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.itemNavigator.IssueListViewPlugin;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.itemNavigator.layout.LayoutTO;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.itemNavigator.viewPlugin.ViewDescriptorBL;
import com.aurel.track.itemNavigator.viewPlugin.ViewPluginBL;
import com.aurel.track.persist.ReportBeanHistoryLoader;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanExpandContext;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;

/**
 * Datasource implemetation for ReportBeans
 * @author Tamas
 *
 */
public class ReportBeanDatasource extends BasicDatasource {
	
	private static final Logger LOGGER = LogManager.getLogger(ReportBeanDatasource.class);
	
	/**
	 * Gets the data source object (a Document object in this case) retrieved using the parameters settings
	 * @param parameters
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param templateDescriptionMap
	 * @param templateID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public Object getDatasource(Map<String, String[]> parameters, DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> templateDescriptionMap,
			Integer templateID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		ReportBeans reportBeans = null;
		Boolean fromIssueNavigator = (Boolean)contextMap.get(CONTEXT_ATTRIBUTE.FROM_ISSUE_NAVIGATOR);
		Integer dashboardProjectOrReleaseID = (Integer)contextMap.get(CONTEXT_ATTRIBUTE.DASHBOARD_PROJECT_RELEASE_ID);
		boolean useProjectSpecificID = false;
		Boolean projectSpecificID = (Boolean)contextMap.get(IPluggableDatasource.CONTEXT_ATTRIBUTE.USE_PROJETC_SPECIFIC_ID);
		if (projectSpecificID!=null) {
			useProjectSpecificID = projectSpecificID.booleanValue();
		}
		String queryName = null;
		String queryExpression = null;
		if (fromIssueNavigator!=null && fromIssueNavigator.booleanValue()) {
			ReportBeanExpandContext reportBeanExpandContext = null;
			QueryContext queryContext=ItemNavigatorBL.loadLastQuery(personBean.getObjectID(),locale);
			boolean plain = false;
			IssueListViewPlugin plugin = null;
			if (queryContext!=null) {
				Integer queryID = queryContext.getQueryID();
				Integer queryType = queryContext.getQueryType();
				//needed in report overview jasper design to get the query based layout
				contextMap.put(CONTEXT_ATTRIBUTE.QUERY_ID, queryID);
				contextMap.put(CONTEXT_ATTRIBUTE.QUERY_TYPE, queryType);
				reportBeanExpandContext = getReportBeanExpandContext(queryType, queryID, personBean, locale);
				if (templateID==null) {
					//dynamic pdf/excel report from item navigator: plain is get from the viewID (the export should mirror the actual order in item navigator, i.e. if plain view then create the reportbenas as plainData)
					String viewID = NavigatorLayoutBL.getSavedItemFilterView(queryID, queryType);
					if (viewID==null || "".equals(viewID)) {
						viewID = personBean.getLastSelectedView();
						LOGGER.debug("Get the last used view by person: " + personBean.getLabel() + ": " +  viewID);
					}
					if (viewID!=null) {
						IssueListViewDescriptor issueListViewDescriptor = ViewDescriptorBL.getDescriptor(viewID);
						if (issueListViewDescriptor!=null) {
							plain = issueListViewDescriptor.isPlainData();
							plugin = ViewPluginBL.getPlugin(issueListViewDescriptor.getTheClassName());
						}
					}
				}
			}
			//from issue navigator: datasource is the last query
			List<Integer> workItemIDs = (List<Integer>)contextMap.get(CONTEXT_ATTRIBUTE.WORKITEMIDS);
			if (workItemIDs!=null && !workItemIDs.isEmpty()) {
				List<ReportBean> reportBeanList = LoadItemIDListItems.getReportBeansByWorkItemIDs(
						GeneralUtils.createIntArrFromIntegerList(workItemIDs), false, personBean.getObjectID(), locale,
						true, true, true, true, true, true, false, true, false);
				
				reportBeans = new ReportBeans(reportBeanList, locale, reportBeanExpandContext, false, plain);
			} else {
				if (queryContext!=null) {
					if (plugin!=null) {
						reportBeanExpandContext  = plugin.updateReportBeanExpandContext(personBean, new LinkedList<ReportBean>(), reportBeanExpandContext, new HashMap(), queryContext, true);
					}
					reportBeans = ItemNavigatorBL.executeQuery(personBean, locale, queryContext, reportBeanExpandContext, plain);
					queryName = queryContext.getQueryName();
				}
			}
		} else {
			if (dashboardProjectOrReleaseID!=null) {
				//reports called from dashboard: the project/release of the project/release specific dashboard
				//or the project/release set as datasource for "global" dashboard
				Integer dashboardID = (Integer)contextMap.get(CONTEXT_ATTRIBUTE.DASHBOARD_ID);
				queryName = ItemNavigatorBL.getQueryName(QUERY_TYPE.DASHBOARD, dashboardID,-1, locale);
				FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(dashboardProjectOrReleaseID, true, true, false);
				List<ReportBean> reportBeanList = LoadTreeFilterItems.getTreeFilterReportBeansForReport(filterUpperTO, null, null, personBean, locale);
				reportBeans = new ReportBeans(reportBeanList, locale, getReportBeanExpandContext(ItemNavigatorBL.QUERY_TYPE.DASHBOARD, dashboardID, personBean, locale), true);
				contextMap.put(CONTEXT_ATTRIBUTE.QUERY_ID, dashboardProjectOrReleaseID);
				contextMap.put(CONTEXT_ATTRIBUTE.QUERY_TYPE, ItemNavigatorBL.QUERY_TYPE.DASHBOARD);
			} else {
				//from report configuration page: first save the parameters into the database, then get the reportBeans
				Map<String, Object> savedParamsMap = new HashMap<String, Object>();
				String paramSettings = loadParamObjectsAndPropertyStringsAndFromStringArrParams(parameters, locale, savedParamsMap);
				saveParameters(paramSettings, personBean.getObjectID(), templateID);
				Integer datasourceType = (Integer)savedParamsMap.get(PARAMETER_NAME.DATASOURCETYPE);
				if (datasourceType==null) {
					LOGGER.warn("No datasourceType was selected");
					return null;
				}
				if (datasourceType.intValue()==DATASOURCE_TYPE.PROJECT_RELEASE) {
					Integer projectOrReleaseID = (Integer)savedParamsMap.get(PARAMETER_NAME.PROJECT_OR_RELEASE_ID);
					if (projectOrReleaseID==null) {
						LOGGER.warn("No project/release was selected");
						return null;
					} else {
						FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(projectOrReleaseID, true, true, false);
						List<ReportBean> reportBeanList = LoadTreeFilterItems.getTreeFilterReportBeansForReport(filterUpperTO, null, null, personBean, locale);
						Integer queryType = ItemNavigatorBL.QUERY_TYPE.PROJECT_RELEASE;
						Integer queryID = projectOrReleaseID;
						reportBeans = new ReportBeans(reportBeanList, locale, getReportBeanExpandContext(queryType, queryID, personBean, locale), true);
						queryName = ItemNavigatorBL.getQueryName(QUERY_TYPE.PROJECT_RELEASE, projectOrReleaseID,-1, locale);
						contextMap.put(CONTEXT_ATTRIBUTE.QUERY_ID, queryID);
						contextMap.put(CONTEXT_ATTRIBUTE.QUERY_TYPE, queryType);
					}
				} else {
					Integer filterID = (Integer)savedParamsMap.get(PARAMETER_NAME.FILTER_ID);
					if (filterID==null) {
						LOGGER.warn("No filter was selected");
						return null;
					} else {
						Integer queryType = ItemNavigatorBL.QUERY_TYPE.SAVED;
						Integer queryID = filterID;
						reportBeans = FilterExecuterFacade.getSavedFilterReportBeans(filterID, locale,
									personBean, new LinkedList<ErrorData>(), getReportBeanExpandContext(queryType, queryID, personBean, locale), null, null, true);
						contextMap.put(CONTEXT_ATTRIBUTE.QUERY_ID, queryID);
						contextMap.put(CONTEXT_ATTRIBUTE.QUERY_TYPE, queryType);
						ILabelBean labelBean = TreeFilterFacade.getInstance().getByKey(filterID);
						if (labelBean!=null) {
							queryName = labelBean.getLabel();
						}
					}
				}
			}
		}

		return getDocument(templateDescriptionMap, contextMap,
                           reportBeans,
                           personBean,
                           locale,
                           queryName,
                           queryExpression,
                           useProjectSpecificID,
                           templateID);
	}
	
	/**
	 * Overwrite this method for other types of export formats.
	 * @param templateDescriptionMap
	 * @param contextMap
	 * @param reportBeans
	 * @param personBean
	 * @param locale
	 * @param queryName
	 * @param queryExpression
	 * @param useProjectSpecificID
	 * @return a DOM document, but be aware that the real output might be generated as a side oroduct.
	 */
	protected Object getDocument(Map<String,Object> templateDescriptionMap,
								 Map<String, Object> contextMap,
			                     ReportBeans reportBeans,
			                     TPersonBean personBean,
			                     Locale locale,
			                     String queryName,
			                     String queryExpression,
			                     boolean useProjectSpecificID,
			                     Integer templateID){
		boolean withInlineComment = false;
		String exportFormat = (String)contextMap.get(CONTEXT_ATTRIBUTE.EXPORT_FORMAT);
		if (exportFormat==null) {
			//not dynamically generated, get it from description
			if (templateDescriptionMap!=null) {
				exportFormat = (String)templateDescriptionMap.get(IDescriptionAttributes.FORMAT);
			}
		} else {
			//dynamically generated content
			withInlineComment = true;
		}
		boolean withHistory = false;
		Boolean history = false;
		if (templateDescriptionMap!=null) {
			if (templateDescriptionMap.get(IDescriptionAttributes.HISTORY)!=null) {
				try {
					history = Boolean.valueOf((String)templateDescriptionMap.get(IDescriptionAttributes.HISTORY));
					if (history!=null) {
						withHistory = history.booleanValue();
					}
				} catch (Exception e) {
					LOGGER.warn("history should be a boolean string " + e.getMessage(), e);
				}
			}
		}
		//if it will be exported to CSV or XLS then make the long texts plain, otherwise simplified HTML
		boolean longTextIsPlain = ReportExporter.FORMAT_CSV.equals(exportFormat) || 
			ReportExporter.FORMAT_XLS.equals(exportFormat) || 
			ReportExporter.FORMAT_XML.equals(exportFormat);
		return getDocumentFromReportBeans(reportBeans, withHistory, withInlineComment,
			personBean, locale, queryName, queryExpression, longTextIsPlain, useProjectSpecificID);
	}
	
	/**
	 * Get the reportBeanExpandContext: grouping and sorting is important for ordering the result
	 * that means the order of items in report should be the same as executing the same filter from item navigator
	 * @param queryType
	 * @param queryID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	private static ReportBeanExpandContext getReportBeanExpandContext(Integer queryType, Integer queryID, TPersonBean personBean, Locale locale) {
		ReportBeanExpandContext reportBeanExpandContext = null;
		if (queryType!=null && queryID!=null) {
			LayoutTO layoutTO = NavigatorLayoutBL.loadLayout(personBean, locale, queryType, queryID, true);
			reportBeanExpandContext = new ReportBeanExpandContext();
			reportBeanExpandContext.setGroupingSorting(reportBeanExpandContext, layoutTO);
		}
		return reportBeanExpandContext;
	}
	
	/**
	 * Transform the reportBeans into DOM Document
	 * @param reportBeans
	 * @param withHistory
	 * @param withInlineComment whether the comment field should be set explicitly with merging all comments found
	 * @param personBean
	 * @param locale
	 * @param queryName
	 * @param queryExpression
	 * @param longTextIsPlain
	 * @param useProjectSpecificID
	 * @return
	 */
	protected static Document getDocumentFromReportBeans(ReportBeans reportBeans, boolean withHistory, boolean withInlineComment, TPersonBean personBean, 
			Locale locale, String queryName, String queryExpression, boolean longTextIsPlain, boolean useProjectSpecificID) {
		if (reportBeans == null) {
			return null;
		}
		Integer personID = personBean.getObjectID();
		List<ReportBean> items = reportBeans.getItems();
		if (items == null) {
			return null;
		}
		ReportBeanLoader.addISOValuesToReportBeans(items, personBean.getObjectID(), locale);
		//add the history data
		LONG_TEXT_TYPE longTextType = LONG_TEXT_TYPE.ISSIMPLIFIEDHTML;
		if (longTextIsPlain) {
			longTextType = LONG_TEXT_TYPE.ISPLAIN;
		}
		//add the history to each ReportBean: the long fields history elements can be transformed to the longTextType
		//needed and consequently we can spare storing also the real attribute values in the HistoryValues objects
		//(near the showValues) but the actual long fields cannot be transformed at this moment because 
		//they are stored in the session and it would affect the future renderings of the ReportBeans
		if (withHistory) {
			items = (List)ReportBeanHistoryLoader.getReportBeanWithHistoryList(items, locale, false,
				true, true, null, true, true, true, true, false, personID, null, null, null, true, longTextType);
		} else {
			//include comments in the comment field's show value
			if (withInlineComment) {
				items = (List)ReportBeanHistoryLoader.getReportBeanWithHistoryList(items, locale, false,
						true, false, null, false, false, false, false, false, personID, null, null, null, true, longTextType);
				for (ReportBean reportBean : items) { 
					ReportBeanWithHistory reportBeanWithHistory = (ReportBeanWithHistory)reportBean;
					List<HistoryValues> comments = reportBeanWithHistory.getComments();
					StringBuilder stringBuilder = new StringBuilder();
					for (HistoryValues historyValues : comments) {
						stringBuilder.append(DateTimeUtils.getInstance().formatGUIDateTime(historyValues.getLastEdit(), locale));
						stringBuilder.append(" ");
						stringBuilder.append(historyValues.getChangedByName());
						stringBuilder.append('\n');
						stringBuilder.append(historyValues.getNewShowValue());
						stringBuilder.append('\n');
					}
					reportBean.getWorkItemBean().setComment(stringBuilder.toString());
				}
			}
		}
		ReportBeansToXML converter = new ReportBeansToXML();
		return converter.convertToDOM(items, withHistory, locale, personBean.getFullName(), 
			queryName, queryExpression, useProjectSpecificID);
	}

	
	/**
	 * Serializes the raw data source object into an OutputStream (typically an XML file)  
	 * @param outputStream typically the OutputStream of the HttpResponse object 
	 * @param datasource typically DOM Document object or any other proprietary object
	 * @return typically an XML file
	 */
	public void serializeDatasource(OutputStream outputStream, Object datasource) {
		ReportBeansToXML.convertToXml(outputStream, (Document)datasource);
	}
}
