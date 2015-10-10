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


package com.aurel.track.item.consInf;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.NotifyDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.util.GeneralUtils;

/**
 * Business logic class for "load", "delete" and "me" operations 
 * related to consultants/informants  
 * Deals with  consultants/informants for 
 * new issues (managed in session) and 
 * existing issues (managed in database) 
 * @author Tamas Ruff
 *
 */
public class ConsInfBL {
	
	private static NotifyDAO notifyDAO = DAOFactory.getFactory().getNotifyDAO();	
	
	/**
	 * Whether the person has a specific RACI Role for a workItem (direct or through a group)
	 * @param workItemID
	 * @param personIDs
	 * @param raciRole if null it doesn't matter
	 * @return
	 * @throws Exception
	 */
	public static boolean hasRaciRole(Integer workItemID, List<Integer> personIDs, String raciRole) {
		return notifyDAO.anyPersonHasDirectRaciRole(workItemID, personIDs, raciRole) || 
				notifyDAO.anyPersonHasIndirectRaciRole(workItemID, personIDs, raciRole);	
		//return notifyDAO.hasRaciRole(workItemID, personID, raciRole);
	}
	
	
	/**
	 * Deletes a raci role for a list of persons for a workItem
	 * @param workItemKey
	 * @param selectedPersons
	 * @param raciRole
	 */
	public static void deleteByWorkItemAndPersonsAndRaciRole(Integer workItemKey, Integer[] selectedPersons, String raciRole) {
		notifyDAO.deleteByWorkItemAndPersonsAndRaciRole(workItemKey, selectedPersons, raciRole);
	}
	
	/**
	 * Deletes all persons from a raci role for a workItem
	 * @param workItemKey	
	 * @param raciRole
	 */
	public static void deleteByWorkItemAndRaciRole(Integer workItemKey, String raciRole) {
		notifyDAO.deleteByWorkItemAndRaciRole(workItemKey, raciRole);
	}
	
	/**
	 * Inserts a raci role for a person in a workItem
	 * @param workItemKey
	 * @param person
	 * @param raciRole
	 */
	public static void insertByWorkItemAndPersonAndRaciRole(Integer workItemKey, Integer person, String raciRole) {
		notifyDAO.insertByWorkItemAndPersonAndRaciRole(workItemKey, person, raciRole);
	}
	
	/**
	 * Whether the person has a specific raci Role for a workItem directly (not through group)
	 * @param workItemKey
	 * @param personKey
	 * @param raciRole if null it doesn't matter
	 * @return
	 * @throws Exception
	 */
	public static boolean hasDirectRaciRole(Integer workItemKey, Integer personKey, String raciRole) {
		return notifyDAO.hasDirectRaciRole(workItemKey, personKey, raciRole);
	}
	
	/**
	 * Gets the list of TNotifyBeans by a list of workItemIDs 
	 * for which the person (or a group it belongs to) has consultant or informant role 
	 * @param workItemIDs
	 * @param personIDs
	 * @return
	 */
	public static List<TNotifyBean> getByWorkItemsAndPersons(List<Integer> workItemIDs, List<Integer> personIDs) {
		return notifyDAO.getByWorkItemsAndPersons(workItemIDs, personIDs);
	}
	
	/**
	 * Loads the list of TNotifyBeans associated with items
	 * @param workItemIDs
	 * @param raciRole
	 * @return
	 */
	public static List<TNotifyBean> loadWatcherByItems(List<Integer> workItemIDs, String raciRole) {
		return notifyDAO.loadWatcherByItems(workItemIDs, raciRole);
	}
	
	/**
	 * Loads the list of TNotifyBeans associated with an array of workItems
	 * @param workItemIDs
	 * @return
	 */
	public static List<TNotifyBean> loadLuceneConsInf(int[] workItemIDs) {
		return notifyDAO.loadLuceneConsInf(workItemIDs);
	}
	
	/**
	 * Gets the watchers filtered by filterSelectsTO and raciBean
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	public static List<TNotifyBean> loadTreeFilterWatchers(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID) {
		return notifyDAO.loadTreeFilterWatchers(filterUpperTO, raciBean, personID);
	}
	
	/**
	 * Get the watchers for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	public static List<TNotifyBean> loadTQLFilterWatchers(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors) {
		return notifyDAO.loadTQLFilterWatchers(tqlExpression, personBean, locale, errors);
	}
	
	/**
	 * Counts the direct watcher persons for an item
	 * @param workItemID
	 * @return
	 */
	public static int countDirectWatcherPersons(Integer workItemID) {
		return notifyDAO.countDirectWatcherPersons(workItemID);
	}
	
