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
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkflowActivityBean;
import com.aurel.track.beans.TWorkflowGuardBean;
import com.aurel.track.beans.TWorkflowTransitionBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;

/**
 * Migration code from 417 to 500
 * @author Tamas
 *
 */
public class Migrate417To500 {
	private static final Logger LOGGER = LogManager.getLogger(Migrate417To500.class);
	
	static void migrateWorkflows() {
	}
	
	
	/**
	 * In some previous track+ version the type flag combo was disabled which lead to null values (nothing submitted) for type flags
	 * To correct this behavior the the null state flags to general
	 */
	static void setTypeFlagsForItemTypes() {
		List<TListTypeBean> itemTypes = IssueTypeBL.loadAll();
		if (itemTypes!=null) {
			for (TListTypeBean listTypeBean : itemTypes) {
				if (listTypeBean.getTypeflag()==null) {
					listTypeBean.setTypeflag(TListTypeBean.TYPEFLAGS.GENERAL);
					IssueTypeBL.saveSimple(listTypeBean);
				}
			}
		}
	}
	
	/**
	 * Add the wokflow user
	 * Use JDBC because negative objectIDs should be added
	 */
	static void addWorkflowPerson() {
	}
	
	/**
	 * Add deleted basket
	 * @param objectID
	 * @param lastName
	 * @param UUID
	 * @return
	 */
	private static String addWorkflowPersonStmt(int objectID, String firstName, String lastName, String loginName, String UUID) {
		return "INSERT INTO TPERSON (PKEY, FIRSTNAME, LASTNAME, LOGINNAME, EMAIL, PASSWD, SALT,  DELETED, " + " PREFLOCALE, TPUUID)"
				+ "VALUES (" + objectID + ",'" + firstName + "', '" + lastName + "', '" + loginName +"', '','', '', 'Y', '','"+UUID+"')";
	}
}
