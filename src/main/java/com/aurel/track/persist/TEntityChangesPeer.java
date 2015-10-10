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

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TEntityChangesBean;
import com.aurel.track.cluster.ClusterBL.ENTITY_TYPE;
import com.aurel.track.dao.EntityChangesDAO;

/**
 * This table holds the changed entities for updating the full text search index in each clusternode
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TEntityChangesPeer
    extends com.aurel.track.persist.BaseTEntityChangesPeer implements EntityChangesDAO
{
	public static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(TEntityChangesPeer.class);
	
	/**
	 * Loads all entity changes for a cluster node and a changeType
	 * @param clusterNodeID
	 * @param changeTypes
	 * @return
	 */
	public List<TEntityChangesBean> loadByClusterNodeAndChangeTypes(Integer clusterNodeID, int[] changeTypes) {
		Criteria crit = new Criteria();
		crit.add(CLUSTERNODE, clusterNodeID);
		crit.addIn(CHANGETYPE, changeTypes);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading entity changes by cluster node " + clusterNodeID +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the entity changes by clusterNode, entityType, list and change type
	 * @param clusterNode
	 * @param entityType
	 * @param list
	 * @param changeType
	 * @return
	 */
	public List<TEntityChangesBean> loadByClusterNodeEntityTypeListChangeType(Integer clusterNode, Integer entityType, Integer list, int changeType) {
		Criteria crit = new Criteria();
		crit.add(CLUSTERNODE, clusterNode);
		crit.add(ENTITYTYPE, entityType);
		if (list==null) {
			crit.add(LIST, (Integer)null, Criteria.ISNULL);
		} else {
			crit.add(LIST, list);
		}
		crit.add(CHANGETYPE, changeType);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading entity changes by cluster node " + clusterNode +  " entityType " + entityType + " list " + list + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the entity changes by clusterNode, entityType and list
	 * @param clusterNode
	 * @param entityID
	 * @param entityType
	 * @param list
	 * @param changeTypes
	 * @return
	 */
	public List<TEntityChangesBean> loadByClusterNodeEntityIDEntityTypeListChangeTypes(Integer clusterNode, Integer entityID, Integer entityType, Integer list, int[] changeTypes) {
		Criteria crit = new Criteria();
		if (clusterNode==null) {
			crit.add(CLUSTERNODE, (Integer)null, Criteria.ISNULL);
		} else {
			crit.add(CLUSTERNODE, clusterNode);
		}
		crit.add(ENTITYTYPE, entityType);
		if (list==null) {
			crit.add(LIST, (Integer)null, Criteria.ISNULL);
		} else {
			crit.add(LIST, list);
		}
		crit.addIn(CHANGETYPE, changeTypes);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading entity changes by cluster node " + clusterNode +  " entityType " + entityType + " list " + list + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the entity changes by clusterNode, entityType and list
	 * @param clusterNode
	 * @param entityID
	 * @param entityType
	 * @param list
	 * @param changeType
	 * @return
	 */
	/*public List<TEntityChangesBean> loadByClusterNodeEntityIDEntityTypeListChangeType(Integer clusterNode, Integer entityID, Integer entityType, Integer list, int changeType) {
		Criteria crit = new Criteria();
		crit.add(CLUSTERNODE, clusterNode);
		crit.add(ENTITYKEY, entityID);
		crit.add(ENTITYTYPE, entityType);
		crit.add(LIST, list);
		crit.add(CHANGETYPE, changeType);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading entity changes by cluster node " + clusterNode +  " entityType " + entityType + " list " + list + " failed with " + e.getMessage(), e);
			return null;
		}
	}*/
	
	/**
	 * Loads the entity changes by entityType, listID, entityID and changeType
	 * @param entityType
	 * @param listID
	 * @param entityID
	 * @param changeType
	 * @return
	 */
	/*public List<TEntityChangesBean> loadByEntityTypeListEntityIDChangeType(int entityType, Integer listID, Integer entityID, int changeType) {
		Criteria crit = new Criteria();
		crit.add(ENTITYTYPE, entityType);
		if (listID==null) {
			crit.add(LIST, (Integer)null, Criteria.ISNULL);
		} else {
			crit.add(LIST, listID);
		}
		crit.add(ENTITYKEY, entityID);
		crit.add(CHANGETYPE, changeType);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading entity changes by entityType " + entityType + " list " + listID + " entityID " + entityID + " changeType " + changeType + " failed with " + e.getMessage(), e);
			return null;
		}
	}*/
	
	/**
	 * Loads the entity changes by entityType, entityID and changeType
	 * @param entityType
	 * @param entityID
	 * @param changeType
	 * @return
	 */
	/*public List<TEntityChangesBean> loadByEntityTypeEntityIDChangeType(int entityType, Integer entityID, int changeType) {
		Criteria crit = new Criteria();
		crit.add(ENTITYTYPE, entityType);
		crit.add(ENTITYKEY, entityID);
		crit.add(CHANGETYPE, changeType);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading entity changes by entityType " + entityType + " entityID " + entityID + " changeType " + changeType + " failed with " + e.getMessage(), e);
			return null;
		}
	}*/
	
	/**
	 * Loads all entity changes without a cluster node
	 * @param changeTypes
	 * @return 
	 */
	public List<TEntityChangesBean> loadWithoutClusterNodeByChangeTypes(int[] changeTypes) {
		Criteria crit = new Criteria();
		crit.add(CLUSTERNODE, (Object)null, Criteria.ISNULL);
		crit.addIn(CHANGETYPE, changeTypes);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading entity changes by missing cluster node failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all entity changes without a cluster node
	 * @return 
	 */
	public List<TEntityChangesBean> loadWithoutClusterNodeReindex() {
		Criteria crit = new Criteria();
		crit.add(CLUSTERNODE, (Object)null, Criteria.ISNULL);
		crit.add(ENTITYTYPE, ENTITY_TYPE.ALL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading entity changes by missing cluster node failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads entity changes for full reindex, me as current node
	 * @param clusterNodeID
	 * @return
	 */
	public List<TEntityChangesBean> loadByClusterNodeReindex(Integer clusterNodeID) {
		Criteria crit = new Criteria();
		crit.add(CLUSTERNODE, clusterNodeID);
		crit.add(ENTITYTYPE, ENTITY_TYPE.ALL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading entity changes by missing cluster node failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Saves an entity changes in the TEntityChanges table.
	 * @param entityChangesBean
	 * @return
	 */
	public Integer save(TEntityChangesBean entityChangesBean) {
		TEntityChanges tEntityChange;
		try {
			tEntityChange = TEntityChanges.createTEntityChanges(entityChangesBean);
			tEntityChange.save();
			return tEntityChange.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of the entity change failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes an entry by cluster node, entityID, entityType, list and change type
	 * @param clusterNodeID
	 * @param entityID
	 * @param entityType
	 * @param list
	 * @param changeType
	 */
	public void deleteByClusterNodeEntityIDEntityTypeListChangeTypes(Integer clusterNodeID, Integer entityID, Integer entityType, Integer list, int changeType) {
		Criteria crit = new Criteria();
		if (clusterNodeID!=null) {
			crit.add(CLUSTERNODE, clusterNodeID);
		}
		crit.add(ENTITYTYPE, entityType);
		if (entityID==null) {
			crit.add(ENTITYKEY, (Integer)null, Criteria.ISNULL);
		} else {
			crit.add(ENTITYKEY, entityID);
		}
		if (list==null) {
			crit.add(LIST, (Integer)null, Criteria.ISNULL);
		} else {
			crit.add(LIST, list);
		}
		crit.add(CHANGETYPE, changeType);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the entity change by cluster node " + clusterNodeID +  " failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * Deletes entity changes by clusterNodeID and changeTypes
	 * @param clusterNodeID
	 * @param changeTypes
	 * @return
	 */
	public void deleteByClusterNodeChangeTypes(Integer clusterNodeID, int[] changeTypes) {
		Criteria crit = new Criteria();
		crit.add(CLUSTERNODE, clusterNodeID);
		crit.addIn(CHANGETYPE, changeTypes);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the entity change by cluster node " + clusterNodeID +  " failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * Deletes entity changes by changeTypes withiut cluster node
	 * @param changeTypes
	 * @return
	 */
	public void deleteByChangeTypesWithoutClusterNode(int[] changeTypes) {
		Criteria crit = new Criteria();
		crit.add(CLUSTERNODE, (Object)null, Criteria.ISNULL);
		crit.addIn(CHANGETYPE, changeTypes);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the entity change by changeTypes " + changeTypes.length +  " failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * Deletes entity changes by clusterNodeID
	 * @param clusterNodeID
	 * @return
	 */
	public void deleteReindexByClusterNode(Integer clusterNodeID) {
		Criteria crit = new Criteria();
		crit.add(CLUSTERNODE, clusterNodeID);
		crit.add(ENTITYTYPE, ENTITY_TYPE.ALL);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the entity change by cluster node " + clusterNodeID +  " failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * Delete the entities without a cluster node
	 */
	public void deleteReindexWithoutClusterNode() {
		Criteria crit = new Criteria();
		crit.add(CLUSTERNODE, (Object)null, Criteria.ISNULL);
		crit.add(ENTITYTYPE, ENTITY_TYPE.ALL);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the entity change for master node failed with " + e.getMessage(), e);
		}
	}
	
	private static List<TEntityChangesBean> convertTorqueListToBeanList(List<TEntityChanges> torqueList) {		
		List<TEntityChangesBean> beanList = new LinkedList<TEntityChangesBean>();
		if (torqueList!=null) {
			for (TEntityChanges tEntityChanges : torqueList) {
				beanList.add(tEntityChanges.getBean());
			}
		}
		return beanList;
	}

	
}
