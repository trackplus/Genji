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

import com.trackplus.model.Tcomputedvalue;

public interface ComputedValuesDAO {

	/**
	 * Loads a computedValues from the Tcomputedvalue table by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tcomputedvalue loadByPrimaryKey(Integer objectid);

	/**
	 * Loads a computedValues from the Tcomputedvalue table by workitem, types
	 * and person
	 * 
	 * @param workItemID
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	Tcomputedvalue loadByWorkitemAndtTypesAndPerson(Integer workitemid,
			int efforttype, int computedvaluetype, Integer person);

	/**
	 * Loads a computedValues from the Tcomputedvalue table for workitems, types
	 * and person
	 * 
	 * @param workItemIDs
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	List<Tcomputedvalue> loadByWorkitemAndtTypesAndPerson(int[] workitemids,
			int efforttype, int computedvaluetype, Integer person);

	/**
	 * Loads a computedValues from the Tcomputedvalue table for workitems and
	 * types
	 * 
	 * @param workItemIDs
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	List<Tcomputedvalue> loadByWorkitemAndtTypes(int[] workitemids,
			int efforttype, int computedvaluetype);

	/**
	 * Saves a new/existing computedValues in the Tcomputedvalue table
	 * 
	 * @param computedValues
	 * @return the created optionID
	 */
	Integer save(Tcomputedvalue computedvalues);

	/**
	 * Deletes a record from the Tcomputedvalue table
	 * 
	 * @param objectID
	 */
	void delete(Integer objectID);

	/**
	 * Deletes all records from the Tcomputedvalue table
	 * 
	 * @param objectID
	 */
	void deleteAll();
}
