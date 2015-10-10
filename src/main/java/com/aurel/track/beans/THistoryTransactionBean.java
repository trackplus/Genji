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
import java.util.Objects;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class THistoryTransactionBean
    extends com.aurel.track.beans.base.BaseTHistoryTransactionBean
    implements Serializable, IBeanID, Comparable
{
	public static final long serialVersionUID = 400L;

	private String changedByName;

	public String getChangedByName() {
		return changedByName;
	}

	public void setChangedByName(String changedByName) {
		this.changedByName = changedByName;
	}

	@Override
	public int compareTo(Object o) {
		try {
			THistoryTransactionBean historyTransactionBean = (THistoryTransactionBean)o;
			return this.getLastEdit().compareTo(historyTransactionBean.getLastEdit());
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof THistoryTransactionBean)) {
			return false;
		}
		return Objects.equals(((THistoryTransactionBean)obj).getLastEdit(), this.getLastEdit());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.getLastEdit() == null) ? 0 : this.getLastEdit().hashCode());
		return result;
	}
}
