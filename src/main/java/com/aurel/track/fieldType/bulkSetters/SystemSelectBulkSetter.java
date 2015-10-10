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

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.beans.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ISelect;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.fieldType.types.custom.CustomUserPicker.PARAMETERCODES;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
/**
 * Bulk setter for system select fields
 * Differs from the custom select by the fact that the value is Integer instead of Integer[]
 * @author Tamas
 *
 */
public class SystemSelectBulkSetter extends AbstractBulkSetter {
	private static final Logger LOGGER = LogManager.getLogger(SystemSelectBulkSetter.class);
	protected boolean dynamicIcons = false;
	public SystemSelectBulkSetter(Integer fieldID, boolean dynamicIcons) {
		super(fieldID);
		this.dynamicIcons = dynamicIcons;
	}
	

	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	public String getSetterValueControlClass() {
		
		switch (relation) {
		case BulkRelations.SET_TO:
			return BulkValueTemplates.SELECT_BULK_VALUE_TEMPLATE;
		}
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
	@Override
	public String getSetterValueJsonConfig(String baseName, Object value,
		Object dataSource, Map<Integer, String> labelMap, boolean disabled, TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder("{");
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName));
		if (value!=null) {
			Integer intValue = null;
			try {
				intValue = (Integer)value;
			} catch (Exception e) {
			}
			JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, intValue);
		}
		JSONUtility.appendILabelBeanListWithIcons(stringBuilder, fieldID,
				(ILookup)FieldTypeManager.getFieldTypeRT(fieldID), "datasource", (List<ILabelBean>)dataSource, false);
		JSONUtility.appendIntegerValue(stringBuilder, "fieldID", fieldID);
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the iconCls class if dynamicIcons is false
	 * @param labelBean
	 */
	protected String getIconCls(ILabelBean labelBean) {
		if (!dynamicIcons) {
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null) {
				ILookup select = (ILookup)fieldTypeRT;
				return select.getIconCls(labelBean);
			}
		}
		return null;
	}

	public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
		if (displayStringMap == null) {
			return null;
		}
		String value = displayStringMap.get(getKeyPrefix());
		if (value==null) {
			return null;
		}
		switch (getRelation()) {
		case BulkRelations.SET_TO:
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			LOGGER.warn("Converting the " + value +  " to Integer for display string failed with " + e.getMessage(), e);
		}
		}
		return null;
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
		if (getRelation()==BulkRelations.SET_NULL) {
			workItemBean.setAttribute(fieldID, parameterCode, null);
			return null;
		}
		Integer newValue = null;
		try {
			newValue = (Integer)value;
		} catch (Exception e) {
			LOGGER.debug("Getting the integer value for " + value +  " failed with " + e.getMessage(), e);
		}
		switch (getRelation()) {
		case BulkRelations.SET_TO: 
			//change only if the target value not equals the actual value
			Object oldValue = workItemBean.getAttribute(fieldID);
			Integer projectID = workItemBean.getProjectID();
			Integer issueTypeID = workItemBean.getListTypeID();
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (oldValue==null || fieldTypeRT.valueModified(newValue, oldValue)) {
				if (selectValueSurelyAllowed) {
					//do not load the datasouce
					workItemBean.setAttribute(fieldID, parameterCode, newValue);
					return null;
				} else {
					//test whether it is found in the datasouce
					ISelect selectFieldType = (ISelect)fieldTypeRT;
					List<ILabelBean> allowedIBeanList = selectContext.getDatasourceCache(fieldID, projectID, issueTypeID);
					if (allowedIBeanList==null) {
						allowedIBeanList = selectFieldType.loadEditDataSource(selectContext);
						selectContext.addDatasourceCache(fieldID, projectID, issueTypeID, allowedIBeanList);
					}
					if (allowedIBeanList!=null) {
						for (IBeanID beanID : allowedIBeanList) {
							if (EqualUtils.equal(newValue, beanID.getObjectID())) {
								//if target value found among the possible values then set as allowed change 																
								workItemBean.setAttribute(fieldID, parameterCode, newValue);
								return null;
							}
						}
					}
					//not found among the possible values, add the error key in the list
					String fieldLabel = selectContext.getFieldLabelCache(fieldID, projectID, issueTypeID);
					if (fieldLabel==null) {
						TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getValidConfig(fieldID, issueTypeID, projectID);
						if (fieldConfigBean!=null) {
							fieldConfigBean = LocalizeUtil.localizeFieldConfig(fieldConfigBean, selectContext.getLocale());
							if (fieldConfigBean!=null) {
								fieldLabel = fieldConfigBean.getLabel();
							}
						}
						selectContext.addFieldLabelCache(fieldID, projectID, issueTypeID, fieldLabel);
					}
					return new ErrorData("itemov.massOperation.err.targetValueNotAllowed", fieldLabel);
				}
			}
			break;
		}
		return null;
	}		
}
