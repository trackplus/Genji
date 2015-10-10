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


package com.aurel.track.screen;

import com.aurel.track.beans.screen.IField;

/**
* A wraper for field
* Contain the field the position of field(row,col) ant the htmlString
* @author Adrian Bojani
*
*/
public abstract class FieldWrapper{
	private IField field;
	private String jsClass;
	private String jsonData;
	private int row;
	private int col;
	
	private String htmlString;
	private String tooltip;
	
	private String fieldType;
	private boolean invisible;
	
	
	public IField getField() {
		return field;
	}
	public void setField(IField field) {
		this.field = field;
	}
	public String getJsClass() {
		return jsClass;
	}
	public void setJsClass(String jsClass) {
		this.jsClass = jsClass;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public String getHtmlString() {
		return htmlString;
	}
	public void setHtmlString(String htmlString) {
		this.htmlString = htmlString;
	}
		public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	public boolean isInvisible() {
		return invisible;
	}
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}
}
