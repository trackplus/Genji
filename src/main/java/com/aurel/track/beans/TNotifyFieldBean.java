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

import com.aurel.track.fieldType.constants.BooleanFields;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TNotifyFieldBean
    extends com.aurel.track.beans.base.BaseTNotifyFieldBean
    implements Serializable
{
	public static final long serialVersionUID = 400L;
	
	public static final int ORIGINATOR = 1;
	public static final int MANAGER = 2;
	public static final int RESPONSIBLE = 3;
	public static final int CONSULTANT = 4;
	public static final int INFORMANT = 5;
	public static final int OBSERVER = 6;
	
	public boolean isOriginator() {
		return BooleanFields.fromStringToBoolean(getOriginator());
	}
	public void setOriginator(boolean originator) {
		setOriginator(BooleanFields.fromBooleanToString(originator));
	}
	
	public boolean isManager() {
		return BooleanFields.fromStringToBoolean(getManager());
	}
	public void setManager(boolean manager) {
		setManager(BooleanFields.fromBooleanToString(manager));
	}
	
	public boolean isResponsible() {
		return BooleanFields.fromStringToBoolean(getResponsible());
	}
	public void setResponsible(boolean responsible) {
		setResponsible(BooleanFields.fromBooleanToString(responsible));
	}
	
	public boolean isConsultant() {
		return BooleanFields.fromStringToBoolean(getConsultant());
	}
	public void setConsultant(boolean consultant) {
		setConsultant(BooleanFields.fromBooleanToString(consultant));
	}
	
	public boolean isInformant() {
		return BooleanFields.fromStringToBoolean(getInformant());
	}
	public void setInformant(boolean informant) {
		setInformant(BooleanFields.fromBooleanToString(informant));
	}
	
	public boolean isObserver() {
		return BooleanFields.fromStringToBoolean(getObserver());
	}
	public void setObserver(boolean observer) {
		setObserver(BooleanFields.fromBooleanToString(observer));
	}
	
}
