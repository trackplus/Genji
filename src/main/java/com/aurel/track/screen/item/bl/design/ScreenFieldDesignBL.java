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

package com.aurel.track.screen.item.bl.design;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TScreenFieldBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.design.renderer.TypeRendererDT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.item.TreeNodeField;
import com.aurel.track.screen.item.adapterDAO.ItemScreenFactory;
import com.aurel.track.util.GeneralUtils;

public class ScreenFieldDesignBL extends AbstractFieldDesignBL {
	//singleton isntance
	private static ScreenFieldDesignBL instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static ScreenFieldDesignBL getInstance() {
		if (instance == null) {
			instance = new ScreenFieldDesignBL();
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
	public ScreenFieldDesignBL() {
		super();
	}

	@Override
	protected void updateField(IField field, String fieldInfo) {
		Integer fieldDefPK=new Integer(fieldInfo);
		TFieldBean fieldDef=FieldBL.loadByPrimaryKey(fieldDefPK);
		field.setName(fieldDef.getName());
		((TScreenFieldBean)field).setField(fieldDefPK);
		field.setDescription(fieldDef.getDescription());
		((TScreenFieldBean)field).setValueHAlign(new Integer(LEFT));
		((TScreenFieldBean)field).setLabelHAlign(new Integer(LEFT));
		((TScreenFieldBean)field).setValueVAlign(new Integer(MIDDLE));
		((TScreenFieldBean)field).setLabelVAlign(new Integer(MIDDLE));
		((TScreenFieldBean)field).setHideLabelBoolean(false);
	}
	@Override
	public  void setFieldScreenProperty(IField fs,IField fsSchema){
		super.setFieldScreenProperty(fs,fsSchema);
		((TScreenFieldBean)fs).setLabelVAlign(((TScreenFieldBean)fsSchema).getLabelVAlign());
		((TScreenFieldBean)fs).setLabelHAlign(((TScreenFieldBean)fsSchema).getLabelHAlign());
		((TScreenFieldBean)fs).setValueVAlign(((TScreenFieldBean)fsSchema).getValueVAlign());
		((TScreenFieldBean)fs).setValueHAlign(((TScreenFieldBean)fsSchema).getValueHAlign());
		((TScreenFieldBean)fs).setHideLabelBoolean(((TScreenFieldBean)fsSchema).isHideLabelBoolean());
	}

	public List<TFieldConfigBean> getFieldsConfigs(Locale locale){
		List allFields= FieldBL.loadActive();
		List<Integer> fieldIDList=GeneralUtils.createIntegerListFromBeanList(allFields);
		List<TFieldConfigBean> fieldsConfigs=LocalizeUtil.localizeFieldConfigs(FieldConfigBL.loadDefaultForFields(fieldIDList),locale);
		for (Iterator iterator = fieldsConfigs.iterator(); iterator.hasNext();) {
			TFieldConfigBean fieldConfigBean = (TFieldConfigBean) iterator.next();
			fieldConfigBean.setCustom(isCustomField(allFields, fieldConfigBean.getField()));
		}
		return fieldsConfigs;
	}
	private boolean isCustomField(List allFields,Integer fieldID){
		for (int i = 0; i < allFields.size(); i++) {
			TFieldBean field=(TFieldBean) allFields.get(i);
			if(field.getObjectID().intValue()==fieldID.intValue()){
				return BooleanFields.TRUE_VALUE.equals(field.getIsCustom());
			}
		}
		return false;
	}

	public void loadFields(List<TreeNodeField> systemFields, List<TreeNodeField> customFields, Locale locale){
		List<TFieldBean> allFields = FieldBL.loadActive();
		Map<Integer,TFieldBean> fieldsMap = new HashMap<Integer, TFieldBean>();
		List<Integer> fieldIDList = new ArrayList<Integer>(allFields.size());
		for (TFieldBean fieldBean : allFields) {
			fieldsMap.put(fieldBean.getObjectID(), fieldBean);
			fieldIDList.add(fieldBean.getObjectID());
		}
		Map<Integer, String> fieldsConfigLabelsMap = FieldRuntimeBL.getLocalizedDefaultFieldLabels(fieldIDList, locale);// LocalizeUtil.localizeFieldConfigs(DAOFactory.getFactory().getFieldConfigDAO().loadDefaultForFields(fieldIDList),locale);
		for (Map.Entry<Integer, String> fieldsConfigLabelEntry : fieldsConfigLabelsMap.entrySet()) {
			Integer fieldID = fieldsConfigLabelEntry.getKey();
			String label = fieldsConfigLabelEntry.getValue();
			TFieldBean fieldBean= fieldsMap.get(fieldID);
			TreeNodeField treeNodeField=new TreeNodeField();
			treeNodeField.setObjectID(fieldID);
			treeNodeField.setName(fieldBean.getName());
			treeNodeField.setLabel(label);
			treeNodeField.setDescription(fieldBean.getDescription());
			FieldType fieldType=FieldTypeManager.getInstance().getType(fieldID);
			if (fieldType!=null){
				String iconCls=fieldType.getIconName();
				treeNodeField.setImg(iconCls);
			}
			if (BooleanFields.TRUE_VALUE.equals(fieldBean.getIsCustom())){
				customFields.add(treeNodeField);
			} else {
				//exclude the project specific issue no field from filter
				//configuration because only one issueNo field should be available
				if (!SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO.equals(fieldID)) {
					systemFields.add(treeNodeField);
				}
			}
		}
		Collections.sort(systemFields);
		Collections.sort(customFields);
	}

	 public TypeRendererDT getTypeRenderer(TScreenFieldBean fs){
		 return FieldTypeManager.getInstance().getType(fs.getField()).getRendererDT();
	 }
	 public FieldType getType(TScreenFieldBean fs){
		 return FieldTypeManager.getInstance().getType(fs.getField());
	 }
	 
	@Override
	public String encodeJSON_FieldProperies(IField field,String fieldType){
		return new ItemScreenDesignJSON().encodeFieldProperies(field,fieldType);
	}
}
