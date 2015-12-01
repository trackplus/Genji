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

package com.aurel.track.screen.item.bl.runtime;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.AbstractScreenJSON;
import com.aurel.track.screen.FieldWrapper;
import com.aurel.track.screen.item.ItemFieldWrapperRuntime;

public class ItemScreenRuntimeJSON extends AbstractScreenJSON{
	public ItemScreenRuntimeJSON(){
	}
	@Override
	protected void appendFieldProperties(FieldWrapper fieldWrapper,StringBuilder sb){
		ItemFieldWrapperRuntime itemFieldWrapperRuntime=(ItemFieldWrapperRuntime)fieldWrapper;
		JSONUtility.appendStringValue(sb,"label",itemFieldWrapperRuntime.getLabel());
		JSONUtility.appendIntegerValue(sb,"fieldID",itemFieldWrapperRuntime.getFieldID());
		JSONUtility.appendStringValue(sb,"extReadOnlyClassName",itemFieldWrapperRuntime.getExtReadOnlyClassName());
		JSONUtility.appendStringValue(sb,"labelHAlign",itemFieldWrapperRuntime.getLabelHAlign());
		JSONUtility.appendStringValue(sb,"labelVAlign",itemFieldWrapperRuntime.getLabelVAlign());
		JSONUtility.appendStringValue(sb,"valueHAlign",itemFieldWrapperRuntime.getValueHAlign());
		JSONUtility.appendStringValue(sb,"valueVAlign",itemFieldWrapperRuntime.getValueVAlign());
		JSONUtility.appendBooleanValue(sb,"hideLabel",itemFieldWrapperRuntime.isHideLabelBoolean());
		super.appendFieldProperties(fieldWrapper, sb);
	}
}
