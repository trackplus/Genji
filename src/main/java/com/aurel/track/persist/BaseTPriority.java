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



import com.aurel.track.persist.TBLOB;
import com.aurel.track.persist.TBLOBPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TBLOBBean;

import com.aurel.track.beans.TPpriorityBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TEscalationEntryBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TPriority
 */
public abstract class BaseTPriority extends TpBaseObject
{
    /** The Peer class */
    private static final TPriorityPeer peer =
        new TPriorityPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the label field */
    private String label;

    /** The value for the tooltip field */
    private String tooltip;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the wlevel field */
    private Integer wlevel;

    /** The value for the symbol field */
    private String symbol;

    /** The value for the iconKey field */
    private Integer iconKey;

    /** The value for the iconChanged field */
    private String iconChanged = "N";

    /** The value for the cSSStyle field */
    private String cSSStyle;

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



        // update associated TPpriority
        if (collTPprioritys != null)
        {
            for (int i = 0; i < collTPprioritys.size(); i++)
            {
                ((TPpriority) collTPprioritys.get(i))
                        .setPriority(v);
            }
        }

        // update associated TWorkItem
        if (collTWorkItems != null)
        {
            for (int i = 0; i < collTWorkItems.size(); i++)
            {
                ((TWorkItem) collTWorkItems.get(i))
                        .setPriorityID(v);
            }
        }

