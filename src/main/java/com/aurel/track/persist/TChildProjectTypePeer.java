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

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TChildProjectTypeBean;
import com.aurel.track.dao.ChildProjectTypeDAO;

/**
 * Describes which project type can have a child project depending on the a project type of parent project
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TChildProjectTypePeer
    extends com.aurel.track.persist.BaseTChildProjectTypePeer implements ChildProjectTypeDAO
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TChildProjectTypePeer.class);
	/**
	 * Load the TChildIssueTypeBean for a parentIssueType 
	 * @param parentProjectType
	 * @return
	 */
	@Override
	public List<TChildProjectTypeBean> loadAssignmentsByParent(Integer parentProjectType) {
		Criteria crit = new Criteria();
		crit.add(PROJECTTYPEPARENT, parentProjectType);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading issueTypes by parentIssueType failed with " + e.getMessage());
			return null;
		}
	}
	
	
	/**
	 * Save TChildProjectTypeBean in the TChildProjectType table
	 * @param parentProjectType
	 * @return
	 */
	@Override
	public Integer save(TChildProjectTypeBean parentProjectTypeBean) {
		TChildProjectType childProjectType;
		try {
			childProjectType = TChildProjectType.createTChildProjectType(parentProjectTypeBean);
			childProjectType.save();
			return childProjectType.getObjectID();
			
		} catch (Exception e) {
			LOGGER.error("Saving of an childProjectTypeBean failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Deletes TChildProjectTypeBeans by parent and children 
	 * @param parentProjectTypeID
	 * @param childProjectTypeIDs
	 * @return
	 */
	@Override
	public void delete(Integer parentProjectTypeID, List<Integer> childProjectTypeIDs) {
		if (parentProjectTypeID==null || childProjectTypeIDs==null || childProjectTypeIDs.isEmpty()) {
			return;
		}
		Criteria crit = new Criteria();
		crit.add(PROJECTTYPEPARENT, parentProjectTypeID);
		crit.addIn(PROJECTTYPECHILD, childProjectTypeIDs);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting childProjectTypeBeans " + childProjectTypeIDs.size() + " from parentProjectType " + parentProjectTypeID +  " failed with: " + e);
		}
	}
	
	private List<TChildProjectTypeBean> convertTorqueListToBeanList(List<TChildProjectType> torqueList) {
		List<TChildProjectTypeBean> beanList = new LinkedList<TChildProjectTypeBean>();
		if (torqueList!=null) {
			for (TChildProjectType tChildProjectType : torqueList) {
				beanList.add(tChildProjectType.getBean());
			}
		}
		return beanList;
	}
}
