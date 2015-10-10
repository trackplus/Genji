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



import com.aurel.track.persist.TAccount;
import com.aurel.track.persist.TAccountPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;
import com.aurel.track.persist.TEffortType;
import com.aurel.track.persist.TEffortTypePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TAccountBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TEffortTypeBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TCost
 */
public abstract class BaseTCost extends TpBaseObject
{
    /** The Peer class */
    private static final TCostPeer peer =
        new TCostPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the account field */
    private Integer account;

    /** The value for the person field */
    private Integer person;

    /** The value for the workitem field */
    private Integer workitem;

    /** The value for the hours field */
    private Double hours;

    /** The value for the cost field */
    private Double cost;

    /** The value for the subject field */
    private String subject;

    /** The value for the efforttype field */
    private Integer efforttype;

    /** The value for the effortvalue field */
    private Double effortvalue;

    /** The value for the effortdate field */
    private Date effortdate;

    /** The value for the invoicenumber field */
    private String invoicenumber;

    /** The value for the invoicedate field */
    private Date invoicedate;

    /** The value for the invoicepath field */
    private String invoicepath;

    /** The value for the description field */
    private String description;

    /** The value for the moreProps field */
    private String moreProps;

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
     * Get the Account
     *
     * @return Integer
     */
    public Integer getAccount()
    {
        return account;
    }


