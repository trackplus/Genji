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
import com.aurel.track.beans.TFieldBean;


public class FieldParser extends ParserFactory //implements IParser  
{
	private static final Logger LOGGER = LogManager.getLogger(FieldParser.class);
	private boolean isField = false;
	//shows wether we are in a sub element (sub elements can have same attribute names
	//as well, for example description
	private boolean isSubField = false;
	private boolean isName = false;
	private boolean isDeprecated = false;
	private boolean isFieldType = false;
	private boolean isFilterField = false;
	private boolean isRequired = false;
	private boolean isIsCustom = false;
	
	private TFieldBean tmpFieldBean;
	private List<TFieldBean> fieldBeans;
	private StringBuffer buffer ;			
	

	
	public List<TFieldBean> parse()
	{	
		fieldBeans = new ArrayList<TFieldBean>();
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
		 return fieldBeans;
	}
	  


	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		 buffer = new StringBuffer(); 
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.FIELD.equals(attributes.getValue("type"))) {
				 tmpFieldBean = new TFieldBean();
				 tmpFieldBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpFieldBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isField = true;
				 isSubField = false;
			 }
			 else {
				 isSubField = true;
			 }
		 }
		 if (isField && !isSubField)
		 {
			 if (element_name.equals(IExchangeFieldNames.DEPRECATED)){
				 isDeprecated = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.FIELDTYPE)){
				 isFieldType = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.FILTERFIELD)){
				 isFilterField = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.NAME)){
				 isName = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REQUIRED)){
				 isRequired = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ISCUSTOM)){
				 isIsCustom = true;
			 }
		 }
	 }

	 @Override
	public void endElement(String uri, String localName, 
			 String element_name) throws SAXException
	 {
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isField){
			 fieldBeans.add(tmpFieldBean);
			 isField = false;
		 }
		 if (!isSubField && isField)
		 {
			 if (element_name.equals(IExchangeFieldNames.FIELDTYPE)){
				 tmpFieldBean.setFieldType(buffer.toString());
				 isFieldType = false;
			 }	
			 else if (element_name.equals(IExchangeFieldNames.DEPRECATED)){
				 tmpFieldBean.setDeprecated(buffer.toString());
				 isDeprecated = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.FILTERFIELD)){
				 tmpFieldBean.setFilterField(buffer.toString());
				 isFilterField = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.NAME)){
				 tmpFieldBean.setName(buffer.toString());
				 isName = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REQUIRED)){
				 tmpFieldBean.setRequired(buffer.toString());
				 isRequired = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ISCUSTOM)){
				 tmpFieldBean.setIsCustom(buffer.toString());
				 isIsCustom = false;
			 }	 
		 }
	 }

	@Override
	public void characters(char[] ch, int start, int len) throws SAXException{
		String str = new String (ch, start, len);
		if (isDeprecated){
			buffer.append(str);
		}
		if (isFieldType){
			buffer.append(str);
		}
		if (isFilterField){
			buffer.append(str);
		}
		if (isName){
			buffer.append(str);
		}
		if (isRequired){
			buffer.append(str);
		}
		if (isIsCustom){
			buffer.append(str);
		}
	}
}



