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


package com.aurel.track.fieldType.design.custom.date;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.fieldType.constants.DateOptions;
import com.aurel.track.fieldType.design.text.DateDT;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;

public class CustomDateDT extends DateDT {

	public CustomDateDT(String pluginID) {
		super(pluginID);		
	}

	public CustomDateDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);		
	}
	
	@Override
	protected List<IntegerStringBean> getOptions(Locale locale, String bundleName) {		
		List<IntegerStringBean> options = new ArrayList<IntegerStringBean>();
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText("customTextBoxDate.option.empty", locale, bundleName), new Integer(DateOptions.EMPTY)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText("customTextBoxDate.option.now", locale, bundleName), new Integer(DateOptions.NOW)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedText("customTextBoxDate.option.date", locale, bundleName), new Integer(DateOptions.DATE)));
		return options;
	}

	@Override
	protected List<IntegerStringBean> getDefaultOptions(Locale locale, String bundleName) {
		return getOptions(locale, bundleName);
	}
	
	@Override
	protected String getDefaultMaxDateErrorKey() {		
		return "customTextBoxDate.error.minMaxDate";
	}

	@Override
	protected String getDefaultMinDateErrorKey() {		
		return "customTextBoxDate.error.defaultMinDate";
	}

	@Override
	protected String getMinMaxDateErrorKey() {
		return "customTextBoxDate.error.defaultMaxDate";
	}
	
	
}
