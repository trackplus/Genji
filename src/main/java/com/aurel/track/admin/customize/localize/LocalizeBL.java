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

package com.aurel.track.admin.customize.localize;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Transaction;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.beans.ILocalizedLabelBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TLocalizedResourcesBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.LocalizedResourcesDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.listFields.LocalizedListIndexer;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.LocaleHandler;

/**
 * Business logic for implicitly loading/saving/deleting the localized texts 
 * @author Tamas
 *
 */
public class LocalizeBL {

	private static final Logger LOGGER = LogManager.getLogger(LocalizeBL.class);
	private static LocalizedResourcesDAO localizedResourcesDAO = DAOFactory.getFactory().getLocalizedResourcesDAO();
	
	public static String DOT = ".";
	/**
	 * All hardcoded resource types are negative because there can be custom list with any positive listID
	 * @author Tamas Ruff
	 */
	public static interface RESOURCE_TYPES {
		public static int FIELD_LABEL = -51;
		public static int FIELD_TOOLTIP = -52;
		public static int LINK_NAME = -53;		
		public static int ROLE = -55;
		public static int BASKET = -56;
		public static int ACTION = -57;
		public static int COCKPIT_TEMPLATE = -58;
		public static int USER_LEVEL = -59;
		public static int PROJECT_STATUS = -60;
		public static int RELEASE_STATUS = -61;
		public static int ACCOUNT_STATUS = -62;
		public static int FILTER_LABEL = -63;
		public static int FILTER_TOOLTIP = -64;	
		public static int ISSUETYPE = -SystemFields.ISSUETYPE;//-2
		public static int STATUS = -SystemFields.STATE;//-4
		public static int PRIORITY = -SystemFields.PRIORITY;//-10
		public static int SEVERITY = -SystemFields.SEVERITY;//-11
	}

	/**
	 * Resource keys for locale editor
	 * @author Tamas
	 *
	 */
	public static interface RESOURCE_TYPE_KEYS {
		public static String FIELD_LABEL_KEY = "admin.customize.localeEditor.type.fieldLabel";
		public static String FIELD_TOOLTIP_KEY = "admin.customize.localeEditor.type.fieldTooltip";
		public static String FILTER_LABEL_KEY = "admin.customize.localeEditor.type.filterLabel";
		public static String FILTER_TOOLTIP_KEY = "admin.customize.localeEditor.type.filterTooltip";
		public static String LINK_NAME_KEY = "admin.customize.localeEditor.type.link";
		public static String ROLE_KEY = "admin.customize.localeEditor.type.role";
		public static String USER_LEVEL_KEY = "admin.customize.localeEditor.type.userLevel";
		public static String BASKET_KEY = "admin.customize.localeEditor.type.basket";
		public static String ACTION_KEY = "admin.customize.localeEditor.type.action";
		public static String PROJECT_STATUS_KEY = "admin.customize.localeEditor.type.projectStatus";
		public static String RELEASE_STATUS_KEY = "admin.customize.localeEditor.type.releaseStatus";
		public static String ACCOUNT_STATUS_KEY = "admin.customize.account.lbl.accountStatus";
		public static String COCKPIT_TEMPLATES_KEY = "admin.customize.localeEditor.type.cockpitTemplate";
		
		public static String DB_ENTITY = "admin.customize.localeEditor.category.dbEntity";
		public static String LISTS = "menu.admin.custom.list";
		public static String GLOBAL_LISTS = ListBL.RESOURCE_KEYS.GLOBAL_LISTS;
		public static String PROJECT_LISTS = ListBL.RESOURCE_KEYS.PROJECT_LISTS;
		public static String SYSTEM_STATUS = "menu.admin.custom.objectStatus";
	
		public static String UI_TEXT = "admin.customize.localeEditor.category.uiText";
		public static String CUSTOM_LISTS = "admin.customize.localeEditor.subcategory.customLists";
		public static String FIELDS = "admin.customize.localeEditor.subcategory.fields";
		public static String FILTERS = "admin.customize.localeEditor.subcategory.filters";
		public static String OTHER = "admin.customize.localeEditor.subcategory.other";
		public static String COMMON = "admin.customize.localeEditor.subcategory.common";
		public static String FORMS = "admin.customize.localeEditor.subcategory.forms";
	}

	/**
	 * Main category: either DB entity or UI text
	 * @author Tamas
	 *
	 */
	public static interface RESOURCE_CATEGORIES {
		public static int DB_ENTITY = 1;
		public static int UI_TEXT = 2;
	}

