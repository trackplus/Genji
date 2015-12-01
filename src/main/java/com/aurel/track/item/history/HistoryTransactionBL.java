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

package com.aurel.track.item.history;

import java.util.Date;
import java.util.List;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.THistoryTransactionBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.HistoryTransactionDAO;
import com.aurel.track.util.IntegerStringBean;

/**
 * Utility classes for history transaction
 * @author Tamas
 *
 */
public class HistoryTransactionBL {

	private static HistoryTransactionDAO historyTransactionDAO = DAOFactory.getFactory().getHistoryTransactionDAO();
	
	/**
	 * Load the history transaction by primary key
	 * @param objectID
	 * @return
	 */
	public static THistoryTransactionBean getHistoryTransactionBean(Integer objectID){
		return historyTransactionDAO.loadByPrimaryKey(objectID);
	}

	/**
	 * Loads the HistoryTransactions by itemID and fields changed since
	 * @param itemID
	 * @param fieldIDs
	 * @param since
	 * @return
	 */
	public static List<THistoryTransactionBean> loadByItemAndFieldsSince(Integer itemID, List<Integer> fieldIDs, Date since) {
		return historyTransactionDAO.loadByItemAndFieldsSince(itemID, fieldIDs, since);
	}
	
	/**
     * Gets the history transactions for a number of workItems and fields for a specific user
     * that has caused these changes.
     * @param workItemIDs an array with issue oids
     * @param fieldIDs if not null or empty filter also by fieldID, otherwise neglect this parameter.
     * See {@link com.aurel.track.fieldType.constants.SystemFields} for possible values
     * @param includeField relevant only if fieldID not null or empty:
     *      <ul><li>true: include the fields</li>
     *          <li>false: exclude the fields</li></ul>
     * @param personIDs if not null or empty filter also by personID
     * @param fromDate if specified consider history only after this date
     * @param toDate  if specified consider history only before this date
     * @return a list of history transactions defined as shown above
     */
	public static List<THistoryTransactionBean> getByWorkItemsAndFields(int[] workItemIDs, 
    		Integer[] fieldIDs, boolean includeField, List<Integer> personIDs,
    		Date fromDate, Date toDate) {
		return historyTransactionDAO.getByWorkItemsAndFields(workItemIDs, fieldIDs, includeField, personIDs, fromDate, toDate);
	}
	
	/**
     * <p>Gets the <code>HistorySelectValues</code> (that is the actual field changes) list for a 
     * given set of issues. Only the specified field with the given select values is considered.
     * Only the given time interval is considered, if specified.
     * </p>
     * <p>The selectValues list depends on the field type: for example for statuses these are
     * the object ids of the statuses to consider; for priorities these are the object ids for
     * priorities to be considered.
     * </p>
     * @param workItemIDs should not be null or zero length
     * @param fieldID should not be null. See {@link com.aurel.track.fieldType.constants.SystemFields} for possible values 
     * @param selectValues if null do not filter
     * @param dateFrom if null do not filter
     * @param dateTo if null do not filter
     * @return
     */
	public static List<HistorySelectValues> getByWorkItemsFieldNewValuesDates(int[] workItemIDs, 
    		Integer fieldID, List<Integer> selectValues, Date dateFrom, Date dateTo) {
    	return historyTransactionDAO.getByWorkItemsFieldNewValuesDates(workItemIDs, fieldID, selectValues, dateFrom, dateTo);
    }
	
	/**
     * Gets an <code>IntegerStringBean</code> list where the integer is the
     * object id of an issue and the string the new value of a long text field as
     * specified by <code>fieldID</code>. Thus, all issues are returned that
     * had a long text change within this field.
     * @param workItemIDs if null get for all workItems
     * @param fieldID should not be null 
     * @return
     */
	public static List<IntegerStringBean> getByWorkItemsLongTextField(List<Integer> workItemIDs, Integer fieldID) {
		return historyTransactionDAO.getByWorkItemsLongTextField(workItemIDs, fieldID);
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
	public static List<THistoryTransactionBean> getActivityStream(List<Integer> workItemIDs, Integer limit,
			Date fromDate, Date toDate, List<Integer> changeTypes, List<Integer> changedByPersons) {
		return historyTransactionDAO.getActivityStream(workItemIDs, limit, fromDate, toDate, changeTypes, changedByPersons);
	}

	/**
	 * Gets the history transactions by tree filter for activity stream
	 * @param filterSelectsTO
	 * @param raciBean
	 * @param personID
	 * @param limit
	 * @param fromDate
	 * @param toDate
	 * @param changeTypes
	 * @param changedByPersons
	 * @return
	 */
	public static List<THistoryTransactionBean> loadActivityStreamHistoryTransactions(FilterUpperTO filterSelectsTO, RACIBean raciBean, Integer personID,
			Integer limit, Date fromDate, Date toDate, List<Integer> changeTypes, List<Integer> changedByPersons) {
		return historyTransactionDAO.loadActivityStreamHistoryTransactions(filterSelectsTO, raciBean, personID, limit, fromDate, toDate, changeTypes, changedByPersons);
	}
	
	/**
	 * Saves a hitory transaction
	 * @param workItem
	 * @param changedBy
	 * @param lastEdit
	 * @param uuid
	 * @return
	 */
	public static Integer saveHistoryTransaction(Integer workItem, Integer changedBy, Date lastEdit, String uuid) {
		THistoryTransactionBean historyTransactionBean = new THistoryTransactionBean();
		historyTransactionBean.setWorkItem(workItem);
		historyTransactionBean.setChangedByID(changedBy);			
		historyTransactionBean.setLastEdit(lastEdit);
		if (uuid!=null) {
			historyTransactionBean.setUuid(uuid);
		}
		return historyTransactionDAO.save(historyTransactionBean);	
	}
	
	/**
	 * Saves a HistoryTransactionBean in the THistoryTransaction table
	 * @param historyTransactionBean
	 * @return the object id of this new transaction
	 */
	public static Integer save(THistoryTransactionBean historyTransactionBean) {
		return historyTransactionDAO.save(historyTransactionBean);
	}
	
	/**
	 * Deletes a HistoryTransaction by primary key 
	 * @param objectID
	 */
	public static void delete(Integer objectID) {
		historyTransactionDAO.delete(objectID);
	}
}
