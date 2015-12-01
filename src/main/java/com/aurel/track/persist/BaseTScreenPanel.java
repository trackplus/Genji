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



import com.aurel.track.persist.TScreenTab;
import com.aurel.track.persist.TScreenTabPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TScreenPanelBean;
import com.aurel.track.beans.TScreenTabBean;

import com.aurel.track.beans.TScreenFieldBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TScreenPanel
 */
public abstract class BaseTScreenPanel extends TpBaseObject
{
    /** The Peer class */
    private static final TScreenPanelPeer peer =
        new TScreenPanelPeer();


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

    /** The value for the rowsNo field */
    private Integer rowsNo;

    /** The value for the colsNo field */
    private Integer colsNo;

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



        // update associated TScreenField
        if (collTScreenFields != null)
        {
            for (int i = 0; i < collTScreenFields.size(); i++)
            {
                ((TScreenField) collTScreenFields.get(i))
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
     * Get the RowsNo
     *
     * @return Integer
     */
    public Integer getRowsNo()
    {
        return rowsNo;
    }


    /**
     * Set the value of RowsNo
     *
     * @param v new value
     */
    public void setRowsNo(Integer v) 
    {

        if (!ObjectUtils.equals(this.rowsNo, v))
        {
            this.rowsNo = v;
            setModified(true);
        }


    }

    /**
     * Get the ColsNo
     *
     * @return Integer
     */
    public Integer getColsNo()
    {
        return colsNo;
    }


    /**
     * Set the value of ColsNo
     *
     * @param v new value
     */
    public void setColsNo(Integer v) 
    {

        if (!ObjectUtils.equals(this.colsNo, v))
        {
            this.colsNo = v;
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


        if (aTScreenTab != null && !ObjectUtils.equals(aTScreenTab.getObjectID(), v))
        {
            aTScreenTab = null;
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

    



    private TScreenTab aTScreenTab;

    /**
     * Declares an association between this object and a TScreenTab object
     *
     * @param v TScreenTab
     * @throws TorqueException
     */
    public void setTScreenTab(TScreenTab v) throws TorqueException
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTScreenTab = v;
    }


    /**
     * Returns the associated TScreenTab object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TScreenTab object
     * @throws TorqueException
     */
    public TScreenTab getTScreenTab()
        throws TorqueException
    {
        if (aTScreenTab == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTScreenTab = TScreenTabPeer.retrieveByPK(SimpleKey.keyFor(this.parent));
        }
        return aTScreenTab;
    }

    /**
     * Return the associated TScreenTab object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TScreenTab object
     * @throws TorqueException
     */
    public TScreenTab getTScreenTab(Connection connection)
        throws TorqueException
    {
        if (aTScreenTab == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTScreenTab = TScreenTabPeer.retrieveByPK(SimpleKey.keyFor(this.parent), connection);
        }
        return aTScreenTab;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTScreenTabKey(ObjectKey key) throws TorqueException
    {

        setParent(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTScreenFields
     */
    protected List<TScreenField> collTScreenFields;

    /**
     * Temporary storage of collTScreenFields to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScreenFields()
    {
        if (collTScreenFields == null)
        {
            collTScreenFields = new ArrayList<TScreenField>();
        }
    }


    /**
     * Method called to associate a TScreenField object to this object
     * through the TScreenField foreign key attribute
     *
     * @param l TScreenField
     * @throws TorqueException
     */
    public void addTScreenField(TScreenField l) throws TorqueException
    {
        getTScreenFields().add(l);
        l.setTScreenPanel((TScreenPanel) this);
    }

    /**
     * Method called to associate a TScreenField object to this object
     * through the TScreenField foreign key attribute using connection.
     *
     * @param l TScreenField
     * @throws TorqueException
     */
    public void addTScreenField(TScreenField l, Connection con) throws TorqueException
    {
        getTScreenFields(con).add(l);
        l.setTScreenPanel((TScreenPanel) this);
    }

    /**
     * The criteria used to select the current contents of collTScreenFields
     */
    private Criteria lastTScreenFieldsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenFields(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScreenField> getTScreenFields()
        throws TorqueException
    {
        if (collTScreenFields == null)
        {
            collTScreenFields = getTScreenFields(new Criteria(10));
        }
        return collTScreenFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreenPanel has previously
     * been saved, it will retrieve related TScreenFields from storage.
     * If this TScreenPanel is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScreenField> getTScreenFields(Criteria criteria) throws TorqueException
    {
        if (collTScreenFields == null)
        {
            if (isNew())
            {
               collTScreenFields = new ArrayList<TScreenField>();
            }
            else
            {
                criteria.add(TScreenFieldPeer.PARENT, getObjectID() );
                collTScreenFields = TScreenFieldPeer.doSelect(criteria);
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
                criteria.add(TScreenFieldPeer.PARENT, getObjectID());
                if (!lastTScreenFieldsCriteria.equals(criteria))
                {
                    collTScreenFields = TScreenFieldPeer.doSelect(criteria);
                }
            }
        }
        lastTScreenFieldsCriteria = criteria;

        return collTScreenFields;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenFields(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenField> getTScreenFields(Connection con) throws TorqueException
    {
        if (collTScreenFields == null)
        {
            collTScreenFields = getTScreenFields(new Criteria(10), con);
        }
        return collTScreenFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreenPanel has previously
     * been saved, it will retrieve related TScreenFields from storage.
     * If this TScreenPanel is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenField> getTScreenFields(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScreenFields == null)
        {
            if (isNew())
            {
               collTScreenFields = new ArrayList<TScreenField>();
            }
            else
            {
                 criteria.add(TScreenFieldPeer.PARENT, getObjectID());
                 collTScreenFields = TScreenFieldPeer.doSelect(criteria, con);
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
                 criteria.add(TScreenFieldPeer.PARENT, getObjectID());
                 if (!lastTScreenFieldsCriteria.equals(criteria))
                 {
                     collTScreenFields = TScreenFieldPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScreenFieldsCriteria = criteria;

         return collTScreenFields;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreenPanel is new, it will return
     * an empty collection; or if this TScreenPanel has previously
     * been saved, it will retrieve related TScreenFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreenPanel.
     */
    protected List<TScreenField> getTScreenFieldsJoinTScreenPanel(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenFields == null)
        {
            if (isNew())
            {
               collTScreenFields = new ArrayList<TScreenField>();
            }
            else
            {
                criteria.add(TScreenFieldPeer.PARENT, getObjectID());
                collTScreenFields = TScreenFieldPeer.doSelectJoinTScreenPanel(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenFieldPeer.PARENT, getObjectID());
            if (!lastTScreenFieldsCriteria.equals(criteria))
            {
                collTScreenFields = TScreenFieldPeer.doSelectJoinTScreenPanel(criteria);
            }
        }
        lastTScreenFieldsCriteria = criteria;

        return collTScreenFields;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TScreenPanel is new, it will return
     * an empty collection; or if this TScreenPanel has previously
     * been saved, it will retrieve related TScreenFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TScreenPanel.
     */
    protected List<TScreenField> getTScreenFieldsJoinTField(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenFields == null)
        {
            if (isNew())
            {
               collTScreenFields = new ArrayList<TScreenField>();
            }
            else
            {
                criteria.add(TScreenFieldPeer.PARENT, getObjectID());
                collTScreenFields = TScreenFieldPeer.doSelectJoinTField(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenFieldPeer.PARENT, getObjectID());
            if (!lastTScreenFieldsCriteria.equals(criteria))
            {
                collTScreenFields = TScreenFieldPeer.doSelectJoinTField(criteria);
            }
        }
        lastTScreenFieldsCriteria = criteria;

        return collTScreenFields;
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
            fieldNames.add("RowsNo");
            fieldNames.add("ColsNo");
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
        if (name.equals("RowsNo"))
        {
            return getRowsNo();
        }
        if (name.equals("ColsNo"))
        {
            return getColsNo();
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
        if (name.equals("RowsNo"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRowsNo((Integer) value);
            return true;
        }
        if (name.equals("ColsNo"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setColsNo((Integer) value);
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
        if (name.equals(TScreenPanelPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TScreenPanelPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TScreenPanelPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TScreenPanelPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TScreenPanelPeer.SORTORDER))
        {
            return getIndex();
        }
        if (name.equals(TScreenPanelPeer.ROWSNO))
        {
            return getRowsNo();
        }
        if (name.equals(TScreenPanelPeer.COLSNO))
        {
            return getColsNo();
        }
        if (name.equals(TScreenPanelPeer.PARENT))
        {
            return getParent();
        }
        if (name.equals(TScreenPanelPeer.TPUUID))
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
      if (TScreenPanelPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TScreenPanelPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TScreenPanelPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TScreenPanelPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TScreenPanelPeer.SORTORDER.equals(name))
        {
            return setByName("Index", value);
        }
      if (TScreenPanelPeer.ROWSNO.equals(name))
        {
            return setByName("RowsNo", value);
        }
      if (TScreenPanelPeer.COLSNO.equals(name))
        {
            return setByName("ColsNo", value);
        }
      if (TScreenPanelPeer.PARENT.equals(name))
        {
            return setByName("Parent", value);
        }
      if (TScreenPanelPeer.TPUUID.equals(name))
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
            return getRowsNo();
        }
        if (pos == 6)
        {
            return getColsNo();
        }
        if (pos == 7)
        {
            return getParent();
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
            return setByName("RowsNo", value);
        }
    if (position == 6)
        {
            return setByName("ColsNo", value);
        }
    if (position == 7)
        {
            return setByName("Parent", value);
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
        save(TScreenPanelPeer.DATABASE_NAME);
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
                    TScreenPanelPeer.doInsert((TScreenPanel) this, con);
                    setNew(false);
                }
                else
                {
                    TScreenPanelPeer.doUpdate((TScreenPanel) this, con);
                }
            }


            if (collTScreenFields != null)
            {
                for (int i = 0; i < collTScreenFields.size(); i++)
                {
                    ((TScreenField) collTScreenFields.get(i)).save(con);
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
    public TScreenPanel copy() throws TorqueException
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
    public TScreenPanel copy(Connection con) throws TorqueException
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
    public TScreenPanel copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TScreenPanel(), deepcopy);
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
    public TScreenPanel copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TScreenPanel(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TScreenPanel copyInto(TScreenPanel copyObj) throws TorqueException
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
    protected TScreenPanel copyInto(TScreenPanel copyObj, Connection con) throws TorqueException
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
    protected TScreenPanel copyInto(TScreenPanel copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setLabel(label);
        copyObj.setDescription(description);
        copyObj.setIndex(index);
        copyObj.setRowsNo(rowsNo);
        copyObj.setColsNo(colsNo);
        copyObj.setParent(parent);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TScreenField> vTScreenFields = getTScreenFields();
        if (vTScreenFields != null)
        {
            for (int i = 0; i < vTScreenFields.size(); i++)
            {
                TScreenField obj =  vTScreenFields.get(i);
                copyObj.addTScreenField(obj.copy());
            }
        }
        else
        {
            copyObj.collTScreenFields = null;
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
    protected TScreenPanel copyInto(TScreenPanel copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setLabel(label);
        copyObj.setDescription(description);
        copyObj.setIndex(index);
        copyObj.setRowsNo(rowsNo);
        copyObj.setColsNo(colsNo);
        copyObj.setParent(parent);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TScreenField> vTScreenFields = getTScreenFields(con);
        if (vTScreenFields != null)
        {
            for (int i = 0; i < vTScreenFields.size(); i++)
            {
                TScreenField obj =  vTScreenFields.get(i);
                copyObj.addTScreenField(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScreenFields = null;
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
    public TScreenPanelPeer getPeer()
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
        return TScreenPanelPeer.getTableMap();
    }

  
    /**
     * Creates a TScreenPanelBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TScreenPanelBean with the contents of this object
     */
    public TScreenPanelBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TScreenPanelBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TScreenPanelBean with the contents of this object
     */
    public TScreenPanelBean getBean(IdentityMap createdBeans)
    {
        TScreenPanelBean result = (TScreenPanelBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TScreenPanelBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setLabel(getLabel());
        result.setDescription(getDescription());
        result.setIndex(getIndex());
        result.setRowsNo(getRowsNo());
        result.setColsNo(getColsNo());
        result.setParent(getParent());
        result.setUuid(getUuid());



        if (collTScreenFields != null)
        {
            List<TScreenFieldBean> relatedBeans = new ArrayList<TScreenFieldBean>(collTScreenFields.size());
            for (Iterator<TScreenField> collTScreenFieldsIt = collTScreenFields.iterator(); collTScreenFieldsIt.hasNext(); )
            {
                TScreenField related = (TScreenField) collTScreenFieldsIt.next();
                TScreenFieldBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScreenFieldBeans(relatedBeans);
        }




        if (aTScreenTab != null)
        {
            TScreenTabBean relatedBean = aTScreenTab.getBean(createdBeans);
            result.setTScreenTabBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TScreenPanel with the contents
     * of a TScreenPanelBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TScreenPanelBean which contents are used to create
     *        the resulting class
     * @return an instance of TScreenPanel with the contents of bean
     */
    public static TScreenPanel createTScreenPanel(TScreenPanelBean bean)
        throws TorqueException
    {
        return createTScreenPanel(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TScreenPanel with the contents
     * of a TScreenPanelBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TScreenPanelBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TScreenPanel with the contents of bean
     */

    public static TScreenPanel createTScreenPanel(TScreenPanelBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TScreenPanel result = (TScreenPanel) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TScreenPanel();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setLabel(bean.getLabel());
        result.setDescription(bean.getDescription());
        result.setIndex(bean.getIndex());
        result.setRowsNo(bean.getRowsNo());
        result.setColsNo(bean.getColsNo());
        result.setParent(bean.getParent());
        result.setUuid(bean.getUuid());



        {
            List<TScreenFieldBean> relatedBeans = bean.getTScreenFieldBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScreenFieldBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScreenFieldBean relatedBean =  relatedBeansIt.next();
                    TScreenField related = TScreenField.createTScreenField(relatedBean, createdObjects);
                    result.addTScreenFieldFromBean(related);
                }
            }
        }




        {
            TScreenTabBean relatedBean = bean.getTScreenTabBean();
            if (relatedBean != null)
            {
                TScreenTab relatedObject = TScreenTab.createTScreenTab(relatedBean, createdObjects);
                result.setTScreenTab(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TScreenField object to this object.
     * through the TScreenField foreign key attribute
     *
     * @param toAdd TScreenField
     */
    protected void addTScreenFieldFromBean(TScreenField toAdd)
    {
        initTScreenFields();
        collTScreenFields.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TScreenPanel:\n");
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
        str.append("RowsNo = ")
           .append(getRowsNo())
           .append("\n");
        str.append("ColsNo = ")
           .append(getColsNo())
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
