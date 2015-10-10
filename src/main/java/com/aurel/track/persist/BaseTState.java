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



import com.aurel.track.persist.TBLOB;
import com.aurel.track.persist.TBLOBPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TBLOBBean;

import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TStateChangeBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TPstateBean;
import com.aurel.track.beans.TWorkFlowBean;
import com.aurel.track.beans.TWorkFlowBean;
import com.aurel.track.beans.TInitStateBean;
import com.aurel.track.beans.TWorkflowStationBean;
import com.aurel.track.beans.TEscalationStateBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TState
 */
public abstract class BaseTState extends TpBaseObject
{
    /** The Peer class */
    private static final TStatePeer peer =
        new TStatePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the tooltip field */
    private String tooltip;

    /** The value for the stateflag field */
    private Integer stateflag;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the symbol field */
    private String symbol;

    /** The value for the iconKey field */
    private Integer iconKey;

    /** The value for the iconChanged field */
    private String iconChanged = "N";

    /** The value for the cSSStyle field */
    private String cSSStyle;

    /** The value for the percentComplete field */
    private Integer percentComplete;

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



        // update associated TNotify
        if (collTNotifys != null)
        {
            for (int i = 0; i < collTNotifys.size(); i++)
            {
                ((TNotify) collTNotifys.get(i))
                        .setStateID(v);
            }
        }

        // update associated TProject
        if (collTProjects != null)
        {
            for (int i = 0; i < collTProjects.size(); i++)
            {
                ((TProject) collTProjects.get(i))
                        .setDefaultInitStateID(v);
            }
        }

        // update associated TStateChange
        if (collTStateChanges != null)
        {
            for (int i = 0; i < collTStateChanges.size(); i++)
            {
                ((TStateChange) collTStateChanges.get(i))
                        .setChangedToID(v);
            }
        }

        // update associated TWorkItem
        if (collTWorkItems != null)
        {
            for (int i = 0; i < collTWorkItems.size(); i++)
            {
                ((TWorkItem) collTWorkItems.get(i))
                        .setStateID(v);
            }
        }

        // update associated TPstate
        if (collTPstates != null)
        {
            for (int i = 0; i < collTPstates.size(); i++)
            {
                ((TPstate) collTPstates.get(i))
                        .setState(v);
            }
        }

        // update associated TWorkFlow
        if (collTWorkFlowsRelatedByStateFrom != null)
        {
            for (int i = 0; i < collTWorkFlowsRelatedByStateFrom.size(); i++)
            {
                ((TWorkFlow) collTWorkFlowsRelatedByStateFrom.get(i))
                        .setStateFrom(v);
            }
        }

        // update associated TWorkFlow
        if (collTWorkFlowsRelatedByStateTo != null)
        {
            for (int i = 0; i < collTWorkFlowsRelatedByStateTo.size(); i++)
            {
                ((TWorkFlow) collTWorkFlowsRelatedByStateTo.get(i))
                        .setStateTo(v);
            }
        }

        // update associated TInitState
        if (collTInitStates != null)
        {
            for (int i = 0; i < collTInitStates.size(); i++)
            {
                ((TInitState) collTInitStates.get(i))
                        .setStateKey(v);
            }
        }

        // update associated TWorkflowStation
        if (collTWorkflowStations != null)
        {
            for (int i = 0; i < collTWorkflowStations.size(); i++)
            {
                ((TWorkflowStation) collTWorkflowStations.get(i))
                        .setStatus(v);
            }
        }