    /**
     * Set the value of Account
     *
     * @param v new value
     */
    public void setAccount(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.account, v))
        {
            this.account = v;
            setModified(true);
        }


        if (aTAccount != null && !ObjectUtils.equals(aTAccount.getObjectID(), v))
        {
            aTAccount = null;
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
     * Get the Workitem
     *
     * @return Integer
     */
    public Integer getWorkitem()
    {
        return workitem;
    }


    /**
     * Set the value of Workitem
     *
     * @param v new value
     */
    public void setWorkitem(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workitem, v))
        {
            this.workitem = v;
            setModified(true);
        }


        if (aTWorkItem != null && !ObjectUtils.equals(aTWorkItem.getObjectID(), v))
        {
            aTWorkItem = null;
        }

    }

    /**
     * Get the Hours
     *
     * @return Double
     */
    public Double getHours()
    {
        return hours;
    }


    /**
     * Set the value of Hours
     *
     * @param v new value
     */
    public void setHours(Double v) 
    {

        if (!ObjectUtils.equals(this.hours, v))
        {
            this.hours = v;
            setModified(true);
        }


    }

    /**
     * Get the Cost
     *
     * @return Double
     */
    public Double getCost()
    {
        return cost;
    }


    /**
     * Set the value of Cost
     *
     * @param v new value
     */
    public void setCost(Double v) 
    {

        if (!ObjectUtils.equals(this.cost, v))
        {
            this.cost = v;
            setModified(true);
        }


    }

    /**
     * Get the Subject
     *
     * @return String
     */
    public String getSubject()
    {
        return subject;
    }


    /**
     * Set the value of Subject
     *
     * @param v new value
     */
    public void setSubject(String v) 
    {

        if (!ObjectUtils.equals(this.subject, v))
        {
            this.subject = v;
            setModified(true);
        }


    }

    /**
     * Get the Efforttype
     *
     * @return Integer
     */
    public Integer getEfforttype()
    {
        return efforttype;
    }


    /**
     * Set the value of Efforttype
     *
     * @param v new value
     */
    public void setEfforttype(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.efforttype, v))
        {
            this.efforttype = v;
            setModified(true);
        }


        if (aTEffortType != null && !ObjectUtils.equals(aTEffortType.getObjectID(), v))
        {
            aTEffortType = null;
        }

    }

    /**
     * Get the Effortvalue
     *
     * @return Double
     */
    public Double getEffortvalue()
    {
        return effortvalue;
    }


    /**
     * Set the value of Effortvalue
     *
     * @param v new value
     */
    public void setEffortvalue(Double v) 
    {

        if (!ObjectUtils.equals(this.effortvalue, v))
        {
            this.effortvalue = v;
            setModified(true);
        }


    }

    /**
     * Get the Effortdate
     *
     * @return Date
     */
    public Date getEffortdate()
    {
        return effortdate;
    }


    /**
     * Set the value of Effortdate
     *
     * @param v new value
     */
    public void setEffortdate(Date v) 
    {

        if (!ObjectUtils.equals(this.effortdate, v))
        {
            this.effortdate = v;
            setModified(true);
        }


    }

    /**
     * Get the Invoicenumber
     *
     * @return String
     */
    public String getInvoicenumber()
    {
        return invoicenumber;
    }


    /**
     * Set the value of Invoicenumber
     *
     * @param v new value
     */
    public void setInvoicenumber(String v) 
    {

        if (!ObjectUtils.equals(this.invoicenumber, v))
        {
            this.invoicenumber = v;
            setModified(true);
        }


    }

    /**
     * Get the Invoicedate
     *
     * @return Date
     */
    public Date getInvoicedate()
    {
        return invoicedate;
    }


    /**
     * Set the value of Invoicedate
     *
     * @param v new value
     */
    public void setInvoicedate(Date v) 
    {

        if (!ObjectUtils.equals(this.invoicedate, v))
        {
            this.invoicedate = v;
            setModified(true);
        }


    }

    /**
     * Get the Invoicepath
     *
     * @return String
     */
    public String getInvoicepath()
    {
        return invoicepath;
    }


    /**
     * Set the value of Invoicepath
     *
     * @param v new value
     */
    public void setInvoicepath(String v) 
    {

        if (!ObjectUtils.equals(this.invoicepath, v))
        {
            this.invoicepath = v;
            setModified(true);
        }


    }

    /**
     * Get the Description
     *
     * @return String
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set the value of Description
     *
     * @param v new value
     */
    public void setDescription(String v) 
    {

        if (!ObjectUtils.equals(this.description, v))
        {
            this.description = v;
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

    



    private TAccount aTAccount;

    /**
     * Declares an association between this object and a TAccount object
     *
     * @param v TAccount
     * @throws TorqueException
     */
    public void setTAccount(TAccount v) throws TorqueException
    {
        if (v == null)
        {
            setAccount((Integer) null);
        }
        else
        {
            setAccount(v.getObjectID());
        }
        aTAccount = v;
    }


    /**
     * Returns the associated TAccount object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TAccount object
     * @throws TorqueException
     */
    public TAccount getTAccount()
        throws TorqueException
    {
        if (aTAccount == null && (!ObjectUtils.equals(this.account, null)))
        {
            aTAccount = TAccountPeer.retrieveByPK(SimpleKey.keyFor(this.account));
        }
        return aTAccount;
    }

    /**
     * Return the associated TAccount object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TAccount object
     * @throws TorqueException
     */
    public TAccount getTAccount(Connection connection)
        throws TorqueException
    {
        if (aTAccount == null && (!ObjectUtils.equals(this.account, null)))
        {
            aTAccount = TAccountPeer.retrieveByPK(SimpleKey.keyFor(this.account), connection);
        }
        return aTAccount;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTAccountKey(ObjectKey key) throws TorqueException
    {

        setAccount(new Integer(((NumberKey) key).intValue()));
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
            setWorkitem((Integer) null);
        }
        else
        {
            setWorkitem(v.getObjectID());
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
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workitem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workitem));
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
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workitem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workitem), connection);
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

        setWorkitem(new Integer(((NumberKey) key).intValue()));
    }




    private TEffortType aTEffortType;

    /**
     * Declares an association between this object and a TEffortType object
     *
     * @param v TEffortType
     * @throws TorqueException
     */
    public void setTEffortType(TEffortType v) throws TorqueException
    {
        if (v == null)
        {
            setEfforttype((Integer) null);
        }
        else
        {
            setEfforttype(v.getObjectID());
        }
        aTEffortType = v;
    }


    /**
     * Returns the associated TEffortType object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TEffortType object
     * @throws TorqueException
     */
    public TEffortType getTEffortType()
        throws TorqueException
    {
        if (aTEffortType == null && (!ObjectUtils.equals(this.efforttype, null)))
        {
            aTEffortType = TEffortTypePeer.retrieveByPK(SimpleKey.keyFor(this.efforttype));
        }
        return aTEffortType;
    }

    /**
     * Return the associated TEffortType object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TEffortType object
     * @throws TorqueException
     */
    public TEffortType getTEffortType(Connection connection)
        throws TorqueException
    {
        if (aTEffortType == null && (!ObjectUtils.equals(this.efforttype, null)))
        {
            aTEffortType = TEffortTypePeer.retrieveByPK(SimpleKey.keyFor(this.efforttype), connection);
        }
        return aTEffortType;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTEffortTypeKey(ObjectKey key) throws TorqueException
    {

        setEfforttype(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Account");
            fieldNames.add("Person");
            fieldNames.add("Workitem");
            fieldNames.add("Hours");
            fieldNames.add("Cost");
            fieldNames.add("Subject");
            fieldNames.add("Efforttype");
            fieldNames.add("Effortvalue");
            fieldNames.add("Effortdate");
            fieldNames.add("Invoicenumber");
            fieldNames.add("Invoicedate");
            fieldNames.add("Invoicepath");
            fieldNames.add("Description");
            fieldNames.add("MoreProps");
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
        if (name.equals("Account"))
        {
            return getAccount();
        }
        if (name.equals("Person"))
        {
            return getPerson();
        }
        if (name.equals("Workitem"))
        {
            return getWorkitem();
        }
        if (name.equals("Hours"))
        {
            return getHours();
        }
        if (name.equals("Cost"))
        {
            return getCost();
        }
        if (name.equals("Subject"))
        {
            return getSubject();
        }
        if (name.equals("Efforttype"))
        {
            return getEfforttype();
        }
        if (name.equals("Effortvalue"))
        {
            return getEffortvalue();
        }
        if (name.equals("Effortdate"))
        {
            return getEffortdate();
        }
        if (name.equals("Invoicenumber"))
        {
            return getInvoicenumber();
        }
        if (name.equals("Invoicedate"))
        {
            return getInvoicedate();
        }
        if (name.equals("Invoicepath"))
        {
            return getInvoicepath();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("MoreProps"))
        {
            return getMoreProps();
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
        if (name.equals("Account"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAccount((Integer) value);
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
        if (name.equals("Workitem"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkitem((Integer) value);
            return true;
        }
        if (name.equals("Hours"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setHours((Double) value);
            return true;
        }
        if (name.equals("Cost"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCost((Double) value);
            return true;
        }
        if (name.equals("Subject"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSubject((String) value);
            return true;
        }
        if (name.equals("Efforttype"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEfforttype((Integer) value);
            return true;
        }
        if (name.equals("Effortvalue"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEffortvalue((Double) value);
            return true;
        }
        if (name.equals("Effortdate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEffortdate((Date) value);
            return true;
        }
        if (name.equals("Invoicenumber"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setInvoicenumber((String) value);
            return true;
        }
        if (name.equals("Invoicedate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setInvoicedate((Date) value);
            return true;
        }
        if (name.equals("Invoicepath"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setInvoicepath((String) value);
            return true;
        }
        if (name.equals("Description"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDescription((String) value);
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
        if (name.equals(TCostPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TCostPeer.ACCOUNT))
        {
            return getAccount();
        }
        if (name.equals(TCostPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TCostPeer.WORKITEM))
        {
            return getWorkitem();
        }
        if (name.equals(TCostPeer.HOURS))
        {
            return getHours();
        }
        if (name.equals(TCostPeer.COST))
        {
            return getCost();
        }
        if (name.equals(TCostPeer.SUBJECT))
        {
            return getSubject();
        }
        if (name.equals(TCostPeer.EFFORTTYPE))
        {
            return getEfforttype();
        }
        if (name.equals(TCostPeer.EFFORTVALUE))
        {
            return getEffortvalue();
        }
        if (name.equals(TCostPeer.EFFORTDATE))
        {
            return getEffortdate();
        }
        if (name.equals(TCostPeer.INVOICENUMBER))
        {
            return getInvoicenumber();
        }
        if (name.equals(TCostPeer.INVOICEDATE))
        {
            return getInvoicedate();
        }
        if (name.equals(TCostPeer.INVOICEPATH))
        {
            return getInvoicepath();
        }
        if (name.equals(TCostPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TCostPeer.MOREPROPS))
        {
            return getMoreProps();
        }
        if (name.equals(TCostPeer.LASTEDIT))
        {
            return getLastEdit();
        }
        if (name.equals(TCostPeer.TPUUID))
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
      if (TCostPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TCostPeer.ACCOUNT.equals(name))
        {
            return setByName("Account", value);
        }
      if (TCostPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TCostPeer.WORKITEM.equals(name))
        {
            return setByName("Workitem", value);
        }
      if (TCostPeer.HOURS.equals(name))
        {
            return setByName("Hours", value);
        }
      if (TCostPeer.COST.equals(name))
        {
            return setByName("Cost", value);
        }
      if (TCostPeer.SUBJECT.equals(name))
        {
            return setByName("Subject", value);
        }
      if (TCostPeer.EFFORTTYPE.equals(name))
        {
            return setByName("Efforttype", value);
        }
      if (TCostPeer.EFFORTVALUE.equals(name))
        {
            return setByName("Effortvalue", value);
        }
      if (TCostPeer.EFFORTDATE.equals(name))
        {
            return setByName("Effortdate", value);
        }
      if (TCostPeer.INVOICENUMBER.equals(name))
        {
            return setByName("Invoicenumber", value);
        }
      if (TCostPeer.INVOICEDATE.equals(name))
        {
            return setByName("Invoicedate", value);
        }
      if (TCostPeer.INVOICEPATH.equals(name))
        {
            return setByName("Invoicepath", value);
        }
      if (TCostPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TCostPeer.MOREPROPS.equals(name))
        {
            return setByName("MoreProps", value);
        }
      if (TCostPeer.LASTEDIT.equals(name))
        {
            return setByName("LastEdit", value);
        }
      if (TCostPeer.TPUUID.equals(name))
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
            return getAccount();
        }
        if (pos == 2)
        {
            return getPerson();
        }
        if (pos == 3)
        {
            return getWorkitem();
        }
        if (pos == 4)
        {
            return getHours();
        }
        if (pos == 5)
        {
            return getCost();
        }
        if (pos == 6)
        {
            return getSubject();
        }
        if (pos == 7)
        {
            return getEfforttype();
        }
        if (pos == 8)
        {
            return getEffortvalue();
        }
        if (pos == 9)
        {
            return getEffortdate();
        }
        if (pos == 10)
        {
            return getInvoicenumber();
        }
        if (pos == 11)
        {
            return getInvoicedate();
        }
        if (pos == 12)
        {
            return getInvoicepath();
        }
        if (pos == 13)
        {
            return getDescription();
        }
        if (pos == 14)
        {
            return getMoreProps();
        }
        if (pos == 15)
        {
            return getLastEdit();
        }
        if (pos == 16)
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
            return setByName("Account", value);
        }
    if (position == 2)
        {
            return setByName("Person", value);
        }
    if (position == 3)
        {
            return setByName("Workitem", value);
        }
    if (position == 4)
        {
            return setByName("Hours", value);
        }
    if (position == 5)
        {
            return setByName("Cost", value);
        }
    if (position == 6)
        {
            return setByName("Subject", value);
        }
    if (position == 7)
        {
            return setByName("Efforttype", value);
        }
    if (position == 8)
        {
            return setByName("Effortvalue", value);
        }
    if (position == 9)
        {
            return setByName("Effortdate", value);
        }
    if (position == 10)
        {
            return setByName("Invoicenumber", value);
        }
    if (position == 11)
        {
            return setByName("Invoicedate", value);
        }
    if (position == 12)
        {
            return setByName("Invoicepath", value);
        }
    if (position == 13)
        {
            return setByName("Description", value);
        }
    if (position == 14)
        {
            return setByName("MoreProps", value);
        }
    if (position == 15)
        {
            return setByName("LastEdit", value);
        }
    if (position == 16)
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
        save(TCostPeer.DATABASE_NAME);
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
                    TCostPeer.doInsert((TCost) this, con);
                    setNew(false);
                }
                else
                {
                    TCostPeer.doUpdate((TCost) this, con);
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
    public TCost copy() throws TorqueException
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
    public TCost copy(Connection con) throws TorqueException
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
    public TCost copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TCost(), deepcopy);
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
    public TCost copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TCost(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TCost copyInto(TCost copyObj) throws TorqueException
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
    protected TCost copyInto(TCost copyObj, Connection con) throws TorqueException
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
    protected TCost copyInto(TCost copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAccount(account);
        copyObj.setPerson(person);
        copyObj.setWorkitem(workitem);
        copyObj.setHours(hours);
        copyObj.setCost(cost);
        copyObj.setSubject(subject);
        copyObj.setEfforttype(efforttype);
        copyObj.setEffortvalue(effortvalue);
        copyObj.setEffortdate(effortdate);
        copyObj.setInvoicenumber(invoicenumber);
        copyObj.setInvoicedate(invoicedate);
        copyObj.setInvoicepath(invoicepath);
        copyObj.setDescription(description);
        copyObj.setMoreProps(moreProps);
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
    protected TCost copyInto(TCost copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAccount(account);
        copyObj.setPerson(person);
        copyObj.setWorkitem(workitem);
        copyObj.setHours(hours);
        copyObj.setCost(cost);
        copyObj.setSubject(subject);
        copyObj.setEfforttype(efforttype);
        copyObj.setEffortvalue(effortvalue);
        copyObj.setEffortdate(effortdate);
        copyObj.setInvoicenumber(invoicenumber);
        copyObj.setInvoicedate(invoicedate);
        copyObj.setInvoicepath(invoicepath);
        copyObj.setDescription(description);
        copyObj.setMoreProps(moreProps);
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
    public TCostPeer getPeer()
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
        return TCostPeer.getTableMap();
    }

  
    /**
     * Creates a TCostBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TCostBean with the contents of this object
     */
    public TCostBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TCostBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TCostBean with the contents of this object
     */
    public TCostBean getBean(IdentityMap createdBeans)
    {
        TCostBean result = (TCostBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TCostBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setAccount(getAccount());
        result.setPerson(getPerson());
        result.setWorkitem(getWorkitem());
        result.setHours(getHours());
        result.setCost(getCost());
        result.setSubject(getSubject());
        result.setEfforttype(getEfforttype());
        result.setEffortvalue(getEffortvalue());
        result.setEffortdate(getEffortdate());
        result.setInvoicenumber(getInvoicenumber());
        result.setInvoicedate(getInvoicedate());
        result.setInvoicepath(getInvoicepath());
        result.setDescription(getDescription());
        result.setMoreProps(getMoreProps());
        result.setLastEdit(getLastEdit());
        result.setUuid(getUuid());





        if (aTAccount != null)
        {
            TAccountBean relatedBean = aTAccount.getBean(createdBeans);
            result.setTAccountBean(relatedBean);
        }



        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTWorkItem != null)
        {
            TWorkItemBean relatedBean = aTWorkItem.getBean(createdBeans);
            result.setTWorkItemBean(relatedBean);
        }



        if (aTEffortType != null)
        {
            TEffortTypeBean relatedBean = aTEffortType.getBean(createdBeans);
            result.setTEffortTypeBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TCost with the contents
     * of a TCostBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TCostBean which contents are used to create
     *        the resulting class
     * @return an instance of TCost with the contents of bean
     */
    public static TCost createTCost(TCostBean bean)
        throws TorqueException
    {
        return createTCost(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TCost with the contents
     * of a TCostBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TCostBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TCost with the contents of bean
     */

    public static TCost createTCost(TCostBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TCost result = (TCost) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TCost();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setAccount(bean.getAccount());
        result.setPerson(bean.getPerson());
        result.setWorkitem(bean.getWorkitem());
        result.setHours(bean.getHours());
        result.setCost(bean.getCost());
        result.setSubject(bean.getSubject());
        result.setEfforttype(bean.getEfforttype());
        result.setEffortvalue(bean.getEffortvalue());
        result.setEffortdate(bean.getEffortdate());
        result.setInvoicenumber(bean.getInvoicenumber());
        result.setInvoicedate(bean.getInvoicedate());
        result.setInvoicepath(bean.getInvoicepath());
        result.setDescription(bean.getDescription());
        result.setMoreProps(bean.getMoreProps());
        result.setLastEdit(bean.getLastEdit());
        result.setUuid(bean.getUuid());





        {
            TAccountBean relatedBean = bean.getTAccountBean();
            if (relatedBean != null)
            {
                TAccount relatedObject = TAccount.createTAccount(relatedBean, createdObjects);
                result.setTAccount(relatedObject);
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



        {
            TWorkItemBean relatedBean = bean.getTWorkItemBean();
            if (relatedBean != null)
            {
                TWorkItem relatedObject = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                result.setTWorkItem(relatedObject);
            }
        }



        {
            TEffortTypeBean relatedBean = bean.getTEffortTypeBean();
            if (relatedBean != null)
            {
                TEffortType relatedObject = TEffortType.createTEffortType(relatedBean, createdObjects);
                result.setTEffortType(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TCost:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Account = ")
           .append(getAccount())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("Workitem = ")
           .append(getWorkitem())
           .append("\n");
        str.append("Hours = ")
           .append(getHours())
           .append("\n");
        str.append("Cost = ")
           .append(getCost())
           .append("\n");
        str.append("Subject = ")
           .append(getSubject())
           .append("\n");
        str.append("Efforttype = ")
           .append(getEfforttype())
           .append("\n");
        str.append("Effortvalue = ")
           .append(getEffortvalue())
           .append("\n");
        str.append("Effortdate = ")
           .append(getEffortdate())
           .append("\n");
        str.append("Invoicenumber = ")
           .append(getInvoicenumber())
           .append("\n");
        str.append("Invoicedate = ")
           .append(getInvoicedate())
           .append("\n");
        str.append("Invoicepath = ")
           .append(getInvoicepath())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("MoreProps = ")
           .append(getMoreProps())
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
