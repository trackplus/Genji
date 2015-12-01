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



import com.aurel.track.persist.TState;
import com.aurel.track.persist.TStatePeer;
import com.aurel.track.persist.TWorkflowDef;
import com.aurel.track.persist.TWorkflowDefPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TWorkflowStationBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkflowDefBean;

import com.aurel.track.beans.TWorkflowTransitionBean;
import com.aurel.track.beans.TWorkflowTransitionBean;
import com.aurel.track.beans.TWorkflowActivityBean;
import com.aurel.track.beans.TWorkflowActivityBean;
import com.aurel.track.beans.TWorkflowActivityBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TWorkflowStation
 */
public abstract class BaseTWorkflowStation extends TpBaseObject
{
    /** The Peer class */
    private static final TWorkflowStationPeer peer =
        new TWorkflowStationPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the status field */
    private Integer status;

    /** The value for the workflow field */
    private Integer workflow;

    /** The value for the nodeX field */
    private Integer nodeX;

    /** The value for the nodeY field */
    private Integer nodeY;

    /** The value for the stationType field */
    private Integer stationType;

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
        if (collTWorkflowTransitionsRelatedByStationFrom != null)
        {
            for (int i = 0; i < collTWorkflowTransitionsRelatedByStationFrom.size(); i++)
            {
                ((TWorkflowTransition) collTWorkflowTransitionsRelatedByStationFrom.get(i))
                        .setStationFrom(v);
            }
        }

        // update associated TWorkflowTransition
        if (collTWorkflowTransitionsRelatedByStationTo != null)
        {
            for (int i = 0; i < collTWorkflowTransitionsRelatedByStationTo.size(); i++)
            {
                ((TWorkflowTransition) collTWorkflowTransitionsRelatedByStationTo.get(i))
                        .setStationTo(v);
            }
        }

        // update associated TWorkflowActivity
        if (collTWorkflowActivitysRelatedByStationEntryActivity != null)
        {
            for (int i = 0; i < collTWorkflowActivitysRelatedByStationEntryActivity.size(); i++)
            {
                ((TWorkflowActivity) collTWorkflowActivitysRelatedByStationEntryActivity.get(i))
                        .setStationEntryActivity(v);
            }
        }

        // update associated TWorkflowActivity
        if (collTWorkflowActivitysRelatedByStationExitActivity != null)
        {
            for (int i = 0; i < collTWorkflowActivitysRelatedByStationExitActivity.size(); i++)
            {
                ((TWorkflowActivity) collTWorkflowActivitysRelatedByStationExitActivity.get(i))
                        .setStationExitActivity(v);
            }
        }

        // update associated TWorkflowActivity
        if (collTWorkflowActivitysRelatedByStationDoActivity != null)
        {
            for (int i = 0; i < collTWorkflowActivitysRelatedByStationDoActivity.size(); i++)
            {
                ((TWorkflowActivity) collTWorkflowActivitysRelatedByStationDoActivity.get(i))
                        .setStationDoActivity(v);
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
     * Get the Status
     *
     * @return Integer
     */
    public Integer getStatus()
    {
        return status;
    }


    /**
     * Set the value of Status
     *
     * @param v new value
     */
    public void setStatus(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.status, v))
        {
            this.status = v;
            setModified(true);
        }


        if (aTState != null && !ObjectUtils.equals(aTState.getObjectID(), v))
        {
            aTState = null;
        }

    }

    /**
     * Get the Workflow
     *
     * @return Integer
     */
    public Integer getWorkflow()
    {
        return workflow;
    }


