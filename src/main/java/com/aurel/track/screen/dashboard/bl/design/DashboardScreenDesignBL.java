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

package com.aurel.track.screen.dashboard.bl.design;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL.PREDEFINED_QUERY;
import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dbase.InitDatabase;
import com.aurel.track.plugin.PluginDescriptor;
import com.aurel.track.report.dashboard.StatusOverTimeGraph;
import com.aurel.track.report.dashboard.TimePeriodDashboardView.TIMEPERIOD_PARAMETERS;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.design.AbstractScreenDesignBL;
import com.aurel.track.screen.dashboard.adapterDAO.DashboardScreenFactory;
import com.aurel.track.screen.dashboard.bl.DashboardUtil;


public class DashboardScreenDesignBL extends AbstractScreenDesignBL {
	//singleton instance
	private static DashboardScreenDesignBL instance;

	public static final Integer CLIENT_COCKPIT_TEMPLATE_ID = Integer.valueOf(-1);
	/**
	 * get a singleton instance
	 * @return
	 */
	public static DashboardScreenDesignBL getInstance() {
		if (instance == null) {
			instance = new DashboardScreenDesignBL();
		}
		return instance;
	}

	@Override
	protected ScreenFactory getScreenFactory() {
		return DashboardScreenFactory.getInstance();
	}

	/**
	 * constructor
	 */
	public DashboardScreenDesignBL() {
		super();
	}

	public Integer copyProjectScreen(Integer screenID,String name,String description,Integer projectID,Integer entityType){
		IScreen screen=loadScreen(screenID);
		TDashboardScreenBean clone=(TDashboardScreenBean)getScreenFactory().createIScreeenInstance();
		clone.setName(name);
		clone.setLabel(name==null?screen.getLabel()+"(1)":name);
		clone.setDescription(description==null?screen.getDescription():description);
		clone.setProject(projectID);
		clone.setEntityType(entityType);
		return saveCopyClone(screen, clone);
	}

	public Integer saveAsTemplateDashboard(Integer screenID, String name,
							  String description, String tagLabel, Integer userID){
		IScreen screen=loadScreen(screenID);
		TDashboardScreenBean clone=(TDashboardScreenBean)getScreenFactory().createIScreeenInstance();
		clone.setName(name);
		clone.setLabel(name==null?screen.getLabel()+"(1)":name);
		clone.setDescription(description==null?screen.getDescription():description);
		clone.setOwner(userID);
		clone.setTagLabel(tagLabel);
		return saveCopyClone(screen, clone);
	}




	/**
	 * Load the TDashboardScreenBean by personID
	 * If not found for the person try the default one
	 * If neither found create a new for personID
	 * @param personBean
	 * @return
	 */
	public TDashboardScreenBean loadByPerson(TPersonBean personBean){
		Integer personID=personBean.getObjectID();
		TDashboardScreenBean result=(TDashboardScreenBean)getScreenFactory().getScreenDAO().loadByPerson(personID);
		if(result==null){
			result = (TDashboardScreenBean)getScreenFactory().getScreenDAO().loadDefault();
			if(result!=null){
				Integer copyClone=copyScreen(result.getObjectID(),result.getName()+"("+personBean.getUsername()+")",
						result.getDescription(), null, personID);
				result=(TDashboardScreenBean)getScreenFactory().getScreenDAO().loadByPrimaryKey(copyClone);
			}
			if (result==null) {
				result=createNewDashboardScreen("default","defaultDashboard",personID,null,null);
			}
		}
		List tabs=getScreenFactory().getTabDAO().loadByParent(result.getObjectID());
		result.setTabs(tabs);
		return result;
	}
	public TDashboardScreenBean loadByPersonNotCreateDefault(Integer objectID){
		return (TDashboardScreenBean) getScreenFactory().getScreenDAO().loadByPerson(objectID);
	}
	/**
	 *
	 * @param personID
	 * @param projectID
	 * @param entityType
	 * @return
	 */
	public TDashboardScreenBean loadByProject(Integer personID,Integer projectID,Integer entityType){
		TDashboardScreenBean result=DAOFactory.getFactory().getDashboardScreenDAO().loadByProject(projectID, entityType);
		if(result==null){
			//if (result==null) {
				result=createNewDashboardScreenProject("projectDash","defaultProjectDashboard",personID,projectID,entityType);
		}
		List tabs=getScreenFactory().getTabDAO().loadByParent(result.getObjectID());
		result.setTabs(tabs);
		return result;
	}