        // update associated TEscalationEntry
        if (collTEscalationEntrys != null)
        {
            for (int i = 0; i < collTEscalationEntrys.size(); i++)
            {
                ((TEscalationEntry) collTEscalationEntrys.get(i))
                        .setPriority(v);
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
     * Get the Tooltip
     *
     * @return String
     */
    public String getTooltip()
    {
        return tooltip;
    }


    /**
     * Set the value of Tooltip
     *
     * @param v new value
     */
    public void setTooltip(String v) 
    {

        if (!ObjectUtils.equals(this.tooltip, v))
        {
            this.tooltip = v;
            setModified(true);
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
     * Get the Wlevel
     *
     * @return Integer
     */
    public Integer getWlevel()
    {
        return wlevel;
    }


    /**
     * Set the value of Wlevel
     *
     * @param v new value
     */
    public void setWlevel(Integer v) 
    {

        if (!ObjectUtils.equals(this.wlevel, v))
        {
            this.wlevel = v;
            setModified(true);
        }


    }

    /**
     * Get the Symbol
     *
     * @return String
     */
    public String getSymbol()
    {
        return symbol;
    }


    /**
     * Set the value of Symbol
     *
     * @param v new value
     */
    public void setSymbol(String v) 
    {

        if (!ObjectUtils.equals(this.symbol, v))
        {
            this.symbol = v;
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
     * Get the IconChanged
     *
     * @return String
     */
    public String getIconChanged()
    {
        return iconChanged;
    }


    /**
     * Set the value of IconChanged
     *
     * @param v new value
     */
    public void setIconChanged(String v) 
    {

        if (!ObjectUtils.equals(this.iconChanged, v))
        {
            this.iconChanged = v;
            setModified(true);
        }


    }

    /**
     * Get the CSSStyle
     *
     * @return String
     */
    public String getCSSStyle()
    {
        return cSSStyle;
    }


    /**
     * Set the value of CSSStyle
     *
     * @param v new value
     */
    public void setCSSStyle(String v) 
    {

        if (!ObjectUtils.equals(this.cSSStyle, v))
        {
            this.cSSStyle = v;
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
     * Collection to store aggregation of collTPprioritys
     */
    protected List<TPpriority> collTPprioritys;

    /**
     * Temporary storage of collTPprioritys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPprioritys()
    {
        if (collTPprioritys == null)
        {
            collTPprioritys = new ArrayList<TPpriority>();
        }
    }


    /**
     * Method called to associate a TPpriority object to this object
     * through the TPpriority foreign key attribute
     *
     * @param l TPpriority
     * @throws TorqueException
     */
    public void addTPpriority(TPpriority l) throws TorqueException
    {
        getTPprioritys().add(l);
        l.setTPriority((TPriority) this);
    }

    /**
     * Method called to associate a TPpriority object to this object
     * through the TPpriority foreign key attribute using connection.
     *
     * @param l TPpriority
     * @throws TorqueException
     */
    public void addTPpriority(TPpriority l, Connection con) throws TorqueException
    {
        getTPprioritys(con).add(l);
        l.setTPriority((TPriority) this);
    }

    /**
     * The criteria used to select the current contents of collTPprioritys
     */
    private Criteria lastTPprioritysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPprioritys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPpriority> getTPprioritys()
        throws TorqueException
    {
        if (collTPprioritys == null)
        {
            collTPprioritys = getTPprioritys(new Criteria(10));
        }
        return collTPprioritys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPriority has previously
     * been saved, it will retrieve related TPprioritys from storage.
     * If this TPriority is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPpriority> getTPprioritys(Criteria criteria) throws TorqueException
    {
        if (collTPprioritys == null)
        {
            if (isNew())
            {
               collTPprioritys = new ArrayList<TPpriority>();
            }
            else
            {
                criteria.add(TPpriorityPeer.PRIORITY, getObjectID() );
                collTPprioritys = TPpriorityPeer.doSelect(criteria);
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
                criteria.add(TPpriorityPeer.PRIORITY, getObjectID());
                if (!lastTPprioritysCriteria.equals(criteria))
                {
                    collTPprioritys = TPpriorityPeer.doSelect(criteria);
                }
            }
        }
        lastTPprioritysCriteria = criteria;

        return collTPprioritys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPprioritys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPpriority> getTPprioritys(Connection con) throws TorqueException
    {
        if (collTPprioritys == null)
        {
            collTPprioritys = getTPprioritys(new Criteria(10), con);
        }
        return collTPprioritys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPriority has previously
     * been saved, it will retrieve related TPprioritys from storage.
     * If this TPriority is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPpriority> getTPprioritys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPprioritys == null)
        {
            if (isNew())
            {
               collTPprioritys = new ArrayList<TPpriority>();
            }
            else
            {
                 criteria.add(TPpriorityPeer.PRIORITY, getObjectID());
                 collTPprioritys = TPpriorityPeer.doSelect(criteria, con);
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
                 criteria.add(TPpriorityPeer.PRIORITY, getObjectID());
                 if (!lastTPprioritysCriteria.equals(criteria))
                 {
                     collTPprioritys = TPpriorityPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPprioritysCriteria = criteria;

         return collTPprioritys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TPprioritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
     */
    protected List<TPpriority> getTPprioritysJoinTPriority(Criteria criteria)
        throws TorqueException
    {
        if (collTPprioritys == null)
        {
            if (isNew())
            {
               collTPprioritys = new ArrayList<TPpriority>();
            }
            else
            {
                criteria.add(TPpriorityPeer.PRIORITY, getObjectID());
                collTPprioritys = TPpriorityPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPpriorityPeer.PRIORITY, getObjectID());
            if (!lastTPprioritysCriteria.equals(criteria))
            {
                collTPprioritys = TPpriorityPeer.doSelectJoinTPriority(criteria);
            }
        }
        lastTPprioritysCriteria = criteria;

        return collTPprioritys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TPprioritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
     */
    protected List<TPpriority> getTPprioritysJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTPprioritys == null)
        {
            if (isNew())
            {
               collTPprioritys = new ArrayList<TPpriority>();
            }
            else
            {
                criteria.add(TPpriorityPeer.PRIORITY, getObjectID());
                collTPprioritys = TPpriorityPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPpriorityPeer.PRIORITY, getObjectID());
            if (!lastTPprioritysCriteria.equals(criteria))
            {
                collTPprioritys = TPpriorityPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTPprioritysCriteria = criteria;

        return collTPprioritys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TPprioritys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
     */
    protected List<TPpriority> getTPprioritysJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTPprioritys == null)
        {
            if (isNew())
            {
               collTPprioritys = new ArrayList<TPpriority>();
            }
            else
            {
                criteria.add(TPpriorityPeer.PRIORITY, getObjectID());
                collTPprioritys = TPpriorityPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPpriorityPeer.PRIORITY, getObjectID());
            if (!lastTPprioritysCriteria.equals(criteria))
            {
                collTPprioritys = TPpriorityPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTPprioritysCriteria = criteria;

        return collTPprioritys;
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
        l.setTPriority((TPriority) this);
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
        l.setTPriority((TPriority) this);
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
     * Otherwise if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     * If this TPriority is new, it will return
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID() );
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     * If this TPriority is new, it will return
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
                 criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
                 criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOwnerID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByChangedByID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByOriginatorID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPersonRelatedByResponsibleID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTClass(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTSeverity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseNoticedID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTReleaseRelatedByReleaseScheduledID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
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
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TWorkItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
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
                criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
                collTWorkItems = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemPeer.PRIORITYKEY, getObjectID());
            if (!lastTWorkItemsCriteria.equals(criteria))
            {
                collTWorkItems = TWorkItemPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWorkItemsCriteria = criteria;

        return collTWorkItems;
    }













    /**
     * Collection to store aggregation of collTEscalationEntrys
     */
    protected List<TEscalationEntry> collTEscalationEntrys;

    /**
     * Temporary storage of collTEscalationEntrys to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTEscalationEntrys()
    {
        if (collTEscalationEntrys == null)
        {
            collTEscalationEntrys = new ArrayList<TEscalationEntry>();
        }
    }


    /**
     * Method called to associate a TEscalationEntry object to this object
     * through the TEscalationEntry foreign key attribute
     *
     * @param l TEscalationEntry
     * @throws TorqueException
     */
    public void addTEscalationEntry(TEscalationEntry l) throws TorqueException
    {
        getTEscalationEntrys().add(l);
        l.setTPriority((TPriority) this);
    }

    /**
     * Method called to associate a TEscalationEntry object to this object
     * through the TEscalationEntry foreign key attribute using connection.
     *
     * @param l TEscalationEntry
     * @throws TorqueException
     */
    public void addTEscalationEntry(TEscalationEntry l, Connection con) throws TorqueException
    {
        getTEscalationEntrys(con).add(l);
        l.setTPriority((TPriority) this);
    }

    /**
     * The criteria used to select the current contents of collTEscalationEntrys
     */
    private Criteria lastTEscalationEntrysCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEscalationEntrys(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TEscalationEntry> getTEscalationEntrys()
        throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            collTEscalationEntrys = getTEscalationEntrys(new Criteria(10));
        }
        return collTEscalationEntrys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPriority has previously
     * been saved, it will retrieve related TEscalationEntrys from storage.
     * If this TPriority is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TEscalationEntry> getTEscalationEntrys(Criteria criteria) throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            if (isNew())
            {
               collTEscalationEntrys = new ArrayList<TEscalationEntry>();
            }
            else
            {
                criteria.add(TEscalationEntryPeer.PRIORITY, getObjectID() );
                collTEscalationEntrys = TEscalationEntryPeer.doSelect(criteria);
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
                criteria.add(TEscalationEntryPeer.PRIORITY, getObjectID());
                if (!lastTEscalationEntrysCriteria.equals(criteria))
                {
                    collTEscalationEntrys = TEscalationEntryPeer.doSelect(criteria);
                }
            }
        }
        lastTEscalationEntrysCriteria = criteria;

        return collTEscalationEntrys;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTEscalationEntrys(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEscalationEntry> getTEscalationEntrys(Connection con) throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            collTEscalationEntrys = getTEscalationEntrys(new Criteria(10), con);
        }
        return collTEscalationEntrys;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPriority has previously
     * been saved, it will retrieve related TEscalationEntrys from storage.
     * If this TPriority is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TEscalationEntry> getTEscalationEntrys(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            if (isNew())
            {
               collTEscalationEntrys = new ArrayList<TEscalationEntry>();
            }
            else
            {
                 criteria.add(TEscalationEntryPeer.PRIORITY, getObjectID());
                 collTEscalationEntrys = TEscalationEntryPeer.doSelect(criteria, con);
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
                 criteria.add(TEscalationEntryPeer.PRIORITY, getObjectID());
                 if (!lastTEscalationEntrysCriteria.equals(criteria))
                 {
                     collTEscalationEntrys = TEscalationEntryPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTEscalationEntrysCriteria = criteria;

         return collTEscalationEntrys;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TEscalationEntrys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
     */
    protected List<TEscalationEntry> getTEscalationEntrysJoinTSLA(Criteria criteria)
        throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            if (isNew())
            {
               collTEscalationEntrys = new ArrayList<TEscalationEntry>();
            }
            else
            {
                criteria.add(TEscalationEntryPeer.PRIORITY, getObjectID());
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTSLA(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEscalationEntryPeer.PRIORITY, getObjectID());
            if (!lastTEscalationEntrysCriteria.equals(criteria))
            {
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTSLA(criteria);
            }
        }
        lastTEscalationEntrysCriteria = criteria;

        return collTEscalationEntrys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TEscalationEntrys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
     */
    protected List<TEscalationEntry> getTEscalationEntrysJoinTPriority(Criteria criteria)
        throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            if (isNew())
            {
               collTEscalationEntrys = new ArrayList<TEscalationEntry>();
            }
            else
            {
                criteria.add(TEscalationEntryPeer.PRIORITY, getObjectID());
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTPriority(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEscalationEntryPeer.PRIORITY, getObjectID());
            if (!lastTEscalationEntrysCriteria.equals(criteria))
            {
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTPriority(criteria);
            }
        }
        lastTEscalationEntrysCriteria = criteria;

        return collTEscalationEntrys;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TPriority is new, it will return
     * an empty collection; or if this TPriority has previously
     * been saved, it will retrieve related TEscalationEntrys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TPriority.
     */
    protected List<TEscalationEntry> getTEscalationEntrysJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTEscalationEntrys == null)
        {
            if (isNew())
            {
               collTEscalationEntrys = new ArrayList<TEscalationEntry>();
            }
            else
            {
                criteria.add(TEscalationEntryPeer.PRIORITY, getObjectID());
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TEscalationEntryPeer.PRIORITY, getObjectID());
            if (!lastTEscalationEntrysCriteria.equals(criteria))
            {
                collTEscalationEntrys = TEscalationEntryPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTEscalationEntrysCriteria = criteria;

        return collTEscalationEntrys;
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
            fieldNames.add("Tooltip");
            fieldNames.add("Sortorder");
            fieldNames.add("Wlevel");
            fieldNames.add("Symbol");
            fieldNames.add("IconKey");
            fieldNames.add("IconChanged");
            fieldNames.add("CSSStyle");
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
        if (name.equals("Tooltip"))
        {
            return getTooltip();
        }
        if (name.equals("Sortorder"))
        {
            return getSortorder();
        }
        if (name.equals("Wlevel"))
        {
            return getWlevel();
        }
        if (name.equals("Symbol"))
        {
            return getSymbol();
        }
        if (name.equals("IconKey"))
        {
            return getIconKey();
        }
        if (name.equals("IconChanged"))
        {
            return getIconChanged();
        }
        if (name.equals("CSSStyle"))
        {
            return getCSSStyle();
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
        if (name.equals("Tooltip"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTooltip((String) value);
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
        if (name.equals("Wlevel"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWlevel((Integer) value);
            return true;
        }
        if (name.equals("Symbol"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSymbol((String) value);
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
        if (name.equals("IconChanged"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIconChanged((String) value);
            return true;
        }
        if (name.equals("CSSStyle"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCSSStyle((String) value);
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
        if (name.equals(TPriorityPeer.PKEY))
        {
            return getObjectID();
        }
        if (name.equals(TPriorityPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TPriorityPeer.TOOLTIP))
        {
            return getTooltip();
        }
        if (name.equals(TPriorityPeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TPriorityPeer.WLEVEL))
        {
            return getWlevel();
        }
        if (name.equals(TPriorityPeer.SYMBOL))
        {
            return getSymbol();
        }
        if (name.equals(TPriorityPeer.ICONKEY))
        {
            return getIconKey();
        }
        if (name.equals(TPriorityPeer.ICONCHANGED))
        {
            return getIconChanged();
        }
        if (name.equals(TPriorityPeer.CSSSTYLE))
        {
            return getCSSStyle();
        }
        if (name.equals(TPriorityPeer.TPUUID))
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
      if (TPriorityPeer.PKEY.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TPriorityPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TPriorityPeer.TOOLTIP.equals(name))
        {
            return setByName("Tooltip", value);
        }
      if (TPriorityPeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TPriorityPeer.WLEVEL.equals(name))
        {
            return setByName("Wlevel", value);
        }
      if (TPriorityPeer.SYMBOL.equals(name))
        {
            return setByName("Symbol", value);
        }
      if (TPriorityPeer.ICONKEY.equals(name))
        {
            return setByName("IconKey", value);
        }
      if (TPriorityPeer.ICONCHANGED.equals(name))
        {
            return setByName("IconChanged", value);
        }
      if (TPriorityPeer.CSSSTYLE.equals(name))
        {
            return setByName("CSSStyle", value);
        }
      if (TPriorityPeer.TPUUID.equals(name))
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
            return getTooltip();
        }
        if (pos == 3)
        {
            return getSortorder();
        }
        if (pos == 4)
        {
            return getWlevel();
        }
        if (pos == 5)
        {
            return getSymbol();
        }
        if (pos == 6)
        {
            return getIconKey();
        }
        if (pos == 7)
        {
            return getIconChanged();
        }
        if (pos == 8)
        {
            return getCSSStyle();
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
            return setByName("Tooltip", value);
        }
    if (position == 3)
        {
            return setByName("Sortorder", value);
        }
    if (position == 4)
        {
            return setByName("Wlevel", value);
        }
    if (position == 5)
        {
            return setByName("Symbol", value);
        }
    if (position == 6)
        {
            return setByName("IconKey", value);
        }
    if (position == 7)
        {
            return setByName("IconChanged", value);
        }
    if (position == 8)
        {
            return setByName("CSSStyle", value);
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
        save(TPriorityPeer.DATABASE_NAME);
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
                    TPriorityPeer.doInsert((TPriority) this, con);
                    setNew(false);
                }
                else
                {
                    TPriorityPeer.doUpdate((TPriority) this, con);
                }
            }


            if (collTPprioritys != null)
            {
                for (int i = 0; i < collTPprioritys.size(); i++)
                {
                    ((TPpriority) collTPprioritys.get(i)).save(con);
                }
            }

            if (collTWorkItems != null)
            {
                for (int i = 0; i < collTWorkItems.size(); i++)
                {
                    ((TWorkItem) collTWorkItems.get(i)).save(con);
                }
            }

            if (collTEscalationEntrys != null)
            {
                for (int i = 0; i < collTEscalationEntrys.size(); i++)
                {
                    ((TEscalationEntry) collTEscalationEntrys.get(i)).save(con);
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
    public TPriority copy() throws TorqueException
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
    public TPriority copy(Connection con) throws TorqueException
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
    public TPriority copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TPriority(), deepcopy);
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
    public TPriority copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TPriority(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TPriority copyInto(TPriority copyObj) throws TorqueException
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
    protected TPriority copyInto(TPriority copyObj, Connection con) throws TorqueException
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
    protected TPriority copyInto(TPriority copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setTooltip(tooltip);
        copyObj.setSortorder(sortorder);
        copyObj.setWlevel(wlevel);
        copyObj.setSymbol(symbol);
        copyObj.setIconKey(iconKey);
        copyObj.setIconChanged(iconChanged);
        copyObj.setCSSStyle(cSSStyle);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TPpriority> vTPprioritys = getTPprioritys();
        if (vTPprioritys != null)
        {
            for (int i = 0; i < vTPprioritys.size(); i++)
            {
                TPpriority obj =  vTPprioritys.get(i);
                copyObj.addTPpriority(obj.copy());
            }
        }
        else
        {
            copyObj.collTPprioritys = null;
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


        List<TEscalationEntry> vTEscalationEntrys = getTEscalationEntrys();
        if (vTEscalationEntrys != null)
        {
            for (int i = 0; i < vTEscalationEntrys.size(); i++)
            {
                TEscalationEntry obj =  vTEscalationEntrys.get(i);
                copyObj.addTEscalationEntry(obj.copy());
            }
        }
        else
        {
            copyObj.collTEscalationEntrys = null;
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
    protected TPriority copyInto(TPriority copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setLabel(label);
        copyObj.setTooltip(tooltip);
        copyObj.setSortorder(sortorder);
        copyObj.setWlevel(wlevel);
        copyObj.setSymbol(symbol);
        copyObj.setIconKey(iconKey);
        copyObj.setIconChanged(iconChanged);
        copyObj.setCSSStyle(cSSStyle);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TPpriority> vTPprioritys = getTPprioritys(con);
        if (vTPprioritys != null)
        {
            for (int i = 0; i < vTPprioritys.size(); i++)
            {
                TPpriority obj =  vTPprioritys.get(i);
                copyObj.addTPpriority(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPprioritys = null;
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


        List<TEscalationEntry> vTEscalationEntrys = getTEscalationEntrys(con);
        if (vTEscalationEntrys != null)
        {
            for (int i = 0; i < vTEscalationEntrys.size(); i++)
            {
                TEscalationEntry obj =  vTEscalationEntrys.get(i);
                copyObj.addTEscalationEntry(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTEscalationEntrys = null;
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
    public TPriorityPeer getPeer()
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
        return TPriorityPeer.getTableMap();
    }

  
    /**
     * Creates a TPriorityBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TPriorityBean with the contents of this object
     */
    public TPriorityBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TPriorityBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TPriorityBean with the contents of this object
     */
    public TPriorityBean getBean(IdentityMap createdBeans)
    {
        TPriorityBean result = (TPriorityBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TPriorityBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setLabel(getLabel());
        result.setTooltip(getTooltip());
        result.setSortorder(getSortorder());
        result.setWlevel(getWlevel());
        result.setSymbol(getSymbol());
        result.setIconKey(getIconKey());
        result.setIconChanged(getIconChanged());
        result.setCSSStyle(getCSSStyle());
        result.setUuid(getUuid());



        if (collTPprioritys != null)
        {
            List<TPpriorityBean> relatedBeans = new ArrayList<TPpriorityBean>(collTPprioritys.size());
            for (Iterator<TPpriority> collTPprioritysIt = collTPprioritys.iterator(); collTPprioritysIt.hasNext(); )
            {
                TPpriority related = (TPpriority) collTPprioritysIt.next();
                TPpriorityBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPpriorityBeans(relatedBeans);
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


        if (collTEscalationEntrys != null)
        {
            List<TEscalationEntryBean> relatedBeans = new ArrayList<TEscalationEntryBean>(collTEscalationEntrys.size());
            for (Iterator<TEscalationEntry> collTEscalationEntrysIt = collTEscalationEntrys.iterator(); collTEscalationEntrysIt.hasNext(); )
            {
                TEscalationEntry related = (TEscalationEntry) collTEscalationEntrysIt.next();
                TEscalationEntryBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTEscalationEntryBeans(relatedBeans);
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
     * Creates an instance of TPriority with the contents
     * of a TPriorityBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TPriorityBean which contents are used to create
     *        the resulting class
     * @return an instance of TPriority with the contents of bean
     */
    public static TPriority createTPriority(TPriorityBean bean)
        throws TorqueException
    {
        return createTPriority(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TPriority with the contents
     * of a TPriorityBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TPriorityBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TPriority with the contents of bean
     */

    public static TPriority createTPriority(TPriorityBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TPriority result = (TPriority) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TPriority();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setLabel(bean.getLabel());
        result.setTooltip(bean.getTooltip());
        result.setSortorder(bean.getSortorder());
        result.setWlevel(bean.getWlevel());
        result.setSymbol(bean.getSymbol());
        result.setIconKey(bean.getIconKey());
        result.setIconChanged(bean.getIconChanged());
        result.setCSSStyle(bean.getCSSStyle());
        result.setUuid(bean.getUuid());



        {
            List<TPpriorityBean> relatedBeans = bean.getTPpriorityBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPpriorityBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPpriorityBean relatedBean =  relatedBeansIt.next();
                    TPpriority related = TPpriority.createTPpriority(relatedBean, createdObjects);
                    result.addTPpriorityFromBean(related);
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
            List<TEscalationEntryBean> relatedBeans = bean.getTEscalationEntryBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TEscalationEntryBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TEscalationEntryBean relatedBean =  relatedBeansIt.next();
                    TEscalationEntry related = TEscalationEntry.createTEscalationEntry(relatedBean, createdObjects);
                    result.addTEscalationEntryFromBean(related);
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
     * Method called to associate a TPpriority object to this object.
     * through the TPpriority foreign key attribute
     *
     * @param toAdd TPpriority
     */
    protected void addTPpriorityFromBean(TPpriority toAdd)
    {
        initTPprioritys();
        collTPprioritys.add(toAdd);
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


    /**
     * Method called to associate a TEscalationEntry object to this object.
     * through the TEscalationEntry foreign key attribute
     *
     * @param toAdd TEscalationEntry
     */
    protected void addTEscalationEntryFromBean(TEscalationEntry toAdd)
    {
        initTEscalationEntrys();
        collTEscalationEntrys.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TPriority:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Tooltip = ")
           .append(getTooltip())
           .append("\n");
        str.append("Sortorder = ")
           .append(getSortorder())
           .append("\n");
        str.append("Wlevel = ")
           .append(getWlevel())
           .append("\n");
        str.append("Symbol = ")
           .append(getSymbol())
           .append("\n");
        str.append("IconKey = ")
           .append(getIconKey())
           .append("\n");
        str.append("IconChanged = ")
           .append(getIconChanged())
           .append("\n");
        str.append("CSSStyle = ")
           .append(getCSSStyle())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
