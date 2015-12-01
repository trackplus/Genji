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


package com.aurel.track.linkType;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.util.ParameterMetadata;

/**
 * Interface for link types
 * @author Tamas
 *
 */
public interface ILinkType {
	
	//the parent child relationship used in the filter
	public static final int PARENT_CHILD = 0;
	
	public static interface PARENT_CHILD_EXPRESSION {
		public static final int ALL_CHILDREN = 1;
		public static final int ALL_NOT_CLOSED_CHILDREN = 2;
		public static final int ALL_PARENTS = 3;
		
		public static final int FIRST_LEVEL = 3;
		public static final int FIRST_LEVEL_NOT_CLOSED = 4;
		public static final int LEVEL_X = 5;
		public static final int LEVEL_X_NOT_CLOSED = 6;
	}

	
	/**
	 * Defines the possible link directions
	 * 1. the linkDirection of a workItemLinkBean can be either 
	 * 	LEFT_TO_RIGHT or RIGHT_TO_LEFT
	 * 2. the linkDirection of a linkTypeBean can be either 
	 *	UNIDIRECTIONAL_OUTWARD or UNIDIRECTIONAL_INWARD or BIDIRECTIONAL
	 * 3. the linkType plugin's getPossibleDirection() can be 
	 *  UNIDIRECTIONAL_OUTWARD or UNIDIRECTIONAL_INWARD or BIDIRECTIONAL or ANY
	 *  ANY means that by instantiating a new link type of a specific linkType plugin 
	 *  the administrator can choose either UNIDIRECTIONAL_OUTWARD or UNIDIRECTIONAL_INWARD or BIDIRECTIONAL
	 *  as linkDirection
	 * @author Tamas
	 *
	 */
	public static interface LINK_DIRECTION {
		//for bidirectional relationships see the successors from predecessor with the "normal" name of the bidirectional relationship
		public static final int LEFT_TO_RIGHT = 1;
		//only the predecessor sees the successors but not vice versa: used for link types
		public static final int UNIDIRECTIONAL_OUTWARD = 1;
		//for bidirectional relationships see the predecessor from successor with the reverse name of the bidirectional relationship
		public static final int RIGHT_TO_LEFT = 2;
		//only the successor sees the predecessors but not vice versa
		public static final int UNIDIRECTIONAL_INWARD = 2;
		//predecessor sees the successors and vice versa
		public static final int BIDIRECTIONAL = 3;
		public static final int ANY=4; //any of the previous values
	}
	
	public static interface FILTER_EXPRESSION_HIERARCHICAL {
		public static final int LEFT_TO_RIGHT_FIRST_LEVEL = 1;
		public static final int RIGHT_TO_LEFT_FIRST_LEVEL = 2;
		public static final int LEFT_TO_RIGHT_LEVEL_X = 3;
		public static final int RIGHT_TO_LEFT_LEVEL_X = 4;
		public static final int LEFT_TO_RIGHT_ALL = 5;
		public static final int RIGHT_TO_LEFT_ALL = 6;
	}

	public static interface FILTER_EXPRESSION_PLAIN {
		public static final int LEFT_TO_RIGHT = FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_FIRST_LEVEL;
		public static final int RIGHT_TO_LEFT = FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_FIRST_LEVEL;
	}
	
	public String getPluginID();
	
	/**
	 * Called before the link is created
	 * @return
	 */
	boolean beforeCreateLink();
	
	/**
	 * Called after the link is created
	 */
	void afterCreateLink();
	
	/**
	 * Get the possible direction for the links
	 * @return
	 */
	int getPossibleDirection();
		
	/**
	 * Whether this link type appears in gantt view
	 * @return
	 */
	boolean isGanttSpecific();
	
	/**
	 * Whether this link type is inline 
	 * (never created explicitly, only implicitly in the background by including an item inline in a document)
	 * @return
	 */
	boolean isInline();
	
