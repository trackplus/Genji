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




  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TLocalizedResourcesBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TLocalizedResources
 */
public abstract class BaseTLocalizedResources extends TpBaseObject
{
    /** The Peer class */
    private static final TLocalizedResourcesPeer peer =
        new TLocalizedResourcesPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the tableName field */
    private String tableName;

    /** The value for the primaryKeyValue field */
    private Integer primaryKeyValue;

    /** The value for the fieldName field */
    private String fieldName;

    /** The value for the localizedText field */
    private String localizedText;

    /** The value for the locale field */
    private String locale;

    /** The value for the textChanged field */
    private String textChanged = "N";

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
     * Get the TableName
     *
     * @return String
     */
    public String getTableName()
    {
        return tableName;
    }


    /**
     * Set the value of TableName
     *
     * @param v new value
     */
    public void setTableName(String v) 
    {

        if (!ObjectUtils.equals(this.tableName, v))
        {
            this.tableName = v;
            setModified(true);
        }


    }

    /**
     * Get the PrimaryKeyValue
     *
     * @return Integer
     */
    public Integer getPrimaryKeyValue()
    {
        return primaryKeyValue;
    }


    /**
     * Set the value of PrimaryKeyValue
     *
     * @param v new value
     */
    public void setPrimaryKeyValue(Integer v) 
    {

        if (!ObjectUtils.equals(this.primaryKeyValue, v))
        {
            this.primaryKeyValue = v;
            setModified(true);
        }


    }

    /**
     * Get the FieldName
     *
     * @return String
     */
    public String getFieldName()
    {
        return fieldName;
    }


    /**
     * Set the value of FieldName
     *
     * @param v new value
     */
    public void setFieldName(String v) 
    {

        if (!ObjectUtils.equals(this.fieldName, v))
        {
            this.fieldName = v;
            setModified(true);
        }


    }

    /**
     * Get the LocalizedText
     *
     * @return String
     */
    public String getLocalizedText()
    {
        return localizedText;
    }


    /**
     * Set the value of LocalizedText
     *
     * @param v new value
     */
    public void setLocalizedText(String v) 
    {

        if (!ObjectUtils.equals(this.localizedText, v))
        {
            this.localizedText = v;
            setModified(true);
        }


    }

    /**
     * Get the Locale
     *
     * @return String
     */
    public String getLocale()
    {
        return locale;
    }


    /**
     * Set the value of Locale
     *
     * @param v new value
     */
    public void setLocale(String v) 
    {

        if (!ObjectUtils.equals(this.locale, v))
        {
            this.locale = v;
            setModified(true);
        }


    }

    /**
     * Get the TextChanged
     *
     * @return String
     */
    public String getTextChanged()
    {
        return textChanged;
    }


