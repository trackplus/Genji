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
public class TWorkflowActivityBean
    extends com.aurel.track.beans.base.BaseTWorkflowActivityBean
    implements Serializable,ISerializableLabelBean{
	public static final long serialVersionUID = 400L;
	

	@Override
	public Map<String, String> serializeBean() {

		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("uuid", getUuid());

		Integer transitionActivity= getTransitionActivity();
		if(transitionActivity!=null){
			attributesMap.put("transitionActivity",transitionActivity.toString());
		}

		Integer stationEntryActivity= getStationEntryActivity();
		if(stationEntryActivity!=null){
			attributesMap.put("stationEntryActivity",stationEntryActivity.toString());
		}
		Integer stationDoActivity= getStationDoActivity();
		if(stationDoActivity!=null){
			attributesMap.put("stationDoActivity",stationDoActivity.toString());
		}
		Integer stationExitActivity= getStationExitActivity();
		if(stationExitActivity!=null){
			attributesMap.put("stationExitActivity",stationExitActivity.toString());
		}

		Integer groovyScript=getGroovyScript();
		if(groovyScript!=null){
			attributesMap.put("groovyScript",groovyScript.toString());
		}
		Integer newMan=getNewMan();
		if(newMan!=null){
			attributesMap.put("newMan",newMan.toString());
		}
		Integer newResp=getNewResp();
		if(newResp!=null){
			attributesMap.put("newResp",newResp.toString());
		}
		Integer activityType=getActivityType();
		if(activityType!=null){
			attributesMap.put("activityType",activityType.toString());
		}


		String activityParams=getActivityParams();
		if(activityParams!=null){
			attributesMap.put("activityParams",activityParams);
		}

		Integer fieldSetterRelation=getFieldSetterRelation();
		if(fieldSetterRelation!=null){
			attributesMap.put("fieldSetterRelation",fieldSetterRelation.toString());
		}
		String paramName=getParamName();
		if(paramName!=null){
			attributesMap.put("paramName",paramName);
		}
		Integer fieldSetMode=getFieldSetMode();
		if(fieldSetMode!=null){
			attributesMap.put("fieldSetMode",fieldSetMode.toString());
		}

		Integer sortOrder=getSortOrder();
		if(sortOrder!=null){
			attributesMap.put("sortOrder",sortOrder.toString());
		}

		Integer sla=getSla();
		if(sla!=null){
			attributesMap.put("sla",sla.toString());
		}

		Integer screen=getScreen();
		if(screen!=null){
			attributesMap.put("screen",screen.toString());
		}





		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			this.setObjectID(new Integer(strObjectID));
		}
		this.setUuid(attributes.get("uuid"));
		if(attributes.get("transitionActivity")!=null){
			this.setTransitionActivity(Integer.parseInt(attributes.get("transitionActivity")));
		}
		if(attributes.get("stationEntryActivity")!=null){
			this.setStationEntryActivity(Integer.parseInt(attributes.get("stationEntryActivity")));
		}
		if(attributes.get("stationDoActivity")!=null){
			this.setStationDoActivity(Integer.parseInt(attributes.get("stationDoActivity")));
		}
		if(attributes.get("stationExitActivity")!=null){
			this.setStationExitActivity(Integer.parseInt(attributes.get("stationExitActivity")));
		}

		if(attributes.get("groovyScript")!=null){
			this.setGroovyScript(Integer.parseInt(attributes.get("groovyScript")));
		}
		if(attributes.get("newMan")!=null){
			this.setNewMan(Integer.parseInt(attributes.get("newMan")));
		}
		if(attributes.get("newResp")!=null){
			this.setNewResp(Integer.parseInt(attributes.get("newResp")));
		}
		if(attributes.get("activityType")!=null){
			this.setActivityType(Integer.parseInt(attributes.get("activityType")));
		}


		if(attributes.get("activityParams")!=null){
			this.setActivityParams(attributes.get("activityParams"));
		}
		if(attributes.get("fieldSetterRelation")!=null){
			this.setFieldSetterRelation(Integer.parseInt(attributes.get("fieldSetterRelation")));
		}

		if(attributes.get("paramName")!=null){
			this.setParamName(attributes.get("paramName"));
		}

		if(attributes.get("fieldSetMode")!=null){
			this.setFieldSetMode(Integer.parseInt(attributes.get("fieldSetMode")));
		}

		if(attributes.get("sortOrder")!=null){
			this.setSortOrder(Integer.parseInt(attributes.get("sortOrder")));
		}

		if(attributes.get("sla")!=null){
			this.setSla(Integer.parseInt(attributes.get("sla")));
		}

		if(attributes.get("screen")!=null){
			this.setScreen(Integer.parseInt(attributes.get("screen")));
		}

		return this;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		TWorkflowActivityBean internalWorkflowActivity=(TWorkflowActivityBean)serializableLabelBean;
		if (internalWorkflowActivity == null) {
			return false;
		}
		String externalUuid = getUuid();
		String internalUuid = internalWorkflowActivity.getUuid();
		return EqualUtils.equalStrict(externalUuid, internalUuid);
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public String getLabel() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
