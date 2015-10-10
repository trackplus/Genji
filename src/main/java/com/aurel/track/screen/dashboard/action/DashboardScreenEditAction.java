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


package com.aurel.track.screen.dashboard.action;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.action.AbstractScreenEditAction;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.aurel.track.screen.bl.design.AbstractScreenDesignBL;
import com.aurel.track.screen.bl.design.AbstractTabDesignBL;
import com.aurel.track.screen.dashboard.adapterDAO.DashboardScreenFactory;
import com.aurel.track.screen.dashboard.bl.design.DashboardFieldDesignBL;
import com.aurel.track.screen.dashboard.bl.design.DashboardPanelDesignBL;
import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignBL;
import com.aurel.track.screen.dashboard.bl.design.DashboardTabDesignBL;

/**
 * Used to treat the operations from screen designer
 * operations on screen:reload,getproperty setproperty, deletetab,addtab
 * @author Adrian Bojani
 *
 */
public class DashboardScreenEditAction extends AbstractScreenEditAction {

	private static final long serialVersionUID = 340L;
	
	//private boolean defaultDashboardFlag;
	public static final String DASHBOARD_EDIT_FROM = "tmp_dashboardEditFrom";
	public static final String DASHBOARD_EDIT_PERSON = "tmp_dashboardEditPerson";
	private Boolean fromAdmin;	
	/**
	 * the currently logged in person if backAction=false
	 * the selected person from dashboardAssign if backAction=true
	 */
	private Integer personID;
	private TPersonBean personBean;

	private Integer projectID;
	private Integer entityType;


	@Override
	public void prepare() throws Exception {
		fromAdmin = (Boolean)session.get(DASHBOARD_EDIT_FROM);
		if (fromAdmin==null) {
			fromAdmin = new Boolean(false);
		}
		if (fromAdmin.booleanValue()) {
			//person sets dashboard for somebody else			
			//set the person for which the current dashboard edit takes place
			personID = (Integer)session.get(DASHBOARD_EDIT_PERSON);
		} else {
			//person sets dashboard for herself
			personID =((TPersonBean) session.get(Constants.USER_KEY)).getObjectID();
		}
		personBean=((TPersonBean) session.get(Constants.USER_KEY));

		super.prepare();
		if(projectID!=null){
			configURL="dashboardParamsConfig.action?projectID="+projectID+"&entityType="+entityType+"&dashboardID=";
		}else{
			configURL="dashboardParamsConfig.action?dashboardID=";
		}
	}
	
	@Override
	protected IScreen loadScreen() {
		IScreen result=null;
		if(componentID!=null){
			result = DashboardScreenDesignBL.getInstance().loadScreen(componentID);
		}else{
			if(projectID!=null){
				result=DashboardScreenDesignBL.getInstance().loadByProject(personID, projectID, entityType);
			}else{
				result = DashboardScreenDesignBL.getInstance().loadByPerson(personBean);
			}
			componentID=result.getObjectID();
		}
		List<ITab> tabs=result.getTabs();
		if(tabs.size()==1){
			Integer firstTabID=tabs.get(0).getObjectID();
			ITab firstTab= DashboardTabDesignBL.getInstance().loadFullTab(firstTabID);
			DashboardPanelDesignBL dashboardPanelDesignBL=new DashboardPanelDesignBL();
			dashboardPanelDesignBL.setLocale(locale);
			dashboardPanelDesignBL.calculateFieldWrappers(firstTab);
			List<ITab> tabsFull=new ArrayList<ITab>();
			tabsFull.add(firstTab);
			result.setTabs(tabsFull);
		}
		return result;
	}
	@Override
	protected String appendExtraInitJSON(IScreen screen,Integer selectedTab){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendIntegerValue(sb,"projectID",projectID);
		JSONUtility.appendIntegerValue(sb,"entityType",entityType);
		return  sb.toString();
	}

	@Override
	public AbstractScreenDesignBL getScreenDesignBL() {
		return DashboardScreenDesignBL.getInstance();
	}

	@Override
	public AbstractTabDesignBL getTabDesignBL() {
		return DashboardTabDesignBL.getInstance();
	}

	@Override
	public AbstractPanelDesignBL getPanelDesignBL() {
		return DashboardPanelDesignBL.getInstance();
	}
	public AbstractFieldDesignBL getFieldDesignBL(){
		return DashboardFieldDesignBL.getInstance();
	}

	@Override
	public ScreenFactory getScreenFactory() {
		return DashboardScreenFactory.getInstance();
	}	
	/**
	 * @param fromAdmin the fromAdmin to set
	 */
	public void setFromAdmin(Boolean fromAdmin) {
		this.fromAdmin = fromAdmin;
	}


	public String localizeTab(String label){
		if(getText("dashboard.tab.label."+label).equals("dashboard.tab.label."+label)){
			return label;
		}else{
			return getText("dashboard.tab.label."+label);
		}
	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getEntityType() {
		return entityType;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}

	public String getLayoutCls() {
		return "com.trackplus.layout.DashboardEditLayout";
	}

	public String getPageTitle() {
		return "cockpit.screenEdit.title";
	}

}
