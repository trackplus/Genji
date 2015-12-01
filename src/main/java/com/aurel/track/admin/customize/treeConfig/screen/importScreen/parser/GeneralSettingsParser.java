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
import com.aurel.track.beans.TGeneralSettingsBean;


public class GeneralSettingsParser extends ParserFactory //implements IParser
{
	private static final Logger LOGGER = LogManager.getLogger(GeneralSettingsParser.class);
	private TGeneralSettingsBean tmpGeneralSettingsBean;
	private List<TGeneralSettingsBean> generalSettingsBeans;

	private boolean isGeneralSettings = false;
	private boolean isSubGeneralSettings = false;
	private boolean isConfig = false;
    private boolean isIntegerValue = false;
    private boolean isDoubleValue = false;
    private boolean isTextValue = false;
    private boolean isDateValue = false;
    private boolean isCharacterValue = false;
    private boolean isParameterCode = false;
    private boolean isValidValue = false;
    private StringBuffer buffer ;



	public List<TGeneralSettingsBean> parse()
	{
		generalSettingsBeans = new ArrayList<TGeneralSettingsBean>();
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
		 return generalSettingsBeans;
	}




	 @Override
	public void startElement(String uri, String localName,
				 String element_name, Attributes attributes) throws SAXException
	 {
		 buffer = new StringBuffer();
		 if (element_name.equals(IExchangeFieldNames.ITEM)){
			 if (ITableTypes.GENERALSETTINGS.equals(attributes.getValue("type"))) {
				 tmpGeneralSettingsBean = new TGeneralSettingsBean();
				 tmpGeneralSettingsBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpGeneralSettingsBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isGeneralSettings = true;
				 isSubGeneralSettings = false;
			 }
			 else {
				 isSubGeneralSettings = true;
			 }
		 }
		 if (isGeneralSettings && !isSubGeneralSettings)
		 {
			 if ("config".equals(element_name)){
				 isConfig = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.INTEGERVALUE)){
				 isIntegerValue = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DOUBLEVALUE)){
				 isDoubleValue = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.TEXTVALUE)){
				 isTextValue = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DATEVALUE)){
				 isDateValue = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CHARACTERVALUE)){
				 isCharacterValue = true;
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
		 if (element_name.equals(IExchangeFieldNames.ITEM)&& isGeneralSettings){
			 {
				 generalSettingsBeans.add(tmpGeneralSettingsBean);
					isGeneralSettings = false;
			 }
		 }
		 if ( isGeneralSettings && !isSubGeneralSettings) {
			 if ("config".equals(element_name)){
				 tmpGeneralSettingsBean.setConfig(Integer.parseInt(buffer.toString()));
				 isConfig = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.INTEGERVALUE)){
				 tmpGeneralSettingsBean.setIntegerValue(Integer.parseInt(buffer.toString()));
				 isIntegerValue = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DOUBLEVALUE)){
				 tmpGeneralSettingsBean.setDoubleValue(Double.parseDouble(buffer.toString()));
				 isDoubleValue = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.TEXTVALUE)){
				 tmpGeneralSettingsBean.setTextValue(buffer.toString());
				 isTextValue = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DATEVALUE)){
				 try
				 {
					 tmpGeneralSettingsBean.setDateValue(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(buffer.toString()));
				 }
				 catch(ParseException e)
				 {
					 System.out.println("Error="+ e.getMessage());
				 }
				 isDateValue = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CHARACTERVALUE)){
				 tmpGeneralSettingsBean.setCharacterValue(buffer.toString());
				 isCharacterValue = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PARAMETERCODE)){
				 tmpGeneralSettingsBean.setParameterCode(Integer.parseInt(buffer.toString()));
				 isParameterCode = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.VALIDVALUE)){
				 tmpGeneralSettingsBean.setValidValue(Integer.parseInt(buffer.toString()));
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

		if (isIntegerValue){
			buffer.append(str);
		}
		if (isDoubleValue){
			buffer.append(str);
		}
		if (isTextValue){
			buffer.append(str);
		}
		if (isDateValue){
			buffer.append(str);
		}
		if (isCharacterValue){
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



