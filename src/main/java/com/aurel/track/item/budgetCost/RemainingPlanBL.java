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

package com.aurel.track.item.budgetCost;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.ActualEstimatedBudgetDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.budgetCost.AccountingBL.TIMEUNITS;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.IEventSubscriber;
import com.aurel.track.util.event.parameters.AfterBudgetExpenseChangeEventParam;

/**
 * Business logic for remaining plan (costBeans)
 * @author Tamas Ruff
 *
 */
public class RemainingPlanBL {
	private static ActualEstimatedBudgetDAO actualEstimatedBudgetDAO = DAOFactory.getFactory().getActualEstimatedBudgetDAO();
	//the remaining value should not be negative
	public static Double MINIMAL_ESTIMATED_VALUE = new Double(0);
	/**
	 * Loads the remaining plan by workItem
	 * @param workItemID
	 * @return
	 */
	public static TActualEstimatedBudgetBean loadByWorkItemID(Integer workItemID) {
		return actualEstimatedBudgetDAO.loadByWorkItemKey(workItemID);
	}

	/**
	 * Saves the actualEstimatedBudgetBean
	 * @param actualEstimatedBudgetBean
	 * @return
	 */
	public static Integer save(TActualEstimatedBudgetBean actualEstimatedBudgetBean) {
		return actualEstimatedBudgetDAO.save(actualEstimatedBudgetBean);
	}

	/**
	 * Delete an actualEstimatedBudgetBean by primary key
	 * @param workItemID
	 */
	public static void deleteByWorkItem(Integer workItemID) {
		actualEstimatedBudgetDAO.deleteByWorkItem(workItemID);
	}

	/**
	 * Loads a ActualEstimatedBudgetBean list by workItemKeys
	 * @param workItemKeys
	 * @return
	 */
	public static List<TActualEstimatedBudgetBean> loadByWorkItemKeys(int[] workItemKeys) {
		return actualEstimatedBudgetDAO.loadByWorkItemKeys(workItemKeys);
	}

	/**
	 * Loads the remaining values for a tree filter
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param computedValueTypes
	 * @return
	 */
	public static List<TActualEstimatedBudgetBean> loadByTreeFilter(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID) {
		return actualEstimatedBudgetDAO.loadByTreeFilter(filterUpperTO, raciBean, personID);
	}

	/**
	 * Get the remaining values for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @param computedValueTypes
	 * @return
	 */
	public static List<TActualEstimatedBudgetBean> loadByTQLFilter(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors) {
		return actualEstimatedBudgetDAO.loadByTQLFilter(tqlExpression, personBean, locale, errors);
	}

	/**
	 * Loads the itemID keyed map of estimated budget beans
	 * @param workItemIDs
	 * @return
	 */
	public static Map<Integer, TActualEstimatedBudgetBean> loadRemainingBudgetMapByItemIDs(int[] workItemIDs){
		Map<Integer,TActualEstimatedBudgetBean> map = new HashMap<Integer, TActualEstimatedBudgetBean>();
		List<TActualEstimatedBudgetBean> estimatedBeansList = loadByWorkItemKeys(workItemIDs);
		if (estimatedBeansList!=null) {
			for (TActualEstimatedBudgetBean actualEstimatedBudgetBean : estimatedBeansList) {
				map.put(actualEstimatedBudgetBean.getWorkItemID(), actualEstimatedBudgetBean);
			}
		}
		return map;
	}
	
