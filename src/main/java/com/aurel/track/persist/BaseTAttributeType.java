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



import com.aurel.track.persist.TAttributeClass;
import com.aurel.track.persist.TAttributeClassPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TAttributeTypeBean;
import com.aurel.track.beans.TAttributeClassBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TAttributeType
 */
public abstract class BaseTAttributeType extends TpBaseObject
{
    /** The Peer class */
    private static final TAttributeTypePeer peer =
        new TAttributeTypePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the attributeTypeName field */
    private String attributeTypeName;

    /** The value for the attributeClass field */
    private Integer attributeClass;

    /** The value for the javaClassName field */
    private String javaClassName;

    /** The value for the validationKey field */
    private String validationKey;

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
     * Get the AttributeTypeName
     *
     * @return String
     */
    public String getAttributeTypeName()
    {
        return attributeTypeName;
    }


    /**
     * Set the value of AttributeTypeName
     *
     * @param v new value
     */
    public void setAttributeTypeName(String v) 
    {

        if (!ObjectUtils.equals(this.attributeTypeName, v))
        {
            this.attributeTypeName = v;
            setModified(true);
        }


    }

    /**
     * Get the AttributeClass
     *
     * @return Integer
     */
    public Integer getAttributeClass()
    {
        return attributeClass;
    }


