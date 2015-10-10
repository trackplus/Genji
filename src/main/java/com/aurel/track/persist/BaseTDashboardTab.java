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



import com.aurel.track.persist.TDashboardScreen;
import com.aurel.track.persist.TDashboardScreenPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TDashboardTabBean;
import com.aurel.track.beans.TDashboardScreenBean;

import com.aurel.track.beans.TDashboardPanelBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TDashboardTab
 */
public abstract class BaseTDashboardTab extends TpBaseObject
{
    /** The Peer class */
    private static final TDashboardTabPeer peer =
        new TDashboardTabPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the label field */
    private String label;

    /** The value for the description field */
    private String description;

    /** The value for the index field */
    private Integer index;

    /** The value for the parent field */
    private Integer parent;

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



        // update associated TDashboardPanel
        if (collTDashboardPanels != null)
        {
            for (int i = 0; i < collTDashboardPanels.size(); i++)
            {
                ((TDashboardPanel) collTDashboardPanels.get(i))
                        .setParent(v);
            }
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
     * Get the Index
     *
     * @return Integer
     */
    public Integer getIndex()
    {
        return index;
    }


    /**
     * Set the value of Index
     *
     * @param v new value
     */
    public void setIndex(Integer v) 
    {

        if (!ObjectUtils.equals(this.index, v))
        {
            this.index = v;
            setModified(true);
        }


    }

    /**
     * Get the Parent
     *
     * @return Integer
     */
    public Integer getParent()
    {
        return parent;
    }


    /**
     * Set the value of Parent
     *
     * @param v new value
     */
    public void setParent(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.parent, v))
        {
            this.parent = v;
            setModified(true);
        }


        if (aTDashboardScreen != null && !ObjectUtils.equals(aTDashboardScreen.getObjectID(), v))
        {
            aTDashboardScreen = null;
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

    



    private TDashboardScreen aTDashboardScreen;

    /**
     * Declares an association between this object and a TDashboardScreen object
     *
     * @param v TDashboardScreen
     * @throws TorqueException
     */
    public void setTDashboardScreen(TDashboardScreen v) throws TorqueException
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTDashboardScreen = v;
    }


    /**
     * Returns the associated TDashboardScreen object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TDashboardScreen object
     * @throws TorqueException
     */
    public TDashboardScreen getTDashboardScreen()
        throws TorqueException
    {
        if (aTDashboardScreen == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTDashboardScreen = TDashboardScreenPeer.retrieveByPK(SimpleKey.keyFor(this.parent));
        }
        return aTDashboardScreen;
    }

