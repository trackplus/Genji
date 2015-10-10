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

package com.aurel.track.admin.user.userLevel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TUserLevelBean;
import com.aurel.track.beans.TUserLevelSettingBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.UserLevelDAO;
import com.aurel.track.dao.UserLevelSettingDAO;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LocaleHandler;

/**
 * Utility class for using userlevels
 * @author Tamas
 *
 */
public class UserLevelBL {
	 /**
	 * The hardcoded IDs for actions
	 * @author Tamas
	 */
	public static interface USER_LEVEL_ACTION_IDS {
		//100 cockpit
		public static int COCKPIT = 100;
	    public static int CONFIGURE_COCKPIT = 101;
	    //200 project cockpit
	    public static int PROJECT_COCKPIT = 200;
	    //300 item navigator
	    public static int ITEM_NAVIGATOR = 300;
	    //31x main filter
	    public static int ITEM_NAVIGATOR_MAIN_FILTER = 310;
	    public static int MAIN_FILTER_PROJECT = 311;
	    public static int MAIN_FILTER_FILTER = 312;
	    public static int MAIN_FILTER_STATUS = 313;
	    public static int MAIN_FILTER_BASKET = 314;
	    //32x subfilter
	    public static int ITEM_NAVIGATOR_SUBFILTER = 320;
	    public static int SUBFILTER_PROJECT = 321;
	    public static int SUBFILTER_STATUS = 322;
	    public static int SUBFILTER_PRIORITY = 323;
	    public static int SUBFILTER_ITEMTYPE = 324;
	    public static int SUBFILTER_BASKET = 325;
	    public static int SUBFILTER_SCHEDULE = 326;
	    //33x baskets
	    public static int PLANNED_ITEMS_BASKET = 331;
	    public static int NEXT_ACTIONS_BASKET = 332;
	    public static int REMINDER_BASKET = 333;
	    public static int DELEGATED_BASKET = 334;
	    public static int TRASH_BASKET = 335;
	    public static int INCUBATOR_BASKET = 336;
	    public static int REFERENCE_BASKET = 337;
	    //34x views
	    public static int ITEM_NAVIGATOR_FLAT_GRID_VIEW = 340;
	    public static int ITEM_NAVIGATOR_TREE_GRID_VIEW = 341;
	    public static int ITEM_NAVIGATOR_HIERARCHICAL_VIEW = 342;
	    public static int ITEM_NAVIGATOR_AGILE_VIEW = 344;
	    //400 reports
	    public static int REPORTS = 400;
	    //500 admin
	    public static int ADMINISTARTION = 500;
	    //52x projects
	    public static int PRIVATE_WORKSPACE = 521;
	    //54x customize
	    public static int MANAGE_FILTERS = 541;
	    public static int REPORT_TEMPLATES = 542;
	    public static int ROLES = 543;
	    public static int FORMS = 544;
	    public static int FIELDS = 545;
	    public static int LISTS = 546;
	    public static int USER_LEVEL = 547;
	    public static int WORKFLOWS = 548;
	    //55x actions
	    public static int ADMINISTARTION_ACTIONS = 550;
	}

