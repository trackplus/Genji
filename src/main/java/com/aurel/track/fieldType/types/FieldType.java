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


package com.aurel.track.fieldType.types;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.fieldType.design.IFieldTypeDT;
import com.aurel.track.fieldType.design.renderer.TypeRendererDT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;

/**
 * Defines the design time and runtime types and renderers for system and custom fields
 * @author Tamas Ruff
 *
 */
public abstract class FieldType {
		
	private static final Logger LOGGER = LogManager.getLogger(FieldType.class);
	
	protected Integer fieldID;
	

	public String getPluginID() {
		return this.getClass().getName();
	}

	/**
	 * Get the name of the icon for a type.
	 * This name must be a fileName in the icons directory
	 * @return
	 */
	public abstract String getIconName();
	
	/**
	 * The design time field type
	 * @return
	 */
	public abstract IFieldTypeDT getFieldTypeDT();
	
	/**
	 * The runtime field type
	 * @return
	 */
	public abstract IFieldTypeRT getFieldTypeRT();
	
	/**
	 * Get the renderer for designTime environment
	 * @return
	 */
	public abstract TypeRendererDT getRendererDT();
	
	/**
	 * Get the renderer for runtime environment
	 * @return
	 */
	public abstract TypeRendererRT getRendererRT();
	
	/**
	 * Get the FieldType by class name
	 * @param fieldTypeClassName
	 * @return
	 */
	public static FieldType fieldTypeFactory(String fieldTypeClassName) {
		Class fieldTypeClass = null;
		if (fieldTypeClassName == null) {
			LOGGER.warn("No fieldType specified ");
			return null;
		}
		try {
			fieldTypeClass = Class.forName(fieldTypeClassName);
		} catch (ClassNotFoundException e) {
			LOGGER.warn("The fieldType class " + fieldTypeClassName + "  not found found in the classpath " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (fieldTypeClass!=null)
		try {
			return (FieldType) fieldTypeClass.newInstance();
		} catch (Exception e) {
			LOGGER.warn("Instantiating the fieldType class " + fieldTypeClassName + "  failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}	
		return null;
	}
	
	/**
	 * Get the list of compatible field types
	 * Compatibility can be two way (simple list <-> editable list, or user picker <-> on behalf picker) or only one way (long text -> rich text)
	 * @return
	 */
	public List<FieldType> getCompatibleFieldTypes() {
		return null;
	}
	
	public Integer getFieldID() {
		return fieldID;
	}

	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
		
	
}
