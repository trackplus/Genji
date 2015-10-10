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


package com.aurel.track.admin.user.filtersInMenu;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.IssueFilterFacade;
import com.aurel.track.admin.customize.category.filter.MenuitemFilterBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TMenuitemQueryBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

public class FiltersInMenuBL {


	static List<FilterForPersonTO> loadFilterAssignmnetData(String selectedPersonIDs, Locale locale) {
        List<Integer> personIDs =  getIntegerListFromCommaSeparatedIDs(selectedPersonIDs);
		List<TMenuitemQueryBean> menuItemQueries = MenuitemFilterBL.loadByPersons(personIDs);
		List<FilterForPersonTO>  filterAssignments = new LinkedList<FilterForPersonTO>();
		List<TPersonBean> personBeans = PersonBL.loadByKeys(personIDs);
		Map<Integer, TPersonBean> personMap = GeneralUtils.createMapFromList(personBeans);
		Set<Integer> filterIDs = new HashSet<Integer>();
		for (TMenuitemQueryBean menuitemQueryBean : menuItemQueries) {
			filterIDs.add(menuitemQueryBean.getQueryKey());
		}
        Set<Integer> usedFilterIDs =  new HashSet<Integer>();
		Map<Integer, TQueryRepositoryBean> filterMap = GeneralUtils.createMapFromList(LocalizeUtil.localizeDropDownList(
				FilterBL.loadByPrimaryKeys(GeneralUtils.createIntegerListFromCollection(filterIDs)), locale));
		for (TMenuitemQueryBean menuitemQueryBean : menuItemQueries) {
			Integer filterID = menuitemQueryBean.getQueryKey();
			Integer personID = menuitemQueryBean.getPerson();
			String personName = personMap.get(personID).getLabel();
			String filterLabel = filterMap.get(filterID).getLabel();
			FilterForPersonTO filterForPersonTO = new FilterForPersonTO();
			filterForPersonTO.setFilterID(filterID);
			filterForPersonTO.setFilterLabel(filterLabel);
			filterForPersonTO.setPersonID(personID);
			filterForPersonTO.setPersonName(personName);
			filterAssignments.add(filterForPersonTO);
            if (!usedFilterIDs.contains(filterID)) {
                usedFilterIDs.add(filterID);
                FilterForPersonTO filterOnlyTO = new FilterForPersonTO();
                filterOnlyTO.setFilterID(filterID);
                filterOnlyTO.setFilterLabel(filterLabel);
                filterAssignments.add(filterOnlyTO);
            }
		}
        Collections.sort(filterAssignments);
        String previousFilterLabel = null;
        for (FilterForPersonTO filterForPersonTO : filterAssignments) {
            String filterLabel = filterForPersonTO.getFilterLabel();
            if (!filterLabel.equals(previousFilterLabel)) {
                previousFilterLabel = filterLabel;
                filterForPersonTO.setFirst(true);
            }
        }
		return filterAssignments;
		
	}

    /**
     * Gets the selected personIDs form the comma separated string
     * @param commaSeparatedIDs
     * @return
     */
    private static List<Integer> getIntegerListFromCommaSeparatedIDs(String commaSeparatedIDs) {
        if (commaSeparatedIDs!=null) {
            return GeneralUtils.createIntegerListFromStringArr(commaSeparatedIDs.split(","));
        }
        return new LinkedList<Integer>();
    }
    
	/**
	 * Gets the menuItemQueryBeans map by filter and person
	 * @param personIDList
	 * @param filterIDList
	 * @return
	 */
	private static Map<Integer, Map<Integer, TMenuitemQueryBean>> getFilterToPersonToMenuMap(List<Integer> personIDList, List<Integer> filterIDList) {
		List<TMenuitemQueryBean> existingMenuitemQueryBeans = MenuitemFilterBL.loadByPersonsAndQueries(personIDList, filterIDList);
		Map<Integer, Map<Integer, TMenuitemQueryBean>> filterToPersonToMenuMap = new HashMap<Integer, Map<Integer,TMenuitemQueryBean>>();
		for (TMenuitemQueryBean menuitemQueryBean : existingMenuitemQueryBeans) {
			Integer filterID = menuitemQueryBean.getQueryKey();
			Integer personID = menuitemQueryBean.getPerson();
			Map<Integer, TMenuitemQueryBean> filterMap = filterToPersonToMenuMap.get(filterID);
			if (filterMap==null) {
				filterMap = new HashMap<Integer, TMenuitemQueryBean>();
				filterToPersonToMenuMap.put(filterID, filterMap);
			}
			filterMap.put(personID, menuitemQueryBean);
		}
		return filterToPersonToMenuMap;
	}
	
