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
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.callbackInterfaces.IExternalLookupLucene;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.search.LuceneSearcher;

public class ExternalListSearcher extends AbstractListFieldSearcher {
	
	private static final Logger LOGGER = LogManager.getLogger(ExternalListSearcher.class);
	private static ExternalListSearcher instance;
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static ExternalListSearcher getInstance(){
		if(instance==null){
			instance=new ExternalListSearcher();
		}
		return instance;
	}
	/**
	 * Gets the index writer ID
	 * @return
	 */
	@Override
	protected int getIndexSearcherID() {
		return LuceneUtil.INDEXES.EXTERNAL_LOOKUP_WRITER;
	}
	
	/**
	 * Gets the fieldIDs stored in this index 
	 * @return
	 */
	@Override
	protected List<Integer> getFieldIDs() {
		List<Integer> externalIDFields = new LinkedList<Integer>();
		Map<Integer, FieldType> fieldTypeCache = FieldTypeManager.getInstance().getTypeCache();
		if (fieldTypeCache!=null) {
			for (Integer fieldID : fieldTypeCache.keySet()) {
				FieldType fieldType = fieldTypeCache.get(fieldID);
				if (fieldType!=null) {
					IFieldTypeRT fieldTypeRT = fieldType.getFieldTypeRT();
					if (fieldTypeRT.getValueType()==ValueType.EXTERNALID) {
						externalIDFields.add(fieldID);
					}
				}
			}
		}
		return externalIDFields;
	}
	
