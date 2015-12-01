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



import com.aurel.track.persist.TWorkflowStation;
import com.aurel.track.persist.TWorkflowStationPeer;
import com.aurel.track.persist.TWorkflowStation;
import com.aurel.track.persist.TWorkflowStationPeer;
import com.aurel.track.persist.TAction;
import com.aurel.track.persist.TActionPeer;
import com.aurel.track.persist.TWorkflowDef;
import com.aurel.track.persist.TWorkflowDefPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TWorkflowTransitionBean;
import com.aurel.track.beans.TWorkflowStationBean;
import com.aurel.track.beans.TWorkflowStationBean;
import com.aurel.track.beans.TActionBean;
import com.aurel.track.beans.TWorkflowDefBean;

import com.aurel.track.beans.TWorkflowActivityBean;
import com.aurel.track.beans.TWorkflowGuardBean;
import com.aurel.track.beans.TItemTransitionBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TWorkflowTransition
 */
public abstract class BaseTWorkflowTransition extends TpBaseObject
{
    /** The Peer class */
    private static final TWorkflowTransitionPeer peer =
        new TWorkflowTransitionPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the stationFrom field */
    private Integer stationFrom;

    /** The value for the stationTo field */
    private Integer stationTo;

    /** The value for the actionKey field */
    private Integer actionKey;

    /** The value for the workflow field */
    private Integer workflow;

    /** The value for the textPositionX field */
    private Integer textPositionX;

    /** The value for the textPositionY field */
    private Integer textPositionY;

    /** The value for the transitionType field */
    private Integer transitionType;

    /** The value for the controlPoints field */
    private String controlPoints;

    /** The value for the elapsedTime field */
    private Integer elapsedTime;

    /** The value for the timeUnit field */
    private Integer timeUnit;

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



        // update associated TWorkflowActivity
        if (collTWorkflowActivitys != null)
        {
            for (int i = 0; i < collTWorkflowActivitys.size(); i++)
            {
                ((TWorkflowActivity) collTWorkflowActivitys.get(i))
                        .setTransitionActivity(v);
            }
        }

        // update associated TWorkflowGuard
        if (collTWorkflowGuards != null)
        {
            for (int i = 0; i < collTWorkflowGuards.size(); i++)
            {
                ((TWorkflowGuard) collTWorkflowGuards.get(i))
                        .setWorkflowTransition(v);
            }
        }