    /**
     * Return the associated TDashboardScreen object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TDashboardScreen object
     * @throws TorqueException
     */
    public TDashboardScreen getTDashboardScreen(Connection connection)
        throws TorqueException
    {
        if (aTDashboardScreen == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTDashboardScreen = TDashboardScreenPeer.retrieveByPK(SimpleKey.keyFor(this.parent), connection);
        }
        return aTDashboardScreen;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTDashboardScreenKey(ObjectKey key) throws TorqueException
    {

        setParent(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTDashboardPanels
     */
    protected List<TDashboardPanel> collTDashboardPanels;

    /**
     * Temporary storage of collTDashboardPanels to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTDashboardPanels()
    {
        if (collTDashboardPanels == null)
        {
            collTDashboardPanels = new ArrayList<TDashboardPanel>();
        }
    }


    /**
     * Method called to associate a TDashboardPanel object to this object
     * through the TDashboardPanel foreign key attribute
     *
     * @param l TDashboardPanel
     * @throws TorqueException
     */
    public void addTDashboardPanel(TDashboardPanel l) throws TorqueException
    {
        getTDashboardPanels().add(l);
        l.setTDashboardTab((TDashboardTab) this);
    }

    /**
     * Method called to associate a TDashboardPanel object to this object
     * through the TDashboardPanel foreign key attribute using connection.
     *
     * @param l TDashboardPanel
     * @throws TorqueException
     */
    public void addTDashboardPanel(TDashboardPanel l, Connection con) throws TorqueException
    {
        getTDashboardPanels(con).add(l);
        l.setTDashboardTab((TDashboardTab) this);
    }

    /**
     * The criteria used to select the current contents of collTDashboardPanels
     */
    private Criteria lastTDashboardPanelsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDashboardPanels(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TDashboardPanel> getTDashboardPanels()
        throws TorqueException
    {
        if (collTDashboardPanels == null)
        {
            collTDashboardPanels = getTDashboardPanels(new Criteria(10));
        }
        return collTDashboardPanels;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardTab has previously
     * been saved, it will retrieve related TDashboardPanels from storage.
     * If this TDashboardTab is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TDashboardPanel> getTDashboardPanels(Criteria criteria) throws TorqueException
    {
        if (collTDashboardPanels == null)
        {
            if (isNew())
            {
               collTDashboardPanels = new ArrayList<TDashboardPanel>();
            }
            else
            {
                criteria.add(TDashboardPanelPeer.PARENT, getObjectID() );
                collTDashboardPanels = TDashboardPanelPeer.doSelect(criteria);
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
                criteria.add(TDashboardPanelPeer.PARENT, getObjectID());
                if (!lastTDashboardPanelsCriteria.equals(criteria))
                {
                    collTDashboardPanels = TDashboardPanelPeer.doSelect(criteria);
                }
            }
        }
        lastTDashboardPanelsCriteria = criteria;

        return collTDashboardPanels;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDashboardPanels(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDashboardPanel> getTDashboardPanels(Connection con) throws TorqueException
    {
        if (collTDashboardPanels == null)
        {
            collTDashboardPanels = getTDashboardPanels(new Criteria(10), con);
        }
        return collTDashboardPanels;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardTab has previously
     * been saved, it will retrieve related TDashboardPanels from storage.
     * If this TDashboardTab is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDashboardPanel> getTDashboardPanels(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTDashboardPanels == null)
        {
            if (isNew())
            {
               collTDashboardPanels = new ArrayList<TDashboardPanel>();
            }
            else
            {
                 criteria.add(TDashboardPanelPeer.PARENT, getObjectID());
                 collTDashboardPanels = TDashboardPanelPeer.doSelect(criteria, con);
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
                 criteria.add(TDashboardPanelPeer.PARENT, getObjectID());
                 if (!lastTDashboardPanelsCriteria.equals(criteria))
                 {
                     collTDashboardPanels = TDashboardPanelPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTDashboardPanelsCriteria = criteria;

         return collTDashboardPanels;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardTab is new, it will return
     * an empty collection; or if this TDashboardTab has previously
     * been saved, it will retrieve related TDashboardPanels from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDashboardTab.
     */
    protected List<TDashboardPanel> getTDashboardPanelsJoinTDashboardTab(Criteria criteria)
        throws TorqueException
    {
        if (collTDashboardPanels == null)
        {
            if (isNew())
            {
               collTDashboardPanels = new ArrayList<TDashboardPanel>();
            }
            else
            {
                criteria.add(TDashboardPanelPeer.PARENT, getObjectID());
                collTDashboardPanels = TDashboardPanelPeer.doSelectJoinTDashboardTab(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDashboardPanelPeer.PARENT, getObjectID());
            if (!lastTDashboardPanelsCriteria.equals(criteria))
            {
                collTDashboardPanels = TDashboardPanelPeer.doSelectJoinTDashboardTab(criteria);
            }
        }
        lastTDashboardPanelsCriteria = criteria;

        return collTDashboardPanels;
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
            fieldNames.add("Name");
            fieldNames.add("Label");
            fieldNames.add("Description");
            fieldNames.add("Index");
            fieldNames.add("Parent");
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
        if (name.equals("Name"))
        {
            return getName();
        }
        if (name.equals("Label"))
        {
            return getLabel();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("Index"))
        {
            return getIndex();
        }
        if (name.equals("Parent"))
        {
            return getParent();
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
        if (name.equals("Index"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIndex((Integer) value);
            return true;
        }
        if (name.equals("Parent"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParent((Integer) value);
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
        if (name.equals(TDashboardTabPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TDashboardTabPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TDashboardTabPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TDashboardTabPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TDashboardTabPeer.SORTORDER))
        {
            return getIndex();
        }
        if (name.equals(TDashboardTabPeer.PARENT))
        {
            return getParent();
        }
        if (name.equals(TDashboardTabPeer.TPUUID))
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
      if (TDashboardTabPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TDashboardTabPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TDashboardTabPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TDashboardTabPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TDashboardTabPeer.SORTORDER.equals(name))
        {
            return setByName("Index", value);
        }
      if (TDashboardTabPeer.PARENT.equals(name))
        {
            return setByName("Parent", value);
        }
      if (TDashboardTabPeer.TPUUID.equals(name))
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
            return getName();
        }
        if (pos == 2)
        {
            return getLabel();
        }
        if (pos == 3)
        {
            return getDescription();
        }
        if (pos == 4)
        {
            return getIndex();
        }
        if (pos == 5)
        {
            return getParent();
        }
        if (pos == 6)
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
            return setByName("Name", value);
        }
    if (position == 2)
        {
            return setByName("Label", value);
        }
    if (position == 3)
        {
            return setByName("Description", value);
        }
    if (position == 4)
        {
            return setByName("Index", value);
        }
    if (position == 5)
        {
            return setByName("Parent", value);
        }
    if (position == 6)
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
        save(TDashboardTabPeer.DATABASE_NAME);
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
                    TDashboardTabPeer.doInsert((TDashboardTab) this, con);
                    setNew(false);
                }
                else
                {
                    TDashboardTabPeer.doUpdate((TDashboardTab) this, con);
                }
            }


            if (collTDashboardPanels != null)
            {
                for (int i = 0; i < collTDashboardPanels.size(); i++)
                {
                    ((TDashboardPanel) collTDashboardPanels.get(i)).save(con);
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
    public TDashboardTab copy() throws TorqueException
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
    public TDashboardTab copy(Connection con) throws TorqueException
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
    public TDashboardTab copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TDashboardTab(), deepcopy);
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
    public TDashboardTab copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TDashboardTab(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TDashboardTab copyInto(TDashboardTab copyObj) throws TorqueException
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
    protected TDashboardTab copyInto(TDashboardTab copyObj, Connection con) throws TorqueException
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
    protected TDashboardTab copyInto(TDashboardTab copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setLabel(label);
        copyObj.setDescription(description);
        copyObj.setIndex(index);
        copyObj.setParent(parent);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TDashboardPanel> vTDashboardPanels = getTDashboardPanels();
        if (vTDashboardPanels != null)
        {
            for (int i = 0; i < vTDashboardPanels.size(); i++)
            {
                TDashboardPanel obj =  vTDashboardPanels.get(i);
                copyObj.addTDashboardPanel(obj.copy());
            }
        }
        else
        {
            copyObj.collTDashboardPanels = null;
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
    protected TDashboardTab copyInto(TDashboardTab copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setLabel(label);
        copyObj.setDescription(description);
        copyObj.setIndex(index);
        copyObj.setParent(parent);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TDashboardPanel> vTDashboardPanels = getTDashboardPanels(con);
        if (vTDashboardPanels != null)
        {
            for (int i = 0; i < vTDashboardPanels.size(); i++)
            {
                TDashboardPanel obj =  vTDashboardPanels.get(i);
                copyObj.addTDashboardPanel(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTDashboardPanels = null;
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
    public TDashboardTabPeer getPeer()
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
        return TDashboardTabPeer.getTableMap();
    }

  
    /**
     * Creates a TDashboardTabBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TDashboardTabBean with the contents of this object
     */
    public TDashboardTabBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TDashboardTabBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TDashboardTabBean with the contents of this object
     */
    public TDashboardTabBean getBean(IdentityMap createdBeans)
    {
        TDashboardTabBean result = (TDashboardTabBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TDashboardTabBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setLabel(getLabel());
        result.setDescription(getDescription());
        result.setIndex(getIndex());
        result.setParent(getParent());
        result.setUuid(getUuid());



        if (collTDashboardPanels != null)
        {
            List<TDashboardPanelBean> relatedBeans = new ArrayList<TDashboardPanelBean>(collTDashboardPanels.size());
            for (Iterator<TDashboardPanel> collTDashboardPanelsIt = collTDashboardPanels.iterator(); collTDashboardPanelsIt.hasNext(); )
            {
                TDashboardPanel related = (TDashboardPanel) collTDashboardPanelsIt.next();
                TDashboardPanelBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTDashboardPanelBeans(relatedBeans);
        }




        if (aTDashboardScreen != null)
        {
            TDashboardScreenBean relatedBean = aTDashboardScreen.getBean(createdBeans);
            result.setTDashboardScreenBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TDashboardTab with the contents
     * of a TDashboardTabBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TDashboardTabBean which contents are used to create
     *        the resulting class
     * @return an instance of TDashboardTab with the contents of bean
     */
    public static TDashboardTab createTDashboardTab(TDashboardTabBean bean)
        throws TorqueException
    {
        return createTDashboardTab(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TDashboardTab with the contents
     * of a TDashboardTabBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TDashboardTabBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TDashboardTab with the contents of bean
     */

    public static TDashboardTab createTDashboardTab(TDashboardTabBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TDashboardTab result = (TDashboardTab) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TDashboardTab();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setLabel(bean.getLabel());
        result.setDescription(bean.getDescription());
        result.setIndex(bean.getIndex());
        result.setParent(bean.getParent());
        result.setUuid(bean.getUuid());



        {
            List<TDashboardPanelBean> relatedBeans = bean.getTDashboardPanelBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TDashboardPanelBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TDashboardPanelBean relatedBean =  relatedBeansIt.next();
                    TDashboardPanel related = TDashboardPanel.createTDashboardPanel(relatedBean, createdObjects);
                    result.addTDashboardPanelFromBean(related);
                }
            }
        }




        {
            TDashboardScreenBean relatedBean = bean.getTDashboardScreenBean();
            if (relatedBean != null)
            {
                TDashboardScreen relatedObject = TDashboardScreen.createTDashboardScreen(relatedBean, createdObjects);
                result.setTDashboardScreen(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TDashboardPanel object to this object.
     * through the TDashboardPanel foreign key attribute
     *
     * @param toAdd TDashboardPanel
     */
    protected void addTDashboardPanelFromBean(TDashboardPanel toAdd)
    {
        initTDashboardPanels();
        collTDashboardPanels.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TDashboardTab:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Index = ")
           .append(getIndex())
           .append("\n");
        str.append("Parent = ")
           .append(getParent())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
