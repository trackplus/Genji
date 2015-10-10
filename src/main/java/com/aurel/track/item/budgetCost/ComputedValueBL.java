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

import java.util.ArrayList;
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

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterBL.CHANGE_TYPE;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.ComputedValuesDAO;
import com.aurel.track.dao.CostDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.budgetCost.AccountingBL.TIMEUNITS;
import com.aurel.track.lucene.index.associatedFields.ExpenseIndexer;
import com.aurel.track.util.GeneralUtils;

/**
 * Logic with computed values 
 * @author Tamas Ruff
 *
 */
public class ComputedValueBL {
	
	public static class ValueAndTimeUnitBean {
		
		private Double doubleValue = null;
		private Integer timeUnit;
		private Integer personID;
		
		public Double getDoubleValue() {
			return doubleValue;
		}
		public void setDoubleValue(Double doubleValue) {
			this.doubleValue = doubleValue;
		}
		public Integer getTimeUnit() {
			return timeUnit;
		}
		public void setTimeUnit(Integer timeUnit) {
			this.timeUnit = timeUnit;
		}
		public Integer getPersonID() {
			return personID;
		}
		public void setPersonID(Integer personID) {
			this.personID = personID;
		}
	}

	private static final Logger LOGGER = LogManager.getLogger(ComputedValueBL.class);
	private static CostDAO costDAO = DAOFactory.getFactory().getCostDAO();
	private static ComputedValuesDAO computedValuesDAO = DAOFactory.getFactory().getComputedValuesDAO();
	
	/**
	 * Gets the computed value bean by workItem, effortType, computedValueType and person
	 * @param workItemID
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	public static TComputedValuesBean loadByWorkItemAndTypesAndPerson(Integer workItemID,
			int effortType, int computedValueType, Integer person) {
		return computedValuesDAO.loadByWorkItemAndTypesAndPerson(
			workItemID, effortType, computedValueType, person);
	}
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItems, types and person
	 * @param workItemIDs
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	public static List<TComputedValuesBean> loadByWorkItemsAndTypesAndPerson(int[] workItemIDs, int effortType, int computedValueType, Integer person) {
		return computedValuesDAO.loadByWorkItemsAndTypesAndPerson(workItemIDs, effortType, computedValueType, person);
	}
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItems and types
	 * @param workItemIDs
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	public static List<TComputedValuesBean> loadByWorkItemsAndTypes(int[] workItemIDs, int effortType, int computedValueType) {
		return computedValuesDAO.loadByWorkItemsAndTypes(workItemIDs, effortType, computedValueType);
	}
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItemIDs, computedValueType and person
	 * @param workItemIDs
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	public static List<TComputedValuesBean> loadByWorkItemsAndTypesAndPerson(int[] workItemIDs, int computedValueType, Integer person) {
		return computedValuesDAO.loadByWorkItemsAndTypesAndPerson(workItemIDs, computedValueType, person);
	}
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItemIDs and types
	 * @param workItemIDs
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	public static List<TComputedValuesBean> loadByWorkItemsAndTypes(int[] workItemIDs, int[] computedValueType){
		return computedValuesDAO.loadByWorkItemsAndTypes(workItemIDs, computedValueType);
	}
	
	/**
	 * Loads the computed values for a tree filter for a person
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param computedValueType
	 * @param myExpenses
	 * @return
	 */
	public static List<TComputedValuesBean> loadByTreeFilterForPerson(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID, int computedValueType, boolean myExpenses) {
		return computedValuesDAO.loadByTreeFilterForPerson(filterUpperTO, raciBean, personID, computedValueType, myExpenses);
	}
	
	/**
	 * Loads the computed values for a tree filter
	 * @param filterUpperTO
	 * @param raciBean
	 * @param computedValueTypes
	 * @return
	 */
	public static List<TComputedValuesBean> loadByTreeFilter(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID, int[] computedValueTypes) {
		return computedValuesDAO.loadByTreeFilter(filterUpperTO, raciBean, personID, computedValueTypes);
	}
	
	/**
	 * Get the computed values for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @param computedValueType
	 * @param myExpenses
	 * @return
	 */
	public static List<TComputedValuesBean> loadByTQLFilterForPerson(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors, int computedValueType, boolean myExpenses) {
		return computedValuesDAO.loadByTQLFilterForPerson(tqlExpression, personBean, locale, errors, computedValueType, myExpenses);
	}
	
