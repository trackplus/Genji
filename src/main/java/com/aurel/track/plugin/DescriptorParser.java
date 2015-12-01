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

/**
 *
 */
public interface DescriptorParser {
	public interface ATTRIBUTES {
		public static String ID = "id";
		public static String NAME = "name";
		public static String CLASS = "class";
		public static String BUNDLE_NAME = "bundleName";
		public static String DESCRIPTION = "description";
		public static String LISTING = "listing";
		public static String PREVIEWIMG = "preview-gif";		
		public static String JS_CLASS = "jsClass";
		public static String JS_CONFIG_CLASS = "jsConfigClass";
		public static String PERSON = "person";
		public static String PROJECT = "project";		
	}
	PluginDescriptor createDescriptor(String uri, String localName, String qName, Attributes attributes);
	public Object handleInternalElement(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, Attributes attributes);
	public void setInternalElementValue(PluginDescriptor descriptor,Object parent, String uri, String localName, String qName, String value);
}
