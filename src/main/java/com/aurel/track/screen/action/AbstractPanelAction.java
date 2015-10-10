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
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.AbstractScreenJSON;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Used to treat the operations from screen designer
 * operations on pan:reload,getproperties, setproperty,delete field, add field
 * @author Adrian Bojani
 *
 */
public abstract class AbstractPanelAction extends ActionSupport implements Preparable, SessionAware/*,Validateable, ValidationAware*/{
	private IPanel panel;
	private Integer componentID;
	protected Integer screenID;
	private String property;
	private String value;
	protected Object[][] fields;
	//used to treat operation on fields
	private Integer fieldID;
    private String fieldType;
    private String source;
	private String target;
	private Integer sourcePanelID;
    private Map session;
    protected Locale locale;
    
	public static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(AbstractPanelAction.class);
	
    protected abstract AbstractPanelDesignBL getAbstractPanelDesignBL();
    protected abstract AbstractFieldDesignBL getAbstractFieldDesignBL();
    public abstract ScreenFactory getScreenFactory();    
    public void prepare() throws Exception {
        locale=(Locale) session.get(Constants.LOCALE_KEY);
    }
	/**
	 * Reload the panel
	 * @return
	 */
	public String reload(){
		IPanel panWrapper= getAbstractPanelDesignBL().loadPanelWrapped(componentID);
		String s=encodePanel(panWrapper);
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	protected abstract String encodePanel(IPanel panel);
	/**
	 * Obtain the properties for the panel
	 * @return
	 */
	public String properties(){
		panel=getAbstractPanelDesignBL().loadPanel(componentID);
		String s=new AbstractScreenJSON(){}.encodePanelProperties(panel);
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	/**
	 * Update a panel property
	 * @return
	 */
	public String updateProperty(){
		try{
		clearCache();
		IPanel panelDb=getAbstractPanelDesignBL().loadPanelWrapped(componentID);
		getAbstractPanelDesignBL().setPanelProperty(panelDb,panel);
		getAbstractPanelDesignBL().saveScreenPanel(panelDb);
		return properties();
		}catch (Exception ex){
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
		}
		return null;
	}
	/**
	 * Add a new field to the panel
	 * @return
	 */
	public String addField(){
		clearCache();
		int row=Integer.parseInt(target.substring(0,target.indexOf(";")));
		int col=Integer.parseInt(target.substring(target.indexOf(";")+1));
		getAbstractPanelDesignBL().addFieldScreen(componentID,fieldType,row,col);
		return reload();
	}
	/**
	 * Move a field
	 * @return
	 */
	public String moveField(){
		clearCache();
		int row_source=Integer.parseInt(source.substring(0,source.indexOf(";")));
		int col_source=Integer.parseInt(source.substring(source.indexOf(";")+1));
		int row_target=Integer.parseInt(target.substring(0,target.indexOf(";")));
		int col_target=Integer.parseInt(target.substring(target.indexOf(";")+1));
		getAbstractPanelDesignBL().moveFieldScreen(componentID,row_source,col_source,row_target,col_target);
		return reload();
	}
	/**
	 * Move a field from a panele to other
	 * @return
	 */
	public String moveFieldFromOther(){
		clearCache();
		int row_target=Integer.parseInt(target.substring(0,target.indexOf(";")));
		int col_target=Integer.parseInt(target.substring(target.indexOf(";")+1));
		getAbstractPanelDesignBL().moveFieldFromOther(componentID,fieldID,row_target,col_target,sourcePanelID);
		return reload();
	}
	/**
	 * delete a field from panel
	 * @return
	 */
	public String doDeleteChild(){
		clearCache();
		getAbstractFieldDesignBL().deleteFieldScreen(componentID,fieldID);
		return reload();
	}
	protected void clearCache(){
	}

	/**
	 * @return the panel
	 */
	public IPanel getPanel() {
		if(panel==null){
            panel=getScreenFactory().createIPanelInstance();
        }
        return panel;
	}
	/**
	 * @param panel the panel to set
	 */
	public void setPanel(IPanel panel) {
		this.panel = panel;
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
	 * @return the fields
	 */
	public Object[][] getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(Object[][] fields) {
		this.fields = fields;
	}
	/**
	 * @return the fieldID
	 */
	public Integer getFieldID() {
		return fieldID;
	}
	/**
	 * @param fieldID the fieldID to set
	 */
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * @return the sourcePanelID
	 */
	public Integer getSourcePanelID() {
		return sourcePanelID;
	}
	/**
	 * @param sourcePanelID the sourcePanelID to set
	 */
	public void setSourcePanelID(Integer sourcePanelID) {
		this.sourcePanelID = sourcePanelID;
	}

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
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
