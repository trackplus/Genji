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
import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.aurel.track.screen.bl.design.AbstractScreenDesignBL;
import com.aurel.track.screen.bl.design.AbstractTabDesignBL;
import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignJSON;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Used to treat the operations from screen designer
 * @author Adrian Bojani
 *
 */
public abstract class AbstractScreenEditAction extends ActionSupport implements Preparable, SessionAware/*,Validateable, ValidationAware*/{
	private static final Logger LOGGER = LogManager.getLogger(AbstractScreenEditAction.class);
	protected final String TAB_TYPE="Tab";
	protected final String PANEL_TYPE="Panel";
	protected final String FIELD_TYPE="Field";
	public static final String SESSION_PREFIX_CLIPBOARD="screen_clipboard";

	protected IScreen screen;
	protected Integer componentID;
	protected Integer screenID;
	protected List<ITab> tabs;
	protected String property;
	protected String value;
	protected Integer tabID;
	protected Integer newIndex;
	protected Locale locale;
	protected Integer selectedTab;
	public abstract AbstractScreenDesignBL getScreenDesignBL();
	public abstract AbstractTabDesignBL getTabDesignBL();
	public abstract AbstractPanelDesignBL getPanelDesignBL();
	protected abstract AbstractFieldDesignBL getFieldDesignBL();
	public abstract ScreenFactory getScreenFactory();

	protected String sourceType;
	protected Integer sourceID;
	protected String targetType;
	protected Integer targetID;
	protected String targetInfo;

	public static final long serialVersionUID = 400L;
	
	protected Map session;

    protected String backAction;

	public String getConfigURL() {
		return configURL;
	}

	public void setConfigURL(String configURL) {
		this.configURL = configURL;
	}

	protected String configURL=null;

    private boolean hasInitData=true;
	private String initData;

	private String layoutCls="com.trackplus.layout.ItemScreenEditLayout";
	private String pageTitle="admin.customize.form.config.title";
	
