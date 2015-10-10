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

package com.aurel.track.screen.dashboard.adapterDAO;

import java.util.List;
import java.util.Set;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.DashboardFieldDAO;
import com.aurel.track.screen.adapterDAO.IFieldDAO;

/**
 *
 */
class FieldDAOAdapter implements IFieldDAO {
    private DashboardFieldDAO dashboardFieldDAO;
    private static FieldDAOAdapter instance;
    public static FieldDAOAdapter getInstance() {
        if(instance ==null){
            instance =new FieldDAOAdapter();
        }
        return instance;
    }
    private FieldDAOAdapter() {
        dashboardFieldDAO = DAOFactory.getFactory().getDashboardFieldDAO();
    }

    public IField loadByPrimaryKey(Integer objectID) {
        return dashboardFieldDAO.loadByPrimaryKey(objectID);
    }

    public List loadAll() {
        return dashboardFieldDAO.loadAll();
    }
 

    public Integer save(IField field) {
        return dashboardFieldDAO.save((TDashboardFieldBean)field);
    }

    public void delete(Integer objectID) {
        dashboardFieldDAO.delete(objectID);
    }

    public boolean isDeletable(Integer objectID) {
        return dashboardFieldDAO.isDeletable(objectID);
    }

    public List loadByParent(Integer parentID) {
        return dashboardFieldDAO.loadByParent(parentID);
    }

    public IField loadByParentAndIndex(Integer parentID, Integer row, Integer col) {
        return dashboardFieldDAO.loadByParentAndIndex(parentID,row,col);
    }
}
