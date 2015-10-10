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


package com.aurel.track.persist;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.TreeFilterCriteria;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.dao.BudgetDAO;
import com.aurel.track.util.GeneralUtils;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Mon Mar 27 12:21:16 EEST 2006]
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TBudgetPeer
	extends com.aurel.track.persist.BaseTBudgetPeer
	implements BudgetDAO
{
	
	private static final long serialVersionUID = 8596279112821322886L;
	
	private static final Logger LOGGER = LogManager.getLogger(TBudgetPeer.class);
	
	/**
	 * Adds a plan filter
	 * @param crit
	 * @param plan
	 */
	private static void addPlanCondition(Criteria crit, boolean plan) {
		if (plan) {
			Criterion criterionPlannedValue = crit.getNewCriterion(BUDGETTYPE, TBudgetBean.BUDGET_TYPE.PLANNED_VALUE, Criteria.EQUAL);
			Criterion criterionNoBudgetType = crit.getNewCriterion(BUDGETTYPE, null, Criteria.ISNULL);
			crit.add(criterionPlannedValue.or(criterionNoBudgetType));
		} else {
			crit.add(BUDGETTYPE, TBudgetBean.BUDGET_TYPE.BUDGET);
		}
	}
	
	/**
	 * Loads the last BudgetBean by workItemKey 
	 * @param workItemKey
	 * @param plan
	 * @return
	 */
	public TBudgetBean loadLastByWorkItem(Integer workItemKey, boolean plan) {
		if (workItemKey==null) {
			return null;
		}
		TBudget budget = null;
		Criteria crit = new Criteria();
		crit.add(WORKITEMKEY, workItemKey);
		addPlanCondition(crit, plan);
		crit.addDescendingOrderByColumn(LASTEDIT);
		try {
			List<TBudget> list = doSelect(crit);
			if (list!=null && !list.isEmpty()) {
				budget = list.get(0);
			
			}
		} catch(Exception e) {
			LOGGER.error("Loading the last budget by workItemKey " + workItemKey + " failed with " + e);
		}
		if (budget!=null) {
			return budget.getBean();
		}
		return null;
	}

	
	/**
	 * Load all budgets without budgetType
	 * @return
	 */
	public void updateWithoutBudgetType() {
		Criteria selectCriteria = new Criteria();
		selectCriteria.add(BUDGETTYPE, (Object)null, Criteria.ISNULL);
		Criteria updateCriteria = new Criteria();
		updateCriteria.add(BUDGETTYPE, TBudgetBean.BUDGET_TYPE.PLANNED_VALUE);
		
		try {
			doUpdate(selectCriteria, updateCriteria);
		} catch (TorqueException e) {
			LOGGER.error("Updating the existing budget records to PLANNED_VALUE type failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * Loads a BudgetBean list by workItemKeys and person
	 * @param workItemKeys
	 * @param changedByPersons if null no not filter by personID
	 * @param plan
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<TBudgetBean> loadByWorkItemKeys(int[] workItemKeys, List<Integer> changedByPersons, Boolean plan, Date fromDate, Date toDate) {
		List<TBudgetBean> list = new LinkedList<TBudgetBean>();
		if (workItemKeys==null || workItemKeys.length==0) {
			return list;
		}
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemKeys);
		if (workItemIDChunksList==null) {
			return list;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = (int[])iterator.next();
			Criteria crit = new Criteria();
			crit.addIn(WORKITEMKEY, workItemIDChunk);
			if (changedByPersons!=null && !changedByPersons.isEmpty()) {
				crit.addIn(CHANGEDBY, changedByPersons);
			}
			if (fromDate!=null && toDate!=null) {
				Criterion criterionFrom = crit.getNewCriterion(LASTEDIT, fromDate, Criteria.GREATER_EQUAL);
				Criterion criterionTo = crit.getNewCriterion(LASTEDIT, toDate, Criteria.LESS_THAN);
				crit.add(criterionFrom.and(criterionTo));
			} else {
				if (fromDate!=null) {
					crit.add(LASTEDIT, fromDate, Criteria.GREATER_EQUAL);
				} else {
					if (toDate!=null) {
						crit.add(LASTEDIT, toDate, Criteria.LESS_THAN);
					}
				}
			}
			if (plan!=null) {
				addPlanCondition(crit, plan.booleanValue());
			}
			crit.addDescendingOrderByColumn(WORKITEMKEY);
			crit.addDescendingOrderByColumn(LASTEDIT);
			try {
				list.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (TorqueException e) {
				LOGGER.error("Getting the bugets for workItems failed with " + e.getMessage(), e);
			}
		}
		return list;
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
	public List<TBudgetBean> loadloadActivityStreamBugetsPlans(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID,
			Integer limit, Date fromDate, Date toDate, List<Integer> changedByPersons, Boolean plan) {
		Integer[] selectedProjects = filterUpperTO.getSelectedProjects();
		if (selectedProjects==null  || selectedProjects.length==0) {
			//at least one selected project needed
			return new ArrayList<TBudgetBean>();
		}		
		Criteria crit = TreeFilterCriteria.prepareTreeFilterCriteria(filterUpperTO, raciBean, personID);
		if (plan!=null) {
			addPlanCondition(crit, plan.booleanValue());
		}
		addActivityStreamCriteria(crit, limit, fromDate, toDate, changedByPersons);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the buget/pla by tree filter, fromDate " + fromDate + " toDate " + toDate +
					" plan " + plan + " persons " + changedByPersons +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Prepare the activity stream criteria
	 * @param limit
	 * @param fromDate
	 * @param toDate
	 * @param changeTypes
	 * @param changedByPersons
	 * @return
	 */
	private Criteria addActivityStreamCriteria(Criteria criteria, Integer limit,
			Date fromDate, Date toDate, List<Integer> changedByPersons) {
		if (limit!=null){
			criteria.setLimit(limit);
		}
		criteria.addJoin(TWorkItemPeer.WORKITEMKEY,  WORKITEMKEY);
		if (changedByPersons!=null && !changedByPersons.isEmpty()) {
			criteria.addIn(CHANGEDBY, changedByPersons);
		}
		if (fromDate!=null && toDate!=null) {
			Criterion critMinDate = criteria.getNewCriterion(LASTEDIT , fromDate, Criteria.GREATER_EQUAL);
			Criterion critMaxDate = criteria.getNewCriterion(LASTEDIT , toDate, Criteria.LESS_EQUAL);
			criteria.add(critMinDate.and(critMaxDate));
		} else {
			if (fromDate!=null) {
				criteria.add(LASTEDIT, fromDate, Criteria.GREATER_EQUAL);
			} else {
				if (toDate!=null) {
					criteria.add(LASTEDIT, toDate, Criteria.LESS_EQUAL);
				}
			}
		}
		criteria.addAscendingOrderByColumn(WORKITEMKEY);
		criteria.addAscendingOrderByColumn(BUDGETTYPE);
		criteria.addAscendingOrderByColumn(LASTEDIT);
		return criteria;
	}
	
	/**
	 * Saves a BudgetBean in the TBudget table
	 * @param budgetBean
	 * @return
	 */
	public Integer save(TBudgetBean budgetBean) {
		try {
			TBudget tBudget = BaseTBudget.createTBudget(budgetBean);
			tBudget.save();
			Integer budgetID = tBudget.getObjectID();
			budgetBean.setObjectID(budgetID);
			return budgetID;
		} catch (Exception e) {
			LOGGER.error("Saving of a budget failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Get the budget and planned value history for an item
	 * @param workItemID
	 * @param budgetType
	 * @return
	 */
	public List<TBudgetBean> getByWorkItem(Integer workItemID, Integer budgetType) {
		Criteria crit = new Criteria();
		crit.add(WORKITEMKEY, workItemID);
		if (budgetType!=null) {
			if (TBudgetBean.BUDGET_TYPE.PLANNED_VALUE.equals(budgetType)) {
				Criterion criterionPlannedValue = crit.getNewCriterion(BUDGETTYPE, TBudgetBean.BUDGET_TYPE.PLANNED_VALUE, Criteria.EQUAL);
				Criterion criterionNoBudgetType = crit.getNewCriterion(BUDGETTYPE, null, Criteria.ISNULL);
				crit.add(criterionPlannedValue.or(criterionNoBudgetType));
			} else {
				crit.add(BUDGETTYPE, TBudgetBean.BUDGET_TYPE.BUDGET);
			}
		} 
		crit.addAscendingOrderByColumn(LASTEDIT);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the budget/plan history for workItem " + workItemID + " failed with " + e.getMessage(), e);
			return null;
		}
	
	}
	
	/**
	 * Gets a list of TBudgetBeans with the last plan value for each workItem
	 * @return
	 */
	public List<TBudgetBean> loadLastPlanForWorkItems() {
		Criteria crit = new Criteria();
		crit.addAscendingOrderByColumn(WORKITEMKEY);
		crit.addDescendingOrderByColumn(LASTEDIT);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all budgets in descending order failed with " + e);
			return null;
		}
	}
	
	/**
	 * Gets a list of TBudgetBeans with the last plan value for each workItem
	 * @param workItemIDs
	 * @return
	 */
	public List<TBudgetBean> loadLastPlanForWorkItems(List<Integer> workItemIDs) {
		List<TBudgetBean> budgetBeanList = new LinkedList<TBudgetBean>();
		if (workItemIDs==null || workItemIDs.isEmpty()) {
			return budgetBeanList;
		}
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return budgetBeanList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			Criteria crit = new Criteria();
			crit.addIn(WORKITEMKEY, workItemIDChunk);
			addPlanCondition(crit, true);
			crit.addAscendingOrderByColumn(WORKITEMKEY);
			crit.addDescendingOrderByColumn(LASTEDIT);
			try {
				budgetBeanList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch(Exception e) {
				LOGGER.error("Loading all budgets in descending order failed with " + e);
				return null;
			}
		}
		return budgetBeanList;
	}
	
	/**
	 * Gets a list of TBudgetBeans by IDs
	 * @param objectIDs
	 * @return
	 */
	public List<TBudgetBean> getByIDs(List<Integer> objectIDs) {
		List<TBudgetBean> budgetBeanList = new LinkedList<TBudgetBean>();
		if (objectIDs==null || objectIDs.isEmpty()) {
			return budgetBeanList;
		}
		List<int[]> objectIDChunksList = GeneralUtils.getListOfChunks(objectIDs);
		if (objectIDChunksList==null) {
			return budgetBeanList;
		}
		Iterator<int[]> iterator = objectIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] objectIDChunk = iterator.next();
			Criteria crit = new Criteria();
			crit.addIn(OBJECTID, objectIDChunk);
			try {
				budgetBeanList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch(Exception e) {
				LOGGER.error("Loading all budgets by ID failed with " + e);
				return null;
			}
		}
		return budgetBeanList;
	}
	
	/**
	 * Gets all indexable budgetBeans
	 * @return
	 */
	public List<TBudgetBean> loadAllIndexable() {
		Criteria crit = new Criteria();
		Criterion emptyDescriptionCriterion = crit.getNewCriterion(CHANGEDESCRIPTION, "", Criteria.NOT_EQUAL);
		Criterion nullDescriptionCriterion = crit.getNewCriterion(CHANGEDESCRIPTION, (Object)null, Criteria.ISNOTNULL);
		crit.add(emptyDescriptionCriterion.and(nullDescriptionCriterion));
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all indexable costs failed with " + e);
			return null;
		}
	}
	
	 private static List<TBudgetBean> convertTorqueListToBeanList(List<TBudget> torqueList) {
		List<TBudgetBean> beanList = new ArrayList<TBudgetBean>();
		if (torqueList!=null){
			Iterator<TBudget> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				TBudgetBean bean=itrTorqueList.next().getBean();
				 beanList.add(bean);
			}
		}
		return beanList;
	}
	
}