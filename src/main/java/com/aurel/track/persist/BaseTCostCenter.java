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
import com.aurel.track.beans.TCostCenterBean;

import com.aurel.track.beans.TDepartmentBean;
import com.aurel.track.beans.TAccountBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TCostCenter
 */
public abstract class BaseTCostCenter extends TpBaseObject
{
    /** The Peer class */
    private static final TCostCenterPeer peer =
        new TCostCenterPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the costcenterNumber field */
    private String costcenterNumber;

    /** The value for the costcenterName field */
    private String costcenterName;

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



        // update associated TDepartment
        if (collTDepartments != null)
        {
            for (int i = 0; i < collTDepartments.size(); i++)
            {
                ((TDepartment) collTDepartments.get(i))
                        .setCostcenter(v);
            }
        }

        // update associated TAccount
        if (collTAccounts != null)
        {
            for (int i = 0; i < collTAccounts.size(); i++)
            {
                ((TAccount) collTAccounts.get(i))
                        .setCostCenter(v);
            }
        }
    }

    /**
     * Get the CostcenterNumber
     *
     * @return String
     */
    public String getCostcenterNumber()
    {
        return costcenterNumber;
    }


    /**
     * Set the value of CostcenterNumber
     *
     * @param v new value
     */
    public void setCostcenterNumber(String v) 
    {

        if (!ObjectUtils.equals(this.costcenterNumber, v))
        {
            this.costcenterNumber = v;
            setModified(true);
        }


    }

    /**
     * Get the CostcenterName
     *
     * @return String
     */
    public String getCostcenterName()
    {
        return costcenterName;
    }


    /**
     * Set the value of CostcenterName
     *
     * @param v new value
     */
    public void setCostcenterName(String v) 
    {

        if (!ObjectUtils.equals(this.costcenterName, v))
        {
            this.costcenterName = v;
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
        l.setTCostCenter((TCostCenter) this);
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
        l.setTCostCenter((TCostCenter) this);
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
     * Otherwise if this TCostCenter has previously
     * been saved, it will retrieve related TDepartments from storage.
     * If this TCostCenter is new, it will return
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
                criteria.add(TDepartmentPeer.COSTCENTER, getObjectID() );
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
                criteria.add(TDepartmentPeer.COSTCENTER, getObjectID());
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
     * Otherwise if this TCostCenter has previously
     * been saved, it will retrieve related TDepartments from storage.
     * If this TCostCenter is new, it will return
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
                 criteria.add(TDepartmentPeer.COSTCENTER, getObjectID());
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
                 criteria.add(TDepartmentPeer.COSTCENTER, getObjectID());
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
     * Otherwise if this TCostCenter is new, it will return
     * an empty collection; or if this TCostCenter has previously
     * been saved, it will retrieve related TDepartments from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCostCenter.
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
                criteria.add(TDepartmentPeer.COSTCENTER, getObjectID());
                collTDepartments = TDepartmentPeer.doSelectJoinTCostCenter(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDepartmentPeer.COSTCENTER, getObjectID());
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
     * Otherwise if this TCostCenter is new, it will return
     * an empty collection; or if this TCostCenter has previously
     * been saved, it will retrieve related TDepartments from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCostCenter.
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
                criteria.add(TDepartmentPeer.COSTCENTER, getObjectID());
                collTDepartments = TDepartmentPeer.doSelectJoinTDomain(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TDepartmentPeer.COSTCENTER, getObjectID());
            if (!lastTDepartmentsCriteria.equals(criteria))
            {
                collTDepartments = TDepartmentPeer.doSelectJoinTDomain(criteria);
            }
        }
        lastTDepartmentsCriteria = criteria;

        return collTDepartments;
    }





    /**
     * Collection to store aggregation of collTAccounts
     */
    protected List<TAccount> collTAccounts;

    /**
     * Temporary storage of collTAccounts to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAccounts()
    {
        if (collTAccounts == null)
        {
            collTAccounts = new ArrayList<TAccount>();
        }
    }


    /**
     * Method called to associate a TAccount object to this object
     * through the TAccount foreign key attribute
     *
     * @param l TAccount
     * @throws TorqueException
     */
    public void addTAccount(TAccount l) throws TorqueException
    {
        getTAccounts().add(l);
        l.setTCostCenter((TCostCenter) this);
    }

    /**
     * Method called to associate a TAccount object to this object
     * through the TAccount foreign key attribute using connection.
     *
     * @param l TAccount
     * @throws TorqueException
     */
    public void addTAccount(TAccount l, Connection con) throws TorqueException
    {
        getTAccounts(con).add(l);
        l.setTCostCenter((TCostCenter) this);
    }

    /**
     * The criteria used to select the current contents of collTAccounts
     */
    private Criteria lastTAccountsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAccounts(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAccount> getTAccounts()
        throws TorqueException
    {
        if (collTAccounts == null)
        {
            collTAccounts = getTAccounts(new Criteria(10));
        }
        return collTAccounts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCostCenter has previously
     * been saved, it will retrieve related TAccounts from storage.
     * If this TCostCenter is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAccount> getTAccounts(Criteria criteria) throws TorqueException
    {
        if (collTAccounts == null)
        {
            if (isNew())
            {
               collTAccounts = new ArrayList<TAccount>();
            }
            else
            {
                criteria.add(TAccountPeer.COSTCENTER, getObjectID() );
                collTAccounts = TAccountPeer.doSelect(criteria);
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
                criteria.add(TAccountPeer.COSTCENTER, getObjectID());
                if (!lastTAccountsCriteria.equals(criteria))
                {
                    collTAccounts = TAccountPeer.doSelect(criteria);
                }
            }
        }
        lastTAccountsCriteria = criteria;

        return collTAccounts;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAccounts(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAccount> getTAccounts(Connection con) throws TorqueException
    {
        if (collTAccounts == null)
        {
            collTAccounts = getTAccounts(new Criteria(10), con);
        }
        return collTAccounts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCostCenter has previously
     * been saved, it will retrieve related TAccounts from storage.
     * If this TCostCenter is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAccount> getTAccounts(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAccounts == null)
        {
            if (isNew())
            {
               collTAccounts = new ArrayList<TAccount>();
            }
            else
            {
                 criteria.add(TAccountPeer.COSTCENTER, getObjectID());
                 collTAccounts = TAccountPeer.doSelect(criteria, con);
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
                 criteria.add(TAccountPeer.COSTCENTER, getObjectID());
                 if (!lastTAccountsCriteria.equals(criteria))
                 {
                     collTAccounts = TAccountPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAccountsCriteria = criteria;

         return collTAccounts;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCostCenter is new, it will return
     * an empty collection; or if this TCostCenter has previously
     * been saved, it will retrieve related TAccounts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCostCenter.
     */
    protected List<TAccount> getTAccountsJoinTSystemState(Criteria criteria)
        throws TorqueException
    {
        if (collTAccounts == null)
        {
            if (isNew())
            {
               collTAccounts = new ArrayList<TAccount>();
            }
            else
            {
                criteria.add(TAccountPeer.COSTCENTER, getObjectID());
                collTAccounts = TAccountPeer.doSelectJoinTSystemState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAccountPeer.COSTCENTER, getObjectID());
            if (!lastTAccountsCriteria.equals(criteria))
            {
                collTAccounts = TAccountPeer.doSelectJoinTSystemState(criteria);
            }
        }
        lastTAccountsCriteria = criteria;

        return collTAccounts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TCostCenter is new, it will return
     * an empty collection; or if this TCostCenter has previously
     * been saved, it will retrieve related TAccounts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TCostCenter.
     */
    protected List<TAccount> getTAccountsJoinTCostCenter(Criteria criteria)
        throws TorqueException
    {
        if (collTAccounts == null)
        {
            if (isNew())
            {
               collTAccounts = new ArrayList<TAccount>();
            }
            else
            {
                criteria.add(TAccountPeer.COSTCENTER, getObjectID());
                collTAccounts = TAccountPeer.doSelectJoinTCostCenter(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAccountPeer.COSTCENTER, getObjectID());
            if (!lastTAccountsCriteria.equals(criteria))
            {
                collTAccounts = TAccountPeer.doSelectJoinTCostCenter(criteria);
            }
        }
        lastTAccountsCriteria = criteria;

        return collTAccounts;
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
            fieldNames.add("CostcenterNumber");
            fieldNames.add("CostcenterName");
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
        if (name.equals("CostcenterNumber"))
        {
            return getCostcenterNumber();
        }
        if (name.equals("CostcenterName"))
        {
            return getCostcenterName();
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
        if (name.equals("CostcenterNumber"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCostcenterNumber((String) value);
            return true;
        }
        if (name.equals("CostcenterName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCostcenterName((String) value);
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
        if (name.equals(TCostCenterPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TCostCenterPeer.COSTCENTERNUMBER))
        {
            return getCostcenterNumber();
        }
        if (name.equals(TCostCenterPeer.COSTCENTERNAME))
        {
            return getCostcenterName();
        }
        if (name.equals(TCostCenterPeer.MOREPROPS))
        {
            return getMoreProps();
        }
        if (name.equals(TCostCenterPeer.TPUUID))
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
      if (TCostCenterPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TCostCenterPeer.COSTCENTERNUMBER.equals(name))
        {
            return setByName("CostcenterNumber", value);
        }
      if (TCostCenterPeer.COSTCENTERNAME.equals(name))
        {
            return setByName("CostcenterName", value);
        }
      if (TCostCenterPeer.MOREPROPS.equals(name))
        {
            return setByName("MoreProps", value);
        }
      if (TCostCenterPeer.TPUUID.equals(name))
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
            return getCostcenterNumber();
        }
        if (pos == 2)
        {
            return getCostcenterName();
        }
        if (pos == 3)
        {
            return getMoreProps();
        }
        if (pos == 4)
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
            return setByName("CostcenterNumber", value);
        }
    if (position == 2)
        {
            return setByName("CostcenterName", value);
        }
    if (position == 3)
        {
            return setByName("MoreProps", value);
        }
    if (position == 4)
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
        save(TCostCenterPeer.DATABASE_NAME);
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
                    TCostCenterPeer.doInsert((TCostCenter) this, con);
                    setNew(false);
                }
                else
                {
                    TCostCenterPeer.doUpdate((TCostCenter) this, con);
                }
            }


            if (collTDepartments != null)
            {
                for (int i = 0; i < collTDepartments.size(); i++)
                {
                    ((TDepartment) collTDepartments.get(i)).save(con);
                }
            }

            if (collTAccounts != null)
            {
                for (int i = 0; i < collTAccounts.size(); i++)
                {
                    ((TAccount) collTAccounts.get(i)).save(con);
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
    public TCostCenter copy() throws TorqueException
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
    public TCostCenter copy(Connection con) throws TorqueException
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
    public TCostCenter copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TCostCenter(), deepcopy);
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
    public TCostCenter copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TCostCenter(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TCostCenter copyInto(TCostCenter copyObj) throws TorqueException
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
    protected TCostCenter copyInto(TCostCenter copyObj, Connection con) throws TorqueException
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
    protected TCostCenter copyInto(TCostCenter copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setCostcenterNumber(costcenterNumber);
        copyObj.setCostcenterName(costcenterName);
        copyObj.setMoreProps(moreProps);
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


        List<TAccount> vTAccounts = getTAccounts();
        if (vTAccounts != null)
        {
            for (int i = 0; i < vTAccounts.size(); i++)
            {
                TAccount obj =  vTAccounts.get(i);
                copyObj.addTAccount(obj.copy());
            }
        }
        else
        {
            copyObj.collTAccounts = null;
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
    protected TCostCenter copyInto(TCostCenter copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setCostcenterNumber(costcenterNumber);
        copyObj.setCostcenterName(costcenterName);
        copyObj.setMoreProps(moreProps);
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


        List<TAccount> vTAccounts = getTAccounts(con);
        if (vTAccounts != null)
        {
            for (int i = 0; i < vTAccounts.size(); i++)
            {
                TAccount obj =  vTAccounts.get(i);
                copyObj.addTAccount(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAccounts = null;
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
    public TCostCenterPeer getPeer()
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
        return TCostCenterPeer.getTableMap();
    }

  
    /**
     * Creates a TCostCenterBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TCostCenterBean with the contents of this object
     */
    public TCostCenterBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TCostCenterBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TCostCenterBean with the contents of this object
     */
    public TCostCenterBean getBean(IdentityMap createdBeans)
    {
        TCostCenterBean result = (TCostCenterBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TCostCenterBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setCostcenterNumber(getCostcenterNumber());
        result.setCostcenterName(getCostcenterName());
        result.setMoreProps(getMoreProps());
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


        if (collTAccounts != null)
        {
            List<TAccountBean> relatedBeans = new ArrayList<TAccountBean>(collTAccounts.size());
            for (Iterator<TAccount> collTAccountsIt = collTAccounts.iterator(); collTAccountsIt.hasNext(); )
            {
                TAccount related = (TAccount) collTAccountsIt.next();
                TAccountBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAccountBeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TCostCenter with the contents
     * of a TCostCenterBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TCostCenterBean which contents are used to create
     *        the resulting class
     * @return an instance of TCostCenter with the contents of bean
     */
    public static TCostCenter createTCostCenter(TCostCenterBean bean)
        throws TorqueException
    {
        return createTCostCenter(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TCostCenter with the contents
     * of a TCostCenterBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TCostCenterBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TCostCenter with the contents of bean
     */

    public static TCostCenter createTCostCenter(TCostCenterBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TCostCenter result = (TCostCenter) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TCostCenter();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setCostcenterNumber(bean.getCostcenterNumber());
        result.setCostcenterName(bean.getCostcenterName());
        result.setMoreProps(bean.getMoreProps());
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
            List<TAccountBean> relatedBeans = bean.getTAccountBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAccountBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAccountBean relatedBean =  relatedBeansIt.next();
                    TAccount related = TAccount.createTAccount(relatedBean, createdObjects);
                    result.addTAccountFromBean(related);
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
     * Method called to associate a TAccount object to this object.
     * through the TAccount foreign key attribute
     *
     * @param toAdd TAccount
     */
    protected void addTAccountFromBean(TAccount toAdd)
    {
        initTAccounts();
        collTAccounts.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TCostCenter:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("CostcenterNumber = ")
           .append(getCostcenterNumber())
           .append("\n");
        str.append("CostcenterName = ")
           .append(getCostcenterName())
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
