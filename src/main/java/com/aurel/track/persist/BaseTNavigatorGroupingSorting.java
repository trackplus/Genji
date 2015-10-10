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
import com.aurel.track.beans.TNavigatorGroupingSortingBean;
import com.aurel.track.beans.TNavigatorLayoutBean;



/**
 * grouping and sorting in item navigator
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TNavigatorGroupingSorting
 */
public abstract class BaseTNavigatorGroupingSorting extends TpBaseObject
{
    /** The Peer class */
    private static final TNavigatorGroupingSortingPeer peer =
        new TNavigatorGroupingSortingPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the navigatorLayout field */
    private Integer navigatorLayout;

    /** The value for the field field */
    private Integer field;

    /** The value for the sortPosition field */
    private Integer sortPosition;

    /** The value for the isGrouping field */
    private String isGrouping = "N";

    /** The value for the isDescending field */
    private String isDescending = "N";

    /** The value for the isCollapsed field */
    private String isCollapsed = "N";

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
    public void setObjectID(Integer v) 
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
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
     * Get the Field
     *
     * @return Integer
     */
    public Integer getField()
    {
        return field;
    }


    /**
     * Set the value of Field
     *
     * @param v new value
     */
    public void setField(Integer v) 
    {

        if (!ObjectUtils.equals(this.field, v))
        {
            this.field = v;
            setModified(true);
        }


    }

    /**
     * Get the SortPosition
     *
     * @return Integer
     */
    public Integer getSortPosition()
    {
        return sortPosition;
    }


    /**
     * Set the value of SortPosition
     *
     * @param v new value
     */
    public void setSortPosition(Integer v) 
    {

        if (!ObjectUtils.equals(this.sortPosition, v))
        {
            this.sortPosition = v;
            setModified(true);
        }


    }

    /**
     * Get the IsGrouping
     *
     * @return String
     */
    public String getIsGrouping()
    {
        return isGrouping;
    }


