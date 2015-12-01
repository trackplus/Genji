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

package com.aurel.track.cluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TClusterNodeBean;
import com.aurel.track.beans.TEntityChangesBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterBL.CHANGE_TYPE;
import com.aurel.track.cluster.ClusterBL.ENTITY_TYPE;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.EntityChangesDAO;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.budgetCost.BudgetBL;
import com.aurel.track.item.budgetCost.ExpenseBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.lucene.index.associatedFields.AttachmentIndexer;
import com.aurel.track.lucene.index.associatedFields.BudgetPlanIndexer;
import com.aurel.track.lucene.index.associatedFields.ExpenseIndexer;
import com.aurel.track.lucene.index.associatedFields.IAssociatedFieldsIndexer;
import com.aurel.track.lucene.index.associatedFields.LinkIndexer;
import com.aurel.track.lucene.index.listFields.LocalizedListIndexer;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.GeneralUtils;

/**
 * Utility methods for updating the cluster nodes: cache and lucene index
 * @author Tamas
 *
 */
public class ClusterUpdateBL {
	
	private static final Logger LOGGER = LogManager.getLogger(ClusterUpdateBL.class);
	private static EntityChangesDAO entityChangesDAO = DAOFactory.getFactory().getEntityChangesDAO();
	
	/**
	 * Invalidates the cache for my node
	 */
	public static void updateClusterNode() {
		String ipAddress = ClusterBL.getIPAddress();
		LOGGER.debug("Updating cluster node " + ipAddress);
		if (ipAddress!=null) {
			TClusterNodeBean clusterNodeBean  = ClusterBL.loadByIP(ipAddress);
			Integer clusterNodeID = null;
			if (clusterNodeBean!=null) {
				clusterNodeID = clusterNodeBean.getObjectID();
				//update cache for my node
				updateCacheOnNode(clusterNodeID, ipAddress);
			}
			//update index
			int[] changeTypes = new int[] {CHANGE_TYPE.ADD_TO_INDEX, CHANGE_TYPE.UPDATE_INDEX, CHANGE_TYPE.DELETE_FROM_INDEX};
			if (ClusterBL.isSharedLuceneIndex()) {
				//one shared index
				if (ClusterBL.getIAmTheMaster()) {
					//full reindex is needed?
					boolean reindex = false;
					List<TEntityChangesBean> indexChangesForMeAsMaster = entityChangesDAO.loadWithoutClusterNodeReindex();
					if (indexChangesForMeAsMaster==null || indexChangesForMeAsMaster.isEmpty()) {
						//no full reindex, get all changes one by one
						indexChangesForMeAsMaster = entityChangesDAO.loadWithoutClusterNodeByChangeTypes(changeTypes);
						LOGGER.debug("Reindex " + indexChangesForMeAsMaster.size() + " entities from masternode (" + ipAddress + ")");
					} else {
						LOGGER.debug("Full reindex needed on masternode (" + ipAddress + ")");
						reindex = true;
					}
					updateFullTextIndexOnMasterNode(indexChangesForMeAsMaster, ipAddress);
					entityChangesDAO.deleteByChangeTypesWithoutClusterNode(changeTypes);
					if (reindex) {
						entityChangesDAO.deleteReindexWithoutClusterNode();
					}
				}
			} else {
				//each node has own index
				changeTypes = new int[] {CHANGE_TYPE.ADD_TO_INDEX, CHANGE_TYPE.UPDATE_INDEX, CHANGE_TYPE.DELETE_FROM_INDEX};
				boolean reindex = false;
				List<TEntityChangesBean> indexChangesMeAsNode = entityChangesDAO.loadByClusterNodeReindex(clusterNodeID);
				if (indexChangesMeAsNode==null || indexChangesMeAsNode.isEmpty()) {
					indexChangesMeAsNode = entityChangesDAO.loadByClusterNodeAndChangeTypes(clusterNodeID, changeTypes);
					LOGGER.debug("Reindex " + indexChangesMeAsNode.size() + " entities from node " + ipAddress);
				} else {
					LOGGER.debug("Full reindex needed on node " + ipAddress);
					reindex = true;
				}
				updateFullTextIndexOnMasterNode(indexChangesMeAsNode, ipAddress);
				entityChangesDAO.deleteByClusterNodeChangeTypes(clusterNodeID, changeTypes);
				if (reindex) {
					entityChangesDAO.deleteReindexByClusterNode(clusterNodeID);
				}
			}
		}
	}
	 