	/**
	 * Assign filters for persons in menu
     * @param selectedPersonIDs
	 * @param selectedFilterIDs
	 */
	static boolean addFiltersInMenu(String selectedPersonIDs, String selectedFilterIDs, TPersonBean personBean) {
		List<Integer> personIDList = getIntegerListFromCommaSeparatedIDs(selectedPersonIDs);
		List<Integer> filterIDList = getIntegerListFromCommaSeparatedIDs(selectedFilterIDs);
		Map<Integer, Map<Integer, TMenuitemQueryBean>> filterToPersonToMenuMap = getFilterToPersonToMenuMap(personIDList, filterIDList);
		for (Integer filterID : filterIDList) {
			Map<Integer, TMenuitemQueryBean> filterMap = filterToPersonToMenuMap.get(filterID);
			if (filterMap==null) {
				filterMap = new HashMap<Integer, TMenuitemQueryBean>();
			}
			for (Integer personID : personIDList) {
				TMenuitemQueryBean menuitemQueryBean = filterMap.get(personID);
				if (menuitemQueryBean==null) {
					menuitemQueryBean = new TMenuitemQueryBean();
					menuitemQueryBean.setQueryKey(filterID);
					menuitemQueryBean.setPerson(personID);
				}
				menuitemQueryBean.setIncludeInMenu(true);
				MenuitemFilterBL.save(menuitemQueryBean);
			}
		}
        return personIDList.contains(personBean.getObjectID());
	}
	
	/**
	 * Delete filter menu entries for users
	 * No direct delete is possible because if the the menuItem entry has a cssField it can't be deleted only the includeInMenu flag should be set
	 * @param unassign
     * @param selectedPersonIDs
     * @param personBean
	 */
	static boolean unassign(String unassign, String selectedPersonIDs, TPersonBean personBean) {
		Set<Integer> filterIDSet = new HashSet<Integer>();
        List<Integer> personIDList = null;
        if (selectedPersonIDs==null) {
            //the person removed the filter from issueNavigator
            personIDList = new LinkedList<Integer>();
            personIDList.add(personBean.getObjectID());
        }  else {
            //remove through user management (at least one user is selected)
            personIDList = getIntegerListFromCommaSeparatedIDs(selectedPersonIDs);
        }
		Map<Integer, Set<Integer>> filterToPersonsRemoveMap = new HashMap<Integer, Set<Integer>>();
		String[] unassignIDs = unassign.split(",");
		for (String unassignStr : unassignIDs) {
			Set<Integer> personsForFilter = null;
			String[] unassignParts = unassignStr.split("\\|");
			Integer filterID = null;
			try {
				filterID = Integer.valueOf(unassignParts[0]);
				if (filterID!=null) {
					personsForFilter = filterToPersonsRemoveMap.get(filterID);
					if (personsForFilter==null) {
						personsForFilter = new HashSet<Integer>();
						filterToPersonsRemoveMap.put(filterID, personsForFilter);
					}
					filterIDSet.add(filterID);
				}
			} catch (Exception e) {
			}
            if (unassignParts.length>1) {
                Integer personID = null;
                try {
                    personID = Integer.valueOf(unassignParts[1]);
                    if (personID!=null) {
                        personsForFilter.add(personID);
                    }
                } catch (Exception e) {
                }
            } else {
                //remove the filterID (not explicit persons) -> remove filterID from all selected persons
                personsForFilter.addAll(personIDList);
            }
		}
		List<Integer> filterIDList = GeneralUtils.createIntegerListFromCollection(filterIDSet);
		Map<Integer, Map<Integer, TMenuitemQueryBean>> existingFilterToPersonToMenuMap = getFilterToPersonToMenuMap(personIDList, filterIDList);
		for (Integer filterID : filterToPersonsRemoveMap.keySet()) {
			Set<Integer> personIDs = filterToPersonsRemoveMap.get(filterID);
			Map<Integer, TMenuitemQueryBean> existingFilterMap = existingFilterToPersonToMenuMap.get(filterID);
			if (personIDs!=null) {
				for (Integer personID : personIDs) {
					TMenuitemQueryBean menuitemQueryBean = existingFilterMap.get(personID);
					if (menuitemQueryBean!=null) {
						Integer cssField = menuitemQueryBean.getCSSStyleField();
						if (cssField==null || IssueFilterFacade.NONE_STYLE_FIELD.equals(cssField)) {
							MenuitemFilterBL.delete(personID, filterID);
						} else {
							menuitemQueryBean.setIncludeInMenu(false);
							MenuitemFilterBL.save(menuitemQueryBean);
						}
					}
				}
			}
		}
        return personIDList.contains(personBean.getObjectID());
	}
}
