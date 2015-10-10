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

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.Attributes;

import com.aurel.track.plugin.VersionControlDescriptor.BrowserDescriptor;

/**
 *
 */
public class VersionControlParser implements DescriptorParser {
	private static final Logger LOGGER = LogManager.getLogger(VersionControlParser.class);
	
	public PluginDescriptor createDescriptor(String uri, String localName, String qName, Attributes attributes){
		VersionControlDescriptor descriptor = new VersionControlDescriptor();
		descriptor.setName(attributes.getValue("name"));
		descriptor.setId(attributes.getValue("id"));
		descriptor.setTheClassName(attributes.getValue("class"));
		descriptor.setDescription(attributes.getValue("description"));
		descriptor.setJsConfigClass(attributes.getValue("jsConfigClass"));
		descriptor.setPageDescription(attributes.getValue("pageDescription"));
		descriptor.setBundleName(attributes.getValue("bundleName"));
		//verify the class
		try {
			Class.forName(descriptor.getTheClassName());
		} catch (Exception e) {
			LOGGER.warn("Invalid class name:\""+descriptor.getTheClassName()+"\" for :"+descriptor.getId());
			return null;
		}
		return descriptor;
	}
	public Object handleInternalElement(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, Attributes attributes){
		if(qName.equalsIgnoreCase("browser")){
			if(descriptor instanceof VersionControlDescriptor){
				BrowserDescriptor browser=new BrowserDescriptor();
				browser.setId(attributes.getValue("id"));
				browser.setName(attributes.getValue("name"));
				((VersionControlDescriptor)descriptor).addBrowser(browser);
				return browser;
			}
		}
		return null;
    }
	
	
	public void setInternalElementValue(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, String value){
		if(qName.equalsIgnoreCase("changeset-link")){
			if(parent!=null&&parent instanceof BrowserDescriptor){
				try{
					BrowserDescriptor browser=(BrowserDescriptor) parent;
					browser.setChangesetLink(value);
				}catch (Exception e) {
					//ignore;
				}
			}
		}
		if(qName.equalsIgnoreCase("added-link")){
			if(parent!=null){
				try{
					BrowserDescriptor browser=(BrowserDescriptor) parent;
					browser.setAddedLink(value);
				}catch (Exception e) {
					//ignore;
				}
			}
		}
		if(qName.equalsIgnoreCase("modified-link")){
			if(parent!=null){
				try{
					BrowserDescriptor browser=(BrowserDescriptor) parent;
					browser.setModifiedLink(value);
				}catch (Exception e) {
					//ignore;
				}
			}
		}
		if(qName.equalsIgnoreCase("replaced-link")){
			if(parent!=null){
				try{
					BrowserDescriptor browser=(BrowserDescriptor) parent;
					browser.setReplacedLink(value);
				}catch (Exception e) {
					//ignore;
				}
			}
		}
		if(qName.equalsIgnoreCase("deleted-link")){
			if(parent!=null){
				try{
					BrowserDescriptor browser=(BrowserDescriptor) parent;
					browser.setDeletedLink(value);
				}catch (Exception e) {
					//ignore;
				}
			}
		}
		if(qName.equalsIgnoreCase("baseURL")){
			if(parent!=null){
				try{
					BrowserDescriptor browser=(BrowserDescriptor) parent;
					browser.setBaseURL(value);
				}catch (Exception e) {
					//ignore;
				}
			}
		}
	}
}


