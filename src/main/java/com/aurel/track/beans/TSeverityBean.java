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

import com.aurel.track.admin.customize.lists.systemOption.SeverityBL;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.resources.LocalizationKeyPrefixes;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TSeverityBean
    extends com.aurel.track.beans.base.BaseTSeverityBean
    implements Serializable, IBeanID, ILocalizedLabelBean, 
    ISortedBean, ISerializableLabelBean
{
	
	private static final long serialVersionUID = 1L;
		
	
	public static class TYPEFLAGS {
		public static final int WARNING_LEVEL1 = 0;
		public static final int WARNING_LEVEL2 = 1;
		public static final int WARNING_LEVEL3 = 2;
	}		
		
	/**
	 * Gets the possible typeflags
	 * @param entityType
	 * @return
	 */
	public int[] getPossibleTypeFlags(Integer entityType) {
	    return new int[] {TYPEFLAGS.WARNING_LEVEL1, 
	    		TYPEFLAGS.WARNING_LEVEL2, 
	    		TYPEFLAGS.WARNING_LEVEL3};
	 }
	
	 
	 public String getKeyPrefix() {
		 return LocalizationKeyPrefixes.FIELD_SYSTEMSELECT_KEY_PREFIX + 
			SystemFields.SEVERITY + ".";
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
		TSeverityBean severityBean = new TSeverityBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			severityBean.setObjectID(new Integer(strObjectID));
		}
		severityBean.setUuid(attributes.get("uuid"));
		severityBean.setLabel(attributes.get("label"));
		String strSortOrder = attributes.get("sortorder");
		if (strSortOrder!=null) {
			severityBean.setSortorder(new Integer(strSortOrder));
		}
		String strWlevel = attributes.get("wlevel");
		if (strWlevel!=null) {
			severityBean.setWlevel(new Integer(strWlevel));
		}
		severityBean.setSymbol(attributes.get("symbol"));
		severityBean.setCSSStyle(attributes.get("CSSSTyle"));
		String strIconKey = attributes.get("iconKey");
		if (strIconKey!=null) {
			severityBean.setIconKey(new Integer(strIconKey));
			severityBean.setIconChanged(BooleanFields.TRUE_VALUE);
		}

		return severityBean;
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
		TSeverityBean severityBean = (TSeverityBean)serializableLabelBean;
		String externalLabel = getLabel();
		String internalLabel = severityBean.getLabel();
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
		return SeverityBL.save((TSeverityBean)serializableLabelBean, null);
	}
}
