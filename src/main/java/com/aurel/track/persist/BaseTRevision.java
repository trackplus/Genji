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



import com.aurel.track.persist.TRepository;
import com.aurel.track.persist.TRepositoryPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TRevisionBean;
import com.aurel.track.beans.TRepositoryBean;

import com.aurel.track.beans.TRevisionWorkitemsBean;


/**
 * Comments from the version control system
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TRevision
 */
public abstract class BaseTRevision extends TpBaseObject
{
    /** The Peer class */
    private static final TRevisionPeer peer =
        new TRevisionPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the fileName field */
    private String fileName;

    /** The value for the authorName field */
    private String authorName;

    /** The value for the changeDescription field */
    private String changeDescription;

    /** The value for the revisionDate field */
    private Date revisionDate;

    /** The value for the revisionNumber field */
    private String revisionNumber;

    /** The value for the repositoryKey field */
    private Integer repositoryKey;

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



        // update associated TRevisionWorkitems
        if (collTRevisionWorkitemss != null)
        {
            for (int i = 0; i < collTRevisionWorkitemss.size(); i++)
            {
                ((TRevisionWorkitems) collTRevisionWorkitemss.get(i))
                        .setRevisionKey(v);
            }
        }
    }

    /**
     * Get the FileName
     *
     * @return String
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * Set the value of FileName
     *
     * @param v new value
     */
    public void setFileName(String v) 
    {

        if (!ObjectUtils.equals(this.fileName, v))
        {
            this.fileName = v;
            setModified(true);
        }


    }

    /**
     * Get the AuthorName
     *
     * @return String
     */
    public String getAuthorName()
    {
        return authorName;
    }


    /**
     * Set the value of AuthorName
     *
     * @param v new value
     */
    public void setAuthorName(String v) 
    {

        if (!ObjectUtils.equals(this.authorName, v))
        {
            this.authorName = v;
            setModified(true);
        }


    }

    /**
     * Get the ChangeDescription
     *
     * @return String
     */
    public String getChangeDescription()
    {
        return changeDescription;
    }


    /**
     * Set the value of ChangeDescription
     *
     * @param v new value
     */
    public void setChangeDescription(String v) 
    {

        if (!ObjectUtils.equals(this.changeDescription, v))
        {
            this.changeDescription = v;
            setModified(true);
        }


    }

    /**
     * Get the RevisionDate
     *
     * @return Date
     */
    public Date getRevisionDate()
    {
        return revisionDate;
    }


    /**
     * Set the value of RevisionDate
     *
     * @param v new value
     */
    public void setRevisionDate(Date v) 
    {

        if (!ObjectUtils.equals(this.revisionDate, v))
        {
            this.revisionDate = v;
            setModified(true);
        }


    }

    /**
     * Get the RevisionNumber
     *
     * @return String
     */
    public String getRevisionNumber()
    {
        return revisionNumber;
    }


    /**
     * Set the value of RevisionNumber
     *
     * @param v new value
     */
    public void setRevisionNumber(String v) 
    {

        if (!ObjectUtils.equals(this.revisionNumber, v))
        {
            this.revisionNumber = v;
            setModified(true);
        }


    }

    /**
     * Get the RepositoryKey
     *
     * @return Integer
     */
    public Integer getRepositoryKey()
    {
        return repositoryKey;
    }


    /**
     * Set the value of RepositoryKey
     *
     * @param v new value
     */
    public void setRepositoryKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.repositoryKey, v))
        {
            this.repositoryKey = v;
            setModified(true);
        }


        if (aTRepository != null && !ObjectUtils.equals(aTRepository.getObjectID(), v))
        {
            aTRepository = null;
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

    



    private TRepository aTRepository;

    /**
     * Declares an association between this object and a TRepository object
     *
     * @param v TRepository
     * @throws TorqueException
     */
    public void setTRepository(TRepository v) throws TorqueException
    {
        if (v == null)
        {
            setRepositoryKey((Integer) null);
        }
        else
        {
            setRepositoryKey(v.getObjectID());
        }
        aTRepository = v;
    }


    /**
     * Returns the associated TRepository object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TRepository object
     * @throws TorqueException
     */
    public TRepository getTRepository()
        throws TorqueException
    {
        if (aTRepository == null && (!ObjectUtils.equals(this.repositoryKey, null)))
        {
            aTRepository = TRepositoryPeer.retrieveByPK(SimpleKey.keyFor(this.repositoryKey));
        }
        return aTRepository;
    }

    /**
     * Return the associated TRepository object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TRepository object
     * @throws TorqueException
     */
    public TRepository getTRepository(Connection connection)
        throws TorqueException
    {
        if (aTRepository == null && (!ObjectUtils.equals(this.repositoryKey, null)))
        {
            aTRepository = TRepositoryPeer.retrieveByPK(SimpleKey.keyFor(this.repositoryKey), connection);
        }
        return aTRepository;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTRepositoryKey(ObjectKey key) throws TorqueException
    {

        setRepositoryKey(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTRevisionWorkitemss
     */
    protected List<TRevisionWorkitems> collTRevisionWorkitemss;

    /**
     * Temporary storage of collTRevisionWorkitemss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTRevisionWorkitemss()
    {
        if (collTRevisionWorkitemss == null)
        {
            collTRevisionWorkitemss = new ArrayList<TRevisionWorkitems>();
        }
    }


    /**
     * Method called to associate a TRevisionWorkitems object to this object
     * through the TRevisionWorkitems foreign key attribute
     *
     * @param l TRevisionWorkitems
     * @throws TorqueException
     */
    public void addTRevisionWorkitems(TRevisionWorkitems l) throws TorqueException
    {
        getTRevisionWorkitemss().add(l);
        l.setTRevision((TRevision) this);
    }

    /**
     * Method called to associate a TRevisionWorkitems object to this object
     * through the TRevisionWorkitems foreign key attribute using connection.
     *
     * @param l TRevisionWorkitems
     * @throws TorqueException
     */
    public void addTRevisionWorkitems(TRevisionWorkitems l, Connection con) throws TorqueException
    {
        getTRevisionWorkitemss(con).add(l);
        l.setTRevision((TRevision) this);
    }

    /**
     * The criteria used to select the current contents of collTRevisionWorkitemss
     */
    private Criteria lastTRevisionWorkitemssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRevisionWorkitemss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TRevisionWorkitems> getTRevisionWorkitemss()
        throws TorqueException
    {
        if (collTRevisionWorkitemss == null)
        {
            collTRevisionWorkitemss = getTRevisionWorkitemss(new Criteria(10));
        }
        return collTRevisionWorkitemss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRevision has previously
     * been saved, it will retrieve related TRevisionWorkitemss from storage.
     * If this TRevision is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TRevisionWorkitems> getTRevisionWorkitemss(Criteria criteria) throws TorqueException
    {
        if (collTRevisionWorkitemss == null)
        {
            if (isNew())
            {
               collTRevisionWorkitemss = new ArrayList<TRevisionWorkitems>();
            }
            else
            {
                criteria.add(TRevisionWorkitemsPeer.REVISIONKEY, getObjectID() );
                collTRevisionWorkitemss = TRevisionWorkitemsPeer.doSelect(criteria);
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
                criteria.add(TRevisionWorkitemsPeer.REVISIONKEY, getObjectID());
                if (!lastTRevisionWorkitemssCriteria.equals(criteria))
                {
                    collTRevisionWorkitemss = TRevisionWorkitemsPeer.doSelect(criteria);
                }
            }
        }
        lastTRevisionWorkitemssCriteria = criteria;

        return collTRevisionWorkitemss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRevisionWorkitemss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRevisionWorkitems> getTRevisionWorkitemss(Connection con) throws TorqueException
    {
        if (collTRevisionWorkitemss == null)
        {
            collTRevisionWorkitemss = getTRevisionWorkitemss(new Criteria(10), con);
        }
        return collTRevisionWorkitemss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRevision has previously
     * been saved, it will retrieve related TRevisionWorkitemss from storage.
     * If this TRevision is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRevisionWorkitems> getTRevisionWorkitemss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTRevisionWorkitemss == null)
        {
            if (isNew())
            {
               collTRevisionWorkitemss = new ArrayList<TRevisionWorkitems>();
            }
            else
            {
                 criteria.add(TRevisionWorkitemsPeer.REVISIONKEY, getObjectID());
                 collTRevisionWorkitemss = TRevisionWorkitemsPeer.doSelect(criteria, con);
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
                 criteria.add(TRevisionWorkitemsPeer.REVISIONKEY, getObjectID());
                 if (!lastTRevisionWorkitemssCriteria.equals(criteria))
                 {
                     collTRevisionWorkitemss = TRevisionWorkitemsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTRevisionWorkitemssCriteria = criteria;

         return collTRevisionWorkitemss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRevision is new, it will return
     * an empty collection; or if this TRevision has previously
     * been saved, it will retrieve related TRevisionWorkitemss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRevision.
     */
    protected List<TRevisionWorkitems> getTRevisionWorkitemssJoinTRevision(Criteria criteria)
        throws TorqueException
    {
        if (collTRevisionWorkitemss == null)
        {
            if (isNew())
            {
               collTRevisionWorkitemss = new ArrayList<TRevisionWorkitems>();
            }
            else
            {
                criteria.add(TRevisionWorkitemsPeer.REVISIONKEY, getObjectID());
                collTRevisionWorkitemss = TRevisionWorkitemsPeer.doSelectJoinTRevision(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TRevisionWorkitemsPeer.REVISIONKEY, getObjectID());
            if (!lastTRevisionWorkitemssCriteria.equals(criteria))
            {
                collTRevisionWorkitemss = TRevisionWorkitemsPeer.doSelectJoinTRevision(criteria);
            }
        }
        lastTRevisionWorkitemssCriteria = criteria;

        return collTRevisionWorkitemss;
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
            fieldNames.add("FileName");
            fieldNames.add("AuthorName");
            fieldNames.add("ChangeDescription");
            fieldNames.add("RevisionDate");
            fieldNames.add("RevisionNumber");
            fieldNames.add("RepositoryKey");
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
        if (name.equals("FileName"))
        {
            return getFileName();
        }
        if (name.equals("AuthorName"))
        {
            return getAuthorName();
        }
        if (name.equals("ChangeDescription"))
        {
            return getChangeDescription();
        }
        if (name.equals("RevisionDate"))
        {
            return getRevisionDate();
        }
        if (name.equals("RevisionNumber"))
        {
            return getRevisionNumber();
        }
        if (name.equals("RepositoryKey"))
        {
            return getRepositoryKey();
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
        if (name.equals("FileName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFileName((String) value);
            return true;
        }
        if (name.equals("AuthorName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAuthorName((String) value);
            return true;
        }
        if (name.equals("ChangeDescription"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setChangeDescription((String) value);
            return true;
        }
        if (name.equals("RevisionDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRevisionDate((Date) value);
            return true;
        }
        if (name.equals("RevisionNumber"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRevisionNumber((String) value);
            return true;
        }
        if (name.equals("RepositoryKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRepositoryKey((Integer) value);
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
        if (name.equals(TRevisionPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TRevisionPeer.FILENAME))
        {
            return getFileName();
        }
        if (name.equals(TRevisionPeer.AUTHORNAME))
        {
            return getAuthorName();
        }
        if (name.equals(TRevisionPeer.CHANGEDESCRIPTION))
        {
            return getChangeDescription();
        }
        if (name.equals(TRevisionPeer.REVISIONDATE))
        {
            return getRevisionDate();
        }
        if (name.equals(TRevisionPeer.REVISIONNUMBR))
        {
            return getRevisionNumber();
        }
        if (name.equals(TRevisionPeer.REPOSITORYKEY))
        {
            return getRepositoryKey();
        }
        if (name.equals(TRevisionPeer.TPUUID))
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
      if (TRevisionPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TRevisionPeer.FILENAME.equals(name))
        {
            return setByName("FileName", value);
        }
      if (TRevisionPeer.AUTHORNAME.equals(name))
        {
            return setByName("AuthorName", value);
        }
      if (TRevisionPeer.CHANGEDESCRIPTION.equals(name))
        {
            return setByName("ChangeDescription", value);
        }
      if (TRevisionPeer.REVISIONDATE.equals(name))
        {
            return setByName("RevisionDate", value);
        }
      if (TRevisionPeer.REVISIONNUMBR.equals(name))
        {
            return setByName("RevisionNumber", value);
        }
      if (TRevisionPeer.REPOSITORYKEY.equals(name))
        {
            return setByName("RepositoryKey", value);
        }
      if (TRevisionPeer.TPUUID.equals(name))
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
            return getFileName();
        }
        if (pos == 2)
        {
            return getAuthorName();
        }
        if (pos == 3)
        {
            return getChangeDescription();
        }
        if (pos == 4)
        {
            return getRevisionDate();
        }
        if (pos == 5)
        {
            return getRevisionNumber();
        }
        if (pos == 6)
        {
            return getRepositoryKey();
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
            return setByName("FileName", value);
        }
    if (position == 2)
        {
            return setByName("AuthorName", value);
        }
    if (position == 3)
        {
            return setByName("ChangeDescription", value);
        }
    if (position == 4)
        {
            return setByName("RevisionDate", value);
        }
    if (position == 5)
        {
            return setByName("RevisionNumber", value);
        }
    if (position == 6)
        {
            return setByName("RepositoryKey", value);
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
        save(TRevisionPeer.DATABASE_NAME);
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
                    TRevisionPeer.doInsert((TRevision) this, con);
                    setNew(false);
                }
                else
                {
                    TRevisionPeer.doUpdate((TRevision) this, con);
                }
            }


            if (collTRevisionWorkitemss != null)
            {
                for (int i = 0; i < collTRevisionWorkitemss.size(); i++)
                {
                    ((TRevisionWorkitems) collTRevisionWorkitemss.get(i)).save(con);
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
    public TRevision copy() throws TorqueException
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
    public TRevision copy(Connection con) throws TorqueException
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
    public TRevision copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TRevision(), deepcopy);
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
    public TRevision copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TRevision(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TRevision copyInto(TRevision copyObj) throws TorqueException
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
    protected TRevision copyInto(TRevision copyObj, Connection con) throws TorqueException
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
    protected TRevision copyInto(TRevision copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setFileName(fileName);
        copyObj.setAuthorName(authorName);
        copyObj.setChangeDescription(changeDescription);
        copyObj.setRevisionDate(revisionDate);
        copyObj.setRevisionNumber(revisionNumber);
        copyObj.setRepositoryKey(repositoryKey);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TRevisionWorkitems> vTRevisionWorkitemss = getTRevisionWorkitemss();
        if (vTRevisionWorkitemss != null)
        {
            for (int i = 0; i < vTRevisionWorkitemss.size(); i++)
            {
                TRevisionWorkitems obj =  vTRevisionWorkitemss.get(i);
                copyObj.addTRevisionWorkitems(obj.copy());
            }
        }
        else
        {
            copyObj.collTRevisionWorkitemss = null;
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
    protected TRevision copyInto(TRevision copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setFileName(fileName);
        copyObj.setAuthorName(authorName);
        copyObj.setChangeDescription(changeDescription);
        copyObj.setRevisionDate(revisionDate);
        copyObj.setRevisionNumber(revisionNumber);
        copyObj.setRepositoryKey(repositoryKey);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TRevisionWorkitems> vTRevisionWorkitemss = getTRevisionWorkitemss(con);
        if (vTRevisionWorkitemss != null)
        {
            for (int i = 0; i < vTRevisionWorkitemss.size(); i++)
            {
                TRevisionWorkitems obj =  vTRevisionWorkitemss.get(i);
                copyObj.addTRevisionWorkitems(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTRevisionWorkitemss = null;
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
    public TRevisionPeer getPeer()
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
        return TRevisionPeer.getTableMap();
    }

  
    /**
     * Creates a TRevisionBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TRevisionBean with the contents of this object
     */
    public TRevisionBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TRevisionBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TRevisionBean with the contents of this object
     */
    public TRevisionBean getBean(IdentityMap createdBeans)
    {
        TRevisionBean result = (TRevisionBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TRevisionBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setFileName(getFileName());
        result.setAuthorName(getAuthorName());
        result.setChangeDescription(getChangeDescription());
        result.setRevisionDate(getRevisionDate());
        result.setRevisionNumber(getRevisionNumber());
        result.setRepositoryKey(getRepositoryKey());
        result.setUuid(getUuid());



        if (collTRevisionWorkitemss != null)
        {
            List<TRevisionWorkitemsBean> relatedBeans = new ArrayList<TRevisionWorkitemsBean>(collTRevisionWorkitemss.size());
            for (Iterator<TRevisionWorkitems> collTRevisionWorkitemssIt = collTRevisionWorkitemss.iterator(); collTRevisionWorkitemssIt.hasNext(); )
            {
                TRevisionWorkitems related = (TRevisionWorkitems) collTRevisionWorkitemssIt.next();
                TRevisionWorkitemsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTRevisionWorkitemsBeans(relatedBeans);
        }




        if (aTRepository != null)
        {
            TRepositoryBean relatedBean = aTRepository.getBean(createdBeans);
            result.setTRepositoryBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TRevision with the contents
     * of a TRevisionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TRevisionBean which contents are used to create
     *        the resulting class
     * @return an instance of TRevision with the contents of bean
     */
    public static TRevision createTRevision(TRevisionBean bean)
        throws TorqueException
    {
        return createTRevision(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TRevision with the contents
     * of a TRevisionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TRevisionBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TRevision with the contents of bean
     */

    public static TRevision createTRevision(TRevisionBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TRevision result = (TRevision) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TRevision();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setFileName(bean.getFileName());
        result.setAuthorName(bean.getAuthorName());
        result.setChangeDescription(bean.getChangeDescription());
        result.setRevisionDate(bean.getRevisionDate());
        result.setRevisionNumber(bean.getRevisionNumber());
        result.setRepositoryKey(bean.getRepositoryKey());
        result.setUuid(bean.getUuid());



        {
            List<TRevisionWorkitemsBean> relatedBeans = bean.getTRevisionWorkitemsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TRevisionWorkitemsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TRevisionWorkitemsBean relatedBean =  relatedBeansIt.next();
                    TRevisionWorkitems related = TRevisionWorkitems.createTRevisionWorkitems(relatedBean, createdObjects);
                    result.addTRevisionWorkitemsFromBean(related);
                }
            }
        }




        {
            TRepositoryBean relatedBean = bean.getTRepositoryBean();
            if (relatedBean != null)
            {
                TRepository relatedObject = TRepository.createTRepository(relatedBean, createdObjects);
                result.setTRepository(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TRevisionWorkitems object to this object.
     * through the TRevisionWorkitems foreign key attribute
     *
     * @param toAdd TRevisionWorkitems
     */
    protected void addTRevisionWorkitemsFromBean(TRevisionWorkitems toAdd)
    {
        initTRevisionWorkitemss();
        collTRevisionWorkitemss.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TRevision:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("FileName = ")
           .append(getFileName())
           .append("\n");
        str.append("AuthorName = ")
           .append(getAuthorName())
           .append("\n");
        str.append("ChangeDescription = ")
           .append(getChangeDescription())
           .append("\n");
        str.append("RevisionDate = ")
           .append(getRevisionDate())
           .append("\n");
        str.append("RevisionNumber = ")
           .append(getRevisionNumber())
           .append("\n");
        str.append("RepositoryKey = ")
           .append(getRepositoryKey())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
