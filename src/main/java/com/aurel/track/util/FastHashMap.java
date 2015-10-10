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


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * <p>A customized implementation of <code>java.util.HashMap</code> designed
 * to operate in a multithreaded environment where the large majority of
 * method calls are read-only, instead of structural changes.  When operating
 * in "fast" mode, read calls are non-synchronized and write calls perform the
 * following steps:</p>
 * <ul>
 * <li>Clone the existing collection
 * <li>Perform the modification on the clone
 * <li>Replace the existing collection with the (modified) clone
 * </ul>
 * <p>When first created, objects of this class default to "slow" mode, where
 * all accesses of any type are synchronized but no cloning takes place.  This
 * is appropriate for initially populating the collection, followed by a switch
 * to "fast" mode (by calling <code>setFast(true)</code>) after initialization
 * is complete.</p>
 *
 * <p><strong>NOTE</strong>: If you are creating and accessing a
 * <code>HashMap</code> only within a single thread, you should use
 * <code>java.util.HashMap</code> directly (with no synchronization), for
 * maximum performance.</p>
 *
 *
 * @author Craig R. McClanahan
 * @version $Revision: 3938 $ $Date: 2012-09-17 21:25:53 +0200 (Mo, 17 Sep 2012) $
 */

public class FastHashMap extends HashMap {


    // ----------------------------------------------------------- Constructors


	private static final long serialVersionUID = -597567419504669292L;

	/**
     * Construct a an empty map.
     */
    public FastHashMap() {

        super();
        this.map = new HashMap();

    }


    /**
     * Construct an empty map with the specified capacity.
     *
     * @param capacity The initial capacity of the empty map
     */
    public FastHashMap(int capacity) {

        super();
        this.map = new HashMap(capacity);

    }


    /**
     * Construct an empty map with the specified capacity and load factor.
     *
     * @param capacity The initial capacity of the empty map
     * @param factor The load factor of the new map
     */
    public FastHashMap(int capacity, float factor) {

        super();
        this.map = new HashMap(capacity, factor);

    }


