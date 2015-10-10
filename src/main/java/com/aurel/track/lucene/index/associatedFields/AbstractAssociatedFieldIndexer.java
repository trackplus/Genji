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

package com.aurel.track.lucene.index.associatedFields;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;

/**
 * Base class for associated field indexers
 * @author Tamas Ruff
 *
 */
public abstract class AbstractAssociatedFieldIndexer implements IAssociatedFieldsIndexer {
	
	private static final Logger LOGGER = LogManager.getLogger(AbstractAssociatedFieldIndexer.class);
	
	/**
	 * Gets the index writer ID
	 * @return
	 */
	protected abstract int getIndexWriterID();
	/**
	 * Loads all indexable records for reindexing
	 * @return
	 */
	protected abstract List loadAllIndexable();
	/**
	 * Gets the the unique identifier fieldName
	 * used for deleting a document by ID
	 * @return
	 */
	protected abstract String getObjectIDFieldName();
	
	/**
	 * Gets the fieldName containing the workItemID field
	 * used for deleting documents by workItems
	 * @return
	 */
	protected abstract String getWorkItemFieldName();
	
	/**
	 * Gets the other field name containing the workitemID  field
	 * used for deleting documents by workItems. Makes sense only for links
	 * @return
	 */
	protected String getAdditionalFieldName() {
		return null;
	}
	
	/**
	 * Gets the associated field name for logging purposes  
	 * @return
	 */
	protected abstract String getLuceneFieldName();
	/**
	 * Creates a document form a record 
	 * @param entry
	 * @return
	 */
	protected abstract Document createDocument(Object entry);
	
	/**
	 * Reindexes all
	 */
	public synchronized void reIndexAll() {
		IndexWriter indexWriter = null;
		try {
			LOGGER.debug("Reindexing " + getLuceneFieldName() + "s started...");
			//initializes the IndexWriter for recreating the index (deletes the previous index)
			indexWriter = LuceneIndexer.initWriter(true, getIndexWriterID());
			if (indexWriter==null) {
				LOGGER.error("IndexWriter null by indexing");
				return;
			}
			List allIndexableEntries = loadAllIndexable();
			if (allIndexableEntries!=null) {
				for (Object object : allIndexableEntries) {
					Document doc = createDocument(object);
					try {
						if (doc!=null) {
							indexWriter.addDocument(doc);
						}
					} catch (IOException e) {
						LOGGER.error("Adding entry to the index failed with " + e.getMessage(), e);
					}
				}
				LOGGER.debug("Reindexing " + allIndexableEntries.size() +
						" " + getLuceneFieldName() +  "s completed.");
			}
		} catch (Exception e) {
			LOGGER.error("Reindexing failed with " + e.getMessage(), e);
		} finally {
			//LuceneIndexer.closeWriter(indexWriter);
			LuceneIndexer.initWriter(false, getIndexWriterID());
		}
	}
	
	/**
	 * Deletes a document by key
	 * @param objectID
	 */
	public void deleteByKey(Integer objectID) {
		if (!ClusterBL.indexInstantly()) {
			LOGGER.debug("Index instantly is false by deleting the " + getLuceneFieldName() + " with entryID " + objectID);
			return;
		}
		if (objectID!=null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Deleting the " + getLuceneFieldName() + " document by Term " + getObjectIDFieldName() + "=" + objectID.toString() +
						" from " + LuceneUtil.getDirectoryString(getIndexWriterID()));
			}
			IndexWriter indexWriter = LuceneIndexer.getIndexWriter(getIndexWriterID());
			if (indexWriter==null) {
				LOGGER.error("IndexWriter null by deleting by key");
				return;
			}
			Term keyTerm = new Term(getObjectIDFieldName(), objectID.toString());
			try {
				indexWriter.deleteDocuments(keyTerm);
			} catch (IOException e) {
				LOGGER.error("Deleting the " + getLuceneFieldName() + " with ID " + objectID + " failed with " + e.getMessage(), e);
			}
			try {
				indexWriter.commit();
			} catch (IOException e) {
				LOGGER.error("Flushing the indexWriter after removing a " + getLuceneFieldName() + " with ID " + objectID + "  failed with " + e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Removes the indexes for a list of workItems
	 * @param workItemIDs
	 */	
	public void deleteByWorkItems(List<Integer> workItemIDs) {
		IndexWriter indexWriter = null;
		try {
			if (!LuceneUtil.isUseLucene() || workItemIDs==null || workItemIDs.isEmpty()) {
				return;
			}
			indexWriter = LuceneIndexer.getIndexWriter(getIndexWriterID());
			if (indexWriter==null) {
				LOGGER.error("IndexWriter null in deleting by workItems");
				return;
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Deleting " + getLuceneFieldName() + " documents for " +
						workItemIDs.size() + " issues from " + LuceneUtil.getDirectoryString(getIndexWriterID()));
			}
			for (Integer workItemID : workItemIDs) {
				String workItemFieldName = getWorkItemFieldName();
				Term keyTerm = new Term(workItemFieldName, workItemID.toString());
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Deleting the " + getLuceneFieldName() + 
						" document by Term " + workItemFieldName + "=" + workItemID.toString());
				}
				try {
					indexWriter.deleteDocuments(keyTerm);
				} catch (IOException e) {
					LOGGER.error("Removing by workItemID " + workItemID + " failed with " + e.getMessage(), e);
				}
				String additionalWorkitemField = getAdditionalFieldName();
				if (additionalWorkitemField!=null) {
					keyTerm = new Term(additionalWorkitemField, workItemID.toString());
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Deleting the " + getLuceneFieldName() + 
							" document by Term " + additionalWorkitemField + "=" + workItemID.toString());
					}
					try {
						indexWriter.deleteDocuments(keyTerm);
					} catch (IOException e) {
						LOGGER.error("Removing by workItemID " + workItemID + " failed with " + e.getMessage(), e);
					}
				}
			}
		} catch(Exception ex) {
			LOGGER.error("deleteListFromIndex failed with " + ex.getMessage());
		}
		try {
			indexWriter.commit();
		} catch (IOException e) {
			LOGGER.error("Flushing the indexWriter after removing a list of workItemBeans  failed with " + e.getMessage(), e);
		}
	}
}
