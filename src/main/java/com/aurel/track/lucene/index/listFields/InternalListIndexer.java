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

package com.aurel.track.lucene.index.listFields;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;

/**
 * Index the internal (inside track+ db tables) localized and lot localized list fields
 * @author Tamas Ruff
 *
 */
public abstract class InternalListIndexer extends AbstractListFieldIndexer {
	
	private static final Logger LOGGER = LogManager.getLogger(InternalListIndexer.class);
	
	/**
	 * Loads all indexable entitities for reindex
	 * @return
	 */
	protected List loadAllIndexable(IFieldTypeRT fieldTypeRT, Integer fieldID) {
		return ((ILookup)fieldTypeRT).getDataSource(fieldID);
	}
	/**
	 * 
	 * Adds a ILabelBeans to the non-localized lookup index  
	 * @param labelBean
	 * @param type
	 */
	public synchronized void addLabelBean(ILabelBean labelBean, int type, boolean add) {
		if (!LuceneUtil.isUseLucene()) {
			return;
		}
		if (!ClusterBL.indexInstantly()) {
			LOGGER.debug("Index instantly is false");
			return;
		}
		if (labelBean==null || labelBean.getObjectID()==null) {
			LOGGER.warn("Bean or value null by adding a internal list option of type " + type);
			return;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Save a new " + add + " internal list option of type " + type);
		}
		Document document = createDocument(labelBean, type);
		if (document!=null) {
			IndexWriter indexWriter = LuceneIndexer.getIndexWriter(getIndexWriterID());
			if (indexWriter == null) {
				LOGGER.error("IndexWriter null by adding a non-localized list option of type " + type);
				return;
			}
			try {
				indexWriter.addDocument(document);
			} catch (IOException e) {
				LOGGER.error("Adding the document for list option with key " +
						labelBean.getObjectID() + " and type " + type + " failed with " + e.getMessage(), e);
			}
			try {
				indexWriter.commit();
			} catch (IOException e) {
				LOGGER.error("Flushing list option with key " + labelBean.getObjectID() +
					" and type " + type + " failed with " + e.getMessage(), e);
			}
		}
	}
	/**
	 * Updates the non-localized lookup index with a modified ILabelBean 
	 * @param labelBean
	 * @param type
	 */
	public void updateLabelBean(ILabelBean labelBean, int type) {
		deleteByKeyAndType(labelBean.getObjectID(), type);
		addLabelBean(labelBean, type, false);
	}
	/**
	 * Creates a non-localized lookup document.
	 * @param externalObject
	 * @param fieldTypeRT
	 * @param fieldID
	 */
	protected Document createDocument(Object externalObject, IFieldTypeRT fieldTypeRT, Integer fieldID) {
		ILabelBean labelBean = (ILabelBean)externalObject;
		int type = fieldTypeRT.getLookupEntityType();
		return createDocument(labelBean, type);
	}
	/** 
	 * Creates a non-localized lookup document.
	 * @param labelBean
	 * @param type
	 * @return
	 */
	protected abstract Document createDocument(ILabelBean labelBean, int type);
}
