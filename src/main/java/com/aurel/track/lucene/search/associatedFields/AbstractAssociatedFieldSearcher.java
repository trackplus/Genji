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

package com.aurel.track.lucene.search.associatedFields;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;

import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.search.AbstractLookupFieldSearcher;
import com.aurel.track.lucene.search.ILookupFieldSearcher;
import com.aurel.track.lucene.search.LuceneSearcher;

/**
 * Search in associated entities
 * @author Tamas Ruff
 *
 */
public abstract class AbstractAssociatedFieldSearcher
	extends AbstractLookupFieldSearcher implements ILookupFieldSearcher {

	private static final Logger LOGGER = LogManager.getLogger(AbstractAssociatedFieldSearcher.class);
	/**
	 * Gets the index searcher ID
	 * @return
	 */
	protected abstract int getIndexSearcherID();
	/**
	 * Gets the explicit field which can be used in lucene expresions
	 * The end user should know only this name
	 * @return
	 */
	protected abstract String getLuceneFieldName();

	/**
	 * Gets the lucene document's 'internal' text field names
	 * An associated entity might have more then one 'internal' text field to be searched in
	 * The end user should not know about these fields
	 * @return
	 */
	protected abstract String[] getSearchFieldNames();

	/**
	 * Gets the lucene document field name containing the workItemID field
	 * @return
	 */
	protected abstract String getWorkItemFieldName();

	/**
	 * Gets the lucene document field name containing the workItemID field
	 * @param document
	 * @return
	 */
	protected String getAdditionalWorkItemFieldName(Document document) {
		return null;
	}

	/**
	 * Preprocess an explicit field
	 * @param analyzer
	 * @param toBeProcessedString a part of the user entered query string
	 * @param locale
	 * @param indexStart the index to start looking for fieldName
	 * @return
	 */
	@Override
	public String preprocessExplicitField(Analyzer analyzer,
			String toBeProcessedString, Locale locale, int indexStart) {
		String fieldName = getLuceneFieldName();
		int indexFound = LuceneSearcher.fieldNameIndex(toBeProcessedString, fieldName, indexStart);
		if (indexFound==-1) {
			return toBeProcessedString;
		}
		int beginReplaceIndex = indexFound + fieldName.length() + 1;
		String originalFieldValue = LuceneSearcher.getFieldValue(toBeProcessedString.substring(beginReplaceIndex));
		if (originalFieldValue==null || "".equals(originalFieldValue)) {
			return toBeProcessedString;
		}
		String processedFieldValue = searchExplicitField(analyzer, fieldName, originalFieldValue, null, locale);
		if (processedFieldValue==null || "".equals(processedFieldValue)) {
			return toBeProcessedString;
		}
		StringBuilder original = new StringBuilder(toBeProcessedString);
		//get the user entered value of the field
		String issueNoFieldName = LuceneUtil.getFieldName(SystemFields.ISSUENO);
		original.replace(indexFound, beginReplaceIndex + originalFieldValue.length(), issueNoFieldName + LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + processedFieldValue);
		return preprocessExplicitField(analyzer, original.toString(), locale, beginReplaceIndex + processedFieldValue.length());
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
		String workItemsFound = searchNoExplicitField(analyzer, toBeProcessedString, locale);
		if (workItemsFound!=null && !workItemsFound.equals(toBeProcessedString)) {
			QueryParser queryParser = new QueryParser(LuceneUtil.getFieldName(SystemFields.ISSUENO), analyzer);
			String queryString = LuceneUtil.getFieldName(SystemFields.ISSUENO) +
					LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + workItemsFound;
			LOGGER.debug(getLuceneFieldName() + " workItems found : " + workItemsFound);
			try {
				query = queryParser.parse(queryString);
			} catch (ParseException e) {
				LOGGER.warn("Parsing without explicit field the " + getLuceneFieldName() +  "  for " + queryString + " field failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return query;
	}


	/**
	 * Get the OR separated IDs which match the specified field's user entered string
	 * @param analyzer
	 * @param fieldName
	 * @param fieldValue
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	@Override
	protected String searchExplicitField(Analyzer analyzer, String fieldName,
			String fieldValue, Integer fieldID, Locale locale) {
		IndexSearcher indexSearcher = null;
		try {
			Query query = getAssociatedFieldQuery(analyzer, fieldValue);
			if (query==null) {
				return fieldValue;
			}
			indexSearcher = LuceneSearcher.getIndexSearcher(getIndexSearcherID());
			if (indexSearcher == null) {
				return fieldValue;
			}
			ScoreDoc[] scoreDocs;
			try {
				TopDocsCollector<ScoreDoc> collector = TopScoreDocCollector.create(LuceneSearcher.MAXIMAL_HITS);
				indexSearcher.search(query, collector);
				scoreDocs = collector.topDocs().scoreDocs;
			} catch (IOException e) {
				LOGGER.warn("Searching the " + getLuceneFieldName() + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				return fieldValue;
			}
			if (scoreDocs==null || scoreDocs.length==0) {
				return fieldValue;
			}
			if (scoreDocs.length>LuceneSearcher.MAX_BOOLEAN_CLAUSES) {
				LOGGER.warn("Maximum number of boolean clauses was exceeded");
			}

			Set<Integer> workItemIDs = new HashSet<Integer>();
			for (int i = 0; i < scoreDocs.length; i++) {
				int docID = scoreDocs[i].doc;
				Document doc;
				try {
					doc = indexSearcher.doc(docID);
				} catch (IOException e) {
					LOGGER.error("Getting the documents from index searcher for " + getLuceneFieldName() + "  failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
					return fieldValue;
				}


				String workItemFieldName = getWorkItemFieldName();
				String workItemIDStr = doc.get(workItemFieldName);
				Integer workItemID = null;
				if (workItemIDStr!=null) {
					try {
						workItemID = Integer.valueOf(workItemIDStr);
						workItemIDs.add(workItemID);
					} catch (Exception e) {
						LOGGER.debug(e);
					}
				}
				//by links there are two workitems for bidirectional links
				String additionalWorkItemFieldName = getAdditionalWorkItemFieldName(doc);
				if (additionalWorkItemFieldName!=null) {
					workItemIDStr = doc.get(additionalWorkItemFieldName);
					workItemID = null;
					if (workItemIDStr!=null) {
						try {
							workItemID = Integer.valueOf(workItemIDStr);
							workItemIDs.add(workItemID);
						} catch (Exception e) {
						}
					}
				}

			}
			return LuceneSearcher.createORDividedIDs(workItemIDs);
		} catch(Exception e) {
			LOGGER.warn("Getting the " + getLuceneFieldName() + " field " + fieldValue + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return fieldValue;
		} finally {
			LuceneSearcher.closeIndexSearcherAndUnderlyingIndexReader(indexSearcher, getLuceneFieldName());
		}
	}

	/**
	 * Get the OR separated IDs which match the user entered string
	 * @param analyzer
	 * @param fieldValue
	 * @param locale
	 * @return
	 */
	@Override
	protected String searchNoExplicitField(Analyzer analyzer, String fieldValue, Locale locale) {
		return searchExplicitField(analyzer, null, fieldValue, null, locale);
	}


	/**
	 * Gets the lucene query object
	 * @param fieldValue
	 * @return
	 */
	protected Query getAssociatedFieldQuery(Analyzer analyzer, String fieldValue) {
		Query query = null;
		String[] searchFieldNames = getSearchFieldNames();
		if (searchFieldNames!=null && searchFieldNames.length>0) {
			if (searchFieldNames.length==1) {
				QueryParser queryParser = new QueryParser(searchFieldNames[0], analyzer);
				try {
					query = queryParser.parse(fieldValue);
				} catch (ParseException e) {
					LOGGER.error("Parsing the query string '" + fieldValue + "' for  " + getLuceneFieldName() + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			} else {
				BooleanClause.Occur[] orFlags = LuceneSearcher.getOrFlagsArray(searchFieldNames.length);
				try {
					query = MultiFieldQueryParser.parse(fieldValue, searchFieldNames, orFlags, analyzer);
				} catch (ParseException e) {
					LOGGER.error("Parsing the query string '" + fieldValue + "' for " + getLuceneFieldName() + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return query;
	}
}
