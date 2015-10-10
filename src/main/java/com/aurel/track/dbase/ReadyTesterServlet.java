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

package com.aurel.track.dbase;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.ApplicationStarter;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.ResourceBundleManager;

/**
 */
public class ReadyTesterServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 500L;
	private static final Logger LOGGER = LogManager.getLogger(ReadyTesterServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		prepareResponse(resp);
		PrintWriter out = resp.getWriter();
		Enumeration<Locale> locales = req.getLocales();
		execute(out, locales);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		prepareResponse(resp);
		PrintWriter out = resp.getWriter();
		Enumeration<Locale> locales = req.getLocales();
		execute(out, locales);
	}
	
	private void prepareResponse(HttpServletResponse httpServletResponse){
		try {
			httpServletResponse.reset();
			httpServletResponse.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			//when the <s:action/> is present on the jsp
			//(mainly getting root nodes the same response object is used, and second time it can't be reseted)
		}
		httpServletResponse.setContentType("application/json");
	}

	public String execute(PrintWriter out, Enumeration<Locale> locales){
		StringBuilder sb=new StringBuilder();
		//Locale locale = LocaleHandler.getExistingLocale(locales);
		ResourceBundle rb = ResourceBundleManager.getLoaderResourceBundle(locales);
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, "success", true);
		sb.append("data:{");
		Boolean ready = (Boolean) getServletContext().getAttribute(ApplicationStarter.READY);
		Integer percentComplete=(Integer) getServletContext().getAttribute(ApplicationStarter.PERCENT_COMPLETE);
		String progressText=(String) getServletContext().getAttribute(ApplicationStarter.PROGRESS_TEXT);
		if(progressText==null){				
			try {
				progressText= rb.getString("progressText"); // "Progress "+"...";
			}catch(Exception ex) {				
			}			
		}
		if(percentComplete==null){
			percentComplete=1;
		}
		
		JSONUtility.appendIntegerValue(sb,"percentComplete",percentComplete);
		JSONUtility.appendStringValue(sb,"progressText",progressText);
		
		JSONUtility.appendStringValue(sb,"progress100",rb.getString("progress100"));
		JSONUtility.appendStringValue(sb,"progressPercent",rb.getString("progressPercent"));
		JSONUtility.appendStringValue(sb,"redirect",rb.getString("redirect"));
		JSONUtility.appendStringValue(sb,"initializing",rb.getString("initializing"));

		
		if(ready!=null){
			JSONUtility.appendBooleanValue(sb,"ready",ready,true);
		}else{
			JSONUtility.appendBooleanValue(sb,"ready",false,true);
		}
		sb.append("}}");
		try {
			out.println(sb);
		} catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}

		return null;
	}
}
