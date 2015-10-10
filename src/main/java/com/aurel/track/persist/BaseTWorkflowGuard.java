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



import com.aurel.track.persist.TWorkflowTransition;
import com.aurel.track.persist.TWorkflowTransitionPeer;
import com.aurel.track.persist.TRole;
import com.aurel.track.persist.TRolePeer;
import com.aurel.track.persist.TScripts;
import com.aurel.track.persist.TScriptsPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TWorkflowGuardBean;
import com.aurel.track.beans.TWorkflowTransitionBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.beans.TPersonBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TWorkflowGuard
 */
public abstract class BaseTWorkflowGuard extends TpBaseObject
{
    /** The Peer class */
    private static final TWorkflowGuardPeer peer =
        new TWorkflowGuardPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the guardType field */
    private Integer guardType;

    /** The value for the guardParams field */
    private String guardParams;

    /** The value for the workflowTransition field */
    private Integer workflowTransition;

    /** The value for the role field */
    private Integer role;

    /** The value for the groovyScript field */
    private Integer groovyScript;

    /** The value for the person field */
    private Integer person;

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
     * Get the GuardType
     *
     * @return Integer
     */
    public Integer getGuardType()
    {
        return guardType;
    }


    /**
     * Set the value of GuardType
     *
     * @param v new value
     */
    public void setGuardType(Integer v) 
    {

        if (!ObjectUtils.equals(this.guardType, v))
        {
            this.guardType = v;
            setModified(true);
        }


    }

    /**
     * Get the GuardParams
     *
     * @return String
     */
    public String getGuardParams()
    {
        return guardParams;
    }


    /**
     * Set the value of GuardParams
     *
     * @param v new value
     */
    public void setGuardParams(String v) 
    {

        if (!ObjectUtils.equals(this.guardParams, v))
        {
            this.guardParams = v;
            setModified(true);
        }


    }

    /**
     * Get the WorkflowTransition
     *
     * @return Integer
     */
    public Integer getWorkflowTransition()
    {
        return workflowTransition;
    }


