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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TItemTransitionBean;
import com.aurel.track.dao.ItemTransitionDAO;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TItemTransitionPeer
    extends com.aurel.track.persist.BaseTItemTransitionPeer implements ItemTransitionDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TItemTransitionPeer.class);
	/**
	 * Loads itemTransitionBeans by transition and item  
	 * @param itemIDs
	 * @param transitionID
	 * @return
	 */
	public List<TItemTransitionBean> loadByItemsAndTransition(Set<Integer> itemIDs, Integer transitionID) {
		List<TItemTransitionBean> list =  new ArrayList<TItemTransitionBean>();
		if (itemIDs==null || itemIDs.isEmpty() || transitionID==null) {
			return list;
		}
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(itemIDs);
		if (workItemIDChunksList==null) {
			return list;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = (int[])iterator.next();
			criteria = new Criteria();
			criteria.addIn(WORKITEM, workItemIDChunk);
			criteria.add(TRANSITION, transitionID);
			try {
				list.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the item transitions by itemIDs failed with " + e.getMessage(), e);
			}
		}
		return list;
	}

	/**
	 * Saves a itemTransitionBean in the TItemTransitionBean table
	 * @param itemTransitionBean
	 * @return
	 */
	public Integer save(TItemTransitionBean itemTransitionBean) {
		try {
			TItemTransition tItemTransition = TItemTransition.createTItemTransition(itemTransitionBean);
			tItemTransition.save();
			return tItemTransition.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving an item transition failed with " + e.getMessage(), e);
			return null;
		}
	}
			
	/**
	 * Deletes all entries of an item
	 * @param itemID
	 * @return
	 */
	public void deleteByWorkItem(Integer itemID) {
		Criteria  criteria = new Criteria();
		criteria.add(WORKITEM, itemID);
		try {
			doDelete(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the item transition for item " + itemID + " failed with " + e.getMessage(), e);
		}
	}
	
	private List<TItemTransitionBean> convertTorqueListToBeanList(List<TItemTransition> torqueList) {
		List<TItemTransitionBean> beanList = new LinkedList<TItemTransitionBean>();
		if (torqueList!=null) {
			for (TItemTransition itemTransition : torqueList) {
				beanList.add(itemTransition.getBean());
			}
		}
		return beanList;
	}
}
