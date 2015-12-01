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

package com.aurel.track.item.operation;

import java.util.HashMap;
import java.util.Map;

public class ItemOperationManager {
	private static Map<String,ItemOperation> map;

	public static void registerOperation(ItemOperation itemOperation){
		if(map==null){
			map=new HashMap<String, ItemOperation>();
		}
		map.put(itemOperation.getPluginID(),itemOperation);
	}
	public static ItemOperation  getOperation(String operationID){
		return map.get(operationID);
	}
}