 	/**
 	 * Invalidates the cache for my node
 	 * @param clusterNodeID
 	 * @param ipAddress
 	 */
	public static void updateCacheOnNode(Integer clusterNodeID, String ipAddress) {
		int[] changeTypes = new int[] {CHANGE_TYPE.UPDATE_CACHE};
		List<TEntityChangesBean> cacheChangesList = entityChangesDAO.loadByClusterNodeAndChangeTypes(clusterNodeID, changeTypes);
		if (cacheChangesList!=null) {
			LOGGER.debug("Number of cache changes for " + ipAddress + ": " + cacheChangesList.size());
			Set<Integer> dirtyLists = new HashSet<Integer>();
			Set<Integer> dirtySystemStateLists = new HashSet<Integer>();
			for (TEntityChangesBean entityChangesBean : cacheChangesList) {
				Integer entityType = entityChangesBean.getEntityType();
				//Integer entityID = entityChangesBean.getEntityKey();
				//right now only the system lists are cached
				if (entityType!=null) {
					if (entityType.intValue()==ENTITY_TYPE.SYSTEM_LIST) {
						Integer listID = entityChangesBean.getList();
						if (listID!=null) {
							dirtyLists.add(listID);
						}	
					} else {
						if (entityType.intValue()==ENTITY_TYPE.SYSTEM_STATE_LIST) {
							Integer entityFlag = entityChangesBean.getList();
							if (entityFlag!=null) {
								dirtySystemStateLists.add(entityFlag);
							}	
						}
					}
				}
			}
			for (Integer listID : dirtyLists) {
				LOGGER.debug("Reset list '" + ClusterBL.getSystemList(listID) + "' in cache");
				LookupContainer.resetLookupMap(listID);
			}
			for (Integer entityFlag : dirtySystemStateLists) {
				LOGGER.debug("Reset system state list '" + entityFlag + "' in cache");
				LookupContainer.resetSystemStateMap(entityFlag);
			}
			//delete the entity changes from database after the cache was invalidated
			entityChangesDAO.deleteByClusterNodeChangeTypes(clusterNodeID, changeTypes);
		}
	}
	
