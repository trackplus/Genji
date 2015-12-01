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




  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TSLABean;

import com.aurel.track.beans.TWorkflowActivityBean;
import com.aurel.track.beans.TEscalationEntryBean;
import com.aurel.track.beans.TOrgProjectSLABean;


/**
 * service level agreement
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TSLA
 */
public abstract class BaseTSLA extends TpBaseObject
{
    /** The Peer class */
    private static final TSLAPeer peer =
        new TSLAPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

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
                        .setSla(v);
            }
        }

        // update associated TEscalationEntry
        if (collTEscalationEntrys != null)
        {
            for (int i = 0; i < collTEscalationEntrys.size(); i++)
            {
                ((TEscalationEntry) collTEscalationEntrys.get(i))
                        .setSla(v);
            }
        }

        // update associated TOrgProjectSLA
        if (collTOrgProjectSLAs != null)
        {
            for (int i = 0; i < collTOrgProjectSLAs.size(); i++)
            {
                ((TOrgProjectSLA) collTOrgProjectSLAs.get(i))
                        .setSla(v);
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
        l.setTSLA((TSLA) this);
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
        l.setTSLA((TSLA) this);
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
     * Otherwise if this TSLA has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     * If this TSLA is new, it will return
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
                criteria.add(TWorkflowActivityPeer.SLA, getObjectID() );
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
                criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
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
     * Otherwise if this TSLA has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     * If this TSLA is new, it will return
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
                 criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
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
                 criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
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
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
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
                criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
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
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
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
                criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationEntryActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
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
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
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
                criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationExitActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
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
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
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
                criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTWorkflowStationRelatedByStationDoActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
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
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
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
                criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
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
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
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
                criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewMan(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
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
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
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
                criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTPersonRelatedByNewResp(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
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
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
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
                criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
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
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TWorkflowActivitys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
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
                criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowActivityPeer.SLA, getObjectID());
            if (!lastTWorkflowActivitysCriteria.equals(criteria))
            {
                collTWorkflowActivitys = TWorkflowActivityPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTWorkflowActivitysCriteria = criteria;

        return collTWorkflowActivitys;
    }





    /**
     * Collection to store aggregation of collTEscalationEntrys
     */
    protected List<TEscalationEntry> collTEscalationEntrys;

    /**
     * Temporary storage of collTEscalationEntrys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTEscalationEntrys()
    {
        if (collTEscalationEntrys == null)
        {
            collTEscalationEntrys = new ArrayList<TEscalationEntry>();
        }
    }


    /**
     * Method called to associate a TEscalationEntry object to this object
     * through the TEscalationEntry foreign key attribute
     *
     * @param l TEscalationEntry
     * @throws TorqueException
     */
    public void addTEscalationEntry(TEscalationEntry l) throws TorqueException
    {
        getTEscalationEntrys().add(l);
        l.setTSLA((TSLA) this);
    }

    /**
     * Method called to associate a TEscalationEntry object to this object
     * through the TEscalationEntry foreign key attribute using connection.
     *
     * @param l TEscalationEntry
     * @throws TorqueException
     */
    public void addTEscalationEntry(TEscalationEntry l, Connection con) throws TorqueException
    {
        getTEscalationEntrys(con).add(l);
        l.setTSLA((TSLA) this);
    }

    /**
     * The criteria used to select the current contents of collTEscalationEntrys
     */
    private Criteria lastTEscalationEntrysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEscalationEntrys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TEscalationEntry> getTEscalationEntrys()
        throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            collTEscalationEntrys = getTEscalationEntrys(new Criteria(10));
        }
        return collTEscalationEntrys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSLA has previously
     * been saved, it will retrieve related TEscalationEntrys from storage.
     * If this TSLA is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TEscalationEntry> getTEscalationEntrys(Criteria criteria) throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            if (isNew())
            {
               collTEscalationEntrys = new ArrayList<TEscalationEntry>();
            }
            else
            {
                criteria.add(TEscalationEntryPeer.SLA, getObjectID() );
                collTEscalationEntrys = TEscalationEntryPeer.doSelect(criteria);
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
                criteria.add(TEscalationEntryPeer.SLA, getObjectID());
                if (!lastTEscalationEntrysCriteria.equals(criteria))
                {
                    collTEscalationEntrys = TEscalationEntryPeer.doSelect(criteria);
                }
            }
        }
        lastTEscalationEntrysCriteria = criteria;

        return collTEscalationEntrys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEscalationEntrys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEscalationEntry> getTEscalationEntrys(Connection con) throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            collTEscalationEntrys = getTEscalationEntrys(new Criteria(10), con);
        }
        return collTEscalationEntrys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSLA has previously
     * been saved, it will retrieve related TEscalationEntrys from storage.
     * If this TSLA is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEscalationEntry> getTEscalationEntrys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            if (isNew())
            {
               collTEscalationEntrys = new ArrayList<TEscalationEntry>();
            }
            else
            {
                 criteria.add(TEscalationEntryPeer.SLA, getObjectID());
                 collTEscalationEntrys = TEscalationEntryPeer.doSelect(criteria, con);
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
                 criteria.add(TEscalationEntryPeer.SLA, getObjectID());
                 if (!lastTEscalationEntrysCriteria.equals(criteria))
                 {
                     collTEscalationEntrys = TEscalationEntryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTEscalationEntrysCriteria = criteria;

         return collTEscalationEntrys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TEscalationEntrys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
     */
    protected List<TEscalationEntry> getTEscalationEntrysJoinTSLA(Criteria criteria)
        throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            if (isNew())
            {
               collTEscalationEntrys = new ArrayList<TEscalationEntry>();
            }
            else
            {
                criteria.add(TEscalationEntryPeer.SLA, getObjectID());
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEscalationEntryPeer.SLA, getObjectID());
            if (!lastTEscalationEntrysCriteria.equals(criteria))
            {
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTSLA(criteria);
            }
        }
        lastTEscalationEntrysCriteria = criteria;

        return collTEscalationEntrys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TEscalationEntrys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
     */
    protected List<TEscalationEntry> getTEscalationEntrysJoinTPriority(Criteria criteria)
        throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            if (isNew())
            {
               collTEscalationEntrys = new ArrayList<TEscalationEntry>();
            }
            else
            {
                criteria.add(TEscalationEntryPeer.SLA, getObjectID());
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEscalationEntryPeer.SLA, getObjectID());
            if (!lastTEscalationEntrysCriteria.equals(criteria))
            {
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTPriority(criteria);
            }
        }
        lastTEscalationEntrysCriteria = criteria;

        return collTEscalationEntrys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TEscalationEntrys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
     */
    protected List<TEscalationEntry> getTEscalationEntrysJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            if (isNew())
            {
               collTEscalationEntrys = new ArrayList<TEscalationEntry>();
            }
            else
            {
                criteria.add(TEscalationEntryPeer.SLA, getObjectID());
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEscalationEntryPeer.SLA, getObjectID());
            if (!lastTEscalationEntrysCriteria.equals(criteria))
            {
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTEscalationEntrysCriteria = criteria;

        return collTEscalationEntrys;
    }





    /**
     * Collection to store aggregation of collTOrgProjectSLAs
     */
    protected List<TOrgProjectSLA> collTOrgProjectSLAs;

    /**
     * Temporary storage of collTOrgProjectSLAs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTOrgProjectSLAs()
    {
        if (collTOrgProjectSLAs == null)
        {
            collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
        }
    }


    /**
     * Method called to associate a TOrgProjectSLA object to this object
     * through the TOrgProjectSLA foreign key attribute
     *
     * @param l TOrgProjectSLA
     * @throws TorqueException
     */
    public void addTOrgProjectSLA(TOrgProjectSLA l) throws TorqueException
    {
        getTOrgProjectSLAs().add(l);
        l.setTSLA((TSLA) this);
    }

    /**
     * Method called to associate a TOrgProjectSLA object to this object
     * through the TOrgProjectSLA foreign key attribute using connection.
     *
     * @param l TOrgProjectSLA
     * @throws TorqueException
     */
    public void addTOrgProjectSLA(TOrgProjectSLA l, Connection con) throws TorqueException
    {
        getTOrgProjectSLAs(con).add(l);
        l.setTSLA((TSLA) this);
    }

    /**
     * The criteria used to select the current contents of collTOrgProjectSLAs
     */
    private Criteria lastTOrgProjectSLAsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOrgProjectSLAs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TOrgProjectSLA> getTOrgProjectSLAs()
        throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            collTOrgProjectSLAs = getTOrgProjectSLAs(new Criteria(10));
        }
        return collTOrgProjectSLAs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSLA has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     * If this TSLA is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TOrgProjectSLA> getTOrgProjectSLAs(Criteria criteria) throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            if (isNew())
            {
               collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
            }
            else
            {
                criteria.add(TOrgProjectSLAPeer.SLA, getObjectID() );
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelect(criteria);
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
                criteria.add(TOrgProjectSLAPeer.SLA, getObjectID());
                if (!lastTOrgProjectSLAsCriteria.equals(criteria))
                {
                    collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelect(criteria);
                }
            }
        }
        lastTOrgProjectSLAsCriteria = criteria;

        return collTOrgProjectSLAs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOrgProjectSLAs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOrgProjectSLA> getTOrgProjectSLAs(Connection con) throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            collTOrgProjectSLAs = getTOrgProjectSLAs(new Criteria(10), con);
        }
        return collTOrgProjectSLAs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSLA has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     * If this TSLA is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOrgProjectSLA> getTOrgProjectSLAs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            if (isNew())
            {
               collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
            }
            else
            {
                 criteria.add(TOrgProjectSLAPeer.SLA, getObjectID());
                 collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelect(criteria, con);
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
                 criteria.add(TOrgProjectSLAPeer.SLA, getObjectID());
                 if (!lastTOrgProjectSLAsCriteria.equals(criteria))
                 {
                     collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTOrgProjectSLAsCriteria = criteria;

         return collTOrgProjectSLAs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
     */
    protected List<TOrgProjectSLA> getTOrgProjectSLAsJoinTDepartment(Criteria criteria)
        throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            if (isNew())
            {
               collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
            }
            else
            {
                criteria.add(TOrgProjectSLAPeer.SLA, getObjectID());
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTDepartment(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOrgProjectSLAPeer.SLA, getObjectID());
            if (!lastTOrgProjectSLAsCriteria.equals(criteria))
            {
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTDepartment(criteria);
            }
        }
        lastTOrgProjectSLAsCriteria = criteria;

        return collTOrgProjectSLAs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
     */
    protected List<TOrgProjectSLA> getTOrgProjectSLAsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            if (isNew())
            {
               collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
            }
            else
            {
                criteria.add(TOrgProjectSLAPeer.SLA, getObjectID());
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOrgProjectSLAPeer.SLA, getObjectID());
            if (!lastTOrgProjectSLAsCriteria.equals(criteria))
            {
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTOrgProjectSLAsCriteria = criteria;

        return collTOrgProjectSLAs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TSLA is new, it will return
     * an empty collection; or if this TSLA has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TSLA.
     */
    protected List<TOrgProjectSLA> getTOrgProjectSLAsJoinTSLA(Criteria criteria)
        throws TorqueException
    {
        if (collTOrgProjectSLAs == null)
        {
            if (isNew())
            {
               collTOrgProjectSLAs = new ArrayList<TOrgProjectSLA>();
            }
            else
            {
                criteria.add(TOrgProjectSLAPeer.SLA, getObjectID());
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOrgProjectSLAPeer.SLA, getObjectID());
            if (!lastTOrgProjectSLAsCriteria.equals(criteria))
            {
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTSLA(criteria);
            }
        }
        lastTOrgProjectSLAsCriteria = criteria;

        return collTOrgProjectSLAs;
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
        if (name.equals(TSLAPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TSLAPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TSLAPeer.TPUUID))
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
      if (TSLAPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TSLAPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TSLAPeer.TPUUID.equals(name))
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
        save(TSLAPeer.DATABASE_NAME);
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
                    TSLAPeer.doInsert((TSLA) this, con);
                    setNew(false);
                }
                else
                {
                    TSLAPeer.doUpdate((TSLA) this, con);
                }
            }


            if (collTWorkflowActivitys != null)
            {
                for (int i = 0; i < collTWorkflowActivitys.size(); i++)
                {
                    ((TWorkflowActivity) collTWorkflowActivitys.get(i)).save(con);
                }
            }

            if (collTEscalationEntrys != null)
            {
                for (int i = 0; i < collTEscalationEntrys.size(); i++)
                {
                    ((TEscalationEntry) collTEscalationEntrys.get(i)).save(con);
                }
            }

            if (collTOrgProjectSLAs != null)
            {
                for (int i = 0; i < collTOrgProjectSLAs.size(); i++)
                {
                    ((TOrgProjectSLA) collTOrgProjectSLAs.get(i)).save(con);
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
    public TSLA copy() throws TorqueException
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
    public TSLA copy(Connection con) throws TorqueException
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
    public TSLA copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TSLA(), deepcopy);
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
    public TSLA copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TSLA(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TSLA copyInto(TSLA copyObj) throws TorqueException
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
    protected TSLA copyInto(TSLA copyObj, Connection con) throws TorqueException
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
    protected TSLA copyInto(TSLA copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
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


        List<TEscalationEntry> vTEscalationEntrys = getTEscalationEntrys();
        if (vTEscalationEntrys != null)
        {
            for (int i = 0; i < vTEscalationEntrys.size(); i++)
            {
                TEscalationEntry obj =  vTEscalationEntrys.get(i);
                copyObj.addTEscalationEntry(obj.copy());
            }
        }
        else
        {
            copyObj.collTEscalationEntrys = null;
        }


        List<TOrgProjectSLA> vTOrgProjectSLAs = getTOrgProjectSLAs();
        if (vTOrgProjectSLAs != null)
        {
            for (int i = 0; i < vTOrgProjectSLAs.size(); i++)
            {
                TOrgProjectSLA obj =  vTOrgProjectSLAs.get(i);
                copyObj.addTOrgProjectSLA(obj.copy());
            }
        }
        else
        {
            copyObj.collTOrgProjectSLAs = null;
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
    protected TSLA copyInto(TSLA copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
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


        List<TEscalationEntry> vTEscalationEntrys = getTEscalationEntrys(con);
        if (vTEscalationEntrys != null)
        {
            for (int i = 0; i < vTEscalationEntrys.size(); i++)
            {
                TEscalationEntry obj =  vTEscalationEntrys.get(i);
                copyObj.addTEscalationEntry(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTEscalationEntrys = null;
        }


        List<TOrgProjectSLA> vTOrgProjectSLAs = getTOrgProjectSLAs(con);
        if (vTOrgProjectSLAs != null)
        {
            for (int i = 0; i < vTOrgProjectSLAs.size(); i++)
            {
                TOrgProjectSLA obj =  vTOrgProjectSLAs.get(i);
                copyObj.addTOrgProjectSLA(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTOrgProjectSLAs = null;
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
    public TSLAPeer getPeer()
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
        return TSLAPeer.getTableMap();
    }

  
    /**
     * Creates a TSLABean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TSLABean with the contents of this object
     */
    public TSLABean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TSLABean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TSLABean with the contents of this object
     */
    public TSLABean getBean(IdentityMap createdBeans)
    {
        TSLABean result = (TSLABean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TSLABean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
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


        if (collTEscalationEntrys != null)
        {
            List<TEscalationEntryBean> relatedBeans = new ArrayList<TEscalationEntryBean>(collTEscalationEntrys.size());
            for (Iterator<TEscalationEntry> collTEscalationEntrysIt = collTEscalationEntrys.iterator(); collTEscalationEntrysIt.hasNext(); )
            {
                TEscalationEntry related = (TEscalationEntry) collTEscalationEntrysIt.next();
                TEscalationEntryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTEscalationEntryBeans(relatedBeans);
        }


        if (collTOrgProjectSLAs != null)
        {
            List<TOrgProjectSLABean> relatedBeans = new ArrayList<TOrgProjectSLABean>(collTOrgProjectSLAs.size());
            for (Iterator<TOrgProjectSLA> collTOrgProjectSLAsIt = collTOrgProjectSLAs.iterator(); collTOrgProjectSLAsIt.hasNext(); )
            {
                TOrgProjectSLA related = (TOrgProjectSLA) collTOrgProjectSLAsIt.next();
                TOrgProjectSLABean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTOrgProjectSLABeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TSLA with the contents
     * of a TSLABean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TSLABean which contents are used to create
     *        the resulting class
     * @return an instance of TSLA with the contents of bean
     */
    public static TSLA createTSLA(TSLABean bean)
        throws TorqueException
    {
        return createTSLA(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TSLA with the contents
     * of a TSLABean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TSLABean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TSLA with the contents of bean
     */

    public static TSLA createTSLA(TSLABean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TSLA result = (TSLA) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TSLA();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
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
            List<TEscalationEntryBean> relatedBeans = bean.getTEscalationEntryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TEscalationEntryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TEscalationEntryBean relatedBean =  relatedBeansIt.next();
                    TEscalationEntry related = TEscalationEntry.createTEscalationEntry(relatedBean, createdObjects);
                    result.addTEscalationEntryFromBean(related);
                }
            }
        }


        {
            List<TOrgProjectSLABean> relatedBeans = bean.getTOrgProjectSLABeans();
            if (relatedBeans != null)
            {
                for (Iterator<TOrgProjectSLABean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TOrgProjectSLABean relatedBean =  relatedBeansIt.next();
                    TOrgProjectSLA related = TOrgProjectSLA.createTOrgProjectSLA(relatedBean, createdObjects);
                    result.addTOrgProjectSLAFromBean(related);
                }
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
     * Method called to associate a TEscalationEntry object to this object.
     * through the TEscalationEntry foreign key attribute
     *
     * @param toAdd TEscalationEntry
     */
    protected void addTEscalationEntryFromBean(TEscalationEntry toAdd)
    {
        initTEscalationEntrys();
        collTEscalationEntrys.add(toAdd);
    }


    /**
     * Method called to associate a TOrgProjectSLA object to this object.
     * through the TOrgProjectSLA foreign key attribute
     *
     * @param toAdd TOrgProjectSLA
     */
    protected void addTOrgProjectSLAFromBean(TOrgProjectSLA toAdd)
    {
        initTOrgProjectSLAs();
        collTOrgProjectSLAs.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TSLA:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
