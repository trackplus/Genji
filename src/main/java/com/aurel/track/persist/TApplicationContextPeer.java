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

package com.aurel.track.persist;


/**
 *  Manages the single table entry for the application context that
 *  can be shared among different computers of a cluster
 */
public class TApplicationContextPeer
    extends com.aurel.track.persist.BaseTApplicationContextPeer
{
    //private static final Logger LOGGER = LogManager.getLogger(TApplicationContextPeer.class);
    
	public static final long serialVersionUID = 400L;
	
    /*public static TApplicationContext loadContext()  {
    	List contexts = null;
    	TApplicationContext context = null;
    	
        try
        {
        	contexts = doSelect(new Criteria());
        	if (contexts == null || contexts.isEmpty()) {
        		context = new TApplicationContext(); // create the one and only
               	context.setLoggedLimitedUsers(new Integer(0));
                context.setLoggedFullUsers(new Integer(0));
        		context.save();
            	contexts = doSelect(new Criteria()); // and again        		
        	}
        	Iterator it = contexts.iterator();
        	if (it.hasNext()) {
        		context = (TApplicationContext)it.next(); // take the one and only...
        	}
        }
        catch(Exception e)
		{
			LOGGER.error("Loading context failed with " + e.getMessage(), e);
		}
        return context;
	}*/
}
