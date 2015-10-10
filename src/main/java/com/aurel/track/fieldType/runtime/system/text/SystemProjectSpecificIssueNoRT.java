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

import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SystemOutputBaseRT;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.StringMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.matchers.run.StringMatcherRT;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.lucene.LuceneUtil;

/**
 * Runtime class for project specific IssueNo
 * @author Tamas Ruff
 *
 */
public class SystemProjectSpecificIssueNoRT extends SystemOutputBaseRT {

	private static final Logger LOGGER = LogManager.getLogger(SystemProjectSpecificIssueNoRT.class);
	/**
	 * Defines the data type of the field attribute
	 * Used by saving field attributes for custom fields
	 * and saving explicit history for both system and custom fields
	 * Should be a @ValueType constant
	 * This field will be never historized (in this case the real type would be Integer)
	 * but used in ReportOverviewJasperDesign it is a calculated string
	 * (avoid Integer parsing error in jasper reports)
	 * @return
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
	 * Creates the matcher object for configuring the matcher
	 * @param fieldID
	 */
	public IMatcherDT getMatcherDT(Integer fieldID) {
		//return new StringMatcherDT(fieldID);
		//should not appear in the tree filter because only issueNo appears
		return null;
	}

	/**
	 * Creates the  matcher object for executing the matcher
	 * @param fieldID
	 * @param relation
	 * @param matchValue
	 */
	@Override
	public IMatcherRT getMatcherRT(Integer fieldID, int relation, Object matchValue,
			MatcherContext matcherContext){
		//return new IntegerMatcherRT(fieldID, relation, matchValue, matcherContext);
		return new StringMatcherRT(fieldID, relation, matchValue, matcherContext);
	}

	@Override
	public MatcherConverter getMatcherConverter() {
		//return IntegerMatcherConverter.getInstance();
		return StringMatcherConverter.getInstance();
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
		if (workItemID!=null) {
			TWorkItemBean workItemBean = null;
			if (localLookupContainer!=null) {
				Map<Integer, TWorkItemBean> workItemsMap = localLookupContainer.getWorkItemsMap();
				if (workItemsMap!=null) {
					workItemBean = workItemsMap.get(workItemID);
				}
			}
			if (workItemBean==null) {
				LOGGER.warn("The workItem " + workItemID + " not found in container");
				try {
					workItemBean = ItemBL.loadWorkItem(workItemID);
				} catch (ItemLoaderException e) {
				}
			}
			return getShowValue(value, workItemBean);
		}
		return "";
	}

	public static String getShowValue(Object value, TWorkItemBean workItemBean) {
		String projectPrefix = null;
		if (workItemBean!=null) {
			Integer projectID = workItemBean.getProjectID();
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean==null) {
				LOGGER.info("The project " + projectID + " was not found in container");
			} else {
				projectPrefix = projectBean.getPrefix();
			}
		}
		Integer projectSpecificIssueNo = (Integer)value;
		return ItemBL.getSimpleOrPorjectSpecificItemNo(true, projectPrefix, projectSpecificIssueNo);
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
	 * Gets the string value to be stored by lucene
	 * @param value the value of the field
	 * @param workItemBean the lucene value might depend on other fields of the workItem
	 * @return
	 */
	public String getLuceneValue(Object value, TWorkItemBean workItemBean) {
		String showValue = getShowValue(value, workItemBean);
		if (showValue==null) {
			return "";
		} else {
			//the standard analyzer lowercases the
			return showValue.toLowerCase();
		}
	}

	/**
	 * Whether the field should be stored
	 * @return
	 */
	public int getLuceneStored() {
		return LuceneUtil.STORE.YES;
	}

	/**
	 * Whether the field should be tokenized
	 * @return
	 */
	public int getLuceneTokenized() {
		return LuceneUtil.TOKENIZE.NO;
	}

	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.DIRECTTEXT;
	}
}
