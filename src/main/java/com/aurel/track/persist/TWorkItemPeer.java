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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.Transaction;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.CriteriaUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.MeetingCriteria;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.ResponsibleCriteria;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.TreeFilterCriteria;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.dao.torque.SimpleCriteria;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.exchange.excel.ExcelImportNotUniqueIdentifiersException;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.tql.TqlBL;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;
import com.workingdogs.village.Value;


/**
 * 
 * This class is the persistence layer proxy for the core objects in the 
 * Genji system, the issues.
 *
 */
public class TWorkItemPeer extends com.aurel.track.persist.BaseTWorkItemPeer 
	implements WorkItemDAO {

	private static final long serialVersionUID = 3300436877834074233L;
	private static final Logger LOGGER = LogManager.getLogger(TWorkItemPeer.class);
	private static final String failedWith = " failed with ";
	
	private static Class[] dependentPeerClasses = {
		TActualEstimatedBudgetPeer.class, 
		TAttachmentPeer.class,
		TBaseLinePeer.class,
		TBudgetPeer.class,
		TCostPeer.class,
		TIssueAttributeValuePeer.class,
		TNotifyPeer.class,
		TStateChangePeer.class,
		TTrailPeer.class,
		TComputedValuesPeer.class,
		TAttributeValuePeer.class,
		TWorkItemLinkPeer.class,
		TWorkItemLinkPeer.class,
		TWorkItemLockPeer.class,
		THistoryTransactionPeer.class,
		TSummaryMailPeer.class,
		TMSProjectTaskPeer.class,
		TPersonBasketPeer.class,
		TLastVisitedItemPeer.class,
		TReadIssuePeer.class,
		TAttachmentVersionPeer.class,
		TItemTransitionPeer.class,
		//use the superclass doDelete() method!!!
		BaseTWorkItemPeer.class
	};
	
	private static String[] fields = {
		TActualEstimatedBudgetPeer.WORKITEMKEY, 
		TAttachmentPeer.WORKITEM,
		TBaseLinePeer.WORKITEMKEY,
		TBudgetPeer.WORKITEMKEY,
		TCostPeer.WORKITEM,
		TIssueAttributeValuePeer.ISSUE,
		TNotifyPeer.WORKITEM,
		TStateChangePeer.WORKITEMKEY,
		TTrailPeer.WORKITEMKEY,
		TComputedValuesPeer.WORKITEMKEY,
		TAttributeValuePeer.WORKITEM,
		TWorkItemLinkPeer.LINKPRED,
		TWorkItemLinkPeer.LINKSUCC,
		TWorkItemLockPeer.WORKITEM,
		THistoryTransactionPeer.WORKITEM,
		TSummaryMailPeer.WORKITEM,
		TMSProjectTaskPeer.WORKITEM,
		TPersonBasketPeer.WORKITEM,
		TLastVisitedItemPeer.WORKITEM,
		TReadIssuePeer.WORKITEM,
		TAttachmentVersionPeer.WORKITEM,
		TItemTransitionPeer.WORKITEM,
		BaseTWorkItemPeer.WORKITEMKEY
	};

	/**
	 * delete workItems with the given criteria and delete all dependent database entries
	 * Called by reflection
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		try {
			// retrieve all the items first
			List<TWorkItem> list = doSelect(crit);
			if (list == null || list.isEmpty()) {
				return;
			}
			List<Integer> itemIDs = new ArrayList<Integer>(list.size());
			for (TWorkItem tWorkItem : list) {
				itemIDs.add(tWorkItem.getObjectID());
			}
			removeParents(itemIDs);
			List<Integer> itemPickerFields = FieldBL.getCustomPickerFieldIDs(SystemFields.INTEGER_ISSUENO);
			for (TWorkItem workItem : list) {
				Integer workItemID = workItem.getObjectID();
				if (itemPickerFields!=null && !itemPickerFields.isEmpty()) {
					for (Integer fieldID : itemPickerFields) {
						//Remove the occurrences of items in item pickers
						AttributeValueBL.deleteBySystemOption(fieldID, workItemID, SystemFields.INTEGER_ISSUENO);
					}
				}
				ReflectionHelper.delete(dependentPeerClasses, fields, workItemID);
			}
		} catch (TorqueException e) {
			LOGGER.error("Cascade deleteing the field configs failed with " + e.getMessage(), e);
		}
	}

	/**
	 * Set the superior workItem to null for the child workItems of a deleted parent node 
	 * @param objectID
	 */
	private static void removeParents(List<Integer> itemIDs) {
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(itemIDs);
		if (workItemIDChunksList==null) {
			return;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			int[] workItemIDChunk = (int[])iterator.next();
			Criteria selectCriteria = new Criteria();
			selectCriteria.addIn(SUPERIORWORKITEM, workItemIDChunk);
			Criteria updateCriteria = new Criteria();
			updateCriteria.add(SUPERIORWORKITEM, (Object)null, Criteria.ISNULL);
			try {
				doUpdate(selectCriteria, updateCriteria);
			} catch (TorqueException e) {
				LOGGER.error("Removing the deleted superior workItem from childen to be deleted  at loop " + i + failedWith + e.getMessage(), e);
			}
		}
	}
	
	
	/************************************************************************
	 *************************Bean methods***********************************
	 ************************************************************************/
	/**
	 * Loads a priorityBean by primary key 
	 * @param objectID
	 * @return
	 */
	public TWorkItemBean loadByPrimaryKey(Integer objectID) throws ItemLoaderException {
		TWorkItem tWorkItem = null;
		try {
			tWorkItem = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.info("Loading the workItem by primary key " + objectID + failedWith + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			throw new ItemLoaderException("Loading the workItem by " + objectID + " failed", e);
		} 
		if (tWorkItem!=null){
			return tWorkItem.getBean();
		}
		return null;
	}
	
	public TWorkItemBean loadByProjectSpecificID(Integer projectID,Integer idNumber) throws ItemLoaderException{
		Criteria crit = new Criteria();
		crit.add(PROJECTKEY,projectID);
		crit.add(IDNUMBER,idNumber);
		try	{
			List<TWorkItem> torqueList = doSelect(crit);
			if(torqueList!=null && !torqueList.isEmpty()){
				return torqueList.get(0).getBean();
			}
		} catch (TorqueException e) {
			LOGGER.error("loadByProjectSpecificID failed with " + e.getMessage(), e);
		}

		return null;
	}
	
	/**
	 * Gets the list of workItems which should appear in the reminder email 
	 * @param personID
	 * @param remindMeAsOriginator
	 * @param remindMeAsManager
	 * @param remindMeAsResponsible
	 * @param dateLimit
	 * @return
	 */
	public List<TWorkItemBean> loadReminderWorkItems(Integer personID, String remindMeAsOriginator, String remindMeAsManager, String remindMeAsResponsible, Date dateLimit) {
		// now build the criteria
		Criteria crit = new Criteria();
		Criterion raciCriterion = null;
		boolean first = true;
		if (remindMeAsOriginator!=null && BooleanFields.TRUE_VALUE.equals(remindMeAsOriginator)) {
			LOGGER.debug("Adding originator");
			raciCriterion = crit.getNewCriterion(ORIGINATOR, personID, Criteria.EQUAL);
			first = false;
		}
		if (remindMeAsManager!=null && BooleanFields.TRUE_VALUE.equals(remindMeAsManager)) {
			if (first) {
				LOGGER.debug("Adding manager");
				raciCriterion = crit.getNewCriterion(OWNER, personID, Criteria.EQUAL);
				first = false;
			} else {
				LOGGER.debug("ORing manager");
				if (raciCriterion!=null) {  // just to disable compiler warning
					raciCriterion = raciCriterion.or(crit.getNewCriterion(OWNER, personID, Criteria.EQUAL));
				}
			}
		}
		if (remindMeAsResponsible!=null && BooleanFields.TRUE_VALUE.equals(remindMeAsResponsible)) {
			if (first) {
				LOGGER.debug("adding responsible");
				raciCriterion = crit.getNewCriterion(RESPONSIBLE, personID, Criteria.EQUAL);
			} else {
				LOGGER.debug("ORing responsible");
				if (raciCriterion!=null) { // just to disable compiler warning
					raciCriterion = raciCriterion.or(crit.getNewCriterion(RESPONSIBLE, personID, Criteria.EQUAL));
				}
			}
		}
		if (raciCriterion!=null) {
			crit.add(raciCriterion);
		} else {
			crit.add(RESPONSIBLE, personID, Criteria.EQUAL);
		}
		Criterion endDatesCriterion = crit.getNewCriterion(ENDDATE, dateLimit, Criteria.LESS_EQUAL);
		endDatesCriterion.or(crit.getNewCriterion(TOPDOWNENDDATE, dateLimit, Criteria.LESS_EQUAL));
		crit.add(endDatesCriterion);
		CriteriaUtil.addStateFlagUnclosedCriteria(crit);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		CriteriaUtil.addActiveProjectCriteria(crit);
		//crit.add(TWorkItemPeer.ENDDATE, dateLimit, Criteria.LESS_EQUAL);
		crit.addDescendingOrderByColumn(ENDDATE);
		crit.addDescendingOrderByColumn(TOPDOWNENDDATE);
		
		LOGGER.debug(crit.toString());
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the reminder workItems failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the number of entries in TWORKITEM.
	 * @return number of table entries in TWORKITEM
	 */
	public int count() {
		String COUNT = "count(" + WORKITEMKEY + ")";
		Criteria crit = new Criteria(); // use empty Criteria to get all entries		
		crit.addSelectColumn(COUNT);	// SELECT count(WORKITEMKEY);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
		} catch (TorqueException e) {
			LOGGER.error("Counting the workItems failed with " + e.getMessage(), e);
		} catch (DataSetException e) {
			LOGGER.error("Counting the workItems failed with " + e.getMessage(), e);
		}
		return 0;
		// get the one and only returned record. Get the first value in this record.
		// Record values index always starts at 1.
	}
	
	/**
	 * Gets a workItem by secondary key values
	 * @param identifierFieldValues
	 * @return
	 */
	public TWorkItemBean loadBySecondaryKeys(Map<Integer, Object> identifierFieldValues) throws ExcelImportNotUniqueIdentifiersException {
		List<TWorkItem> workItems = null;
		Criteria crit = new Criteria();
		try {
			Object projectID = identifierFieldValues.get(SystemFields.INTEGER_PROJECT);
			if (projectID!=null) {
				crit.add(PROJECTKEY, projectID);
			}
			Object releaseScheduled = identifierFieldValues.get(SystemFields.INTEGER_RELEASESCHEDULED);
			if (releaseScheduled!=null) {
				crit.add(RELSCHEDULEDKEY, releaseScheduled);
			}
			Object releaseNoticed = identifierFieldValues.get(SystemFields.INTEGER_RELEASENOTICED);
			if (releaseNoticed!=null) {
				crit.add(RELNOTICEDKEY, releaseNoticed);
			}
			Object managerID = identifierFieldValues.get(SystemFields.INTEGER_MANAGER);
			if (managerID!=null) {
				crit.add(OWNER, managerID);
			}
			Object responsibleID = identifierFieldValues.get(SystemFields.INTEGER_RESPONSIBLE);
			if (responsibleID!=null) {
				crit.add(RESPONSIBLE, responsibleID);
			}
			Object originatorID = identifierFieldValues.get(SystemFields.INTEGER_ORIGINATOR);
			if (originatorID!=null) {
				crit.add(ORIGINATOR, originatorID);
			}
			Object changedByID = identifierFieldValues.get(SystemFields.INTEGER_CHANGEDBY);
			if (changedByID!=null) {
				crit.add(CHANGEDBY, changedByID);
			}
			Object issueTypeID = identifierFieldValues.get(SystemFields.INTEGER_ISSUETYPE);
			if (issueTypeID!=null) {
				crit.add(CATEGORYKEY, issueTypeID);
			}
			Object stateID = identifierFieldValues.get(SystemFields.INTEGER_STATE);
			if (stateID!=null) {
				crit.add(STATE, stateID);
			}
			Object priorityID = identifierFieldValues.get(SystemFields.INTEGER_PRIORITY);
			if (priorityID!=null) {
				crit.add(PRIORITYKEY, priorityID);
			}
			Object severityID = identifierFieldValues.get(SystemFields.INTEGER_SEVERITY);
			if (severityID!=null) {
				crit.add(SEVERITYKEY, severityID);
			}
			Object synopsis = identifierFieldValues.get(SystemFields.INTEGER_SYNOPSIS);
			if (synopsis!=null && !"".equals(synopsis)) {
				crit.add(PACKAGESYNOPSYS, synopsis);
			}
			Object createDate = identifierFieldValues.get(SystemFields.INTEGER_CREATEDATE);
			if (createDate!=null) {
				crit.add(CREATED, createDate);
			}
			Object startDate = identifierFieldValues.get(SystemFields.INTEGER_STARTDATE);
			if (startDate!=null) {
				crit.add(STARTDATE, startDate);
			}
			Object endDate = identifierFieldValues.get(SystemFields.INTEGER_ENDDATE);
			if (endDate!=null) {
				crit.add(ENDDATE, endDate);
			}
			Object issueNo = identifierFieldValues.get(SystemFields.INTEGER_ISSUENO);
			if (issueNo!=null) {
				crit.add(WORKITEMKEY, issueNo);
			}
			Object build = identifierFieldValues.get(SystemFields.INTEGER_BUILD);
			if (build!=null && !"".equals(build)) {
				crit.add(BUILD, build);
			}
			Object submitterEmail = identifierFieldValues.get(SystemFields.INTEGER_SUBMITTEREMAIL);
			if (submitterEmail!=null && !"".equals(submitterEmail)) {
				crit.add(SUBMITTEREMAIL, submitterEmail);
			}
			Object superiorWorkItem = identifierFieldValues.get(SystemFields.INTEGER_SUPERIORWORKITEM);
			if (superiorWorkItem!=null) {
				crit.add(SUPERIORWORKITEM, superiorWorkItem);
			}
			//ReportBeanLoader.addArchivedDeletedFilter(crit);
			workItems = doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Getting the workItems by secondary keys failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (workItems==null || workItems.isEmpty()) {
			return null;
		} else {
			if (!workItems.isEmpty()) {
				LOGGER.debug("More than one workItems found for the secondary keys");
				throw new ExcelImportNotUniqueIdentifiersException(workItems.size() + "workItems found");
			}
		}
		return workItems.get(0).getBean();
	}
	
	
	/**
	 * Gets the list of all workItems
	 * @return
	 */	
	public List<TWorkItemBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Getting all workItems failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return new LinkedList<TWorkItemBean>();
		}
	}
	
	/**
	 * Gets the list of all issues with parent.
	 * @return
	 */
	public List<TWorkItemBean> loadAllWithParent() {
		Criteria crit = new Criteria();
		crit.add(SUPERIORWORKITEM, (Object)null, Criteria.ISNOTNULL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Getting all workItems with parent failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return new LinkedList<TWorkItemBean>();
		}
	}
	
	public List<TWorkItemBean> getChildren(Integer key){
		Criteria crit = new Criteria();
		crit.add(BaseTWorkItemPeer.SUPERIORWORKITEM, key, Criteria.EQUAL);
		crit.addAscendingOrderByColumn(BaseTWorkItemPeer.WORKITEMKEY);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the children by key " + key + failedWith + e);
			return null;
		}
	}
	
	/**
	 * Gets the children of a issue array
	 * @param workItemIDs
	 * @param notClosed whether to get only the unclosed child issues, or all child issues
	 * @param archived
	 * @param deleted
	 * @param itemTypesList
	 * @return
	 */
	public List<TWorkItemBean> getChildren(int[] workItemIDs,
			boolean notClosed, Integer archived, Integer deleted, List<Integer> itemTypesList) {
		List<TWorkItemBean> workItemList = new ArrayList<TWorkItemBean>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return workItemList;
		}
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return workItemList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(SUPERIORWORKITEM, workItemIDChunk);
			if (notClosed) {
				CriteriaUtil.addStateFlagUnclosedCriteria(criteria);
			}
			if (itemTypesList!=null && !itemTypesList.isEmpty()) {
				criteria.addIn(CATEGORYKEY, itemTypesList);
			}
			CriteriaUtil.addArchivedCriteria(criteria, archived, deleted);
			try {
				workItemList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the workItemBeans by children and the chunk number " + 
						i + " of length  "  + workItemIDChunk.length + failedWith + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return workItemList;
	}
	
	/**
	 * Gets the last workItem created by a person 
	 * @param personID
	 * @return
	 */
	public TWorkItemBean loadLastCreated(Integer personID) {
		Date createdDate = null;
		String max = "max(" + CREATED + ")";
		Criteria criteria = new Criteria();
		criteria.add(ORIGINATOR, personID);
		criteria.addSelectColumn(max);
		List<Record> records = null;
		try {
			records = doSelectVillageRecords(criteria);
			if (records!=null && !records.isEmpty()) {
				Record record  = records.get(0);
				if (record!=null) {
					createdDate = record.getValue(1).asDate();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Getting the creation date of the last workItem created by person " + personID + " failed with: " + e);
		}
		List<TWorkItem> workItemList = null;
		if (createdDate!=null) {
			createdDate=DateTimeUtils.clearDateSeconds(createdDate);
			criteria.clear();
			criteria.add(ORIGINATOR, personID);
			criteria.add(CREATED, createdDate, Criteria.GREATER_EQUAL);	
			criteria.addDescendingOrderByColumn(WORKITEMKEY);
			try {
				workItemList = doSelect(criteria);
			} catch (TorqueException e) {
				LOGGER.error("Getting the last workItem created by person " + personID + " failed with: " + e);
			}
		}
		if (workItemList!=null && !workItemList.isEmpty()) {
			return ((TWorkItem)workItemList.get(0)).getBean();
		}
		//}
		return null;
	}
		
	/**
	 * Gets the last workItem created by a person for a project and issueType
	 * When for the project-issueType is not found, get the last by project
	 * @param personID
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	public TWorkItemBean loadLastCreatedByProjectIssueType(Integer personID, Integer projectID, Integer issueTypeID) {
		List<TWorkItem> workItemList = null;
		Criteria criteria = new Criteria();
		criteria.add(PROJECTKEY, projectID);
		criteria.add(ORIGINATOR, personID);
		criteria.add(CATEGORYKEY, issueTypeID);	
		criteria.addDescendingOrderByColumn(CREATED);
		criteria.setLimit(1);
		try {
			workItemList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Getting the creation date of the last workItem by person " + personID + " project " + 
					projectID + " failed with: " + e);
		}
		if (workItemList!=null && !workItemList.isEmpty()) {
			return ((TWorkItem)workItemList.get(0)).getBean();
		}
		criteria.clear();
		criteria.add(PROJECTKEY, projectID);
		criteria.add(ORIGINATOR, personID);
		criteria.addDescendingOrderByColumn(CREATED);
		criteria.setLimit(1);
		try {
			workItemList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Getting the creation date of the last workItem by person " + personID + " project " + 
					projectID + " failed with: " + e);
		}
		if (workItemList!=null && !workItemList.isEmpty()) {
			return ((TWorkItem)workItemList.get(0)).getBean();
		}
		return null;
	}

	
	
	/**
	 * Loads a WorkItemBean list by workItemKeys
	 * @param workItemKeys
	 * @return
	 */
	public List<TWorkItemBean> loadByWorkItemKeys(int[] workItemKeys) {
		return  loadByWorkItemKeys(workItemKeys,(Date)null,(Date)null);
	}
	public List<TWorkItemBean> loadByWorkItemKeys(int[] workItemKeys,Date startDate,Date endDate){
		List<TWorkItemBean> workItemList = new ArrayList<TWorkItemBean>();
		if (workItemKeys==null || workItemKeys.length==0) {
			return workItemList;
		}
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemKeys);
		if (workItemIDChunksList==null) {
			return workItemList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			int[] workItemIDChunk = (int[])iterator.next();
			criteria = new Criteria();
			criteria.addIn(WORKITEMKEY, workItemIDChunk);
			addDateCriteria(criteria,startDate,endDate);
			try {
				workItemList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the workItemBeans by workItemKeys and the chunk number " + 
						i + " of length  "  + workItemIDChunk.length + failedWith + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return workItemList;
	}

	/**
	 * Loads a list with {@link TWorkItemBean}s as specified by the object identifiers in parameter.
	 * @param workItemKeys - the array of object identifiers for the <code>TWorkItemBean</code>s to be retrieved.
	 * @param archived
	 * @param deleted 
	 * @return A list with {@link TWorkItemBean}s.
	 */
	public List<TWorkItemBean> loadByWorkItemKeys(int[] workItemKeys, Integer archived, Integer deleted) {
		List<TWorkItemBean> workItemList = new ArrayList<TWorkItemBean>();
		if (workItemKeys==null || workItemKeys.length==0) {
			return workItemList;
		}
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemKeys);
		if (workItemIDChunksList==null) {
			return workItemList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			int[] workItemIDChunk = (int[])iterator.next();
			criteria = new Criteria();			
			criteria.addIn(WORKITEMKEY, workItemIDChunk);
			CriteriaUtil.addArchivedCriteria(criteria, archived, deleted);
			try {
				workItemList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the workItemBeans by workItemKeys and the chunk number " + 
						i + " of length  "  + workItemIDChunk.length + failedWith + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return workItemList;
	}
	
	/**
	 * Loads a list with {@link TWorkItemBean}s as specified by synopsis list in parameter.
	 * @param workItemKeys - synopsisList
	 * <p/>
	 * @return A list with {@link TWorkItemBean}s.
	 */
	public List<TWorkItemBean> loadBySynopsisList(List<String> synopsisList) {
		List<TWorkItemBean> workItemList = new ArrayList<TWorkItemBean>();
		if (synopsisList==null || synopsisList.isEmpty()) {
			return workItemList;
		}
		Criteria criteria;
		List<List<String>> workItemSynopsisChunksList = GeneralUtils.getListOfStringChunks(synopsisList);
		if (workItemSynopsisChunksList==null) {
			return workItemList;
		}
		Iterator<List<String>> iterator = workItemSynopsisChunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			List<String> workItemSynopsisChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(PACKAGESYNOPSYS, workItemSynopsisChunk);	
			try {
				workItemList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the workItemBeans by synopsis list and the chunk number " + 
						i + " of length  "  + workItemSynopsisChunk.size() + failedWith + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return workItemList;
	}
	
	/**
	 * Loads a list with {@link TWorkItemBean}s as specified by the uuids in parameter.
	 * @param uuids - the list of object uuids for the TWorkItemBeans to be retrieved.
	 * <p/>
	 * @return A list with {@link TWorkItemBean}s.
	 */
	public List<TWorkItemBean> loadByUuids(List<String> uuids) {
		List<TWorkItemBean> workItemList = new ArrayList<TWorkItemBean>();
		if (uuids==null || uuids.isEmpty()) {
			return workItemList;
		}
		Criteria criteria;
		List<List<String>> workItemIDChunksList = GeneralUtils.getListOfStringChunks(uuids);
		if (workItemIDChunksList==null) {
			return workItemList;
		}
		Iterator<List<String>> iterator = workItemIDChunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			List<String> workItemIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(TPUUID, workItemIDChunk);
			try {
				workItemList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the workItemBeans by uuids and the chunk number " + 
						i + " of length  "  + workItemIDChunk.size() + failedWith + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return workItemList;
	}
	
	/**
	 * Loads workItems for import which has associated msProjectTaskBean objects 
	 * and are of type task, include also the closed and deleted, archived tasks
	 * @param entityID
	 * @param entityType 
	 */
	public List<TWorkItemBean> loadMsProjectTasksForImport(Integer entityID, int entityType) {
		Criteria crit = new Criteria();
		crit.addJoin(WORKITEMKEY, BaseTMSProjectTaskPeer.WORKITEM);
		crit.addJoin(CATEGORYKEY, BaseTListTypePeer.PKEY);
		crit.add(BaseTListTypePeer.TYPEFLAG, TListTypeBean.TYPEFLAGS.TASK);
		switch (entityType) {
		case SystemFields.RELEASESCHEDULED:
			crit.add(RELSCHEDULEDKEY, entityID);
			break;
		case SystemFields.PROJECT:
			crit.add(PROJECTKEY, entityID);	
			break;
		default:
			return null;
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading WorkItems with task by entityID " + entityID + " entityType " + entityType + failedWith + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads workItems for export which has associated msProjectTaskBean objects and are of type task
	 * exclude the archived and deleted tasks and depending on "closed" also the closed tasks
	 * @param entityID
	 * @param entityType
	 * @param notClosed
	 * @return
	 */
	public List<TWorkItemBean> loadMsProjectTasksForExport(Integer entityID, int entityType, boolean notClosed) {
		Criteria crit = new Criteria();
		//left join because the new task workItems doesn't have TMSProjectTask pair yet
		crit.addJoin(WORKITEMKEY, BaseTMSProjectTaskPeer.WORKITEM, Criteria.LEFT_JOIN);
		crit.addJoin(CATEGORYKEY, BaseTListTypePeer.PKEY);
		crit.add(BaseTListTypePeer.TYPEFLAG, TListTypeBean.TYPEFLAGS.TASK);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		if (notClosed) {
			CriteriaUtil.addStateFlagUnclosedCriteria(crit);
		}
		crit.addAscendingOrderByColumn(BaseTMSProjectTaskPeer.OUTLINENUMBER);
		//the new ones doesn't have outline number, they are ordered by creation date
		crit.addAscendingOrderByColumn(CREATED);
		switch (entityType) {
		case SystemFields.RELEASESCHEDULED:
			crit.add(RELSCHEDULEDKEY, entityID);
			break;
		case SystemFields.PROJECT:
			crit.add(PROJECTKEY, entityID);	
			break;
		default:
			return null;
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading WorkItems with task by entityID " + entityID + " entityType " + entityType + failedWith + e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Loads all indexable workItems and sets the projectID fields
	 * @return
	 */
	public List<TWorkItemBean> loadIndexable() {
		//For example workItems from closed projects, closed workItems
		List<TWorkItemBean> workItemBeans = loadAll();
		return workItemBeans;
	}
	
	/**
	 * Gets the maximal objectID
	 */
	public Integer getMaxObjectID() {
		String max = "max(" + WORKITEMKEY + ")";
		Criteria crit = new Criteria();
		crit.addSelectColumn(max);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (Exception e) {
			LOGGER.error("Getting the maximal workItemID failed with " + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Gets the next chunk
	 * @param actualValue
	 * @param chunkInterval
	 * @return
	 */
	public List<TWorkItemBean> getNextChunk(Integer actualValue, Integer chunkInterval) {
		SimpleCriteria crit = new SimpleCriteria();
		int toValue = actualValue.intValue() + chunkInterval.intValue();
		crit.addIsBetween(WORKITEMKEY, actualValue.intValue(), toValue);
		crit.addAscendingOrderByColumn(LASTEDIT);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the workitems from " + actualValue + " to " + toValue + failedWith + e.getMessage(), e);
			return null;
		}
		
	}
	
	/**
	 * Load the workItems by a systemListID depending on a systemListType
	 * @param systemListType
	 * @param systemListID
	 * @return
	 */
	public int[] loadBySystemList(int systemListType, Integer systemListID) {
		Criteria crit = new Criteria();
		switch (systemListType) {
		case SystemFields.PROJECT:
			crit.add(PROJECTKEY, systemListID);
			break;
		case SystemFields.RELEASE:
			Criterion relNoticed = crit.getNewCriterion(RELNOTICEDKEY, systemListID, Criteria.EQUAL);
			Criterion relScheduled= crit.getNewCriterion(RELSCHEDULEDKEY, systemListID, Criteria.EQUAL);
			crit.add(relNoticed.or(relScheduled));
			break;
		case SystemFields.ISSUETYPE:
			crit.add(CATEGORYKEY, systemListID);
			break;
		case SystemFields.STATE:
			crit.add(STATE, systemListID);
			break;
		case SystemFields.PRIORITY:
			crit.add(PRIORITYKEY, systemListID);
			break;
		case SystemFields.SEVERITY:
			crit.add(SEVERITYKEY, systemListID);
			break;
		default:
			return null;
		}
		crit.addSelectColumn(WORKITEMKEY);
		List<Record> torqueList = null;
		try {
			torqueList = doSelectVillageRecords(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading workItemIDs by  systemListType " + systemListType +
					" and systemListID " + systemListID + failedWith + e);
		}
		int[] workItemIDs = null;
		if (torqueList!=null) {
			workItemIDs = new int[torqueList.size()];
			for (int i = 0; i < torqueList.size(); i++) {
				Record record = (Record)torqueList.get(i);
				try {
					workItemIDs[i] = record.getValue(1).asInt();
				} catch (DataSetException e) {
					LOGGER.error("Getting the workItemIDs by  systemListType " + systemListType +
							" and systemListID " + systemListID + failedWith + e);
				}
			}
		}
		return workItemIDs;
	}
	
	
	/**
	 * Get all the issue from an project, include the closed/archived/deleted issues.
	 */
	public List<TWorkItemBean> loadAllByProject(Integer projectID, Integer archived, Integer deleted) {
		Criteria crit = new Criteria();
		crit.add(PROJECTKEY, projectID);
		crit.addAscendingOrderByColumn(WORKITEMKEY);
		CriteriaUtil.addArchivedCriteria(crit, archived, deleted);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all workItems by project " + projectID + failedWith + e.getMessage(), e);
			return null;
		}
	}
	
	Object mutex = new Object();
	
	/**
	 * Saves a workItemBean in the TWorkItem table and creates/updates a lucene Document for it.
	 * Be sure that the TWorkItem.save() is not called directly from other program code, 
	 * because then no lucene indexing takes place!!!
	 * @param workItemBean
	 */
	public Integer save(TWorkItemBean workItemBean) throws ItemPersisterException {
		boolean isNew = false;
		Integer objectID = null;
		Integer idNumber = null;
		try {
			isNew = workItemBean.getObjectID()==null || workItemBean.getObjectID().intValue()==-1;
			TWorkItem tWorkItem = BaseTWorkItem.createTWorkItem(workItemBean);
			if (isNew) {
				synchronized (mutex) {
					Integer projectID = tWorkItem.getProjectID();
					Integer nextWBS = getNextWbsOnLevel(tWorkItem.getSuperiorworkitem(), projectID);
					tWorkItem.setWBSOnLevel(nextWBS);
					idNumber = calculateIDNumber(projectID, tWorkItem);
				}
			} else {
				tWorkItem.save();
			}
			objectID = tWorkItem.getObjectID();
			LOGGER.debug("WorkItem with ID " + objectID + " saved ");
		} catch (Exception e) {
			LOGGER.error("Saving the workItem " + workItemBean.getObjectID() + 
					" with synopsis " + workItemBean.getSynopsis() + failedWith + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			throw new ItemPersisterException("Saving the workItem " + workItemBean.getObjectID() + 
					" with synopsis " + workItemBean.getSynopsis() + " failed", e);
		}
		try  {
			//objectID null means that the save doesn't succeeded 
			if (objectID!=null) {
				if (isNew) {
					workItemBean.setObjectID(objectID);
					workItemBean.setIDNumber(idNumber);
				}
				LuceneIndexer.addToWorkItemIndex(workItemBean, !isNew);
				//possible lucene update in other cluster nodes
				ClusterMarkChangesBL.markDirtyWorkitemInCluster(objectID, ClusterMarkChangesBL.getChangeTypeByAddOrUpdateIndex(isNew));
			}
		} catch (Exception e) {
			LOGGER.error("Saving the workitem in lucene index failed with " + e.getMessage(), e);
		}
		return objectID;
	}
	
	/**
	 * Calculates the project specific IDNumber
	 * If tWorkItem is specified then it is saved after setting the calculated IDNumber
	 * Otherwise only returns the IDNumber
	 * @param projectID
	 * @param tWorkItem
	 * @return
	 */
	private static synchronized Integer calculateIDNumber(Integer projectID, TWorkItem tWorkItem) {
		Integer nextItemID = null;
		if (projectID!=null) {
			Connection connection = null;
			try {
				connection = Transaction.begin(DATABASE_NAME);
			} catch (TorqueException e) {
				LOGGER.error("Getting the connection for failed with " + e.getMessage(), e);
				return null;
			}
			if (connection!=null) {
				try {
					TProject tProject = null;
					try {
						tProject = TProjectPeer.retrieveByPK(projectID, connection);
					} catch(Exception e) {
						LOGGER.error("Loading of a project by primary key " + projectID + failedWith + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
					if (tProject!=null) {
						nextItemID = tProject.getNextItemID();
						if (nextItemID==null) {
							nextItemID = getNextItemID(projectID);
						}
						if (tWorkItem!=null) {
							tWorkItem.setIDNumber(nextItemID);
							tWorkItem.save(connection);
						}
						tProject.setNextItemID(nextItemID.intValue()+1);
						TProjectPeer.doUpdate(tProject, connection);
						Transaction.commit(connection);
					}
					connection = null;
				} catch (Exception e) {
					LOGGER.error("Setting the project specific itemID failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				} finally {
					if (connection!=null) {
						Transaction.safeRollback(connection);
					}
				}
			}
		}
		return nextItemID;
	}
	
	/**
	 * Saves a workItemBean in the TWorkItem table without lucene update. 
	 * @param workItemBean
	 */
	public Integer saveSimple(TWorkItemBean workItemBean) throws ItemPersisterException {
		Integer objectID = null;
		try {
			TWorkItem tWorkItem = BaseTWorkItem.createTWorkItem(workItemBean);
			tWorkItem.save();
			objectID = tWorkItem.getObjectID();
			LOGGER.debug("WorkItem with ID " + objectID + " saved ");
		} catch (Exception e) {
			LOGGER.error("Saving the workItem " + workItemBean.getObjectID() + 
					" with synopsis " + workItemBean.getSynopsis() + failedWith + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			throw new ItemPersisterException("Saving the workItem " + workItemBean.getObjectID() + 
					" with synopsis " + workItemBean.getSynopsis() + " failed", e);
		}
		return objectID;
	}
	
	
	/**
	 * Whether the item has open children besides exceptChildrenIDs
	 * @param objectID
	 * @return
	 */
	public List<TWorkItemBean> getOpenChildren(Integer objectID, List<Integer> exceptChildrenIDs) {
		Criteria crit = new Criteria();
		crit.add(SUPERIORWORKITEM, objectID);
		crit.addJoin(STATE, TStatePeer.PKEY);
		crit.add(TStatePeer.STATEFLAG, TStateBean.STATEFLAGS.CLOSED, Criteria.NOT_EQUAL);
		if (exceptChildrenIDs!=null && !exceptChildrenIDs.isEmpty()) {
			crit.addNotIn(WORKITEMKEY, exceptChildrenIDs);
		}
		CriteriaUtil.addArchivedDeletedFilter(crit);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting the open children failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return  null;
		}
	}
	
	/**
	 * Is any workItem from the array still open? 
	 * @param workItemIDs
	 * @return
	 */
	public boolean isAnyWorkItemOpen(int[] workItemIDs) {
		if (workItemIDs==null || workItemIDs.length==0) {
			return false;
		}
		Criteria crit = new Criteria();
		crit.addIn(WORKITEMKEY, workItemIDs);
		crit.addJoin(STATE, BaseTStatePeer.PKEY);
		crit.add(BaseTStatePeer.STATEFLAG, TStateBean.STATEFLAGS.CLOSED, Criteria.NOT_EQUAL);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		List<TWorkItem> list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the open children failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return list!=null && !list.isEmpty();	
	}
	
	/**
	 * Returns a deep copy of the workItemBean 
	 * @param workItemBean
	 * @return
	 */
	public TWorkItemBean copyDeep(TWorkItemBean workItemBean) {
		TWorkItem tWorkItemOriginal = null;
		try {
			tWorkItemOriginal = BaseTWorkItem.createTWorkItem(workItemBean);
		} catch (TorqueException e1) {
			LOGGER.error("Getting the torque object from bean failed with " + e1.getMessage());
		}
		TWorkItem tWorkItemCopy = null;
		if (tWorkItemOriginal!=null) {
			try {
				tWorkItemCopy = tWorkItemOriginal.copy(true);
			} catch (TorqueException e2) {
				LOGGER.error("Deep copy failed with " + e2.getMessage());
			}
		}
		if (tWorkItemCopy!=null) {
			return tWorkItemCopy.getBean();
		} else {
			return null;
		}
	}
	
	/**
	 * Get a list of workItemBeans according to a prepared critera
	 * @param criteria
	 * @return
	 */
	public static List<TWorkItemBean> getWorkItemBeansByCriteria(Criteria criteria) {
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the workItems by criteria " + criteria.toString() + failedWith + e.getMessage(), e);
			return null;
		}
	}
	
	/*********************************************************
	* Manager-, Responsible-, My- and Custom Reports methods * 
	*********************************************************/
	
	public static void addDateCriteria(Criteria crit, Date startDate, Date endDate){
		if (startDate==null && endDate==null) {
			return;
		}
		if (startDate!=null && endDate!=null) {
			// criteria for start date
			Criteria.Criterion cStartDateMin = crit.getNewCriterion(STARTDATE , startDate, Criteria.GREATER_EQUAL);
			Criteria.Criterion cStartDateMax = crit.getNewCriterion(STARTDATE , endDate, Criteria.LESS_EQUAL);
			Criteria.Criterion cStartDateAnd = cStartDateMin.and(cStartDateMax);
			// criteria for topdown start date
			Criteria.Criterion cTopDownStartDateMin = crit.getNewCriterion(TOPDOWNSTARTDATE, startDate, Criteria.GREATER_EQUAL);
			Criteria.Criterion cTopDownStartDateMax = crit.getNewCriterion(TOPDOWNSTARTDATE, endDate, Criteria.LESS_EQUAL);
			Criteria.Criterion cTopDownStartDateAnd = cTopDownStartDateMin.and(cTopDownStartDateMax);

			// criteria for end date
			Criteria.Criterion cEndDateMin = crit.getNewCriterion(ENDDATE, startDate, Criteria.GREATER_EQUAL);
			Criteria.Criterion cEndDateMax = crit.getNewCriterion(ENDDATE, endDate, Criteria.LESS_EQUAL);
			Criteria.Criterion cEndDateAnd = cEndDateMin.and(cEndDateMax);
			// criteria for topDown end date
			Criteria.Criterion cTopDownEndDateMin = crit.getNewCriterion(TOPDOWNENDDATE, startDate, Criteria.GREATER_EQUAL);
			Criteria.Criterion cTopDownEndDateMax = crit.getNewCriterion(TOPDOWNENDDATE, endDate, Criteria.LESS_EQUAL);
			Criteria.Criterion cTopDownEndDateAnd = cTopDownEndDateMin.and(cTopDownEndDateMax);

			// or criteria for start and end date
			Criteria.Criterion cDateOr = cStartDateAnd.or(cEndDateAnd).or(cTopDownStartDateAnd).or(cTopDownEndDateAnd);
			crit.add(cDateOr);
		} else {
			if (startDate!=null) {
				// criteria for start date
				Criteria.Criterion cStartDateMin = crit.getNewCriterion(STARTDATE , startDate, Criteria.GREATER_EQUAL);
				// criteria for TopDown start date
				Criteria.Criterion cTopDownStartDateMin = crit.getNewCriterion(TOPDOWNSTARTDATE , startDate, Criteria.GREATER_EQUAL);
				// criteria for end date
				Criteria.Criterion cEndDateMin = crit.getNewCriterion(ENDDATE, startDate, Criteria.GREATER_EQUAL);
				// criteria for TopDown end date
				Criteria.Criterion cTopDownEndDateMin = crit.getNewCriterion(TOPDOWNENDDATE, startDate, Criteria.GREATER_EQUAL);
				// or criteria for start and end date
				Criteria.Criterion cDateOr = cStartDateMin.or(cEndDateMin).or(cTopDownStartDateMin).or(cTopDownEndDateMin);
				crit.add(cDateOr);
			}
			if (endDate!=null) {
				// criteria for start date
				Criteria.Criterion cStartDateMax = crit.getNewCriterion(STARTDATE , endDate, Criteria.LESS_EQUAL);
				// criteria for TopDownstart date
				Criteria.Criterion cTopDownStartDateMax = crit.getNewCriterion(TOPDOWNSTARTDATE , endDate, Criteria.LESS_EQUAL);
				// criteria for end date
				Criteria.Criterion cEndDateMax = crit.getNewCriterion(ENDDATE , endDate, Criteria.LESS_EQUAL);
				// criteria for TopDown end date
				Criteria.Criterion cTopDownEndDateMax = crit.getNewCriterion(TOPDOWNENDDATE , endDate, Criteria.LESS_EQUAL);
				// or criteria for start and end date
				Criteria.Criterion cDateOr = cStartDateMax.or(cEndDateMax).or(cTopDownStartDateMax).or(cTopDownEndDateMax);
				crit.add(cDateOr);
			}
		}
	}
	
	/**
	 * Count the number of items in InBasket
	 * @param personID
	 * @return
	 */
	public Integer countResponsibleWorkItems(Integer personID) {
		Criteria crit = ResponsibleCriteria.prepareResponsibleCriteria(personID);
		String COUNT = "count(" + WORKITEMKEY + ")";
		crit.addSelectColumn(COUNT);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
		} catch (TorqueException e) {
			LOGGER.error("Counting the workItems failed with " + e.getMessage(), e);
		} catch (DataSetException e) {
			LOGGER.error("Counting the workItems failed with " + e.getMessage(), e);
		}
		return 0;
	}
	
	/**
	 * Get the workItemBeans the person is responsible for
	 * If project is not null then just for that project
	 * @param personID
	 * @param project
	 * @param entityFlag the meaning of the project: can be project, release, subproject etc.
	 * @return
	 */
	public List<TWorkItemBean> loadResponsibleWorkItems(Integer personID) {
		Criteria crit = ResponsibleCriteria.prepareResponsibleCriteria(personID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the responsible workItems for person " + personID + failedWith + e.getMessage(), e);
			return new ArrayList<TWorkItemBean>();
		}
	}
	
	/**
	 * Get meetings workItemBeans the person
	 * is manager or responsible or owner for
	 * If project is not null then just for that project/release
	 * @param personID
	 * @param project
	 * @param entityFlag the meaning of the project: can be project, release, subproject etc.
	 * @return
	 */
	public List<TWorkItemBean> loadOrigManRespMeetings(Integer personID, Integer[] projectIDs, Integer[] releaseIDs) {
		Criteria crit = MeetingCriteria.prepareOrigManRespMeetingsCriteria(personID, projectIDs, releaseIDs);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the my workItems for person " + personID + failedWith + e.getMessage(), e);
			return new ArrayList<TWorkItemBean>();
		}
	}
	
	/**
	 * Get meetings workItemBeans the person
	 * is consulted or informed for
	 * If project is not null then just for that project/release
	 * @param personID
	 * @param project
	 * @param entityFlag the meaning of the project: can be project, release, subproject etc.
	 * @return
	 */
	public List<TWorkItemBean> loadConsInfMeetings(Integer personID, Integer[] projectIDs, Integer[] releaseIDs) {
		Criteria crit = MeetingCriteria.prepareConsInfMeetingsCriteria(personID, projectIDs, releaseIDs);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the my workItems for person " + personID + failedWith + e.getMessage(), e);
			return new ArrayList<TWorkItemBean>();
		}
	}
	
	/**
	 * Get the workItemBeans filtered by the FilterSelectsTO
	 * @param filterSelectsTO
	 * @return
	 */
	public Set<Integer> loadParentsInContext(Integer[] projectIDs, Integer itemTypeID) {
		Set<Integer> parentIDSet = new HashSet<Integer>();
		Criteria crit = new Criteria();
		String parent = "PARENT";
		String child = "CHILD";
		crit.addAlias(parent, TWorkItemPeer.TABLE_NAME);
		crit.addAlias(child, TWorkItemPeer.TABLE_NAME);
		crit.addJoin(parent + ".WORKITEMKEY", child + ".SUPERIORWORKITEM");
		if (projectIDs != null && projectIDs.length != 0) {
			crit.addIn(parent + ".PROJECTKEY", projectIDs);
		}
		if (itemTypeID!=null) {
			crit.add(parent + ".CATEGORYKEY", itemTypeID);
		}
		crit.addSelectColumn(parent + ".WORKITEMKEY");
		crit.setDistinct();
		List<Record> records = null;
		try {
			records = doSelectVillageRecords(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading the context items with parent  failed with " + e.getMessage(), e);
			return parentIDSet;
		}
		try {
			if (records!=null && !records.isEmpty()) {
				for (Record record : records) {
					Integer workItemID = record.getValue(1).asIntegerObj();
					parentIDSet.add(workItemID); 
				}
			}
		} catch (Exception e) {
			LOGGER.error("Getting the parent itemID failed with " + e.getMessage(), e);
		}
		return parentIDSet;
	}
	
	/**
	 * Count the number of filter items
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	public Integer countTreeFilterItems(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID) {
		Integer[] selectedProjects = filterUpperTO.getSelectedProjects();
		if (selectedProjects==null  || selectedProjects.length==0) {
			//at least one selected project needed
			return 0;
		}		
		Criteria crit = TreeFilterCriteria.prepareTreeFilterCriteria(filterUpperTO, raciBean, personID);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(crit.toString());
		}
		String COUNT = "count(" + WORKITEMKEY + ")";
		crit.addSelectColumn(COUNT);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
		} catch (TorqueException e) {
			LOGGER.error("Counting the workItems failed with " + e.getMessage(), e);
		} catch (DataSetException e) {
			LOGGER.error("Counting the workItems failed with " + e.getMessage(), e);
		}
		return 0;
	}
	
	public List<TWorkItemBean> loadTreeFilterItems(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID, Date startDate,Date endDate) {
		Integer[] selectedProjects = filterUpperTO.getSelectedProjects();
		if (selectedProjects==null  || selectedProjects.length==0) {
			//at least one selected project needed
			return new ArrayList<TWorkItemBean>();
		}		
		Criteria crit = TreeFilterCriteria.prepareTreeFilterCriteria(filterUpperTO, raciBean, personID);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(crit.toString());
		}
		addDateCriteria(crit,startDate,endDate);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the custom report workItems failed with " + e.getMessage(), e);
			return new ArrayList<TWorkItemBean>();
		}
	}
	
	/**
	 * Gets the itemIDs filtered by filterSelectsTO and raciBean which are parents
	 * @param filterSelectsTO
	 * @param raciBean
	 * @param personID
	 * @param startDate
	 * @param enddDate
	 * @return
	 */
	public Set<Integer> loadTreeFilterParentIDs(FilterUpperTO filterSelectsTO, RACIBean raciBean, Integer personID, Date startDate, Date endDate) {
		Integer[] selectedProjects = filterSelectsTO.getSelectedProjects();
		if (selectedProjects==null  || selectedProjects.length==0) {
			//at least one selected project needed
			return new HashSet<Integer>();
		}		
		Criteria crit = TreeFilterCriteria.prepareTreeFilterCriteria(filterSelectsTO, raciBean, personID);
		addDateCriteria(crit,startDate,endDate);
		return getParentItems(crit);
	}
	
	/**
	 * Gets the itemIDs of those items which have children
	 * @param crit
	 * @return
	 */
	private static Set<Integer> getParentItems(Criteria crit) {
		Set<Integer> parentIDSet = new HashSet<Integer>();
		String parent = TWorkItemPeer.TABLE_NAME;//"PARENT";
		String child = "CHILD";
		crit.addAlias(parent, TWorkItemPeer.TABLE_NAME);
		crit.addAlias(child, TWorkItemPeer.TABLE_NAME);
		crit.addJoin(parent + ".WORKITEMKEY", child + ".SUPERIORWORKITEM");
		crit.addSelectColumn(parent + ".WORKITEMKEY");
		//crit.setDistinct();
		List<Record> records = null;
		try {
			records = doSelectVillageRecords(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading the context items with parent  failed with " + e.getMessage(), e);
			return parentIDSet;
		}
		try {
			if (records!=null && !records.isEmpty()) {
				for (Record record : records) {
					Integer workItemID = record.getValue(1).asIntegerObj();
					parentIDSet.add(workItemID); 
				}
			}
		} catch (Exception e) {
			LOGGER.error("Getting the parent itemID failed with " + e.getMessage(), e);
		}
		return parentIDSet;
	}
	
	/**
	 * Loads the items filtered by a TQL expression 
	 * @param filterString
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<TWorkItemBean> loadTQLFilterItems(String filterString, TPersonBean personBean, Locale locale, List<ErrorData> errors, Date startDate, Date endDate) {
		Criteria tqlCriteria = TqlBL.createCriteria(filterString, personBean, locale, errors);
		addDateCriteria(tqlCriteria,startDate, endDate);
		try {
			return convertTorqueListToBeanList(doSelect(tqlCriteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the TQL workItems failed with " + e.getMessage(), e);
			return new ArrayList<TWorkItemBean>();
		}	
	}
	
	/**
	 * Loads the itemIDs filtered by a TQL expression which are parents
	 * @param filterString
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	public Set<Integer> loadTQLFilterParentIDs(String filterString, TPersonBean personBean, Locale locale, List<ErrorData> errors) {
		Criteria tqlCriteria = TqlBL.createCriteria(filterString, personBean, locale, errors);
		return getParentItems(tqlCriteria);
	}
	
	
	/**
	/**
	 * Load projectsIDs where the user has
	 * originator/manager/responsible role for at least one workItem
	 * @param meAndMySubstituted
	 * @param meAndMySubstitutedAndGroups
	 * @return
	 */
	public Set<Integer> loadOrigManRespProjects(List<Integer> meAndMySubstituted, List<Integer> meAndMySubstitutedAndGroups) {
		Criteria criteria = new Criteria();
		Criterion originator = criteria.getNewCriterion(TWorkItemPeer.ORIGINATOR, meAndMySubstituted, Criteria.IN);
		Criterion manager = criteria.getNewCriterion(TWorkItemPeer.OWNER, meAndMySubstituted, Criteria.IN);
		Criterion responsible = criteria.getNewCriterion(TWorkItemPeer.RESPONSIBLE, meAndMySubstitutedAndGroups, Criteria.IN);
		criteria.add(originator.or(manager).or(responsible));
		return getProjectIDs(criteria, "originator/manager/responsible");
	}
	
	/**
	 * Load projectsIDs where the user has consultant/informant
	 * role for at least one workItem
	 * @param meAndMySubstitutedAndGroups
	 * @return
	 */
	public Set<Integer> loadConsultantInformantProjects(List<Integer> meAndMySubstitutedAndGroups) {
		Criteria criteria = new Criteria();
		criteria.addJoin(TWorkItemPeer.WORKITEMKEY, TNotifyPeer.WORKITEM);
		criteria.addIn(TNotifyPeer.PERSONKEY, meAndMySubstitutedAndGroups);
		//criteria.addIn(TNotifyPeer.RACIROLE, new String[] {RaciRole.CONSULTANT, RaciRole.INFORMANT});
		return getProjectIDs(criteria, "consultant/informant");
	}
	
	/**
	 * Load the not closed projects where the user has on behalf of
	 * role for at least one workItem
	 * @param meAndMySubstitutedAndGroups
	 * @return
	 */
	public Set<Integer> loadOnBehalfOfProjects(List<Integer> meAndMySubstitutedAndGroups, List<Integer> onBehalfPickerFieldIDs) {
		Criteria criteria = new Criteria();
		criteria.addJoin(TWorkItemPeer.WORKITEMKEY, TAttributeValuePeer.WORKITEM);
		criteria.addIn(TAttributeValuePeer.FIELDKEY, onBehalfPickerFieldIDs);
		criteria.addIn(TAttributeValuePeer.SYSTEMOPTIONID, meAndMySubstitutedAndGroups);
		return getProjectIDs(criteria, "on behalf of");
		
	}
	
	/**
	 * Gets the projectIDs from resultset
	 * @param criteria
	 * @param raciRoles
	 * @return
	 */
	private static Set<Integer> getProjectIDs(Criteria criteria, String raciRoles) {
		Set<Integer> projectIDs = new HashSet<Integer>();
		try {
			criteria.addSelectColumn(PROJECTKEY);
			criteria.setDistinct();
			List<Record> projectIDRecords  = doSelectVillageRecords(criteria);
			if (projectIDRecords!=null) {
				for (Record record : projectIDRecords) {
					try {
						Value value =  record.getValue(1);
						if (value!=null) {
							Integer projectID = value.asIntegerObj();
							if (projectID!=null) {
								projectIDs.add(projectID);
							}
						}
					} catch (DataSetException e) {
						LOGGER.error("Getting the projectID failed with " + e.getMessage(), e);
					}
				}
			}
		} catch (TorqueException e) {
			LOGGER.error("Loading of " + raciRoles +" projects failed with " + e.getMessage(), e);
		}
		return projectIDs;
	}
	
	
	/**
	 * Convert the torque object list into bean list
	 * @param torqueList
	 * @return
	 */
	public static List<TWorkItemBean> convertTorqueListToBeanList(List<TWorkItem> torqueList) {
		List<TWorkItemBean> beanList = new ArrayList<TWorkItemBean>();
		if (torqueList!=null){
			Iterator<TWorkItem> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}
	
	/**
	 * sets the wbs for the entire project
	 * @param projectID
	 */
	public void setWbs(Integer projectID) {
		Criteria crit = new Criteria();
		crit.add(PROJECTKEY, projectID);
		//in the case that the wbs got corrupted but still a wbs existed before try to preserve the order
		crit.addAscendingOrderByColumn(WBSONLEVEL);
		//if wbs does not exist then fall back to workItemKey as wbs
		crit.addAscendingOrderByColumn(WORKITEMKEY);
		List<TWorkItem> workItems = null;
		try {
			workItems = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading the workItems by project " + projectID + failedWith + e.getMessage(), e);
		}
		Map<Integer, List<Integer>> childerenIDsForParentIDsMap = new HashMap<Integer, List<Integer>>();
		Map<Integer, TWorkItem> workItemsMap = new HashMap<Integer, TWorkItem>();
		List<Integer> rootItems = new ArrayList<Integer>();
		if (workItems != null) {
			for (TWorkItem workItem : workItems) {
				Integer workItemID =  workItem.getObjectID();
				workItemsMap.put(workItemID, workItem);
				Integer parentID = workItem.getSuperiorworkitem();
				if (parentID==null) {
					rootItems.add(workItemID);
				} else {
					List<Integer> children = childerenIDsForParentIDsMap.get(parentID);
					if (children==null) {
						children = new LinkedList<Integer>();
						childerenIDsForParentIDsMap.put(parentID, children);
					}
					children.add(workItemID);
				}
			}
		}
		setWbsOnLevel(workItemsMap, rootItems);
		for (Integer parentIDs : childerenIDsForParentIDsMap.keySet()) {
			List<Integer> childrenIDs = childerenIDsForParentIDsMap.get(parentIDs);
			setWbsOnLevel(workItemsMap, childrenIDs);
		}
	}
	
	/**
	 * Sets the wbs on a level
	 * @param workItemsMap
	 * @param workItemsOnSameLevel
	 */
	private static void setWbsOnLevel(Map<Integer, TWorkItem> workItemsMap, List<Integer> workItemsOnSameLevel) {
		if (workItemsOnSameLevel!=null) {
			for (int i = 0; i < workItemsOnSameLevel.size(); i++) {
				Integer workItemID = workItemsOnSameLevel.get(i);
				TWorkItem workItem = workItemsMap.get(workItemID);
				if (workItem!=null) {
					int wbs = i+1;
					//save only if differs: avoid the expensive save operations if the value is already set
					if (workItem.getWBSOnLevel()==null || workItem.getWBSOnLevel().intValue()!=wbs) {
						workItem.setWBSOnLevel(wbs);
						try {
							workItem.save();
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Set the WBS " + wbs + " for workItem " + workItemID);
							}
						} catch (Exception e) {
							LOGGER.error("Setting the WBS " + wbs + failedWith + e.getMessage(), e);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Sets the wbs on the affected workItems after a drag and drop operation
	 * @param draggedWorkItemID
	 * @param droppedToWorkItemID
	 * @param before
	 */
	public synchronized void dropNearWorkItem(Integer draggedWorkItemID, Integer droppedToWorkItemID, boolean before) {

		TWorkItemBean workItemBeanDragged = null;
		try {
			workItemBeanDragged = loadByPrimaryKey(draggedWorkItemID);
		} catch (ItemLoaderException e1) {
			LOGGER.warn("The fromWorkItemID " + draggedWorkItemID + " does not exist");
			return;
		}
		TWorkItemBean workItemBeanDroppedTo = null;
		try {
			workItemBeanDroppedTo = loadByPrimaryKey(droppedToWorkItemID);
		} catch (ItemLoaderException e1) {
			LOGGER.warn("The toWorkItemID " + droppedToWorkItemID + " does not exist");
			return;
		}
		Integer projectID = workItemBeanDragged.getProjectID();
		if (projectID==null) {
			LOGGER.warn("No project found for " + draggedWorkItemID + " does not exist");
			return;
		}
		if (workItemBeanDragged.getWBSOnLevel()==null || workItemBeanDroppedTo.getWBSOnLevel()==null) {
			setWbs(projectID);
			try {
				//load them again this time with wbs numbers set
				workItemBeanDragged = loadByPrimaryKey(draggedWorkItemID);
				workItemBeanDroppedTo = loadByPrimaryKey(droppedToWorkItemID);
			} catch (ItemLoaderException e) {
			}
		}
		if (!projectID.equals(workItemBeanDroppedTo.getProjectID())) {
			LOGGER.debug("The drop target is not from the same project: abort drop");
			return;
		}
		Integer parentOfDraggedWorkItem = workItemBeanDragged.getSuperiorworkitem();
		Integer parentOfDroppedToWorkItem = workItemBeanDroppedTo.getSuperiorworkitem();
		Integer draggedSortOrder = workItemBeanDragged.getWBSOnLevel();
		Integer droppedToSortOrder = workItemBeanDroppedTo.getWBSOnLevel();
		if (draggedSortOrder==null || droppedToSortOrder==null) {
			LOGGER.warn("The draggedSortOrder " + draggedSortOrder + " droppedToSortOrder " + droppedToSortOrder);
			return; 
		}
		String sqlStmt = null;
		String droppedCriteria = "";
		String draggedCriteria = "";
		Integer workItemSortOrder;
		boolean parentsNull = parentOfDraggedWorkItem==null && parentOfDroppedToWorkItem==null;
		boolean parentsNotNull = parentOfDraggedWorkItem!=null && parentOfDroppedToWorkItem!=null;
		if (parentsNull || (parentsNotNull && parentOfDraggedWorkItem != null && 
						parentOfDraggedWorkItem.equals(parentOfDroppedToWorkItem))) {
			if (draggedSortOrder.equals(droppedToSortOrder)) {
				//on the same level the same sortorder, not a real move, do nothing 
				LOGGER.debug("The draggedSortOrder " + draggedSortOrder + " equals droppedToSortOrder " + droppedToSortOrder);
				return; 
			}
			String parentCriteria = "";
			//same level: both parent null or same parent 
			if (parentsNotNull) {
				parentCriteria = " AND SUPERIORWORKITEM = " + parentOfDraggedWorkItem;
			} else {
				parentCriteria = " AND SUPERIORWORKITEM IS NULL ";
			}
			int inc = 0;
			if (draggedSortOrder>droppedToSortOrder) {
				inc = 1;
				if (before) {
					droppedCriteria = " AND WBSONLEVEL >= " + droppedToSortOrder;
					draggedCriteria = " AND WBSONLEVEL < " + draggedSortOrder;
					workItemSortOrder = droppedToSortOrder;
				} else {
					droppedCriteria = " AND WBSONLEVEL > " + droppedToSortOrder;
					draggedCriteria = " AND WBSONLEVEL < " + draggedSortOrder;
					workItemSortOrder = droppedToSortOrder+1;
				}
			} else {
				inc = -1;
				if (before) {
					droppedCriteria = " AND WBSONLEVEL < " + droppedToSortOrder;
					draggedCriteria = " AND WBSONLEVEL > " + draggedSortOrder;
					workItemSortOrder = droppedToSortOrder-1;
				} else {
					droppedCriteria = " AND WBSONLEVEL <= " + droppedToSortOrder;
					draggedCriteria = " AND WBSONLEVEL > " + draggedSortOrder;
					workItemSortOrder = droppedToSortOrder;
				}
			}
			sqlStmt = "UPDATE TWORKITEM SET WBSONLEVEL = WBSONLEVEL " + 
				" + " + inc + " WHERE " + " PROJECTKEY = " + projectID + 
				parentCriteria + draggedCriteria + droppedCriteria;
			executeStatemant(sqlStmt);
		} else {
			if (EqualUtils.equal(draggedWorkItemID, parentOfDroppedToWorkItem)) {
				LOGGER.warn("The WBS change would cause the issue " + draggedWorkItemID + " to became parent of itself");
				//avoid same parentID as issueID 
				return;
			}
			if (ItemBL.isAscendant(draggedWorkItemID, parentOfDroppedToWorkItem)) {
				LOGGER.warn("The chosen parent " + parentOfDroppedToWorkItem  + " is already a descendant of the " + draggedWorkItemID);
				return;
			}
			//different levels
			//1. remove the dragged item from the original position and shift the following items up
			shiftBranch(draggedSortOrder, parentOfDraggedWorkItem, projectID, false);
			//2. shift down the actual items in the new place to make space for the dragged item to the new position
			String parentCriteriaOfDroppedItem = "";
			if (parentOfDroppedToWorkItem!=null) {
				parentCriteriaOfDroppedItem = " AND SUPERIORWORKITEM = " + parentOfDroppedToWorkItem;
			} else {
				parentCriteriaOfDroppedItem = " AND SUPERIORWORKITEM IS NULL ";
			}
			if (before) {
				droppedCriteria = " AND WBSONLEVEL >= " + droppedToSortOrder;
				workItemSortOrder = droppedToSortOrder;
			} else {
				droppedCriteria = " AND WBSONLEVEL > " + droppedToSortOrder;
				workItemSortOrder = droppedToSortOrder+1;
			}
			sqlStmt = "UPDATE TWORKITEM SET WBSONLEVEL = WBSONLEVEL" + 
				 "+1 " + " WHERE " + " PROJECTKEY = " + projectID + 
				parentCriteriaOfDroppedItem + droppedCriteria;
			executeStatemant(sqlStmt);
			workItemBeanDragged.setSuperiorworkitem(parentOfDroppedToWorkItem);
		}
		workItemBeanDragged.setWBSOnLevel(workItemSortOrder);
		try {
			saveSimple(workItemBeanDragged);
		} catch (ItemPersisterException e) {
			LOGGER.error("Saving the new droppedToSortOrder " + droppedToSortOrder + 
					" for  workItemID " + draggedWorkItemID + failedWith + e.getMessage(), e);
		}
	}
	
	/**
	 * The parent was changed: recalculate the WBS both the old parent and the new parent can be null
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param contextInformation
	 */
	public void parentChanged(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, 
			Map<String, Object> contextInformation) {
		if (workItemBeanOriginal==null) {
			//new item
			return;
		}
		Integer oldParent = workItemBeanOriginal.getSuperiorworkitem();
		Integer newParent = workItemBean.getSuperiorworkitem();
		if ((oldParent==null && newParent==null) || (oldParent!=null && newParent!=null && oldParent.equals(newParent))) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("no parent change: wbs not affected");
			}
			return;
		}
		Integer projectID = workItemBean.getProjectID();
		if (projectID==null) {
			LOGGER.warn("No project found for workItem" + workItemBean.getObjectID());
			return;
		}
		//old parent workItem and project
		TWorkItemBean oldParentWorkItemBean = null;
		boolean sameProjectAsOldParent = false;
		if (oldParent!=null) {
			try {
				oldParentWorkItemBean = loadByPrimaryKey(oldParent);
				Integer oldParentProjectID = oldParentWorkItemBean.getProjectID();
				if (oldParentProjectID==null) {
					LOGGER.warn("No project found for oldParent " + oldParent);
					return;
				} else {
					sameProjectAsOldParent = projectID.equals(oldParentProjectID);
				}
			} catch (ItemLoaderException e1) {
				LOGGER.warn("The oldParent workItem " + oldParent + " does not exist");
				return;
			}
		} else {
			//previously it has no parent: the projects are considered the same
			sameProjectAsOldParent = true;
		}
		//new parent workItem and project
		TWorkItemBean newParentWorkItemBean = null;
		boolean sameProjectAsNewParent = false;
		if (newParent!=null) {
			try {
				newParentWorkItemBean = loadByPrimaryKey(newParent);
				Integer newParentProjectID = newParentWorkItemBean.getProjectID();
				if (newParentProjectID==null) {
					LOGGER.warn("No project found for newParent " + newParent);
					return;
				} else {
					sameProjectAsNewParent = projectID.equals(newParentProjectID);
				}
			} catch (ItemLoaderException e1) {
				LOGGER.warn("The newParent workItem " + newParent + " does not exist");
				return;
			}
		} else {
			//the parent is removed: the projects are considered the same
			sameProjectAsNewParent = true;
		}
		boolean outdent = false;
		if (contextInformation!=null && contextInformation.get(TWorkItemBean.OUTDENT)!=null && 
				((Boolean)contextInformation.get(TWorkItemBean.OUTDENT)).booleanValue()) {
			outdent = true;
		}
		Integer originalWBS = workItemBean.getWBSOnLevel();
		if (originalWBS==null) {
			//renumber if null and get the WBS number again
			setWbs(projectID);
			try {
				//load them again this time with wbs numbers set
				workItemBean = loadByPrimaryKey(workItemBean.getObjectID());
				originalWBS = workItemBean.getWBSOnLevel();
			} catch (ItemLoaderException e) {
			}
		}
		
		//1. remove the item from the original parent's childen and shift the following items up
		if (sameProjectAsOldParent && !outdent) {
			//if there were no previous wbs or the project of the workItem differs form the  
			//oldParent's project then there is no need to change anything in the old branch
			//move the issue's next siblings up one level if it is no outdent 
			//(by outdent the issue's next siblings will be children of the issue) 
			shiftBranch(originalWBS, oldParent, projectID, false);
		} 
		//2. add the item as last child of the new parent
		if (sameProjectAsNewParent) {
			//if the new parent differs from the workItem's parent then no change in wbs
			Integer newWBS;
			if (outdent && (contextInformation != null) && (oldParentWorkItemBean != null)) {
				List<Integer> filteredNextSiblingIDs = (List<Integer>)contextInformation.get(TWorkItemBean.NEXT_SIBLINGS);
				//by outdent the oldParent becomes the new previous sibling: make space for the outdented item,
				//by shifting down the siblings of newParent (previous grandparent) after the oldParent
				Integer oldParentWBS = oldParentWorkItemBean.getWBSOnLevel();
				if (oldParentWBS==null) {
					newWBS = Integer.valueOf(1);
				} else {
					newWBS = Integer.valueOf(oldParentWBS.intValue()+1);
				}
				//shift down the children of the newParent beginning with 
				//oldParentWBS exclusive to make place for the outdented issue
				shiftBranch(oldParentWBS, newParent, projectID, true);
				//get the next available WBS after the outdented issue's original children 
				Integer nextWbsOnOudentedChildren = getNextWbsOnLevel(workItemBean.getObjectID(), projectID);
				//gather the all siblings (not just the next ones according to wbs) of the outdented issue, 
				//because it is not guaranteed that at the moment of the outline the sortOrder is by wbs
				//(can be that in the ReportBeans there is a next sibling which has a lower wbs nummer as the outlined one still according
				//to the selected ReportBeans sortorder it is a next sibling, consequently it will become a child of outlined issue)
				Criteria criteria = new Criteria();
				criteria.add(SUPERIORWORKITEM, oldParent);
				criteria.add(WORKITEMKEY, workItemBean.getObjectID(), Criteria.NOT_EQUAL);
				//criteria.add(WBSONLEVEL, originalWBS, Criteria.GREATER_THAN);
				criteria.add(PROJECTKEY, projectID);
				//in the case that the wbs got corrupted but still a wbs existed before try to preserve the order
				criteria.addAscendingOrderByColumn(WBSONLEVEL);
				//if wbs does not exist then fall back to workItemKey as wbs
				criteria.addAscendingOrderByColumn(WORKITEMKEY);
				List<TWorkItemBean> allSiblingWorkItemBeans = null;
				try {
					allSiblingWorkItemBeans = convertTorqueListToBeanList(doSelect(criteria));
				} catch (TorqueException e) {
					LOGGER.warn("Getting the next siblings of the outdented issue " + workItemBean.getObjectID() +  failedWith + e.getMessage(), e );
				}
				List<String> idChunks = GeneralUtils.getCommaSepararedIDChunksInParenthesis(filteredNextSiblingIDs);
				
				//set all next siblings as children of the outdented workItem (see MS Project)
				
				//actualize the WBS for old next present siblings (future children of the outdented issue).
				//No common update is possible because the WBS of the next present siblings might contain gaps
				Map<Integer, TWorkItemBean> siblingWorkItemsMap = 
					GeneralUtils.createMapFromList(allSiblingWorkItemBeans);
				for (int i = 0; i < filteredNextSiblingIDs.size(); i++) {
					//the order from the ReportBeans should be preserved, insependently of current wbs 
					//(consquently the relative wbs order between the siblings might change)
					TWorkItemBean nextSiblingworkItemBean = siblingWorkItemsMap.get(filteredNextSiblingIDs.get(i));
					if (nextSiblingworkItemBean!=null && nextSiblingworkItemBean.getWBSOnLevel()!=(nextWbsOnOudentedChildren+i)) {
						nextSiblingworkItemBean.setWBSOnLevel(nextWbsOnOudentedChildren+i);
						try {
							saveSimple(nextSiblingworkItemBean);
						} catch (ItemPersisterException e) {
							LOGGER.warn("Saving the outdented present workitem " + nextSiblingworkItemBean.getObjectID() + failedWith + e.getMessage(), e);
						}
					}
				}
				
				//renumber those next siblings which were not re-parented (those not present in the ReportBeans) 
				//to fill up the gaps leaved by the re-parented present next siblings
				//no common update is possible because of gaps
				int i=0;
				if (allSiblingWorkItemBeans != null) {
					for (Iterator<TWorkItemBean> iterator = allSiblingWorkItemBeans.iterator(); iterator.hasNext();) {
						TWorkItemBean nextSiblingworkItemBean = iterator.next();
						if (!filteredNextSiblingIDs.contains(nextSiblingworkItemBean.getObjectID())) {
							i++;
							if (nextSiblingworkItemBean.getWBSOnLevel()!=i) {
								nextSiblingworkItemBean.setWBSOnLevel(i);
								try {
									saveSimple(nextSiblingworkItemBean);
								} catch (ItemPersisterException e) {
									LOGGER.warn("Saving the outdented not present workItem " + nextSiblingworkItemBean.getObjectID() + failedWith + e.getMessage(), e);
								}
							}
						}
					}
				}
				String parentCriteria = " AND SUPERIORWORKITEM = " + oldParent;
				String subprojectCriteria = " PROJECTKEY = " + projectID;
				//set all present next siblings as children of the outdented workItem (see MS Project).
				for (Iterator<String> iterator = idChunks.iterator(); iterator.hasNext();) {
					String idChunk = iterator.next();
					String sqlStmt = "UPDATE TWORKITEM SET SUPERIORWORKITEM = " + 
					workItemBean.getObjectID() + " WHERE " + subprojectCriteria + 
					parentCriteria + " AND WORKITEMKEY IN " + idChunk;	
					executeStatemant(sqlStmt);
				}
			} else {
				newWBS = getNextWbsOnLevel(newParent, projectID);
			}
			//shift the entire branch down and set the outdented workitem as first child of his previous parent
			if (EqualUtils.notEqual(workItemBean.getWBSOnLevel(), newWBS)) {
				workItemBean.setWBSOnLevel(newWBS);
			}
		}
	}
	
	/**
	 * The project was changed: recalculate the WBS
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 */
	public void projectChanged(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal) {
		if (workItemBeanOriginal==null) {
			//new item
			return;
		}
		Integer oldProjectID = workItemBeanOriginal.getProjectID();
		Integer newProjectID = workItemBean.getProjectID();
		if (oldProjectID==null || newProjectID==null) {
			LOGGER.warn("Old project " + oldProjectID + " new project " + newProjectID);
			return;
		}
		//same project -> no wbs change
		if (oldProjectID.equals(newProjectID)) {
			LOGGER.debug("No project changed ");
			return;
		}
		TWorkItemBean parentWorkItemBean = null;
		Integer parentID = workItemBean.getSuperiorworkitem();
		Integer parentProjectID = null;
		if (parentID!=null) {
			try {
				parentWorkItemBean =  loadByPrimaryKey(parentID);
			} catch (ItemLoaderException e) {
				LOGGER.warn("The parent workItem " + parentID + " does not exist");
			}
			if (parentWorkItemBean!=null) {
				parentProjectID = parentWorkItemBean.getProjectID();
			}
		}
		Integer originalWBS = workItemBean.getWBSOnLevel();
		if (originalWBS==null) {
			//renumber if null and get the WBS number again
			setWbs(oldProjectID);
			try {
				//load them again this time with wbs numbers set
				workItemBean = loadByPrimaryKey(workItemBean.getObjectID());
				originalWBS = workItemBean.getWBSOnLevel();
			} catch (ItemLoaderException e) {
			}
		}
		
		//1. remove the item from the original parent's childen and shift the following items up  
		//if the item has no parent or the item's parent project and the item's old project is the same
		if (parentProjectID==null || parentProjectID.equals(oldProjectID)) {
			//if there were no previous wbs or the project of the workItem differs form the  
			//oldParent's project then there is no need to change anything in the old branch			
			//String commaSeparatedSubprojects = getCommaSepararedSubprojectIDs(oldProjectID, new HashSet<Integer>());
			shiftBranch(originalWBS, parentID, oldProjectID, false);
		} else {
			LOGGER.debug("The originalSortOrder was null. There is no need to shift the children of the old parent ");
		}
		//2. add the item as last child of the new (parent project)
		if (parentProjectID==null || parentProjectID.equals(newProjectID)) {
			//if the new project differs from the workItem's parent then no change in wbs
			Integer newWbs = getNextWbsOnLevel(parentID, newProjectID);
			if (EqualUtils.notEqual(workItemBean.getWBSOnLevel(), newWbs)) {
				workItemBean.setWBSOnLevel(newWbs);
			}
		}
		Integer idNumber = calculateIDNumber(newProjectID, null);
		if (idNumber!=null) {
			workItemBean.setIDNumber(idNumber);
		}
	}
	
	/**
	 * Gets the root items or children of a parent from a project
	 * The archived/deleted items are also included  
	 * @param parentID
	 * @param projectID
	 * @return
	 */
	private static Integer getNextWbsOnLevel(Integer parentID, Integer projectID){
		Criteria crit = new Criteria();
		if (parentID==null) {
			crit.add(SUPERIORWORKITEM, (Integer)null, Criteria.ISNULL);
		} else {
			crit.add(SUPERIORWORKITEM, parentID, Criteria.EQUAL);
		}
		crit.add(PROJECTKEY, projectID);
		String max = "max(" + WBSONLEVEL + ")";
		crit.addSelectColumn(max);
		Integer maxWBSOnLevel = null;
		try {
			maxWBSOnLevel = ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (Exception e) {
			LOGGER.error("Getting the maximal wbs for parent " + parentID + " projectID " + projectID + failedWith + e.getMessage(), e);			
		}
		if (maxWBSOnLevel==null) {
			return Integer.valueOf(1);
		} else {
			return Integer.valueOf(maxWBSOnLevel.intValue()+1);
		}
	}
	
	private static void shiftBranch(Integer originalWBS, Integer parentID, Integer projectID, boolean increase) {
		String oldParentCriteria = "";
		if (parentID!=null) {
			oldParentCriteria = " AND SUPERIORWORKITEM = " + parentID;
		} else {
			oldParentCriteria = " AND SUPERIORWORKITEM IS NULL ";
		}
		String increaseCriteria;
		if (increase) {
			increaseCriteria = "+1 ";
		} else {
			increaseCriteria = "-1 ";
		}
		String shiftOldParentsChildren = " AND WBSONLEVEL > " + originalWBS;
		String projectCriteria = " PROJECTKEY = " + projectID;
		String sqlStmt = "UPDATE TWORKITEM SET WBSONLEVEL = WBSONLEVEL " + 
			increaseCriteria + " WHERE " + projectCriteria + 
			oldParentCriteria + shiftOldParentsChildren;
		executeStatemant(sqlStmt);
	}
	
	private static void executeStatemant(String sqlStmt) {
		Connection db = null;
		try {
			db = Torque.getConnection(DATABASE_NAME);
			// it's the same name for all tables here, so we don't care
			Statement stmt;
			stmt = db.createStatement();
			stmt.executeUpdate(sqlStmt);
		} catch (TorqueException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} finally {
			Torque.closeConnection(db);
		}
	}

	/**
	 * Gets the root items or children of a parent from a project
	 * The archived/deleted items are also included  
	 * @param parentID
	 * @param projectID
	 * @return
	 */
	private static Integer getNextItemID(Integer projectID){
		Criteria crit = new Criteria();
		crit.add(PROJECTKEY, projectID);
		String max = "max(" + IDNUMBER + ")";
		crit.addSelectColumn(max);
		Integer maxIDNumber = null;
		try {
			List<Record> records = doSelectVillageRecords(crit);
			if (records!=null && !records.isEmpty()) {
				maxIDNumber = records.get(0).getValue(1).asIntegerObj();
			}
		} catch (Exception e) {
			LOGGER.error("Getting the maximal IDNumber for projectID " + projectID + failedWith + e.getMessage(), e);
		}
		if (maxIDNumber==null) {
			return Integer.valueOf(1);
		} else {
			return Integer.valueOf(maxIDNumber.intValue()+1);
		}
	}

	/**
	 * Gets the items from the context in a certain status which were not modified after lastModified
	 * @param workflowContext
	 * @param statusID
	 * @param lastModified
	 * @return
	 */
	public List<TWorkItemBean> getInStatusUnmodifiedAfter(WorkflowContext workflowContext, Integer statusID, Date lastModified) {
		Criteria crit = new Criteria();
		CriteriaUtil.addActiveInactiveProjectCriteria(crit);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		CriteriaUtil.addAccessLevelPublicFilter(crit);
		addWorkflowContextCriteria(crit, workflowContext);
		crit.add(STATE, statusID);
		crit.add(LASTEDIT, lastModified, Criteria.LESS_EQUAL);
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Criteria " + crit);
			}
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the items in status " + statusID + " not modifed after " + lastModified + failedWith + e.getMessage(), e);
			return new ArrayList<TWorkItemBean>();
		}
	}
	
	/**
	 * Gets the itemIDs from the context in a certain status without a status change after lastModified
	 * @param workflowContext
	 * @param statusID
	 * @param lastModified
	 * @return
	 */
	public List<Integer> getInStatusNoStatusChangeAfter(WorkflowContext workflowContext, Integer statusID, Date lastModified) {
		List<Integer> itemIDs = null;
		Criteria crit = new Criteria();
		String maxLastEdit = "MAX(" + THistoryTransactionPeer.LASTEDIT + ")";
		CriteriaUtil.addActiveInactiveProjectCriteria(crit);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		CriteriaUtil.addAccessLevelPublicFilter(crit);
		addWorkflowContextCriteria(crit, workflowContext);
		crit.add(STATE, statusID);
		crit.addJoin(WORKITEMKEY, THistoryTransactionPeer.WORKITEM);
		crit.addJoin(THistoryTransactionPeer.OBJECTID, TFieldChangePeer.HISTORYTRANSACTION);
		crit.add(TFieldChangePeer.FIELDKEY, SystemFields.INTEGER_STATE);
		crit.addSelectColumn(THistoryTransactionPeer.WORKITEM);
		crit.addGroupByColumn(THistoryTransactionPeer.WORKITEM);
		Criterion criterion = crit.getNewCriterion(maxLastEdit,  lastModified, Criteria.LESS_EQUAL);
		crit.addHaving(criterion);
		List<Record> records = new LinkedList<Record>();
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Criteria " + crit);
			}
			records = doSelectVillageRecords(crit);
		} catch(Exception e) {
			LOGGER.error("Groupping the workitems by date of the last modified status before " + lastModified + failedWith + e.getMessage(), e);
		}
		try {
			if (records!=null && !records.isEmpty()) {
				itemIDs = new ArrayList<Integer>(records.size());
				for (Record record : records) {
					Integer itemID = record.getValue(1).asIntegerObj();
					itemIDs.add(itemID);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Loading the items in status " + statusID + " not modifed after " + lastModified + failedWith + e.getMessage(), e);
		}
		return itemIDs;
		
	}
	
	/**
	 * Filter the items by context
	 * @param crit
	 * @param workflowContext
	 * @return
	 */
	private static Criteria addWorkflowContextCriteria(Criteria crit, WorkflowContext workflowContext) {
		Integer itemTypeID = workflowContext.getItemTypeID();
		Integer projectTypeID = workflowContext.getProjectTypeID();
		Integer projectID = workflowContext.getProjectID();
		if (itemTypeID!=null) {
			crit.add(CATEGORYKEY, itemTypeID);
		} 
		if (projectTypeID!=null) {
			crit.addJoin(PROJECTKEY, TProjectPeer.PKEY);
			crit.add(TProjectPeer.PROJECTTYPE, projectTypeID);
		} else {
			if (projectID!=null) {
				crit.add(PROJECTKEY, projectID);
			}
		}
		return crit;
	}
	
}
