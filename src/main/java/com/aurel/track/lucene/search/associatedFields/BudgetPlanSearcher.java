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

package com.aurel.track.lucene.search.associatedFields;

import com.aurel.track.lucene.LuceneUtil;

/**
 * Search the expense subjects and descriptions
 * @author Tamas Ruff
 *
 */
public class BudgetPlanSearcher extends AbstractAssociatedFieldSearcher {
	private static BudgetPlanSearcher instance;
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static BudgetPlanSearcher getInstance(){
		if(instance==null){
			instance=new BudgetPlanSearcher();
		}
		return instance;
	}
	/**
	 * Gets the index searcher ID
	 * @return
	 */
	protected int getIndexSearcherID() {
		return LuceneUtil.INDEXES.BUDGET_PLAN_INDEX;
	}
	/**
	 * Gets the explicit field which can be used in lucene expresions
	 * The end user should know only this name
	 * @return
	 */
	protected String getLuceneFieldName() {
		return LuceneUtil.BUDGET_PLAN;
	}
	/**
	 * Gets the lucene field names the string will be searched in 
	 * @return
	 */
	protected String[] getSearchFieldNames() {
		return new String[] {LuceneUtil.BUDGET_INDEX_FIELDS.DESCRIPTION};
	}
	/**
	 * Gets the fieldName containing the workItemID field
	 * @return
	 */
	protected String getWorkItemFieldName() {
		return LuceneUtil.BUDGET_INDEX_FIELDS.WORKITEMID;
	}
}