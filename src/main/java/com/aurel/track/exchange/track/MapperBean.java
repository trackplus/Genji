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

package com.aurel.track.exchange.track;

import java.util.List;

import com.aurel.track.beans.TFieldBean;

/**
 * Bean for storing the mapping data 
 * @author Tamas
 *
 */
public class MapperBean {	
	private ExchangeField exchangeField;
	private List<TFieldBean> possibleMappings;
	private Integer preselectedValue;
	
	public MapperBean() {
		super();
	}
	
	public MapperBean(ExchangeField importBean,
			List<TFieldBean> possibleMappings) {
		super();
		this.exchangeField = importBean;
		this.possibleMappings = possibleMappings;
	}
	
	public ExchangeField getExchangeField() {
		return exchangeField;
	}

	public void setExchangeField(ExchangeField exchangeField) {
		this.exchangeField = exchangeField;
	}

	public List<TFieldBean> getPossibleMappings() {
		return possibleMappings;
	}
	public void setPossibleMappings(List<TFieldBean> possibleMappings) {
		this.possibleMappings = possibleMappings;
	}

	public Integer getPreselectedValue() {
		return preselectedValue;
	}

	public void setPreselectedValue(Integer preselectedValue) {
		this.preselectedValue = preselectedValue;
	}
	
	
}