    /**
     * Set the value of Workflow
     *
     * @param v new value
     */
    public void setWorkflow(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workflow, v))
        {
            this.workflow = v;
            setModified(true);
        }


        if (aTWorkflowDef != null && !ObjectUtils.equals(aTWorkflowDef.getObjectID(), v))
        {
            aTWorkflowDef = null;
        }

    }

    /**
     * Get the NodeX
     *
     * @return Integer
     */
    public Integer getNodeX()
    {
        return nodeX;
    }


    /**
     * Set the value of NodeX
     *
     * @param v new value
     */
    public void setNodeX(Integer v) 
    {

        if (!ObjectUtils.equals(this.nodeX, v))
        {
            this.nodeX = v;
            setModified(true);
        }


    }

    /**
     * Get the NodeY
     *
     * @return Integer
     */
    public Integer getNodeY()
    {
        return nodeY;
    }


    /**
     * Set the value of NodeY
     *
     * @param v new value
     */
    public void setNodeY(Integer v) 
    {

        if (!ObjectUtils.equals(this.nodeY, v))
        {
            this.nodeY = v;
            setModified(true);
        }


    }

    /**
     * Get the StationType
     *
     * @return Integer
     */
    public Integer getStationType()
    {
        return stationType;
    }


    /**
     * Set the value of StationType
     *
     * @param v new value
     */
    public void setStationType(Integer v) 
    {

        if (!ObjectUtils.equals(this.stationType, v))
        {
            this.stationType = v;
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

    



    private TState aTState;

    /**
     * Declares an association between this object and a TState object
     *
     * @param v TState
     * @throws TorqueException
     */
    public void setTState(TState v) throws TorqueException
    {
        if (v == null)
        {
            setStatus((Integer) null);
        }
        else
        {
            setStatus(v.getObjectID());
        }
        aTState = v;
    }


    /**
     * Returns the associated TState object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TState object
     * @throws TorqueException
     */
    public TState getTState()
        throws TorqueException
    {
        if (aTState == null && (!ObjectUtils.equals(this.status, null)))
        {
            aTState = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.status));
        }
        return aTState;
    }

    /**
     * Return the associated TState object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TState object
     * @throws TorqueException
     */
    public TState getTState(Connection connection)
        throws TorqueException
    {
        if (aTState == null && (!ObjectUtils.equals(this.status, null)))
        {
            aTState = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.status), connection);
        }
        return aTState;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTStateKey(ObjectKey key) throws TorqueException
    {

        setStatus(new Integer(((NumberKey) key).intValue()));
    }




    private TWorkflowDef aTWorkflowDef;

    /**
     * Declares an association between this object and a TWorkflowDef object
     *
     * @param v TWorkflowDef
     * @throws TorqueException
     */
    public void setTWorkflowDef(TWorkflowDef v) throws TorqueException
    {
        if (v == null)
        {
            setWorkflow((Integer) null);
        }
        else
        {
            setWorkflow(v.getObjectID());
        }
        aTWorkflowDef = v;
    }


    /**
     * Returns the associated TWorkflowDef object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkflowDef object
     * @throws TorqueException
     */
    public TWorkflowDef getTWorkflowDef()
        throws TorqueException
    {
        if (aTWorkflowDef == null && (!ObjectUtils.equals(this.workflow, null)))
        {
            aTWorkflowDef = TWorkflowDefPeer.retrieveByPK(SimpleKey.keyFor(this.workflow));
        }
        return aTWorkflowDef;
    }

    /**
     * Return the associated TWorkflowDef object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkflowDef object
     * @throws TorqueException
     */
    public TWorkflowDef getTWorkflowDef(Connection connection)
        throws TorqueException
    {
        if (aTWorkflowDef == null && (!ObjectUtils.equals(this.workflow, null)))
        {
            aTWorkflowDef = TWorkflowDefPeer.retrieveByPK(SimpleKey.keyFor(this.workflow), connection);
        }
        return aTWorkflowDef;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkflowDefKey(ObjectKey key) throws TorqueException
    {

        setWorkflow(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTWorkflowTransitionsRelatedByStationFrom
     */
    protected List<TWorkflowTransition> collTWorkflowTransitionsRelatedByStationFrom;

    /**
     * Temporary storage of collTWorkflowTransitionsRelatedByStationFrom to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowTransitionsRelatedByStationFrom()
    {
        if (collTWorkflowTransitionsRelatedByStationFrom == null)
        {
            collTWorkflowTransitionsRelatedByStationFrom = new ArrayList<TWorkflowTransition>();
        }
    }


    /**
     * Method called to associate a TWorkflowTransition object to this object
     * through the TWorkflowTransition foreign key attribute
     *
     * @param l TWorkflowTransition
     * @throws TorqueException
     */
    public void addTWorkflowTransitionRelatedByStationFrom(TWorkflowTransition l) throws TorqueException
    {
        getTWorkflowTransitionsRelatedByStationFrom().add(l);
        l.setTWorkflowStationRelatedByStationFrom((TWorkflowStation) this);
    }

    /**
     * Method called to associate a TWorkflowTransition object to this object
     * through the TWorkflowTransition foreign key attribute using connection.
     *
     * @param l TWorkflowTransition
     * @throws TorqueException
     */
    public void addTWorkflowTransitionRelatedByStationFrom(TWorkflowTransition l, Connection con) throws TorqueException
    {
        getTWorkflowTransitionsRelatedByStationFrom(con).add(l);
        l.setTWorkflowStationRelatedByStationFrom((TWorkflowStation) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowTransitionsRelatedByStationFrom
     */
    private Criteria lastTWorkflowTransitionsRelatedByStationFromCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowTransitionsRelatedByStationFrom(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationFrom()
        throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationFrom == null)
        {
            collTWorkflowTransitionsRelatedByStationFrom = getTWorkflowTransitionsRelatedByStationFrom(new Criteria(10));
        }
        return collTWorkflowTransitionsRelatedByStationFrom;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowTransitionsRelatedByStationFrom from storage.
     * If this TWorkflowStation is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationFrom(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationFrom == null)
        {
            if (isNew())
            {
               collTWorkflowTransitionsRelatedByStationFrom = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.STATIONFROM, getObjectID() );
                collTWorkflowTransitionsRelatedByStationFrom = TWorkflowTransitionPeer.doSelect(criteria);
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
                criteria.add(TWorkflowTransitionPeer.STATIONFROM, getObjectID());
                if (!lastTWorkflowTransitionsRelatedByStationFromCriteria.equals(criteria))
                {
                    collTWorkflowTransitionsRelatedByStationFrom = TWorkflowTransitionPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowTransitionsRelatedByStationFromCriteria = criteria;

        return collTWorkflowTransitionsRelatedByStationFrom;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowTransitionsRelatedByStationFrom(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationFrom(Connection con) throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationFrom == null)
        {
            collTWorkflowTransitionsRelatedByStationFrom = getTWorkflowTransitionsRelatedByStationFrom(new Criteria(10), con);
        }
        return collTWorkflowTransitionsRelatedByStationFrom;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowTransitionsRelatedByStationFrom from storage.
     * If this TWorkflowStation is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationFrom(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationFrom == null)
        {
            if (isNew())
            {
               collTWorkflowTransitionsRelatedByStationFrom = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                 criteria.add(TWorkflowTransitionPeer.STATIONFROM, getObjectID());
                 collTWorkflowTransitionsRelatedByStationFrom = TWorkflowTransitionPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowTransitionPeer.STATIONFROM, getObjectID());
                 if (!lastTWorkflowTransitionsRelatedByStationFromCriteria.equals(criteria))
                 {
                     collTWorkflowTransitionsRelatedByStationFrom = TWorkflowTransitionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowTransitionsRelatedByStationFromCriteria = criteria;

         return collTWorkflowTransitionsRelatedByStationFrom;
     }



















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowTransitionsRelatedByStationFrom from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationFromJoinTWorkflowStationRelatedByStationTo(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationFrom == null)
        {
            if (isNew())
            {
               collTWorkflowTransitionsRelatedByStationFrom = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.STATIONFROM, getObjectID());
                collTWorkflowTransitionsRelatedByStationFrom = TWorkflowTransitionPeer.doSelectJoinTWorkflowStationRelatedByStationTo(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.STATIONFROM, getObjectID());
            if (!lastTWorkflowTransitionsRelatedByStationFromCriteria.equals(criteria))
            {
                collTWorkflowTransitionsRelatedByStationFrom = TWorkflowTransitionPeer.doSelectJoinTWorkflowStationRelatedByStationTo(criteria);
            }
        }
        lastTWorkflowTransitionsRelatedByStationFromCriteria = criteria;

        return collTWorkflowTransitionsRelatedByStationFrom;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowTransitionsRelatedByStationFrom from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationFromJoinTAction(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationFrom == null)
        {
            if (isNew())
            {
               collTWorkflowTransitionsRelatedByStationFrom = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.STATIONFROM, getObjectID());
                collTWorkflowTransitionsRelatedByStationFrom = TWorkflowTransitionPeer.doSelectJoinTAction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.STATIONFROM, getObjectID());
            if (!lastTWorkflowTransitionsRelatedByStationFromCriteria.equals(criteria))
            {
                collTWorkflowTransitionsRelatedByStationFrom = TWorkflowTransitionPeer.doSelectJoinTAction(criteria);
            }
        }
        lastTWorkflowTransitionsRelatedByStationFromCriteria = criteria;

        return collTWorkflowTransitionsRelatedByStationFrom;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowTransitionsRelatedByStationFrom from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationFromJoinTWorkflowDef(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationFrom == null)
        {
            if (isNew())
            {
               collTWorkflowTransitionsRelatedByStationFrom = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.STATIONFROM, getObjectID());
                collTWorkflowTransitionsRelatedByStationFrom = TWorkflowTransitionPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.STATIONFROM, getObjectID());
            if (!lastTWorkflowTransitionsRelatedByStationFromCriteria.equals(criteria))
            {
                collTWorkflowTransitionsRelatedByStationFrom = TWorkflowTransitionPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        lastTWorkflowTransitionsRelatedByStationFromCriteria = criteria;

        return collTWorkflowTransitionsRelatedByStationFrom;
    }





    /**
     * Collection to store aggregation of collTWorkflowTransitionsRelatedByStationTo
     */
    protected List<TWorkflowTransition> collTWorkflowTransitionsRelatedByStationTo;

    /**
     * Temporary storage of collTWorkflowTransitionsRelatedByStationTo to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowTransitionsRelatedByStationTo()
    {
        if (collTWorkflowTransitionsRelatedByStationTo == null)
        {
            collTWorkflowTransitionsRelatedByStationTo = new ArrayList<TWorkflowTransition>();
        }
    }


    /**
     * Method called to associate a TWorkflowTransition object to this object
     * through the TWorkflowTransition foreign key attribute
     *
     * @param l TWorkflowTransition
     * @throws TorqueException
     */
    public void addTWorkflowTransitionRelatedByStationTo(TWorkflowTransition l) throws TorqueException
    {
        getTWorkflowTransitionsRelatedByStationTo().add(l);
        l.setTWorkflowStationRelatedByStationTo((TWorkflowStation) this);
    }

    /**
     * Method called to associate a TWorkflowTransition object to this object
     * through the TWorkflowTransition foreign key attribute using connection.
     *
     * @param l TWorkflowTransition
     * @throws TorqueException
     */
    public void addTWorkflowTransitionRelatedByStationTo(TWorkflowTransition l, Connection con) throws TorqueException
    {
        getTWorkflowTransitionsRelatedByStationTo(con).add(l);
        l.setTWorkflowStationRelatedByStationTo((TWorkflowStation) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowTransitionsRelatedByStationTo
     */
    private Criteria lastTWorkflowTransitionsRelatedByStationToCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowTransitionsRelatedByStationTo(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationTo()
        throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationTo == null)
        {
            collTWorkflowTransitionsRelatedByStationTo = getTWorkflowTransitionsRelatedByStationTo(new Criteria(10));
        }
        return collTWorkflowTransitionsRelatedByStationTo;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowTransitionsRelatedByStationTo from storage.
     * If this TWorkflowStation is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationTo(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationTo == null)
        {
            if (isNew())
            {
               collTWorkflowTransitionsRelatedByStationTo = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.STATIONTO, getObjectID() );
                collTWorkflowTransitionsRelatedByStationTo = TWorkflowTransitionPeer.doSelect(criteria);
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
                criteria.add(TWorkflowTransitionPeer.STATIONTO, getObjectID());
                if (!lastTWorkflowTransitionsRelatedByStationToCriteria.equals(criteria))
                {
                    collTWorkflowTransitionsRelatedByStationTo = TWorkflowTransitionPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowTransitionsRelatedByStationToCriteria = criteria;

        return collTWorkflowTransitionsRelatedByStationTo;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowTransitionsRelatedByStationTo(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationTo(Connection con) throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationTo == null)
        {
            collTWorkflowTransitionsRelatedByStationTo = getTWorkflowTransitionsRelatedByStationTo(new Criteria(10), con);
        }
        return collTWorkflowTransitionsRelatedByStationTo;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowTransitionsRelatedByStationTo from storage.
     * If this TWorkflowStation is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationTo(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationTo == null)
        {
            if (isNew())
            {
               collTWorkflowTransitionsRelatedByStationTo = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                 criteria.add(TWorkflowTransitionPeer.STATIONTO, getObjectID());
                 collTWorkflowTransitionsRelatedByStationTo = TWorkflowTransitionPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowTransitionPeer.STATIONTO, getObjectID());
                 if (!lastTWorkflowTransitionsRelatedByStationToCriteria.equals(criteria))
                 {
                     collTWorkflowTransitionsRelatedByStationTo = TWorkflowTransitionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowTransitionsRelatedByStationToCriteria = criteria;

         return collTWorkflowTransitionsRelatedByStationTo;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowTransitionsRelatedByStationTo from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationToJoinTWorkflowStationRelatedByStationFrom(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationTo == null)
        {
            if (isNew())
            {
               collTWorkflowTransitionsRelatedByStationTo = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.STATIONTO, getObjectID());
                collTWorkflowTransitionsRelatedByStationTo = TWorkflowTransitionPeer.doSelectJoinTWorkflowStationRelatedByStationFrom(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.STATIONTO, getObjectID());
            if (!lastTWorkflowTransitionsRelatedByStationToCriteria.equals(criteria))
            {
                collTWorkflowTransitionsRelatedByStationTo = TWorkflowTransitionPeer.doSelectJoinTWorkflowStationRelatedByStationFrom(criteria);
            }
        }
        lastTWorkflowTransitionsRelatedByStationToCriteria = criteria;

        return collTWorkflowTransitionsRelatedByStationTo;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowTransitionsRelatedByStationTo from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationToJoinTAction(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationTo == null)
        {
            if (isNew())
            {
               collTWorkflowTransitionsRelatedByStationTo = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.STATIONTO, getObjectID());
                collTWorkflowTransitionsRelatedByStationTo = TWorkflowTransitionPeer.doSelectJoinTAction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.STATIONTO, getObjectID());
            if (!lastTWorkflowTransitionsRelatedByStationToCriteria.equals(criteria))
            {
                collTWorkflowTransitionsRelatedByStationTo = TWorkflowTransitionPeer.doSelectJoinTAction(criteria);
            }
        }
        lastTWorkflowTransitionsRelatedByStationToCriteria = criteria;

        return collTWorkflowTransitionsRelatedByStationTo;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowTransitionsRelatedByStationTo from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowTransition> getTWorkflowTransitionsRelatedByStationToJoinTWorkflowDef(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowTransitionsRelatedByStationTo == null)
        {
            if (isNew())
            {
               collTWorkflowTransitionsRelatedByStationTo = new ArrayList<TWorkflowTransition>();
            }
            else
            {
                criteria.add(TWorkflowTransitionPeer.STATIONTO, getObjectID());
                collTWorkflowTransitionsRelatedByStationTo = TWorkflowTransitionPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.STATIONTO, getObjectID());
            if (!lastTWorkflowTransitionsRelatedByStationToCriteria.equals(criteria))
            {
                collTWorkflowTransitionsRelatedByStationTo = TWorkflowTransitionPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        lastTWorkflowTransitionsRelatedByStationToCriteria = criteria;

        return collTWorkflowTransitionsRelatedByStationTo;
    }





    /**
     * Collection to store aggregation of collTWorkflowActivitysRelatedByStationEntryActivity
     */
    protected List<TWorkflowActivity> collTWorkflowActivitysRelatedByStationEntryActivity;

    /**
     * Temporary storage of collTWorkflowActivitysRelatedByStationEntryActivity to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowActivitysRelatedByStationEntryActivity()
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            collTWorkflowActivitysRelatedByStationEntryActivity = new ArrayList<TWorkflowActivity>();
        }
    }


    /**
     * Method called to associate a TWorkflowActivity object to this object
     * through the TWorkflowActivity foreign key attribute
     *
     * @param l TWorkflowActivity
     * @throws TorqueException
     */
    public void addTWorkflowActivityRelatedByStationEntryActivity(TWorkflowActivity l) throws TorqueException
    {
        getTWorkflowActivitysRelatedByStationEntryActivity().add(l);
        l.setTWorkflowStationRelatedByStationEntryActivity((TWorkflowStation) this);
    }

    /**
     * Method called to associate a TWorkflowActivity object to this object
     * through the TWorkflowActivity foreign key attribute using connection.
     *
     * @param l TWorkflowActivity
     * @throws TorqueException
     */
    public void addTWorkflowActivityRelatedByStationEntryActivity(TWorkflowActivity l, Connection con) throws TorqueException
    {
        getTWorkflowActivitysRelatedByStationEntryActivity(con).add(l);
        l.setTWorkflowStationRelatedByStationEntryActivity((TWorkflowStation) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowActivitysRelatedByStationEntryActivity
     */
    private Criteria lastTWorkflowActivitysRelatedByStationEntryActivityCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowActivitysRelatedByStationEntryActivity(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivity()
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            collTWorkflowActivitysRelatedByStationEntryActivity = getTWorkflowActivitysRelatedByStationEntryActivity(new Criteria(10));
        }
        return collTWorkflowActivitysRelatedByStationEntryActivity;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationEntryActivity from storage.
     * If this TWorkflowStation is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivity(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationEntryActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID() );
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelect(criteria);
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
                criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
                if (!lastTWorkflowActivitysRelatedByStationEntryActivityCriteria.equals(criteria))
                {
                    collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowActivitysRelatedByStationEntryActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationEntryActivity;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowActivitysRelatedByStationEntryActivity(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivity(Connection con) throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            collTWorkflowActivitysRelatedByStationEntryActivity = getTWorkflowActivitysRelatedByStationEntryActivity(new Criteria(10), con);
        }
        return collTWorkflowActivitysRelatedByStationEntryActivity;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationEntryActivity from storage.
     * If this TWorkflowStation is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivity(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationEntryActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                 criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
                 collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
                 if (!lastTWorkflowActivitysRelatedByStationEntryActivityCriteria.equals(criteria))
                 {
                     collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowActivitysRelatedByStationEntryActivityCriteria = criteria;

         return collTWorkflowActivitysRelatedByStationEntryActivity;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationEntryActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivityJoinTWorkflowTransition(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationEntryActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationEntryActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationEntryActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationEntryActivity;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationEntryActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivityJoinTWorkflowStationRelatedByStationExitActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationEntryActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationExitActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationEntryActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationExitActivity(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationEntryActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationEntryActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationEntryActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivityJoinTWorkflowStationRelatedByStationDoActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationEntryActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationDoActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationEntryActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationDoActivity(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationEntryActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationEntryActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationEntryActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivityJoinTScripts(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationEntryActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationEntryActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationEntryActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationEntryActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationEntryActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivityJoinTPersonRelatedByNewMan(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationEntryActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationEntryActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationEntryActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationEntryActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationEntryActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivityJoinTPersonRelatedByNewResp(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationEntryActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationEntryActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationEntryActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationEntryActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationEntryActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivityJoinTSLA(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationEntryActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationEntryActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationEntryActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationEntryActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationEntryActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationEntryActivityJoinTScreen(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationEntryActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationEntryActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONENTRYACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationEntryActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationEntryActivity = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationEntryActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationEntryActivity;
    }





    /**
     * Collection to store aggregation of collTWorkflowActivitysRelatedByStationExitActivity
     */
    protected List<TWorkflowActivity> collTWorkflowActivitysRelatedByStationExitActivity;

    /**
     * Temporary storage of collTWorkflowActivitysRelatedByStationExitActivity to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowActivitysRelatedByStationExitActivity()
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            collTWorkflowActivitysRelatedByStationExitActivity = new ArrayList<TWorkflowActivity>();
        }
    }


    /**
     * Method called to associate a TWorkflowActivity object to this object
     * through the TWorkflowActivity foreign key attribute
     *
     * @param l TWorkflowActivity
     * @throws TorqueException
     */
    public void addTWorkflowActivityRelatedByStationExitActivity(TWorkflowActivity l) throws TorqueException
    {
        getTWorkflowActivitysRelatedByStationExitActivity().add(l);
        l.setTWorkflowStationRelatedByStationExitActivity((TWorkflowStation) this);
    }

    /**
     * Method called to associate a TWorkflowActivity object to this object
     * through the TWorkflowActivity foreign key attribute using connection.
     *
     * @param l TWorkflowActivity
     * @throws TorqueException
     */
    public void addTWorkflowActivityRelatedByStationExitActivity(TWorkflowActivity l, Connection con) throws TorqueException
    {
        getTWorkflowActivitysRelatedByStationExitActivity(con).add(l);
        l.setTWorkflowStationRelatedByStationExitActivity((TWorkflowStation) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowActivitysRelatedByStationExitActivity
     */
    private Criteria lastTWorkflowActivitysRelatedByStationExitActivityCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowActivitysRelatedByStationExitActivity(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivity()
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            collTWorkflowActivitysRelatedByStationExitActivity = getTWorkflowActivitysRelatedByStationExitActivity(new Criteria(10));
        }
        return collTWorkflowActivitysRelatedByStationExitActivity;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationExitActivity from storage.
     * If this TWorkflowStation is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivity(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationExitActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID() );
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelect(criteria);
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
                criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
                if (!lastTWorkflowActivitysRelatedByStationExitActivityCriteria.equals(criteria))
                {
                    collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowActivitysRelatedByStationExitActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationExitActivity;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowActivitysRelatedByStationExitActivity(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivity(Connection con) throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            collTWorkflowActivitysRelatedByStationExitActivity = getTWorkflowActivitysRelatedByStationExitActivity(new Criteria(10), con);
        }
        return collTWorkflowActivitysRelatedByStationExitActivity;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationExitActivity from storage.
     * If this TWorkflowStation is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivity(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationExitActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                 criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
                 collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
                 if (!lastTWorkflowActivitysRelatedByStationExitActivityCriteria.equals(criteria))
                 {
                     collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowActivitysRelatedByStationExitActivityCriteria = criteria;

         return collTWorkflowActivitysRelatedByStationExitActivity;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationExitActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivityJoinTWorkflowTransition(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationExitActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationExitActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationExitActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationExitActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationExitActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivityJoinTWorkflowStationRelatedByStationEntryActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationExitActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationEntryActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationExitActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationEntryActivity(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationExitActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationExitActivity;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationExitActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivityJoinTWorkflowStationRelatedByStationDoActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationExitActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationDoActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationExitActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationDoActivity(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationExitActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationExitActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationExitActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivityJoinTScripts(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationExitActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationExitActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationExitActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationExitActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationExitActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivityJoinTPersonRelatedByNewMan(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationExitActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationExitActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationExitActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationExitActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationExitActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivityJoinTPersonRelatedByNewResp(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationExitActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationExitActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationExitActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationExitActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationExitActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivityJoinTSLA(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationExitActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationExitActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationExitActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationExitActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationExitActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationExitActivityJoinTScreen(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationExitActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationExitActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONEXITACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationExitActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationExitActivity = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationExitActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationExitActivity;
    }





    /**
     * Collection to store aggregation of collTWorkflowActivitysRelatedByStationDoActivity
     */
    protected List<TWorkflowActivity> collTWorkflowActivitysRelatedByStationDoActivity;

    /**
     * Temporary storage of collTWorkflowActivitysRelatedByStationDoActivity to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowActivitysRelatedByStationDoActivity()
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            collTWorkflowActivitysRelatedByStationDoActivity = new ArrayList<TWorkflowActivity>();
        }
    }


    /**
     * Method called to associate a TWorkflowActivity object to this object
     * through the TWorkflowActivity foreign key attribute
     *
     * @param l TWorkflowActivity
     * @throws TorqueException
     */
    public void addTWorkflowActivityRelatedByStationDoActivity(TWorkflowActivity l) throws TorqueException
    {
        getTWorkflowActivitysRelatedByStationDoActivity().add(l);
        l.setTWorkflowStationRelatedByStationDoActivity((TWorkflowStation) this);
    }

    /**
     * Method called to associate a TWorkflowActivity object to this object
     * through the TWorkflowActivity foreign key attribute using connection.
     *
     * @param l TWorkflowActivity
     * @throws TorqueException
     */
    public void addTWorkflowActivityRelatedByStationDoActivity(TWorkflowActivity l, Connection con) throws TorqueException
    {
        getTWorkflowActivitysRelatedByStationDoActivity(con).add(l);
        l.setTWorkflowStationRelatedByStationDoActivity((TWorkflowStation) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowActivitysRelatedByStationDoActivity
     */
    private Criteria lastTWorkflowActivitysRelatedByStationDoActivityCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowActivitysRelatedByStationDoActivity(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivity()
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            collTWorkflowActivitysRelatedByStationDoActivity = getTWorkflowActivitysRelatedByStationDoActivity(new Criteria(10));
        }
        return collTWorkflowActivitysRelatedByStationDoActivity;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationDoActivity from storage.
     * If this TWorkflowStation is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivity(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationDoActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID() );
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelect(criteria);
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
                criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
                if (!lastTWorkflowActivitysRelatedByStationDoActivityCriteria.equals(criteria))
                {
                    collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowActivitysRelatedByStationDoActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationDoActivity;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowActivitysRelatedByStationDoActivity(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivity(Connection con) throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            collTWorkflowActivitysRelatedByStationDoActivity = getTWorkflowActivitysRelatedByStationDoActivity(new Criteria(10), con);
        }
        return collTWorkflowActivitysRelatedByStationDoActivity;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationDoActivity from storage.
     * If this TWorkflowStation is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivity(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationDoActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                 criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
                 collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
                 if (!lastTWorkflowActivitysRelatedByStationDoActivityCriteria.equals(criteria))
                 {
                     collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowActivitysRelatedByStationDoActivityCriteria = criteria;

         return collTWorkflowActivitysRelatedByStationDoActivity;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationDoActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivityJoinTWorkflowTransition(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationDoActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationDoActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationDoActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationDoActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationDoActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivityJoinTWorkflowStationRelatedByStationEntryActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationDoActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationEntryActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationDoActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationEntryActivity(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationDoActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationDoActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationDoActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivityJoinTWorkflowStationRelatedByStationExitActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationDoActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationExitActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationDoActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationExitActivity(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationDoActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationDoActivity;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationDoActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivityJoinTScripts(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationDoActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationDoActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationDoActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationDoActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationDoActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivityJoinTPersonRelatedByNewMan(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationDoActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationDoActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationDoActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationDoActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationDoActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivityJoinTPersonRelatedByNewResp(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationDoActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationDoActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationDoActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationDoActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationDoActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivityJoinTSLA(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationDoActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationDoActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationDoActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationDoActivity;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowStation is new, it will return
     * an empty collection; or if this TWorkflowStation has previously
     * been saved, it will retrieve related TWorkflowActivitysRelatedByStationDoActivity from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowStation.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysRelatedByStationDoActivityJoinTScreen(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitysRelatedByStationDoActivity == null)
        {
            if (isNew())
            {
               collTWorkflowActivitysRelatedByStationDoActivity = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.STATIONDOACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysRelatedByStationDoActivityCriteria.equals(criteria))
            {
                collTWorkflowActivitysRelatedByStationDoActivity = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTWorkflowActivitysRelatedByStationDoActivityCriteria = criteria;

        return collTWorkflowActivitysRelatedByStationDoActivity;
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
            fieldNames.add("Status");
            fieldNames.add("Workflow");
            fieldNames.add("NodeX");
            fieldNames.add("NodeY");
            fieldNames.add("StationType");
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
        if (name.equals("Status"))
        {
            return getStatus();
        }
        if (name.equals("Workflow"))
        {
            return getWorkflow();
        }
        if (name.equals("NodeX"))
        {
            return getNodeX();
        }
        if (name.equals("NodeY"))
        {
            return getNodeY();
        }
        if (name.equals("StationType"))
        {
            return getStationType();
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
        if (name.equals("Status"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStatus((Integer) value);
            return true;
        }
        if (name.equals("Workflow"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkflow((Integer) value);
            return true;
        }
        if (name.equals("NodeX"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNodeX((Integer) value);
            return true;
        }
        if (name.equals("NodeY"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNodeY((Integer) value);
            return true;
        }
        if (name.equals("StationType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStationType((Integer) value);
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
        if (name.equals(TWorkflowStationPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TWorkflowStationPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TWorkflowStationPeer.STATUS))
        {
            return getStatus();
        }
        if (name.equals(TWorkflowStationPeer.WORKFLOW))
        {
            return getWorkflow();
        }
        if (name.equals(TWorkflowStationPeer.NODEX))
        {
            return getNodeX();
        }
        if (name.equals(TWorkflowStationPeer.NODEY))
        {
            return getNodeY();
        }
        if (name.equals(TWorkflowStationPeer.STATIONTYPE))
        {
            return getStationType();
        }
        if (name.equals(TWorkflowStationPeer.TPUUID))
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
      if (TWorkflowStationPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TWorkflowStationPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TWorkflowStationPeer.STATUS.equals(name))
        {
            return setByName("Status", value);
        }
      if (TWorkflowStationPeer.WORKFLOW.equals(name))
        {
            return setByName("Workflow", value);
        }
      if (TWorkflowStationPeer.NODEX.equals(name))
        {
            return setByName("NodeX", value);
        }
      if (TWorkflowStationPeer.NODEY.equals(name))
        {
            return setByName("NodeY", value);
        }
      if (TWorkflowStationPeer.STATIONTYPE.equals(name))
        {
            return setByName("StationType", value);
        }
      if (TWorkflowStationPeer.TPUUID.equals(name))
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
            return getStatus();
        }
        if (pos == 3)
        {
            return getWorkflow();
        }
        if (pos == 4)
        {
            return getNodeX();
        }
        if (pos == 5)
        {
            return getNodeY();
        }
        if (pos == 6)
        {
            return getStationType();
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
            return setByName("Name", value);
        }
    if (position == 2)
        {
            return setByName("Status", value);
        }
    if (position == 3)
        {
            return setByName("Workflow", value);
        }
    if (position == 4)
        {
            return setByName("NodeX", value);
        }
    if (position == 5)
        {
            return setByName("NodeY", value);
        }
    if (position == 6)
        {
            return setByName("StationType", value);
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
        save(TWorkflowStationPeer.DATABASE_NAME);
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
                    TWorkflowStationPeer.doInsert((TWorkflowStation) this, con);
                    setNew(false);
                }
                else
                {
                    TWorkflowStationPeer.doUpdate((TWorkflowStation) this, con);
                }
            }


            if (collTWorkflowTransitionsRelatedByStationFrom != null)
            {
                for (int i = 0; i < collTWorkflowTransitionsRelatedByStationFrom.size(); i++)
                {
                    ((TWorkflowTransition) collTWorkflowTransitionsRelatedByStationFrom.get(i)).save(con);
                }
            }

            if (collTWorkflowTransitionsRelatedByStationTo != null)
            {
                for (int i = 0; i < collTWorkflowTransitionsRelatedByStationTo.size(); i++)
                {
                    ((TWorkflowTransition) collTWorkflowTransitionsRelatedByStationTo.get(i)).save(con);
                }
            }

            if (collTWorkflowActivitysRelatedByStationEntryActivity != null)
            {
                for (int i = 0; i < collTWorkflowActivitysRelatedByStationEntryActivity.size(); i++)
                {
                    ((TWorkflowActivity) collTWorkflowActivitysRelatedByStationEntryActivity.get(i)).save(con);
                }
            }

            if (collTWorkflowActivitysRelatedByStationExitActivity != null)
            {
                for (int i = 0; i < collTWorkflowActivitysRelatedByStationExitActivity.size(); i++)
                {
                    ((TWorkflowActivity) collTWorkflowActivitysRelatedByStationExitActivity.get(i)).save(con);
                }
            }

            if (collTWorkflowActivitysRelatedByStationDoActivity != null)
            {
                for (int i = 0; i < collTWorkflowActivitysRelatedByStationDoActivity.size(); i++)
                {
                    ((TWorkflowActivity) collTWorkflowActivitysRelatedByStationDoActivity.get(i)).save(con);
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
    public TWorkflowStation copy() throws TorqueException
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
    public TWorkflowStation copy(Connection con) throws TorqueException
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
    public TWorkflowStation copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TWorkflowStation(), deepcopy);
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
    public TWorkflowStation copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TWorkflowStation(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TWorkflowStation copyInto(TWorkflowStation copyObj) throws TorqueException
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
    protected TWorkflowStation copyInto(TWorkflowStation copyObj, Connection con) throws TorqueException
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
    protected TWorkflowStation copyInto(TWorkflowStation copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setStatus(status);
        copyObj.setWorkflow(workflow);
        copyObj.setNodeX(nodeX);
        copyObj.setNodeY(nodeY);
        copyObj.setStationType(stationType);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkflowTransition> vTWorkflowTransitionsRelatedByStationFrom = getTWorkflowTransitionsRelatedByStationFrom();
        if (vTWorkflowTransitionsRelatedByStationFrom != null)
        {
            for (int i = 0; i < vTWorkflowTransitionsRelatedByStationFrom.size(); i++)
            {
                TWorkflowTransition obj =  vTWorkflowTransitionsRelatedByStationFrom.get(i);
                copyObj.addTWorkflowTransitionRelatedByStationFrom(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowTransitionsRelatedByStationFrom = null;
        }


        List<TWorkflowTransition> vTWorkflowTransitionsRelatedByStationTo = getTWorkflowTransitionsRelatedByStationTo();
        if (vTWorkflowTransitionsRelatedByStationTo != null)
        {
            for (int i = 0; i < vTWorkflowTransitionsRelatedByStationTo.size(); i++)
            {
                TWorkflowTransition obj =  vTWorkflowTransitionsRelatedByStationTo.get(i);
                copyObj.addTWorkflowTransitionRelatedByStationTo(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowTransitionsRelatedByStationTo = null;
        }


        List<TWorkflowActivity> vTWorkflowActivitysRelatedByStationEntryActivity = getTWorkflowActivitysRelatedByStationEntryActivity();
        if (vTWorkflowActivitysRelatedByStationEntryActivity != null)
        {
            for (int i = 0; i < vTWorkflowActivitysRelatedByStationEntryActivity.size(); i++)
            {
                TWorkflowActivity obj =  vTWorkflowActivitysRelatedByStationEntryActivity.get(i);
                copyObj.addTWorkflowActivityRelatedByStationEntryActivity(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowActivitysRelatedByStationEntryActivity = null;
        }


        List<TWorkflowActivity> vTWorkflowActivitysRelatedByStationExitActivity = getTWorkflowActivitysRelatedByStationExitActivity();
        if (vTWorkflowActivitysRelatedByStationExitActivity != null)
        {
            for (int i = 0; i < vTWorkflowActivitysRelatedByStationExitActivity.size(); i++)
            {
                TWorkflowActivity obj =  vTWorkflowActivitysRelatedByStationExitActivity.get(i);
                copyObj.addTWorkflowActivityRelatedByStationExitActivity(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowActivitysRelatedByStationExitActivity = null;
        }


        List<TWorkflowActivity> vTWorkflowActivitysRelatedByStationDoActivity = getTWorkflowActivitysRelatedByStationDoActivity();
        if (vTWorkflowActivitysRelatedByStationDoActivity != null)
        {
            for (int i = 0; i < vTWorkflowActivitysRelatedByStationDoActivity.size(); i++)
            {
                TWorkflowActivity obj =  vTWorkflowActivitysRelatedByStationDoActivity.get(i);
                copyObj.addTWorkflowActivityRelatedByStationDoActivity(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowActivitysRelatedByStationDoActivity = null;
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
    protected TWorkflowStation copyInto(TWorkflowStation copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setStatus(status);
        copyObj.setWorkflow(workflow);
        copyObj.setNodeX(nodeX);
        copyObj.setNodeY(nodeY);
        copyObj.setStationType(stationType);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkflowTransition> vTWorkflowTransitionsRelatedByStationFrom = getTWorkflowTransitionsRelatedByStationFrom(con);
        if (vTWorkflowTransitionsRelatedByStationFrom != null)
        {
            for (int i = 0; i < vTWorkflowTransitionsRelatedByStationFrom.size(); i++)
            {
                TWorkflowTransition obj =  vTWorkflowTransitionsRelatedByStationFrom.get(i);
                copyObj.addTWorkflowTransitionRelatedByStationFrom(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowTransitionsRelatedByStationFrom = null;
        }


        List<TWorkflowTransition> vTWorkflowTransitionsRelatedByStationTo = getTWorkflowTransitionsRelatedByStationTo(con);
        if (vTWorkflowTransitionsRelatedByStationTo != null)
        {
            for (int i = 0; i < vTWorkflowTransitionsRelatedByStationTo.size(); i++)
            {
                TWorkflowTransition obj =  vTWorkflowTransitionsRelatedByStationTo.get(i);
                copyObj.addTWorkflowTransitionRelatedByStationTo(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowTransitionsRelatedByStationTo = null;
        }


        List<TWorkflowActivity> vTWorkflowActivitysRelatedByStationEntryActivity = getTWorkflowActivitysRelatedByStationEntryActivity(con);
        if (vTWorkflowActivitysRelatedByStationEntryActivity != null)
        {
            for (int i = 0; i < vTWorkflowActivitysRelatedByStationEntryActivity.size(); i++)
            {
                TWorkflowActivity obj =  vTWorkflowActivitysRelatedByStationEntryActivity.get(i);
                copyObj.addTWorkflowActivityRelatedByStationEntryActivity(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowActivitysRelatedByStationEntryActivity = null;
        }


        List<TWorkflowActivity> vTWorkflowActivitysRelatedByStationExitActivity = getTWorkflowActivitysRelatedByStationExitActivity(con);
        if (vTWorkflowActivitysRelatedByStationExitActivity != null)
        {
            for (int i = 0; i < vTWorkflowActivitysRelatedByStationExitActivity.size(); i++)
            {
                TWorkflowActivity obj =  vTWorkflowActivitysRelatedByStationExitActivity.get(i);
                copyObj.addTWorkflowActivityRelatedByStationExitActivity(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowActivitysRelatedByStationExitActivity = null;
        }


        List<TWorkflowActivity> vTWorkflowActivitysRelatedByStationDoActivity = getTWorkflowActivitysRelatedByStationDoActivity(con);
        if (vTWorkflowActivitysRelatedByStationDoActivity != null)
        {
            for (int i = 0; i < vTWorkflowActivitysRelatedByStationDoActivity.size(); i++)
            {
                TWorkflowActivity obj =  vTWorkflowActivitysRelatedByStationDoActivity.get(i);
                copyObj.addTWorkflowActivityRelatedByStationDoActivity(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowActivitysRelatedByStationDoActivity = null;
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
    public TWorkflowStationPeer getPeer()
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
        return TWorkflowStationPeer.getTableMap();
    }

  
    /**
     * Creates a TWorkflowStationBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TWorkflowStationBean with the contents of this object
     */
    public TWorkflowStationBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TWorkflowStationBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TWorkflowStationBean with the contents of this object
     */
    public TWorkflowStationBean getBean(IdentityMap createdBeans)
    {
        TWorkflowStationBean result = (TWorkflowStationBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TWorkflowStationBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setStatus(getStatus());
        result.setWorkflow(getWorkflow());
        result.setNodeX(getNodeX());
        result.setNodeY(getNodeY());
        result.setStationType(getStationType());
        result.setUuid(getUuid());



        if (collTWorkflowTransitionsRelatedByStationFrom != null)
        {
            List<TWorkflowTransitionBean> relatedBeans = new ArrayList<TWorkflowTransitionBean>(collTWorkflowTransitionsRelatedByStationFrom.size());
            for (Iterator<TWorkflowTransition> collTWorkflowTransitionsRelatedByStationFromIt = collTWorkflowTransitionsRelatedByStationFrom.iterator(); collTWorkflowTransitionsRelatedByStationFromIt.hasNext(); )
            {
                TWorkflowTransition related = (TWorkflowTransition) collTWorkflowTransitionsRelatedByStationFromIt.next();
                TWorkflowTransitionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowTransitionBeansRelatedByStationFrom(relatedBeans);
        }


        if (collTWorkflowTransitionsRelatedByStationTo != null)
        {
            List<TWorkflowTransitionBean> relatedBeans = new ArrayList<TWorkflowTransitionBean>(collTWorkflowTransitionsRelatedByStationTo.size());
            for (Iterator<TWorkflowTransition> collTWorkflowTransitionsRelatedByStationToIt = collTWorkflowTransitionsRelatedByStationTo.iterator(); collTWorkflowTransitionsRelatedByStationToIt.hasNext(); )
            {
                TWorkflowTransition related = (TWorkflowTransition) collTWorkflowTransitionsRelatedByStationToIt.next();
                TWorkflowTransitionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowTransitionBeansRelatedByStationTo(relatedBeans);
        }


        if (collTWorkflowActivitysRelatedByStationEntryActivity != null)
        {
            List<TWorkflowActivityBean> relatedBeans = new ArrayList<TWorkflowActivityBean>(collTWorkflowActivitysRelatedByStationEntryActivity.size());
            for (Iterator<TWorkflowActivity> collTWorkflowActivitysRelatedByStationEntryActivityIt = collTWorkflowActivitysRelatedByStationEntryActivity.iterator(); collTWorkflowActivitysRelatedByStationEntryActivityIt.hasNext(); )
            {
                TWorkflowActivity related = (TWorkflowActivity) collTWorkflowActivitysRelatedByStationEntryActivityIt.next();
                TWorkflowActivityBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowActivityBeansRelatedByStationEntryActivity(relatedBeans);
        }


        if (collTWorkflowActivitysRelatedByStationExitActivity != null)
        {
            List<TWorkflowActivityBean> relatedBeans = new ArrayList<TWorkflowActivityBean>(collTWorkflowActivitysRelatedByStationExitActivity.size());
            for (Iterator<TWorkflowActivity> collTWorkflowActivitysRelatedByStationExitActivityIt = collTWorkflowActivitysRelatedByStationExitActivity.iterator(); collTWorkflowActivitysRelatedByStationExitActivityIt.hasNext(); )
            {
                TWorkflowActivity related = (TWorkflowActivity) collTWorkflowActivitysRelatedByStationExitActivityIt.next();
                TWorkflowActivityBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowActivityBeansRelatedByStationExitActivity(relatedBeans);
        }


        if (collTWorkflowActivitysRelatedByStationDoActivity != null)
        {
            List<TWorkflowActivityBean> relatedBeans = new ArrayList<TWorkflowActivityBean>(collTWorkflowActivitysRelatedByStationDoActivity.size());
            for (Iterator<TWorkflowActivity> collTWorkflowActivitysRelatedByStationDoActivityIt = collTWorkflowActivitysRelatedByStationDoActivity.iterator(); collTWorkflowActivitysRelatedByStationDoActivityIt.hasNext(); )
            {
                TWorkflowActivity related = (TWorkflowActivity) collTWorkflowActivitysRelatedByStationDoActivityIt.next();
                TWorkflowActivityBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowActivityBeansRelatedByStationDoActivity(relatedBeans);
        }




        if (aTState != null)
        {
            TStateBean relatedBean = aTState.getBean(createdBeans);
            result.setTStateBean(relatedBean);
        }



        if (aTWorkflowDef != null)
        {
            TWorkflowDefBean relatedBean = aTWorkflowDef.getBean(createdBeans);
            result.setTWorkflowDefBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TWorkflowStation with the contents
     * of a TWorkflowStationBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TWorkflowStationBean which contents are used to create
     *        the resulting class
     * @return an instance of TWorkflowStation with the contents of bean
     */
    public static TWorkflowStation createTWorkflowStation(TWorkflowStationBean bean)
        throws TorqueException
    {
        return createTWorkflowStation(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TWorkflowStation with the contents
     * of a TWorkflowStationBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TWorkflowStationBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TWorkflowStation with the contents of bean
     */

    public static TWorkflowStation createTWorkflowStation(TWorkflowStationBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TWorkflowStation result = (TWorkflowStation) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TWorkflowStation();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setStatus(bean.getStatus());
        result.setWorkflow(bean.getWorkflow());
        result.setNodeX(bean.getNodeX());
        result.setNodeY(bean.getNodeY());
        result.setStationType(bean.getStationType());
        result.setUuid(bean.getUuid());



        {
            List<TWorkflowTransitionBean> relatedBeans = bean.getTWorkflowTransitionBeansRelatedByStationFrom();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowTransitionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowTransitionBean relatedBean =  relatedBeansIt.next();
                    TWorkflowTransition related = TWorkflowTransition.createTWorkflowTransition(relatedBean, createdObjects);
                    result.addTWorkflowTransitionRelatedByStationFromFromBean(related);
                }
            }
        }


        {
            List<TWorkflowTransitionBean> relatedBeans = bean.getTWorkflowTransitionBeansRelatedByStationTo();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowTransitionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowTransitionBean relatedBean =  relatedBeansIt.next();
                    TWorkflowTransition related = TWorkflowTransition.createTWorkflowTransition(relatedBean, createdObjects);
                    result.addTWorkflowTransitionRelatedByStationToFromBean(related);
                }
            }
        }


        {
            List<TWorkflowActivityBean> relatedBeans = bean.getTWorkflowActivityBeansRelatedByStationEntryActivity();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowActivityBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowActivityBean relatedBean =  relatedBeansIt.next();
                    TWorkflowActivity related = TWorkflowActivity.createTWorkflowActivity(relatedBean, createdObjects);
                    result.addTWorkflowActivityRelatedByStationEntryActivityFromBean(related);
                }
            }
        }


        {
            List<TWorkflowActivityBean> relatedBeans = bean.getTWorkflowActivityBeansRelatedByStationExitActivity();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowActivityBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowActivityBean relatedBean =  relatedBeansIt.next();
                    TWorkflowActivity related = TWorkflowActivity.createTWorkflowActivity(relatedBean, createdObjects);
                    result.addTWorkflowActivityRelatedByStationExitActivityFromBean(related);
                }
            }
        }


        {
            List<TWorkflowActivityBean> relatedBeans = bean.getTWorkflowActivityBeansRelatedByStationDoActivity();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowActivityBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowActivityBean relatedBean =  relatedBeansIt.next();
                    TWorkflowActivity related = TWorkflowActivity.createTWorkflowActivity(relatedBean, createdObjects);
                    result.addTWorkflowActivityRelatedByStationDoActivityFromBean(related);
                }
            }
        }




        {
            TStateBean relatedBean = bean.getTStateBean();
            if (relatedBean != null)
            {
                TState relatedObject = TState.createTState(relatedBean, createdObjects);
                result.setTState(relatedObject);
            }
        }



        {
            TWorkflowDefBean relatedBean = bean.getTWorkflowDefBean();
            if (relatedBean != null)
            {
                TWorkflowDef relatedObject = TWorkflowDef.createTWorkflowDef(relatedBean, createdObjects);
                result.setTWorkflowDef(relatedObject);
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
    protected void addTWorkflowTransitionRelatedByStationFromFromBean(TWorkflowTransition toAdd)
    {
        initTWorkflowTransitionsRelatedByStationFrom();
        collTWorkflowTransitionsRelatedByStationFrom.add(toAdd);
    }


    /**
     * Method called to associate a TWorkflowTransition object to this object.
     * through the TWorkflowTransition foreign key attribute
     *
     * @param toAdd TWorkflowTransition
     */
    protected void addTWorkflowTransitionRelatedByStationToFromBean(TWorkflowTransition toAdd)
    {
        initTWorkflowTransitionsRelatedByStationTo();
        collTWorkflowTransitionsRelatedByStationTo.add(toAdd);
    }


    /**
     * Method called to associate a TWorkflowActivity object to this object.
     * through the TWorkflowActivity foreign key attribute
     *
     * @param toAdd TWorkflowActivity
     */
    protected void addTWorkflowActivityRelatedByStationEntryActivityFromBean(TWorkflowActivity toAdd)
    {
        initTWorkflowActivitysRelatedByStationEntryActivity();
        collTWorkflowActivitysRelatedByStationEntryActivity.add(toAdd);
    }


    /**
     * Method called to associate a TWorkflowActivity object to this object.
     * through the TWorkflowActivity foreign key attribute
     *
     * @param toAdd TWorkflowActivity
     */
    protected void addTWorkflowActivityRelatedByStationExitActivityFromBean(TWorkflowActivity toAdd)
    {
        initTWorkflowActivitysRelatedByStationExitActivity();
        collTWorkflowActivitysRelatedByStationExitActivity.add(toAdd);
    }


    /**
     * Method called to associate a TWorkflowActivity object to this object.
     * through the TWorkflowActivity foreign key attribute
     *
     * @param toAdd TWorkflowActivity
     */
    protected void addTWorkflowActivityRelatedByStationDoActivityFromBean(TWorkflowActivity toAdd)
    {
        initTWorkflowActivitysRelatedByStationDoActivity();
        collTWorkflowActivitysRelatedByStationDoActivity.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TWorkflowStation:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Status = ")
           .append(getStatus())
           .append("\n");
        str.append("Workflow = ")
           .append(getWorkflow())
           .append("\n");
        str.append("NodeX = ")
           .append(getNodeX())
           .append("\n");
        str.append("NodeY = ")
           .append(getNodeY())
           .append("\n");
        str.append("StationType = ")
           .append(getStationType())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
