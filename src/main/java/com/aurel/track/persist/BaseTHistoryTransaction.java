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



import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.THistoryTransactionBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TPersonBean;

import com.aurel.track.beans.TFieldChangeBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to THistoryTransaction
 */
public abstract class BaseTHistoryTransaction extends TpBaseObject
{
    /** The Peer class */
    private static final THistoryTransactionPeer peer =
        new THistoryTransactionPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the workItem field */
    private Integer workItem;

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
    public void setObjectID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
        }



        // update associated TFieldChange
        if (collTFieldChanges != null)
        {
            for (int i = 0; i < collTFieldChanges.size(); i++)
            {
                ((TFieldChange) collTFieldChanges.get(i))
                        .setHistoryTransaction(v);
            }
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
   


    /**
     * Collection to store aggregation of collTFieldChanges
     */
    protected List<TFieldChange> collTFieldChanges;

    /**
     * Temporary storage of collTFieldChanges to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTFieldChanges()
    {
        if (collTFieldChanges == null)
        {
            collTFieldChanges = new ArrayList<TFieldChange>();
        }
    }


    /**
     * Method called to associate a TFieldChange object to this object
     * through the TFieldChange foreign key attribute
     *
     * @param l TFieldChange
     * @throws TorqueException
     */
    public void addTFieldChange(TFieldChange l) throws TorqueException
    {
        getTFieldChanges().add(l);
        l.setTHistoryTransaction((THistoryTransaction) this);
    }

    /**
     * Method called to associate a TFieldChange object to this object
     * through the TFieldChange foreign key attribute using connection.
     *
     * @param l TFieldChange
     * @throws TorqueException
     */
    public void addTFieldChange(TFieldChange l, Connection con) throws TorqueException
    {
        getTFieldChanges(con).add(l);
        l.setTHistoryTransaction((THistoryTransaction) this);
    }

    /**
     * The criteria used to select the current contents of collTFieldChanges
     */
    private Criteria lastTFieldChangesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldChanges(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TFieldChange> getTFieldChanges()
        throws TorqueException
    {
        if (collTFieldChanges == null)
        {
            collTFieldChanges = getTFieldChanges(new Criteria(10));
        }
        return collTFieldChanges;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this THistoryTransaction has previously
     * been saved, it will retrieve related TFieldChanges from storage.
     * If this THistoryTransaction is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TFieldChange> getTFieldChanges(Criteria criteria) throws TorqueException
    {
        if (collTFieldChanges == null)
        {
            if (isNew())
            {
               collTFieldChanges = new ArrayList<TFieldChange>();
            }
            else
            {
                criteria.add(TFieldChangePeer.HISTORYTRANSACTION, getObjectID() );
                collTFieldChanges = TFieldChangePeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TFieldChangePeer.HISTORYTRANSACTION, getObjectID());
                if (!lastTFieldChangesCriteria.equals(criteria))
                {
                    collTFieldChanges = TFieldChangePeer.doSelect(criteria);
                }
            }
        }
        lastTFieldChangesCriteria = criteria;

        return collTFieldChanges;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldChanges(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldChange> getTFieldChanges(Connection con) throws TorqueException
    {
        if (collTFieldChanges == null)
        {
            collTFieldChanges = getTFieldChanges(new Criteria(10), con);
        }
        return collTFieldChanges;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this THistoryTransaction has previously
     * been saved, it will retrieve related TFieldChanges from storage.
     * If this THistoryTransaction is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldChange> getTFieldChanges(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTFieldChanges == null)
        {
            if (isNew())
            {
               collTFieldChanges = new ArrayList<TFieldChange>();
            }
            else
            {
                 criteria.add(TFieldChangePeer.HISTORYTRANSACTION, getObjectID());
                 collTFieldChanges = TFieldChangePeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TFieldChangePeer.HISTORYTRANSACTION, getObjectID());
                 if (!lastTFieldChangesCriteria.equals(criteria))
                 {
                     collTFieldChanges = TFieldChangePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTFieldChangesCriteria = criteria;

         return collTFieldChanges;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this THistoryTransaction is new, it will return
     * an empty collection; or if this THistoryTransaction has previously
     * been saved, it will retrieve related TFieldChanges from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in THistoryTransaction.
     */
    protected List<TFieldChange> getTFieldChangesJoinTHistoryTransaction(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldChanges == null)
        {
            if (isNew())
            {
               collTFieldChanges = new ArrayList<TFieldChange>();
            }
            else
            {
                criteria.add(TFieldChangePeer.HISTORYTRANSACTION, getObjectID());
                collTFieldChanges = TFieldChangePeer.doSelectJoinTHistoryTransaction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldChangePeer.HISTORYTRANSACTION, getObjectID());
            if (!lastTFieldChangesCriteria.equals(criteria))
            {
                collTFieldChanges = TFieldChangePeer.doSelectJoinTHistoryTransaction(criteria);
            }
        }
        lastTFieldChangesCriteria = criteria;

        return collTFieldChanges;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this THistoryTransaction is new, it will return
     * an empty collection; or if this THistoryTransaction has previously
     * been saved, it will retrieve related TFieldChanges from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in THistoryTransaction.
     */
    protected List<TFieldChange> getTFieldChangesJoinTOptionRelatedByNewCustomOptionID(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldChanges == null)
        {
            if (isNew())
            {
               collTFieldChanges = new ArrayList<TFieldChange>();
            }
            else
            {
                criteria.add(TFieldChangePeer.HISTORYTRANSACTION, getObjectID());
                collTFieldChanges = TFieldChangePeer.doSelectJoinTOptionRelatedByNewCustomOptionID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldChangePeer.HISTORYTRANSACTION, getObjectID());
            if (!lastTFieldChangesCriteria.equals(criteria))
            {
                collTFieldChanges = TFieldChangePeer.doSelectJoinTOptionRelatedByNewCustomOptionID(criteria);
            }
        }
        lastTFieldChangesCriteria = criteria;

        return collTFieldChanges;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this THistoryTransaction is new, it will return
     * an empty collection; or if this THistoryTransaction has previously
     * been saved, it will retrieve related TFieldChanges from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in THistoryTransaction.
     */
    protected List<TFieldChange> getTFieldChangesJoinTOptionRelatedByOldCustomOptionID(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldChanges == null)
        {
            if (isNew())
            {
               collTFieldChanges = new ArrayList<TFieldChange>();
            }
            else
            {
                criteria.add(TFieldChangePeer.HISTORYTRANSACTION, getObjectID());
                collTFieldChanges = TFieldChangePeer.doSelectJoinTOptionRelatedByOldCustomOptionID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldChangePeer.HISTORYTRANSACTION, getObjectID());
            if (!lastTFieldChangesCriteria.equals(criteria))
            {
                collTFieldChanges = TFieldChangePeer.doSelectJoinTOptionRelatedByOldCustomOptionID(criteria);
            }
        }
        lastTFieldChangesCriteria = criteria;

        return collTFieldChanges;
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
        if (name.equals("WorkItem"))
        {
            return getWorkItem();
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
        if (name.equals(THistoryTransactionPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(THistoryTransactionPeer.WORKITEM))
        {
            return getWorkItem();
        }
        if (name.equals(THistoryTransactionPeer.CHANGEDBY))
        {
            return getChangedByID();
        }
        if (name.equals(THistoryTransactionPeer.LASTEDIT))
        {
            return getLastEdit();
        }
        if (name.equals(THistoryTransactionPeer.TPUUID))
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
      if (THistoryTransactionPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (THistoryTransactionPeer.WORKITEM.equals(name))
        {
            return setByName("WorkItem", value);
        }
      if (THistoryTransactionPeer.CHANGEDBY.equals(name))
        {
            return setByName("ChangedByID", value);
        }
      if (THistoryTransactionPeer.LASTEDIT.equals(name))
        {
            return setByName("LastEdit", value);
        }
      if (THistoryTransactionPeer.TPUUID.equals(name))
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
            return getChangedByID();
        }
        if (pos == 3)
        {
            return getLastEdit();
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
            return setByName("WorkItem", value);
        }
    if (position == 2)
        {
            return setByName("ChangedByID", value);
        }
    if (position == 3)
        {
            return setByName("LastEdit", value);
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
        save(THistoryTransactionPeer.DATABASE_NAME);
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
                    THistoryTransactionPeer.doInsert((THistoryTransaction) this, con);
                    setNew(false);
                }
                else
                {
                    THistoryTransactionPeer.doUpdate((THistoryTransaction) this, con);
                }
            }


            if (collTFieldChanges != null)
            {
                for (int i = 0; i < collTFieldChanges.size(); i++)
                {
                    ((TFieldChange) collTFieldChanges.get(i)).save(con);
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
        throws TorqueException
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) throws TorqueException
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
    public THistoryTransaction copy() throws TorqueException
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
    public THistoryTransaction copy(Connection con) throws TorqueException
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
    public THistoryTransaction copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new THistoryTransaction(), deepcopy);
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
    public THistoryTransaction copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new THistoryTransaction(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected THistoryTransaction copyInto(THistoryTransaction copyObj) throws TorqueException
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
    protected THistoryTransaction copyInto(THistoryTransaction copyObj, Connection con) throws TorqueException
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
    protected THistoryTransaction copyInto(THistoryTransaction copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkItem(workItem);
        copyObj.setChangedByID(changedByID);
        copyObj.setLastEdit(lastEdit);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TFieldChange> vTFieldChanges = getTFieldChanges();
        if (vTFieldChanges != null)
        {
            for (int i = 0; i < vTFieldChanges.size(); i++)
            {
                TFieldChange obj =  vTFieldChanges.get(i);
                copyObj.addTFieldChange(obj.copy());
            }
        }
        else
        {
            copyObj.collTFieldChanges = null;
        }
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
    protected THistoryTransaction copyInto(THistoryTransaction copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkItem(workItem);
        copyObj.setChangedByID(changedByID);
        copyObj.setLastEdit(lastEdit);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TFieldChange> vTFieldChanges = getTFieldChanges(con);
        if (vTFieldChanges != null)
        {
            for (int i = 0; i < vTFieldChanges.size(); i++)
            {
                TFieldChange obj =  vTFieldChanges.get(i);
                copyObj.addTFieldChange(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTFieldChanges = null;
        }
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public THistoryTransactionPeer getPeer()
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
        return THistoryTransactionPeer.getTableMap();
    }

  
    /**
     * Creates a THistoryTransactionBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a THistoryTransactionBean with the contents of this object
     */
    public THistoryTransactionBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a THistoryTransactionBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a THistoryTransactionBean with the contents of this object
     */
    public THistoryTransactionBean getBean(IdentityMap createdBeans)
    {
        THistoryTransactionBean result = (THistoryTransactionBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new THistoryTransactionBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setWorkItem(getWorkItem());
        result.setChangedByID(getChangedByID());
        result.setLastEdit(getLastEdit());
        result.setUuid(getUuid());



        if (collTFieldChanges != null)
        {
            List<TFieldChangeBean> relatedBeans = new ArrayList<TFieldChangeBean>(collTFieldChanges.size());
            for (Iterator<TFieldChange> collTFieldChangesIt = collTFieldChanges.iterator(); collTFieldChangesIt.hasNext(); )
            {
                TFieldChange related = (TFieldChange) collTFieldChangesIt.next();
                TFieldChangeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTFieldChangeBeans(relatedBeans);
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
     * Creates an instance of THistoryTransaction with the contents
     * of a THistoryTransactionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the THistoryTransactionBean which contents are used to create
     *        the resulting class
     * @return an instance of THistoryTransaction with the contents of bean
     */
    public static THistoryTransaction createTHistoryTransaction(THistoryTransactionBean bean)
        throws TorqueException
    {
        return createTHistoryTransaction(bean, new IdentityMap());
    }

    /**
     * Creates an instance of THistoryTransaction with the contents
     * of a THistoryTransactionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the THistoryTransactionBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of THistoryTransaction with the contents of bean
     */

    public static THistoryTransaction createTHistoryTransaction(THistoryTransactionBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        THistoryTransaction result = (THistoryTransaction) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new THistoryTransaction();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setWorkItem(bean.getWorkItem());
        result.setChangedByID(bean.getChangedByID());
        result.setLastEdit(bean.getLastEdit());
        result.setUuid(bean.getUuid());



        {
            List<TFieldChangeBean> relatedBeans = bean.getTFieldChangeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TFieldChangeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TFieldChangeBean relatedBean =  relatedBeansIt.next();
                    TFieldChange related = TFieldChange.createTFieldChange(relatedBean, createdObjects);
                    result.addTFieldChangeFromBean(related);
                }
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



    /**
     * Method called to associate a TFieldChange object to this object.
     * through the TFieldChange foreign key attribute
     *
     * @param toAdd TFieldChange
     */
    protected void addTFieldChangeFromBean(TFieldChange toAdd)
    {
        initTFieldChanges();
        collTFieldChanges.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("THistoryTransaction:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("WorkItem = ")
           .append(getWorkItem())
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
