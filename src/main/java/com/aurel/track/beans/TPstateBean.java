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
import com.aurel.track.dao.PStatusDAO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TPstateBean
    extends com.aurel.track.beans.base.BaseTPstateBean
    implements Serializable,ISerializableLabelBean{
	public static final long serialVersionUID = 400L;

	private static PStatusDAO pStatusDAO = DAOFactory.getFactory().getPStatusDAO();

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("projectType", getProjectType().toString());
		attributesMap.put("listType", getListType().toString());
		attributesMap.put("state", getState().toString());
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TPstateBean pstateBean = new TPstateBean();
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
		String stateStr = attributes.get("state");
		if (stateStr!=null) {
			pstateBean.setState(new Integer(stateStr));
		}

		pstateBean.setUuid(attributes.get("uuid"));
		return pstateBean;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TPstateBean pstateBean = (TPstateBean)serializableLabelBean;
		Integer externalProjectType=pstateBean.getProjectType();
		Integer internalProjectType = getProjectType();
		Integer externalListType=pstateBean.getListType();
		Integer internalListType = getListType();
		Integer externalState=pstateBean.getState();
		Integer internalState = getState();
		if (externalProjectType!=null && internalProjectType!=null&&
				externalListType!=null&&internalListType!=null&&
				externalState!=null&&internalState!=null){
			return externalProjectType.equals(internalProjectType)&&externalListType.equals(internalListType)&&
					externalState.equals(internalState);
		}
		return false;
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return pStatusDAO.save((TPstateBean)serializableLabelBean);
	}

	@Override
	public String getLabel() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
