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

package com.aurel.track.itemNavigator.filterInMenu;

import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;

/**
 * Factory for report or filter categories
 * @author Tamas
 *
 */
public class FilterInMenuFactory {
	
	private static FilterInMenuFactory instance;
	public static FilterInMenuFactory getInstance(){
		if(instance==null){
			instance=new FilterInMenuFactory();
		}
		return instance;
	}

	
	/**
	 * Get the proper configItem for configType
	 * @param categoryType
	 * @return
	 */
	public IFilterInMenu getFilterInMenuFacade(Integer queryType, Integer queryID) {
		if (queryType!=null) {
			switch (queryType.intValue()) {
			case QUERY_TYPE.INSTANT:
				return new InstantFilterInMenu(queryID);
			case QUERY_TYPE.SAVED:
				return new SavedFilterInMenu(queryID);
			case QUERY_TYPE.DASHBOARD:
				return new DashboardFilterInMenu(queryID);
			case QUERY_TYPE.BASKET:
				return new BasketFilterInMenu(queryID);
			case QUERY_TYPE.PROJECT_RELEASE:
				return new ProjectReleaseFilterInMenu(queryID);
			case QUERY_TYPE.LUCENE_SEARCH:
				return new LuceneSearchInMenu(queryID);
			case QUERY_TYPE.STATUS:
				return new StatusFilterInMenu(queryID);
			}
		}
		return null;
	}
	
	
	
}
