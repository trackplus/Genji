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

import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.resources.LocalizationKeyPrefixes;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TListTypeBean
    extends com.aurel.track.beans.base.BaseTListTypeBean
    implements Serializable, IBeanID, ILocalizedLabelBean, 
    	ISortedBean, ISerializableLabelBean
{	
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getKeyPrefix() {
		return LocalizationKeyPrefixes.FIELD_SYSTEMSELECT_KEY_PREFIX + 
			SystemFields.ISSUETYPE + ".";
	}
		
	public static class TYPEFLAGS {
		public static final int GENERAL = 0;
		public static final int TASK = 1;
		public static final int SPRINT = 2;
		public static final int MEETING = 3;
		public static final int DOCUMENT = 4;
		public static final int DOCUMENT_SECTION = 5;
		public static final int DOCUMENT_FOLDER = 6;
	}
	
	/**
	 * Gets the possible typeflags
	 * @param
	 * @return
	 */
	
	@Override
	public Comparable getSortOrderValue() {
		return getSortorder();
	}
	
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("label", getLabel());
		attributesMap.put("uuid", getUuid());
		Integer sortorder = getSortorder();
		if (sortorder!=null) {
			attributesMap.put("sortorder", sortorder.toString());
		}
		attributesMap.put("symbol", getSymbol());
		attributesMap.put("CSSSTyle", getCSSStyle());
		Integer iconKey = getIconKey();
		if (iconKey!=null) {
			attributesMap.put("iconKey", iconKey.toString());
		}

		Integer typeflag = getTypeflag();
		if (typeflag!=null) {
			attributesMap.put("typeflag", typeflag.toString());
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
		TListTypeBean listTypeBean = new TListTypeBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			listTypeBean.setObjectID(new Integer(strObjectID));
		}
		listTypeBean.setUuid(attributes.get("uuid"));
		listTypeBean.setLabel(attributes.get("label"));
		String strSortOrder = attributes.get("sortorder");
		if (strSortOrder!=null) {
			listTypeBean.setSortorder(new Integer(strSortOrder));
		}
		listTypeBean.setSymbol(attributes.get("symbol"));
		listTypeBean.setCSSStyle(attributes.get("CSSSTyle"));
		String strIconKey = attributes.get("iconKey");
		if (strIconKey!=null) {
			listTypeBean.setIconKey(new Integer(strIconKey));
			listTypeBean.setIconChanged(BooleanFields.TRUE_VALUE);
		}

		String strTypeflag = attributes.get("typeflag");
		if (strTypeflag!=null) {
			listTypeBean.setTypeflag(new Integer(strTypeflag));
		}
		return listTypeBean;
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
		TListTypeBean issueTypeBean = (TListTypeBean)serializableLabelBean;
		String externalLabel = getLabel();
		String internalLabel = issueTypeBean.getLabel();
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
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {
		return IssueTypeBL.save((TListTypeBean)serializableLabelBean, null);
	}
}
