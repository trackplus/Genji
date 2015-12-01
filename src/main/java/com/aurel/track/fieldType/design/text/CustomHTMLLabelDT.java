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


package com.aurel.track.fieldType.design.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.errors.ErrorData;

/**
 * Base class for defining the design time behavior 
 * for custom single text box field types 
 * @author Tamas Ruff
 *
 */
public class CustomHTMLLabelDT extends TextDT {

	public CustomHTMLLabelDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);		
	}

	public CustomHTMLLabelDT(String pluginID) {
		super(pluginID);
	}
	
	@Override
	public List<ErrorData> isValidSettings(Map<Integer, Object> settings) {		
		List<ErrorData> errorDataList = new ArrayList<ErrorData>();
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)settings.get(mapParameterCode);		
		if (textBoxSettingsBean!=null) {
			String defaultText = textBoxSettingsBean.getDefaultText();
			if (defaultText!=null && defaultText.length()>255) {
				errorDataList.add(new ErrorData("customHTMLLabel.error.maxLength"));
			}			
		}
		return errorDataList;
	}

	
	/**
	 * Whether by configuring a system or custom field 
	 * the required checkbox should appear or not.
	 * It should not appear for the required system fields 
	 * and for the types where this doesn't makes sense: ex. lables, checkboxes
	 * Overwrite it to return false for such fields 
	 * @return
	 */
	@Override
	public boolean renderRequiredFlag() {
		return false;
	}
	
	/**
	 * No history for label
	 */
	@Override
	public boolean renderHistoryFlag() {
		return false;
	}
}
