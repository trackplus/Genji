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



import com.aurel.track.persist.TDepartment;
import com.aurel.track.persist.TDepartmentPeer;
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TSLA;
import com.aurel.track.persist.TSLAPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TOrgProjectSLABean;
import com.aurel.track.beans.TDepartmentBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TSLABean;



/**
 * service level agreement for a customer in a project
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TOrgProjectSLA
 */
public abstract class BaseTOrgProjectSLA extends TpBaseObject
{
    /** The Peer class */
    private static final TOrgProjectSLAPeer peer =
        new TOrgProjectSLAPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the department field */
    private Integer department;

    /** The value for the project field */
    private Integer project;

    /** The value for the sla field */
    private Integer sla;

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
     * Get the Department
     *
     * @return Integer
     */
    public Integer getDepartment()
    {
        return department;
    }


    /**
     * Set the value of Department
     *
     * @param v new value
     */
    public void setDepartment(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.department, v))
        {
            this.department = v;
            setModified(true);
        }


        if (aTDepartment != null && !ObjectUtils.equals(aTDepartment.getObjectID(), v))
        {
            aTDepartment = null;
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
     * Get the Sla
     *
     * @return Integer
     */
    public Integer getSla()
    {
        return sla;
    }


    /**
     * Set the value of Sla
     *
     * @param v new value
     */
    public void setSla(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.sla, v))
        {
            this.sla = v;
            setModified(true);
        }


        if (aTSLA != null && !ObjectUtils.equals(aTSLA.getObjectID(), v))
        {
            aTSLA = null;
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

    



    private TDepartment aTDepartment;

    /**
     * Declares an association between this object and a TDepartment object
     *
     * @param v TDepartment
     * @throws TorqueException
     */
    public void setTDepartment(TDepartment v) throws TorqueException
    {
        if (v == null)
        {
            setDepartment((Integer) null);
        }
        else
        {
            setDepartment(v.getObjectID());
        }
        aTDepartment = v;
    }


    /**
     * Returns the associated TDepartment object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TDepartment object
     * @throws TorqueException
     */
    public TDepartment getTDepartment()
        throws TorqueException
    {
        if (aTDepartment == null && (!ObjectUtils.equals(this.department, null)))
        {
            aTDepartment = TDepartmentPeer.retrieveByPK(SimpleKey.keyFor(this.department));
        }
        return aTDepartment;
    }

    /**
     * Return the associated TDepartment object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TDepartment object
     * @throws TorqueException
     */
    public TDepartment getTDepartment(Connection connection)
        throws TorqueException
    {
        if (aTDepartment == null && (!ObjectUtils.equals(this.department, null)))
        {
            aTDepartment = TDepartmentPeer.retrieveByPK(SimpleKey.keyFor(this.department), connection);
        }
        return aTDepartment;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTDepartmentKey(ObjectKey key) throws TorqueException
    {

        setDepartment(new Integer(((NumberKey) key).intValue()));
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




    private TSLA aTSLA;

    /**
     * Declares an association between this object and a TSLA object
     *
     * @param v TSLA
     * @throws TorqueException
     */
    public void setTSLA(TSLA v) throws TorqueException
    {
        if (v == null)
        {
            setSla((Integer) null);
        }
        else
        {
            setSla(v.getObjectID());
        }
        aTSLA = v;
    }


    /**
     * Returns the associated TSLA object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TSLA object
     * @throws TorqueException
     */
    public TSLA getTSLA()
        throws TorqueException
    {
        if (aTSLA == null && (!ObjectUtils.equals(this.sla, null)))
        {
            aTSLA = TSLAPeer.retrieveByPK(SimpleKey.keyFor(this.sla));
        }
        return aTSLA;
    }

    /**
     * Return the associated TSLA object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TSLA object
     * @throws TorqueException
     */
    public TSLA getTSLA(Connection connection)
        throws TorqueException
    {
        if (aTSLA == null && (!ObjectUtils.equals(this.sla, null)))
        {
            aTSLA = TSLAPeer.retrieveByPK(SimpleKey.keyFor(this.sla), connection);
        }
        return aTSLA;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTSLAKey(ObjectKey key) throws TorqueException
    {

        setSla(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Department");
            fieldNames.add("Project");
            fieldNames.add("Sla");
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
        if (name.equals("Department"))
        {
            return getDepartment();
        }
        if (name.equals("Project"))
        {
            return getProject();
        }
        if (name.equals("Sla"))
        {
            return getSla();
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
        if (name.equals("Department"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDepartment((Integer) value);
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
        if (name.equals("Sla"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSla((Integer) value);
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
        if (name.equals(TOrgProjectSLAPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TOrgProjectSLAPeer.DEPARTMENT))
        {
            return getDepartment();
        }
        if (name.equals(TOrgProjectSLAPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TOrgProjectSLAPeer.SLA))
        {
            return getSla();
        }
        if (name.equals(TOrgProjectSLAPeer.TPUUID))
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
      if (TOrgProjectSLAPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TOrgProjectSLAPeer.DEPARTMENT.equals(name))
        {
            return setByName("Department", value);
        }
      if (TOrgProjectSLAPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TOrgProjectSLAPeer.SLA.equals(name))
        {
            return setByName("Sla", value);
        }
      if (TOrgProjectSLAPeer.TPUUID.equals(name))
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
            return getDepartment();
        }
        if (pos == 2)
        {
            return getProject();
        }
        if (pos == 3)
        {
            return getSla();
        }
        if (pos == 4)
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
            return setByName("Department", value);
        }
    if (position == 2)
        {
            return setByName("Project", value);
        }
    if (position == 3)
        {
            return setByName("Sla", value);
        }
    if (position == 4)
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
        save(TOrgProjectSLAPeer.DATABASE_NAME);
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
                    TOrgProjectSLAPeer.doInsert((TOrgProjectSLA) this, con);
                    setNew(false);
                }
                else
                {
                    TOrgProjectSLAPeer.doUpdate((TOrgProjectSLA) this, con);
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
    public TOrgProjectSLA copy() throws TorqueException
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
    public TOrgProjectSLA copy(Connection con) throws TorqueException
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
    public TOrgProjectSLA copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TOrgProjectSLA(), deepcopy);
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
    public TOrgProjectSLA copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TOrgProjectSLA(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TOrgProjectSLA copyInto(TOrgProjectSLA copyObj) throws TorqueException
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
    protected TOrgProjectSLA copyInto(TOrgProjectSLA copyObj, Connection con) throws TorqueException
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
    protected TOrgProjectSLA copyInto(TOrgProjectSLA copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setDepartment(department);
        copyObj.setProject(project);
        copyObj.setSla(sla);
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
    protected TOrgProjectSLA copyInto(TOrgProjectSLA copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setDepartment(department);
        copyObj.setProject(project);
        copyObj.setSla(sla);
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
    public TOrgProjectSLAPeer getPeer()
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
        return TOrgProjectSLAPeer.getTableMap();
    }

  
    /**
     * Creates a TOrgProjectSLABean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TOrgProjectSLABean with the contents of this object
     */
    public TOrgProjectSLABean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TOrgProjectSLABean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TOrgProjectSLABean with the contents of this object
     */
    public TOrgProjectSLABean getBean(IdentityMap createdBeans)
    {
        TOrgProjectSLABean result = (TOrgProjectSLABean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TOrgProjectSLABean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setDepartment(getDepartment());
        result.setProject(getProject());
        result.setSla(getSla());
        result.setUuid(getUuid());





        if (aTDepartment != null)
        {
            TDepartmentBean relatedBean = aTDepartment.getBean(createdBeans);
            result.setTDepartmentBean(relatedBean);
        }



        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }



        if (aTSLA != null)
        {
            TSLABean relatedBean = aTSLA.getBean(createdBeans);
            result.setTSLABean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TOrgProjectSLA with the contents
     * of a TOrgProjectSLABean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TOrgProjectSLABean which contents are used to create
     *        the resulting class
     * @return an instance of TOrgProjectSLA with the contents of bean
     */
    public static TOrgProjectSLA createTOrgProjectSLA(TOrgProjectSLABean bean)
        throws TorqueException
    {
        return createTOrgProjectSLA(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TOrgProjectSLA with the contents
     * of a TOrgProjectSLABean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TOrgProjectSLABean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TOrgProjectSLA with the contents of bean
     */

    public static TOrgProjectSLA createTOrgProjectSLA(TOrgProjectSLABean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TOrgProjectSLA result = (TOrgProjectSLA) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TOrgProjectSLA();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setDepartment(bean.getDepartment());
        result.setProject(bean.getProject());
        result.setSla(bean.getSla());
        result.setUuid(bean.getUuid());





        {
            TDepartmentBean relatedBean = bean.getTDepartmentBean();
            if (relatedBean != null)
            {
                TDepartment relatedObject = TDepartment.createTDepartment(relatedBean, createdObjects);
                result.setTDepartment(relatedObject);
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
            TSLABean relatedBean = bean.getTSLABean();
            if (relatedBean != null)
            {
                TSLA relatedObject = TSLA.createTSLA(relatedBean, createdObjects);
                result.setTSLA(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TOrgProjectSLA:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Department = ")
           .append(getDepartment())
           .append("\n");
        str.append("Project = ")
           .append(getProject())
           .append("\n");
        str.append("Sla = ")
           .append(getSla())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
