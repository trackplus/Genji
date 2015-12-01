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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.treeConfig.field.OptionSettingsBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TOptionSettingsBean
    extends com.aurel.track.beans.base.BaseTOptionSettingsBean
    implements Serializable, ISerializableLabelBean
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TOptionSettingsBean.class);		
	
	@Override
	public String getLabel() {	
		return null;
	}
	
	/**
	 * Serialize a label bean
	 * @param labelBean
	 * @return
	 */
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("list", getList().toString());
		attributesMap.put("config", getConfig().toString());		
		Integer parameterCode = getParameterCode();
		if (parameterCode!=null) {
			attributesMap.put("parameterCode", parameterCode.toString());
		}
		String multiple = getMultiple();
		if (multiple!=null && !"".equals(multiple)) {
			attributesMap.put("multiple", multiple);
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;			
	}
	
	/**
	 * Deserialize the labelBean 
	 * @param attributes
	 * @return
	 */
	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TOptionSettingsBean optionSettingsBean = new TOptionSettingsBean();				
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			optionSettingsBean.setObjectID(new Integer(strObjectID));
		}
		String strList = attributes.get("list");
		if (strList!=null) {
			optionSettingsBean.setList(new Integer(strList));
		}
		String strConfig = attributes.get("config");
		if (strConfig!=null) {
			optionSettingsBean.setConfig(new Integer(strConfig));
		}
		optionSettingsBean.setUuid(attributes.get("uuid"));
		return optionSettingsBean;
	}
	
	
	/**
	 * Whether two label beans are equivalent  
	 * @param serializableLabelBean
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}		
		TOptionSettingsBean optionSettingsBean = (TOptionSettingsBean) serializableLabelBean;			
		if (getUuid()!=null && optionSettingsBean.getUuid()!=null)
			if (getUuid().equals(optionSettingsBean.getUuid()))
				return true;
		Integer externalConfig = getConfig();
		Integer internalConfig = optionSettingsBean.getConfig();
		
		Map<Integer, Integer> fieldConfigMatches = matchesMap.get(ExchangeFieldNames.FIELDCONFIG); 
		//matches if config matches: the list values are neither compared nor modified if an option settings is already present 
		if (externalConfig!=null && internalConfig!=null && fieldConfigMatches != null && fieldConfigMatches.get(externalConfig)!=null) {
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
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {		
		TOptionSettingsBean optionSettingsBean = (TOptionSettingsBean)serializableLabelBean;
		Integer externalListID = optionSettingsBean.getList();
		if (externalListID!=null) {
			Map<Integer, Integer> listsMap = 
				matchesMap.get(ExchangeFieldNames.LIST);
			Integer internalListID = listsMap.get(externalListID);
			if (internalListID==null) {
				LOGGER.debug("No matcher list found for external list " + externalListID);
			}
			optionSettingsBean.setList(internalListID);
		}
		Integer externalConfigID = optionSettingsBean.getConfig();
		if (externalConfigID!=null) {
			Map<Integer, Integer> fieldConfigsMap = 
				matchesMap.get(ExchangeFieldNames.FIELDCONFIG);
			Integer internalFieldConfigID = fieldConfigsMap.get(externalConfigID);
			if (internalFieldConfigID==null) {
				LOGGER.debug("No matcher config found for external list " + externalListID);
			}
			optionSettingsBean.setConfig(internalFieldConfigID);
		}
		return OptionSettingsBL.save(optionSettingsBean);
	}

	
}
