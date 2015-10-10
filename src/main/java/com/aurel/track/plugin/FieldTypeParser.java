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


package com.aurel.track.plugin;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.Attributes;

public class FieldTypeParser implements DescriptorParser {
	private static final Logger LOGGER = LogManager.getLogger(FieldTypeParser.class);
    public PluginDescriptor createDescriptor(String uri, String localName, String qName, Attributes attributes){
    	FieldTypeDescriptor descriptor = new FieldTypeDescriptor();    	    	        
        descriptor.setId(attributes.getValue("id"));
        descriptor.setName(attributes.getValue("name"));        
        descriptor.setTheClassName(attributes.getValue("class"));
        descriptor.setBundleName(attributes.getValue("bundleName"));
        descriptor.setDescription(attributes.getValue("description"));
        descriptor.setLabel(attributes.getValue("label"));
        descriptor.setTooltip(attributes.getValue("tooltip"));
        descriptor.setJsConfigClass(attributes.getValue("jsConfigClass"));
        try {
			Class.forName(descriptor.getTheClassName());
		} catch (Exception e) {
			LOGGER.error("FieldType class not found:"+descriptor.getTheClassName());
			return null;
		}
       return descriptor;
    }
    public Object handleInternalElement(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, Attributes attributes){
    	return null;
    }
    public void setInternalElementValue(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, String value){
    }
}