    /**
     * Set the value of WorkflowTransition
     *
     * @param v new value
     */
    public void setWorkflowTransition(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workflowTransition, v))
        {
            this.workflowTransition = v;
            setModified(true);
        }


        if (aTWorkflowTransition != null && !ObjectUtils.equals(aTWorkflowTransition.getObjectID(), v))
        {
            aTWorkflowTransition = null;
        }

    }

    /**
     * Get the Role
     *
     * @return Integer
     */
    public Integer getRole()
    {
        return role;
    }


    /**
     * Set the value of Role
     *
     * @param v new value
     */
    public void setRole(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.role, v))
        {
            this.role = v;
            setModified(true);
        }


        if (aTRole != null && !ObjectUtils.equals(aTRole.getObjectID(), v))
        {
            aTRole = null;
        }

    }

    /**
     * Get the GroovyScript
     *
     * @return Integer
     */
    public Integer getGroovyScript()
    {
        return groovyScript;
    }


    /**
     * Set the value of GroovyScript
     *
     * @param v new value
     */
    public void setGroovyScript(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.groovyScript, v))
        {
            this.groovyScript = v;
            setModified(true);
        }


        if (aTScripts != null && !ObjectUtils.equals(aTScripts.getObjectID(), v))
        {
            aTScripts = null;
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

    



    private TWorkflowTransition aTWorkflowTransition;

    /**
     * Declares an association between this object and a TWorkflowTransition object
     *
     * @param v TWorkflowTransition
     * @throws TorqueException
     */
    public void setTWorkflowTransition(TWorkflowTransition v) throws TorqueException
    {
        if (v == null)
        {
            setWorkflowTransition((Integer) null);
        }
        else
        {
            setWorkflowTransition(v.getObjectID());
        }
        aTWorkflowTransition = v;
    }


    /**
     * Returns the associated TWorkflowTransition object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkflowTransition object
     * @throws TorqueException
     */
    public TWorkflowTransition getTWorkflowTransition()
        throws TorqueException
    {
        if (aTWorkflowTransition == null && (!ObjectUtils.equals(this.workflowTransition, null)))
        {
            aTWorkflowTransition = TWorkflowTransitionPeer.retrieveByPK(SimpleKey.keyFor(this.workflowTransition));
        }
        return aTWorkflowTransition;
    }

    /**
     * Return the associated TWorkflowTransition object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkflowTransition object
     * @throws TorqueException
     */
    public TWorkflowTransition getTWorkflowTransition(Connection connection)
        throws TorqueException
    {
        if (aTWorkflowTransition == null && (!ObjectUtils.equals(this.workflowTransition, null)))
        {
            aTWorkflowTransition = TWorkflowTransitionPeer.retrieveByPK(SimpleKey.keyFor(this.workflowTransition), connection);
        }
        return aTWorkflowTransition;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkflowTransitionKey(ObjectKey key) throws TorqueException
    {

        setWorkflowTransition(new Integer(((NumberKey) key).intValue()));
    }




    private TRole aTRole;

    /**
     * Declares an association between this object and a TRole object
     *
     * @param v TRole
     * @throws TorqueException
     */
    public void setTRole(TRole v) throws TorqueException
    {
        if (v == null)
        {
            setRole((Integer) null);
        }
        else
        {
            setRole(v.getObjectID());
        }
        aTRole = v;
    }


    /**
     * Returns the associated TRole object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TRole object
     * @throws TorqueException
     */
    public TRole getTRole()
        throws TorqueException
    {
        if (aTRole == null && (!ObjectUtils.equals(this.role, null)))
        {
            aTRole = TRolePeer.retrieveByPK(SimpleKey.keyFor(this.role));
        }
        return aTRole;
    }

    /**
     * Return the associated TRole object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TRole object
     * @throws TorqueException
     */
    public TRole getTRole(Connection connection)
        throws TorqueException
    {
        if (aTRole == null && (!ObjectUtils.equals(this.role, null)))
        {
            aTRole = TRolePeer.retrieveByPK(SimpleKey.keyFor(this.role), connection);
        }
        return aTRole;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTRoleKey(ObjectKey key) throws TorqueException
    {

        setRole(new Integer(((NumberKey) key).intValue()));
    }




    private TScripts aTScripts;

    /**
     * Declares an association between this object and a TScripts object
     *
     * @param v TScripts
     * @throws TorqueException
     */
    public void setTScripts(TScripts v) throws TorqueException
    {
        if (v == null)
        {
            setGroovyScript((Integer) null);
        }
        else
        {
            setGroovyScript(v.getObjectID());
        }
        aTScripts = v;
    }


    /**
     * Returns the associated TScripts object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TScripts object
     * @throws TorqueException
     */
    public TScripts getTScripts()
        throws TorqueException
    {
        if (aTScripts == null && (!ObjectUtils.equals(this.groovyScript, null)))
        {
            aTScripts = TScriptsPeer.retrieveByPK(SimpleKey.keyFor(this.groovyScript));
        }
        return aTScripts;
    }

    /**
     * Return the associated TScripts object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TScripts object
     * @throws TorqueException
     */
    public TScripts getTScripts(Connection connection)
        throws TorqueException
    {
        if (aTScripts == null && (!ObjectUtils.equals(this.groovyScript, null)))
        {
            aTScripts = TScriptsPeer.retrieveByPK(SimpleKey.keyFor(this.groovyScript), connection);
        }
        return aTScripts;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTScriptsKey(ObjectKey key) throws TorqueException
    {

        setGroovyScript(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("GuardType");
            fieldNames.add("GuardParams");
            fieldNames.add("WorkflowTransition");
            fieldNames.add("Role");
            fieldNames.add("GroovyScript");
            fieldNames.add("Person");
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
        if (name.equals("GuardType"))
        {
            return getGuardType();
        }
        if (name.equals("GuardParams"))
        {
            return getGuardParams();
        }
        if (name.equals("WorkflowTransition"))
        {
            return getWorkflowTransition();
        }
        if (name.equals("Role"))
        {
            return getRole();
        }
        if (name.equals("GroovyScript"))
        {
            return getGroovyScript();
        }
        if (name.equals("Person"))
        {
            return getPerson();
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
        if (name.equals("GuardType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setGuardType((Integer) value);
            return true;
        }
        if (name.equals("GuardParams"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setGuardParams((String) value);
            return true;
        }
        if (name.equals("WorkflowTransition"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkflowTransition((Integer) value);
            return true;
        }
        if (name.equals("Role"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRole((Integer) value);
            return true;
        }
        if (name.equals("GroovyScript"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setGroovyScript((Integer) value);
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
        if (name.equals(TWorkflowGuardPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TWorkflowGuardPeer.GUARDTYPE))
        {
            return getGuardType();
        }
        if (name.equals(TWorkflowGuardPeer.GUARDPARAMS))
        {
            return getGuardParams();
        }
        if (name.equals(TWorkflowGuardPeer.WORKFLOWTRANSITION))
        {
            return getWorkflowTransition();
        }
        if (name.equals(TWorkflowGuardPeer.ROLEKEY))
        {
            return getRole();
        }
        if (name.equals(TWorkflowGuardPeer.GROOVYSCRIPT))
        {
            return getGroovyScript();
        }
        if (name.equals(TWorkflowGuardPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TWorkflowGuardPeer.TPUUID))
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
      if (TWorkflowGuardPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TWorkflowGuardPeer.GUARDTYPE.equals(name))
        {
            return setByName("GuardType", value);
        }
      if (TWorkflowGuardPeer.GUARDPARAMS.equals(name))
        {
            return setByName("GuardParams", value);
        }
      if (TWorkflowGuardPeer.WORKFLOWTRANSITION.equals(name))
        {
            return setByName("WorkflowTransition", value);
        }
      if (TWorkflowGuardPeer.ROLEKEY.equals(name))
        {
            return setByName("Role", value);
        }
      if (TWorkflowGuardPeer.GROOVYSCRIPT.equals(name))
        {
            return setByName("GroovyScript", value);
        }
      if (TWorkflowGuardPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TWorkflowGuardPeer.TPUUID.equals(name))
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
            return getGuardType();
        }
        if (pos == 2)
        {
            return getGuardParams();
        }
        if (pos == 3)
        {
            return getWorkflowTransition();
        }
        if (pos == 4)
        {
            return getRole();
        }
        if (pos == 5)
        {
            return getGroovyScript();
        }
        if (pos == 6)
        {
            return getPerson();
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
            return setByName("GuardType", value);
        }
    if (position == 2)
        {
            return setByName("GuardParams", value);
        }
    if (position == 3)
        {
            return setByName("WorkflowTransition", value);
        }
    if (position == 4)
        {
            return setByName("Role", value);
        }
    if (position == 5)
        {
            return setByName("GroovyScript", value);
        }
    if (position == 6)
        {
            return setByName("Person", value);
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
        save(TWorkflowGuardPeer.DATABASE_NAME);
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
                    TWorkflowGuardPeer.doInsert((TWorkflowGuard) this, con);
                    setNew(false);
                }
                else
                {
                    TWorkflowGuardPeer.doUpdate((TWorkflowGuard) this, con);
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
    public TWorkflowGuard copy() throws TorqueException
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
    public TWorkflowGuard copy(Connection con) throws TorqueException
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
    public TWorkflowGuard copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TWorkflowGuard(), deepcopy);
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
    public TWorkflowGuard copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TWorkflowGuard(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TWorkflowGuard copyInto(TWorkflowGuard copyObj) throws TorqueException
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
    protected TWorkflowGuard copyInto(TWorkflowGuard copyObj, Connection con) throws TorqueException
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
    protected TWorkflowGuard copyInto(TWorkflowGuard copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setGuardType(guardType);
        copyObj.setGuardParams(guardParams);
        copyObj.setWorkflowTransition(workflowTransition);
        copyObj.setRole(role);
        copyObj.setGroovyScript(groovyScript);
        copyObj.setPerson(person);
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
    protected TWorkflowGuard copyInto(TWorkflowGuard copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setGuardType(guardType);
        copyObj.setGuardParams(guardParams);
        copyObj.setWorkflowTransition(workflowTransition);
        copyObj.setRole(role);
        copyObj.setGroovyScript(groovyScript);
        copyObj.setPerson(person);
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
    public TWorkflowGuardPeer getPeer()
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
        return TWorkflowGuardPeer.getTableMap();
    }

  
    /**
     * Creates a TWorkflowGuardBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TWorkflowGuardBean with the contents of this object
     */
    public TWorkflowGuardBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TWorkflowGuardBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TWorkflowGuardBean with the contents of this object
     */
    public TWorkflowGuardBean getBean(IdentityMap createdBeans)
    {
        TWorkflowGuardBean result = (TWorkflowGuardBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TWorkflowGuardBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setGuardType(getGuardType());
        result.setGuardParams(getGuardParams());
        result.setWorkflowTransition(getWorkflowTransition());
        result.setRole(getRole());
        result.setGroovyScript(getGroovyScript());
        result.setPerson(getPerson());
        result.setUuid(getUuid());





        if (aTWorkflowTransition != null)
        {
            TWorkflowTransitionBean relatedBean = aTWorkflowTransition.getBean(createdBeans);
            result.setTWorkflowTransitionBean(relatedBean);
        }



        if (aTRole != null)
        {
            TRoleBean relatedBean = aTRole.getBean(createdBeans);
            result.setTRoleBean(relatedBean);
        }



        if (aTScripts != null)
        {
            TScriptsBean relatedBean = aTScripts.getBean(createdBeans);
            result.setTScriptsBean(relatedBean);
        }



        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TWorkflowGuard with the contents
     * of a TWorkflowGuardBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TWorkflowGuardBean which contents are used to create
     *        the resulting class
     * @return an instance of TWorkflowGuard with the contents of bean
     */
    public static TWorkflowGuard createTWorkflowGuard(TWorkflowGuardBean bean)
        throws TorqueException
    {
        return createTWorkflowGuard(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TWorkflowGuard with the contents
     * of a TWorkflowGuardBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TWorkflowGuardBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TWorkflowGuard with the contents of bean
     */

    public static TWorkflowGuard createTWorkflowGuard(TWorkflowGuardBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TWorkflowGuard result = (TWorkflowGuard) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TWorkflowGuard();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setGuardType(bean.getGuardType());
        result.setGuardParams(bean.getGuardParams());
        result.setWorkflowTransition(bean.getWorkflowTransition());
        result.setRole(bean.getRole());
        result.setGroovyScript(bean.getGroovyScript());
        result.setPerson(bean.getPerson());
        result.setUuid(bean.getUuid());





        {
            TWorkflowTransitionBean relatedBean = bean.getTWorkflowTransitionBean();
            if (relatedBean != null)
            {
                TWorkflowTransition relatedObject = TWorkflowTransition.createTWorkflowTransition(relatedBean, createdObjects);
                result.setTWorkflowTransition(relatedObject);
            }
        }



        {
            TRoleBean relatedBean = bean.getTRoleBean();
            if (relatedBean != null)
            {
                TRole relatedObject = TRole.createTRole(relatedBean, createdObjects);
                result.setTRole(relatedObject);
            }
        }



        {
            TScriptsBean relatedBean = bean.getTScriptsBean();
            if (relatedBean != null)
            {
                TScripts relatedObject = TScripts.createTScripts(relatedBean, createdObjects);
                result.setTScripts(relatedObject);
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
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TWorkflowGuard:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("GuardType = ")
           .append(getGuardType())
           .append("\n");
        str.append("GuardParams = ")
           .append(getGuardParams())
           .append("\n");
        str.append("WorkflowTransition = ")
           .append(getWorkflowTransition())
           .append("\n");
        str.append("Role = ")
           .append(getRole())
           .append("\n");
        str.append("GroovyScript = ")
           .append(getGroovyScript())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
