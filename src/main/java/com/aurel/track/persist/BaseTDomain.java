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




  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TDomainBean;

import com.aurel.track.beans.TDepartmentBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TPersonInDomainBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TDomain
 */
public abstract class BaseTDomain extends TpBaseObject
{
    /** The Peer class */
    private static final TDomainPeer peer =
        new TDomainPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the description field */
    private String description;

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



        // update associated TDepartment
        if (collTDepartments != null)
        {
            for (int i = 0; i < collTDepartments.size(); i++)
            {
                ((TDepartment) collTDepartments.get(i))
                        .setDomain(v);
            }
        }

        // update associated TProject
        if (collTProjects != null)
        {
            for (int i = 0; i < collTProjects.size(); i++)
            {
                ((TProject) collTProjects.get(i))
                        .setDomain(v);
            }
        }

        // update associated TPersonInDomain
        if (collTPersonInDomains != null)
        {
            for (int i = 0; i < collTPersonInDomains.size(); i++)
            {
                ((TPersonInDomain) collTPersonInDomains.get(i))
                        .setDomain(v);
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
     * Collection to store aggregation of collTDepartments
     */
    protected List<TDepartment> collTDepartments;

    /**
     * Temporary storage of collTDepartments to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTDepartments()
    {
        if (collTDepartments == null)
        {
            collTDepartments = new ArrayList<TDepartment>();
        }
    }


    /**
     * Method called to associate a TDepartment object to this object
     * through the TDepartment foreign key attribute
     *
     * @param l TDepartment
     * @throws TorqueException
     */
    public void addTDepartment(TDepartment l) throws TorqueException
    {
        getTDepartments().add(l);
        l.setTDomain((TDomain) this);
    }

    /**
     * Method called to associate a TDepartment object to this object
     * through the TDepartment foreign key attribute using connection.
     *
     * @param l TDepartment
     * @throws TorqueException
     */
    public void addTDepartment(TDepartment l, Connection con) throws TorqueException
    {
        getTDepartments(con).add(l);
        l.setTDomain((TDomain) this);
    }

    /**
     * The criteria used to select the current contents of collTDepartments
     */
    private Criteria lastTDepartmentsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDepartments(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TDepartment> getTDepartments()
        throws TorqueException
    {
        if (collTDepartments == null)
        {
            collTDepartments = getTDepartments(new Criteria(10));
        }
        return collTDepartments;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDomain has previously
     * been saved, it will retrieve related TDepartments from storage.
     * If this TDomain is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TDepartment> getTDepartments(Criteria criteria) throws TorqueException
    {
        if (collTDepartments == null)
        {
            if (isNew())
            {
               collTDepartments = new ArrayList<TDepartment>();
            }
            else
            {
                criteria.add(TDepartmentPeer.DOMAINKEY, getObjectID() );
                collTDepartments = TDepartmentPeer.doSelect(criteria);
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
                criteria.add(TDepartmentPeer.DOMAINKEY, getObjectID());
                if (!lastTDepartmentsCriteria.equals(criteria))
                {
                    collTDepartments = TDepartmentPeer.doSelect(criteria);
                }
            }
        }
        lastTDepartmentsCriteria = criteria;

        return collTDepartments;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTDepartments(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDepartment> getTDepartments(Connection con) throws TorqueException
    {
        if (collTDepartments == null)
        {
            collTDepartments = getTDepartments(new Criteria(10), con);
        }
        return collTDepartments;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDomain has previously
     * been saved, it will retrieve related TDepartments from storage.
     * If this TDomain is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TDepartment> getTDepartments(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTDepartments == null)
        {
            if (isNew())
            {
               collTDepartments = new ArrayList<TDepartment>();
            }
            else
            {
                 criteria.add(TDepartmentPeer.DOMAINKEY, getObjectID());
                 collTDepartments = TDepartmentPeer.doSelect(criteria, con);
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
                 criteria.add(TDepartmentPeer.DOMAINKEY, getObjectID());
                 if (!lastTDepartmentsCriteria.equals(criteria))
                 {
                     collTDepartments = TDepartmentPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTDepartmentsCriteria = criteria;

         return collTDepartments;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDomain is new, it will return
     * an empty collection; or if this TDomain has previously
     * been saved, it will retrieve related TDepartments from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDomain.
     */
    protected List<TDepartment> getTDepartmentsJoinTCostCenter(Criteria criteria)
        throws TorqueException
    {
        if (collTDepartments == null)
        {
            if (isNew())
            {
               collTDepartments = new ArrayList<TDepartment>();
            }
            else
            {
                criteria.add(TDepartmentPeer.DOMAINKEY, getObjectID());
                collTDepartments = TDepartmentPeer.doSelectJoinTCostCenter(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDepartmentPeer.DOMAINKEY, getObjectID());
            if (!lastTDepartmentsCriteria.equals(criteria))
            {
                collTDepartments = TDepartmentPeer.doSelectJoinTCostCenter(criteria);
            }
        }
        lastTDepartmentsCriteria = criteria;

        return collTDepartments;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDomain is new, it will return
     * an empty collection; or if this TDomain has previously
     * been saved, it will retrieve related TDepartments from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDomain.
     */
    protected List<TDepartment> getTDepartmentsJoinTDomain(Criteria criteria)
        throws TorqueException
    {
        if (collTDepartments == null)
        {
            if (isNew())
            {
               collTDepartments = new ArrayList<TDepartment>();
            }
            else
            {
                criteria.add(TDepartmentPeer.DOMAINKEY, getObjectID());
                collTDepartments = TDepartmentPeer.doSelectJoinTDomain(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDepartmentPeer.DOMAINKEY, getObjectID());
            if (!lastTDepartmentsCriteria.equals(criteria))
            {
                collTDepartments = TDepartmentPeer.doSelectJoinTDomain(criteria);
            }
        }
        lastTDepartmentsCriteria = criteria;

        return collTDepartments;
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
        l.setTDomain((TDomain) this);
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
        l.setTDomain((TDomain) this);
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
     * Otherwise if this TDomain has previously
     * been saved, it will retrieve related TProjects from storage.
     * If this TDomain is new, it will return
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
                criteria.add(TProjectPeer.DOMAINKEY, getObjectID() );
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
                criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
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
     * Otherwise if this TDomain has previously
     * been saved, it will retrieve related TProjects from storage.
     * If this TDomain is new, it will return
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
                 criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
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
                 criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
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
     * Otherwise if this TDomain is new, it will return
     * an empty collection; or if this TDomain has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDomain.
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
                criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultOwnerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
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
     * Otherwise if this TDomain is new, it will return
     * an empty collection; or if this TDomain has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDomain.
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
                criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTPersonRelatedByDefaultManagerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
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
     * Otherwise if this TDomain is new, it will return
     * an empty collection; or if this TDomain has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDomain.
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
                criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
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
     * Otherwise if this TDomain is new, it will return
     * an empty collection; or if this TDomain has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDomain.
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
                criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
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
     * Otherwise if this TDomain is new, it will return
     * an empty collection; or if this TDomain has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDomain.
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
                criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTSystemState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
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
     * Otherwise if this TDomain is new, it will return
     * an empty collection; or if this TDomain has previously
     * been saved, it will retrieve related TProjects from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDomain.
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
                criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
                collTProjects = TProjectPeer.doSelectJoinTDomain(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TProjectPeer.DOMAINKEY, getObjectID());
            if (!lastTProjectsCriteria.equals(criteria))
            {
                collTProjects = TProjectPeer.doSelectJoinTDomain(criteria);
            }
        }
        lastTProjectsCriteria = criteria;

        return collTProjects;
    }





    /**
     * Collection to store aggregation of collTPersonInDomains
     */
    protected List<TPersonInDomain> collTPersonInDomains;

    /**
     * Temporary storage of collTPersonInDomains to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPersonInDomains()
    {
        if (collTPersonInDomains == null)
        {
            collTPersonInDomains = new ArrayList<TPersonInDomain>();
        }
    }


    /**
     * Method called to associate a TPersonInDomain object to this object
     * through the TPersonInDomain foreign key attribute
     *
     * @param l TPersonInDomain
     * @throws TorqueException
     */
    public void addTPersonInDomain(TPersonInDomain l) throws TorqueException
    {
        getTPersonInDomains().add(l);
        l.setTDomain((TDomain) this);
    }

    /**
     * Method called to associate a TPersonInDomain object to this object
     * through the TPersonInDomain foreign key attribute using connection.
     *
     * @param l TPersonInDomain
     * @throws TorqueException
     */
    public void addTPersonInDomain(TPersonInDomain l, Connection con) throws TorqueException
    {
        getTPersonInDomains(con).add(l);
        l.setTDomain((TDomain) this);
    }

    /**
     * The criteria used to select the current contents of collTPersonInDomains
     */
    private Criteria lastTPersonInDomainsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPersonInDomains(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPersonInDomain> getTPersonInDomains()
        throws TorqueException
    {
        if (collTPersonInDomains == null)
        {
            collTPersonInDomains = getTPersonInDomains(new Criteria(10));
        }
        return collTPersonInDomains;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDomain has previously
     * been saved, it will retrieve related TPersonInDomains from storage.
     * If this TDomain is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPersonInDomain> getTPersonInDomains(Criteria criteria) throws TorqueException
    {
        if (collTPersonInDomains == null)
        {
            if (isNew())
            {
               collTPersonInDomains = new ArrayList<TPersonInDomain>();
            }
            else
            {
                criteria.add(TPersonInDomainPeer.DOMAINKEY, getObjectID() );
                collTPersonInDomains = TPersonInDomainPeer.doSelect(criteria);
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
                criteria.add(TPersonInDomainPeer.DOMAINKEY, getObjectID());
                if (!lastTPersonInDomainsCriteria.equals(criteria))
                {
                    collTPersonInDomains = TPersonInDomainPeer.doSelect(criteria);
                }
            }
        }
        lastTPersonInDomainsCriteria = criteria;

        return collTPersonInDomains;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPersonInDomains(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPersonInDomain> getTPersonInDomains(Connection con) throws TorqueException
    {
        if (collTPersonInDomains == null)
        {
            collTPersonInDomains = getTPersonInDomains(new Criteria(10), con);
        }
        return collTPersonInDomains;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDomain has previously
     * been saved, it will retrieve related TPersonInDomains from storage.
     * If this TDomain is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPersonInDomain> getTPersonInDomains(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPersonInDomains == null)
        {
            if (isNew())
            {
               collTPersonInDomains = new ArrayList<TPersonInDomain>();
            }
            else
            {
                 criteria.add(TPersonInDomainPeer.DOMAINKEY, getObjectID());
                 collTPersonInDomains = TPersonInDomainPeer.doSelect(criteria, con);
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
                 criteria.add(TPersonInDomainPeer.DOMAINKEY, getObjectID());
                 if (!lastTPersonInDomainsCriteria.equals(criteria))
                 {
                     collTPersonInDomains = TPersonInDomainPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPersonInDomainsCriteria = criteria;

         return collTPersonInDomains;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDomain is new, it will return
     * an empty collection; or if this TDomain has previously
     * been saved, it will retrieve related TPersonInDomains from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDomain.
     */
    protected List<TPersonInDomain> getTPersonInDomainsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTPersonInDomains == null)
        {
            if (isNew())
            {
               collTPersonInDomains = new ArrayList<TPersonInDomain>();
            }
            else
            {
                criteria.add(TPersonInDomainPeer.DOMAINKEY, getObjectID());
                collTPersonInDomains = TPersonInDomainPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonInDomainPeer.DOMAINKEY, getObjectID());
            if (!lastTPersonInDomainsCriteria.equals(criteria))
            {
                collTPersonInDomains = TPersonInDomainPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTPersonInDomainsCriteria = criteria;

        return collTPersonInDomains;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TDomain is new, it will return
     * an empty collection; or if this TDomain has previously
     * been saved, it will retrieve related TPersonInDomains from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TDomain.
     */
    protected List<TPersonInDomain> getTPersonInDomainsJoinTDomain(Criteria criteria)
        throws TorqueException
    {
        if (collTPersonInDomains == null)
        {
            if (isNew())
            {
               collTPersonInDomains = new ArrayList<TPersonInDomain>();
            }
            else
            {
                criteria.add(TPersonInDomainPeer.DOMAINKEY, getObjectID());
                collTPersonInDomains = TPersonInDomainPeer.doSelectJoinTDomain(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonInDomainPeer.DOMAINKEY, getObjectID());
            if (!lastTPersonInDomainsCriteria.equals(criteria))
            {
                collTPersonInDomains = TPersonInDomainPeer.doSelectJoinTDomain(criteria);
            }
        }
        lastTPersonInDomainsCriteria = criteria;

        return collTPersonInDomains;
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
            fieldNames.add("Description");
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
        if (name.equals("Description"))
        {
            return getDescription();
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
        if (name.equals(TDomainPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TDomainPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TDomainPeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TDomainPeer.TPUUID))
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
      if (TDomainPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TDomainPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TDomainPeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TDomainPeer.TPUUID.equals(name))
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
            return getDescription();
        }
        if (pos == 3)
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
            return setByName("Description", value);
        }
    if (position == 3)
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
        save(TDomainPeer.DATABASE_NAME);
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
                    TDomainPeer.doInsert((TDomain) this, con);
                    setNew(false);
                }
                else
                {
                    TDomainPeer.doUpdate((TDomain) this, con);
                }
            }


            if (collTDepartments != null)
            {
                for (int i = 0; i < collTDepartments.size(); i++)
                {
                    ((TDepartment) collTDepartments.get(i)).save(con);
                }
            }

            if (collTProjects != null)
            {
                for (int i = 0; i < collTProjects.size(); i++)
                {
                    ((TProject) collTProjects.get(i)).save(con);
                }
            }

            if (collTPersonInDomains != null)
            {
                for (int i = 0; i < collTPersonInDomains.size(); i++)
                {
                    ((TPersonInDomain) collTPersonInDomains.get(i)).save(con);
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
    public TDomain copy() throws TorqueException
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
    public TDomain copy(Connection con) throws TorqueException
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
    public TDomain copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TDomain(), deepcopy);
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
    public TDomain copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TDomain(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TDomain copyInto(TDomain copyObj) throws TorqueException
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
    protected TDomain copyInto(TDomain copyObj, Connection con) throws TorqueException
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
    protected TDomain copyInto(TDomain copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setDescription(description);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TDepartment> vTDepartments = getTDepartments();
        if (vTDepartments != null)
        {
            for (int i = 0; i < vTDepartments.size(); i++)
            {
                TDepartment obj =  vTDepartments.get(i);
                copyObj.addTDepartment(obj.copy());
            }
        }
        else
        {
            copyObj.collTDepartments = null;
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


        List<TPersonInDomain> vTPersonInDomains = getTPersonInDomains();
        if (vTPersonInDomains != null)
        {
            for (int i = 0; i < vTPersonInDomains.size(); i++)
            {
                TPersonInDomain obj =  vTPersonInDomains.get(i);
                copyObj.addTPersonInDomain(obj.copy());
            }
        }
        else
        {
            copyObj.collTPersonInDomains = null;
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
    protected TDomain copyInto(TDomain copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setDescription(description);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TDepartment> vTDepartments = getTDepartments(con);
        if (vTDepartments != null)
        {
            for (int i = 0; i < vTDepartments.size(); i++)
            {
                TDepartment obj =  vTDepartments.get(i);
                copyObj.addTDepartment(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTDepartments = null;
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


        List<TPersonInDomain> vTPersonInDomains = getTPersonInDomains(con);
        if (vTPersonInDomains != null)
        {
            for (int i = 0; i < vTPersonInDomains.size(); i++)
            {
                TPersonInDomain obj =  vTPersonInDomains.get(i);
                copyObj.addTPersonInDomain(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPersonInDomains = null;
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
    public TDomainPeer getPeer()
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
        return TDomainPeer.getTableMap();
    }

  
    /**
     * Creates a TDomainBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TDomainBean with the contents of this object
     */
    public TDomainBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TDomainBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TDomainBean with the contents of this object
     */
    public TDomainBean getBean(IdentityMap createdBeans)
    {
        TDomainBean result = (TDomainBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TDomainBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setDescription(getDescription());
        result.setUuid(getUuid());



        if (collTDepartments != null)
        {
            List<TDepartmentBean> relatedBeans = new ArrayList<TDepartmentBean>(collTDepartments.size());
            for (Iterator<TDepartment> collTDepartmentsIt = collTDepartments.iterator(); collTDepartmentsIt.hasNext(); )
            {
                TDepartment related = (TDepartment) collTDepartmentsIt.next();
                TDepartmentBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTDepartmentBeans(relatedBeans);
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


        if (collTPersonInDomains != null)
        {
            List<TPersonInDomainBean> relatedBeans = new ArrayList<TPersonInDomainBean>(collTPersonInDomains.size());
            for (Iterator<TPersonInDomain> collTPersonInDomainsIt = collTPersonInDomains.iterator(); collTPersonInDomainsIt.hasNext(); )
            {
                TPersonInDomain related = (TPersonInDomain) collTPersonInDomainsIt.next();
                TPersonInDomainBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPersonInDomainBeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TDomain with the contents
     * of a TDomainBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TDomainBean which contents are used to create
     *        the resulting class
     * @return an instance of TDomain with the contents of bean
     */
    public static TDomain createTDomain(TDomainBean bean)
        throws TorqueException
    {
        return createTDomain(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TDomain with the contents
     * of a TDomainBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TDomainBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TDomain with the contents of bean
     */

    public static TDomain createTDomain(TDomainBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TDomain result = (TDomain) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TDomain();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setDescription(bean.getDescription());
        result.setUuid(bean.getUuid());



        {
            List<TDepartmentBean> relatedBeans = bean.getTDepartmentBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TDepartmentBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TDepartmentBean relatedBean =  relatedBeansIt.next();
                    TDepartment related = TDepartment.createTDepartment(relatedBean, createdObjects);
                    result.addTDepartmentFromBean(related);
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
            List<TPersonInDomainBean> relatedBeans = bean.getTPersonInDomainBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPersonInDomainBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPersonInDomainBean relatedBean =  relatedBeansIt.next();
                    TPersonInDomain related = TPersonInDomain.createTPersonInDomain(relatedBean, createdObjects);
                    result.addTPersonInDomainFromBean(related);
                }
            }
        }

    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TDepartment object to this object.
     * through the TDepartment foreign key attribute
     *
     * @param toAdd TDepartment
     */
    protected void addTDepartmentFromBean(TDepartment toAdd)
    {
        initTDepartments();
        collTDepartments.add(toAdd);
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
     * Method called to associate a TPersonInDomain object to this object.
     * through the TPersonInDomain foreign key attribute
     *
     * @param toAdd TPersonInDomain
     */
    protected void addTPersonInDomainFromBean(TPersonInDomain toAdd)
    {
        initTPersonInDomains();
        collTPersonInDomains.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TDomain:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
