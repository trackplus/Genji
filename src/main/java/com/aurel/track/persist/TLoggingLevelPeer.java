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


package com.aurel.track.persist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.admin.server.logging.LoggingConfigBL;
import com.aurel.track.dao.LoggingLevelDAO;

/**
 *  This class manages the logging level configuration settings.
 *  Initial configuration will be read from log4j.properties file.
 *  Only changed settings will be saved to the database.
 */
public class TLoggingLevelPeer
    extends com.aurel.track.persist.BaseTLoggingLevelPeer
    implements LoggingLevelDAO
{
	private static final long serialVersionUID = 340L;
	private static Map<String,String> storedLoggers = new HashMap<String,String>();
	private static final Logger LOGGER = LogManager.getLogger(TLoggingLevelPeer.class);
	
	/**
	 * 
	 * @param theLogger
	 */
	public static void save(Logger theLogger, Level newLevel) {
		String level = storedLoggers.get(theLogger.getName());
		if (level == null || !level.equals(newLevel.name())) {
			try {
				Criteria crit = new Criteria();
			    crit.add(BaseTLoggingLevelPeer.THECLASSNAME, (Object) theLogger.getName(), Criteria.EQUAL );
			    List<TLoggingLevel> result = doSelect(crit);
			    if (result != null && result.size() > 0) {
			    	Iterator<TLoggingLevel> it = result.iterator();
			    	while (it.hasNext()) {
			    		TLoggingLevel llbean = (TLoggingLevel)it.next();
			    		//Level ll = theLogger.getLevel();
			    		//String ln = ll.name();
			    		llbean.setLogLevel(newLevel.name());
			    		llbean.save();
			    	}
			    }
			    else {
			    	TLoggingLevel llbean = new TLoggingLevel();
			    	llbean.setLogLevel(newLevel.name());
			    	llbean.setTheClassName(theLogger.getName());
			    	llbean.save();
			    }				
			}
			catch (Exception se) {
				LOGGER.error("Problem saving LOGGER level: " + se.getMessage());
			}
		}
		
	}
	
	public static void load() {

		Criteria crit = new Criteria();    
		try {
			List<TLoggingLevel> loggers = doSelect(crit);  // get them all
			Logger currentLogger = null;
			
			if (loggers != null && loggers.size() > 0){
				Iterator<TLoggingLevel> i = loggers.iterator();
				TLoggingLevel llbean = null;
				while (i.hasNext())
				{
					llbean = (TLoggingLevel) i.next();
					
					storedLoggers.put(llbean.getTheClassName(), llbean.getLogLevel());
					
					currentLogger = LogManager.getLogger(llbean.getTheClassName());
					
					if (currentLogger != null) {
						LoggingConfigBL.setLevel(currentLogger,Level.toLevel(llbean.getLogLevel()));
					}
				}
			}
		}
		catch (Exception e) {
			LOGGER.error("Exception trying to load logging level beans: "
					+ e.getMessage(), e);
		}
	}
	

}