    /**
     * Construct a new map with the same mappings as the specified map.
     *
     * @param map The map whose mappings are to be copied
     */
    public FastHashMap(Map map) {

        super();
        this.map = new HashMap(map);

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The underlying map we are managing.
     */
    protected HashMap map = null;


    // ------------------------------------------------------------- Properties


    /**
     * Are we operating in "fast" mode?
     */
    protected boolean fast = false;

    public boolean getFast() {
        return (this.fast);
    }

    public void setFast(boolean fast) {
        this.fast = fast;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Remove all mappings from this map.
     */
    @Override
	public void clear() {

        if (fast) {
            synchronized (this) {
                HashMap temp = (HashMap) map.clone();
                temp.clear();
                map = temp;
            }
        } else {
            synchronized (map) {
                map.clear();
            }
        }

    }


    /**
     * Return a shallow copy of this <code>FastHashMap</code> instance.
     * The keys and values themselves are not copied.
     */
    @Override
	public Object clone() {

        FastHashMap results = null;
        if (fast) {
            results = new FastHashMap(map);
        } else {
            synchronized (map) {
                results = new FastHashMap(map);
            }
        }
        results.setFast(getFast());
        return (results);

    }


    /**
     * Return <code>true</code> if this map contains a mapping for the
     * specified key.
     *
     * @param key Key to be searched for
     */
    @Override
	public boolean containsKey(Object key) {

        if (fast) {
            return (map.containsKey(key));
        } else {
            synchronized (map) {
                return (map.containsKey(key));
            }
        }

    }


    /**
     * Return <code>true</code> if this map contains one or more keys mapping
     * to the specified value.
     *
     * @param value Value to be searched for
     */
    @Override
	public boolean containsValue(Object value) {

        if (fast) {
            return (map.containsValue(value));
        } else {
            synchronized (map) {
                return (map.containsValue(value));
            }
        }

    }


    /**
     * Return a collection view of the mappings contained in this map.  Each
     * element in the returned collection is a <code>Map.Entry</code>.
     */
    @Override
	public Set entrySet() {

        if (fast) {
            return (map.entrySet());
        } else {
            synchronized (map) {
                return (map.entrySet());
            }
        }

    }


    /**
     * Compare the specified object with this list for equality.  This
     * implementation uses exactly the code that is used to define the
     * list equals function in the documentation for the
     * <code>Map.equals</code> method.
     *
     * @param o Object to be compared to this list
     */
    @Override
	public boolean equals(Object o) {

        // Simple tests that require no synchronization
        if (o == this)
            return (true);
        else if (!(o instanceof Map))
            return (false);
        Map mo = (Map) o;

        // Compare the two maps for equality
        if (fast) {
            if (mo.size() != map.size())
                return (false);
            java.util.Iterator i = map.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry e = (Map.Entry) i.next();
                Object key = e.getKey();
                Object value = e.getValue();
                if (value == null) {
                    if (!(mo.get(key) == null && mo.containsKey(key)))
                        return (false);
                } else {
                    if (!value.equals(mo.get(key)))
                        return (false);
                }
            }
            return (true);
        } else {
            synchronized (map) {
                if (mo.size() != map.size())
                    return (false);
                java.util.Iterator i = map.entrySet().iterator();
                while (i.hasNext()) {
                    Map.Entry e = (Map.Entry) i.next();
                    Object key = e.getKey();
                    Object value = e.getValue();
                    if (value == null) {
                        if (!(mo.get(key) == null && mo.containsKey(key)))
                            return (false);
                    } else {
                        if (!value.equals(mo.get(key)))
                            return (false);
                    }
                }
                return (true);
            }
        }

    }


    /**
     * Return the value to which this map maps the specified key.  Returns
     * <code>null</code> if the map contains no mapping for this key, or if
     * there is a mapping with a value of <code>null</code>.  Use the
     * <code>containsKey()</code> method to disambiguate these cases.
     *
     * @param key Key whose value is to be returned
     */
    @Override
	public Object get(Object key) {

        if (fast) {
            return (map.get(key));
        } else {
            synchronized (map) {
                return (map.get(key));
            }
        }

    }


    /**
     * Return the hash code value for this map.  This implementation uses
     * exactly the code that is used to define the list hash function in the
     * documentation for the <code>Map.hashCode</code> method.
     */
    @Override
	public int hashCode() {

        if (fast) {
            int h = 0;
            java.util.Iterator i = map.entrySet().iterator();
            while (i.hasNext())
                h += i.next().hashCode();
            return (h);
        } else {
            synchronized (map) {
                int h = 0;
                java.util.Iterator i = map.entrySet().iterator();
                while (i.hasNext())
                    h += i.next().hashCode();
                return (h);
            }
        }

    }


    /**
     * Return <code>true</code> if this map contains no mappings.
     */
    @Override
	public boolean isEmpty() {

        if (fast) {
            return (map.isEmpty());
        } else {
            synchronized (map) {
                return (map.isEmpty());
            }
        }

    }


    /**
     * Return a set view of the keys contained in this map.
     */
    @Override
	public Set keySet() {

        if (fast) {
            return (map.keySet());
        } else {
            synchronized (map) {
                return (map.keySet());
            }
        }

    }


    /**
     * Associate the specified value with the specified key in this map.
     * If the map previously contained a mapping for this key, the old
     * value is replaced and returned.
     *
     * @param key The key with which the value is to be associated
     * @param value The value to be associated with this key
     */
    @Override
	public Object put(Object key, Object value) {

        if (fast) {
            synchronized (this) {
                HashMap temp = (HashMap) map.clone();
                Object result = temp.put(key, value);
                map = temp;
                return (result);
            }
        } else {
            synchronized (map) {
                return (map.put(key, value));
            }
        }

    }


    /**
     * Copy all of the mappings from the specified map to this one, replacing
     * any mappings with the same keys.
     *
     * @param in Map whose mappings are to be copied
     */
    @Override
	public void putAll(Map in) {

        if (fast) {
            synchronized (this) {
                HashMap temp = (HashMap) map.clone();
                temp.putAll(in);
                map = temp;
            }
        } else {
            synchronized (map) {
                map.putAll(in);
            }
        }

    }


    /**
     * Remove any mapping for this key, and return any previously
     * mapped value.
     *
     * @param key Key whose mapping is to be removed
     */
    @Override
	public Object remove(Object key) {

        if (fast) {
            synchronized (this) {
                HashMap temp = (HashMap) map.clone();
                Object result = temp.remove(key);
                map = temp;
                return (result);
            }
        } else {
            synchronized (map) {
                return (map.remove(key));
            }
        }

    }


    /**
     * Return the number of key-value mappings in this map.
     */
    @Override
	public int size() {

        if (fast) {
            return (map.size());
        } else {
            synchronized (map) {
                return (map.size());
            }
        }

    }


    /**
     * Return a collection view of the values contained in this map.
     */
    @Override
	public Collection values() {

        if (fast) {
            return (map.values());
        } else {
            synchronized (map) {
                return (map.values());
            }
        }

    }


}
