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
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.dao.OptionDAO;
import com.aurel.track.util.GeneralUtils;
import com.workingdogs.village.Record;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TOptionPeer
	extends com.aurel.track.persist.BaseTOptionPeer implements OptionDAO
{	
	private static final long serialVersionUID = 500L;

	private static final Logger LOGGER = LogManager.getLogger(TOptionPeer.class);
	
	private static Class[] dependentPeerClasses = {
		TAttributeValuePeer.class, 
		TFieldChangePeer.class,
		TFieldChangePeer.class
	};
	
	private static String[] dependentFields = {
		TAttributeValuePeer.CUSTOMOPTIONID,
		TFieldChangePeer.OLDCUSTOMOPTIONID,
		TFieldChangePeer.NEWCUSTOMOPTIONID
	};
	
	private static Class[] deletePeerClasses = {
		TConfigOptionsRolePeer.class,
		TFieldChangePeer.class,
		TFieldChangePeer.class,
		TOptionPeer.class
	};
	
	private static String[] deleteFields = {
		TConfigOptionsRolePeer.ROLEKEY,
		TFieldChangePeer.OLDCUSTOMOPTIONID,
		TFieldChangePeer.NEWCUSTOMOPTIONID,
		TOptionPeer.OBJECTID
	};
	
	/**
	 * Loads an option by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TOptionBean loadByPrimaryKey(Integer objectID) {
		TOption tOption = null;
		try {
			tOption = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.info("Loading of an option by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tOption!=null) {
			return tOption.getBean();
		}
		return null;
	}
	
	/**
	 * Gets an optionBean by label
	 * @param list
	 * @param parentID
	 * @param label
	 * @return
	 */
	@Override
	public List<TOptionBean> loadByLabel(Integer list, Integer parentID, String label) {
		Criteria crit = new Criteria();
		crit.add(LIST, list);
		if (parentID==null) {
			crit.add(PARENTOPTION, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(PARENTOPTION, parentID);
		}
		crit.add(LABEL, label);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the option by list " + list + 
					" and label " + label +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the label beans with a certain symbol
	 * @param listID
	 * @param symbol
	 * @param excludeObjectID
	 * @return
	 */
	
	/**
	 * Gets the optionBeans by uuid list
	 * @param uuids
	 * @return
	 */
	@Override
	public List<TOptionBean> loadByUUIDs(List<String> uuids) {
		List<TOptionBean> optionBeanList = new LinkedList<TOptionBean>();
		if (uuids==null || uuids.isEmpty()) {
			return new ArrayList<TOptionBean>();
		}
		Criteria criteria;
		List<List<String>> uuidChunksList = GeneralUtils.getListOfStringChunks(uuids);
		if (uuidChunksList==null) {
			return optionBeanList;
		}
		Iterator<List<String>> iterator = uuidChunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			List<String> uuidChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(TPUUID, uuidChunk);
			try {
				optionBeanList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the optionBeans by uuids and the chunk number " + 
						i + " of length  "  + uuidChunk.size() + " failed with " + e.getMessage(), e);
			}
		}
		return optionBeanList;
	}
	
	/**
	 * Load all options
	 * @return
	 */
	@Override
	public List<TOptionBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all custom options failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Load by keys
	 * @param objectIDs
	 * @return
	 */
	@Override
	public List<TOptionBean> loadByKeys(Integer[] objectIDs) {
		if (objectIDs==null || objectIDs.length==0) {
			return new LinkedList<TOptionBean>();
		}
		Criteria crit = new Criteria();
		crit.addIn(OBJECTID, objectIDs);
		crit.addAscendingOrderByColumn(BaseTOptionPeer.SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading options by keys  " + objectIDs +  " failed with: " + e);
			return null;
		}
	}
	
	/**
	 * Saves a new/existing optionBean in the TOption table
	 * @param listBean
	 * @return the created optionID
	 */
	@Override
	public Integer save(TOptionBean optionBean) {
		try {
			if (optionBean.getIsDefault() == null) {
				optionBean.setIsDefault("N");
			}
			if (optionBean.getDeleted() == null) {
				optionBean.setDeleted("N");
			}
			TOption tOption = BaseTOption.createTOption(optionBean);
			tOption.save();
			return tOption.getObjectID();
			//return objectID;
		} catch (Exception e) {
			LOGGER.error("Saving of an option failed with " + e.getMessage());
			return null;
		}
	}
	
	@Override
	public boolean hasDependentData(Integer pkey) {
		return ReflectionHelper.hasDependentData(dependentPeerClasses, dependentFields, pkey);
	}
	
	/**
	 * Deletes an option by primary key
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		List<Integer> customSelectFieldIDs = FieldBL.getCustomSelectFieldIDs();
		if (customSelectFieldIDs!=null) {
			for (Integer customSelectFieldID : customSelectFieldIDs) {
				new TCardFieldOptionPeer().deleteOptionForField(customSelectFieldID, objectID);
			}
		}
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	/**
	 * Deletes all the optionBeans belonging to a list from TOption table
	 * @param objectID
	 */
	@Override
	public void deleteByList(Integer listID) {
		Criteria crit = new Criteria();
		crit.add(LIST, listID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting all options for the list " + listID + " failed with: " + e);
		}
	}
		
	/**
	 * Gets the active optionBean objects from the TOption table 
	 * by listID, ordered by sortorder field  
	 * @param listID
	 * @return
	 */
	@Override
	public List<TOptionBean> loadActiveByListOrderedBySortorder(Integer listID) {
		Criteria crit = new Criteria();
		crit.add(LIST, listID);
		crit.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
		crit.addAscendingOrderByColumn(BaseTOptionPeer.SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading active options for the list " + listID +  " ordered by sortorder failed with: " + e);
			return null;
		}
	}
	
	/**
 	 * Gets the active optionBean objects from the TOption table 
	 * by listID and parentID, ordered by sortorder field  
	 * Used when two or more lists might have the same parent
	 * @param listID
	 * @param parentID
	 * @return
	 */
	@Override
	public List<TOptionBean> loadActiveByListAndParentOrderedBySortorder(Integer listID, Integer parentID) {
		Criteria crit = new Criteria();
		crit.add(LIST, listID);
		crit.add(PARENTOPTION, parentID);
		crit.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
		crit.addAscendingOrderByColumn(BaseTOptionPeer.SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all options for the list " + listID +
					" and parent " + parentID + " ordered by sortorder failed with: " + e);
			return null;
		}
	}
	
	/**
	 * Gets the active optionBean objects from the TOption table 
	 * by listID, ordered by label field  
	 * @param listID
	 * @param ascending
	 * @return
	 */
	@Override
	public List<TOptionBean> loadActiveByListOrderedByLabel(Integer listID, boolean ascending) {
		Criteria crit = new Criteria();
		crit.add(LIST, listID);
		crit.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
		if (ascending) {
			crit.addAscendingOrderByColumn(LABEL);
		} else {
			crit.addDescendingOrderByColumn(LABEL);
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading active options for the list " + listID + " ordered by label failed with: " + e);
			return null;
		}
	}
	
	/**
 	 * Gets the active optionBean objects from the TOption table 
	 * by listID and parentID, ordered by label field  
	 * Used when two or more lists might have the same parent
	 * @param listID
	 * @param parentID
	 * @param ascending
	 * @return
	 */
	@Override
	public List<TOptionBean> loadActiveByListAndParentOrderedByLabel(Integer listID, Integer parentID, boolean ascending) {
		Criteria crit = new Criteria();
		crit.add(LIST, listID);
		crit.add(PARENTOPTION, parentID);
		crit.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
		if (ascending)
		{
			crit.addAscendingOrderByColumn(LABEL);
		}
		else
		{
			crit.addDescendingOrderByColumn(LABEL);
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all options for the list " + listID +
					" and parent " + parentID + " ordered by label failed with: " + e);
			return null;
		}
	}
	
	/**
	 * Gets the last used sort order for a listID
	 * @param listID
	 * @return
	 */
	@Override
	public Integer getNextSortOrder(Integer listID) {
		Integer sortOrder = null;
		String max = "max(" + SORTORDER + ")";
		Criteria crit = new Criteria();
		crit.add(LIST, listID);
		crit.addSelectColumn(max);
		try {
			sortOrder = ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (Exception e) {
			LOGGER.error("Getting the next sortorder for the list " + listID + " failed with: " + e);
		}
		if (sortOrder==null) {
			return Integer.valueOf(1);
		} else {
			return Integer.valueOf(sortOrder.intValue()+1);
		}
	}
	
	/**
	 * Gets the last used sort order for a listID and parentID 
	 * @param listID
	 * @param parentID
	 * @return
	 */
	@Override
	public Integer getNextSortOrder(Integer listID, Integer parentID) {
		Integer sortOrder = null;
		String max = "max(" + SORTORDER + ")";
		Criteria crit = new Criteria();
		crit.add(LIST, listID);
		crit.add(PARENTOPTION, parentID);
		crit.addSelectColumn(max);
		try {
			sortOrder = ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (Exception e) {
			LOGGER.error("Getting the next sortorder for the list " + listID + " and parent " + parentID + " failed with: " + e);
		}
		if (sortOrder==null) {
			return Integer.valueOf(1);
		} else {
			return Integer.valueOf(sortOrder.intValue()+1);
		}
	}
	
	/**
	 * Gets a list of optionBeans from the TOption table by the parentID
	 * @param parentID
	 * @return
	 */
	@Override
	public List<TOptionBean> getOptionsByParent(Integer parentID) {
		Criteria crit = new Criteria();
		crit.add(PARENTOPTION, parentID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the optionsIDs by parentoption " + parentID + " failed with: " + e);
			return null;
		}
	}
			
	/**
	 * Gets the list of default optionIDs from the TOption by listID 
	 * @param listID
	 * @return
	 */
	@Override
	public Integer[] loadDefaultIDsForList(Integer listID){
		Integer[] optionIDs = new Integer[0];
		List<TOption> optionRecordList = null;
		Criteria crit = new Criteria();
		crit.add(LIST, listID);
		crit.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
		crit.add(ISDEFAULT, "Y");
		try {
			optionRecordList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading the default optionsIDs for list " + listID + " failed with: " + e);
		}
		if (optionRecordList!=null && !optionRecordList.isEmpty()) {
			optionIDs = new Integer[optionRecordList.size()];
			Iterator<TOption> itrOptionRecordList = optionRecordList.iterator();
			int i = 0;
			while (itrOptionRecordList.hasNext()) {
				TOption tOption = itrOptionRecordList.next();
				optionIDs[i++] = tOption.getObjectID();
			}
		}
		return optionIDs;
	}
	
	
	/**
	 * Gets the list of default optionIDs from the TOption by listID and parentIDs
	 * @param listID
	 * @param parentIDs
	 * @return
	 */
	@Override
	public Integer[] loadDefaultIDsForListAndParents(Integer listID, Integer[] parentIDs) {
		Integer[] optionIDs = new Integer[0];
		if (listID!=null && parentIDs!=null && parentIDs.length>0) {
			List<TOption> optionRecordList = null;
			Criteria crit = new Criteria();
			crit.add(LIST, listID);
			crit.addIn(PARENTOPTION, parentIDs);
			crit.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
			crit.add(ISDEFAULT, "Y");
			try {
				optionRecordList = doSelect(crit);
			} catch (TorqueException e) {
				LOGGER.error("Loading the default optionsIDs for list " + listID + " and parentIDs " + parentIDs + " failed with: " + e);
			}
			if (optionRecordList!=null && !optionRecordList.isEmpty()) {
				optionIDs = new Integer[optionRecordList.size()];
				Iterator<TOption> itrOptionRecordList = optionRecordList.iterator();
				int i = 0;
				while (itrOptionRecordList.hasNext()) {
					TOption tOption = itrOptionRecordList.next();
						optionIDs[i++] = tOption.getObjectID();
				}
			}
		}
		return optionIDs;
	}

	/**
	 * Loads the create datasource for a list as a list of LabelValueBeans
	 * @param listID
	 * @return
	 */
	@Override
	public List<TOptionBean> loadDataSourceByList(Integer listID) {
		Criteria crit = new Criteria();
		crit.add(LIST, listID);
		crit.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
		crit.addAscendingOrderByColumn(SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the create data source for list " + listID + " failed with: " + e);
			return null;
		}
	}
	
	/**
	 * Loads the create datasource for a listID and a parents
	 * @param listID
	 * @param parentIDs
	 * @return
	 */
	@Override
	public List<TOptionBean> loadCreateDataSourceByListAndParents(Integer listID, Integer[] parentIDs) {
		if (listID!=null && parentIDs!=null && parentIDs.length>0) {
			Criteria crit = new Criteria();
			crit.add(LIST, listID);
			crit.addIn(PARENTOPTION, parentIDs);
			crit.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
			crit.addAscendingOrderByColumn(SORTORDER);
			try {
				return convertTorqueListToBeanList(doSelect(crit));
			} catch (TorqueException e) {
				LOGGER.error("Loading the create data source for list " + listID + " and parents " + parentIDs + " failed with: " + e);
			}
		}
		return new LinkedList<TOptionBean>();
	}

	
	/**
	 * Loads the edit datasource for a listID, parents and current selections
	 * @param listID
	 * @param parentIDs
	 * @param objectIDs
	 * @return
	 */
	@Override
	public List<TOptionBean> loadEditDataSourceByListAndParents(Integer listID, Integer[] parentIDs, Integer[] objectIDs) {
		if (listID!=null && parentIDs!=null && parentIDs.length>0) {
			Criteria crit = new Criteria();
			Criterion criterionList = crit.getNewCriterion(LIST, listID, Criteria.EQUAL);
			Criterion criterionParent = crit.getNewCriterion(PARENTOPTION, parentIDs, Criteria.IN);
			if(objectIDs!=null&&objectIDs.length>0){
				Criterion criterionObject = crit.getNewCriterion(OBJECTID, objectIDs, Criteria.IN);
				crit.add(criterionList.and(criterionParent).or(criterionObject));
			}else{
				crit.add(criterionList.and(criterionParent));
			}
			crit.add(DELETED, (Object)"Y", Criteria.NOT_EQUAL);
			crit.addAscendingOrderByColumn(SORTORDER);
			try {
				return convertTorqueListToBeanList(doSelect(crit));
			} catch (TorqueException e) {
				LOGGER.error("Loading the edit data source for list " + listID + " and parent " + parentIDs + " failed with: " + e);
			}
		}
		return new LinkedList<TOptionBean>();
	}
	
	
	
	/*********************************************************
	* Manager-, Responsible-, My- and Custom Reports methods * 
	*********************************************************/
	
	/**
	 * Gets the optionBeans for a prepared criteria
	 * @param preparedCriteria
	 * @return
	 * @throws TorqueException
	 */
	private static List<TOptionBean> getReportOptions(Criteria preparedCriteria) throws TorqueException {
		preparedCriteria.addJoin(BaseTWorkItemPeer.WORKITEMKEY,  BaseTAttributeValuePeer.WORKITEM);
		preparedCriteria.addJoin(BaseTAttributeValuePeer.CUSTOMOPTIONID,  BaseTOptionPeer.OBJECTID);
		return convertTorqueListToBeanList(doSelect(preparedCriteria));
	}
	
	/**
	 * Get the optionBeans associated with workItems from a project
	 * @param projectID
	 * @return
	 */
	
	/**
	 * Get the optionBeans associated with workItems from a release
	 * @return
	 */
	
	/**
	 * Get the optionBeans associated with workItems the person is manager for
	 * @param personID
	 * @return
	 */

	/**
	 * Get the optionBeans associated with workItems the person is responsible for
	 * @param personID
	 * @return
	 */
	
	/**
	 * Get the optionBeans associated with workItems the person is originator for
	 * @param personID
	 * @return
	 */
	
	/**
	 * Get the optionBeans associated with workItems the person is manager or responsible or owner for
	 * @param personID 
	 * @return
	 */
		
	/**
	 * Get the optionBeans filtered by the FilterSelectsTO
	 * @param filterSelectsTO
	 * @return
	 */
	
	/**
	 * Get the optionBeans filtered by a TQL expression
	 * It is made static and not memeber of the OptionDAO 
	 * because the Criteria parameter would make
	 * the DAO interface dependent on Torque 
	 * @param tqlCriteria
	 * @return
	 */
	
	/**
	 * Get the optionBeans for an array of workItemIDs 
	 * @param workItemIDs
	 * @return
	 */
	@Override
	public List<TOptionBean> loadLuceneOptions(int[] workItemIDs) {
		List<TOptionBean> attributeValueBeansList = new LinkedList<TOptionBean>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return attributeValueBeansList;
		}
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return attributeValueBeansList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(BaseTAttributeValuePeer.WORKITEM, workItemIDChunk);
			try {
				attributeValueBeansList.addAll(getReportOptions(criteria));
			} catch(Exception e) {
				LOGGER.error("Loading the optionBeans by workItemIDs failed with " + e.getMessage());
			}
		}
		return attributeValueBeansList;
	}
	
	/**
	 * Get the optionBeans for the history of workItemIDs
	 * @param workItemIDs
	 * @param personID
	 * @return
	 */
	@Override
	public Map<Integer, TOptionBean> loadHistoryOptions(int[] workItemIDs) {
		List<TOption> torqueList = new LinkedList<TOption>();
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList!=null && !workItemIDChunksList.isEmpty()) {
			Criteria criteria;
			Iterator<int[]> iterator = workItemIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] workItemIDChunk = iterator.next();
				criteria = HistoryDropdownContainerLoader.prepareHistoryCustomOptionCriteria(workItemIDChunk, true);
				try {
					torqueList.addAll(doSelect(criteria));
				} catch(Exception e) {
					LOGGER.error("Loading the new history optionBeans for workItems failed with " + e.getMessage());
				}
				criteria = HistoryDropdownContainerLoader.prepareHistoryCustomOptionCriteria(workItemIDChunk, false);
				try {
					torqueList.addAll(doSelect(criteria));
				} catch(Exception e) {
					LOGGER.error("Loading the old history optionBeans for workItems failed with " + e.getMessage());
				}
				
			}
		}
		return GeneralUtils.createMapFromList(convertTorqueListToBeanList(torqueList));
	}
	
	/**
	 * Returns the sort order column name
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
	 * Converts a list of TOption torque objects to a list of TOptionBean objects 
	 * @param torqueList
	 * @return
	 */
	public static List<TOptionBean> convertTorqueListToBeanList(List<TOption> torqueList) {
		List<TOptionBean> beanList = new LinkedList<TOptionBean>();
		TOption tOption;
		if (torqueList!=null) {
			Iterator<TOption> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()) {
				tOption = itrTorqueList.next();
				beanList.add(tOption.getBean());
			}
		}
		return beanList;
	}	
	
	/**
	 * Whether any option from a list is assigned to history entry
	 * @param listID
	 * @param newValue
	 * @return
	 */
	@Override
	public boolean isListAssignedToHistoryEntry(Integer listID, boolean newValue) {
		List childLists = null;
		Criteria criteria = new Criteria();
		if (newValue) {
			criteria.addJoin(TFieldChangePeer.NEWCUSTOMOPTIONID, TOptionPeer.OBJECTID);
		} else {
			criteria.addJoin(TFieldChangePeer.OLDCUSTOMOPTIONID, TOptionPeer.OBJECTID);
		}
		criteria.add(TOptionPeer.LIST, listID);
		try {
			childLists = doSelect(criteria);
		}
		catch (TorqueException e) {
			LOGGER.error("Is assigned to history entry for list " + listID + " newValue " + newValue + " failed with " + e.getMessage());		
		}
		return (childLists!=null && !childLists.isEmpty());
	}
	
	/**A
	 * returns all options related to the list
	 * @param listObjectId
	 * @return
	 */
	@Override
	public List<TOptionBean> loadByListID(Integer listObjectId) {
		Criteria crit = new Criteria();
		crit.add(LIST, listObjectId);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the textbox settings by config " + listObjectId + " failed with: " + e);
			return null;
		}
	}
	
	/**A
	 * Gets the children of the Option by the parents ID
	 * If the parentID is null, then returns all the Options who does not have any parent
	 * @param parentID
	 * @return 
	 */
		@Override
		public List<TOptionBean> getChildren(Integer parentID){
			Criteria crit = new Criteria();
			if (parentID == null) {
				crit.add(PARENTOPTION, (Object)"PARENTLIST is NULL", Criteria.CUSTOM);
			} else {
				crit.add(PARENTOPTION, parentID);
			}
			try	{
				return convertTorqueListToBeanList(doSelect(crit));
			}
			catch(TorqueException e){
				LOGGER.error("Loading the custom fields failed with:" + e.getMessage());
				return null;
			}
		}
		
		/**A
		 * Load all options for the selected list IDs
		 * @param listIds
		 * @return
		 */
		@Override
		public List<TOptionBean> loadForListIDs(List<Integer> listIDs){
			Criteria crit = new Criteria();
			crit.addIn(LIST,listIDs);
			try	{
				return convertTorqueListToBeanList(doSelect(crit));
			} catch(TorqueException e){
				LOGGER.error("Loading the custom fields failed with:" + e.getMessage());
				return null;
			}
		}
		
}
