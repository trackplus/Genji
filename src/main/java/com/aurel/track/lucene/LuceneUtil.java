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

package com.aurel.track.lucene;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.SiteDAO;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.PluginUtils;

/**
 *
 * @author  Tamas Ruff
 * @version $Revision: 1799 $
 *
 */
public class LuceneUtil {

	private static final Logger LOGGER = LogManager.getLogger(LuceneUtil.class);
	private static SiteDAO siteDAO = DAOFactory.getFactory().getSiteDAO();
	//the separator which should be entered by the end-user to split the parts of a composite value
	//for example pc:p1#c11 means the field with name pc (parent child) 
	//and first part (parent value) is p1 and second part (child value) is c11
	//if the separator string is part of the string value it should be escaped with \
	//Ex. pc:p\#1#c\#11 means search for p#1 as parent value and c#11 as child value 
	public static String COMPOSITE_FIELDVALUE_SEPARATOR = "#";
	//the temporary replacement for field value separator to execute the split correctly
	public static String COMPOSITE_FIELDVALUE_SEPARATOR_ESCAPING_REPLACEMENT = "hopefully_not_used_string_if_used_bad_luck";
	
	//the separator of the composite part field for synthetize the lucene field name
	private static String COMPOSITE_FIELDNAME_SEPARATOR = "#";
	
	//the symbolic boolean values stored in the index because "Y" and "N" does not work (it is filtered out by the analyzer?)
	public static String BOOLEAN_YES = "yesnotfilteredoutbyanalyzer";
	public static String BOOLEAN_NO = "nonotfilteredoutbyanalyzer";
	
	public static String HIGHLIGHTER_FIELD = "Highlighter";
	
	/**
	 * Lucene index identifier constants
	 *
	 */
	public static interface INDEXES {
		public static final int WORKITEM_INDEX = 1;
		public static final int NOT_LOCALIZED_LIST_INDEX = 2;
		public static final int LOCALIZED_LIST_INDEX = 3;
		public static final int ATTACHMENT_INDEX = 4;
		public static final int EXTERNAL_LOOKUP_WRITER = 5;
		public static final int LINK_INDEX = 6;
		public static final int EXPENSE_INDEX = 7;
		public static final int BUDGET_PLAN_INDEX = 8;
	}
	
	/**
	 * Config parameters for lucene
	 *
	 */
	public static interface LUCENESETTINGS {
		public static final String USELUCENE = "useLucene";
		public static final String INDEXATTACHMENTS = "indexAttachments";
		public static final String ANALYZER = "analizer";
		public static final String INDEXPATH = "indexPath";
		public static final String REINDEXONSTARTUP = "reindexOnStartup";
	}
	
	/**
	 * Constants to avoid referencing lucene store constants from the fieldType classes 
	 * @author Tamas Ruff
	 *
	 */
	public static interface STORE {
		public static final int YES = 1;
		public static final int NO = 0;
	}
	
	/**
	 * Constants to avoid referencing lucene tokenize constants from the fieldType classes 
	 * @author Tamas Ruff
	 *
	 */
	public static interface TOKENIZE {
		public static final int YES = 1;
		public static final int NO = 0;
	}
	
	/**
	 * Helper method to transform the symbolic store constants to lucene store constants  
	 * @param store
	 * @return
	 */
	public static Field.Store getLuceneStore(int store) {
		switch (store) {
		case STORE.YES:
			return Field.Store.YES;
		case STORE.NO:
			return Field.Store.NO;
		default:
			return Field.Store.NO;
		}
	}
	
	/**
	 * Helper method to transform the symbolic tokenized constants to lucene tokenized constants  
	 * @param tokenized
	 * @return
	 */
	
