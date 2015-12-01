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


package com.aurel.track.beans;

import java.io.Serializable;

import com.aurel.track.fieldType.constants.BooleanFields;

/**
 * grouping and sorting in item navigator
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TNavigatorGroupingSortingBean
    extends com.aurel.track.beans.base.BaseTNavigatorGroupingSortingBean
    implements Serializable
{
	/**
	 * true, if it is a grouping field
	 * @return 
	 */
	public boolean isGrouping() {
		return BooleanFields.fromStringToBoolean(getIsGrouping());
	}
	
	/**
	 * set grouping
	 * @return 
	 */
	public void setGrouping(boolean isGrouping) {
		setIsGrouping(BooleanFields.fromBooleanToString(isGrouping));
	}
	
	/**
	 * true, if order is descending
	 * @return 
	 */
	public boolean isDescending() {
		return BooleanFields.fromStringToBoolean(getIsDescending());
	}
	
	/**
	 * set descending
	 * @return 
	 */
	public void setDescending(boolean isDescending) {
		setIsDescending(BooleanFields.fromBooleanToString(isDescending));
	}
	
	/**
	 * true, if group is collapsed
	 * @return 
	 */
	public boolean isCollapsed() {
		return BooleanFields.fromStringToBoolean(getIsCollapsed());
	}
	
	/**
	 * set collapsed
	 * @return 
	 */
	public void setCollapsed(boolean isCollapsed) {
		setIsCollapsed(BooleanFields.fromBooleanToString(isCollapsed));
	}
}
