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
import com.aurel.track.beans.TBasketBean;
import com.aurel.track.beans.TPersonBean;

import com.aurel.track.beans.TPersonBasketBean;


/**
 * WorkItem baskets for persons
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TBasket
 */
public abstract class BaseTBasket extends TpBaseObject
{
    /** The Peer class */
    private static final TBasketPeer peer =
        new TBasketPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the divisible field */
    private String divisible = "N";

    /** The value for the parentBasket field */
    private Integer parentBasket;

    /** The value for the person field */
    private Integer person;

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



        // update associated TPersonBasket
        if (collTPersonBaskets != null)
        {
            for (int i = 0; i < collTPersonBaskets.size(); i++)
            {
                ((TPersonBasket) collTPersonBaskets.get(i))
                        .setBasket(v);
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
     * Get the Divisible
     *
     * @return String
     */
    public String getDivisible()
    {
        return divisible;
    }


    /**
     * Set the value of Divisible
     *
     * @param v new value
     */
    public void setDivisible(String v) 
    {

        if (!ObjectUtils.equals(this.divisible, v))
        {
            this.divisible = v;
            setModified(true);
        }


    }

    /**
     * Get the ParentBasket
     *
     * @return Integer
     */
    public Integer getParentBasket()
    {
        return parentBasket;
    }


    /**
     * Set the value of ParentBasket
     *
     * @param v new value
     */
    public void setParentBasket(Integer v) 
    {

        if (!ObjectUtils.equals(this.parentBasket, v))
        {
            this.parentBasket = v;
            setModified(true);
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
   


    /**
     * Collection to store aggregation of collTPersonBaskets
     */
    protected List<TPersonBasket> collTPersonBaskets;

    /**
     * Temporary storage of collTPersonBaskets to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPersonBaskets()
    {
        if (collTPersonBaskets == null)
        {
            collTPersonBaskets = new ArrayList<TPersonBasket>();
        }
    }


    /**
     * Method called to associate a TPersonBasket object to this object
     * through the TPersonBasket foreign key attribute
     *
     * @param l TPersonBasket
     * @throws TorqueException
     */
    public void addTPersonBasket(TPersonBasket l) throws TorqueException
    {
        getTPersonBaskets().add(l);
        l.setTBasket((TBasket) this);
    }

    /**
     * Method called to associate a TPersonBasket object to this object
     * through the TPersonBasket foreign key attribute using connection.
     *
     * @param l TPersonBasket
     * @throws TorqueException
     */
    public void addTPersonBasket(TPersonBasket l, Connection con) throws TorqueException
    {
        getTPersonBaskets(con).add(l);
        l.setTBasket((TBasket) this);
    }

    /**
     * The criteria used to select the current contents of collTPersonBaskets
     */
    private Criteria lastTPersonBasketsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPersonBaskets(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPersonBasket> getTPersonBaskets()
        throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            collTPersonBaskets = getTPersonBaskets(new Criteria(10));
        }
        return collTPersonBaskets;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBasket has previously
     * been saved, it will retrieve related TPersonBaskets from storage.
     * If this TBasket is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPersonBasket> getTPersonBaskets(Criteria criteria) throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            if (isNew())
            {
               collTPersonBaskets = new ArrayList<TPersonBasket>();
            }
            else
            {
                criteria.add(TPersonBasketPeer.BASKET, getObjectID() );
                collTPersonBaskets = TPersonBasketPeer.doSelect(criteria);
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
                criteria.add(TPersonBasketPeer.BASKET, getObjectID());
                if (!lastTPersonBasketsCriteria.equals(criteria))
                {
                    collTPersonBaskets = TPersonBasketPeer.doSelect(criteria);
                }
            }
        }
        lastTPersonBasketsCriteria = criteria;

        return collTPersonBaskets;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPersonBaskets(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPersonBasket> getTPersonBaskets(Connection con) throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            collTPersonBaskets = getTPersonBaskets(new Criteria(10), con);
        }
        return collTPersonBaskets;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBasket has previously
     * been saved, it will retrieve related TPersonBaskets from storage.
     * If this TBasket is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPersonBasket> getTPersonBaskets(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            if (isNew())
            {
               collTPersonBaskets = new ArrayList<TPersonBasket>();
            }
            else
            {
                 criteria.add(TPersonBasketPeer.BASKET, getObjectID());
                 collTPersonBaskets = TPersonBasketPeer.doSelect(criteria, con);
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
                 criteria.add(TPersonBasketPeer.BASKET, getObjectID());
                 if (!lastTPersonBasketsCriteria.equals(criteria))
                 {
                     collTPersonBaskets = TPersonBasketPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPersonBasketsCriteria = criteria;

         return collTPersonBaskets;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBasket is new, it will return
     * an empty collection; or if this TBasket has previously
     * been saved, it will retrieve related TPersonBaskets from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBasket.
     */
    protected List<TPersonBasket> getTPersonBasketsJoinTBasket(Criteria criteria)
        throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            if (isNew())
            {
               collTPersonBaskets = new ArrayList<TPersonBasket>();
            }
            else
            {
                criteria.add(TPersonBasketPeer.BASKET, getObjectID());
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTBasket(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonBasketPeer.BASKET, getObjectID());
            if (!lastTPersonBasketsCriteria.equals(criteria))
            {
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTBasket(criteria);
            }
        }
        lastTPersonBasketsCriteria = criteria;

        return collTPersonBaskets;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBasket is new, it will return
     * an empty collection; or if this TBasket has previously
     * been saved, it will retrieve related TPersonBaskets from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBasket.
     */
    protected List<TPersonBasket> getTPersonBasketsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            if (isNew())
            {
               collTPersonBaskets = new ArrayList<TPersonBasket>();
            }
            else
            {
                criteria.add(TPersonBasketPeer.BASKET, getObjectID());
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonBasketPeer.BASKET, getObjectID());
            if (!lastTPersonBasketsCriteria.equals(criteria))
            {
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTPersonBasketsCriteria = criteria;

        return collTPersonBaskets;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBasket is new, it will return
     * an empty collection; or if this TBasket has previously
     * been saved, it will retrieve related TPersonBaskets from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBasket.
     */
    protected List<TPersonBasket> getTPersonBasketsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            if (isNew())
            {
               collTPersonBaskets = new ArrayList<TPersonBasket>();
            }
            else
            {
                criteria.add(TPersonBasketPeer.BASKET, getObjectID());
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonBasketPeer.BASKET, getObjectID());
            if (!lastTPersonBasketsCriteria.equals(criteria))
            {
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTPersonBasketsCriteria = criteria;

        return collTPersonBaskets;
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
            fieldNames.add("Divisible");
            fieldNames.add("ParentBasket");
            fieldNames.add("Person");
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
        if (name.equals("Divisible"))
        {
            return getDivisible();
        }
        if (name.equals("ParentBasket"))
        {
            return getParentBasket();
        }
        if (name.equals("Person"))
        {
            return getPerson();
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
        if (name.equals("Divisible"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDivisible((String) value);
            return true;
        }
        if (name.equals("ParentBasket"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParentBasket((Integer) value);
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
        if (name.equals(TBasketPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TBasketPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TBasketPeer.DIVISIBLE))
        {
            return getDivisible();
        }
        if (name.equals(TBasketPeer.PARENTBASKET))
        {
            return getParentBasket();
        }
        if (name.equals(TBasketPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TBasketPeer.TPUUID))
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
      if (TBasketPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TBasketPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TBasketPeer.DIVISIBLE.equals(name))
        {
            return setByName("Divisible", value);
        }
      if (TBasketPeer.PARENTBASKET.equals(name))
        {
            return setByName("ParentBasket", value);
        }
      if (TBasketPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TBasketPeer.TPUUID.equals(name))
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
            return getDivisible();
        }
        if (pos == 3)
        {
            return getParentBasket();
        }
        if (pos == 4)
        {
            return getPerson();
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
            return setByName("Label", value);
        }
    if (position == 2)
        {
            return setByName("Divisible", value);
        }
    if (position == 3)
        {
            return setByName("ParentBasket", value);
        }
    if (position == 4)
        {
            return setByName("Person", value);
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
        save(TBasketPeer.DATABASE_NAME);
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
                    TBasketPeer.doInsert((TBasket) this, con);
                    setNew(false);
                }
                else
                {
                    TBasketPeer.doUpdate((TBasket) this, con);
                }
            }


            if (collTPersonBaskets != null)
            {
                for (int i = 0; i < collTPersonBaskets.size(); i++)
                {
                    ((TPersonBasket) collTPersonBaskets.get(i)).save(con);
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
    public TBasket copy() throws TorqueException
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
    public TBasket copy(Connection con) throws TorqueException
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
    public TBasket copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TBasket(), deepcopy);
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
    public TBasket copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TBasket(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TBasket copyInto(TBasket copyObj) throws TorqueException
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
    protected TBasket copyInto(TBasket copyObj, Connection con) throws TorqueException
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
    protected TBasket copyInto(TBasket copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setDivisible(divisible);
        copyObj.setParentBasket(parentBasket);
        copyObj.setPerson(person);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TPersonBasket> vTPersonBaskets = getTPersonBaskets();
        if (vTPersonBaskets != null)
        {
            for (int i = 0; i < vTPersonBaskets.size(); i++)
            {
                TPersonBasket obj =  vTPersonBaskets.get(i);
                copyObj.addTPersonBasket(obj.copy());
            }
        }
        else
        {
            copyObj.collTPersonBaskets = null;
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
    protected TBasket copyInto(TBasket copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setDivisible(divisible);
        copyObj.setParentBasket(parentBasket);
        copyObj.setPerson(person);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TPersonBasket> vTPersonBaskets = getTPersonBaskets(con);
        if (vTPersonBaskets != null)
        {
            for (int i = 0; i < vTPersonBaskets.size(); i++)
            {
                TPersonBasket obj =  vTPersonBaskets.get(i);
                copyObj.addTPersonBasket(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPersonBaskets = null;
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
    public TBasketPeer getPeer()
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
        return TBasketPeer.getTableMap();
    }

  
    /**
     * Creates a TBasketBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TBasketBean with the contents of this object
     */
    public TBasketBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TBasketBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TBasketBean with the contents of this object
     */
    public TBasketBean getBean(IdentityMap createdBeans)
    {
        TBasketBean result = (TBasketBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TBasketBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setDivisible(getDivisible());
        result.setParentBasket(getParentBasket());
        result.setPerson(getPerson());
        result.setUuid(getUuid());



        if (collTPersonBaskets != null)
        {
            List<TPersonBasketBean> relatedBeans = new ArrayList<TPersonBasketBean>(collTPersonBaskets.size());
            for (Iterator<TPersonBasket> collTPersonBasketsIt = collTPersonBaskets.iterator(); collTPersonBasketsIt.hasNext(); )
            {
                TPersonBasket related = (TPersonBasket) collTPersonBasketsIt.next();
                TPersonBasketBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPersonBasketBeans(relatedBeans);
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
     * Creates an instance of TBasket with the contents
     * of a TBasketBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TBasketBean which contents are used to create
     *        the resulting class
     * @return an instance of TBasket with the contents of bean
     */
    public static TBasket createTBasket(TBasketBean bean)
        throws TorqueException
    {
        return createTBasket(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TBasket with the contents
     * of a TBasketBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TBasketBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TBasket with the contents of bean
     */

    public static TBasket createTBasket(TBasketBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TBasket result = (TBasket) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TBasket();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setDivisible(bean.getDivisible());
        result.setParentBasket(bean.getParentBasket());
        result.setPerson(bean.getPerson());
        result.setUuid(bean.getUuid());



        {
            List<TPersonBasketBean> relatedBeans = bean.getTPersonBasketBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPersonBasketBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPersonBasketBean relatedBean =  relatedBeansIt.next();
                    TPersonBasket related = TPersonBasket.createTPersonBasket(relatedBean, createdObjects);
                    result.addTPersonBasketFromBean(related);
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
     * Method called to associate a TPersonBasket object to this object.
     * through the TPersonBasket foreign key attribute
     *
     * @param toAdd TPersonBasket
     */
    protected void addTPersonBasketFromBean(TPersonBasket toAdd)
    {
        initTPersonBaskets();
        collTPersonBaskets.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TBasket:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Divisible = ")
           .append(getDivisible())
           .append("\n");
        str.append("ParentBasket = ")
           .append(getParentBasket())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
