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

package com.aurel.track.screen.item.bl.design;

import com.aurel.track.beans.TScreenFieldBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.AbstractScreenJSON;
import com.aurel.track.screen.FieldWrapper;
import com.aurel.track.screen.item.ItemFieldWrapper;

public class ItemScreenDesignJSON extends AbstractScreenJSON{
	public ItemScreenDesignJSON(){
	}
	@Override
	protected void appendFieldProperties(FieldWrapper fieldWrapper,StringBuilder sb){
		ItemFieldWrapper itemFieldWrapper=(ItemFieldWrapper)fieldWrapper;
		JSONUtility.appendStringValue(sb,"label",itemFieldWrapper.getLabel());
		JSONUtility.appendIntegerValue(sb,"fieldID",itemFieldWrapper.getFieldID());
		JSONUtility.appendStringValue(sb,"labelHAlign",itemFieldWrapper.getLabelHAlign());
		JSONUtility.appendStringValue(sb,"labelVAlign",itemFieldWrapper.getLabelVAlign());
		JSONUtility.appendStringValue(sb,"valueHAlign",itemFieldWrapper.getValueHAlign());
		JSONUtility.appendStringValue(sb,"valueVAlign",itemFieldWrapper.getValueVAlign());
		JSONUtility.appendBooleanValue(sb,"hideLabel",itemFieldWrapper.isHideLabelBoolean());

		super.appendFieldProperties(fieldWrapper, sb);
	}
	@Override
	protected String encodeFieldPropertiesExtra(IField field,String fieldType){
		TScreenFieldBean itemField=(TScreenFieldBean)field;
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendIntegerValue(sb,"field.labelHAlign",itemField.getLabelHAlign());
		JSONUtility.appendIntegerValue(sb,"field.labelVAlign",itemField.getLabelVAlign());
		JSONUtility.appendIntegerValue(sb,"field.valueHAlign",itemField.getValueHAlign());
		JSONUtility.appendIntegerValue(sb,"field.valueVAlign",itemField.getValueVAlign());
		JSONUtility.appendBooleanValue(sb,"field.hideLabelBoolean",itemField.isHideLabelBoolean());
		return sb.toString();
	}
}
