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

import org.apache.torque.om.Persistent;

/** 
 */
public  class TBaseLine 
    extends com.aurel.track.persist.BaseTBaseLine
    implements Persistent {

    private static final long serialVersionUID = -3648452662278541815L;

    public boolean hasChanged(TBaseLine oldObject) {
		boolean result = false;
		if (oldObject == null) {
			if (this.getStartDate() != null
				|| this.getEndDate() != null) {
				result = true;
			}
			return result;
		}

		// The hasChanged test with the result true or false
		
		if (this.getStartDate() != null) {
			if (!this.getStartDate().equals(oldObject.getStartDate())) {
				result = true;
			}
		}
		else { // one is null pointer
			if (this.getStartDate() != oldObject.getStartDate()) {
				result = true;
			}
		}

		if (this.getEndDate() != null) { 
			if (!this.getEndDate().equals(oldObject.getEndDate())) {
				result = true;
			}
		}
		else {  // one is null pointer
			if (this.getEndDate() != oldObject.getEndDate()) {
				result = true;
			}
		}

	 	return result;
	}
}
