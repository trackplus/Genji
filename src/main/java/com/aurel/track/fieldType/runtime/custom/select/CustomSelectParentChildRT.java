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

package com.aurel.track.fieldType.runtime.custom.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.DropDownContainer;
import com.aurel.track.fieldType.runtime.base.ICustomFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.bl.CustomSelectUtil;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;

public class CustomSelectParentChildRT extends CustomCompositeBaseRT {
	
	@Override
	public int getNumberOfParts() { 
		return 2;
	}

	/**
	 * The custom field type of each part
	 * It may imply defining of a new class for each part separately
	 * Each "part"-class should implement the complying callback interfaces  
	 * @param index
	 * @return
	 */
	@Override
	public ICustomFieldTypeRT getCustomFieldType(int index) {
		List<Integer> childNumbersHierarchy;
		switch(index) {
		case 1:
			return new CustomSelectSimpleRT();
		case 2:
			//the parent is the first part of the composite 
			//and the child list is the first child list of the parent 
			childNumbersHierarchy = new ArrayList<Integer>();
			childNumbersHierarchy.add(Integer.valueOf(1));
			return new CustomDependentSelectRT(Integer.valueOf(1), Integer.valueOf(1), childNumbersHierarchy);
		}
		return null;
	}
	
	public Map getValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean, Integer parameterCode, 
			Object settingsObject, TWorkItemBean workItemBean){
		Map validatorsMap = new HashMap();
		//enough to verify the last component about the required validation
		//otherwise the validation message is doubled
		IFieldTypeRT fieldTypeRT = getCustomFieldType(getNumberOfParts());
		validatorsMap.putAll(fieldTypeRT.procesLoadValidators(fieldID, 
					fieldConfigBean, getNumberOfParts(), 
					settingsObject,workItemBean));
		return validatorsMap;
	}
	
	/**
	 * Actualizes the dropDownContainer with the datasource for a select type field 
	 * by editing an existing issue/creating a new issue/refreshing a composite part. 
	 * If the parameterCode of the selectContext is not null then the field is is a part of a composite field
	 * and probably a refresh took place (not edit/create). In this case only the datasource of the 
	 * composite part specified by paramaterCode will be reloaded, not for each part of the composite 
	 * @param selectContext
	 * @param dropDownContainer 
	 * @return
	 */
	@Override
	public void processLoadDataSource(SelectContext selectContext, DropDownContainer dropDownContainer){
		CustomSelectSimpleRT parentSelect=(CustomSelectSimpleRT)getCustomFieldType(1);
		CustomDependentSelectRT childSelect=(CustomDependentSelectRT)getCustomFieldType(2);
		Integer parameterCode = selectContext.getParameterCode();
		TFieldConfigBean fieldConfigBean = selectContext.getFieldConfigBean();
		if (parameterCode==null) {
			//edit or create took place
			//first select
			parameterCode=new Integer(1);
			selectContext.setParameterCode(parameterCode);
			parentSelect.processLoadDataSource(selectContext, dropDownContainer);
			Object origParentValue=selectContext.getWorkItemBean().getAttribute(selectContext.getFieldID(), parameterCode);
			Integer[] parentOptions = CustomSelectUtil.getSelectedOptions(origParentValue);
			if (parentOptions==null || parentOptions.length==0) {
				//ensure the first selection in case of create and no default value
				//by create: if no default value was set for this list (origParentValue is null or Integer[0] 
				//			after calling the setDefaultAttribute()) then get the first value from the list: 
				///			we should be sure that even when no default value is defined the first entry is 
				//			implicitely selected to have a parentID for getting the child list
				//by edit: if no value (i.e. null) was previously saved but now the parent list has entries then get the first
				//all this if fieldConfigBean.isRequired() is true i.e. no "None" header is present: 
				//if None is present then no child list is needed
				//if print mode, then leave the value empty
				if (!selectContext.isView() && fieldConfigBean.isRequiredString() && selectContext.isCreate()) {
					//get the previously loaded parent list
					List parentList=dropDownContainer.getDataSourceList(MergeUtil.mergeKey(selectContext.getFieldID(), parameterCode));
					//the parent list has at least one element set the first one for the children
					if(parentList!=null && !parentList.isEmpty()){
						selectContext.getWorkItemBean().setAttribute(selectContext.getFieldID(), parameterCode, 
								new Integer[]{((TOptionBean)parentList.get(0)).getObjectID()});
					}
				}
			}
			//second select
			parameterCode=new Integer(2);
			selectContext.setParameterCode(parameterCode);
			childSelect.processLoadDataSource(selectContext, dropDownContainer);
		} else if (selectContext.getParameterCode().intValue()==2) {
			//refresh took place
			childSelect.processLoadDataSource(selectContext, dropDownContainer);
		}
	}
}
