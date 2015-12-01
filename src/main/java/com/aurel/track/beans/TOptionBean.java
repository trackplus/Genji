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

import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.persist.ISortedObject;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.util.EqualUtils;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TOptionBean
    extends com.aurel.track.beans.base.BaseTOptionBean
    implements Serializable, ISortedObject, IBeanID, ILocalizedLabelBean, ISortedBean, ISerializableLabelBean
{	
	
	private static final long serialVersionUID = 1L;
	@Override
	public String getKeyPrefix() {
		return LocalizationKeyPrefixes.FIELD_CUSTOMSELECT_KEY_PREFIX+getList();
	}

	@Override
	public Comparable getSortOrderValue() {
		return getSortOrder();
	}
	
	public boolean isDefault() {
		return BooleanFields.fromStringToBoolean(getIsDefault());
	}
	
	public void setDefault(boolean defaultValue) {
		setIsDefault(BooleanFields.fromBooleanToString(defaultValue));
	}
	
	public boolean isDeleted() {
		return BooleanFields.fromStringToBoolean(getDeleted());
	}
		
	
	public void setDeleted(boolean deleted) {
		setDeleted(BooleanFields.fromBooleanToString(deleted));
	}
	
	/**
	 * Serialize a label bean
	 * @return
	 */
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		Integer list = getList();
		if (list!=null) {
			attributesMap.put("list", list.toString());
		}
		attributesMap.put("label", getLabel());
		Integer parentOption = getParentOption();
		if (parentOption!=null) {
			attributesMap.put("parentOption", parentOption.toString());
		}
		Integer sortOrder = getSortOrder();
		if (sortOrder!=null) {
			attributesMap.put("sortOrder", sortOrder.toString());
		}
		attributesMap.put("isDefault", getIsDefault());
		attributesMap.put("deleted", getDeleted());
		attributesMap.put("symbol", getSymbol());
		attributesMap.put("CSSSTyle", getCSSStyle());
		Integer iconKey = getIconKey();
		if (iconKey!=null) {
			attributesMap.put("iconKey", iconKey.toString());
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
		
	
	/**
	 * De-serialze the labelBean 
	 * @param attributes
	 * @return
	 */
	@Override
	public ISerializableLabelBean deserializeBean(
			Map<String, String> attributes) {
		TOptionBean optionBean=new TOptionBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			optionBean.setObjectID(new Integer(strObjectID));
		}
		String strList = attributes.get("list");
		if (strList!=null) {
			optionBean.setList(new Integer(strList));
		}else{
			optionBean.setList(null);
		}
		optionBean.setLabel(attributes.get("label"));
		String strParentOption = attributes.get("parentOption");
		if (strParentOption!=null) {
			optionBean.setParentOption(new Integer(strParentOption));
		}else{
			optionBean.setParentOption(null);
		}
		String strSortOrder = attributes.get("sortOrder");
		if (strSortOrder!=null) {
			optionBean.setSortOrder(new Integer(strSortOrder));
		}
		optionBean.setIsDefault(attributes.get("isDefault"));
		optionBean.setDeleted(attributes.get("deleted"));
		optionBean.setSymbol(attributes.get("symbol"));
		optionBean.setCSSStyle(attributes.get("CSSSTyle"));
		String strIconKey = attributes.get("iconKey");
		if (strIconKey!=null) {
			optionBean.setIconKey(new Integer(strIconKey));
			optionBean.setIconChanged(BooleanFields.TRUE_VALUE);
		}
		optionBean.setUuid(attributes.get("uuid"));
		return optionBean;
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
		TOptionBean optionBean = (TOptionBean) serializableLabelBean;
		String externalUuid = getUuid();
		String internalUuid = optionBean.getUuid();
		if(EqualUtils.equalStrict(externalUuid, internalUuid)){
			return true;
		}
		String externalLabel = getLabel();
		String internalLabel = optionBean.getLabel();
		Integer externalList = getList();
		Integer internalList = optionBean.getList();
		Integer externalParentOption = getParentOption();
		Integer internalParentOption = optionBean.getParentOption();
		if(matchesMap==null||matchesMap.isEmpty()){
			return EqualUtils.equal(externalLabel,internalLabel)&&
					EqualUtils.equal(externalList,internalList)&&
					EqualUtils.equal(externalParentOption,internalParentOption);
		}


		
		Map<Integer, Integer> listMatches = matchesMap.get(ExchangeFieldNames.LIST); 
		Map<Integer, Integer> optionMatches = matchesMap.get(ExchangeFieldNames.OPTION);
		boolean match = false; 
		if (listMatches!=null && listMatches.get(externalList)!=null && listMatches.get(externalList).equals(internalList)) {
			//list match
			if (externalParentOption!=null || internalParentOption!=null) {
				//child option 
				if (externalParentOption!=null && internalParentOption!=null) {
					match = optionMatches!=null && 
							optionMatches.get(externalParentOption).equals(internalParentOption) &&
							EqualUtils.equal(externalLabel,internalLabel);
				}
			} else {
				//not a child option: compare only the labels  
				match = EqualUtils.equal(externalLabel,internalLabel);
			}
		}
		if (match) {
			String deleted = optionBean.getDeleted();
			if (BooleanFields.TRUE_VALUE.equals(deleted)) {
				//remove the deleted flag if match found
				optionBean.setDeleted(BooleanFields.FALSE_VALUE);
				OptionBL.save(optionBean);
			}
		}
		return match;
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
		TOptionBean optionBean = (TOptionBean)serializableLabelBean;
		Integer externalList = optionBean.getList();
		if (externalList!=null) {
			Map<Integer, Integer> listMap = 
				matchesMap.get(ExchangeFieldNames.LIST);
			optionBean.setList(listMap.get(externalList));
		}
		Integer parentOption = optionBean.getParentOption();
		
		if (parentOption!=null) {
			Map<Integer, Integer> optionMap = 
				matchesMap.get(ExchangeFieldNames.OPTION);
			optionBean.setParentOption(optionMap.get(parentOption));
		}			
		return OptionBL.save(optionBean);
	}
}
