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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.validators.EmailValidator;
import com.aurel.track.fieldType.runtime.validators.Validator;
import com.aurel.track.lucene.LuceneUtil;

/**
 * Runtime class for IssueNo
 * @author Tamas Ruff
 *
 */
public class SystemSubmitterEmailRT  extends SystemShortTextRT {

	/**
	 * Get the specific attribute from TTextBoxSettingsBean for default value
	 */
	@Override
	public Object getSpecificDefaultAttribute(TTextBoxSettingsBean textBoxSettingsBean) {
		return null;
	}
	
	/**
	 * Configure and add the e-mail validator
	 */
	@Override
	public Map<Integer, List<Validator>> getValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean,Integer parameterCode,
			Object settingsObject, TWorkItemBean workItemBean) {
		Map<Integer, List<Validator>> validatorsMap = super.getValidators(fieldID,fieldConfigBean,parameterCode,
				settingsObject, workItemBean); 
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)settingsObject;			
		
		List<Validator> validatorsList = new ArrayList<Validator>();
		if(validatorsMap.get(Integer.valueOf(0))!=null){
			validatorsList=validatorsMap.get(Integer.valueOf(0));
		}
		validatorsMap.put(Integer.valueOf(0), validatorsList);
		EmailValidator emailValidator = new EmailValidator();
		String patternString = null;
		if(textBoxSettingsBean!=null){
			patternString = textBoxSettingsBean.getDefaultText();
		}
		emailValidator.setPatternString(patternString);
		validatorsList.add(emailValidator);
		return validatorsMap;
	}
	
	/**
	 * Whether the value of this field can be changed
	 * @return
	 */
	/*public boolean isReadOnly() {
		return true;
	}*/
	
	
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
	
}