	/**
	 * Lookup entity types defined
	 *
	 */
	public static interface LOOKUPENTITYTYPES {
		//not stored (calculated) value
		public static int NOSTORE = 0;
		//direct values
		public static int DIRECTTEXT = 1;
		public static int DIRECTDATE = 2;
		public static int DIRECTINTEGER = 3;
		public static int DIRECTDOUBLE = 4;
		public static int DIRECTBOOLEAN = 5;
		//localized lookup types
		public static int LISTTYPE = 10;
		public static int PRIORITY = 11;
		public static int SEVERITY = 12;
		public static int STATE = 13;
		//not localized lookup types
		public static int PROJECT = 20;
		//public static int CLASS = 22;
		public static int RELEASE = 23;
		public static int PERSONNAME = 24;
		public static int CUSTOMOPTION = 25;
		//composite values
		public static int COMPOSITE = 30;
		
		public static int EXTERNALID = 40;
	}
	
	
	/**
	 * Preprocess types defined
	 * Depending on the preprocess type different preprocessing is needed
	 */
	public static interface PREPROCESSTYPES {
		//stored directly, no preprocessing of the user entered values needed
		public static int DIRECT = 1;
		//stored a date directly, preprocessing needed to format the date to the format used by lucene (YYYYMMDD) 
		public static int DATE = 2;
		//localized boolen strings: YES/NO JA/NEIN
		public static int BOOLEAN_LOOKUP = 3;
		//stored in the not localized lookup index, 
		//preprocessing needed to replace the user entered values with the matching lookup IDs 
		public static int NOT_LOCALIZED_LOOKUP = 4;
		//stored in the localized lookup index, 
		//preprocessing needed to replace the user entered values with the matching lookup IDs
		public static int LOCALIZED_LOOKUP = 5;
		//preprocessing needed to replace the user entered (#-divided) 
		//composite values with the matching parts
		public static int COMPOSITE_LOOKUP = 6;
		//preprocessing needed to replace the user entered values with the matching external lookup IDs
		public static int EXTERNAL_LOOKUP = 7;
		//no store
		public static int NOSTORE = 8;
	}
	
	/**
	 * Get the preprocess type needed by lookupEntityType
	 * Determines which preprocessing is needed for a lookupEntityType
	 * @param lookupEntityType
	 * @return
	 */
	public static int getPreprocessType(int lookupEntityType) {
		switch (lookupEntityType) {
		case LOOKUPENTITYTYPES.DIRECTTEXT:
		case LOOKUPENTITYTYPES.DIRECTINTEGER:
		case LOOKUPENTITYTYPES.DIRECTDOUBLE:
			return PREPROCESSTYPES.DIRECT;
		case LOOKUPENTITYTYPES.DIRECTDATE:
			return PREPROCESSTYPES.DATE;
		case LOOKUPENTITYTYPES.DIRECTBOOLEAN:
			return PREPROCESSTYPES.BOOLEAN_LOOKUP;
		case LOOKUPENTITYTYPES.LISTTYPE:
		case LOOKUPENTITYTYPES.PRIORITY:
		case LOOKUPENTITYTYPES.SEVERITY:
		case LOOKUPENTITYTYPES.STATE:
		case LOOKUPENTITYTYPES.CUSTOMOPTION:
			return PREPROCESSTYPES.LOCALIZED_LOOKUP;
		case LOOKUPENTITYTYPES.PROJECT:
		case LOOKUPENTITYTYPES.PERSONNAME:
		case LOOKUPENTITYTYPES.RELEASE:	
			return PREPROCESSTYPES.NOT_LOCALIZED_LOOKUP;
		case LOOKUPENTITYTYPES.COMPOSITE:
			return PREPROCESSTYPES.COMPOSITE_LOOKUP;
		case LOOKUPENTITYTYPES.EXTERNALID:
			return PREPROCESSTYPES.EXTERNAL_LOOKUP;
		case LOOKUPENTITYTYPES.NOSTORE:
			return PREPROCESSTYPES.NOSTORE;
		}
		return PREPROCESSTYPES.DIRECT;
	}
	
