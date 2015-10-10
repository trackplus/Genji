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

import com.aurel.track.dao.ChildProjectTypeDAO;
import com.aurel.track.dao.DAOFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Describes which project type can have a parent project's child project depending on the a project type of parent project
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TChildProjectTypeBean
    extends com.aurel.track.beans.base.BaseTChildProjectTypeBean
    implements Serializable,ISerializableLabelBean{

	private static ChildProjectTypeDAO childProjectTypeDAO = DAOFactory.getFactory().getChildProjectTypeDAO();

	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("projectTypeParent", getProjectTypeParent().toString());
		attributesMap.put("projectTypeChild", getProjectTypeChild().toString());
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TChildProjectTypeBean childProjectTypeBean = new TChildProjectTypeBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			childProjectTypeBean.setObjectID(new Integer(strObjectID));
		}

		String projectTypeParentStr = attributes.get("projectTypeParent");
		if (projectTypeParentStr!=null) {
			childProjectTypeBean.setProjectTypeParent(new Integer(projectTypeParentStr));
		}

		String projectTypeChildStr = attributes.get("projectTypeChild");
		if (projectTypeChildStr!=null) {
			childProjectTypeBean.setProjectTypeChild(new Integer(projectTypeChildStr));
		}

		childProjectTypeBean.setUuid(attributes.get("uuid"));
		return childProjectTypeBean;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TChildProjectTypeBean childProjectTypeBean = (TChildProjectTypeBean)serializableLabelBean;

		Integer externalProjectTypeParent=childProjectTypeBean.getProjectTypeParent();
		Integer internalProjectTypeParent = getProjectTypeParent();

		Integer externalProjectTypeChild=childProjectTypeBean.getProjectTypeChild();
		Integer internalProjectTypeChild = getProjectTypeChild();

		if (externalProjectTypeParent!=null && internalProjectTypeParent!=null&&
				externalProjectTypeChild!=null&&internalProjectTypeChild!=null){
			return externalProjectTypeParent.equals(internalProjectTypeParent)&&externalProjectTypeChild.equals(internalProjectTypeChild);
		}
		return false;
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return childProjectTypeDAO.save((TChildProjectTypeBean)serializableLabelBean);
	}

	@Override
	public String getLabel() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
