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

package com.aurel.track.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.exchange.msProject.importer.LinkLagBL;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemDetailBL;
import com.aurel.track.item.budgetCost.BudgetPlanExpenseJSON;
import com.aurel.track.item.budgetCost.PercentDoneUtil;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.itemNavigator.ItemTreeNode;
import com.aurel.track.itemNavigator.layout.LayoutTO;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldTO;
import com.aurel.track.itemNavigator.layout.column.ColumnFieldsBL;
import com.aurel.track.itemNavigator.layout.column.LayoutColumnsBL;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.itemNavigator.layout.group.SortFieldTO;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.linkType.ItemLinkSpecificData;
import com.aurel.track.linkType.MsProjectLinkSpecificData;
import com.aurel.track.linkType.MsProjectLinkTypeBL;
import com.aurel.track.persist.ReportBeanHistoryLoader;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanLink;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.report.group.GroupLimitBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.numberFormatter.DoubleWithDecimalDigitsNumberFormatUtil;

public class ReportItemJSON {
	private static final int ITEM_OK = 0;
	private static final int ITEM_DUE_SOON = 1;
	private static final int ITEM_OVERDUE = 2;

	private static int index = 0;

	public static String encodeReportBeans(ReportBeans reportBeans,
			LayoutTO layoutTO, boolean includeLongFields, Locale locale,
			boolean useProjectSpecificID, TPersonBean personBean,
			Integer queryFieldCSS, boolean plainData,
			Set<Integer> exclusiveShortFields, boolean includePercentDone,
			boolean useIssueTypeIcon) {
		StringBuilder sb = new StringBuilder();
		index = 0;
		if (reportBeans != null) {
			List<ItemTreeNode> rows = reportBeans
					.getReportRows(/* plainData */);
			Map<Integer, List<HistoryValues>> fieldChangeMap = new HashMap<Integer, List<HistoryValues>>();
			Map<Integer, List<TBudgetBean>> plannedValueMap = new HashMap<Integer, List<TBudgetBean>>();
			Map<Integer, List<TBudgetBean>> budgetMap = new HashMap<Integer, List<TBudgetBean>>();
			Map<Integer, List<TCostBean>> costMap = new HashMap<Integer, List<TCostBean>>();
			Map<Integer, List<HistoryValues>> commentMap = new HashMap<Integer, List<HistoryValues>>();
			List<ReportBean> reporBeanList = reportBeans.getItems();
			List<ColumnFieldTO> columnFields = layoutTO.getColumnFields();// reportItemLayout.getReportLayout();
			if (includeLongFields) {
				boolean commentSelected = LayoutColumnsBL.isInLayout(
						SystemFields.INTEGER_COMMENT, columnFields);
				boolean fieldChangeHistorySelected = LayoutColumnsBL
						.isInLayout(
								TReportLayoutBean.PSEUDO_COLUMNS.HISTORY_LIST,
								columnFields);
				boolean planHistorySelected = LayoutColumnsBL.isInLayout(
						TReportLayoutBean.PSEUDO_COLUMNS.PLAN_HISTORY_LIST,
						columnFields);
				boolean budgetHistorySelected = LayoutColumnsBL.isInLayout(
						TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_HISTORY_LIST,
						columnFields);
				boolean costsSelected = LayoutColumnsBL.isInLayout(
						TReportLayoutBean.PSEUDO_COLUMNS.COST_LIST,
						columnFields);
				if (commentSelected || fieldChangeHistorySelected
						|| planHistorySelected || budgetHistorySelected
						|| costsSelected) {
					List<ReportBeanWithHistory> reportBeanWithHistoryList = ReportBeanHistoryLoader
							.getReportBeanWithHistoryList(reporBeanList,
									locale, false, commentSelected,
									fieldChangeHistorySelected, null,
									budgetHistorySelected, planHistorySelected,
									costsSelected, true, false,
									personBean.getObjectID(), null, null, null,
									false,
									HistoryLoaderBL.LONG_TEXT_TYPE.ISFULLHTML);
					for (ReportBeanWithHistory reportBeanWithHistory : reportBeanWithHistoryList) {
						Integer workItemID = reportBeanWithHistory
								.getWorkItemBean().getObjectID();
						if (commentSelected) {
							List<HistoryValues> commentsList = reportBeanWithHistory
									.getComments();
							if (commentsList != null && !commentsList.isEmpty()) {
								commentMap.put(workItemID, commentsList);
							}
						}
						if (fieldChangeHistorySelected) {
							Map<Integer, Map<Integer, HistoryValues>> workItemHistoryMap = reportBeanWithHistory
									.getHistoryValuesMap();
							List<HistoryValues> fieldChangeHistoryList = HistoryLoaderBL
									.setTransactionLimits(HistoryLoaderBL
											.getHistoryValuesList(
													workItemHistoryMap, false));
							if (fieldChangeHistoryList != null
									&& !fieldChangeHistoryList.isEmpty()) {
								fieldChangeMap.put(workItemID,
										fieldChangeHistoryList);
							}
						}
						if (planHistorySelected) {
							List<TBudgetBean> plannedValueHistory = reportBeanWithHistory
									.getPlannedValueHistory();
							if (plannedValueHistory != null
									&& !plannedValueHistory.isEmpty()) {
								plannedValueMap.put(workItemID,
										plannedValueHistory);
							}
						}
						if (budgetHistorySelected) {
							List<TBudgetBean> budgetHistory = reportBeanWithHistory
									.getBudgetHistory();
							if (budgetHistory != null
									&& !budgetHistory.isEmpty()) {
								budgetMap.put(workItemID, budgetHistory);
							}
						}
						if (costsSelected) {
							List<TCostBean> costs = reportBeanWithHistory
									.getCosts();
							if (costs != null && !costs.isEmpty()) {
								costMap.put(workItemID, costs);
							}
						}
					}
				}
			}
			Map<Integer, Double> workItemIdToPercent = null;
			Map<Object, Double> groupValueToPercent = null;
			Map<Object, Date> groupValueToStartDate = new HashMap<Object, Date>();
			Map<Object, Date> groupValueToEndDate = new HashMap<Object, Date>();
			Map<Object, Date> groupValueToTopDownStartDate = new HashMap<Object, Date>();
			Map<Object, Date> groupValueToTopDownEndDate = new HashMap<Object, Date>();
			Map<Integer, Double> projectWorkingHoursMap = new HashMap<Integer, Double>();
			if (includePercentDone) {
				workItemIdToPercent = PercentDoneUtil.getItemPercentsDone(
						reportBeans, personBean.getObjectID(),
						projectWorkingHoursMap);
				groupValueToPercent = PercentDoneUtil.getGroupPercentsDone(
						rows, projectWorkingHoursMap, groupValueToStartDate,
						groupValueToEndDate, groupValueToTopDownStartDate,
						groupValueToTopDownEndDate);
			}
			sb.append(encodeNodes(rows, layoutTO, includeLongFields, locale,
					useProjectSpecificID, personBean, projectWorkingHoursMap,
					fieldChangeMap, budgetMap, plannedValueMap, costMap,
					commentMap, queryFieldCSS, exclusiveShortFields,
					workItemIdToPercent, groupValueToPercent,
					groupValueToStartDate, groupValueToEndDate,
					groupValueToTopDownStartDate, groupValueToTopDownEndDate,
					useIssueTypeIcon));
		} else {
			sb.append("[]");
		}
		return sb.toString();
	}

