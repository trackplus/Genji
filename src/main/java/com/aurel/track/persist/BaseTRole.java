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



import com.aurel.track.persist.TProjectType;
import com.aurel.track.persist.TProjectTypePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TProjectTypeBean;

import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.beans.TWorkFlowRoleBean;
import com.aurel.track.beans.TRoleListTypeBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.beans.TConfigOptionsRoleBean;
import com.aurel.track.beans.TWorkflowGuardBean;
import com.aurel.track.beans.TPRoleBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TRole
 */
public abstract class BaseTRole extends TpBaseObject
{
    /** The Peer class */
    private static final TRolePeer peer =
        new TRolePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the accesskey field */
    private Integer accesskey;

    /** The value for the extendedaccesskey field */
    private String extendedaccesskey;

    /** The value for the projecttype field */
    private Integer projecttype;

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



        // update associated TAccessControlList
        if (collTAccessControlLists != null)
        {
            for (int i = 0; i < collTAccessControlLists.size(); i++)
            {
                ((TAccessControlList) collTAccessControlLists.get(i))
                        .setRoleID(v);
            }
        }

        // update associated TWorkFlowRole
        if (collTWorkFlowRoles != null)
        {
            for (int i = 0; i < collTWorkFlowRoles.size(); i++)
            {
                ((TWorkFlowRole) collTWorkFlowRoles.get(i))
                        .setMayChangeRole(v);
            }
        }

        // update associated TRoleListType
        if (collTRoleListTypes != null)
        {
            for (int i = 0; i < collTRoleListTypes.size(); i++)
            {
                ((TRoleListType) collTRoleListTypes.get(i))
                        .setRole(v);
            }
        }

        // update associated TRoleField
        if (collTRoleFields != null)
        {
            for (int i = 0; i < collTRoleFields.size(); i++)
            {
                ((TRoleField) collTRoleFields.get(i))
                        .setRoleKey(v);
            }
        }

        // update associated TConfigOptionsRole
        if (collTConfigOptionsRoles != null)
        {
            for (int i = 0; i < collTConfigOptionsRoles.size(); i++)
            {
                ((TConfigOptionsRole) collTConfigOptionsRoles.get(i))
                        .setRoleKey(v);
            }
        }

        // update associated TWorkflowGuard
        if (collTWorkflowGuards != null)
        {
            for (int i = 0; i < collTWorkflowGuards.size(); i++)
            {
                ((TWorkflowGuard) collTWorkflowGuards.get(i))
                        .setRole(v);
            }
        }

