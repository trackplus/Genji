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

package com.aurel.track.screen;

/**
 * Define the system actions constants
 */
public interface SystemActions {
	//pseudo actions
	public static final int COPY=-1;
	public static final int PRINT=-2;
	public static final int NEW=1;
	public static final int EDIT=2;
	public static final int MOVE=3;
	public static final int NEW_CHILD=4;
	public static final int CHANGE_STATUS=5;
	//public static final int ADD_COMMENT=6;
	public static final int NEW_LINKED_ITEM=6;
	public static final int ADD_EXPENSE=10;
	
	
}
