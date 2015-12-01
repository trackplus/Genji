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

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TClusterNodeBean;
import com.aurel.track.beans.TEntityChangesBean;
import com.aurel.track.cluster.ClusterBL.CHANGE_TYPE;
import com.aurel.track.cluster.ClusterBL.ENTITY_TYPE;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.EntityChangesDAO;
import com.aurel.track.util.EqualUtils;

/**
 * Register the cluster regarding changes in entityChanges table
 * @author Tamas
 *
 */
public class ClusterMarkChangesBL {
	private static final Logger LOGGER = LogManager.getLogger(ClusterMarkChangesBL.class);
	private static EntityChangesDAO entityChangesDAO = DAOFactory.getFactory().getEntityChangesDAO();
	
	/**
	 * Gets the change type string for debug purposes
	 * @param changeType
	 * @return
	 */
	private static String getChange(int changeType) {
		switch (changeType) {
		case CHANGE_TYPE.ADD_TO_INDEX:
			return "AddToIndex";
		case CHANGE_TYPE.UPDATE_INDEX:
			return "UpdateIndex";
		case CHANGE_TYPE.DELETE_FROM_INDEX:
			return "DeleteFromIndex";
		case CHANGE_TYPE.UPDATE_CACHE:
			return "UpdateCache";	
		}
		return "unknown";
	}
	
	/**
	 * Gets the change type to register by add or update, used for cache update and possibly index update (system lists)
	 * @param isNew
	 * @return
	 */
	public static int getChangeTypeByAddOrUpdate(boolean isNew) {
		if (ClusterBL.indexInstantly()) {
			//update only the local cache
			return ClusterBL.CHANGE_TYPE.UPDATE_CACHE;
		} else {
			//update the locale cache and the full text index
			if (isNew) {
				return ClusterBL.CHANGE_TYPE.ADD_TO_INDEX;
			} else {
				return ClusterBL.CHANGE_TYPE.UPDATE_INDEX;
			}
		}
	}

	/**
	 *  Gets the change type to register by add or update, used for index update
	 * @param isNew
	 * @return
	 */
	public static int getChangeTypeByAddOrUpdateIndex(boolean isNew) {
		if (isNew) {
			return ClusterBL.CHANGE_TYPE.ADD_TO_INDEX;
		} else {
			return ClusterBL.CHANGE_TYPE.UPDATE_INDEX;
		}
	}
	
	/**
	 * Gets the change type to register by delete
	 * @return
	 */
	public static int getChangeTypeByDelete() {
		if (ClusterBL.indexInstantly()) {
			//only cache map should be updated
			return CHANGE_TYPE.UPDATE_CACHE;
		} else {
			//update cache and the full text index
			return CHANGE_TYPE.DELETE_FROM_INDEX;
		}
	}
	/**************************************************************/
	/***************Cache and full text entities*******************/
	/**************************************************************/
	
	public static void markReindex() {
		if (ClusterBL.isSharedLuceneIndex()) {
			if (!ClusterBL.getIAmTheMaster()) {
				markDirtyEntryForMasterNode(ClusterBL.ENTITY_TYPE.ALL, null, CHANGE_TYPE.UPDATE_INDEX);
			}
		} else {
			markDirtyEntryInEachClusterNode(ClusterBL.ENTITY_TYPE.ALL, null, null, CHANGE_TYPE.UPDATE_INDEX);
		}
	}
	
	/**
	 * Mark list entry change for cluster node
	 * @param listFieldID
	 * @param entityID
	 * @param changeType
	 */
	public static void markDirtySystemListEntryInCluster(Integer listFieldID, Integer entityID, int changeType) {
		//for updating the cache
		markDirtyEntryInEachClusterNode(ClusterBL.ENTITY_TYPE.SYSTEM_LIST, listFieldID, entityID, CHANGE_TYPE.UPDATE_CACHE);
		if (changeType!=CHANGE_TYPE.UPDATE_CACHE) {
			//for updating the full text index
			if (ClusterBL.isSharedLuceneIndex()) {
				if (!ClusterBL.getIAmTheMaster()) {
					markDirtyListEntryForMasterNode(ClusterBL.ENTITY_TYPE.SYSTEM_LIST, listFieldID, entityID, changeType);
				}
			} else {
				markDirtyEntryInEachClusterNode(ClusterBL.ENTITY_TYPE.SYSTEM_LIST, listFieldID, entityID, changeType);
			}
		}
	}
	
