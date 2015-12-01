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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
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

import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.lucene.index.associatedFields.textExctractor.ITextExtractor;
import com.aurel.track.lucene.index.associatedFields.textExctractor.TextExtractorFactory;

/**
 * Index the attachments
 * @author Tamas Ruff
 *
 */
public class AttachmentIndexer extends AbstractAssociatedFieldIndexer {

	private static final Logger LOGGER = LogManager.getLogger(AttachmentIndexer.class);
	private static AttachmentIndexer instance;
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static AttachmentIndexer getInstance(){
		if(instance==null){
			instance=new AttachmentIndexer();
		}
		return instance;
	}

	/**
	 * Gets the index writer ID
	 * @return
	 */
	@Override
	protected int getIndexWriterID() {
		return LuceneUtil.INDEXES.ATTACHMENT_INDEX;
	}

	/**
	 * Loads all indexable entities for reindex
	 * @return
	 */
	@Override
	protected List<TAttachmentBean> loadAllIndexable() {
		return AttachBL.loadAll();
	}

	/**
	 * Gets the the unique identifier fieldName
	 * @return
	 */
	@Override
	protected String getObjectIDFieldName() {
		return LuceneUtil.ATTACHMENT_INDEX_FIELDS.ATTACHMENTID;
	}
	/**
	 * Gets the fieldName containing the workItemID field
	 * @return
	 */
	@Override
	protected String getWorkItemFieldName() {
		return LuceneUtil.ATTACHMENT_INDEX_FIELDS.ISSUENO;
	}

	/**
	 * Gets the associated field name for logging purposes
	 * @return
	 */
	@Override
	protected String getLuceneFieldName() {
		return LuceneUtil.ATTACHMENT;
	}

