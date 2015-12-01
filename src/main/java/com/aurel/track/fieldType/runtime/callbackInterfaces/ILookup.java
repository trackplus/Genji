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

package com.aurel.track.fieldType.runtime.callbackInterfaces;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.fieldType.runtime.base.SerializableBeanAllowedContext;

/**
 * Utility interface for data exchange
 * @author Tamas
 *
 */
public interface ILookup {

	/**
	 * Whether the datasource is tree or list
	 * @return
	 */
	boolean isTree();
	
	/**
	 * Whether the list entries have dynamic icons
	 * @return
	 */
	boolean hasDynamicIcons();
	
	/**
	 * Gets the iconCls class for label bean if dynamicIcons is false
	 * @param labelBean
	 */
	String getIconCls(ILabelBean labelBean);
	
	/**
	 * Gets the localized bean by objectID
	 * @param objectID
	 * @param locale
	 * @return
	 */
	ILabelBean getLabelBean(Integer objectID, Locale locale);
	
	/**
	 * The field key for getting the corresponding map from dropdown container.
	 * The SystemSelects and the Pickers are stored under 
	 * the same key in the container (the key of the SystemSelect field)
	 * (for ex. manager, responsible, originator, changedBy and 
	 * user picker share the same person map)
	 * The CustomSelects are stored under their own fieldIDs 
	 * @return
	 */
	public Integer getDropDownMapFieldKey(Integer fieldID);
	
	/**
	 * Serialize a label bean to a string attributes map 
	 * @param serializableLabelBean
	 * @return
	 */
	Map<String, String> serializeBean(ISerializableLabelBean serializableLabelBean);
	
	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	ISerializableLabelBean getNewSerializableLabelBean();
	
	/**
	 * Deserialize the labelBean 
	 * @param attributes
	 * @return
	 */
	ISerializableLabelBean deserializeBean(Map<String, String> attributes);
		
	/**
	 * Gets the ID by the label
	 * @param fieldID
	 * @param projectID
	 * @param issueTypeID
	 * @param locale
	 * @param label
	 * @param lookupBeansMap
	 * @param componentPartsMap
	 * @return
	 */
	Integer getLookupIDByLabel(Integer fieldID,
			Integer projectID, Integer issueTypeID,
			Locale locale, String label, 
			Map<String, ILabelBean> lookupBeansMap, Map<Integer, Integer> componentPartsMap);
	
	/**
	 * Whether the lookupID found by label is allowed in 
	 * the context of serializableBeanAllowedContext
	 * In excel the lookup entries are not limited by the user interface controls
	 * This method should return false if the lookupID
	 * is not allowed (for ex. a person without manager role was set as manager) 
	 * @param objectID
	 * @param serializableBeanAllowedContext
	 * @return
	 */
	boolean lookupBeanAllowed(Integer objectID,
			SerializableBeanAllowedContext serializableBeanAllowedContext);
	
	/**
	 * Gets the datasource for all possible list entries 
	 * @param fieldID
	 * @return
	 */
	List<ILabelBean> getDataSource(Integer fieldID);
}
