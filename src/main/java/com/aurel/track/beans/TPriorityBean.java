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
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.admin.customize.lists.systemOption.PriorityBL;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.resources.LocalizationKeyPrefixes;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TPriorityBean
	extends com.aurel.track.beans.base.BaseTPriorityBean
	implements Serializable, IBeanID, ILocalizedLabelBean, 
		ISortedBean, ISerializableLabelBean
{
	private static final long serialVersionUID = 1L;
	
	
	public static class TYPEFLAGS {
		public static final int LEVEL1 = 0;
		public static final int LEVEL2 = 1;
		public static final int LEVEL3 = 2;
		public static final int LEVEL4 = 3;
	}
	
	/**
	 * Gets the possible typeflags
	 * @param entityType
	 * @return
	 */
	public int[] getPossibleTypeFlags(Integer entityType) {
		return new int[] {TYPEFLAGS.LEVEL1, 
				TYPEFLAGS.LEVEL2, 
				TYPEFLAGS.LEVEL3, 
				TYPEFLAGS.LEVEL4};
	 }
	
	public String getKeyPrefix() {
		return LocalizationKeyPrefixes.FIELD_SYSTEMSELECT_KEY_PREFIX + 
			SystemFields.PRIORITY + ".";
	}

	public Comparable getSortOrderValue() {
		return getSortorder();
	}

	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("label", getLabel());
		attributesMap.put("uuid", getUuid());
		Integer sortorder = getSortorder();
		if (sortorder!=null) {
			attributesMap.put("sortorder", sortorder.toString());
		}
		Integer wlevel = getWlevel();
		if (wlevel!=null) {
			attributesMap.put("wlevel", wlevel.toString());
		}
		attributesMap.put("symbol", getSymbol());
		attributesMap.put("CSSSTyle", getCSSStyle());
		Integer iconKey = getIconKey();
		if (iconKey!=null) {
			attributesMap.put("iconKey", iconKey.toString());
		}
		return attributesMap;
	}
	
	/**
	 * De-serialze the labelBean 
	 * @param attributes
	 * @return
	 */
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TPriorityBean priorityBean = new TPriorityBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			priorityBean.setObjectID(new Integer(strObjectID));
		}
		priorityBean.setUuid(attributes.get("uuid"));
		priorityBean.setLabel(attributes.get("label")+"_blah");
		String strSortOrder = attributes.get("sortorder");
		if (strSortOrder!=null) {
			priorityBean.setSortorder(new Integer(strSortOrder));
		}
		String strWlevel = attributes.get("wlevel");
		if (strWlevel!=null) {
			priorityBean.setWlevel(new Integer(strWlevel));
		}
		priorityBean.setSymbol(attributes.get("symbol"));
		priorityBean.setCSSStyle(attributes.get("CSSSTyle"));
		String strIconKey = attributes.get("iconKey");
		if (strIconKey!=null) {
			priorityBean.setIconKey(new Integer(strIconKey));
			priorityBean.setIconChanged(BooleanFields.TRUE_VALUE);
		}

		return priorityBean;
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
		TPriorityBean priorityBean = (TPriorityBean)serializableLabelBean;
		String externalLabel = getLabel();
		String internalLabel = priorityBean.getLabel();
		if (externalLabel!=null && internalLabel!=null) {
			if (externalLabel.equals(internalLabel)) {
				return true;
			}
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
		return PriorityBL.save((TPriorityBean)serializableLabelBean, null);
	}
}
