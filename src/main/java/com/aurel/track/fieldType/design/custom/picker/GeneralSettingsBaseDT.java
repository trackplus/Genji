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


package com.aurel.track.fieldType.design.custom.picker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.fieldType.design.BaseFieldTypeDT;

/**
 * Base class for design time custom fields whose configuration 
 * is made through one or more GeneralSettings beans 
 * @author Tamas Ruff
 *
 */
public class GeneralSettingsBaseDT extends BaseFieldTypeDT {

	private static final Logger LOGGER = LogManager.getLogger(GeneralSettingsBaseDT.class);
	
	
	public GeneralSettingsBaseDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);		
	}

	public GeneralSettingsBaseDT(String pluginID) {
		super(pluginID);
	}

	/**
	 * Get the set of those parameter codes which have a single value associated
	 * Should be set according to the template ftl   
	 * @return
	 */
	protected List<Integer> getGeneralSettingsParameterCodes() {
		return null;
	}
	
	/**
	 * Get the set of those parameter codes which might have multiple values associated
	 * (typically multiple select)
	 * Should be set according to the template ftl
	 * @return
	 */
	protected List<Integer> getMultipleIntegerParameterCodes() {
		return null;
	}
	
	/**
	 * Copies the settings from the source map to the destination map, setting also the configIDs for the destination beans
	 * @param srcSettings the source settings:
	 * 	- key: parameterCode
	 * 	- value: TGeneralSettingsBean or a list of TGeneralSettingsBeans
	 * @param destSettings the destination settings
	 * 	- key:	parameterCode
	 * 	- value: TGeneralSettingsBean or a list of TGeneralSettingsBeans
	 * @param destConfigID the configID for the destination settings 
	 */	
	@Override
	public void copySettings(Map<Integer, Object> srcSettings, 
			Map<Integer, Object> destSettings, Integer destConfigID) {		
		TGeneralSettingsBean srcGeneralSettingsBean;
		TGeneralSettingsBean destGeneralSettingsBean;				
		List<Integer> singleValueParameterCodes =  getGeneralSettingsParameterCodes();
		if (singleValueParameterCodes!=null) {
			Iterator<Integer> itrSingleValueParameterCodes = singleValueParameterCodes.iterator();
			while (itrSingleValueParameterCodes.hasNext()) {
				Integer parameterCode = itrSingleValueParameterCodes.next();
				srcGeneralSettingsBean = (TGeneralSettingsBean)srcSettings.get(parameterCode);
				if (srcGeneralSettingsBean!=null) {
					destGeneralSettingsBean = copyGeneralSettingsBean(srcGeneralSettingsBean, destConfigID);						
					destSettings.put(parameterCode, destGeneralSettingsBean);
				}						
			}				
		}
		List<Integer> multipleValueParameterCodes = getMultipleIntegerParameterCodes();
		if (multipleValueParameterCodes!=null) { 
			Iterator<Integer> itrMultipleValueParameterCodes = multipleValueParameterCodes.iterator();
			while (itrMultipleValueParameterCodes.hasNext()) {
				Integer parameterCode = itrMultipleValueParameterCodes.next();
				List<TGeneralSettingsBean> srcMultipleParameterList = (List<TGeneralSettingsBean>)srcSettings.get(parameterCode);					
				if (srcMultipleParameterList!=null) {
					List<TGeneralSettingsBean> destMultipleParameterList = new LinkedList<TGeneralSettingsBean>();
					destSettings.put(parameterCode, destMultipleParameterList);
					Iterator<TGeneralSettingsBean> iterMultipleList = srcMultipleParameterList.iterator();					
					while (iterMultipleList.hasNext()) {
						srcGeneralSettingsBean = iterMultipleList.next();							
						destGeneralSettingsBean = copyGeneralSettingsBean(srcGeneralSettingsBean, destConfigID);							
						destMultipleParameterList.add(destGeneralSettingsBean);
					}				
				}				
			}						
		}	
	}
	
	/**
	 * Helper method for copying a TGeneralSettingsBean  
	 * @param srcGeneralSettingsBean
	 * @param destConfigID
	 * @return
	 */
	private static TGeneralSettingsBean copyGeneralSettingsBean(
			TGeneralSettingsBean srcGeneralSettingsBean, Integer destConfigID) {
		TGeneralSettingsBean destGeneralSettingsBean = new TGeneralSettingsBean();
		//FieldBeansHelper.copyGeneralSettingsProperties(srcGeneralSettingsBean, destGeneralSettingsBean);
		destGeneralSettingsBean.setParameterCode(srcGeneralSettingsBean.getParameterCode());
		destGeneralSettingsBean.setIntegerValue(srcGeneralSettingsBean.getIntegerValue());
		destGeneralSettingsBean.setDoubleValue(srcGeneralSettingsBean.getDoubleValue());
		destGeneralSettingsBean.setTextValue(srcGeneralSettingsBean.getTextValue());
		destGeneralSettingsBean.setDateValue(srcGeneralSettingsBean.getDateValue());
		destGeneralSettingsBean.setValidValue(srcGeneralSettingsBean.getValidValue());
		destGeneralSettingsBean.setConfig(destConfigID);
		return destGeneralSettingsBean;
	}
	
	/**
	 * Loads a map with field settings for a configID
	 * - key: parameterCode
	 * - value: TGeneralSettingsBean or a list of TGeneralSettingsBeans
	 * @param configID
	 * @return
	 */
	@Override
	public Map<Integer, Object> loadSettings(Integer configID) {		
		Map<Integer, Object> settings = new HashMap<Integer, Object>();
		if (configID!=null) {
			List<Integer> singleValueParameterCodes =  getGeneralSettingsParameterCodes();
			List<Integer> multipleValueParameterCodes = getMultipleIntegerParameterCodes();
			List<TGeneralSettingsBean> generalSettingsBeans = GeneralSettingsBL.loadByConfig(configID);
			for (TGeneralSettingsBean generalSettingsBean : generalSettingsBeans) {
				Integer parameterCode = generalSettingsBean.getParameterCode();
				if (parameterCode==null) {
					LOGGER.error("Null parameter code by loading the general settings for congigID " + configID);
				} else {					
					if (singleValueParameterCodes!=null && singleValueParameterCodes.contains(parameterCode)) {
						settings.put(parameterCode, generalSettingsBean);
					} else {
						if (multipleValueParameterCodes!=null && multipleValueParameterCodes.contains(parameterCode)) {
							List multipleParameterList = (List)settings.get(parameterCode);
							if (multipleParameterList==null) {
								multipleParameterList= new LinkedList();
								settings.put(parameterCode, multipleParameterList);
							}
							multipleParameterList.add(generalSettingsBean);
						} else {
							LOGGER.error("Wrong paremeterCode "+ parameterCode + " by loading the general settings for congigID " + configID);
						}
					}
				}			
			}
		}
		return settings;
	}
	
	
	/**
	 * Saves the configuration settings for a field
	 * @param settings
	 * @param configID
	 */
	@Override
	public void saveSettings(Map<Integer, Object> settings, Integer configID) {
		//deleteSettings(configID);		
		List<Integer>  singleValueParameterCodes =  getGeneralSettingsParameterCodes();
		List<Integer> multipleValueParameterCodes = getMultipleIntegerParameterCodes();		
		for (Integer parameterCode : settings.keySet()) {
			if (singleValueParameterCodes!=null && singleValueParameterCodes.contains(parameterCode)) {
				TGeneralSettingsBean generalSettingsBean = (TGeneralSettingsBean)settings.get(parameterCode);
				generalSettingsBean.setConfig(configID);
				GeneralSettingsBL.save(generalSettingsBean);
			} else { 
				if (multipleValueParameterCodes!=null && multipleValueParameterCodes.contains(parameterCode)) {
					List<TGeneralSettingsBean> multipleParameterList = (List<TGeneralSettingsBean>)settings.get(parameterCode);
					if (multipleParameterList!=null) {
						for (TGeneralSettingsBean generalSettingsBean : multipleParameterList) {
							generalSettingsBean.setConfig(configID);
							GeneralSettingsBL.save(generalSettingsBean);
						}
					}
				} else {
					LOGGER.error("Wrong paremeterCode "+ parameterCode + " by saving the general settings for congigID " + configID);
				}
			}
		}
	}
		
	/**
	 * Deletes the select list settings from the database
	 * @param configID
	 */
	@Override
	public void deleteSettings(Integer configID) {
		GeneralSettingsBL.deleteByConfig(configID);
	}
}
