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

package com.aurel.track.screen.bl;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.screen.FieldWrapper;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.adapterDAO.IFieldDAO;
import com.aurel.track.screen.adapterDAO.IPanelDAO;


/**
 *
 */
public abstract class AbstractPanelBL {
	
	public static final int MAX_ROWS=30;
	public static final int MAX_COLS=30;
	protected IPanelDAO panelDAO;
	protected IFieldDAO fieldDAO;
	protected abstract ScreenFactory getScreenFactory();

	public AbstractPanelBL(){
		panelDAO = getScreenFactory().getPanelDAO();
		fieldDAO = getScreenFactory().getFieldDAO();
	}
	/**
	 * Load a ScreenPanel having given pk
	 * @param pk
	 * @return
	 */
	public IPanel loadPanel(Integer pk){
		IPanel panel=panelDAO.loadByPrimaryKey(pk);
		return panel;
	}
	public IPanel loadPanelWrapped(Integer pk){
		IPanel panel=panelDAO.loadFullByPrimaryKey(pk);
		panel.setFieldWrappers(getFieldsAsTable(panel));
		return panel;
	}
	
	/**
	 * @param tab
	 * @return
	 */
	public void calculateFieldWrappers(ITab tab){
		if (tab!=null) {
			List<IPanel> panels=tab.getPanels();
			if(panels!=null){
				for (Iterator<IPanel> iter = panels.iterator(); iter.hasNext();) {
					IPanel pan = iter.next();
					pan.setFieldWrappers(getFieldsAsTable(pan));
				}
			}
		}
	}
	/**
	 * Obtain the field from a panel in useful form used for html rendering
	 * @param pan
	 * @return
	 */
	public  FieldWrapper[][] getFieldsAsTable(IPanel pan){
		int rows=pan.getRowsNo().intValue();
		int cols=pan.getColsNo().intValue();
		FieldWrapper[][] result=new FieldWrapper[rows][cols];
		// a matrix for used cells - at beginning all cells are not used
		boolean[][] used=new boolean[rows][cols];
		for(int r=0;r<rows;r++){
			for(int c=0;c<cols;c++){
				if(used[r][c]) continue;
				int colSpan=1;
				int rowSpan=1;
				IField fieldScreen=pan.getScreenField(r,c);
				if(fieldScreen!=null){
					colSpan=fieldScreen.getColSpan().intValue();
					rowSpan=fieldScreen.getRowSpan().intValue();
				}
				//mark as used for span
				for(int i=1;i<rowSpan&&r+i<rows;i++){
					used[r+i][c]=true;
					for(int j=1;j<colSpan&&c+j<cols;j++){
						used[r+i][c+j]=true;
					}
				}
				for(int j=1;j<colSpan&&c+j<cols;j++){
					used[r][c+j]=true;
				}

				result[r][c]=createFieldWrapper();

				result[r][c].setField(fieldScreen);
				result[r][c].setRow(r);
				result[r][c].setCol(c);
				if(fieldScreen!=null){
					updateFieldWrapper(result[r][c],fieldScreen);
				}
			}
		}
		return result;
	}
	protected abstract FieldWrapper createFieldWrapper();
	/**
	 * @param fieldWrapper
	 * @param fieldScreen
	 * 
	 */
	protected abstract void updateFieldWrapper(FieldWrapper fieldWrapper, IField fieldScreen);

}