        // update associated TEscalationState
        if (collTEscalationStates != null)
        {
            for (int i = 0; i < collTEscalationStates.size(); i++)
            {
                ((TEscalationState) collTEscalationStates.get(i))
                        .setStatus(v);
            }
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
     * Get the Tooltip
     *
     * @return String
     */
    public String getTooltip()
    {
        return tooltip;
    }


    /**
     * Set the value of Tooltip
     *
     * @param v new value
     */
    public void setTooltip(String v) 
    {

        if (!ObjectUtils.equals(this.tooltip, v))
        {
            this.tooltip = v;
            setModified(true);
        }


    }

    /**
     * Get the Stateflag
     *
     * @return Integer
     */
    public Integer getStateflag()
    {
        return stateflag;
    }


    /**
     * Set the value of Stateflag
     *
     * @param v new value
     */
    public void setStateflag(Integer v) 
    {

        if (!ObjectUtils.equals(this.stateflag, v))
        {
            this.stateflag = v;
            setModified(true);
        }


    }

    /**
     * Get the Sortorder
     *
     * @return Integer
     */
    public Integer getSortorder()
    {
        return sortorder;
    }


    /**
     * Set the value of Sortorder
     *
     * @param v new value
     */
    public void setSortorder(Integer v) 
    {

        if (!ObjectUtils.equals(this.sortorder, v))
        {
            this.sortorder = v;
            setModified(true);
        }


    }

    /**
     * Get the Symbol
     *
     * @return String
     */
    public String getSymbol()
    {
        return symbol;
    }


    /**
     * Set the value of Symbol
     *
     * @param v new value
     */
    public void setSymbol(String v) 
    {

        if (!ObjectUtils.equals(this.symbol, v))
        {
            this.symbol = v;
            setModified(true);
        }


    }

    /**
     * Get the IconKey
     *
     * @return Integer
     */
    public Integer getIconKey()
    {
        return iconKey;
    }


    /**
     * Set the value of IconKey
     *
     * @param v new value
     */
    public void setIconKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.iconKey, v))
        {
            this.iconKey = v;
            setModified(true);
        }


        if (aTBLOB != null && !ObjectUtils.equals(aTBLOB.getObjectID(), v))
        {
            aTBLOB = null;
        }

    }

    /**
     * Get the IconChanged
     *
     * @return String
     */
    public String getIconChanged()
    {
        return iconChanged;
    }


    /**
     * Set the value of IconChanged
     *
     * @param v new value
     */
    public void setIconChanged(String v) 
    {

        if (!ObjectUtils.equals(this.iconChanged, v))
        {
            this.iconChanged = v;
            setModified(true);
        }


    }

    /**
     * Get the CSSStyle
     *
     * @return String
     */
    public String getCSSStyle()
    {
        return cSSStyle;
    }


    /**
     * Set the value of CSSStyle
     *
     * @param v new value
     */
    public void setCSSStyle(String v) 
    {

        if (!ObjectUtils.equals(this.cSSStyle, v))
        {
            this.cSSStyle = v;
            setModified(true);
        }


    }

    /**
     * Get the PercentComplete
     *
     * @return Integer
     */
    public Integer getPercentComplete()
    {
        return percentComplete;
    }


    /**
     * Set the value of PercentComplete
     *
     * @param v new value
     */
    public void setPercentComplete(Integer v) 
    {

        if (!ObjectUtils.equals(this.percentComplete, v))
        {
            this.percentComplete = v;
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

    



    private TBLOB aTBLOB;

    /**
     * Declares an association between this object and a TBLOB object
     *
     * @param v TBLOB
     * @throws TorqueException
     */
    public void setTBLOB(TBLOB v) throws TorqueException
    {
        if (v == null)
        {
            setIconKey((Integer) null);
        }
        else
        {
            setIconKey(v.getObjectID());
        }
        aTBLOB = v;
    }


    /**
     * Returns the associated TBLOB object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TBLOB object
     * @throws TorqueException
     */
    public TBLOB getTBLOB()
        throws TorqueException
    {
        if (aTBLOB == null && (!ObjectUtils.equals(this.iconKey, null)))
        {
            aTBLOB = TBLOBPeer.retrieveByPK(SimpleKey.keyFor(this.iconKey));
        }
        return aTBLOB;
    }

    /**
     * Return the associated TBLOB object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TBLOB object
     * @throws TorqueException
     */
    public TBLOB getTBLOB(Connection connection)
        throws TorqueException
    {
        if (aTBLOB == null && (!ObjectUtils.equals(this.iconKey, null)))
        {
            aTBLOB = TBLOBPeer.retrieveByPK(SimpleKey.keyFor(this.iconKey), connection);
        }
        return aTBLOB;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTBLOBKey(ObjectKey key) throws TorqueException
    {

        setIconKey(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTNotifys
     */
    protected List<TNotify> collTNotifys;

    /**
     * Temporary storage of collTNotifys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTNotifys()
    {
        if (collTNotifys == null)
        {
            collTNotifys = new ArrayList<TNotify>();
        }
    }


    /**
     * Method called to associate a TNotify object to this object
     * through the TNotify foreign key attribute
     *
     * @param l TNotify
     * @throws TorqueException
     */
    public void addTNotify(TNotify l) throws TorqueException
    {
        getTNotifys().add(l);
        l.setTState((TState) this);
    }

    /**
     * Method called to associate a TNotify object to this object
     * through the TNotify foreign key attribute using connection.
     *
     * @param l TNotify
     * @throws TorqueException
     */
    public void addTNotify(TNotify l, Connection con) throws TorqueException
    {
        getTNotifys(con).add(l);
        l.setTState((TState) this);
    }

    /**
     * The criteria used to select the current contents of collTNotifys
     */
    private Criteria lastTNotifysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNotifys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TNotify> getTNotifys()
        throws TorqueException
    {
        if (collTNotifys == null)
        {
            collTNotifys = getTNotifys(new Criteria(10));
        }
        return collTNotifys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TNotifys from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TNotify> getTNotifys(Criteria criteria) throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                criteria.add(TNotifyPeer.STATEKEY, getObjectID() );
                collTNotifys = TNotifyPeer.doSelect(criteria);
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
                criteria.add(TNotifyPeer.STATEKEY, getObjectID());
                if (!lastTNotifysCriteria.equals(criteria))
                {
                    collTNotifys = TNotifyPeer.doSelect(criteria);
                }
            }
        }
        lastTNotifysCriteria = criteria;

        return collTNotifys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNotifys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNotify> getTNotifys(Connection con) throws TorqueException
    {
        if (collTNotifys == null)
        {
            collTNotifys = getTNotifys(new Criteria(10), con);
        }
        return collTNotifys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TNotifys from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNotify> getTNotifys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                 criteria.add(TNotifyPeer.STATEKEY, getObjectID());
                 collTNotifys = TNotifyPeer.doSelect(criteria, con);
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
                 criteria.add(TNotifyPeer.STATEKEY, getObjectID());
                 if (!lastTNotifysCriteria.equals(criteria))
                 {
                     collTNotifys = TNotifyPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTNotifysCriteria = criteria;

         return collTNotifys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TNotify> getTNotifysJoinTProjectCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                criteria.add(TNotifyPeer.STATEKEY, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.STATEKEY, getObjectID());
            if (!lastTNotifysCriteria.equals(criteria))
            {
                collTNotifys = TNotifyPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        lastTNotifysCriteria = criteria;

        return collTNotifys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TNotify> getTNotifysJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                criteria.add(TNotifyPeer.STATEKEY, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.STATEKEY, getObjectID());
            if (!lastTNotifysCriteria.equals(criteria))
            {
                collTNotifys = TNotifyPeer.doSelectJoinTState(criteria);
            }
        }
        lastTNotifysCriteria = criteria;

        return collTNotifys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TNotify> getTNotifysJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                criteria.add(TNotifyPeer.STATEKEY, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.STATEKEY, getObjectID());
            if (!lastTNotifysCriteria.equals(criteria))
            {
                collTNotifys = TNotifyPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTNotifysCriteria = criteria;

        return collTNotifys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TNotify> getTNotifysJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                criteria.add(TNotifyPeer.STATEKEY, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.STATEKEY, getObjectID());
            if (!lastTNotifysCriteria.equals(criteria))
            {
                collTNotifys = TNotifyPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTNotifysCriteria = criteria;

        return collTNotifys;
    }





    /**
     * Collection to store aggregation of collTProjects
     */
    protected List<TProject> collTProjects;

    /**
     * Temporary storage of collTProjects to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTProjects()
    {
        if (collTProjects == null)
        {
            collTProjects = new ArrayList<TProject>();
        }
    }


    /**
     * Method called to associate a TProject object to this object
     * through the TProject foreign key attribute
     *
     * @param l TProject
     * @throws TorqueException
     */
    public void addTProject(TProject l) throws TorqueException
    {
        getTProjects().add(l);
        l.setTState((TState) this);
    }

    /**
     * Method called to associate a TProject object to this object
     * through the TProject foreign key attribute using connection.
     *
     * @param l TProject
     * @throws TorqueException
     */
    public void addTProject(TProject l, Connection con) throws TorqueException
    {
        getTProjects(con).add(l);
        l.setTState((TState) this);
    }

    /**
     * The criteria used to select the current contents of collTProjects
     */
    private Criteria lastTProjectsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjects(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TProject> getTProjects()
        throws TorqueException
    {
        if (collTProjects == null)
        {
            collTProjects = getTProjects(new Criteria(10));
        }
        return collTProjects;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TProjects from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TProject> getTProjects(Criteria criteria) throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.DEFINITSTATE, getObjectID() );
                collTProjects = TProjectPeer.doSelect(criteria);
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
                criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
                if (!lastTProjectsCriteria.equals(criteria))
                {
                    collTProjects = TProjectPeer.doSelect(criteria);
                }
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjects(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProject> getTProjects(Connection con) throws TorqueException
    {
        if (collTProjects == null)
        {
            collTProjects = getTProjects(new Criteria(10), con);
        }
        return collTProjects;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TProjects from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProject> getTProjects(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                 criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
                 collTProjects = TProjectPeer.doSelect(criteria, con);
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
                 criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
                 if (!lastTProjectsCriteria.equals(criteria))
                 {
                     collTProjects = TProjectPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTProjectsCriteria = criteria;

         return collTProjects;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TProject> getTProjectsJoinTPersonRelatedByDefaultOwnerID(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultOwnerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultOwnerID(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TProject> getTProjectsJoinTPersonRelatedByDefaultManagerID(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultManagerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultManagerID(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TProject> getTProjectsJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTState(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TProject> getTProjectsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TProject> getTProjectsJoinTSystemState(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTSystemState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTSystemState(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TProject> getTProjectsJoinTDomain(Criteria criteria)
        throws TorqueException
    {
        if (collTProjects == null)
        {
            if (isNew())
            {
               collTProjects = new ArrayList<TProject>();
            }
            else
            {
                criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTDomain(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DEFINITSTATE, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTDomain(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }





    /**
     * Collection to store aggregation of collTStateChanges
     */
    protected List<TStateChange> collTStateChanges;

    /**
     * Temporary storage of collTStateChanges to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTStateChanges()
    {
        if (collTStateChanges == null)
        {
            collTStateChanges = new ArrayList<TStateChange>();
        }
    }


    /**
     * Method called to associate a TStateChange object to this object
     * through the TStateChange foreign key attribute
     *
     * @param l TStateChange
     * @throws TorqueException
     */
    public void addTStateChange(TStateChange l) throws TorqueException
    {
        getTStateChanges().add(l);
        l.setTState((TState) this);
    }

    /**
     * Method called to associate a TStateChange object to this object
     * through the TStateChange foreign key attribute using connection.
     *
     * @param l TStateChange
     * @throws TorqueException
     */
    public void addTStateChange(TStateChange l, Connection con) throws TorqueException
    {
        getTStateChanges(con).add(l);
        l.setTState((TState) this);
    }

    /**
     * The criteria used to select the current contents of collTStateChanges
     */
    private Criteria lastTStateChangesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTStateChanges(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TStateChange> getTStateChanges()
        throws TorqueException
    {
        if (collTStateChanges == null)
        {
            collTStateChanges = getTStateChanges(new Criteria(10));
        }
        return collTStateChanges;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TStateChanges from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TStateChange> getTStateChanges(Criteria criteria) throws TorqueException
    {
        if (collTStateChanges == null)
        {
            if (isNew())
            {
               collTStateChanges = new ArrayList<TStateChange>();
            }
            else
            {
                criteria.add(TStateChangePeer.CHANGEDTO, getObjectID() );
                collTStateChanges = TStateChangePeer.doSelect(criteria);
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
                criteria.add(TStateChangePeer.CHANGEDTO, getObjectID());
                if (!lastTStateChangesCriteria.equals(criteria))
                {
                    collTStateChanges = TStateChangePeer.doSelect(criteria);
                }
            }
        }
        lastTStateChangesCriteria = criteria;

        return collTStateChanges;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTStateChanges(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TStateChange> getTStateChanges(Connection con) throws TorqueException
    {
        if (collTStateChanges == null)
        {
            collTStateChanges = getTStateChanges(new Criteria(10), con);
        }
        return collTStateChanges;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TStateChanges from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TStateChange> getTStateChanges(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTStateChanges == null)
        {
            if (isNew())
            {
               collTStateChanges = new ArrayList<TStateChange>();
            }
            else
            {
                 criteria.add(TStateChangePeer.CHANGEDTO, getObjectID());
                 collTStateChanges = TStateChangePeer.doSelect(criteria, con);
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
                 criteria.add(TStateChangePeer.CHANGEDTO, getObjectID());
                 if (!lastTStateChangesCriteria.equals(criteria))
                 {
                     collTStateChanges = TStateChangePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTStateChangesCriteria = criteria;

         return collTStateChanges;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TStateChanges from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TStateChange> getTStateChangesJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTStateChanges == null)
        {
            if (isNew())
            {
               collTStateChanges = new ArrayList<TStateChange>();
            }
            else
            {
                criteria.add(TStateChangePeer.CHANGEDTO, getObjectID());
                collTStateChanges = TStateChangePeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TStateChangePeer.CHANGEDTO, getObjectID());
            if (!lastTStateChangesCriteria.equals(criteria))
            {
                collTStateChanges = TStateChangePeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTStateChangesCriteria = criteria;

        return collTStateChanges;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TStateChanges from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TStateChange> getTStateChangesJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTStateChanges == null)
        {
            if (isNew())
            {
               collTStateChanges = new ArrayList<TStateChange>();
            }
            else
            {
                criteria.add(TStateChangePeer.CHANGEDTO, getObjectID());
                collTStateChanges = TStateChangePeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TStateChangePeer.CHANGEDTO, getObjectID());
            if (!lastTStateChangesCriteria.equals(criteria))
            {
                collTStateChanges = TStateChangePeer.doSelectJoinTState(criteria);
            }
        }
        lastTStateChangesCriteria = criteria;

        return collTStateChanges;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TStateChanges from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TStateChange> getTStateChangesJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTStateChanges == null)
        {
            if (isNew())
            {
               collTStateChanges = new ArrayList<TStateChange>();
            }
            else
            {
                criteria.add(TStateChangePeer.CHANGEDTO, getObjectID());
                collTStateChanges = TStateChangePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TStateChangePeer.CHANGEDTO, getObjectID());
            if (!lastTStateChangesCriteria.equals(criteria))
            {
                collTStateChanges = TStateChangePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTStateChangesCriteria = criteria;

        return collTStateChanges;
    }





    /**
     * Collection to store aggregation of collTWorkItems
     */
    protected List<TWorkItem> collTWorkItems;

    /**
     * Temporary storage of collTWorkItems to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkItems()
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = new ArrayList<TWorkItem>();
        }
    }


    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItem(TWorkItem l) throws TorqueException
    {
        getTWorkItems().add(l);
        l.setTState((TState) this);
    }

    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute using connection.
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItem(TWorkItem l, Connection con) throws TorqueException
    {
        getTWorkItems(con).add(l);
        l.setTState((TState) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkItems
     */
    private Criteria lastTWorkItemsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItems(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItems()
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = getTWorkItems(new Criteria(10));
        }
        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItems(Criteria criteria) throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID() );
                collTWorkItems = TWorkItemPeer.doSelect(criteria);
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
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                if (!lastTWorkItemsCriteria.equals(criteria))
                {
                    collTWorkItems = TWorkItemPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItems(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItems(Connection con) throws TorqueException
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = getTWorkItems(new Criteria(10), con);
        }
        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItems(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                 criteria.add(TWorkItemPeer.STATE, getObjectID());
                 collTWorkItems = TWorkItemPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkItemPeer.STATE, getObjectID());
                 if (!lastTWorkItemsCriteria.equals(criteria))
                 {
                     collTWorkItems = TWorkItemPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkItemsCriteria = criteria;

         return collTWorkItems;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByOwnerID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByChangedByID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByOriginatorID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByResponsibleID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTProjectCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTClass(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPriority(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTSeverity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTReleaseRelatedByReleaseNoticedID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTReleaseRelatedByReleaseScheduledID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkItem> getTWorkItemsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.STATE, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.STATE, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }













    /**
     * Collection to store aggregation of collTPstates
     */
    protected List<TPstate> collTPstates;

    /**
     * Temporary storage of collTPstates to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPstates()
    {
        if (collTPstates == null)
        {
            collTPstates = new ArrayList<TPstate>();
        }
    }


    /**
     * Method called to associate a TPstate object to this object
     * through the TPstate foreign key attribute
     *
     * @param l TPstate
     * @throws TorqueException
     */
    public void addTPstate(TPstate l) throws TorqueException
    {
        getTPstates().add(l);
        l.setTState((TState) this);
    }

    /**
     * Method called to associate a TPstate object to this object
     * through the TPstate foreign key attribute using connection.
     *
     * @param l TPstate
     * @throws TorqueException
     */
    public void addTPstate(TPstate l, Connection con) throws TorqueException
    {
        getTPstates(con).add(l);
        l.setTState((TState) this);
    }

    /**
     * The criteria used to select the current contents of collTPstates
     */
    private Criteria lastTPstatesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPstates(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPstate> getTPstates()
        throws TorqueException
    {
        if (collTPstates == null)
        {
            collTPstates = getTPstates(new Criteria(10));
        }
        return collTPstates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TPstates from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPstate> getTPstates(Criteria criteria) throws TorqueException
    {
        if (collTPstates == null)
        {
            if (isNew())
            {
               collTPstates = new ArrayList<TPstate>();
            }
            else
            {
                criteria.add(TPstatePeer.STATE, getObjectID() );
                collTPstates = TPstatePeer.doSelect(criteria);
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
                criteria.add(TPstatePeer.STATE, getObjectID());
                if (!lastTPstatesCriteria.equals(criteria))
                {
                    collTPstates = TPstatePeer.doSelect(criteria);
                }
            }
        }
        lastTPstatesCriteria = criteria;

        return collTPstates;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPstates(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPstate> getTPstates(Connection con) throws TorqueException
    {
        if (collTPstates == null)
        {
            collTPstates = getTPstates(new Criteria(10), con);
        }
        return collTPstates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TPstates from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPstate> getTPstates(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPstates == null)
        {
            if (isNew())
            {
               collTPstates = new ArrayList<TPstate>();
            }
            else
            {
                 criteria.add(TPstatePeer.STATE, getObjectID());
                 collTPstates = TPstatePeer.doSelect(criteria, con);
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
                 criteria.add(TPstatePeer.STATE, getObjectID());
                 if (!lastTPstatesCriteria.equals(criteria))
                 {
                     collTPstates = TPstatePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPstatesCriteria = criteria;

         return collTPstates;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TPstates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TPstate> getTPstatesJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTPstates == null)
        {
            if (isNew())
            {
               collTPstates = new ArrayList<TPstate>();
            }
            else
            {
                criteria.add(TPstatePeer.STATE, getObjectID());
                collTPstates = TPstatePeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPstatePeer.STATE, getObjectID());
            if (!lastTPstatesCriteria.equals(criteria))
            {
                collTPstates = TPstatePeer.doSelectJoinTState(criteria);
            }
        }
        lastTPstatesCriteria = criteria;

        return collTPstates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TPstates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TPstate> getTPstatesJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTPstates == null)
        {
            if (isNew())
            {
               collTPstates = new ArrayList<TPstate>();
            }
            else
            {
                criteria.add(TPstatePeer.STATE, getObjectID());
                collTPstates = TPstatePeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPstatePeer.STATE, getObjectID());
            if (!lastTPstatesCriteria.equals(criteria))
            {
                collTPstates = TPstatePeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTPstatesCriteria = criteria;

        return collTPstates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TPstates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TPstate> getTPstatesJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTPstates == null)
        {
            if (isNew())
            {
               collTPstates = new ArrayList<TPstate>();
            }
            else
            {
                criteria.add(TPstatePeer.STATE, getObjectID());
                collTPstates = TPstatePeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPstatePeer.STATE, getObjectID());
            if (!lastTPstatesCriteria.equals(criteria))
            {
                collTPstates = TPstatePeer.doSelectJoinTListType(criteria);
            }
        }
        lastTPstatesCriteria = criteria;

        return collTPstates;
    }





    /**
     * Collection to store aggregation of collTWorkFlowsRelatedByStateFrom
     */
    protected List<TWorkFlow> collTWorkFlowsRelatedByStateFrom;

    /**
     * Temporary storage of collTWorkFlowsRelatedByStateFrom to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkFlowsRelatedByStateFrom()
    {
        if (collTWorkFlowsRelatedByStateFrom == null)
        {
            collTWorkFlowsRelatedByStateFrom = new ArrayList<TWorkFlow>();
        }
    }


    /**
     * Method called to associate a TWorkFlow object to this object
     * through the TWorkFlow foreign key attribute
     *
     * @param l TWorkFlow
     * @throws TorqueException
     */
    public void addTWorkFlowRelatedByStateFrom(TWorkFlow l) throws TorqueException
    {
        getTWorkFlowsRelatedByStateFrom().add(l);
        l.setTStateRelatedByStateFrom((TState) this);
    }

    /**
     * Method called to associate a TWorkFlow object to this object
     * through the TWorkFlow foreign key attribute using connection.
     *
     * @param l TWorkFlow
     * @throws TorqueException
     */
    public void addTWorkFlowRelatedByStateFrom(TWorkFlow l, Connection con) throws TorqueException
    {
        getTWorkFlowsRelatedByStateFrom(con).add(l);
        l.setTStateRelatedByStateFrom((TState) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkFlowsRelatedByStateFrom
     */
    private Criteria lastTWorkFlowsRelatedByStateFromCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlowsRelatedByStateFrom(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkFlow> getTWorkFlowsRelatedByStateFrom()
        throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateFrom == null)
        {
            collTWorkFlowsRelatedByStateFrom = getTWorkFlowsRelatedByStateFrom(new Criteria(10));
        }
        return collTWorkFlowsRelatedByStateFrom;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TWorkFlowsRelatedByStateFrom from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkFlow> getTWorkFlowsRelatedByStateFrom(Criteria criteria) throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateFrom == null)
        {
            if (isNew())
            {
               collTWorkFlowsRelatedByStateFrom = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.STATEFROM, getObjectID() );
                collTWorkFlowsRelatedByStateFrom = TWorkFlowPeer.doSelect(criteria);
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
                criteria.add(TWorkFlowPeer.STATEFROM, getObjectID());
                if (!lastTWorkFlowsRelatedByStateFromCriteria.equals(criteria))
                {
                    collTWorkFlowsRelatedByStateFrom = TWorkFlowPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkFlowsRelatedByStateFromCriteria = criteria;

        return collTWorkFlowsRelatedByStateFrom;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlowsRelatedByStateFrom(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlow> getTWorkFlowsRelatedByStateFrom(Connection con) throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateFrom == null)
        {
            collTWorkFlowsRelatedByStateFrom = getTWorkFlowsRelatedByStateFrom(new Criteria(10), con);
        }
        return collTWorkFlowsRelatedByStateFrom;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TWorkFlowsRelatedByStateFrom from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlow> getTWorkFlowsRelatedByStateFrom(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateFrom == null)
        {
            if (isNew())
            {
               collTWorkFlowsRelatedByStateFrom = new ArrayList<TWorkFlow>();
            }
            else
            {
                 criteria.add(TWorkFlowPeer.STATEFROM, getObjectID());
                 collTWorkFlowsRelatedByStateFrom = TWorkFlowPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkFlowPeer.STATEFROM, getObjectID());
                 if (!lastTWorkFlowsRelatedByStateFromCriteria.equals(criteria))
                 {
                     collTWorkFlowsRelatedByStateFrom = TWorkFlowPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkFlowsRelatedByStateFromCriteria = criteria;

         return collTWorkFlowsRelatedByStateFrom;
     }



















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkFlowsRelatedByStateFrom from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkFlow> getTWorkFlowsRelatedByStateFromJoinTStateRelatedByStateTo(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateFrom == null)
        {
            if (isNew())
            {
               collTWorkFlowsRelatedByStateFrom = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.STATEFROM, getObjectID());
                collTWorkFlowsRelatedByStateFrom = TWorkFlowPeer.doSelectJoinTStateRelatedByStateTo(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowPeer.STATEFROM, getObjectID());
            if (!lastTWorkFlowsRelatedByStateFromCriteria.equals(criteria))
            {
                collTWorkFlowsRelatedByStateFrom = TWorkFlowPeer.doSelectJoinTStateRelatedByStateTo(criteria);
            }
        }
        lastTWorkFlowsRelatedByStateFromCriteria = criteria;

        return collTWorkFlowsRelatedByStateFrom;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkFlowsRelatedByStateFrom from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkFlow> getTWorkFlowsRelatedByStateFromJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateFrom == null)
        {
            if (isNew())
            {
               collTWorkFlowsRelatedByStateFrom = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.STATEFROM, getObjectID());
                collTWorkFlowsRelatedByStateFrom = TWorkFlowPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowPeer.STATEFROM, getObjectID());
            if (!lastTWorkFlowsRelatedByStateFromCriteria.equals(criteria))
            {
                collTWorkFlowsRelatedByStateFrom = TWorkFlowPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTWorkFlowsRelatedByStateFromCriteria = criteria;

        return collTWorkFlowsRelatedByStateFrom;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkFlowsRelatedByStateFrom from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkFlow> getTWorkFlowsRelatedByStateFromJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateFrom == null)
        {
            if (isNew())
            {
               collTWorkFlowsRelatedByStateFrom = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.STATEFROM, getObjectID());
                collTWorkFlowsRelatedByStateFrom = TWorkFlowPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowPeer.STATEFROM, getObjectID());
            if (!lastTWorkFlowsRelatedByStateFromCriteria.equals(criteria))
            {
                collTWorkFlowsRelatedByStateFrom = TWorkFlowPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTWorkFlowsRelatedByStateFromCriteria = criteria;

        return collTWorkFlowsRelatedByStateFrom;
    }





    /**
     * Collection to store aggregation of collTWorkFlowsRelatedByStateTo
     */
    protected List<TWorkFlow> collTWorkFlowsRelatedByStateTo;

    /**
     * Temporary storage of collTWorkFlowsRelatedByStateTo to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkFlowsRelatedByStateTo()
    {
        if (collTWorkFlowsRelatedByStateTo == null)
        {
            collTWorkFlowsRelatedByStateTo = new ArrayList<TWorkFlow>();
        }
    }


    /**
     * Method called to associate a TWorkFlow object to this object
     * through the TWorkFlow foreign key attribute
     *
     * @param l TWorkFlow
     * @throws TorqueException
     */
    public void addTWorkFlowRelatedByStateTo(TWorkFlow l) throws TorqueException
    {
        getTWorkFlowsRelatedByStateTo().add(l);
        l.setTStateRelatedByStateTo((TState) this);
    }

    /**
     * Method called to associate a TWorkFlow object to this object
     * through the TWorkFlow foreign key attribute using connection.
     *
     * @param l TWorkFlow
     * @throws TorqueException
     */
    public void addTWorkFlowRelatedByStateTo(TWorkFlow l, Connection con) throws TorqueException
    {
        getTWorkFlowsRelatedByStateTo(con).add(l);
        l.setTStateRelatedByStateTo((TState) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkFlowsRelatedByStateTo
     */
    private Criteria lastTWorkFlowsRelatedByStateToCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlowsRelatedByStateTo(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkFlow> getTWorkFlowsRelatedByStateTo()
        throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateTo == null)
        {
            collTWorkFlowsRelatedByStateTo = getTWorkFlowsRelatedByStateTo(new Criteria(10));
        }
        return collTWorkFlowsRelatedByStateTo;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TWorkFlowsRelatedByStateTo from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkFlow> getTWorkFlowsRelatedByStateTo(Criteria criteria) throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateTo == null)
        {
            if (isNew())
            {
               collTWorkFlowsRelatedByStateTo = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.STATETO, getObjectID() );
                collTWorkFlowsRelatedByStateTo = TWorkFlowPeer.doSelect(criteria);
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
                criteria.add(TWorkFlowPeer.STATETO, getObjectID());
                if (!lastTWorkFlowsRelatedByStateToCriteria.equals(criteria))
                {
                    collTWorkFlowsRelatedByStateTo = TWorkFlowPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkFlowsRelatedByStateToCriteria = criteria;

        return collTWorkFlowsRelatedByStateTo;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlowsRelatedByStateTo(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlow> getTWorkFlowsRelatedByStateTo(Connection con) throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateTo == null)
        {
            collTWorkFlowsRelatedByStateTo = getTWorkFlowsRelatedByStateTo(new Criteria(10), con);
        }
        return collTWorkFlowsRelatedByStateTo;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TWorkFlowsRelatedByStateTo from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlow> getTWorkFlowsRelatedByStateTo(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateTo == null)
        {
            if (isNew())
            {
               collTWorkFlowsRelatedByStateTo = new ArrayList<TWorkFlow>();
            }
            else
            {
                 criteria.add(TWorkFlowPeer.STATETO, getObjectID());
                 collTWorkFlowsRelatedByStateTo = TWorkFlowPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkFlowPeer.STATETO, getObjectID());
                 if (!lastTWorkFlowsRelatedByStateToCriteria.equals(criteria))
                 {
                     collTWorkFlowsRelatedByStateTo = TWorkFlowPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkFlowsRelatedByStateToCriteria = criteria;

         return collTWorkFlowsRelatedByStateTo;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkFlowsRelatedByStateTo from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkFlow> getTWorkFlowsRelatedByStateToJoinTStateRelatedByStateFrom(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateTo == null)
        {
            if (isNew())
            {
               collTWorkFlowsRelatedByStateTo = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.STATETO, getObjectID());
                collTWorkFlowsRelatedByStateTo = TWorkFlowPeer.doSelectJoinTStateRelatedByStateFrom(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowPeer.STATETO, getObjectID());
            if (!lastTWorkFlowsRelatedByStateToCriteria.equals(criteria))
            {
                collTWorkFlowsRelatedByStateTo = TWorkFlowPeer.doSelectJoinTStateRelatedByStateFrom(criteria);
            }
        }
        lastTWorkFlowsRelatedByStateToCriteria = criteria;

        return collTWorkFlowsRelatedByStateTo;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkFlowsRelatedByStateTo from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkFlow> getTWorkFlowsRelatedByStateToJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateTo == null)
        {
            if (isNew())
            {
               collTWorkFlowsRelatedByStateTo = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.STATETO, getObjectID());
                collTWorkFlowsRelatedByStateTo = TWorkFlowPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowPeer.STATETO, getObjectID());
            if (!lastTWorkFlowsRelatedByStateToCriteria.equals(criteria))
            {
                collTWorkFlowsRelatedByStateTo = TWorkFlowPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTWorkFlowsRelatedByStateToCriteria = criteria;

        return collTWorkFlowsRelatedByStateTo;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkFlowsRelatedByStateTo from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TWorkFlow> getTWorkFlowsRelatedByStateToJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowsRelatedByStateTo == null)
        {
            if (isNew())
            {
               collTWorkFlowsRelatedByStateTo = new ArrayList<TWorkFlow>();
            }
            else
            {
                criteria.add(TWorkFlowPeer.STATETO, getObjectID());
                collTWorkFlowsRelatedByStateTo = TWorkFlowPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowPeer.STATETO, getObjectID());
            if (!lastTWorkFlowsRelatedByStateToCriteria.equals(criteria))
            {
                collTWorkFlowsRelatedByStateTo = TWorkFlowPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTWorkFlowsRelatedByStateToCriteria = criteria;

        return collTWorkFlowsRelatedByStateTo;
    }





    /**
     * Collection to store aggregation of collTInitStates
     */
    protected List<TInitState> collTInitStates;

    /**
     * Temporary storage of collTInitStates to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTInitStates()
    {
        if (collTInitStates == null)
        {
            collTInitStates = new ArrayList<TInitState>();
        }
    }


    /**
     * Method called to associate a TInitState object to this object
     * through the TInitState foreign key attribute
     *
     * @param l TInitState
     * @throws TorqueException
     */
    public void addTInitState(TInitState l) throws TorqueException
    {
        getTInitStates().add(l);
        l.setTState((TState) this);
    }

    /**
     * Method called to associate a TInitState object to this object
     * through the TInitState foreign key attribute using connection.
     *
     * @param l TInitState
     * @throws TorqueException
     */
    public void addTInitState(TInitState l, Connection con) throws TorqueException
    {
        getTInitStates(con).add(l);
        l.setTState((TState) this);
    }

    /**
     * The criteria used to select the current contents of collTInitStates
     */
    private Criteria lastTInitStatesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTInitStates(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TInitState> getTInitStates()
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            collTInitStates = getTInitStates(new Criteria(10));
        }
        return collTInitStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TInitStates from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TInitState> getTInitStates(Criteria criteria) throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.STATEKEY, getObjectID() );
                collTInitStates = TInitStatePeer.doSelect(criteria);
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
                criteria.add(TInitStatePeer.STATEKEY, getObjectID());
                if (!lastTInitStatesCriteria.equals(criteria))
                {
                    collTInitStates = TInitStatePeer.doSelect(criteria);
                }
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTInitStates(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TInitState> getTInitStates(Connection con) throws TorqueException
    {
        if (collTInitStates == null)
        {
            collTInitStates = getTInitStates(new Criteria(10), con);
        }
        return collTInitStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TInitStates from storage.
     * If this TState is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TInitState> getTInitStates(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                 criteria.add(TInitStatePeer.STATEKEY, getObjectID());
                 collTInitStates = TInitStatePeer.doSelect(criteria, con);
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
                 criteria.add(TInitStatePeer.STATEKEY, getObjectID());
                 if (!lastTInitStatesCriteria.equals(criteria))
                 {
                     collTInitStates = TInitStatePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTInitStatesCriteria = criteria;

         return collTInitStates;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TInitStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TInitState> getTInitStatesJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.STATEKEY, getObjectID());
                collTInitStates = TInitStatePeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TInitStatePeer.STATEKEY, getObjectID());
            if (!lastTInitStatesCriteria.equals(criteria))
            {
                collTInitStates = TInitStatePeer.doSelectJoinTProject(criteria);
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TInitStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TInitState> getTInitStatesJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.STATEKEY, getObjectID());
                collTInitStates = TInitStatePeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TInitStatePeer.STATEKEY, getObjectID());
            if (!lastTInitStatesCriteria.equals(criteria))
            {
                collTInitStates = TInitStatePeer.doSelectJoinTListType(criteria);
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TInitStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
     */
    protected List<TInitState> getTInitStatesJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTInitStates == null)
        {
            if (isNew())
            {
               collTInitStates = new ArrayList<TInitState>();
            }
            else
            {
                criteria.add(TInitStatePeer.STATEKEY, getObjectID());
                collTInitStates = TInitStatePeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TInitStatePeer.STATEKEY, getObjectID());
            if (!lastTInitStatesCriteria.equals(criteria))
            {
                collTInitStates = TInitStatePeer.doSelectJoinTState(criteria);
            }
        }
        lastTInitStatesCriteria = criteria;

        return collTInitStates;
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
        l.setTState((TState) this);
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
        l.setTState((TState) this);
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
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TWorkflowStations from storage.
     * If this TState is new, it will return
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
                criteria.add(TWorkflowStationPeer.STATUS, getObjectID() );
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
                criteria.add(TWorkflowStationPeer.STATUS, getObjectID());
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
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TWorkflowStations from storage.
     * If this TState is new, it will return
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
                 criteria.add(TWorkflowStationPeer.STATUS, getObjectID());
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
                 criteria.add(TWorkflowStationPeer.STATUS, getObjectID());
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
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkflowStations from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
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
                criteria.add(TWorkflowStationPeer.STATUS, getObjectID());
                collTWorkflowStations = TWorkflowStationPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowStationPeer.STATUS, getObjectID());
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
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TWorkflowStations from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
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
                criteria.add(TWorkflowStationPeer.STATUS, getObjectID());
                collTWorkflowStations = TWorkflowStationPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowStationPeer.STATUS, getObjectID());
            if (!lastTWorkflowStationsCriteria.equals(criteria))
            {
                collTWorkflowStations = TWorkflowStationPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        lastTWorkflowStationsCriteria = criteria;

        return collTWorkflowStations;
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
        l.setTState((TState) this);
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
        l.setTState((TState) this);
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
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TEscalationStates from storage.
     * If this TState is new, it will return
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
                criteria.add(TEscalationStatePeer.STATUS, getObjectID() );
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
                criteria.add(TEscalationStatePeer.STATUS, getObjectID());
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
     * Otherwise if this TState has previously
     * been saved, it will retrieve related TEscalationStates from storage.
     * If this TState is new, it will return
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
                 criteria.add(TEscalationStatePeer.STATUS, getObjectID());
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
                 criteria.add(TEscalationStatePeer.STATUS, getObjectID());
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
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TEscalationStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
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
                criteria.add(TEscalationStatePeer.STATUS, getObjectID());
                collTEscalationStates = TEscalationStatePeer.doSelectJoinTEscalationEntry(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEscalationStatePeer.STATUS, getObjectID());
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
     * Otherwise if this TState is new, it will return
     * an empty collection; or if this TState has previously
     * been saved, it will retrieve related TEscalationStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TState.
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
                criteria.add(TEscalationStatePeer.STATUS, getObjectID());
                collTEscalationStates = TEscalationStatePeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEscalationStatePeer.STATUS, getObjectID());
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
            fieldNames.add("Label");
            fieldNames.add("Tooltip");
            fieldNames.add("Stateflag");
            fieldNames.add("Sortorder");
            fieldNames.add("Symbol");
            fieldNames.add("IconKey");
            fieldNames.add("IconChanged");
            fieldNames.add("CSSStyle");
            fieldNames.add("PercentComplete");
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
        if (name.equals("Label"))
        {
            return getLabel();
        }
        if (name.equals("Tooltip"))
        {
            return getTooltip();
        }
        if (name.equals("Stateflag"))
        {
            return getStateflag();
        }
        if (name.equals("Sortorder"))
        {
            return getSortorder();
        }
        if (name.equals("Symbol"))
        {
            return getSymbol();
        }
        if (name.equals("IconKey"))
        {
            return getIconKey();
        }
        if (name.equals("IconChanged"))
        {
            return getIconChanged();
        }
        if (name.equals("CSSStyle"))
        {
            return getCSSStyle();
        }
        if (name.equals("PercentComplete"))
        {
            return getPercentComplete();
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
        if (name.equals("Tooltip"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTooltip((String) value);
            return true;
        }
        if (name.equals("Stateflag"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStateflag((Integer) value);
            return true;
        }
        if (name.equals("Sortorder"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSortorder((Integer) value);
            return true;
        }
        if (name.equals("Symbol"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSymbol((String) value);
            return true;
        }
        if (name.equals("IconKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIconKey((Integer) value);
            return true;
        }
        if (name.equals("IconChanged"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIconChanged((String) value);
            return true;
        }
        if (name.equals("CSSStyle"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCSSStyle((String) value);
            return true;
        }
        if (name.equals("PercentComplete"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPercentComplete((Integer) value);
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
        if (name.equals(TStatePeer.PKEY))
        {
            return getObjectID();
        }
        if (name.equals(TStatePeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TStatePeer.TOOLTIP))
        {
            return getTooltip();
        }
        if (name.equals(TStatePeer.STATEFLAG))
        {
            return getStateflag();
        }
        if (name.equals(TStatePeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TStatePeer.SYMBOL))
        {
            return getSymbol();
        }
        if (name.equals(TStatePeer.ICONKEY))
        {
            return getIconKey();
        }
        if (name.equals(TStatePeer.ICONCHANGED))
        {
            return getIconChanged();
        }
        if (name.equals(TStatePeer.CSSSTYLE))
        {
            return getCSSStyle();
        }
        if (name.equals(TStatePeer.PERCENTCOMPLETE))
        {
            return getPercentComplete();
        }
        if (name.equals(TStatePeer.TPUUID))
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
      if (TStatePeer.PKEY.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TStatePeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TStatePeer.TOOLTIP.equals(name))
        {
            return setByName("Tooltip", value);
        }
      if (TStatePeer.STATEFLAG.equals(name))
        {
            return setByName("Stateflag", value);
        }
      if (TStatePeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TStatePeer.SYMBOL.equals(name))
        {
            return setByName("Symbol", value);
        }
      if (TStatePeer.ICONKEY.equals(name))
        {
            return setByName("IconKey", value);
        }
      if (TStatePeer.ICONCHANGED.equals(name))
        {
            return setByName("IconChanged", value);
        }
      if (TStatePeer.CSSSTYLE.equals(name))
        {
            return setByName("CSSStyle", value);
        }
      if (TStatePeer.PERCENTCOMPLETE.equals(name))
        {
            return setByName("PercentComplete", value);
        }
      if (TStatePeer.TPUUID.equals(name))
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
            return getLabel();
        }
        if (pos == 2)
        {
            return getTooltip();
        }
        if (pos == 3)
        {
            return getStateflag();
        }
        if (pos == 4)
        {
            return getSortorder();
        }
        if (pos == 5)
        {
            return getSymbol();
        }
        if (pos == 6)
        {
            return getIconKey();
        }
        if (pos == 7)
        {
            return getIconChanged();
        }
        if (pos == 8)
        {
            return getCSSStyle();
        }
        if (pos == 9)
        {
            return getPercentComplete();
        }
        if (pos == 10)
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
            return setByName("Label", value);
        }
    if (position == 2)
        {
            return setByName("Tooltip", value);
        }
    if (position == 3)
        {
            return setByName("Stateflag", value);
        }
    if (position == 4)
        {
            return setByName("Sortorder", value);
        }
    if (position == 5)
        {
            return setByName("Symbol", value);
        }
    if (position == 6)
        {
            return setByName("IconKey", value);
        }
    if (position == 7)
        {
            return setByName("IconChanged", value);
        }
    if (position == 8)
        {
            return setByName("CSSStyle", value);
        }
    if (position == 9)
        {
            return setByName("PercentComplete", value);
        }
    if (position == 10)
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
        save(TStatePeer.DATABASE_NAME);
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
                    TStatePeer.doInsert((TState) this, con);
                    setNew(false);
                }
                else
                {
                    TStatePeer.doUpdate((TState) this, con);
                }
            }


            if (collTNotifys != null)
            {
                for (int i = 0; i < collTNotifys.size(); i++)
                {
                    ((TNotify) collTNotifys.get(i)).save(con);
                }
            }

            if (collTProjects != null)
            {
                for (int i = 0; i < collTProjects.size(); i++)
                {
                    ((TProject) collTProjects.get(i)).save(con);
                }
            }

            if (collTStateChanges != null)
            {
                for (int i = 0; i < collTStateChanges.size(); i++)
                {
                    ((TStateChange) collTStateChanges.get(i)).save(con);
                }
            }

            if (collTWorkItems != null)
            {
                for (int i = 0; i < collTWorkItems.size(); i++)
                {
                    ((TWorkItem) collTWorkItems.get(i)).save(con);
                }
            }

            if (collTPstates != null)
            {
                for (int i = 0; i < collTPstates.size(); i++)
                {
                    ((TPstate) collTPstates.get(i)).save(con);
                }
            }

            if (collTWorkFlowsRelatedByStateFrom != null)
            {
                for (int i = 0; i < collTWorkFlowsRelatedByStateFrom.size(); i++)
                {
                    ((TWorkFlow) collTWorkFlowsRelatedByStateFrom.get(i)).save(con);
                }
            }

            if (collTWorkFlowsRelatedByStateTo != null)
            {
                for (int i = 0; i < collTWorkFlowsRelatedByStateTo.size(); i++)
                {
                    ((TWorkFlow) collTWorkFlowsRelatedByStateTo.get(i)).save(con);
                }
            }

            if (collTInitStates != null)
            {
                for (int i = 0; i < collTInitStates.size(); i++)
                {
                    ((TInitState) collTInitStates.get(i)).save(con);
                }
            }

            if (collTWorkflowStations != null)
            {
                for (int i = 0; i < collTWorkflowStations.size(); i++)
                {
                    ((TWorkflowStation) collTWorkflowStations.get(i)).save(con);
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
    public TState copy() throws TorqueException
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
    public TState copy(Connection con) throws TorqueException
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
    public TState copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TState(), deepcopy);
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
    public TState copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TState(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TState copyInto(TState copyObj) throws TorqueException
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
    protected TState copyInto(TState copyObj, Connection con) throws TorqueException
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
    protected TState copyInto(TState copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setTooltip(tooltip);
        copyObj.setStateflag(stateflag);
        copyObj.setSortorder(sortorder);
        copyObj.setSymbol(symbol);
        copyObj.setIconKey(iconKey);
        copyObj.setIconChanged(iconChanged);
        copyObj.setCSSStyle(cSSStyle);
        copyObj.setPercentComplete(percentComplete);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TNotify> vTNotifys = getTNotifys();
        if (vTNotifys != null)
        {
            for (int i = 0; i < vTNotifys.size(); i++)
            {
                TNotify obj =  vTNotifys.get(i);
                copyObj.addTNotify(obj.copy());
            }
        }
        else
        {
            copyObj.collTNotifys = null;
        }


        List<TProject> vTProjects = getTProjects();
        if (vTProjects != null)
        {
            for (int i = 0; i < vTProjects.size(); i++)
            {
                TProject obj =  vTProjects.get(i);
                copyObj.addTProject(obj.copy());
            }
        }
        else
        {
            copyObj.collTProjects = null;
        }


        List<TStateChange> vTStateChanges = getTStateChanges();
        if (vTStateChanges != null)
        {
            for (int i = 0; i < vTStateChanges.size(); i++)
            {
                TStateChange obj =  vTStateChanges.get(i);
                copyObj.addTStateChange(obj.copy());
            }
        }
        else
        {
            copyObj.collTStateChanges = null;
        }


        List<TWorkItem> vTWorkItems = getTWorkItems();
        if (vTWorkItems != null)
        {
            for (int i = 0; i < vTWorkItems.size(); i++)
            {
                TWorkItem obj =  vTWorkItems.get(i);
                copyObj.addTWorkItem(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkItems = null;
        }


        List<TPstate> vTPstates = getTPstates();
        if (vTPstates != null)
        {
            for (int i = 0; i < vTPstates.size(); i++)
            {
                TPstate obj =  vTPstates.get(i);
                copyObj.addTPstate(obj.copy());
            }
        }
        else
        {
            copyObj.collTPstates = null;
        }


        List<TWorkFlow> vTWorkFlowsRelatedByStateFrom = getTWorkFlowsRelatedByStateFrom();
        if (vTWorkFlowsRelatedByStateFrom != null)
        {
            for (int i = 0; i < vTWorkFlowsRelatedByStateFrom.size(); i++)
            {
                TWorkFlow obj =  vTWorkFlowsRelatedByStateFrom.get(i);
                copyObj.addTWorkFlowRelatedByStateFrom(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkFlowsRelatedByStateFrom = null;
        }


        List<TWorkFlow> vTWorkFlowsRelatedByStateTo = getTWorkFlowsRelatedByStateTo();
        if (vTWorkFlowsRelatedByStateTo != null)
        {
            for (int i = 0; i < vTWorkFlowsRelatedByStateTo.size(); i++)
            {
                TWorkFlow obj =  vTWorkFlowsRelatedByStateTo.get(i);
                copyObj.addTWorkFlowRelatedByStateTo(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkFlowsRelatedByStateTo = null;
        }


        List<TInitState> vTInitStates = getTInitStates();
        if (vTInitStates != null)
        {
            for (int i = 0; i < vTInitStates.size(); i++)
            {
                TInitState obj =  vTInitStates.get(i);
                copyObj.addTInitState(obj.copy());
            }
        }
        else
        {
            copyObj.collTInitStates = null;
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
    protected TState copyInto(TState copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setTooltip(tooltip);
        copyObj.setStateflag(stateflag);
        copyObj.setSortorder(sortorder);
        copyObj.setSymbol(symbol);
        copyObj.setIconKey(iconKey);
        copyObj.setIconChanged(iconChanged);
        copyObj.setCSSStyle(cSSStyle);
        copyObj.setPercentComplete(percentComplete);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TNotify> vTNotifys = getTNotifys(con);
        if (vTNotifys != null)
        {
            for (int i = 0; i < vTNotifys.size(); i++)
            {
                TNotify obj =  vTNotifys.get(i);
                copyObj.addTNotify(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTNotifys = null;
        }


        List<TProject> vTProjects = getTProjects(con);
        if (vTProjects != null)
        {
            for (int i = 0; i < vTProjects.size(); i++)
            {
                TProject obj =  vTProjects.get(i);
                copyObj.addTProject(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTProjects = null;
        }


        List<TStateChange> vTStateChanges = getTStateChanges(con);
        if (vTStateChanges != null)
        {
            for (int i = 0; i < vTStateChanges.size(); i++)
            {
                TStateChange obj =  vTStateChanges.get(i);
                copyObj.addTStateChange(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTStateChanges = null;
        }


        List<TWorkItem> vTWorkItems = getTWorkItems(con);
        if (vTWorkItems != null)
        {
            for (int i = 0; i < vTWorkItems.size(); i++)
            {
                TWorkItem obj =  vTWorkItems.get(i);
                copyObj.addTWorkItem(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkItems = null;
        }


        List<TPstate> vTPstates = getTPstates(con);
        if (vTPstates != null)
        {
            for (int i = 0; i < vTPstates.size(); i++)
            {
                TPstate obj =  vTPstates.get(i);
                copyObj.addTPstate(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPstates = null;
        }


        List<TWorkFlow> vTWorkFlowsRelatedByStateFrom = getTWorkFlowsRelatedByStateFrom(con);
        if (vTWorkFlowsRelatedByStateFrom != null)
        {
            for (int i = 0; i < vTWorkFlowsRelatedByStateFrom.size(); i++)
            {
                TWorkFlow obj =  vTWorkFlowsRelatedByStateFrom.get(i);
                copyObj.addTWorkFlowRelatedByStateFrom(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkFlowsRelatedByStateFrom = null;
        }


        List<TWorkFlow> vTWorkFlowsRelatedByStateTo = getTWorkFlowsRelatedByStateTo(con);
        if (vTWorkFlowsRelatedByStateTo != null)
        {
            for (int i = 0; i < vTWorkFlowsRelatedByStateTo.size(); i++)
            {
                TWorkFlow obj =  vTWorkFlowsRelatedByStateTo.get(i);
                copyObj.addTWorkFlowRelatedByStateTo(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkFlowsRelatedByStateTo = null;
        }


        List<TInitState> vTInitStates = getTInitStates(con);
        if (vTInitStates != null)
        {
            for (int i = 0; i < vTInitStates.size(); i++)
            {
                TInitState obj =  vTInitStates.get(i);
                copyObj.addTInitState(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTInitStates = null;
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
    public TStatePeer getPeer()
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
        return TStatePeer.getTableMap();
    }

  
    /**
     * Creates a TStateBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TStateBean with the contents of this object
     */
    public TStateBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TStateBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TStateBean with the contents of this object
     */
    public TStateBean getBean(IdentityMap createdBeans)
    {
        TStateBean result = (TStateBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TStateBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setTooltip(getTooltip());
        result.setStateflag(getStateflag());
        result.setSortorder(getSortorder());
        result.setSymbol(getSymbol());
        result.setIconKey(getIconKey());
        result.setIconChanged(getIconChanged());
        result.setCSSStyle(getCSSStyle());
        result.setPercentComplete(getPercentComplete());
        result.setUuid(getUuid());



        if (collTNotifys != null)
        {
            List<TNotifyBean> relatedBeans = new ArrayList<TNotifyBean>(collTNotifys.size());
            for (Iterator<TNotify> collTNotifysIt = collTNotifys.iterator(); collTNotifysIt.hasNext(); )
            {
                TNotify related = (TNotify) collTNotifysIt.next();
                TNotifyBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTNotifyBeans(relatedBeans);
        }


        if (collTProjects != null)
        {
            List<TProjectBean> relatedBeans = new ArrayList<TProjectBean>(collTProjects.size());
            for (Iterator<TProject> collTProjectsIt = collTProjects.iterator(); collTProjectsIt.hasNext(); )
            {
                TProject related = (TProject) collTProjectsIt.next();
                TProjectBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTProjectBeans(relatedBeans);
        }


        if (collTStateChanges != null)
        {
            List<TStateChangeBean> relatedBeans = new ArrayList<TStateChangeBean>(collTStateChanges.size());
            for (Iterator<TStateChange> collTStateChangesIt = collTStateChanges.iterator(); collTStateChangesIt.hasNext(); )
            {
                TStateChange related = (TStateChange) collTStateChangesIt.next();
                TStateChangeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTStateChangeBeans(relatedBeans);
        }


        if (collTWorkItems != null)
        {
            List<TWorkItemBean> relatedBeans = new ArrayList<TWorkItemBean>(collTWorkItems.size());
            for (Iterator<TWorkItem> collTWorkItemsIt = collTWorkItems.iterator(); collTWorkItemsIt.hasNext(); )
            {
                TWorkItem related = (TWorkItem) collTWorkItemsIt.next();
                TWorkItemBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkItemBeans(relatedBeans);
        }


        if (collTPstates != null)
        {
            List<TPstateBean> relatedBeans = new ArrayList<TPstateBean>(collTPstates.size());
            for (Iterator<TPstate> collTPstatesIt = collTPstates.iterator(); collTPstatesIt.hasNext(); )
            {
                TPstate related = (TPstate) collTPstatesIt.next();
                TPstateBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPstateBeans(relatedBeans);
        }


        if (collTWorkFlowsRelatedByStateFrom != null)
        {
            List<TWorkFlowBean> relatedBeans = new ArrayList<TWorkFlowBean>(collTWorkFlowsRelatedByStateFrom.size());
            for (Iterator<TWorkFlow> collTWorkFlowsRelatedByStateFromIt = collTWorkFlowsRelatedByStateFrom.iterator(); collTWorkFlowsRelatedByStateFromIt.hasNext(); )
            {
                TWorkFlow related = (TWorkFlow) collTWorkFlowsRelatedByStateFromIt.next();
                TWorkFlowBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkFlowBeansRelatedByStateFrom(relatedBeans);
        }


        if (collTWorkFlowsRelatedByStateTo != null)
        {
            List<TWorkFlowBean> relatedBeans = new ArrayList<TWorkFlowBean>(collTWorkFlowsRelatedByStateTo.size());
            for (Iterator<TWorkFlow> collTWorkFlowsRelatedByStateToIt = collTWorkFlowsRelatedByStateTo.iterator(); collTWorkFlowsRelatedByStateToIt.hasNext(); )
            {
                TWorkFlow related = (TWorkFlow) collTWorkFlowsRelatedByStateToIt.next();
                TWorkFlowBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkFlowBeansRelatedByStateTo(relatedBeans);
        }


        if (collTInitStates != null)
        {
            List<TInitStateBean> relatedBeans = new ArrayList<TInitStateBean>(collTInitStates.size());
            for (Iterator<TInitState> collTInitStatesIt = collTInitStates.iterator(); collTInitStatesIt.hasNext(); )
            {
                TInitState related = (TInitState) collTInitStatesIt.next();
                TInitStateBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTInitStateBeans(relatedBeans);
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




        if (aTBLOB != null)
        {
            TBLOBBean relatedBean = aTBLOB.getBean(createdBeans);
            result.setTBLOBBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TState with the contents
     * of a TStateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TStateBean which contents are used to create
     *        the resulting class
     * @return an instance of TState with the contents of bean
     */
    public static TState createTState(TStateBean bean)
        throws TorqueException
    {
        return createTState(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TState with the contents
     * of a TStateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TStateBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TState with the contents of bean
     */

    public static TState createTState(TStateBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TState result = (TState) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TState();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setTooltip(bean.getTooltip());
        result.setStateflag(bean.getStateflag());
        result.setSortorder(bean.getSortorder());
        result.setSymbol(bean.getSymbol());
        result.setIconKey(bean.getIconKey());
        result.setIconChanged(bean.getIconChanged());
        result.setCSSStyle(bean.getCSSStyle());
        result.setPercentComplete(bean.getPercentComplete());
        result.setUuid(bean.getUuid());



        {
            List<TNotifyBean> relatedBeans = bean.getTNotifyBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TNotifyBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TNotifyBean relatedBean =  relatedBeansIt.next();
                    TNotify related = TNotify.createTNotify(relatedBean, createdObjects);
                    result.addTNotifyFromBean(related);
                }
            }
        }


        {
            List<TProjectBean> relatedBeans = bean.getTProjectBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TProjectBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TProjectBean relatedBean =  relatedBeansIt.next();
                    TProject related = TProject.createTProject(relatedBean, createdObjects);
                    result.addTProjectFromBean(related);
                }
            }
        }


        {
            List<TStateChangeBean> relatedBeans = bean.getTStateChangeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TStateChangeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TStateChangeBean relatedBean =  relatedBeansIt.next();
                    TStateChange related = TStateChange.createTStateChange(relatedBean, createdObjects);
                    result.addTStateChangeFromBean(related);
                }
            }
        }


        {
            List<TWorkItemBean> relatedBeans = bean.getTWorkItemBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkItemBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkItemBean relatedBean =  relatedBeansIt.next();
                    TWorkItem related = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                    result.addTWorkItemFromBean(related);
                }
            }
        }


        {
            List<TPstateBean> relatedBeans = bean.getTPstateBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPstateBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPstateBean relatedBean =  relatedBeansIt.next();
                    TPstate related = TPstate.createTPstate(relatedBean, createdObjects);
                    result.addTPstateFromBean(related);
                }
            }
        }


        {
            List<TWorkFlowBean> relatedBeans = bean.getTWorkFlowBeansRelatedByStateFrom();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkFlowBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkFlowBean relatedBean =  relatedBeansIt.next();
                    TWorkFlow related = TWorkFlow.createTWorkFlow(relatedBean, createdObjects);
                    result.addTWorkFlowRelatedByStateFromFromBean(related);
                }
            }
        }


        {
            List<TWorkFlowBean> relatedBeans = bean.getTWorkFlowBeansRelatedByStateTo();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkFlowBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkFlowBean relatedBean =  relatedBeansIt.next();
                    TWorkFlow related = TWorkFlow.createTWorkFlow(relatedBean, createdObjects);
                    result.addTWorkFlowRelatedByStateToFromBean(related);
                }
            }
        }


        {
            List<TInitStateBean> relatedBeans = bean.getTInitStateBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TInitStateBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TInitStateBean relatedBean =  relatedBeansIt.next();
                    TInitState related = TInitState.createTInitState(relatedBean, createdObjects);
                    result.addTInitStateFromBean(related);
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
            TBLOBBean relatedBean = bean.getTBLOBBean();
            if (relatedBean != null)
            {
                TBLOB relatedObject = TBLOB.createTBLOB(relatedBean, createdObjects);
                result.setTBLOB(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TNotify object to this object.
     * through the TNotify foreign key attribute
     *
     * @param toAdd TNotify
     */
    protected void addTNotifyFromBean(TNotify toAdd)
    {
        initTNotifys();
        collTNotifys.add(toAdd);
    }


    /**
     * Method called to associate a TProject object to this object.
     * through the TProject foreign key attribute
     *
     * @param toAdd TProject
     */
    protected void addTProjectFromBean(TProject toAdd)
    {
        initTProjects();
        collTProjects.add(toAdd);
    }


    /**
     * Method called to associate a TStateChange object to this object.
     * through the TStateChange foreign key attribute
     *
     * @param toAdd TStateChange
     */
    protected void addTStateChangeFromBean(TStateChange toAdd)
    {
        initTStateChanges();
        collTStateChanges.add(toAdd);
    }


    /**
     * Method called to associate a TWorkItem object to this object.
     * through the TWorkItem foreign key attribute
     *
     * @param toAdd TWorkItem
     */
    protected void addTWorkItemFromBean(TWorkItem toAdd)
    {
        initTWorkItems();
        collTWorkItems.add(toAdd);
    }


    /**
     * Method called to associate a TPstate object to this object.
     * through the TPstate foreign key attribute
     *
     * @param toAdd TPstate
     */
    protected void addTPstateFromBean(TPstate toAdd)
    {
        initTPstates();
        collTPstates.add(toAdd);
    }


    /**
     * Method called to associate a TWorkFlow object to this object.
     * through the TWorkFlow foreign key attribute
     *
     * @param toAdd TWorkFlow
     */
    protected void addTWorkFlowRelatedByStateFromFromBean(TWorkFlow toAdd)
    {
        initTWorkFlowsRelatedByStateFrom();
        collTWorkFlowsRelatedByStateFrom.add(toAdd);
    }


    /**
     * Method called to associate a TWorkFlow object to this object.
     * through the TWorkFlow foreign key attribute
     *
     * @param toAdd TWorkFlow
     */
    protected void addTWorkFlowRelatedByStateToFromBean(TWorkFlow toAdd)
    {
        initTWorkFlowsRelatedByStateTo();
        collTWorkFlowsRelatedByStateTo.add(toAdd);
    }


    /**
     * Method called to associate a TInitState object to this object.
     * through the TInitState foreign key attribute
     *
     * @param toAdd TInitState
     */
    protected void addTInitStateFromBean(TInitState toAdd)
    {
        initTInitStates();
        collTInitStates.add(toAdd);
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
        str.append("TState:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Tooltip = ")
           .append(getTooltip())
           .append("\n");
        str.append("Stateflag = ")
           .append(getStateflag())
           .append("\n");
        str.append("Sortorder = ")
           .append(getSortorder())
           .append("\n");
        str.append("Symbol = ")
           .append(getSymbol())
           .append("\n");
        str.append("IconKey = ")
           .append(getIconKey())
           .append("\n");
        str.append("IconChanged = ")
           .append(getIconChanged())
           .append("\n");
        str.append("CSSStyle = ")
           .append(getCSSStyle())
           .append("\n");
        str.append("PercentComplete = ")
           .append(getPercentComplete())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
