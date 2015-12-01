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

import com.trackplus.model.Taccount;

/**
 * This class implements the data access object (DAO) for work and cost
 * accounts. Accounts belong to cost centers, and can be assigned to projects.
 * Actual work and cost is written towards accounts.
 * 
 * @author Tamas Ruff
 * 
 */
public interface AccountDAO {

	/**
	 * Loads an {@link Taccount} by objectID.
	 * 
	 * @param objectIDs
	 * @return the {@link Taccount} with the given ObjectID
	 */
	Taccount loadByPrimaryKey(Integer objectID);

	/**
	 * Loads accounts by costcenter and label
	 * 
	 * @param costcenterID
	 * @param accountNumber
	 * @return
	 */
	List<Taccount> loadByNumber(Integer costcenterID, String accountNumber);

	/**
	 * Gets all accounts
	 * 
	 * @return
	 */
	List<Taccount> loadAll();

	/**
	 * Gets all accounts for a costcenter
	 * 
	 * @return
	 */
	List<Taccount> loadByCostcenter(Integer costcenterID);

	/**
	 * Loads a list of {@link Taccount} by objectID list
	 * 
	 * @param objectIDs
	 * @return list of {@link Taccount} with the given objectIDs
	 */
	List<Taccount> loadByKeys(List<Integer> objectIDs);

	/**
	 * Loads a list of {@link Taccount} by costcenters
	 * 
	 * @param costcenters
	 * @return
	 */
	List<Taccount> loadByCostcenters(Integer[] costcenters);

	/**
	 * Loads the accounts with a specific stateflag which are present in a
	 * project
	 * 
	 * @param project
	 * @param stateflag
	 * @return list of {@link Taccount} beans in that project which match the
	 *         given stateflag
	 */
	List<Taccount> loadByProjectForStateflag(Integer project, int stateflag);

	/**
	 * Loads the accounts with a specific stateflag
	 * 
	 * @param stateflag
	 * @return list of {@link Taccount} beans in that project which match the
	 *         given stateflag
	 */
	List<Taccount> loadByStateflag(int stateflag);

	/**
	 * Loads the {@link Taccount} list by workItemKeys (issue numbers).
	 * 
	 * @param workItemKeys
	 * @return list of account beans
	 */
	List<Taccount> loadByWorkItemKeys(int[] workItemKeys);

	/**
	 * Saves an account to the Taccount table.
	 * 
	 * @param accountBean
	 * @return
	 */
	Integer save(Taccount accountBean);

	/**
	 * Deletes an account from the database
	 * 
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);

	/**
	 * Returns whether the account has dependent data
	 * 
	 * @param objectID
	 * @return
	 */
	boolean hasDependentData(Integer objectID);

	/**
	 * Replaces all occurrences of the oldID with the newID and then deletes the
	 * oldID account
	 * 
	 * @param oldID
	 * @param newID
	 * @return
	 */
	void replaceAndDelete(Integer oldID, Integer newID);
}
