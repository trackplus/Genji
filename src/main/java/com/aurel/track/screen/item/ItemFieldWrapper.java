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

package com.aurel.track.screen.item;

import com.aurel.track.screen.FieldWrapper;

public class ItemFieldWrapper extends FieldWrapper{
	private String labelHAlign,labelVAlign,valueHAlign,valueVAlign;
	private Integer fieldID;
	private String label;
	private String fieldType;
	private boolean hideLabelBoolean=false;

	public String getLabelHAlign() {
		return labelHAlign;
	}

	public void setLabelHAlign(String labelHAlign) {
		this.labelHAlign = labelHAlign;
	}

	public String getLabelVAlign() {
		return labelVAlign;
	}

	public void setLabelVAlign(String labelVAlign) {
		this.labelVAlign = labelVAlign;
	}

	public String getValueHAlign() {
		return valueHAlign;
	}

	public void setValueHAlign(String valueHAlign) {
		this.valueHAlign = valueHAlign;
	}

	public String getValueVAlign() {
		return valueVAlign;
	}

	public void setValueVAlign(String valueVAlign) {
		this.valueVAlign = valueVAlign;
	}

	public Integer getFieldID() {
		return fieldID;
	}

	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getFieldType() {
		return fieldType;
	}

	@Override
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public boolean isHideLabelBoolean() {
		return hideLabelBoolean;
	}

	public void setHideLabelBoolean(boolean hideLabelBoolean) {
		this.hideLabelBoolean = hideLabelBoolean;
	}
}
