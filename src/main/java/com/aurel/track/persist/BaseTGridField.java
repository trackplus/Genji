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



import com.aurel.track.persist.TGridLayout;
import com.aurel.track.persist.TGridLayoutPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TGridFieldBean;
import com.aurel.track.beans.TGridLayoutBean;



/**
 * fields in grids
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TGridField
 */
public abstract class BaseTGridField extends TpBaseObject
{
    /** The Peer class */
    private static final TGridFieldPeer peer =
        new TGridFieldPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the gridLayout field */
    private Integer gridLayout;

    /** The value for the gridField field */
    private Integer gridField;

    /** The value for the fieldPosition field */
    private Integer fieldPosition;

    /** The value for the fieldWidth field */
    private Integer fieldWidth;

    /** The value for the filterString field */
    private String filterString;

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
     * Get the GridLayout
     *
     * @return Integer
     */
    public Integer getGridLayout()
    {
        return gridLayout;
    }


    /**
     * Set the value of GridLayout
     *
     * @param v new value
     */
    public void setGridLayout(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.gridLayout, v))
        {
            this.gridLayout = v;
            setModified(true);
        }


        if (aTGridLayout != null && !ObjectUtils.equals(aTGridLayout.getObjectID(), v))
        {
            aTGridLayout = null;
        }

    }

    /**
     * Get the GridField
     *
     * @return Integer
     */
    public Integer getGridField()
    {
        return gridField;
    }


    /**
     * Set the value of GridField
     *
     * @param v new value
     */
    public void setGridField(Integer v) 
    {

        if (!ObjectUtils.equals(this.gridField, v))
        {
            this.gridField = v;
            setModified(true);
        }


    }

    /**
     * Get the FieldPosition
     *
     * @return Integer
     */
    public Integer getFieldPosition()
    {
        return fieldPosition;
    }


    /**
     * Set the value of FieldPosition
     *
     * @param v new value
     */
    public void setFieldPosition(Integer v) 
    {

        if (!ObjectUtils.equals(this.fieldPosition, v))
        {
            this.fieldPosition = v;
            setModified(true);
        }


    }

    /**
     * Get the FieldWidth
     *
     * @return Integer
     */
    public Integer getFieldWidth()
    {
        return fieldWidth;
    }


    /**
     * Set the value of FieldWidth
     *
     * @param v new value
     */
    public void setFieldWidth(Integer v) 
    {

        if (!ObjectUtils.equals(this.fieldWidth, v))
        {
            this.fieldWidth = v;
            setModified(true);
        }


    }

    /**
     * Get the FilterString
     *
     * @return String
     */
    public String getFilterString()
    {
        return filterString;
    }


    /**
     * Set the value of FilterString
     *
     * @param v new value
     */
    public void setFilterString(String v) 
    {

        if (!ObjectUtils.equals(this.filterString, v))
        {
            this.filterString = v;
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

    



    private TGridLayout aTGridLayout;

    /**
     * Declares an association between this object and a TGridLayout object
     *
     * @param v TGridLayout
     * @throws TorqueException
     */
    public void setTGridLayout(TGridLayout v) throws TorqueException
    {
        if (v == null)
        {
            setGridLayout((Integer) null);
        }
        else
        {
            setGridLayout(v.getObjectID());
        }
        aTGridLayout = v;
    }


    /**
     * Returns the associated TGridLayout object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TGridLayout object
     * @throws TorqueException
     */
    public TGridLayout getTGridLayout()
        throws TorqueException
    {
        if (aTGridLayout == null && (!ObjectUtils.equals(this.gridLayout, null)))
        {
            aTGridLayout = TGridLayoutPeer.retrieveByPK(SimpleKey.keyFor(this.gridLayout));
        }
        return aTGridLayout;
    }

    /**
     * Return the associated TGridLayout object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TGridLayout object
     * @throws TorqueException
     */
    public TGridLayout getTGridLayout(Connection connection)
        throws TorqueException
    {
        if (aTGridLayout == null && (!ObjectUtils.equals(this.gridLayout, null)))
        {
            aTGridLayout = TGridLayoutPeer.retrieveByPK(SimpleKey.keyFor(this.gridLayout), connection);
        }
        return aTGridLayout;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTGridLayoutKey(ObjectKey key) throws TorqueException
    {

        setGridLayout(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("GridLayout");
            fieldNames.add("GridField");
            fieldNames.add("FieldPosition");
            fieldNames.add("FieldWidth");
            fieldNames.add("FilterString");
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
        if (name.equals("GridLayout"))
        {
            return getGridLayout();
        }
        if (name.equals("GridField"))
        {
            return getGridField();
        }
        if (name.equals("FieldPosition"))
        {
            return getFieldPosition();
        }
        if (name.equals("FieldWidth"))
        {
            return getFieldWidth();
        }
        if (name.equals("FilterString"))
        {
            return getFilterString();
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
        if (name.equals("GridLayout"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setGridLayout((Integer) value);
            return true;
        }
        if (name.equals("GridField"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setGridField((Integer) value);
            return true;
        }
        if (name.equals("FieldPosition"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldPosition((Integer) value);
            return true;
        }
        if (name.equals("FieldWidth"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldWidth((Integer) value);
            return true;
        }
        if (name.equals("FilterString"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFilterString((String) value);
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
        if (name.equals(TGridFieldPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TGridFieldPeer.GRIDLAYOUT))
        {
            return getGridLayout();
        }
        if (name.equals(TGridFieldPeer.GRIDFIELD))
        {
            return getGridField();
        }
        if (name.equals(TGridFieldPeer.FIELDPOSITION))
        {
            return getFieldPosition();
        }
        if (name.equals(TGridFieldPeer.FIELDWIDTH))
        {
            return getFieldWidth();
        }
        if (name.equals(TGridFieldPeer.FILTERSTRING))
        {
            return getFilterString();
        }
        if (name.equals(TGridFieldPeer.TPUUID))
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
      if (TGridFieldPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TGridFieldPeer.GRIDLAYOUT.equals(name))
        {
            return setByName("GridLayout", value);
        }
      if (TGridFieldPeer.GRIDFIELD.equals(name))
        {
            return setByName("GridField", value);
        }
      if (TGridFieldPeer.FIELDPOSITION.equals(name))
        {
            return setByName("FieldPosition", value);
        }
      if (TGridFieldPeer.FIELDWIDTH.equals(name))
        {
            return setByName("FieldWidth", value);
        }
      if (TGridFieldPeer.FILTERSTRING.equals(name))
        {
            return setByName("FilterString", value);
        }
      if (TGridFieldPeer.TPUUID.equals(name))
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
            return getGridLayout();
        }
        if (pos == 2)
        {
            return getGridField();
        }
        if (pos == 3)
        {
            return getFieldPosition();
        }
        if (pos == 4)
        {
            return getFieldWidth();
        }
        if (pos == 5)
        {
            return getFilterString();
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
            return setByName("GridLayout", value);
        }
    if (position == 2)
        {
            return setByName("GridField", value);
        }
    if (position == 3)
        {
            return setByName("FieldPosition", value);
        }
    if (position == 4)
        {
            return setByName("FieldWidth", value);
        }
    if (position == 5)
        {
            return setByName("FilterString", value);
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
        save(TGridFieldPeer.DATABASE_NAME);
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
                    TGridFieldPeer.doInsert((TGridField) this, con);
                    setNew(false);
                }
                else
                {
                    TGridFieldPeer.doUpdate((TGridField) this, con);
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
    public TGridField copy() throws TorqueException
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
    public TGridField copy(Connection con) throws TorqueException
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
    public TGridField copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TGridField(), deepcopy);
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
    public TGridField copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TGridField(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TGridField copyInto(TGridField copyObj) throws TorqueException
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
    protected TGridField copyInto(TGridField copyObj, Connection con) throws TorqueException
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
    protected TGridField copyInto(TGridField copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setGridLayout(gridLayout);
        copyObj.setGridField(gridField);
        copyObj.setFieldPosition(fieldPosition);
        copyObj.setFieldWidth(fieldWidth);
        copyObj.setFilterString(filterString);
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
    protected TGridField copyInto(TGridField copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setGridLayout(gridLayout);
        copyObj.setGridField(gridField);
        copyObj.setFieldPosition(fieldPosition);
        copyObj.setFieldWidth(fieldWidth);
        copyObj.setFilterString(filterString);
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
    public TGridFieldPeer getPeer()
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
        return TGridFieldPeer.getTableMap();
    }

  
    /**
     * Creates a TGridFieldBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TGridFieldBean with the contents of this object
     */
    public TGridFieldBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TGridFieldBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TGridFieldBean with the contents of this object
     */
    public TGridFieldBean getBean(IdentityMap createdBeans)
    {
        TGridFieldBean result = (TGridFieldBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TGridFieldBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setGridLayout(getGridLayout());
        result.setGridField(getGridField());
        result.setFieldPosition(getFieldPosition());
        result.setFieldWidth(getFieldWidth());
        result.setFilterString(getFilterString());
        result.setUuid(getUuid());





        if (aTGridLayout != null)
        {
            TGridLayoutBean relatedBean = aTGridLayout.getBean(createdBeans);
            result.setTGridLayoutBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TGridField with the contents
     * of a TGridFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TGridFieldBean which contents are used to create
     *        the resulting class
     * @return an instance of TGridField with the contents of bean
     */
    public static TGridField createTGridField(TGridFieldBean bean)
        throws TorqueException
    {
        return createTGridField(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TGridField with the contents
     * of a TGridFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TGridFieldBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TGridField with the contents of bean
     */

    public static TGridField createTGridField(TGridFieldBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TGridField result = (TGridField) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TGridField();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setGridLayout(bean.getGridLayout());
        result.setGridField(bean.getGridField());
        result.setFieldPosition(bean.getFieldPosition());
        result.setFieldWidth(bean.getFieldWidth());
        result.setFilterString(bean.getFilterString());
        result.setUuid(bean.getUuid());





        {
            TGridLayoutBean relatedBean = bean.getTGridLayoutBean();
            if (relatedBean != null)
            {
                TGridLayout relatedObject = TGridLayout.createTGridLayout(relatedBean, createdObjects);
                result.setTGridLayout(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TGridField:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("GridLayout = ")
           .append(getGridLayout())
           .append("\n");
        str.append("GridField = ")
           .append(getGridField())
           .append("\n");
        str.append("FieldPosition = ")
           .append(getFieldPosition())
           .append("\n");
        str.append("FieldWidth = ")
           .append(getFieldWidth())
           .append("\n");
        str.append("FilterString = ")
           .append(getFilterString())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}