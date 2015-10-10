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


package com.aurel.track.fieldType.bulkSetters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.json.JSONUtility;

/**
 * Bulk setter for archive level field (archive/delete issue)
 * @author Tamas
 *
 */
public class ArchiveLevelBulkSetter extends AbstractBulkSetter {
	public ArchiveLevelBulkSetter(Integer fieldID) {
		super(fieldID);
	}

	@Override
	public List <Integer> getPossibleRelations(boolean required) {
		List <Integer> relations = new ArrayList<Integer>();
		relations.add(Integer.valueOf(BulkRelations.ARCHIVE));
		relations.add(Integer.valueOf(BulkRelations.DELETE));
		relations.add(Integer.valueOf(BulkRelations.UNARCHIVE_UNDELETE));
		return relations;
	}
	
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	public String getSetterValueControlClass() {
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;
	}

	/**
	 * The JSON configuration object for configuring the js control(s) containing the value
	 * @param baseName: the name of the control: important by submit
	 * @param value: the value to be set by rendering (first time or after a submit)
	 * @param dataSource: defined only for lists (list for global lists, map for context dependent lists)
	 * @param labelMap: defined only for context (project/issuType) dependent lists
	 * @param disabled: whether the control is disabled
     * @param personBean
	 * @param locale
	 * @return
	 */
	public String getSetterValueJsonConfig(String baseName, Object value,
		Object dataSource, Map<Integer, String> labelMap, boolean disabled, TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder("{");
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName));
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * No ftl, never called
	 */
	public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
		switch (getRelation()) {
		case BulkRelations.ARCHIVE:
			return TWorkItemBean.ARCHIVE_LEVEL_ARCHIVED;
		case BulkRelations.DELETE:
			return TWorkItemBean.ARCHIVE_LEVEL_DELETED;
		default:
			return TWorkItemBean.ARCHIVE_LEVEL_UNARCHIVED;
		}
	}
	
	/**
	 * Sets the workItemBean's attribute depending on the value and bulkRelation
	 * @param workItemBean
	 * @param fieldID
	 * @param parameterCode
	 * @param bulkTranformContext
	 * @param selectContext
	 * @param value	
	 * @return ErrorData if an error is found
	 */
	@Override
	public ErrorData setWorkItemAttribute(TWorkItemBean workItemBean,
			Integer fieldID, Integer parameterCode, 
			BulkTranformContext bulkTranformContext, 
			SelectContext selectContext, Object value) {
		Integer valueByRelation = null;
		switch (getRelation()) {
		case BulkRelations.ARCHIVE:
			valueByRelation = TWorkItemBean.ARCHIVE_LEVEL_ARCHIVED;
			break;
		case BulkRelations.DELETE:
			valueByRelation = TWorkItemBean.ARCHIVE_LEVEL_DELETED;
			break;
		default:
			valueByRelation = TWorkItemBean.ARCHIVE_LEVEL_UNARCHIVED;
			break;
		}	
		workItemBean.setAttribute(fieldID, valueByRelation);
		return null; 
	}
}
