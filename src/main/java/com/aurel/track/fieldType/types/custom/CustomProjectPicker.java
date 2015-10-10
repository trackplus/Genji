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


package com.aurel.track.fieldType.types.custom;

import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.fieldType.design.IFieldTypeDT;
import com.aurel.track.fieldType.design.custom.picker.GeneralSettingsBaseDT;
import com.aurel.track.fieldType.design.renderer.SelectRendererDT;
import com.aurel.track.fieldType.design.renderer.TypeRendererDT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.custom.picker.ProjectPickerRT;
import com.aurel.track.fieldType.runtime.renderer.ProjectPickerRendererRT;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;


/**
 * Project picker
 * @author Tamas Ruff
 *
 */
public class CustomProjectPicker extends FieldType {

	@Override
	public IFieldTypeDT getFieldTypeDT() {
		return new GeneralSettingsBaseDT(getPluginID());
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
		return new ProjectPickerRT();
	}

	@Override
	public TypeRendererRT getRendererRT() {
		return ProjectPickerRendererRT.getInstance();
	}
	
	
}
