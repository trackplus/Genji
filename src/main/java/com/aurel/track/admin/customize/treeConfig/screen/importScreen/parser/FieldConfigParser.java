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

package com.aurel.track.admin.customize.treeConfig.screen.importScreen.parser;

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
import com.aurel.track.beans.TFieldConfigBean;


public class FieldConfigParser extends ParserFactory //implements IParser  
{
	private static final Logger LOGGER = LogManager.getLogger(FieldConfigParser.class);
	private boolean isFieldConfig = false;
	private boolean isSubFieldConfig = false;
	private boolean isField = false;
	private boolean isLabel = false;
	private boolean isToolTip = false;
	private boolean isRequired = false;
	private boolean isHistory = false;
	private boolean isIssueType = false;
	private boolean isProjectType = false;
	private boolean isDescription = false;
	private boolean isProject = false;
	private TFieldConfigBean tmpFieldConfigBean;
	private List<TFieldConfigBean> fieldConfigBeans; 
	private StringBuffer buffer ;
				

	
	public List<TFieldConfigBean> parse()
	{	
		fieldConfigBeans = new ArrayList<TFieldConfigBean>();
		File file = getFile();
		 try 
		 {
			 SAXParserFactory parserFact = SAXParserFactory.newInstance();
			 SAXParser parser = parserFact.newSAXParser();
			 parser.parse(file, this);
		 } 
		 catch(SAXException e) {
			 LOGGER.error("Parsing Sax Exception:" + e.getMessage(), e);
		 }
		 catch(ParserConfigurationException e) {
			 LOGGER.error("Parse Configuration Exception:" + e.getMessage(), e);
		 }
		 catch (IOException e) {
			 LOGGER.error("IO exception:" + e.getMessage(), e);
		 }
		 catch (Exception e)
		 {
			 LOGGER.error("Error parsing file " +file.getName() + " :" + e.getMessage(), e);
		 }
		 
		 return fieldConfigBeans;
	}
	  


	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		 buffer = new StringBuffer();
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.FIELD_CONFIG.equals(attributes.getValue("type"))) {
				 tmpFieldConfigBean = new TFieldConfigBean();
				 tmpFieldConfigBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpFieldConfigBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isFieldConfig = true;
				 isSubFieldConfig = false;
			 }
			 else if (isFieldConfig) {
				 isSubFieldConfig = true;
			 }
		 }
		 if (isFieldConfig && !isSubFieldConfig)
		 {
			 if (element_name.equals(IExchangeFieldNames.FIELD)){
				 isField = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LABEL)){
				 isLabel = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.TOOLTIP)){
				 isToolTip = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REQUIRED)){
				 isRequired = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.HISTORY)){
				 isHistory = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ISSUETYPE)){
				 isIssueType = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PROJECTTYPE)){
				 isProjectType = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PROJECT)){
				 isProject = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
				 isDescription = true;
			 }
		 }
	 }
	 
	 @Override
	public void endElement(String uri, String localName, 
			 String element_name) throws SAXException
	 {
			 if (element_name.equals(IExchangeFieldNames.ITEM) && isFieldConfig)
			 {
				 	fieldConfigBeans.add(tmpFieldConfigBean);
					isFieldConfig = false;
			 }
			 if (isFieldConfig&& !isSubFieldConfig)
			 {
				 if (element_name.equals(IExchangeFieldNames.FIELD)){
					 tmpFieldConfigBean.setField(Integer.parseInt(buffer.toString()));
					 isField = false;
				 }	
				 else if (element_name.equals(IExchangeFieldNames.LABEL)){
					 tmpFieldConfigBean.setLabel(buffer.toString());
					 isLabel = false;
				 }
				 else if (element_name.equals(IExchangeFieldNames.TOOLTIP)){
					 tmpFieldConfigBean.setTooltip(buffer.toString());
					 isToolTip = false;
				 }
				 else if (element_name.equals(IExchangeFieldNames.REQUIRED)){
					 tmpFieldConfigBean.setRequired(buffer.toString());
					 isRequired = false;
				 }
				 else if (element_name.equals(IExchangeFieldNames.HISTORY)){
					 tmpFieldConfigBean.setHistory(buffer.toString());
					 isHistory= false;
				 }
				 else if (element_name.equals(IExchangeFieldNames.ISSUETYPE)){
					 tmpFieldConfigBean.setIssueType(Integer.parseInt(buffer.toString()));
					 isIssueType = false;
				 }
				 else if (element_name.equals(IExchangeFieldNames.PROJECTTYPE)){
					 tmpFieldConfigBean.setProjectType(Integer.parseInt(buffer.toString()));
					 isProjectType = false;
				 }
				 else if (element_name.equals(IExchangeFieldNames.PROJECT)){
					 tmpFieldConfigBean.setProject(Integer.parseInt(buffer.toString()));
					 isProject = false;
				 }
				 else if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
					 tmpFieldConfigBean.setDescription(buffer.toString());
					 isDescription = false;
				 }
			 }
	}
	 
	@Override
	public void characters(char[] ch, int start, int len) throws SAXException{
		String str = new String (ch, start, len);
		
		if (isField){
			buffer.append(str);
		}
		
		if (isLabel){
			buffer.append(str);
		}
		if (isToolTip){
			buffer.append(str);
		}
		if (isRequired){
			buffer.append(str);
		}
		if (isHistory){
			buffer.append(str);
		}
		if (isIssueType){
			buffer.append(str);
		}
		if (isProjectType){
			buffer.append(str);
		}
		if (isProject){
			buffer.append(str);
		}
		if (isDescription){
			buffer.append(str);
		}

	}

}



