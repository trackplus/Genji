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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.attachment.AttachBL;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.dbase.JobScheduler;
import com.aurel.track.lucene.util.FileUtil;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.LabelValueBean;
import com.trackplus.ddl.DDLException;
import com.trackplus.ddl.DataReader;
import com.trackplus.ddl.DataWriter;
import com.trackplus.ddl.DatabaseInfo;
import com.trackplus.ddl.MetaDataBL;


public class DatabaseBackupBL {
	
	private static final Logger LOGGER = LogManager.getLogger(DatabaseBackupBL.class);
	
	private static final String DB_BACKUP_TMP_DIR = "dbBackupTmp";
	private static final String DB_RESTORE_TMP_DIR = "dbRestoreTmp";
	public static final String ATTACHMENTS_NAME = "Attachments";

	/**
	 * Hide public default constructor
	 */
	private DatabaseBackupBL() {
		
	}
	

	public static String formatBackupName(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
		return sdf.format(date);
	}
	
	public static void checkBackupNumber(int backupNumber) throws DatabaseBackupBLException{
		//Obtain the backup list file name sorted by lastModified.
		//The first element is the most recent 
		List<File>  backups= getBackupFiles();
		if(backups.size()>backupNumber){
			LOGGER.debug("Must Remove "+(backups.size()-backupNumber)+" backup files!");
			while(backups.size()>backupNumber){
				File backupFile=backups.get(backups.size()-1);
				try{
					backupFile.delete();
				}catch (Exception ex){
					LOGGER.error("Can't delete backup file:"+backupFile.getAbsolutePath());
					if(LOGGER.isDebugEnabled()){
						LOGGER.error(ExceptionUtils.getStackTrace(ex));
					}
				}
				backups.remove(backups.size()-1);
			}
		}
	}

