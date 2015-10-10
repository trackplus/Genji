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

package com.aurel.track.fieldType.types.system.text;

import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.fieldType.design.IFieldTypeDT;
import com.aurel.track.fieldType.design.renderer.NumberRendererDT;
import com.aurel.track.fieldType.design.renderer.TypeRendererDT;
import com.aurel.track.fieldType.design.text.SystemDurationDT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.renderer.DurationRendererRT;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.runtime.system.text.SystemDurationRT;

/**
 * Field type for duration
 * @author Tamas
 *
 */
public class SystemDuration extends SystemTextBase {

	@Override
	public String getIconName() {
		return FieldDesignBL.ICON_CLS.TEXTFIELD_ICONCLS;
	}

	@Override
	public IFieldTypeDT getFieldTypeDT() {
		return new SystemDurationDT(getPluginID());
	}

	@Override
	public IFieldTypeRT getFieldTypeRT() {
		return new SystemDurationRT();
	}

	@Override
	public TypeRendererDT getRendererDT() {
		return NumberRendererDT.getInstance();
	}

	@Override
	public TypeRendererRT getRendererRT() {
		return DurationRendererRT.getInstance();
	}
}