	/**
	 * Once a planned value was changed on a leaf workItem or a new leaf workItem
	 * was created or copied or the parent was changed the ancestor planned values should be actualized
	 * @param parentID
	 * @param hoursPerWorkingDay
	 * @param forceRecompute cover the case then the remaining plan was set form a non null value to null
	 * (the ancestors contain the previous non-null values in their sum)
	 * FIXME it should be synchronized but performance is also important
	 */
	public static /*synchronized*/ void actualizeAncestorRemainingPlannedValues(Integer workItemID,
			Integer parentID, Double hoursPerWorkingDay) {
		//recalculate remaining planned values for ancestors
		Set<Integer> visistedAscendentsSet = new HashSet<Integer>();
		while (parentID!=null) {
			TWorkItemBean parentWorkItem = null;
			try {
				parentWorkItem = ItemBL.loadWorkItem(parentID);
			} catch (ItemLoaderException e) {
			}
			if (parentWorkItem!=null) {
				setSumOfDescendentRemainingPlannedValues(parentID, hoursPerWorkingDay);
			}
			parentID = parentWorkItem.getSuperiorworkitem();
			if (parentID!=null) {
				if (!visistedAscendentsSet.contains(parentID)) {
					visistedAscendentsSet.add(parentID);
				} else {
					//circular reference
					parentID = null;
				}
			}
		}
	}

	/**
	 * Gets the sum of descendants' remaining plan. If the last child of the workItemID was just removed (is is leaf now) then return null
	 * signaling that the computed value (calculated from descendants) should be changed with the last value
	 * from the budget history (if any, otherwise the computed value should be deleted)
	 * Sum up the nearest descendant with computedValuesBean found
	 * (do not search in children of the nearest descendant,
	 * we suppose that the budget/plan value conforms in their children)
	 * if computedValueType is PLAN probably only the direct children are summed up (planned values are calculated),
	 * if computedValueType is BUDGET it could be more deep (budgets are not necessarily specified at each level)
	 * @param workItemID
	 * @param effortType
	 * @param computedValueType
	 * @param hoursPerWorkingDay
	 * @return
	 */
	private static void setSumOfDescendentRemainingPlannedValues(Integer workItemID, Double hoursPerWorkingDay) {
		if (workItemID==null) {
			return;
		}
		Integer commonDenominatorTimeUnit = TIMEUNITS.WORKDAYS;
		List<TActualEstimatedBudgetBean> allComputedValuesBean = new ArrayList<TActualEstimatedBudgetBean>();
		Set<Integer> toRemoveSet = new HashSet<Integer>();
		toRemoveSet.add(workItemID);
		List<TWorkItemBean> childrenWorkItemBeans = ItemBL.getChildren(workItemID);
		if (childrenWorkItemBeans==null || childrenWorkItemBeans.isEmpty()) {
				//after a parent change the old parent will be leaf (the only child was removed)
				//as best effort try to calculate the remaining plan based on total plan and expenses
				TActualEstimatedBudgetBean actualEstimatedBudgetBean = new TActualEstimatedBudgetBean();
				RemainingPlanBL.calculateFirstRemainingPlanFromDb(actualEstimatedBudgetBean, workItemID, hoursPerWorkingDay);
				saveRemainingPlan(workItemID, actualEstimatedBudgetBean.getEstimatedHours(),
						actualEstimatedBudgetBean.getTimeUnit(), actualEstimatedBudgetBean.getEstimatedCost(), null);
		} else {
			while (childrenWorkItemBeans!=null && !childrenWorkItemBeans.isEmpty()) {
				Set<Integer> childIDSet = GeneralUtils.createIntegerSetFromBeanList(childrenWorkItemBeans);
				int[] childIDsArr =GeneralUtils.createIntArrFromSet(childIDSet);
				List<TActualEstimatedBudgetBean> remainingPlanValuesList =
						loadByWorkItemKeys(childIDsArr);
				if (remainingPlanValuesList!=null && !remainingPlanValuesList.isEmpty()) {
					//temporary table, processed later
					allComputedValuesBean.addAll(remainingPlanValuesList);
					for (TActualEstimatedBudgetBean actualEstimatedBudgetBean : remainingPlanValuesList) {
						Integer childID = actualEstimatedBudgetBean.getWorkItemID();
						//nearest remaining planned values found on the hierarchy:
						//remove from set to not get the remaining plan of the children (do not count twice)
						childIDSet.remove(childID);
						Integer timeUnitChild = actualEstimatedBudgetBean.getTimeUnit();
						if (timeUnitChild==null || timeUnitChild.intValue()==TIMEUNITS.HOURS) {
							commonDenominatorTimeUnit = TIMEUNITS.HOURS;
						}
					}
				}
				childIDSet.removeAll(toRemoveSet);
				if (!childIDSet.isEmpty()) {
					childIDsArr = GeneralUtils.createIntArrFromSet(childIDSet);
					childrenWorkItemBeans = ItemBL.getChildren(childIDsArr, true, null, null, null);
					toRemoveSet.addAll(childIDSet);
				} else {
					childrenWorkItemBeans = null;
				}
			}
			double sumHours = 0.0;
			double sumCost = 0.0;
			for (TActualEstimatedBudgetBean actualEstimatedBudgetBean : allComputedValuesBean) {
				Double hours = actualEstimatedBudgetBean.getEstimatedHours();
				if (hours!=null) {
					if (TIMEUNITS.WORKDAYS.equals(commonDenominatorTimeUnit)) {
						sumHours+=hours;
					} else {
						Integer timeUnit = actualEstimatedBudgetBean.getTimeUnit();
						if (timeUnit!=null && timeUnit.equals(TIMEUNITS.WORKDAYS)) {
							//transform WORKDAYS to HOURS
							sumHours+=AccountingBL.transformToTimeUnits(hours, hoursPerWorkingDay, TIMEUNITS.WORKDAYS, TIMEUNITS.HOURS);
						} else {
							sumHours+=hours;
						}
					}
				}
				Double cost = actualEstimatedBudgetBean.getEstimatedCost();
				if (cost!=null) {
					sumCost += cost;
				}
			}
			if (isZero(sumHours) && isZero(sumCost)) {
				deleteByWorkItem(workItemID);
			} else {
				saveRemainingPlan(workItemID, sumHours, commonDenominatorTimeUnit, sumCost, null);
			}
		}
	}

