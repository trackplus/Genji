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



import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TMSProjectExchangeBean;
import com.aurel.track.beans.TPersonBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TMSProjectExchange
 */
public abstract class BaseTMSProjectExchange extends TpBaseObject
{
    /** The Peer class */
    private static final TMSProjectExchangePeer peer =
        new TMSProjectExchangePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the exchangeDirection field */
    private Integer exchangeDirection;

    /** The value for the entityID field */
    private Integer entityID;

    /** The value for the entityType field */
    private Integer entityType;

    /** The value for the fileName field */
    private String fileName;

    /** The value for the fileContent field */
    private String fileContent;

    /** The value for the changedBy field */
    private Integer changedBy;

    /** The value for the lastEdit field */
    private Date lastEdit;

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
     * Get the ExchangeDirection
     *
     * @return Integer
     */
    public Integer getExchangeDirection()
    {
        return exchangeDirection;
    }


    /**
     * Set the value of ExchangeDirection
     *
     * @param v new value
     */
    public void setExchangeDirection(Integer v) 
    {

        if (!ObjectUtils.equals(this.exchangeDirection, v))
        {
            this.exchangeDirection = v;
            setModified(true);
        }


    }

    /**
     * Get the EntityID
     *
     * @return Integer
     */
    public Integer getEntityID()
    {
        return entityID;
    }


    /**
     * Set the value of EntityID
     *
     * @param v new value
     */
    public void setEntityID(Integer v) 
    {

        if (!ObjectUtils.equals(this.entityID, v))
        {
            this.entityID = v;
            setModified(true);
        }


    }

    /**
     * Get the EntityType
     *
     * @return Integer
     */
    public Integer getEntityType()
    {
        return entityType;
    }


    /**
     * Set the value of EntityType
     *
     * @param v new value
     */
    public void setEntityType(Integer v) 
    {

        if (!ObjectUtils.equals(this.entityType, v))
        {
            this.entityType = v;
            setModified(true);
        }


    }

    /**
     * Get the FileName
     *
     * @return String
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * Set the value of FileName
     *
     * @param v new value
     */
    public void setFileName(String v) 
    {

        if (!ObjectUtils.equals(this.fileName, v))
        {
            this.fileName = v;
            setModified(true);
        }


    }

    /**
     * Get the FileContent
     *
     * @return String
     */
    public String getFileContent()
    {
        return fileContent;
    }


    /**
     * Set the value of FileContent
     *
     * @param v new value
     */
    public void setFileContent(String v) 
    {

        if (!ObjectUtils.equals(this.fileContent, v))
        {
            this.fileContent = v;
            setModified(true);
        }


    }

    /**
     * Get the ChangedBy
     *
     * @return Integer
     */
    public Integer getChangedBy()
    {
        return changedBy;
    }


