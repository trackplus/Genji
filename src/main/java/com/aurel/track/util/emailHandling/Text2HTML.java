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


package com.aurel.track.util.emailHandling;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Text2HTML {
	
	public static String addHTMLBreaks(String plainText) {
		if (plainText!=null) {			
			Pattern crlfPattern = Pattern.compile("(\r\n|\r|\n|\n\r)");
			Matcher matcher = crlfPattern.matcher(plainText);			 
			if (matcher.find()) {
				return matcher.replaceAll("<br>");
			}								
		}
		return plainText;
	}		 
}
