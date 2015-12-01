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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TPRoleBean;
import com.aurel.track.beans.TPlistTypeBean;
import com.aurel.track.dao.PRoleDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TPRolePeer
    extends com.aurel.track.persist.BaseTPRolePeer implements PRoleDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TPRolePeer.class);
	
	/**
	 * Load the TPRoleBean for a projectTypeID
	 * @param projectTypeID
	 * @return
	 */
	@Override
	public List<TPRoleBean> loadByProjectType(Integer projectTypeID) {
		Criteria crit = new Criteria();
        crit.add(PROJECTTYPE, projectTypeID);                
    	try {
    		return convertTorqueListToBeanList(doSelect(crit));
        } catch (Exception e) {
            LOGGER.error("Loading roles associated with projectType " + projectTypeID + " failed with " + e.getMessage());
            return new LinkedList<TPRoleBean>();
        }
	}
	
	/**
	 * Save  TPRoleBean in the TPRole table
	 * @param pRoleBean
	 * @return
	 */
	@Override
	public Integer save(TPRoleBean pRoleBean) {
		TPRole tpRole;		
		try {						
			tpRole = TPRole.createTPRole(pRoleBean);			
			tpRole.save();
			return tpRole.getObjectID();			
		} catch (Exception e) {
			LOGGER.error("Saving of an tpRoleBean failed with " + e.getMessage());
			return null;
		}	
	}
	
	/**
	 * Deletes a TPRoleBeans from the TPRole table 
	 * @param projectTypeID
	 * @param roleIDs
	 */
	@Override
	public void delete(Integer projectTypeID, List<Integer> roleIDs) {
		if (projectTypeID==null || roleIDs==null || roleIDs.isEmpty()) {
			return;
		}
		Criteria crit = new Criteria();
		crit.add(PROJECTTYPE, projectTypeID);
		crit.addIn(ROLEKEY, roleIDs);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting roles " + roleIDs.size() + " from projectType " + projectTypeID +  " failed with: " + e);
		}
	}
	
	/**
	 * Convert the torque objects to beans
	 * @param torqueList
	 * @return
	 */
	private List<TPRoleBean> convertTorqueListToBeanList(List<TPRole> torqueList) {		
		List<TPRoleBean> beanList = new LinkedList<TPRoleBean>();
		if (torqueList!=null) {
			for (TPRole tpRole : torqueList) {
				beanList.add(tpRole.getBean());
			}
		}
		return beanList;
	}
}
