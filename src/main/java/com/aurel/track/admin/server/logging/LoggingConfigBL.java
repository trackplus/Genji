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

package com.aurel.track.admin.server.logging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.aurel.track.StartServlet;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.persist.TLoggingLevelPeer;
import com.aurel.track.util.IntegerStringBean;

/**
 *  The application layer for managing the loggers.
 */
public class LoggingConfigBL {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static List<IntegerStringBean> getLevelList(){
		Level[] alevels = Level.values();
		List<IntegerStringBean> levelList = new ArrayList<IntegerStringBean>();
		Map <Integer,Level> levelMap = new TreeMap<Integer,Level>();
		for (int i=0; i < alevels.length; ++i) {
			levelMap.put(alevels[i].intLevel(), alevels[i]);
		}
		for (Map.Entry<Integer, Level> entry : levelMap.entrySet()) {
			levelList.add(toIntegerStringBean(entry.getValue()));
			LOGGER.debug(entry.getValue().name());
		}
		
		return levelList;
	}
	
	/**
	 * Get all loggers that are available.
	 * @param filter a string that must be contained in the class name for the LOGGER to be added to the result
	 * @return a list with LoggingLevelBean objects
	 */
	public static List<LoggingLevelBean> getLoggers(String filter){
		List<LoggingLevelBean> loggers = new ArrayList<LoggingLevelBean>(100);
		LoggingLevelBean llb = null;
		final LoggerContext ctx = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(StartServlet.class.getClassLoader(), false);
        
        Collection<org.apache.logging.log4j.core.Logger> cloggers = ctx.getLoggers();
        
		if (filter != null) {
			filter = filter.toUpperCase();
		}

		for (org.apache.logging.log4j.core.Logger currentLogger: cloggers) {
			llb = new LoggingLevelBean(currentLogger.getName(), currentLogger.getLevel().name(),currentLogger.getLevel().intLevel());
			if (filter == null || "".equals(filter)) {
				loggers.add(llb);
			} else if (filter!= null && llb.getClassName().toUpperCase().contains(filter)) {
				loggers.add(llb);
			}
		}
		Collections.sort(loggers);
		return loggers;
	}

	/**
	 * Encodes a list of LoggingLevelBean(s) into a JSON object.
	 * @param sb the StringBuilder to which the JSON object is appended
	 * @param name the parameter name of this object, like <code>logList: {....}</code>
	 * @param list the list to be encoded
	 * @param last true if this shall be the last parameter for this StringBuilder to close the JSON object
	 */
	public static void encodeJSONLoggerList(StringBuilder sb, String name, List<LoggingLevelBean> list,boolean last){
		sb.append(name).append(":");
		sb.append("[");
		if(list!=null){
			for (Iterator<LoggingLevelBean> iterator = list.iterator(); iterator.hasNext();) {
				LoggingLevelBean log = iterator.next();
				sb.append("{");
				JSONUtility.appendStringValue(sb, "className", log.getClassName());
				JSONUtility.appendStringValue(sb, "level", log.getLevel());
				JSONUtility.appendIntegerValue(sb, "value", log.getValue(),true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		if(!last){
			sb.append(",");
		}
	}

	/**
	 * Save a single LOGGER setting to the database
	 * @param className the class name for this logger
	 * @param level the new logging level
	 * @return
	 */
	public static boolean saveLogging(String className, Level level){
		Logger currentLogger = LogManager.getLogger(className);
		setLevel(currentLogger, level);
		TLoggingLevelPeer.save(currentLogger, level);
		return true;
	}
	
	private static IntegerStringBean toIntegerStringBean(Level level){
		return new IntegerStringBean(level.name(), level.intLevel());
	}
	
	private static Map<String,Level> levelMap = new HashMap<String,Level>();
	private static boolean isInitialized = false;
	
	public static Level getMappedLevel(String clientLevel) {
		if (isInitialized == false) {
			initLevelMap();
			isInitialized = true;
		}
		Level level = levelMap.get(clientLevel);
		if (level == null) level = Level.OFF;
		return level;
	}
	
	public static void setLevel(Logger logger, Level level) {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(StartServlet.class.getClassLoader(), false);
		Configuration config = ctx.getConfiguration();

		LoggerConfig loggerConfig = new LoggerConfig();
		loggerConfig.setLevel(level);
		
		config.removeLogger(logger.getName());
		config.addLogger(logger.getName(), loggerConfig);
		
		ctx.updateLoggers(config);
	}
	
	private static void initLevelMap() {
		Level[] alevels = Level.values();
		for (int i=0; i < alevels.length; ++i) {
			levelMap.put(alevels[i].name(), alevels[i]);
		}
	}
		
}
