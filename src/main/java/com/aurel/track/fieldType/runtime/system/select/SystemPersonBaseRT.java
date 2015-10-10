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


package com.aurel.track.fieldType.runtime.system.select;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.exchange.track.NameMappingBL;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.matchers.run.SystemSelectMatcherRT;
import com.aurel.track.lucene.LuceneUtil;

public abstract class SystemPersonBaseRT extends SystemSelectBaseRT{
		
	private static final Logger LOGGER = LogManager.getLogger(SystemPersonBaseRT.class); 
	
	/**
	 * Gets the iconCls class for label bean if dynamicIcons is false
	 * @param labelBean
	 */
	public String getIconCls(ILabelBean labelBean) {
		if (labelBean==null) {
			return null;
		} else {
			TPersonBean personBean = (TPersonBean)labelBean;
			if (personBean.isGroup()) {
				return PersonBL.GROUP_ICON_CLASS;
			}else{
				return PersonBL.USER_ICON_CLASS;
			}
		}
	}
	
	/**
	 * The field key for getting the corresponding map from dropdown container.
	 * The SystemSelects and the Pickers are stored under 
	 * the same key in the container (the key of the SystemSelect field)
	 * (for ex. manager, responsible, originator, changedBy and 
	 * user picker share the same person map)
	 * The CustomSelects are stored under their own fieldIDs   
	 * @return
	 */
	@Override
	public Integer getDropDownMapFieldKey(Integer fieldID) {
		return new Integer(SystemFields.PERSON);
	}
	/**
	 * Get the ILabelBean by primary key 
	 * @return
	 */
	@Override
	public ILabelBean getLabelBean(Integer optionID, Locale locale) {
		if (optionID!=null && 
				(optionID.equals(MatcherContext.LOGGED_USER_SYMBOLIC) || optionID.equals(MatcherContext.PARAMETER))) {			
			TPersonBean personBean = new TPersonBean();
			String localizedName;
			if (optionID.equals(MatcherContext.LOGGED_USER_SYMBOLIC)) {
				localizedName = MatcherContext.getLocalizedLoggedInUser(locale);
			} else {
				localizedName = MatcherContext.getLocalizedParameter(locale);
			}
			personBean.setLastName(localizedName);
			personBean.setObjectID(optionID);
			return personBean;
		}
		return PersonBL.loadByPrimaryKey(optionID);
	}
	
	/**
	 * Gets the context with actual item data for injecting content into the labels
	 * @param fieldID
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public Map<String, Object> getLabelContext(Integer fieldID, Object value, Locale locale) {
		Map<String, Object> labelContextMap = null;
		Integer selectedValue = null;
		if (value!=null) {
			try {
				selectedValue = (Integer)value;
			} catch(Exception ex) {
				
			}
			if (selectedValue!=null) {
				TPersonBean personBean = LookupContainer.getPersonBean(selectedValue);
				if (personBean!=null) {
					labelContextMap = new HashMap<String, Object>();
					labelContextMap.put("email", personBean.getEmail());
					labelContextMap.put("phone", personBean.getPhone());
					labelContextMap.put("name", personBean.getName());
				}
				
			}
		}
		return labelContextMap;
	}
	
	/**
	 * Loads the matcher for the field 
	 * @param fieldID 
	 */
	@Override
	public IMatcherRT getMatcherRT(Integer fieldID, int relation, Object matchValue, 
			MatcherContext matcherContext){
		IMatcherRT matcherRT = new SystemSelectMatcherRT(fieldID, relation, matchValue, matcherContext);
		//replace the symbolic value with the actual value
		Integer[] matchValueArr;
		if (matchValue!=null) {
			try {
				matchValueArr = (Integer[])matchValue;
				if (matchValueArr!=null) {
					for (int i = 0; i < matchValueArr.length; i++) {
						Integer intMatchValue = matchValueArr[i];
						if (intMatchValue!=null && intMatchValue.equals(MatcherContext.LOGGED_USER_SYMBOLIC)) {
							Map<Integer, Integer> loggedUserReplacement = 
								(Map<Integer, Integer>)(matcherContext.getContextMap().get(MatcherContext.LOGGED_USER));
							if (loggedUserReplacement!=null && loggedUserReplacement.get(MatcherContext.LOGGED_USER_SYMBOLIC)!=null) {
								matchValueArr[i] = loggedUserReplacement.get(MatcherContext.LOGGED_USER_SYMBOLIC);
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Converting the match value to Integer[] failed with " + e.getMessage(), e);
			}
		}
		return matcherRT;
	}
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.PERSONNAME;
	}
	
	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	public ISerializableLabelBean getNewSerializableLabelBean() {
		return new TPersonBean();
	}
	
	/**
	 * Gets the ID by the label
	 * @param fieldID	 
	 * @param projectID
	 * @param issueTypeID
	 * @param locale
	 * @param label
	 * @param lookupBeansMap
	 * @param componentPartsMap
	 * @return
	 */
	public Integer getLookupIDByLabel(Integer fieldID,
			Integer projectID, Integer issueTypeID, 
			Locale locale, String label, 
			Map<String, ILabelBean> lookupBeansMap, Map<Integer, Integer> componentPartsMap) {
		if (label!=null) {
			/*if (label.endsWith(TPersonBean.SYS_ADM)) {
				label = label.replace(TPersonBean.SYS_ADM, "");
			}*/
			if (label.endsWith(TPersonBean.DEACTIVATED)) {
				label = label.replace(TPersonBean.DEACTIVATED, "");
			}
		}
		Integer objectID = NameMappingBL.getExactOrSimilarMatch(label, lookupBeansMap);
		if (objectID!=null) {
			return objectID;
		} else {
			//try to match the login name
			if (lookupBeansMap!=null) {
				Map<String, ILabelBean> loginToLabelBeanMap = new HashMap<String, ILabelBean>();		
				for (Iterator<ILabelBean> iterator = lookupBeansMap.values().iterator(); iterator.hasNext();) {
					TPersonBean labelBean = (TPersonBean)iterator.next();
					String loginName = labelBean.getLoginName();
					if (loginName!=null) {
						if (loginName.equals(label)) {
							return labelBean.getObjectID();
						} else {
							loginToLabelBeanMap.put(loginName, labelBean);
						}
					}
				}
				String similarName = NameMappingBL.getSimilarName(label, loginToLabelBeanMap.keySet());
				if (similarName!=null) {
					ILabelBean labelBean  = loginToLabelBeanMap.get(similarName);
					if (labelBean!=null) {
						return labelBean.getObjectID();
					}
				}
			}
		}
		TPersonBean personBean = PersonBL.loadByLabel(label);
		if (personBean!=null) {
			return personBean.getObjectID();
		}
		return null;
	}
	
	/**
	 * Gets the datasource for all possible list entries 
	 * @param fieldID
	 * @return
	 */
	public List<ILabelBean> getDataSource(Integer fieldID) {
		return (List)PersonBL.loadPersonsAndGroups();
	}
}
