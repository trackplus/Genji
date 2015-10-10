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



import com.aurel.track.persist.TScreen;
import com.aurel.track.persist.TScreenPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TScreenTabBean;
import com.aurel.track.beans.TScreenBean;

import com.aurel.track.beans.TScreenPanelBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TScreenTab
 */
public abstract class BaseTScreenTab extends TpBaseObject
{
    /** The Peer class */
    private static final TScreenTabPeer peer =
        new TScreenTabPeer();


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



        // update associated TScreenPanel
        if (collTScreenPanels != null)
        {
            for (int i = 0; i < collTScreenPanels.size(); i++)
            {
                ((TScreenPanel) collTScreenPanels.get(i))
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


        if (aTScreen != null && !ObjectUtils.equals(aTScreen.getObjectID(), v))
        {
            aTScreen = null;
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

    



    private TScreen aTScreen;

    /**
     * Declares an association between this object and a TScreen object
     *
     * @param v TScreen
     * @throws TorqueException
     */
    public void setTScreen(TScreen v) throws TorqueException
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTScreen = v;
    }


    /**
     * Returns the associated TScreen object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TScreen object
     * @throws TorqueException
     */
    public TScreen getTScreen()
        throws TorqueException
    {
        if (aTScreen == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTScreen = TScreenPeer.retrieveByPK(SimpleKey.keyFor(this.parent));
        }
        return aTScreen;
    }

    /**
     * Return the associated TScreen object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TScreen object
     * @throws TorqueException
     */
    public TScreen getTScreen(Connection connection)
        throws TorqueException
    {
        if (aTScreen == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTScreen = TScreenPeer.retrieveByPK(SimpleKey.keyFor(this.parent), connection);
        }
        return aTScreen;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTScreenKey(ObjectKey key) throws TorqueException
    {

        setParent(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTScreenPanels
     */
    protected List<TScreenPanel> collTScreenPanels;

    /**
     * Temporary storage of collTScreenPanels to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScreenPanels()
    {
        if (collTScreenPanels == null)
        {
            collTScreenPanels = new ArrayList<TScreenPanel>();
        }
    }


    /**
     * Method called to associate a TScreenPanel object to this object
     * through the TScreenPanel foreign key attribute
     *
     * @param l TScreenPanel
     * @throws TorqueException
     */
    public void addTScreenPanel(TScreenPanel l) throws TorqueException
    {
        getTScreenPanels().add(l);
        l.setTScreenTab((TScreenTab) this);
    }

    /**
     * Method called to associate a TScreenPanel object to this object
     * through the TScreenPanel foreign key attribute using connection.
     *
     * @param l TScreenPanel
     * @throws TorqueException
     */
    public void addTScreenPanel(TScreenPanel l, Connection con) throws TorqueException
    {
        getTScreenPanels(con).add(l);
        l.setTScreenTab((TScreenTab) this);
    }

    /**
     * The criteria used to select the current contents of collTScreenPanels
     */
    private Criteria lastTScreenPanelsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenPanels(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScreenPanel> getTScreenPanels()
        throws TorqueException
    {
        if (collTScreenPanels == null)
        {
            collTScreenPanels = getTScreenPanels(new Criteria(10));
        }
        return collTScreenPanels;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreenTab has previously
     * been saved, it will retrieve related TScreenPanels from storage.
     * If this TScreenTab is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScreenPanel> getTScreenPanels(Criteria criteria) throws TorqueException
    {
        if (collTScreenPanels == null)
        {
            if (isNew())
            {
               collTScreenPanels = new ArrayList<TScreenPanel>();
            }
            else
            {
                criteria.add(TScreenPanelPeer.PARENT, getObjectID() );
                collTScreenPanels = TScreenPanelPeer.doSelect(criteria);
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
                criteria.add(TScreenPanelPeer.PARENT, getObjectID());
                if (!lastTScreenPanelsCriteria.equals(criteria))
                {
                    collTScreenPanels = TScreenPanelPeer.doSelect(criteria);
                }
            }
        }
        lastTScreenPanelsCriteria = criteria;

        return collTScreenPanels;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenPanels(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenPanel> getTScreenPanels(Connection con) throws TorqueException
    {
        if (collTScreenPanels == null)
        {
            collTScreenPanels = getTScreenPanels(new Criteria(10), con);
        }
        return collTScreenPanels;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreenTab has previously
     * been saved, it will retrieve related TScreenPanels from storage.
     * If this TScreenTab is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenPanel> getTScreenPanels(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScreenPanels == null)
        {
            if (isNew())
            {
               collTScreenPanels = new ArrayList<TScreenPanel>();
            }
            else
            {
                 criteria.add(TScreenPanelPeer.PARENT, getObjectID());
                 collTScreenPanels = TScreenPanelPeer.doSelect(criteria, con);
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
                 criteria.add(TScreenPanelPeer.PARENT, getObjectID());
                 if (!lastTScreenPanelsCriteria.equals(criteria))
                 {
                     collTScreenPanels = TScreenPanelPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScreenPanelsCriteria = criteria;

         return collTScreenPanels;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreenTab is new, it will return
     * an empty collection; or if this TScreenTab has previously
     * been saved, it will retrieve related TScreenPanels from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreenTab.
     */
    protected List<TScreenPanel> getTScreenPanelsJoinTScreenTab(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenPanels == null)
        {
            if (isNew())
            {
               collTScreenPanels = new ArrayList<TScreenPanel>();
            }
            else
            {
                criteria.add(TScreenPanelPeer.PARENT, getObjectID());
                collTScreenPanels = TScreenPanelPeer.doSelectJoinTScreenTab(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenPanelPeer.PARENT, getObjectID());
            if (!lastTScreenPanelsCriteria.equals(criteria))
            {
                collTScreenPanels = TScreenPanelPeer.doSelectJoinTScreenTab(criteria);
            }
        }
        lastTScreenPanelsCriteria = criteria;

        return collTScreenPanels;
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
        if (name.equals(TScreenTabPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TScreenTabPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TScreenTabPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TScreenTabPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TScreenTabPeer.SORTORDER))
        {
            return getIndex();
        }
        if (name.equals(TScreenTabPeer.PARENT))
        {
            return getParent();
        }
        if (name.equals(TScreenTabPeer.TPUUID))
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
      if (TScreenTabPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TScreenTabPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TScreenTabPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TScreenTabPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TScreenTabPeer.SORTORDER.equals(name))
        {
            return setByName("Index", value);
        }
      if (TScreenTabPeer.PARENT.equals(name))
        {
            return setByName("Parent", value);
        }
      if (TScreenTabPeer.TPUUID.equals(name))
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
        save(TScreenTabPeer.DATABASE_NAME);
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
                    TScreenTabPeer.doInsert((TScreenTab) this, con);
                    setNew(false);
                }
                else
                {
                    TScreenTabPeer.doUpdate((TScreenTab) this, con);
                }
            }


            if (collTScreenPanels != null)
            {
                for (int i = 0; i < collTScreenPanels.size(); i++)
                {
                    ((TScreenPanel) collTScreenPanels.get(i)).save(con);
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
    public TScreenTab copy() throws TorqueException
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
    public TScreenTab copy(Connection con) throws TorqueException
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
    public TScreenTab copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TScreenTab(), deepcopy);
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
    public TScreenTab copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TScreenTab(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TScreenTab copyInto(TScreenTab copyObj) throws TorqueException
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
    protected TScreenTab copyInto(TScreenTab copyObj, Connection con) throws TorqueException
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
    protected TScreenTab copyInto(TScreenTab copyObj, boolean deepcopy) throws TorqueException
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


        List<TScreenPanel> vTScreenPanels = getTScreenPanels();
        if (vTScreenPanels != null)
        {
            for (int i = 0; i < vTScreenPanels.size(); i++)
            {
                TScreenPanel obj =  vTScreenPanels.get(i);
                copyObj.addTScreenPanel(obj.copy());
            }
        }
        else
        {
            copyObj.collTScreenPanels = null;
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
    protected TScreenTab copyInto(TScreenTab copyObj, boolean deepcopy, Connection con) throws TorqueException
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


        List<TScreenPanel> vTScreenPanels = getTScreenPanels(con);
        if (vTScreenPanels != null)
        {
            for (int i = 0; i < vTScreenPanels.size(); i++)
            {
                TScreenPanel obj =  vTScreenPanels.get(i);
                copyObj.addTScreenPanel(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScreenPanels = null;
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
    public TScreenTabPeer getPeer()
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
        return TScreenTabPeer.getTableMap();
    }

  
    /**
     * Creates a TScreenTabBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TScreenTabBean with the contents of this object
     */
    public TScreenTabBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TScreenTabBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TScreenTabBean with the contents of this object
     */
    public TScreenTabBean getBean(IdentityMap createdBeans)
    {
        TScreenTabBean result = (TScreenTabBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TScreenTabBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setLabel(getLabel());
        result.setDescription(getDescription());
        result.setIndex(getIndex());
        result.setParent(getParent());
        result.setUuid(getUuid());



        if (collTScreenPanels != null)
        {
            List<TScreenPanelBean> relatedBeans = new ArrayList<TScreenPanelBean>(collTScreenPanels.size());
            for (Iterator<TScreenPanel> collTScreenPanelsIt = collTScreenPanels.iterator(); collTScreenPanelsIt.hasNext(); )
            {
                TScreenPanel related = (TScreenPanel) collTScreenPanelsIt.next();
                TScreenPanelBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScreenPanelBeans(relatedBeans);
        }




        if (aTScreen != null)
        {
            TScreenBean relatedBean = aTScreen.getBean(createdBeans);
            result.setTScreenBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TScreenTab with the contents
     * of a TScreenTabBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TScreenTabBean which contents are used to create
     *        the resulting class
     * @return an instance of TScreenTab with the contents of bean
     */
    public static TScreenTab createTScreenTab(TScreenTabBean bean)
        throws TorqueException
    {
        return createTScreenTab(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TScreenTab with the contents
     * of a TScreenTabBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TScreenTabBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TScreenTab with the contents of bean
     */

    public static TScreenTab createTScreenTab(TScreenTabBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TScreenTab result = (TScreenTab) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TScreenTab();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setLabel(bean.getLabel());
        result.setDescription(bean.getDescription());
        result.setIndex(bean.getIndex());
        result.setParent(bean.getParent());
        result.setUuid(bean.getUuid());



        {
            List<TScreenPanelBean> relatedBeans = bean.getTScreenPanelBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScreenPanelBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScreenPanelBean relatedBean =  relatedBeansIt.next();
                    TScreenPanel related = TScreenPanel.createTScreenPanel(relatedBean, createdObjects);
                    result.addTScreenPanelFromBean(related);
                }
            }
        }




        {
            TScreenBean relatedBean = bean.getTScreenBean();
            if (relatedBean != null)
            {
                TScreen relatedObject = TScreen.createTScreen(relatedBean, createdObjects);
                result.setTScreen(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TScreenPanel object to this object.
     * through the TScreenPanel foreign key attribute
     *
     * @param toAdd TScreenPanel
     */
    protected void addTScreenPanelFromBean(TScreenPanel toAdd)
    {
        initTScreenPanels();
        collTScreenPanels.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TScreenTab:\n");
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
