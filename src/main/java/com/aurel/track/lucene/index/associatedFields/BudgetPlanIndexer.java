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
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.item.budgetCost.BudgetBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;

/**
 * Index the budget/plan descriptions
 * @author Tamas Ruff
 *
 */
public class BudgetPlanIndexer extends AbstractAssociatedFieldIndexer {
	
	private static final Logger LOGGER = LogManager.getLogger(BudgetPlanIndexer.class);
	private static BudgetPlanIndexer instance;
	
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static BudgetPlanIndexer getInstance(){
		if(instance==null){
			instance=new BudgetPlanIndexer();
		}
		return instance;
	}
	/**
	 * Gets the index writer ID
	 * @return
	 */
	protected int getIndexWriterID() {
		return LuceneUtil.INDEXES.BUDGET_PLAN_INDEX;
	}
	/**
	 * Loads all indexable entitities for reindex
	 * @return
	 */
	protected List loadAllIndexable() {
		return BudgetBL.loadAllIndexable();
	}
	/**
	 * Gets the the unique identifier fieldName
	 * @return
	 */
	protected String getObjectIDFieldName() {
		return LuceneUtil.BUDGET_INDEX_FIELDS.PLANID;
	}
	/**
	 * Gets the fieldName containing the workItemID field
	 * @return
	 */
	protected String getWorkItemFieldName() {
		return LuceneUtil.BUDGET_INDEX_FIELDS.WORKITEMID;
	}
	
	/**
	 * Gets the associated field name for logging purposes  
	 * @return
	 */
	protected String getLuceneFieldName() {
		return LuceneUtil.BUDGET_PLAN;
	}
	
	/**
	 * Adds an attachment to the attachment index
	 * Used by attaching a new file to the workItem 
	 * @param attachFile
	 */
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
			LOGGER.error("IndexWriter null by adding a budget/plan");
			return;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Save a new " + add + " " +  getLuceneFieldName());
		}
		TBudgetBean budgetBean = (TBudgetBean)object;
		if (!add) {
			Integer objectID = budgetBean.getObjectID();
			if (objectID!=null) {
				Term keyTerm = new Term(getObjectIDFieldName(), objectID.toString());
				try {
					indexWriter.deleteDocuments(keyTerm);
					indexWriter.commit();
				} catch (IOException e) {
					LOGGER.error("Removing the entity " + objectID + " failed with " + e.getMessage(), e);
				}
			}
		}
		try {
			Document doc = createDocument(budgetBean);
			if (doc!=null) {
				indexWriter.addDocument(doc);
			}
		} catch (IOException e) {
			LOGGER.error("Adding a budget/plan to the index failed with " + e.getMessage(), e);
		}
		try {
			indexWriter.commit();
		} catch (IOException e) {
			LOGGER.error("Flushing the budget/plan failed with " + e.getMessage(), e);
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
	protected Document createDocument(Object entry) {
		TBudgetBean budgetBean = (TBudgetBean)entry;
		Integer budgetID = budgetBean.getObjectID();
		Integer workItemID = budgetBean.getWorkItemID();
		String description = budgetBean.getChangeDescription();
		Integer budgetType = budgetBean.getBudgetType();
		if (budgetID!=null && workItemID!=null && (description!=null && description.trim().length()>0)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Creating the " + getLuceneFieldName() +
						" document by budgetID " + budgetID +
						" workItemID " + workItemID +
						" budgetType " + budgetType +
						" description " + description);
			}
			try {
				Document document = new Document();
				document.add(new StringField(LuceneUtil.BUDGET_INDEX_FIELDS.PLANID, budgetID.toString(), Field.Store.YES));
				if (workItemID!=null) {
					document.add(new StringField(LuceneUtil.BUDGET_INDEX_FIELDS.WORKITEMID, workItemID.toString(), Field.Store.YES));
				}
				if (budgetType!=null) {
					document.add(new StringField(LuceneUtil.BUDGET_INDEX_FIELDS.BUDGET_TYPE, budgetType.toString(),	Field.Store.YES));
				}
				if (description!=null) {
					document.add(new TextField(LuceneUtil.BUDGET_INDEX_FIELDS.DESCRIPTION, description, Field.Store.NO));
				}
				return document;
			} catch (Exception e) {
				LOGGER.error("Creating the " + getLuceneFieldName() +
						" document for budgetID " + budgetID + 
						" and workItemID " + workItemID +
						" and budgetType " + budgetType +
						" and description " + description +
						" failed with " + e.getMessage(), e);
			}
		}
		return null;
	}
}
