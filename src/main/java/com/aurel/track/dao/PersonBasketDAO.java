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

package com.aurel.track.dao;

import com.aurel.track.beans.TPersonBasketBean;

import java.util.Date;
import java.util.List;

/**
 * DAO for PersonBasket
 * @author Tamas Ruff
 */
public interface PersonBasketDAO {
	/**
	 * Loads a personBasketBeans by basket and person  
	 * @param basketID
	 * @param personID
	 * @return
	 */
	List<TPersonBasketBean> loadByBasketAndPerson(Integer basketID, Integer personID);
	
	/**
	 * Loads a personBasketBeans by person for a date
	 * @param personID
	 * @param date
	 * @return
	 */
	List<TPersonBasketBean> loadPersonBasketItemsByDate(Integer personID, Date date);
	
	/**
	 * Loads a personBasketBeans by person in a time interval
	 * @param personID
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	List<TPersonBasketBean> loadPersonBasketItemsByTime(Integer personID, Date dateFrom, Date dateTo);

	/**
	 * Loadig the personal basket for an item
	 * @param itemID
	 * @param personID
	 * @return
	 */
	TPersonBasketBean loadBasketByItemAndPerson(Integer itemID,Integer personID);
	/**
	 * Loadig the personal baskets for  items
	 * @param workItemIDs
	 * @param personID
	 * @return
	 */
	List<TPersonBasketBean> loadBasketsByItemsAndPerson(List<Integer> workItemIDs,Integer personID);

	/**
	 * Saves a PersonBasketBean in the TPersonBasket table
	 * @param personBasketBean
	 * @return
	 */
	Integer save(TPersonBasketBean personBasketBean);
			
	/**
	 * Deletes a basketBean from the TBasket table 
	 * @param basketID
	 * @param personID
	 */
	void delete(Integer basketID, Integer personID);
	void emptyTrash(Integer personID);
}
