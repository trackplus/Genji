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

package com.aurel.track.lucene.search.listFields;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.lucene.LuceneUtil;

public class LocalizedListCompositePartSearcher extends LocalizedListSearcher {
	
	private static LocalizedListCompositePartSearcher instance;
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static LocalizedListCompositePartSearcher getInstance(){
		if(instance==null){
			instance=new LocalizedListCompositePartSearcher();
		}
		return instance;
	}
	
	
	/**
	 * Gets the fieldIDs stored in this index for searching by explicit fields:
	 * gather the composite fields
	 * @return
	 */
	protected List<Integer> getFieldIDs() {
		List<Integer> fieldIDs = new LinkedList<Integer>();
		Map<Integer, FieldType> fieldTypeCache = FieldTypeManager.getInstance().getTypeCache();
		if (fieldTypeCache!=null) {
			for (Integer fieldID : fieldTypeCache.keySet()) {
				FieldType fieldType = fieldTypeCache.get(fieldID);
				if (fieldType!=null) {
					IFieldTypeRT fieldTypeRT = fieldType.getFieldTypeRT();
					if (fieldTypeRT.isComposite()) {
						fieldIDs.add(fieldID);
					}
				}
			}
		}
		return fieldIDs;
	}
	
	/**
	 * Preprocess an explicit field: for a composite preprocess each part separately
	 * @param analyzer 
	 * @param toBeProcessedString a part of the user entered query string
	 * @param locale the name of the user entered field
	 * @param indexStart the index to start looking for fieldName 
	 * @return
	 */
	public String preprocessExplicitField(Analyzer analyzer,
			String toBeProcessedString, Locale locale, int indexStart) {
		List<Integer> fieldIDs = getFieldIDs();
		for (Integer fieldID : fieldIDs) {
			TFieldBean fieldBean = FieldTypeManager.getInstance().getFieldBean(fieldID);
			if (fieldBean!=null) {
				String fieldName = LuceneUtil.normalizeFieldName(fieldBean.getName());
				CustomCompositeBaseRT customCompositeBaseRT = (CustomCompositeBaseRT)FieldTypeManager.getFieldTypeRT(fieldID);
				int numberOfParts = customCompositeBaseRT.getNumberOfParts();
				for (int i = 0; i < numberOfParts; i++) {
					Integer parameterCode = Integer.valueOf(i+1);
					String luceneFieldName = LuceneUtil.synthetizeCompositePartFieldName(fieldName, parameterCode);
					toBeProcessedString = replaceExplicitFieldValue(analyzer, toBeProcessedString,
							luceneFieldName, luceneFieldName, fieldID, locale, indexStart);
				}
			}
		}
		return toBeProcessedString;
	}
	
	/**
	 * A custom composite lookup part is always stored as custom option
	 */
	protected int getEntityType(Integer fieldID) {
		return LuceneUtil.LOOKUPENTITYTYPES.CUSTOMOPTION;
	}
	
	/**
	 * Gets the workItem field names for a type
	 * The type stored in list index is LuceneUtil.LOOKUPENTITYTYPES.CUSTOMOPTION for all custom lists
	 * (simple and composite). For composite part searcher it will be forced to get the composite parts 
	 */
	protected String[] getWorkItemFieldNames(Integer type) {
		return getWorkItemFieldNamesForLookupType(LuceneUtil.LOOKUPENTITYTYPES.COMPOSITE);
	}
}
