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

package com.aurel.track.itemNavigator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.item.massOperation.MassOperationException;
import com.aurel.track.item.massOperation.MassOperationJSON;
import com.aurel.track.item.operation.ItemOperationException;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuJSON;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuTO;
import com.aurel.track.itemNavigator.layout.LayoutTO;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.itemNavigator.navigator.Navigator;
import com.aurel.track.itemNavigator.navigator.NavigatorJSON;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.ReportItemJSON;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.PropertiesHelper;

/**
 * JSON helper for ItemNavigatorAction
 * @author Adrian Bojani
 */
public class ItemNavigatorJSON {
	public static String encodeJSON(LayoutTO layoutTO, ReportBeans reportBeans,Navigator nav,Integer lastSelectedNavigator,
			List<FilterInMenuTO> lastQueries,int totalCount, int count,Integer overflowItems,
			List<IssueListViewDescriptor> descriptors,String lastSelectedView,ApplicationBean appBean,Locale locale,
			QueryContext queryContext,TPersonBean personBean,Integer workItemID,Integer actionID,
			Integer queryFieldCSS,Map<Integer,String> cssRules,boolean includeLongFields,boolean plainData,String extraJSON,Set<Integer> exclusiveShortFields,
			boolean includePercentDone, boolean tooManyItems, boolean showDragDropInfoMsg,boolean useIssueTypeIcon){
		boolean  useProjectSpecificID=false;
		if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
			useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		boolean includeCharts=(appBean.getEdition() >= 2);


		JSONUtility.appendJSONValue(sb, "lastQueries", FilterInMenuJSON.encodeFiltersInMenu(lastQueries));
		JSONUtility.appendIntegerValue(sb,"totalCount",totalCount);
		JSONUtility.appendIntegerValue(sb,"count",count);
		JSONUtility.appendIntegerValue(sb,"overflowItems",overflowItems);

		JSONUtility.appendBooleanValue(sb, "showDragDropInfoMsg", showDragDropInfoMsg);

		/* Gantt HOLIDAYS config
		 * The holidays array contains the free days dates.
		 * This code block will be used when Genji calendar is implemented.
		 * This is sample for useing.
		 *
		 * */
		StringBuilder holidays = new StringBuilder();
		holidays.append("[");
		holidays.append("]");
		JSONUtility.appendJSONValue(sb, "holidays", holidays.toString());
		JSONUtility.appendBooleanValue(sb, "isPrintItemEditable", personBean.isPrintItemEditable());
		JSONUtility.appendBooleanValue(sb, "showBaseline", ApplicationBean.getInstance().getSiteBean().getShowBaseline());
		JSONUtility.appendBooleanValue(sb, "showBoth", ApplicationBean.getInstance().getSiteBean().getShowBothGantt());
		JSONUtility.appendBooleanValue(sb, "highlightCriticalPath", ApplicationBean.getInstance().getSiteBean().getHighlightCriticalPathGantt());
		JSONUtility.appendBooleanValue(sb, "validateRelationships", ApplicationBean.getInstance().getSiteBean().getValidateRelationshipsGantt());
		JSONUtility.appendBooleanValue(sb, "isActiveTopDownDate", ApplicationBean.getInstance().getBudgetActive());
		JSONUtility.appendBooleanValue(sb, "tooManyItems", tooManyItems);
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
		/* ENDS of Gantt HOLIDAYS/MILESTONE config */


		JSONUtility.appendJSONValue(sb,"queryContext",encodeQueryContext(queryContext));
		if(cssRules!=null){
			JSONUtility.appendIntegerValue(sb,"queryFieldCSS",queryFieldCSS);
			JSONUtility.appendJSONValue(sb,"cssRules",ItemNavigatorJSON.encodeCssRules(cssRules));
		}
		boolean isFilterView=false;
		boolean maySaveFilterLayout=false;
		TQueryRepositoryBean  queryRepositoryBean=NavigatorLayoutBL.getSavedQuery(queryContext.getQueryID(), queryContext.getQueryType());
		if(queryRepositoryBean!=null){
			String viewID = NavigatorLayoutBL.getSavedItemFilterView(queryContext.getQueryID(), queryContext.getQueryType());
			isFilterView=viewID!=null&&!viewID.equals("");
			if (isFilterView) {
				maySaveFilterLayout=NavigatorLayoutBL.isModifiable(queryRepositoryBean, personBean);
			}
		}
		JSONUtility.appendJSONValue(sb, "layout", ReportItemJSON.encodeLayout(layoutTO, useProjectSpecificID));
		JSONUtility.appendStringParametersMap(sb, "dashboardParams", queryContext.getDashboardParams());
		JSONUtility.appendJSONValue(sb, "issueListViews", ReportItemJSON.encodeIssueListViewDescriptors(descriptors, locale));
		JSONUtility.appendStringValue(sb, "lastSelectedView", lastSelectedView);
		JSONUtility.appendBooleanValue(sb, "isFilterView", isFilterView);
		JSONUtility.appendBooleanValue(sb, "maySaveFilterLayout", maySaveFilterLayout);
		JSONUtility.appendBooleanValue(sb, "summaryItemsBehavior", ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior());
		JSONUtility.appendStringValue(sb, "lastSelectedViewPerson", personBean.getLastSelectedView());
		JSONUtility.appendJSONValue(sb, "issues", ReportItemJSON.encodeReportBeans(reportBeans, layoutTO, includeLongFields,locale,useProjectSpecificID,personBean,queryFieldCSS,plainData,exclusiveShortFields,includePercentDone,useIssueTypeIcon));
		JSONUtility.appendJSONValue(sb, "listViewData", extraJSON);
		JSONUtility.appendIntegerValue(sb,"workItemID",workItemID);
		if(nav!=null){
			JSONUtility.appendJSONValue(sb,"navigator", NavigatorJSON.encode(nav));
			JSONUtility.appendIntegerValue(sb,"lastSelectedNavigator",lastSelectedNavigator);
		}
		boolean settingsVisible=false;
		String settingsVisibleStr = PropertiesHelper.getProperty(personBean.getMoreProperties(), TPersonBean.ITEM_NAVIGATOR_SETTINGS_VISIBLE);
		if(settingsVisibleStr!=null){
			settingsVisible= settingsVisibleStr.equalsIgnoreCase("true");
		}

		boolean filterEditVisible=false;
		String filterEditVisibleStr = PropertiesHelper.getProperty(personBean.getMoreProperties(), TPersonBean.ITEM_NAVIGATOR_FILTER_EDIT_VISIBLE);
		if(filterEditVisibleStr!=null){
			filterEditVisible= filterEditVisibleStr.equalsIgnoreCase("true");
		}

		JSONUtility.appendBooleanValue(sb, "settingsVisible", settingsVisible);
		JSONUtility.appendBooleanValue(sb, "filterEditVisible", filterEditVisible);
		JSONUtility.appendIntegerValue(sb,"actionID",actionID);
		JSONUtility.appendBooleanValue(sb, "includeCharts", includeCharts,true);
		sb.append("}");
		return sb.toString();
	}
	public static String encodeQueryContext(QueryContext queryContext){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb,"id",queryContext.getId());
		JSONUtility.appendIntegerValue(sb,"queryType",queryContext.getQueryType());
		JSONUtility.appendIntegerValue(sb,"queryID",queryContext.getQueryID());
		JSONUtility.appendStringValue(sb,"iconCls",queryContext.getIconCls());
		JSONUtility.appendStringValue(sb,"queryName",queryContext.getQueryName(),true);
		sb.append("}");
		return sb.toString();
	}
	public static String encodeJSONCanDropOnNode(Set<TWorkItemBean> invalidItems){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb,JSONUtility.JSON_FIELDS.SUCCESS,true);
		boolean  canDrop=(invalidItems==null||invalidItems.isEmpty());
		sb.append("data:{");
		if(!canDrop){
			sb.append("invalidItems:[");
			Iterator<TWorkItemBean> it=invalidItems.iterator();
			while(it.hasNext()){
				TWorkItemBean workItemBean=it.next();
				sb.append("{");
				JSONUtility.appendIntegerValue(sb,"id",workItemBean.getObjectID());
				JSONUtility.appendStringValue(sb,"title",workItemBean.getSynopsis(),true);
				sb.append("}");
			}
			sb.append("],");
		}
		JSONUtility.appendBooleanValue(sb,"canDrop",canDrop,true);
		sb.append("}}");
		return sb.toString();
	}
	public static String encodeException(ItemOperationException itemOperationException,Locale locale){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false);


		switch (itemOperationException.getType()){
			case ItemOperationException.TYPE_COMMON:{
				JSONUtility.appendStringValue(sb,"errorMessage",itemOperationException.getMessage());
				break;
			}
			case ItemOperationException.TYPE_MASS_OPERATION:{
				MassOperationException massEx=(MassOperationException)itemOperationException.getCause();
				JSONUtility.appendJSONValue(sb,"massOperation",MassOperationJSON.getErrorMessagesJSON(massEx,locale));
			}
		}
		JSONUtility.appendIntegerValue(sb,"type",itemOperationException.getType(),true);
		sb.append("}");
		return sb.toString();
	}

	public static String encodeCssRules(Map<Integer,String> cssRules){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(cssRules!=null){
			Iterator<Integer> it=cssRules.keySet().iterator();
			while (it.hasNext()){
				Integer key=it.next();
				sb.append("{");
				JSONUtility.appendIntegerValue(sb,"id",key);
				JSONUtility.appendStringValue(sb,"rule",cssRules.get(key),true);
				sb.append("}");
				if(it.hasNext()){
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
