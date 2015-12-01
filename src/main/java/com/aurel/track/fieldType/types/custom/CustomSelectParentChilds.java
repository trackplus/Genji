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
import com.aurel.track.fieldType.design.custom.select.CustomSelectParentChildrenDT;
import com.aurel.track.fieldType.design.renderer.SelectParentChildrenRendererDT;
import com.aurel.track.fieldType.design.renderer.TypeRendererDT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.custom.select.CustomSelectParentChildrenRT;
import com.aurel.track.fieldType.runtime.renderer.SelectParentChildrenRendererRT;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;

public class CustomSelectParentChilds extends FieldType {

	@Override
	public String getIconName() {
		return FieldDesignBL.ICON_CLS.CASCADE_SELECT_ICONCLS;
	}

	@Override
	public IFieldTypeDT getFieldTypeDT() {
		return new CustomSelectParentChildrenDT(getPluginID());
	}
	
	@Override
	public TypeRendererDT getRendererDT() {
		return SelectParentChildrenRendererDT.getInstance();
	}

	@Override
	public IFieldTypeRT getFieldTypeRT() {		
		return new CustomSelectParentChildrenRT();
	}

	@Override
	public TypeRendererRT getRendererRT() {
		return SelectParentChildrenRendererRT.getInstance();
	}
}
