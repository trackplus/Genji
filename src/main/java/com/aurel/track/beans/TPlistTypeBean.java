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
import com.aurel.track.dao.PIssueTypeDAO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TPlistTypeBean
    extends com.aurel.track.beans.base.BaseTPlistTypeBean
    implements Serializable,ISerializableLabelBean{
	public static final long serialVersionUID = 400L;

	private static PIssueTypeDAO pIssueTypeDAO = DAOFactory.getFactory().getPIssueTypeDAO();

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("projectType", getProjectType().toString());
		attributesMap.put("category", getCategory().toString());
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TPlistTypeBean plistTypeBean = new TPlistTypeBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			plistTypeBean.setObjectID(new Integer(strObjectID));
		}
		String projectTypeStr = attributes.get("projectType");
		if (projectTypeStr!=null) {
			plistTypeBean.setProjectType(new Integer(projectTypeStr));
		}
		String categoryStr = attributes.get("category");
		if (categoryStr!=null) {
			plistTypeBean.setCategory(new Integer(categoryStr));
		}

		plistTypeBean.setUuid(attributes.get("uuid"));
		return plistTypeBean;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TPlistTypeBean plistTypeBean = (TPlistTypeBean)serializableLabelBean;
		Integer externalProjectType=plistTypeBean.getProjectType();
		Integer internalProjectType = getProjectType();
		Integer externalCategory=plistTypeBean.getCategory();
		Integer internalCategory = getCategory();
		if (externalProjectType!=null && internalProjectType!=null&&
				externalCategory!=null&&internalCategory!=null){
			return externalProjectType.equals(internalProjectType)&&externalCategory.equals(internalCategory);
		}
		return false;
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return pIssueTypeDAO.save((TPlistTypeBean)serializableLabelBean);
	}

	@Override
	public String getLabel() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
