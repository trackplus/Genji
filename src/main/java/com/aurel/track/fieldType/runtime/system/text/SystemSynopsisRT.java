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

package com.aurel.track.fieldType.runtime.system.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.validators.StringValidator;
import com.aurel.track.fieldType.runtime.validators.Validator;

/**
 * Specific validator for synopsis
 * @author Tamas
 *
 */
public class SystemSynopsisRT extends SystemShortTextRT {
	/**
	 * Max length for Synopsis is 255
	 */
	@Override
	public Map<Integer, List<Validator>> getValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean,Integer parameterCode,
			Object settingsObject, TWorkItemBean workItemBean) {
		Map<Integer, List<Validator>> validatorsMap = super.getValidators(fieldID,fieldConfigBean,parameterCode,
				settingsObject, workItemBean); 		
		List<Validator> validatorsList = new ArrayList<Validator>();
		if(validatorsMap.get(Integer.valueOf(0))!=null){
			validatorsList=validatorsMap.get(Integer.valueOf(0));
		}
		validatorsMap.put(Integer.valueOf(0), validatorsList);
		StringValidator stringValidator=new StringValidator();
		stringValidator.setMaxLength(Integer.valueOf(255));
		validatorsList.add(stringValidator);
		return validatorsMap;
	}
}
