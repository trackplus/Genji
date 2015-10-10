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

package com.aurel.track.admin.customize.workflow.activity;

/**
 * Base interface for workflow activities: config and execute
 * @author Tamas
 *
 */
public interface IActivity {
	/**
	 * @return the activityType
	 */
	Integer getActivityType();
	
	/**
	 * Whether the activity refers to a field (system or custom) or to an other hardcoded activity
	 * @return
	 */
	boolean hasSetter();
	
	
	/**
	 * @return the setter
	 */
	Integer getSetter();


	/**
	 * @param setter the relation to set
	 */
	void setSetter(Integer setter);
}
