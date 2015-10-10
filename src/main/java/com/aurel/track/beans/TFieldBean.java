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
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.util.EqualUtils;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TFieldBean
    extends com.aurel.track.beans.base.BaseTFieldBean
    implements Serializable, IBeanID, ISerializableLabelBean
{
		
	private static final long serialVersionUID = 1L;
	
	
	public boolean isFilterFieldString() {
		if (BooleanFields.TRUE_VALUE.equals(getFilterField())) {
			return true;
		}
		return false;
	}
	
	public void setFilterFieldString(boolean filterField) {
		if (filterField) {
			setFilterField(BooleanFields.TRUE_VALUE);
		} else {
			setFilterField(BooleanFields.FALSE_VALUE);
		}
	}
	
	public boolean isDeprecatedString() {
		if (BooleanFields.TRUE_VALUE.equals(getDeprecated())) {
			return true;
		}
		return false;
	}
	
	public void setDeprecatedString(boolean deprecated) {
		if (deprecated) {
			setDeprecated(BooleanFields.TRUE_VALUE);
		} else {
			setDeprecated(BooleanFields.FALSE_VALUE);
		}
	}
	
	public boolean isRequiredString() {
		if (BooleanFields.TRUE_VALUE.equals(getRequired())) {
			return true;
		}
		return false;
	}
	
	public boolean isCustomString() {
		if (BooleanFields.TRUE_VALUE.equals(getIsCustom())) {
			return true;
		}
		return false;
	}
	
	public String getLabel() {
		return getName();
	}
	
	/**
	 * Whether two label beans are equivalent
	 * @param serializableLabelBean
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean) {
		if (serializableLabelBean==null) {
			return false;
		}
		TFieldBean fieldBean = (TFieldBean) serializableLabelBean;
		if (getUuid() != null && fieldBean.getUuid()!=null) {
			if (getUuid().equals(fieldBean.getUuid())) {
				return true;
			}
		}
		String externalName = getName();
		String internalName = fieldBean.getName();
		String externalFieldType = getFieldType();
		String internalFieldType = fieldBean.getFieldType();
		//a field matches if the name and field type matches
		if (externalName!=null && internalName!=null && 
				externalFieldType!=null && internalFieldType!=null) {
			return externalName.equals(internalName) && externalFieldType.equals(internalFieldType);
		}		
		return false;
	}
	
	/**
	 * Serialize a label bean to a dom element
	 * @param labelBean
	 * @return
	 */
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("name", getName());
		attributesMap.put("fieldType", getFieldType());
		attributesMap.put("deprecated", getDeprecated());
		attributesMap.put("isCustom", getIsCustom());
		attributesMap.put("required", getRequired());
		attributesMap.put("filterField", getFilterField());
		String description = getDescription();
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);
		}
		//the persons are by field match not yet available
		Integer owner = getOwner();
		if (owner!=null ) {
			attributesMap.put("owner", owner.toString());
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
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			this.setObjectID(new Integer(strObjectID));
		}
		this.setName(attributes.get("name"));
		this.setFieldType(attributes.get("fieldType"));
		
		this.setDeprecated(attributes.get("deprecated"));
		this.setIsCustom(attributes.get("isCustom"));
		this.setRequired(attributes.get("required"));
		this.setDescription(attributes.get("description"));
		//the persons are by field match not yet available
		/*String strOwner = attributes.get("owner");
		if (strOwner!=null) {
			fieldBean.setOwner(new Integer(strOwner));
		}*/
		this.setUuid(attributes.get("uuid"));
		return this;
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
		TFieldBean fieldBean = (TFieldBean) serializableLabelBean;
		String externalUuid = getUuid();
		String internalUuid = fieldBean.getUuid();
		if(EqualUtils.equalStrict(externalUuid, internalUuid)){
			return true;
		}
		String externalName = getName();
		String internalName = fieldBean.getName();
		String externalFieldType = getFieldType();
		String internalFieldType = fieldBean.getFieldType();
		return EqualUtils.equal(externalName,internalName)&&
				EqualUtils.equal(externalFieldType,internalFieldType);
	}
	
	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	public Integer saveBean(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		TFieldBean fieldBean = (TFieldBean)serializableLabelBean;
		//the persons are by field match not yet available
		/*Integer owner = fieldBean.getOwner();
		if (owner!=null) {
			Map<Integer, Integer> personsMap = 
				matchesMap.get(MergeUtil.mergeKey(SystemFields.PERSON, null));
			fieldBean.setOwner(personsMap.get(owner));
		}*/		
		return FieldBL.save(fieldBean);
	}
	
	public Integer saveBean(ISerializableLabelBean serializableLabelBean) {
		TFieldBean fieldBean = (TFieldBean)serializableLabelBean;
		return FieldBL.save(fieldBean);
	}
}
