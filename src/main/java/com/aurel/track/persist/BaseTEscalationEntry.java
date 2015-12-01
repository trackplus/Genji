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



import com.aurel.track.persist.TSLA;
import com.aurel.track.persist.TSLAPeer;
import com.aurel.track.persist.TPriority;
import com.aurel.track.persist.TPriorityPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TEscalationEntryBean;
import com.aurel.track.beans.TSLABean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TPersonBean;

import com.aurel.track.beans.TEscalationStateBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TEscalationEntry
 */
public abstract class BaseTEscalationEntry extends TpBaseObject
{
    /** The Peer class */
    private static final TEscalationEntryPeer peer =
        new TEscalationEntryPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the sla field */
    private Integer sla;

    /** The value for the priority field */
    private Integer priority;

    /** The value for the escalateTo field */
    private Integer escalateTo;

    /** The value for the sparameters field */
    private String sparameters;

    /** The value for the nlevel field */
    private Integer nlevel;

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



        // update associated TEscalationState
        if (collTEscalationStates != null)
        {
            for (int i = 0; i < collTEscalationStates.size(); i++)
            {
                ((TEscalationState) collTEscalationStates.get(i))
                        .setEscalationentry(v);
            }
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
     * Get the Priority
     *
     * @return Integer
     */
    public Integer getPriority()
    {
        return priority;
    }


    /**
     * Set the value of Priority
     *
     * @param v new value
     */
    public void setPriority(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.priority, v))
        {
            this.priority = v;
            setModified(true);
        }


