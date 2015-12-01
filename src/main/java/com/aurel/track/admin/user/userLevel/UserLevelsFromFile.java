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

package com.aurel.track.admin.user.userLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.dbase.HandleHome;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.PropertiesConfigurationHelper;

/**
 * User level settings
 * @author Tamas Ruff
 *
 */
public class UserLevelsFromFile {

    private static final Logger LOGGER = LogManager.getLogger(UserLevelsFromFile.class);
    public static String HAS_PRIVATE_WORKSPACE = "HasPrivateWorkspace";
    public static String CONFIGURE_COCKPIT = "ConfigureCockpit";
    public static String HAS_PROJECT_COCKPIT = "HasProjectCockpit";
    public static String HAS_COCKPIT = "HasCockpit";

    public static String ITEM_NAVIGATOR_HAS_FILTERS = "ItemNavigator.HasFilters";
    public static String ITEM_NAVIGATOR_HAS_FILTERS_HAS_WORKSPACES = "ItemNavigator.HasFilters.HasWorkspaces";
    public static String ITEM_NAVIGATOR_HAS_FILTERS_HAS_FILTERS="ItemNavigator.HasFilters.HasFilters";
    public static String ITEM_NAVIGATOR_HAS_FILTERS_HAS_BASKETS="ItemNavigator.HasFilters.HasBaskets";
    public static String ITEM_NAVIGATOR_HAS_FILTERS_HAS_STATES="ItemNavigator.HasFilters.HasStates";

    public static String ITEM_NAVIGATOR_HAS_SUBFILTERS = "ItemNavigator.HasSubFilters";
    public static String ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_WORKSPACES="ItemNavigator.HasSubFilters.HasWorkspaces";
    public static String ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_FILTERS="ItemNavigator.HasSubFilters.HasFilters";
    public static String ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_BASKETS="ItemNavigator.HasSubFilters.HasBaskets";
    public static String ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_STATES="ItemNavigator.HasSubFilters.HasStates";
    public static String ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_ITEM_TYPES="ItemNavigator.HasSubFilters.HasItemTypes";
    public static String ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_PRIORITIES="ItemNavigator.HasSubFilters.HasPriorities";
    public static String ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_SCHEDULE="ItemNavigator.HasSubFilters.HasSchedule";

    public static String HAS_ITEM_NAVIGATOR = "HasItemNavigator";
    public static String ITEM_NAVIGATOR_HAS_COMPLEX_VIEW = "ItemNavigator.HasComplexView";
    public static String ITEM_NAVIGATOR_HAS_WBS = "ItemNavigator.HasWBS";
    public static String ITEM_NAVIGATOR_HAS_SIMPLE_VIEW = "ItemNavigator.HasSimpleView";
    public static String ITEM_NAVIGATOR_HAS_GANTT_VIEW = "ItemNavigator.HasGantt";
    public static String ITEM_NAVIGATOR_HAS_CARD_VIEW = "ItemNavigator.HasAgile";
    public static String ITEM_NAVIGATOR_HAS_FOLDERS = "ItemNavigator.HasFolders";
    public static String HAS_BASKETS = "HasBaskets";
    public static String HAS_BASKET_PLANNED_ITEMS = "HasBasket.PlannedItems";
    public static String HAS_BASKET_NEXT_ACTIONS = "HasBasket.NextActions";
    public static String HAS_BASKET_REMINDER = "HasBasket.Reminder";
    public static String HAS_BASKET_DELEGATED = "HasBasket.Delegated";
    public static String HAS_BASKET_INCUBATOR = "HasBasket.Incubator";
    public static String HAS_BASKET_TRASH = "HasBasket.Trash";
    public static String HAS_BASKET_REFERENCE = "HasBasket.Reference";
    public static String USE_REPORTS = "UseReports";
    public static String MANAGE_REPORTS = "ManageReports";
    public static String MANAGE_FILTERS = "ManageFilters";
    public static String USE_WIKI = "UseWiki";
    public static String HAS_ADMIN_MENU = "HasAdminMenu";
    //the following four entries are valid only if user is projectAdmin
    public static String ADMINISTRTION_CAN_SEE_USER_ROLES = "Administration.CanSeeUserRoles";
    public static String ADMINISTRTION_MANAGE_FORMS = "Administration.ManageForms";
    public static String ADMINISTRTION_MANAGE_FIELDS = "Administration.ManageFields";
    public static String ADMINISTRTION_MANAGE_LISTS = "Administration.ManageLists";
    public static String ADMINISTRTION_CAN_SEE_WORKFLOWS = "Administration.CanSeeWorkflows";
    public static String ADMINISTRTION_CAN_EXECUTE_ACTIONS = "Administration.CanExecuteActions";

    
    /**
     * If not null search in classpath of the web application for UserLevels.properties
     * Otherwise search in the <TRACKPLUS_HOME>
     */
    private String initDataDir;
    
    
    
