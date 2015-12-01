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

import com.aurel.track.beans.TScreenFieldBean;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.AbstractFieldBL;
import com.aurel.track.screen.item.adapterDAO.ItemScreenFactory;

public class ScreenFieldRuntimeBL extends AbstractFieldBL {
	//singleton isntance
	private static ScreenFieldRuntimeBL instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static ScreenFieldRuntimeBL getInstance() {
		if (instance == null) {
			instance = new ScreenFieldRuntimeBL();
		}
		return instance;
	}

    @Override
	protected ScreenFactory getScreenFactory() {
        return ItemScreenFactory.getInstance();
    }

    /**
	 * constructor
	 */
	public ScreenFieldRuntimeBL() {
		super();
	}
	/**
	 * Get the runtime type rendere for a field
	 * @param fs
	 * @return
	 */
	public  TypeRendererRT getTypeRendererRT(TScreenFieldBean fs){
		FieldType fieldType = FieldTypeManager.getInstance().getType(fs.getField());
		fieldType.setFieldID(fs.getField());
		return fieldType.getRendererRT();
	}
}
