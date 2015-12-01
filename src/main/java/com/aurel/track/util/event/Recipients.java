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



package com.aurel.track.util.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.notify.settings.NotifySettingsBL;
import com.aurel.track.admin.customize.notify.trigger.NotifyTriggerBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TNotifyFieldBean;
import com.aurel.track.beans.TNotifySettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.CustomSelectUtil;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.fieldType.types.custom.CustomUserPicker;
import com.aurel.track.util.GeneralUtils;

/**
 * This class manages a list of email recipients for e-mail notification
 * It is a regular ArrayList with additional method <code>addUnique</code>
 * that makes sure no duplicate e-mail addresses are in the list.
 */
public class Recipients {
	private static final Logger LOGGER = LogManager.getLogger(Recipients.class);
	//the workItem bean the RACI person list is build for
	private TWorkItemBean workItemBean;
	private TWorkItemBean workItemBeanOld;
	//the personBeans to notify
	private List<TPersonBean> personsBeansToNotify = new ArrayList<TPersonBean>();
	//the personIDs to notify
	private Set<Integer> personsIDsToNotify = new HashSet<Integer>();
	//a map with personIDs to substituteIDs
	private Map<Integer, Integer> personToSubstituteMap = new HashMap<Integer, Integer>();
	//associates the picker persons to the  user picker fieldIDs: a person can be selected in more than one user picker (directly or through groups)
	private Map<Integer, List<Integer>> personsIDsInUserPickerFieldIDsMap = null;
	//associates the user picker fieldIDs to the selected automail RACI role
	protected Map<Integer, Integer> userPickerFieldIDsToRaciRolesMap = null;
	
	public Recipients(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOld) {
		this.workItemBean = workItemBean;
		this.workItemBeanOld = workItemBeanOld;
		//gets all user picker fields from the workItemBean
		Set<Integer> userPickerFieldIDs = getAllUserPickerFields(workItemBean);
		if (userPickerFieldIDs!=null && !userPickerFieldIDs.isEmpty()) {
			this.personsIDsInUserPickerFieldIDsMap = getPersonIDsToUserPickerFieldIDs(userPickerFieldIDs);
			if (this.personsIDsInUserPickerFieldIDsMap!=null && !this.personsIDsInUserPickerFieldIDsMap.isEmpty()) {
				this.userPickerFieldIDsToRaciRolesMap = userPickerFieldIDsToRaciRoles(userPickerFieldIDs);
			}
		}
	}
	
	/**
	 * Gets the RACI roles set a picker person is implied (a person could be selected in more than one user picker field directly or through group)
	 * @param personID
	 * @return
	 */
	public Set<Integer> getPickerRaciRolesForPerson(Integer personID) {
		Set<Integer> pickerRaciRolesSet = new HashSet<Integer>();
		if (personsIDsInUserPickerFieldIDsMap!=null && userPickerFieldIDsToRaciRolesMap!=null) {
			List<Integer> pickerFieldList = personsIDsInUserPickerFieldIDsMap.get(personID);
			if (pickerFieldList!=null && !pickerFieldList.isEmpty()) {
				for (Integer fieldID : pickerFieldList) {
					Integer raciRole = userPickerFieldIDsToRaciRolesMap.get(fieldID);
					if (raciRole!=null) {
						pickerRaciRolesSet.add(raciRole);
					}
				}
			}
		}
		return pickerRaciRolesSet;
	}
	
