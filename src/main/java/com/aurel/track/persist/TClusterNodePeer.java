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

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TClusterNodeBean;
import com.aurel.track.dao.ClusterNodeDAO;
import com.aurel.track.prop.ApplicationBean;

/**
 * This table holds properties for each node in the cluster
 *
 */
public class TClusterNodePeer
extends com.aurel.track.persist.BaseTClusterNodePeer implements ClusterNodeDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TClusterNodePeer.class);
	public static final long serialVersionUID = 400L;
	
	private static Class[] deletePeerClasses = {    	
    	TLoggedInUsersPeer.class,
    	TEntityChangesPeer.class,
    	//use the superclass doDelete() methode!!!
        BaseTClusterNodePeer.class
    };
    
    private static String[] deleteFields = {    	
    	TLoggedInUsersPeer.NODEADDRESS,
    	TEntityChangesPeer.CLUSTERNODE,
    	BaseTClusterNodePeer.OBJECTID
    };	
	
    /**
	 * Load all cluster nodes
	 * @return
	 */
	@Override
	public List<TClusterNodeBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading all cluster nodes failed with " + e.getMessage());
			return null;
		}
	}
	
    /**
	 * Loads all entity changes for a cluster node
	 * @return 
	 */
	@Override
	public List<TClusterNodeBean> loadByIP(String ip) {
		Criteria crit = new Criteria();
		crit.add(NODEADDRESS, ip);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading node by ip " + ip + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Saves an entity changes in the TEntityChanges table.
	 * @param clusterNodeBean
	 * @return
	 */
	@Override
	public Integer save(TClusterNodeBean clusterNodeBean) {
		TClusterNode tClusterNode;
		try {
			tClusterNode = TClusterNode.createTClusterNode(clusterNodeBean);
			tClusterNode.save();
			Integer oid = tClusterNode.getObjectID();
			ApplicationBean.getInstance().setClusterNodeBean(clusterNodeBean);
			return oid;
		} catch (Exception e) {
			LOGGER.error("Saving of the cluster node failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Deletes a cluster node by ID
	 * @param clusterNodeID
	 * @return
	 */
	@Override
	public void delete(Integer clusterNodeID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, clusterNodeID);
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
	/*private static int PASSAWAYTIMEOUT = 5*60; // in seconds
	
	public static void setPassawayTimeout(int seconds) {
		PASSAWAYTIMEOUT = seconds;
	}
	
	public static int getPassawayTimeout() {
		return PASSAWAYTIMEOUT;
	}*/
	
	//private static boolean iAmTheMaster = false;
	
	/**
	 * Indicates if a clearAllMasters is in progress
	 */
	//private static Date clearInProgressDate = null;
	
	/**
	 * This is only possible to set from the routine that tries to set this
	 * node as a master node. You cannot directly set this node as master 
	 * using this routine.
	 * 
	 * @param master
	 */
	/*private static void setIAmTheMaster(boolean master) {
		// TODO Turn on indexing for this node.
		iAmTheMaster = master;
	}*/
	
	/*public static boolean getIAmTheMaster() {
		return iAmTheMaster;
	}*/
	
	/**
	 * Load information for a single cluster node from the database.
     *
	 * @param inetAddress the Internet address of the node to look for
	 * @return a TClusterNode with the information from the database, or null if
	 * it does not exist.
	 */
	/*public static TClusterNode load(InetAddress inetAddress)  {
		if (inetAddress == null) {
			return null;
		}
		List<TClusterNode> nodes = null;
		Criteria crit = new Criteria();
		crit.add(NODEADDRESS, (Object) inetAddress.getHostAddress(), Criteria.EQUAL);
		try {
			nodes = doSelect(crit);
		} catch(Exception e) {
			LOGGER.error("Loading node failed with " + e.getMessage());
		}
		if (nodes==null || nodes.isEmpty()) {
			return null;
		} else {
			return nodes.get(0);// take the one and only...
		}
	}*/

	
	
	/**
	 * Load all nodes of the cluster that are currently registered
	 * in the database.
	 */
	/*public static List<TClusterNode> loadAll()  {
		List<TClusterNode> nodes = null;
		Criteria crit = new Criteria();
		try {
			nodes = doSelect(crit);
		} catch(Exception e) {
			LOGGER.error("Loading all nodes failed with " + e.getMessage());
		}
		return nodes;
	}*/
	
	/**
	 * This will update the time stamp, or create a new entry if it does not yet exist.
	 */
	/*public static void updateMe()  {
		ApplicationBean appBean = ApplicationBean.getInstance();
		InetAddress myIp= null;
		if (appBean.getInetAddress() !=  null) {
		    myIp = appBean.getInetAddress()[0];
		}
		if (myIp==null) {
			// Something is wrong with the network
			LOGGER.error("Cannot update cluster node table. Most likely something is wrong with your DNS configuration.");
			return;
		}
		TClusterNode myNode = load(myIp);
		if (myNode==null && myIp!=null) {
			myNode = new TClusterNode();
			myNode.setMasterNode(new Integer(0));
			myNode.setNodeAddress(myIp.getHostAddress());
		}
		if (myNode.getMasterNode()!=null 
			&& myNode.getMasterNode().intValue() <= 0
			&& !clearInProgress) {
			// Somebody else might have withdrawn me the master rights.
			myNode.setMasterNode(new Integer(0));
			setIAmTheMaster(false);
		}
		Date now = new Date();
		// Check if we can force ourself to be the master after the timeout period for
		// clearing the previous master.
		if (clearInProgress && (now.getTime() - clearInProgressDate.getTime()) > PASSAWAYTIMEOUT*1000 ) {
			myNode.setMasterNode(new Integer(1000));
			clearInProgress = false;
			setIAmTheMaster(true);			
		}
		myNode.setLastUpdate(new Date());
		try {
			myNode.save();
		} catch (Exception e) {
			LOGGER.error("Could not update my node: " + e.getMessage());
		}
		TLoggedInUsersPeer.updateLoggedInUsers(appBean.getActiveSessions());
		TLoggedInUsersPeer.cleanLoggedInUsersTable();
		return;
	}*/
	
	/**
	 * This will remove any entries that haven't been updated for some time.
	 */
	/*public static void cleanInactiveNodes()  {
		updateMe();
		List<TClusterNode> nodes = loadAll();
		if (nodes!=null) {
			for (TClusterNode currentNode : nodes) {
				Date lupdate = currentNode.getLastUpdate();
				Date now = new Date();
				if (lupdate!=null && (now.getTime()-lupdate.getTime()) > PASSAWAYTIMEOUT*1000 ) {
					Criteria criteria = new Criteria();
					criteria.add(OBJECTID, currentNode.getObjectID());
					doDelete(criteria);
				}
			}
		}
		
	}*/
	
	/**
	 * Deletes the TClasses satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	/*public static void doDelete(Criteria crit) {
		List list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of THistoryTransaction to be deleted failed with " + e.getMessage());
		}			
        if (list == null || list.isEmpty()) {
            return;
        }
		Iterator iter = list.iterator();
		TClusterNode tClusterNode = null;
		while(iter.hasNext()) {
			tClusterNode = (TClusterNode)iter.next();
			LOGGER.debug("Deleting the ClusterNode with ID " + tClusterNode.getObjectID() + 
					" and node address " + tClusterNode.getNodeAddress());
			ReflectionHelper.delete(deletePeerClasses, deleteFields, tClusterNode.getObjectID());
		}
	}*/
	
	/**
	 * Will attempt to set this node as the master node, if
	 * there is no other master node.
	 */
	/*public static void setMeAsMasterIfPossible()  {
		boolean meMassa = false;
		InetAddress myIp = null;
		if (ApplicationBean.getInstance().getInetAddress() != null) {
			myIp = ApplicationBean.getInstance().getInetAddress()[0];
		} else {
			return;
		}
		TClusterNode myNode = load(myIp);
		if (myNode == null) {
			updateMe(); // I am not even in the database
			myNode = load(myIp);
		}
		if (myNode == null || myNode.getMasterNode().intValue() > 0) {
			setIAmTheMaster(true);
			return; // I am already the master
		}		
		Random generator = new Random(myIp.hashCode());
		boolean notResolved = true;
		while (notResolved) {
			notResolved = false;   // just try once, unless there is a conflict
			int rand = generator.nextInt(999999) + 1;
			// Check if anybody else is already the master. If so, forget it.
			// BEGIN OF CRITICAL SECTION
			List<TClusterNode> nodes = loadAll();
			Iterator it = nodes.listIterator();
			while (it.hasNext()) {
				Integer i = ((TClusterNode)it.next()).getMasterNode();
				if (i!=null && i.intValue() > 0) {
					setIAmTheMaster(false);
					return; // This position is already taken...
				}
			}
			myNode.setMasterNode(new Integer(rand));
			try {
				myNode.save();
			} catch (Exception e1) {
				LOGGER.error(e1.getMessage());
			}
			// END OF CRITICAL SECTION

			// Now check again. In the mean time there could have been another cluster
			// node successful in establishing itself as the master node. We let the
			// one with the higher number win.

			// Check if anybody else has been successful installed itself as the master. 
			// If so, let's retreat if we have the smaller random number.
			// This is not very likely to happen since it would mean that the servers
			// run this piece of code (a few nanoseconds) at exactly the same time.
			nodes = loadAll();
			it = nodes.listIterator();
			meMassa = true;
			while (it.hasNext()) {
				TClusterNode currentNode = (TClusterNode)it.next();
				Integer i = currentNode.getMasterNode();
				if (i!=null && i.intValue()>rand) {
					myNode.setMasterNode(new Integer(0));
					meMassa = false;
					try {
						myNode.save();  // This position is already taken, we retreat...
						break;
					} catch (Exception e2) {
						LOGGER.error(e2.getMessage());
					}           
				}
				// Now this is even more unlikely, since it would furthermore require
				// that two different random generators with different seeds generate
				// the same number in a space of one million numbers. But you never know...
				if (i != null && i.intValue() == rand 
						&& currentNode.getNodeAddress() != null
						&& !currentNode.getNodeAddress().equals(myNode.getNodeAddress())) {
					// Shoot, we have the same random number for two different nodes.
					// How could that happen, really bad luck...
					notResolved = true;  // so let's start all over
					meMassa = false;
					break;
				}
			}
		}
		setIAmTheMaster(meMassa);
		return;
	}*/
	
	/**
	 * Will force clear the previous master and set me as the new master after
	 * the grace period (PASSAWAYTIMEOUT). Be aware: this implementation can lead to
	 * a period of at most PASSAWAYTIMEOUT where nobody acts as a master, and hence
	 * indices are not updated anymore.
	 * <P>
	 * We might therefore consider to force an index rebuild after the master has changed. But
	 * this is not yet implemented (would have to be put in updateMe()).
	 */
	/*public static void forceMeAsMaster()  {
		List<TClusterNode> nodes = loadAll();
		ListIterator it = nodes.listIterator();
		while (it.hasNext()) {
			TClusterNode currentNode = (TClusterNode)it.next();
			if (currentNode.getMasterNode()!= null && currentNode.getMasterNode().intValue() > 0) {
				// Somebody else might have withdrawn me the master rights.
				currentNode.setMasterNode(new Integer(0));
				try {
					currentNode.save();
				} catch (Exception e) {
					LOGGER.error("Can't clear master node: " + e.getMessage());
				}
			}
		}
		clearInProgress=true;
		clearInProgressDate=new Date(); // This will be checked in updateMe().
		return;
	}*/

	
	private static List<TClusterNodeBean> convertTorqueListToBeanList(List<TClusterNode> torqueList) {		
		List<TClusterNodeBean> beanList = new LinkedList<TClusterNodeBean>();
		if (torqueList!=null) {
			for (TClusterNode tClusterNode : torqueList) {
				beanList.add(tClusterNode.getBean());
			}
		}
		return beanList;
	}
}