	/**
	 * Mark list entry change for cluster node
	 * @param listFieldID
	 * @param entityID
	 * @param changeType
	 */
	public static void markDirtySystemStatesListEntryInCluster(Integer entityFlag, Integer systemStateID) {
		//for updating the cache
		markDirtyEntryInEachClusterNode(ClusterBL.ENTITY_TYPE.SYSTEM_STATE_LIST, entityFlag, systemStateID, CHANGE_TYPE.UPDATE_CACHE);
		
	}

	/**************************************************************/
	/***************Only full text index updates*******************/
	/**************************************************************/
	
	/**
	 * Mark custom list entry change for cluster node
	 * @param listFieldID
	 * @param entityID
	 * @param changeType
	 */
	public static void markDirtyCustomListEntryInCluster(Integer listID, Integer entityID, int changeType) {
		if (ClusterBL.isSharedLuceneIndex()) {
			if (!ClusterBL.getIAmTheMaster()) {
				markDirtyListEntryForMasterNode(ClusterBL.ENTITY_TYPE.CUSTOM_LIST, listID, entityID, changeType);
			}
		} else {
			markDirtyEntryInEachClusterNode(ClusterBL.ENTITY_TYPE.CUSTOM_LIST, listID, entityID, changeType);
		}
	}
	
	/**
	 * Mark a dirty item in cluster
	 * @param workItemID
	 * @param changeType
	 */
	public static void markDirtyWorkitemInCluster(Integer workItemID, Integer changeType) {
		if (workItemID!=null) {
			if (ClusterBL.isSharedLuceneIndex()) {
				if (!ClusterBL.getIAmTheMaster()) {
					markDirtyEntryForMasterNode(ClusterBL.ENTITY_TYPE.WORKITEM, workItemID, changeType);
				}
			} else {
				markDirtyEntryInEachClusterNode(ClusterBL.ENTITY_TYPE.WORKITEM, null, workItemID, changeType);
			}
		}
	}
	
	/**
	 * Mark dirty items
	 * @param workItemBeanIDs
	 */
	public static void markDirtyWorkItemsInCluster(int[] workItemBeanIDs) {
		if (workItemBeanIDs!=null && workItemBeanIDs.length>0) {
			if (ClusterBL.isSharedLuceneIndex()) {
				if (!ClusterBL.getIAmTheMaster()) {
					for (int workItemBeanID : workItemBeanIDs) {
						markDirtyEntryForMasterNode(ClusterBL.ENTITY_TYPE.WORKITEM, workItemBeanID, ClusterBL.CHANGE_TYPE.UPDATE_INDEX);
					}
				}
			} else {
				for (int workItemBeanID : workItemBeanIDs) {
					markDirtyEntryInEachClusterNode(ClusterBL.ENTITY_TYPE.WORKITEM, null, workItemBeanID,  ClusterBL.CHANGE_TYPE.UPDATE_INDEX);
				}
				
			}
		}
	}

	/**
	 * Mark attachment change for cluster node
	 */
	public static void markDirtyAttachmentInCluster(Integer attachmentID, int changeType) {
		if (ClusterBL.isSharedLuceneIndex()) {
			if (!ClusterBL.getIAmTheMaster()) {
				markDirtyEntryForMasterNode(ClusterBL.ENTITY_TYPE.ATTACHMENT_LIST, attachmentID, changeType);
			}
		} else {
			markDirtyEntryInEachClusterNode(ClusterBL.ENTITY_TYPE.ATTACHMENT_LIST, null, attachmentID, changeType);
		}
	}

	/**
	 * Mark budget/plan description change for cluster node
	 */
	public static void markDirtyBudgetPlanInCluster(Integer objectID, int changeType) {
		if (ClusterBL.isSharedLuceneIndex()) {
			if (!ClusterBL.getIAmTheMaster()) {
				markDirtyEntryForMasterNode(ClusterBL.ENTITY_TYPE.BUDGET_PLAN, objectID, changeType);
			}
		} else {
			markDirtyEntryInEachClusterNode(ClusterBL.ENTITY_TYPE.BUDGET_PLAN, null, objectID, changeType);
		}
		
	}

