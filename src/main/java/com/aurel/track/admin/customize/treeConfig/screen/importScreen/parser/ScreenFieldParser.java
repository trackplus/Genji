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
import com.aurel.track.beans.TScreenFieldBean;



public class ScreenFieldParser extends ParserFactory //implements IParser  
{
	private static final Logger LOGGER = LogManager.getLogger(ScreenFieldParser.class);
	private TScreenFieldBean tmpScreenFieldBean;
	private List<TScreenFieldBean> screenFieldBeans;
	
	private boolean isScreenField = false;
	private boolean isSubScreenField =false;
    private boolean isName = false;
    private boolean isDescription = false;
    private boolean isIndex = false;
    private boolean isColIndex = false;
    private boolean isRowIndex = false;
    private boolean isColSpan = false;
    private boolean isRowSpan = false;
    private boolean isLabelHAlign = false;
    private boolean isLabelVAlign = false;
    private boolean isValueHAlign = false;
    private boolean isValueVAlign = false;
    private boolean isIsEmpty = false;
    private boolean isParent = false;
    private boolean isField = false;
    private StringBuffer buffer ;
    
  
    
	public List<TScreenFieldBean> parse()
	{	
		screenFieldBeans = new ArrayList<TScreenFieldBean>();
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
		 return screenFieldBeans;
	}
	  

	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		 buffer = new StringBuffer();
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.SCREEN_FIELD.equals(attributes.getValue("type"))) {
				 tmpScreenFieldBean = new TScreenFieldBean();
				 tmpScreenFieldBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpScreenFieldBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isScreenField = true;
				 isSubScreenField = false;
			 }
			 else {
				 isSubScreenField = true;
			 }
		 }			    
		 if (isScreenField && !isSubScreenField)
		 {
			 if (element_name.equals(IExchangeFieldNames.NAME)){
				 isName = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
				 isDescription = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.INDEX)){
				 isIndex = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.COLINDEX)){
				 isColIndex = true;
			 } 
			 else if (element_name.equals(IExchangeFieldNames.ROWINDEX)){
				 isRowIndex = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.COLSPAN)){
				 isColSpan = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ROWSPAN)){
				 isRowSpan = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LABELHALIGN)){
				 isLabelHAlign = true;
			 } 
			 else if (element_name.equals(IExchangeFieldNames.LABELVALIGN)){
				 isLabelVAlign = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.VALUEHALIGN)){
				 isValueHAlign = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.VALUEVALIGN)){
				 isValueVAlign = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ISEMPTY)){
				 isIsEmpty = true;
			 } 
			 else if (element_name.equals(IExchangeFieldNames.PARENT)){
				 isParent = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.FIELD)){
				 isField = true;
			 } 
		 }
	 }
	 
	 @Override
	public void endElement(String uri, String localName, 
			 String element_name) throws SAXException
	 {
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isScreenField){ 
			
				 screenFieldBeans.add(tmpScreenFieldBean);
				 isScreenField = false;
		 }
		 if (!isSubScreenField && isScreenField)
		 {
			 if (element_name.equals(IExchangeFieldNames.NAME)){
				 tmpScreenFieldBean.setName(buffer.toString());
				 isName = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
					tmpScreenFieldBean.setDescription(buffer.toString());
				 isDescription = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.INDEX)){
					tmpScreenFieldBean.setIndex(Integer.parseInt(buffer.toString()));
				 isIndex = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.COLINDEX)){
					tmpScreenFieldBean.setColIndex(Integer.parseInt(buffer.toString()));
				 isColIndex = false;
			 } 
			 else if (element_name.equals(IExchangeFieldNames.ROWINDEX)){
					tmpScreenFieldBean.setRowIndex(Integer.parseInt(buffer.toString()));
				 isRowIndex = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.COLSPAN)){
					tmpScreenFieldBean.setColSpan(Integer.parseInt(buffer.toString()));
				 isColSpan = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ROWSPAN)){
					tmpScreenFieldBean.setRowSpan(Integer.parseInt(buffer.toString()));
				 isRowSpan = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LABELHALIGN)){
					tmpScreenFieldBean.setLabelHAlign(Integer.parseInt(buffer.toString()));
				 isLabelHAlign = false;
			 } 
			 else if (element_name.equals(IExchangeFieldNames.LABELVALIGN)){
					tmpScreenFieldBean.setLabelVAlign(Integer.parseInt(buffer.toString()));
				 isLabelVAlign = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.VALUEHALIGN)){
					tmpScreenFieldBean.setValueHAlign(Integer.parseInt(buffer.toString()));
				 isValueHAlign = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.VALUEVALIGN)){
					tmpScreenFieldBean.setValueVAlign(Integer.parseInt(buffer.toString()));
				 isValueVAlign = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ISEMPTY)){
					tmpScreenFieldBean.setIsEmpty(buffer.toString());
				 isIsEmpty = false;
			 } 
			 else if (element_name.equals(IExchangeFieldNames.PARENT)){
					tmpScreenFieldBean.setParent(Integer.parseInt(buffer.toString()));
				 isParent = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.FIELD)){
					tmpScreenFieldBean.setField(Integer.parseInt(buffer.toString()));
				 isField = false;
			 }
		 }
	 }

	@Override
	public void characters(char[] ch, int start, int len) throws SAXException{
		String str = new String (ch, start, len);
		
		if (isName){
			buffer.append(str);
		}
		if (isDescription){
			buffer.append(str);
		}
		if (isIndex){
			buffer.append(str);
		}
		if (isColIndex){
			buffer.append(str);
		}
		if (isRowIndex){
			buffer.append(str);
		}
		if (isColSpan){
			buffer.append(str);
		}
		if (isRowSpan){
			buffer.append(str);
		}
		if (isLabelHAlign){
			buffer.append(str);
		}
		if (isLabelVAlign){
			buffer.append(str);
		}
		if (isValueHAlign){
			buffer.append(str);
		}
		if (isValueVAlign){
			buffer.append(str);
		}
		if (isIsEmpty){
			buffer.append(str);
		}
		if (isParent){
			buffer.append(str);
		}
		if (isField){
			buffer.append(str);
		}
	}

}



