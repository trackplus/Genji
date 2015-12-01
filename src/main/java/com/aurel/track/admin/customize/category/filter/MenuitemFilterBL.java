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

package com.aurel.track.admin.customize.category.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL.PREDEFINED_QUERY;
import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL.PREDEFINED_QUERY_NAME;
import com.aurel.track.beans.TMenuitemQueryBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MenuitemQueryDAO;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;

public class MenuitemFilterBL {

	private static final Logger LOGGER = LogManager.getLogger(PredefinedQueryBL.class);
	
	private static MenuitemQueryDAO menuitemQueryDAO = DAOFactory.getFactory().getMenuitemQueryDAO();
	
	/**
	 * Loads the menuitemQueryBeans by person and queryIDs
	 * @param personID
	 * @param filterIDs
	 * @return
	 */
	public static List<TMenuitemQueryBean> loadByPersonAndQueries(Integer personID, List<Integer> filterIDs) {
		return menuitemQueryDAO.loadByPersonAndQueries(personID, filterIDs);
	}
	
	/**
	 * Loads the menuitemQueryBeans by persons
	 * @param personIDs
	 * @return
	 */
	public static List<TMenuitemQueryBean> loadByPersons(List<Integer> personIDs) {
		return menuitemQueryDAO.loadByPersons(personIDs);
	}
	
	/**
	 * Loads the menuitemQueryBeans by persons and queryIDs
	 * @param personIDs
	 * @param queryIDs
	 * @return
	 */
	public static List<TMenuitemQueryBean> loadByPersonsAndQueries(List<Integer> personIDs, List<Integer> queryIDs) {
		return menuitemQueryDAO.loadByPersonsAndQueries(personIDs, queryIDs);
	}
	
	/**
	 * Loads the menuitemQueryBeans by person and query
	 * @param personID
	 * @param filterID
	 * @return
	 */
	public static TMenuitemQueryBean loadByPersonAndQuery(Integer personID, Integer filterID) {
		return menuitemQueryDAO.loadByPersonAndQuery(personID, filterID);
	}
	
	/**
	 * Saves a menuitemQueryBean in the database
	 * @param menuitemQueryBean 
	 * @return
	 */
	public static Integer save(TMenuitemQueryBean menuitemQueryBean) {
		return menuitemQueryDAO.save(menuitemQueryBean);
	}
	
	/**
	 * Deletes a menuitemQueryBean from the database
	 * @param personID
	 * @param queryID 
	 * @return
	 */
	public static void delete(Integer personID, Integer queryID) {
		menuitemQueryDAO.delete(personID, queryID);
	}
	
	/**
	 * Actualizes the menu item query
	 * @param personID
	 * @param filterID
	 * @param includeInMenu
	 * @param styleField
	 */
	static boolean actualizeMenuItemQuery(Integer personID, Integer filterID,
				boolean includeInMenu, Integer styleField) {
		boolean changed = false;
		TMenuitemQueryBean menuitemQueryBean = loadByPersonAndQuery(personID, filterID);
		if (!includeInMenu && (styleField==null || styleField.equals(IssueFilterFacade.NONE_STYLE_FIELD))) {
			if (menuitemQueryBean==null) {
				//nothing changed
				return false;
			} else {
				//delete from menu
				delete(personID, filterID);
				return true;
			}
		} else {
			if (menuitemQueryBean==null) {
				//add as new
				menuitemQueryBean = new TMenuitemQueryBean();
				menuitemQueryBean.setPerson(personID);
				menuitemQueryBean.setQueryKey(filterID);
				changed = true;
			} else {
				changed = EqualUtils.notEqual(menuitemQueryBean.isIncludeInMenu(), includeInMenu);
			}
			menuitemQueryBean.setCSSStyleField(styleField);
			menuitemQueryBean.setIncludeInMenu(includeInMenu);
			save(menuitemQueryBean);
			return changed;
		}
	}
	
	/**
	 * Actualize the menu item query from change subscribe
	 * @param personID
	 * @param filterID
	 * @param includeInMenu
	 * @return
	 */
	static boolean actualizeMenuItemQuery(Integer personID, Integer filterID, boolean includeInMenu) {
		boolean changed = false;
		TMenuitemQueryBean menuitemQueryBean = loadByPersonAndQuery(personID, filterID);
		if (!includeInMenu) {
			if (menuitemQueryBean==null) {
				//nothing changed
				return false;
			} else {
				changed = EqualUtils.notEqual(menuitemQueryBean.isIncludeInMenu(), includeInMenu);
				Integer styleField = menuitemQueryBean.getCSSStyleField();
				if (styleField==null || styleField.equals(IssueFilterFacade.NONE_STYLE_FIELD)) {
					//no style field exists, delete menu entry from db
					delete(personID, filterID);
				} else {
					menuitemQueryBean.setIncludeInMenu(includeInMenu);
					save(menuitemQueryBean);
				}
			}
		} else {
			if (menuitemQueryBean==null) {
				//add as new
				menuitemQueryBean = new TMenuitemQueryBean();
				menuitemQueryBean.setPerson(personID);
				menuitemQueryBean.setQueryKey(filterID);
				changed = true;
			} else {
				changed = EqualUtils.notEqual(menuitemQueryBean.isIncludeInMenu(), includeInMenu);
			}
			menuitemQueryBean.setIncludeInMenu(includeInMenu);
			save(menuitemQueryBean);
		}
		return changed;
	}
	
