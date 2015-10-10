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

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterBL.CHANGE_TYPE;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.BudgetDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.item.ItemBL;
import com.aurel.track.lucene.index.associatedFields.BudgetPlanIndexer;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.IEventSubscriber;
import com.aurel.track.util.event.parameters.AfterBudgetExpenseChangeEventParam;

public class BudgetBL {
	private static BudgetDAO budgetDAO = DAOFactory.getFactory().getBudgetDAO();
	
	public static Integer saveBudgetBean(TBudgetBean budgetBean) {
		boolean isNew = budgetBean.getObjectID()==null;
		Integer objectID = budgetDAO.save(budgetBean);
		BudgetPlanIndexer.getInstance().addToIndex(budgetBean, isNew);
		//possible lucene update in other cluster nodes
		ClusterMarkChangesBL.markDirtyBudgetPlanInCluster(objectID, CHANGE_TYPE.ADD_TO_INDEX);
		return objectID;
	}
	
	/**
	 * Gets the budgets/plans by tree filter for activity stream
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param limit
	 * @param fromDate
	 * @param toDate
	 * @param changedByPersons
	 * @param plan
	 * @return
	 */
	public static List<TBudgetBean> loadActivityStreamBugetsPlans(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID,
			Integer limit, Date fromDate, Date toDate, List<Integer> changedByPersons, Boolean plan) {
		List<TBudgetBean> budgetBeanList = budgetDAO.loadloadActivityStreamBugetsPlans(filterUpperTO, raciBean, personID, limit, fromDate, toDate, changedByPersons, plan);
		Set<Integer> workItemIDsSet = new HashSet<Integer>();
		for (TBudgetBean budgetBean : budgetBeanList) {
			workItemIDsSet.add(budgetBean.getWorkItemID());
		}
		List<TWorkItemBean> workItemsWithBudget = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(workItemIDsSet));
		Map<Integer, TWorkItemBean> workItemBeansMap = GeneralUtils.createMapFromList(workItemsWithBudget);
		AccessBeans.filterBudgetBeans(budgetBeanList, personID, workItemBeansMap);
		return budgetBeanList;
	}
	
	/**
	 * Get the budget and planned value history for an item
	 * @param workItemID
	 * @param budgetType
	 * @return
	 */
	public static List<TBudgetBean> getByWorkItem(Integer workItemID, Integer budgetType) {
		return budgetDAO.getByWorkItem(workItemID, budgetType);
	}
	
	/**
	 * Loads the budget from the database
	 * @param effortID
	 */
	public static TBudgetBean loadLastBudgetOrPlanFromDb(Integer workItemID, boolean plan) {
		return budgetDAO.loadLastByWorkItem(workItemID, plan);
	}
	
	/**
	 * Gets a list of TBudgetBeans with the last plan value for each workItem
	 * @return
	 */
	public static List<TBudgetBean> loadLastPlanForWorkItems() {
		return budgetDAO.loadLastPlanForWorkItems();
	}
	
	/**
	 * Gets a list of TBudgetBeans with the last plan value for each workItem
	 * @param workItemIDs
	 * @return
	 */
	public static List<TBudgetBean> loadLastPlanForWorkItems(List<Integer> workItemIDs) {
		return budgetDAO.loadLastPlanForWorkItems(workItemIDs);
	}
	
	/**
	 * Gets a list of TBudgetBeans by IDs
	 * @param objectIDs
	 * @return
	 */
	public static List<TBudgetBean> getByIDs(List<Integer> objectIDs) {
		return budgetDAO.getByIDs(objectIDs);
	}
	
	/**
	 * Gets all indexable budgetBeans
	 * @return
	 */
	public static List<TBudgetBean> loadAllIndexable() {
		return budgetDAO.loadAllIndexable();
	}
	
	/**
	 * Loads a BudgetBean list by workItemKeys
	 * @param workItemKeys
	 * @param changedByPersons
	 * @param plan
	 * @param fromDate
	 * @param toDate	
	 * @return
	 */
	public static List<TBudgetBean> loadByWorkItemKeys(int[] workItemKeys, List<Integer> changedByPersons, Boolean plan, Date fromDate, Date toDate) {
		return budgetDAO.loadByWorkItemKeys(workItemKeys, changedByPersons, plan, fromDate, toDate);
	}
	
	
	/**
	 * Gets the budget type
	 * @param plannedValue
	 * @return
	 */
	public static Integer getBudgetType(boolean plannedValue) {
		if (plannedValue) {
			return TBudgetBean.BUDGET_TYPE.PLANNED_VALUE;
		} else {
			return TBudgetBean.BUDGET_TYPE.BUDGET;
		}
	}
	
	/**
	 * Save the total budget into the database
	 * @param accountingForm
	 * @param personBean
	 * @param workItemBean
	 * @param project
	 * @param notify 	true	-	send notification event
	 * 					false 	-	do not send notification event	
	 * @return whether this budget setting is the very first one (in this case and 
	 * 			only in this case the estimated remaining budget will be recalculated)
	 */
	public static boolean saveBudgetOrPlanToDb(TBudgetBean budgetBeanNew, TBudgetBean budgetBeanOld,
			TPersonBean personBean, boolean plan, TWorkItemBean workItemBean, boolean notify) {
		Integer workItemKey = workItemBean.getObjectID();
		if (budgetBeanOld==null || budgetBeanNew.hasChanged(budgetBeanOld)) {
			if (personBean!=null && personBean.getObjectID()!=null) {
				budgetBeanNew.setChangedByID(personBean.getObjectID());
			}
			budgetBeanNew.setLastEdit(new Date());
			budgetBeanNew.setWorkItemID(workItemKey);
			budgetBeanNew.setBudgetType(getBudgetType(plan));
			//new entry saved as new because it is historized
			saveBudgetBean(budgetBeanNew);
			ComputedValueBL.computeBudgetOrPlan(budgetBeanNew);
			if (notify) {
				notifyBudgetPlanChange(personBean, workItemBean, budgetBeanOld, budgetBeanNew, plan);
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Loads the budget from the database for a period of time
	 * Important is that at database level there is a descending ordering by lastEdit
	 * @param workItemIDs
	 * @param changedByPersons
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static SortedMap<Integer, TBudgetBean> loadLastBudgetForWorkItemsMap(int workItemIDs[], List<Integer> changedByPersons, Boolean plan, Date fromDate, Date toDate) {
		List<TBudgetBean> budgetBeans = loadByWorkItemKeys(workItemIDs, changedByPersons, plan, fromDate, toDate);
		SortedMap<Integer, TBudgetBean> lastBudgetForWorkItemsMap = new TreeMap<Integer, TBudgetBean>();
		//the TBudgetBeans are already sorted in database query in descending order!!!
		if (budgetBeans!=null) {
			for (TBudgetBean budgetBean : budgetBeans) {
				Integer workItemID = budgetBean.getWorkItemID();
				if (!lastBudgetForWorkItemsMap.containsKey(workItemID)) {
					lastBudgetForWorkItemsMap.put(workItemID, budgetBean);
				}
			}
		}
		return lastBudgetForWorkItemsMap;
	}
	
	/**
	 * Notify about budget change
	 * @param personBean
	 * @param workItemBean
	 * @param budgetBeanOld
	 * @param budgetBeanNew
	 * @param plan
	 */
	public static void notifyBudgetPlanChange(TPersonBean personBean, TWorkItemBean workItemBean,
			TBudgetBean budgetBeanOld, TBudgetBean budgetBeanNew, boolean plan) {
		//send notification mail after saving a budget or plan
		AfterBudgetExpenseChangeEventParam afterBudgetExpenseChangeEventParam = new AfterBudgetExpenseChangeEventParam();
		if (plan) {
			afterBudgetExpenseChangeEventParam.setNewPlannedValue(budgetBeanNew);
			afterBudgetExpenseChangeEventParam.setOldPlannedValue(budgetBeanOld);
		} else {
			afterBudgetExpenseChangeEventParam.setNewBudget(budgetBeanNew);
			afterBudgetExpenseChangeEventParam.setOldBudget(budgetBeanOld);
		}
		afterBudgetExpenseChangeEventParam.setWorkItemBean(workItemBean);
		afterBudgetExpenseChangeEventParam.setPersonBean(personBean);
		EventPublisher evp = EventPublisher.getInstance();
		if (evp != null) {
			List<Integer> events = new LinkedList<Integer>();
			if (plan) {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_UPDATEPLANNEDVALUE));
			} else {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_UPDATEBUDGET));
			}
			evp.notify(events, afterBudgetExpenseChangeEventParam);
		}
	}
}
