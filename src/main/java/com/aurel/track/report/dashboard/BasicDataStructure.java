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


package com.aurel.track.report.dashboard;

import java.util.Map;

public class BasicDataStructure {

	private Map priorities;

	private Map states;

	private Map persons;

	private Map myTqlView;

	private Map myItems;

	public Map getMyTqlView() {
		return myTqlView;
	}

	public Map getPersons() {
		return persons;
	}

	public void setPersons(Map persons) {
		this.persons = persons;
	}

	public Map getPriorities() {
		return priorities;
	}

	public void setPriorities(Map priorities) {
		this.priorities = priorities;
	}

	public Map getStates() {
		return states;
	}

	public void setStates(Map states) {
		this.states = states;
	}

	public void setMyTqlView(Map myTqlView) {
		this.myTqlView = myTqlView;
	}

	public Map getMyItems() {
		return myItems;
	}

	public void setMyItems(Map myItems) {
		this.myItems = myItems;
	}
}
