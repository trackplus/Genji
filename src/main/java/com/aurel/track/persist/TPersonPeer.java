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


package com.aurel.track.persist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.CriteriaUtil;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TBasketBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.PersonDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.util.GeneralUtils;
import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;
import com.workingdogs.village.Value;

public class TPersonPeer extends com.aurel.track.persist.BaseTPersonPeer implements PersonDAO {

	private static final long serialVersionUID = 500L;
	private static final Logger LOGGER = LogManager.getLogger(TPersonPeer.class);

	private static Class[] replacePersonPeerClasses = { TAttachmentPeer.class, TBaseLinePeer.class, TCostPeer.class, TIssueAttributeValuePeer.class,
			TProjectPeer.class, TProjectPeer.class, TPublicReportRepositoryPeer.class, TStateChangePeer.class, TTrailPeer.class,
			TWorkItemPeer.class,
			TWorkItemPeer.class,
			TWorkItemPeer.class,
			TWorkItemPeer.class,
			TBudgetPeer.class,
			TActualEstimatedBudgetPeer.class,
			TFieldPeer.class,
			TScreenPeer.class,
			// dealt with explicitly to delete the private ones but preserve the
			// public and project triggers
			TNotifyTriggerPeer.class,
			// dealt with explicitly to delete the private ones but preserve the
			// public and project triggers
			TQueryRepositoryPeer.class,
			// dealt with explicitly to delete the private ones but preserve the
			// public and project triggers
			TExportTemplatePeer.class, TListPeer.class, TComputedValuesPeer.class, THistoryTransactionPeer.class, TSummaryMailPeer.class,
			TWorkItemLinkPeer.class, TScriptsPeer.class, TMSProjectExchangePeer.class, TFilterCategoryPeer.class, TReportCategoryPeer.class,
			TWorkflowDefPeer.class, TWorkflowActivityPeer.class, TWorkflowActivityPeer.class, TWorkflowGuardPeer.class, TEscalationEntryPeer.class,
			TPersonInDomainPeer.class, TAttachmentVersionPeer.class, TPersonPeer.class, };

	private static String[] replacePersonFields = { TAttachmentPeer.CHANGEDBY, TBaseLinePeer.CHANGEDBY, TCostPeer.PERSON, TIssueAttributeValuePeer.PERSON,
			TProjectPeer.DEFMANAGER, TProjectPeer.DEFOWNER, TPublicReportRepositoryPeer.OWNER, TStateChangePeer.CHANGEDBY, TTrailPeer.CHANGEDBY,
			TWorkItemPeer.CHANGEDBY,
			TWorkItemPeer.ORIGINATOR,
			TWorkItemPeer.OWNER,
			TWorkItemPeer.RESPONSIBLE,
			TBudgetPeer.CHANGEDBY,
			TActualEstimatedBudgetPeer.CHANGEDBY,
			TFieldPeer.OWNER,
			TScreenPeer.OWNER,
			// dealt with explicitly to delete the private ones but preserve the
			// public and project triggers
			TNotifyTriggerPeer.PERSON,
			// dealt with explicitly to delete the private ones but preserve the
			// public and project triggers
			TQueryRepositoryPeer.PERSON,
			// dealt with explicitly to delete the private ones but preserve the
			// public and project triggers
			TExportTemplatePeer.PERSON, TListPeer.OWNER, TComputedValuesPeer.PERSON, THistoryTransactionPeer.CHANGEDBY, TSummaryMailPeer.PERSONFROM,
			TWorkItemLinkPeer.CHANGEDBY, TScriptsPeer.CHANGEDBY, TMSProjectExchangePeer.CHANGEDBY, TFilterCategoryPeer.CREATEDBY,
			TReportCategoryPeer.CREATEDBY, TWorkflowDefPeer.OWNER, TWorkflowActivityPeer.NEWMAN, TWorkflowActivityPeer.NEWRESP, TWorkflowGuardPeer.PERSON,
			TEscalationEntryPeer.ESCALATETO, TPersonInDomainPeer.PERSONKEY, TAttachmentVersionPeer.CHANGEDBY, TPersonPeer.SUBSTITUTEKEY, };

	private static Class[] deletePersonPeerClasses = { TAccessControlListPeer.class, TNotifyPeer.class, TPrivateReportRepositoryPeer.class,
			TReportLayoutPeer.class, TSchedulerPeer.class, TGroupMemberPeer.class, TNotifySettingsPeer.class, TWorkItemLockPeer.class,
			TLoggedInUsersPeer.class, TSummaryMailPeer.class, TTemplatePersonPeer.class, TReportPersonSettingsPeer.class, TDashboardScreenPeer.class,
			TMenuitemQueryPeer.class, TPersonBasketPeer.class, TBasketPeer.class, TLastVisitedItemPeer.class, TReadIssuePeer.class,
			TLastExecutedQueryPeer.class, TWorkFlowPeer.class, TDashboardScreenPeer.class, TReportSubscribePeer.class, TGridLayoutPeer.class,
			TNavigatorLayoutPeer.class, TMailTextBlockPeer.class, TUserFeaturePeer.class,
			// delete itself
			TPersonPeer.class };

	private static String[] deletePersonFields = { TAccessControlListPeer.PERSONKEY, TNotifyPeer.PERSONKEY, TPrivateReportRepositoryPeer.OWNER,
			TReportLayoutPeer.PERSON, TSchedulerPeer.PERSON, TGroupMemberPeer.PERSON, TNotifySettingsPeer.PERSON, TWorkItemLockPeer.PERSON,
			TLoggedInUsersPeer.LOGGEDUSER, TSummaryMailPeer.PERSONTO, TTemplatePersonPeer.PERSON, TReportPersonSettingsPeer.PERSON,
			TDashboardScreenPeer.PERSONPKEY, TMenuitemQueryPeer.PERSON, TPersonBasketPeer.PERSON, TBasketPeer.PERSON, TLastVisitedItemPeer.PERSON,
			TReadIssuePeer.PERSON, TLastExecutedQueryPeer.PERSON, TWorkFlowPeer.RESPONSIBLE, TDashboardScreenPeer.OWNER, TReportSubscribePeer.PERSON,
			TGridLayoutPeer.PERSON, TNavigatorLayoutPeer.PERSON, TMailTextBlockPeer.PERSON, TUserFeaturePeer.PERSON,
			// delete itself
			TPersonPeer.PKEY };

	private static Class[] replaceGroupPeerClasses = { TProjectPeer.class, TWorkflowActivityPeer.class, TWorkflowGuardPeer.class, TWorkItemPeer.class };

	private static String[] replaceGroupFields = { TProjectPeer.DEFOWNER, TWorkflowActivityPeer.NEWRESP, TWorkflowGuardPeer.PERSON, TWorkItemPeer.RESPONSIBLE };

	private static Class[] deleteGroupPeerClasses = { TAccessControlListPeer.class, TNotifyPeer.class, TGroupMemberPeer.class,
			// delete itself
			TPersonPeer.class

	};

	private static String[] deleteGroupFields = { TAccessControlListPeer.PERSONKEY, TNotifyPeer.PERSONKEY, TGroupMemberPeer.THEGROUP, TPersonPeer.PKEY };

