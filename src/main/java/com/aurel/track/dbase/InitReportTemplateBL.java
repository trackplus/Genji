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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.StartServlet;
import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.beans.TExportTemplateBean;
import com.aurel.track.util.PluginUtils;

/**
 * Used on init database to init/synchronize report templates
 */
public class InitReportTemplateBL {

	private static final Logger LOGGER = LogManager.getLogger(InitReportTemplateBL.class);

	public static void addReportTemplates() {
		URL urlSrc;
		File srcDir = null;
		//first try to get the template dir through class.getResource(path)
		urlSrc = PluginUtils.class.getResource("/resources/reportTemplates");
		urlSrc = PluginUtils.createValidFileURL(urlSrc);
		if (urlSrc!=null)
		{
			LOGGER.info("Retrieving report templates from " + urlSrc.toString());
			srcDir = new File(urlSrc.getPath());
			Long uuid = new Date().getTime();
			File tmpDir = new File(System.getProperty("java.io.tmpdir")+File.separator+"TrackTmp"+ uuid.toString());
			if (!tmpDir.isDirectory()) {
				tmpDir.mkdir();
			}
			tmpDir.deleteOnExit();

			File[] files = srcDir.listFiles(new InitReportTemplateBL.Filter());
			if (files == null || files.length == 0) {
				LOGGER.error("Problem unzipping report template: No files.");
				return;
			}
			for (int index = 0; index < files.length; index++)
			{
				ZipFile zipFile = null;
				try {
					String sname = files[index].getName();
					String oid = sname.substring(sname.length()-6,sname.length()-4);
					zipFile = new ZipFile(files[index], ZipFile.OPEN_READ);
					LOGGER.debug("Extracting template from " + files[index].getName());
					unzipFileIntoDirectory(zipFile, tmpDir);

					File descriptor = new File(tmpDir,"description.xml");
					InputStream in = new FileInputStream(descriptor);
					//parse using builder to get DOM representation of the XML file
					Map<String, Object> desc = ReportBL.getTemplateDescription(in);

					String rname = "The name";
					String description = "The description";
					String expfmt = (String) desc.get("format");



					Map<String,String> localizedStuff = (Map<String,String>)desc.get("locale_" + Locale.getDefault().getLanguage());
					if (localizedStuff != null) {
						rname = localizedStuff.get("listing");
						description = localizedStuff.get("description");
					} else {
						localizedStuff = (Map<String,String>)desc.get("locale_en");
						rname = localizedStuff.get("listing");
						description = localizedStuff.get("description");
					}

					addReportTemplateToDatabase(new Integer(oid), rname, expfmt, description);

				} catch (IOException e) {
					LOGGER.error("Problem unzipping report template " + files[index].getName());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}

	/**
	 * Extracts and copies the template definitions from the zips from the classpath
	 * to the directory parallel with the attachment directory in the disk
	 *
	 */
	public static void verifyReportTemplates(){
		LOGGER.info("Verifying report templates...");
		List<TExportTemplateBean> templatesList = ReportBL.getAllTemplates();
		
		int noOfDbTemplates = templatesList.size();
		int noOfNewlyInstalled = 0;
		
		for (int i = 0; i < templatesList.size(); i++) {
			TExportTemplateBean templateBean = templatesList.get(i);
			Integer templateID = templateBean.getObjectID();
			File f=ReportBL.getDirTemplate(templateID);
			LOGGER.debug("Move template:"+templateBean.getName()+" to"+f.getAbsolutePath());
			NumberFormat nf = new DecimalFormat("00000");
			String dbId = nf.format(templateID);
			String reportTemplateName=ReportBL.RESOURCE_PATH+"/"+ReportBL.EXPORT_TEMPLATE_PREFIX+dbId+".zip";
			InputStream is=StartServlet.class.getClassLoader().getResourceAsStream(reportTemplateName);
			if (is==null) {
				//zip not found in the classpath: nothing to expand and copy
				continue;
			}
			if (!f.exists()) {
				boolean pathCreated = f.mkdirs();
				if (!pathCreated) {
					LOGGER.error("The destination directory " + f.getAbsolutePath() + " can't be created. " +
							"This might be caused by OS file permissions or " +
							"because the default directory lies inside the tomcat application. " +
							"Set the attachment directory to a writable absolute path directory (outside tomcat) and restart tomcat");
					continue;
				}
			}
			ZipInputStream zf=new ZipInputStream(new BufferedInputStream(is));
			try {
				ReportBL.saveTemplate(templateID,zf);
				++noOfNewlyInstalled;
			} catch (IOException e) {
				LOGGER.error("Can't move template:"+templateBean.getName()+"!",e);
			}
		}
		LOGGER.debug("Verifying report templates ready!");
		LOGGER.info("There are " + noOfDbTemplates + " report templates in the database; " + noOfNewlyInstalled + " were newly installed.");
	}



	/* Adds the report template entry to the database. Does not add entries that have been previously
	* deleted. Adds just new entries in case of an existing installation.
			*/
	private static void addReportTemplateToDatabase(Integer oid, String name, String expfmt, String description) {

		String stmt = "INSERT INTO TEXPORTTEMPLATE (OBJECTID,NAME,EXPORTFORMAT,REPOSITORYTYPE,DESCRIPTION,PROJECT,PERSON,REPORTTYPE)"
				+ "VALUES ("+ oid + ",'" + name + "','" + expfmt + "',2,'" + description + "',NULL,1,'Jasper Report')";

		Connection coni = null;
		Connection cono = null;
		ResultSet rs = null;
		try {
			coni = InitDatabase.getConnection();
			cono = InitDatabase.getConnection();
			PreparedStatement istmt = coni.prepareStatement("SELECT MAX(OBJECTID) FROM TEXPORTTEMPLATE WHERE OBJECTID < 100");
			Statement ostmt = cono.createStatement();

			rs = istmt.executeQuery();
			Integer maxInt = 0;
			if (rs != null) {
				rs.next();
				maxInt = rs.getInt(1);
			}
			if (oid.intValue() <= maxInt.intValue()) {
				return;
			}
			
			istmt = coni.prepareStatement("SELECT * FROM TEXPORTTEMPLATE WHERE OBJECTID = ?");
			istmt.setInt(1, oid);
			
			rs = istmt.executeQuery();
			if (rs == null || !rs.next()) {
				LOGGER.info("Adding report template with OID " + oid + ": " + name);
				try {
					ostmt.executeUpdate(stmt);
				} catch (Exception exc) {
					LOGGER.error("Problem...: " + exc.getMessage());
				}
			}
		} catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) rs.close();
				if (coni != null) coni.close();
				if (cono != null) cono.close();
			} catch (Exception e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * Unzip a ZIP file into a directory
	 * @param zipFile
	 * @param dir
	 */
	private static void unzipFileIntoDirectory(ZipFile zipFile, File dir) {
		Enumeration files = zipFile.entries();
		File f = null;
		FileOutputStream fos = null;
		while (files.hasMoreElements()) {
			try {
				ZipEntry entry = (ZipEntry) files.nextElement();
				InputStream eis = zipFile.getInputStream(entry);
				byte[] buffer = new byte[1024];
				int bytesRead = 0;

				f = new File(dir.getAbsolutePath() + File.separator + entry.getName());

				if (entry.isDirectory()) {
					f.mkdirs();
					continue;
				} else {
					f.getParentFile().mkdirs();
					f.createNewFile();
				}

				fos = new FileOutputStream(f);

				while ((bytesRead = eis.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
				}
			} catch (IOException e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				continue;
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
		}
	}






	static class Filter implements FileFilter
	{
		@Override
		public boolean accept(File file)
		{
			return file.getName().startsWith("export");
		}
	}
}