	private static String encodeNodes(List<ItemTreeNode> nodes,
			LayoutTO layoutTO, boolean includeLongFields, Locale locale,
			boolean useProjectSpecificID, TPersonBean personBean,
			Map<Integer, Double> projectWorkingHoursMap,
			Map<Integer, List<HistoryValues>> fieldChangeMap,
			Map<Integer, List<TBudgetBean>> budgetMap,
			Map<Integer, List<TBudgetBean>> plannedValueMap,
			Map<Integer, List<TCostBean>> costMap,
			Map<Integer, List<HistoryValues>> commentMap,
			Integer queryFieldCSS, Set<Integer> exclusiveShortFields,
			Map<Integer, Double> workItemIdToPercent,
			Map<Object, Double> groupValueToPercent,
			Map<Object, Date> groupValueToStartDate,
			Map<Object, Date> groupValueToEndDate,
			Map<Object, Date> groupValueToTopDownStartDate,
			Map<Object, Date> groupValueToTopDownEndDate,
			boolean useIssueTypeIcon) {
		StringBuilder sb = new StringBuilder();
		//Set<Integer> editableProjectsSet = null;
		sb.append("[");
		if (nodes != null) {
			for (Iterator<ItemTreeNode> iterator = nodes.iterator(); iterator.hasNext();) {
				ItemTreeNode treeNode = iterator.next();
				sb.append("{");
				GroupLimitBean groupLimitBean = treeNode.getGroupLimitBean();
				if (groupLimitBean != null) {
					boolean editable = false;
					boolean linkable = false;
					Integer firstColumn = 12;
					Object sortOrder = null;
					boolean isTreeField = groupLimitBean.isTreeField();
					if (isTreeField) {
						/*if (SystemFields.INTEGER_PROJECT.equals(groupLimitBean.getGroupField())) {
							if (editableProjectsSet==null) {
								editableProjectsSet = new HashSet<Integer>();
								List<TProjectBean> editableProjects = ProjectBL.loadProjectsWithModifyIssueRight(personBean.getObjectID());
								if (editableProjects!=null) {
									for (TProjectBean projectBean : editableProjects) {
										editableProjectsSet.add(projectBean.getObjectID());
									}
								}
							}
							Integer projectID = (Integer)groupLimitBean.getGroupValue();
							if (projectID!=null) {
								editable = editableProjectsSet.contains(projectID);
							}
						}*/
						// sort order below the tree field: for a cleaner
						// rendering the items (leafs)
						// or possible subgroups should precede the the tree
						// branches
						Integer depthInTree = groupLimitBean.getDepthInTree();
						if (depthInTree != null
								&& depthInTree.intValue() > GroupLimitBean.FIRST_LEVEL_DEPTH) {
							Integer treeSortOrder = (Integer) groupLimitBean.getTreeSortOrder();
							// only if branch of the tree field because the
							// first branch level is sorted according to the
							// "main" grouping level
							// it is enforced to appear the items (leafs) first
							// and then the branches (folders) for cleaner
							// visual rendering
							// so force a greater sort order for folders as for
							// the items
							if (useProjectSpecificID) {
								if (treeSortOrder != null) {
									// zzz to be as "late" as possible: later as
									// itemIDs, which are based on the project
									// prefix
									sortOrder = "zzz"
											+ (10000000 + treeSortOrder);
								} else {
									sortOrder = "zzz" + 10000000;
								}
							} else {
								if (treeSortOrder != null) {
									sortOrder = (Integer) treeSortOrder + 10000000;
								} else {
									sortOrder = 10000000;
								}
							}
						}
					}
					List<ColumnFieldTO> shortFields = layoutTO.getShortFields();
					if (!shortFields.isEmpty()) {
						firstColumn = shortFields.get(0).getFieldID();
					}
					sb.append(encodeOpenGroup(treeNode, firstColumn, sortOrder,
							useProjectSpecificID, groupValueToPercent,
							groupValueToStartDate, groupValueToEndDate,
							groupValueToTopDownStartDate,
							groupValueToTopDownEndDate, locale, editable, linkable));
				} else {
					ReportBean reportBean = treeNode.getReportBean();
					sb.append(encodeOpenReportBean(reportBean, layoutTO,
							includeLongFields, locale, useProjectSpecificID,
							personBean, projectWorkingHoursMap, fieldChangeMap,
							budgetMap, plannedValueMap, costMap, commentMap,
							queryFieldCSS, exclusiveShortFields,
							workItemIdToPercent, useIssueTypeIcon));
				}
				List<ItemTreeNode> children = (List) treeNode.getChildren();
				if (children != null && !children.isEmpty()) {
					JSONUtility.appendJSONValue(
							sb,
							JSONUtility.JSON_FIELDS.CHILDREN,
							encodeNodes(children, layoutTO, includeLongFields,
									locale, useProjectSpecificID, personBean,
									projectWorkingHoursMap, fieldChangeMap,
									budgetMap, plannedValueMap, costMap,
									commentMap, queryFieldCSS,
									exclusiveShortFields, workItemIdToPercent,
									groupValueToPercent, groupValueToStartDate,
									groupValueToEndDate,
									groupValueToTopDownStartDate,
									groupValueToTopDownEndDate,
									useIssueTypeIcon));
				}
				JSONUtility.appendBooleanValue(sb,
						JSONUtility.JSON_FIELDS.LEAF, treeNode.getLeaf(), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");// end data
		return sb.toString();
	}

	private static String encodeOpenGroup(ItemTreeNode treeNode,
			Integer firstColumn, Object sortOrder,
			boolean useProjectSpecificID,
			Map<Object, Double> groupValueToPercent,
			Map<Object, Date> groupValueToStartDate,
			Map<Object, Date> groupValueToEndDate,
			Map<Object, Date> groupValueToTopDownStartDate,
			Map<Object, Date> groupValueToTopDownEndDate, Locale locale,
			boolean editable, boolean linkable) {
		GroupLimitBean groupLimitBean = treeNode.getGroupLimitBean();
		StringBuilder sb = new StringBuilder();

		JSONUtility.appendStringValue(sb, "id",
				"g" + groupLimitBean.getOrdinalNumberString());
		// FIXME add only for Gantt view
		JSONUtility.appendStringValue(sb, "Id",
				"g" + groupLimitBean.getOrdinalNumberString());
		JSONUtility.appendStringValue(sb, "text", "group");
		Integer projectID = 0;
		if (groupLimitBean.getReportBeans() != null
				&& !groupLimitBean.getReportBeans().isEmpty()) {
			projectID = groupLimitBean.getReportBeans().get(0)
					.getWorkItemBean().getProjectID();
		}
		JSONUtility.appendIntegerValue(sb, "projectID", projectID);
		JSONUtility.appendBooleanValue(sb, "group", true);
		JSONUtility.appendBooleanValue(sb, "editable", editable);
		JSONUtility.appendBooleanValue(sb, "linkable", linkable);
		String cssColorClassGroup = getCssColorClassGroup(treeNode);
		JSONUtility.appendStringValue(sb, "cssColorClassGroup",
				cssColorClassGroup);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.EXPANDED,
				groupLimitBean.isExpanding());
		JSONUtility.appendStringValue(
				sb,
				"f" + firstColumn,
				groupLimitBean.getGroupLabel() + " ("
						+ groupLimitBean.getNumberOfWorkItems() + ")");
		JSONUtility.appendStringValue(
				sb,
				"Name",
				groupLimitBean.getGroupLabel() + " ("
						+ groupLimitBean.getNumberOfWorkItems() + ")"); // For
																		// Gantt
																		// chart
		if (useProjectSpecificID) {
			if (sortOrder == null) {
				// first in the list
				sortOrder = "-1";
			}
			JSONUtility.appendStringValue(sb, "f_so" + firstColumn,
					(String) sortOrder);
			JSONUtility.appendStringValue(sb, "wbs", (String) sortOrder);
		} else {
			if (sortOrder == null) {
				// first in the list
				sortOrder = Integer.valueOf(-1);
			}
			JSONUtility.appendIntegerValue(sb, "f_so" + firstColumn,
					(Integer) sortOrder);
			JSONUtility.appendStringValue(sb, "wbs",
					((Integer) sortOrder).toString());
		}
		JSONUtility.appendBooleanValue(sb, "expanded",
				groupLimitBean.isExpanding());
		Object groupValue = groupLimitBean.getGroupValue();

		if (groupValueToPercent != null) {
			Double percent = groupValueToPercent.get(groupValue);
			if (percent != null) {
				DoubleWithDecimalDigitsNumberFormatUtil formatter = DoubleWithDecimalDigitsNumberFormatUtil
						.getInstance(2);
				String frmatedPercent = formatter.formatGUI(percent, locale);
				JSONUtility.appendStringValue(sb, "PercentDone",
						percent.toString());
				JSONUtility.appendStringValue(sb, "PercentDoneToolTip",
						frmatedPercent);

			} else {
				JSONUtility.appendStringValue(sb, "PercentDoneToolTip", "0");
			}
		} else {
			JSONUtility.appendStringValue(sb, "PercentDoneToolTip", "0");
		}

		if (!ApplicationBean.getInstance().getSiteBean().getShowBaseline()) {
			Date startDate = null;
			if (groupValueToStartDate != null) {
				startDate = groupValueToStartDate.get(groupValue);
			}
			Date endDate = null;
			if (groupValueToEndDate != null) {
				endDate = groupValueToEndDate.get(groupValue);
			}
			if (startDate != null && endDate != null) {
				JSONUtility.appendStringValue(sb, "f19", DateTimeUtils
						.getInstance().formatGUIDate(startDate, locale));
				JSONUtility.appendStringValue(sb, "f20", DateTimeUtils
						.getInstance().formatGUIDate(endDate, locale));
			}
			Date topDownStartDate = null;
			if (groupValueToTopDownStartDate != null) {
				topDownStartDate = groupValueToTopDownStartDate.get(groupValue);

			}
			Date topDownEndDate = null;
			if (groupValueToTopDownEndDate != null) {
				topDownEndDate = groupValueToTopDownEndDate.get(groupValue);
			}
			if (topDownStartDate != null && topDownEndDate != null) {
				JSONUtility.appendStringValue(sb, "f29", DateTimeUtils
						.getInstance().formatGUIDate(topDownStartDate, locale));
				JSONUtility.appendStringValue(sb, "f30", DateTimeUtils
						.getInstance().formatGUIDate(topDownEndDate, locale));
			}
		} else {
			/*
			 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			 * JSONUtility.appendStringValue(sb, "StartDate",
			 * dateFormat.format(topDownStartDate)); Date realTopDownEndDate =
			 * addOneDay(topDownEndDate); JSONUtility.appendStringValue(sb,
			 * "EndDate", dateFormat.format(realTopDownEndDate).toString());
			 */

			Date topDownStartDate = null;
			if (groupValueToTopDownStartDate != null) {
				topDownStartDate = groupValueToTopDownStartDate.get(groupValue);
			}
			Date topDownEndDate = null;
			if (groupValueToTopDownEndDate != null) {
				topDownEndDate = groupValueToTopDownEndDate.get(groupValue);
			}
			if (topDownStartDate != null && topDownEndDate != null) {
				JSONUtility.appendStringValue(sb, "f29", DateTimeUtils
						.getInstance().formatGUIDate(topDownStartDate, locale));
				JSONUtility.appendStringValue(sb, "f30", DateTimeUtils
						.getInstance().formatGUIDate(topDownEndDate, locale));
			}
		}
		// JSONUtility.appendBooleanValue(sb, "leaf",false);
		return sb.toString();
	}


	/**
	 * Add one day to end dates because the Gantt chart takes the end date as
	 * exclusive while Genji as inclusive
	 *
	 * @param date
	 * @return
	 */
	private static Date addOneDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}

	private static String encodeOpenReportBean(ReportBean reportBean,
			LayoutTO layoutTO, boolean includeLongFields, Locale locale,
			boolean useProjectSpecificID, TPersonBean personBean,
			Map<Integer, Double> projectWorkingHoursMap,
			Map<Integer, List<HistoryValues>> fieldChangeMap,
			Map<Integer, List<TBudgetBean>> budgetMap,
			Map<Integer, List<TBudgetBean>> plannedValueMap,
			Map<Integer, List<TCostBean>> costMap,
			Map<Integer, List<HistoryValues>> commentMap,
			Integer queryFieldCSS, Set<Integer> exclusiveShortFields,
			Map<Integer, Double> workItemIdToPercent, boolean useIssueTypeIcon) {
		StringBuilder sb = new StringBuilder();
		boolean accessLevelFlag = false;
		accessLevelFlag = reportBean.getWorkItemBean().isAccessLevelFlag();
		List<ColumnFieldTO> shortFields = layoutTO.getShortFields();
		Object value;
		if (queryFieldCSS != null) {
			if (queryFieldCSS < 0) {
				value = reportBean.getWorkItemBean().getAttribute(
						queryFieldCSS * -1);
			} else {
				// custom field
				Object[] valueArr = (Object[]) reportBean.getWorkItemBean()
						.getAttribute(queryFieldCSS);
				if (valueArr != null && valueArr.length > 0) {
					value = (Integer) valueArr[0];
				} else {
					value = null;
				}

			}
			if (value != null) {
				JSONUtility.appendStringValue(sb, "queryFieldCSS",
						value.toString());
			}
		}
		// needed only in wbs view. The hardcoded sorting is made on the client
		// side by this field
		// it should be added forcedly because the client side sorting should
		// work even if the wbs column is not selected as visible
		// if wbs field is selected in item navigator then the same value will
		// be added also as f27
		// JSONUtility.appendIntegerValue(sb,"wbs",reportBean.getWorkItemBean().getWBSOnLevel());
		JSONUtility.appendStringValue(sb, "wbs",
				(String) reportBean.getSortOrder(SystemFields.INTEGER_WBS));
		if (exclusiveShortFields != null) {
			Iterator<Integer> it = exclusiveShortFields.iterator();
			while (it.hasNext()) {
				Integer fieldID = it.next();
				appendField(reportBean, useProjectSpecificID, sb, fieldID);
			}
		} else {
			boolean hasStartDate = false;
			boolean hasEndDate = false;
			boolean hasTopDownStartDate = false;
			boolean hasTopDownEndDate = false;
			for (int i = 0; i < shortFields.size(); i++) {
				ColumnFieldTO columnsField = shortFields.get(i);
				Integer fieldID = columnsField.getFieldID();
				switch (fieldID.intValue()) {
				case SystemFields.STARTDATE:
					hasStartDate = true;
					break;
				case SystemFields.ENDDATE:
					hasEndDate = true;
					break;
				case SystemFields.TOP_DOWN_START_DATE:
					hasTopDownStartDate = true;
					break;
				case SystemFields.TOP_DOWN_END_DATE:
					hasTopDownEndDate = true;
					break;
				default:
					break;
				}
				appendField(reportBean, useProjectSpecificID, sb, fieldID);
			}
			if (!hasStartDate) {
				appendField(reportBean, useProjectSpecificID, sb, SystemFields.INTEGER_STARTDATE);
			}
			if (!hasEndDate) {
				appendField(reportBean, useProjectSpecificID, sb, SystemFields.INTEGER_ENDDATE);
			}
			if (!hasTopDownStartDate) {
				appendField(reportBean, useProjectSpecificID, sb, SystemFields.INTEGER_TOP_DOWN_START_DATE);
			}
			if (!hasTopDownEndDate) {
				appendField(reportBean, useProjectSpecificID, sb, SystemFields.INTEGER_TOP_DOWN_END_DATE);
			}

		}
		if (includeLongFields) {
			List<ColumnFieldTO> longFields = layoutTO.getLongFields();
			Integer workItemID = reportBean.getWorkItemBean().getObjectID();
			if (longFields != null) {
				Integer reportField = null;
				for (int i = 0; i < longFields.size(); i++) {
					ColumnFieldTO columnField = longFields.get(i);
					reportField = columnField.getFieldID();
					switch (reportField.intValue()) {
					case TReportLayoutBean.PSEUDO_COLUMNS.HISTORY_LIST: {
						List<HistoryValues> historyList = (List<HistoryValues>) fieldChangeMap
								.get(workItemID);
						if (historyList != null) {
							JSONUtility.appendJSONValue(sb, "f" + reportField,
									ItemDetailBL.encodeJSON_HistoryList(
											historyList, true, locale, true));
						}
						break;
					}
					case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_HISTORY_LIST: {
						List<TBudgetBean> budgetHistory = budgetMap
								.get(workItemID);
						if (budgetHistory != null) {
							JSONUtility
									.appendJSONValue(
											sb,
											"f" + reportField,
											BudgetPlanExpenseJSON
													.encodeBudgetPlanHistoryForReportBean(
															(List) budgetHistory,
															locale));
						}
						break;
					}
					case TReportLayoutBean.PSEUDO_COLUMNS.PLAN_HISTORY_LIST: {
						List<TBudgetBean> planHistory = plannedValueMap
								.get(workItemID);
						if (planHistory != null) {
							JSONUtility
									.appendJSONValue(
											sb,
											"f" + reportField,
											BudgetPlanExpenseJSON
													.encodeBudgetPlanHistoryForReportBean(
															(List) planHistory,
															locale));
						}
						break;
					}
					case TReportLayoutBean.PSEUDO_COLUMNS.COST_LIST: {
						List<TCostBean> costs = costMap.get(workItemID);
						if (costs != null) {
							JSONUtility.appendJSONValue(sb, "f" + reportField,
									BudgetPlanExpenseJSON
											.encodeExpensesForReportBean(costs,
													locale));
						}
						break;
					}
					case SystemFields.COMMENT: {
						List<HistoryValues> comments = (List<HistoryValues>) commentMap
								.get(workItemID);
						if (comments != null) {
							JSONUtility.appendJSONValue(sb, "f" + reportField,
									ItemDetailBL.encodeJSON_Comments(comments,
											locale));
						}
						break;
					}
					default: {
						String showValue = reportBean.getShowValue(columnField
								.getFieldID());
						if (showValue != null) {
							JSONUtility.appendStringValue(sb,
									"f" + reportField, ItemDetailBL
											.formatDescription(showValue,
													locale));
						}
					}
					}
				}
			}
		}
		TWorkItemBean workItemBean = reportBean.getWorkItemBean();
		Integer itemID = workItemBean.getObjectID();
		Integer projectID = workItemBean.getProjectID();
		JSONUtility.appendIntegerValue(sb, "id", itemID);
		JSONUtility.appendStringValue(sb, "cssColorClass",
				getCssColorClass(reportBean));

		if (useIssueTypeIcon) {
			JSONUtility.appendStringValue(sb, "icon",
					getIconIssueType(reportBean));
		} else {
			JSONUtility
					.appendStringValue(sb, "iconCls", getIconCls(reportBean));
		}
		// JSONUtility.appendStringList(sb,"outOfBoundIcons",getOutOfBoundIconCls(reportBean));
		JSONUtility.appendBooleanValue(sb, "accessLevelFlag", accessLevelFlag);
		JSONUtility.appendIntegerValue(sb, "workItemID", itemID);
		JSONUtility.appendIntegerValue(sb, "projectID",
				workItemBean.getProjectID());
		JSONUtility.appendIntegerValue(sb, "releaseScheduledID",
				(workItemBean.getReleaseScheduledID() == null) ? -1
						: workItemBean.getReleaseScheduledID());
		JSONUtility.appendIntegerValue(sb, "issueTypeID",
				workItemBean.getListTypeID());
		JSONUtility.appendIntegerValue(sb, "parentID",
				workItemBean.getSuperiorworkitem());
		JSONUtility.appendIntegerValue(sb, "originatorID",
				workItemBean.getOriginatorID());
		JSONUtility.appendBooleanValue(sb, "expanded", reportBean.isExpanded());
		JSONUtility.appendBooleanValue(sb, "extendedItem",
				reportBean.isExtendedItem());
		JSONUtility.appendIntegerValue(sb, "workItemIndex", index++);
		JSONUtility.appendStringValue(sb, "status",
				reportBean.getShowValue(SystemFields.INTEGER_STATE).toString());
		JSONUtility.appendStringValue(sb, "responsibleValue", reportBean
				.getShowValue(SystemFields.RESPONSIBLE).toString());

		JSONUtility.appendStringValue(sb, "localizedStartDate", reportBean
				.getShowValue(SystemFields.STARTDATE).toString());
		JSONUtility.appendStringValue(sb, "localizedEndDate", reportBean
				.getShowValue(SystemFields.ENDDATE).toString());
		// FIXME addd only for Gantt view
		JSONUtility.appendStringValue(sb, "Id", itemID.toString());
		if (workItemIdToPercent != null) {
			if (workItemIdToPercent.get(itemID) != null) {
				DoubleWithDecimalDigitsNumberFormatUtil formatter = DoubleWithDecimalDigitsNumberFormatUtil
						.getInstance(2);
				String frmatedPercent = formatter.formatGUI(
						workItemIdToPercent.get(itemID), locale);
				JSONUtility.appendStringValue(sb, "PercentDone",
						workItemIdToPercent.get(itemID).toString());
				JSONUtility.appendStringValue(sb, "PercentDoneToolTip",
						frmatedPercent);

			} else {
				JSONUtility.appendStringValue(sb, "PercentDoneToolTip", "0");
			}
		} else {
			JSONUtility.appendStringValue(sb, "PercentDoneToolTip", "0");
		}

		List<ReportBean> children = reportBean.getChildren();

		Date startDate = MsProjectLinkTypeBL.getStartDate(workItemBean);
		Date endDate = MsProjectLinkTypeBL.getEndDate(workItemBean);

		if (workItemBean.isMilestone() && children == null && startDate != null) {
			JSONUtility.appendStringValue(sb, "Duration", "0");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (startDate != null) {
				String formattedDate = dateFormat.format(startDate);
				JSONUtility.appendStringValue(sb, "StartDate", formattedDate);
				JSONUtility.appendStringValue(sb, "EndDate", formattedDate);
			}
			if (workItemBean.getTopDownStartDate() != null) {
				String formattedDate = dateFormat.format(workItemBean
						.getTopDownStartDate());
				JSONUtility.appendStringValue(sb, "BaselineStartDate",
						formattedDate);
				JSONUtility.appendStringValue(sb, "BaselineEndDate",
						formattedDate);
			}

		} else if (startDate == null || endDate == null) {
			JSONUtility.appendStringValue(sb, "calendarStartDateMissing",
					"true");
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			JSONUtility.appendStringValue(sb, "StartDate",
					dateFormat.format(startDate));
			Date realEndDate = addOneDay(endDate);
			JSONUtility.appendStringValue(sb, "EndDate",
					dateFormat.format(realEndDate).toString());
			if (workItemBean.getTopDownStartDate() != null
					&& workItemBean.getTopDownEndDate() != null) {
				JSONUtility.appendStringValue(sb, "BaselineStartDate",
						dateFormat.format(workItemBean.getTopDownStartDate()));
				Date realTopDownEndDate = addOneDay(workItemBean
						.getTopDownEndDate());
				JSONUtility.appendStringValue(sb, "BaselineEndDate",
						dateFormat.format(realTopDownEndDate));
			}
		}

		JSONUtility.appendStringValue(sb, "Name", workItemBean.getSynopsis());
		SortedSet<ReportBeanLink> ganntMSProjectReportBeanLinksSet = reportBean
				.getGanntMSProjectReportBeanLinksSet();
		// append links
		StringBuilder linksTo = new StringBuilder();

		if (ganntMSProjectReportBeanLinksSet != null) {
			for (Iterator<ReportBeanLink> iterator = ganntMSProjectReportBeanLinksSet.iterator(); iterator.hasNext();) {
				ReportBeanLink reportBeanLink = iterator.next();
				linksTo.append(reportBeanLink.getWorkItemID() + "/" + reportBeanLink.getObjectID());
				ItemLinkSpecificData itemLinkSpecificData = reportBeanLink.getLinkSpecificData();
				if (itemLinkSpecificData != null) {
					MsProjectLinkSpecificData msProjectLinkSpecificData = (MsProjectLinkSpecificData) itemLinkSpecificData;
					linksTo.append("/"+ msProjectLinkSpecificData.getDependencyType());
					if (msProjectLinkSpecificData.getLinkLag() != null && msProjectLinkSpecificData.getLinkLagFormat() != null) {
						Double hoursPerWorkday = projectWorkingHoursMap.get(projectID);// MsProjectLinkTypeBL.getHoursPerWorkingDayForWorkItem(workItemBean);
						if (hoursPerWorkday == null) {
							hoursPerWorkday = ProjectBL.getHoursPerWorkingDay(projectID);
							projectWorkingHoursMap.put(projectID, hoursPerWorkday);
						}
						Double convertedLinkLag = LinkLagBL.getUILinkLagFromMinutes(msProjectLinkSpecificData.getLinkLag(),msProjectLinkSpecificData.getLinkLagFormat(),hoursPerWorkday);
						linksTo.append("/" + convertedLinkLag);
						linksTo.append("/" + msProjectLinkSpecificData.getLinkLagFormat());
					}
				}
				if (iterator.hasNext()) {
					linksTo.append(",");
				}
			}
		}
		JSONUtility.appendStringValue(sb, "linksTo", linksTo.toString());
		// append rights
		JSONUtility.appendBooleanValue(sb, "editable", reportBean.isEditable());
		JSONUtility.appendBooleanValue(sb, "linkable", reportBean.isLinking());
		Set<Integer> notEditableFields = reportBean.getNotEditableFields();
		if (notEditableFields != null) {
			JSONUtility.appendIntegerSetAsArray(sb, "notEditableFields",
					notEditableFields);

			if (notEditableFields.contains(SystemFields.STARTDATE)) {
				JSONUtility.appendBooleanValue(sb, "startDateEditFlag", false);
			} else {
				JSONUtility.appendBooleanValue(sb, "startDateEditFlag", true);
			}

			if (notEditableFields.contains(SystemFields.ENDDATE)) {
				JSONUtility.appendBooleanValue(sb, "endDateEditFlag", false);
			} else {
				JSONUtility.appendBooleanValue(sb, "endDateEditFlag", true);
			}

		}

		return sb.toString();
	}

	private static void appendField(ReportBean reportBean,
			boolean useProjectSpecificID, StringBuilder sb, Integer fieldID) {
		String showValue;
		Object value;
		FieldType fieldType;
		int valueType;
		TypeRendererRT fieldTypeRendererRT;
		switch (fieldID) {
		case SystemFields.ISSUENO: {
			if (useProjectSpecificID) {
				String prjSpecificID = reportBean
						.getShowValue(SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO);
				JSONUtility.appendStringValue(sb, "f" + fieldID, prjSpecificID);
				Integer itemNrInt = reportBean.getWorkItemBean().getIDNumber();
				prjSpecificID = prjSpecificID.substring(0,
						prjSpecificID.lastIndexOf(itemNrInt + ""));
				prjSpecificID = prjSpecificID + (1000000 + itemNrInt);
				JSONUtility.appendStringValue(sb, "f_so" + fieldID,
						prjSpecificID);
			} else {
				JSONUtility.appendStringValue(sb, "f" + fieldID, reportBean
						.getWorkItemBean().getObjectID().toString());
				JSONUtility.appendIntegerValue(sb, "f_so" + fieldID,
						(Integer) reportBean.getWorkItemBean().getObjectID());
			}
			break;
		}
		case SystemFields.WBS:
			JSONUtility.appendStringValue(sb, "f" + fieldID,
					reportBean.getShowValue(fieldID));
			// not string comparison (1, 10,..., 2 but 1, 2, ..., 10)
			JSONUtility.appendStringValue(sb, "f_so" + fieldID,
					(String) reportBean.getSortOrder(fieldID));
			break;
		case TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL: {
			JSONUtility.appendStringValue(sb, "attachmentIds",
					reportBean.getAttachmentIds());
			showValue = reportBean.getShowValue(fieldID);
			if (showValue != null) {
				JSONUtility.appendStringValue(sb, "f" + fieldID,
						showValue.toString());
			}
			break;
		}
		// TODO refactor to use render on client
		case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST:
		case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME:
		case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST:
		case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME:
		case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST:
		case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME:
		case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST:
		case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME:
		case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST:
		case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME: {
			Double doubleSo = (Double) reportBean.getSortOrder(fieldID);
			int intSo = 0;
			if (doubleSo != null) {
				intSo = (int) (doubleSo.doubleValue() * 100d);
				JSONUtility.appendIntegerValue(sb, "f_so" + fieldID, intSo);
				// value=reportBean.getWorkItemBean().getAttribute(fieldID);
				value = reportBean.getShowValue(fieldID);
				if (value != null) {
					JSONUtility.appendStringValue(sb, "f" + fieldID,
							value.toString());
				}
			}
			break;
		}
		case TReportLayoutBean.PSEUDO_COLUMNS.PRIORITY_SYMBOL:
		case TReportLayoutBean.PSEUDO_COLUMNS.SEVERITY_SYMBOL:
		case TReportLayoutBean.PSEUDO_COLUMNS.STATUS_SYMBOL:
		case TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL: {
			JSONUtility.appendIntegerValue(sb, "f_so" + fieldID,
					(Integer) reportBean.getSortOrder(fieldID * -1));
			value = reportBean.getWorkItemBean().getAttribute(fieldID * -1);
			if (value != null) {
				JSONUtility.appendStringValue(sb, "f" + fieldID,
						value.toString());
			}
			break;
		}

		case TReportLayoutBean.PSEUDO_COLUMNS.ORIGINATOR_SYMBOL:
		case TReportLayoutBean.PSEUDO_COLUMNS.MANAGER_SYMBOL:
		case TReportLayoutBean.PSEUDO_COLUMNS.RESPONSIBLE_SYMBOL: {
			value = reportBean.getWorkItemBean().getAttribute(fieldID * -1);
			if (value != null) {
				JSONUtility.appendStringValue(sb, "f" + fieldID,
						value.toString());
			}
			break;
		}

		case TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL:
			value = reportBean.getShowValuesMap().get(
					TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL);
			JSONUtility.appendStringValue(sb, "f" + fieldID, (String) value);
			break;
		case TReportLayoutBean.PSEUDO_COLUMNS.OVERFLOW_ICONS:
			value = getOutOfBoundIconCls(reportBean);
			JSONUtility.appendStringValue(sb, "f" + fieldID, (String) value);
			break;
		case TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST:
		case TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST:
		case TReportLayoutBean.PSEUDO_COLUMNS.LINKED_ITEMS:
			value = reportBean.getShowValue(fieldID);
			JSONUtility.appendStringValue(sb, "f" + fieldID, (String) value);
			break;
		/*
		 * case TReportLayoutBean.PSEUDO_COLUMNS.LINKED_ITEMS: StringBuilder
		 * stringBuilder = new StringBuilder(); SortedSet<ReportBeanLink>
		 * reportBeanLinks = reportBean.getReportBeanLinksSet(); if
		 * (reportBeanLinks!=null) { for (Iterator<ReportBeanLink> iterator =
		 * reportBeanLinks.iterator(); iterator.hasNext();) { ReportBeanLink
		 * reportBeanLink = iterator.next(); String showLink = null; if
		 * (useProjectSpecificID) { showLink =
		 * reportBeanLink.getProjectSpecificIssueNo(); } else { showLink =
		 * reportBeanLink.getWorkItemID().toString(); }
		 * stringBuilder.append(showLink); if (iterator.hasNext()) {
		 * stringBuilder.append(" | "); } } }
		 * JSONUtility.appendStringValue(sb,"f"+fieldID,
		 * stringBuilder.toString()); break;
		 */
		default: {
			if (fieldID.intValue() < ColumnFieldsBL.CUSTOM_LIST_ICON_FIELD_START) {
				Integer customListFieldID = ColumnFieldsBL
						.getCustomListFieldFromIconField(fieldID);
				IFieldTypeRT fieldTypeRT = FieldTypeManager
						.getFieldTypeRT(/* Integer.valueOf(-fieldID) */customListFieldID);
				if (fieldTypeRT != null
						&& (fieldTypeRT.getValueType() == ValueType.CUSTOMOPTION || fieldTypeRT
								.isComposite())) {
					Object[] selectedValues = null;
					if (fieldTypeRT.getValueType() == ValueType.CUSTOMOPTION) {
						JSONUtility
								.appendIntegerValue(
										sb,
										"f_so" + fieldID,
										(Integer) reportBean
												.getSortOrder(/* fieldID*-1 */customListFieldID));
					}
					value = reportBean.getWorkItemBean().getAttribute(
							/* fieldID*-1 */customListFieldID);
					if (value != null) {
						if (fieldTypeRT.getValueType() == ValueType.CUSTOMOPTION) {
							try {
								selectedValues = (Object[]) value;
							} catch (Exception e) {
							}
						} else {
							Map<Integer, Object[]> selectedComposite = (Map<Integer, Object[]>) value;
							if (selectedComposite != null) {
								selectedValues = selectedComposite
										.get(selectedComposite.size());
							}
						}
						if (selectedValues != null) {
							JSONUtility.appendObjectArrayAsArray(sb, "f"
									+ fieldID, selectedValues);
						}
					}
				}
			} else {
				fieldType = FieldTypeManager.getInstance().getType(fieldID);
				if (fieldType != null) {
					valueType = fieldType.getFieldTypeRT().getValueType();
					fieldType.setFieldID(fieldID);
					fieldTypeRendererRT = fieldType.getRendererRT();
					switch (valueType) {
					case ValueType.SYSTEMOPTION: {
						JSONUtility.appendStringValue(sb, "f" + fieldID,
								reportBean.getShowValue(fieldID));
						switch (fieldID) {
						case SystemFields.ISSUETYPE:
						case SystemFields.STATE:
						case SystemFields.RELEASENOTICED:
						case SystemFields.RELEASESCHEDULED:
						case SystemFields.PRIORITY:
						case SystemFields.SEVERITY: {
							JSONUtility.appendIntegerValue(sb,
									"f_so" + fieldID,
									(Integer) reportBean.getSortOrder(fieldID));
						}
						}
						break;
					}
					case ValueType.CUSTOMOPTION: {
						JSONUtility.appendStringValue(sb, "f" + fieldID,
								reportBean.getShowValue(fieldID));
						JSONUtility.appendIntegerValue(sb, "f_so" + fieldID,
								(Integer) reportBean.getSortOrder(fieldID));
						break;
					}
					case ValueType.DATE:
					case ValueType.DATETIME: {
						JSONUtility.appendStringValue(sb, "f" + fieldID,
								reportBean.getShowValue(fieldID));
						Date d = (Date) reportBean.getSortOrder(fieldID);
						if (d != null) {
							JSONUtility.appendLongValue(sb, "f_so" + fieldID,
									d.getTime());
						}
						break;
					}
					case ValueType.INTEGER:
						String jsonValueInt = fieldTypeRendererRT
								.encodeJsonValue(reportBean.getWorkItemBean()
										.getAttribute(fieldID));
						JSONUtility.appendJSONValue(sb, "f" + fieldID,
								jsonValueInt);
						break;
					case ValueType.DOUBLE: {
						// String
						// jsonValue=fieldTypeRendererRT.encodeJsonValue(reportBean.getWorkItemBean().getAttribute(fieldID));
						// JSONUtility.appendJSONValue(sb,"f"+fieldID,jsonValue);
						String doubleShowValue = reportBean
								.getShowValue(fieldID);
						JSONUtility.appendStringValue(sb, "f" + fieldID,
								doubleShowValue);
						break;
					}
					case ValueType.BOOLEAN: {
						JSONUtility.appendStringValue(sb, "f" + fieldID,
								reportBean.getShowValue(fieldID));
						break;
					}
					case ValueType.EXTERNALID:
						JSONUtility.appendStringValue(sb, "f" + fieldID,
								reportBean.getShowValue(fieldID));
						break;
					default: {

						if (fieldType.getFieldTypeRT().isComposite()) {
							JSONUtility.appendStringValue(sb, "f" + fieldID,
									reportBean.getShowValue(fieldID));
						} else {
							String jsonValue = fieldTypeRendererRT
									.encodeJsonValue(reportBean
											.getShowValue(fieldID));
							JSONUtility.appendJSONValue(sb, "f" + fieldID,
									jsonValue);
						}
					}
					}
				} else {
					showValue = reportBean.getShowValue(fieldID);
					if (showValue != null) {
						JSONUtility.appendStringValue(sb, "f" + fieldID,
								showValue.toString());
					}
				}
			}
		}
		}
	}

	public static String getIconIssueType(ReportBean reportBean) {
		return "optionIconStream.action?fieldID=-2&optionID="
				+ reportBean.getWorkItemBean().getListTypeID();
	}

	public static String getIconCls(ReportBean reportBean) {
		String iconCls = "";
		// String cssColorClass;
		TWorkItemBean workItemBean = reportBean.getWorkItemBean();
		if (workItemBean.isArchived()) {
			iconCls = "item-archived";
		} else {
			if (workItemBean.isDeleted()) {
				iconCls = "item-delete16";
			} else {
				if (Integer.valueOf(TStateBean.STATEFLAGS.CLOSED).equals(
						reportBean.getStateFlag())) {
					if (!reportBean.isCommittedDateConflict()) {
						iconCls = "item-checkgreen";
					} else {
						iconCls = "item-checkred";
						// cssColorClass = "ClosedNotOnPlan";
					}
				} else {
					boolean dateConflict = reportBean.isDateConflict();
					boolean budgetPlanConflict = reportBean
							.isBudgetOrPlanConflict();
					if (!dateConflict && !budgetPlanConflict) {
						iconCls = "item-work-to-do";
						if (reportBean.getBottomUpDateDueFlag() == TWorkItemBean.DUE_FLAG.DUE_SOON) {
							// cssColorClass = "DueSoon";
						}
					} else {
						if (dateConflict && !budgetPlanConflict) {
							iconCls = "item-calendarOverflow";
						} else {
							if (!dateConflict && budgetPlanConflict) {
								iconCls = "item-budgetOverflow";
							} else {
								iconCls = "item-calendarBudgetOverflow";
							}
						}
						// cssColorClass = "NotOnPlan";
					}
				}
			}
		}
		return iconCls;
	}

	private static String getCssColorClassGroup(ItemTreeNode treeNode) {
		String result = null;
		int overdue = getOverdue(treeNode);
		switch (overdue) {
		case ITEM_OK: {
			result = null;
			break;
		}
		case ITEM_DUE_SOON: {
			result = "group-dueSoon";
			break;
		}
		case ITEM_OVERDUE: {
			result = "group-notOnPlan";
			break;
		}
		}
		return result;
	}

	/*
	 * return 0 no overdue, 1-due soon, 2-overdue
	 */
	private static int getOverdue(ItemTreeNode treeNode) {
		int overdue = 0;
		ReportBean reportBean;
		reportBean = treeNode.getReportBean();
		if (reportBean != null) {
			overdue = getOverdue(reportBean);
			if (overdue == ITEM_OVERDUE) {
				// no need to iterate the children is already overdue itself
				return ITEM_OVERDUE;
			}
		}
		List<ItemTreeNode> children = (List) treeNode.getChildren();
		for (int i = 0; i < children.size(); i++) {
			ItemTreeNode child = children.get(i);
			reportBean = child.getReportBean();
			int childOverdue = 0;
			if (reportBean != null) {
				childOverdue = getOverdue(reportBean);
			} else {
				childOverdue = getOverdue(child);
			}
			if (childOverdue == ITEM_OVERDUE) {
				return ITEM_OVERDUE;
			}
			if (childOverdue > 0) {
				overdue = childOverdue;
			}
		}
		return overdue;
	}

	/*
	 * return 0 no overdue, 1-due soon, 2-overdue
	 */
	private static int getOverdue(ReportBean reportBean) {
		int result = 0;
		TWorkItemBean workItemBean = reportBean.getWorkItemBean();
		if (!workItemBean.isArchived()
				&& !workItemBean.isDeleted()
				&& !Integer.valueOf(TStateBean.STATEFLAGS.CLOSED).equals(
						reportBean.getStateFlag())) {
			if (!reportBean.isDateConflict()
					&& !reportBean.isBudgetOrPlanConflict()) {
				if (reportBean.getBottomUpDateDueFlag() == TWorkItemBean.DUE_FLAG.DUE_SOON
						|| reportBean.getTopDownDateDueFlag() == TWorkItemBean.DUE_FLAG.DUE_SOON) {
					result = ITEM_DUE_SOON;
				}
			} else {
				result = ITEM_OVERDUE;
			}
		}
		return result;
	}

	private static String getOutOfBoundIconCls(ReportBean reportBean) {
		List<String> iconClsList = new LinkedList<String>();
		boolean committedDateConflict = reportBean.isCommittedDateConflict();
		boolean targetDateConflict = reportBean.isTargetDateConflict();
		boolean plannedValueConflict = reportBean.isPlannedValueConflict();
		boolean budgetConflict = reportBean.isBudgetConflict();
		if (committedDateConflict) {
			iconClsList.add("committedDateOverflow.gif");
		}
		if (targetDateConflict) {
			iconClsList.add("targetDateOverflow.gif");
		}
		if (plannedValueConflict) {
			iconClsList.add("plannedValueOverflow.gif");
		}
		if (budgetConflict) {
			iconClsList.add("budgetOverflow.gif");
		}
		StringBuilder stringBuilder = new StringBuilder();
		if (!iconClsList.isEmpty()) {
			for (Iterator<String> iterator = iconClsList.iterator(); iterator
					.hasNext();) {
				String iconCls = iterator.next();
				stringBuilder.append(iconCls);
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}
			return stringBuilder.toString();
		} else {
			return null;
		}

	}

	/**
	 * @deprecated
	 * @param reportBean
	 * @return
	 */
	@Deprecated
	private static String getCssColorClass(ReportBean reportBean) {
		String cssColorClass = "OnPlan";
		if (Integer.valueOf(TStateBean.STATEFLAGS.CLOSED).equals(
				reportBean.getStateFlag())) {
			if (reportBean.isDateConflict()) {
				cssColorClass = "ClosedNotOnPlan";
			}
		} else {
			if (!reportBean.isDateConflict()
					&& !reportBean.isBudgetOrPlanConflict()) {
				if (reportBean.isNotClosedConflictingAncestor()) {
					cssColorClass = "NotOnPlan";
				} else {
					if (reportBean.getBottomUpDateDueFlag() == TWorkItemBean.DUE_FLAG.DUE_SOON
							|| reportBean.getTopDownDateDueFlag() == TWorkItemBean.DUE_FLAG.DUE_SOON) {
						cssColorClass = "DueSoon";
					}
				}
			} else {
				cssColorClass = "NotOnPlan";
			}
		}
		return cssColorClass;
	}

	public static String encodeLayout(LayoutTO layoutTO,
			boolean useProjectSpecificID) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, "bulkEdit", layoutTO.isBulkEdit());
		JSONUtility.appendBooleanValue(sb, "indexNumber",
				layoutTO.isIndexNumber());
		JSONUtility.appendJSONValue(
				sb,
				"longFields",
				encodeColumnFields(layoutTO.getLongFields(),
						useProjectSpecificID));
		JSONUtility.appendJSONValue(
				sb,
				"shortFields",
				encodeColumnFields(layoutTO.getShortFields(),
						useProjectSpecificID));
		SortFieldTO sortFieldTO = layoutTO.getSortField();
		if (sortFieldTO != null) {
			JSONUtility.appendIntegerValue(sb, "sortField",
					sortFieldTO.getFieldID());
			JSONUtility.appendBooleanValue(sb, "sortOrder",
					sortFieldTO.isDescending());
			boolean sortWithSO = hasExtraSortOrder(sortFieldTO.getFieldID());
			JSONUtility.appendBooleanValue(sb, "sortWithSO", sortWithSO);

		}
		JSONUtility.appendJSONValue(sb, "groupColumns",
				encodeGroupFieldBeanList(layoutTO.getGroupFields()), true);
		sb.append("}");
		return sb.toString();
	}

	/*
	 * public static String encodeReportLayoutBeanList(List<TReportLayoutBean>
	 * list,boolean useProjectSpecificID ){ StringBuilder sb=new
	 * StringBuilder(); sb.append("["); if(list!=null){ TReportLayoutBean bean;
	 * for (int i = 0; i < list.size(); i++) { bean=list.get(i); if(i>0){
	 * sb.append(","); }
	 * sb.append(encodeReportLayoutBean(bean,useProjectSpecificID)); } }
	 * sb.append("]"); return sb.toString(); }
	 */

	public static String encodeColumnFields(List<ColumnFieldTO> columnFields,
			boolean useProjectSpecificID) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (columnFields != null) {
			boolean first = true;
			for (int i = 0; i < columnFields.size(); i++) {
				ColumnFieldTO columnFieldTO = columnFields.get(i);
				if (i > 0) {
					sb.append(",");
				}
				sb.append(encodeColumnField(columnFieldTO, i + 1,
						useProjectSpecificID));
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private static String getSortOrderType(Integer fieldID,
			boolean useProjectSpecificID) {
		if ((useProjectSpecificID && fieldID == SystemFields.ISSUENO)
				|| fieldID == SystemFields.WBS) {
			return "string";
		}
		return null;
	}

	public static boolean hasExtraSortOrder(Integer fieldID) {
		boolean result = false;
		switch (fieldID) {
		case SystemFields.ISSUENO:
		case SystemFields.WBS: // show value is a string "1.2.3" but sort order
								// is integer 3
		case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST:
		case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME:
		case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST:
		case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME:
		case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST:
		case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME:
		case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST:
		case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME:
			return true;
		}
		if (fieldID.intValue() > 0) {
			FieldType fieldType = FieldTypeManager.getInstance().getType(
					fieldID);
			if (fieldType != null) {
				int valueType = fieldType.getFieldTypeRT().getValueType();
				switch (valueType) {
				case ValueType.SYSTEMOPTION: {
					switch (fieldID) {
					case SystemFields.ISSUETYPE:
					case SystemFields.STATE:
					case SystemFields.RELEASENOTICED:
					case SystemFields.RELEASESCHEDULED:
					case SystemFields.PRIORITY:
					case SystemFields.SEVERITY: {
						result = true;
						break;
					}
					}
					break;
				}
				case ValueType.CUSTOMOPTION: {
					result = true;
					break;
				}
				case ValueType.DATE:
				case ValueType.DATETIME: {
					result = true;
				}
				}
			}
		}
		return result;
	}

	private static String getExtJsType(Integer fieldID) {
		String result = "string";
		if (SystemFields.INTEGER_ISSUENO.equals(fieldID)) {
			return result;
		}
		if (TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL == fieldID
				.intValue()) {
			return "int";
		}
		if (fieldID.intValue() > 0) {
			FieldType fieldType = FieldTypeManager.getInstance().getType(
					fieldID);
			if (fieldType != null) {
				int valueType = fieldType.getFieldTypeRT().getValueType();
				switch (valueType) {
				/*
				 * case ValueType.DOUBLE:{ result="float"; break; }
				 */
				case ValueType.INTEGER: {
					result = "int";
					break;
				}
				/*
				 * case ValueType.BOOLEAN:{ result="bool"; break; }
				 */
				}
			}
		}
		return result;
	}

	private static String getExtJsClassName(Integer fieldID) {
		FieldType fieldType = FieldTypeManager.getInstance().getType(fieldID);
		String extClassName = "";
		if (fieldType != null) {
			fieldType.setFieldID(fieldID);
			extClassName = fieldType.getRendererRT().getExtClassName();
			if (extClassName == null) {
				extClassName = "";
			}
		}
		return extClassName;
	}

	/*
	 * public static String encodeReportLayoutBean(TReportLayoutBean rlb,boolean
	 * useProjectSpecificID){ StringBuilder sb=new StringBuilder();
	 * sb.append("{");
	 *
	 * JSONUtility.appendIntegerValue(sb, "id", rlb.getObjectID());
	 * JSONUtility.appendIntegerValue(sb, "reportField", rlb.getReportField());
	 * JSONUtility.appendIntegerValue(sb, "fieldPosition",
	 * rlb.getFieldPosition()); JSONUtility.appendIntegerValue(sb, "fieldWidth",
	 * rlb.getFieldWidth());
	 *
	 * JSONUtility.appendStringValue(sb, "extJsType",
	 * getExtJsType(rlb.getReportField())); JSONUtility.appendBooleanValue(sb,
	 * "so", hasExtraSortOrder(rlb.getReportField()));
	 * JSONUtility.appendStringValue(sb, "soType",
	 * getSortOrderType(rlb.getReportField(),useProjectSpecificID));
	 *
	 *
	 * JSONUtility.appendIntegerValue(sb, "fieldType", rlb.getFieldType());
	 * JSONUtility.appendStringValue(sb, "expanding", rlb.getExpanding());
	 * JSONUtility.appendStringValue(sb, "label", rlb.getLabel());
	 * JSONUtility.appendBooleanValue(sb, "renderHeaderAsImg",
	 * rlb.isRenderHeaderAsImg()); JSONUtility.appendStringValue(sb,
	 * "headerImgName", rlb.getHeaderImgName());
	 * JSONUtility.appendBooleanValue(sb, "renderContentAsImg",
	 * rlb.isRenderContentAsImg()); JSONUtility.appendBooleanValue(sb,
	 * "fieldIsCustomIcon", rlb.isFieldIsCustomIcon());
	 * JSONUtility.appendBooleanValue(sb, "renderAsLong",
	 * rlb.isRenderAsLong(),true);
	 *
	 * sb.append("}"); return sb.toString(); }
	 */

	public static String encodeColumnField(ColumnFieldTO rlb, int i,
			boolean useProjectSpecificID) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, "id", rlb.getObjectID());
		JSONUtility.appendIntegerValue(sb, "reportField", rlb.getFieldID());
		JSONUtility.appendIntegerValue(sb, "fieldPosition", i /*
															 * rlb.getFieldPosition
															 * ()
															 */);
		JSONUtility.appendIntegerValue(sb, "fieldWidth", rlb.getFieldWidth());

		JSONUtility.appendStringValue(sb, "extJsType",
				getExtJsType(rlb.getFieldID()));
		JSONUtility.appendStringValue(sb, "extJsRendererClass",
				getExtJsClassName(rlb.getFieldID()));
		JSONUtility.appendBooleanValue(sb, "so",
				hasExtraSortOrder(rlb.getFieldID()));
		JSONUtility.appendStringValue(sb, "soType",
				getSortOrderType(rlb.getFieldID(), useProjectSpecificID));

		// JSONUtility.appendIntegerValue(sb, "fieldType", rlb.getFieldType());
		// JSONUtility.appendStringValue(sb, "expanding", rlb.getExpanding());
		JSONUtility.appendStringValue(sb, "label", rlb.getLabel());
		JSONUtility.appendBooleanValue(sb, "renderHeaderAsImg",
				rlb.isRenderHeaderAsImg());
		JSONUtility.appendStringValue(sb, "headerImgName",
				rlb.getHeaderImgName());
		JSONUtility.appendBooleanValue(sb, "renderContentAsImg",
				rlb.isRenderContentAsImg());
		JSONUtility.appendBooleanValue(sb, "fieldIsCustomIcon",
				rlb.isFieldIsCustomIcon());
		JSONUtility.appendBooleanValue(sb, "renderAsLong",
				rlb.isRenderAsLong(), true);

		sb.append("}");
		return sb.toString();
	}

	/*
	 * public static String
	 * encodeReportLayoutWrapperList(List<ReportLayoutWrapper> list){
	 * StringBuilder sb=new StringBuilder(); sb.append("["); if(list!=null){
	 * ReportLayoutWrapper bean; for (int i = 0; i < list.size(); i++) {
	 * bean=list.get(i); if(i>0){ sb.append(","); }
	 * sb.append(encodeReportLayoutWrapper(bean)); } } sb.append("]"); return
	 * sb.toString(); }
	 */
	/*
	 * public static String encodeReportLayoutWrapper(ReportLayoutWrapper rlw){
	 * StringBuilder sb=new StringBuilder(); sb.append("{");
	 *
	 * JSONUtility.appendBooleanValue(sb, "used", rlw.getUsed());
	 * JSONUtility.appendIntegerValue(sb, "defaultWidth",
	 * rlw.getDefaultWidth()); JSONUtility.appendJSONValue(sb,
	 * "reportLayout",encodeReportLayoutBean(rlw.getReportLayout(),false),true);
	 * sb.append("}"); return sb.toString(); }
	 */
	private static String encodeGroupFieldBeanList(List<GroupFieldTO> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				GroupFieldTO bean = list.get(i);
				if (i > 0) {
					sb.append(",");
				}
				sb.append(encodeGroupFieldTO(bean));
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private static String encodeGroupFieldTO(GroupFieldTO gfb) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		JSONUtility.appendIntegerValue(sb, "fieldID", gfb.getFieldID());
		JSONUtility.appendBooleanValue(sb, "descending", gfb.isDescending());
		JSONUtility
				.appendBooleanValue(sb, "collapsed", gfb.isCollapsed(), true);
		sb.append("}");
		return sb.toString();
	}

	/*
	 * public static String encodeGrouping(List<Integer>
	 * groupingLevels,List<GroupFieldBean> groupColumns, List<IntegerStringBean>
	 * groupFieldList,List<IntegerStringBean> ascendingDescendingList,
	 * List<IntegerStringBean> expandCollapseList){ StringBuilder sb=new
	 * StringBuilder(); sb.append("{");
	 * JSONUtility.appendIntegerListAsArray(sb,"groupingLevels",groupingLevels);
	 * JSONUtility.appendJSONValue(sb, "groupColumns",
	 * encodeGroupFieldBeanList(groupColumns)); //groupFieldList
	 * JSONUtility.appendIntegerStringBeanList
	 * (sb,"groupFieldList",groupFieldList);
	 * JSONUtility.appendIntegerStringBeanList
	 * (sb,"ascendingDescendingList",ascendingDescendingList);
	 * JSONUtility.appendIntegerStringBeanList
	 * (sb,"expandCollapseList",expandCollapseList,true); sb.append("}"); return
	 * sb.toString(); }
	 */

	public static String encodeIssueListViewDescriptors(
			List<IssueListViewDescriptor> list, Locale locale) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (list != null) {
			IssueListViewDescriptor descriptor;
			for (int i = 0; i < list.size(); i++) {
				descriptor = list.get(i);
				if (i > 0) {
					sb.append(",");
				}
				sb.append(encodeIssueListViewDescriptor(descriptor, locale));
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static String encodeIssueListViewDescriptor(
			IssueListViewDescriptor descriptor, Locale locale) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		JSONUtility.appendStringValue(sb, "name",
				getText("itemov.viewMode." + descriptor.getName(), locale));
		JSONUtility.appendStringValue(
				sb,
				"description",
				getText("itemov.viewMode." + descriptor.getDescription(),
						locale));
		JSONUtility.appendStringValue(sb, "jsClass", descriptor.getJsClass());
		JSONUtility.appendStringValue(sb, "jsConfigClass",
				descriptor.getJsConfigClass());
		JSONUtility.appendStringValue(sb, "theClassName",
				descriptor.getTheClassName());

		JSONUtility.appendStringValue(sb, "iconCls", descriptor.getIconCls());
		JSONUtility.appendBooleanValue(sb, "enabledColumnChoose",
				descriptor.isEnabledColumnChoose());
		JSONUtility.appendBooleanValue(sb, "enabledGrouping",
				descriptor.isEnabledGrouping());
		JSONUtility.appendBooleanValue(sb, "useLongFields",
				descriptor.isUseLongFields());
		JSONUtility.appendBooleanValue(sb, "plainData",
				descriptor.isPlainData());
		JSONUtility.appendStringValue(sb, "id", descriptor.getId(), true);

		sb.append("}");
		return sb.toString();
	}

	private static String getText(String s, Locale locale) {
		return LocalizeUtil.getLocalizedTextFromApplicationResources(s, locale);
	}

}
