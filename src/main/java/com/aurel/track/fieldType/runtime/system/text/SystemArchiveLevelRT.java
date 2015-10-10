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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.ArchiveLevelBulkSetter;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.fieldChange.apply.ArchiveLevelFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.ArchiveLevelFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.converter.IntegerSetterConverter;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.SystemOutputBaseRT;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.resources.LocalizeUtil;

public class SystemArchiveLevelRT  extends SystemOutputBaseRT {
	
	private static final Logger LOGGER = LogManager.getLogger(SystemArchiveLevelRT.class);
	
	/** 
	 * Defines the data type of the field attribute
	 * Used by saving field attributes for custom fields
	 * and saving explicit history for both system and custom fields
	 * Should be a @ValueType constant
	 * Here not really meaningful because this field will be never historized
	 * @return
	 */
	public int getValueType() {
		return ValueType.INTEGER;
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new ArchiveLevelBulkSetter(fieldID);
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new ArchiveLevelFieldChangeConfig(fieldID);
	}
	
	/**
	 * Gets the IActivityExecute object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new ArchiveLevelFieldChangeApply(fieldID);
	}
	
	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		return new IntegerSetterConverter(fieldID);
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
		return getShowValue(value, locale);
	}
	
	/**
	 * Get the value to be shown from the database and then try to localize  
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		String keySuffix = null;
		if (value==null) {
			return "";
		}
		Integer intValue = null;
		try {
			intValue = (Integer)value;
		} catch (Exception e) {
			LOGGER.warn("Casting the value type " + value.getClass().getName() +
					" to Integer failed with " + e.getMessage(), e);
		}
		if (intValue!=null) {
			if (TWorkItemBean.ARCHIVE_LEVEL_ARCHIVED.equals(intValue)) {
				keySuffix="archived";
			} else {
				if (TWorkItemBean.ARCHIVE_LEVEL_DELETED.equals(intValue)) {
					keySuffix="deleted";
				}
			}
		}
		if (keySuffix!=null) {
			String localizedLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("common.archiveLevel." + keySuffix, locale);
			if (localizedLabel != null && localizedLabel.length() > 0) {
				return localizedLabel;
			}
		}
		return "";
	}
	
	public boolean isoDiffersFromLocaleSpecific() {
		return true;
	}
	
	/**
	 * Get the ISO show value for locale independent exporting to xml
	 * typically same as show value, date and number values are formatted by iso format 
	 * @param fieldID
	 * @param parameterCode
	 * @param value
	 * @param workItemID
	 * @param localLookupContainer
	 * @param locale
	 * @return
	 */
	public String getShowISOValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		if (value==null) {
			return null;
		}
		Integer intValue = null;
		try {
			intValue = (Integer)value;
		} catch (Exception e) {
			LOGGER.error("Casting the value type " + value.getClass().getName() +
					" to Integer failed with " + e.getMessage(), e);
		}
		if (intValue==null) {
			return null;
		} else {
			return intValue.toString();
		}
	}
	
	/**
	 * Parses the string value into the corresponding object value
	 * @param isoStrValue
	 * @return
	 */
	@Override
	public Object parseISOValue(Object isoStrValue) {
		if (isoStrValue!=null) {
			return new Integer(isoStrValue.toString());
		}
		return null; 
	}
	
	/**
	 * Whether the field should appear in the groupable fields list
	 * @return
	 */
	@Override
	public boolean isGroupable() {
		return false;
	}
	
	/**
	 * Whether the field can be rendered on a form
	 * @return
	 */
	@Override
	public boolean mightAppearOnForm() {
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
		return LuceneUtil.LOOKUPENTITYTYPES.DIRECTINTEGER;
	}
}
