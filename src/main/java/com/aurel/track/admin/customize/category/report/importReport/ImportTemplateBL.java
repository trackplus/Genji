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

package com.aurel.track.admin.customize.category.report.importReport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.CategoryBL.REPOSITORY_TYPE;
import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.admin.customize.category.report.exportReport.ExportTemplateParser;
import com.aurel.track.admin.customize.treeConfig.screen.importScreen.parser.ParserFactory;
import com.aurel.track.beans.TExportTemplateBean;

public class ImportTemplateBL {
	
	private static ExportTemplateParser exportTemplateParser = ParserFactory.getInstance().getExportTemplateParser();
	
	private static final Logger LOGGER = LogManager.getLogger(ImportTemplateBL.class);
	
	/**
	 * Perform the necessary changes on the TExportTemplateBean extracted from the xml
	 * Update the values which remain the same in the database
	 * @param dbOptionSettingsBean
	 * @param xmlOptionSettingsBean
	 * @return
	 */
	private static TExportTemplateBean getExportTemplateValues(TExportTemplateBean dbExportTemplateBean, 
								TExportTemplateBean xmlExportTemplateBean)
	{	
		String dbUuid = dbExportTemplateBean.getUuid();
		xmlExportTemplateBean.setUuid(dbUuid);
		xmlExportTemplateBean.setObjectID(dbExportTemplateBean.getObjectID());
		
		xmlExportTemplateBean.setNew(false);
		xmlExportTemplateBean.setModified(true);
		return xmlExportTemplateBean;
	}
	

	/**
	 * Update the TEXPORTTEMPLATE table
	 */
	public static void updateTExportTemplate(Integer loggedPerson, File file, boolean overwriteExisting) {
		Map<Integer,Integer> exportTemplateMap = new HashMap<Integer,Integer>();
		boolean isUpdated = false; 
		String fileName = null;
		//unzip the archive
		try
		{
			FileInputStream fin = new FileInputStream(file);
		    ZipInputStream zin = new ZipInputStream(fin);
		    ZipEntry ze = null;
		    while ((ze = zin.getNextEntry()) != null) {
		    	if (ze.getName().equals("exptemplate.xml")){
		    		fileName = ze.getName();
				    FileOutputStream fout = new FileOutputStream(ze.getName());
				    for (int c = zin.read(); c != -1; c = zin.read()) {
				    	fout.write(c);
				    }
				    fout.close();
		    	  }
		        zin.closeEntry(); 
		      }
		      zin.close();
		    if (fileName==null) {
		    	System.out.println("File not found");
		    	return;
		    }
		    File xmlFile = new File(fileName);
			ParserFactory.getInstance();
			ParserFactory.setFile(xmlFile);
			
			List<TExportTemplateBean> dbExportTemplate = ReportBL.getAllTemplates();
			List<TExportTemplateBean> xmlExportTemplate = exportTemplateParser.parse();
			//System.out.println("Size xml=" + Integer.toString(xmlExportTemplate.size()));
			Iterator itrXml = xmlExportTemplate.iterator();
			while (itrXml.hasNext())
			{
				isUpdated = false;
				TExportTemplateBean tmpXmlExportTemplateBean = (TExportTemplateBean)itrXml.next();
				Iterator itrDb = dbExportTemplate.iterator();
				while (itrDb.hasNext())
				{
					TExportTemplateBean tmpDbExportTemplateBean = (TExportTemplateBean)itrDb.next();
					//update database
					if (tmpXmlExportTemplateBean.considerAsSame(tmpDbExportTemplateBean)) {
						isUpdated = true;
						exportTemplateMap.put(tmpXmlExportTemplateBean.getObjectID(), tmpDbExportTemplateBean.getObjectID());
						tmpXmlExportTemplateBean = getExportTemplateValues(tmpDbExportTemplateBean, tmpXmlExportTemplateBean);
						tmpDbExportTemplateBean.saveBean(tmpXmlExportTemplateBean);
					}
				}
				//insert new item into database
				if (isUpdated == false)
				{
					tmpXmlExportTemplateBean.setPerson(loggedPerson);
					tmpXmlExportTemplateBean.setRepositoryType(REPOSITORY_TYPE.PUBLIC);
					tmpXmlExportTemplateBean.setNew(true);
					tmpXmlExportTemplateBean.saveBean(tmpXmlExportTemplateBean);	
				}	
			}
			if (xmlFile.exists())
				xmlFile.delete();
		}
		catch (FileNotFoundException e){
			LOGGER.error("File not found:" + e.getMessage(), e);
		}
		catch (Exception e)
		{
			LOGGER.error("Error:" + e.getMessage(), e);
		}

		
	}
}
	

