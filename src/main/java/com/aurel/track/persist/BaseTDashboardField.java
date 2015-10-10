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



import com.aurel.track.persist.TDashboardPanel;
import com.aurel.track.persist.TDashboardPanelPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TDashboardPanelBean;

import com.aurel.track.beans.TDashboardParameterBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TDashboardField
 */
public abstract class BaseTDashboardField extends TpBaseObject
{
    /** The Peer class */
    private static final TDashboardFieldPeer peer =
        new TDashboardFieldPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the description field */
    private String description;

    /** The value for the index field */
    private Integer index;

    /** The value for the colIndex field */
    private Integer colIndex;

    /** The value for the rowIndex field */
    private Integer rowIndex;

    /** The value for the colSpan field */
    private Integer colSpan;

    /** The value for the rowSpan field */
    private Integer rowSpan;

    /** The value for the parent field */
    private Integer parent;

    /** The value for the dashboardID field */
    private String dashboardID;

    /** The value for the theDescription field */
    private String theDescription;

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



        // update associated TDashboardParameter
        if (collTDashboardParameters != null)
        {
            for (int i = 0; i < collTDashboardParameters.size(); i++)
            {
                ((TDashboardParameter) collTDashboardParameters.get(i))
                        .setDashboardField(v);
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
     * Get the ColIndex
     *
     * @return Integer
     */
    public Integer getColIndex()
    {
        return colIndex;
    }


    /**
     * Set the value of ColIndex
     *
     * @param v new value
     */
    public void setColIndex(Integer v) 
    {

        if (!ObjectUtils.equals(this.colIndex, v))
        {
            this.colIndex = v;
            setModified(true);
        }


    }

    /**
     * Get the RowIndex
     *
     * @return Integer
     */
    public Integer getRowIndex()
    {
        return rowIndex;
    }


    /**
     * Set the value of RowIndex
     *
     * @param v new value
     */
    public void setRowIndex(Integer v) 
    {

        if (!ObjectUtils.equals(this.rowIndex, v))
        {
            this.rowIndex = v;
            setModified(true);
        }


    }

    /**
     * Get the ColSpan
     *
     * @return Integer
     */
    public Integer getColSpan()
    {
        return colSpan;
    }


    /**
     * Set the value of ColSpan
     *
     * @param v new value
     */
    public void setColSpan(Integer v) 
    {

        if (!ObjectUtils.equals(this.colSpan, v))
        {
            this.colSpan = v;
            setModified(true);
        }


    }

    /**
     * Get the RowSpan
     *
     * @return Integer
     */
    public Integer getRowSpan()
    {
        return rowSpan;
    }


    /**
     * Set the value of RowSpan
     *
     * @param v new value
     */
    public void setRowSpan(Integer v) 
    {

        if (!ObjectUtils.equals(this.rowSpan, v))
        {
            this.rowSpan = v;
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


        if (aTDashboardPanel != null && !ObjectUtils.equals(aTDashboardPanel.getObjectID(), v))
        {
            aTDashboardPanel = null;
        }

    }

    /**
     * Get the DashboardID
     *
     * @return String
     */
    public String getDashboardID()
    {
        return dashboardID;
    }


    /**
     * Set the value of DashboardID
     *
     * @param v new value
     */
    public void setDashboardID(String v) 
    {

        if (!ObjectUtils.equals(this.dashboardID, v))
        {
            this.dashboardID = v;
            setModified(true);
        }


    }

    /**
     * Get the TheDescription
     *
     * @return String
     */
    public String getTheDescription()
    {
        return theDescription;
    }


    /**
     * Set the value of TheDescription
     *
     * @param v new value
     */
    public void setTheDescription(String v) 
    {

        if (!ObjectUtils.equals(this.theDescription, v))
        {
            this.theDescription = v;
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

    



    private TDashboardPanel aTDashboardPanel;

    /**
     * Declares an association between this object and a TDashboardPanel object
     *
     * @param v TDashboardPanel
     * @throws TorqueException
     */
    public void setTDashboardPanel(TDashboardPanel v) throws TorqueException
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTDashboardPanel = v;
    }


    /**
     * Returns the associated TDashboardPanel object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TDashboardPanel object
     * @throws TorqueException
     */
    public TDashboardPanel getTDashboardPanel()
        throws TorqueException
    {
        if (aTDashboardPanel == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTDashboardPanel = TDashboardPanelPeer.retrieveByPK(SimpleKey.keyFor(this.parent));
        }
        return aTDashboardPanel;
    }

    /**
     * Return the associated TDashboardPanel object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TDashboardPanel object
     * @throws TorqueException
     */
    public TDashboardPanel getTDashboardPanel(Connection connection)
        throws TorqueException
    {
        if (aTDashboardPanel == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTDashboardPanel = TDashboardPanelPeer.retrieveByPK(SimpleKey.keyFor(this.parent), connection);
        }
        return aTDashboardPanel;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTDashboardPanelKey(ObjectKey key) throws TorqueException
    {

        setParent(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTDashboardParameters
     */
    protected List<TDashboardParameter> collTDashboardParameters;

    /**
     * Temporary storage of collTDashboardParameters to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTDashboardParameters()
    {
        if (collTDashboardParameters == null)
        {
            collTDashboardParameters = new ArrayList<TDashboardParameter>();
        }
    }


    /**
     * Method called to associate a TDashboardParameter object to this object
     * through the TDashboardParameter foreign key attribute
     *
     * @param l TDashboardParameter
     * @throws TorqueException
     */
    public void addTDashboardParameter(TDashboardParameter l) throws TorqueException
    {
        getTDashboardParameters().add(l);
        l.setTDashboardField((TDashboardField) this);
    }

    /**
     * Method called to associate a TDashboardParameter object to this object
     * through the TDashboardParameter foreign key attribute using connection.
     *
     * @param l TDashboardParameter
     * @throws TorqueException
     */
    public void addTDashboardParameter(TDashboardParameter l, Connection con) throws TorqueException
    {
        getTDashboardParameters(con).add(l);
        l.setTDashboardField((TDashboardField) this);
    }

    /**
     * The criteria used to select the current contents of collTDashboardParameters
     */
    private Criteria lastTDashboardParametersCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDashboardParameters(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TDashboardParameter> getTDashboardParameters()
        throws TorqueException
    {
        if (collTDashboardParameters == null)
        {
            collTDashboardParameters = getTDashboardParameters(new Criteria(10));
        }
        return collTDashboardParameters;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardField has previously
     * been saved, it will retrieve related TDashboardParameters from storage.
     * If this TDashboardField is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TDashboardParameter> getTDashboardParameters(Criteria criteria) throws TorqueException
    {
        if (collTDashboardParameters == null)
        {
            if (isNew())
            {
               collTDashboardParameters = new ArrayList<TDashboardParameter>();
            }
            else
            {
                criteria.add(TDashboardParameterPeer.DASHBOARDFIELD, getObjectID() );
                collTDashboardParameters = TDashboardParameterPeer.doSelect(criteria);
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
                criteria.add(TDashboardParameterPeer.DASHBOARDFIELD, getObjectID());
                if (!lastTDashboardParametersCriteria.equals(criteria))
                {
                    collTDashboardParameters = TDashboardParameterPeer.doSelect(criteria);
                }
            }
        }
        lastTDashboardParametersCriteria = criteria;

        return collTDashboardParameters;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDashboardParameters(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDashboardParameter> getTDashboardParameters(Connection con) throws TorqueException
    {
        if (collTDashboardParameters == null)
        {
            collTDashboardParameters = getTDashboardParameters(new Criteria(10), con);
        }
        return collTDashboardParameters;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardField has previously
     * been saved, it will retrieve related TDashboardParameters from storage.
     * If this TDashboardField is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDashboardParameter> getTDashboardParameters(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTDashboardParameters == null)
        {
            if (isNew())
            {
               collTDashboardParameters = new ArrayList<TDashboardParameter>();
            }
            else
            {
                 criteria.add(TDashboardParameterPeer.DASHBOARDFIELD, getObjectID());
                 collTDashboardParameters = TDashboardParameterPeer.doSelect(criteria, con);
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
                 criteria.add(TDashboardParameterPeer.DASHBOARDFIELD, getObjectID());
                 if (!lastTDashboardParametersCriteria.equals(criteria))
                 {
                     collTDashboardParameters = TDashboardParameterPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTDashboardParametersCriteria = criteria;

         return collTDashboardParameters;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardField is new, it will return
     * an empty collection; or if this TDashboardField has previously
     * been saved, it will retrieve related TDashboardParameters from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDashboardField.
     */
    protected List<TDashboardParameter> getTDashboardParametersJoinTDashboardField(Criteria criteria)
        throws TorqueException
    {
        if (collTDashboardParameters == null)
        {
            if (isNew())
            {
               collTDashboardParameters = new ArrayList<TDashboardParameter>();
            }
            else
            {
                criteria.add(TDashboardParameterPeer.DASHBOARDFIELD, getObjectID());
                collTDashboardParameters = TDashboardParameterPeer.doSelectJoinTDashboardField(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDashboardParameterPeer.DASHBOARDFIELD, getObjectID());
            if (!lastTDashboardParametersCriteria.equals(criteria))
            {
                collTDashboardParameters = TDashboardParameterPeer.doSelectJoinTDashboardField(criteria);
            }
        }
        lastTDashboardParametersCriteria = criteria;

        return collTDashboardParameters;
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
            fieldNames.add("Description");
            fieldNames.add("Index");
            fieldNames.add("ColIndex");
            fieldNames.add("RowIndex");
            fieldNames.add("ColSpan");
            fieldNames.add("RowSpan");
            fieldNames.add("Parent");
            fieldNames.add("DashboardID");
            fieldNames.add("TheDescription");
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
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("Index"))
        {
            return getIndex();
        }
        if (name.equals("ColIndex"))
        {
            return getColIndex();
        }
        if (name.equals("RowIndex"))
        {
            return getRowIndex();
        }
        if (name.equals("ColSpan"))
        {
            return getColSpan();
        }
        if (name.equals("RowSpan"))
        {
            return getRowSpan();
        }
        if (name.equals("Parent"))
        {
            return getParent();
        }
        if (name.equals("DashboardID"))
        {
            return getDashboardID();
        }
        if (name.equals("TheDescription"))
        {
            return getTheDescription();
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
        if (name.equals("ColIndex"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setColIndex((Integer) value);
            return true;
        }
        if (name.equals("RowIndex"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRowIndex((Integer) value);
            return true;
        }
        if (name.equals("ColSpan"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setColSpan((Integer) value);
            return true;
        }
        if (name.equals("RowSpan"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRowSpan((Integer) value);
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
        if (name.equals("DashboardID"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDashboardID((String) value);
            return true;
        }
        if (name.equals("TheDescription"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTheDescription((String) value);
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
        if (name.equals(TDashboardFieldPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TDashboardFieldPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TDashboardFieldPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TDashboardFieldPeer.SORTORDER))
        {
            return getIndex();
        }
        if (name.equals(TDashboardFieldPeer.COLINDEX))
        {
            return getColIndex();
        }
        if (name.equals(TDashboardFieldPeer.ROWINDEX))
        {
            return getRowIndex();
        }
        if (name.equals(TDashboardFieldPeer.COLSPAN))
        {
            return getColSpan();
        }
        if (name.equals(TDashboardFieldPeer.ROWSPAN))
        {
            return getRowSpan();
        }
        if (name.equals(TDashboardFieldPeer.PARENT))
        {
            return getParent();
        }
        if (name.equals(TDashboardFieldPeer.DASHBOARDID))
        {
            return getDashboardID();
        }
        if (name.equals(TDashboardFieldPeer.THEDESCRIPTION))
        {
            return getTheDescription();
        }
        if (name.equals(TDashboardFieldPeer.TPUUID))
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
      if (TDashboardFieldPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TDashboardFieldPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TDashboardFieldPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TDashboardFieldPeer.SORTORDER.equals(name))
        {
            return setByName("Index", value);
        }
      if (TDashboardFieldPeer.COLINDEX.equals(name))
        {
            return setByName("ColIndex", value);
        }
      if (TDashboardFieldPeer.ROWINDEX.equals(name))
        {
            return setByName("RowIndex", value);
        }
      if (TDashboardFieldPeer.COLSPAN.equals(name))
        {
            return setByName("ColSpan", value);
        }
      if (TDashboardFieldPeer.ROWSPAN.equals(name))
        {
            return setByName("RowSpan", value);
        }
      if (TDashboardFieldPeer.PARENT.equals(name))
        {
            return setByName("Parent", value);
        }
      if (TDashboardFieldPeer.DASHBOARDID.equals(name))
        {
            return setByName("DashboardID", value);
        }
      if (TDashboardFieldPeer.THEDESCRIPTION.equals(name))
        {
            return setByName("TheDescription", value);
        }
      if (TDashboardFieldPeer.TPUUID.equals(name))
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
            return getDescription();
        }
        if (pos == 3)
        {
            return getIndex();
        }
        if (pos == 4)
        {
            return getColIndex();
        }
        if (pos == 5)
        {
            return getRowIndex();
        }
        if (pos == 6)
        {
            return getColSpan();
        }
        if (pos == 7)
        {
            return getRowSpan();
        }
        if (pos == 8)
        {
            return getParent();
        }
        if (pos == 9)
        {
            return getDashboardID();
        }
        if (pos == 10)
        {
            return getTheDescription();
        }
        if (pos == 11)
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
            return setByName("Description", value);
        }
    if (position == 3)
        {
            return setByName("Index", value);
        }
    if (position == 4)
        {
            return setByName("ColIndex", value);
        }
    if (position == 5)
        {
            return setByName("RowIndex", value);
        }
    if (position == 6)
        {
            return setByName("ColSpan", value);
        }
    if (position == 7)
        {
            return setByName("RowSpan", value);
        }
    if (position == 8)
        {
            return setByName("Parent", value);
        }
    if (position == 9)
        {
            return setByName("DashboardID", value);
        }
    if (position == 10)
        {
            return setByName("TheDescription", value);
        }
    if (position == 11)
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
        save(TDashboardFieldPeer.DATABASE_NAME);
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
                    TDashboardFieldPeer.doInsert((TDashboardField) this, con);
                    setNew(false);
                }
                else
                {
                    TDashboardFieldPeer.doUpdate((TDashboardField) this, con);
                }
            }


            if (collTDashboardParameters != null)
            {
                for (int i = 0; i < collTDashboardParameters.size(); i++)
                {
                    ((TDashboardParameter) collTDashboardParameters.get(i)).save(con);
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
    public TDashboardField copy() throws TorqueException
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
    public TDashboardField copy(Connection con) throws TorqueException
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
    public TDashboardField copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TDashboardField(), deepcopy);
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
    public TDashboardField copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TDashboardField(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TDashboardField copyInto(TDashboardField copyObj) throws TorqueException
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
    protected TDashboardField copyInto(TDashboardField copyObj, Connection con) throws TorqueException
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
    protected TDashboardField copyInto(TDashboardField copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setDescription(description);
        copyObj.setIndex(index);
        copyObj.setColIndex(colIndex);
        copyObj.setRowIndex(rowIndex);
        copyObj.setColSpan(colSpan);
        copyObj.setRowSpan(rowSpan);
        copyObj.setParent(parent);
        copyObj.setDashboardID(dashboardID);
        copyObj.setTheDescription(theDescription);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TDashboardParameter> vTDashboardParameters = getTDashboardParameters();
        if (vTDashboardParameters != null)
        {
            for (int i = 0; i < vTDashboardParameters.size(); i++)
            {
                TDashboardParameter obj =  vTDashboardParameters.get(i);
                copyObj.addTDashboardParameter(obj.copy());
            }
        }
        else
        {
            copyObj.collTDashboardParameters = null;
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
    protected TDashboardField copyInto(TDashboardField copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setDescription(description);
        copyObj.setIndex(index);
        copyObj.setColIndex(colIndex);
        copyObj.setRowIndex(rowIndex);
        copyObj.setColSpan(colSpan);
        copyObj.setRowSpan(rowSpan);
        copyObj.setParent(parent);
        copyObj.setDashboardID(dashboardID);
        copyObj.setTheDescription(theDescription);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TDashboardParameter> vTDashboardParameters = getTDashboardParameters(con);
        if (vTDashboardParameters != null)
        {
            for (int i = 0; i < vTDashboardParameters.size(); i++)
            {
                TDashboardParameter obj =  vTDashboardParameters.get(i);
                copyObj.addTDashboardParameter(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTDashboardParameters = null;
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
    public TDashboardFieldPeer getPeer()
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
        return TDashboardFieldPeer.getTableMap();
    }

  
    /**
     * Creates a TDashboardFieldBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TDashboardFieldBean with the contents of this object
     */
    public TDashboardFieldBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TDashboardFieldBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TDashboardFieldBean with the contents of this object
     */
    public TDashboardFieldBean getBean(IdentityMap createdBeans)
    {
        TDashboardFieldBean result = (TDashboardFieldBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TDashboardFieldBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setDescription(getDescription());
        result.setIndex(getIndex());
        result.setColIndex(getColIndex());
        result.setRowIndex(getRowIndex());
        result.setColSpan(getColSpan());
        result.setRowSpan(getRowSpan());
        result.setParent(getParent());
        result.setDashboardID(getDashboardID());
        result.setTheDescription(getTheDescription());
        result.setUuid(getUuid());



        if (collTDashboardParameters != null)
        {
            List<TDashboardParameterBean> relatedBeans = new ArrayList<TDashboardParameterBean>(collTDashboardParameters.size());
            for (Iterator<TDashboardParameter> collTDashboardParametersIt = collTDashboardParameters.iterator(); collTDashboardParametersIt.hasNext(); )
            {
                TDashboardParameter related = (TDashboardParameter) collTDashboardParametersIt.next();
                TDashboardParameterBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTDashboardParameterBeans(relatedBeans);
        }




        if (aTDashboardPanel != null)
        {
            TDashboardPanelBean relatedBean = aTDashboardPanel.getBean(createdBeans);
            result.setTDashboardPanelBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TDashboardField with the contents
     * of a TDashboardFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TDashboardFieldBean which contents are used to create
     *        the resulting class
     * @return an instance of TDashboardField with the contents of bean
     */
    public static TDashboardField createTDashboardField(TDashboardFieldBean bean)
        throws TorqueException
    {
        return createTDashboardField(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TDashboardField with the contents
     * of a TDashboardFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TDashboardFieldBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TDashboardField with the contents of bean
     */

    public static TDashboardField createTDashboardField(TDashboardFieldBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TDashboardField result = (TDashboardField) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TDashboardField();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setDescription(bean.getDescription());
        result.setIndex(bean.getIndex());
        result.setColIndex(bean.getColIndex());
        result.setRowIndex(bean.getRowIndex());
        result.setColSpan(bean.getColSpan());
        result.setRowSpan(bean.getRowSpan());
        result.setParent(bean.getParent());
        result.setDashboardID(bean.getDashboardID());
        result.setTheDescription(bean.getTheDescription());
        result.setUuid(bean.getUuid());



        {
            List<TDashboardParameterBean> relatedBeans = bean.getTDashboardParameterBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TDashboardParameterBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TDashboardParameterBean relatedBean =  relatedBeansIt.next();
                    TDashboardParameter related = TDashboardParameter.createTDashboardParameter(relatedBean, createdObjects);
                    result.addTDashboardParameterFromBean(related);
                }
            }
        }




        {
            TDashboardPanelBean relatedBean = bean.getTDashboardPanelBean();
            if (relatedBean != null)
            {
                TDashboardPanel relatedObject = TDashboardPanel.createTDashboardPanel(relatedBean, createdObjects);
                result.setTDashboardPanel(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TDashboardParameter object to this object.
     * through the TDashboardParameter foreign key attribute
     *
     * @param toAdd TDashboardParameter
     */
    protected void addTDashboardParameterFromBean(TDashboardParameter toAdd)
    {
        initTDashboardParameters();
        collTDashboardParameters.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TDashboardField:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Index = ")
           .append(getIndex())
           .append("\n");
        str.append("ColIndex = ")
           .append(getColIndex())
           .append("\n");
        str.append("RowIndex = ")
           .append(getRowIndex())
           .append("\n");
        str.append("ColSpan = ")
           .append(getColSpan())
           .append("\n");
        str.append("RowSpan = ")
           .append(getRowSpan())
           .append("\n");
        str.append("Parent = ")
           .append(getParent())
           .append("\n");
        str.append("DashboardID = ")
           .append(getDashboardID())
           .append("\n");
        str.append("TheDescription = ")
           .append(getTheDescription())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
