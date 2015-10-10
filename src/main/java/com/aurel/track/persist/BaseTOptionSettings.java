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



import com.aurel.track.persist.TList;
import com.aurel.track.persist.TListPeer;
import com.aurel.track.persist.TFieldConfig;
import com.aurel.track.persist.TFieldConfigPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TFieldConfigBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TOptionSettings
 */
public abstract class BaseTOptionSettings extends TpBaseObject
{
    /** The Peer class */
    private static final TOptionSettingsPeer peer =
        new TOptionSettingsPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the list field */
    private Integer list;

    /** The value for the config field */
    private Integer config;

    /** The value for the parameterCode field */
    private Integer parameterCode;

    /** The value for the multiple field */
    private String multiple = "N";

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
     * Get the List
     *
     * @return Integer
     */
    public Integer getList()
    {
        return list;
    }


    /**
     * Set the value of List
     *
     * @param v new value
     */
    public void setList(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.list, v))
        {
            this.list = v;
            setModified(true);
        }


        if (aTList != null && !ObjectUtils.equals(aTList.getObjectID(), v))
        {
            aTList = null;
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
     * Get the Multiple
     *
     * @return String
     */
    public String getMultiple()
    {
        return multiple;
    }


    /**
     * Set the value of Multiple
     *
     * @param v new value
     */
    public void setMultiple(String v) 
    {

        if (!ObjectUtils.equals(this.multiple, v))
        {
            this.multiple = v;
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

    



    private TList aTList;

    /**
     * Declares an association between this object and a TList object
     *
     * @param v TList
     * @throws TorqueException
     */
    public void setTList(TList v) throws TorqueException
    {
        if (v == null)
        {
            setList((Integer) null);
        }
        else
        {
            setList(v.getObjectID());
        }
        aTList = v;
    }


    /**
     * Returns the associated TList object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TList object
     * @throws TorqueException
     */
    public TList getTList()
        throws TorqueException
    {
        if (aTList == null && (!ObjectUtils.equals(this.list, null)))
        {
            aTList = TListPeer.retrieveByPK(SimpleKey.keyFor(this.list));
        }
        return aTList;
    }

    /**
     * Return the associated TList object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TList object
     * @throws TorqueException
     */
    public TList getTList(Connection connection)
        throws TorqueException
    {
        if (aTList == null && (!ObjectUtils.equals(this.list, null)))
        {
            aTList = TListPeer.retrieveByPK(SimpleKey.keyFor(this.list), connection);
        }
        return aTList;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTListKey(ObjectKey key) throws TorqueException
    {

        setList(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("List");
            fieldNames.add("Config");
            fieldNames.add("ParameterCode");
            fieldNames.add("Multiple");
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
        if (name.equals("List"))
        {
            return getList();
        }
        if (name.equals("Config"))
        {
            return getConfig();
        }
        if (name.equals("ParameterCode"))
        {
            return getParameterCode();
        }
        if (name.equals("Multiple"))
        {
            return getMultiple();
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
        if (name.equals("List"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setList((Integer) value);
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
        if (name.equals("Multiple"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMultiple((String) value);
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
        if (name.equals(TOptionSettingsPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TOptionSettingsPeer.LIST))
        {
            return getList();
        }
        if (name.equals(TOptionSettingsPeer.CONFIG))
        {
            return getConfig();
        }
        if (name.equals(TOptionSettingsPeer.PARAMETERCODE))
        {
            return getParameterCode();
        }
        if (name.equals(TOptionSettingsPeer.MULTIPLE))
        {
            return getMultiple();
        }
        if (name.equals(TOptionSettingsPeer.TPUUID))
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
      if (TOptionSettingsPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TOptionSettingsPeer.LIST.equals(name))
        {
            return setByName("List", value);
        }
      if (TOptionSettingsPeer.CONFIG.equals(name))
        {
            return setByName("Config", value);
        }
      if (TOptionSettingsPeer.PARAMETERCODE.equals(name))
        {
            return setByName("ParameterCode", value);
        }
      if (TOptionSettingsPeer.MULTIPLE.equals(name))
        {
            return setByName("Multiple", value);
        }
      if (TOptionSettingsPeer.TPUUID.equals(name))
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
            return getList();
        }
        if (pos == 2)
        {
            return getConfig();
        }
        if (pos == 3)
        {
            return getParameterCode();
        }
        if (pos == 4)
        {
            return getMultiple();
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
            return setByName("List", value);
        }
    if (position == 2)
        {
            return setByName("Config", value);
        }
    if (position == 3)
        {
            return setByName("ParameterCode", value);
        }
    if (position == 4)
        {
            return setByName("Multiple", value);
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
        save(TOptionSettingsPeer.DATABASE_NAME);
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
                    TOptionSettingsPeer.doInsert((TOptionSettings) this, con);
                    setNew(false);
                }
                else
                {
                    TOptionSettingsPeer.doUpdate((TOptionSettings) this, con);
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
    public TOptionSettings copy() throws TorqueException
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
    public TOptionSettings copy(Connection con) throws TorqueException
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
    public TOptionSettings copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TOptionSettings(), deepcopy);
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
    public TOptionSettings copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TOptionSettings(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TOptionSettings copyInto(TOptionSettings copyObj) throws TorqueException
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
    protected TOptionSettings copyInto(TOptionSettings copyObj, Connection con) throws TorqueException
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
    protected TOptionSettings copyInto(TOptionSettings copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setList(list);
        copyObj.setConfig(config);
        copyObj.setParameterCode(parameterCode);
        copyObj.setMultiple(multiple);
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
    protected TOptionSettings copyInto(TOptionSettings copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setList(list);
        copyObj.setConfig(config);
        copyObj.setParameterCode(parameterCode);
        copyObj.setMultiple(multiple);
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
    public TOptionSettingsPeer getPeer()
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
        return TOptionSettingsPeer.getTableMap();
    }

  
    /**
     * Creates a TOptionSettingsBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TOptionSettingsBean with the contents of this object
     */
    public TOptionSettingsBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TOptionSettingsBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TOptionSettingsBean with the contents of this object
     */
    public TOptionSettingsBean getBean(IdentityMap createdBeans)
    {
        TOptionSettingsBean result = (TOptionSettingsBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TOptionSettingsBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setList(getList());
        result.setConfig(getConfig());
        result.setParameterCode(getParameterCode());
        result.setMultiple(getMultiple());
        result.setUuid(getUuid());





        if (aTList != null)
        {
            TListBean relatedBean = aTList.getBean(createdBeans);
            result.setTListBean(relatedBean);
        }



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
     * Creates an instance of TOptionSettings with the contents
     * of a TOptionSettingsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TOptionSettingsBean which contents are used to create
     *        the resulting class
     * @return an instance of TOptionSettings with the contents of bean
     */
    public static TOptionSettings createTOptionSettings(TOptionSettingsBean bean)
        throws TorqueException
    {
        return createTOptionSettings(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TOptionSettings with the contents
     * of a TOptionSettingsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TOptionSettingsBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TOptionSettings with the contents of bean
     */

    public static TOptionSettings createTOptionSettings(TOptionSettingsBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TOptionSettings result = (TOptionSettings) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TOptionSettings();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setList(bean.getList());
        result.setConfig(bean.getConfig());
        result.setParameterCode(bean.getParameterCode());
        result.setMultiple(bean.getMultiple());
        result.setUuid(bean.getUuid());





        {
            TListBean relatedBean = bean.getTListBean();
            if (relatedBean != null)
            {
                TList relatedObject = TList.createTList(relatedBean, createdObjects);
                result.setTList(relatedObject);
            }
        }



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
        str.append("TOptionSettings:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("List = ")
           .append(getList())
           .append("\n");
        str.append("Config = ")
           .append(getConfig())
           .append("\n");
        str.append("ParameterCode = ")
           .append(getParameterCode())
           .append("\n");
        str.append("Multiple = ")
           .append(getMultiple())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
