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

package com.aurel.track.screen.item.action;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.item.ItemScreenCache;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.item.adapterDAO.ScreenDAOAdapter;
import com.aurel.track.screen.item.bl.design.ScreenDesignBL;
import com.aurel.track.util.GeneralUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
/**
 * A action used to treat operations on Screen List
 * @author Adrian Bojani
 *
 *
 */
public class ScreenAction extends ActionSupport implements Preparable, SessionAware {
	
	private static final Logger LOGGER = LogManager.getLogger(ScreenAction.class);

	private static final long serialVersionUID = 340L;
	//private TScreenBean screen;
	private String name;
	private String tagLabel;
	private String description;
	private Integer screenID;
	private String selectedScreenIDs;
	//private List screens;
	//private boolean newScreen=false;
	private String sortKey;
	private Map<String, Object> session;
	private Integer userID;
	private boolean canViewScreen=false;
	private static final String HOME="cockpit";
	private boolean sysAdmin;
	//private boolean editMode=false;
	private boolean copy=false;
	private Locale locale;
	private List<Integer> selectedScreenIDList;
	private Integer replacementID;

	public void setSession(Map<String, Object> ses) {
		this.session=ses;
	}
	public void prepare() throws Exception {
		//screens= ScreenDesignBL.getInstance().getScreens();
		if(sortKey==null){
			sortKey=(String)session.get("sortKey");
			if(sortKey==null){
				sortKey="name";
			}
		}
		TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);
		userID=((TPersonBean) session.get(Constants.USER_KEY)).getObjectID();
		sysAdmin= personBean.isSys();
		if (sysAdmin){
			canViewScreen=true;
		} else {
			canViewScreen = personBean.isProjAdmin();
		}
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		selectedScreenIDList = getScreenIDList(selectedScreenIDs);
	}
	
	/**
	 * Gets the selected personIDs form the comma separated string
	 * @param selectedPersonIDs
	 * @return
	 */
	private static List<Integer> getScreenIDList(String selectedScreenIDs) {
		if (selectedScreenIDs!=null) {
			return GeneralUtils.createIntegerListFromStringArr(selectedScreenIDs.split(","));
		}
		return new LinkedList<Integer>();
	}
	
	/**
	 * Save a new screen
	 * @return
	 */
	public String save() {
		Integer newPk=null;
		if(screenID!=null){
			if(copy){
				newPk = ScreenDesignBL.getInstance().copyScreen(screenID,name,description,tagLabel,userID);
			}else{
				TScreenBean originalScreen=(TScreenBean) ScreenDesignBL.getInstance().loadScreen(screenID);
				originalScreen.setName(name);
				originalScreen.setTagLabel(tagLabel);
				originalScreen.setDescription(description);
				ScreenDesignBL.getInstance().saveScreen(originalScreen);
				ItemScreenCache.getInstance().removeScreen(screenID);
				newPk = screenID;
			}
		}else{
			newPk=ScreenDesignBL.getInstance().createNewScreen(name, tagLabel, description,userID);
		}
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONSuccessAndID(newPk));
		return null;
	}
	/**
	 * Call when a edit a screen is needed: 
	 * load the screen  
	 * @return
	 */
	public String edit() {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(!canViewScreen){
			JSONUtility.appendBooleanValue(sb,JSONUtility.JSON_FIELDS.SUCCESS,false);
		}else{
			TScreenBean screenBean;
			if(screenID==null){
				screenBean=new TScreenBean();
			}else{
				screenBean=(TScreenBean) ScreenDesignBL.getInstance().loadScreen(screenID);
				if(copy){
					String[] args={screenBean.getName()};
					String name = LocalizeUtil.getParametrizedString("common.copy", args, locale);
					screenBean.setName(name);
				}
			}
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			JSONUtility.appendJSONValue(sb,JSONUtility.JSON_FIELDS.DATA,
				ScreenDesignBL.getInstance().encodeJSONScreenTO(screenBean),true);
		}
		sb.append("}");
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	public String design(){
		if(!canViewScreen){
			return HOME;
		}
		TScreenBean screenBean=(TScreenBean)ScreenDesignBL.getInstance().loadScreen(screenID);
		if(sysAdmin||screenBean.getOwner().equals(userID)){
			return "edit";
		}else{//do not have right to edit
			return HOME;
		}
	}
	/**
	 * Use to copy a screen   
	 * load the screen  
	 * @return
	 */
	/*public String doCopy() {
		if(!canViewScreen){
			return HOME;
		}
		TScreenBean originalScreen=(TScreenBean) ScreenDesignBL.getInstance().loadScreen(screenID);
		TScreenBean screenBean=new TScreenBean();
		screenBean.setName(LocalizeUtil.getParametrizedString("common.lbl.copy",
				new Object[] {originalScreen.getName()}, locale));
		screenBean.setDescription(LocalizeUtil.getParametrizedString("common.lbl.copy",
				new Object[] {originalScreen.getDescription()}, locale));
		return doNew();
	}*/
	
	/**
	 * Delete a screen
	 * @return
	 */
	/*public String doDelete() {
		if (canViewScreen && canDeleteScreen()){
			ItemScreenCache.getInstance().removeScreen(screenID);
			ScreenDesignBL.getInstance().deleteScreen(screenID);
			JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		}else{//do not have right to edit
			JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(), "No right to delete", 0);
		}
		return null;
	}*/
	
	/**
	 * Deletes a person without dependency or offers the replacement list
	 * @param personID
	 * @return
	 */
	public String delete() {
		/*List<Integer> screenIDs = null;
		if (selectedScreenIDs==null) {
			JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
			return null;
		} else {*/
			//screenIDs = GeneralUtils.createIntegerListFromStringArr(selectedScreenIDs.split(","));
			if (selectedScreenIDList==null || selectedScreenIDList.isEmpty()) {
				JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
				return null;
			}
		//}
		if (canViewScreen && canDeleteScreen(selectedScreenIDList)) {
			for (Integer screenID : selectedScreenIDList) {
				ItemScreenCache.getInstance().removeScreen(screenID);
				ScreenDesignBL.getInstance().deleteScreen(screenID);
			}
			JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONSuccess());
		} else {
			JSONUtility.encodeJSON(ServletActionContext.getResponse(),
					JSONUtility.encodeJSONFailure(null, JSONUtility.DELETE_ERROR_CODES.NEED_REPLACE));
		}
		return null;
	}
	
	private boolean canDeleteScreen(List<Integer> screenIDs) {
		for (Integer screenID : screenIDs) {
			if(!ScreenDesignBL.getInstance().isDeletable(screenID)){
				//not deletable screen
				return false;
			}
		}
		if (sysAdmin){
			//system administrator can delete any screen
			return true;
		}
		//Need to load screen to check the owner
		/*TScreenBean screenBean=(TScreenBean)ScreenDesignBL.getInstance().loadScreen(screenID);
		if(userID.equals(screenBean.getOwner())){
			return true;
		}*/
		return false;
	}
	
	/**
	 * Renders the replacement department
	 * @return
	 */
	public String renderReplace() {
		JSONUtility.encodeJSON(ServletActionContext.getResponse(),
			prepareReplacement(selectedScreenIDList, null, locale));
		return null;
	}
	
	/**
	 * Replaces and deletes the person
	 * @return
	 */
	public String replaceAndDelete() {
		String jsonResponse = null;
		if (replacementID==null) {
			String errorMessage = getText("common.err.replacementRequired", 
					new String[] {getText("admin.customize.form.config.lbl.form")});
			jsonResponse = prepareReplacement(selectedScreenIDList, errorMessage, locale);
		} else {
			if (selectedScreenIDList!=null && !selectedScreenIDList.isEmpty()) {
				for (Integer screenID : selectedScreenIDList) {
					//delete the private entities before replacement
					ScreenDAOAdapter.getInstance().replaceScreen(screenID, replacementID);
					ItemScreenCache.getInstance().removeScreen(screenID);
					ScreenDesignBL.getInstance().deleteScreen(screenID);
				}	
			}
			jsonResponse = JSONUtility.encodeJSONSuccess();
		}
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), jsonResponse);
		return null;
	}
	
	/**
	 * Creates the JSON string for replacement triggers
	 * @param selectedScreenIDs
	 * @param errorMessage
	 * @param locale
	 * @return
	 * 
	 */
	static String prepareReplacement(List<Integer> selectedScreenIDs, String errorMessage, Locale locale) {
		String replacementEntity = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.form.config.lbl.form", locale);
		String formName = null;
		List<TScreenBean> replacementFormList = prepareReplacementScreens(selectedScreenIDs);
		if (selectedScreenIDs!=null && !selectedScreenIDs.isEmpty()) {
			if (selectedScreenIDs.size()==1) {
				//delete only one person
				TScreenBean screenBean = (TScreenBean)ScreenDAOAdapter.getInstance().loadByPrimaryKey(selectedScreenIDs.get(0));
				if (screenBean!=null) {
					formName = screenBean.getLabel();
				}
				return JSONUtility.createReplacementListJSON(true, formName, replacementEntity, replacementEntity, (List)replacementFormList, errorMessage, locale);		
			} else {
				//delete more than one screen
				int totalNumber = selectedScreenIDs.size();
				String entities = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.form.config.lbl.forms", locale);
				return JSONUtility.createReplacementListJSON(true, totalNumber, entities, replacementEntity, (List)replacementFormList, errorMessage, locale);
			}
		}
		return JSONUtility.createReplacementListJSON(true, formName, replacementEntity, replacementEntity, (List)replacementFormList, errorMessage, locale);	
	}
	
	/**
	 * Prepares the replacement triggers
	 * @param personID
	 * @return
	 */
	public static List<TScreenBean> prepareReplacementScreens(List<Integer> screenIDs) {
		List<TScreenBean> replacementScreenList = ScreenDAOAdapter.getInstance().loadAll();
		if (replacementScreenList!=null && screenIDs!=null) {
			Iterator<TScreenBean> iterator = replacementScreenList.iterator();
			while (iterator.hasNext()) {
				TScreenBean screenBean = iterator.next();
				if (screenIDs.contains(screenBean.getObjectID())) {
					iterator.remove();
				} else {
					//taken the name as label
					screenBean.setLabel(screenBean.getName());
				}
			}
		}
		return replacementScreenList;
	}
	
	/**
	 * Call when a new screen is fired
	 * @return
	 */
	/*public String doNew() {
		if(!canViewScreen){
			return HOME;
		}
		newScreen=true;
		boolean up=true;
		Boolean oldUp=(Boolean)session.get("up");
		if(oldUp!=null){
			up=oldUp.booleanValue();
		}
		screens=ScreenDesignBL.getInstance().getScreens(sortKey,up);
		return INPUT;
	}*/
	/**
	 * Obtain all screen from db
	 * @return
	 */
	public String list() {
		List screens = null;
		if(canViewScreen){
			screens=ScreenDesignBL.getInstance().getScreens(sortKey,true);
		}else{
			screens=new ArrayList();
		}
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), ScreenDesignBL.getInstance().encodeJSONScreenList(screens));
		/*try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(ScreenDesignBL.getInstance().encodeJSONScreenList(screens));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}*/
		return null;
	}
	
	/**
	 * @return the screens
	 */
	/*public List getScreens() {
		return screens;
	}*/
	/**
	 * @param screens the screens to set
	 */
	/*public void setScreens(List screens) {
		this.screens = screens;
	}*/
	/**
	 * @return the newScreen
	 */
	/*public boolean isNewScreen() {
		return newScreen;
	}*/
	/**
	 * @param newScreen the newScreen to set
	 */
	/*public void setNewScreen(boolean newScreen) {
		this.newScreen = newScreen;
	}*/
	/**
	 * @param screenID the screenID to set
	 */
	public void setScreenID(Integer screenID) {
		this.screenID = screenID;
	}
	/**
	 * @param sortKey the sortKey to set
	 */
	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	
	/*public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}*/

	public void setCopy(boolean copy) {
		this.copy = copy;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTagLabel(String tagLabel) {
		this.tagLabel = tagLabel;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setSelectedScreenIDs(String selectedScreenIDs) {
		this.selectedScreenIDs = selectedScreenIDs;
	}
	public void setReplacementID(Integer replacementID) {
		this.replacementID = replacementID;
	}
	
	
	
}
