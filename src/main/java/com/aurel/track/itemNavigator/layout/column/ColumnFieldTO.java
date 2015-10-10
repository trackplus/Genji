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

package com.aurel.track.itemNavigator.layout.column;

import com.aurel.track.itemNavigator.layout.LayoutFieldTO;


/**
 * Transfer object for a layout column
 * @author Tamas
 *
 */
public class ColumnFieldTO extends LayoutFieldTO {
	private Integer fieldWidth;
	private boolean renderHeaderAsImg;
	private String headerImgName;
	private boolean renderContentAsImg;
	private boolean fieldIsCustomIcon;
	private boolean renderAsLong;
	
	public ColumnFieldTO() {
		super();
	}
	
	public ColumnFieldTO(Integer fieldID, String label) {
		super(fieldID, label);
	}
	
	public ColumnFieldTO(Integer fieldID, String name, String label) {
		super(fieldID, name, label);
	}
	
	public Integer getFieldWidth() {
		return fieldWidth;
	}
	public void setFieldWidth(Integer fieldWidth) {
		this.fieldWidth = fieldWidth;
	}
	public boolean isRenderHeaderAsImg() {
		return renderHeaderAsImg;
	}
	public void setRenderHeaderAsImg(boolean renderHeaderAsImg) {
		this.renderHeaderAsImg = renderHeaderAsImg;
	}
	public String getHeaderImgName() {
		return headerImgName;
	}
	public void setHeaderImgName(String headerImgName) {
		this.headerImgName = headerImgName;
	}
	public boolean isRenderContentAsImg() {
		return renderContentAsImg;
	}
	public void setRenderContentAsImg(boolean renderContentAsImg) {
		this.renderContentAsImg = renderContentAsImg;
	}
	public boolean isFieldIsCustomIcon() {
		return fieldIsCustomIcon;
	}
	public void setFieldIsCustomIcon(boolean fieldIsCustomIcon) {
		this.fieldIsCustomIcon = fieldIsCustomIcon;
	}
	public boolean isRenderAsLong() {
		return renderAsLong;
	}
	public void setRenderAsLong(boolean renderAsLong) {
		this.renderAsLong = renderAsLong;
	}
	
	
}
