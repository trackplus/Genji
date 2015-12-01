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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;

/**
 * Transform error messages to localized strings to be sent as JSON
 * @author Tamas
 *
 */
public class ErrorHandlerJSONAdapter {

	/**
	 * Gets a list of error strings from ErrorData list
	 * @param errorList
	 * @param locale
	 */
	public static List<String> handleErrorList(List<ErrorData> errorList, Locale locale){
		List<String> errorMessages = new LinkedList<String>();
		if (errorList!=null) {
			for (ErrorData errorData : errorList) {
				errorMessages.add(createMessage(errorData, locale));
			}
		}
		return errorMessages;
	}

	/**
	 * Gets the concatenated error strings lists from ErrorData list
	 * @param errorList
	 * @param locale
	 */
	public static String handleErrorListAsString(List<ErrorData> errorList, Locale locale, String messageSeparator) {
		StringBuilder errorMessages = new StringBuilder();
		Iterator<ErrorData> iterator=errorList.iterator();
		while(iterator.hasNext()){
			ErrorData errorData=iterator.next();
			errorMessages.append(createMessage(errorData, locale));
			if (iterator.hasNext()) {
				errorMessages.append(messageSeparator);
			}	
		}
		return errorMessages.toString();
	}

	/**
	 * Gets the list of IntegerStringBeans from ErrorData List
	 * @param errorList
	 * @param confirmation
	 * @param locale
	 * @return
	 */
	public static List<IntegerStringBean> handleErrorListAsIntegerStringBean(List<ErrorData> errorList, /*boolean confirmation,*/ Locale locale){
		List<IntegerStringBean> errorMessages = new LinkedList<IntegerStringBean>();
		if (errorList!=null) {
			for (ErrorData errorData : errorList) {
				errorMessages.add(new IntegerStringBean(createMessage(errorData, /*confirmation,*/ locale),errorData.getFieldID()));
			}
		}
		return errorMessages;
	}


	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters  
	 * @param errorData
	 * @param confirmation
	 * @param locale
	 * @return
	 */
	public static String createMessage(ErrorData errorData, Locale locale){
		String key = errorData.getResourceKey();
		if (key!=null) {
			List<Object> params=new LinkedList<Object>();
			if (errorData.getResourceParameters()!=null) {
				for (ErrorParameter parameter : errorData.getResourceParameters()) {
					if (parameter.isResource()) {
						params.add(LocalizeUtil.getLocalizedTextFromApplicationResources(parameter.getParameterValue()+"", locale));
					} else {
						params.add(parameter.getParameterValue());
					}
				}
				return LocalizeUtil.getParametrizedString(key, params.toArray(), locale);
			} else {
				return LocalizeUtil.getLocalizedTextFromApplicationResources(key,  locale);
			}
		}
		return "";
	}
	
	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters 
	 * Specifically used for getting the 
	 * @param errorData
	 * @param fieldConfigs
	 * @param locale
	 * @return
	 */
	public static String createFieldMessage(ErrorData errorData, Map<Integer, TFieldConfigBean> fieldConfigs, Locale locale){
		String key=errorData.getResourceKey();
		List<Object> params=new LinkedList<Object>();
		String fieldLabel="";		
		if (errorData.getFieldID()!=null && fieldConfigs!=null) {
			TFieldConfigBean fieldConfigBean=fieldConfigs.get(errorData.getFieldID());
			if (fieldConfigBean!=null) {
				fieldLabel=fieldConfigBean.getLabel();
			}
			params.add(fieldLabel);
		}		
		if(errorData.getResourceParameters()!=null){
			for (Iterator<ErrorParameter> iter = errorData.getResourceParameters().iterator(); iter.hasNext();) {
				ErrorParameter parameter = iter.next();
				if(parameter.isResource()){
					params.add(LocalizeUtil.getLocalizedTextFromApplicationResources(parameter.getParameterValue()+"", locale));
				} else {
					params.add(parameter.getParameterValue());
				}
			}
		}
		return LocalizeUtil.getParametrizedString(key, params.toArray(), locale);
	}
}
