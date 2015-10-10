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


package com.aurel.track.exchange;

import java.util.List;
import java.util.Locale;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;


/**
 * Business logic class for notification settings
 * @author Tamas Ruff
 *
 */
public class ImportJSON {
	
	public static interface JSON_FIELDS {
		static final String DISABLE_FINAL = "disableFinal";
	}
		
	public interface ERROR_CODES {
		//depending on errorCode the rendering on client side differs  
		static public Integer ERROR_MESSAGE = Integer.valueOf(1);
		static public Integer ERROR_MESSAGES = Integer.valueOf(2);
		static public Integer GRID_AND_ROW_ERRORS  = Integer.valueOf(3);
		static public Integer CONFLICTS = Integer.valueOf(4);
	}

	/**
	 * Gets the json string for import failure
	 * @param errorCode how to interpret the errorMessage field
     * @param errorMessage
	 * @param disableFinish whether to disable the finish button after this error
	 * @return
	 */
	public static String importErrorMessageJSON(Integer errorCode, String errorMessage, boolean disableFinish){
		StringBuilder sb=new StringBuilder();
		sb.append("{");		
		if(errorCode!=null){
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ERROR_CODE, errorCode);
		}		
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DISABLE_FINAL, disableFinish);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the json string for import failure
	 * @param errorMessages
	 * @param errorCode how to interpret the errorMessage field
	 * @param disableFinish whether to disable the finish button after this error 
	 * @return
	 */
	public static String importErrorMessageListJSON(List<String> errorMessages, Integer errorCode, boolean disableFinish){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(errorCode!=null){
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ERROR_CODE, errorCode);
		}
		if(errorMessages!=null){
			JSONUtility.appendStringList(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessages);
		}
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DISABLE_FINAL, disableFinish);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the json string for import failure
     * @param success
	 * @param message the result message
	 * @param disableFinal whether to disable the finish button after this error
     * @param locale
	 * @return
	 */
	public static String importMessageJSON(boolean success, String message, boolean disableFinal, Locale locale){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.DISABLE_FINAL, disableFinal);
		JSONUtility.appendStringValue(sb,  JSONUtility.JSON_FIELDS.TITLE,
				LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.importResult", locale));
		JSONUtility.appendStringValue(sb,  JSONUtility.JSON_FIELDS.MESSAGE, message);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success, true);
		sb.append("}");
		return sb.toString();
	}
}
