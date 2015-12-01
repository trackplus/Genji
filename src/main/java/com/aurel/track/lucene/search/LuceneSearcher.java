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

package com.aurel.track.lucene.search;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;

import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.callbackInterfaces.IExternalLookupLucene;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.search.associatedFields.AttachmentSearcher;
import com.aurel.track.lucene.search.associatedFields.BudgetPlanSearcher;
import com.aurel.track.lucene.search.associatedFields.ExpenseSearcher;
import com.aurel.track.lucene.search.associatedFields.LinkSearcher;
import com.aurel.track.lucene.search.listFields.ExternalListSearcher;
import com.aurel.track.lucene.search.listFields.LocalizedListCompositePartSearcher;
import com.aurel.track.lucene.search.listFields.LocalizedListSearcher;
import com.aurel.track.lucene.search.listFields.NotLocalizedListSearcher;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;

/**
 * Utility methods for lucene searching 
 * @author Tamas Ruff
 *
 */
public class LuceneSearcher {

	private static final Logger LOGGER = LogManager.getLogger(LuceneSearcher.class);
	/**
	 * The maximum number of boolean clauses. Interesting mainly for lookup results combined with OR-s
	 */
	public static int MAX_BOOLEAN_CLAUSES = 1024;
	/**
	 * the string which separates the field name from the field value
	 */
	public static String FIELD_NAME_VALUE_SEPARATOR = ":";
	