	/**
	 * Load the consInfShow from the database
	 * @param workItemKey
	 */	
	public static void loadConsInfFromDb(Integer workItemKey, Integer personKey, ConsInfShow consInfShow) {
		//gather all direct consultants/informants persons/groups		
		consInfShow.setRealConsultantPersons(PersonBL.getDirectConsultantPersons(workItemKey));
		consInfShow.setRealConsultantGroups(PersonBL.getDirectConsultantGroups(workItemKey));
		consInfShow.setRealInformantPersons(PersonBL.getDirectInformantPersons(workItemKey));
		consInfShow.setRealInformantGroups(PersonBL.getDirectInformantGroups(workItemKey));

		//whether the current user is consultant 
		boolean isConsultant = hasDirectRaciRole(workItemKey, personKey, RaciRole.CONSULTANT);
		consInfShow.setAmIConsultant(isConsultant);

		//whether the current user is informant
		boolean isInformant = hasDirectRaciRole(workItemKey, personKey, RaciRole.INFORMANT);
		consInfShow.setAmIInformant(isInformant);
	}

	
	/**
	 * Whether the current user is selected for delete
	 * @param selectedPersons
	 * @param person
	 * @return
	 */
	public static boolean foundMeAsSelected(Integer[] selectedPersons, Integer person) {
		if (selectedPersons==null || person==null) {
			return false;
		} else {
			for (int i=0; i<selectedPersons.length; i++) {
				if (person.equals(selectedPersons[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Whether the current user is selected for delete
	 * @param personBeans
	 * @param personID
	 * @return
	 */
	public static boolean foundMeInRole(List<TPersonBean> personBeans, Integer personID) {
		if (personBeans==null || personID==null) {
			return false;
		} else {
			for (TPersonBean personBean : personBeans) {
				if (personBean.getObjectID().equals(personID)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Deletes those elements from list
	 * which correspond to elements of values array
	 * @param personBeans
	 * @param values
	 * @return
	 */
	public static void deleteSelected(List<TPersonBean> personBeans, Integer[] values) {
		if (personBeans==null || values==null || values.length==0) {
			return;
		}
		Set<Integer> valuesSet = new HashSet<Integer>();
		for (int i = 0; i < values.length; i++) {
			valuesSet.add(values[i]);			
		}
		Iterator<TPersonBean> iterator = personBeans.iterator();
		while (iterator.hasNext()) {
			TPersonBean personBean = iterator.next();
			if (valuesSet.contains(personBean.getObjectID())) {
				iterator.remove();
			}
		}		   		
	}
	

	/**
	 * Adds the person to the list by maintaining the alphabetical order
	 * @param personBeans
	 * @param personID
	 * @return
	 */
	public static void meAdd(List<TPersonBean> personBeans, Integer personID) {
		TPersonBean personBean;
		if (personBeans==null || personID==null) {
			return;
		}
		TPersonBean me = LookupContainer.getPersonBean(personID);
		for (int i = 0; i < personBeans.size(); i++) {
			personBean = personBeans.get(i);
			if (personBean.getObjectID().equals(personID)) {
				//if already in the list, do not add it again
				return;
			}
			if (me.getFullName().compareTo(personBean.getFullName())<0) {
				personBeans.add(i, me);
				return;
			}
		}
		personBeans.add(me);
	}

	/**
	 * Removes the person from the list
	 * @param personBeans
	 * @param personID
	 * @return
	 */
	public static void meRemove(List<TPersonBean> personBeans, Integer personID) {
		TPersonBean personBean;
		if (personBeans==null || personID==null) {
			return;
		}
		Iterator<TPersonBean> iterator = personBeans.iterator();
		while (iterator.hasNext()) {
			personBean = iterator.next();	
			if (personBean.getObjectID().equals(personID)) {
				iterator.remove();
				return;
			}
		}
	}
	
	/**
	 * Whether the current user can add himself as consulted: 
	 * because a consulted can implicitly edit the issue the user 
	 * should have explicit edit right to have this button available
	 * (implicit edit right through other RACI roles is not enough) 
	 * @param personID
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	public static boolean isEnableAddMeConsulted(Integer personID, Integer projectID, Integer issueTypeID) {
		return AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID, 
				AccessFlagIndexes.MODIFYANYTASK, true, false);
	}
	
	/**
	 * Whether the current user can add himself as informed
	 * because an informed can implicitly read the issue the user 
	 * should have explicit read right to have this button available
	 * (implicit read right through other RACI roles is not enough) 
	 * @param personID
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	public static boolean isEnableAddMeInformed(Integer personID, Integer projectID, Integer issueTypeID) {
		return AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID, 
				AccessFlagIndexes.READANYTASK, true, false);
	}
	
	/**
	 * Update the excel watchers
	 * @param rowNo
	 * @param workItemID
	 * @param rowNoToPseudoFieldsOriginal
	 * @param rowNoToPseudoFieldsExcel
	 * @return
	 */
	public static boolean updateExcelWatchers(Integer rowNo, Integer workItemID,
			Map<Integer, Map<Integer, List<Integer>>> rowNoToPseudoFieldsOriginal,
			Map<Integer, Map<Integer, List<Integer>>> rowNoToPseudoFieldsExcel) {
		List<Integer> consultedOriginal = null;
		List<Integer> consultedExcel =  null;
		List<Integer> informedOriginal = null;
		List<Integer> informedExcel =  null;
		if (rowNoToPseudoFieldsOriginal!=null) {
			Map<Integer, List<Integer>> pseudoFieldsOriginal = rowNoToPseudoFieldsOriginal.get(rowNo);
			if (pseudoFieldsOriginal!=null) {
				consultedOriginal = pseudoFieldsOriginal.get(TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST);
				informedOriginal = pseudoFieldsOriginal.get(TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST);
			}
		}
		if (rowNoToPseudoFieldsExcel!=null) {
			Map<Integer, List<Integer>> pseudoFieldsExcel = rowNoToPseudoFieldsExcel.get(rowNo);
			if (pseudoFieldsExcel!=null) {
				consultedExcel = pseudoFieldsExcel.get(TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST);
				informedExcel = pseudoFieldsExcel.get(TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST);
			}
		}
		boolean updatConsulted = ConsInfBL.saveWatchers(workItemID, consultedOriginal, consultedExcel,  RaciRole.CONSULTANT);
		boolean updateInformed = ConsInfBL.saveWatchers(workItemID, informedOriginal, informedExcel,  RaciRole.INFORMANT);
		return updatConsulted || updateInformed;
	}
	/**
	 * Gets the difference in watchers
	 * @param oldValues
	 * @param newValues
	 * @param addList
	 * @param deleteList
	 */
	private static void loadWatcherDifference(List<Integer> oldValues, List<Integer> newValues, List<Integer> addList, List<Integer> deleteList) {		
		if ((oldValues==null || oldValues.isEmpty()) && (newValues==null || newValues.isEmpty())) {
			return;
		}
		if (oldValues==null) {
			for (Integer objectID : newValues) {
				addList.add(objectID);
			}
		} else {
			if (newValues==null) {
				for (Integer objectID : oldValues) {
					deleteList.add(objectID);
				}
			} else {
				Collection<Integer> toRemove = CollectionUtils.subtract(oldValues, newValues);
				for (Integer objectID : toRemove) {
					deleteList.add(objectID);
				}
				Collection<Integer> toAdd = CollectionUtils.subtract(newValues, oldValues);
				for (Integer objectID : toAdd) {
					addList.add(objectID);
				}
			}
		}
	}

	/**
	 * Whether the watcher list changed
	 * @param oldValues
	 * @param newValues
	 */
	public static boolean watcherChanged(List<Integer> oldValues, List<Integer> newValues) {		
		if ((oldValues==null || oldValues.isEmpty()) && (newValues==null || newValues.isEmpty())) {
			return false;
		}
		if (oldValues==null) {
			return true;
			
		} else {
			if (newValues==null) {
				return true;
			} else {
				if (oldValues.size()!=newValues.size()) {
					return true;
				}
				Collection<Integer> intersection = CollectionUtils.intersection(oldValues, newValues);
				return intersection==null || newValues.size()!=intersection.size();
			}
		}
	}
	/**
	 * Update the watchers
	 * @param workItemID
	 * @param watcherOriginal
	 * @param watcherExcel
	 * @param raciRole
	 * @return
	 */
	private static boolean saveWatchers(Integer workItemID, List<Integer> watcherOriginal, List<Integer> watcherExcel, String raciRole) {
		boolean saveNeeded = false;
		List<Integer> addWatcher = new LinkedList<Integer>();
		List<Integer> deleteWatcher = new LinkedList<Integer>();
		loadWatcherDifference(watcherOriginal, watcherExcel, addWatcher, deleteWatcher);
		if (!addWatcher.isEmpty()) {
			saveNeeded = true;
			for (Integer consultedID : addWatcher) {
				ConsInfBL.insertByWorkItemAndPersonAndRaciRole(workItemID, consultedID, raciRole);
			}
		}
		if (!deleteWatcher.isEmpty()) {
			saveNeeded = true;
			ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID, GeneralUtils.createIntegerArrFromCollection(deleteWatcher), raciRole);
		}
		return saveNeeded;
	}
}
