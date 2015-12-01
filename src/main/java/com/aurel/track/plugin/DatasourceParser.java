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
public class DatasourceParser implements DescriptorParser {
	
	private static final Logger LOGGER = LogManager.getLogger(DatasourceParser.class);
   
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
		DatasourceDescriptor datasourceDescripor = new DatasourceDescriptor();				
		//first set the optional fields to load the datasourceDescripor with as much information as possible
		//for the case to identify the wrong datasource element in case of error
		datasourceDescripor.setName(attributes.getValue(DescriptorParser.ATTRIBUTES.NAME));
		datasourceDescripor.setBundleName(attributes.getValue(DescriptorParser.ATTRIBUTES.BUNDLE_NAME));		
		datasourceDescripor.setDescription(attributes.getValue(DescriptorParser.ATTRIBUTES.DESCRIPTION));
		datasourceDescripor.setListing(attributes.getValue(DescriptorParser.ATTRIBUTES.LISTING));
		datasourceDescripor.setPreviewImg(attributes.getValue(DescriptorParser.ATTRIBUTES.PREVIEWIMG));
		//configuration page
		datasourceDescripor.setJsConfigClass(attributes.getValue(DescriptorParser.ATTRIBUTES.JS_CONFIG_CLASS));
		datasourceDescripor.setJsClass(attributes.getValue(DescriptorParser.ATTRIBUTES.JS_CLASS));
		
		String datasourceID = attributes.getValue(DescriptorParser.ATTRIBUTES.ID);
		if (datasourceID==null) {
			//ID of the datasource, it is mandatory. If not present ignore this entry
			LOGGER.warn("The following datasource " + datasourceDescripor.toString() + 
					" has no ID specified, consequently it will be ignored");
			return null;
		} else {
			datasourceDescripor.setId(datasourceID);
		}		
		
		//person is optional but if specified should be an integer
		String strPersonID = attributes.getValue(DescriptorParser.ATTRIBUTES.PERSON);
		if (strPersonID!=null) {
			Integer personID = null;
			try {
				personID = new Integer(strPersonID);
			}
			catch (Exception e) {
				LOGGER.warn("Converting the person to integer at the datasource " + 
						datasourceDescripor.toString() + "  failed with " + e.getMessage(), e);
			}
			if (personID!=null) {
				datasourceDescripor.setPersonID(personID);
			}
		} 
		//project is optional but if specified should be an integer
		String strProjectID = attributes.getValue(DescriptorParser.ATTRIBUTES.PROJECT);
		if (strProjectID!=null) {
			Integer projectID = null;
			try {
				projectID = new Integer(strProjectID);
			}
			catch (Exception e) {
				LOGGER.warn("Converting the project to integer at the datasource " + 
						datasourceDescripor.toString() + " failed with " + e.getMessage(), e);
			}
			if (projectID!=null) {
				datasourceDescripor.setProjectID(projectID);
			}
		}
		//the name of the class implementing the IPluggableDatasouce
		String className = attributes.getValue(DescriptorParser.ATTRIBUTES.CLASS);
		if (className==null) {
			//theoretically is possible: use the default data source (ReportBeans from the session)
			//with a freemarker template. If the template is also missing then ignore this datasource entry 
			if (datasourceDescripor.getJsClass()==null) {
				LOGGER.warn("Neither datasource class no page (template) specified for datasource " + 
						datasourceDescripor.toString() + " consequently it will be ignored");
				return null;
			}
			if (datasourceDescripor.getJsConfigClass()!=null) {
				LOGGER.warn("The jsConfigClass attribute for datasource " +
						datasourceDescripor.toString() + " will be ignored because no class was specified." +
								"No class means implicit datasource which does not need a configuration page");
			}
		} else {		
			datasourceDescripor.setTheClassName(className);
			try {
				Class.forName(datasourceDescripor.getTheClassName());
			} catch (Exception e) {
				//the class was specified but not found in the classpath: ignore this entry
				LOGGER.warn("The datasource class " + datasourceDescripor.getTheClassName() + 
						" for datasource " + datasourceDescripor.toString() + 
						" was not found in the classpath consequently it will be ignored " + e);
				return null;
			}
		}
	   return datasourceDescripor;
	}
	@Override
	public Object handleInternalElement(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, Attributes attributes){
		return null;
	}
	@Override
	public void setInternalElementValue(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, String value){
	}
}
