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

package com.aurel.track.lucene.index.associatedFields;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import com.aurel.track.beans.TCostBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.item.budgetCost.ExpenseBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;

/**
 * Index the expense subject and descriptions
 * @author Tamas Ruff
 *
 */
public class ExpenseIndexer extends AbstractAssociatedFieldIndexer {
	
	private static final Logger LOGGER = LogManager.getLogger(ExpenseIndexer.class);
	private static ExpenseIndexer instance;
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static ExpenseIndexer getInstance(){
		if(instance==null){
			instance=new ExpenseIndexer();
		}
		return instance;
	}
	/**
	 * Gets the index writer ID
	 * @return
	 */
	@Override
	protected int getIndexWriterID() {
		return LuceneUtil.INDEXES.EXPENSE_INDEX;
	}
	/**
	 * Loads all indexable entitities for reindex
	 * @return
	 */
	@Override
	protected List loadAllIndexable() {
		return ExpenseBL.loadAllIndexable();
	}
	/**
	 * Gets the the unique identifier fieldName
	 * @return
	 */
	@Override
	protected String getObjectIDFieldName() {
		return LuceneUtil.EXPENSE_INDEX_FIELDS.EXPENSEID;
	}
	/**
	 * Gets the fieldName containing the workItemID field
	 * @return
	 */
	@Override
	protected String getWorkItemFieldName() {
		return LuceneUtil.EXPENSE_INDEX_FIELDS.WORKITEMID;
	}
	/**
	 * Gets the associated field name for logging purposes  
	 * @return
	 */
	@Override
	protected String getLuceneFieldName() {
		return LuceneUtil.EXPENSE;
	}
	/**
	 * Adds an attachment to the attachment index
	 * Used by attaching a new file to the workItem 
	 * @param attachFile
	 */
	@Override
	public void addToIndex(Object object, boolean add) {
		if (!LuceneUtil.isUseLucene()) {
			return;
		}
		if (!ClusterBL.indexInstantly()) {
			LOGGER.debug("Index instantly is false");
			return;
		}
		IndexWriter indexWriter = LuceneIndexer.getIndexWriter(getIndexWriterID());
		if (indexWriter == null) {
			LOGGER.error("IndexWriter null by adding an expense");
			return;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Save a new " + add + " " +  getLuceneFieldName());
		}
		TCostBean costBean = (TCostBean)object;
		if (!add) {
			Integer objectID = costBean.getObjectID();
			if (objectID!=null) {
				Term keyTerm = new Term(getObjectIDFieldName(), objectID.toString());
				try {
					indexWriter.deleteDocuments(keyTerm);
					indexWriter.commit();
				} catch (IOException e) {
					LOGGER.error("Removing the entity " + objectID + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		try {
			Document doc = createDocument(costBean);
			if (doc!=null) {
				indexWriter.addDocument(doc);
			}
		} catch (IOException e) {
			LOGGER.error("Adding an expense to the index failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			indexWriter.commit();
		} catch (IOException e) {
			LOGGER.error("Flushing the expense failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	/**
	 * Returns an attachment document based on reader content. 
	 * @param issueNo
	 * @param originalName
	 * @param realName
	 * @param reader
	 * @return
	 */
	@Override
	protected Document createDocument(Object entry) {
		TCostBean costBean = (TCostBean)entry;
		Integer expenseID = costBean.getObjectID();
		Integer workItemID = costBean.getWorkItemID();
		String description = costBean.getDescription();
		String subject = costBean.getSubject();
		if (expenseID!=null && workItemID!=null && 
				((description!=null && description.trim().length()>0) ||
				(subject!=null && subject.trim().length()>0))) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Creating the " + getLuceneFieldName() +
						" document by expenseID " + expenseID +
						" workItemID " + workItemID +
						" subject " + subject +
						" description " + description);
			}
			try {
				Document document = new Document();
				document.add(new StringField(LuceneUtil.EXPENSE_INDEX_FIELDS.EXPENSEID, expenseID.toString(), Field.Store.YES));
				if (workItemID!=null) {
					document.add(new StringField(LuceneUtil.EXPENSE_INDEX_FIELDS.WORKITEMID, workItemID.toString(), Field.Store.YES));
				}
				if (subject!=null) {
					document.add(new TextField(LuceneUtil.EXPENSE_INDEX_FIELDS.SUBJECT, subject, Field.Store.NO));
				}
				if (description!=null) {
					document.add(new TextField(LuceneUtil.EXPENSE_INDEX_FIELDS.DESCRIPTION, description, Field.Store.NO));
				}
				return document;
			} catch (Exception e) {
				LOGGER.error("Creating the " + getLuceneFieldName() +
						" document for expenseID " + expenseID + 
						" and workItemID " + workItemID +
						" and subject " + subject +
						" and description " + description +
						" failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
	}
}
