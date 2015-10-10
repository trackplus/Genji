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

import com.aurel.track.util.EqualUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TWorkflowStationBean
    extends com.aurel.track.beans.base.BaseTWorkflowStationBean
    implements Serializable,ISerializableLabelBean{
	public static final long serialVersionUID = 400L;

	@Override
	public String getLabel() {
		return getName();
	}

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		String mailSubject = getName();
		if (mailSubject!=null && !"".equals(mailSubject)) {
			attributesMap.put("name", mailSubject);
		}
		if(getNodeX()!=null){
			attributesMap.put("x", getNodeX().toString());
		}
		if(getNodeY()!=null){
			attributesMap.put("y", getNodeY().toString());
		}
		if(getStationType()!=null){
			attributesMap.put("type", getStationType().toString());
		}
		if(getStatus()!=null){
			attributesMap.put("stateID", getStatus().toString());
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
		this.setName(attributes.get("name"));
		this.setUuid(attributes.get("uuid"));
		if(attributes.get("x")!=null){
			this.setNodeX(Integer.parseInt(attributes.get("x")));
		}
		if(attributes.get("y")!=null){
			this.setNodeY(Integer.parseInt(attributes.get("y")));
		}
		if(attributes.get("type")!=null){
			this.setStationType(Integer.parseInt(attributes.get("type")));
		}
		if(attributes.get("stateID")!=null){
			this.setStatus(Integer.parseInt(attributes.get("stateID")));
		}
		this.setWorkflow(Integer.parseInt(attributes.get("workflow")));
		return this;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		TWorkflowStationBean internalWorkflowStationBean=(TWorkflowStationBean)serializableLabelBean;
		if (internalWorkflowStationBean == null) {
			return false;
		}

		String externalUuid = getUuid();
		String internalUuid = internalWorkflowStationBean.getUuid();

		return EqualUtils.equalStrict(externalUuid, internalUuid);
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}



	public static interface STATION_TYPE {
		public static final int STANDARD = 0;
		public static final int START = 1;
		public static final int END= 2;
	}
	
	private List<TWorkflowActivityBean> activityBeanList;

	public List<TWorkflowActivityBean> getActivityBeanList() {
		return activityBeanList;
	}

	public void setActivityBeanList(List<TWorkflowActivityBean> activityBeanList) {
		this.activityBeanList = activityBeanList;
	}
}
