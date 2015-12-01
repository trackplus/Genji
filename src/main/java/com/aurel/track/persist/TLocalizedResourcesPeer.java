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

package com.aurel.track.persist;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TLocalizedResourcesBean;
import com.aurel.track.dao.LocalizedResourcesDAO;
import com.workingdogs.village.Record;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TLocalizedResourcesPeer
	extends com.aurel.track.persist.BaseTLocalizedResourcesPeer
	implements LocalizedResourcesDAO
{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TLocalizedResourcesPeer.class);
	
	/**
	 * Gets the locales used in the database
	 * @return
	 */
	@Override
	public Set<String> getExistingLocales() {
		Set<String> localesList = new TreeSet<String>();
		Criteria crit = new Criteria();
		crit.addSelectColumn(LOCALE);
		crit.add(LOCALE, (Object)null, Criteria.ISNOTNULL);
		crit.setDistinct();
		try {
			List<Record> records = doSelectVillageRecords(crit);
			for (Record record : records) {
				localesList.add(record.getValue(1).asString());
			}
		} catch (Exception e) {
			LOGGER.error("Getting the locales failed with: " + e);
		}
		return localesList;
	}
	
	/**
	 * Gets the TLocalizedResourcesBean for a prefixString, key and the locale string 
	 * @param fieldName
	 * @param key
	 * @param locale
	 * @return
	 */
	@Override
	public List<TLocalizedResourcesBean> getLocalizedResourceForKey(String fieldName, Integer key, String locale){
		Criteria crit = new Criteria();
		if (fieldName==null) {
			return null;
		}
		crit.add(FIELDNAME, fieldName);
		if (key==null) {
			crit.add(PRIMARYKEYVALUE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(PRIMARYKEYVALUE, key);
		}
		if (locale==null) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, locale);
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the localized string for prefixString " + 
					fieldName+ " key " + key + " and locale " + locale + " failed with: " + e);
			return null;
		}
	}
	
	
	
	
	/**
	 * Gets the TLocalizedResourcesBean for a prefixString, localizedText and the locale string 
	 * @param fieldName
	 * @param localizedText
	 * @param locale
	 * @return
	 */
	@Override
	public TLocalizedResourcesBean getKeyForLocalizedResource(String fieldName, String localizedText, String locale) {
		Criteria crit = new Criteria();
		if (fieldName==null || localizedText==null) {
			return null;
		}
		crit.add(FIELDNAME, fieldName);
		crit.add(LOCALIZEDTEXT, localizedText);
		if (locale==null) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, locale);
		}
		List localizedResourceList = null;
		try {
			localizedResourceList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading the primary key for prefixString " + 
					fieldName+ " localizedText " + localizedText + " and locale " + locale + " failed with: " + e);
		} 
		if (localizedResourceList==null || localizedResourceList.isEmpty()) {
			return null;
		}
		return ((TLocalizedResources)localizedResourceList.get(0)).getBean();
	}
	
	/**
	 * Gets all localized strings for a prefixString and for a locale
	 * If locale is not specified get the value for null locale
	 * @param fieldName
	 * @param locale
	 * @return
	 */
	@Override
	public List<TLocalizedResourcesBean> getLocalizedResourcesForType(String fieldName, String locale) {
		List<TLocalizedResources> localizedResourceList = null;
		Criteria crit = new Criteria();
		if (fieldName==null) {
			return new ArrayList<TLocalizedResourcesBean>();
		}
		crit.add(FIELDNAME, fieldName);
		if (locale==null) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, locale);
		}
		try {
			localizedResourceList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading all localized strings for fieldName " + 
					fieldName + " and locale " + locale + " failed with: " + e);
		}
		return convertTorqueListToBeanList(localizedResourceList);
	}
	
	
	
	/**
	 * Gets all localized strings for a likePrefixString and for a locale
	 * If locale is not specified get the value for null locale
	 * @param fieldName
	 * @param likeString
	 * @param locale
	 * @return
	 */
	@Override
	public List<TLocalizedResourcesBean> getLocalizedResourcesForTypeAndPrefixLike(String fieldName, String likeString, String locale) {
		List<TLocalizedResources> localizedResourceList = null;
		Criteria crit = new Criteria();		
		if (fieldName!=null && !"".equals(fieldName)) {
			crit.add(FIELDNAME, fieldName);
		}
		if (likeString!=null && !"".equals(likeString)) {
			crit.add(LOCALIZEDTEXT, (Object)likeString, Criteria.LIKE);
			crit.getCriterion(LOCALIZEDTEXT).setIgnoreCase(true);
		}
		if (locale==null) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, locale);
		}
		try {
			localizedResourceList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading all localized strings for fieldName " + fieldName + " likePrefixString " + 
					likeString + " and locale " + locale + " failed with: " + e);
		}		 
		return convertTorqueListToBeanList(localizedResourceList);
	}
	
	/**
	 * Gets all localized strings for a fieldName, primaryKeys and for a locale
	 * If locale is not specified get the value for null locale
	 * @param fieldName
	 * @param locale
	 * @return
	 */
	@Override
	public List<TLocalizedResourcesBean> getLocalizedResourcesForTypeAndPrimaryKeys(
			String fieldName, List<Integer> primaryKeys, String locale) {
		List<TLocalizedResources> localizedResourceList = null;
		Criteria crit = new Criteria();
		if (primaryKeys!=null && !primaryKeys.isEmpty()) {
			//some entities (like release might not have primary keys)
			crit.addIn(PRIMARYKEYVALUE, primaryKeys);
		}
		if (fieldName!=null && !"".equals(fieldName)) {
			crit.add(FIELDNAME, fieldName);
		}
		if (locale==null) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, locale);
		}
		try {
			localizedResourceList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading all localized strings for fieldName " + 
					fieldName  + " primaryKeys " + primaryKeys.size() + " and locale " + locale + " failed with: " + e);
		}
		return convertTorqueListToBeanList(localizedResourceList);
	}
	
	/**
	 * Gets all localized strings for a fieldName, likeString, primaryKeys and for a locale
	 * If locale is not specified get the value for null locale
	 * @param fieldName
	 * @param likeString
	 * @param primaryKeys
	 * @param locale
	 * @return
	 */
	@Override
	public List<TLocalizedResourcesBean> getLocalizedResourcesForTypeAndLikeAndPrimaryKeys(
			String fieldName, String likeString, List<Integer> primaryKeys, String locale) {
		List<TLocalizedResources> localizedResourceList = null;
		Criteria crit = new Criteria();
		if (primaryKeys==null || primaryKeys.isEmpty()) {
			return new ArrayList<TLocalizedResourcesBean>();
		}		
		crit.add(FIELDNAME, fieldName);	
		crit.addIn(PRIMARYKEYVALUE, primaryKeys);
		if (locale==null) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, locale);
		}
		if (likeString!=null && !"".equals(likeString)) {
			crit.add(LOCALIZEDTEXT, (Object)likeString, Criteria.LIKE);
			crit.getCriterion(LOCALIZEDTEXT).setIgnoreCase(true);
		}
		try {
			localizedResourceList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading all localized strings for fieldName " + 
					fieldName + "likeString " + likeString + " primaryKeys " + 
					primaryKeys.size() + " and locale " + locale + " failed with: " + e);
		}
		return convertTorqueListToBeanList(localizedResourceList);
	}
	
	/**
	 * Gets all localized strings for a locale
	 * If locale is not specified get the value for null locale
	 * @param locale
	 * @return
	 */
	@Override
	public List<TLocalizedResourcesBean> getLocalizedResourcesForLocale(String locale) {
		Criteria crit = new Criteria();
		if (locale==null) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, locale);
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all localized strings for locale " + locale + " failed with: " + e);
			return null;
		}
	}
		
	
	/**
	 * Gets the localized resources for a locale
	 * If locale is not specified get the value for null locale
	 * @param locale
	 * @param withPrimaryKey whether to get the localized entities (withPrimaryKey = true) or the UI label resources
	 * @return
	 */  
	 @Override
	 public List<TLocalizedResourcesBean> getResourcesForLocale(String locale, boolean withPrimaryKey) {
		Criteria crit = new Criteria();
		if (locale==null) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, locale);
		}
		if (withPrimaryKey) {
			 crit.add(PRIMARYKEYVALUE, (Object)null, Criteria.ISNOTNULL);
		} else {
			 crit.add(PRIMARYKEYVALUE, (Object)null, Criteria.ISNULL);
		}
		crit.addAscendingOrderByColumn(FIELDNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all localized UI strings for locale " + locale + " failed with: " + e);
			return null;
		}
	 }
	 
	 /**
		 * Gets all localized entries for a fieldName like and for a locale  
		 * If locale is not specified get the value for null locale
		 * @param fieldNameLike
		 * @param locale
		 * @return
		 */
	@Override
	public List<TLocalizedResourcesBean> getUIResourcesForFieldNameLike(String fieldNameLike, String locale) {
		Criteria crit = new Criteria();
		crit.add(FIELDNAME, (Object)fieldNameLike, Criteria.LIKE);
		if (locale==null) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, locale);
		}
		crit.add(PRIMARYKEYVALUE, (Object)null, Criteria.ISNULL);
		crit.addAscendingOrderByColumn(FIELDNAME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all localized UI strings for fieldNames " + 
				fieldNameLike + " and locale " + locale + " failed with: " + e);
			return null;
		}
	}
	 
	  /**
	   * Gets the localized UI entries for a list of fieldNames and for a locale  
	   * If locale is not specified get the value for null locale
	   * @param fieldNameLike
	   * @param locale
	   * @return
	   */
	  @Override
	  public List<TLocalizedResourcesBean> getUIResourcesForFieldNames(List<String> fieldNames, String locale) {
		if (fieldNames==null || fieldNames.isEmpty()) {
			return new LinkedList<TLocalizedResourcesBean>();
		}
		Criteria crit = new Criteria();
		crit.addIn(FIELDNAME, fieldNames);
		if (locale==null) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, locale);
		}
		crit.add(PRIMARYKEYVALUE, (Object)null, Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all localized UI strings for fieldNames " + 
					fieldNames.size() + " and locale " + locale + " failed with: " + e);
			return null;
		}
	
	}
	
	/**
	 * Saves a localizedResourcesBean in the database
	 * @param localizedResourcesBean 
	 * @return
	 */
	@Override
	public  Integer save(TLocalizedResourcesBean localizedResourcesBean) {
		if (localizedResourcesBean!=null) {
		try {
			TLocalizedResources localizedResources = 
				BaseTLocalizedResources.createTLocalizedResources(localizedResourcesBean);
			localizedResources.save();
			return localizedResources.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving the localizedResourcesBean with fieldName " + localizedResourcesBean.getFieldName() + 
					" locale " + localizedResourcesBean.getLocale() +
					" localized text " + localizedResourcesBean.getLocalizedText() +
					" primary key " + localizedResourcesBean.getPrimaryKeyValue() +
					"  failed with " + e.getMessage(), e);
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Saves a localizedResourcesBean in the database without commit
	 * @param localizedResourcesBean 
	 * @return
	 */
	@Override
	public  Integer saveNoCommit(TLocalizedResourcesBean localizedResourcesBean, Connection con) {
		if (localizedResourcesBean!=null) {
		try {
			TLocalizedResources localizedResources = 
				BaseTLocalizedResources.createTLocalizedResources(localizedResourcesBean);
			localizedResources.save(con);
			return localizedResources.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving the localizedResourcesBean with fieldName " + localizedResourcesBean.getFieldName() + 
					" locale " + localizedResourcesBean.getLocale() +
					" localized text " + localizedResourcesBean.getLocalizedText() +
					" primary key " + localizedResourcesBean.getPrimaryKeyValue() +
					"  failed with " + e.getMessage(), e);
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Inserts a localizedResourcesBean in the database
	 * @param localizedResourcesBean 
	 * @return
	 */
	@Override
	public Integer insert(TLocalizedResourcesBean localizedResourcesBean) {
		if (localizedResourcesBean!=null) {
			try {
				TLocalizedResources localizedResources = 
					BaseTLocalizedResources.createTLocalizedResources(localizedResourcesBean);
				//db = Torque.getConnection(DATABASE_NAME);
				// it's the same name for all tables here, so we don't care
				doInsert(localizedResources/*, db*/);
				return localizedResources.getObjectID();
			} catch (Exception e) {
				LOGGER.error("Inserting the localizedResourcesBean with fieldName " + localizedResourcesBean.getFieldName() + 
						" locale " + localizedResourcesBean.getLocale() +
						" localized text " + localizedResourcesBean.getLocalizedText() +
						" primary key " + localizedResourcesBean.getPrimaryKeyValue() +
						"  failed with " + e.getMessage(), e);
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Inserts a localizedResourcesBean in the database
	 * @param localizedResourcesBean 
	 * @return
	 */
	@Override
	public Integer insertNoCommit(TLocalizedResourcesBean localizedResourcesBean, Connection con) {
		if (localizedResourcesBean!=null) {
			try {
				TLocalizedResources localizedResources = 
					BaseTLocalizedResources.createTLocalizedResources(localizedResourcesBean);
				
				localizedResources.save(con);
				
				return localizedResources.getObjectID();
			} catch (Exception e) {
				LOGGER.error("Inserting the localizedResourcesBean with fieldName " + localizedResourcesBean.getFieldName() + 
						" locale " + localizedResourcesBean.getLocale() +
						" localized text " + localizedResourcesBean.getLocalizedText() +
						" primary key " + localizedResourcesBean.getPrimaryKeyValue() +
						"  failed with " + e.getMessage(), e);
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Deletes a localizedResourcesBean from the database
	 * @param resourceID 
	 * @return
	 */
	@Override
	public void delete(Integer resourceID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, resourceID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting a localized reource for key " + resourceID + " failed with: " + e);
		}
	}
	
	/**
	 * Gets the TLocalizedResourcesBean for a fieldName, and key for all locales
	 * @param fieldName
	 * @param key	
	 * @return
	 */
	@Override
	public void deleteLocalizedResourcesForFieldNameLikeAndKey(String fieldName, Integer key) {
		Criteria crit = new Criteria();
		crit.add(FIELDNAME, (Object)fieldName, Criteria.LIKE);
		if (key==null) {
			crit.add(PRIMARYKEYVALUE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(PRIMARYKEYVALUE, key);
		}
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleteing the localized string for fieldName " + 
					fieldName+ " key " + key + " failed with: " + e);
		}
	}
	
	/**
	 * Gets the TLocalizedResourcesBean for a fieldName for all locales
	 * @param fieldName 
	 * @return
	 */
	@Override
	public void deleteLocalizedResourcesForFieldName(String fieldName) {
		Criteria crit = new Criteria();
		crit.add(FIELDNAME, (Object)fieldName, Criteria.LIKE);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleteing the localized string for fieldName " + 
					fieldName+" failed with: " + e);
		}
	}
	
	/**
	 * Deletes the TLocalizedResourcesBean for a fieldName, and key for all locales
	 * @param fieldName
	 * @param key
	 * @return
	 */
	@Override
	public void deleteLocalizedResourcesForFieldNameAndKey(String fieldName, Integer key) {
		Criteria crit = new Criteria();
		crit.add(FIELDNAME, fieldName);
		if (key==null) {
			crit.add(PRIMARYKEYVALUE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(PRIMARYKEYVALUE, key);
		}
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleteing the localized string for fieldName " + 
					fieldName+ " key " + key + " failed with: " + e);
		}
	}
	
	/**
	 * Deletes the TLocalizedResourcesBean for a fieldName, key and locale
	 * @param fieldName
	 * @param key
	 * @param localeStr
	 * @return
	 */
	@Override
	public void deleteLocalizedResourcesForFieldNameAndKeyAndLocale(String fieldName, Integer key, String localeStr) {
		Criteria crit = new Criteria();
		crit.add(FIELDNAME, (Object)fieldName, Criteria.LIKE);
		if (key==null) {
			crit.add(PRIMARYKEYVALUE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(PRIMARYKEYVALUE, key);
		}
		if (localeStr==null || "".equals(localeStr)) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, localeStr);
		}
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleteing the localized string for fieldName " + 
					fieldName+ " key " + key + " failed with: " + e);
		}
	}
	
	/**
	 * Deletes all resources for a locale 
	 * @param localeStr
	 */
	@Override
	public void deleteAllForLocale(String localeStr) {
		Criteria crit = new Criteria();
		if (localeStr==null || "".equals(localeStr)) {
			crit.add(LOCALE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(LOCALE, localeStr);
		}
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleteing the localized string for locale " + 
					localeStr + " failed with: " + e);
		}
	}
	
	/**
	 * Converts a list of TLocalizedResources torque objects to a list of TLocalizedResourcesBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TLocalizedResourcesBean> convertTorqueListToBeanList(List<TLocalizedResources> torqueList) {
		List<TLocalizedResourcesBean> beanList = new LinkedList<TLocalizedResourcesBean>();		
		if (torqueList!=null) {
			for (TLocalizedResources tLocalizedResources : torqueList) {
				beanList.add(tLocalizedResources.getBean());
			}
		}
		return beanList;
	}
}