    /**
     * Set the value of AttributeClass
     *
     * @param v new value
     */
    public void setAttributeClass(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.attributeClass, v))
        {
            this.attributeClass = v;
            setModified(true);
        }


        if (aTAttributeClass != null && !ObjectUtils.equals(aTAttributeClass.getObjectID(), v))
        {
            aTAttributeClass = null;
        }

    }

    /**
     * Get the JavaClassName
     *
     * @return String
     */
    public String getJavaClassName()
    {
        return javaClassName;
    }


    /**
     * Set the value of JavaClassName
     *
     * @param v new value
     */
    public void setJavaClassName(String v) 
    {

        if (!ObjectUtils.equals(this.javaClassName, v))
        {
            this.javaClassName = v;
            setModified(true);
        }


    }

    /**
     * Get the ValidationKey
     *
     * @return String
     */
    public String getValidationKey()
    {
        return validationKey;
    }


    /**
     * Set the value of ValidationKey
     *
     * @param v new value
     */
    public void setValidationKey(String v) 
    {

        if (!ObjectUtils.equals(this.validationKey, v))
        {
            this.validationKey = v;
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

    



    private TAttributeClass aTAttributeClass;

    /**
     * Declares an association between this object and a TAttributeClass object
     *
     * @param v TAttributeClass
     * @throws TorqueException
     */
    public void setTAttributeClass(TAttributeClass v) throws TorqueException
    {
        if (v == null)
        {
            setAttributeClass((Integer) null);
        }
        else
        {
            setAttributeClass(v.getObjectID());
        }
        aTAttributeClass = v;
    }


    /**
     * Returns the associated TAttributeClass object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TAttributeClass object
     * @throws TorqueException
     */
    public TAttributeClass getTAttributeClass()
        throws TorqueException
    {
        if (aTAttributeClass == null && (!ObjectUtils.equals(this.attributeClass, null)))
        {
            aTAttributeClass = TAttributeClassPeer.retrieveByPK(SimpleKey.keyFor(this.attributeClass));
        }
        return aTAttributeClass;
    }

    /**
     * Return the associated TAttributeClass object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TAttributeClass object
     * @throws TorqueException
     */
    public TAttributeClass getTAttributeClass(Connection connection)
        throws TorqueException
    {
        if (aTAttributeClass == null && (!ObjectUtils.equals(this.attributeClass, null)))
        {
            aTAttributeClass = TAttributeClassPeer.retrieveByPK(SimpleKey.keyFor(this.attributeClass), connection);
        }
        return aTAttributeClass;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTAttributeClassKey(ObjectKey key) throws TorqueException
    {

        setAttributeClass(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("AttributeTypeName");
            fieldNames.add("AttributeClass");
            fieldNames.add("JavaClassName");
            fieldNames.add("ValidationKey");
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
        if (name.equals("AttributeTypeName"))
        {
            return getAttributeTypeName();
        }
        if (name.equals("AttributeClass"))
        {
            return getAttributeClass();
        }
        if (name.equals("JavaClassName"))
        {
            return getJavaClassName();
        }
        if (name.equals("ValidationKey"))
        {
            return getValidationKey();
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
        if (name.equals("AttributeTypeName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAttributeTypeName((String) value);
            return true;
        }
        if (name.equals("AttributeClass"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAttributeClass((Integer) value);
            return true;
        }
        if (name.equals("JavaClassName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setJavaClassName((String) value);
            return true;
        }
        if (name.equals("ValidationKey"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setValidationKey((String) value);
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
        if (name.equals(TAttributeTypePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TAttributeTypePeer.ATTRIBUTETYPENAME))
        {
            return getAttributeTypeName();
        }
        if (name.equals(TAttributeTypePeer.ATTRIBUTECLASS))
        {
            return getAttributeClass();
        }
        if (name.equals(TAttributeTypePeer.JAVACLASSNAME))
        {
            return getJavaClassName();
        }
        if (name.equals(TAttributeTypePeer.VALIDATIONKEY))
        {
            return getValidationKey();
        }
        if (name.equals(TAttributeTypePeer.TPUUID))
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
      if (TAttributeTypePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TAttributeTypePeer.ATTRIBUTETYPENAME.equals(name))
        {
            return setByName("AttributeTypeName", value);
        }
      if (TAttributeTypePeer.ATTRIBUTECLASS.equals(name))
        {
            return setByName("AttributeClass", value);
        }
      if (TAttributeTypePeer.JAVACLASSNAME.equals(name))
        {
            return setByName("JavaClassName", value);
        }
      if (TAttributeTypePeer.VALIDATIONKEY.equals(name))
        {
            return setByName("ValidationKey", value);
        }
      if (TAttributeTypePeer.TPUUID.equals(name))
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
            return getAttributeTypeName();
        }
        if (pos == 2)
        {
            return getAttributeClass();
        }
        if (pos == 3)
        {
            return getJavaClassName();
        }
        if (pos == 4)
        {
            return getValidationKey();
        }
        if (pos == 5)
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
            return setByName("AttributeTypeName", value);
        }
    if (position == 2)
        {
            return setByName("AttributeClass", value);
        }
    if (position == 3)
        {
            return setByName("JavaClassName", value);
        }
    if (position == 4)
        {
            return setByName("ValidationKey", value);
        }
    if (position == 5)
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
        save(TAttributeTypePeer.DATABASE_NAME);
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
                    TAttributeTypePeer.doInsert((TAttributeType) this, con);
                    setNew(false);
                }
                else
                {
                    TAttributeTypePeer.doUpdate((TAttributeType) this, con);
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
    public TAttributeType copy() throws TorqueException
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
    public TAttributeType copy(Connection con) throws TorqueException
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
    public TAttributeType copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TAttributeType(), deepcopy);
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
    public TAttributeType copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TAttributeType(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TAttributeType copyInto(TAttributeType copyObj) throws TorqueException
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
    protected TAttributeType copyInto(TAttributeType copyObj, Connection con) throws TorqueException
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
    protected TAttributeType copyInto(TAttributeType copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAttributeTypeName(attributeTypeName);
        copyObj.setAttributeClass(attributeClass);
        copyObj.setJavaClassName(javaClassName);
        copyObj.setValidationKey(validationKey);
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
    protected TAttributeType copyInto(TAttributeType copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAttributeTypeName(attributeTypeName);
        copyObj.setAttributeClass(attributeClass);
        copyObj.setJavaClassName(javaClassName);
        copyObj.setValidationKey(validationKey);
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
    public TAttributeTypePeer getPeer()
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
        return TAttributeTypePeer.getTableMap();
    }

  
    /**
     * Creates a TAttributeTypeBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TAttributeTypeBean with the contents of this object
     */
    public TAttributeTypeBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TAttributeTypeBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TAttributeTypeBean with the contents of this object
     */
    public TAttributeTypeBean getBean(IdentityMap createdBeans)
    {
        TAttributeTypeBean result = (TAttributeTypeBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TAttributeTypeBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setAttributeTypeName(getAttributeTypeName());
        result.setAttributeClass(getAttributeClass());
        result.setJavaClassName(getJavaClassName());
        result.setValidationKey(getValidationKey());
        result.setUuid(getUuid());





        if (aTAttributeClass != null)
        {
            TAttributeClassBean relatedBean = aTAttributeClass.getBean(createdBeans);
            result.setTAttributeClassBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TAttributeType with the contents
     * of a TAttributeTypeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TAttributeTypeBean which contents are used to create
     *        the resulting class
     * @return an instance of TAttributeType with the contents of bean
     */
    public static TAttributeType createTAttributeType(TAttributeTypeBean bean)
        throws TorqueException
    {
        return createTAttributeType(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TAttributeType with the contents
     * of a TAttributeTypeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TAttributeTypeBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TAttributeType with the contents of bean
     */

    public static TAttributeType createTAttributeType(TAttributeTypeBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TAttributeType result = (TAttributeType) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TAttributeType();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setAttributeTypeName(bean.getAttributeTypeName());
        result.setAttributeClass(bean.getAttributeClass());
        result.setJavaClassName(bean.getJavaClassName());
        result.setValidationKey(bean.getValidationKey());
        result.setUuid(bean.getUuid());





        {
            TAttributeClassBean relatedBean = bean.getTAttributeClassBean();
            if (relatedBean != null)
            {
                TAttributeClass relatedObject = TAttributeClass.createTAttributeClass(relatedBean, createdObjects);
                result.setTAttributeClass(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TAttributeType:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("AttributeTypeName = ")
           .append(getAttributeTypeName())
           .append("\n");
        str.append("AttributeClass = ")
           .append(getAttributeClass())
           .append("\n");
        str.append("JavaClassName = ")
           .append(getJavaClassName())
           .append("\n");
        str.append("ValidationKey = ")
           .append(getValidationKey())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
