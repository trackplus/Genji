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

package com.aurel.track.admin.user.profile;

import java.io.Serializable;
import java.util.List;

import com.aurel.track.util.IntegerStringBean;

/**
 * Transfer object used for the user profile watch list configuration.
 * @author Joerg Friedrich
 */
public class ProfileWatchlistTO implements Serializable{

	private static final long serialVersionUID = 400L;
	
	/**
	 * 
	 * The fields of this transfer object, that is their names in the JSON world.
	 * For example, in the user interface world a field would be named
	 * "wlist.systemVersion". The base name builds the first part, the second
	 * part is the specific field.
	 *
	 */
	public interface JSONFIELDS {
		String watchlist = "wlist";  // base name
		
		String filters = "filters";
	}
	
	private List<IntegerStringBean> filters;

	public List<IntegerStringBean> getFilters() {
		return filters;
	}

	public void setFilters(List<IntegerStringBean> filters) {
		this.filters = filters;
	}

}
