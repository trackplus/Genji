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
import java.util.Map;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TScreenConfigBean
    extends com.aurel.track.beans.base.BaseTScreenConfigBean
    implements Serializable,ConfigItem, ISerializableLabelBean{
	
	public static final long serialVersionUID = 400L;
	
	public Integer getConfigRel() {
		return getAction();
	}
	public void setConfigRel(Integer configRel) {
		setAction(configRel);
	}
	
	/**
	 * Whether the config is a default (global) one 
	 * @return
	 */
	public boolean isDefault() {
		return getProjectType()==null && getProject()==null && getIssueType()==null;
	}

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		if(getName()!=null){
			attributesMap.put("name", getName());
		}
		if(getDescription()!=null){
			attributesMap.put("description", getDescription());
		}
		if(getScreen()!=null){
			attributesMap.put("screen", getScreen().toString());
		}
		if(getIssueType()!=null){
			attributesMap.put("issueType", getIssueType().toString());
		}
		if(getProjectType()!=null){
			attributesMap.put("projectType", getProjectType().toString());
		}
		if(getProject()!=null){
			attributesMap.put("project", getProject().toString());
		}
		if(getAction()!=null){
			attributesMap.put("action", getAction().toString());
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TScreenConfigBean screenConfigBean = new TScreenConfigBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			screenConfigBean.setObjectID(new Integer(strObjectID));
		}
		String description = attributes.get("description");
		screenConfigBean.setName(description);

		String screen = attributes.get("screen");
		if (screen!=null) {
			screenConfigBean.setScreen(new Integer(screen));
		}
		String issueType = attributes.get("issueType");
		if (issueType!=null) {
			screenConfigBean.setIssueType(new Integer(issueType));
		}
		String projectTypeStr = attributes.get("projectType");
		if (projectTypeStr!=null) {
			screenConfigBean.setProjectType(new Integer(projectTypeStr));
		}
		String project = attributes.get("project");
		if (project!=null) {
			screenConfigBean.setProject(new Integer(project));
		}
		String action = attributes.get("action");
		if (action!=null) {
			screenConfigBean.setAction(new Integer(action));
		}

		screenConfigBean.setUuid(attributes.get("uuid"));
		return screenConfigBean;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		TScreenConfigBean screenConfigBean = (TScreenConfigBean)serializableLabelBean;

		Integer externalScreen=screenConfigBean.getScreen();
		Integer internalScreen = getScreen();

		Integer externalIssueType=screenConfigBean.getIssueType();
		Integer internalIssueType = getIssueType();

		Integer externalProjectType=screenConfigBean.getProjectType();
		Integer internalProjectType = getProjectType();

		Integer externalProject=screenConfigBean.getProject();
		Integer internalProject = getProject();

		Integer externalAction=screenConfigBean.getAction();
		Integer internalAction = getAction();

		return EqualUtils.equal(externalScreen,internalScreen)&&
				EqualUtils.equal(externalIssueType,internalIssueType)&&
				EqualUtils.equal(externalProjectType,internalProjectType)&&
				EqualUtils.equal(externalProject,internalProject)&&
				EqualUtils.equal(externalAction,internalAction);
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return null;
	}

	@Override
	public String getLabel() {
		return null;
	}
}
