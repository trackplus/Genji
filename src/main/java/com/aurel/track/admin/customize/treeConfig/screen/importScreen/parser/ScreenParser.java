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
import com.aurel.track.beans.TScreenBean;


public class ScreenParser extends ParserFactory //implements IParser  
{

	private static final Logger LOGGER = LogManager.getLogger(ScreenParser.class);
	private TScreenBean tmpScreenBean;
	private List<TScreenBean> screenBeans;
	
	private boolean isScreen = false;
	private boolean isSubScreen = false;
    private boolean isName = false;
    private boolean isLabel = false;
    private boolean isTagLabel = false;
    private boolean isDescription = false;
    private boolean isOwner = false;  
    private StringBuffer buffer ;

   
    
	public List<TScreenBean> parse()
	{	
		screenBeans = new ArrayList<TScreenBean>();
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
			 LOGGER.error("Parse Configuration Exception:" + e.getMessage());
		 }
		 catch (IOException e) {
			 LOGGER.error("IO exception:" + e.getMessage());
		 }
		 catch (Exception e)
		 {
			 LOGGER.error("Error parsing file " +file.getName() + " :" + e.getMessage());
		 }
		 return screenBeans;
	}
	  

	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		 buffer = new StringBuffer();
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.SCREEN.equals(attributes.getValue("type"))) {
				 tmpScreenBean = new TScreenBean();
				 tmpScreenBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpScreenBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isScreen = true;
				 isSubScreen = false;
			 }
			 else {
				 isSubScreen = true;
			 }
		 }		
		 if (isScreen && !isSubScreen)
		 {	 
			 if (element_name.equals(IExchangeFieldNames.NAME)){
				 isName = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LABEL)){
				 isLabel = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.TAGLABEL)){
				 isTagLabel = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
				 isDescription = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.OWNER)){
				 isOwner = true;
			 }
		 }
	 }
	 
	 @Override
	public void endElement(String uri, String localName, 
			 String element_name) throws SAXException
	 {
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isScreen){ 
				 screenBeans.add(tmpScreenBean);
				 isScreen = false;
		 }
		 if (isScreen && !isSubScreen)
		 {
			 if (element_name.equals(IExchangeFieldNames.NAME)){
					tmpScreenBean.setName(buffer.toString());
				 isName = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.LABEL)){
					tmpScreenBean.setLabel(buffer.toString());
				 isLabel = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.TAGLABEL)){
					tmpScreenBean.setTagLabel(buffer.toString());
				 isTagLabel = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
					tmpScreenBean.setDescription(buffer.toString());
				 isDescription = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.OWNER)){
					tmpScreenBean.setOwner(Integer.parseInt(buffer.toString()));
				 isOwner = false;
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
		if (isTagLabel){
			buffer.append(str);
		}
		if (isDescription){
			buffer.append(str);
		}
		if (isOwner){
			buffer.append(str);
		}
	}

	
}



