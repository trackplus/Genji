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

package com.aurel.track.lucene.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



/**
 * <a href="InstancePool.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1229 $
 *
 */
public class InstancePool {
	
	private static final Logger LOGGER = LogManager.getLogger(InstancePool.class);

    public static Object get(String className) {
        return _instance._get(className);
    }

    public static void put(String className, Object obj) {
        _instance._put(className, obj);
    }

    private InstancePool() {
        _classPool = new ConcurrentHashMap();
    }

    private Object _get(String className) {
        className = className.trim();

        Object obj = _classPool.get(className);

        if (obj == null) {
            try {
                obj = Class.forName(className).newInstance();
                _put(className, obj);
            }
            catch (ClassNotFoundException cnofe) {
                LOGGER.error(ExceptionUtils.getStackTrace(cnofe));
            }
            catch (InstantiationException ie) {
                LOGGER.error(ExceptionUtils.getStackTrace(ie));
            }
            catch (IllegalAccessException iae) {
                LOGGER.error(ExceptionUtils.getStackTrace(iae));
            }
        }

        return obj;
    }

    private void _put(String className, Object obj) {
        _classPool.put(className, obj);
    }

    private static InstancePool _instance = new InstancePool();

    private Map _classPool;

}
