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

package com.aurel.track.item;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.renderer.CompositeTypeRendererRT;
import com.aurel.track.fieldType.runtime.renderer.TypeConversionException;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.IntegerStringBean;

/**
 *
 */
public class ItemSaveBL {
	private static final Logger LOGGER = LogManager.getLogger(ItemSaveAction.class);
	public static interface ERROR_CODE{
		public final int GENERAL=0;
		public final int CONVERSION=1;
		public final int OUT_OF_DATE=2;
        public final int CONFIRMATION=3;
	}
	public static boolean isOutOfDate(Date lastModifiedDate,WorkItemContext workItemContext){
		Date originalLastModifiedDate=workItemContext.getWorkItemBean().getLastEdit();
		if(originalLastModifiedDate!=null&&lastModifiedDate!=null){
			if(lastModifiedDate.before(originalLastModifiedDate)){
				LOGGER.warn("Item already saved on "+DateTimeUtils.getInstance().formatISODateTime(originalLastModifiedDate)+"!");
				return true;
			}
		}
		return false;
	}
	/**
	 * Gets the fieldID from fieldKey f1 -> 1
	 * @param fieldKey
	 * @return
	 */
	private static Integer getFieldIDFromFieldKey(String fieldKey) {
		if (fieldKey!=null) {
			fieldKey=fieldKey.substring(1);//eliminate 'f' char
			int idx=fieldKey.indexOf("_");
			if(idx>0){
				fieldKey=fieldKey.substring(0,idx);
			}
			try {
				return Integer.parseInt(fieldKey);
			}catch (Exception ex){
			}
		}
		return null;
	}

