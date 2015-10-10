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
import com.aurel.track.beans.TScreenPanelBean;


public class ScreenPanelParser extends ParserFactory //implements IParser  
{
	private static final Logger LOGGER = LogManager.getLogger(ScreenPanelParser.class);
	private TScreenPanelBean tmpScreenPanelBean;
	private List<TScreenPanelBean> screenPanelBeans;
	
	private boolean isScreenPanel = false;
	private boolean isSubScreenPanel = false;
    private boolean isName = false;
    private boolean isLabel = false;
    private boolean isDescription = false;
    private boolean isIndex = false;
    private boolean isRowsNo = false;
    private boolean isColsNo = false;
    private boolean isParent = false;
    private StringBuffer buffer ;
    

    
	public List<TScreenPanelBean> parse()
	{	
		screenPanelBeans = new ArrayList<TScreenPanelBean>();
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
		 return screenPanelBeans;
	}
	  

	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		 buffer = new StringBuffer();
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.SCREEN_PANEL.equals(attributes.getValue("type"))) {
				 tmpScreenPanelBean = new TScreenPanelBean();
				 tmpScreenPanelBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpScreenPanelBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isScreenPanel = true;
				 isSubScreenPanel = false;
			 }
			 else {
				 isSubScreenPanel = true;
			 }
		 }		
		 if (isScreenPanel && !isSubScreenPanel)
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
			 else if (element_name.equals(IExchangeFieldNames.ROWSNO)){
				 isRowsNo = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.COLSNO)){
				 isColsNo = true;
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
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isScreenPanel){ 
				 screenPanelBeans.add(tmpScreenPanelBean);
				 isScreenPanel = false;
		 }
		 if (!isSubScreenPanel && isScreenPanel )
		 {
			 if (element_name.equals(IExchangeFieldNames.NAME)){
				 tmpScreenPanelBean.setName(buffer.toString());
				 isName = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.LABEL)){
				 tmpScreenPanelBean.setLabel(buffer.toString());
				 isLabel = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
				 tmpScreenPanelBean.setDescription(buffer.toString());
				 isDescription = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.INDEX)){
				 tmpScreenPanelBean.setIndex(Integer.parseInt(buffer.toString()));
				 isIndex = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.ROWSNO)){
				 tmpScreenPanelBean.setRowsNo(Integer.parseInt(buffer.toString()));
				 isRowsNo = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.COLSNO)){
				 tmpScreenPanelBean.setColsNo(Integer.parseInt(buffer.toString()));
				 isColsNo = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.PARENT)){
				 tmpScreenPanelBean.setParent(Integer.parseInt(buffer.toString()));
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
		if (isRowsNo){
			buffer.append(str);
		}
		if (isColsNo){
			buffer.append(str);
		}
		if (isParent){
			buffer.append(str);
		}
		
	}

}



