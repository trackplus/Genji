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
import com.aurel.track.beans.TBLOBBean;

import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TActionBean;
import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TLinkTypeBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TBLOB
 */
public abstract class BaseTBLOB extends TpBaseObject
{
    /** The Peer class */
    private static final TBLOBPeer peer =
        new TBLOBPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the bLOBValue field */
    private byte[] bLOBValue;

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



        // update associated TListType
        if (collTListTypes != null)
        {
            for (int i = 0; i < collTListTypes.size(); i++)
            {
                ((TListType) collTListTypes.get(i))
                        .setIconKey(v);
            }
        }

        // update associated TPerson
        if (collTPersons != null)
        {
            for (int i = 0; i < collTPersons.size(); i++)
            {
                ((TPerson) collTPersons.get(i))
                        .setIconKey(v);
            }
        }

        // update associated TPriority
        if (collTPrioritys != null)
        {
            for (int i = 0; i < collTPrioritys.size(); i++)
            {
                ((TPriority) collTPrioritys.get(i))
                        .setIconKey(v);
            }
        }

        // update associated TSeverity
        if (collTSeveritys != null)
        {
            for (int i = 0; i < collTSeveritys.size(); i++)
            {
                ((TSeverity) collTSeveritys.get(i))
                        .setIconKey(v);
            }
        }

        // update associated TState
        if (collTStates != null)
        {
            for (int i = 0; i < collTStates.size(); i++)
            {
                ((TState) collTStates.get(i))
                        .setIconKey(v);
            }
        }

        // update associated TSystemState
        if (collTSystemStates != null)
        {
            for (int i = 0; i < collTSystemStates.size(); i++)
            {
                ((TSystemState) collTSystemStates.get(i))
                        .setIconKey(v);
            }
        }

        // update associated TOption
        if (collTOptions != null)
        {
            for (int i = 0; i < collTOptions.size(); i++)
            {
                ((TOption) collTOptions.get(i))
                        .setIconKey(v);
            }
        }

        // update associated TAction
        if (collTActions != null)
        {
            for (int i = 0; i < collTActions.size(); i++)
            {
                ((TAction) collTActions.get(i))
                        .setIconKey(v);
            }
        }

        // update associated TLinkType
        if (collTLinkTypesRelatedByOutwardIconKey != null)
        {
            for (int i = 0; i < collTLinkTypesRelatedByOutwardIconKey.size(); i++)
            {
                ((TLinkType) collTLinkTypesRelatedByOutwardIconKey.get(i))
                        .setOutwardIconKey(v);
            }
        }