        if (aTPriority != null && !ObjectUtils.equals(aTPriority.getObjectID(), v))
        {
            aTPriority = null;
        }

    }

    /**
     * Get the EscalateTo
     *
     * @return Integer
     */
    public Integer getEscalateTo()
    {
        return escalateTo;
    }


    /**
     * Set the value of EscalateTo
     *
     * @param v new value
     */
    public void setEscalateTo(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.escalateTo, v))
        {
            this.escalateTo = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
        }

    }

    /**
     * Get the Sparameters
     *
     * @return String
     */
    public String getSparameters()
    {
        return sparameters;
    }


    /**
     * Set the value of Sparameters
     *
     * @param v new value
     */
    public void setSparameters(String v) 
    {

        if (!ObjectUtils.equals(this.sparameters, v))
        {
            this.sparameters = v;
            setModified(true);
        }


    }

    /**
     * Get the Nlevel
     *
     * @return Integer
     */
    public Integer getNlevel()
    {
        return nlevel;
    }


    /**
     * Set the value of Nlevel
     *
     * @param v new value
     */
    public void setNlevel(Integer v) 
    {

        if (!ObjectUtils.equals(this.nlevel, v))
        {
            this.nlevel = v;
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




    private TPriority aTPriority;

    /**
     * Declares an association between this object and a TPriority object
     *
     * @param v TPriority
     * @throws TorqueException
     */
    public void setTPriority(TPriority v) throws TorqueException
    {
        if (v == null)
        {
            setPriority((Integer) null);
        }
        else
        {
            setPriority(v.getObjectID());
        }
        aTPriority = v;
    }


    /**
     * Returns the associated TPriority object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPriority object
     * @throws TorqueException
     */
    public TPriority getTPriority()
        throws TorqueException
    {
        if (aTPriority == null && (!ObjectUtils.equals(this.priority, null)))
        {
            aTPriority = TPriorityPeer.retrieveByPK(SimpleKey.keyFor(this.priority));
        }
        return aTPriority;
    }

    /**
     * Return the associated TPriority object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPriority object
     * @throws TorqueException
     */
    public TPriority getTPriority(Connection connection)
        throws TorqueException
    {
        if (aTPriority == null && (!ObjectUtils.equals(this.priority, null)))
        {
            aTPriority = TPriorityPeer.retrieveByPK(SimpleKey.keyFor(this.priority), connection);
        }
        return aTPriority;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPriorityKey(ObjectKey key) throws TorqueException
    {

        setPriority(new Integer(((NumberKey) key).intValue()));
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
            setEscalateTo((Integer) null);
        }
        else
        {
            setEscalateTo(v.getObjectID());
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
        if (aTPerson == null && (!ObjectUtils.equals(this.escalateTo, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.escalateTo));
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
        if (aTPerson == null && (!ObjectUtils.equals(this.escalateTo, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.escalateTo), connection);
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

        setEscalateTo(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTEscalationStates
     */
    protected List<TEscalationState> collTEscalationStates;

    /**
     * Temporary storage of collTEscalationStates to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTEscalationStates()
    {
        if (collTEscalationStates == null)
        {
            collTEscalationStates = new ArrayList<TEscalationState>();
        }
    }


    /**
     * Method called to associate a TEscalationState object to this object
     * through the TEscalationState foreign key attribute
     *
     * @param l TEscalationState
     * @throws TorqueException
     */
    public void addTEscalationState(TEscalationState l) throws TorqueException
    {
        getTEscalationStates().add(l);
        l.setTEscalationEntry((TEscalationEntry) this);
    }

    /**
     * Method called to associate a TEscalationState object to this object
     * through the TEscalationState foreign key attribute using connection.
     *
     * @param l TEscalationState
     * @throws TorqueException
     */
    public void addTEscalationState(TEscalationState l, Connection con) throws TorqueException
    {
        getTEscalationStates(con).add(l);
        l.setTEscalationEntry((TEscalationEntry) this);
    }

    /**
     * The criteria used to select the current contents of collTEscalationStates
     */
    private Criteria lastTEscalationStatesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEscalationStates(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TEscalationState> getTEscalationStates()
        throws TorqueException
    {
        if (collTEscalationStates == null)
        {
            collTEscalationStates = getTEscalationStates(new Criteria(10));
        }
        return collTEscalationStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEscalationEntry has previously
     * been saved, it will retrieve related TEscalationStates from storage.
     * If this TEscalationEntry is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TEscalationState> getTEscalationStates(Criteria criteria) throws TorqueException
    {
        if (collTEscalationStates == null)
        {
            if (isNew())
            {
               collTEscalationStates = new ArrayList<TEscalationState>();
            }
            else
            {
                criteria.add(TEscalationStatePeer.ESCALATIONENTRY, getObjectID() );
                collTEscalationStates = TEscalationStatePeer.doSelect(criteria);
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
                criteria.add(TEscalationStatePeer.ESCALATIONENTRY, getObjectID());
                if (!lastTEscalationStatesCriteria.equals(criteria))
                {
                    collTEscalationStates = TEscalationStatePeer.doSelect(criteria);
                }
            }
        }
        lastTEscalationStatesCriteria = criteria;

        return collTEscalationStates;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEscalationStates(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEscalationState> getTEscalationStates(Connection con) throws TorqueException
    {
        if (collTEscalationStates == null)
        {
            collTEscalationStates = getTEscalationStates(new Criteria(10), con);
        }
        return collTEscalationStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEscalationEntry has previously
     * been saved, it will retrieve related TEscalationStates from storage.
     * If this TEscalationEntry is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEscalationState> getTEscalationStates(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTEscalationStates == null)
        {
            if (isNew())
            {
               collTEscalationStates = new ArrayList<TEscalationState>();
            }
            else
            {
                 criteria.add(TEscalationStatePeer.ESCALATIONENTRY, getObjectID());
                 collTEscalationStates = TEscalationStatePeer.doSelect(criteria, con);
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
                 criteria.add(TEscalationStatePeer.ESCALATIONENTRY, getObjectID());
                 if (!lastTEscalationStatesCriteria.equals(criteria))
                 {
                     collTEscalationStates = TEscalationStatePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTEscalationStatesCriteria = criteria;

         return collTEscalationStates;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEscalationEntry is new, it will return
     * an empty collection; or if this TEscalationEntry has previously
     * been saved, it will retrieve related TEscalationStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TEscalationEntry.
     */
    protected List<TEscalationState> getTEscalationStatesJoinTEscalationEntry(Criteria criteria)
        throws TorqueException
    {
        if (collTEscalationStates == null)
        {
            if (isNew())
            {
               collTEscalationStates = new ArrayList<TEscalationState>();
            }
            else
            {
                criteria.add(TEscalationStatePeer.ESCALATIONENTRY, getObjectID());
                collTEscalationStates = TEscalationStatePeer.doSelectJoinTEscalationEntry(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEscalationStatePeer.ESCALATIONENTRY, getObjectID());
            if (!lastTEscalationStatesCriteria.equals(criteria))
            {
                collTEscalationStates = TEscalationStatePeer.doSelectJoinTEscalationEntry(criteria);
            }
        }
        lastTEscalationStatesCriteria = criteria;

        return collTEscalationStates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TEscalationEntry is new, it will return
     * an empty collection; or if this TEscalationEntry has previously
     * been saved, it will retrieve related TEscalationStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TEscalationEntry.
     */
    protected List<TEscalationState> getTEscalationStatesJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTEscalationStates == null)
        {
            if (isNew())
            {
               collTEscalationStates = new ArrayList<TEscalationState>();
            }
            else
            {
                criteria.add(TEscalationStatePeer.ESCALATIONENTRY, getObjectID());
                collTEscalationStates = TEscalationStatePeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEscalationStatePeer.ESCALATIONENTRY, getObjectID());
            if (!lastTEscalationStatesCriteria.equals(criteria))
            {
                collTEscalationStates = TEscalationStatePeer.doSelectJoinTState(criteria);
            }
        }
        lastTEscalationStatesCriteria = criteria;

        return collTEscalationStates;
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
            fieldNames.add("Sla");
            fieldNames.add("Priority");
            fieldNames.add("EscalateTo");
            fieldNames.add("Sparameters");
            fieldNames.add("Nlevel");
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
        if (name.equals("Sla"))
        {
            return getSla();
        }
        if (name.equals("Priority"))
        {
            return getPriority();
        }
        if (name.equals("EscalateTo"))
        {
            return getEscalateTo();
        }
        if (name.equals("Sparameters"))
        {
            return getSparameters();
        }
        if (name.equals("Nlevel"))
        {
            return getNlevel();
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
        if (name.equals("Priority"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPriority((Integer) value);
            return true;
        }
        if (name.equals("EscalateTo"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEscalateTo((Integer) value);
            return true;
        }
        if (name.equals("Sparameters"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSparameters((String) value);
            return true;
        }
        if (name.equals("Nlevel"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNlevel((Integer) value);
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
        if (name.equals(TEscalationEntryPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TEscalationEntryPeer.SLA))
        {
            return getSla();
        }
        if (name.equals(TEscalationEntryPeer.PRIORITY))
        {
            return getPriority();
        }
        if (name.equals(TEscalationEntryPeer.ESCALATETO))
        {
            return getEscalateTo();
        }
        if (name.equals(TEscalationEntryPeer.SPARAMETERS))
        {
            return getSparameters();
        }
        if (name.equals(TEscalationEntryPeer.NLEVEL))
        {
            return getNlevel();
        }
        if (name.equals(TEscalationEntryPeer.TPUUID))
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
      if (TEscalationEntryPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TEscalationEntryPeer.SLA.equals(name))
        {
            return setByName("Sla", value);
        }
      if (TEscalationEntryPeer.PRIORITY.equals(name))
        {
            return setByName("Priority", value);
        }
      if (TEscalationEntryPeer.ESCALATETO.equals(name))
        {
            return setByName("EscalateTo", value);
        }
      if (TEscalationEntryPeer.SPARAMETERS.equals(name))
        {
            return setByName("Sparameters", value);
        }
      if (TEscalationEntryPeer.NLEVEL.equals(name))
        {
            return setByName("Nlevel", value);
        }
      if (TEscalationEntryPeer.TPUUID.equals(name))
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
            return getSla();
        }
        if (pos == 2)
        {
            return getPriority();
        }
        if (pos == 3)
        {
            return getEscalateTo();
        }
        if (pos == 4)
        {
            return getSparameters();
        }
        if (pos == 5)
        {
            return getNlevel();
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
            return setByName("Sla", value);
        }
    if (position == 2)
        {
            return setByName("Priority", value);
        }
    if (position == 3)
        {
            return setByName("EscalateTo", value);
        }
    if (position == 4)
        {
            return setByName("Sparameters", value);
        }
    if (position == 5)
        {
            return setByName("Nlevel", value);
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
        save(TEscalationEntryPeer.DATABASE_NAME);
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
                    TEscalationEntryPeer.doInsert((TEscalationEntry) this, con);
                    setNew(false);
                }
                else
                {
                    TEscalationEntryPeer.doUpdate((TEscalationEntry) this, con);
                }
            }


            if (collTEscalationStates != null)
            {
                for (int i = 0; i < collTEscalationStates.size(); i++)
                {
                    ((TEscalationState) collTEscalationStates.get(i)).save(con);
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
    public TEscalationEntry copy() throws TorqueException
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
    public TEscalationEntry copy(Connection con) throws TorqueException
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
    public TEscalationEntry copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TEscalationEntry(), deepcopy);
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
    public TEscalationEntry copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TEscalationEntry(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TEscalationEntry copyInto(TEscalationEntry copyObj) throws TorqueException
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
    protected TEscalationEntry copyInto(TEscalationEntry copyObj, Connection con) throws TorqueException
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
    protected TEscalationEntry copyInto(TEscalationEntry copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setSla(sla);
        copyObj.setPriority(priority);
        copyObj.setEscalateTo(escalateTo);
        copyObj.setSparameters(sparameters);
        copyObj.setNlevel(nlevel);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TEscalationState> vTEscalationStates = getTEscalationStates();
        if (vTEscalationStates != null)
        {
            for (int i = 0; i < vTEscalationStates.size(); i++)
            {
                TEscalationState obj =  vTEscalationStates.get(i);
                copyObj.addTEscalationState(obj.copy());
            }
        }
        else
        {
            copyObj.collTEscalationStates = null;
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
    protected TEscalationEntry copyInto(TEscalationEntry copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setSla(sla);
        copyObj.setPriority(priority);
        copyObj.setEscalateTo(escalateTo);
        copyObj.setSparameters(sparameters);
        copyObj.setNlevel(nlevel);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TEscalationState> vTEscalationStates = getTEscalationStates(con);
        if (vTEscalationStates != null)
        {
            for (int i = 0; i < vTEscalationStates.size(); i++)
            {
                TEscalationState obj =  vTEscalationStates.get(i);
                copyObj.addTEscalationState(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTEscalationStates = null;
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
    public TEscalationEntryPeer getPeer()
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
        return TEscalationEntryPeer.getTableMap();
    }

  
    /**
     * Creates a TEscalationEntryBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TEscalationEntryBean with the contents of this object
     */
    public TEscalationEntryBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TEscalationEntryBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TEscalationEntryBean with the contents of this object
     */
    public TEscalationEntryBean getBean(IdentityMap createdBeans)
    {
        TEscalationEntryBean result = (TEscalationEntryBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TEscalationEntryBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setSla(getSla());
        result.setPriority(getPriority());
        result.setEscalateTo(getEscalateTo());
        result.setSparameters(getSparameters());
        result.setNlevel(getNlevel());
        result.setUuid(getUuid());



        if (collTEscalationStates != null)
        {
            List<TEscalationStateBean> relatedBeans = new ArrayList<TEscalationStateBean>(collTEscalationStates.size());
            for (Iterator<TEscalationState> collTEscalationStatesIt = collTEscalationStates.iterator(); collTEscalationStatesIt.hasNext(); )
            {
                TEscalationState related = (TEscalationState) collTEscalationStatesIt.next();
                TEscalationStateBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTEscalationStateBeans(relatedBeans);
        }




        if (aTSLA != null)
        {
            TSLABean relatedBean = aTSLA.getBean(createdBeans);
            result.setTSLABean(relatedBean);
        }



        if (aTPriority != null)
        {
            TPriorityBean relatedBean = aTPriority.getBean(createdBeans);
            result.setTPriorityBean(relatedBean);
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
     * Creates an instance of TEscalationEntry with the contents
     * of a TEscalationEntryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TEscalationEntryBean which contents are used to create
     *        the resulting class
     * @return an instance of TEscalationEntry with the contents of bean
     */
    public static TEscalationEntry createTEscalationEntry(TEscalationEntryBean bean)
        throws TorqueException
    {
        return createTEscalationEntry(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TEscalationEntry with the contents
     * of a TEscalationEntryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TEscalationEntryBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TEscalationEntry with the contents of bean
     */

    public static TEscalationEntry createTEscalationEntry(TEscalationEntryBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TEscalationEntry result = (TEscalationEntry) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TEscalationEntry();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setSla(bean.getSla());
        result.setPriority(bean.getPriority());
        result.setEscalateTo(bean.getEscalateTo());
        result.setSparameters(bean.getSparameters());
        result.setNlevel(bean.getNlevel());
        result.setUuid(bean.getUuid());



        {
            List<TEscalationStateBean> relatedBeans = bean.getTEscalationStateBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TEscalationStateBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TEscalationStateBean relatedBean =  relatedBeansIt.next();
                    TEscalationState related = TEscalationState.createTEscalationState(relatedBean, createdObjects);
                    result.addTEscalationStateFromBean(related);
                }
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



        {
            TPriorityBean relatedBean = bean.getTPriorityBean();
            if (relatedBean != null)
            {
                TPriority relatedObject = TPriority.createTPriority(relatedBean, createdObjects);
                result.setTPriority(relatedObject);
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



    /**
     * Method called to associate a TEscalationState object to this object.
     * through the TEscalationState foreign key attribute
     *
     * @param toAdd TEscalationState
     */
    protected void addTEscalationStateFromBean(TEscalationState toAdd)
    {
        initTEscalationStates();
        collTEscalationStates.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TEscalationEntry:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Sla = ")
           .append(getSla())
           .append("\n");
        str.append("Priority = ")
           .append(getPriority())
           .append("\n");
        str.append("EscalateTo = ")
           .append(getEscalateTo())
           .append("\n");
        str.append("Sparameters = ")
           .append(getSparameters())
           .append("\n");
        str.append("Nlevel = ")
           .append(getNlevel())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
