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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.aurel.track.admin.customize.treeConfig.screen.importScreen.IExchangeFieldNames;
import com.aurel.track.admin.customize.treeConfig.screen.importScreen.ITableTypes;
import com.aurel.track.admin.customize.treeConfig.screen.importScreen.parser.ParserFactory;
import com.aurel.track.beans.TExportTemplateBean;



public class ExportTemplateParser extends ParserFactory //implements IParser  
{
	private static final Logger LOGGER = LogManager.getLogger(ExportTemplateParser.class);
	private boolean isExportTemplate = false;
	//shows wether we are in a sub element (sub elements can have same attribute names
	//as well, for example description
	private boolean isSub = false;

	
	private boolean isName = false;
	private boolean isReportType = false;
	private boolean isExportFormat = false;
	private boolean isRepositoryType = false;
	private boolean isDescription = false;
	private boolean isProject = false;
	private boolean isPerson = false;
	private boolean isCategoryKey = false;
	private boolean isDeleted = false;
	
	private TExportTemplateBean tmpExportTemplateBean;
	private List<TExportTemplateBean> exportTemplateBeans;
	private StringBuffer buffer ;			
	
	public List<TExportTemplateBean> parse()
	{	
		exportTemplateBeans = new ArrayList<TExportTemplateBean>();
		File file = getFile();
		 try 
		 {
			 SAXParserFactory parserFact = SAXParserFactory.newInstance();
			 SAXParser parser = parserFact.newSAXParser();
			 parser.parse(file, this);
		 } 
		 catch(SAXException e) {
			 LOGGER.error("Parsing Sax Exception:" + e.getMessage());
		 }
		 catch(ParserConfigurationException e) {
			 LOGGER.error("Parse Configuration Exception: " + e.getMessage());
		 }
		 catch (IOException e) {
			 LOGGER.error("IO exception:" + e.getMessage());
		 }
		 catch (Exception e)
		 {
			 LOGGER.error("Error parsing file " +file.getName() + " :" + e.getMessage());
		 }
		 return exportTemplateBeans;
	}
	  


	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException {
		 buffer = new StringBuffer(); 
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.EXPORTTEMPLATE.equals(attributes.getValue("type"))) {
				 tmpExportTemplateBean = new TExportTemplateBean();
				 tmpExportTemplateBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpExportTemplateBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isExportTemplate = true;
				 isSub = false;
			 }
			 else {
				 isSub = true;
			 }
		 }
		 if (isExportTemplate && !isSub)
		 {
			 if (element_name.equals(IExchangeFieldNames.NAME)){
				 isName = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REPORTTYPE)){
				 isReportType = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EXPORTFORMAT)){
				 isExportFormat = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REPOSITORYTYPE)){
				 isRepositoryType = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
				 isDescription = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PROJECT)){
				 isProject = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PERSON)){
				 isPerson = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CATEGORYKEY)){
				 isCategoryKey = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DELETED)){
				 isDeleted = true;
			 }
		 }
	 }


	 @Override
	public void endElement(String uri, String localName, 
			 String element_name) throws SAXException
	 {
		 
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isExportTemplate){
			 exportTemplateBeans.add(tmpExportTemplateBean);
			 isExportTemplate = false;
		 }
		 if (!isSub && isExportTemplate)
		 {
			 if (element_name.equals(IExchangeFieldNames.NAME)){
				 tmpExportTemplateBean.setName(buffer.toString());
				 isName = false;
			 }	
			 else if (element_name.equals(IExchangeFieldNames.REPORTTYPE)){
				 tmpExportTemplateBean.setReportType(buffer.toString());
				 isReportType = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EXPORTFORMAT)){
				 tmpExportTemplateBean.setExportFormat(buffer.toString());
				 isExportFormat = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REPOSITORYTYPE)){
				 tmpExportTemplateBean.setRepositoryType(Integer.parseInt(buffer.toString()));
				 isRepositoryType = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
				 tmpExportTemplateBean.setDescription(buffer.toString());
				 isDescription = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PROJECT)){
				 tmpExportTemplateBean.setProject(Integer.parseInt(buffer.toString()));
				 isProject = false;
			 }	
			 else if (element_name.equals(IExchangeFieldNames.PERSON)){
				 tmpExportTemplateBean.setPerson(Integer.parseInt(buffer.toString()));
				 isPerson = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CATEGORYKEY)){
				 tmpExportTemplateBean.setCategoryKey(Integer.parseInt(buffer.toString()));
				 isCategoryKey = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DELETED)){
				 tmpExportTemplateBean.setDeleted(buffer.toString());
				 isDeleted = false;
			 }		 
		 }
	 }

	 
	@Override
	public void characters(char[] ch, int start, int len) throws SAXException{
		String str = new String (ch, start, len);
		if (isName){
			buffer.append(str);
		}
		if (isReportType){
			buffer.append(str);
		}
		if (isExportFormat){
			buffer.append(str);
		}
		if (isRepositoryType){
			buffer.append(str);
		}
		if (isDescription){
			buffer.append(str);
		}
		if (isProject){
			buffer.append(str);
		}
		if (isPerson){
			buffer.append(str);
		}
		if (isCategoryKey){
			buffer.append(str);
		}
		if (isDeleted){
			buffer.append(str);
		}
	}
}



