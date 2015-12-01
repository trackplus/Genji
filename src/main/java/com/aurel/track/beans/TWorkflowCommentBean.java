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

import com.aurel.track.util.EqualUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TWorkflowCommentBean
    extends com.aurel.track.beans.base.BaseTWorkflowCommentBean
	implements Serializable,ISerializableLabelBean{

	@Override
	public String getLabel() {
		return getObjectID()+"";
	}

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		if(getNodeX()!=null){
			attributesMap.put("x", getNodeX().toString());
		}
		if(getNodeY()!=null){
			attributesMap.put("y", getNodeY().toString());
		}
		if(getDescription()!=null){
			attributesMap.put("text", getDescription());
		}
		attributesMap.put("uuid", getUuid());
		attributesMap.put("workflow", getWorkflow().toString());

		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			this.setObjectID(new Integer(strObjectID));
		}
		this.setUuid(attributes.get("uuid"));
		if(attributes.get("x")!=null){
			this.setNodeX(Integer.parseInt(attributes.get("x")));
		}
		if(attributes.get("y")!=null){
			this.setNodeY(Integer.parseInt(attributes.get("y")));
		}
		if(attributes.get("text")!=null){
			this.setDescription(attributes.get("text"));
		}
		this.setWorkflow(Integer.parseInt(attributes.get("workflow")));
		return this;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		TWorkflowCommentBean internalWorkflowCommentBean=(TWorkflowCommentBean)serializableLabelBean;
		if (internalWorkflowCommentBean == null) {
			return false;
		}

		String externalUuid = getUuid();
		String internalUuid = internalWorkflowCommentBean.getUuid();

		return EqualUtils.equalStrict(externalUuid, internalUuid);
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

}
