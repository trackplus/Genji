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

package com.aurel.track.dbase;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.lists.ChildIssueTypeAssignmentsBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.beans.TChildIssueTypeBean;
import com.aurel.track.beans.TListTypeBean;

public class Migrate415To416 {

	private static final Logger LOGGER = LogManager.getLogger(Migrate415To416.class);
	
	
	/**
	 * Add the document and document section
	 * Use JDBC because negative objectIDs should be added
	 */
	public static void addNewItemTypes() {
		LOGGER.info("Add new item types");
		List<String> itemTypeStms = new ArrayList<String>();
		TListTypeBean docIssueTypeBean = IssueTypeBL.loadByPrimaryKey(-6);
		if (docIssueTypeBean==null) {
			LOGGER.info("Add 'Document folder' item type");
			itemTypeStms.add(addItemtype(-6, "Document folder", 6, -6, "documentFolder.png", "2009"));
		}
		
		Connection cono = null;
		try {
			cono = InitDatabase.getConnection();
			Statement ostmt = cono.createStatement();
			cono.setAutoCommit(false);
			for (String filterClobStmt : itemTypeStms) {
				ostmt.executeUpdate(filterClobStmt);
			}
			cono.commit();
			cono.setAutoCommit(true);
		} catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (cono != null) {
					cono.close();
				}
			} catch (Exception e) {
				LOGGER.info("Closing the connection failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		//children types
		List<TChildIssueTypeBean> childIssueTypeBeans = ChildIssueTypeAssignmentsBL.loadByChildAssignmentsByParent(-6);
		if (childIssueTypeBeans==null || childIssueTypeBeans.isEmpty()) {
			//document may have only document section children
			ChildIssueTypeAssignmentsBL.save(-6, -4);
		}
	}
	
	/**
	 * Add new item type
	 * @param objectID
	 * @param label
	 * @param typeflag
	 * @param sortOrder
	 * @param iconName
	 * @param UUID
	 * @return
	 */
	private static String addItemtype(int objectID, String label, int typeflag, int sortOrder, String iconName, String UUID) {
		return "INSERT INTO TCATEGORY (PKEY, LABEL, TYPEFLAG, SORTORDER, SYMBOL, TPUUID)"
				+ "VALUES (" + objectID + ", '" + label + "'," + typeflag + "," + sortOrder + ",'" + iconName + "','" + UUID +"')";
	}

	
}
