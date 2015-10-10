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



package com.aurel.track.beans;

/**
 * Interface to define the object ID of a bean
 * Used by DropDown container
 * It is just a marker interface because the getObjectID() 
 * is implemented anyway in the generated beans,
 * but unfortunately they do not have a common base class
 * which contains this method, so we put this marker interface to avoid reflection
 * @author Tamas Ruff
 *
 */
public interface IBeanID {
	/**
	 * Gets the objectID of the bean
	 * @return
	 */
	Integer getObjectID();
	
	/**
	 * Sets the objectID
	 */
	void setObjectID(Integer objectID);
}