	/**
	 * The keys to localize the actions
	 * @author Tamas
	 */
	 public static interface USER_LEVEL_ACTION_KEYS {
		public static String MAIN_MENU = "admin.customize.userLevel.lbl.mainMenu";
	    public static String COCKPIT = "menu.cockpit";
	    public static String CONFIGURE_COCKPIT = "cockpit.config";
	    public static String PROJECT_COCKPIT = "menu.browseProjects";
	    public static String ITEM_NAVIGATOR = "menu.findItems";
	    public static String ITEM_NAVIGATOR_MAIN_FILTER = "common.lbl.queries";
	    public static String MAIN_FILTER_PROJECT = "menu.admin.project";
	    public static String MAIN_FILTER_FILTER = "common.lbl.queries";
	    public static String MAIN_FILTER_BASKET = "common.lbl.basket";
	    public static String ITEM_NAVIGATOR_SUBFILTER = "common.lbl.subfilter";
	    public static String SUBFILTER_PROJECT = "menu.admin.project";
	    public static String SUBFILTER_BASKET = "common.lbl.basket";
	    public static String SUBFILTER_SCHEDULE = "itemov.lbl.scheduled";
	    public static String ITEM_NAVIGATOR_VIEW = "admin.customize.queryFilter.lbl.view";
	    public static String ITEM_NAVIGATOR_FLAT_GRID_VIEW = "itemov.viewMode.flatGridView";
	    public static String ITEM_NAVIGATOR_TREE_GRID_VIEW = "itemov.viewMode.treeGridView";
	    public static String ITEM_NAVIGATOR_HIERARCHICAL_VIEW = "itemov.viewMode.wbsView";
	    public static String ITEM_NAVIGATOR_GANTT_VIEW = "itemov.viewMode.ganttChartView";
	    public static String ITEM_NAVIGATOR_AGILE_VIEW = "itemov.viewMode.cardView";
	    public static String REPORTS = "menu.reports";
	    public static String ADMINISTARTION = "menu.admin";
	    public static String PROJECTS = "menu.admin.project";
	    public static String PRIVATE_WORKSPACE = "admin.project.lbl.privateProject";
	    public static String CUSTOMIZE = "menu.admin.custom";
	    public static String MANAGE_FILTERS = "menu.admin.custom.queryFilters";
	    public static String REPORT_TEMPLATES = "menu.admin.custom.reportTemplates";
	    public static String ROLES = "menu.admin.custom.roles";
	    public static String FORMS = "menu.admin.custom.customForms";
	    public static String FIELDS = "menu.admin.custom.customField";
	    public static String LISTS = "menu.admin.custom.list";
	    public static String USER_LEVEL = "menu.admin.custom.userLevels";
	    public static String WORKFLOWS = "menu.admin.custom.workflow";
	    public static String ADMINISTARTION_ACTIONS = "menu.admin.action";
	}



	private static final Logger LOGGER = LogManager.getLogger(UserLevelBL.class);
	private static UserLevelDAO userLevelDAO = DAOFactory.getFactory().getUserLevelDAO();
	private static UserLevelSettingDAO userLevelSettingDAO = DAOFactory.getFactory().getUserLevelSettingDAO();

	/*
	 * User level methods
	 */
	/**
	 * Loads a TUserLevelBean by primary key
	 * @param objectID
	 * @return
	 */
	public static TUserLevelBean loadByPrimaryKey(Integer objectID) {
		return userLevelDAO.loadByPrimaryKey(objectID);
	}


	/**
	 * Loads all user level beans
	 * @return
	 */
	public static List<TUserLevelBean> loadCustomUserLevels() {
		return userLevelDAO.loadAll();
	}

	/**
	 * Saves a userLevelBean to the TUserLevel table.
	 * @param userLevelBean
	 * @return
	 */
	public static Integer save(TUserLevelBean userLevelBean) {
		return userLevelDAO.save(userLevelBean);
	}

	/**
	 * Whether the user level has dependent data (is assigned to any user)
	 * @param userLevelID
	 * @return
	 */
	public static boolean hasDependentData(Integer userLevelID) {
		return userLevelDAO.hasDependentData(userLevelID);
	}

	/**
	 * Replaces the oldUserLevelID with newUserLevelID
	 * @param oldUserLevelID
	 * @param newUserLevelID
	 * @return
	 */
	public static void replace(Integer oldUserLevelID, Integer newUserLevelID) {
		userLevelDAO.replace(oldUserLevelID, newUserLevelID);
	}

	/**
	 * Deletes a userLevelID
	 * @param userLevelID
	 * @return
	 */
	public static void delete(Integer userLevelID) {
		userLevelDAO.delete(userLevelID);
	}

	/*
	 * User level action DAO methods
	 */

	/**
	 * Loads all settings
	 * @return
	 */
	public static List<TUserLevelSettingBean> loadAllSettings() {
		return userLevelSettingDAO.loadAll();
	}

