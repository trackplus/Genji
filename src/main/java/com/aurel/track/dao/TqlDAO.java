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


package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TPrivateReportRepositoryBean;
import com.aurel.track.beans.TProjectReportRepositoryBean;
import com.aurel.track.beans.TPublicReportRepositoryBean;

/**
 * Data Access Object for TQL
 * TODO: add functions for delete
 */
public interface TqlDAO {
    public List getPublicTql();
    public List getPrivateTql(Integer userID);
    public List getProjectTql(Integer projectID);
    public List getProjectTqlByUser(Integer userId);
    public TProjectReportRepositoryBean getProjectTqlById(Integer id);
    public TPrivateReportRepositoryBean getPrivateTqlById(Integer id);
    public TPublicReportRepositoryBean getPublicTqlById(Integer id);
}
