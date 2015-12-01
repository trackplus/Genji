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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.beans.screen.IField;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TDashboardFieldBean
	extends com.aurel.track.beans.base.BaseTDashboardFieldBean
	implements Serializable, IField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3769353560799533073L;
	private Map<String,String> parametres;

	private Integer iconRendering;

	@Override
	public IField cloneMe() {
		TDashboardFieldBean fieldBean=new TDashboardFieldBean();
		fieldBean.setColIndex(this.getColIndex());
		fieldBean.setColSpan(this.getColSpan());
		fieldBean.setDescription(this.getDescription());
		fieldBean.setIndex(this.getIndex());
		fieldBean.setName(this.getName());
		fieldBean.setParent(this.getParent());
		fieldBean.setRowIndex(this.getRowIndex());
		fieldBean.setRowSpan(this.getRowSpan());

		fieldBean.setDashboardID(this.getDashboardID());
		fieldBean.setParametres(this.getParametres());
		return fieldBean;
	}

	public TDashboardFieldBean() {
		super();
		this.parametres = new HashMap<String,String>();
	}

	public Map<String,String> getParametres() {
		return parametres;
	}

	public void setParametres(Map<String,String> parametres) {
		this.parametres = parametres;
	}

	@Override
	public void setIconRendering(Integer iconRendering){
		this.iconRendering=iconRendering;
	}
	@Override
	public Integer getIconRendering(){
		return iconRendering;
	}

}