    /**
     * Set the value of TextChanged
     *
     * @param v new value
     */
    public void setTextChanged(String v) 
    {

        if (!ObjectUtils.equals(this.textChanged, v))
        {
            this.textChanged = v;
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
            fieldNames.add("TableName");
            fieldNames.add("PrimaryKeyValue");
            fieldNames.add("FieldName");
            fieldNames.add("LocalizedText");
            fieldNames.add("Locale");
            fieldNames.add("TextChanged");
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
        if (name.equals("TableName"))
        {
            return getTableName();
        }
        if (name.equals("PrimaryKeyValue"))
        {
            return getPrimaryKeyValue();
        }
        if (name.equals("FieldName"))
        {
            return getFieldName();
        }
        if (name.equals("LocalizedText"))
        {
            return getLocalizedText();
        }
        if (name.equals("Locale"))
        {
            return getLocale();
        }
        if (name.equals("TextChanged"))
        {
            return getTextChanged();
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
        if (name.equals("TableName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTableName((String) value);
            return true;
        }
        if (name.equals("PrimaryKeyValue"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPrimaryKeyValue((Integer) value);
            return true;
        }
        if (name.equals("FieldName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldName((String) value);
            return true;
        }
        if (name.equals("LocalizedText"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLocalizedText((String) value);
            return true;
        }
        if (name.equals("Locale"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLocale((String) value);
            return true;
        }
        if (name.equals("TextChanged"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTextChanged((String) value);
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
        if (name.equals(TLocalizedResourcesPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TLocalizedResourcesPeer.TABLENAME))
        {
            return getTableName();
        }
        if (name.equals(TLocalizedResourcesPeer.PRIMARYKEYVALUE))
        {
            return getPrimaryKeyValue();
        }
        if (name.equals(TLocalizedResourcesPeer.FIELDNAME))
        {
            return getFieldName();
        }
        if (name.equals(TLocalizedResourcesPeer.LOCALIZEDTEXT))
        {
            return getLocalizedText();
        }
        if (name.equals(TLocalizedResourcesPeer.LOCALE))
        {
            return getLocale();
        }
        if (name.equals(TLocalizedResourcesPeer.TEXTCHANGED))
        {
            return getTextChanged();
        }
        if (name.equals(TLocalizedResourcesPeer.TPUUID))
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
      if (TLocalizedResourcesPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TLocalizedResourcesPeer.TABLENAME.equals(name))
        {
            return setByName("TableName", value);
        }
      if (TLocalizedResourcesPeer.PRIMARYKEYVALUE.equals(name))
        {
            return setByName("PrimaryKeyValue", value);
        }
      if (TLocalizedResourcesPeer.FIELDNAME.equals(name))
        {
            return setByName("FieldName", value);
        }
      if (TLocalizedResourcesPeer.LOCALIZEDTEXT.equals(name))
        {
            return setByName("LocalizedText", value);
        }
      if (TLocalizedResourcesPeer.LOCALE.equals(name))
        {
            return setByName("Locale", value);
        }
      if (TLocalizedResourcesPeer.TEXTCHANGED.equals(name))
        {
            return setByName("TextChanged", value);
        }
      if (TLocalizedResourcesPeer.TPUUID.equals(name))
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
            return getTableName();
        }
        if (pos == 2)
        {
            return getPrimaryKeyValue();
        }
        if (pos == 3)
        {
            return getFieldName();
        }
        if (pos == 4)
        {
            return getLocalizedText();
        }
        if (pos == 5)
        {
            return getLocale();
        }
        if (pos == 6)
        {
            return getTextChanged();
        }
        if (pos == 7)
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
            return setByName("TableName", value);
        }
    if (position == 2)
        {
            return setByName("PrimaryKeyValue", value);
        }
    if (position == 3)
        {
            return setByName("FieldName", value);
        }
    if (position == 4)
        {
            return setByName("LocalizedText", value);
        }
    if (position == 5)
        {
            return setByName("Locale", value);
        }
    if (position == 6)
        {
            return setByName("TextChanged", value);
        }
    if (position == 7)
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
        save(TLocalizedResourcesPeer.DATABASE_NAME);
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
                    TLocalizedResourcesPeer.doInsert((TLocalizedResources) this, con);
                    setNew(false);
                }
                else
                {
                    TLocalizedResourcesPeer.doUpdate((TLocalizedResources) this, con);
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
    public TLocalizedResources copy() throws TorqueException
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
    public TLocalizedResources copy(Connection con) throws TorqueException
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
    public TLocalizedResources copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TLocalizedResources(), deepcopy);
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
    public TLocalizedResources copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TLocalizedResources(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TLocalizedResources copyInto(TLocalizedResources copyObj) throws TorqueException
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
    protected TLocalizedResources copyInto(TLocalizedResources copyObj, Connection con) throws TorqueException
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
    protected TLocalizedResources copyInto(TLocalizedResources copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setTableName(tableName);
        copyObj.setPrimaryKeyValue(primaryKeyValue);
        copyObj.setFieldName(fieldName);
        copyObj.setLocalizedText(localizedText);
        copyObj.setLocale(locale);
        copyObj.setTextChanged(textChanged);
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
    protected TLocalizedResources copyInto(TLocalizedResources copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setTableName(tableName);
        copyObj.setPrimaryKeyValue(primaryKeyValue);
        copyObj.setFieldName(fieldName);
        copyObj.setLocalizedText(localizedText);
        copyObj.setLocale(locale);
        copyObj.setTextChanged(textChanged);
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
    public TLocalizedResourcesPeer getPeer()
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
        return TLocalizedResourcesPeer.getTableMap();
    }

  
    /**
     * Creates a TLocalizedResourcesBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TLocalizedResourcesBean with the contents of this object
     */
    public TLocalizedResourcesBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TLocalizedResourcesBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TLocalizedResourcesBean with the contents of this object
     */
    public TLocalizedResourcesBean getBean(IdentityMap createdBeans)
    {
        TLocalizedResourcesBean result = (TLocalizedResourcesBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TLocalizedResourcesBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setTableName(getTableName());
        result.setPrimaryKeyValue(getPrimaryKeyValue());
        result.setFieldName(getFieldName());
        result.setLocalizedText(getLocalizedText());
        result.setLocale(getLocale());
        result.setTextChanged(getTextChanged());
        result.setUuid(getUuid());


        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TLocalizedResources with the contents
     * of a TLocalizedResourcesBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TLocalizedResourcesBean which contents are used to create
     *        the resulting class
     * @return an instance of TLocalizedResources with the contents of bean
     */
    public static TLocalizedResources createTLocalizedResources(TLocalizedResourcesBean bean)
        throws TorqueException
    {
        return createTLocalizedResources(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TLocalizedResources with the contents
     * of a TLocalizedResourcesBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TLocalizedResourcesBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TLocalizedResources with the contents of bean
     */

    public static TLocalizedResources createTLocalizedResources(TLocalizedResourcesBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TLocalizedResources result = (TLocalizedResources) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TLocalizedResources();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setTableName(bean.getTableName());
        result.setPrimaryKeyValue(bean.getPrimaryKeyValue());
        result.setFieldName(bean.getFieldName());
        result.setLocalizedText(bean.getLocalizedText());
        result.setLocale(bean.getLocale());
        result.setTextChanged(bean.getTextChanged());
        result.setUuid(bean.getUuid());


    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TLocalizedResources:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("TableName = ")
           .append(getTableName())
           .append("\n");
        str.append("PrimaryKeyValue = ")
           .append(getPrimaryKeyValue())
           .append("\n");
        str.append("FieldName = ")
           .append(getFieldName())
           .append("\n");
        str.append("LocalizedText = ")
           .append(getLocalizedText())
           .append("\n");
        str.append("Locale = ")
           .append(getLocale())
           .append("\n");
        str.append("TextChanged = ")
           .append(getTextChanged())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
