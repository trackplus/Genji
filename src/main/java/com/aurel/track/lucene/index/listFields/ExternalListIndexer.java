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
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.callbackInterfaces.IExternalLookupLucene;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.LuceneUtil.EXTERNAL_INDEX_FIELDS;

/**
 * Index the external list fields
 * @author Tamas Ruff
 *
 */
public class ExternalListIndexer extends AbstractListFieldIndexer {
	
	private static final Logger LOGGER = LogManager.getLogger(ExternalListIndexer.class);
	private static ExternalListIndexer instance;
	
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static ExternalListIndexer getInstance(){
		if(instance==null){
			instance=new ExternalListIndexer();
		}
		return instance;
	}
	/**
	 * Gets the index writer ID
	 * @return
	 */
	@Override
	protected int getIndexWriterID() {
		return LuceneUtil.INDEXES.EXTERNAL_LOOKUP_WRITER;
	}
	/**
	 * Loads all indexable entitities for reindex
	 * @return
	 */
	@Override
	protected List loadAllIndexable(IFieldTypeRT fieldTypeRT, Integer fieldID) {
		return ((IExternalLookupLucene)fieldTypeRT).getAllExternalLookups();
	}
	/**
	 * Gets the field name of the combined key which is the unique identifier
	 * @return
	 */
	@Override
	protected String getCombinedKeyFieldName() {
		return LuceneUtil.EXTERNAL_INDEX_FIELDS.COMBINEDKEY;
	}
	/**
	 * Gets the list field type for logging purposes  
	 * @return
	 */
	@Override
	protected String getListFieldType() {
		return "external";
	}
	/**
	 * Gets the fieldIDs stored in this index 
	 * @return
	 */
	@Override
	protected List<Integer> getFieldIDs() {
		List<Integer> externalIDFields = new LinkedList<Integer>();
		Map<Integer, FieldType> fieldTypeCache = FieldTypeManager.getInstance().getTypeCache();
		if (fieldTypeCache!=null) {
			for (Integer fieldID : fieldTypeCache.keySet()) {
				FieldType fieldType = fieldTypeCache.get(fieldID);
				if (fieldType!=null) {
					IFieldTypeRT fieldTypeRT = fieldType.getFieldTypeRT();
					if (fieldTypeRT.getValueType()==ValueType.EXTERNALID) {
						externalIDFields.add(fieldID);
					}
				}
			}
		}
		return externalIDFields;
	}
	/**
	 * Creates an external lookup document
	 */
	@Override
	protected Document createDocument(Object externalObject, IFieldTypeRT fieldTypeRT, Integer fieldID) {
		IExternalLookupLucene externalLookupLucene = (IExternalLookupLucene)fieldTypeRT;
		String id = null;
		try {
			Document document = new Document();
			Integer lookupObjectID = externalLookupLucene.getLookupObjectID(externalObject);
			if (lookupObjectID==null) {
				return null;
			}
			id = lookupObjectID.toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Creating the external list option document by id " + id +
						" fieldID " + fieldID);
			}
			document.add(new StringField(EXTERNAL_INDEX_FIELDS.OBJECTID, id, Field.Store.YES));
			document.add(new StringField(EXTERNAL_INDEX_FIELDS.FIELDID, String.valueOf(fieldID), Field.Store.YES));
			String[] searchableFieldNames = externalLookupLucene.getSearchableFieldNames();
			Map<String, String> unfoldedSearchFields = externalLookupLucene.getUnfoldedSearchFields(externalObject);
			for (String searchableFieldName : searchableFieldNames) {
				String unfoldSearchFieldValue = unfoldedSearchFields.get(searchableFieldName);
				if (unfoldSearchFieldValue != null) {
					document.add(new TextField(searchableFieldName, unfoldSearchFieldValue, Field.Store.NO));
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("searchableFieldName " + searchableFieldName +
								" unfoldSearchFieldValue " + unfoldSearchFieldValue);
					}
				}
			}
			return document;
		} catch (Exception e) {
			LOGGER.error("Creating the document for external list option with id " +
					id + " and field " + fieldID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}
}
