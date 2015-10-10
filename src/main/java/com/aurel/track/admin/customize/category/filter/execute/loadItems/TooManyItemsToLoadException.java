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

package com.aurel.track.admin.customize.category.filter.execute.loadItems;

/**
 * Exception thrown when too may items are to be loaded
 * @author Tamas
 *
 */
public class TooManyItemsToLoadException extends Exception {
		
	private static final long serialVersionUID = 1L;

	private int itemCount = 0;
	
	public TooManyItemsToLoadException(String s, int itemCount) {
		super(s);
		this.itemCount = itemCount;
	}

	public TooManyItemsToLoadException(String message, Exception cause) {
		super(message, cause);
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	
	 

}
