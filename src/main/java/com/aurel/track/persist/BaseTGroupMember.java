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
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TGroupMemberBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPersonBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TGroupMember
 */
public abstract class BaseTGroupMember extends TpBaseObject
{
    /** The Peer class */
    private static final TGroupMemberPeer peer =
        new TGroupMemberPeer();


    /** The value for the objectId field */
    private Integer objectId;

    /** The value for the theGroup field */
    private Integer theGroup;

    /** The value for the person field */
    private Integer person;

    /** The value for the uuid field */
    private String uuid;


    /**
     * Get the ObjectId
     *
     * @return Integer
     */
    public Integer getObjectId()
    {
        return objectId;
    }


    /**
     * Set the value of ObjectId
     *
     * @param v new value
     */
    public void setObjectId(Integer v) 
    {

        if (!ObjectUtils.equals(this.objectId, v))
        {
            this.objectId = v;
            setModified(true);
        }


    }

    /**
     * Get the TheGroup
     *
     * @return Integer
     */
    public Integer getTheGroup()
    {
        return theGroup;
    }


    /**
     * Set the value of TheGroup
     *
     * @param v new value
     */
    public void setTheGroup(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.theGroup, v))
        {
            this.theGroup = v;
            setModified(true);
        }


        if (aTPersonRelatedByTheGroup != null && !ObjectUtils.equals(aTPersonRelatedByTheGroup.getObjectID(), v))
        {
            aTPersonRelatedByTheGroup = null;
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


        if (aTPersonRelatedByPerson != null && !ObjectUtils.equals(aTPersonRelatedByPerson.getObjectID(), v))
        {
            aTPersonRelatedByPerson = null;
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

    



    private TPerson aTPersonRelatedByTheGroup;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByTheGroup(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setTheGroup((Integer) null);
        }
        else
        {
            setTheGroup(v.getObjectID());
        }
        aTPersonRelatedByTheGroup = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByTheGroup()
        throws TorqueException
    {
        if (aTPersonRelatedByTheGroup == null && (!ObjectUtils.equals(this.theGroup, null)))
        {
            aTPersonRelatedByTheGroup = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.theGroup));
        }
        return aTPersonRelatedByTheGroup;
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
    public TPerson getTPersonRelatedByTheGroup(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByTheGroup == null && (!ObjectUtils.equals(this.theGroup, null)))
        {
            aTPersonRelatedByTheGroup = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.theGroup), connection);
        }
        return aTPersonRelatedByTheGroup;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByTheGroupKey(ObjectKey key) throws TorqueException
    {

        setTheGroup(new Integer(((NumberKey) key).intValue()));
    }




    private TPerson aTPersonRelatedByPerson;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByPerson(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setPerson((Integer) null);
        }
        else
        {
            setPerson(v.getObjectID());
        }
        aTPersonRelatedByPerson = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByPerson()
        throws TorqueException
    {
        if (aTPersonRelatedByPerson == null && (!ObjectUtils.equals(this.person, null)))
        {
            aTPersonRelatedByPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.person));
        }
        return aTPersonRelatedByPerson;
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
    public TPerson getTPersonRelatedByPerson(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByPerson == null && (!ObjectUtils.equals(this.person, null)))
        {
            aTPersonRelatedByPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.person), connection);
        }
        return aTPersonRelatedByPerson;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByPersonKey(ObjectKey key) throws TorqueException
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
            fieldNames.add("ObjectId");
            fieldNames.add("TheGroup");
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
        if (name.equals("ObjectId"))
        {
            return getObjectId();
        }
        if (name.equals("TheGroup"))
        {
            return getTheGroup();
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
        if (name.equals("ObjectId"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setObjectId((Integer) value);
            return true;
        }
        if (name.equals("TheGroup"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTheGroup((Integer) value);
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
        if (name.equals(TGroupMemberPeer.OBJECTID))
        {
            return getObjectId();
        }
        if (name.equals(TGroupMemberPeer.THEGROUP))
        {
            return getTheGroup();
        }
        if (name.equals(TGroupMemberPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TGroupMemberPeer.TPUUID))
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
      if (TGroupMemberPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectId", value);
        }
      if (TGroupMemberPeer.THEGROUP.equals(name))
        {
            return setByName("TheGroup", value);
        }
      if (TGroupMemberPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TGroupMemberPeer.TPUUID.equals(name))
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
            return getObjectId();
        }
        if (pos == 1)
        {
            return getTheGroup();
        }
        if (pos == 2)
        {
            return getPerson();
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
            return setByName("ObjectId", value);
        }
    if (position == 1)
        {
            return setByName("TheGroup", value);
        }
    if (position == 2)
        {
            return setByName("Person", value);
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
        save(TGroupMemberPeer.DATABASE_NAME);
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
                    TGroupMemberPeer.doInsert((TGroupMember) this, con);
                    setNew(false);
                }
                else
                {
                    TGroupMemberPeer.doUpdate((TGroupMember) this, con);
                }
            }

            alreadyInSave = false;
        }
    }


    /**
     * Set the PrimaryKey using ObjectKey.
     *
     * @param key objectId ObjectKey
     */
    public void setPrimaryKey(ObjectKey key)
        
    {
        setObjectId(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) 
    {
        setObjectId(new Integer(key));
    }


    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
        return SimpleKey.keyFor(getObjectId());
    }
 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     */
    public TGroupMember copy() throws TorqueException
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
    public TGroupMember copy(Connection con) throws TorqueException
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
    public TGroupMember copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TGroupMember(), deepcopy);
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
    public TGroupMember copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TGroupMember(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TGroupMember copyInto(TGroupMember copyObj) throws TorqueException
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
    protected TGroupMember copyInto(TGroupMember copyObj, Connection con) throws TorqueException
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
    protected TGroupMember copyInto(TGroupMember copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectId(objectId);
        copyObj.setTheGroup(theGroup);
        copyObj.setPerson(person);
        copyObj.setUuid(uuid);

        copyObj.setObjectId((Integer)null);

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
    protected TGroupMember copyInto(TGroupMember copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectId(objectId);
        copyObj.setTheGroup(theGroup);
        copyObj.setPerson(person);
        copyObj.setUuid(uuid);

        copyObj.setObjectId((Integer)null);

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
    public TGroupMemberPeer getPeer()
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
        return TGroupMemberPeer.getTableMap();
    }

  
    /**
     * Creates a TGroupMemberBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TGroupMemberBean with the contents of this object
     */
    public TGroupMemberBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TGroupMemberBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TGroupMemberBean with the contents of this object
     */
    public TGroupMemberBean getBean(IdentityMap createdBeans)
    {
        TGroupMemberBean result = (TGroupMemberBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TGroupMemberBean();
        createdBeans.put(this, result);

        result.setObjectId(getObjectId());
        result.setTheGroup(getTheGroup());
        result.setPerson(getPerson());
        result.setUuid(getUuid());





        if (aTPersonRelatedByTheGroup != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByTheGroup.getBean(createdBeans);
            result.setTPersonBeanRelatedByTheGroup(relatedBean);
        }



        if (aTPersonRelatedByPerson != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByPerson.getBean(createdBeans);
            result.setTPersonBeanRelatedByPerson(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TGroupMember with the contents
     * of a TGroupMemberBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TGroupMemberBean which contents are used to create
     *        the resulting class
     * @return an instance of TGroupMember with the contents of bean
     */
    public static TGroupMember createTGroupMember(TGroupMemberBean bean)
        throws TorqueException
    {
        return createTGroupMember(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TGroupMember with the contents
     * of a TGroupMemberBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TGroupMemberBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TGroupMember with the contents of bean
     */

    public static TGroupMember createTGroupMember(TGroupMemberBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TGroupMember result = (TGroupMember) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TGroupMember();
        createdObjects.put(bean, result);

        result.setObjectId(bean.getObjectId());
        result.setTheGroup(bean.getTheGroup());
        result.setPerson(bean.getPerson());
        result.setUuid(bean.getUuid());





        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByTheGroup();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByTheGroup(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByPerson();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByPerson(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TGroupMember:\n");
        str.append("ObjectId = ")
           .append(getObjectId())
           .append("\n");
        str.append("TheGroup = ")
           .append(getTheGroup())
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
