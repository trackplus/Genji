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

package com.aurel.track.admin.customize.category.report;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryJSON;
import com.aurel.track.admin.customize.category.CategoryTokens;
import com.aurel.track.beans.TExportTemplateBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Class for report utility methods
 * @author Tamas
 *
 */
public class ReportConfigBL {
	
	/**
	 * Saves a new or existing report
	 * @param node
	 * @param label
	 * @param typeFlag
	 * @param add
	 * @param description
	 * @param customListType
	 * @param cascadingType
	 * @param repositoryType
	 * @param projectID
	 * @param personID
	 * @param locale
	 */
	static String save(ReportFacade reportFacade, String nodeID, String label, File reportFile, String reportFileFileName, boolean add,
			Integer personID, Locale locale) throws IOException {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(nodeID);
		TExportTemplateBean exportTemplateBean =  (TExportTemplateBean)reportFacade.prepareBeanAfterAddEdit(
				categoryTokens, label, personID, add);
		ZipFile zipFile = null;
		if (add) {
			zipFile = new ZipFile(reportFile, ZipFile.OPEN_READ);
			ZipEntry zentryDescription=zipFile.getEntry("description.xml");
			if (zentryDescription == null) {
				zipFile.close();
				return JSONUtility.encodeJSONFailure(
						LocalizeUtil.getLocalizedTextFromApplicationResources("report.reportExportManager.err.descriptionMissing", locale));
			}
			Map<String, Object> descriptionMap = ReportBL.getTemplateDescription(zipFile.getInputStream(zentryDescription));
			exportTemplateBean.setReportType( (String)descriptionMap.get("type"));
			exportTemplateBean.setExportFormat((String)descriptionMap.get("format"));
			exportTemplateBean.setDescription(ReportBL.getLocalizedText(descriptionMap, "description", locale));
			
			if (label==null || "".equals(label)) {
				exportTemplateBean.setName((String)descriptionMap.get("name"));
			}
		}
		Integer reportID = ReportBL.saveReport(exportTemplateBean/*, reportFile, reportFileFileName*/);
		if (add) {
			saveTemplate(reportID, zipFile);
		}
		categoryTokens.setObjectID(reportID);
		categoryTokens.setType(CategoryBL.TYPE.LEAF);
		return CategoryJSON.createSaveResultJSON(true, CategoryTokens.encodeNode(categoryTokens), reportID);
		
	}
	
	/**
	 * Save the new template at given location(repository,project)
	 * @param repository
	 * @param personID
	 * @param project
	 * @param templateName
	 * @param uploadZip
	 * @return an error meassage if exist
	 */
	static String saveTemplate(Integer reportID, ZipFile zipFile)
			throws IOException {
		// specify buffer size for extraction
		final int BUFFER = 2048;
		File unzipDestinationDirectory = ReportBL.getDirTemplate(reportID);
		if (!unzipDestinationDirectory.exists()) {
			unzipDestinationDirectory.mkdirs();
		} else {
			return "report.reportExportManager.err.templateExists";
		}
		// Create an enumeration of the entries in the zip file
		Enumeration zipFileEntries = zipFile.entries();
		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(unzipDestinationDirectory, currentEntry);
			// grab file's parent directory structure
			File destinationParent = destFile.getParentFile();
			// create the parent directory structure if needed
			destinationParent.mkdirs();
			// extract file if not a directory
			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(zipFile
						.getInputStream(entry));
				int currentByte;
				// establish buffer for writing file
				byte data[] = new byte[BUFFER];
				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos,
						BUFFER);
				// read and write until last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();
			}
		}
		zipFile.close();
		return null;
	}
	
}
