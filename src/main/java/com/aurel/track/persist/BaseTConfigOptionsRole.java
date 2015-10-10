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



import com.aurel.track.persist.TFieldConfig;
import com.aurel.track.persist.TFieldConfigPeer;
import com.aurel.track.persist.TRole;
import com.aurel.track.persist.TRolePeer;
import com.aurel.track.persist.TOption;
import com.aurel.track.persist.TOptionPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TConfigOptionsRoleBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TOptionBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TConfigOptionsRole
 */
public abstract class BaseTConfigOptionsRole extends TpBaseObject
{
    /** The Peer class */
    private static final TConfigOptionsRolePeer peer =
        new TConfigOptionsRolePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the configKey field */
    private Integer configKey;

    /** The value for the roleKey field */
    private Integer roleKey;

    /** The value for the optionKey field */
    private Integer optionKey;

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
     * Get the ConfigKey
     *
     * @return Integer
     */
    public Integer getConfigKey()
    {
        return configKey;
    }


    /**
     * Set the value of ConfigKey
     *
     * @param v new value
     */
    public void setConfigKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.configKey, v))
        {
            this.configKey = v;
            setModified(true);
        }


        if (aTFieldConfig != null && !ObjectUtils.equals(aTFieldConfig.getObjectID(), v))
        {
            aTFieldConfig = null;
        }

    }

    /**
     * Get the RoleKey
     *
     * @return Integer
     */
    public Integer getRoleKey()
    {
        return roleKey;
    }


    /**
     * Set the value of RoleKey
     *
     * @param v new value
     */
    public void setRoleKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.roleKey, v))
        {
            this.roleKey = v;
            setModified(true);
        }


        if (aTRole != null && !ObjectUtils.equals(aTRole.getObjectID(), v))
        {
            aTRole = null;
        }

    }

    /**
     * Get the OptionKey
     *
     * @return Integer
     */
    public Integer getOptionKey()
    {
        return optionKey;
    }


    /**
     * Set the value of OptionKey
     *
     * @param v new value
     */
    public void setOptionKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.optionKey, v))
        {
            this.optionKey = v;
            setModified(true);
        }


        if (aTOption != null && !ObjectUtils.equals(aTOption.getObjectID(), v))
        {
            aTOption = null;
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

    



    private TFieldConfig aTFieldConfig;

    /**
     * Declares an association between this object and a TFieldConfig object
     *
     * @param v TFieldConfig
     * @throws TorqueException
     */
    public void setTFieldConfig(TFieldConfig v) throws TorqueException
    {
        if (v == null)
        {
            setConfigKey((Integer) null);
        }
        else
        {
            setConfigKey(v.getObjectID());
        }
        aTFieldConfig = v;
    }


    /**
     * Returns the associated TFieldConfig object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TFieldConfig object
     * @throws TorqueException
     */
    public TFieldConfig getTFieldConfig()
        throws TorqueException
    {
        if (aTFieldConfig == null && (!ObjectUtils.equals(this.configKey, null)))
        {
            aTFieldConfig = TFieldConfigPeer.retrieveByPK(SimpleKey.keyFor(this.configKey));
        }
        return aTFieldConfig;
    }

    /**
     * Return the associated TFieldConfig object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TFieldConfig object
     * @throws TorqueException
     */
    public TFieldConfig getTFieldConfig(Connection connection)
        throws TorqueException
    {
        if (aTFieldConfig == null && (!ObjectUtils.equals(this.configKey, null)))
        {
            aTFieldConfig = TFieldConfigPeer.retrieveByPK(SimpleKey.keyFor(this.configKey), connection);
        }
        return aTFieldConfig;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTFieldConfigKey(ObjectKey key) throws TorqueException
    {

        setConfigKey(new Integer(((NumberKey) key).intValue()));
    }




    private TRole aTRole;

    /**
     * Declares an association between this object and a TRole object
     *
     * @param v TRole
     * @throws TorqueException
     */
    public void setTRole(TRole v) throws TorqueException
    {
        if (v == null)
        {
            setRoleKey((Integer) null);
        }
        else
        {
            setRoleKey(v.getObjectID());
        }
        aTRole = v;
    }


    /**
     * Returns the associated TRole object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TRole object
     * @throws TorqueException
     */
    public TRole getTRole()
        throws TorqueException
    {
        if (aTRole == null && (!ObjectUtils.equals(this.roleKey, null)))
        {
            aTRole = TRolePeer.retrieveByPK(SimpleKey.keyFor(this.roleKey));
        }
        return aTRole;
    }

    /**
     * Return the associated TRole object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TRole object
     * @throws TorqueException
     */
    public TRole getTRole(Connection connection)
        throws TorqueException
    {
        if (aTRole == null && (!ObjectUtils.equals(this.roleKey, null)))
        {
            aTRole = TRolePeer.retrieveByPK(SimpleKey.keyFor(this.roleKey), connection);
        }
        return aTRole;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTRoleKey(ObjectKey key) throws TorqueException
    {

        setRoleKey(new Integer(((NumberKey) key).intValue()));
    }




    private TOption aTOption;

    /**
     * Declares an association between this object and a TOption object
     *
     * @param v TOption
     * @throws TorqueException
     */
    public void setTOption(TOption v) throws TorqueException
    {
        if (v == null)
        {
            setOptionKey((Integer) null);
        }
        else
        {
            setOptionKey(v.getObjectID());
        }
        aTOption = v;
    }


    /**
     * Returns the associated TOption object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TOption object
     * @throws TorqueException
     */
    public TOption getTOption()
        throws TorqueException
    {
        if (aTOption == null && (!ObjectUtils.equals(this.optionKey, null)))
        {
            aTOption = TOptionPeer.retrieveByPK(SimpleKey.keyFor(this.optionKey));
        }
        return aTOption;
    }

    /**
     * Return the associated TOption object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TOption object
     * @throws TorqueException
     */
    public TOption getTOption(Connection connection)
        throws TorqueException
    {
        if (aTOption == null && (!ObjectUtils.equals(this.optionKey, null)))
        {
            aTOption = TOptionPeer.retrieveByPK(SimpleKey.keyFor(this.optionKey), connection);
        }
        return aTOption;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTOptionKey(ObjectKey key) throws TorqueException
    {

        setOptionKey(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("ConfigKey");
            fieldNames.add("RoleKey");
            fieldNames.add("OptionKey");
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
        if (name.equals("ConfigKey"))
        {
            return getConfigKey();
        }
        if (name.equals("RoleKey"))
        {
            return getRoleKey();
        }
        if (name.equals("OptionKey"))
        {
            return getOptionKey();
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
        if (name.equals("ConfigKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setConfigKey((Integer) value);
            return true;
        }
        if (name.equals("RoleKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRoleKey((Integer) value);
            return true;
        }
        if (name.equals("OptionKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOptionKey((Integer) value);
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
        if (name.equals(TConfigOptionsRolePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TConfigOptionsRolePeer.CONFIGKEY))
        {
            return getConfigKey();
        }
        if (name.equals(TConfigOptionsRolePeer.ROLEKEY))
        {
            return getRoleKey();
        }
        if (name.equals(TConfigOptionsRolePeer.OPTIONKEY))
        {
            return getOptionKey();
        }
        if (name.equals(TConfigOptionsRolePeer.TPUUID))
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
      if (TConfigOptionsRolePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TConfigOptionsRolePeer.CONFIGKEY.equals(name))
        {
            return setByName("ConfigKey", value);
        }
      if (TConfigOptionsRolePeer.ROLEKEY.equals(name))
        {
            return setByName("RoleKey", value);
        }
      if (TConfigOptionsRolePeer.OPTIONKEY.equals(name))
        {
            return setByName("OptionKey", value);
        }
      if (TConfigOptionsRolePeer.TPUUID.equals(name))
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
            return getConfigKey();
        }
        if (pos == 2)
        {
            return getRoleKey();
        }
        if (pos == 3)
        {
            return getOptionKey();
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
            return setByName("ConfigKey", value);
        }
    if (position == 2)
        {
            return setByName("RoleKey", value);
        }
    if (position == 3)
        {
            return setByName("OptionKey", value);
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
        save(TConfigOptionsRolePeer.DATABASE_NAME);
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
                    TConfigOptionsRolePeer.doInsert((TConfigOptionsRole) this, con);
                    setNew(false);
                }
                else
                {
                    TConfigOptionsRolePeer.doUpdate((TConfigOptionsRole) this, con);
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
    public TConfigOptionsRole copy() throws TorqueException
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
    public TConfigOptionsRole copy(Connection con) throws TorqueException
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
    public TConfigOptionsRole copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TConfigOptionsRole(), deepcopy);
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
    public TConfigOptionsRole copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TConfigOptionsRole(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TConfigOptionsRole copyInto(TConfigOptionsRole copyObj) throws TorqueException
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
    protected TConfigOptionsRole copyInto(TConfigOptionsRole copyObj, Connection con) throws TorqueException
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
    protected TConfigOptionsRole copyInto(TConfigOptionsRole copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setConfigKey(configKey);
        copyObj.setRoleKey(roleKey);
        copyObj.setOptionKey(optionKey);
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
    protected TConfigOptionsRole copyInto(TConfigOptionsRole copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setConfigKey(configKey);
        copyObj.setRoleKey(roleKey);
        copyObj.setOptionKey(optionKey);
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
    public TConfigOptionsRolePeer getPeer()
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
        return TConfigOptionsRolePeer.getTableMap();
    }

  
    /**
     * Creates a TConfigOptionsRoleBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TConfigOptionsRoleBean with the contents of this object
     */
    public TConfigOptionsRoleBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TConfigOptionsRoleBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TConfigOptionsRoleBean with the contents of this object
     */
    public TConfigOptionsRoleBean getBean(IdentityMap createdBeans)
    {
        TConfigOptionsRoleBean result = (TConfigOptionsRoleBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TConfigOptionsRoleBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setConfigKey(getConfigKey());
        result.setRoleKey(getRoleKey());
        result.setOptionKey(getOptionKey());
        result.setUuid(getUuid());





        if (aTFieldConfig != null)
        {
            TFieldConfigBean relatedBean = aTFieldConfig.getBean(createdBeans);
            result.setTFieldConfigBean(relatedBean);
        }



        if (aTRole != null)
        {
            TRoleBean relatedBean = aTRole.getBean(createdBeans);
            result.setTRoleBean(relatedBean);
        }



        if (aTOption != null)
        {
            TOptionBean relatedBean = aTOption.getBean(createdBeans);
            result.setTOptionBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TConfigOptionsRole with the contents
     * of a TConfigOptionsRoleBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TConfigOptionsRoleBean which contents are used to create
     *        the resulting class
     * @return an instance of TConfigOptionsRole with the contents of bean
     */
    public static TConfigOptionsRole createTConfigOptionsRole(TConfigOptionsRoleBean bean)
        throws TorqueException
    {
        return createTConfigOptionsRole(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TConfigOptionsRole with the contents
     * of a TConfigOptionsRoleBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TConfigOptionsRoleBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TConfigOptionsRole with the contents of bean
     */

    public static TConfigOptionsRole createTConfigOptionsRole(TConfigOptionsRoleBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TConfigOptionsRole result = (TConfigOptionsRole) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TConfigOptionsRole();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setConfigKey(bean.getConfigKey());
        result.setRoleKey(bean.getRoleKey());
        result.setOptionKey(bean.getOptionKey());
        result.setUuid(bean.getUuid());





        {
            TFieldConfigBean relatedBean = bean.getTFieldConfigBean();
            if (relatedBean != null)
            {
                TFieldConfig relatedObject = TFieldConfig.createTFieldConfig(relatedBean, createdObjects);
                result.setTFieldConfig(relatedObject);
            }
        }



        {
            TRoleBean relatedBean = bean.getTRoleBean();
            if (relatedBean != null)
            {
                TRole relatedObject = TRole.createTRole(relatedBean, createdObjects);
                result.setTRole(relatedObject);
            }
        }



        {
            TOptionBean relatedBean = bean.getTOptionBean();
            if (relatedBean != null)
            {
                TOption relatedObject = TOption.createTOption(relatedBean, createdObjects);
                result.setTOption(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TConfigOptionsRole:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("ConfigKey = ")
           .append(getConfigKey())
           .append("\n");
        str.append("RoleKey = ")
           .append(getRoleKey())
           .append("\n");
        str.append("OptionKey = ")
           .append(getOptionKey())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
