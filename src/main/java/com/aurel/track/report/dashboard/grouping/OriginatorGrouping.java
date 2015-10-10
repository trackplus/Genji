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

package com.aurel.track.report.dashboard.grouping;

import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TWorkItemBean;

/**
 * Group items by originator
 * @author Tamas
 *
 */
public class OriginatorGrouping extends AbstractGrouping{
	public OriginatorGrouping(int projectIndex) {
		super(projectIndex);
	}

	@Override
	protected Integer getWorkItemFieldValue(TWorkItemBean itemBean) {
		return itemBean.getOriginatorID();
	}

	@Override
	protected String formatKey(Integer labelBeanID) {
		return "orig"+labelBeanID+"-"+projectIndex;
	}
	
	@Override
	protected List groupByFieldList(Locale locale) {
		return PersonBL.loadPersons();
	}
}
