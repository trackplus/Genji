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

import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.dao.RoleFieldDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TRoleFieldPeer
    extends com.aurel.track.persist.BaseTRoleFieldPeer
    implements RoleFieldDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TRoleFieldPeer.class); 
	
	public static final long serialVersionUID = 400L;
	
	
	/**
	 * Gets all field restrictions for all roles
	 * @return
	 */
	@Override
	public List<TRoleFieldBean> loadAll() {
		Criteria  criteria = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Getting all limited fields failed with " + e.getMessage());
			return null;
		}
	}
	
	@Override
	public List<TRoleFieldBean> getByRole(Integer roleID) {
		Criteria  criteria = new Criteria();
		criteria.add(ROLEKEY, roleID);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Getting the limited fields for role " + roleID + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets the list of TRoleFieldBean by IDs 
	 * @param roleFieldBeanIDs 
	 * @return
	 */
	@Override
	public List<TRoleFieldBean> getRoleFieldsByKeys(List<Integer> roleFieldBeanIDs) {
		if (roleFieldBeanIDs==null || roleFieldBeanIDs.isEmpty()) {
			return new LinkedList<TRoleFieldBean>();
		}
		Criteria criteria = new Criteria();
		criteria.addIn(OBJECTID, roleFieldBeanIDs);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Getting the role fields by ids failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets the restricted fields in any role
	 * @param fieldIDs
	 * @return
	 */
	@Override
	public List<TRoleFieldBean> getByFields(List<Integer> fieldIDs) {
		if (fieldIDs==null || fieldIDs.isEmpty()) {
			return new LinkedList<TRoleFieldBean>();
		}
		Criteria crit = new Criteria();
		crit.addIn(FIELDKEY, fieldIDs);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the roleFieldBeans by fields " + fieldIDs.size() +  " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Saves a TRoleFieldBean
	 * @param roleFieldBean 
	 * @return
	 */
	@Override
	public void save(TRoleFieldBean roleFieldBean) {
		try {
			TRoleField roleField = TRoleField.createTRoleField(roleFieldBean);
			roleField.save();
		} catch (Exception e) {
			LOGGER.error("Saving the role field for role " + roleFieldBean.getRoleKey() + 
				" and field " + roleFieldBean.getFieldKey() + " failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * Deletes a TRoleFieldBean field by objectID
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the role field " + objectID + " failed with: " + e);
		}
	}
	
	
	public static List<TRoleFieldBean> convertTorqueListToBeanList(List<TRoleField> torqueList) {
		List<TRoleFieldBean> beanList = new LinkedList<TRoleFieldBean>();
		if (torqueList!=null){
			for (TRoleField tRoleField : torqueList) {
				beanList.add(tRoleField.getBean());
			}
		}
		return beanList;
	}
	
}
