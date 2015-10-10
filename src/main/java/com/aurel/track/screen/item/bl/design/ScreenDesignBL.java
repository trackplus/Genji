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

package com.aurel.track.screen.item.bl.design;

import java.util.List;

import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.design.AbstractScreenDesignBL;
import com.aurel.track.screen.item.adapterDAO.ItemScreenFactory;

public class ScreenDesignBL extends AbstractScreenDesignBL {
	//singleton instance
	private static ScreenDesignBL instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static ScreenDesignBL getInstance() {
		if (instance == null) {
			instance = new ScreenDesignBL();
		}
		return instance;
	}

	@Override
	protected ScreenFactory getScreenFactory() {
		return ItemScreenFactory.getInstance();
	}

	/**
	 * constructor
	 */
	public ScreenDesignBL() {
		super();
	}

	@Override
	public String encodeJSON_ScreenProperies(IScreen screen) {
		return new ItemScreenDesignJSON().encodeScreenProperties(screen);
	}
	public String encodeJSONScreenTO(IScreen screen) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb,"id",screen.getObjectID());
		JSONUtility.appendStringValue(sb,"name",screen.getName());
		JSONUtility.appendStringValue(sb,"tagLabel",screen.getTagLabel());
		JSONUtility.appendStringValue(sb,"description",screen.getDescription());
		JSONUtility.appendIntegerValue(sb,"ownerId",screen.getPersonID(),true);
		sb.append("}");
		return  sb.toString();
	}
	public String encodeJSONScreenList(List screens){
		StringBuilder sb=new StringBuilder();
		TScreenBean screen;
		sb.append("[");
		for (int i = 0; i < screens.size(); i++) {
			screen=(TScreenBean)screens.get(i);
			if(i>0){
				sb.append(",");
			}
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, "id", screen.getObjectID());
			JSONUtility.appendStringValue(sb,"name",screen.getName());
			JSONUtility.appendStringValue(sb,"tagLabel",screen.getTagLabel());
			JSONUtility.appendStringValue(sb,"description",screen.getDescription());
			JSONUtility.appendStringValue(sb,"owner",screen.getOwnerName(),true);
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}

}