	/**
	 * Get the computed values for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @param computedValueTypes
	 * @return
	 */
	public static List<TComputedValuesBean> loadByTQLFilter(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors, int[] computedValueTypes) {
		return computedValuesDAO.loadByTQLFilter(tqlExpression, personBean, locale, errors, computedValueTypes);
	}
	
	/**
	 * Gets the computed value 
	 * @param workItemID
	 * @param effortType
	 * @param computedValueType
	 * @return
	 */
	public static TComputedValuesBean loadByWorkItemAndTypes(
			Integer workItemID, int effortType, int computedValueType) {
		List<TComputedValuesBean> computedValuesList = loadByWorkItemsAndTypes(
				new int[] {workItemID}, effortType, computedValueType);
		if (computedValuesList!=null && !computedValuesList.isEmpty()) {
			return computedValuesList.get(0);
		}
		return null;
	}
	
	/**
	 * Gets the computed value 
	 * @param workItemID
	 * @param effortType
	 * @param computedValueType
	 * @return
	 */
	public static List<TComputedValuesBean> loadByWorkItemAndTypesList(
			Integer workItemID, int effortType, int computedValueType) {
		return loadByWorkItemsAndTypes(
				new int[] {workItemID}, effortType, computedValueType);
	}
	
	
	/**
	 * Saves a new/existing computedValuesBean in the TComputedValues table
	 * @param computedValuesBean
	 * @return the created optionID
	 */
	public static Integer save(TComputedValuesBean computedValuesBean) {
		return computedValuesDAO.save(computedValuesBean);
	}
	
	/**
	 * Deletes a record from the TComputedValues table
	 * @param objectID
	 */
	public static void delete(Integer objectID) {
		computedValuesDAO.delete(objectID);
	}
	
	/**
	 * Deletes all records from the TComputedValues table
	 * @param objectID
	 */
	public static void deleteAll() {
		computedValuesDAO.deleteAll();
	}
	
	/**
	 * Saves the budget bean in the TBudget table and actualizes the TComputedValues table
	 * @param costBean
	 */
	public static void computeBudgetOrPlan(TBudgetBean budgetBean) {
		if (budgetBean!=null) {
			Integer budgetType = budgetBean.getBudgetType();
			int computedValueType;
			if (budgetType==null || budgetType.intValue()==TBudgetBean.BUDGET_TYPE.PLANNED_VALUE) {
				computedValueType = TComputedValuesBean.COMPUTEDVALUETYPE.PLAN;
			} else {
				computedValueType = TComputedValuesBean.COMPUTEDVALUETYPE.BUDGET;
			}
			saveComputedValue(budgetBean.getWorkItemID(),
					TComputedValuesBean.EFFORTTYPE.TIME, computedValueType,
				null, budgetBean.getEstimatedHours(), budgetBean.getTimeUnit());
			saveComputedValue(budgetBean.getWorkItemID(),
					TComputedValuesBean.EFFORTTYPE.COST, computedValueType,
				null, budgetBean.getEstimatedCost(), null);
		}
	}
	
	/**
	 * Saves the cost bean in the TCost table and actualizes the TComputedValues table
	 * @param costBean
	 */
	public static void computeExpenses(Integer workItemID, Integer person) {
		computeTotalExpenses(workItemID);
		computePersonExpenses(workItemID, person);
	}
	
	/**
	 * Computes the total expenses for workItem
	 * @param workItemID
	 */
	public static void computeTotalExpenses(Integer workItemID) {
		//compute the time expenses for workItem
		computeAndSaveExpenseValue(workItemID, TComputedValuesBean.EFFORTTYPE.TIME,
				TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null);
		//compute the cost expenses for workItem
		computeAndSaveExpenseValue(workItemID, TComputedValuesBean.EFFORTTYPE.COST,
				TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null);
	}
	
	/**
	 * Computes the person expenses for workItem
	 * @param workItemID
	 * @param personID
	 */
	public static void computePersonExpenses(Integer workItemID, Integer personID) {
		//compute the time expenses for workItem and person
		computeAndSaveExpenseValue(workItemID, TComputedValuesBean.EFFORTTYPE.TIME,
				TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, personID);
		//compute the cost expenses for workItem and person
		computeAndSaveExpenseValue(workItemID, TComputedValuesBean.EFFORTTYPE.COST,
				TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, personID);
	}
	
