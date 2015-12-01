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

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TProjectCategoryBean;
import com.aurel.track.beans.TProjectBean;

import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TWorkItemBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TProjectCategory
 */
public abstract class BaseTProjectCategory extends TpBaseObject
{
    /** The Peer class */
    private static final TProjectCategoryPeer peer =
        new TProjectCategoryPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the projectID field */
    private Integer projectID;

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



        // update associated TNotify
        if (collTNotifys != null)
        {
            for (int i = 0; i < collTNotifys.size(); i++)
            {
                ((TNotify) collTNotifys.get(i))
                        .setProjectCategoryID(v);
            }
        }

        // update associated TWorkItem
        if (collTWorkItems != null)
        {
            for (int i = 0; i < collTWorkItems.size(); i++)
            {
                ((TWorkItem) collTWorkItems.get(i))
                        .setProjectCategoryID(v);
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
   


    /**
     * Collection to store aggregation of collTNotifys
     */
    protected List<TNotify> collTNotifys;

    /**
     * Temporary storage of collTNotifys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTNotifys()
    {
        if (collTNotifys == null)
        {
            collTNotifys = new ArrayList<TNotify>();
        }
    }


    /**
     * Method called to associate a TNotify object to this object
     * through the TNotify foreign key attribute
     *
     * @param l TNotify
     * @throws TorqueException
     */
    public void addTNotify(TNotify l) throws TorqueException
    {
        getTNotifys().add(l);
        l.setTProjectCategory((TProjectCategory) this);
    }

    /**
     * Method called to associate a TNotify object to this object
     * through the TNotify foreign key attribute using connection.
     *
     * @param l TNotify
     * @throws TorqueException
     */
    public void addTNotify(TNotify l, Connection con) throws TorqueException
    {
        getTNotifys(con).add(l);
        l.setTProjectCategory((TProjectCategory) this);
    }

    /**
     * The criteria used to select the current contents of collTNotifys
     */
    private Criteria lastTNotifysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNotifys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TNotify> getTNotifys()
        throws TorqueException
    {
        if (collTNotifys == null)
        {
            collTNotifys = getTNotifys(new Criteria(10));
        }
        return collTNotifys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory has previously
     * been saved, it will retrieve related TNotifys from storage.
     * If this TProjectCategory is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TNotify> getTNotifys(Criteria criteria) throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                criteria.add(TNotifyPeer.PROJCATKEY, getObjectID() );
                collTNotifys = TNotifyPeer.doSelect(criteria);
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
                criteria.add(TNotifyPeer.PROJCATKEY, getObjectID());
                if (!lastTNotifysCriteria.equals(criteria))
                {
                    collTNotifys = TNotifyPeer.doSelect(criteria);
                }
            }
        }
        lastTNotifysCriteria = criteria;

        return collTNotifys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTNotifys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNotify> getTNotifys(Connection con) throws TorqueException
    {
        if (collTNotifys == null)
        {
            collTNotifys = getTNotifys(new Criteria(10), con);
        }
        return collTNotifys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory has previously
     * been saved, it will retrieve related TNotifys from storage.
     * If this TProjectCategory is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TNotify> getTNotifys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                 criteria.add(TNotifyPeer.PROJCATKEY, getObjectID());
                 collTNotifys = TNotifyPeer.doSelect(criteria, con);
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
                 criteria.add(TNotifyPeer.PROJCATKEY, getObjectID());
                 if (!lastTNotifysCriteria.equals(criteria))
                 {
                     collTNotifys = TNotifyPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTNotifysCriteria = criteria;

         return collTNotifys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TNotify> getTNotifysJoinTProjectCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                criteria.add(TNotifyPeer.PROJCATKEY, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.PROJCATKEY, getObjectID());
            if (!lastTNotifysCriteria.equals(criteria))
            {
                collTNotifys = TNotifyPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        lastTNotifysCriteria = criteria;

        return collTNotifys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TNotify> getTNotifysJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                criteria.add(TNotifyPeer.PROJCATKEY, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.PROJCATKEY, getObjectID());
            if (!lastTNotifysCriteria.equals(criteria))
            {
                collTNotifys = TNotifyPeer.doSelectJoinTState(criteria);
            }
        }
        lastTNotifysCriteria = criteria;

        return collTNotifys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TNotify> getTNotifysJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                criteria.add(TNotifyPeer.PROJCATKEY, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.PROJCATKEY, getObjectID());
            if (!lastTNotifysCriteria.equals(criteria))
            {
                collTNotifys = TNotifyPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTNotifysCriteria = criteria;

        return collTNotifys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TNotify> getTNotifysJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTNotifys == null)
        {
            if (isNew())
            {
               collTNotifys = new ArrayList<TNotify>();
            }
            else
            {
                criteria.add(TNotifyPeer.PROJCATKEY, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.PROJCATKEY, getObjectID());
            if (!lastTNotifysCriteria.equals(criteria))
            {
                collTNotifys = TNotifyPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTNotifysCriteria = criteria;

        return collTNotifys;
    }





    /**
     * Collection to store aggregation of collTWorkItems
     */
    protected List<TWorkItem> collTWorkItems;

    /**
     * Temporary storage of collTWorkItems to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkItems()
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = new ArrayList<TWorkItem>();
        }
    }


    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItem(TWorkItem l) throws TorqueException
    {
        getTWorkItems().add(l);
        l.setTProjectCategory((TProjectCategory) this);
    }

    /**
     * Method called to associate a TWorkItem object to this object
     * through the TWorkItem foreign key attribute using connection.
     *
     * @param l TWorkItem
     * @throws TorqueException
     */
    public void addTWorkItem(TWorkItem l, Connection con) throws TorqueException
    {
        getTWorkItems(con).add(l);
        l.setTProjectCategory((TProjectCategory) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkItems
     */
    private Criteria lastTWorkItemsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItems(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItems()
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = getTWorkItems(new Criteria(10));
        }
        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     * If this TProjectCategory is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkItem> getTWorkItems(Criteria criteria) throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID() );
                collTWorkItems = TWorkItemPeer.doSelect(criteria);
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
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                if (!lastTWorkItemsCriteria.equals(criteria))
                {
                    collTWorkItems = TWorkItemPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItems(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItems(Connection con) throws TorqueException
    {
        if (collTWorkItems == null)
        {
            collTWorkItems = getTWorkItems(new Criteria(10), con);
        }
        return collTWorkItems;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     * If this TProjectCategory is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItem> getTWorkItems(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                 criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                 collTWorkItems = TWorkItemPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                 if (!lastTWorkItemsCriteria.equals(criteria))
                 {
                     collTWorkItems = TWorkItemPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkItemsCriteria = criteria;

         return collTWorkItems;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByOwnerID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByChangedByID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByOriginatorID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPersonRelatedByResponsibleID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTProjectCategory(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTClass(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTPriority(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTSeverity(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTReleaseRelatedByReleaseNoticedID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTReleaseRelatedByReleaseScheduledID(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TProjectCategory is new, it will return
     * an empty collection; or if this TProjectCategory has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TProjectCategory.
     */
    protected List<TWorkItem> getTWorkItemsJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItems == null)
        {
            if (isNew())
            {
               collTWorkItems = new ArrayList<TWorkItem>();
            }
            else
            {
                criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PROJCATKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
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
        if (name.equals(TProjectCategoryPeer.PKEY))
        {
            return getObjectID();
        }
        if (name.equals(TProjectCategoryPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TProjectCategoryPeer.PROJKEY))
        {
            return getProjectID();
        }
        if (name.equals(TProjectCategoryPeer.TPUUID))
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
      if (TProjectCategoryPeer.PKEY.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TProjectCategoryPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TProjectCategoryPeer.PROJKEY.equals(name))
        {
            return setByName("ProjectID", value);
        }
      if (TProjectCategoryPeer.TPUUID.equals(name))
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
        save(TProjectCategoryPeer.DATABASE_NAME);
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
                    TProjectCategoryPeer.doInsert((TProjectCategory) this, con);
                    setNew(false);
                }
                else
                {
                    TProjectCategoryPeer.doUpdate((TProjectCategory) this, con);
                }
            }


            if (collTNotifys != null)
            {
                for (int i = 0; i < collTNotifys.size(); i++)
                {
                    ((TNotify) collTNotifys.get(i)).save(con);
                }
            }

            if (collTWorkItems != null)
            {
                for (int i = 0; i < collTWorkItems.size(); i++)
                {
                    ((TWorkItem) collTWorkItems.get(i)).save(con);
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
    public TProjectCategory copy() throws TorqueException
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
    public TProjectCategory copy(Connection con) throws TorqueException
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
    public TProjectCategory copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TProjectCategory(), deepcopy);
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
    public TProjectCategory copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TProjectCategory(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TProjectCategory copyInto(TProjectCategory copyObj) throws TorqueException
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
    protected TProjectCategory copyInto(TProjectCategory copyObj, Connection con) throws TorqueException
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
    protected TProjectCategory copyInto(TProjectCategory copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setProjectID(projectID);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TNotify> vTNotifys = getTNotifys();
        if (vTNotifys != null)
        {
            for (int i = 0; i < vTNotifys.size(); i++)
            {
                TNotify obj =  vTNotifys.get(i);
                copyObj.addTNotify(obj.copy());
            }
        }
        else
        {
            copyObj.collTNotifys = null;
        }


        List<TWorkItem> vTWorkItems = getTWorkItems();
        if (vTWorkItems != null)
        {
            for (int i = 0; i < vTWorkItems.size(); i++)
            {
                TWorkItem obj =  vTWorkItems.get(i);
                copyObj.addTWorkItem(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkItems = null;
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
    protected TProjectCategory copyInto(TProjectCategory copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setProjectID(projectID);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TNotify> vTNotifys = getTNotifys(con);
        if (vTNotifys != null)
        {
            for (int i = 0; i < vTNotifys.size(); i++)
            {
                TNotify obj =  vTNotifys.get(i);
                copyObj.addTNotify(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTNotifys = null;
        }


        List<TWorkItem> vTWorkItems = getTWorkItems(con);
        if (vTWorkItems != null)
        {
            for (int i = 0; i < vTWorkItems.size(); i++)
            {
                TWorkItem obj =  vTWorkItems.get(i);
                copyObj.addTWorkItem(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkItems = null;
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
    public TProjectCategoryPeer getPeer()
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
        return TProjectCategoryPeer.getTableMap();
    }

  
    /**
     * Creates a TProjectCategoryBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TProjectCategoryBean with the contents of this object
     */
    public TProjectCategoryBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TProjectCategoryBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TProjectCategoryBean with the contents of this object
     */
    public TProjectCategoryBean getBean(IdentityMap createdBeans)
    {
        TProjectCategoryBean result = (TProjectCategoryBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TProjectCategoryBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setProjectID(getProjectID());
        result.setUuid(getUuid());



        if (collTNotifys != null)
        {
            List<TNotifyBean> relatedBeans = new ArrayList<TNotifyBean>(collTNotifys.size());
            for (Iterator<TNotify> collTNotifysIt = collTNotifys.iterator(); collTNotifysIt.hasNext(); )
            {
                TNotify related = (TNotify) collTNotifysIt.next();
                TNotifyBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTNotifyBeans(relatedBeans);
        }


        if (collTWorkItems != null)
        {
            List<TWorkItemBean> relatedBeans = new ArrayList<TWorkItemBean>(collTWorkItems.size());
            for (Iterator<TWorkItem> collTWorkItemsIt = collTWorkItems.iterator(); collTWorkItemsIt.hasNext(); )
            {
                TWorkItem related = (TWorkItem) collTWorkItemsIt.next();
                TWorkItemBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkItemBeans(relatedBeans);
        }




        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TProjectCategory with the contents
     * of a TProjectCategoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TProjectCategoryBean which contents are used to create
     *        the resulting class
     * @return an instance of TProjectCategory with the contents of bean
     */
    public static TProjectCategory createTProjectCategory(TProjectCategoryBean bean)
        throws TorqueException
    {
        return createTProjectCategory(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TProjectCategory with the contents
     * of a TProjectCategoryBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TProjectCategoryBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TProjectCategory with the contents of bean
     */

    public static TProjectCategory createTProjectCategory(TProjectCategoryBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TProjectCategory result = (TProjectCategory) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TProjectCategory();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setProjectID(bean.getProjectID());
        result.setUuid(bean.getUuid());



        {
            List<TNotifyBean> relatedBeans = bean.getTNotifyBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TNotifyBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TNotifyBean relatedBean =  relatedBeansIt.next();
                    TNotify related = TNotify.createTNotify(relatedBean, createdObjects);
                    result.addTNotifyFromBean(related);
                }
            }
        }


        {
            List<TWorkItemBean> relatedBeans = bean.getTWorkItemBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkItemBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkItemBean relatedBean =  relatedBeansIt.next();
                    TWorkItem related = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                    result.addTWorkItemFromBean(related);
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
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TNotify object to this object.
     * through the TNotify foreign key attribute
     *
     * @param toAdd TNotify
     */
    protected void addTNotifyFromBean(TNotify toAdd)
    {
        initTNotifys();
        collTNotifys.add(toAdd);
    }


    /**
     * Method called to associate a TWorkItem object to this object.
     * through the TWorkItem foreign key attribute
     *
     * @param toAdd TWorkItem
     */
    protected void addTWorkItemFromBean(TWorkItem toAdd)
    {
        initTWorkItems();
        collTWorkItems.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TProjectCategory:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("ProjectID = ")
           .append(getProjectID())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
