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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenPanelDAO;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.screen.FieldWrapper;
import com.aurel.track.util.EqualUtils;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TScreenPanelBean
	extends com.aurel.track.beans.base.BaseTScreenPanelBean
	implements Serializable, IPanel, ISerializableLabelBean {
	
	private static final long serialVersionUID = 4331924121180208513L;
	
	//The fields of the panel the position on matrix is the position in grid
	private FieldWrapper[][] fieldWrappers;
	private List<IField> fields;
	private IField[][] fieldsArray;
	private static ScreenPanelDAO screenPanelDAO = DAOFactory.getFactory().getScreenPanelDAO();
	
	@Override
	public void setFields(List<IField> fields){
		this.fields=new ArrayList<IField>();
		if(fields!=null){
			for (Iterator<IField> iter = fields.iterator(); iter.hasNext();) {
				TScreenFieldBean field = (TScreenFieldBean) iter.next();
				addField(field);
			}
		}
	}
	
	/**
	 * Get the field on given position
	 * @param row
	 * @param col
	 * @return
	 */
	@Override
	public  IField getScreenField(int row, int col){
		if(fieldsArray==null){
			fieldsArray=new IField[this.getRowsNo().intValue()][this.getColsNo().intValue()];
		}
		if(row<0||row>fieldsArray.length) {
			return null;
		}
		if(col<0||col>fieldsArray[row].length) {
			return null;
		}
		return fieldsArray[row][col];
	}
	
	/**
	 * Add a new field at the end
	 * @param field
	 */
	public void addField(TScreenFieldBean field){
		if(fieldsArray==null){
			fieldsArray= new IField[this.getRowsNo().intValue()][this.getColsNo().intValue()];
		}
		field.setParent(this.getObjectID());
		fieldsArray[field.getRowIndex().intValue()][field.getColIndex().intValue()]=field;
		if (fields==null) {
			fields = new ArrayList<IField>();
		}
		fields.add(field);
	}

	@Override
	public FieldWrapper[][] getFieldWrappers() {
		if(fieldWrappers==null){
			fieldWrappers=new FieldWrapper[getRowsNo().intValue()][getColsNo().intValue()];
		}
		return fieldWrappers;
	}

	public void setFieldWrappers(FieldWrapper[][] fieldWrappers) {
		this.fieldWrappers = fieldWrappers;
	}

	@Override
	public List<IField> getFields() {
		return fields;
	}
	
	
	/**A
	 * serialize Bean
	 */
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("name", getName());
		attributesMap.put("label", getLabel());	
		attributesMap.put("index", getIndex().toString());
		attributesMap.put("rowsNo", getRowsNo().toString());	
		attributesMap.put("colsNo", getColsNo().toString());
		attributesMap.put("parent", getParent().toString());	
		String description = getDescription();
		
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);
		}
		
		attributesMap.put("uuid", getUuid());
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
		this.setLabel(attributes.get("label"));	
		this.setIndex(Integer.parseInt(attributes.get("index")));
		this.setRowsNo(Integer.parseInt(attributes.get("rowsNo")));
		this.setColsNo(Integer.parseInt(attributes.get("colsNo")));
		
		this.setParent(Integer.parseInt(attributes.get("parent")));
		this.setDescription(attributes.get("description"));
		
		this.setUuid(attributes.get("uuid"));
		return this;
	}
	
	
	/**A
	 * Check if we should consider two beans the same
	 * @param 
	 * @return 
	 */
	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}		
		TScreenPanelBean screenPanelBean = (TScreenPanelBean) serializableLabelBean;
		String externalUuid = getUuid();
		String internalUuid = screenPanelBean.getUuid();
		if(EqualUtils.equalStrict(externalUuid, internalUuid)){
			return true;
		}
		///
		return false;
	}
	
	/**A
	 * Save the bean
	 * @param 
	 * @return 
	 */
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {		
		TScreenPanelBean screenPanelBean = (TScreenPanelBean)serializableLabelBean;		
		Integer externalParent = screenPanelBean.getParent();
		Map<Integer, Integer> parentMatches = 
				matchesMap.get(ExchangeFieldNames.SCREENTAB);
		if (externalParent!=null && parentMatches.get(externalParent)!=null) {
			
			screenPanelBean.setParent(parentMatches.get(externalParent));
		}
		return screenPanelDAO.save(screenPanelBean);
	}

	@Override
	public IPanel cloneMe(){
		TScreenPanelBean panelBean=new TScreenPanelBean();
		panelBean.setColsNo(this.getColsNo());
		panelBean.setDescription(this.getDescription());
		panelBean.setIndex(this.getIndex());
		panelBean.setLabel(this.getLabel());
		panelBean.setName(this.getName());
		panelBean.setParent(this.getParent());
		panelBean.setRowsNo(this.getRowsNo());

		List<IField> fieldsClone=new ArrayList<IField>();
		List<IField> fields=this.getFields();
		if(fields!=null){
			for(IField field:fields){
				fieldsClone.add(field.cloneMe());
			}
		}
		panelBean.setFields(fieldsClone);
		return panelBean;
	}
}
