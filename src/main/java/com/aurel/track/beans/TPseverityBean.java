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

import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PSeverityDAO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TPseverityBean
    extends com.aurel.track.beans.base.BaseTPseverityBean
    implements Serializable,ISerializableLabelBean{
	public static final long serialVersionUID = 400L;
	private static PSeverityDAO pSeverityDAO = DAOFactory.getFactory().getPSeverityDAO();

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("projectType", getProjectType().toString());
		attributesMap.put("listType", getListType().toString());
		attributesMap.put("severity", getSeverity().toString());
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TPseverityBean pstateBean = new TPseverityBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			pstateBean.setObjectID(new Integer(strObjectID));
		}
		String projectTypeStr = attributes.get("projectType");
		if (projectTypeStr!=null) {
			pstateBean.setProjectType(new Integer(projectTypeStr));
		}
		String listTypeStr = attributes.get("listType");
		if (listTypeStr!=null) {
			pstateBean.setListType(new Integer(listTypeStr));
		}
		String severityStr = attributes.get("severity");
		if (severityStr!=null) {
			pstateBean.setSeverity(new Integer(severityStr));
		}

		pstateBean.setUuid(attributes.get("uuid"));
		return pstateBean;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TPseverityBean pstateBean = (TPseverityBean)serializableLabelBean;
		Integer externalProjectType=pstateBean.getProjectType();
		Integer internalProjectType = getProjectType();
		Integer externalListType=pstateBean.getListType();
		Integer internalListType = getListType();
		Integer externalSeverity=pstateBean.getSeverity();
		Integer internalSeverity = getSeverity();
		if (externalProjectType!=null && internalProjectType!=null&&
				externalListType!=null&&internalListType!=null&&
				externalSeverity!=null&&internalSeverity!=null){
			return externalProjectType.equals(internalProjectType)&&externalListType.equals(internalListType)&&
					externalSeverity.equals(internalSeverity);
		}
		return false;
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return pSeverityDAO.save((TPseverityBean)serializableLabelBean);
	}

	@Override
	public String getLabel() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