    private static UserLevelsFromFile INSTANCE = null;

    public static final List<String> userLevelKeys = new ArrayList<String>();

    public static UserLevelsFromFile getInstance(String initDataDir){
        if (INSTANCE == null) {
            INSTANCE = new UserLevelsFromFile(initDataDir);
        }
        return INSTANCE;
    }

    private Map<Integer, SortedMap<String, Boolean>> userLevelsMap;
    private Map<String, Integer> userDefinedExtraUserLevelsMap = null;
    // This is <locale,localized level name>
    private Map<Integer, Map<String,String>> localeMap = null;


    private UserLevelsFromFile(String initDataDir) {
    	this.initDataDir = initDataDir;
        if (userLevelKeys.isEmpty()) {
            userLevelKeys.add(HAS_PRIVATE_WORKSPACE);
            userLevelKeys.add(CONFIGURE_COCKPIT);
            userLevelKeys.add(HAS_PROJECT_COCKPIT);
            userLevelKeys.add(HAS_COCKPIT);
            userLevelKeys.add(HAS_ITEM_NAVIGATOR);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_FILTERS);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_FILTERS_HAS_WORKSPACES);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_FILTERS_HAS_FILTERS);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_FILTERS_HAS_BASKETS);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_FILTERS_HAS_STATES);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_SUBFILTERS);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_WORKSPACES);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_FILTERS);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_BASKETS);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_STATES);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_ITEM_TYPES);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_PRIORITIES);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_SUBFILTERS_HAS_SCHEDULE);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_COMPLEX_VIEW);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_WBS);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_SIMPLE_VIEW);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_GANTT_VIEW);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_CARD_VIEW);
            userLevelKeys.add(ITEM_NAVIGATOR_HAS_FOLDERS);
            userLevelKeys.add(HAS_BASKETS);
            userLevelKeys.add(HAS_BASKET_PLANNED_ITEMS);
            userLevelKeys.add(HAS_BASKET_NEXT_ACTIONS);
            userLevelKeys.add(HAS_BASKET_REMINDER);
            userLevelKeys.add(HAS_BASKET_DELEGATED);
            userLevelKeys.add(HAS_BASKET_INCUBATOR);
            userLevelKeys.add(HAS_BASKET_TRASH);
            userLevelKeys.add(HAS_BASKET_REFERENCE);
            userLevelKeys.add(USE_REPORTS);
            userLevelKeys.add(MANAGE_REPORTS);
            userLevelKeys.add(MANAGE_FILTERS);
            userLevelKeys.add(USE_WIKI);
            userLevelKeys.add(HAS_ADMIN_MENU);
            //the following four entries are valid only if user is projectAdmin
            userLevelKeys.add(ADMINISTRTION_CAN_SEE_USER_ROLES);
            userLevelKeys.add(ADMINISTRTION_MANAGE_FORMS);
            userLevelKeys.add(ADMINISTRTION_MANAGE_FIELDS);
            userLevelKeys.add(ADMINISTRTION_MANAGE_LISTS);
            userLevelKeys.add(ADMINISTRTION_CAN_SEE_WORKFLOWS);
            userLevelKeys.add(ADMINISTRTION_CAN_EXECUTE_ACTIONS);
        }
        this.userLevelsMap = loadUserLevels();
    }

    

    public Map<Integer, SortedMap<String, Boolean>> getUserLevelsMap() {
		return userLevelsMap;
	}



	public Map<String, Integer> getUserDefinedExtraUserLevelsMap() {
        return userDefinedExtraUserLevelsMap;
    }

    public Map<String,String> getLocalizedLevelsMap(Integer level) {
        if (localeMap != null) {
            return localeMap.get(level);
        } else {
            return null;
        }
    }

    /**
     * Loads the user levels
     * This is a map of maps. The outer map is for each user level, and the inner
     * map contains the settings for each user level
     * @return
     */
    public Map<Integer, SortedMap<String, Boolean>> loadUserLevels() {
        Map<Integer, SortedMap<String, Boolean>> locUserLevelsMap = new HashMap<Integer, SortedMap<String,Boolean>>();
        userDefinedExtraUserLevelsMap = new HashMap<String, Integer>();
        localeMap = new HashMap <Integer, Map<String,String>>();
        String ON = "on";
        String OFF = "off";
        PropertiesConfiguration propertiesConfiguration = null;
        if (this.initDataDir==null) {
        	//get from <TRACKPLUS_HOME> probably migrate
        	propertiesConfiguration = PropertiesConfigurationHelper.getPathnamePropFile(HandleHome.getTrackplus_Home(), HandleHome.USER_LEVELS_FILE);
        } else {
        	//get from application context
        	try {
				propertiesConfiguration = PropertiesConfigurationHelper.loadServletContextPropFile(ApplicationBean.getInstance().getServletContext(), initDataDir, HandleHome.USER_LEVELS_FILE);
			} catch (ServletException e) {
			}
        }
        Pattern LEVEL_PATTERN = Pattern.compile("Level([0-9]+)");
        if (propertiesConfiguration!=null) {
            Iterator<String> keys = propertiesConfiguration.getKeys();
            // First path through, we cannot assume it is sorted
            while (keys.hasNext()) {
                String key = keys.next();
                Matcher match = LEVEL_PATTERN.matcher(key);
                if(match.matches()) {
                    Integer levelNumber = Integer.parseInt(match.group(1));
                    String value = propertiesConfiguration.getString(key);
                    userDefinedExtraUserLevelsMap.put(value, levelNumber);
                    Map<String, String> localizedLevelsMap = new HashMap<String,String>();
                    localeMap.put(levelNumber, localizedLevelsMap);
                }
            }
            keys = propertiesConfiguration.getKeys();
            while (keys.hasNext()) {
                String key = keys.next();
                Matcher match = LEVEL_PATTERN.matcher(key);
                if(match.matches()) {
                    Integer levelNumber = Integer.parseInt(match.group(1));
                    String value = propertiesConfiguration.getString(key);
                    userDefinedExtraUserLevelsMap.put(value, levelNumber);
                }else{
                    String[] keyParts = key.split("\\.");
                    if (keyParts!=null && keyParts.length>1) {
                        String userLevelName = keyParts[0];
                        if (userLevelName!=null && !"".equals(userLevelName)) {
                            String userLevelRightName = key.substring(userLevelName.length()+1);
                            Integer userLevelID = userLevelNameToID(userLevelName);
                            if (userLevelID==null) {
                                LOGGER.info("No user level specified for " + userLevelName + " (key: " + key + ")");
                                continue;
                            }
                            SortedMap<String, Boolean> userLevelMap = locUserLevelsMap.get(userLevelID);
                            if (userLevelMap==null) {
                                userLevelMap = new TreeMap<String, Boolean>();
                                locUserLevelsMap.put(userLevelID, userLevelMap);
                            }
                            String value = propertiesConfiguration.getString(key);

                            if (keyParts[1] != null && "locale".equals(keyParts[1])) {
                                if (keyParts[2] != null && userLevelID != null) {
                                    if (localeMap.get(userLevelID) != null) {
                                        localeMap.get(userLevelID).put(keyParts[2], value);
                                        continue;
                                    }
                                }
                            }
                            if (value!=null && !"".equals(value) && userLevelRightName != null && !"".equals(userLevelRightName)) {
                                if (ON.equals(value)) {
                                    userLevelMap.put(userLevelRightName, Boolean.TRUE);
                                } else {
                                    if (OFF.equals(value)) {
                                        userLevelMap.put(userLevelRightName, Boolean.FALSE);
                                    } else {
                                        try {
                                            userLevelMap.put(userLevelRightName, Boolean.valueOf(value));
                                        } catch (Exception e) {
                                            LOGGER.info("The value " + value + " for key " + key + " can't be converted to a boolean " + e.getMessage());
                                            LOGGER.debug(ExceptionUtils.getStackTrace(e));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.userLevelsMap = locUserLevelsMap;
        return locUserLevelsMap;
    }

    private Integer userLevelNameToID(String name) {
        return userDefinedExtraUserLevelsMap.get(name);
    }


}
