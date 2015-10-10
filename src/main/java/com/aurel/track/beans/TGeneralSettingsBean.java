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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TGeneralSettingsBean
    extends com.aurel.track.beans.base.BaseTGeneralSettingsBean
    implements Serializable, ISerializableLabelBean
{	
	private static final long serialVersionUID = 1L;
	
	public String getLabel() {	
		return null;
	}
	
	public boolean isCharacterValueString() {
		if (BooleanFields.TRUE_VALUE.equals(getCharacterValue())) {
			return true;
		}
		return false;
	}
	
	public void setCharacterValueString(boolean deprecated) {
		if (deprecated) {
			setCharacterValue(BooleanFields.TRUE_VALUE);
		} else {
			setCharacterValue(BooleanFields.FALSE_VALUE);
		}
	}
	
	/**
	 * Serialize a label bean to a dom element
	 * @param labelBean
	 * @return
	 */
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());		
		attributesMap.put("config", getConfig().toString());		
		Integer integerValue = getIntegerValue();
		if (integerValue!=null) {
			attributesMap.put("integerValue", integerValue.toString());		
		}
		Double doubleValue = getDoubleValue();
		if (doubleValue!=null) {
			attributesMap.put("doubleValue", DoubleNumberFormatUtil.formatISO(doubleValue));
		}
		String textValue = getTextValue();
		if (textValue!=null) {
			attributesMap.put("textValue", textValue);
		}
		Date dateValue = getDateValue();
		if (dateValue!=null) {
			attributesMap.put("dateValue", DateTimeUtils.getInstance().formatISODateTime(dateValue));
		}
		String characterValue = getCharacterValue();
		if (characterValue!=null) {
			attributesMap.put("characterValue", characterValue);
		}
		Integer parameterCode = getParameterCode();
		if (parameterCode!=null) {
			attributesMap.put("parameterCode", parameterCode.toString());		
		}
		Integer validValue = getValidValue();
		if (validValue!=null) {
			attributesMap.put("validValue", validValue.toString());		
		}		
		attributesMap.put("uuid", getUuid());		
		return attributesMap;			
		
	}
	
	/**
	 * Deserialize the labelBean 
	 * @param attributes
	 * @return
	 */
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TGeneralSettingsBean generalSettingsBean = new TGeneralSettingsBean();			
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			generalSettingsBean.setObjectID(new Integer(strObjectID));
		}		
		String strConfig = attributes.get("config");
		if (strConfig!=null) {
			generalSettingsBean.setConfig(new Integer(strConfig));
		}
		
		String strIntegerValue = attributes.get("integerValue");
		if (strIntegerValue!=null) {
			generalSettingsBean.setIntegerValue(new Integer(strIntegerValue));
					
		}
		String strDoubleValue = attributes.get("doubleValue");
		if (strDoubleValue!=null) {
			generalSettingsBean.setDoubleValue(DoubleNumberFormatUtil.parseISO(strDoubleValue));
		}
		generalSettingsBean.setTextValue(attributes.get("textValue"));		
		String strDateValue = attributes.get("dateValue");
		if (strDateValue!=null) {
			generalSettingsBean.setDateValue(DateTimeUtils.getInstance().parseISODate(strDateValue));
		}
		generalSettingsBean.setCharacterValue(attributes.get("characterValue"));
	
		String strParameterCode = attributes.get("parameterCode");
		if (strParameterCode!=null) {
			generalSettingsBean.setParameterCode(new Integer(strParameterCode));
		}
		String validValue = attributes.get("validValue");
		if (validValue!=null) {
			generalSettingsBean.setValidValue(new Integer(validValue));
		}		
		generalSettingsBean.setUuid(attributes.get("uuid"));
		return generalSettingsBean;
	}
	
	
	/**
	 * Whether two label beans are equivalent  
	 * @param serializableLabelBean
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}		
		TGeneralSettingsBean generalSettingsBean = (TGeneralSettingsBean)serializableLabelBean;				
		if (getUuid() != null && generalSettingsBean.getUuid()!=null)
			if (getUuid().equals(generalSettingsBean.getUuid()))
				return true;	
		Integer externalConfig = getConfig();
		Integer internalConfig = generalSettingsBean.getConfig();
		Map<Integer, Integer> fieldConfigMatches = matchesMap.get(ExchangeFieldNames.FIELDCONFIG);
		if(fieldConfigMatches==null||fieldConfigMatches.get(externalConfig)==null){
			return  externalConfig.equals(internalConfig);
		}
		//matches if the config matches 		
		if (externalConfig!=null && internalConfig!=null && fieldConfigMatches.get(externalConfig)!=null) {
			return fieldConfigMatches.get(externalConfig).equals(internalConfig);
		}		
		return false;
	}
	
	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {		
		TGeneralSettingsBean generalSettingsBean = (TGeneralSettingsBean)serializableLabelBean;		
		Integer externalConfigID = generalSettingsBean.getConfig();
		if (externalConfigID!=null) {
			Map<Integer, Integer> fieldConfigsMap = 
				matchesMap.get(ExchangeFieldNames.FIELDCONFIG);
			generalSettingsBean.setConfig(fieldConfigsMap.get(externalConfigID));
		}
		return GeneralSettingsBL.save(generalSettingsBean);
	}
}