	public static int MAXIMAL_HITS = 1000;
	
	
	/**
	 * Find the workItemIDs for a user entered query string
	 * The userQueryString may or may not have explicit fields specified 
	 * The further processing depends very much on whether there is explicit field specified or not
	 * @param userQueryString
	 * @param external whether track+ item search or wiki search
	 * @param locale
	 * @param highlightedTextMap
	 * @return
	 */
	public static int[] searchWorkItems(String userQueryString, boolean external, Locale locale,
			Map<Integer, String> highlightedTextMap) throws ParseException, BooleanQuery.TooManyClauses {
		Analyzer analyzer = LuceneUtil.getAnalyzer();
		if (userQueryString==null) {
			LOGGER.info("Query string is null");
			return null;
		} else {
			LOGGER.debug("Query string is " + userQueryString);
		}
		List<TListTypeBean> documentTypesList = IssueTypeBL.loadAllDocumentTypes();
		Set<Integer> documentTypeIDs = GeneralUtils.createIntegerSetFromBeanList(documentTypesList);
		String preprocessedQueryString = userQueryString;
		Query query = null;
		Query projectSpecificIDQuery = null;
		Map<String, Integer> compositeFieldIDByFieldName = new HashMap<String, Integer>();
		Map<String, String> fieldLabelToFieldNameMap = new HashMap<String, String>();
		List<String> fieldNames = getAllFieldNames(compositeFieldIDByFieldName, fieldLabelToFieldNameMap, locale);
		userQueryString = replaceFieldLabelsWithFieldNames(userQueryString, fieldLabelToFieldNameMap);
		if (fieldSpecified(userQueryString, fieldNames, compositeFieldIDByFieldName)) {
			//initialize the query with the SYNOPSIS as default field
			//queryParser = new QueryParser(LuceneUtil.getFieldName(SystemFields.SYNOPSYS), analyzer);
			//preprocess the query:
			// 1. by replacing the not localized lookup texts with the corresponding values: ex. Project:TryItOut -> Project:1
			// 2. by replacing the localized lookup texts with the corresponding values: ex. Status:geschlossen -> Status:10
			// 3. preprocess the localized date in lucene format. ex. EndDate:24.12.07 -> EndDate:20071224
			// 4. preprocess the localized boolean value
			// 5. preprocess composite fields pc:p1#c11 -> pc#1:12 AND pc#2:21 
			// 6. replace the text found in an attachment with the ID(s) of the workItems the attachment belongs to
			preprocessedQueryString = preprocess(analyzer, userQueryString, locale);
			//set all direct text fields as default fields 
			/*List<IntegerStringBean> directTextFieldNames = LuceneUtil.getFieldNamesForPreprocessType(LuceneUtil.PREPROCESSTYPES.DIRECT);
			String[] fieldNamesArr = new String[directTextFieldNames.size()];
			for (int i = 0; i < directTextFieldNames.size(); i++) {
				fieldNamesArr[i] = directTextFieldNames.get(i).getLabel();
			}
			BooleanClause.Occur[] orFlags = getOrFlagsArray(directTextFieldNames.size());
			try {
				//initialize the query with all direct fields as default field
				query = MultiFieldQueryParser.parse(LuceneUtil.VERSION, preprocessedQueryString, fieldNamesArr, orFlags, analyzer);
			} catch (ParseException e) {
				LOGGER.error("Parsing without explicit field for workItem fields (MultiFieldQueryParser) failed with " + e.getMessage());
				throw e;
			}*/
			QueryParser queryParser = new QueryParser(LuceneUtil.getFieldName(SystemFields.ISSUENO), analyzer);
			try {
				query = queryParser.parse(preprocessedQueryString);
			} catch (ParseException e) {
				LOGGER.warn("Parsing explicit field  for " + preprocessedQueryString + " field failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				throw e;
			}
			if (query!=null && LOGGER.isDebugEnabled()) {
				LOGGER.debug("The preprocessed query: " + query.toString());
			}
			Query itemTypeQuery = getItemTypeQuery(documentTypeIDs, analyzer);
			if (itemTypeQuery!=null) {
				LOGGER.debug("The item type query: " + itemTypeQuery.toString());
				BooleanClause.Occur occur = null;
				if (external) {
					occur = BooleanClause.Occur.MUST;
				} else {
					occur = BooleanClause.Occur.MUST_NOT;
				}
				BooleanQuery finalQuery = new BooleanQuery();
				finalQuery.add(query, BooleanClause.Occur.MUST);
				finalQuery.add(itemTypeQuery, occur);
				query = finalQuery;
			}
		} else {
			if (isPossibleProjectSpecificID(userQueryString)) {
				TFieldBean fieldBean = FieldTypeManager.getInstance().getFieldBean(SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO);
				if (fieldBean!=null) {
					String termField = fieldBean.getName();
					Term term = new Term(termField, userQueryString);
					projectSpecificIDQuery = new TermQuery(term);
					int[] projSpecificIDResult = getQueryResults(projectSpecificIDQuery, userQueryString, preprocessedQueryString, highlightedTextMap);
					if (projSpecificIDResult!=null && projSpecificIDResult.length>0) {
						//return only if projectSpecific item numbers are found
						return projSpecificIDResult;
					}
				}
			}
			query = preprocessNoExplicitField(analyzer, userQueryString, documentTypeIDs, external, locale);
			preprocessedQueryString = query.toString();
		}
		return getQueryResults(query, userQueryString, preprocessedQueryString, highlightedTextMap);
	}
	
	private static int[] getQueryResults(Query query, String userQueryString, String preprocessedQueryString, Map<Integer, String> highlightedTextMap) {
		int[] hitIDs = new int[0];
		IndexSearcher indexSearcher = null;
		try {
			long start = 0;
			if (LOGGER.isDebugEnabled()) {
				start = new Date().getTime();
			}
			indexSearcher = getIndexSearcher(LuceneUtil.INDEXES.WORKITEM_INDEX);
			if (indexSearcher == null) {
				return hitIDs;
			}
			ScoreDoc[] scoreDocs;
			try {
				TopDocsCollector<ScoreDoc> collector = TopScoreDocCollector.create(MAXIMAL_HITS);
				indexSearcher.search(query, collector);
				scoreDocs = collector.topDocs().scoreDocs;
			} catch (IOException e) {
				LOGGER.warn("Getting the workitem search results failed with failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				return hitIDs;
			}
			if (LOGGER.isDebugEnabled()) {
				long end = new Date().getTime();
				LOGGER.debug("Found " + scoreDocs.length + " document(s) (in " + (end - start) + " milliseconds) that matched the user query '" +
					userQueryString + "' the preprocessed query '" + preprocessedQueryString + "' and the query.toString() '" + query.toString() + "'");
			}
			QueryScorer queryScorer = new QueryScorer(query/*, LuceneUtil.HIGHLIGHTER_FIELD*/);
	        Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
	        Highlighter highlighter = new Highlighter(queryScorer); // Set the best scorer fragments
	        highlighter.setTextFragmenter(fragmenter); // Set fragment to highlight
	        hitIDs = new int[scoreDocs.length];
			for (int i = 0; i < scoreDocs.length; i++) {
				int docID = scoreDocs[i].doc;
				Document doc = null;
				try {
					doc = indexSearcher.doc(docID);
				} catch (IOException e) {
					LOGGER.error("Getting the workitem documents failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
				if (doc!=null) {
					Integer itemID = Integer.valueOf(doc.get(LuceneUtil.getFieldName(SystemFields.ISSUENO)));
					if (itemID!=null) {
						hitIDs[i] = itemID.intValue();
						if (highlightedTextMap!=null) {
							String highligherFieldValue = doc.get(LuceneUtil.HIGHLIGHTER_FIELD);
							TokenStream tokenStream = null;
							try {
								tokenStream = TokenSources.getTokenStream(LuceneUtil.HIGHLIGHTER_FIELD, null, highligherFieldValue, LuceneUtil.getAnalyzer(), -1);
							} catch(Exception ex) {
								LOGGER.debug(ex.getMessage());
							}
							if (tokenStream!=null) {
					            String fragment = highlighter.getBestFragment(tokenStream, highligherFieldValue);
					            if (fragment!=null) {
					            	highlightedTextMap.put(itemID, fragment);
					            }
							}
						}
					}
				}
			}
			return hitIDs;
		} catch (BooleanQuery.TooManyClauses e) {
			LOGGER.error("Searching the query resulted in too many clauses. Try to narrow the query results. " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			throw e;
		} catch(Exception e) {
			LOGGER.error("Searching the workitems failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return hitIDs;
		} finally {
			closeIndexSearcherAndUnderlyingIndexReader(indexSearcher, "workItem");
		}
	}
	
	/**
	 * Handling of a specific case: possible project specific itemID containing lucene escaping characters
	 * Precoditions:
	 * 1. should be a single term (no whitespaces inside)
	 * 2. should contain lucene escaping characters (+ - && || ! ( ) { } [ ] ^ " ~ * ? : \ but here we test only - or +).
	 * If the search term does not contain a lucene escaping character then the "default" lucene search suffice
	 * @param queryString the user entered query string	
	 * @return
	 */
	private static boolean isPossibleProjectSpecificID(String queryString) {
		if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
			String trimmedQueryString = queryString.trim();
			boolean containsSpecialChar = false;
			//special lucene characters: escaping them in term with \ does not work in query parser because it replaces them with space: exp-11 -> exp 11 -> i.e. exp OR 11
			//we test here only some if the probable escaping characters in a project prefix 
			char[] mightContainChars = new char[] {'+', '-', ':'};
			//char[] notContainChars = new char[] {':'};
			for (char c : trimmedQueryString.toCharArray()) {
			    if (Character.isWhitespace(c)) {
			    	//more than one token: not a term but a possible complex query string
			    	return false;
			    }
			    if (!containsSpecialChar) {
				    for (char ch : mightContainChars) {
						if (c==ch) {
							containsSpecialChar = true;
						}
					}
			    }
			}
			if (containsSpecialChar) {
				//.+  there is at least one character before the digit
				//? will ensure it does a lazy match instead of a greedy match
				boolean match = trimmedQueryString.matches("^.+?\\d$");
				LOGGER.debug("Match " + match + " possible project specific itemID " + queryString);
				return match;
			}
		}
		return false;
	}
	
	/**
	 * Gets the associated field indexers
	 * @return
	 */
	public static List<ILookupFieldSearcher> getLookupFieldSearchers() {
		List<ILookupFieldSearcher> lookupFieldSearchers = new LinkedList<ILookupFieldSearcher>();
		lookupFieldSearchers.add(NotLocalizedListSearcher.getInstance());
		lookupFieldSearchers.add(LocalizedListSearcher.getInstance());
		lookupFieldSearchers.add(LocalizedListCompositePartSearcher.getInstance());
		lookupFieldSearchers.add(ExternalListSearcher.getInstance());
		lookupFieldSearchers.add(AttachmentSearcher.getInstance());
		lookupFieldSearchers.add(LinkSearcher.getInstance());
		lookupFieldSearchers.add(ExpenseSearcher.getInstance());
		lookupFieldSearchers.add(BudgetPlanSearcher.getInstance());
		return lookupFieldSearchers;
	}

	/**
	 * 
	 * @param userQueryString
	 * @param fieldLabelToFieldNameMap
	 * @return
	 */
	private static String replaceFieldLabelsWithFieldNames(String userQueryString, Map<String, String>  fieldLabelToFieldNameMap) {
		LOGGER.debug("Before replacing the field labels to field names: " + userQueryString);
		for (Map.Entry<String, String> entry : fieldLabelToFieldNameMap.entrySet()) {
			userQueryString = replaceFieldLabelWithFieldName(userQueryString, entry.getKey(), entry.getValue(), 0);
		}
		LOGGER.debug("After replacing the field labels to field names: " + userQueryString);
		return userQueryString;
	}
	
	/**
	 * Replaces the label for a field with the objectID value
	 * @param analyzer 
	 * @param userQueryString a part of the user entered query string
	 * @param fieldLabel the workItem field name (like CRM Contact)
	 * @param fieldName the name of the user entered lucene field (like Company from CRM Contact)
	 * @param indexStart the index to start looking for fieldName 
	 * @return
	 */
	private static String replaceFieldLabelWithFieldName(
			String userQueryString, String fieldLabel, String fieldName, int indexStart) {
		int indexFound = LuceneSearcher.fieldNameIndex(userQueryString, fieldLabel, indexStart);
		if (indexFound==-1) {
			return userQueryString;
		}
		StringBuffer original = new StringBuffer(userQueryString);
		original.replace(indexFound, indexFound + fieldLabel.length(), fieldName);
		LOGGER.debug("Replace field label '" + fieldLabel + "' with field name '" + fieldName + "'");
		return replaceFieldLabelWithFieldName(original.toString(), fieldLabel, fieldName,
				indexFound + fieldName.length());
	}
	
	/**
	 * Whether the user entered query string contains at least one explicit fieldName
	 * @param userQueryString
	 * @return
	 */
	private static boolean fieldSpecified(String userQueryString, List<String> fieldNames, Map<String, Integer> compositeFieldIDByFieldName) {
		if (userQueryString==null) {
			return false;
		}
		fieldNames.add(LuceneUtil.ATTACHMENT);
		fieldNames.add(LuceneUtil.EXPENSE);
		fieldNames.add(LuceneUtil.BUDGET_PLAN);
		fieldNames.add(LuceneUtil.LINK);
		for (String fieldName : fieldNames) {
			int indexFound = userQueryString.indexOf(fieldName + FIELD_NAME_VALUE_SEPARATOR);
			if (indexFound!=-1) {
				return true;
			}
		}
		//search for composite parts
		for (String fieldName : fieldNames) {
			Integer compositeFieldID = compositeFieldIDByFieldName.get(fieldName);
			if (compositeFieldID!=null) {
				CustomCompositeBaseRT customCompositeBaseRT = (CustomCompositeBaseRT)FieldTypeManager.getFieldTypeRT(compositeFieldID);
				if (customCompositeBaseRT!=null) {
					int numberOfParts = customCompositeBaseRT.getNumberOfParts();
					for (int i = 0; i < numberOfParts; i++) {
						int indexFound = userQueryString.indexOf(LuceneUtil.synthetizeCompositePartFieldName(
								fieldName, Integer.valueOf(i+1)) + FIELD_NAME_VALUE_SEPARATOR);
						if (indexFound!=-1) {
							return true;
						}
					}
				}
			}
		}
		int indexFound = userQueryString.indexOf(FIELD_NAME_VALUE_SEPARATOR);
		if (indexFound!=-1) {
			String beforeStringValueSeparator = userQueryString.substring(0, indexFound); 
			String[] parts = beforeStringValueSeparator.split("\\s+");
			if (parts!=null && parts.length>0) {
				String lastWord = parts[parts.length - 1];
				if (lastWord!=null) {
					//the last word before ':' is not a valid field name
					LOGGER.info("The string " + lastWord + " is not a valid field name");
				}
			}
		}
		return false;
	}
	
	/**
	 * Get the list of all field names and the localized labels to look whether there is 
	 * explicit fieldName specified in the user entered search string 
	 * @return
	 */
	private static List<String> getAllFieldNames(
			Map<String, Integer> compositeFieldIDByFieldName, Map<String, String> fieldLabelToFieldNameMap, Locale locale) {
		List<String> fieldNames = new LinkedList<String>();
		Map<Integer, TFieldBean> fieldBeanCache = FieldTypeManager.getInstance().getFieldBeanCache();
		Map<Integer, TFieldConfigBean> fieldConfigsMap = FieldRuntimeBL.getLocalizedDefaultFieldConfigsMap(locale);
		Collection<TFieldBean> fieldBeans = fieldBeanCache.values();
		for (TFieldBean fieldBean : fieldBeans) {
			Integer fieldID = fieldBean.getObjectID();
			TFieldConfigBean fieldConfigBean = fieldConfigsMap.get(fieldID);
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null && fieldTypeRT.getValueType()==ValueType.EXTERNALID) {
				if (fieldTypeRT!=null && fieldTypeRT.getValueType()==ValueType.EXTERNALID) {
					try {
						IExternalLookupLucene externalLookupLucene = (IExternalLookupLucene)fieldTypeRT;
						String[] searchableFieldNames = externalLookupLucene.getSearchableFieldNames();
						if (searchableFieldNames!=null) {
							for (String searchableFieldName : searchableFieldNames) {
								fieldNames.add(LuceneUtil.normalizeFieldName(searchableFieldName));
							}
						}
					} catch (Exception e) {
						LOGGER.warn("Getting the field names for external lookup " + fieldID + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			} else {
				String fieldName = LuceneUtil.normalizeFieldName(fieldBean.getName());
				if (fieldTypeRT!=null && fieldTypeRT.isComposite() && compositeFieldIDByFieldName!=null) {
					compositeFieldIDByFieldName.put(fieldName, fieldID);
				}
				fieldNames.add(fieldName);
				if (fieldConfigBean!=null) {
					String fieldLabel = fieldConfigBean.getLabel();
					boolean isProjectSpecificIssueNo = fieldID.equals(SystemFields.INTEGER_ISSUENO) && ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn();
					if (EqualUtils.notEqual(fieldName, fieldLabel) || isProjectSpecificIssueNo) {
						if (isProjectSpecificIssueNo) {
							//replace localized "Issue No." label with projectSpecificIssueNo name
							TFieldBean issueNofieldBean = fieldBeanCache.get(SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO);
							if (issueNofieldBean!=null) {
								fieldName = issueNofieldBean.getName();
							}
						}
						fieldLabelToFieldNameMap.put(fieldLabel, fieldName);
					}
				}
			}
		}
		return fieldNames;
	}
	
	/**
	 * Preprocess the user entered query string:
	 * -	the (localized and not localized) lookup field labels are replaced with their keys from database
	 * -	the date fields are transformed to their string representation
	 * @param analyzer
	 * @param userQueryString the user entered query string
	 * @param locale
	 * @return
	 */
	private static String preprocess(Analyzer analyzer, String userQueryString, Locale locale) {
		String processedString = userQueryString;
		List<ILookupFieldSearcher> lookupFieldSearchers = getLookupFieldSearchers();
		LOGGER.debug("User query string: " + processedString);
		for (ILookupFieldSearcher lookupFieldSearcher : lookupFieldSearchers) {
			String processedStringNew = lookupFieldSearcher.preprocessExplicitField(analyzer, processedString, locale, 0);
			if (LOGGER.isDebugEnabled()) {
				if (!processedString.equals(processedStringNew)) {
					LOGGER.debug("Transformed to: " + processedStringNew);
				}
			}
			processedString = processedStringNew;
		}
		List<IntegerStringBean> dateFields = getFieldNamesForPreprocessType(LuceneUtil.PREPROCESSTYPES.DATE);
		if (dateFields!=null) {
			for (IntegerStringBean dateField : dateFields) {
				processedString = preprocessDate(processedString, dateField.getLabel(), 0, locale);
			}
		}
		List<IntegerStringBean> booleanFields = 
			getFieldNamesForPreprocessType(LuceneUtil.PREPROCESSTYPES.BOOLEAN_LOOKUP);
		if (booleanFields!=null) {
			for (IntegerStringBean booleanField : booleanFields) {
				processedString = preprocessBoolean(processedString,
						booleanField.getLabel(), 0, locale);
			}
		}
		List<IntegerStringBean> compositeFields = getFieldNamesForPreprocessType(LuceneUtil.PREPROCESSTYPES.COMPOSITE_LOOKUP);
		if (compositeFields!=null) {
			for (IntegerStringBean compositeField : compositeFields) {
				processedString = preprocessComposite(analyzer, processedString,
						compositeField.getLabel(), compositeField.getValue(), 0, locale);
			}
		}
		return processedString;
	}
	
	/**
	 * Preprocess a date field. 
	 * It could be a normal date or a date range
	 * In both cases the user entered date will be parsed to a date and then
	 * converted with DateTools.dateToString(date, Resolution.DAY);
	 * @param toBeProcessedString a part of the user entered query string
	 * @param fieldName the name of the user entered field
	 * @param indexStart the index to start looking for fieldName
	 * @param locale
	 * @return
	 */
	private static String preprocessDate(String toBeProcessedString,
			String fieldName, int indexStart, Locale locale) {
		int indexFound = fieldNameIndex(toBeProcessedString, fieldName, indexStart);
		if (indexFound==-1) {
			return toBeProcessedString;
		}
		int beginReplaceIndex = indexFound + fieldName.length() + 1;
		//gets the user entered value of the field
		//this could be a normal date or a date range
		String originalFieldValue = getFieldValue(toBeProcessedString.substring(beginReplaceIndex));
		if (originalFieldValue==null || "".equals(originalFieldValue)) {
			return toBeProcessedString;
		}	
		String processedFieldValue;
		if ((originalFieldValue.startsWith("[") || originalFieldValue.startsWith("{")) && 
				(originalFieldValue.endsWith("]") || originalFieldValue.endsWith("}"))) {
			String rangeBegin = originalFieldValue.substring(0,1);
			String rangeEnd = originalFieldValue.substring(originalFieldValue.length()-1);
			String strippedFieldValue = originalFieldValue.substring(1, originalFieldValue.length()-1);
			
			//if date range query we need two dates
			String[] dates;
			String date1 = null;
			String date2 = null;
			String splitString = " TO ";
			dates = strippedFieldValue.split(splitString);
			int i;
			String tmp;
			for (i=0; i<dates.length;i++) {
				tmp = transformDateFields(dates[i], locale);
				//parses to a date?
				if (!dates[i].equals(tmp)) {
					date1 = tmp;
					break;
				}
			}
			for (int j=i+1; j<dates.length;j++) {
				tmp = transformDateFields(dates[j], locale);
				//parses to a date?
				if (!dates[j].equals(tmp)) {
					date2 = tmp;
					break;
				}
			}
			if (date1!=null && date2!=null) {
				processedFieldValue = rangeBegin + date1 + splitString + date2 + rangeEnd;
			} else {
				processedFieldValue = originalFieldValue;
			}
		} else {
			processedFieldValue = transformDateFields(originalFieldValue, locale);
		}
		if (processedFieldValue==null || "".equals(processedFieldValue)) {
			return toBeProcessedString;
		}	
		StringBuffer original = new StringBuffer(toBeProcessedString);
		original.replace(beginReplaceIndex, beginReplaceIndex + originalFieldValue.length(), processedFieldValue);
		return preprocessDate(original.toString(), fieldName, beginReplaceIndex + processedFieldValue.length(), locale);
	}

	
	/**
	 * Preprocess a boolean field.
	 * Looks up the matchings by the localized name of the boolean value 
	 * 	
	 * @param toBeProcessedString a part of the user entered query string
	 * @param fieldName the name of the user entered field
	 * @param indexStart the index to start looking for fieldName
	 * @param locale
	 * @return
	 */
	private static String preprocessBoolean(String toBeProcessedString, 
			String fieldName, int indexStart, Locale locale) {
		int indexFound = fieldNameIndex(toBeProcessedString, fieldName, indexStart);
		if (indexFound==-1) {
			return toBeProcessedString;
		}
		int beginReplaceIndex = indexFound + fieldName.length() + 1;
		//gets the user entered value of the field
		//this could be a normal date or a date range
		String originalFieldValue = getFieldValue(toBeProcessedString.substring(beginReplaceIndex));
		if (originalFieldValue==null || "".equals(originalFieldValue)) {
			return toBeProcessedString;
		}
		String processedFieldValue = transformBooleanFields(originalFieldValue, locale);
		
		if (processedFieldValue==null || "".equals(processedFieldValue)) {
			return toBeProcessedString;
		}
		StringBuffer original = new StringBuffer(toBeProcessedString);
		original.replace(beginReplaceIndex, beginReplaceIndex + originalFieldValue.length(), processedFieldValue);
		return preprocessBoolean(original.toString(), fieldName, beginReplaceIndex + processedFieldValue.length(), locale);
	}
	
	/**
	 * Preprocess a composite field. 
	 * The composite string is separated into parts by the # character 
	 * @param analyzer
	 * @param toBeProcessedString a part of the user entered query string
	 * @param fieldName the name of the user entered field
	 * @param indexStart the index to start looking for fieldName
	 * @param locale
	 * @return
	 */
	private static String preprocessComposite(Analyzer analyzer, String toBeProcessedString,
			String fieldName, Integer fieldID, int indexStart, Locale locale) {
		int indexFound = fieldNameIndex(toBeProcessedString, fieldName, indexStart);
		if (indexFound==-1) {
			return toBeProcessedString;
		}
		int beginReplaceIndex = indexFound + fieldName.length() + 1;
		//gets the user entered value of the field
		String originalFieldValue = getFieldValue(toBeProcessedString.substring(beginReplaceIndex));
		if (originalFieldValue==null || "".equals(originalFieldValue)) {
			return toBeProcessedString;
		}	
		String processedFieldValue;
		//get rid of parenthesis and quotation marks from both ends of the entire originalFieldValue (if it is the case) 
		//because otherwise they would be interpreted as part of the searched strings
		//TODO what if by pc:(p1#c11) somebody want to find the string "(p1" for the first part and "c11)" for the second part?
		//then delete the following two if-s and make sure that if such case happens than the parentheses are part of the searched string 
		String strippedFieldValue = originalFieldValue;
		if (originalFieldValue.startsWith("(") && originalFieldValue.endsWith(")")) {
			strippedFieldValue = originalFieldValue.substring(1, originalFieldValue.length()-1);
		}
		if (originalFieldValue.startsWith("\"") && originalFieldValue.endsWith("\"")) {
			strippedFieldValue = originalFieldValue.substring(1, originalFieldValue.length()-1);
		}
		processedFieldValue = transformCompositeFields(analyzer, fieldName, fieldID, strippedFieldValue, locale);
		if (processedFieldValue==null || "".equals(processedFieldValue)) {
			return toBeProcessedString;
		}
		StringBuffer original = new StringBuffer(toBeProcessedString);
		original.replace(beginReplaceIndex- fieldName.length() - 1, beginReplaceIndex + originalFieldValue.length(), processedFieldValue);
		return preprocessComposite(analyzer, original.toString(), fieldName, fieldID, beginReplaceIndex + processedFieldValue.length(), locale);
	}
	
	/**
	 * Transforms the user entered date string to lucene date string format
	 * by trying to reconstruct the Date object either by local or by ISO (yyy-MM-dd)
	 * DateTools calculates in GMT (comparing to the server TimeZone), so it should be adjusted  
	 * @param originalFieldValue
	 * @param locale
	 * @return
	 */
	private static String transformDateFields(String originalFieldValue, Locale locale) {
		String dateString = originalFieldValue;
		Date date;
		//DateTimeUtils dtu = new DateTimeUtils(locale);
		date = DateTimeUtils.getInstance().parseGUIDate(originalFieldValue, locale);
		if (date==null) {
			date = DateTimeUtils.getInstance().parseShortDate(originalFieldValue, locale);
		}
		//set the date according to offset from the GMT
		//see http://www.gossamer-threads.com/lists/lucene/java-user/39303?search_string=DateTools;#39303
		Calendar cal = new GregorianCalendar();
		int minutesOffset = (cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET)) / (60 * 1000);
		if (date!=null) {
			cal.setTime(date);
			cal.add(Calendar.MINUTE, minutesOffset); 
			return DateTools.dateToString(cal.getTime(), Resolution.DAY);
		}
		date = DateTimeUtils.getInstance().parseISODate(originalFieldValue);
		if (date!=null) {
			cal.setTime(date);
			cal.add(Calendar.MINUTE, minutesOffset);
			return DateTools.dateToString(cal.getTime(), Resolution.DAY);
		}
		return dateString;
	}
	
	/**
	 * Transforms the user entered localized booelan string to a BooleanFields value ("Y" or "N") 
	 * @param originalFieldValue
	 * @param locale
	 * @return
	 */
	private static String transformBooleanFields(String originalFieldValue, Locale locale) {
		if (originalFieldValue==null) {
			return originalFieldValue;
		}
		String stringYes = LocalizeUtil.getLocalizedTextFromApplicationResources("common.boolean." + BooleanFields.TRUE_VALUE, locale);
		String stringNo = LocalizeUtil.getLocalizedTextFromApplicationResources("common.boolean." + BooleanFields.FALSE_VALUE, locale);
		if (originalFieldValue.equalsIgnoreCase(stringYes) || BooleanFields.TRUE_VALUE.equals(originalFieldValue) || Boolean.TRUE.toString().equals(originalFieldValue)) {
			return LuceneUtil.BOOLEAN_YES;
		}
		if (originalFieldValue.equalsIgnoreCase(stringNo) || BooleanFields.FALSE_VALUE.equals(originalFieldValue) || Boolean.FALSE.toString().equals(originalFieldValue)) {
			return LuceneUtil.BOOLEAN_NO;
		}
		return originalFieldValue;
	}
	
	/**
	 * Transform the composite field value into an AND sequence of their part values.
	 * When a part is null or empty string then do not include in the AND sequence 
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	private static String transformCompositeFields(Analyzer analyzer,
			String fieldName, Integer fieldID, String fieldValue, Locale locale) {
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID); //LuceneUtil.getFieldTypeRTByFieldName(fieldName);
		if (fieldTypeRT==null) {
			return fieldValue;
		}
		CustomCompositeBaseRT compositeFieldTypeRT = null;
		try {
			compositeFieldTypeRT = (CustomCompositeBaseRT)fieldTypeRT;
		} catch (Exception e) {
			LOGGER.error("Casting the runtime field type " + fieldTypeRT.getClass().getName() +
					" to CustomCompositeFieldTypeRT failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (compositeFieldTypeRT==null) {
			return fieldValue;
		}
		int noOfParts = compositeFieldTypeRT.getNumberOfParts();
		//for example pc:p\#1#c\#11 really means pc:p#1#c#11 for field part values p#1 and c#11, 
		//but the # characters in field value strings should be escaped, because otherwise 
		//they are considered field part separators for composite fields   
		String escapedFieldValue = fieldValue.replaceAll("\\\\" + LuceneUtil.COMPOSITE_FIELDVALUE_SEPARATOR, LuceneUtil.COMPOSITE_FIELDVALUE_SEPARATOR_ESCAPING_REPLACEMENT);
		
		String[] fieldValueParts = escapedFieldValue.split(LuceneUtil.COMPOSITE_FIELDVALUE_SEPARATOR, noOfParts);
		if (fieldValueParts==null || fieldValueParts.length==0) {
			LOGGER.error("No composite separator found in field value");
			return fieldValue;
		}
		if (fieldValueParts.length>noOfParts) {
			LOGGER.error("Number of parts needed by type " + noOfParts + " but in value found " + fieldValueParts.length);
			return fieldValue;
		}
		StringBuffer transformedString = new StringBuffer();
		transformedString.append("(");
		for (int i = 0; i < fieldValueParts.length; i++) {
			//do not take into account the missing values for a part: in this case search only for parent independently of child values 
			if (fieldValueParts[i]!=null && !"".equals(fieldValueParts[i].trim())) {
				if (transformedString.length()>1) {
					transformedString.append( " AND " );
				}
				//call the preprocess for each part of the composite (each part can be of any lookup type)
				transformedString.append(preprocess(analyzer, fieldName + "#" + (i+1) + FIELD_NAME_VALUE_SEPARATOR +
						//replace it back but this time without the \ character
						fieldValueParts[i].replaceAll(LuceneUtil.COMPOSITE_FIELDVALUE_SEPARATOR_ESCAPING_REPLACEMENT,
								LuceneUtil.COMPOSITE_FIELDVALUE_SEPARATOR), 
								locale));
			}
		}
		transformedString.append(")");
		return transformedString.toString();
	}
	
	/**
	 * Returns the index of the field name found in the toBeProcessedString or -1 if not found 
	 * @param toBeProcessedString
	 * @param fieldName
	 * @param indexStart
	 * @return
	 */
	public static int fieldNameIndex(String toBeProcessedString, String fieldName, int indexStart) {
		int indexFound = toBeProcessedString.indexOf(fieldName + FIELD_NAME_VALUE_SEPARATOR, indexStart);
		//field not found
		if (indexFound==-1) {
			return indexFound;
		}
		//do not take as match if there exists an another fieldName with the same name ending part as the searched field. 
		//For example: "Status" and "s": by looking for field "s" it would find also for field "Status"
		//so it should be verified whether indexFound is:
		//	- the first character 
		//	- is a whitespace before it.
		//	- is a '+' or '-' (boolean query syntax) before it but before these chars is a whitespace
		//If neither of them, do not consider it as match but search further in the toBeProcessedString
		/*if (indexFound>indexStart && !Character.isWhitespace(toBeProcessedString.charAt(indexFound-1))) {
			return fieldNameIndex(toBeProcessedString, fieldName, indexFound + fieldName.length() + 1);
		}*/
		if (indexFound>indexStart) {
			char lastCharBeforeFieldName = toBeProcessedString.charAt(indexFound-1);
			if (Character.isWhitespace(lastCharBeforeFieldName)) {
				//found the fieldName with a whitespace before
				return indexFound;
			}
			if (indexFound>indexStart+1) {
				char lastButOneCharBeforeFieldName = toBeProcessedString.charAt(indexFound-2);
				if ((lastCharBeforeFieldName=='+' || lastCharBeforeFieldName=='-') &&
						Character.isWhitespace(lastButOneCharBeforeFieldName)) {
					//found the fieldName with a + or - character before and a whitespace before these
					return indexFound;
				}
			}
			return fieldNameIndex(toBeProcessedString, fieldName, indexFound + fieldName.length() + 1);
		}
		return indexFound;
	}
	
	/**
	 * Preprocess a userQueryString which do not have explicit field(s) specified
	 * Uses some text fields as default fields but looks for the words 
	 * also in the other lookup indexes 
	 * @param analyzer 
	 * @param toBeProcessedString a part of the user entered query string
	 * @param locale 
	 * @return 
	 */
	private static Query preprocessNoExplicitField(Analyzer analyzer, 
			String toBeProcessedString, Set<Integer> itemTypeIDs, boolean external, Locale locale) throws ParseException {
		//direct workItem fields
		BooleanQuery finalQuery = new BooleanQuery();
		BooleanClause.Occur[] orFlags;
		Query workItemDirectQuery = null;
		//initialize the QueryParser with text fields as default fields
		BooleanQuery workItemQuery = new BooleanQuery();
		List<IntegerStringBean> directTextFieldNames = getFieldNamesForPreprocessType(LuceneUtil.PREPROCESSTYPES.DIRECT);
		String[] fieldNamesArr = new String[directTextFieldNames.size()];
		for (int i = 0; i < directTextFieldNames.size(); i++) {
			fieldNamesArr[i] = directTextFieldNames.get(i).getLabel();
		}
		orFlags = getOrFlagsArray(directTextFieldNames.size());
		try {
			workItemDirectQuery = MultiFieldQueryParser.parse(toBeProcessedString, fieldNamesArr, orFlags, analyzer);
		} catch (ParseException e) {
			LOGGER.warn("Parsing without explicit field for workItem fields (MultiFieldQueryParser) failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			throw e;
		} catch (Exception e) {
			LOGGER.warn("Parsing without explicit field for workItem fields (MultiFieldQueryParser) failed with throwable " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		//combine with or all occurrences LOOKUPENTITYTYPES
		if (workItemDirectQuery!=null) {
			workItemQuery.add(workItemDirectQuery, BooleanClause.Occur.SHOULD);
		}
		List<ILookupFieldSearcher> lookupFieldSearchers = getLookupFieldSearchers();
		for (ILookupFieldSearcher lookupFieldSearcher : lookupFieldSearchers) {
			Query query = lookupFieldSearcher.getNoExplicitFieldQuery(analyzer, toBeProcessedString, locale);
			if (query!=null) {
				workItemQuery.add(query, BooleanClause.Occur.SHOULD);
			}
		}
		finalQuery.add(workItemQuery, BooleanClause.Occur.MUST);
		Query itemTypeQuery = getItemTypeQuery(itemTypeIDs, analyzer);
		if (itemTypeQuery!=null) {
			LOGGER.debug("The item type query: " + itemTypeQuery.toString());
			BooleanClause.Occur occur = null;
			if (external) {
				occur = BooleanClause.Occur.MUST;
			} else {
				occur = BooleanClause.Occur.MUST_NOT;
			}
			finalQuery.add(itemTypeQuery, occur);
		}
		return finalQuery;
	}
	
	/**
	 * Gets the itemType specific query 
	 * @param itemTypeIDs
	 * @param exclude
	 * @return
	 */
	private static Query getItemTypeQuery(Set<Integer> itemTypeIDs, Analyzer analyzer) throws ParseException {
		Query itemTypeQuery = null;
		if (itemTypeIDs!=null && itemTypeIDs.size()>0) {
			String itemTypeName = LuceneUtil.normalizeFieldName(FieldBL.loadByPrimaryKey(SystemFields.INTEGER_ISSUETYPE).getName());
			StringBuffer directQuery = new StringBuffer();
			String orDividedIDs = LuceneSearcher.createORDividedIDs(itemTypeIDs);
						directQuery.append(itemTypeName + LuceneSearcher.FIELD_NAME_VALUE_SEPARATOR + orDividedIDs);
			QueryParser queryParser = new QueryParser(itemTypeName, analyzer);
			try {
				itemTypeQuery = queryParser.parse(orDividedIDs);
			} catch (ParseException e) {
				LOGGER.warn("Parsing item types field  for " + orDividedIDs + " field failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				throw e;
			}
		}
		return itemTypeQuery;
	}
	
	/**
	 * returns an array of BooleanClause.Occur's with a specific length
	 * @param length
	 * @return
	 */
	public static BooleanClause.Occur[] getOrFlagsArray(int length) {
		BooleanClause.Occur[] orFlags = new BooleanClause.Occur[length];
		for (int i=0; i<length; i++) {
			orFlags[i] = BooleanClause.Occur.SHOULD;
		}
		return orFlags;
	}
	
	/**
	 * Get the string array of field names for a preprocess type
	 * It includes also the parts of the composite types if the 
	 * preprocessType is not LuceneUtil.PREPROCESSTYPES.COMPOSITE_LOOKUP
	 * In this case the fieldName is synthesized from the composite field name and the parameterCode
	 * @return
	 */
	private static List<IntegerStringBean> getFieldNamesForPreprocessType(int preprocessType) {
		List<IntegerStringBean> fieldList = new LinkedList<IntegerStringBean>();
		Map<Integer, TFieldBean> fieldBeanCache = FieldTypeManager.getInstance().getFieldBeanCache();
		Map<Integer, FieldType> typeCache = FieldTypeManager.getInstance().getTypeCache();
		Set<Map.Entry<Integer, FieldType>> fieldTypes = typeCache.entrySet();
		for (Map.Entry<Integer, FieldType> fieldTypeEntry : fieldTypes) {
			Integer fieldID = fieldTypeEntry.getKey();
			FieldType fieldType = fieldTypeEntry.getValue();
			if (fieldType!=null) {
				IFieldTypeRT fieldTypeRT = fieldType.getFieldTypeRT();
					if (LuceneUtil.getPreprocessType(fieldTypeRT.getLookupEntityType())==preprocessType) {
						TFieldBean fieldBean = fieldBeanCache.get(fieldID);
						fieldList.add(new IntegerStringBean(LuceneUtil.normalizeFieldName(fieldBean.getName()), fieldID));
					}
			}
		}
		return fieldList; 
	}
	
	/**
	 * Gets the user entered value assigned to a fieldName
	 * @param queryString a part of the user entered query string
	 * @return
	 */
	public static String getFieldValue(String queryString) {
		String toBeReplacedString = "";
		if (queryString==null || queryString.length()==0) {
			return toBeReplacedString;
		}
		//fist char in the field value is after "FieldName:"
		char firstChar = queryString.charAt(0);
		int index = 0;
		switch (firstChar) {
		//grouping boolean queries
		case '(':
			index = findEmbeddableClosingIndex(queryString, '(', ')');
			break;
		//range queries
		case '[':
			index = findSimpleClosingIndex(queryString, ']');
			break;
		//range queries
		case '{':
			index = findSimpleClosingIndex(queryString, '}');
			break;
		//phrase queries
		case '"':	
			index = findSimpleClosingIndex(queryString, '"');
			break;
		//term queries
		default:
			index = findWhiteSpace(queryString);
			break;
		}
		if (index>0) {
			toBeReplacedString = queryString.substring(0, index);
		}
		return toBeReplacedString;
	}
	
	/**
	 * Find the index of the closing character 
	 * The parentheses could be embedded in several levels and can be escaped 
	 * @param queryString a part of the user entered query string
	 * @param open character at the beginning 
	 * @param close character at the end
	 * @return
	 */
	private static int findEmbeddableClosingIndex(String queryString, char open, char close) {
		int parenthesesCount = 0;
		int counter = 0;
		char currentChar;		
		while (parenthesesCount!=0 || counter==0) {
			if (counter == queryString.length()) {
				return -1;
			}
			currentChar = queryString.charAt(counter++);
			if (currentChar == open) {
				//it is not escaped
				if (counter<2 || !isLastCharEscaped(queryString.substring(0, counter))) { //counter==1 || (counter > 1 && queryString.charAt(counter-2)!='\\')) 
					parenthesesCount++;
				}
			}
			if (currentChar == close) {
				//it is not escaped
				if (counter<2 || !isLastCharEscaped(queryString.substring(0, counter))) {  //counter > 1 && queryString.charAt(counter-2)!='\\')
					parenthesesCount--;
				}
			}
		}
		//till the end or the first white space 
		//because of the phrase queries with ~
		while (true) {
			if (counter == queryString.length()) {
				return counter;
			}
			currentChar = queryString.charAt(counter++);
			if (Character.isWhitespace(currentChar)) {
				return counter-1;
			}
		}
	}
	
	/**
	 * 
	 * Find the index of the closing character
	 * Used for the other (not embeddable) characters
	 * only the escaping should be taken into account  
	 * @param queryString a part of the user entered query string
	 * @param close character at the end
	 * @return
	 */
	private static int findSimpleClosingIndex(String queryString, char close) {
		//start from 1 because the index at 1 is the close character
		int counter = 1;
		char currentChar;
		boolean found = false;
		while (!found) {
			if (counter == queryString.length()) {
				return -1;
			}
			currentChar = queryString.charAt(counter++);
			if (currentChar == close) {
				//it is not escaped
				if (counter<2 || !isLastCharEscaped(queryString.substring(0, counter))) { //counter > 1 && queryString.charAt(counter-2)!='\\') {
					found = true;
				}
			}
		}
		//till the end or the first white space 
		//because of the phrase queries with ~
		while (true) {
			if (counter == queryString.length()) {
				return counter;
			}
			currentChar = queryString.charAt(counter++);
			if (Character.isWhitespace(currentChar)) {
				return counter-1;
			}
		}
	}
	
	/**
	 * Whether the last character form the string is escaped
	 * (odd number of backslah characters)
	 * @param queryString
	 * @return
	 */
	private static boolean isLastCharEscaped(String queryString) {
		char escapeChar = '\\';
		int counter = queryString.length()-1;
		boolean escaped = false;
		while (counter>0 && queryString.charAt(--counter)==escapeChar) {
			escaped = !escaped;
		}
		return escaped;
	}
	
	/**
	 * Find the index of the closing character
	 * used for finding the closing white spaces  
	 * @param queryString a part of the user entered query string	
	 * @return
	 */
	private static int findWhiteSpace(String queryString) {
		int counter = 0;
		char currentChar;
		//till the end or the first white space 
		while (true) {
			if (counter == queryString.length()) {
				return counter;
			}
			currentChar = queryString.charAt(counter++);
			if (Character.isWhitespace(currentChar)) {
				return counter-1;
			}
		}
	}
	
	/**
	 * Prepares an Indexsearcher object for an Index
	 * @param index
	 * @return
	 */	
	public static IndexSearcher getIndexSearcher(int index) {
		Directory indexDir = LuceneUtil.getIndexDirectory(index);
		if (indexDir==null) {
			LOGGER.error("The index directory for " + index + " doesn't exist or is not a directory");
			return null;
		}
		//initialize the searcher
		//we will initialize always a new searcher because the data should be up to date
		/*
		 * 	Lucene FAQ: 
		 * 	1.	Make sure to open a new IndexSearcher after adding documents. 
		 * 		An IndexSearcher will only see the documents that were in the index in the moment it was opened.
		 * 	2.	It is recommended to use only one IndexSearcher from all threads in order to save memory
		 * 	Thanks for your help :) 
		 */
		IndexSearcher is = null;
		try {
			IndexReader indexReader = DirectoryReader.open(indexDir);
			is = new IndexSearcher(indexReader);
		} catch (IOException e) {
			LOGGER.warn("Initializing the IndexSearcher for index " + index + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return is;
	}
	
	public static void closeIndexSearcherAndUnderlyingIndexReader(IndexSearcher indexSearcher, String index) {
		if (indexSearcher != null) {
			IndexReader indexReader = indexSearcher.getIndexReader();
			try {
				if (indexReader!=null) {
					indexReader.close();
				}
			} catch (IOException e1) {
				LOGGER.error("Closing the " + index + " IndexReader failed with " + e1.getMessage());
			}
		}
	}
	
	/**
	 * Creates the OR divided workItemIDs
	 * @param objectIDs
	 * @return
	 */
	public static String createORDividedIDs(Set<Integer> objectIDs) {
		StringBuilder stringBuilder = new StringBuilder();
		if (objectIDs!=null && !objectIDs.isEmpty()) {
			if (objectIDs.size()==1) {
				for (Integer objectID : objectIDs) {
					if (objectID!=null && objectID.intValue()<0) {
						//FIXME: escape the - sign if negative, but \\ does not work. As fix a range query is used: within [ ] the minus sign works
						//stringBuilder.append("\\"+objectID);
						stringBuilder.append("["+objectID + " TO " + objectID + "]");
					} else {
						stringBuilder.append(objectID);
					}
				}
			} else {
				stringBuilder.append("(");
				for (Iterator<Integer> iterator = objectIDs.iterator(); iterator.hasNext();) {
					Integer objectID = iterator.next();
					if (objectID!=null && objectID.intValue()<0) {
						//FIXME: escape the - sign if negative, but \\ does not work. As fix a range query is used: within [ ] the minus sign works
						stringBuilder.append("["+objectID + " TO " + objectID + "]");
					} else {
						stringBuilder.append(objectID);
					}
					if (iterator.hasNext()) {
						stringBuilder.append(" OR ");
					}
				}
				stringBuilder.append(")");
			}
		}
		return stringBuilder.toString(); 
	}
}
	
