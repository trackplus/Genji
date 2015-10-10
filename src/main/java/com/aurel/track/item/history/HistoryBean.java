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

package com.aurel.track.item.history;

import java.util.Date;

/**
 * A common interface for history beans, used for sort
 *
 */
public interface HistoryBean extends IChronologicalField {
    
	public static interface HISTORY_TYPE {
		public static int COMMON_HISTORYVALUES = 0;
		public static int OTHER_EXPLICIT_HISTORY = -1;
		public static int COMMENT = -2;
		public static int TRAIL = 1;
		public static int BASELINE_CHANGE = 2;
		public static int STATE_CHANGE = 3;
		public static int PLANNED_VALUE_CHANGE = 4;
		public static int REMAINING_PLAN_CHANGE = 5;
		public static int COST = 6;
		public static int ATTACHMENT = 7;
		public static int CHILD_ADDED = 8; 
		public static int BUDGET_CHANGE = 9;
	}
	/**
	 * Get the object id
	 * @return
	 */
	Integer getObjectID();
	
	/**
	 * Get the type of the history bean
	 * It should be one of the HISTORY_TYPE constants
	 * @return
	 */
	int getType();
	
	/**
     * last edit 
     * @return
     */
    Date getLastEdit();
    
    /**
     * The name of the person who made the modification on a workItem
     * @return
     */
    String getChangedByName();
    
    /**
     * Set the name of the changedBy person
     * @return
     */
    void setChangedByName(String name);
    
    /**
     * The personID who made the modification on a workItem
     * @return
     */
    Integer getChangedByID();
    
    /**
     * The ID of the workItem, the the history bean refers to
     * @return
     */
    Integer getWorkItemID();
    
    /**
     * Get the description of the change
     * @return
     */
    String getDescription();
    
    /**
     * Set the description of the change
     * @param description
     * @return
     */
    void setDescription(String description);
}
