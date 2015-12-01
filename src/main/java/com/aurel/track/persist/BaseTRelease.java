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



import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TSystemState;
import com.aurel.track.persist.TSystemStatePeer;
import com.aurel.track.persist.TRelease;
import com.aurel.track.persist.TReleasePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TReleaseBean;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TRelease
 */
public abstract class BaseTRelease extends TpBaseObject
{
    /** The Peer class */
    private static final TReleasePeer peer =
        new TReleasePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the projectID field */
    private Integer projectID;

    /** The value for the status field */
    private Integer status;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the moreProps field */
    private String moreProps;

    /** The value for the description field */
    private String description;

    /** The value for the dueDate field */
    private Date dueDate;

    /** The value for the parent field */
    private Integer parent;

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



        // update associated TWorkItem
        if (collTWorkItemsRelatedByReleaseNoticedID != null)
        {
            for (int i = 0; i < collTWorkItemsRelatedByReleaseNoticedID.size(); i++)
            {
                ((TWorkItem) collTWorkItemsRelatedByReleaseNoticedID.get(i))
                        .setReleaseNoticedID(v);
            }
        }

        // update associated TWorkItem
        if (collTWorkItemsRelatedByReleaseScheduledID != null)
        {
            for (int i = 0; i < collTWorkItemsRelatedByReleaseScheduledID.size(); i++)
            {
                ((TWorkItem) collTWorkItemsRelatedByReleaseScheduledID.get(i))
                        .setReleaseScheduledID(v);
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
     * Get the ProjectID
     *
     * @return Integer
     */
    public Integer getProjectID()
    {
        return projectID;
    }


    /**
     * Set the value of ProjectID
     *
     * @param v new value
     */
    public void setProjectID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.projectID, v))
        {
            this.projectID = v;
            setModified(true);
        }


        if (aTProject != null && !ObjectUtils.equals(aTProject.getObjectID(), v))
        {
            aTProject = null;
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
     * Get the DueDate
     *
     * @return Date
     */
    public Date getDueDate()
    {
        return dueDate;
    }


    /**
     * Set the value of DueDate
     *
     * @param v new value
     */
    public void setDueDate(Date v) 
    {

        if (!ObjectUtils.equals(this.dueDate, v))
        {
            this.dueDate = v;
            setModified(true);
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
    public void setParent(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.parent, v))
        {
            this.parent = v;
            setModified(true);
        }


        if (aTReleaseRelatedByParent != null && !ObjectUtils.equals(aTReleaseRelatedByParent.getObjectID(), v))
        {
            aTReleaseRelatedByParent = null;
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

    



    private TProject aTProject;

    /**
     * Declares an association between this object and a TProject object
     *
     * @param v TProject
     * @throws TorqueException
     */
    public void setTProject(TProject v) throws TorqueException
    {
        if (v == null)
        {
            setProjectID((Integer) null);
        }
        else
        {
            setProjectID(v.getObjectID());
        }
        aTProject = v;
    }


    /**
     * Returns the associated TProject object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProject object
     * @throws TorqueException
     */
    public TProject getTProject()
        throws TorqueException
    {
        if (aTProject == null && (!ObjectUtils.equals(this.projectID, null)))
        {
            aTProject = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.projectID));
        }
        return aTProject;
    }

    /**
     * Return the associated TProject object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProject object
     * @throws TorqueException
     */
    public TProject getTProject(Connection connection)
        throws TorqueException
    {
        if (aTProject == null && (!ObjectUtils.equals(this.projectID, null)))
        {
            aTProject = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.projectID), connection);
        }
        return aTProject;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectKey(ObjectKey key) throws TorqueException
    {

        setProjectID(new Integer(((NumberKey) key).intValue()));
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




    private TRelease aTReleaseRelatedByParent;

    /**
     * Declares an association between this object and a TRelease object
     *
     * @param v TRelease
     * @throws TorqueException
     */
    public void setTReleaseRelatedByParent(TRelease v) throws TorqueException
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTReleaseRelatedByParent = v;
    }


