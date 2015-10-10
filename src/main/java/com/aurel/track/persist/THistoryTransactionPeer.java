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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.TreeFilterCriteria;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.THistoryTransactionBean;
import com.aurel.track.dao.HistoryTransactionDAO;
import com.aurel.track.item.history.HistorySelectValues;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class THistoryTransactionPeer
    extends com.aurel.track.persist.BaseTHistoryTransactionPeer
    implements HistoryTransactionDAO
{	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(THistoryTransactionPeer.class);
	
	private static Class[] deletePeerClasses = {    	
    	TFieldChangePeer.class,
    	//use the superclass doDelete() methode!!!
        BaseTHistoryTransactionPeer.class
    };
    
    private static String[] deleteFields = {    	
    	TFieldChangePeer.HISTORYTRANSACTION,
    	BaseTHistoryTransactionPeer.OBJECTID
    };	
	
	/**
	 * Loads a HistoryTransactionBean by primary key 
	 * @param objectID
	 * @return
	 */
	public THistoryTransactionBean loadByPrimaryKey(Integer objectID) {
		THistoryTransaction tHistoryTransaction = null;
    	try {
    		tHistoryTransaction = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		LOGGER.warn("Loading of a history transaction by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	} 
    	if (tHistoryTransaction!=null) {
			return tHistoryTransaction.getBean();
		}
		return null; 
	}
	
	
	/**
	 * Loads the HistoryTransactions by itemID and fields changed since
	 * @param itemID
	 * @param fieldIDs
	 * @param since
	 * @return
	 */
	public List<THistoryTransactionBean> loadByItemAndFieldsSince(Integer itemID, List<Integer> fieldIDs, Date since) {
		if (fieldIDs!=null && !fieldIDs.isEmpty() && itemID!=null) {
			Criteria criteria = new Criteria();
	    	criteria.add(WORKITEM, itemID);
	    	criteria.add(LASTEDIT, since, Criteria.GREATER_THAN);
	    	criteria.addDescendingOrderByColumn(THistoryTransactionPeer.LASTEDIT);
	    	criteria.addJoin(OBJECTID, TFieldChangePeer.HISTORYTRANSACTION);
	    	criteria.addIn(TFieldChangePeer.FIELDKEY, fieldIDs);
	    	try {
				return convertTorqueListToBeanList(doSelect(criteria));
			} catch (TorqueException e) {
				LOGGER.error("Loading the history transactions by itemID  "  + itemID + " for  fieldIDs " + fieldIDs + " since " + since + " failed with " + e.getMessage());
	        	if (LOGGER.isDebugEnabled()) {
	        		LOGGER.debug(ExceptionUtils.getStackTrace(e));
	        	}
			}
		}
		return null;
	}
	
	/**
     * Get the history transactions for workItems, fields and person
     * @param workItemIDs
     * @param fieldIDs if not null or empty filter also by fieldID, otherwise neglect this parameter
     * @param includeField: relevant only if fieldID not null or empty:
     * 		if true include the fields, if false exclude the fields
     * @param personIDs if not null or empty filter also by personID
     * @param fromDate if specified after this date
     * @param toDate  if specified before this date
     * @return
     */
    public List<THistoryTransactionBean> getByWorkItemsAndFields(int[] workItemIDs, 
    		Integer[] fieldIDs, boolean includeField, List<Integer> personIDs, Date fromDate, Date toDate) {    	
    	List<THistoryTransactionBean> transactionBeanList = new ArrayList<THistoryTransactionBean>();		
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);        
		if (workItemIDChunksList!=null && !workItemIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = workItemIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] workItemIDChunk = (int[])iterator.next();
				boolean addJoin = fieldIDs!=null && fieldIDs.length>0;
				Criteria criteria = new Criteria();
				criteria = addConditions(criteria, workItemIDChunk, fieldIDs, 
						includeField, personIDs, fromDate, toDate, addJoin);
				criteria.addDescendingOrderByColumn(LASTEDIT);
				if (addJoin && fieldIDs!=null && fieldIDs.length>1) {
					//set distinct if join is present and more than one field is searched for
					criteria.setDistinct();
				}
				try {
					transactionBeanList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
				} catch(Exception e) {
					LOGGER.error("Getting the history transactions for workItems " + workItemIDs + 
							" field " + fieldIDs + " include field " + includeField  + 
							" failed with " + e.getMessage(), e);
		        }		
			}
		}	
        return transactionBeanList;
    }
    
    public static Criteria addConditions(Criteria criteria, int[] workItemIDChunk, Integer fieldIDs[], 
    		boolean includeField, List<Integer> personIDs, Date fromDate, Date toDate, boolean addJoin) {    	
    	criteria.addIn(BaseTHistoryTransactionPeer.WORKITEM, workItemIDChunk);
    	if (addJoin) {
    		//join only if fields present
    		criteria.addJoin(OBJECTID, TFieldChangePeer.HISTORYTRANSACTION);
    	}
    	if (fieldIDs!=null && fieldIDs.length>0) {    		
			if (includeField) {
				criteria.addIn(BaseTFieldChangePeer.FIELDKEY, fieldIDs);
			} else {
				criteria.addNotIn(BaseTFieldChangePeer.FIELDKEY, fieldIDs);
			}				
        }
		if (personIDs!=null && !personIDs.isEmpty()) {
			criteria.addIn(CHANGEDBY, personIDs);
		}
		if (fromDate!=null && toDate!=null) {
			Criterion criterionFrom = criteria.getNewCriterion(LASTEDIT, fromDate, Criteria.GREATER_EQUAL);
			Criterion criterionTo = criteria.getNewCriterion(LASTEDIT, toDate, Criteria.LESS_THAN);
			criteria.add(criterionFrom.and(criterionTo));
		} else {
			if (fromDate!=null) {
				criteria.add(LASTEDIT, fromDate, Criteria.GREATER_EQUAL);
			} else {
				if (toDate!=null) {
					criteria.add(LASTEDIT, toDate, Criteria.LESS_THAN);
				}
			}
		}
		return criteria;
    }
    
    /**
     * Get the history transactions for workItems, field, selected values and dates 
     * @param workItemIDs: should be not null or zero length
     * @param fieldID: should be not null 
     * @param selectValues: if null do not filter
     * @param dateFrom: if null do not filter
     * @param dateTo: if null do not filter
     * @return
     */
    public List<HistorySelectValues> getByWorkItemsFieldNewValuesDates(int[] workItemIDs, 
    		Integer fieldID, List<Integer> selectValues, Date dateFrom, Date dateTo) {    	
    	List recordList = new ArrayList();
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);        
		if (workItemIDChunksList!=null && !workItemIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = workItemIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] workItemIDChunk = (int[])iterator.next();
				Criteria crit = getWorkItemsFieldSelectValuesDateCriteria(workItemIDChunk, fieldID,
			    		selectValues, dateFrom, dateTo);
				try {
					recordList.addAll(doSelectVillageRecords(crit));
				} catch(Exception e) {
					LOGGER.error("Getting the history transactions for workItems " + workItemIDs + 
							" field " + fieldID + " dateFrom " + dateFrom  + 
							" dateTo " + dateTo + " failed with " + e.getMessage(), e);
		        }		
			}
		}					
        return convertRecordListToHistorySelectValuesList(recordList);
    }
    
    private static Criteria getWorkItemsFieldSelectValuesDateCriteria(int[] workItemIDs, Integer fieldID,
    		List<Integer> selectValues, Date dateFrom, Date dateTo) {
    	Criteria criteria = new Criteria(); 
    	criteria.addIn(BaseTHistoryTransactionPeer.WORKITEM, workItemIDs);
    	criteria.addJoin(BaseTHistoryTransactionPeer.OBJECTID, BaseTFieldChangePeer.HISTORYTRANSACTION);					
    	criteria.add(BaseTFieldChangePeer.FIELDKEY, fieldID);
    	if (selectValues!=null && !selectValues.isEmpty()) {
    		criteria.addIn(BaseTFieldChangePeer.NEWSYSTEMOPTIONID, selectValues);
    	}
    	if (dateFrom!=null && dateTo!=null) {
    		Criterion changesFrom = criteria.getNewCriterion(LASTEDIT, dateFrom, Criteria.GREATER_EQUAL);
    		Criterion changesTo = criteria.getNewCriterion(LASTEDIT, dateTo, Criteria.LESS_THAN);
    		criteria.add(changesFrom.and(changesTo));
    	} else {
    		if (dateFrom!=null) {
    			criteria.add(LASTEDIT, dateFrom, Criteria.GREATER_EQUAL);
    		}
    		if (dateTo!=null) {
    			criteria.add(LASTEDIT, dateTo, Criteria.LESS_THAN);
    		}
    	}
    	//set the descending order
    	criteria.addDescendingOrderByColumn(LASTEDIT);    	
    	criteria.addSelectColumn(BaseTHistoryTransactionPeer.WORKITEM);
    	criteria.addSelectColumn(BaseTHistoryTransactionPeer.LASTEDIT);
    	criteria.addSelectColumn(BaseTFieldChangePeer.NEWSYSTEMOPTIONID);    	
    	return criteria;
    }
    
    /**
     * Get an IntegerStringBean list for workItems, field
     * @param workItemID if null get for all workItems
     * @param fieldID should not be null 
     * @return
     */
    public List<IntegerStringBean> getByWorkItemsLongTextField(List workItemIDs, Integer fieldID) {
    	List torqueList = new ArrayList();
    	List workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);   
    	if (workItemIDChunksList!=null && !workItemIDChunksList.isEmpty()) {
			Iterator iterator = workItemIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] workItemIDChunk = (int[])iterator.next();
				Criteria crit = getLongTextFieldCriteria(fieldID);					
				crit.addIn(BaseTHistoryTransactionPeer.WORKITEM, workItemIDChunk);				    
				try {
					torqueList.addAll(doSelectVillageRecords(crit));
				} catch(Exception e) {
					LOGGER.error("Getting the long field for workItems " + workItemIDs + 
							" field " + fieldID + " failed with " + e.getMessage(), e);
		        }		
			}
		} else {
			//no workItem specified, means all
			Criteria crit = getLongTextFieldCriteria(fieldID);						
			try {				
				torqueList = doSelectVillageRecords(crit);
			} catch (TorqueException e) {
				LOGGER.error("Getting the long field for workItems " + workItemIDs + 
						" field " + fieldID  + " failed with " + e.getMessage(), e);
			}
		}	     	    	
    	return convertRecordListToIntegerStringBeanList(torqueList);
    }
    
	private	Criteria getLongTextFieldCriteria(Integer fieldID) {
		Criteria criteria = new Criteria();		
		criteria.addJoin(BaseTHistoryTransactionPeer.OBJECTID, BaseTFieldChangePeer.HISTORYTRANSACTION);					
    	criteria.add(BaseTFieldChangePeer.FIELDKEY, fieldID);
    	criteria.addSelectColumn(BaseTHistoryTransactionPeer.WORKITEM);    	
    	criteria.addSelectColumn(BaseTFieldChangePeer.NEWLONGTEXTVALUE);    
    	return criteria;
	}
		
	public Integer save(THistoryTransactionBean historyTransactionBean) {
		if (historyTransactionBean==null) {
			return null;
		}
		THistoryTransaction tHistoryTransaction;		
		try {
			tHistoryTransaction = BaseTHistoryTransaction.createTHistoryTransaction(historyTransactionBean);
			tHistoryTransaction.save();
			return tHistoryTransaction.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a HistoryTransaction for workItem " + historyTransactionBean.getWorkItem() +
					" and person " + historyTransactionBean.getChangedByID() + "  failed with " + e.getMessage(), e);
			return null;
		}	
	}
	
	/**
	 * Deletes the TClasses satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		List list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of THistoryTransaction to be deleted failed with " + e.getMessage(), e);
		}			
        if (list == null || list.isEmpty()) {
            return;
        }
		Iterator iter = list.iterator();
		THistoryTransaction tHistoryTransaction = null;
		while(iter.hasNext()) {
			tHistoryTransaction = (THistoryTransaction)iter.next();
			LOGGER.debug("Deleting the HistoryTransaction from workItem " + tHistoryTransaction.getWorkItem() + 
					" and tHistoryTransactionID " + tHistoryTransaction.getObjectID());
			ReflectionHelper.delete(deletePeerClasses, deleteFields, tHistoryTransaction.getObjectID());
		}
	}  
	
	/**
	 * Deletes a HistoryTransaction by primary key 
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
        crit.add(OBJECTID, objectID);        
        doDelete(crit);        
	}
	
	/**
	 * Gets the history transactions for activity stream
	 * @param limit
	 * @param workItemIDs
	 * @param fromDate
	 * @param toDate
	 * @param changeTypes
	 * @param changedByPersons
	 * @return
	 */
	public List<THistoryTransactionBean> getActivityStream(List<Integer> workItemIDs, Integer limit,
			Date fromDate, Date toDate, List<Integer> changeTypes, List<Integer> changedByPersons) {
		Criteria criteria = null;
		if (workItemIDs==null || workItemIDs.isEmpty()) {
			criteria = prepareActivityStreamCriteria(limit, fromDate, toDate, changeTypes, changedByPersons);
			try {
				return convertTorqueListToBeanList(doSelect(criteria));
			} catch (TorqueException e) {
				LOGGER.error("Getting the history transactions by workItems, fromDate " + fromDate + " toDate " + toDate +
						" changeTypes " + changeTypes + " persons " + changedByPersons +  " failed with " + e.getMessage(), e);
			}
		}
		List<THistoryTransactionBean> historyTransactionList = new LinkedList<THistoryTransactionBean>(); 
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		for (int[] workItemIDChunk : workItemIDChunksList) {
			criteria = prepareActivityStreamCriteria(limit, fromDate, toDate, changeTypes, changedByPersons);
			criteria.addIn(WORKITEM, workItemIDChunk);
			try {
				historyTransactionList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch (TorqueException e) {
				LOGGER.error("Getting the history transactions by workItems, fromDate " + fromDate + " toDate " + toDate +
						" changeTypes " + changeTypes + " persons " + changedByPersons +  " failed with " + e.getMessage(), e);
			}
		}
		return historyTransactionList;
	}
	
	/**
	 * Gets the history transactions by tree filter for activity stream
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
	public List<THistoryTransactionBean> loadActivityStreamHistoryTransactions(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID,
			Integer limit, Date fromDate, Date toDate, List<Integer> changeTypes, List<Integer> changedByPersons) {
		Integer[] selectedProjects = filterUpperTO.getSelectedProjects();
		if (selectedProjects==null  || selectedProjects.length==0) {
			//at least one selected project needed
			return new ArrayList<THistoryTransactionBean>();
		}		
		Criteria crit = TreeFilterCriteria.prepareTreeFilterCriteria(filterUpperTO, raciBean, personID);
		addActivityStreamCriteria(crit, limit, fromDate, toDate, changeTypes, changedByPersons);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the history transactions by tree filter, fromDate " + fromDate + " toDate " + toDate +
					" changeTypes " + changeTypes + " persons " + changedByPersons +  " failed with " + e.getMessage(), e);
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
			Date fromDate, Date toDate,
			List<Integer> changeTypes, List<Integer> changedByPersons) {
		if (limit!=null){
			criteria.setLimit(limit);
		}
		criteria.addJoin(TWorkItemPeer.WORKITEMKEY,  WORKITEM);
		if (changeTypes!=null && !changeTypes.isEmpty()) {
			criteria.addJoin(OBJECTID, TFieldChangePeer.HISTORYTRANSACTION);
			criteria.addIn(TFieldChangePeer.FIELDKEY, changeTypes);
		}
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
		//because of changeTypes is should be set distinct (more than one change type for the same transaction)
		criteria.setDistinct();
		criteria.addDescendingOrderByColumn(LASTEDIT);
		return criteria;
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
	private Criteria prepareActivityStreamCriteria(Integer limit,
			Date fromDate, Date toDate,
			List<Integer> changeTypes, List<Integer> changedByPersons) {
		Criteria criteria = new Criteria();
		if (limit!=null){
			criteria.setLimit(limit);
		}
		if (changeTypes!=null && !changeTypes.isEmpty()) {
			criteria.addJoin(OBJECTID, TFieldChangePeer.HISTORYTRANSACTION);
			criteria.addIn(TFieldChangePeer.FIELDKEY, changeTypes);
		}
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
		//because of changeTypes is should be set distinct (more than one change type for the same transaction)
		criteria.setDistinct();
		criteria.addDescendingOrderByColumn(LASTEDIT);
		return criteria;
	}
	
	/**
	 * Load last "limit" transactions for issues in given project 
	 * @param limit if this value <=0 then get all issue in project
	 * @param projectID if null get transactions from all projects
	 * @param issueTypeIDs
	 * @param personID
	 * @return
	 */
	/*public List<THistoryTransactionBean> loadLastInProject(int limit, 
			Integer projectID, List<Integer> issueTypeIDs, Integer personID) {		
		Criteria criteria = prepareLastInCriteria(limit, projectID, issueTypeIDs, personID);				
		//criteria.addJoin(TWorkItemPeer.PROJCATKEY,  BaseTProjectCategoryPeer.PKEY);
		criteria.add(TWorkItemPeer.PROJECTKEY,  projectID);			
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Getting last " + limit + 
					" HistoryTransaction in project " + projectID  + " failed with " + e.getMessage(), e);
			return null;
		}		
	}*/
	
	/**
	 * Load last "limit" transactions for issues in given release 
	 * @param limit if this value <=0 then get all issue in release
	 * @param projectID if null get transactions from all release
	 * @param releaseID
	 * @param issueTypeIDs
	 * @param personID
	 * @return
	 */
	/*public List<THistoryTransactionBean> loadLastInRelease(int limit,
			Integer projectID, Integer releaseID, List<Integer> issueTypeIDs, Integer personID) {
		List torqueList = new ArrayList();		
		Criteria criteria = prepareLastInCriteria(limit, projectID, issueTypeIDs, personID);			
		criteria.add(BaseTWorkItemPeer.RELSCHEDULEDKEY,  releaseID, Criteria.EQUAL);				
		try {				
			torqueList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Getting last " + limit + 
					" HistoryTransaction in release " + releaseID  + " failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(torqueList);
	}*/
	
	
	
	private static List<THistoryTransactionBean> convertTorqueListToBeanList(List<THistoryTransaction> torqueList) {
		List<THistoryTransactionBean> beanList = new ArrayList<THistoryTransactionBean>();
		if (torqueList!=null) {
			for (THistoryTransaction historyTransaction : torqueList) {                              
                beanList.add(historyTransaction.getBean());
			}
		}
		return beanList;
	}
	
	private List<HistorySelectValues> convertRecordListToHistorySelectValuesList(List recordList) {
		List<HistorySelectValues> historySelectValuesList = new ArrayList<HistorySelectValues>();
		if (recordList!=null) {
			Iterator iterator = recordList.iterator();
			while (iterator.hasNext()) {
				Record record = (Record) iterator.next();
				HistorySelectValues historySelectValues = new HistorySelectValues();
				try {
					historySelectValues.setWorkItemID(record.getValue(1).asIntegerObj());
					historySelectValues.setLastEdit(record.getValue(2).asDate());
					historySelectValues.setNewValue(record.getValue(3).asIntegerObj());
					historySelectValuesList.add(historySelectValues);
				} catch (DataSetException e) {
					LOGGER.error("Converting the record to HistorySelectValues failed with " + e.getMessage(), e);
				}
				
			}
		}
		return historySelectValuesList;
	}
	
	private List<IntegerStringBean> convertRecordListToIntegerStringBeanList(List recordList) {
		List<IntegerStringBean> historyCommentsList = new ArrayList<IntegerStringBean>();
		if (recordList!=null) {
			Iterator iterator = recordList.iterator();
			while (iterator.hasNext()) {
				Record record = (Record) iterator.next();
				IntegerStringBean integerStringBean = new IntegerStringBean();
				try {
					integerStringBean.setValue(record.getValue(1).asIntegerObj());
					integerStringBean.setLabel(record.getValue(2).asString());					
					historyCommentsList.add(integerStringBean);
				} catch (DataSetException e) {
					LOGGER.error("Converting the record to IntegerStringBean failed with " + e.getMessage(), e);
				}
				
			}
		}
		return historyCommentsList;
	}

	
}
