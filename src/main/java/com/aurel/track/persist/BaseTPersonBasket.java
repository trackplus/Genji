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



import com.aurel.track.persist.TBasket;
import com.aurel.track.persist.TBasketPeer;
import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TPersonBasketBean;
import com.aurel.track.beans.TBasketBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TPersonBean;



/**
 * WorkItem baskets for persons
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TPersonBasket
 */
public abstract class BaseTPersonBasket extends TpBaseObject
{
    /** The Peer class */
    private static final TPersonBasketPeer peer =
        new TPersonBasketPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the basket field */
    private Integer basket;

    /** The value for the workItem field */
    private Integer workItem;

    /** The value for the person field */
    private Integer person;

    /** The value for the reminderDate field */
    private Date reminderDate;

    /** The value for the delegateText field */
    private String delegateText;

    /** The value for the moreProps field */
    private String moreProps;

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
     * Get the Basket
     *
     * @return Integer
     */
    public Integer getBasket()
    {
        return basket;
    }


    /**
     * Set the value of Basket
     *
     * @param v new value
     */
    public void setBasket(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.basket, v))
        {
            this.basket = v;
            setModified(true);
        }


        if (aTBasket != null && !ObjectUtils.equals(aTBasket.getObjectID(), v))
        {
            aTBasket = null;
        }

    }

    /**
     * Get the WorkItem
     *
     * @return Integer
     */
    public Integer getWorkItem()
    {
        return workItem;
    }


    /**
     * Set the value of WorkItem
     *
     * @param v new value
     */
    public void setWorkItem(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workItem, v))
        {
            this.workItem = v;
            setModified(true);
        }


        if (aTWorkItem != null && !ObjectUtils.equals(aTWorkItem.getObjectID(), v))
        {
            aTWorkItem = null;
        }

    }

    /**
     * Get the Person
     *
     * @return Integer
     */
    public Integer getPerson()
    {
        return person;
    }


    /**
     * Set the value of Person
     *
     * @param v new value
     */
    public void setPerson(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.person, v))
        {
            this.person = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
        }

    }

    /**
     * Get the ReminderDate
     *
     * @return Date
     */
    public Date getReminderDate()
    {
        return reminderDate;
    }


    /**
     * Set the value of ReminderDate
     *
     * @param v new value
     */
    public void setReminderDate(Date v) 
    {

        if (!ObjectUtils.equals(this.reminderDate, v))
        {
            this.reminderDate = v;
            setModified(true);
        }


    }

    /**
     * Get the DelegateText
     *
     * @return String
     */
    public String getDelegateText()
    {
        return delegateText;
    }


    /**
     * Set the value of DelegateText
     *
     * @param v new value
     */
    public void setDelegateText(String v) 
    {

        if (!ObjectUtils.equals(this.delegateText, v))
        {
            this.delegateText = v;
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

    



    private TBasket aTBasket;

    /**
     * Declares an association between this object and a TBasket object
     *
     * @param v TBasket
     * @throws TorqueException
     */
    public void setTBasket(TBasket v) throws TorqueException
    {
        if (v == null)
        {
            setBasket((Integer) null);
        }
        else
        {
            setBasket(v.getObjectID());
        }
        aTBasket = v;
    }


    /**
     * Returns the associated TBasket object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TBasket object
     * @throws TorqueException
     */
    public TBasket getTBasket()
        throws TorqueException
    {
        if (aTBasket == null && (!ObjectUtils.equals(this.basket, null)))
        {
            aTBasket = TBasketPeer.retrieveByPK(SimpleKey.keyFor(this.basket));
        }
        return aTBasket;
    }

    /**
     * Return the associated TBasket object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TBasket object
     * @throws TorqueException
     */
    public TBasket getTBasket(Connection connection)
        throws TorqueException
    {
        if (aTBasket == null && (!ObjectUtils.equals(this.basket, null)))
        {
            aTBasket = TBasketPeer.retrieveByPK(SimpleKey.keyFor(this.basket), connection);
        }
        return aTBasket;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTBasketKey(ObjectKey key) throws TorqueException
    {

        setBasket(new Integer(((NumberKey) key).intValue()));
    }




    private TWorkItem aTWorkItem;

    /**
     * Declares an association between this object and a TWorkItem object
     *
     * @param v TWorkItem
     * @throws TorqueException
     */
    public void setTWorkItem(TWorkItem v) throws TorqueException
    {
        if (v == null)
        {
            setWorkItem((Integer) null);
        }
        else
        {
            setWorkItem(v.getObjectID());
        }
        aTWorkItem = v;
    }


    /**
     * Returns the associated TWorkItem object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItem()
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItem));
        }
        return aTWorkItem;
    }

    /**
     * Return the associated TWorkItem object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItem(Connection connection)
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItem), connection);
        }
        return aTWorkItem;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkItemKey(ObjectKey key) throws TorqueException
    {

        setWorkItem(new Integer(((NumberKey) key).intValue()));
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
            setPerson((Integer) null);
        }
        else
        {
            setPerson(v.getObjectID());
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
        if (aTPerson == null && (!ObjectUtils.equals(this.person, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.person));
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
        if (aTPerson == null && (!ObjectUtils.equals(this.person, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.person), connection);
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

        setPerson(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Basket");
            fieldNames.add("WorkItem");
            fieldNames.add("Person");
            fieldNames.add("ReminderDate");
            fieldNames.add("DelegateText");
            fieldNames.add("MoreProps");
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
        if (name.equals("Basket"))
        {
            return getBasket();
        }
        if (name.equals("WorkItem"))
        {
            return getWorkItem();
        }
        if (name.equals("Person"))
        {
            return getPerson();
        }
        if (name.equals("ReminderDate"))
        {
            return getReminderDate();
        }
        if (name.equals("DelegateText"))
        {
            return getDelegateText();
        }
        if (name.equals("MoreProps"))
        {
            return getMoreProps();
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
        if (name.equals("Basket"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setBasket((Integer) value);
            return true;
        }
        if (name.equals("WorkItem"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkItem((Integer) value);
            return true;
        }
        if (name.equals("Person"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPerson((Integer) value);
            return true;
        }
        if (name.equals("ReminderDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReminderDate((Date) value);
            return true;
        }
        if (name.equals("DelegateText"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDelegateText((String) value);
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
        if (name.equals(TPersonBasketPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TPersonBasketPeer.BASKET))
        {
            return getBasket();
        }
        if (name.equals(TPersonBasketPeer.WORKITEM))
        {
            return getWorkItem();
        }
        if (name.equals(TPersonBasketPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TPersonBasketPeer.REMINDERDATE))
        {
            return getReminderDate();
        }
        if (name.equals(TPersonBasketPeer.DELEGATETEXT))
        {
            return getDelegateText();
        }
        if (name.equals(TPersonBasketPeer.MOREPROPS))
        {
            return getMoreProps();
        }
        if (name.equals(TPersonBasketPeer.TPUUID))
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
      if (TPersonBasketPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TPersonBasketPeer.BASKET.equals(name))
        {
            return setByName("Basket", value);
        }
      if (TPersonBasketPeer.WORKITEM.equals(name))
        {
            return setByName("WorkItem", value);
        }
      if (TPersonBasketPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TPersonBasketPeer.REMINDERDATE.equals(name))
        {
            return setByName("ReminderDate", value);
        }
      if (TPersonBasketPeer.DELEGATETEXT.equals(name))
        {
            return setByName("DelegateText", value);
        }
      if (TPersonBasketPeer.MOREPROPS.equals(name))
        {
            return setByName("MoreProps", value);
        }
      if (TPersonBasketPeer.TPUUID.equals(name))
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
            return getBasket();
        }
        if (pos == 2)
        {
            return getWorkItem();
        }
        if (pos == 3)
        {
            return getPerson();
        }
        if (pos == 4)
        {
            return getReminderDate();
        }
        if (pos == 5)
        {
            return getDelegateText();
        }
        if (pos == 6)
        {
            return getMoreProps();
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
            return setByName("Basket", value);
        }
    if (position == 2)
        {
            return setByName("WorkItem", value);
        }
    if (position == 3)
        {
            return setByName("Person", value);
        }
    if (position == 4)
        {
            return setByName("ReminderDate", value);
        }
    if (position == 5)
        {
            return setByName("DelegateText", value);
        }
    if (position == 6)
        {
            return setByName("MoreProps", value);
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
        save(TPersonBasketPeer.DATABASE_NAME);
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
                    TPersonBasketPeer.doInsert((TPersonBasket) this, con);
                    setNew(false);
                }
                else
                {
                    TPersonBasketPeer.doUpdate((TPersonBasket) this, con);
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
    public TPersonBasket copy() throws TorqueException
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
    public TPersonBasket copy(Connection con) throws TorqueException
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
    public TPersonBasket copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TPersonBasket(), deepcopy);
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
    public TPersonBasket copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TPersonBasket(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TPersonBasket copyInto(TPersonBasket copyObj) throws TorqueException
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
    protected TPersonBasket copyInto(TPersonBasket copyObj, Connection con) throws TorqueException
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
    protected TPersonBasket copyInto(TPersonBasket copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setBasket(basket);
        copyObj.setWorkItem(workItem);
        copyObj.setPerson(person);
        copyObj.setReminderDate(reminderDate);
        copyObj.setDelegateText(delegateText);
        copyObj.setMoreProps(moreProps);
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
    protected TPersonBasket copyInto(TPersonBasket copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setBasket(basket);
        copyObj.setWorkItem(workItem);
        copyObj.setPerson(person);
        copyObj.setReminderDate(reminderDate);
        copyObj.setDelegateText(delegateText);
        copyObj.setMoreProps(moreProps);
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
    public TPersonBasketPeer getPeer()
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
        return TPersonBasketPeer.getTableMap();
    }

  
    /**
     * Creates a TPersonBasketBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TPersonBasketBean with the contents of this object
     */
    public TPersonBasketBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TPersonBasketBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TPersonBasketBean with the contents of this object
     */
    public TPersonBasketBean getBean(IdentityMap createdBeans)
    {
        TPersonBasketBean result = (TPersonBasketBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TPersonBasketBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setBasket(getBasket());
        result.setWorkItem(getWorkItem());
        result.setPerson(getPerson());
        result.setReminderDate(getReminderDate());
        result.setDelegateText(getDelegateText());
        result.setMoreProps(getMoreProps());
        result.setUuid(getUuid());





        if (aTBasket != null)
        {
            TBasketBean relatedBean = aTBasket.getBean(createdBeans);
            result.setTBasketBean(relatedBean);
        }



        if (aTWorkItem != null)
        {
            TWorkItemBean relatedBean = aTWorkItem.getBean(createdBeans);
            result.setTWorkItemBean(relatedBean);
        }



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
     * Creates an instance of TPersonBasket with the contents
     * of a TPersonBasketBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TPersonBasketBean which contents are used to create
     *        the resulting class
     * @return an instance of TPersonBasket with the contents of bean
     */
    public static TPersonBasket createTPersonBasket(TPersonBasketBean bean)
        throws TorqueException
    {
        return createTPersonBasket(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TPersonBasket with the contents
     * of a TPersonBasketBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TPersonBasketBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TPersonBasket with the contents of bean
     */

    public static TPersonBasket createTPersonBasket(TPersonBasketBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TPersonBasket result = (TPersonBasket) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TPersonBasket();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setBasket(bean.getBasket());
        result.setWorkItem(bean.getWorkItem());
        result.setPerson(bean.getPerson());
        result.setReminderDate(bean.getReminderDate());
        result.setDelegateText(bean.getDelegateText());
        result.setMoreProps(bean.getMoreProps());
        result.setUuid(bean.getUuid());





        {
            TBasketBean relatedBean = bean.getTBasketBean();
            if (relatedBean != null)
            {
                TBasket relatedObject = TBasket.createTBasket(relatedBean, createdObjects);
                result.setTBasket(relatedObject);
            }
        }



        {
            TWorkItemBean relatedBean = bean.getTWorkItemBean();
            if (relatedBean != null)
            {
                TWorkItem relatedObject = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                result.setTWorkItem(relatedObject);
            }
        }



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
        str.append("TPersonBasket:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Basket = ")
           .append(getBasket())
           .append("\n");
        str.append("WorkItem = ")
           .append(getWorkItem())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("ReminderDate = ")
           .append(getReminderDate())
           .append("\n");
        str.append("DelegateText = ")
           .append(getDelegateText())
           .append("\n");
        str.append("MoreProps = ")
           .append(getMoreProps())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
