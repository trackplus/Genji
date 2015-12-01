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

package com.aurel.track.screen.bl;

import com.aurel.track.beans.screen.IField;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.FieldDAO;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.adapterDAO.IFieldDAO;

/**
 * 
 */
public abstract class AbstractFieldBL {
	public static final int TOP = 0;

	public static final int BOTTOM = 1;

	public static final int MIDDLE = 2;

	public static final int LEFT = 3;

	public static final int RIGHT = 4;

	public static final int CENTER = 5;

	protected IFieldDAO screenFieldDAO;
	protected FieldDAO fieldDAO;

	protected abstract ScreenFactory getScreenFactory();
    public AbstractFieldBL(){
		screenFieldDAO = getScreenFactory().getFieldDAO();
		fieldDAO = DAOFactory.getFactory().getFieldDAO();
	}
	/**
	 * Load a ScreenField having given pk
	 * @param pk
	 * @return
	 */
	public IField loadField(Integer pk){
		IField screenField=screenFieldDAO.loadByPrimaryKey(pk);
		return screenField;
	}


	public  static String getValignString(Integer va){
		if(va==null){
			return "middle";
		}
		int valign=va.intValue();
		switch(valign){
		case TOP:{
			return "top";
		}
		case BOTTOM:{
			return "bottom";
		}
		case MIDDLE:{
			return "middle";
		}
		}
		return "middle";
	}
	public  static String getHalignString(Integer ha){
		if(ha==null){
			return "center";
		}
		int halign=ha.intValue();
		switch(halign){
		case LEFT:{
			return "left";
		}
		case RIGHT:{
			return "right";
		}
		case CENTER:{
			return "center";
		}
		}
		return "center";
	}
}
