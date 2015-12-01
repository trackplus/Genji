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

import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.fieldType.design.IFieldTypeDT;
import com.aurel.track.fieldType.design.custom.picker.ItemPickerDT;
import com.aurel.track.fieldType.design.renderer.SelectRendererDT;
import com.aurel.track.fieldType.design.renderer.TypeRendererDT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.custom.picker.ItemPickerRT;
import com.aurel.track.fieldType.runtime.renderer.ItemPickerRendererRT;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;


/**
 * User picker
 * Not yet implemented completely so comment the "implements ICustomFieldType"
 * not to appear in the available field types box 
 * @author Tamas Ruff
 *
 */
public class CustomItemPicker extends FieldType {

	public static interface PARAMETERCODES {
		public static int DATASOURCE_OPTION = 0; //project/release or filter
		public static int PROJECT_RELEASE = 1; //project/release selected
		public static int FILTER = 2; //filter selected
		public static int INCLUDE_CLOSED = 3; //whether to include closed items
	}

	public static interface DATASOURCE_OPTIONS {
		public static int PROJECT_RELEASE = 1;
		public static int FILTER = 2;
	}

	
	@Override
	public IFieldTypeDT getFieldTypeDT() {
		return new ItemPickerDT(getPluginID());
	}

	@Override
	public String getIconName() {
		return FieldDesignBL.ICON_CLS.COMBO_ICONCLS;
	}

	@Override
	public TypeRendererDT getRendererDT() {
		return SelectRendererDT.getInstance();
	}

	@Override
	public IFieldTypeRT getFieldTypeRT() {
		return new ItemPickerRT();
	}

	@Override
	public TypeRendererRT getRendererRT() {
		return ItemPickerRendererRT.getInstance();
	}
	
	
}