	/**
	 * Mark expense description change for cluster node
	 */
	public static void markDirtyExpenseInCluster(Integer objectID, int changeType) {
		if (ClusterBL.isSharedLuceneIndex()) {
			if (!ClusterBL.getIAmTheMaster()) {
				markDirtyEntryForMasterNode(ClusterBL.ENTITY_TYPE.EXPENSE, objectID, changeType);
			}
		} else {
			markDirtyEntryInEachClusterNode(ClusterBL.ENTITY_TYPE.EXPENSE, null, objectID, changeType);
		}
	}

	/**
	 * Mark link description change for cluster node
	 */
	public static void markDirtyLinkInCluster(Integer objectID, int changeType) {
		if (ClusterBL.isSharedLuceneIndex()) {
			if (!ClusterBL.getIAmTheMaster()) {
				markDirtyEntryForMasterNode(ClusterBL.ENTITY_TYPE.LINK, objectID, changeType);
			}
		} else {
			markDirtyEntryInEachClusterNode(ClusterBL.ENTITY_TYPE.LINK, null, objectID, changeType);
		}
	}

	/**
	 * Marks the entity changes for each cluster node except me: used for refreshing the cache
	 * @param entityType
	 * @param list
	 * @param entityID
	 * @param changeType
	 */
	private static void markDirtyEntryInEachClusterNode(int entityType, Integer list, Integer entityID, Integer changeType) {
		if (ClusterBL.isCluster() && !ClusterBL.getIAmTheMaster()) {
			String myIP = ClusterBL.getIPAddress();
			List<TClusterNodeBean> allClusterNodes = ClusterBL.loadAllClusterNodes();
			if (allClusterNodes!=null) {
				for (Iterator<TClusterNodeBean> iterator = allClusterNodes.iterator(); iterator.hasNext();) {
					TClusterNodeBean clusterNodeBean = iterator.next();
					String ip = clusterNodeBean.getNodeAddress();
					if (EqualUtils.equal(ip, myIP)) {
						//the actual node's cache is updated directly no dirty entry is needed
						iterator.remove();
					} else {
						Integer clusterNodeID = clusterNodeBean.getObjectID();
						addEntityChangeIfNeeded(clusterNodeID, entityType, list, changeType, entityID, ip);
					}
				}
			}
		}
	}
	
	/**
	 * Adds a new entity change if needed
	 * @param clusterNodeID
	 * @param entityType
	 * @param list
	 * @param changeType
	 * @param entityID
	 * @param ip
	 */
	private static void addEntityChangeIfNeeded(Integer clusterNodeID, int entityType, Integer list, Integer changeType, Integer entityID, String ip) {
		List<TEntityChangesBean> entityChangesBeans = null;
		switch (changeType) {
		case CHANGE_TYPE.ADD_TO_INDEX:
			//add needs always adding an entity change
			addEntityChange(clusterNodeID, entityType, list, changeType, entityID, ip);
			break;
		case CHANGE_TYPE.UPDATE_CACHE:
			//add entity change for update cache if not recently added or updated
			entityChangesBeans = entityChangesDAO.loadByClusterNodeEntityTypeListChangeType(clusterNodeID, entityType, list, CHANGE_TYPE.UPDATE_CACHE);
			if (entityChangesBeans==null || entityChangesBeans.isEmpty()) {
				//add the entity change if the cache was not updated recently
				//for cache the entityID is not recorded because the entire list is invalidated
				addEntityChange(clusterNodeID, entityType, list, changeType, null, ip);
			}
			break;
		case CHANGE_TYPE.UPDATE_INDEX:
			//add entity change for update if not recently added or updated
			int[] changeTypes = new int[] {CHANGE_TYPE.ADD_TO_INDEX, CHANGE_TYPE.UPDATE_INDEX};
			entityChangesBeans = entityChangesDAO.loadByClusterNodeEntityIDEntityTypeListChangeTypes(clusterNodeID, entityID, entityType, list, changeTypes);
			if (entityChangesBeans==null || entityChangesBeans.isEmpty()) {
				//add the entity change if it was not added or updated recently
				addEntityChange(clusterNodeID, entityType, list, changeType, entityID, ip);
			}
			break;
		case CHANGE_TYPE.DELETE_FROM_INDEX:
			entityChangesBeans = entityChangesDAO.loadByClusterNodeEntityIDEntityTypeListChangeTypes(clusterNodeID, entityID, entityType, list, new int[] {CHANGE_TYPE.ADD_TO_INDEX});
			if (entityChangesBeans==null || entityChangesBeans.isEmpty()) {
				//not added now
				entityChangesBeans = entityChangesDAO.loadByClusterNodeEntityIDEntityTypeListChangeTypes(clusterNodeID, entityID, entityType, list, new int[] {CHANGE_TYPE.UPDATE_INDEX});
				if (entityChangesBeans!=null && !entityChangesBeans.isEmpty()) {
					//it was updated recently, but now is deleted: remove the update from entity changes because it will be deleted
					entityChangesDAO.deleteByClusterNodeEntityIDEntityTypeListChangeTypes(clusterNodeID, entityID, entityType, list, CHANGE_TYPE.UPDATE_INDEX);
				}
				addEntityChange(clusterNodeID, entityType, list, changeType, entityID, ip);
			} else {
				//it was added recently, but now is deleted: remove from entity changes because no entity change is needed in other cluster nodes
				entityChangesDAO.deleteByClusterNodeEntityIDEntityTypeListChangeTypes(clusterNodeID, entityID, entityType, list, CHANGE_TYPE.ADD_TO_INDEX);
				entityChangesDAO.deleteByClusterNodeEntityIDEntityTypeListChangeTypes(null, null, entityType, list, CHANGE_TYPE.UPDATE_CACHE);
			}
			break;
		}
	}
	
