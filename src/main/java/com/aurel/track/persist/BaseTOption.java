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



import com.aurel.track.persist.TList;
import com.aurel.track.persist.TListPeer;
import com.aurel.track.persist.TBLOB;
import com.aurel.track.persist.TBLOBPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TBLOBBean;

import com.aurel.track.beans.TConfigOptionsRoleBean;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TFieldChangeBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TOption
 */
public abstract class BaseTOption extends TpBaseObject
{
    /** The Peer class */
    private static final TOptionPeer peer =
        new TOptionPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the list field */
    private Integer list;

    /** The value for the label field */
    private String label;

    /** The value for the tooltip field */
    private String tooltip;

    /** The value for the parentOption field */
    private Integer parentOption;

    /** The value for the sortOrder field */
    private Integer sortOrder;

    /** The value for the isDefault field */
    private String isDefault = "N";

    /** The value for the deleted field */
    private String deleted = "N";

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



        // update associated TConfigOptionsRole
        if (collTConfigOptionsRoles != null)
        {
            for (int i = 0; i < collTConfigOptionsRoles.size(); i++)
            {
                ((TConfigOptionsRole) collTConfigOptionsRoles.get(i))
                        .setOptionKey(v);
            }
        }

        // update associated TFieldChange
        if (collTFieldChangesRelatedByNewCustomOptionID != null)
        {
            for (int i = 0; i < collTFieldChangesRelatedByNewCustomOptionID.size(); i++)
            {
                ((TFieldChange) collTFieldChangesRelatedByNewCustomOptionID.get(i))
                        .setNewCustomOptionID(v);
            }
        }

