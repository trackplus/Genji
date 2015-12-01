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



import com.aurel.track.persist.TAttributeOption;
import com.aurel.track.persist.TAttributeOptionPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TAttributeBean;
import com.aurel.track.beans.TAttributeOptionBean;

import com.aurel.track.beans.TIssueAttributeValueBean;
import com.aurel.track.beans.TAttributeOptionBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TAttribute
 */
public abstract class BaseTAttribute extends TpBaseObject
{
    /** The Peer class */
    private static final TAttributePeer peer =
        new TAttributePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the attributeName field */
    private String attributeName;

    /** The value for the attributeType field */
    private Integer attributeType;

    /** The value for the deleted field */
    private Integer deleted;

    /** The value for the description field */
    private String description;

    /** The value for the permission field */
    private String permission;

    /** The value for the requiredOption field */
    private Integer requiredOption;

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



        // update associated TIssueAttributeValue
        if (collTIssueAttributeValues != null)
        {
            for (int i = 0; i < collTIssueAttributeValues.size(); i++)
            {
                ((TIssueAttributeValue) collTIssueAttributeValues.get(i))
                        .setAttributeID(v);
            }
        }

        // update associated TAttributeOption
        if (collTAttributeOptions != null)
        {
            for (int i = 0; i < collTAttributeOptions.size(); i++)
            {
                ((TAttributeOption) collTAttributeOptions.get(i))
                        .setAttributeID(v);
            }
        }
    }

    /**
     * Get the AttributeName
     *
     * @return String
     */
    public String getAttributeName()
    {
        return attributeName;
    }


    /**
     * Set the value of AttributeName
     *
     * @param v new value
     */
    public void setAttributeName(String v) 
    {

        if (!ObjectUtils.equals(this.attributeName, v))
        {
            this.attributeName = v;
            setModified(true);
        }


    }

    /**
     * Get the AttributeType
     *
     * @return Integer
     */
    public Integer getAttributeType()
    {
        return attributeType;
    }


    /**
     * Set the value of AttributeType
     *
     * @param v new value
     */
    public void setAttributeType(Integer v) 
    {

        if (!ObjectUtils.equals(this.attributeType, v))
        {
            this.attributeType = v;
            setModified(true);
        }


    }

    /**
     * Get the Deleted
     *
     * @return Integer
     */
    public Integer getDeleted()
    {
        return deleted;
    }


    /**
     * Set the value of Deleted
     *
     * @param v new value
     */
    public void setDeleted(Integer v) 
    {

        if (!ObjectUtils.equals(this.deleted, v))
        {
            this.deleted = v;
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
     * Get the Permission
     *
     * @return String
     */
    public String getPermission()
    {
        return permission;
    }


    /**
     * Set the value of Permission
     *
     * @param v new value
     */
    public void setPermission(String v) 
    {

        if (!ObjectUtils.equals(this.permission, v))
        {
            this.permission = v;
            setModified(true);
        }


    }

    /**
     * Get the RequiredOption
     *
     * @return Integer
     */
    public Integer getRequiredOption()
    {
        return requiredOption;
    }


    /**
     * Set the value of RequiredOption
     *
     * @param v new value
     */
    public void setRequiredOption(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.requiredOption, v))
        {
            this.requiredOption = v;
            setModified(true);
        }


        if (aTAttributeOption != null && !ObjectUtils.equals(aTAttributeOption.getObjectID(), v))
        {
            aTAttributeOption = null;
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

    



    private TAttributeOption aTAttributeOption;

    /**
     * Declares an association between this object and a TAttributeOption object
     *
     * @param v TAttributeOption
     * @throws TorqueException
     */
    public void setTAttributeOption(TAttributeOption v) throws TorqueException
    {
        if (v == null)
        {
            setRequiredOption((Integer) null);
        }
        else
        {
            setRequiredOption(v.getObjectID());
        }
        aTAttributeOption = v;
    }


    /**
     * Returns the associated TAttributeOption object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TAttributeOption object
     * @throws TorqueException
     */
    public TAttributeOption getTAttributeOption()
        throws TorqueException
    {
        if (aTAttributeOption == null && (!ObjectUtils.equals(this.requiredOption, null)))
        {
            aTAttributeOption = TAttributeOptionPeer.retrieveByPK(SimpleKey.keyFor(this.requiredOption));
        }
        return aTAttributeOption;
    }

    /**
     * Return the associated TAttributeOption object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TAttributeOption object
     * @throws TorqueException
     */
    public TAttributeOption getTAttributeOption(Connection connection)
        throws TorqueException
    {
        if (aTAttributeOption == null && (!ObjectUtils.equals(this.requiredOption, null)))
        {
            aTAttributeOption = TAttributeOptionPeer.retrieveByPK(SimpleKey.keyFor(this.requiredOption), connection);
        }
        return aTAttributeOption;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTAttributeOptionKey(ObjectKey key) throws TorqueException
    {

        setRequiredOption(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTIssueAttributeValues
     */
    protected List<TIssueAttributeValue> collTIssueAttributeValues;

    /**
     * Temporary storage of collTIssueAttributeValues to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTIssueAttributeValues()
    {
        if (collTIssueAttributeValues == null)
        {
            collTIssueAttributeValues = new ArrayList<TIssueAttributeValue>();
        }
    }


    /**
     * Method called to associate a TIssueAttributeValue object to this object
     * through the TIssueAttributeValue foreign key attribute
     *
     * @param l TIssueAttributeValue
     * @throws TorqueException
     */
    public void addTIssueAttributeValue(TIssueAttributeValue l) throws TorqueException
    {
        getTIssueAttributeValues().add(l);
        l.setTAttribute((TAttribute) this);
    }

    /**
     * Method called to associate a TIssueAttributeValue object to this object
     * through the TIssueAttributeValue foreign key attribute using connection.
     *
     * @param l TIssueAttributeValue
     * @throws TorqueException
     */
    public void addTIssueAttributeValue(TIssueAttributeValue l, Connection con) throws TorqueException
    {
        getTIssueAttributeValues(con).add(l);
        l.setTAttribute((TAttribute) this);
    }

    /**
     * The criteria used to select the current contents of collTIssueAttributeValues
     */
    private Criteria lastTIssueAttributeValuesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTIssueAttributeValues(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TIssueAttributeValue> getTIssueAttributeValues()
        throws TorqueException
    {
        if (collTIssueAttributeValues == null)
        {
            collTIssueAttributeValues = getTIssueAttributeValues(new Criteria(10));
        }
        return collTIssueAttributeValues;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttribute has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     * If this TAttribute is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TIssueAttributeValue> getTIssueAttributeValues(Criteria criteria) throws TorqueException
    {
        if (collTIssueAttributeValues == null)
        {
            if (isNew())
            {
               collTIssueAttributeValues = new ArrayList<TIssueAttributeValue>();
            }
            else
            {
                criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID() );
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelect(criteria);
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
                criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID());
                if (!lastTIssueAttributeValuesCriteria.equals(criteria))
                {
                    collTIssueAttributeValues = TIssueAttributeValuePeer.doSelect(criteria);
                }
            }
        }
        lastTIssueAttributeValuesCriteria = criteria;

        return collTIssueAttributeValues;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTIssueAttributeValues(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TIssueAttributeValue> getTIssueAttributeValues(Connection con) throws TorqueException
    {
        if (collTIssueAttributeValues == null)
        {
            collTIssueAttributeValues = getTIssueAttributeValues(new Criteria(10), con);
        }
        return collTIssueAttributeValues;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttribute has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     * If this TAttribute is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TIssueAttributeValue> getTIssueAttributeValues(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTIssueAttributeValues == null)
        {
            if (isNew())
            {
               collTIssueAttributeValues = new ArrayList<TIssueAttributeValue>();
            }
            else
            {
                 criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID());
                 collTIssueAttributeValues = TIssueAttributeValuePeer.doSelect(criteria, con);
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
                 criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID());
                 if (!lastTIssueAttributeValuesCriteria.equals(criteria))
                 {
                     collTIssueAttributeValues = TIssueAttributeValuePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTIssueAttributeValuesCriteria = criteria;

         return collTIssueAttributeValues;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttribute is new, it will return
     * an empty collection; or if this TAttribute has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttribute.
     */
    protected List<TIssueAttributeValue> getTIssueAttributeValuesJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTIssueAttributeValues == null)
        {
            if (isNew())
            {
               collTIssueAttributeValues = new ArrayList<TIssueAttributeValue>();
            }
            else
            {
                criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID());
            if (!lastTIssueAttributeValuesCriteria.equals(criteria))
            {
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTIssueAttributeValuesCriteria = criteria;

        return collTIssueAttributeValues;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttribute is new, it will return
     * an empty collection; or if this TAttribute has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttribute.
     */
    protected List<TIssueAttributeValue> getTIssueAttributeValuesJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTIssueAttributeValues == null)
        {
            if (isNew())
            {
               collTIssueAttributeValues = new ArrayList<TIssueAttributeValue>();
            }
            else
            {
                criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID());
            if (!lastTIssueAttributeValuesCriteria.equals(criteria))
            {
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTIssueAttributeValuesCriteria = criteria;

        return collTIssueAttributeValues;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttribute is new, it will return
     * an empty collection; or if this TAttribute has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttribute.
     */
    protected List<TIssueAttributeValue> getTIssueAttributeValuesJoinTAttribute(Criteria criteria)
        throws TorqueException
    {
        if (collTIssueAttributeValues == null)
        {
            if (isNew())
            {
               collTIssueAttributeValues = new ArrayList<TIssueAttributeValue>();
            }
            else
            {
                criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTAttribute(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID());
            if (!lastTIssueAttributeValuesCriteria.equals(criteria))
            {
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTAttribute(criteria);
            }
        }
        lastTIssueAttributeValuesCriteria = criteria;

        return collTIssueAttributeValues;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttribute is new, it will return
     * an empty collection; or if this TAttribute has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttribute.
     */
    protected List<TIssueAttributeValue> getTIssueAttributeValuesJoinTAttributeOption(Criteria criteria)
        throws TorqueException
    {
        if (collTIssueAttributeValues == null)
        {
            if (isNew())
            {
               collTIssueAttributeValues = new ArrayList<TIssueAttributeValue>();
            }
            else
            {
                criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTAttributeOption(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.ATTRIBUTEID, getObjectID());
            if (!lastTIssueAttributeValuesCriteria.equals(criteria))
            {
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTAttributeOption(criteria);
            }
        }
        lastTIssueAttributeValuesCriteria = criteria;

        return collTIssueAttributeValues;
    }





    /**
     * Collection to store aggregation of collTAttributeOptions
     */
    protected List<TAttributeOption> collTAttributeOptions;

    /**
     * Temporary storage of collTAttributeOptions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAttributeOptions()
    {
        if (collTAttributeOptions == null)
        {
            collTAttributeOptions = new ArrayList<TAttributeOption>();
        }
    }


    /**
     * Method called to associate a TAttributeOption object to this object
     * through the TAttributeOption foreign key attribute
     *
     * @param l TAttributeOption
     * @throws TorqueException
     */
    public void addTAttributeOption(TAttributeOption l) throws TorqueException
    {
        getTAttributeOptions().add(l);
        l.setTAttribute((TAttribute) this);
    }

    /**
     * Method called to associate a TAttributeOption object to this object
     * through the TAttributeOption foreign key attribute using connection.
     *
     * @param l TAttributeOption
     * @throws TorqueException
     */
    public void addTAttributeOption(TAttributeOption l, Connection con) throws TorqueException
    {
        getTAttributeOptions(con).add(l);
        l.setTAttribute((TAttribute) this);
    }

    /**
     * The criteria used to select the current contents of collTAttributeOptions
     */
    private Criteria lastTAttributeOptionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttributeOptions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAttributeOption> getTAttributeOptions()
        throws TorqueException
    {
        if (collTAttributeOptions == null)
        {
            collTAttributeOptions = getTAttributeOptions(new Criteria(10));
        }
        return collTAttributeOptions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttribute has previously
     * been saved, it will retrieve related TAttributeOptions from storage.
     * If this TAttribute is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAttributeOption> getTAttributeOptions(Criteria criteria) throws TorqueException
    {
        if (collTAttributeOptions == null)
        {
            if (isNew())
            {
               collTAttributeOptions = new ArrayList<TAttributeOption>();
            }
            else
            {
                criteria.add(TAttributeOptionPeer.ATTRIBUTEID, getObjectID() );
                collTAttributeOptions = TAttributeOptionPeer.doSelect(criteria);
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
                criteria.add(TAttributeOptionPeer.ATTRIBUTEID, getObjectID());
                if (!lastTAttributeOptionsCriteria.equals(criteria))
                {
                    collTAttributeOptions = TAttributeOptionPeer.doSelect(criteria);
                }
            }
        }
        lastTAttributeOptionsCriteria = criteria;

        return collTAttributeOptions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttributeOptions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttributeOption> getTAttributeOptions(Connection con) throws TorqueException
    {
        if (collTAttributeOptions == null)
        {
            collTAttributeOptions = getTAttributeOptions(new Criteria(10), con);
        }
        return collTAttributeOptions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttribute has previously
     * been saved, it will retrieve related TAttributeOptions from storage.
     * If this TAttribute is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttributeOption> getTAttributeOptions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAttributeOptions == null)
        {
            if (isNew())
            {
               collTAttributeOptions = new ArrayList<TAttributeOption>();
            }
            else
            {
                 criteria.add(TAttributeOptionPeer.ATTRIBUTEID, getObjectID());
                 collTAttributeOptions = TAttributeOptionPeer.doSelect(criteria, con);
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
                 criteria.add(TAttributeOptionPeer.ATTRIBUTEID, getObjectID());
                 if (!lastTAttributeOptionsCriteria.equals(criteria))
                 {
                     collTAttributeOptions = TAttributeOptionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAttributeOptionsCriteria = criteria;

         return collTAttributeOptions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttribute is new, it will return
     * an empty collection; or if this TAttribute has previously
     * been saved, it will retrieve related TAttributeOptions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttribute.
     */
    protected List<TAttributeOption> getTAttributeOptionsJoinTAttribute(Criteria criteria)
        throws TorqueException
    {
        if (collTAttributeOptions == null)
        {
            if (isNew())
            {
               collTAttributeOptions = new ArrayList<TAttributeOption>();
            }
            else
            {
                criteria.add(TAttributeOptionPeer.ATTRIBUTEID, getObjectID());
                collTAttributeOptions = TAttributeOptionPeer.doSelectJoinTAttribute(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttributeOptionPeer.ATTRIBUTEID, getObjectID());
            if (!lastTAttributeOptionsCriteria.equals(criteria))
            {
                collTAttributeOptions = TAttributeOptionPeer.doSelectJoinTAttribute(criteria);
            }
        }
        lastTAttributeOptionsCriteria = criteria;

        return collTAttributeOptions;
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
            fieldNames.add("AttributeName");
            fieldNames.add("AttributeType");
            fieldNames.add("Deleted");
            fieldNames.add("Description");
            fieldNames.add("Permission");
            fieldNames.add("RequiredOption");
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
        if (name.equals("AttributeName"))
        {
            return getAttributeName();
        }
        if (name.equals("AttributeType"))
        {
            return getAttributeType();
        }
        if (name.equals("Deleted"))
        {
            return getDeleted();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("Permission"))
        {
            return getPermission();
        }
        if (name.equals("RequiredOption"))
        {
            return getRequiredOption();
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
        if (name.equals("AttributeName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAttributeName((String) value);
            return true;
        }
        if (name.equals("AttributeType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAttributeType((Integer) value);
            return true;
        }
        if (name.equals("Deleted"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDeleted((Integer) value);
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
        if (name.equals("Permission"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPermission((String) value);
            return true;
        }
        if (name.equals("RequiredOption"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRequiredOption((Integer) value);
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
        if (name.equals(TAttributePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TAttributePeer.ATTRIBUTENAME))
        {
            return getAttributeName();
        }
        if (name.equals(TAttributePeer.ATTRIBUTETYPE))
        {
            return getAttributeType();
        }
        if (name.equals(TAttributePeer.DELETED))
        {
            return getDeleted();
        }
        if (name.equals(TAttributePeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TAttributePeer.PERMISSION))
        {
            return getPermission();
        }
        if (name.equals(TAttributePeer.REQUIREDOPTION))
        {
            return getRequiredOption();
        }
        if (name.equals(TAttributePeer.TPUUID))
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
      if (TAttributePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TAttributePeer.ATTRIBUTENAME.equals(name))
        {
            return setByName("AttributeName", value);
        }
      if (TAttributePeer.ATTRIBUTETYPE.equals(name))
        {
            return setByName("AttributeType", value);
        }
      if (TAttributePeer.DELETED.equals(name))
        {
            return setByName("Deleted", value);
        }
      if (TAttributePeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TAttributePeer.PERMISSION.equals(name))
        {
            return setByName("Permission", value);
        }
      if (TAttributePeer.REQUIREDOPTION.equals(name))
        {
            return setByName("RequiredOption", value);
        }
      if (TAttributePeer.TPUUID.equals(name))
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
            return getAttributeName();
        }
        if (pos == 2)
        {
            return getAttributeType();
        }
        if (pos == 3)
        {
            return getDeleted();
        }
        if (pos == 4)
        {
            return getDescription();
        }
        if (pos == 5)
        {
            return getPermission();
        }
        if (pos == 6)
        {
            return getRequiredOption();
        }
        if (pos == 7)
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
            return setByName("AttributeName", value);
        }
    if (position == 2)
        {
            return setByName("AttributeType", value);
        }
    if (position == 3)
        {
            return setByName("Deleted", value);
        }
    if (position == 4)
        {
            return setByName("Description", value);
        }
    if (position == 5)
        {
            return setByName("Permission", value);
        }
    if (position == 6)
        {
            return setByName("RequiredOption", value);
        }
    if (position == 7)
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
        save(TAttributePeer.DATABASE_NAME);
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
                    TAttributePeer.doInsert((TAttribute) this, con);
                    setNew(false);
                }
                else
                {
                    TAttributePeer.doUpdate((TAttribute) this, con);
                }
            }


            if (collTIssueAttributeValues != null)
            {
                for (int i = 0; i < collTIssueAttributeValues.size(); i++)
                {
                    ((TIssueAttributeValue) collTIssueAttributeValues.get(i)).save(con);
                }
            }

            if (collTAttributeOptions != null)
            {
                for (int i = 0; i < collTAttributeOptions.size(); i++)
                {
                    ((TAttributeOption) collTAttributeOptions.get(i)).save(con);
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
    public TAttribute copy() throws TorqueException
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
    public TAttribute copy(Connection con) throws TorqueException
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
    public TAttribute copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TAttribute(), deepcopy);
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
    public TAttribute copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TAttribute(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TAttribute copyInto(TAttribute copyObj) throws TorqueException
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
    protected TAttribute copyInto(TAttribute copyObj, Connection con) throws TorqueException
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
    protected TAttribute copyInto(TAttribute copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAttributeName(attributeName);
        copyObj.setAttributeType(attributeType);
        copyObj.setDeleted(deleted);
        copyObj.setDescription(description);
        copyObj.setPermission(permission);
        copyObj.setRequiredOption(requiredOption);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TIssueAttributeValue> vTIssueAttributeValues = getTIssueAttributeValues();
        if (vTIssueAttributeValues != null)
        {
            for (int i = 0; i < vTIssueAttributeValues.size(); i++)
            {
                TIssueAttributeValue obj =  vTIssueAttributeValues.get(i);
                copyObj.addTIssueAttributeValue(obj.copy());
            }
        }
        else
        {
            copyObj.collTIssueAttributeValues = null;
        }


        List<TAttributeOption> vTAttributeOptions = getTAttributeOptions();
        if (vTAttributeOptions != null)
        {
            for (int i = 0; i < vTAttributeOptions.size(); i++)
            {
                TAttributeOption obj =  vTAttributeOptions.get(i);
                copyObj.addTAttributeOption(obj.copy());
            }
        }
        else
        {
            copyObj.collTAttributeOptions = null;
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
    protected TAttribute copyInto(TAttribute copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAttributeName(attributeName);
        copyObj.setAttributeType(attributeType);
        copyObj.setDeleted(deleted);
        copyObj.setDescription(description);
        copyObj.setPermission(permission);
        copyObj.setRequiredOption(requiredOption);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TIssueAttributeValue> vTIssueAttributeValues = getTIssueAttributeValues(con);
        if (vTIssueAttributeValues != null)
        {
            for (int i = 0; i < vTIssueAttributeValues.size(); i++)
            {
                TIssueAttributeValue obj =  vTIssueAttributeValues.get(i);
                copyObj.addTIssueAttributeValue(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTIssueAttributeValues = null;
        }


        List<TAttributeOption> vTAttributeOptions = getTAttributeOptions(con);
        if (vTAttributeOptions != null)
        {
            for (int i = 0; i < vTAttributeOptions.size(); i++)
            {
                TAttributeOption obj =  vTAttributeOptions.get(i);
                copyObj.addTAttributeOption(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAttributeOptions = null;
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
    public TAttributePeer getPeer()
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
        return TAttributePeer.getTableMap();
    }

  
    /**
     * Creates a TAttributeBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TAttributeBean with the contents of this object
     */
    public TAttributeBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TAttributeBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TAttributeBean with the contents of this object
     */
    public TAttributeBean getBean(IdentityMap createdBeans)
    {
        TAttributeBean result = (TAttributeBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TAttributeBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setAttributeName(getAttributeName());
        result.setAttributeType(getAttributeType());
        result.setDeleted(getDeleted());
        result.setDescription(getDescription());
        result.setPermission(getPermission());
        result.setRequiredOption(getRequiredOption());
        result.setUuid(getUuid());



        if (collTIssueAttributeValues != null)
        {
            List<TIssueAttributeValueBean> relatedBeans = new ArrayList<TIssueAttributeValueBean>(collTIssueAttributeValues.size());
            for (Iterator<TIssueAttributeValue> collTIssueAttributeValuesIt = collTIssueAttributeValues.iterator(); collTIssueAttributeValuesIt.hasNext(); )
            {
                TIssueAttributeValue related = (TIssueAttributeValue) collTIssueAttributeValuesIt.next();
                TIssueAttributeValueBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTIssueAttributeValueBeans(relatedBeans);
        }


        if (collTAttributeOptions != null)
        {
            List<TAttributeOptionBean> relatedBeans = new ArrayList<TAttributeOptionBean>(collTAttributeOptions.size());
            for (Iterator<TAttributeOption> collTAttributeOptionsIt = collTAttributeOptions.iterator(); collTAttributeOptionsIt.hasNext(); )
            {
                TAttributeOption related = (TAttributeOption) collTAttributeOptionsIt.next();
                TAttributeOptionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAttributeOptionBeans(relatedBeans);
        }




        if (aTAttributeOption != null)
        {
            TAttributeOptionBean relatedBean = aTAttributeOption.getBean(createdBeans);
            result.setTAttributeOptionBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TAttribute with the contents
     * of a TAttributeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TAttributeBean which contents are used to create
     *        the resulting class
     * @return an instance of TAttribute with the contents of bean
     */
    public static TAttribute createTAttribute(TAttributeBean bean)
        throws TorqueException
    {
        return createTAttribute(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TAttribute with the contents
     * of a TAttributeBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TAttributeBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TAttribute with the contents of bean
     */

    public static TAttribute createTAttribute(TAttributeBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TAttribute result = (TAttribute) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TAttribute();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setAttributeName(bean.getAttributeName());
        result.setAttributeType(bean.getAttributeType());
        result.setDeleted(bean.getDeleted());
        result.setDescription(bean.getDescription());
        result.setPermission(bean.getPermission());
        result.setRequiredOption(bean.getRequiredOption());
        result.setUuid(bean.getUuid());



        {
            List<TIssueAttributeValueBean> relatedBeans = bean.getTIssueAttributeValueBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TIssueAttributeValueBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TIssueAttributeValueBean relatedBean =  relatedBeansIt.next();
                    TIssueAttributeValue related = TIssueAttributeValue.createTIssueAttributeValue(relatedBean, createdObjects);
                    result.addTIssueAttributeValueFromBean(related);
                }
            }
        }


        {
            List<TAttributeOptionBean> relatedBeans = bean.getTAttributeOptionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAttributeOptionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAttributeOptionBean relatedBean =  relatedBeansIt.next();
                    TAttributeOption related = TAttributeOption.createTAttributeOption(relatedBean, createdObjects);
                    result.addTAttributeOptionFromBean(related);
                }
            }
        }




        {
            TAttributeOptionBean relatedBean = bean.getTAttributeOptionBean();
            if (relatedBean != null)
            {
                TAttributeOption relatedObject = TAttributeOption.createTAttributeOption(relatedBean, createdObjects);
                result.setTAttributeOption(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TIssueAttributeValue object to this object.
     * through the TIssueAttributeValue foreign key attribute
     *
     * @param toAdd TIssueAttributeValue
     */
    protected void addTIssueAttributeValueFromBean(TIssueAttributeValue toAdd)
    {
        initTIssueAttributeValues();
        collTIssueAttributeValues.add(toAdd);
    }


    /**
     * Method called to associate a TAttributeOption object to this object.
     * through the TAttributeOption foreign key attribute
     *
     * @param toAdd TAttributeOption
     */
    protected void addTAttributeOptionFromBean(TAttributeOption toAdd)
    {
        initTAttributeOptions();
        collTAttributeOptions.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TAttribute:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("AttributeName = ")
           .append(getAttributeName())
           .append("\n");
        str.append("AttributeType = ")
           .append(getAttributeType())
           .append("\n");
        str.append("Deleted = ")
           .append(getDeleted())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Permission = ")
           .append(getPermission())
           .append("\n");
        str.append("RequiredOption = ")
           .append(getRequiredOption())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
