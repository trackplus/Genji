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
 * Base class for all activities
 * @author Tamas
 *
 */
public abstract class AbstractActivity implements IActivity {
	//the activity type
	protected Integer activityType;
	protected Integer setter;
	
	public AbstractActivity(Integer activityType) {
		this.activityType = activityType;
	}

	public Integer getActivityType() {
		return activityType;
	}
	
	/**
	 * Whether the activity refers to a field (system or custom) or to an other hardcoded activity
	 * @return
	 */
	public boolean hasSetter() {
		return true;
	}

	public Integer getSetter() {
		return setter;
	}

	public void setSetter(Integer setter) {
		this.setter = setter;
	}
	
	
}
