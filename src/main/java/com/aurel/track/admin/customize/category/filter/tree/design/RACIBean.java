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

package com.aurel.track.admin.customize.category.filter.tree.design;

import java.util.Set;


/**
 * Helper object for RACI related data
 * @author Tamas
 *
 */
public class RACIBean {
	private Set<Integer> authors = null;
	private Set<Integer> responsibles = null;
	private Set<Integer> managers = null;
	public Set<Integer> getAuthors() {
		return authors;
	}
	public void setAuthors(Set<Integer> authors) {
		this.authors = authors;
	}
	public Set<Integer> getResponsibles() {
		return responsibles;
	}
	public void setResponsibles(Set<Integer> responsibles) {
		this.responsibles = responsibles;
	}
	public Set<Integer> getManagers() {
		return managers;
	}
	public void setManagers(Set<Integer> managers) {
		this.managers = managers;
	}
	
	
}
