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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.accessControl.AccessControlBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TAccessControlListBean
    extends com.aurel.track.beans.base.BaseTAccessControlListBean
    implements Serializable, ISerializableLabelBean
{
	private static final long serialVersionUID = 1L;
	
	
	public String getLabel() {
		return null;
	}

	public Integer getObjectID() {
		return null;
	}

	public void setObjectID(Integer objectID) {
	}

	/**
	 * Serialize a label bean to a dom element
	 * @param labelBean
	 * @return
	 */
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("personID", getPersonID().toString());
		attributesMap.put("roleID", getRoleID().toString());
		attributesMap.put("projectID", getProjectID().toString());
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
	
	/**
	 * Deserialze the labelBean 
	 * @param attributes
	 * @return
	 */
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TAccessControlListBean accessControlListBean = new TAccessControlListBean();
		String strPersonID = attributes.get("personID");
		if (strPersonID!=null) {
			accessControlListBean.setPersonID(new Integer(strPersonID));
		}
		String strRoleID = attributes.get("roleID");
		if (strRoleID!=null) {
			accessControlListBean.setRoleID(new Integer(strRoleID));
		}
		String strProjectID = attributes.get("projectID");
		if (strProjectID!=null) {
			accessControlListBean.setProjectID(new Integer(strProjectID));
		}
		accessControlListBean.setUuid(attributes.get("uuid"));
		return accessControlListBean;
	}
	
	/**
	 * Whether two label beans are equivalent: it will be never called  
	 * @param serializableLabelBean 
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TAccessControlListBean accessControlListBean = (TAccessControlListBean) serializableLabelBean;
		try {
			return getPersonID().equals(accessControlListBean.getPersonID()) && 
				getRoleID().equals(accessControlListBean.getRoleID()) && 
				getProjectID().equals(accessControlListBean.getProjectID());
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {
		TAccessControlListBean tAccessControlListBean = (TAccessControlListBean)serializableLabelBean;
		Integer personID = tAccessControlListBean.getPersonID();
		if (personID!=null) {
			Map<Integer, Integer> personMap = 
				matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
			if (personMap==null) {
				//no person map found 
				return null;
			} else {
				Integer internalPersonID = personMap.get(personID);
				if (internalPersonID==null) {
					//person can't be identified 
					return null;
				} else {
					tAccessControlListBean.setPersonID(internalPersonID);
				}
			}
		}
		Integer roleID = tAccessControlListBean.getRoleID();
		if (roleID!=null) {
			Map<Integer, Integer> roleMap = 
				matchesMap.get(ExchangeFieldNames.ROLE);
			if (roleMap==null) {
				//no role map found 
				return null;
			} else {
				Integer internalRoleID = roleMap.get(roleID);
				if (internalRoleID==null) {
					//role can't be identified 
					return null;
				} else {
					tAccessControlListBean.setRoleID(internalRoleID);
				}
			}
			
		}
		Integer projectID = tAccessControlListBean.getProjectID();
		if (projectID!=null) {
			Map<Integer, Integer> projectMap = 
				matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null));
			if (projectMap==null) {
				//no project map found 
			} else {
				Integer internalProject = projectMap.get(projectID);
				if (internalProject==null) {
					//project can't be identified 
					return null;
				} else {
					tAccessControlListBean.setProjectID(internalProject);
				}
			}
		}
		AccessControlBL.save(tAccessControlListBean);
		return null;
	}
}
