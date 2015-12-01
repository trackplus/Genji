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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.ParameterMetadata;

public class MeetingTopicLinkType extends AbstractSpecificLinkType {
	private static final Logger LOGGER = Logger.getLogger(MeetingTopicLinkType.class);
	//private static Integer DURATION_PARAM = Integer.valueOf(1);
	private static String DURATION = "Duration";
	
	private static MeetingTopicLinkType instance;

	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade
	 * @return
	 */
	public static MeetingTopicLinkType getInstance(){
		if(instance==null){
			instance=new MeetingTopicLinkType();
		}
		return instance;
	}

	@Override
	public int getPossibleDirection() {
		return LINK_DIRECTION.BIDIRECTIONAL;
	}

	/**
	 * Gets the item type specific data for a reportBeanLink
	 * @param workItemLinkBean
	 * @return
	 */
	@Override
	public ItemLinkSpecificData getItemLinkSpecificData(TWorkItemLinkBean workItemLinkBean) {
		MeetingTopicLinkSpecificData meetingTopicLinkSpecificData = new MeetingTopicLinkSpecificData();
		meetingTopicLinkSpecificData.setDuration(workItemLinkBean.getIntegerValue1());
		return meetingTopicLinkSpecificData;
	}

	/**
	 * Gets the itemLinkSpecificData as a string map for serializing 
	 * @param itemLinkSpecificData
	 * @param locale
	 * @return
	 */
	@Override
	public Map<String, String> serializeItemLinkSpecificData(ItemLinkSpecificData itemLinkSpecificData, Locale locale) {
		if (itemLinkSpecificData!=null) {
			try {
				MeetingTopicLinkSpecificData meetingTopicLinkSpecificData = (MeetingTopicLinkSpecificData)itemLinkSpecificData;
				if (meetingTopicLinkSpecificData!=null) {
					Integer duration = meetingTopicLinkSpecificData.getDuration();
					if (duration!=null) {
						Map<String, String> itemLinkSpecificDataMap = new HashMap<String, String>();
						itemLinkSpecificDataMap.put("duration", String.valueOf(duration) + " " + LocalizeUtil.getLocalizedTextFromApplicationResources("common.timeunit.minutes", locale));
						return itemLinkSpecificDataMap;
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Converting the itemLinkSpecificData to MeetingTopicLinkSpecificData failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
	}
	
	@Override
	public boolean selectableForQueryResultSuperset() {
		return true;
	}

	@Override
	public boolean isHierarchical() {
		return false;
	}

	@Override
	public String prepareParametersOnLinkTab(TWorkItemLinkBean workItemLinkBean, Integer linkDirection, Locale locale) {
		Integer duration = workItemLinkBean.getIntegerValue1();
		if (duration==null) {
			return null;
		}
		return String.valueOf(duration) + " " + LocalizeUtil.getLocalizedTextFromApplicationResources("common.timeunit.minutes", locale);
	}

	/**
	 * Prepare the parameters map 
	 * Used in webservice
	 * @param workItemLinkBean
	 * @return
	 */
	@Override
	public Map<String, String> prepareParametersMap(TWorkItemLinkBean workItemLinkBean) {
		Map<String, String> parametersMap = new HashMap<String, String>();
		Integer duration = workItemLinkBean.getIntegerValue1();
		if (duration!=null) {
			parametersMap.put(DURATION, String.valueOf(duration));
		}
		return parametersMap;
	}
	
	/**
	 * Gets the parameter metadata as a list
	 * @param locale
	 * @return
	 */
	@Override
	public List<ParameterMetadata> getParameterMetadata(Locale locale) {
		List<ParameterMetadata> parameterList = new LinkedList<ParameterMetadata>();
		ParameterMetadata duration = new ParameterMetadata(DURATION, "double", false);
		parameterList.add(duration);
		return parameterList;
	}
	
	static interface JSON_FIELDS {
		static final String DURATION_LABEL = "durationLabel";
		static final String DURATION = "duration";
		static final String TIME_UNIT = "timeUnit";
	}

	/**
	 * Gets the JavaScript class for configuring the link type specific part
	 * @return
	 */
	@Override
	public String getLinkTypeJSClass() {
		return "js.ext.com.track.linkType.MeetingTopic";
	}

	/**
	 * Gets the link type specific configuration as JSON
	 * @param workItemLinkBean
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public String getSpecificJSON(TWorkItemLinkBean workItemLinkBean, TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder("{");
		if (workItemLinkBean!=null) {
			Integer duration = workItemLinkBean.getIntegerValue1();
			JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.DURATION, duration);
		}
		JSONUtility.appendStringValue(stringBuilder,
				JSON_FIELDS.DURATION_LABEL, LocalizeUtil.getLocalizedTextFromApplicationResources(
					"item.tabs.itemLink.lbl.duration", locale));
		JSONUtility.appendStringValue(stringBuilder,
				JSON_FIELDS.TIME_UNIT, LocalizeUtil.getLocalizedTextFromApplicationResources(
					"common.timeunit.minutes", locale), true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	/**
	 * Sets the workItemLinkBean according to the values submitted in the parameters map
	 * @param parametersMap the parameters from link configuration
	 * @param workItemLinkBean
	 * @param personBean the current user
	 * @param locale
	 * @return
	 */
	@Override
	public List<ErrorData> unwrapParametersBeforeSave(
			Map<String, String> parametersMap,
			TWorkItemLinkBean workItemLinkBean, TPersonBean personBean, Locale locale) {
		Integer duration = null;
		try {
			if (parametersMap!=null) {
				duration = Integer.valueOf(parametersMap.get(DURATION));
			}
		} catch (Exception e) {
			LOGGER.warn("Getting the duration parameter failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		workItemLinkBean.setIntegerValue1(duration);
		return null;
	}

	
}
