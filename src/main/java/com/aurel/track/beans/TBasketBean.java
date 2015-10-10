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

import com.aurel.track.resources.LocalizationKeyPrefixes;

import java.io.Serializable;

/**
 * WorkItem baskets for persons
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TBasketBean
    extends com.aurel.track.beans.base.BaseTBasketBean
    implements Serializable, ILocalizedLabelBean
{
	public static final long serialVersionUID = 400L;
	
	public static interface  BASKET_TYPES{
		public static final int IN_BASKET=1;
		public static final int PLANNED_ITEMS=2;
		public static final int NEXT_ACTIONS=3;
		public static final int CALENDAR=4;
		public static final int DELEGATED=5;
		public static final int TRASH=6;
		public static final int INCUBATOR=7;
		public static final int REFERENCE=8;
		public static final int DELETED=-1;
	}
	
	/**
	 * 
	 */
	public String getKeyPrefix() {
		return LocalizationKeyPrefixes.BASKET_KEYPREFIX;
	}
}