        // update associated TLinkType
        if (collTLinkTypesRelatedByInwardIconKey != null)
        {
            for (int i = 0; i < collTLinkTypesRelatedByInwardIconKey.size(); i++)
            {
                ((TLinkType) collTLinkTypesRelatedByInwardIconKey.get(i))
                        .setInwardIconKey(v);
            }
        }
    }

    /**
     * Get the BLOBValue
     *
     * @return byte[]
     */
    public byte[] getBLOBValue()
    {
        return bLOBValue;
    }


    /**
     * Set the value of BLOBValue
     *
     * @param v new value
     */
    public void setBLOBValue(byte[] v) 
    {

        if (!ObjectUtils.equals(this.bLOBValue, v))
        {
            this.bLOBValue = v;
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
     * Collection to store aggregation of collTListTypes
     */
    protected List<TListType> collTListTypes;

    /**
     * Temporary storage of collTListTypes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTListTypes()
    {
        if (collTListTypes == null)
        {
            collTListTypes = new ArrayList<TListType>();
        }
    }


    /**
     * Method called to associate a TListType object to this object
     * through the TListType foreign key attribute
     *
     * @param l TListType
     * @throws TorqueException
     */
    public void addTListType(TListType l) throws TorqueException
    {
        getTListTypes().add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * Method called to associate a TListType object to this object
     * through the TListType foreign key attribute using connection.
     *
     * @param l TListType
     * @throws TorqueException
     */
    public void addTListType(TListType l, Connection con) throws TorqueException
    {
        getTListTypes(con).add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTListTypes
     */
    private Criteria lastTListTypesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTListTypes(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TListType> getTListTypes()
        throws TorqueException
    {
        if (collTListTypes == null)
        {
            collTListTypes = getTListTypes(new Criteria(10));
        }
        return collTListTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TListTypes from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TListType> getTListTypes(Criteria criteria) throws TorqueException
    {
        if (collTListTypes == null)
        {
            if (isNew())
            {
               collTListTypes = new ArrayList<TListType>();
            }
            else
            {
                criteria.add(TListTypePeer.ICONKEY, getObjectID() );
                collTListTypes = TListTypePeer.doSelect(criteria);
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
                criteria.add(TListTypePeer.ICONKEY, getObjectID());
                if (!lastTListTypesCriteria.equals(criteria))
                {
                    collTListTypes = TListTypePeer.doSelect(criteria);
                }
            }
        }
        lastTListTypesCriteria = criteria;

        return collTListTypes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTListTypes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TListType> getTListTypes(Connection con) throws TorqueException
    {
        if (collTListTypes == null)
        {
            collTListTypes = getTListTypes(new Criteria(10), con);
        }
        return collTListTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TListTypes from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TListType> getTListTypes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTListTypes == null)
        {
            if (isNew())
            {
               collTListTypes = new ArrayList<TListType>();
            }
            else
            {
                 criteria.add(TListTypePeer.ICONKEY, getObjectID());
                 collTListTypes = TListTypePeer.doSelect(criteria, con);
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
                 criteria.add(TListTypePeer.ICONKEY, getObjectID());
                 if (!lastTListTypesCriteria.equals(criteria))
                 {
                     collTListTypes = TListTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTListTypesCriteria = criteria;

         return collTListTypes;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TListTypes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TListType> getTListTypesJoinTBLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTListTypes == null)
        {
            if (isNew())
            {
               collTListTypes = new ArrayList<TListType>();
            }
            else
            {
                criteria.add(TListTypePeer.ICONKEY, getObjectID());
                collTListTypes = TListTypePeer.doSelectJoinTBLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TListTypePeer.ICONKEY, getObjectID());
            if (!lastTListTypesCriteria.equals(criteria))
            {
                collTListTypes = TListTypePeer.doSelectJoinTBLOB(criteria);
            }
        }
        lastTListTypesCriteria = criteria;

        return collTListTypes;
    }





    /**
     * Collection to store aggregation of collTPersons
     */
    protected List<TPerson> collTPersons;

    /**
     * Temporary storage of collTPersons to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPersons()
    {
        if (collTPersons == null)
        {
            collTPersons = new ArrayList<TPerson>();
        }
    }


    /**
     * Method called to associate a TPerson object to this object
     * through the TPerson foreign key attribute
     *
     * @param l TPerson
     * @throws TorqueException
     */
    public void addTPerson(TPerson l) throws TorqueException
    {
        getTPersons().add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * Method called to associate a TPerson object to this object
     * through the TPerson foreign key attribute using connection.
     *
     * @param l TPerson
     * @throws TorqueException
     */
    public void addTPerson(TPerson l, Connection con) throws TorqueException
    {
        getTPersons(con).add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTPersons
     */
    private Criteria lastTPersonsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPersons(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPerson> getTPersons()
        throws TorqueException
    {
        if (collTPersons == null)
        {
            collTPersons = getTPersons(new Criteria(10));
        }
        return collTPersons;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TPersons from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPerson> getTPersons(Criteria criteria) throws TorqueException
    {
        if (collTPersons == null)
        {
            if (isNew())
            {
               collTPersons = new ArrayList<TPerson>();
            }
            else
            {
                criteria.add(TPersonPeer.ICONKEY, getObjectID() );
                collTPersons = TPersonPeer.doSelect(criteria);
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
                criteria.add(TPersonPeer.ICONKEY, getObjectID());
                if (!lastTPersonsCriteria.equals(criteria))
                {
                    collTPersons = TPersonPeer.doSelect(criteria);
                }
            }
        }
        lastTPersonsCriteria = criteria;

        return collTPersons;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPersons(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPerson> getTPersons(Connection con) throws TorqueException
    {
        if (collTPersons == null)
        {
            collTPersons = getTPersons(new Criteria(10), con);
        }
        return collTPersons;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TPersons from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPerson> getTPersons(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPersons == null)
        {
            if (isNew())
            {
               collTPersons = new ArrayList<TPerson>();
            }
            else
            {
                 criteria.add(TPersonPeer.ICONKEY, getObjectID());
                 collTPersons = TPersonPeer.doSelect(criteria, con);
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
                 criteria.add(TPersonPeer.ICONKEY, getObjectID());
                 if (!lastTPersonsCriteria.equals(criteria))
                 {
                     collTPersons = TPersonPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPersonsCriteria = criteria;

         return collTPersons;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TPersons from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TPerson> getTPersonsJoinTDepartment(Criteria criteria)
        throws TorqueException
    {
        if (collTPersons == null)
        {
            if (isNew())
            {
               collTPersons = new ArrayList<TPerson>();
            }
            else
            {
                criteria.add(TPersonPeer.ICONKEY, getObjectID());
                collTPersons = TPersonPeer.doSelectJoinTDepartment(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonPeer.ICONKEY, getObjectID());
            if (!lastTPersonsCriteria.equals(criteria))
            {
                collTPersons = TPersonPeer.doSelectJoinTDepartment(criteria);
            }
        }
        lastTPersonsCriteria = criteria;

        return collTPersons;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TPersons from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TPerson> getTPersonsJoinTPrivateReportRepository(Criteria criteria)
        throws TorqueException
    {
        if (collTPersons == null)
        {
            if (isNew())
            {
               collTPersons = new ArrayList<TPerson>();
            }
            else
            {
                criteria.add(TPersonPeer.ICONKEY, getObjectID());
                collTPersons = TPersonPeer.doSelectJoinTPrivateReportRepository(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonPeer.ICONKEY, getObjectID());
            if (!lastTPersonsCriteria.equals(criteria))
            {
                collTPersons = TPersonPeer.doSelectJoinTPrivateReportRepository(criteria);
            }
        }
        lastTPersonsCriteria = criteria;

        return collTPersons;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TPersons from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TPerson> getTPersonsJoinTBLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTPersons == null)
        {
            if (isNew())
            {
               collTPersons = new ArrayList<TPerson>();
            }
            else
            {
                criteria.add(TPersonPeer.ICONKEY, getObjectID());
                collTPersons = TPersonPeer.doSelectJoinTBLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonPeer.ICONKEY, getObjectID());
            if (!lastTPersonsCriteria.equals(criteria))
            {
                collTPersons = TPersonPeer.doSelectJoinTBLOB(criteria);
            }
        }
        lastTPersonsCriteria = criteria;

        return collTPersons;
    }













    /**
     * Collection to store aggregation of collTPrioritys
     */
    protected List<TPriority> collTPrioritys;

    /**
     * Temporary storage of collTPrioritys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPrioritys()
    {
        if (collTPrioritys == null)
        {
            collTPrioritys = new ArrayList<TPriority>();
        }
    }


    /**
     * Method called to associate a TPriority object to this object
     * through the TPriority foreign key attribute
     *
     * @param l TPriority
     * @throws TorqueException
     */
    public void addTPriority(TPriority l) throws TorqueException
    {
        getTPrioritys().add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * Method called to associate a TPriority object to this object
     * through the TPriority foreign key attribute using connection.
     *
     * @param l TPriority
     * @throws TorqueException
     */
    public void addTPriority(TPriority l, Connection con) throws TorqueException
    {
        getTPrioritys(con).add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTPrioritys
     */
    private Criteria lastTPrioritysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPrioritys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPriority> getTPrioritys()
        throws TorqueException
    {
        if (collTPrioritys == null)
        {
            collTPrioritys = getTPrioritys(new Criteria(10));
        }
        return collTPrioritys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TPrioritys from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPriority> getTPrioritys(Criteria criteria) throws TorqueException
    {
        if (collTPrioritys == null)
        {
            if (isNew())
            {
               collTPrioritys = new ArrayList<TPriority>();
            }
            else
            {
                criteria.add(TPriorityPeer.ICONKEY, getObjectID() );
                collTPrioritys = TPriorityPeer.doSelect(criteria);
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
                criteria.add(TPriorityPeer.ICONKEY, getObjectID());
                if (!lastTPrioritysCriteria.equals(criteria))
                {
                    collTPrioritys = TPriorityPeer.doSelect(criteria);
                }
            }
        }
        lastTPrioritysCriteria = criteria;

        return collTPrioritys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPrioritys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPriority> getTPrioritys(Connection con) throws TorqueException
    {
        if (collTPrioritys == null)
        {
            collTPrioritys = getTPrioritys(new Criteria(10), con);
        }
        return collTPrioritys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TPrioritys from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPriority> getTPrioritys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPrioritys == null)
        {
            if (isNew())
            {
               collTPrioritys = new ArrayList<TPriority>();
            }
            else
            {
                 criteria.add(TPriorityPeer.ICONKEY, getObjectID());
                 collTPrioritys = TPriorityPeer.doSelect(criteria, con);
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
                 criteria.add(TPriorityPeer.ICONKEY, getObjectID());
                 if (!lastTPrioritysCriteria.equals(criteria))
                 {
                     collTPrioritys = TPriorityPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPrioritysCriteria = criteria;

         return collTPrioritys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TPrioritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TPriority> getTPrioritysJoinTBLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTPrioritys == null)
        {
            if (isNew())
            {
               collTPrioritys = new ArrayList<TPriority>();
            }
            else
            {
                criteria.add(TPriorityPeer.ICONKEY, getObjectID());
                collTPrioritys = TPriorityPeer.doSelectJoinTBLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPriorityPeer.ICONKEY, getObjectID());
            if (!lastTPrioritysCriteria.equals(criteria))
            {
                collTPrioritys = TPriorityPeer.doSelectJoinTBLOB(criteria);
            }
        }
        lastTPrioritysCriteria = criteria;

        return collTPrioritys;
    }





    /**
     * Collection to store aggregation of collTSeveritys
     */
    protected List<TSeverity> collTSeveritys;

    /**
     * Temporary storage of collTSeveritys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTSeveritys()
    {
        if (collTSeveritys == null)
        {
            collTSeveritys = new ArrayList<TSeverity>();
        }
    }


    /**
     * Method called to associate a TSeverity object to this object
     * through the TSeverity foreign key attribute
     *
     * @param l TSeverity
     * @throws TorqueException
     */
    public void addTSeverity(TSeverity l) throws TorqueException
    {
        getTSeveritys().add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * Method called to associate a TSeverity object to this object
     * through the TSeverity foreign key attribute using connection.
     *
     * @param l TSeverity
     * @throws TorqueException
     */
    public void addTSeverity(TSeverity l, Connection con) throws TorqueException
    {
        getTSeveritys(con).add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTSeveritys
     */
    private Criteria lastTSeveritysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTSeveritys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TSeverity> getTSeveritys()
        throws TorqueException
    {
        if (collTSeveritys == null)
        {
            collTSeveritys = getTSeveritys(new Criteria(10));
        }
        return collTSeveritys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TSeveritys from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TSeverity> getTSeveritys(Criteria criteria) throws TorqueException
    {
        if (collTSeveritys == null)
        {
            if (isNew())
            {
               collTSeveritys = new ArrayList<TSeverity>();
            }
            else
            {
                criteria.add(TSeverityPeer.ICONKEY, getObjectID() );
                collTSeveritys = TSeverityPeer.doSelect(criteria);
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
                criteria.add(TSeverityPeer.ICONKEY, getObjectID());
                if (!lastTSeveritysCriteria.equals(criteria))
                {
                    collTSeveritys = TSeverityPeer.doSelect(criteria);
                }
            }
        }
        lastTSeveritysCriteria = criteria;

        return collTSeveritys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTSeveritys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TSeverity> getTSeveritys(Connection con) throws TorqueException
    {
        if (collTSeveritys == null)
        {
            collTSeveritys = getTSeveritys(new Criteria(10), con);
        }
        return collTSeveritys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TSeveritys from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TSeverity> getTSeveritys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTSeveritys == null)
        {
            if (isNew())
            {
               collTSeveritys = new ArrayList<TSeverity>();
            }
            else
            {
                 criteria.add(TSeverityPeer.ICONKEY, getObjectID());
                 collTSeveritys = TSeverityPeer.doSelect(criteria, con);
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
                 criteria.add(TSeverityPeer.ICONKEY, getObjectID());
                 if (!lastTSeveritysCriteria.equals(criteria))
                 {
                     collTSeveritys = TSeverityPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTSeveritysCriteria = criteria;

         return collTSeveritys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TSeveritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TSeverity> getTSeveritysJoinTBLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTSeveritys == null)
        {
            if (isNew())
            {
               collTSeveritys = new ArrayList<TSeverity>();
            }
            else
            {
                criteria.add(TSeverityPeer.ICONKEY, getObjectID());
                collTSeveritys = TSeverityPeer.doSelectJoinTBLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TSeverityPeer.ICONKEY, getObjectID());
            if (!lastTSeveritysCriteria.equals(criteria))
            {
                collTSeveritys = TSeverityPeer.doSelectJoinTBLOB(criteria);
            }
        }
        lastTSeveritysCriteria = criteria;

        return collTSeveritys;
    }





    /**
     * Collection to store aggregation of collTStates
     */
    protected List<TState> collTStates;

    /**
     * Temporary storage of collTStates to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTStates()
    {
        if (collTStates == null)
        {
            collTStates = new ArrayList<TState>();
        }
    }


    /**
     * Method called to associate a TState object to this object
     * through the TState foreign key attribute
     *
     * @param l TState
     * @throws TorqueException
     */
    public void addTState(TState l) throws TorqueException
    {
        getTStates().add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * Method called to associate a TState object to this object
     * through the TState foreign key attribute using connection.
     *
     * @param l TState
     * @throws TorqueException
     */
    public void addTState(TState l, Connection con) throws TorqueException
    {
        getTStates(con).add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTStates
     */
    private Criteria lastTStatesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTStates(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TState> getTStates()
        throws TorqueException
    {
        if (collTStates == null)
        {
            collTStates = getTStates(new Criteria(10));
        }
        return collTStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TStates from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TState> getTStates(Criteria criteria) throws TorqueException
    {
        if (collTStates == null)
        {
            if (isNew())
            {
               collTStates = new ArrayList<TState>();
            }
            else
            {
                criteria.add(TStatePeer.ICONKEY, getObjectID() );
                collTStates = TStatePeer.doSelect(criteria);
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
                criteria.add(TStatePeer.ICONKEY, getObjectID());
                if (!lastTStatesCriteria.equals(criteria))
                {
                    collTStates = TStatePeer.doSelect(criteria);
                }
            }
        }
        lastTStatesCriteria = criteria;

        return collTStates;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTStates(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TState> getTStates(Connection con) throws TorqueException
    {
        if (collTStates == null)
        {
            collTStates = getTStates(new Criteria(10), con);
        }
        return collTStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TStates from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TState> getTStates(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTStates == null)
        {
            if (isNew())
            {
               collTStates = new ArrayList<TState>();
            }
            else
            {
                 criteria.add(TStatePeer.ICONKEY, getObjectID());
                 collTStates = TStatePeer.doSelect(criteria, con);
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
                 criteria.add(TStatePeer.ICONKEY, getObjectID());
                 if (!lastTStatesCriteria.equals(criteria))
                 {
                     collTStates = TStatePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTStatesCriteria = criteria;

         return collTStates;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TState> getTStatesJoinTBLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTStates == null)
        {
            if (isNew())
            {
               collTStates = new ArrayList<TState>();
            }
            else
            {
                criteria.add(TStatePeer.ICONKEY, getObjectID());
                collTStates = TStatePeer.doSelectJoinTBLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TStatePeer.ICONKEY, getObjectID());
            if (!lastTStatesCriteria.equals(criteria))
            {
                collTStates = TStatePeer.doSelectJoinTBLOB(criteria);
            }
        }
        lastTStatesCriteria = criteria;

        return collTStates;
    }





    /**
     * Collection to store aggregation of collTSystemStates
     */
    protected List<TSystemState> collTSystemStates;

    /**
     * Temporary storage of collTSystemStates to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTSystemStates()
    {
        if (collTSystemStates == null)
        {
            collTSystemStates = new ArrayList<TSystemState>();
        }
    }


    /**
     * Method called to associate a TSystemState object to this object
     * through the TSystemState foreign key attribute
     *
     * @param l TSystemState
     * @throws TorqueException
     */
    public void addTSystemState(TSystemState l) throws TorqueException
    {
        getTSystemStates().add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * Method called to associate a TSystemState object to this object
     * through the TSystemState foreign key attribute using connection.
     *
     * @param l TSystemState
     * @throws TorqueException
     */
    public void addTSystemState(TSystemState l, Connection con) throws TorqueException
    {
        getTSystemStates(con).add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTSystemStates
     */
    private Criteria lastTSystemStatesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTSystemStates(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TSystemState> getTSystemStates()
        throws TorqueException
    {
        if (collTSystemStates == null)
        {
            collTSystemStates = getTSystemStates(new Criteria(10));
        }
        return collTSystemStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TSystemStates from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TSystemState> getTSystemStates(Criteria criteria) throws TorqueException
    {
        if (collTSystemStates == null)
        {
            if (isNew())
            {
               collTSystemStates = new ArrayList<TSystemState>();
            }
            else
            {
                criteria.add(TSystemStatePeer.ICONKEY, getObjectID() );
                collTSystemStates = TSystemStatePeer.doSelect(criteria);
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
                criteria.add(TSystemStatePeer.ICONKEY, getObjectID());
                if (!lastTSystemStatesCriteria.equals(criteria))
                {
                    collTSystemStates = TSystemStatePeer.doSelect(criteria);
                }
            }
        }
        lastTSystemStatesCriteria = criteria;

        return collTSystemStates;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTSystemStates(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TSystemState> getTSystemStates(Connection con) throws TorqueException
    {
        if (collTSystemStates == null)
        {
            collTSystemStates = getTSystemStates(new Criteria(10), con);
        }
        return collTSystemStates;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TSystemStates from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TSystemState> getTSystemStates(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTSystemStates == null)
        {
            if (isNew())
            {
               collTSystemStates = new ArrayList<TSystemState>();
            }
            else
            {
                 criteria.add(TSystemStatePeer.ICONKEY, getObjectID());
                 collTSystemStates = TSystemStatePeer.doSelect(criteria, con);
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
                 criteria.add(TSystemStatePeer.ICONKEY, getObjectID());
                 if (!lastTSystemStatesCriteria.equals(criteria))
                 {
                     collTSystemStates = TSystemStatePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTSystemStatesCriteria = criteria;

         return collTSystemStates;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TSystemStates from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TSystemState> getTSystemStatesJoinTBLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTSystemStates == null)
        {
            if (isNew())
            {
               collTSystemStates = new ArrayList<TSystemState>();
            }
            else
            {
                criteria.add(TSystemStatePeer.ICONKEY, getObjectID());
                collTSystemStates = TSystemStatePeer.doSelectJoinTBLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TSystemStatePeer.ICONKEY, getObjectID());
            if (!lastTSystemStatesCriteria.equals(criteria))
            {
                collTSystemStates = TSystemStatePeer.doSelectJoinTBLOB(criteria);
            }
        }
        lastTSystemStatesCriteria = criteria;

        return collTSystemStates;
    }





    /**
     * Collection to store aggregation of collTOptions
     */
    protected List<TOption> collTOptions;

    /**
     * Temporary storage of collTOptions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTOptions()
    {
        if (collTOptions == null)
        {
            collTOptions = new ArrayList<TOption>();
        }
    }


    /**
     * Method called to associate a TOption object to this object
     * through the TOption foreign key attribute
     *
     * @param l TOption
     * @throws TorqueException
     */
    public void addTOption(TOption l) throws TorqueException
    {
        getTOptions().add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * Method called to associate a TOption object to this object
     * through the TOption foreign key attribute using connection.
     *
     * @param l TOption
     * @throws TorqueException
     */
    public void addTOption(TOption l, Connection con) throws TorqueException
    {
        getTOptions(con).add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTOptions
     */
    private Criteria lastTOptionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOptions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TOption> getTOptions()
        throws TorqueException
    {
        if (collTOptions == null)
        {
            collTOptions = getTOptions(new Criteria(10));
        }
        return collTOptions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TOptions from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TOption> getTOptions(Criteria criteria) throws TorqueException
    {
        if (collTOptions == null)
        {
            if (isNew())
            {
               collTOptions = new ArrayList<TOption>();
            }
            else
            {
                criteria.add(TOptionPeer.ICONKEY, getObjectID() );
                collTOptions = TOptionPeer.doSelect(criteria);
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
                criteria.add(TOptionPeer.ICONKEY, getObjectID());
                if (!lastTOptionsCriteria.equals(criteria))
                {
                    collTOptions = TOptionPeer.doSelect(criteria);
                }
            }
        }
        lastTOptionsCriteria = criteria;

        return collTOptions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTOptions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOption> getTOptions(Connection con) throws TorqueException
    {
        if (collTOptions == null)
        {
            collTOptions = getTOptions(new Criteria(10), con);
        }
        return collTOptions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TOptions from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TOption> getTOptions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTOptions == null)
        {
            if (isNew())
            {
               collTOptions = new ArrayList<TOption>();
            }
            else
            {
                 criteria.add(TOptionPeer.ICONKEY, getObjectID());
                 collTOptions = TOptionPeer.doSelect(criteria, con);
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
                 criteria.add(TOptionPeer.ICONKEY, getObjectID());
                 if (!lastTOptionsCriteria.equals(criteria))
                 {
                     collTOptions = TOptionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTOptionsCriteria = criteria;

         return collTOptions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TOptions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TOption> getTOptionsJoinTList(Criteria criteria)
        throws TorqueException
    {
        if (collTOptions == null)
        {
            if (isNew())
            {
               collTOptions = new ArrayList<TOption>();
            }
            else
            {
                criteria.add(TOptionPeer.ICONKEY, getObjectID());
                collTOptions = TOptionPeer.doSelectJoinTList(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOptionPeer.ICONKEY, getObjectID());
            if (!lastTOptionsCriteria.equals(criteria))
            {
                collTOptions = TOptionPeer.doSelectJoinTList(criteria);
            }
        }
        lastTOptionsCriteria = criteria;

        return collTOptions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TOptions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TOption> getTOptionsJoinTBLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTOptions == null)
        {
            if (isNew())
            {
               collTOptions = new ArrayList<TOption>();
            }
            else
            {
                criteria.add(TOptionPeer.ICONKEY, getObjectID());
                collTOptions = TOptionPeer.doSelectJoinTBLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOptionPeer.ICONKEY, getObjectID());
            if (!lastTOptionsCriteria.equals(criteria))
            {
                collTOptions = TOptionPeer.doSelectJoinTBLOB(criteria);
            }
        }
        lastTOptionsCriteria = criteria;

        return collTOptions;
    }





    /**
     * Collection to store aggregation of collTActions
     */
    protected List<TAction> collTActions;

    /**
     * Temporary storage of collTActions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTActions()
    {
        if (collTActions == null)
        {
            collTActions = new ArrayList<TAction>();
        }
    }


    /**
     * Method called to associate a TAction object to this object
     * through the TAction foreign key attribute
     *
     * @param l TAction
     * @throws TorqueException
     */
    public void addTAction(TAction l) throws TorqueException
    {
        getTActions().add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * Method called to associate a TAction object to this object
     * through the TAction foreign key attribute using connection.
     *
     * @param l TAction
     * @throws TorqueException
     */
    public void addTAction(TAction l, Connection con) throws TorqueException
    {
        getTActions(con).add(l);
        l.setTBLOB((TBLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTActions
     */
    private Criteria lastTActionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTActions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAction> getTActions()
        throws TorqueException
    {
        if (collTActions == null)
        {
            collTActions = getTActions(new Criteria(10));
        }
        return collTActions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TActions from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAction> getTActions(Criteria criteria) throws TorqueException
    {
        if (collTActions == null)
        {
            if (isNew())
            {
               collTActions = new ArrayList<TAction>();
            }
            else
            {
                criteria.add(TActionPeer.ICONKEY, getObjectID() );
                collTActions = TActionPeer.doSelect(criteria);
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
                criteria.add(TActionPeer.ICONKEY, getObjectID());
                if (!lastTActionsCriteria.equals(criteria))
                {
                    collTActions = TActionPeer.doSelect(criteria);
                }
            }
        }
        lastTActionsCriteria = criteria;

        return collTActions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTActions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAction> getTActions(Connection con) throws TorqueException
    {
        if (collTActions == null)
        {
            collTActions = getTActions(new Criteria(10), con);
        }
        return collTActions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TActions from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAction> getTActions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTActions == null)
        {
            if (isNew())
            {
               collTActions = new ArrayList<TAction>();
            }
            else
            {
                 criteria.add(TActionPeer.ICONKEY, getObjectID());
                 collTActions = TActionPeer.doSelect(criteria, con);
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
                 criteria.add(TActionPeer.ICONKEY, getObjectID());
                 if (!lastTActionsCriteria.equals(criteria))
                 {
                     collTActions = TActionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTActionsCriteria = criteria;

         return collTActions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TActions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TAction> getTActionsJoinTBLOB(Criteria criteria)
        throws TorqueException
    {
        if (collTActions == null)
        {
            if (isNew())
            {
               collTActions = new ArrayList<TAction>();
            }
            else
            {
                criteria.add(TActionPeer.ICONKEY, getObjectID());
                collTActions = TActionPeer.doSelectJoinTBLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TActionPeer.ICONKEY, getObjectID());
            if (!lastTActionsCriteria.equals(criteria))
            {
                collTActions = TActionPeer.doSelectJoinTBLOB(criteria);
            }
        }
        lastTActionsCriteria = criteria;

        return collTActions;
    }





    /**
     * Collection to store aggregation of collTLinkTypesRelatedByOutwardIconKey
     */
    protected List<TLinkType> collTLinkTypesRelatedByOutwardIconKey;

    /**
     * Temporary storage of collTLinkTypesRelatedByOutwardIconKey to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTLinkTypesRelatedByOutwardIconKey()
    {
        if (collTLinkTypesRelatedByOutwardIconKey == null)
        {
            collTLinkTypesRelatedByOutwardIconKey = new ArrayList<TLinkType>();
        }
    }


    /**
     * Method called to associate a TLinkType object to this object
     * through the TLinkType foreign key attribute
     *
     * @param l TLinkType
     * @throws TorqueException
     */
    public void addTLinkTypeRelatedByOutwardIconKey(TLinkType l) throws TorqueException
    {
        getTLinkTypesRelatedByOutwardIconKey().add(l);
        l.setTBLOBRelatedByOutwardIconKey((TBLOB) this);
    }

    /**
     * Method called to associate a TLinkType object to this object
     * through the TLinkType foreign key attribute using connection.
     *
     * @param l TLinkType
     * @throws TorqueException
     */
    public void addTLinkTypeRelatedByOutwardIconKey(TLinkType l, Connection con) throws TorqueException
    {
        getTLinkTypesRelatedByOutwardIconKey(con).add(l);
        l.setTBLOBRelatedByOutwardIconKey((TBLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTLinkTypesRelatedByOutwardIconKey
     */
    private Criteria lastTLinkTypesRelatedByOutwardIconKeyCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLinkTypesRelatedByOutwardIconKey(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TLinkType> getTLinkTypesRelatedByOutwardIconKey()
        throws TorqueException
    {
        if (collTLinkTypesRelatedByOutwardIconKey == null)
        {
            collTLinkTypesRelatedByOutwardIconKey = getTLinkTypesRelatedByOutwardIconKey(new Criteria(10));
        }
        return collTLinkTypesRelatedByOutwardIconKey;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TLinkTypesRelatedByOutwardIconKey from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TLinkType> getTLinkTypesRelatedByOutwardIconKey(Criteria criteria) throws TorqueException
    {
        if (collTLinkTypesRelatedByOutwardIconKey == null)
        {
            if (isNew())
            {
               collTLinkTypesRelatedByOutwardIconKey = new ArrayList<TLinkType>();
            }
            else
            {
                criteria.add(TLinkTypePeer.OUTWARDICONKEY, getObjectID() );
                collTLinkTypesRelatedByOutwardIconKey = TLinkTypePeer.doSelect(criteria);
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
                criteria.add(TLinkTypePeer.OUTWARDICONKEY, getObjectID());
                if (!lastTLinkTypesRelatedByOutwardIconKeyCriteria.equals(criteria))
                {
                    collTLinkTypesRelatedByOutwardIconKey = TLinkTypePeer.doSelect(criteria);
                }
            }
        }
        lastTLinkTypesRelatedByOutwardIconKeyCriteria = criteria;

        return collTLinkTypesRelatedByOutwardIconKey;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLinkTypesRelatedByOutwardIconKey(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TLinkType> getTLinkTypesRelatedByOutwardIconKey(Connection con) throws TorqueException
    {
        if (collTLinkTypesRelatedByOutwardIconKey == null)
        {
            collTLinkTypesRelatedByOutwardIconKey = getTLinkTypesRelatedByOutwardIconKey(new Criteria(10), con);
        }
        return collTLinkTypesRelatedByOutwardIconKey;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TLinkTypesRelatedByOutwardIconKey from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TLinkType> getTLinkTypesRelatedByOutwardIconKey(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTLinkTypesRelatedByOutwardIconKey == null)
        {
            if (isNew())
            {
               collTLinkTypesRelatedByOutwardIconKey = new ArrayList<TLinkType>();
            }
            else
            {
                 criteria.add(TLinkTypePeer.OUTWARDICONKEY, getObjectID());
                 collTLinkTypesRelatedByOutwardIconKey = TLinkTypePeer.doSelect(criteria, con);
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
                 criteria.add(TLinkTypePeer.OUTWARDICONKEY, getObjectID());
                 if (!lastTLinkTypesRelatedByOutwardIconKeyCriteria.equals(criteria))
                 {
                     collTLinkTypesRelatedByOutwardIconKey = TLinkTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTLinkTypesRelatedByOutwardIconKeyCriteria = criteria;

         return collTLinkTypesRelatedByOutwardIconKey;
     }



















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TLinkTypesRelatedByOutwardIconKey from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TLinkType> getTLinkTypesRelatedByOutwardIconKeyJoinTBLOBRelatedByInwardIconKey(Criteria criteria)
        throws TorqueException
    {
        if (collTLinkTypesRelatedByOutwardIconKey == null)
        {
            if (isNew())
            {
               collTLinkTypesRelatedByOutwardIconKey = new ArrayList<TLinkType>();
            }
            else
            {
                criteria.add(TLinkTypePeer.OUTWARDICONKEY, getObjectID());
                collTLinkTypesRelatedByOutwardIconKey = TLinkTypePeer.doSelectJoinTBLOBRelatedByInwardIconKey(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TLinkTypePeer.OUTWARDICONKEY, getObjectID());
            if (!lastTLinkTypesRelatedByOutwardIconKeyCriteria.equals(criteria))
            {
                collTLinkTypesRelatedByOutwardIconKey = TLinkTypePeer.doSelectJoinTBLOBRelatedByInwardIconKey(criteria);
            }
        }
        lastTLinkTypesRelatedByOutwardIconKeyCriteria = criteria;

        return collTLinkTypesRelatedByOutwardIconKey;
    }





    /**
     * Collection to store aggregation of collTLinkTypesRelatedByInwardIconKey
     */
    protected List<TLinkType> collTLinkTypesRelatedByInwardIconKey;

    /**
     * Temporary storage of collTLinkTypesRelatedByInwardIconKey to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTLinkTypesRelatedByInwardIconKey()
    {
        if (collTLinkTypesRelatedByInwardIconKey == null)
        {
            collTLinkTypesRelatedByInwardIconKey = new ArrayList<TLinkType>();
        }
    }


    /**
     * Method called to associate a TLinkType object to this object
     * through the TLinkType foreign key attribute
     *
     * @param l TLinkType
     * @throws TorqueException
     */
    public void addTLinkTypeRelatedByInwardIconKey(TLinkType l) throws TorqueException
    {
        getTLinkTypesRelatedByInwardIconKey().add(l);
        l.setTBLOBRelatedByInwardIconKey((TBLOB) this);
    }

    /**
     * Method called to associate a TLinkType object to this object
     * through the TLinkType foreign key attribute using connection.
     *
     * @param l TLinkType
     * @throws TorqueException
     */
    public void addTLinkTypeRelatedByInwardIconKey(TLinkType l, Connection con) throws TorqueException
    {
        getTLinkTypesRelatedByInwardIconKey(con).add(l);
        l.setTBLOBRelatedByInwardIconKey((TBLOB) this);
    }

    /**
     * The criteria used to select the current contents of collTLinkTypesRelatedByInwardIconKey
     */
    private Criteria lastTLinkTypesRelatedByInwardIconKeyCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLinkTypesRelatedByInwardIconKey(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TLinkType> getTLinkTypesRelatedByInwardIconKey()
        throws TorqueException
    {
        if (collTLinkTypesRelatedByInwardIconKey == null)
        {
            collTLinkTypesRelatedByInwardIconKey = getTLinkTypesRelatedByInwardIconKey(new Criteria(10));
        }
        return collTLinkTypesRelatedByInwardIconKey;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TLinkTypesRelatedByInwardIconKey from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TLinkType> getTLinkTypesRelatedByInwardIconKey(Criteria criteria) throws TorqueException
    {
        if (collTLinkTypesRelatedByInwardIconKey == null)
        {
            if (isNew())
            {
               collTLinkTypesRelatedByInwardIconKey = new ArrayList<TLinkType>();
            }
            else
            {
                criteria.add(TLinkTypePeer.INWARDICONKEY, getObjectID() );
                collTLinkTypesRelatedByInwardIconKey = TLinkTypePeer.doSelect(criteria);
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
                criteria.add(TLinkTypePeer.INWARDICONKEY, getObjectID());
                if (!lastTLinkTypesRelatedByInwardIconKeyCriteria.equals(criteria))
                {
                    collTLinkTypesRelatedByInwardIconKey = TLinkTypePeer.doSelect(criteria);
                }
            }
        }
        lastTLinkTypesRelatedByInwardIconKeyCriteria = criteria;

        return collTLinkTypesRelatedByInwardIconKey;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLinkTypesRelatedByInwardIconKey(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TLinkType> getTLinkTypesRelatedByInwardIconKey(Connection con) throws TorqueException
    {
        if (collTLinkTypesRelatedByInwardIconKey == null)
        {
            collTLinkTypesRelatedByInwardIconKey = getTLinkTypesRelatedByInwardIconKey(new Criteria(10), con);
        }
        return collTLinkTypesRelatedByInwardIconKey;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB has previously
     * been saved, it will retrieve related TLinkTypesRelatedByInwardIconKey from storage.
     * If this TBLOB is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TLinkType> getTLinkTypesRelatedByInwardIconKey(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTLinkTypesRelatedByInwardIconKey == null)
        {
            if (isNew())
            {
               collTLinkTypesRelatedByInwardIconKey = new ArrayList<TLinkType>();
            }
            else
            {
                 criteria.add(TLinkTypePeer.INWARDICONKEY, getObjectID());
                 collTLinkTypesRelatedByInwardIconKey = TLinkTypePeer.doSelect(criteria, con);
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
                 criteria.add(TLinkTypePeer.INWARDICONKEY, getObjectID());
                 if (!lastTLinkTypesRelatedByInwardIconKeyCriteria.equals(criteria))
                 {
                     collTLinkTypesRelatedByInwardIconKey = TLinkTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTLinkTypesRelatedByInwardIconKeyCriteria = criteria;

         return collTLinkTypesRelatedByInwardIconKey;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TBLOB is new, it will return
     * an empty collection; or if this TBLOB has previously
     * been saved, it will retrieve related TLinkTypesRelatedByInwardIconKey from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TBLOB.
     */
    protected List<TLinkType> getTLinkTypesRelatedByInwardIconKeyJoinTBLOBRelatedByOutwardIconKey(Criteria criteria)
        throws TorqueException
    {
        if (collTLinkTypesRelatedByInwardIconKey == null)
        {
            if (isNew())
            {
               collTLinkTypesRelatedByInwardIconKey = new ArrayList<TLinkType>();
            }
            else
            {
                criteria.add(TLinkTypePeer.INWARDICONKEY, getObjectID());
                collTLinkTypesRelatedByInwardIconKey = TLinkTypePeer.doSelectJoinTBLOBRelatedByOutwardIconKey(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TLinkTypePeer.INWARDICONKEY, getObjectID());
            if (!lastTLinkTypesRelatedByInwardIconKeyCriteria.equals(criteria))
            {
                collTLinkTypesRelatedByInwardIconKey = TLinkTypePeer.doSelectJoinTBLOBRelatedByOutwardIconKey(criteria);
            }
        }
        lastTLinkTypesRelatedByInwardIconKeyCriteria = criteria;

        return collTLinkTypesRelatedByInwardIconKey;
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
            fieldNames.add("BLOBValue");
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
        if (name.equals("BLOBValue"))
        {
            return getBLOBValue();
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
        if (name.equals("BLOBValue"))
        {
            // Object fields can be null
            if (value != null && ! byte[].class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setBLOBValue((byte[]) value);
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
        if (name.equals(TBLOBPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TBLOBPeer.BLOBVALUE))
        {
            return getBLOBValue();
        }
        if (name.equals(TBLOBPeer.TPUUID))
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
      if (TBLOBPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TBLOBPeer.BLOBVALUE.equals(name))
        {
            return setByName("BLOBValue", value);
        }
      if (TBLOBPeer.TPUUID.equals(name))
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
            return getBLOBValue();
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
            return setByName("BLOBValue", value);
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
        save(TBLOBPeer.DATABASE_NAME);
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
                    TBLOBPeer.doInsert((TBLOB) this, con);
                    setNew(false);
                }
                else
                {
                    TBLOBPeer.doUpdate((TBLOB) this, con);
                }
            }


            if (collTListTypes != null)
            {
                for (int i = 0; i < collTListTypes.size(); i++)
                {
                    ((TListType) collTListTypes.get(i)).save(con);
                }
            }

            if (collTPersons != null)
            {
                for (int i = 0; i < collTPersons.size(); i++)
                {
                    ((TPerson) collTPersons.get(i)).save(con);
                }
            }

            if (collTPrioritys != null)
            {
                for (int i = 0; i < collTPrioritys.size(); i++)
                {
                    ((TPriority) collTPrioritys.get(i)).save(con);
                }
            }

            if (collTSeveritys != null)
            {
                for (int i = 0; i < collTSeveritys.size(); i++)
                {
                    ((TSeverity) collTSeveritys.get(i)).save(con);
                }
            }

            if (collTStates != null)
            {
                for (int i = 0; i < collTStates.size(); i++)
                {
                    ((TState) collTStates.get(i)).save(con);
                }
            }

            if (collTSystemStates != null)
            {
                for (int i = 0; i < collTSystemStates.size(); i++)
                {
                    ((TSystemState) collTSystemStates.get(i)).save(con);
                }
            }

            if (collTOptions != null)
            {
                for (int i = 0; i < collTOptions.size(); i++)
                {
                    ((TOption) collTOptions.get(i)).save(con);
                }
            }

            if (collTActions != null)
            {
                for (int i = 0; i < collTActions.size(); i++)
                {
                    ((TAction) collTActions.get(i)).save(con);
                }
            }

            if (collTLinkTypesRelatedByOutwardIconKey != null)
            {
                for (int i = 0; i < collTLinkTypesRelatedByOutwardIconKey.size(); i++)
                {
                    ((TLinkType) collTLinkTypesRelatedByOutwardIconKey.get(i)).save(con);
                }
            }

            if (collTLinkTypesRelatedByInwardIconKey != null)
            {
                for (int i = 0; i < collTLinkTypesRelatedByInwardIconKey.size(); i++)
                {
                    ((TLinkType) collTLinkTypesRelatedByInwardIconKey.get(i)).save(con);
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
    public TBLOB copy() throws TorqueException
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
    public TBLOB copy(Connection con) throws TorqueException
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
    public TBLOB copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TBLOB(), deepcopy);
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
    public TBLOB copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TBLOB(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TBLOB copyInto(TBLOB copyObj) throws TorqueException
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
    protected TBLOB copyInto(TBLOB copyObj, Connection con) throws TorqueException
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
    protected TBLOB copyInto(TBLOB copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setBLOBValue(bLOBValue);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TListType> vTListTypes = getTListTypes();
        if (vTListTypes != null)
        {
            for (int i = 0; i < vTListTypes.size(); i++)
            {
                TListType obj =  vTListTypes.get(i);
                copyObj.addTListType(obj.copy());
            }
        }
        else
        {
            copyObj.collTListTypes = null;
        }


        List<TPerson> vTPersons = getTPersons();
        if (vTPersons != null)
        {
            for (int i = 0; i < vTPersons.size(); i++)
            {
                TPerson obj =  vTPersons.get(i);
                copyObj.addTPerson(obj.copy());
            }
        }
        else
        {
            copyObj.collTPersons = null;
        }


        List<TPriority> vTPrioritys = getTPrioritys();
        if (vTPrioritys != null)
        {
            for (int i = 0; i < vTPrioritys.size(); i++)
            {
                TPriority obj =  vTPrioritys.get(i);
                copyObj.addTPriority(obj.copy());
            }
        }
        else
        {
            copyObj.collTPrioritys = null;
        }


        List<TSeverity> vTSeveritys = getTSeveritys();
        if (vTSeveritys != null)
        {
            for (int i = 0; i < vTSeveritys.size(); i++)
            {
                TSeverity obj =  vTSeveritys.get(i);
                copyObj.addTSeverity(obj.copy());
            }
        }
        else
        {
            copyObj.collTSeveritys = null;
        }


        List<TState> vTStates = getTStates();
        if (vTStates != null)
        {
            for (int i = 0; i < vTStates.size(); i++)
            {
                TState obj =  vTStates.get(i);
                copyObj.addTState(obj.copy());
            }
        }
        else
        {
            copyObj.collTStates = null;
        }


        List<TSystemState> vTSystemStates = getTSystemStates();
        if (vTSystemStates != null)
        {
            for (int i = 0; i < vTSystemStates.size(); i++)
            {
                TSystemState obj =  vTSystemStates.get(i);
                copyObj.addTSystemState(obj.copy());
            }
        }
        else
        {
            copyObj.collTSystemStates = null;
        }


        List<TOption> vTOptions = getTOptions();
        if (vTOptions != null)
        {
            for (int i = 0; i < vTOptions.size(); i++)
            {
                TOption obj =  vTOptions.get(i);
                copyObj.addTOption(obj.copy());
            }
        }
        else
        {
            copyObj.collTOptions = null;
        }


        List<TAction> vTActions = getTActions();
        if (vTActions != null)
        {
            for (int i = 0; i < vTActions.size(); i++)
            {
                TAction obj =  vTActions.get(i);
                copyObj.addTAction(obj.copy());
            }
        }
        else
        {
            copyObj.collTActions = null;
        }


        List<TLinkType> vTLinkTypesRelatedByOutwardIconKey = getTLinkTypesRelatedByOutwardIconKey();
        if (vTLinkTypesRelatedByOutwardIconKey != null)
        {
            for (int i = 0; i < vTLinkTypesRelatedByOutwardIconKey.size(); i++)
            {
                TLinkType obj =  vTLinkTypesRelatedByOutwardIconKey.get(i);
                copyObj.addTLinkTypeRelatedByOutwardIconKey(obj.copy());
            }
        }
        else
        {
            copyObj.collTLinkTypesRelatedByOutwardIconKey = null;
        }


        List<TLinkType> vTLinkTypesRelatedByInwardIconKey = getTLinkTypesRelatedByInwardIconKey();
        if (vTLinkTypesRelatedByInwardIconKey != null)
        {
            for (int i = 0; i < vTLinkTypesRelatedByInwardIconKey.size(); i++)
            {
                TLinkType obj =  vTLinkTypesRelatedByInwardIconKey.get(i);
                copyObj.addTLinkTypeRelatedByInwardIconKey(obj.copy());
            }
        }
        else
        {
            copyObj.collTLinkTypesRelatedByInwardIconKey = null;
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
    protected TBLOB copyInto(TBLOB copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setBLOBValue(bLOBValue);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TListType> vTListTypes = getTListTypes(con);
        if (vTListTypes != null)
        {
            for (int i = 0; i < vTListTypes.size(); i++)
            {
                TListType obj =  vTListTypes.get(i);
                copyObj.addTListType(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTListTypes = null;
        }


        List<TPerson> vTPersons = getTPersons(con);
        if (vTPersons != null)
        {
            for (int i = 0; i < vTPersons.size(); i++)
            {
                TPerson obj =  vTPersons.get(i);
                copyObj.addTPerson(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPersons = null;
        }


        List<TPriority> vTPrioritys = getTPrioritys(con);
        if (vTPrioritys != null)
        {
            for (int i = 0; i < vTPrioritys.size(); i++)
            {
                TPriority obj =  vTPrioritys.get(i);
                copyObj.addTPriority(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPrioritys = null;
        }


        List<TSeverity> vTSeveritys = getTSeveritys(con);
        if (vTSeveritys != null)
        {
            for (int i = 0; i < vTSeveritys.size(); i++)
            {
                TSeverity obj =  vTSeveritys.get(i);
                copyObj.addTSeverity(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTSeveritys = null;
        }


        List<TState> vTStates = getTStates(con);
        if (vTStates != null)
        {
            for (int i = 0; i < vTStates.size(); i++)
            {
                TState obj =  vTStates.get(i);
                copyObj.addTState(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTStates = null;
        }


        List<TSystemState> vTSystemStates = getTSystemStates(con);
        if (vTSystemStates != null)
        {
            for (int i = 0; i < vTSystemStates.size(); i++)
            {
                TSystemState obj =  vTSystemStates.get(i);
                copyObj.addTSystemState(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTSystemStates = null;
        }


        List<TOption> vTOptions = getTOptions(con);
        if (vTOptions != null)
        {
            for (int i = 0; i < vTOptions.size(); i++)
            {
                TOption obj =  vTOptions.get(i);
                copyObj.addTOption(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTOptions = null;
        }


        List<TAction> vTActions = getTActions(con);
        if (vTActions != null)
        {
            for (int i = 0; i < vTActions.size(); i++)
            {
                TAction obj =  vTActions.get(i);
                copyObj.addTAction(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTActions = null;
        }


        List<TLinkType> vTLinkTypesRelatedByOutwardIconKey = getTLinkTypesRelatedByOutwardIconKey(con);
        if (vTLinkTypesRelatedByOutwardIconKey != null)
        {
            for (int i = 0; i < vTLinkTypesRelatedByOutwardIconKey.size(); i++)
            {
                TLinkType obj =  vTLinkTypesRelatedByOutwardIconKey.get(i);
                copyObj.addTLinkTypeRelatedByOutwardIconKey(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTLinkTypesRelatedByOutwardIconKey = null;
        }


        List<TLinkType> vTLinkTypesRelatedByInwardIconKey = getTLinkTypesRelatedByInwardIconKey(con);
        if (vTLinkTypesRelatedByInwardIconKey != null)
        {
            for (int i = 0; i < vTLinkTypesRelatedByInwardIconKey.size(); i++)
            {
                TLinkType obj =  vTLinkTypesRelatedByInwardIconKey.get(i);
                copyObj.addTLinkTypeRelatedByInwardIconKey(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTLinkTypesRelatedByInwardIconKey = null;
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
    public TBLOBPeer getPeer()
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
        return TBLOBPeer.getTableMap();
    }

  
    /**
     * Creates a TBLOBBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TBLOBBean with the contents of this object
     */
    public TBLOBBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TBLOBBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TBLOBBean with the contents of this object
     */
    public TBLOBBean getBean(IdentityMap createdBeans)
    {
        TBLOBBean result = (TBLOBBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TBLOBBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setBLOBValue(getBLOBValue());
        result.setUuid(getUuid());



        if (collTListTypes != null)
        {
            List<TListTypeBean> relatedBeans = new ArrayList<TListTypeBean>(collTListTypes.size());
            for (Iterator<TListType> collTListTypesIt = collTListTypes.iterator(); collTListTypesIt.hasNext(); )
            {
                TListType related = (TListType) collTListTypesIt.next();
                TListTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTListTypeBeans(relatedBeans);
        }


        if (collTPersons != null)
        {
            List<TPersonBean> relatedBeans = new ArrayList<TPersonBean>(collTPersons.size());
            for (Iterator<TPerson> collTPersonsIt = collTPersons.iterator(); collTPersonsIt.hasNext(); )
            {
                TPerson related = (TPerson) collTPersonsIt.next();
                TPersonBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPersonBeans(relatedBeans);
        }


        if (collTPrioritys != null)
        {
            List<TPriorityBean> relatedBeans = new ArrayList<TPriorityBean>(collTPrioritys.size());
            for (Iterator<TPriority> collTPrioritysIt = collTPrioritys.iterator(); collTPrioritysIt.hasNext(); )
            {
                TPriority related = (TPriority) collTPrioritysIt.next();
                TPriorityBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPriorityBeans(relatedBeans);
        }


        if (collTSeveritys != null)
        {
            List<TSeverityBean> relatedBeans = new ArrayList<TSeverityBean>(collTSeveritys.size());
            for (Iterator<TSeverity> collTSeveritysIt = collTSeveritys.iterator(); collTSeveritysIt.hasNext(); )
            {
                TSeverity related = (TSeverity) collTSeveritysIt.next();
                TSeverityBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTSeverityBeans(relatedBeans);
        }


        if (collTStates != null)
        {
            List<TStateBean> relatedBeans = new ArrayList<TStateBean>(collTStates.size());
            for (Iterator<TState> collTStatesIt = collTStates.iterator(); collTStatesIt.hasNext(); )
            {
                TState related = (TState) collTStatesIt.next();
                TStateBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTStateBeans(relatedBeans);
        }


        if (collTSystemStates != null)
        {
            List<TSystemStateBean> relatedBeans = new ArrayList<TSystemStateBean>(collTSystemStates.size());
            for (Iterator<TSystemState> collTSystemStatesIt = collTSystemStates.iterator(); collTSystemStatesIt.hasNext(); )
            {
                TSystemState related = (TSystemState) collTSystemStatesIt.next();
                TSystemStateBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTSystemStateBeans(relatedBeans);
        }


        if (collTOptions != null)
        {
            List<TOptionBean> relatedBeans = new ArrayList<TOptionBean>(collTOptions.size());
            for (Iterator<TOption> collTOptionsIt = collTOptions.iterator(); collTOptionsIt.hasNext(); )
            {
                TOption related = (TOption) collTOptionsIt.next();
                TOptionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTOptionBeans(relatedBeans);
        }


        if (collTActions != null)
        {
            List<TActionBean> relatedBeans = new ArrayList<TActionBean>(collTActions.size());
            for (Iterator<TAction> collTActionsIt = collTActions.iterator(); collTActionsIt.hasNext(); )
            {
                TAction related = (TAction) collTActionsIt.next();
                TActionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTActionBeans(relatedBeans);
        }


        if (collTLinkTypesRelatedByOutwardIconKey != null)
        {
            List<TLinkTypeBean> relatedBeans = new ArrayList<TLinkTypeBean>(collTLinkTypesRelatedByOutwardIconKey.size());
            for (Iterator<TLinkType> collTLinkTypesRelatedByOutwardIconKeyIt = collTLinkTypesRelatedByOutwardIconKey.iterator(); collTLinkTypesRelatedByOutwardIconKeyIt.hasNext(); )
            {
                TLinkType related = (TLinkType) collTLinkTypesRelatedByOutwardIconKeyIt.next();
                TLinkTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTLinkTypeBeansRelatedByOutwardIconKey(relatedBeans);
        }


        if (collTLinkTypesRelatedByInwardIconKey != null)
        {
            List<TLinkTypeBean> relatedBeans = new ArrayList<TLinkTypeBean>(collTLinkTypesRelatedByInwardIconKey.size());
            for (Iterator<TLinkType> collTLinkTypesRelatedByInwardIconKeyIt = collTLinkTypesRelatedByInwardIconKey.iterator(); collTLinkTypesRelatedByInwardIconKeyIt.hasNext(); )
            {
                TLinkType related = (TLinkType) collTLinkTypesRelatedByInwardIconKeyIt.next();
                TLinkTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTLinkTypeBeansRelatedByInwardIconKey(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TBLOB with the contents
     * of a TBLOBBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TBLOBBean which contents are used to create
     *        the resulting class
     * @return an instance of TBLOB with the contents of bean
     */
    public static TBLOB createTBLOB(TBLOBBean bean)
        throws TorqueException
    {
        return createTBLOB(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TBLOB with the contents
     * of a TBLOBBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TBLOBBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TBLOB with the contents of bean
     */

    public static TBLOB createTBLOB(TBLOBBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TBLOB result = (TBLOB) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TBLOB();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setBLOBValue(bean.getBLOBValue());
        result.setUuid(bean.getUuid());



        {
            List<TListTypeBean> relatedBeans = bean.getTListTypeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TListTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TListTypeBean relatedBean =  relatedBeansIt.next();
                    TListType related = TListType.createTListType(relatedBean, createdObjects);
                    result.addTListTypeFromBean(related);
                }
            }
        }


        {
            List<TPersonBean> relatedBeans = bean.getTPersonBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPersonBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPersonBean relatedBean =  relatedBeansIt.next();
                    TPerson related = TPerson.createTPerson(relatedBean, createdObjects);
                    result.addTPersonFromBean(related);
                }
            }
        }


        {
            List<TPriorityBean> relatedBeans = bean.getTPriorityBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPriorityBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPriorityBean relatedBean =  relatedBeansIt.next();
                    TPriority related = TPriority.createTPriority(relatedBean, createdObjects);
                    result.addTPriorityFromBean(related);
                }
            }
        }


        {
            List<TSeverityBean> relatedBeans = bean.getTSeverityBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TSeverityBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TSeverityBean relatedBean =  relatedBeansIt.next();
                    TSeverity related = TSeverity.createTSeverity(relatedBean, createdObjects);
                    result.addTSeverityFromBean(related);
                }
            }
        }


        {
            List<TStateBean> relatedBeans = bean.getTStateBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TStateBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TStateBean relatedBean =  relatedBeansIt.next();
                    TState related = TState.createTState(relatedBean, createdObjects);
                    result.addTStateFromBean(related);
                }
            }
        }


        {
            List<TSystemStateBean> relatedBeans = bean.getTSystemStateBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TSystemStateBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TSystemStateBean relatedBean =  relatedBeansIt.next();
                    TSystemState related = TSystemState.createTSystemState(relatedBean, createdObjects);
                    result.addTSystemStateFromBean(related);
                }
            }
        }


        {
            List<TOptionBean> relatedBeans = bean.getTOptionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TOptionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TOptionBean relatedBean =  relatedBeansIt.next();
                    TOption related = TOption.createTOption(relatedBean, createdObjects);
                    result.addTOptionFromBean(related);
                }
            }
        }


        {
            List<TActionBean> relatedBeans = bean.getTActionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TActionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TActionBean relatedBean =  relatedBeansIt.next();
                    TAction related = TAction.createTAction(relatedBean, createdObjects);
                    result.addTActionFromBean(related);
                }
            }
        }


        {
            List<TLinkTypeBean> relatedBeans = bean.getTLinkTypeBeansRelatedByOutwardIconKey();
            if (relatedBeans != null)
            {
                for (Iterator<TLinkTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TLinkTypeBean relatedBean =  relatedBeansIt.next();
                    TLinkType related = TLinkType.createTLinkType(relatedBean, createdObjects);
                    result.addTLinkTypeRelatedByOutwardIconKeyFromBean(related);
                }
            }
        }


        {
            List<TLinkTypeBean> relatedBeans = bean.getTLinkTypeBeansRelatedByInwardIconKey();
            if (relatedBeans != null)
            {
                for (Iterator<TLinkTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TLinkTypeBean relatedBean =  relatedBeansIt.next();
                    TLinkType related = TLinkType.createTLinkType(relatedBean, createdObjects);
                    result.addTLinkTypeRelatedByInwardIconKeyFromBean(related);
                }
            }
        }

    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TListType object to this object.
     * through the TListType foreign key attribute
     *
     * @param toAdd TListType
     */
    protected void addTListTypeFromBean(TListType toAdd)
    {
        initTListTypes();
        collTListTypes.add(toAdd);
    }


    /**
     * Method called to associate a TPerson object to this object.
     * through the TPerson foreign key attribute
     *
     * @param toAdd TPerson
     */
    protected void addTPersonFromBean(TPerson toAdd)
    {
        initTPersons();
        collTPersons.add(toAdd);
    }


    /**
     * Method called to associate a TPriority object to this object.
     * through the TPriority foreign key attribute
     *
     * @param toAdd TPriority
     */
    protected void addTPriorityFromBean(TPriority toAdd)
    {
        initTPrioritys();
        collTPrioritys.add(toAdd);
    }


    /**
     * Method called to associate a TSeverity object to this object.
     * through the TSeverity foreign key attribute
     *
     * @param toAdd TSeverity
     */
    protected void addTSeverityFromBean(TSeverity toAdd)
    {
        initTSeveritys();
        collTSeveritys.add(toAdd);
    }


    /**
     * Method called to associate a TState object to this object.
     * through the TState foreign key attribute
     *
     * @param toAdd TState
     */
    protected void addTStateFromBean(TState toAdd)
    {
        initTStates();
        collTStates.add(toAdd);
    }


    /**
     * Method called to associate a TSystemState object to this object.
     * through the TSystemState foreign key attribute
     *
     * @param toAdd TSystemState
     */
    protected void addTSystemStateFromBean(TSystemState toAdd)
    {
        initTSystemStates();
        collTSystemStates.add(toAdd);
    }


    /**
     * Method called to associate a TOption object to this object.
     * through the TOption foreign key attribute
     *
     * @param toAdd TOption
     */
    protected void addTOptionFromBean(TOption toAdd)
    {
        initTOptions();
        collTOptions.add(toAdd);
    }


    /**
     * Method called to associate a TAction object to this object.
     * through the TAction foreign key attribute
     *
     * @param toAdd TAction
     */
    protected void addTActionFromBean(TAction toAdd)
    {
        initTActions();
        collTActions.add(toAdd);
    }


    /**
     * Method called to associate a TLinkType object to this object.
     * through the TLinkType foreign key attribute
     *
     * @param toAdd TLinkType
     */
    protected void addTLinkTypeRelatedByOutwardIconKeyFromBean(TLinkType toAdd)
    {
        initTLinkTypesRelatedByOutwardIconKey();
        collTLinkTypesRelatedByOutwardIconKey.add(toAdd);
    }


    /**
     * Method called to associate a TLinkType object to this object.
     * through the TLinkType foreign key attribute
     *
     * @param toAdd TLinkType
     */
    protected void addTLinkTypeRelatedByInwardIconKeyFromBean(TLinkType toAdd)
    {
        initTLinkTypesRelatedByInwardIconKey();
        collTLinkTypesRelatedByInwardIconKey.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TBLOB:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("BLOBValue = ")
           .append("<binary>")
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
