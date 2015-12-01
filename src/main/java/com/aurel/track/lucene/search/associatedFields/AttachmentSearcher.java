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

package com.aurel.track.lucene.search.associatedFields;

import com.aurel.track.lucene.LuceneUtil;

/**
 * Search the expense subjects and descriptions
 * @author Tamas Ruff
 *
 */
public class AttachmentSearcher extends AbstractAssociatedFieldSearcher {
	
	private static AttachmentSearcher instance;
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static AttachmentSearcher getInstance(){
		if(instance==null){
			instance=new AttachmentSearcher();
		}
		return instance;
	}
	
	/**
	 * Gets the index searcher ID
	 * @return
	 */
	@Override
	protected int getIndexSearcherID() {
		return LuceneUtil.INDEXES.ATTACHMENT_INDEX;
	}	
	/**
	 * Gets the explicit field which can be used in lucene expresions
	 * The end user should know only this name
	 * @return
	 */
	@Override
	protected String getLuceneFieldName() {
		return LuceneUtil.ATTACHMENT;
	}
	/**
	 * Gets the lucene field names the string will be searched in 
	 * @return
	 */
	@Override
	protected String[] getSearchFieldNames() {
		return new String[] {LuceneUtil.ATTACHMENT_INDEX_FIELDS.ORIGINALNAME,
				LuceneUtil.ATTACHMENT_INDEX_FIELDS.CONTENT,
				LuceneUtil.ATTACHMENT_INDEX_FIELDS.DESCRIPTION};
	}
	/**
	 * Gets the fieldName containing the workItemID field
	 * @return
	 */
	@Override
	protected String getWorkItemFieldName() {
		return LuceneUtil.ATTACHMENT_INDEX_FIELDS.ISSUENO;
	}
}
