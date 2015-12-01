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


package com.aurel.track.persist;

import java.util.Locale;

import org.apache.torque.om.Persistent;

/** 
 */
public  class TStateChange 
    extends com.aurel.track.persist.BaseTStateChange
    implements Persistent {
	
    private static final long serialVersionUID = -682955872016001138L;
    protected Locale currentLocale = new Locale("de", "DE");  // default
	
	public boolean hasChanged (TStateChange oldObject) {
		boolean result = hasStateChanged(oldObject);
		if (descriptionWasChanged(oldObject)) {
			result = true;
		}
        return result;
	}

	public boolean descriptionWasChanged(TStateChange scBean) {
		if (scBean == null) {
			return true;
		}
		if (this.getChangeDescription() == null) {
			this.setChangeDescription("");
		}
		if (scBean.getChangeDescription() == null) {
			scBean.setChangeDescription("");
		}
		if (this.getChangeDescription().equals(
									   scBean.getChangeDescription())) {
			return false;
		}
		else {
			return true;
		}
	}

	public boolean hasStateChanged (TStateChange oldObject) {
		boolean result = false;

        if (oldObject == null) {
			return true;
		}

		if (this.getChangedToID().intValue() 
		                           != oldObject.getChangedToID().intValue()) { 
			result = true;
		}

        return result;
	}
	
    /** Return the last edited date. */

    
	public void setCurrentLocale(Locale clocale) {
		this.currentLocale = clocale;
	}
}