	/**
	 * Gets the item link specific data
	 * @param workItemLinkBean
	 * @return
	 */
	ItemLinkSpecificData getItemLinkSpecificData(TWorkItemLinkBean workItemLinkBean);
	
	/**
	 * Gets the itemLinkSpecificData as a string map for serializing 
	 * @param itemLinkSpecificData
	 * @param locale
	 * @return
	 */
	Map<String, String> serializeItemLinkSpecificData(ItemLinkSpecificData itemLinkSpecificData, Locale locale);

	/**
	 * Validation called before saving the issue
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param person
	 * @param predToSuccLinksOfType
	 * @param succToPredLinksOfType
	 * @param confirm whether the user confirmed
	 * @param locale
	 * @return
	 */
	List<ErrorData> validateBeforeIssueSave(TWorkItemBean workItemBean, 
			TWorkItemBean workItemBeanOriginal, Integer person, 
			List<TWorkItemLinkBean> predToSuccLinksOfType,
			List<TWorkItemLinkBean> succToPredLinksOfType, boolean confirm, Locale locale);
	
	/**
	 *  Called after the issue is saved
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param personID
	 * @param predToSuccLinksOfType
	 * @param succToPredLinksOfType
	 * @param locale
	 */
	void afterIssueSave(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, Integer personID, List<TWorkItemLinkBean> predToSuccLinksOfType,
			List<TWorkItemLinkBean> succToPredLinksOfType, Locale locale);

	/**
	 * Whether this link type appears in the tree query as 
	 * selectable for query result superset
	 * @return
	 */
	boolean selectableForQueryResultSuperset();
	
	/**
	 * Whether the linkType is hierarchical (more than one level)
	 * Makes sense only if selectableAsQueryResultSuperset returns true
	 * @return
	 */
	boolean isHierarchical();
	
	/**
	 * Prepares the parameters in the Link tab
	 * @param workItemLinkBean
	 * @param linkDirection
	 * @param locale
	 * @return
	 */
	String prepareParametersOnLinkTab(TWorkItemLinkBean workItemLinkBean, Integer linkDirection, Locale locale);
	
	/**
	 * Prepare the parameters map 
	 * Used in webservice
	 * @param workItemLinkBean
	 * @return
	 */
	Map<String, String> prepareParametersMap(TWorkItemLinkBean workItemLinkBean);
	
	/**
	 * Gets the parameter metadata as a list
	 * @param locale
	 * @return
	 */
	List<ParameterMetadata> getParameterMetadata(Locale locale);

	/**
	 * Gets the JavaScript class for configuring the link type specific part
	 * @return
	 */
	String getLinkTypeJSClass();
	
	/**
	 * Gets the link type specific configuration as JSON
	 * @param workItemLinkBean
	 * @param personBean
	 * @param locale
	 * @return
	 */
	String getSpecificJSON(TWorkItemLinkBean workItemLinkBean, TPersonBean personBean, Locale locale);
	
	/**
	 * Sets the workItemLinkBean according to the values submitted in the parameters map
	 * @param parametersMap the parameters from link configuration
	 * @param workItemLinkBean
	 * @param personBean the current user
	 * @param locale
	 * @return
	 */
	List<ErrorData> unwrapParametersBeforeSave(Map<String, String> parametersMap, TWorkItemLinkBean workItemLinkBean, TPersonBean personBean, Locale locale);
	
	/**
	 * Validates the workItemLink before save
	 * @param workItemLinkBean
	 * @param workItemLinkOriginal
	 * @param workItemsLinksMap
	 * @param predWorkItem
	 * @param succWorkItem
	 * @param personBean
	 * @param locale
	 * @return
	 */
	List<ErrorData> validateBeforeSave(TWorkItemLinkBean workItemLinkBean, TWorkItemLinkBean workItemLinkOriginal,
			SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap,
			TWorkItemBean predWorkItem, TWorkItemBean succWorkItem, TPersonBean personBean, Locale locale);
	
}
