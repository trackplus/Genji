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

package com.aurel.track.itemNavigator.layout;

import java.util.LinkedList;
import java.util.List;

import com.aurel.track.itemNavigator.layout.column.ColumnFieldTO;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.itemNavigator.layout.group.SortFieldTO;

/**
 * Transfer object for complete layout
 * @author Tamas
 *
 */
public class LayoutTO {
	private List<ColumnFieldTO> longFields;
	private List<ColumnFieldTO> shortFields;
	private List<GroupFieldTO> groupFields;
	private SortFieldTO sortField;
	private boolean bulkEdit;
	private boolean indexNumber;
	
	public List<ColumnFieldTO> getColumnFields() {
		List<ColumnFieldTO> columnFields = new LinkedList<ColumnFieldTO>();
		if (shortFields!=null) {
			columnFields.addAll(shortFields);
		}
		if (longFields!=null) {
			columnFields.addAll(longFields);
		}
		return columnFields;
	}
	
	public List<GroupFieldTO> getGroupFields() {
		return groupFields;
	}
	
	public void setGroupFields(List<GroupFieldTO> groupFields) {
		this.groupFields = groupFields;
	}
	
	public SortFieldTO getSortField() {
		return sortField;
	}
	
	public void setSortField(SortFieldTO sortField) {
		this.sortField = sortField;
	}
	
	public List<ColumnFieldTO> getLongFields() {
		return longFields;
	}
	
	public void setLongFields(List<ColumnFieldTO> longFields) {
		this.longFields = longFields;
	}
	
	public List<ColumnFieldTO> getShortFields() {
		return shortFields;
	}
	
	public void setShortFields(List<ColumnFieldTO> shortFields) {
		this.shortFields = shortFields;
	}
	
	public boolean isBulkEdit() {
		return bulkEdit;
	}
	
	public void setBulkEdit(boolean bulkEdit) {
		this.bulkEdit = bulkEdit;
	}
	
	public boolean isIndexNumber() {
		return indexNumber;
	}
	
	public void setIndexNumber(boolean indexNumber) {
		this.indexNumber = indexNumber;
	}
	
}
