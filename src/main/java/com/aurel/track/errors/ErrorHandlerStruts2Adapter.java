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

package com.aurel.track.errors;

import java.util.LinkedList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Handling error data to struts2 actions
 */
public class ErrorHandlerStruts2Adapter {
	//error handling
	
	
	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters 
	 * Specifically used for getting the 
	 * @param errorData
	 * @param fieldConfigs
	 * @param action
	 * @return
	 */
	
	/**
	 * Injects the errors in the action using the ErrorData object from the list
	 * @param errorList
	 * @param action
	 */
	
	
	
	/**
	 * Injects the errors in the action using the ErrorData object from the list
	 * @param errorList
	 * @param action
	 */
	
	
	/**
	 * Injects the errors in the action using the ErrorData object from the list
	 * @param errorData
	 * @param action
	 */
	public static void handleErrorData(ErrorData errorData, ActionSupport action){
		String fieldName=errorData.getFieldName();
		if (fieldName==null) {
			action.addActionError(createMessage(errorData, action));
		} else {			   
			action.addFieldError(fieldName,createMessage(errorData, action));
		}
		
	}
		
	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters  
	 * @param errorData
	 * @param action
	 * @return
	 */
	private static String createMessage(ErrorData errorData, ActionSupport action){
		String key=errorData.getResourceKey();
		List<Object> params=new LinkedList<Object>();
		if(errorData.getResourceParameters()!=null) {
			for (ErrorParameter parameter : errorData.getResourceParameters()) {
				if(parameter.isResource()){
					params.add(action.getText(parameter.getParameterValue()+""));
				}else{
					params.add(parameter.getParameterValue());
				}
			}
		}
		return action.getText(key, params);
	}
	
	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters 
	 * Specifically used for getting the 
	 * @param errorData
	 * @param fieldConfigs
	 * @param locale
	 * @return
	 */
	
	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters  
	 * @param errorData
	 * @param locale
	 * @return
	 */
	
	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters  
	 * @param errorDataList
	 * @param locale
	 * @return
	 */
	
	
	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters
	 * directly from a ResourceBundle (not using the ActionSupport.getText())
	 * Used for plugin resources which can't be accessed through action   
	 * @param errorData
	 * @param locale
     * @param bundleName
	 * @return
	 */
}