        // update associated TItemTransition
        if (collTItemTransitions != null)
        {
            for (int i = 0; i < collTItemTransitions.size(); i++)
            {
                ((TItemTransition) collTItemTransitions.get(i))
                        .setTransition(v);
            }
        }
    }

    /**
     * Get the StationFrom
     *
     * @return Integer
     */
    public Integer getStationFrom()
    {
        return stationFrom;
    }


    /**
     * Set the value of StationFrom
     *
     * @param v new value
     */
    public void setStationFrom(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.stationFrom, v))
        {
            this.stationFrom = v;
            setModified(true);
        }


        if (aTWorkflowStationRelatedByStationFrom != null && !ObjectUtils.equals(aTWorkflowStationRelatedByStationFrom.getObjectID(), v))
        {
            aTWorkflowStationRelatedByStationFrom = null;
        }

    }

    /**
     * Get the StationTo
     *
     * @return Integer
     */
    public Integer getStationTo()
    {
        return stationTo;
    }


    /**
     * Set the value of StationTo
     *
     * @param v new value
     */
    public void setStationTo(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.stationTo, v))
        {
            this.stationTo = v;
            setModified(true);
        }


        if (aTWorkflowStationRelatedByStationTo != null && !ObjectUtils.equals(aTWorkflowStationRelatedByStationTo.getObjectID(), v))
        {
            aTWorkflowStationRelatedByStationTo = null;
        }

    }

    /**
     * Get the ActionKey
     *
     * @return Integer
     */
    public Integer getActionKey()
    {
        return actionKey;
    }


    /**
     * Set the value of ActionKey
     *
     * @param v new value
     */
    public void setActionKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.actionKey, v))
        {
            this.actionKey = v;
            setModified(true);
        }


        if (aTAction != null && !ObjectUtils.equals(aTAction.getObjectID(), v))
        {
            aTAction = null;
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
     * Get the TextPositionX
     *
     * @return Integer
     */
    public Integer getTextPositionX()
    {
        return textPositionX;
    }


    /**
     * Set the value of TextPositionX
     *
     * @param v new value
     */
    public void setTextPositionX(Integer v) 
    {

        if (!ObjectUtils.equals(this.textPositionX, v))
        {
            this.textPositionX = v;
            setModified(true);
        }


    }

    /**
     * Get the TextPositionY
     *
     * @return Integer
     */
    public Integer getTextPositionY()
    {
        return textPositionY;
    }


    /**
     * Set the value of TextPositionY
     *
     * @param v new value
     */
    public void setTextPositionY(Integer v) 
    {

        if (!ObjectUtils.equals(this.textPositionY, v))
        {
            this.textPositionY = v;
            setModified(true);
        }


    }

    /**
     * Get the TransitionType
     *
     * @return Integer
     */
    public Integer getTransitionType()
    {
        return transitionType;
    }


    /**
     * Set the value of TransitionType
     *
     * @param v new value
     */
    public void setTransitionType(Integer v) 
    {

        if (!ObjectUtils.equals(this.transitionType, v))
        {
            this.transitionType = v;
            setModified(true);
        }


    }

    /**
     * Get the ControlPoints
     *
     * @return String
     */
    public String getControlPoints()
    {
        return controlPoints;
    }


    /**
     * Set the value of ControlPoints
     *
     * @param v new value
     */
    public void setControlPoints(String v) 
    {

        if (!ObjectUtils.equals(this.controlPoints, v))
        {
            this.controlPoints = v;
            setModified(true);
        }


    }

    /**
     * Get the ElapsedTime
     *
     * @return Integer
     */
    public Integer getElapsedTime()
    {
        return elapsedTime;
    }


    /**
     * Set the value of ElapsedTime
     *
     * @param v new value
     */
    public void setElapsedTime(Integer v) 
    {

        if (!ObjectUtils.equals(this.elapsedTime, v))
        {
            this.elapsedTime = v;
            setModified(true);
        }


    }

    /**
     * Get the TimeUnit
     *
     * @return Integer
     */
    public Integer getTimeUnit()
    {
        return timeUnit;
    }


    /**
     * Set the value of TimeUnit
     *
     * @param v new value
     */
    public void setTimeUnit(Integer v) 
    {

        if (!ObjectUtils.equals(this.timeUnit, v))
        {
            this.timeUnit = v;
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

    



    private TWorkflowStation aTWorkflowStationRelatedByStationFrom;

    /**
     * Declares an association between this object and a TWorkflowStation object
     *
     * @param v TWorkflowStation
     * @throws TorqueException
     */
    public void setTWorkflowStationRelatedByStationFrom(TWorkflowStation v) throws TorqueException
    {
        if (v == null)
        {
            setStationFrom((Integer) null);
        }
        else
        {
            setStationFrom(v.getObjectID());
        }
        aTWorkflowStationRelatedByStationFrom = v;
    }


    /**
     * Returns the associated TWorkflowStation object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkflowStation object
     * @throws TorqueException
     */
    public TWorkflowStation getTWorkflowStationRelatedByStationFrom()
        throws TorqueException
    {
        if (aTWorkflowStationRelatedByStationFrom == null && (!ObjectUtils.equals(this.stationFrom, null)))
        {
            aTWorkflowStationRelatedByStationFrom = TWorkflowStationPeer.retrieveByPK(SimpleKey.keyFor(this.stationFrom));
        }
        return aTWorkflowStationRelatedByStationFrom;
    }

    /**
     * Return the associated TWorkflowStation object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkflowStation object
     * @throws TorqueException
     */
    public TWorkflowStation getTWorkflowStationRelatedByStationFrom(Connection connection)
        throws TorqueException
    {
        if (aTWorkflowStationRelatedByStationFrom == null && (!ObjectUtils.equals(this.stationFrom, null)))
        {
            aTWorkflowStationRelatedByStationFrom = TWorkflowStationPeer.retrieveByPK(SimpleKey.keyFor(this.stationFrom), connection);
        }
        return aTWorkflowStationRelatedByStationFrom;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkflowStationRelatedByStationFromKey(ObjectKey key) throws TorqueException
    {

        setStationFrom(new Integer(((NumberKey) key).intValue()));
    }




    private TWorkflowStation aTWorkflowStationRelatedByStationTo;

    /**
     * Declares an association between this object and a TWorkflowStation object
     *
     * @param v TWorkflowStation
     * @throws TorqueException
     */
    public void setTWorkflowStationRelatedByStationTo(TWorkflowStation v) throws TorqueException
    {
        if (v == null)
        {
            setStationTo((Integer) null);
        }
        else
        {
            setStationTo(v.getObjectID());
        }
        aTWorkflowStationRelatedByStationTo = v;
    }


    /**
     * Returns the associated TWorkflowStation object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkflowStation object
     * @throws TorqueException
     */
    public TWorkflowStation getTWorkflowStationRelatedByStationTo()
        throws TorqueException
    {
        if (aTWorkflowStationRelatedByStationTo == null && (!ObjectUtils.equals(this.stationTo, null)))
        {
            aTWorkflowStationRelatedByStationTo = TWorkflowStationPeer.retrieveByPK(SimpleKey.keyFor(this.stationTo));
        }
        return aTWorkflowStationRelatedByStationTo;
    }

    /**
     * Return the associated TWorkflowStation object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkflowStation object
     * @throws TorqueException
     */
    public TWorkflowStation getTWorkflowStationRelatedByStationTo(Connection connection)
        throws TorqueException
    {
        if (aTWorkflowStationRelatedByStationTo == null && (!ObjectUtils.equals(this.stationTo, null)))
        {
            aTWorkflowStationRelatedByStationTo = TWorkflowStationPeer.retrieveByPK(SimpleKey.keyFor(this.stationTo), connection);
        }
        return aTWorkflowStationRelatedByStationTo;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkflowStationRelatedByStationToKey(ObjectKey key) throws TorqueException
    {

        setStationTo(new Integer(((NumberKey) key).intValue()));
    }




    private TAction aTAction;

    /**
     * Declares an association between this object and a TAction object
     *
     * @param v TAction
     * @throws TorqueException
     */
    public void setTAction(TAction v) throws TorqueException
    {
        if (v == null)
        {
            setActionKey((Integer) null);
        }
        else
        {
            setActionKey(v.getObjectID());
        }
        aTAction = v;
    }


    /**
     * Returns the associated TAction object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TAction object
     * @throws TorqueException
     */
    public TAction getTAction()
        throws TorqueException
    {
        if (aTAction == null && (!ObjectUtils.equals(this.actionKey, null)))
        {
            aTAction = TActionPeer.retrieveByPK(SimpleKey.keyFor(this.actionKey));
        }
        return aTAction;
    }

    /**
     * Return the associated TAction object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TAction object
     * @throws TorqueException
     */
    public TAction getTAction(Connection connection)
        throws TorqueException
    {
        if (aTAction == null && (!ObjectUtils.equals(this.actionKey, null)))
        {
            aTAction = TActionPeer.retrieveByPK(SimpleKey.keyFor(this.actionKey), connection);
        }
        return aTAction;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTActionKey(ObjectKey key) throws TorqueException
    {

        setActionKey(new Integer(((NumberKey) key).intValue()));
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
     * Collection to store aggregation of collTWorkflowActivitys
     */
    protected List<TWorkflowActivity> collTWorkflowActivitys;

    /**
     * Temporary storage of collTWorkflowActivitys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowActivitys()
    {
        if (collTWorkflowActivitys == null)
        {
            collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
        }
    }


    /**
     * Method called to associate a TWorkflowActivity object to this object
     * through the TWorkflowActivity foreign key attribute
     *
     * @param l TWorkflowActivity
     * @throws TorqueException
     */
    public void addTWorkflowActivity(TWorkflowActivity l) throws TorqueException
    {
        getTWorkflowActivitys().add(l);
        l.setTWorkflowTransition((TWorkflowTransition) this);
    }

    /**
     * Method called to associate a TWorkflowActivity object to this object
     * through the TWorkflowActivity foreign key attribute using connection.
     *
     * @param l TWorkflowActivity
     * @throws TorqueException
     */
    public void addTWorkflowActivity(TWorkflowActivity l, Connection con) throws TorqueException
    {
        getTWorkflowActivitys(con).add(l);
        l.setTWorkflowTransition((TWorkflowTransition) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowActivitys
     */
    private Criteria lastTWorkflowActivitysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowActivitys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowActivity> getTWorkflowActivitys()
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            collTWorkflowActivitys = getTWorkflowActivitys(new Criteria(10));
        }
        return collTWorkflowActivitys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     * If this TWorkflowTransition is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowActivity> getTWorkflowActivitys(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID() );
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelect(criteria);
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
                criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                if (!lastTWorkflowActivitysCriteria.equals(criteria))
                {
                    collTWorkflowActivitys = TWorkflowActivityPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowActivitys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowActivity> getTWorkflowActivitys(Connection con) throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            collTWorkflowActivitys = getTWorkflowActivitys(new Criteria(10), con);
        }
        return collTWorkflowActivitys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     * If this TWorkflowTransition is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowActivity> getTWorkflowActivitys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                 criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                 collTWorkflowActivitys = TWorkflowActivityPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                 if (!lastTWorkflowActivitysCriteria.equals(criteria))
                 {
                     collTWorkflowActivitys = TWorkflowActivityPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowActivitysCriteria = criteria;

         return collTWorkflowActivitys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTWorkflowTransition(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTWorkflowStationRelatedByStationEntryActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationEntryActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationEntryActivity(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTWorkflowStationRelatedByStationExitActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationExitActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationExitActivity(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTWorkflowStationRelatedByStationDoActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationDoActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationDoActivity(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTScripts(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTPersonRelatedByNewMan(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTPersonRelatedByNewResp(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTSLA(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowActivity> getTWorkflowActivitysJoinTScreen(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowActivitys == null)
        {
            if (isNew())
            {
               collTWorkflowActivitys = new ArrayList<TWorkflowActivity>();
            }
            else
            {
                criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.TRANSITIONACTIVITY, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }





    /**
     * Collection to store aggregation of collTWorkflowGuards
     */
    protected List<TWorkflowGuard> collTWorkflowGuards;

    /**
     * Temporary storage of collTWorkflowGuards to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkflowGuards()
    {
        if (collTWorkflowGuards == null)
        {
            collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
        }
    }


    /**
     * Method called to associate a TWorkflowGuard object to this object
     * through the TWorkflowGuard foreign key attribute
     *
     * @param l TWorkflowGuard
     * @throws TorqueException
     */
    public void addTWorkflowGuard(TWorkflowGuard l) throws TorqueException
    {
        getTWorkflowGuards().add(l);
        l.setTWorkflowTransition((TWorkflowTransition) this);
    }

    /**
     * Method called to associate a TWorkflowGuard object to this object
     * through the TWorkflowGuard foreign key attribute using connection.
     *
     * @param l TWorkflowGuard
     * @throws TorqueException
     */
    public void addTWorkflowGuard(TWorkflowGuard l, Connection con) throws TorqueException
    {
        getTWorkflowGuards(con).add(l);
        l.setTWorkflowTransition((TWorkflowTransition) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkflowGuards
     */
    private Criteria lastTWorkflowGuardsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowGuards(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkflowGuard> getTWorkflowGuards()
        throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            collTWorkflowGuards = getTWorkflowGuards(new Criteria(10));
        }
        return collTWorkflowGuards;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     * If this TWorkflowTransition is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkflowGuard> getTWorkflowGuards(Criteria criteria) throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID() );
                collTWorkflowGuards = TWorkflowGuardPeer.doSelect(criteria);
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
                criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID());
                if (!lastTWorkflowGuardsCriteria.equals(criteria))
                {
                    collTWorkflowGuards = TWorkflowGuardPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkflowGuardsCriteria = criteria;

        return collTWorkflowGuards;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkflowGuards(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowGuard> getTWorkflowGuards(Connection con) throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            collTWorkflowGuards = getTWorkflowGuards(new Criteria(10), con);
        }
        return collTWorkflowGuards;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     * If this TWorkflowTransition is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkflowGuard> getTWorkflowGuards(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                 criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID());
                 collTWorkflowGuards = TWorkflowGuardPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID());
                 if (!lastTWorkflowGuardsCriteria.equals(criteria))
                 {
                     collTWorkflowGuards = TWorkflowGuardPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkflowGuardsCriteria = criteria;

         return collTWorkflowGuards;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowGuard> getTWorkflowGuardsJoinTWorkflowTransition(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID());
            if (!lastTWorkflowGuardsCriteria.equals(criteria))
            {
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        lastTWorkflowGuardsCriteria = criteria;

        return collTWorkflowGuards;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowGuard> getTWorkflowGuardsJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID());
            if (!lastTWorkflowGuardsCriteria.equals(criteria))
            {
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTRole(criteria);
            }
        }
        lastTWorkflowGuardsCriteria = criteria;

        return collTWorkflowGuards;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowGuard> getTWorkflowGuardsJoinTScripts(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID());
            if (!lastTWorkflowGuardsCriteria.equals(criteria))
            {
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTScripts(criteria);
            }
        }
        lastTWorkflowGuardsCriteria = criteria;

        return collTWorkflowGuards;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TWorkflowGuard> getTWorkflowGuardsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkflowGuards == null)
        {
            if (isNew())
            {
               collTWorkflowGuards = new ArrayList<TWorkflowGuard>();
            }
            else
            {
                criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.WORKFLOWTRANSITION, getObjectID());
            if (!lastTWorkflowGuardsCriteria.equals(criteria))
            {
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTWorkflowGuardsCriteria = criteria;

        return collTWorkflowGuards;
    }





    /**
     * Collection to store aggregation of collTItemTransitions
     */
    protected List<TItemTransition> collTItemTransitions;

    /**
     * Temporary storage of collTItemTransitions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTItemTransitions()
    {
        if (collTItemTransitions == null)
        {
            collTItemTransitions = new ArrayList<TItemTransition>();
        }
    }


    /**
     * Method called to associate a TItemTransition object to this object
     * through the TItemTransition foreign key attribute
     *
     * @param l TItemTransition
     * @throws TorqueException
     */
    public void addTItemTransition(TItemTransition l) throws TorqueException
    {
        getTItemTransitions().add(l);
        l.setTWorkflowTransition((TWorkflowTransition) this);
    }

    /**
     * Method called to associate a TItemTransition object to this object
     * through the TItemTransition foreign key attribute using connection.
     *
     * @param l TItemTransition
     * @throws TorqueException
     */
    public void addTItemTransition(TItemTransition l, Connection con) throws TorqueException
    {
        getTItemTransitions(con).add(l);
        l.setTWorkflowTransition((TWorkflowTransition) this);
    }

    /**
     * The criteria used to select the current contents of collTItemTransitions
     */
    private Criteria lastTItemTransitionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTItemTransitions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TItemTransition> getTItemTransitions()
        throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            collTItemTransitions = getTItemTransitions(new Criteria(10));
        }
        return collTItemTransitions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition has previously
     * been saved, it will retrieve related TItemTransitions from storage.
     * If this TWorkflowTransition is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TItemTransition> getTItemTransitions(Criteria criteria) throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            if (isNew())
            {
               collTItemTransitions = new ArrayList<TItemTransition>();
            }
            else
            {
                criteria.add(TItemTransitionPeer.TRANSITION, getObjectID() );
                collTItemTransitions = TItemTransitionPeer.doSelect(criteria);
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
                criteria.add(TItemTransitionPeer.TRANSITION, getObjectID());
                if (!lastTItemTransitionsCriteria.equals(criteria))
                {
                    collTItemTransitions = TItemTransitionPeer.doSelect(criteria);
                }
            }
        }
        lastTItemTransitionsCriteria = criteria;

        return collTItemTransitions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTItemTransitions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TItemTransition> getTItemTransitions(Connection con) throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            collTItemTransitions = getTItemTransitions(new Criteria(10), con);
        }
        return collTItemTransitions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition has previously
     * been saved, it will retrieve related TItemTransitions from storage.
     * If this TWorkflowTransition is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TItemTransition> getTItemTransitions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            if (isNew())
            {
               collTItemTransitions = new ArrayList<TItemTransition>();
            }
            else
            {
                 criteria.add(TItemTransitionPeer.TRANSITION, getObjectID());
                 collTItemTransitions = TItemTransitionPeer.doSelect(criteria, con);
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
                 criteria.add(TItemTransitionPeer.TRANSITION, getObjectID());
                 if (!lastTItemTransitionsCriteria.equals(criteria))
                 {
                     collTItemTransitions = TItemTransitionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTItemTransitionsCriteria = criteria;

         return collTItemTransitions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TItemTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TItemTransition> getTItemTransitionsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            if (isNew())
            {
               collTItemTransitions = new ArrayList<TItemTransition>();
            }
            else
            {
                criteria.add(TItemTransitionPeer.TRANSITION, getObjectID());
                collTItemTransitions = TItemTransitionPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TItemTransitionPeer.TRANSITION, getObjectID());
            if (!lastTItemTransitionsCriteria.equals(criteria))
            {
                collTItemTransitions = TItemTransitionPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTItemTransitionsCriteria = criteria;

        return collTItemTransitions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowTransition is new, it will return
     * an empty collection; or if this TWorkflowTransition has previously
     * been saved, it will retrieve related TItemTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowTransition.
     */
    protected List<TItemTransition> getTItemTransitionsJoinTWorkflowTransition(Criteria criteria)
        throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            if (isNew())
            {
               collTItemTransitions = new ArrayList<TItemTransition>();
            }
            else
            {
                criteria.add(TItemTransitionPeer.TRANSITION, getObjectID());
                collTItemTransitions = TItemTransitionPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TItemTransitionPeer.TRANSITION, getObjectID());
            if (!lastTItemTransitionsCriteria.equals(criteria))
            {
                collTItemTransitions = TItemTransitionPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        lastTItemTransitionsCriteria = criteria;

        return collTItemTransitions;
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
            fieldNames.add("StationFrom");
            fieldNames.add("StationTo");
            fieldNames.add("ActionKey");
            fieldNames.add("Workflow");
            fieldNames.add("TextPositionX");
            fieldNames.add("TextPositionY");
            fieldNames.add("TransitionType");
            fieldNames.add("ControlPoints");
            fieldNames.add("ElapsedTime");
            fieldNames.add("TimeUnit");
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
        if (name.equals("StationFrom"))
        {
            return getStationFrom();
        }
        if (name.equals("StationTo"))
        {
            return getStationTo();
        }
        if (name.equals("ActionKey"))
        {
            return getActionKey();
        }
        if (name.equals("Workflow"))
        {
            return getWorkflow();
        }
        if (name.equals("TextPositionX"))
        {
            return getTextPositionX();
        }
        if (name.equals("TextPositionY"))
        {
            return getTextPositionY();
        }
        if (name.equals("TransitionType"))
        {
            return getTransitionType();
        }
        if (name.equals("ControlPoints"))
        {
            return getControlPoints();
        }
        if (name.equals("ElapsedTime"))
        {
            return getElapsedTime();
        }
        if (name.equals("TimeUnit"))
        {
            return getTimeUnit();
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
        if (name.equals("StationFrom"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStationFrom((Integer) value);
            return true;
        }
        if (name.equals("StationTo"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStationTo((Integer) value);
            return true;
        }
        if (name.equals("ActionKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setActionKey((Integer) value);
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
        if (name.equals("TextPositionX"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTextPositionX((Integer) value);
            return true;
        }
        if (name.equals("TextPositionY"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTextPositionY((Integer) value);
            return true;
        }
        if (name.equals("TransitionType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTransitionType((Integer) value);
            return true;
        }
        if (name.equals("ControlPoints"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setControlPoints((String) value);
            return true;
        }
        if (name.equals("ElapsedTime"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setElapsedTime((Integer) value);
            return true;
        }
        if (name.equals("TimeUnit"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTimeUnit((Integer) value);
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
        if (name.equals(TWorkflowTransitionPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TWorkflowTransitionPeer.STATIONFROM))
        {
            return getStationFrom();
        }
        if (name.equals(TWorkflowTransitionPeer.STATIONTO))
        {
            return getStationTo();
        }
        if (name.equals(TWorkflowTransitionPeer.ACTIONKEY))
        {
            return getActionKey();
        }
        if (name.equals(TWorkflowTransitionPeer.WORKFLOW))
        {
            return getWorkflow();
        }
        if (name.equals(TWorkflowTransitionPeer.TEXTPOSITIONX))
        {
            return getTextPositionX();
        }
        if (name.equals(TWorkflowTransitionPeer.TEXTPOSITIONY))
        {
            return getTextPositionY();
        }
        if (name.equals(TWorkflowTransitionPeer.TRANSITIONTYPE))
        {
            return getTransitionType();
        }
        if (name.equals(TWorkflowTransitionPeer.CONTROLPOINTS))
        {
            return getControlPoints();
        }
        if (name.equals(TWorkflowTransitionPeer.ELAPSEDTIME))
        {
            return getElapsedTime();
        }
        if (name.equals(TWorkflowTransitionPeer.TIMEUNIT))
        {
            return getTimeUnit();
        }
        if (name.equals(TWorkflowTransitionPeer.TPUUID))
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
      if (TWorkflowTransitionPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TWorkflowTransitionPeer.STATIONFROM.equals(name))
        {
            return setByName("StationFrom", value);
        }
      if (TWorkflowTransitionPeer.STATIONTO.equals(name))
        {
            return setByName("StationTo", value);
        }
      if (TWorkflowTransitionPeer.ACTIONKEY.equals(name))
        {
            return setByName("ActionKey", value);
        }
      if (TWorkflowTransitionPeer.WORKFLOW.equals(name))
        {
            return setByName("Workflow", value);
        }
      if (TWorkflowTransitionPeer.TEXTPOSITIONX.equals(name))
        {
            return setByName("TextPositionX", value);
        }
      if (TWorkflowTransitionPeer.TEXTPOSITIONY.equals(name))
        {
            return setByName("TextPositionY", value);
        }
      if (TWorkflowTransitionPeer.TRANSITIONTYPE.equals(name))
        {
            return setByName("TransitionType", value);
        }
      if (TWorkflowTransitionPeer.CONTROLPOINTS.equals(name))
        {
            return setByName("ControlPoints", value);
        }
      if (TWorkflowTransitionPeer.ELAPSEDTIME.equals(name))
        {
            return setByName("ElapsedTime", value);
        }
      if (TWorkflowTransitionPeer.TIMEUNIT.equals(name))
        {
            return setByName("TimeUnit", value);
        }
      if (TWorkflowTransitionPeer.TPUUID.equals(name))
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
            return getStationFrom();
        }
        if (pos == 2)
        {
            return getStationTo();
        }
        if (pos == 3)
        {
            return getActionKey();
        }
        if (pos == 4)
        {
            return getWorkflow();
        }
        if (pos == 5)
        {
            return getTextPositionX();
        }
        if (pos == 6)
        {
            return getTextPositionY();
        }
        if (pos == 7)
        {
            return getTransitionType();
        }
        if (pos == 8)
        {
            return getControlPoints();
        }
        if (pos == 9)
        {
            return getElapsedTime();
        }
        if (pos == 10)
        {
            return getTimeUnit();
        }
        if (pos == 11)
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
            return setByName("StationFrom", value);
        }
    if (position == 2)
        {
            return setByName("StationTo", value);
        }
    if (position == 3)
        {
            return setByName("ActionKey", value);
        }
    if (position == 4)
        {
            return setByName("Workflow", value);
        }
    if (position == 5)
        {
            return setByName("TextPositionX", value);
        }
    if (position == 6)
        {
            return setByName("TextPositionY", value);
        }
    if (position == 7)
        {
            return setByName("TransitionType", value);
        }
    if (position == 8)
        {
            return setByName("ControlPoints", value);
        }
    if (position == 9)
        {
            return setByName("ElapsedTime", value);
        }
    if (position == 10)
        {
            return setByName("TimeUnit", value);
        }
    if (position == 11)
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
        save(TWorkflowTransitionPeer.DATABASE_NAME);
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
                    TWorkflowTransitionPeer.doInsert((TWorkflowTransition) this, con);
                    setNew(false);
                }
                else
                {
                    TWorkflowTransitionPeer.doUpdate((TWorkflowTransition) this, con);
                }
            }


            if (collTWorkflowActivitys != null)
            {
                for (int i = 0; i < collTWorkflowActivitys.size(); i++)
                {
                    ((TWorkflowActivity) collTWorkflowActivitys.get(i)).save(con);
                }
            }

            if (collTWorkflowGuards != null)
            {
                for (int i = 0; i < collTWorkflowGuards.size(); i++)
                {
                    ((TWorkflowGuard) collTWorkflowGuards.get(i)).save(con);
                }
            }

            if (collTItemTransitions != null)
            {
                for (int i = 0; i < collTItemTransitions.size(); i++)
                {
                    ((TItemTransition) collTItemTransitions.get(i)).save(con);
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
    public TWorkflowTransition copy() throws TorqueException
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
    public TWorkflowTransition copy(Connection con) throws TorqueException
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
    public TWorkflowTransition copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TWorkflowTransition(), deepcopy);
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
    public TWorkflowTransition copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TWorkflowTransition(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TWorkflowTransition copyInto(TWorkflowTransition copyObj) throws TorqueException
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
    protected TWorkflowTransition copyInto(TWorkflowTransition copyObj, Connection con) throws TorqueException
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
    protected TWorkflowTransition copyInto(TWorkflowTransition copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setStationFrom(stationFrom);
        copyObj.setStationTo(stationTo);
        copyObj.setActionKey(actionKey);
        copyObj.setWorkflow(workflow);
        copyObj.setTextPositionX(textPositionX);
        copyObj.setTextPositionY(textPositionY);
        copyObj.setTransitionType(transitionType);
        copyObj.setControlPoints(controlPoints);
        copyObj.setElapsedTime(elapsedTime);
        copyObj.setTimeUnit(timeUnit);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkflowActivity> vTWorkflowActivitys = getTWorkflowActivitys();
        if (vTWorkflowActivitys != null)
        {
            for (int i = 0; i < vTWorkflowActivitys.size(); i++)
            {
                TWorkflowActivity obj =  vTWorkflowActivitys.get(i);
                copyObj.addTWorkflowActivity(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowActivitys = null;
        }


        List<TWorkflowGuard> vTWorkflowGuards = getTWorkflowGuards();
        if (vTWorkflowGuards != null)
        {
            for (int i = 0; i < vTWorkflowGuards.size(); i++)
            {
                TWorkflowGuard obj =  vTWorkflowGuards.get(i);
                copyObj.addTWorkflowGuard(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkflowGuards = null;
        }


        List<TItemTransition> vTItemTransitions = getTItemTransitions();
        if (vTItemTransitions != null)
        {
            for (int i = 0; i < vTItemTransitions.size(); i++)
            {
                TItemTransition obj =  vTItemTransitions.get(i);
                copyObj.addTItemTransition(obj.copy());
            }
        }
        else
        {
            copyObj.collTItemTransitions = null;
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
    protected TWorkflowTransition copyInto(TWorkflowTransition copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setStationFrom(stationFrom);
        copyObj.setStationTo(stationTo);
        copyObj.setActionKey(actionKey);
        copyObj.setWorkflow(workflow);
        copyObj.setTextPositionX(textPositionX);
        copyObj.setTextPositionY(textPositionY);
        copyObj.setTransitionType(transitionType);
        copyObj.setControlPoints(controlPoints);
        copyObj.setElapsedTime(elapsedTime);
        copyObj.setTimeUnit(timeUnit);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkflowActivity> vTWorkflowActivitys = getTWorkflowActivitys(con);
        if (vTWorkflowActivitys != null)
        {
            for (int i = 0; i < vTWorkflowActivitys.size(); i++)
            {
                TWorkflowActivity obj =  vTWorkflowActivitys.get(i);
                copyObj.addTWorkflowActivity(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowActivitys = null;
        }


        List<TWorkflowGuard> vTWorkflowGuards = getTWorkflowGuards(con);
        if (vTWorkflowGuards != null)
        {
            for (int i = 0; i < vTWorkflowGuards.size(); i++)
            {
                TWorkflowGuard obj =  vTWorkflowGuards.get(i);
                copyObj.addTWorkflowGuard(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkflowGuards = null;
        }


        List<TItemTransition> vTItemTransitions = getTItemTransitions(con);
        if (vTItemTransitions != null)
        {
            for (int i = 0; i < vTItemTransitions.size(); i++)
            {
                TItemTransition obj =  vTItemTransitions.get(i);
                copyObj.addTItemTransition(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTItemTransitions = null;
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
    public TWorkflowTransitionPeer getPeer()
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
        return TWorkflowTransitionPeer.getTableMap();
    }

  
    /**
     * Creates a TWorkflowTransitionBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TWorkflowTransitionBean with the contents of this object
     */
    public TWorkflowTransitionBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TWorkflowTransitionBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TWorkflowTransitionBean with the contents of this object
     */
    public TWorkflowTransitionBean getBean(IdentityMap createdBeans)
    {
        TWorkflowTransitionBean result = (TWorkflowTransitionBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TWorkflowTransitionBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setStationFrom(getStationFrom());
        result.setStationTo(getStationTo());
        result.setActionKey(getActionKey());
        result.setWorkflow(getWorkflow());
        result.setTextPositionX(getTextPositionX());
        result.setTextPositionY(getTextPositionY());
        result.setTransitionType(getTransitionType());
        result.setControlPoints(getControlPoints());
        result.setElapsedTime(getElapsedTime());
        result.setTimeUnit(getTimeUnit());
        result.setUuid(getUuid());



        if (collTWorkflowActivitys != null)
        {
            List<TWorkflowActivityBean> relatedBeans = new ArrayList<TWorkflowActivityBean>(collTWorkflowActivitys.size());
            for (Iterator<TWorkflowActivity> collTWorkflowActivitysIt = collTWorkflowActivitys.iterator(); collTWorkflowActivitysIt.hasNext(); )
            {
                TWorkflowActivity related = (TWorkflowActivity) collTWorkflowActivitysIt.next();
                TWorkflowActivityBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowActivityBeans(relatedBeans);
        }


        if (collTWorkflowGuards != null)
        {
            List<TWorkflowGuardBean> relatedBeans = new ArrayList<TWorkflowGuardBean>(collTWorkflowGuards.size());
            for (Iterator<TWorkflowGuard> collTWorkflowGuardsIt = collTWorkflowGuards.iterator(); collTWorkflowGuardsIt.hasNext(); )
            {
                TWorkflowGuard related = (TWorkflowGuard) collTWorkflowGuardsIt.next();
                TWorkflowGuardBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkflowGuardBeans(relatedBeans);
        }


        if (collTItemTransitions != null)
        {
            List<TItemTransitionBean> relatedBeans = new ArrayList<TItemTransitionBean>(collTItemTransitions.size());
            for (Iterator<TItemTransition> collTItemTransitionsIt = collTItemTransitions.iterator(); collTItemTransitionsIt.hasNext(); )
            {
                TItemTransition related = (TItemTransition) collTItemTransitionsIt.next();
                TItemTransitionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTItemTransitionBeans(relatedBeans);
        }




        if (aTWorkflowStationRelatedByStationFrom != null)
        {
            TWorkflowStationBean relatedBean = aTWorkflowStationRelatedByStationFrom.getBean(createdBeans);
            result.setTWorkflowStationBeanRelatedByStationFrom(relatedBean);
        }



        if (aTWorkflowStationRelatedByStationTo != null)
        {
            TWorkflowStationBean relatedBean = aTWorkflowStationRelatedByStationTo.getBean(createdBeans);
            result.setTWorkflowStationBeanRelatedByStationTo(relatedBean);
        }



        if (aTAction != null)
        {
            TActionBean relatedBean = aTAction.getBean(createdBeans);
            result.setTActionBean(relatedBean);
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
     * Creates an instance of TWorkflowTransition with the contents
     * of a TWorkflowTransitionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TWorkflowTransitionBean which contents are used to create
     *        the resulting class
     * @return an instance of TWorkflowTransition with the contents of bean
     */
    public static TWorkflowTransition createTWorkflowTransition(TWorkflowTransitionBean bean)
        throws TorqueException
    {
        return createTWorkflowTransition(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TWorkflowTransition with the contents
     * of a TWorkflowTransitionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TWorkflowTransitionBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TWorkflowTransition with the contents of bean
     */

    public static TWorkflowTransition createTWorkflowTransition(TWorkflowTransitionBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TWorkflowTransition result = (TWorkflowTransition) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TWorkflowTransition();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setStationFrom(bean.getStationFrom());
        result.setStationTo(bean.getStationTo());
        result.setActionKey(bean.getActionKey());
        result.setWorkflow(bean.getWorkflow());
        result.setTextPositionX(bean.getTextPositionX());
        result.setTextPositionY(bean.getTextPositionY());
        result.setTransitionType(bean.getTransitionType());
        result.setControlPoints(bean.getControlPoints());
        result.setElapsedTime(bean.getElapsedTime());
        result.setTimeUnit(bean.getTimeUnit());
        result.setUuid(bean.getUuid());



        {
            List<TWorkflowActivityBean> relatedBeans = bean.getTWorkflowActivityBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowActivityBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowActivityBean relatedBean =  relatedBeansIt.next();
                    TWorkflowActivity related = TWorkflowActivity.createTWorkflowActivity(relatedBean, createdObjects);
                    result.addTWorkflowActivityFromBean(related);
                }
            }
        }


        {
            List<TWorkflowGuardBean> relatedBeans = bean.getTWorkflowGuardBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkflowGuardBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkflowGuardBean relatedBean =  relatedBeansIt.next();
                    TWorkflowGuard related = TWorkflowGuard.createTWorkflowGuard(relatedBean, createdObjects);
                    result.addTWorkflowGuardFromBean(related);
                }
            }
        }


        {
            List<TItemTransitionBean> relatedBeans = bean.getTItemTransitionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TItemTransitionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TItemTransitionBean relatedBean =  relatedBeansIt.next();
                    TItemTransition related = TItemTransition.createTItemTransition(relatedBean, createdObjects);
                    result.addTItemTransitionFromBean(related);
                }
            }
        }




        {
            TWorkflowStationBean relatedBean = bean.getTWorkflowStationBeanRelatedByStationFrom();
            if (relatedBean != null)
            {
                TWorkflowStation relatedObject = TWorkflowStation.createTWorkflowStation(relatedBean, createdObjects);
                result.setTWorkflowStationRelatedByStationFrom(relatedObject);
            }
        }



        {
            TWorkflowStationBean relatedBean = bean.getTWorkflowStationBeanRelatedByStationTo();
            if (relatedBean != null)
            {
                TWorkflowStation relatedObject = TWorkflowStation.createTWorkflowStation(relatedBean, createdObjects);
                result.setTWorkflowStationRelatedByStationTo(relatedObject);
            }
        }



        {
            TActionBean relatedBean = bean.getTActionBean();
            if (relatedBean != null)
            {
                TAction relatedObject = TAction.createTAction(relatedBean, createdObjects);
                result.setTAction(relatedObject);
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
     * Method called to associate a TWorkflowActivity object to this object.
     * through the TWorkflowActivity foreign key attribute
     *
     * @param toAdd TWorkflowActivity
     */
    protected void addTWorkflowActivityFromBean(TWorkflowActivity toAdd)
    {
        initTWorkflowActivitys();
        collTWorkflowActivitys.add(toAdd);
    }


    /**
     * Method called to associate a TWorkflowGuard object to this object.
     * through the TWorkflowGuard foreign key attribute
     *
     * @param toAdd TWorkflowGuard
     */
    protected void addTWorkflowGuardFromBean(TWorkflowGuard toAdd)
    {
        initTWorkflowGuards();
        collTWorkflowGuards.add(toAdd);
    }


    /**
     * Method called to associate a TItemTransition object to this object.
     * through the TItemTransition foreign key attribute
     *
     * @param toAdd TItemTransition
     */
    protected void addTItemTransitionFromBean(TItemTransition toAdd)
    {
        initTItemTransitions();
        collTItemTransitions.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TWorkflowTransition:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("StationFrom = ")
           .append(getStationFrom())
           .append("\n");
        str.append("StationTo = ")
           .append(getStationTo())
           .append("\n");
        str.append("ActionKey = ")
           .append(getActionKey())
           .append("\n");
        str.append("Workflow = ")
           .append(getWorkflow())
           .append("\n");
        str.append("TextPositionX = ")
           .append(getTextPositionX())
           .append("\n");
        str.append("TextPositionY = ")
           .append(getTextPositionY())
           .append("\n");
        str.append("TransitionType = ")
           .append(getTransitionType())
           .append("\n");
        str.append("ControlPoints = ")
           .append(getControlPoints())
           .append("\n");
        str.append("ElapsedTime = ")
           .append(getElapsedTime())
           .append("\n");
        str.append("TimeUnit = ")
           .append(getTimeUnit())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
