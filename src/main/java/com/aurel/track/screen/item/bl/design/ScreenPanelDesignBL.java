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

package com.aurel.track.screen.item.bl.design;

import java.util.Locale;

import com.aurel.track.beans.TScreenFieldBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.fieldType.design.renderer.TypeRendererDT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.screen.FieldWrapper;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.AbstractFieldBL;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.aurel.track.screen.item.ItemFieldWrapper;
import com.aurel.track.screen.item.adapterDAO.ItemScreenFactory;

/**
 * A Business Logic class for ScreenPanel for designTime
 * @author Adrian Bojani
 *
 */
public class ScreenPanelDesignBL extends AbstractPanelDesignBL {
	//singleton isntance
	private Locale locale;
	private static ScreenPanelDesignBL instance;
	private ScreenFieldDesignBL screenFieldBL;	
	
	/**
	 * get a singleton instance
	 * @return
	 */
	public static ScreenPanelDesignBL getInstance() {
		if (instance == null) {
			instance = new ScreenPanelDesignBL();
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
	public ScreenPanelDesignBL() {
		super();
		screenFieldBL=ScreenFieldDesignBL.getInstance();		
	}
	@Override
	protected FieldWrapper createFieldWrapper(){
		return new ItemFieldWrapper();
	}
	@Override
	protected void updateFieldWrapper(FieldWrapper fw, IField fs) {
		TScreenFieldBean fieldScreen=(TScreenFieldBean)fs;
		ItemFieldWrapper fieldWrapper= (ItemFieldWrapper) fw;
		TypeRendererDT fieldType=screenFieldBL.getTypeRenderer(fieldScreen);
		fieldWrapper.setLabelHAlign(AbstractFieldBL.getHalignString(fieldScreen.getLabelHAlign()));
		fieldWrapper.setLabelVAlign(AbstractFieldBL.getValignString(fieldScreen.getLabelVAlign()));
		fieldWrapper.setValueHAlign(AbstractFieldBL.getHalignString(fieldScreen.getValueHAlign()));
		fieldWrapper.setValueVAlign(AbstractFieldBL.getValignString(fieldScreen.getValueVAlign()));
		fieldWrapper.setHideLabelBoolean(fieldScreen.isHideLabelBoolean());
		//fieldWrapper.setHtmlString(fieldType.getHtml());
		//fieldWrapper.setSrcImage(fieldType.getImageName());
		//fieldWrapper.setFieldType(screenFieldBL.getType(fieldScreen).getFieldTypeName());		
		String label=FieldRuntimeBL.getLocalizedDefaultFieldLabel(fieldScreen.getField(), locale);
		fieldWrapper.setJsonData("{}");
		fieldWrapper.setJsClass(fieldType.getExtClassName());
		fieldWrapper.setLabel(label);

	}

	
	@Override
	public AbstractFieldDesignBL getFieldDesignBL() {
		return ScreenFieldDesignBL.getInstance();
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}