	/**
	 * Updates the lucene index
	 */
	private static void updateFullTextIndexOnMasterNode(List<TEntityChangesBean> fullTextIndexChangesMeAsMaster, String ipAddress) {
		//get the entity changes for the actual master
		if (fullTextIndexChangesMeAsMaster!=null) {
			LOGGER.debug("Number of full text search index changes for the actual master (" + ipAddress + "): " + fullTextIndexChangesMeAsMaster.size());
			/**
			 * changes for system lists: changeType -> system list id (fieldID) -> changed list options
			 * 
			 */
			Map<Integer, Map<Integer, List<Integer>>> systemListChangesMap = new HashMap<Integer, Map<Integer,List<Integer>>>();
			/**
			 * all other changes: mainType -> changeType -> changed entity IDs
			 */
			Map<Integer, Map<Integer, List<Integer>>> otherChangesMap = new HashMap<Integer, Map<Integer,List<Integer>>>();
			for (TEntityChangesBean entityChangesBean : fullTextIndexChangesMeAsMaster) {
				Integer entityType = entityChangesBean.getEntityType();
				Integer changeType = entityChangesBean.getChangeType();
				Integer entityID = entityChangesBean.getEntityKey();
				if (entityType!=null) {
					switch(entityType) {
					case ENTITY_TYPE.ALL:
						LuceneIndexer luceneIndexer = new LuceneIndexer();
							//is finished means not yet run or already finished indexing
						if (luceneIndexer.isFinished()) {
								//reindex in a new thread (the modifiers are initialized inside run)
								ApplicationBean.getInstance().getExecutor().execute(luceneIndexer);
						}
						return;
					case ENTITY_TYPE.WORKITEM:
					case ENTITY_TYPE.CUSTOM_LIST:
					case ENTITY_TYPE.ATTACHMENT_LIST:
					case ENTITY_TYPE.BUDGET_PLAN:
					case ENTITY_TYPE.EXPENSE:
					case ENTITY_TYPE.LINK:
						Map<Integer, List<Integer>> entitiesOfTypeChangesMap = otherChangesMap.get(entityType);
						if (entitiesOfTypeChangesMap==null) {
							entitiesOfTypeChangesMap = new HashMap<Integer, List<Integer>>();
							otherChangesMap.put(entityType, entitiesOfTypeChangesMap);
						}
						List<Integer> entityIDsByChangeType = entitiesOfTypeChangesMap.get(changeType);
						if (entityIDsByChangeType==null) {
							entityIDsByChangeType = new LinkedList<Integer>();
							entitiesOfTypeChangesMap.put(changeType, entityIDsByChangeType);
						}
						entityIDsByChangeType.add(entityID);
						break;
					case ENTITY_TYPE.SYSTEM_LIST:
						Integer list = entityChangesBean.getList();
						if (list!=null) {
							Map<Integer, List<Integer>> systemListChangesMapOfChangeType = systemListChangesMap.get(changeType);
							if (systemListChangesMapOfChangeType==null) {
								systemListChangesMapOfChangeType = new HashMap<Integer, List<Integer>>();
								systemListChangesMap.put(changeType, systemListChangesMapOfChangeType);
							}
							List<Integer> entityList = systemListChangesMapOfChangeType.get(list);
							if (entityList==null) {
								entityList = new LinkedList<Integer>();
								systemListChangesMapOfChangeType.put(list, entityList);
							}
							entityList.add(entityID);
						}
						break;
					}
				}
			}
			//workitem changes
			Map<Integer, List<Integer>> workItemChangesMap = otherChangesMap.get(ENTITY_TYPE.WORKITEM);
			updateWorkitemChanges(workItemChangesMap, ipAddress);
			//system list changes
			updateSystemListChanges(systemListChangesMap, ipAddress);
			//custom list changes
			Map<Integer, List<Integer>> customListChangesMap = otherChangesMap.get(ENTITY_TYPE.CUSTOM_LIST);
			updateCustomListChanges(customListChangesMap, ipAddress);
			//associated entity changes
			Map<Integer, List<Integer>> attachmentChangesMap = otherChangesMap.get(ENTITY_TYPE.ATTACHMENT_LIST);
			updateAssociatedFieldChanges(attachmentChangesMap, ENTITY_TYPE.ATTACHMENT_LIST, ipAddress);
			Map<Integer, List<Integer>> budgetPlanChangesMap = otherChangesMap.get(ENTITY_TYPE.BUDGET_PLAN);
			updateAssociatedFieldChanges(budgetPlanChangesMap, ENTITY_TYPE.BUDGET_PLAN, ipAddress);
			Map<Integer, List<Integer>> expenseChangesMap = otherChangesMap.get(ENTITY_TYPE.EXPENSE);
			updateAssociatedFieldChanges(expenseChangesMap, ENTITY_TYPE.EXPENSE, ipAddress);
			Map<Integer, List<Integer>> linkChangesMap = otherChangesMap.get(ENTITY_TYPE.LINK);
			updateAssociatedFieldChanges(linkChangesMap, ENTITY_TYPE.LINK, ipAddress);
			
		}
	}
	
