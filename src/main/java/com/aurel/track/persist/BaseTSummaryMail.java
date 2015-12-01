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



import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TSummaryMailBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPersonBean;



/**
 * This table holds the summary mail parts before they are sent
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TSummaryMail
 */
public abstract class BaseTSummaryMail extends TpBaseObject
{
    /** The Peer class */
    private static final TSummaryMailPeer peer =
        new TSummaryMailPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the workItem field */
    private Integer workItem;

    /** The value for the pERSONFROM field */
    private Integer pERSONFROM;

    /** The value for the fromAddress field */
    private String fromAddress;

    /** The value for the mailSubject field */
    private String mailSubject;

    /** The value for the workItemLink field */
    private String workItemLink;

    /** The value for the pERSONTO field */
    private Integer pERSONTO;

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
     * Get the PERSONFROM
     *
     * @return Integer
     */
    public Integer getPERSONFROM()
    {
        return pERSONFROM;
    }


    /**
     * Set the value of PERSONFROM
     *
     * @param v new value
     */
    public void setPERSONFROM(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.pERSONFROM, v))
        {
            this.pERSONFROM = v;
            setModified(true);
        }


        if (aTPersonRelatedByPERSONFROM != null && !ObjectUtils.equals(aTPersonRelatedByPERSONFROM.getObjectID(), v))
        {
            aTPersonRelatedByPERSONFROM = null;
        }

    }

    /**
     * Get the FromAddress
     *
     * @return String
     */
    public String getFromAddress()
    {
        return fromAddress;
    }


    /**
     * Set the value of FromAddress
     *
     * @param v new value
     */
    public void setFromAddress(String v) 
    {

        if (!ObjectUtils.equals(this.fromAddress, v))
        {
            this.fromAddress = v;
            setModified(true);
        }


    }

    /**
     * Get the MailSubject
     *
     * @return String
     */
    public String getMailSubject()
    {
        return mailSubject;
    }


    /**
     * Set the value of MailSubject
     *
     * @param v new value
     */
    public void setMailSubject(String v) 
    {

        if (!ObjectUtils.equals(this.mailSubject, v))
        {
            this.mailSubject = v;
            setModified(true);
        }


    }

    /**
     * Get the WorkItemLink
     *
     * @return String
     */
    public String getWorkItemLink()
    {
        return workItemLink;
    }


    /**
     * Set the value of WorkItemLink
     *
     * @param v new value
     */
    public void setWorkItemLink(String v) 
    {

        if (!ObjectUtils.equals(this.workItemLink, v))
        {
            this.workItemLink = v;
            setModified(true);
        }


    }

    /**
     * Get the PERSONTO
     *
     * @return Integer
     */
    public Integer getPERSONTO()
    {
        return pERSONTO;
    }


    /**
     * Set the value of PERSONTO
     *
     * @param v new value
     */
    public void setPERSONTO(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.pERSONTO, v))
        {
            this.pERSONTO = v;
            setModified(true);
        }


        if (aTPersonRelatedByPERSONTO != null && !ObjectUtils.equals(aTPersonRelatedByPERSONTO.getObjectID(), v))
        {
            aTPersonRelatedByPERSONTO = null;
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




    private TPerson aTPersonRelatedByPERSONFROM;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByPERSONFROM(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setPERSONFROM((Integer) null);
        }
        else
        {
            setPERSONFROM(v.getObjectID());
        }
        aTPersonRelatedByPERSONFROM = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByPERSONFROM()
        throws TorqueException
    {
        if (aTPersonRelatedByPERSONFROM == null && (!ObjectUtils.equals(this.pERSONFROM, null)))
        {
            aTPersonRelatedByPERSONFROM = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.pERSONFROM));
        }
        return aTPersonRelatedByPERSONFROM;
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
    public TPerson getTPersonRelatedByPERSONFROM(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByPERSONFROM == null && (!ObjectUtils.equals(this.pERSONFROM, null)))
        {
            aTPersonRelatedByPERSONFROM = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.pERSONFROM), connection);
        }
        return aTPersonRelatedByPERSONFROM;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByPERSONFROMKey(ObjectKey key) throws TorqueException
    {

        setPERSONFROM(new Integer(((NumberKey) key).intValue()));
    }




    private TPerson aTPersonRelatedByPERSONTO;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByPERSONTO(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setPERSONTO((Integer) null);
        }
        else
        {
            setPERSONTO(v.getObjectID());
        }
        aTPersonRelatedByPERSONTO = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByPERSONTO()
        throws TorqueException
    {
        if (aTPersonRelatedByPERSONTO == null && (!ObjectUtils.equals(this.pERSONTO, null)))
        {
            aTPersonRelatedByPERSONTO = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.pERSONTO));
        }
        return aTPersonRelatedByPERSONTO;
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
    public TPerson getTPersonRelatedByPERSONTO(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByPERSONTO == null && (!ObjectUtils.equals(this.pERSONTO, null)))
        {
            aTPersonRelatedByPERSONTO = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.pERSONTO), connection);
        }
        return aTPersonRelatedByPERSONTO;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByPERSONTOKey(ObjectKey key) throws TorqueException
    {

        setPERSONTO(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("WorkItem");
            fieldNames.add("PERSONFROM");
            fieldNames.add("FromAddress");
            fieldNames.add("MailSubject");
            fieldNames.add("WorkItemLink");
            fieldNames.add("PERSONTO");
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
        if (name.equals("WorkItem"))
        {
            return getWorkItem();
        }
        if (name.equals("PERSONFROM"))
        {
            return getPERSONFROM();
        }
        if (name.equals("FromAddress"))
        {
            return getFromAddress();
        }
        if (name.equals("MailSubject"))
        {
            return getMailSubject();
        }
        if (name.equals("WorkItemLink"))
        {
            return getWorkItemLink();
        }
        if (name.equals("PERSONTO"))
        {
            return getPERSONTO();
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
        if (name.equals("PERSONFROM"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPERSONFROM((Integer) value);
            return true;
        }
        if (name.equals("FromAddress"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFromAddress((String) value);
            return true;
        }
        if (name.equals("MailSubject"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMailSubject((String) value);
            return true;
        }
        if (name.equals("WorkItemLink"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkItemLink((String) value);
            return true;
        }
        if (name.equals("PERSONTO"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPERSONTO((Integer) value);
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
        if (name.equals(TSummaryMailPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TSummaryMailPeer.WORKITEM))
        {
            return getWorkItem();
        }
        if (name.equals(TSummaryMailPeer.PERSONFROM))
        {
            return getPERSONFROM();
        }
        if (name.equals(TSummaryMailPeer.FROMADDRESS))
        {
            return getFromAddress();
        }
        if (name.equals(TSummaryMailPeer.MAILSUBJECT))
        {
            return getMailSubject();
        }
        if (name.equals(TSummaryMailPeer.WORKITEMLINK))
        {
            return getWorkItemLink();
        }
        if (name.equals(TSummaryMailPeer.PERSONTO))
        {
            return getPERSONTO();
        }
        if (name.equals(TSummaryMailPeer.LASTEDIT))
        {
            return getLastEdit();
        }
        if (name.equals(TSummaryMailPeer.TPUUID))
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
      if (TSummaryMailPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TSummaryMailPeer.WORKITEM.equals(name))
        {
            return setByName("WorkItem", value);
        }
      if (TSummaryMailPeer.PERSONFROM.equals(name))
        {
            return setByName("PERSONFROM", value);
        }
      if (TSummaryMailPeer.FROMADDRESS.equals(name))
        {
            return setByName("FromAddress", value);
        }
      if (TSummaryMailPeer.MAILSUBJECT.equals(name))
        {
            return setByName("MailSubject", value);
        }
      if (TSummaryMailPeer.WORKITEMLINK.equals(name))
        {
            return setByName("WorkItemLink", value);
        }
      if (TSummaryMailPeer.PERSONTO.equals(name))
        {
            return setByName("PERSONTO", value);
        }
      if (TSummaryMailPeer.LASTEDIT.equals(name))
        {
            return setByName("LastEdit", value);
        }
      if (TSummaryMailPeer.TPUUID.equals(name))
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
            return getWorkItem();
        }
        if (pos == 2)
        {
            return getPERSONFROM();
        }
        if (pos == 3)
        {
            return getFromAddress();
        }
        if (pos == 4)
        {
            return getMailSubject();
        }
        if (pos == 5)
        {
            return getWorkItemLink();
        }
        if (pos == 6)
        {
            return getPERSONTO();
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
            return setByName("WorkItem", value);
        }
    if (position == 2)
        {
            return setByName("PERSONFROM", value);
        }
    if (position == 3)
        {
            return setByName("FromAddress", value);
        }
    if (position == 4)
        {
            return setByName("MailSubject", value);
        }
    if (position == 5)
        {
            return setByName("WorkItemLink", value);
        }
    if (position == 6)
        {
            return setByName("PERSONTO", value);
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
        save(TSummaryMailPeer.DATABASE_NAME);
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
                    TSummaryMailPeer.doInsert((TSummaryMail) this, con);
                    setNew(false);
                }
                else
                {
                    TSummaryMailPeer.doUpdate((TSummaryMail) this, con);
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
    public TSummaryMail copy() throws TorqueException
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
    public TSummaryMail copy(Connection con) throws TorqueException
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
    public TSummaryMail copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TSummaryMail(), deepcopy);
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
    public TSummaryMail copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TSummaryMail(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TSummaryMail copyInto(TSummaryMail copyObj) throws TorqueException
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
    protected TSummaryMail copyInto(TSummaryMail copyObj, Connection con) throws TorqueException
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
    protected TSummaryMail copyInto(TSummaryMail copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkItem(workItem);
        copyObj.setPERSONFROM(pERSONFROM);
        copyObj.setFromAddress(fromAddress);
        copyObj.setMailSubject(mailSubject);
        copyObj.setWorkItemLink(workItemLink);
        copyObj.setPERSONTO(pERSONTO);
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
    protected TSummaryMail copyInto(TSummaryMail copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkItem(workItem);
        copyObj.setPERSONFROM(pERSONFROM);
        copyObj.setFromAddress(fromAddress);
        copyObj.setMailSubject(mailSubject);
        copyObj.setWorkItemLink(workItemLink);
        copyObj.setPERSONTO(pERSONTO);
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
    public TSummaryMailPeer getPeer()
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
        return TSummaryMailPeer.getTableMap();
    }

  
    /**
     * Creates a TSummaryMailBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TSummaryMailBean with the contents of this object
     */
    public TSummaryMailBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TSummaryMailBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TSummaryMailBean with the contents of this object
     */
    public TSummaryMailBean getBean(IdentityMap createdBeans)
    {
        TSummaryMailBean result = (TSummaryMailBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TSummaryMailBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setWorkItem(getWorkItem());
        result.setPERSONFROM(getPERSONFROM());
        result.setFromAddress(getFromAddress());
        result.setMailSubject(getMailSubject());
        result.setWorkItemLink(getWorkItemLink());
        result.setPERSONTO(getPERSONTO());
        result.setLastEdit(getLastEdit());
        result.setUuid(getUuid());





        if (aTWorkItem != null)
        {
            TWorkItemBean relatedBean = aTWorkItem.getBean(createdBeans);
            result.setTWorkItemBean(relatedBean);
        }



        if (aTPersonRelatedByPERSONFROM != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByPERSONFROM.getBean(createdBeans);
            result.setTPersonBeanRelatedByPERSONFROM(relatedBean);
        }



        if (aTPersonRelatedByPERSONTO != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByPERSONTO.getBean(createdBeans);
            result.setTPersonBeanRelatedByPERSONTO(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TSummaryMail with the contents
     * of a TSummaryMailBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TSummaryMailBean which contents are used to create
     *        the resulting class
     * @return an instance of TSummaryMail with the contents of bean
     */
    public static TSummaryMail createTSummaryMail(TSummaryMailBean bean)
        throws TorqueException
    {
        return createTSummaryMail(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TSummaryMail with the contents
     * of a TSummaryMailBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TSummaryMailBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TSummaryMail with the contents of bean
     */

    public static TSummaryMail createTSummaryMail(TSummaryMailBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TSummaryMail result = (TSummaryMail) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TSummaryMail();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setWorkItem(bean.getWorkItem());
        result.setPERSONFROM(bean.getPERSONFROM());
        result.setFromAddress(bean.getFromAddress());
        result.setMailSubject(bean.getMailSubject());
        result.setWorkItemLink(bean.getWorkItemLink());
        result.setPERSONTO(bean.getPERSONTO());
        result.setLastEdit(bean.getLastEdit());
        result.setUuid(bean.getUuid());





        {
            TWorkItemBean relatedBean = bean.getTWorkItemBean();
            if (relatedBean != null)
            {
                TWorkItem relatedObject = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                result.setTWorkItem(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByPERSONFROM();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByPERSONFROM(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByPERSONTO();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByPERSONTO(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TSummaryMail:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("WorkItem = ")
           .append(getWorkItem())
           .append("\n");
        str.append("PERSONFROM = ")
           .append(getPERSONFROM())
           .append("\n");
        str.append("FromAddress = ")
           .append(getFromAddress())
           .append("\n");
        str.append("MailSubject = ")
           .append(getMailSubject())
           .append("\n");
        str.append("WorkItemLink = ")
           .append(getWorkItemLink())
           .append("\n");
        str.append("PERSONTO = ")
           .append(getPERSONTO())
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
