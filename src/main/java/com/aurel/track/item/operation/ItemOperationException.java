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

package com.aurel.track.item.operation;

/**
 */
public class ItemOperationException extends Exception {
	public static final long serialVersionUID = 400L;
	
	public static final int TYPE_COMMON=0;
	public static final int TYPE_MASS_OPERATION=1;
	private int type;
	public ItemOperationException(int type,String message) {
		super(message);
		this.type=type;
	}
	public ItemOperationException(int type,Exception cause) {
		super(cause);
		this.type=type;
	}
	public ItemOperationException(String message) {
		super(message);
		this.type=TYPE_COMMON;
	}
	public ItemOperationException(Exception cause) {
		super(cause);
		this.type=TYPE_COMMON;
	}

	public int getType() {
		return type;
	}
}
