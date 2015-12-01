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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.search.AbstractLookupFieldSearcher;
import com.aurel.track.lucene.search.LuceneSearcher;

/**
 * Search in list labels
 * @author Tamas Ruff
 *
 */
public abstract class AbstractListFieldSearcher
	extends AbstractLookupFieldSearcher {
	
	private static final Logger LOGGER = LogManager.getLogger(AbstractListFieldSearcher.class);
	/**
	 * Gets the index searcher ID
	 * @return
	 */
	protected abstract int getIndexSearcherID();
	/**
	 * Gets the fieldIDs stored in this index 
	 * @return
	 */
	protected abstract List<Integer> getFieldIDs();
	/**
	 * Replaces the label for a field with the objectID value
	 * @param analyzer 
	 * @param toBeProcessedString a part of the user entered query string
	 * @param workItemFieldName the workItem field name (like CRM Contact)
	 * @param luceneFieldName the name of the user entered lucene field (like Company from CRM Contact)
	 * @param indexStart the index to start looking for fieldName 
	 * @return
	 */
	protected String replaceExplicitFieldValue(Analyzer analyzer, 
			String toBeProcessedString, String workItemFieldName, String luceneFieldName, Integer fieldID, Locale locale, int indexStart) {
		int indexFound = LuceneSearcher.fieldNameIndex(toBeProcessedString, workItemFieldName, indexStart);
		if (indexFound==-1) {
			return toBeProcessedString;
		}
		int beginReplaceIndex = indexFound + workItemFieldName.length() + 1;
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
	 * Preprocess the toBeProcessedString when no field is specified
	 * @param analyzer
	 * @param toBeProcessedString
	 * @param locale
	 * @return
	 */
	@Override
	public Query getNoExplicitFieldQuery(Analyzer analyzer, String toBeProcessedString, Locale locale) {
		Query query = null;
		String queryString = searchNoExplicitField(analyzer, toBeProcessedString, locale);
		if (queryString!=null && queryString.trim().length()>0) {
			QueryParser queryParser = new QueryParser(LuceneUtil.getFieldName(SystemFields.ISSUENO), analyzer);
			LOGGER.debug("List matches found for no explicit field " + queryString);
			try {
				query = queryParser.parse(queryString);
			} catch (ParseException e) {
				LOGGER.warn("Parsing without explicit field for " + queryParser + " field failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return query;
	}
	
	/**
	 * Finds the list options which match the user entered string in link descriptions
	 * @param analyzer
	 * @param fieldName
	 * @param label
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	@Override
	protected String searchExplicitField(Analyzer analyzer, String fieldName,
			String label, Integer fieldID, Locale locale) {
		IndexSearcher indexSearcher = null;
		try {
			Query query = getExplicitFieldQuery(analyzer, fieldName, label, fieldID, locale);
			if (query==null) {
				return label;
			}
			indexSearcher = LuceneSearcher.getIndexSearcher(getIndexSearcherID());
			if (indexSearcher == null) {
				return label;
			}
			ScoreDoc[] scoreDocs;
			try {
				TopDocsCollector<ScoreDoc> collector = TopScoreDocCollector.create(LuceneSearcher.MAXIMAL_HITS);
				indexSearcher.search(query, collector);
				scoreDocs = collector.topDocs().scoreDocs;
			} catch (IOException e) {
				LOGGER.warn("Searching by fieldName " + fieldName +
						" and fieldValue " + label + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				return label;
			}
			if (scoreDocs == null || scoreDocs.length==0) {
				return label;
			}
			if (scoreDocs.length>LuceneSearcher.MAX_BOOLEAN_CLAUSES) {
				LOGGER.warn("Maximum number of boolean clauses was exceeded");
			}
			Set<Integer> listOptionIDs = new HashSet<Integer>(); 
			for (int i = 0; i < scoreDocs.length; i++) {
				int docID = scoreDocs[i].doc;
				Document doc;
				try {
					doc = indexSearcher.doc(docID);
				} catch (IOException e) {
					LOGGER.error("Getting the documents from index searcher for fieldName " + fieldName +
							" and fieldValue " + label  + "  failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
					return label;
				}
				String listOptionIDStr = doc.get(getValueFieldName());
				Integer listOptionID = null;
				if (listOptionIDStr!=null) {
					try {
						listOptionID = Integer.valueOf(listOptionIDStr);
						listOptionIDs.add(listOptionID);
					} catch (Exception e) {
					}
				}
			}
			return LuceneSearcher.createORDividedIDs(listOptionIDs);
		} catch(Exception e) {
			LOGGER.error("Getting the fieldName " + fieldName + " and fieldValue " + label + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return label;
		} finally {
			LuceneSearcher.closeIndexSearcherAndUnderlyingIndexReader(indexSearcher, fieldName);
		}
	}
	
	/**
	 * Get the workItemIDs which match the user entered string
	 * @param analyzer
	 * @param fieldValue
	 * @param locale
	 * @return
	 */
	@Override
	protected String searchNoExplicitField(Analyzer analyzer, String toBeProcessedString, Locale locale) {
		IndexSearcher indexSearcher = null;
		Map<Integer, Set<Integer>> result = new HashMap<Integer, Set<Integer>>();
		try {
			Query lookupQuery = getNoExlplicitFieldQuery(analyzer, toBeProcessedString, locale);
			if (lookupQuery==null) {
				return "";
			}
			indexSearcher = LuceneSearcher.getIndexSearcher(getIndexSearcherID());
			if (indexSearcher == null) {
				return "";
			}
			ScoreDoc[] scoreDocs;
			try {
				TopDocsCollector<ScoreDoc> collector = TopScoreDocCollector.create(LuceneSearcher.MAXIMAL_HITS);
				indexSearcher.search(lookupQuery, collector);
				scoreDocs = collector.topDocs().scoreDocs;
			} catch (IOException e) {
				return "";
			}
			if (scoreDocs == null || scoreDocs.length==0) {
				return "";
			}
			if (scoreDocs.length>LuceneSearcher.MAX_BOOLEAN_CLAUSES) {
				LOGGER.warn("Maximum number of boolean clauses was exceeded by not localized lookup");
			}
			Document doc;
			for (int i = 0; i < scoreDocs.length; i++) {
				int docID = scoreDocs[i].doc;
				try {
					doc = indexSearcher.doc(docID);
				} catch (IOException e) {
					LOGGER.error("Getting the documents from index searcher for fieldValue " +
							toBeProcessedString + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
					return "";
				}
				String typeStr = doc.get(getTypeFieldName());
				String idStr = doc.get(getValueFieldName());
				Integer type = null;
				Integer id = null;
				try {
					type = Integer.valueOf(typeStr);
					id = Integer.valueOf(idStr);
					Set<Integer> ids = result.get(type);
					if (ids == null) {
						ids = new HashSet<Integer>();
						result.put(type, ids);
					}
					ids.add(id);
				} catch(NumberFormatException ex) {
					continue;
				}
			}
		}catch(Exception ex){
		} finally {
			LuceneSearcher.closeIndexSearcherAndUnderlyingIndexReader(indexSearcher, "no field");
		}
		Set<Integer> types = result.keySet();
		StringBuffer directQuery = new StringBuffer();
		for (Integer type : types) {
			Set<Integer> ids = result.get(type);
			String orDividedIDs = LuceneSearcher.createORDividedIDs(ids);
			String[] workItemFieldNames = getWorkItemFieldNames(type);
			for (int i=0; i<workItemFieldNames.length; i++) {
				if (i>0) {
					directQuery.append(" OR ");
				}
				if (ids.size()>1) {
					directQuery.append(workItemFieldNames[i] + LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + "(" + orDividedIDs + ")");
				} else {
					directQuery.append(workItemFieldNames[i] + LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + orDividedIDs);
				}
			}
		}
		return directQuery.toString();
	}
	
	/**
	 * Used by preprocessing user queries without explicit field type
	 * When a certain value is found in a lookup index get the a lookupEntityType
	 * of the found document and try all workItem fields of that lookupEntityType
	 * @param lookupEntityType
	 * @return
	 */
	protected String[] getWorkItemFieldNamesForLookupType(int lookupEntityType) {
		List<String> fieldNamesList = new LinkedList<String>();
		Map<Integer, TFieldBean> fieldBeanCache = FieldTypeManager.getInstance().getFieldBeanCache();
		Map<Integer, FieldType> typeCache = FieldTypeManager.getInstance().getTypeCache();
		Set<Map.Entry<Integer, FieldType>> fieldTypes = typeCache.entrySet();
		for (Map.Entry<Integer, FieldType> fieldTypeEntry : fieldTypes) {
			Integer fieldID = fieldTypeEntry.getKey();
			IFieldTypeRT fieldTypeRT = ((FieldType)fieldTypeEntry.getValue()).getFieldTypeRT();
			if (fieldTypeRT.getLookupEntityType()==lookupEntityType) {
				TFieldBean fieldBean = fieldBeanCache.get(fieldID);
				String name = LuceneUtil.normalizeFieldName(fieldBean.getName());
				if (lookupEntityType==LuceneUtil.LOOKUPENTITYTYPES.COMPOSITE) {
					CustomCompositeBaseRT customCompositeBaseRT = (CustomCompositeBaseRT)fieldTypeRT;
					int numberOfParts = customCompositeBaseRT.getNumberOfParts();
					for (int i = 0; i < numberOfParts; i++) {
						fieldNamesList.add(LuceneUtil.synthetizeCompositePartFieldName(
							name, Integer.valueOf(i+1)));
					}
				} else {
					fieldNamesList.add(name);
				}
			}
		}
		String[] fieldNamesArr = new String[fieldNamesList.size()];
		for (int i = 0; i < fieldNamesList.size(); i++) {
			fieldNamesArr[i] = (String)fieldNamesList.get(i);
		}
		return fieldNamesArr;
	}
	
	/**
	 * Gets the workItem field names for a type 
	 */
	protected abstract String[] getWorkItemFieldNames(Integer type);
	
	/**
	 * Gets the lucene query for explicit lookup field
	 * @param analyzer
	 * @param fieldName
	 * @param fieldValue
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	protected abstract Query getExplicitFieldQuery(Analyzer analyzer, String fieldName,
			String fieldValue, Integer fieldID, Locale locale);
	
	/**
	 * Gets the lucene query when no field is specified 
	 * @param analyzer
	 * @param toBeProcessedString
	 * @param locale
	 * @return
	 */
	protected abstract Query getNoExlplicitFieldQuery(Analyzer analyzer,
			String toBeProcessedString, Locale locale);
	
	/**
	 * Gets the lucene field from document representing the list label
	 */
	protected abstract String getLabelFieldName();
	
	/**
	 * Gets the lucene field from document representing the type
	 */
	protected abstract String getTypeFieldName();
	
	/**
	 * Gets the lucene field from document representing the list optionID
	 */
	protected abstract String getValueFieldName();
}

