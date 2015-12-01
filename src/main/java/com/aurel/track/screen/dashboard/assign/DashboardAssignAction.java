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

package com.aurel.track.screen.dashboard.assign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.item.ItemDetailBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignBL;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Tamas Ruff, Adrian Bojani
 *
 */
public class DashboardAssignAction extends ActionSupport
	implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(DashboardAssignAction.class);
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private boolean canViewScreen=false;
	private TPersonBean personBean;
	private boolean  sys=false;
	private Integer objectID;
	private boolean copy;
	private String name;
	private String description;
	private String deletedItems;

	/**
	 * Prepares the listBean using the listID parameter
	 */
	@Override
	public void prepare() throws Exception {
		locale=(Locale) session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		sys= personBean.isSys();
		if(sys){
			canViewScreen=true;
		}else{
			canViewScreen = personBean.isProjAdmin();
		}
	}

	@Override
	public String execute(){
		List<TDashboardScreenBean> screens;
		if(canViewScreen){
			screens= DashboardAssignBL.loadNotAssigned();
		}else{
			screens=new ArrayList<TDashboardScreenBean>();
		}
		JSONUtility.encodeJSON(servletResponse, DashboardAssignJSON.encodeScreenList(screens, locale));
		return null;
	}

	public String edit() {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(!canViewScreen){
			JSONUtility.appendBooleanValue(sb,JSONUtility.JSON_FIELDS.SUCCESS,false);
		}else{
			TDashboardScreenBean screenBean;
			if(objectID==null){
				screenBean=new TDashboardScreenBean();
			}else{
				screenBean=DashboardAssignBL.loadByID(objectID);
				if(copy){
					String[] args={screenBean.getName()};
					String name = LocalizeUtil.getParametrizedString("common.copy", args, locale);
					screenBean.setName(name);
				}
			}
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			JSONUtility.appendJSONValue(sb,JSONUtility.JSON_FIELDS.DATA,
					DashboardAssignJSON.encodeJSONScreenTO(screenBean),true);
		}
		sb.append("}");
		JSONUtility.encodeJSON(servletResponse, sb.toString());
		return null;
	}
	
	public String save() {
		Integer newPk=null;
		if(objectID!=null){
			if(copy){
				newPk = DashboardScreenDesignBL.getInstance().copyScreen(objectID,name,description,null,null);
			}else{
				TDashboardScreenBean originalScreen=DashboardAssignBL.loadByID(objectID);
				originalScreen.setName(name);
				originalScreen.setDescription(description);
				DashboardScreenDesignBL.getInstance().saveScreen(originalScreen);
				newPk = objectID;
			}
		}else{
			TDashboardScreenBean dashboardScreen=DashboardScreenDesignBL.getInstance().createNewDashboardScreen(name,description,null,null,null);
			newPk=dashboardScreen.getObjectID();
		}
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccessAndID(newPk));
		return null;
	}
	public String delete(){
		Integer[] screensToDelete = ItemDetailBL.getIntegerTokens(deletedItems);
		for (int i = 0; i < screensToDelete.length; i++) {
			DashboardScreenDesignBL.getInstance().deleteScreen(screensToDelete[i]);
		}
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess());
		return null;
	}

	public String deleteCockpitTemplate(){
		Integer[] screensToDelete = ItemDetailBL.getIntegerTokens(deletedItems);
		Set<Integer> mySet = new HashSet<Integer>(Arrays.asList(screensToDelete));
		String resp;
		if(mySet.contains(DashboardScreenDesignBL.CLIENT_COCKPIT_TEMPLATE_ID)){
			TDashboardScreenBean screen = (TDashboardScreenBean)DashboardScreenDesignBL.getInstance().loadScreen(DashboardScreenDesignBL.CLIENT_COCKPIT_TEMPLATE_ID);
			String name = "";
			if(screen != null) {
				name = screen.getName();
			}
			String msg = LocalizeUtil.getParametrizedString("admin.customize.localeEditor.type.permErrorCockpitTplDelete", new Object[] {name}, locale);
			resp = JSONUtility.encodeJSONFailure(msg);
		}else {
			for (int i = 0; i < screensToDelete.length; i++) {
				DashboardScreenDesignBL.getInstance().deleteScreen(screensToDelete[i]);
			}
			resp = JSONUtility.encodeJSONSuccess();
		}
		JSONUtility.encodeJSON(servletResponse, resp);
		return null;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public void setCopy(boolean copy) {
		this.copy = copy;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDeletedItems(String deletedItems) {
		this.deletedItems = deletedItems;
	}

	/**
	 * @param session the session to set
	 */
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
}
