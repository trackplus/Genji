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

package com.aurel.track.lucene.index.listFields;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.beans.TListBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.custom.select.CustomSelectSimpleRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.ILookupFieldIndexer;
import com.aurel.track.lucene.index.LuceneIndexer;

/**
 * Base class for all list indexers
 * @author Tamas Ruff
 *
 */
public abstract class AbstractListFieldIndexer implements ILookupFieldIndexer {
	
	private static final Logger LOGGER = LogManager.getLogger(AbstractListFieldIndexer.class);
	
	/**
	 * Gets the index writer ID
	 * @return
	 */
	protected abstract int getIndexWriterID();
	
	/**
	 * Gets the fieldIDs stored in this index 
	 * @return
	 */
	protected abstract List<Integer> getFieldIDs();
	
	/**
	 * Gets the custom listIDs stored in this index 
	 * @return
	 */
	protected List<TListBean> getCustomListBeans() {
		return null;
	}
	
	/**
	 * Creates a document 
	 * @param entry
	 * @param fieldTypeRT
	 * @param fieldID
	 * @return
	 */
	protected abstract Document createDocument(Object entry, IFieldTypeRT fieldTypeRT, Integer fieldID);
	/**
	 * Loads all indexable records for reindex
	 * @return
	 */
	protected abstract List loadAllIndexable(IFieldTypeRT fieldTypeRT, Integer fieldID);
	/**
	 * Gets the field name of the combined key which is the unique identifier
	 * used for deleting a document
	 * @return
	 */
	protected abstract String getCombinedKeyFieldName();
	/**
	 * Gets the list field type for logging purposes  
	 * @return
	 */
	protected abstract String getListFieldType();
	/**
	 * Reindexes all
	 */
	@Override
	public synchronized void reIndexAll() {
		List<Integer> fieldIDs = getFieldIDs();
		IndexWriter indexWriter = null;
		if (fieldIDs!=null && !fieldIDs.isEmpty()) {
			try {
				indexWriter = LuceneIndexer.initWriter(true, getIndexWriterID());
				for (Integer fieldID : fieldIDs) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					addToListFieldToIndex(indexWriter, fieldTypeRT, fieldID);
				}
			} catch (Exception e) {
				LOGGER.error("Reindexing system lists failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			} finally {
				LuceneIndexer.initWriter(false, getIndexWriterID());
			}
		}
		List<TListBean> customListBeans = getCustomListBeans();
		if (customListBeans!=null && !customListBeans.isEmpty()) {
			indexWriter = LuceneIndexer.initWriter(false, getIndexWriterID());
			try {
				for (TListBean listBean : customListBeans) {
					addCustomListToIndex(indexWriter, listBean);
				}
			} catch (Exception e) {
				LOGGER.error("Reindexing custom lists failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			} finally {
				LuceneIndexer.initWriter(false, getIndexWriterID());
			}
			
		}
	}
	
	/**
	 * Adds the list of ILabelBeans to the non-localized lookup index 
	 * @param indexWriter
	 * @param fieldID
	 */
	protected void addToListFieldToIndex(IndexWriter indexWriter,
			IFieldTypeRT fieldTypeRT, Integer fieldID) {
		if (indexWriter==null) {
			LOGGER.error("IndexWriter null by adding a list options for fieldID " + fieldID);
			return;
		}
		LOGGER.debug("Reindexing " + getListFieldType() + " list field " + fieldID + " started...");
		List allExternalLookups = loadAllIndexable(fieldTypeRT, fieldID);
		if (allExternalLookups!=null && !allExternalLookups.isEmpty()) {
			for (Object externalObject : allExternalLookups) {
				Document document = createDocument(externalObject, fieldTypeRT, fieldID);
				if (document!=null) {
					try {
						indexWriter.addDocument(document);
					} catch (IOException e) {
						LOGGER.error("Adding a list option document for field " +
							fieldID + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
			LOGGER.debug("Reindexing " + allExternalLookups.size() + " options for " + getListFieldType() + " list field " +  fieldID + " completed");
		}
	}
	
	/**
	 * Adds the list of ILabelBeans to the non-localized lookup index 
	 * @param indexWriter
	 * @param fieldID
	 */
	protected void addCustomListToIndex(IndexWriter indexWriter, TListBean listBean) {
		Integer listID = listBean.getObjectID();
		String listLabel = listBean.getLabel();
		if (indexWriter == null) {
			LOGGER.error("IndexWriter null by adding a list options for custom list " + listLabel + " ID " + listID);
			return;
		}
		StringBuilder stringBuilder = new StringBuilder();
		if (listBean.getProject()!=null) {
			stringBuilder.append(" project " + listBean.getProject());
		}
		if (listBean.getParentList()!=null) {
			stringBuilder.append(" parent list " + listBean.getParentList());
		}
		LOGGER.debug("Reindexing " + getListFieldType() + " custom list " + listLabel + " with ID " + listID + stringBuilder.toString() + " started...");
		List allExternalLookups = OptionBL.loadByListID(listID);
		if (allExternalLookups!=null && !allExternalLookups.isEmpty()) {
			for (Object externalObject : allExternalLookups) {
				Document document = createDocument(externalObject, new CustomSelectSimpleRT(), null);
				if (document!=null) {
					try {
						indexWriter.addDocument(document);
					} catch (IOException e) {
						LOGGER.error("Adding a list option document for custom list " +
								listLabel + " with ID " + listID + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
			LOGGER.debug("Reindexing " + allExternalLookups.size() + " options for " + getListFieldType() +
					" custom list " + listLabel + " ID " + listID + " completed");
		}
	}
	/**
	 * Deletes a document by key
	 * @param objectID
	 */
	public synchronized void deleteByKeyAndType(Integer objectID, int type) {
		if (!ClusterBL.indexInstantly()) {
			LOGGER.debug("Index instantly is false by deleting the entry " + objectID + " from list " + type);
			return;
		}
		if (objectID!=null) {
			IndexWriter indexWriter = LuceneIndexer.getIndexWriter(getIndexWriterID());
			if (indexWriter == null) {
				LOGGER.error("IndexWriter null by deleting a list option by key");
				return;
			}
			String combinedKeyFieldName = getCombinedKeyFieldName();
			String combinedKeyFieldValue = LuceneUtil.getCombinedKeyValue(objectID.toString(), String.valueOf(type));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Deleting the " + getListFieldType() + " document by Term " + combinedKeyFieldName + "=" + combinedKeyFieldValue +
						" from " + LuceneUtil.getDirectoryString(getIndexWriterID()));
			}
			Term keyTerm = new Term(combinedKeyFieldName, combinedKeyFieldValue);
			try {
				indexWriter.deleteDocuments(keyTerm);
			} catch (IOException e) {
				LOGGER.error("Removing the list option " + objectID +
						" type " + type + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			try {
				indexWriter.commit();
			} catch (IOException e) {
				LOGGER.error("Flushing list option removal for list option " + objectID +
						" type " + type + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}
}
