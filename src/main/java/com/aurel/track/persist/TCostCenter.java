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


package com.aurel.track.persist;


import org.apache.torque.om.Persistent;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Mon Mar 27 12:21:16 EEST 2006]
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public  class TCostCenter
    extends com.aurel.track.persist.BaseTCostCenter
    implements Persistent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6504832319379231471L;
	
	public String getFullName()
    {
    	String number = getCostcenterNumber();
    	String name = getCostcenterName();
    	String label;
    	if (number==null)
    	{
    		number = "";
    	}
    	if (name==null)
    	{
    		name = "";
    	}
    	label = number + " - " + name;    	
    	return label;
    }
	
	public static String getFullName(String number, String name)
    {

    	if (number==null)
    	{
    		number = "";
    	}
    	if (name==null)
    	{
    		name = "";
    	}
    	return number + " - " + name;    	    	
    }
	
}
