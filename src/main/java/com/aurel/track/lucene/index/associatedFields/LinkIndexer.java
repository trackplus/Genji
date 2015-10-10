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

import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.linkType.ILinkType.LINK_DIRECTION;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;

/**
 * Index the link descriptions
 * @author Tamas Ruff
 *
 */
public class LinkIndexer extends AbstractAssociatedFieldIndexer {
	
	private static final Logger LOGGER = LogManager.getLogger(LinkIndexer.class);
	private static LinkIndexer instance;
	
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static LinkIndexer getInstance(){
		if(instance==null){
			instance=new LinkIndexer();
		}
		return instance;
	}
	/**
	 * Gets the index writer ID
	 * @return
	 */
	protected int getIndexWriterID() {
		return LuceneUtil.INDEXES.LINK_INDEX;
	}
	/**
	 * Loads all indexable entitities for reindex
	 * @return
	 */
	protected List loadAllIndexable() {
		return ItemLinkBL.loadAllIndexable();
	}
	/**
	 * Gets the the unique identifier fieldName
	 * @return
	 */
	protected String getObjectIDFieldName() {
		return LuceneUtil.LINK_INDEX_FIELDS.LINKID;
	}
	/**
	 * Gets the fieldName containing the workItemID field
	 * @return
	 */
	protected String getWorkItemFieldName() {
		return LuceneUtil.LINK_INDEX_FIELDS.LINKPRED;
	}
	
	/**
	 * Gets the other field name containing the workitemID  field
	 * used for deleting documents by workItems. Makes sense only for links
	 * @return
	 */
	protected String getAdditionalFieldName() {
		return LuceneUtil.LINK_INDEX_FIELDS.LINKSUCC;
	}
	
	/**
	 * Gets the associated field name for logging purposes  
	 * @return
	 */
	protected String getLuceneFieldName() {
		return LuceneUtil.LINK;
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
			LOGGER.error("IndexWriter null by adding a link");
			return;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Save a new " + add + " " +  getLuceneFieldName());
		}
		TWorkItemLinkBean workItemLinkBean = (TWorkItemLinkBean)object;
		if (!add) {
			Integer objectID = workItemLinkBean.getObjectID();
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
			Document doc = createDocument(workItemLinkBean);
			if (doc!=null) {
				indexWriter.addDocument(doc);
			}
		} catch (IOException e) {
			LOGGER.error("Adding an link to the index failed with " + e.getMessage(), e);
		}
		try {
			indexWriter.commit();
		} catch (IOException e) {
			LOGGER.error("Flushing the link failed with " + e.getMessage(), e);
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
		TWorkItemLinkBean workItemLinkBean = (TWorkItemLinkBean)entry;
		Integer linkID = workItemLinkBean.getObjectID();
		String description = workItemLinkBean.getDescription();
		if (linkID!=null && description!=null && description.trim().length()>0) {
			Integer linkPred = workItemLinkBean.getLinkPred();
			Integer linkSucc = workItemLinkBean.getLinkSucc();
			Integer linkTypeID = workItemLinkBean.getLinkType();
			ILinkType linkType = LinkTypeBL.getLinkTypePluginInstanceByLinkTypeKey(linkTypeID);
			boolean isBidirectional = false;
			if (linkType.getPossibleDirection()==LINK_DIRECTION.BIDIRECTIONAL) {
				isBidirectional = true;
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Creating the " + getLuceneFieldName() +
						" document by linkID " + linkID +
						" linkPred " + linkPred +
						" linkSucc " + linkSucc +
						" description " + description);
			}
			try {
				Document document = new Document();
				document.add(new StringField(LuceneUtil.LINK_INDEX_FIELDS.LINKID, linkID.toString(), Field.Store.YES));
				if (linkPred!=null) {
					document.add(new StringField(LuceneUtil.LINK_INDEX_FIELDS.LINKPRED, linkPred.toString(), Field.Store.YES));
				}
				if (linkSucc!=null) {
					document.add(new StringField(LuceneUtil.LINK_INDEX_FIELDS.LINKSUCC, linkSucc.toString(), Field.Store.YES));
				}
				document.add(new StringField(LuceneUtil.LINK_INDEX_FIELDS.BIDIRECTIONAL, Boolean.valueOf(isBidirectional).toString(), Field.Store.YES));
				document.add(new TextField(LuceneUtil.LINK_INDEX_FIELDS.DESCRIPTION, description, Field.Store.NO));
				return document;
			} catch (Exception e) {
				LOGGER.error("Creating the link document for linkPred " + linkPred  + " and linkSucc " + linkSucc + " failed with " + e.getMessage(), e);
			}
		}
		return null;
	}
}
