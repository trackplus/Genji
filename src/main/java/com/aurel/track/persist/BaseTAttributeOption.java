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



import com.aurel.track.persist.TAttribute;
import com.aurel.track.persist.TAttributePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TAttributeOptionBean;
import com.aurel.track.beans.TAttributeBean;

import com.aurel.track.beans.TIssueAttributeValueBean;
import com.aurel.track.beans.TAttributeBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TAttributeOption
 */
public abstract class BaseTAttributeOption extends TpBaseObject
{
    /** The Peer class */
    private static final TAttributeOptionPeer peer =
        new TAttributeOptionPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the attributeID field */
    private Integer attributeID;

    /** The value for the parentOption field */
    private Integer parentOption;

    /** The value for the optionName field */
    private String optionName;

    /** The value for the deleted field */
    private Integer deleted;

    /** The value for the sortorder field */
    private Integer sortorder;

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
                        .setOptionID(v);
            }
        }

        // update associated TAttribute
        if (collTAttributes != null)
        {
            for (int i = 0; i < collTAttributes.size(); i++)
            {
                ((TAttribute) collTAttributes.get(i))
                        .setRequiredOption(v);
            }
        }
    }

    /**
     * Get the AttributeID
     *
     * @return Integer
     */
    public Integer getAttributeID()
    {
        return attributeID;
    }


    /**
     * Set the value of AttributeID
     *
     * @param v new value
     */
    public void setAttributeID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.attributeID, v))
        {
            this.attributeID = v;
            setModified(true);
        }


        if (aTAttribute != null && !ObjectUtils.equals(aTAttribute.getObjectID(), v))
        {
            aTAttribute = null;
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
     * Get the OptionName
     *
     * @return String
     */
    public String getOptionName()
    {
        return optionName;
    }


    /**
     * Set the value of OptionName
     *
     * @param v new value
     */
    public void setOptionName(String v) 
    {

        if (!ObjectUtils.equals(this.optionName, v))
        {
            this.optionName = v;
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

    



    private TAttribute aTAttribute;

    /**
     * Declares an association between this object and a TAttribute object
     *
     * @param v TAttribute
     * @throws TorqueException
     */
    public void setTAttribute(TAttribute v) throws TorqueException
    {
        if (v == null)
        {
            setAttributeID((Integer) null);
        }
        else
        {
            setAttributeID(v.getObjectID());
        }
        aTAttribute = v;
    }


    /**
     * Returns the associated TAttribute object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TAttribute object
     * @throws TorqueException
     */
    public TAttribute getTAttribute()
        throws TorqueException
    {
        if (aTAttribute == null && (!ObjectUtils.equals(this.attributeID, null)))
        {
            aTAttribute = TAttributePeer.retrieveByPK(SimpleKey.keyFor(this.attributeID));
        }
        return aTAttribute;
    }

    /**
     * Return the associated TAttribute object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TAttribute object
     * @throws TorqueException
     */
    public TAttribute getTAttribute(Connection connection)
        throws TorqueException
    {
        if (aTAttribute == null && (!ObjectUtils.equals(this.attributeID, null)))
        {
            aTAttribute = TAttributePeer.retrieveByPK(SimpleKey.keyFor(this.attributeID), connection);
        }
        return aTAttribute;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTAttributeKey(ObjectKey key) throws TorqueException
    {

        setAttributeID(new Integer(((NumberKey) key).intValue()));
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
        l.setTAttributeOption((TAttributeOption) this);
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
        l.setTAttributeOption((TAttributeOption) this);
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
     * Otherwise if this TAttributeOption has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     * If this TAttributeOption is new, it will return
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
                criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID() );
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
                criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID());
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
     * Otherwise if this TAttributeOption has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     * If this TAttributeOption is new, it will return
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
                 criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID());
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
                 criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID());
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
     * Otherwise if this TAttributeOption is new, it will return
     * an empty collection; or if this TAttributeOption has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttributeOption.
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
                criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID());
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
     * Otherwise if this TAttributeOption is new, it will return
     * an empty collection; or if this TAttributeOption has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttributeOption.
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
                criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID());
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
     * Otherwise if this TAttributeOption is new, it will return
     * an empty collection; or if this TAttributeOption has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttributeOption.
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
                criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTAttribute(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID());
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
     * Otherwise if this TAttributeOption is new, it will return
     * an empty collection; or if this TAttributeOption has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttributeOption.
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
                criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTAttributeOption(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.OPTIONID, getObjectID());
            if (!lastTIssueAttributeValuesCriteria.equals(criteria))
            {
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTAttributeOption(criteria);
            }
        }
        lastTIssueAttributeValuesCriteria = criteria;

        return collTIssueAttributeValues;
    }





    /**
     * Collection to store aggregation of collTAttributes
     */
    protected List<TAttribute> collTAttributes;

    /**
     * Temporary storage of collTAttributes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAttributes()
    {
        if (collTAttributes == null)
        {
            collTAttributes = new ArrayList<TAttribute>();
        }
    }


    /**
     * Method called to associate a TAttribute object to this object
     * through the TAttribute foreign key attribute
     *
     * @param l TAttribute
     * @throws TorqueException
     */
    public void addTAttribute(TAttribute l) throws TorqueException
    {
        getTAttributes().add(l);
        l.setTAttributeOption((TAttributeOption) this);
    }

    /**
     * Method called to associate a TAttribute object to this object
     * through the TAttribute foreign key attribute using connection.
     *
     * @param l TAttribute
     * @throws TorqueException
     */
    public void addTAttribute(TAttribute l, Connection con) throws TorqueException
    {
        getTAttributes(con).add(l);
        l.setTAttributeOption((TAttributeOption) this);
    }

    /**
     * The criteria used to select the current contents of collTAttributes
     */
    private Criteria lastTAttributesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttributes(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAttribute> getTAttributes()
        throws TorqueException
    {
        if (collTAttributes == null)
        {
            collTAttributes = getTAttributes(new Criteria(10));
        }
        return collTAttributes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttributeOption has previously
     * been saved, it will retrieve related TAttributes from storage.
     * If this TAttributeOption is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAttribute> getTAttributes(Criteria criteria) throws TorqueException
    {
        if (collTAttributes == null)
        {
            if (isNew())
            {
               collTAttributes = new ArrayList<TAttribute>();
            }
            else
            {
                criteria.add(TAttributePeer.REQUIREDOPTION, getObjectID() );
                collTAttributes = TAttributePeer.doSelect(criteria);
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
                criteria.add(TAttributePeer.REQUIREDOPTION, getObjectID());
                if (!lastTAttributesCriteria.equals(criteria))
                {
                    collTAttributes = TAttributePeer.doSelect(criteria);
                }
            }
        }
        lastTAttributesCriteria = criteria;

        return collTAttributes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttributes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttribute> getTAttributes(Connection con) throws TorqueException
    {
        if (collTAttributes == null)
        {
            collTAttributes = getTAttributes(new Criteria(10), con);
        }
        return collTAttributes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttributeOption has previously
     * been saved, it will retrieve related TAttributes from storage.
     * If this TAttributeOption is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttribute> getTAttributes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAttributes == null)
        {
            if (isNew())
            {
               collTAttributes = new ArrayList<TAttribute>();
            }
            else
            {
                 criteria.add(TAttributePeer.REQUIREDOPTION, getObjectID());
                 collTAttributes = TAttributePeer.doSelect(criteria, con);
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
                 criteria.add(TAttributePeer.REQUIREDOPTION, getObjectID());
                 if (!lastTAttributesCriteria.equals(criteria))
                 {
                     collTAttributes = TAttributePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAttributesCriteria = criteria;

         return collTAttributes;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TAttributeOption is new, it will return
     * an empty collection; or if this TAttributeOption has previously
     * been saved, it will retrieve related TAttributes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TAttributeOption.
     */
    protected List<TAttribute> getTAttributesJoinTAttributeOption(Criteria criteria)
        throws TorqueException
    {
        if (collTAttributes == null)
        {
            if (isNew())
            {
               collTAttributes = new ArrayList<TAttribute>();
            }
            else
            {
                criteria.add(TAttributePeer.REQUIREDOPTION, getObjectID());
                collTAttributes = TAttributePeer.doSelectJoinTAttributeOption(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttributePeer.REQUIREDOPTION, getObjectID());
            if (!lastTAttributesCriteria.equals(criteria))
            {
                collTAttributes = TAttributePeer.doSelectJoinTAttributeOption(criteria);
            }
        }
        lastTAttributesCriteria = criteria;

        return collTAttributes;
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
            fieldNames.add("AttributeID");
            fieldNames.add("ParentOption");
            fieldNames.add("OptionName");
            fieldNames.add("Deleted");
            fieldNames.add("Sortorder");
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
        if (name.equals("AttributeID"))
        {
            return getAttributeID();
        }
        if (name.equals("ParentOption"))
        {
            return getParentOption();
        }
        if (name.equals("OptionName"))
        {
            return getOptionName();
        }
        if (name.equals("Deleted"))
        {
            return getDeleted();
        }
        if (name.equals("Sortorder"))
        {
            return getSortorder();
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
        if (name.equals("AttributeID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAttributeID((Integer) value);
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
        if (name.equals("OptionName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOptionName((String) value);
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
        if (name.equals(TAttributeOptionPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TAttributeOptionPeer.ATTRIBUTEID))
        {
            return getAttributeID();
        }
        if (name.equals(TAttributeOptionPeer.PARENTOPTION))
        {
            return getParentOption();
        }
        if (name.equals(TAttributeOptionPeer.OPTIONNAME))
        {
            return getOptionName();
        }
        if (name.equals(TAttributeOptionPeer.DELETED))
        {
            return getDeleted();
        }
        if (name.equals(TAttributeOptionPeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TAttributeOptionPeer.TPUUID))
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
      if (TAttributeOptionPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TAttributeOptionPeer.ATTRIBUTEID.equals(name))
        {
            return setByName("AttributeID", value);
        }
      if (TAttributeOptionPeer.PARENTOPTION.equals(name))
        {
            return setByName("ParentOption", value);
        }
      if (TAttributeOptionPeer.OPTIONNAME.equals(name))
        {
            return setByName("OptionName", value);
        }
      if (TAttributeOptionPeer.DELETED.equals(name))
        {
            return setByName("Deleted", value);
        }
      if (TAttributeOptionPeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TAttributeOptionPeer.TPUUID.equals(name))
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
            return getAttributeID();
        }
        if (pos == 2)
        {
            return getParentOption();
        }
        if (pos == 3)
        {
            return getOptionName();
        }
        if (pos == 4)
        {
            return getDeleted();
        }
        if (pos == 5)
        {
            return getSortorder();
        }
        if (pos == 6)
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
            return setByName("AttributeID", value);
        }
    if (position == 2)
        {
            return setByName("ParentOption", value);
        }
    if (position == 3)
        {
            return setByName("OptionName", value);
        }
    if (position == 4)
        {
            return setByName("Deleted", value);
        }
    if (position == 5)
        {
            return setByName("Sortorder", value);
        }
    if (position == 6)
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
        save(TAttributeOptionPeer.DATABASE_NAME);
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
                    TAttributeOptionPeer.doInsert((TAttributeOption) this, con);
                    setNew(false);
                }
                else
                {
                    TAttributeOptionPeer.doUpdate((TAttributeOption) this, con);
                }
            }


            if (collTIssueAttributeValues != null)
            {
                for (int i = 0; i < collTIssueAttributeValues.size(); i++)
                {
                    ((TIssueAttributeValue) collTIssueAttributeValues.get(i)).save(con);
                }
            }

            if (collTAttributes != null)
            {
                for (int i = 0; i < collTAttributes.size(); i++)
                {
                    ((TAttribute) collTAttributes.get(i)).save(con);
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
    public TAttributeOption copy() throws TorqueException
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
    public TAttributeOption copy(Connection con) throws TorqueException
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
    public TAttributeOption copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TAttributeOption(), deepcopy);
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
    public TAttributeOption copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TAttributeOption(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TAttributeOption copyInto(TAttributeOption copyObj) throws TorqueException
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
    protected TAttributeOption copyInto(TAttributeOption copyObj, Connection con) throws TorqueException
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
    protected TAttributeOption copyInto(TAttributeOption copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAttributeID(attributeID);
        copyObj.setParentOption(parentOption);
        copyObj.setOptionName(optionName);
        copyObj.setDeleted(deleted);
        copyObj.setSortorder(sortorder);
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


        List<TAttribute> vTAttributes = getTAttributes();
        if (vTAttributes != null)
        {
            for (int i = 0; i < vTAttributes.size(); i++)
            {
                TAttribute obj =  vTAttributes.get(i);
                copyObj.addTAttribute(obj.copy());
            }
        }
        else
        {
            copyObj.collTAttributes = null;
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
    protected TAttributeOption copyInto(TAttributeOption copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setAttributeID(attributeID);
        copyObj.setParentOption(parentOption);
        copyObj.setOptionName(optionName);
        copyObj.setDeleted(deleted);
        copyObj.setSortorder(sortorder);
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


        List<TAttribute> vTAttributes = getTAttributes(con);
        if (vTAttributes != null)
        {
            for (int i = 0; i < vTAttributes.size(); i++)
            {
                TAttribute obj =  vTAttributes.get(i);
                copyObj.addTAttribute(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAttributes = null;
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
    public TAttributeOptionPeer getPeer()
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
        return TAttributeOptionPeer.getTableMap();
    }

  
    /**
     * Creates a TAttributeOptionBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TAttributeOptionBean with the contents of this object
     */
    public TAttributeOptionBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TAttributeOptionBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TAttributeOptionBean with the contents of this object
     */
    public TAttributeOptionBean getBean(IdentityMap createdBeans)
    {
        TAttributeOptionBean result = (TAttributeOptionBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TAttributeOptionBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setAttributeID(getAttributeID());
        result.setParentOption(getParentOption());
        result.setOptionName(getOptionName());
        result.setDeleted(getDeleted());
        result.setSortorder(getSortorder());
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


        if (collTAttributes != null)
        {
            List<TAttributeBean> relatedBeans = new ArrayList<TAttributeBean>(collTAttributes.size());
            for (Iterator<TAttribute> collTAttributesIt = collTAttributes.iterator(); collTAttributesIt.hasNext(); )
            {
                TAttribute related = (TAttribute) collTAttributesIt.next();
                TAttributeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAttributeBeans(relatedBeans);
        }




        if (aTAttribute != null)
        {
            TAttributeBean relatedBean = aTAttribute.getBean(createdBeans);
            result.setTAttributeBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TAttributeOption with the contents
     * of a TAttributeOptionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TAttributeOptionBean which contents are used to create
     *        the resulting class
     * @return an instance of TAttributeOption with the contents of bean
     */
    public static TAttributeOption createTAttributeOption(TAttributeOptionBean bean)
        throws TorqueException
    {
        return createTAttributeOption(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TAttributeOption with the contents
     * of a TAttributeOptionBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TAttributeOptionBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TAttributeOption with the contents of bean
     */

    public static TAttributeOption createTAttributeOption(TAttributeOptionBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TAttributeOption result = (TAttributeOption) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TAttributeOption();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setAttributeID(bean.getAttributeID());
        result.setParentOption(bean.getParentOption());
        result.setOptionName(bean.getOptionName());
        result.setDeleted(bean.getDeleted());
        result.setSortorder(bean.getSortorder());
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
            List<TAttributeBean> relatedBeans = bean.getTAttributeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAttributeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAttributeBean relatedBean =  relatedBeansIt.next();
                    TAttribute related = TAttribute.createTAttribute(relatedBean, createdObjects);
                    result.addTAttributeFromBean(related);
                }
            }
        }




        {
            TAttributeBean relatedBean = bean.getTAttributeBean();
            if (relatedBean != null)
            {
                TAttribute relatedObject = TAttribute.createTAttribute(relatedBean, createdObjects);
                result.setTAttribute(relatedObject);
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
     * Method called to associate a TAttribute object to this object.
     * through the TAttribute foreign key attribute
     *
     * @param toAdd TAttribute
     */
    protected void addTAttributeFromBean(TAttribute toAdd)
    {
        initTAttributes();
        collTAttributes.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TAttributeOption:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("AttributeID = ")
           .append(getAttributeID())
           .append("\n");
        str.append("ParentOption = ")
           .append(getParentOption())
           .append("\n");
        str.append("OptionName = ")
           .append(getOptionName())
           .append("\n");
        str.append("Deleted = ")
           .append(getDeleted())
           .append("\n");
        str.append("Sortorder = ")
           .append(getSortorder())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
