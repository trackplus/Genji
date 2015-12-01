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

package com.aurel.track.macro;

import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.report.execute.ReportBeanLink;

/**
 */
public class MacroContext {
	private TPersonBean personBean;
	private Locale locale;
	private TWorkItemBean workItemBean;
	private boolean useProjectSpecificID;
	Map<Integer, SortedSet<ReportBeanLink>> linksMap;
	Map<Integer, TWorkItemBean> itemsMap;

	public MacroContext() {
	}

	public TPersonBean getPersonBean() {
		return personBean;
	}

	public void setPersonBean(TPersonBean personBean) {
		this.personBean = personBean;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public TWorkItemBean getWorkItemBean() {
		return workItemBean;
	}

	public void setWorkItemBean(TWorkItemBean workItemBean) {
		this.workItemBean = workItemBean;
	}

	public boolean isUseProjectSpecificID() {
		return useProjectSpecificID;
	}

	public void setUseProjectSpecificID(boolean useProjectSpecificID) {
		this.useProjectSpecificID = useProjectSpecificID;
	}

	public Map<Integer, SortedSet<ReportBeanLink>> getLinksMap() {
		return linksMap;
	}

	public void setLinksMap(Map<Integer, SortedSet<ReportBeanLink>> linksMap) {
		this.linksMap = linksMap;
	}

	public Map<Integer, TWorkItemBean> getItemsMap() {
		return itemsMap;
	}

	public void setItemsMap(Map<Integer, TWorkItemBean> itemsMap) {
		this.itemsMap = itemsMap;
	}
}