    /**
     * Set the value of IsGrouping
     *
     * @param v new value
     */
    public void setIsGrouping(String v) 
    {

        if (!ObjectUtils.equals(this.isGrouping, v))
        {
            this.isGrouping = v;
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
     * Get the IsCollapsed
     *
     * @return String
     */
    public String getIsCollapsed()
    {
        return isCollapsed;
    }


    /**
     * Set the value of IsCollapsed
     *
     * @param v new value
     */
    public void setIsCollapsed(String v) 
    {

        if (!ObjectUtils.equals(this.isCollapsed, v))
        {
            this.isCollapsed = v;
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
            fieldNames.add("Field");
            fieldNames.add("SortPosition");
            fieldNames.add("IsGrouping");
            fieldNames.add("IsDescending");
            fieldNames.add("IsCollapsed");
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
        if (name.equals("Field"))
        {
            return getField();
        }
        if (name.equals("SortPosition"))
        {
            return getSortPosition();
        }
        if (name.equals("IsGrouping"))
        {
            return getIsGrouping();
        }
        if (name.equals("IsDescending"))
        {
            return getIsDescending();
        }
        if (name.equals("IsCollapsed"))
        {
            return getIsCollapsed();
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
        if (name.equals("Field"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setField((Integer) value);
            return true;
        }
        if (name.equals("SortPosition"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSortPosition((Integer) value);
            return true;
        }
        if (name.equals("IsGrouping"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsGrouping((String) value);
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
        if (name.equals("IsCollapsed"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsCollapsed((String) value);
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
        if (name.equals(TNavigatorGroupingSortingPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TNavigatorGroupingSortingPeer.NAVIGATORLAYOUT))
        {
            return getNavigatorLayout();
        }
        if (name.equals(TNavigatorGroupingSortingPeer.FIELD))
        {
            return getField();
        }
        if (name.equals(TNavigatorGroupingSortingPeer.SORTPOSITION))
        {
            return getSortPosition();
        }
        if (name.equals(TNavigatorGroupingSortingPeer.ISGROUPING))
        {
            return getIsGrouping();
        }
        if (name.equals(TNavigatorGroupingSortingPeer.ISDESCENDING))
        {
            return getIsDescending();
        }
        if (name.equals(TNavigatorGroupingSortingPeer.ISCOLLAPSED))
        {
            return getIsCollapsed();
        }
        if (name.equals(TNavigatorGroupingSortingPeer.TPUUID))
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
      if (TNavigatorGroupingSortingPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TNavigatorGroupingSortingPeer.NAVIGATORLAYOUT.equals(name))
        {
            return setByName("NavigatorLayout", value);
        }
      if (TNavigatorGroupingSortingPeer.FIELD.equals(name))
        {
            return setByName("Field", value);
        }
      if (TNavigatorGroupingSortingPeer.SORTPOSITION.equals(name))
        {
            return setByName("SortPosition", value);
        }
      if (TNavigatorGroupingSortingPeer.ISGROUPING.equals(name))
        {
            return setByName("IsGrouping", value);
        }
      if (TNavigatorGroupingSortingPeer.ISDESCENDING.equals(name))
        {
            return setByName("IsDescending", value);
        }
      if (TNavigatorGroupingSortingPeer.ISCOLLAPSED.equals(name))
        {
            return setByName("IsCollapsed", value);
        }
      if (TNavigatorGroupingSortingPeer.TPUUID.equals(name))
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
            return getField();
        }
        if (pos == 3)
        {
            return getSortPosition();
        }
        if (pos == 4)
        {
            return getIsGrouping();
        }
        if (pos == 5)
        {
            return getIsDescending();
        }
        if (pos == 6)
        {
            return getIsCollapsed();
        }
        if (pos == 7)
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
            return setByName("Field", value);
        }
    if (position == 3)
        {
            return setByName("SortPosition", value);
        }
    if (position == 4)
        {
            return setByName("IsGrouping", value);
        }
    if (position == 5)
        {
            return setByName("IsDescending", value);
        }
    if (position == 6)
        {
            return setByName("IsCollapsed", value);
        }
    if (position == 7)
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
        save(TNavigatorGroupingSortingPeer.DATABASE_NAME);
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
                    TNavigatorGroupingSortingPeer.doInsert((TNavigatorGroupingSorting) this, con);
                    setNew(false);
                }
                else
                {
                    TNavigatorGroupingSortingPeer.doUpdate((TNavigatorGroupingSorting) this, con);
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
        
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) 
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
    public TNavigatorGroupingSorting copy() throws TorqueException
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
    public TNavigatorGroupingSorting copy(Connection con) throws TorqueException
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
    public TNavigatorGroupingSorting copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TNavigatorGroupingSorting(), deepcopy);
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
    public TNavigatorGroupingSorting copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TNavigatorGroupingSorting(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TNavigatorGroupingSorting copyInto(TNavigatorGroupingSorting copyObj) throws TorqueException
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
    protected TNavigatorGroupingSorting copyInto(TNavigatorGroupingSorting copyObj, Connection con) throws TorqueException
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
    protected TNavigatorGroupingSorting copyInto(TNavigatorGroupingSorting copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setNavigatorLayout(navigatorLayout);
        copyObj.setField(field);
        copyObj.setSortPosition(sortPosition);
        copyObj.setIsGrouping(isGrouping);
        copyObj.setIsDescending(isDescending);
        copyObj.setIsCollapsed(isCollapsed);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {
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
    protected TNavigatorGroupingSorting copyInto(TNavigatorGroupingSorting copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setNavigatorLayout(navigatorLayout);
        copyObj.setField(field);
        copyObj.setSortPosition(sortPosition);
        copyObj.setIsGrouping(isGrouping);
        copyObj.setIsDescending(isDescending);
        copyObj.setIsCollapsed(isCollapsed);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TNavigatorGroupingSortingPeer getPeer()
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
        return TNavigatorGroupingSortingPeer.getTableMap();
    }

  
    /**
     * Creates a TNavigatorGroupingSortingBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TNavigatorGroupingSortingBean with the contents of this object
     */
    public TNavigatorGroupingSortingBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TNavigatorGroupingSortingBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TNavigatorGroupingSortingBean with the contents of this object
     */
    public TNavigatorGroupingSortingBean getBean(IdentityMap createdBeans)
    {
        TNavigatorGroupingSortingBean result = (TNavigatorGroupingSortingBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TNavigatorGroupingSortingBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setNavigatorLayout(getNavigatorLayout());
        result.setField(getField());
        result.setSortPosition(getSortPosition());
        result.setIsGrouping(getIsGrouping());
        result.setIsDescending(getIsDescending());
        result.setIsCollapsed(getIsCollapsed());
        result.setUuid(getUuid());





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
     * Creates an instance of TNavigatorGroupingSorting with the contents
     * of a TNavigatorGroupingSortingBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TNavigatorGroupingSortingBean which contents are used to create
     *        the resulting class
     * @return an instance of TNavigatorGroupingSorting with the contents of bean
     */
    public static TNavigatorGroupingSorting createTNavigatorGroupingSorting(TNavigatorGroupingSortingBean bean)
        throws TorqueException
    {
        return createTNavigatorGroupingSorting(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TNavigatorGroupingSorting with the contents
     * of a TNavigatorGroupingSortingBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TNavigatorGroupingSortingBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TNavigatorGroupingSorting with the contents of bean
     */

    public static TNavigatorGroupingSorting createTNavigatorGroupingSorting(TNavigatorGroupingSortingBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TNavigatorGroupingSorting result = (TNavigatorGroupingSorting) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TNavigatorGroupingSorting();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setNavigatorLayout(bean.getNavigatorLayout());
        result.setField(bean.getField());
        result.setSortPosition(bean.getSortPosition());
        result.setIsGrouping(bean.getIsGrouping());
        result.setIsDescending(bean.getIsDescending());
        result.setIsCollapsed(bean.getIsCollapsed());
        result.setUuid(bean.getUuid());





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



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TNavigatorGroupingSorting:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("NavigatorLayout = ")
           .append(getNavigatorLayout())
           .append("\n");
        str.append("Field = ")
           .append(getField())
           .append("\n");
        str.append("SortPosition = ")
           .append(getSortPosition())
           .append("\n");
        str.append("IsGrouping = ")
           .append(getIsGrouping())
           .append("\n");
        str.append("IsDescending = ")
           .append(getIsDescending())
           .append("\n");
        str.append("IsCollapsed = ")
           .append(getIsCollapsed())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
