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

package com.aurel.track.screen.dashboard.adapterDAO;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.DashboardScreenDAO;
import com.aurel.track.screen.adapterDAO.IScreenDAO;

/**
 *
 */
class ScreenDAOAdapter implements IScreenDAO {
    private static ScreenDAOAdapter instance;
    private DashboardScreenDAO screenDAO;
    public static ScreenDAOAdapter getInstance(){
        if(instance==null){
            instance= new ScreenDAOAdapter();
        }
       return instance;
    }
    public ScreenDAOAdapter(){
        screenDAO=DAOFactory.getFactory().getDashboardScreenDAO();
    }
    
    @Override
    public IScreen loadByPrimaryKey(Integer objectID) {
        return screenDAO.loadByPrimaryKey(objectID);
    }

    @Override
    public IScreen tryToLoadByPrimaryKey(Integer objectID) {
        return screenDAO.tryToLoadByPrimaryKey(objectID);
    }
    
    @Override
    public IScreen loadByPerson(Integer pk) {
        return screenDAO.loadByPersonKey(pk);
    }
    
	@Override
	public IScreen loadDefault() {
		return screenDAO.loadDefault();
	}
	
	@Override
	public List loadAll() {
        return new ArrayList();
    }

    @Override
    public List loadAllOrdered(String sortKey, boolean ascending) {
        return new ArrayList();
    }

    @Override
    public Integer save(IScreen screen) {
        return screenDAO.save((TDashboardScreenBean)screen);
    }

    @Override
    public void delete(Integer objectID) {
        screenDAO.delete(objectID);
    }

    @Override
    public boolean isDeletable(Integer objectID) {
        return true;//screenDAO.isDeletable(objectID);
    }
}