	@Override
	public void prepare() throws Exception {
		locale=(Locale)session.get(Constants.LOCALE_KEY);
	}
	@Override
	public String execute() throws Exception {
		initData=executeInternal();
		return SUCCESS;
	}
	protected IScreen loadScreen(){
		return getScreenDesignBL().loadScreen(componentID);
	}
	/**
	 * Reload the screen
	 * @return
	 */
	public String reload(){
		String data=executeInternal();
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			JSONUtility.appendJSONValue(sb, JSONUtility.JSON_FIELDS.DATA, data, true);
			sb.append("}");
			out.println(sb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	public String copy(){
		LOGGER.debug("copy: sourceType="+sourceType+" sourceID="+sourceID);
		if(TAB_TYPE.equals(sourceType)){
			ITab tab=getTabDesignBL().loadFullTab(sourceID);
			if(tab!=null){
				LOGGER.debug("tab loaded successfuly");
				ITab clone=tab.cloneMe();
				session.put(SESSION_PREFIX_CLIPBOARD,clone);
			}else{
				LOGGER.error("Tab not found for copy");
			}

		}
		if(PANEL_TYPE.equals(sourceType)){
			IPanel panel=getPanelDesignBL().loadPanelWrapped(sourceID);
			if(panel!=null){
				LOGGER.debug("panel loaded successfuly");
				IPanel clone=panel.cloneMe();
				session.put(SESSION_PREFIX_CLIPBOARD,clone);
			}else{
				LOGGER.error("Panel not found for copy");
			}

		}
		if(FIELD_TYPE.equals(sourceType)){
			IField field=getFieldDesignBL().loadField(sourceID);
			if(field!=null){
				LOGGER.debug("field loaded successfuly");
				IField clone=field.cloneMe();
				session.put(SESSION_PREFIX_CLIPBOARD,clone);
			}else{
				LOGGER.error("field not found for copy");
			}

		}

		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	public String paste(){
		LOGGER.debug("paste: sourceType="+sourceType+" sourceId="+sourceID+" targetType="+targetType+" targetId="+targetID+" targetInfo="+targetInfo);
		//paste tab
		if(TAB_TYPE.equals(sourceType)){
			ITab tab=(ITab)session.get(SESSION_PREFIX_CLIPBOARD);
			IScreen screen=getScreenDesignBL().loadScreen(componentID);
			if(screen!=null){
				tab.setParent(componentID);
				tab.setIndex(null);
				Integer pk=getTabDesignBL().saveScreenTab(tab);
				List<IPanel> panels=tab.getPanels();
				if(panels!=null){
					for(IPanel p:panels){
						p.setParent(pk);
						getPanelDesignBL().saveScreenPanel(p,true);
					}
				}
			}
		}

		//paste panel
		if(PANEL_TYPE.equals(sourceType)){
			IPanel panel=(IPanel)session.get(SESSION_PREFIX_CLIPBOARD);
			ITab targetTab=null;
			if(TAB_TYPE.equals(targetType)){
				targetTab=getTabDesignBL().loadTab(targetID);
			}
			if(PANEL_TYPE.equals(targetType)){
				IPanel targetPanel=getPanelDesignBL().loadPanel(targetID);
				targetTab=getTabDesignBL().loadTab(targetPanel.getParent());
			}
			panel.setParent(targetTab.getObjectID());
			panel.setIndex(null);
			getPanelDesignBL().saveScreenPanel(panel,true);
		}

		//paste field
		if(FIELD_TYPE.equals(sourceType)){
			IField field=(IField)session.get(SESSION_PREFIX_CLIPBOARD);
			int row=0;
			int col=0;
			IPanel targetPanel=null;
			Integer panelPk=null;
			if(PANEL_TYPE.equals(targetType)&&targetInfo!=null){
				targetPanel=getPanelDesignBL().loadPanel(targetID);
				panelPk=targetPanel.getObjectID();
				row=Integer.parseInt(targetInfo.substring(0,targetInfo.indexOf(";")));
				col=Integer.parseInt(targetInfo.substring(targetInfo.indexOf(";")+1));
			}
			if(FIELD_TYPE.equals(targetType)){
				IField fieldTarget=getFieldDesignBL().loadField(targetID);
				panelPk=fieldTarget.getParent();
				row=fieldTarget.getRowIndex();
				col=fieldTarget.getColIndex();
			}
			if(panelPk!=null){
				field.setParent(panelPk);
				getPanelDesignBL().addFieldScreen(panelPk,field,row,col);
			}

		}
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	public String executeInternal(){
		screen=loadScreen();
		if(selectedTab==null){
			if(tabs!=null && !tabs.isEmpty()){
				ITab firstTab=tabs.get(0);
				selectedTab=firstTab.getObjectID();
			}
		}
		return prepareJSON(screen, selectedTab); 
	}
	protected String prepareJSON(IScreen screen,Integer selectedTab){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, "selectedTab", selectedTab);
        JSONUtility.appendStringValue(sb,"backAction",backAction);
		JSONUtility.appendStringValue(sb,"configURL",configURL);
		sb.append(appendExtraInitJSON(screen,selectedTab));
       	JSONUtility.appendJSONValue(sb, "screen",new DashboardScreenDesignJSON().encodeScreen(screen) , true);
		sb.append("}");
		return sb.toString();
	}
	protected String appendExtraInitJSON(IScreen screen,Integer selectedTab){
		return "";
	}
	
	/**
	 * Add a new tab
	 * @return
	 */
	public String addTab(){
		clearCache();
		ITab tab=getScreenFactory().createITabInstance();
		tab.setParent(componentID);
		selectedTab=getTabDesignBL().saveScreenTab(tab);
		return reload();

	}
	/**
	 * Change the order of the tab
	 * @return
	 */
	public String moveTab(){
		clearCache();
		getScreenDesignBL().moveTab(componentID, tabID, newIndex);
		selectedTab=tabID;
		return reload();

	}
	/**
	 * Delete a child (tab)
	 * @return
	 */
	public String deleteChild(){
		clearCache();
		screen=loadScreen();
		tabs=screen.getTabs();
		if(tabs.size()>1){
			getTabDesignBL().deleteScreenTab(tabID);
			selectedTab=null;
			for(int i=1;i<tabs.size();i++){
				if(tabs.get(i).getObjectID().equals(tabID)){
					selectedTab=tabs.get(i-1).getObjectID();
					break;
				}
			}
		}
		return reload();
	}
	/**
	 * Obtain the properties for screen
	 * -load the screen
	 * @return
	 */
	public String properties(){
		screen=loadScreen();
		String s=getScreenDesignBL().encodeJSON_ScreenProperies(screen);
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
	 * Set a screen property
	 * @return
	 */
	public String updateProperty(){
		clearCache();
		IScreen screenDb=getScreenDesignBL().loadScreen(componentID);
		getScreenDesignBL().setScreenProperty(screenDb,screen);
		getScreenDesignBL().saveScreen(screenDb);
		return properties();
	}

	protected void clearCache(){
	}

	/**
	 * @return the screen
	 */
	public IScreen getScreen() {
		if(screen==null){
			screen=getScreenFactory().createIScreeenInstance();
		}
		return screen;
	}
	/**
	 * @param screen the screen to set
	 */
	public void setScreen(IScreen screen) {
		this.screen = screen;
	}
	/**
	 * @return the tabs
	 */
	public List getTabs() {
		return tabs;
	}
	/**
	 * @param tabs the tabs to set
	 */
	public void setTabs(List tabs) {
		this.tabs = tabs;
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
	 * @return the tabID
	 */
	public Integer getTabID() {
		return tabID;
	}
	/**
	 * @param tabID the tabID to set
	 */
	public void setTabID(Integer tabID) {
		this.tabID = tabID;
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

	@Override
	public void setSession(Map session) {
		this.session = session;
	}
	public Integer getSelectedTsab() {
		return selectedTab;
	}
	public void setSelectedTab(Integer selectedTab) {
		this.selectedTab = selectedTab;
	}
	public boolean isHasInitData() {
		return hasInitData;
	}
	public void setHasInitData(boolean hasInitData) {
		this.hasInitData = hasInitData;
	}
	public String getInitData() {
		return initData;
	}
	public Integer getComponentID() {
		return componentID;
	}
	public void setComponentID(Integer componentID) {
		this.componentID = componentID;
	}
    public String getBackAction() {
        return backAction;
    }

    public void setBackAction(String backAction) {
        this.backAction = backAction;
    }

	public Integer getScreenID() {
		return screenID;
	}

	public void setScreenID(Integer screenID) {
		this.screenID = screenID;
	}

	public void setSourceID(Integer sourceID) {
		this.sourceID = sourceID;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public void setTargetID(Integer targetID) {
		this.targetID = targetID;
	}

	public void setTargetInfo(String targetInfo) {
		this.targetInfo = targetInfo;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
