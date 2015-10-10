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
import com.aurel.track.beans.TScreenTabBean;


public class ScreenTabParser extends ParserFactory //implements IParser  
{

	private static final Logger LOGGER = LogManager.getLogger(ScreenTabParser.class);
	private TScreenTabBean tmpScreenTabBean;
	private List<TScreenTabBean> screenTabBeans;
	
	private boolean isScreenTab = false;
	private boolean isSubScreenTab = false;
    private boolean isName = false;
    private boolean isLabel = false;
    private boolean isDescription = false;
    private boolean isIndex = false;
    private boolean isParent = false;
    private StringBuffer buffer ;
    
   
    
	public List<TScreenTabBean> parse()
	{	
		screenTabBeans = new ArrayList<TScreenTabBean>();
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
		 return screenTabBeans;
	}

    
	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		 buffer = new StringBuffer();
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.SCREEN_TAB.equals(attributes.getValue("type"))) {
				 tmpScreenTabBean = new TScreenTabBean();
				 tmpScreenTabBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpScreenTabBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isScreenTab = true;
				 isSubScreenTab = false;
			 }
			 else {
				 isSubScreenTab = true;
			 }
		 }		
		 if (isScreenTab && !isSubScreenTab)
		 {
			 if (element_name.equals(IExchangeFieldNames.NAME)){
				 isName = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LABEL)){
				 isLabel = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
				 isDescription = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.INDEX)){
				 isIndex = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PARENT)){
				 isParent = true;
			 }
		 }
	 }
	 
	 @Override
	public void endElement(String uri, String localName, 
			 String element_name) throws SAXException
	 {
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isScreenTab){ 
				 screenTabBeans.add(tmpScreenTabBean);
				 isScreenTab = false;
		 }
		 if  (isScreenTab && !isSubScreenTab)
		 {
			 if (element_name.equals(IExchangeFieldNames.NAME)){	
				 tmpScreenTabBean.setName(buffer.toString());
				 isName = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.LABEL)){
				 tmpScreenTabBean.setLabel(buffer.toString());
				 isLabel = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
				 tmpScreenTabBean.setDescription(buffer.toString());
				 isDescription = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.INDEX)){
				 tmpScreenTabBean.setIndex(Integer.parseInt(buffer.toString()));
				 isIndex = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.PARENT)){
				 tmpScreenTabBean.setParent(Integer.parseInt(buffer.toString()));
				 isParent = false;
			 }
		 }
	 }

	@Override
	public void characters(char[] ch, int start, int len) throws SAXException{
		String str = new String (ch, start, len);
		
		if (isName){
			buffer.append(str);
		}
		if (isLabel){
			buffer.append(str);
		}
		if (isDescription){
			buffer.append(str);
		}
		if (isIndex){
			buffer.append(str);
		}
		if (isParent){
			buffer.append(str);
		}
	}

	
}