    public void checkAndCreateClientDefaultCockpit() {
    	TDashboardScreenBean screen = null;
    	try {
    		screen = (TDashboardScreenBean)DashboardScreenDesignBL.getInstance().tryToLoadScreen(CLIENT_COCKPIT_TEMPLATE_ID);
    		LOGGER.debug("Client default cockpit already exists.");
    	}catch(Exception ex) {
    		LOGGER.debug("Couldn't find client default cockpit, we are now going to create one.");
    	}
    	if(screen == null) {
	    	Connection cono = null;
	    	Statement ostmt = null;
			Locale locale = Locale.getDefault();
			LOGGER.debug("Using default locale:" + locale.getDisplayName());
	    	String templateName = "Client user default template";
	    	String description = "This template is assigned automatically to newly created client users";
	    	String templateLabel = "Client user default template";
			try {
				cono = InitDatabase.getConnection();
				ostmt = cono.createStatement();
				cono.setAutoCommit(false);
				String createClientDefaultTemplate = "INSERT INTO TDASHBOARDSCREEN (OBJECTID, NAME, LABEL, DESCRIPTION)"
						+ "VALUES (" + CLIENT_COCKPIT_TEMPLATE_ID + ",'" + templateName + "', '" + templateLabel + "', '" + description +"')";
				ostmt.executeUpdate(createClientDefaultTemplate);
				cono.commit();
				cono.setAutoCommit(true);
				LOGGER.debug("Client user default cockpit screen has been created with ID: " + CLIENT_COCKPIT_TEMPLATE_ID);
				createClientDefaultCockpit();
			} catch (Exception e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			} finally {
				if (ostmt!=null) {
					try {
						ostmt.close();
					} catch (SQLException e) {
						LOGGER.warn(ExceptionUtils.getStackTrace(e));
					}
				}
				try {
					if (cono != null) {
						cono.close();
					}
				} catch (Exception e) {
					LOGGER.info("Closing the connection failed with " + e.getMessage());
					LOGGER.warn(ExceptionUtils.getStackTrace(e));
				}
			}
    	}
    }

    private void createClientDefaultCockpit() {
    	TDashboardScreenBean screen = (TDashboardScreenBean)DashboardScreenDesignBL.getInstance().loadScreen(CLIENT_COCKPIT_TEMPLATE_ID);
    	screen.setProject(null);
    	screen.setEntityType(null);
    	screen.setPerson(null);
    	Integer newPk=screenDAO.save(screen);
		screen.setObjectID(newPk);
		ITab tab=getScreenFactory().createITabInstance();
		tab.setDescription("Main tab");
		tab.setName("Main");
		tab.setLabel("Main");
		tab.setParent(newPk);
		Integer tabPk=tabDAO.save(tab);
		String[] defaultPlugins= DashboardUtil.getDefaultPluginForClientUsers();
		IPanel pan=getScreenFactory().createIPanelInstance();
		pan.setParent(tabPk);
		pan.setRowsNo(new Integer(2));
		pan.setColsNo(new Integer(2));
		Map<String, String> dashBoardParameters = new HashMap<String, String>();
		Integer panPk=panelDAO.save(pan);
		for (int i = 0; i < defaultPlugins.length; i++) {
			String defaultPlugin = defaultPlugins[i];
			TDashboardFieldBean field=(TDashboardFieldBean)getScreenFactory().createIFieldInstance();
			switch (i) {
			case 0:
				field.setRowIndex(new Integer(0));
				field.setColIndex(new Integer(0));
				dashBoardParameters.put("selFilter", Integer.toString(PREDEFINED_QUERY.OUTSTANDING));
				field.setParametres(dashBoardParameters);
				break;
			case 1:
				field.setRowIndex(new Integer(0));
				field.setColIndex(new Integer(1));
				dashBoardParameters.put("selFilter", Integer.toString(PREDEFINED_QUERY.MY_ITEMS));
				field.setParametres(dashBoardParameters);
				break;
			case 2:
				field.setRowIndex(new Integer(1));
				field.setColIndex(new Integer(0));
				dashBoardParameters.put("selFilter", Integer.toString(PREDEFINED_QUERY.CLOSED_RECENTLY));
				field.setParametres(dashBoardParameters);
				break;
			default:
				break;
			}
			field.setRowSpan(new Integer(1));
			field.setColSpan(new Integer(1));
			PluginDescriptor descr = DashboardUtil.getDescriptor(defaultPlugin);
			field.setName(descr.getName());
			field.setDescription(descr.getDescription());
			field.setDashboardID(defaultPlugin);
			field.setParent(panPk);
			fieldDAO.save(field);
			LOGGER.debug("Client user default cockpit has been created, with default items!");
		}

    }

