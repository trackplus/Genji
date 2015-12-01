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



import com.aurel.track.persist.TEffortUnit;
import com.aurel.track.persist.TEffortUnitPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TEffortTypeBean;
import com.aurel.track.beans.TEffortUnitBean;

import com.aurel.track.beans.TCostBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TEffortType
 */
public abstract class BaseTEffortType extends TpBaseObject
{
    /** The Peer class */
    private static final TEffortTypePeer peer =
        new TEffortTypePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the effortUnit field */
    private Integer effortUnit;

    /** The value for the description field */
    private String description;

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



        // update associated TCost
        if (collTCosts != null)
        {
            for (int i = 0; i < collTCosts.size(); i++)
            {
                ((TCost) collTCosts.get(i))
                        .setEfforttype(v);
            }
        }
    }

    /**
     * Get the Label
     *
     * @return String
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set the value of Label
     *
     * @param v new value
     */
    public void setLabel(String v) 
    {

        if (!ObjectUtils.equals(this.label, v))
        {
            this.label = v;
            setModified(true);
        }


    }

    /**
     * Get the EffortUnit
     *
     * @return Integer
     */
    public Integer getEffortUnit()
    {
        return effortUnit;
    }


    /**
     * Set the value of EffortUnit
     *
     * @param v new value
     */
    public void setEffortUnit(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.effortUnit, v))
        {
            this.effortUnit = v;
            setModified(true);
        }


        if (aTEffortUnit != null && !ObjectUtils.equals(aTEffortUnit.getObjectID(), v))
        {
            aTEffortUnit = null;
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

    



    private TEffortUnit aTEffortUnit;

    /**
     * Declares an association between this object and a TEffortUnit object
     *
     * @param v TEffortUnit
     * @throws TorqueException
     */
    public void setTEffortUnit(TEffortUnit v) throws TorqueException
    {
        if (v == null)
        {
            setEffortUnit((Integer) null);
        }
        else
        {
            setEffortUnit(v.getObjectID());
        }
        aTEffortUnit = v;
    }


    /**
     * Returns the associated TEffortUnit object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TEffortUnit object
     * @throws TorqueException
     */
    public TEffortUnit getTEffortUnit()
        throws TorqueException
    {
        if (aTEffortUnit == null && (!ObjectUtils.equals(this.effortUnit, null)))
        {
            aTEffortUnit = TEffortUnitPeer.retrieveByPK(SimpleKey.keyFor(this.effortUnit));
        }
        return aTEffortUnit;
    }

    /**
     * Return the associated TEffortUnit object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TEffortUnit object
     * @throws TorqueException
     */
    public TEffortUnit getTEffortUnit(Connection connection)
        throws TorqueException
    {
        if (aTEffortUnit == null && (!ObjectUtils.equals(this.effortUnit, null)))
        {
            aTEffortUnit = TEffortUnitPeer.retrieveByPK(SimpleKey.keyFor(this.effortUnit), connection);
        }
        return aTEffortUnit;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTEffortUnitKey(ObjectKey key) throws TorqueException
    {

        setEffortUnit(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTCosts
     */
    protected List<TCost> collTCosts;

    /**
     * Temporary storage of collTCosts to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTCosts()
    {
        if (collTCosts == null)
        {
            collTCosts = new ArrayList<TCost>();
        }
    }


    /**
     * Method called to associate a TCost object to this object
     * through the TCost foreign key attribute
     *
     * @param l TCost
     * @throws TorqueException
     */
    public void addTCost(TCost l) throws TorqueException
    {
        getTCosts().add(l);
        l.setTEffortType((TEffortType) this);
    }

    /**
     * Method called to associate a TCost object to this object
     * through the TCost foreign key attribute using connection.
     *
     * @param l TCost
     * @throws TorqueException
     */
    public void addTCost(TCost l, Connection con) throws TorqueException
    {
        getTCosts(con).add(l);
        l.setTEffortType((TEffortType) this);
    }

    /**
     * The criteria used to select the current contents of collTCosts
     */
    private Criteria lastTCostsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCosts(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TCost> getTCosts()
        throws TorqueException
    {
        if (collTCosts == null)
        {
            collTCosts = getTCosts(new Criteria(10));
        }
        return collTCosts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEffortType has previously
     * been saved, it will retrieve related TCosts from storage.
     * If this TEffortType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TCost> getTCosts(Criteria criteria) throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.EFFORTTYPE, getObjectID() );
                collTCosts = TCostPeer.doSelect(criteria);
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
                criteria.add(TCostPeer.EFFORTTYPE, getObjectID());
                if (!lastTCostsCriteria.equals(criteria))
                {
                    collTCosts = TCostPeer.doSelect(criteria);
                }
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCosts(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCost> getTCosts(Connection con) throws TorqueException
    {
        if (collTCosts == null)
        {
            collTCosts = getTCosts(new Criteria(10), con);
        }
        return collTCosts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEffortType has previously
     * been saved, it will retrieve related TCosts from storage.
     * If this TEffortType is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCost> getTCosts(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                 criteria.add(TCostPeer.EFFORTTYPE, getObjectID());
                 collTCosts = TCostPeer.doSelect(criteria, con);
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
                 criteria.add(TCostPeer.EFFORTTYPE, getObjectID());
                 if (!lastTCostsCriteria.equals(criteria))
                 {
                     collTCosts = TCostPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTCostsCriteria = criteria;

         return collTCosts;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEffortType is new, it will return
     * an empty collection; or if this TEffortType has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TEffortType.
     */
    protected List<TCost> getTCostsJoinTAccount(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.EFFORTTYPE, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTAccount(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.EFFORTTYPE, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTAccount(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEffortType is new, it will return
     * an empty collection; or if this TEffortType has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TEffortType.
     */
    protected List<TCost> getTCostsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.EFFORTTYPE, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.EFFORTTYPE, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEffortType is new, it will return
     * an empty collection; or if this TEffortType has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TEffortType.
     */
    protected List<TCost> getTCostsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.EFFORTTYPE, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.EFFORTTYPE, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEffortType is new, it will return
     * an empty collection; or if this TEffortType has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TEffortType.
     */
    protected List<TCost> getTCostsJoinTEffortType(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.EFFORTTYPE, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTEffortType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.EFFORTTYPE, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTEffortType(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
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
            fieldNames.add("Label");
            fieldNames.add("EffortUnit");
            fieldNames.add("Description");
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
        if (name.equals("Label"))
        {
            return getLabel();
        }
        if (name.equals("EffortUnit"))
        {
            return getEffortUnit();
        }
        if (name.equals("Description"))
        {
            return getDescription();
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
        if (name.equals("Label"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLabel((String) value);
            return true;
        }
        if (name.equals("EffortUnit"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEffortUnit((Integer) value);
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
        if (name.equals(TEffortTypePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TEffortTypePeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TEffortTypePeer.EFFORTUNIT))
        {
            return getEffortUnit();
        }
        if (name.equals(TEffortTypePeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TEffortTypePeer.TPUUID))
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
      if (TEffortTypePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TEffortTypePeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TEffortTypePeer.EFFORTUNIT.equals(name))
        {
            return setByName("EffortUnit", value);
        }
      if (TEffortTypePeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TEffortTypePeer.TPUUID.equals(name))
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
            return getLabel();
        }
        if (pos == 2)
        {
            return getEffortUnit();
        }
        if (pos == 3)
        {
            return getDescription();
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
            return setByName("Label", value);
        }
    if (position == 2)
        {
            return setByName("EffortUnit", value);
        }
    if (position == 3)
        {
            return setByName("Description", value);
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
        save(TEffortTypePeer.DATABASE_NAME);
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
                    TEffortTypePeer.doInsert((TEffortType) this, con);
                    setNew(false);
                }
                else
                {
                    TEffortTypePeer.doUpdate((TEffortType) this, con);
                }
            }


            if (collTCosts != null)
            {
                for (int i = 0; i < collTCosts.size(); i++)
                {
                    ((TCost) collTCosts.get(i)).save(con);
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
    public TEffortType copy() throws TorqueException
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
    public TEffortType copy(Connection con) throws TorqueException
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
    public TEffortType copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TEffortType(), deepcopy);
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
    public TEffortType copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TEffortType(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TEffortType copyInto(TEffortType copyObj) throws TorqueException
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
    protected TEffortType copyInto(TEffortType copyObj, Connection con) throws TorqueException
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
    protected TEffortType copyInto(TEffortType copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setEffortUnit(effortUnit);
        copyObj.setDescription(description);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TCost> vTCosts = getTCosts();
        if (vTCosts != null)
        {
            for (int i = 0; i < vTCosts.size(); i++)
            {
                TCost obj =  vTCosts.get(i);
                copyObj.addTCost(obj.copy());
            }
        }
        else
        {
            copyObj.collTCosts = null;
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
    protected TEffortType copyInto(TEffortType copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setEffortUnit(effortUnit);
        copyObj.setDescription(description);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TCost> vTCosts = getTCosts(con);
        if (vTCosts != null)
        {
            for (int i = 0; i < vTCosts.size(); i++)
            {
                TCost obj =  vTCosts.get(i);
                copyObj.addTCost(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTCosts = null;
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
    public TEffortTypePeer getPeer()
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
        return TEffortTypePeer.getTableMap();
    }

  
    /**
     * Creates a TEffortTypeBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TEffortTypeBean with the contents of this object
     */
    public TEffortTypeBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TEffortTypeBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TEffortTypeBean with the contents of this object
     */
    public TEffortTypeBean getBean(IdentityMap createdBeans)
    {
        TEffortTypeBean result = (TEffortTypeBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TEffortTypeBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setEffortUnit(getEffortUnit());
        result.setDescription(getDescription());
        result.setUuid(getUuid());



        if (collTCosts != null)
        {
            List<TCostBean> relatedBeans = new ArrayList<TCostBean>(collTCosts.size());
            for (Iterator<TCost> collTCostsIt = collTCosts.iterator(); collTCostsIt.hasNext(); )
            {
                TCost related = (TCost) collTCostsIt.next();
                TCostBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTCostBeans(relatedBeans);
        }




        if (aTEffortUnit != null)
        {
            TEffortUnitBean relatedBean = aTEffortUnit.getBean(createdBeans);
            result.setTEffortUnitBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TEffortType with the contents
     * of a TEffortTypeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TEffortTypeBean which contents are used to create
     *        the resulting class
     * @return an instance of TEffortType with the contents of bean
     */
    public static TEffortType createTEffortType(TEffortTypeBean bean)
        throws TorqueException
    {
        return createTEffortType(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TEffortType with the contents
     * of a TEffortTypeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TEffortTypeBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TEffortType with the contents of bean
     */

    public static TEffortType createTEffortType(TEffortTypeBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TEffortType result = (TEffortType) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TEffortType();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setEffortUnit(bean.getEffortUnit());
        result.setDescription(bean.getDescription());
        result.setUuid(bean.getUuid());



        {
            List<TCostBean> relatedBeans = bean.getTCostBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TCostBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TCostBean relatedBean =  relatedBeansIt.next();
                    TCost related = TCost.createTCost(relatedBean, createdObjects);
                    result.addTCostFromBean(related);
                }
            }
        }




        {
            TEffortUnitBean relatedBean = bean.getTEffortUnitBean();
            if (relatedBean != null)
            {
                TEffortUnit relatedObject = TEffortUnit.createTEffortUnit(relatedBean, createdObjects);
                result.setTEffortUnit(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TCost object to this object.
     * through the TCost foreign key attribute
     *
     * @param toAdd TCost
     */
    protected void addTCostFromBean(TCost toAdd)
    {
        initTCosts();
        collTCosts.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TEffortType:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("EffortUnit = ")
           .append(getEffortUnit())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
