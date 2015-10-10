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
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.runtime.validators.ExtensibleSelectRequiredValidator;
import com.aurel.track.fieldType.runtime.validators.Validator;

public class CustomExtensibleSelectRT extends CustomSelectSimpleRT {
	public static Integer ADD_NEW = Integer.valueOf(-2); 
	
	/**
	 * Returns a map of validators to be applied
	 */
	@Override
	public Map<Integer, List<Validator>> getValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean, Integer parameterCode,
			Object settingsObject, TWorkItemBean workItemBean) {
		Map<Integer, List<Validator>> validatorsMap = new HashMap<Integer, List<Validator>>(); 
		if(BooleanFields.TRUE_VALUE.equals(fieldConfigBean.getRequired())){
			Integer[] arrSelectedOption = null;
			try {
				arrSelectedOption = (Integer[])workItemBean.getAttribute(fieldID);
			} catch (Exception e) {
			}			
			String textValue = (String)workItemBean.getAttribute(fieldID, Integer.valueOf(2));
			if (arrSelectedOption!=null && arrSelectedOption.length>0 && ADD_NEW.equals(arrSelectedOption[0]) 
					&& (textValue==null || "".equals(textValue.trim()))) {				
				//add the specific required validator: if ADD_NEW is selected then the textValue should be filled
				List<Validator> validatorsList = new ArrayList<Validator>();				
				validatorsList.add(new ExtensibleSelectRequiredValidator());
				if (parameterCode==null) {
					parameterCode = Integer.valueOf(0);
				}
				validatorsMap.put(parameterCode, validatorsList);
			} else {
				//classic required validator
				validatorsMap = super.getValidators(fieldID,fieldConfigBean,parameterCode,
						settingsObject, workItemBean); 
			}
		}
		return validatorsMap;
	}
}
