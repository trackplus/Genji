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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.List;

import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TDashboardScreenBean
	extends com.aurel.track.beans.base.BaseTDashboardScreenBean
	implements Serializable, IScreen, ILabelBean {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3973777296324993340L;
	private List<ITab> tabs;

	@Override
	public Integer getPersonID() {
		return getPerson();
	}

	@Override
	public void setPersonID(Integer personID) {
		setPerson(personID);
	}

	@Override
	public List<ITab> getTabs() {
		return tabs;
	}

	@Override
	public void setTabs(List<ITab> tabs) {
		this.tabs = tabs;
	}
	@Override
	public String getTagLabel() {
		return null;
	}
	
	@Override
	public void setTagLabel(String tagLabel) {

	}
}
