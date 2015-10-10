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

package com.aurel.track.errors;

import java.util.LinkedList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Handling error data to struts2 actions
 */
public class ErrorHandlerStruts2Adapter {
	//error handling
	/*public static void handleErrorList(List<ErrorData> errorList, Map<Integer, TFieldConfigBean> fieldConfigs, ActionSupport action){
		Iterator<ErrorData> it=errorList.iterator();
		String prefixMapping="session.workItemContext.workItemBean";
		while(it.hasNext()){
			ErrorData errorData=it.next();
			Integer fieldID=errorData.getFieldID();
			if (fieldID==null) {
				action.addActionError(createFieldMessage(errorData, fieldConfigs, action));
			} else {
				String nameMapping=prefixMapping+"."+ TWorkItemBean.getPropertyName(fieldID);
				action.addFieldError(nameMapping,createFieldMessage(errorData, fieldConfigs, action));
			}
		}
	}*/
	
	/*public static String getErrorList(List<ErrorData> errorList, Map<Integer, TFieldConfigBean> fieldConfigs, ActionSupport action){
		Iterator<ErrorData> it=errorList.iterator();
		StringBuffer stringBuffer = new StringBuffer();
		while(it.hasNext()){
			ErrorData errorData = it.next();			
			stringBuffer.append(createFieldMessage(errorData, fieldConfigs, action));
			if (it.hasNext()) {
				stringBuffer.append(" ");
			}
		}
		return stringBuffer.toString();
	}*/
	
	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters 
	 * Specifically used for getting the 
	 * @param errorData
	 * @param fieldConfigs
	 * @param action
	 * @return
	 */
	/*public static String createFieldMessage(ErrorData errorData, Map<Integer, TFieldConfigBean> fieldConfigs, ActionSupport action){
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
					params.add(action.getText(parameter.getParameterValue()+""));
				}else{
					params.add(parameter.getParameterValue());
				}
			}
		}
		return action.getText(key, params);		
	}*/
	
	/**
	 * Injects the errors in the action using the ErrorData object from the list
	 * @param errorList
	 * @param action
	 */
	/*public static void handleErrorList(List<ErrorData> errorList, ActionSupport action){
		Iterator<ErrorData> iterator=errorList.iterator();		
		while(iterator.hasNext()){
			ErrorData errorData=iterator.next();
			String fieldName=errorData.getFieldName();
			if (fieldName==null) {
				action.addActionError(createMessage(errorData, action));
			} else {			   
				action.addFieldError(fieldName,createMessage(errorData, action));
			}
		}
	}*/
	
	
	
	/**
	 * Injects the errors in the action using the ErrorData object from the list
	 * @param errorList
	 * @param action
	 */
	/*public static void handleErrorList(List<ErrorData> errorList, ActionSupport action, Locale locale, String bundleName){
		Iterator<ErrorData> iterator=errorList.iterator();
		ResourceBundle resourceBundle = ResourceBundleManager.getInstance().getResourceBundle(bundleName, locale);
		while(iterator.hasNext()){
			ErrorData errorData=iterator.next();
			String fieldName=errorData.getFieldName();
			if (fieldName==null) {
				action.addActionError(createMessageWithoutAction(errorData, locale, resourceBundle));
			} else {			   
				action.addFieldError(fieldName, createMessageWithoutAction(errorData, locale, resourceBundle));
			}
		}
	}*/
	
	
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
	/*public static String createApplicationResourcesFieldMessage(ErrorData errorData, Map<Integer, TFieldConfigBean> fieldConfigs, Locale locale){
		String key=errorData.getResourceKey();
		List<Object> params=new LinkedList<Object>();
		//String fieldLabel="";		
		if(errorData.getResourceParameters()!=null){
			for (Iterator<ErrorParameter> iter = errorData.getResourceParameters().iterator(); iter.hasNext();) {
				ErrorParameter parameter = iter.next();
				if(parameter.isResource()){
					params.add(LocalizeUtil.getLocalizedTextFromApplicationResources(parameter.getParameterValue()+"", locale));
				}else{
					params.add(parameter.getParameterValue());
				}
			}			
		}
		return LocalizeUtil.getParametrizedString(key, params.toArray(), locale);		
	}*/
	
	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters  
	 * @param errorData
	 * @param locale
	 * @return
	 */
	/*public static String createApplicationResourcesMessage(ErrorData errorData, Locale locale){
		String key = errorData.getResourceKey();
		List<Object> params=new LinkedList<Object>();
		if (errorData.getResourceParameters()!=null) {
			for (ErrorParameter parameter : errorData.getResourceParameters()) {
				if(parameter.isResource()){
					params.add(LocalizeUtil.getLocalizedTextFromApplicationResources(parameter.getParameterValue()+"", locale));
				}else{
					params.add(parameter.getParameterValue());
				}
			}
			return LocalizeUtil.getParametrizedString(key, params.toArray(), locale);
		}
		return LocalizeUtil.getLocalizedTextFromApplicationResources(key, locale);
	}*/
	
	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters  
	 * @param errorDataList
	 * @param locale
	 * @return
	 */
	/*public static String createApplicationResourcesMessages(List<ErrorData> errorDataList, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		if (errorDataList!=null) {
			for (Iterator<ErrorData> iterator = errorDataList.iterator(); iterator.hasNext();) {
				ErrorData errorData = iterator.next();
				String errorMessage = createApplicationResourcesMessage(errorData, locale);
				stringBuilder.append(errorMessage);
				if (iterator.hasNext()) {
					stringBuilder.append(", ");
				}
			}
		}
		return stringBuilder.toString();
	}*/
	
	
	/**
	 * Gets a localized message with no- or more (localized or not localized) parameters
	 * directly from a ResourceBundle (not using the ActionSupport.getText())
	 * Used for plugin resources which can't be accessed through action   
	 * @param errorData
	 * @param locale
     * @param bundleName
	 * @return
	 */
	/*private static String createMessageWithoutAction(ErrorData errorData, Locale locale, ResourceBundle bundleName){
		List<Object> params=new LinkedList<Object>();
		if(errorData.getResourceParameters()!=null){
			for (Iterator<ErrorParameter> iter = errorData.getResourceParameters().iterator(); iter.hasNext();) {
				ErrorParameter parameter = iter.next();
				if(parameter.isResource()){
					params.add(LocalizedTextUtil.findText(bundleName, parameter.getParameterValue()+"", locale));
				}else{
					params.add(parameter.getParameterValue());
				}
			}
		}
		return LocalizedTextUtil.findText(bundleName, errorData.getResourceKey(), locale, "", params.toArray());
	}*/
}
