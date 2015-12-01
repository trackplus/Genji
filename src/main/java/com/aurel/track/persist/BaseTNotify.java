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



import com.aurel.track.persist.TProjectCategory;
import com.aurel.track.persist.TProjectCategoryPeer;
import com.aurel.track.persist.TState;
import com.aurel.track.persist.TStatePeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TProjectCategoryBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TNotify
 */
public abstract class BaseTNotify extends TpBaseObject
{
    /** The Peer class */
    private static final TNotifyPeer peer =
        new TNotifyPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the projectCategoryID field */
    private Integer projectCategoryID;

    /** The value for the stateID field */
    private Integer stateID;

    /** The value for the personID field */
    private Integer personID;

    /** The value for the workItem field */
    private Integer workItem;

    /** The value for the raciRole field */
    private String raciRole;

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
     * Get the ProjectCategoryID
     *
     * @return Integer
     */
    public Integer getProjectCategoryID()
    {
        return projectCategoryID;
    }


    /**
     * Set the value of ProjectCategoryID
     *
     * @param v new value
     */
    public void setProjectCategoryID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.projectCategoryID, v))
        {
            this.projectCategoryID = v;
            setModified(true);
        }


        if (aTProjectCategory != null && !ObjectUtils.equals(aTProjectCategory.getObjectID(), v))
        {
            aTProjectCategory = null;
        }

    }

    /**
     * Get the StateID
     *
     * @return Integer
     */
    public Integer getStateID()
    {
        return stateID;
    }


    /**
     * Set the value of StateID
     *
     * @param v new value
     */
    public void setStateID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.stateID, v))
        {
            this.stateID = v;
            setModified(true);
        }


        if (aTState != null && !ObjectUtils.equals(aTState.getObjectID(), v))
        {
            aTState = null;
        }

    }

    /**
     * Get the PersonID
     *
     * @return Integer
     */
    public Integer getPersonID()
    {
        return personID;
    }


    /**
     * Set the value of PersonID
     *
     * @param v new value
     */
    public void setPersonID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.personID, v))
        {
            this.personID = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
        }

    }

    /**
     * Get the WorkItem
     *
     * @return Integer
     */
    public Integer getWorkItem()
    {
        return workItem;
    }


    /**
     * Set the value of WorkItem
     *
     * @param v new value
     */
    public void setWorkItem(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workItem, v))
        {
            this.workItem = v;
            setModified(true);
        }


        if (aTWorkItem != null && !ObjectUtils.equals(aTWorkItem.getObjectID(), v))
        {
            aTWorkItem = null;
        }

    }

    /**
     * Get the RaciRole
     *
     * @return String
     */
    public String getRaciRole()
    {
        return raciRole;
    }


    /**
     * Set the value of RaciRole
     *
     * @param v new value
     */
    public void setRaciRole(String v) 
    {

        if (!ObjectUtils.equals(this.raciRole, v))
        {
            this.raciRole = v;
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

    



    private TProjectCategory aTProjectCategory;

    /**
     * Declares an association between this object and a TProjectCategory object
     *
     * @param v TProjectCategory
     * @throws TorqueException
     */
    public void setTProjectCategory(TProjectCategory v) throws TorqueException
    {
        if (v == null)
        {
            setProjectCategoryID((Integer) null);
        }
        else
        {
            setProjectCategoryID(v.getObjectID());
        }
        aTProjectCategory = v;
    }


    /**
     * Returns the associated TProjectCategory object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProjectCategory object
     * @throws TorqueException
     */
    public TProjectCategory getTProjectCategory()
        throws TorqueException
    {
        if (aTProjectCategory == null && (!ObjectUtils.equals(this.projectCategoryID, null)))
        {
            aTProjectCategory = TProjectCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.projectCategoryID));
        }
        return aTProjectCategory;
    }

    /**
     * Return the associated TProjectCategory object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProjectCategory object
     * @throws TorqueException
     */
    public TProjectCategory getTProjectCategory(Connection connection)
        throws TorqueException
    {
        if (aTProjectCategory == null && (!ObjectUtils.equals(this.projectCategoryID, null)))
        {
            aTProjectCategory = TProjectCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.projectCategoryID), connection);
        }
        return aTProjectCategory;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectCategoryKey(ObjectKey key) throws TorqueException
    {

        setProjectCategoryID(new Integer(((NumberKey) key).intValue()));
    }




    private TState aTState;

    /**
     * Declares an association between this object and a TState object
     *
     * @param v TState
     * @throws TorqueException
     */
    public void setTState(TState v) throws TorqueException
    {
        if (v == null)
        {
            setStateID((Integer) null);
        }
        else
        {
            setStateID(v.getObjectID());
        }
        aTState = v;
    }


    /**
     * Returns the associated TState object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TState object
     * @throws TorqueException
     */
    public TState getTState()
        throws TorqueException
    {
        if (aTState == null && (!ObjectUtils.equals(this.stateID, null)))
        {
            aTState = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.stateID));
        }
        return aTState;
    }

    /**
     * Return the associated TState object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TState object
     * @throws TorqueException
     */
    public TState getTState(Connection connection)
        throws TorqueException
    {
        if (aTState == null && (!ObjectUtils.equals(this.stateID, null)))
        {
            aTState = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.stateID), connection);
        }
        return aTState;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTStateKey(ObjectKey key) throws TorqueException
    {

        setStateID(new Integer(((NumberKey) key).intValue()));
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
            setPersonID((Integer) null);
        }
        else
        {
            setPersonID(v.getObjectID());
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
        if (aTPerson == null && (!ObjectUtils.equals(this.personID, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.personID));
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
        if (aTPerson == null && (!ObjectUtils.equals(this.personID, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.personID), connection);
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

        setPersonID(new Integer(((NumberKey) key).intValue()));
    }




    private TWorkItem aTWorkItem;

    /**
     * Declares an association between this object and a TWorkItem object
     *
     * @param v TWorkItem
     * @throws TorqueException
     */
    public void setTWorkItem(TWorkItem v) throws TorqueException
    {
        if (v == null)
        {
            setWorkItem((Integer) null);
        }
        else
        {
            setWorkItem(v.getObjectID());
        }
        aTWorkItem = v;
    }


    /**
     * Returns the associated TWorkItem object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItem()
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItem));
        }
        return aTWorkItem;
    }

    /**
     * Return the associated TWorkItem object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItem(Connection connection)
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workItem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workItem), connection);
        }
        return aTWorkItem;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkItemKey(ObjectKey key) throws TorqueException
    {

        setWorkItem(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("ProjectCategoryID");
            fieldNames.add("StateID");
            fieldNames.add("PersonID");
            fieldNames.add("WorkItem");
            fieldNames.add("RaciRole");
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
        if (name.equals("ProjectCategoryID"))
        {
            return getProjectCategoryID();
        }
        if (name.equals("StateID"))
        {
            return getStateID();
        }
        if (name.equals("PersonID"))
        {
            return getPersonID();
        }
        if (name.equals("WorkItem"))
        {
            return getWorkItem();
        }
        if (name.equals("RaciRole"))
        {
            return getRaciRole();
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
        if (name.equals("ProjectCategoryID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProjectCategoryID((Integer) value);
            return true;
        }
        if (name.equals("StateID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStateID((Integer) value);
            return true;
        }
        if (name.equals("PersonID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPersonID((Integer) value);
            return true;
        }
        if (name.equals("WorkItem"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkItem((Integer) value);
            return true;
        }
        if (name.equals("RaciRole"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRaciRole((String) value);
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
        if (name.equals(TNotifyPeer.PKEY))
        {
            return getObjectID();
        }
        if (name.equals(TNotifyPeer.PROJCATKEY))
        {
            return getProjectCategoryID();
        }
        if (name.equals(TNotifyPeer.STATEKEY))
        {
            return getStateID();
        }
        if (name.equals(TNotifyPeer.PERSONKEY))
        {
            return getPersonID();
        }
        if (name.equals(TNotifyPeer.WORKITEM))
        {
            return getWorkItem();
        }
        if (name.equals(TNotifyPeer.RACIROLE))
        {
            return getRaciRole();
        }
        if (name.equals(TNotifyPeer.TPUUID))
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
      if (TNotifyPeer.PKEY.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TNotifyPeer.PROJCATKEY.equals(name))
        {
            return setByName("ProjectCategoryID", value);
        }
      if (TNotifyPeer.STATEKEY.equals(name))
        {
            return setByName("StateID", value);
        }
      if (TNotifyPeer.PERSONKEY.equals(name))
        {
            return setByName("PersonID", value);
        }
      if (TNotifyPeer.WORKITEM.equals(name))
        {
            return setByName("WorkItem", value);
        }
      if (TNotifyPeer.RACIROLE.equals(name))
        {
            return setByName("RaciRole", value);
        }
      if (TNotifyPeer.TPUUID.equals(name))
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
            return getProjectCategoryID();
        }
        if (pos == 2)
        {
            return getStateID();
        }
        if (pos == 3)
        {
            return getPersonID();
        }
        if (pos == 4)
        {
            return getWorkItem();
        }
        if (pos == 5)
        {
            return getRaciRole();
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
            return setByName("ProjectCategoryID", value);
        }
    if (position == 2)
        {
            return setByName("StateID", value);
        }
    if (position == 3)
        {
            return setByName("PersonID", value);
        }
    if (position == 4)
        {
            return setByName("WorkItem", value);
        }
    if (position == 5)
        {
            return setByName("RaciRole", value);
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
        save(TNotifyPeer.DATABASE_NAME);
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
                    TNotifyPeer.doInsert((TNotify) this, con);
                    setNew(false);
                }
                else
                {
                    TNotifyPeer.doUpdate((TNotify) this, con);
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
    public TNotify copy() throws TorqueException
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
    public TNotify copy(Connection con) throws TorqueException
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
    public TNotify copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TNotify(), deepcopy);
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
    public TNotify copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TNotify(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TNotify copyInto(TNotify copyObj) throws TorqueException
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
    protected TNotify copyInto(TNotify copyObj, Connection con) throws TorqueException
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
    protected TNotify copyInto(TNotify copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setProjectCategoryID(projectCategoryID);
        copyObj.setStateID(stateID);
        copyObj.setPersonID(personID);
        copyObj.setWorkItem(workItem);
        copyObj.setRaciRole(raciRole);
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
    protected TNotify copyInto(TNotify copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setProjectCategoryID(projectCategoryID);
        copyObj.setStateID(stateID);
        copyObj.setPersonID(personID);
        copyObj.setWorkItem(workItem);
        copyObj.setRaciRole(raciRole);
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
    public TNotifyPeer getPeer()
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
        return TNotifyPeer.getTableMap();
    }

  
    /**
     * Creates a TNotifyBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TNotifyBean with the contents of this object
     */
    public TNotifyBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TNotifyBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TNotifyBean with the contents of this object
     */
    public TNotifyBean getBean(IdentityMap createdBeans)
    {
        TNotifyBean result = (TNotifyBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TNotifyBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setProjectCategoryID(getProjectCategoryID());
        result.setStateID(getStateID());
        result.setPersonID(getPersonID());
        result.setWorkItem(getWorkItem());
        result.setRaciRole(getRaciRole());
        result.setUuid(getUuid());





        if (aTProjectCategory != null)
        {
            TProjectCategoryBean relatedBean = aTProjectCategory.getBean(createdBeans);
            result.setTProjectCategoryBean(relatedBean);
        }



        if (aTState != null)
        {
            TStateBean relatedBean = aTState.getBean(createdBeans);
            result.setTStateBean(relatedBean);
        }



        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTWorkItem != null)
        {
            TWorkItemBean relatedBean = aTWorkItem.getBean(createdBeans);
            result.setTWorkItemBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TNotify with the contents
     * of a TNotifyBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TNotifyBean which contents are used to create
     *        the resulting class
     * @return an instance of TNotify with the contents of bean
     */
    public static TNotify createTNotify(TNotifyBean bean)
        throws TorqueException
    {
        return createTNotify(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TNotify with the contents
     * of a TNotifyBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TNotifyBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TNotify with the contents of bean
     */

    public static TNotify createTNotify(TNotifyBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TNotify result = (TNotify) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TNotify();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setProjectCategoryID(bean.getProjectCategoryID());
        result.setStateID(bean.getStateID());
        result.setPersonID(bean.getPersonID());
        result.setWorkItem(bean.getWorkItem());
        result.setRaciRole(bean.getRaciRole());
        result.setUuid(bean.getUuid());





        {
            TProjectCategoryBean relatedBean = bean.getTProjectCategoryBean();
            if (relatedBean != null)
            {
                TProjectCategory relatedObject = TProjectCategory.createTProjectCategory(relatedBean, createdObjects);
                result.setTProjectCategory(relatedObject);
            }
        }



        {
            TStateBean relatedBean = bean.getTStateBean();
            if (relatedBean != null)
            {
                TState relatedObject = TState.createTState(relatedBean, createdObjects);
                result.setTState(relatedObject);
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



        {
            TWorkItemBean relatedBean = bean.getTWorkItemBean();
            if (relatedBean != null)
            {
                TWorkItem relatedObject = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                result.setTWorkItem(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TNotify:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("ProjectCategoryID = ")
           .append(getProjectCategoryID())
           .append("\n");
        str.append("StateID = ")
           .append(getStateID())
           .append("\n");
        str.append("PersonID = ")
           .append(getPersonID())
           .append("\n");
        str.append("WorkItem = ")
           .append(getWorkItem())
           .append("\n");
        str.append("RaciRole = ")
           .append(getRaciRole())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
