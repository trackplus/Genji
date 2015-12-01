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

import org.xml.sax.Attributes;

public class ModuleParser implements DescriptorParser {
	@Override
	public PluginDescriptor createDescriptor(String uri, String localName, String qName, Attributes attributes){
		ModuleDescriptor descriptor = new ModuleDescriptor();
		String useHeaderStr=attributes.getValue("useHeader");
		boolean useHeader=false;
		if(useHeaderStr!=null){
			useHeader=useHeaderStr.equalsIgnoreCase("true");
		}
		descriptor.setUseHeader(useHeader);

		String haveIE9CssStr=attributes.getValue("haveIE9Css");
		boolean haveIE9Css=false;
		if(haveIE9CssStr!=null){
			haveIE9Css=haveIE9CssStr.equalsIgnoreCase("true");
		}
		descriptor.setHaveIE9Css(haveIE9Css);

		descriptor.setName(attributes.getValue("name"));
		descriptor.setId(attributes.getValue("id"));
		descriptor.setDescription(attributes.getValue("description"));
		descriptor.setIconCls(attributes.getValue("iconCls"));
		descriptor.setUrl(attributes.getValue("url"));
		return descriptor;
	}
	@Override
	public Object handleInternalElement(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, Attributes attributes){
		return null;
	}
	@Override
	public void setInternalElementValue(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, String value){
	}
}
