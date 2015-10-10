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



import com.aurel.track.persist.TUserLevel;
import com.aurel.track.persist.TUserLevelPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TUserLevelSettingBean;
import com.aurel.track.beans.TUserLevelBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TUserLevelSetting
 */
public abstract class BaseTUserLevelSetting extends TpBaseObject
{
    /** The Peer class */
    private static final TUserLevelSettingPeer peer =
        new TUserLevelSettingPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the userLevel field */
    private Integer userLevel;

    /** The value for the aCTIONKEY field */
    private Integer aCTIONKEY;

    /** The value for the isActive field */
    private String isActive = "N";

    /** The value for the uuid field */
    private String uuid;


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
    public void setUserLevel(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.userLevel, v))
        {
            this.userLevel = v;
            setModified(true);
        }


        if (aTUserLevel != null && !ObjectUtils.equals(aTUserLevel.getObjectID(), v))
        {
            aTUserLevel = null;
        }

    }

    /**
     * Get the ACTIONKEY
     *
     * @return Integer
     */
    public Integer getACTIONKEY()
    {
        return aCTIONKEY;
    }


    /**
     * Set the value of ACTIONKEY
     *
     * @param v new value
     */
    public void setACTIONKEY(Integer v) 
    {

        if (!ObjectUtils.equals(this.aCTIONKEY, v))
        {
            this.aCTIONKEY = v;
            setModified(true);
        }


    }

    /**
     * Get the IsActive
     *
     * @return String
     */
    public String getIsActive()
    {
        return isActive;
    }


    /**
     * Set the value of IsActive
     *
     * @param v new value
     */
    public void setIsActive(String v) 
    {

        if (!ObjectUtils.equals(this.isActive, v))
        {
            this.isActive = v;
            setModified(true);
        }


    }

    /**
     * Get the Uuid
     *
     * @return String
     */
    public String getUuid()
    {
        return uuid;
    }


    /**
     * Set the value of Uuid
     *
     * @param v new value
     */
    public void setUuid(String v) 
    {

        if (!ObjectUtils.equals(this.uuid, v))
        {
            this.uuid = v;
            setModified(true);
        }


    }

    



    private TUserLevel aTUserLevel;

    /**
     * Declares an association between this object and a TUserLevel object
     *
     * @param v TUserLevel
     * @throws TorqueException
     */
    public void setTUserLevel(TUserLevel v) throws TorqueException
    {
        if (v == null)
        {
            setUserLevel((Integer) null);
        }
        else
        {
            setUserLevel(v.getObjectID());
        }
        aTUserLevel = v;
    }


    /**
     * Returns the associated TUserLevel object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TUserLevel object
     * @throws TorqueException
     */
    public TUserLevel getTUserLevel()
        throws TorqueException
    {
        if (aTUserLevel == null && (!ObjectUtils.equals(this.userLevel, null)))
        {
            aTUserLevel = TUserLevelPeer.retrieveByPK(SimpleKey.keyFor(this.userLevel));
        }
        return aTUserLevel;
    }

    /**
     * Return the associated TUserLevel object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TUserLevel object
     * @throws TorqueException
     */
    public TUserLevel getTUserLevel(Connection connection)
        throws TorqueException
    {
        if (aTUserLevel == null && (!ObjectUtils.equals(this.userLevel, null)))
        {
            aTUserLevel = TUserLevelPeer.retrieveByPK(SimpleKey.keyFor(this.userLevel), connection);
        }
        return aTUserLevel;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTUserLevelKey(ObjectKey key) throws TorqueException
    {

        setUserLevel(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("UserLevel");
            fieldNames.add("ACTIONKEY");
            fieldNames.add("IsActive");
            fieldNames.add("Uuid");
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
        if (name.equals("UserLevel"))
        {
            return getUserLevel();
        }
        if (name.equals("ACTIONKEY"))
        {
            return getACTIONKEY();
        }
        if (name.equals("IsActive"))
        {
            return getIsActive();
        }
        if (name.equals("Uuid"))
        {
            return getUuid();
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
        if (name.equals("ACTIONKEY"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setACTIONKEY((Integer) value);
            return true;
        }
        if (name.equals("IsActive"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsActive((String) value);
            return true;
        }
        if (name.equals("Uuid"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setUuid((String) value);
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
        if (name.equals(TUserLevelSettingPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TUserLevelSettingPeer.USERLEVEL))
        {
            return getUserLevel();
        }
        if (name.equals(TUserLevelSettingPeer.ACTIONKEY))
        {
            return getACTIONKEY();
        }
        if (name.equals(TUserLevelSettingPeer.ISACTIVE))
        {
            return getIsActive();
        }
        if (name.equals(TUserLevelSettingPeer.TPUUID))
        {
            return getUuid();
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
      if (TUserLevelSettingPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TUserLevelSettingPeer.USERLEVEL.equals(name))
        {
            return setByName("UserLevel", value);
        }
      if (TUserLevelSettingPeer.ACTIONKEY.equals(name))
        {
            return setByName("ACTIONKEY", value);
        }
      if (TUserLevelSettingPeer.ISACTIVE.equals(name))
        {
            return setByName("IsActive", value);
        }
      if (TUserLevelSettingPeer.TPUUID.equals(name))
        {
            return setByName("Uuid", value);
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
            return getUserLevel();
        }
        if (pos == 2)
        {
            return getACTIONKEY();
        }
        if (pos == 3)
        {
            return getIsActive();
        }
        if (pos == 4)
        {
            return getUuid();
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
            return setByName("UserLevel", value);
        }
    if (position == 2)
        {
            return setByName("ACTIONKEY", value);
        }
    if (position == 3)
        {
            return setByName("IsActive", value);
        }
    if (position == 4)
        {
            return setByName("Uuid", value);
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
        save(TUserLevelSettingPeer.DATABASE_NAME);
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
                    TUserLevelSettingPeer.doInsert((TUserLevelSetting) this, con);
                    setNew(false);
                }
                else
                {
                    TUserLevelSettingPeer.doUpdate((TUserLevelSetting) this, con);
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
    public TUserLevelSetting copy() throws TorqueException
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
    public TUserLevelSetting copy(Connection con) throws TorqueException
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
    public TUserLevelSetting copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TUserLevelSetting(), deepcopy);
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
    public TUserLevelSetting copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TUserLevelSetting(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TUserLevelSetting copyInto(TUserLevelSetting copyObj) throws TorqueException
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
    protected TUserLevelSetting copyInto(TUserLevelSetting copyObj, Connection con) throws TorqueException
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
    protected TUserLevelSetting copyInto(TUserLevelSetting copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setUserLevel(userLevel);
        copyObj.setACTIONKEY(aCTIONKEY);
        copyObj.setIsActive(isActive);
        copyObj.setUuid(uuid);

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
    protected TUserLevelSetting copyInto(TUserLevelSetting copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setUserLevel(userLevel);
        copyObj.setACTIONKEY(aCTIONKEY);
        copyObj.setIsActive(isActive);
        copyObj.setUuid(uuid);

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
    public TUserLevelSettingPeer getPeer()
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
        return TUserLevelSettingPeer.getTableMap();
    }

  
    /**
     * Creates a TUserLevelSettingBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TUserLevelSettingBean with the contents of this object
     */
    public TUserLevelSettingBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TUserLevelSettingBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TUserLevelSettingBean with the contents of this object
     */
    public TUserLevelSettingBean getBean(IdentityMap createdBeans)
    {
        TUserLevelSettingBean result = (TUserLevelSettingBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TUserLevelSettingBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setUserLevel(getUserLevel());
        result.setACTIONKEY(getACTIONKEY());
        result.setIsActive(getIsActive());
        result.setUuid(getUuid());





        if (aTUserLevel != null)
        {
            TUserLevelBean relatedBean = aTUserLevel.getBean(createdBeans);
            result.setTUserLevelBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TUserLevelSetting with the contents
     * of a TUserLevelSettingBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TUserLevelSettingBean which contents are used to create
     *        the resulting class
     * @return an instance of TUserLevelSetting with the contents of bean
     */
    public static TUserLevelSetting createTUserLevelSetting(TUserLevelSettingBean bean)
        throws TorqueException
    {
        return createTUserLevelSetting(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TUserLevelSetting with the contents
     * of a TUserLevelSettingBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TUserLevelSettingBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TUserLevelSetting with the contents of bean
     */

    public static TUserLevelSetting createTUserLevelSetting(TUserLevelSettingBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TUserLevelSetting result = (TUserLevelSetting) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TUserLevelSetting();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setUserLevel(bean.getUserLevel());
        result.setACTIONKEY(bean.getACTIONKEY());
        result.setIsActive(bean.getIsActive());
        result.setUuid(bean.getUuid());





        {
            TUserLevelBean relatedBean = bean.getTUserLevelBean();
            if (relatedBean != null)
            {
                TUserLevel relatedObject = TUserLevel.createTUserLevel(relatedBean, createdObjects);
                result.setTUserLevel(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TUserLevelSetting:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("UserLevel = ")
           .append(getUserLevel())
           .append("\n");
        str.append("ACTIONKEY = ")
           .append(getACTIONKEY())
           .append("\n");
        str.append("IsActive = ")
           .append(getIsActive())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
