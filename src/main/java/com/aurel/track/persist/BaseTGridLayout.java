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
import com.aurel.track.beans.TGridLayoutBean;
import com.aurel.track.beans.TPersonBean;

import com.aurel.track.beans.TGridFieldBean;
import com.aurel.track.beans.TGridGroupingSortingBean;


/**
 * layout for grids
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TGridLayout
 */
public abstract class BaseTGridLayout extends TpBaseObject
{
    /** The Peer class */
    private static final TGridLayoutPeer peer =
        new TGridLayoutPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the person field */
    private Integer person;

    /** The value for the layoutKey field */
    private Integer layoutKey;

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



        // update associated TGridField
        if (collTGridFields != null)
        {
            for (int i = 0; i < collTGridFields.size(); i++)
            {
                ((TGridField) collTGridFields.get(i))
                        .setGridLayout(v);
            }
        }

        // update associated TGridGroupingSorting
        if (collTGridGroupingSortings != null)
        {
            for (int i = 0; i < collTGridGroupingSortings.size(); i++)
            {
                ((TGridGroupingSorting) collTGridGroupingSortings.get(i))
                        .setGridLayout(v);
            }
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
     * Get the LayoutKey
     *
     * @return Integer
     */
    public Integer getLayoutKey()
    {
        return layoutKey;
    }


    /**
     * Set the value of LayoutKey
     *
     * @param v new value
     */
    public void setLayoutKey(Integer v) 
    {

        if (!ObjectUtils.equals(this.layoutKey, v))
        {
            this.layoutKey = v;
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
     * Collection to store aggregation of collTGridFields
     */
    protected List<TGridField> collTGridFields;

    /**
     * Temporary storage of collTGridFields to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTGridFields()
    {
        if (collTGridFields == null)
        {
            collTGridFields = new ArrayList<TGridField>();
        }
    }


    /**
     * Method called to associate a TGridField object to this object
     * through the TGridField foreign key attribute
     *
     * @param l TGridField
     * @throws TorqueException
     */
    public void addTGridField(TGridField l) throws TorqueException
    {
        getTGridFields().add(l);
        l.setTGridLayout((TGridLayout) this);
    }

    /**
     * Method called to associate a TGridField object to this object
     * through the TGridField foreign key attribute using connection.
     *
     * @param l TGridField
     * @throws TorqueException
     */
    public void addTGridField(TGridField l, Connection con) throws TorqueException
    {
        getTGridFields(con).add(l);
        l.setTGridLayout((TGridLayout) this);
    }

    /**
     * The criteria used to select the current contents of collTGridFields
     */
    private Criteria lastTGridFieldsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTGridFields(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TGridField> getTGridFields()
        throws TorqueException
    {
        if (collTGridFields == null)
        {
            collTGridFields = getTGridFields(new Criteria(10));
        }
        return collTGridFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TGridLayout has previously
     * been saved, it will retrieve related TGridFields from storage.
     * If this TGridLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TGridField> getTGridFields(Criteria criteria) throws TorqueException
    {
        if (collTGridFields == null)
        {
            if (isNew())
            {
               collTGridFields = new ArrayList<TGridField>();
            }
            else
            {
                criteria.add(TGridFieldPeer.GRIDLAYOUT, getObjectID() );
                collTGridFields = TGridFieldPeer.doSelect(criteria);
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
                criteria.add(TGridFieldPeer.GRIDLAYOUT, getObjectID());
                if (!lastTGridFieldsCriteria.equals(criteria))
                {
                    collTGridFields = TGridFieldPeer.doSelect(criteria);
                }
            }
        }
        lastTGridFieldsCriteria = criteria;

        return collTGridFields;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTGridFields(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TGridField> getTGridFields(Connection con) throws TorqueException
    {
        if (collTGridFields == null)
        {
            collTGridFields = getTGridFields(new Criteria(10), con);
        }
        return collTGridFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TGridLayout has previously
     * been saved, it will retrieve related TGridFields from storage.
     * If this TGridLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TGridField> getTGridFields(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTGridFields == null)
        {
            if (isNew())
            {
               collTGridFields = new ArrayList<TGridField>();
            }
            else
            {
                 criteria.add(TGridFieldPeer.GRIDLAYOUT, getObjectID());
                 collTGridFields = TGridFieldPeer.doSelect(criteria, con);
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
                 criteria.add(TGridFieldPeer.GRIDLAYOUT, getObjectID());
                 if (!lastTGridFieldsCriteria.equals(criteria))
                 {
                     collTGridFields = TGridFieldPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTGridFieldsCriteria = criteria;

         return collTGridFields;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TGridLayout is new, it will return
     * an empty collection; or if this TGridLayout has previously
     * been saved, it will retrieve related TGridFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TGridLayout.
     */
    protected List<TGridField> getTGridFieldsJoinTGridLayout(Criteria criteria)
        throws TorqueException
    {
        if (collTGridFields == null)
        {
            if (isNew())
            {
               collTGridFields = new ArrayList<TGridField>();
            }
            else
            {
                criteria.add(TGridFieldPeer.GRIDLAYOUT, getObjectID());
                collTGridFields = TGridFieldPeer.doSelectJoinTGridLayout(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TGridFieldPeer.GRIDLAYOUT, getObjectID());
            if (!lastTGridFieldsCriteria.equals(criteria))
            {
                collTGridFields = TGridFieldPeer.doSelectJoinTGridLayout(criteria);
            }
        }
        lastTGridFieldsCriteria = criteria;

        return collTGridFields;
    }





    /**
     * Collection to store aggregation of collTGridGroupingSortings
     */
    protected List<TGridGroupingSorting> collTGridGroupingSortings;

    /**
     * Temporary storage of collTGridGroupingSortings to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTGridGroupingSortings()
    {
        if (collTGridGroupingSortings == null)
        {
            collTGridGroupingSortings = new ArrayList<TGridGroupingSorting>();
        }
    }


    /**
     * Method called to associate a TGridGroupingSorting object to this object
     * through the TGridGroupingSorting foreign key attribute
     *
     * @param l TGridGroupingSorting
     * @throws TorqueException
     */
    public void addTGridGroupingSorting(TGridGroupingSorting l) throws TorqueException
    {
        getTGridGroupingSortings().add(l);
        l.setTGridLayout((TGridLayout) this);
    }

    /**
     * Method called to associate a TGridGroupingSorting object to this object
     * through the TGridGroupingSorting foreign key attribute using connection.
     *
     * @param l TGridGroupingSorting
     * @throws TorqueException
     */
    public void addTGridGroupingSorting(TGridGroupingSorting l, Connection con) throws TorqueException
    {
        getTGridGroupingSortings(con).add(l);
        l.setTGridLayout((TGridLayout) this);
    }

    /**
     * The criteria used to select the current contents of collTGridGroupingSortings
     */
    private Criteria lastTGridGroupingSortingsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTGridGroupingSortings(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TGridGroupingSorting> getTGridGroupingSortings()
        throws TorqueException
    {
        if (collTGridGroupingSortings == null)
        {
            collTGridGroupingSortings = getTGridGroupingSortings(new Criteria(10));
        }
        return collTGridGroupingSortings;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TGridLayout has previously
     * been saved, it will retrieve related TGridGroupingSortings from storage.
     * If this TGridLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TGridGroupingSorting> getTGridGroupingSortings(Criteria criteria) throws TorqueException
    {
        if (collTGridGroupingSortings == null)
        {
            if (isNew())
            {
               collTGridGroupingSortings = new ArrayList<TGridGroupingSorting>();
            }
            else
            {
                criteria.add(TGridGroupingSortingPeer.GRIDLAYOUT, getObjectID() );
                collTGridGroupingSortings = TGridGroupingSortingPeer.doSelect(criteria);
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
                criteria.add(TGridGroupingSortingPeer.GRIDLAYOUT, getObjectID());
                if (!lastTGridGroupingSortingsCriteria.equals(criteria))
                {
                    collTGridGroupingSortings = TGridGroupingSortingPeer.doSelect(criteria);
                }
            }
        }
        lastTGridGroupingSortingsCriteria = criteria;

        return collTGridGroupingSortings;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTGridGroupingSortings(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TGridGroupingSorting> getTGridGroupingSortings(Connection con) throws TorqueException
    {
        if (collTGridGroupingSortings == null)
        {
            collTGridGroupingSortings = getTGridGroupingSortings(new Criteria(10), con);
        }
        return collTGridGroupingSortings;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TGridLayout has previously
     * been saved, it will retrieve related TGridGroupingSortings from storage.
     * If this TGridLayout is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TGridGroupingSorting> getTGridGroupingSortings(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTGridGroupingSortings == null)
        {
            if (isNew())
            {
               collTGridGroupingSortings = new ArrayList<TGridGroupingSorting>();
            }
            else
            {
                 criteria.add(TGridGroupingSortingPeer.GRIDLAYOUT, getObjectID());
                 collTGridGroupingSortings = TGridGroupingSortingPeer.doSelect(criteria, con);
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
                 criteria.add(TGridGroupingSortingPeer.GRIDLAYOUT, getObjectID());
                 if (!lastTGridGroupingSortingsCriteria.equals(criteria))
                 {
                     collTGridGroupingSortings = TGridGroupingSortingPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTGridGroupingSortingsCriteria = criteria;

         return collTGridGroupingSortings;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TGridLayout is new, it will return
     * an empty collection; or if this TGridLayout has previously
     * been saved, it will retrieve related TGridGroupingSortings from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TGridLayout.
     */
    protected List<TGridGroupingSorting> getTGridGroupingSortingsJoinTGridLayout(Criteria criteria)
        throws TorqueException
    {
        if (collTGridGroupingSortings == null)
        {
            if (isNew())
            {
               collTGridGroupingSortings = new ArrayList<TGridGroupingSorting>();
            }
            else
            {
                criteria.add(TGridGroupingSortingPeer.GRIDLAYOUT, getObjectID());
                collTGridGroupingSortings = TGridGroupingSortingPeer.doSelectJoinTGridLayout(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TGridGroupingSortingPeer.GRIDLAYOUT, getObjectID());
            if (!lastTGridGroupingSortingsCriteria.equals(criteria))
            {
                collTGridGroupingSortings = TGridGroupingSortingPeer.doSelectJoinTGridLayout(criteria);
            }
        }
        lastTGridGroupingSortingsCriteria = criteria;

        return collTGridGroupingSortings;
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
            fieldNames.add("Person");
            fieldNames.add("LayoutKey");
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
        if (name.equals("Person"))
        {
            return getPerson();
        }
        if (name.equals("LayoutKey"))
        {
            return getLayoutKey();
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
        if (name.equals("LayoutKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLayoutKey((Integer) value);
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
        if (name.equals(TGridLayoutPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TGridLayoutPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TGridLayoutPeer.LAYOUTKEY))
        {
            return getLayoutKey();
        }
        if (name.equals(TGridLayoutPeer.TPUUID))
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
      if (TGridLayoutPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TGridLayoutPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TGridLayoutPeer.LAYOUTKEY.equals(name))
        {
            return setByName("LayoutKey", value);
        }
      if (TGridLayoutPeer.TPUUID.equals(name))
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
            return getPerson();
        }
        if (pos == 2)
        {
            return getLayoutKey();
        }
        if (pos == 3)
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
            return setByName("Person", value);
        }
    if (position == 2)
        {
            return setByName("LayoutKey", value);
        }
    if (position == 3)
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
        save(TGridLayoutPeer.DATABASE_NAME);
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
                    TGridLayoutPeer.doInsert((TGridLayout) this, con);
                    setNew(false);
                }
                else
                {
                    TGridLayoutPeer.doUpdate((TGridLayout) this, con);
                }
            }


            if (collTGridFields != null)
            {
                for (int i = 0; i < collTGridFields.size(); i++)
                {
                    ((TGridField) collTGridFields.get(i)).save(con);
                }
            }

            if (collTGridGroupingSortings != null)
            {
                for (int i = 0; i < collTGridGroupingSortings.size(); i++)
                {
                    ((TGridGroupingSorting) collTGridGroupingSortings.get(i)).save(con);
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
    public TGridLayout copy() throws TorqueException
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
    public TGridLayout copy(Connection con) throws TorqueException
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
    public TGridLayout copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TGridLayout(), deepcopy);
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
    public TGridLayout copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TGridLayout(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TGridLayout copyInto(TGridLayout copyObj) throws TorqueException
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
    protected TGridLayout copyInto(TGridLayout copyObj, Connection con) throws TorqueException
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
    protected TGridLayout copyInto(TGridLayout copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setLayoutKey(layoutKey);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TGridField> vTGridFields = getTGridFields();
        if (vTGridFields != null)
        {
            for (int i = 0; i < vTGridFields.size(); i++)
            {
                TGridField obj =  vTGridFields.get(i);
                copyObj.addTGridField(obj.copy());
            }
        }
        else
        {
            copyObj.collTGridFields = null;
        }


        List<TGridGroupingSorting> vTGridGroupingSortings = getTGridGroupingSortings();
        if (vTGridGroupingSortings != null)
        {
            for (int i = 0; i < vTGridGroupingSortings.size(); i++)
            {
                TGridGroupingSorting obj =  vTGridGroupingSortings.get(i);
                copyObj.addTGridGroupingSorting(obj.copy());
            }
        }
        else
        {
            copyObj.collTGridGroupingSortings = null;
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
    protected TGridLayout copyInto(TGridLayout copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setLayoutKey(layoutKey);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TGridField> vTGridFields = getTGridFields(con);
        if (vTGridFields != null)
        {
            for (int i = 0; i < vTGridFields.size(); i++)
            {
                TGridField obj =  vTGridFields.get(i);
                copyObj.addTGridField(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTGridFields = null;
        }


        List<TGridGroupingSorting> vTGridGroupingSortings = getTGridGroupingSortings(con);
        if (vTGridGroupingSortings != null)
        {
            for (int i = 0; i < vTGridGroupingSortings.size(); i++)
            {
                TGridGroupingSorting obj =  vTGridGroupingSortings.get(i);
                copyObj.addTGridGroupingSorting(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTGridGroupingSortings = null;
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
    public TGridLayoutPeer getPeer()
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
        return TGridLayoutPeer.getTableMap();
    }

  
    /**
     * Creates a TGridLayoutBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TGridLayoutBean with the contents of this object
     */
    public TGridLayoutBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TGridLayoutBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TGridLayoutBean with the contents of this object
     */
    public TGridLayoutBean getBean(IdentityMap createdBeans)
    {
        TGridLayoutBean result = (TGridLayoutBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TGridLayoutBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setPerson(getPerson());
        result.setLayoutKey(getLayoutKey());
        result.setUuid(getUuid());



        if (collTGridFields != null)
        {
            List<TGridFieldBean> relatedBeans = new ArrayList<TGridFieldBean>(collTGridFields.size());
            for (Iterator<TGridField> collTGridFieldsIt = collTGridFields.iterator(); collTGridFieldsIt.hasNext(); )
            {
                TGridField related = (TGridField) collTGridFieldsIt.next();
                TGridFieldBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTGridFieldBeans(relatedBeans);
        }


        if (collTGridGroupingSortings != null)
        {
            List<TGridGroupingSortingBean> relatedBeans = new ArrayList<TGridGroupingSortingBean>(collTGridGroupingSortings.size());
            for (Iterator<TGridGroupingSorting> collTGridGroupingSortingsIt = collTGridGroupingSortings.iterator(); collTGridGroupingSortingsIt.hasNext(); )
            {
                TGridGroupingSorting related = (TGridGroupingSorting) collTGridGroupingSortingsIt.next();
                TGridGroupingSortingBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTGridGroupingSortingBeans(relatedBeans);
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
     * Creates an instance of TGridLayout with the contents
     * of a TGridLayoutBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TGridLayoutBean which contents are used to create
     *        the resulting class
     * @return an instance of TGridLayout with the contents of bean
     */
    public static TGridLayout createTGridLayout(TGridLayoutBean bean)
        throws TorqueException
    {
        return createTGridLayout(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TGridLayout with the contents
     * of a TGridLayoutBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TGridLayoutBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TGridLayout with the contents of bean
     */

    public static TGridLayout createTGridLayout(TGridLayoutBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TGridLayout result = (TGridLayout) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TGridLayout();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setPerson(bean.getPerson());
        result.setLayoutKey(bean.getLayoutKey());
        result.setUuid(bean.getUuid());



        {
            List<TGridFieldBean> relatedBeans = bean.getTGridFieldBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TGridFieldBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TGridFieldBean relatedBean =  relatedBeansIt.next();
                    TGridField related = TGridField.createTGridField(relatedBean, createdObjects);
                    result.addTGridFieldFromBean(related);
                }
            }
        }


        {
            List<TGridGroupingSortingBean> relatedBeans = bean.getTGridGroupingSortingBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TGridGroupingSortingBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TGridGroupingSortingBean relatedBean =  relatedBeansIt.next();
                    TGridGroupingSorting related = TGridGroupingSorting.createTGridGroupingSorting(relatedBean, createdObjects);
                    result.addTGridGroupingSortingFromBean(related);
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
     * Method called to associate a TGridField object to this object.
     * through the TGridField foreign key attribute
     *
     * @param toAdd TGridField
     */
    protected void addTGridFieldFromBean(TGridField toAdd)
    {
        initTGridFields();
        collTGridFields.add(toAdd);
    }


    /**
     * Method called to associate a TGridGroupingSorting object to this object.
     * through the TGridGroupingSorting foreign key attribute
     *
     * @param toAdd TGridGroupingSorting
     */
    protected void addTGridGroupingSortingFromBean(TGridGroupingSorting toAdd)
    {
        initTGridGroupingSortings();
        collTGridGroupingSortings.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TGridLayout:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("LayoutKey = ")
           .append(getLayoutKey())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
