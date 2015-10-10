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

package com.aurel.track.admin.server.dbbackup;

import com.aurel.track.json.JSONUtility;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 */
public class BackupJSON {
	private BackupJSON(){
	}
	public static String  encodeJSONBackupList(List<BackupTO> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			File file;
			BackupTO backupTO;
			for (int i = 0; i < list.size(); i++) {
				backupTO=list.get(i);
				file=backupTO.getFile();
				if(i>0){
					sb.append(",");
				}
				sb.append("{");
				JSONUtility.appendStringValue(sb, "name", file.getName());
				JSONUtility.appendLongValue(sb,"length",file.length());

				JSONUtility.appendStringValue(sb, "systemVersion", backupTO.getSystemVersion());
				JSONUtility.appendStringValue(sb, "dbVersion", backupTO.getDbVersion());
				JSONUtility.appendStringValue(sb, "dbType", backupTO.getDbType());
				JSONUtility.appendBooleanValue(sb, "valid", backupTO.isValid());

				JSONUtility.appendDateTimeValue(sb,"lastModified",new Date(file.lastModified()),true);
				sb.append("}");
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
