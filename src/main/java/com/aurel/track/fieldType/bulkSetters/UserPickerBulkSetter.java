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

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ISelect;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.fieldType.types.custom.CustomUserPicker.PARAMETERCODES;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
/**
 * Bulk setter for system select fields
 * Differs from the custom select by the fact that the value is Integer instead of Integer[]
 * @author Tamas
 *
 */
public class UserPickerBulkSetter extends SystemSelectBulkSetter {	
	private static final Logger LOGGER = LogManager.getLogger(UserPickerBulkSetter.class);
	
	public UserPickerBulkSetter(Integer fieldID) {
		super(fieldID, false);	
	}
	
	/**
	 * Based on field settings the field could be single or multiple select.
	 * This method checks whether is multiple select or not.
	 * @return
	 */
	private boolean isMultipleSelectPicker() {
		boolean isMultipleSelect = false;		
		if(fieldID != null) {
			TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getDefaultFieldConfig(fieldID);
			if (fieldConfigBean!=null) {
				List<TGeneralSettingsBean> generalSettingsBeans = GeneralSettingsBL.loadByConfig(fieldConfigBean.getObjectID());
				for (TGeneralSettingsBean generalSettingsBean : generalSettingsBeans) {
					if (generalSettingsBean.getParameterCode()!=null) {
						if(generalSettingsBean.getParameterCode().intValue() == PARAMETERCODES.IS_MULTIPLE_SELECT) {
							isMultipleSelect = BooleanFields.fromStringToBoolean(generalSettingsBean.getCharacterValue());
						}
					}
				}
			}
		}
		return isMultipleSelect;
	}

	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	@Override
	public String getSetterValueControlClass() {
		switch (relation) {
		case BulkRelations.SET_TO:
			if(isMultipleSelectPicker()) {
				return BulkValueTemplates.USER_PICKER_MULTIPLE_SELECT;
			}
			return BulkValueTemplates.SELECT_BULK_VALUE_TEMPLATE;
		}
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;
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
		Integer[] newValue = null;
		if (value!=null) {
			try {
				newValue = (Integer[])value;
			} catch(Exception ex) {
				LOGGER.info("Converting the value " + value + " of type " + value.getClass().getName() + " to Integer[] faled with " + ex.getMessage() );
			}
		}
		switch (getRelation()) {
		case BulkRelations.SET_TO: 
			//change only if the target value not equals the actual value
			Object oldValue = workItemBean.getAttribute(fieldID);
			Integer projectID = workItemBean.getProjectID();
			Integer issueTypeID = workItemBean.getListTypeID();
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (oldValue==null || fieldTypeRT.valueModified(newValue, oldValue)) {
				//test whether it is found in the datasouce
				ISelect selectFieldType = (ISelect)fieldTypeRT;
				List<ILabelBean> allowedIBeanList = selectContext.getDatasourceCache(fieldID, projectID, issueTypeID);
				if (allowedIBeanList==null) {
					allowedIBeanList = selectFieldType.loadEditDataSource(selectContext);
					selectContext.addDatasourceCache(fieldID, projectID, issueTypeID, allowedIBeanList);
				} 
				if (allowedIBeanList!=null) {
					for (IBeanID beanID : allowedIBeanList) {
						if (newValue!=null && newValue.length>0 && EqualUtils.equal(newValue[0], beanID.getObjectID())) {
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
			break;
		}
		return null;
	}	

	@Override
	public Object fromDisplayString(Map<String, String> displayStringMap, Locale locale) {
		if (displayStringMap == null) {
			return null;
		}
		switch (getRelation()) {
		case BulkRelations.SET_TO:
			String values = displayStringMap.get(getKeyPrefix());
			if (values!=null) {
				String[] idsList = values.split(",");
				Set<Integer>idsSet = new HashSet<Integer>();
				for (String oneIDString : idsList) {
					Integer oneID = Integer.valueOf(oneIDString);
					idsSet.add(oneID);
				}
				return GeneralUtils.createIntegerArrFromSet(idsSet);
			}
		}				
		return null;
	}
}
