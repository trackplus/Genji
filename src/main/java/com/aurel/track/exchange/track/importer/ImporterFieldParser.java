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


package com.aurel.track.exchange.track.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.exchange.track.ExchangeFieldNames;

public class ImporterFieldParser extends DefaultHandler {	
	private static final Logger LOGGER = LogManager.getLogger(ImporterFieldParser.class);
	private List<ISerializableLabelBean> fieldBeans = new ArrayList<ISerializableLabelBean>();    
	
	
	public List<ISerializableLabelBean> parse(File xml) {		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			//parse the file and also register this class for call backs 
			LOGGER.debug("Field parser started...");
            sp.parse(xml, this);
            LOGGER.debug("Field parser done...");
            return fieldBeans;
		}catch(SAXException se) {
			LOGGER.error(ExceptionUtils.getStackTrace(se));
		}catch(ParserConfigurationException pce) {
			LOGGER.error(ExceptionUtils.getStackTrace(pce));
		}catch (IOException ie) {
			LOGGER.error(ExceptionUtils.getStackTrace(ie));
		}
		return null;
	}
	
    //Event Handlers
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {                
        if(qName.equalsIgnoreCase(ExchangeFieldNames.FIELD) /*||
        		qName.equalsIgnoreCase(ExchangeFieldNames.FIELDCONFIG)*/) { 
        	addLableBean(fieldBeans, attributes, qName);        	 
        }                        
	}
    
    private void addLableBean(List<ISerializableLabelBean> fieldBeans, Attributes attributes, String name) {    	
    	Map<String, String> attributesMap = ParserUtil.getAttributesMap(attributes);
    	ISerializableLabelBean labelBean = null;
    	
    	if (name==ExchangeFieldNames.FIELD) {
    		labelBean = new TFieldBean().deserializeBean(attributesMap);
    	}
    	if (labelBean==null) {
    		LOGGER.warn("Deserialized labelBean is null for " + name);
    	} else {	    		    	
    		fieldBeans.add(labelBean);
    	}
    	
    }
        
}