    /**
     * Returns the associated TRelease object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TRelease object
     * @throws TorqueException
     */
    public TRelease getTReleaseRelatedByParent()
        throws TorqueException
    {
        if (aTReleaseRelatedByParent == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTReleaseRelatedByParent = TReleasePeer.retrieveByPK(SimpleKey.keyFor(this.parent));
        }
        return aTReleaseRelatedByParent;
    }

    /**
     * Return the associated TRelease object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TRelease object
     * @throws TorqueException
     */
    public TRelease getTReleaseRelatedByParent(Connection connection)
        throws TorqueException
    {
        if (aTReleaseRelatedByParent == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTReleaseRelatedByParent = TReleasePeer.retrieveByPK(SimpleKey.keyFor(this.parent), connection);
        }
        return aTReleaseRelatedByParent;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTReleaseRelatedByParentKey(ObjectKey key) throws TorqueException
    {

        setParent(new Integer(((NumberKey) key).intValue()));
    }
   





    /**
     * Collection to store aggregation of collTWorkItemsRelatedByReleaseNoticedID
     */
    protected List<TWorkItem> collTWorkItemsRelatedByReleaseNoticedID;

    /**
     * Temporary storage of collTWorkItemsRelatedByReleaseNoticedID to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkItemsRelatedByReleaseNoticedID()
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
        }
    }


    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItemRelatedByReleaseNoticedID(TWorkItem l) throws TorqueException
    {
        getTWorkItemsRelatedByReleaseNoticedID().add(l);
        l.setTReleaseRelatedByReleaseNoticedID((TRelease) this);
    }

    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute using connection.
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItemRelatedByReleaseNoticedID(TWorkItem l, Connection con) throws TorqueException
    {
        getTWorkItemsRelatedByReleaseNoticedID(con).add(l);
        l.setTReleaseRelatedByReleaseNoticedID((TRelease) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkItemsRelatedByReleaseNoticedID
     */
    private Criteria lastTWorkItemsRelatedByReleaseNoticedIDCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemsRelatedByReleaseNoticedID(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedID()
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            collTWorkItemsRelatedByReleaseNoticedID = getTWorkItemsRelatedByReleaseNoticedID(new Criteria(10));
        }
        return collTWorkItemsRelatedByReleaseNoticedID;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     * If this TRelease is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedID(Criteria criteria) throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID() );
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelect(criteria);
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
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
                {
                    collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemsRelatedByReleaseNoticedID(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedID(Connection con) throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            collTWorkItemsRelatedByReleaseNoticedID = getTWorkItemsRelatedByReleaseNoticedID(new Criteria(10), con);
        }
        return collTWorkItemsRelatedByReleaseNoticedID;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     * If this TRelease is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedID(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                 criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                 collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                 if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
                 {
                     collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

         return collTWorkItemsRelatedByReleaseNoticedID;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTPersonRelatedByOwnerID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTPersonRelatedByChangedByID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTPersonRelatedByOriginatorID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTPersonRelatedByResponsibleID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTProjectCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTClass(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTPriority(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTSeverity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTReleaseRelatedByReleaseScheduledID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseNoticedID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseNoticedIDJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseNoticedID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseNoticedID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELNOTICEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseNoticedIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseNoticedID = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseNoticedIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseNoticedID;
    }













    /**
     * Collection to store aggregation of collTWorkItemsRelatedByReleaseScheduledID
     */
    protected List<TWorkItem> collTWorkItemsRelatedByReleaseScheduledID;

    /**
     * Temporary storage of collTWorkItemsRelatedByReleaseScheduledID to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkItemsRelatedByReleaseScheduledID()
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
        }
    }


    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItemRelatedByReleaseScheduledID(TWorkItem l) throws TorqueException
    {
        getTWorkItemsRelatedByReleaseScheduledID().add(l);
        l.setTReleaseRelatedByReleaseScheduledID((TRelease) this);
    }

    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute using connection.
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItemRelatedByReleaseScheduledID(TWorkItem l, Connection con) throws TorqueException
    {
        getTWorkItemsRelatedByReleaseScheduledID(con).add(l);
        l.setTReleaseRelatedByReleaseScheduledID((TRelease) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkItemsRelatedByReleaseScheduledID
     */
    private Criteria lastTWorkItemsRelatedByReleaseScheduledIDCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemsRelatedByReleaseScheduledID(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledID()
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            collTWorkItemsRelatedByReleaseScheduledID = getTWorkItemsRelatedByReleaseScheduledID(new Criteria(10));
        }
        return collTWorkItemsRelatedByReleaseScheduledID;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     * If this TRelease is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledID(Criteria criteria) throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID() );
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelect(criteria);
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
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
                {
                    collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemsRelatedByReleaseScheduledID(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledID(Connection con) throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            collTWorkItemsRelatedByReleaseScheduledID = getTWorkItemsRelatedByReleaseScheduledID(new Criteria(10), con);
        }
        return collTWorkItemsRelatedByReleaseScheduledID;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     * If this TRelease is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledID(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                 criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                 collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                 if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
                 {
                     collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

         return collTWorkItemsRelatedByReleaseScheduledID;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTPersonRelatedByOwnerID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTPersonRelatedByChangedByID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTPersonRelatedByOriginatorID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTPersonRelatedByResponsibleID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTProjectCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTClass(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTPriority(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTSeverity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTReleaseRelatedByReleaseNoticedID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRelease is new, it will return
     * an empty collection; or if this TRelease has previously
     * been saved, it will retrieve related TWorkItemsRelatedByReleaseScheduledID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRelease.
     */
    protected List<TWorkItem> getTWorkItemsRelatedByReleaseScheduledIDJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemsRelatedByReleaseScheduledID == null)
        {
            if (isNew())
            {
               collTWorkItemsRelatedByReleaseScheduledID = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.RELSCHEDULEDKEY, getObjectID());
            if (!lastTWorkItemsRelatedByReleaseScheduledIDCriteria.equals(criteria))
            {
                collTWorkItemsRelatedByReleaseScheduledID = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWorkItemsRelatedByReleaseScheduledIDCriteria = criteria;

        return collTWorkItemsRelatedByReleaseScheduledID;
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
            fieldNames.add("ProjectID");
            fieldNames.add("Status");
            fieldNames.add("Sortorder");
            fieldNames.add("MoreProps");
            fieldNames.add("Description");
            fieldNames.add("DueDate");
            fieldNames.add("Parent");
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
        if (name.equals("ProjectID"))
        {
            return getProjectID();
        }
        if (name.equals("Status"))
        {
            return getStatus();
        }
        if (name.equals("Sortorder"))
        {
            return getSortorder();
        }
        if (name.equals("MoreProps"))
        {
            return getMoreProps();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("DueDate"))
        {
            return getDueDate();
        }
        if (name.equals("Parent"))
        {
            return getParent();
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
        if (name.equals("ProjectID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProjectID((Integer) value);
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
        if (name.equals("DueDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDueDate((Date) value);
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
        if (name.equals(TReleasePeer.PKEY))
        {
            return getObjectID();
        }
        if (name.equals(TReleasePeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TReleasePeer.PROJKEY))
        {
            return getProjectID();
        }
        if (name.equals(TReleasePeer.STATUS))
        {
            return getStatus();
        }
        if (name.equals(TReleasePeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TReleasePeer.MOREPROPS))
        {
            return getMoreProps();
        }
        if (name.equals(TReleasePeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TReleasePeer.DUEDATE))
        {
            return getDueDate();
        }
        if (name.equals(TReleasePeer.PARENT))
        {
            return getParent();
        }
        if (name.equals(TReleasePeer.TPUUID))
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
      if (TReleasePeer.PKEY.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TReleasePeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TReleasePeer.PROJKEY.equals(name))
        {
            return setByName("ProjectID", value);
        }
      if (TReleasePeer.STATUS.equals(name))
        {
            return setByName("Status", value);
        }
      if (TReleasePeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TReleasePeer.MOREPROPS.equals(name))
        {
            return setByName("MoreProps", value);
        }
      if (TReleasePeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TReleasePeer.DUEDATE.equals(name))
        {
            return setByName("DueDate", value);
        }
      if (TReleasePeer.PARENT.equals(name))
        {
            return setByName("Parent", value);
        }
      if (TReleasePeer.TPUUID.equals(name))
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
            return getProjectID();
        }
        if (pos == 3)
        {
            return getStatus();
        }
        if (pos == 4)
        {
            return getSortorder();
        }
        if (pos == 5)
        {
            return getMoreProps();
        }
        if (pos == 6)
        {
            return getDescription();
        }
        if (pos == 7)
        {
            return getDueDate();
        }
        if (pos == 8)
        {
            return getParent();
        }
        if (pos == 9)
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
            return setByName("ProjectID", value);
        }
    if (position == 3)
        {
            return setByName("Status", value);
        }
    if (position == 4)
        {
            return setByName("Sortorder", value);
        }
    if (position == 5)
        {
            return setByName("MoreProps", value);
        }
    if (position == 6)
        {
            return setByName("Description", value);
        }
    if (position == 7)
        {
            return setByName("DueDate", value);
        }
    if (position == 8)
        {
            return setByName("Parent", value);
        }
    if (position == 9)
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
        save(TReleasePeer.DATABASE_NAME);
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
                    TReleasePeer.doInsert((TRelease) this, con);
                    setNew(false);
                }
                else
                {
                    TReleasePeer.doUpdate((TRelease) this, con);
                }
            }


            if (collTWorkItemsRelatedByReleaseNoticedID != null)
            {
                for (int i = 0; i < collTWorkItemsRelatedByReleaseNoticedID.size(); i++)
                {
                    ((TWorkItem) collTWorkItemsRelatedByReleaseNoticedID.get(i)).save(con);
                }
            }

            if (collTWorkItemsRelatedByReleaseScheduledID != null)
            {
                for (int i = 0; i < collTWorkItemsRelatedByReleaseScheduledID.size(); i++)
                {
                    ((TWorkItem) collTWorkItemsRelatedByReleaseScheduledID.get(i)).save(con);
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
    public TRelease copy() throws TorqueException
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
    public TRelease copy(Connection con) throws TorqueException
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
    public TRelease copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TRelease(), deepcopy);
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
    public TRelease copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TRelease(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TRelease copyInto(TRelease copyObj) throws TorqueException
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
    protected TRelease copyInto(TRelease copyObj, Connection con) throws TorqueException
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
    protected TRelease copyInto(TRelease copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setProjectID(projectID);
        copyObj.setStatus(status);
        copyObj.setSortorder(sortorder);
        copyObj.setMoreProps(moreProps);
        copyObj.setDescription(description);
        copyObj.setDueDate(dueDate);
        copyObj.setParent(parent);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkItem> vTWorkItemsRelatedByReleaseNoticedID = getTWorkItemsRelatedByReleaseNoticedID();
        if (vTWorkItemsRelatedByReleaseNoticedID != null)
        {
            for (int i = 0; i < vTWorkItemsRelatedByReleaseNoticedID.size(); i++)
            {
                TWorkItem obj =  vTWorkItemsRelatedByReleaseNoticedID.get(i);
                copyObj.addTWorkItemRelatedByReleaseNoticedID(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkItemsRelatedByReleaseNoticedID = null;
        }


        List<TWorkItem> vTWorkItemsRelatedByReleaseScheduledID = getTWorkItemsRelatedByReleaseScheduledID();
        if (vTWorkItemsRelatedByReleaseScheduledID != null)
        {
            for (int i = 0; i < vTWorkItemsRelatedByReleaseScheduledID.size(); i++)
            {
                TWorkItem obj =  vTWorkItemsRelatedByReleaseScheduledID.get(i);
                copyObj.addTWorkItemRelatedByReleaseScheduledID(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkItemsRelatedByReleaseScheduledID = null;
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
    protected TRelease copyInto(TRelease copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setProjectID(projectID);
        copyObj.setStatus(status);
        copyObj.setSortorder(sortorder);
        copyObj.setMoreProps(moreProps);
        copyObj.setDescription(description);
        copyObj.setDueDate(dueDate);
        copyObj.setParent(parent);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWorkItem> vTWorkItemsRelatedByReleaseNoticedID = getTWorkItemsRelatedByReleaseNoticedID(con);
        if (vTWorkItemsRelatedByReleaseNoticedID != null)
        {
            for (int i = 0; i < vTWorkItemsRelatedByReleaseNoticedID.size(); i++)
            {
                TWorkItem obj =  vTWorkItemsRelatedByReleaseNoticedID.get(i);
                copyObj.addTWorkItemRelatedByReleaseNoticedID(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkItemsRelatedByReleaseNoticedID = null;
        }


        List<TWorkItem> vTWorkItemsRelatedByReleaseScheduledID = getTWorkItemsRelatedByReleaseScheduledID(con);
        if (vTWorkItemsRelatedByReleaseScheduledID != null)
        {
            for (int i = 0; i < vTWorkItemsRelatedByReleaseScheduledID.size(); i++)
            {
                TWorkItem obj =  vTWorkItemsRelatedByReleaseScheduledID.get(i);
                copyObj.addTWorkItemRelatedByReleaseScheduledID(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkItemsRelatedByReleaseScheduledID = null;
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
    public TReleasePeer getPeer()
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
        return TReleasePeer.getTableMap();
    }

  
    /**
     * Creates a TReleaseBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TReleaseBean with the contents of this object
     */
    public TReleaseBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TReleaseBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TReleaseBean with the contents of this object
     */
    public TReleaseBean getBean(IdentityMap createdBeans)
    {
        TReleaseBean result = (TReleaseBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TReleaseBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setProjectID(getProjectID());
        result.setStatus(getStatus());
        result.setSortorder(getSortorder());
        result.setMoreProps(getMoreProps());
        result.setDescription(getDescription());
        result.setDueDate(getDueDate());
        result.setParent(getParent());
        result.setUuid(getUuid());



        if (collTWorkItemsRelatedByReleaseNoticedID != null)
        {
            List<TWorkItemBean> relatedBeans = new ArrayList<TWorkItemBean>(collTWorkItemsRelatedByReleaseNoticedID.size());
            for (Iterator<TWorkItem> collTWorkItemsRelatedByReleaseNoticedIDIt = collTWorkItemsRelatedByReleaseNoticedID.iterator(); collTWorkItemsRelatedByReleaseNoticedIDIt.hasNext(); )
            {
                TWorkItem related = (TWorkItem) collTWorkItemsRelatedByReleaseNoticedIDIt.next();
                TWorkItemBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkItemBeansRelatedByReleaseNoticedID(relatedBeans);
        }


        if (collTWorkItemsRelatedByReleaseScheduledID != null)
        {
            List<TWorkItemBean> relatedBeans = new ArrayList<TWorkItemBean>(collTWorkItemsRelatedByReleaseScheduledID.size());
            for (Iterator<TWorkItem> collTWorkItemsRelatedByReleaseScheduledIDIt = collTWorkItemsRelatedByReleaseScheduledID.iterator(); collTWorkItemsRelatedByReleaseScheduledIDIt.hasNext(); )
            {
                TWorkItem related = (TWorkItem) collTWorkItemsRelatedByReleaseScheduledIDIt.next();
                TWorkItemBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkItemBeansRelatedByReleaseScheduledID(relatedBeans);
        }




        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }



        if (aTSystemState != null)
        {
            TSystemStateBean relatedBean = aTSystemState.getBean(createdBeans);
            result.setTSystemStateBean(relatedBean);
        }



        if (aTReleaseRelatedByParent != null)
        {
            TReleaseBean relatedBean = aTReleaseRelatedByParent.getBean(createdBeans);
            result.setTReleaseBeanRelatedByParent(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TRelease with the contents
     * of a TReleaseBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TReleaseBean which contents are used to create
     *        the resulting class
     * @return an instance of TRelease with the contents of bean
     */
    public static TRelease createTRelease(TReleaseBean bean)
        throws TorqueException
    {
        return createTRelease(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TRelease with the contents
     * of a TReleaseBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TReleaseBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TRelease with the contents of bean
     */

    public static TRelease createTRelease(TReleaseBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TRelease result = (TRelease) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TRelease();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setProjectID(bean.getProjectID());
        result.setStatus(bean.getStatus());
        result.setSortorder(bean.getSortorder());
        result.setMoreProps(bean.getMoreProps());
        result.setDescription(bean.getDescription());
        result.setDueDate(bean.getDueDate());
        result.setParent(bean.getParent());
        result.setUuid(bean.getUuid());



        {
            List<TWorkItemBean> relatedBeans = bean.getTWorkItemBeansRelatedByReleaseNoticedID();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkItemBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkItemBean relatedBean =  relatedBeansIt.next();
                    TWorkItem related = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                    result.addTWorkItemRelatedByReleaseNoticedIDFromBean(related);
                }
            }
        }


        {
            List<TWorkItemBean> relatedBeans = bean.getTWorkItemBeansRelatedByReleaseScheduledID();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkItemBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkItemBean relatedBean =  relatedBeansIt.next();
                    TWorkItem related = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                    result.addTWorkItemRelatedByReleaseScheduledIDFromBean(related);
                }
            }
        }




        {
            TProjectBean relatedBean = bean.getTProjectBean();
            if (relatedBean != null)
            {
                TProject relatedObject = TProject.createTProject(relatedBean, createdObjects);
                result.setTProject(relatedObject);
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
            TReleaseBean relatedBean = bean.getTReleaseBeanRelatedByParent();
            if (relatedBean != null)
            {
                TRelease relatedObject = TRelease.createTRelease(relatedBean, createdObjects);
                result.setTReleaseRelatedByParent(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TWorkItem object to this object.
     * through the TWorkItem foreign key attribute
     *
     * @param toAdd TWorkItem
     */
    protected void addTWorkItemRelatedByReleaseNoticedIDFromBean(TWorkItem toAdd)
    {
        initTWorkItemsRelatedByReleaseNoticedID();
        collTWorkItemsRelatedByReleaseNoticedID.add(toAdd);
    }


    /**
     * Method called to associate a TWorkItem object to this object.
     * through the TWorkItem foreign key attribute
     *
     * @param toAdd TWorkItem
     */
    protected void addTWorkItemRelatedByReleaseScheduledIDFromBean(TWorkItem toAdd)
    {
        initTWorkItemsRelatedByReleaseScheduledID();
        collTWorkItemsRelatedByReleaseScheduledID.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TRelease:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("ProjectID = ")
           .append(getProjectID())
           .append("\n");
        str.append("Status = ")
           .append(getStatus())
           .append("\n");
        str.append("Sortorder = ")
           .append(getSortorder())
           .append("\n");
        str.append("MoreProps = ")
           .append(getMoreProps())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("DueDate = ")
           .append(getDueDate())
           .append("\n");
        str.append("Parent = ")
           .append(getParent())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