	public  TDashboardScreenBean createNewDashboardScreen(String name,String description,Integer personID,Integer projectID,Integer entityType){
		TDashboardScreenBean s=(TDashboardScreenBean)getScreenFactory().createIScreeenInstance();
		s.setProject(projectID);
		s.setEntityType(entityType);
		s.setName(name);
		s.setLabel(name);
		s.setPerson(personID);
		s.setDescription(description);
		Integer newPk=screenDAO.save(s);
		s.setObjectID(newPk);
		ITab tab=getScreenFactory().createITabInstance();
		tab.setDescription("Main tab");
		tab.setName("Main");
		tab.setLabel("Main");
		tab.setParent(newPk);
		Integer tabPk=tabDAO.save(tab);

		String[] defaultPlugins= DashboardUtil.getDefautPlugins();
		for (int i = 0; i < defaultPlugins.length; i++) {
			String defaultPlugin = defaultPlugins[i];
			IPanel pan=getScreenFactory().createIPanelInstance();
			pan.setParent(tabPk);
			pan.setRowsNo(new Integer(1));
			pan.setColsNo(new Integer(1));

			Integer panPk=panelDAO.save(pan);

			TDashboardFieldBean field=(TDashboardFieldBean)getScreenFactory().createIFieldInstance();
			field.setRowIndex(new Integer(0));
			field.setColIndex(new Integer(0));
			field.setRowSpan(new Integer(1));
			field.setColSpan(new Integer(1));
			PluginDescriptor descr=DashboardUtil.getDescriptor(defaultPlugin);
			field.setName(descr.getName());
			field.setDescription(descr.getDescription());
			field.setDashboardID(defaultPlugin);
			field.setParent(panPk);
			fieldDAO.save(field);
		}
		return s;
	}
	private TDashboardScreenBean createNewDashboardScreenProject(String name,String description,Integer personID,Integer projectID,Integer entityType){
		TDashboardScreenBean s=(TDashboardScreenBean)getScreenFactory().createIScreeenInstance();
		s.setProject(projectID);
		s.setEntityType(entityType);
		s.setName(name);
		s.setLabel(name);
		s.setPerson(personID);
		s.setDescription(description);
		Integer newPk=screenDAO.save(s);
		s.setObjectID(newPk);

		ITab tab=getScreenFactory().createITabInstance();
		tab.setDescription("Main tab");
		tab.setName("Main");
		tab.setLabel("Main");
		tab.setParent(newPk);
		Integer tabPk=tabDAO.save(tab);

		IPanel pan=getScreenFactory().createIPanelInstance();
		pan.setParent(tabPk);
		pan.setRowsNo(new Integer(2));
		pan.setColsNo(new Integer(3));

		Integer panPk=panelDAO.save(pan);

		createDashField("com.aurel.track.report.dashboard.MyItems", panPk, 0, 0, 1,1,null);
		Map<String,String> paramsSOT=new HashMap<String, String>();
		paramsSOT.put(TIMEPERIOD_PARAMETERS.SELECTED_PERIOD_TYPE,StatusOverTimeGraph.PERIOD_TYPE.DAYS_BEFORE+"");
		paramsSOT.put(TIMEPERIOD_PARAMETERS.DAYS_BEFORE,"60");
		paramsSOT.put(StatusOverTimeGraph.CONFIGURATION_PARAMETERS.SELECTED_TIME_INTERVAL,StatusOverTimeGraph.TIME_INTERVAL.WEEK+"");
		paramsSOT.put(StatusOverTimeGraph.CONFIGURATION_PARAMETERS.SELECTED_CALCULATION_MODE,StatusOverTimeGraph.CALCULATION_MODE.ACTUAL_SAMPLE+"");
		createDashField("com.aurel.track.report.dashboard.StatusOverTimeGraph", panPk, 0, 1, 1,1,paramsSOT);

		createDashField("com.aurel.track.report.dashboard.ReportsAndFilters", panPk, 0, 2, 2,1,null);

		Map<String,String> paramsSOT_below=new HashMap<String,String>();
		paramsSOT_below.put(TIMEPERIOD_PARAMETERS.SELECTED_PERIOD_TYPE,StatusOverTimeGraph.PERIOD_TYPE.DAYS_BEFORE+"");
		paramsSOT_below.put(TIMEPERIOD_PARAMETERS.DAYS_BEFORE,"60");
		paramsSOT_below.put(StatusOverTimeGraph.CONFIGURATION_PARAMETERS.SELECTED_TIME_INTERVAL,StatusOverTimeGraph.TIME_INTERVAL.WEEK+"");
		paramsSOT_below.put(StatusOverTimeGraph.CONFIGURATION_PARAMETERS.SELECTED_CALCULATION_MODE,StatusOverTimeGraph.CALCULATION_MODE.NEW+"");
		createDashField("com.aurel.track.report.dashboard.StatusOverTimeGraph", panPk, 1, 0, 1,1,paramsSOT_below);

		createDashField("com.aurel.track.report.dashboard.TqlItems", panPk, 1, 1, 1,1,null);


		ITab tabRelNotes=getScreenFactory().createITabInstance();
		tabRelNotes.setDescription("Release notes");
		tabRelNotes.setName("ReleaseNotes");
		tabRelNotes.setLabel("Release notes");
		tabRelNotes.setParent(newPk);
		Integer tabRelNotesPk=tabDAO.save(tabRelNotes);

		IPanel panRelNotes=getScreenFactory().createIPanelInstance();
		panRelNotes.setParent(tabRelNotesPk);
		panRelNotes.setRowsNo(new Integer(1));
		panRelNotes.setColsNo(new Integer(1));
		panPk=panelDAO.save(panRelNotes);

		createDashField("com.aurel.track.report.dashboard.ReleaseNotes", panPk, 0, 0, 1,1,null);

		return s;
	}

