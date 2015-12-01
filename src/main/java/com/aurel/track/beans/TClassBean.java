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
import java.util.Map;

import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TClassBean
    extends com.aurel.track.beans.base.BaseTClassBean
    implements Serializable, IBeanID, ISortedBean, ISerializableLabelBean, IDefaultLabelBean
{
	
	private static final long serialVersionUID = 1L;	
	
	private Boolean defaultValue;
		
	@Override
	public Boolean getDefaultValue() {		
		return defaultValue;
	}

	@Override
	public void setDefaultValue(Boolean defaultValue) {
		this.defaultValue = defaultValue;		
	}

	@Override
	public Comparable getSortOrderValue() {		
		return getLabel();
	}

	@Override
	public Map<String, String> serializeBean() {		
		Map<String, String> attributesMap = new HashMap<String, String>();		
		attributesMap.put("objectID", getObjectID().toString());		
		attributesMap.put("label", getLabel());
		attributesMap.put("uuid", getUuid());
		Integer projectID = getProjectID();
		if (projectID!=null) {
			attributesMap.put("projectID", projectID.toString());
		}
		return attributesMap;
	}

	/**
	 * De-serialze the labelBean 
	 * @param attributes
	 * @return
	 */
	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TClassBean classBean = new TClassBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			classBean.setObjectID(new Integer(strObjectID));
		}
		classBean.setUuid(attributes.get("uuid"));
		classBean.setLabel(attributes.get("label"));		
		String strProjectID = attributes.get("projectID");
		if (strProjectID!=null) {
			classBean.setProjectID(new Integer(strProjectID));
		}		
		return classBean;
	}
	
	/**
	 * Whether two label beans are equivalent 	
	 * @param serializableLabelBean
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}					
		TClassBean classBean = (TClassBean) serializableLabelBean;
		String externalLabel = getLabel();
		String internalLabel = classBean.getLabel();	
		Integer externalProject = getProjectID();
		Integer internalProject = classBean.getProjectID();
		//a class matches only if the class's project matches and the class label matches 
		Map<Integer, Integer> projectMatches = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null)); 
		if (projectMatches!=null && projectMatches.get(externalProject)!=null && externalLabel!=null && internalLabel!=null) {
			return projectMatches.get(externalProject).equals(internalProject)  && externalLabel.equals(internalLabel);			
		}		
		return false;
	}
	
	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {		
		TClassBean classBean = (TClassBean)serializableLabelBean;
		Integer projectID = classBean.getProjectID();
		if (projectID!=null) {
			Map<Integer, Integer> projectMap = 
				matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null));
			classBean.setProjectID(projectMap.get(projectID));
		}
		return null;//classDAO.save(classBean);
	}
}
