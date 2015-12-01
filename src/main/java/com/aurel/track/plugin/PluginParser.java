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


package com.aurel.track.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PluginParser extends DefaultHandler {
	private static final Logger LOGGER = LogManager.getLogger(PluginParser.class);
	private Map<String, List<PluginDescriptor>> pluginDescriptors;
	private Map<String, DescriptorParser> descriptorParserMap;

	//to maintain context
	private PluginDescriptor currentDescriptor;
	private DescriptorParser currentDescriptorParser;
	private Object currentInternalObject;
	private String tempVal;

	private PluginParser(Map<String, DescriptorParser> descriptorMappings){
		pluginDescriptors=new HashMap<String, List<PluginDescriptor>>();
		this.descriptorParserMap=descriptorMappings;
	}

	public static Map<String, List<PluginDescriptor>> parseDocument(InputStream is,Map<String, DescriptorParser> descriptorMappings){
		PluginParser parser=new PluginParser(descriptorMappings);
		parser.parse(is);
		return parser.getPluginDescriptors();
	}

	public Map<String, List<PluginDescriptor>> getPluginDescriptors() {
		return pluginDescriptors;
	}

	private void parse(InputStream is) {
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse the file and also register this class for call backs
			sp.parse(is, this);
		}catch(SAXException se) {
			LOGGER.error(ExceptionUtils.getStackTrace(se));
		}catch(ParserConfigurationException pce) {
			LOGGER.error(ExceptionUtils.getStackTrace(pce));
		}catch (IOException ie) {
			LOGGER.error(ExceptionUtils.getStackTrace(ie));
		}
	}
	//Event Handlers
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		DescriptorParser descriptorParser= descriptorParserMap.get(qName);
		LOGGER.debug("StartElement name: " + qName + ", DescriptorParser: " + descriptorParser);
		if(descriptorParser!=null){
			currentDescriptor=descriptorParser.createDescriptor(uri,localName,qName, attributes);
			currentInternalObject=null;
			currentDescriptorParser=descriptorParser;
		}else{
			if(currentDescriptor!=null&&currentDescriptorParser!=null){
				Object x=currentDescriptorParser.handleInternalElement(currentDescriptor, currentInternalObject, uri, localName, qName, attributes);
				if(x!=null){
					currentInternalObject=x;
				}
			}
		}
	}
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}


	@Override
	public void endElement(String uri, String localName, String qName) {
		DescriptorParser descriptorParser= descriptorParserMap.get(qName);
		if(descriptorParser!=null){
			List<PluginDescriptor> list= pluginDescriptors.get(qName);
			if(list==null){
				list=new ArrayList();
			}
			if(currentDescriptor!=null){
				list.add(currentDescriptor);
			}
			pluginDescriptors.put(qName,list);
		}else{
			if(currentDescriptor!=null&&currentDescriptorParser!=null){
				currentDescriptorParser.setInternalElementValue(currentDescriptor, currentInternalObject, uri, localName, qName, tempVal);
			}
		}
	}
}
