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

package com.aurel.track.item.massOperation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.bulkSetters.BulkRelations;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.util.GeneralUtils;

/**
 * Getting the consulted and informed persons by rights
 * @author Tamas
 *
 */
public class MassOperationWatchersBL {

	/**
	 * Gets the possible watchers
	 * @param fieldID
	 * @param relationID
	 * @param projectIssueTypeContexts
	 * @param personBean
	 * @param involvedProjectsIDs
	 * @param selectedWorkItemIDsList
	 * @return
	 */
	static List<TPersonBean> getPossibleWatchers(Integer fieldID, int relationID,
			Map<Integer, Set<Integer>> projectIssueTypeContexts, TPersonBean personBean,
			Integer[] involvedProjectsIDs, List<Integer> selectedWorkItemIDsList) {
		List<TPersonBean> possibleValues = null;
		if (relationID==BulkRelations.ADD_ITEMS || relationID==BulkRelations.SET_TO) {
			possibleValues = MassOperationWatchersBL.addWatcherOptions(fieldID, involvedProjectsIDs, 
				projectIssueTypeContexts, personBean);
		} else {
			if (relationID==BulkRelations.REMOVE_ITEMS) {
				possibleValues = MassOperationWatchersBL.removeWatcherOptions(fieldID,
						selectedWorkItemIDsList, projectIssueTypeContexts, personBean);
			}
		}
		return possibleValues;
	}
	
	/**
	 * Get the consulted/informed persons to add
	 * @param watcherField
	 * @param involvedProjects
	 * @param projectIssueTypeContexts
	 * @param personBean
	 * @return
	 */
	private static List<TPersonBean> addWatcherOptions(int watcherField, Integer[] involvedProjects, 
			Map<Integer, Set<Integer>> projectIssueTypeContexts, TPersonBean personBean) {
		boolean modifyWatchersRestricted = AccessBeans.getFieldRestrictedInAnyContext(
				personBean.getObjectID(), projectIssueTypeContexts, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS, true);
		if (modifyWatchersRestricted) {
			//no modify watchers right at : only the current person can be added
			List<TPersonBean> personBeanList = new ArrayList<TPersonBean>();
			if (watcherField==MassOperationBL.CONSULTANT_FIELDID) {
				boolean editInAllProjects = MassOperationRightsBL.hasRightInAllContexts(projectIssueTypeContexts, 
							personBean.getObjectID(), AccessFlagIndexes.MODIFYANYTASK);
				if (editInAllProjects) {
					//the current person can add himself as consulted only if he has edit right in each context
					personBeanList.add(personBean);
				}
			} else {
				//the current person can add himself as informed because he has read right on each selected issue
				personBeanList.add(personBean);
			}
			
			return personBeanList;
		} else {
			if (watcherField==MassOperationBL.CONSULTANT_FIELDID) {
				return MassOperationWatchersBL.getAddConsultantList(involvedProjects);
			} else {
				return MassOperationWatchersBL.getAddInformantList(involvedProjects);
			}
		}
	}

	/**
	 * Get the consulted/informed persons to remove
	 * @param watcherField
	 * @param selectedIssueMap
	 * @param projectIssueTypeContexts
	 * @param personBean
	 * @return
	 */
	private static List<TPersonBean> removeWatcherOptions(int watcherField, List<Integer> selectedWorkItemIDs, 
			Map<Integer, Set<Integer>> projectIssueTypeContexts, TPersonBean personBean) {
		/*boolean modifyWatchersInAllProjects = MassOperationRightsBL.hasRightInAllContexts(projectIssueTypeContexts, 
				personBean.getObjectID(), AccessFlagMigrationIndexes.MODIFYCONSULTANTSINFORMANTANDOTHERWATCHERS);*/
		boolean modifyWatchersRestricted = AccessBeans.getFieldRestrictedInAnyContext(
				personBean.getObjectID(), projectIssueTypeContexts, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS, true);
		if (modifyWatchersRestricted) {
			List<TPersonBean> personBeanList = new ArrayList<TPersonBean>();
			personBeanList.add(personBean);
			return personBeanList;
		} else {
			if (watcherField==MassOperationBL.CONSULTANT_FIELDID) {
				return PersonBL.loadUsedConsultantsInformantsByWorkItemIDs(selectedWorkItemIDs, RaciRole.CONSULTANT);
			} else {
				return PersonBL.loadUsedConsultantsInformantsByWorkItemIDs(selectedWorkItemIDs, RaciRole.INFORMANT);
			}
		}
	}

