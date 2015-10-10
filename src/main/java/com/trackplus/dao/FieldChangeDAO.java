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


package com.trackplus.dao;

import java.util.Date;
import java.util.List;

import com.aurel.track.item.ItemPersisterException;
import com.trackplus.model.Tfieldchange;

/**
 * DAO for FieldChange
 * 
 * @author Tamas Ruff
 * 
 */
public interface FieldChangeDAO {

	/**
	 * Loads a FieldChangeBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tfieldchange loadByPrimaryKey(Integer objectID);

	/**
	 * Load all FieldChangeBeans
	 * 
	 * @return
	 */
	// List loadAll();

	/**
	 * Loads a FieldChangeBean list by workItemKeys
	 * 
	 * @param workItemKeys
	 *            if null or empty return empty list
	 * @return
	 */
	// List loadByWorkItemKeys(int[] workItemKeys);

	/**
	 * Get the fieldChangeBeans from the history of the workItemIDs which refer
	 * to customOptions
	 * 
	 * @param workItemIDs
	 * @param personID
	 *            if not null filter also by personID, otherwise neglect this
	 *            parameter
	 * @return
	 */
	List<Tfieldchange> loadHistoryCustomOptionFieldChanges(int[] workItemIDs/*
																			 * ,
																			 * Integer
																			 * personID
																			 */);

	/**
	 * Get the FieldChanges for an workItem
	 * 
	 * @param workItemIDs
	 *            if not null or empty filter also by workItemIDs, otherwise
	 *            neglect this parameter
	 * @param fieldIDs
	 *            if not null filter also by fieldID, otherwise neglect this
	 *            parameter
	 * @param includeField
	 *            : relevant only if fieldID not null: if true include this
	 *            field, if false exclude this field
	 * @param personID
	 *            if not null filter also by personID, otherwise neglect this
	 *            parameter
	 * @param fromDate
	 *            if specified after this date
	 * @param toDate
	 *            if specified before this date
	 * @return
	 */
	List<Tfieldchange> getByWorkItemsAndFields(int[] workItemIDs,
			Integer[] fieldIDs, boolean includeField, Integer personID,
			Date fromDate, Date toDate);

	/**
	 * Get the fieldChangeBeans from the history which are linked to some
	 * transactionUUIDs
	 * 
	 * @param transactionUUIDList
	 * @return
	 */
	List<Tfieldchange> loadByTransactionUUIDS(List transactionUUIDList);

	/**
	 * Get the fieldChangeBeans from the history which are linked to some
	 * transactionIDs
	 * 
	 * @param transactionIDList
	 * @return
	 */
	public List<Tfieldchange> loadByTransactionIDS(
			List<Integer> transactionIDList);

	/**
	 * Saves a HistoryTransactionBean in the THistoryTransaction table
	 * 
	 * @param historyTransactionBean
	 * @return
	 */
	Integer save(Tfieldchange fieldChangeBean) throws ItemPersisterException;

	/**
	 * Whether the history transaction contains any field changes
	 * 
	 * @return
	 */
	boolean hasFieldChanges(Integer historyTransaction);

	/**
	 * Deletes a FieldChange by primary key
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Whether a system option appears in the history
	 * 
	 * @param objectID
	 * @param fieldID
	 * @param newValues
	 * @return
	 */
	boolean isSystemOptionInHistory(Integer objectID, Integer fieldID,
			boolean newValues);

	/**
	 * Replace a system option in the field change history
	 * 
	 * @param oldObjectID
	 * @param newObjectID
	 * @param fieldID
	 * @param newValues
	 */
	void replaceSystemOptionInHistory(Integer oldObjectID, Integer newObjectID,
			Integer fieldID, boolean newValues);

	/**
	 * Set to null a system option in the field change history
	 * 
	 * @param objectID
	 * @param fieldID
	 * @param newValues
	 */
	public void setSystemOptionToNullInHistory(Integer objectID,
			Integer fieldID, boolean newValues);

}
