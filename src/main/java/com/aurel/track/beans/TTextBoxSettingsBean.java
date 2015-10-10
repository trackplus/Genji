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

import com.aurel.track.admin.customize.treeConfig.field.TextBoxSettingsBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TTextBoxSettingsBean
    extends com.aurel.track.beans.base.BaseTTextBoxSettingsBean
    implements Serializable, ISerializableLabelBean
{
	private static final long serialVersionUID = 1L;
	
	public TTextBoxSettingsBean() {
		super();
	}
	
	public String getLabel() {
		return null;
	}

	public boolean isDefaultCharString() {
		if (BooleanFields.TRUE_VALUE.equals(getDefaultChar())) {
			return true;
		}
		return false;
	}
	
	public void setDefaultCharString(boolean deprecated) {
		if (deprecated) {
			setDefaultChar(BooleanFields.TRUE_VALUE);
		} else {
			setDefaultChar(BooleanFields.FALSE_VALUE);
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
		attributesMap.put("required", getRequired());
		String defaultText = getDefaultText();
		if (defaultText!=null && !"".equals(defaultText)) {
			attributesMap.put("defaultText", getDefaultText());
		}
		Integer defaultInteger = getDefaultInteger();
		if (defaultInteger!=null) {
			attributesMap.put("defaultInteger", defaultInteger.toString());
		}
		Double defaultDouble = getDefaultDouble();
		if (defaultDouble!=null) {
			attributesMap.put("defaultDouble", DoubleNumberFormatUtil.formatISO(defaultDouble));
		}
		Date defaultDate = getDefaultDate();
		if (defaultDate!=null) {
			attributesMap.put("defaultDate", DateTimeUtils.getInstance().formatISODateTime(defaultDate));
		}
		String defaultChar = getDefaultChar();
		if (defaultChar!=null && !"".equals(defaultChar)) {
			attributesMap.put("defaultChar", getDefaultChar());
		}		
		Integer defaultOption = getDefaultOption();
		if (defaultOption!=null) {
			attributesMap.put("defaultOption", defaultOption.toString());
		}
		Integer minOption = getMinOption();
		if (minOption!=null) {
			attributesMap.put("minOption", minOption.toString());
		}
		Integer maxOption = getMaxOption();
		if (maxOption!=null) {
			attributesMap.put("maxOption", maxOption.toString());
		}		
		Integer minTextLength = getMinTextLength();
		if (minTextLength!=null) {
			attributesMap.put("minTextLength", minTextLength.toString());
		}
		Integer maxTextLength = getMaxTextLength();
		if (maxTextLength!=null) {
			attributesMap.put("maxTextLength", maxTextLength.toString());
		}		
		Date minDate = getMinDate();
		if (minDate!=null) {
			attributesMap.put("minDate", DateTimeUtils.getInstance().formatISODateTime(minDate));
		}
		Date maxDate = getMaxDate();
		if (maxDate!=null) {
			attributesMap.put("maxDate", DateTimeUtils.getInstance().formatISODateTime(maxDate));
		}		
		Integer minInteger = getMinInteger();
		if (minInteger!=null) {
			attributesMap.put("minInteger", minInteger.toString());
		}
		Integer maxInteger = getMaxInteger();
		if (maxInteger!=null) {
			attributesMap.put("maxInteger", maxInteger.toString());
		}
		Double minDouble = getMinDouble();
		if (minDouble!=null) {
			attributesMap.put("minDouble", DoubleNumberFormatUtil.formatISO(minDouble));
		}
		Double maxDouble = getMaxDouble();
		if (maxDouble!=null) {
			attributesMap.put("maxDouble", DoubleNumberFormatUtil.formatISO(maxDouble));
		}
		Integer maxDecimalDigit = getMaxDecimalDigit();
		if (maxDecimalDigit!=null) {
			attributesMap.put("maxDecimalDigit", maxDecimalDigit.toString());
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
		TTextBoxSettingsBean textBoxSettingsBean = new TTextBoxSettingsBean();				
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			textBoxSettingsBean.setObjectID(new Integer(strObjectID));
		}		
		String strConfig = attributes.get("config");
		if (strConfig!=null) {
			textBoxSettingsBean.setConfig(new Integer(strConfig));
		}
		textBoxSettingsBean.setRequired(attributes.get("required"));
		textBoxSettingsBean.setDefaultText(attributes.get("defaultText"));
		String strDefaultInteger = attributes.get("defaultInteger");
		if (strDefaultInteger!=null) {
			textBoxSettingsBean.setDefaultInteger(new Integer(strDefaultInteger));
		}
		String strDefaultDouble = attributes.get("defaultDouble");
		if (strDefaultDouble!=null) {
			textBoxSettingsBean.setDefaultDouble(DoubleNumberFormatUtil.parseISO(strDefaultDouble));
		}
		String strDefaultDate = attributes.get("defaultDate");
		if (strDefaultDate!=null) {
			textBoxSettingsBean.setDefaultDate(DateTimeUtils.getInstance().parseISODate(strDefaultDate));
		}
		textBoxSettingsBean.setDefaultChar(attributes.get("defaultChar"));
		String strDefaultOption = attributes.get("defaultOption");
		if (strDefaultOption!=null) {
			textBoxSettingsBean.setDefaultOption(new Integer(strDefaultOption));
		}
		String strMinOption = attributes.get("minOption");
		if (strMinOption!=null) {
			textBoxSettingsBean.setMinOption(new Integer(strMinOption));
		}		
		String strMaxOption = attributes.get("maxOption");
		if (strMaxOption!=null) {
			textBoxSettingsBean.setMaxOption(new Integer(strMaxOption));
		}
		String strMinTextLength = attributes.get("minTextLength");
		if (strMinTextLength!=null) {
			textBoxSettingsBean.setMinTextLength(new Integer(strMinTextLength));
		}		
		String strMaxTextLength = attributes.get("maxTextLength");
		if (strMaxTextLength!=null) {
			textBoxSettingsBean.setMaxTextLength(new Integer(strMaxTextLength));
		}
		String strMinDate = attributes.get("minDate");
		if (strMinDate!=null) {
			textBoxSettingsBean.setMinDate(DateTimeUtils.getInstance().parseISODate(strMinDate.toString()));
		}
		String strMaxDate = attributes.get("maxDate");
		if (strMaxDate!=null) {
			textBoxSettingsBean.setMaxDate(DateTimeUtils.getInstance().parseISODate(strMaxDate.toString()));
		}
		String strMinInteger = attributes.get("minInteger");
		if (strMinInteger!=null) {
			textBoxSettingsBean.setMinInteger(new Integer(strMinInteger));
		}		
		String strMaxInteger = attributes.get("maxInteger");
		if (strMaxInteger!=null) {
			textBoxSettingsBean.setMaxInteger(new Integer(strMaxInteger));
		}
		String strMinDouble = attributes.get("minDouble");
		if (strMinDouble!=null) {
			textBoxSettingsBean.setMinDouble(DoubleNumberFormatUtil.parseISO(strMinDouble));
		}
		String strMaxDouble = attributes.get("maxDouble");
		if (strMaxDouble!=null) {
			textBoxSettingsBean.setMaxDouble(DoubleNumberFormatUtil.parseISO(strMaxDouble));
		}
		String strMaxDecimalDigit = attributes.get("maxDecimalDigit");
		if (strMaxDecimalDigit!=null) {
			textBoxSettingsBean.setMaxDecimalDigit(new Integer(strMaxDecimalDigit));
		}
		String strParameterCode = attributes.get("parameterCode");
		if (strParameterCode!=null) {
			textBoxSettingsBean.setParameterCode(new Integer(strParameterCode));
		}
		String validValue = attributes.get("validValue");
		if (validValue!=null) {
			textBoxSettingsBean.setValidValue(new Integer(validValue));
		}
		textBoxSettingsBean.setUuid(attributes.get("uuid"));
		return textBoxSettingsBean;
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
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean) serializableLabelBean;						
		Integer externalConfig = getConfig();
		Integer internalConfig = textBoxSettingsBean.getConfig();
		Map<Integer, Integer> fieldConfigMatches = matchesMap.get(ExchangeFieldNames.FIELDCONFIG);
		if(fieldConfigMatches==null){
			return textBoxSettingsBean.getUuid().equals(getUuid());
		}
		//matches if config matches: the individual values are neither compared nor modified if a textbox settings is already present 		
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
		TTextBoxSettingsBean textBoxSettingsBean = (TTextBoxSettingsBean)serializableLabelBean;		
		Integer externalConfigID = textBoxSettingsBean.getConfig();
		if (externalConfigID!=null) {
			Map<Integer, Integer> fieldConfigsMap = 
				matchesMap.get(ExchangeFieldNames.FIELDCONFIG);
			textBoxSettingsBean.setConfig(fieldConfigsMap.get(externalConfigID));
		}
		return TextBoxSettingsBL.save(textBoxSettingsBean);
	}
	
}
