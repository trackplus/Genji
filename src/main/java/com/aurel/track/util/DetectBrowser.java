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
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public final class DetectBrowser implements Serializable {
	
	private String userAgent = null;
	private boolean ie = false;
	private boolean ns4 = false;
	private boolean ns6 = false;
	private boolean gecko = false;
	private boolean opera = false;
	private static final long serialVersionUID = 1;

	/*
	  this is only for a .net expansion needful
	  private boolean netEnabled = false;
	*/

	private static final Logger LOGGER = LogManager.getLogger(DetectBrowser.class);

	public void setRequest(HttpServletRequest request) {
		userAgent = request.getHeader("User-Agent");
		if (userAgent == null) userAgent = "unknown";
		LOGGER.debug("User agent is >" + userAgent + "<");

		String agent = userAgent.toLowerCase();
		ns4 = true;
		ie = false;
		ns6 = false;
		gecko = false;
		opera = false;

		if(agent.indexOf("msie") != -1) {
			// Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 4.0; T31246
			ie = true;
			ns4 = false;
			ns6 = false;
			gecko = false;
			opera = false;
		} else if(agent.indexOf("gecko") != -1) { // Netscape 6.1 and Mozilla
			// Mozilla/5.0 (Windows; U; WinNT4.0; de-DE; rv:0.9.2) 
			// Gecko /20010726 Netscape6/6.1<
			ns6 = true;
			gecko = true;
			ie = false;
			ns4 = false;
			opera = false;
		} else if(agent.indexOf("mozilla/4") != -1) { // Netscape 4.7
			// Mozilla/4.7 [de] (WinNT; U)
			// Mozilla/4.76 (Windows NT 4.0; U) Opera 5.12  [de]
			ns4 = true;
			ie = false;
			ns6 = false;
			gecko = false;
			opera = false;
		} else if(agent.indexOf("opera") != -1) { // Opera			
			ns4 = false;
			ie = false;
			ns6 = false;
			gecko = false;
			opera = true;
		}
			

		/*
		  this is only for a .net expansion needful

		  if(user.indexOf(".net clr") != -1)
		  netEnabled = true;
		*/
	}
	
	public String getUserAgent() {
		return userAgent;
	}
	
	public boolean getIsIE() {
		return ie;
	}

	public boolean getIsNS4() {
		return ns4;
	}

	public boolean getIsNS6() {
		return ns6;
	}

	public boolean getIsGecko() {
		return gecko;
	}
	
	public boolean getIsOpera() {
		return opera;
	}

	/*
	  this is only for a .net expansion needful

	  public boolean isNetEnabled() {
	  return netEnabled;
	  }
	  
	  
	*/	
}
