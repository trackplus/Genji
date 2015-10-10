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

package com.aurel.track.item.budgetCost;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.budgetCost.AccountingBL.TIMEUNITS;
import com.aurel.track.itemNavigator.ItemTreeNode;
import com.aurel.track.report.execute.ComputedValuesLoaderBL;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.report.group.GroupLimitBean;
import com.aurel.track.util.GeneralUtils;

public class PercentDoneUtil {

	public static Logger LOGGER = LogManager.getLogger(PercentDoneUtil.class);

	/************************************************************* CALCULATING PERCENT DONE **********************************************************/
	/**
	 * This method calculates percent done number for each work item from report bean.
	 * Returns a map with work item object id and the percent done number. /ObjectID=>percent done number/
	 * Loads also two maps for used by calculating percent done at group level
	 * @param reportBeans
	 * @param personID
	 * @param workItemIDToActualHours
	 * @param workItemIDToRemainingHours
	 * @return
	 */
	public static Map<Integer, Double> getItemPercentsDone(ReportBeans reportBeans, Integer personID,
			Map<Integer, Double> projectWorkingHoursMap) {
		List<ReportBean> reporBeanList = reportBeans.getItems();
		List<TWorkItemBean> workItemBeans = new LinkedList<TWorkItemBean>();
		Map<Integer, Integer> itemIDToProjectID = new HashMap<Integer, Integer>();
		int[] workItemKeys = new int[reporBeanList.size()];
		Set<Integer> projectIDsSet = new HashSet<Integer>();
		DecimalFormat numberFormatter = new DecimalFormat("#.##");
		int i = 0;
		for(ReportBean reportBean : reporBeanList) {
			TWorkItemBean workItemBean = reportBean.getWorkItemBean();
			Integer workItemID = workItemBean.getObjectID();
			Integer projectID = workItemBean.getProjectID();
			workItemBeans.add(workItemBean);
			workItemKeys[i] = workItemBean.getObjectID();
			projectIDsSet.add(projectID);
			itemIDToProjectID.put(workItemID, projectID);
			i++;
		}
		List<Integer> fieldIDs = new LinkedList<Integer>();
		fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
		List<TComputedValuesBean> computedValueBeans = ComputedValuesLoaderBL.loadByWorkItemKeys(
				workItemBeans, AccessBeans.getFieldRestrictions(personID, workItemBeans, fieldIDs, false),
				TComputedValuesBean.EFFORTTYPE.TIME, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null, false, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Computed value beans list size: " + computedValueBeans.size());
		}
		Map<Integer, Double> workItemIDToActualHours = new HashMap<Integer, Double>();
		for(TComputedValuesBean valueBean : computedValueBeans) {
			if(valueBean.getPerson() == null) {
				workItemIDToActualHours.put(valueBean.getWorkitemKey(), valueBean.getComputedValue());
				if(LOGGER.isDebugEnabled()) {
					LOGGER.debug("WorkItem key: " + valueBean.getWorkitemKey() + " " + "Computed value: " + valueBean.getComputedValue());
				}
			}
		}
		for (Integer projectID : projectIDsSet) {
			Double hoursPerWorkDay = null;
			if(projectID != null) {
				hoursPerWorkDay = ProjectBL.getHoursPerWorkingDay(projectID);
			}
			if(hoursPerWorkDay == null) {
				hoursPerWorkDay = AccountingBL.DEFAULTHOURSPERWORKINGDAY;
			}
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Hourse per workday for project " + projectID + ": " + hoursPerWorkDay);
			}
			projectWorkingHoursMap.put(projectID, hoursPerWorkDay);
		}
		List<TActualEstimatedBudgetBean> estimateBudgets = RemainingPlanBL.loadByWorkItemKeys(workItemKeys);
		Map<Integer, Double>workItemIdToPercentDone = new HashMap<Integer, Double>();
		for(TActualEstimatedBudgetBean estimated : estimateBudgets) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Estimated hours: " + estimated.getEstimatedHours());
			}
			Double estimatedRemainingHours = estimated.getEstimatedHours();
			Integer workItemID = estimated.getWorkItemID();
			Double actualCosts = workItemIDToActualHours.get(workItemID);
			Integer estimatedTimeUnit = estimated.getTimeUnit();
			if (estimatedRemainingHours!=null) {
				if (estimatedTimeUnit!=null && estimatedTimeUnit.equals(TIMEUNITS.WORKDAYS)) {
					//the remaining hours are calculated in hours
					Double hoursPerWorkDay = null;
					Integer projectID = itemIDToProjectID.get(workItemID);
					if (projectID!=null) {
						hoursPerWorkDay = projectWorkingHoursMap.get(projectID);
					}
					if (hoursPerWorkDay==null) {
						hoursPerWorkDay = AccountingBL.DEFAULTHOURSPERWORKINGDAY;
					}
					estimatedRemainingHours *= hoursPerWorkDay;
				}
			}
			if (estimatedRemainingHours!=null && actualCosts!=null) {
				Double realEstimated = actualCosts + estimatedRemainingHours;
				Double percentDone = (actualCosts / realEstimated) * 100;
				String formatted = numberFormatter.format(percentDone);
				formatted = formatted.replaceAll(",", ".");
				percentDone = Double.valueOf(formatted);
				workItemIdToPercentDone.put(workItemID, percentDone);
				if(LOGGER.isDebugEnabled()) {
					LOGGER.debug("Sum of actual cost: " + actualCosts + " " + "Sum estimated: " + realEstimated);
				}
			}
		}
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Work item id to percent done map size: " + workItemIdToPercentDone.size());
		}
		return workItemIdToPercentDone;
	}

	/**
	 * This method calculates percent done number for each group from ReportBeans.
	 * Returns a map with work item groupValue and the percent done number
	 * @param nodes
	 * @param workItemIDToActualHours
	 * @param workItemIDToRemainingHours
	 * @return
	 */
	public static Map<Object, Double> getGroupPercentsDone(List<ItemTreeNode> nodes, Map<Integer, Double> projectWorkingHoursMap,
			Map<Object, Date> groupValueToStartDate, Map<Object, Date> groupValueToEndDate,
			Map<Object, Date> groupValueToTopDownStartDate, Map<Object, Date> groupValueToTopDownEndDate) {
		Map<Object, Double> groupValueToActualHours = new HashMap<Object, Double>();
		Map<Object, Double> groupValueToRemainingHours = new HashMap<Object, Double>();
		Map<Object, Double> groupValueToPercentDone = new HashMap<Object, Double>();
		if (nodes!=null){
			for (Iterator<ItemTreeNode> iterator = nodes.iterator(); iterator.hasNext();) {
				ItemTreeNode treeNode = iterator.next();
				GroupLimitBean groupLimitBean = treeNode.getGroupLimitBean();
				if (groupLimitBean!=null){
					computeGroupPercentsDone(groupLimitBean, projectWorkingHoursMap,
							groupValueToStartDate, groupValueToEndDate, groupValueToTopDownStartDate, groupValueToTopDownEndDate,
							groupValueToActualHours, groupValueToRemainingHours, groupValueToPercentDone);
				}
			}
		}
		return groupValueToPercentDone;
	}

	/**
	 * This method calculates percent done number for a group and their subgroups recursively
	 * @param groupLimitBean
	 * @param workItemIDToActualHours
	 * @param workItemIDToRemainingHours
	 * @param groupValueToActualHours
	 * @param groupValueToRemainingHours
	 * @param groupValueToPercentDone
	 */
	private static void computeGroupPercentsDone(GroupLimitBean groupLimitBean, Map<Integer, Double> projectWorkingHoursMap,
			Map<Object, Date> groupValueToStartDate, Map<Object, Date> groupValueToEndDate,
			Map<Object, Date> groupValueToTopDownStartDate, Map<Object, Date> groupValueToTopDownEndDate,
		Map<Object, Double> groupValueToActualHours, Map<Object, Double> groupValueToRemainingHours,
		Map<Object, Double> groupValueToPercentDone) {
		if (groupLimitBean!=null) {
			DecimalFormat numberFormatter = new DecimalFormat("#.##");
			Object groupValue = groupLimitBean.getGroupValue();
			Integer projectID = (Integer)groupValue;
			List<TWorkItemBean> projectWorkItems = ItemBL.loadAllByProject(projectID, null, null);
			Set<Integer> workItemIDSet = GeneralUtils.createIntegerSetFromBeanList(projectWorkItems);
			for (Iterator<TWorkItemBean> iterator = projectWorkItems.iterator(); iterator.hasNext();) {
				TWorkItemBean workItemBean = (TWorkItemBean) iterator.next();
				Integer parentID = workItemBean.getSuperiorworkitem();
				if (parentID!=null && workItemIDSet.contains(parentID)) {
					//only the highest level items should remain because they sum up the item expenses from the lower level
					iterator.remove();
				}
			}
			Date minStartDate = null;
			Date maxEndDate = null;
			Date minTopDownStartDate = null;
			Date maxTopDownEndDate = null;
			Double totalActualHours = 0.0;
			Double totalRemainingHours = 0.0;
			if (!projectWorkItems.isEmpty()) {
				for (TWorkItemBean workItemBean : projectWorkItems) {
					Date startDate = workItemBean.getStartDate();
					if (startDate!=null) {
						if (minStartDate==null || minStartDate.after(startDate)) {
							minStartDate = startDate;
						}
					}
					Date endDate = workItemBean.getEndDate();
					if (endDate!=null) {
						if (maxEndDate==null || maxEndDate.before(endDate)) {
							maxEndDate = endDate;
						}
					}
					Date topDownStartDate = workItemBean.getTopDownStartDate();
					if (topDownStartDate!=null) {
						if (minTopDownStartDate==null || minTopDownStartDate.after(topDownStartDate)) {
							minTopDownStartDate = topDownStartDate;
						}
					}
					Date topDownEndDate = workItemBean.getTopDownEndDate();
					if (topDownEndDate!=null) {
						if (maxTopDownEndDate==null || maxTopDownEndDate.before(topDownEndDate)) {
							maxTopDownEndDate = topDownEndDate;
						}
					}
				}
				int[] nonChildProjectItems = GeneralUtils.createIntArrFromIntegerList(GeneralUtils.createIntegerListFromBeanList(projectWorkItems));
				if (groupValue!=null) {
					Double hoursPerWorkingDay = projectWorkingHoursMap.get(projectID);
					if (hoursPerWorkingDay==null) {
						hoursPerWorkingDay = AccountingBL.DEFAULTHOURSPERWORKINGDAY;
					}
					List<TComputedValuesBean> expenseComputedValues = ComputedValueBL.loadByWorkItemsAndTypesAndPerson(nonChildProjectItems,
							TComputedValuesBean.EFFORTTYPE.TIME, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null);
					for (TComputedValuesBean computedValuesBean : expenseComputedValues) {
						Double computedValue = computedValuesBean.getComputedValue();
						if (computedValue!=null) {
							totalActualHours+=computedValue;
						}
					}
					List<TActualEstimatedBudgetBean> remainingPlanValuesList = RemainingPlanBL.loadByWorkItemKeys(nonChildProjectItems);
					for (TActualEstimatedBudgetBean computedValuesBean : remainingPlanValuesList) {
						Double computedValue = computedValuesBean.getEstimatedHours();
						Integer timeUnit = computedValuesBean.getTimeUnit();
						if (computedValue!=null) {
							if (TIMEUNITS.WORKDAYS.equals(timeUnit)) {
								totalRemainingHours+=AccountingBL.transformToTimeUnits(computedValue, hoursPerWorkingDay, TIMEUNITS.WORKDAYS, TIMEUNITS.HOURS);
							} else {
								totalRemainingHours+=computedValue;
							}
						}
					}
				}
			}
			List<GroupLimitBean> subtrees = groupLimitBean.getTreeSubgroups();
			if (subtrees!=null) {
				for (GroupLimitBean subtreeLimitBean : subtrees) {
					Object subtreeValue = subtreeLimitBean.getGroupValue();
					computeGroupPercentsDone(subtreeLimitBean, projectWorkingHoursMap, groupValueToStartDate, groupValueToEndDate,
							groupValueToTopDownStartDate, groupValueToTopDownEndDate,
							groupValueToActualHours, groupValueToRemainingHours, groupValueToPercentDone);
					Double subtreeActualHours = groupValueToActualHours.get(subtreeValue);
					if (subtreeActualHours!=null) {
						totalActualHours+=subtreeActualHours.doubleValue();
					}
					Double subtreeRemainingHours = groupValueToRemainingHours.get(subtreeValue);
					if (subtreeRemainingHours!=null) {
						totalRemainingHours+=subtreeRemainingHours.doubleValue();
					}
					Date startDate = groupValueToStartDate.get(subtreeValue);
					if (startDate!=null) {
						if (minStartDate==null || minStartDate.after(startDate)) {
							minStartDate = startDate;
						}
					}
					Date endDate = groupValueToEndDate.get(subtreeValue);
					if (endDate!=null) {
						if (maxEndDate==null || maxEndDate.before(endDate)) {
							maxEndDate = endDate;
						}
					}
					Date topDownStartDate = groupValueToTopDownStartDate.get(subtreeValue);
					if (topDownStartDate!=null) {
						if (minTopDownStartDate==null || minTopDownStartDate.after(topDownStartDate)) {
							minTopDownStartDate = topDownStartDate;
						}
					}
					Date topDownEndDate = groupValueToTopDownEndDate.get(subtreeValue);
					if (topDownEndDate!=null) {
						if (maxTopDownEndDate==null || maxTopDownEndDate.before(topDownEndDate)) {
							maxTopDownEndDate = topDownEndDate;
						}
					}
				}
			}
			if (minStartDate!=null) {
				groupValueToStartDate.put(groupValue, minStartDate);
			}
			if (maxEndDate!=null) {
				groupValueToEndDate.put(groupValue, maxEndDate);
			}
			if (minTopDownStartDate!=null) {
				groupValueToTopDownStartDate.put(groupValue, minTopDownStartDate);
			}
			if (maxTopDownEndDate!=null) {
				groupValueToTopDownEndDate.put(groupValue, maxTopDownEndDate);
			}
			groupValueToActualHours.put(groupValue, totalActualHours);
			groupValueToRemainingHours.put(groupValue, totalRemainingHours);
			Double totalEstimated = totalActualHours + totalRemainingHours;
			Double percentDone = 0.0;
			if (Double.compare(totalEstimated, 0.0) != 0) {
				percentDone = (totalActualHours / totalEstimated) * 100;
				String formatted = numberFormatter.format(percentDone);
				formatted = formatted.replaceAll(",", ".");
				try {
					percentDone = Double.valueOf(formatted);
				} catch (Exception e) {
					LOGGER.warn("Getting double value from " + formatted + " failed with " + e.getMessage(), e);
				}
			}
			groupValueToPercentDone.put(groupValue, percentDone);
		}
	}

}
