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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.Attributes;

/**
 * Datasource descriptor creator for datasources
 * @author Tamas
 *
 */
public class LinkTypeParser implements DescriptorParser {
	
	private static final Logger LOGGER = LogManager.getLogger(LinkTypeParser.class);
   
	/**
	 * Creates a DatasourceDescriptor for each valid datasource element found in the plug-ins
	 * If the datasource element is not valid it is not created (returns null)
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 * 
	 */
	@Override
	public PluginDescriptor createDescriptor(String uri, String localName, String qName, Attributes attributes){
    	LinkTypeDescriptor linkTypeDescriptor = new LinkTypeDescriptor();    	    	
    	linkTypeDescriptor.setName(attributes.getValue(DescriptorParser.ATTRIBUTES.NAME));
        linkTypeDescriptor.setBundleName(attributes.getValue(DescriptorParser.ATTRIBUTES.BUNDLE_NAME));        
        linkTypeDescriptor.setDescription(attributes.getValue(DescriptorParser.ATTRIBUTES.DESCRIPTION));
        //configuration page
        linkTypeDescriptor.setJsClass(attributes.getValue(DescriptorParser.ATTRIBUTES.JS_CLASS));
        
        String linkTypeID = attributes.getValue(DescriptorParser.ATTRIBUTES.ID);
    	if (linkTypeID==null) {
    		//ID of the datasource, it is mandatory. If not present ignore this entry
    		LOGGER.warn("The following linkType " + linkTypeDescriptor.toString() + 
    				" has no ID specified, consequently it will be ignored");
    		return null;
    	} else {
    		linkTypeDescriptor.setId(linkTypeID);
    	}        
        
                 
        //the name of the class implementing the IPluggableDatasouce
        String className = attributes.getValue(DescriptorParser.ATTRIBUTES.CLASS);
        if (className==null) {
        	LOGGER.warn("Linktype " + linkTypeDescriptor.toString() + " will be ignored because no class was specified.");
        		return null;        	
        } else {        
	        linkTypeDescriptor.setTheClassName(className);
	        try {
				Class.forName(linkTypeDescriptor.getTheClassName());
			} catch (Exception e) {
				//the class was specified but not found in the classpath: ignore this entry
				LOGGER.warn("The linkType class " + linkTypeDescriptor.getTheClassName() + 
						" for datasource " + linkTypeDescriptor.toString() + " was not found in the classpath consequently it will be ignored" + e.getMessage(), e);
				return null;
			}
        }
       return linkTypeDescriptor;
    }
	@Override
	public Object handleInternalElement(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, Attributes attributes){
    	return null;
    }
	@Override
	public void setInternalElementValue(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, String value){
    }
}




