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

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.CriteriaUtil;
import com.aurel.track.beans.TBasketBean;
import com.aurel.track.beans.TPersonBasketBean;
import com.aurel.track.dao.PersonBasketDAO;
import com.aurel.track.util.GeneralUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import java.util.*;

/**
 * WorkItem baskets for persons
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TPersonBasketPeer
	extends com.aurel.track.persist.BaseTPersonBasketPeer implements PersonBasketDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TPersonBasketPeer.class);
	
	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads a personBasketBeans by basket and person  
	 * @param basketID
	 * @param personI
	 * @return
	 */
	@Override
	public List<TPersonBasketBean> loadByBasketAndPerson(Integer basketID, Integer personID) {
		Criteria crit = new Criteria();
		crit.add(BASKET, basketID);
		crit.add(PERSON, personID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading basket items for basket " + basketID + " and person " + personID +  " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads a personBasketBeans by person for a date
	 * @param personID
	 * @param date
	 * @return
	 */
	@Override
	public List<TPersonBasketBean> loadPersonBasketItemsByDate(Integer personID, Date date) {
		Criteria crit = new Criteria();
		crit.add(PERSON, personID);
		crit.addJoin(TPersonBasketPeer.WORKITEM, TWorkItemPeer.WORKITEMKEY);
		CriteriaUtil.addStateFlagUnclosedCriteria(crit);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		CriteriaUtil.addActiveProjectCriteria(crit);
		crit.add(REMINDERDATE, date, Criteria.EQUAL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading basket items for person " + personID + " for date " + date + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads a personBasketBeans by person and date
	 * @param personID
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	@Override
	public List<TPersonBasketBean> loadPersonBasketItemsByTime(Integer personID, Date dateFrom, Date dateTo) {
		Criteria crit = new Criteria();
		crit.add(PERSON, personID);
		crit.addJoin(TPersonBasketPeer.WORKITEM, TWorkItemPeer.WORKITEMKEY);
		CriteriaUtil.addStateFlagUnclosedCriteria(crit);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		CriteriaUtil.addActiveProjectCriteria(crit);
		Criteria.Criterion criterionFrom = crit.getNewCriterion(REMINDERDATE, dateFrom, Criteria.GREATER_EQUAL);
		Criteria.Criterion criterionTo = crit.getNewCriterion(REMINDERDATE, dateTo, Criteria.LESS_THAN);
		crit.add(criterionFrom.and(criterionTo));
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading basket items for person " + personID + " between date " + dateFrom + " and " + dateTo +  " failed with " + e.getMessage());
			return null;
		}
	}
	
	@Override
	public TPersonBasketBean loadBasketByItemAndPerson(Integer itemID,Integer personID){
		TPersonBasketBean personBasketBean = null;
		Criteria crit = new Criteria();
		crit.add(WORKITEM,itemID);
		crit.add(PERSON,personID);
		try {
			List lst=doSelect(crit);
			if(lst!=null && !lst.isEmpty()){
				personBasketBean=((TPersonBasket)lst.get(0)).getBean();
			}
		} catch (Exception e) {
			LOGGER.error("Loading  basket by itemID:"+itemID+" and personID:" + personID +  " failed with " + e.getMessage());
			personBasketBean=null;
		}
		return personBasketBean;
	}
	@Override
	public List<TPersonBasketBean> loadBasketsByItemsAndPerson(List<Integer> workItemIDs,Integer personID){
		List basketList =  new ArrayList();
		if (workItemIDs==null || workItemIDs.isEmpty()) {
			return basketList;
		}
		Criteria criteria;
		List workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return basketList;
		}
		Iterator iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = (int[])iterator.next();
			criteria = new Criteria();
			criteria.addIn(WORKITEM, workItemIDChunk);
			criteria.add(PERSON,personID);
			try {
				basketList.addAll(doSelect(criteria));
			} catch(Exception e) {
				LOGGER.error("Loading the basket by workItemIDs failed with " + e.getMessage());
			}
		}
		return convertTorqueListToBeanList(basketList);
	}
	
	/**
	 * Saves a PersonBasketBean in the TPersonBasket table
	 * @param personBasketBean
	 * @return
	 */
	@Override
	public Integer save(TPersonBasketBean personBasketBean) {
		try {
			TPersonBasket tPersonBasket = BaseTPersonBasket.createTPersonBasket(personBasketBean);
			tPersonBasket.save();
			return tPersonBasket.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a person basket failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Deletes a basketBean from the TBasket table 
	 * @param basketID
	 * @param personID
	 */
	@Override
	public void delete(Integer basketID, Integer personID) {
		Criteria  criteria = new Criteria();
		criteria.add(BASKET, basketID);
		criteria.add(PERSON, personID);
		try {
			doDelete(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the person basket for basket " + basketID + " and person " + personID + " failed with " + e.getMessage());
		}
	}
	@Override
	public void emptyTrash(Integer personID){
		Criteria  selectCriteria = new Criteria();
		selectCriteria.add(PERSON, personID);
		selectCriteria.add(BASKET, TBasketBean.BASKET_TYPES.TRASH);
		Criteria updateCriteria = new Criteria();
		updateCriteria.add(BASKET, TBasketBean.BASKET_TYPES.DELETED);
		try {
			doUpdate(selectCriteria, updateCriteria);
		} catch (TorqueException e) {
			LOGGER.error("Empty the trash basket for person " + personID + " failed with " + e.getMessage());
		}
	}

	
	private List<TPersonBasketBean> convertTorqueListToBeanList(List<TPersonBasket> torqueList) {
		List<TPersonBasketBean> beanList = new LinkedList<TPersonBasketBean>();
		if (torqueList!=null) {
			for (TPersonBasket tPersonBasket : torqueList) {
				beanList.add(tPersonBasket.getBean());
			}
		}
		return beanList;
	}
}