	private void createDashField(String dashID,Integer panelID,Integer rowIndex,
			Integer colIndex,Integer rowSpan, Integer colSpan,Map<String,String> parameters){
		TDashboardFieldBean field=(TDashboardFieldBean)getScreenFactory().createIFieldInstance();
		field.setRowIndex(rowIndex);
		field.setColIndex(colIndex);
		field.setRowSpan(rowSpan);
		field.setColSpan(colSpan);

		PluginDescriptor descr=DashboardUtil.getDescriptor(dashID);
		field.setName(descr.getName());
		field.setDescription(descr.getDescription());
		field.setDashboardID(dashID);
		field.setParent(panelID);

		field.setParametres(parameters);

		fieldDAO.save(field);
	}
	public void copyScreen(Integer screenID,Integer userID,Integer projectID,Integer entityType){
		IScreen screen=loadScreen(screenID);
		TDashboardScreenBean clone=(TDashboardScreenBean) getScreenFactory().createIScreeenInstance();
		clone.setName(screen.getName());
		clone.setLabel(screen.getLabel());
		clone.setDescription(screen.getDescription());
		clone.setPersonID(userID);
		clone.setProject(projectID);
		clone.setEntityType(entityType);
		saveCopyClone(screen, clone);
	}

	@Override
	public String encodeJSON_ScreenProperies(IScreen screen) {
		return new DashboardScreenDesignJSON().encodeScreenProperties(screen);
	}

}
