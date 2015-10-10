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
/**
 * An parser used to obtain the dashboard descriptions from trackplus-plugin.xml 
 * @author Adrian Bojani
 *
 */
public class DashboardParser implements DescriptorParser {
	private static final Logger LOGGER = LogManager.getLogger(DashboardParser.class);
	public PluginDescriptor createDescriptor(String uri, String localName, String qName, Attributes attributes){
		DashboardDescriptor descriptor = new DashboardDescriptor();
		descriptor.setName(attributes.getValue("name"));
		descriptor.setId(attributes.getValue("id"));
		descriptor.setTheClassName(attributes.getValue("class"));
		descriptor.setDescription(attributes.getValue("description"));
		descriptor.setLabel(attributes.getValue("label"));
		
		descriptor.setJsClass(attributes.getValue("jsClass"));

		descriptor.setJsConfigClass(attributes.getValue("jsConfigClass"));
		

		descriptor.setPageDescription(attributes.getValue("pageDescription"));
		
		descriptor.setThumbnail(attributes.getValue("thumbnail"));
		
		String thumbnailRelease=attributes.getValue("thumbnailRelease");
		if(thumbnailRelease!=null&&thumbnailRelease.trim().length()>0){
			descriptor.setThumbnailRelease(thumbnailRelease);
		}else{
			descriptor.setThumbnailRelease(descriptor.getThumbnail());
		}
		String thumbnailProject=attributes.getValue("thumbnailProject");
		if(thumbnailProject!=null&&thumbnailProject.trim().length()>0){
			descriptor.setThumbnailProject(thumbnailProject);
		}else{
			descriptor.setThumbnailProject(descriptor.getThumbnail());
		}
		
		descriptor.setTooltip(attributes.getValue("tooltip"));
		descriptor.setBundleName(attributes.getValue("bundleName"));

		try {
			Class.forName(descriptor.getTheClassName());
		} catch (Exception e) {
			LOGGER.error("Dashboard Class not found:"+descriptor.getTheClassName());
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




