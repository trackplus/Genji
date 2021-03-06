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

package com.aurel.track.persist;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.TreeFilterCriteria;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.dao.CostDAO;
import com.aurel.track.util.GeneralUtils;
import com.workingdogs.village.Record;
import com.workingdogs.village.Value;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Tue Jun 15 21:31:34 CEST 2004]
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TCostPeer
	extends com.aurel.track.persist.BaseTCostPeer
	implements CostDAO
{

	private static final long serialVersionUID = 5992400714623483773L;
	private static final Logger LOGGER = LogManager.getLogger(TCostPeer.class);
	
	/**
	 * Loads an expense by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TCostBean loadByPrimaryKey(Integer objectID) {
		TCost tCost = null;
		try {
			tCost = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.warn("Loading of an expense by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tCost!=null) {
			return tCost.getBean();
		}
		return null;
	}
	
	/**
	 * Gets all costs/efforts  
	 * @return
	 */
	@Override
	public List<TCostBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all costs failed with " + e);
			return null;
		}
	}
	
	/**
	 * Gets all costs/efforts  
	 * @return
	 */
	@Override
	public List<TCostBean> loadAllIndexable() {
		Criteria crit = new Criteria();
		Criterion emptyDescriptionCriterion = crit.getNewCriterion(DESCRIPTION, "", Criteria.NOT_EQUAL);
		Criterion nullDescriptionCriterion = crit.getNewCriterion(DESCRIPTION, (Object)null, Criteria.ISNOTNULL);
		Criterion descriptionCriterion = emptyDescriptionCriterion.and(nullDescriptionCriterion); 
		Criterion emptySubjectCriterion = crit.getNewCriterion(SUBJECT, "", Criteria.NOT_EQUAL);
		Criterion nullSubjectCriterion = crit.getNewCriterion(SUBJECT, (Object)null, Criteria.ISNOTNULL);
		Criterion subjectCriterion = emptySubjectCriterion.and(nullSubjectCriterion);
		crit.add(descriptionCriterion.or(subjectCriterion));
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all indexable costs failed with " + e);
			return null;
		}
	}
	
	
	/**
	 * Saves a CostBean in the TCost table
	 * @param costBean
	 * @return
	 */
	@Override
	public Integer save(TCostBean costBean) {
		try {
			TCost tCost = BaseTCost.createTCost(costBean);
			tCost.save();
			Integer objectID = tCost.getObjectID();
			costBean.setObjectID(objectID);
			return objectID;
		} catch (Exception e) {
			LOGGER.error("Saving of a cost failed with " + e.getMessage());
			return null;
		}	
	}
	
	/**
	 * Deletes a cost by primary key
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting an expense for key " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Get the total work or total cost expense for a workItem 
	 * @param workItemKey
	 * @param work work or cost
	 * @return
	 */
	@Override
	public double getSumExpenseByWorkItem(Integer workItemKey, boolean work) {
		double effort = 0.0;
		String fieldName = null;
		if (work) {
			fieldName = HOURS;
		} else {
			fieldName = COST;
		}
		try {
			Criteria criteria = new Criteria();
			criteria.add(WORKITEM, workItemKey);
			criteria.addSelectColumn("SUM(" + fieldName + ")");
			List<Record> result = doSelectVillageRecords(criteria);
			if (result!=null && !result.isEmpty()) {
				Record record = (Record) result.get(0);
				effort = record.getValue(1).asDouble();
			}
		} catch(Exception e) {
			LOGGER.error("Calculating the total expenses by workItemKey " +
					workItemKey + " and work " + work +" failed with " + e);
		}
		return effort;
	}
	
	
	/**
	 * Gets the total work or total cost expenses for a workItem booked by a list of persons
	 * @param workItemKey
	 * @param persons
	 * @param work work or cost
	 * @return
	 */
	@Override
	public double getSumExpenseByWorkItemAndPersons(Integer workItemKey, Integer[] persons, boolean work) {
		double effort = 0.0;
		if (persons!=null && persons.length>0) {
			String fieldName = null;
			if (work) {
				fieldName = HOURS;
			} else {
				fieldName = COST;
			}
			try {
				Criteria criteria = new Criteria();
				criteria.add(WORKITEM, workItemKey);
				criteria.addIn(PERSON, persons);
				criteria.addSelectColumn("SUM(" + fieldName + ")");
				List<Record> result = doSelectVillageRecords(criteria);
				if (result!=null && !result.isEmpty()) {
					Record record = (Record) result.get(0);
					effort = record.getValue(1).asDouble();
				}
			} catch(Exception e) {
				LOGGER.error("Calculating the total expenses by workItemKey " + workItemKey +
						" persons " + persons.length + " and work " + work + " failed with " + e);
			}
		}
		return effort;
	}
	
	/**
	 * Loads the cost beans by objectIDs
	 * @param objectIDs
	 * @return
	 */
	@Override
	public List<TCostBean> loadByKeys(List<Integer> objectIDs) {
		List<TCostBean> costList = new LinkedList<TCostBean>();
		if (objectIDs==null || objectIDs.isEmpty()) {
			return costList;
		}
		List<int[]> objectIDChunksList = GeneralUtils.getListOfChunks(objectIDs);
		if (objectIDChunksList==null) {
			return costList;
		}		
		Iterator<int[]> iterator = objectIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] objectIDChunk = iterator.next();
			Criteria crit = new Criteria();
			crit.addIn(OBJECTID, objectIDChunk);
			try {
				costList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (TorqueException e) {
				LOGGER.error("Getting the costs for objectIDs failed with " + e.getMessage());
			}
		}		
		return costList;
	}
	
	/**
	 * Loads a the CostBeans by workItemKeys and person
	 * Loads all costs independently whether the user has or not "view all expenses" role
	 * The list will be filtered out later on because it is too costly to filter them for eack workItem 
	 * @param workItemKeys
	 * @param personIDsArr if null no not filter by person
	 * @param fromDate
	 * @param toDate
	 * @param accounts
	 * @param ascendingDate
	 * @return
	 */
	@Override
	public List<TCostBean> loadByWorkItemKeys(int[] workItemKeys, Integer[] personIDsArr, 
			Date fromDate, Date toDate, List<Integer> accounts,  boolean ascendingDate) {
		List<TCostBean> costList = new LinkedList<TCostBean>();
		if (workItemKeys==null || workItemKeys.length==0) {
			return costList;
		}
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemKeys);
		if (workItemIDChunksList==null) {
			return costList;
		}		
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			Criteria crit = new Criteria();
			crit.addIn(WORKITEM, workItemIDChunk);
			if (accounts!=null && !accounts.isEmpty()) {
				crit.addIn(ACCOUNT, accounts);
			}
			ReportBeanHistoryLoader.addFilterConditions(crit, personIDsArr, fromDate, toDate, PERSON, EFFORTDATE);
			crit.addDescendingOrderByColumn(WORKITEM);
			if (ascendingDate) {
				crit.addAscendingOrderByColumn(EFFORTDATE);
				crit.addAscendingOrderByColumn(LASTEDIT);
			} else {
				crit.addDescendingOrderByColumn(EFFORTDATE);
				crit.addDescendingOrderByColumn(LASTEDIT);
			}
			try {
				costList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (TorqueException e) {
				LOGGER.error("Getting the costs for workItems failed with " + e.getMessage());
			}
		}		
		return costList;
	}	
	
	/**
	 * Gets the costs by tree filter for activity stream
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param limit
	 * @param fromDate
	 * @param toDate
	 * @param changeTypes
	 * @param changedByPersons
	 * @return
	 */
	@Override
	public List<TCostBean> loadActivityStreamCosts(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID,
			Integer limit, Date fromDate, Date toDate, List<Integer> changedByPersons) {
		Integer[] selectedProjects = filterUpperTO.getSelectedProjects();
		if (selectedProjects==null  || selectedProjects.length==0) {
			//at least one selected project needed
			return new ArrayList<TCostBean>();
		}		
		Criteria crit = TreeFilterCriteria.prepareTreeFilterCriteria(filterUpperTO, raciBean, personID);
		addActivityStreamCriteria(crit, limit, fromDate, toDate, changedByPersons);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the costs by tree filter, fromDate " + fromDate + " toDate " + toDate +
					" persons " + changedByPersons +  " failed with " + e.getMessage(), e);
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
		criteria.addJoin(TWorkItemPeer.WORKITEMKEY,  WORKITEM);
		if (changedByPersons!=null && !changedByPersons.isEmpty()) {
			criteria.addIn(PERSON, changedByPersons);
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
		criteria.addDescendingOrderByColumn(LASTEDIT);
		return criteria;
	}
	
	/**
	 * Get the cost beans for a workItem 
	 * @param workItemID
	 * @param personID if specified filter by person
	 * @return
	 */
	@Override
	public List<TCostBean> getByWorkItemAndPerson(Integer workItemID, Integer personID) {
		List<TCost> torqueList = new ArrayList<TCost>();
		Criteria crit = new Criteria();
		crit.add(WORKITEM, workItemID);
		//show just own costs
		if (personID!=null) {
			crit.add(BaseTCostPeer.PERSON, personID);
		}
		crit.addDescendingOrderByColumn(EFFORTDATE);
		//the hours/minutes/seconds part of effort date might be empty, better use the lastEdit also as order by
		crit.addDescendingOrderByColumn(LASTEDIT);
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the costs for workItem " + workItemID + " failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}
	
	/**
	 * Gets a list of TComputedValuesBean with the sum of cost/time values grouped by workItems 
	 * @return
	 */
	@Override
	public List<TComputedValuesBean> loadExpenseGroupedByWorkItem() {
		List<TComputedValuesBean> computedValuesBeanList = new LinkedList<TComputedValuesBean>();
		String sumHours = "SUM(" + HOURS + ")";
		String sumCost = "SUM(" + COST + ")";
		Criteria crit = new Criteria();
		crit.addSelectColumn(WORKITEM);
		crit.addSelectColumn(sumHours);
		crit.addSelectColumn(sumCost);
		crit.addGroupByColumn(WORKITEM);
		List<Record> records = new LinkedList<Record>();
		try {
			records = doSelectVillageRecords(crit);
		} catch(Exception e) {
			LOGGER.error("Groupping the expenses by workItems failed with " + e.getMessage());
		}
		try {
			if (records!=null && !records.isEmpty()) {
				for (Record record : records) {
					Integer workItemKey = record.getValue(1).asIntegerObj();
					Value value = record.getValue(2);
					if (value!=null && !value.isNull()) {
						computedValuesBeanList.add(createExpenseComputedValuesBean(workItemKey, 
								new Integer(TComputedValuesBean.EFFORTTYPE.TIME), value.asDoubleObj(), null));
					}
					value = record.getValue(3);
					if (value!=null && !value.isNull()) {
						computedValuesBeanList.add(createExpenseComputedValuesBean(workItemKey, 
								new Integer(TComputedValuesBean.EFFORTTYPE.COST), value.asDoubleObj(), null));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Getting the groupped expenses by workItems failed with " + e.getMessage());
		}
		return computedValuesBeanList;
	}
	
	
	
	/**
	 * Gets a list of TComputedValuesBean with the sum of cost/time values grouped by workItems and persons
	 * @return
	 */
	@Override
	public List<TComputedValuesBean> loadExpenseGroupedByWorkItemAndPerson() {
		List<TComputedValuesBean> computedValuesBeanList = new LinkedList<TComputedValuesBean>();
		String sumHours = "SUM(" + HOURS + ")";
		String sumCost = "SUM(" + COST + ")";
		Criteria crit = new Criteria();
		crit.addSelectColumn(WORKITEM);
		crit.addSelectColumn(PERSON);
		crit.addSelectColumn(sumHours);
		crit.addSelectColumn(sumCost);
		crit.addGroupByColumn(WORKITEM);
		crit.addGroupByColumn(PERSON);
		List<Record> records = new LinkedList<Record>();
		try {
			records = doSelectVillageRecords(crit);
		} catch(Exception e) {
			LOGGER.error("Groupping the expenses by workItems failed with " + e.getMessage());
		}	
		try {
			if (records!=null && !records.isEmpty()) {
				for (Record record : records) {
					Integer workItemKey = record.getValue(1).asIntegerObj();
					Integer person = record.getValue(2).asIntegerObj();
					Value value = record.getValue(3);
					if (value!=null && !value.isNull()) {
						computedValuesBeanList.add(createExpenseComputedValuesBean(workItemKey, 
								new Integer(TComputedValuesBean.EFFORTTYPE.TIME), value.asDoubleObj(), person));
					}
					value = record.getValue(4);
					if (value!=null && !value.isNull()) {
						computedValuesBeanList.add(createExpenseComputedValuesBean(workItemKey, 
								new Integer(TComputedValuesBean.EFFORTTYPE.COST), value.asDoubleObj(), person));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Getting the groupped expenses by workItems failed with " + e.getMessage());
		}
		return computedValuesBeanList;
	}
	
	private TComputedValuesBean createExpenseComputedValuesBean(Integer workItemKey, 
			Integer effortType, Double computedValue, Integer person) {
		TComputedValuesBean computedValuesBean = new TComputedValuesBean();
		computedValuesBean.setWorkitemKey(workItemKey);
		computedValuesBean.setEffortType(effortType);
		computedValuesBean.setComputedValueType(Integer.valueOf(TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE));
		computedValuesBean.setComputedValue(computedValue);
		computedValuesBean.setPerson(person);
		return computedValuesBean;
	}
	
	/**
	 * Gets a list of TCostBean with the sum of expense values grouped by workItems
	 * @param workItemIDs
	 * @return
	 */
	@Override
	public List<TCostBean> loadSumExpensesForWorkItems(List<Integer> workItemIDs) {
		List<TCostBean> costBeanList = new LinkedList<TCostBean>();
		if (workItemIDs==null || workItemIDs.isEmpty()) {
			return costBeanList;
		}
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return costBeanList;
		}
		List<Record> records = new LinkedList<Record>();
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			String sumHours = "SUM(" + HOURS + ")";
			String sumCost = "SUM(" + COST + ")";
			Criteria crit = new Criteria();
			crit.addIn(WORKITEM, workItemIDChunk);
			crit.addSelectColumn(WORKITEM);
			crit.addSelectColumn(sumHours);
			crit.addSelectColumn(sumCost);
			crit.addGroupByColumn(WORKITEM);
			try {
				records.addAll(doSelectVillageRecords(crit));
			} catch(Exception e) {
				LOGGER.error("Groupping the expenses by parent workItems failed with " + e.getMessage());
			}
		}
		try {
			if (records!=null && !records.isEmpty()) {
				for (Record record : records) {
					TCostBean costBean = new TCostBean();
					Integer workItemKey = record.getValue(1).asIntegerObj();
					costBean.setWorkitem(workItemKey);
					Value work = record.getValue(2);
					if (work!=null && !work.isNull()) {
						costBean.setHours(work.asDoubleObj());
					}
					Value cost = record.getValue(3);
					if (cost!=null && !cost.isNull()) {
						costBean.setCost(cost.asDoubleObj());
					}
					costBeanList.add(costBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Getting the groupped expenses by workItems failed with " + e.getMessage());
		}
		return costBeanList;
	}
	
	
	/**
	 * Gets a list of TCostBean with the sum of expense values grouped by workItems and persons
	 * @param workItemIDs
	 * @return
	 */
	@Override
	public List<TCostBean> loadSumExpensesForWorkItemsAndPersons(List<Integer> workItemIDs) {
		List<TCostBean> costBeanList = new LinkedList<TCostBean>();
		if (workItemIDs==null || workItemIDs.isEmpty()) {
			return costBeanList;
		}
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return costBeanList;
		}
		List<Record> records = new LinkedList<Record>();
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			String sumHours = "SUM(" + HOURS + ")";
			String sumCost = "SUM(" + COST + ")";
			Criteria crit = new Criteria();
			crit.addIn(WORKITEM, workItemIDChunk);
			crit.addSelectColumn(WORKITEM);
			crit.addSelectColumn(PERSON);
			crit.addSelectColumn(sumHours);
			crit.addSelectColumn(sumCost);
			crit.addGroupByColumn(WORKITEM);
			crit.addGroupByColumn(PERSON);
			try {
				records.addAll(doSelectVillageRecords(crit));
			} catch(Exception e) {
				LOGGER.error("Groupping the expenses by parent workItems failed with " + e.getMessage());
			}
		}
		try {
			if (records!=null && !records.isEmpty()) {
				for (Record record : records) {
					TCostBean costBean = new TCostBean();
					Integer workItemKey = record.getValue(1).asIntegerObj();
					costBean.setWorkitem(workItemKey);
					Integer person = record.getValue(2).asIntegerObj();
					costBean.setPerson(person);
					Value work = record.getValue(3);
					if (work!=null && !work.isNull()) {
						costBean.setHours(work.asDoubleObj());
					}
					Value cost = record.getValue(4);
					if (cost!=null && !cost.isNull()) {
						costBean.setCost(cost.asDoubleObj());
					}
					costBeanList.add(costBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Getting the groupped expenses by workItems failed with " + e.getMessage());
		}
		return costBeanList;
	}
		
	private static List<TCostBean> convertTorqueListToBeanList(List<TCost> torqueList) {
		List<TCostBean> beanList = new LinkedList<TCostBean>();
		if (torqueList!=null){
			for (TCost tCost : torqueList) {
				beanList.add(tCost.getBean());
			}
		}
		return beanList;
	}
}