	/**
	 * Save the remaining plan
	 * @param workItemID
	 * @param sumWork
	 * @param timeUnit
	 * @param sumCost
	 * @param personID
	 */
	static TActualEstimatedBudgetBean saveRemainingPlan(Integer workItemID,
			Double sumWork, Integer timeUnit, Double sumCost, Integer personID) {
		TActualEstimatedBudgetBean actualEstimatedBudgetBean = actualEstimatedBudgetDAO.loadByWorkItemKey(workItemID);
		if (actualEstimatedBudgetBean==null) {
			actualEstimatedBudgetBean = new TActualEstimatedBudgetBean();
			actualEstimatedBudgetBean.setLastEdit(new Date());
			actualEstimatedBudgetBean.setWorkItemID(workItemID);
		}
		if (personID!=null) {
			actualEstimatedBudgetBean.setChangedByID(personID);
		}
		actualEstimatedBudgetBean.setEstimatedHours(sumWork);
		actualEstimatedBudgetBean.setTimeUnit(timeUnit);
		actualEstimatedBudgetBean.setEstimatedCost(sumCost);
		save(actualEstimatedBudgetBean);
		return actualEstimatedBudgetBean;
	}

	/**
	 * Compute the bottom up dates recursively starting from top parents
	 * @param workItemBean
	 * @param parentToChildrenMap
	 * @param workItemBeansMap
	 * @param startDate
	 * @return
	 */
	public static TActualEstimatedBudgetBean computeBottomUpRemainingPlannedValues(TWorkItemBean workItemBean,
			Map<Integer, List<Integer>> parentToChildrenMap,
			Map<Integer, TWorkItemBean> workItemBeansMap, Map<Integer, Double> hoursPerWorkdayForProject) {
		Integer workItemID = workItemBean.getObjectID();
		Integer projectID = workItemBean.getProjectID();
		List<Integer> childrenList = parentToChildrenMap.get(workItemID);
		if (childrenList==null) {
			//leaf workItem
			return actualEstimatedBudgetDAO.loadByWorkItemKey(workItemID);
		} else {
			//parent with children
			Integer commonTimeUnit = TIMEUNITS.WORKDAYS;
			List<TActualEstimatedBudgetBean> childPlans = new LinkedList<TActualEstimatedBudgetBean>();
			//compute the bottom up values for the children and add them in a temporary list
			//in the same cycle compute the commonDenominatorTimeUnit:
			for (Integer childID : childrenList) {
				TWorkItemBean childWorkItem = workItemBeansMap.get(childID);
				TActualEstimatedBudgetBean actualEstimatedBudgetBeanChild = null;
				if (childWorkItem!=null) {
					actualEstimatedBudgetBeanChild = computeBottomUpRemainingPlannedValues(childWorkItem, parentToChildrenMap,
							workItemBeansMap, hoursPerWorkdayForProject);
					if (actualEstimatedBudgetBeanChild!=null) {
						//add in a temporary list to compute it later once the commonDenominatorTimeUnit is known
						childPlans.add(actualEstimatedBudgetBeanChild);
						Integer timeUnitChild = actualEstimatedBudgetBeanChild.getTimeUnit();
						if (timeUnitChild!=null && TIMEUNITS.HOURS.equals(timeUnitChild)) {
							commonTimeUnit = TIMEUNITS.HOURS;
						}
					}
				}
			}
			//sum up the children in an additional cycle once the commonDenominatorTimeUnit is known
			double childenPlanHourSum = 0.0;
			double childenPlanCostSum = 0.0;
			if (!childPlans.isEmpty()) {
				//at least one child found with planned value set
				for (TActualEstimatedBudgetBean valueAndTimeUnitBeanChild : childPlans) {
					Double childHour = valueAndTimeUnitBeanChild.getEstimatedHours();
					Double childCost = valueAndTimeUnitBeanChild.getEstimatedCost();
					if (childHour!=null) {
						if (TIMEUNITS.WORKDAYS.equals(commonTimeUnit)) {
							childenPlanHourSum+=childHour.doubleValue();
						} else {
							if (TIMEUNITS.WORKDAYS.equals(valueAndTimeUnitBeanChild.getTimeUnit())) {
								Double hoursPerWorkingDay = hoursPerWorkdayForProject.get(projectID);
								if (hoursPerWorkingDay==null) {
									hoursPerWorkingDay = ProjectBL.getHoursPerWorkingDay(projectID);
									hoursPerWorkdayForProject.put(projectID, hoursPerWorkingDay);
								}
								//transform WORKDAYS to HOURS
								childenPlanHourSum+=AccountingBL.transformToTimeUnits(childHour, hoursPerWorkingDay, TIMEUNITS.WORKDAYS, TIMEUNITS.HOURS);
							} else {
								childenPlanHourSum+=childHour.doubleValue();
							}
						}
					}
					if (childCost!=null) {
						childenPlanCostSum+=childCost.doubleValue();
					}
				}
				return saveRemainingPlan(workItemID, childenPlanHourSum, commonTimeUnit, childenPlanCostSum, null);
			}
			return null;
		}
	}

	

