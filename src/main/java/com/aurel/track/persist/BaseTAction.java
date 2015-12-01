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



import com.aurel.track.persist.TBLOB;
import com.aurel.track.persist.TBLOBPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TActionBean;
import com.aurel.track.beans.TBLOBBean;

import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.beans.TWorkflowTransitionBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TAction
 */
public abstract class BaseTAction extends TpBaseObject
{
    /** The Peer class */
    private static final TActionPeer peer =
        new TActionPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the label field */
    private String label;

    /** The value for the description field */
    private String description;

    /** The value for the actionType field */
    private Integer actionType;

    /** The value for the iconKey field */
    private Integer iconKey;

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



        // update associated TScreenConfig
        if (collTScreenConfigs != null)
        {
            for (int i = 0; i < collTScreenConfigs.size(); i++)
            {
                ((TScreenConfig) collTScreenConfigs.get(i))
                        .setAction(v);
            }
        }

        // update associated TWorkflowTransition
        if (collTWorkflowTransitions != null)
        {
            for (int i = 0; i < collTWorkflowTransitions.size(); i++)
            {
                ((TWorkflowTransition) collTWorkflowTransitions.get(i))
                        .setActionKey(v);
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
     * Get the ActionType
     *
     * @return Integer
     */
    public Integer getActionType()
    {
        return actionType;
    }


    /**
     * Set the value of ActionType
     *
     * @param v new value
     */
    public void setActionType(Integer v) 
    {

        if (!ObjectUtils.equals(this.actionType, v))
        {
            this.actionType = v;
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
     * Collection to store aggregation of collTScreenConfigs
     */
    protected List<TScreenConfig> collTScreenConfigs;

    /**
     * Temporary storage of collTScreenConfigs to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTScreenConfigs()
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = new ArrayList<TScreenConfig>();
        }
    }


    /**
     * Method called to associate a TScreenConfig object to this object
     * through the TScreenConfig foreign key attribute
     *
     * @param l TScreenConfig
     * @throws TorqueException
     */
    public void addTScreenConfig(TScreenConfig l) throws TorqueException
    {
        getTScreenConfigs().add(l);
        l.setTAction((TAction) this);
    }

    /**
     * Method called to associate a TScreenConfig object to this object
     * through the TScreenConfig foreign key attribute using connection.
     *
     * @param l TScreenConfig
     * @throws TorqueException
     */
    public void addTScreenConfig(TScreenConfig l, Connection con) throws TorqueException
    {
        getTScreenConfigs(con).add(l);
        l.setTAction((TAction) this);
    }

    /**
     * The criteria used to select the current contents of collTScreenConfigs
     */
    private Criteria lastTScreenConfigsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenConfigs(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TScreenConfig> getTScreenConfigs()
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = getTScreenConfigs(new Criteria(10));
        }
        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAction has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     * If this TAction is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TScreenConfig> getTScreenConfigs(Criteria criteria) throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID() );
                collTScreenConfigs = TScreenConfigPeer.doSelect(criteria);
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
                criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
                if (!lastTScreenConfigsCriteria.equals(criteria))
                {
                    collTScreenConfigs = TScreenConfigPeer.doSelect(criteria);
                }
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTScreenConfigs(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenConfig> getTScreenConfigs(Connection con) throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            collTScreenConfigs = getTScreenConfigs(new Criteria(10), con);
        }
        return collTScreenConfigs;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAction has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     * If this TAction is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TScreenConfig> getTScreenConfigs(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                 criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
                 collTScreenConfigs = TScreenConfigPeer.doSelect(criteria, con);
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
                 criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
                 if (!lastTScreenConfigsCriteria.equals(criteria))
                 {
                     collTScreenConfigs = TScreenConfigPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTScreenConfigsCriteria = criteria;

         return collTScreenConfigs;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAction is new, it will return
     * an empty collection; or if this TAction has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAction.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTScreen(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTScreen(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTScreen(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAction is new, it will return
     * an empty collection; or if this TAction has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAction.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAction is new, it will return
     * an empty collection; or if this TAction has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAction.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAction is new, it will return
     * an empty collection; or if this TAction has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAction.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAction is new, it will return
     * an empty collection; or if this TAction has previously
     * been saved, it will retrieve related TScreenConfigs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAction.
     */
    protected List<TScreenConfig> getTScreenConfigsJoinTAction(Criteria criteria)
        throws TorqueException
    {
        if (collTScreenConfigs == null)
        {
            if (isNew())
            {
               collTScreenConfigs = new ArrayList<TScreenConfig>();
            }
            else
            {
                criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTAction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TScreenConfigPeer.ACTIONKEY, getObjectID());
            if (!lastTScreenConfigsCriteria.equals(criteria))
            {
                collTScreenConfigs = TScreenConfigPeer.doSelectJoinTAction(criteria);
            }
        }
        lastTScreenConfigsCriteria = criteria;

        return collTScreenConfigs;
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
        l.setTAction((TAction) this);
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
        l.setTAction((TAction) this);
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
     * Otherwise if this TAction has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     * If this TAction is new, it will return
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
                criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID() );
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
                criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID());
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
     * Otherwise if this TAction has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     * If this TAction is new, it will return
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
                 criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID());
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
                 criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID());
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
     * Otherwise if this TAction is new, it will return
     * an empty collection; or if this TAction has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAction.
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
                criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID());
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTWorkflowStationRelatedByStationFrom(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID());
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
     * Otherwise if this TAction is new, it will return
     * an empty collection; or if this TAction has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAction.
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
                criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID());
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTWorkflowStationRelatedByStationTo(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID());
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
     * Otherwise if this TAction is new, it will return
     * an empty collection; or if this TAction has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAction.
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
                criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID());
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTAction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID());
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
     * Otherwise if this TAction is new, it will return
     * an empty collection; or if this TAction has previously
     * been saved, it will retrieve related TWorkflowTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAction.
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
                criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID());
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowTransitionPeer.ACTIONKEY, getObjectID());
            if (!lastTWorkflowTransitionsCriteria.equals(criteria))
            {
                collTWorkflowTransitions = TWorkflowTransitionPeer.doSelectJoinTWorkflowDef(criteria);
            }
        }
        lastTWorkflowTransitionsCriteria = criteria;

        return collTWorkflowTransitions;
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
            fieldNames.add("Description");
            fieldNames.add("ActionType");
            fieldNames.add("IconKey");
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
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("ActionType"))
        {
            return getActionType();
        }
        if (name.equals("IconKey"))
        {
            return getIconKey();
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
        if (name.equals("ActionType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setActionType((Integer) value);
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
        if (name.equals(TActionPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TActionPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TActionPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TActionPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TActionPeer.ACTIONTYPE))
        {
            return getActionType();
        }
        if (name.equals(TActionPeer.ICONKEY))
        {
            return getIconKey();
        }
        if (name.equals(TActionPeer.TPUUID))
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
      if (TActionPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TActionPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TActionPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TActionPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TActionPeer.ACTIONTYPE.equals(name))
        {
            return setByName("ActionType", value);
        }
      if (TActionPeer.ICONKEY.equals(name))
        {
            return setByName("IconKey", value);
        }
      if (TActionPeer.TPUUID.equals(name))
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
            return getDescription();
        }
        if (pos == 4)
        {
            return getActionType();
        }
        if (pos == 5)
        {
            return getIconKey();
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
            return setByName("Name", value);
        }
    if (position == 2)
        {
            return setByName("Label", value);
        }
    if (position == 3)
        {
            return setByName("Description", value);
        }
    if (position == 4)
        {
            return setByName("ActionType", value);
        }
    if (position == 5)
        {
            return setByName("IconKey", value);
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
        save(TActionPeer.DATABASE_NAME);
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
                    TActionPeer.doInsert((TAction) this, con);
                    setNew(false);
                }
                else
                {
                    TActionPeer.doUpdate((TAction) this, con);
                }
            }


            if (collTScreenConfigs != null)
            {
                for (int i = 0; i < collTScreenConfigs.size(); i++)
                {
                    ((TScreenConfig) collTScreenConfigs.get(i)).save(con);
                }
            }

            if (collTWorkflowTransitions != null)
            {
                for (int i = 0; i < collTWorkflowTransitions.size(); i++)
                {
                    ((TWorkflowTransition) collTWorkflowTransitions.get(i)).save(con);
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
    public TAction copy() throws TorqueException
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
    public TAction copy(Connection con) throws TorqueException
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
    public TAction copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TAction(), deepcopy);
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
    public TAction copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TAction(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TAction copyInto(TAction copyObj) throws TorqueException
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
    protected TAction copyInto(TAction copyObj, Connection con) throws TorqueException
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
    protected TAction copyInto(TAction copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setLabel(label);
        copyObj.setDescription(description);
        copyObj.setActionType(actionType);
        copyObj.setIconKey(iconKey);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TScreenConfig> vTScreenConfigs = getTScreenConfigs();
        if (vTScreenConfigs != null)
        {
            for (int i = 0; i < vTScreenConfigs.size(); i++)
            {
                TScreenConfig obj =  vTScreenConfigs.get(i);
                copyObj.addTScreenConfig(obj.copy());
            }
        }
        else
        {
            copyObj.collTScreenConfigs = null;
        }


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
    protected TAction copyInto(TAction copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setLabel(label);
        copyObj.setDescription(description);
        copyObj.setActionType(actionType);
        copyObj.setIconKey(iconKey);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TScreenConfig> vTScreenConfigs = getTScreenConfigs(con);
        if (vTScreenConfigs != null)
        {
            for (int i = 0; i < vTScreenConfigs.size(); i++)
            {
                TScreenConfig obj =  vTScreenConfigs.get(i);
                copyObj.addTScreenConfig(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTScreenConfigs = null;
        }


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
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TActionPeer getPeer()
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
        return TActionPeer.getTableMap();
    }

  
    /**
     * Creates a TActionBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TActionBean with the contents of this object
     */
    public TActionBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TActionBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TActionBean with the contents of this object
     */
    public TActionBean getBean(IdentityMap createdBeans)
    {
        TActionBean result = (TActionBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TActionBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setLabel(getLabel());
        result.setDescription(getDescription());
        result.setActionType(getActionType());
        result.setIconKey(getIconKey());
        result.setUuid(getUuid());



        if (collTScreenConfigs != null)
        {
            List<TScreenConfigBean> relatedBeans = new ArrayList<TScreenConfigBean>(collTScreenConfigs.size());
            for (Iterator<TScreenConfig> collTScreenConfigsIt = collTScreenConfigs.iterator(); collTScreenConfigsIt.hasNext(); )
            {
                TScreenConfig related = (TScreenConfig) collTScreenConfigsIt.next();
                TScreenConfigBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTScreenConfigBeans(relatedBeans);
        }


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
     * Creates an instance of TAction with the contents
     * of a TActionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TActionBean which contents are used to create
     *        the resulting class
     * @return an instance of TAction with the contents of bean
     */
    public static TAction createTAction(TActionBean bean)
        throws TorqueException
    {
        return createTAction(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TAction with the contents
     * of a TActionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TActionBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TAction with the contents of bean
     */

    public static TAction createTAction(TActionBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TAction result = (TAction) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TAction();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setLabel(bean.getLabel());
        result.setDescription(bean.getDescription());
        result.setActionType(bean.getActionType());
        result.setIconKey(bean.getIconKey());
        result.setUuid(bean.getUuid());



        {
            List<TScreenConfigBean> relatedBeans = bean.getTScreenConfigBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TScreenConfigBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TScreenConfigBean relatedBean =  relatedBeansIt.next();
                    TScreenConfig related = TScreenConfig.createTScreenConfig(relatedBean, createdObjects);
                    result.addTScreenConfigFromBean(related);
                }
            }
        }


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
     * Method called to associate a TScreenConfig object to this object.
     * through the TScreenConfig foreign key attribute
     *
     * @param toAdd TScreenConfig
     */
    protected void addTScreenConfigFromBean(TScreenConfig toAdd)
    {
        initTScreenConfigs();
        collTScreenConfigs.add(toAdd);
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


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TAction:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("ActionType = ")
           .append(getActionType())
           .append("\n");
        str.append("IconKey = ")
           .append(getIconKey())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
