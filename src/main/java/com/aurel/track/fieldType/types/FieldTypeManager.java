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


package com.aurel.track.fieldType.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.design.IFieldTypeDT;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
/**
 * Used to determine the type of a given field
 * @author Adrian Bojani
 *
 */
public class FieldTypeManager {
	private static final Logger LOGGER = LogManager.getLogger(FieldTypeManager.class);
	//singleton instance
	private static FieldTypeManager instance;
	/**
	 * - key: fieldID
	 * - value: a FieldType instance object  
	 */
	private Map<Integer, FieldType> typeCache;
	/**
	 * - key: fieldID
	 * - value: a FieldType class name
	 */
	private Map<Integer, String> typeNameCache;
	/**
	 * - key: fieldID
	 * - value: a TFieldBean object 
	 */
	private Map<Integer, TFieldBean> fieldBeanCache;
	/**
	 * get a singleton instance
	 * @return
	 */
	public static FieldTypeManager getInstance() {
		if (instance == null) {
			instance = new FieldTypeManager();
		}
		return instance;
	}

	/**
	 * make constructor private
	 */
	private FieldTypeManager() {
	}
	
	/**
	 * get the Type for a field id
	 * @param fieldID
	 * @return
	 */
	public String getTypeName(Integer fieldID){
		if(typeNameCache==null){
			loadFromDB();
		}
		String typeName=(String)typeNameCache.get(fieldID);
		if(typeName==null) { //maybe a new field was just added
			loadFromDB();
			typeName=(String)typeNameCache.get(fieldID);
		}
		if (typeName==null) {
			LOGGER.debug("The name of the FieldType class for fieldID " + fieldID +  " is not found");
		}
		return typeName;
	}
	
	/**
	 * get the Type for a field id
	 * @param fieldID
	 * @return
	 */
	public FieldType getType(Integer fieldID){
		if (fieldID==null || fieldID.intValue()<0) {
			return null;
		}
		if(typeCache==null){
			loadFromDB();
		}
		FieldType type=(FieldType)typeCache.get(fieldID);
		if(type==null) { //maybe a new field was just added
			loadFromDB();
			type=(FieldType)typeCache.get(fieldID);
		}
		if (type==null) {
			LOGGER.debug("The FieldType object for fieldID " + fieldID +  " is not found");
		}
		return type;
	}
	
	/**
	 * get the Type for a field id
	 * @param fieldID
	 * @return
	 */
	public TFieldBean getFieldBean(Integer fieldID){
		if(fieldBeanCache==null){
			loadFromDB();
		}
		TFieldBean fieldBean=(TFieldBean)fieldBeanCache.get(fieldID);
		if(fieldBean==null) {//maybe a new field was just added
			loadFromDB();
			fieldBean=(TFieldBean)fieldBeanCache.get(fieldID);
		}
		if (fieldBean==null) {
			LOGGER.info("The fieldBean with ID " + fieldID +  " is not found in the TField table");
		}
		return fieldBean;
	}
	
	public void invalidateCache(){
		typeNameCache=null;
		typeCache=null;
		fieldBeanCache=null;
	}
	
	/**
	 * Load all fields from the database and populate the two maps
	 *
	 */
	private void loadFromDB(){
		typeNameCache=new HashMap<Integer, String>();
		typeCache=new HashMap<Integer, FieldType>();
		fieldBeanCache=new HashMap<Integer, TFieldBean>();
		List<TFieldBean> fields = FieldBL.loadAll();
		for (TFieldBean fieldBean : fields) {
			typeNameCache.put(fieldBean.getObjectID(), fieldBean.getFieldType());
			typeCache.put(fieldBean.getObjectID(), FieldType.fieldTypeFactory(fieldBean.getFieldType()));
			fieldBeanCache.put(fieldBean.getObjectID(), fieldBean);
		}
	}
	
	/**
	 * @return the fieldBeanCache
	 */
	public Map<Integer, TFieldBean> getFieldBeanCache() {
		if (fieldBeanCache==null) {
			loadFromDB();
		}
		return fieldBeanCache;
	}

	/**
	 * @return the typeCache
	 */
	public Map<Integer, FieldType> getTypeCache() {
		if (typeCache==null) {
			loadFromDB();
		}
		return typeCache;
	}
	
	/**
	 * Return a FieldType instance for a fieldID
	 * @param fieldID
	 * @return
	 */
	public static String getFieldTypeName(Integer fieldID) {
		return getInstance().getTypeName(fieldID);
	}
	
	/**
	 * Return a FieldType instance for a fieldID
	 * @param fieldID
	 * @return
	 */
	public static FieldType getFieldType(Integer fieldID) {
		return getInstance().getType(fieldID);
	}
	
	/**
	 * Return the design time interface for a fieldID
	 * @param fieldID
	 * @return
	 */
	public static IFieldTypeDT getFieldTypeDT(Integer fieldID) {
		FieldType fieldType = getInstance().getType(fieldID);
		if (fieldType!=null) {
			return fieldType.getFieldTypeDT();
		}
		return null;
	}
	
	/**
	 * Return the runtime interface for a fieldID
	 * @param fieldID
	 * @return
	 */
	public static IFieldTypeRT getFieldTypeRT(Integer fieldID) {
		FieldType fieldType = getInstance().getType(fieldID);
		if (fieldType!=null) {
			return fieldType.getFieldTypeRT();
		}
		return null;
	}
	
	/**
	 * Return the runtime interface for a composite field component
	 * @param fieldID
	 * @param parameterCode
	 * @return
	 */
	public static IFieldTypeRT getFieldTypeRT(Integer fieldID, Integer parameterCode) {
		IFieldTypeRT fieldTypeRT = getFieldTypeRT(fieldID);
		if (fieldTypeRT==null || parameterCode==null) {
			return fieldTypeRT;
		}
		try {
			CustomCompositeBaseRT customCompositeBaseRT = (CustomCompositeBaseRT)fieldTypeRT;
			return customCompositeBaseRT.getCustomFieldType(parameterCode.intValue());
		} catch (Exception e) {
			LOGGER.warn("Getting the IFieldTypeRT for field " + fieldID + 
					" and parameterCode " + parameterCode + " failed with " + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Whether  the field with this fieldID is a long text because then the read only
	 * rendering (on the report overview, printItem, etc.)
	 * should be made different as the rest of the "short" fields
	 * This can be the case for Description, Comment and longtext custom fields
	 * @parm  fieldID
	 * @return
	 */
	public static boolean isLong(Integer fieldID){
		IFieldTypeRT fieldTypeRT = getFieldTypeRT(fieldID);
		if (fieldTypeRT!=null) {
			return fieldTypeRT.isLong();
		}
		return false;
	}
	
	
	/**
	 * Whether the field with this fieldID is a groupable 
	 * @parm  fieldID
	 * @return
	 */
	public static boolean isGroupable(Integer fieldID){
		IFieldTypeRT fieldTypeRT = getFieldTypeRT(fieldID);
		if (fieldTypeRT!=null) {
			return fieldTypeRT.isGroupable();
		}
		return false;
	}
	
}
