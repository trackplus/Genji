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


package com.aurel.track.item.action;

import com.aurel.track.beans.TWorkItemBean;

public class EditItemActionPlugin extends AbstractPluginItemAction{
	@Override
	public boolean isEnabled(Integer personID, TWorkItemBean workItem,
			boolean allowedToChange, boolean allowedToCreate, int appEdition) {					
		if (workItem==null || workItem.getObjectID()==null) {
			//no status change for new issues
			return false;
		}												
		return allowedToChange;		
	}
}
