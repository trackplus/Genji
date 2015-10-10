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



import com.aurel.track.persist.TNavigatorLayout;
import com.aurel.track.persist.TNavigatorLayoutPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TCardGroupingFieldBean;
import com.aurel.track.beans.TNavigatorLayoutBean;

import com.aurel.track.beans.TCardFieldOptionBean;


/**
 * grouping field for card
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TCardGroupingField
 */
public abstract class BaseTCardGroupingField extends TpBaseObject
{
    /** The Peer class */
    private static final TCardGroupingFieldPeer peer =
        new TCardGroupingFieldPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the navigatorLayout field */
    private Integer navigatorLayout;

    /** The value for the cardField field */
    private Integer cardField;

    /** The value for the isActiv field */
    private String isActiv = "N";

    /** The value for the sortField field */
    private Integer sortField;

    /** The value for the isDescending field */
    private String isDescending = "N";

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



        // update associated TCardFieldOption
        if (collTCardFieldOptions != null)
        {
            for (int i = 0; i < collTCardFieldOptions.size(); i++)
            {
                ((TCardFieldOption) collTCardFieldOptions.get(i))
                        .setGroupingField(v);
            }
        }
    }

    /**
     * Get the NavigatorLayout
     *
     * @return Integer
     */
    public Integer getNavigatorLayout()
    {
        return navigatorLayout;
    }


    /**
     * Set the value of NavigatorLayout
     *
     * @param v new value
     */
    public void setNavigatorLayout(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.navigatorLayout, v))
        {
            this.navigatorLayout = v;
            setModified(true);
        }


        if (aTNavigatorLayout != null && !ObjectUtils.equals(aTNavigatorLayout.getObjectID(), v))
        {
            aTNavigatorLayout = null;
        }

    }

    /**
     * Get the CardField
     *
     * @return Integer
     */
    public Integer getCardField()
    {
        return cardField;
    }


    /**
     * Set the value of CardField
     *
     * @param v new value
     */
    public void setCardField(Integer v) 
    {

        if (!ObjectUtils.equals(this.cardField, v))
        {
            this.cardField = v;
            setModified(true);
        }


    }

    /**
     * Get the IsActiv
     *
     * @return String
     */
    public String getIsActiv()
    {
        return isActiv;
    }


    /**
     * Set the value of IsActiv
     *
     * @param v new value
     */
    public void setIsActiv(String v) 
    {

        if (!ObjectUtils.equals(this.isActiv, v))
        {
            this.isActiv = v;
            setModified(true);
        }


    }

    /**
     * Get the SortField
     *
     * @return Integer
     */
    public Integer getSortField()
    {
        return sortField;
    }


    /**
     * Set the value of SortField
     *
     * @param v new value
     */
    public void setSortField(Integer v) 
    {

        if (!ObjectUtils.equals(this.sortField, v))
        {
            this.sortField = v;
            setModified(true);
        }


    }

    /**
     * Get the IsDescending
     *
     * @return String
     */
    public String getIsDescending()
    {
        return isDescending;
    }


    /**
     * Set the value of IsDescending
     *
     * @param v new value
     */
    public void setIsDescending(String v) 
    {

        if (!ObjectUtils.equals(this.isDescending, v))
        {
            this.isDescending = v;
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

    



    private TNavigatorLayout aTNavigatorLayout;

    /**
     * Declares an association between this object and a TNavigatorLayout object
     *
     * @param v TNavigatorLayout
     * @throws TorqueException
     */
    public void setTNavigatorLayout(TNavigatorLayout v) throws TorqueException
    {
        if (v == null)
        {
            setNavigatorLayout((Integer) null);
        }
        else
        {
            setNavigatorLayout(v.getObjectID());
        }
        aTNavigatorLayout = v;
    }


    /**
     * Returns the associated TNavigatorLayout object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TNavigatorLayout object
     * @throws TorqueException
     */
    public TNavigatorLayout getTNavigatorLayout()
        throws TorqueException
    {
        if (aTNavigatorLayout == null && (!ObjectUtils.equals(this.navigatorLayout, null)))
        {
            aTNavigatorLayout = TNavigatorLayoutPeer.retrieveByPK(SimpleKey.keyFor(this.navigatorLayout));
        }
        return aTNavigatorLayout;
    }

    /**
     * Return the associated TNavigatorLayout object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TNavigatorLayout object
     * @throws TorqueException
     */
    public TNavigatorLayout getTNavigatorLayout(Connection connection)
        throws TorqueException
    {
        if (aTNavigatorLayout == null && (!ObjectUtils.equals(this.navigatorLayout, null)))
        {
            aTNavigatorLayout = TNavigatorLayoutPeer.retrieveByPK(SimpleKey.keyFor(this.navigatorLayout), connection);
        }
        return aTNavigatorLayout;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTNavigatorLayoutKey(ObjectKey key) throws TorqueException
    {

        setNavigatorLayout(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTCardFieldOptions
     */
    protected List<TCardFieldOption> collTCardFieldOptions;

    /**
     * Temporary storage of collTCardFieldOptions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTCardFieldOptions()
    {
        if (collTCardFieldOptions == null)
        {
            collTCardFieldOptions = new ArrayList<TCardFieldOption>();
        }
    }


    /**
     * Method called to associate a TCardFieldOption object to this object
     * through the TCardFieldOption foreign key attribute
     *
     * @param l TCardFieldOption
     * @throws TorqueException
     */
    public void addTCardFieldOption(TCardFieldOption l) throws TorqueException
    {
        getTCardFieldOptions().add(l);
        l.setTCardGroupingField((TCardGroupingField) this);
    }

    /**
     * Method called to associate a TCardFieldOption object to this object
     * through the TCardFieldOption foreign key attribute using connection.
     *
     * @param l TCardFieldOption
     * @throws TorqueException
     */
    public void addTCardFieldOption(TCardFieldOption l, Connection con) throws TorqueException
    {
        getTCardFieldOptions(con).add(l);
        l.setTCardGroupingField((TCardGroupingField) this);
    }

    /**
     * The criteria used to select the current contents of collTCardFieldOptions
     */
    private Criteria lastTCardFieldOptionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCardFieldOptions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TCardFieldOption> getTCardFieldOptions()
        throws TorqueException
    {
        if (collTCardFieldOptions == null)
        {
            collTCardFieldOptions = getTCardFieldOptions(new Criteria(10));
        }
        return collTCardFieldOptions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCardGroupingField has previously
     * been saved, it will retrieve related TCardFieldOptions from storage.
     * If this TCardGroupingField is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TCardFieldOption> getTCardFieldOptions(Criteria criteria) throws TorqueException
    {
        if (collTCardFieldOptions == null)
        {
            if (isNew())
            {
               collTCardFieldOptions = new ArrayList<TCardFieldOption>();
            }
            else
            {
                criteria.add(TCardFieldOptionPeer.GROUPINGFIELD, getObjectID() );
                collTCardFieldOptions = TCardFieldOptionPeer.doSelect(criteria);
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
                criteria.add(TCardFieldOptionPeer.GROUPINGFIELD, getObjectID());
                if (!lastTCardFieldOptionsCriteria.equals(criteria))
                {
                    collTCardFieldOptions = TCardFieldOptionPeer.doSelect(criteria);
                }
            }
        }
        lastTCardFieldOptionsCriteria = criteria;

        return collTCardFieldOptions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCardFieldOptions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCardFieldOption> getTCardFieldOptions(Connection con) throws TorqueException
    {
        if (collTCardFieldOptions == null)
        {
            collTCardFieldOptions = getTCardFieldOptions(new Criteria(10), con);
        }
        return collTCardFieldOptions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCardGroupingField has previously
     * been saved, it will retrieve related TCardFieldOptions from storage.
     * If this TCardGroupingField is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCardFieldOption> getTCardFieldOptions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTCardFieldOptions == null)
        {
            if (isNew())
            {
               collTCardFieldOptions = new ArrayList<TCardFieldOption>();
            }
            else
            {
                 criteria.add(TCardFieldOptionPeer.GROUPINGFIELD, getObjectID());
                 collTCardFieldOptions = TCardFieldOptionPeer.doSelect(criteria, con);
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
                 criteria.add(TCardFieldOptionPeer.GROUPINGFIELD, getObjectID());
                 if (!lastTCardFieldOptionsCriteria.equals(criteria))
                 {
                     collTCardFieldOptions = TCardFieldOptionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTCardFieldOptionsCriteria = criteria;

         return collTCardFieldOptions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCardGroupingField is new, it will return
     * an empty collection; or if this TCardGroupingField has previously
     * been saved, it will retrieve related TCardFieldOptions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCardGroupingField.
     */
    protected List<TCardFieldOption> getTCardFieldOptionsJoinTCardGroupingField(Criteria criteria)
        throws TorqueException
    {
        if (collTCardFieldOptions == null)
        {
            if (isNew())
            {
               collTCardFieldOptions = new ArrayList<TCardFieldOption>();
            }
            else
            {
                criteria.add(TCardFieldOptionPeer.GROUPINGFIELD, getObjectID());
                collTCardFieldOptions = TCardFieldOptionPeer.doSelectJoinTCardGroupingField(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCardFieldOptionPeer.GROUPINGFIELD, getObjectID());
            if (!lastTCardFieldOptionsCriteria.equals(criteria))
            {
                collTCardFieldOptions = TCardFieldOptionPeer.doSelectJoinTCardGroupingField(criteria);
            }
        }
        lastTCardFieldOptionsCriteria = criteria;

        return collTCardFieldOptions;
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
            fieldNames.add("NavigatorLayout");
            fieldNames.add("CardField");
            fieldNames.add("IsActiv");
            fieldNames.add("SortField");
            fieldNames.add("IsDescending");
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
        if (name.equals("NavigatorLayout"))
        {
            return getNavigatorLayout();
        }
        if (name.equals("CardField"))
        {
            return getCardField();
        }
        if (name.equals("IsActiv"))
        {
            return getIsActiv();
        }
        if (name.equals("SortField"))
        {
            return getSortField();
        }
        if (name.equals("IsDescending"))
        {
            return getIsDescending();
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
        if (name.equals("NavigatorLayout"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNavigatorLayout((Integer) value);
            return true;
        }
        if (name.equals("CardField"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCardField((Integer) value);
            return true;
        }
        if (name.equals("IsActiv"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsActiv((String) value);
            return true;
        }
        if (name.equals("SortField"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSortField((Integer) value);
            return true;
        }
        if (name.equals("IsDescending"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsDescending((String) value);
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
        if (name.equals(TCardGroupingFieldPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TCardGroupingFieldPeer.NAVIGATORLAYOUT))
        {
            return getNavigatorLayout();
        }
        if (name.equals(TCardGroupingFieldPeer.CARDFIELD))
        {
            return getCardField();
        }
        if (name.equals(TCardGroupingFieldPeer.ISACTIV))
        {
            return getIsActiv();
        }
        if (name.equals(TCardGroupingFieldPeer.SORTFIELD))
        {
            return getSortField();
        }
        if (name.equals(TCardGroupingFieldPeer.ISDESCENDING))
        {
            return getIsDescending();
        }
        if (name.equals(TCardGroupingFieldPeer.TPUUID))
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
      if (TCardGroupingFieldPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TCardGroupingFieldPeer.NAVIGATORLAYOUT.equals(name))
        {
            return setByName("NavigatorLayout", value);
        }
      if (TCardGroupingFieldPeer.CARDFIELD.equals(name))
        {
            return setByName("CardField", value);
        }
      if (TCardGroupingFieldPeer.ISACTIV.equals(name))
        {
            return setByName("IsActiv", value);
        }
      if (TCardGroupingFieldPeer.SORTFIELD.equals(name))
        {
            return setByName("SortField", value);
        }
      if (TCardGroupingFieldPeer.ISDESCENDING.equals(name))
        {
            return setByName("IsDescending", value);
        }
      if (TCardGroupingFieldPeer.TPUUID.equals(name))
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
            return getNavigatorLayout();
        }
        if (pos == 2)
        {
            return getCardField();
        }
        if (pos == 3)
        {
            return getIsActiv();
        }
        if (pos == 4)
        {
            return getSortField();
        }
        if (pos == 5)
        {
            return getIsDescending();
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
            return setByName("NavigatorLayout", value);
        }
    if (position == 2)
        {
            return setByName("CardField", value);
        }
    if (position == 3)
        {
            return setByName("IsActiv", value);
        }
    if (position == 4)
        {
            return setByName("SortField", value);
        }
    if (position == 5)
        {
            return setByName("IsDescending", value);
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
        save(TCardGroupingFieldPeer.DATABASE_NAME);
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
                    TCardGroupingFieldPeer.doInsert((TCardGroupingField) this, con);
                    setNew(false);
                }
                else
                {
                    TCardGroupingFieldPeer.doUpdate((TCardGroupingField) this, con);
                }
            }


            if (collTCardFieldOptions != null)
            {
                for (int i = 0; i < collTCardFieldOptions.size(); i++)
                {
                    ((TCardFieldOption) collTCardFieldOptions.get(i)).save(con);
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
    public TCardGroupingField copy() throws TorqueException
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
    public TCardGroupingField copy(Connection con) throws TorqueException
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
    public TCardGroupingField copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TCardGroupingField(), deepcopy);
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
    public TCardGroupingField copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TCardGroupingField(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TCardGroupingField copyInto(TCardGroupingField copyObj) throws TorqueException
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
    protected TCardGroupingField copyInto(TCardGroupingField copyObj, Connection con) throws TorqueException
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
    protected TCardGroupingField copyInto(TCardGroupingField copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setNavigatorLayout(navigatorLayout);
        copyObj.setCardField(cardField);
        copyObj.setIsActiv(isActiv);
        copyObj.setSortField(sortField);
        copyObj.setIsDescending(isDescending);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TCardFieldOption> vTCardFieldOptions = getTCardFieldOptions();
        if (vTCardFieldOptions != null)
        {
            for (int i = 0; i < vTCardFieldOptions.size(); i++)
            {
                TCardFieldOption obj =  vTCardFieldOptions.get(i);
                copyObj.addTCardFieldOption(obj.copy());
            }
        }
        else
        {
            copyObj.collTCardFieldOptions = null;
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
    protected TCardGroupingField copyInto(TCardGroupingField copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setNavigatorLayout(navigatorLayout);
        copyObj.setCardField(cardField);
        copyObj.setIsActiv(isActiv);
        copyObj.setSortField(sortField);
        copyObj.setIsDescending(isDescending);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TCardFieldOption> vTCardFieldOptions = getTCardFieldOptions(con);
        if (vTCardFieldOptions != null)
        {
            for (int i = 0; i < vTCardFieldOptions.size(); i++)
            {
                TCardFieldOption obj =  vTCardFieldOptions.get(i);
                copyObj.addTCardFieldOption(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTCardFieldOptions = null;
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
    public TCardGroupingFieldPeer getPeer()
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
        return TCardGroupingFieldPeer.getTableMap();
    }

  
    /**
     * Creates a TCardGroupingFieldBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TCardGroupingFieldBean with the contents of this object
     */
    public TCardGroupingFieldBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TCardGroupingFieldBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TCardGroupingFieldBean with the contents of this object
     */
    public TCardGroupingFieldBean getBean(IdentityMap createdBeans)
    {
        TCardGroupingFieldBean result = (TCardGroupingFieldBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TCardGroupingFieldBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setNavigatorLayout(getNavigatorLayout());
        result.setCardField(getCardField());
        result.setIsActiv(getIsActiv());
        result.setSortField(getSortField());
        result.setIsDescending(getIsDescending());
        result.setUuid(getUuid());



        if (collTCardFieldOptions != null)
        {
            List<TCardFieldOptionBean> relatedBeans = new ArrayList<TCardFieldOptionBean>(collTCardFieldOptions.size());
            for (Iterator<TCardFieldOption> collTCardFieldOptionsIt = collTCardFieldOptions.iterator(); collTCardFieldOptionsIt.hasNext(); )
            {
                TCardFieldOption related = (TCardFieldOption) collTCardFieldOptionsIt.next();
                TCardFieldOptionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTCardFieldOptionBeans(relatedBeans);
        }




        if (aTNavigatorLayout != null)
        {
            TNavigatorLayoutBean relatedBean = aTNavigatorLayout.getBean(createdBeans);
            result.setTNavigatorLayoutBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TCardGroupingField with the contents
     * of a TCardGroupingFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TCardGroupingFieldBean which contents are used to create
     *        the resulting class
     * @return an instance of TCardGroupingField with the contents of bean
     */
    public static TCardGroupingField createTCardGroupingField(TCardGroupingFieldBean bean)
        throws TorqueException
    {
        return createTCardGroupingField(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TCardGroupingField with the contents
     * of a TCardGroupingFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TCardGroupingFieldBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TCardGroupingField with the contents of bean
     */

    public static TCardGroupingField createTCardGroupingField(TCardGroupingFieldBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TCardGroupingField result = (TCardGroupingField) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TCardGroupingField();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setNavigatorLayout(bean.getNavigatorLayout());
        result.setCardField(bean.getCardField());
        result.setIsActiv(bean.getIsActiv());
        result.setSortField(bean.getSortField());
        result.setIsDescending(bean.getIsDescending());
        result.setUuid(bean.getUuid());



        {
            List<TCardFieldOptionBean> relatedBeans = bean.getTCardFieldOptionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TCardFieldOptionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TCardFieldOptionBean relatedBean =  relatedBeansIt.next();
                    TCardFieldOption related = TCardFieldOption.createTCardFieldOption(relatedBean, createdObjects);
                    result.addTCardFieldOptionFromBean(related);
                }
            }
        }




        {
            TNavigatorLayoutBean relatedBean = bean.getTNavigatorLayoutBean();
            if (relatedBean != null)
            {
                TNavigatorLayout relatedObject = TNavigatorLayout.createTNavigatorLayout(relatedBean, createdObjects);
                result.setTNavigatorLayout(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TCardFieldOption object to this object.
     * through the TCardFieldOption foreign key attribute
     *
     * @param toAdd TCardFieldOption
     */
    protected void addTCardFieldOptionFromBean(TCardFieldOption toAdd)
    {
        initTCardFieldOptions();
        collTCardFieldOptions.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TCardGroupingField:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("NavigatorLayout = ")
           .append(getNavigatorLayout())
           .append("\n");
        str.append("CardField = ")
           .append(getCardField())
           .append("\n");
        str.append("IsActiv = ")
           .append(getIsActiv())
           .append("\n");
        str.append("SortField = ")
           .append(getSortField())
           .append("\n");
        str.append("IsDescending = ")
           .append(getIsDescending())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
