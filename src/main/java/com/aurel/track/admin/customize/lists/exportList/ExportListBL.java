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

package com.aurel.track.admin.customize.lists.exportList;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.ListOptionIDTokens;
import com.aurel.track.beans.TListBean;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityExporter;
import com.aurel.track.configExchange.exporter.EntityExporterException;
import com.aurel.track.configExchange.impl.ListExporter;

public class ExportListBL {


	private static final Logger LOGGER = LogManager.getLogger(ExportListBL.class);

	/**
	 * Export the selected list
	 * @param parentList
	 * @return
	 */
	
	public static String exportList(TListBean parentList)throws EntityExporterException {
		ListExporter listExporter=new ListExporter();
		List<EntityContext> entityContextList=new ArrayList<EntityContext>();
		entityContextList.add(listExporter.createContext(parentList));
		String dom = EntityExporter.export2(entityContextList);
		return dom;
	}

	public static TListBean getListBean(String node) {
		ListOptionIDTokens listOptionIDTokens = ListBL.decodeNode(node);
		//get the ID of the node selected
		Integer currentListID = listOptionIDTokens.getChildListID();

		return ListBL.loadByPrimaryKey(currentListID);
	}

}