	public static void zipDatabase(String backupName,boolean includeAttachments,PropertiesConfiguration tcfg) throws DatabaseBackupBLException{
		String driver=tcfg.getString("torque.dsfactory.track.connection.driver");
		if(driver!=null){
			driver=driver.trim();
		}
		String url=tcfg.getString("torque.dsfactory.track.connection.url");
		if(url!=null){
			url=url.trim();
		}
		String user=tcfg.getString("torque.dsfactory.track.connection.user");
		if(user!=null){
			user=user.trim();
		}
		String password=tcfg.getString("torque.dsfactory.track.connection.password");
		String backupRootDir=ApplicationBean.getInstance().getSiteBean().getBackupDir();
		String backupDirTMP=backupRootDir+File.separator+DB_BACKUP_TMP_DIR;
		
		File tmpDir=new File(backupDirTMP);
		FileUtil.deltree(tmpDir);
		tmpDir.mkdirs();
		
		DatabaseInfo databaseInfo=new DatabaseInfo(driver,url,user,password);

		try {
			DataReader.writeDataToSql(databaseInfo,tmpDir.getAbsolutePath());
		} catch (DDLException e) {
			e.printStackTrace();
		}
		String fileNameData=tmpDir.getAbsolutePath()+File.separator+DataReader.FILE_NAME_DATA;
		long size=0;
		try{
			File file=new File(fileNameData);
			size=file.length();
		}catch (Exception ex){
			LOGGER.error(ExceptionUtils.getStackTrace(ex),ex);
		}
		if(size<=1024){
			LOGGER.error("Can't write database data to file. The file size to small! FileName:"+fileNameData+",size="+size+" bytes.");
			DatabaseBackupBLException ex= new DatabaseBackupBLException("Can't write database data to file(File size to small:\""+fileNameData+"\")");
			ex.setLocalizedKey("admin.server.databaseBackup.err.cantWriteDatabaseDataToFile");
			ex.setLocalizedParams(new Object[]{fileNameData});
			throw  ex;
		}

		exportSchema(tmpDir);

		zipPart2(includeAttachments, backupName, tmpDir);

	}
	private static void exportSchema(File tmpDir){
		//dbase
		String dbasePath="/dbase/";
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


		for(int i=0;i<databases.length;i++){
			String dbName=databases[i];
			String fileNamePath=dbasePath+dbName+"/track-schema.sql";
			String fileNameIdTablePath=dbasePath+dbName+"/id-table-schema.sql";
			String commentPrefix=commentPrefixes[i];
			InputStream isIdTable = ApplicationBean.getInstance().getServletContext().getResourceAsStream(fileNameIdTablePath);
			InputStream is = ApplicationBean.getInstance().getServletContext().getResourceAsStream(fileNamePath);

			try {
				MetaDataBL.splitSchema(is,isIdTable, tmpDir.getAbsolutePath(),commentPrefix,dbName);
			} catch (DDLException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
	}
	
	private static void zipPart2(Boolean includeAttachments, String backupName, File tmpDir) throws DatabaseBackupBLException {
		if(includeAttachments){
			String attachDir=AttachBL.getAttachDirBase();
			String distDir=tmpDir.getAbsolutePath()+File.separator+ATTACHMENTS_NAME;
			copyAttachments(attachDir, distDir);
		}
		String backupFileName = backupName;
		if(!backupFileName.endsWith(".zip")){
			backupFileName=backupFileName+".zip";
		}
		String backupRootDirPath = ApplicationBean.getInstance().getSiteBean().getBackupDir();
		if (backupRootDirPath==null || "".equals(backupRootDirPath)) {
			LOGGER.error("No backup directory defined");
			DatabaseBackupBLException ex = new DatabaseBackupBLException("No backup directory defined");
			ex.setLocalizedKey("admin.server.databaseBackup.err.noBackupDirectory");
			//ex.setLocalizedParams(new Object[]{fbackup.getAbsolutePath()});
			throw ex;
		}
		File fbackupRootDir=new File(ApplicationBean.getInstance().getSiteBean().getBackupDir());
		if(!fbackupRootDir.exists()){
			fbackupRootDir.mkdirs();
		}
		File fbackup=new File(fbackupRootDir,backupFileName);
		ZipOutputStream zipOut=null;
		try {
			zipOut = new ZipOutputStream(new FileOutputStream(fbackup), Charset.forName("UTF-8"));
		} catch (FileNotFoundException e) {
			LOGGER.error("Can't find backup file:\""+fbackup.getAbsolutePath()+"\"");
			DatabaseBackupBLException ex= new DatabaseBackupBLException("Can't find backup file:\""+fbackup.getAbsolutePath()+"\"",e);
			ex.setLocalizedKey("admin.server.databaseBackup.err.cantFindBackupFile");
			ex.setLocalizedParams(new Object[]{fbackup.getAbsolutePath()});
			throw ex;
		}

		FileUtil.zipFiles(tmpDir, zipOut);
		
		try {
			zipOut.close();
			LOGGER.info("Backup created successfully at " + new Date());
		} catch (IOException e) {
			LOGGER.error("Can't close zip file: ");
			DatabaseBackupBLException ex= new DatabaseBackupBLException("Can't close zip file",e);
			ex.setLocalizedKey("admin.server.databaseBackup.err.cantCloseZipFile");
			ex.setLocalizedParams(new Object[]{fbackup.getAbsolutePath()});
			throw ex;
		}
		FileUtil.deltree(tmpDir);
	}
	
	public static void restoreDatabase(String backupName,String _driver,
			                           String _url,String _user,String password,String attachemntDir) throws DatabaseBackupBLException{
		File zipFile=new File(ApplicationBean.getInstance().getSiteBean().getBackupDir() +
				File.separator+backupName);
		String driver = _driver;
		String url = _url;
		String user = _user;
		if(driver!=null){
			driver=driver.trim();
		}
		if(url!=null){
			url=url.trim();
		}
		if(user!=null){
			user=user.trim();
		}
		restoreDatabase(zipFile, driver, url, user, password,attachemntDir);
	}
	
	private static void restoreDatabase(File zipFile,String driver,String url,String user,String password,String attachemntDir) throws DatabaseBackupBLException{
		LOGGER.debug("Restore database: zipFile:"+zipFile.getAbsoluteFile()+" url="+url);
		String restoreDir=HandleHome.getTrackplus_Home() + File.separator+HandleHome.DATA_DIR+File.separator+DB_RESTORE_TMP_DIR;
		File fdir=new File(restoreDir);
		FileUtil.deltree(fdir);
		fdir.mkdirs();
		try {
			FileUtil.unzipFile(zipFile, fdir);
		} catch (IOException e) {
			LOGGER.error("Error on unzip backup file:\""+zipFile.getAbsolutePath()+"\" in directory: "+fdir.getAbsolutePath(),e);
			DatabaseBackupBLException ex= new DatabaseBackupBLException("Error on unzip backup file:\""+zipFile.getAbsolutePath()+"\" in directory:"+fdir.getAbsolutePath(),e);
			ex.setLocalizedKey("admin.server.databaseBackup.err.cantUnZipFile");
			ex.setLocalizedParams(new Object[]{zipFile.getAbsolutePath()});
			throw ex;
		}

		String databaseName=MetaDataBL.getDatabaseType(url);
		LOGGER.debug("databaseName="+databaseName);
		String fileSchema=restoreDir+File.separator+databaseName+"_schema.sql";
		String fileSchemaConstraints=restoreDir+File.separator+databaseName+"_schema_constraints.sql";

		DatabaseInfo databaseInfo=new DatabaseInfo(driver,url,user,password);
		try{
			boolean emptyDB=MetaDataBL.checkEmptyDatabase(databaseInfo);
			if(!emptyDB){
				LOGGER.error("Target database is not empty!");
				throw new DatabaseBackupBLException("Target database is not empty!");
			}

		}catch (DDLException e){
			e.printStackTrace();
			throw new DatabaseBackupBLException("Can't create schema DB:"+e.getMessage(),e);
		}


		LOGGER.info("Importing schema DB...");
		try {
			DataWriter.executeScript(fileSchema, databaseInfo);
			LOGGER.info("Importing schema DB successfuly!");
		} catch (DDLException e) {
			e.printStackTrace();
			throw new DatabaseBackupBLException("Can't create schema DB:"+e.getMessage(),e);
		}

		LOGGER.info("Importing data...");
		try {
			DataWriter.writeDataToDB(databaseInfo,restoreDir);
			LOGGER.info("Importing data successfuly!");
		} catch (DDLException e) {
			e.printStackTrace();
			throw new DatabaseBackupBLException("Can't import data:"+e.getMessage(),e);
		}

		LOGGER.info("Importing constraints...");
		try {
			DataWriter.executeScript(fileSchemaConstraints, databaseInfo);
			LOGGER.info("Importing constraints successfuly!");
		} catch (DDLException e) {
			e.printStackTrace();
			throw new DatabaseBackupBLException("Can't import constraints:"+e.getMessage(),e);
		}


		if(attachemntDir!=null){
			LOGGER.info("Importing attachments...");
			File attachmentDirDist=new File(attachemntDir+File.separator+HandleHome.DATA_DIR+File.separator+AttachBL.AttachmentsName);
			if(!attachmentDirDist.exists()){
				attachmentDirDist.mkdirs();
			}
			String attach=restoreDir+File.separator+ATTACHMENTS_NAME;
			File fAttach=new File(attach);
			if(fAttach.exists()){
				FileUtil.copyDirectory(fAttach, attachmentDirDist);
			}
			LOGGER.info("Done importing attachments!");
		}
	}
	public static Properties getBackupInfo(File backupFile) throws DatabaseBackupBLException {
		Properties prop = new Properties();
		ZipFile zipFile = null;

		try {
			zipFile = new ZipFile(backupFile, ZipFile.OPEN_READ);
		} catch (IOException e) {
			throw new DatabaseBackupBLException(e.getMessage(),e);
		}

		ZipEntry zipEntryInfo=zipFile.getEntry(DataReader.FILE_NAME_INFO);
		if (zipEntryInfo == null) {
			try {
				zipFile.close();
			} catch (IOException e) {
				throw new DatabaseBackupBLException(e.getMessage(),e);
			}
			throw new DatabaseBackupBLException("Invalid backup. No file info");
		}

		try {
			prop.load(zipFile.getInputStream(zipEntryInfo));
		} catch (IOException e) {
			throw new DatabaseBackupBLException(e.getMessage(),e);
		}

		try {
			zipFile.close();
		} catch (IOException e) {
			throw new DatabaseBackupBLException(e.getMessage(),e);
		}
		return prop;
	}
	

	private static void copyAttachments(String attachDir,String distDir){
		FileUtil.copyDirectory(attachDir, distDir);
	}
	
	public static List<File> getBackupFiles() throws DatabaseBackupBLException{
		String backupDir=ApplicationBean.getInstance().getSiteBean().getBackupDir();
		if(backupDir==null){
			DatabaseBackupBLException ex= new DatabaseBackupBLException("Can't find backup directory:\"null\"");
			ex.setLocalizedKey("admin.server.databaseBackup.err.noBackupDirectory");
			throw  ex;
		}
		File f=new File(backupDir);
		if(!f.exists()){
			f.mkdirs();
			return new ArrayList<File>();
		}
		File[] files=f.listFiles();
		Arrays.sort(files, new Comparator(){
			public int compare(Object arg0, Object arg1) {
				long firstDate=((File)arg0).lastModified();
				long secondDate=((File)arg1).lastModified();
				return firstDate<secondDate ? 1 : (firstDate==secondDate ? 0 : -1);
			}
		});
		List<File> fileNames=new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			if(!files[i].isDirectory()){
				fileNames.add(files[i]);
			}
		}
		return fileNames;
	}

	public static List<BackupTO> getBackups() throws DatabaseBackupBLException{
		List<File> backupFiles=DatabaseBackupBL.getBackupFiles();
		List<BackupTO> backups=new ArrayList<BackupTO>();
		for (File f:backupFiles){
			BackupTO backupTO=new BackupTO();
			backupTO.setFile(f);
			try{
				Properties info=getBackupInfo(f);
				backupTO.setDbType(info.getProperty(DataReader.DATABASE_TYPE));
				backupTO.setSystemVersion(info.getProperty(DataReader.SYSTEM_VERSION));
				String dbVersion=info.getProperty(DataReader.DB_VERSION);
				backupTO.setDbVersion(dbVersion);
				backupTO.setValid(dbVersion!=null&&dbVersion.length()>0);
			}catch (DatabaseBackupBLException e){
				LOGGER.warn("Invalid backup file:"+f.getName()+" Error:"+e.getMessage());
				backupTO.setValid(false);
			}

			backups.add(backupTO);
		}
		return  backups;
	}
	
	public static List<LabelValueBean> checkConnection(String driverClassName, String url, String user, String password, Locale locale) {
		List<LabelValueBean> errors = new ArrayList<LabelValueBean>();
		Connection con=null;
		DatabaseInfo databaseInfo=new DatabaseInfo(driverClassName, url, user, password);
		try {
			con = DataReader.getConnection(databaseInfo);
		} catch (DDLException e) {
			addErrorMessage(locale, errors, e);
		}
		if(con!=null){
			boolean emptyDB= false;
			try {
				emptyDB = MetaDataBL.checkEmptyDatabase(con);
			} catch (DDLException e) {
				addErrorMessage(locale, errors, e);
			}
			if(!emptyDB){
				LOGGER.warn("The database:"+url+" is not empty");
				errors.add(new LabelValueBean("Database is not empty!","turl"));
			}
			try {
				con.close();
			} catch (SQLException e) {
				addErrorMessage(locale, errors, e);
			}
		}
		return errors;
	}

	private static void addErrorMessage(Locale locale, List<LabelValueBean> errors, Exception e) {
		String msg = e.getMessage();
		String msgu = msg.toUpperCase();
		errors.add(new LabelValueBean(getText("admin.server.databaseRestore.err.connectFailed", locale) + " " + msg,"turl"));
		if (msgu.contains("ACCESS") || msgu.contains("DENIED")) {
			errors.add(new LabelValueBean(getText("admin.server.databaseRestore.err.badPassword", locale) + " " + msg,"user"));
			errors.add(new LabelValueBean(getText("admin.server.databaseRestore.err.badPassword", locale) + " " + msg,"password"));
		}
		if (msgu.contains("DRIVER") || msgu.contains("CLASS")) {
			errors.add(new LabelValueBean(getText("admin.server.databaseRestore.err.connectFailed", locale) + " " + msg,"driverClassName"));
		}
		LOGGER.debug(e.getMessage(), e);
	}


	public static boolean setDBJobCronExpression(String backupTime,List<Integer> backupOnDays){
		StringBuilder cron = new StringBuilder();
		cron.append(getCronTime(backupTime) + " ? * " + getCronDays(backupOnDays));
		LOGGER.info("Configured new cron schedule for backup: "+ cron);
		return JobScheduler.setDBJobCronExpression(cron.toString());
	}

	/**
	 * Small routine to convert a time string to crontab format
	 * @param time
	 * @return
	 */
	private static String getCronTime(String time) {

		if (time == null || "".equals(time)){
			return "0 15 23";
		}
		String[] cronTime = {"0", "0", "0"};
		int i =  2;
		for (String value : time.split(":")) {
			cronTime[i] = value;
			--i;
			if (i < 0){
				break;
			}
		}

		return cronTime[0] + " " + cronTime[1] + " " +cronTime[2];
	}

	/**
	 * A little routine that creates the day of the week for crontab
	 * @param days
	 * @return
	 */
	private static String getCronDays(List<Integer> days){
		if (days == null || days.isEmpty()) {
			return "1";
		}
		else {
			StringBuilder buf = new StringBuilder();
			Integer cur;
			Iterator<Integer> it = days.iterator();
			while (it.hasNext()) {
				cur = it.next();
				buf.append(cur.toString());
				if (it.hasNext()) {
					buf.append(",");
				}
			}
			return buf.toString();
		}
	}

	private static String getText(String s, Locale locale){
		return LocalizeUtil.getLocalizedTextFromApplicationResources(s, locale);
	}
	
}
