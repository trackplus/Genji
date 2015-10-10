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

import com.aurel.track.beans.TClusterNodeBean;

/**
 * DAO for cluster nodes
 * @author Tamas Ruff
 *
 */
public interface ClusterNodeDAO {
	
	/**
	 * Load all cluster nodes
	 * @return
	 */
	List<TClusterNodeBean> loadAll();
	
	/**
	 * Loads cluster node by IP
	 * @return 
	 */
	List<TClusterNodeBean> loadByIP(String ip);
		
	/**
	 * Saves a cluster node in ClusterNode table.
	 * @param clusterNodeBean
	 * @return
	 */
	Integer save(TClusterNodeBean clusterNodeBean);
	
	/**
	 * Deletes a cluster node by ID
	 * @param clusterNodeID
	 * @return
	 */
	void delete(Integer clusterNodeID);
	
	
}
