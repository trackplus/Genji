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

package com.aurel.track.item.workflow.execute;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aurel.track.beans.TItemTransitionBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ItemTransitionDAO;

/**
 * Logic for item transitions
 * @author Tamas
 *
 */
public class ItemTransitionBL {
	private static ItemTransitionDAO itemTransitionDAO = DAOFactory.getFactory().getItemTransitionDAO();

	/**
	 * Loads itemTransitionBeans by itemIDs and transition and return the found itemIDs
	 * @param itemIDs
	 * @param transitionID
	 * @return
	 */
	public static Set<Integer> foundByItemsAndTransition(Set<Integer> itemIDs, Integer transitionID) {
		Set<Integer> transactionDoneItems = new HashSet<Integer>();
		List<TItemTransitionBean> itemTransitionList = itemTransitionDAO.loadByItemsAndTransition(itemIDs, transitionID);
		if (itemTransitionList!=null && !itemTransitionList.isEmpty()) {
			for (TItemTransitionBean itemTransitionBean : itemTransitionList) {
				transactionDoneItems.add(itemTransitionBean.getWorkItem());
			}
		}
		return transactionDoneItems;
	}
	

	/**
	 * Saves a itemTransitionBean in the TItemTransitionBean table
	 * @param itemTransitionBean
	 * @return
	 */
	public static Integer addItemTransition(Integer transitionID, Integer itemID) {
		TItemTransitionBean itemTransitionBean = new TItemTransitionBean();
		itemTransitionBean.setTransition(transitionID);
		itemTransitionBean.setWorkItem(itemID);
		itemTransitionBean.setTransitionTime(new Date());
		return itemTransitionDAO.save(itemTransitionBean);
	}
			
	/**
	 * Deletes all entries of an item
	 * @param itemID
	 * @return
	 */
	public static void deleteByWorkItem(Integer itemID) {
		itemTransitionDAO.deleteByWorkItem(itemID);
	}
}
