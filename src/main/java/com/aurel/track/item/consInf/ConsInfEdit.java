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


package com.aurel.track.item.consInf;


import java.util.List;

import com.aurel.track.beans.TPersonBean;
/**
 * Bean storing the consultant/informant data for the add consultant/informant popup
 * @author Tamas Ruff
 *
 */
public class ConsInfEdit {
		
	private List<TPersonBean> potentialPersons;
	private List<TPersonBean> potentialGroups;
	private Integer[] selectedPersons;
	private Integer[] selectedGroups;
	private String selectedPersonsStr;
	
	public List<TPersonBean> getPotentialGroups() {
		return potentialGroups;
	}
	public void setPotentialGroups(List<TPersonBean> potentialGroups) {
		this.potentialGroups = potentialGroups;
	}
	public List<TPersonBean> getPotentialPersons() {
		return potentialPersons;
	}
	public void setPotentialPersons(List<TPersonBean> potentialPersons) {
		this.potentialPersons = potentialPersons;
	}
	public Integer[] getSelectedGroups() {
		return selectedGroups;
	}
	public void setSelectedGroups(Integer[] selectedGroups) {
		this.selectedGroups = selectedGroups;
	}
	public Integer[] getSelectedPersons() {
		return selectedPersons;
	}
	public void setSelectedPersons(Integer[] selectedPersons) {
		this.selectedPersons = selectedPersons;
	}

	public String getSelectedPersonsStr() {
		return selectedPersonsStr;
	}

	public void setSelectedPersonsStr(String selectedPersonsStr) {
		this.selectedPersonsStr = selectedPersonsStr;
	}
}
