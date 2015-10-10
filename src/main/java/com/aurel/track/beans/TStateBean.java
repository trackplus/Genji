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

import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.util.EqualUtils;

/**
 * A TStateBean contains a possible issue status. The statuses that are available can be configured
 * by the user. Each status has
 * <ul>
 *  <li>a label; this can be a resource key that is then localized. If no resource can be found,
 *  the label is taken directly.</li>
 *  <li>a stateflag, indicating the class of this status (see {@link com.aurel.track.beans.TStateBean.STATEFLAGS}).</li>
 *  <li>a sortorder when appearing in selection boxes</li>
 *  <li>a symbol, a string for a GIF or png icon</li>
 *  <li>an icon, which is a key into the <code>TBLOB</code> table</li>
 *  <li>a number for percent complete. A status can be associated with a completion percentage.</li>
 * </ul>
 * @see STATEFLAGS for values and meaning of the stateflag field
 */
public class TStateBean
    extends com.aurel.track.beans.base.BaseTStateBean
    implements Serializable, IBeanID, ILocalizedLabelBean, 
    	ISortedBean, ISerializableLabelBean
{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Each status has a stateflag field, which indicates the meaning of this status to this system.
	 * Status can be defined arbitrarily by the user, but the system only knows via the statusflag
	 * field how to interpret the status.
	 *
	 */
	public static class STATEFLAGS
	{
		/**
		 * This means that the status designates an issue as not closed. For example this issue has been
		 * assigned, or opened, or implemented, but it has not been closed thus far.
		 */
		public static final int ACTIVE = 0;
		
		/**
		 * This is an intermediate status, which is not really used in the system.
		 */
		public static final int INACTIVE = 2;
		
		/**
		 * This designates a status as "closed". For example, the status "suspended" could be treated
		 * as a closed status, since users usually don't want to get suspended issues in their list 
		 * of open issues.
		 */
		public static final int CLOSED = 1;
		
		/**
		 * disabled/barred/read only. If an issue is in this status then it can be modified
		 * (even if the user would have edit right on it) only by a person who according to the
		 * workflow have a right to move the issue to a non-disabled state  
		 */
		public static final int DISABLED = 3;
	}
	
	/**
	 * Gets the possible typeflags
	 * @param entityType
	 * @return
	 */
	public int[] getPossibleTypeFlags(Integer entityType) {
		return new int[] {STATEFLAGS.ACTIVE,
			STATEFLAGS.INACTIVE, 
			STATEFLAGS.CLOSED,
			STATEFLAGS.DISABLED};
	 }
		
	
	public String getKeyPrefix() {
		return LocalizationKeyPrefixes.FIELD_SYSTEMSELECT_KEY_PREFIX + 
			SystemFields.STATE + ".";
	}

	public Comparable getSortOrderValue() {
		return getSortorder();
	}
	
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("label", getLabel());
		attributesMap.put("uuid", getUuid());
		Integer stateFlag = getStateflag();
		if (stateFlag!=null) {
			attributesMap.put("stateFlag", stateFlag.toString());
		}
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

		return attributesMap;
	}
	
	/**
	 * De-serialze the labelBean 
	 * @param attributes
	 * @return
	 */
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TStateBean stateBean = new TStateBean();	
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			stateBean.setObjectID(new Integer(strObjectID));
		}
		stateBean.setUuid(attributes.get("uuid"));
		stateBean.setLabel(attributes.get("label"));
		String strSortOrder = attributes.get("sortorder");
		if (strSortOrder!=null) {
			stateBean.setSortorder(new Integer(strSortOrder));
		}
		String strStateFlag = attributes.get("stateFlag");
		if (strStateFlag!=null) {
			stateBean.setStateflag(new Integer(strStateFlag));
		}

		stateBean.setSymbol(attributes.get("symbol"));
		stateBean.setCSSStyle(attributes.get("CSSSTyle"));
		String strIconKey = attributes.get("iconKey");
		if (strIconKey!=null) {
			stateBean.setIconKey(new Integer(strIconKey));
			stateBean.setIconChanged(BooleanFields.TRUE_VALUE);
		}
		return stateBean;
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
		TStateBean stateBean = (TStateBean)serializableLabelBean;
		String externalUuid = getUuid();
		String internalUuid = stateBean.getUuid();

		if(EqualUtils.equalStrict(externalUuid, internalUuid)){
			return true;
		}

		String externalLabel = getLabel();
		String internalLabel = stateBean.getLabel();
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
		return StatusBL.save((TStateBean)serializableLabelBean, null);
	}
}
