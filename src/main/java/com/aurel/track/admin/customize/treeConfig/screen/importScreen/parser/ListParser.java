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
import com.aurel.track.beans.TListBean;

/**
 * TListBean xml parser
 * Can parse also Hierarchical TListBean xml structures
 * @author Andras
 *
 */

public class ListParser extends ParserFactory //implements IParser  
{
	private static final Logger LOGGER = LogManager.getLogger(ListParser.class);
	private TListBean tmpListBean;
	private List<TListBean> listBeans;
				
    private boolean isList = false;
    private Integer listCount = 0;
    private boolean isSubList = false;
    private boolean isName= false;
    private boolean isDescription= false;
    private boolean isTagLabel= false;
    private boolean isParentList= false;
    private boolean isListType= false;
    private boolean isChildNumber= false;
    private boolean isDeleted = false;
    private boolean isRepositoryType= false;
    private boolean isProject= false;
    private boolean isOwner= false;
    
    private StringBuffer buffer ;
    
  
    
	public List<TListBean> parse()
	{	
		listBeans = new ArrayList<TListBean>();
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
		 
		 return listBeans;
	}
	  

	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		
		 buffer = new StringBuffer();
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.LIST.equals(attributes.getValue("type"))) {
				 listCount++;
				 tmpListBean = new TListBean();
				 tmpListBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpListBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isList = true;
				 isSubList = false;
			 }
			 else {
				 isSubList = true;
			 }
		 }
		 if (isList && !isSubList)
		 {
			 if (element_name.equals(IExchangeFieldNames.NAME)){
			     isName = true;		     
			 }
			 else if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
				 isDescription = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.TAGLABEL)){
				 isTagLabel = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PARENTLIST)){
				 isParentList = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LISTTYPE)){
				 isListType = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CHILDNUMBER)){
				 isChildNumber = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DELETED)){
				 isDeleted = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REPOSITORYTYPE)){
				 isRepositoryType = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PROJECT)){
				 isProject = true;
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
		 
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isList){
			 listCount--;
			 if (listCount == 0)
				 isList = false;
		 }
		 if (isList && !isSubList)
		 {	 
			 if (element_name.equals(IExchangeFieldNames.NAME)){
				 tmpListBean.setName(buffer.toString());	
				 isName = false;
				 listBeans.add(tmpListBean);
			 }
			 else if (element_name.equals(IExchangeFieldNames.DESCRIPTION)){
	 			 tmpListBean.setDescription(buffer.toString());
	 			 isDescription = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.TAGLABEL)){
				 tmpListBean.setTagLabel(buffer.toString());
				 isTagLabel = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PARENTLIST)){
				 tmpListBean.setParentList(Integer.parseInt(buffer.toString()));
				 isParentList = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LISTTYPE)){
				 tmpListBean.setListType(Integer.parseInt(buffer.toString()));
				 isListType = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DELETED)){
				 tmpListBean.setDeleted(buffer.toString());
				 isDeleted = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REPOSITORYTYPE)){
				 tmpListBean.setRepositoryType(Integer.parseInt(buffer.toString()));
				 isRepositoryType = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PROJECT)){
				 tmpListBean.setProject(Integer.parseInt(buffer.toString()));
				 isProject = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.OWNER)){
				 tmpListBean.setOwner(Integer.parseInt(buffer.toString()));
				 isOwner = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CHILDNUMBER)){
				 tmpListBean.setChildNumber(Integer.parseInt(buffer.toString()));
				 isChildNumber = false;
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
		if (isTagLabel){
			buffer.append(str);
		}
		if (isParentList){
			buffer.append(str);
		}
		if (isListType){
			buffer.append(str);
		}
		if (isChildNumber){
			buffer.append(str);
		}
		if (isDeleted){
			buffer.append(str);
		}
		if (isRepositoryType){
			buffer.append(str);
		}
		if (isProject){
			buffer.append(str);
		}
		if (isOwner){
			buffer.append(str);
		}
	}

}



