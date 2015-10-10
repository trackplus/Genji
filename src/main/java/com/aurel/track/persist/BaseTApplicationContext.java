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
import com.aurel.track.beans.TApplicationContextBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TApplicationContext
 */
public abstract class BaseTApplicationContext extends TpBaseObject
{
    /** The Peer class */
    private static final TApplicationContextPeer peer =
        new TApplicationContextPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the loggedFullUsers field */
    private Integer loggedFullUsers;

    /** The value for the loggedLimitedUsers field */
    private Integer loggedLimitedUsers;

    /** The value for the refreshConfiguration field */
    private Integer refreshConfiguration = new Integer(0);

    /** The value for the firstTime field */
    private Integer firstTime = new Integer(0);

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
     * Get the LoggedFullUsers
     *
     * @return Integer
     */
    public Integer getLoggedFullUsers()
    {
        return loggedFullUsers;
    }


    /**
     * Set the value of LoggedFullUsers
     *
     * @param v new value
     */
    public void setLoggedFullUsers(Integer v) 
    {

        if (!ObjectUtils.equals(this.loggedFullUsers, v))
        {
            this.loggedFullUsers = v;
            setModified(true);
        }


    }

    /**
     * Get the LoggedLimitedUsers
     *
     * @return Integer
     */
    public Integer getLoggedLimitedUsers()
    {
        return loggedLimitedUsers;
    }


    /**
     * Set the value of LoggedLimitedUsers
     *
     * @param v new value
     */
    public void setLoggedLimitedUsers(Integer v) 
    {

        if (!ObjectUtils.equals(this.loggedLimitedUsers, v))
        {
            this.loggedLimitedUsers = v;
            setModified(true);
        }


    }

    /**
     * Get the RefreshConfiguration
     *
     * @return Integer
     */
    public Integer getRefreshConfiguration()
    {
        return refreshConfiguration;
    }


    /**
     * Set the value of RefreshConfiguration
     *
     * @param v new value
     */
    public void setRefreshConfiguration(Integer v) 
    {

        if (!ObjectUtils.equals(this.refreshConfiguration, v))
        {
            this.refreshConfiguration = v;
            setModified(true);
        }


    }

    /**
     * Get the FirstTime
     *
     * @return Integer
     */
    public Integer getFirstTime()
    {
        return firstTime;
    }


