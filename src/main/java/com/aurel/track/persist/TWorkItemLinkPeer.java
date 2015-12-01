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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.CriteriaUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.TreeFilterCriteria;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.dao.WorkItemLinkDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.linkType.ILinkType.LINK_DIRECTION;
import com.aurel.track.tql.TqlBL;
import com.aurel.track.util.GeneralUtils;
import com.workingdogs.village.Record;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TWorkItemLinkPeer
	extends com.aurel.track.persist.BaseTWorkItemLinkPeer implements WorkItemLinkDAO {
	
	private static final long serialVersionUID = -1603363788200379070L;
	
	private static final Logger LOGGER = LogManager.getLogger(TWorkItemLinkPeer.class);

	/**
	 * Loads a workItemLinkBean by primary key
	 * @param objectID
	 * @return 
	 */
	@Override
	public TWorkItemLinkBean loadByPrimaryKey(Integer objectID){
		TWorkItemLink link = null;
		try {
			link = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.warn("Loading of a link by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (link!=null) {
			return link.getBean();
		}
		return null;
	}
	
	/**
	 * Loads all workItemLinkBeans
	 * @return 
	 */
	@Override
	public List<TWorkItemLinkBean> loadAllIndexable(){
		Criteria crit = new Criteria();
		Criterion emptyStringCriterion = crit.getNewCriterion(DESCRIPTION, "", Criteria.NOT_EQUAL);
		Criterion nullCriterion = crit.getNewCriterion(DESCRIPTION, (Object)null, Criteria.ISNOTNULL);
		crit.add(emptyStringCriterion.and(nullCriterion));
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all links failed with " + e.getMessage());
			return null;
		}
	}
	
	
	/**
	 * Loads all workItemLinkBeans by link types and direction
	 * @param linkTypeIDs
	 * @param linkDirection
	 * @return 
	 */
	@Override
	public List<TWorkItemLinkBean> loadByLinkTypeAndDirection(List<Integer> linkTypeIDs, Integer linkDirection) {
		if (linkTypeIDs!=null && !linkTypeIDs.isEmpty()) {
			Criteria criteria = new Criteria();
			criteria.addIn(LINKTYPE, linkTypeIDs);
			criteria.add(LINKDIRECTION, linkDirection);
			try {
				return convertTorqueListToBeanList(doSelect(criteria));
			} catch(Exception e) {
				LOGGER.error("Loading the workItemLinkBeans by linkType length " + linkTypeIDs.size() + 
						" and direction " + linkDirection +  " failed with " + e.getMessage(), e);
			}
		}
		return new LinkedList<TWorkItemLinkBean>();
	}
	
	/**
	 * Load all successors for an workItem
	 * @return 
	 */
	@Override
	public List<TWorkItemLinkBean> loadByWorkItemPred(Integer itemID){
		Criteria crit = new Criteria();
		crit.add(LINKPRED, itemID);
		crit.addJoin(LINKSUCC, TWorkItemPeer.WORKITEMKEY);
		crit.addAscendingOrderByColumn(SORTORDER);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading links from predecessor item " + itemID + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Load all predecessors for an workItem
	 */
	@Override
	public List<TWorkItemLinkBean> loadByWorkItemSucc(Integer itemID){
		Criteria crit = new Criteria();
		crit.add(LINKSUCC, itemID);
		crit.addJoin(LINKPRED, TWorkItemPeer.WORKITEMKEY);
		crit.addAscendingOrderByColumn(SORTORDER);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all links to successor item "  + itemID + "  failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Load all successors for an workItem for a link type
	 * @param itemID
	 * @param linkType
	 * @return
	 */
	@Override
	public List<TWorkItemLinkBean> loadByPredAndLinkType(Integer itemID, Integer linkType, Integer direction) {
		Criteria crit = new Criteria();
		crit.add(LINKPRED, itemID);
		crit.addJoin(LINKSUCC, TWorkItemPeer.WORKITEMKEY);
		crit.addAscendingOrderByColumn(SORTORDER);
		crit.add(LINKTYPE, linkType);
		crit.add(LINKDIRECTION, direction);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading links from predecessor item " + itemID + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Load all predecessors for an workItem  for a link type
	 * @param itemID
	 * @param linkType
	 * @return
	 */
	@Override
	public List<TWorkItemLinkBean> loadBySuccAndLinkType(Integer itemID, Integer linkType, Integer direction) {
		Criteria crit = new Criteria();
		crit.add(LINKSUCC, itemID);
		crit.addJoin(LINKPRED, TWorkItemPeer.WORKITEMKEY);
		crit.addAscendingOrderByColumn(SORTORDER);
		crit.add(LINKTYPE, linkType);
		crit.add(LINKDIRECTION, direction);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all links to successor item "  + itemID + "  failed with " + e.getMessage());
			return null;
		}
	}
	
	@Override
	public int countByWorkItem(Integer itemID){
		return countByWorkItemPred(itemID)+countByWorkItemSucc(itemID);
	}
	public int countByWorkItemPred(Integer itemID){
		String COUNT = "count(" + OBJECTID + ")";
		Criteria crit = new Criteria();
		crit.add(LINKPRED, itemID);
		crit.addJoin(LINKSUCC, TWorkItemPeer.WORKITEMKEY);
		crit.addJoin(LINKTYPE,TLinkTypePeer.OBJECTID);
		crit.add(TLinkTypePeer.LINKDIRECTION,LINK_DIRECTION.RIGHT_TO_LEFT,Criteria.NOT_EQUAL);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		crit.addSelectColumn(COUNT);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
		} catch (Exception e) {
			LOGGER.error("Counting links by workItemID " + itemID +  " failed with " + e.getMessage());
			return 0;
		}
	}
	public int countByWorkItemSucc(Integer itemID){
		String COUNT = "count(" + OBJECTID + ")";
		Criteria crit = new Criteria();
		crit.add(LINKSUCC,itemID);
		crit.addJoin(LINKPRED, TWorkItemPeer.WORKITEMKEY);
		crit.addJoin(LINKTYPE,TLinkTypePeer.OBJECTID);
		crit.add(TLinkTypePeer.LINKDIRECTION,LINK_DIRECTION.LEFT_TO_RIGHT,Criteria.NOT_EQUAL);
		CriteriaUtil.addArchivedDeletedFilter(crit);
		crit.addSelectColumn(COUNT);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
		} catch (Exception e) {
			LOGGER.error("Counting links by workItemID " + itemID +  " failed with " + e.getMessage());
			return 0;
		}
	}
	
	/**
	 * Loads all unidirectional links between two workItems. 
	 * @param worItemLinkID 
	 * @param linkPred
	 * @param linkSucc
	 * @param linkTypeIDs
	 * @return
	 */
	@Override
	public List<TWorkItemLinkBean> loadLinksOfWorkItems(Integer worItemLinkID,
			Integer linkPred, Integer linkSucc, List<Integer> linkTypeIDs) {
		List<TWorkItemLink> itemLinks = new ArrayList<TWorkItemLink>();
		Criteria crit = new Criteria();
		if (linkTypeIDs!=null && !linkTypeIDs.isEmpty()) {
			if (worItemLinkID!=null) {
				crit.add(OBJECTID, worItemLinkID, Criteria.NOT_EQUAL);
			}
			crit.addIn(LINKTYPE, linkTypeIDs);
			crit.add(LINKPRED, linkPred);
			crit.add(LINKSUCC, linkSucc);
			try {
				itemLinks = doSelect(crit);
			}
			catch(Exception e) {
				LOGGER.error("Loading all links failed with " + e.getMessage());
			}
		}
		return convertTorqueListToBeanList(itemLinks);
	}
	
	
	/**
	 * Load the directly linked workItems
	 * @param workItemIDChunk base set of workItemIDs
	 * @param linkTypes the link type to look for
	 * @param workItemsArePred
	 * @param direction
	 * @param archived
	 * @param deleted
	 * @return
	 */
	@Override
	public List<TWorkItemLinkBean> getWorkItemsOfDirection(int[] workItemIDChunk, 
			List<Integer> linkTypes, boolean workItemsArePred, Integer direction, Integer archived, Integer deleted) {
		Criteria criteria = new Criteria();
		if (linkTypes==null || linkTypes.isEmpty()) {
			return new ArrayList<TWorkItemLinkBean>();
		}
		criteria.addIn(LINKTYPE, linkTypes);
		String column;
		if (workItemsArePred) {
			column = LINKPRED;
			criteria.addJoin(LINKSUCC, TWorkItemPeer.WORKITEMKEY);
		} else {
			column = LINKSUCC;
			criteria.addJoin(LINKPRED, TWorkItemPeer.WORKITEMKEY);
		}
		criteria.addIn(column, workItemIDChunk);
		CriteriaUtil.addArchivedCriteria(criteria, archived, deleted);
		criteria.add(LINKDIRECTION, direction);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch(Exception e) {
			LOGGER.error("Loading the workItemLinkBeans by workItemKeys of length " + workItemIDChunk.length + 
					" field " + column + " and direction " + direction +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Load the directly linked workItems
	 * @param predChunk
	 * @param succChunk
	 * @param linkType
	 * @param direction
	 * @return
	 */
	@Override
	public List<TWorkItemLinkBean> getLinkedItems(int[] predChunk, int[] succChunk, Integer linkType, Integer direction) {
		Criteria criteria = new Criteria();
		if (predChunk==null || predChunk.length==0 || succChunk==null || succChunk.length==0) {
			return null;
		}
		criteria.addIn(LINKPRED, predChunk);
		criteria.addIn(LINKSUCC, succChunk);
		criteria.add(LINKTYPE, linkType);
		criteria.add(LINKDIRECTION, direction);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch(Exception e) {
			LOGGER.error("Loading the workItemLinkBeans by predecessors of length " + predChunk.length + 
					" predecessors " + succChunk.length + " link type " + linkType + " and direction " + direction +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Load all linked workItems for a list of workItemIDs
	 * @param workItemIDs base set of workItemIDs 
	 * @return
	 */
	@Override
	public List<TWorkItemLinkBean> loadByWorkItems(int[] workItemIDs) {
		List<TWorkItemLinkBean> workItemLinksList = new ArrayList<TWorkItemLinkBean>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return workItemLinksList;
		}
		
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return workItemLinksList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			workItemLinksList.addAll(getWorkItemsOfDirection(workItemIDChunk, LINKPRED));
			workItemLinksList.addAll(getWorkItemsOfDirection(workItemIDChunk, LINKSUCC));
		}
		return workItemLinksList;
	}

	private List<TWorkItemLinkBean> getWorkItemsOfDirection(int[] workItemIDChunk, String column) {
		Criteria criteria = new Criteria();
		criteria.addIn(column, workItemIDChunk);
		if (LINKPRED.equals(column)) {
			criteria.addJoin(LINKSUCC, TWorkItemPeer.WORKITEMKEY);
		} else {
			criteria.addJoin(LINKPRED, TWorkItemPeer.WORKITEMKEY);
		}
		CriteriaUtil.addArchivedDeletedFilter(criteria);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch(Exception e) {
			LOGGER.error("Loading the workItemLinkBeans by workItemKeys of length " + workItemIDChunk.length + 
					" field " + column + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the classBeans for a prepared criteria
	 * @param preparedCriteria
	 * @return
	 * @throws TorqueException
	 */
	private static List<TWorkItemLinkBean> getFilterLinks(Criteria preparedCriteriaPred,  Criteria preparedCriteriaSucc) throws TorqueException {
		List<TWorkItemLinkBean> workItemLinksList = new LinkedList<TWorkItemLinkBean>();
		preparedCriteriaPred.addJoin(TWorkItemPeer.WORKITEMKEY, LINKPRED);
		List<TWorkItemLinkBean> predecessorItemLinks = convertTorqueListToBeanList(doSelect(preparedCriteriaPred));
		if (predecessorItemLinks!=null) {
			workItemLinksList.addAll(predecessorItemLinks);
		}
		preparedCriteriaSucc.addJoin(TWorkItemPeer.WORKITEMKEY, LINKSUCC);
		List<TWorkItemLinkBean> successorItemLinks = convertTorqueListToBeanList(doSelect(preparedCriteriaSucc));
		if (successorItemLinks!=null) {
			workItemLinksList.addAll(successorItemLinks);
		}
		return workItemLinksList;
	}
	
	/**
	 * Gets the links filtered by filterSelectsTO and raciBean
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	@Override
	public List<TWorkItemLinkBean> loadTreeFilterLinks(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID) {
		Integer[] selectedProjects = filterUpperTO.getSelectedProjects();
		if (selectedProjects==null  || selectedProjects.length==0) {
			//at least one selected project needed
			return new ArrayList<TWorkItemLinkBean>();
		}		
		Criteria critPred = TreeFilterCriteria.prepareTreeFilterCriteria(filterUpperTO, raciBean, personID);
		Criteria critSucc = TreeFilterCriteria.prepareTreeFilterCriteria(filterUpperTO, raciBean, personID);
		try {
			return getFilterLinks(critPred, critSucc);
		} catch (TorqueException e) {
			LOGGER.error("Loading the links for tree filter failed with " + e.getMessage());
			return new ArrayList<TWorkItemLinkBean>();
		}
	}
	
	/**
	 * Get the links for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	@Override
	public List<TWorkItemLinkBean> loadTQLFilterLinks(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors) {
		Criteria critPred = TqlBL.createCriteria(tqlExpression, personBean, locale, errors);
		Criteria critSucc = TqlBL.createCriteria(tqlExpression, personBean, locale, errors);
		try {
			return getFilterLinks(critPred, critSucc);
		} catch (TorqueException e) {
			LOGGER.error("Loading the links for TQL filter " + tqlExpression + " failed with " + e.getMessage());
			return new ArrayList<TWorkItemLinkBean>();
		}
	}
	
	@Override
	public List<TWorkItemLinkBean> loadByIDs(List<Integer> linkIDs) {
		List<TWorkItemLinkBean> workItemLinksBeanList = new ArrayList<TWorkItemLinkBean>();
		if (linkIDs == null || linkIDs.isEmpty()) {
			return workItemLinksBeanList;
		}
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(linkIDs);
		if (workItemIDChunksList==null) {
			return workItemLinksBeanList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();		
			Criteria crit = new Criteria();
			crit.addIn(OBJECTID, workItemIDChunk);
			crit.addAscendingOrderByColumn(SORTORDER);
			try {
				workItemLinksBeanList.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (TorqueException e) {
				LOGGER.error("Error while getting link beans: " + e.getMessage());
			}
		}
		return workItemLinksBeanList;
	}
	
	/**
	 * Gets the sort order column name 
	 * @return
	 */
	@Override
	public String getSortOrderColumn() {
		return "SORTORDER";
	}
	
	/**
	 * Returns the table name
	 * @return
	 */
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	
	/**
	 * Saves a new/existing workItemLinkBean
	 * @param workItemLinkBean
	 * @return the created optionID
	 */
	@Override
	public Integer save(TWorkItemLinkBean workItemLinkBean){
		try {
			TWorkItemLink tWorkItemLink = BaseTWorkItemLink.createTWorkItemLink(workItemLinkBean);
			tWorkItemLink.save();
			Integer objectID = tWorkItemLink.getObjectID();
			workItemLinkBean.setObjectID(objectID);
			return objectID;
		} catch (Exception e) {
			LOGGER.error("Saving of a link failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Deletes a workItemLinkBean
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID){
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the link with id " + objectID + " failed with: " + e);
		}
	}
	
	
	private static List<TWorkItemLinkBean> convertTorqueListToBeanList(List<TWorkItemLink> torqueList) {
		List<TWorkItemLinkBean> beanList = new LinkedList<TWorkItemLinkBean>();
		if (torqueList!=null){
			for (TWorkItemLink tWorkItemLink : torqueList) {
				beanList.add(tWorkItemLink.getBean());
			}
		}
		return beanList;
	}

}
