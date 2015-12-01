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

package com.aurel.track.admin.user.department;


/**
 * The structure of a department and person in department node's id field
 * @author Tamas
 *
 */
public class PersonInDepartmentTokens {	
	private Integer departmentID;
	//if null then departmentID
	private Integer personID;
		
	public PersonInDepartmentTokens() {
		super();
	}
	
	/**
	 * Constructor	
	 * @param departmentID
	 */
	public PersonInDepartmentTokens(Integer departmentID) {
		super();
		this.departmentID = departmentID;
	}

	/**
	 * Constructor
	 * @param departmentID
	 * @param personID
	 */
	public PersonInDepartmentTokens(Integer departmentID, Integer personID) {
		super();
		this.departmentID = departmentID;
		this.personID = personID;
	}

	public Integer getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(Integer departmentID) {
		this.departmentID = departmentID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
	
}
