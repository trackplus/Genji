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
import com.aurel.track.beans.TOptionBean;


public class OptionParser extends ParserFactory //implements IParser  
{
	private static final Logger LOGGER = LogManager.getLogger(OptionParser.class);
	private TOptionBean tmpOptionBean;
	private List<TOptionBean> optionBeans;
	
	private boolean isOption = false;
    private Integer optionCount = 0;
	private boolean isSubOption = false;
    private boolean isList = false;
    private boolean isLabel = false;
    private boolean isParentOption = false;
    private boolean isSortOrder = false;
    private boolean isIsDefault = false;
    private boolean isDeleted = false;
    private boolean isSymbol = false;
    private boolean isIconKey = false;
    private boolean isIconChanged = false;
    private boolean isBgrcolor = false;
    private StringBuffer buffer ;
   

    
	public List<TOptionBean> parse()
	{	
		optionBeans = new ArrayList<TOptionBean>();
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
		 return optionBeans;
	}
	  

	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		 buffer = new StringBuffer();
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.OPTION.equals(attributes.getValue("type"))) {
				 optionCount++;
				 tmpOptionBean = new TOptionBean();
				 tmpOptionBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpOptionBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isOption = true;
				 isSubOption = false;
			 }
			 else {
				 isSubOption = true;
			 }
		 }
		 if (isOption && !isSubOption)
		 {
			 if (element_name.equals(IExchangeFieldNames.LIST)){
				 isList = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LABEL)){
				 isLabel = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PARENTOPTION)){
				 isParentOption = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.SORTORDER)){
				 isSortOrder = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ISDEFAULT)){
				 isIsDefault = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DELETED)){
				 isDeleted = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.SYMBOL)){
				 isSymbol = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ICONKEY)){
				 isIconKey = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ICONCHANGED)){
				 isIconChanged = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CSSSTYLE)){
				 isBgrcolor = true;
			 }
		 }		 
	 }
	 
	 @Override
	public void endElement(String uri, String localName, 
			 String element_name) throws SAXException
	 {
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isOption){ 
				 optionCount--;
				 if (optionCount == 0)
					 isOption = false;
		 }
		 if (isOption && !isSubOption)
		 {
			 if (element_name.equals(IExchangeFieldNames.LIST)){
				 tmpOptionBean.setList(Integer.parseInt(buffer.toString()));
				 isList = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.LABEL)){
				 tmpOptionBean.setLabel(buffer.toString());
				 optionBeans.add(tmpOptionBean);
				 isLabel = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.PARENTOPTION)){
				 tmpOptionBean.setParentOption(Integer.parseInt(buffer.toString()));
				 isParentOption = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.SORTORDER)){
				 tmpOptionBean.setSortOrder(Integer.parseInt(buffer.toString()));
				 isSortOrder = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.ISDEFAULT)){
				 tmpOptionBean.setIsDefault(buffer.toString());
				 isIsDefault = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.DELETED)){
				 tmpOptionBean.setDeleted(buffer.toString());
				 isDeleted = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.SYMBOL)){
				 tmpOptionBean.setSymbol(buffer.toString());
				 isSymbol = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.ICONKEY)){
				 tmpOptionBean.setIconKey(Integer.parseInt(buffer.toString()));
				 isIconKey = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.ICONCHANGED)){
				 tmpOptionBean.setIconChanged(buffer.toString());
				 isIconChanged = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.CSSSTYLE)){
				 tmpOptionBean.setCSSStyle(buffer.toString());
				 isBgrcolor = false;
			 }
		 }
	 }
	 
	@Override
	public void characters(char[] ch, int start, int len) throws SAXException{
		String str = new String (ch, start, len);
		
		if (isList){
			buffer.append(str);
		}
		if (isLabel){
			buffer.append(str);
		}
		if (isParentOption){
			buffer.append(str);
		}
		if (isSortOrder){
			buffer.append(str);
		}
		if (isIsDefault){
			buffer.append(str);
		}
		if (isDeleted){
			buffer.append(str);
		}
		if (isSymbol){
			buffer.append(str);
		}
		if (isIconKey){
			buffer.append(str);
		}
		if (isIconChanged){
			buffer.append(str);
		}
		if (isBgrcolor){
			buffer.append(str);
		}
	}

}