	/**
	 * The hardcoded name of the attachment field
	 * TODO make it configurable (how?)
	 */
	public static String ATTACHMENT = "Attachment";
	/**
	 * Link descriptions
	 */
	public static String LINK = "Link";
	/**
	 * Expense subject or description
	 */
	public static String EXPENSE = "Expense";
	/**
	 * Plan description
	 */
	public static String BUDGET_PLAN = "BudgetPlan";

	
	/**
	 * Gets the name of the field by fieldID
	 */
	public static String getFieldName(int fieldID) {
		return FieldTypeManager.getInstance().getFieldBean(Integer.valueOf(fieldID)).getName();
	}
	
	/**
	 * the java Properties file accepts no spaces in the key
	 * we replace all spaces with underscore  
	 * @param fieldName
	 * @return
	 */
	public static String normalizeFieldName(String fieldName) {
		if (fieldName==null) {
			fieldName = "";
		}
		return fieldName.trim().replaceAll(" ","_");
	}
	
	/**
	 * the java Properties file accepts no spaces in the key
	 * we replace all spaces with underscore  
	 * @param fieldName
	 * @param parameterCode
	 * @return
	 */
	public static String synthetizeCompositePartFieldName(String fieldName, Integer parameterCode) {
		return fieldName + COMPOSITE_FIELDNAME_SEPARATOR + parameterCode;
	}
	
	/**
	 * lucene field names for external lookup types
	 * the labels of external lookups are defined in the corresponding field type
	 *
	 */
	public static class EXTERNAL_INDEX_FIELDS {
		public static String FIELDID = "FieldID";
		//used only by delete, because it works only with Term (BooleanQuery not possible)
		public static String COMBINEDKEY = "CombinedKey";
		public static String OBJECTID = "ObjectID";
	}
	
	/**
	 * lucene field names for the not localized lookup types
	 *
	 */
	public static class LIST_INDEX_FIELDS_NOT_LOCALIZED {
		public static String LABEL = "Label";
		public static String VALUE = "Value";
		public static String TYPE = "Type";
		//used only by delete, because it works only with Term (BooleanQuery not possible)
		public static String COMBINEDKEY = "CombinedKey";
	}
	
	/**
	 * lucene field names for the localized lookup types
	 *
	 */
	public static class LIST_INDEX_FIELDS_LOCALIZED {
		//there is a field name for each locale found but these are not previously known
		//they look like en_US or de
		//the key from the lookup table 
		public static String VALUE = "Value";
		//the not localized label from the lookup table
		public static String LABEL = "Label";
		//normally same as Label (could differ by initializing the db: severity 'major' in db, 'serious' in defualt localized)
		public static String DEFAULT_LOCALIZED = "DefaultLocalized";
		//the localized entity type
		public static String TYPE = "Type";
		//used only by delete, because it works only with Term (BooleanQuery not possible)
		public static String COMBINEDKEY = "CombinedKey";
	}
	
	/**
	 * lucene field names for the not localized lookup types
	 *
	 */
	public static class ATTACHMENT_INDEX_FIELDS {
		public static String ATTACHMENTID = "AttachmentID";
		public static String ISSUENO = "IssueNo";
		//original name of the file
		public static String ORIGINALNAME = "OriginalName";
		//the real name of the file
		public static String REALNAME = "RealName";
		//public static String COMBINEDKEY = "CombinedKey";
		//the content of the file
		public static String CONTENT = "Content";
		//the user entered description for an attachment
		public static String DESCRIPTION = "Description";
	}
		
	/**
	 * lucene field names for the links
	 *
	 */
	public static class LINK_INDEX_FIELDS {
		public static String LINKID = "LinkID";
		public static String LINKPRED = "LinkPred";
		public static String LINKSUCC = "LinkSucc";
		public static String BIDIRECTIONAL = "Bidirectional";
		//the user entered description for link
		public static String DESCRIPTION = "Description";
	}
	
	/**
	 * lucene field names for the expenses
	 *
	 */
	public static class EXPENSE_INDEX_FIELDS {
		public static String EXPENSEID = "ExpenseID";
		public static String WORKITEMID = "WorkitemID";
		public static String SUBJECT = "Subject";
		public static String DESCRIPTION = "Description";
	}
	
