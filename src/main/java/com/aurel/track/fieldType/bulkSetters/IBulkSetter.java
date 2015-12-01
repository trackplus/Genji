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


package com.aurel.track.fieldType.bulkSetters;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.SelectContext;


/**
 * Base interface for configuring a bulk setter 
 * @author Tamas Ruff
 */
public interface IBulkSetter {	
	
	/**
	 * @return the fieldID
	 */
	Integer getFieldID();

	/**
	 * @return the relation
	 */
	int getRelation();


	/**
	 * @param relation the relation to set
	 */
	void setRelation(int relation);
	
	/**
	 * Whether the value selected for a select field is surely allowed 
	 * @return
	 */
	boolean isSelectValueSurelyAllowed();
	
	/**
	 * Set the selectValueSurelyAllowed value 
	 * @param selectValueSurelyAllowed
	 */
	void setSelectValueSurelyAllowed(boolean selectValueSurelyAllowed);
	
	/**
	 * Gets the possible relations for a field type 
	 */
	List<Integer> getPossibleRelations(boolean required);
	
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	String getSetterValueControlClass();
	
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
	String getSetterValueJsonConfig(String baseName, Object value,
		Object dataSource, Map<Integer, String> labelMap, boolean disabled, TPersonBean personBean, Locale locale);
		
	/**
	 * Process the submitted value from the displayStringMap into value of expected type
	 * @param displayStringMap
	 * @param locale
	 * @return
	 */
	Object fromDisplayString(Map<String, String> displayStringMap, Locale locale);
	
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
	ErrorData setWorkItemAttribute(TWorkItemBean workItemBean,
			Integer fieldID, Integer parameterCode, 
			BulkTranformContext bulkTranformContext, 
			SelectContext selectContext, Object value);
}
