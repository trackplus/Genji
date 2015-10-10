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

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TFieldConfigBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TGeneralSettings
 */
public abstract class BaseTGeneralSettings extends TpBaseObject
{
    /** The Peer class */
    private static final TGeneralSettingsPeer peer =
        new TGeneralSettingsPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the config field */
    private Integer config;

    /** The value for the integerValue field */
    private Integer integerValue;

    /** The value for the doubleValue field */
    private Double doubleValue;

    /** The value for the textValue field */
    private String textValue;

    /** The value for the dateValue field */
    private Date dateValue;

    /** The value for the characterValue field */
    private String characterValue;

    /** The value for the parameterCode field */
    private Integer parameterCode;

    /** The value for the validValue field */
    private Integer validValue;

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
     * Get the Config
     *
     * @return Integer
     */
    public Integer getConfig()
    {
        return config;
    }


    /**
     * Set the value of Config
     *
     * @param v new value
     */
    public void setConfig(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.config, v))
        {
            this.config = v;
            setModified(true);
        }


        if (aTFieldConfig != null && !ObjectUtils.equals(aTFieldConfig.getObjectID(), v))
        {
            aTFieldConfig = null;
        }

    }

    /**
     * Get the IntegerValue
     *
     * @return Integer
     */
    public Integer getIntegerValue()
    {
        return integerValue;
    }


    /**
     * Set the value of IntegerValue
     *
     * @param v new value
     */
    public void setIntegerValue(Integer v) 
    {

        if (!ObjectUtils.equals(this.integerValue, v))
        {
            this.integerValue = v;
            setModified(true);
        }


    }

    /**
     * Get the DoubleValue
     *
     * @return Double
     */
    public Double getDoubleValue()
    {
        return doubleValue;
    }


    /**
     * Set the value of DoubleValue
     *
     * @param v new value
     */
    public void setDoubleValue(Double v) 
    {

        if (!ObjectUtils.equals(this.doubleValue, v))
        {
            this.doubleValue = v;
            setModified(true);
        }


    }

    /**
     * Get the TextValue
     *
     * @return String
     */
    public String getTextValue()
    {
        return textValue;
    }


    /**
     * Set the value of TextValue
     *
     * @param v new value
     */
    public void setTextValue(String v) 
    {

        if (!ObjectUtils.equals(this.textValue, v))
        {
            this.textValue = v;
            setModified(true);
        }


    }

    /**
     * Get the DateValue
     *
     * @return Date
     */
    public Date getDateValue()
    {
        return dateValue;
    }


    /**
     * Set the value of DateValue
     *
     * @param v new value
     */
    public void setDateValue(Date v) 
    {

        if (!ObjectUtils.equals(this.dateValue, v))
        {
            this.dateValue = v;
            setModified(true);
        }


    }

    /**
     * Get the CharacterValue
     *
     * @return String
     */
    public String getCharacterValue()
    {
        return characterValue;
    }


    /**
     * Set the value of CharacterValue
     *
     * @param v new value
     */
    public void setCharacterValue(String v) 
    {

        if (!ObjectUtils.equals(this.characterValue, v))
        {
            this.characterValue = v;
            setModified(true);
        }


    }

    /**
     * Get the ParameterCode
     *
     * @return Integer
     */
    public Integer getParameterCode()
    {
        return parameterCode;
    }


    /**
     * Set the value of ParameterCode
     *
     * @param v new value
     */
    public void setParameterCode(Integer v) 
    {

        if (!ObjectUtils.equals(this.parameterCode, v))
        {
            this.parameterCode = v;
            setModified(true);
        }


    }

    /**
     * Get the ValidValue
     *
     * @return Integer
     */
    public Integer getValidValue()
    {
        return validValue;
    }


    /**
     * Set the value of ValidValue
     *
     * @param v new value
     */
    public void setValidValue(Integer v) 
    {

        if (!ObjectUtils.equals(this.validValue, v))
        {
            this.validValue = v;
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
            setConfig((Integer) null);
        }
        else
        {
            setConfig(v.getObjectID());
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
        if (aTFieldConfig == null && (!ObjectUtils.equals(this.config, null)))
        {
            aTFieldConfig = TFieldConfigPeer.retrieveByPK(SimpleKey.keyFor(this.config));
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
        if (aTFieldConfig == null && (!ObjectUtils.equals(this.config, null)))
        {
            aTFieldConfig = TFieldConfigPeer.retrieveByPK(SimpleKey.keyFor(this.config), connection);
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

        setConfig(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Config");
            fieldNames.add("IntegerValue");
            fieldNames.add("DoubleValue");
            fieldNames.add("TextValue");
            fieldNames.add("DateValue");
            fieldNames.add("CharacterValue");
            fieldNames.add("ParameterCode");
            fieldNames.add("ValidValue");
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
        if (name.equals("Config"))
        {
            return getConfig();
        }
        if (name.equals("IntegerValue"))
        {
            return getIntegerValue();
        }
        if (name.equals("DoubleValue"))
        {
            return getDoubleValue();
        }
        if (name.equals("TextValue"))
        {
            return getTextValue();
        }
        if (name.equals("DateValue"))
        {
            return getDateValue();
        }
        if (name.equals("CharacterValue"))
        {
            return getCharacterValue();
        }
        if (name.equals("ParameterCode"))
        {
            return getParameterCode();
        }
        if (name.equals("ValidValue"))
        {
            return getValidValue();
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
        if (name.equals("Config"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setConfig((Integer) value);
            return true;
        }
        if (name.equals("IntegerValue"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIntegerValue((Integer) value);
            return true;
        }
        if (name.equals("DoubleValue"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDoubleValue((Double) value);
            return true;
        }
        if (name.equals("TextValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTextValue((String) value);
            return true;
        }
        if (name.equals("DateValue"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDateValue((Date) value);
            return true;
        }
        if (name.equals("CharacterValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCharacterValue((String) value);
            return true;
        }
        if (name.equals("ParameterCode"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParameterCode((Integer) value);
            return true;
        }
        if (name.equals("ValidValue"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setValidValue((Integer) value);
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
        if (name.equals(TGeneralSettingsPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TGeneralSettingsPeer.CONFIG))
        {
            return getConfig();
        }
        if (name.equals(TGeneralSettingsPeer.INTEGERVALUE))
        {
            return getIntegerValue();
        }
        if (name.equals(TGeneralSettingsPeer.DOUBLEVALUE))
        {
            return getDoubleValue();
        }
        if (name.equals(TGeneralSettingsPeer.TEXTVALUE))
        {
            return getTextValue();
        }
        if (name.equals(TGeneralSettingsPeer.DATEVALUE))
        {
            return getDateValue();
        }
        if (name.equals(TGeneralSettingsPeer.CHARACTERVALUE))
        {
            return getCharacterValue();
        }
        if (name.equals(TGeneralSettingsPeer.PARAMETERCODE))
        {
            return getParameterCode();
        }
        if (name.equals(TGeneralSettingsPeer.VALIDVALUE))
        {
            return getValidValue();
        }
        if (name.equals(TGeneralSettingsPeer.TPUUID))
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
      if (TGeneralSettingsPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TGeneralSettingsPeer.CONFIG.equals(name))
        {
            return setByName("Config", value);
        }
      if (TGeneralSettingsPeer.INTEGERVALUE.equals(name))
        {
            return setByName("IntegerValue", value);
        }
      if (TGeneralSettingsPeer.DOUBLEVALUE.equals(name))
        {
            return setByName("DoubleValue", value);
        }
      if (TGeneralSettingsPeer.TEXTVALUE.equals(name))
        {
            return setByName("TextValue", value);
        }
      if (TGeneralSettingsPeer.DATEVALUE.equals(name))
        {
            return setByName("DateValue", value);
        }
      if (TGeneralSettingsPeer.CHARACTERVALUE.equals(name))
        {
            return setByName("CharacterValue", value);
        }
      if (TGeneralSettingsPeer.PARAMETERCODE.equals(name))
        {
            return setByName("ParameterCode", value);
        }
      if (TGeneralSettingsPeer.VALIDVALUE.equals(name))
        {
            return setByName("ValidValue", value);
        }
      if (TGeneralSettingsPeer.TPUUID.equals(name))
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
            return getConfig();
        }
        if (pos == 2)
        {
            return getIntegerValue();
        }
        if (pos == 3)
        {
            return getDoubleValue();
        }
        if (pos == 4)
        {
            return getTextValue();
        }
        if (pos == 5)
        {
            return getDateValue();
        }
        if (pos == 6)
        {
            return getCharacterValue();
        }
        if (pos == 7)
        {
            return getParameterCode();
        }
        if (pos == 8)
        {
            return getValidValue();
        }
        if (pos == 9)
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
            return setByName("Config", value);
        }
    if (position == 2)
        {
            return setByName("IntegerValue", value);
        }
    if (position == 3)
        {
            return setByName("DoubleValue", value);
        }
    if (position == 4)
        {
            return setByName("TextValue", value);
        }
    if (position == 5)
        {
            return setByName("DateValue", value);
        }
    if (position == 6)
        {
            return setByName("CharacterValue", value);
        }
    if (position == 7)
        {
            return setByName("ParameterCode", value);
        }
    if (position == 8)
        {
            return setByName("ValidValue", value);
        }
    if (position == 9)
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
        save(TGeneralSettingsPeer.DATABASE_NAME);
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
                    TGeneralSettingsPeer.doInsert((TGeneralSettings) this, con);
                    setNew(false);
                }
                else
                {
                    TGeneralSettingsPeer.doUpdate((TGeneralSettings) this, con);
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
    public TGeneralSettings copy() throws TorqueException
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
    public TGeneralSettings copy(Connection con) throws TorqueException
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
    public TGeneralSettings copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TGeneralSettings(), deepcopy);
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
    public TGeneralSettings copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TGeneralSettings(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TGeneralSettings copyInto(TGeneralSettings copyObj) throws TorqueException
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
    protected TGeneralSettings copyInto(TGeneralSettings copyObj, Connection con) throws TorqueException
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
    protected TGeneralSettings copyInto(TGeneralSettings copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setConfig(config);
        copyObj.setIntegerValue(integerValue);
        copyObj.setDoubleValue(doubleValue);
        copyObj.setTextValue(textValue);
        copyObj.setDateValue(dateValue);
        copyObj.setCharacterValue(characterValue);
        copyObj.setParameterCode(parameterCode);
        copyObj.setValidValue(validValue);
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
    protected TGeneralSettings copyInto(TGeneralSettings copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setConfig(config);
        copyObj.setIntegerValue(integerValue);
        copyObj.setDoubleValue(doubleValue);
        copyObj.setTextValue(textValue);
        copyObj.setDateValue(dateValue);
        copyObj.setCharacterValue(characterValue);
        copyObj.setParameterCode(parameterCode);
        copyObj.setValidValue(validValue);
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
    public TGeneralSettingsPeer getPeer()
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
        return TGeneralSettingsPeer.getTableMap();
    }

  
    /**
     * Creates a TGeneralSettingsBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TGeneralSettingsBean with the contents of this object
     */
    public TGeneralSettingsBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TGeneralSettingsBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TGeneralSettingsBean with the contents of this object
     */
    public TGeneralSettingsBean getBean(IdentityMap createdBeans)
    {
        TGeneralSettingsBean result = (TGeneralSettingsBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TGeneralSettingsBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setConfig(getConfig());
        result.setIntegerValue(getIntegerValue());
        result.setDoubleValue(getDoubleValue());
        result.setTextValue(getTextValue());
        result.setDateValue(getDateValue());
        result.setCharacterValue(getCharacterValue());
        result.setParameterCode(getParameterCode());
        result.setValidValue(getValidValue());
        result.setUuid(getUuid());





        if (aTFieldConfig != null)
        {
            TFieldConfigBean relatedBean = aTFieldConfig.getBean(createdBeans);
            result.setTFieldConfigBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TGeneralSettings with the contents
     * of a TGeneralSettingsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TGeneralSettingsBean which contents are used to create
     *        the resulting class
     * @return an instance of TGeneralSettings with the contents of bean
     */
    public static TGeneralSettings createTGeneralSettings(TGeneralSettingsBean bean)
        throws TorqueException
    {
        return createTGeneralSettings(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TGeneralSettings with the contents
     * of a TGeneralSettingsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TGeneralSettingsBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TGeneralSettings with the contents of bean
     */

    public static TGeneralSettings createTGeneralSettings(TGeneralSettingsBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TGeneralSettings result = (TGeneralSettings) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TGeneralSettings();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setConfig(bean.getConfig());
        result.setIntegerValue(bean.getIntegerValue());
        result.setDoubleValue(bean.getDoubleValue());
        result.setTextValue(bean.getTextValue());
        result.setDateValue(bean.getDateValue());
        result.setCharacterValue(bean.getCharacterValue());
        result.setParameterCode(bean.getParameterCode());
        result.setValidValue(bean.getValidValue());
        result.setUuid(bean.getUuid());





        {
            TFieldConfigBean relatedBean = bean.getTFieldConfigBean();
            if (relatedBean != null)
            {
                TFieldConfig relatedObject = TFieldConfig.createTFieldConfig(relatedBean, createdObjects);
                result.setTFieldConfig(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TGeneralSettings:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Config = ")
           .append(getConfig())
           .append("\n");
        str.append("IntegerValue = ")
           .append(getIntegerValue())
           .append("\n");
        str.append("DoubleValue = ")
           .append(getDoubleValue())
           .append("\n");
        str.append("TextValue = ")
           .append(getTextValue())
           .append("\n");
        str.append("DateValue = ")
           .append(getDateValue())
           .append("\n");
        str.append("CharacterValue = ")
           .append(getCharacterValue())
           .append("\n");
        str.append("ParameterCode = ")
           .append(getParameterCode())
           .append("\n");
        str.append("ValidValue = ")
           .append(getValidValue())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
