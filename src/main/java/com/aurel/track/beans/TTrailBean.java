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

import java.io.Serializable;

import com.aurel.track.item.history.HistoryBean;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TTrailBean
    extends com.aurel.track.beans.base.BaseTTrailBean
    implements Serializable, HistoryBean {

	public static final long serialVersionUID = 400L;
	
	private String changedByName;
			
	/**
	 * Get the type of the history bean
	 * It should be one of the HISTORY_TYPE constants
	 * @return
	 */
	public int getType() {
		return HistoryBean.HISTORY_TYPE.TRAIL;
	}

	/**
     * The name of the person who made the modification on a workItem
     * @return
     */
	public String getChangedByName() {
		
		return changedByName;
	}

	/**
	 * @param changedByName the changedByName to set
	 */
	public void setChangedByName(String changedByName) {
		this.changedByName = changedByName;
	}
	
	/**
	 *  Get the description of the change
	 */
	public String getDescription() {
		return getChangeDescription();
	}
	
	/**
     * Set the description of the change
     * @return
     */
    public void setDescription(String description) {
    	setChangeDescription(description);
    }
}
