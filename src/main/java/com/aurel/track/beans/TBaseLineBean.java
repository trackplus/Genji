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

import com.aurel.track.item.history.HistoryBean;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TBaseLineBean
    extends com.aurel.track.beans.base.BaseTBaseLineBean
    implements Serializable, HistoryBean {
	public static final long serialVersionUID = 400L;
	
	private String changedByName;
	
	/**
	 * Get the type of the history bean
	 * It should be one of the HISTORY_TYPE constants
	 * @return
	 */
	@Override
	public int getType() {
		return HistoryBean.HISTORY_TYPE.BASELINE_CHANGE;
	}
	
	/**
    * The name of the person who made the modification on a workItem
    * @return
    */
    @Override
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
	@Override
	public String getDescription() {
		return getReasonForChange();
	}
	
	/**
     * Set the description of the change
     * @return
     */
    @Override
    public void setDescription(String description) {
    	setReasonForChange(description);
    }
}