	/**
	 * Adds an entity change
	 * @param clusterNodeID
	 * @param entityType
	 * @param list
	 * @param changeType
	 * @param entityID
	 * @param ip
	 */
	private static void addEntityChange(Integer clusterNodeID, int entityType, Integer list, Integer changeType, Integer entityID, String ip) {
		TEntityChangesBean entityChangesBean = new TEntityChangesBean();
		entityChangesBean.setClusterNode(clusterNodeID);
		entityChangesBean.setEntityType(entityType);
		entityChangesBean.setList(list);
		entityChangesBean.setChangeType(changeType);
		entityChangesBean.setEntityKey(entityID);
		entityChangesDAO.save(entityChangesBean);
		if (LOGGER.isDebugEnabled()) {
			String listString = "";
			if (list!=null) {
				if (entityType==ENTITY_TYPE.SYSTEM_LIST) {
					listString = ClusterBL.getSystemList(list);
				} else {
					if (entityType==ENTITY_TYPE.CUSTOM_LIST) {
						listString = "Custom";
					}
				}
				listString = ", list '" + listString + "'";
			}
			String entityString = "";
			if (entityID!=null) {
				entityString = ", entityID " + entityID;
			}
			String changeTypeString = "";
			if (changeType!=null) {
				changeTypeString = ", change type '" + getChange(changeType) + "'";
			}
			String ipString = "";
			if (ip!=null) {
				ipString = " for the ip " + ip;
			} else {
				ipString = " for the master node";
			}
			LOGGER.debug("Add entity: type '" + ClusterBL.getEntityTypeString(entityType) + "'" + listString
					+ entityString  + changeTypeString + ipString);
		}
	}
	
	/**
	 * Marks the entity changes for the master cluster node, although it is not known which will be the cluster node at the time of processing this change
	 * That's why the cluster node remains empty and a non master node should leave this entry unchanged but the actual cluster node should process
	 * Used for refreshing the full text index
	 * @param entityType
	 * @param entityID
	 * @param changeType
	 */
	private static void markDirtyListEntryForMasterNode(int entityType, Integer listID, Integer entityID, Integer changeType) {
		if (ClusterBL.isCluster() && !ClusterBL.getIAmTheMaster()) {
			addEntityChangeIfNeeded(null, entityType, listID, changeType, entityID, null);
		}
	}
	
	/**
	 * Marks the entity changes for the master cluster node, although it is not known which will be the cluster node at the time of processing this change
	 * That's why the cluster node remains empty and a non master node should leave this entry unchanged but the actual cluster node should process
	 * Used for refreshing the full text index
	 * @param entityType
	 * @param entityID
	 * @param changeType
	 */
	private static void markDirtyEntryForMasterNode(int entityType, Integer entityID, Integer changeType) {
		if (ClusterBL.isCluster() && !ClusterBL.getIAmTheMaster()) {
			addEntityChangeIfNeeded(null, entityType, null, changeType, entityID, null);
		}
	}
	
}