	/**
	 * Preprocess a not localized lookup field
	 * @param analyzer 
	 * @param toBeProcessedString a part of the user entered query string
	 * @param workItemFieldName the workItem field name (like CRM Contact)
	 * @param luceneFieldName the name of the user entered lucene field (like Company from CRM Contact)
	 * @param indexStart the index to start looking for fieldName 
	 * @return
	 */
	@Override
	protected String replaceExplicitFieldValue(Analyzer analyzer, 
			String toBeProcessedString, String workItemFieldName, String luceneFieldName, Integer fieldID, Locale locale, int indexStart) {
		int indexFound = LuceneSearcher.fieldNameIndex(toBeProcessedString, luceneFieldName, indexStart);
		if (indexFound==-1) {
			return toBeProcessedString;
		}
		int beginReplaceIndex = indexFound + luceneFieldName.length() + 1;
		String originalFieldValue = LuceneSearcher.getFieldValue(toBeProcessedString.substring(beginReplaceIndex));
		if (originalFieldValue==null || "".equals(originalFieldValue)) {
			return toBeProcessedString;
		}
		String processedFieldValue = searchExplicitField(analyzer, luceneFieldName, originalFieldValue, fieldID, locale);
		if (processedFieldValue==null || "".equals(processedFieldValue)) {
			return toBeProcessedString;
		}
		StringBuffer original = new StringBuffer(toBeProcessedString);
		original.replace(indexFound, beginReplaceIndex + originalFieldValue.length(),
				workItemFieldName + LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + processedFieldValue);
		return replaceExplicitFieldValue(analyzer, original.toString(), workItemFieldName, luceneFieldName,
				fieldID, locale, beginReplaceIndex + processedFieldValue.length());
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
	public String preprocessExplicitField(Analyzer analyzer,
			String toBeProcessedString, Locale locale, int indexStart) {
		List<Integer> fieldIDs = getFieldIDs();
		for (Integer fieldID : fieldIDs) {
			TFieldBean fieldBean = FieldTypeManager.getInstance().getFieldBean(fieldID);
			if (fieldBean!=null) {
				String workItemFieldName = LuceneUtil.normalizeFieldName(fieldBean.getName());
				IExternalLookupLucene externalLookupField = (IExternalLookupLucene)FieldTypeManager.getFieldTypeRT(fieldID);
				String[] searchableFields = externalLookupField.getSearchableFieldNames();
				if (searchableFields!=null && searchableFields.length>0) {
					for (String luceneFieldName : searchableFields) {
						//for ex. in external list index look for CustomerName, ContactPartnerName
						//but the resulting ID should be looked up in workItem index as CRMContact field 
						toBeProcessedString = replaceExplicitFieldValue(analyzer, toBeProcessedString, workItemFieldName, luceneFieldName, fieldID, locale, indexStart);
					}
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
	@Override
	protected Query getExplicitFieldQuery(Analyzer analyzer,
			String fieldName, String fieldValue, Integer fieldID, Locale locale) {
		Query query= null;
		QueryParser queryParser = new QueryParser(fieldName, analyzer);
		//adds the type criteria
		String externalFieldQueryString = fieldValue + 
			" AND " + LuceneUtil.EXTERNAL_INDEX_FIELDS.FIELDID + 
			LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + fieldID;
		try {
			query = queryParser.parse(externalFieldQueryString);
		} catch (ParseException e) {
			LOGGER.error("Parsing the external lookups query string fieldName " + fieldName + " fieldValue '" + fieldValue + "' failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return query;
	}
	
	/**
	 * Gets the lucene query when no field is specified 
	 * @param analyzer
	 * @param toBeProcessedString
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	@Override
	protected Query getNoExlplicitFieldQuery(Analyzer analyzer,
			String toBeProcessedString, Locale locale) {
		List<Integer> fieldIDs = getFieldIDs();
		if (fieldIDs!=null && !fieldIDs.isEmpty()) {
			BooleanQuery booleanQuery = new BooleanQuery();
			for (Integer fieldID : fieldIDs) {
				Query searchabelFieldQuery = getNoFieldQuerySearchableField(analyzer, toBeProcessedString, fieldID, locale);
				booleanQuery.add(searchabelFieldQuery, BooleanClause.Occur.SHOULD);
			}
			return booleanQuery;
		}
		return null;
	}
	
	/**
	 * Gets the lucene query when no field is specified 
	 * @param analyzer
	 * @param toBeProcessedString
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	protected Query getNoFieldQuerySearchableField(Analyzer analyzer,
			String toBeProcessedString, Integer fieldID, Locale locale) {
		BooleanQuery booleanQuery = new BooleanQuery();
		IExternalLookupLucene externalLookupField = (IExternalLookupLucene)FieldTypeManager.getFieldTypeRT(fieldID);
		String[] searchableFields = externalLookupField.getSearchableFieldNames();
		if (searchableFields!=null && searchableFields.length>0) {
			for (String searchableField : searchableFields) {
				QueryParser queryParser = new QueryParser(searchableField, analyzer);
				Query externalLookupFieldQuery = null;
				String externalFieldQueryString = toBeProcessedString + 
						" AND " + LuceneUtil.EXTERNAL_INDEX_FIELDS.FIELDID + 
						LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + fieldID;
				try {
					externalLookupFieldQuery = queryParser.parse(toBeProcessedString);
					booleanQuery.add(externalLookupFieldQuery, BooleanClause.Occur.SHOULD);
				} catch (ParseException e) {
					LOGGER.error("Parsing the external lookups query string for fieldValue '" + externalFieldQueryString + "' failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return booleanQuery;
	}
	/**
	 * Gets the workItem field names for a type 
	 */
	@Override
	protected String[] getWorkItemFieldNames(Integer type) {
		//for external list the type is the fieldID
		TFieldBean fieldBean = FieldTypeManager.getInstance().getFieldBean(type);
		if (fieldBean!=null) {
			return new String[] {fieldBean.getName()};
		}
		return new String[] {};
	}
	/**
	 * Gets the lucene field from document representing the list label
	 */
	@Override
	protected String getLabelFieldName() {
		return "";
	}
	/**
	 * Gets the lucene field from document representing the type
	 */
	@Override
	protected String getTypeFieldName() {
		return LuceneUtil.EXTERNAL_INDEX_FIELDS.FIELDID;
	}
	/**
	 * Gets the lucene field from document representing the list label
	 */
	@Override
	protected String getValueFieldName() {
		return LuceneUtil.EXTERNAL_INDEX_FIELDS.OBJECTID;
	}
}
