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


package com.aurel.track.fieldType.design.custom.select.config;

import java.util.List;

/**
 * Helper class for copying hierarchical lists (and their options) 
 * @author Tamas Ruff
 *
 */
public class ListHierarchy {
	private Integer srcListID;
	private Integer destListID;
	private List<ListHierarchy> dependentLists;
	/**
	 * @return the dependentLists
	 */
	public List<ListHierarchy> getDependentLists() {
		return dependentLists;
	}
	/**
	 * @param dependentLists the dependentLists to set
	 */
	public void setDependentLists(List<ListHierarchy> dependentLists) {
		this.dependentLists = dependentLists;
	}	
	/**
	 * @return the destListID
	 */
	public Integer getDestListID() {
		return destListID;
	}
	/**
	 * @param destListID the destListID to set
	 */
	public void setDestListID(Integer destListID) {
		this.destListID = destListID;
	}
	/**
	 * @return the srcListID
	 */
	public Integer getSrcListID() {
		return srcListID;
	}
	/**
	 * @param srcListID the srcListID to set
	 */
	public void setSrcListID(Integer srcListID) {
		this.srcListID = srcListID;
	}
	
	
}
