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

package com.aurel.track.screen.card.adapterDAO;

import java.util.List;

import com.aurel.track.beans.TCardPanelBean;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.CardPanelDAO;
import com.aurel.track.screen.adapterDAO.IPanelDAO;

/**
 *
 */
class CardPanelDAOAdapter implements IPanelDAO {
	private CardPanelDAO cardPanelDAO;
	private static CardPanelDAOAdapter instance;
	public static CardPanelDAOAdapter getInstance() {
		if(instance ==null){
			instance =new CardPanelDAOAdapter();
		}
		return instance;
	}

	private CardPanelDAOAdapter() {
		cardPanelDAO = DAOFactory.getFactory().getCardPanelDAO();
	}

	@Override
	public IPanel loadByPrimaryKey(Integer objectID) {
		return cardPanelDAO.loadByPrimaryKey(objectID);
	}
	@Override
	public IPanel loadFullByPrimaryKey(Integer objectID){
		return cardPanelDAO.loadFullByPrimaryKey(objectID);
	}

	@Override
	public List loadAll() {
		return null;
	}

	@Override
	public Integer save(IPanel panel) {
		return cardPanelDAO.save((TCardPanelBean)panel);
	}

	@Override
	public void delete(Integer objectID) {
		cardPanelDAO.delete(objectID);
	}

	@Override
	public boolean isDeletable(Integer objectID) {
		return true;
	}

	@Override
	public List loadByParent(Integer parentID) {
		return null;
	}
}
