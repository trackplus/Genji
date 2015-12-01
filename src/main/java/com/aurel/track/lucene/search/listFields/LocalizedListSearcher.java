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

package com.aurel.track.lucene.search.listFields;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.search.LuceneSearcher;

public class LocalizedListSearcher extends AbstractListFieldSearcher {
	
	private static final Logger LOGGER = LogManager.getLogger(LocalizedListSearcher.class);
	private static LocalizedListSearcher instance;
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static LocalizedListSearcher getInstance(){
		if(instance==null){
			instance=new LocalizedListSearcher();
		}
		return instance;
	}
	/**
	 * Gets the index writer ID
	 * @return
	 */
	@Override
	protected int getIndexSearcherID() {
		return LuceneUtil.INDEXES.LOCALIZED_LIST_INDEX;
	}
	
	/**
	 * Gets the fieldIDs stored in this index for searching by explicit fields
	 * @return
	 */
	@Override
	protected List<Integer> getFieldIDs() {
		List<Integer> fieldIDs = new LinkedList<Integer>();
		fieldIDs.add(SystemFields.INTEGER_ISSUETYPE);
		fieldIDs.add(SystemFields.INTEGER_PRIORITY);
		fieldIDs.add(SystemFields.INTEGER_SEVERITY);
		fieldIDs.add(SystemFields.INTEGER_STATE);
		Map<Integer, FieldType> fieldTypeCache = FieldTypeManager.getInstance().getTypeCache();
		if (fieldTypeCache!=null) {
			//gets one custom list field if exist
			for (Integer fieldID : fieldTypeCache.keySet()) {
				FieldType fieldType = fieldTypeCache.get(fieldID);
				if (fieldType!=null) {
					IFieldTypeRT fieldTypeRT = fieldType.getFieldTypeRT();
					if (fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION) {
						//get only the simple custom lists
						//the composite fields are dealt with separately
						fieldIDs.add(fieldID);
					}
				}
			}
		}
		return fieldIDs;
	}
	
	/**
	 * Preprocess an explicit field
	 * @param analyzer 
	 * @param toBeProcessedString a part of the user entered query string
	 * @param activityName the name of the user entered field
	 * @param indexStart the index to start looking for fieldName 
	 * @return
	 */
	@Override
	public String preprocessExplicitField(Analyzer analyzer, String toBeProcessedString,
			Locale locale, int indexStart) {
		List<Integer> fieldIDs = getFieldIDs();
		for (Integer fieldID : fieldIDs) {
			TFieldBean fieldBean = FieldTypeManager.getInstance().getFieldBean(fieldID);
			if (fieldBean!=null) {
				String luceneFieldName = LuceneUtil.normalizeFieldName(fieldBean.getName());
				toBeProcessedString = replaceExplicitFieldValue(analyzer, toBeProcessedString,
						luceneFieldName, luceneFieldName, fieldID, locale, indexStart);
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
	@Override
	protected Query getExplicitFieldQuery(Analyzer analyzer,
			String fieldName, String fieldValue, Integer fieldID, Locale locale) {
		QueryParser queryParser = new QueryParser(getLabelFieldName(), analyzer);
		//search for both localized and not localized value
		String queryString = "(" + getLabelFieldName()+ LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + fieldValue +
				" OR " + locale.toString() + LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + fieldValue + 
				" OR " + LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.DEFAULT_LOCALIZED + LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + fieldValue +
				") AND " + LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.TYPE + 
			LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + getEntityType(fieldID);
		Query query = null;
		try {
			query = queryParser.parse(queryString);
		} catch (ParseException e) {
			LOGGER.error("Parsing the query string for fieldName  " + fieldName +
				" and fieldValue '" + fieldValue + "' failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return query;
	}
	
	
	protected int getEntityType(Integer fieldID) {
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		return fieldTypeRT.getLookupEntityType();
	}
	
	/**
	 * Gets the lucene query when no field is specified,
	 * that means search the entire localized index by label, default localized or locale 
	 * @param analyzer
	 * @param toBeProcessedString
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	@Override
	protected Query getNoExlplicitFieldQuery(Analyzer analyzer,
			String toBeProcessedString, Locale locale) {
		QueryParser queryParser = new QueryParser(getLabelFieldName(), analyzer);
		Query query = null;
		try {
			//search for both localized and not localized value
			query = queryParser.parse(getLabelFieldName()+ LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + toBeProcessedString +
					" OR " + locale.toString() + LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + toBeProcessedString +
					" OR " + LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.DEFAULT_LOCALIZED + LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + toBeProcessedString);
		} catch (ParseException e) {
			LOGGER.error("Parsing the no field query string for fieldValue '" + toBeProcessedString + "' failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return query;
	}
	
	/**
	 * Gets the workItem field names for a type 
	 */
	@Override
	protected String[] getWorkItemFieldNames(Integer type) {
		return getWorkItemFieldNamesForLookupType(type.intValue());
	}
	
	/**
	 * Gets the lucene field from document representing the list label
	 */
	@Override
	protected String getLabelFieldName() {
		return LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.LABEL;
	}
	
	/**
	 * Gets the lucene field from document representing the type
	 */
	@Override
	protected String getTypeFieldName() {
		return LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.TYPE;
	}
	
	/**
	 * Gets the lucene field from document representing the list label
	 */
	@Override
	protected String getValueFieldName() {
		return LuceneUtil.LIST_INDEX_FIELDS_LOCALIZED.VALUE;
	}
}