	/**
	 * Updates the lucene index with the system list changes
	 * @param customListChangesMap
	 * @param ipAddress
	 */
	private static void updateSystemListChanges(Map<Integer, Map<Integer, List<Integer>>> systemListChangesMap, String ipAddress) {
		if (systemListChangesMap!=null && !systemListChangesMap.isEmpty()) {
			for (Map.Entry<Integer, Map<Integer, List<Integer>>> entry : systemListChangesMap.entrySet()) {
				Integer changeType = entry.getKey();
				LOGGER.debug("Update system list index for ip " + ipAddress);
				Map<Integer, List<Integer>> systemListChangesMapOfChangeType = entry.getValue();
				for (Map.Entry<Integer, List<Integer>> listEntry : systemListChangesMapOfChangeType.entrySet()) {
					//reset the entire system list independently of the changes 
					Integer listID = listEntry.getKey();
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(listID);
					Integer lookupEntityType = fieldTypeRT.getLookupEntityType();
					LOGGER.debug("Update index for list '" + ClusterBL.getSystemList(listID) + "'");
					List<Integer> entityIDs = listEntry.getValue();
					ILabelBean labelBean = null;
					for (Integer entityID : entityIDs) {
						switch (changeType.intValue()) {
						case CHANGE_TYPE.ADD_TO_INDEX:
							LOGGER.debug("Add index for list '" + ClusterBL.getSystemList(listID) + "' and entry " + entityID);
							labelBean = LookupContainer.getNotLocalizedLabelBean(listID, entityID);
							if (labelBean!=null) {
								LocalizedListIndexer.getInstance().addLabelBean(labelBean, lookupEntityType, true);
							}
							break;
						case CHANGE_TYPE.UPDATE_INDEX:
							LOGGER.debug("Update index for list '" + ClusterBL.getSystemList(listID) + "' and entry " + entityID);
							labelBean = LookupContainer.getNotLocalizedLabelBean(listID, entityID);
							if (labelBean!=null) {
								LocalizedListIndexer.getInstance().updateLabelBean(labelBean, lookupEntityType);
							}
							break;
						case CHANGE_TYPE.DELETE_FROM_INDEX:
							LOGGER.debug("Delete index for list '" + ClusterBL.getSystemList(listID) + "' and entry " + entityID);
							LocalizedListIndexer.getInstance().deleteByKeyAndType(entityID, lookupEntityType);
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Updates the lucene index with the item changes
	 * @param workItemChangesMap
	 * @param ipAddress
	 */
	private static void updateWorkitemChanges(Map<Integer, List<Integer>> workItemChangesMap, String ipAddress) {
		if (workItemChangesMap!=null && !workItemChangesMap.isEmpty()) {
			for (Map.Entry<Integer, List<Integer>> entry : workItemChangesMap.entrySet()) {
				Integer changeType = entry.getKey();
				List<Integer> workItemIDs = entry.getValue();
				switch (changeType.intValue()) {
				case CHANGE_TYPE.ADD_TO_INDEX:
					List<TWorkItemBean> addedWorkItems = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromIntegerList(workItemIDs));
					LOGGER.debug("Number of workitems added for " + ipAddress + ": "  + workItemIDs.size());
					LuceneIndexer.addToWorkItemIndex(addedWorkItems, null);
					break;
				case CHANGE_TYPE.UPDATE_INDEX:
					LuceneIndexer.updateWorkItemIndex(GeneralUtils.createIntArrFromIntegerList(workItemIDs), null);
					LOGGER.debug("Number of workitems updated for " + ipAddress + ": "  + workItemIDs.size());
					break;
				case CHANGE_TYPE.DELETE_FROM_INDEX:
					List<TWorkItemBean> deletedWorkItems = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromIntegerList(workItemIDs));
					LuceneIndexer.deleteFromWorkItemIndex(deletedWorkItems, null, true);
					LOGGER.debug("Number of workitems deleted for " + ipAddress + ": "  + workItemIDs.size());
					break;
				}
			}
		}
	}
	
	/**
	 * Updates the lucene index with the custom list changes
	 * @param customListChangesMap
	 * @param ipAddress
	 */
	private static void updateCustomListChanges(Map<Integer, List<Integer>> customListChangesMap, String ipAddress) {
		if (customListChangesMap!=null && !customListChangesMap.isEmpty()) {
			for (Map.Entry<Integer, List<Integer>> entry : customListChangesMap.entrySet()) {
				Integer changeType = entry.getKey();
				List<Integer> optionIDs = entry.getValue();
				switch (changeType.intValue()) {
				case CHANGE_TYPE.ADD_TO_INDEX:
					List<TOptionBean> addedOptions = OptionBL.loadByKeys(GeneralUtils.createIntegerArrFromCollection(optionIDs));
					if (addedOptions!=null) {
						LOGGER.debug("Number of custom options added for " + ipAddress + ": "  + addedOptions.size());
						for (TOptionBean optionBean : addedOptions) {
							LocalizedListIndexer.getInstance().addLabelBean(optionBean,
									LuceneUtil.LOOKUPENTITYTYPES.CUSTOMOPTION, true);
						}
					}
					break;
				case CHANGE_TYPE.UPDATE_INDEX:
					List<TOptionBean> updatedOptions = OptionBL.loadByKeys(GeneralUtils.createIntegerArrFromCollection(optionIDs));
					if (updatedOptions!=null) {
						LOGGER.debug("Number of custom options updated for " + ipAddress + ": "  + updatedOptions.size());
						for (TOptionBean optionBean : updatedOptions) {
							LocalizedListIndexer.getInstance().updateLabelBean(optionBean,
									LuceneUtil.LOOKUPENTITYTYPES.CUSTOMOPTION);
						}
					}
					break;
				case CHANGE_TYPE.DELETE_FROM_INDEX:
					LOGGER.debug("Number of custom options deleted for " + ipAddress + ": "  + optionIDs.size());
					for (Integer optionID : optionIDs) {
						LocalizedListIndexer.getInstance().deleteByKeyAndType(optionID, LuceneUtil.LOOKUPENTITYTYPES.CUSTOMOPTION);
					}
					break;
				}
			}
		}
	}
	
	/**
	 * Updates the lucene index with the associated entity changes
	 * @param changesMap
	 * @param entityType
	 * @param ipAddress
	 */
	private static void updateAssociatedFieldChanges(Map<Integer, List<Integer>> changesMap, Integer entityType, String ipAddress) {
		if (changesMap!=null && !changesMap.isEmpty()) {
			for (Map.Entry<Integer, List<Integer>> entry : changesMap.entrySet()) {
				Integer changeType = entry.getKey();
				List<Integer> objectIDs = entry.getValue();
				if (objectIDs!=null) {
					IAssociatedFieldsIndexer associatedFieldsIndexer = getAssociatedFieldIndexer(entityType);
					switch (changeType.intValue()) {
					case CHANGE_TYPE.ADD_TO_INDEX:
					case CHANGE_TYPE.UPDATE_INDEX:
						List entityList = getAssociatedEntitiesByIDs(objectIDs, entityType);
						if (entityList!=null) {
							LOGGER.debug(entityList.size() + " entities of type '" + ClusterBL.getEntityTypeString(entityType) +  "' added/updated for " + ipAddress + "  ");
							for (Object entityBean : entityList) {
								associatedFieldsIndexer.addToIndex(entityBean, changeType.intValue()==CHANGE_TYPE.ADD_TO_INDEX);
							}
						}
						break;
					case CHANGE_TYPE.DELETE_FROM_INDEX:
						LOGGER.debug( objectIDs.size() + " type '" + ClusterBL.getEntityTypeString(entityType) + "' deleted for " + ipAddress);
						for (Integer optionID : objectIDs) {
							associatedFieldsIndexer.deleteByKey(optionID);
						}
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Gets the associated entity beans by IDs
	 * @param objectIDs
	 * @param associatedEntityType
	 * @return
	 */
	private static List getAssociatedEntitiesByIDs(List<Integer> objectIDs, int associatedEntityType) {
		switch (associatedEntityType) {
		case ENTITY_TYPE.ATTACHMENT_LIST:
			return AttachBL.loadByAttachmentIDs(objectIDs);
		case ENTITY_TYPE.BUDGET_PLAN:
			return BudgetBL.getByIDs(objectIDs);
		case ENTITY_TYPE.EXPENSE:
			return ExpenseBL.loadByKeys(objectIDs);
		case ENTITY_TYPE.LINK:
			return ItemLinkBL.loadByIDs(objectIDs);
		default:
			return null;
		}
	}
	
	/**
	 * Gets the associated field indexer implementation
	 * @param associatedEntityType
	 * @return
	 */
	private static IAssociatedFieldsIndexer getAssociatedFieldIndexer(int associatedEntityType) {
		switch (associatedEntityType) {
		case ENTITY_TYPE.ATTACHMENT_LIST:
			return AttachmentIndexer.getInstance();
		case ENTITY_TYPE.BUDGET_PLAN:
			return BudgetPlanIndexer.getInstance();
		case ENTITY_TYPE.EXPENSE:
			return ExpenseIndexer.getInstance();
		case ENTITY_TYPE.LINK:
			return LinkIndexer.getInstance();
		default:
			return null;
		}
	}
}
