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

package com.aurel.track.lucene.search.listFields;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.search.LuceneSearcher;

public class NotLocalizedListSearcher extends AbstractListFieldSearcher {
	
	private static final Logger LOGGER = LogManager.getLogger(NotLocalizedListSearcher.class);
	private static NotLocalizedListSearcher instance;
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static NotLocalizedListSearcher getInstance(){
		if(instance==null){
			instance=new NotLocalizedListSearcher();
		}
		return instance;
	}
	/**
	 * Gets the index writer ID
	 * @return
	 */
	protected int getIndexSearcherID() {
		return LuceneUtil.INDEXES.NOT_LOCALIZED_LIST_INDEX;
	}
	
	/**
	 * Gets the fieldID types stored in this index 
	 * @return
	 */
	protected List<Integer> getFieldIDs() {
		List<Integer> externalIDFields = new LinkedList<Integer>();
		externalIDFields.add(SystemFields.INTEGER_PROJECT);
		externalIDFields.add(SystemFields.INTEGER_RELEASE);
		externalIDFields.add(SystemFields.INTEGER_PERSON);
		return externalIDFields;
	}
	
	/**
	 * Preprocess an explicit field
	 * @param analyzer 
	 * @param toBeProcessedString a part of the user entered query string
	 * @param activityName the name of the user entered field
	 * @param indexStart the index to start looking for fieldName 
	 * @return
	 */
	public String preprocessExplicitField(Analyzer analyzer, String toBeProcessedString,
			Locale locale, int indexStart) {
		List<Integer> fieldIDs = getFieldIDs();
		for (Integer fieldID : fieldIDs) {
			if (SystemFields.INTEGER_PERSON.equals(fieldID)) {
				TFieldBean fieldBean = FieldTypeManager.getInstance().getFieldBean(fieldID);
				String luceneFieldName = LuceneUtil.normalizeFieldName(fieldBean.getName());
				//gather all person fields
				List<String> personWorkItemFieldNames = new LinkedList<String>();
				personWorkItemFieldNames.add(FieldTypeManager.getInstance().getFieldBean(SystemFields.INTEGER_MANAGER).getName());
				personWorkItemFieldNames.add(FieldTypeManager.getInstance().getFieldBean(SystemFields.INTEGER_RESPONSIBLE).getName());
				personWorkItemFieldNames.add(FieldTypeManager.getInstance().getFieldBean(SystemFields.INTEGER_ORIGINATOR).getName());
				personWorkItemFieldNames.add(FieldTypeManager.getInstance().getFieldBean(SystemFields.INTEGER_CHANGEDBY).getName());
				//gather user pickers
				Map<Integer, FieldType> fieldTypeCache = FieldTypeManager.getInstance().getTypeCache();
				if (fieldTypeCache!=null) {
					for (Integer field : fieldTypeCache.keySet()) {
						IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(field);
						if (fieldTypeRT.isUserPicker()) {
							personWorkItemFieldNames.add(FieldTypeManager.getInstance().getFieldBean(field).getName());
						}
					}
				}
				for (String workItemFieldName : personWorkItemFieldNames) {
					//for. ex: workItemFieldName: Responsible, luceneFieldName: corresponding to SystemFields.INTEGER_PERSON is manager 
					//(all persons are stored under manager in not localized lookup index)
					//so search in not localized list index by "Manager" but the resulting ID
					//should be match against  the field Responsible in the workItem index
					toBeProcessedString = replaceExplicitFieldValue(analyzer, toBeProcessedString, workItemFieldName, luceneFieldName, fieldID, locale, indexStart);
				}
			} else {
				//for project and release the lucene document field name is same as the lucene expression field name 
				TFieldBean fieldBean = FieldTypeManager.getInstance().getFieldBean(fieldID);
				if (fieldBean!=null) {
					String luceneFieldName = LuceneUtil.normalizeFieldName(fieldBean.getName());
					toBeProcessedString = replaceExplicitFieldValue(analyzer, toBeProcessedString, luceneFieldName, luceneFieldName, fieldID, locale, indexStart);
				}
			}
		}
		return toBeProcessedString;
	}
	
	/**
	 * Gets the lucene query for explicit lookup field
	 * @param analyzer
	 * @param fieldName
	 * @param fieldValue
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	protected Query getExplicitFieldQuery(Analyzer analyzer,
			String fieldName, String fieldValue, Integer fieldID, Locale locale) {
		Query query = null;
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		int lookupEntityType = fieldTypeRT.getLookupEntityType();
		QueryParser queryParser = new QueryParser(getLabelFieldName(), analyzer);
		String typedFieldQueryString = fieldValue +" AND " + getTypeFieldName() + 
			LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + lookupEntityType;
		try {
			query = queryParser.parse(typedFieldQueryString);
		} catch (ParseException e) {
			LOGGER.error("Parsing the query string for fieldName  " + fieldName +
				" and fieldValue '" + fieldValue + "' failed with " + e.getMessage(), e);
		}
		return query;
	}
	
	/**
	 * Gets the lucene query when no field is specified,
	 * that means search the entire not localized index by label
	 * @param analyzer
	 * @param toBeProcessedString
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	protected Query getNoExlplicitFieldQuery(Analyzer analyzer,
			String toBeProcessedString, Locale locale) {
		QueryParser queryParser = new QueryParser(getLabelFieldName(), analyzer);
		Query query = null;
		try {
			query = queryParser.parse(toBeProcessedString);
		} catch (ParseException e) {
			LOGGER.error("Parsing the no field query string for fieldValue '" + toBeProcessedString + "' failed with " + e.getMessage(), e);
		}
		return query;
	}
	/**
	 * Gets the workItem field names for a type 
	 */
	protected String[] getWorkItemFieldNames(Integer type) {
		return getWorkItemFieldNamesForLookupType(type.intValue());
	}
	/**
	 * Gets the lucene field from document representing the list label
	 */
	protected String getLabelFieldName() {
		return LuceneUtil.LIST_INDEX_FIELDS_NOT_LOCALIZED.LABEL;
	}
	/**
	 * Gets the lucene field from document representing the type
	 */
	protected String getTypeFieldName() {
		return LuceneUtil.LIST_INDEX_FIELDS_NOT_LOCALIZED.TYPE;
	}
	/**
	 * Gets the lucene field from document representing the list label
	 */
	protected String getValueFieldName() {
		return LuceneUtil.LIST_INDEX_FIELDS_NOT_LOCALIZED.VALUE;
	}
}