	/**
	 * Saves the cost bean in the TCost table and actualizes the TComputedValues table
	 * @param costBean
	 */
	public static void deleteAndComputeExpense(Integer objectID) {
		TCostBean costBean = ExpenseBL.loadByPrimaryKey(objectID);
		if (costBean!=null) {
			Integer workItemID = costBean.getWorkItemID();
			costDAO.delete(objectID);
			ExpenseIndexer.getInstance().deleteByKey(objectID);
			//possible lucene update in other cluster nodes
			ClusterMarkChangesBL.markDirtyExpenseInCluster(objectID, CHANGE_TYPE.DELETE_FROM_INDEX);
			if (costBean.getHours()!=null) {
				//when time was added recalculate the total time for workItem and for person-workItem	
				computeAndSaveExpenseValue(workItemID, TComputedValuesBean.EFFORTTYPE.TIME,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null);
				computeAndSaveExpenseValue(workItemID, TComputedValuesBean.EFFORTTYPE.TIME,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, costBean.getPerson());
				
			}
			if (costBean.getCost()!=null) {
				//when cost was added recalculae the total time for workItem
				computeAndSaveExpenseValue(workItemID, TComputedValuesBean.EFFORTTYPE.COST,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null);
				computeAndSaveExpenseValue(workItemID, TComputedValuesBean.EFFORTTYPE.COST,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, costBean.getPerson());
			}
		}
	}
	
	/**
	 * Compute the expense value
	 * @param workItemID
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 */
	private static void computeAndSaveExpenseValue(Integer workItemID, 
			 int effortType, int computedValueType, Integer person) {
		double sum;
		Integer measurementUnit = null;
		boolean work = (effortType==TComputedValuesBean.EFFORTTYPE.TIME);
		if (work) {
			measurementUnit = TIMEUNITS.HOURS;
		}
		if (person==null) {
			sum = costDAO.getSumExpenseByWorkItem(workItemID, work);
		} else {
			sum = costDAO.getSumExpenseByWorkItemAndPersons(workItemID, new Integer[] { person }, work);
		}
		Double sumValue = null;
		if (!isZero(sum)) {
			sumValue = AccountingBL.roundToDecimalDigits(sum, work);
		}
		saveComputedValue(workItemID, effortType,
				TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE,
				person, sumValue, measurementUnit);
	}
	
	/**
	 * Saves the computed value
	 * A new bean will be created only if doesn't exists one already
	 * @param workItemID
	 * @param effortType
	 * @param computedValueType
	 * @param computedValue
	 */
	static void saveComputedValue(Integer workItemID, int effortType, int computedValueType, 
			Integer person,	Double computedValue, Integer measurementUnit) {
		//get the existing (if exists) to modify
		TComputedValuesBean computedValuesBean = loadByWorkItemAndTypesAndPerson(
				workItemID,	effortType, computedValueType, person);
		if (computedValue==null) {
			if (computedValuesBean==null) {
				//no previous computed value -> do not save it
				return;
			} else {
				//there is a previous computed value -> remove
				delete(computedValuesBean.getObjectID()); 
			}
		}
		if (computedValuesBean==null) {
			//otherwise create a new one now
			computedValuesBean = new TComputedValuesBean();
			computedValuesBean.setWorkitemKey(workItemID);
			computedValuesBean.setEffortType(Integer.valueOf(effortType));
			computedValuesBean.setComputedValueType(Integer.valueOf(computedValueType));
			computedValuesBean.setPerson(person);
		}
		computedValuesBean.setComputedValue(computedValue);
		computedValuesBean.setMeasurementUnit(measurementUnit);
		save(computedValuesBean);
	}
	
	/**
	 * Reset the calculated expenses to the direct expenses of the workItem
	 * @param workItemIDs
	 */
	public static void resetExpenseSumToDirect(List<Integer> workItemIDs) {
		List<TCostBean> costBeans = costDAO.loadSumExpensesForWorkItems(workItemIDs);
		if (costBeans!=null) {
			for (TCostBean costBean : costBeans) {
				Integer workItemID = costBean.getWorkItemID();
				saveComputedValue(workItemID, TComputedValuesBean.EFFORTTYPE.TIME,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null, costBean.getHours(), TIMEUNITS.HOURS);
				saveComputedValue(workItemID, TComputedValuesBean.EFFORTTYPE.COST,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null, costBean.getCost(), null);
			}
		}
		List<TCostBean> costBeansForPerson = costDAO.loadSumExpensesForWorkItemsAndPersons(workItemIDs);
		if (costBeansForPerson!=null) {
			for (TCostBean costBean : costBeansForPerson) {
				Integer workItemID = costBean.getWorkItemID();
				Integer personID = costBean.getPerson();
				saveComputedValue(workItemID, TComputedValuesBean.EFFORTTYPE.TIME,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, personID, costBean.getHours(), TIMEUNITS.HOURS);
				saveComputedValue(workItemID, TComputedValuesBean.EFFORTTYPE.COST,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, personID, costBean.getCost(), null);
			}
		}
	}
	
