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


package com.aurel.track.persist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.TreeFilterCriteria;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.NotifyDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.tql.TqlBL;
import com.aurel.track.util.GeneralUtils;
import com.workingdogs.village.Record;


public class TNotifyPeer 
	extends com.aurel.track.persist.BaseTNotifyPeer
	implements NotifyDAO
	{

	private static final long serialVersionUID = -3589988373684256469L;

	private static final Logger LOGGER = LogManager.getLogger(TNotifyPeer.class);
	
	/**
	 * Whether the person has a specific raci Role for a workItem 
	 * @param workItemKey
	 * @param personKey
	 * @param raciRole if null it doesn't matter
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean hasDirectRaciRole(Integer workItemKey, Integer personKey, String raciRole) {
		List watchers = null;
		Criteria crit = new Criteria();
		crit.add(WORKITEM, workItemKey);
		crit.add(PERSONKEY, personKey);
		if (raciRole!=null) {
			crit.add(RACIROLE, raciRole);
		}
		try {
			watchers = doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Verifying the direct raci role for person " + personKey + 
					" in workItem " + workItemKey + " failed with  " + e.getMessage(), e);
		}
		if (watchers==null || watchers.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Whether any person from list has a specific raci Role for a workItem directly (not through group)
	 * @param workItemKey
	 * @param personIDs
	 * @param raciRole if null it doesn't matter
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean anyPersonHasDirectRaciRole(Integer workItemKey, List<Integer> personIDs, String raciRole) {
		List watchers = null;
		Criteria crit = new Criteria();
		crit.add(WORKITEM, workItemKey);
		crit.addIn(PERSONKEY, personIDs);
		if (raciRole!=null) {
			crit.add(RACIROLE, raciRole);
		}
		try {
			watchers = doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Verifying the direct raci role for " + personIDs.size() + 
					" persons in workItem " + workItemKey + " failed with  " + e.getMessage(), e);
		}
		if (watchers==null || watchers.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Whether any person from list has a specific raci Role for a workItem through group
	 * @param workItemKey
	 * @param personIDs
	 * @param raciRole if null it doesn't matter
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean anyPersonHasIndirectRaciRole(Integer workItemKey, List<Integer> personIDs, String raciRole) {
		List watchers = null;
		Criteria crit = new Criteria();
		crit.addJoin(PERSONKEY, TGroupMemberPeer.THEGROUP);
		crit.addIn(TGroupMemberPeer.PERSON, personIDs);
		crit.add(WORKITEM, workItemKey);
		if (raciRole!=null) {
			crit.add(RACIROLE, raciRole);
		}
		try {
			watchers = doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Verifying the indirect raci role for person " + personIDs.size() + 
					" persons in workItem " + workItemKey + " failed with  " + e.getMessage(), e);
		}
		if (watchers==null || watchers.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Deletes a raci role for a list of persons for a workItem
	 * @param workItemKey
	 * @param persons
	 * @param raciRole
	 */
	@Override
	public void deleteByWorkItemAndPersonsAndRaciRole(Integer workItemKey, Integer[] persons, String raciRole) {
		if (workItemKey!=null && persons!=null && persons.length>0) {
			Criteria crit = new Criteria();
			crit.add(WORKITEM, workItemKey);
			crit.addIn(PERSONKEY, persons);
			crit.add(RACIROLE, raciRole);
			try {			
				doDelete(crit);
			} catch (TorqueException te) {
				LOGGER.error("Could not delete RACI role " + raciRole + " for workItem " + workItemKey + 
						" and persons " + persons + ": " + te.getMessage(), te);
			}
		}
	}
		
	/**
	 * Deletes all persons from a raci role for a workItem
	 * @param workItemKey	
	 * @param raciRole
	 */
	@Override
	public void deleteByWorkItemAndRaciRole(Integer workItemKey, String raciRole) {
		if (workItemKey!=null) {
			Criteria crit = new Criteria();
			crit.add(WORKITEM, workItemKey);
			crit.add(RACIROLE, raciRole);
			try {
				doDelete(crit);
			} catch (TorqueException te) {
				LOGGER.error("Could not delete RACI role " + raciRole + " for workItem " + workItemKey + 
						" failed with " + te.getMessage(), te);
			}
		}
	}
	
	/**
	 * Inserts a raci role for a person in a workItem
	 * @param workItemKey
	 * @param person
	 * @param raciRole
	 */
	@Override
	public void insertByWorkItemAndPersonAndRaciRole(Integer workItemKey, Integer person, String raciRole) {
		if (workItemKey!=null && person!=null) {
			Criteria crit = new Criteria();
			crit.add(WORKITEM, workItemKey);
			crit.add(PERSONKEY, person);
			crit.add(RACIROLE, raciRole);
			try {
				List notifies = doSelect(crit);
				if (notifies ==null || notifies.isEmpty()) {
					TNotifyBean notifyBean = new TNotifyBean();
					notifyBean.setWorkItem(workItemKey);
					notifyBean.setPersonID(person);
					notifyBean.setRaciRole(raciRole);
					save(notifyBean);
				}
			} catch (TorqueException te) {
				LOGGER.error("Could not insert RACI role " + raciRole + " for workItem " + workItemKey + 
						" and person " + person + " failed with " + te.getMessage(), te);
			}
		}
	}

	/**
	 * Saves a notifyBean in the TNotify table
	 * @param fieldBean
	 * @return
	 */
	public Integer save(TNotifyBean notifyBean) {
		TNotify tNotify;
		try {
			tNotify = BaseTNotify.createTNotify(notifyBean);
			tNotify.save();
			return tNotify.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a notify failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets the classBeans for a prepared criteria
	 * @param preparedCriteria
	 * @return
	 * @throws TorqueException
	 */
	private static List<TNotifyBean> getFilterWatchers(Criteria preparedCriteria) throws TorqueException {
		preparedCriteria.addJoin(TWorkItemPeer.WORKITEMKEY,  WORKITEM);
		preparedCriteria.add(RACIROLE, (Object)null, Criteria.ISNOTNULL);
		return convertTorqueListToBeanList(doSelect(preparedCriteria));
	}
	
	/**
	 * Gets the watchers filtered by filterSelectsTO and raciBean
	 * @param filterSelectsTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	@Override
	public List<TNotifyBean> loadTreeFilterWatchers(FilterUpperTO filterSelectsTO, RACIBean raciBean, Integer personID) {
		Integer[] selectedProjects = filterSelectsTO.getSelectedProjects();
		if (selectedProjects==null  || selectedProjects.length==0) {
			//at least one selected project needed
			return new ArrayList<TNotifyBean>();
		}		
		Criteria crit = TreeFilterCriteria.prepareTreeFilterCriteria(filterSelectsTO, raciBean, personID);
		try {
			return getFilterWatchers(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading the watchers for tree filter failed with " + e.getMessage());
			return new ArrayList<TNotifyBean>();
		}
	}
	
	/**
	 * Get the watchers for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	@Override
	public List<TNotifyBean> loadTQLFilterWatchers(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors) {
		Criteria crit = TqlBL.createCriteria(tqlExpression, personBean, locale, errors);
		try {
			return getFilterWatchers(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading the watchers for TQL filter " + tqlExpression + " failed with " + e.getMessage());
			return new ArrayList<TNotifyBean>();
		}
	}
	
	/**
	 * Loads the list of TNotifyBeans for all the workItems the 
	 * person is originator or manager or responsible for 		 
	 * @param personID
	 */
	
	
	/**
	 * Loads the list of TNotifyBeans filtered by the FilterSelectsTO
	 * @param filterSelectsTO
	 * @return
	 */
	
	/**
	 * Loads the list of TNotifyBeans associated with items
	 * @param workItemIDs
	 * @param raciRole
	 * @return
	 */
	@Override
	public List<TNotifyBean> loadWatcherByItems(List<Integer> workItemIDs, String raciRole) {
		List<TNotifyBean> consInfList = new LinkedList<TNotifyBean>();
		if (workItemIDs==null || workItemIDs.isEmpty()) {
			return consInfList;
		}	
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return consInfList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();
			
			criteria.addIn(WORKITEM, workItemIDChunk);
			if (raciRole==null) {
				criteria.add(RACIROLE, (Object)null, Criteria.ISNOTNULL);
			} else {
				criteria.add(RACIROLE, raciRole);
			}
			try {
				consInfList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the watchers by items failed with " + e.getMessage());
			}
		}
		return consInfList;
	}
	
	/**
	 * Loads the list of TNotifyBeans associated with an array of workItems
	 * @param workItemIDs
	 * @return
	 */
	@Override
	public List<TNotifyBean> loadLuceneConsInf(int[] workItemIDs) {
		List<TNotifyBean> consInfList = new LinkedList<TNotifyBean>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return consInfList;
		}	
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return consInfList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(TWorkItemPeer.WORKITEMKEY, workItemIDChunk);
			try {
				consInfList.addAll(getFilterWatchers(criteria));
			} catch(Exception e) {
				LOGGER.error("Loading the lucene report consultants/informants (by workItemKeys) failed with " + e.getMessage());
			}
		}
		return consInfList;
	}
	
	/**
	 * Get the TNotifyBeans filtered by a TQL expression
	 * It is made static and not memeber of the NotifyDAO 
	 * because the Criteria parameter would make
	 * the DAO interface dependent on Torque 
	 * @param tqlCriteria
	 * @return
	 */

	/************************************************************************
	 * Get consulted/informed beans to calculate read/edit right for person *
	 ***********************************************************************/
	
	/**
	 * Gets the set of issue oids from a project/release ID list
	 * for which the person or a group it belongs to has consultant or informant role 
	 * @param projectReleaseIDList
	 * @param personIDs
	 * @param isProject <ul><li>true: projectReleaseID is a projectID</li>
	 * 					<li>false: projectReleaseID is a releaseID false</li></ul>
	 */
	
	
	/**
	 * Gets the list of TNotifyBeans filtered by FilterSelectsTO 
	 * for which the person or a group it belongs to has consultant or informant role 
	 * @param filterSelectsTO
	 * @param personIDs
	 * @return
	 */
	
	/**
	 * Gets the list of TNotifyBeans by a list of workItemIDs 
	 * for which the person (or a group it belongs to) has consultant or informant role 
	 * @param workItemIDs
	 * @param personIDs
	 * @return
	 */
	@Override
	public List<TNotifyBean> getByWorkItemsAndPersons(List<Integer> workItemIDs, List<Integer> personIDs) {
		List<TNotifyBean> notifyBeans = new LinkedList<TNotifyBean>();
		if (workItemIDs==null || workItemIDs.isEmpty()) {
			return notifyBeans;
		}
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return notifyBeans;
		}
		Iterator<int[]> chunkIterator = workItemIDChunksList.iterator();
		while (chunkIterator.hasNext()) {
			int[] workItemIDChunk = (int[])chunkIterator.next();
			Criteria crit = new Criteria();
			crit.addIn(TWorkItemPeer.WORKITEMKEY,  workItemIDChunk);
			crit.addJoin(TWorkItemPeer.WORKITEMKEY,  WORKITEM);
			crit.addIn(TNotifyPeer.PERSONKEY, personIDs);
			try {
				notifyBeans.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (TorqueException e) {
				LOGGER.error("Loading the direct consultant/informant workItemIDs for lucene report failed with failed with " + e.getMessage());			
			}
		}
		return notifyBeans;
	}
	
	/** Gets the list of TNotifyBeans filterd by TQL 
	 * for which the person or a group it belongs to has consultant or informant role 
	 * It is made static and not member of the WorkItemDAO 
	 * because the Criteria parameter would make
	 * the DAO interface dependent on Torque 
	 * @param tqlCriteria
	 * @param personIDs
	 * @return
	 */

	@Override
	public int countDirectWatcherPersons(Integer workItemID){
		String COUNT = "count(" + PKEY + ")";
		Criteria crit = new Criteria();
		crit.add(WORKITEM, workItemID);
		crit.addSelectColumn(COUNT);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
		} catch (Exception e) {
			LOGGER.error("countDirectWatcherPersons by workItemID " + workItemID +  " failed with " + e.getMessage());
			return 0;
		}
	}

	
	
	/**
	 * Converts a list of TNotify torque objects to a list of TNotifyBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TNotifyBean> convertTorqueListToBeanList(List<TNotify> torqueList) {
		List<TNotifyBean> beanList = new LinkedList<TNotifyBean>();	
		if (torqueList!=null) {
			for (TNotify tNotify : torqueList) {
				beanList.add(tNotify.getBean());
			}
		}
		return beanList;
	}
}
