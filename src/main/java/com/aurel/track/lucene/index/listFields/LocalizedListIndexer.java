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

package com.aurel.track.lucene.index.listFields;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ILocalizedLabelBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.LocaleHandler;

/**
 * Index the localized lookup lists
 * @author Tamas Ruff
 *
 */
public class LocalizedListIndexer extends InternalListIndexer {
	
	private static final Logger LOGGER = LogManager.getLogger(LocalizedListIndexer.class);
	private static LocalizedListIndexer instance;
	
	private Set<Locale> countrySet = new HashSet<Locale>();
	private Set<Locale> languageSet = new HashSet<Locale>();
	private Locale defaultLocale = getDefaultLocaleAndLocaleSets(countrySet, languageSet);
	
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static LocalizedListIndexer getInstance(){
		if(instance==null){
			instance=new LocalizedListIndexer();
			instance.countrySet = new HashSet<Locale>();
			instance.languageSet = new HashSet<Locale>();
			instance.defaultLocale = getDefaultLocaleAndLocaleSets(instance.countrySet, instance.languageSet);
		}
		return instance;
	}
	/**
	 * Gets the index writer ID
	 * @return
	 */
	protected int getIndexWriterID() {
		return LuceneUtil.INDEXES.LOCALIZED_LIST_INDEX;
	}
	/**
	 * Gets the field name of the combined key which is the unique identifier
	 * @return
	 */
	protected String getCombinedKeyFieldName() {
		return LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.COMBINEDKEY;
	}
	/**
	 * Gets the list field type for logging purposes  
	 * @return
	 */
	protected String getListFieldType() {
		return "localized";
	}
	/**
	 * Gets the fieldIDs stored in this index 
	 * @return
	 */
	protected List<Integer> getFieldIDs() {
		List<Integer> fieldIDs = new LinkedList<Integer>();
		fieldIDs.add(SystemFields.INTEGER_ISSUETYPE);
		fieldIDs.add(SystemFields.INTEGER_PRIORITY);
		fieldIDs.add(SystemFields.INTEGER_SEVERITY);
		fieldIDs.add(SystemFields.INTEGER_STATE);
		return fieldIDs;
	}
	
	/**
	 * Gets the custom listIDs stored in this index 
	 * @return
	 */
	protected List<TListBean> getCustomListBeans() {
		return ListBL.loadAll();
	}
	
	/**
	 * Creates a localized lookup document.
	 * @param localizedLabelBean
	 * @param type
	 * @return
	 */
	protected Document createDocument(ILabelBean labelBean, int type) {
		ILocalizedLabelBean localizedLabelBean = (ILocalizedLabelBean)labelBean;
		String id = localizedLabelBean.getObjectID().toString();
		String notLocalizedName = localizedLabelBean.getLabel();
		String keyPrefix = localizedLabelBean.getKeyPrefix();
		Document document = new Document();
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Creating the localized list option document by id " + id +
						" not localized name " + notLocalizedName + " type " + String.valueOf(type));
			}
			if (countrySet!=null && !countrySet.isEmpty()) {
				String key = keyPrefix + id;
				String defaultLocalizedName = null;
				if (defaultLocale!=null) {
					defaultLocalizedName = LocalizeUtil.getLocalizedTextFromApplicationResources(key, defaultLocale);
					if (defaultLocalizedName!=null && !defaultLocalizedName.equals(notLocalizedName)) {
						if (defaultLocalizedName.equals(key)) {
							//no default localization
							defaultLocalizedName = notLocalizedName;
						} else {
							document.add(new TextField(LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.DEFAULT_LOCALIZED,
									defaultLocalizedName, Field.Store.NO));
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug(" defaultLocalizedName " + defaultLocalizedName);
							}
						}
					}
				}
				Map<String, String> localizedNameMap = new HashMap<String, String>(); 
				for (Locale locale : languageSet) {
					String localizedName = LocalizeUtil.getLocalizedTextFromApplicationResources(key, locale);
					if (localizedName!=null && !localizedName.equals(key) && !localizedName.equals(defaultLocalizedName)) {
						localizedNameMap.put(locale.toString(), localizedName);
						//locale specific label which differs from the default localization (fallbacks to default will not be stored)
						document.add(new TextField(locale.toString(), localizedName, Field.Store.NO));
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(" localizedName for locale " + locale.toString() + " " + localizedName);
						}
					}
				}
				for (Locale locale : countrySet) {
					String localizedName = LocalizeUtil.getLocalizedTextFromApplicationResources(key, locale);
					if (localizedName!=null && !localizedName.equals(key) && !localizedName.equals(defaultLocalizedName)) {
						String localeStr = locale.toString();
						if (localeStr!=null && localeStr.length()>=2) {
							String countryString = localeStr.substring(0, 2);
							String languageLocalizedName = localizedNameMap.get(countryString);
							if (languageLocalizedName==null || !languageLocalizedName.equals(localizedName)) {
								//locale specific label which differs from the default localization (fallbacks to default will not be stored)
								document.add(new TextField(locale.toString(), localizedName, Field.Store.NO));
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug(" localizedName for locale " + locale.toString() + " " + localizedName);
								}
							}
						}
					}
				}
			}
			//COMBINEDKEY used only by delete, because it works only with Term (BooleanQuery not possible)
			document.add(new StringField(LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.COMBINEDKEY,
					LuceneUtil.getCombinedKeyValue(id, String.valueOf(type)),
					Field.Store.YES));
			document.add(new StringField(LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.VALUE, id,
					Field.Store.YES));
			document.add(new TextField(LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.LABEL, notLocalizedName,
					Field.Store.NO));
			document.add(new StringField(LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.TYPE, String.valueOf(type),
					Field.Store.YES));
			return document;
		} catch (Exception e) {
			LOGGER.error("Creating the document for localized list entry with keyPrefix " +
					keyPrefix + " label " + notLocalizedName + " id " + id +
					" Type " + type + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	/**
	 * Gets the sets with locales having localized strings
	 * @return
	 */
	private static Locale getDefaultLocaleAndLocaleSets(Set<Locale> countrySet, Set<Locale> languageSet) {
		List<LabelValueBean> localeBeans = LocaleHandler.getPossibleLocales();
		Locale defaultLocale = null;
		for (LabelValueBean localeBean : localeBeans) {
			String localeString = localeBean.getValue();
			Locale locale = LocaleHandler.getLocaleFromString(localeString);
			if (locale!=null) {
				if ("".equals(localeString)) {
					defaultLocale = locale; 
				} else {
					String country = locale.getCountry();
					if (country==null || "".equals(country)) {
						languageSet.add(locale);
					} else {
						countrySet.add(locale);
					}
				}
			}
		}
		return defaultLocale;
	}
}
