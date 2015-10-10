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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TRoleListTypeBean;
import com.aurel.track.dao.RoleListTypeDAO;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Thu Aug 26 15:01:17 CEST 2004]
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TRoleListTypePeer
    extends com.aurel.track.persist.BaseTRoleListTypePeer
    implements RoleListTypeDAO
{

    private static final long serialVersionUID = -1168712377676504178L;
    private static final Logger LOGGER = LogManager.getLogger(TRoleListTypePeer.class); 
    /**
     * Filter out the roles which have no correspondent issueType in the TRoleListType table
     * but have at least one other issueType assigned
     * @param selectedRoleIDs
     * @param issueType
     * @return
     */
	/*public static List<Integer> filterRoleByIssueType(List<Integer> selectedRoleIDs, Integer issueType) {
		List filteredRoleIDs = new ArrayList();
		if (selectedRoleIDs==null || selectedRoleIDs.isEmpty()) {
			return filteredRoleIDs;
		}
		List assignmentsList = null;
        Criteria criteria = new Criteria();
        criteria.addIn(PROLE, selectedRoleIDs);
        try {
        	assignmentsList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the roles to issueTypes by roles " + selectedRoleIDs + " failed with " + e.getMessage(), e);
		}
		//assignments found with this issue
		Set<Integer> foundWithAccordingIssueType = new HashSet<Integer>();
		//assignments found with other issue
		Set<Integer> foundWithOtherIssueType = new HashSet<Integer>();		
		if (assignmentsList!=null) {
			Iterator iterator = assignmentsList.iterator();
			while (iterator.hasNext()) {
				TRoleListType roleListType = (TRoleListType) iterator.next();
				if (issueType.equals(roleListType.getListType())) {
					foundWithAccordingIssueType.add(roleListType.getRole());					
				} else {
					foundWithOtherIssueType.add(roleListType.getRole());
				}
			}
		}
		Iterator iterator = selectedRoleIDs.iterator();
		while (iterator.hasNext()) {
			Integer roleID = (Integer) iterator.next();
			if (foundWithAccordingIssueType.contains(roleID) || !foundWithOtherIssueType.contains(roleID)) {
				filteredRoleIDs.add(roleID);
			}			
		}
		return filteredRoleIDs;
	}*/
	
	/**
	 * Loads all RoleListTypeBeans
	 * @return 
	 */
	public List<TRoleListTypeBean> loadAll() {
		Criteria criteria = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading all roleListTypes failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the listTypes assigned to a role
	 * @param roleID
	 * @return
	 */
	public List<TRoleListTypeBean> loadByRole(Integer roleID) {
		Criteria criteria = new Criteria();
		criteria.addJoin(LISTTYPE, TListTypePeer.PKEY);
		criteria.add(PROLE, roleID);
		criteria.addAscendingOrderByColumn(TListTypePeer.SORTORDER);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading roleListTypes for role " + roleID +  " failed with " + e.getMessage(), e);
			return null;
		}			
	}
	
	/**
	 * Load the role to list type assignments for roles
	 * @param roleIDs
	 * @return
	 */
	public List<TRoleListTypeBean> loadByRoles(List<Integer> roleIDs) {
		if (roleIDs==null || roleIDs.isEmpty()) {
			return new LinkedList<TRoleListTypeBean>();
		}
		Criteria criteria = new Criteria();		
		criteria.addIn(PROLE, roleIDs);		
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading roleListTypes for roles " + roleIDs.size() +
					" failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the role list types by roles and listType 
	 * @param roleIDs
	 * @param listType
	 * @return
	 */
	public List<TRoleListTypeBean> loadByRolesAndListType(Object[] roleIDs, Integer listType) {
		if (roleIDs==null || roleIDs.length==0) {
			return new LinkedList<TRoleListTypeBean>();
		}
		Criteria criteria = new Criteria();
		criteria.addIn(PROLE, roleIDs);
		if (listType!=null) {
			criteria.add(LISTTYPE, listType);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading roleListTypes for roles " + roleIDs.length +
					" and listType " + listType +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	private static List<TRoleListTypeBean> convertTorqueListToBeanList(List<TRoleListType> torqueList) {
		List<TRoleListTypeBean> beanList = new LinkedList<TRoleListTypeBean>();
		if (torqueList!=null){
			for (TRoleListType tRoleListType : torqueList) {
				beanList.add(tRoleListType.getBean());
			}
		}
		return beanList;
	}
	
	/**
	 * Saves a roleListTypebean
	 * @param roleBean
	 * @return the created optionID
	 */
	public Integer save(TRoleListTypeBean roleListTypebean) {
		try {			
			TRoleListType tToleListType = BaseTRoleListType.createTRoleListType(roleListTypebean);
			tToleListType.save();
			Integer objectID = tToleListType.getObjectID();			
			return objectID;
		} catch (Exception e) {
			LOGGER.error("Saving of a roleListType failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes a roleListTypebean
	 * @param roleID
	 * @param issueTypeID
	 */
	public void delete(Integer roleID, Integer issueTypeID) {
		Criteria crit = new Criteria();
        crit.add(PROLE, roleID);
        crit.add(LISTTYPE, issueTypeID);
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting a roleListType for role " + roleID + 
        			" and issueType " +  issueTypeID + " failed with: " + e);
        }    	
	}
}