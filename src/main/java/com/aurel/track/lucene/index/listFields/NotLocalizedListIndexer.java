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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.lucene.LuceneUtil;

/**
 * Index the not localized lookup lists
 * @author Tamas Ruff
 *
 */
public class NotLocalizedListIndexer extends InternalListIndexer {
	
	private static final Logger LOGGER = LogManager.getLogger(NotLocalizedListIndexer.class);
	private static NotLocalizedListIndexer instance;
	
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static NotLocalizedListIndexer getInstance(){
		if(instance==null){
			instance=new NotLocalizedListIndexer();
		}
		return instance;
	}
	
	/**
	 * Gets the index writer ID
	 * @return
	 */
	@Override
	protected int getIndexWriterID() {
		return LuceneUtil.INDEXES.NOT_LOCALIZED_LIST_INDEX;
	}
	/**
	 * Gets the field name of the combined key which is the unique identifier
	 * @return
	 */
	@Override
	protected String getCombinedKeyFieldName() {
		return LuceneUtil.LIST_INDEX_FIELDS_NOT_LOCALIZED.COMBINEDKEY;
	}
	/**
	 * Gets the list field type for logging purposes  
	 * @return
	 */
	@Override
	protected String getListFieldType() {
		return "not localized";
	}
	/**
	 * Gets the fieldID types stored in this index 
	 * @return
	 */
	@Override
	protected List<Integer> getFieldIDs() {
		List<Integer> externalIDFields = new LinkedList<Integer>();
		externalIDFields.add(SystemFields.INTEGER_PROJECT);
		externalIDFields.add(SystemFields.INTEGER_RELEASE);	
		externalIDFields.add(SystemFields.INTEGER_PERSON);
		return externalIDFields;
	}
	/** 
	 * Creates a non-localized lookup document.
	 * @param labelBean
	 * @param type
	 * @return
	 */
	@Override
	protected Document createDocument(ILabelBean labelBean, int type) {
		String id = null;
		try {
			id = labelBean.getObjectID().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Creating the not localized list option document by id " + id +
						" label " + labelBean.getLabel() + " type " + String.valueOf(type));
			}
			Document document = new Document();
			document.add(new StringField(LuceneUtil.LIST_INDEX_FIELDS_NOT_LOCALIZED.COMBINEDKEY,
					LuceneUtil.getCombinedKeyValue(id, String.valueOf(type)),
					Field.Store.YES));
			document.add(new StringField(LuceneUtil.LIST_INDEX_FIELDS_NOT_LOCALIZED.VALUE,
					id, Field.Store.YES));
			document.add(new TextField(LuceneUtil.LIST_INDEX_FIELDS_NOT_LOCALIZED.LABEL,
					labelBean.getLabel(),  Field.Store.NO));
			document.add(new StringField(LuceneUtil.LIST_INDEX_FIELDS_NOT_LOCALIZED.TYPE,
					String.valueOf(type), Field.Store.YES));
			return document;
		} catch (Exception e) {
			LOGGER.error("Creating the document for non-localized list option " +
					" with label " + labelBean.getLabel() + " id " + id +
					" type " + type + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}
}