	/**
	 * Loads the settings for a user level
	 * @param userLevelID
	 * @return
	 */
	public static List<TUserLevelSettingBean> loadActionsByUserLevel(Integer userLevelID) {
		return userLevelSettingDAO.loadByUserLevel(userLevelID);
	}

	/**
	 * Loads the settings for a user level
	 * @param userLevelID
	 * @param actionID
	 * @return
	 */
	public static List<TUserLevelSettingBean> loadActionsByUserLevelAndAction(Integer userLevelID, Integer actionID) {
		return userLevelSettingDAO.loadByUserLevelAndAction(userLevelID, actionID);
	}

	/**
	 * Saves a userLevelSetting to the TUuserLevelSetting table.
	 * @param userLevelSettingBean
	 * @return
	 */
	public static Integer saveAction(TUserLevelSettingBean userLevelSettingBean) {
		return userLevelSettingDAO.save(userLevelSettingBean);
	}

	/**
	 * Deletes a userLevelSetting by userLevel and actionID
	 * @param userLevelID
	 * @param actionID
	 * @return
	 */
	public static void deleteAction(Integer userLevelID, Integer actionID) {
		userLevelSettingDAO.delete(userLevelID, actionID);
	}



	/**
	 * Loads all user level beans
	 * @return
	 */
	public static List<TUserLevelBean> loadCustomUserLevels(Locale locale) {
		List<TUserLevelBean> userLevelBeans = loadCustomUserLevels();
		LocalizeUtil.localizeDropDownList(userLevelBeans, locale);
		return userLevelBeans;
	}

	/**
	 * Loads all user level beans
	 * @return
	 */
	public static List<TUserLevelBean> loadAllUserLevels() {
		List<TUserLevelBean> userLevels = new LinkedList<TUserLevelBean>();
		TUserLevelBean clientUserLevel = new TUserLevelBean();
		clientUserLevel.setObjectID(TPersonBean.USERLEVEL.CLIENT);
		clientUserLevel.setLabel(LocalizeUtil.getLocalizedEntity(
        		new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.CLIENT, Locale.ENGLISH));
		userLevels.add(clientUserLevel);
		TUserLevelBean fullUserLevel = new TUserLevelBean();
		fullUserLevel.setObjectID(TPersonBean.USERLEVEL.FULL);
		fullUserLevel.setLabel(LocalizeUtil.getLocalizedEntity(
        		new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.FULL, Locale.ENGLISH));
		userLevels.add(fullUserLevel);
		TUserLevelBean sysAdminUserLevel = new TUserLevelBean();
		sysAdminUserLevel.setObjectID(TPersonBean.USERLEVEL.SYSADMIN);
		sysAdminUserLevel.setLabel(LocalizeUtil.getLocalizedEntity(
        		new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.SYSADMIN, Locale.ENGLISH));
		userLevels.add(sysAdminUserLevel);
		TUserLevelBean sysManUserLevel = new TUserLevelBean();
		sysManUserLevel.setObjectID(TPersonBean.USERLEVEL.SYSMAN);
		sysManUserLevel.setLabel(LocalizeUtil.getLocalizedEntity(
        		new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.SYSMAN, Locale.ENGLISH));
		userLevels.add(sysManUserLevel);
		userLevels.addAll(userLevelDAO.loadAll());
		return userLevels;
	}


	/**
     * Gets the map with the user level strings
     * @param locale
     * @return
     */
    public static List<IntegerStringBean> getUserLevelsList(Locale locale) {
    	List<IntegerStringBean> userLevelsList = new LinkedList<IntegerStringBean>();
    	if (!ApplicationBean.getApplicationBean().isGenji()) {
    		userLevelsList.add(new IntegerStringBean(LocalizeUtil.getLocalizedEntity(
        		new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.CLIENT, locale), TPersonBean.USERLEVEL.CLIENT));
    	}
        userLevelsList.add(new IntegerStringBean(LocalizeUtil.getLocalizedEntity(
        		new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.FULL, locale), TPersonBean.USERLEVEL.FULL));
        userLevelsList.add(new IntegerStringBean(LocalizeUtil.getLocalizedEntity(
        		new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.SYSADMIN, locale), TPersonBean.USERLEVEL.SYSADMIN));
        userLevelsList.add(new IntegerStringBean(LocalizeUtil.getLocalizedEntity(
        		new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.SYSMAN, locale), TPersonBean.USERLEVEL.SYSMAN));
        userLevelsList.addAll(getExtraUserLevelsList(locale));
        return userLevelsList;
    }

