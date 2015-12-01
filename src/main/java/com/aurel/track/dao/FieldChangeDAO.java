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


package com.aurel.track.dao;

import java.util.Date;
import java.util.List;

import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.item.ItemPersisterException;

/**
 * DAO for FieldChange
 * @author Tamas Ruff
 *
 */
public interface FieldChangeDAO {
	
	/**
	 * Loads a FieldChangeBean by primary key 
	 * @param objectID
	 * @return
	 */
	TFieldChangeBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Loads the FieldChangeBeans by itemID and fields changed since
	 * @param itemID
	 * @param fieldIDs
	 * @param since
	 * @return
	 */
	List<TFieldChangeBean> loadByItemAndFieldsSince(Integer itemID, List<Integer> fieldIDs, Date since);
	
	/**
	 * Get the fieldChangeBeans from the history of the workItemIDs
	 * which refer to customOptions
	 * @param workItemIDs
	 * @return
	 */
	List<TFieldChangeBean> loadHistoryCustomOptionFieldChanges(int[] workItemIDs);
	
	/**
     * Get the FieldChanges  for an workItem
     * @param workItemIDs if not null or empty filter also by workItemIDs, otherwise neglect this parameter
     * @param fieldIDs if not null filter also by fieldID, otherwise neglect this parameter
     * @param includeField: relevant only if fieldID not null:
     * 		if true include this field, if false exclude this field
     * @param personIDs if not null or empty filter also by personIDs, otherwise neglect this parameter
     * @param fromDate if specified after this date
     * @param toDate  if specified before this date
     * @return
     */
    List<TFieldChangeBean> getByWorkItemsAndFields(int[] workItemIDs, Integer[] fieldIDs,
    		boolean includeField, List<Integer> personIDs, Date fromDate, Date toDate);

    /**
	 * Get the fieldChangeBeans from the history which are linked to some transactionUUIDs
	 * @param transactionUUIDList
	 * @return
	 */
	List<TFieldChangeBean> loadByTransactionUUIDS(List<String> transactionUUIDList);
	/**
	 * Get the fieldChangeBeans from the history which are linked to some transactionIDs
	 * @param transactionIDList
	 * @return
	 */
	List<TFieldChangeBean> loadByTransactionIDS(List<Integer> transactionIDList);
	
	/**
     * Get the FieldChanges for a fieldID
     * @param fieldID
     * @return
     */
	List<TFieldChangeBean> getByFieldID(Integer fieldID);
    
	/**
	 * Saves a HistoryTransactionBean in the THistoryTransaction table
	 * @param historyTransactionBean
	 * @return
	 */
	Integer save(TFieldChangeBean fieldChangeBean) throws ItemPersisterException;
    
	/**
	 * Whether the history transaction contains any field changes 
	 * @return
	 */
	boolean hasFieldChanges(Integer historyTransaction);
	
	/**
	 * Deletes a FieldChange by primary key 
	 * @param objectID
	 */
	void delete(Integer objectID);
	
	/**
     * Get the FieldChanges  for a fieldID
     * @param fieldIDs
     * @return
     */
	void deleteByFieldID(Integer fieldID);
	
	/**
	 * Whether a system option appears in the history
	 * @param objectID
	 * @param fieldID
	 * @param newValues
	 * @return
	 */
	boolean isSystemOptionInHistory(Integer objectID, Integer fieldID, boolean newValues);
	
	/**
	 * Whether a system option from list appears in the history
	 * The reflection does not work because an additional condition
	 * should be satisfied (no direct foreign key relationship exists)
	 * @param objectIDs
	 * @param fieldID
	 * @param newValues
	 */
	boolean isSystemOptionInHistory(List<Integer> objectIDs, Integer fieldID, boolean newValues);
	
	/**
	 * Replace a system option in the field change history
	 * @param oldObjectID
	 * @param newObjectID
	 * @param fieldID
	 * @param newValues
	 */
	void replaceSystemOptionInHistory(Integer oldObjectID, Integer newObjectID, Integer fieldID, boolean newValues);
	
	/**
	 * Set to null a system option in the field change history
	 * @param objectID
	 * @param fieldID
	 * @param newValues
	 */
	void setSystemOptionToNullInHistory(Integer objectID, Integer fieldID, boolean newValues);

	public int countCommentsByWorkItemID(Integer workItemID);
	
}