	/**
	 * Loads a priorityBean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	public TPersonBean loadByPrimaryKey(Integer objectID) {
		TPerson tPerson = null;
		try {
			tPerson = retrieveByPK(objectID);
		} catch (Exception e) {
			LOGGER.debug("Loading the person by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
		}
		if (tPerson != null) {
			return tPerson.getBean();
		}
		return null;
	}

	/**
	 * Load all persons substituted by a person
	 * 
	 * @param substituteID
	 * @return
	 */
	public List<Integer> loadSubstitutedPersons(Integer substituteID) {
		List<Integer> substitutedPersons = new LinkedList<Integer>();
		Criteria crit = new Criteria();
		crit.add(SUBSTITUTEKEY, substituteID);
		crit.addSelectColumn(PKEY);
		List<Record> records = null;
		try {
			records = doSelectVillageRecords(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the persons substituted by " + substituteID + " failed with " + e.getMessage(), e);
			return Collections.emptyList();
		}
		if (records != null && !records.isEmpty()) {
			for (Record record : records) {
				Value value;
				try {
					value = record.getValue(1);
					if (value != null) {
						Integer substitutedPersonID = value.asIntegerObj();
						if (substitutedPersonID != null) {
							LOGGER.debug("Person " + substituteID + " is substitute for person " + substitutedPersonID);
							substitutedPersons.add(substitutedPersonID);
						}
					}
				} catch (DataSetException e) {
					LOGGER.error("Getting the persons substituted by " + substituteID + " failed with " + e.getMessage(), e);
					LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
				}
			}
		}
		return substitutedPersons;
	}

	/**
	 * Loads the number of persons in departments
	 * 
	 * @return
	 */
	public Map<Integer, Integer> loadNumberOfPersonsInDepartments(List<Integer> departmentIDs) {
		Map<Integer, Integer> numberOfPersonsInGroups = new HashMap<Integer, Integer>();
		if (departmentIDs != null && !departmentIDs.isEmpty()) {
			Criteria crit = new Criteria();
			String countPersons = "COUNT(" + PKEY + ")";
			crit.addSelectColumn(countPersons);
			crit.addSelectColumn(DEPKEY);
			crit.addGroupByColumn(DEPKEY);
			Criterion criterion = crit.getNewCriterion(DEPKEY, departmentIDs, Criteria.IN);
			crit.addHaving(criterion);
			List<Record> records = new LinkedList<Record>();
			try {
				records = doSelectVillageRecords(crit);
			} catch (Exception e) {
				LOGGER.error("Groupping the persons by departments " + departmentIDs + " failed with " + e.getMessage(), e);
				LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			}
			try {
				if (records != null && !records.isEmpty()) {
					for (Record record : records) {
						Integer numberOfPersons = record.getValue(1).asIntegerObj();
						Integer groupID = record.getValue(2).asIntegerObj();
						numberOfPersonsInGroups.put(groupID, numberOfPersons);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Getting the number of persons by departments " + departmentIDs + " failed with " + e.getMessage(), e);
				LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			}
		}
		return numberOfPersonsInGroups;
	}

	/**
	 * Gets the persons who need reminder e-mails
	 * 
	 * @return
	 */
	public List<TPersonBean> loadDailyReminderPersons(Date date) {
		Criteria crit = new Criteria();
		crit.add(DELETED, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		crit.add(PKEY, 0, Criteria.GREATER_THAN);// exclude workflow user
		crit.add(NOEMAILSPLEASE, TPersonBean.EMAIL.YES_EMAIL_PLEASE, Criteria.EQUAL);
		Criterion dateCriterion = crit.getNewCriterion(EMAILLASTREMINDED, date, Criteria.LESS_THAN);
		dateCriterion.or(crit.getNewCriterion(EMAILLASTREMINDED, null, Criteria.ISNULL));
		crit.and(dateCriterion);
		Criterion raciCriterion = crit.getNewCriterion(REMINDMEASORIGINATOR, BooleanFields.TRUE_VALUE, Criteria.EQUAL);
		raciCriterion.or(crit.getNewCriterion(REMINDMEASMANAGER, BooleanFields.TRUE_VALUE, Criteria.EQUAL));
		raciCriterion.or(crit.getNewCriterion(REMINDMEASRESPONSIBLE, BooleanFields.TRUE_VALUE, Criteria.EQUAL));
		crit.and(raciCriterion);
		LOGGER.debug(crit.toString());
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the reminder persons failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Gets the persons who need basket reminder e-mails
	 * 
	 * @return
	 */
	public List<TPersonBean> loadBasketReminderPersons(Date fromDate, Date toDate) {
		Criteria crit = new Criteria();
		crit.add(DELETED, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		crit.add(PKEY, 0, Criteria.GREATER_THAN);// exclude workflow user
		crit.add(NOEMAILSPLEASE, TPersonBean.EMAIL.YES_EMAIL_PLEASE, Criteria.EQUAL);
		crit.addJoin(PKEY, TPersonBasketPeer.PERSON);
		crit.add(TPersonBasketPeer.REMINDERDATE, fromDate, Criteria.GREATER_EQUAL);
		crit.add(TPersonBasketPeer.REMINDERDATE, toDate, Criteria.LESS_THAN);
		crit.addIn(TPersonBasketPeer.BASKET, new int[] { TBasketBean.BASKET_TYPES.CALENDAR, TBasketBean.BASKET_TYPES.DELEGATED });
		crit.setDistinct();
		LOGGER.debug(crit.toString());
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the reminder basket persons failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * @return List of Persons with the specific keys
	 * @throws Exception
	 */
	public List<TPersonBean> loadByKeys(List<Integer> personIDs) {
		List<TPersonBean> personList = new LinkedList<TPersonBean>();
		if (personIDs == null || personIDs.isEmpty()) {
			return personList;
		}
		Criteria crit = null;
		List<int[]> personChunkList = GeneralUtils.getListOfChunks(personIDs);
		Iterator<int[]> iterator = personChunkList.iterator();
		while (iterator.hasNext()) {
			int[] personIDChunk = iterator.next();
			crit = new Criteria();
			crit.addIn(PKEY, personIDChunk);
			crit.addAscendingOrderByColumn(ISGROUP);
			crit.addAscendingOrderByColumn(LASTNAME);
			crit.addAscendingOrderByColumn(FIRSTNAME);
			crit.addAscendingOrderByColumn(LOGINNAME);
		}
		try {
			personList.addAll(convertTorqueListToBeanList(doSelect(crit)));
		} catch (Exception e) {
			LOGGER.error("Loading the persons by keys " + personIDs + " failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
		}
		return personList;
	}

	/**
	 * Gets the personBeans by uuid list
	 * 
	 * @param uuids
	 * @return
	 */
	public List<TPersonBean> loadByUUIDs(List<String> uuids) {
		return loadByFieldValues(uuids, TPUUID);
	}

	/**
	 * Gets the person(s) with an email
	 * 
	 * @param email
	 * @return
	 */
	public List<TPersonBean> loadByEmail(String email) {
		if (email == null || email.length() < 1) {
			LOGGER.error("Faulty parameter email in loadByEmail");
		}
		Criteria crit = new Criteria();
		crit.add(EMAIL, (Object) email, Criteria.EQUAL);
		crit.getCriterion(EMAIL).setIgnoreCase(true);
		crit.add(ISGROUP, (Object) "Y", Criteria.NOT_EQUAL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading by email " + email + " failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Load the persons by a String field
	 * 
	 * @param fieldValues
	 * @param fieldName
	 * @return
	 */
	private List<TPersonBean> loadByFieldValues(List<String> fieldValues, String fieldName) {
		List<TPerson> personBeanList = new LinkedList<TPerson>();
		if (fieldValues == null || fieldValues.isEmpty()) {
			return new LinkedList<TPersonBean>();
		}
		Criteria criteria;
		List<List<String>> chunksList = GeneralUtils.getListOfStringChunks(fieldValues);
		if (chunksList == null) {
			return new LinkedList<TPersonBean>();
		}
		Iterator<List<String>> iterator = chunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			List<String> chunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(fieldName, chunk);
			try {
				personBeanList.addAll(doSelect(criteria));
			} catch (Exception e) {
				LOGGER.error(
						"Loading the personBeans by " + fieldName + " and the chunk number " + i + " of length  " + chunk.size() + " failed with "
								+ e.getMessage(), e);
				LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			}
		}
		return convertTorqueListToBeanList(personBeanList);
	}

	/**
	 * Gets a personBean from the TPerson table with userName
	 * 
	 * @param userName
	 * @return
	 */
	public TPersonBean loadByLoginName(String loginName) {
		List<TPerson> persons = null;
		if (loginName == null || loginName.length() == 0) {
			return null;
		}
		Criteria crit = new Criteria();
		crit.add(LOGINNAME, (Object) loginName, Criteria.EQUAL);
		crit.add(ISGROUP, (Object) "Y", Criteria.NOT_EQUAL);
		crit.getCriterion(LOGINNAME).setIgnoreCase(true);
		crit.getCriterion(ISGROUP).setIgnoreCase(true);
		try {
			persons = doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Loading by loginname " + loginName + " failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
		}
		if (persons != null && !persons.isEmpty()) {
			return ((TPerson) persons.get(0)).getBean();
		} else {
			return null;
		}
	}

	/**
	 * Gets a personBean from the TPerson table by groupname
	 * 
	 * @param groupName
	 * @return
	 */
	public TPersonBean loadGroupByName(String groupName) {
		List<TPerson> persons = new LinkedList<TPerson>();
		if (groupName == null || groupName.length() == 0) {
			return null;
		}
		Criteria crit = new Criteria();
		crit.add(LOGINNAME, (Object) groupName, Criteria.EQUAL);
		crit.add(ISGROUP, (Object) "Y", Criteria.EQUAL);
		crit.getCriterion(LOGINNAME).setIgnoreCase(true);
		crit.getCriterion(ISGROUP).setIgnoreCase(true);
		try {
			persons = doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Loading by groupName " + groupName + " failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
		}
		if (persons != null && !persons.isEmpty()) {
			return persons.get(0).getBean();
		}
		return null;
	}

	/**
	 * Whether a combination of lastname, firstname and email already exists
	 * 
	 * @param lastname
	 * @param firstname
	 * @param email
	 * @param personKey
	 * @return
	 */
	public boolean nameAndEmailExist(String lastname, String firstname, String email, Integer personKey) {
		Criteria crit = new Criteria();
		if (lastname != null) {
			crit.add(LASTNAME, lastname);
			crit.getCriterion(LASTNAME).setIgnoreCase(true);
		}
		if (firstname != null) {
			crit.add(FIRSTNAME, firstname);
			crit.getCriterion(FIRSTNAME).setIgnoreCase(true);
		}
		if (email != null) {
			crit.add(EMAIL, email);
			crit.getCriterion(EMAIL).setIgnoreCase(true);
		}
		crit.add(ISGROUP, (Object) "Y", Criteria.NOT_EQUAL);
		crit.getCriterion(ISGROUP).setIgnoreCase(true);
		if (personKey != null) {
			crit.add(PKEY, personKey, Criteria.NOT_EQUAL);
		}
		List<TPerson> persons = null;
		try {
			persons = doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Loading by lastname " + lastname + " firstname " + firstname + " email " + email + " personKey=" + personKey + " criteria=" + crit
					+ " failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
		}
		return persons != null && !persons.isEmpty();
	}

	/**
	 * Load a person by firstName and lastName
	 * 
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public TPersonBean loadByFirstNameLastName(String firstName, String lastName) {
		List<TPerson> torqueList = new ArrayList<TPerson>();
		Criteria crit = new Criteria();
		if (firstName == null) {
			crit.add(FIRSTNAME, (Object) null, Criteria.ISNULL);
		} else {
			crit.add(FIRSTNAME, firstName);
		}
		if (lastName == null) {
			crit.add(LASTNAME, (Object) null, Criteria.ISNULL);
		} else {
			crit.add(LASTNAME, lastName);
		}
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading the person with firstName " + firstName + " and lastName " + lastName + " failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
		}
		if (torqueList == null || torqueList.isEmpty()) {
			return null;
		} else {
			if (torqueList.size() > 1) {
				LOGGER.warn("More than one person with firstName " + firstName + " and lastName " + lastName);
			}
			return torqueList.get(0).getBean();
		}
	}

	/**
	 * Load all system administrators
	 */
	public List<TPersonBean> loadSystemAdmins() {
		Criteria crit = new Criteria();

		Criterion lessThan100 = crit.getNewCriterion(BaseTPersonPeer.PKEY, Integer.valueOf(100), Criteria.LESS_THAN);
		Criterion greatherThan0 = crit.getNewCriterion(BaseTPersonPeer.PKEY, Integer.valueOf(0), Criteria.GREATER_THAN);
		crit.add(lessThan100.and(greatherThan0));
		// for groups this field is null
		Criterion sysAdminLevel = crit.getNewCriterion(BaseTPersonPeer.USERLEVEL, TPersonBean.USERLEVEL.SYSADMIN, Criteria.EQUAL);
		crit.add(lessThan100.or(sysAdminLevel));
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the system admins failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Activate or deactivate the persons
	 * 
	 * @param persons
	 * @param deactivate
	 */
	public void activateDeactivatePersons(List<Integer> persons, boolean deactivate) {
		try {
			if (persons != null && !persons.isEmpty()) {
				Criteria selectCrit;
				Criteria updateCrit;
				List<int[]> personChunkList = GeneralUtils.getListOfChunks(persons);
				Iterator<int[]> iterator = personChunkList.iterator();
				while (iterator.hasNext()) {
					int[] personIDChunk = iterator.next();
					selectCrit = new Criteria();
					updateCrit = new Criteria();
					selectCrit.addIn(PKEY, personIDChunk);
					if (deactivate) {
						selectCrit.add(DELETED, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
						updateCrit.add(DELETED, BooleanFields.TRUE_VALUE);
					} else {
						selectCrit.add(DELETED, (Object) BooleanFields.FALSE_VALUE, Criteria.NOT_EQUAL);
						updateCrit.add(DELETED, BooleanFields.FALSE_VALUE);
						updateCrit.add(TOKENEXPDATE, null);
						updateCrit.add(TOKENPASSWD, null);
					}
					doUpdate(selectCrit, updateCrit);
				}
			}
		} catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			LOGGER.error("Activating/deactivating " + deactivate + " the persons " + persons.size() + " failed with " + e);
		}
	}

	/**
	 * Set the user level for persons
	 * 
	 * @param persons
	 * @param deactivate
	 */
	public void setUserLevelPersons(List<Integer> persons, Integer userLevel) {
		try {
			if (persons != null && !persons.isEmpty()) {
				Criteria selectCrit;
				Criteria updateCrit;
				List<int[]> personChunkList = GeneralUtils.getListOfChunks(persons);
				Iterator<int[]> iterator = personChunkList.iterator();
				while (iterator.hasNext()) {
					int[] personIDChunk = iterator.next();
					selectCrit = new Criteria();
					updateCrit = new Criteria();
					selectCrit.addIn(PKEY, personIDChunk);
					updateCrit.add(USERLEVEL, userLevel);
					doUpdate(selectCrit, updateCrit);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Setting the user level " + userLevel + " the persons " + persons.size() + "  failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
		}
	}

	/**
	 * Loads all persons
	 * 
	 * @return
	 */
	public List<TPersonBean> loadPersons() {
		Criteria crit = new Criteria();
		crit.add(PKEY, 0, Criteria.GREATER_THAN);// exclude workflow user
		crit.add(ISGROUP, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		crit.addAscendingOrderByColumn(BaseTPersonPeer.LASTNAME);
		crit.addAscendingOrderByColumn(BaseTPersonPeer.FIRSTNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading all persons failed with " + e, e);
			return null;
		}
	}

	/**
	 * Loads all clients
	 * 
	 * @return
	 */
	public List<TPersonBean> loadAllClients() {
		Criteria crit = new Criteria();
		crit.add(PKEY, 0, Criteria.GREATER_THAN);// exclude workflow user
		crit.add(ISGROUP, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		crit.add(USERLEVEL, TPersonBean.USERLEVEL.CLIENT, Criteria.EQUAL);
		crit.addAscendingOrderByColumn(BaseTPersonPeer.LASTNAME);
		crit.addAscendingOrderByColumn(BaseTPersonPeer.FIRSTNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading all persons failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Loads all users except client
	 * 
	 * @return
	 */
	public List<TPersonBean> loadAllUsers() {
		Criteria crit = new Criteria();
		crit.add(PKEY, 0, Criteria.GREATER_THAN);// exclude workflow user
		crit.add(ISGROUP, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		//crit.add(USERLEVEL, TPersonBean.USERLEVEL.CLIENT, Criteria.NOT_EQUAL);
		Criterion criterionNoUserLevel = crit.getNewCriterion(USERLEVEL, (Object)null, Criteria.ISNULL);
		Criterion criterionNoClient = crit.getNewCriterion(USERLEVEL, TPersonBean.USERLEVEL.CLIENT, Criteria.NOT_EQUAL);
		crit.add(criterionNoUserLevel.or(criterionNoClient));
		crit.addAscendingOrderByColumn(BaseTPersonPeer.LASTNAME);
		crit.addAscendingOrderByColumn(BaseTPersonPeer.FIRSTNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading all persons failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Loads the active persons
	 * 
	 * @param actualValue
	 * @return
	 */
	public List<TPersonBean> loadActivePersons() {
		Criteria crit = new Criteria();
		crit.add(DELETED, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		crit.add(ISGROUP, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		crit.add(PKEY, 0, Criteria.GREATER_THAN);// exclude workflow user

		crit.addAscendingOrderByColumn(BaseTPersonPeer.LASTNAME);
		crit.addAscendingOrderByColumn(BaseTPersonPeer.FIRSTNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the active persons failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * 
	 * @return List of active persons and groups
	 */
	public List<TPersonBean> loadActivePersonsAndGroups() {
		Criteria crit = new Criteria();

		crit.add(DELETED, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		crit.add(PKEY, 0, Criteria.GREATER_THAN);// exclude workflow user
		crit.addAscendingOrderByColumn(BaseTPersonPeer.ISGROUP);
		crit.addAscendingOrderByColumn(BaseTPersonPeer.LASTNAME);
		crit.addAscendingOrderByColumn(BaseTPersonPeer.FIRSTNAME);
		crit.addAscendingOrderByColumn(BaseTPersonPeer.LOGINNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the active persons and groups failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Load all groups
	 * 
	 * @return
	 */
	public List<TPersonBean> loadGroups() {
		Criteria crit = new Criteria();
		crit.add(ISGROUP, "Y");
		crit.addAscendingOrderByColumn(LOGINNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading all groups failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Loads all persons and groups
	 * 
	 * @return
	 */
	public List<TPersonBean> loadPersonsAndGroups() {
		Criteria crit = new Criteria();

		crit.add(PKEY, 0, Criteria.GREATER_THAN);// exclude workflow user
		crit.addAscendingOrderByColumn(ISGROUP);
		crit.addAscendingOrderByColumn(LASTNAME);
		crit.addAscendingOrderByColumn(FIRSTNAME);
		crit.addAscendingOrderByColumn(LOGINNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading all persons and groups failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Loads the persons with any of the specified roles in a project
	 * 
	 * @param projectIDs
	 * @param roleIDs
	 * @return
	 */
	public List<TPersonBean> loadByProjectAndRoles(List<Integer> projectIDs, List<Integer> roleIDs) {
		if (roleIDs == null || roleIDs.isEmpty()) {
			return new ArrayList<TPersonBean>();
		}
		Criteria crit = new Criteria();
		crit.addJoin(TAccessControlListPeer.PERSONKEY, PKEY);
		crit.addIn(TAccessControlListPeer.PROJKEY, projectIDs);
		crit.addIn(TAccessControlListPeer.ROLEKEY, roleIDs);
		crit.addAscendingOrderByColumn(ISGROUP);
		crit.addAscendingOrderByColumn(LASTNAME);
		crit.addAscendingOrderByColumn(FIRSTNAME);
		crit.addAscendingOrderByColumn(LOGINNAME);
		crit.setDistinct();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Getting the persons by project " + projectIDs + " and role + " + roleIDs.size() + " failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return new ArrayList<TPersonBean>();
		}
	}

	/**
	 * Returns the real persons which are directly present in the list The list
	 * may contain both personID-s and groupID-s
	 * 
	 * @param personIDs
	 * @param excludeInactive
	 * @param currentOption
	 * @return
	 */
	public List<TPersonBean> getDirectPersons(List<Integer> personIDs, boolean excludeInactive, Integer currentOption) {
		List<TPersonBean> personBeans = new LinkedList<TPersonBean>();
		if (personIDs == null || personIDs.isEmpty()) {
			return personBeans;
		}
		Criteria crit;
		List<int[]> personChunks = GeneralUtils.getListOfChunks(personIDs);
		if (personChunks != null) {
			for (int[] personChunk : personChunks) {
				crit = new Criteria();
				crit.addIn(PKEY, personChunk);
				// get only the persons
				crit.add(ISGROUP, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
				crit.addAscendingOrderByColumn(LASTNAME);
				crit.addAscendingOrderByColumn(FIRSTNAME);
				addExcludeCurrent(crit, excludeInactive, currentOption);
				try {
					personBeans.addAll(convertTorqueListToBeanList(doSelect(crit)));
				} catch (Exception e) {
					LOGGER.error("Loading the direct persons failed with " + e, e);
					LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
				}
			}
		}
		return personBeans;
	}

	/**
	 * Returns the real persons which are indirectly (through group) present in
	 * the list The list may contain both personID-s and groupID-s
	 * 
	 * @param personIDs
	 * @param excludeInactive
	 * @param currentOption
	 * @return
	 */
	public List<TPersonBean> getIndirectPersons(List<Integer> personIDs, boolean excludeInactive, Integer currentOption) {
		List<TPersonBean> personBeans = new LinkedList<TPersonBean>();
		if (personIDs == null || personIDs.isEmpty()) {
			return personBeans;
		}
		Criteria crit;
		List<int[]> personChunks = GeneralUtils.getListOfChunks(personIDs);
		if (personChunks != null) {
			for (int[] personChunk : personChunks) {
				crit = new Criteria();
				crit.addJoin(TGroupMemberPeer.PERSON, PKEY);
				// get only the persons through group
				crit.addIn(TGroupMemberPeer.THEGROUP, personChunk);
				crit.setDistinct();
				addExcludeCurrent(crit, excludeInactive, currentOption);
				try {
					personBeans.addAll(convertTorqueListToBeanList(doSelect(crit)));
				} catch (Exception e) {
					LOGGER.error("Loading the indirect persons failed with " + e, e);
					LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
				}
			}
		}
		return personBeans;
	}

	private static void addExcludeCurrent(Criteria criteria, boolean excludeInactive, Integer currentOption) {
		if (excludeInactive && currentOption != null) {
			Criterion criterionInactive = criteria.getNewCriterion(TPersonPeer.DELETED, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
			Criterion criterionCurrentOption = criteria.getNewCriterion(TPersonPeer.PKEY, currentOption, Criteria.EQUAL);
			criteria.add(criterionInactive.or(criterionCurrentOption));
		} else {
			if (excludeInactive) {
				criteria.add(TPersonPeer.DELETED, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
			}
		}
	}

	/**
	 * Returns the real groups which are directly present in the list The list
	 * may contain both personID-s and groupID-s
	 * 
	 * @param personIDs
	 * @param excludeInactive
	 * @return
	 */
	public List<TPersonBean> getDirectGroups(List<Integer> personIDs, boolean excludeInactive) {
		List<TPersonBean> personBeans = new LinkedList<TPersonBean>();
		if (personIDs == null || personIDs.isEmpty()) {
			return personBeans;
		}
		Criteria crit;
		List<int[]> personChunks = GeneralUtils.getListOfChunks(personIDs);
		if (personChunks != null) {
			for (int[] personChunk : personChunks) {
				crit = new Criteria();
				crit.addIn(PKEY, personChunk);
				// get only the groups
				crit.add(ISGROUP, BooleanFields.TRUE_VALUE);
				crit.addAscendingOrderByColumn(BaseTPersonPeer.LOGINNAME);
				if (excludeInactive) {
					crit.add(BaseTPersonPeer.DELETED, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
				}
				try {
					personBeans.addAll(convertTorqueListToBeanList(doSelect(crit)));
				} catch (Exception e) {
					LOGGER.error("Loading the direct groups failed with " + e, e);
					LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
				}
			}

		}
		return personBeans;
	}

	public Integer countUsers(boolean disabled, boolean limited) {
		List<TPerson> persons = null;
		Integer count = new Integer(0);
		Criteria crit = new Criteria();
		crit.add(ISGROUP, (Object) "Y", Criteria.NOT_EQUAL);
		crit.add(PKEY, 0, Criteria.GREATER_THAN);// exclude workflow user
		if (disabled) {
			crit.add(DELETED, (Object) BooleanFields.TRUE_VALUE, Criteria.EQUAL);
			crit.getCriterion(DELETED).setIgnoreCase(true);
		} else {
			crit.add(DELETED, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
			crit.getCriterion(DELETED).setIgnoreCase(true);
		}
		if (limited) {
			crit.add(USERLEVEL, TPersonBean.USERLEVEL.CLIENT, Criteria.EQUAL);
		} else {
			crit.add(USERLEVEL, TPersonBean.USERLEVEL.CLIENT, Criteria.NOT_EQUAL);
			Criterion critor = crit.getCriterion(USERLEVEL);
			critor.or(crit.getNewCriterion(USERLEVEL, (Object) null, Criteria.ISNULL));
		}
		try {
			persons = doSelect(crit);
			if (persons != null) {
				count = Integer.valueOf(persons.size());
			}
		} catch (Exception e) {
			LOGGER.error("counting active/inactive persons " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
		}
		return count;
	}

	/**
	 * Count the user by user levels
	 * 
	 * @param userLevels
	 * @param disabled
	 * @return
	 */
	public int countByUserLevels(List<Integer> userLevels, boolean disabled) {
		if (userLevels == null || userLevels.isEmpty()) {
			return 0;
		}
		String COUNT = "count(" + PKEY + ")";
		Criteria crit = new Criteria();
		crit.add(PKEY, 0, Criteria.GREATER_THAN);// exclude workflow user
		if (disabled) {
			crit.add(DELETED, (Object) "Y", Criteria.EQUAL);
			crit.getCriterion(DELETED).setIgnoreCase(true);
		} else {
			crit.add(DELETED, (Object) "Y", Criteria.NOT_EQUAL);
			crit.getCriterion(DELETED).setIgnoreCase(true);
		}
		crit.addSelectColumn(COUNT);
		crit.addIn(USERLEVEL, userLevels);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
		} catch (Exception e) {
			LOGGER.error("Count the number of users by user level field with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
		}
		return 0;
	}

	/**
	 * Gets the logged in users
	 * 
	 * @return
	 */
	public List<TPersonBean> getLoggedInUsers() {
		Criteria crit = new Criteria();
		crit.addJoin(PKEY, TLoggedInUsersPeer.LOGGEDUSER);
		crit.addAscendingOrderByColumn(LASTNAME);
		crit.addAscendingOrderByColumn(FIRSTNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Getting the logged in persons failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Saves a personBean in the TPerson table
	 * 
	 * @param personBean
	 * @return
	 */
	public Integer save(TPersonBean personBean) {
		try {
			TPerson tPerson = BaseTPerson.createTPerson(personBean);
			tPerson.save();
			return tPerson.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a person failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	public boolean hasDependentPersonData(List<Integer> personIDs) {
		return ReflectionHelper.hasDependentData(replacePersonPeerClasses, replacePersonFields, personIDs);
	}

	/**
	 * The reflection does not work because an additional condition should be
	 * satisfied (no direct foreign key relationship exists)
	 * 
	 * @param oldPersonID
	 * @param newPersonID
	 */
	/*public boolean hasDependencyInUserPicker(List<Integer> personIDs) {
		if (personIDs == null || personIDs.isEmpty()) {
			return false;
		}
		List<TPerson> persons = null;
		Criteria selectCriteria;
		List<int[]> personChunkList = GeneralUtils.getListOfChunks(personIDs);
		Iterator<int[]> iterator = personChunkList.iterator();
		while (iterator.hasNext()) {
			int[] personIDChunk = iterator.next();
			selectCriteria = new Criteria();
			selectCriteria.addIn(TAttributeValuePeer.SYSTEMOPTIONID, personIDChunk);
			selectCriteria.add(TAttributeValuePeer.SYSTEMOPTIONTYPE, SystemFields.PERSON);
			selectCriteria.setDistinct();
			try {
				persons = doSelect(selectCriteria);
			} catch (Exception e) {
				LOGGER.error("Verifiying the dependent " + "oldPersonIDs " + personIDs.size() + " for the user picker failed with " + e.getMessage(), e);
			}
			if (persons != null && !persons.isEmpty()) {
				return true;
			}
		}
		return false;
	}*/

	/**
	 * The reflection does not work because an additional condition should be
	 * satisfied (no direct foreign key relationship exists)
	 * 
	 * @param oldPersonID
	 * @param newPersonID
	 */
	/*public boolean hasDependencyInHistory(List<Integer> personIDs, boolean newValues) {
		if (personIDs == null || personIDs.isEmpty()) {
			return false;
		}
		List<TPerson> persons = null;
		Criteria selectCriteria;
		List<int[]> personChunkList = GeneralUtils.getListOfChunks(personIDs);
		Iterator<int[]> iterator = personChunkList.iterator();
		while (iterator.hasNext()) {
			int[] personIDChunk = iterator.next();
			selectCriteria = new Criteria();
			if (newValues) {
				selectCriteria.addIn(TFieldChangePeer.NEWSYSTEMOPTIONID, personIDChunk);
			} else {
				selectCriteria.addIn(TFieldChangePeer.OLDSYSTEMOPTIONID, personIDChunk);
			}
			selectCriteria.add(TFieldChangePeer.SYSTEMOPTIONTYPE, SystemFields.INTEGER_PERSON);
			//selectCriteria.setDistinct();
			try {
				persons = doSelect(selectCriteria);
			} catch (Exception e) {
				LOGGER.error("Verifiying the dependent " + "oldPersonIDs " + personIDs.size() + " for the user picker failed with " + e.getMessage(), e);
			}
			if (persons != null && !persons.isEmpty()) {
				return true;
			}
		}
		return false;
	}*/

	/**
	 * Delete a person without dependences
	 * 
	 * @param personID
	 */
	public void deletePerson(Integer personID) {
		new TCardFieldOptionPeer().deleteOptionForField(SystemFields.INTEGER_MANAGER, personID);
		new TCardFieldOptionPeer().deleteOptionForField(SystemFields.INTEGER_RESPONSIBLE, personID);
		List<Integer> userPickerFieldIDs = FieldBL.getUserPickerFieldIDs();
		if (userPickerFieldIDs != null) {
			for (Integer userPickerFieldID : userPickerFieldIDs) {
				new TCardFieldOptionPeer().deleteOptionForField(userPickerFieldID, personID);
			}
		}
		ReflectionHelper.delete(deletePersonPeerClasses, deletePersonFields, personID);
	}

	/**
	 * Replaces the dependences with a new personID and deletes the old personID
	 * from the TPerson table
	 * 
	 * @param oldPersonID
	 * @param newPersonID
	 */
	public void replacePerson(Integer oldPersonID, Integer newPersonID) {
		ReflectionHelper.replace(replacePersonPeerClasses, replacePersonFields, oldPersonID, newPersonID);
	}

	/**
	 * The reflection does not work because an additional condition should be
	 * satisfied (no direct foreign key relationship exists)
	 * 
	 * @param oldPersonID
	 * @param newPersonID
	 */
	public void replaceUserPicker(Integer oldPersonID, Integer newPersonID) {
		Criteria selectCriteria = new Criteria();
		Criteria updateCriteria = new Criteria();
		selectCriteria.add(BaseTAttributeValuePeer.SYSTEMOPTIONID, oldPersonID);
		selectCriteria.add(BaseTAttributeValuePeer.SYSTEMOPTIONTYPE, SystemFields.PERSON);
		updateCriteria.add(BaseTAttributeValuePeer.SYSTEMOPTIONID, newPersonID);
		try {
			doUpdate(selectCriteria, updateCriteria);
		} catch (Exception e) {
			LOGGER.error("Trying to replace " + "oldPersonID " + oldPersonID + " with " + "newPersonID  " + newPersonID + " for the user picker failed with "
					+ e.getMessage(), e);
		}
	}

	/**
	 * The reflection does not work because an additional condition should be
	 * satisfied (no direct foreign key relationship exists)
	 * 
	 * @param oldPersonID
	 * @param newPersonID
	 * @param newValues
	 */
	public void replaceHistoryPerson(Integer oldPersonID, Integer newPersonID, boolean newValues) {
		Criteria selectCriteria = new Criteria();
		Criteria updateCriteria = new Criteria();
		if (newValues) {
			selectCriteria.add(TFieldChangePeer.NEWSYSTEMOPTIONID, oldPersonID);
			updateCriteria.add(TFieldChangePeer.NEWSYSTEMOPTIONID, newPersonID);
		} else {
			selectCriteria.add(TFieldChangePeer.OLDSYSTEMOPTIONID, oldPersonID);
			updateCriteria.add(TFieldChangePeer.OLDSYSTEMOPTIONID, newPersonID);
		}
		selectCriteria.add(TFieldChangePeer.SYSTEMOPTIONTYPE, SystemFields.PERSON);
		try {
			doUpdate(selectCriteria, updateCriteria);
		} catch (Exception e) {
			LOGGER.error("Trying to replace " + "oldPersonID " + oldPersonID + " with " + "newPersonID  " + newPersonID + " in the history persons "
					+ newValues + " values failed with " + e.getMessage(), e);
		}
	}

	public void deleteGroup(Integer groupID) {
		new TCardFieldOptionPeer().deleteOptionForField(SystemFields.INTEGER_RESPONSIBLE, groupID);
		List<Integer> userPickerFieldIDs = FieldBL.getUserPickerFieldIDs();
		if (userPickerFieldIDs != null) {
			for (Integer userPickerFieldID : userPickerFieldIDs) {
				new TCardFieldOptionPeer().deleteOptionForField(userPickerFieldID, groupID);
			}
		}
		ReflectionHelper.delete(deleteGroupPeerClasses, deleteGroupFields, groupID);
	}

	public boolean hasDependentGroupData(List<Integer> groupIDs) {
		return ReflectionHelper.hasDependentData(replaceGroupPeerClasses, replaceGroupFields, groupIDs);
	}

	public void replaceGroup(Integer oldPerson, Integer newPerson) {
		ReflectionHelper.replace(replaceGroupPeerClasses, replaceGroupFields, oldPerson, newPerson);
	}

	/**
	 * Gets a list of personBeans ordered by isGroup, lastname, firstname,
	 * loginname
	 * 
	 * @param personIDs
	 * @return
	 */
	public List<TPersonBean> loadSortedPersonsOrGroups(List<Integer> personIDs) {
		List<TPersonBean> list = new LinkedList<TPersonBean>();
		if (personIDs == null || personIDs.isEmpty()) {
			return list;
		}
		List<int[]> personIDChunksList = GeneralUtils.getListOfChunks(personIDs);
		if (personIDChunksList == null) {
			return new LinkedList<TPersonBean>();
		}
		for (int[] personIDChunk : personIDChunksList) {
			Criteria crit = new Criteria();
			crit.addIn(PKEY, personIDChunk);
			crit.addAscendingOrderByColumn(ISGROUP);
			crit.addAscendingOrderByColumn(LASTNAME);
			crit.addAscendingOrderByColumn(FIRSTNAME);
			crit.addAscendingOrderByColumn(LOGINNAME);
			try {
				list.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (Exception e) {
				LOGGER.error("Loading the persons by keys " + personIDs + " sorted by person names and group names failed with " + e, e);
			}
		}
		return list;
	}

	/**
	 * Get the persons who have at least one work in a project or release and
	 * listType
	 * 
	 * @param entityID
	 * @param entityType
	 * @param listType
	 * @return
	 */
	public List<TPersonBean> getPersonsWithWorkInProject(Integer entityID, int entityType, Integer listType) {
		Criteria criteria = new Criteria();
		criteria.addJoin(PKEY, TCostPeer.PERSON);
		criteria.addJoin(TCostPeer.WORKITEM, TWorkItemPeer.WORKITEMKEY);
		if (listType != null) {
			criteria.add(TWorkItemPeer.CATEGORYKEY, listType);
		}
		switch (entityType) {
		case SystemFields.RELEASESCHEDULED:
			criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, entityID);
			break;
		case SystemFields.PROJECT:
			criteria.add(TWorkItemPeer.PROJECTKEY, entityID);
			break;
		default:
			return null;
		}
		criteria.setDistinct();
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Getting the persons woth work in a project/release " + entityID + " " + entityType + " failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Get the persons with informant or consultant role for a workItemKey
	 * 
	 * @param workItemID
	 * @return
	 */
	public List<TPersonBean> loadNotifyThroughRaci(Integer workItemID) {
		if (workItemID == null) {
			return new LinkedList<TPersonBean>();
		}
		Criteria crit = new Criteria();
		crit.addJoin(PKEY, TNotifyPeer.PERSONKEY);
		crit.add(TNotifyPeer.WORKITEM, workItemID);
		crit.add(TNotifyPeer.RACIROLE, (Object) null, Criteria.ISNOTNULL);
		// for persons this field is either 0 or 1
		Criterion criterionNotOne = crit.getNewCriterion(NOEMAILSPLEASE, new Integer(1), Criteria.NOT_EQUAL);
		// for groups this field is null
		Criterion criterionNull = crit.getNewCriterion(NOEMAILSPLEASE, null, Criteria.ISNULL);
		crit.add(criterionNotOne.or(criterionNull));
		// when a person is both consultant and informant
		crit.setDistinct();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the raci role persons for notification for item " + workItemID + " failed  with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Load the persons belonging to a group
	 * 
	 * @param groupKey
	 * @return
	 */
	public List<TPersonBean> loadPersonsForGroup(Integer groupKey) {
		Criteria crit = new Criteria();
		crit.addJoin(BaseTGroupMemberPeer.PERSON, BaseTPersonPeer.PKEY);
		crit.add(BaseTGroupMemberPeer.THEGROUP, groupKey);
		crit.addAscendingOrderByColumn(LASTNAME);
		crit.addAscendingOrderByColumn(FIRSTNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the persons by group " + groupKey + " failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Load the groups a person belongings to
	 * 
	 * @param personID
	 * @return
	 */
	public List<TPersonBean> loadGroupsForPerson(Integer personID) {
		Criteria crit = new Criteria();
		crit.addJoin(TGroupMemberPeer.THEGROUP, TPersonPeer.PKEY);
		crit.add(TGroupMemberPeer.PERSON, personID);
		crit.addAscendingOrderByColumn(LOGINNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the groups the person " + personID + " belongs to failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Load the groups any person from personIDs belongs to
	 * 
	 * @param personIDs
	 * @return
	 */
	public List<TPersonBean> loadGroupsForPersons(List<Integer> personIDs) {
		Criteria crit = new Criteria();
		crit.addJoin(TGroupMemberPeer.THEGROUP, TPersonPeer.PKEY);
		crit.addIn(TGroupMemberPeer.PERSON, personIDs);
		crit.addAscendingOrderByColumn(LOGINNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the groups the persons " + personIDs.size() + " belongs to failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Load the persons from a department
	 * 
	 * @param departmentID
	 * @return
	 */
	public List<TPersonBean> loadPersonsForDepartment(Integer departmentID) {
		Criteria crit = new Criteria();
		crit.add(BaseTPersonPeer.DEPKEY, departmentID);
		crit.addAscendingOrderByColumn(LASTNAME);
		crit.addAscendingOrderByColumn(FIRSTNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the persons by department " + departmentID + " failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Load the persons belonging to any group from an array of groups
	 * 
	 * @param groupKeys
	 * @return
	 */
	public List<TPersonBean> loadPersonsForGroups(List<Integer> groupKeys) {
		Criteria crit = new Criteria();
		crit.addJoin(BaseTGroupMemberPeer.PERSON, BaseTPersonPeer.PKEY);
		crit.addIn(BaseTGroupMemberPeer.THEGROUP, groupKeys);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the persons by groups " + groupKeys + " failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	public List<TPersonBean> getDirectRaciPersons(Integer workItemKey, boolean group, String raciRole) {
		if (workItemKey == null) {
			return new LinkedList<TPersonBean>();
		}
		Criteria crit = new Criteria();
		crit.addJoin(BaseTPersonPeer.PKEY, BaseTNotifyPeer.PERSONKEY);
		crit.add(BaseTNotifyPeer.WORKITEM, workItemKey);
		crit.add(BaseTNotifyPeer.RACIROLE, raciRole);
		if (group) {
			crit.add(BaseTPersonPeer.ISGROUP, BooleanFields.TRUE_VALUE);
			crit.addAscendingOrderByColumn(BaseTPersonPeer.LOGINNAME);
		} else {
			crit.add(BaseTPersonPeer.ISGROUP, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
			crit.addAscendingOrderByColumn(BaseTPersonPeer.LASTNAME);
			crit.addAscendingOrderByColumn(BaseTPersonPeer.FIRSTNAME);
		}
		try {
			return convertTorqueListToBeanList(BaseTPersonPeer.doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error(
					"Loading the raci role persons/groups for item " + workItemKey + " group " + group + " raci role " + raciRole + " failed  with "
							+ e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Returns the direct informant groups (no persons)
	 * 
	 * @param workItemKey
	 * @return
	 */
	public List<TPersonBean> getDirectInformants(Integer workItemKey) {
		return getDirectRaci(workItemKey, RaciRole.INFORMANT);
	}

	/**
	 * Returns the direct consultant groups (no persons)
	 * 
	 * @param workItemKey
	 * @return
	 */
	public List<TPersonBean> getDirectConsultants(Integer workItemKey) {
		return getDirectRaci(workItemKey, RaciRole.CONSULTANT);
	}

	private List<TPersonBean> getDirectRaci(Integer workItemKey, String raciRole) {
		if (workItemKey == null) {
			return new LinkedList<TPersonBean>();
		}
		Criteria crit = new Criteria();
		crit.addJoin(TPersonPeer.PKEY, TNotifyPeer.PERSONKEY);
		crit.add(TNotifyPeer.WORKITEM, workItemKey);
		crit.add(TNotifyPeer.RACIROLE, raciRole);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the raci role persons/groups for item " + workItemKey + " raci role " + raciRole + " failed  with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Load the distinct persons who have added cost/effort to a workItem
	 * 
	 * @param workItemKey
	 * @return
	 */
	public List<TPersonBean> loadPersonsWithEffort(Integer workItemKey) {
		Criteria crit = new Criteria();
		crit.addJoin(PKEY, TCostPeer.PERSON);
		crit.add(TCostPeer.WORKITEM, workItemKey);
		crit.setDistinct();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the users which have cost/effort by workItemKey " + workItemKey + " failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			return null;
		}
	}

	/**
	 * Include into person list only if has RACI role also in public issues (or
	 * he is the logged in person)
	 * 
	 * @param crit
	 * @param personID
	 * @param raciField
	 * @return
	 */
	private Criteria addAccessLevel(Criteria crit, Integer personID, String raciField) {
		Criterion accessLevelPublicCriterion = crit.getNewCriterion(BaseTWorkItemPeer.ACCESSLEVEL, TWorkItemBean.ACCESS_LEVEL_PRIVATE, Criteria.NOT_EQUAL);
		Criterion accessLevelNullCriterion = crit.getNewCriterion(BaseTWorkItemPeer.ACCESSLEVEL, (Object) null, Criteria.ISNULL);
		Criterion raciCriterion = crit.getNewCriterion(raciField, personID, Criteria.EQUAL);
		crit.add(accessLevelPublicCriterion.or(accessLevelNullCriterion.or(raciCriterion)));
		CriteriaUtil.addArchivedDeletedFilter(crit);
		return crit;
	}

	/**
	 * Load the persons which are managers for at least one workItem in any of
	 * the projects
	 * 
	 * @param projects
	 * @return
	 */
	public List<TPersonBean> loadUsedManagersByProjects(Integer person, Integer[] projects) {
		List<TPersonBean> personList = new ArrayList<TPersonBean>();
		if (projects == null || projects.length == 0) {
			return new ArrayList<TPersonBean>();
		}
		List<int[]> projectChunkList = GeneralUtils.getListOfChunks(projects);
		Iterator<int[]> iterator = projectChunkList.iterator();
		while (iterator.hasNext()) {
			int[] projectIDChunk = (int[]) iterator.next();
			Criteria crit = new Criteria();
			crit.addJoin(PKEY, TWorkItemPeer.OWNER);
			crit.addIn(TWorkItemPeer.PROJECTKEY, projectIDChunk);
			addAccessLevel(crit, person, BaseTWorkItemPeer.OWNER);
			crit.addAscendingOrderByColumn(LASTNAME);
			crit.addAscendingOrderByColumn(FIRSTNAME);
			crit.setDistinct();
			try {
				personList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (Exception e) {
				LOGGER.error("Loading the used managers for projects " + projects + " failed with " + e, e);
				LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
			}
		}
		return personList;
	}

	/**
	 * Load the persons/groups which are responsibles for at least one workItem
	 * in any of the projects
	 * 
	 * @param projects
	 * @return
	 */
	public List<TPersonBean> loadUsedResonsiblesByProjects(Integer person, Integer[] projects) {
		List<TPersonBean> personList = new ArrayList<TPersonBean>();
		if (projects == null || projects.length == 0) {
			return new ArrayList<TPersonBean>();
		}
		List<int[]> projectChunkList = GeneralUtils.getListOfChunks(projects);
		Iterator<int[]> iterator = projectChunkList.iterator();
		while (iterator.hasNext()) {
			int[] projectIDChunk = (int[]) iterator.next();
			Criteria crit = new Criteria();
			crit.addJoin(PKEY, TWorkItemPeer.RESPONSIBLE);
			crit.addIn(TWorkItemPeer.PROJECTKEY, projectIDChunk);
			addAccessLevel(crit, person, BaseTWorkItemPeer.RESPONSIBLE);
			crit.addAscendingOrderByColumn(ISGROUP);
			crit.addAscendingOrderByColumn(LASTNAME);
			crit.addAscendingOrderByColumn(FIRSTNAME);
			crit.addAscendingOrderByColumn(LOGINNAME);
			crit.setDistinct();
			try {
				personList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (Exception e) {
				LOGGER.error("Loading the used reponsibles for projects " + projects + " failed with " + e, e);
			}
		}
		return personList;
	}

	/**
	 * Load the persons which are originators for at least one workItem in any
	 * of the projects
	 * 
	 * @param projects
	 * @return
	 */
	public List<TPersonBean> loadUsedOriginatorsByProjects(Integer person, Integer[] projects) {
		List<TPersonBean> personList = new ArrayList<TPersonBean>();
		if (projects == null || projects.length == 0) {
			return new ArrayList<TPersonBean>();
		}
		List<int[]> projectChunkList = GeneralUtils.getListOfChunks(projects);
		Iterator<int[]> iterator = projectChunkList.iterator();
		while (iterator.hasNext()) {
			int[] projectIDChunk = (int[]) iterator.next();
			Criteria crit = new Criteria();
			crit.addJoin(PKEY, TWorkItemPeer.ORIGINATOR);
			crit.addIn(TWorkItemPeer.PROJECTKEY, projectIDChunk);
			addAccessLevel(crit, person, BaseTWorkItemPeer.ORIGINATOR);
			crit.addAscendingOrderByColumn(LASTNAME);
			crit.addAscendingOrderByColumn(FIRSTNAME);
			crit.setDistinct();
			try {
				personList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (Exception e) {
				LOGGER.error("Loading the used originators for projects " + projects + " failed with " + e, e);
				return new ArrayList<TPersonBean>();
			}
		}
		return personList;
	}

	/**
	 * Load the persons which are last edited persons for at least one workItem
	 * in any of the projects
	 * 
	 * @param projects
	 * @return
	 */
	public List<TPersonBean> loadUsedLastEditedByProjects(Integer[] projects) {
		List<TPersonBean> personList = new ArrayList<TPersonBean>();
		if (projects == null || projects.length == 0) {
			return new ArrayList<TPersonBean>();
		}
		List<int[]> projectChunkList = GeneralUtils.getListOfChunks(projects);
		Iterator<int[]> iterator = projectChunkList.iterator();
		while (iterator.hasNext()) {
			int[] projectIDChunk = (int[]) iterator.next();
			Criteria crit = new Criteria();
			crit.addJoin(PKEY, TWorkItemPeer.CHANGEDBY);
			crit.addIn(TWorkItemPeer.PROJECTKEY, projectIDChunk);
			crit.addAscendingOrderByColumn(LASTNAME);
			crit.addAscendingOrderByColumn(FIRSTNAME);
			crit.setDistinct();
			try {
				personList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (Exception e) {
				LOGGER.error("Loading the used last edited persons for projects " + projectIDChunk + " failed with " + e, e);
			}
		}
		return personList;
	}

	/**
	 * Load the persons/groups which are consultants/informants 
	 * for at least one workItem in any of the projects   
	 * @param projects 
	 * @param raciRole
	 * @return
	 */
	public List<TPersonBean> loadUsedConsultantsInformantsByProjects(Integer[] projects, String raciRole) {
		List<TPersonBean> personList = new ArrayList<TPersonBean>();
		if (projects == null || projects.length == 0) {
			return new ArrayList<TPersonBean>();
		}
		List<int[]> projectChunkList = GeneralUtils.getListOfChunks(projects);
		Iterator<int[]> iterator = projectChunkList.iterator();
		while (iterator.hasNext()) {
			int[] projectIDChunk = (int[]) iterator.next();
			Criteria crit = new Criteria();
			crit.addJoin(PKEY, TNotifyPeer.PERSONKEY);
			crit.addJoin(TNotifyPeer.WORKITEM, TWorkItemPeer.WORKITEMKEY);
			if (raciRole!=null) {
				crit.add(TNotifyPeer.RACIROLE, raciRole);
			}
			crit.addIn(TWorkItemPeer.PROJECTKEY, projectIDChunk);
			crit.addAscendingOrderByColumn(ISGROUP);
			crit.addAscendingOrderByColumn(LASTNAME);
			crit.addAscendingOrderByColumn(FIRSTNAME);
			crit.addAscendingOrderByColumn(LOGINNAME);
			crit.setDistinct();
			try {
				personList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (Exception e) {
				LOGGER.error("Loading the used informants/consultants for projects " + projectIDChunk + " failed with " + e, e);
			}
		}
		return personList;
	}

	/**
	 * Load the persons which are picked persons for at least one workItem in
	 * any of the projects
	 * 
	 * @param projects
	 * @return
	 */
	public List<TPersonBean> loadUsedUserPickersByProjects(Integer fieldID, Integer[] projects) {
		List<TPersonBean> personList = new ArrayList<TPersonBean>();
		if (projects == null || projects.length == 0) {
			return new ArrayList<TPersonBean>();
		}
		List<int[]> projectChunkList = GeneralUtils.getListOfChunks(projects);
		Iterator<int[]> iterator = projectChunkList.iterator();
		while (iterator.hasNext()) {
			int[] projectIDChunk = (int[]) iterator.next();
			Criteria crit = new Criteria();
			crit.addJoin(PKEY, TAttributeValuePeer.SYSTEMOPTIONID);
			crit.add(TAttributeValuePeer.FIELDKEY, fieldID);
			crit.addJoin(TAttributeValuePeer.WORKITEM, TWorkItemPeer.WORKITEMKEY);
			crit.addIn(TWorkItemPeer.PROJECTKEY, projectIDChunk);
			crit.addAscendingOrderByColumn(ISGROUP);
			crit.addAscendingOrderByColumn(LASTNAME);
			crit.addAscendingOrderByColumn(FIRSTNAME);
			crit.addAscendingOrderByColumn(LOGINNAME);
			crit.setDistinct();
			try {
				personList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (Exception e) {
				LOGGER.error("Loading the used picker persons for projects " + projects + " failed with " + e, e);
				return new ArrayList<TPersonBean>();
			}
		}
		return personList;
	}

	/**
	 * Load the persons/groups which are consultants/informants for at least one
	 * workItem from the array
	 * 
	 * @param workItemIDs
	 * @param raciRole
	 * @return
	 */
	public List<TPersonBean> loadUsedConsultantsInformantsByWorkItemIDs(List<Integer> workItemIDs, String raciRole) {
		if (workItemIDs == null || workItemIDs.isEmpty()) {
			return new LinkedList<TPersonBean>();
		}
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList == null) {
			return new LinkedList<TPersonBean>();
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		List<TPerson> personList = new LinkedList<TPerson>();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			Criteria criteria = prepareUsedConsultantsInformantsCriteria(workItemIDChunk, raciRole);
			try {
				personList.addAll(doSelect(criteria));
			} catch (Exception e) {
				LOGGER.error("Loading the used consultant/informant person by workItemIDs failed with " + e.getMessage(), e);
			}
		}
		// some persons might get duplicated as a
		// result of the presence in more than one chunk
		// so let's gather them in a Set
		Set<Integer> personIDs = new HashSet<Integer>();
		Iterator<TPerson> itrPerson = personList.iterator();
		while (itrPerson.hasNext()) {
			TPerson tPerson = itrPerson.next();
			personIDs.add(tPerson.getObjectID());
		}
		return loadSortedPersonsOrGroups(GeneralUtils.createListFromSet(personIDs));
	}

	private Criteria prepareUsedConsultantsInformantsCriteria(int[] workItemIDs, String raciRole) {
		Criteria criteria = new Criteria();
		criteria.addJoin(PKEY, BaseTNotifyPeer.PERSONKEY);
		criteria.addIn(BaseTNotifyPeer.WORKITEM, workItemIDs);
		if (raciRole != null) {
			criteria.add(BaseTNotifyPeer.RACIROLE, raciRole);
		}
		criteria.setDistinct();
		return criteria;
	}

	/**
	 * @return List of personbeans for departments
	 * @throws Exception
	 */
	public List<TPersonBean> loadByDepartments(Integer[] departmentKeys) {
		if (departmentKeys != null && departmentKeys.length > 0) {
			Criteria crit = new Criteria();
			crit.add(ISGROUP, (Object) "Y", Criteria.NOT_EQUAL);
			crit.addIn(DEPKEY, departmentKeys);
			crit.addAscendingOrderByColumn(LASTNAME);
			crit.addAscendingOrderByColumn(FIRSTNAME);
			try {
				return convertTorqueListToBeanList(doSelect(crit));
			} catch (Exception e) {
				LOGGER.error("Loading the persons by department keys " + departmentKeys + " failed with " + e, e);
			}
		}
		return new LinkedList<TPersonBean>();
	}

	/**
	 * Loads the persons from the departments
	 * 
	 * @param departmentIDs
	 * @param currentOptions
	 * @return
	 */
	public List<TPersonBean> loadByDepartments(List<Integer> departmentIDs, Integer[] currentOptions) {
		Set<Integer> persons = new HashSet<Integer>();
		if (departmentIDs != null && !departmentIDs.isEmpty()) {
			Criteria criteria = new Criteria();
			criteria.addIn(DEPKEY, departmentIDs);
			// filter out the deactivated users
			criteria.add(DELETED, (Object) BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
			criteria.setDistinct();
			List<TPerson> personsByDepartments = null;
			try {
				personsByDepartments = doSelect(criteria);
			} catch (TorqueException e) {
				LOGGER.error("Loading the persons by departments with current options failed with " + e.getMessage(), e);
			}
			if (personsByDepartments != null) {
				Iterator<TPerson> iterator = personsByDepartments.iterator();
				while (iterator.hasNext()) {
					TPerson tPerson = iterator.next();
					persons.add(tPerson.getObjectID());
				}
			}
		}
		if (currentOptions != null && currentOptions.length > 0) {
			for (int i = 0; i < currentOptions.length; i++) {
				persons.add(currentOptions[i]);
			}
		}
		return loadByKeys(GeneralUtils.createIntegerListFromCollection(persons));
	}

	/**
	 * Gets the persons who added attachment for any of the workItems
	 * 
	 * @param workItemIDs
	 * @return
	 */
	public List<TPersonBean> getAttachmentPersons(int[] workItemIDs) {
		List<TPersonBean> personBeanList = new LinkedList<TPersonBean>();
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList != null && !workItemIDChunksList.isEmpty()) {
			Criteria criteria;
			Iterator<int[]> iterator = workItemIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] workItemIDChunk = (int[]) iterator.next();
				criteria = new Criteria();
				criteria.addJoin(PKEY, BaseTAttachmentPeer.CHANGEDBY);
				criteria.addIn(BaseTAttachmentPeer.WORKITEM, workItemIDChunk);
				try {
					personBeanList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
				} catch (Exception e) {
					LOGGER.error("Loading the attachment personBeans failed with " + e.getMessage(), e);
				}
			}
		}
		return personBeanList;
	}

	/**
	 * Returns the persons having explicit automail settings in any of the
	 * projects having a trigger set with at least one observer field
	 * 
	 * @param personIDs
	 * @param projects
	 * @param actionType
	 * @return
	 */
	public List<TPersonBean> getObserverPersonsInProjects(List<Integer> personIDs, List<Integer> projects, Integer actionType) {
		List<TPersonBean> personList = new LinkedList<TPersonBean>();
		if (personIDs == null || personIDs.isEmpty()) {
			return personList;
		}
		Criteria crit = null;
		List<int[]> personChunkList = GeneralUtils.getListOfChunks(personIDs);
		Iterator<int[]> iterator = personChunkList.iterator();
		while (iterator.hasNext()) {
			int[] personIDChunk = iterator.next();
			crit = new Criteria();
			crit.addIn(PKEY, personIDChunk);
			crit.addJoin(PKEY, TNotifySettingsPeer.PERSON);
			crit.addIn(TNotifySettingsPeer.PROJECT, projects);
			crit.addJoin(TNotifySettingsPeer.NOTIFYTRIGGER, TNotifyFieldPeer.NOTIFYTRIGGER);
			crit.add(TNotifyFieldPeer.OBSERVER, BooleanFields.TRUE_VALUE);
			crit.add(TNotifyFieldPeer.ACTIONTYPE, actionType);
			crit.setDistinct();
		}
		try {
			personList.addAll(convertTorqueListToBeanList(doSelect(crit)));
		} catch (Exception e) {
			LOGGER.error("Loading the observer persons by keys " + personIDs + " and " + projects + " failed with " + e, e);
		}
		return personList;
	}

	public static List<TPersonBean> convertTorqueListToBeanList(List<TPerson> torqueList) {
		List<TPersonBean> beanList = new LinkedList<TPersonBean>();
		if (torqueList != null) {
			Iterator<TPerson> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()) {
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}

	/**
	 * @return List of all persons that owns screens
	 * @throws Exception
	 */
	static List<TPerson> loadScreenOwners() {
		List<TPerson> persons = null;
		Criteria crit = new Criteria();
		crit.addJoin(BaseTPersonPeer.PKEY, BaseTScreenPeer.OWNER);
		crit.setDistinct();
		try {
			persons = doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Loading all screen owners failed with " + e, e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e), e);
		}
		return persons;
	}

	public TPersonBean loadByTokenPasswd(String tokenPasswd) {
		if (tokenPasswd == null || tokenPasswd.length() < 1) {
			LOGGER.error("Faulty parameter in loadByTokenPasswd.");
			return null;
		}

		Criteria crit = new Criteria();
		crit.add(TOKENPASSWD, (Object) tokenPasswd, Criteria.EQUAL);

		try {
			List<TPersonBean> tps = convertTorqueListToBeanList(doSelect(crit));
			if (tps != null && tps.size() > 0) {
				return tps.get(0);
			}
		} catch (Exception e) {
			LOGGER.debug("Loading by token password " + tokenPasswd + " failed with " + e.getMessage(), e);
		}
		return null;
	}

	public TPersonBean loadByForgotPasswordToken(String tokenPasswd) {
		if (tokenPasswd == null || tokenPasswd.length() < 1) {
			LOGGER.error("Faulty parameter in loadByForgotPasswordToken.");
			return null;
		}

		Criteria crit = new Criteria();
		crit.add(FORGOTPASSWORDKEY, (Object) tokenPasswd, Criteria.EQUAL);

		try {
			List<TPersonBean> tps = convertTorqueListToBeanList(doSelect(crit));
			if (tps != null && tps.size() > 0) {
				return tps.get(0);
			}
		} catch (Exception e) {
			LOGGER.debug("Loading by forgot password key " + tokenPasswd + " failed with " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Remove the tokens for the forgot password links in case somebody tried to
	 * reset the password for another person
	 * 
	 */
	public void cancelForgotPasswordTokens() {

		Criteria crit = new Criteria();
		Date now = new Date();
		crit.add(TOKENEXPDATE, now, Criteria.LESS_THAN);
		crit.add(TOKENEXPDATE, Criteria.ISNOTNULL);
		crit.add(TOKENPASSWD, (Object) null, Criteria.ISNOTNULL);

		try {
			List<TPerson> tps = doSelect(crit);
			if (tps != null && tps.size() > 0) {
				for (TPerson tp : tps) {
					tp.setTokenExpDate(null);
					tp.setTokenExpDate(null);
					tp.setForgotPasswordKey(null);
					tp.save();
					LOGGER.info("Removed password reset token for " + tp.getFirstName() + " " + tp.getLastName() + ": " + tp.getEmail());
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Loading by expired registration token failed with " + e.getMessage(), e);
		}
		return;
	}

	/**
	 * Remove users that have registered themselves and that have not confirmed
	 * their registration within the grace period.
	 * 
	 */
	public void removeUnconfirmedUsers() {

		Criteria crit = new Criteria();
		Date now = new Date();
		crit.add(TOKENEXPDATE, now, Criteria.LESS_THAN);
		crit.add(TOKENPASSWD, (Object) null, Criteria.ISNOTNULL);

		try {
			List<TPerson> tps = doSelect(crit);
			if (tps != null && tps.size() > 0) {
				for (TPerson tp : tps) {
					this.deletePerson(tp.getObjectID());
					LOGGER.info("Removed unconfirmed user " + tp.getFirstName() + " " + tp.getLastName() + ": " + tp.getEmail());
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Loading by expired registration token expired failed with " + e.getMessage(), e);
		}
		return;
	}

}