	/**
	  * Gets the localized UI entries for a list of fieldNames and for a locale  
	  * If locale is not specified get the value for null locale
	  * @param fieldNameLike
	  * @param locale
	  * @return
	  */
	public static List<TLocalizedResourcesBean> getUIResourcesForFieldNames(List<String> fieldNames, String locale) {
		return localizedResourcesDAO.getUIResourcesForFieldNames(fieldNames, locale);
	}
	 
	 
	/**
	 * Gets the localized resources for a locale
	 * If locale is not specified get the value for null locale
	 * @param locale
	 * @param withPrimaryKey whether to get the localized entities (withPrimaryKey = true) or the UI label resources
	 * @return
	 */	
	public static List<TLocalizedResourcesBean> getResourcesForLocale(String locale, boolean withPrimaryKey) {
		return localizedResourcesDAO.getResourcesForLocale(locale, withPrimaryKey);
	}
	
	/**
	 * Gets all localized resources for a locale
	 * @param locale
	 * @return
	 */
	public static List<TLocalizedResourcesBean> getLocalizedResourcesForLocale(String locale) {
		return localizedResourcesDAO.getLocalizedResourcesForLocale(locale);
	}
	
	/**
	 * Gets all localized strings for a fieldName a likePrefixString and for a locale
	 * If locale is not specified get the value for null locale
	 * @param likeString
	 * @param locale
	 * @return
	 */
	public static List<TLocalizedResourcesBean> getLocalizedResourcesForTypeAndPrefixLike(String fieldName, String likeString, String locale) {
		return localizedResourcesDAO.getLocalizedResourcesForTypeAndPrefixLike(fieldName, likeString, locale);
	}
	
	/**
	 * Gets all localized UI entries for a fieldName like and for a locale  
	 * If locale is not specified get the value for null locale
	 * @param fieldNameLike
	 * @param locale
	 * @return
	 */
	public static List<TLocalizedResourcesBean> getUIResourcesForFieldNameLike(String fieldNameLike, String locale) {
		return localizedResourcesDAO.getUIResourcesForFieldNameLike(fieldNameLike, locale);
	}
	 
	/**
	 * Gets all localized strings for a fieldName and for a locale
	 * If locale is not specified get the value for null locale
	 * @param fieldName
	 * @param locale
	 * @return
	 */
	public static List<TLocalizedResourcesBean> getLocalizedResourcesForType(String fieldName, String locale) {
		return localizedResourcesDAO.getLocalizedResourcesForType(fieldName, locale);
	}
	
	/**
	 * Gets the TLocalizedResourcesBean for a fieldName, key and the locale string 
	 * @param fieldName
	 * @param key
	 * @param locale
	 * @return
	 */
	public static List<TLocalizedResourcesBean> getLocalizedResourceForKey(String fieldName, Integer key, String locale) {
		return localizedResourcesDAO.getLocalizedResourceForKey(fieldName, key, locale);
	}
	
