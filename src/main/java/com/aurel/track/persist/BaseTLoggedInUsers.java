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



import com.aurel.track.persist.TClusterNode;
import com.aurel.track.persist.TClusterNodePeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TLoggedInUsersBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TClusterNodeBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TLoggedInUsers
 */
public abstract class BaseTLoggedInUsers extends TpBaseObject
{
    /** The Peer class */
    private static final TLoggedInUsersPeer peer =
        new TLoggedInUsersPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the nodeAddress field */
    private Integer nodeAddress;

    /** The value for the sessionId field */
    private String sessionId;

    /** The value for the loggedUser field */
    private Integer loggedUser;

    /** The value for the userLevel field */
    private Integer userLevel;

    /** The value for the lastUpdate field */
    private Date lastUpdate;

    /** The value for the moreProps field */
    private String moreProps;


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
    public void setObjectID(Integer v) 
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
        }


    }

    /**
     * Get the NodeAddress
     *
     * @return Integer
     */
    public Integer getNodeAddress()
    {
        return nodeAddress;
    }


    /**
     * Set the value of NodeAddress
     *
     * @param v new value
     */
    public void setNodeAddress(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.nodeAddress, v))
        {
            this.nodeAddress = v;
            setModified(true);
        }


        if (aTClusterNode != null && !ObjectUtils.equals(aTClusterNode.getObjectID(), v))
        {
            aTClusterNode = null;
        }

    }

    /**
     * Get the SessionId
     *
     * @return String
     */
    public String getSessionId()
    {
        return sessionId;
    }


    /**
     * Set the value of SessionId
     *
     * @param v new value
     */
    public void setSessionId(String v) 
    {

        if (!ObjectUtils.equals(this.sessionId, v))
        {
            this.sessionId = v;
            setModified(true);
        }


    }

    /**
     * Get the LoggedUser
     *
     * @return Integer
     */
    public Integer getLoggedUser()
    {
        return loggedUser;
    }


    /**
     * Set the value of LoggedUser
     *
     * @param v new value
     */
    public void setLoggedUser(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.loggedUser, v))
        {
            this.loggedUser = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
        }

    }

    /**
     * Get the UserLevel
     *
     * @return Integer
     */
    public Integer getUserLevel()
    {
        return userLevel;
    }


    /**
     * Set the value of UserLevel
     *
     * @param v new value
     */
    public void setUserLevel(Integer v) 
    {

        if (!ObjectUtils.equals(this.userLevel, v))
        {
            this.userLevel = v;
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
     * Get the MoreProps
     *
     * @return String
     */
    public String getMoreProps()
    {
        return moreProps;
    }


    /**
     * Set the value of MoreProps
     *
     * @param v new value
     */
    public void setMoreProps(String v) 
    {

        if (!ObjectUtils.equals(this.moreProps, v))
        {
            this.moreProps = v;
            setModified(true);
        }


    }

    



    private TPerson aTPerson;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPerson(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setLoggedUser((Integer) null);
        }
        else
        {
            setLoggedUser(v.getObjectID());
        }
        aTPerson = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPerson()
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.loggedUser, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.loggedUser));
        }
        return aTPerson;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPerson(Connection connection)
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.loggedUser, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.loggedUser), connection);
        }
        return aTPerson;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonKey(ObjectKey key) throws TorqueException
    {

        setLoggedUser(new Integer(((NumberKey) key).intValue()));
    }




    private TClusterNode aTClusterNode;

    /**
     * Declares an association between this object and a TClusterNode object
     *
     * @param v TClusterNode
     * @throws TorqueException
     */
    public void setTClusterNode(TClusterNode v) throws TorqueException
    {
        if (v == null)
        {
            setNodeAddress((Integer) null);
        }
        else
        {
            setNodeAddress(v.getObjectID());
        }
        aTClusterNode = v;
    }


    /**
     * Returns the associated TClusterNode object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TClusterNode object
     * @throws TorqueException
     */
    public TClusterNode getTClusterNode()
        throws TorqueException
    {
        if (aTClusterNode == null && (!ObjectUtils.equals(this.nodeAddress, null)))
        {
            aTClusterNode = TClusterNodePeer.retrieveByPK(SimpleKey.keyFor(this.nodeAddress));
        }
        return aTClusterNode;
    }

    /**
     * Return the associated TClusterNode object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TClusterNode object
     * @throws TorqueException
     */
    public TClusterNode getTClusterNode(Connection connection)
        throws TorqueException
    {
        if (aTClusterNode == null && (!ObjectUtils.equals(this.nodeAddress, null)))
        {
            aTClusterNode = TClusterNodePeer.retrieveByPK(SimpleKey.keyFor(this.nodeAddress), connection);
        }
        return aTClusterNode;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTClusterNodeKey(ObjectKey key) throws TorqueException
    {

        setNodeAddress(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("SessionId");
            fieldNames.add("LoggedUser");
            fieldNames.add("UserLevel");
            fieldNames.add("LastUpdate");
            fieldNames.add("MoreProps");
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
        if (name.equals("SessionId"))
        {
            return getSessionId();
        }
        if (name.equals("LoggedUser"))
        {
            return getLoggedUser();
        }
        if (name.equals("UserLevel"))
        {
            return getUserLevel();
        }
        if (name.equals("LastUpdate"))
        {
            return getLastUpdate();
        }
        if (name.equals("MoreProps"))
        {
            return getMoreProps();
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
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNodeAddress((Integer) value);
            return true;
        }
        if (name.equals("SessionId"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSessionId((String) value);
            return true;
        }
        if (name.equals("LoggedUser"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLoggedUser((Integer) value);
            return true;
        }
        if (name.equals("UserLevel"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setUserLevel((Integer) value);
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
        if (name.equals("MoreProps"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMoreProps((String) value);
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
        if (name.equals(TLoggedInUsersPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TLoggedInUsersPeer.NODEADDRESS))
        {
            return getNodeAddress();
        }
        if (name.equals(TLoggedInUsersPeer.SESSIONID))
        {
            return getSessionId();
        }
        if (name.equals(TLoggedInUsersPeer.LOGGEDUSER))
        {
            return getLoggedUser();
        }
        if (name.equals(TLoggedInUsersPeer.USERLEVEL))
        {
            return getUserLevel();
        }
        if (name.equals(TLoggedInUsersPeer.LASTUPDATE))
        {
            return getLastUpdate();
        }
        if (name.equals(TLoggedInUsersPeer.MOREPROPS))
        {
            return getMoreProps();
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
      if (TLoggedInUsersPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TLoggedInUsersPeer.NODEADDRESS.equals(name))
        {
            return setByName("NodeAddress", value);
        }
      if (TLoggedInUsersPeer.SESSIONID.equals(name))
        {
            return setByName("SessionId", value);
        }
      if (TLoggedInUsersPeer.LOGGEDUSER.equals(name))
        {
            return setByName("LoggedUser", value);
        }
      if (TLoggedInUsersPeer.USERLEVEL.equals(name))
        {
            return setByName("UserLevel", value);
        }
      if (TLoggedInUsersPeer.LASTUPDATE.equals(name))
        {
            return setByName("LastUpdate", value);
        }
      if (TLoggedInUsersPeer.MOREPROPS.equals(name))
        {
            return setByName("MoreProps", value);
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
            return getSessionId();
        }
        if (pos == 3)
        {
            return getLoggedUser();
        }
        if (pos == 4)
        {
            return getUserLevel();
        }
        if (pos == 5)
        {
            return getLastUpdate();
        }
        if (pos == 6)
        {
            return getMoreProps();
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
            return setByName("SessionId", value);
        }
    if (position == 3)
        {
            return setByName("LoggedUser", value);
        }
    if (position == 4)
        {
            return setByName("UserLevel", value);
        }
    if (position == 5)
        {
            return setByName("LastUpdate", value);
        }
    if (position == 6)
        {
            return setByName("MoreProps", value);
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
        save(TLoggedInUsersPeer.DATABASE_NAME);
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
                    TLoggedInUsersPeer.doInsert((TLoggedInUsers) this, con);
                    setNew(false);
                }
                else
                {
                    TLoggedInUsersPeer.doUpdate((TLoggedInUsers) this, con);
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
        
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) 
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
    public TLoggedInUsers copy() throws TorqueException
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
    public TLoggedInUsers copy(Connection con) throws TorqueException
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
    public TLoggedInUsers copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TLoggedInUsers(), deepcopy);
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
    public TLoggedInUsers copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TLoggedInUsers(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TLoggedInUsers copyInto(TLoggedInUsers copyObj) throws TorqueException
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
    protected TLoggedInUsers copyInto(TLoggedInUsers copyObj, Connection con) throws TorqueException
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
    protected TLoggedInUsers copyInto(TLoggedInUsers copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setNodeAddress(nodeAddress);
        copyObj.setSessionId(sessionId);
        copyObj.setLoggedUser(loggedUser);
        copyObj.setUserLevel(userLevel);
        copyObj.setLastUpdate(lastUpdate);
        copyObj.setMoreProps(moreProps);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {
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
    protected TLoggedInUsers copyInto(TLoggedInUsers copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setNodeAddress(nodeAddress);
        copyObj.setSessionId(sessionId);
        copyObj.setLoggedUser(loggedUser);
        copyObj.setUserLevel(userLevel);
        copyObj.setLastUpdate(lastUpdate);
        copyObj.setMoreProps(moreProps);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TLoggedInUsersPeer getPeer()
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
        return TLoggedInUsersPeer.getTableMap();
    }

  
    /**
     * Creates a TLoggedInUsersBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TLoggedInUsersBean with the contents of this object
     */
    public TLoggedInUsersBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TLoggedInUsersBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TLoggedInUsersBean with the contents of this object
     */
    public TLoggedInUsersBean getBean(IdentityMap createdBeans)
    {
        TLoggedInUsersBean result = (TLoggedInUsersBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TLoggedInUsersBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setNodeAddress(getNodeAddress());
        result.setSessionId(getSessionId());
        result.setLoggedUser(getLoggedUser());
        result.setUserLevel(getUserLevel());
        result.setLastUpdate(getLastUpdate());
        result.setMoreProps(getMoreProps());





        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTClusterNode != null)
        {
            TClusterNodeBean relatedBean = aTClusterNode.getBean(createdBeans);
            result.setTClusterNodeBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TLoggedInUsers with the contents
     * of a TLoggedInUsersBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TLoggedInUsersBean which contents are used to create
     *        the resulting class
     * @return an instance of TLoggedInUsers with the contents of bean
     */
    public static TLoggedInUsers createTLoggedInUsers(TLoggedInUsersBean bean)
        throws TorqueException
    {
        return createTLoggedInUsers(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TLoggedInUsers with the contents
     * of a TLoggedInUsersBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TLoggedInUsersBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TLoggedInUsers with the contents of bean
     */

    public static TLoggedInUsers createTLoggedInUsers(TLoggedInUsersBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TLoggedInUsers result = (TLoggedInUsers) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TLoggedInUsers();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setNodeAddress(bean.getNodeAddress());
        result.setSessionId(bean.getSessionId());
        result.setLoggedUser(bean.getLoggedUser());
        result.setUserLevel(bean.getUserLevel());
        result.setLastUpdate(bean.getLastUpdate());
        result.setMoreProps(bean.getMoreProps());





        {
            TPersonBean relatedBean = bean.getTPersonBean();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPerson(relatedObject);
            }
        }



        {
            TClusterNodeBean relatedBean = bean.getTClusterNodeBean();
            if (relatedBean != null)
            {
                TClusterNode relatedObject = TClusterNode.createTClusterNode(relatedBean, createdObjects);
                result.setTClusterNode(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TLoggedInUsers:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("NodeAddress = ")
           .append(getNodeAddress())
           .append("\n");
        str.append("SessionId = ")
           .append(getSessionId())
           .append("\n");
        str.append("LoggedUser = ")
           .append(getLoggedUser())
           .append("\n");
        str.append("UserLevel = ")
           .append(getUserLevel())
           .append("\n");
        str.append("LastUpdate = ")
           .append(getLastUpdate())
           .append("\n");
        str.append("MoreProps = ")
           .append(getMoreProps())
           .append("\n");
        return(str.toString());
    }
}
