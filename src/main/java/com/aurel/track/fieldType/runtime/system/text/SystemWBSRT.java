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


package com.aurel.track.fieldType.runtime.system.text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.SystemOutputBaseRT;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.util.GeneralUtils;

/**
 * Runtime class for IssueNo
 * @author Tamas Ruff
 *
 */
public class SystemWBSRT extends SystemOutputBaseRT {

	private static final Logger LOGGER = LogManager.getLogger(SystemWBSRT.class);
	private static String WBS_SEPARATOR = ".";
	/** 
	 * Defines the data type of the field attribute
	 * Used by saving field attributes for custom fields
	 * and saving explicit history for both system and custom fields
	 * Should be a @ValueType constant
	 * This field will be never historized (in this case the real type would be Integer)
	 * but used in ReportOverviewJasperDesign it is a calculated string
	 * (avoid Integer parsing error in jasper reports) 
	 * * @return
	 */
	public int getValueType() {
		return ValueType.TEXT;
	}
	
	/**
	 * Whether the value of this field can be changed
	 * @return
	 */
	public boolean isReadOnly() {
		return true;
	}
	
	/**
	 * Get the show value called when an item result set is implied
	 * @param fieldID
	 * @param parameterCode
	 * @param value
	 * @param workItemID
	 * @param localLookupContainer
	 * @param locale 
	 * @return
	 */
	public String getShowValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		String wbs = null;
		TWorkItemBean workItemBean = null;
		if (localLookupContainer!=null) {
			Map<Integer, TWorkItemBean> workItemsMap = localLookupContainer.getWorkItemsMap();
			Map<Integer, Integer> workItemToParent = localLookupContainer.getWorkItemToParentMap();
			//avoid infinite hierarchy cycles
			Set<Integer> workItemIDs = new HashSet<Integer>();
			if (workItemToParent!=null && workItemsMap!=null) {
				workItemBean = workItemsMap.get(workItemID);
				String levelString = "";
				if (workItemBean!=null && workItemBean.getWBSOnLevel()!=null) {
					levelString = workItemBean.getWBSOnLevel().toString();
				}
				wbs = levelString;
				Integer previousParentID = workItemID;
				while (workItemToParent.containsKey(previousParentID)) {
					workItemIDs.add(previousParentID);
					Integer nextParentID = workItemToParent.get(previousParentID);
					if (workItemIDs.contains(nextParentID)) {
						LOGGER.warn("Recursive hierarchy found for workItemID " + workItemID + " and ancestor " + previousParentID);
						return wbs;
					} else {
						previousParentID = nextParentID;
					}
					workItemBean = (TWorkItemBean)workItemsMap.get(previousParentID);
					levelString = "";
					if (workItemBean!=null && workItemBean.getWBSOnLevel()!=null) {
						levelString = workItemBean.getWBSOnLevel().toString();
						wbs = levelString + WBS_SEPARATOR + wbs;
					}
				}
				return wbs;
			}
		}
		try {
			workItemBean = ItemBL.loadWorkItem(workItemID);
		} catch (ItemLoaderException e) {
		}
		if (workItemBean!=null) {
			String levelString = "";
			if (workItemBean.getWBSOnLevel()!=null) {
				levelString = workItemBean.getWBSOnLevel().toString();
			}
			wbs = levelString;
			while (workItemBean.getSuperiorworkitem()!=null) {
				try {
					workItemBean = ItemBL.loadWorkItem(workItemBean.getSuperiorworkitem());
				} catch (ItemLoaderException e) {
				}
				levelString = "";
				if (workItemBean!=null && workItemBean.getWBSOnLevel()!=null) {
					levelString = workItemBean.getWBSOnLevel().toString();
					wbs = levelString + WBS_SEPARATOR + wbs;
				}
			}
		}
		return wbs;
	}
	
	/**
	 * Parses the string value into the corresponding object value
	 * @param isoStrValue
	 * @return
	 */
	@Override
	public Object parseISOValue(Object isoStrValue) {
		if (isoStrValue!=null) {
			return Integer.valueOf(isoStrValue.toString());
		}
		return null;
	}
	
	/**
	 * The base class implementation (return value that means wbsOnLevel) is not enough because:
	 * although the hierarchy part is enforced by building in ReportBeans, so the
	 * sort order would be  calculated only inside the same level.
	 * But if the parent/ascendent is not included in the filter result
	 * then the wbsOnLevel integer is clearly not enough.
	 * Get the sort order related to the value
	 * By default the value is directly used as sortOrder
	 * the select fields has extra sortOrder columns 
	 * @param fieldID
	 * @param parameterCode
	 * @param value the value the sortorder is looked for
	 * @param workItemID
	 * @param localLookupContainer 
	 * @return
	 */
	public Comparable getSortOrderValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer) {
		//return new WBSComparable(getShowValue(fieldID, parameterCode, value, workItemID, localLookupContainer, null), WBS_SEPARATOR);
		String showValue = getShowValue(fieldID, parameterCode, value, workItemID, localLookupContainer, null);
		List<Integer> wbsOnLevelsList = GeneralUtils.createIntegerListFromStringArr(showValue.split("\\."));
		NumberFormat nf = new DecimalFormat("000000");
		StringBuilder stringBuilder = new StringBuilder();
		for (Integer wbsOnLevel : wbsOnLevelsList) {
			stringBuilder.append(nf.format(wbsOnLevel));
		}
		return stringBuilder.toString();
		//return (Integer)value;
	}
	
	/**
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable   
	 * @return
	 */
	@Override
	public boolean isGroupable() {
		return false;
	}
	
	/**
	 * Whether the field might be matched in for an excel column
	 * @return
	 */
	public boolean mightMatchExcelColumn() {
		return false;
	}
	
	/**
	 * Whether the field should be stored
	 * @return
	 */ 
	public int getLuceneStored() {
		return LuceneUtil.STORE.NO;
	}
	
	/**
	 * Whether the field should be tokenized
	 * @return
	 */
	public int getLuceneTokenized() {
		return LuceneUtil.TOKENIZE.NO;
	}
	
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.NOSTORE;
	}
}
