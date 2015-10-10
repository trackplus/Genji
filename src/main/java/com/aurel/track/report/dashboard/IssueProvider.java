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

package com.aurel.track.report.dashboard;

import java.util.Map;


/**
 * This interface describes an issue provider, a class that returns an
 * integer array of issue numbers based on the map with parameters.
 * <p>
 * A typical use is in dashboard views. The dashboard view may provide a link
 * pointing to the ReportLoadAction, with action=itemNos. If the ReportLoadAction
 * is called with this action type, it will try to retrieve the associated
 * issue provider from the session map, the associated parameter map 
 * from the session map, and then call this issue
 * provider with the map as parameter to retrieve an int[] of issue numbers.
 * <p>
 * These issue numbers will then be displayed in the report overview.
 */
public interface IssueProvider {
    public static final String LAYOUT_QUERY_TYPE="layout.queryType";
    public static final String LAYOUT_QUERY_ID="layout.queryID";

    public int[] getIssueNos(Map providerParams);

}
