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
import com.aurel.track.beans.TRecurrencePatternBean;

import com.aurel.track.beans.TReportSubscribeBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TRecurrencePattern
 */
public abstract class BaseTRecurrencePattern extends TpBaseObject
{
    /** The Peer class */
    private static final TRecurrencePatternPeer peer =
        new TRecurrencePatternPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the recurrencePeriod field */
    private Integer recurrencePeriod;

    /** The value for the param1 field */
    private Integer param1;

    /** The value for the param2 field */
    private Integer param2;

    /** The value for the param3 field */
    private Integer param3;

    /** The value for the days field */
    private String days;

    /** The value for the dateIsAbsolute field */
    private String dateIsAbsolute = "Y";

    /** The value for the startDate field */
    private Date startDate;

    /** The value for the endDate field */
    private Date endDate;

    /** The value for the occurenceType field */
    private Integer occurenceType;

    /** The value for the noOfOccurences field */
    private Integer noOfOccurences;

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



        // update associated TReportSubscribe
        if (collTReportSubscribes != null)
        {
            for (int i = 0; i < collTReportSubscribes.size(); i++)
            {
                ((TReportSubscribe) collTReportSubscribes.get(i))
                        .setRecurrencePattern(v);
            }
        }
    }

    /**
     * Get the RecurrencePeriod
     *
     * @return Integer
     */
    public Integer getRecurrencePeriod()
    {
        return recurrencePeriod;
    }


    /**
     * Set the value of RecurrencePeriod
     *
     * @param v new value
     */
    public void setRecurrencePeriod(Integer v) 
    {

        if (!ObjectUtils.equals(this.recurrencePeriod, v))
        {
            this.recurrencePeriod = v;
            setModified(true);
        }


    }

    /**
     * Get the Param1
     *
     * @return Integer
     */
    public Integer getParam1()
    {
        return param1;
    }


    /**
     * Set the value of Param1
     *
     * @param v new value
     */
    public void setParam1(Integer v) 
    {

        if (!ObjectUtils.equals(this.param1, v))
        {
            this.param1 = v;
            setModified(true);
        }


    }

    /**
     * Get the Param2
     *
     * @return Integer
     */
    public Integer getParam2()
    {
        return param2;
    }


    /**
     * Set the value of Param2
     *
     * @param v new value
     */
    public void setParam2(Integer v) 
    {

        if (!ObjectUtils.equals(this.param2, v))
        {
            this.param2 = v;
            setModified(true);
        }


    }

    /**
     * Get the Param3
     *
     * @return Integer
     */
    public Integer getParam3()
    {
        return param3;
    }


    /**
     * Set the value of Param3
     *
     * @param v new value
     */
    public void setParam3(Integer v) 
    {

        if (!ObjectUtils.equals(this.param3, v))
        {
            this.param3 = v;
            setModified(true);
        }


    }

    /**
     * Get the Days
     *
     * @return String
     */
    public String getDays()
    {
        return days;
    }


    /**
     * Set the value of Days
     *
     * @param v new value
     */
    public void setDays(String v) 
    {

        if (!ObjectUtils.equals(this.days, v))
        {
            this.days = v;
            setModified(true);
        }


    }

    /**
     * Get the DateIsAbsolute
     *
     * @return String
     */
    public String getDateIsAbsolute()
    {
        return dateIsAbsolute;
    }


    /**
     * Set the value of DateIsAbsolute
     *
     * @param v new value
     */
    public void setDateIsAbsolute(String v) 
    {

        if (!ObjectUtils.equals(this.dateIsAbsolute, v))
        {
            this.dateIsAbsolute = v;
            setModified(true);
        }


    }

    /**
     * Get the StartDate
     *
     * @return Date
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Set the value of StartDate
     *
     * @param v new value
     */
    public void setStartDate(Date v) 
    {

        if (!ObjectUtils.equals(this.startDate, v))
        {
            this.startDate = v;
            setModified(true);
        }


    }

    /**
     * Get the EndDate
     *
     * @return Date
     */
    public Date getEndDate()
    {
        return endDate;
    }


    /**
     * Set the value of EndDate
     *
     * @param v new value
     */
    public void setEndDate(Date v) 
    {

        if (!ObjectUtils.equals(this.endDate, v))
        {
            this.endDate = v;
            setModified(true);
        }


    }

    /**
     * Get the OccurenceType
     *
     * @return Integer
     */
    public Integer getOccurenceType()
    {
        return occurenceType;
    }


    /**
     * Set the value of OccurenceType
     *
     * @param v new value
     */
    public void setOccurenceType(Integer v) 
    {

        if (!ObjectUtils.equals(this.occurenceType, v))
        {
            this.occurenceType = v;
            setModified(true);
        }


    }

    /**
     * Get the NoOfOccurences
     *
     * @return Integer
     */
    public Integer getNoOfOccurences()
    {
        return noOfOccurences;
    }


    /**
     * Set the value of NoOfOccurences
     *
     * @param v new value
     */
    public void setNoOfOccurences(Integer v) 
    {

        if (!ObjectUtils.equals(this.noOfOccurences, v))
        {
            this.noOfOccurences = v;
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
     * Collection to store aggregation of collTReportSubscribes
     */
    protected List<TReportSubscribe> collTReportSubscribes;

    /**
     * Temporary storage of collTReportSubscribes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTReportSubscribes()
    {
        if (collTReportSubscribes == null)
        {
            collTReportSubscribes = new ArrayList<TReportSubscribe>();
        }
    }


    /**
     * Method called to associate a TReportSubscribe object to this object
     * through the TReportSubscribe foreign key attribute
     *
     * @param l TReportSubscribe
     * @throws TorqueException
     */
    public void addTReportSubscribe(TReportSubscribe l) throws TorqueException
    {
        getTReportSubscribes().add(l);
        l.setTRecurrencePattern((TRecurrencePattern) this);
    }

    /**
     * Method called to associate a TReportSubscribe object to this object
     * through the TReportSubscribe foreign key attribute using connection.
     *
     * @param l TReportSubscribe
     * @throws TorqueException
     */
    public void addTReportSubscribe(TReportSubscribe l, Connection con) throws TorqueException
    {
        getTReportSubscribes(con).add(l);
        l.setTRecurrencePattern((TRecurrencePattern) this);
    }

    /**
     * The criteria used to select the current contents of collTReportSubscribes
     */
    private Criteria lastTReportSubscribesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportSubscribes(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TReportSubscribe> getTReportSubscribes()
        throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            collTReportSubscribes = getTReportSubscribes(new Criteria(10));
        }
        return collTReportSubscribes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRecurrencePattern has previously
     * been saved, it will retrieve related TReportSubscribes from storage.
     * If this TRecurrencePattern is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TReportSubscribe> getTReportSubscribes(Criteria criteria) throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            if (isNew())
            {
               collTReportSubscribes = new ArrayList<TReportSubscribe>();
            }
            else
            {
                criteria.add(TReportSubscribePeer.RECURRENCEPATTERN, getObjectID() );
                collTReportSubscribes = TReportSubscribePeer.doSelect(criteria);
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
                criteria.add(TReportSubscribePeer.RECURRENCEPATTERN, getObjectID());
                if (!lastTReportSubscribesCriteria.equals(criteria))
                {
                    collTReportSubscribes = TReportSubscribePeer.doSelect(criteria);
                }
            }
        }
        lastTReportSubscribesCriteria = criteria;

        return collTReportSubscribes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportSubscribes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportSubscribe> getTReportSubscribes(Connection con) throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            collTReportSubscribes = getTReportSubscribes(new Criteria(10), con);
        }
        return collTReportSubscribes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRecurrencePattern has previously
     * been saved, it will retrieve related TReportSubscribes from storage.
     * If this TRecurrencePattern is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportSubscribe> getTReportSubscribes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            if (isNew())
            {
               collTReportSubscribes = new ArrayList<TReportSubscribe>();
            }
            else
            {
                 criteria.add(TReportSubscribePeer.RECURRENCEPATTERN, getObjectID());
                 collTReportSubscribes = TReportSubscribePeer.doSelect(criteria, con);
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
                 criteria.add(TReportSubscribePeer.RECURRENCEPATTERN, getObjectID());
                 if (!lastTReportSubscribesCriteria.equals(criteria))
                 {
                     collTReportSubscribes = TReportSubscribePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTReportSubscribesCriteria = criteria;

         return collTReportSubscribes;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRecurrencePattern is new, it will return
     * an empty collection; or if this TRecurrencePattern has previously
     * been saved, it will retrieve related TReportSubscribes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRecurrencePattern.
     */
    protected List<TReportSubscribe> getTReportSubscribesJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            if (isNew())
            {
               collTReportSubscribes = new ArrayList<TReportSubscribe>();
            }
            else
            {
                criteria.add(TReportSubscribePeer.RECURRENCEPATTERN, getObjectID());
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportSubscribePeer.RECURRENCEPATTERN, getObjectID());
            if (!lastTReportSubscribesCriteria.equals(criteria))
            {
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTReportSubscribesCriteria = criteria;

        return collTReportSubscribes;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRecurrencePattern is new, it will return
     * an empty collection; or if this TRecurrencePattern has previously
     * been saved, it will retrieve related TReportSubscribes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRecurrencePattern.
     */
    protected List<TReportSubscribe> getTReportSubscribesJoinTRecurrencePattern(Criteria criteria)
        throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            if (isNew())
            {
               collTReportSubscribes = new ArrayList<TReportSubscribe>();
            }
            else
            {
                criteria.add(TReportSubscribePeer.RECURRENCEPATTERN, getObjectID());
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTRecurrencePattern(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportSubscribePeer.RECURRENCEPATTERN, getObjectID());
            if (!lastTReportSubscribesCriteria.equals(criteria))
            {
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTRecurrencePattern(criteria);
            }
        }
        lastTReportSubscribesCriteria = criteria;

        return collTReportSubscribes;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TRecurrencePattern is new, it will return
     * an empty collection; or if this TRecurrencePattern has previously
     * been saved, it will retrieve related TReportSubscribes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TRecurrencePattern.
     */
    protected List<TReportSubscribe> getTReportSubscribesJoinTExportTemplate(Criteria criteria)
        throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            if (isNew())
            {
               collTReportSubscribes = new ArrayList<TReportSubscribe>();
            }
            else
            {
                criteria.add(TReportSubscribePeer.RECURRENCEPATTERN, getObjectID());
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTExportTemplate(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportSubscribePeer.RECURRENCEPATTERN, getObjectID());
            if (!lastTReportSubscribesCriteria.equals(criteria))
            {
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTExportTemplate(criteria);
            }
        }
        lastTReportSubscribesCriteria = criteria;

        return collTReportSubscribes;
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
            fieldNames.add("RecurrencePeriod");
            fieldNames.add("Param1");
            fieldNames.add("Param2");
            fieldNames.add("Param3");
            fieldNames.add("Days");
            fieldNames.add("DateIsAbsolute");
            fieldNames.add("StartDate");
            fieldNames.add("EndDate");
            fieldNames.add("OccurenceType");
            fieldNames.add("NoOfOccurences");
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
        if (name.equals("RecurrencePeriod"))
        {
            return getRecurrencePeriod();
        }
        if (name.equals("Param1"))
        {
            return getParam1();
        }
        if (name.equals("Param2"))
        {
            return getParam2();
        }
        if (name.equals("Param3"))
        {
            return getParam3();
        }
        if (name.equals("Days"))
        {
            return getDays();
        }
        if (name.equals("DateIsAbsolute"))
        {
            return getDateIsAbsolute();
        }
        if (name.equals("StartDate"))
        {
            return getStartDate();
        }
        if (name.equals("EndDate"))
        {
            return getEndDate();
        }
        if (name.equals("OccurenceType"))
        {
            return getOccurenceType();
        }
        if (name.equals("NoOfOccurences"))
        {
            return getNoOfOccurences();
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
        if (name.equals("RecurrencePeriod"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRecurrencePeriod((Integer) value);
            return true;
        }
        if (name.equals("Param1"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParam1((Integer) value);
            return true;
        }
        if (name.equals("Param2"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParam2((Integer) value);
            return true;
        }
        if (name.equals("Param3"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParam3((Integer) value);
            return true;
        }
        if (name.equals("Days"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDays((String) value);
            return true;
        }
        if (name.equals("DateIsAbsolute"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDateIsAbsolute((String) value);
            return true;
        }
        if (name.equals("StartDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStartDate((Date) value);
            return true;
        }
        if (name.equals("EndDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEndDate((Date) value);
            return true;
        }
        if (name.equals("OccurenceType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOccurenceType((Integer) value);
            return true;
        }
        if (name.equals("NoOfOccurences"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNoOfOccurences((Integer) value);
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
        if (name.equals(TRecurrencePatternPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TRecurrencePatternPeer.RECURRENCEPERIOD))
        {
            return getRecurrencePeriod();
        }
        if (name.equals(TRecurrencePatternPeer.PARAM1))
        {
            return getParam1();
        }
        if (name.equals(TRecurrencePatternPeer.PARAM2))
        {
            return getParam2();
        }
        if (name.equals(TRecurrencePatternPeer.PARAM3))
        {
            return getParam3();
        }
        if (name.equals(TRecurrencePatternPeer.DAYS))
        {
            return getDays();
        }
        if (name.equals(TRecurrencePatternPeer.DATEISABSOLUTE))
        {
            return getDateIsAbsolute();
        }
        if (name.equals(TRecurrencePatternPeer.STARTDATE))
        {
            return getStartDate();
        }
        if (name.equals(TRecurrencePatternPeer.ENDDATE))
        {
            return getEndDate();
        }
        if (name.equals(TRecurrencePatternPeer.OCCURENCETYPE))
        {
            return getOccurenceType();
        }
        if (name.equals(TRecurrencePatternPeer.NOOFOCCURENCES))
        {
            return getNoOfOccurences();
        }
        if (name.equals(TRecurrencePatternPeer.TPUUID))
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
      if (TRecurrencePatternPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TRecurrencePatternPeer.RECURRENCEPERIOD.equals(name))
        {
            return setByName("RecurrencePeriod", value);
        }
      if (TRecurrencePatternPeer.PARAM1.equals(name))
        {
            return setByName("Param1", value);
        }
      if (TRecurrencePatternPeer.PARAM2.equals(name))
        {
            return setByName("Param2", value);
        }
      if (TRecurrencePatternPeer.PARAM3.equals(name))
        {
            return setByName("Param3", value);
        }
      if (TRecurrencePatternPeer.DAYS.equals(name))
        {
            return setByName("Days", value);
        }
      if (TRecurrencePatternPeer.DATEISABSOLUTE.equals(name))
        {
            return setByName("DateIsAbsolute", value);
        }
      if (TRecurrencePatternPeer.STARTDATE.equals(name))
        {
            return setByName("StartDate", value);
        }
      if (TRecurrencePatternPeer.ENDDATE.equals(name))
        {
            return setByName("EndDate", value);
        }
      if (TRecurrencePatternPeer.OCCURENCETYPE.equals(name))
        {
            return setByName("OccurenceType", value);
        }
      if (TRecurrencePatternPeer.NOOFOCCURENCES.equals(name))
        {
            return setByName("NoOfOccurences", value);
        }
      if (TRecurrencePatternPeer.TPUUID.equals(name))
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
            return getRecurrencePeriod();
        }
        if (pos == 2)
        {
            return getParam1();
        }
        if (pos == 3)
        {
            return getParam2();
        }
        if (pos == 4)
        {
            return getParam3();
        }
        if (pos == 5)
        {
            return getDays();
        }
        if (pos == 6)
        {
            return getDateIsAbsolute();
        }
        if (pos == 7)
        {
            return getStartDate();
        }
        if (pos == 8)
        {
            return getEndDate();
        }
        if (pos == 9)
        {
            return getOccurenceType();
        }
        if (pos == 10)
        {
            return getNoOfOccurences();
        }
        if (pos == 11)
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
            return setByName("RecurrencePeriod", value);
        }
    if (position == 2)
        {
            return setByName("Param1", value);
        }
    if (position == 3)
        {
            return setByName("Param2", value);
        }
    if (position == 4)
        {
            return setByName("Param3", value);
        }
    if (position == 5)
        {
            return setByName("Days", value);
        }
    if (position == 6)
        {
            return setByName("DateIsAbsolute", value);
        }
    if (position == 7)
        {
            return setByName("StartDate", value);
        }
    if (position == 8)
        {
            return setByName("EndDate", value);
        }
    if (position == 9)
        {
            return setByName("OccurenceType", value);
        }
    if (position == 10)
        {
            return setByName("NoOfOccurences", value);
        }
    if (position == 11)
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
        save(TRecurrencePatternPeer.DATABASE_NAME);
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
                    TRecurrencePatternPeer.doInsert((TRecurrencePattern) this, con);
                    setNew(false);
                }
                else
                {
                    TRecurrencePatternPeer.doUpdate((TRecurrencePattern) this, con);
                }
            }


            if (collTReportSubscribes != null)
            {
                for (int i = 0; i < collTReportSubscribes.size(); i++)
                {
                    ((TReportSubscribe) collTReportSubscribes.get(i)).save(con);
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
    public TRecurrencePattern copy() throws TorqueException
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
    public TRecurrencePattern copy(Connection con) throws TorqueException
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
    public TRecurrencePattern copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TRecurrencePattern(), deepcopy);
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
    public TRecurrencePattern copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TRecurrencePattern(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TRecurrencePattern copyInto(TRecurrencePattern copyObj) throws TorqueException
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
    protected TRecurrencePattern copyInto(TRecurrencePattern copyObj, Connection con) throws TorqueException
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
    protected TRecurrencePattern copyInto(TRecurrencePattern copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setRecurrencePeriod(recurrencePeriod);
        copyObj.setParam1(param1);
        copyObj.setParam2(param2);
        copyObj.setParam3(param3);
        copyObj.setDays(days);
        copyObj.setDateIsAbsolute(dateIsAbsolute);
        copyObj.setStartDate(startDate);
        copyObj.setEndDate(endDate);
        copyObj.setOccurenceType(occurenceType);
        copyObj.setNoOfOccurences(noOfOccurences);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TReportSubscribe> vTReportSubscribes = getTReportSubscribes();
        if (vTReportSubscribes != null)
        {
            for (int i = 0; i < vTReportSubscribes.size(); i++)
            {
                TReportSubscribe obj =  vTReportSubscribes.get(i);
                copyObj.addTReportSubscribe(obj.copy());
            }
        }
        else
        {
            copyObj.collTReportSubscribes = null;
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
    protected TRecurrencePattern copyInto(TRecurrencePattern copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setRecurrencePeriod(recurrencePeriod);
        copyObj.setParam1(param1);
        copyObj.setParam2(param2);
        copyObj.setParam3(param3);
        copyObj.setDays(days);
        copyObj.setDateIsAbsolute(dateIsAbsolute);
        copyObj.setStartDate(startDate);
        copyObj.setEndDate(endDate);
        copyObj.setOccurenceType(occurenceType);
        copyObj.setNoOfOccurences(noOfOccurences);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TReportSubscribe> vTReportSubscribes = getTReportSubscribes(con);
        if (vTReportSubscribes != null)
        {
            for (int i = 0; i < vTReportSubscribes.size(); i++)
            {
                TReportSubscribe obj =  vTReportSubscribes.get(i);
                copyObj.addTReportSubscribe(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTReportSubscribes = null;
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
    public TRecurrencePatternPeer getPeer()
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
        return TRecurrencePatternPeer.getTableMap();
    }

  
    /**
     * Creates a TRecurrencePatternBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TRecurrencePatternBean with the contents of this object
     */
    public TRecurrencePatternBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TRecurrencePatternBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TRecurrencePatternBean with the contents of this object
     */
    public TRecurrencePatternBean getBean(IdentityMap createdBeans)
    {
        TRecurrencePatternBean result = (TRecurrencePatternBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TRecurrencePatternBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setRecurrencePeriod(getRecurrencePeriod());
        result.setParam1(getParam1());
        result.setParam2(getParam2());
        result.setParam3(getParam3());
        result.setDays(getDays());
        result.setDateIsAbsolute(getDateIsAbsolute());
        result.setStartDate(getStartDate());
        result.setEndDate(getEndDate());
        result.setOccurenceType(getOccurenceType());
        result.setNoOfOccurences(getNoOfOccurences());
        result.setUuid(getUuid());



        if (collTReportSubscribes != null)
        {
            List<TReportSubscribeBean> relatedBeans = new ArrayList<TReportSubscribeBean>(collTReportSubscribes.size());
            for (Iterator<TReportSubscribe> collTReportSubscribesIt = collTReportSubscribes.iterator(); collTReportSubscribesIt.hasNext(); )
            {
                TReportSubscribe related = (TReportSubscribe) collTReportSubscribesIt.next();
                TReportSubscribeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTReportSubscribeBeans(relatedBeans);
        }

        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TRecurrencePattern with the contents
     * of a TRecurrencePatternBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TRecurrencePatternBean which contents are used to create
     *        the resulting class
     * @return an instance of TRecurrencePattern with the contents of bean
     */
    public static TRecurrencePattern createTRecurrencePattern(TRecurrencePatternBean bean)
        throws TorqueException
    {
        return createTRecurrencePattern(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TRecurrencePattern with the contents
     * of a TRecurrencePatternBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TRecurrencePatternBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TRecurrencePattern with the contents of bean
     */

    public static TRecurrencePattern createTRecurrencePattern(TRecurrencePatternBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TRecurrencePattern result = (TRecurrencePattern) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TRecurrencePattern();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setRecurrencePeriod(bean.getRecurrencePeriod());
        result.setParam1(bean.getParam1());
        result.setParam2(bean.getParam2());
        result.setParam3(bean.getParam3());
        result.setDays(bean.getDays());
        result.setDateIsAbsolute(bean.getDateIsAbsolute());
        result.setStartDate(bean.getStartDate());
        result.setEndDate(bean.getEndDate());
        result.setOccurenceType(bean.getOccurenceType());
        result.setNoOfOccurences(bean.getNoOfOccurences());
        result.setUuid(bean.getUuid());



        {
            List<TReportSubscribeBean> relatedBeans = bean.getTReportSubscribeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TReportSubscribeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TReportSubscribeBean relatedBean =  relatedBeansIt.next();
                    TReportSubscribe related = TReportSubscribe.createTReportSubscribe(relatedBean, createdObjects);
                    result.addTReportSubscribeFromBean(related);
                }
            }
        }

    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TReportSubscribe object to this object.
     * through the TReportSubscribe foreign key attribute
     *
     * @param toAdd TReportSubscribe
     */
    protected void addTReportSubscribeFromBean(TReportSubscribe toAdd)
    {
        initTReportSubscribes();
        collTReportSubscribes.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TRecurrencePattern:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("RecurrencePeriod = ")
           .append(getRecurrencePeriod())
           .append("\n");
        str.append("Param1 = ")
           .append(getParam1())
           .append("\n");
        str.append("Param2 = ")
           .append(getParam2())
           .append("\n");
        str.append("Param3 = ")
           .append(getParam3())
           .append("\n");
        str.append("Days = ")
           .append(getDays())
           .append("\n");
        str.append("DateIsAbsolute = ")
           .append(getDateIsAbsolute())
           .append("\n");
        str.append("StartDate = ")
           .append(getStartDate())
           .append("\n");
        str.append("EndDate = ")
           .append(getEndDate())
           .append("\n");
        str.append("OccurenceType = ")
           .append(getOccurenceType())
           .append("\n");
        str.append("NoOfOccurences = ")
           .append(getNoOfOccurences())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