	/**
	 * Updates the estimated remaining budget in form if auto adjust
	 * @param accountingForm
	 * @param workItemKey
	 * @param project
	 */
	public static void calculateFirstRemainingPlanFromDb(
			TActualEstimatedBudgetBean actualEstimatedBudgetBean,
			Integer workItemKey, Double hoursPerWorkingDay) {
		TBudgetBean budgetBean = BudgetBL.loadLastBudgetOrPlanFromDb(workItemKey, true);
		if (budgetBean!=null) {
			if (actualEstimatedBudgetBean.getTimeUnit()==null) {
				actualEstimatedBudgetBean.setTimeUnit(budgetBean.getTimeUnit());
			}
			if (budgetBean.getEstimatedHours()!=null && actualEstimatedBudgetBean.getEstimatedHours()==null) {
				double totalPlanWork = AccountingBL.transformToTimeUnits(budgetBean.getEstimatedHours(), hoursPerWorkingDay,
					budgetBean.getTimeUnit(), TIMEUNITS.HOURS).doubleValue();
				//set remaining hours only if total plan for hours exist
				TComputedValuesBean computedValuesBeanWork = ComputedValueBL.loadByWorkItemAndTypesAndPerson(workItemKey,
						TComputedValuesBean.EFFORTTYPE.TIME, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null);
				double totalExpenseWork = 0.0;
				if (computedValuesBeanWork!=null) {
					totalExpenseWork = computedValuesBeanWork.getComputedValue();
				}
				if (totalPlanWork<totalExpenseWork) {
					actualEstimatedBudgetBean.setEstimatedHours(MINIMAL_ESTIMATED_VALUE);
				} else {
					actualEstimatedBudgetBean.setEstimatedHours(
							AccountingBL.roundToDecimalDigits(AccountingBL.transformToTimeUnits(new Double(totalPlanWork - totalExpenseWork), hoursPerWorkingDay,
								TIMEUNITS.HOURS, actualEstimatedBudgetBean.getTimeUnit()), true));
				}
			}
			if (budgetBean.getEstimatedCost()!=null && actualEstimatedBudgetBean.getEstimatedCost()==null) {
				double totalPlanCost = budgetBean.getEstimatedCost().doubleValue();
				//set remaining costs only if total plan for costs exist
				TComputedValuesBean computedValuesBeanCost = ComputedValueBL.loadByWorkItemAndTypesAndPerson(workItemKey,
						TComputedValuesBean.EFFORTTYPE.COST, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null);
				double totalExpenseCost = 0.0;
				if (computedValuesBeanCost!=null) {
					totalExpenseCost = computedValuesBeanCost.getComputedValue();
				}
				if (totalPlanCost<totalExpenseCost) {
					actualEstimatedBudgetBean.setEstimatedCost(MINIMAL_ESTIMATED_VALUE);
				} else {
					actualEstimatedBudgetBean.setEstimatedCost(AccountingBL.roundToDecimalDigits(new Double(totalPlanCost - totalExpenseCost), false));
				}
			}
		}
	}

