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



import com.aurel.track.persist.TCostCenter;
import com.aurel.track.persist.TCostCenterPeer;
import com.aurel.track.persist.TDomain;
import com.aurel.track.persist.TDomainPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TDepartmentBean;
import com.aurel.track.beans.TCostCenterBean;
import com.aurel.track.beans.TDomainBean;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TOrgProjectSLABean;


/**
 * This table holds the departments. Each user belongs to one department.
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TDepartment
 */
public abstract class BaseTDepartment extends TpBaseObject
{
    /** The Peer class */
    private static final TDepartmentPeer peer =
        new TDepartmentPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the costcenter field */
    private Integer costcenter;

    /** The value for the parent field */
    private Integer parent;

    /** The value for the domain field */
    private Integer domain;

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



        // update associated TPerson
        if (collTPersons != null)
        {
            for (int i = 0; i < collTPersons.size(); i++)
            {
                ((TPerson) collTPersons.get(i))
                        .setDepartmentID(v);
            }
        }

        // update associated TOrgProjectSLA
        if (collTOrgProjectSLAs != null)
        {
            for (int i = 0; i < collTOrgProjectSLAs.size(); i++)
            {
                ((TOrgProjectSLA) collTOrgProjectSLAs.get(i))
                        .setDepartment(v);
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
     * Get the Costcenter
     *
     * @return Integer
     */
    public Integer getCostcenter()
    {
        return costcenter;
    }


    /**
     * Set the value of Costcenter
     *
     * @param v new value
     */
    public void setCostcenter(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.costcenter, v))
        {
            this.costcenter = v;
            setModified(true);
        }


        if (aTCostCenter != null && !ObjectUtils.equals(aTCostCenter.getObjectID(), v))
        {
            aTCostCenter = null;
        }

    }

    /**
     * Get the Parent
     *
     * @return Integer
     */
    public Integer getParent()
    {
        return parent;
    }


    /**
     * Set the value of Parent
     *
     * @param v new value
     */
    public void setParent(Integer v) 
    {

        if (!ObjectUtils.equals(this.parent, v))
        {
            this.parent = v;
            setModified(true);
        }


    }

    /**
     * Get the Domain
     *
     * @return Integer
     */
    public Integer getDomain()
    {
        return domain;
    }


