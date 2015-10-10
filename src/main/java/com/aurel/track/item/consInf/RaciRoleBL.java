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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.screen.SystemActions;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.IEventSubscriber;
import com.aurel.track.util.event.parameters.AfterItemSaveEventParam;

/**
 * Business logic class for "load", and "save" (add from popup) operations 
 * related to consultants/informants  
 * Deals with  consultants/informants for 
 * new issues (managed in session) and 
 * existing issues (managed in database) 
 * @author Tamas Ruff
 * 
 */

public class RaciRoleBL {
	
	
	/**
	 * Loads the persons with specific raci role from database.
	 * The persons already selected in this role will be excluded 
	 * @param project
	 * @param issueType
	 * @param workItemKey
	 * @param raciRole
	 * @param consInfEdit
	 * @return
	 */
	public static List<ErrorData> loadFromDb(Integer project, Integer issueType, 
			Integer workItemKey, String raciRole, ConsInfEdit consInfEdit) {
		loadPotentialPersonsDb(project, issueType, raciRole, workItemKey, consInfEdit);
		return getLoadErrors(consInfEdit, raciRole);
	}
	
	
	/**
	 * Loads the persons with specific raci role, from database and session
	 * The persons already selected in this role will be excluded 
	 * @param project
	 * @param issueType
	 * @param raciRole
	 * @param consInfEdit
	 * @param consInfShow
	 * @return
	 */
	public static List<ErrorData> loadFromSession(Integer project, Integer issueType, String raciRole,
			ConsInfEdit consInfEdit, ConsInfShow consInfShow) {
		loadPotentialPersonsSession(project, issueType, raciRole, consInfEdit, consInfShow);
		return getLoadErrors(consInfEdit, raciRole);
	}
	
		
		