    static List<Integer> getUserLevelActions() {
    	List<Integer> userLevelActions = new LinkedList<Integer>();
    	userLevelActions.add(USER_LEVEL_ACTION_IDS.COCKPIT);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.CONFIGURE_COCKPIT);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.PROJECT_COCKPIT);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_MAIN_FILTER);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.MAIN_FILTER_PROJECT);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.MAIN_FILTER_FILTER);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.MAIN_FILTER_STATUS);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.MAIN_FILTER_BASKET);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_SUBFILTER);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.SUBFILTER_PROJECT);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.SUBFILTER_STATUS);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.SUBFILTER_PRIORITY);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.SUBFILTER_ITEMTYPE);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.SUBFILTER_BASKET);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.SUBFILTER_SCHEDULE);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.PLANNED_ITEMS_BASKET);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.NEXT_ACTIONS_BASKET);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.REMINDER_BASKET);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.DELEGATED_BASKET);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.TRASH_BASKET);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.INCUBATOR_BASKET);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.REFERENCE_BASKET);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_FLAT_GRID_VIEW);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_TREE_GRID_VIEW);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_HIERARCHICAL_VIEW);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_AGILE_VIEW);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.REPORTS);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.ADMINISTARTION);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.PRIVATE_WORKSPACE);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.MANAGE_FILTERS);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.REPORT_TEMPLATES);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.ROLES);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.FORMS);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.FIELDS);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.LISTS);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.USER_LEVEL);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.WORKFLOWS);
	    userLevelActions.add(USER_LEVEL_ACTION_IDS.ADMINISTARTION_ACTIONS);
	    return userLevelActions;
    }

    /**
     * All actions allowed
     * @return
     */
    static Map<Integer, Boolean> getFullUserActionsMap() {
        Map<Integer, Boolean> actionsMap = new HashMap<Integer, Boolean>();
        List<Integer> userLevelActions = getUserLevelActions();
        for (Integer actionID : userLevelActions) {
            actionsMap.put(actionID, Boolean.TRUE);
        }
        return actionsMap;
    }

    /**
     * Access for the easy user
     * @return
     */
    static Map<Integer, Boolean> getEasyUserActionsMap() {
    	Map<Integer, Boolean> actionsMap = new HashMap<Integer, Boolean>();
        List<Integer> userLevelActions = getUserLevelActions();
        for (Integer actionID : userLevelActions) {
            actionsMap.put(actionID, Boolean.FALSE);
        }
        return actionsMap;
    }


    /**
     * Gets the extra user levels map
     * @param locale
     * @return
     */
    public static List<IntegerStringBean> getExtraUserLevelsList(Locale locale) {
    	List<IntegerStringBean> extraUserLevels = new LinkedList<IntegerStringBean>();
        List<TUserLevelBean> userLevelBeans = loadCustomUserLevels(locale);
        if (userLevelBeans!=null) {
            for (TUserLevelBean userLevelBean : userLevelBeans) {
                Integer objectID = userLevelBean.getObjectID();
                String label = userLevelBean.getLabel();
                extraUserLevels.add(new IntegerStringBean(label, objectID));
            }
        }
        return extraUserLevels;
    }

    private static Map<String, Integer> getActionLabelToActionIDMap() {
    	 Map<String, Integer> actionLabelToActionIDMap = new HashMap<String, Integer>();
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_PRIVATE_WORKSPACE, USER_LEVEL_ACTION_IDS.PRIVATE_WORKSPACE);

    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_COCKPIT, USER_LEVEL_ACTION_IDS.COCKPIT);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.CONFIGURE_COCKPIT, USER_LEVEL_ACTION_IDS.CONFIGURE_COCKPIT);

    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_PROJECT_COCKPIT, USER_LEVEL_ACTION_IDS.PROJECT_COCKPIT);

    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_ITEM_NAVIGATOR, USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_FILTERS, USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_MAIN_FILTER);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_FILTERS_HAS_WORKSPACES, USER_LEVEL_ACTION_IDS.MAIN_FILTER_PROJECT);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_FILTERS_HAS_FILTERS, USER_LEVEL_ACTION_IDS.MAIN_FILTER_FILTER);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_FILTERS_HAS_BASKETS, USER_LEVEL_ACTION_IDS.MAIN_FILTER_BASKET);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_FILTERS_HAS_STATES, USER_LEVEL_ACTION_IDS.MAIN_FILTER_STATUS);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_SUBFILTERS, USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_SUBFILTER);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_WORKSPACES, UserLevelBL.USER_LEVEL_ACTION_IDS.SUBFILTER_PROJECT);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_BASKETS, USER_LEVEL_ACTION_IDS.SUBFILTER_BASKET);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_STATES, USER_LEVEL_ACTION_IDS.SUBFILTER_STATUS);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_ITEM_TYPES, USER_LEVEL_ACTION_IDS.SUBFILTER_ITEMTYPE);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_PRIORITIES, USER_LEVEL_ACTION_IDS.SUBFILTER_PRIORITY);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_SCHEDULE, USER_LEVEL_ACTION_IDS.SUBFILTER_SCHEDULE);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_SIMPLE_VIEW, USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_FLAT_GRID_VIEW);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_COMPLEX_VIEW, USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_TREE_GRID_VIEW);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_WBS, USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_HIERARCHICAL_VIEW);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ITEM_NAVIGATOR_HAS_CARD_VIEW, USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_AGILE_VIEW);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_BASKET_PLANNED_ITEMS, USER_LEVEL_ACTION_IDS.PLANNED_ITEMS_BASKET);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_BASKET_NEXT_ACTIONS, USER_LEVEL_ACTION_IDS.NEXT_ACTIONS_BASKET);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_BASKET_REMINDER, USER_LEVEL_ACTION_IDS.REMINDER_BASKET);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_BASKET_DELEGATED, USER_LEVEL_ACTION_IDS.DELEGATED_BASKET);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_BASKET_INCUBATOR, USER_LEVEL_ACTION_IDS.INCUBATOR_BASKET);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_BASKET_TRASH, USER_LEVEL_ACTION_IDS.TRASH_BASKET);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_BASKET_REFERENCE, USER_LEVEL_ACTION_IDS.REFERENCE_BASKET);

    	 actionLabelToActionIDMap.put(UserLevelsFromFile.USE_REPORTS, USER_LEVEL_ACTION_IDS.REPORTS);

    	 actionLabelToActionIDMap.put(UserLevelsFromFile.HAS_ADMIN_MENU, USER_LEVEL_ACTION_IDS.ADMINISTARTION);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.MANAGE_FILTERS, USER_LEVEL_ACTION_IDS.MANAGE_FILTERS);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.MANAGE_REPORTS, USER_LEVEL_ACTION_IDS.REPORT_TEMPLATES);

    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ADMINISTRTION_CAN_SEE_USER_ROLES, USER_LEVEL_ACTION_IDS.ROLES);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ADMINISTRTION_MANAGE_FORMS, USER_LEVEL_ACTION_IDS.FORMS);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ADMINISTRTION_MANAGE_FIELDS, USER_LEVEL_ACTION_IDS.FIELDS);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ADMINISTRTION_MANAGE_LISTS, USER_LEVEL_ACTION_IDS.LISTS);
    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ADMINISTRTION_CAN_SEE_WORKFLOWS, USER_LEVEL_ACTION_IDS.WORKFLOWS);

    	 actionLabelToActionIDMap.put(UserLevelsFromFile.ADMINISTRTION_CAN_EXECUTE_ACTIONS, USER_LEVEL_ACTION_IDS.ADMINISTARTION_ACTIONS);
    	 return actionLabelToActionIDMap;
    }



    public static void migrateFromProperyFileToDatabase(String initDataDir) {
    	List<TUserLevelBean> customUserLevels = UserLevelBL.loadCustomUserLevels();
	    if (customUserLevels==null || customUserLevels.isEmpty()) {
	    	LOGGER.debug("No custom user levels found. Try to migrate from UserLevels.properties");
	    	UserLevelsFromFile userLevelsInstance = UserLevelsFromFile.getInstance(initDataDir);
	    	/**
	    	 * userLevelID to actionID to boolean
	    	 */
	    	Map<Integer, SortedMap<String, Boolean>> userLevelsMap = userLevelsInstance.getUserLevelsMap();
	    	/**
	    	 * UserLevelName to userLevelID
	    	 */
	    	Map<String, Integer> userDefinedExtraUserLevelsMap = userLevelsInstance.getUserDefinedExtraUserLevelsMap();
	    	String keyPrefix = new TUserLevelBean().getKeyPrefix();
	    	Map<String, Integer> actionLabelToActionIDMap = getActionLabelToActionIDMap();
	    	for (Map.Entry<String, Integer> entry : userDefinedExtraUserLevelsMap.entrySet()) {
				String userLevelLabel = entry.getKey();
				Integer oldUserLevelID = entry.getValue();
				TUserLevelBean userLevelBean = new TUserLevelBean();
				userLevelBean.setLabel(userLevelLabel);
				Integer newUserLevelID = save(userLevelBean);
				if (newUserLevelID==null) {
					LOGGER.warn("The user level " + userLevelLabel + " with level ID " + oldUserLevelID + " was not saved in database");
					continue;
				}
				LOGGER.debug("The user level " + userLevelLabel + " with level ID " + oldUserLevelID + " was saved in database with ID " + newUserLevelID);
				Map<String, String> localeMap = userLevelsInstance.getLocalizedLevelsMap(oldUserLevelID);
				if (localeMap!=null) {
					for (Map.Entry<String, String> localeEntry : localeMap.entrySet()) {
						String localeStr = localeEntry.getKey();
						String userLevelLocalizedLabel = localeEntry.getValue();
						if ("en".equals(localeStr)) {
							//save the English also as default
							LocalizeBL.saveLocalizedResource(keyPrefix, newUserLevelID, userLevelLocalizedLabel, null);
						}
						Locale locale = LocaleHandler.getLocaleFromString(localeStr);
						if (locale!=null) {
							LocalizeBL.saveLocalizedResource(keyPrefix, newUserLevelID, userLevelLocalizedLabel, locale);
							LOGGER.debug("Saved for locale " + localeStr + " as " + userLevelLocalizedLabel);
						}
					}
				}
				SortedMap<String, Boolean> userLevelMap = userLevelsMap.get(oldUserLevelID);
				if (userLevelMap!=null) {
					//for each possible action
					for (Map.Entry<String, Integer> actionLabelToIDEntry : actionLabelToActionIDMap.entrySet()) {
						String actionLabel = actionLabelToIDEntry.getKey();
						Integer actionID = actionLabelToIDEntry.getValue();
						Boolean active = userLevelMap.get(actionLabel);
						if (active==null) {
							//the missing entry is interpreted as active/enabled
							active = Boolean.TRUE;
						}
						if (active!=null) {
							//action present in file
							TUserLevelSettingBean userLevelSettingBean = new TUserLevelSettingBean();
							userLevelSettingBean.setUserLevel(newUserLevelID);
							userLevelSettingBean.setACTIONKEY(actionID);
							userLevelSettingBean.setActive(active.booleanValue());
							saveAction(userLevelSettingBean);
							LOGGER.debug("Save action " + actionLabel + " active " + active.booleanValue() + " with ID " + actionID);

						}
					}
				}
				UserLevelBL.replace(oldUserLevelID, newUserLevelID);
			}
    	}
    }
}
