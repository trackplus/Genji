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


package com.aurel.track.admin.customize.lists;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.util.SortOrderUtil;

/**
 * Base business logic interface for list entries 
 * @author Tamas Ruff
 *
 */
public abstract class OptionBaseBL implements ListOrOptionBaseBL {
	
			
	/**
	 * Whether the typeflag is defined for this option 
	 * @return
	 */
	public abstract boolean  hasTypeFlag();
	
	/**
	 * Whether editing the type flag is disabled 
	 * @return
	 */
	public abstract boolean isDisableTypeflag();
	
	/**
	 * Gets the typeflag if hasTypeflag
	 * @param labelBean
	 * @return
	 */
	public abstract Integer getTypeflag(ILabelBean labelBean);
	
	/**
	 * Gets the typeflag label if hasTypeFlag
	 * @param listID
	 * @param labelBean
	 * @param locale
	 * @return
	 */
	public abstract String getTypeflagLabel(Integer listID, ILabelBean labelBean, Locale locale);
	
	/**
	 * Whether the symbol is defined for this option 
	 * @param leaf
	 * @return
	 */
	public abstract boolean hasSymbol(boolean leaf);
	
	/**
	 * Gets the symbol
	 * @param labelBean
	 * @return
	 */
	public abstract String getSymbol(ILabelBean labelBean);
		
	/**
	 * Sets the symbol
	 * @param labelBean
	 * @param symbol
	 * @return
	 */
	public abstract void setSymbol(ILabelBean labelBean, String symbol);
	
	/**
	 * Gets the iconKey
	 * @param labelBean
	 * @return
	 */
	public abstract Integer getIconKey(ILabelBean labelBean);
		
	/**
	 * Sets the iconKey
	 * @param labelBean
	 * @param iconKey
	 * @return
	 */
	public abstract void setIconKey(ILabelBean labelBean, Integer iconKey);
	
	/**
	 * Sets the iconChanged flag
	 * @param labelBean
	 * @param iconChanged
	 * @return
	 */
	public abstract void setIconChanged(ILabelBean labelBean, boolean iconChanged);
	
	/**
	 * Whether the background color is defined for this option
	 * @param leaf
	 * @return
	 */
	public abstract boolean hasCssStyle(boolean leaf);
	
	/**
	 * Gets the background color
	 * @param labelBean
	 * @return
	 */
	public abstract String getCSSStyle(ILabelBean labelBean);
	
	/**
	 * Sets the background color
	 * @param labelBean
	 * @param CSSStyle
	 * @return
	 */
	public abstract void setCSSStyle(ILabelBean labelBean, String CSSStyle);
	
	/**
	 * Whether the percent complete flag is defined for this option
	 * @return
	 */
	public abstract boolean hasPercentComplete();
	
	/**
	 * Gets the percent complete value
	 * @param labelBean
	 * @return
	 */
	public abstract Integer getPercentComplete(ILabelBean labelBean); 
	
	/**
	 * Gets the percent complete value
	 * @param labelBean
	 * @param percentComplete
	 * @return
	 */
	public abstract void setPercentComplete(ILabelBean labelBean, Integer percentComplete); 
	
	/**
	 * Whether the default option is defined for this option
	 * @return
	 */
	public abstract boolean hasDefaultOption(); 
	
	/**
	 * Whether this option is set as default
	 * @param labelBean
	 * @return
	 */
	public abstract boolean isDefaultOption(ILabelBean labelBean);
	
	/**
	 * Gets the percent complete value
	 * @param labelBean
	 * @param defaultOption
	 * @return
	 */
	public abstract void setDefaultOption(ILabelBean labelBean, boolean defaultOption); 
	
	/******************* sort order specific methods **********************/
	/**
	 * Gets the specific extra constraints
	 * @param labelBean
	 * @return
	 */
	public abstract String getSpecificCriteria(ILabelBean labelBean);
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	public abstract Integer getSortOrder(ILabelBean labelBean);
	
	/**
	 * Gets the sort order of the bean
	 * @param labelBean
	 * @return
	 */
	public abstract void setSortOrder(ILabelBean labelBean, Integer sortOrder);
	
	/**
	 * Gets the sort order column name 
	 * @return
	 */
	public abstract String getSortOrderColumn();
	
	/**
	 * Gets the table the sorting is made in 
	 * @return
	 */
	public abstract String getTableName();
	
	/**
	 * Saves the label bean into the database  
	 * @param labelBean
	 */
	public abstract Integer saveSimple(ILabelBean labelBean);
	
	/**
	 * Normalize the sort order for the list: removes the holes between the sortOrder fields
	 * @param sortedBeans
	 * @return 
	 */
	public List<ILabelBean> normalizeSortOrder(List<ILabelBean> sortedBeans) {
		if (sortedBeans!=null) {
			Iterator<ILabelBean> itrSortedObjects = sortedBeans.iterator();
			int i = 0;
			while(itrSortedObjects.hasNext()) {
				i++;
				ILabelBean sortedObject = itrSortedObjects.next();
				if (getSortOrder(sortedObject)==null || getSortOrder(sortedObject).intValue()!=i) {
					//set and save only if differs
					setSortOrder(sortedObject, Integer.valueOf(i));
					saveSimple(sortedObject);
				}
			}
		}
		return sortedBeans;
	}
		
	/**
	 * Sets the wbs on the affected workItems after a drag and drop operation
	 * @param draggedID
	 * @param droppedToID
	 * @param before
	 */
	public synchronized void dropNear(Integer draggedID, Integer droppedToID, 
			boolean before, Integer listID) {
		ILabelBean draggedBean = getLabelBean(draggedID, listID);
		ILabelBean droppedToBean = getLabelBean(droppedToID, listID);
		Integer draggedSortOrder = getSortOrder(draggedBean);
		Integer droppedToSortOrder = getSortOrder(droppedToBean);
		String sortOrderColumn = getSortOrderColumn();
		String tabelName = getTableName();
		Integer newSortOrder = SortOrderUtil.dropNear(draggedSortOrder, droppedToSortOrder,
				sortOrderColumn, tabelName, getSpecificCriteria(draggedBean), before);
		setSortOrder(draggedBean, newSortOrder);
		saveSimple(draggedBean);
	}
}
