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
import com.aurel.track.beans.TEmailProcessedBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TEmailProcessed
 */
public abstract class BaseTEmailProcessed extends TpBaseObject
{
    /** The Peer class */
    private static final TEmailProcessedPeer peer =
        new TEmailProcessedPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the processedDate field */
    private Date processedDate;

    /** The value for the messageUID field */
    private String messageUID;

    /** The value for the receivedAt field */
    private String receivedAt;

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
     * Get the ProcessedDate
     *
     * @return Date
     */
    public Date getProcessedDate()
    {
        return processedDate;
    }


    /**
     * Set the value of ProcessedDate
     *
     * @param v new value
     */
    public void setProcessedDate(Date v) 
    {

        if (!ObjectUtils.equals(this.processedDate, v))
        {
            this.processedDate = v;
            setModified(true);
        }


    }

    /**
     * Get the MessageUID
     *
     * @return String
     */
    public String getMessageUID()
    {
        return messageUID;
    }


    /**
     * Set the value of MessageUID
     *
     * @param v new value
     */
    public void setMessageUID(String v) 
    {

        if (!ObjectUtils.equals(this.messageUID, v))
        {
            this.messageUID = v;
            setModified(true);
        }


    }

    /**
     * Get the ReceivedAt
     *
     * @return String
     */
    public String getReceivedAt()
    {
        return receivedAt;
    }


    /**
     * Set the value of ReceivedAt
     *
     * @param v new value
     */
    public void setReceivedAt(String v) 
    {

        if (!ObjectUtils.equals(this.receivedAt, v))
        {
            this.receivedAt = v;
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
            fieldNames.add("ProcessedDate");
            fieldNames.add("MessageUID");
            fieldNames.add("ReceivedAt");
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
        if (name.equals("ProcessedDate"))
        {
            return getProcessedDate();
        }
        if (name.equals("MessageUID"))
        {
            return getMessageUID();
        }
        if (name.equals("ReceivedAt"))
        {
            return getReceivedAt();
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
        if (name.equals("ProcessedDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProcessedDate((Date) value);
            return true;
        }
        if (name.equals("MessageUID"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMessageUID((String) value);
            return true;
        }
        if (name.equals("ReceivedAt"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReceivedAt((String) value);
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
        if (name.equals(TEmailProcessedPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TEmailProcessedPeer.PROCESSEDDATE))
        {
            return getProcessedDate();
        }
        if (name.equals(TEmailProcessedPeer.MESSAGEUID))
        {
            return getMessageUID();
        }
        if (name.equals(TEmailProcessedPeer.RECEIVEDAT))
        {
            return getReceivedAt();
        }
        if (name.equals(TEmailProcessedPeer.TPUUID))
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
      if (TEmailProcessedPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TEmailProcessedPeer.PROCESSEDDATE.equals(name))
        {
            return setByName("ProcessedDate", value);
        }
      if (TEmailProcessedPeer.MESSAGEUID.equals(name))
        {
            return setByName("MessageUID", value);
        }
      if (TEmailProcessedPeer.RECEIVEDAT.equals(name))
        {
            return setByName("ReceivedAt", value);
        }
      if (TEmailProcessedPeer.TPUUID.equals(name))
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
            return getProcessedDate();
        }
        if (pos == 2)
        {
            return getMessageUID();
        }
        if (pos == 3)
        {
            return getReceivedAt();
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
            return setByName("ProcessedDate", value);
        }
    if (position == 2)
        {
            return setByName("MessageUID", value);
        }
    if (position == 3)
        {
            return setByName("ReceivedAt", value);
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
        save(TEmailProcessedPeer.DATABASE_NAME);
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
                    TEmailProcessedPeer.doInsert((TEmailProcessed) this, con);
                    setNew(false);
                }
                else
                {
                    TEmailProcessedPeer.doUpdate((TEmailProcessed) this, con);
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
    public TEmailProcessed copy() throws TorqueException
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
    public TEmailProcessed copy(Connection con) throws TorqueException
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
    public TEmailProcessed copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TEmailProcessed(), deepcopy);
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
    public TEmailProcessed copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TEmailProcessed(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TEmailProcessed copyInto(TEmailProcessed copyObj) throws TorqueException
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
    protected TEmailProcessed copyInto(TEmailProcessed copyObj, Connection con) throws TorqueException
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
    protected TEmailProcessed copyInto(TEmailProcessed copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setProcessedDate(processedDate);
        copyObj.setMessageUID(messageUID);
        copyObj.setReceivedAt(receivedAt);
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
    protected TEmailProcessed copyInto(TEmailProcessed copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setProcessedDate(processedDate);
        copyObj.setMessageUID(messageUID);
        copyObj.setReceivedAt(receivedAt);
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
    public TEmailProcessedPeer getPeer()
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
        return TEmailProcessedPeer.getTableMap();
    }

  
    /**
     * Creates a TEmailProcessedBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TEmailProcessedBean with the contents of this object
     */
    public TEmailProcessedBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TEmailProcessedBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TEmailProcessedBean with the contents of this object
     */
    public TEmailProcessedBean getBean(IdentityMap createdBeans)
    {
        TEmailProcessedBean result = (TEmailProcessedBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TEmailProcessedBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setProcessedDate(getProcessedDate());
        result.setMessageUID(getMessageUID());
        result.setReceivedAt(getReceivedAt());
        result.setUuid(getUuid());


        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TEmailProcessed with the contents
     * of a TEmailProcessedBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TEmailProcessedBean which contents are used to create
     *        the resulting class
     * @return an instance of TEmailProcessed with the contents of bean
     */
    public static TEmailProcessed createTEmailProcessed(TEmailProcessedBean bean)
        throws TorqueException
    {
        return createTEmailProcessed(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TEmailProcessed with the contents
     * of a TEmailProcessedBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TEmailProcessedBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TEmailProcessed with the contents of bean
     */

    public static TEmailProcessed createTEmailProcessed(TEmailProcessedBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TEmailProcessed result = (TEmailProcessed) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TEmailProcessed();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setProcessedDate(bean.getProcessedDate());
        result.setMessageUID(bean.getMessageUID());
        result.setReceivedAt(bean.getReceivedAt());
        result.setUuid(bean.getUuid());


    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TEmailProcessed:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("ProcessedDate = ")
           .append(getProcessedDate())
           .append("\n");
        str.append("MessageUID = ")
           .append(getMessageUID())
           .append("\n");
        str.append("ReceivedAt = ")
           .append(getReceivedAt())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
