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

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.sql.*;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class DataReader {



	private static final Logger LOGGER = LogManager.getLogger(DataReader.class);

	public static final int ASCII_LINE_SEPARATOR =7;
	public static final int ASCII_DATA_SEPARATOR=5;


	public static final char LINE_SEPARATOR =(char)ASCII_LINE_SEPARATOR;

	public static final int MAX_VALUE_STRING=4000;

	public static final String FILE_NAME_DATA = "data.sql";
	public static final String FILE_NAME_INFO = "info.properties";
	public static final String FILE_NAME_DATA_UPDATE = "dataUpdate.sql";
	public static final String FILE_NAME_DATA_CLEAN = "dataClean.sql";
	public static final String FILE_NAME_DATA_UPDATE_CLEAN = "dataUpdateClean.sql";
	public static final String FILE_NAME_BLOB = "blob.dat";

	public static final String DATABASE_TYPE = "databaseType";
	public static final String SYSTEM_VERSION = "versionTrack";
	public static final String DB_VERSION = "versionDB";


	public DataReader(){
	}

	public static void writeDataToSql(DatabaseInfo databaseInfo,String dirName) throws DDLException{
		LOGGER.info("Exporting SQL data from \""+databaseInfo.getUrl()+"\" ...");
		Map<String,String> info=new TreeMap<String,String>();
		java.util.Date d1=new java.util.Date();
		info.put("start",d1.toString());
		info.put("driver",databaseInfo.getDriver());
		info.put("url",databaseInfo.getUrl());
		info.put("user",databaseInfo.getUser());
		info.put("user",databaseInfo.getUser());
		info.put("usePassword",Boolean.toString(databaseInfo.getPassword()!=null));
		String databaseType=MetaDataBL.getDatabaseType(databaseInfo.getUrl());
		info.put(DATABASE_TYPE,databaseType);

		Connection connection = getConnection(databaseInfo);
		//log the database meta data information's
		logDatabaseMetaDataInfo(databaseInfo, connection);

		String[] versions= MetaDataBL.getVersions(connection);
		info.put(SYSTEM_VERSION,versions[0]);
		info.put(DB_VERSION,versions[1]);

		StringValueConverter stringValueConverter=new GenericStringValueConverter();

		BufferedWriter writer = createBufferedWriter(dirName+File.separator+FILE_NAME_DATA);
		BufferedWriter writerUpdate = createBufferedWriter(dirName+File.separator+FILE_NAME_DATA_UPDATE);
		BufferedWriter writerClean = createBufferedWriter(dirName+File.separator+FILE_NAME_DATA_CLEAN);
		BufferedWriter writerUpdateClean = createBufferedWriter(dirName+File.separator+FILE_NAME_DATA_UPDATE_CLEAN);
		BufferedWriter writerBlob = createBufferedWriter(dirName+File.separator+FILE_NAME_BLOB);

		int idx=0;
		String[] tableNames=MetaDataBL.getTableNames();
		for(String tableName:tableNames){
			LOGGER.debug("Processing table: "+tableName+"....");
			int count=getTableData(writer,writerClean,writerUpdate,writerUpdateClean, connection,tableName,stringValueConverter);
			info.put("_"+tableName,count+"");
			LOGGER.debug("Records exported:"+count+"\n");
			idx=idx+count;
		}
		LOGGER.debug("Processing blob data ....");
		int count=getBlobTableData(writerBlob,connection);
		LOGGER.debug(" Blob record exported:"+count+"\n");
		info.put("table_BLOB",count+"");
		idx=idx+count;

		try{
			char dataSeparator=(char)ASCII_DATA_SEPARATOR;
			writerBlob.write(dataSeparator);
			writerBlob.newLine();
			writerBlob.newLine();
			writerBlob.write("--TMSPROJECTEXCHANGE");
			writerBlob.newLine();
		}catch (IOException e) {
			LOGGER.error("Error on close blob stream file :"+e.getMessage());
			throw new DDLException(e.getMessage(),e);
		}

		LOGGER.debug("Processing clob data ....");
		count=getClobTableData(writerBlob,connection);
		LOGGER.debug(" Clob record exported:"+count+"\n");
		info.put("table_TMSPROJECTEXCHANGE",count+"");
		idx=idx+count;
		info.put("allData",idx+"");

		try {
			writer.flush();
			writer.close();

			writerClean.flush();
			writerClean.close();

			writerUpdate.flush();
			writerUpdate.close();

			writerUpdateClean.flush();
			writerUpdateClean.close();

			writerBlob.flush();
			writerBlob.close();
		}catch (IOException e) {
			LOGGER.error("Error on close stream file: "+e.getMessage());
			throw new DDLException(e.getMessage(),e);
		}

		try {
			connection.close();
		} catch (SQLException e) {
			throw new DDLException(e.getMessage(),e);
		}

		java.util.Date d2=new java.util.Date();
		long timeSpend=d2.getTime()-d1.getTime();
		info.put("timeSpend",Long.toString(timeSpend));

		writeInfoToFile(info, dirName + File.separator + FILE_NAME_INFO);
		LOGGER.info("Data generated. All records found: "+idx+". Time spend: "+timeSpend+" ms!");
	}

	private static void logDatabaseMetaDataInfo(DatabaseInfo databaseInfo, Connection connection) throws DDLException {
		DatabaseMetaData databaseMetaData = null;
		try {
			databaseMetaData = connection.getMetaData();
			int    majorVersion   = databaseMetaData.getDatabaseMajorVersion();
			int    minorVersion   = databaseMetaData.getDatabaseMinorVersion();
			String productName    = databaseMetaData.getDatabaseProductName();
			String productVersion = databaseMetaData.getDatabaseProductVersion();
			int driverMajorVersion = databaseMetaData.getDriverMajorVersion();
			int driverMinorVersion = databaseMetaData.getDriverMinorVersion();

			LOGGER.debug("DB DRIVER="+databaseInfo.getDriver());
			LOGGER.debug("DB URL="+databaseInfo.getUrl());
			LOGGER.debug("DB USER="+databaseInfo.getUser());
			String password=databaseInfo.getPassword()==null?null:databaseInfo.getPassword().replaceAll(".","*");
			LOGGER.debug("DB PASSWORD="+password+"\n");

			LOGGER.debug("DB majorVersion="+majorVersion);
			LOGGER.debug("DB minorVersion="+minorVersion);
			LOGGER.debug("DB productName="+productName);
			LOGGER.debug("DB productVersion="+productVersion);
			LOGGER.debug("DB driverMajorVersion="+driverMajorVersion);
			LOGGER.debug("DB driverMinorVersion="+driverMinorVersion);
		}catch (SQLException e){
			throw new DDLException(e.getMessage(),e);
		}
	}

	public static Connection getConnection(DatabaseInfo databaseInfo) throws DDLException {
		try {
			Class.forName(databaseInfo.getDriver());
		} catch (ClassNotFoundException e) {
			throw new DDLException(e.getMessage(),e);
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(databaseInfo.getUrl(), databaseInfo.getUser(), databaseInfo.getPassword());
		} catch (SQLException e) {
			throw new DDLException(e.getMessage(),e);
		}
		return connection;
	}

	public static BufferedWriter createBufferedWriter(String fileName) throws DDLException {
		BufferedWriter writer = null;
		try {
			File file = new File(fileName);
			writer= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		} catch (Exception e) {
			LOGGER.error("Error on create/write file "+fileName+" :"+e.getMessage());
			throw new DDLException(e.getMessage(),e);
		}
		return writer;
	}

	private static void writeInfoToFile(Map<String,String> info,String fileName) throws DDLException{
		BufferedWriter writer = createBufferedWriter(fileName);
		Iterator<String> it=info.keySet().iterator();
		while(it.hasNext()){
			String key=it.next();
			String value=info.get(key);
			try{
				writer.write(key);
				writer.write("=");
				writer.write(value);
				writer.newLine();
			}catch (IOException ex){
				throw  new DDLException(ex.getMessage(),ex);
			}
		}

		try {
			writer.flush();
			writer.close();
		}catch (IOException e) {
			LOGGER.error("Error on close stream file "+fileName+" :"+e.getMessage());
			throw new DDLException(e.getMessage(),e);
		}
	}


	private static int getTableData(BufferedWriter writer,BufferedWriter writerClean,BufferedWriter writerUpdate,BufferedWriter writerUpdateClean,
									Connection con,String tableName,StringValueConverter stringValueConverter) throws DDLException {
		Statement st = MetaDataBL.createStatement(con);
		String sql="SELECT * FROM "+tableName;
		ResultSet rs;
		try {
			rs = st.executeQuery(sql);
		}catch (SQLException e){
			throw  new DDLException(e.getMessage(),e);
		}


		int idx=0;
		int idxUpdate=0;

		try{
			ResultSetMetaData md = rs.getMetaData();
			String primaryKey=MetaDataBL.getPrimaryKey(tableName, con);
			int columnCount = md.getColumnCount();
			StringBuilder sb=new StringBuilder();
			sb.append("INSERT INTO "+tableName+"(");
			for(int i=0;i<columnCount;i++){
				String columnName=md.getColumnName(i + 1);
				sb.append(columnName);
				if(i<columnCount-1){
					sb.append(", ");
				}
			}
			sb.append(") VALUES(");

			StringBuilder updateSQL;
			while (rs.next()){
				StringBuilder line=new StringBuilder();
				line.append(sb);
				String primaryKeyValue=rs.getString(primaryKey);
				for(int i=0;i<columnCount;i++){
					String value;
					String columnName=md.getColumnName(i+1);
					try{
					 	value=stringValueConverter.getStringValue(md,i+1,rs,tableName);
					}catch (DDLException ex){
						LOGGER.warn("Error: "+ex.getMessage()+" for column:"+columnName+" in table:"+tableName+" primary key "+primaryKey+"="+primaryKeyValue+". Will be set to NULL!");
						value=null;
					}
					if(value!=null&&value.length()>MAX_VALUE_STRING){

						if("MAILBODY".equalsIgnoreCase(columnName)||"CLOBVALUE".equalsIgnoreCase(columnName)){
							//blob
						}else{
							updateSQL=new StringBuilder();
							updateSQL.append("UPDATE ").append(tableName).append(" SET ").append(columnName).append("=");
							updateSQL.append(value).append("\n WHERE ").append(primaryKey).append("=").append(primaryKeyValue);
							updateSQL.append(";");

							MetaDataBL.appendLine(writerUpdateClean,updateSQL.toString());

							updateSQL.append(LINE_SEPARATOR);
							MetaDataBL.appendLine(writerUpdate,updateSQL.toString());

							idxUpdate++;
							value=null;
						}
					}
					line.append(value);
					if(i<columnCount-1){
						line.append(", ");
					}
				}
				line.append(");");

				MetaDataBL.appendLine(writerClean, line.toString());

				line.append(LINE_SEPARATOR);
				MetaDataBL.appendLine(writer, line.toString());

				idx++;
			}
		}catch (SQLException e){
			throw  new DDLException(e.getMessage(),e);
		}
		try {
			rs.close();
		} catch (SQLException e) {
			throw  new DDLException(e.getMessage(),e);
		}
		if(idxUpdate>0){
			LOGGER.warn("There was " + idxUpdate + " records with String size>" + MAX_VALUE_STRING + " found in table:" + tableName);
		}
		return idx;
	}


	private static int  getBlobTableData(BufferedWriter writer,Connection connection) throws DDLException {
		try{
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM TBLOB");
			int idx=0;
			while (rs.next()){
				StringBuilder line=new StringBuilder();

				//OBJECTID
				String value=rs.getString("OBJECTID");
				line.append(value).append(",");

				//BLOBVALUE
				Blob blobValue=rs.getBlob("BLOBVALUE");
				if(blobValue!=null){
					String str = new String(Base64.encodeBase64(blobValue.getBytes(1l, (int) blobValue.length())));
					if(str.length()==0){
						str=" ";
					}
					line.append(str);
				}else{
					line.append("null");
				}
				line.append(",");

				//TPUUID
				value=rs.getString("TPUUID");
				line.append(value);
				writer.write(line.toString());
				writer.newLine();
				idx++;
			}
			rs.close();
			return idx;
		}catch(SQLException ex){
			throw  new DDLException(ex.getMessage(),ex);
		}catch(IOException ex){
			throw  new DDLException(ex.getMessage(),ex);
		}
	}

	private static int getClobTableData(BufferedWriter writer,Connection connection) throws DDLException {
		Statement st = MetaDataBL.createStatement(connection);
		try{
			ResultSet rs = st.executeQuery("SELECT * FROM TMSPROJECTEXCHANGE");
			int idx=0;
			String[] columns=MetaDataBL.getColumnsMsProjectExchange();
			while (rs.next()){
				StringBuilder line=new StringBuilder();
				for(int i=0;i<columns.length;i++){
					String value=rs.getString(columns[i]);
					if(value!=null&&"FILECONTENT".equals(columns[i])){
						value = encodeBase64FileContent(value);
					}
					line.append(value);
					if(i<columns.length-1){
						line.append(",");
					}
				}
				MetaDataBL.appendLine(writer,line.toString());
				idx++;
			}
			rs.close();
			return idx;
		}catch(SQLException ex){
			throw  new DDLException(ex.getMessage(),ex);
		}
	}

	private static String encodeBase64FileContent(String value) {
		value= Base64.encodeBase64String(value.getBytes());
		if(value.length()==0){
			//empty value we replace with  space char, otherwise will not be precessed correctly to database restore
			value=" ";
		}
		return value;
	}


}
