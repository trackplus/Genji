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



import com.aurel.track.persist.TSeverity;
import com.aurel.track.persist.TSeverityPeer;
import com.aurel.track.persist.TProjectType;
import com.aurel.track.persist.TProjectTypePeer;
import com.aurel.track.persist.TListType;
import com.aurel.track.persist.TListTypePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TPseverityBean;
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TListTypeBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TPseverity
 */
public abstract class BaseTPseverity extends TpBaseObject
{
    /** The Peer class */
    private static final TPseverityPeer peer =
        new TPseverityPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the severity field */
    private Integer severity;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the listType field */
    private Integer listType;

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
     * Get the Severity
     *
     * @return Integer
     */
    public Integer getSeverity()
    {
        return severity;
    }


    /**
     * Set the value of Severity
     *
     * @param v new value
     */
    public void setSeverity(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.severity, v))
        {
            this.severity = v;
            setModified(true);
        }


        if (aTSeverity != null && !ObjectUtils.equals(aTSeverity.getObjectID(), v))
        {
            aTSeverity = null;
        }

    }

    /**
     * Get the ProjectType
     *
     * @return Integer
     */
    public Integer getProjectType()
    {
        return projectType;
    }


    /**
     * Set the value of ProjectType
     *
     * @param v new value
     */
    public void setProjectType(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.projectType, v))
        {
            this.projectType = v;
            setModified(true);
        }


        if (aTProjectType != null && !ObjectUtils.equals(aTProjectType.getObjectID(), v))
        {
            aTProjectType = null;
        }

    }

    /**
     * Get the ListType
     *
     * @return Integer
     */
    public Integer getListType()
    {
        return listType;
    }


    /**
     * Set the value of ListType
     *
     * @param v new value
     */
    public void setListType(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.listType, v))
        {
            this.listType = v;
            setModified(true);
        }


        if (aTListType != null && !ObjectUtils.equals(aTListType.getObjectID(), v))
        {
            aTListType = null;
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

    



    private TSeverity aTSeverity;

    /**
     * Declares an association between this object and a TSeverity object
     *
     * @param v TSeverity
     * @throws TorqueException
     */
    public void setTSeverity(TSeverity v) throws TorqueException
    {
        if (v == null)
        {
            setSeverity((Integer) null);
        }
        else
        {
            setSeverity(v.getObjectID());
        }
        aTSeverity = v;
    }


    /**
     * Returns the associated TSeverity object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TSeverity object
     * @throws TorqueException
     */
    public TSeverity getTSeverity()
        throws TorqueException
    {
        if (aTSeverity == null && (!ObjectUtils.equals(this.severity, null)))
        {
            aTSeverity = TSeverityPeer.retrieveByPK(SimpleKey.keyFor(this.severity));
        }
        return aTSeverity;
    }

    /**
     * Return the associated TSeverity object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TSeverity object
     * @throws TorqueException
     */
    public TSeverity getTSeverity(Connection connection)
        throws TorqueException
    {
        if (aTSeverity == null && (!ObjectUtils.equals(this.severity, null)))
        {
            aTSeverity = TSeverityPeer.retrieveByPK(SimpleKey.keyFor(this.severity), connection);
        }
        return aTSeverity;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTSeverityKey(ObjectKey key) throws TorqueException
    {

        setSeverity(new Integer(((NumberKey) key).intValue()));
    }




    private TProjectType aTProjectType;

    /**
     * Declares an association between this object and a TProjectType object
     *
     * @param v TProjectType
     * @throws TorqueException
     */
    public void setTProjectType(TProjectType v) throws TorqueException
    {
        if (v == null)
        {
            setProjectType((Integer) null);
        }
        else
        {
            setProjectType(v.getObjectID());
        }
        aTProjectType = v;
    }


    /**
     * Returns the associated TProjectType object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProjectType object
     * @throws TorqueException
     */
    public TProjectType getTProjectType()
        throws TorqueException
    {
        if (aTProjectType == null && (!ObjectUtils.equals(this.projectType, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projectType));
        }
        return aTProjectType;
    }

    /**
     * Return the associated TProjectType object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProjectType object
     * @throws TorqueException
     */
    public TProjectType getTProjectType(Connection connection)
        throws TorqueException
    {
        if (aTProjectType == null && (!ObjectUtils.equals(this.projectType, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projectType), connection);
        }
        return aTProjectType;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectTypeKey(ObjectKey key) throws TorqueException
    {

        setProjectType(new Integer(((NumberKey) key).intValue()));
    }




    private TListType aTListType;

    /**
     * Declares an association between this object and a TListType object
     *
     * @param v TListType
     * @throws TorqueException
     */
    public void setTListType(TListType v) throws TorqueException
    {
        if (v == null)
        {
            setListType((Integer) null);
        }
        else
        {
            setListType(v.getObjectID());
        }
        aTListType = v;
    }


    /**
     * Returns the associated TListType object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TListType object
     * @throws TorqueException
     */
    public TListType getTListType()
        throws TorqueException
    {
        if (aTListType == null && (!ObjectUtils.equals(this.listType, null)))
        {
            aTListType = TListTypePeer.retrieveByPK(SimpleKey.keyFor(this.listType));
        }
        return aTListType;
    }

    /**
     * Return the associated TListType object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TListType object
     * @throws TorqueException
     */
    public TListType getTListType(Connection connection)
        throws TorqueException
    {
        if (aTListType == null && (!ObjectUtils.equals(this.listType, null)))
        {
            aTListType = TListTypePeer.retrieveByPK(SimpleKey.keyFor(this.listType), connection);
        }
        return aTListType;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTListTypeKey(ObjectKey key) throws TorqueException
    {

        setListType(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Severity");
            fieldNames.add("ProjectType");
            fieldNames.add("ListType");
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
        if (name.equals("Severity"))
        {
            return getSeverity();
        }
        if (name.equals("ProjectType"))
        {
            return getProjectType();
        }
        if (name.equals("ListType"))
        {
            return getListType();
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
        if (name.equals("Severity"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSeverity((Integer) value);
            return true;
        }
        if (name.equals("ProjectType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProjectType((Integer) value);
            return true;
        }
        if (name.equals("ListType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setListType((Integer) value);
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
        if (name.equals(TPseverityPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TPseverityPeer.SEVERITY))
        {
            return getSeverity();
        }
        if (name.equals(TPseverityPeer.PROJECTTYPE))
        {
            return getProjectType();
        }
        if (name.equals(TPseverityPeer.LISTTYPE))
        {
            return getListType();
        }
        if (name.equals(TPseverityPeer.TPUUID))
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
      if (TPseverityPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TPseverityPeer.SEVERITY.equals(name))
        {
            return setByName("Severity", value);
        }
      if (TPseverityPeer.PROJECTTYPE.equals(name))
        {
            return setByName("ProjectType", value);
        }
      if (TPseverityPeer.LISTTYPE.equals(name))
        {
            return setByName("ListType", value);
        }
      if (TPseverityPeer.TPUUID.equals(name))
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
            return getSeverity();
        }
        if (pos == 2)
        {
            return getProjectType();
        }
        if (pos == 3)
        {
            return getListType();
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
            return setByName("Severity", value);
        }
    if (position == 2)
        {
            return setByName("ProjectType", value);
        }
    if (position == 3)
        {
            return setByName("ListType", value);
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
        save(TPseverityPeer.DATABASE_NAME);
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
                    TPseverityPeer.doInsert((TPseverity) this, con);
                    setNew(false);
                }
                else
                {
                    TPseverityPeer.doUpdate((TPseverity) this, con);
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
    public TPseverity copy() throws TorqueException
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
    public TPseverity copy(Connection con) throws TorqueException
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
    public TPseverity copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TPseverity(), deepcopy);
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
    public TPseverity copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TPseverity(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TPseverity copyInto(TPseverity copyObj) throws TorqueException
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
    protected TPseverity copyInto(TPseverity copyObj, Connection con) throws TorqueException
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
    protected TPseverity copyInto(TPseverity copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setSeverity(severity);
        copyObj.setProjectType(projectType);
        copyObj.setListType(listType);
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
    protected TPseverity copyInto(TPseverity copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setSeverity(severity);
        copyObj.setProjectType(projectType);
        copyObj.setListType(listType);
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
    public TPseverityPeer getPeer()
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
        return TPseverityPeer.getTableMap();
    }

  
    /**
     * Creates a TPseverityBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TPseverityBean with the contents of this object
     */
    public TPseverityBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TPseverityBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TPseverityBean with the contents of this object
     */
    public TPseverityBean getBean(IdentityMap createdBeans)
    {
        TPseverityBean result = (TPseverityBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TPseverityBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setSeverity(getSeverity());
        result.setProjectType(getProjectType());
        result.setListType(getListType());
        result.setUuid(getUuid());





        if (aTSeverity != null)
        {
            TSeverityBean relatedBean = aTSeverity.getBean(createdBeans);
            result.setTSeverityBean(relatedBean);
        }



        if (aTProjectType != null)
        {
            TProjectTypeBean relatedBean = aTProjectType.getBean(createdBeans);
            result.setTProjectTypeBean(relatedBean);
        }



        if (aTListType != null)
        {
            TListTypeBean relatedBean = aTListType.getBean(createdBeans);
            result.setTListTypeBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TPseverity with the contents
     * of a TPseverityBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TPseverityBean which contents are used to create
     *        the resulting class
     * @return an instance of TPseverity with the contents of bean
     */
    public static TPseverity createTPseverity(TPseverityBean bean)
        throws TorqueException
    {
        return createTPseverity(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TPseverity with the contents
     * of a TPseverityBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TPseverityBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TPseverity with the contents of bean
     */

    public static TPseverity createTPseverity(TPseverityBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TPseverity result = (TPseverity) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TPseverity();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setSeverity(bean.getSeverity());
        result.setProjectType(bean.getProjectType());
        result.setListType(bean.getListType());
        result.setUuid(bean.getUuid());





        {
            TSeverityBean relatedBean = bean.getTSeverityBean();
            if (relatedBean != null)
            {
                TSeverity relatedObject = TSeverity.createTSeverity(relatedBean, createdObjects);
                result.setTSeverity(relatedObject);
            }
        }



        {
            TProjectTypeBean relatedBean = bean.getTProjectTypeBean();
            if (relatedBean != null)
            {
                TProjectType relatedObject = TProjectType.createTProjectType(relatedBean, createdObjects);
                result.setTProjectType(relatedObject);
            }
        }



        {
            TListTypeBean relatedBean = bean.getTListTypeBean();
            if (relatedBean != null)
            {
                TListType relatedObject = TListType.createTListType(relatedBean, createdObjects);
                result.setTListType(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TPseverity:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Severity = ")
           .append(getSeverity())
           .append("\n");
        str.append("ProjectType = ")
           .append(getProjectType())
           .append("\n");
        str.append("ListType = ")
           .append(getListType())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