	/**
	 * Build the list with the persons to be notified for all raci roles 
	 * @param projectIDs
	 * @param isCreated
	 */
	public void buildRaciList(List<Integer> projectIDs, boolean isCreated) {
		Integer managerID = workItemBean.getOwnerID();
		Integer responsibleID = workItemBean.getResponsibleID();
		Integer originatorID = workItemBean.getOriginatorID();
		//add real manager
		if (addUnique(managerID, "manager")) {
			addSubstituterPerson(managerID, "manager's substituter");
		}
		if (workItemBeanOld!=null) {
			Integer oldManagerID = workItemBeanOld.getOwnerID();
			if (oldManagerID!=null && !oldManagerID.equals(managerID)) {
				addUnique(oldManagerID, "oldManager");
			}
		}
		addResponsible(LookupContainer.getPersonBean(responsibleID));
		if (workItemBeanOld!=null) {
			Integer oldResponsibleID = workItemBeanOld.getResponsibleID();
			if (oldResponsibleID!=null && !oldResponsibleID.equals(responsibleID)) {
				addResponsible(LookupContainer.getPersonBean(oldResponsibleID));
			}
		}
		String submitterEmail = workItemBean.getSubmitterEmail();
		//the POP3/IMAP submitter should get e-mail modifying the issue values
		//according to originator trigger settings for the "guest" user
		if (submitterEmail!=null && !"".equals(submitterEmail)) {
			//submitted through e-mail
			boolean personIsKnown = addEmailAddress(submitterEmail, originatorID);
			if (personIsKnown) {
				//not guest: although the corresponding e-mail address is added already
				//but just to be sure that even if the e-mail address of the originator 
				//was changed since the submission we still add also the e-mail of the actual originator
				if (addUnique(originatorID, "originator")) {
					addSubstituterPerson(originatorID, "originator's substituter");
				}
			}
			//do not add the guest user as originator if submitterEmail 
			//is set because in this case guest is only a dummy originator not a real one
			//if it would be the real one then personIsKnown would be true 
		} else {
			//created through web UI
			if (addUnique(originatorID, "originator")) {
				addSubstituterPerson(originatorID, "originator's substituter");
			}
		}
		//add watchers
		addConsultantsInformants(workItemBean);
		if (personsIDsInUserPickerFieldIDsMap!=null) {
			addUserPickers(personsIDsInUserPickerFieldIDsMap.keySet());
		}
		//persons with read right are potential observers  
		List<TPersonBean> personsWithReadRights = PersonBL.getPersonsWithRightInProjectAndListType(
				workItemBean.getProjectID(), workItemBean.getListTypeID(),
				AccessBeans.AccessFlagIndexes.READANYTASK, true,
				TPersonBean.PERSON_CATEGORY.ALL_PERSONS, null, true, true);
		List<Integer> personIDsWithReadRight = GeneralUtils.createIntegerListFromBeanList(personsWithReadRights);
		personIDsWithReadRight.removeAll(personsIDsToNotify);
		if (!personIDsWithReadRight.isEmpty()) {
			//other than RACI persons exist
			List<Integer> ancestorProjectIDs = GeneralUtils.createIntegerListFromIntegerArr(ProjectBL.getAncestorProjects(GeneralUtils.createIntegerArrFromCollection(projectIDs)));
			addObserversByPersonAutomailSettings(personIDsWithReadRight, ancestorProjectIDs, isCreated);
			if (!personIDsWithReadRight.isEmpty()) {
				List<Integer> genericProjectList = new LinkedList<Integer>();
				genericProjectList.add(NotifySettingsBL.OTHERPROJECTSID);
				addObserversByPersonAutomailSettings(personIDsWithReadRight, genericProjectList, isCreated);
				if (!personIDsWithReadRight.isEmpty()) {
					if (!addObserversByProjectAutomailSettings(personsWithReadRights, ancestorProjectIDs, isCreated)) {
						List<Integer> defaultGenericProjectList = new LinkedList<Integer>();
						defaultGenericProjectList.add(NotifySettingsBL.OTHERPROJECTSID);
						addObserversByProjectAutomailSettings(personsWithReadRights, defaultGenericProjectList, isCreated);
					}
				}
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Persons to notify before trigger/filter: ");
			for (TPersonBean personBean : personsBeansToNotify) {
				LOGGER.debug(personBean.getName() + " ("+ personBean.getObjectID() +")");
			}
		}
	}

	/**
	 * Adds a person based on emailAddress to the recipient 
	 * @param emailAddress whether the person is known
	 * @return
	 */
	private boolean addEmailAddress(String emailAddress, Integer originatorID) {
		TPersonBean submitterPerson = null;
		if (emailAddress!=null && !"".equals(emailAddress)) {
			List<TPersonBean> submitters = PersonBL.loadByEmail(emailAddress);
			if (submitters!=null && !submitters.isEmpty()) {
				if (LOGGER.isDebugEnabled() && submitters.size()>1) {
					LOGGER.debug("There are " + submitters.size() + " having e-mail address " + emailAddress);
				}
				submitterPerson = submitters.get(0);
			}
		}
		if (submitterPerson==null) {
			//unknown user get the "guest" as base but do not set the objectID
			submitterPerson = PersonBL.loadByLoginName(TPersonBean.GUEST_USER);
			if (submitterPerson==null && originatorID!=null) {
				//no guest exists get the originator as base
				submitterPerson = LookupContainer.getPersonBean(originatorID);
			}
			TPersonBean personBean = new TPersonBean();
			if (submitterPerson!=null) {
				personBean.setPrefLocale(submitterPerson.getPrefLocale());
				//include in recipients only if guest user accepts e-mail 
				personBean.setNoEmailPleaseBool(submitterPerson.getNoEmailPleaseBool());
				personBean.setPrefEmailType(submitterPerson.getPrefEmailType());
			}
			personBean.setEmail(emailAddress);
			personBean.setFirstName("");
			personBean.setLastName("");
			personBean.setDeleted("N");
			addUnique(personBean, "guest submitter");
			return false;
		} else {
			//known user
			addUnique(submitterPerson, "submitter " + emailAddress);
			return true;
		}
	}

	/**
	 * Add also the substituter person if exits
	 * @param personID
	 * @param role
	 */
	private void addSubstituterPerson(Integer personID, String role) {
		TPersonBean personBean = LookupContainer.getPersonBean(personID);
		if (personBean!=null) {
			Integer substitutePersonID = personBean.getSubstituteID();
			//add substituted person if exists
			if (substitutePersonID!=null) {
				personToSubstituteMap.put(personID, substitutePersonID);
				LOGGER.debug("Add " + personBean.getFirstName() + " " + personBean.getLastName() + " to notification list as " + role);
			}
		}
	}
	
	/**
	 * Add the person if not yet added
	 * @param personID
	 * @param raciRole
	 */
	private boolean addUnique(Integer personID, String raciRole) {
		if (!personsIDsToNotify.contains(personID)) {
			TPersonBean personBean = LookupContainer.getPersonBean(personID);
			if (personBean!=null) {
				return addUnique(personBean, raciRole);
			}
			return false;
		} else {
			//already added
			return true;
		}
	}
	
	/**
	 * Add a new person if e-mail not yet added
	 * @param personBean
	 * @param raciRole
	 * @return false if the e-mail should not be sent because then the supplier shouldn't be notified
	 */
	public boolean addUnique(TPersonBean personBean, String raciRole) {
		if (personBean==null){
			LOGGER.debug("Notifying rejected because the user is null");
			return false;
		}
		if (personBean.getEmail()==null
			|| personBean.getEmail().length() < 1) { 
			LOGGER.debug("Notifying " + personBean.getName() + " as " + raciRole + " rejected because the user has no e-mail address");
			return false;
		} else {
			if (personBean.getNoEmailPleaseBool()) {
				LOGGER.debug("Notifying " + personBean.getName() + " for " + raciRole + " rejected because the user do not want to receive e-mail at all");
				return false;
			} else {
				if (BooleanFields.TRUE_VALUE.equals(personBean.getDeleted())) {
					LOGGER.debug("Notifying " + personBean.getLabel() + " for " + raciRole + " rejected because the user is not active");
					return false;
				} else {
					if (workItemBean.isAccessLevelFlag() && !personBean.getObjectID().equals(workItemBean.getOriginatorID())) {
						LOGGER.debug("Notifying " + personBean.getName() + " for " + raciRole + " rejected because it is a private issue");
						return false;
					} else {
						if (workItemBean.isArchivedOrDeleted() &&
								!PersonBL.isProjectAdmin(personBean.getObjectID(), workItemBean.getProjectID())) {
							LOGGER.debug("Notifying " + personBean.getName() + " for " + raciRole + " rejected because the issue is archived/deleted");
							return false;
						}
					}
				}
			}
		}
		String mailAddress = personBean.getEmail();
		boolean addThisAddress = true;
		for (TPersonBean personToNotify : personsBeansToNotify) {
			if (mailAddress.equals(personToNotify.getEmail())) {
				addThisAddress = false;
				LOGGER.debug("Adding " + personToNotify.getName() + " (" + personBean.getObjectID() + ") as " + raciRole + " rejected because e-mail is already in list");
				break;
			}
		}
		if (addThisAddress) {
			LOGGER.debug("Add " + personBean.getName() + " (" + personBean.getObjectID() + ") to notification list as " + raciRole);
			personsBeansToNotify.add(personBean);
			personsIDsToNotify.add(personBean.getObjectID());
		}
		return true;
	}
	
	/**
	 * Also a group can be responsible.
	 * In this case add each person from the group to the list
	 * @param responsibleBean
	 */
	private void addResponsible(TPersonBean responsibleBean) {
		if (responsibleBean!=null) {
			if (responsibleBean.isGroup()) {
				List<TPersonBean> responsibleRoleThroughGroup = 
						PersonBL.loadPersonsForGroup(responsibleBean.getObjectID());
				if (responsibleRoleThroughGroup!=null) {
					for (TPersonBean personBean : responsibleRoleThroughGroup) {
						//add responsible persons through group
						if (addUnique(personBean, "responsible through group")) {
							addSubstituterPerson(personBean.getObjectID(), "responsible through group's substituter");
						}
					}
				}
			} else {
				if (addUnique(responsibleBean, "responsible")) {
					addSubstituterPerson(responsibleBean.getObjectID(), "responsible's substituter");
				}
			}
		}
	}
	
	/**
	 * Add the user picker persons
	 * @param userPickerPersons
	 */
	private void addUserPickers(Set<Integer> userPickerPersons) {
		if (userPickerPersons!=null && !userPickerPersons.isEmpty()) {
			List<TPersonBean> pickerPersonBeans = PersonBL.loadByKeys(
					GeneralUtils.createListFromCollection(userPickerPersons));
			addPersons(pickerPersonBeans, "user picker", "user picker substituter");
		}
	}
	
	/**
	 * Get the persons to be notified as consultant/informant  
	 * @param workItemBean
	 */
	private void addConsultantsInformants(TWorkItemBean workItemBean) {
		//get the persons to be notified through RACI role (consultants or informants)
		//for this persons no read access should be tested because they have read access 
		//through the fact that they are either informants or consultants.
		List<TPersonBean> personNotifyThroughRaciRole = 
				PersonBL.loadNotifyThroughRaci(workItemBean.getObjectID());
		//informants and consultants can be also groups not just persons
		addPersonsOrGroups(personNotifyThroughRaciRole, "watcher", "watcher's substituter", "watcher through group", "watcher through group's substituter");
	}

	/**
	 * Add persons related to personsOrGroups list
	 * @param personsOrGroups
	 * @param personRole the persons from personsOrGroups
	 * @param personSubstituter the substituter persons for persons
	 * @param throughGroupRole the get the persons member of groups from personsOrGroups
	 * @param throughGroupSubstituter the substituter persons for group member persons
	 */
	private void addPersonsOrGroups(List<TPersonBean> personsOrGroups, String personRole, String personSubstituter, String throughGroupRole, String throughGroupSubstituter) {
		List<Integer> groupIDs = new ArrayList<Integer>();
		if (personsOrGroups!=null && !personsOrGroups.isEmpty()) {
			for (TPersonBean personBean : personsOrGroups) {
				Integer personID = personBean.getObjectID();
				if (personBean.isGroup()) {
					groupIDs.add(personID);
				} else {
					//add direct RACI persons
					if (addUnique(personBean, personRole)) {
						addSubstituterPerson(personID, personSubstituter);
					}
				}
			}
			if (!groupIDs.isEmpty()) {
				List<TPersonBean> personRaciRoleThroughGroup = 
					PersonBL.loadPersonsForGroups(groupIDs);
				if (personRaciRoleThroughGroup!=null) {
					for (TPersonBean personBean : personRaciRoleThroughGroup) {
						//add consultant/informant persons through group
						if (addUnique(personBean, throughGroupRole)) {
							addSubstituterPerson(personBean.getObjectID(), throughGroupSubstituter);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Add persons related to personsOrGroups list
	 * @param persons
	 * @param personRole the persons from personsOrGroups
	 * @param personSubstituter the substituter persons for persons
	 * @param throughGroupRole the get the persons member of groups from personsOrGroups
	 * @param throughGroupSubstituter the substituter persons for group member persons
	 */
	private void addPersons(List<TPersonBean> persons, String personRole, String personSubstituter) {
		if (persons!=null && !persons.isEmpty()) {
			for (TPersonBean personBean : persons) {
				Integer personID = personBean.getObjectID();
				//add direct RACI persons
				if (addUnique(personBean, personRole)) {
					addSubstituterPerson(personID, personSubstituter);
				}
			}
		}
	}
	
	/**
	 * Add observer person set as person specific automail assignment
	 * @param personIDsWithReadRight
	 * @param projectIDs
	 */
	private void addObserversByPersonAutomailSettings(List<Integer> personIDsWithReadRight, List<Integer> projectIDs, boolean isCreated) {
		if (!personIDsWithReadRight.isEmpty()) {
			Integer actionType = null;
			if (isCreated) {
				actionType = Integer.valueOf(NotifyTriggerBL.ACTIONTYPE.CREATE_ISSUE);
			} else {
				actionType = Integer.valueOf(NotifyTriggerBL.ACTIONTYPE.EDIT_ISSUE);
			}
			List<TPersonBean> observerPersonsInProject = PersonBL.getObserverPersonsInProjects(personIDsWithReadRight, projectIDs, actionType);
			if (observerPersonsInProject!=null && observerPersonsInProject.size()>0) {
				for (TPersonBean personBean : observerPersonsInProject) {
					addUnique(personBean, "direct observer in project " + projectIDs.get(0));
					personIDsWithReadRight.remove(personBean.getObjectID());
				}
			}
		}
	}
	
	/**
	 * Add observer person set as project specific automail assignment
	 * @param personIDsWithReadRight
	 * @param projectIDs
	 * @param isCreated
	 * @return true if project specific automail setting found
	 */
	private boolean addObserversByProjectAutomailSettings(List<TPersonBean> personIDsWithReadRight, List<Integer> projectIDs, boolean isCreated) {
		List<TNotifySettingsBean> defaultsByProjects = NotifySettingsBL.loadDefaultsByProjects(projectIDs);
		if (defaultsByProjects!=null && !defaultsByProjects.isEmpty()) {
			for (TNotifySettingsBean notifySettingsBean : defaultsByProjects) {
				Integer triggerID = notifySettingsBean.getNotifyTrigger();
				if (triggerID!=null) {
					List<TNotifyFieldBean> notifyFieldBeans = null;
					if (isCreated) {
						//add
						notifyFieldBeans = NotifyTriggerBL.getTriggerFieldsForRaciRole(null, projectIDs,
							Integer.valueOf(NotifyTriggerBL.ACTIONTYPE.CREATE_ISSUE), null);
					} else {
						//edit
						notifyFieldBeans = NotifyTriggerBL.getTriggerFieldsForRaciRole(null, projectIDs, 
							Integer.valueOf(NotifyTriggerBL.ACTIONTYPE.EDIT_ISSUE), 
							Integer.valueOf(NotifyTriggerBL.FIELDTYPE.ISSUE_FIELD));
					}
					boolean observerFieldFound = false;
					if (notifyFieldBeans!=null) {
						for (TNotifyFieldBean notifyFieldBean : notifyFieldBeans) {
							if (notifyFieldBean.isObserver()) {
								observerFieldFound = true;
								break;
							}
						}
					}
					if (observerFieldFound) {
						for (TPersonBean personBean : personIDsWithReadRight) {
							addUnique(personBean, "project inherited observer in project " + projectIDs.get(0));
						}
					}
				}
			}
			//project specific setting found
			return true;
		}
		//no project specific setting found 
		return false;
	}
	
	public List<TPersonBean> getPersonsBeansToNotify() {
		return personsBeansToNotify;
	}

	public Map<Integer, Integer> getPersonToSubstituteMap() {
		return personToSubstituteMap;
	}
	
	/**
	 * Gather all user picker fieldIDs for a workItemBean
	 * @param workItemBean
	 * @return
	 */
	private Set<Integer> getAllUserPickerFields(TWorkItemBean workItemBean) {
		Set<Integer> userPickerFields = new HashSet<Integer>();
		Set<Integer> customFields = workItemBean.getCustomFieldIDSet();
		if (customFields!=null) {
			for (Integer fieldID : customFields) {
				IFieldTypeRT fieldType = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldType.isUserPicker()) {
					userPickerFields.add(fieldID);
					if (LOGGER.isDebugEnabled()) {
						TFieldBean fieldBean = FieldBL.loadByPrimaryKey(fieldID);
						if (fieldBean!=null) {
							LOGGER.debug("User picker field found: " + fieldBean.getName() + " (" + fieldID + ")");
						}
					}
				}
			}
		}
		return userPickerFields;
	}
	
	/**
	 * Returns a map:	
	 * 	-	key: personID
	 * 	-	value: list of user picker fieldIDs where the person is selected
	 * @param userPickerFields
	 * @return
	 */
	private Map<Integer, List<Integer>> getPersonIDsToUserPickerFieldIDs(Set<Integer> userPickerFields) {
		Map<Integer, List<Integer>> personIDToUserPickerFieldIDs = new HashMap<Integer, List<Integer>>();
		if (userPickerFields!=null && !userPickerFields.isEmpty()) {
			for (Integer userPickerFieldID : userPickerFields) {
				Object objSelectedPersons = workItemBean.getAttribute(userPickerFieldID, null);
				Integer[] selectedPersons = CustomSelectUtil.getSelectedOptions(objSelectedPersons);
				if (selectedPersons!=null && selectedPersons.length>0) {
					for (int i = 0; i < selectedPersons.length; i++) {
						Integer selectedPerson = selectedPersons[i];
						if (selectedPerson!=null) {
							TPersonBean personBean = LookupContainer.getPersonBean(selectedPerson);
							if (personBean!=null) {
								if (personBean.isGroup()) {
									LOGGER.debug("Group " + personBean.getLabel() + "(" + selectedPerson + ") selected in user picker field " + userPickerFieldID );
									List<TPersonBean> groupMemberPersonBeans = PersonBL.loadPersonsForGroup(selectedPerson);
									if (groupMemberPersonBeans!=null) {
										for (TPersonBean memberPersonBean : groupMemberPersonBeans) {
											Integer memberID = memberPersonBean.getObjectID();
											List<Integer> userPickersFields = personIDToUserPickerFieldIDs.get(memberID);
											if (userPickersFields==null) {
												userPickersFields = new ArrayList<Integer>();
												personIDToUserPickerFieldIDs.put(memberID, userPickersFields);
											}
											userPickersFields.add(userPickerFieldID);
											LOGGER.debug("Person " + memberPersonBean.getLabel() + "(" + memberID + ") added through selected group in user picker field " + userPickerFieldID);
										}
									}
								} else {
									List<Integer> userPickersFields = personIDToUserPickerFieldIDs.get(selectedPerson);
									if (userPickersFields==null) {
										userPickersFields = new ArrayList<Integer>();
										personIDToUserPickerFieldIDs.put(selectedPerson, userPickersFields);
									}
									userPickersFields.add(userPickerFieldID);
									LOGGER.debug("Person " + personBean.getLabel() + "(" + selectedPerson + ") selected in user picker field " + userPickerFieldID);
								}
							}
						}
					}
				}
			}
		}
		return personIDToUserPickerFieldIDs;
	}
	
	/**
	 * Returns a map:	
	 * 	-	key: user picker fieldID
	 * 	-	value: the selected automail RACI option
	 * @param userPickerFieldIDs
	 * @return
	 */
	private Map<Integer,Integer> userPickerFieldIDsToRaciRoles(Set<Integer> userPickerFieldIDs) {
		Map<Integer,Integer> userPickerFieldToRaciTemplate = new HashMap<Integer,Integer>();
		if (userPickerFieldIDs!=null && !userPickerFieldIDs.isEmpty()) {
			for (Integer userPickerFieldID : userPickerFieldIDs) {
				TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getValidConfig(userPickerFieldID, workItemBean.getListTypeID(), workItemBean.getProjectID());
				if (fieldConfigBean!=null) {
					TGeneralSettingsBean automailSelectionBean = GeneralSettingsBL.loadSingleByConfigAndParameter(fieldConfigBean.getObjectID(), 
						Integer.valueOf(CustomUserPicker.PARAMETERCODES.AUTOMAIL_OPTION));
					if (automailSelectionBean!=null) {
						Integer automailSelection = automailSelectionBean.getIntegerValue();
							if (automailSelection!=null && automailSelection.intValue()!=CustomUserPicker.AUTOMAIL_OPTIONS.NOMAIL) {
								//the auto mail selection from user picker settings 
								userPickerFieldToRaciTemplate.put(userPickerFieldID, automailSelection);
								LOGGER.debug("Automail option for field config " + fieldConfigBean.getLabel() +
										" ("+ userPickerFieldID +"): " + automailSelection);
							}
					} else {
						//on behalf of picker: get the auto mails as originator
						userPickerFieldToRaciTemplate.put(userPickerFieldID, CustomUserPicker.AUTOMAIL_OPTIONS.ORIGINATOR);
						LOGGER.debug("No automail option found for field config " + fieldConfigBean.getLabel() +
								" ("+ userPickerFieldID +"). Take it as on behalf person, automail according to Author");
					}
				}
			}
		}
		return userPickerFieldToRaciTemplate;
	}
}
