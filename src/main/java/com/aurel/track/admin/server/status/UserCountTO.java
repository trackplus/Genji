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

package com.aurel.track.admin.server.status;

/**
 * Transfer object for counting different users
 * @author Tamas
 *
 */
public class UserCountTO {
	private String label;
	private int numberOfActiveUsers;
	private int numberOfInactiveUsers;
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public int getNumberOfActiveUsers() {
		return numberOfActiveUsers;
	}
	public void setNumberOfActiveUsers(int numberOfActiveUsers) {
		this.numberOfActiveUsers = numberOfActiveUsers;
	}
	public int getNumberOfInactiveUsers() {
		return numberOfInactiveUsers;
	}
	public void setNumberOfInactiveUsers(int numberOfInactiveUsers) {
		this.numberOfInactiveUsers = numberOfInactiveUsers;
	}
	
	
}
