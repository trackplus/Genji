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

package com.aurel.track.exchange.track;

import java.util.HashMap;
import java.util.Map;

/**
 * Exchange history information for a field
 * @author Tamas
 *
 */
public class ExchangeHistoryFieldEntry {
	private String fieldID;
	private String parameterCode;
	private String timesEdited;
	private String oldStringValue;
	private String newStringValue;
	
	public ExchangeHistoryFieldEntry() {
		super();
	}
	
	public String getFieldID() {
		return fieldID;
	}

	public void setFieldID(String fieldID) {
		this.fieldID = fieldID;
	}

	public String getParameterCode() {
		return parameterCode;
	}

	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}

	public String getOldStringValue() {
		return oldStringValue;
	}

	public void setOldStringValue(String oldStringValue) {
		this.oldStringValue = oldStringValue;
	}

	public String getNewStringValue() {
		return newStringValue;
	}

	public void setNewStringValue(String newStringValue) {
		this.newStringValue = newStringValue;
	}
		
	public String getTimesEdited() {
		return timesEdited;
	}

	public void setTimesEdited(String timesEdited) {
		this.timesEdited = timesEdited;
	}

	/**
	 * Serialize a label bean to a dom element
	 * @return
	 */
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();		
		attributesMap.put(ExchangeFieldNames.FIELDID, getFieldID());
		if (getParameterCode()!=null) {
			attributesMap.put(ExchangeFieldNames.PARAMETERCODE, getParameterCode());
		}
		if (getTimesEdited()!=null) {
			attributesMap.put(ExchangeFieldNames.TIMES_EDITED, getTimesEdited());
		}
		return attributesMap;
	}
	
	/**
	 * Deserialze the labelBean 
	 * @param attributes
	 * @return
	 */
	public ExchangeHistoryFieldEntry deserializeBean(Map<String, String> attributes) {
		ExchangeHistoryFieldEntry exchangeHistoryFieldEntry = new ExchangeHistoryFieldEntry();				
		exchangeHistoryFieldEntry.setFieldID(attributes.get(ExchangeFieldNames.FIELDID));
		String parameterCode = attributes.get(ExchangeFieldNames.PARAMETERCODE);
		if (parameterCode!=null) {
			exchangeHistoryFieldEntry.setParameterCode(parameterCode);
		}
		String timesEdited = attributes.get(ExchangeFieldNames.TIMES_EDITED);
		if (timesEdited!=null) {
			exchangeHistoryFieldEntry.setTimesEdited(timesEdited);
		}
		return exchangeHistoryFieldEntry;
	}
	
}
