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
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TNotifyTrigger;
import com.aurel.track.persist.TNotifyTriggerPeer;
import com.aurel.track.persist.TQueryRepository;
import com.aurel.track.persist.TQueryRepositoryPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TNotifySettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TNotifyTriggerBean;
import com.aurel.track.beans.TQueryRepositoryBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TNotifySettings
 */
public abstract class BaseTNotifySettings extends TpBaseObject
{
    /** The Peer class */
    private static final TNotifySettingsPeer peer =
        new TNotifySettingsPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the person field */
    private Integer person;

    /** The value for the project field */
    private Integer project;

    /** The value for the notifyTrigger field */
    private Integer notifyTrigger;

    /** The value for the notifyFilter field */
    private Integer notifyFilter;

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
     * Get the Project
     *
     * @return Integer
     */
    public Integer getProject()
    {
        return project;
    }


    /**
     * Set the value of Project
     *
     * @param v new value
     */
    public void setProject(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.project, v))
        {
            this.project = v;
            setModified(true);
        }


        if (aTProject != null && !ObjectUtils.equals(aTProject.getObjectID(), v))
        {
            aTProject = null;
        }

    }

    /**
     * Get the NotifyTrigger
     *
     * @return Integer
     */
    public Integer getNotifyTrigger()
    {
        return notifyTrigger;
    }


    /**
     * Set the value of NotifyTrigger
     *
     * @param v new value
     */
    public void setNotifyTrigger(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.notifyTrigger, v))
        {
            this.notifyTrigger = v;
            setModified(true);
        }


        if (aTNotifyTrigger != null && !ObjectUtils.equals(aTNotifyTrigger.getObjectID(), v))
        {
            aTNotifyTrigger = null;
        }

    }

    /**
     * Get the NotifyFilter
     *
     * @return Integer
     */
    public Integer getNotifyFilter()
    {
        return notifyFilter;
    }


    /**
     * Set the value of NotifyFilter
     *
     * @param v new value
     */
    public void setNotifyFilter(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.notifyFilter, v))
        {
            this.notifyFilter = v;
            setModified(true);
        }


        if (aTQueryRepository != null && !ObjectUtils.equals(aTQueryRepository.getObjectID(), v))
        {
            aTQueryRepository = null;
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




    private TProject aTProject;

    /**
     * Declares an association between this object and a TProject object
     *
     * @param v TProject
     * @throws TorqueException
     */
    public void setTProject(TProject v) throws TorqueException
    {
        if (v == null)
        {
            setProject((Integer) null);
        }
        else
        {
            setProject(v.getObjectID());
        }
        aTProject = v;
    }


    /**
     * Returns the associated TProject object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProject object
     * @throws TorqueException
     */
    public TProject getTProject()
        throws TorqueException
    {
        if (aTProject == null && (!ObjectUtils.equals(this.project, null)))
        {
            aTProject = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.project));
        }
        return aTProject;
    }

    /**
     * Return the associated TProject object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProject object
     * @throws TorqueException
     */
    public TProject getTProject(Connection connection)
        throws TorqueException
    {
        if (aTProject == null && (!ObjectUtils.equals(this.project, null)))
        {
            aTProject = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.project), connection);
        }
        return aTProject;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectKey(ObjectKey key) throws TorqueException
    {

        setProject(new Integer(((NumberKey) key).intValue()));
    }




    private TNotifyTrigger aTNotifyTrigger;

    /**
     * Declares an association between this object and a TNotifyTrigger object
     *
     * @param v TNotifyTrigger
     * @throws TorqueException
     */
    public void setTNotifyTrigger(TNotifyTrigger v) throws TorqueException
    {
        if (v == null)
        {
            setNotifyTrigger((Integer) null);
        }
        else
        {
            setNotifyTrigger(v.getObjectID());
        }
        aTNotifyTrigger = v;
    }


    /**
     * Returns the associated TNotifyTrigger object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TNotifyTrigger object
     * @throws TorqueException
     */
    public TNotifyTrigger getTNotifyTrigger()
        throws TorqueException
    {
        if (aTNotifyTrigger == null && (!ObjectUtils.equals(this.notifyTrigger, null)))
        {
            aTNotifyTrigger = TNotifyTriggerPeer.retrieveByPK(SimpleKey.keyFor(this.notifyTrigger));
        }
        return aTNotifyTrigger;
    }

    /**
     * Return the associated TNotifyTrigger object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TNotifyTrigger object
     * @throws TorqueException
     */
    public TNotifyTrigger getTNotifyTrigger(Connection connection)
        throws TorqueException
    {
        if (aTNotifyTrigger == null && (!ObjectUtils.equals(this.notifyTrigger, null)))
        {
            aTNotifyTrigger = TNotifyTriggerPeer.retrieveByPK(SimpleKey.keyFor(this.notifyTrigger), connection);
        }
        return aTNotifyTrigger;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTNotifyTriggerKey(ObjectKey key) throws TorqueException
    {

        setNotifyTrigger(new Integer(((NumberKey) key).intValue()));
    }




    private TQueryRepository aTQueryRepository;

    /**
     * Declares an association between this object and a TQueryRepository object
     *
     * @param v TQueryRepository
     * @throws TorqueException
     */
    public void setTQueryRepository(TQueryRepository v) throws TorqueException
    {
        if (v == null)
        {
            setNotifyFilter((Integer) null);
        }
        else
        {
            setNotifyFilter(v.getObjectID());
        }
        aTQueryRepository = v;
    }


    /**
     * Returns the associated TQueryRepository object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TQueryRepository object
     * @throws TorqueException
     */
    public TQueryRepository getTQueryRepository()
        throws TorqueException
    {
        if (aTQueryRepository == null && (!ObjectUtils.equals(this.notifyFilter, null)))
        {
            aTQueryRepository = TQueryRepositoryPeer.retrieveByPK(SimpleKey.keyFor(this.notifyFilter));
        }
        return aTQueryRepository;
    }

    /**
     * Return the associated TQueryRepository object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TQueryRepository object
     * @throws TorqueException
     */
    public TQueryRepository getTQueryRepository(Connection connection)
        throws TorqueException
    {
        if (aTQueryRepository == null && (!ObjectUtils.equals(this.notifyFilter, null)))
        {
            aTQueryRepository = TQueryRepositoryPeer.retrieveByPK(SimpleKey.keyFor(this.notifyFilter), connection);
        }
        return aTQueryRepository;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTQueryRepositoryKey(ObjectKey key) throws TorqueException
    {

        setNotifyFilter(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Project");
            fieldNames.add("NotifyTrigger");
            fieldNames.add("NotifyFilter");
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
        if (name.equals("Project"))
        {
            return getProject();
        }
        if (name.equals("NotifyTrigger"))
        {
            return getNotifyTrigger();
        }
        if (name.equals("NotifyFilter"))
        {
            return getNotifyFilter();
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
        if (name.equals("Project"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProject((Integer) value);
            return true;
        }
        if (name.equals("NotifyTrigger"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNotifyTrigger((Integer) value);
            return true;
        }
        if (name.equals("NotifyFilter"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNotifyFilter((Integer) value);
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
        if (name.equals(TNotifySettingsPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TNotifySettingsPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TNotifySettingsPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TNotifySettingsPeer.NOTIFYTRIGGER))
        {
            return getNotifyTrigger();
        }
        if (name.equals(TNotifySettingsPeer.NOTIFYFILTER))
        {
            return getNotifyFilter();
        }
        if (name.equals(TNotifySettingsPeer.TPUUID))
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
      if (TNotifySettingsPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TNotifySettingsPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TNotifySettingsPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TNotifySettingsPeer.NOTIFYTRIGGER.equals(name))
        {
            return setByName("NotifyTrigger", value);
        }
      if (TNotifySettingsPeer.NOTIFYFILTER.equals(name))
        {
            return setByName("NotifyFilter", value);
        }
      if (TNotifySettingsPeer.TPUUID.equals(name))
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
            return getProject();
        }
        if (pos == 3)
        {
            return getNotifyTrigger();
        }
        if (pos == 4)
        {
            return getNotifyFilter();
        }
        if (pos == 5)
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
            return setByName("Project", value);
        }
    if (position == 3)
        {
            return setByName("NotifyTrigger", value);
        }
    if (position == 4)
        {
            return setByName("NotifyFilter", value);
        }
    if (position == 5)
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
        save(TNotifySettingsPeer.DATABASE_NAME);
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
                    TNotifySettingsPeer.doInsert((TNotifySettings) this, con);
                    setNew(false);
                }
                else
                {
                    TNotifySettingsPeer.doUpdate((TNotifySettings) this, con);
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
    public TNotifySettings copy() throws TorqueException
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
    public TNotifySettings copy(Connection con) throws TorqueException
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
    public TNotifySettings copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TNotifySettings(), deepcopy);
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
    public TNotifySettings copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TNotifySettings(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TNotifySettings copyInto(TNotifySettings copyObj) throws TorqueException
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
    protected TNotifySettings copyInto(TNotifySettings copyObj, Connection con) throws TorqueException
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
    protected TNotifySettings copyInto(TNotifySettings copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setProject(project);
        copyObj.setNotifyTrigger(notifyTrigger);
        copyObj.setNotifyFilter(notifyFilter);
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
    protected TNotifySettings copyInto(TNotifySettings copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setProject(project);
        copyObj.setNotifyTrigger(notifyTrigger);
        copyObj.setNotifyFilter(notifyFilter);
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
    public TNotifySettingsPeer getPeer()
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
        return TNotifySettingsPeer.getTableMap();
    }

  
    /**
     * Creates a TNotifySettingsBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TNotifySettingsBean with the contents of this object
     */
    public TNotifySettingsBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TNotifySettingsBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TNotifySettingsBean with the contents of this object
     */
    public TNotifySettingsBean getBean(IdentityMap createdBeans)
    {
        TNotifySettingsBean result = (TNotifySettingsBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TNotifySettingsBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setPerson(getPerson());
        result.setProject(getProject());
        result.setNotifyTrigger(getNotifyTrigger());
        result.setNotifyFilter(getNotifyFilter());
        result.setUuid(getUuid());





        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }



        if (aTNotifyTrigger != null)
        {
            TNotifyTriggerBean relatedBean = aTNotifyTrigger.getBean(createdBeans);
            result.setTNotifyTriggerBean(relatedBean);
        }



        if (aTQueryRepository != null)
        {
            TQueryRepositoryBean relatedBean = aTQueryRepository.getBean(createdBeans);
            result.setTQueryRepositoryBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TNotifySettings with the contents
     * of a TNotifySettingsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TNotifySettingsBean which contents are used to create
     *        the resulting class
     * @return an instance of TNotifySettings with the contents of bean
     */
    public static TNotifySettings createTNotifySettings(TNotifySettingsBean bean)
        throws TorqueException
    {
        return createTNotifySettings(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TNotifySettings with the contents
     * of a TNotifySettingsBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TNotifySettingsBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TNotifySettings with the contents of bean
     */

    public static TNotifySettings createTNotifySettings(TNotifySettingsBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TNotifySettings result = (TNotifySettings) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TNotifySettings();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setPerson(bean.getPerson());
        result.setProject(bean.getProject());
        result.setNotifyTrigger(bean.getNotifyTrigger());
        result.setNotifyFilter(bean.getNotifyFilter());
        result.setUuid(bean.getUuid());





        {
            TPersonBean relatedBean = bean.getTPersonBean();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPerson(relatedObject);
            }
        }



        {
            TProjectBean relatedBean = bean.getTProjectBean();
            if (relatedBean != null)
            {
                TProject relatedObject = TProject.createTProject(relatedBean, createdObjects);
                result.setTProject(relatedObject);
            }
        }



        {
            TNotifyTriggerBean relatedBean = bean.getTNotifyTriggerBean();
            if (relatedBean != null)
            {
                TNotifyTrigger relatedObject = TNotifyTrigger.createTNotifyTrigger(relatedBean, createdObjects);
                result.setTNotifyTrigger(relatedObject);
            }
        }



        {
            TQueryRepositoryBean relatedBean = bean.getTQueryRepositoryBean();
            if (relatedBean != null)
            {
                TQueryRepository relatedObject = TQueryRepository.createTQueryRepository(relatedBean, createdObjects);
                result.setTQueryRepository(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TNotifySettings:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("Project = ")
           .append(getProject())
           .append("\n");
        str.append("NotifyTrigger = ")
           .append(getNotifyTrigger())
           .append("\n");
        str.append("NotifyFilter = ")
           .append(getNotifyFilter())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
