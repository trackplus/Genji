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

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.PropertiesHelper;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TReleaseBean
	extends com.aurel.track.beans.base.BaseTReleaseBean
	implements Serializable, IHierarchicalBean, ISortedBean, ISerializableLabelBean
{
	
	public static class MOREPPROPS {
		public static String DEFAULT_RELEASENOTICED = "defaultReleaseNoticed";
		public static String DEFAULT_RELEASESCHEDULED = "defaultReleaseScheduled";
		public static String RESOURCE_PERSON_MAPPINGS = TProjectBean.MOREPPROPS.RESOURCE_PERSON_MAPPINGS;
	}
	
	private static final long serialVersionUID = 1L;
	public Comparable getSortOrderValue() {
		return getSortorder();
	}
	
	public void setMoreProps(String _moreProps) {
		moreProperties = PropertiesHelper.getProperties(_moreProps);
		super.setMoreProps(_moreProps);
	}
	
	protected Properties moreProperties = null;
	
	public Properties getMoreProperties() {
		return moreProperties;
	}

	
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("label", getLabel());
		attributesMap.put("uuid", getUuid());
		Integer projectID = getProjectID();
		if (projectID!=null) {
			attributesMap.put("projectID", projectID.toString());
		}
		Integer status = getStatus();
		if (status!=null) {
			attributesMap.put("status", status.toString());
		}
		Integer sortorder = getSortorder();
		if (sortorder!=null) {
			attributesMap.put("sortorder", sortorder.toString());
		}
		String moreProps = getMoreProps();
		if (moreProps!=null && !"".equals(moreProps)) {
			attributesMap.put("moreProps", getMoreProps());
		}
		Integer parent = getParent();
		if (parent!=null) {
			attributesMap.put("parent", parent.toString());
		}
		String description = getDescription();
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);
		}
		Date dueDate = getDueDate();
		if (dueDate!=null) {
			attributesMap.put("dueDate", DateTimeUtils.getInstance().formatISODate(dueDate));
		}	
		return attributesMap;
	}
	
	/**
	 * De-serialze the labelBean 
	 * @param attributes
	 * @return
	 */
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TReleaseBean releaseBean = new TReleaseBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			releaseBean.setObjectID(new Integer(strObjectID));
		}
		releaseBean.setUuid(attributes.get("uuid"));
		releaseBean.setLabel(attributes.get("label"));
		String strProjectID = attributes.get("projectID");
		if (strProjectID!=null) {
			releaseBean.setProjectID(new Integer(strProjectID));
		}
		String strStatus = attributes.get("status");
		if (strStatus!=null) {
			releaseBean.setStatus(new Integer(strStatus));
		}
		String strSortorder = attributes.get("sortorder");
		if (strSortorder!=null) {
			releaseBean.setSortorder(new Integer(strSortorder));
		}
		releaseBean.setMoreProps(attributes.get("moreProps"));
		releaseBean.setDescription(attributes.get("description"));
		String strDueDate = attributes.get("dueDate");
		if (strDueDate!=null) {
			releaseBean.setDueDate(DateTimeUtils.getInstance().parseISODate(strDueDate));
		}
		String strParent = attributes.get("parent");
		if (strParent!=null) {
			releaseBean.setParent(Integer.valueOf(strParent));
		}
		return releaseBean;
	}
	
	/**
	 * Whether two label beans are equivalent 
	 * @param serializableLabelBean
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TReleaseBean releaseBean = (TReleaseBean) serializableLabelBean;
		String externalLabel = getLabel();
		String internalLabel = releaseBean.getLabel();
		Integer externalProject = getProjectID();
		Integer internalProject = releaseBean.getProjectID();
		//a release matches only it the release's project matches and the release label matches 
		Map<Integer, Integer> projectMatches = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null)); 
		if (projectMatches!=null && projectMatches.get(externalProject)!=null && 
				externalLabel!=null && internalLabel!=null) {
			return projectMatches.get(externalProject).equals(internalProject) && 
				externalLabel.equals(internalLabel);
		}		
		return false;
	}
	
	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {
		TReleaseBean releaseBean = (TReleaseBean)serializableLabelBean;
		Integer releaseStatus = releaseBean.getStatus();
		if (releaseStatus!=null) {
			Map<Integer, Integer> systemStatusMap = 
				matchesMap.get(ExchangeFieldNames.SYSTEMSTATE);
			releaseBean.setStatus(systemStatusMap.get(releaseStatus));
		}		
		Integer projectID = releaseBean.getProjectID();
		if (projectID!=null) {
			Map<Integer, Integer> projectMap = 
				matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null));
			releaseBean.setProjectID(projectMap.get(projectID));
		}
		Integer parentID = releaseBean.getParent();
		if (parentID!=null) {
			Map<Integer, Integer> releaseMap = 
					matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_RELEASE, null));
			if (releaseMap!=null) {
				releaseBean.setParent(releaseMap.get(parentID));
			}
		}
		return ReleaseBL.save(releaseBean);
	}
}
