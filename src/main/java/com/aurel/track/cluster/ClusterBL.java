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

package com.aurel.track.cluster;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TClusterNodeBean;
import com.aurel.track.beans.TLoggedInUsersBean;
import com.aurel.track.dao.ClusterNodeDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.LoggedInUsersDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.prop.ApplicationBean;

/**
 * Helper class for clustering logic
 */
public class ClusterBL {
	
	private static ClusterNodeDAO clusterNodeDAO = DAOFactory.getFactory().getClusterNodeDAO();
	private static LoggedInUsersDAO loggedInUsersDAO = DAOFactory.getFactory().getLoggedInUsersDAO();
	private static final Logger LOGGER = LogManager.getLogger(ClusterBL.class);
	
	/**
	 * Saves a logged in user in the TLoggedInUsers table.
	 * @param entityChangesBean
	 * @return
	 */
	public static Integer saveLoggedInUser(TLoggedInUsersBean loggedInUsersBean) {
		return loggedInUsersDAO.save(loggedInUsersBean);
	}
	
	/**
	 * Remove a logged user by session from TLoggedInUsers table (by logging out)
	 * @param sessionID
	 */
	public static void removeLoggedInUserBySession(String sessionID) {
		loggedInUsersDAO.removeBySession(sessionID);
	}
	

	/**
	 * The period after which the server has to perform an update of the node
	 * table. If the node table is not updated, it is being deleted because
	 * it is assumed that the server is not running any more.
	 * <P>
	 * Please note that in the worst case during this timeout period the full
	 * text search index may not be updated. However, no update events get
	 * lost, this is just a matter of actuality.
	 * <P>
	 * The actual value will be set in the quartz-jobs.xml, not here! This
	 * is just some dummy initial value.
	 */
	private static int PASSAWAYTIMEOUT = 5*60; // in seconds
	
	private static boolean iAmTheMaster = false;
	
	
	/**
	 * This is only possible to set from the routine that tries to set this
	 * node as a master node. You cannot directly set this node as master 
	 * using this routine.
	 * 
	 * @param master
	 */
	private static void setIAmTheMaster(boolean master) {
		// TODO Turn on indexing for this node.
		iAmTheMaster = master;
	}
	
	public static boolean getIAmTheMaster() {
		return iAmTheMaster;
	}
	
	/**
	 * Whether the lucene index is shared in cluster
	 */
	public static boolean isSharedLuceneIndex() {
		return true;
	}
	
	public static boolean isCluster() {
		return ApplicationBean.getApplicationBean().isCluster();
		//return true;
	}
	
	/**
	 * Whether the full text indexing should be executed instantly
	 */
	public static boolean indexInstantly() {
		if (isSharedLuceneIndex()) {
			//one central index location shared by all cluster nodes only the master can the index actualize
			return getIAmTheMaster();
		} else {
			//each node has the own locale index files
			return true;
		}
	}
	
	public static void setPassawayTimeout(int seconds) {
		PASSAWAYTIMEOUT = seconds;
	}
	
	public static int getPassawayTimeout() {
		return PASSAWAYTIMEOUT;
	}
	
	/**
	 * Load all cluster nodes
	 * @return
	 */
	public static List<TClusterNodeBean> loadAllClusterNodes() {
		return clusterNodeDAO.loadAll();
	}
	
