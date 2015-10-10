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

import com.aurel.track.beans.TSystemStateBean;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Tue Jun 15 21:31:34 CEST 2004]
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public  class TAccount
    extends com.aurel.track.persist.BaseTAccount
    implements Persistent
{

    private static final long serialVersionUID = 4724290625828331271L;
    
    public String getFullName()
    {
    	String accountNumber = getAccountNumber();
    	String accountName = getAccountName();
    	String label;
    	if (accountNumber==null)
    	{
    		accountNumber = "";
    	}
    	if (accountName==null)
    	{
    		accountName = "";
    	}
    	label = accountNumber + " - " + accountName;
    	try
    	{
    		
    	if (this.getTSystemState().getStateflag().intValue() == TSystemStateBean.STATEFLAGS.INACTIVE)
    	{
    		label += " *";
    	}
    	}
    	catch(Exception e)
    	{    		
    	}
    	return label;
    }
    
    public String getFullName(boolean inactive)
    {
    	String accountNumber = getAccountNumber();
    	String accountName = getAccountName();
    	String label;
    	if (accountNumber==null)
    	{
    		accountNumber = "";
    	}
    	if (accountName==null)
    	{
    		accountName = "";
    	}
    	label = accountNumber + " - " + accountName;
    	if (inactive)
    	{
    		label += " *";
    	}
    	return label;
    }
    
    public static String getFullName(String accountNumber, String accountName)
    {

    	if (accountNumber==null)
    	{
    		accountNumber = "";
    	}
    	if (accountName==null)
    	{
    		accountName = "";
    	}
    	return accountNumber + " - " + accountName;    	    	
    }
	
}
