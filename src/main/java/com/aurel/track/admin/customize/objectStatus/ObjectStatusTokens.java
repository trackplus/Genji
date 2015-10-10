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

package com.aurel.track.admin.customize.objectStatus;


/**
 * The structure of a object status option's id field
 * @author Tamas
 *
 */
public class ObjectStatusTokens {
	//for tree nodes only listID is specified
	private Integer listID;	
	//for grid rows both listId and optionId are specified
	private Integer optionID;
			
	public ObjectStatusTokens() {
		super();
	}

	public ObjectStatusTokens(Integer listID, Integer optionID) {
		super();
		this.listID = listID;
		this.optionID = optionID;
	}

	public Integer getListID() {
		return listID;
	}

	public void setListID(Integer listID) {
		this.listID = listID;
	}

	public Integer getOptionID() {
		return optionID;
	}

	public void setOptionID(Integer optionID) {
		this.optionID = optionID;
	}
	
	
}
