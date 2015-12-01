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

package com.aurel.track.report.dashboard.grouping;

import com.aurel.track.fieldType.constants.SystemFields;

/**
 * Grouping class by fieldID 
 * @author Tamas
 *
 */
public class GroupingFactory {
	
	public static AbstractGrouping getGrouping(int fieldID, int projectIndex) {
		switch (fieldID) {
		case SystemFields.STATE:
			return new StatusGrouping(projectIndex);
		case SystemFields.ISSUETYPE:
			return new IssueTypeGrouping(projectIndex);
		case SystemFields.ORIGINATOR:
			return new OriginatorGrouping(projectIndex);
		case SystemFields.MANAGER:
			return new ManagerGrouping(projectIndex);
		case SystemFields.RESPONSIBLE:
			return new ResponsibleGrouping(projectIndex);
		case SystemFields.PRIORITY:
			return new PriorityGrouping(projectIndex);
		case SystemFields.SEVERITY:
			return new SeverityGrouping(projectIndex);
		default:
			return null;
		}
	}
}
