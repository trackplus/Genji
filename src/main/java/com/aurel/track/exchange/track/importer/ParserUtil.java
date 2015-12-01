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

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

public class ParserUtil {
	
	/**
	 * Transform the sax attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static Map<String, String> getAttributesMap(Attributes attributes) {
    	Map<String, String> attributesMap = new HashMap<String, String>();
    	int length = attributes.getLength();
    	for (int i = 0; i < length; i++) {
			String localName = attributes.getQName(i);
			attributesMap.put(localName, attributes.getValue(localName));
		}
    	return attributesMap;
	}
}
