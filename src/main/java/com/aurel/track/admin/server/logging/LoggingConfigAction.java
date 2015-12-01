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


package com.aurel.track.admin.server.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.Support;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
* Implementation of <strong>LoggingConfigAction</strong> that processes the
* logging level configuration requests
*/
public class LoggingConfigAction extends ActionSupport implements Preparable, SessionAware{

	private static final long serialVersionUID = 390L;

	private static final Logger LOGGER = LogManager.getLogger(LoggingConfigAction.class);
	private static final Logger CLOGGER = LogManager.getLogger("CLIENT");
	private static final Logger ROOTLOGGER = LogManager.getRootLogger();
	private Map<String, Object> session;

	private String maxFileSize = "100KB";
	private Integer maxNoOfBackups = 1;
	private String fileName = "";

	private String className;
	private Level level;
	private Map<Integer, Level> levels = new HashMap<Integer, Level> () {};

	/**
	 *
	 */
	@Override
	public void prepare() throws Exception {
	}


	/**
	 * Loads the loggable classes into a JSON object.
	 * @return nothing, output is directly written into stream.
	 */

	public String load () {
		LOGGER.debug("\n\r\n\r\n\r");
		LOGGER.debug("LoggingConfigAction:  Processing load");

		// Get the currently logged on user (is he administrator?)
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		if(user == null || !user.getIsSysAdmin()) {
			// just throw her to the logon page
			return "logon";
		}
		List<IntegerStringBean> levels=LoggingConfigBL.getLevelList();
		List<LoggingLevelBean> loggers=LoggingConfigBL.getLoggers(filter);

		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append("data:{");
		JSONUtility.appendIntegerStringBeanList(sb, "levels", levels);
		LoggingConfigBL.encodeJSONLoggerList(sb,"loggers",loggers,true);
		sb.append("}");
		sb.append("}");
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			LOGGER.error(e);
		}
		return null;
	}



	/**
	 * Saves the new configuration to the database.
	 * @return nothing, output is directly written to the output stream
	 */
	public String save (){

		LOGGER.debug("\n\r\n\r\n\r");
		LOGGER.debug("Processing LoggingConfigAction.save()");

		// Get the currently logged on user (is she administrator?)
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);

		if(user == null || !user.getIsSysAdmin()) {
			JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),"No sys admin",1);
			return null;
		}
		LoggingConfigBL.saveLogging(className, level);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;

	}

	/**
	 * Logs information from the browser to the regular logger
	 */
	public String logClient (){
		LoggingConfigBL.setLevel(LOGGER,Level.ALL);
		Date date = new Date(getTimeStampClient());
		CLOGGER.log(LoggingConfigBL.getMappedLevel(getLevelClient()),DateTimeUtils.getInstance().formatISODateTime(date) + ": " + getMessageClient());
		return null;
	}

	/**
	 * @param session the session to set
	 */
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	/**
	 * @return the application
	 */

	/**
	 * @param application the application to set
	 */

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getLevel() {
		return level.intLevel();
	}


	public void setRollingFileName(String theFileName) {
		this.fileName = theFileName;
	}

	public String getRollingFileName() {
		return this.fileName;
	}

	public void setNoOfBackupFiles(Integer noOfBacks) {
		this.maxNoOfBackups = noOfBacks;
	}

	public Integer getNoOfBackupFiles() {
		return this.maxNoOfBackups;
	}

	public void setMaxBackupFileSize(String maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public String getMaxBackupFileSize() {
		return this.maxFileSize;
	}

	public void setLevel(int ilevel) {
		Level[] alevels = Level.values();
		for (int i=0; i < alevels.length; ++i) {
			if (ilevel == alevels[i].intLevel()) {
				this.level = alevels[i];
				break;
			}
		}
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	private String filter;

	/*
	 * The parameters from the client logger
	 */
	private String loggerClient;
	private Long timeStampClient;
	private String levelClient;
	private String messageClient;
	private String exceptionClient;
	private String urlClient;

	public String getLoggerClient() {
		return loggerClient;
	}

	public void setLoggerClient(String loggerClient) {
		this.loggerClient = loggerClient;
	}

	public Long getTimeStampClient() {
		return timeStampClient;
	}

	public void setTimeStampClient(Long timeStampClient) {
		this.timeStampClient = timeStampClient;
	}

	public String getLevelClient() {
		return levelClient;
	}

	public void setLevelClient(String levelClient) {
		this.levelClient = levelClient;
	}

	public String getMessageClient() {
		return messageClient;
	}

	public void setMessageClient(String messageClient) {
		this.messageClient = messageClient;
	}

	public String getExceptionClient() {
		return exceptionClient;
	}

	public void setExceptionClient(String exceptionClient) {
		this.exceptionClient = exceptionClient;
	}

	public String getUrlClient() {
		return urlClient;
	}

	public void setUrlClient(String urlClient) {
		this.urlClient = urlClient;
	}

}
