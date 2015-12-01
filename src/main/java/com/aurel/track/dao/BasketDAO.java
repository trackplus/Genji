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

import java.util.List;
import java.util.Set;

import com.aurel.track.beans.TBasketBean;

/**
 * DAO for Basket
 * @author Tamas Ruff
 */
public interface BasketDAO {
	/**
	 * Loads a basketBean by primary key 
	 * @param objectID
	 * @return
	 */
	TBasketBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Loads a basketBeans by keys 
	 * @param objectID
	 * @return
	 */
	List<TBasketBean> loadByPrimaryKeys(Set<Integer> objectIDs);
		
	/**
	 * Loads the main baskets   
	 * @return
	 */
	List<TBasketBean> loadMainBaskets();
		
	/**
	 * Loads child baskets of a parent basket   
	 * @return
	 */
	List<TBasketBean> loadChildBaskets(Integer basketID);

	/**
	 * Loadig the personal basket for an item
	 * @param itemID
	 * @param personID
	 * @return
	 */
	TBasketBean loadBasketByItemAndPerson(Integer itemID, Integer personID);
	/**
	 * Loadig the personal basket for  items
	 * @param itemIDs
	 * @param personID
	 * @return
	 */
	List<TBasketBean> loadBasketsByItemsAndPerson(int[] itemIDs,Integer personID);
	
	/**
	 * Saves a basketBean in the TBasket table
	 * @param basketBean
	 * @return
	 */
	Integer save(TBasketBean basketBean);
			
	/**
	 * Whether there are issues in this basket
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);
	
	/**
	 * Deletes a basketBean from the TBasket table 
	 * @param objectID
	 */
	void delete(Integer objectID);
	
	/**
	 * Replaces the dependences with a newBasketID and 
	 * deletes the oldBasketID from the TBasket table 
	 * @param oldBasketID
	 * @param newBasketID
	 */
	void replace(Integer oldBasketID, Integer newBasketID);
			
}
