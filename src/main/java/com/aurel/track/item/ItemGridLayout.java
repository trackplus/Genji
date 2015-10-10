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


package com.aurel.track.item;

public class ItemGridLayout {
	public static interface COLUMN_TYPE{
		public static final int STRING=0;
		public static final int DATETIME=1;
		public static final int INTEGER=2;
		public static final int DOUBLE=3;
		public static final int BYTES=4;
		public static final int IMAGE=5;
		public static final int DATE=6;
	}
	private int id;
	private String header;
	private boolean sortable;
	private String dataIndex;
	private int type;
	private int width;
	private boolean hidden;
	private String direction;
	private boolean grouping;
	public ItemGridLayout(){
	}
	public ItemGridLayout(String dataIndex, String header, int id,
			boolean sortable, int type, int width) {
		super();
		this.dataIndex = dataIndex;
		this.header = header;
		this.id = id;
		this.sortable = sortable;
		this.type = type;
		this.width = width;
		this.hidden=false;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public boolean isSortable() {
		return sortable;
	}
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	public String getDataIndex() {
		return dataIndex;
	}
	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public boolean isGrouping() {
		return grouping;
	}
	public void setGrouping(boolean grouping) {
		this.grouping = grouping;
	}
	@Override
	public String toString() {
		return "ItemGridLayout(id="+id+", width="+width+", hidden="+hidden+")";
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemGridLayout) {
			return id == ((ItemGridLayout)obj).getId();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
