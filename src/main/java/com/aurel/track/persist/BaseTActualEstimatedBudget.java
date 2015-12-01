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

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TPersonBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TActualEstimatedBudget
 */
public abstract class BaseTActualEstimatedBudget extends TpBaseObject
{
    /** The Peer class */
    private static final TActualEstimatedBudgetPeer peer =
        new TActualEstimatedBudgetPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the workItemID field */
    private Integer workItemID;

    /** The value for the estimatedHours field */
    private Double estimatedHours;

    /** The value for the timeUnit field */
    private Integer timeUnit;

    /** The value for the estimatedCost field */
    private Double estimatedCost;

    /** The value for the efforttype field */
    private Integer efforttype;

    /** The value for the effortvalue field */
    private Double effortvalue;

    /** The value for the changedByID field */
    private Integer changedByID;

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
     * Get the WorkItemID
     *
     * @return Integer
     */
    public Integer getWorkItemID()
    {
        return workItemID;
    }


    /**
     * Set the value of WorkItemID
     *
     * @param v new value
     */
    public void setWorkItemID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workItemID, v))
        {
            this.workItemID = v;
            setModified(true);
        }


        if (aTWorkItem != null && !ObjectUtils.equals(aTWorkItem.getObjectID(), v))
        {
            aTWorkItem = null;
        }

    }

    /**
     * Get the EstimatedHours
     *
     * @return Double
     */
    public Double getEstimatedHours()
    {
        return estimatedHours;
    }


    /**
     * Set the value of EstimatedHours
     *
     * @param v new value
     */
    public void setEstimatedHours(Double v) 
    {

        if (!ObjectUtils.equals(this.estimatedHours, v))
        {
            this.estimatedHours = v;
            setModified(true);
        }


    }

    /**
     * Get the TimeUnit
     *
     * @return Integer
     */
    public Integer getTimeUnit()
    {
        return timeUnit;
    }


    /**
     * Set the value of TimeUnit
     *
     * @param v new value
     */
    public void setTimeUnit(Integer v) 
    {

        if (!ObjectUtils.equals(this.timeUnit, v))
        {
            this.timeUnit = v;
            setModified(true);
        }


    }

    /**
     * Get the EstimatedCost
     *
     * @return Double
     */
    public Double getEstimatedCost()
    {
        return estimatedCost;
    }


    /**
     * Set the value of EstimatedCost
     *
     * @param v new value
     */
    public void setEstimatedCost(Double v) 
    {

        if (!ObjectUtils.equals(this.estimatedCost, v))
        {
            this.estimatedCost = v;
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
    public void setEfforttype(Integer v) 
    {

        if (!ObjectUtils.equals(this.efforttype, v))
        {
            this.efforttype = v;
            setModified(true);
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
     * Get the ChangedByID
     *
     * @return Integer
     */
    public Integer getChangedByID()
    {
        return changedByID;
    }


    /**
     * Set the value of ChangedByID
     *
     * @param v new value
     */
    public void setChangedByID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.changedByID, v))
        {
            this.changedByID = v;
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
            setWorkItemID((Integer) null);
        }
        else
        {
            setWorkItemID(v.getObjectID());
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
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItemID, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItemID));
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
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItemID, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItemID), connection);
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

        setWorkItemID(new Integer(((NumberKey) key).intValue()));
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
            setChangedByID((Integer) null);
        }
        else
        {
            setChangedByID(v.getObjectID());
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
        if (aTPerson == null && (!ObjectUtils.equals(this.changedByID, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.changedByID));
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
        if (aTPerson == null && (!ObjectUtils.equals(this.changedByID, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.changedByID), connection);
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

        setChangedByID(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("WorkItemID");
            fieldNames.add("EstimatedHours");
            fieldNames.add("TimeUnit");
            fieldNames.add("EstimatedCost");
            fieldNames.add("Efforttype");
            fieldNames.add("Effortvalue");
            fieldNames.add("ChangedByID");
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
        if (name.equals("WorkItemID"))
        {
            return getWorkItemID();
        }
        if (name.equals("EstimatedHours"))
        {
            return getEstimatedHours();
        }
        if (name.equals("TimeUnit"))
        {
            return getTimeUnit();
        }
        if (name.equals("EstimatedCost"))
        {
            return getEstimatedCost();
        }
        if (name.equals("Efforttype"))
        {
            return getEfforttype();
        }
        if (name.equals("Effortvalue"))
        {
            return getEffortvalue();
        }
        if (name.equals("ChangedByID"))
        {
            return getChangedByID();
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
        if (name.equals("WorkItemID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkItemID((Integer) value);
            return true;
        }
        if (name.equals("EstimatedHours"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEstimatedHours((Double) value);
            return true;
        }
        if (name.equals("TimeUnit"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTimeUnit((Integer) value);
            return true;
        }
        if (name.equals("EstimatedCost"))
        {
            // Object fields can be null
            if (value != null && ! Double.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEstimatedCost((Double) value);
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
        if (name.equals("ChangedByID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setChangedByID((Integer) value);
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
        if (name.equals(TActualEstimatedBudgetPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TActualEstimatedBudgetPeer.WORKITEMKEY))
        {
            return getWorkItemID();
        }
        if (name.equals(TActualEstimatedBudgetPeer.ESTIMATEDHOURS))
        {
            return getEstimatedHours();
        }
        if (name.equals(TActualEstimatedBudgetPeer.TIMEUNIT))
        {
            return getTimeUnit();
        }
        if (name.equals(TActualEstimatedBudgetPeer.ESTIMATEDCOST))
        {
            return getEstimatedCost();
        }
        if (name.equals(TActualEstimatedBudgetPeer.EFFORTTYPE))
        {
            return getEfforttype();
        }
        if (name.equals(TActualEstimatedBudgetPeer.EFFORTVALUE))
        {
            return getEffortvalue();
        }
        if (name.equals(TActualEstimatedBudgetPeer.CHANGEDBY))
        {
            return getChangedByID();
        }
        if (name.equals(TActualEstimatedBudgetPeer.LASTEDIT))
        {
            return getLastEdit();
        }
        if (name.equals(TActualEstimatedBudgetPeer.TPUUID))
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
      if (TActualEstimatedBudgetPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TActualEstimatedBudgetPeer.WORKITEMKEY.equals(name))
        {
            return setByName("WorkItemID", value);
        }
      if (TActualEstimatedBudgetPeer.ESTIMATEDHOURS.equals(name))
        {
            return setByName("EstimatedHours", value);
        }
      if (TActualEstimatedBudgetPeer.TIMEUNIT.equals(name))
        {
            return setByName("TimeUnit", value);
        }
      if (TActualEstimatedBudgetPeer.ESTIMATEDCOST.equals(name))
        {
            return setByName("EstimatedCost", value);
        }
      if (TActualEstimatedBudgetPeer.EFFORTTYPE.equals(name))
        {
            return setByName("Efforttype", value);
        }
      if (TActualEstimatedBudgetPeer.EFFORTVALUE.equals(name))
        {
            return setByName("Effortvalue", value);
        }
      if (TActualEstimatedBudgetPeer.CHANGEDBY.equals(name))
        {
            return setByName("ChangedByID", value);
        }
      if (TActualEstimatedBudgetPeer.LASTEDIT.equals(name))
        {
            return setByName("LastEdit", value);
        }
      if (TActualEstimatedBudgetPeer.TPUUID.equals(name))
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
            return getWorkItemID();
        }
        if (pos == 2)
        {
            return getEstimatedHours();
        }
        if (pos == 3)
        {
            return getTimeUnit();
        }
        if (pos == 4)
        {
            return getEstimatedCost();
        }
        if (pos == 5)
        {
            return getEfforttype();
        }
        if (pos == 6)
        {
            return getEffortvalue();
        }
        if (pos == 7)
        {
            return getChangedByID();
        }
        if (pos == 8)
        {
            return getLastEdit();
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
            return setByName("WorkItemID", value);
        }
    if (position == 2)
        {
            return setByName("EstimatedHours", value);
        }
    if (position == 3)
        {
            return setByName("TimeUnit", value);
        }
    if (position == 4)
        {
            return setByName("EstimatedCost", value);
        }
    if (position == 5)
        {
            return setByName("Efforttype", value);
        }
    if (position == 6)
        {
            return setByName("Effortvalue", value);
        }
    if (position == 7)
        {
            return setByName("ChangedByID", value);
        }
    if (position == 8)
        {
            return setByName("LastEdit", value);
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
        save(TActualEstimatedBudgetPeer.DATABASE_NAME);
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
                    TActualEstimatedBudgetPeer.doInsert((TActualEstimatedBudget) this, con);
                    setNew(false);
                }
                else
                {
                    TActualEstimatedBudgetPeer.doUpdate((TActualEstimatedBudget) this, con);
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
    public TActualEstimatedBudget copy() throws TorqueException
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
    public TActualEstimatedBudget copy(Connection con) throws TorqueException
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
    public TActualEstimatedBudget copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TActualEstimatedBudget(), deepcopy);
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
    public TActualEstimatedBudget copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TActualEstimatedBudget(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TActualEstimatedBudget copyInto(TActualEstimatedBudget copyObj) throws TorqueException
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
    protected TActualEstimatedBudget copyInto(TActualEstimatedBudget copyObj, Connection con) throws TorqueException
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
    protected TActualEstimatedBudget copyInto(TActualEstimatedBudget copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkItemID(workItemID);
        copyObj.setEstimatedHours(estimatedHours);
        copyObj.setTimeUnit(timeUnit);
        copyObj.setEstimatedCost(estimatedCost);
        copyObj.setEfforttype(efforttype);
        copyObj.setEffortvalue(effortvalue);
        copyObj.setChangedByID(changedByID);
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
    protected TActualEstimatedBudget copyInto(TActualEstimatedBudget copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkItemID(workItemID);
        copyObj.setEstimatedHours(estimatedHours);
        copyObj.setTimeUnit(timeUnit);
        copyObj.setEstimatedCost(estimatedCost);
        copyObj.setEfforttype(efforttype);
        copyObj.setEffortvalue(effortvalue);
        copyObj.setChangedByID(changedByID);
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
    public TActualEstimatedBudgetPeer getPeer()
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
        return TActualEstimatedBudgetPeer.getTableMap();
    }

  
    /**
     * Creates a TActualEstimatedBudgetBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TActualEstimatedBudgetBean with the contents of this object
     */
    public TActualEstimatedBudgetBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TActualEstimatedBudgetBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TActualEstimatedBudgetBean with the contents of this object
     */
    public TActualEstimatedBudgetBean getBean(IdentityMap createdBeans)
    {
        TActualEstimatedBudgetBean result = (TActualEstimatedBudgetBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TActualEstimatedBudgetBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setWorkItemID(getWorkItemID());
        result.setEstimatedHours(getEstimatedHours());
        result.setTimeUnit(getTimeUnit());
        result.setEstimatedCost(getEstimatedCost());
        result.setEfforttype(getEfforttype());
        result.setEffortvalue(getEffortvalue());
        result.setChangedByID(getChangedByID());
        result.setLastEdit(getLastEdit());
        result.setUuid(getUuid());





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
     * Creates an instance of TActualEstimatedBudget with the contents
     * of a TActualEstimatedBudgetBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TActualEstimatedBudgetBean which contents are used to create
     *        the resulting class
     * @return an instance of TActualEstimatedBudget with the contents of bean
     */
    public static TActualEstimatedBudget createTActualEstimatedBudget(TActualEstimatedBudgetBean bean)
        throws TorqueException
    {
        return createTActualEstimatedBudget(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TActualEstimatedBudget with the contents
     * of a TActualEstimatedBudgetBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TActualEstimatedBudgetBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TActualEstimatedBudget with the contents of bean
     */

    public static TActualEstimatedBudget createTActualEstimatedBudget(TActualEstimatedBudgetBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TActualEstimatedBudget result = (TActualEstimatedBudget) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TActualEstimatedBudget();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setWorkItemID(bean.getWorkItemID());
        result.setEstimatedHours(bean.getEstimatedHours());
        result.setTimeUnit(bean.getTimeUnit());
        result.setEstimatedCost(bean.getEstimatedCost());
        result.setEfforttype(bean.getEfforttype());
        result.setEffortvalue(bean.getEffortvalue());
        result.setChangedByID(bean.getChangedByID());
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
        str.append("TActualEstimatedBudget:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("WorkItemID = ")
           .append(getWorkItemID())
           .append("\n");
        str.append("EstimatedHours = ")
           .append(getEstimatedHours())
           .append("\n");
        str.append("TimeUnit = ")
           .append(getTimeUnit())
           .append("\n");
        str.append("EstimatedCost = ")
           .append(getEstimatedCost())
           .append("\n");
        str.append("Efforttype = ")
           .append(getEfforttype())
           .append("\n");
        str.append("Effortvalue = ")
           .append(getEffortvalue())
           .append("\n");
        str.append("ChangedByID = ")
           .append(getChangedByID())
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
