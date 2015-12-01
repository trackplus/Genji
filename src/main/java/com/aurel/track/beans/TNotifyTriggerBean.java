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

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TNotifyTriggerBean
    extends com.aurel.track.beans.base.BaseTNotifyTriggerBean
    implements ILabelBean, Serializable
{
	public static final long serialVersionUID = 400L;
	
	//the localized label of the trigger type: system or own
	private String typeLabel;
	//it is his own trigger or other's trigger:
	//whether the delete button is available or not
	private boolean own;

	/**
	 * @return the typeLabel
	 */
	public String getTypeLabel() {
		return typeLabel;
	}

	/**
	 * @param typeLabel the typeLabel to set
	 */
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

	/**
	 * @return the own
	 */
	public boolean isOwn() {
		return own;
	}

	/**
	 * @param own the own to set
	 */
	public void setOwn(boolean own) {
		this.own = own;
	}
		
	
	
}
