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
 * [Mon Mar 27 12:21:16 EEST 2006]
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public  class TBudget
    extends com.aurel.track.persist.BaseTBudget
    implements Persistent
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7778720314299374506L;
	private Hashtable changedAttributes = new Hashtable();
		
	/*public void copyValues(TBudget tBudget)
	{
		if (tBudget!=null)
		{
			this.setEstimatedHours(tBudget.getEstimatedHours());
			this.setTimeUnit(tBudget.getTimeUnit());
			this.setEstimatedCost(tBudget.getEstimatedCost());
			this.setChangeDescripton(tBudget.getChangeDescripton());
		}
	}*/
	
	public boolean hasChanged(TBudget lastBudget) {
						
		changedAttributes.clear();

        boolean result = false;

		if (lastBudget == null) {
			
			changedAttributes.put("FirstBudget", new Boolean(true)); 
			return true;
		}

		if (EqualUtils.notEqual(this.getEstimatedHours(), lastBudget.getEstimatedHours())) {			
			changedAttributes.put("Effort", new Boolean(true)); 
			result = true;
		}

		if (EqualUtils.notEqual(this.getTimeUnit(), lastBudget.getTimeUnit())) {			
			changedAttributes.put("TimeUnit", new Boolean(true)); 
			result = true;
		}

		if (EqualUtils.notEqual(this.getEstimatedCost(), lastBudget.getEstimatedCost())) {			
			changedAttributes.put("Cost", new Boolean(true)); 
			result = true;
		}
		
		if (EqualUtils.notEqual(this.getChangeDescription(), lastBudget.getChangeDescription())) {			
			changedAttributes.put("Description", new Boolean(true)); 
			result = true;
		}
		
		return result;
	}
		
}