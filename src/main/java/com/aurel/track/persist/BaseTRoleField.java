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



import com.aurel.track.persist.TRole;
import com.aurel.track.persist.TRolePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.beans.TRoleBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TRoleField
 */
public abstract class BaseTRoleField extends TpBaseObject
{
    /** The Peer class */
    private static final TRoleFieldPeer peer =
        new TRoleFieldPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the roleKey field */
    private Integer roleKey;

    /** The value for the fieldKey field */
    private Integer fieldKey;

    /** The value for the accessRight field */
    private Integer accessRight;

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
     * Get the FieldKey
     *
     * @return Integer
     */
    public Integer getFieldKey()
    {
        return fieldKey;
    }


    /**
     * Set the value of FieldKey
     *
     * @param v new value
     */
    public void setFieldKey(Integer v) 
    {

        if (!ObjectUtils.equals(this.fieldKey, v))
        {
            this.fieldKey = v;
            setModified(true);
        }


    }

    /**
     * Get the AccessRight
     *
     * @return Integer
     */
    public Integer getAccessRight()
    {
        return accessRight;
    }


    /**
     * Set the value of AccessRight
     *
     * @param v new value
     */
    public void setAccessRight(Integer v) 
    {

        if (!ObjectUtils.equals(this.accessRight, v))
        {
            this.accessRight = v;
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
            fieldNames.add("RoleKey");
            fieldNames.add("FieldKey");
            fieldNames.add("AccessRight");
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
        if (name.equals("RoleKey"))
        {
            return getRoleKey();
        }
        if (name.equals("FieldKey"))
        {
            return getFieldKey();
        }
        if (name.equals("AccessRight"))
        {
            return getAccessRight();
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
        if (name.equals("FieldKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldKey((Integer) value);
            return true;
        }
        if (name.equals("AccessRight"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAccessRight((Integer) value);
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
        if (name.equals(TRoleFieldPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TRoleFieldPeer.ROLEKEY))
        {
            return getRoleKey();
        }
        if (name.equals(TRoleFieldPeer.FIELDKEY))
        {
            return getFieldKey();
        }
        if (name.equals(TRoleFieldPeer.ACCESSRIGHT))
        {
            return getAccessRight();
        }
        if (name.equals(TRoleFieldPeer.TPUUID))
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
      if (TRoleFieldPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TRoleFieldPeer.ROLEKEY.equals(name))
        {
            return setByName("RoleKey", value);
        }
      if (TRoleFieldPeer.FIELDKEY.equals(name))
        {
            return setByName("FieldKey", value);
        }
      if (TRoleFieldPeer.ACCESSRIGHT.equals(name))
        {
            return setByName("AccessRight", value);
        }
      if (TRoleFieldPeer.TPUUID.equals(name))
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
            return getRoleKey();
        }
        if (pos == 2)
        {
            return getFieldKey();
        }
        if (pos == 3)
        {
            return getAccessRight();
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
            return setByName("RoleKey", value);
        }
    if (position == 2)
        {
            return setByName("FieldKey", value);
        }
    if (position == 3)
        {
            return setByName("AccessRight", value);
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
        save(TRoleFieldPeer.DATABASE_NAME);
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
                    TRoleFieldPeer.doInsert((TRoleField) this, con);
                    setNew(false);
                }
                else
                {
                    TRoleFieldPeer.doUpdate((TRoleField) this, con);
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
    public TRoleField copy() throws TorqueException
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
    public TRoleField copy(Connection con) throws TorqueException
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
    public TRoleField copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TRoleField(), deepcopy);
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
    public TRoleField copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TRoleField(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TRoleField copyInto(TRoleField copyObj) throws TorqueException
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
    protected TRoleField copyInto(TRoleField copyObj, Connection con) throws TorqueException
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
    protected TRoleField copyInto(TRoleField copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setRoleKey(roleKey);
        copyObj.setFieldKey(fieldKey);
        copyObj.setAccessRight(accessRight);
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
    protected TRoleField copyInto(TRoleField copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setRoleKey(roleKey);
        copyObj.setFieldKey(fieldKey);
        copyObj.setAccessRight(accessRight);
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
    public TRoleFieldPeer getPeer()
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
        return TRoleFieldPeer.getTableMap();
    }

  
    /**
     * Creates a TRoleFieldBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TRoleFieldBean with the contents of this object
     */
    public TRoleFieldBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TRoleFieldBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TRoleFieldBean with the contents of this object
     */
    public TRoleFieldBean getBean(IdentityMap createdBeans)
    {
        TRoleFieldBean result = (TRoleFieldBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TRoleFieldBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setRoleKey(getRoleKey());
        result.setFieldKey(getFieldKey());
        result.setAccessRight(getAccessRight());
        result.setUuid(getUuid());





        if (aTRole != null)
        {
            TRoleBean relatedBean = aTRole.getBean(createdBeans);
            result.setTRoleBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TRoleField with the contents
     * of a TRoleFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TRoleFieldBean which contents are used to create
     *        the resulting class
     * @return an instance of TRoleField with the contents of bean
     */
    public static TRoleField createTRoleField(TRoleFieldBean bean)
        throws TorqueException
    {
        return createTRoleField(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TRoleField with the contents
     * of a TRoleFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TRoleFieldBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TRoleField with the contents of bean
     */

    public static TRoleField createTRoleField(TRoleFieldBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TRoleField result = (TRoleField) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TRoleField();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setRoleKey(bean.getRoleKey());
        result.setFieldKey(bean.getFieldKey());
        result.setAccessRight(bean.getAccessRight());
        result.setUuid(bean.getUuid());





        {
            TRoleBean relatedBean = bean.getTRoleBean();
            if (relatedBean != null)
            {
                TRole relatedObject = TRole.createTRole(relatedBean, createdObjects);
                result.setTRole(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TRoleField:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("RoleKey = ")
           .append(getRoleKey())
           .append("\n");
        str.append("FieldKey = ")
           .append(getFieldKey())
           .append("\n");
        str.append("AccessRight = ")
           .append(getAccessRight())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
