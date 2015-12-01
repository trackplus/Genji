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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TLoggedInUsersBean;
import com.aurel.track.dao.LoggedInUsersDAO;

/**
 *  This class handles book keeping of logged in users. Book keeping is done via
 *  a database table, so to make this function work across several servers in a
 *  cluster.
 */
public class TLoggedInUsersPeer
    extends com.aurel.track.persist.BaseTLoggedInUsersPeer implements LoggedInUsersDAO
{
    private static final Logger LOGGER = LogManager.getLogger(TLoggedInUsersPeer.class);
    
	public static final long serialVersionUID = 400L;
	
	/**
	 * Load all logged in users
	 */
	
	/**
	 * Remove the logged in users by cluster node (shutdown  a cluster node)
	 * @param clusterNodeID
	 */
	
	/**
	 * When a session is created for a user, this user is added to
	 * the list of logged in users.
	 * @param user
	 * @param sessionId
	 */
	
	/**
	 * If a session is terminated, the associated user is removed
	 * from the list of logged in users.
	 * @param sessionId
	 */

	/**
	 * Remove a user by session (logged out)
	 * @param sessions
	 */
	@Override
	public void removeBySession(String sessionID) {
		Criteria crit = new Criteria();
		crit.add(SESSIONID,  sessionID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the user by session with id " + sessionID + " failed with " + e.getMessage());
		}
	}
	/**
	 * @return the number of full users currently logged in.
	 */

	/**
	 * @return the number of limited users currently logged in.
	 */

	/**
	 * @return a HashMap with String session ids as key, and TPersonBean user as value.
	 */
	
	/**
	 * At the end of the servlet life all users with sessions belonging to this servlet 
	 * should be cleared from the logged in user table in the database.
	 * @param sessions
	 */
	
	/**
	 * This will clean out all users that haven't been updated during the last timeout
	 * period plus some grace period of 30 seconds.
	 */
	
	/**
	 * We update the currently active users every so often to see how recent their
	 * session is. This permits us to clean the table from any debris in case
	 * there was an unclean shutdown.
	 * @param sessions
	 */
	
	/**
	 * Update the timestamp for logged in users
	 * @param clusterNodeID
	 * @param date
	 */
	
	/**
	 * Saves an entity changes in the TEntityChanges table.
	 * @param entityChangesBean
	 * @return
	 */
	@Override
	public Integer save(TLoggedInUsersBean loggedInUsersBean) {
		TLoggedInUsers tloggedInUsers;
		try {
			tloggedInUsers = TLoggedInUsers.createTLoggedInUsers(loggedInUsersBean);
			tloggedInUsers.save();
			return tloggedInUsers.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a logged in user failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Deletes logged in user by objectID
	 * @param objectID
	 * @return
	 */
	
}
