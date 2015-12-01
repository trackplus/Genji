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

// The following import can be changed to when moving to Java 5.0
// The library is contained in file backport-util-concurrent
// TODO for Java 5.0
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 4225 $
 *
 */
public class SimpleCachePool {

    public static Object get(String id) {
        return _instance._get(id);
    }

    public static void put(String id, Object obj) {
        _instance._put(id, obj);
    }

    public static Object remove(String id) {
        return _instance._remove(id);
    }

    private SimpleCachePool() {
        _pool = new ConcurrentHashMap(_SIZE);
    }

    private Object _get(String id) {
        return _pool.get(id);
    }

    private void _put(String id, Object obj) {
        _pool.put(id, obj);
    }

    private Object _remove(String id) {
        return _pool.remove(id);
    }

    private static SimpleCachePool _instance = new SimpleCachePool();
    private static int _SIZE = 100000;

    private Map _pool;

}