	public static Map<Integer,Object> unwrapContextExclusiveFields(WorkItemContext ctx, Map<String,String> fieldValues,List<IntegerStringBean> errors, boolean isMobileApplication){
		Set<Integer> fields=new HashSet<Integer>();
		Iterator<String> it=fieldValues.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			Integer fieldID=getFieldIDFromFieldKey(key);
			if (fieldID!=null) {
				fields.add(fieldID);
			}
		}
		Locale locale=ctx.getLocale();
		Map<Integer,Object> result=new HashMap<Integer, Object>();
		for (Iterator<Integer> iterator = fields.iterator(); iterator.hasNext();) {
			Integer fieldID=iterator.next();
			unwrapFieldValue(ctx, fieldValues, errors, locale, result, fieldID);
		}
		return result;
	}
	public static Map<Integer,Object> unwrapContext(WorkItemContext ctx, Map<String,String> fieldValues,List<IntegerStringBean> errors, boolean isMobileApplication){
		Set<Integer> presentFields=ctx.getPresentFieldIDs();
		Set<Integer> requiredSystemFields= FieldsManagerRT.getRequiredSystemFieldsList();
		presentFields.add(SystemFields.INTEGER_SYNOPSIS);
		Locale locale=ctx.getLocale();
		Map<Integer,Object> result=new HashMap<Integer, Object>();
		Map<Integer, Integer> fieldRestrictions = AccessBeans.getRestrictedFieldsAndBottomUpDates(ctx);
		for (Iterator<Integer> iterator = presentFields.iterator(); iterator.hasNext();) {
			Integer fieldID=iterator.next();
			if(fieldID.intValue()== SystemFields.ISSUENO||
					fieldID.intValue()==SystemFields.LASTMODIFIEDDATE||
					fieldID.intValue()==SystemFields.CREATEDATE||
					fieldID.intValue()==SystemFields.ORIGINATOR||
					fieldID.intValue()==SystemFields.PROJECT||
					fieldID.intValue()==SystemFields.ISSUETYPE||
					fieldID.intValue()==SystemFields.WBS||
					(fieldRestrictions!=null && fieldRestrictions.containsKey(fieldID))){
				//not modifiable field
				continue;
			}
			//clear values in case is not send from form and not required system fields
			if(!requiredSystemFields.contains(fieldID)) {
				//because the null fields are not submitted from the form all not required fields from the form are preset to null
				//to allow removing values (set to null)
				//the non null values will be set anyway in unwrapFieldValue
				result.put(fieldID, null);
			}
			unwrapFieldValue(ctx, fieldValues, errors, locale, result, fieldID);
		}
		return result;
	}

	/**
	 * Unwrap the inline edited fields in item navigator
	 * @param ctx
	 * @param fieldValues
	 * @param errors
	 * @param isMobileApplication
	 * @return
	 */
	public static Map<Integer,Object> unwrapContextNavigatorInlineEdit(WorkItemContext ctx, Map<String,String> fieldValues, List<IntegerStringBean> errors){
		Locale locale=ctx.getLocale();
		Map<Integer,Object> result=new HashMap<Integer, Object>();
		Map<Integer, Integer> fieldRestrictions = AccessBeans.getRestrictedFieldsAndBottomUpDates(ctx);
		for (String fieldKeyString : fieldValues.keySet()) {
			Integer fieldID=getFieldIDFromFieldKey(fieldKeyString);
			if(fieldID.intValue()== SystemFields.ISSUENO||
					fieldID.intValue()==SystemFields.LASTMODIFIEDDATE||
					fieldID.intValue()==SystemFields.CREATEDATE||
					fieldID.intValue()==SystemFields.ORIGINATOR||
					fieldID.intValue()==SystemFields.PROJECT||
					fieldID.intValue()==SystemFields.ISSUETYPE||
					fieldID.intValue()==SystemFields.WBS||
					(fieldRestrictions!=null && fieldRestrictions.containsKey(fieldID))){
				//not modifiable field
				continue;
			}
			//set only the really submitted fields and leave the other unchanged
			unwrapFieldValue(ctx, fieldValues, errors, locale, result, fieldID);
		}
		return result;
	}

	private static void unwrapFieldValue(WorkItemContext ctx, Map<String, String> fieldValues, List<IntegerStringBean> errors, Locale locale, Map<Integer, Object> result, Integer fieldID) {
		FieldType fieldType= FieldTypeManager.getInstance().getType(fieldID);
		fieldType.setFieldID(fieldID);
		TypeRendererRT fieldTypeRendererRT= fieldType.getRendererRT();
		if(fieldType.getFieldTypeRT().isComposite()){
			CompositeTypeRendererRT compositeTypeRendererRT= (CompositeTypeRendererRT)fieldTypeRendererRT;
			int parts=compositeTypeRendererRT.getNumberOfParts();
			Map<Integer, Object> compositeValueMap=new HashMap<Integer, Object>();
			for(int i=1;i<=parts;i++){
				TypeRendererRT partTypeRendererRT=compositeTypeRendererRT.getCustomTypeRenderer(i);
				String jsonValue=fieldValues.get("f"+fieldID+"_"+i);
				if(jsonValue!=null&&jsonValue.length()>0){
					Object value=null;
					try{
						value=partTypeRendererRT.decodeJsonValue(jsonValue, fieldID, ctx);
						compositeValueMap.put(i,value);
					}catch(TypeConversionException ex){
						TFieldConfigBean fieldConfigBean=(TFieldConfigBean)ctx.getFieldConfigs().get(fieldID);
						fieldConfigBean= LocalizeUtil.localizeFieldConfig(fieldConfigBean, locale);
						String localizedLabel=localizeError(ex.getMessage(),fieldConfigBean.getLabel(),locale);
						errors.add(new IntegerStringBean(localizedLabel,fieldID));
					}
				}
			}
			if(!compositeValueMap.isEmpty()){
				result.put(fieldID,compositeValueMap);
			}else{
				result.put(fieldID, null);
			}
		}else{
			if(fieldValues.containsKey("f"+fieldID)){
				String jsonValue=fieldValues.get("f"+fieldID);
				Object value=null;
				if(jsonValue!=null&&jsonValue.length()>0){
					try{
						value=fieldTypeRendererRT.decodeJsonValue(jsonValue, fieldID,ctx);
					}catch(TypeConversionException ex){
						TFieldConfigBean fieldConfigBean=(TFieldConfigBean)ctx.getFieldConfigs().get(fieldID);
						fieldConfigBean= LocalizeUtil.localizeFieldConfig(fieldConfigBean, locale);
						String localizedLabel=localizeError(ex.getMessage(),fieldConfigBean.getLabel(),locale);
						errors.add(new IntegerStringBean(localizedLabel,fieldID));
					}
				}
				result.put(fieldID,value);
			}
		}
	}

	public static void updateCtx(WorkItemContext ctx, Map<Integer,Object> fieldValues){
		for (Map.Entry<Integer,Object> entry:fieldValues.entrySet()) {
			Integer fieldID=entry.getKey();
			Object value=entry.getValue();
			ctx.getWorkItemBean().setAttribute(fieldID,value);
		}
	}

	private static String localizeError(String key, String fieldLabel,Locale locale){
		return LocalizeUtil.getParametrizedString(key,new String[]{fieldLabel},locale);
	}
}
