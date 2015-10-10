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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.AbstractScreenJSON;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.aurel.track.screen.bl.design.AbstractTabDesignBL;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Used to treat the operations from screen designer
 * operations on tab:reload,getproperty setproperty, addPanel,removePanel
 * @author Adrian Bojani
 *
 */
public abstract class AbstractTabAction extends ActionSupport implements Preparable, SessionAware/*,Validateable, ValidationAware*/{

	private static final Logger LOGGER = LogManager.getLogger(AbstractTabAction.class);
	
	private ITab tab;
	private Integer componentID;
	private Integer tabID;
	protected Integer screenID;
	private String property;
	private String value;
	protected List<IPanel> panels;
	private Integer panelID;
	private Integer newIndex;
	protected abstract AbstractPanelDesignBL getAbstractPanelDesignBL();
	protected abstract AbstractTabDesignBL getAbstractTabDesignBL();
	public abstract String localizeTab(String label);
	public abstract ScreenFactory getScreenFactory();
	protected Locale locale;
	private Map session;
	private String label;
	
	public static final long serialVersionUID = 400L;
	
	public void prepare() throws Exception {
		locale=(Locale) session.get(Constants.LOCALE_KEY);
		if(componentID==null&&tabID!=null){
			componentID=tabID;
		}
	}
	public abstract AbstractScreenJSON getScreenJSON();
	/**
	 * Reload the tab
	 * @return
	 */
	public String reload(){
		try{
			tab= getAbstractTabDesignBL().loadFullTab(componentID);
			getAbstractPanelDesignBL().calculateFieldWrappers(tab);
		}catch (Exception ex){
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
		}
		
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			JSONUtility.appendJSONValue(sb, JSONUtility.JSON_FIELDS.DATA, getScreenJSON().encodeTab(tab), true);
			sb.append("}");
			out.println(sb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	
	/**
	 * Obtain the properties for tab
	 * load the tab from db
	 * @return
	 */
	public String properties(){
		tab= getAbstractTabDesignBL().loadTab(componentID);
		String s=new AbstractScreenJSON() {}.encodeTabProperties(tab);
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
	 * Update a tab property
	 * @return
	 */
	public String updateProperty(){
		clearCache();
		ITab tabDB= getAbstractTabDesignBL().loadTab(componentID);
		getAbstractTabDesignBL().setTabProperty(tabDB,tab);
		getAbstractTabDesignBL().saveScreenTab(tabDB);
		return properties();
	}
	/**
	 * Add a new panel to the tab
	 * @return
	 */
	public String addPanel(){
		clearCache();
		IPanel pan=getScreenFactory().createIPanelInstance();
		pan.setParent(componentID);
		getAbstractPanelDesignBL().saveScreenPanel(pan);
		return reload();
	}
	/**
	 * Delete a panel form tab
	 * @return
	 */
	public String deleteChild(){
		clearCache();
		getAbstractPanelDesignBL().deleteScreenPanel(panelID);
		return reload();
	}
	/**
	 * Move a panel
	 * @return
	 */
	public String moveChild(){
		clearCache();
		getAbstractTabDesignBL().movePanel(componentID, panelID, newIndex);
		return reload();
	}
	protected void clearCache(){
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
	 * @return the tab
	 */
	public ITab getTab() {
		if(tab==null){
			tab=getScreenFactory().createITabInstance();
		}
		return tab;
	}
	/**
	 * @param tab the tab to set
	 */
	public void setTab(ITab tab) {
		this.tab = tab;
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
	 * @return the panels
	 */
	public List getPanels() {
		return panels;
	}
	/**
	 * @param panels the panels to set
	 */
	public void setPanels(List panels) {
		this.panels = panels;
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
	 * @return the newIndex
	 */
	public Integer getNewIndex() {
		return newIndex;
	}
	/**
	 * @param newIndex the newIndex to set
	 */
	public void setNewIndex(Integer newIndex) {
		this.newIndex = newIndex;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getComponentID() {
		return componentID;
	}
	public void setComponentID(Integer componentID) {
		this.componentID = componentID;
	}
	public Integer getTabID() {
		return tabID;
	}

	public void setTabID(Integer tabID) {
		this.tabID = tabID;
	}

	public Integer getScreenID() {
		return screenID;
	}

	public void setScreenID(Integer screenID) {
		this.screenID = screenID;
	}
}
