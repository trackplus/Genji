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


package com.aurel.track.item.lock;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemLockBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemLockDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.ItemBL;

/**
 * Helper class for locking the workItems
 * @author Tamas Ruff
 *
 */
public class ItemLockBL {

	private static WorkItemLockDAO workItemLockDAO = DAOFactory.getFactory().getWorkItemLockDAO();

	private static final Logger LOGGER = LogManager.getLogger(ItemLockBL.class);
	/**
	 * name of the session attribute under which the current locked issue is saved
	 */
	public static final String LOCKEDISSUE = "LOCKEDISSUE";


	/**
	 * Releases a locked issueNumber if the sessionID corresponds
	 * @param itemID
	 * @param sessionID
	 */
	public static void removeLockedIssueBySession(Integer itemID, String sessionID) {
		LOGGER.debug("Unlocked issue " + itemID);
		if (itemID==null || sessionID==null) {
			return;
		}
		//get by workItem
		TWorkItemLockBean workItemLockBeanByIssueNo = workItemLockDAO.loadByPrimaryKey(itemID);
		//release only if it is from the right session
		if (workItemLockBeanByIssueNo!=null && sessionID.equals(workItemLockBeanByIssueNo.getHttpSession())) {
			workItemLockDAO.delete(itemID);
		}
	}

	/**
	 * Releases all locks for a single user.
	 * It will be called by the logging in,
	 * for releasing of the locks which remained blocked
	 * by closing the browser window by editItem.
	 */
	public static synchronized void removeLockedIssuesByUser(Integer personID) {
		workItemLockDAO.deleteByPerson(personID);
	}


	/**
	 * Prepares the errorData in case of no locking possible (the issue is already locked by somebody else)
	 * To avoid the copy-paste code of building the error messages in each edit-related action
	 * @param itemID
	 * @param httpSession
	 * @return
	 */
	public static ErrorData lockItem(Integer itemID, TPersonBean personBean, String sessionID) {
		TWorkItemLockBean workItemLockBeanByIssueNo = workItemLockDAO.loadByPrimaryKey(itemID);
		if (workItemLockBeanByIssueNo==null) {
			if (itemID==null || sessionID==null || personBean==null) {
				//should never happen
				return null;
			}
			workItemLockBeanByIssueNo = new TWorkItemLockBean();
			workItemLockBeanByIssueNo.setWorkItem(itemID);
			workItemLockBeanByIssueNo.setPerson(personBean.getObjectID());
			workItemLockBeanByIssueNo.setHttpSession(sessionID);
			LOGGER.debug("Lock issue " + itemID + " by user " + personBean.getName() + " in session " + sessionID);
			workItemLockDAO.save(workItemLockBeanByIssueNo);
			return null;
		} else {
			boolean sameSession = sessionID.equals(workItemLockBeanByIssueNo.getHttpSession());
			if (sameSession) {
				return null;
			} else {
				return getLockedItemErrorData(workItemLockBeanByIssueNo);
			}
		}
	}

	public static ErrorData isItemLocked(Integer itemID,  String sessionID) {
		TWorkItemLockBean workItemLockBeanByIssueNo = workItemLockDAO.loadByPrimaryKey(itemID);
		if (workItemLockBeanByIssueNo==null) {
			return null;
		}else {
			boolean sameSession = sessionID.equals(workItemLockBeanByIssueNo.getHttpSession());
			if(sameSession) {
				return null;
			}
		}
		return getLockedItemErrorData(workItemLockBeanByIssueNo);
	}

	private static ErrorData getLockedItemErrorData(TWorkItemLockBean workItemLockBeanByIssueNo) {
		String lockedByName = null;
		Integer lockedByPerson = workItemLockBeanByIssueNo.getPerson();
		if (lockedByPerson!=null) {
			TPersonBean lockedByPersonBean = LookupContainer.getPersonBean(lockedByPerson);
			if (lockedByPersonBean!=null) {
				lockedByName = lockedByPersonBean.getName();
			}
		}
		if (lockedByName==null) {
			lockedByName = "";
		}
		List<ErrorParameter> errorParameters = new LinkedList<ErrorParameter>();
		ErrorParameter errorParameter = new ErrorParameter(lockedByName);
		errorParameters.add(errorParameter);
		ErrorData errorData = new ErrorData("report.reportError.error.itemLocked", errorParameters);
		errorData.setConfirm(true);
		return errorData;
	}

	/**
	 * Gets the locked items from workItemIDList
	 * @param workItemIDList
	 * @return
	 */
	public static Map<Integer, Integer> getLockedIssues(List<Integer> workItemIDList) {
		Map<Integer, Integer> lockedItemsMap = null;
		List<TWorkItemLockBean> workItemLockBeans = workItemLockDAO.getLockedIssues(workItemIDList);
		if (workItemLockBeans!=null && !workItemLockBeans.isEmpty()) {
			lockedItemsMap = new HashMap<Integer, Integer>();
			for (TWorkItemLockBean workItemLockBean : workItemLockBeans) {
				lockedItemsMap.put(workItemLockBean.getWorkItem(), workItemLockBean.getPerson());
			}
		}
		return lockedItemsMap;
	}

	/**
	 * Deletes all TWorkItemLockBeans from the database by starting the server
	 */
	public static synchronized void removeAllLocks() {
		workItemLockDAO.deleteAll();
	}
}
