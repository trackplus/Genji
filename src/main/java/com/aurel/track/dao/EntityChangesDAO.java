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


package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TEntityChangesBean;

/**
 * DAO for entity changes 
 * @author Tamas Ruff
 *
 */
public interface EntityChangesDAO {
		
	/**
	 * Loads all entity changes for a cluster node and a changeType
	 * @param clusterNodeID
	 * @param changeTypes
	 * @return
	 */
	List<TEntityChangesBean> loadByClusterNodeAndChangeTypes(Integer clusterNodeID, int[] changeTypes);
	
	/**
	 * Loads the entity changes by clusterNode, entityType, list and change type
	 * @param clusterNode
	 * @param entityType
	 * @param list
	 * @param changeType
	 * @return
	 */
	List<TEntityChangesBean> loadByClusterNodeEntityTypeListChangeType(Integer clusterNode, Integer entityType, Integer list, int changeType);
	
	/**
	 * Loads the entity changes by clusterNode, entityType and list
	 * @param clusterNode
	 * @param entityID
	 * @param entityType
	 * @param list
	 * @param changeTypes
	 * @return
	 */
	List<TEntityChangesBean> loadByClusterNodeEntityIDEntityTypeListChangeTypes(Integer clusterNode, Integer entityID, Integer entityType, Integer list, int[] changeTypes);
	
	/**
	 * Loads the entity changes by entityType, listID, entityID and changeType
	 * @param entityType
	 * @param listID
	 * @param entityID
	 * @param changeType
	 * @return
	 */
	//List<TEntityChangesBean> loadByEntityTypeListEntityIDChangeType(int entityType, Integer listID, Integer entityID, int changeType);
	
	/**
	 * Loads the entity changes by entityType, entityID and changeType
	 * @param entityType
	 * @param entityID
	 * @param changeType
	 * @return
	 */
	//List<TEntityChangesBean> loadByEntityTypeEntityIDChangeType(int entityType, Integer entityID, int changeType);
	
	/**
	 * Loads all entity changes without a cluster node
	 * @param changeTypes
	 * @return 
	 */
	List<TEntityChangesBean> loadWithoutClusterNodeByChangeTypes(int[] changeTypes);
	
	/**
	 * Loads entity changes for full reindex, me as master
	 * @return 
	 */
	List<TEntityChangesBean> loadWithoutClusterNodeReindex();
	
	/**
	 * Loads entity changes for full reindex, me as current node
	 * @param clusterNodeID
	 * @return
	 */
	List<TEntityChangesBean> loadByClusterNodeReindex(Integer clusterNodeID);
	
	/**
	 * Saves an entity changes in the TEntityChanges table.
	 * @param entityChangesBean
	 * @return
	 */
	Integer save(TEntityChangesBean entityChangesBean);
	
	/**
	 * Deletes an entry by cluster node, entityID, entityType, list and change type
	 * @param clusterNodeID
	 * @param entityID
	 * @param entityType
	 * @param list
	 * @param changeType
	 */
	void deleteByClusterNodeEntityIDEntityTypeListChangeTypes(Integer clusterNodeID, Integer entityID, Integer entityType, Integer list, int changeType);
	
	/**
	 * Deletes entity changes by clusterNodeID and changeTypes
	 * @param clusterNodeID
	 * @param changeTypes
	 * @return
	 */
	void deleteByClusterNodeChangeTypes(Integer clusterNodeID, int[] changeTypes);
	
	/**
	 * Deletes entity changes by changeTypes withiut cluster node
	 * @param changeTypes
	 * @return
	 */
	void deleteByChangeTypesWithoutClusterNode(int[] changeTypes);
	
	/**
	 * Deletes entity changes by clusterNodeID
	 * @param clusterNodeID
	 * @return
	 */
	void deleteReindexByClusterNode(Integer clusterNodeID);
	
	/**
	 * Delete the entities without a cluster node
	 */
	void deleteReindexWithoutClusterNode();
}
