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



import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TRole;
import com.aurel.track.persist.TRolePeer;
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TProjectBean;



/**
 * Describes which person has which role in which project.
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TAccessControlList
 */
public abstract class BaseTAccessControlList extends TpBaseObject
{
    /** The Peer class */
    private static final TAccessControlListPeer peer =
        new TAccessControlListPeer();


    /** The value for the personID field */
    private Integer personID;

    /** The value for the roleID field */
    private Integer roleID;

    /** The value for the projectID field */
    private Integer projectID;

    /** The value for the uuid field */
    private String uuid;


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
     * Get the RoleID
     *
     * @return Integer
     */
    public Integer getRoleID()
    {
        return roleID;
    }


    /**
     * Set the value of RoleID
     *
     * @param v new value
     */
    public void setRoleID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.roleID, v))
        {
            this.roleID = v;
            setModified(true);
        }


        if (aTRole != null && !ObjectUtils.equals(aTRole.getObjectID(), v))
        {
            aTRole = null;
        }

    }

    /**
     * Get the ProjectID
     *
     * @return Integer
     */
    public Integer getProjectID()
    {
        return projectID;
    }


    /**
     * Set the value of ProjectID
     *
     * @param v new value
     */
    public void setProjectID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.projectID, v))
        {
            this.projectID = v;
            setModified(true);
        }


        if (aTProject != null && !ObjectUtils.equals(aTProject.getObjectID(), v))
        {
            aTProject = null;
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
            setRoleID((Integer) null);
        }
        else
        {
            setRoleID(v.getObjectID());
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
        if (aTRole == null && (!ObjectUtils.equals(this.roleID, null)))
        {
            aTRole = TRolePeer.retrieveByPK(SimpleKey.keyFor(this.roleID));
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
        if (aTRole == null && (!ObjectUtils.equals(this.roleID, null)))
        {
            aTRole = TRolePeer.retrieveByPK(SimpleKey.keyFor(this.roleID), connection);
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

        setRoleID(new Integer(((NumberKey) key).intValue()));
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
            setProjectID((Integer) null);
        }
        else
        {
            setProjectID(v.getObjectID());
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
        if (aTProject == null && (!ObjectUtils.equals(this.projectID, null)))
        {
            aTProject = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.projectID));
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
        if (aTProject == null && (!ObjectUtils.equals(this.projectID, null)))
        {
            aTProject = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.projectID), connection);
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

        setProjectID(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("PersonID");
            fieldNames.add("RoleID");
            fieldNames.add("ProjectID");
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
        if (name.equals("PersonID"))
        {
            return getPersonID();
        }
        if (name.equals("RoleID"))
        {
            return getRoleID();
        }
        if (name.equals("ProjectID"))
        {
            return getProjectID();
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
        if (name.equals("RoleID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRoleID((Integer) value);
            return true;
        }
        if (name.equals("ProjectID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProjectID((Integer) value);
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
        if (name.equals(TAccessControlListPeer.PERSONKEY))
        {
            return getPersonID();
        }
        if (name.equals(TAccessControlListPeer.ROLEKEY))
        {
            return getRoleID();
        }
        if (name.equals(TAccessControlListPeer.PROJKEY))
        {
            return getProjectID();
        }
        if (name.equals(TAccessControlListPeer.TPUUID))
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
      if (TAccessControlListPeer.PERSONKEY.equals(name))
        {
            return setByName("PersonID", value);
        }
      if (TAccessControlListPeer.ROLEKEY.equals(name))
        {
            return setByName("RoleID", value);
        }
      if (TAccessControlListPeer.PROJKEY.equals(name))
        {
            return setByName("ProjectID", value);
        }
      if (TAccessControlListPeer.TPUUID.equals(name))
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
            return getPersonID();
        }
        if (pos == 1)
        {
            return getRoleID();
        }
        if (pos == 2)
        {
            return getProjectID();
        }
        if (pos == 3)
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
            return setByName("PersonID", value);
        }
    if (position == 1)
        {
            return setByName("RoleID", value);
        }
    if (position == 2)
        {
            return setByName("ProjectID", value);
        }
    if (position == 3)
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
        save(TAccessControlListPeer.DATABASE_NAME);
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
                    TAccessControlListPeer.doInsert((TAccessControlList) this, con);
                    setNew(false);
                }
                else
                {
                    TAccessControlListPeer.doUpdate((TAccessControlList) this, con);
                }
            }

            alreadyInSave = false;
        }
    }



    private final SimpleKey[] pks = new SimpleKey[3];
    private final ComboKey comboPK = new ComboKey(pks);

    /**
     * Set the PrimaryKey with an ObjectKey
     *
     * @param key
     */
    public void setPrimaryKey(ObjectKey key) throws TorqueException
    {
        SimpleKey[] keys = (SimpleKey[]) key.getValue();
        setPersonID(new Integer(((NumberKey)keys[0]).intValue()));
        setRoleID(new Integer(((NumberKey)keys[1]).intValue()));
        setProjectID(new Integer(((NumberKey)keys[2]).intValue()));
    }

    /**
     * Set the PrimaryKey using SimpleKeys.
     *
     * @param personID Integer
     * @param roleID Integer
     * @param projectID Integer
     */
    public void setPrimaryKey( Integer personID, Integer roleID, Integer projectID)
        throws TorqueException
    {
        setPersonID(personID);
        setRoleID(roleID);
        setProjectID(projectID);
    }

    /**
     * Set the PrimaryKey using a String.
     */
    public void setPrimaryKey(String key) throws TorqueException
    {
        setPrimaryKey(new ComboKey(key));
    }

    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
        pks[0] = SimpleKey.keyFor(getPersonID());
        pks[1] = SimpleKey.keyFor(getRoleID());
        pks[2] = SimpleKey.keyFor(getProjectID());
        return comboPK;
    }
 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     */
    public TAccessControlList copy() throws TorqueException
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
    public TAccessControlList copy(Connection con) throws TorqueException
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
    public TAccessControlList copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TAccessControlList(), deepcopy);
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
    public TAccessControlList copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TAccessControlList(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TAccessControlList copyInto(TAccessControlList copyObj) throws TorqueException
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
    protected TAccessControlList copyInto(TAccessControlList copyObj, Connection con) throws TorqueException
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
    protected TAccessControlList copyInto(TAccessControlList copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setPersonID(personID);
        copyObj.setRoleID(roleID);
        copyObj.setProjectID(projectID);
        copyObj.setUuid(uuid);

        copyObj.setPersonID((Integer)null);
        copyObj.setRoleID((Integer)null);
        copyObj.setProjectID((Integer)null);

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
    protected TAccessControlList copyInto(TAccessControlList copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setPersonID(personID);
        copyObj.setRoleID(roleID);
        copyObj.setProjectID(projectID);
        copyObj.setUuid(uuid);

        copyObj.setPersonID((Integer)null);
        copyObj.setRoleID((Integer)null);
        copyObj.setProjectID((Integer)null);

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
    public TAccessControlListPeer getPeer()
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
        return TAccessControlListPeer.getTableMap();
    }

  
    /**
     * Creates a TAccessControlListBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TAccessControlListBean with the contents of this object
     */
    public TAccessControlListBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TAccessControlListBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TAccessControlListBean with the contents of this object
     */
    public TAccessControlListBean getBean(IdentityMap createdBeans)
    {
        TAccessControlListBean result = (TAccessControlListBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TAccessControlListBean();
        createdBeans.put(this, result);

        result.setPersonID(getPersonID());
        result.setRoleID(getRoleID());
        result.setProjectID(getProjectID());
        result.setUuid(getUuid());





        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTRole != null)
        {
            TRoleBean relatedBean = aTRole.getBean(createdBeans);
            result.setTRoleBean(relatedBean);
        }



        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TAccessControlList with the contents
     * of a TAccessControlListBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TAccessControlListBean which contents are used to create
     *        the resulting class
     * @return an instance of TAccessControlList with the contents of bean
     */
    public static TAccessControlList createTAccessControlList(TAccessControlListBean bean)
        throws TorqueException
    {
        return createTAccessControlList(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TAccessControlList with the contents
     * of a TAccessControlListBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TAccessControlListBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TAccessControlList with the contents of bean
     */

    public static TAccessControlList createTAccessControlList(TAccessControlListBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TAccessControlList result = (TAccessControlList) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TAccessControlList();
        createdObjects.put(bean, result);

        result.setPersonID(bean.getPersonID());
        result.setRoleID(bean.getRoleID());
        result.setProjectID(bean.getProjectID());
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
            TRoleBean relatedBean = bean.getTRoleBean();
            if (relatedBean != null)
            {
                TRole relatedObject = TRole.createTRole(relatedBean, createdObjects);
                result.setTRole(relatedObject);
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
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TAccessControlList:\n");
        str.append("PersonID = ")
           .append(getPersonID())
           .append("\n");
        str.append("RoleID = ")
           .append(getRoleID())
           .append("\n");
        str.append("ProjectID = ")
           .append(getProjectID())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
