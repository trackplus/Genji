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

import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PRoleDAO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TPRoleBean
    extends com.aurel.track.beans.base.BaseTPRoleBean
		implements Serializable,ISerializableLabelBean{
	private static PRoleDAO pRoleDAO = DAOFactory.getFactory().getPRoleDAO();

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("projectType", getProjectType().toString());
		attributesMap.put("role", getRole().toString());
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TPRoleBean tpRoleBean = new TPRoleBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			tpRoleBean.setObjectID(new Integer(strObjectID));
		}
		String projectTypeStr = attributes.get("projectType");
		if (projectTypeStr!=null) {
			tpRoleBean.setProjectType(new Integer(projectTypeStr));
		}
		String roleStr = attributes.get("role");
		if (roleStr!=null) {
			tpRoleBean.setRole(new Integer(roleStr));
		}
		tpRoleBean.setUuid(attributes.get("uuid"));
		return tpRoleBean;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TPRoleBean tpRoleBean = (TPRoleBean)serializableLabelBean;
		Integer externalProjectType=tpRoleBean.getProjectType();
		Integer internalProjectType = getProjectType();
		Integer externalRole=tpRoleBean.getRole();
		Integer internalRole = getRole();
		if (externalProjectType!=null && internalProjectType!=null&&
				externalRole!=null&&internalRole!=null){
			return externalProjectType.equals(internalProjectType)&&externalRole.equals(internalRole);
		}
		return false;
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return pRoleDAO.save((TPRoleBean)serializableLabelBean);
	}

	@Override
	public String getLabel() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
