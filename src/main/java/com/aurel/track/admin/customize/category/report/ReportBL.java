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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aurel.track.admin.customize.category.report.execute.IDescriptionAttributes;
import com.aurel.track.beans.TExportTemplateBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ExportTemplateDAO;
import com.aurel.track.dbase.HandleHome;

/**
 * Report utility methods
 * @author Tamas
 *
 */
public class ReportBL {
	private static final Logger LOGGER = LogManager.getLogger(ReportBL.class);
	private static ExportTemplateDAO exportTemplateDAO = DAOFactory.getFactory().getExportTemplateDAO();
	public static final String RESOURCE_PATH="resources/reportTemplates";
	public static final String EXPORT_TEMPLATE_PREFIX="exportTemplate";
	
	/**
     * Gets a exportTemplateBean from the TExportTemplate table
     * @param objectID
     * @return
     */
	public static TExportTemplateBean loadByPrimaryKey(Integer objectID) {
		return exportTemplateDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Loads all templates between two indexes
	 * @return
	 */
	public static List<TExportTemplateBean> loadFromTo(Integer from, Integer to) {
		return exportTemplateDAO.loadFromTo(from, to);
	}
	
	/**
	 * Loads exportTemplateBeans by IDs
	 * @param reportIDs
	 * @return
	 */
	public static List<TExportTemplateBean> loadByIDs(List<Integer> reportIDs) {
		return exportTemplateDAO.loadByIDs(reportIDs);
	}
	
	/**
	 * Loads the templates derived from a parent template
	 * @param parentTemplateID
	 * @return
	 */
	public static List<TExportTemplateBean> loadDerived(Integer parentTemplateID) {
		return exportTemplateDAO.loadDerived(parentTemplateID);
	}
	
	/**
	 * Get all report templates
	 * @return
	 */
	public static List<TExportTemplateBean> getAllTemplates() {
		return exportTemplateDAO.loadAll();
	}
	
	/**
	 * Saves the filter after submit, before save
	 * @param repository
	 * @param objectID
	 * @param number
	 * @param personID
	 * @param add
	 */
	public static Integer saveReport(TExportTemplateBean exportTemplateBean/*, File reportFile, String reportFileFileName*/) {
		return exportTemplateDAO.save(exportTemplateBean);
	}
	
	/**
	 * Deletes a exportTemplate from the TExportTemplate table
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	public static void delete(Integer objectID) {
		exportTemplateDAO.delete(objectID);
	}
	
	public static File getDirTemplate(Integer objectID){
		if (objectID==null) {
			return null;
		}
		NumberFormat nf = new DecimalFormat("00000");
		String dbId = nf.format(objectID);
		File dir;
		if(HandleHome.getTrackplus_Home() ==null){
			dir= new File(EXPORT_TEMPLATE_PREFIX+dbId);
		}else{
			dir= new File(HandleHome.getTrackplus_Home() + File.separator
					+ HandleHome.REPORT_TEMPLATES_DIR + File.separator+EXPORT_TEMPLATE_PREFIX+dbId);
		}
		return dir;
	}
	
	/**
	 * Whether a configuration is needed if the datasource is hardcoded (the dashboard specific project/release)
	 * @param templateID
	 * @return
	 */
	public static boolean configNeededFormDashboard(Integer templateID) {
		File templateFile=getDirTemplate(templateID);
		if (templateFile.exists()) {
			Map<String, Object> descriptionMap = getTemplateDescription(templateFile);
			if (descriptionMap!=null) {
				String datasourcePluginID = (String) descriptionMap.get(IDescriptionAttributes.DATASOURCEPLUGIN);
				if (datasourcePluginID==null) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static Map<String, Object> getTemplateDescription(File templateFile) {
		File file = new File(templateFile, "description.xml");
		try {
			return getTemplateDescription(new FileInputStream(file)) ;
		} catch (FileNotFoundException e) {
			LOGGER.error("getTemplateDescription error:"+e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return new HashMap<String, Object>();
		}
	}

	public static Map<String, String> getTemplateDescription2(InputStream is) {
		Map<String, String> description = new HashMap<String, String>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList nl = doc.getDocumentElement().getChildNodes();
			for (int s = 0; s < nl.getLength(); s++) {
				if (nl.item(s).getNodeType() == Node.ELEMENT_NODE) {
					description.put(nl.item(s).getNodeName(), nl.item(s).getChildNodes().item(0).getNodeValue());
				}
			}
		} catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return description;
	}
	
	public static Map<String, Object> getTemplateDescription(InputStream is) {
		Map<String, Object> description = new HashMap<String, Object>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			try {
				NodeList nl = doc.getElementsByTagName("locale");
				for (int s = 0; s < nl.getLength(); s++) {
					Node localeNode = nl.item(s);
					if (localeNode.getNodeType() == Node.ELEMENT_NODE) {
						Map<String, String> localizedStuff = new HashMap<String, String>();
						String value = "";
						String descript = "";
						String listing = "";
						String toolTip = "";
						Element element = (Element) localeNode;
						descript = getTagValue("description", element).trim();
						listing = getTagValue("listing", element).trim();
						toolTip = getTagValue("tool-tip", element).trim();
						value = element.getAttribute("value");
						if (value==null) {
							value="";
						}
						localizedStuff.put("listing", listing);
						localizedStuff.put("description", descript);
						localizedStuff.put("tool-tip", toolTip);
	
						description.put("locale_"+value, localizedStuff);
					}
				}
			}catch (Exception e) {
				LOGGER.error("Getting the locale elements failed with " + e.getMessage());
			}
			NodeList nl = doc.getDocumentElement().getChildNodes();
			for (int s = 0; s < nl.getLength(); s++) {
				if (nl.item(s).getNodeType() == Node.ELEMENT_NODE) {
					description.put(nl.item(s).getNodeName(), nl.item(s).getChildNodes().item(0).getNodeValue());
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return description;
	}
	
	private static String getTagValue(String sTag, Element eElement) {
		try {
			NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
			Node nValue = nlList.item(0);
			return nValue.getNodeValue();
		} catch (Exception e) {
			LOGGER.warn("Getting the tag value " + sTag + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Get a localized 
	 * @param map
	 * @param tag can be "listing", "description", or "tool-tip"
	 * @param locale
	 * @return
	 */
	public static String getLocalizedText(Map<String, Object> map, String tag, Locale locale) {
		Map<String,String> localizedStuff = null;
		String ret = null;

		if (locale != null) {
			localizedStuff = (Map<String,String>)map.get("locale_" + locale.getLanguage());
			if (localizedStuff != null) {
				ret = localizedStuff.get(tag);
				return ret;
			}
		}
		
		localizedStuff = (Map<String,String>)map.get("locale_" + Locale.getDefault().getLanguage());
		if (localizedStuff != null) {
			ret = localizedStuff.get(tag);
			return ret;
		}
		
		localizedStuff = (Map<String,String>)map.get("locale_");
		if (localizedStuff != null) {
			ret = localizedStuff.get(tag);
			return ret;
		}
		return ret;
	}
	
	/**
	 * Save the new template at given location(repository, project)
	 * @param repository
	 * @param personID
	 * @param project
	 * @param templateName
	 * @param uploadZip
	 * @return an error meassage if exist
	 */
	public static void saveTemplate(Integer id, ZipInputStream zis)
			throws IOException {
		// specify buffer size for extraction
		final int BUFFER = 2048;
		// Open Zip file for reading
		File unzipDestinationDirectory= getDirTemplate(id);
		BufferedOutputStream dest = null;
		ZipEntry entry;
		while((entry = zis.getNextEntry()) != null) {
			File destFile = new File(unzipDestinationDirectory, entry.getName());
			if (destFile.exists()) {
				destFile.delete();
			}
			// grab file's parent directory structure
			File destinationParent = destFile.getParentFile();
			// create the parent directory structure if needed
			destinationParent.mkdirs();
			int count;
			byte data[] = new byte[BUFFER];
			// write the files to the disk
			FileOutputStream fos = new FileOutputStream(destFile);
			dest = new BufferedOutputStream(fos, BUFFER);
			while ((count = zis.read(data, 0, BUFFER))!= -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
		}
		zis.close();
	}
	
	/**
	 * Zips a directory content into a zip stream
	 * @param dirZip
	 * @param zipOut
	 * @param rootPath
	 */
	public static void zipFiles(File dirZip, ZipOutputStream zipOut, String rootPath) {
		try {
			// get a listing of the directory content
			File[] dirList = dirZip.listFiles();
			byte[] readBuffer = new byte[2156];
			int bytesIn;
			// loop through dirList, and zip the files
			for (int i = 0; i < dirList.length; i++) {
				File f = dirList[i];
				if (f.isDirectory()) {
					// if the File object is a directory, call this
					// function again to add its content recursively
					String filePath = f.getAbsolutePath();
					zipFiles(new File(filePath), zipOut, rootPath);
					// loop again
					continue;
				}
				// if we reached here, the File object f was not a directory
				// create a FileInputStream on top of f
				FileInputStream fis = new FileInputStream(f);
				// create a new zip entry
				ZipEntry anEntry = new ZipEntry(f.getAbsolutePath().substring(
						rootPath.length()+1, f.getAbsolutePath().length()));
				// place the zip entry in the ZipOutputStream object
				zipOut.putNextEntry(anEntry);
				// now write the content of the file to the ZipOutputStream
				while ((bytesIn = fis.read(readBuffer)) != -1) {
					zipOut.write(readBuffer, 0, bytesIn);
				}
				// close the Stream
				fis.close();
			}

		} catch (Exception e) {
			LOGGER.error("Zip the " + dirZip.getName() + " failed with " + e.getMessage() );
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}
}
