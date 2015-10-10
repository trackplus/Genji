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

package com.aurel.track.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadUtil {
	
	public static void prepareResponse(HttpServletRequest request,
			HttpServletResponse response, String fileName) {
		prepareResponse(request, response, fileName, null, null, false);
	}
	
	public static void prepareResponse(HttpServletRequest request,
			HttpServletResponse response, String fileName, String contentType) {
		prepareResponse(request, response, fileName, contentType, null, false);
	}
	
	public static void prepareResponse(HttpServletRequest request,
			HttpServletResponse response, String fileName, String contentType, String fileLength) {
		prepareResponse(request, response, fileName, contentType, fileLength, false);
	}
	
	public static void prepareResponse(HttpServletRequest request,
			HttpServletResponse response, String fileName, String contentType, String fileLength, boolean inline) {
		String disposition="inline";
		if (!inline){
			disposition="attachment";
		}
		
		response.reset();
		// setup the http header
		response.setHeader("Pragma", "");
		prepareCacheControlHeader(request, response);
		if (fileName==null) {
			response.setHeader("Content-Disposition", disposition);
		} else {
			response.setHeader("Content-Disposition", disposition+"; filename=\"" + fileName + "\"");
		}		
		if (fileLength!=null) {
			response.setHeader("Content-length", fileLength);
		}
		if (contentType!=null) {
			response.setHeader("Content-Type", contentType);
		}
	}
	
	/**
	 * Prepares the cache control header
	 * @param request
	 * @param response
	 */
	public static void prepareCacheControlHeader(HttpServletRequest request, HttpServletResponse response) {
		DetectBrowser dtb = new DetectBrowser();	
		dtb.setRequest(request);
		//Workaround for a IE bug
		if (dtb.getIsIE()) {
			response.setHeader("Cache-Control", "private");
			response.setHeader("Expires", "0");							
		} else {
			response.setHeader("Cache-Control", "no-cache");			
		}
	}
	
}
