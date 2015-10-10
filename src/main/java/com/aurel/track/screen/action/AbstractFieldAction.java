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

package com.aurel.track.screen.action;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAware;

/**
 * Used to treat the operations from screen designer
 * operations on field:reload,getproperty setproperty
 * @author Adrian Bojani
 *
 */
public abstract class AbstractFieldAction extends ActionSupport implements Preparable, Validateable, ValidationAware {
	private Integer componentID;
	protected Integer screenID;
	private IField field;
	private String property;
	private String value;
	private Integer panelID;
	private String fieldType;
	
	public static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(AbstractFieldAction.class);


    protected abstract AbstractFieldDesignBL getAbstractFieldDesignBL();
    protected abstract AbstractPanelDesignBL getAbstractPanelDesignBL();
    public abstract ScreenFactory getScreenFactory();
    public void prepare() throws Exception {
	}
	/**
	 * Validate property for panel
	 */
	@Override
	public void validate(){
		IField fieldDB= getAbstractFieldDesignBL().loadField(componentID);
		IPanel parent= getAbstractPanelDesignBL().loadPanel(fieldDB.getParent());
		int rowIndex=fieldDB.getRowIndex().intValue();
		int colIndex=fieldDB.getColIndex().intValue();
		int maxColSpan=parent.getColsNo().intValue()-colIndex;
		int maxRowSpan=parent.getRowsNo().intValue()-rowIndex;
		if(field.getRowSpan()!=null&&field.getColSpan()!=null){
			int rows=field.getRowSpan().intValue();
			int cols=field.getColSpan().intValue();
			if(rows>maxRowSpan){
				addFieldError("field.rowSpan",getText("screenEdit.error.screenField.rowSpanTooBig"));
			}
			if(cols>maxColSpan){
				addFieldError("field.colSpan",getText("screenEdit.error.screenField.colSpanTooBig"));
			}
		}
	}
	/**
	 * Obtain the properties for a field
	 * -load the screen field
	 * @return
	 */
	public String properties(){
		field=getAbstractFieldDesignBL().loadField(componentID);
		fieldType=getType(field);
		String s=getAbstractFieldDesignBL().encodeJSON_FieldProperies(field,fieldType);
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(s);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
    protected abstract String getType(IField field);
    /**
	 * Update a field property
	 * @return
	 */
	public String updateProperty(){
		clearCache();
		IField fieldDB=getAbstractFieldDesignBL().loadField(componentID);
		panelID=fieldDB.getParent();
		fieldType=getType(field);
		getAbstractFieldDesignBL().setFieldScreenProperty(fieldDB,field);
		getAbstractFieldDesignBL().saveScreenField(fieldDB);
		return properties();
		
	}
	protected void clearCache(){
	}

  	/**
	 * @return the field
	 */
	public IField getField() {
        if(field==null){
            field=getScreenFactory().createIFieldInstance();
        }
        return field;
	}
	/**
	 * @param field the field to set
	 */
	public void setField(IField field) {
		this.field = field;
	}
	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the panelID
	 */
	public Integer getPanelID() {
		return panelID;
	}
	/**
	 * @param panelID the panelID to set
	 */
	public void setPanelID(Integer panelID) {
		this.panelID = panelID;
	}
	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public Integer getComponentID() {
		return componentID;
	}
	public void setComponentID(Integer componentID) {
		this.componentID = componentID;
	}

	public Integer getScreenID() {
		return screenID;
	}

	public void setScreenID(Integer screenID) {
		this.screenID = screenID;
	}
}
