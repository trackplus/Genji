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

package com.aurel.track.fieldType.runtime.system.text;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.CommentBulkSetter;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.fieldChange.config.CommentChangeConfig;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.prop.ApplicationBean;

/**
 * Comment specific field length
 * @author Tamas
 *
 */
public class SystemCommentRT extends SystemRichTextBaseRT {
	
	private static final Logger LOGGER = LogManager.getLogger(SystemCommentRT.class);	
	
	@Override
	public int getDatabaseFieldLength() {
		return ApplicationBean.getInstance().getCommentMaxLength();
	}
		
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new CommentBulkSetter(fieldID);		
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new CommentChangeConfig(fieldID);
	}
	
	/**
	 * Loads the saved custom attribute value from the database 
	 * to the workItem customAttributeValues Map
	 * The comments are stored in a dumped string to be used in matchers
	 * For showing the comments they should be stored separately 
	 * @param fieldID 
	 * @param parameterCode neglected for single custom fields
	 * @param workItemBean
	 * @param attributeValueMap: 
	 * 	-	key: fieldID_parameterCode
	 * 	-	value: TAttributeValueBean or list of TAttributeValueBeans
	 */
	@Override
	public void loadAttribute(Integer fieldID, Integer parameterCode, 
			TWorkItemBean workItemBean, Map<String, Object> attributeValueMap) {
		List<TAttributeValueBean> attributeValueList = null;
		//get the attributes list from the custom attributes map
		try {
			attributeValueList = (List<TAttributeValueBean>)attributeValueMap.get(MergeUtil.mergeKey(fieldID, parameterCode));
		} catch (Exception e) {
			LOGGER.error("Converting the attribute value for field " + fieldID + " and parameterCode " +
					parameterCode + " for workItem " + workItemBean.getObjectID() + " to List failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		//dump all comments into a single string to be used in the matcher
		StringBuilder stringBuilder = new StringBuilder();
		if (attributeValueList!=null) {
			for (int i = 0; i < attributeValueList.size(); i++) {
				TAttributeValueBean attributeValueBean = attributeValueList.get(i);
				stringBuilder.append(AttributeValueBL.getSpecificAttribute(attributeValueBean, getValueType()));
			}
		}
		//set the attribute on workItem
		if (stringBuilder.length()>0) {
			//leave null if no comment was added for matching also the the NULL/NOT NULL matcher
			workItemBean.setAttribute(fieldID, parameterCode, stringBuilder.toString());
		}
	}
}
