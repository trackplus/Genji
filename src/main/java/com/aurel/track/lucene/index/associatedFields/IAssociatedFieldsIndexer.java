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

import java.util.List;

import com.aurel.track.lucene.index.ILookupFieldIndexer;

/**
 * Base interface for associated lucene indexer fields:
 * attachments, link descriptions, expense descriptions, budget/plan descriptions
 * The workItemID is foreign key in the tables indexed here
 * @author Tamas Ruff
 *
 */
public interface IAssociatedFieldsIndexer extends ILookupFieldIndexer {
	
	/**
	 * Adds an associated entity to the corresponding index
	 * @param object
	 * @param add
	 */
	void addToIndex(Object object, boolean add);
	
	/**
	 * Removes the record indexes for a list of workItems
	 * @param workItemIDs
	 */	
	void deleteByWorkItems(List<Integer> workItemIDs);
	
	/**
	 * Deletes a document by key
	 * @param objectID
	 */
	void deleteByKey(Integer objectID);
}
