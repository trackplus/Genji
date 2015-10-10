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

import java.util.LinkedList;
import java.util.List;

import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.fieldType.design.IFieldTypeDT;
import com.aurel.track.fieldType.design.custom.select.CustomSelectSimpleDT;
import com.aurel.track.fieldType.design.renderer.SelectRendererDT;
import com.aurel.track.fieldType.design.renderer.TypeRendererDT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.custom.select.CustomExtensibleSelectRT;
import com.aurel.track.fieldType.runtime.renderer.ExtensibleSelectRendererRT;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;

public class CustomExtensibleSelect extends FieldType {
	
	@Override
	public String getIconName() {
		return FieldDesignBL.ICON_CLS.COMBO_ICONCLS;
	}

	@Override
	public IFieldTypeDT getFieldTypeDT() {
		return new CustomSelectSimpleDT(getPluginID());
	}
	
	@Override
	public TypeRendererDT getRendererDT() {
		return SelectRendererDT.getInstance();
	}

	@Override
	public IFieldTypeRT getFieldTypeRT() {		
		return new CustomExtensibleSelectRT();
	}

	@Override
	public TypeRendererRT getRendererRT() {
		return ExtensibleSelectRendererRT.getInstance();
	}
	
	/**
	 * Get the list of compatible field types
	 * Compatibility can be two way (simple list <-> editable list, or user picker <-> on behalf picker) or only one way (long text -> rich text)
	 * @return
	 */
	public List<FieldType> getCompatibleFieldTypes() {
		List<FieldType> compatibleFieldTypes = new LinkedList<FieldType>();
		compatibleFieldTypes.add(new CustomSelectSimple());
		compatibleFieldTypes.add(new CustomSelectMultiple());
		return compatibleFieldTypes;
	}
}