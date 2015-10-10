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


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.torque.TorqueException;
import org.apache.torque.map.TableMap;
import org.apache.torque.om.BaseObject;
import org.apache.torque.om.ComboKey;
import org.apache.torque.om.DateKey;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.om.StringKey;
import org.apache.torque.om.Persistent;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;




  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TClusterNodeBean;

import com.aurel.track.beans.TLoggedInUsersBean;
import com.aurel.track.beans.TEntityChangesBean;


/**
 * This table holds properties for each node in the cluster
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TClusterNode
 */
public abstract class BaseTClusterNode extends TpBaseObject
{
    /** The Peer class */
    private static final TClusterNodePeer peer =
        new TClusterNodePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the nodeAddress field */
    private String nodeAddress;

    /** The value for the nodeURL field */
    private String nodeURL;

    /** The value for the lastUpdate field */
    private Date lastUpdate;

    /** The value for the masterNode field */
    private Integer masterNode = new Integer(0);

    /** The value for the reloadConfig field */
    private Integer reloadConfig = new Integer(0);

    /** The value for the reloadChanges field */
    private String reloadChanges;


    /**
     * Get the ObjectID
     *
     * @return Integer
     */
    public Integer getObjectID()
    {
        return objectID;
    }


    /**
     * Set the value of ObjectID
     *
     * @param v new value
     */
    public void setObjectID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
        }



        // update associated TLoggedInUsers
        if (collTLoggedInUserss != null)
        {
            for (int i = 0; i < collTLoggedInUserss.size(); i++)
            {
                ((TLoggedInUsers) collTLoggedInUserss.get(i))
                        .setNodeAddress(v);
            }
        }

        // update associated TEntityChanges
        if (collTEntityChangess != null)
        {
            for (int i = 0; i < collTEntityChangess.size(); i++)
            {
                ((TEntityChanges) collTEntityChangess.get(i))
                        .setClusterNode(v);
            }
        }
    }

    /**
     * Get the NodeAddress
     *
     * @return String
     */
    public String getNodeAddress()
    {
        return nodeAddress;
    }


    /**
     * Set the value of NodeAddress
     *
     * @param v new value
     */
    public void setNodeAddress(String v) 
    {

        if (!ObjectUtils.equals(this.nodeAddress, v))
        {
            this.nodeAddress = v;
            setModified(true);
        }


    }

    /**
     * Get the NodeURL
     *
     * @return String
     */
    public String getNodeURL()
    {
        return nodeURL;
    }


    /**
     * Set the value of NodeURL
     *
     * @param v new value
     */
    public void setNodeURL(String v) 
    {

        if (!ObjectUtils.equals(this.nodeURL, v))
        {
            this.nodeURL = v;
            setModified(true);
        }


    }

    /**
     * Get the LastUpdate
     *
     * @return Date
     */
    public Date getLastUpdate()
    {
        return lastUpdate;
    }


    /**
     * Set the value of LastUpdate
     *
     * @param v new value
     */
    public void setLastUpdate(Date v) 
    {

        if (!ObjectUtils.equals(this.lastUpdate, v))
        {
            this.lastUpdate = v;
            setModified(true);
        }


    }

    /**
     * Get the MasterNode
     *
     * @return Integer
     */
    public Integer getMasterNode()
    {
        return masterNode;
    }


    /**
     * Set the value of MasterNode
     *
     * @param v new value
     */
    public void setMasterNode(Integer v) 
    {

        if (!ObjectUtils.equals(this.masterNode, v))
        {
            this.masterNode = v;
            setModified(true);
        }


    }

    /**
     * Get the ReloadConfig
     *
     * @return Integer
     */
    public Integer getReloadConfig()
    {
        return reloadConfig;
    }


    /**
     * Set the value of ReloadConfig
     *
     * @param v new value
     */
    public void setReloadConfig(Integer v) 
    {

        if (!ObjectUtils.equals(this.reloadConfig, v))
        {
            this.reloadConfig = v;
            setModified(true);
        }


    }

    /**
     * Get the ReloadChanges
     *
     * @return String
     */
    public String getReloadChanges()
    {
        return reloadChanges;
    }


    /**
     * Set the value of ReloadChanges
     *
     * @param v new value
     */
    public void setReloadChanges(String v) 
    {

        if (!ObjectUtils.equals(this.reloadChanges, v))
        {
            this.reloadChanges = v;
            setModified(true);
        }


    }

       


    /**
     * Collection to store aggregation of collTLoggedInUserss
     */
    protected List<TLoggedInUsers> collTLoggedInUserss;

    /**
     * Temporary storage of collTLoggedInUserss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTLoggedInUserss()
    {
        if (collTLoggedInUserss == null)
        {
            collTLoggedInUserss = new ArrayList<TLoggedInUsers>();
        }
    }


    /**
     * Method called to associate a TLoggedInUsers object to this object
     * through the TLoggedInUsers foreign key attribute
     *
     * @param l TLoggedInUsers
     * @throws TorqueException
     */
    public void addTLoggedInUsers(TLoggedInUsers l) throws TorqueException
    {
        getTLoggedInUserss().add(l);
        l.setTClusterNode((TClusterNode) this);
    }

    /**
     * Method called to associate a TLoggedInUsers object to this object
     * through the TLoggedInUsers foreign key attribute using connection.
     *
     * @param l TLoggedInUsers
     * @throws TorqueException
     */
    public void addTLoggedInUsers(TLoggedInUsers l, Connection con) throws TorqueException
    {
        getTLoggedInUserss(con).add(l);
        l.setTClusterNode((TClusterNode) this);
    }

    /**
     * The criteria used to select the current contents of collTLoggedInUserss
     */
    private Criteria lastTLoggedInUserssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLoggedInUserss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TLoggedInUsers> getTLoggedInUserss()
        throws TorqueException
    {
        if (collTLoggedInUserss == null)
        {
            collTLoggedInUserss = getTLoggedInUserss(new Criteria(10));
        }
        return collTLoggedInUserss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TClusterNode has previously
     * been saved, it will retrieve related TLoggedInUserss from storage.
     * If this TClusterNode is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TLoggedInUsers> getTLoggedInUserss(Criteria criteria) throws TorqueException
    {
        if (collTLoggedInUserss == null)
        {
            if (isNew())
            {
               collTLoggedInUserss = new ArrayList<TLoggedInUsers>();
            }
            else
            {
                criteria.add(TLoggedInUsersPeer.NODEADDRESS, getObjectID() );
                collTLoggedInUserss = TLoggedInUsersPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TLoggedInUsersPeer.NODEADDRESS, getObjectID());
                if (!lastTLoggedInUserssCriteria.equals(criteria))
                {
                    collTLoggedInUserss = TLoggedInUsersPeer.doSelect(criteria);
                }
            }
        }
        lastTLoggedInUserssCriteria = criteria;

        return collTLoggedInUserss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLoggedInUserss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TLoggedInUsers> getTLoggedInUserss(Connection con) throws TorqueException
    {
        if (collTLoggedInUserss == null)
        {
            collTLoggedInUserss = getTLoggedInUserss(new Criteria(10), con);
        }
        return collTLoggedInUserss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TClusterNode has previously
     * been saved, it will retrieve related TLoggedInUserss from storage.
     * If this TClusterNode is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TLoggedInUsers> getTLoggedInUserss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTLoggedInUserss == null)
        {
            if (isNew())
            {
               collTLoggedInUserss = new ArrayList<TLoggedInUsers>();
            }
            else
            {
                 criteria.add(TLoggedInUsersPeer.NODEADDRESS, getObjectID());
                 collTLoggedInUserss = TLoggedInUsersPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TLoggedInUsersPeer.NODEADDRESS, getObjectID());
                 if (!lastTLoggedInUserssCriteria.equals(criteria))
                 {
                     collTLoggedInUserss = TLoggedInUsersPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTLoggedInUserssCriteria = criteria;

         return collTLoggedInUserss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TClusterNode is new, it will return
     * an empty collection; or if this TClusterNode has previously
     * been saved, it will retrieve related TLoggedInUserss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TClusterNode.
     */
    protected List<TLoggedInUsers> getTLoggedInUserssJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTLoggedInUserss == null)
        {
            if (isNew())
            {
               collTLoggedInUserss = new ArrayList<TLoggedInUsers>();
            }
            else
            {
                criteria.add(TLoggedInUsersPeer.NODEADDRESS, getObjectID());
                collTLoggedInUserss = TLoggedInUsersPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TLoggedInUsersPeer.NODEADDRESS, getObjectID());
            if (!lastTLoggedInUserssCriteria.equals(criteria))
            {
                collTLoggedInUserss = TLoggedInUsersPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTLoggedInUserssCriteria = criteria;

        return collTLoggedInUserss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TClusterNode is new, it will return
     * an empty collection; or if this TClusterNode has previously
     * been saved, it will retrieve related TLoggedInUserss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TClusterNode.
     */
    protected List<TLoggedInUsers> getTLoggedInUserssJoinTClusterNode(Criteria criteria)
        throws TorqueException
    {
        if (collTLoggedInUserss == null)
        {
            if (isNew())
            {
               collTLoggedInUserss = new ArrayList<TLoggedInUsers>();
            }
            else
            {
                criteria.add(TLoggedInUsersPeer.NODEADDRESS, getObjectID());
                collTLoggedInUserss = TLoggedInUsersPeer.doSelectJoinTClusterNode(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TLoggedInUsersPeer.NODEADDRESS, getObjectID());
            if (!lastTLoggedInUserssCriteria.equals(criteria))
            {
                collTLoggedInUserss = TLoggedInUsersPeer.doSelectJoinTClusterNode(criteria);
            }
        }
        lastTLoggedInUserssCriteria = criteria;

        return collTLoggedInUserss;
    }





    /**
     * Collection to store aggregation of collTEntityChangess
     */
    protected List<TEntityChanges> collTEntityChangess;

    /**
     * Temporary storage of collTEntityChangess to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTEntityChangess()
    {
        if (collTEntityChangess == null)
        {
            collTEntityChangess = new ArrayList<TEntityChanges>();
        }
    }


    /**
     * Method called to associate a TEntityChanges object to this object
     * through the TEntityChanges foreign key attribute
     *
     * @param l TEntityChanges
     * @throws TorqueException
     */
    public void addTEntityChanges(TEntityChanges l) throws TorqueException
    {
        getTEntityChangess().add(l);
        l.setTClusterNode((TClusterNode) this);
    }

    /**
     * Method called to associate a TEntityChanges object to this object
     * through the TEntityChanges foreign key attribute using connection.
     *
     * @param l TEntityChanges
     * @throws TorqueException
     */
    public void addTEntityChanges(TEntityChanges l, Connection con) throws TorqueException
    {
        getTEntityChangess(con).add(l);
        l.setTClusterNode((TClusterNode) this);
    }

    /**
     * The criteria used to select the current contents of collTEntityChangess
     */
    private Criteria lastTEntityChangessCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEntityChangess(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TEntityChanges> getTEntityChangess()
        throws TorqueException
    {
        if (collTEntityChangess == null)
        {
            collTEntityChangess = getTEntityChangess(new Criteria(10));
        }
        return collTEntityChangess;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TClusterNode has previously
     * been saved, it will retrieve related TEntityChangess from storage.
     * If this TClusterNode is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TEntityChanges> getTEntityChangess(Criteria criteria) throws TorqueException
    {
        if (collTEntityChangess == null)
        {
            if (isNew())
            {
               collTEntityChangess = new ArrayList<TEntityChanges>();
            }
            else
            {
                criteria.add(TEntityChangesPeer.CLUSTERNODE, getObjectID() );
                collTEntityChangess = TEntityChangesPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TEntityChangesPeer.CLUSTERNODE, getObjectID());
                if (!lastTEntityChangessCriteria.equals(criteria))
                {
                    collTEntityChangess = TEntityChangesPeer.doSelect(criteria);
                }
            }
        }
        lastTEntityChangessCriteria = criteria;

        return collTEntityChangess;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEntityChangess(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEntityChanges> getTEntityChangess(Connection con) throws TorqueException
    {
        if (collTEntityChangess == null)
        {
            collTEntityChangess = getTEntityChangess(new Criteria(10), con);
        }
        return collTEntityChangess;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TClusterNode has previously
     * been saved, it will retrieve related TEntityChangess from storage.
     * If this TClusterNode is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEntityChanges> getTEntityChangess(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTEntityChangess == null)
        {
            if (isNew())
            {
               collTEntityChangess = new ArrayList<TEntityChanges>();
            }
            else
            {
                 criteria.add(TEntityChangesPeer.CLUSTERNODE, getObjectID());
                 collTEntityChangess = TEntityChangesPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TEntityChangesPeer.CLUSTERNODE, getObjectID());
                 if (!lastTEntityChangessCriteria.equals(criteria))
                 {
                     collTEntityChangess = TEntityChangesPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTEntityChangessCriteria = criteria;

         return collTEntityChangess;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TClusterNode is new, it will return
     * an empty collection; or if this TClusterNode has previously
     * been saved, it will retrieve related TEntityChangess from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TClusterNode.
     */
    protected List<TEntityChanges> getTEntityChangessJoinTClusterNode(Criteria criteria)
        throws TorqueException
    {
        if (collTEntityChangess == null)
        {
            if (isNew())
            {
               collTEntityChangess = new ArrayList<TEntityChanges>();
            }
            else
            {
                criteria.add(TEntityChangesPeer.CLUSTERNODE, getObjectID());
                collTEntityChangess = TEntityChangesPeer.doSelectJoinTClusterNode(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEntityChangesPeer.CLUSTERNODE, getObjectID());
            if (!lastTEntityChangessCriteria.equals(criteria))
            {
                collTEntityChangess = TEntityChangesPeer.doSelectJoinTClusterNode(criteria);
            }
        }
        lastTEntityChangessCriteria = criteria;

        return collTEntityChangess;
    }



        
    private static List<String> fieldNames = null;

    /**
     * Generate a list of field names.
     *
     * @return a list of field names
     */
    public static synchronized List<String> getFieldNames()
    {
        if (fieldNames == null)
        {
            fieldNames = new ArrayList<String>();
            fieldNames.add("ObjectID");
            fieldNames.add("NodeAddress");
            fieldNames.add("NodeURL");
            fieldNames.add("LastUpdate");
            fieldNames.add("MasterNode");
            fieldNames.add("ReloadConfig");
            fieldNames.add("ReloadChanges");
            fieldNames = Collections.unmodifiableList(fieldNames);
        }
        return fieldNames;
    }

    /**
     * Retrieves a field from the object by field (Java) name passed in as a String.
     *
     * @param name field name
     * @return value
     */
    public Object getByName(String name)
    {
        if (name.equals("ObjectID"))
        {
            return getObjectID();
        }
        if (name.equals("NodeAddress"))
        {
            return getNodeAddress();
        }
        if (name.equals("NodeURL"))
        {
            return getNodeURL();
        }
        if (name.equals("LastUpdate"))
        {
            return getLastUpdate();
        }
        if (name.equals("MasterNode"))
        {
            return getMasterNode();
        }
        if (name.equals("ReloadConfig"))
        {
            return getReloadConfig();
        }
        if (name.equals("ReloadChanges"))
        {
            return getReloadChanges();
        }
        return null;
    }

    /**
     * Set a field in the object by field (Java) name.
     *
     * @param name field name
     * @param value field value
     * @return True if value was set, false if not (invalid name / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByName(String name, Object value )
        throws TorqueException, IllegalArgumentException
    {
        if (name.equals("ObjectID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setObjectID((Integer) value);
            return true;
        }
        if (name.equals("NodeAddress"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNodeAddress((String) value);
            return true;
        }
        if (name.equals("NodeURL"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNodeURL((String) value);
            return true;
        }
        if (name.equals("LastUpdate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLastUpdate((Date) value);
            return true;
        }
        if (name.equals("MasterNode"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMasterNode((Integer) value);
            return true;
        }
        if (name.equals("ReloadConfig"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReloadConfig((Integer) value);
            return true;
        }
        if (name.equals("ReloadChanges"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReloadChanges((String) value);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.  The String must be one of the static
     * Strings defined in this Class' Peer.
     *
     * @param name peer name
     * @return value
     */
    public Object getByPeerName(String name)
    {
        if (name.equals(TClusterNodePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TClusterNodePeer.NODEADDRESS))
        {
            return getNodeAddress();
        }
        if (name.equals(TClusterNodePeer.NODEURL))
        {
            return getNodeURL();
        }
        if (name.equals(TClusterNodePeer.LASTUPDATE))
        {
            return getLastUpdate();
        }
        if (name.equals(TClusterNodePeer.MASTERNODE))
        {
            return getMasterNode();
        }
        if (name.equals(TClusterNodePeer.RELOADCONFIG))
        {
            return getReloadConfig();
        }
        if (name.equals(TClusterNodePeer.RELOADCHANGES))
        {
            return getReloadChanges();
        }
        return null;
    }

    /**
     * Set field values by Peer Field Name
     *
     * @param name field name
     * @param value field value
     * @return True if value was set, false if not (invalid name / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByPeerName(String name, Object value)
        throws TorqueException, IllegalArgumentException
    {
      if (TClusterNodePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TClusterNodePeer.NODEADDRESS.equals(name))
        {
            return setByName("NodeAddress", value);
        }
      if (TClusterNodePeer.NODEURL.equals(name))
        {
            return setByName("NodeURL", value);
        }
      if (TClusterNodePeer.LASTUPDATE.equals(name))
        {
            return setByName("LastUpdate", value);
        }
      if (TClusterNodePeer.MASTERNODE.equals(name))
        {
            return setByName("MasterNode", value);
        }
      if (TClusterNodePeer.RELOADCONFIG.equals(name))
        {
            return setByName("ReloadConfig", value);
        }
      if (TClusterNodePeer.RELOADCHANGES.equals(name))
        {
            return setByName("ReloadChanges", value);
        }
        return false;
    }

    /**
     * Retrieves a field from the object by Position as specified
     * in the xml schema.  Zero-based.
     *
     * @param pos position in xml schema
     * @return value
     */
    public Object getByPosition(int pos)
    {
        if (pos == 0)
        {
            return getObjectID();
        }
        if (pos == 1)
        {
            return getNodeAddress();
        }
        if (pos == 2)
        {
            return getNodeURL();
        }
        if (pos == 3)
        {
            return getLastUpdate();
        }
        if (pos == 4)
        {
            return getMasterNode();
        }
        if (pos == 5)
        {
            return getReloadConfig();
        }
        if (pos == 6)
        {
            return getReloadChanges();
        }
        return null;
    }

    /**
     * Set field values by its position (zero based) in the XML schema.
     *
     * @param position The field position
     * @param value field value
     * @return True if value was set, false if not (invalid position / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByPosition(int position, Object value)
        throws TorqueException, IllegalArgumentException
    {
    if (position == 0)
        {
            return setByName("ObjectID", value);
        }
    if (position == 1)
        {
            return setByName("NodeAddress", value);
        }
    if (position == 2)
        {
            return setByName("NodeURL", value);
        }
    if (position == 3)
        {
            return setByName("LastUpdate", value);
        }
    if (position == 4)
        {
            return setByName("MasterNode", value);
        }
    if (position == 5)
        {
            return setByName("ReloadConfig", value);
        }
    if (position == 6)
        {
            return setByName("ReloadChanges", value);
        }
        return false;
    }
     
    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     *
     * @throws Exception
     */
    public void save() throws Exception
    {
        save(TClusterNodePeer.DATABASE_NAME);
    }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     * Note: this code is here because the method body is
     * auto-generated conditionally and therefore needs to be
     * in this file instead of in the super class, BaseObject.
     *
     * @param dbName
     * @throws TorqueException
     */
    public void save(String dbName) throws TorqueException
    {
        Connection con = null;
        try
        {
            con = Transaction.begin(dbName);
            save(con);
            Transaction.commit(con);
        }
        catch(TorqueException e)
        {
            Transaction.safeRollback(con);
            throw e;
        }
    }

    /** flag to prevent endless save loop, if this object is referenced
        by another object which falls in this transaction. */
    private boolean alreadyInSave = false;
    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.  This method
     * is meant to be used as part of a transaction, otherwise use
     * the save() method and the connection details will be handled
     * internally
     *
     * @param con
     * @throws TorqueException
     */
    public void save(Connection con) throws TorqueException
    {
        if (!alreadyInSave)
        {
            alreadyInSave = true;



            // If this object has been modified, then save it to the database.
            if (isModified())
            {
                if (isNew())
                {
                    TClusterNodePeer.doInsert((TClusterNode) this, con);
                    setNew(false);
                }
                else
                {
                    TClusterNodePeer.doUpdate((TClusterNode) this, con);
                }
            }


            if (collTLoggedInUserss != null)
            {
                for (int i = 0; i < collTLoggedInUserss.size(); i++)
                {
                    ((TLoggedInUsers) collTLoggedInUserss.get(i)).save(con);
                }
            }

            if (collTEntityChangess != null)
            {
                for (int i = 0; i < collTEntityChangess.size(); i++)
                {
                    ((TEntityChanges) collTEntityChangess.get(i)).save(con);
                }
            }
            alreadyInSave = false;
        }
    }


    /**
     * Set the PrimaryKey using ObjectKey.
     *
     * @param key objectID ObjectKey
     */
    public void setPrimaryKey(ObjectKey key)
        throws TorqueException
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) throws TorqueException
    {
        setObjectID(new Integer(key));
    }


    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
        return SimpleKey.keyFor(getObjectID());
    }
 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     */
    public TClusterNode copy() throws TorqueException
    {
        return copy(true);
    }

    /**
     * Makes a copy of this object using connection.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     *
     * @param con the database connection to read associated objects.
     */
    public TClusterNode copy(Connection con) throws TorqueException
    {
        return copy(true, con);
    }

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * If the parameter deepcopy is true, it then fills all the
     * association collections and sets the related objects to
     * isNew=true.
     *
     * @param deepcopy whether to copy the associated objects.
     */
    public TClusterNode copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TClusterNode(), deepcopy);
    }

    /**
     * Makes a copy of this object using connection.
     * It creates a new object filling in the simple attributes.
     * If the parameter deepcopy is true, it then fills all the
     * association collections and sets the related objects to
     * isNew=true.
     *
     * @param deepcopy whether to copy the associated objects.
     * @param con the database connection to read associated objects.
     */
    public TClusterNode copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TClusterNode(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TClusterNode copyInto(TClusterNode copyObj) throws TorqueException
    {
        return copyInto(copyObj, true);
    }

  
    /**
     * Fills the copyObj with the contents of this object using connection.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param con the database connection to read associated objects.
     */
    protected TClusterNode copyInto(TClusterNode copyObj, Connection con) throws TorqueException
    {
        return copyInto(copyObj, true, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * If deepcopy is true, The associated objects are also copied
     * and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param deepcopy whether the associated objects should be copied.
     */
    protected TClusterNode copyInto(TClusterNode copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setNodeAddress(nodeAddress);
        copyObj.setNodeURL(nodeURL);
        copyObj.setLastUpdate(lastUpdate);
        copyObj.setMasterNode(masterNode);
        copyObj.setReloadConfig(reloadConfig);
        copyObj.setReloadChanges(reloadChanges);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TLoggedInUsers> vTLoggedInUserss = getTLoggedInUserss();
        if (vTLoggedInUserss != null)
        {
            for (int i = 0; i < vTLoggedInUserss.size(); i++)
            {
                TLoggedInUsers obj =  vTLoggedInUserss.get(i);
                copyObj.addTLoggedInUsers(obj.copy());
            }
        }
        else
        {
            copyObj.collTLoggedInUserss = null;
        }


        List<TEntityChanges> vTEntityChangess = getTEntityChangess();
        if (vTEntityChangess != null)
        {
            for (int i = 0; i < vTEntityChangess.size(); i++)
            {
                TEntityChanges obj =  vTEntityChangess.get(i);
                copyObj.addTEntityChanges(obj.copy());
            }
        }
        else
        {
            copyObj.collTEntityChangess = null;
        }
        }
        return copyObj;
    }
        
    
    /**
     * Fills the copyObj with the contents of this object using connection.
     * If deepcopy is true, The associated objects are also copied
     * and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param deepcopy whether the associated objects should be copied.
     * @param con the database connection to read associated objects.
     */
    protected TClusterNode copyInto(TClusterNode copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setNodeAddress(nodeAddress);
        copyObj.setNodeURL(nodeURL);
        copyObj.setLastUpdate(lastUpdate);
        copyObj.setMasterNode(masterNode);
        copyObj.setReloadConfig(reloadConfig);
        copyObj.setReloadChanges(reloadChanges);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TLoggedInUsers> vTLoggedInUserss = getTLoggedInUserss(con);
        if (vTLoggedInUserss != null)
        {
            for (int i = 0; i < vTLoggedInUserss.size(); i++)
            {
                TLoggedInUsers obj =  vTLoggedInUserss.get(i);
                copyObj.addTLoggedInUsers(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTLoggedInUserss = null;
        }


        List<TEntityChanges> vTEntityChangess = getTEntityChangess(con);
        if (vTEntityChangess != null)
        {
            for (int i = 0; i < vTEntityChangess.size(); i++)
            {
                TEntityChanges obj =  vTEntityChangess.get(i);
                copyObj.addTEntityChanges(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTEntityChangess = null;
        }
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TClusterNodePeer getPeer()
    {
        return peer;
    }

    /**
     * Retrieves the TableMap object related to this Table data without
     * compiler warnings of using getPeer().getTableMap().
     *
     * @return The associated TableMap object.
     */
    public TableMap getTableMap() throws TorqueException
    {
        return TClusterNodePeer.getTableMap();
    }

  
    /**
     * Creates a TClusterNodeBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TClusterNodeBean with the contents of this object
     */
    public TClusterNodeBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TClusterNodeBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TClusterNodeBean with the contents of this object
     */
    public TClusterNodeBean getBean(IdentityMap createdBeans)
    {
        TClusterNodeBean result = (TClusterNodeBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TClusterNodeBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setNodeAddress(getNodeAddress());
        result.setNodeURL(getNodeURL());
        result.setLastUpdate(getLastUpdate());
        result.setMasterNode(getMasterNode());
        result.setReloadConfig(getReloadConfig());
        result.setReloadChanges(getReloadChanges());



        if (collTLoggedInUserss != null)
        {
            List<TLoggedInUsersBean> relatedBeans = new ArrayList<TLoggedInUsersBean>(collTLoggedInUserss.size());
            for (Iterator<TLoggedInUsers> collTLoggedInUserssIt = collTLoggedInUserss.iterator(); collTLoggedInUserssIt.hasNext(); )
            {
                TLoggedInUsers related = (TLoggedInUsers) collTLoggedInUserssIt.next();
                TLoggedInUsersBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTLoggedInUsersBeans(relatedBeans);
        }


        if (collTEntityChangess != null)
        {
            List<TEntityChangesBean> relatedBeans = new ArrayList<TEntityChangesBean>(collTEntityChangess.size());
            for (Iterator<TEntityChanges> collTEntityChangessIt = collTEntityChangess.iterator(); collTEntityChangessIt.hasNext(); )
            {
                TEntityChanges related = (TEntityChanges) collTEntityChangessIt.next();
                TEntityChangesBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTEntityChangesBeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TClusterNode with the contents
     * of a TClusterNodeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TClusterNodeBean which contents are used to create
     *        the resulting class
     * @return an instance of TClusterNode with the contents of bean
     */
    public static TClusterNode createTClusterNode(TClusterNodeBean bean)
        throws TorqueException
    {
        return createTClusterNode(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TClusterNode with the contents
     * of a TClusterNodeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TClusterNodeBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TClusterNode with the contents of bean
     */

    public static TClusterNode createTClusterNode(TClusterNodeBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TClusterNode result = (TClusterNode) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TClusterNode();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setNodeAddress(bean.getNodeAddress());
        result.setNodeURL(bean.getNodeURL());
        result.setLastUpdate(bean.getLastUpdate());
        result.setMasterNode(bean.getMasterNode());
        result.setReloadConfig(bean.getReloadConfig());
        result.setReloadChanges(bean.getReloadChanges());



        {
            List<TLoggedInUsersBean> relatedBeans = bean.getTLoggedInUsersBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TLoggedInUsersBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TLoggedInUsersBean relatedBean =  relatedBeansIt.next();
                    TLoggedInUsers related = TLoggedInUsers.createTLoggedInUsers(relatedBean, createdObjects);
                    result.addTLoggedInUsersFromBean(related);
                }
            }
        }


        {
            List<TEntityChangesBean> relatedBeans = bean.getTEntityChangesBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TEntityChangesBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TEntityChangesBean relatedBean =  relatedBeansIt.next();
                    TEntityChanges related = TEntityChanges.createTEntityChanges(relatedBean, createdObjects);
                    result.addTEntityChangesFromBean(related);
                }
            }
        }

    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TLoggedInUsers object to this object.
     * through the TLoggedInUsers foreign key attribute
     *
     * @param toAdd TLoggedInUsers
     */
    protected void addTLoggedInUsersFromBean(TLoggedInUsers toAdd)
    {
        initTLoggedInUserss();
        collTLoggedInUserss.add(toAdd);
    }


    /**
     * Method called to associate a TEntityChanges object to this object.
     * through the TEntityChanges foreign key attribute
     *
     * @param toAdd TEntityChanges
     */
    protected void addTEntityChangesFromBean(TEntityChanges toAdd)
    {
        initTEntityChangess();
        collTEntityChangess.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TClusterNode:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("NodeAddress = ")
           .append(getNodeAddress())
           .append("\n");
        str.append("NodeURL = ")
           .append(getNodeURL())
           .append("\n");
        str.append("LastUpdate = ")
           .append(getLastUpdate())
           .append("\n");
        str.append("MasterNode = ")
           .append(getMasterNode())
           .append("\n");
        str.append("ReloadConfig = ")
           .append(getReloadConfig())
           .append("\n");
        str.append("ReloadChanges = ")
           .append(getReloadChanges())
           .append("\n");
        return(str.toString());
    }
}
