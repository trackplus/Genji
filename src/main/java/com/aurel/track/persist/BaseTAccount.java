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



import com.aurel.track.persist.TSystemState;
import com.aurel.track.persist.TSystemStatePeer;
import com.aurel.track.persist.TCostCenter;
import com.aurel.track.persist.TCostCenterPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TAccountBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TCostCenterBean;

import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TProjectAccountBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TAccount
 */
public abstract class BaseTAccount extends TpBaseObject
{
    /** The Peer class */
    private static final TAccountPeer peer =
        new TAccountPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the accountNumber field */
    private String accountNumber;

    /** The value for the accountName field */
    private String accountName;

    /** The value for the status field */
    private Integer status;

    /** The value for the costCenter field */
    private Integer costCenter;

    /** The value for the description field */
    private String description;

    /** The value for the moreProps field */
    private String moreProps;

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



        // update associated TCost
        if (collTCosts != null)
        {
            for (int i = 0; i < collTCosts.size(); i++)
            {
                ((TCost) collTCosts.get(i))
                        .setAccount(v);
            }
        }

        // update associated TProjectAccount
        if (collTProjectAccounts != null)
        {
            for (int i = 0; i < collTProjectAccounts.size(); i++)
            {
                ((TProjectAccount) collTProjectAccounts.get(i))
                        .setAccount(v);
            }
        }
    }

    /**
     * Get the AccountNumber
     *
     * @return String
     */
    public String getAccountNumber()
    {
        return accountNumber;
    }


    /**
     * Set the value of AccountNumber
     *
     * @param v new value
     */
    public void setAccountNumber(String v) 
    {

        if (!ObjectUtils.equals(this.accountNumber, v))
        {
            this.accountNumber = v;
            setModified(true);
        }


    }

    /**
     * Get the AccountName
     *
     * @return String
     */
    public String getAccountName()
    {
        return accountName;
    }


    /**
     * Set the value of AccountName
     *
     * @param v new value
     */
    public void setAccountName(String v) 
    {

        if (!ObjectUtils.equals(this.accountName, v))
        {
            this.accountName = v;
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


        if (aTSystemState != null && !ObjectUtils.equals(aTSystemState.getObjectID(), v))
        {
            aTSystemState = null;
        }

    }

    /**
     * Get the CostCenter
     *
     * @return Integer
     */
    public Integer getCostCenter()
    {
        return costCenter;
    }


    /**
     * Set the value of CostCenter
     *
     * @param v new value
     */
    public void setCostCenter(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.costCenter, v))
        {
            this.costCenter = v;
            setModified(true);
        }


        if (aTCostCenter != null && !ObjectUtils.equals(aTCostCenter.getObjectID(), v))
        {
            aTCostCenter = null;
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
     * Get the MoreProps
     *
     * @return String
     */
    public String getMoreProps()
    {
        return moreProps;
    }


    /**
     * Set the value of MoreProps
     *
     * @param v new value
     */
    public void setMoreProps(String v) 
    {

        if (!ObjectUtils.equals(this.moreProps, v))
        {
            this.moreProps = v;
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

    



    private TSystemState aTSystemState;

    /**
     * Declares an association between this object and a TSystemState object
     *
     * @param v TSystemState
     * @throws TorqueException
     */
    public void setTSystemState(TSystemState v) throws TorqueException
    {
        if (v == null)
        {
            setStatus((Integer) null);
        }
        else
        {
            setStatus(v.getObjectID());
        }
        aTSystemState = v;
    }


    /**
     * Returns the associated TSystemState object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TSystemState object
     * @throws TorqueException
     */
    public TSystemState getTSystemState()
        throws TorqueException
    {
        if (aTSystemState == null && (!ObjectUtils.equals(this.status, null)))
        {
            aTSystemState = TSystemStatePeer.retrieveByPK(SimpleKey.keyFor(this.status));
        }
        return aTSystemState;
    }

    /**
     * Return the associated TSystemState object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TSystemState object
     * @throws TorqueException
     */
    public TSystemState getTSystemState(Connection connection)
        throws TorqueException
    {
        if (aTSystemState == null && (!ObjectUtils.equals(this.status, null)))
        {
            aTSystemState = TSystemStatePeer.retrieveByPK(SimpleKey.keyFor(this.status), connection);
        }
        return aTSystemState;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTSystemStateKey(ObjectKey key) throws TorqueException
    {

        setStatus(new Integer(((NumberKey) key).intValue()));
    }




    private TCostCenter aTCostCenter;

    /**
     * Declares an association between this object and a TCostCenter object
     *
     * @param v TCostCenter
     * @throws TorqueException
     */
    public void setTCostCenter(TCostCenter v) throws TorqueException
    {
        if (v == null)
        {
            setCostCenter((Integer) null);
        }
        else
        {
            setCostCenter(v.getObjectID());
        }
        aTCostCenter = v;
    }


    /**
     * Returns the associated TCostCenter object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TCostCenter object
     * @throws TorqueException
     */
    public TCostCenter getTCostCenter()
        throws TorqueException
    {
        if (aTCostCenter == null && (!ObjectUtils.equals(this.costCenter, null)))
        {
            aTCostCenter = TCostCenterPeer.retrieveByPK(SimpleKey.keyFor(this.costCenter));
        }
        return aTCostCenter;
    }

    /**
     * Return the associated TCostCenter object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TCostCenter object
     * @throws TorqueException
     */
    public TCostCenter getTCostCenter(Connection connection)
        throws TorqueException
    {
        if (aTCostCenter == null && (!ObjectUtils.equals(this.costCenter, null)))
        {
            aTCostCenter = TCostCenterPeer.retrieveByPK(SimpleKey.keyFor(this.costCenter), connection);
        }
        return aTCostCenter;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTCostCenterKey(ObjectKey key) throws TorqueException
    {

        setCostCenter(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTCosts
     */
    protected List<TCost> collTCosts;

    /**
     * Temporary storage of collTCosts to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTCosts()
    {
        if (collTCosts == null)
        {
            collTCosts = new ArrayList<TCost>();
        }
    }


    /**
     * Method called to associate a TCost object to this object
     * through the TCost foreign key attribute
     *
     * @param l TCost
     * @throws TorqueException
     */
    public void addTCost(TCost l) throws TorqueException
    {
        getTCosts().add(l);
        l.setTAccount((TAccount) this);
    }

    /**
     * Method called to associate a TCost object to this object
     * through the TCost foreign key attribute using connection.
     *
     * @param l TCost
     * @throws TorqueException
     */
    public void addTCost(TCost l, Connection con) throws TorqueException
    {
        getTCosts(con).add(l);
        l.setTAccount((TAccount) this);
    }

    /**
     * The criteria used to select the current contents of collTCosts
     */
    private Criteria lastTCostsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCosts(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TCost> getTCosts()
        throws TorqueException
    {
        if (collTCosts == null)
        {
            collTCosts = getTCosts(new Criteria(10));
        }
        return collTCosts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAccount has previously
     * been saved, it will retrieve related TCosts from storage.
     * If this TAccount is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TCost> getTCosts(Criteria criteria) throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.ACCOUNT, getObjectID() );
                collTCosts = TCostPeer.doSelect(criteria);
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
                criteria.add(TCostPeer.ACCOUNT, getObjectID());
                if (!lastTCostsCriteria.equals(criteria))
                {
                    collTCosts = TCostPeer.doSelect(criteria);
                }
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCosts(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCost> getTCosts(Connection con) throws TorqueException
    {
        if (collTCosts == null)
        {
            collTCosts = getTCosts(new Criteria(10), con);
        }
        return collTCosts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAccount has previously
     * been saved, it will retrieve related TCosts from storage.
     * If this TAccount is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCost> getTCosts(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                 criteria.add(TCostPeer.ACCOUNT, getObjectID());
                 collTCosts = TCostPeer.doSelect(criteria, con);
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
                 criteria.add(TCostPeer.ACCOUNT, getObjectID());
                 if (!lastTCostsCriteria.equals(criteria))
                 {
                     collTCosts = TCostPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTCostsCriteria = criteria;

         return collTCosts;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAccount is new, it will return
     * an empty collection; or if this TAccount has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAccount.
     */
    protected List<TCost> getTCostsJoinTAccount(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.ACCOUNT, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTAccount(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.ACCOUNT, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTAccount(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAccount is new, it will return
     * an empty collection; or if this TAccount has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAccount.
     */
    protected List<TCost> getTCostsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.ACCOUNT, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.ACCOUNT, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAccount is new, it will return
     * an empty collection; or if this TAccount has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAccount.
     */
    protected List<TCost> getTCostsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.ACCOUNT, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.ACCOUNT, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAccount is new, it will return
     * an empty collection; or if this TAccount has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAccount.
     */
    protected List<TCost> getTCostsJoinTEffortType(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.ACCOUNT, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTEffortType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.ACCOUNT, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTEffortType(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }





    /**
     * Collection to store aggregation of collTProjectAccounts
     */
    protected List<TProjectAccount> collTProjectAccounts;

    /**
     * Temporary storage of collTProjectAccounts to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTProjectAccounts()
    {
        if (collTProjectAccounts == null)
        {
            collTProjectAccounts = new ArrayList<TProjectAccount>();
        }
    }


    /**
     * Method called to associate a TProjectAccount object to this object
     * through the TProjectAccount foreign key attribute
     *
     * @param l TProjectAccount
     * @throws TorqueException
     */
    public void addTProjectAccount(TProjectAccount l) throws TorqueException
    {
        getTProjectAccounts().add(l);
        l.setTAccount((TAccount) this);
    }

    /**
     * Method called to associate a TProjectAccount object to this object
     * through the TProjectAccount foreign key attribute using connection.
     *
     * @param l TProjectAccount
     * @throws TorqueException
     */
    public void addTProjectAccount(TProjectAccount l, Connection con) throws TorqueException
    {
        getTProjectAccounts(con).add(l);
        l.setTAccount((TAccount) this);
    }

    /**
     * The criteria used to select the current contents of collTProjectAccounts
     */
    private Criteria lastTProjectAccountsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjectAccounts(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TProjectAccount> getTProjectAccounts()
        throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            collTProjectAccounts = getTProjectAccounts(new Criteria(10));
        }
        return collTProjectAccounts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAccount has previously
     * been saved, it will retrieve related TProjectAccounts from storage.
     * If this TAccount is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TProjectAccount> getTProjectAccounts(Criteria criteria) throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            if (isNew())
            {
               collTProjectAccounts = new ArrayList<TProjectAccount>();
            }
            else
            {
                criteria.add(TProjectAccountPeer.ACCOUNT, getObjectID() );
                collTProjectAccounts = TProjectAccountPeer.doSelect(criteria);
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
                criteria.add(TProjectAccountPeer.ACCOUNT, getObjectID());
                if (!lastTProjectAccountsCriteria.equals(criteria))
                {
                    collTProjectAccounts = TProjectAccountPeer.doSelect(criteria);
                }
            }
        }
        lastTProjectAccountsCriteria = criteria;

        return collTProjectAccounts;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTProjectAccounts(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProjectAccount> getTProjectAccounts(Connection con) throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            collTProjectAccounts = getTProjectAccounts(new Criteria(10), con);
        }
        return collTProjectAccounts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAccount has previously
     * been saved, it will retrieve related TProjectAccounts from storage.
     * If this TAccount is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TProjectAccount> getTProjectAccounts(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            if (isNew())
            {
               collTProjectAccounts = new ArrayList<TProjectAccount>();
            }
            else
            {
                 criteria.add(TProjectAccountPeer.ACCOUNT, getObjectID());
                 collTProjectAccounts = TProjectAccountPeer.doSelect(criteria, con);
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
                 criteria.add(TProjectAccountPeer.ACCOUNT, getObjectID());
                 if (!lastTProjectAccountsCriteria.equals(criteria))
                 {
                     collTProjectAccounts = TProjectAccountPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTProjectAccountsCriteria = criteria;

         return collTProjectAccounts;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAccount is new, it will return
     * an empty collection; or if this TAccount has previously
     * been saved, it will retrieve related TProjectAccounts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAccount.
     */
    protected List<TProjectAccount> getTProjectAccountsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            if (isNew())
            {
               collTProjectAccounts = new ArrayList<TProjectAccount>();
            }
            else
            {
                criteria.add(TProjectAccountPeer.ACCOUNT, getObjectID());
                collTProjectAccounts = TProjectAccountPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectAccountPeer.ACCOUNT, getObjectID());
            if (!lastTProjectAccountsCriteria.equals(criteria))
            {
                collTProjectAccounts = TProjectAccountPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTProjectAccountsCriteria = criteria;

        return collTProjectAccounts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAccount is new, it will return
     * an empty collection; or if this TAccount has previously
     * been saved, it will retrieve related TProjectAccounts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAccount.
     */
    protected List<TProjectAccount> getTProjectAccountsJoinTAccount(Criteria criteria)
        throws TorqueException
    {
        if (collTProjectAccounts == null)
        {
            if (isNew())
            {
               collTProjectAccounts = new ArrayList<TProjectAccount>();
            }
            else
            {
                criteria.add(TProjectAccountPeer.ACCOUNT, getObjectID());
                collTProjectAccounts = TProjectAccountPeer.doSelectJoinTAccount(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectAccountPeer.ACCOUNT, getObjectID());
            if (!lastTProjectAccountsCriteria.equals(criteria))
            {
                collTProjectAccounts = TProjectAccountPeer.doSelectJoinTAccount(criteria);
            }
        }
        lastTProjectAccountsCriteria = criteria;

        return collTProjectAccounts;
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
            fieldNames.add("AccountNumber");
            fieldNames.add("AccountName");
            fieldNames.add("Status");
            fieldNames.add("CostCenter");
            fieldNames.add("Description");
            fieldNames.add("MoreProps");
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
        if (name.equals("AccountNumber"))
        {
            return getAccountNumber();
        }
        if (name.equals("AccountName"))
        {
            return getAccountName();
        }
        if (name.equals("Status"))
        {
            return getStatus();
        }
        if (name.equals("CostCenter"))
        {
            return getCostCenter();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("MoreProps"))
        {
            return getMoreProps();
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
        if (name.equals("AccountNumber"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAccountNumber((String) value);
            return true;
        }
        if (name.equals("AccountName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAccountName((String) value);
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
        if (name.equals("CostCenter"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCostCenter((Integer) value);
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
        if (name.equals("MoreProps"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMoreProps((String) value);
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
        if (name.equals(TAccountPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TAccountPeer.ACCOUNTNUMBER))
        {
            return getAccountNumber();
        }
        if (name.equals(TAccountPeer.ACCOUNTNAME))
        {
            return getAccountName();
        }
        if (name.equals(TAccountPeer.STATUS))
        {
            return getStatus();
        }
        if (name.equals(TAccountPeer.COSTCENTER))
        {
            return getCostCenter();
        }
        if (name.equals(TAccountPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TAccountPeer.MOREPROPS))
        {
            return getMoreProps();
        }
        if (name.equals(TAccountPeer.TPUUID))
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
      if (TAccountPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TAccountPeer.ACCOUNTNUMBER.equals(name))
        {
            return setByName("AccountNumber", value);
        }
      if (TAccountPeer.ACCOUNTNAME.equals(name))
        {
            return setByName("AccountName", value);
        }
      if (TAccountPeer.STATUS.equals(name))
        {
            return setByName("Status", value);
        }
      if (TAccountPeer.COSTCENTER.equals(name))
        {
            return setByName("CostCenter", value);
        }
      if (TAccountPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TAccountPeer.MOREPROPS.equals(name))
        {
            return setByName("MoreProps", value);
        }
      if (TAccountPeer.TPUUID.equals(name))
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
            return getAccountNumber();
        }
        if (pos == 2)
        {
            return getAccountName();
        }
        if (pos == 3)
        {
            return getStatus();
        }
        if (pos == 4)
        {
            return getCostCenter();
        }
        if (pos == 5)
        {
            return getDescription();
        }
        if (pos == 6)
        {
            return getMoreProps();
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
            return setByName("AccountNumber", value);
        }
    if (position == 2)
        {
            return setByName("AccountName", value);
        }
    if (position == 3)
        {
            return setByName("Status", value);
        }
    if (position == 4)
        {
            return setByName("CostCenter", value);
        }
    if (position == 5)
        {
            return setByName("Description", value);
        }
    if (position == 6)
        {
            return setByName("MoreProps", value);
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
        save(TAccountPeer.DATABASE_NAME);
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
                    TAccountPeer.doInsert((TAccount) this, con);
                    setNew(false);
                }
                else
                {
                    TAccountPeer.doUpdate((TAccount) this, con);
                }
            }


            if (collTCosts != null)
            {
                for (int i = 0; i < collTCosts.size(); i++)
                {
                    ((TCost) collTCosts.get(i)).save(con);
                }
            }

            if (collTProjectAccounts != null)
            {
                for (int i = 0; i < collTProjectAccounts.size(); i++)
                {
                    ((TProjectAccount) collTProjectAccounts.get(i)).save(con);
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
    public TAccount copy() throws TorqueException
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
    public TAccount copy(Connection con) throws TorqueException
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
    public TAccount copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TAccount(), deepcopy);
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
    public TAccount copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TAccount(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TAccount copyInto(TAccount copyObj) throws TorqueException
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
    protected TAccount copyInto(TAccount copyObj, Connection con) throws TorqueException
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
    protected TAccount copyInto(TAccount copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAccountNumber(accountNumber);
        copyObj.setAccountName(accountName);
        copyObj.setStatus(status);
        copyObj.setCostCenter(costCenter);
        copyObj.setDescription(description);
        copyObj.setMoreProps(moreProps);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TCost> vTCosts = getTCosts();
        if (vTCosts != null)
        {
            for (int i = 0; i < vTCosts.size(); i++)
            {
                TCost obj =  vTCosts.get(i);
                copyObj.addTCost(obj.copy());
            }
        }
        else
        {
            copyObj.collTCosts = null;
        }


        List<TProjectAccount> vTProjectAccounts = getTProjectAccounts();
        if (vTProjectAccounts != null)
        {
            for (int i = 0; i < vTProjectAccounts.size(); i++)
            {
                TProjectAccount obj =  vTProjectAccounts.get(i);
                copyObj.addTProjectAccount(obj.copy());
            }
        }
        else
        {
            copyObj.collTProjectAccounts = null;
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
    protected TAccount copyInto(TAccount copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAccountNumber(accountNumber);
        copyObj.setAccountName(accountName);
        copyObj.setStatus(status);
        copyObj.setCostCenter(costCenter);
        copyObj.setDescription(description);
        copyObj.setMoreProps(moreProps);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TCost> vTCosts = getTCosts(con);
        if (vTCosts != null)
        {
            for (int i = 0; i < vTCosts.size(); i++)
            {
                TCost obj =  vTCosts.get(i);
                copyObj.addTCost(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTCosts = null;
        }


        List<TProjectAccount> vTProjectAccounts = getTProjectAccounts(con);
        if (vTProjectAccounts != null)
        {
            for (int i = 0; i < vTProjectAccounts.size(); i++)
            {
                TProjectAccount obj =  vTProjectAccounts.get(i);
                copyObj.addTProjectAccount(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTProjectAccounts = null;
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
    public TAccountPeer getPeer()
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
        return TAccountPeer.getTableMap();
    }

  
    /**
     * Creates a TAccountBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TAccountBean with the contents of this object
     */
    public TAccountBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TAccountBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TAccountBean with the contents of this object
     */
    public TAccountBean getBean(IdentityMap createdBeans)
    {
        TAccountBean result = (TAccountBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TAccountBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setAccountNumber(getAccountNumber());
        result.setAccountName(getAccountName());
        result.setStatus(getStatus());
        result.setCostCenter(getCostCenter());
        result.setDescription(getDescription());
        result.setMoreProps(getMoreProps());
        result.setUuid(getUuid());



        if (collTCosts != null)
        {
            List<TCostBean> relatedBeans = new ArrayList<TCostBean>(collTCosts.size());
            for (Iterator<TCost> collTCostsIt = collTCosts.iterator(); collTCostsIt.hasNext(); )
            {
                TCost related = (TCost) collTCostsIt.next();
                TCostBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTCostBeans(relatedBeans);
        }


        if (collTProjectAccounts != null)
        {
            List<TProjectAccountBean> relatedBeans = new ArrayList<TProjectAccountBean>(collTProjectAccounts.size());
            for (Iterator<TProjectAccount> collTProjectAccountsIt = collTProjectAccounts.iterator(); collTProjectAccountsIt.hasNext(); )
            {
                TProjectAccount related = (TProjectAccount) collTProjectAccountsIt.next();
                TProjectAccountBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTProjectAccountBeans(relatedBeans);
        }




        if (aTSystemState != null)
        {
            TSystemStateBean relatedBean = aTSystemState.getBean(createdBeans);
            result.setTSystemStateBean(relatedBean);
        }



        if (aTCostCenter != null)
        {
            TCostCenterBean relatedBean = aTCostCenter.getBean(createdBeans);
            result.setTCostCenterBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TAccount with the contents
     * of a TAccountBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TAccountBean which contents are used to create
     *        the resulting class
     * @return an instance of TAccount with the contents of bean
     */
    public static TAccount createTAccount(TAccountBean bean)
        throws TorqueException
    {
        return createTAccount(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TAccount with the contents
     * of a TAccountBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TAccountBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TAccount with the contents of bean
     */

    public static TAccount createTAccount(TAccountBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TAccount result = (TAccount) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TAccount();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setAccountNumber(bean.getAccountNumber());
        result.setAccountName(bean.getAccountName());
        result.setStatus(bean.getStatus());
        result.setCostCenter(bean.getCostCenter());
        result.setDescription(bean.getDescription());
        result.setMoreProps(bean.getMoreProps());
        result.setUuid(bean.getUuid());



        {
            List<TCostBean> relatedBeans = bean.getTCostBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TCostBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TCostBean relatedBean =  relatedBeansIt.next();
                    TCost related = TCost.createTCost(relatedBean, createdObjects);
                    result.addTCostFromBean(related);
                }
            }
        }


        {
            List<TProjectAccountBean> relatedBeans = bean.getTProjectAccountBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TProjectAccountBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TProjectAccountBean relatedBean =  relatedBeansIt.next();
                    TProjectAccount related = TProjectAccount.createTProjectAccount(relatedBean, createdObjects);
                    result.addTProjectAccountFromBean(related);
                }
            }
        }




        {
            TSystemStateBean relatedBean = bean.getTSystemStateBean();
            if (relatedBean != null)
            {
                TSystemState relatedObject = TSystemState.createTSystemState(relatedBean, createdObjects);
                result.setTSystemState(relatedObject);
            }
        }



        {
            TCostCenterBean relatedBean = bean.getTCostCenterBean();
            if (relatedBean != null)
            {
                TCostCenter relatedObject = TCostCenter.createTCostCenter(relatedBean, createdObjects);
                result.setTCostCenter(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TCost object to this object.
     * through the TCost foreign key attribute
     *
     * @param toAdd TCost
     */
    protected void addTCostFromBean(TCost toAdd)
    {
        initTCosts();
        collTCosts.add(toAdd);
    }


    /**
     * Method called to associate a TProjectAccount object to this object.
     * through the TProjectAccount foreign key attribute
     *
     * @param toAdd TProjectAccount
     */
    protected void addTProjectAccountFromBean(TProjectAccount toAdd)
    {
        initTProjectAccounts();
        collTProjectAccounts.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TAccount:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("AccountNumber = ")
           .append(getAccountNumber())
           .append("\n");
        str.append("AccountName = ")
           .append(getAccountName())
           .append("\n");
        str.append("Status = ")
           .append(getStatus())
           .append("\n");
        str.append("CostCenter = ")
           .append(getCostCenter())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("MoreProps = ")
           .append(getMoreProps())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