	/**
	 * Saves the raci roles for the selected persons in the database, 
	 * then reloads the data to be shown at the page
	 * @param project
	 * @param issueType
	 * @param workItemKey
	 * @param raciRole
	 * @param consInfEdit
	 * @return
	 */
	public static List<ErrorData> saveToDb(Integer project, Integer issueType, 
			Integer workItemKey, String raciRole, ConsInfEdit consInfEdit, Integer personID, Locale locale) {
		List<ErrorData> errors = getSaveErrors(consInfEdit);
		if (errors!=null && !errors.isEmpty()) {
			loadPotentialPersonsDb(project, issueType, raciRole, workItemKey, consInfEdit);
			return errors;
		}		
		Integer[] arrSelectedPersons = consInfEdit.getSelectedPersons();
		if (arrSelectedPersons!=null) {
			for (int i = 0; i<arrSelectedPersons.length; i++) {
				ConsInfBL.insertByWorkItemAndPersonAndRaciRole(workItemKey, arrSelectedPersons[i], raciRole);
			}
		}
		WorkItemContext workItemContext = FieldsManagerRT.loadWorkItem(personID, workItemKey, locale, SystemActions.EDIT);
		//TWorkItemBean workItemBean = ItemBL2.loadWorkItemSystemAttributes(workItemKey);
		AfterItemSaveEventParam afterItemSaveEventParam = new AfterItemSaveEventParam();
		afterItemSaveEventParam.setWorkItemNew(workItemContext.getWorkItemBean());
		afterItemSaveEventParam.setWorkItemOld(workItemContext.getWorkItemBean());
		afterItemSaveEventParam.setSelectedPersons(GeneralUtils.createListFromIntArr(arrSelectedPersons));
		//afterItemSaveEventParam.setSelectedGroups(GeneralUtils.createListFromIntArr(arrSelectedGroups));
		afterItemSaveEventParam.setFieldConfigs(workItemContext.getFieldConfigs());
		afterItemSaveEventParam.setLocale(locale);
		EventPublisher eventPublisher = EventPublisher.getInstance();
		if (eventPublisher!=null) {
			List<Integer> events = new LinkedList<Integer>();
			if (RaciRole.CONSULTANT.equals(raciRole)) {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADD_CONSULTED));
			} else {
				events.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_ADD_INFORMED));
			}
			afterItemSaveEventParam.setInterestingFields(workItemContext.getPresentFieldIDs());
			eventPublisher.notify(events, afterItemSaveEventParam);
		}
		
		//reload the consultants/informants from database
		loadPotentialPersonsDb(project, issueType, raciRole, workItemKey, consInfEdit);
		return errors;
	}
	
	/**
	 * Saves the raci roles for the selected persons in the consInfShow session object,
	 * then reloads the data to be shown at the page
	 * @param project
	 * @param issueType
	 * @param raciRole
	 * @param consInfEdit
	 * @param consInfShow
	 * @return
	 */
	public static List<ErrorData> saveToSession(Integer project, Integer issueType, 
			Integer person, String raciRole, 
			ConsInfEdit consInfEdit, ConsInfShow consInfShow) {
		List<ErrorData> errors = getSaveErrors(consInfEdit);
		if (errors!=null && !errors.isEmpty()) { 
			loadPotentialPersonsSession(project, issueType, raciRole, consInfEdit, consInfShow);
			return errors;
		} 
		Integer[] arrSelectedPersons = consInfEdit.getSelectedPersons();
		//Integer[] arrSelectedGroups = consInfEdit.getSelectedGroups();
		List<TPersonBean> realPersons = null;
		//List<TPersonBean> realGroups = null;
		boolean addMe = false;
		if (RaciRole.CONSULTANT.equalsIgnoreCase(raciRole.trim().toLowerCase())) {
			realPersons = consInfShow.getRealConsultantPersons();
			//realGroups = consInfShow.getRealConsultantGroups();
		} else {
			if (RaciRole.INFORMANT.equalsIgnoreCase(raciRole.trim().toLowerCase())) {
				realPersons = consInfShow.getRealInformantPersons();
				//realGroups = consInfShow.getRealInformantGroups();
			}
		}
		List<Integer> realPersonIDs = GeneralUtils.createIntegerListFromBeanList(realPersons);
		//List<Integer> realGroupIDs = GeneralUtils.createIntegerListFromBeanList(realGroups);
		//add the newly selected personIDs
		if (arrSelectedPersons!=null) {
			for (int i=0; i<arrSelectedPersons.length; i++) {
				realPersonIDs.add(arrSelectedPersons[i]);
				if (person!=null && person.equals(arrSelectedPersons[i])) {
					addMe = true;
				}
			}
		}
		/*if (arrSelectedGroups!=null) {
			for (int i=0; i<arrSelectedGroups.length; i++) {
				realGroupIDs.add(arrSelectedGroups[i]);
			}
		}*/
		
		//reread from the database to sort them alphabetically
		realPersons = PersonBL.loadSortedPersonsOrGroups(realPersonIDs);
		//realGroups = PersonBL.loadSortedPersonsOrGroups(realGroupIDs);
		if (RaciRole.CONSULTANT.equalsIgnoreCase(raciRole.trim().toLowerCase())) {
			consInfShow.setRealConsultantPersons(realPersons);
			//consInfShow.setRealConsultantGroups(realGroups);
			consInfShow.setSelectedConsultantPersons(new Integer[0]);
			consInfShow.setSelectedConsultantGroups(new Integer[0]);
			if (addMe) {
				consInfShow.setAmIConsultant(true);
			}
		} else {
			if (RaciRole.INFORMANT.equalsIgnoreCase(raciRole.trim().toLowerCase())) {
				consInfShow.setRealInformantPersons(realPersons);
				//consInfShow.setRealInformantGroups(realGroups);
				consInfShow.setSelectedInformantPersons(new Integer[0]);
				consInfShow.setSelectedInformantGroups(new Integer[0]);
				if (addMe) {
					consInfShow.setAmIInformant(true);
				}
			}
		}
		loadPotentialPersonsSession(project, issueType, raciRole, consInfEdit, consInfShow);
		return errors;
	}
	
	/**
	 * Saves the raci roles from the session in the database 
	 * after saving the new issue 
	 * @param workItemKey
	 * @param consInfShow
	 */  
	public static void saveFromSessionToDb(Integer workItemKey, ConsInfShow consInfShow){
		List<TPersonBean> realConsultantPersons = consInfShow.getRealConsultantPersons();
		savePersonList(workItemKey, realConsultantPersons, RaciRole.CONSULTANT);
		List<TPersonBean> realConsultantGroups = consInfShow.getRealConsultantGroups();
		savePersonList(workItemKey, realConsultantGroups, RaciRole.CONSULTANT);
		List<TPersonBean> realInformantPersons = consInfShow.getRealInformantPersons();
		savePersonList(workItemKey, realInformantPersons, RaciRole.INFORMANT);
		List<TPersonBean> realInformantGroups = consInfShow.getRealInformantGroups();
		savePersonList(workItemKey, realInformantGroups, RaciRole.INFORMANT);
	}
	/**
	 * Update the consultant and informant for an existing item
	 * @param workItemKey
	 * @param consInfEdit
	 * @param informant
	 */
	public static void update(Integer workItemKey, ConsInfEdit consInfEdit,String raciRole) {
		Integer[] selPerson = consInfEdit.getSelectedPersons();
		Integer[] selGroups=consInfEdit.getSelectedGroups();
		if((selPerson==null||selPerson.length==0)&&
				(selGroups==null||selGroups.length==0)){
			return;
		}
		//load real consultant persons
		List<TPersonBean> dbPersons;
		List<TPersonBean> dbGroups;
		if(RaciRole.CONSULTANT.equals(raciRole)){
			dbPersons= PersonBL.getDirectConsultantPersons(workItemKey);
			dbGroups= PersonBL.getDirectConsultantGroups(workItemKey);
		}else{
			dbPersons= PersonBL.getDirectInformantPersons(workItemKey);
			dbGroups= PersonBL.getDirectInformantGroups(workItemKey);
		}
		if(dbPersons==null){
			dbPersons=new LinkedList<TPersonBean>();
		}
		if(dbGroups==null){
			dbGroups=new LinkedList<TPersonBean>();
		}
		List<Integer> peopleToAdd=new LinkedList<Integer>();
		Integer[] peopleToDelete;
		
		//iterate on selPerson and remove form dbPersons if the person must add
		//at the end of iterations the dbPersons will contain the people that must be removed!
		if(selPerson!=null){
			for (int i = 0; i < selPerson.length; i++) {
				int index=indexOf(dbPersons, selPerson[i]);
				if(index==-1){
					//not found in DB-> must add
					peopleToAdd.add(selPerson[i]);
				}else{
					//person exist in the DB 
					//we remove form dbPerson
					dbPersons.remove(index);
				}
			}
		}
		if(selGroups!=null){
			for (int i = 0; i < selGroups.length; i++) {
				int index=indexOf(dbGroups, selGroups[i]);
				if(index==-1){
					//not found in DB-> must add
					peopleToAdd.add(selGroups[i]);
				}else{
					//person exist in the DB 
					//we remove form dbPerson
					dbGroups.remove(index);
				}
			}
		}
		//Collect the people that remain in the dbPersons and dbPerople
		//this people must be deleted
		peopleToDelete=new Integer[dbPersons.size()+dbGroups.size()];
		for (int i = 0; i < dbPersons.size(); i++) {
			peopleToDelete[i]=((TPersonBean)dbPersons.get(i)).getObjectID();
		}
		for (int i = 0; i < dbGroups.size(); i++) {
			peopleToDelete[dbPersons.size()+i]=((TPersonBean)dbGroups.get(i)).getObjectID();
		}
		
		if(!peopleToAdd.isEmpty()){
			for (int i = 0; i < peopleToAdd.size(); i++) {
				ConsInfBL.insertByWorkItemAndPersonAndRaciRole(workItemKey, (Integer)peopleToAdd.get(i), raciRole);
			}
			
		}
		ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemKey,peopleToDelete, raciRole);
	}
	private static int indexOf(List<TPersonBean> persons,Integer personId){
		for (int i = 0; i < persons.size(); i++) {
			if(persons.get(i).getObjectID().equals(personId)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Saves raci roles in a workitem for a list of persons
	 * @param workItemKey
	 * @param personList
	 * @param raciRole
	 */
	private static void savePersonList(Integer workItemKey, List<TPersonBean> personList, String raciRole) {
		if (personList!=null && !personList.isEmpty()) {
			for (TPersonBean personBean : personList) {
				ConsInfBL.insertByWorkItemAndPersonAndRaciRole(workItemKey, personBean.getObjectID(), raciRole);
			}
		}
	}
	
	/**
	 * Loads the persons with specific raci role, from database
	 * The persons already selected in this role will be excluded 
	 * @param project
	 * @param issueType
	 * @param raciRole
	 * @param workItemKey
	 * @param consInfEdit
	 */
	private static void loadPotentialPersonsDb(Integer project, Integer issueType, String raciRole,
			Integer workItemKey, ConsInfEdit consInfEdit) {
		//persons with raci role 
		List<TPersonBean> personsWithRole = new LinkedList<TPersonBean>();
		List<TPersonBean> groupsWithRole = new LinkedList<TPersonBean>();
		
		//already selected persons
		List<TPersonBean> actualPersons = new LinkedList<TPersonBean>();
		List<TPersonBean> actualGroups= new LinkedList<TPersonBean>();
		
		if (RaciRole.CONSULTANT.equalsIgnoreCase(raciRole.trim().toLowerCase())) {
			//load persons with consultant role
			personsWithRole = PersonBL.loadConsultantPersonsByProjectAndIssueType(project, issueType);
			//load groups with consultant role
			groupsWithRole  = PersonBL.loadConsultantGroupsByProjectAndIssueType(project, issueType);
			
			//load real consultant persons
			actualPersons = PersonBL.getDirectConsultantPersons(workItemKey);
			//load real consultant groups
			actualGroups = PersonBL.getDirectConsultantGroups(workItemKey);
		} else {
			if (RaciRole.INFORMANT.equalsIgnoreCase(raciRole.trim().toLowerCase())) {
				//load persons with informant role
				personsWithRole = PersonBL.loadInformantPersonsByProjectAndIssueType(project, issueType);
				//load groups with informant role
				groupsWithRole  = PersonBL.loadInformantGroupsByProjectAndIssueType(project, issueType);
				
				//load real informant persons
				actualPersons = PersonBL.getDirectInformantPersons(workItemKey);;
				//load real informant groups
				actualGroups = PersonBL.getDirectInformantGroups(workItemKey);
			}
		}
		//remove the persons with actual raci role from the persons with possible raci role
		if (actualPersons!=null) {
			removeExisting(personsWithRole, actualPersons);
		}	 
		if (actualGroups!=null) {
			removeExisting(groupsWithRole, actualGroups);	
		}
		consInfEdit.setPotentialPersons(personsWithRole);
		consInfEdit.setPotentialGroups(groupsWithRole);
	}

	/**
	 * Loads the persons with specific raci role, from database and session
	 * The persons already selected in this role will be excluded 
	 * @param project
	 * @param issueType
	 * @param raciRole
	 * @param consInfEdit
	 * @param consInfShow
	 */
	private static void loadPotentialPersonsSession(Integer project, Integer issueType, String raciRole,
			ConsInfEdit consInfEdit, ConsInfShow consInfShow) {
		//persons with raci role 
		List<TPersonBean> personsWithRole = new LinkedList<TPersonBean>();
		List<TPersonBean> groupsWithRole = new LinkedList<TPersonBean>();
		
		//already selected persons
		List<TPersonBean> actualPersons = new LinkedList<TPersonBean>();
		List<TPersonBean> actualGroups= new LinkedList<TPersonBean>();
		
		if (RaciRole.CONSULTANT.equalsIgnoreCase(raciRole.trim().toLowerCase())) {
			//load persons with consultant role
			personsWithRole = PersonBL.loadConsultantPersonsByProjectAndIssueType(project, issueType);
			//load groups with consultant role
			groupsWithRole = PersonBL.loadConsultantGroupsByProjectAndIssueType(project, issueType);
			
			//load real consultant persons
			actualPersons = consInfShow.getRealConsultantPersons();
			//load real consultant groups
			actualGroups = consInfShow.getRealConsultantGroups();
		} else {
			if (RaciRole.INFORMANT.equalsIgnoreCase(raciRole.trim().toLowerCase())) {
				//load persons with informant role
				personsWithRole = PersonBL.loadInformantPersonsByProjectAndIssueType(project, issueType);
				//load groups with informant role
				groupsWithRole  = PersonBL.loadInformantGroupsByProjectAndIssueType(project, issueType);
				
				//load real informant persons
				actualPersons = consInfShow.getRealInformantPersons();
				//load real informant groups
				actualGroups = consInfShow.getRealInformantGroups();
			}
		}
		//remove the persons with actual raci role from the persons with possible raci role
		if (actualPersons!=null) {
			removeExisting(personsWithRole, actualPersons);
		}	 
		if (actualGroups!=null) {
			removeExisting(groupsWithRole, actualGroups);
		}
		consInfEdit.setPotentialPersons(personsWithRole);
		consInfEdit.setPotentialGroups(groupsWithRole);
		 
	}
	
	/**
	 * Removes those personBeans from the personsWithRole list
	 * which appear in the actualPersons list
	 * @param personsWithRole
	 * @param actualPersons
	 */
	private static void removeExisting(List<TPersonBean> personsWithRole, List<TPersonBean> actualPersons) {
		if (actualPersons!=null && personsWithRole!=null) {
			for (TPersonBean actualPersonBean : actualPersons) {
				Iterator<TPersonBean> itrPersonsWithRole = personsWithRole.iterator();
				while (itrPersonsWithRole.hasNext()) {
					TPersonBean rolePersonBean = itrPersonsWithRole.next();
					if (rolePersonBean.getObjectID().equals(actualPersonBean.getObjectID())) {
						itrPersonsWithRole.remove();
					}
				}
			}
		}
	}
	
	/**
	 * Gets the errors occured during the loading (either from database or from session)
	 * @param consInfEdit
	 * @param raciRole
	 * @return
	 */
	private static List<ErrorData> getLoadErrors(ConsInfEdit consInfEdit, String raciRole) {
		List<ErrorData> errors = new LinkedList<ErrorData>();
		if (consInfEdit.getPotentialPersons().isEmpty() && consInfEdit.getPotentialGroups().isEmpty()) {
			ErrorData errorData =null;
			if (RaciRole.CONSULTANT.equalsIgnoreCase(raciRole.trim().toLowerCase())) {
				errorData =new ErrorData("addRaciRole.error.noConsultant");
			} else {
				if (RaciRole.INFORMANT.equalsIgnoreCase(raciRole.trim().toLowerCase())) {
					errorData =new ErrorData("addRaciRole.error.noInformant");
				}
			}
			errors.add(errorData);
		}
		return errors;
	}
	
	/**
	 * Gets the errors occured during the saving (either from database or from session)
	 * @param consInfEdit
	 * @return
	 */
	private static List<ErrorData> getSaveErrors(ConsInfEdit consInfEdit) {
		List<ErrorData> errors = new LinkedList<ErrorData>();
		Integer[] arrSelectedPersons = consInfEdit.getSelectedPersons();
		//Integer[] arrSelectedGroups = consInfEdit.getSelectedGroups();
		if ((arrSelectedPersons==null || arrSelectedPersons.length==0) /*&&
				(arrSelectedGroups==null || arrSelectedGroups.length==0)*/) {
			ErrorData errorData = new ErrorData("addRaciRole.error.notSelected");
			errors.add(errorData);
		}
		return errors;
	}
}
