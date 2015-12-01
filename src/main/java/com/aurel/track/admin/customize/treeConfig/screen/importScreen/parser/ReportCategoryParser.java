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
import com.aurel.track.beans.TReportCategoryBean;

/**
 * TReportCategory xml parser
 * Can parse also Hierarchical TReportCategory xml structures
 * @author Andras
 *
 */

public class ReportCategoryParser extends ParserFactory //implements IParser  
{
	private static final Logger LOGGER = LogManager.getLogger(ReportCategoryParser.class);
	private TReportCategoryBean tmpCategoryBean;
	private List<TReportCategoryBean> reportCategoryBeans;
	private int reportCategoryCount = 0;
	
	
    private boolean isReportCategory = false;
    private boolean isSub = false;
    private boolean isLabel = false;
    private boolean isRepository = false;
    private boolean isCreatedBy= false;
    private boolean isProject= false;
    private boolean isParentID= false;

    
    private StringBuffer buffer ;
    
	public List<TReportCategoryBean> parse()
	{	
		reportCategoryBeans = new ArrayList<TReportCategoryBean>();
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
		 
		 for (TReportCategoryBean l : reportCategoryBeans)
		 {
			 System.out.println("label =" + l.getLabel());
		 }
		 
		 return reportCategoryBeans;
	}
	  
    
	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		
		 buffer = new StringBuffer();
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.REPORTCATEGORY.equals(attributes.getValue("type"))) {
				 reportCategoryCount++;
				 tmpCategoryBean = new TReportCategoryBean();
				 tmpCategoryBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpCategoryBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isReportCategory = true;
				 isSub = false;
			 }
			 else {
				 isSub = true;
			 }
		 }
		 if (isReportCategory && !isSub)
		 {
			 if (element_name.equals(IExchangeFieldNames.LABEL)){
				 isLabel = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REPOSITORY)){
				 isRepository = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CREATEDBY)){
				 isCreatedBy = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PROJECT)){
				 isProject = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PARENTID)){
				 isParentID = true;
			 }
		 }
	 }
	    
	 @Override
	public void endElement(String uri, String localName, 
			 String element_name) throws SAXException
	 {
		 
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isReportCategory){
			 reportCategoryCount--;
			 if (reportCategoryCount == 0)
				 isReportCategory = false;
		 }
		 if (isReportCategory && !isSub)
		 {	 
			 if (element_name.equals(IExchangeFieldNames.LABEL)){
				 tmpCategoryBean.setLabel(buffer.toString());
				 reportCategoryBeans.add(tmpCategoryBean);
				 isLabel = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REPOSITORY)){
				 tmpCategoryBean.setRepository(Integer.parseInt(buffer.toString()));
				 isRepository = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CREATEDBY)){
				 tmpCategoryBean.setCreatedBy(Integer.parseInt(buffer.toString()));
				 isCreatedBy = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PROJECT)){
				 tmpCategoryBean.setProject(Integer.parseInt(buffer.toString()));
				 isProject = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PARENTID)){
				 tmpCategoryBean.setParentID(Integer.parseInt(buffer.toString()));
				 isParentID = false;
			 }
		 }
	 }
	 
	@Override
	public void characters(char[] ch, int start, int len) throws SAXException{
		String str = new String (ch, start, len);
		
		if (isLabel){
			System.out.println(str);
			buffer.append(str);
		}
		if (isRepository){
			buffer.append(str);
		}
		if (isCreatedBy){
			buffer.append(str);
		}
		if (isProject){
			buffer.append(str);
		}
		if (isParentID){
			buffer.append(str);
		}
	}

}



