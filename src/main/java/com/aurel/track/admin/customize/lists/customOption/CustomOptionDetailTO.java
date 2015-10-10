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

package com.aurel.track.admin.customize.lists.customOption;

import com.aurel.track.admin.customize.lists.OptionDetailBaseTO;

/**
 * Transfer object for a custom list option
 * @author Tamas
 *
 */
public class CustomOptionDetailTO extends OptionDetailBaseTO {		
	private Integer parentOptionID;
	private boolean defaultOption;
	
	public Integer getParentOptionID() {
		return parentOptionID;
	}

	public void setParentOptionID(Integer parentOptionID) {
		this.parentOptionID = parentOptionID;
	}

	public boolean isDefaultOption() {
		return defaultOption;
	}

	public void setDefaultOption(boolean defaultOption) {
		this.defaultOption = defaultOption;
	}		
}
