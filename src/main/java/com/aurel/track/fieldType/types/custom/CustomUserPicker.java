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


package com.aurel.track.fieldType.types.custom;

import java.util.LinkedList;
import java.util.List;

import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.design.IFieldTypeDT;
import com.aurel.track.fieldType.design.custom.picker.UserPickerDT;
import com.aurel.track.fieldType.design.renderer.SelectRendererDT;
import com.aurel.track.fieldType.design.renderer.TypeRendererDT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.custom.picker.UserPickerRT;
import com.aurel.track.fieldType.runtime.renderer.CustomSelectRendererRT;
import com.aurel.track.fieldType.runtime.renderer.SelectMultipleRendererRT;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;

/**
 * User picker
 * Not yet implemented completely so comment the "implements ICustomFieldType"
 * not to appear in the available field types box 
 * @author Tamas Ruff
 *
 */
public class CustomUserPicker extends FieldType {

	public static interface PARAMETERCODES {
		public static int DATASOURCE_OPTION = 0; //role or department
		public static int ROLE_OPTION = 1; //one or more role selected
		public static int DEPARTMENT_OPTION = 2; //one or more department selected
		public static int AUTOMAIL_OPTION = 3; //one automail option 		
		public static int IS_MULTIPLE_SELECT = 4;//allo selecting multiple items from issue picker
	}

	public static interface DATASOURCE_OPTIONS {
		//if change this constants change it also in the jsp and in the js!
		public static int ROLE = 1;
		public static int DEPARTMENT = 2;
	}

	public static interface AUTOMAIL_OPTIONS {
		public static int NOMAIL = 0;
		public static int ORIGINATOR = 1;
		public static int MANAGER = 2;
		public static int RESPONSIBLE = 3;
		public static int CONSULTANT = 4;
		public static int INFORMANT = 5;
		public static int OBSERVER = 6;
	}

	/**
	 * Symbolic ID for the current user's department, should be replaced with the real one at runtime
	 */
	public static int CURRENT_USERS_DEPARTMENT = -1;
	
	@Override
	public IFieldTypeDT getFieldTypeDT() {
		return new UserPickerDT(getPluginID());
	}

	@Override
	public String getIconName() {
		return FieldDesignBL.ICON_CLS.COMBO_ICONCLS;
	}

	@Override
	public TypeRendererDT getRendererDT() {
		return SelectRendererDT.getInstance();
	}

	@Override
	public IFieldTypeRT getFieldTypeRT() {
		return new UserPickerRT();
	}
	
	/**
	 * Based on field settings the field could be single or multiple select.
	 * This method checks whether is multiple select or not.
	 * @return
	 */
	private boolean isMultipleSelectPicker(Integer configID) {
		boolean isMultipleSelect = false;		
		if (configID!=null) {
			List<TGeneralSettingsBean> generalSettingsBeans = GeneralSettingsBL.loadByConfig(configID);
			for (TGeneralSettingsBean generalSettingsBean : generalSettingsBeans) {
				if (generalSettingsBean.getParameterCode()!=null) {
					if(generalSettingsBean.getParameterCode().intValue() == PARAMETERCODES.IS_MULTIPLE_SELECT) {
						isMultipleSelect = BooleanFields.fromStringToBoolean(generalSettingsBean.getCharacterValue());
					}
				}
			
			}
		}
		return isMultipleSelect;
	}
	
	@Override
	public TypeRendererRT getRendererRT() {
		TFieldConfigBean fieldConfigBean = null;
		if (fieldID!=null) {
			fieldConfigBean = FieldRuntimeBL.getValidConfig(fieldID, null, null);
		}
		if (fieldConfigBean==null) {
			return CustomSelectRendererRT.getInstance();
		} else {
			if (isMultipleSelectPicker(fieldConfigBean.getObjectID())) {
				return SelectMultipleRendererRT.getInstance();
			} else {
				return CustomSelectRendererRT.getInstance();
			}
		}
	}
	
	/**
	 * Get the list of compatible field types
	 * Compatibility can be two way (simple list <-> editable list, or user picker <-> on behalf picker) or only one way (long text -> rich text)
	 * @return
	 */
	@Override
	public List<FieldType> getCompatibleFieldTypes() {
		List<FieldType> compatibleFieldTypes = new LinkedList<FieldType>();
		compatibleFieldTypes.add(new CustomOnBehalfOfPicker());
		return compatibleFieldTypes;
	}
}