	/**
	 * Loads cluster node by IP
	 * @return 
	 */
	public static TClusterNodeBean loadByIP(String ip) {
		List<TClusterNodeBean> clusterNodes = clusterNodeDAO.loadByIP(ip);
		if (clusterNodes!=null && !clusterNodes.isEmpty()) {
			return clusterNodes.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Saves a cluster node in ClusterNode table.
	 * @param clusterNodeBean
	 * @return
	 */
	private static Integer save(TClusterNodeBean clusterNodeBean) {
		Integer cloid = clusterNodeDAO.save(clusterNodeBean);
		return cloid;
	}
	
	/**
	 * Deletes a cluster node by ID
	 * @param clusterNodeID
	 * @return
	 */
	public static void delete(Integer clusterNodeID) {
		clusterNodeDAO.delete(clusterNodeID);
	}
	
	/**
	 * Gets the IP of the localhost
	 * @return
	 */
	public static String getIPAddress() {
		InetAddress[] inetAddresses = ApplicationBean.getInetAddress();
		if (inetAddresses!=null && inetAddresses.length>0) {
			InetAddress firstInetAddress = inetAddresses[0];
			if (LOGGER.isTraceEnabled() && inetAddresses.length>1) {
				LOGGER.trace("IPs found: ");
				for (InetAddress inetAddresse : inetAddresses) {
					LOGGER.trace(inetAddresse.getHostAddress());
				}
			}
			if (firstInetAddress!=null) {
				String ipNumber = firstInetAddress.getHostAddress();
				LOGGER.debug("The IP for the first inet address is " + ipNumber);
				return ipNumber;
			}
		} else {
			LOGGER.warn("No InetAddress in ApplicationBean");
		}
		return null;
	}
	
	/**
	 * Process the cluster by application start and in cluster guardian job
	 */
	public static void processCluster(boolean updateEntities) {
		updateMe();
		cleanInactiveNodes();
		setMeAsMasterIfPossible(updateEntities);
		if (ClusterBL.isCluster() && updateEntities) {
			ClusterUpdateBL.updateClusterNode();
		}
	}
	
	/**
	 * This will update the time stamp, or create a new entry if it does not yet exist.
	 */
	private static void updateMe()  {
		String myIP = getIPAddress();
		if (myIP==null) {
			// Something is wrong with the network
			LOGGER.error("Cannot update cluster node table. Most likely something is wrong with your DNS configuration.");
			return;
		}
		TClusterNodeBean myNode = loadByIP(myIP);
		if (myNode==null) {
			myNode = new TClusterNodeBean();
			myNode.setMasterNode(Integer.valueOf(0));
			myNode.setNodeAddress(myIP);
		}
		Date date = new Date();
		myNode.setLastUpdate(date);
		clusterNodeDAO.save(myNode);
		LOGGER.debug("The cluster node " + myIP + " set the alive date to " + date);
	}
	
	/**
	 * clean the timed out users
	 * @param passAwayTimeout
	 */
	/*private static void cleanLoggedInUsersTable(Integer passAwayTimeout)  {
		List<TLoggedInUsersBean> loggedInUsers = loggedInUsersDAO.loadAll();
		if (loggedInUsers!=null) {
			for (TLoggedInUsersBean loggedInUsersBean : loggedInUsers) {
				Date lastUpdate = loggedInUsersBean.getLastUpdate();
				Date now = new Date();
				if (lastUpdate==null || lastUpdate!=null && (now.getTime() - lastUpdate.getTime())/1000 > (passAwayTimeout + 30)) {
					loggedInUsersDAO.deleteByID(loggedInUsersBean.getObjectID());
				}
			}
		}
	}*/	
	
	/**
	 * This will remove any entries that haven't been updated for some time.
	 */
	private static void cleanInactiveNodes()  {
		List<TClusterNodeBean> nodes = loadAllClusterNodes();
		if (nodes!=null) {
			for (TClusterNodeBean currentNode : nodes) {
				Date lupdate = currentNode.getLastUpdate();
				Date now = new Date();
				//add 30 seconds to be sure that the master node will not be changed only because the cron job runs few milliseconds later as another cluster node 
				if (lupdate!=null && (now.getTime()-lupdate.getTime()) > (PASSAWAYTIMEOUT + 30) *1000 ) {
					LOGGER.debug("The node with IP " + currentNode.getNodeAddress() + " is inactive since " + lupdate + " and will be removed");
					delete(currentNode.getObjectID());
				}
			}
		} else { // If the list is empty reinstall me as the master node
			setMeAsMasterIfPossible(true);
		}
		
		
	}
	
	/**
	 * Will attempt to set this node as the master node, if
	 * there is no other master node.
	 */
	private static void setMeAsMasterIfPossible(boolean initIndexWriters)  {
		String myIp = getIPAddress();
		boolean meMaster = false;
		if (myIp==null) {
			return;
		}
		TClusterNodeBean myNode = loadByIP(myIp);
		if (myNode==null) {
			return;
		}
		if (myNode.getMasterNode().intValue() > 0) {
			setIAmTheMaster(true);
			return; // I am already the master
		}		
		Random generator = new Random(myIp.hashCode());
		boolean notResolved = true;
		while (notResolved) {
			notResolved = false;   // just try once, unless there is a conflict
			// Check if anybody else is already the master. If so, forget it.
			List<TClusterNodeBean> nodes = loadAllClusterNodes();
			for (TClusterNodeBean node : nodes) {
				Integer masternode = node.getMasterNode();
				if (masternode!=null && masternode.intValue() > 0) {
					//another master found
					setIAmTheMaster(false);
					return; // This position is already taken...
				}
			}
			// BEGIN OF CRITICAL SECTION: two or more cluster nodes are got through here at the same moment
			int rand = generator.nextInt(999999) + 1;
			myNode.setMasterNode(new Integer(rand));
			save(myNode);
			LOGGER.info("The cluster node " + myIp + " becomes the new master");
			// END OF CRITICAL SECTION

			// Now check again. In the mean time there could have been another cluster
			// node successful in establishing itself as the master node. We let the
			// one with the higher number win.

			// Check if anybody else has been successful installed itself as the master. 
			// If so, let's retreat if we have the smaller random number.
			// This is not very likely to happen since it would mean that the servers
			// run this piece of code (a few nanoseconds) at exactly the same time.
			nodes = loadAllClusterNodes();
			meMaster = true;
			for (TClusterNodeBean node : nodes) {
				Integer masterNode = node.getMasterNode();
				if (masterNode!=null && masterNode.intValue()>rand) {
					myNode.setMasterNode(Integer.valueOf(0));
					meMaster = false;
					save(myNode); // This position is already taken, we retreat...
					LOGGER.info("Due to race conditions two master nodes were active " + myIp + " retreats from master");
				}
				// Now this is even more unlikely, since it would furthermore require
				// that two different random generators with different seeds generate
				// the same number in a space of one million numbers. But you never know...
				if (masterNode!=null && masterNode.intValue()==rand 
						&& node.getNodeAddress() != null
						&& !node.getNodeAddress().equals(myNode.getNodeAddress())) {
					// Shoot, we have the same random number for two different nodes.
					// How could that happen, really bad luck...
					notResolved = true;  // so let's start all over
					meMaster = false;
					LOGGER.info("Race condition try again");
					break;
				}
			}
		}
		setIAmTheMaster(meMaster);
		if (meMaster && initIndexWriters) {
			//if master was changed hopefully the old master has released the index locks and the new master should take the index files
			//if the old master didn't released the locks then we are in trouble
			LuceneIndexer.initIndexWriters(false);
		}
	}
	
	
	public static interface ENTITY_TYPE {
		public static int ALL = 0; //full reindex of lucene indexes
		public static int WORKITEM = LuceneUtil.INDEXES.WORKITEM_INDEX;
		public static int SYSTEM_LIST = LuceneUtil.INDEXES.NOT_LOCALIZED_LIST_INDEX;//not really one to one but important is to have different integers for each change type
		public static int CUSTOM_LIST = LuceneUtil.INDEXES.LOCALIZED_LIST_INDEX;//not really one to one but important is to have different integers
		public static int ATTACHMENT_LIST = LuceneUtil.INDEXES.ATTACHMENT_INDEX;
		public static int BUDGET_PLAN = LuceneUtil.INDEXES.BUDGET_PLAN_INDEX;
		public static int EXPENSE = LuceneUtil.INDEXES.EXPENSE_INDEX;
		public static int LINK = LuceneUtil.INDEXES.LINK_INDEX;
		public static int SYSTEM_STATE_LIST = -1;//only for system lists cache (lucene index is not affected)
	}
	
	public static interface CHANGE_TYPE {
		//an new entity added: lucene index update is needed
		public static int ADD_TO_INDEX = 1;
		//an entity was updated: lucene index update is needed
		public static int UPDATE_INDEX = 2;
		//an entity was deleted: lucene index update is needed
		public static int DELETE_FROM_INDEX = 3;
		//update only the cache (after changing a sort order, icon etc. which does not affect the full text index directly)
		public static int UPDATE_CACHE = 4;
		//reload all entities
		//public static int RELOAD_ALL = 5;
	}

	/**
	 * Gets the system list string for debug purposes
	 * @param listID
	 * @return
	 */
	public static String getSystemList(Integer listID) {
		switch(listID.intValue()) {
		case SystemFields.PROJECT: 
			return "Project";
		case SystemFields.RELEASE: 
			return "Release";
		case SystemFields.PERSON: 
			return "Person";
		case SystemFields.ISSUETYPE: 
			return "ItemType";
		case SystemFields.STATE: 
			return "Status";
		case SystemFields.PRIORITY: 
			return "Priority";
		case SystemFields.SEVERITY: 
			return "Severity";	
		}
		return "unknown";
	}

	/**
	 * Gets the entityType string for debug purposes
	 * @param entityType
	 * @return
	 */
	static String getEntityTypeString(int entityType) {
		switch(entityType) {
		case ENTITY_TYPE.ALL: 
			return "Full rindex";
		case ENTITY_TYPE.WORKITEM:
			return "Item";
		case ENTITY_TYPE.SYSTEM_LIST:
			return "System list";
		case ENTITY_TYPE.CUSTOM_LIST:
			return "Custom list";
		case ENTITY_TYPE.ATTACHMENT_LIST:
			return "Attachment";
		case ENTITY_TYPE.BUDGET_PLAN:
			return "Budget/plan";
		case ENTITY_TYPE.EXPENSE:
			return "Expense";
		case ENTITY_TYPE.LINK:
			return "Link";
		case ENTITY_TYPE.SYSTEM_STATE_LIST:
			return "System state list";
		}
		return "unknown";
	}
	
	
	

	
	

}
