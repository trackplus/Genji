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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.aurel.track.util.EqualUtils;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TWorkflowTransitionBean
    extends com.aurel.track.beans.base.BaseTWorkflowTransitionBean
    implements Serializable, ISerializableLabelBean, Comparable<TWorkflowTransitionBean>{
	public static interface TIME_UNIT {
		static final int MINUTE = 1;
		static final int HOUR = 2;
		static final int DAY = 3;
		static final int WEEK = 4;
		static final int MONTH = 5;
	}

	public static final long serialVersionUID = 400L;
	private List<TWorkflowActivityBean> activityBeanList;
	private List<TWorkflowGuardBean> guardBeanList;

	public List<TWorkflowActivityBean> getActivityBeanList() {
		return activityBeanList;
	}

	public void setActivityBeanList(List<TWorkflowActivityBean> activityBeanList) {
		this.activityBeanList = activityBeanList;
	}

	public List<TWorkflowGuardBean> getGuardBeanList() {
		return guardBeanList;
	}

	public void setGuardBeanList(List<TWorkflowGuardBean> guardBeanList) {
		this.guardBeanList = guardBeanList;
	}

	/**
	 * Used by sorting sets of personBeans by group and fullName
	 */
	@Override
	public int compareTo(TWorkflowTransitionBean o) {
		TWorkflowTransitionBean workflowTransitionBean = (TWorkflowTransitionBean)o;
		Integer myTime = getTimeInMinutes(getElapsedTime(), getTimeUnit());
		if (myTime==null) {
			myTime = 0;
		}
		Integer otherTime = getTimeInMinutes(workflowTransitionBean.getElapsedTime(), workflowTransitionBean.getTimeUnit());
		if (otherTime==null) {
			otherTime = 0;
		}
		return myTime.compareTo(otherTime);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof TWorkflowTransitionBean)) {
			return false;
		}
		TWorkflowTransitionBean workflowTransitionBean = (TWorkflowTransitionBean)obj;
		return Objects.equals(getObjectID(), workflowTransitionBean.getObjectID());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		Integer myTime = getTimeInMinutes(getElapsedTime(), getTimeUnit());
		if (myTime==null) {
			myTime = 0;
		}
		result = prime * result
				+ ((myTime == null) ? 0 : myTime.hashCode());
		return result;
	}

	/**
	 * Gets the time in minutes
	 * @param elapsedTime
	 * @param timeUnit
	 * @return
	 */
	private static Integer getTimeInMinutes(Integer elapsedTime, Integer timeUnit) {
		if (elapsedTime==null || timeUnit==null) {
			return null;
		} else {
			switch (timeUnit.intValue()) {
			case TIME_UNIT.MINUTE:
				return elapsedTime;
			case TIME_UNIT.HOUR:
				return elapsedTime * 60;
			case TIME_UNIT.DAY:
				return elapsedTime * 60 * 24;
			case TIME_UNIT.WEEK:
				return elapsedTime * 60 * 24 * 7;
			case TIME_UNIT.MONTH:
				return elapsedTime * 60 * 24 * 30;
			}
		}
		return null;
	}


	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		if(getStationFrom()!=null){
			attributesMap.put("fromID",getStationFrom().toString());
		}
		if(getStationTo()!=null){
			attributesMap.put("toID",getStationTo().toString());
		}
		if(getActionKey()!=null){
			attributesMap.put("actionID",getActionKey().toString());
		}
		if(getTransitionType()!=null){
			attributesMap.put("type",getTransitionType().toString());
		}
		if(getElapsedTime()!=null){
			attributesMap.put("elapsedTime",getElapsedTime().toString());
		}
		if(getTimeUnit()!=null){
			attributesMap.put("timeUnit",getTimeUnit().toString());
		}
		String cp=getControlPoints();
		if(cp!=null&&cp.trim().length()>0){
			attributesMap.put("controlPoints",cp);
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
		if(attributes.get("fromID")!=null){
			this.setStationFrom(Integer.parseInt(attributes.get("fromID")));
		}
		if(attributes.get("toID")!=null){
			this.setStationTo(Integer.parseInt(attributes.get("toID")));
		}
		if(attributes.get("actionID")!=null){
			this.setActionKey(Integer.parseInt(attributes.get("actionID")));
		}
		if(attributes.get("type")!=null){
			this.setTransitionType(Integer.parseInt(attributes.get("type")));
		}
		if(attributes.get("elapsedTime")!=null){
			this.setElapsedTime(Integer.parseInt(attributes.get("elapsedTime")));
		}
		if(attributes.get("timeUnit")!=null){
			this.setTimeUnit(Integer.parseInt(attributes.get("timeUnit")));
		}
		String cp=attributes.get("controlPoints");
		if(cp!=null&&cp.trim().length()>0){
			this.setControlPoints(cp);
		}
		this.setWorkflow(Integer.parseInt(attributes.get("workflow")));
		return this;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		TWorkflowTransitionBean internalWorkflowTransitionBean=(TWorkflowTransitionBean)serializableLabelBean;
		if (internalWorkflowTransitionBean == null) {
			return false;
		}

		String externalUuid = getUuid();
		String internalUuid = internalWorkflowTransitionBean.getUuid();

		return EqualUtils.equalStrict(externalUuid, internalUuid);
	}



	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public String getLabel() {
		return "";
	}
}
