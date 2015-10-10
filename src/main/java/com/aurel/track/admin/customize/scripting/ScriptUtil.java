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

package com.aurel.track.admin.customize.scripting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ScriptUtil {

	private static final Logger LOGGER = LogManager.getLogger(ScriptUtil.class);
	
	/**
	 * Read the parameter script line by line
	 * @param content
	 * @return
	 */
	public static List<String> getParameterDataLines(String content) {
		List<String> lines = new ArrayList<String>();
	    	if (content!=null) {
	    		BufferedReader reader = new BufferedReader(new StringReader(content));
	    		try {
	    			String line = null;
			    	while ((line = reader.readLine())!=null) {
			    		lines.add(line);
		    		}
	    		} catch(IOException e) {
	    			LOGGER.warn("Getting the line failed with " + e.getMessage(), e);
	    		} finally {
	    			try {
	    				reader.close();
					} catch (IOException e) {
						LOGGER.warn("Closing the stream failed with " + e.getMessage(), e);
					}
	    		}
	    	}
	    return lines;
	}
	
	/**
	 * Gets the ldap groups: groupName and DN
	 * @param pattern
	 * @param comment
	 * @param lines
	 * @return
	 */
	public static Map<String, String> getParameterMap(String pattern, String comment, List<String> lines) {
		if (comment==null) {
			comment = "#";
		}
	    Pattern pt = Pattern.compile(pattern);
	    Matcher mt = null;
	    Map<String, String> map = new HashMap<String, String>();
	    for (String line : lines) {
	    	mt = pt.matcher(line);
	    	if (mt.matches()) {
	    		String key = mt.group(1).trim();
	    		String value = mt.group(2).trim();
	    		if (key!=null && !key.startsWith(comment)) {
	    			 map.put(key,value);
	    			 LOGGER.debug("Key = " + key + ", value = " + value);
	    		 }
	    	 }
	     }
	     return map;
	 }
}
