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

package com.aurel.track.screen.card.bl.design;

import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.design.AbstractScreenDesignBL;
import com.aurel.track.screen.card.adapterDAO.CardScreenFactory;

/**
 */
public class CardScreenDesignBL extends AbstractScreenDesignBL {
	//singleton instance
	private static CardScreenDesignBL instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static CardScreenDesignBL getInstance() {
		if (instance == null) {
			instance = new CardScreenDesignBL();
		}
		return instance;
	}

	@Override
	protected ScreenFactory getScreenFactory() {
		return CardScreenFactory.getInstance();
	}

	/**
	 * constructor
	 */
	public CardScreenDesignBL() {
		super();
	}
	public  String encodeJSON_ScreenProperies(IScreen screen){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, "id", screen.getObjectID());
		JSONUtility.appendStringValue(sb,"name",screen.getName());
		JSONUtility.appendStringValue(sb,"description",screen.getDescription());
		sb.append("}");
		return  sb.toString();
	}
}
