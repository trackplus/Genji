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
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TChildIssueTypeBean;
import com.aurel.track.dao.ChildIssueTypeDAO;

/**
 * Describes that a parent item's issue type which issue types can have as child
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TChildIssueTypePeer
	extends com.aurel.track.persist.BaseTChildIssueTypePeer implements ChildIssueTypeDAO
{
	public static final long serialVersionUID = 400L;
	
	private static final Logger LOGGER = LogManager.getLogger(TChildIssueTypePeer.class);
	/**
	 * Loads all TChildIssueTypeBean 
	 * @return
	 */
	@Override
	public List<TChildIssueTypeBean> loadAll() {	
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all childIssueTypeBean failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Load the TChildIssueTypeBean for childIssueTypes (changed rows)
	 * @param childIssueTypes
	 * @return
	 */
	@Override
	public List<TChildIssueTypeBean> loadByChildAssignments(List<Integer> childIssueTypes) {
		if (childIssueTypes!=null && !childIssueTypes.isEmpty()) {
			Criteria crit = new Criteria();
			crit.addIn(ISSUETYPECHILD, childIssueTypes);
			try {
				return convertTorqueListToBeanList(doSelect(crit));
			} catch (Exception e) {
				LOGGER.error("Loading issueTypes by childIssueTypes failed with " + e.getMessage());
				return null;
			}
		}
		return new LinkedList<TChildIssueTypeBean>();
	}
	
	/**
	 * Load the TChildIssueTypeBean for a parentIssueType 
	 * @param parentIssueType
	 * @return
	 */
	@Override
	public List<TChildIssueTypeBean> loadByChildAssignmentsByParent(Integer parentIssueType) {
		Criteria crit = new Criteria();
		crit.add(ISSUETYPEPARENT, parentIssueType);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading issueTypes by parentIssueType failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Save  TChildIssueTypeBean in the TChildIssueType table
	 * @param childIssueTypeBean
	 * @return
	 */
	@Override
	public Integer save(TChildIssueTypeBean childIssueTypeBean) {
		TChildIssueType childIssueType;
		try {
			childIssueType = BaseTChildIssueType.createTChildIssueType(childIssueTypeBean);
			childIssueType.save();
			return childIssueType.getObjectID();
			
		} catch (Exception e) {
			LOGGER.error("Saving of an childIssueTypeBean failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Deletes a TChildIssueTypeBean from the TChildIssueType table 
	 * @param objectID
	 * @return
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the childIssueTypeBean " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Deletes TChildIssueTypeBean by parent and children 
	 * @param parentIssueTypeID
	 * @param childIssueTypeID
	 * @return
	 */
	@Override
	public void delete(Integer parentIssueTypeID, List<Integer> childIssueTypeIDs) {
		if (parentIssueTypeID==null || childIssueTypeIDs==null || childIssueTypeIDs.isEmpty()) {
			return;
		}
		Criteria crit = new Criteria();
		crit.add(ISSUETYPEPARENT, parentIssueTypeID);
		crit.addIn(ISSUETYPECHILD, childIssueTypeIDs);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the childIssueTypeBean " + childIssueTypeIDs + " from parentIssueType " + parentIssueTypeID +  " failed with: " + e);
		}
	}
	
	private List<TChildIssueTypeBean> convertTorqueListToBeanList(List<TChildIssueType> torqueList) {
		List<TChildIssueTypeBean> beanList = new ArrayList<TChildIssueTypeBean>();
		if (torqueList!=null) {
			for (TChildIssueType tChildIssueType : torqueList) {
				beanList.add(tChildIssueType.getBean());
			}
		}
		return beanList;
	}
}
