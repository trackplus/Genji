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

package com.trackplus.ddl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class DataWriter {
	private static final Logger LOGGER = LogManager.getLogger(DataWriter.class);

	private DataWriter(){
	}

	public static void writeDataToDB(DatabaseInfo databaseInfo,String dirName) throws DDLException{

		String fileName=dirName+File.separator+DataReader.FILE_NAME_DATA;
		LOGGER.info("Importing data from file \" "+fileName+"\" to DB \""+databaseInfo.getUrl()+"\" ...");
		Date d1=new Date();

		Properties info= DataWriter.readProperties(dirName+File.separator+DataReader.FILE_NAME_INFO);
		String sourceDriver=info.getProperty("driver");
		String sourceUrl=info.getProperty("url");
		String allData=info.getProperty("allData");

		String versionTrack= info.getProperty("versionTrack");
		String versionDB= info.getProperty("versionDB");


		LOGGER.debug("versionTrack="+versionTrack);
		LOGGER.debug("versionDB="+versionDB);
		LOGGER.debug("sourceDriver="+sourceDriver);
		LOGGER.debug("sourceUrl="+sourceUrl);
		LOGGER.info("All data to import: "+allData);

		Connection connection = getConnection(databaseInfo);

		int idx=0;
		idx= executeScript(fileName, connection);
		LOGGER.info("Records imported:"+idx+"\n");
		String fileNameUpdate=dirName+File.separator+DataReader.FILE_NAME_DATA_UPDATE;

		LOGGER.info("Execute updates...");
		String databaseTypeTarget=MetaDataBL.getDatabaseType(databaseInfo.getUrl());
		String databaseTypeSource=MetaDataBL.getDatabaseType(sourceUrl);
		int countUpdate=0;
		boolean sameDatabaseType=databaseTypeSource.equals(databaseTypeTarget);
		if(sameDatabaseType){
			LOGGER.info("Same database type");
			String endOfStatement=";"+DataReader.LINE_SEPARATOR;
			countUpdate=executeScript(fileNameUpdate,connection,endOfStatement);
		}else{
			countUpdate=executeUpdateScript(fileNameUpdate,connection);
		}
		LOGGER.info(countUpdate+" updates executed");


		LOGGER.info("Importing blob data ....");
		int count=importBlobScript(dirName+File.separator+DataReader.FILE_NAME_BLOB,connection);
		LOGGER.info("Blob record imported:"+count+"\n");
		idx=idx+count;

		try {
			connection.close();
		} catch (SQLException e) {
			throw new DDLException(e.getMessage(),e);
		}

		Date d2=new Date();

		LOGGER.info("Data imported! Insert executed "+idx+". Time spend: "+(d2.getTime()-d1.getTime())+" ms!");
	}

	private static Connection getConnection(DatabaseInfo databaseInfo) throws DDLException {
		try {
			Class.forName(databaseInfo.getDriver());
		} catch (ClassNotFoundException e) {
			throw new DDLException(e.getMessage(),e);
		}

		Connection connection;
		try {
			connection = DriverManager.getConnection(databaseInfo.getUrl(), databaseInfo.getUser(), databaseInfo.getPassword());
		} catch (SQLException e) {
			throw new DDLException(e.getMessage(),e);
		}
		return connection;
	}

	private static int executeScript(String fileName, Connection con) throws DDLException{
		String endOfStatement=";"+DataReader.LINE_SEPARATOR;
		return executeScript(fileName, con,endOfStatement);
	}

	public static int executeScript(String fileName, DatabaseInfo databaseInfo) throws DDLException{
		Connection connection = getConnection(databaseInfo);
		int result;
		try{
			result = executeScript(fileName, connection, ";");
		}catch (Exception e){
			throw new DDLException(e.getMessage(),e);
		}
		finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new DDLException(e.getMessage(),e);
			}
		}
		return result;
	}
	private static int executeScript(String fileName, Connection con,String endOfStatement) throws DDLException{
		BufferedReader bufferedReader =createBufferedReader(fileName);

		String line;
		StringBuilder sql=new StringBuilder();

		Statement stmt = MetaDataBL.createStatement(con);

		int idx=0;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				sql.append(line);
				if(line.endsWith(endOfStatement)){
					String s=sql.substring(0, sql.length() - endOfStatement.length());
					idx++;
					if (idx%5000 == 0) {
						LOGGER.info(idx + " inserts executed...");
					}
					executeUpdate(con,stmt, s);
					sql.setLength(0);
				}else{
					sql.append("\n");
				}
			}
		} catch (IOException e) {
			throw new DDLException(e.getMessage(),e);
		}

		//close file
		try{
			bufferedReader.close();
		}catch (IOException e){
			throw new DDLException(e.getMessage(),e);
		}

		//close connection
		try{
			stmt.close();
			con.commit();
			con.setAutoCommit(true);
		}catch (SQLException e){
			throw new DDLException(e.getMessage(),e);
		}

		return idx;
	}


	private static void executeUpdate(Connection con, Statement stmt, String s) throws DDLException{
		try {
			stmt.executeUpdate(s);
		} catch (SQLException e) {
			LOGGER.error("Error execute script line:"+e.getMessage());
			LOGGER.warn("-------------\n\n");
			LOGGER.warn(s);
			LOGGER.warn("-------------\n\n");
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			try{
				stmt.close();
				con.rollback();
				con.setAutoCommit(true);
			}catch (SQLException ex){
				throw new DDLException(e.getMessage(),e);
			}
			throw new DDLException(e.getMessage(),e);
		}
	}


	private static int executeUpdateScript(String fileName, Connection con) throws DDLException{
		String endOfStatement=";"+DataReader.LINE_SEPARATOR;

		BufferedReader bufferedReader = createBufferedReader(fileName);
		String line;
		StringBuilder sql=new StringBuilder();

		Statement stmt = MetaDataBL.createStatement(con);

		int idx=0;
		Map<String,Integer> columnSizeMap=new HashMap<String,Integer>();
		try {
			while ((line = bufferedReader.readLine()) != null) {
				sql.append(line);
				if(line.endsWith(endOfStatement)){
					String s=sql.substring(0, sql.length() - endOfStatement.length() + 1);
					Integer maxSize = getMaxColumnSize(con, columnSizeMap, s);
					int idxStart=s.indexOf("'");
					int idxEnd=s.lastIndexOf("'");
					String value=s.substring(idxStart+1,idxEnd);
					if(value.length()>maxSize){
						LOGGER.warn("Value bigger found for:"+s.substring(0,idxStart)+"..." +s.substring(idxEnd+1));
						value=value.substring(0,maxSize-1);
						s=s.substring(0,idxStart+1)+value+s.substring(idxEnd,s.length());
					}
					idx++;
					executeUpdate(con,stmt,s);
					sql.setLength(0);
				}else{
					sql.append("\n");
				}
			}
		} catch (IOException e) {
			throw new DDLException(e.getMessage(),e);
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			throw new DDLException(e.getMessage(),e);
		}
		try {
			stmt.close();
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			throw new DDLException(e.getMessage(),e);
		}
		return idx;
	}

	private static Integer getMaxColumnSize(Connection con, Map<String, Integer> columnSizeMap, String s) throws DDLException{
		////UPDATE <tableName> SET <column>='...'
		StringTokenizer st=new StringTokenizer(s.substring(0,s.indexOf("=")));
		st.nextToken();//UPDATE
		String tableName=st.nextToken();
		st.nextToken();//SET
		String columnName=st.nextToken();
		String key=tableName+"_"+columnName;
		Integer maxSize=columnSizeMap.get(key);
		if(maxSize==null){
			maxSize= MetaDataBL.getColumnSize(con, tableName, columnName);
			columnSizeMap.put(key,maxSize);
			LOGGER.debug("MAX size for column "+columnName+" in table "+tableName+" is "+maxSize);
		}
		return maxSize;
	}

	public static BufferedReader createBufferedReader(String fileName) throws DDLException {
		InputStreamReader inputStreamReader;
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(fileName), "UTF8");
		} catch (FileNotFoundException e) {
			throw new DDLException(e.getMessage(),e);
		} catch (UnsupportedEncodingException e) {
			throw new DDLException(e.getMessage(),e);
		}

		return new BufferedReader(inputStreamReader);
	}


	private static int importBlobScript(String fileName,Connection con) throws DDLException{
		BufferedReader bufferedReader = createBufferedReader(fileName);
		String line;
		int idx=0;

		String endDataChar=String.valueOf((char)DataReader.ASCII_DATA_SEPARATOR);
		boolean clobData=false;
		try{
			while ((line = bufferedReader.readLine()) != null) {
				if(line.startsWith("--")||line.length()==0){
					continue;
				}
				if(!clobData&&(line.length()==1||line.equals(endDataChar))){
					clobData=true;
					continue;
				}
				if(clobData){
					insertClobData(con,line);
				}else{
					insertBlobData(con, line);
				}
				idx++;
			}
		}catch (IOException e){
			throw  new DDLException(e.getMessage(),e);
		}
		try{
			bufferedReader.close();
		}catch (IOException e){
			throw  new DDLException(e.getMessage(),e);
		}
		return idx;
	}

	private static void insertBlobData(Connection con, String line) throws DDLException {
		String sql = "INSERT INTO TBLOB(OBJECTID, BLOBVALUE, TPUUID) VALUES(?,?,?)";
		StringTokenizer st=new StringTokenizer(line,",");
		Integer objectID=Integer.valueOf(st.nextToken());
		String base64Str=st.nextToken();
		byte[] bytes= Base64.decodeBase64(base64Str);
		String tpuid=null;
		if(st.hasMoreTokens()){
			tpuid=st.nextToken();
		}

		try{
			PreparedStatement preparedStatement =con.prepareStatement(sql);
			preparedStatement.setInt(1, objectID);
			preparedStatement.setBinaryStream(2,new ByteArrayInputStream(bytes),bytes.length);
			preparedStatement.setString(3, tpuid);
			preparedStatement.executeUpdate();
		}catch (SQLException e){
			throw  new DDLException(e.getMessage(),e);
		}
	}
	private static void insertClobData(Connection con, String line) throws DDLException {
		/*
		"OBJECTID",//Integer not null
		"EXCHANGEDIRECTION",//Integer not null
		"ENTITYID",//Integer not null
		"ENTITYTYPE",//Integer not null
		"FILENAME",//Varchar(255)
		"CHANGEDBY",//Integer
		"LASTEDIT",//Timestamp
		"TPUUID",//Varchar(36)
		"FILECONTENT"//Blob sub_type 1
		 */

		String sql = "INSERT INTO TMSPROJECTEXCHANGE(OBJECTID, EXCHANGEDIRECTION, ENTITYID,ENTITYTYPE,FILENAME,CHANGEDBY,LASTEDIT,TPUUID,FILECONTENT) "+
				"VALUES(?,?,?,?,?,?,?,?,?)";
		StringTokenizer st=new StringTokenizer(line,",");
		Integer objectID=Integer.valueOf(st.nextToken());
		Integer exchangeDirection=Integer.valueOf(st.nextToken());
		Integer entityID=Integer.valueOf(st.nextToken());
		Integer entityType=Integer.valueOf(st.nextToken());
		String fileName=st.nextToken();
		if("null".equalsIgnoreCase( fileName)){
			fileName=null;
		}
		Integer changedBy=null;
		try{
			changedBy=Integer.valueOf(st.nextToken());
		}catch (Exception ex){
			LOGGER.debug(ex);
		}

		Timestamp lastEdit=null;
		String lastEditStr=st.nextToken();
		if(lastEditStr!=null){
			try{
				lastEdit=Timestamp.valueOf(lastEditStr);
			}catch (Exception ex){
				LOGGER.debug(ex);
			}
		}
		String tpuid=st.nextToken();
		String base64Str=st.nextToken();
		if(base64Str.length()==1&&" ".equals(base64Str)){
			base64Str="";
		}
		byte[] bytes= Base64.decodeBase64(base64Str);
		String fileContent=new String(bytes);

		try{
			PreparedStatement preparedStatement =con.prepareStatement(sql);

			preparedStatement.setInt(1, objectID);
			preparedStatement.setInt(2, exchangeDirection);
			preparedStatement.setInt(3, entityID);
			preparedStatement.setInt(4, entityType);
			preparedStatement.setString(5, fileName);
			preparedStatement.setInt(6, changedBy);
			preparedStatement.setTimestamp(7, lastEdit);
			preparedStatement.setString(8, tpuid);
			preparedStatement.setString(9,fileContent);

			preparedStatement.executeUpdate();
		}catch (SQLException e){
			throw  new DDLException(e.getMessage(),e);
		}

	}


	public static Properties readProperties(String fileName){
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(fileName);
			// load a properties file
			prop.load(input);
		} catch (IOException ex) {
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return prop;
	}
}
