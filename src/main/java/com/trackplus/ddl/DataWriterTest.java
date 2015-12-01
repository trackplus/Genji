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

package com.trackplus.ddl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.aurel.track.admin.server.logging.LoggingConfigBL;

import org.apache.logging.log4j.LogManager;

import java.io.File;

public class DataWriterTest {

	private DataWriterTest(){
	}

	private static DatabaseInfo createFirebirdDbInfo(){
		String driver = "org.firebirdsql.jdbc.FBDriver";
		String url = "jdbc:firebirdsql://localhost/d:/dbs/TRACK511.GDB?charSet=UTF-8";
		String username = "sysdba";
		String password = null;
		return new DatabaseInfo(driver,url,username,password);
	}

	private static DatabaseInfo createSqlServerDbInfo(){
		String driver = "net.sourceforge.jtds.jdbc.Driver";
		String url = "jdbc:jtds:sqlserver://localhost/track511";
		String username = "sa";
		String password = null;
		return new DatabaseInfo(driver,url,username,password);
	}

	private static DatabaseInfo createMySqlDbInfo(){
		String driver = "org.gjt.mm.mysql.Driver";
		String url = "jdbc:mysql://localhost:3306/track400e?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
		String username = "root";
		String password = null;
		return new DatabaseInfo(driver,url,username,password);
	}

	public static void main(String[] args){

		String dirName="d:\\tmp7\\7";
		String dbType=MetaDataBL.DATABASE_FIREBIRD;

		DatabaseInfo databaseInfo=createFirebirdDbInfo();
		Logger LOGGER = LogManager.getLogger(DataWriter.class);
		LoggingConfigBL.setLevel(LOGGER, Level.DEBUG);
		boolean emptyDB=false;
		try{
			emptyDB=MetaDataBL.checkEmptyDatabase(databaseInfo);
		}catch (DDLException e){
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		if(!emptyDB){
			LOGGER.error("Target database is not empty!");
		}else{
			String fileSchema=dirName+ File.separator+dbType+"_schema.sql";
			LOGGER.info("Importing schema DB...");
			try {
				DataWriter.executeScript(fileSchema, databaseInfo);
				LOGGER.info("Importing schema DB successfuly!");
			} catch (DDLException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}

			LOGGER.info("Importing data...");
			try {
				DataWriter.writeDataToDB(databaseInfo,dirName);
				LOGGER.info("Importing data successfuly!");
			} catch (DDLException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}

			LOGGER.info("Importing constraints...");
			String fileSchemaConstraints=dirName+File.separator+dbType+"_schema_constraints.sql";
			try {
				DataWriter.executeScript(fileSchemaConstraints, databaseInfo);
				LOGGER.info("Importing constraints successfuly!");
			} catch (DDLException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}
		System.out.println("Ready!");
	}
}
