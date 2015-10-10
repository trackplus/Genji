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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.ITab;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TDashboardTabBean
	extends com.aurel.track.beans.base.BaseTDashboardTabBean
	implements Serializable, ITab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4848372779420679L;
	private List<IPanel> panels;
	private HashSet<String>fieldTypes;
	
	public List<IPanel> getPanels() {
		return panels;
	}
	public void setPanels(List<IPanel> panels) {
		this.panels = panels;
	}
	public ITab cloneMe(){
		TDashboardTabBean tabBean=new TDashboardTabBean();
		tabBean.setDescription(this.getDescription());
		tabBean.setIndex(this.getIndex());
		tabBean.setLabel(this.getLabel());
		tabBean.setName(this.getName());
		tabBean.setParent(this.getParent());

		List<IPanel> panelsClone=new ArrayList<IPanel>();
		List<IPanel> panels=this.getPanels();
		if(panels!=null){
			for(IPanel p:panels){
				panelsClone.add(p.cloneMe());
			}
		}
		tabBean.setPanels(panelsClone);

		return tabBean;
	}
	
	/**
	 * Return all dashboard ID's from tab
	 */
	public void setFieldTypes(HashSet<String> fieldTypes) {
		this.fieldTypes = fieldTypes;
		
	}
	/**
	 * Set all dashboard ID's on tab
	 */
	public Set<String> getFieldTypes() {
		return this.fieldTypes;
	}
}