    /**
     * Set the value of ChangedBy
     *
     * @param v new value
     */
    public void setChangedBy(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.changedBy, v))
        {
            this.changedBy = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
        }

    }

    /**
     * Get the LastEdit
     *
     * @return Date
     */
    public Date getLastEdit()
    {
        return lastEdit;
    }


    /**
     * Set the value of LastEdit
     *
     * @param v new value
     */
    public void setLastEdit(Date v) 
    {

        if (!ObjectUtils.equals(this.lastEdit, v))
        {
            this.lastEdit = v;
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
            setChangedBy((Integer) null);
        }
        else
        {
            setChangedBy(v.getObjectID());
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
        if (aTPerson == null && (!ObjectUtils.equals(this.changedBy, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.changedBy));
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
        if (aTPerson == null && (!ObjectUtils.equals(this.changedBy, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.changedBy), connection);
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

        setChangedBy(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("ExchangeDirection");
            fieldNames.add("EntityID");
            fieldNames.add("EntityType");
            fieldNames.add("FileName");
            fieldNames.add("FileContent");
            fieldNames.add("ChangedBy");
            fieldNames.add("LastEdit");
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
        if (name.equals("ExchangeDirection"))
        {
            return getExchangeDirection();
        }
        if (name.equals("EntityID"))
        {
            return getEntityID();
        }
        if (name.equals("EntityType"))
        {
            return getEntityType();
        }
        if (name.equals("FileName"))
        {
            return getFileName();
        }
        if (name.equals("FileContent"))
        {
            return getFileContent();
        }
        if (name.equals("ChangedBy"))
        {
            return getChangedBy();
        }
        if (name.equals("LastEdit"))
        {
            return getLastEdit();
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
        if (name.equals("ExchangeDirection"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setExchangeDirection((Integer) value);
            return true;
        }
        if (name.equals("EntityID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEntityID((Integer) value);
            return true;
        }
        if (name.equals("EntityType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEntityType((Integer) value);
            return true;
        }
        if (name.equals("FileName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFileName((String) value);
            return true;
        }
        if (name.equals("FileContent"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFileContent((String) value);
            return true;
        }
        if (name.equals("ChangedBy"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setChangedBy((Integer) value);
            return true;
        }
        if (name.equals("LastEdit"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLastEdit((Date) value);
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
        if (name.equals(TMSProjectExchangePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TMSProjectExchangePeer.EXCHANGEDIRECTION))
        {
            return getExchangeDirection();
        }
        if (name.equals(TMSProjectExchangePeer.ENTITYID))
        {
            return getEntityID();
        }
        if (name.equals(TMSProjectExchangePeer.ENTITYTYPE))
        {
            return getEntityType();
        }
        if (name.equals(TMSProjectExchangePeer.FILENAME))
        {
            return getFileName();
        }
        if (name.equals(TMSProjectExchangePeer.FILECONTENT))
        {
            return getFileContent();
        }
        if (name.equals(TMSProjectExchangePeer.CHANGEDBY))
        {
            return getChangedBy();
        }
        if (name.equals(TMSProjectExchangePeer.LASTEDIT))
        {
            return getLastEdit();
        }
        if (name.equals(TMSProjectExchangePeer.TPUUID))
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
      if (TMSProjectExchangePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TMSProjectExchangePeer.EXCHANGEDIRECTION.equals(name))
        {
            return setByName("ExchangeDirection", value);
        }
      if (TMSProjectExchangePeer.ENTITYID.equals(name))
        {
            return setByName("EntityID", value);
        }
      if (TMSProjectExchangePeer.ENTITYTYPE.equals(name))
        {
            return setByName("EntityType", value);
        }
      if (TMSProjectExchangePeer.FILENAME.equals(name))
        {
            return setByName("FileName", value);
        }
      if (TMSProjectExchangePeer.FILECONTENT.equals(name))
        {
            return setByName("FileContent", value);
        }
      if (TMSProjectExchangePeer.CHANGEDBY.equals(name))
        {
            return setByName("ChangedBy", value);
        }
      if (TMSProjectExchangePeer.LASTEDIT.equals(name))
        {
            return setByName("LastEdit", value);
        }
      if (TMSProjectExchangePeer.TPUUID.equals(name))
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
            return getExchangeDirection();
        }
        if (pos == 2)
        {
            return getEntityID();
        }
        if (pos == 3)
        {
            return getEntityType();
        }
        if (pos == 4)
        {
            return getFileName();
        }
        if (pos == 5)
        {
            return getFileContent();
        }
        if (pos == 6)
        {
            return getChangedBy();
        }
        if (pos == 7)
        {
            return getLastEdit();
        }
        if (pos == 8)
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
            return setByName("ExchangeDirection", value);
        }
    if (position == 2)
        {
            return setByName("EntityID", value);
        }
    if (position == 3)
        {
            return setByName("EntityType", value);
        }
    if (position == 4)
        {
            return setByName("FileName", value);
        }
    if (position == 5)
        {
            return setByName("FileContent", value);
        }
    if (position == 6)
        {
            return setByName("ChangedBy", value);
        }
    if (position == 7)
        {
            return setByName("LastEdit", value);
        }
    if (position == 8)
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
        save(TMSProjectExchangePeer.DATABASE_NAME);
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
                    TMSProjectExchangePeer.doInsert((TMSProjectExchange) this, con);
                    setNew(false);
                }
                else
                {
                    TMSProjectExchangePeer.doUpdate((TMSProjectExchange) this, con);
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
    public TMSProjectExchange copy() throws TorqueException
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
    public TMSProjectExchange copy(Connection con) throws TorqueException
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
    public TMSProjectExchange copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TMSProjectExchange(), deepcopy);
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
    public TMSProjectExchange copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TMSProjectExchange(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TMSProjectExchange copyInto(TMSProjectExchange copyObj) throws TorqueException
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
    protected TMSProjectExchange copyInto(TMSProjectExchange copyObj, Connection con) throws TorqueException
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
    protected TMSProjectExchange copyInto(TMSProjectExchange copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setExchangeDirection(exchangeDirection);
        copyObj.setEntityID(entityID);
        copyObj.setEntityType(entityType);
        copyObj.setFileName(fileName);
        copyObj.setFileContent(fileContent);
        copyObj.setChangedBy(changedBy);
        copyObj.setLastEdit(lastEdit);
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
    protected TMSProjectExchange copyInto(TMSProjectExchange copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setExchangeDirection(exchangeDirection);
        copyObj.setEntityID(entityID);
        copyObj.setEntityType(entityType);
        copyObj.setFileName(fileName);
        copyObj.setFileContent(fileContent);
        copyObj.setChangedBy(changedBy);
        copyObj.setLastEdit(lastEdit);
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
    public TMSProjectExchangePeer getPeer()
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
        return TMSProjectExchangePeer.getTableMap();
    }

  
    /**
     * Creates a TMSProjectExchangeBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TMSProjectExchangeBean with the contents of this object
     */
    public TMSProjectExchangeBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TMSProjectExchangeBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TMSProjectExchangeBean with the contents of this object
     */
    public TMSProjectExchangeBean getBean(IdentityMap createdBeans)
    {
        TMSProjectExchangeBean result = (TMSProjectExchangeBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TMSProjectExchangeBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setExchangeDirection(getExchangeDirection());
        result.setEntityID(getEntityID());
        result.setEntityType(getEntityType());
        result.setFileName(getFileName());
        result.setFileContent(getFileContent());
        result.setChangedBy(getChangedBy());
        result.setLastEdit(getLastEdit());
        result.setUuid(getUuid());





        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TMSProjectExchange with the contents
     * of a TMSProjectExchangeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TMSProjectExchangeBean which contents are used to create
     *        the resulting class
     * @return an instance of TMSProjectExchange with the contents of bean
     */
    public static TMSProjectExchange createTMSProjectExchange(TMSProjectExchangeBean bean)
        throws TorqueException
    {
        return createTMSProjectExchange(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TMSProjectExchange with the contents
     * of a TMSProjectExchangeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TMSProjectExchangeBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TMSProjectExchange with the contents of bean
     */

    public static TMSProjectExchange createTMSProjectExchange(TMSProjectExchangeBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TMSProjectExchange result = (TMSProjectExchange) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TMSProjectExchange();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setExchangeDirection(bean.getExchangeDirection());
        result.setEntityID(bean.getEntityID());
        result.setEntityType(bean.getEntityType());
        result.setFileName(bean.getFileName());
        result.setFileContent(bean.getFileContent());
        result.setChangedBy(bean.getChangedBy());
        result.setLastEdit(bean.getLastEdit());
        result.setUuid(bean.getUuid());





        {
            TPersonBean relatedBean = bean.getTPersonBean();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPerson(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TMSProjectExchange:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("ExchangeDirection = ")
           .append(getExchangeDirection())
           .append("\n");
        str.append("EntityID = ")
           .append(getEntityID())
           .append("\n");
        str.append("EntityType = ")
           .append(getEntityType())
           .append("\n");
        str.append("FileName = ")
           .append(getFileName())
           .append("\n");
        str.append("FileContent = ")
           .append(getFileContent())
           .append("\n");
        str.append("ChangedBy = ")
           .append(getChangedBy())
           .append("\n");
        str.append("LastEdit = ")
           .append(getLastEdit())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
