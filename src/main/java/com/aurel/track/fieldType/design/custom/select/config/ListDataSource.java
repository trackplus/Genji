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


package com.aurel.track.fieldType.design.custom.select.config;

import java.util.List;

import com.aurel.track.beans.TOptionBean;

/**
 * The data structure representing an entry from a list
 * This structure enables to build hierarchical relations
 * Used for preparing the data to be shown in the view 
 * @author Tamas Ruff
 *
 */
public class ListDataSource {

	TOptionBean optionBean;
	List dependentList;
	
	public ListDataSource(){		
	}
		
	public ListDataSource(TOptionBean optionBean) {
		this.optionBean = optionBean;		
	}

	
	/**
	 * @return the dependentList
	 */
	public List getDependentList() {
		return dependentList;
	}
	/**
	 * @param dependentList the dependentList to set
	 */
	public void setDependentList(List dependentList) {
		this.dependentList = dependentList;
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
