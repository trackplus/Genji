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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.resources.LocalizationKeyPrefixes;

/**
 * <p>
 * This class handles roles with access permissions. Access to issues is granted via
 * access control lists in database table <code>TACL</code> that relate a role, a person
 * or group, and a project. Thus, access rights are granted to persons specifically for
 * a project via roles.
 * </p>
 * <p>
 * Access permissions are stored as a single string of "1"s and "0"s in a role entry, attribute
 * "extendedAccessKey". A "1" at a certain position means that the related permission is granted, a
 * "0" means the permission is denied.
 * </p>
 * <p> The permissions and their related positions are defined and documented 
 * in {@link AccessBeans.AccessFlagIndexes}.
 * </p>
 */
public class TRoleBean extends com.aurel.track.beans.base.BaseTRoleBean
	implements Serializable, ILocalizedLabelBean, ISerializableLabelBean {
		
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Override
	public String getKeyPrefix() {
		return LocalizationKeyPrefixes.ROLE_KEYPREFIX;
	}

	/**
	 * Serialize a label bean to a dom element
	 * @return
	 */
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("label", getLabel());
		Integer accesskey = getAccesskey();
		if (accesskey!=null) {
			attributesMap.put("accesskey", accesskey.toString());
		}
		attributesMap.put("extendedaccesskey", getExtendedaccesskey());
		Integer projectType = getProjecttype();
		if (projectType!=null) {
			attributesMap.put("projectType", projectType.toString());
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
	
	/**
	 * Deserialze the labelBean 
	 * @param attributes
	 * @return
	 */
	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TRoleBean roleBean = new TRoleBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			roleBean.setObjectID(new Integer(strObjectID));
		}
		roleBean.setLabel(attributes.get("label"));
		String strAccessKey = attributes.get("accesskey");
		if (strAccessKey!=null) {
			roleBean.setAccesskey(new Integer(strAccessKey));
		}
		roleBean.setExtendedaccesskey(attributes.get("extendedaccesskey"));
		String strProjectType = attributes.get("projectType");
		if (strProjectType!=null) {
			roleBean.setProjecttype(new Integer(strProjectType));
		}
		roleBean.setUuid(attributes.get("uuid"));
		return roleBean;
	}
	
	/**
	 * Whether two label beans are equivalent  
	 * @param serializableLabelBean 
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		TRoleBean roleBean = (TRoleBean) serializableLabelBean;
		String externLabel = getLabel();
		String internLabel = roleBean.getLabel();
		if (externLabel!=null && internLabel!=null) {
			return externLabel.equals(internLabel);
		}
		return false;
	}
	
	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {
		TRoleBean roleBean = (TRoleBean) serializableLabelBean;	
		Integer projectType = roleBean.getProjecttype();
		if (projectType!=null) {
			Map<Integer, Integer> projetcTypeMap = matchesMap.get(ExchangeFieldNames.PROJECTTYPE);
			roleBean.setProjecttype(projetcTypeMap.get(projectType));
		}
		return RoleBL.save(roleBean);
	}

}
