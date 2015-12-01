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

package com.aurel.track.admin.customize.category.report.exportReport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.aurel.track.admin.customize.category.CategoryBL.REPOSITORY_TYPE;
import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.admin.customize.treeConfig.screen.importScreen.IExchangeFieldNames;
import com.aurel.track.beans.TExportTemplateBean;
import com.aurel.track.util.GeneralUtils;

public class ExportReportTemplateBL {
	
	private static Document dom = null;
	private static Element rootElement;
	private static final Logger LOGGER = LogManager.getLogger(ExportReportTemplateBL.class);


	/**
	 * Export selected TEXPORTTEMPLATE data to xml
	 * @param outputStream
	 */
	public static ZipOutputStream exportToZip (OutputStream outputStream, String selectedReportTemplates) {
		List<Integer> selectedIDs = getReportIDList(selectedReportTemplates);
		//export data to xml
		exportXml(selectedIDs);
		//zip xml and template zips into a new zip file
		return saveZip(outputStream, selectedIDs);	
	}
	
	
	/**
	 * create DOM
	 */
	public static void createDOM()
	{
		try
		{
			//create DOM
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			dom = builder.newDocument();	
			
			//create root element
			rootElement = dom.createElement (IExchangeFieldNames.TRACKPLUS_EXCHANGE);
			dom.appendChild(rootElement);
		}
		catch (FactoryConfigurationError e)
		{
			LOGGER.error("Creating the DOM document failed with FactoryConfigurationError:" + e.getMessage());
			return;
		} 
		catch (ParserConfigurationException e)
		{
			LOGGER.error("Creating the DOM document failed with ParserConfigurationException: " + e.getMessage());
			return;
		}
		catch (Exception e)
		{
			LOGGER.error("Error: " + e.getMessage());
		}
	}
	
	/**
	 * export data to xml
	 * remark: repository type converted to public
	 * @param selectedIDs
	 */
	private static void exportXml(List<Integer> selectedIDs)
	{
		//create DOM
		createDOM();
		//add elements to dom
		List<TExportTemplateBean> tExportTemplateBeans =  ReportBL.loadByIDs(selectedIDs);		
		Iterator<TExportTemplateBean> itrTemplate = tExportTemplateBeans.iterator();	
		while(itrTemplate.hasNext()) {	
			TExportTemplateBean tExportTemplateBean = itrTemplate.next(); 
			tExportTemplateBean.setRepositoryType(REPOSITORY_TYPE.PUBLIC);
			Map<String,String> serTExportTemplateBean = tExportTemplateBean.serializeBean();
			Element exportTemplateElement = generateElement(serTExportTemplateBean, ITableTypes.EXPORTTEMPLATE);
			rootElement.appendChild(exportTemplateElement);
		}
	}
		
	/**
	 * generate elements from the serializedBean
	 * @param serializedBean
	 * @param typeAttribute
	 * @return
	 */
	private static Element generateElement(Map<String,String> serializedBean, String typeAttribute)
	{	
		Element tmpElement;
		Element tmpSubElement;
		try {
			tmpElement = dom.createElement(IExchangeFieldNames.ITEM);
			tmpElement.setAttribute(IExchangeFieldNames.ITEM_TYPE, typeAttribute);
			Set s = serializedBean.entrySet();
	        Iterator it = s.iterator();
	        while (it.hasNext()) {
	        	Map.Entry mapData = (Map.Entry)it.next();
	        	String key = (String)mapData.getKey();
	        	String value = (String)mapData.getValue();
	        	
	        	if (value!=null) {
	        		if (key=="objectID") {
	        			tmpElement.setAttribute(IExchangeFieldNames.OBJECT_ID, value);
	        		}
	        		else if (key=="uuid") {
		        		tmpElement.setAttribute(IExchangeFieldNames.TPUUID, value);
		        	}
		        	else {
		        		tmpSubElement = dom.createElement(key);
				        Text text = dom.createTextNode(value);
				        tmpSubElement.appendChild(text);
				        tmpElement.appendChild(tmpSubElement);
			        }
		        }
	       }
		}
		catch (Exception e) {
			LOGGER.warn("Error = "+e.getMessage());
			return null;
		}
		return tmpElement;
	}

	
	
	/**
	 * save the DOM 
	 */
	public static ZipOutputStream saveZip(OutputStream outputStream, List<Integer> selectedIDs)
	{		
		ZipOutputStream zipOut = null;

		byte[] buf = new byte[1024];
		Transformer transformer = null;

		try 
		{	
			//create transformer
			TransformerFactory tFactory = TransformerFactory.newInstance();
			transformer = tFactory.newTransformer ();
			transformer.setOutputProperty (OutputKeys.INDENT, "yes");
			transformer.setOutputProperty (OutputKeys.METHOD, "xml");
			transformer.setOutputProperty (OutputKeys.ENCODING,"UTF-8");
			transformer.setOutputProperty ("{http://xml.apache.org/xslt}indent-amount", "4");			
			
			//add xml to zip
			zipOut = new ZipOutputStream(outputStream);
			zipOut.putNextEntry(new ZipEntry("templates.xml"));			
			transformer.transform (new DOMSource (dom), new StreamResult(zipOut));
			
			// zip the template files and add them to the main zip
			for (Integer objectID : selectedIDs){
				ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
				ZipOutputStream zipOut2 = new ZipOutputStream(bos);	
				
				File tmpTemplateToDownload = ReportBL.getDirTemplate(objectID);
				ReportBL.zipFiles(tmpTemplateToDownload, zipOut2,
						tmpTemplateToDownload.getAbsolutePath());
					
				zipOut2.close();
				InputStream decodedInput=new ByteArrayInputStream(bos.toByteArray());
	
				zipOut.putNextEntry(new ZipEntry(tmpTemplateToDownload.getName()+".zip"));
				int len;
				while ((len = decodedInput.read(buf)) > 0) {
		            zipOut.write(buf, 0, len);
		        }
			}
				
			zipOut.closeEntry();
			zipOut.close();
			
		}catch (TransformerConfigurationException e)
		{
			LOGGER.error ("Creating the transformer failed with TransformerConfigurationException: " + e.getMessage());
			return null;
		}
		catch (TransformerException e){
			LOGGER.error ("Transform failed with TransformerException: " + e.getMessage());
			return null;
		}
		catch (FileNotFoundException e)
		{
			LOGGER.error("Error: " +e.getMessage());
			return null;
		}
		catch (IOException e)
		{
			LOGGER.error("Error: " +e.getMessage());
			return null;
		}
		return zipOut;
	}
	
	
	
	
	/**
	 * Gets the selected personIDs form the comma separated string
	 * @param selectedPersonIDs
	 * @return
	 */
	private static List<Integer> getReportIDList(String selectedReportIDs) {
		String[] selectedReportIDArr = selectedReportIDs.split(",");
		return GeneralUtils.createIntegerListFromStringArr(selectedReportIDArr);
	}
}



