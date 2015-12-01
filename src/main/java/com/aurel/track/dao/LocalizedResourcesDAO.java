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


package com.aurel.track.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

import com.aurel.track.beans.TLocalizedResourcesBean;

/**
 * DAO for LocalizedResources 
 * @author Tamas Ruff
 *
 */
public interface LocalizedResourcesDAO {
	
	/**
	 * Gets the locales used in the database
	 * @return
	 */
	Set<String> getExistingLocales();
	
	/**
	 * Gets the TLocalizedResourcesBean for a fieldName, key and the locale string 
	 * @param fieldName
	 * @param key
	 * @param locale
	 * @return
	 */
	List<TLocalizedResourcesBean> getLocalizedResourceForKey(String fieldName, Integer key, String locale);


	
	/**
	 * Gets the TLocalizedResourcesBean for a prefixString, localizedText and the locale string 
	 * @param fieldName
	 * @param localizedText
	 * @param locale
	 * @return
	 */
	TLocalizedResourcesBean getKeyForLocalizedResource(String fieldName, String localizedText, String locale);
	
	/**
	 * Gets all localized strings for a fieldName and for a locale
	 * If locale is not specified get the value for null locale
	 * @param fieldName
	 * @param locale
	 * @return
	 */
	List<TLocalizedResourcesBean> getLocalizedResourcesForType(String fieldName, String locale);
	

	/**
	 * Gets all localized strings for a fieldName a likePrefixString and for a locale
	 * If locale is not specified get the value for null locale
	 * @param likeString
	 * @param locale
	 * @return
	 */
	List<TLocalizedResourcesBean> getLocalizedResourcesForTypeAndPrefixLike(String fieldName, String likeString, String locale);
	
	/**
	 * Gets all localized strings for a fieldName, primaryKeys and for a locale
	 * If locale is not specified get the value for null locale
	 * @param fieldName
	 * @param locale
	 * @return
	 */
	List<TLocalizedResourcesBean> getLocalizedResourcesForTypeAndPrimaryKeys(String fieldName, List<Integer> primaryKeys, String locale);
	
	/**
	 * Gets all localized strings for a fieldName, likeString, primaryKeys and for a locale
	 * If locale is not specified get the value for null locale
	 * @param fieldName
	 * @param likeString
	 * @param primaryKeys
	 * @param locale
	 * @return
	 */
	List<TLocalizedResourcesBean> getLocalizedResourcesForTypeAndLikeAndPrimaryKeys(
			String fieldName, String likeString, List<Integer> primaryKeys, String locale);
	
	
	/**
	 * Gets all localized strings for a locale
	 * If locale is not specified get the value for null locale
	 * @param locale
	 * @return
	 */
	List<TLocalizedResourcesBean> getLocalizedResourcesForLocale(String locale);
	
	/**
	 * Gets the localized resources for a locale
	 * If locale is not specified get the value for null locale
	 * @param locale
	 * @param withPrimaryKey whether to get the localized entities (withPrimaryKey = true) or the UI label resources
	 * @return
	 */	
	List<TLocalizedResourcesBean> getResourcesForLocale(String locale, boolean withPrimaryKey);
	
	/**
	 * Gets all localized UI entries for a fieldName like and for a locale  
	 * If locale is not specified get the value for null locale
	 * @param fieldNameLike
	 * @param locale
	 * @return
	 */
	 List<TLocalizedResourcesBean> getUIResourcesForFieldNameLike(String fieldNameLike, String locale);
			
	 /**
	  * Gets the localized UI entries for a list of fieldNames and for a locale  
	  * If locale is not specified get the value for null locale
	  * @param fieldNameLike
	  * @param locale
	  * @return
	  */
	 List<TLocalizedResourcesBean> getUIResourcesForFieldNames(List<String> fieldNames, String locale);
	 
	/**
	 * Saves a localizedResourcesBean in the database
	 * @param localizedResourcesBean 
	 * @return
	 */
	Integer save(TLocalizedResourcesBean localizedResourcesBean);
	
	/**
	 * Saves a localizedResourcesBean in the database without commit
	 * @param localizedResourcesBean 
	 * @return
	 */
	Integer saveNoCommit(TLocalizedResourcesBean localizedResourcesBean, Connection con);

	/**
	 * Inserts a localizedResourcesBean in the database
	 * @param localizedResourcesBean 
	 * @return
	 */
	Integer insert(TLocalizedResourcesBean localizedResourcesBean);
	
	/**
	 * Inserts a localizedResourcesBean in the database without commit
	 * @param localizedResourcesBean 
	 * @return
	 */
	Integer insertNoCommit(TLocalizedResourcesBean localizedResourcesBean, Connection con);
	
	/**
	 * Deletes a localizedResourcesBean from the database
	 * @param resourceID 
	 * @return
	 */
	void delete(Integer resourceID);
	
	/**
	 * Deletes the TLocalizedResourcesBean for like a fieldName, and key for all locales
	 * @param fieldName
	 * @param key	
	 * @return
	 */
	void deleteLocalizedResourcesForFieldNameLikeAndKey(String fieldName, Integer key);
	
	/**
	 * Deletes the TLocalizedResourcesBean for a fieldName for all locales
	 * @param fieldName 
	 * @return
	 */
	void deleteLocalizedResourcesForFieldName(String fieldName);
	
	/**
	 * Deletes the TLocalizedResourcesBean for a fieldName, and key for all locales
	 * @param fieldName
	 * @param key
	 * @return
	 */
	void deleteLocalizedResourcesForFieldNameAndKey(String fieldName, Integer key);
	
	/**
	 * Deletes the TLocalizedResourcesBean for a fieldName, key and locale
	 * @param fieldName
	 * @param key
	 * @param localeStr
	 * @return
	 */
	void deleteLocalizedResourcesForFieldNameAndKeyAndLocale(String fieldName, Integer key, String localeStr);
	
	/**
	 * Deletes all resources for a locale 
	 * @param localeStr
	 */
	void deleteAllForLocale(String localeStr);
}

