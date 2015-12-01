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


package com.trackplus.dao;

import java.util.List;

import com.trackplus.model.Tlinktype;

/**
 * DAO for LinkType
 * 
 * @author Tamas Ruff
 * 
 */
public interface LinkTypeDAO {

	/**
	 * Loads a linkTypeBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tlinktype loadByPrimaryKey(Integer objectID);

	/**
	 * Whether exist links of a type
	 * 
	 * @param linkTypeID
	 * @return
	 */
	boolean hasLinksOfType(Integer linkTypeID);

	/**
	 * Load all link types of a specific type
	 * 
	 * @param linkTypeClass
	 * @param exceptLinkTypeID
	 * @return
	 */
	List<Tlinktype> loadByLinkType(String linkTypeClass,
			Integer exceptLinkTypeID);

	/**
	 * Loads all linkTypeBeans
	 * 
	 * @return
	 */
	List<Tlinktype> loadAll();

	/**
	 * Gets the list of Tworkitemlink by linkIDsList
	 * 
	 * @param linkTypeIDsList
	 * @return
	 */
	List<Tlinktype> loadByLinkIDs(List<Integer> linkTypeIDsList);

	/**
	 * Saves a new/existing linkTypeBean
	 * 
	 * @param linkTypeBean
	 * @return the created optionID
	 */
	Integer save(Tlinktype linkTypeBean);

	/**
	 * Deletes a linkTypeBean
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Replaces the dependences with a new linkTypeID and deletes the old
	 * linkTypeID from the TLinkType table
	 * 
	 * @param oldLinkTypeID
	 * @param newLinkTypeID
	 */
	void replaceAndDelete(Integer oldLinkTypeID, Integer newLinkTypeID);
}
