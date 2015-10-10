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

package com.aurel.track.lucene.index;

import java.io.IOException;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;

import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.associatedFields.AttachmentIndexer;
import com.aurel.track.lucene.index.associatedFields.BudgetPlanIndexer;
import com.aurel.track.lucene.index.associatedFields.ExpenseIndexer;
import com.aurel.track.lucene.index.associatedFields.IAssociatedFieldsIndexer;
import com.aurel.track.lucene.index.associatedFields.LinkIndexer;
import com.aurel.track.lucene.index.listFields.ExternalListIndexer;
import com.aurel.track.lucene.index.listFields.LocalizedListIndexer;
import com.aurel.track.lucene.index.listFields.NotLocalizedListIndexer;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.emailHandling.Html2Text;

/**
 * Utility methods for lucene indexing
 * Reindexing runs in his own thread  
 * @author Tamas Ruff
 *
 */
public class LuceneIndexer implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(LuceneIndexer.class);
	/**
	 * DAO classes 
	 */
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
	private static boolean halt;
	private boolean finished = true;
	
	/**
	 * the writers for each index 
	 */
	private static IndexWriter workItemWriter;
	private static IndexWriter notLocalizedLookupWriter;
	private static IndexWriter localizedLookupWriter;
	private static IndexWriter externalLookupWriter;
	private static IndexWriter attachmentWriter;
	private static IndexWriter expenseWriter;
	private static IndexWriter budgetPlanWriter;
	private static IndexWriter linkWriter;
	
	
	public static void halt() {
		halt = true;
	}
	
	/**
	 * @return Returns the halt.
	 */
	public static boolean isHalt() {
		return halt;
	}

	/**
	 * @param haltFlag The halt to set.
	 */
	public static void setHalt(boolean haltFlag) {
		halt = haltFlag;
	}
	
	public boolean isFinished() {
		return finished;
	}

	/**
	 * The run method of the thread
	 */
	public void run() {
		finished = false;
		reIndex();
		finished = true;
	}

	private static IndexWriter getWorkItemIndexWriter() {
		return workItemWriter;
	}
	
	public static IndexWriter getIndexWriter(int index) {
		IndexWriter indexWriter = null;
		switch (index) {
		case LuceneUtil.INDEXES.WORKITEM_INDEX:
			indexWriter = workItemWriter;
			break;
		case LuceneUtil.INDEXES.NOT_LOCALIZED_LIST_INDEX:
			indexWriter = notLocalizedLookupWriter;
			break;
		case LuceneUtil.INDEXES.LOCALIZED_LIST_INDEX:
			indexWriter = localizedLookupWriter;
			break;
		case LuceneUtil.INDEXES.EXTERNAL_LOOKUP_WRITER:
			indexWriter = externalLookupWriter;
			break;
		case LuceneUtil.INDEXES.ATTACHMENT_INDEX:
			indexWriter = attachmentWriter;
			break;
		case LuceneUtil.INDEXES.EXPENSE_INDEX:
			indexWriter = expenseWriter;
			break;
		case LuceneUtil.INDEXES.BUDGET_PLAN_INDEX:
			indexWriter = budgetPlanWriter;
			break;
		case LuceneUtil.INDEXES.LINK_INDEX:
			indexWriter = linkWriter;
			break;
		}
		return indexWriter;
	}
	
	/*************************************writer initializers*************************************/
	/**
	 * Initializes an IndexWriter. 
	 * It will be called from the following places:
	 * 	-	on system startup workItemWriter should be initialized! 
	 * 						created = false if no reindex at startup
	 * 						created = true if reindex at startup
	 * 	-	before explicit reIndexing: created = true
	 * 	-	after reindexing: create = false;
	 * 	During the adding/editing/deleting of index data the IndexWriter should be initialized with created = false!
	 * @param created
	 * @param index
	 */
	public static IndexWriter initWriter(boolean created, int index) {
		Directory indexDirectory = LuceneUtil.getIndexDirectory(index);
		if (indexDirectory == null) {
			LOGGER.error("Can not find or create the index directory for workitems");
			return null;
		}
		Analyzer analyzer = LuceneUtil.getAnalyzer();
		if (analyzer == null) {
			LOGGER.error("Analyzer is null");
			return null;
		}
		IndexWriter indexWriter = getIndexWriter(index);
		if (indexWriter!=null) {
			try {
				//release the lock
				indexWriter.close();
			} catch (IOException e) {
				LOGGER.error("Closing the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
			}
		}
		try {
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
			OpenMode openMode = null;
			if (created) {
				openMode = OpenMode.CREATE;
			} else {
				openMode = OpenMode.APPEND;
			}
			indexWriterConfig.setOpenMode(openMode);
			indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
		} catch (OverlappingFileLockException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("OverlappingFileLockException " + e);
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
			if (!created) {
				//try again this time with created = true
				try {
					//open for create
					IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
					indexWriterConfig.setOpenMode(OpenMode.CREATE);
					//indexWriter = new IndexWriter(indexDirectory, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
					indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
					//close it in order to reopen it for modifications
					indexWriter.close();
				} catch (IOException e1) {
					LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
				}
				//try again this time with created = false
				try {
					IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
					indexWriterConfig.setOpenMode(OpenMode.APPEND);
					//indexWriter = new IndexWriter(indexDirectory, analyzer, false, IndexWriter.MaxFieldLength.UNLIMITED);
					indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
				} catch (IOException e1) {
					LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
				}
			} else {
				LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
			}
		} catch (IOException e) {
			//tried probably with created = false, when the index structure doesn't exist yet
			//it is the case when by startup the useLucene is active but reindexOnStartup not
			//we should try to open the writers with false (for modifications) in order to not to destroy the possible existing index
			//but when the index doesn't exists yet the opening of the writer with false fails. And this is the case now.
			if (!created) {
				//try again this time with created = true
				try {
					//open for create
					IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
					indexWriterConfig.setOpenMode(OpenMode.CREATE);
					//indexWriter = new IndexWriter(indexDirectory, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
					indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
					//close it in order to reopen it for modifications
					indexWriter.close();
				} catch (IOException e1) {
					LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
				}
				//try again this time with created = false
				try {
					IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
					indexWriterConfig.setOpenMode(OpenMode.APPEND);
					//indexWriter = new IndexWriter(indexDirectory, analyzer, false, IndexWriter.MaxFieldLength.UNLIMITED);
					indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
				} catch (IOException e1) {
					LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
				}
			} else {
				LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
			}
		}
		switch (index) {
		case LuceneUtil.INDEXES.WORKITEM_INDEX:
			workItemWriter = indexWriter;
			break;
		case LuceneUtil.INDEXES.NOT_LOCALIZED_LIST_INDEX:
			notLocalizedLookupWriter = indexWriter;
			break;
		case LuceneUtil.INDEXES.LOCALIZED_LIST_INDEX:
			localizedLookupWriter = indexWriter;
			break;
		case LuceneUtil.INDEXES.EXTERNAL_LOOKUP_WRITER:
			externalLookupWriter = indexWriter;
			break;
		case LuceneUtil.INDEXES.ATTACHMENT_INDEX:
			attachmentWriter = indexWriter;
			break;
		case LuceneUtil.INDEXES.EXPENSE_INDEX:
			expenseWriter = indexWriter;
			break;
		case LuceneUtil.INDEXES.BUDGET_PLAN_INDEX:
			budgetPlanWriter = indexWriter;
			break;
		case LuceneUtil.INDEXES.LINK_INDEX:
			linkWriter = indexWriter;
			break;
		default:
			return null;
		}
		return indexWriter;
	}
	
	private static IndexWriter initWorkItemIndexWriter(boolean created) {
		return initWriter(created, LuceneUtil.INDEXES.WORKITEM_INDEX);
	}
	
	/**
	 * Initialize all the IndexWriters (by starting the application) for modifications
	 * @param created
	 */
	public static void initIndexWriters(boolean created) {
		initWriter(created, LuceneUtil.INDEXES.WORKITEM_INDEX);
		initWriter(created, LuceneUtil.INDEXES.NOT_LOCALIZED_LIST_INDEX);
		initWriter(created, LuceneUtil.INDEXES.LOCALIZED_LIST_INDEX);
		initWriter(created, LuceneUtil.INDEXES.EXTERNAL_LOOKUP_WRITER);
		initWriter(created, LuceneUtil.INDEXES.ATTACHMENT_INDEX);
		initWriter(created, LuceneUtil.INDEXES.EXPENSE_INDEX);
		initWriter(created, LuceneUtil.INDEXES.BUDGET_PLAN_INDEX);
		initWriter(created, LuceneUtil.INDEXES.LINK_INDEX);
	}
	
	/**
	 * All IndexWriters should be closed by system shutdown in order to release the locks 
	 * @return
	 */
	public static void closeWriters() {
		if (workItemWriter!=null) {
			try {
				//release the lock
				workItemWriter.close();
			} catch (IOException e) {
				LOGGER.error("Closing the IndexWriter for workitems failed with " + e.getMessage(), e);
			}
		}
		if (notLocalizedLookupWriter!=null) {
			try {
				//release the lock
				notLocalizedLookupWriter.close();
			} catch (IOException e) {
				LOGGER.error("Closing the IndexWriter for not localized list fields failed with " + e.getMessage(), e);
			}
		}
		if (localizedLookupWriter!=null) {
			try {
				//release the lock
				localizedLookupWriter.close();
			} catch (IOException e) {
				LOGGER.error("Closing the IndexWriter for not localized list fields failed with " + e.getMessage(), e);
			}
		}
		if (externalLookupWriter!=null) {
			try {
				//release the lock
				externalLookupWriter.close();
			} catch (IOException e) {
				LOGGER.error("Closing the IndexWriter for external lists fields failed with " + e.getMessage(), e);
			}
		}
		if (attachmentWriter!=null) {
			try {
				//release the lock
				attachmentWriter.close();
			} catch (IOException e) {
				LOGGER.error("Closing the IndexWriter for attachments failed with " + e.getMessage(), e);
			}
		}
		if (expenseWriter!=null) {
			try {
				//release the lock
				expenseWriter.close();
			} catch (IOException e) {
				LOGGER.error("Closing the IndexWriter for expense failed with " + e.getMessage(), e);
			}
		}
		if (budgetPlanWriter!=null) {
			try {
				//release the lock
				budgetPlanWriter.close();
			} catch (IOException e) {
				LOGGER.error("Closing the IndexWriter for budget/plan failed with " + e.getMessage(), e);
			}
		}
		if (linkWriter!=null) {
			try {
				//release the lock
				linkWriter.close();
			} catch (IOException e) {
				LOGGER.error("Closing the IndexWriter for link failed with " + e.getMessage(), e);
			}
		}
	}
	/**
	 * Initializes an IndexWriter. 
	 * It will be called from the following places:
	 * 	-	on system startup workItemWriter should be initialized! 
	 * 						created = false if no reindex at startup
	 * 						created = true if reindex at startup
	 * 	-	before explicit reIndexing: created = true
	 * 	-	after reindexing: create = false;
	 * 	During the adding/editing/deleting of index data the IndexWriter should be initialized with created = false!
	 * @param create
	 * @param index
	 */
	/*public static IndexWriter initWriter(boolean create, int index) {
		Directory indexDirectory = LuceneUtil.getIndexDirectory(index);
		if (indexDirectory == null) {
			LOGGER.error("Can not find or create the index directory for workitems");
			return null;
		}
		Analyzer analyzer = LuceneUtil.getAnalyzer(); 
		if (analyzer==null) {
			LOGGER.error("Analyzer is null");
			return null;
		}
		IndexWriter indexWriter = null;
		
		try {
			OpenMode openMode = null;
			if (create) {
				openMode = OpenMode.CREATE;
			} else {
				openMode = OpenMode.APPEND;
			}
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(LuceneUtil.VERSION, analyzer);
			indexWriterConfig.setOpenMode(openMode);
			indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
		} catch (OverlappingFileLockException e) {
			//file still locked
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("OverlappingFileLockException " + e);
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
			if (!create) {
				//try again this time with created = true
				try {
					//open for create
					IndexWriterConfig indexWriterConfig = new IndexWriterConfig(LuceneUtil.VERSION, analyzer);
					indexWriterConfig.setOpenMode(OpenMode.CREATE);
					indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
					//close it in order to reopen it for modifications
					indexWriter.close();
				} catch (IOException e1) {
					LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
				}
				//try again this time with created = false
				try {
					IndexWriterConfig indexWriterConfig = new IndexWriterConfig(LuceneUtil.VERSION, analyzer);
					indexWriterConfig.setOpenMode(OpenMode.APPEND);
					indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
				} catch (IOException e1) {
					LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
				}
			} else {
				LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
			}
		} catch (IOException e) {
			//index not exist
			//tried probably with created = false, when the index structure doesn't exist yet
			//it is the case when by startup the useLucene is active but reindexOnStartup not
			//we should try to open the writers with false (for modifications) in order to not to destroy the possible existing index
			//but when the index doesn't exists yet the opening of the writer with false fails. And this is the case now.
			if (!create) {
				//try again this time with created = true
				try {
					//open for create
					IndexWriterConfig indexWriterConfig = new IndexWriterConfig(LuceneUtil.VERSION, analyzer);
					indexWriterConfig.setOpenMode(OpenMode.CREATE);
					indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
					//close it in order to reopen it for modifications
					closeWriter(indexWriter);
				} catch (IOException e1) {
					LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
				}
				//try again this time with created = false
				try {
					IndexWriterConfig indexWriterConfig = new IndexWriterConfig(LuceneUtil.VERSION, analyzer);
					indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
					indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
				} catch (IOException e1) {
					LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
				}
			} else {
				LOGGER.error("Creating the IndexWriter for index " + index + " failed with " + e.getMessage(), e);
			}
		}
		return indexWriter;
	}*/
	
	
	
	/**
	 * Initializes the expense writer.  
	 *	-	reIndexing from scratch: created = true
	 *	-	updating the index: create = false;
	 * @param created
	 */
	
	
	/*public static void closeWriter(IndexWriter indexWriter) {
		if (indexWriter!=null) {
			try {
				indexWriter.close();
			} catch (CorruptIndexException e) {
				LOGGER.error("Closing the index writer failed with CorruptIndexException " + e.getMessage(), e);
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			} catch (IOException e) {
				LOGGER.error("Closing the index writer failed with IOException " + e.getMessage(), e);
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}*/
	
	/**
	 * Gets the associated field indexers
	 * @return
	 */
	public static List<IAssociatedFieldsIndexer> getAssociatedFieldIndexers() {
		List<IAssociatedFieldsIndexer> associatedFieldIndexers = new LinkedList<IAssociatedFieldsIndexer>();
		associatedFieldIndexers.add(AttachmentIndexer.getInstance());
		associatedFieldIndexers.add(LinkIndexer.getInstance());
		associatedFieldIndexers.add(ExpenseIndexer.getInstance());
		associatedFieldIndexers.add(BudgetPlanIndexer.getInstance());
		return associatedFieldIndexers;
	}

	/**
	 * Gets the list indexers
	 * @return
	 */
	public static List<ILookupFieldIndexer> getListFieldIndexers() {
		List<ILookupFieldIndexer> listIndexers = new LinkedList<ILookupFieldIndexer>();
		listIndexers.add(NotLocalizedListIndexer.getInstance());
		listIndexers.add(LocalizedListIndexer.getInstance());
		listIndexers.add(ExternalListIndexer.getInstance());
		return listIndexers;
	}

	/**
	 * Reindex everything
	 *
	 */
	public void reIndex() {
		LOGGER.info("Reindexing with Lucene started...");
		long start = System.currentTimeMillis();
		reIndexWorkItems();
		List<ILookupFieldIndexer> workItemListIndexers = getListFieldIndexers();
		for (ILookupFieldIndexer workItemListIndexer : workItemListIndexers) {
			workItemListIndexer.reIndexAll();
		}
		List<IAssociatedFieldsIndexer> associatedFieldIndexers = getAssociatedFieldIndexers();
		for (IAssociatedFieldsIndexer associatedFieldsIndexer : associatedFieldIndexers) {
			associatedFieldsIndexer.reIndexAll();
		}
		long end = System.currentTimeMillis();
		LOGGER.info("Reindexing with Lucene completed in " +
				((end - start) / DateTimeUtils.SECOND) + " seconds.");
	}

	/*************************************reindex methods*************************************/
	
	/** 
	 * Reindexes all workitems. 
	 * The custom attributes and the history beans should be set for each workItemBean
	 */
	public synchronized void reIndexWorkItems() {
		IndexWriter indexWriter = null;
		try {
			LOGGER.info("Reindexing workitems started... ");
			//initializes the IndexWriter for recreating the index (deletes the previous index)
			indexWriter = initWorkItemIndexWriter(true);
			int chunkInterval = GeneralUtils.ITEMS_PRO_STATEMENT;
			Integer maxWorkItemID = workItemDAO.getMaxObjectID();
			int noOfWorkItems = 0;
			if (maxWorkItemID!=null && maxWorkItemID.intValue()>0){
				int fromID = 0;
				while (fromID<maxWorkItemID) {
					List<TWorkItemBean> workItemChunk = workItemDAO.getNextChunk(fromID, chunkInterval);
					if (workItemChunk!=null) {
						LOGGER.info("Reindexing workitems between " + fromID + " and "  + (fromID + chunkInterval) + 
								", a total number of " + workItemChunk.size() + " workitems.");
						noOfWorkItems += workItemChunk.size();
						addToWorkItemIndex(indexWriter, workItemChunk);
					}
					fromID += chunkInterval;
				}
			}
			LOGGER.info("Reindexing " + noOfWorkItems + " workitems completed.");
		} catch (Exception e) {
			LOGGER.error("Reindexing issues failed with " + e.getMessage(), e);
		} finally {
			initWorkItemIndexWriter(false);
		}
	}
	
	private static String getComments(StringBuffer workItemOldComments, String newComment) {
		if (workItemOldComments==null) {
			workItemOldComments = new StringBuffer();
		}
		if (newComment!=null) {
			workItemOldComments.append(newComment);
		}
		return workItemOldComments.toString();
	}
	
	
	/*************************************workitem index writer methods***********************************/
	
	/**
	* Only the system attributes are set for the workItems
	* set the custom attributes and the comments 
	* to prepare them for indexing
	*/
	private static List<TWorkItemBean> prepareWorkItemBeans(List<TWorkItemBean> workItemBeans) {
		List<Integer> workItemIDs = GeneralUtils.createIntegerListFromBeanList(workItemBeans);
		int[] arrWorkItemIDs = GeneralUtils.createIntArrFromIntegerList(workItemIDs);
		//get the comments for all workItems
		Map<Integer, StringBuffer> workItemsCommentsMap = 
			HistoryLoaderBL.getByWorkItemsLongTextField(workItemIDs, 
					SystemFields.INTEGER_COMMENT, HistoryLoaderBL.LONG_TEXT_TYPE.ISPLAIN);
		//get attribute values for all workItems
		List<TAttributeValueBean> allAttributeValueBeansList = AttributeValueBL.loadByWorkItemKeys(arrWorkItemIDs);
		Map<Integer, Map<String, Object>> attributeValueBeansMap = AttributeValueBL.prepareAttributeValueMapForWorkItems(allAttributeValueBeansList, null, null);
		//gather the fields in a set to avoid calling processLoad for each attributeValueBean 
		//(each part of a composite or each value of a multiple select)
		Map<Integer, Set<Integer>> fieldSetMap = AttributeValueBL.getFieldIDsSetForWorkItems(allAttributeValueBeansList);
		if (workItemBeans!=null) {
			for (TWorkItemBean workItemBean : workItemBeans) {
				Integer workItemID = workItemBean.getObjectID();
				//load the custom attributes
				Map<String, Object> attributeValueBeansMapForWorkItem = attributeValueBeansMap.get(workItemID);
				if (attributeValueBeansMapForWorkItem==null) {
					attributeValueBeansMapForWorkItem = new HashMap<String, Object>();
				}
				Set<Integer> fieldIDSetForWorkItem = fieldSetMap.get(workItemID);
				AttributeValueBL.loadWorkItemCustomAttributes(workItemBean, attributeValueBeansMapForWorkItem, fieldIDSetForWorkItem);
				//load the comments
				workItemBean.setComment(getComments(workItemsCommentsMap.get(workItemBean.getObjectID()), workItemBean.getComment()));
			}
		}
		return workItemBeans;
	}
	
	/**
	 * Adds a list of TWorkItems to the workItem index 
	 * Used by recreating the index
	 * @param indexWriter
	 * @param workItemBeans the beans are considered to be fully prepared: 
	 * 			i.e. history beans and custom attributes are set
	 */
	private static void addToWorkItemIndex(IndexWriter indexWriter, List<TWorkItemBean> workItemBeans) {
		//use lucene uncheched in site config
		if (!LuceneUtil.isUseLucene()) {
			return;
		}
		if (indexWriter == null) {
			LOGGER.error("IndexWriter null by adding a list if workItems");
			return;
		}
		prepareWorkItemBeans(workItemBeans);
		if (workItemBeans!=null && !workItemBeans.isEmpty()) {
			for (TWorkItemBean workItemBean : workItemBeans) {
				//use lucene uncheched in site config
				if (!LuceneUtil.isUseLucene()) {
					return;
				}
				//addToWorkItemIndex(TWorkItemBean workItemBean) could be used, 
				//but we do not use it because of the superfluous flushes
				//we make flush and optimize only after each workItem is added
				Document document = createWorkItemDocument(workItemBean);
				try {
					if (document!=null) {
						indexWriter.addDocument(document);
					}
				} catch (IOException e) {
					LOGGER.error("Adding a workItem document for workItemBean " + workItemBean.getObjectID() + " failed with " + e.getMessage(), e);				
				}
			}
			try {
				indexWriter.commit();
			} catch (IOException e) {
				LOGGER.error("Flushing the IndexWriter after adding all workItemBeanss failed with " + e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Adds a list of TWorkItems to the workItem index 
	 * Used by recreating the index
	 * @param workItemBeans
	 * @param fieldID the beans are considered to be fully prepared:
	 * 			i.e. history beans and custom attributes are set
	 */
	public static synchronized void addToWorkItemIndex(List<TWorkItemBean> workItemBeans, Integer fieldID) {
		//use lucene unchecked in site config
		if (!LuceneUtil.isUseLucene()) {
			return;
		}
		if (workItemBeans==null) {
			return;
		}
		if (!ClusterBL.indexInstantly()) {
			LOGGER.debug("Index instantly is false by adding workItemBeans " + workItemBeans.size() + " by field " + fieldID);
			return;
		}
		prepareWorkItemBeans(workItemBeans);
		if (workItemBeans!=null && !workItemBeans.isEmpty()) {
			IndexWriter indexWriter = getWorkItemIndexWriter();
			if (indexWriter == null) {
				LOGGER.error("IndexWriter null by adding a list if workItems");
				return;
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Add " + workItemBeans.size() +
					" previously deleted workItem documents back to workItemIndex after changing a dependency for field " + fieldID);
			}
			for (TWorkItemBean workItemBean : workItemBeans) {
				//use lucene uncheched in site config
				if (!LuceneUtil.isUseLucene()) {
					return;
				}
				//addToWorkItemIndex(TWorkItemBean workItemBean) could be used, 
				//but we do not use it because of the superfluous flushes
				//we make flush and optimize only after each workItem is added
				Document document = createWorkItemDocument(workItemBean);
				try {
					if (document!=null) {
						indexWriter.addDocument(document);
					}
				} catch (IOException e) {
					LOGGER.error("Adding a workItem document for workItemBean " + workItemBean.getObjectID() + " failed with IOException" + e.getMessage(), e);
				}
			}
			try {
				indexWriter.commit();
			} catch (IOException e) {
				LOGGER.error("Flushing the workItemBeans failed with " + e.getMessage(), e);
			}
			
		}
	}
	
	/**
	 * Updates the workItemBean index with a modified TWorkItemBean
	 * Deletes the old document and create a new one
	 * @param workItemBeanIDs
     * @param
	 */
	public static void updateWorkItemIndex(int[] workItemBeanIDs, Integer fieldID) {
		if (!LuceneUtil.isUseLucene()) {
			return;
		}
		if (workItemBeanIDs==null) {
			return;
		}
		if (!ClusterBL.indexInstantly()) {
			LOGGER.debug("Index instantly is false by updating workItemBeanIDs " + workItemBeanIDs.length + " by field " + fieldID);
			return;
		}
		List<TWorkItemBean> workItemBeans = ItemBL.loadByWorkItemKeys(workItemBeanIDs);
		if (LOGGER.isDebugEnabled() && workItemBeans!=null) {
			LOGGER.debug("Update " + workItemBeans.size() + " items in workItemIndex after changing a dependency for field " + fieldID);
		}
		deleteFromWorkItemIndex(workItemBeans, fieldID, false);
		addToWorkItemIndex(workItemBeans, fieldID);
	}
	
	/**
	 * Adds a TWorkItem to the workitem index
	 * Used by creating a new and by updating (delete old and create new) of an existing workItem
	 * Although workItemBean contains no history bean it is not needed 
	 * because by creating only an "emtpy" state change history is created. 
	 * However it contains the custom attribute values.  
	 * @param workItemBean
	 */
	public static synchronized void addToWorkItemIndex(TWorkItemBean workItemBean, boolean deleteExisting) {
		if (!LuceneUtil.isUseLucene()) {
			return;
		}
		if (!ClusterBL.indexInstantly()) {
			LOGGER.debug("Index instantly is false");
			return;
		}
		if (workItemBean==null || workItemBean.getObjectID()==null) {
			LOGGER.error("TWorkItemBean or ObjectID null by adding/updating a workitem");
			return;
		}
		IndexWriter indexWriter = getWorkItemIndexWriter();
		if (indexWriter==null) {
			LOGGER.error("IndexWriter null by adding a workitem");
			return;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Save a new " + !deleteExisting + " workItem into workItemIndex");
		}
		if (deleteExisting) {
			String workItemID = workItemBean.getObjectID().toString();
			Term term = new Term(LuceneUtil.getFieldName(SystemFields.ISSUENO), workItemID);
			try {
				indexWriter.deleteDocuments(term);
				indexWriter.commit();
			} catch (IOException e) {
				LOGGER.error("Removing a workItembean " + workItemID + " from the index failed with " + e.getMessage(), e);
			}
		}
		
		List<Integer> workItemIDsList = new ArrayList<Integer>();
		workItemIDsList.add(workItemBean.getObjectID());
		Map<Integer, StringBuffer> workItemsCommentsMap = 
			HistoryLoaderBL.getByWorkItemsLongTextField(workItemIDsList, 
					SystemFields.INTEGER_COMMENT, HistoryLoaderBL.LONG_TEXT_TYPE.ISPLAIN);
		String actualComment = workItemBean.getComment();
		String actualCommentPlain = "";
		try {
			actualCommentPlain = Html2Text.getNewInstance().convert(actualComment);
		} catch (Exception e) {
			LOGGER.warn("Transforming the actual comment to plain text failed with " + e.getMessage(), e);
			actualCommentPlain = workItemBean.getComment();
		}
		workItemBean.setComment(getComments(workItemsCommentsMap.get(workItemBean.getObjectID()), actualCommentPlain));
		Document doc = createWorkItemDocument(workItemBean);
		//set the original comment back because the workItem will be used by other parts (e-mail notification, etc.)
		workItemBean.setComment(actualComment);
		if (doc!=null) {
			try {
				indexWriter.addDocument(doc);
			} catch (IOException e) {
				LOGGER.error("Adding a workItemBean " + workItemBean.getObjectID() + " to the index failed with " + e.getMessage(), e);
			} 
		}
		try {
			indexWriter.commit();
		} catch (IOException e) {
			LOGGER.error("Flushing the workItemBean failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * Removes a list of TWorkItems from the workitem index
	 * Used by deleting a project (for deleting all of its workitems) 
	 * @param workItemBeans
	 * @return
	 */
	public static synchronized void deleteFromWorkItemIndex(List<TWorkItemBean> workItemBeans, Integer fieldID, boolean deleteProject) {
		String workItemID;
		if (!LuceneUtil.isUseLucene()) {
			return;
		}
		if (workItemBeans==null) {
			return;
		}
		if (!ClusterBL.indexInstantly()) {
			LOGGER.debug("Index instantly is false by deleting workItemBeans " + workItemBeans.size() + " by field " + fieldID + " and deleteProject " + deleteProject);
			return;
		}
		if (LOGGER.isDebugEnabled()) {
			if (deleteProject) {
				LOGGER.debug("Delete " + workItemBeans.size() + 
						" workItems from workItemIndex after deleting a project with all items " + fieldID);
			} else {
				LOGGER.debug("Delete " + workItemBeans.size() + 
					" workItems from workItemIndex after changing a dependency for field " + fieldID + " to add them again");
			}
		}
		IndexWriter indexWriter = getWorkItemIndexWriter();
		if (indexWriter == null) {
			LOGGER.error("IndexWriter null by deleting a list of workItems");
			return;
		}
		try {
			for (TWorkItemBean workItemBean : workItemBeans) {
				workItemID = workItemBean.getObjectID().toString();
				Term term = new Term(LuceneUtil.getFieldName(SystemFields.ISSUENO), workItemID);
				try {
					indexWriter.deleteDocuments(term);
				} catch (Exception e) {
					LOGGER.error("Removing a workItemBean " + workItemID + " from the index failed with " + e.getMessage(), e);
				}
			}
		} catch (Exception e) {
		}
		try {
			indexWriter.commit();
		} catch (IOException e) {
			LOGGER.error("Flushing the deleting of workItemBeans failed with " + e.getMessage(), e);
		}
	}
	
	/*************************************create document methods***********************************/
	
	/**
	 * Creates the Lucene searchable document for a workItem.
	 * The field names for lucene fields should be normalized (replace the spaces)
	 * The custom attributes and the comments of the workItem should be populated
	 * @param workItemBean 
	 */
	private static Document createWorkItemDocument(TWorkItemBean workItemBean) {
		String luceneFieldName;
		IFieldTypeRT fieldTypeRT;
		Integer fieldID;
		Integer parameterCode;
		Integer workItemID = workItemBean.getObjectID();
		//index only saved items
		if (workItemID==null) {
			return null;
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creating the workItem document by id " + workItemID);
		}
		Document document = new Document();
		boolean withHighlighter = LuceneUtil.isHighlightedItemType(workItemBean.getListTypeID());
		StringBuilder highligterText = new StringBuilder();
		try {
			//index all not null system fields
			for (int i = 0; i < SystemFields.getSystemFieldsArray().length; i++) {
				fieldID = Integer.valueOf(SystemFields.getSystemFieldsArray()[i]);
				TFieldBean fieldBean = FieldTypeManager.getInstance().getFieldBean(fieldID);
				if (fieldBean!=null) {
					luceneFieldName = fieldBean.getName();
					fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					//it might be that the workItemBean.getAttribute(fieldID) is null but the 
					//fieldTypeRT.getLuceneValue(workItemBean.getAttribute(fieldID)) is not null. 
					//For example by checkboxes: null means false/No
					if (fieldTypeRT!=null) {
						String fieldName = LuceneUtil.normalizeFieldName(luceneFieldName);
						String luceneValue = fieldTypeRT.getLuceneValue(workItemBean.getAttribute(fieldID), workItemBean);
						Field field = createField(fieldTypeRT, fieldName, luceneValue);
						if (field!=null) {
							document.add(field);
							if (withHighlighter && fieldTypeRT.isHighlightedField()) {
								highligterText.append(" ").append(luceneValue);
							}
						}
					}
				}
			}
			//index all not null custom fields
			//each single field value and each part of a composite field values
			//are saved in separate lucene fields 
			Map<String, Object> customAttributes = workItemBean.getCustomAttributeValues();
			if (customAttributes!=null && !customAttributes.isEmpty()) {
				Iterator<String> iterator = customAttributes.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					Object value = customAttributes.get(key);
					if (value!=null) {
						String[] splittedKey = MergeUtil.splitKey(key);
						fieldID = Integer.valueOf(splittedKey[0].substring(1));	//get rid of "f"
						parameterCode = null;
						if (splittedKey.length>1) {
							//composite custom field
							String parameterCodeStr = splittedKey[1];
							if (parameterCodeStr!=null && !"null".equals(parameterCodeStr)) {
								try {
									parameterCode = Integer.valueOf(parameterCodeStr);
								} catch (Exception e) {
									LOGGER.error("Converting the parameterCode " + parameterCodeStr + " to an integer failed with " + e.getMessage(), e);
								}
							}
						}
						fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						TFieldBean fieldBean = FieldTypeManager.getInstance().getFieldBean(fieldID);
						CustomCompositeBaseRT customCompositeFieldTypeRT = null;
						if (parameterCode==null) {
							//single custom field
							luceneFieldName = LuceneUtil.normalizeFieldName(fieldBean.getName());
						} else {
							//part of a composite custom field
							//the lucene field name is synthetized from the composite field and the parameter code 
							luceneFieldName = LuceneUtil.synthetizeCompositePartFieldName(
									LuceneUtil.normalizeFieldName(fieldBean.getName()), parameterCode);
							try {
								customCompositeFieldTypeRT = (CustomCompositeBaseRT)fieldTypeRT;
							} catch (Exception e) {
								LOGGER.error("The type of the fieldTypeRT is " + fieldTypeRT.getClass().getName() + 
										". Casting it to CustomCompositeFieldTypeRT failed with " + e.getMessage(), e);
							}
							if (customCompositeFieldTypeRT!=null) {
								fieldTypeRT = customCompositeFieldTypeRT.getCustomFieldType(parameterCode.intValue());
							}
						}
						String luceneValue = fieldTypeRT.getLuceneValue(value, workItemBean);
						Field field = createField(fieldTypeRT, luceneFieldName, luceneValue);
						if (field!=null) {
							if (withHighlighter && fieldTypeRT.isHighlightedField()) {
								highligterText.append(" ").append(luceneValue);
							}
							document.add(field);
						}
					}
				}
			}
			if (withHighlighter && highligterText.length()>0) {
				document.add(new TextField(LuceneUtil.HIGHLIGHTER_FIELD, highligterText.toString(), Field.Store.YES));
			}
			return document;
		} catch (Exception e) {
			LOGGER.error("Indexing the workItem " + workItemID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Creates a new field for document
	 * @param fieldTypeRT
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	private static Field createField(IFieldTypeRT fieldTypeRT, String fieldName, String fieldValue) {
		Field field = null;
		if (fieldTypeRT!=null && fieldValue!=null) {
			int tokenized = fieldTypeRT.getLuceneTokenized();
			int stored = fieldTypeRT.getLuceneStored();
			Store store = LuceneUtil.getLuceneStore(stored);
			if (tokenized==LuceneUtil.TOKENIZE.NO) {
				field = new StringField(fieldName, fieldValue, store);
			} else {
				field = new TextField(fieldName, fieldValue, store);
			}
		}
		return field;
	}
}
