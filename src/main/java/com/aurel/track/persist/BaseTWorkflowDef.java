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

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TWorkflowDefBean;
import com.aurel.track.beans.TPersonBean;

import com.aurel.track.beans.TWorkflowTransitionBean;
import com.aurel.track.beans.TWorkflowStationBean;
import com.aurel.track.beans.TWorkflowConnectBean;
import com.aurel.track.beans.TWorkflowCommentBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TWorkflowDef
 */
public abstract class BaseTWorkflowDef extends TpBaseObject
{
    /** The Peer class */
    private static final TWorkflowDefPeer peer =
        new TWorkflowDefPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the description field */
    private String description;

    /** The value for the tagLabel field */
    private String tagLabel;

    /** The value for the owner field */
    private Integer owner;

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



        // update associated TWorkflowTransition
        if (collTWorkflowTransitions != null)
        {
            for (int i = 0; i < collTWorkflowTransitions.size(); i++)
            {
                ((TWorkflowTransition) collTWorkflowTransitions.get(i))
                        .setWorkflow(v);
            }
        }

        // update associated TWorkflowStation
        if (collTWorkflowStations != null)
        {
            for (int i = 0; i < collTWorkflowStations.size(); i++)
            {
                ((TWorkflowStation) collTWorkflowStations.get(i))
                        .setWorkflow(v);
            }
        }

        // update associated TWorkflowConnect
        if (collTWorkflowConnects != null)
        {
            for (int i = 0; i < collTWorkflowConnects.size(); i++)
            {
                ((TWorkflowConnect) collTWorkflowConnects.get(i))
                        .setWorkflow(v);
            }
        }

