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

public class IssueListViewParser implements DescriptorParser {
	private static final Logger LOGGER = LogManager.getLogger(IssueListViewParser.class);
	public PluginDescriptor createDescriptor(String uri, String localName, String qName, Attributes attributes){
		IssueListViewDescriptor descriptor = new IssueListViewDescriptor();
		descriptor.setName(attributes.getValue("name"));
		descriptor.setId(attributes.getValue("id"));
		descriptor.setTheClassName(attributes.getValue("class"));
		descriptor.setDescription(attributes.getValue("description"));
		descriptor.setJsClass(attributes.getValue("jsClass"));
		descriptor.setJsConfigClass(attributes.getValue("jsConfigClass"));
		descriptor.setIconCls(attributes.getValue("iconCls"));
		
		boolean enabledColumnChoose=true;
		String enabledColumnChooseStr=attributes.getValue("enabledColumnChoose");
		if(enabledColumnChooseStr!=null){
			enabledColumnChoose=Boolean.parseBoolean(enabledColumnChooseStr);
		}
		descriptor.setEnabledColumnChoose(enabledColumnChoose);

		boolean enabledGrouping=true;
		String enabledGroupingStr=attributes.getValue("enabledGrouping");
		if(enabledGroupingStr!=null){
			enabledGrouping=Boolean.parseBoolean(enabledGroupingStr);
		}
		descriptor.setEnabledGrouping(enabledGrouping);

		boolean useLongFields=false;
		String useLongFieldsStr=attributes.getValue("useLongFields");
		if(useLongFieldsStr!=null){
			useLongFields=Boolean.parseBoolean(useLongFieldsStr);
		}
		descriptor.setUseLongFields(useLongFields);

		boolean plainData=false;
		String plainDataStr=attributes.getValue("plainData");
		if(plainDataStr!=null){
			plainData=Boolean.parseBoolean(plainDataStr);
		}
		descriptor.setPlainData(plainData);

		boolean gantt=false;
		String ganttStr=attributes.getValue("gantt");
		if(ganttStr!=null){
			gantt=Boolean.parseBoolean(ganttStr);
		}
		descriptor.setGantt(gantt);
		try {
			Class.forName(descriptor.getTheClassName());
		} catch (Exception e) {
			LOGGER.error("IssueListView Class not found:"+descriptor.getTheClassName());
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