	/**
	 * Saves the remaining plan 
	 * @param actualEstimatedBudgetBeanNew
	 * @param workItemBean
	 * @param personBean
	 * @param notify
	 */
	public static void saveEstimatedRemainingBudgetToDb(TActualEstimatedBudgetBean actualEstimatedBudgetBeanNew,TWorkItemBean workItemBean,TPersonBean personBean, boolean notify){
		Integer workItemKey=workItemBean.getObjectID();
		//get the existing estimated remaining budget
		TActualEstimatedBudgetBean actualEstimatedBudgetBeanOld = loadByWorkItemID(workItemKey);
		if (actualEstimatedBudgetBeanOld==null) {
			actualEstimatedBudgetBeanOld = new TActualEstimatedBudgetBean();
		}
		saveRemainingPlanAndNotify(actualEstimatedBudgetBeanNew, actualEstimatedBudgetBeanOld, personBean, workItemBean,notify);
		//actualize ancestor planned values
		Integer parentID = workItemBean.getSuperiorworkitem(); 
		if (parentID!=null && ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior()) {
			Double hoursPerWorkingday = ProjectBL.getHoursPerWorkingDay(workItemBean.getProjectID());
			RemainingPlanBL.actualizeAncestorRemainingPlannedValues(workItemBean.getObjectID(), parentID, hoursPerWorkingday);
		}
	}
	
