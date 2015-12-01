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

package com.aurel.track.fieldType.types.custom;

import java.util.LinkedList;
import java.util.List;

import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.custom.picker.OnBehalfOfPickerRT;
import com.aurel.track.fieldType.types.FieldType;

/**
 * An extension for user picker.
 * The selected person will inherit the originator RACI role 
 * @author Tamas Ruff
 *
 */
public class CustomOnBehalfOfPicker extends CustomUserPicker {
	@Override
	public IFieldTypeRT getFieldTypeRT() {
		return new OnBehalfOfPickerRT();
	}
	
	/**
	 * Get the list of compatible field types
	 * Compatibility can be two way (simple list <-> editable list, or user picker <-> on behalf picker) or only one way (long text -> rich text)
	 * @return
	 */
	@Override
	public List<FieldType> getCompatibleFieldTypes() {
		List<FieldType> compatibleFieldTypes = new LinkedList<FieldType>();
		compatibleFieldTypes.add(new CustomUserPicker());
		return compatibleFieldTypes;
	}
}
