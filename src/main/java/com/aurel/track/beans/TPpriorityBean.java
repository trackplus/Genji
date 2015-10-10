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
import com.aurel.track.dao.PPriorityDAO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TPpriorityBean
    extends com.aurel.track.beans.base.BaseTPpriorityBean
    implements Serializable,ISerializableLabelBean{
	public static final long serialVersionUID = 400L;
	private static PPriorityDAO pPriorityDAO = DAOFactory.getFactory().getPPriorityDAO();

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("projectType", getProjectType().toString());
		attributesMap.put("listType", getListType().toString());
		attributesMap.put("priority", getPriority().toString());
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TPpriorityBean ppriorityBean = new TPpriorityBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			ppriorityBean.setObjectID(new Integer(strObjectID));
		}
		String projectTypeStr = attributes.get("projectType");
		if (projectTypeStr!=null) {
			ppriorityBean.setProjectType(new Integer(projectTypeStr));
		}
		String listTypeStr = attributes.get("listType");
		if (listTypeStr!=null) {
			ppriorityBean.setListType(new Integer(listTypeStr));
		}
		String priorityStr = attributes.get("priority");
		if (priorityStr!=null) {
			ppriorityBean.setPriority(new Integer(priorityStr));
		}

		ppriorityBean.setUuid(attributes.get("uuid"));
		return ppriorityBean;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TPpriorityBean ppriorityBean = (TPpriorityBean)serializableLabelBean;
		Integer externalProjectType=ppriorityBean.getProjectType();
		Integer internalProjectType = getProjectType();
		Integer externalListType=ppriorityBean.getListType();
		Integer internalListType = getListType();
		Integer externalPriority=ppriorityBean.getPriority();
		Integer internalpriority = getPriority();
		if (externalProjectType!=null && internalProjectType!=null&&
				externalListType!=null&&internalListType!=null&&
				externalPriority!=null&&internalpriority!=null){
			return externalProjectType.equals(internalProjectType)&&externalListType.equals(internalListType)&&
					externalPriority.equals(internalpriority);
		}
		return false;
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return pPriorityDAO.save((TPpriorityBean)serializableLabelBean);
	}

	@Override
	public String getLabel() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
