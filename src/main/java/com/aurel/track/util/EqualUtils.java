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


package com.aurel.track.util;

import java.util.Calendar;
import java.util.Date;

public class EqualUtils {

	public static boolean notEqual(Integer iNew, Integer iOld) {
	    if (iNew == null && iOld == null) {
	    	return false; // equal
	    }
	    if (iNew == null || iOld == null) {
	    	return true;  // not equal
	    }
	    return iNew.intValue() != iOld.intValue();
	}

	public static boolean equal(Integer iNew, Integer iOld) {
	   return !notEqual(iNew, iOld);
	}

	public static boolean notEqual(Double dNew, Double dOld) {
	    if (dNew == null && dOld == null) {
	    	return false; // equal
	    }
	    if (dNew == null || dOld == null) {
	    	return true;  // not equal
	    }
	    return Double.compare(dNew.doubleValue(), dOld.doubleValue()) != 0;
	}

	public static boolean notEqual(String sNew, String sOld) {
	    if (sNew == null && sOld == null) {
	    	return false; // equal
	    }
	    if (sNew == null || sOld == null) {
	    	return true;  // not equal
	    }
	    return !sNew.equals(sOld);
	}
	public static boolean equal(String sNew, String sOld) {
		return !notEqual(sNew, sOld);
	}
	public static boolean equalStrict(String sNew, String sOld) {
		if(sNew==null||sNew.trim().length()==0||sOld==null||sOld.trim().length()==0){
			return false;
		}
		return sNew.equals(sOld);
	}


	/**
	 * Important to convert to the long value because when one is
	 * java.util.Date and the other java.util.Timestamp then the equals returns false
	 * also if the values contained are equal
	 * (Ex. workItemOld contains a Timestamp object after loading from the database
	 * while the edited workItem contains Date object after submit)
	 * @param dNew
	 * @param dOld
	 * @return
	 */
	public static boolean notEqual(Date dNew, Date dOld) {
	    if (dNew == null && dOld == null) {
	    	return false; // equal
	    }
	    if (dNew == null || dOld == null) {
	    	return true;  // not equal
	    }
	    /* Important to compare the long values because when one is
		 * java.util.Date and the other java.util.Timestamp then
		 * the equals returns true or false depending on
		 *  the first operand, even if the long values are equal
		 * (newValue.equals(oldValue) returns true because
		 * a Timestamp is a Date but oldValue.equals(newValue)
		 * returns false because a Date is not a Timestamp)
		 * (Ex. workItemOld contains a Timestamp object after loading from the database
		 * while the edited workItem contains Date object after submit)*/
	    return dNew.getTime()!=dOld.getTime();
	}

	/**
	 * Compare only the date part not the time part
	 * @param dNew
	 * @param dOld
	 * @return
	 */
	public static boolean notEqualDateNeglectTime(Date dNew, Date dOld) {
	    if (dNew == null && dOld == null) {
	    	return false; // equal
	    }
	    if (dNew == null || dOld == null) {
	    	return true;  // not equal
	    }
	    Calendar calNew = Calendar.getInstance();
	    calNew.setTime(dNew);
	    CalendarUtil.clearTime(calNew);

	    Calendar calOld = Calendar.getInstance();
	    calOld.setTime(dOld);
	    CalendarUtil.clearTime(calOld);

	   return calNew.getTime().getTime()!=calOld.getTime().getTime();
	}

	public static boolean notEqual(Boolean bNew, Boolean bOld) {
		if (bNew==null) {
			bNew = Boolean.FALSE;
		}
		if (bOld==null) {
			bOld = Boolean.FALSE;
		}
	    return bNew.booleanValue() != bOld.booleanValue();
	}

	/** Whether the value has modified
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean valueModified(Object o1, Object o2)
	{
		//the null value and the empty string "" are taken as equal
		if (o1==null) {
			o1="";
		}
		if (o2==null) {
			o2="";
		}
		return !o1.equals(o2);
	}
}
