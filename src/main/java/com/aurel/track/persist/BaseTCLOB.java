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
import com.aurel.track.beans.TCLOBBean;

import com.aurel.track.beans.TEventBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TLastExecutedQueryBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TCLOB
 */
public abstract class BaseTCLOB extends TpBaseObject
{
    /** The Peer class */
    private static final TCLOBPeer peer =
        new TCLOBPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the cLOBValue field */
    private String cLOBValue;

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



        // update associated TEvent
        if (collTEvents != null)
        {
            for (int i = 0; i < collTEvents.size(); i++)
            {
                ((TEvent) collTEvents.get(i))
                        .setEventScript(v);
            }
        }

        // update associated TQueryRepository
        if (collTQueryRepositorys != null)
        {
            for (int i = 0; i < collTQueryRepositorys.size(); i++)
            {
                ((TQueryRepository) collTQueryRepositorys.get(i))
                        .setQueryKey(v);
            }
        }

        // update associated TLastExecutedQuery
        if (collTLastExecutedQuerys != null)
        {
            for (int i = 0; i < collTLastExecutedQuerys.size(); i++)
            {
                ((TLastExecutedQuery) collTLastExecutedQuerys.get(i))
                        .setQueryClob(v);
            }
        }
    }

    /**
     * Get the CLOBValue
     *
     * @return String
     */
    public String getCLOBValue()
    {
        return cLOBValue;
    }


    /**
     * Set the value of CLOBValue
     *
     * @param v new value
     */
    public void setCLOBValue(String v) 
    {

        if (!ObjectUtils.equals(this.cLOBValue, v))
        {
            this.cLOBValue = v;
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
     * Collection to store aggregation of collTEvents
     */
    protected List<TEvent> collTEvents;

    /**
     * Temporary storage of collTEvents to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTEvents()
    {
        if (collTEvents == null)
        {
            collTEvents = new ArrayList<TEvent>();
        }
    }


    /**
     * Method called to associate a TEvent object to this object
     * through the TEvent foreign key attribute
     *
     * @param l TEvent
     * @throws TorqueException
     */
    public void addTEvent(TEvent l) throws TorqueException
    {
        getTEvents().add(l);
        l.setTCLOB((TCLOB) this);
    }

    /**
     * Method called to associate a TEvent object to this object
     * through the TEvent foreign key attribute using connection.
     *
     * @param l TEvent
     * @throws TorqueException
     */
    public void addTEvent(TEvent l, Connection con) throws TorqueException
    {
        getTEvents(con).add(l);
        l.setTCLOB((TCLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTEvents
     */
    private Criteria lastTEventsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEvents(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TEvent> getTEvents()
        throws TorqueException
    {
        if (collTEvents == null)
        {
            collTEvents = getTEvents(new Criteria(10));
        }
        return collTEvents;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB has previously
     * been saved, it will retrieve related TEvents from storage.
     * If this TCLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TEvent> getTEvents(Criteria criteria) throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.EVENTSCRIPT, getObjectID() );
                collTEvents = TEventPeer.doSelect(criteria);
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
                criteria.add(TEventPeer.EVENTSCRIPT, getObjectID());
                if (!lastTEventsCriteria.equals(criteria))
                {
                    collTEvents = TEventPeer.doSelect(criteria);
                }
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEvents(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEvent> getTEvents(Connection con) throws TorqueException
    {
        if (collTEvents == null)
        {
            collTEvents = getTEvents(new Criteria(10), con);
        }
        return collTEvents;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB has previously
     * been saved, it will retrieve related TEvents from storage.
     * If this TCLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEvent> getTEvents(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                 criteria.add(TEventPeer.EVENTSCRIPT, getObjectID());
                 collTEvents = TEventPeer.doSelect(criteria, con);
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
                 criteria.add(TEventPeer.EVENTSCRIPT, getObjectID());
                 if (!lastTEventsCriteria.equals(criteria))
                 {
                     collTEvents = TEventPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTEventsCriteria = criteria;

         return collTEvents;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB is new, it will return
     * an empty collection; or if this TCLOB has previously
     * been saved, it will retrieve related TEvents from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCLOB.
     */
    protected List<TEvent> getTEventsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.EVENTSCRIPT, getObjectID());
                collTEvents = TEventPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEventPeer.EVENTSCRIPT, getObjectID());
            if (!lastTEventsCriteria.equals(criteria))
            {
                collTEvents = TEventPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB is new, it will return
     * an empty collection; or if this TCLOB has previously
     * been saved, it will retrieve related TEvents from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCLOB.
     */
    protected List<TEvent> getTEventsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.EVENTSCRIPT, getObjectID());
                collTEvents = TEventPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEventPeer.EVENTSCRIPT, getObjectID());
            if (!lastTEventsCriteria.equals(criteria))
            {
                collTEvents = TEventPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB is new, it will return
     * an empty collection; or if this TCLOB has previously
     * been saved, it will retrieve related TEvents from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCLOB.
     */
    protected List<TEvent> getTEventsJoinTCLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTEvents == null)
        {
            if (isNew())
            {
               collTEvents = new ArrayList<TEvent>();
            }
            else
            {
                criteria.add(TEventPeer.EVENTSCRIPT, getObjectID());
                collTEvents = TEventPeer.doSelectJoinTCLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEventPeer.EVENTSCRIPT, getObjectID());
            if (!lastTEventsCriteria.equals(criteria))
            {
                collTEvents = TEventPeer.doSelectJoinTCLOB(criteria);
            }
        }
        lastTEventsCriteria = criteria;

        return collTEvents;
    }





    /**
     * Collection to store aggregation of collTQueryRepositorys
     */
    protected List<TQueryRepository> collTQueryRepositorys;

    /**
     * Temporary storage of collTQueryRepositorys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTQueryRepositorys()
    {
        if (collTQueryRepositorys == null)
        {
            collTQueryRepositorys = new ArrayList<TQueryRepository>();
        }
    }


    /**
     * Method called to associate a TQueryRepository object to this object
     * through the TQueryRepository foreign key attribute
     *
     * @param l TQueryRepository
     * @throws TorqueException
     */
    public void addTQueryRepository(TQueryRepository l) throws TorqueException
    {
        getTQueryRepositorys().add(l);
        l.setTCLOB((TCLOB) this);
    }

    /**
     * Method called to associate a TQueryRepository object to this object
     * through the TQueryRepository foreign key attribute using connection.
     *
     * @param l TQueryRepository
     * @throws TorqueException
     */
    public void addTQueryRepository(TQueryRepository l, Connection con) throws TorqueException
    {
        getTQueryRepositorys(con).add(l);
        l.setTCLOB((TCLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTQueryRepositorys
     */
    private Criteria lastTQueryRepositorysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTQueryRepositorys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TQueryRepository> getTQueryRepositorys()
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            collTQueryRepositorys = getTQueryRepositorys(new Criteria(10));
        }
        return collTQueryRepositorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     * If this TCLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TQueryRepository> getTQueryRepositorys(Criteria criteria) throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID() );
                collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria);
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
                criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID());
                if (!lastTQueryRepositorysCriteria.equals(criteria))
                {
                    collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria);
                }
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTQueryRepositorys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TQueryRepository> getTQueryRepositorys(Connection con) throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            collTQueryRepositorys = getTQueryRepositorys(new Criteria(10), con);
        }
        return collTQueryRepositorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     * If this TCLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TQueryRepository> getTQueryRepositorys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                 criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID());
                 collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria, con);
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
                 criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID());
                 if (!lastTQueryRepositorysCriteria.equals(criteria))
                 {
                     collTQueryRepositorys = TQueryRepositoryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTQueryRepositorysCriteria = criteria;

         return collTQueryRepositorys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB is new, it will return
     * an empty collection; or if this TCLOB has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCLOB.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB is new, it will return
     * an empty collection; or if this TCLOB has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCLOB.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB is new, it will return
     * an empty collection; or if this TCLOB has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCLOB.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTCLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTCLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTCLOB(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB is new, it will return
     * an empty collection; or if this TCLOB has previously
     * been saved, it will retrieve related TQueryRepositorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCLOB.
     */
    protected List<TQueryRepository> getTQueryRepositorysJoinTFilterCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTQueryRepositorys == null)
        {
            if (isNew())
            {
               collTQueryRepositorys = new ArrayList<TQueryRepository>();
            }
            else
            {
                criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID());
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTFilterCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TQueryRepositoryPeer.QUERYKEY, getObjectID());
            if (!lastTQueryRepositorysCriteria.equals(criteria))
            {
                collTQueryRepositorys = TQueryRepositoryPeer.doSelectJoinTFilterCategory(criteria);
            }
        }
        lastTQueryRepositorysCriteria = criteria;

        return collTQueryRepositorys;
    }





    /**
     * Collection to store aggregation of collTLastExecutedQuerys
     */
    protected List<TLastExecutedQuery> collTLastExecutedQuerys;

    /**
     * Temporary storage of collTLastExecutedQuerys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTLastExecutedQuerys()
    {
        if (collTLastExecutedQuerys == null)
        {
            collTLastExecutedQuerys = new ArrayList<TLastExecutedQuery>();
        }
    }


    /**
     * Method called to associate a TLastExecutedQuery object to this object
     * through the TLastExecutedQuery foreign key attribute
     *
     * @param l TLastExecutedQuery
     * @throws TorqueException
     */
    public void addTLastExecutedQuery(TLastExecutedQuery l) throws TorqueException
    {
        getTLastExecutedQuerys().add(l);
        l.setTCLOB((TCLOB) this);
    }

    /**
     * Method called to associate a TLastExecutedQuery object to this object
     * through the TLastExecutedQuery foreign key attribute using connection.
     *
     * @param l TLastExecutedQuery
     * @throws TorqueException
     */
    public void addTLastExecutedQuery(TLastExecutedQuery l, Connection con) throws TorqueException
    {
        getTLastExecutedQuerys(con).add(l);
        l.setTCLOB((TCLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTLastExecutedQuerys
     */
    private Criteria lastTLastExecutedQuerysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLastExecutedQuerys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TLastExecutedQuery> getTLastExecutedQuerys()
        throws TorqueException
    {
        if (collTLastExecutedQuerys == null)
        {
            collTLastExecutedQuerys = getTLastExecutedQuerys(new Criteria(10));
        }
        return collTLastExecutedQuerys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB has previously
     * been saved, it will retrieve related TLastExecutedQuerys from storage.
     * If this TCLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TLastExecutedQuery> getTLastExecutedQuerys(Criteria criteria) throws TorqueException
    {
        if (collTLastExecutedQuerys == null)
        {
            if (isNew())
            {
               collTLastExecutedQuerys = new ArrayList<TLastExecutedQuery>();
            }
            else
            {
                criteria.add(TLastExecutedQueryPeer.QUERYCLOB, getObjectID() );
                collTLastExecutedQuerys = TLastExecutedQueryPeer.doSelect(criteria);
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
                criteria.add(TLastExecutedQueryPeer.QUERYCLOB, getObjectID());
                if (!lastTLastExecutedQuerysCriteria.equals(criteria))
                {
                    collTLastExecutedQuerys = TLastExecutedQueryPeer.doSelect(criteria);
                }
            }
        }
        lastTLastExecutedQuerysCriteria = criteria;

        return collTLastExecutedQuerys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLastExecutedQuerys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TLastExecutedQuery> getTLastExecutedQuerys(Connection con) throws TorqueException
    {
        if (collTLastExecutedQuerys == null)
        {
            collTLastExecutedQuerys = getTLastExecutedQuerys(new Criteria(10), con);
        }
        return collTLastExecutedQuerys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB has previously
     * been saved, it will retrieve related TLastExecutedQuerys from storage.
     * If this TCLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TLastExecutedQuery> getTLastExecutedQuerys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTLastExecutedQuerys == null)
        {
            if (isNew())
            {
               collTLastExecutedQuerys = new ArrayList<TLastExecutedQuery>();
            }
            else
            {
                 criteria.add(TLastExecutedQueryPeer.QUERYCLOB, getObjectID());
                 collTLastExecutedQuerys = TLastExecutedQueryPeer.doSelect(criteria, con);
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
                 criteria.add(TLastExecutedQueryPeer.QUERYCLOB, getObjectID());
                 if (!lastTLastExecutedQuerysCriteria.equals(criteria))
                 {
                     collTLastExecutedQuerys = TLastExecutedQueryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTLastExecutedQuerysCriteria = criteria;

         return collTLastExecutedQuerys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB is new, it will return
     * an empty collection; or if this TCLOB has previously
     * been saved, it will retrieve related TLastExecutedQuerys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCLOB.
     */
    protected List<TLastExecutedQuery> getTLastExecutedQuerysJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTLastExecutedQuerys == null)
        {
            if (isNew())
            {
               collTLastExecutedQuerys = new ArrayList<TLastExecutedQuery>();
            }
            else
            {
                criteria.add(TLastExecutedQueryPeer.QUERYCLOB, getObjectID());
                collTLastExecutedQuerys = TLastExecutedQueryPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TLastExecutedQueryPeer.QUERYCLOB, getObjectID());
            if (!lastTLastExecutedQuerysCriteria.equals(criteria))
            {
                collTLastExecutedQuerys = TLastExecutedQueryPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTLastExecutedQuerysCriteria = criteria;

        return collTLastExecutedQuerys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCLOB is new, it will return
     * an empty collection; or if this TCLOB has previously
     * been saved, it will retrieve related TLastExecutedQuerys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCLOB.
     */
    protected List<TLastExecutedQuery> getTLastExecutedQuerysJoinTCLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTLastExecutedQuerys == null)
        {
            if (isNew())
            {
               collTLastExecutedQuerys = new ArrayList<TLastExecutedQuery>();
            }
            else
            {
                criteria.add(TLastExecutedQueryPeer.QUERYCLOB, getObjectID());
                collTLastExecutedQuerys = TLastExecutedQueryPeer.doSelectJoinTCLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TLastExecutedQueryPeer.QUERYCLOB, getObjectID());
            if (!lastTLastExecutedQuerysCriteria.equals(criteria))
            {
                collTLastExecutedQuerys = TLastExecutedQueryPeer.doSelectJoinTCLOB(criteria);
            }
        }
        lastTLastExecutedQuerysCriteria = criteria;

        return collTLastExecutedQuerys;
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
            fieldNames.add("CLOBValue");
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
        if (name.equals("CLOBValue"))
        {
            return getCLOBValue();
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
        if (name.equals("CLOBValue"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCLOBValue((String) value);
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
        if (name.equals(TCLOBPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TCLOBPeer.CLOBVALUE))
        {
            return getCLOBValue();
        }
        if (name.equals(TCLOBPeer.TPUUID))
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
      if (TCLOBPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TCLOBPeer.CLOBVALUE.equals(name))
        {
            return setByName("CLOBValue", value);
        }
      if (TCLOBPeer.TPUUID.equals(name))
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
            return getCLOBValue();
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
            return setByName("CLOBValue", value);
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
        save(TCLOBPeer.DATABASE_NAME);
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
                    TCLOBPeer.doInsert((TCLOB) this, con);
                    setNew(false);
                }
                else
                {
                    TCLOBPeer.doUpdate((TCLOB) this, con);
                }
            }


            if (collTEvents != null)
            {
                for (int i = 0; i < collTEvents.size(); i++)
                {
                    ((TEvent) collTEvents.get(i)).save(con);
                }
            }

            if (collTQueryRepositorys != null)
            {
                for (int i = 0; i < collTQueryRepositorys.size(); i++)
                {
                    ((TQueryRepository) collTQueryRepositorys.get(i)).save(con);
                }
            }

            if (collTLastExecutedQuerys != null)
            {
                for (int i = 0; i < collTLastExecutedQuerys.size(); i++)
                {
                    ((TLastExecutedQuery) collTLastExecutedQuerys.get(i)).save(con);
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
    public TCLOB copy() throws TorqueException
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
    public TCLOB copy(Connection con) throws TorqueException
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
    public TCLOB copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TCLOB(), deepcopy);
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
    public TCLOB copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TCLOB(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TCLOB copyInto(TCLOB copyObj) throws TorqueException
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
    protected TCLOB copyInto(TCLOB copyObj, Connection con) throws TorqueException
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
    protected TCLOB copyInto(TCLOB copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setCLOBValue(cLOBValue);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TEvent> vTEvents = getTEvents();
        if (vTEvents != null)
        {
            for (int i = 0; i < vTEvents.size(); i++)
            {
                TEvent obj =  vTEvents.get(i);
                copyObj.addTEvent(obj.copy());
            }
        }
        else
        {
            copyObj.collTEvents = null;
        }


        List<TQueryRepository> vTQueryRepositorys = getTQueryRepositorys();
        if (vTQueryRepositorys != null)
        {
            for (int i = 0; i < vTQueryRepositorys.size(); i++)
            {
                TQueryRepository obj =  vTQueryRepositorys.get(i);
                copyObj.addTQueryRepository(obj.copy());
            }
        }
        else
        {
            copyObj.collTQueryRepositorys = null;
        }


        List<TLastExecutedQuery> vTLastExecutedQuerys = getTLastExecutedQuerys();
        if (vTLastExecutedQuerys != null)
        {
            for (int i = 0; i < vTLastExecutedQuerys.size(); i++)
            {
                TLastExecutedQuery obj =  vTLastExecutedQuerys.get(i);
                copyObj.addTLastExecutedQuery(obj.copy());
            }
        }
        else
        {
            copyObj.collTLastExecutedQuerys = null;
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
    protected TCLOB copyInto(TCLOB copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setCLOBValue(cLOBValue);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TEvent> vTEvents = getTEvents(con);
        if (vTEvents != null)
        {
            for (int i = 0; i < vTEvents.size(); i++)
            {
                TEvent obj =  vTEvents.get(i);
                copyObj.addTEvent(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTEvents = null;
        }


        List<TQueryRepository> vTQueryRepositorys = getTQueryRepositorys(con);
        if (vTQueryRepositorys != null)
        {
            for (int i = 0; i < vTQueryRepositorys.size(); i++)
            {
                TQueryRepository obj =  vTQueryRepositorys.get(i);
                copyObj.addTQueryRepository(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTQueryRepositorys = null;
        }


        List<TLastExecutedQuery> vTLastExecutedQuerys = getTLastExecutedQuerys(con);
        if (vTLastExecutedQuerys != null)
        {
            for (int i = 0; i < vTLastExecutedQuerys.size(); i++)
            {
                TLastExecutedQuery obj =  vTLastExecutedQuerys.get(i);
                copyObj.addTLastExecutedQuery(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTLastExecutedQuerys = null;
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
    public TCLOBPeer getPeer()
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
        return TCLOBPeer.getTableMap();
    }

  
    /**
     * Creates a TCLOBBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TCLOBBean with the contents of this object
     */
    public TCLOBBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TCLOBBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TCLOBBean with the contents of this object
     */
    public TCLOBBean getBean(IdentityMap createdBeans)
    {
        TCLOBBean result = (TCLOBBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TCLOBBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setCLOBValue(getCLOBValue());
        result.setUuid(getUuid());



        if (collTEvents != null)
        {
            List<TEventBean> relatedBeans = new ArrayList<TEventBean>(collTEvents.size());
            for (Iterator<TEvent> collTEventsIt = collTEvents.iterator(); collTEventsIt.hasNext(); )
            {
                TEvent related = (TEvent) collTEventsIt.next();
                TEventBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTEventBeans(relatedBeans);
        }


        if (collTQueryRepositorys != null)
        {
            List<TQueryRepositoryBean> relatedBeans = new ArrayList<TQueryRepositoryBean>(collTQueryRepositorys.size());
            for (Iterator<TQueryRepository> collTQueryRepositorysIt = collTQueryRepositorys.iterator(); collTQueryRepositorysIt.hasNext(); )
            {
                TQueryRepository related = (TQueryRepository) collTQueryRepositorysIt.next();
                TQueryRepositoryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTQueryRepositoryBeans(relatedBeans);
        }


        if (collTLastExecutedQuerys != null)
        {
            List<TLastExecutedQueryBean> relatedBeans = new ArrayList<TLastExecutedQueryBean>(collTLastExecutedQuerys.size());
            for (Iterator<TLastExecutedQuery> collTLastExecutedQuerysIt = collTLastExecutedQuerys.iterator(); collTLastExecutedQuerysIt.hasNext(); )
            {
                TLastExecutedQuery related = (TLastExecutedQuery) collTLastExecutedQuerysIt.next();
                TLastExecutedQueryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTLastExecutedQueryBeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TCLOB with the contents
     * of a TCLOBBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TCLOBBean which contents are used to create
     *        the resulting class
     * @return an instance of TCLOB with the contents of bean
     */
    public static TCLOB createTCLOB(TCLOBBean bean)
        throws TorqueException
    {
        return createTCLOB(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TCLOB with the contents
     * of a TCLOBBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TCLOBBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TCLOB with the contents of bean
     */

    public static TCLOB createTCLOB(TCLOBBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TCLOB result = (TCLOB) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TCLOB();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setCLOBValue(bean.getCLOBValue());
        result.setUuid(bean.getUuid());



        {
            List<TEventBean> relatedBeans = bean.getTEventBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TEventBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TEventBean relatedBean =  relatedBeansIt.next();
                    TEvent related = TEvent.createTEvent(relatedBean, createdObjects);
                    result.addTEventFromBean(related);
                }
            }
        }


        {
            List<TQueryRepositoryBean> relatedBeans = bean.getTQueryRepositoryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TQueryRepositoryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TQueryRepositoryBean relatedBean =  relatedBeansIt.next();
                    TQueryRepository related = TQueryRepository.createTQueryRepository(relatedBean, createdObjects);
                    result.addTQueryRepositoryFromBean(related);
                }
            }
        }


        {
            List<TLastExecutedQueryBean> relatedBeans = bean.getTLastExecutedQueryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TLastExecutedQueryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TLastExecutedQueryBean relatedBean =  relatedBeansIt.next();
                    TLastExecutedQuery related = TLastExecutedQuery.createTLastExecutedQuery(relatedBean, createdObjects);
                    result.addTLastExecutedQueryFromBean(related);
                }
            }
        }

    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TEvent object to this object.
     * through the TEvent foreign key attribute
     *
     * @param toAdd TEvent
     */
    protected void addTEventFromBean(TEvent toAdd)
    {
        initTEvents();
        collTEvents.add(toAdd);
    }


    /**
     * Method called to associate a TQueryRepository object to this object.
     * through the TQueryRepository foreign key attribute
     *
     * @param toAdd TQueryRepository
     */
    protected void addTQueryRepositoryFromBean(TQueryRepository toAdd)
    {
        initTQueryRepositorys();
        collTQueryRepositorys.add(toAdd);
    }


    /**
     * Method called to associate a TLastExecutedQuery object to this object.
     * through the TLastExecutedQuery foreign key attribute
     *
     * @param toAdd TLastExecutedQuery
     */
    protected void addTLastExecutedQueryFromBean(TLastExecutedQuery toAdd)
    {
        initTLastExecutedQuerys();
        collTLastExecutedQuerys.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TCLOB:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("CLOBValue = ")
           .append(getCLOBValue())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
