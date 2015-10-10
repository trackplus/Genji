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

package com.aurel.track.admin.customize.category.filter;

import com.aurel.track.beans.TQueryRepositoryBean;


/**
 * Factory for filter type specific implementations
 * @author Tamas
 *
 */
public class FilterFacadeFactory {
	
	private static FilterFacadeFactory instance;
	public static FilterFacadeFactory getInstance(){
		if(instance==null){
			instance=new FilterFacadeFactory();
		}
		return instance;
	}

			
	/**
	 * Get the proper FilterFacade for filterType
	 * @param filterType
	 * @param treeFilter: important only by FilterAction.loadDetail and FilterAction.Save
	 * @return
	 */
	public FilterFacade getFilterFacade(Integer filterType, boolean treeFilter){
		if (filterType==TQueryRepositoryBean.QUERY_PURPOSE.NOTIFY_FILTER) {
			return NotifyFilterFacade.getInstance();
		} else {
			if(treeFilter){
				return TreeFilterFacade.getInstance();
			} else {
				if (filterType == TQueryRepositoryBean.QUERY_PURPOSE.TQL_FILTER) {
					return TQLFilterFacade.getInstance();
				} else {
					return TQLPlusFilterFacade.getInstance();
				}
			}		
		}
	}
}