        // update associated TPRole
        if (collTPRoles != null)
        {
            for (int i = 0; i < collTPRoles.size(); i++)
            {
                ((TPRole) collTPRoles.get(i))
                        .setRole(v);
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
     * Get the Accesskey
     *
     * @return Integer
     */
    public Integer getAccesskey()
    {
        return accesskey;
    }


    /**
     * Set the value of Accesskey
     *
     * @param v new value
     */
    public void setAccesskey(Integer v) 
    {

        if (!ObjectUtils.equals(this.accesskey, v))
        {
            this.accesskey = v;
            setModified(true);
        }


    }

    /**
     * Get the Extendedaccesskey
     *
     * @return String
     */
    public String getExtendedaccesskey()
    {
        return extendedaccesskey;
    }


    /**
     * Set the value of Extendedaccesskey
     *
     * @param v new value
     */
    public void setExtendedaccesskey(String v) 
    {

        if (!ObjectUtils.equals(this.extendedaccesskey, v))
        {
            this.extendedaccesskey = v;
            setModified(true);
        }


    }

    /**
     * Get the Projecttype
     *
     * @return Integer
     */
    public Integer getProjecttype()
    {
        return projecttype;
    }


    /**
     * Set the value of Projecttype
     *
     * @param v new value
     */
    public void setProjecttype(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.projecttype, v))
        {
            this.projecttype = v;
            setModified(true);
        }


        if (aTProjectType != null && !ObjectUtils.equals(aTProjectType.getObjectID(), v))
        {
            aTProjectType = null;
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
            setProjecttype((Integer) null);
        }
        else
        {
            setProjecttype(v.getObjectID());
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
        if (aTProjectType == null && (!ObjectUtils.equals(this.projecttype, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projecttype));
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
        if (aTProjectType == null && (!ObjectUtils.equals(this.projecttype, null)))
        {
            aTProjectType = TProjectTypePeer.retrieveByPK(SimpleKey.keyFor(this.projecttype), connection);
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

        setProjecttype(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTAccessControlLists
     */
    protected List<TAccessControlList> collTAccessControlLists;

    /**
     * Temporary storage of collTAccessControlLists to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAccessControlLists()
    {
        if (collTAccessControlLists == null)
        {
            collTAccessControlLists = new ArrayList<TAccessControlList>();
        }
    }


    /**
     * Method called to associate a TAccessControlList object to this object
     * through the TAccessControlList foreign key attribute
     *
     * @param l TAccessControlList
     * @throws TorqueException
     */
    public void addTAccessControlList(TAccessControlList l) throws TorqueException
    {
        getTAccessControlLists().add(l);
        l.setTRole((TRole) this);
    }

    /**
     * Method called to associate a TAccessControlList object to this object
     * through the TAccessControlList foreign key attribute using connection.
     *
     * @param l TAccessControlList
     * @throws TorqueException
     */
    public void addTAccessControlList(TAccessControlList l, Connection con) throws TorqueException
    {
        getTAccessControlLists(con).add(l);
        l.setTRole((TRole) this);
    }

    /**
     * The criteria used to select the current contents of collTAccessControlLists
     */
    private Criteria lastTAccessControlListsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAccessControlLists(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAccessControlList> getTAccessControlLists()
        throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            collTAccessControlLists = getTAccessControlLists(new Criteria(10));
        }
        return collTAccessControlLists;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TAccessControlLists from storage.
     * If this TRole is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAccessControlList> getTAccessControlLists(Criteria criteria) throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            if (isNew())
            {
               collTAccessControlLists = new ArrayList<TAccessControlList>();
            }
            else
            {
                criteria.add(TAccessControlListPeer.ROLEKEY, getObjectID() );
                collTAccessControlLists = TAccessControlListPeer.doSelect(criteria);
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
                criteria.add(TAccessControlListPeer.ROLEKEY, getObjectID());
                if (!lastTAccessControlListsCriteria.equals(criteria))
                {
                    collTAccessControlLists = TAccessControlListPeer.doSelect(criteria);
                }
            }
        }
        lastTAccessControlListsCriteria = criteria;

        return collTAccessControlLists;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAccessControlLists(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAccessControlList> getTAccessControlLists(Connection con) throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            collTAccessControlLists = getTAccessControlLists(new Criteria(10), con);
        }
        return collTAccessControlLists;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TAccessControlLists from storage.
     * If this TRole is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAccessControlList> getTAccessControlLists(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            if (isNew())
            {
               collTAccessControlLists = new ArrayList<TAccessControlList>();
            }
            else
            {
                 criteria.add(TAccessControlListPeer.ROLEKEY, getObjectID());
                 collTAccessControlLists = TAccessControlListPeer.doSelect(criteria, con);
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
                 criteria.add(TAccessControlListPeer.ROLEKEY, getObjectID());
                 if (!lastTAccessControlListsCriteria.equals(criteria))
                 {
                     collTAccessControlLists = TAccessControlListPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAccessControlListsCriteria = criteria;

         return collTAccessControlLists;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TAccessControlLists from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
     */
    protected List<TAccessControlList> getTAccessControlListsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            if (isNew())
            {
               collTAccessControlLists = new ArrayList<TAccessControlList>();
            }
            else
            {
                criteria.add(TAccessControlListPeer.ROLEKEY, getObjectID());
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAccessControlListPeer.ROLEKEY, getObjectID());
            if (!lastTAccessControlListsCriteria.equals(criteria))
            {
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTAccessControlListsCriteria = criteria;

        return collTAccessControlLists;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TAccessControlLists from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
     */
    protected List<TAccessControlList> getTAccessControlListsJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            if (isNew())
            {
               collTAccessControlLists = new ArrayList<TAccessControlList>();
            }
            else
            {
                criteria.add(TAccessControlListPeer.ROLEKEY, getObjectID());
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAccessControlListPeer.ROLEKEY, getObjectID());
            if (!lastTAccessControlListsCriteria.equals(criteria))
            {
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTRole(criteria);
            }
        }
        lastTAccessControlListsCriteria = criteria;

        return collTAccessControlLists;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TAccessControlLists from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
     */
    protected List<TAccessControlList> getTAccessControlListsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTAccessControlLists == null)
        {
            if (isNew())
            {
               collTAccessControlLists = new ArrayList<TAccessControlList>();
            }
            else
            {
                criteria.add(TAccessControlListPeer.ROLEKEY, getObjectID());
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAccessControlListPeer.ROLEKEY, getObjectID());
            if (!lastTAccessControlListsCriteria.equals(criteria))
            {
                collTAccessControlLists = TAccessControlListPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTAccessControlListsCriteria = criteria;

        return collTAccessControlLists;
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
        l.setTRole((TRole) this);
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
        l.setTRole((TRole) this);
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
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TWorkFlowRoles from storage.
     * If this TRole is new, it will return
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
                criteria.add(TWorkFlowRolePeer.MAYCHANGEROLE, getObjectID() );
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
                criteria.add(TWorkFlowRolePeer.MAYCHANGEROLE, getObjectID());
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
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TWorkFlowRoles from storage.
     * If this TRole is new, it will return
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
                 criteria.add(TWorkFlowRolePeer.MAYCHANGEROLE, getObjectID());
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
                 criteria.add(TWorkFlowRolePeer.MAYCHANGEROLE, getObjectID());
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
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TWorkFlowRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
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
                criteria.add(TWorkFlowRolePeer.MAYCHANGEROLE, getObjectID());
                collTWorkFlowRoles = TWorkFlowRolePeer.doSelectJoinTWorkFlow(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowRolePeer.MAYCHANGEROLE, getObjectID());
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
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TWorkFlowRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
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
                criteria.add(TWorkFlowRolePeer.MAYCHANGEROLE, getObjectID());
                collTWorkFlowRoles = TWorkFlowRolePeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkFlowRolePeer.MAYCHANGEROLE, getObjectID());
            if (!lastTWorkFlowRolesCriteria.equals(criteria))
            {
                collTWorkFlowRoles = TWorkFlowRolePeer.doSelectJoinTRole(criteria);
            }
        }
        lastTWorkFlowRolesCriteria = criteria;

        return collTWorkFlowRoles;
    }





    /**
     * Collection to store aggregation of collTRoleListTypes
     */
    protected List<TRoleListType> collTRoleListTypes;

    /**
     * Temporary storage of collTRoleListTypes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTRoleListTypes()
    {
        if (collTRoleListTypes == null)
        {
            collTRoleListTypes = new ArrayList<TRoleListType>();
        }
    }


    /**
     * Method called to associate a TRoleListType object to this object
     * through the TRoleListType foreign key attribute
     *
     * @param l TRoleListType
     * @throws TorqueException
     */
    public void addTRoleListType(TRoleListType l) throws TorqueException
    {
        getTRoleListTypes().add(l);
        l.setTRole((TRole) this);
    }

    /**
     * Method called to associate a TRoleListType object to this object
     * through the TRoleListType foreign key attribute using connection.
     *
     * @param l TRoleListType
     * @throws TorqueException
     */
    public void addTRoleListType(TRoleListType l, Connection con) throws TorqueException
    {
        getTRoleListTypes(con).add(l);
        l.setTRole((TRole) this);
    }

    /**
     * The criteria used to select the current contents of collTRoleListTypes
     */
    private Criteria lastTRoleListTypesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRoleListTypes(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TRoleListType> getTRoleListTypes()
        throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            collTRoleListTypes = getTRoleListTypes(new Criteria(10));
        }
        return collTRoleListTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TRoleListTypes from storage.
     * If this TRole is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TRoleListType> getTRoleListTypes(Criteria criteria) throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            if (isNew())
            {
               collTRoleListTypes = new ArrayList<TRoleListType>();
            }
            else
            {
                criteria.add(TRoleListTypePeer.PROLE, getObjectID() );
                collTRoleListTypes = TRoleListTypePeer.doSelect(criteria);
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
                criteria.add(TRoleListTypePeer.PROLE, getObjectID());
                if (!lastTRoleListTypesCriteria.equals(criteria))
                {
                    collTRoleListTypes = TRoleListTypePeer.doSelect(criteria);
                }
            }
        }
        lastTRoleListTypesCriteria = criteria;

        return collTRoleListTypes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRoleListTypes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRoleListType> getTRoleListTypes(Connection con) throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            collTRoleListTypes = getTRoleListTypes(new Criteria(10), con);
        }
        return collTRoleListTypes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TRoleListTypes from storage.
     * If this TRole is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRoleListType> getTRoleListTypes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            if (isNew())
            {
               collTRoleListTypes = new ArrayList<TRoleListType>();
            }
            else
            {
                 criteria.add(TRoleListTypePeer.PROLE, getObjectID());
                 collTRoleListTypes = TRoleListTypePeer.doSelect(criteria, con);
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
                 criteria.add(TRoleListTypePeer.PROLE, getObjectID());
                 if (!lastTRoleListTypesCriteria.equals(criteria))
                 {
                     collTRoleListTypes = TRoleListTypePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTRoleListTypesCriteria = criteria;

         return collTRoleListTypes;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TRoleListTypes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
     */
    protected List<TRoleListType> getTRoleListTypesJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            if (isNew())
            {
               collTRoleListTypes = new ArrayList<TRoleListType>();
            }
            else
            {
                criteria.add(TRoleListTypePeer.PROLE, getObjectID());
                collTRoleListTypes = TRoleListTypePeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TRoleListTypePeer.PROLE, getObjectID());
            if (!lastTRoleListTypesCriteria.equals(criteria))
            {
                collTRoleListTypes = TRoleListTypePeer.doSelectJoinTRole(criteria);
            }
        }
        lastTRoleListTypesCriteria = criteria;

        return collTRoleListTypes;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TRoleListTypes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
     */
    protected List<TRoleListType> getTRoleListTypesJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTRoleListTypes == null)
        {
            if (isNew())
            {
               collTRoleListTypes = new ArrayList<TRoleListType>();
            }
            else
            {
                criteria.add(TRoleListTypePeer.PROLE, getObjectID());
                collTRoleListTypes = TRoleListTypePeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TRoleListTypePeer.PROLE, getObjectID());
            if (!lastTRoleListTypesCriteria.equals(criteria))
            {
                collTRoleListTypes = TRoleListTypePeer.doSelectJoinTListType(criteria);
            }
        }
        lastTRoleListTypesCriteria = criteria;

        return collTRoleListTypes;
    }





    /**
     * Collection to store aggregation of collTRoleFields
     */
    protected List<TRoleField> collTRoleFields;

    /**
     * Temporary storage of collTRoleFields to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTRoleFields()
    {
        if (collTRoleFields == null)
        {
            collTRoleFields = new ArrayList<TRoleField>();
        }
    }


    /**
     * Method called to associate a TRoleField object to this object
     * through the TRoleField foreign key attribute
     *
     * @param l TRoleField
     * @throws TorqueException
     */
    public void addTRoleField(TRoleField l) throws TorqueException
    {
        getTRoleFields().add(l);
        l.setTRole((TRole) this);
    }

    /**
     * Method called to associate a TRoleField object to this object
     * through the TRoleField foreign key attribute using connection.
     *
     * @param l TRoleField
     * @throws TorqueException
     */
    public void addTRoleField(TRoleField l, Connection con) throws TorqueException
    {
        getTRoleFields(con).add(l);
        l.setTRole((TRole) this);
    }

    /**
     * The criteria used to select the current contents of collTRoleFields
     */
    private Criteria lastTRoleFieldsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRoleFields(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TRoleField> getTRoleFields()
        throws TorqueException
    {
        if (collTRoleFields == null)
        {
            collTRoleFields = getTRoleFields(new Criteria(10));
        }
        return collTRoleFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TRoleFields from storage.
     * If this TRole is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TRoleField> getTRoleFields(Criteria criteria) throws TorqueException
    {
        if (collTRoleFields == null)
        {
            if (isNew())
            {
               collTRoleFields = new ArrayList<TRoleField>();
            }
            else
            {
                criteria.add(TRoleFieldPeer.ROLEKEY, getObjectID() );
                collTRoleFields = TRoleFieldPeer.doSelect(criteria);
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
                criteria.add(TRoleFieldPeer.ROLEKEY, getObjectID());
                if (!lastTRoleFieldsCriteria.equals(criteria))
                {
                    collTRoleFields = TRoleFieldPeer.doSelect(criteria);
                }
            }
        }
        lastTRoleFieldsCriteria = criteria;

        return collTRoleFields;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTRoleFields(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRoleField> getTRoleFields(Connection con) throws TorqueException
    {
        if (collTRoleFields == null)
        {
            collTRoleFields = getTRoleFields(new Criteria(10), con);
        }
        return collTRoleFields;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TRoleFields from storage.
     * If this TRole is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TRoleField> getTRoleFields(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTRoleFields == null)
        {
            if (isNew())
            {
               collTRoleFields = new ArrayList<TRoleField>();
            }
            else
            {
                 criteria.add(TRoleFieldPeer.ROLEKEY, getObjectID());
                 collTRoleFields = TRoleFieldPeer.doSelect(criteria, con);
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
                 criteria.add(TRoleFieldPeer.ROLEKEY, getObjectID());
                 if (!lastTRoleFieldsCriteria.equals(criteria))
                 {
                     collTRoleFields = TRoleFieldPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTRoleFieldsCriteria = criteria;

         return collTRoleFields;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TRoleFields from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
     */
    protected List<TRoleField> getTRoleFieldsJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTRoleFields == null)
        {
            if (isNew())
            {
               collTRoleFields = new ArrayList<TRoleField>();
            }
            else
            {
                criteria.add(TRoleFieldPeer.ROLEKEY, getObjectID());
                collTRoleFields = TRoleFieldPeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TRoleFieldPeer.ROLEKEY, getObjectID());
            if (!lastTRoleFieldsCriteria.equals(criteria))
            {
                collTRoleFields = TRoleFieldPeer.doSelectJoinTRole(criteria);
            }
        }
        lastTRoleFieldsCriteria = criteria;

        return collTRoleFields;
    }





    /**
     * Collection to store aggregation of collTConfigOptionsRoles
     */
    protected List<TConfigOptionsRole> collTConfigOptionsRoles;

    /**
     * Temporary storage of collTConfigOptionsRoles to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTConfigOptionsRoles()
    {
        if (collTConfigOptionsRoles == null)
        {
            collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
        }
    }


    /**
     * Method called to associate a TConfigOptionsRole object to this object
     * through the TConfigOptionsRole foreign key attribute
     *
     * @param l TConfigOptionsRole
     * @throws TorqueException
     */
    public void addTConfigOptionsRole(TConfigOptionsRole l) throws TorqueException
    {
        getTConfigOptionsRoles().add(l);
        l.setTRole((TRole) this);
    }

    /**
     * Method called to associate a TConfigOptionsRole object to this object
     * through the TConfigOptionsRole foreign key attribute using connection.
     *
     * @param l TConfigOptionsRole
     * @throws TorqueException
     */
    public void addTConfigOptionsRole(TConfigOptionsRole l, Connection con) throws TorqueException
    {
        getTConfigOptionsRoles(con).add(l);
        l.setTRole((TRole) this);
    }

    /**
     * The criteria used to select the current contents of collTConfigOptionsRoles
     */
    private Criteria lastTConfigOptionsRolesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTConfigOptionsRoles(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TConfigOptionsRole> getTConfigOptionsRoles()
        throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            collTConfigOptionsRoles = getTConfigOptionsRoles(new Criteria(10));
        }
        return collTConfigOptionsRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     * If this TRole is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TConfigOptionsRole> getTConfigOptionsRoles(Criteria criteria) throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            if (isNew())
            {
               collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
            }
            else
            {
                criteria.add(TConfigOptionsRolePeer.ROLEKEY, getObjectID() );
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelect(criteria);
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
                criteria.add(TConfigOptionsRolePeer.ROLEKEY, getObjectID());
                if (!lastTConfigOptionsRolesCriteria.equals(criteria))
                {
                    collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelect(criteria);
                }
            }
        }
        lastTConfigOptionsRolesCriteria = criteria;

        return collTConfigOptionsRoles;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTConfigOptionsRoles(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TConfigOptionsRole> getTConfigOptionsRoles(Connection con) throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            collTConfigOptionsRoles = getTConfigOptionsRoles(new Criteria(10), con);
        }
        return collTConfigOptionsRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     * If this TRole is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TConfigOptionsRole> getTConfigOptionsRoles(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            if (isNew())
            {
               collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
            }
            else
            {
                 criteria.add(TConfigOptionsRolePeer.ROLEKEY, getObjectID());
                 collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelect(criteria, con);
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
                 criteria.add(TConfigOptionsRolePeer.ROLEKEY, getObjectID());
                 if (!lastTConfigOptionsRolesCriteria.equals(criteria))
                 {
                     collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTConfigOptionsRolesCriteria = criteria;

         return collTConfigOptionsRoles;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
     */
    protected List<TConfigOptionsRole> getTConfigOptionsRolesJoinTFieldConfig(Criteria criteria)
        throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            if (isNew())
            {
               collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
            }
            else
            {
                criteria.add(TConfigOptionsRolePeer.ROLEKEY, getObjectID());
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TConfigOptionsRolePeer.ROLEKEY, getObjectID());
            if (!lastTConfigOptionsRolesCriteria.equals(criteria))
            {
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        lastTConfigOptionsRolesCriteria = criteria;

        return collTConfigOptionsRoles;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
     */
    protected List<TConfigOptionsRole> getTConfigOptionsRolesJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            if (isNew())
            {
               collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
            }
            else
            {
                criteria.add(TConfigOptionsRolePeer.ROLEKEY, getObjectID());
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TConfigOptionsRolePeer.ROLEKEY, getObjectID());
            if (!lastTConfigOptionsRolesCriteria.equals(criteria))
            {
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTRole(criteria);
            }
        }
        lastTConfigOptionsRolesCriteria = criteria;

        return collTConfigOptionsRoles;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
     */
    protected List<TConfigOptionsRole> getTConfigOptionsRolesJoinTOption(Criteria criteria)
        throws TorqueException
    {
        if (collTConfigOptionsRoles == null)
        {
            if (isNew())
            {
               collTConfigOptionsRoles = new ArrayList<TConfigOptionsRole>();
            }
            else
            {
                criteria.add(TConfigOptionsRolePeer.ROLEKEY, getObjectID());
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTOption(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TConfigOptionsRolePeer.ROLEKEY, getObjectID());
            if (!lastTConfigOptionsRolesCriteria.equals(criteria))
            {
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTOption(criteria);
            }
        }
        lastTConfigOptionsRolesCriteria = criteria;

        return collTConfigOptionsRoles;
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
        l.setTRole((TRole) this);
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
        l.setTRole((TRole) this);
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
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     * If this TRole is new, it will return
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
                criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID() );
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
                criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID());
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
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     * If this TRole is new, it will return
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
                 criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID());
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
                 criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID());
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
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
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
                criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID());
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
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
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
                criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID());
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
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
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
                criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTScripts(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID());
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
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TWorkflowGuards from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
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
                criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID());
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkflowGuardPeer.ROLEKEY, getObjectID());
            if (!lastTWorkflowGuardsCriteria.equals(criteria))
            {
                collTWorkflowGuards = TWorkflowGuardPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTWorkflowGuardsCriteria = criteria;

        return collTWorkflowGuards;
    }





    /**
     * Collection to store aggregation of collTPRoles
     */
    protected List<TPRole> collTPRoles;

    /**
     * Temporary storage of collTPRoles to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPRoles()
    {
        if (collTPRoles == null)
        {
            collTPRoles = new ArrayList<TPRole>();
        }
    }


    /**
     * Method called to associate a TPRole object to this object
     * through the TPRole foreign key attribute
     *
     * @param l TPRole
     * @throws TorqueException
     */
    public void addTPRole(TPRole l) throws TorqueException
    {
        getTPRoles().add(l);
        l.setTRole((TRole) this);
    }

    /**
     * Method called to associate a TPRole object to this object
     * through the TPRole foreign key attribute using connection.
     *
     * @param l TPRole
     * @throws TorqueException
     */
    public void addTPRole(TPRole l, Connection con) throws TorqueException
    {
        getTPRoles(con).add(l);
        l.setTRole((TRole) this);
    }

    /**
     * The criteria used to select the current contents of collTPRoles
     */
    private Criteria lastTPRolesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPRoles(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPRole> getTPRoles()
        throws TorqueException
    {
        if (collTPRoles == null)
        {
            collTPRoles = getTPRoles(new Criteria(10));
        }
        return collTPRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TPRoles from storage.
     * If this TRole is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPRole> getTPRoles(Criteria criteria) throws TorqueException
    {
        if (collTPRoles == null)
        {
            if (isNew())
            {
               collTPRoles = new ArrayList<TPRole>();
            }
            else
            {
                criteria.add(TPRolePeer.ROLEKEY, getObjectID() );
                collTPRoles = TPRolePeer.doSelect(criteria);
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
                criteria.add(TPRolePeer.ROLEKEY, getObjectID());
                if (!lastTPRolesCriteria.equals(criteria))
                {
                    collTPRoles = TPRolePeer.doSelect(criteria);
                }
            }
        }
        lastTPRolesCriteria = criteria;

        return collTPRoles;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPRoles(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPRole> getTPRoles(Connection con) throws TorqueException
    {
        if (collTPRoles == null)
        {
            collTPRoles = getTPRoles(new Criteria(10), con);
        }
        return collTPRoles;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole has previously
     * been saved, it will retrieve related TPRoles from storage.
     * If this TRole is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPRole> getTPRoles(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPRoles == null)
        {
            if (isNew())
            {
               collTPRoles = new ArrayList<TPRole>();
            }
            else
            {
                 criteria.add(TPRolePeer.ROLEKEY, getObjectID());
                 collTPRoles = TPRolePeer.doSelect(criteria, con);
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
                 criteria.add(TPRolePeer.ROLEKEY, getObjectID());
                 if (!lastTPRolesCriteria.equals(criteria))
                 {
                     collTPRoles = TPRolePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPRolesCriteria = criteria;

         return collTPRoles;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TPRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
     */
    protected List<TPRole> getTPRolesJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTPRoles == null)
        {
            if (isNew())
            {
               collTPRoles = new ArrayList<TPRole>();
            }
            else
            {
                criteria.add(TPRolePeer.ROLEKEY, getObjectID());
                collTPRoles = TPRolePeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPRolePeer.ROLEKEY, getObjectID());
            if (!lastTPRolesCriteria.equals(criteria))
            {
                collTPRoles = TPRolePeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTPRolesCriteria = criteria;

        return collTPRoles;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRole is new, it will return
     * an empty collection; or if this TRole has previously
     * been saved, it will retrieve related TPRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRole.
     */
    protected List<TPRole> getTPRolesJoinTRole(Criteria criteria)
        throws TorqueException
    {
        if (collTPRoles == null)
        {
            if (isNew())
            {
               collTPRoles = new ArrayList<TPRole>();
            }
            else
            {
                criteria.add(TPRolePeer.ROLEKEY, getObjectID());
                collTPRoles = TPRolePeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPRolePeer.ROLEKEY, getObjectID());
            if (!lastTPRolesCriteria.equals(criteria))
            {
                collTPRoles = TPRolePeer.doSelectJoinTRole(criteria);
            }
        }
        lastTPRolesCriteria = criteria;

        return collTPRoles;
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
            fieldNames.add("Accesskey");
            fieldNames.add("Extendedaccesskey");
            fieldNames.add("Projecttype");
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
        if (name.equals("Accesskey"))
        {
            return getAccesskey();
        }
        if (name.equals("Extendedaccesskey"))
        {
            return getExtendedaccesskey();
        }
        if (name.equals("Projecttype"))
        {
            return getProjecttype();
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
        if (name.equals("Accesskey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAccesskey((Integer) value);
            return true;
        }
        if (name.equals("Extendedaccesskey"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setExtendedaccesskey((String) value);
            return true;
        }
        if (name.equals("Projecttype"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProjecttype((Integer) value);
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
        if (name.equals(TRolePeer.PKEY))
        {
            return getObjectID();
        }
        if (name.equals(TRolePeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TRolePeer.ACCESSKEY))
        {
            return getAccesskey();
        }
        if (name.equals(TRolePeer.EXTENDEDACCESSKEY))
        {
            return getExtendedaccesskey();
        }
        if (name.equals(TRolePeer.PROJECTTYPE))
        {
            return getProjecttype();
        }
        if (name.equals(TRolePeer.TPUUID))
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
      if (TRolePeer.PKEY.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TRolePeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TRolePeer.ACCESSKEY.equals(name))
        {
            return setByName("Accesskey", value);
        }
      if (TRolePeer.EXTENDEDACCESSKEY.equals(name))
        {
            return setByName("Extendedaccesskey", value);
        }
      if (TRolePeer.PROJECTTYPE.equals(name))
        {
            return setByName("Projecttype", value);
        }
      if (TRolePeer.TPUUID.equals(name))
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
            return getAccesskey();
        }
        if (pos == 3)
        {
            return getExtendedaccesskey();
        }
        if (pos == 4)
        {
            return getProjecttype();
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
            return setByName("Accesskey", value);
        }
    if (position == 3)
        {
            return setByName("Extendedaccesskey", value);
        }
    if (position == 4)
        {
            return setByName("Projecttype", value);
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
        save(TRolePeer.DATABASE_NAME);
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
                    TRolePeer.doInsert((TRole) this, con);
                    setNew(false);
                }
                else
                {
                    TRolePeer.doUpdate((TRole) this, con);
                }
            }


            if (collTAccessControlLists != null)
            {
                for (int i = 0; i < collTAccessControlLists.size(); i++)
                {
                    ((TAccessControlList) collTAccessControlLists.get(i)).save(con);
                }
            }

            if (collTWorkFlowRoles != null)
            {
                for (int i = 0; i < collTWorkFlowRoles.size(); i++)
                {
                    ((TWorkFlowRole) collTWorkFlowRoles.get(i)).save(con);
                }
            }

            if (collTRoleListTypes != null)
            {
                for (int i = 0; i < collTRoleListTypes.size(); i++)
                {
                    ((TRoleListType) collTRoleListTypes.get(i)).save(con);
                }
            }

            if (collTRoleFields != null)
            {
                for (int i = 0; i < collTRoleFields.size(); i++)
                {
                    ((TRoleField) collTRoleFields.get(i)).save(con);
                }
            }

            if (collTConfigOptionsRoles != null)
            {
                for (int i = 0; i < collTConfigOptionsRoles.size(); i++)
                {
                    ((TConfigOptionsRole) collTConfigOptionsRoles.get(i)).save(con);
                }
            }

            if (collTWorkflowGuards != null)
            {
                for (int i = 0; i < collTWorkflowGuards.size(); i++)
                {
                    ((TWorkflowGuard) collTWorkflowGuards.get(i)).save(con);
                }
            }

            if (collTPRoles != null)
            {
                for (int i = 0; i < collTPRoles.size(); i++)
                {
                    ((TPRole) collTPRoles.get(i)).save(con);
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
    public TRole copy() throws TorqueException
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
    public TRole copy(Connection con) throws TorqueException
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
    public TRole copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TRole(), deepcopy);
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
    public TRole copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TRole(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TRole copyInto(TRole copyObj) throws TorqueException
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
    protected TRole copyInto(TRole copyObj, Connection con) throws TorqueException
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
    protected TRole copyInto(TRole copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setAccesskey(accesskey);
        copyObj.setExtendedaccesskey(extendedaccesskey);
        copyObj.setProjecttype(projecttype);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TAccessControlList> vTAccessControlLists = getTAccessControlLists();
        if (vTAccessControlLists != null)
        {
            for (int i = 0; i < vTAccessControlLists.size(); i++)
            {
                TAccessControlList obj =  vTAccessControlLists.get(i);
                copyObj.addTAccessControlList(obj.copy());
            }
        }
        else
        {
            copyObj.collTAccessControlLists = null;
        }


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


        List<TRoleListType> vTRoleListTypes = getTRoleListTypes();
        if (vTRoleListTypes != null)
        {
            for (int i = 0; i < vTRoleListTypes.size(); i++)
            {
                TRoleListType obj =  vTRoleListTypes.get(i);
                copyObj.addTRoleListType(obj.copy());
            }
        }
        else
        {
            copyObj.collTRoleListTypes = null;
        }


        List<TRoleField> vTRoleFields = getTRoleFields();
        if (vTRoleFields != null)
        {
            for (int i = 0; i < vTRoleFields.size(); i++)
            {
                TRoleField obj =  vTRoleFields.get(i);
                copyObj.addTRoleField(obj.copy());
            }
        }
        else
        {
            copyObj.collTRoleFields = null;
        }


        List<TConfigOptionsRole> vTConfigOptionsRoles = getTConfigOptionsRoles();
        if (vTConfigOptionsRoles != null)
        {
            for (int i = 0; i < vTConfigOptionsRoles.size(); i++)
            {
                TConfigOptionsRole obj =  vTConfigOptionsRoles.get(i);
                copyObj.addTConfigOptionsRole(obj.copy());
            }
        }
        else
        {
            copyObj.collTConfigOptionsRoles = null;
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


        List<TPRole> vTPRoles = getTPRoles();
        if (vTPRoles != null)
        {
            for (int i = 0; i < vTPRoles.size(); i++)
            {
                TPRole obj =  vTPRoles.get(i);
                copyObj.addTPRole(obj.copy());
            }
        }
        else
        {
            copyObj.collTPRoles = null;
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
    protected TRole copyInto(TRole copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setAccesskey(accesskey);
        copyObj.setExtendedaccesskey(extendedaccesskey);
        copyObj.setProjecttype(projecttype);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TAccessControlList> vTAccessControlLists = getTAccessControlLists(con);
        if (vTAccessControlLists != null)
        {
            for (int i = 0; i < vTAccessControlLists.size(); i++)
            {
                TAccessControlList obj =  vTAccessControlLists.get(i);
                copyObj.addTAccessControlList(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAccessControlLists = null;
        }


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


        List<TRoleListType> vTRoleListTypes = getTRoleListTypes(con);
        if (vTRoleListTypes != null)
        {
            for (int i = 0; i < vTRoleListTypes.size(); i++)
            {
                TRoleListType obj =  vTRoleListTypes.get(i);
                copyObj.addTRoleListType(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTRoleListTypes = null;
        }


        List<TRoleField> vTRoleFields = getTRoleFields(con);
        if (vTRoleFields != null)
        {
            for (int i = 0; i < vTRoleFields.size(); i++)
            {
                TRoleField obj =  vTRoleFields.get(i);
                copyObj.addTRoleField(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTRoleFields = null;
        }


        List<TConfigOptionsRole> vTConfigOptionsRoles = getTConfigOptionsRoles(con);
        if (vTConfigOptionsRoles != null)
        {
            for (int i = 0; i < vTConfigOptionsRoles.size(); i++)
            {
                TConfigOptionsRole obj =  vTConfigOptionsRoles.get(i);
                copyObj.addTConfigOptionsRole(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTConfigOptionsRoles = null;
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


        List<TPRole> vTPRoles = getTPRoles(con);
        if (vTPRoles != null)
        {
            for (int i = 0; i < vTPRoles.size(); i++)
            {
                TPRole obj =  vTPRoles.get(i);
                copyObj.addTPRole(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPRoles = null;
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
    public TRolePeer getPeer()
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
        return TRolePeer.getTableMap();
    }

  
    /**
     * Creates a TRoleBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TRoleBean with the contents of this object
     */
    public TRoleBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TRoleBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TRoleBean with the contents of this object
     */
    public TRoleBean getBean(IdentityMap createdBeans)
    {
        TRoleBean result = (TRoleBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TRoleBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setAccesskey(getAccesskey());
        result.setExtendedaccesskey(getExtendedaccesskey());
        result.setProjecttype(getProjecttype());
        result.setUuid(getUuid());



        if (collTAccessControlLists != null)
        {
            List<TAccessControlListBean> relatedBeans = new ArrayList<TAccessControlListBean>(collTAccessControlLists.size());
            for (Iterator<TAccessControlList> collTAccessControlListsIt = collTAccessControlLists.iterator(); collTAccessControlListsIt.hasNext(); )
            {
                TAccessControlList related = (TAccessControlList) collTAccessControlListsIt.next();
                TAccessControlListBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAccessControlListBeans(relatedBeans);
        }


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


        if (collTRoleListTypes != null)
        {
            List<TRoleListTypeBean> relatedBeans = new ArrayList<TRoleListTypeBean>(collTRoleListTypes.size());
            for (Iterator<TRoleListType> collTRoleListTypesIt = collTRoleListTypes.iterator(); collTRoleListTypesIt.hasNext(); )
            {
                TRoleListType related = (TRoleListType) collTRoleListTypesIt.next();
                TRoleListTypeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTRoleListTypeBeans(relatedBeans);
        }


        if (collTRoleFields != null)
        {
            List<TRoleFieldBean> relatedBeans = new ArrayList<TRoleFieldBean>(collTRoleFields.size());
            for (Iterator<TRoleField> collTRoleFieldsIt = collTRoleFields.iterator(); collTRoleFieldsIt.hasNext(); )
            {
                TRoleField related = (TRoleField) collTRoleFieldsIt.next();
                TRoleFieldBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTRoleFieldBeans(relatedBeans);
        }


        if (collTConfigOptionsRoles != null)
        {
            List<TConfigOptionsRoleBean> relatedBeans = new ArrayList<TConfigOptionsRoleBean>(collTConfigOptionsRoles.size());
            for (Iterator<TConfigOptionsRole> collTConfigOptionsRolesIt = collTConfigOptionsRoles.iterator(); collTConfigOptionsRolesIt.hasNext(); )
            {
                TConfigOptionsRole related = (TConfigOptionsRole) collTConfigOptionsRolesIt.next();
                TConfigOptionsRoleBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTConfigOptionsRoleBeans(relatedBeans);
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


        if (collTPRoles != null)
        {
            List<TPRoleBean> relatedBeans = new ArrayList<TPRoleBean>(collTPRoles.size());
            for (Iterator<TPRole> collTPRolesIt = collTPRoles.iterator(); collTPRolesIt.hasNext(); )
            {
                TPRole related = (TPRole) collTPRolesIt.next();
                TPRoleBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPRoleBeans(relatedBeans);
        }




        if (aTProjectType != null)
        {
            TProjectTypeBean relatedBean = aTProjectType.getBean(createdBeans);
            result.setTProjectTypeBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TRole with the contents
     * of a TRoleBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TRoleBean which contents are used to create
     *        the resulting class
     * @return an instance of TRole with the contents of bean
     */
    public static TRole createTRole(TRoleBean bean)
        throws TorqueException
    {
        return createTRole(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TRole with the contents
     * of a TRoleBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TRoleBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TRole with the contents of bean
     */

    public static TRole createTRole(TRoleBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TRole result = (TRole) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TRole();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setAccesskey(bean.getAccesskey());
        result.setExtendedaccesskey(bean.getExtendedaccesskey());
        result.setProjecttype(bean.getProjecttype());
        result.setUuid(bean.getUuid());



        {
            List<TAccessControlListBean> relatedBeans = bean.getTAccessControlListBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAccessControlListBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAccessControlListBean relatedBean =  relatedBeansIt.next();
                    TAccessControlList related = TAccessControlList.createTAccessControlList(relatedBean, createdObjects);
                    result.addTAccessControlListFromBean(related);
                }
            }
        }


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
            List<TRoleListTypeBean> relatedBeans = bean.getTRoleListTypeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TRoleListTypeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TRoleListTypeBean relatedBean =  relatedBeansIt.next();
                    TRoleListType related = TRoleListType.createTRoleListType(relatedBean, createdObjects);
                    result.addTRoleListTypeFromBean(related);
                }
            }
        }


        {
            List<TRoleFieldBean> relatedBeans = bean.getTRoleFieldBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TRoleFieldBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TRoleFieldBean relatedBean =  relatedBeansIt.next();
                    TRoleField related = TRoleField.createTRoleField(relatedBean, createdObjects);
                    result.addTRoleFieldFromBean(related);
                }
            }
        }


        {
            List<TConfigOptionsRoleBean> relatedBeans = bean.getTConfigOptionsRoleBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TConfigOptionsRoleBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TConfigOptionsRoleBean relatedBean =  relatedBeansIt.next();
                    TConfigOptionsRole related = TConfigOptionsRole.createTConfigOptionsRole(relatedBean, createdObjects);
                    result.addTConfigOptionsRoleFromBean(related);
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
            List<TPRoleBean> relatedBeans = bean.getTPRoleBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPRoleBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPRoleBean relatedBean =  relatedBeansIt.next();
                    TPRole related = TPRole.createTPRole(relatedBean, createdObjects);
                    result.addTPRoleFromBean(related);
                }
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
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TAccessControlList object to this object.
     * through the TAccessControlList foreign key attribute
     *
     * @param toAdd TAccessControlList
     */
    protected void addTAccessControlListFromBean(TAccessControlList toAdd)
    {
        initTAccessControlLists();
        collTAccessControlLists.add(toAdd);
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
     * Method called to associate a TRoleListType object to this object.
     * through the TRoleListType foreign key attribute
     *
     * @param toAdd TRoleListType
     */
    protected void addTRoleListTypeFromBean(TRoleListType toAdd)
    {
        initTRoleListTypes();
        collTRoleListTypes.add(toAdd);
    }


    /**
     * Method called to associate a TRoleField object to this object.
     * through the TRoleField foreign key attribute
     *
     * @param toAdd TRoleField
     */
    protected void addTRoleFieldFromBean(TRoleField toAdd)
    {
        initTRoleFields();
        collTRoleFields.add(toAdd);
    }


    /**
     * Method called to associate a TConfigOptionsRole object to this object.
     * through the TConfigOptionsRole foreign key attribute
     *
     * @param toAdd TConfigOptionsRole
     */
    protected void addTConfigOptionsRoleFromBean(TConfigOptionsRole toAdd)
    {
        initTConfigOptionsRoles();
        collTConfigOptionsRoles.add(toAdd);
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
     * Method called to associate a TPRole object to this object.
     * through the TPRole foreign key attribute
     *
     * @param toAdd TPRole
     */
    protected void addTPRoleFromBean(TPRole toAdd)
    {
        initTPRoles();
        collTPRoles.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TRole:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Accesskey = ")
           .append(getAccesskey())
           .append("\n");
        str.append("Extendedaccesskey = ")
           .append(getExtendedaccesskey())
           .append("\n");
        str.append("Projecttype = ")
           .append(getProjecttype())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