	/**
	 * lucene field names for the plans
	 *
	 */
	public static class BUDGET_INDEX_FIELDS {
		public static String PLANID = "PlanID";
		public static String WORKITEMID = "WorkitemID";
		public static String DESCRIPTION = "Description";
		public static String BUDGET_TYPE = "BudgetType";
	}
	
	/**
	 * name of the application context attribute storing the LuceneIndexer
	 */
	//public static String LUCENEINDEXER = "LUCENEINDEXER";
	
	/**
	 * lucene settings 
	 */
	private static List<LabelValueBean> foundAnalysers = new ArrayList<LabelValueBean>();
	private static Analyzer analyzer;
	private static String path;
	private static boolean useLucene;
	private static boolean indexAttachments;
	public static boolean reindexOnStartup;
	
	public static String LUCENEMAINDIR = "LuceneIndexes";
	private static String WORKITEM_DIRECTORY = "WorkItemIndex";	
	private static String NOTLOCALIZED_LOOKUPFIELDS_DIRECTORY = "NotLocalizedLookupIndex";
	private static String LOCALIZED_LOOKUPFIELDS_DIRECTORY = "LocalizedLookupIndex";
	private static String ATTACHMENT_DIRECTORY = "AttachmentIndex";
	private static String EXTERNAL_LOOKUPFIELDS_DIRECTORY = "ExternalLookupIndex";
	private static String LINK_DIRECTORY = "LinkIndex";
	private static String EXPENSE_DIRECTORY = "ExpenseIndex";
	private static String BUDGET_PLAN_DIRECTORY = "BudgetPlanIndex";
	
	
	/**
	 * @return Returns the analyzer.
	 */
	public static Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * @param analyzer The analyzer to set.
	 */
	public static void setAnalyzer(Analyzer analyzer) {
		LuceneUtil.analyzer = analyzer;
	}

	/**
	 * @return Returns the foundAnalysers.
	 */
	public static List<LabelValueBean> getFoundAnalysers() {
		return foundAnalysers;
	}

	/**
	 * @param foundAnalysers The foundAnalysers to set.
	 */
	public static void setFoundAnalysers(List<LabelValueBean> foundAnalysers) {
		LuceneUtil.foundAnalysers = foundAnalysers;
	}

	/**
	 * @return Returns the indexAttachments.
	 */
	public static boolean isIndexAttachments() {
		return indexAttachments;
	}

	/**
	 * @param indexAttachments The indexAttachments to set.
	 */
	public static void setIndexAttachments(boolean indexAttachments) {
		LuceneUtil.indexAttachments = indexAttachments;
	}

	/**
	 * @return Returns the path.
	 */
	public static String getPath() {
		return path;
	}

	/**
	 * @param path The path to set.
	 */
	public static void setPath(String path) {
		LuceneUtil.path = path;
	}

	/**
	 * @return Returns the reindexOnStartup.
	 */
	public static boolean isReindexOnStartup() {
		return reindexOnStartup;
	}

	/**
	 * @param reindexOnStartup The reindexOnStartup to set.
	 */
	public static void setReindexOnStartup(boolean reindexOnStartup) {
		LuceneUtil.reindexOnStartup = reindexOnStartup;
	}

	
	/**
	 * @return Returns the useLucene.
	 */
	public static boolean isUseLucene() {
		return useLucene;
	}

	/**
	 * @param useLucene The useLucene to set.
	 */
	public static void setUseLucene(boolean useLucene) {
		LuceneUtil.useLucene = useLucene;
	}

	/**
	 * creation of the combined field value based on the field value and field type
	 * @param value
	 * @param type
	 * @return
	 */
	public static String getCombinedKeyValue(String value, String type) {
		return value + "|" + type;
	}
	