    /**
     * Set the value of Domain
     *
     * @param v new value
     */
    public void setDomain(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.domain, v))
        {
            this.domain = v;
            setModified(true);
        }


        if (aTDomain != null && !ObjectUtils.equals(aTDomain.getObjectID(), v))
        {
            aTDomain = null;
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
            setCostcenter((Integer) null);
        }
        else
        {
            setCostcenter(v.getObjectID());
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
        if (aTCostCenter == null && (!ObjectUtils.equals(this.costcenter, null)))
        {
            aTCostCenter = TCostCenterPeer.retrieveByPK(SimpleKey.keyFor(this.costcenter));
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
        if (aTCostCenter == null && (!ObjectUtils.equals(this.costcenter, null)))
        {
            aTCostCenter = TCostCenterPeer.retrieveByPK(SimpleKey.keyFor(this.costcenter), connection);
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

        setCostcenter(new Integer(((NumberKey) key).intValue()));
    }




    private TDomain aTDomain;

    /**
     * Declares an association between this object and a TDomain object
     *
     * @param v TDomain
     * @throws TorqueException
     */
    public void setTDomain(TDomain v) throws TorqueException
    {
        if (v == null)
        {
            setDomain((Integer) null);
        }
        else
        {
            setDomain(v.getObjectID());
        }
        aTDomain = v;
    }


    /**
     * Returns the associated TDomain object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TDomain object
     * @throws TorqueException
     */
    public TDomain getTDomain()
        throws TorqueException
    {
        if (aTDomain == null && (!ObjectUtils.equals(this.domain, null)))
        {
            aTDomain = TDomainPeer.retrieveByPK(SimpleKey.keyFor(this.domain));
        }
        return aTDomain;
    }

    /**
     * Return the associated TDomain object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TDomain object
     * @throws TorqueException
     */
    public TDomain getTDomain(Connection connection)
        throws TorqueException
    {
        if (aTDomain == null && (!ObjectUtils.equals(this.domain, null)))
        {
            aTDomain = TDomainPeer.retrieveByPK(SimpleKey.keyFor(this.domain), connection);
        }
        return aTDomain;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTDomainKey(ObjectKey key) throws TorqueException
    {

        setDomain(new Integer(((NumberKey) key).intValue()));
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
        l.setTDepartment((TDepartment) this);
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
        l.setTDepartment((TDepartment) this);
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
     * Otherwise if this TDepartment has previously
     * been saved, it will retrieve related TPersons from storage.
     * If this TDepartment is new, it will return
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
                criteria.add(TPersonPeer.DEPKEY, getObjectID() );
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
                criteria.add(TPersonPeer.DEPKEY, getObjectID());
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
     * Otherwise if this TDepartment has previously
     * been saved, it will retrieve related TPersons from storage.
     * If this TDepartment is new, it will return
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
                 criteria.add(TPersonPeer.DEPKEY, getObjectID());
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
                 criteria.add(TPersonPeer.DEPKEY, getObjectID());
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
     * Otherwise if this TDepartment is new, it will return
     * an empty collection; or if this TDepartment has previously
     * been saved, it will retrieve related TPersons from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDepartment.
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
                criteria.add(TPersonPeer.DEPKEY, getObjectID());
                collTPersons = TPersonPeer.doSelectJoinTDepartment(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonPeer.DEPKEY, getObjectID());
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
     * Otherwise if this TDepartment is new, it will return
     * an empty collection; or if this TDepartment has previously
     * been saved, it will retrieve related TPersons from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDepartment.
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
                criteria.add(TPersonPeer.DEPKEY, getObjectID());
                collTPersons = TPersonPeer.doSelectJoinTPrivateReportRepository(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonPeer.DEPKEY, getObjectID());
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
     * Otherwise if this TDepartment is new, it will return
     * an empty collection; or if this TDepartment has previously
     * been saved, it will retrieve related TPersons from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDepartment.
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
                criteria.add(TPersonPeer.DEPKEY, getObjectID());
                collTPersons = TPersonPeer.doSelectJoinTBLOB(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonPeer.DEPKEY, getObjectID());
            if (!lastTPersonsCriteria.equals(criteria))
            {
                collTPersons = TPersonPeer.doSelectJoinTBLOB(criteria);
            }
        }
        lastTPersonsCriteria = criteria;

        return collTPersons;
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
        l.setTDepartment((TDepartment) this);
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
        l.setTDepartment((TDepartment) this);
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
     * Otherwise if this TDepartment has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     * If this TDepartment is new, it will return
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
                criteria.add(TOrgProjectSLAPeer.DEPARTMENT, getObjectID() );
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
                criteria.add(TOrgProjectSLAPeer.DEPARTMENT, getObjectID());
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
     * Otherwise if this TDepartment has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     * If this TDepartment is new, it will return
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
                 criteria.add(TOrgProjectSLAPeer.DEPARTMENT, getObjectID());
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
                 criteria.add(TOrgProjectSLAPeer.DEPARTMENT, getObjectID());
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
     * Otherwise if this TDepartment is new, it will return
     * an empty collection; or if this TDepartment has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDepartment.
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
                criteria.add(TOrgProjectSLAPeer.DEPARTMENT, getObjectID());
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTDepartment(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOrgProjectSLAPeer.DEPARTMENT, getObjectID());
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
     * Otherwise if this TDepartment is new, it will return
     * an empty collection; or if this TDepartment has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDepartment.
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
                criteria.add(TOrgProjectSLAPeer.DEPARTMENT, getObjectID());
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOrgProjectSLAPeer.DEPARTMENT, getObjectID());
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
     * Otherwise if this TDepartment is new, it will return
     * an empty collection; or if this TDepartment has previously
     * been saved, it will retrieve related TOrgProjectSLAs from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDepartment.
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
                criteria.add(TOrgProjectSLAPeer.DEPARTMENT, getObjectID());
                collTOrgProjectSLAs = TOrgProjectSLAPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TOrgProjectSLAPeer.DEPARTMENT, getObjectID());
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
            fieldNames.add("Label");
            fieldNames.add("Costcenter");
            fieldNames.add("Parent");
            fieldNames.add("Domain");
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
        if (name.equals("Costcenter"))
        {
            return getCostcenter();
        }
        if (name.equals("Parent"))
        {
            return getParent();
        }
        if (name.equals("Domain"))
        {
            return getDomain();
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
        if (name.equals("Costcenter"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCostcenter((Integer) value);
            return true;
        }
        if (name.equals("Parent"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParent((Integer) value);
            return true;
        }
        if (name.equals("Domain"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDomain((Integer) value);
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
        if (name.equals(TDepartmentPeer.PKEY))
        {
            return getObjectID();
        }
        if (name.equals(TDepartmentPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TDepartmentPeer.COSTCENTER))
        {
            return getCostcenter();
        }
        if (name.equals(TDepartmentPeer.PARENT))
        {
            return getParent();
        }
        if (name.equals(TDepartmentPeer.DOMAINKEY))
        {
            return getDomain();
        }
        if (name.equals(TDepartmentPeer.TPUUID))
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
      if (TDepartmentPeer.PKEY.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TDepartmentPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TDepartmentPeer.COSTCENTER.equals(name))
        {
            return setByName("Costcenter", value);
        }
      if (TDepartmentPeer.PARENT.equals(name))
        {
            return setByName("Parent", value);
        }
      if (TDepartmentPeer.DOMAINKEY.equals(name))
        {
            return setByName("Domain", value);
        }
      if (TDepartmentPeer.TPUUID.equals(name))
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
            return getCostcenter();
        }
        if (pos == 3)
        {
            return getParent();
        }
        if (pos == 4)
        {
            return getDomain();
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
            return setByName("Label", value);
        }
    if (position == 2)
        {
            return setByName("Costcenter", value);
        }
    if (position == 3)
        {
            return setByName("Parent", value);
        }
    if (position == 4)
        {
            return setByName("Domain", value);
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
        save(TDepartmentPeer.DATABASE_NAME);
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
                    TDepartmentPeer.doInsert((TDepartment) this, con);
                    setNew(false);
                }
                else
                {
                    TDepartmentPeer.doUpdate((TDepartment) this, con);
                }
            }


            if (collTPersons != null)
            {
                for (int i = 0; i < collTPersons.size(); i++)
                {
                    ((TPerson) collTPersons.get(i)).save(con);
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
    public TDepartment copy() throws TorqueException
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
    public TDepartment copy(Connection con) throws TorqueException
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
    public TDepartment copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TDepartment(), deepcopy);
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
    public TDepartment copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TDepartment(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TDepartment copyInto(TDepartment copyObj) throws TorqueException
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
    protected TDepartment copyInto(TDepartment copyObj, Connection con) throws TorqueException
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
    protected TDepartment copyInto(TDepartment copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setCostcenter(costcenter);
        copyObj.setParent(parent);
        copyObj.setDomain(domain);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


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
    protected TDepartment copyInto(TDepartment copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setCostcenter(costcenter);
        copyObj.setParent(parent);
        copyObj.setDomain(domain);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


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
    public TDepartmentPeer getPeer()
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
        return TDepartmentPeer.getTableMap();
    }

  
    /**
     * Creates a TDepartmentBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TDepartmentBean with the contents of this object
     */
    public TDepartmentBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TDepartmentBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TDepartmentBean with the contents of this object
     */
    public TDepartmentBean getBean(IdentityMap createdBeans)
    {
        TDepartmentBean result = (TDepartmentBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TDepartmentBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setCostcenter(getCostcenter());
        result.setParent(getParent());
        result.setDomain(getDomain());
        result.setUuid(getUuid());



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




        if (aTCostCenter != null)
        {
            TCostCenterBean relatedBean = aTCostCenter.getBean(createdBeans);
            result.setTCostCenterBean(relatedBean);
        }



        if (aTDomain != null)
        {
            TDomainBean relatedBean = aTDomain.getBean(createdBeans);
            result.setTDomainBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TDepartment with the contents
     * of a TDepartmentBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TDepartmentBean which contents are used to create
     *        the resulting class
     * @return an instance of TDepartment with the contents of bean
     */
    public static TDepartment createTDepartment(TDepartmentBean bean)
        throws TorqueException
    {
        return createTDepartment(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TDepartment with the contents
     * of a TDepartmentBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TDepartmentBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TDepartment with the contents of bean
     */

    public static TDepartment createTDepartment(TDepartmentBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TDepartment result = (TDepartment) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TDepartment();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setCostcenter(bean.getCostcenter());
        result.setParent(bean.getParent());
        result.setDomain(bean.getDomain());
        result.setUuid(bean.getUuid());



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




        {
            TCostCenterBean relatedBean = bean.getTCostCenterBean();
            if (relatedBean != null)
            {
                TCostCenter relatedObject = TCostCenter.createTCostCenter(relatedBean, createdObjects);
                result.setTCostCenter(relatedObject);
            }
        }



        {
            TDomainBean relatedBean = bean.getTDomainBean();
            if (relatedBean != null)
            {
                TDomain relatedObject = TDomain.createTDomain(relatedBean, createdObjects);
                result.setTDomain(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
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
        str.append("TDepartment:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Costcenter = ")
           .append(getCostcenter())
           .append("\n");
        str.append("Parent = ")
           .append(getParent())
           .append("\n");
        str.append("Domain = ")
           .append(getDomain())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
