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


package com.aurel.track.exchange.track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.util.GeneralUtils;

public class FieldMapperBL {
	
	private static final Logger LOGGER = LogManager.getLogger(FieldMapperBL.class);
	
	/**
	 * The maximal number of hardcoded fields for 
	 * which the fieldID equality precedes UUID equality
	 */
	public static int maximalHardcodedFields = 50;
	
	/**
	 * Get the trackplus mappings which should not be rendered:
	 * The first 50 are mapped using the fieldID, and above based on uuid
	 * Trackplus - only imports based on a trackplus export
	 * The matches found are removed from the externalFields list 
	 * @param externalFields
	 * @param internalFields
	 * @param fieldIDsSet
	 * @param uuidsSet
	 * @return
	 */
	public static List<ExchangeField> getImplicitTrackplusMapping(
			List<ExchangeField> externalFields, List<TFieldBean> internalFields, 
			Set<Integer> fieldIDsSet, Set<String> uuidsSet) {
		List<ExchangeField> explicitMapping = new ArrayList<ExchangeField>();
		Map<String, ISerializableLabelBean> fieldBeansByUUIDMap = 
			GeneralUtils.createUUIDMapFromSerializableBean((List)internalFields);
		if (externalFields!=null) {
			Iterator<ExchangeField> iterator = externalFields.iterator();
			while (iterator.hasNext()) {
				ExchangeField exchangeField = iterator.next();
				Integer fieldID = exchangeField.getFieldID();
				String fieldUuid = exchangeField.getFieldUuid();
				String fieldType = exchangeField.getFieldType();				
				if (fieldID!=null && fieldID.intValue()<maximalHardcodedFields) {
					//the system fieldIDs are hardcoded anyway and they should 
					//be the same between track+ instances
					//(the UUIDs are the same only if the export was made from the same system)
					fieldIDsSet.add(fieldID);
					iterator.remove();
					continue;
				}
				if (fieldUuid!=null) {
					if (fieldBeansByUUIDMap.containsKey(fieldUuid)) {
						TFieldBean fieldBean = (TFieldBean)fieldBeansByUUIDMap.get(fieldUuid);
						if (fieldType!=null && !fieldType.equals(fieldBean.getFieldType())) {
							//a custom field: the export was made from the same system
							uuidsSet.add(fieldUuid);
							iterator.remove();
							continue;
						} else {
							//should never happen
							LOGGER.warn("The field type for fieldID " + fieldID + " and fieldUuid" + fieldUuid  + " has been changed");
						}
					}
				}
			}
		}		
		return explicitMapping;		
	}
	
	/**
	 * Prepare the rendering for explicit trackplus mapping
	 * Trackplus - only imports based on a trackplus export - only then are fieldID and fieldType specified
	 * @param importFields
	 * @param localeFields
	 * @return
	 */
	public static List<MapperBean> prepareExplicitTrackplusMapping(List<ExchangeField> importFields, List<TFieldBean> localeFields) {
		List<MapperBean> mappingList = new ArrayList<MapperBean>();
		Map<String, List<TFieldBean>> fieldBeansByFieldTypeMap = getFieldBeansByFieldTypeMap(localeFields);
		if (importFields!=null) {
			Iterator<ExchangeField> iterator = importFields.iterator();
			while (iterator.hasNext()) {
				ExchangeField exchangeField = iterator.next();
				Integer fieldID = exchangeField.getFieldID();				
				String fieldName = exchangeField.getFieldName();
				String fieldType = exchangeField.getFieldType();				
				if (fieldID!=null && fieldType!=null) {
					List<TFieldBean> possibleMappings = fieldBeansByFieldTypeMap.get(fieldType);
					if (possibleMappings==null) {
						LOGGER.warn("No field type " + fieldType + " for fieldID " + fieldID + " has been found");
					} else {
						MapperBean mapperBean = new MapperBean(exchangeField, possibleMappings);
						mappingList.add(mapperBean);
						//try a preselection by name
						boolean foundByName = false;
						Iterator<TFieldBean> possibleMappingsItr = possibleMappings.iterator();						
						while (possibleMappingsItr.hasNext()) {
							TFieldBean fieldBean = possibleMappingsItr.next();
							if (fieldName!=null && fieldName.equals(fieldBean.getName())) {
								mapperBean.setPreselectedValue(fieldBean.getObjectID());
								foundByName = true;
							}
						}
						//lastly try a preselection by fieldID
						if (!foundByName) {
							possibleMappingsItr = possibleMappings.iterator();						
							while (possibleMappingsItr.hasNext()) {
								TFieldBean fieldBean = possibleMappingsItr.next();
								if (fieldID.equals(fieldBean.getObjectID())) {
									mapperBean.setPreselectedValue(fieldBean.getObjectID());							
								}
							}	
						}
					}
					iterator.remove();
					continue;
				}
			}
		}
		return mappingList;
	}
	
	/**
	 * When the application is external all the files found in the external datasource should be prepared for mapping
	 * A preselction can be made based only in names (ExchangeFields contain only names) 
	 * @param importFields
	 * @param localeFields
	 */
	public static List<MapperBean> prepareExternalApplicationMapping(
			List<ExchangeField> importFields, List<TFieldBean> localeFields) {
		List<MapperBean> mappingList = new ArrayList<MapperBean>();		
		if (importFields!=null) {
			Iterator<ExchangeField> iterator = importFields.iterator();
			while (iterator.hasNext()) {
				ExchangeField exchangeField = iterator.next();							
				String fieldName = exchangeField.getFieldName();								
				MapperBean mapperBean = new MapperBean(exchangeField, localeFields);
				mappingList.add(mapperBean);
				//try a preselection by name			
				Iterator<TFieldBean> possibleMappingsItr = localeFields.iterator();						
				while (possibleMappingsItr.hasNext()) {
					TFieldBean fieldBean = possibleMappingsItr.next();
					if (fieldName!=null && fieldName.equals(fieldBean.getName())) {
						mapperBean.setPreselectedValue(fieldBean.getObjectID());
					}
				}				
			}				
		}		
		return mappingList;
	}
	
	/**
	 * Get a fieldType based map for a list of TFieldBean for quicker find
	 * @param fields
	 * @return
	 */
	private static Map<String, List<TFieldBean>> getFieldBeansByFieldTypeMap(List<TFieldBean> fields) {
		Map<String, List<TFieldBean>> fieldBeansListByFieldTypeMap = new HashMap<String, List<TFieldBean>>();
		Iterator<TFieldBean> iterator = fields.iterator();
		while (iterator.hasNext()) {
			TFieldBean fieldBean = iterator.next();
			String fieldType = fieldBean.getFieldType();
			if (fieldType!=null) {
				List<TFieldBean> fieldBeansList = fieldBeansListByFieldTypeMap.get(fieldType);
				if (fieldBeansList==null) {
					fieldBeansList = new ArrayList<TFieldBean>();
					fieldBeansListByFieldTypeMap.put(fieldType, fieldBeansList);
				}
				fieldBeansList.add(fieldBean);
			}
		}
		return fieldBeansListByFieldTypeMap;
	}
}
