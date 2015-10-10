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

package com.aurel.track.admin.customize.category.filter.execute;

import com.aurel.track.beans.TQueryRepositoryBean.QUERY_PURPOSE;


/**
 * Factory for filter type specific implementations
 * @author Tamas
 *
 */
public class FilterExecuterFacadeFactory {

	private static FilterExecuterFacadeFactory instance;
	public static FilterExecuterFacadeFactory getInstance(){
		if(instance==null){
			instance=new FilterExecuterFacadeFactory();
		}
		return instance;
	}

	/**
	 * Get the proper FilterExecuterFacade for filterType
	 * @param filterType
	 * @return
	 */
	public FilterExecuterFacade getFilterExecuterFacade(Integer filterType){
		if (filterType!=null) {
			switch (filterType.intValue()) {
			case QUERY_PURPOSE.TREE_FILTER:
				return TreeFilterExecuterFacade.getInstance();
			case QUERY_PURPOSE.TQLPLUS_FILTER:
				return TQLPlusFilterExecuterFacade.getInstance();
			case QUERY_PURPOSE.TQL_FILTER:
				return TQLFilterExecuterFacade.getInstance();
			default:
				break;
			}
		}
		return null;
	}
}
