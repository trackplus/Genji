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
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TDashboardScreenBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TPersonBean;

import com.aurel.track.beans.TDashboardTabBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TDashboardScreen
 */
public abstract class BaseTDashboardScreen extends TpBaseObject
{
    /** The Peer class */
    private static final TDashboardScreenPeer peer =
        new TDashboardScreenPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the label field */
    private String label;

    /** The value for the person field */
    private Integer person;

    /** The value for the project field */
    private Integer project;

    /** The value for the entityType field */
    private Integer entityType;

    /** The value for the owner field */
    private Integer owner;

    /** The value for the description field */
    private String description;

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



        // update associated TDashboardTab
        if (collTDashboardTabs != null)
        {
            for (int i = 0; i < collTDashboardTabs.size(); i++)
            {
                ((TDashboardTab) collTDashboardTabs.get(i))
                        .setParent(v);
            }
        }
    }

    /**
     * Get the Name
     *
     * @return String
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set the value of Name
     *
     * @param v new value
     */
    public void setName(String v) 
    {

        if (!ObjectUtils.equals(this.name, v))
        {
            this.name = v;
            setModified(true);
        }


    }

    /**
     * Get the Label
     *
     * @return String
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set the value of Label
     *
     * @param v new value
     */
    public void setLabel(String v) 
    {

        if (!ObjectUtils.equals(this.label, v))
        {
            this.label = v;
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


        if (aTPersonRelatedByPerson != null && !ObjectUtils.equals(aTPersonRelatedByPerson.getObjectID(), v))
        {
            aTPersonRelatedByPerson = null;
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
     * Get the EntityType
     *
     * @return Integer
     */
    public Integer getEntityType()
    {
        return entityType;
    }


    /**
     * Set the value of EntityType
     *
     * @param v new value
     */
    public void setEntityType(Integer v) 
    {

        if (!ObjectUtils.equals(this.entityType, v))
        {
            this.entityType = v;
            setModified(true);
        }


    }

    /**
     * Get the Owner
     *
     * @return Integer
     */
    public Integer getOwner()
    {
        return owner;
    }


    /**
     * Set the value of Owner
     *
     * @param v new value
     */
    public void setOwner(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.owner, v))
        {
            this.owner = v;
            setModified(true);
        }


        if (aTPersonRelatedByOwner != null && !ObjectUtils.equals(aTPersonRelatedByOwner.getObjectID(), v))
        {
            aTPersonRelatedByOwner = null;
        }

    }

    /**
     * Get the Description
     *
     * @return String
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set the value of Description
     *
     * @param v new value
     */
    public void setDescription(String v) 
    {

        if (!ObjectUtils.equals(this.description, v))
        {
            this.description = v;
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




    private TPerson aTPersonRelatedByOwner;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByOwner(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setOwner((Integer) null);
        }
        else
        {
            setOwner(v.getObjectID());
        }
        aTPersonRelatedByOwner = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByOwner()
        throws TorqueException
    {
        if (aTPersonRelatedByOwner == null && (!ObjectUtils.equals(this.owner, null)))
        {
            aTPersonRelatedByOwner = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.owner));
        }
        return aTPersonRelatedByOwner;
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
    public TPerson getTPersonRelatedByOwner(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByOwner == null && (!ObjectUtils.equals(this.owner, null)))
        {
            aTPersonRelatedByOwner = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.owner), connection);
        }
        return aTPersonRelatedByOwner;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByOwnerKey(ObjectKey key) throws TorqueException
    {

        setOwner(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTDashboardTabs
     */
    protected List<TDashboardTab> collTDashboardTabs;

    /**
     * Temporary storage of collTDashboardTabs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTDashboardTabs()
    {
        if (collTDashboardTabs == null)
        {
            collTDashboardTabs = new ArrayList<TDashboardTab>();
        }
    }


    /**
     * Method called to associate a TDashboardTab object to this object
     * through the TDashboardTab foreign key attribute
     *
     * @param l TDashboardTab
     * @throws TorqueException
     */
    public void addTDashboardTab(TDashboardTab l) throws TorqueException
    {
        getTDashboardTabs().add(l);
        l.setTDashboardScreen((TDashboardScreen) this);
    }

    /**
     * Method called to associate a TDashboardTab object to this object
     * through the TDashboardTab foreign key attribute using connection.
     *
     * @param l TDashboardTab
     * @throws TorqueException
     */
    public void addTDashboardTab(TDashboardTab l, Connection con) throws TorqueException
    {
        getTDashboardTabs(con).add(l);
        l.setTDashboardScreen((TDashboardScreen) this);
    }

    /**
     * The criteria used to select the current contents of collTDashboardTabs
     */
    private Criteria lastTDashboardTabsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDashboardTabs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TDashboardTab> getTDashboardTabs()
        throws TorqueException
    {
        if (collTDashboardTabs == null)
        {
            collTDashboardTabs = getTDashboardTabs(new Criteria(10));
        }
        return collTDashboardTabs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardScreen has previously
     * been saved, it will retrieve related TDashboardTabs from storage.
     * If this TDashboardScreen is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TDashboardTab> getTDashboardTabs(Criteria criteria) throws TorqueException
    {
        if (collTDashboardTabs == null)
        {
            if (isNew())
            {
               collTDashboardTabs = new ArrayList<TDashboardTab>();
            }
            else
            {
                criteria.add(TDashboardTabPeer.PARENT, getObjectID() );
                collTDashboardTabs = TDashboardTabPeer.doSelect(criteria);
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
                criteria.add(TDashboardTabPeer.PARENT, getObjectID());
                if (!lastTDashboardTabsCriteria.equals(criteria))
                {
                    collTDashboardTabs = TDashboardTabPeer.doSelect(criteria);
                }
            }
        }
        lastTDashboardTabsCriteria = criteria;

        return collTDashboardTabs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDashboardTabs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDashboardTab> getTDashboardTabs(Connection con) throws TorqueException
    {
        if (collTDashboardTabs == null)
        {
            collTDashboardTabs = getTDashboardTabs(new Criteria(10), con);
        }
        return collTDashboardTabs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardScreen has previously
     * been saved, it will retrieve related TDashboardTabs from storage.
     * If this TDashboardScreen is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDashboardTab> getTDashboardTabs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTDashboardTabs == null)
        {
            if (isNew())
            {
               collTDashboardTabs = new ArrayList<TDashboardTab>();
            }
            else
            {
                 criteria.add(TDashboardTabPeer.PARENT, getObjectID());
                 collTDashboardTabs = TDashboardTabPeer.doSelect(criteria, con);
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
                 criteria.add(TDashboardTabPeer.PARENT, getObjectID());
                 if (!lastTDashboardTabsCriteria.equals(criteria))
                 {
                     collTDashboardTabs = TDashboardTabPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTDashboardTabsCriteria = criteria;

         return collTDashboardTabs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDashboardScreen is new, it will return
     * an empty collection; or if this TDashboardScreen has previously
     * been saved, it will retrieve related TDashboardTabs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDashboardScreen.
     */
    protected List<TDashboardTab> getTDashboardTabsJoinTDashboardScreen(Criteria criteria)
        throws TorqueException
    {
        if (collTDashboardTabs == null)
        {
            if (isNew())
            {
               collTDashboardTabs = new ArrayList<TDashboardTab>();
            }
            else
            {
                criteria.add(TDashboardTabPeer.PARENT, getObjectID());
                collTDashboardTabs = TDashboardTabPeer.doSelectJoinTDashboardScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDashboardTabPeer.PARENT, getObjectID());
            if (!lastTDashboardTabsCriteria.equals(criteria))
            {
                collTDashboardTabs = TDashboardTabPeer.doSelectJoinTDashboardScreen(criteria);
            }
        }
        lastTDashboardTabsCriteria = criteria;

        return collTDashboardTabs;
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
            fieldNames.add("Name");
            fieldNames.add("Label");
            fieldNames.add("Person");
            fieldNames.add("Project");
            fieldNames.add("EntityType");
            fieldNames.add("Owner");
            fieldNames.add("Description");
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
        if (name.equals("Name"))
        {
            return getName();
        }
        if (name.equals("Label"))
        {
            return getLabel();
        }
        if (name.equals("Person"))
        {
            return getPerson();
        }
        if (name.equals("Project"))
        {
            return getProject();
        }
        if (name.equals("EntityType"))
        {
            return getEntityType();
        }
        if (name.equals("Owner"))
        {
            return getOwner();
        }
        if (name.equals("Description"))
        {
            return getDescription();
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
        if (name.equals("Name"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setName((String) value);
            return true;
        }
        if (name.equals("Label"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLabel((String) value);
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
        if (name.equals("EntityType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEntityType((Integer) value);
            return true;
        }
        if (name.equals("Owner"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOwner((Integer) value);
            return true;
        }
        if (name.equals("Description"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDescription((String) value);
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
        if (name.equals(TDashboardScreenPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TDashboardScreenPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TDashboardScreenPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TDashboardScreenPeer.PERSONPKEY))
        {
            return getPerson();
        }
        if (name.equals(TDashboardScreenPeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TDashboardScreenPeer.ENTITYTYPE))
        {
            return getEntityType();
        }
        if (name.equals(TDashboardScreenPeer.OWNER))
        {
            return getOwner();
        }
        if (name.equals(TDashboardScreenPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TDashboardScreenPeer.TPUUID))
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
      if (TDashboardScreenPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TDashboardScreenPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TDashboardScreenPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TDashboardScreenPeer.PERSONPKEY.equals(name))
        {
            return setByName("Person", value);
        }
      if (TDashboardScreenPeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TDashboardScreenPeer.ENTITYTYPE.equals(name))
        {
            return setByName("EntityType", value);
        }
      if (TDashboardScreenPeer.OWNER.equals(name))
        {
            return setByName("Owner", value);
        }
      if (TDashboardScreenPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TDashboardScreenPeer.TPUUID.equals(name))
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
            return getName();
        }
        if (pos == 2)
        {
            return getLabel();
        }
        if (pos == 3)
        {
            return getPerson();
        }
        if (pos == 4)
        {
            return getProject();
        }
        if (pos == 5)
        {
            return getEntityType();
        }
        if (pos == 6)
        {
            return getOwner();
        }
        if (pos == 7)
        {
            return getDescription();
        }
        if (pos == 8)
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
            return setByName("Name", value);
        }
    if (position == 2)
        {
            return setByName("Label", value);
        }
    if (position == 3)
        {
            return setByName("Person", value);
        }
    if (position == 4)
        {
            return setByName("Project", value);
        }
    if (position == 5)
        {
            return setByName("EntityType", value);
        }
    if (position == 6)
        {
            return setByName("Owner", value);
        }
    if (position == 7)
        {
            return setByName("Description", value);
        }
    if (position == 8)
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
        save(TDashboardScreenPeer.DATABASE_NAME);
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
                    TDashboardScreenPeer.doInsert((TDashboardScreen) this, con);
                    setNew(false);
                }
                else
                {
                    TDashboardScreenPeer.doUpdate((TDashboardScreen) this, con);
                }
            }


            if (collTDashboardTabs != null)
            {
                for (int i = 0; i < collTDashboardTabs.size(); i++)
                {
                    ((TDashboardTab) collTDashboardTabs.get(i)).save(con);
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
    public TDashboardScreen copy() throws TorqueException
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
    public TDashboardScreen copy(Connection con) throws TorqueException
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
    public TDashboardScreen copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TDashboardScreen(), deepcopy);
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
    public TDashboardScreen copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TDashboardScreen(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TDashboardScreen copyInto(TDashboardScreen copyObj) throws TorqueException
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
    protected TDashboardScreen copyInto(TDashboardScreen copyObj, Connection con) throws TorqueException
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
    protected TDashboardScreen copyInto(TDashboardScreen copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setLabel(label);
        copyObj.setPerson(person);
        copyObj.setProject(project);
        copyObj.setEntityType(entityType);
        copyObj.setOwner(owner);
        copyObj.setDescription(description);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TDashboardTab> vTDashboardTabs = getTDashboardTabs();
        if (vTDashboardTabs != null)
        {
            for (int i = 0; i < vTDashboardTabs.size(); i++)
            {
                TDashboardTab obj =  vTDashboardTabs.get(i);
                copyObj.addTDashboardTab(obj.copy());
            }
        }
        else
        {
            copyObj.collTDashboardTabs = null;
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
    protected TDashboardScreen copyInto(TDashboardScreen copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setLabel(label);
        copyObj.setPerson(person);
        copyObj.setProject(project);
        copyObj.setEntityType(entityType);
        copyObj.setOwner(owner);
        copyObj.setDescription(description);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TDashboardTab> vTDashboardTabs = getTDashboardTabs(con);
        if (vTDashboardTabs != null)
        {
            for (int i = 0; i < vTDashboardTabs.size(); i++)
            {
                TDashboardTab obj =  vTDashboardTabs.get(i);
                copyObj.addTDashboardTab(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTDashboardTabs = null;
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
    public TDashboardScreenPeer getPeer()
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
        return TDashboardScreenPeer.getTableMap();
    }

  
    /**
     * Creates a TDashboardScreenBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TDashboardScreenBean with the contents of this object
     */
    public TDashboardScreenBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TDashboardScreenBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TDashboardScreenBean with the contents of this object
     */
    public TDashboardScreenBean getBean(IdentityMap createdBeans)
    {
        TDashboardScreenBean result = (TDashboardScreenBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TDashboardScreenBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setLabel(getLabel());
        result.setPerson(getPerson());
        result.setProject(getProject());
        result.setEntityType(getEntityType());
        result.setOwner(getOwner());
        result.setDescription(getDescription());
        result.setUuid(getUuid());



        if (collTDashboardTabs != null)
        {
            List<TDashboardTabBean> relatedBeans = new ArrayList<TDashboardTabBean>(collTDashboardTabs.size());
            for (Iterator<TDashboardTab> collTDashboardTabsIt = collTDashboardTabs.iterator(); collTDashboardTabsIt.hasNext(); )
            {
                TDashboardTab related = (TDashboardTab) collTDashboardTabsIt.next();
                TDashboardTabBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTDashboardTabBeans(relatedBeans);
        }




        if (aTPersonRelatedByPerson != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByPerson.getBean(createdBeans);
            result.setTPersonBeanRelatedByPerson(relatedBean);
        }



        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }



        if (aTPersonRelatedByOwner != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByOwner.getBean(createdBeans);
            result.setTPersonBeanRelatedByOwner(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TDashboardScreen with the contents
     * of a TDashboardScreenBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TDashboardScreenBean which contents are used to create
     *        the resulting class
     * @return an instance of TDashboardScreen with the contents of bean
     */
    public static TDashboardScreen createTDashboardScreen(TDashboardScreenBean bean)
        throws TorqueException
    {
        return createTDashboardScreen(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TDashboardScreen with the contents
     * of a TDashboardScreenBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TDashboardScreenBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TDashboardScreen with the contents of bean
     */

    public static TDashboardScreen createTDashboardScreen(TDashboardScreenBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TDashboardScreen result = (TDashboardScreen) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TDashboardScreen();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setLabel(bean.getLabel());
        result.setPerson(bean.getPerson());
        result.setProject(bean.getProject());
        result.setEntityType(bean.getEntityType());
        result.setOwner(bean.getOwner());
        result.setDescription(bean.getDescription());
        result.setUuid(bean.getUuid());



        {
            List<TDashboardTabBean> relatedBeans = bean.getTDashboardTabBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TDashboardTabBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TDashboardTabBean relatedBean =  relatedBeansIt.next();
                    TDashboardTab related = TDashboardTab.createTDashboardTab(relatedBean, createdObjects);
                    result.addTDashboardTabFromBean(related);
                }
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



        {
            TProjectBean relatedBean = bean.getTProjectBean();
            if (relatedBean != null)
            {
                TProject relatedObject = TProject.createTProject(relatedBean, createdObjects);
                result.setTProject(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByOwner();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByOwner(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TDashboardTab object to this object.
     * through the TDashboardTab foreign key attribute
     *
     * @param toAdd TDashboardTab
     */
    protected void addTDashboardTabFromBean(TDashboardTab toAdd)
    {
        initTDashboardTabs();
        collTDashboardTabs.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TDashboardScreen:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("Project = ")
           .append(getProject())
           .append("\n");
        str.append("EntityType = ")
           .append(getEntityType())
           .append("\n");
        str.append("Owner = ")
           .append(getOwner())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
