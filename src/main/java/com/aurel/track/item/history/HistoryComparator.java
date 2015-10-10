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

package com.aurel.track.item.history;

import java.util.Comparator;
import java.util.Date;

/**
 * A comparator used to sort history beans
 */
public class HistoryComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1) {

		int sortDir = -1;   //  1 sorts in ascending order,
		                   //  -1 sorts in descending order (newest date comes first)

		Date left =  ((IChronologicalField)arg0).getLastEdit();
		Date right = ((IChronologicalField)arg1).getLastEdit();

		if((left == null) && (right == null)) {
			return 0;
		}

		if(left == null) {
			return sortDir * -1;
		}

		if(right == null) {
			return sortDir * 1;
		}

		return sortDir * left.compareTo(right);
	}

}
