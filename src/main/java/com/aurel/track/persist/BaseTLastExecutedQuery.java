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
import com.aurel.track.persist.TCLOB;
import com.aurel.track.persist.TCLOBPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TLastExecutedQueryBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TCLOBBean;



/**
 * store the last n executed queries with all data needed to execute them again
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TLastExecutedQuery
 */
public abstract class BaseTLastExecutedQuery extends TpBaseObject
{
    /** The Peer class */
    private static final TLastExecutedQueryPeer peer =
        new TLastExecutedQueryPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the person field */
    private Integer person;

    /** The value for the queryType field */
    private Integer queryType;

    /** The value for the queryClob field */
    private Integer queryClob;

    /** The value for the queryKey field */
    private Integer queryKey;

    /** The value for the lastExecutedTime field */
    private Date lastExecutedTime;

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
     * Get the QueryType
     *
     * @return Integer
     */
    public Integer getQueryType()
    {
        return queryType;
    }


    /**
     * Set the value of QueryType
     *
     * @param v new value
     */
    public void setQueryType(Integer v) 
    {

        if (!ObjectUtils.equals(this.queryType, v))
        {
            this.queryType = v;
            setModified(true);
        }


    }

    /**
     * Get the QueryClob
     *
     * @return Integer
     */
    public Integer getQueryClob()
    {
        return queryClob;
    }


