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
import com.aurel.track.resources.LocalizationKeyPrefixes;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TQueryRepositoryBean
    extends com.aurel.track.beans.base.BaseTQueryRepositoryBean
    implements ILabelBeanWithCategory, ILocalizedLabelBean, ISortedBean, Serializable {
	
	private static final long serialVersionUID = 1L;
	public static int LABEL_LENGTH = 100;

	/**
	 * 
	 */
	@Override
	public String getKeyPrefix() {
		return LocalizationKeyPrefixes.FILTER_LABEL_PREFIX;
	}
	
	public static interface REPOSITORY_TYPE {
		public static final int PRIVATE = 1;
		public static final int PUBLIC = 2;
		public static final int PROJECT = 3;
	}

	public static interface QUERY_PURPOSE {
		public static final int TREE_FILTER = 1;
		public static final int NOTIFY_FILTER = 2;
		public static final int TQLPLUS_FILTER = 3;
		public static final int TQL_FILTER = 4;
	}

	public static interface FIELD_MOMENT {
		public static final int NEW = 1;
		public static final int OLD = 2;
	}

	public static interface REPORT_QUERY_TYPE {
		public static final int TREE_FILTER = 1;
		public static final int TQL_FILTER = 2;
	}

	public boolean isIncludeInMenu() {
		return BooleanFields.fromStringToBoolean(getMenuItem());
	}
	
	public void setIncludeInMenu(boolean include) {		
		setMenuItem(BooleanFields.fromBooleanToString(include));				
	}
	
	/**
	 * Gets the comparable for ordering
	 */
	@Override
	public Comparable getSortOrderValue() {
		//although it is sorted when read from DB the filtername can be localized
		//that's why after localizing it should be sorted again 
		//once sortOrder is added at DB level this is not needed any more
		return getLabel();
	}
}
