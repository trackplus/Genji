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
public class TWorkflowConnectBean
    extends com.aurel.track.beans.base.BaseTWorkflowConnectBean
    implements Serializable, ConfigItem,ISerializableLabelBean{
	public static final long serialVersionUID = 400L;
	
	/*
	 * for field config the rel entity is the field
	 * @see com.aurel.track.beans.ConfigItem#getConfigRel()
	 */
	public Integer getConfigRel() {
		return null;
	}
	
	public void setConfigRel(Integer configRel) {
	}
	
	public boolean isDefault() {
		return getProjectType()==null && getProject()==null && getIssueType()==null;
	}

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		if(getIssueType()!=null){
			attributesMap.put("issueType", getIssueType().toString());
		}
		if(getProjectType()!=null){
			attributesMap.put("projectType", getProjectType().toString());
		}
		if(getProject()!=null){
			attributesMap.put("project", getProject().toString());
		}
		if(getWorkflow()!=null){
			attributesMap.put("workflow", getWorkflow().toString());
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TWorkflowConnectBean workflowConnectBean = new TWorkflowConnectBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			workflowConnectBean.setObjectID(new Integer(strObjectID));
		}

		String workflow = attributes.get("workflow");
		if (workflow!=null) {
			workflowConnectBean.setWorkflow(new Integer(workflow));
		}
		String issueType = attributes.get("issueType");
		if (issueType!=null) {
			workflowConnectBean.setIssueType(new Integer(issueType));
		}
		String projectTypeStr = attributes.get("projectType");
		if (projectTypeStr!=null) {
			workflowConnectBean.setProjectType(new Integer(projectTypeStr));
		}
		String project = attributes.get("project");
		if (project!=null) {
			workflowConnectBean.setProject(new Integer(project));
		}

		workflowConnectBean.setUuid(attributes.get("uuid"));
		return workflowConnectBean;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		TWorkflowConnectBean workflowConnectBean = (TWorkflowConnectBean)serializableLabelBean;

		Integer externalWorkflow=workflowConnectBean.getWorkflow();
		Integer internalWorkflow = getWorkflow();

		Integer externalIssueType=workflowConnectBean.getIssueType();
		Integer internalIssueType = getIssueType();

		Integer externalProjectType=workflowConnectBean.getProjectType();
		Integer internalProjectType = getProjectType();

		Integer externalProject=workflowConnectBean.getProject();
		Integer internalProject = getProject();

		return EqualUtils.equal(externalWorkflow, internalWorkflow)&&
				EqualUtils.equal(externalIssueType,internalIssueType)&&
				EqualUtils.equal(externalProjectType,internalProjectType)&&
				EqualUtils.equal(externalProject,internalProject);
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
