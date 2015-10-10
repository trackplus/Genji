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



import com.aurel.track.persist.TState;
import com.aurel.track.persist.TStatePeer;
import com.aurel.track.persist.TState;
import com.aurel.track.persist.TStatePeer;
import com.aurel.track.persist.TProjectType;
import com.aurel.track.persist.TProjectTypePeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TWorkFlowBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TPersonBean;

import com.aurel.track.beans.TWorkFlowRoleBean;
import com.aurel.track.beans.TWorkFlowCategoryBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TWorkFlow
 */
public abstract class BaseTWorkFlow extends TpBaseObject
{
    /** The Peer class */
    private static final TWorkFlowPeer peer =
        new TWorkFlowPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the stateFrom field */
    private Integer stateFrom;

    /** The value for the stateTo field */
    private Integer stateTo;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the responsible field */
    private Integer responsible;

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



        // update associated TWorkFlowRole
        if (collTWorkFlowRoles != null)
        {
            for (int i = 0; i < collTWorkFlowRoles.size(); i++)
            {
                ((TWorkFlowRole) collTWorkFlowRoles.get(i))
                        .setWorkFlow(v);
            }
        }

        // update associated TWorkFlowCategory
        if (collTWorkFlowCategorys != null)
        {
            for (int i = 0; i < collTWorkFlowCategorys.size(); i++)
            {
                ((TWorkFlowCategory) collTWorkFlowCategorys.get(i))
                        .setWorkFlow(v);
            }
        }
    }

    /**
     * Get the StateFrom
     *
     * @return Integer
     */
    public Integer getStateFrom()
    {
        return stateFrom;
    }


    /**
     * Set the value of StateFrom
     *
     * @param v new value
     */
    public void setStateFrom(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.stateFrom, v))
        {
            this.stateFrom = v;
            setModified(true);
        }


        if (aTStateRelatedByStateFrom != null && !ObjectUtils.equals(aTStateRelatedByStateFrom.getObjectID(), v))
        {
            aTStateRelatedByStateFrom = null;
        }

    }

    /**
     * Get the StateTo
     *
     * @return Integer
     */
    public Integer getStateTo()
    {
        return stateTo;
    }


    /**
     * Set the value of StateTo
     *
     * @param v new value
     */
    public void setStateTo(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.stateTo, v))
        {
            this.stateTo = v;
            setModified(true);
        }


        if (aTStateRelatedByStateTo != null && !ObjectUtils.equals(aTStateRelatedByStateTo.getObjectID(), v))
        {
            aTStateRelatedByStateTo = null;
        }

    }

    /**
     * Get the ProjectType
     *
     * @return Integer
     */
    public Integer getProjectType()
    {
        return projectType;
    }


    /**
     * Set the value of ProjectType
     *
     * @param v new value
     */
    public void setProjectType(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.projectType, v))
        {
            this.projectType = v;
            setModified(true);
        }


        if (aTProjectType != null && !ObjectUtils.equals(aTProjectType.getObjectID(), v))
        {
            aTProjectType = null;
        }

    }

    /**
     * Get the Responsible
     *
     * @return Integer
     */
    public Integer getResponsible()
    {
        return responsible;
    }


    /**
     * Set the value of Responsible
     *
     * @param v new value
     */
    public void setResponsible(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.responsible, v))
        {
            this.responsible = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
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

    



    private TState aTStateRelatedByStateFrom;

    /**
     * Declares an association between this object and a TState object
     *
     * @param v TState
     * @throws TorqueException
     */
    public void setTStateRelatedByStateFrom(TState v) throws TorqueException
    {
        if (v == null)
        {
            setStateFrom((Integer) null);
        }
        else
        {
            setStateFrom(v.getObjectID());
        }
        aTStateRelatedByStateFrom = v;
    }


    /**
     * Returns the associated TState object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TState object
     * @throws TorqueException
     */
    public TState getTStateRelatedByStateFrom()
        throws TorqueException
    {
        if (aTStateRelatedByStateFrom == null && (!ObjectUtils.equals(this.stateFrom, null)))
        {
            aTStateRelatedByStateFrom = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.stateFrom));
        }
        return aTStateRelatedByStateFrom;
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
    public TState getTStateRelatedByStateFrom(Connection connection)
        throws TorqueException
    {
        if (aTStateRelatedByStateFrom == null && (!ObjectUtils.equals(this.stateFrom, null)))
        {
            aTStateRelatedByStateFrom = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.stateFrom), connection);
        }
        return aTStateRelatedByStateFrom;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTStateRelatedByStateFromKey(ObjectKey key) throws TorqueException
    {

        setStateFrom(new Integer(((NumberKey) key).intValue()));
    }




    private TState aTStateRelatedByStateTo;

    /**
     * Declares an association between this object and a TState object
     *
     * @param v TState
     * @throws TorqueException
     */
    public void setTStateRelatedByStateTo(TState v) throws TorqueException
    {
        if (v == null)
        {
            setStateTo((Integer) null);
        }
        else
        {
            setStateTo(v.getObjectID());
        }
        aTStateRelatedByStateTo = v;
    }


    /**
     * Returns the associated TState object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TState object
     * @throws TorqueException
     */
    public TState getTStateRelatedByStateTo()
        throws TorqueException
    {
        if (aTStateRelatedByStateTo == null && (!ObjectUtils.equals(this.stateTo, null)))
        {
            aTStateRelatedByStateTo = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.stateTo));
        }
        return aTStateRelatedByStateTo;
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
    public TState getTStateRelatedByStateTo(Connection connection)
        throws TorqueException
    {
        if (aTStateRelatedByStateTo == null && (!ObjectUtils.equals(this.stateTo, null)))
        {
            aTStateRelatedByStateTo = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.stateTo), connection);
        }
        return aTStateRelatedByStateTo;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTStateRelatedByStateToKey(ObjectKey key) throws TorqueException
    {

        setStateTo(new Integer(((NumberKey) key).intValue()));
    }




    private TProjectType aTProjectType;

    /**
     * Declares an association between this object and a TProjectType object
     *
     * @param v TProjectType
     * @throws TorqueException
     */
    public void setTProjectType(TProjectType v) throws TorqueException
    {
        if (v == null)
        {
            setProjectType((Integer) null);
        }
        else
        {
            setProjectType(v.getObjectID());
        }
        aTProjectType = v;
    }


    /**
     * Returns the associated TProjectType object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProjectType object
     * @throws TorqueException
     */
    public TProjectType getTProjectType()
        throws TorqueException
    {
        if (aTProjectType == null && (!ObjectUtils.equals(this.projectType, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projectType));
        }
        return aTProjectType;
    }

    /**
     * Return the associated TProjectType object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProjectType object
     * @throws TorqueException
     */
    public TProjectType getTProjectType(Connection connection)
        throws TorqueException
    {
        if (aTProjectType == null && (!ObjectUtils.equals(this.projectType, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projectType), connection);
        }
        return aTProjectType;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectTypeKey(ObjectKey key) throws TorqueException
    {

        setProjectType(new Integer(((NumberKey) key).intValue()));
    }




    private TPerson aTPerson;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPerson(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setResponsible((Integer) null);
        }
        else
        {
            setResponsible(v.getObjectID());
        }
        aTPerson = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPerson()
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.responsible, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.responsible));
        }
        return aTPerson;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPerson(Connection connection)
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.responsible, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.responsible), connection);
        }
        return aTPerson;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonKey(ObjectKey key) throws TorqueException
    {

        setResponsible(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTWorkFlowRoles
     */
    protected List<TWorkFlowRole> collTWorkFlowRoles;

    /**
     * Temporary storage of collTWorkFlowRoles to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkFlowRoles()
    {
        if (collTWorkFlowRoles == null)
        {
            collTWorkFlowRoles = new ArrayList<TWorkFlowRole>();
        }
    }


    /**
     * Method called to associate a TWorkFlowRole object to this object
     * through the TWorkFlowRole foreign key attribute
     *
     * @param l TWorkFlowRole
     * @throws TorqueException
     */
    public void addTWorkFlowRole(TWorkFlowRole l) throws TorqueException
    {
        getTWorkFlowRoles().add(l);
        l.setTWorkFlow((TWorkFlow) this);
    }

    /**
     * Method called to associate a TWorkFlowRole object to this object
     * through the TWorkFlowRole foreign key attribute using connection.
     *
     * @param l TWorkFlowRole
     * @throws TorqueException
     */
    public void addTWorkFlowRole(TWorkFlowRole l, Connection con) throws TorqueException
    {
        getTWorkFlowRoles(con).add(l);
        l.setTWorkFlow((TWorkFlow) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkFlowRoles
     */
    private Criteria lastTWorkFlowRolesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlowRoles(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkFlowRole> getTWorkFlowRoles()
        throws TorqueException
    {
        if (collTWorkFlowRoles == null)
        {
            collTWorkFlowRoles = getTWorkFlowRoles(new Criteria(10));
        }
        return collTWorkFlowRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkFlow has previously
     * been saved, it will retrieve related TWorkFlowRoles from storage.
     * If this TWorkFlow is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkFlowRole> getTWorkFlowRoles(Criteria criteria) throws TorqueException
    {
        if (collTWorkFlowRoles == null)
        {
            if (isNew())
            {
               collTWorkFlowRoles = new ArrayList<TWorkFlowRole>();
            }
            else
            {
                criteria.add(TWorkFlowRolePeer.WORKFLOW, getObjectID() );
                collTWorkFlowRoles = TWorkFlowRolePeer.doSelect(criteria);
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
                criteria.add(TWorkFlowRolePeer.WORKFLOW, getObjectID());
                if (!lastTWorkFlowRolesCriteria.equals(criteria))
                {
                    collTWorkFlowRoles = TWorkFlowRolePeer.doSelect(criteria);
                }
            }
        }
        lastTWorkFlowRolesCriteria = criteria;

        return collTWorkFlowRoles;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlowRoles(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlowRole> getTWorkFlowRoles(Connection con) throws TorqueException
    {
        if (collTWorkFlowRoles == null)
        {
            collTWorkFlowRoles = getTWorkFlowRoles(new Criteria(10), con);
        }
        return collTWorkFlowRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkFlow has previously
     * been saved, it will retrieve related TWorkFlowRoles from storage.
     * If this TWorkFlow is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlowRole> getTWorkFlowRoles(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkFlowRoles == null)
        {
            if (isNew())
            {
               collTWorkFlowRoles = new ArrayList<TWorkFlowRole>();
            }
            else
            {
                 criteria.add(TWorkFlowRolePeer.WORKFLOW, getObjectID());
                 collTWorkFlowRoles = TWorkFlowRolePeer.doSelect(criteria, con);
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
                 criteria.add(TWorkFlowRolePeer.WORKFLOW, getObjectID());
                 if (!lastTWorkFlowRolesCriteria.equals(criteria))
                 {
                     collTWorkFlowRoles = TWorkFlowRolePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkFlowRolesCriteria = criteria;

         return collTWorkFlowRoles;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkFlow is new, it will return
     * an empty collection; or if this TWorkFlow has previously
     * been saved, it will retrieve related TWorkFlowRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkFlow.
     */
    protected List<TWorkFlowRole> getTWorkFlowRolesJoinTWorkFlow(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowRoles == null)
        {
            if (isNew())
            {
               collTWorkFlowRoles = new ArrayList<TWorkFlowRole>();
            }
            else
            {
                criteria.add(TWorkFlowRolePeer.WORKFLOW, getObjectID());
                collTWorkFlowRoles = TWorkFlowRolePeer.doSelectJoinTWorkFlow(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowRolePeer.WORKFLOW, getObjectID());
            if (!lastTWorkFlowRolesCriteria.equals(criteria))
            {
                collTWorkFlowRoles = TWorkFlowRolePeer.doSelectJoinTWorkFlow(criteria);
            }
        }
        lastTWorkFlowRolesCriteria = criteria;

        return collTWorkFlowRoles;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkFlow is new, it will return
     * an empty collection; or if this TWorkFlow has previously
     * been saved, it will retrieve related TWorkFlowRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkFlow.
     */
    protected List<TWorkFlowRole> getTWorkFlowRolesJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowRoles == null)
        {
            if (isNew())
            {
               collTWorkFlowRoles = new ArrayList<TWorkFlowRole>();
            }
            else
            {
                criteria.add(TWorkFlowRolePeer.WORKFLOW, getObjectID());
                collTWorkFlowRoles = TWorkFlowRolePeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowRolePeer.WORKFLOW, getObjectID());
            if (!lastTWorkFlowRolesCriteria.equals(criteria))
            {
                collTWorkFlowRoles = TWorkFlowRolePeer.doSelectJoinTRole(criteria);
            }
        }
        lastTWorkFlowRolesCriteria = criteria;

        return collTWorkFlowRoles;
    }





    /**
     * Collection to store aggregation of collTWorkFlowCategorys
     */
    protected List<TWorkFlowCategory> collTWorkFlowCategorys;

    /**
     * Temporary storage of collTWorkFlowCategorys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkFlowCategorys()
    {
        if (collTWorkFlowCategorys == null)
        {
            collTWorkFlowCategorys = new ArrayList<TWorkFlowCategory>();
        }
    }


    /**
     * Method called to associate a TWorkFlowCategory object to this object
     * through the TWorkFlowCategory foreign key attribute
     *
     * @param l TWorkFlowCategory
     * @throws TorqueException
     */
    public void addTWorkFlowCategory(TWorkFlowCategory l) throws TorqueException
    {
        getTWorkFlowCategorys().add(l);
        l.setTWorkFlow((TWorkFlow) this);
    }

    /**
     * Method called to associate a TWorkFlowCategory object to this object
     * through the TWorkFlowCategory foreign key attribute using connection.
     *
     * @param l TWorkFlowCategory
     * @throws TorqueException
     */
    public void addTWorkFlowCategory(TWorkFlowCategory l, Connection con) throws TorqueException
    {
        getTWorkFlowCategorys(con).add(l);
        l.setTWorkFlow((TWorkFlow) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkFlowCategorys
     */
    private Criteria lastTWorkFlowCategorysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlowCategorys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkFlowCategory> getTWorkFlowCategorys()
        throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            collTWorkFlowCategorys = getTWorkFlowCategorys(new Criteria(10));
        }
        return collTWorkFlowCategorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkFlow has previously
     * been saved, it will retrieve related TWorkFlowCategorys from storage.
     * If this TWorkFlow is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkFlowCategory> getTWorkFlowCategorys(Criteria criteria) throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            if (isNew())
            {
               collTWorkFlowCategorys = new ArrayList<TWorkFlowCategory>();
            }
            else
            {
                criteria.add(TWorkFlowCategoryPeer.WORKFLOW, getObjectID() );
                collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelect(criteria);
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
                criteria.add(TWorkFlowCategoryPeer.WORKFLOW, getObjectID());
                if (!lastTWorkFlowCategorysCriteria.equals(criteria))
                {
                    collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkFlowCategorysCriteria = criteria;

        return collTWorkFlowCategorys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkFlowCategorys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlowCategory> getTWorkFlowCategorys(Connection con) throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            collTWorkFlowCategorys = getTWorkFlowCategorys(new Criteria(10), con);
        }
        return collTWorkFlowCategorys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkFlow has previously
     * been saved, it will retrieve related TWorkFlowCategorys from storage.
     * If this TWorkFlow is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkFlowCategory> getTWorkFlowCategorys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            if (isNew())
            {
               collTWorkFlowCategorys = new ArrayList<TWorkFlowCategory>();
            }
            else
            {
                 criteria.add(TWorkFlowCategoryPeer.WORKFLOW, getObjectID());
                 collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkFlowCategoryPeer.WORKFLOW, getObjectID());
                 if (!lastTWorkFlowCategorysCriteria.equals(criteria))
                 {
                     collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkFlowCategorysCriteria = criteria;

         return collTWorkFlowCategorys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkFlow is new, it will return
     * an empty collection; or if this TWorkFlow has previously
     * been saved, it will retrieve related TWorkFlowCategorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkFlow.
     */
    protected List<TWorkFlowCategory> getTWorkFlowCategorysJoinTWorkFlow(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            if (isNew())
            {
               collTWorkFlowCategorys = new ArrayList<TWorkFlowCategory>();
            }
            else
            {
                criteria.add(TWorkFlowCategoryPeer.WORKFLOW, getObjectID());
                collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelectJoinTWorkFlow(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowCategoryPeer.WORKFLOW, getObjectID());
            if (!lastTWorkFlowCategorysCriteria.equals(criteria))
            {
                collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelectJoinTWorkFlow(criteria);
            }
        }
        lastTWorkFlowCategorysCriteria = criteria;

        return collTWorkFlowCategorys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkFlow is new, it will return
     * an empty collection; or if this TWorkFlow has previously
     * been saved, it will retrieve related TWorkFlowCategorys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkFlow.
     */
    protected List<TWorkFlowCategory> getTWorkFlowCategorysJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkFlowCategorys == null)
        {
            if (isNew())
            {
               collTWorkFlowCategorys = new ArrayList<TWorkFlowCategory>();
            }
            else
            {
                criteria.add(TWorkFlowCategoryPeer.WORKFLOW, getObjectID());
                collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowCategoryPeer.WORKFLOW, getObjectID());
            if (!lastTWorkFlowCategorysCriteria.equals(criteria))
            {
                collTWorkFlowCategorys = TWorkFlowCategoryPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWorkFlowCategorysCriteria = criteria;

        return collTWorkFlowCategorys;
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
            fieldNames.add("StateFrom");
            fieldNames.add("StateTo");
            fieldNames.add("ProjectType");
            fieldNames.add("Responsible");
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
        if (name.equals("StateFrom"))
        {
            return getStateFrom();
        }
        if (name.equals("StateTo"))
        {
            return getStateTo();
        }
        if (name.equals("ProjectType"))
        {
            return getProjectType();
        }
        if (name.equals("Responsible"))
        {
            return getResponsible();
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
        if (name.equals("StateFrom"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStateFrom((Integer) value);
            return true;
        }
        if (name.equals("StateTo"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStateTo((Integer) value);
            return true;
        }
        if (name.equals("ProjectType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProjectType((Integer) value);
            return true;
        }
        if (name.equals("Responsible"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setResponsible((Integer) value);
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
        if (name.equals(TWorkFlowPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TWorkFlowPeer.STATEFROM))
        {
            return getStateFrom();
        }
        if (name.equals(TWorkFlowPeer.STATETO))
        {
            return getStateTo();
        }
        if (name.equals(TWorkFlowPeer.PROJECTTYPE))
        {
            return getProjectType();
        }
        if (name.equals(TWorkFlowPeer.RESPONSIBLE))
        {
            return getResponsible();
        }
        if (name.equals(TWorkFlowPeer.TPUUID))
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
      if (TWorkFlowPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TWorkFlowPeer.STATEFROM.equals(name))
        {
            return setByName("StateFrom", value);
        }
      if (TWorkFlowPeer.STATETO.equals(name))
        {
            return setByName("StateTo", value);
        }
      if (TWorkFlowPeer.PROJECTTYPE.equals(name))
        {
            return setByName("ProjectType", value);
        }
      if (TWorkFlowPeer.RESPONSIBLE.equals(name))
        {
            return setByName("Responsible", value);
        }
      if (TWorkFlowPeer.TPUUID.equals(name))
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
            return getStateFrom();
        }
        if (pos == 2)
        {
            return getStateTo();
        }
        if (pos == 3)
        {
            return getProjectType();
        }
        if (pos == 4)
        {
            return getResponsible();
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
            return setByName("StateFrom", value);
        }
    if (position == 2)
        {
            return setByName("StateTo", value);
        }
    if (position == 3)
        {
            return setByName("ProjectType", value);
        }
    if (position == 4)
        {
            return setByName("Responsible", value);
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
        save(TWorkFlowPeer.DATABASE_NAME);
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
                    TWorkFlowPeer.doInsert((TWorkFlow) this, con);
                    setNew(false);
                }
                else
                {
                    TWorkFlowPeer.doUpdate((TWorkFlow) this, con);
                }
            }


            if (collTWorkFlowRoles != null)
            {
                for (int i = 0; i < collTWorkFlowRoles.size(); i++)
                {
                    ((TWorkFlowRole) collTWorkFlowRoles.get(i)).save(con);
                }
            }

            if (collTWorkFlowCategorys != null)
            {
                for (int i = 0; i < collTWorkFlowCategorys.size(); i++)
                {
                    ((TWorkFlowCategory) collTWorkFlowCategorys.get(i)).save(con);
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
    public TWorkFlow copy() throws TorqueException
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
    public TWorkFlow copy(Connection con) throws TorqueException
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
    public TWorkFlow copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TWorkFlow(), deepcopy);
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
    public TWorkFlow copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TWorkFlow(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TWorkFlow copyInto(TWorkFlow copyObj) throws TorqueException
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
    protected TWorkFlow copyInto(TWorkFlow copyObj, Connection con) throws TorqueException
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
    protected TWorkFlow copyInto(TWorkFlow copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setStateFrom(stateFrom);
        copyObj.setStateTo(stateTo);
        copyObj.setProjectType(projectType);
        copyObj.setResponsible(responsible);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkFlowRole> vTWorkFlowRoles = getTWorkFlowRoles();
        if (vTWorkFlowRoles != null)
        {
            for (int i = 0; i < vTWorkFlowRoles.size(); i++)
            {
                TWorkFlowRole obj =  vTWorkFlowRoles.get(i);
                copyObj.addTWorkFlowRole(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkFlowRoles = null;
        }


        List<TWorkFlowCategory> vTWorkFlowCategorys = getTWorkFlowCategorys();
        if (vTWorkFlowCategorys != null)
        {
            for (int i = 0; i < vTWorkFlowCategorys.size(); i++)
            {
                TWorkFlowCategory obj =  vTWorkFlowCategorys.get(i);
                copyObj.addTWorkFlowCategory(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkFlowCategorys = null;
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
    protected TWorkFlow copyInto(TWorkFlow copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setStateFrom(stateFrom);
        copyObj.setStateTo(stateTo);
        copyObj.setProjectType(projectType);
        copyObj.setResponsible(responsible);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkFlowRole> vTWorkFlowRoles = getTWorkFlowRoles(con);
        if (vTWorkFlowRoles != null)
        {
            for (int i = 0; i < vTWorkFlowRoles.size(); i++)
            {
                TWorkFlowRole obj =  vTWorkFlowRoles.get(i);
                copyObj.addTWorkFlowRole(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkFlowRoles = null;
        }


        List<TWorkFlowCategory> vTWorkFlowCategorys = getTWorkFlowCategorys(con);
        if (vTWorkFlowCategorys != null)
        {
            for (int i = 0; i < vTWorkFlowCategorys.size(); i++)
            {
                TWorkFlowCategory obj =  vTWorkFlowCategorys.get(i);
                copyObj.addTWorkFlowCategory(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkFlowCategorys = null;
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
    public TWorkFlowPeer getPeer()
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
        return TWorkFlowPeer.getTableMap();
    }

  
    /**
     * Creates a TWorkFlowBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TWorkFlowBean with the contents of this object
     */
    public TWorkFlowBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TWorkFlowBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TWorkFlowBean with the contents of this object
     */
    public TWorkFlowBean getBean(IdentityMap createdBeans)
    {
        TWorkFlowBean result = (TWorkFlowBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TWorkFlowBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setStateFrom(getStateFrom());
        result.setStateTo(getStateTo());
        result.setProjectType(getProjectType());
        result.setResponsible(getResponsible());
        result.setUuid(getUuid());



        if (collTWorkFlowRoles != null)
        {
            List<TWorkFlowRoleBean> relatedBeans = new ArrayList<TWorkFlowRoleBean>(collTWorkFlowRoles.size());
            for (Iterator<TWorkFlowRole> collTWorkFlowRolesIt = collTWorkFlowRoles.iterator(); collTWorkFlowRolesIt.hasNext(); )
            {
                TWorkFlowRole related = (TWorkFlowRole) collTWorkFlowRolesIt.next();
                TWorkFlowRoleBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkFlowRoleBeans(relatedBeans);
        }


        if (collTWorkFlowCategorys != null)
        {
            List<TWorkFlowCategoryBean> relatedBeans = new ArrayList<TWorkFlowCategoryBean>(collTWorkFlowCategorys.size());
            for (Iterator<TWorkFlowCategory> collTWorkFlowCategorysIt = collTWorkFlowCategorys.iterator(); collTWorkFlowCategorysIt.hasNext(); )
            {
                TWorkFlowCategory related = (TWorkFlowCategory) collTWorkFlowCategorysIt.next();
                TWorkFlowCategoryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkFlowCategoryBeans(relatedBeans);
        }




        if (aTStateRelatedByStateFrom != null)
        {
            TStateBean relatedBean = aTStateRelatedByStateFrom.getBean(createdBeans);
            result.setTStateBeanRelatedByStateFrom(relatedBean);
        }



        if (aTStateRelatedByStateTo != null)
        {
            TStateBean relatedBean = aTStateRelatedByStateTo.getBean(createdBeans);
            result.setTStateBeanRelatedByStateTo(relatedBean);
        }



        if (aTProjectType != null)
        {
            TProjectTypeBean relatedBean = aTProjectType.getBean(createdBeans);
            result.setTProjectTypeBean(relatedBean);
        }



        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TWorkFlow with the contents
     * of a TWorkFlowBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TWorkFlowBean which contents are used to create
     *        the resulting class
     * @return an instance of TWorkFlow with the contents of bean
     */
    public static TWorkFlow createTWorkFlow(TWorkFlowBean bean)
        throws TorqueException
    {
        return createTWorkFlow(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TWorkFlow with the contents
     * of a TWorkFlowBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TWorkFlowBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TWorkFlow with the contents of bean
     */

    public static TWorkFlow createTWorkFlow(TWorkFlowBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TWorkFlow result = (TWorkFlow) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TWorkFlow();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setStateFrom(bean.getStateFrom());
        result.setStateTo(bean.getStateTo());
        result.setProjectType(bean.getProjectType());
        result.setResponsible(bean.getResponsible());
        result.setUuid(bean.getUuid());



        {
            List<TWorkFlowRoleBean> relatedBeans = bean.getTWorkFlowRoleBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkFlowRoleBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkFlowRoleBean relatedBean =  relatedBeansIt.next();
                    TWorkFlowRole related = TWorkFlowRole.createTWorkFlowRole(relatedBean, createdObjects);
                    result.addTWorkFlowRoleFromBean(related);
                }
            }
        }


        {
            List<TWorkFlowCategoryBean> relatedBeans = bean.getTWorkFlowCategoryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkFlowCategoryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkFlowCategoryBean relatedBean =  relatedBeansIt.next();
                    TWorkFlowCategory related = TWorkFlowCategory.createTWorkFlowCategory(relatedBean, createdObjects);
                    result.addTWorkFlowCategoryFromBean(related);
                }
            }
        }




        {
            TStateBean relatedBean = bean.getTStateBeanRelatedByStateFrom();
            if (relatedBean != null)
            {
                TState relatedObject = TState.createTState(relatedBean, createdObjects);
                result.setTStateRelatedByStateFrom(relatedObject);
            }
        }



        {
            TStateBean relatedBean = bean.getTStateBeanRelatedByStateTo();
            if (relatedBean != null)
            {
                TState relatedObject = TState.createTState(relatedBean, createdObjects);
                result.setTStateRelatedByStateTo(relatedObject);
            }
        }



        {
            TProjectTypeBean relatedBean = bean.getTProjectTypeBean();
            if (relatedBean != null)
            {
                TProjectType relatedObject = TProjectType.createTProjectType(relatedBean, createdObjects);
                result.setTProjectType(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBean();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPerson(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TWorkFlowRole object to this object.
     * through the TWorkFlowRole foreign key attribute
     *
     * @param toAdd TWorkFlowRole
     */
    protected void addTWorkFlowRoleFromBean(TWorkFlowRole toAdd)
    {
        initTWorkFlowRoles();
        collTWorkFlowRoles.add(toAdd);
    }


    /**
     * Method called to associate a TWorkFlowCategory object to this object.
     * through the TWorkFlowCategory foreign key attribute
     *
     * @param toAdd TWorkFlowCategory
     */
    protected void addTWorkFlowCategoryFromBean(TWorkFlowCategory toAdd)
    {
        initTWorkFlowCategorys();
        collTWorkFlowCategorys.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TWorkFlow:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("StateFrom = ")
           .append(getStateFrom())
           .append("\n");
        str.append("StateTo = ")
           .append(getStateTo())
           .append("\n");
        str.append("ProjectType = ")
           .append(getProjectType())
           .append("\n");
        str.append("Responsible = ")
           .append(getResponsible())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
