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




  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TRepositoryBean;

import com.aurel.track.beans.TRevisionBean;


/**
 * Version control repositiories defined
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TRepository
 */
public abstract class BaseTRepository extends TpBaseObject
{
    /** The Peer class */
    private static final TRepositoryPeer peer =
        new TRepositoryPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the repositoryType field */
    private String repositoryType;

    /** The value for the repositoryURL field */
    private String repositoryURL;

    /** The value for the startDate field */
    private Date startDate;

    /** The value for the endDate field */
    private Date endDate;

    /** The value for the statusKey field */
    private Integer statusKey;

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
    public void setObjectID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
        }



        // update associated TRevision
        if (collTRevisions != null)
        {
            for (int i = 0; i < collTRevisions.size(); i++)
            {
                ((TRevision) collTRevisions.get(i))
                        .setRepositoryKey(v);
            }
        }
    }

    /**
     * Get the RepositoryType
     *
     * @return String
     */
    public String getRepositoryType()
    {
        return repositoryType;
    }


    /**
     * Set the value of RepositoryType
     *
     * @param v new value
     */
    public void setRepositoryType(String v) 
    {

        if (!ObjectUtils.equals(this.repositoryType, v))
        {
            this.repositoryType = v;
            setModified(true);
        }


    }

    /**
     * Get the RepositoryURL
     *
     * @return String
     */
    public String getRepositoryURL()
    {
        return repositoryURL;
    }


    /**
     * Set the value of RepositoryURL
     *
     * @param v new value
     */
    public void setRepositoryURL(String v) 
    {

        if (!ObjectUtils.equals(this.repositoryURL, v))
        {
            this.repositoryURL = v;
            setModified(true);
        }


    }

    /**
     * Get the StartDate
     *
     * @return Date
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Set the value of StartDate
     *
     * @param v new value
     */
    public void setStartDate(Date v) 
    {

        if (!ObjectUtils.equals(this.startDate, v))
        {
            this.startDate = v;
            setModified(true);
        }


    }

    /**
     * Get the EndDate
     *
     * @return Date
     */
    public Date getEndDate()
    {
        return endDate;
    }


    /**
     * Set the value of EndDate
     *
     * @param v new value
     */
    public void setEndDate(Date v) 
    {

        if (!ObjectUtils.equals(this.endDate, v))
        {
            this.endDate = v;
            setModified(true);
        }


    }

    /**
     * Get the StatusKey
     *
     * @return Integer
     */
    public Integer getStatusKey()
    {
        return statusKey;
    }


    /**
     * Set the value of StatusKey
     *
     * @param v new value
     */
    public void setStatusKey(Integer v) 
    {

        if (!ObjectUtils.equals(this.statusKey, v))
        {
            this.statusKey = v;
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

       


    /**
     * Collection to store aggregation of collTRevisions
     */
    protected List<TRevision> collTRevisions;

    /**
     * Temporary storage of collTRevisions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTRevisions()
    {
        if (collTRevisions == null)
        {
            collTRevisions = new ArrayList<TRevision>();
        }
    }


    /**
     * Method called to associate a TRevision object to this object
     * through the TRevision foreign key attribute
     *
     * @param l TRevision
     * @throws TorqueException
     */
    public void addTRevision(TRevision l) throws TorqueException
    {
        getTRevisions().add(l);
        l.setTRepository((TRepository) this);
    }

    /**
     * Method called to associate a TRevision object to this object
     * through the TRevision foreign key attribute using connection.
     *
     * @param l TRevision
     * @throws TorqueException
     */
    public void addTRevision(TRevision l, Connection con) throws TorqueException
    {
        getTRevisions(con).add(l);
        l.setTRepository((TRepository) this);
    }

    /**
     * The criteria used to select the current contents of collTRevisions
     */
    private Criteria lastTRevisionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRevisions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TRevision> getTRevisions()
        throws TorqueException
    {
        if (collTRevisions == null)
        {
            collTRevisions = getTRevisions(new Criteria(10));
        }
        return collTRevisions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRepository has previously
     * been saved, it will retrieve related TRevisions from storage.
     * If this TRepository is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TRevision> getTRevisions(Criteria criteria) throws TorqueException
    {
        if (collTRevisions == null)
        {
            if (isNew())
            {
               collTRevisions = new ArrayList<TRevision>();
            }
            else
            {
                criteria.add(TRevisionPeer.REPOSITORYKEY, getObjectID() );
                collTRevisions = TRevisionPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TRevisionPeer.REPOSITORYKEY, getObjectID());
                if (!lastTRevisionsCriteria.equals(criteria))
                {
                    collTRevisions = TRevisionPeer.doSelect(criteria);
                }
            }
        }
        lastTRevisionsCriteria = criteria;

        return collTRevisions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRevisions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRevision> getTRevisions(Connection con) throws TorqueException
    {
        if (collTRevisions == null)
        {
            collTRevisions = getTRevisions(new Criteria(10), con);
        }
        return collTRevisions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRepository has previously
     * been saved, it will retrieve related TRevisions from storage.
     * If this TRepository is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRevision> getTRevisions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTRevisions == null)
        {
            if (isNew())
            {
               collTRevisions = new ArrayList<TRevision>();
            }
            else
            {
                 criteria.add(TRevisionPeer.REPOSITORYKEY, getObjectID());
                 collTRevisions = TRevisionPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TRevisionPeer.REPOSITORYKEY, getObjectID());
                 if (!lastTRevisionsCriteria.equals(criteria))
                 {
                     collTRevisions = TRevisionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTRevisionsCriteria = criteria;

         return collTRevisions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRepository is new, it will return
     * an empty collection; or if this TRepository has previously
     * been saved, it will retrieve related TRevisions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRepository.
     */
    protected List<TRevision> getTRevisionsJoinTRepository(Criteria criteria)
        throws TorqueException
    {
        if (collTRevisions == null)
        {
            if (isNew())
            {
               collTRevisions = new ArrayList<TRevision>();
            }
            else
            {
                criteria.add(TRevisionPeer.REPOSITORYKEY, getObjectID());
                collTRevisions = TRevisionPeer.doSelectJoinTRepository(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TRevisionPeer.REPOSITORYKEY, getObjectID());
            if (!lastTRevisionsCriteria.equals(criteria))
            {
                collTRevisions = TRevisionPeer.doSelectJoinTRepository(criteria);
            }
        }
        lastTRevisionsCriteria = criteria;

        return collTRevisions;
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
            fieldNames.add("RepositoryType");
            fieldNames.add("RepositoryURL");
            fieldNames.add("StartDate");
            fieldNames.add("EndDate");
            fieldNames.add("StatusKey");
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
        if (name.equals("RepositoryType"))
        {
            return getRepositoryType();
        }
        if (name.equals("RepositoryURL"))
        {
            return getRepositoryURL();
        }
        if (name.equals("StartDate"))
        {
            return getStartDate();
        }
        if (name.equals("EndDate"))
        {
            return getEndDate();
        }
        if (name.equals("StatusKey"))
        {
            return getStatusKey();
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
        if (name.equals("RepositoryType"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRepositoryType((String) value);
            return true;
        }
        if (name.equals("RepositoryURL"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRepositoryURL((String) value);
            return true;
        }
        if (name.equals("StartDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStartDate((Date) value);
            return true;
        }
        if (name.equals("EndDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEndDate((Date) value);
            return true;
        }
        if (name.equals("StatusKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStatusKey((Integer) value);
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
        if (name.equals(TRepositoryPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TRepositoryPeer.REPOSITORYTYPE))
        {
            return getRepositoryType();
        }
        if (name.equals(TRepositoryPeer.REPOSITORYURL))
        {
            return getRepositoryURL();
        }
        if (name.equals(TRepositoryPeer.STARTDATE))
        {
            return getStartDate();
        }
        if (name.equals(TRepositoryPeer.ENDDATE))
        {
            return getEndDate();
        }
        if (name.equals(TRepositoryPeer.STATUSKEY))
        {
            return getStatusKey();
        }
        if (name.equals(TRepositoryPeer.TPUUID))
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
      if (TRepositoryPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TRepositoryPeer.REPOSITORYTYPE.equals(name))
        {
            return setByName("RepositoryType", value);
        }
      if (TRepositoryPeer.REPOSITORYURL.equals(name))
        {
            return setByName("RepositoryURL", value);
        }
      if (TRepositoryPeer.STARTDATE.equals(name))
        {
            return setByName("StartDate", value);
        }
      if (TRepositoryPeer.ENDDATE.equals(name))
        {
            return setByName("EndDate", value);
        }
      if (TRepositoryPeer.STATUSKEY.equals(name))
        {
            return setByName("StatusKey", value);
        }
      if (TRepositoryPeer.TPUUID.equals(name))
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
            return getRepositoryType();
        }
        if (pos == 2)
        {
            return getRepositoryURL();
        }
        if (pos == 3)
        {
            return getStartDate();
        }
        if (pos == 4)
        {
            return getEndDate();
        }
        if (pos == 5)
        {
            return getStatusKey();
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
            return setByName("RepositoryType", value);
        }
    if (position == 2)
        {
            return setByName("RepositoryURL", value);
        }
    if (position == 3)
        {
            return setByName("StartDate", value);
        }
    if (position == 4)
        {
            return setByName("EndDate", value);
        }
    if (position == 5)
        {
            return setByName("StatusKey", value);
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
        save(TRepositoryPeer.DATABASE_NAME);
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
                    TRepositoryPeer.doInsert((TRepository) this, con);
                    setNew(false);
                }
                else
                {
                    TRepositoryPeer.doUpdate((TRepository) this, con);
                }
            }


            if (collTRevisions != null)
            {
                for (int i = 0; i < collTRevisions.size(); i++)
                {
                    ((TRevision) collTRevisions.get(i)).save(con);
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
        throws TorqueException
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) throws TorqueException
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
    public TRepository copy() throws TorqueException
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
    public TRepository copy(Connection con) throws TorqueException
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
    public TRepository copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TRepository(), deepcopy);
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
    public TRepository copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TRepository(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TRepository copyInto(TRepository copyObj) throws TorqueException
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
    protected TRepository copyInto(TRepository copyObj, Connection con) throws TorqueException
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
    protected TRepository copyInto(TRepository copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setRepositoryType(repositoryType);
        copyObj.setRepositoryURL(repositoryURL);
        copyObj.setStartDate(startDate);
        copyObj.setEndDate(endDate);
        copyObj.setStatusKey(statusKey);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TRevision> vTRevisions = getTRevisions();
        if (vTRevisions != null)
        {
            for (int i = 0; i < vTRevisions.size(); i++)
            {
                TRevision obj =  vTRevisions.get(i);
                copyObj.addTRevision(obj.copy());
            }
        }
        else
        {
            copyObj.collTRevisions = null;
        }
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
    protected TRepository copyInto(TRepository copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setRepositoryType(repositoryType);
        copyObj.setRepositoryURL(repositoryURL);
        copyObj.setStartDate(startDate);
        copyObj.setEndDate(endDate);
        copyObj.setStatusKey(statusKey);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TRevision> vTRevisions = getTRevisions(con);
        if (vTRevisions != null)
        {
            for (int i = 0; i < vTRevisions.size(); i++)
            {
                TRevision obj =  vTRevisions.get(i);
                copyObj.addTRevision(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTRevisions = null;
        }
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TRepositoryPeer getPeer()
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
        return TRepositoryPeer.getTableMap();
    }

  
    /**
     * Creates a TRepositoryBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TRepositoryBean with the contents of this object
     */
    public TRepositoryBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TRepositoryBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TRepositoryBean with the contents of this object
     */
    public TRepositoryBean getBean(IdentityMap createdBeans)
    {
        TRepositoryBean result = (TRepositoryBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TRepositoryBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setRepositoryType(getRepositoryType());
        result.setRepositoryURL(getRepositoryURL());
        result.setStartDate(getStartDate());
        result.setEndDate(getEndDate());
        result.setStatusKey(getStatusKey());
        result.setUuid(getUuid());



        if (collTRevisions != null)
        {
            List<TRevisionBean> relatedBeans = new ArrayList<TRevisionBean>(collTRevisions.size());
            for (Iterator<TRevision> collTRevisionsIt = collTRevisions.iterator(); collTRevisionsIt.hasNext(); )
            {
                TRevision related = (TRevision) collTRevisionsIt.next();
                TRevisionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTRevisionBeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TRepository with the contents
     * of a TRepositoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TRepositoryBean which contents are used to create
     *        the resulting class
     * @return an instance of TRepository with the contents of bean
     */
    public static TRepository createTRepository(TRepositoryBean bean)
        throws TorqueException
    {
        return createTRepository(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TRepository with the contents
     * of a TRepositoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TRepositoryBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TRepository with the contents of bean
     */

    public static TRepository createTRepository(TRepositoryBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TRepository result = (TRepository) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TRepository();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setRepositoryType(bean.getRepositoryType());
        result.setRepositoryURL(bean.getRepositoryURL());
        result.setStartDate(bean.getStartDate());
        result.setEndDate(bean.getEndDate());
        result.setStatusKey(bean.getStatusKey());
        result.setUuid(bean.getUuid());



        {
            List<TRevisionBean> relatedBeans = bean.getTRevisionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TRevisionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TRevisionBean relatedBean =  relatedBeansIt.next();
                    TRevision related = TRevision.createTRevision(relatedBean, createdObjects);
                    result.addTRevisionFromBean(related);
                }
            }
        }

    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TRevision object to this object.
     * through the TRevision foreign key attribute
     *
     * @param toAdd TRevision
     */
    protected void addTRevisionFromBean(TRevision toAdd)
    {
        initTRevisions();
        collTRevisions.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TRepository:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("RepositoryType = ")
           .append(getRepositoryType())
           .append("\n");
        str.append("RepositoryURL = ")
           .append(getRepositoryURL())
           .append("\n");
        str.append("StartDate = ")
           .append(getStartDate())
           .append("\n");
        str.append("EndDate = ")
           .append(getEndDate())
           .append("\n");
        str.append("StatusKey = ")
           .append(getStatusKey())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