	/**
	 * Gets all localized strings for a fieldName, primaryKeys and for a locale
	 * If locale is not specified get the value for null locale
	 * @param fieldName
	 * @param locale
	 * @return
	 */
	public static List<TLocalizedResourcesBean> getLocalizedResourcesForTypeAndPrimaryKeys(String fieldName, List<Integer> primaryKeys, String locale) {
		return localizedResourcesDAO.getLocalizedResourcesForTypeAndPrimaryKeys(fieldName, primaryKeys, locale);
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
	public static List<TLocalizedResourcesBean> getLocalizedResourcesForTypeAndLikeAndPrimaryKeys(
			String fieldName, String likeString, List<Integer> primaryKeys, String locale) {
		return localizedResourcesDAO.getLocalizedResourcesForTypeAndLikeAndPrimaryKeys(fieldName, likeString, primaryKeys, locale);
	}
	
	/**
	 * Set the same value also in the TLocalizedResources table with null (or for now "" (mandatory)) locale.  
	 * The localization will be loaded from this table, and if not found, 
	 * from the localized property files, and if not found there from the "native" table. 
	 * The problem is that the correspondence is made through the key (which do not change) instead of values 
	 * and localized property files use the same key for existing entries and then the
	 * renamed label is not visible because the localized property files have priority over those from the "native" table.
	 * This intermediate step is introduced to solve the problem until this localisation 
	 * in database will be implemented also in the Web-Interface.
	 * @param fieldName
	 * @param primaryKey
	 * @param label
	 * @param locale
	 */
	public static void saveLocalizedResource(String fieldName, Integer primaryKey, String label, Locale locale) {
		String localeString = null;
		if (locale!=null) {
			localeString = locale.toString();
		}
		List<TLocalizedResourcesBean> localizedResources = localizedResourcesDAO.getLocalizedResourceForKey(fieldName, primaryKey, localeString);
		save(label, fieldName, primaryKey, localizedResources, localeString);
	}

	/**
	 * Saves a localization entry
	 * @param newLocalizedText
	 * @param fieldName
	 * @param primaryKey
	 * @param originalLocalizedResources
	 * @param localeStr
	 */
	public static void save(String newLocalizedText, String fieldName, 
			Integer primaryKey, List<TLocalizedResourcesBean> originalLocalizedResources, String localeStr) {
		String oldLocalizedText = null;
		TLocalizedResourcesBean localizedResourceBean = null;
		if (originalLocalizedResources!=null && !originalLocalizedResources.isEmpty()) {
			if (originalLocalizedResources.size()>1) {
				//theoretically the list should contain maximal 1 TLocalizedResourcesBean,
				//but it can be that (previous buggy import) there are more than one. The duplicates should be removed
				localizedResourcesDAO.deleteLocalizedResourcesForFieldNameAndKeyAndLocale(fieldName, primaryKey, localeStr);
			} else {
				localizedResourceBean = originalLocalizedResources.get(0);
				if (localizedResourceBean!=null) {
					oldLocalizedText = localizedResourceBean.getLocalizedText();
				}
			}
		}
		boolean newSpecified = newLocalizedText!=null && !"".equals(newLocalizedText.trim());
		boolean oldSpecified = oldLocalizedText!=null && !"".equals(oldLocalizedText.trim());
		boolean saveNeeded = false;
		if (newSpecified && oldSpecified) {
			//both exist
			if (EqualUtils.notEqual(newLocalizedText, oldLocalizedText)) {
				localizedResourceBean.setLocalizedText(newLocalizedText);
				localizedResourceBean.setTextChangedBool(true);
				localizedResourcesDAO.save(localizedResourceBean);
				saveNeeded = true;
			}
		} else {
			if (newSpecified && !oldSpecified) {
				//not existed before or it was empty string
				if (localizedResourceBean==null) {
					//not existed
					localizedResourceBean = new TLocalizedResourcesBean();
					localizedResourceBean.setPrimaryKeyValue(primaryKey);
					localizedResourceBean.setFieldName(fieldName);
					localizedResourceBean.setTextChangedBool(true);
					localizedResourceBean.setLocale(localeStr);
				}
				localizedResourceBean.setLocalizedText(newLocalizedText);
				localizedResourceBean.setTextChangedBool(true);
				localizedResourcesDAO.save(localizedResourceBean);
				saveNeeded = true;
			} else {
				if (!newSpecified && oldSpecified) {
					if (localizedResourceBean!=null) {
						//remove if new is null or empty
						localizedResourcesDAO.delete(localizedResourceBean.getObjectID());
						saveNeeded = true;
					}
				}
			}
		}
		if (saveNeeded) {
			//FIXME: clear cache?
			ResourceBundle.clearCache();
			if (primaryKey!=null) {
				//actualize the lucene index with the changed localization
				if (fieldName.startsWith(LocalizationKeyPrefixes.FIELD_SYSTEMSELECT_KEY_PREFIX)) {
					int beginIndex = LocalizationKeyPrefixes.FIELD_SYSTEMSELECT_KEY_PREFIX.length();
					String fieldIDStr = fieldName.substring(beginIndex, beginIndex+1);
					Integer fieldID = null;
					try {
						fieldID = Integer.valueOf(fieldIDStr);
					} catch (Exception e) {
					}
					if (fieldID!=null) {
						int luceneField = 0;
						ILocalizedLabelBean localizedLabelBean = null;
						switch (fieldID.intValue()) {
						case SystemFields.ISSUETYPE:
							luceneField = LuceneUtil.LOOKUPENTITYTYPES.LISTTYPE;
							localizedLabelBean = LookupContainer.getItemTypeBean(primaryKey);
							break;
						case SystemFields.STATE:
							luceneField = LuceneUtil.LOOKUPENTITYTYPES.STATE;
							localizedLabelBean = LookupContainer.getStatusBean(primaryKey);
							break;
						case SystemFields.PRIORITY:
							luceneField = LuceneUtil.LOOKUPENTITYTYPES.PRIORITY;
							localizedLabelBean = LookupContainer.getPriorityBean(primaryKey);
							break;
						case SystemFields.SEVERITY:
							luceneField = LuceneUtil.LOOKUPENTITYTYPES.SEVERITY;
							localizedLabelBean = LookupContainer.getSeverityBean(primaryKey);
							break;
						}
						if (localizedLabelBean!=null) {
							LocalizedListIndexer.getInstance().updateLabelBean(localizedLabelBean, luceneField);
						}
					}
				} else {
					if (fieldName.startsWith(LocalizationKeyPrefixes.FIELD_CUSTOMSELECT_KEY_PREFIX)) {
						TOptionBean optionBean = OptionBL.loadByPrimaryKey(primaryKey);
						if (optionBean!=null) {
							LocalizedListIndexer.getInstance().updateLabelBean(optionBean, LuceneUtil.LOOKUPENTITYTYPES.CUSTOMOPTION);
						}
					}
				}
			}
		}
	}

	/**
	 * Deletes all resources for a locale 
	 * @param localeStr
	 */
	public static void deleteAllForLocale(String localeStr) {
		localizedResourcesDAO.deleteAllForLocale(localeStr);
	}
	
	/**
	 * Deletes the TLocalizedResourcesBean for a fieldName for all locales
	 * @param fieldName 
	 * @return
	 */
	public static void deleteLocalizedResourcesForFieldName(String fieldName) {
		localizedResourcesDAO.deleteLocalizedResourcesForFieldName(fieldName);
	}
	
	/**
	 * Remove the localization for  an entity (typically by deleting the entity)
	 * @param fieldName
	 * @param objectID
	 */
	public static void removeLocalizedResources(String fieldName, Integer objectID) {
		localizedResourcesDAO.deleteLocalizedResourcesForFieldNameAndKey(fieldName, objectID);
	}

	public static void saveCustomFieldLocalizedResource(int customList, Integer primaryKey, String label, Locale locale) {
		saveLocalizedResource(LocalizeBL.getCustomListFieldName(customList), primaryKey, label, null);
		saveLocalizedResource(LocalizeBL.getCustomListFieldName(customList), primaryKey, label, locale);
	}

	/**
	 * Set the same value also in the TLocalizedResources table with null (or for now "" (mandatory)) locale.  
	 * The localization will be loaded from this table, and if not found, 
	 * from the localized property files, and if not found there from the "native" table. 
	 * The problem is that the correspondence is made through the key (which do not change) instead of values 
	 * and localized property files use the same key for existing entries and then the
	 * renamed label is not visible because the localized property files have priority over those from the "native" table.
	 * This intermediate step is introduced to solve the problem until this localisation 
	 * in database will be implemented also in the Web-Interface.
	 * @param systemField
	 * @param primaryKey
	 * @param label
	 */
	public static void saveSystemFieldLocalizedResource(int systemField, Integer primaryKey, String label, Locale locale) {
		saveLocalizedResource(LocalizeBL.getSystemListFieldName(systemField), primaryKey, label, null);
		saveLocalizedResource(LocalizeBL.getSystemListFieldName(systemField), primaryKey, label, locale);
	}

	public static void saveSystemStateLocalizedResource(int systemState, Integer primaryKey, String label, Locale locale) {
		saveLocalizedResource(LocalizeBL.getSystemStateListFieldName(systemState), primaryKey, label, null);
		saveLocalizedResource(LocalizeBL.getSystemStateListFieldName(systemState), primaryKey, label, locale);
	}

	/**
	 * 
	 * @param inputStream
	 * @param locale
	 * @param overwrite - overwrite values modified from the user interface. This makes only sense
	 *		during development
	 * @param importTarget - RESOURCE_CATEGORIES.DB_ENTITY or RESOURCE_CATEGORIES.UI_TEXT
	 * @param initBoxResources do not overwrite box resources (status, item type by upgrade)
	 */
	public static void saveResources(InputStream inputStream, String locale, boolean overwrite, Integer importTarget, boolean initBoxResources) {
		OrderedProperties properties = new OrderedProperties();
		try {
			properties.load(inputStream);
			boolean withPrimaryKey = false;
			if (importTarget!=null && importTarget.intValue()==LocalizeBL.RESOURCE_CATEGORIES.DB_ENTITY) {
				withPrimaryKey = true;
			}
			LocalizeBL.uploadResources(properties, locale, overwrite, withPrimaryKey, initBoxResources);
		} catch (IOException e) {
			LOGGER.warn("Loading the resource properties from classpath failed with " + e.getMessage(), e);
		}
		finally {
			//flush and close the streams
			if (inputStream!=null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Gets the primary key by localized text
	 * TODO do not get always from the database but make a cache also for the "inverse" maps
	 * @param fieldName
	 * @param localizedText
	 * @param locale
	 * @return
	 */
	public static Integer getPrimaryKey(String fieldName, String localizedText, Locale locale) {
		String localeString = null; 
		if (locale!=null) {
			localeString = locale.toString();
		}
		TLocalizedResourcesBean localizedResourcesBean = localizedResourcesDAO.getKeyForLocalizedResource(fieldName, localizedText, localeString);
		if (localizedResourcesBean!=null) {
			return localizedResourcesBean.getPrimaryKeyValue();
		}
		if (locale!=null) {
			localizedResourcesBean = localizedResourcesDAO.getKeyForLocalizedResource(fieldName, localizedText, null);
			if (localizedResourcesBean!=null) {
				return localizedResourcesBean.getPrimaryKeyValue();
			}	
		}
		return null;
	}

	/**
	 * Gets the locales with at least one entry from the database
	 * @return
	 */
	public static Set<String> getExistingLocales() {
		return localizedResourcesDAO.getExistingLocales();
	}

	public static String getCustomListFieldName(int listID) { 
		return LocalizationKeyPrefixes.FIELD_CUSTOMSELECT_KEY_PREFIX + listID + LocalizeBL.DOT;
	}

	

	/**
	 * Update the resources based on the properties
	 * @param properties
	 * @param strLocale
	 * @param overwrite whether to overwrite the edited resources
	 * @param withPrimaryKey whether the used to be BoxResources are imported or the ApplicationResources 
	 */
	static synchronized void uploadResources(Properties properties, String strLocale, boolean overwrite, boolean withPrimaryKey, boolean initBoxResources) {
		List<TLocalizedResourcesBean> resourceBeans = getResourcesForLocale(strLocale, withPrimaryKey);
		SortedMap<String, List<TLocalizedResourcesBean>> existingLabels = new TreeMap<String, List<TLocalizedResourcesBean>>();
		for (TLocalizedResourcesBean localizedResourcesBean : resourceBeans) {
			String key = localizedResourcesBean.getFieldName();
			if (withPrimaryKey) {
				if (key.startsWith(LocalizationKeyPrefixes.FIELD_LABEL_KEY_PREFIX) || key.startsWith(LocalizationKeyPrefixes.FIELD_TOOLTIP_KEY_PREFIX)) {
					key+=".";
				}
				key=key+localizedResourcesBean.getPrimaryKeyValue();
			}
			List<TLocalizedResourcesBean> localizedResources = existingLabels.get(key);
			if (localizedResources==null) {
				localizedResources = new LinkedList<TLocalizedResourcesBean>();
				existingLabels.put(key, localizedResources);
			}
			localizedResources.add(localizedResourcesBean);
		}
		Enumeration propertyNames = properties.keys();
		Connection connection = null;
		try {
			connection = Transaction.begin();
			connection.setAutoCommit(false);
			while (propertyNames.hasMoreElements()) {
				String key = (String)propertyNames.nextElement();
				String localizedText = LocalizeBL.correctString(properties.getProperty(key));
				Integer primaryKey = null;
				String fieldName = null;
				if (withPrimaryKey) {
					int primaryKeyIndex = key.lastIndexOf(".");
					if (primaryKeyIndex!=-1) {
						try {
							primaryKey = Integer.valueOf(key.substring(primaryKeyIndex+1));
						} catch (Exception e) {
							LOGGER.warn("The last part after . can't be converted to integer " + e.getMessage(), e);
						}
						fieldName = key.substring(0, primaryKeyIndex+1);
						if (fieldName.startsWith(LocalizationKeyPrefixes.FIELD_LABEL_KEY_PREFIX) || fieldName.startsWith(LocalizationKeyPrefixes.FIELD_TOOLTIP_KEY_PREFIX)) {
							//do not have . at the end (legacy)
							fieldName = fieldName.substring(0, fieldName.length()-1);
						}
					}
				} else {
					fieldName = key;
				}
				List<TLocalizedResourcesBean> localizedResources = existingLabels.get(key);
				if (localizedResources!=null && localizedResources.size()>1) {
					//remove all duplicates (as a consequence of previous erroneous imports)
					localizedResourcesDAO.deleteLocalizedResourcesForFieldNameAndKeyAndLocale(fieldName, primaryKey, strLocale);
					localizedResources = null;
				}
				if (localizedResources==null || localizedResources.isEmpty()) {
					//new resource
					TLocalizedResourcesBean localizedResourcesBean = new TLocalizedResourcesBean();
					localizedResourcesBean.setFieldName(fieldName);
					localizedResourcesBean.setPrimaryKeyValue(primaryKey);
					localizedResourcesBean.setLocalizedText(localizedText);
					localizedResourcesBean.setLocale(strLocale);
					localizedResourcesDAO.insert(localizedResourcesBean);
				} else {
					//existing resource
					if (localizedResources.size()==1) {
						TLocalizedResourcesBean localizedResourcesBean = localizedResources.get(0);
						if (EqualUtils.notEqual(localizedText, localizedResourcesBean.getLocalizedText()) /*&& locale.equals(localizedResourcesBean.getLocale())*/) {
							//text changed locally by the customer
							boolean textChanged = localizedResourcesBean.getTextChangedBool();
							if ((textChanged==false && !initBoxResources) || (textChanged && overwrite)) {
								localizedResourcesBean.setLocalizedText(localizedText);
								localizedResourcesDAO.save(localizedResourcesBean);
							}
						} else {
						}
					}
				}
			}
			Transaction.commit(connection);
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			LOGGER.error("Problem filling locales: " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			if (connection != null) {
				Transaction.safeRollback(connection);
			}
		}
		//delete the existing ones not found in the properties
		Locale locale;
		if (strLocale != null) {
			locale = LocaleHandler.getLocaleFromString(strLocale);
		} else {
			locale = Locale.getDefault();
		}
		if (withPrimaryKey) {
			//not sure that all is needed but to be sure we do not miss imported localizations
			LookupContainer.resetLocalizedLookupMap(SystemFields.INTEGER_ISSUETYPE, locale);
			LookupContainer.resetLocalizedLookupMap(SystemFields.INTEGER_STATE, locale);
			LookupContainer.resetLocalizedLookupMap(SystemFields.INTEGER_PRIORITY, locale);
			LookupContainer.resetLocalizedLookupMap(SystemFields.INTEGER_SEVERITY, locale);
		}
		ResourceBundle.clearCache();
	}

	/**
	 * Gets the field names for system fields
	 * @param type
	 * @return
	 */
	public static String getSystemListFieldName(Integer type) {
		switch (type.intValue()) {
		case RESOURCE_TYPES.ISSUETYPE:
			return new TListTypeBean().getKeyPrefix();
		case RESOURCE_TYPES.STATUS:
			return new TStateBean().getKeyPrefix();
		case RESOURCE_TYPES.PRIORITY:
			return new TPriorityBean().getKeyPrefix();
		case RESOURCE_TYPES.SEVERITY:
			return new TSeverityBean().getKeyPrefix();
		case RESOURCE_TYPES.PROJECT_STATUS:
			return new TSystemStateBean().getKeyPrefix() + TSystemStateBean.ENTITYFLAGS.PROJECTSTATE + DOT;
		case RESOURCE_TYPES.RELEASE_STATUS:
			return new TSystemStateBean().getKeyPrefix() + TSystemStateBean.ENTITYFLAGS.RELEASESTATE + DOT;
		case RESOURCE_TYPES.ACCOUNT_STATUS:
			return new TSystemStateBean().getKeyPrefix() + TSystemStateBean.ENTITYFLAGS.ACCOUNTSTATE + DOT;
		default: 
			return "";
		}
	}

	public static String getSystemStateListFieldName(int listID) { 
		return LocalizationKeyPrefixes.SYSTEM_STATUS_KEY_PREFIX + listID + DOT;
	}

	static String correctString(String str) {
		StringBuffer tmp = new StringBuffer();
		char[] ch = str.toCharArray();
		for (int i =0; i < ch.length; i++){
			if (ch[i] =='\n')
				tmp.append("\n");
			else if (ch[i] == '\t')
				tmp.append("\t");
			else if (ch[i] == '\r')
				tmp.append("\r");
			else if (ch[i] == '\f')
				tmp.append("\f");
			else if (ch[i] == '\\')
				tmp.append("\\");
	
			else  {
				tmp.append(ch[i]);
			}
		}
		return tmp.toString();
	}

}
