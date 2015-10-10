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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.aurel.track.beans.TTextBoxSettingsBean;



public class TextBoxSettingsParser extends ParserFactory //implements IParser 
{
	private static final Logger LOGGER = LogManager.getLogger(TextBoxSettingsParser.class);
	private TTextBoxSettingsBean tmpTextBoxSettingsBean;
	private List<TTextBoxSettingsBean> textBoxSettingBeans;
				
	private boolean isTextBoxSettings = false;
	private boolean isSubTextBoxSettings = false;
 	private boolean isConfig = false;
    private boolean isRequired = false;
    private boolean isDefaultText = false;
    private boolean isDefaultInteger = false;
    private boolean isDefaultDouble = false;
    private boolean isDefaultDate = false;
    private boolean isDefaultChar = false;
    private boolean isDefaultOption = false;
    private boolean isMinOption = false;
    private boolean isMaxOption = false;
    private boolean isMinTextLength = false;
    private boolean isMaxTextLength = false;
    private boolean isMinDate = false;
    private boolean isMaxDate = false;
    private boolean isMinInteger = false;
    private boolean isMaxInteger = false;
    private boolean isMinDouble = false;
    private boolean isMaxDouble = false;
    private boolean isMaxDecimalDigit = false;
    private boolean isParameterCode = false;
    private boolean isValidValue = false;  
    private StringBuffer buffer ;
    

