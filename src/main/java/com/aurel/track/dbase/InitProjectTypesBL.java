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

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.configExchange.importer.EntityImporter;
import com.aurel.track.configExchange.importer.EntityImporterException;
import com.aurel.track.configExchange.importer.ImportContext;
import com.aurel.track.configExchange.importer.ImportResult;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.PluginUtils;

/**
 */
public class InitProjectTypesBL {
	private static final Logger LOGGER = LogManager.getLogger(InitProjectTypesBL.class);
	
	public static void addProjectTypes(String initDataDir) {
		File directory = null;
		directory = PluginUtils.getResourceFileFromWebAppRoot(ApplicationBean.getInstance().getServletContext(),
				initDataDir+"/ProjectTypes");
		if (directory !=null && directory.exists() && directory.isDirectory())
		{
			LOGGER.info("Retrieving project type templates from " + directory.toString());
			File[] files = directory.listFiles(new InitProjectTypesBL.Filter());			
			if (files == null || files.length == 0) {
				LOGGER.error("No project types files found.");
				return;
			}
			for (int index = 0; index < files.length; index++){
				File f=files[index];
				importProjectTypes(f);
			}
		}
	}

	public static String importProjectTypes(File uploadFile){
		LOGGER.info("Importing ProjectType from file:"+uploadFile.getName()+"...");
		try {
			ImportContext importContext=new ImportContext();
			importContext.setOverrideExisting(false);
			importContext.setOverrideOnlyNotModifiedByUser(false);
			EntityImporter entityImporter=new EntityImporter();
			List<ImportResult> importResultList=entityImporter.importFile(uploadFile, importContext);
		} catch (EntityImporterException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	static class Filter implements FileFilter{
		@Override
		public boolean accept(File file){
			return file.getName().endsWith(".xml");
		}
	}
}
