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

package com.aurel.track.fieldType.design.custom.select.config;

import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TOptionBean;
/**
 * Models the option hierarchy for cascading selects
 * @author Tamas
 *
 */
public class OptionHierarchy {
	
	TOptionBean optionBean;
	//child list based children
	Map<Integer, List<OptionHierarchy>> listBasedChildrenMap;
	
	public Map<Integer, List<OptionHierarchy>> getListBasedChildrenMap() {
		return listBasedChildrenMap;
	}

	public void setListBasedChildrenMap(
			Map<Integer, List<OptionHierarchy>> listBasedChildrenMap) {
		this.listBasedChildrenMap = listBasedChildrenMap;
	}

	/**
	 * @return the optionBean
	 */
	public TOptionBean getOptionBean() {
		return optionBean;
	}

	/**
	 * @param optionBean the optionBean to set
	 */
	public void setOptionBean(TOptionBean optionBean) {
		this.optionBean = optionBean;
	}
}