	/**
	 * Gets the context path of the application
	 * @return
	 */
	public static String getContexPath() {
		URL url = null;
		File file;
		//get the application context path
		url = LuceneUtil.class.getResource("/../..");
		if (url == null) {
			return null;
		}		
		if (url.getPath()!=null) {
			file = new File(url.getPath());
			if (file.exists()) {
				return stripTrailingPathSeparator(url.getPath());
			}
		}
		//see Bug ID:  4466485 (http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4466485) 
		URI uri = null;
		try {
			uri = new URI(url.toString());
		} catch (URISyntaxException e) {
			LOGGER.error("Getting the lucene index root URI from URL failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}	  	
		if (uri == null) {
			return null;
		}
		//sometimes it is enough
		if (uri.getPath()!=null) {
			file = new File(uri.getPath());
			if (file.exists())
			{
				return stripTrailingPathSeparator(uri.getPath());
			}
		}
		
		try {
			url = uri.toURL();
		} catch (MalformedURLException e) {
		}
		if (url!=null && url.getPath()!=null) {
			file = new File(url.getPath());
			if (file.exists()) {
				return stripTrailingPathSeparator(url.getPath());
			}
		}
		return "";
	}
	
	public static String getDirectoryString(int index) {
		String indexDir;
		switch(index) {
		case INDEXES.WORKITEM_INDEX:
			indexDir = WORKITEM_DIRECTORY;
			break;
		case INDEXES.NOT_LOCALIZED_LIST_INDEX:
			indexDir = NOTLOCALIZED_LOOKUPFIELDS_DIRECTORY;
			break;
		case INDEXES.LOCALIZED_LIST_INDEX:
			indexDir = LOCALIZED_LOOKUPFIELDS_DIRECTORY;
			break;
		case INDEXES.ATTACHMENT_INDEX:
			indexDir = ATTACHMENT_DIRECTORY;
			break;
		case INDEXES.EXTERNAL_LOOKUP_WRITER:
			indexDir = EXTERNAL_LOOKUPFIELDS_DIRECTORY;
			break;
		case INDEXES.LINK_INDEX:
			indexDir = LINK_DIRECTORY;
			break;
		case INDEXES.EXPENSE_INDEX:
			indexDir = EXPENSE_DIRECTORY;
			break;
		case INDEXES.BUDGET_PLAN_INDEX:
			indexDir = BUDGET_PLAN_DIRECTORY;
			break;
		default:
			indexDir = WORKITEM_DIRECTORY;
			break;
		}
		return indexDir;
	}
	
	/**
	 * Gets the directory for an index based on the index number
	 * @param index
	 * @return
	 */
	public static Directory getIndexDirectory(int index) {
		String indexDir = getDirectoryString(index);
		String filePath = stripTrailingPathSeparator(LuceneUtil.getPath());
		if (filePath==null) {
			filePath = "";
		}
		//filePath = filePath.replace('\\', '/');
		String indexPath;
		if ("".equals(filePath.trim())) {
			//relative path
			indexPath = getContexPath() +
						"/" + 
						LuceneUtil.LUCENEMAINDIR + "/" + 
						indexDir;
		} else {
			//absolute path
			indexPath = filePath + "/" + 
						LuceneUtil.LUCENEMAINDIR + "/" + 
						indexDir;
		}
		File directoryFile = new File(indexPath);
		boolean created;
		//if not exists create a file
		if (!directoryFile.exists()) {
			created = directoryFile.mkdirs();
			if (!created) {
				LOGGER.error("Can't create the directory " + filePath);
			}
		} else {
			//if exists with this name but is not a directory
			//the file should be deleted manually
			if (!directoryFile.isDirectory()) {
				LOGGER.error("Can't create the directory because a file with this name exists " + filePath);
			}
		}
		Directory directory = null;
		try {
			directory = new SimpleFSDirectory(directoryFile.toPath());
		} catch (IOException e) {
			LOGGER.error("Can't create a SimpleFSDirectory because a file with this name exists " + filePath);
		}
		return directory;
	}

	/**
	 * strip the trailing pathseparator if exists
	 * @param path
	 * @return
	 */
	private static String stripTrailingPathSeparator(String path) {
		if (path == null) {
			return "";
		}
		if (path.endsWith("/")) {
			path = path.substring(0, path.length()-1);
		}
		return path;
	}
	
	/**
	 * get the list of analyzers found in the system
	 * @param servletContext
	 * @return
	 */
	public static List<LabelValueBean> getAnalyzersList(ServletContext servletContext) {
		Set<String> foundAnalyzersSet = new TreeSet<String>();
		List<LabelValueBean> analyzersBeans = new ArrayList<LabelValueBean>();
		//we specify it explicitly because in the worst case we should have at least this analyzer
		//but may be we should not specify it, because nobody guarantees that this will be included 
		//in the future lucene implementations also, and then the lucene.jar could not be changed 
		foundAnalyzersSet.add(StandardAnalyzer.class.getName());
				//we search just in jars
		//(in the classes folder not, because it is not probable that a new analyzer will be written inside the Genji)
		foundAnalyzersSet.addAll(PluginUtils.getAnalyzersFromJars(servletContext));
		
		//create the analyzer beans to be shown to the user
		for (String className : foundAnalyzersSet) {
			int nameIndex = className.lastIndexOf(".");
			analyzersBeans.add(new LabelValueBean(className.substring(nameIndex+1), className));
		}
		return analyzersBeans;
	}
	
	/**
	 * Initializes the preferences field of a TSite object with default lucene parameters 
	 * It is used the first time the application is launched before the new TSite object is saved 
	 *
	 */
	public static void initLuceneParameters() {
		TSiteBean siteBean = siteDAO.load1();
		//use lucene by default
		String useLuceneStr = siteBean.getUseLucene();
		if (useLuceneStr == null || "".equals(useLuceneStr.trim())) {
			useLuceneStr = "true";
			Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.USELUCENE, useLuceneStr);
			siteDAO.loadAndSaveSynchronized(siteBeanValues);
		}
		//index attachments by default
		String indexAttachmentsStr = siteBean.getIndexAttachments();
		if (indexAttachmentsStr == null || "".equals(indexAttachmentsStr.trim())) {
			indexAttachmentsStr = "true";
			Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.INDEXATTACHMENTS, indexAttachmentsStr);
			siteDAO.loadAndSaveSynchronized(siteBeanValues);
		}
		//do not reindex by default
		String reindexOnStartup = siteBean.getReindexOnStartup();
		if (reindexOnStartup == null || "".equals(reindexOnStartup.trim())) {
			reindexOnStartup = "false";
			Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.REINDEXONSTARTUP, reindexOnStartup);
			siteDAO.loadAndSaveSynchronized(siteBeanValues);
		}
		//set the standard analyzer by default
		String analyzer = siteBean.getAnalyzer();
		if (analyzer == null || "".equals(analyzer.trim())) {
			analyzer = StandardAnalyzer.class.getName();
			Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.ANALYZER, analyzer);
			siteDAO.loadAndSaveSynchronized(siteBeanValues);
		}
	}
	
	/**
	 * Initializes the lucene parameters from the the database 
	 * (table TSite, field Preferences)  
	 * @param site
	 */
	public static void configLuceneParameters(TSiteBean site) {
		//get useLucene 
		String useLuceneStr = site.getUseLucene();
		if (useLuceneStr == null || "".equals(useLuceneStr.trim())) {
			useLuceneStr = "false";
		}
		boolean useLucene = Boolean.valueOf(useLuceneStr).booleanValue();
		LuceneUtil.setUseLucene(useLucene);
		//get indexAttachments
		String indexAttachmentsStr = site.getIndexAttachments();
		if (indexAttachmentsStr == null || "".equals(indexAttachmentsStr.trim())) {
			indexAttachmentsStr = "false";
		}
		boolean indexAttachments = Boolean.valueOf(indexAttachmentsStr).booleanValue();
		LuceneUtil.setIndexAttachments(indexAttachments);
		//get reindexOnStartup
		String reindexOnStartupStr = site.getReindexOnStartup();
		if (reindexOnStartupStr == null || "".equals(reindexOnStartupStr.trim())) {
			reindexOnStartupStr = "false";
		}
		boolean reindexOnStartup = Boolean.valueOf(reindexOnStartupStr).booleanValue();
		LuceneUtil.setReindexOnStartup(reindexOnStartup);
		//get analyzerStr
		String analyzerStr = site.getAnalyzer();
		if (analyzerStr == null || "".equals(analyzerStr.trim())) {
			analyzerStr = StandardAnalyzer.class.getName();
		}
		//get the analyzer class
		Class analyzerClass = null;
		try {
			analyzerClass = Class.forName(analyzerStr.trim());
		} catch (ClassNotFoundException e) {
			LOGGER.error("Analyzer class not found. Fall back to StandardAnalyzer." + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (analyzerClass!=null) {
			Class partypes[] = new Class[0];
			Constructor ct = null;
			try {
				ct = analyzerClass.getConstructor(partypes);
			} catch (Exception e) {
				LOGGER.info("Getting the Version based constructor for " + analyzerStr.trim() + " failed with " + e);
			}
			if (ct!=null) {
				//Object arglist[] = new Object[1];
				//arglist[0] = LuceneUtil.VERSION;
				try {
					analyzer = (Analyzer)ct.newInstance(/*arglist*/);
				} catch (Exception e) {
					LOGGER.error("Instanciationg the Version based constructor for " + analyzerStr.trim() + " failed with " + e);
				}
			}
			//then try it with implicit constructor (for earlier lucene versions)
			if (analyzer==null) {
				try {
					analyzer = (Analyzer)analyzerClass.newInstance();
					LOGGER.info("Instantiating the Analyzer through default constructor");
				} catch (Exception e) {
					LOGGER.error("Instantiating the Analyzer through default constructor failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		if (analyzer==null) {
			LOGGER.warn("Fall back on creating a StandardAnalyzer instance");
			analyzer = new StandardAnalyzer();
		}		
		LuceneUtil.setAnalyzer(analyzer);
		//get indexPath
		String indexPath = site.getIndexPath();
		if (indexPath == null) {
			indexPath = "";
		}
		LuceneUtil.setPath(indexPath);
	}

	/**
	 * Typically the value itself converted to string
	 * But there are some exceptions where the toString()
	 * doesn't work as expected. 
	 * It should be implemented
	 * specific to the lucene requirement to be indexable
	 * set the date according to offset from the GMT
	 * see http://www.gossamer-threads.com/lists/lucene/java-user/39303?search_string=DateTools;#39303
	 * @param value
	 * @return
	 */
	public static String getLuceneDateValue(Object value) {
		Calendar cal = new GregorianCalendar();
		int minutesOffset = (cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET)) / (60 * 1000);
		if (value!=null) {
			Date dateValue = null;
			try {
				dateValue = (Date)value;
			} catch (Exception e) {
				LOGGER.error("The type of the lucene value is " + value.getClass().getName() + 
						". Casting it to Date failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (dateValue!=null) {
				cal.setTime(dateValue);
				cal.add(Calendar.MINUTE, minutesOffset);
				return DateTools.dateToString(cal.getTime(), DateTools.Resolution.DAY);
			}
		}
		return null;
	}

	/**
	 * Whether to add the highlighted field for this item type
	 * @param itemTypeID
	 * @return
	 */
	public static boolean isHighlightedItemType(Integer itemTypeID) {
		boolean withHighlighter = false;
		if (itemTypeID!=null) {
			TListTypeBean itemTypeBean = LookupContainer.getItemTypeBean(itemTypeID);
			if (itemTypeBean!=null) {
				Integer typeFlag = itemTypeBean.getTypeflag();
				if (typeFlag!=null && (typeFlag.equals(TListTypeBean.TYPEFLAGS.DOCUMENT) || typeFlag.equals(TListTypeBean.TYPEFLAGS.DOCUMENT_SECTION))) {
					withHighlighter = true;
				}
			}
		}
		return withHighlighter;
	}
	
	
}
