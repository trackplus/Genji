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

package com.aurel.track.screen.item.adapterDAO;

import java.util.List;

import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenDAO;
import com.aurel.track.screen.adapterDAO.IScreenDAO;

/**
 * 
 */
public class ScreenDAOAdapter implements IScreenDAO {
    private static ScreenDAOAdapter instance;
    private ScreenDAO screenDAO;
    public static ScreenDAOAdapter getInstance(){
        if(instance==null){
            instance= new ScreenDAOAdapter();
        }
       return instance;
    }
    public ScreenDAOAdapter(){
        screenDAO=DAOFactory.getFactory().getScreenDAO();
    }
    public IScreen loadByPrimaryKey(Integer objectID) {
        return screenDAO.loadByPrimaryKey(objectID);
    }
    
    public IScreen tryToLoadByPrimaryKey(Integer objectID) {
        return screenDAO.tryToLoadByPrimaryKey(objectID);
    }

    public IScreen loadByPerson(Integer pk) {
        //no implementation
        return null;
    }

	public IScreen loadDefault() {
		//no implementation
		return null;
	}
	public List loadAll() {
        return screenDAO.loadAll();
    }

    public List loadAllOrdered(String sortKey, boolean ascending) {
        return screenDAO.loadAllOrdered(sortKey,ascending);
    }

    public Integer save(IScreen screen) {
        return screenDAO.save((TScreenBean)screen);
    }

    public void delete(Integer objectID) {
        screenDAO.delete(objectID);
    }

    public boolean isDeletable(Integer objectID) {
        return screenDAO.isDeletable(objectID);
    }
    
    /**
	 * Replaces the dependences with a new screenID
	 * @param oldScreenID
	 * @param newScreenID
	 */
	public void replaceScreen(Integer oldScreenID, Integer newScreenID) {
		screenDAO.replaceScreen(oldScreenID, newScreenID);
	}
}