	/**
	 * Saves the remaining plan
	 * @param actualEstimatedBudgetBeanNew
	 * @param actualEstimatedBudgetBeanOld
	 * @param personBean
	 * @param workItemBean
	 * @param notify
	 */
	public static void saveRemainingPlanAndNotify(
			TActualEstimatedBudgetBean actualEstimatedBudgetBeanNew,
			TActualEstimatedBudgetBean actualEstimatedBudgetBeanOld,
			TPersonBean personBean, TWorkItemBean workItemBean, boolean notify) {
		Integer workItemKey=workItemBean.getObjectID();
		if (actualEstimatedBudgetBeanNew.hasChanged(actualEstimatedBudgetBeanOld)) {
			actualEstimatedBudgetBeanNew.setLastEdit(new Date());
			actualEstimatedBudgetBeanNew.setWorkItemID(workItemKey);
			actualEstimatedBudgetBeanNew.setChangedByID(personBean.getObjectID());
			//old entry saved because it is not historized
			save(actualEstimatedBudgetBeanNew);
			if (notify) {
				notifyRemainingPlanChange(personBean, workItemBean, actualEstimatedBudgetBeanOld, actualEstimatedBudgetBeanNew);
			}
		}
	}

	/**
	 * Notify about remining plan change
	 * @param personBean
	 * @param workItemBean
	 * @param actualEstimatedBudgetBeanOld
	 * @param actualEstimatedBudgetBeanNew
	 */
	private static void notifyRemainingPlanChange(TPersonBean personBean, TWorkItemBean workItemBean,
			TActualEstimatedBudgetBean actualEstimatedBudgetBeanOld, TActualEstimatedBudgetBean actualEstimatedBudgetBeanNew) {
		//send notification mail after saving
		AfterBudgetExpenseChangeEventParam afterBudgetExpenseChangeEventParam = new AfterBudgetExpenseChangeEventParam();
		afterBudgetExpenseChangeEventParam.setNewRemainingBudget(actualEstimatedBudgetBeanNew);
		afterBudgetExpenseChangeEventParam.setOldRemainingBudget(actualEstimatedBudgetBeanOld);
		afterBudgetExpenseChangeEventParam.setWorkItemBean(workItemBean);
		afterBudgetExpenseChangeEventParam.setPersonBean(personBean);
		EventPublisher evp = EventPublisher.getInstance();
		if (evp != null) {
			List<Integer> events = new ArrayList<Integer>();
			events.add(new Integer(IEventSubscriber.EVENT_POST_ISSUE_UPDATEREMAININGPLAN));
			evp.notify(events, afterBudgetExpenseChangeEventParam);
		}
	}

	/**
	 * After resetting the summary behavior calculate the remaining plan as the plan - expense
	 * (this is only best effort, anyway better as leave the previously calculated remaining plan summed up from ancestors)
	 * @param workItemIDs
	 */
	public static void resetRemainingPlanTo100Percent(List<Integer> workItemIDs) {
		List<TWorkItemBean> parentWorkItemBeans = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromIntegerCollection(workItemIDs));
		Map<Integer, Double> hoursPerWorkdayForProject = new HashMap<Integer, Double>();
		for (TWorkItemBean workItemBean : parentWorkItemBeans) {
			Integer workItemID = workItemBean.getObjectID();
			Integer projectID = workItemBean.getProjectID();
			Double hoursPerWorkingDay = hoursPerWorkdayForProject.get(projectID);
			if (hoursPerWorkingDay==null) {
				hoursPerWorkingDay = ProjectBL.getHoursPerWorkingDay(projectID);
				hoursPerWorkdayForProject.put(projectID, hoursPerWorkingDay);
			}
			TActualEstimatedBudgetBean actualEstimatedBudgetBean = new TActualEstimatedBudgetBean();
			calculateFirstRemainingPlanFromDb(actualEstimatedBudgetBean, workItemID, hoursPerWorkingDay);
			saveRemainingPlan(workItemID, actualEstimatedBudgetBean.getEstimatedHours(),
					actualEstimatedBudgetBean.getTimeUnit(), actualEstimatedBudgetBean.getEstimatedCost(), null);
		}
	}

	private static boolean isZero(double val) {
		if(Double.doubleToRawLongBits(val) == 0) {
			return true;
		}
		return false;
	}
}
