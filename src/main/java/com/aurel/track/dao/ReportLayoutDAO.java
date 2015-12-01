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


package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TReportLayoutBean;

public interface ReportLayoutDAO {
	public TReportLayoutBean loadByPrimaryKey(Integer objectID);
	public List<TReportLayoutBean> loadAll();
	public List<TReportLayoutBean> getByPerson(Integer personKey, boolean grouping, Integer layoutType);
	public List<TReportLayoutBean> getByQuery(Integer personKey,Integer queryType,Integer queryID);
	public void save(List<TReportLayoutBean> layout);
	public void save(TReportLayoutBean reportLayoutBean);
	public void remove(TReportLayoutBean reportLayoutBean);
    public void removeByPerson(Integer personKey, Integer layoutType);
    public void removeByQueryPerson(Integer personKey,Integer queryType,Integer queryID);
    public void removeByPersonAndReportFieldNotIn(Integer personKey, Integer layoutType,List<Integer> remainIds,Integer queryType,Integer queryID);
}
	
