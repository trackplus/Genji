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

package com.aurel.track.admin.customize.category.filter.execute.loadItems;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.util.GeneralUtils;

/**
 * Counts the tree filter items
 * @author Tamas
 *
 */
public class LoadTreeFilterItemCounts {
	private static final Logger LOGGER = LogManager.getLogger(LoadTreeFilterItemCounts.class);
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
	/**
	 * Counts the tree filter items
	 * @param filterUpperTO
	 * @param qNode
	 * @param raciBean
	 * @param personBean
	 * @param locale
	 * @param maxCount
	 * @return
	 */
	public static int countTreeFilterProjectRoleItems(FilterUpperTO filterUpperTO,
			TPersonBean personBean, Locale locale, int maxCount) {
		int count = 0;
		Integer personID = personBean.getObjectID();
		if (filterUpperTO.getMatcherContext()==null) {
			MatcherContext matcherContext = new MatcherContext();
			matcherContext.setLoggedInUser(personID);
			matcherContext.setLastLoggedDate(personBean.getLastButOneLogin());
			matcherContext.setLocale(locale);
			filterUpperTO.setMatcherContext(matcherContext);
			matcherContext.setIncludeResponsiblesThroughGroup(filterUpperTO.isIncludeResponsiblesThroughGroup());
			matcherContext.setReleaseTypeSelector(filterUpperTO.getReleaseTypeSelector());
			filterUpperTO.cleanParameters();
		}
		Integer[] selectedProjectsOriginal = filterUpperTO.getSelectedProjects();
		if (selectedProjectsOriginal==null || selectedProjectsOriginal.length==0) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("No Project(s) selected in filter");
			}
			return count;
		}
		/**
		 * Separate the projects with roles from projects with RACI items
		 */
		List<Integer> meAndSubstitutedAndGroups = AccessBeans.getMeAndSubstitutedAndGroups(personID);
		//all selected projects and their descendants 
		Integer[] selectedAndDescendantProjects = filterUpperTO.getSelectedProjects();
		if (LOGGER.isDebugEnabled() && selectedAndDescendantProjects!=null) {
			LOGGER.debug("Project(s) selected in filter: " + LookupContainer.getNotLocalizedLabelBeanListLabels(SystemFields.INTEGER_PROJECT,
					GeneralUtils.createSetFromIntegerArr(selectedAndDescendantProjects)));
		}
		//get also the ancestor projects for getting the role based rights
		Integer[] ancestorProjects = ProjectBL.getAncestorProjects(selectedAndDescendantProjects);
		int[] readAnyRights = new int[] { AccessFlagIndexes.READANYTASK, AccessFlagIndexes.PROJECTADMIN };
		Map<Integer, Set<Integer>> projectToIssueTypesWithReadRight = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(meAndSubstitutedAndGroups, ancestorProjects, readAnyRights);
		
		//gets the selected item types for getting the role based right eventually restricted by item types
		Integer[] selectedItemTypesOriginal = filterUpperTO.getSelectedIssueTypes();
		if (selectedItemTypesOriginal==null || selectedItemTypesOriginal.length==0) {
			List<TListTypeBean> selectableItemTypes = IssueTypeBL.loadAllSelectable();
			selectedItemTypesOriginal = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(selectableItemTypes));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("No explicit item types selected. Number of non document item types included implicitly " + selectableItemTypes.size());
			}
		}
		Set<Integer> selectedItemTypesSet = new HashSet<Integer>();
		if (selectedItemTypesOriginal!=null) {
			selectedItemTypesSet = GeneralUtils.createSetFromIntegerArr(selectedItemTypesOriginal);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Item type(s) selected in filter: " + LookupContainer.getLocalizedLabelBeanListLabels(SystemFields.INTEGER_ISSUETYPE, selectedItemTypesSet, locale));
			}
		}
		List<TProjectBean> selectedAndDescendantProjectBeans = ProjectBL.loadByProjectIDs(GeneralUtils.createListFromIntArr(selectedAndDescendantProjects));
		Map<Integer, Integer> childToParentProjectMap = ProjectBL.getChildToParentMap(selectedAndDescendantProjectBeans, null);
		Set<Integer> raciProjectIDs = new HashSet<Integer>();
		Map<Set<Integer>, Set<Integer>> itemTypesToProjectsMap = LoadTreeFilterItems.getItemTypesToProjectsMap(selectedAndDescendantProjects,
				selectedItemTypesSet, projectToIssueTypesWithReadRight, childToParentProjectMap, raciProjectIDs);
		/**
		 * Get the issues for projects with role grouped by same item types
		 */
		for (Map.Entry<Set<Integer>, Set<Integer>> entry : itemTypesToProjectsMap.entrySet()) {
			Set<Integer> itemTypes = entry.getKey();
			Set<Integer> projectIDs = entry.getValue();
			boolean itemTypeRestrictions = !itemTypes.contains(null); 
			filterUpperTO.setSelectedProjects(GeneralUtils.createIntegerArrFromCollection(projectIDs));
			Integer[] itemTypesArr = null;
			if (itemTypeRestrictions) {
				itemTypesArr = GeneralUtils.createIntegerArrFromCollection(itemTypes);
			} else {
				itemTypesArr = selectedItemTypesOriginal;
			}
			filterUpperTO.setSelectedIssueTypes(itemTypesArr);
			
			Integer projectRoleCount = getItemCount(personID, filterUpperTO, null);
			if (projectRoleCount!=null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Items found in projects " + 
							LookupContainer.getNotLocalizedLabelBeanListLabels(SystemFields.INTEGER_PROJECT, projectIDs) +
							" and item types " + LookupContainer.getLocalizedLabelBeanListLabels(SystemFields.INTEGER_ISSUETYPE,
									GeneralUtils.createSetFromIntegerArr(itemTypesArr), locale) + ": " + projectRoleCount);
				}
				count+=projectRoleCount;
				if (count>maxCount) {
					LOGGER.debug("Too many items found by project roles: " + count);
					return count;
				}
			}
		}
		filterUpperTO.setSelectedProjects(selectedProjectsOriginal);
		filterUpperTO.setSelectedIssueTypes(selectedItemTypesOriginal);
		return count;
	}
	
	/**
	 * Counts the tree filter items
	 * @param filterUpperTO
	 * @param personBean
	 * @param locale
	 * @param maxCount
	 * @return
	 */
	public static int countTreeFilterRACIRoleItems(FilterUpperTO filterUpperTO,
			TPersonBean personBean, Locale locale, int maxCount) {
		int count = 0;
		Integer personID = personBean.getObjectID();
		Integer[] selectedProjects = filterUpperTO.getSelectedProjects();
		if (selectedProjects==null || selectedProjects.length==0) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("No Project(s) selected in filter");
			}
			return count;
		}
		/**
		 * Separate the projects with roles from projects with RACI items
		 */
		List<Integer> meAndSubstitutedAndGroups = AccessBeans.getMeAndSubstitutedAndGroups(personID);
		//all selected projects and their descendants 
		Integer[] selectedAndDescendantProjects = filterUpperTO.getSelectedProjects();
		
		Set<Integer> raciProjectIDs = GeneralUtils.createSetFromIntegerArr(selectedAndDescendantProjects);
		if (!raciProjectIDs.isEmpty()) {
			List<Integer> meAndSubstitutedIDs = AccessBeans.getMeAndSubstituted(personBean);
			Map<Integer, List<Integer>> reciprocGroups = AccessBeans.getRaciprocGroupsMap(meAndSubstitutedIDs);
			List<Integer> reciprocOriginatorGroups = reciprocGroups.get(SystemFields.INTEGER_ORIGINATOR);
			List<Integer> reciprocManagerGroups = reciprocGroups.get(SystemFields.INTEGER_MANAGER);
			List<Integer> reciprocResponsibleGroups = reciprocGroups.get(SystemFields.INTEGER_RESPONSIBLE);
			List<Integer> allGroupsForPerson = reciprocGroups.get(AccessBeans.ALL_PERSONGROUPS);
			RACIBean raciBean = new RACIBean();
			/**
			 * prepare my author items
			 */
			Set<Integer> authorSet = new HashSet<Integer>();
			authorSet.addAll(meAndSubstitutedIDs);
			if (reciprocOriginatorGroups!=null) {
				List<TPersonBean> originatorsInReciprocGroups = PersonBL.getIndirectPersons(reciprocOriginatorGroups, false, null);
				List<Integer> reciprocOriginators = GeneralUtils.createIntegerListFromBeanList(originatorsInReciprocGroups);
				authorSet.addAll(reciprocOriginators);
			}
			raciBean.setAuthors(authorSet);
			/**
			 * prepare my responsible items
			 */
			Set<Integer> responsibleSet = new HashSet<Integer>();
			responsibleSet.addAll(meAndSubstitutedIDs);
			if (reciprocResponsibleGroups!=null) {
				List<TPersonBean> responsiblesInReciprocGroups = PersonBL.getIndirectPersons(reciprocResponsibleGroups, false, null);
				List<Integer> reciprocResponsibles = GeneralUtils.createIntegerListFromBeanList(responsiblesInReciprocGroups);
				responsibleSet.addAll(reciprocResponsibles);
			}
			if (allGroupsForPerson!=null) {
				responsibleSet.addAll(allGroupsForPerson);
			}
			raciBean.setResponsibles(responsibleSet);
			/**
			 * prepare my manager items
			 */
			Set<Integer> managerSet = new HashSet<Integer>();
			managerSet.addAll(meAndSubstitutedIDs);
			if (reciprocManagerGroups!=null) {
				List<TPersonBean> managersInReciprocGroups = PersonBL.getIndirectPersons(reciprocManagerGroups, false, null);
				List<Integer> reciprocManagers = GeneralUtils.createIntegerListFromBeanList(managersInReciprocGroups);
				managerSet.addAll(reciprocManagers);
			}
			raciBean.setManagers(managerSet);
			/**
			 * get the author, responsible and manager items
			 */
			LOGGER.debug("Getting my RACI items...");
			Integer respManAuthItemCount = getItemCount(personID, filterUpperTO, raciBean);
			if (respManAuthItemCount!=null) {
				LOGGER.debug("Responsible/manager/originator count " + respManAuthItemCount);
				count+=respManAuthItemCount;
				if (count>maxCount) {
					LOGGER.debug("Too many items found by RACI roles: " + count);
					return count;
				}
			}
			/**
			 * my watcher items
			 */
			//save temporarily to set it back after filter is executed for watchers
			Integer[] selectedWatchersOriginal = filterUpperTO.getSelectedConsultantsInformants();
			Integer watcherSelectorOriginal = filterUpperTO.getWatcherSelector();
			Set<Integer> watcherSet = new HashSet<Integer>();
			watcherSet.addAll(meAndSubstitutedAndGroups);
			filterUpperTO.setWatcherSelector(FilterUpperTO.CONSINF_SELECTOR.CONSULTANT_OR_INFORMANT);
			if (!watcherSet.isEmpty()) {
				filterUpperTO.setSelectedConsultantsInformants(GeneralUtils.createIntegerArrFromCollection(watcherSet));
				Integer watcherItemsCount = getItemCount(personID, filterUpperTO, null);
				if (watcherItemsCount!=null) {
					LOGGER.debug("Watcher items count" + watcherItemsCount);
					count+=watcherItemsCount;
					if (count>maxCount) {
						LOGGER.debug("Too many items found after watcher count " + count);
						return count;
					}
				}
				//set both watchers and selector back to original value for the next filter execution
				filterUpperTO.setSelectedConsultantsInformants(selectedWatchersOriginal);
				filterUpperTO.setWatcherSelector(watcherSelectorOriginal);
			}
			/**
			 * on behalf of items
			 */
			List<Integer> onBefalfUserPickerFiels = FieldBL.getOnBehalfOfUserPickerFieldIDs();
			if (onBefalfUserPickerFiels!=null && !onBefalfUserPickerFiels.isEmpty()) {
				Map<Integer, Integer[]> selectedCustomSelectsOriginal = filterUpperTO.getSelectedCustomSelects();
				if (selectedCustomSelectsOriginal==null) {
					selectedCustomSelectsOriginal = new HashMap<Integer, Integer[]>();
					filterUpperTO.setSelectedCustomSelects(selectedCustomSelectsOriginal);
				}
				Map<Integer, Integer> customSelectSimpleFieldsMap = filterUpperTO.getCustomSelectSimpleFields();
				if (customSelectSimpleFieldsMap==null) {
					customSelectSimpleFieldsMap = new HashMap<Integer, Integer>();
					filterUpperTO.setCustomSelectSimpleFields(customSelectSimpleFieldsMap);
				}
				for (Integer fieldID : onBefalfUserPickerFiels) {
					//get the filter result for each on behalf of user picker one by one
					Set<Integer> onBehalfSet = new HashSet<Integer>();
					onBehalfSet.addAll(meAndSubstitutedIDs);
					//save temporarily to set it back after filter is executed for on behalf of persons
					Integer[] selectedOnBehalfUserPickerOriginal = selectedCustomSelectsOriginal.get(fieldID);
					if (selectedOnBehalfUserPickerOriginal!=null && selectedOnBehalfUserPickerOriginal.length>0) {
						//if on behalf of field has selection than retain only the selections related to me as author
						onBehalfSet.retainAll(GeneralUtils.createSetFromIntegerArr(selectedOnBehalfUserPickerOriginal));
					} 
					if (!onBehalfSet.isEmpty()) {
						selectedCustomSelectsOriginal.put(fieldID, GeneralUtils.createIntegerArrFromCollection(onBehalfSet));
						IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						customSelectSimpleFieldsMap.put(fieldID, fieldTypeRT.getSystemOptionType());
						Integer onBehalfItemsCount = getItemCount(personID, filterUpperTO, null);
						if (onBehalfItemsCount!=null) {
							LOGGER.debug("On behalf items count" + onBehalfItemsCount);
							count+=onBehalfItemsCount;
							if (count>maxCount) {
								LOGGER.debug("Too many on behalf items found " + count);
								return count;
							}
						}
						//set it back to original value for the next filter execution
						selectedCustomSelectsOriginal.put(fieldID, selectedOnBehalfUserPickerOriginal);
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * Gets the items from the database
	 * @param personID
	 * @param filterUpperTO
	 * @param raciBean
	 * @param withCustomAttributes
	 * @param attributeValueBeanList
	 * @param startDate
	 * @param endDate
	 * @param qNode
	 */
	public static Integer getItemCount(Integer personID, FilterUpperTO filterUpperTO, RACIBean raciBean) {
		Date start = null;
		if (LOGGER.isDebugEnabled()) {
			start = new Date();
		}
		Integer itemCount = workItemDAO.countTreeFilterItems(filterUpperTO, raciBean, personID);
		if (LOGGER.isDebugEnabled() && start!=null) {
			Date end = new Date();
			LOGGER.debug("Counting " + itemCount + " items lasted " + new Long(end.getTime()-start.getTime()).toString() + " ms");
		}
		return itemCount;
	}
}