	/**
	 * Adds an attachment to the attachment index
	 * Used by attaching a new file to the workItem
	 * @param attachFile
	 */
	@Override
	public void addToIndex(Object object, boolean add) {
		if (!LuceneUtil.isUseLucene() || !LuceneUtil.isIndexAttachments() || object==null) {
			return;
		}
		if (!ClusterBL.indexInstantly()) {
			LOGGER.debug("Index instantly is false");
			return;
		}
		IndexWriter indexWriter = LuceneIndexer.getIndexWriter(getIndexWriterID());
		if (indexWriter == null) {
			LOGGER.error("IndexWriter null by adding an attachment");
			return;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Save a new " + add + " attachment into attachmentIndex");
		}
		TAttachmentBean attachmentBean = (TAttachmentBean)object;
		if (!add) {
			Integer objectID = attachmentBean.getObjectID();
			if (objectID!=null) {
				Term keyTerm = new Term(getObjectIDFieldName(), objectID.toString());
				try {
					indexWriter.deleteDocuments(keyTerm);
					indexWriter.commit();
				} catch (IOException e) {
					LOGGER.error("Removing the attachment " + objectID + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		try {
			Document doc = createDocument(attachmentBean);
			if (doc!=null) {
				indexWriter.addDocument(doc);
			}
		} catch (IOException e) {
			LOGGER.error("Adding an attachment to the index failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			indexWriter.commit();
		} catch (IOException e) {
			LOGGER.error("Flushing the attachment failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	/**
	 * Adds all attachments of a workItem to the attachment index
	 * Used after saving a new issue which has also attachments
	 * @param attachFile
	 */
	@Override
	protected Document createDocument(Object entity) {
		TAttachmentBean attach = (TAttachmentBean)entity;
		Integer attachmentID = null;
		Integer workItemID = null;
		String fileName = "";
		String fullFilePath;
		String attachmentName ="";
		String description = "";
		try {
			attachmentID = attach.getObjectID();
			workItemID = attach.getWorkItem();
			fileName = attach.getFileNameOnDisk();
			fullFilePath = attach.getFullFileNameOnDisk();
			attachmentName = attach.getFileName();
			description = attach.getDescription();
			if (fullFilePath!=null) {
				File attachmentFile = new File(fullFilePath);
				if (attachmentFile.exists() && attachmentFile.isFile()) {
					String extension = LuceneFileExtractor.getExtension(attachmentName);
					if (LuceneFileExtractor.isStringExtension(extension)) {
						ITextExtractor textExtractor = TextExtractorFactory.getInstance().getTextExtractor(extension);
						String content = textExtractor.getText(attachmentFile, extension);
						if (content!=null && !"".equals(content)) {
							LOGGER.debug("AttachmentName " + attachmentName + " indexed by string content");
							return createAttachmentDocument(attachmentID, workItemID, attachmentName, fileName, description, content);
						}
					} else {
						if (LuceneFileExtractor.isReaderExtension(extension)) {
							Reader reader = LuceneFileExtractor.getReader(attachmentFile);
							if (reader!=null) {
								LOGGER.debug("AttachmentName " + attachmentName + " indexed by reader");
								return createAttachmentDocument(attachmentID, workItemID, attachmentName, fileName, description, reader);
							}
						} else {
							if (description!=null && !"".equals(description)) {
								return createAttachmentDocument(attachmentID, workItemID, attachmentName, fileName, description, (String)null);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("Indexing the attachment for workItem " + workItemID +
					" attachment: fileName " + fileName + " attachmentName " + attachmentName + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	/**
	 * Returns an attachment document based on string content.
	 * @param issueNo
	 * @param originalName
	 * @param realName
	 * @param content
	 * @return
	 */
	private Document createAttachmentDocument(Integer attachmentID, Integer issueNo,
			String originalName, String realName, String description, String content) {
		try {
			if (attachmentID!=null && issueNo!=null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Creating the " + getLuceneFieldName() +
							" document from string content by attachmentID " + attachmentID +
							" issueNo " + issueNo +
							" originalName " + originalName +
							" realName " + realName +
							" description " + description +
							" content not null " + Boolean.valueOf(content!=null).toString());
				}
				Document document = new Document();
				if (content!=null) {
					document.add(new TextField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.CONTENT, content,	Field.Store.NO));
				}
				document.add(new StringField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.ATTACHMENTID, attachmentID.toString(), Field.Store.YES));
				document.add(new StringField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.ISSUENO, issueNo.toString(), Field.Store.YES));
				document.add(new StringField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.ORIGINALNAME, originalName,	Field.Store.YES));
				document.add(new StringField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.REALNAME, realName, Field.Store.YES));
				if (description!=null) {
					document.add(new TextField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.DESCRIPTION, description, Field.Store.NO));
				}
				return document;
			}
		} catch (Exception e) {
			LOGGER.error("Creating the " + getLuceneFieldName() +
					" document from string content by attachmentID " + attachmentID +
					" issueNo " + issueNo +
					" originalName " + originalName +
					" realName " + realName +
					" description " + description +
					" content not null " + Boolean.valueOf(content!=null).toString() +
					" failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Returns an attachment document based on reader content.
	 * @param issueNo
	 * @param originalName
	 * @param realName
	 * @param reader
	 * @return
	 */
	private Document createAttachmentDocument(Integer attachmentID, Integer issueNo,
			String originalName, String realName, String description, Reader reader) {
		try {
			if (attachmentID!=null && issueNo!=null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Creating the " + getLuceneFieldName() +
							" document from reader by attachmentID " + attachmentID +
							" issueNo " + issueNo +
							" originalName " + originalName +
							" realName " + realName +
							" description " + description +
							" reader not null " + Boolean.valueOf(reader!=null).toString());
				}
				Document document = new Document();
				document.add(new TextField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.CONTENT, reader));
				document.add(new StringField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.ATTACHMENTID, attachmentID.toString(), Field.Store.YES));
				document.add(new StringField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.ISSUENO, issueNo.toString(), Field.Store.YES));
				document.add(new StringField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.ORIGINALNAME, originalName, Field.Store.YES));
				document.add(new StringField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.REALNAME, realName, Field.Store.YES));
				if (description!=null) {
					document.add(new TextField(LuceneUtil.ATTACHMENT_INDEX_FIELDS.DESCRIPTION, description, Field.Store.NO));
				}
				return document;
			}
		} catch (Exception e) {
			LOGGER.error("Creating the " + getLuceneFieldName() +
					" document from reader by attachmentID " + attachmentID +
					" issueNo " + issueNo +
					" originalName " + originalName +
					" realName " + realName +
					" description " + description +
					" reader not null " + Boolean.valueOf(reader!=null).toString() +
					" failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Removes the indexes for a list of workItems
	 * @param workItemIDs
	 */
	@Override
	public synchronized void deleteByWorkItems(List<Integer> workItemIDs) {
		if (!LuceneUtil.isIndexAttachments()) {
			return;
		}
		super.deleteByWorkItems(workItemIDs);
	}

}
