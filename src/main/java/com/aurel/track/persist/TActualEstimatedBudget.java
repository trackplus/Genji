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


import java.util.Hashtable;

import org.apache.torque.om.Persistent;

import com.aurel.track.util.EqualUtils;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Mon Mar 27 14:21:55 EEST 2006]
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public  class TActualEstimatedBudget
    extends com.aurel.track.persist.BaseTActualEstimatedBudget
    implements Persistent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6222255036706463667L;
	private Hashtable changedAttributes = new Hashtable();
	
	public void copyValues(TActualEstimatedBudget lastEstimatedRemainingBudget)
	{
		if (lastEstimatedRemainingBudget!=null)
		{
			this.setEstimatedHours(lastEstimatedRemainingBudget.getEstimatedHours());
			this.setTimeUnit(lastEstimatedRemainingBudget.getTimeUnit());
			this.setEstimatedCost(lastEstimatedRemainingBudget.getEstimatedCost());			
		}
	}
	
	public boolean hasChanged(TActualEstimatedBudget lastEstimatedRemainingBudget) {
						
		changedAttributes.clear();

        boolean result = false;

		if (lastEstimatedRemainingBudget == null) {
			
			changedAttributes.put("FirstEstimatedRemainingBudget", new Boolean(true)); 
			return true;
		}

		if (EqualUtils.notEqual(this.getEstimatedHours(), lastEstimatedRemainingBudget.getEstimatedHours())) {			
			changedAttributes.put("Effort", new Boolean(true)); 
			result = true;
		}

		if (EqualUtils.notEqual(this.getTimeUnit(), lastEstimatedRemainingBudget.getTimeUnit())) {			
			changedAttributes.put("TimeUnit", new Boolean(true)); 
			result = true;
		}

		if (EqualUtils.notEqual(this.getEstimatedCost(), lastEstimatedRemainingBudget.getEstimatedCost())) {			
			changedAttributes.put("Cost", new Boolean(true)); 
			result = true;
		}
				
		return result;
	}
}
