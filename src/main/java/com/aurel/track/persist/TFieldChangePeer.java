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

import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.dao.FieldChangeDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.util.GeneralUtils;
import com.workingdogs.village.Record;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TFieldChangePeer
    extends com.aurel.track.persist.BaseTFieldChangePeer
    implements FieldChangeDAO {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TFieldChangePeer.class);
	
	/**
	 * Loads a FieldChangeBean by primary key 
	 * @param objectID
	 * @return
	 */
	@Override
	public TFieldChangeBean loadByPrimaryKey(Integer objectID) {
		TFieldChange tFieldChange = null;
    	try {
    		tFieldChange = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		LOGGER.warn("Loading of a field change by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	} 
    	if (tFieldChange!=null) {
			return tFieldChange.getBean();
		}
		return null;  
	}
	
	/**
	 * Loads the FieldChangeBeans by itemID and fields changed since
	 * @param itemID
	 * @param fieldIDs
	 * @param since
	 * @return
	 */
	@Override
	public List<TFieldChangeBean> loadByItemAndFieldsSince(Integer itemID, List<Integer> fieldIDs, Date since) {
		if (fieldIDs!=null && !fieldIDs.isEmpty() && itemID!=null) {
			Criteria criteria = new Criteria();
	    	criteria.addIn(FIELDKEY, fieldIDs);
	    	criteria.addJoin(THistoryTransactionPeer.OBJECTID, HISTORYTRANSACTION);
	    	criteria.add(THistoryTransactionPeer.WORKITEM, itemID);
	    	criteria.add(THistoryTransactionPeer.LASTEDIT, since, Criteria.GREATER_THAN);
	    	criteria.addDescendingOrderByColumn(THistoryTransactionPeer.LASTEDIT);
	    	try {
				return convertTorqueListToBeanList(doSelect(criteria));
			} catch (TorqueException e) {
				LOGGER.error("Loading the fieldChangeBeans by itemID  "  + itemID + " for  fieldIDs " + fieldIDs + " since " + since + " failed with " + e.getMessage());
	        	if (LOGGER.isDebugEnabled()) {
	        		LOGGER.debug(ExceptionUtils.getStackTrace(e));
	        	}
			}
		}
		return null;
	}
	
	/**
	 * Get the fieldChangeBeans from the history of the workItemIDs
	 * which refer to customOptions
	 * @param workItemIDs
	 * @param personID if not null filter also by personID, otherwise neglect this parameter
	 * @return
	 */
	@Override
	public List<TFieldChangeBean> loadHistoryCustomOptionFieldChanges(int[] workItemIDs) {
		List<TFieldChangeBean> fieldChangeBeanList = new ArrayList<TFieldChangeBean>();		
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);        
		if (workItemIDChunksList!=null && !workItemIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = workItemIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] workItemIDChunk = (int[])iterator.next();
				Criteria crit = HistoryDropdownContainerLoader.prepareHistoryCriteria(workItemIDChunk);
				crit.add(VALIDVALUE, ValueType.CUSTOMOPTION);																		
				try {
					fieldChangeBeanList.addAll(convertTorqueListToBeanList(doSelect(crit)));
				} catch(Exception e) {
		        	LOGGER.error("Loading the history custom option fieldChanges for workItems failed with " + e.getMessage());
		        	LOGGER.debug(ExceptionUtils.getStackTrace(e));
		        }		
			}
		} 
        return fieldChangeBeanList;
	}
	
	
	
	/**
     * Get the FieldChanges  for an workItem
     * @param workItemIDs
     * @param fieldIDs if not null filter also by fieldID, otherwise neglect this parameter
     * @param includeField: relevant only if fieldIDs not null:
     * 		if true include this field, if false exclude this field
     * @param personID if not null or empty filter also by personID, otherwise neglect this parameter
     * @param fromDate if specified after this date
     * @param toDate  if specified before this date
     * @return
     */
    @Override
    public List<TFieldChangeBean> getByWorkItemsAndFields(int[] workItemIDs, 
    		Integer[] fieldIDs, boolean includeField, List<Integer> personIDs, Date fromDate, Date toDate) {
    	List<TFieldChangeBean> fieldChangeBeanList = new ArrayList<TFieldChangeBean>();		
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);        
		if (workItemIDChunksList!=null && !workItemIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = workItemIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] workItemIDChunk = (int[])iterator.next();
				Criteria criteria = new Criteria();
				THistoryTransactionPeer.addConditions(criteria, workItemIDChunk, fieldIDs, includeField, personIDs, fromDate, toDate, true);
				try {
					fieldChangeBeanList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
				} catch(Exception e) {
		        	LOGGER.error("Loading the fieldChangesBeans for workItemIDs and fieldID " + fieldIDs +
		        			" failed with " + e.getMessage());
	        		LOGGER.debug(ExceptionUtils.getStackTrace(e));
		        }		
			}
		} 
        return fieldChangeBeanList;
    }
    /**
	 * Get the fieldChangeBeans from the history which are linked to some transactionIDs
	 * @param transactionIDList
	 * @return
	 */
	@Override
	public List<TFieldChangeBean> loadByTransactionIDS(List<Integer> transactionIDList) {
		List<TFieldChangeBean> fieldChangeBeansList = new ArrayList<TFieldChangeBean>();
		if (transactionIDList==null || transactionIDList.isEmpty()) {
			return fieldChangeBeansList;
		}
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(transactionIDList);
		if (workItemIDChunksList==null) {
			return fieldChangeBeansList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			int[] transactionIDChunk = (int[])iterator.next();	
			criteria = new Criteria();
			criteria.addIn(HISTORYTRANSACTION, transactionIDChunk);			
			try {
				fieldChangeBeansList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
	        	LOGGER.error("Loading the fieldChangeBeans by transaction uuids and the chunk number " + 
	        			i + " of length  "  + transactionIDChunk.length + " failed with " + e.getMessage());
	        	LOGGER.debug(ExceptionUtils.getStackTrace(e));
	        }			
		}
		return fieldChangeBeansList;
	}
    /**
	 * Get the fieldChangeBeans from the history which are linked to some transactionUUIDs
	 * @param transactionUUIDList
	 * @return
	 */
	@Override
	public List<TFieldChangeBean> loadByTransactionUUIDS(List<String> transactionUUIDList) {
		List<TFieldChangeBean> fieldChangeBeansList = new ArrayList<TFieldChangeBean>();
		if (transactionUUIDList==null || transactionUUIDList.isEmpty()) {
			return fieldChangeBeansList;
		}
		Criteria criteria;
		List<List<String>> workItemIDChunksList = GeneralUtils.getListOfStringChunks(transactionUUIDList);
		if (workItemIDChunksList==null) {
			return fieldChangeBeansList;
		}
		Iterator<List<String>> iterator = workItemIDChunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			List<String> transactionUUIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addJoin(THistoryTransactionPeer.OBJECTID, HISTORYTRANSACTION);
			criteria.addIn(THistoryTransactionPeer.TPUUID, transactionUUIDChunk);			
			try {
				fieldChangeBeansList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
	        	LOGGER.error("Loading the fieldChangeBeans by transaction uuids and the chunk number " + 
	        			i + " of length  "  + transactionUUIDChunk.size() + " failed with " + e.getMessage());
	        	LOGGER.debug(ExceptionUtils.getStackTrace(e));
	        }			
		}
		return fieldChangeBeansList;
	}
    
	/**
     * Get the FieldChanges for a fieldID
     * @param fieldID
     * @return
     */
	@Override
	public List<TFieldChangeBean> getByFieldID(Integer fieldID) {
    	Criteria criteria = new Criteria();
    	criteria.add(TFieldChangePeer.FIELDKEY, fieldID);
    	try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the fieldChangeBeans by fieldID  "  + fieldID + " failed with " + e.getMessage());
        	if (LOGGER.isDebugEnabled()) {
        		LOGGER.debug(ExceptionUtils.getStackTrace(e));
        	}
        	return null;
		}
    }
    
	@Override
	public Integer save(TFieldChangeBean fieldChangeBean) throws ItemPersisterException {
		TFieldChange tFieldChange;		
		try {
			tFieldChange = BaseTFieldChange.createTFieldChange(fieldChangeBean);
			tFieldChange.save();
			return tFieldChange.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a FieldChange for field " + fieldChangeBean.getFieldKey() + " failed with " + e.getMessage());
        	LOGGER.debug(ExceptionUtils.getStackTrace(e));
			throw new ItemPersisterException("Saving the FieldChange for field " + fieldChangeBean.getFieldKey() + " failed", e);			
		}	
	}
	
	/**
	 * Whether the history transaction contains any field changes 
	 * @return
	 */
	@Override
	public boolean hasFieldChanges(Integer historyTransaction) {
		List fieldChanges = null;
		Criteria criteria = new Criteria();
		criteria.add(HISTORYTRANSACTION, historyTransaction);
		try {
			fieldChanges = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Getting the FieldChanges for historyTransaction " + historyTransaction + " failed with: " + e.getMessage());
        	LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return fieldChanges!=null && !fieldChanges.isEmpty();
	}
	
	/**
	 * Deletes a fieldChange by primary key 
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
        crit.add(OBJECTID, objectID);
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting the FieldChange for objectID " + objectID + " failed with: " + e.getMessage());
        	LOGGER.debug(ExceptionUtils.getStackTrace(e));
        }
	}
	
	/**
     * Get the FieldChanges  for a fieldID
     * @param fieldIDs
     * @return
     */
	@Override
	public void deleteByFieldID(Integer fieldID) {
		Criteria crit = new Criteria();
        crit.add(FIELDKEY, fieldID);
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting the FieldChange by fieldID " + fieldID + " failed with: " + e.getMessage());
        	LOGGER.debug(ExceptionUtils.getStackTrace(e));
        }
	}
	
	/**
	 * Whether a system option appears in the history
	 * @param objectID
	 * @param fieldID
	 * @param newValues
	 * @return
	 */
	@Override
	public boolean isSystemOptionInHistory(Integer objectID, Integer fieldID, boolean newValues) {
		List historyList = null;
		Criteria selectCriteria = new Criteria();        
        if (newValues) {
        	selectCriteria.add(NEWSYSTEMOPTIONID, objectID);        	
		} else {
			selectCriteria.add(OLDSYSTEMOPTIONID, objectID);			
		}        
        selectCriteria.add(SYSTEMOPTIONTYPE, fieldID);        
        
        try {
        	historyList = doSelect(selectCriteria);            
        }
        catch (Exception e) {
            LOGGER.error("Getting the history for objectID " + objectID +
                    "fieldID " + fieldID + " in the history newValues=" +
                    newValues +  " failed with " + e.getMessage(), e);              
        } 
        return historyList!=null && !historyList.isEmpty();
	}
	
	/**
	 * Whether a system option from list appears in the history
	 * The reflection does not work because an additional condition
	 * should be satisfied (no direct foreign key relationship exists)
	 * @param objectIDs
	 * @param fieldID
	 * @param newValues
	 */
	@Override
	public boolean isSystemOptionInHistory(List<Integer> objectIDs, Integer fieldID, boolean newValues) {
		if (objectIDs==null || objectIDs.isEmpty()) {
			return false;
		}
		List historyList = null;
		Criteria selectCriteria;
		List<int[]> chunkList = GeneralUtils.getListOfChunks(objectIDs);
		Iterator<int[]> iterator = chunkList.iterator();
		while (iterator.hasNext()) {
			int[] idChunk = iterator.next();
			selectCriteria = new Criteria();
			if (newValues) {
				selectCriteria.addIn(NEWSYSTEMOPTIONID, idChunk);
			} else {
				selectCriteria.addIn(OLDSYSTEMOPTIONID, idChunk);
			}
			selectCriteria.add(SYSTEMOPTIONTYPE, fieldID);
			try {
				historyList = doSelect(selectCriteria);
			} catch (Exception e) {
				LOGGER.error(
						"Verifiying the dependent " +
						"oldPersonIDs " + objectIDs.size() + " for the user picker failed with " + e.getMessage(), e);
			}
			if (historyList!=null && !historyList.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Replace a system option in the field change history
	 * @param oldObjectID
	 * @param newObjectID
	 * @param fieldID
	 * @param newValues
	 */
	@Override
	public void replaceSystemOptionInHistory(Integer oldObjectID, Integer newObjectID, Integer fieldID, boolean newValues) {
		Criteria selectCriteria = new Criteria();
        Criteria updateCriteria = new Criteria();
        if (newValues) {
        	selectCriteria.add(BaseTFieldChangePeer.NEWSYSTEMOPTIONID, oldObjectID);
        	updateCriteria.add(BaseTFieldChangePeer.NEWSYSTEMOPTIONID, newObjectID);
		} else {
			selectCriteria.add(BaseTFieldChangePeer.OLDSYSTEMOPTIONID, oldObjectID);
			updateCriteria.add(BaseTFieldChangePeer.OLDSYSTEMOPTIONID, newObjectID);
		}        
        selectCriteria.add(BaseTFieldChangePeer.SYSTEMOPTIONTYPE, fieldID);                
        try {
        	doUpdate(selectCriteria, updateCriteria);            
        }
        catch (Exception e) {
            LOGGER.error("Replacing " +
                    "oldObjectID " + oldObjectID + " with " +
                    "newObjectID  " + newObjectID + " for field " + fieldID + 
                    " in the history newValues=" + newValues +  " values failed with " + e.getMessage(), e);                            
        }                     
	}
	
	/**
	 * Set to null a system option in the field change history
	 * @param objectID
	 * @param fieldID
	 * @param newValues
	 */
	@Override
	public void setSystemOptionToNullInHistory(Integer objectID, Integer fieldID, boolean newValues) {
		Criteria selectCriteria = new Criteria();
        Criteria updateCriteria = new Criteria();
        if (newValues) {
        	selectCriteria.add(BaseTFieldChangePeer.NEWSYSTEMOPTIONID, objectID);
        	updateCriteria.add(BaseTFieldChangePeer.NEWSYSTEMOPTIONID, (Object)null, Criteria.EQUAL);
		} else {
			selectCriteria.add(BaseTFieldChangePeer.OLDSYSTEMOPTIONID, objectID);
			updateCriteria.add(BaseTFieldChangePeer.OLDSYSTEMOPTIONID, (Object)null, Criteria.EQUAL);
		}        
        selectCriteria.add(BaseTFieldChangePeer.SYSTEMOPTIONTYPE, fieldID);        
        
        try {
        	doUpdate(selectCriteria, updateCriteria);            
        }
        catch (Exception e) {
            LOGGER.error("Setting to null the objectID " + objectID + " for field " + fieldID +
                   " in the history for newValues=" + newValues +  " failed with " + e.getMessage(), e);            
        }                     
	}


	@Override
	public int countCommentsByWorkItemID(Integer workItemID){
		String COUNT = "count(" + OBJECTID + ")";
		Criteria criteria = new Criteria();
		criteria.add(BaseTHistoryTransactionPeer.WORKITEM, workItemID);
		criteria.addJoin(BaseTHistoryTransactionPeer.OBJECTID, HISTORYTRANSACTION);
		criteria.add(FIELDKEY, SystemFields.INTEGER_COMMENT);
		criteria.addSelectColumn(COUNT);
		try {
			return ((Record) doSelectVillageRecords(criteria).get(0)).getValue(1).asInt();
		} catch (Exception e) {
			LOGGER.error("Counting comments by workItemID " + workItemID +  " failed with " + e.getMessage());
			return 0;
		}
	}

	private static List<TFieldChangeBean> convertTorqueListToBeanList(List<TFieldChange> torqueList) {
		List<TFieldChangeBean> beanList = new LinkedList<TFieldChangeBean>();
		if (torqueList!=null){
			Iterator<TFieldChange> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				TFieldChangeBean bean=itrTorqueList.next().getBean();                               
                beanList.add(bean);
			}
		}
		return beanList;
	}
}
