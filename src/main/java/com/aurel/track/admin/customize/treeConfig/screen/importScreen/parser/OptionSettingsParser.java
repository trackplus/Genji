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
import com.aurel.track.beans.TOptionSettingsBean;



public class OptionSettingsParser extends ParserFactory //implements IParser  
{
	private static final Logger LOGGER = LogManager.getLogger(OptionSettingsParser.class);
	private TOptionSettingsBean tmpOptionSettingsBean;
	private List<TOptionSettingsBean> optionSettingsBeans;
	
	private boolean isOptionSettings = false;
	private boolean isSubOptionSettings = false;
    private boolean isList = false;
    private boolean isConfig = false;
    private boolean isParameterCode = false;
    private boolean isMultiple = false;
    private StringBuffer buffer ;
   
    
    
	public List<TOptionSettingsBean> parse()
	{	
		optionSettingsBeans = new ArrayList<TOptionSettingsBean>();
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
		 return optionSettingsBeans;
	}
	  

	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		 buffer = new StringBuffer();
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.OPTIONSETTINGS.equals(attributes.getValue("type"))) {
				 tmpOptionSettingsBean = new TOptionSettingsBean();
				 tmpOptionSettingsBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpOptionSettingsBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isOptionSettings = true;
				 isSubOptionSettings = false;
			 }
			 else {
				 isSubOptionSettings = true;
			 }
		 }	
		 if (isOptionSettings && !isSubOptionSettings)
		 {
			 if (element_name.equals(IExchangeFieldNames.LIST)){
				 isList = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CONFIG)){
				 isConfig = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PARAMETERCODE)){
				 isParameterCode = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MULTIPLE)){
				 isMultiple = true;
			 }
		 }
	 }
	 
	 @Override
	public void endElement(String uri, String localName, 
			 String element_name) throws SAXException
	 {
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isOptionSettings){ 
			 optionSettingsBeans.add(tmpOptionSettingsBean);
			 isOptionSettings = false;
		 }
		 if (isOptionSettings && !isSubOptionSettings)
		 {
			 
			 if (element_name.equals(IExchangeFieldNames.LIST)){
				 tmpOptionSettingsBean.setList(Integer.parseInt(buffer.toString()));
				 isList = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CONFIG)){
				 tmpOptionSettingsBean.setConfig(Integer.parseInt(buffer.toString()));
				 isConfig = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PARAMETERCODE)){
				 tmpOptionSettingsBean.setParameterCode(Integer.parseInt(buffer.toString()));
				 isParameterCode = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MULTIPLE)){
				 tmpOptionSettingsBean.setMultiple(buffer.toString());
				 isMultiple = false;
			 }
		 }
	 }

	@Override
	public void characters(char[] ch, int start, int len) throws SAXException{
		String str = new String (ch, start, len);
		
		if (isList){
			buffer.append(str);
		}
		if (isConfig){
			buffer.append(str);
		}
		if (isParameterCode){
			buffer.append(str);
		}
		if (isMultiple){
			buffer.append(str);
		}
	}

}



