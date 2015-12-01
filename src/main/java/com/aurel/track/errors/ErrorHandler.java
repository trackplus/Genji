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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.resources.ResourceBundleManager;


public class ErrorHandler {
	 
	/**
     * Injects the errors in Map<fieldID,List<errorMessage>> using the ErrorData object from the list
     * @param errorList
     * @param action
     * @param locale
     * 
     * @return
     */
    public static Map handleErrorList(List errorList,Map fieldConfigs, Locale locale){
    	return handleErrorList(errorList, fieldConfigs, locale,ResourceBundleManager.DATABASE_RESOURCES);
    }
	 
	/**
     * Injects the errors in Map<fieldID,List<errorMessage>> using the ErrorData object from the list
     * @param errorList
     * @param action
     * @param locale
     * @param bundleName
     * @return
     */
    public static Map handleErrorList(List errorList, Map fieldConfigs, Locale locale, String bundleName){
        Map map=new HashMap();
    	Iterator iterator=errorList.iterator();
        //ResourceBundle resourceBundle = ResourceBundleManager.getInstance().getResourceBundle(bundleName, locale);
        while(iterator.hasNext()){
            ErrorData errorData=(ErrorData)iterator.next();
            String fieldName=errorData.getFieldName();
            List messages=(List) map.get(fieldName);
            if(messages==null){
            	messages=new ArrayList();
            }
           	messages.add(createMessageWithoutAction(errorData,fieldConfigs, locale, bundleName));
           	map.put(fieldName, messages);
        }
        return map;
    }
    /**
     * Gets a localized message with no- or more (localized or not localized) parameters
     * directly from a ResourceBundle (not using the ActionSupport.getText())
     * Used for plugin resources which can't be accessed through action   
     * @param errorData
     * @param action
     * @return
     */
    protected static String createMessageWithoutAction(ErrorData errorData, Map fieldConfigs, Locale locale, String bundleName){		
		List params=new ArrayList();	
		if (errorData.getFieldID()!=null) {
			String fieldLabel="";
			TFieldConfigBean fieldConfigBean=(TFieldConfigBean)fieldConfigs.get(errorData.getFieldID());
			if (fieldConfigBean!=null) {
				fieldLabel=fieldConfigBean.getLabel();
			}
			params.add(fieldLabel);
		}
		if(errorData.getResourceParameters()!=null){
			for (Iterator iter = errorData.getResourceParameters().iterator(); iter.hasNext();) {
				ErrorParameter parameter = (ErrorParameter) iter.next();
				if(parameter.isResource()){
					params.add(LocalizeUtil.getLocalizedText(parameter.getParameterValue()+"", locale, bundleName));// ResourceBundleManager.getInstance().translate(resourceBundle,parameter.getParameterValue()+""));
				}else{
					params.add(parameter.getParameterValue());
				}
			}
		}
		//return ResourceBundleManager.getInstance().translate(resourceBundle, errorData.getResourceKey(), params.toArray(new Object[0]));
		return LocalizeUtil.getLocalizedTextWithParams(errorData.getResourceKey(), locale, bundleName, params.toArray());
	}
        
}