	/**
	 * Reset the calculated planned value to the last one from the history
	 * @param allBudgetsDescending
	 * @return
	 */
	public static void resetPlannedValuesToLastFromHistory(List<Integer> workItemIDs) {
		List<TBudgetBean> allBudgetsDescending = BudgetBL.loadLastPlanForWorkItems(workItemIDs);
		computePlannedValuesToLastFromHistory(allBudgetsDescending);
	}
	
	/**
	 * Reset the calculated planned value to the last one from the history
	 * @param allBudgetsDescending
	 * @return
	 */
	public static void computePlannedValuesToLastFromHistory(List<TBudgetBean> allBudgetsDescending) {
		TBudgetBean previousBudget = null;
		TBudgetBean nextBudget = null;
		if (allBudgetsDescending!=null) {
			Iterator<TBudgetBean> iterator = allBudgetsDescending.iterator();
			if (iterator.hasNext()) {
				previousBudget = iterator.next();
				saveComputedValue(previousBudget.getWorkItemID(), TComputedValuesBean.EFFORTTYPE.TIME, 
						TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, null, previousBudget.getEstimatedHours(), previousBudget.getTimeUnit());
				saveComputedValue(previousBudget.getWorkItemID(), TComputedValuesBean.EFFORTTYPE.COST, 
						TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, null, previousBudget.getEstimatedCost(), null);
				while (iterator.hasNext()) {
					nextBudget = iterator.next();
					//change to the next workItem
					if (!previousBudget.getWorkItemID().equals(nextBudget.getWorkItemID())) {
						saveComputedValue(nextBudget.getWorkItemID(), TComputedValuesBean.EFFORTTYPE.TIME, 
								TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, null, nextBudget.getEstimatedHours(), nextBudget.getTimeUnit());
						saveComputedValue(nextBudget.getWorkItemID(), TComputedValuesBean.EFFORTTYPE.COST, 
								TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, null, nextBudget.getEstimatedCost(), null);
					}
					previousBudget = nextBudget;
				}
			}
		}
	}
	
	/**
	 * Saves the computed value
	 * A new bean will be created only if doesn't exists one already
	 * @param workItemID
	 * @param effortType
	 * @param computedValueType
	 * @param computedValue
	 */
	static void deleteComputedValue(Integer workItemID, int effortType, int computedValueType, Integer person) {
		//get the existing (if exists) to modify
		TComputedValuesBean computedValuesBean = loadByWorkItemAndTypesAndPerson(
				workItemID,	effortType, computedValueType, person);
		if (computedValuesBean!=null) {
			delete(computedValuesBean.getObjectID());
		}
	}
	
