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

package com.aurel.track.dbase;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.lucene.LuceneUtil;

/**
 * Add the duration and target duration fields
 * @author Tamas
 *
 */
public class Migrate502To503 {
	
	
	private static final Logger LOGGER = LogManager.getLogger(LuceneUtil.class);
	
	/**
	 * Change the general date fields to more specialized ones to allow custom logic at client side
	 */
	public static void changeDateFieldNames() {
		TFieldBean startDateFieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_STARTDATE);
		if (startDateFieldBean!=null) {
			startDateFieldBean.setFieldType("com.aurel.track.fieldType.types.system.text.SystemStartDate");
			FieldBL.save(startDateFieldBean);
		}
		TFieldBean endDateFieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_ENDDATE);
		if (endDateFieldBean!=null) {
			endDateFieldBean.setFieldType("com.aurel.track.fieldType.types.system.text.SystemEndDate");
			FieldBL.save(endDateFieldBean);
		}
		
		TFieldBean startDateTargetFieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_TOP_DOWN_START_DATE);
		if (startDateTargetFieldBean!=null) {
			startDateTargetFieldBean.setFieldType("com.aurel.track.fieldType.types.system.text.SystemStartDateTarget");
			FieldBL.save(startDateTargetFieldBean);
		}
		TFieldBean endDateTargetFieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_TOP_DOWN_END_DATE);
		if (endDateTargetFieldBean!=null) {
			endDateTargetFieldBean.setFieldType("com.aurel.track.fieldType.types.system.text.SystemEndDateTarget");
			FieldBL.save(endDateTargetFieldBean);
		}
		FieldTypeManager.getInstance().invalidateCache();
	}
	
	public static void addDurations() {
		addDuration(SystemFields.INTEGER_DURATION, "Duration", "com.aurel.track.fieldType.types.system.text.SystemDuration", "Duration", "Duration");
		addDuration(SystemFields.INTEGER_TOP_DOWN_DURATION, "TargetDuration", "com.aurel.track.fieldType.types.system.text.SystemTargetDuration", "Target duration", "Target duration");
	}
	
	private static void addDuration(Integer durationField, String name, String fieldType, String label, String tooltip) {
		TFieldBean durationFieldBean = FieldBL.loadByPrimaryKey(durationField);
		if (durationFieldBean==null) {
			LOGGER.info("Adding duration field " + durationField);
			Connection cono = null;
			try {
				cono = InitDatabase.getConnection();
				Statement ostmt = cono.createStatement();
				cono.setAutoCommit(false);
				ostmt.executeUpdate(addField(durationField, name, fieldType));
				ostmt.executeUpdate(addFieldConfig(durationField, durationField, label, tooltip));
				cono.commit();
				cono.setAutoCommit(true);
			} catch (Exception e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if (cono != null) {
						cono.close();
					}
				} catch (Exception e) {
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}
	
	/**
	 * Add new field
	 * @param objectID
	 * @param label
	 * @param typeflag
	 * @param sortOrder
	 * @param iconName
	 * @param UUID
	 * @return
	 */
	private static String addField(Integer objectID, String name, String fieldType) {
		return "INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)" + 
			"VALUES (" + objectID + ", '" + name + "', '" + fieldType + "', 'N', 'N', 'N')";
	}
	
	/**
	 * Adds a field config
	 * @param objectID
	 * @param fieldID
	 * @param label
	 * @param tooltip
	 * @return
	 */
	private static String addFieldConfig(Integer objectID, Integer fieldID, String label, String tooltip) {
		return "INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED, HISTORY)" +
				"VALUES (" + objectID + ", " + fieldID + ", '" + label + "', '" + tooltip + "', 'N', 'N')";
	}
}