	/**
	 * Subscribe a person to filters
	 * @param personID
	 * @param filterIDs
	 */
	public static void subscribePersonsToFilters(Integer personID, List<Integer> filterIDs) {
		List<TMenuitemQueryBean> menuItemQueries = loadByPersonAndQueries(personID, filterIDs);
		Map<Integer, TMenuitemQueryBean> menuitemMap = new HashMap<Integer, TMenuitemQueryBean>();
		for (TMenuitemQueryBean menuitemQueryBean : menuItemQueries) {
			//filter is added already
			menuitemMap.put(menuitemQueryBean.getQueryKey(), menuitemQueryBean);
		}
		for (Integer filterID : filterIDs) {
			TMenuitemQueryBean menuitemQueryBean = menuitemMap.get(filterID);
			if (menuitemQueryBean==null) {
				menuitemQueryBean = new TMenuitemQueryBean();
				menuitemQueryBean.setPerson(personID);
				menuitemQueryBean.setQueryKey(filterID);
			} else {
				if (menuitemQueryBean.isIncludeInMenu()) {
					continue;
				}
			}
			menuitemQueryBean.setIncludeInMenu(true);
			save(menuitemQueryBean);
		}
	}
	
	/**
	 * Gets the filter IDs to subscribe by default a user
	 * @return
	 */
	public static List<Integer> getFilterIDsToSubscribe() {
		List<Integer> filterIDs = new LinkedList<Integer>();
		List<String> filterNames = loadFilterNamesToSubscribe();
		if (filterNames.contains(PREDEFINED_QUERY_NAME.ALL_ITEMS)) {
			filterIDs.add(PREDEFINED_QUERY.ALL_ITEMS);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.OUTSTANDING)) {
			filterIDs.add(PREDEFINED_QUERY.OUTSTANDING);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.MY_ITEMS)) {
			filterIDs.add(PREDEFINED_QUERY.MY_ITEMS);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.MANAGER_ITEMS)) {
			filterIDs.add(PREDEFINED_QUERY.MANAGERS_ITEMS);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.RESPONSIBLE_ITEMS)) {
			filterIDs.add(PREDEFINED_QUERY.RESPONSIBLES_ITEMS);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.AUTHOR_ITEMS)) {
			filterIDs.add(PREDEFINED_QUERY.AUTHOR_ITEMS);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.WATCHER_ITEMS)) {
			filterIDs.add(PREDEFINED_QUERY.WATCHER_ITEMS);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.MEETINGS)) {
			filterIDs.add(PREDEFINED_QUERY.MEETINGS);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.UNSCHEDULED)) {
			filterIDs.add(PREDEFINED_QUERY.UNSCHEDULED);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.CLOSED_RECENTLY)) {
			filterIDs.add(PREDEFINED_QUERY.CLOSED_RECENTLY);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.ADDED_RECENTLY)) {
			filterIDs.add(PREDEFINED_QUERY.ADDED_RECENTLY);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.UPDATED_RECENTLY)) {
			filterIDs.add(PREDEFINED_QUERY.UPDATED_RECENTLY);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.SCRUM_BOARD)) {
			filterIDs.add(PREDEFINED_QUERY.SCRUM_BOARD);
		}
		if (filterNames.contains(PREDEFINED_QUERY_NAME.KANBAN_BOARD)) {
			filterIDs.add(PREDEFINED_QUERY.KANBAN_BOARD);
		}
		//get them from the database to be sure that all of them still exist (was not deleted)
		List<TQueryRepositoryBean> filterBeans = FilterBL.loadByPrimaryKeys(filterIDs);
		return GeneralUtils.createIntegerListFromBeanList(filterBeans);
	}
	
	/**
	 * Loads the filter names from property file
	 * @return
	 */
	private static List<String> loadFilterNamesToSubscribe() {
		List<String> filterNames = new LinkedList<String>();
		String ON = "on";
		String OFF = "off";
		PropertiesConfiguration propertiesConfiguration = null;
		try {
			propertiesConfiguration = 
					HandleHome.getProperties(HandleHome.FILTER_SUBSCRIPTIONS_FILE, ApplicationBean.getInstance().getServletContext());
		} catch (ServletException e) {
			LOGGER.error("ServletException by getting the FilterSubscriptions.properties from war " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (propertiesConfiguration!=null) {
			Iterator<String> keys = propertiesConfiguration.getKeys();
			while (keys.hasNext()) {
				String key = keys.next();
				String[] keyParts = key.split("\\.");
				if (keyParts!=null && keyParts.length>1) {
					String repository = keyParts[0];
					if (repository!=null && !"".equals(repository)) { 
						String filterName = key.substring(repository.length()+1);
						String value = propertiesConfiguration.getString(key);
						if (value!=null && !"".equals(value)) {
							if (ON.equals(value)) {
								filterNames.add(filterName);
							} else {
								if (!OFF.equals(value)) {
									try {
										Boolean boolValue = Boolean.valueOf(value);
										if (boolValue!=null && boolValue.booleanValue()) {
											filterNames.add(filterName);
										}
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
		return filterNames;
	}
}
