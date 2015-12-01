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
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.beans.TConfigOptionsRoleBean;
import com.aurel.track.dao.ConfigOptionRoleDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TConfigOptionsRolePeer
	extends com.aurel.track.persist.BaseTConfigOptionsRolePeer
	implements ConfigOptionRoleDAO {

	private static final Logger LOGGER = LogManager.getLogger(TConfigOptionsRolePeer.class);
	public static final long serialVersionUID = 400L;
	
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Removing the option " + objectID + " failed with: " + e);
		}
	}

	@Override
	public boolean hasOptionRestriction(Integer configID) {
		List options = null;
		Criteria criteria = new Criteria();
		criteria.add(CONFIGKEY, configID);
		try {
			options = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Verifying the restriction failed with " + e.getMessage());
		}
		return options!=null && !options.isEmpty();
	}

	@Override
	public List loadOptionsByConfigForRoles(Integer configID, Integer person,  Integer project) {
		List resultList = new ArrayList();
		Criteria crit;
		Set<Integer> roles = AccessBeans.getRolesWithRightForPersonInProject(person, project, null);
		if (roles==null || roles.isEmpty()) {
			//the person has no role at all in the project
			crit = new Criteria();
			crit.add(CONFIGKEY, configID);
			List restrictionByRoleExists = null;
			try {
				restrictionByRoleExists = doSelect(crit);
			} catch (TorqueException e) {
				LOGGER.error("Getting the option restrictions by configID failed with " + e.getMessage());
			}
			if (restrictionByRoleExists!=null && !restrictionByRoleExists.isEmpty()) {
				//return an empty list meaning no option is available, 
				//because there are role restrictions but the person has no role in the project
				LOGGER.debug("No option available beacuse the options for configID "  + configID + 
						" are restricted by role but the person " + person + " has no role in project " + project);
				return resultList;
			} else {
				//no restriction exists for this configID -> return null which means the options should be 
				//loaded later, independently of role restrictions
				LOGGER.debug("No role restriction -> return null");
				return null;
			}			
		}
		
		Object[] roleIDs = roles.toArray();		
		List tConfigOptionsRole = null;		
		
		//does at least one right have an implicit access to the list type?
		crit = new Criteria();
		crit.addJoin(TRolePeer.PKEY, ROLEKEY);
		crit.addIn(TRolePeer.PKEY, roleIDs);
		crit.add(CONFIGKEY, configID);
		//get only the distinct roles
		crit.setDistinct();				
		try {
			tConfigOptionsRole = BaseTRolePeer.doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Getting the implicit options for a config failed with " + e.getMessage());
		}
		
		//at least one role the user has in project has no restrictions for options 
		if (tConfigOptionsRole!=null && tConfigOptionsRole.size()<roles.size()) {
			//load all options
			LOGGER.debug("At least one role the person " + person + " has in project " + project + " has no restrictions -> return  null");
			return null;
		}
		
		//only explicit options, search for explicit options
		crit = new Criteria();
		crit.addJoin(BaseTOptionPeer.OBJECTID, BaseTConfigOptionsRolePeer.OPTIONKEY);
		crit.addIn(BaseTConfigOptionsRolePeer.ROLEKEY, roleIDs);
		crit.add(BaseTConfigOptionsRolePeer.CONFIGKEY, configID);
		crit.addAscendingOrderByColumn(BaseTOptionPeer.SORTORDER);
		crit.setDistinct();
		try {
			return TOptionPeer.convertTorqueListToBeanList(BaseTOptionPeer.doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Getting the explicit options failed with " + e.getMessage());
		}
		return resultList;
	}
		
	@Override
	public Integer save(TConfigOptionsRoleBean configOptionsRoleBean) {
		TConfigOptionsRole tConfigOptionsRole;
		try {
			tConfigOptionsRole = BaseTConfigOptionsRole.createTConfigOptionsRole(configOptionsRoleBean);
			tConfigOptionsRole.save();
			return tConfigOptionsRole.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Adding of an option for role and config failed with " + e.getMessage());
			return null;
		}
	}
}
