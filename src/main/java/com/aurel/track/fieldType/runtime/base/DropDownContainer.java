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


package com.aurel.track.fieldType.runtime.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;

/**
 * Used by rendering the lists (IFieldTypeRT.processLoadDataSource()), 
 * and as lookup maps for select keys IFieldTypeRT.getShowValue(), 
 * IFieldTypeRT.getShowISOValue(), IFieldTypeRT.getSortOrderValue()
 * @author Tamas
 *
 */
public class DropDownContainer {
	
	/**
	 * Map containing the lists data sources
	 * Used by rendering the lists
	 * 	-	key: 	fieldID_parameterCode
	 * 	-	value: 	list of ILabelBean
	 */	
	protected Map<String, List<ILabelBean>> dataSourceListsMap = new HashMap<String, List<ILabelBean>>();
	
	
	/**
	 * Map containing the datasources as maps
	 * Used by retrieving beans by key 
	 * for ex. getting the show value or for sortOrder value 
	 * 	-	key: fieldID_parameterCode
	 *	-	value: map: key: ID
	 * 				value: the ILabelBean
	 */
	protected Map<String, Map<Integer, ILabelBean>> dataSourceMapsMap = new HashMap<String, Map<Integer,ILabelBean>>();
	
	/**
	 * Map containing the tree data sources
	 * Used by rendering the trees
	 * 	-	key: 	fieldID_parameterCode
	 * 	-	value: 	list of TreeNode
	 */
	protected Map<String, List<TreeNode>> dataSourceTreesMap = new HashMap<String, List<TreeNode>>(); 
	
	/**
	 * Gets the datasource as list for a select field
	 * @param key
	 * @return
	 */
	public List<ILabelBean> getDataSourceList(String key) {
		return dataSourceListsMap.get(key);
	}
	
	/**
	 * Sets the datasource with a list for a select field
	 * @param key
	 * @param dataSourceList
	 * @return
	 */
	public void setDataSourceList(String key, List<ILabelBean> dataSourceList) {
		dataSourceListsMap.put(key, dataSourceList);
		//initialize also the map: this map is used by getShowValue(), getSortOrderValue()
		if (dataSourceList!=null) {
			dataSourceMapsMap.put(key, GeneralUtils.createMapFromList(dataSourceList));
		}
	}

	/**
	 * Gets the datasource as map for a select field
	 * @param key
	 * @return
	 */
	public Map<Integer, ILabelBean> getDataSourceMap(String key) {
		return dataSourceMapsMap.get(key);
	}
	
	/**
	 * Sets the datasource with a map for a select field
	 * @param key
	 * @param dataSourceMap
	 * @return
	 */
	public void setDataSourceMap(String key, Map<Integer, ILabelBean> dataSourceMap) {
		dataSourceMapsMap.put(key, dataSourceMap);
	}

	/**
	 * Gets the datasource as list for a select field
	 * @param key
	 * @return
	 */
	public List<TreeNode> getDataSourceTree(String key) {
		return dataSourceTreesMap.get(key);
	}
	
	/**
	 * Sets the datasource with a list for a select field
	 * @param key
	 * @param dataSourceList
	 * @return
	 */
	public void setDataSourceTree(String key, List<TreeNode> treeList) {
		dataSourceTreesMap.put(key, treeList);		
	}
	
	/**
	 * Get the ILabelBean for an objectID
	 * @param key
	 * @param objectID
	 * @return
	 */
	
}