    /**
     * Set the value of QueryClob
     *
     * @param v new value
     */
    public void setQueryClob(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.queryClob, v))
        {
            this.queryClob = v;
            setModified(true);
        }


        if (aTCLOB != null && !ObjectUtils.equals(aTCLOB.getObjectID(), v))
        {
            aTCLOB = null;
        }

    }

    /**
     * Get the QueryKey
     *
     * @return Integer
     */
    public Integer getQueryKey()
    {
        return queryKey;
    }


    /**
     * Set the value of QueryKey
     *
     * @param v new value
     */
    public void setQueryKey(Integer v) 
    {

        if (!ObjectUtils.equals(this.queryKey, v))
        {
            this.queryKey = v;
            setModified(true);
        }


    }

    /**
     * Get the LastExecutedTime
     *
     * @return Date
     */
    public Date getLastExecutedTime()
    {
        return lastExecutedTime;
    }


    /**
     * Set the value of LastExecutedTime
     *
     * @param v new value
     */
    public void setLastExecutedTime(Date v) 
    {

        if (!ObjectUtils.equals(this.lastExecutedTime, v))
        {
            this.lastExecutedTime = v;
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




    private TCLOB aTCLOB;

    /**
     * Declares an association between this object and a TCLOB object
     *
     * @param v TCLOB
     * @throws TorqueException
     */
    public void setTCLOB(TCLOB v) throws TorqueException
    {
        if (v == null)
        {
            setQueryClob((Integer) null);
        }
        else
        {
            setQueryClob(v.getObjectID());
        }
        aTCLOB = v;
    }


    /**
     * Returns the associated TCLOB object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TCLOB object
     * @throws TorqueException
     */
    public TCLOB getTCLOB()
        throws TorqueException
    {
        if (aTCLOB == null && (!ObjectUtils.equals(this.queryClob, null)))
        {
            aTCLOB = TCLOBPeer.retrieveByPK(SimpleKey.keyFor(this.queryClob));
        }
        return aTCLOB;
    }

    /**
     * Return the associated TCLOB object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TCLOB object
     * @throws TorqueException
     */
    public TCLOB getTCLOB(Connection connection)
        throws TorqueException
    {
        if (aTCLOB == null && (!ObjectUtils.equals(this.queryClob, null)))
        {
            aTCLOB = TCLOBPeer.retrieveByPK(SimpleKey.keyFor(this.queryClob), connection);
        }
        return aTCLOB;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTCLOBKey(ObjectKey key) throws TorqueException
    {

        setQueryClob(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("QueryType");
            fieldNames.add("QueryClob");
            fieldNames.add("QueryKey");
            fieldNames.add("LastExecutedTime");
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
        if (name.equals("QueryType"))
        {
            return getQueryType();
        }
        if (name.equals("QueryClob"))
        {
            return getQueryClob();
        }
        if (name.equals("QueryKey"))
        {
            return getQueryKey();
        }
        if (name.equals("LastExecutedTime"))
        {
            return getLastExecutedTime();
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
        if (name.equals("QueryType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setQueryType((Integer) value);
            return true;
        }
        if (name.equals("QueryClob"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setQueryClob((Integer) value);
            return true;
        }
        if (name.equals("QueryKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setQueryKey((Integer) value);
            return true;
        }
        if (name.equals("LastExecutedTime"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLastExecutedTime((Date) value);
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
        if (name.equals(TLastExecutedQueryPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TLastExecutedQueryPeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TLastExecutedQueryPeer.QUERYTYPE))
        {
            return getQueryType();
        }
        if (name.equals(TLastExecutedQueryPeer.QUERYCLOB))
        {
            return getQueryClob();
        }
        if (name.equals(TLastExecutedQueryPeer.QUERYKEY))
        {
            return getQueryKey();
        }
        if (name.equals(TLastExecutedQueryPeer.LASTEXECUTEDTIME))
        {
            return getLastExecutedTime();
        }
        if (name.equals(TLastExecutedQueryPeer.TPUUID))
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
      if (TLastExecutedQueryPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TLastExecutedQueryPeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TLastExecutedQueryPeer.QUERYTYPE.equals(name))
        {
            return setByName("QueryType", value);
        }
      if (TLastExecutedQueryPeer.QUERYCLOB.equals(name))
        {
            return setByName("QueryClob", value);
        }
      if (TLastExecutedQueryPeer.QUERYKEY.equals(name))
        {
            return setByName("QueryKey", value);
        }
      if (TLastExecutedQueryPeer.LASTEXECUTEDTIME.equals(name))
        {
            return setByName("LastExecutedTime", value);
        }
      if (TLastExecutedQueryPeer.TPUUID.equals(name))
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
            return getQueryType();
        }
        if (pos == 3)
        {
            return getQueryClob();
        }
        if (pos == 4)
        {
            return getQueryKey();
        }
        if (pos == 5)
        {
            return getLastExecutedTime();
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
            return setByName("Person", value);
        }
    if (position == 2)
        {
            return setByName("QueryType", value);
        }
    if (position == 3)
        {
            return setByName("QueryClob", value);
        }
    if (position == 4)
        {
            return setByName("QueryKey", value);
        }
    if (position == 5)
        {
            return setByName("LastExecutedTime", value);
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
        save(TLastExecutedQueryPeer.DATABASE_NAME);
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
                    TLastExecutedQueryPeer.doInsert((TLastExecutedQuery) this, con);
                    setNew(false);
                }
                else
                {
                    TLastExecutedQueryPeer.doUpdate((TLastExecutedQuery) this, con);
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
    public TLastExecutedQuery copy() throws TorqueException
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
    public TLastExecutedQuery copy(Connection con) throws TorqueException
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
    public TLastExecutedQuery copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TLastExecutedQuery(), deepcopy);
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
    public TLastExecutedQuery copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TLastExecutedQuery(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TLastExecutedQuery copyInto(TLastExecutedQuery copyObj) throws TorqueException
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
    protected TLastExecutedQuery copyInto(TLastExecutedQuery copyObj, Connection con) throws TorqueException
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
    protected TLastExecutedQuery copyInto(TLastExecutedQuery copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setQueryType(queryType);
        copyObj.setQueryClob(queryClob);
        copyObj.setQueryKey(queryKey);
        copyObj.setLastExecutedTime(lastExecutedTime);
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
    protected TLastExecutedQuery copyInto(TLastExecutedQuery copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setPerson(person);
        copyObj.setQueryType(queryType);
        copyObj.setQueryClob(queryClob);
        copyObj.setQueryKey(queryKey);
        copyObj.setLastExecutedTime(lastExecutedTime);
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
    public TLastExecutedQueryPeer getPeer()
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
        return TLastExecutedQueryPeer.getTableMap();
    }

  
    /**
     * Creates a TLastExecutedQueryBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TLastExecutedQueryBean with the contents of this object
     */
    public TLastExecutedQueryBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TLastExecutedQueryBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TLastExecutedQueryBean with the contents of this object
     */
    public TLastExecutedQueryBean getBean(IdentityMap createdBeans)
    {
        TLastExecutedQueryBean result = (TLastExecutedQueryBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TLastExecutedQueryBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setPerson(getPerson());
        result.setQueryType(getQueryType());
        result.setQueryClob(getQueryClob());
        result.setQueryKey(getQueryKey());
        result.setLastExecutedTime(getLastExecutedTime());
        result.setUuid(getUuid());





        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTCLOB != null)
        {
            TCLOBBean relatedBean = aTCLOB.getBean(createdBeans);
            result.setTCLOBBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TLastExecutedQuery with the contents
     * of a TLastExecutedQueryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TLastExecutedQueryBean which contents are used to create
     *        the resulting class
     * @return an instance of TLastExecutedQuery with the contents of bean
     */
    public static TLastExecutedQuery createTLastExecutedQuery(TLastExecutedQueryBean bean)
        throws TorqueException
    {
        return createTLastExecutedQuery(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TLastExecutedQuery with the contents
     * of a TLastExecutedQueryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TLastExecutedQueryBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TLastExecutedQuery with the contents of bean
     */

    public static TLastExecutedQuery createTLastExecutedQuery(TLastExecutedQueryBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TLastExecutedQuery result = (TLastExecutedQuery) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TLastExecutedQuery();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setPerson(bean.getPerson());
        result.setQueryType(bean.getQueryType());
        result.setQueryClob(bean.getQueryClob());
        result.setQueryKey(bean.getQueryKey());
        result.setLastExecutedTime(bean.getLastExecutedTime());
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
            TCLOBBean relatedBean = bean.getTCLOBBean();
            if (relatedBean != null)
            {
                TCLOB relatedObject = TCLOB.createTCLOB(relatedBean, createdObjects);
                result.setTCLOB(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TLastExecutedQuery:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("QueryType = ")
           .append(getQueryType())
           .append("\n");
        str.append("QueryClob = ")
           .append(getQueryClob())
           .append("\n");
        str.append("QueryKey = ")
           .append(getQueryKey())
           .append("\n");
        str.append("LastExecutedTime = ")
           .append(getLastExecutedTime())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
