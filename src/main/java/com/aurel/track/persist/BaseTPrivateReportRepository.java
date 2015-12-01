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



import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TPrivateReportRepositoryBean;
import com.aurel.track.beans.TPersonBean;

import com.aurel.track.beans.TPersonBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TPrivateReportRepository
 */
public abstract class BaseTPrivateReportRepository extends TpBaseObject
{
    /** The Peer class */
    private static final TPrivateReportRepositoryPeer peer =
        new TPrivateReportRepositoryPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the ownerID field */
    private Integer ownerID;

    /** The value for the name field */
    private String name;

    /** The value for the query field */
    private String query;

    /** The value for the menuItem field */
    private String menuItem = "N";

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



        // update associated TPerson
        if (collTPersons != null)
        {
            for (int i = 0; i < collTPersons.size(); i++)
            {
                ((TPerson) collTPersons.get(i))
                        .setMyDefaultReport(v);
            }
        }
    }

    /**
     * Get the OwnerID
     *
     * @return Integer
     */
    public Integer getOwnerID()
    {
        return ownerID;
    }


    /**
     * Set the value of OwnerID
     *
     * @param v new value
     */
    public void setOwnerID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.ownerID, v))
        {
            this.ownerID = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
        }

    }

    /**
     * Get the Name
     *
     * @return String
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set the value of Name
     *
     * @param v new value
     */
    public void setName(String v) 
    {

        if (!ObjectUtils.equals(this.name, v))
        {
            this.name = v;
            setModified(true);
        }


    }

    /**
     * Get the Query
     *
     * @return String
     */
    public String getQuery()
    {
        return query;
    }


    /**
     * Set the value of Query
     *
     * @param v new value
     */
    public void setQuery(String v) 
    {

        if (!ObjectUtils.equals(this.query, v))
        {
            this.query = v;
            setModified(true);
        }


    }

    /**
     * Get the MenuItem
     *
     * @return String
     */
    public String getMenuItem()
    {
        return menuItem;
    }


    /**
     * Set the value of MenuItem
     *
     * @param v new value
     */
    public void setMenuItem(String v) 
    {

        if (!ObjectUtils.equals(this.menuItem, v))
        {
            this.menuItem = v;
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
            setOwnerID((Integer) null);
        }
        else
        {
            setOwnerID(v.getObjectID());
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
        if (aTPerson == null && (!ObjectUtils.equals(this.ownerID, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.ownerID));
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
        if (aTPerson == null && (!ObjectUtils.equals(this.ownerID, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.ownerID), connection);
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

        setOwnerID(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTPersons
     */
    protected List<TPerson> collTPersons;

    /**
     * Temporary storage of collTPersons to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPersons()
    {
        if (collTPersons == null)
        {
            collTPersons = new ArrayList<TPerson>();
        }
    }


    /**
     * Method called to associate a TPerson object to this object
     * through the TPerson foreign key attribute
     *
     * @param l TPerson
     * @throws TorqueException
     */
    public void addTPerson(TPerson l) throws TorqueException
    {
        getTPersons().add(l);
        l.setTPrivateReportRepository((TPrivateReportRepository) this);
    }

    /**
     * Method called to associate a TPerson object to this object
     * through the TPerson foreign key attribute using connection.
     *
     * @param l TPerson
     * @throws TorqueException
     */
    public void addTPerson(TPerson l, Connection con) throws TorqueException
    {
        getTPersons(con).add(l);
        l.setTPrivateReportRepository((TPrivateReportRepository) this);
    }

    /**
     * The criteria used to select the current contents of collTPersons
     */
    private Criteria lastTPersonsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPersons(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPerson> getTPersons()
        throws TorqueException
    {
        if (collTPersons == null)
        {
            collTPersons = getTPersons(new Criteria(10));
        }
        return collTPersons;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPrivateReportRepository has previously
     * been saved, it will retrieve related TPersons from storage.
     * If this TPrivateReportRepository is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPerson> getTPersons(Criteria criteria) throws TorqueException
    {
        if (collTPersons == null)
        {
            if (isNew())
            {
               collTPersons = new ArrayList<TPerson>();
            }
            else
            {
                criteria.add(TPersonPeer.MYDEFAULTREPORT, getObjectID() );
                collTPersons = TPersonPeer.doSelect(criteria);
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
                criteria.add(TPersonPeer.MYDEFAULTREPORT, getObjectID());
                if (!lastTPersonsCriteria.equals(criteria))
                {
                    collTPersons = TPersonPeer.doSelect(criteria);
                }
            }
        }
        lastTPersonsCriteria = criteria;

        return collTPersons;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPersons(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPerson> getTPersons(Connection con) throws TorqueException
    {
        if (collTPersons == null)
        {
            collTPersons = getTPersons(new Criteria(10), con);
        }
        return collTPersons;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPrivateReportRepository has previously
     * been saved, it will retrieve related TPersons from storage.
     * If this TPrivateReportRepository is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPerson> getTPersons(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPersons == null)
        {
            if (isNew())
            {
               collTPersons = new ArrayList<TPerson>();
            }
            else
            {
                 criteria.add(TPersonPeer.MYDEFAULTREPORT, getObjectID());
                 collTPersons = TPersonPeer.doSelect(criteria, con);
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
                 criteria.add(TPersonPeer.MYDEFAULTREPORT, getObjectID());
                 if (!lastTPersonsCriteria.equals(criteria))
                 {
                     collTPersons = TPersonPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPersonsCriteria = criteria;

         return collTPersons;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPrivateReportRepository is new, it will return
     * an empty collection; or if this TPrivateReportRepository has previously
     * been saved, it will retrieve related TPersons from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPrivateReportRepository.
     */
    protected List<TPerson> getTPersonsJoinTDepartment(Criteria criteria)
        throws TorqueException
    {
        if (collTPersons == null)
        {
            if (isNew())
            {
               collTPersons = new ArrayList<TPerson>();
            }
            else
            {
                criteria.add(TPersonPeer.MYDEFAULTREPORT, getObjectID());
                collTPersons = TPersonPeer.doSelectJoinTDepartment(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonPeer.MYDEFAULTREPORT, getObjectID());
            if (!lastTPersonsCriteria.equals(criteria))
            {
                collTPersons = TPersonPeer.doSelectJoinTDepartment(criteria);
            }
        }
        lastTPersonsCriteria = criteria;

        return collTPersons;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPrivateReportRepository is new, it will return
     * an empty collection; or if this TPrivateReportRepository has previously
     * been saved, it will retrieve related TPersons from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPrivateReportRepository.
     */
    protected List<TPerson> getTPersonsJoinTPrivateReportRepository(Criteria criteria)
        throws TorqueException
    {
        if (collTPersons == null)
        {
            if (isNew())
            {
               collTPersons = new ArrayList<TPerson>();
            }
            else
            {
                criteria.add(TPersonPeer.MYDEFAULTREPORT, getObjectID());
                collTPersons = TPersonPeer.doSelectJoinTPrivateReportRepository(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonPeer.MYDEFAULTREPORT, getObjectID());
            if (!lastTPersonsCriteria.equals(criteria))
            {
                collTPersons = TPersonPeer.doSelectJoinTPrivateReportRepository(criteria);
            }
        }
        lastTPersonsCriteria = criteria;

        return collTPersons;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPrivateReportRepository is new, it will return
     * an empty collection; or if this TPrivateReportRepository has previously
     * been saved, it will retrieve related TPersons from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPrivateReportRepository.
     */
    protected List<TPerson> getTPersonsJoinTBLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTPersons == null)
        {
            if (isNew())
            {
               collTPersons = new ArrayList<TPerson>();
            }
            else
            {
                criteria.add(TPersonPeer.MYDEFAULTREPORT, getObjectID());
                collTPersons = TPersonPeer.doSelectJoinTBLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonPeer.MYDEFAULTREPORT, getObjectID());
            if (!lastTPersonsCriteria.equals(criteria))
            {
                collTPersons = TPersonPeer.doSelectJoinTBLOB(criteria);
            }
        }
        lastTPersonsCriteria = criteria;

        return collTPersons;
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
            fieldNames.add("OwnerID");
            fieldNames.add("Name");
            fieldNames.add("Query");
            fieldNames.add("MenuItem");
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
        if (name.equals("OwnerID"))
        {
            return getOwnerID();
        }
        if (name.equals("Name"))
        {
            return getName();
        }
        if (name.equals("Query"))
        {
            return getQuery();
        }
        if (name.equals("MenuItem"))
        {
            return getMenuItem();
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
        if (name.equals("OwnerID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOwnerID((Integer) value);
            return true;
        }
        if (name.equals("Name"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setName((String) value);
            return true;
        }
        if (name.equals("Query"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setQuery((String) value);
            return true;
        }
        if (name.equals("MenuItem"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMenuItem((String) value);
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
        if (name.equals(TPrivateReportRepositoryPeer.PKEY))
        {
            return getObjectID();
        }
        if (name.equals(TPrivateReportRepositoryPeer.OWNER))
        {
            return getOwnerID();
        }
        if (name.equals(TPrivateReportRepositoryPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TPrivateReportRepositoryPeer.QUERY))
        {
            return getQuery();
        }
        if (name.equals(TPrivateReportRepositoryPeer.MENUITEM))
        {
            return getMenuItem();
        }
        if (name.equals(TPrivateReportRepositoryPeer.TPUUID))
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
      if (TPrivateReportRepositoryPeer.PKEY.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TPrivateReportRepositoryPeer.OWNER.equals(name))
        {
            return setByName("OwnerID", value);
        }
      if (TPrivateReportRepositoryPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TPrivateReportRepositoryPeer.QUERY.equals(name))
        {
            return setByName("Query", value);
        }
      if (TPrivateReportRepositoryPeer.MENUITEM.equals(name))
        {
            return setByName("MenuItem", value);
        }
      if (TPrivateReportRepositoryPeer.TPUUID.equals(name))
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
            return getOwnerID();
        }
        if (pos == 2)
        {
            return getName();
        }
        if (pos == 3)
        {
            return getQuery();
        }
        if (pos == 4)
        {
            return getMenuItem();
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
            return setByName("OwnerID", value);
        }
    if (position == 2)
        {
            return setByName("Name", value);
        }
    if (position == 3)
        {
            return setByName("Query", value);
        }
    if (position == 4)
        {
            return setByName("MenuItem", value);
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
        save(TPrivateReportRepositoryPeer.DATABASE_NAME);
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
                    TPrivateReportRepositoryPeer.doInsert((TPrivateReportRepository) this, con);
                    setNew(false);
                }
                else
                {
                    TPrivateReportRepositoryPeer.doUpdate((TPrivateReportRepository) this, con);
                }
            }


            if (collTPersons != null)
            {
                for (int i = 0; i < collTPersons.size(); i++)
                {
                    ((TPerson) collTPersons.get(i)).save(con);
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
    public TPrivateReportRepository copy() throws TorqueException
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
    public TPrivateReportRepository copy(Connection con) throws TorqueException
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
    public TPrivateReportRepository copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TPrivateReportRepository(), deepcopy);
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
    public TPrivateReportRepository copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TPrivateReportRepository(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TPrivateReportRepository copyInto(TPrivateReportRepository copyObj) throws TorqueException
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
    protected TPrivateReportRepository copyInto(TPrivateReportRepository copyObj, Connection con) throws TorqueException
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
    protected TPrivateReportRepository copyInto(TPrivateReportRepository copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setOwnerID(ownerID);
        copyObj.setName(name);
        copyObj.setQuery(query);
        copyObj.setMenuItem(menuItem);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TPerson> vTPersons = getTPersons();
        if (vTPersons != null)
        {
            for (int i = 0; i < vTPersons.size(); i++)
            {
                TPerson obj =  vTPersons.get(i);
                copyObj.addTPerson(obj.copy());
            }
        }
        else
        {
            copyObj.collTPersons = null;
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
    protected TPrivateReportRepository copyInto(TPrivateReportRepository copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setOwnerID(ownerID);
        copyObj.setName(name);
        copyObj.setQuery(query);
        copyObj.setMenuItem(menuItem);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TPerson> vTPersons = getTPersons(con);
        if (vTPersons != null)
        {
            for (int i = 0; i < vTPersons.size(); i++)
            {
                TPerson obj =  vTPersons.get(i);
                copyObj.addTPerson(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPersons = null;
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
    public TPrivateReportRepositoryPeer getPeer()
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
        return TPrivateReportRepositoryPeer.getTableMap();
    }

  
    /**
     * Creates a TPrivateReportRepositoryBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TPrivateReportRepositoryBean with the contents of this object
     */
    public TPrivateReportRepositoryBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TPrivateReportRepositoryBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TPrivateReportRepositoryBean with the contents of this object
     */
    public TPrivateReportRepositoryBean getBean(IdentityMap createdBeans)
    {
        TPrivateReportRepositoryBean result = (TPrivateReportRepositoryBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TPrivateReportRepositoryBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setOwnerID(getOwnerID());
        result.setName(getName());
        result.setQuery(getQuery());
        result.setMenuItem(getMenuItem());
        result.setUuid(getUuid());



        if (collTPersons != null)
        {
            List<TPersonBean> relatedBeans = new ArrayList<TPersonBean>(collTPersons.size());
            for (Iterator<TPerson> collTPersonsIt = collTPersons.iterator(); collTPersonsIt.hasNext(); )
            {
                TPerson related = (TPerson) collTPersonsIt.next();
                TPersonBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPersonBeans(relatedBeans);
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
     * Creates an instance of TPrivateReportRepository with the contents
     * of a TPrivateReportRepositoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TPrivateReportRepositoryBean which contents are used to create
     *        the resulting class
     * @return an instance of TPrivateReportRepository with the contents of bean
     */
    public static TPrivateReportRepository createTPrivateReportRepository(TPrivateReportRepositoryBean bean)
        throws TorqueException
    {
        return createTPrivateReportRepository(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TPrivateReportRepository with the contents
     * of a TPrivateReportRepositoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TPrivateReportRepositoryBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TPrivateReportRepository with the contents of bean
     */

    public static TPrivateReportRepository createTPrivateReportRepository(TPrivateReportRepositoryBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TPrivateReportRepository result = (TPrivateReportRepository) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TPrivateReportRepository();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setOwnerID(bean.getOwnerID());
        result.setName(bean.getName());
        result.setQuery(bean.getQuery());
        result.setMenuItem(bean.getMenuItem());
        result.setUuid(bean.getUuid());



        {
            List<TPersonBean> relatedBeans = bean.getTPersonBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPersonBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPersonBean relatedBean =  relatedBeansIt.next();
                    TPerson related = TPerson.createTPerson(relatedBean, createdObjects);
                    result.addTPersonFromBean(related);
                }
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
     * Method called to associate a TPerson object to this object.
     * through the TPerson foreign key attribute
     *
     * @param toAdd TPerson
     */
    protected void addTPersonFromBean(TPerson toAdd)
    {
        initTPersons();
        collTPersons.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TPrivateReportRepository:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("OwnerID = ")
           .append(getOwnerID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Query = ")
           .append(getQuery())
           .append("\n");
        str.append("MenuItem = ")
           .append(getMenuItem())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
