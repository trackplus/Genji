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

package com.aurel.track.admin.customize.workflow.activity;

import java.util.List;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;

/**
 * Executes a workflow activity
 * @author Tamas
 *
 */
public interface IActivityExecute extends IActivity {
	/**
	 * Executes an activity
	 * @param workItemContext
	 * @param value	the value converted from string
	 * @param personBean the person executing the activity
	 * @return ErrorData list if an error is found
	 */
	List<ErrorData> executeActivity(WorkItemContext workItemContext,
			Object value, TPersonBean personBean);
}
