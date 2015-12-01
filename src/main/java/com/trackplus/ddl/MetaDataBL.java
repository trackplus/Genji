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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetaDataBL {
	private static final Logger LOGGER = LogManager.getLogger(MetaDataBL.class);

	public static final String DATABASE_DB2="DB2";
	public static final String DATABASE_DERBY="Derby";
	public static final String DATABASE_FIREBIRD ="Firebird";
	public static final String DATABASE_MS_SQL="MSSQL";
	public static final String DATABASE_MY_SQL="MySQL";
	public static final String DATABASE_ORACLE ="Oracle";
	public static final String DATABASE_POSTGRES ="Postgres";


	public static final String COMMENT_PREFIX_DB2="--";
	public static final String COMMENT_PREFIX_DERBY="--";
	public static final String COMMENT_PREFIX_FIREBIRD ="--";
	public static final String COMMENT_PREFIX_MS_SQL="/*";
	public static final String COMMENT_PREFIX_MY_SQL="#";
	public static final String COMMENT_PREFIX_ORACLE ="--";
	public static final String COMMENT_PREFIX_POSTGRES ="--";


	private static final String[] TABLE_NAMES =new String[]{
			"CLUSTERNODE",
			"ID_TABLE",
			/*"QRTZ_BLOB_TRIGGERS",
			"QRTZ_CALENDARS",
			"QRTZ_CRON_TRIGGERS",
			"QRTZ_FIRED_TRIGGERS",
			"QRTZ_JOB_DETAILS",
			"QRTZ_JOB_LISTENERS",
			"QRTZ_LOCKS",
			"QRTZ_PAUSED_TRIGGER_GRPS",
			"QRTZ_SCHEDULER_STATE",
			"QRTZ_SIMPLE_TRIGGERS",
			"QRTZ_TRIGGERS",
			"QRTZ_TRIGGER_LISTENERS",*/
			"TACCOUNT",
			"TACL",
			"TACTION",
			"TACTUALESTIMATEDBUDGET",
			"TAPPLICATIONCONTEXT",
			"TATTACHMENT",
			"TATTACHMENTVERSION",
			"TATTRIBUTE",
			"TATTRIBUTECLASS",
			"TATTRIBUTEOPTION",
			"TATTRIBUTETYPE",
			"TATTRIBUTEVALUE",
			"TBASELINE",
			"TBASKET",
			//"TBLOB",
			"TBUDGET",
			"TCARDFIELD",
			"TCARDFIELDOPTION",
			"TCARDGROUPINGFIELD",
			"TCARDPANEL",
			"TCATEGORY",
			"TCHILDISSUETYPE",
			"TCHILDPROJECTTYPE",
			"TCLASS",
			"TCLOB",
			"TCOMPUTEDVALUES",
			"TCONFIGOPTIONSROLE",
			"TCOST",
			"TCOSTCENTER",
			"TDASHBOARDFIELD",
			"TDASHBOARDPANEL",
			"TDASHBOARDPARAMETER",
			"TDASHBOARDSCREEN",
			"TDASHBOARDTAB",
			"TDEPARTMENT",
			"TDISABLEFIELD",
			"TDOCSTATE",
			"TDOMAIN",
			"TEFFORTTYPE",
			"TEFFORTUNIT",
			"TEMAILPROCESSED",
			"TENTITYCHANGES",
			"TESCALATIONENTRY",
			"TESCALATIONSTATE",
			"TEVENT",
			"TEXPORTTEMPLATE",
			"TFIELD",
			"TFIELDCHANGE",
			"TFIELDCONFIG",
			"TFILTERCATEGORY",
			"TGENERALPARAM",
			"TGENERALSETTINGS",
			"TGLOBALCSSSTYLE",
			"TGRIDFIELD",
			"TGRIDGROUPINGSORTING",
			"TGRIDLAYOUT",
			"TGROUPMEMBER",
			"THISTORYTRANSACTION",
			"TINITSTATE",
			"TISSUEATTRIBUTEVALUE",
			"TITEMTRANSITION",
			"TLASTEXECUTEDQUERY",
			"TLASTVISITEDITEM",
			"TLINKTYPE",
			"TLIST",
			"TLOCALIZEDRESOURCES",
			"TLOGGEDINUSERS",
			"TLOGGINGLEVEL",
			"TMAILTEMPLATE",
			"TMAILTEMPLATECONFIG",
			"TMAILTEMPLATEDEF",
			"TMAILTEXTBLOCK",
			"TMENUITEMQUERY",
			"TMOTD",
			//"TMSPROJECTEXCHANGE",
			"TMSPROJECTTASK",
			"TNAVIGATORCOLUMN",
			"TNAVIGATORGROUPINGSORTING",
			"TNAVIGATORLAYOUT",
			"TNOTIFY",
			"TNOTIFYFIELD",
			"TNOTIFYSETTINGS",
			"TNOTIFYTRIGGER",
			"TOPTION",
			"TOPTIONSETTINGS",
			"TORGPROJECTSLA",
			"TOUTLINECODE",
			"TOUTLINETEMPLATE",
			"TOUTLINETEMPLATEDEF",
			"TPERSON",
			"TPERSONBASKET",
			"TPERSONINDOMAIN",
			"TPLISTTYPE",
			"TPPRIORITY",
			"TPRIORITY",
			"TPRIVATEREPORTREPOSITORY",
			"TPROJCAT",
			"TPROJECT",
			"TPROJECTACCOUNT",
			"TPROJECTREPORTREPOSITORY",
			"TPROJECTTYPE",
			"TPROLE",
			"TPSEVERITY",
			"TPSTATE",
			"TPUBLICREPORTREPOSITORY",
			"TQUERYREPOSITORY",
			"TREADISSUE",
			"TRECURRENCEPATTERN",
			"TRELEASE",
			"TREPORTCATEGORY",
			"TREPORTLAYOUT",
			"TREPORTPARAMETER",
			"TREPORTPERSONSETTINGS",
			"TREPORTSUBSCRIBE",
			"TREPOSITORY",
			"TREVISION",
			"TREVISIONWORKITEMS",
			"TROLE",
			"TROLEFIELD",
			"TROLELISTTYPE",
			"TSCHEDULER",
			"TSCREEN",
			"TSCREENCONFIG",
			"TSCREENFIELD",
			"TSCREENPANEL",
			"TSCREENTAB",
			"TSCRIPTS",
			"TSEVERITY",
			"TSHORTCUT",
			"TSITE",
			"TSLA",
			"TSTATE",
			"TSTATECHANGE",
			"TSUMMARYMAIL",
			"TSYSTEMSTATE",
			"TTEMPLATEPERSON",
			"TTEXTBOXSETTINGS",
			"TTRAIL",
			"TUSERFEATURE",
			"TUSERLEVEL",
			"TUSERLEVELSETTING",
			"TVERSIONCONTROL",
			"TVERSIONCONTROLPARAMETER",
			"TVIEWPARAM",
			"TWFACTIVITYCONTEXTPARAMS",
			"TWORKFLOW",
			"TWORKFLOWACTIVITY",
			"TWORKFLOWCATEGORY",
			"TWORKFLOWCOMMENT",
			"TWORKFLOWCONNECT",
			"TWORKFLOWDEF",
			"TWORKFLOWGUARD",
			"TWORKFLOWROLE",
			"TWORKFLOWSTATION",
			"TWORKFLOWTRANSITION",
			"TWORKITEM",
			"TWORKITEMLINK",
			"TWORKITEMLOCK"
	};

	private static final String [] COLUMNS_MS_PROJECT_EXCHANGE =new String[]{
		"OBJECTID",//Integer not null
		"EXCHANGEDIRECTION",//Integer not null
		"ENTITYID",//Integer not null
		"ENTITYTYPE",//Integer not null
		"FILENAME",//Varchar(255)
		"CHANGEDBY",//Integer
		"LASTEDIT",//Timestamp
		"TPUUID",//Varchar(36)
		"FILECONTENT"//Blob sub_type 1
	};

	private MetaDataBL(){
	}

	public static String[] getTableNames(){
		return TABLE_NAMES;
	}
	public static String[] getColumnsMsProjectExchange(){
		return COLUMNS_MS_PROJECT_EXCHANGE;
	}
	public static List<String> getTableNames(DatabaseMetaData databaseMetaData) throws SQLException {
		String   catalog          = null;
		String   schemaPattern    = null;
		String   tableNamePattern = null;
		String[] types            = null;

		ResultSet rs = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types );
		List<String> tableNamesList=new ArrayList<String>();
		while(rs.next()) {
			String tableName = rs.getString(3);
			tableNamesList.add(tableName);
		}
		rs.close();
		return tableNamesList;
	}

	private static List<ColumnInfo> getTableInfo(DatabaseMetaData databaseMetaData,String tableName) throws SQLException {
		ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, null);
		List<ColumnInfo> columnInfo=new ArrayList<ColumnInfo>();
		while (resultSet.next()) {
			String name = resultSet.getString("COLUMN_NAME");
			String type = resultSet.getString("TYPE_NAME");
			int size = resultSet.getInt("COLUMN_SIZE");
			columnInfo.add(new ColumnInfo(name,type,size));
		}
		return columnInfo;
	}

	public static void splitSchema(InputStream isSchema,InputStream isIdTable,String dirName,String commentPrefix,String dbName) throws DDLException{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(isSchema));
		BufferedReader bufferedReaderIdTable = new BufferedReader(new InputStreamReader(isIdTable));
		splitSchema(bufferedReader,bufferedReaderIdTable,dirName,commentPrefix,dbName);
	}
	private static void splitSchema(BufferedReader bufferedReader,BufferedReader bufferedReaderIdTable, String dirName,String commentPrefix,String dbName) throws DDLException{
		String line;
		StringBuilder sql=new StringBuilder();
		String endOfStatement=";";

		BufferedWriter writerSchema = DataReader.createBufferedWriter(dirName + File.separator + dbName + "_schema.sql");
		BufferedWriter writerSchemaConstraints = DataReader.createBufferedWriter(dirName+File.separator+dbName+"_schema_constraints.sql");

		//read idTable
		try{
			while ((line = bufferedReaderIdTable.readLine()) != null) {
				if(line.startsWith(commentPrefix)){
					continue;
				}
				sql.append(line);
				if(line.endsWith(endOfStatement)){
					String s=sql.substring(0, sql.length() - endOfStatement.length() + 1);
					appendLine(writerSchema,s);
					sql.setLength(0);
				}else{
					sql.append("\n");
				}
			}
		}catch (IOException e){
			throw new DDLException(e.getMessage(),e);
		}

		//close reader ID table
		try {
			bufferedReaderIdTable.close();
		} catch (IOException e) {
			throw new DDLException(e.getMessage(),e);
		}


		boolean isConstraints=false;
		BufferedWriter writer=writerSchema;

		try{
			while ((line = bufferedReader.readLine()) != null) {
				if(line.startsWith(commentPrefix)){
					continue;
				}
				//MySQL index
				sql.append(line);
				if(isConstraints==false){
					isConstraints=line.indexOf("ADD CONSTRAINT")!=-1;
				}
				if(line.endsWith(endOfStatement)){
					String s=sql.substring(0, sql.length() - endOfStatement.length() + 1);
					if(isConstraints){
						writer=writerSchemaConstraints;
					}
					if(line.indexOf("CREATE  INDEX")!=-1){
						appendLine(writerSchemaConstraints,s);
					}else{
						appendLine(writer,s);
					}
					sql.setLength(0);
				}else{
					sql.append("\n");
				}
			}
		}catch (IOException e){
			throw new DDLException(e.getMessage(),e);
		}

		try {
			bufferedReader.close();

			writerSchema.flush();
			writerSchema.close();

			writerSchemaConstraints.flush();
			writerSchemaConstraints.close();

		} catch (IOException e) {
			throw new DDLException(e.getMessage(),e);
		}

	}
	public static void appendLine(BufferedWriter writer,String line) throws DDLException{
		try{
			writer.write(line);
			writer.newLine();
		}catch (IOException e){
			throw new DDLException(e.getMessage(),e);
		}
	}



	public static  int getColumnSize(Connection conn, String tableName,String columName) throws  DDLException{
		int descSize = 32000;// Firebird
		int sizeMySQL = 16777215;
		try {
			Statement stmt = conn.createStatement();
			stmt.setMaxRows(2);
			String sql="SELECT "+columName+" FROM "+tableName;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs != null) {
				descSize = rs.getMetaData().getColumnDisplaySize(1);
				if (descSize <= 0) {
					// MySQL bug (MySql 5.0.26 for linux returns -1)
					System.err.println("Incorrect maximum description length retrieved: " + descSize);
					descSize = sizeMySQL;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Could not retrieve column sizes from database: " + e.getMessage());
			throw new DDLException(e.getMessage(),e);
		}
		return descSize;
	}
	public static boolean checkEmptyDatabase(DatabaseInfo databaseInfo) throws DDLException{
		Connection connection = DataReader.getConnection(databaseInfo);
		boolean b=checkEmptyDatabase(connection);
		try {
			connection.close();
		} catch (SQLException e){
			throw new DDLException(e.getMessage(),e);
		}
		return b;
	}
	public static boolean checkEmptyDatabase(Connection connection) throws DDLException{
		boolean result;

		DatabaseMetaData databaseMetaData = null;
		try {
			databaseMetaData = connection.getMetaData();
			List<String> dbTables=getTableNames(databaseMetaData);
			if(dbTables.isEmpty()){
				result=true;
			}else{
				result=!containsTable(dbTables,"TSITE");
			}
		}catch (SQLException e){
			throw new DDLException(e.getMessage(),e);
		}
		return result;
	}
	private static boolean containsTable(List<String> dbTables, String tableName){
		for(String s:dbTables){
			if(s.equalsIgnoreCase(tableName)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Create a statement and set auto-commit false
	 * @param con
	 * @return
	 * @throws DDLException
	 */
	public static Statement createStatement(Connection con) throws DDLException {
		Statement stmt = null;
		try {
			con.setAutoCommit(false);
			stmt = con.createStatement();
		} catch (SQLException e) {
			throw new DDLException(e.getMessage(),e);
		}
		return stmt;
	}

	public static String getPrimaryKey(String tableName,Connection connection) throws DDLException {
		String columnName=null;
		try{
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet rsMetaData = databaseMetaData.getPrimaryKeys(null, null, tableName);

			while (rsMetaData.next()) {
				columnName = rsMetaData.getString("COLUMN_NAME");
				break;
			}
		}catch (SQLException e){
			throw  new DDLException(e.getMessage(),e);
		}
		return columnName;
	}

	/**
	 * get version
	 * @param con
	 * @return an array : [<trackVersion>,<DBVersion>]
	 * @throws DDLException
	 */
	public static String[] getVersions(Connection con) throws DDLException {
		Statement st = MetaDataBL.createStatement(con);
		String sql="SELECT TRACKVERSION,DBVERSION FROM TSITE";
		String trackVersion=null;
		String dbVersion=null;
		try{
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				trackVersion = rs.getString(1);
				dbVersion = rs.getString(2);
				break;
			}
			rs.close();
		}catch (SQLException e){
			throw  new DDLException(e.getMessage(),e);
		}
		try {
			st.close();
		} catch (SQLException e) {
			throw  new DDLException(e.getMessage(),e);
		}
		return new String[]{trackVersion,dbVersion};
	}

	public static String getDatabaseType(String url){
		if(url.indexOf(":mysql:")!=-1){
			return MetaDataBL.DATABASE_MY_SQL;
		}
		if(url.indexOf(":oracle:")!=-1){
			return MetaDataBL.DATABASE_ORACLE;
		}
		if(url.indexOf(":sqlserver:")!=-1){
			return MetaDataBL.DATABASE_MS_SQL;
		}
		if(url.indexOf(":firebirdsql:")!=-1){
			return MetaDataBL.DATABASE_FIREBIRD;
		}
		if(url.indexOf(":postgresql:")!=-1){
			return MetaDataBL.DATABASE_POSTGRES;
		}
		if(url.indexOf(":db2:")!=-1){
			return MetaDataBL.DATABASE_DB2;
		}
		return MetaDataBL.DATABASE_FIREBIRD;
	}
}