	/**
	 * Adds or removes consultants or informants to/from workItem
	 * @param massOperationExpressions
	 * @param workItemID
	 * @param watcherField
	 * @param raciRole
	 */
	static void addRemoveWatcher(
			Integer workItemID, Map<Integer, Integer> setterRelationMap,
			Map<Integer, Object> fieldValueMap, Integer watcherField, String raciRole) {
		Integer watcherOperation = setterRelationMap.get(watcherField);
		if (watcherOperation!=null) {
			if (watcherOperation.equals(BulkRelations.SET_NULL)) {
				ConsInfBL.deleteByWorkItemAndRaciRole(workItemID, raciRole);
			} else {
				String watcherString = (String)fieldValueMap.get(watcherField);
				List<Integer> targetWatcherIDs = null;
				if (watcherString!=null) {
					String[] watcherArrString = watcherString.split(",");
					targetWatcherIDs = GeneralUtils.createIntegerListFromStringArr(watcherArrString);
				}
				if (watcherOperation.equals(BulkRelations.SET_TO) || watcherOperation.equals(BulkRelations.ADD_ITEMS)) {
					List<TPersonBean> actualWatcherPersons = new LinkedList<TPersonBean>();
					List<TPersonBean> raciPersons = PersonBL.getDirectRaciPersons(workItemID, false, raciRole);
					if (raciPersons!=null) {
						actualWatcherPersons.addAll(raciPersons);
					}
					List<TPersonBean> raciGroups = PersonBL.getDirectRaciPersons(workItemID, true, raciRole);
					if (raciGroups!=null) {
						actualWatcherPersons.addAll(raciGroups);
					}
					List<Integer> actualWatcherIDs = GeneralUtils.createIntegerListFromBeanList(actualWatcherPersons);
					if (actualWatcherIDs!=null) {
						for (Integer actualWatcherID : actualWatcherIDs) {
							if (targetWatcherIDs!=null && targetWatcherIDs.contains(actualWatcherID)) {
								targetWatcherIDs.remove(actualWatcherID);
							} else {
								if (watcherOperation.equals(BulkRelations.SET_TO)) {
									ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID, new Integer[] {actualWatcherID}, raciRole);
								}
							}
						}
					}
					if (targetWatcherIDs!=null) {
						for (Integer targetWatcherID : targetWatcherIDs) {
							ConsInfBL.insertByWorkItemAndPersonAndRaciRole(workItemID, targetWatcherID, raciRole);
						}
					}
				} else {
					if (watcherOperation.equals(BulkRelations.REMOVE_ITEMS)) {
						ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID,
								GeneralUtils.createIntegerArrFromCollection(targetWatcherIDs), raciRole);
					} 
				}
			}
		}
	}
	
	/**
	 * Get the list of consultants which can be added to all issues 
	 * (the persons who have consultant role in all the projects the selected issues belongs to)
	 * @param projectIDsArr the array of projects the selected issues belong to
	 * @return
	 */
	static List<TPersonBean> getAddConsultantList(Integer[] projectIDsArr) {
		//get the consultants by role
		int[] consultantRights = new int[] { AccessFlagIndexes.CONSULTANT, AccessFlagIndexes.PROJECTADMIN };
		Set<Integer> consultantsByRoleSet = AccessBeans.getPersonSetWithRightInAllOfTheProjects(projectIDsArr, consultantRights);
		return PersonBL.getDirectAndIndirectPersonsAndGroups(GeneralUtils.createIntegerListFromCollection(consultantsByRoleSet), true, true, null);
	}

	/**
	 * Get the list of informants which can be added to all issues 
	 * (the persons who have informant role in all the projects the selected issues belongs to)
	 * @param projectIDsArr the array of projects the selected issues belong to
	 * @return
	 */
	static List<TPersonBean> getAddInformantList(Integer[] projectIDsArr) {
		//get the informants by role
		int[] informantRights = new int[] { AccessFlagIndexes.INFORMANT, AccessFlagIndexes.PROJECTADMIN };
		Set<Integer> informantByRoleSet = AccessBeans.getPersonSetWithRightInAllOfTheProjects(projectIDsArr, informantRights);
		return PersonBL.getDirectAndIndirectPersonsAndGroups(GeneralUtils.createIntegerListFromCollection(informantByRoleSet), true, true, null);
	}

}
