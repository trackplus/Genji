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



import com.aurel.track.persist.TDashboardTab;
import com.aurel.track.persist.TDashboardTabPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TDashboardPanelBean;
import com.aurel.track.beans.TDashboardTabBean;

import com.aurel.track.beans.TDashboardFieldBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TDashboardPanel
 */
public abstract class BaseTDashboardPanel extends TpBaseObject
{
    /** The Peer class */
    private static final TDashboardPanelPeer peer =
        new TDashboardPanelPeer();


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



        // update associated TDashboardField
        if (collTDashboardFields != null)
        {
            for (int i = 0; i < collTDashboardFields.size(); i++)
            {
                ((TDashboardField) collTDashboardFields.get(i))
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


        if (aTDashboardTab != null && !ObjectUtils.equals(aTDashboardTab.getObjectID(), v))
        {
            aTDashboardTab = null;
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

    



    private TDashboardTab aTDashboardTab;

    /**
     * Declares an association between this object and a TDashboardTab object
     *
     * @param v TDashboardTab
     * @throws TorqueException
     */
    public void setTDashboardTab(TDashboardTab v) throws TorqueException
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTDashboardTab = v;
    }


    /**
     * Returns the associated TDashboardTab object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TDashboardTab object
     * @throws TorqueException
     */
    public TDashboardTab getTDashboardTab()
        throws TorqueException
    {
        if (aTDashboardTab == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTDashboardTab = TDashboardTabPeer.retrieveByPK(SimpleKey.keyFor(this.parent));
        }
        return aTDashboardTab;
    }

    /**
     * Return the associated TDashboardTab object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TDashboardTab object
     * @throws TorqueException
     */
    public TDashboardTab getTDashboardTab(Connection connection)
        throws TorqueException
    {
        if (aTDashboardTab == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTDashboardTab = TDashboardTabPeer.retrieveByPK(SimpleKey.keyFor(this.parent), connection);
        }
        return aTDashboardTab;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTDashboardTabKey(ObjectKey key) throws TorqueException
    {

        setParent(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTDashboardFields
     */
    protected List<TDashboardField> collTDashboardFields;

    /**
     * Temporary storage of collTDashboardFields to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTDashboardFields()
    {
        if (collTDashboardFields == null)
        {
            collTDashboardFields = new ArrayList<TDashboardField>();
        }
    }


    /**
     * Method called to associate a TDashboardField object to this object
     * through the TDashboardField foreign key attribute
     *
     * @param l TDashboardField
     * @throws TorqueException
     */
    public void addTDashboardField(TDashboardField l) throws TorqueException
    {
        getTDashboardFields().add(l);
        l.setTDashboardPanel((TDashboardPanel) this);
    }

    /**
     * Method called to associate a TDashboardField object to this object
     * through the TDashboardField foreign key attribute using connection.
     *
     * @param l TDashboardField
     * @throws TorqueException
     */
    public void addTDashboardField(TDashboardField l, Connection con) throws TorqueException
    {
        getTDashboardFields(con).add(l);
        l.setTDashboardPanel((TDashboardPanel) this);
    }

    /**
     * The criteria used to select the current contents of collTDashboardFields
     */
    private Criteria lastTDashboardFieldsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDashboardFields(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TDashboardField> getTDashboardFields()
        throws TorqueException
    {
        if (collTDashboardFields == null)
        {
            collTDashboardFields = getTDashboardFields(new Criteria(10));
        }
        return collTDashboardFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardPanel has previously
     * been saved, it will retrieve related TDashboardFields from storage.
     * If this TDashboardPanel is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TDashboardField> getTDashboardFields(Criteria criteria) throws TorqueException
    {
        if (collTDashboardFields == null)
        {
            if (isNew())
            {
               collTDashboardFields = new ArrayList<TDashboardField>();
            }
            else
            {
                criteria.add(TDashboardFieldPeer.PARENT, getObjectID() );
                collTDashboardFields = TDashboardFieldPeer.doSelect(criteria);
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
                criteria.add(TDashboardFieldPeer.PARENT, getObjectID());
                if (!lastTDashboardFieldsCriteria.equals(criteria))
                {
                    collTDashboardFields = TDashboardFieldPeer.doSelect(criteria);
                }
            }
        }
        lastTDashboardFieldsCriteria = criteria;

        return collTDashboardFields;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDashboardFields(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDashboardField> getTDashboardFields(Connection con) throws TorqueException
    {
        if (collTDashboardFields == null)
        {
            collTDashboardFields = getTDashboardFields(new Criteria(10), con);
        }
        return collTDashboardFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardPanel has previously
     * been saved, it will retrieve related TDashboardFields from storage.
     * If this TDashboardPanel is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDashboardField> getTDashboardFields(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTDashboardFields == null)
        {
            if (isNew())
            {
               collTDashboardFields = new ArrayList<TDashboardField>();
            }
            else
            {
                 criteria.add(TDashboardFieldPeer.PARENT, getObjectID());
                 collTDashboardFields = TDashboardFieldPeer.doSelect(criteria, con);
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
                 criteria.add(TDashboardFieldPeer.PARENT, getObjectID());
                 if (!lastTDashboardFieldsCriteria.equals(criteria))
                 {
                     collTDashboardFields = TDashboardFieldPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTDashboardFieldsCriteria = criteria;

         return collTDashboardFields;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardPanel is new, it will return
     * an empty collection; or if this TDashboardPanel has previously
     * been saved, it will retrieve related TDashboardFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDashboardPanel.
     */
    protected List<TDashboardField> getTDashboardFieldsJoinTDashboardPanel(Criteria criteria)
        throws TorqueException
    {
        if (collTDashboardFields == null)
        {
            if (isNew())
            {
               collTDashboardFields = new ArrayList<TDashboardField>();
            }
            else
            {
                criteria.add(TDashboardFieldPeer.PARENT, getObjectID());
                collTDashboardFields = TDashboardFieldPeer.doSelectJoinTDashboardPanel(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDashboardFieldPeer.PARENT, getObjectID());
            if (!lastTDashboardFieldsCriteria.equals(criteria))
            {
                collTDashboardFields = TDashboardFieldPeer.doSelectJoinTDashboardPanel(criteria);
            }
        }
        lastTDashboardFieldsCriteria = criteria;

        return collTDashboardFields;
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
        if (name.equals(TDashboardPanelPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TDashboardPanelPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TDashboardPanelPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TDashboardPanelPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TDashboardPanelPeer.SORTORDER))
        {
            return getIndex();
        }
        if (name.equals(TDashboardPanelPeer.ROWSNO))
        {
            return getRowsNo();
        }
        if (name.equals(TDashboardPanelPeer.COLSNO))
        {
            return getColsNo();
        }
        if (name.equals(TDashboardPanelPeer.PARENT))
        {
            return getParent();
        }
        if (name.equals(TDashboardPanelPeer.TPUUID))
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
      if (TDashboardPanelPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TDashboardPanelPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TDashboardPanelPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TDashboardPanelPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TDashboardPanelPeer.SORTORDER.equals(name))
        {
            return setByName("Index", value);
        }
      if (TDashboardPanelPeer.ROWSNO.equals(name))
        {
            return setByName("RowsNo", value);
        }
      if (TDashboardPanelPeer.COLSNO.equals(name))
        {
            return setByName("ColsNo", value);
        }
      if (TDashboardPanelPeer.PARENT.equals(name))
        {
            return setByName("Parent", value);
        }
      if (TDashboardPanelPeer.TPUUID.equals(name))
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
        save(TDashboardPanelPeer.DATABASE_NAME);
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
                    TDashboardPanelPeer.doInsert((TDashboardPanel) this, con);
                    setNew(false);
                }
                else
                {
                    TDashboardPanelPeer.doUpdate((TDashboardPanel) this, con);
                }
            }


            if (collTDashboardFields != null)
            {
                for (int i = 0; i < collTDashboardFields.size(); i++)
                {
                    ((TDashboardField) collTDashboardFields.get(i)).save(con);
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
    public TDashboardPanel copy() throws TorqueException
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
    public TDashboardPanel copy(Connection con) throws TorqueException
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
    public TDashboardPanel copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TDashboardPanel(), deepcopy);
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
    public TDashboardPanel copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TDashboardPanel(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TDashboardPanel copyInto(TDashboardPanel copyObj) throws TorqueException
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
    protected TDashboardPanel copyInto(TDashboardPanel copyObj, Connection con) throws TorqueException
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
    protected TDashboardPanel copyInto(TDashboardPanel copyObj, boolean deepcopy) throws TorqueException
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


        List<TDashboardField> vTDashboardFields = getTDashboardFields();
        if (vTDashboardFields != null)
        {
            for (int i = 0; i < vTDashboardFields.size(); i++)
            {
                TDashboardField obj =  vTDashboardFields.get(i);
                copyObj.addTDashboardField(obj.copy());
            }
        }
        else
        {
            copyObj.collTDashboardFields = null;
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
    protected TDashboardPanel copyInto(TDashboardPanel copyObj, boolean deepcopy, Connection con) throws TorqueException
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


        List<TDashboardField> vTDashboardFields = getTDashboardFields(con);
        if (vTDashboardFields != null)
        {
            for (int i = 0; i < vTDashboardFields.size(); i++)
            {
                TDashboardField obj =  vTDashboardFields.get(i);
                copyObj.addTDashboardField(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTDashboardFields = null;
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
    public TDashboardPanelPeer getPeer()
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
        return TDashboardPanelPeer.getTableMap();
    }

  
    /**
     * Creates a TDashboardPanelBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TDashboardPanelBean with the contents of this object
     */
    public TDashboardPanelBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TDashboardPanelBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TDashboardPanelBean with the contents of this object
     */
    public TDashboardPanelBean getBean(IdentityMap createdBeans)
    {
        TDashboardPanelBean result = (TDashboardPanelBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TDashboardPanelBean();
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



        if (collTDashboardFields != null)
        {
            List<TDashboardFieldBean> relatedBeans = new ArrayList<TDashboardFieldBean>(collTDashboardFields.size());
            for (Iterator<TDashboardField> collTDashboardFieldsIt = collTDashboardFields.iterator(); collTDashboardFieldsIt.hasNext(); )
            {
                TDashboardField related = (TDashboardField) collTDashboardFieldsIt.next();
                TDashboardFieldBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTDashboardFieldBeans(relatedBeans);
        }




        if (aTDashboardTab != null)
        {
            TDashboardTabBean relatedBean = aTDashboardTab.getBean(createdBeans);
            result.setTDashboardTabBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TDashboardPanel with the contents
     * of a TDashboardPanelBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TDashboardPanelBean which contents are used to create
     *        the resulting class
     * @return an instance of TDashboardPanel with the contents of bean
     */
    public static TDashboardPanel createTDashboardPanel(TDashboardPanelBean bean)
        throws TorqueException
    {
        return createTDashboardPanel(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TDashboardPanel with the contents
     * of a TDashboardPanelBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TDashboardPanelBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TDashboardPanel with the contents of bean
     */

    public static TDashboardPanel createTDashboardPanel(TDashboardPanelBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TDashboardPanel result = (TDashboardPanel) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TDashboardPanel();
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
            List<TDashboardFieldBean> relatedBeans = bean.getTDashboardFieldBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TDashboardFieldBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TDashboardFieldBean relatedBean =  relatedBeansIt.next();
                    TDashboardField related = TDashboardField.createTDashboardField(relatedBean, createdObjects);
                    result.addTDashboardFieldFromBean(related);
                }
            }
        }




        {
            TDashboardTabBean relatedBean = bean.getTDashboardTabBean();
            if (relatedBean != null)
            {
                TDashboardTab relatedObject = TDashboardTab.createTDashboardTab(relatedBean, createdObjects);
                result.setTDashboardTab(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TDashboardField object to this object.
     * through the TDashboardField foreign key attribute
     *
     * @param toAdd TDashboardField
     */
    protected void addTDashboardFieldFromBean(TDashboardField toAdd)
    {
        initTDashboardFields();
        collTDashboardFields.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TDashboardPanel:\n");
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