        // update associated TWorkflowComment
        if (collTWorkflowComments != null)
        {
            for (int i = 0; i < collTWorkflowComments.size(); i++)
            {
                ((TWorkflowComment) collTWorkflowComments.get(i))
                        .setWorkflow(v);
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
     * Get the TagLabel
     *
     * @return String
     */
    public String getTagLabel()
    {
        return tagLabel;
    }


    /**
     * Set the value of TagLabel
     *
     * @param v new value
     */
    public void setTagLabel(String v) 
    {

        if (!ObjectUtils.equals(this.tagLabel, v))
        {
            this.tagLabel = v;
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
            setOwner((Integer) null);
        }
        else
        {
            setOwner(v.getObjectID());
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
        if (aTPerson == null && (!ObjectUtils.equals(this.owner, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.owner));
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
        if (aTPerson == null && (!ObjectUtils.equals(this.owner, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.owner), connection);
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

        setOwner(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTWorkflowTransitions
     */
    protected List<TWorkflowTransition> collTWorkflowTransitions;

    /**
     * Temporary storage of collTWorkflowTransitions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowTransitions()
    {
        if (collTWorkflowTransitions == null)
        {
            collTWorkflowTransitions = new ArrayList<TWorkflowTransition>();
        }
    }


    /**
     * Method called to associate a TWorkflowTransition object to this object
     * through the TWorkflowTransition foreign key attribute
     *
     * @param l TWorkflowTransition
     * @throws TorqueException
     */
    public void addTWorkflowTransition(TWorkflowTransition l) throws TorqueException
    {
        getTWorkflowTransitions().add(l);
        l.setTWorkflowDef((TWorkflowDef) this);
    }

    /**
     * Method called to associate a TWorkflowTransition object to this object
     * through the TWorkflowTransition foreign key attribute using connection.
     *
     * @param l TWorkflowTransition
     * @throws TorqueException
     */
    public void addTWorkflowTransition(TWorkflowTransition l, Connection con) throws TorqueException
    {
        getTWorkflowTransitions(con).add(l);
        l.setTWorkflowDef((TWorkflowDef) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowTransitions
     */
    private Criteria lastTWorkflowTransitionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowTransitions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowTransition> getTWorkflowTransitions()
        throws TorqueException
    {
        if (collTWorkflowTransitions == null)
        {
            collTWorkflowTransitions = getTWorkflowTransitions(new Criteria(10));
        }
        return collTWorkflowTransitions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     * If this TWorkflowDef is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowTransition> getTWorkflowTransitions(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowTransitions == null)
        {
            if (isNew())
            {
               collTWorkflowTransitions = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID() );
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelect(criteria);
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
                criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID());
                if (!lastTWorkflowTransitionsCriteria.equals(criteria))
                {
                    collTWorkflowTransitions = TWorkflowTransitionPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowTransitionsCriteria = criteria;

        return collTWorkflowTransitions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowTransitions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowTransition> getTWorkflowTransitions(Connection con) throws TorqueException
    {
        if (collTWorkflowTransitions == null)
        {
            collTWorkflowTransitions = getTWorkflowTransitions(new Criteria(10), con);
        }
        return collTWorkflowTransitions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     * If this TWorkflowDef is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowTransition> getTWorkflowTransitions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowTransitions == null)
        {
            if (isNew())
            {
               collTWorkflowTransitions = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                 criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID());
                 collTWorkflowTransitions = TWorkflowTransitionPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID());
                 if (!lastTWorkflowTransitionsCriteria.equals(criteria))
                 {
                     collTWorkflowTransitions = TWorkflowTransitionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowTransitionsCriteria = criteria;

         return collTWorkflowTransitions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef is new, it will return
     * an empty collection; or if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowDef.
     */
    protected List<TWorkflowTransition> getTWorkflowTransitionsJoinTWorkflowStationRelatedByStationFrom(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowTransitions == null)
        {
            if (isNew())
            {
               collTWorkflowTransitions = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID());
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTWorkflowStationRelatedByStationFrom(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID());
            if (!lastTWorkflowTransitionsCriteria.equals(criteria))
            {
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTWorkflowStationRelatedByStationFrom(criteria);
            }
        }
        lastTWorkflowTransitionsCriteria = criteria;

        return collTWorkflowTransitions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef is new, it will return
     * an empty collection; or if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowDef.
     */
    protected List<TWorkflowTransition> getTWorkflowTransitionsJoinTWorkflowStationRelatedByStationTo(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowTransitions == null)
        {
            if (isNew())
            {
               collTWorkflowTransitions = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID());
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTWorkflowStationRelatedByStationTo(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID());
            if (!lastTWorkflowTransitionsCriteria.equals(criteria))
            {
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTWorkflowStationRelatedByStationTo(criteria);
            }
        }
        lastTWorkflowTransitionsCriteria = criteria;

        return collTWorkflowTransitions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef is new, it will return
     * an empty collection; or if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowDef.
     */
    protected List<TWorkflowTransition> getTWorkflowTransitionsJoinTAction(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowTransitions == null)
        {
            if (isNew())
            {
               collTWorkflowTransitions = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID());
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTAction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID());
            if (!lastTWorkflowTransitionsCriteria.equals(criteria))
            {
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTAction(criteria);
            }
        }
        lastTWorkflowTransitionsCriteria = criteria;

        return collTWorkflowTransitions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef is new, it will return
     * an empty collection; or if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowDef.
     */
    protected List<TWorkflowTransition> getTWorkflowTransitionsJoinTWorkflowDef(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowTransitions == null)
        {
            if (isNew())
            {
               collTWorkflowTransitions = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID());
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.WORKFLOW, getObjectID());
            if (!lastTWorkflowTransitionsCriteria.equals(criteria))
            {
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        lastTWorkflowTransitionsCriteria = criteria;

        return collTWorkflowTransitions;
    }





    /**
     * Collection to store aggregation of collTWorkflowStations
     */
    protected List<TWorkflowStation> collTWorkflowStations;

    /**
     * Temporary storage of collTWorkflowStations to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowStations()
    {
        if (collTWorkflowStations == null)
        {
            collTWorkflowStations = new ArrayList<TWorkflowStation>();
        }
    }


    /**
     * Method called to associate a TWorkflowStation object to this object
     * through the TWorkflowStation foreign key attribute
     *
     * @param l TWorkflowStation
     * @throws TorqueException
     */
    public void addTWorkflowStation(TWorkflowStation l) throws TorqueException
    {
        getTWorkflowStations().add(l);
        l.setTWorkflowDef((TWorkflowDef) this);
    }

    /**
     * Method called to associate a TWorkflowStation object to this object
     * through the TWorkflowStation foreign key attribute using connection.
     *
     * @param l TWorkflowStation
     * @throws TorqueException
     */
    public void addTWorkflowStation(TWorkflowStation l, Connection con) throws TorqueException
    {
        getTWorkflowStations(con).add(l);
        l.setTWorkflowDef((TWorkflowDef) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowStations
     */
    private Criteria lastTWorkflowStationsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowStations(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowStation> getTWorkflowStations()
        throws TorqueException
    {
        if (collTWorkflowStations == null)
        {
            collTWorkflowStations = getTWorkflowStations(new Criteria(10));
        }
        return collTWorkflowStations;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowStations from storage.
     * If this TWorkflowDef is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowStation> getTWorkflowStations(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowStations == null)
        {
            if (isNew())
            {
               collTWorkflowStations = new ArrayList<TWorkflowStation>();
            }
            else
            {
                criteria.add(TWorkflowStationPeer.WORKFLOW, getObjectID() );
                collTWorkflowStations = TWorkflowStationPeer.doSelect(criteria);
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
                criteria.add(TWorkflowStationPeer.WORKFLOW, getObjectID());
                if (!lastTWorkflowStationsCriteria.equals(criteria))
                {
                    collTWorkflowStations = TWorkflowStationPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowStationsCriteria = criteria;

        return collTWorkflowStations;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowStations(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowStation> getTWorkflowStations(Connection con) throws TorqueException
    {
        if (collTWorkflowStations == null)
        {
            collTWorkflowStations = getTWorkflowStations(new Criteria(10), con);
        }
        return collTWorkflowStations;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowStations from storage.
     * If this TWorkflowDef is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowStation> getTWorkflowStations(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowStations == null)
        {
            if (isNew())
            {
               collTWorkflowStations = new ArrayList<TWorkflowStation>();
            }
            else
            {
                 criteria.add(TWorkflowStationPeer.WORKFLOW, getObjectID());
                 collTWorkflowStations = TWorkflowStationPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowStationPeer.WORKFLOW, getObjectID());
                 if (!lastTWorkflowStationsCriteria.equals(criteria))
                 {
                     collTWorkflowStations = TWorkflowStationPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowStationsCriteria = criteria;

         return collTWorkflowStations;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef is new, it will return
     * an empty collection; or if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowStations from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowDef.
     */
    protected List<TWorkflowStation> getTWorkflowStationsJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowStations == null)
        {
            if (isNew())
            {
               collTWorkflowStations = new ArrayList<TWorkflowStation>();
            }
            else
            {
                criteria.add(TWorkflowStationPeer.WORKFLOW, getObjectID());
                collTWorkflowStations = TWorkflowStationPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowStationPeer.WORKFLOW, getObjectID());
            if (!lastTWorkflowStationsCriteria.equals(criteria))
            {
                collTWorkflowStations = TWorkflowStationPeer.doSelectJoinTState(criteria);
            }
        }
        lastTWorkflowStationsCriteria = criteria;

        return collTWorkflowStations;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef is new, it will return
     * an empty collection; or if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowStations from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowDef.
     */
    protected List<TWorkflowStation> getTWorkflowStationsJoinTWorkflowDef(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowStations == null)
        {
            if (isNew())
            {
               collTWorkflowStations = new ArrayList<TWorkflowStation>();
            }
            else
            {
                criteria.add(TWorkflowStationPeer.WORKFLOW, getObjectID());
                collTWorkflowStations = TWorkflowStationPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowStationPeer.WORKFLOW, getObjectID());
            if (!lastTWorkflowStationsCriteria.equals(criteria))
            {
                collTWorkflowStations = TWorkflowStationPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        lastTWorkflowStationsCriteria = criteria;

        return collTWorkflowStations;
    }





    /**
     * Collection to store aggregation of collTWorkflowConnects
     */
    protected List<TWorkflowConnect> collTWorkflowConnects;

    /**
     * Temporary storage of collTWorkflowConnects to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowConnects()
    {
        if (collTWorkflowConnects == null)
        {
            collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
        }
    }


    /**
     * Method called to associate a TWorkflowConnect object to this object
     * through the TWorkflowConnect foreign key attribute
     *
     * @param l TWorkflowConnect
     * @throws TorqueException
     */
    public void addTWorkflowConnect(TWorkflowConnect l) throws TorqueException
    {
        getTWorkflowConnects().add(l);
        l.setTWorkflowDef((TWorkflowDef) this);
    }

    /**
     * Method called to associate a TWorkflowConnect object to this object
     * through the TWorkflowConnect foreign key attribute using connection.
     *
     * @param l TWorkflowConnect
     * @throws TorqueException
     */
    public void addTWorkflowConnect(TWorkflowConnect l, Connection con) throws TorqueException
    {
        getTWorkflowConnects(con).add(l);
        l.setTWorkflowDef((TWorkflowDef) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowConnects
     */
    private Criteria lastTWorkflowConnectsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowConnects(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowConnect> getTWorkflowConnects()
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            collTWorkflowConnects = getTWorkflowConnects(new Criteria(10));
        }
        return collTWorkflowConnects;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     * If this TWorkflowDef is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowConnect> getTWorkflowConnects(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID() );
                collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria);
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
                criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID());
                if (!lastTWorkflowConnectsCriteria.equals(criteria))
                {
                    collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowConnects(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowConnect> getTWorkflowConnects(Connection con) throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            collTWorkflowConnects = getTWorkflowConnects(new Criteria(10), con);
        }
        return collTWorkflowConnects;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     * If this TWorkflowDef is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowConnect> getTWorkflowConnects(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                 criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID());
                 collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID());
                 if (!lastTWorkflowConnectsCriteria.equals(criteria))
                 {
                     collTWorkflowConnects = TWorkflowConnectPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowConnectsCriteria = criteria;

         return collTWorkflowConnects;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef is new, it will return
     * an empty collection; or if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowDef.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTWorkflowDef(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef is new, it will return
     * an empty collection; or if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowDef.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef is new, it will return
     * an empty collection; or if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowDef.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef is new, it will return
     * an empty collection; or if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowConnects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowDef.
     */
    protected List<TWorkflowConnect> getTWorkflowConnectsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowConnects == null)
        {
            if (isNew())
            {
               collTWorkflowConnects = new ArrayList<TWorkflowConnect>();
            }
            else
            {
                criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID());
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowConnectPeer.WORKFLOW, getObjectID());
            if (!lastTWorkflowConnectsCriteria.equals(criteria))
            {
                collTWorkflowConnects = TWorkflowConnectPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWorkflowConnectsCriteria = criteria;

        return collTWorkflowConnects;
    }





    /**
     * Collection to store aggregation of collTWorkflowComments
     */
    protected List<TWorkflowComment> collTWorkflowComments;

    /**
     * Temporary storage of collTWorkflowComments to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowComments()
    {
        if (collTWorkflowComments == null)
        {
            collTWorkflowComments = new ArrayList<TWorkflowComment>();
        }
    }


    /**
     * Method called to associate a TWorkflowComment object to this object
     * through the TWorkflowComment foreign key attribute
     *
     * @param l TWorkflowComment
     * @throws TorqueException
     */
    public void addTWorkflowComment(TWorkflowComment l) throws TorqueException
    {
        getTWorkflowComments().add(l);
        l.setTWorkflowDef((TWorkflowDef) this);
    }

    /**
     * Method called to associate a TWorkflowComment object to this object
     * through the TWorkflowComment foreign key attribute using connection.
     *
     * @param l TWorkflowComment
     * @throws TorqueException
     */
    public void addTWorkflowComment(TWorkflowComment l, Connection con) throws TorqueException
    {
        getTWorkflowComments(con).add(l);
        l.setTWorkflowDef((TWorkflowDef) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowComments
     */
    private Criteria lastTWorkflowCommentsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowComments(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowComment> getTWorkflowComments()
        throws TorqueException
    {
        if (collTWorkflowComments == null)
        {
            collTWorkflowComments = getTWorkflowComments(new Criteria(10));
        }
        return collTWorkflowComments;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowComments from storage.
     * If this TWorkflowDef is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowComment> getTWorkflowComments(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowComments == null)
        {
            if (isNew())
            {
               collTWorkflowComments = new ArrayList<TWorkflowComment>();
            }
            else
            {
                criteria.add(TWorkflowCommentPeer.WORKFLOW, getObjectID() );
                collTWorkflowComments = TWorkflowCommentPeer.doSelect(criteria);
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
                criteria.add(TWorkflowCommentPeer.WORKFLOW, getObjectID());
                if (!lastTWorkflowCommentsCriteria.equals(criteria))
                {
                    collTWorkflowComments = TWorkflowCommentPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowCommentsCriteria = criteria;

        return collTWorkflowComments;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowComments(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowComment> getTWorkflowComments(Connection con) throws TorqueException
    {
        if (collTWorkflowComments == null)
        {
            collTWorkflowComments = getTWorkflowComments(new Criteria(10), con);
        }
        return collTWorkflowComments;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowComments from storage.
     * If this TWorkflowDef is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowComment> getTWorkflowComments(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowComments == null)
        {
            if (isNew())
            {
               collTWorkflowComments = new ArrayList<TWorkflowComment>();
            }
            else
            {
                 criteria.add(TWorkflowCommentPeer.WORKFLOW, getObjectID());
                 collTWorkflowComments = TWorkflowCommentPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowCommentPeer.WORKFLOW, getObjectID());
                 if (!lastTWorkflowCommentsCriteria.equals(criteria))
                 {
                     collTWorkflowComments = TWorkflowCommentPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowCommentsCriteria = criteria;

         return collTWorkflowComments;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowDef is new, it will return
     * an empty collection; or if this TWorkflowDef has previously
     * been saved, it will retrieve related TWorkflowComments from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowDef.
     */
    protected List<TWorkflowComment> getTWorkflowCommentsJoinTWorkflowDef(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowComments == null)
        {
            if (isNew())
            {
               collTWorkflowComments = new ArrayList<TWorkflowComment>();
            }
            else
            {
                criteria.add(TWorkflowCommentPeer.WORKFLOW, getObjectID());
                collTWorkflowComments = TWorkflowCommentPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowCommentPeer.WORKFLOW, getObjectID());
            if (!lastTWorkflowCommentsCriteria.equals(criteria))
            {
                collTWorkflowComments = TWorkflowCommentPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        lastTWorkflowCommentsCriteria = criteria;

        return collTWorkflowComments;
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
            fieldNames.add("Description");
            fieldNames.add("TagLabel");
            fieldNames.add("Owner");
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
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("TagLabel"))
        {
            return getTagLabel();
        }
        if (name.equals("Owner"))
        {
            return getOwner();
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
        if (name.equals("TagLabel"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTagLabel((String) value);
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
        if (name.equals(TWorkflowDefPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TWorkflowDefPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TWorkflowDefPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TWorkflowDefPeer.TAGLABEL))
        {
            return getTagLabel();
        }
        if (name.equals(TWorkflowDefPeer.OWNER))
        {
            return getOwner();
        }
        if (name.equals(TWorkflowDefPeer.TPUUID))
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
      if (TWorkflowDefPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TWorkflowDefPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TWorkflowDefPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TWorkflowDefPeer.TAGLABEL.equals(name))
        {
            return setByName("TagLabel", value);
        }
      if (TWorkflowDefPeer.OWNER.equals(name))
        {
            return setByName("Owner", value);
        }
      if (TWorkflowDefPeer.TPUUID.equals(name))
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
            return getDescription();
        }
        if (pos == 3)
        {
            return getTagLabel();
        }
        if (pos == 4)
        {
            return getOwner();
        }
        if (pos == 5)
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
            return setByName("Description", value);
        }
    if (position == 3)
        {
            return setByName("TagLabel", value);
        }
    if (position == 4)
        {
            return setByName("Owner", value);
        }
    if (position == 5)
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
        save(TWorkflowDefPeer.DATABASE_NAME);
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
                    TWorkflowDefPeer.doInsert((TWorkflowDef) this, con);
                    setNew(false);
                }
                else
                {
                    TWorkflowDefPeer.doUpdate((TWorkflowDef) this, con);
                }
            }


            if (collTWorkflowTransitions != null)
            {
                for (int i = 0; i < collTWorkflowTransitions.size(); i++)
                {
                    ((TWorkflowTransition) collTWorkflowTransitions.get(i)).save(con);
                }
            }

            if (collTWorkflowStations != null)
            {
                for (int i = 0; i < collTWorkflowStations.size(); i++)
                {
                    ((TWorkflowStation) collTWorkflowStations.get(i)).save(con);
                }
            }

            if (collTWorkflowConnects != null)
            {
                for (int i = 0; i < collTWorkflowConnects.size(); i++)
                {
                    ((TWorkflowConnect) collTWorkflowConnects.get(i)).save(con);
                }
            }

            if (collTWorkflowComments != null)
            {
                for (int i = 0; i < collTWorkflowComments.size(); i++)
                {
                    ((TWorkflowComment) collTWorkflowComments.get(i)).save(con);
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
    public TWorkflowDef copy() throws TorqueException
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
    public TWorkflowDef copy(Connection con) throws TorqueException
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
    public TWorkflowDef copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TWorkflowDef(), deepcopy);
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
    public TWorkflowDef copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TWorkflowDef(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TWorkflowDef copyInto(TWorkflowDef copyObj) throws TorqueException
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
    protected TWorkflowDef copyInto(TWorkflowDef copyObj, Connection con) throws TorqueException
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
    protected TWorkflowDef copyInto(TWorkflowDef copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setDescription(description);
        copyObj.setTagLabel(tagLabel);
        copyObj.setOwner(owner);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkflowTransition> vTWorkflowTransitions = getTWorkflowTransitions();
        if (vTWorkflowTransitions != null)
        {
            for (int i = 0; i < vTWorkflowTransitions.size(); i++)
            {
                TWorkflowTransition obj =  vTWorkflowTransitions.get(i);
                copyObj.addTWorkflowTransition(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowTransitions = null;
        }


        List<TWorkflowStation> vTWorkflowStations = getTWorkflowStations();
        if (vTWorkflowStations != null)
        {
            for (int i = 0; i < vTWorkflowStations.size(); i++)
            {
                TWorkflowStation obj =  vTWorkflowStations.get(i);
                copyObj.addTWorkflowStation(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowStations = null;
        }


        List<TWorkflowConnect> vTWorkflowConnects = getTWorkflowConnects();
        if (vTWorkflowConnects != null)
        {
            for (int i = 0; i < vTWorkflowConnects.size(); i++)
            {
                TWorkflowConnect obj =  vTWorkflowConnects.get(i);
                copyObj.addTWorkflowConnect(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowConnects = null;
        }


        List<TWorkflowComment> vTWorkflowComments = getTWorkflowComments();
        if (vTWorkflowComments != null)
        {
            for (int i = 0; i < vTWorkflowComments.size(); i++)
            {
                TWorkflowComment obj =  vTWorkflowComments.get(i);
                copyObj.addTWorkflowComment(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowComments = null;
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
    protected TWorkflowDef copyInto(TWorkflowDef copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setDescription(description);
        copyObj.setTagLabel(tagLabel);
        copyObj.setOwner(owner);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkflowTransition> vTWorkflowTransitions = getTWorkflowTransitions(con);
        if (vTWorkflowTransitions != null)
        {
            for (int i = 0; i < vTWorkflowTransitions.size(); i++)
            {
                TWorkflowTransition obj =  vTWorkflowTransitions.get(i);
                copyObj.addTWorkflowTransition(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowTransitions = null;
        }


        List<TWorkflowStation> vTWorkflowStations = getTWorkflowStations(con);
        if (vTWorkflowStations != null)
        {
            for (int i = 0; i < vTWorkflowStations.size(); i++)
            {
                TWorkflowStation obj =  vTWorkflowStations.get(i);
                copyObj.addTWorkflowStation(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowStations = null;
        }


        List<TWorkflowConnect> vTWorkflowConnects = getTWorkflowConnects(con);
        if (vTWorkflowConnects != null)
        {
            for (int i = 0; i < vTWorkflowConnects.size(); i++)
            {
                TWorkflowConnect obj =  vTWorkflowConnects.get(i);
                copyObj.addTWorkflowConnect(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowConnects = null;
        }


        List<TWorkflowComment> vTWorkflowComments = getTWorkflowComments(con);
        if (vTWorkflowComments != null)
        {
            for (int i = 0; i < vTWorkflowComments.size(); i++)
            {
                TWorkflowComment obj =  vTWorkflowComments.get(i);
                copyObj.addTWorkflowComment(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowComments = null;
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
    public TWorkflowDefPeer getPeer()
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
        return TWorkflowDefPeer.getTableMap();
    }

  
    /**
     * Creates a TWorkflowDefBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TWorkflowDefBean with the contents of this object
     */
    public TWorkflowDefBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TWorkflowDefBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TWorkflowDefBean with the contents of this object
     */
    public TWorkflowDefBean getBean(IdentityMap createdBeans)
    {
        TWorkflowDefBean result = (TWorkflowDefBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TWorkflowDefBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setDescription(getDescription());
        result.setTagLabel(getTagLabel());
        result.setOwner(getOwner());
        result.setUuid(getUuid());



        if (collTWorkflowTransitions != null)
        {
            List<TWorkflowTransitionBean> relatedBeans = new ArrayList<TWorkflowTransitionBean>(collTWorkflowTransitions.size());
            for (Iterator<TWorkflowTransition> collTWorkflowTransitionsIt = collTWorkflowTransitions.iterator(); collTWorkflowTransitionsIt.hasNext(); )
            {
                TWorkflowTransition related = (TWorkflowTransition) collTWorkflowTransitionsIt.next();
                TWorkflowTransitionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowTransitionBeans(relatedBeans);
        }


        if (collTWorkflowStations != null)
        {
            List<TWorkflowStationBean> relatedBeans = new ArrayList<TWorkflowStationBean>(collTWorkflowStations.size());
            for (Iterator<TWorkflowStation> collTWorkflowStationsIt = collTWorkflowStations.iterator(); collTWorkflowStationsIt.hasNext(); )
            {
                TWorkflowStation related = (TWorkflowStation) collTWorkflowStationsIt.next();
                TWorkflowStationBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowStationBeans(relatedBeans);
        }


        if (collTWorkflowConnects != null)
        {
            List<TWorkflowConnectBean> relatedBeans = new ArrayList<TWorkflowConnectBean>(collTWorkflowConnects.size());
            for (Iterator<TWorkflowConnect> collTWorkflowConnectsIt = collTWorkflowConnects.iterator(); collTWorkflowConnectsIt.hasNext(); )
            {
                TWorkflowConnect related = (TWorkflowConnect) collTWorkflowConnectsIt.next();
                TWorkflowConnectBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowConnectBeans(relatedBeans);
        }


        if (collTWorkflowComments != null)
        {
            List<TWorkflowCommentBean> relatedBeans = new ArrayList<TWorkflowCommentBean>(collTWorkflowComments.size());
            for (Iterator<TWorkflowComment> collTWorkflowCommentsIt = collTWorkflowComments.iterator(); collTWorkflowCommentsIt.hasNext(); )
            {
                TWorkflowComment related = (TWorkflowComment) collTWorkflowCommentsIt.next();
                TWorkflowCommentBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowCommentBeans(relatedBeans);
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
     * Creates an instance of TWorkflowDef with the contents
     * of a TWorkflowDefBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TWorkflowDefBean which contents are used to create
     *        the resulting class
     * @return an instance of TWorkflowDef with the contents of bean
     */
    public static TWorkflowDef createTWorkflowDef(TWorkflowDefBean bean)
        throws TorqueException
    {
        return createTWorkflowDef(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TWorkflowDef with the contents
     * of a TWorkflowDefBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TWorkflowDefBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TWorkflowDef with the contents of bean
     */

    public static TWorkflowDef createTWorkflowDef(TWorkflowDefBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TWorkflowDef result = (TWorkflowDef) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TWorkflowDef();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setDescription(bean.getDescription());
        result.setTagLabel(bean.getTagLabel());
        result.setOwner(bean.getOwner());
        result.setUuid(bean.getUuid());



        {
            List<TWorkflowTransitionBean> relatedBeans = bean.getTWorkflowTransitionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowTransitionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowTransitionBean relatedBean =  relatedBeansIt.next();
                    TWorkflowTransition related = TWorkflowTransition.createTWorkflowTransition(relatedBean, createdObjects);
                    result.addTWorkflowTransitionFromBean(related);
                }
            }
        }


        {
            List<TWorkflowStationBean> relatedBeans = bean.getTWorkflowStationBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowStationBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowStationBean relatedBean =  relatedBeansIt.next();
                    TWorkflowStation related = TWorkflowStation.createTWorkflowStation(relatedBean, createdObjects);
                    result.addTWorkflowStationFromBean(related);
                }
            }
        }


        {
            List<TWorkflowConnectBean> relatedBeans = bean.getTWorkflowConnectBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowConnectBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowConnectBean relatedBean =  relatedBeansIt.next();
                    TWorkflowConnect related = TWorkflowConnect.createTWorkflowConnect(relatedBean, createdObjects);
                    result.addTWorkflowConnectFromBean(related);
                }
            }
        }


        {
            List<TWorkflowCommentBean> relatedBeans = bean.getTWorkflowCommentBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowCommentBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowCommentBean relatedBean =  relatedBeansIt.next();
                    TWorkflowComment related = TWorkflowComment.createTWorkflowComment(relatedBean, createdObjects);
                    result.addTWorkflowCommentFromBean(related);
                }
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
     * Method called to associate a TWorkflowTransition object to this object.
     * through the TWorkflowTransition foreign key attribute
     *
     * @param toAdd TWorkflowTransition
     */
    protected void addTWorkflowTransitionFromBean(TWorkflowTransition toAdd)
    {
        initTWorkflowTransitions();
        collTWorkflowTransitions.add(toAdd);
    }


    /**
     * Method called to associate a TWorkflowStation object to this object.
     * through the TWorkflowStation foreign key attribute
     *
     * @param toAdd TWorkflowStation
     */
    protected void addTWorkflowStationFromBean(TWorkflowStation toAdd)
    {
        initTWorkflowStations();
        collTWorkflowStations.add(toAdd);
    }


    /**
     * Method called to associate a TWorkflowConnect object to this object.
     * through the TWorkflowConnect foreign key attribute
     *
     * @param toAdd TWorkflowConnect
     */
    protected void addTWorkflowConnectFromBean(TWorkflowConnect toAdd)
    {
        initTWorkflowConnects();
        collTWorkflowConnects.add(toAdd);
    }


    /**
     * Method called to associate a TWorkflowComment object to this object.
     * through the TWorkflowComment foreign key attribute
     *
     * @param toAdd TWorkflowComment
     */
    protected void addTWorkflowCommentFromBean(TWorkflowComment toAdd)
    {
        initTWorkflowComments();
        collTWorkflowComments.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TWorkflowDef:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("TagLabel = ")
           .append(getTagLabel())
           .append("\n");
        str.append("Owner = ")
           .append(getOwner())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
