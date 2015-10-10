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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.screen.FieldWrapper;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TDashboardPanelBean
	extends com.aurel.track.beans.base.BaseTDashboardPanelBean
	implements Serializable, IPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5611927396912783811L;
	
	private FieldWrapper[][] fieldWrappers;
	private List<IField> fields;
	private IField[][] fieldsArray;
	
	public void setFields(List<IField> fields){
		this.fields=new ArrayList<IField>();
		for (Iterator<IField> iter = fields.iterator(); iter.hasNext();) {
			IField field =  iter.next();
			addField(field);
		}
	}
	
	/**
	 * Get the field on given position
	 * @param row
	 * @param col
	 * @return
	 */
	public  IField getScreenField(int row, int col){
		if(fieldsArray==null){
			fieldsArray=new IField[this.getRowsNo().intValue()][this.getColsNo().intValue()];
		}
		if(row<0||row>fieldsArray.length) return null;
		if(col<0||col>fieldsArray[row].length) return null;
		return fieldsArray[row][col];
	}
	
	/**
	 * Add a new field at the end
	 * @param field
	 */
	private void addField(IField field){
		if(fieldsArray==null){
			fieldsArray= new IField[this.getRowsNo().intValue()][this.getColsNo().intValue()];
		}
		field.setParent(this.getObjectID());
		fieldsArray[field.getRowIndex().intValue()][field.getColIndex().intValue()]=field;
		if (fields==null) {
			fields = new ArrayList<IField>();
		}
		this.fields.add(field);
	}

	public FieldWrapper[][] getFieldWrappers() {
		if(fieldWrappers==null){
			fieldWrappers=new FieldWrapper[getRowsNo().intValue()][getColsNo().intValue()];
		}
		return fieldWrappers;
	}

	public void setFieldWrappers(FieldWrapper[][] fieldWrappers) {
		this.fieldWrappers = fieldWrappers;
	}

	public List<IField> getFields() {
		return fields;
	}
	public IPanel cloneMe(){
		TDashboardPanelBean panelBean=new TDashboardPanelBean();
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
