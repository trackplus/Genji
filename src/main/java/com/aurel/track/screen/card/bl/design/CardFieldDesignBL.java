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

package com.aurel.track.screen.card.bl.design;


import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TCardFieldBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.card.adapterDAO.CardScreenFactory;

import java.util.*;

public class CardFieldDesignBL extends AbstractFieldDesignBL {
	//singleton isntance
	private static CardFieldDesignBL instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static CardFieldDesignBL getInstance() {
		if (instance == null) {
			instance = new CardFieldDesignBL();
		}
		return instance;
	}

	@Override
	protected ScreenFactory getScreenFactory() {
		return CardScreenFactory.getInstance();
	}

	/**
	 * constructor
	 */
	public CardFieldDesignBL() {
		super();
	}

	@Override
	protected void updateField(IField field, String fieldInfo) {
		Integer fieldDefPK=new Integer(fieldInfo);
		if(fieldDefPK<0){
			//pseudoField
			field.setName(TReportLayoutBean.getPseudoColumnName(fieldDefPK));

		}else {
			TFieldBean fieldDef = FieldBL.loadByPrimaryKey(fieldDefPK);
			field.setName(fieldDef.getName());
			field.setDescription(fieldDef.getDescription());
		}
		((TCardFieldBean)field).setField(fieldDefPK);
		((TCardFieldBean)field).setIconRendering(0);
		((TCardFieldBean)field).setValueHAlign(new Integer(LEFT));
		((TCardFieldBean)field).setLabelHAlign(new Integer(LEFT));
		((TCardFieldBean)field).setValueVAlign(new Integer(MIDDLE));
		((TCardFieldBean)field).setLabelVAlign(new Integer(MIDDLE));
		((TCardFieldBean)field).setHideLabelBoolean(false);
	}
	@Override
	public  void setFieldScreenProperty(IField fs,IField fsSchema){
		super.setFieldScreenProperty(fs,fsSchema);
		((TCardFieldBean)fs).setIconRendering(((TCardFieldBean)fsSchema).getIconRendering());
		((TCardFieldBean)fs).setLabelVAlign(((TCardFieldBean)fsSchema).getLabelVAlign());
		((TCardFieldBean)fs).setLabelHAlign(((TCardFieldBean)fsSchema).getLabelHAlign());
		((TCardFieldBean)fs).setValueVAlign(((TCardFieldBean)fsSchema).getValueVAlign());
		((TCardFieldBean)fs).setValueHAlign(((TCardFieldBean)fsSchema).getValueHAlign());
		((TCardFieldBean)fs).setHideLabelBoolean(((TCardFieldBean)fsSchema).isHideLabelBoolean());
	}

	@Override
	public String encodeJSON_FieldProperies(IField field,String fieldType){
		return new CardScreenDesignJSON().encodeFieldProperies(field,fieldType);
	}
}
