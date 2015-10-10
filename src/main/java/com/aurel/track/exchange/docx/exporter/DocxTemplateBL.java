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

package com.aurel.track.exchange.docx.exporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.dbase.HandleHome;
import com.aurel.track.util.DownloadUtil;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.PluginUtils;
/**
 * Helper class for uploading docx templates
 * @author Tamas
 *
 */
public class DocxTemplateBL {
	private static final Logger LOGGER = LogManager.getLogger(DocxTemplateBL.class);
	private static String DOCX_EXTENSION = "docx";
	private static String LATEX_EXTENSION = "tex";
	private static String LATEX_TEMPLATE_EXTENSION = "tlx";
	private static int BUFFER_SIZE = 8192;
	
	/**
	 * Gets the directory storing the word templates
	 * @return
	 */
	public static String getWordTemplatesDir() {
		return HandleHome.getTrackplus_Home() + File.separator + HandleHome.WORD_TEMPLATES_DIR + File.separator;
	}

	/**
	 * Gets the directory storing the word templates
	 * @return
	 */
	public static String getLatexTemplatesDir() {
		return HandleHome.getTrackplus_Home() + File.separator + HandleHome.LATEX_TEMPLATES_DIR + File.separator;
	}
	
	/**
	 * Get the JSON for rendering the selecting/uploading templates
	 * @param selectedTemplate
	 * @return
	 */
	static String renderSelectAndUploadTemplates(String selectedTemplate) {
		List<LabelValueBean> existingTemplates = DocxTemplateBL.getExistingTemplates();
		if (selectedTemplate==null && existingTemplates!=null && !existingTemplates.isEmpty()) {
			selectedTemplate = existingTemplates.get(0).getValue();
		} else {
			boolean found = false;
			if (existingTemplates!=null && !existingTemplates.isEmpty()) {
				for (LabelValueBean labelValueBean : existingTemplates) {
					if (selectedTemplate.equals(labelValueBean.getValue())) {
						found = true;
					}
				}
				if (!found) {
					selectedTemplate = existingTemplates.get(0).getValue();
				}
			}
		}
		return DocxTemplateJSON.createFileSelectJSON(true, existingTemplates, selectedTemplate);	
	}
	
	/**
	 * Gets the list of existing templates
	 * @return
	 */
	private static List<LabelValueBean> getExistingTemplates() {
		List<LabelValueBean> templates = new LinkedList<LabelValueBean>();
		
		templates = getExistingTemplates(getWordTemplatesDir());
		templates.addAll(getExistingTemplates(getLatexTemplatesDir()));
		
		return templates;
	}
	
	/**
	 * Gets the list of existing templates
	 * @return
	 */
	private static List<LabelValueBean> getExistingTemplates(String templateDir) {
		List<LabelValueBean> templates = new LinkedList<LabelValueBean>();
		
		String templateFilesPath = templateDir;
		
		File templateFilesDir = new File(templateFilesPath);
		if (templateFilesDir.exists()) {
			for (File file : templateFilesDir.listFiles()) {
				String fileName = file.getName();
				String extension = "";
				int i = fileName.lastIndexOf('.');
				if (i >= 0) {
				    extension = fileName.substring(i+1);
				    if (DOCX_EXTENSION.equals(extension) || LATEX_EXTENSION.equals(extension)
				    	|| LATEX_TEMPLATE_EXTENSION.equals(extension)) {
				    	templates.add(new LabelValueBean(fileName, fileName));
				    }
				    if (LATEX_TEMPLATE_EXTENSION.equals(extension)) {
				    	File dtdir = new File(file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(".tlx")));
				    	if (!dtdir.exists()) {
				    		PluginUtils.unzipFileIntoDirectory(file, file.getParentFile());
				    	}
				    }
				}
			}
		} else {
			templateFilesDir.mkdir();
		}
		return templates;
	}
	
	/**
	 * Download the template file
	 * @param iconBytes
	 * @param request
	 * @param response
	 * @param fileName
	 * @param inline
	 */
	public static void download(String fileName, HttpServletRequest request,
			HttpServletResponse response,  boolean inline) {
		String dir="";
		File file = null;
		
		if (fileName.endsWith(".docx")) {
			dir = getWordTemplatesDir();
			file = new File(dir + File.separator + fileName);
			DownloadUtil.prepareResponse(request, response, fileName,
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
					Long.toString(file.length()), inline);
		}
		
		if (fileName.endsWith(".tlx") || fileName.endsWith(".zip") ) {
			dir = getLatexTemplatesDir();
			DownloadUtil.prepareResponse(request, response, fileName,
					"application/zip");
			file = new File(dir + File.separator + fileName);
		}
		
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		try {
			// retrieve the file data
			inputStream = new FileInputStream(file); 
			outputStream = response.getOutputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = 0;
			while ((bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (FileNotFoundException fnfe) {
			LOGGER.error("FileNotFoundException thrown " + fnfe.getMessage(), fnfe);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error(ExceptionUtils.getStackTrace(fnfe));
			}
			return;
		} catch (Exception ioe) {
			LOGGER.error("Creating the input stream failed with  "
					+ ioe.getMessage(), ioe);
				LOGGER.debug(ExceptionUtils.getStackTrace(ioe));
			return;
		} finally {
			// flush and close the streams
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * Saves the uploaded template file on the disk
	 * @param uploadFile
	 * @param uploadFileName
	 */
	public static void upload(File uploadFile, String uploadFileName) {
		FileOutputStream fileOutputStream = null;
		FileInputStream fileInputStream = null;
		String dir = null;
		
		if (uploadFileName.endsWith(".docx")) {
			dir = getWordTemplatesDir();
		}
		
		if (uploadFileName.endsWith(".tlx") || uploadFileName.endsWith(".zip") ) {
			dir = getLatexTemplatesDir();
		}
		
		try {
			String templatePath = dir;
			File file = new File(templatePath);
			if (!file.exists()) {
				LOGGER.info("Path " + templatePath + " not found. Create it now...");
				boolean created = file.mkdirs();
				if (!created) {
					LOGGER.warn("Path " + templatePath + " not found and can't be created");
					return;
				}
			}
			fileOutputStream = new FileOutputStream(new File(templatePath + uploadFileName));
			fileInputStream = new FileInputStream(uploadFile);
			// retrieve the file data
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = 0;
			while ((bytesRead = fileInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
				// outputStream.write(buffer, 0, bytesRead);
				fileOutputStream.write(buffer, 0, bytesRead);
			}
			//blobBean.setBLOBValue(byteArrayOutputStream.toByteArray());
			//blobID = save(blobBean);
		} catch (FileNotFoundException fnfe) {
			LOGGER.error("Storing the iconFile on disk failed with FileNotFoundException", fnfe);
		} catch (IOException ioe) {
			LOGGER.error("Storing the attachment on disk failed: IOException", ioe);
		} finally {
			// flush and close the streams
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * Delete a template file
	 * @param fileName
	 */
	static void delete(String fileName) {
		String dir = null;
		
		if (fileName.endsWith(".docx")) {
			dir = getWordTemplatesDir();
		}
		
		if (fileName.endsWith(".tlx") || fileName.endsWith(".zip") ) {
			dir = getLatexTemplatesDir();
		}
		File file = new File(getWordTemplatesDir() + fileName);
		if (file.exists()) {
			file.delete();
		}
	}
}
