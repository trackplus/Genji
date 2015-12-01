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

import com.aurel.track.Constants;
import com.aurel.track.admin.server.logging.LoggingConfigBL;
import com.aurel.track.persist.BaseTSitePeer;
import com.aurel.track.prop.ApplicationBean;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.Torque;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;

public class DataReaderTest {

	private DataReaderTest(){
	}

	private static DatabaseInfo createFirebirdDbInfo(){
		String driver = "org.firebirdsql.jdbc.FBDriver";
		String url = "jdbc:firebirdsql://localhost/d:/dbs/TRACK507.GDB?charSet=UTF-8";
		String username = "sysdba";
		String password = null;
		return new DatabaseInfo(driver,url,username,password);
	}

	private static DatabaseInfo createSqlServerDbInfo(){
		String driver = "net.sourceforge.jtds.jdbc.Driver";
		String url = "jdbc:jtds:sqlserver://localhost/track503";
		String username = "sa";
		String password = null;
		return new DatabaseInfo(driver,url,username,password);
	}

	private static void exportSchema(File tmpDir,String dbaseDir){
		String[] databases=new String[]{
				MetaDataBL.DATABASE_DB2, MetaDataBL.DATABASE_DERBY,
				MetaDataBL.DATABASE_FIREBIRD,MetaDataBL.DATABASE_MS_SQL,
				MetaDataBL.DATABASE_MY_SQL,MetaDataBL.DATABASE_ORACLE,
				MetaDataBL.DATABASE_POSTGRES};
		String[] commentPrefixes=new String[]{
				MetaDataBL.COMMENT_PREFIX_DB2, MetaDataBL.COMMENT_PREFIX_DERBY,
				MetaDataBL.COMMENT_PREFIX_FIREBIRD,MetaDataBL.COMMENT_PREFIX_MS_SQL,
				MetaDataBL.COMMENT_PREFIX_MY_SQL,MetaDataBL.COMMENT_PREFIX_ORACLE,
				MetaDataBL.COMMENT_PREFIX_POSTGRES};

		Logger logger = LogManager.getLogger(DataReader.class);

		for(int i=0;i<databases.length;i++){
			String dbName=databases[i];
			String schemaFileName=dbaseDir+File.separator+dbName+File.separator+"track-schema.sql";
			String idTableFileName=dbaseDir+File.separator+dbName+File.separator+"id-table-schema.sql";
			String commentPrefix=commentPrefixes[i];
			InputStream is= null;
			InputStream isIdTable= null;
			try {
				is = new FileInputStream(schemaFileName);
				isIdTable = new FileInputStream(idTableFileName);
			} catch (FileNotFoundException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
			try {
				MetaDataBL.splitSchema(is,isIdTable, tmpDir.getAbsolutePath(),commentPrefix,dbName);
			} catch (DDLException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	public static void main(String[] args){
		DatabaseInfo databaseInfo=createFirebirdDbInfo();
		Logger LOGGER = LogManager.getLogger(DataReader.class);
		LoggingConfigBL.setLevel(LOGGER, Level.DEBUG);

		String dbaseDir="d:\\svn2015\\core\\com.trackplus.core\\src\\main\\webapp\\dbase";
		String tmpDir="d:\\tmp7\\7";
		File file=new File(tmpDir);
		if(!file.exists()){
			file.mkdirs();
		}
		exportSchema(new File(tmpDir),dbaseDir);

		try {
			DataReader.writeDataToSql(databaseInfo,tmpDir);
		} catch (DDLException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
}
