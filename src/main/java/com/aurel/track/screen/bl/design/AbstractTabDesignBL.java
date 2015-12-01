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

package com.aurel.track.screen.bl.design;

/**
 * 
 */
import java.util.List;

import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.screen.bl.AbstractTabBL;

public abstract class AbstractTabDesignBL extends AbstractTabBL {

	/**
	 * constructor
	 */
	public AbstractTabDesignBL() {
		super();
	}
	/**
	 * Save a tab beean ant return the pk
	 * @param tab
	 * @return
	 */
	public  Integer saveScreenTab(ITab tab){
		return tabDAO.save(tab);
	}

	/**
	 * Delete the ScreenTab having given pk
	 * @param pk
	 */
	public  void deleteScreenTab(Integer pk){
		tabDAO.delete(pk);
	}
	public  void setTabProperty(ITab tab,ITab tabSchema){
		tab.setName(tabSchema.getName());
		tab.setLabel(tabSchema.getLabel());
		tab.setDescription(tabSchema.getDescription());
	}
	/**
	 * Add a new panel to a given tab
	 *
	 * @param tabid
	 * @param panelName
	 * @param description
	 * @param rows
	 * @param cols
	 */
	public  void addPanel(Integer tabid, String panelName,
			String description, int rows, int cols) {
		IPanel panel =getScreenFactory().createIPanelInstance();
		panel.setLabel(panelName);
		panel.setDescription(description);
		if (rows == 0) {
			panel.setRowsNo(new Integer(1));
		}else{
			panel.setRowsNo(new Integer(rows));
		}
		if (cols == 0) {
			panel.setColsNo(new Integer(1));
		}else{
			panel.setColsNo(new Integer(cols));
		}
		panel.setParent(tabid);
		panelDAO.save(panel);
	}
	/**
	 * Move a panel from an index to another index
	 * @param tabId
	 * @param panId
	 * @param newIndex
	 */
	public  void movePanel(Integer tabId,Integer panId,Integer newIndex){
		IPanel  pan=panelDAO.loadByPrimaryKey(panId);
		Integer oldIndex=pan.getIndex();
		if(oldIndex.equals(newIndex)){
			return;
		}
		//panel are sorted by index
		List panels=panelDAO.loadByParent(tabId);
		if(oldIndex.intValue()<newIndex.intValue()){
			for (int i=oldIndex.intValue()+1;i<=newIndex.intValue();i++){
				IPanel aPan = (IPanel) panels.get(i);
				aPan.setIndex(new Integer(aPan.getIndex().intValue()-1));
				panelDAO.save(aPan);
			}
		}else{
			for (int i=newIndex.intValue();i<oldIndex.intValue();i++){
				IPanel aPan = (IPanel) panels.get(i);
				aPan.setIndex(new Integer(aPan.getIndex().intValue()+1));
				panelDAO.save(aPan);
			}
		}
		pan.setIndex(newIndex);
		panelDAO.save(pan);
	}

}