    /**
     * Set the value of FirstTime
     *
     * @param v new value
     */
    public void setFirstTime(Integer v) 
    {

        if (!ObjectUtils.equals(this.firstTime, v))
        {
            this.firstTime = v;
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
            fieldNames.add("LoggedFullUsers");
            fieldNames.add("LoggedLimitedUsers");
            fieldNames.add("RefreshConfiguration");
            fieldNames.add("FirstTime");
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
        if (name.equals("LoggedFullUsers"))
        {
            return getLoggedFullUsers();
        }
        if (name.equals("LoggedLimitedUsers"))
        {
            return getLoggedLimitedUsers();
        }
        if (name.equals("RefreshConfiguration"))
        {
            return getRefreshConfiguration();
        }
        if (name.equals("FirstTime"))
        {
            return getFirstTime();
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
        if (name.equals("LoggedFullUsers"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLoggedFullUsers((Integer) value);
            return true;
        }
        if (name.equals("LoggedLimitedUsers"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLoggedLimitedUsers((Integer) value);
            return true;
        }
        if (name.equals("RefreshConfiguration"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRefreshConfiguration((Integer) value);
            return true;
        }
        if (name.equals("FirstTime"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFirstTime((Integer) value);
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
        if (name.equals(TApplicationContextPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TApplicationContextPeer.LOGGEDFULLUSERS))
        {
            return getLoggedFullUsers();
        }
        if (name.equals(TApplicationContextPeer.LOGGEDLIMITEDUSERS))
        {
            return getLoggedLimitedUsers();
        }
        if (name.equals(TApplicationContextPeer.REFRESHCONFIGURATION))
        {
            return getRefreshConfiguration();
        }
        if (name.equals(TApplicationContextPeer.FIRSTTIME))
        {
            return getFirstTime();
        }
        if (name.equals(TApplicationContextPeer.MOREPROPS))
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
      if (TApplicationContextPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TApplicationContextPeer.LOGGEDFULLUSERS.equals(name))
        {
            return setByName("LoggedFullUsers", value);
        }
      if (TApplicationContextPeer.LOGGEDLIMITEDUSERS.equals(name))
        {
            return setByName("LoggedLimitedUsers", value);
        }
      if (TApplicationContextPeer.REFRESHCONFIGURATION.equals(name))
        {
            return setByName("RefreshConfiguration", value);
        }
      if (TApplicationContextPeer.FIRSTTIME.equals(name))
        {
            return setByName("FirstTime", value);
        }
      if (TApplicationContextPeer.MOREPROPS.equals(name))
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
            return getLoggedFullUsers();
        }
        if (pos == 2)
        {
            return getLoggedLimitedUsers();
        }
        if (pos == 3)
        {
            return getRefreshConfiguration();
        }
        if (pos == 4)
        {
            return getFirstTime();
        }
        if (pos == 5)
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
            return setByName("LoggedFullUsers", value);
        }
    if (position == 2)
        {
            return setByName("LoggedLimitedUsers", value);
        }
    if (position == 3)
        {
            return setByName("RefreshConfiguration", value);
        }
    if (position == 4)
        {
            return setByName("FirstTime", value);
        }
    if (position == 5)
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
        save(TApplicationContextPeer.DATABASE_NAME);
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
                    TApplicationContextPeer.doInsert((TApplicationContext) this, con);
                    setNew(false);
                }
                else
                {
                    TApplicationContextPeer.doUpdate((TApplicationContext) this, con);
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
    public TApplicationContext copy() throws TorqueException
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
    public TApplicationContext copy(Connection con) throws TorqueException
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
    public TApplicationContext copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TApplicationContext(), deepcopy);
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
    public TApplicationContext copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TApplicationContext(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TApplicationContext copyInto(TApplicationContext copyObj) throws TorqueException
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
    protected TApplicationContext copyInto(TApplicationContext copyObj, Connection con) throws TorqueException
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
    protected TApplicationContext copyInto(TApplicationContext copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLoggedFullUsers(loggedFullUsers);
        copyObj.setLoggedLimitedUsers(loggedLimitedUsers);
        copyObj.setRefreshConfiguration(refreshConfiguration);
        copyObj.setFirstTime(firstTime);
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
    protected TApplicationContext copyInto(TApplicationContext copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLoggedFullUsers(loggedFullUsers);
        copyObj.setLoggedLimitedUsers(loggedLimitedUsers);
        copyObj.setRefreshConfiguration(refreshConfiguration);
        copyObj.setFirstTime(firstTime);
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
    public TApplicationContextPeer getPeer()
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
        return TApplicationContextPeer.getTableMap();
    }

  
    /**
     * Creates a TApplicationContextBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TApplicationContextBean with the contents of this object
     */
    public TApplicationContextBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TApplicationContextBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TApplicationContextBean with the contents of this object
     */
    public TApplicationContextBean getBean(IdentityMap createdBeans)
    {
        TApplicationContextBean result = (TApplicationContextBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TApplicationContextBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLoggedFullUsers(getLoggedFullUsers());
        result.setLoggedLimitedUsers(getLoggedLimitedUsers());
        result.setRefreshConfiguration(getRefreshConfiguration());
        result.setFirstTime(getFirstTime());
        result.setMoreProps(getMoreProps());


        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TApplicationContext with the contents
     * of a TApplicationContextBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TApplicationContextBean which contents are used to create
     *        the resulting class
     * @return an instance of TApplicationContext with the contents of bean
     */
    public static TApplicationContext createTApplicationContext(TApplicationContextBean bean)
        throws TorqueException
    {
        return createTApplicationContext(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TApplicationContext with the contents
     * of a TApplicationContextBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TApplicationContextBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TApplicationContext with the contents of bean
     */

    public static TApplicationContext createTApplicationContext(TApplicationContextBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TApplicationContext result = (TApplicationContext) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TApplicationContext();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLoggedFullUsers(bean.getLoggedFullUsers());
        result.setLoggedLimitedUsers(bean.getLoggedLimitedUsers());
        result.setRefreshConfiguration(bean.getRefreshConfiguration());
        result.setFirstTime(bean.getFirstTime());
        result.setMoreProps(bean.getMoreProps());


    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TApplicationContext:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("LoggedFullUsers = ")
           .append(getLoggedFullUsers())
           .append("\n");
        str.append("LoggedLimitedUsers = ")
           .append(getLoggedLimitedUsers())
           .append("\n");
        str.append("RefreshConfiguration = ")
           .append(getRefreshConfiguration())
           .append("\n");
        str.append("FirstTime = ")
           .append(getFirstTime())
           .append("\n");
        str.append("MoreProps = ")
           .append(getMoreProps())
           .append("\n");
        return(str.toString());
    }
}