	public List<TTextBoxSettingsBean> parse()
	{	
		textBoxSettingBeans = new ArrayList<TTextBoxSettingsBean>();
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
		 
		 return textBoxSettingBeans;
	}
	  

		

	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		 buffer = new StringBuffer();
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.TEXTBOXSETTINGS.equals(attributes.getValue("type"))) {
				 tmpTextBoxSettingsBean = new TTextBoxSettingsBean();
				 tmpTextBoxSettingsBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpTextBoxSettingsBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isTextBoxSettings = true;
				 isSubTextBoxSettings = false;
			 }
			 else {
				 isSubTextBoxSettings = true;
			 }
		 }		
		 if  (isTextBoxSettings && !isSubTextBoxSettings)
		 {
			 if (element_name.equals(IExchangeFieldNames.CONFIG)){
				 isConfig = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REQUIRED)){
				 isRequired = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTTEXT)){
				 isDefaultText = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTINTEGER)){
				 isDefaultInteger = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTDOUBLE)){
				 isDefaultDouble = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTDATE)){
				 isDefaultDate = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTCHAR)){
				 isDefaultChar = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTOPTION)){
				 isDefaultOption = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MINOPTION)){
				 isMinOption = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXOPTION)){
				 isMaxOption = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MINTEXTLENGTH)){
				 isMinTextLength = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXTEXTLENGTH)){
				 isMaxTextLength = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MINDATE)){
				 isMinDate = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXDATE)){
				 isMaxDate = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MININTEGER)){
				 isMinInteger = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXINTEGER)){
				 isMaxInteger = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MINDOUBLE)){
				 isMinDouble = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXDOUBLE)){
				 isMaxDouble = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXDECIMALDIGIT)){
				 isMaxDecimalDigit = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PARAMETERCODE)){
				 isParameterCode = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.VALIDVALUE)){
				 isValidValue = true;
			 }
		 }
	 }
	 
	 @Override
	public void endElement(String uri, String localName, 
			 String element_name) throws SAXException
	 {
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isTextBoxSettings){ 
				 textBoxSettingBeans.add(tmpTextBoxSettingsBean);
				 isTextBoxSettings = false;
		 }
		 if  (isTextBoxSettings && !isSubTextBoxSettings)
		 {
			 if (element_name.equals(IExchangeFieldNames.CONFIG)){
				 tmpTextBoxSettingsBean.setConfig(Integer.parseInt(buffer.toString()));
				 isConfig = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REQUIRED)){
				 tmpTextBoxSettingsBean.setRequired(buffer.toString());
				 isRequired = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTTEXT)){
				 tmpTextBoxSettingsBean.setDefaultText(buffer.toString());
				 isDefaultText = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTINTEGER)){
				 tmpTextBoxSettingsBean.setDefaultInteger(Integer.parseInt(buffer.toString()));
				 isDefaultInteger = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTDOUBLE)){
				 tmpTextBoxSettingsBean.setDefaultDouble(Double.parseDouble(buffer.toString()));
				 isDefaultDouble = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTDATE)){
				 try {
					 tmpTextBoxSettingsBean.setDefaultDate(
				 	new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(buffer.toString()));
				 }
				 catch(Exception e) {
					 LOGGER.debug(e.getMessage());
				 }
				 isDefaultDate = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTCHAR)){
				 tmpTextBoxSettingsBean.setDefaultChar(buffer.toString());
				 isDefaultChar = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEFAULTOPTION)){
				 tmpTextBoxSettingsBean.setDefaultOption(Integer.parseInt(buffer.toString()));
				 isDefaultOption = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MINOPTION)){
				 tmpTextBoxSettingsBean.setMinOption(Integer.parseInt(buffer.toString()));
				 isMinOption = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXOPTION)){
				 tmpTextBoxSettingsBean.setMaxOption(Integer.parseInt(buffer.toString()));
				 isMaxOption = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MINTEXTLENGTH)){
				 tmpTextBoxSettingsBean.setMinTextLength(Integer.parseInt(buffer.toString()));
				 isMinTextLength = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXTEXTLENGTH)){
				 tmpTextBoxSettingsBean.setMaxTextLength(Integer.parseInt(buffer.toString()));
				 isMaxTextLength = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MINDATE)){
				try {
					tmpTextBoxSettingsBean.setMinDate(
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(buffer.toString()));
				}
				catch(ParseException e){
					LOGGER.debug(e.getMessage(), e);
				}
				isMinDate = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXDATE)){
				 try {
					 tmpTextBoxSettingsBean.setMaxDate(
							 new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(buffer.toString()));	
				 }
				 catch(Exception e){
					 LOGGER.debug(e.getMessage(), e);
				 }
				 isMaxDate = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MININTEGER)){
				 tmpTextBoxSettingsBean.setMinInteger(Integer.parseInt(buffer.toString()));
				 isMinInteger = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXINTEGER)){
				 tmpTextBoxSettingsBean.setMaxInteger(Integer.parseInt(buffer.toString()));
				 isMaxInteger = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MINDOUBLE)){
				 tmpTextBoxSettingsBean.setMinDouble(Double.parseDouble(buffer.toString()));
				 isMinDouble = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXDOUBLE)){
				 tmpTextBoxSettingsBean.setMaxDouble(Double.parseDouble(buffer.toString()));
				 isMaxDouble = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXDECIMALDIGIT)){
				 tmpTextBoxSettingsBean.setMaxDecimalDigit(Integer.parseInt(buffer.toString()));
				 isMaxDecimalDigit = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PARAMETERCODE)){
				 tmpTextBoxSettingsBean.setParameterCode(Integer.parseInt(buffer.toString()));
				 isParameterCode = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.VALIDVALUE)){
				 tmpTextBoxSettingsBean.setValidValue(Integer.parseInt(buffer.toString()));
				 isValidValue = false;
			 }
	 	}
	 }

	@Override
	public void characters(char[] ch, int start, int len) throws SAXException{
		String str = new String (ch, start, len);
		
		if (isConfig){
			buffer.append(str);
		}
		if (isRequired){
			buffer.append(str);
		}
		if (isDefaultText){
			buffer.append(str);
		}
		if (isDefaultInteger){
			buffer.append(str);
		}
		if (isDefaultDouble){
			buffer.append(str);
		}
		if (isDefaultDate){
			buffer.append(str);
		}
		if (isDefaultChar){
			buffer.append(str);
		}
		if (isDefaultOption){
			buffer.append(str);
		}
		if (isMinOption){
			buffer.append(str);
		}
		if (isMaxOption){
			buffer.append(str);
		}
		if (isMinTextLength){
			buffer.append(str);
		}
		if (isMaxTextLength){
			buffer.append(str);
		}
		if (isMinDate){
			buffer.append(str);
		}
		if (isMaxDate){
			buffer.append(str);
		}
		if (isMinInteger){
			buffer.append(str);
		}
		if (isMaxInteger){
			buffer.append(str);
		}
		if (isMinDouble){
			buffer.append(str);
		}
		if (isMaxDouble){
			buffer.append(str);
		}
		if (isMaxDecimalDigit){
			buffer.append(str);
		}
		if (isParameterCode){
			buffer.append(str);
		}
		if (isValidValue){
			buffer.append(str);
		}
	}

}



