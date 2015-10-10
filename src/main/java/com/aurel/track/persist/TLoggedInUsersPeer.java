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
	/*public List<TLoggedInUsersBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Can't delete dead user: " + e.getMessage(), e);
			return null;
		}
	}*/
	
	/**
	 * Remove the logged in users by cluster node (shutdown  a cluster node)
	 * @param clusterNodeID
	 */
	/*public void removeByClusterNode(Integer clusterNodeID) {
		Criteria crit = new Criteria();
		crit.add(NODEADDRESS, clusterNodeID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
            LOGGER.error("Error removing active sessions: " + e.getMessage(), e);
			LOGGER.error(crit.toString());
		}
	}*/
	
	/**
	 * When a session is created for a user, this user is added to
	 * the list of logged in users.
	 * @param user
	 * @param sessionId
	 */
	/*public static void addUser(TPersonBean user, String sessionId) {
		TLoggedInUsers tlu = new TLoggedInUsers();
		int userLevel = 1;
		if (user.isExternal()) {
			userLevel = 0;
		}
		try {
			tlu.setLoggedUser(user.getObjectID());
			tlu.setSessionId(sessionId);
			tlu.setUserLevel(new Integer(userLevel));
			tlu.setLastUpdate(new Date());
			tlu.setNodeAddress(ApplicationBean.getApplicationBean().getClusterNode().getObjectID()); // The primary key of TClusterNode
			tlu.save();
		} catch (Exception e) {
			LOGGER.error("Problem adding user to table TLoggedInUsers: " + e.getMessage(), e);
		}	
	}*/
	
	/**
	 * If a session is terminated, the associated user is removed
	 * from the list of logged in users.
	 * @param sessionId
	 */
	/*public static void removeUser(String sessionId) {
		Criteria crit = new Criteria();
		crit.add(BaseTLoggedInUsersPeer.SESSIONID,  (Object) sessionId, Criteria.EQUAL);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the session with id " + sessionId + " failed with " + e.getMessage(), e);
		}
		return;		
	}*/

	/**
	 * Remove a user by session (logged out)
	 * @param sessions
	 */
	public void removeBySession(String sessionID) {
		Criteria crit = new Criteria();
		crit.add(SESSIONID,  sessionID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the user by session with id " + sessionID + " failed with " + e.getMessage(), e);
		}
	}
	/**
	 * @return the number of full users currently logged in.
	 */
	/*public static int getNumberOfFullUsers() {
		String COUNT = "count(" + BaseTLoggedInUsersPeer.OBJECTID + ")";
		Criteria crit = new Criteria();		
		crit.addSelectColumn(COUNT);
		crit.add(BaseTLoggedInUsersPeer.USERLEVEL, new Integer(1), Criteria.GREATER_EQUAL);
		int theNumber = 0;
		try {
			theNumber = ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
		} catch (Exception e) {
			LOGGER.error("Can't count number of full users: " + e.getMessage(), e);
		}
		return theNumber;
	}*/

	/**
	 * @return the number of limited users currently logged in.
	 */
	/*public static int getNumberOfLimitedUsers() {
		String COUNT = "count(" + BaseTLoggedInUsersPeer.OBJECTID + ")";
		Criteria crit = new Criteria();		
		crit.addSelectColumn(COUNT);
		crit.add(BaseTLoggedInUsersPeer.USERLEVEL, new Integer(1), Criteria.LESS_THAN);
		int theNumber = 0;
		try {
			theNumber = ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
		} catch (Exception e) {
			LOGGER.error("Can't count number of full users: " + e.getMessage(), e);
		}
		return theNumber;
	}*/

	/**
	 * @return a HashMap with String session ids as key, and TPersonBean user as value.
	 */
	/*public static HashMap getLoggedInUsers() {
		HashMap users = new HashMap();
		Criteria crit = new Criteria();
		try {
			List lu = BaseTLoggedInUsersPeer.doSelectJoinTPerson(crit);
			Iterator it = lu.iterator();
			while (it.hasNext()) {
				TLoggedInUsers tlu = (TLoggedInUsers) it.next();
				TPerson user = tlu.getTPerson();
				users.put(tlu.getSessionId(), user.getBean());
			}
		} catch (TorqueException e) {
			LOGGER.error("Error getting logged in users list: " + e.getMessage(), e);
		}
		return users;
	}*/
	
	/**
	 * At the end of the servlet life all users with sessions belonging to this servlet 
	 * should be cleared from the logged in user table in the database.
	 * @param sessions
	 */
	/*public static void removeAllSessions(Map<String, TPersonBean> sessions) {
		Criteria crit = new Criteria();
		if (sessions == null || sessions.isEmpty()) {
			return;
		}
		Object[] sessionIds = sessions.keySet().toArray();
		if (sessionIds == null || sessionIds.length == 0) {
			return;
		}
		crit.addIn(SESSIONID, sessionIds);
		try {

			doDelete(crit);
		} catch (TorqueException e) {
            LOGGER.error("Error removing active sessions: " + e.getMessage(), e);
			LOGGER.error(crit.toString());
		}
	}*/
	
	/**
	 * This will clean out all users that haven't been updated during the last timeout
	 * period plus some grace period of 30 seconds.
	 */
	/*public void cleanLoggedInUsersTable()  {
		Criteria crit = new Criteria();
		try {
			List lusers = doSelect(crit);
			if (lusers == null || lusers.isEmpty()) {
				return;
			}
			ListIterator it = lusers.listIterator();
			while (it.hasNext()) {
				TLoggedInUsers currentUser = (TLoggedInUsers)it.next();
				Date lupdate = currentUser.getLastUpdate();
				Date now = new Date();
				if (lupdate != null && (now.getTime() - lupdate.getTime())/1000 > (ClusterBL.getPassawayTimeout() + 30) ) {
					crit.clear();
					crit.add(OBJECTID, currentUser.getObjectID());
					doDelete(crit);
				}
			}
		} catch (TorqueException e) {
			LOGGER.error("Can't delete dead user: " + e.getMessage(), e);
		}
		return;
	}*/
	
	/**
	 * We update the currently active users every so often to see how recent their
	 * session is. This permits us to clean the table from any debris in case
	 * there was an unclean shutdown.
	 * @param sessions
	 */
	/*public static void updateLoggedInUsers(Map<String, TPersonBean> sessions) {
		if (sessions==null || sessions.isEmpty()) {
			return;
		}
		Criteria scrit = new Criteria();
		Criteria ucrit = new Criteria();
		Object[] sessionIds = sessions.keySet().toArray();
		if (sessionIds==null || sessionIds.length == 0) {
			return;
		}
		scrit.addIn(SESSIONID, sessionIds);
		ucrit.add(LASTUPDATE, new Date());
		try {
			doUpdate(scrit,ucrit);
		} catch (TorqueException e) {
            LOGGER.error("Error updating active sessions: " + e.getMessage(), e);
		}
	}*/
	
	/**
	 * Update the timestamp for logged in users
	 * @param clusterNodeID
	 * @param date
	 */
	/*public void updateLoggedInUsers(Integer clusterNodeID, Date date) {
		Criteria scrit = new Criteria();
		Criteria ucrit = new Criteria();
		scrit.add(NODEADDRESS, clusterNodeID);
		ucrit.add(LASTUPDATE, date);
		try {
			doUpdate(scrit, ucrit);
		} catch (TorqueException e) {
            LOGGER.error("Error updating active sessions: " + e.getMessage(), e);
		}
	}*/
	
	/**
	 * Saves an entity changes in the TEntityChanges table.
	 * @param entityChangesBean
	 * @return
	 */
	public Integer save(TLoggedInUsersBean loggedInUsersBean) {
		TLoggedInUsers tloggedInUsers;
		try {
			tloggedInUsers = TLoggedInUsers.createTLoggedInUsers(loggedInUsersBean);
			tloggedInUsers.save();
			return tloggedInUsers.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a logged in user failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes logged in user by objectID
	 * @param objectID
	 * @return
	 */
	/*public void deleteByID(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the logged in user by id " + objectID +  " failed with " + e.getMessage(), e);
		}
	}*/
	
	/*private static List<TLoggedInUsersBean> convertTorqueListToBeanList(List<TLoggedInUsers> torqueList) {		
		List<TLoggedInUsersBean> beanList = new LinkedList<TLoggedInUsersBean>();
		if (torqueList!=null) {
			for (TLoggedInUsers loggedInUsers : torqueList) {
				beanList.add(loggedInUsers.getBean());
			}
		}
		return beanList;
	}*/
}
