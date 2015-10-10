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

package com.aurel.track.screen.item.bl.runtime;

import com.aurel.track.beans.TScreenFieldBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.screen.FieldWrapper;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.AbstractFieldBL;
import com.aurel.track.screen.bl.AbstractPanelBL;
import com.aurel.track.screen.item.ItemFieldWrapperRuntime;
import com.aurel.track.screen.item.adapterDAO.ItemScreenFactory;

/**
 * A Business Logic class for ScreenPanel for runtime
 * @author Adrian Bojani
 *
 */
public class ScreenPanelRuntimeBL extends AbstractPanelBL {

	private static ScreenPanelRuntimeBL instance;
	public static ScreenPanelRuntimeBL getInstance() {
		if (instance == null) {
			instance = new ScreenPanelRuntimeBL();
		}
		return instance;
	}

	@Override
	protected ScreenFactory getScreenFactory() {
		return ItemScreenFactory.getInstance();
	}
	@Override
	protected FieldWrapper createFieldWrapper(){
		return new ItemFieldWrapperRuntime();
	}
	@Override
	protected void updateFieldWrapper(FieldWrapper fieldWrapper, IField fieldScreen) {
		ItemFieldWrapperRuntime itemFieldWrapper = (ItemFieldWrapperRuntime)fieldWrapper;
		Integer fieldID=((TScreenFieldBean)fieldScreen).getField();
		FieldType fieldType=FieldTypeManager.getInstance().getType(fieldID);
		TypeRendererRT fieldTypeRendererRT = null;
		if (fieldType!=null) {
			fieldType.setFieldID(fieldID);
			fieldTypeRendererRT= fieldType.getRendererRT();
		}
		itemFieldWrapper.setLabelHAlign(AbstractFieldBL.getHalignString(((TScreenFieldBean)fieldScreen).getLabelHAlign()));
		itemFieldWrapper.setLabelVAlign(AbstractFieldBL.getValignString(((TScreenFieldBean)fieldScreen).getLabelVAlign()));
		itemFieldWrapper.setValueHAlign(AbstractFieldBL.getHalignString(((TScreenFieldBean)fieldScreen).getValueHAlign()));
		itemFieldWrapper.setValueVAlign(AbstractFieldBL.getValignString(((TScreenFieldBean)fieldScreen).getValueVAlign()));
		itemFieldWrapper.setHideLabelBoolean(((TScreenFieldBean)fieldScreen).isHideLabelBoolean());

		if (fieldTypeRendererRT!=null) {
			itemFieldWrapper.setJsClass(fieldTypeRendererRT.getExtClassName());
			itemFieldWrapper.setExtReadOnlyClassName(fieldTypeRendererRT.getExtReadOnlyClassName());
		}
		
		itemFieldWrapper.setFieldID(fieldID);
	}
}
