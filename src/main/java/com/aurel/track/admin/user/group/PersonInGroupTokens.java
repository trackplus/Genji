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

package com.aurel.track.admin.user.group;


/**
 * The structure of a group assignment node's id field
 * @author Tamas
 *
 */
public class PersonInGroupTokens {
	private Integer groupID;
	//if null then groupNode
	private Integer personID;
		
	public PersonInGroupTokens() {
		super();
	}
	
	/**
	 * Constructor	
	 * @param groupID
	 */
	public PersonInGroupTokens(Integer groupID) {
		super();		
		this.groupID = groupID;
	}

	/**
	 * Constructor
	 * @param groupID
	 * @param personID
	 */
	public PersonInGroupTokens(Integer groupID, Integer personID) {
		super();
		this.groupID = groupID;
		this.personID = personID;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
	
}
