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

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TComputedValuesBean
    extends com.aurel.track.beans.base.BaseTComputedValuesBean
    implements Serializable
{
	public static final long serialVersionUID = 400L;
	
	/**
	 * The type of the effort
	 * Now hardcoded, in the future new effortypes might be added dynamically 
	 * @author Tamas Ruff
	 *
	 */
	public interface EFFORTTYPE {
		public static final int TIME = 1;
		public static final int COST = 2;		
	}
	
	/**
	 * The type of the computed value
	 * Now hardcoded, in the future new effortypes might be added dynamically
	 * @author Tamas Ruff
	 *
	 */
	public interface COMPUTEDVALUETYPE {
		public static final int EXPENSE = 1;
		public static final int PLAN = 2;	
		//public static final int REMAININGBUDGET = 3;
		public static final int BUDGET = 4;
	}		
}