	/**
	 * Once a planned value was changed on a leaf workItem or a new leaf workItem
	 * was created or copied or the parent was changed the ancestor planned values should be actualized
	 * @param parentID
	 * @param hoursPerWorkingDay
	 * @param forceRecompute cover the case then the plan/expense was set form a non null value to null
	 * (the ancestors contain the previous non-null values in their sum)
	 */
	public static synchronized void actualizeAncestorPlannedValuesOrExpenses(
			Integer workItemID, Integer parentID, int computedValueType,
			Integer personID, Double hoursPerWorkingDay) {
		
		//if (forceRecompute || computedWorkWorkExists || computedWorkCostExists) {
			//recalculate if forced or the actual workItem has any plan/expense
			Set<Integer> visistedAscendentsSet = new HashSet<Integer>();
			while (parentID!=null) {
				TWorkItemBean parentWorkItem = null;
				try {
					parentWorkItem = ItemBL.loadWorkItem(parentID);
				} catch (ItemLoaderException e) {
				}
				if (parentWorkItem!=null) {
					boolean ancestorTimeFound = false;
					//if (forceRecompute || computedWorkWorkExists) {
						ValueAndTimeUnitBean ancestorTimePlan = getSumOfDescendantBudgetOrPlanOrExpense(parentID,
								TComputedValuesBean.EFFORTTYPE.TIME, computedValueType, personID, hoursPerWorkingDay);
						if (ancestorTimePlan!=null) {
							ancestorTimeFound = true;
							saveComputedValue(parentID, TComputedValuesBean.EFFORTTYPE.TIME, computedValueType, personID,
								ancestorTimePlan.getDoubleValue(), ancestorTimePlan.getTimeUnit());
						}
					//}
					boolean ancestorCostFound = false;
					//if (forceRecompute || computedWorkCostExists) {
						ValueAndTimeUnitBean ancestorCostPlan = getSumOfDescendantBudgetOrPlanOrExpense(parentID,
								TComputedValuesBean.EFFORTTYPE.COST, computedValueType, personID, null);
						if (ancestorCostPlan!=null) {
							ancestorCostFound = true;
							saveComputedValue(parentID, TComputedValuesBean.EFFORTTYPE.COST, computedValueType, personID,
								ancestorCostPlan.getDoubleValue(), null);
						}
					//}
					//no descendants (last child was removed) -> workItem is leaf again 
					if (!ancestorTimeFound && !ancestorCostFound) {
						resetComputeForLeaf(parentID, computedValueType, personID);
					}
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
		//}
	}
	
	/**
	 * Recompute the plan or expenses based on the workItem's own values
	 * @param computedValueType
	 * @param workItemID
	 * @param personID
	 */
	static void resetComputeForLeaf(Integer workItemID, Integer computedValueType, Integer personID) {
		if (computedValueType==TComputedValuesBean.COMPUTEDVALUETYPE.PLAN) {
			TBudgetBean budgetBean = BudgetBL.loadLastBudgetOrPlanFromDb(workItemID, true);
			if (budgetBean!=null) {
				//restore the plan from the last budget from history if any
				ComputedValueBL.computeBudgetOrPlan(budgetBean);
			} else {
				//delete the computed value based on sum of descendants
				deleteComputedValue(workItemID, TComputedValuesBean.EFFORTTYPE.TIME, computedValueType, null);
				deleteComputedValue(workItemID, TComputedValuesBean.EFFORTTYPE.COST, computedValueType, null);
			}
		} else {
			if (computedValueType==TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE) {
				//recompute the expense sum by workitem's own expenses
				if (personID==null) {
					//total
					ComputedValueBL.computeTotalExpenses(workItemID);
				} else {
					//specific for person
					ComputedValueBL.computePersonExpenses(workItemID, personID);
				}
			}
		}
	}
	
	/**
	 * Gets the sum of descendants' budget/plan/expense. If the last child of the workItemID was just removed (is is leaf now) then return null
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
	public static ValueAndTimeUnitBean getSumOfDescendantBudgetOrPlanOrExpense(Integer workItemID,
			int effortType, int computedValueType, Integer personID, Double hoursPerWorkingDay) {
		//sum of time in targetTimeUnit or sum of costs
		LOGGER.debug("Compute effortType " + effortType + " and computedValueType " + computedValueType + " for workItem " + workItemID);
		double sum = 0.0;
		Integer commonTimeUnit = TIMEUNITS.WORKDAYS;
		List<TComputedValuesBean> allComputedValuesBean = new ArrayList<TComputedValuesBean>();
		Set<Integer> toRemoveSet = new HashSet<Integer>();
		toRemoveSet.add(workItemID);
		List<TWorkItemBean> childrenWorkItemBeans = ItemBL.getChildren(workItemID);
		if (childrenWorkItemBeans==null || childrenWorkItemBeans.isEmpty()) {
			return null;
		}
		while (childrenWorkItemBeans!=null && !childrenWorkItemBeans.isEmpty()) {
			Set<Integer> childIDSet = GeneralUtils.createIntegerSetFromBeanList(childrenWorkItemBeans);
			int[] childIDsArr =GeneralUtils.createIntArrFromSet(childIDSet);
			List<TComputedValuesBean> childComputedValuesList = null;
			childComputedValuesList = loadByWorkItemsAndTypesAndPerson(
						childIDsArr, effortType, computedValueType, personID);
			if (childComputedValuesList!=null && !childComputedValuesList.isEmpty()) {
				//temporary table, processed later
				allComputedValuesBean.addAll(childComputedValuesList);
				for (TComputedValuesBean computedValuesBean : childComputedValuesList) {
					Integer childID = computedValuesBean.getWorkitemKey();
					//nearest budgets/planned values found on the hierarchy: 
					//remove from set to not get the budget/plan of the children (do not count twice)
					childIDSet.remove(childID);
					if (effortType==TComputedValuesBean.EFFORTTYPE.TIME) {
						Integer timeUnitChild = computedValuesBean.getMeasurementUnit();
						if (timeUnitChild.intValue()==TIMEUNITS.HOURS) {
							commonTimeUnit = TIMEUNITS.HOURS;
						}
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
		for (TComputedValuesBean computedValuesBean : allComputedValuesBean) {
			Double computedValue = computedValuesBean.getComputedValue();
			if (computedValue!=null) {
				if (effortType!=TComputedValuesBean.EFFORTTYPE.TIME || TIMEUNITS.WORKDAYS.equals(commonTimeUnit)) {
					//compute either costs or times when all timeUnits are WORKDAYS
					sum+=computedValue;
				} else {
					//compute times with also HOURS timeUnits (eventually only HOURS)
					Integer timeUnit = computedValuesBean.getMeasurementUnit();
					if (timeUnit!=null && timeUnit.equals(TIMEUNITS.WORKDAYS)) {
						//transform WORKDAYS to HOURS 
						sum+=AccountingBL.transformToTimeUnits(computedValue, hoursPerWorkingDay, TIMEUNITS.WORKDAYS, TIMEUNITS.HOURS);
					} else {
						sum+=computedValue;
					}
				}
			}
		}
		ValueAndTimeUnitBean valueAndTimeUnitBean = new ValueAndTimeUnitBean();
		if (!isZero(sum)) {
			valueAndTimeUnitBean.setDoubleValue(sum);
		}
		valueAndTimeUnitBean.setTimeUnit(commonTimeUnit);
		return valueAndTimeUnitBean;
	}
	
	/**
	 * Compute the bottom up dates recursively starting from top parents 
	 * @param workItemBean
	 * @param parentToChildrenMap
	 * @param workItemBeansMap
	 * @param startDate
	 * @return
	 */
	public static ValueAndTimeUnitBean computeBottomUpValues(TWorkItemBean workItemBean, Map<Integer, List<Integer>> parentToChildrenMap,
			Map<Integer, TWorkItemBean> workItemBeansMap, int effortType, int computedValueType, Map<Integer, Double> hoursPerWorkdayForProject) {
		Integer workItemID = workItemBean.getObjectID();
		LOGGER.debug("Compute bottom up value for workItem " + workItemBean.getObjectID() +
				" effort type " + effortType + " computedValueType" + computedValueType );
		Integer projectID = workItemBean.getProjectID();
		List<Integer> childrenList = parentToChildrenMap.get(workItemID);
		if (childrenList==null) {
			//leaf workItem
			TComputedValuesBean computedValuesBean = null;
			if (computedValueType==TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE) {
				computedValuesBean = loadByWorkItemAndTypesAndPerson(workItemID, effortType, computedValueType, null);
			} else {
				computedValuesBean = loadByWorkItemAndTypes(workItemID, effortType, computedValueType);
			}
			return getChildValues(computedValuesBean);
		} else {
			//parent with children
			Integer commonDenominatorTimeUnit = TIMEUNITS.WORKDAYS;
			List<ValueAndTimeUnitBean> childValues = new LinkedList<ValueAndTimeUnitBean>();
			//compute the bottom up values for the children and add them in a temporary list 
			//in the same cycle compute the commonDenominatorTimeUnit: 
			for (Integer childID : childrenList) {
				TWorkItemBean childWorkItem = workItemBeansMap.get(childID);
				if (childWorkItem!=null) {
					ValueAndTimeUnitBean valueAndTimeUnitBeanChild = computeBottomUpValues(childWorkItem, parentToChildrenMap,
							workItemBeansMap, effortType, computedValueType, hoursPerWorkdayForProject);
					if (valueAndTimeUnitBeanChild!=null) {
						//add in a temporary list to compute it later once the commonDenominatorTimeUnit is known
						childValues.add(valueAndTimeUnitBeanChild);
						if (effortType==TComputedValuesBean.EFFORTTYPE.TIME) {
							Integer timeUnitChild = valueAndTimeUnitBeanChild.getTimeUnit();
							if (timeUnitChild!=null && TIMEUNITS.HOURS.equals(timeUnitChild)) {
								commonDenominatorTimeUnit = TIMEUNITS.HOURS;
							}
						}
					}
				}
			}
			return computeParentValues(childValues, effortType, computedValueType, commonDenominatorTimeUnit, workItemID, projectID, hoursPerWorkdayForProject, null);
		}
	}
	
	/**
	 * Gets the child computed values
	 * @param effortType
	 * @param computedValueType
	 * @param workItemID
	 * @param parentID
	 * @return
	 */
	private static ValueAndTimeUnitBean getChildValues(TComputedValuesBean computedValuesBean) {
		ValueAndTimeUnitBean valueAndTimeUnitBean = new ValueAndTimeUnitBean();
		if (computedValuesBean!=null) {
			Double value = computedValuesBean.getComputedValue();
			Integer effortType = computedValuesBean.getEffortType();
			if (value!=null) {
				valueAndTimeUnitBean.setDoubleValue(value);
			}
			Integer leafTimeUnit = null;
			if (effortType!=null && effortType.intValue()==TComputedValuesBean.EFFORTTYPE.TIME) {
				leafTimeUnit = computedValuesBean.getMeasurementUnit();
				if (leafTimeUnit==null) {
					leafTimeUnit = TIMEUNITS.HOURS;
				}
				valueAndTimeUnitBean.setTimeUnit(leafTimeUnit);
			}
		}
		return valueAndTimeUnitBean;
	}
	/**
	 * 
	 * @param childValues
	 * @param effortType
	 * @param computedValueType
	 * @param commonTimeUnit
	 * @param workItemID
	 * @param projectID
	 * @param hoursPerWorkdayForProject
	 * @return
	 */
	private static ValueAndTimeUnitBean computeParentValues(List<ValueAndTimeUnitBean> childValues,
			Integer effortType, Integer computedValueType, Integer commonTimeUnit,Integer workItemID, 
			Integer projectID, Map<Integer, Double> hoursPerWorkdayForProject, Integer personID) {
		ValueAndTimeUnitBean valueAndTimeUnitBean = new ValueAndTimeUnitBean();
		double childenPlanSum = 0.0;
		if (!childValues.isEmpty()) {
			//at least one child found with planned value set
			for (ValueAndTimeUnitBean valueAndTimeUnitBeanChild : childValues) {
				Double childValue = valueAndTimeUnitBeanChild.getDoubleValue();
				if (childValue!=null) {
					if (effortType!=TComputedValuesBean.EFFORTTYPE.TIME || TIMEUNITS.WORKDAYS.equals(commonTimeUnit)) {
						//compute either costs or times when all timeUnits are WORKDAYS
						childenPlanSum+=childValue.doubleValue();
					} else {
						//compute times with also HOURS timeUnits (eventually only HOURS)
						if (TIMEUNITS.WORKDAYS.equals(valueAndTimeUnitBeanChild.getTimeUnit())) {
							Double hoursPerWorkingDay = hoursPerWorkdayForProject.get(projectID);
							if (hoursPerWorkingDay==null) {
								hoursPerWorkingDay = ProjectBL.getHoursPerWorkingDay(projectID);
								hoursPerWorkdayForProject.put(projectID, hoursPerWorkingDay);
							}
							//transform WORKDAYS to HOURS 
							childenPlanSum+=AccountingBL.transformToTimeUnits(childValue, hoursPerWorkingDay, TIMEUNITS.WORKDAYS, TIMEUNITS.HOURS);
						} else {
							childenPlanSum+=childValue.doubleValue();
						}
					}
				}
			}
			//save it locally for the actual ascendent workItem
			Integer measurementUnit = null; 
			if (effortType==TComputedValuesBean.EFFORTTYPE.TIME) {
				measurementUnit = commonTimeUnit;
			}
			Double sum = null;
			if (!isZero(childenPlanSum)) {
				sum = Double.valueOf(childenPlanSum);
			}
			saveComputedValue(workItemID, effortType, computedValueType, personID,
					sum, measurementUnit);
			valueAndTimeUnitBean.setDoubleValue(childenPlanSum);
			if (effortType==TComputedValuesBean.EFFORTTYPE.TIME) {
				valueAndTimeUnitBean.setTimeUnit(commonTimeUnit);
			}
		}
		return valueAndTimeUnitBean;
	}
	
	/**
	 * Compute the bottom up dates recursively starting from top parents 
	 * @param workItemBean
	 * @param parentToChildrenMap
	 * @param workItemBeansMap
	 * @param startDate
	 * @return
	 */
	public static Map<Integer, ValueAndTimeUnitBean> computeBottomUpPersonValues(TWorkItemBean workItemBean, Map<Integer, List<Integer>> parentToChildrenMap,
			Map<Integer, TWorkItemBean> workItemBeansMap, int effortType, int computedValueType, Map<Integer, Double> hoursPerWorkdayForProject) {
		Integer workItemID = workItemBean.getObjectID();
		LOGGER.debug("Compute bottom up value for workItem " + workItemBean.getObjectID() +
				" effort type " + effortType + " computedValueType" + computedValueType );
		Integer projectID = workItemBean.getProjectID();
		Map<Integer, ValueAndTimeUnitBean> valuesForPersons = new HashMap<Integer, ValueAndTimeUnitBean>();
		List<Integer> childrenList = parentToChildrenMap.get(workItemID);
		if (childrenList==null) {
			//leaf workItem
			List<TComputedValuesBean> computedValuesBeans = loadByWorkItemAndTypesList(workItemID, effortType, computedValueType);
			if (computedValuesBeans!=null) {
				for (TComputedValuesBean computedValuesBean : computedValuesBeans) {
					Integer personID = computedValuesBean.getPerson();
					if (personID!=null) {
						ValueAndTimeUnitBean valueAndTimeUnitBean = getChildValues(computedValuesBean);
						valuesForPersons.put(personID, valueAndTimeUnitBean);
					}
				}
			}
			return valuesForPersons;
		} else {
			//parent with children
			Map<Integer, Integer> commonDenominatorTimeUnitsForPersons = new HashMap<Integer, Integer>();
			Map<Integer, List<ValueAndTimeUnitBean>> childWorkItemValuesProPerson = new HashMap<Integer, List<ValueAndTimeUnitBean>>();
			//compute the bottom up values for the children and add them in a temporary list 
			//in the same cycle compute the commonDenominatorTimeUnit: 
			for (Integer childID : childrenList) {
				TWorkItemBean childWorkItem = workItemBeansMap.get(childID);
				//ValueAndTimeUnitBean valueAndTimeUnitBeanChild = null;
				if (childWorkItem!=null) {
					Map<Integer, ValueAndTimeUnitBean> branchValuesForPersons = computeBottomUpPersonValues(childWorkItem, parentToChildrenMap,
							workItemBeansMap, effortType, computedValueType, hoursPerWorkdayForProject);
					if (branchValuesForPersons!=null) {
						//add in a temporary list to compute it later once the commonDenominatorTimeUnit is known
						for (Integer personID : branchValuesForPersons.keySet()) {
							ValueAndTimeUnitBean valueAndTimeUnitBean = branchValuesForPersons.get(personID);
							List<ValueAndTimeUnitBean> childWorkItemsForPerson = childWorkItemValuesProPerson.get(personID);
							if (childWorkItemsForPerson==null) {
								childWorkItemsForPerson = new LinkedList<ComputedValueBL.ValueAndTimeUnitBean>();
								childWorkItemValuesProPerson.put(personID, childWorkItemsForPerson);
							}
							childWorkItemsForPerson.add(valueAndTimeUnitBean);
							
							Integer timeUnitChild = valueAndTimeUnitBean.getTimeUnit();
							if (timeUnitChild!=null) {
								Integer commonDenominatorTimeUnit = commonDenominatorTimeUnitsForPersons.get(personID);
								if (commonDenominatorTimeUnit==null) {
									commonDenominatorTimeUnitsForPersons.put(personID, timeUnitChild);
								} else {
									if (!timeUnitChild.equals(commonDenominatorTimeUnit)) {
										commonDenominatorTimeUnitsForPersons.put(personID, TIMEUNITS.HOURS);
									}
								}
							}
						}
					}
				}
			}
			//sum up the children in an additional cycle once the commonDenominatorTimeUnit is known
			
			if (!childWorkItemValuesProPerson.isEmpty()) {
				//at least one child found with planned value set
				for (Integer personID : childWorkItemValuesProPerson.keySet()) {
					List<ValueAndTimeUnitBean> childPlans = childWorkItemValuesProPerson.get(personID);
					Integer commonDenominatorTimeUnit = commonDenominatorTimeUnitsForPersons.get(personID);
					ValueAndTimeUnitBean valueAndTimeUnitBean = computeParentValues(childPlans, effortType, computedValueType,
							commonDenominatorTimeUnit, workItemID, projectID, hoursPerWorkdayForProject, personID);
					valuesForPersons.put(personID, valueAndTimeUnitBean);
				}
				//save it locally for the actual ascendent workItem
				
			}
			return valuesForPersons;
		}
	}
	
	private static boolean isZero(double val) {
		if(Double.doubleToRawLongBits(val) == 0) { 
			return true;
		}
		return false;
	}
}
