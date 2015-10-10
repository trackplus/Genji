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
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TChildIssueTypeBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.fieldType.constants.SystemFields;

public class Migrate411To412 {

	private static final Logger LOGGER = LogManager.getLogger(Migrate411To412.class);
	
	
	
	public static void addTaskIsMilestone() {
		TFieldBean taskIsMilestoneFieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_TASK_IS_MILESTONE);
		if (taskIsMilestoneFieldBean==null) {
			LOGGER.info("Adding task is milestone field");
			Connection cono = null;
			try {
				cono = InitDatabase.getConnection();
				Statement ostmt = cono.createStatement();
				cono.setAutoCommit(false);
				ostmt.executeUpdate(addField(SystemFields.INTEGER_TASK_IS_MILESTONE, "TaskIsMilestone", "com.aurel.track.fieldType.types.system.check.SystemTaskIsMilestone"));
				ostmt.executeUpdate(addFieldConfig(SystemFields.INTEGER_TASK_IS_MILESTONE, SystemFields.INTEGER_TASK_IS_MILESTONE, "Task is milestone", "Is milestone"));
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
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}
	
	/**
	 * Add new field
	 * @param objectID
	 * @param label
	 * @param typeflag
	 * @param sortOrder
	 * @param iconName
	 * @param UUID
	 * @return
	 */
	private static String addField(Integer objectID, String name, String fieldType) {
		return "INSERT INTO TFIELD (OBJECTID, NAME, FIELDTYPE, DEPRECATED, ISCUSTOM, REQUIRED)" + 
			"VALUES (" + objectID + ", '" + name + "', '" + fieldType + "', 'N', 'N', 'N')";
	}
	
	/**
	 * Adds a field config
	 * @param objectID
	 * @param fieldID
	 * @param label
	 * @param tooltip
	 * @return
	 */
	private static String addFieldConfig(Integer objectID, Integer fieldID, String label, String tooltip) {
		return "INSERT INTO TFIELDCONFIG(OBJECTID, FIELDKEY, LABEL, TOOLTIP, REQUIRED, HISTORY)" +
				"VALUES (" + objectID + ", " + fieldID + ", '" + label + "', '" + tooltip + "', 'N', 'N')";
	}
	
	/**
	 * Add the document and document section
	 * Use JDBC because negative objectIDs should be added
	 */
	public static void addNewItemTypes() {
		LOGGER.info("Add new item types");
		List<String> itemTypeStms = new ArrayList<String>();
		TListTypeBean docIssueTypeBean = IssueTypeBL.loadByPrimaryKey(-4);
		if (docIssueTypeBean==null) {
			LOGGER.info("Add 'Document' item type");
			itemTypeStms.add(addItemtype(-4, "Document", 4, -4, "document.png", "2007"));
		}
		TListTypeBean docSectionIssueTypeBean = IssueTypeBL.loadByPrimaryKey(-5);
		if (docSectionIssueTypeBean==null) {
			LOGGER.info("Add 'Document section' item type");
			itemTypeStms.add(addItemtype(-5, "Document section", 5, -5, "documentSection.png", "2008"));
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
		List<TChildIssueTypeBean> childIssueTypeBeans = ChildIssueTypeAssignmentsBL.loadByChildAssignmentsByParent(-4);
		if (childIssueTypeBeans==null || childIssueTypeBeans.isEmpty()) {
			//document may have only document section children
			ChildIssueTypeAssignmentsBL.save(-4, -5);
		}
		TListTypeBean actionItemIssueTypeBean = IssueTypeBL.loadByPrimaryKey(5);
		TListTypeBean meetingIssueTypeBean = IssueTypeBL.loadByPrimaryKey(-3);
		if (actionItemIssueTypeBean!=null && meetingIssueTypeBean!=null) {
			//action item exists
			childIssueTypeBeans = ChildIssueTypeAssignmentsBL.loadByChildAssignmentsByParent(-3);
			if (childIssueTypeBeans==null || childIssueTypeBeans.isEmpty()) {
				//meeting may have only action item children
				ChildIssueTypeAssignmentsBL.save(-3, 5);
			}
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