        // update associated TFieldChange
        if (collTFieldChangesRelatedByOldCustomOptionID != null)
        {
            for (int i = 0; i < collTFieldChangesRelatedByOldCustomOptionID.size(); i++)
            {
                ((TFieldChange) collTFieldChangesRelatedByOldCustomOptionID.get(i))
                        .setOldCustomOptionID(v);
            }
        }
    }

    /**
     * Get the List
     *
     * @return Integer
     */
    public Integer getList()
    {
        return list;
    }


    /**
     * Set the value of List
     *
     * @param v new value
     */
    public void setList(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.list, v))
        {
            this.list = v;
            setModified(true);
        }


        if (aTList != null && !ObjectUtils.equals(aTList.getObjectID(), v))
        {
            aTList = null;
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
     * Get the ParentOption
     *
     * @return Integer
     */
    public Integer getParentOption()
    {
        return parentOption;
    }


    /**
     * Set the value of ParentOption
     *
     * @param v new value
     */
    public void setParentOption(Integer v) 
    {

        if (!ObjectUtils.equals(this.parentOption, v))
        {
            this.parentOption = v;
            setModified(true);
        }


    }

    /**
     * Get the SortOrder
     *
     * @return Integer
     */
    public Integer getSortOrder()
    {
        return sortOrder;
    }


    /**
     * Set the value of SortOrder
     *
     * @param v new value
     */
    public void setSortOrder(Integer v) 
    {

        if (!ObjectUtils.equals(this.sortOrder, v))
        {
            this.sortOrder = v;
            setModified(true);
        }


    }

    /**
     * Get the IsDefault
     *
     * @return String
     */
    public String getIsDefault()
    {
        return isDefault;
    }


    /**
     * Set the value of IsDefault
     *
     * @param v new value
     */
    public void setIsDefault(String v) 
    {

        if (!ObjectUtils.equals(this.isDefault, v))
        {
            this.isDefault = v;
            setModified(true);
        }


    }

    /**
     * Get the Deleted
     *
     * @return String
     */
    public String getDeleted()
    {
        return deleted;
    }


    /**
     * Set the value of Deleted
     *
     * @param v new value
     */
    public void setDeleted(String v) 
    {

        if (!ObjectUtils.equals(this.deleted, v))
        {
            this.deleted = v;
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

    



    private TList aTList;

    /**
     * Declares an association between this object and a TList object
     *
     * @param v TList
     * @throws TorqueException
     */
    public void setTList(TList v) throws TorqueException
    {
        if (v == null)
        {
            setList((Integer) null);
        }
        else
        {
            setList(v.getObjectID());
        }
        aTList = v;
    }


    /**
     * Returns the associated TList object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TList object
     * @throws TorqueException
     */
    public TList getTList()
        throws TorqueException
    {
        if (aTList == null && (!ObjectUtils.equals(this.list, null)))
        {
            aTList = TListPeer.retrieveByPK(SimpleKey.keyFor(this.list));
        }
        return aTList;
    }

    /**
     * Return the associated TList object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TList object
     * @throws TorqueException
     */
    public TList getTList(Connection connection)
        throws TorqueException
    {
        if (aTList == null && (!ObjectUtils.equals(this.list, null)))
        {
            aTList = TListPeer.retrieveByPK(SimpleKey.keyFor(this.list), connection);
        }
        return aTList;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTListKey(ObjectKey key) throws TorqueException
    {

        setList(new Integer(((NumberKey) key).intValue()));
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
        l.setTOption((TOption) this);
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
        l.setTOption((TOption) this);
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
     * Otherwise if this TOption has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     * If this TOption is new, it will return
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
                criteria.add(TConfigOptionsRolePeer.OPTIONKEY, getObjectID() );
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
                criteria.add(TConfigOptionsRolePeer.OPTIONKEY, getObjectID());
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
     * Otherwise if this TOption has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     * If this TOption is new, it will return
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
                 criteria.add(TConfigOptionsRolePeer.OPTIONKEY, getObjectID());
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
                 criteria.add(TConfigOptionsRolePeer.OPTIONKEY, getObjectID());
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
     * Otherwise if this TOption is new, it will return
     * an empty collection; or if this TOption has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TOption.
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
                criteria.add(TConfigOptionsRolePeer.OPTIONKEY, getObjectID());
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTFieldConfig(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TConfigOptionsRolePeer.OPTIONKEY, getObjectID());
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
     * Otherwise if this TOption is new, it will return
     * an empty collection; or if this TOption has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TOption.
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
                criteria.add(TConfigOptionsRolePeer.OPTIONKEY, getObjectID());
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTRole(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TConfigOptionsRolePeer.OPTIONKEY, getObjectID());
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
     * Otherwise if this TOption is new, it will return
     * an empty collection; or if this TOption has previously
     * been saved, it will retrieve related TConfigOptionsRoles from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TOption.
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
                criteria.add(TConfigOptionsRolePeer.OPTIONKEY, getObjectID());
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTOption(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TConfigOptionsRolePeer.OPTIONKEY, getObjectID());
            if (!lastTConfigOptionsRolesCriteria.equals(criteria))
            {
                collTConfigOptionsRoles = TConfigOptionsRolePeer.doSelectJoinTOption(criteria);
            }
        }
        lastTConfigOptionsRolesCriteria = criteria;

        return collTConfigOptionsRoles;
    }





    /**
     * Collection to store aggregation of collTFieldChangesRelatedByNewCustomOptionID
     */
    protected List<TFieldChange> collTFieldChangesRelatedByNewCustomOptionID;

    /**
     * Temporary storage of collTFieldChangesRelatedByNewCustomOptionID to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTFieldChangesRelatedByNewCustomOptionID()
    {
        if (collTFieldChangesRelatedByNewCustomOptionID == null)
        {
            collTFieldChangesRelatedByNewCustomOptionID = new ArrayList<TFieldChange>();
        }
    }


    /**
     * Method called to associate a TFieldChange object to this object
     * through the TFieldChange foreign key attribute
     *
     * @param l TFieldChange
     * @throws TorqueException
     */
    public void addTFieldChangeRelatedByNewCustomOptionID(TFieldChange l) throws TorqueException
    {
        getTFieldChangesRelatedByNewCustomOptionID().add(l);
        l.setTOptionRelatedByNewCustomOptionID((TOption) this);
    }

    /**
     * Method called to associate a TFieldChange object to this object
     * through the TFieldChange foreign key attribute using connection.
     *
     * @param l TFieldChange
     * @throws TorqueException
     */
    public void addTFieldChangeRelatedByNewCustomOptionID(TFieldChange l, Connection con) throws TorqueException
    {
        getTFieldChangesRelatedByNewCustomOptionID(con).add(l);
        l.setTOptionRelatedByNewCustomOptionID((TOption) this);
    }

    /**
     * The criteria used to select the current contents of collTFieldChangesRelatedByNewCustomOptionID
     */
    private Criteria lastTFieldChangesRelatedByNewCustomOptionIDCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldChangesRelatedByNewCustomOptionID(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TFieldChange> getTFieldChangesRelatedByNewCustomOptionID()
        throws TorqueException
    {
        if (collTFieldChangesRelatedByNewCustomOptionID == null)
        {
            collTFieldChangesRelatedByNewCustomOptionID = getTFieldChangesRelatedByNewCustomOptionID(new Criteria(10));
        }
        return collTFieldChangesRelatedByNewCustomOptionID;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOption has previously
     * been saved, it will retrieve related TFieldChangesRelatedByNewCustomOptionID from storage.
     * If this TOption is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TFieldChange> getTFieldChangesRelatedByNewCustomOptionID(Criteria criteria) throws TorqueException
    {
        if (collTFieldChangesRelatedByNewCustomOptionID == null)
        {
            if (isNew())
            {
               collTFieldChangesRelatedByNewCustomOptionID = new ArrayList<TFieldChange>();
            }
            else
            {
                criteria.add(TFieldChangePeer.NEWCUSTOMOPTIONID, getObjectID() );
                collTFieldChangesRelatedByNewCustomOptionID = TFieldChangePeer.doSelect(criteria);
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
                criteria.add(TFieldChangePeer.NEWCUSTOMOPTIONID, getObjectID());
                if (!lastTFieldChangesRelatedByNewCustomOptionIDCriteria.equals(criteria))
                {
                    collTFieldChangesRelatedByNewCustomOptionID = TFieldChangePeer.doSelect(criteria);
                }
            }
        }
        lastTFieldChangesRelatedByNewCustomOptionIDCriteria = criteria;

        return collTFieldChangesRelatedByNewCustomOptionID;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldChangesRelatedByNewCustomOptionID(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldChange> getTFieldChangesRelatedByNewCustomOptionID(Connection con) throws TorqueException
    {
        if (collTFieldChangesRelatedByNewCustomOptionID == null)
        {
            collTFieldChangesRelatedByNewCustomOptionID = getTFieldChangesRelatedByNewCustomOptionID(new Criteria(10), con);
        }
        return collTFieldChangesRelatedByNewCustomOptionID;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOption has previously
     * been saved, it will retrieve related TFieldChangesRelatedByNewCustomOptionID from storage.
     * If this TOption is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldChange> getTFieldChangesRelatedByNewCustomOptionID(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTFieldChangesRelatedByNewCustomOptionID == null)
        {
            if (isNew())
            {
               collTFieldChangesRelatedByNewCustomOptionID = new ArrayList<TFieldChange>();
            }
            else
            {
                 criteria.add(TFieldChangePeer.NEWCUSTOMOPTIONID, getObjectID());
                 collTFieldChangesRelatedByNewCustomOptionID = TFieldChangePeer.doSelect(criteria, con);
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
                 criteria.add(TFieldChangePeer.NEWCUSTOMOPTIONID, getObjectID());
                 if (!lastTFieldChangesRelatedByNewCustomOptionIDCriteria.equals(criteria))
                 {
                     collTFieldChangesRelatedByNewCustomOptionID = TFieldChangePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTFieldChangesRelatedByNewCustomOptionIDCriteria = criteria;

         return collTFieldChangesRelatedByNewCustomOptionID;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOption is new, it will return
     * an empty collection; or if this TOption has previously
     * been saved, it will retrieve related TFieldChangesRelatedByNewCustomOptionID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TOption.
     */
    protected List<TFieldChange> getTFieldChangesRelatedByNewCustomOptionIDJoinTHistoryTransaction(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldChangesRelatedByNewCustomOptionID == null)
        {
            if (isNew())
            {
               collTFieldChangesRelatedByNewCustomOptionID = new ArrayList<TFieldChange>();
            }
            else
            {
                criteria.add(TFieldChangePeer.NEWCUSTOMOPTIONID, getObjectID());
                collTFieldChangesRelatedByNewCustomOptionID = TFieldChangePeer.doSelectJoinTHistoryTransaction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldChangePeer.NEWCUSTOMOPTIONID, getObjectID());
            if (!lastTFieldChangesRelatedByNewCustomOptionIDCriteria.equals(criteria))
            {
                collTFieldChangesRelatedByNewCustomOptionID = TFieldChangePeer.doSelectJoinTHistoryTransaction(criteria);
            }
        }
        lastTFieldChangesRelatedByNewCustomOptionIDCriteria = criteria;

        return collTFieldChangesRelatedByNewCustomOptionID;
    }

























    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOption is new, it will return
     * an empty collection; or if this TOption has previously
     * been saved, it will retrieve related TFieldChangesRelatedByNewCustomOptionID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TOption.
     */
    protected List<TFieldChange> getTFieldChangesRelatedByNewCustomOptionIDJoinTOptionRelatedByOldCustomOptionID(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldChangesRelatedByNewCustomOptionID == null)
        {
            if (isNew())
            {
               collTFieldChangesRelatedByNewCustomOptionID = new ArrayList<TFieldChange>();
            }
            else
            {
                criteria.add(TFieldChangePeer.NEWCUSTOMOPTIONID, getObjectID());
                collTFieldChangesRelatedByNewCustomOptionID = TFieldChangePeer.doSelectJoinTOptionRelatedByOldCustomOptionID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldChangePeer.NEWCUSTOMOPTIONID, getObjectID());
            if (!lastTFieldChangesRelatedByNewCustomOptionIDCriteria.equals(criteria))
            {
                collTFieldChangesRelatedByNewCustomOptionID = TFieldChangePeer.doSelectJoinTOptionRelatedByOldCustomOptionID(criteria);
            }
        }
        lastTFieldChangesRelatedByNewCustomOptionIDCriteria = criteria;

        return collTFieldChangesRelatedByNewCustomOptionID;
    }





    /**
     * Collection to store aggregation of collTFieldChangesRelatedByOldCustomOptionID
     */
    protected List<TFieldChange> collTFieldChangesRelatedByOldCustomOptionID;

    /**
     * Temporary storage of collTFieldChangesRelatedByOldCustomOptionID to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTFieldChangesRelatedByOldCustomOptionID()
    {
        if (collTFieldChangesRelatedByOldCustomOptionID == null)
        {
            collTFieldChangesRelatedByOldCustomOptionID = new ArrayList<TFieldChange>();
        }
    }


    /**
     * Method called to associate a TFieldChange object to this object
     * through the TFieldChange foreign key attribute
     *
     * @param l TFieldChange
     * @throws TorqueException
     */
    public void addTFieldChangeRelatedByOldCustomOptionID(TFieldChange l) throws TorqueException
    {
        getTFieldChangesRelatedByOldCustomOptionID().add(l);
        l.setTOptionRelatedByOldCustomOptionID((TOption) this);
    }

    /**
     * Method called to associate a TFieldChange object to this object
     * through the TFieldChange foreign key attribute using connection.
     *
     * @param l TFieldChange
     * @throws TorqueException
     */
    public void addTFieldChangeRelatedByOldCustomOptionID(TFieldChange l, Connection con) throws TorqueException
    {
        getTFieldChangesRelatedByOldCustomOptionID(con).add(l);
        l.setTOptionRelatedByOldCustomOptionID((TOption) this);
    }

    /**
     * The criteria used to select the current contents of collTFieldChangesRelatedByOldCustomOptionID
     */
    private Criteria lastTFieldChangesRelatedByOldCustomOptionIDCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldChangesRelatedByOldCustomOptionID(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TFieldChange> getTFieldChangesRelatedByOldCustomOptionID()
        throws TorqueException
    {
        if (collTFieldChangesRelatedByOldCustomOptionID == null)
        {
            collTFieldChangesRelatedByOldCustomOptionID = getTFieldChangesRelatedByOldCustomOptionID(new Criteria(10));
        }
        return collTFieldChangesRelatedByOldCustomOptionID;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOption has previously
     * been saved, it will retrieve related TFieldChangesRelatedByOldCustomOptionID from storage.
     * If this TOption is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TFieldChange> getTFieldChangesRelatedByOldCustomOptionID(Criteria criteria) throws TorqueException
    {
        if (collTFieldChangesRelatedByOldCustomOptionID == null)
        {
            if (isNew())
            {
               collTFieldChangesRelatedByOldCustomOptionID = new ArrayList<TFieldChange>();
            }
            else
            {
                criteria.add(TFieldChangePeer.OLDCUSTOMOPTIONID, getObjectID() );
                collTFieldChangesRelatedByOldCustomOptionID = TFieldChangePeer.doSelect(criteria);
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
                criteria.add(TFieldChangePeer.OLDCUSTOMOPTIONID, getObjectID());
                if (!lastTFieldChangesRelatedByOldCustomOptionIDCriteria.equals(criteria))
                {
                    collTFieldChangesRelatedByOldCustomOptionID = TFieldChangePeer.doSelect(criteria);
                }
            }
        }
        lastTFieldChangesRelatedByOldCustomOptionIDCriteria = criteria;

        return collTFieldChangesRelatedByOldCustomOptionID;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTFieldChangesRelatedByOldCustomOptionID(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldChange> getTFieldChangesRelatedByOldCustomOptionID(Connection con) throws TorqueException
    {
        if (collTFieldChangesRelatedByOldCustomOptionID == null)
        {
            collTFieldChangesRelatedByOldCustomOptionID = getTFieldChangesRelatedByOldCustomOptionID(new Criteria(10), con);
        }
        return collTFieldChangesRelatedByOldCustomOptionID;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOption has previously
     * been saved, it will retrieve related TFieldChangesRelatedByOldCustomOptionID from storage.
     * If this TOption is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TFieldChange> getTFieldChangesRelatedByOldCustomOptionID(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTFieldChangesRelatedByOldCustomOptionID == null)
        {
            if (isNew())
            {
               collTFieldChangesRelatedByOldCustomOptionID = new ArrayList<TFieldChange>();
            }
            else
            {
                 criteria.add(TFieldChangePeer.OLDCUSTOMOPTIONID, getObjectID());
                 collTFieldChangesRelatedByOldCustomOptionID = TFieldChangePeer.doSelect(criteria, con);
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
                 criteria.add(TFieldChangePeer.OLDCUSTOMOPTIONID, getObjectID());
                 if (!lastTFieldChangesRelatedByOldCustomOptionIDCriteria.equals(criteria))
                 {
                     collTFieldChangesRelatedByOldCustomOptionID = TFieldChangePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTFieldChangesRelatedByOldCustomOptionIDCriteria = criteria;

         return collTFieldChangesRelatedByOldCustomOptionID;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOption is new, it will return
     * an empty collection; or if this TOption has previously
     * been saved, it will retrieve related TFieldChangesRelatedByOldCustomOptionID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TOption.
     */
    protected List<TFieldChange> getTFieldChangesRelatedByOldCustomOptionIDJoinTHistoryTransaction(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldChangesRelatedByOldCustomOptionID == null)
        {
            if (isNew())
            {
               collTFieldChangesRelatedByOldCustomOptionID = new ArrayList<TFieldChange>();
            }
            else
            {
                criteria.add(TFieldChangePeer.OLDCUSTOMOPTIONID, getObjectID());
                collTFieldChangesRelatedByOldCustomOptionID = TFieldChangePeer.doSelectJoinTHistoryTransaction(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldChangePeer.OLDCUSTOMOPTIONID, getObjectID());
            if (!lastTFieldChangesRelatedByOldCustomOptionIDCriteria.equals(criteria))
            {
                collTFieldChangesRelatedByOldCustomOptionID = TFieldChangePeer.doSelectJoinTHistoryTransaction(criteria);
            }
        }
        lastTFieldChangesRelatedByOldCustomOptionIDCriteria = criteria;

        return collTFieldChangesRelatedByOldCustomOptionID;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TOption is new, it will return
     * an empty collection; or if this TOption has previously
     * been saved, it will retrieve related TFieldChangesRelatedByOldCustomOptionID from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TOption.
     */
    protected List<TFieldChange> getTFieldChangesRelatedByOldCustomOptionIDJoinTOptionRelatedByNewCustomOptionID(Criteria criteria)
        throws TorqueException
    {
        if (collTFieldChangesRelatedByOldCustomOptionID == null)
        {
            if (isNew())
            {
               collTFieldChangesRelatedByOldCustomOptionID = new ArrayList<TFieldChange>();
            }
            else
            {
                criteria.add(TFieldChangePeer.OLDCUSTOMOPTIONID, getObjectID());
                collTFieldChangesRelatedByOldCustomOptionID = TFieldChangePeer.doSelectJoinTOptionRelatedByNewCustomOptionID(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TFieldChangePeer.OLDCUSTOMOPTIONID, getObjectID());
            if (!lastTFieldChangesRelatedByOldCustomOptionIDCriteria.equals(criteria))
            {
                collTFieldChangesRelatedByOldCustomOptionID = TFieldChangePeer.doSelectJoinTOptionRelatedByNewCustomOptionID(criteria);
            }
        }
        lastTFieldChangesRelatedByOldCustomOptionIDCriteria = criteria;

        return collTFieldChangesRelatedByOldCustomOptionID;
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
            fieldNames.add("List");
            fieldNames.add("Label");
            fieldNames.add("Tooltip");
            fieldNames.add("ParentOption");
            fieldNames.add("SortOrder");
            fieldNames.add("IsDefault");
            fieldNames.add("Deleted");
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
        if (name.equals("List"))
        {
            return getList();
        }
        if (name.equals("Label"))
        {
            return getLabel();
        }
        if (name.equals("Tooltip"))
        {
            return getTooltip();
        }
        if (name.equals("ParentOption"))
        {
            return getParentOption();
        }
        if (name.equals("SortOrder"))
        {
            return getSortOrder();
        }
        if (name.equals("IsDefault"))
        {
            return getIsDefault();
        }
        if (name.equals("Deleted"))
        {
            return getDeleted();
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
        if (name.equals("List"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setList((Integer) value);
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
        if (name.equals("ParentOption"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParentOption((Integer) value);
            return true;
        }
        if (name.equals("SortOrder"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSortOrder((Integer) value);
            return true;
        }
        if (name.equals("IsDefault"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIsDefault((String) value);
            return true;
        }
        if (name.equals("Deleted"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDeleted((String) value);
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
        if (name.equals(TOptionPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TOptionPeer.LIST))
        {
            return getList();
        }
        if (name.equals(TOptionPeer.LABEL))
        {
            return getLabel();
        }
        if (name.equals(TOptionPeer.TOOLTIP))
        {
            return getTooltip();
        }
        if (name.equals(TOptionPeer.PARENTOPTION))
        {
            return getParentOption();
        }
        if (name.equals(TOptionPeer.SORTORDER))
        {
            return getSortOrder();
        }
        if (name.equals(TOptionPeer.ISDEFAULT))
        {
            return getIsDefault();
        }
        if (name.equals(TOptionPeer.DELETED))
        {
            return getDeleted();
        }
        if (name.equals(TOptionPeer.SYMBOL))
        {
            return getSymbol();
        }
        if (name.equals(TOptionPeer.ICONKEY))
        {
            return getIconKey();
        }
        if (name.equals(TOptionPeer.ICONCHANGED))
        {
            return getIconChanged();
        }
        if (name.equals(TOptionPeer.CSSSTYLE))
        {
            return getCSSStyle();
        }
        if (name.equals(TOptionPeer.TPUUID))
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
      if (TOptionPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TOptionPeer.LIST.equals(name))
        {
            return setByName("List", value);
        }
      if (TOptionPeer.LABEL.equals(name))
        {
            return setByName("Label", value);
        }
      if (TOptionPeer.TOOLTIP.equals(name))
        {
            return setByName("Tooltip", value);
        }
      if (TOptionPeer.PARENTOPTION.equals(name))
        {
            return setByName("ParentOption", value);
        }
      if (TOptionPeer.SORTORDER.equals(name))
        {
            return setByName("SortOrder", value);
        }
      if (TOptionPeer.ISDEFAULT.equals(name))
        {
            return setByName("IsDefault", value);
        }
      if (TOptionPeer.DELETED.equals(name))
        {
            return setByName("Deleted", value);
        }
      if (TOptionPeer.SYMBOL.equals(name))
        {
            return setByName("Symbol", value);
        }
      if (TOptionPeer.ICONKEY.equals(name))
        {
            return setByName("IconKey", value);
        }
      if (TOptionPeer.ICONCHANGED.equals(name))
        {
            return setByName("IconChanged", value);
        }
      if (TOptionPeer.CSSSTYLE.equals(name))
        {
            return setByName("CSSStyle", value);
        }
      if (TOptionPeer.TPUUID.equals(name))
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
            return getList();
        }
        if (pos == 2)
        {
            return getLabel();
        }
        if (pos == 3)
        {
            return getTooltip();
        }
        if (pos == 4)
        {
            return getParentOption();
        }
        if (pos == 5)
        {
            return getSortOrder();
        }
        if (pos == 6)
        {
            return getIsDefault();
        }
        if (pos == 7)
        {
            return getDeleted();
        }
        if (pos == 8)
        {
            return getSymbol();
        }
        if (pos == 9)
        {
            return getIconKey();
        }
        if (pos == 10)
        {
            return getIconChanged();
        }
        if (pos == 11)
        {
            return getCSSStyle();
        }
        if (pos == 12)
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
            return setByName("List", value);
        }
    if (position == 2)
        {
            return setByName("Label", value);
        }
    if (position == 3)
        {
            return setByName("Tooltip", value);
        }
    if (position == 4)
        {
            return setByName("ParentOption", value);
        }
    if (position == 5)
        {
            return setByName("SortOrder", value);
        }
    if (position == 6)
        {
            return setByName("IsDefault", value);
        }
    if (position == 7)
        {
            return setByName("Deleted", value);
        }
    if (position == 8)
        {
            return setByName("Symbol", value);
        }
    if (position == 9)
        {
            return setByName("IconKey", value);
        }
    if (position == 10)
        {
            return setByName("IconChanged", value);
        }
    if (position == 11)
        {
            return setByName("CSSStyle", value);
        }
    if (position == 12)
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
        save(TOptionPeer.DATABASE_NAME);
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
                    TOptionPeer.doInsert((TOption) this, con);
                    setNew(false);
                }
                else
                {
                    TOptionPeer.doUpdate((TOption) this, con);
                }
            }


            if (collTConfigOptionsRoles != null)
            {
                for (int i = 0; i < collTConfigOptionsRoles.size(); i++)
                {
                    ((TConfigOptionsRole) collTConfigOptionsRoles.get(i)).save(con);
                }
            }

            if (collTFieldChangesRelatedByNewCustomOptionID != null)
            {
                for (int i = 0; i < collTFieldChangesRelatedByNewCustomOptionID.size(); i++)
                {
                    ((TFieldChange) collTFieldChangesRelatedByNewCustomOptionID.get(i)).save(con);
                }
            }

            if (collTFieldChangesRelatedByOldCustomOptionID != null)
            {
                for (int i = 0; i < collTFieldChangesRelatedByOldCustomOptionID.size(); i++)
                {
                    ((TFieldChange) collTFieldChangesRelatedByOldCustomOptionID.get(i)).save(con);
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
    public TOption copy() throws TorqueException
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
    public TOption copy(Connection con) throws TorqueException
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
    public TOption copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TOption(), deepcopy);
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
    public TOption copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TOption(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TOption copyInto(TOption copyObj) throws TorqueException
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
    protected TOption copyInto(TOption copyObj, Connection con) throws TorqueException
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
    protected TOption copyInto(TOption copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setList(list);
        copyObj.setLabel(label);
        copyObj.setTooltip(tooltip);
        copyObj.setParentOption(parentOption);
        copyObj.setSortOrder(sortOrder);
        copyObj.setIsDefault(isDefault);
        copyObj.setDeleted(deleted);
        copyObj.setSymbol(symbol);
        copyObj.setIconKey(iconKey);
        copyObj.setIconChanged(iconChanged);
        copyObj.setCSSStyle(cSSStyle);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


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


        List<TFieldChange> vTFieldChangesRelatedByNewCustomOptionID = getTFieldChangesRelatedByNewCustomOptionID();
        if (vTFieldChangesRelatedByNewCustomOptionID != null)
        {
            for (int i = 0; i < vTFieldChangesRelatedByNewCustomOptionID.size(); i++)
            {
                TFieldChange obj =  vTFieldChangesRelatedByNewCustomOptionID.get(i);
                copyObj.addTFieldChangeRelatedByNewCustomOptionID(obj.copy());
            }
        }
        else
        {
            copyObj.collTFieldChangesRelatedByNewCustomOptionID = null;
        }


        List<TFieldChange> vTFieldChangesRelatedByOldCustomOptionID = getTFieldChangesRelatedByOldCustomOptionID();
        if (vTFieldChangesRelatedByOldCustomOptionID != null)
        {
            for (int i = 0; i < vTFieldChangesRelatedByOldCustomOptionID.size(); i++)
            {
                TFieldChange obj =  vTFieldChangesRelatedByOldCustomOptionID.get(i);
                copyObj.addTFieldChangeRelatedByOldCustomOptionID(obj.copy());
            }
        }
        else
        {
            copyObj.collTFieldChangesRelatedByOldCustomOptionID = null;
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
    protected TOption copyInto(TOption copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setList(list);
        copyObj.setLabel(label);
        copyObj.setTooltip(tooltip);
        copyObj.setParentOption(parentOption);
        copyObj.setSortOrder(sortOrder);
        copyObj.setIsDefault(isDefault);
        copyObj.setDeleted(deleted);
        copyObj.setSymbol(symbol);
        copyObj.setIconKey(iconKey);
        copyObj.setIconChanged(iconChanged);
        copyObj.setCSSStyle(cSSStyle);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


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


        List<TFieldChange> vTFieldChangesRelatedByNewCustomOptionID = getTFieldChangesRelatedByNewCustomOptionID(con);
        if (vTFieldChangesRelatedByNewCustomOptionID != null)
        {
            for (int i = 0; i < vTFieldChangesRelatedByNewCustomOptionID.size(); i++)
            {
                TFieldChange obj =  vTFieldChangesRelatedByNewCustomOptionID.get(i);
                copyObj.addTFieldChangeRelatedByNewCustomOptionID(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTFieldChangesRelatedByNewCustomOptionID = null;
        }


        List<TFieldChange> vTFieldChangesRelatedByOldCustomOptionID = getTFieldChangesRelatedByOldCustomOptionID(con);
        if (vTFieldChangesRelatedByOldCustomOptionID != null)
        {
            for (int i = 0; i < vTFieldChangesRelatedByOldCustomOptionID.size(); i++)
            {
                TFieldChange obj =  vTFieldChangesRelatedByOldCustomOptionID.get(i);
                copyObj.addTFieldChangeRelatedByOldCustomOptionID(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTFieldChangesRelatedByOldCustomOptionID = null;
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
    public TOptionPeer getPeer()
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
        return TOptionPeer.getTableMap();
    }

  
    /**
     * Creates a TOptionBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TOptionBean with the contents of this object
     */
    public TOptionBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TOptionBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TOptionBean with the contents of this object
     */
    public TOptionBean getBean(IdentityMap createdBeans)
    {
        TOptionBean result = (TOptionBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TOptionBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setList(getList());
        result.setLabel(getLabel());
        result.setTooltip(getTooltip());
        result.setParentOption(getParentOption());
        result.setSortOrder(getSortOrder());
        result.setIsDefault(getIsDefault());
        result.setDeleted(getDeleted());
        result.setSymbol(getSymbol());
        result.setIconKey(getIconKey());
        result.setIconChanged(getIconChanged());
        result.setCSSStyle(getCSSStyle());
        result.setUuid(getUuid());



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


        if (collTFieldChangesRelatedByNewCustomOptionID != null)
        {
            List<TFieldChangeBean> relatedBeans = new ArrayList<TFieldChangeBean>(collTFieldChangesRelatedByNewCustomOptionID.size());
            for (Iterator<TFieldChange> collTFieldChangesRelatedByNewCustomOptionIDIt = collTFieldChangesRelatedByNewCustomOptionID.iterator(); collTFieldChangesRelatedByNewCustomOptionIDIt.hasNext(); )
            {
                TFieldChange related = (TFieldChange) collTFieldChangesRelatedByNewCustomOptionIDIt.next();
                TFieldChangeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTFieldChangeBeansRelatedByNewCustomOptionID(relatedBeans);
        }


        if (collTFieldChangesRelatedByOldCustomOptionID != null)
        {
            List<TFieldChangeBean> relatedBeans = new ArrayList<TFieldChangeBean>(collTFieldChangesRelatedByOldCustomOptionID.size());
            for (Iterator<TFieldChange> collTFieldChangesRelatedByOldCustomOptionIDIt = collTFieldChangesRelatedByOldCustomOptionID.iterator(); collTFieldChangesRelatedByOldCustomOptionIDIt.hasNext(); )
            {
                TFieldChange related = (TFieldChange) collTFieldChangesRelatedByOldCustomOptionIDIt.next();
                TFieldChangeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTFieldChangeBeansRelatedByOldCustomOptionID(relatedBeans);
        }




        if (aTList != null)
        {
            TListBean relatedBean = aTList.getBean(createdBeans);
            result.setTListBean(relatedBean);
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
     * Creates an instance of TOption with the contents
     * of a TOptionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TOptionBean which contents are used to create
     *        the resulting class
     * @return an instance of TOption with the contents of bean
     */
    public static TOption createTOption(TOptionBean bean)
        throws TorqueException
    {
        return createTOption(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TOption with the contents
     * of a TOptionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TOptionBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TOption with the contents of bean
     */

    public static TOption createTOption(TOptionBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TOption result = (TOption) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TOption();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setList(bean.getList());
        result.setLabel(bean.getLabel());
        result.setTooltip(bean.getTooltip());
        result.setParentOption(bean.getParentOption());
        result.setSortOrder(bean.getSortOrder());
        result.setIsDefault(bean.getIsDefault());
        result.setDeleted(bean.getDeleted());
        result.setSymbol(bean.getSymbol());
        result.setIconKey(bean.getIconKey());
        result.setIconChanged(bean.getIconChanged());
        result.setCSSStyle(bean.getCSSStyle());
        result.setUuid(bean.getUuid());



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
            List<TFieldChangeBean> relatedBeans = bean.getTFieldChangeBeansRelatedByNewCustomOptionID();
            if (relatedBeans != null)
            {
                for (Iterator<TFieldChangeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TFieldChangeBean relatedBean =  relatedBeansIt.next();
                    TFieldChange related = TFieldChange.createTFieldChange(relatedBean, createdObjects);
                    result.addTFieldChangeRelatedByNewCustomOptionIDFromBean(related);
                }
            }
        }


        {
            List<TFieldChangeBean> relatedBeans = bean.getTFieldChangeBeansRelatedByOldCustomOptionID();
            if (relatedBeans != null)
            {
                for (Iterator<TFieldChangeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TFieldChangeBean relatedBean =  relatedBeansIt.next();
                    TFieldChange related = TFieldChange.createTFieldChange(relatedBean, createdObjects);
                    result.addTFieldChangeRelatedByOldCustomOptionIDFromBean(related);
                }
            }
        }




        {
            TListBean relatedBean = bean.getTListBean();
            if (relatedBean != null)
            {
                TList relatedObject = TList.createTList(relatedBean, createdObjects);
                result.setTList(relatedObject);
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
     * Method called to associate a TFieldChange object to this object.
     * through the TFieldChange foreign key attribute
     *
     * @param toAdd TFieldChange
     */
    protected void addTFieldChangeRelatedByNewCustomOptionIDFromBean(TFieldChange toAdd)
    {
        initTFieldChangesRelatedByNewCustomOptionID();
        collTFieldChangesRelatedByNewCustomOptionID.add(toAdd);
    }


    /**
     * Method called to associate a TFieldChange object to this object.
     * through the TFieldChange foreign key attribute
     *
     * @param toAdd TFieldChange
     */
    protected void addTFieldChangeRelatedByOldCustomOptionIDFromBean(TFieldChange toAdd)
    {
        initTFieldChangesRelatedByOldCustomOptionID();
        collTFieldChangesRelatedByOldCustomOptionID.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TOption:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("List = ")
           .append(getList())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        str.append("Tooltip = ")
           .append(getTooltip())
           .append("\n");
        str.append("ParentOption = ")
           .append(getParentOption())
           .append("\n");
        str.append("SortOrder = ")
           .append(getSortOrder())
           .append("\n");
        str.append("IsDefault = ")
           .append(getIsDefault())
           .append("\n");
        str.append("Deleted = ")
           .append(getDeleted())
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
