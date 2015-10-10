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

import java.util.List;

import com.trackplus.model.Tbasket;

/**
 * DAO for Basket
 * 
 * @author Tamas Ruff
 */
public interface BasketDAO {
	/**
	 * Loads a basketBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tbasket loadByPrimaryKey(Integer objectid);

	/**
	 * Loads the main baskets
	 * 
	 * @return
	 */
	List<Tbasket> loadMainBaskets();

	/**
	 * Loads child baskets of a parent basket
	 * 
	 * @return
	 */
	List<Tbasket> loadChildBaskets(Integer basketid);

	/**
	 * Loadig the personal basket for an item
	 * 
	 * @param itemID
	 * @param personID
	 * @return
	 */
	Tbasket loadBasketByItemAndPerson(Integer itemid, Integer personid);

	/**
	 * Loadig the personal basket for items
	 * 
	 * @param itemIDs
	 * @param personID
	 * @return
	 */
	List<Tbasket> loadBasketsByItemsAndPerson(int[] itemids, Integer personid);

	/**
	 * Saves a basketBean in the Tbasket table
	 * 
	 * @param basketBean
	 * @return
	 */
	Integer save(Tbasket basket);

	/**
	 * Whether there are issues in this basket
	 * 
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);

	/**
	 * Deletes a basket from the Tbasket table
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Replaces the dependences with a newBasketID and deletes the oldBasketID
	 * from the Tbasket table
	 * 
	 * @param oldBasketID
	 * @param newBasketID
	 */
	void replace(Integer oldBasketID, Integer newBasketID);

}
