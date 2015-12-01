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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.beans.screen.IField;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenFieldDAO;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.util.EqualUtils;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TScreenFieldBean
    extends com.aurel.track.beans.base.BaseTScreenFieldBean
    implements Serializable, IField, ISerializableLabelBean 
{
	public static final long serialVersionUID = 400L;
	
	private static ScreenFieldDAO screenFieldDAO = DAOFactory.getFactory().getScreenFieldDAO();
	
	@Override
	public String getLabel() {
		return getName();
	}
	public boolean isHideLabelBoolean(){
		return BooleanFields.TRUE_VALUE.equals(getHideLabel());
	}
	public void setHideLabelBoolean(boolean hidenLabel) {
		setHideLabel(BooleanFields.fromBooleanToString(hidenLabel));
	}


	@Override
	public IField cloneMe() {
	 	TScreenFieldBean fieldBean=new TScreenFieldBean();
		fieldBean.setColIndex(this.getColIndex());
		fieldBean.setColSpan(this.getColSpan());
		fieldBean.setDescription(this.getDescription());
		fieldBean.setField(this.getField());
		fieldBean.setIndex(this.getIndex());
		fieldBean.setLabelHAlign(this.getLabelHAlign());
		fieldBean.setLabelVAlign(this.getLabelVAlign());
		fieldBean.setName(this.getName());
		fieldBean.setParent(this.getParent());
		fieldBean.setRowIndex(this.getRowIndex());
		fieldBean.setRowSpan(this.getRowSpan());
		fieldBean.setValueHAlign(this.getValueHAlign());
		fieldBean.setValueVAlign(this.getValueVAlign());
		fieldBean.setHideLabelBoolean(this.isHideLabelBoolean());
		return fieldBean;
	}

	/**
	 * serialize bean
	 */
	@Override
	public Map<String, String> serializeBean() {

		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("name", getName());
		Integer index = getIndex();
		if (index!=null){
			attributesMap.put("index", index.toString());
		}	
		attributesMap.put("colIndex", getColIndex().toString());
		attributesMap.put("rowIndex", getRowIndex().toString());	
		attributesMap.put("colSpan", getColSpan().toString());
		attributesMap.put("rowSpan", getRowSpan().toString());
		attributesMap.put("labelHAlign", getLabelHAlign().toString());	
		attributesMap.put("labelVAlign", getLabelVAlign().toString());
		attributesMap.put("valueHAlign", getValueHAlign().toString());
		attributesMap.put("valueVAlign", getValueVAlign().toString());	
		attributesMap.put("isEmpty", getIsEmpty().toString());
		attributesMap.put("parent", getParent().toString());
		attributesMap.put("field", getField().toString());
		String description = getDescription();
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);
		}
		attributesMap.put("hideLabel", getHideLabel());
		attributesMap.put("uuid",getUuid());
		return attributesMap;				
	}

	/**
	 * deserialize bean
	 */
	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {		
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			this.setObjectID(new Integer(strObjectID));
		}		
		this.setName(attributes.get("name"));	
		
		String index = attributes.get("index");
		if (index!=null && !"".equals(index)) {
			this.setIndex(Integer.parseInt(index));
		}
		this.setColIndex(Integer.parseInt(attributes.get("colIndex")));
		this.setRowIndex(Integer.parseInt(attributes.get("rowIndex")));
		this.setColSpan(Integer.parseInt(attributes.get("colSpan")));
		this.setRowSpan(Integer.parseInt(attributes.get("rowSpan")));	
		this.setLabelHAlign(Integer.parseInt(attributes.get("labelHAlign")));
		this.setLabelVAlign(Integer.parseInt(attributes.get("labelVAlign")));
		this.setValueHAlign(Integer.parseInt(attributes.get("valueHAlign")));
		this.setValueVAlign(Integer.parseInt(attributes.get("valueVAlign")));
		this.setIsEmpty(attributes.get("isEmpty"));
		this.setParent(Integer.parseInt(attributes.get("parent")));
		this.setField(Integer.parseInt(attributes.get("field")));
		String description = attributes.get("description");
		if (description!=null && !"".equals(description)) {
			this.setDescription(description);
		}
		String hideLabelStr = attributes.get("hideLabel");
		if (hideLabelStr!=null && !"".equals(hideLabelStr)) {
			this.setHideLabel(hideLabelStr);
		}
		this.setUuid(attributes.get("uuid"));
		return this;
	}
		
	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}		
		TScreenFieldBean screenFieldBean = (TScreenFieldBean) serializableLabelBean;
		String externalUuid = getUuid();
		String internalUuid = screenFieldBean.getUuid();
		if(EqualUtils.equalStrict(externalUuid, internalUuid)){
			return true;
		}
		
		return false;
	}
	
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {		
		TScreenFieldBean screenFieldBean = (TScreenFieldBean)serializableLabelBean;		
		Integer externalParent = screenFieldBean.getParent();
		Integer externalField = screenFieldBean.getField();
		Map<Integer, Integer> parentMatches = 
				matchesMap.get(ExchangeFieldNames.SCREENPANEL);
		Map<Integer, Integer> fieldMatches = 
				matchesMap.get(ExchangeFieldNames.FIELD);
		if (externalParent!=null && parentMatches.get(externalParent)!=null) {		
			screenFieldBean.setParent(parentMatches.get(externalParent));
		}
		if (externalField!=null && fieldMatches.get(externalField)!=null) {
			screenFieldBean.setField(fieldMatches.get(externalField));
		}
		return screenFieldDAO.save(screenFieldBean);
	}

	

	
		
}
