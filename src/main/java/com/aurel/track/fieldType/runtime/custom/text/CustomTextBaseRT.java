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


package com.aurel.track.fieldType.runtime.custom.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.StringMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.matchers.run.StringMatcherRT;
import com.aurel.track.fieldType.runtime.validators.StringValidator;
import com.aurel.track.fieldType.runtime.validators.Validator;

/**
 * Base class for custom text box/area fields with text content
 * @author Tamas Ruff
 *
 */
public abstract class CustomTextBaseRT extends CustomTextBoxBaseRT {

	/**
	 * Get the specific attribute from TTextBoxSettingsBean for default value
	 */
	@Override
	public Object getSpecificDefaultAttribute(TTextBoxSettingsBean textBoxSettingsBean) {
		return textBoxSettingsBean.getDefaultText();
	}

	@Override
	public Map<Integer, List<Validator>> getValidators(Integer fieldID,
			TFieldConfigBean fieldConfigBean,Integer parameterCode,
			Object settingsObject, TWorkItemBean workItemBean) {
		Map<Integer, List<Validator>> validatorsMap = super.getValidators(fieldID,fieldConfigBean,parameterCode,
				settingsObject, workItemBean);
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)settingsObject;
		if(textBoxSettingsBean==null){
			return validatorsMap;
		}
		if (parameterCode==null) {
			parameterCode = Integer.valueOf(0);
		}
		List<Validator> validatorsList = new ArrayList<Validator>();
		if(validatorsMap.get(parameterCode)!=null){
			validatorsList=validatorsMap.get(parameterCode);
		}
		StringValidator stringValidator=new StringValidator();
		Integer minLength = textBoxSettingsBean.getMinTextLength();
		Integer maxLength = textBoxSettingsBean.getMaxTextLength();
		stringValidator.setMinLength(minLength);
		stringValidator.setMaxLength(maxLength);
		validatorsList.add(stringValidator);
		validatorsMap.put(parameterCode, validatorsList);
		return  validatorsMap;
	}

	/**
	 * Creates the matcher object for configuring the matcher
	 * @param fieldID
	 */
	@Override
	public IMatcherDT getMatcherDT(Integer fieldID) {
		return new StringMatcherDT(fieldID);
	}

	/**
	 * Creates the  matcher object for executing the matcher
	 * @param fieldID
	 * @param relation
	 * @param matchValue
	 */
	@Override
	public IMatcherRT getMatcherRT(Integer fieldID, int relation, Object matchValue,
			MatcherContext matcherContext){
		return new StringMatcherRT(fieldID, relation, matchValue, matcherContext);
	}

	public boolean isHTMLFormat() {
		return false;
	}

}
