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



import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TMSProjectTaskBean;
import com.aurel.track.beans.TWorkItemBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TMSProjectTask
 */
public abstract class BaseTMSProjectTask extends TpBaseObject
{
    /** The Peer class */
    private static final TMSProjectTaskPeer peer =
        new TMSProjectTaskPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the workitem field */
    private Integer workitem;

    /** The value for the uniqueID field */
    private Integer uniqueID;

    /** The value for the taskType field */
    private Integer taskType;

    /** The value for the contact field */
    private String contact;

    /** The value for the wBS field */
    private String wBS;

    /** The value for the outlineNumber field */
    private String outlineNumber;

    /** The value for the duration field */
    private String duration;

    /** The value for the durationFormat field */
    private Integer durationFormat;

    /** The value for the estimated field */
    private String estimated = "N";

    /** The value for the milestone field */
    private String milestone = "N";

    /** The value for the summary field */
    private String summary = "N";

    /** The value for the actualDuration field */
    private String actualDuration;

    /** The value for the remainingDuration field */
    private String remainingDuration;

    /** The value for the constraintType field */
    private Integer constraintType;

    /** The value for the constraintDate field */
    private Date constraintDate;

    /** The value for the deadline field */
    private Date deadline;

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
    public void setObjectID(Integer v) 
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
        }


    }

    /**
     * Get the Workitem
     *
     * @return Integer
     */
    public Integer getWorkitem()
    {
        return workitem;
    }


    /**
     * Set the value of Workitem
     *
     * @param v new value
     */
    public void setWorkitem(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.workitem, v))
        {
            this.workitem = v;
            setModified(true);
        }


        if (aTWorkItem != null && !ObjectUtils.equals(aTWorkItem.getObjectID(), v))
        {
            aTWorkItem = null;
        }

    }

    /**
     * Get the UniqueID
     *
     * @return Integer
     */
    public Integer getUniqueID()
    {
        return uniqueID;
    }


    /**
     * Set the value of UniqueID
     *
     * @param v new value
     */
    public void setUniqueID(Integer v) 
    {

        if (!ObjectUtils.equals(this.uniqueID, v))
        {
            this.uniqueID = v;
            setModified(true);
        }


    }

    /**
     * Get the TaskType
     *
     * @return Integer
     */
    public Integer getTaskType()
    {
        return taskType;
    }


    /**
     * Set the value of TaskType
     *
     * @param v new value
     */
    public void setTaskType(Integer v) 
    {

        if (!ObjectUtils.equals(this.taskType, v))
        {
            this.taskType = v;
            setModified(true);
        }


    }

    /**
     * Get the Contact
     *
     * @return String
     */
    public String getContact()
    {
        return contact;
    }


    /**
     * Set the value of Contact
     *
     * @param v new value
     */
    public void setContact(String v) 
    {

        if (!ObjectUtils.equals(this.contact, v))
        {
            this.contact = v;
            setModified(true);
        }


    }

    /**
     * Get the WBS
     *
     * @return String
     */
    public String getWBS()
    {
        return wBS;
    }


    /**
     * Set the value of WBS
     *
     * @param v new value
     */
    public void setWBS(String v) 
    {

        if (!ObjectUtils.equals(this.wBS, v))
        {
            this.wBS = v;
            setModified(true);
        }


    }

    /**
     * Get the OutlineNumber
     *
     * @return String
     */
    public String getOutlineNumber()
    {
        return outlineNumber;
    }


    /**
     * Set the value of OutlineNumber
     *
     * @param v new value
     */
    public void setOutlineNumber(String v) 
    {

        if (!ObjectUtils.equals(this.outlineNumber, v))
        {
            this.outlineNumber = v;
            setModified(true);
        }


    }

    /**
     * Get the Duration
     *
     * @return String
     */
    public String getDuration()
    {
        return duration;
    }


    /**
     * Set the value of Duration
     *
     * @param v new value
     */
    public void setDuration(String v) 
    {

        if (!ObjectUtils.equals(this.duration, v))
        {
            this.duration = v;
            setModified(true);
        }


    }

    /**
     * Get the DurationFormat
     *
     * @return Integer
     */
    public Integer getDurationFormat()
    {
        return durationFormat;
    }


    /**
     * Set the value of DurationFormat
     *
     * @param v new value
     */
    public void setDurationFormat(Integer v) 
    {

        if (!ObjectUtils.equals(this.durationFormat, v))
        {
            this.durationFormat = v;
            setModified(true);
        }


    }

    /**
     * Get the Estimated
     *
     * @return String
     */
    public String getEstimated()
    {
        return estimated;
    }


    /**
     * Set the value of Estimated
     *
     * @param v new value
     */
    public void setEstimated(String v) 
    {

        if (!ObjectUtils.equals(this.estimated, v))
        {
            this.estimated = v;
            setModified(true);
        }


    }

    /**
     * Get the Milestone
     *
     * @return String
     */
    public String getMilestone()
    {
        return milestone;
    }


    /**
     * Set the value of Milestone
     *
     * @param v new value
     */
    public void setMilestone(String v) 
    {

        if (!ObjectUtils.equals(this.milestone, v))
        {
            this.milestone = v;
            setModified(true);
        }


    }

    /**
     * Get the Summary
     *
     * @return String
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Set the value of Summary
     *
     * @param v new value
     */
    public void setSummary(String v) 
    {

        if (!ObjectUtils.equals(this.summary, v))
        {
            this.summary = v;
            setModified(true);
        }


    }

    /**
     * Get the ActualDuration
     *
     * @return String
     */
    public String getActualDuration()
    {
        return actualDuration;
    }


    /**
     * Set the value of ActualDuration
     *
     * @param v new value
     */
    public void setActualDuration(String v) 
    {

        if (!ObjectUtils.equals(this.actualDuration, v))
        {
            this.actualDuration = v;
            setModified(true);
        }


    }

    /**
     * Get the RemainingDuration
     *
     * @return String
     */
    public String getRemainingDuration()
    {
        return remainingDuration;
    }


    /**
     * Set the value of RemainingDuration
     *
     * @param v new value
     */
    public void setRemainingDuration(String v) 
    {

        if (!ObjectUtils.equals(this.remainingDuration, v))
        {
            this.remainingDuration = v;
            setModified(true);
        }


    }

    /**
     * Get the ConstraintType
     *
     * @return Integer
     */
    public Integer getConstraintType()
    {
        return constraintType;
    }


    /**
     * Set the value of ConstraintType
     *
     * @param v new value
     */
    public void setConstraintType(Integer v) 
    {

        if (!ObjectUtils.equals(this.constraintType, v))
        {
            this.constraintType = v;
            setModified(true);
        }


    }

    /**
     * Get the ConstraintDate
     *
     * @return Date
     */
    public Date getConstraintDate()
    {
        return constraintDate;
    }


    /**
     * Set the value of ConstraintDate
     *
     * @param v new value
     */
    public void setConstraintDate(Date v) 
    {

        if (!ObjectUtils.equals(this.constraintDate, v))
        {
            this.constraintDate = v;
            setModified(true);
        }


    }

    /**
     * Get the Deadline
     *
     * @return Date
     */
    public Date getDeadline()
    {
        return deadline;
    }


    /**
     * Set the value of Deadline
     *
     * @param v new value
     */
    public void setDeadline(Date v) 
    {

        if (!ObjectUtils.equals(this.deadline, v))
        {
            this.deadline = v;
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

    



    private TWorkItem aTWorkItem;

    /**
     * Declares an association between this object and a TWorkItem object
     *
     * @param v TWorkItem
     * @throws TorqueException
     */
    public void setTWorkItem(TWorkItem v) throws TorqueException
    {
        if (v == null)
        {
            setWorkitem((Integer) null);
        }
        else
        {
            setWorkitem(v.getObjectID());
        }
        aTWorkItem = v;
    }


    /**
     * Returns the associated TWorkItem object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItem()
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workitem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workitem));
        }
        return aTWorkItem;
    }

    /**
     * Return the associated TWorkItem object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItem(Connection connection)
        throws TorqueException
    {
        if (aTWorkItem == null && (!ObjectUtils.equals(this.workitem, null)))
        {
            aTWorkItem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.workitem), connection);
        }
        return aTWorkItem;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkItemKey(ObjectKey key) throws TorqueException
    {

        setWorkitem(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("Workitem");
            fieldNames.add("UniqueID");
            fieldNames.add("TaskType");
            fieldNames.add("Contact");
            fieldNames.add("WBS");
            fieldNames.add("OutlineNumber");
            fieldNames.add("Duration");
            fieldNames.add("DurationFormat");
            fieldNames.add("Estimated");
            fieldNames.add("Milestone");
            fieldNames.add("Summary");
            fieldNames.add("ActualDuration");
            fieldNames.add("RemainingDuration");
            fieldNames.add("ConstraintType");
            fieldNames.add("ConstraintDate");
            fieldNames.add("Deadline");
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
        if (name.equals("Workitem"))
        {
            return getWorkitem();
        }
        if (name.equals("UniqueID"))
        {
            return getUniqueID();
        }
        if (name.equals("TaskType"))
        {
            return getTaskType();
        }
        if (name.equals("Contact"))
        {
            return getContact();
        }
        if (name.equals("WBS"))
        {
            return getWBS();
        }
        if (name.equals("OutlineNumber"))
        {
            return getOutlineNumber();
        }
        if (name.equals("Duration"))
        {
            return getDuration();
        }
        if (name.equals("DurationFormat"))
        {
            return getDurationFormat();
        }
        if (name.equals("Estimated"))
        {
            return getEstimated();
        }
        if (name.equals("Milestone"))
        {
            return getMilestone();
        }
        if (name.equals("Summary"))
        {
            return getSummary();
        }
        if (name.equals("ActualDuration"))
        {
            return getActualDuration();
        }
        if (name.equals("RemainingDuration"))
        {
            return getRemainingDuration();
        }
        if (name.equals("ConstraintType"))
        {
            return getConstraintType();
        }
        if (name.equals("ConstraintDate"))
        {
            return getConstraintDate();
        }
        if (name.equals("Deadline"))
        {
            return getDeadline();
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
        if (name.equals("Workitem"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWorkitem((Integer) value);
            return true;
        }
        if (name.equals("UniqueID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setUniqueID((Integer) value);
            return true;
        }
        if (name.equals("TaskType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTaskType((Integer) value);
            return true;
        }
        if (name.equals("Contact"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setContact((String) value);
            return true;
        }
        if (name.equals("WBS"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWBS((String) value);
            return true;
        }
        if (name.equals("OutlineNumber"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOutlineNumber((String) value);
            return true;
        }
        if (name.equals("Duration"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDuration((String) value);
            return true;
        }
        if (name.equals("DurationFormat"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDurationFormat((Integer) value);
            return true;
        }
        if (name.equals("Estimated"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEstimated((String) value);
            return true;
        }
        if (name.equals("Milestone"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setMilestone((String) value);
            return true;
        }
        if (name.equals("Summary"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSummary((String) value);
            return true;
        }
        if (name.equals("ActualDuration"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setActualDuration((String) value);
            return true;
        }
        if (name.equals("RemainingDuration"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRemainingDuration((String) value);
            return true;
        }
        if (name.equals("ConstraintType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setConstraintType((Integer) value);
            return true;
        }
        if (name.equals("ConstraintDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setConstraintDate((Date) value);
            return true;
        }
        if (name.equals("Deadline"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDeadline((Date) value);
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
        if (name.equals(TMSProjectTaskPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TMSProjectTaskPeer.WORKITEM))
        {
            return getWorkitem();
        }
        if (name.equals(TMSProjectTaskPeer.UNIQUEID))
        {
            return getUniqueID();
        }
        if (name.equals(TMSProjectTaskPeer.TASKTYPE))
        {
            return getTaskType();
        }
        if (name.equals(TMSProjectTaskPeer.CONTACT))
        {
            return getContact();
        }
        if (name.equals(TMSProjectTaskPeer.WBS))
        {
            return getWBS();
        }
        if (name.equals(TMSProjectTaskPeer.OUTLINENUMBER))
        {
            return getOutlineNumber();
        }
        if (name.equals(TMSProjectTaskPeer.DURATION))
        {
            return getDuration();
        }
        if (name.equals(TMSProjectTaskPeer.DURATIONFORMAT))
        {
            return getDurationFormat();
        }
        if (name.equals(TMSProjectTaskPeer.ESTIMATED))
        {
            return getEstimated();
        }
        if (name.equals(TMSProjectTaskPeer.MILESTONE))
        {
            return getMilestone();
        }
        if (name.equals(TMSProjectTaskPeer.SUMMARY))
        {
            return getSummary();
        }
        if (name.equals(TMSProjectTaskPeer.ACTUALDURATION))
        {
            return getActualDuration();
        }
        if (name.equals(TMSProjectTaskPeer.REMAININGDURATION))
        {
            return getRemainingDuration();
        }
        if (name.equals(TMSProjectTaskPeer.CONSTRAINTTYPE))
        {
            return getConstraintType();
        }
        if (name.equals(TMSProjectTaskPeer.CONSTRAINTDATE))
        {
            return getConstraintDate();
        }
        if (name.equals(TMSProjectTaskPeer.DEADLINE))
        {
            return getDeadline();
        }
        if (name.equals(TMSProjectTaskPeer.TPUUID))
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
      if (TMSProjectTaskPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TMSProjectTaskPeer.WORKITEM.equals(name))
        {
            return setByName("Workitem", value);
        }
      if (TMSProjectTaskPeer.UNIQUEID.equals(name))
        {
            return setByName("UniqueID", value);
        }
      if (TMSProjectTaskPeer.TASKTYPE.equals(name))
        {
            return setByName("TaskType", value);
        }
      if (TMSProjectTaskPeer.CONTACT.equals(name))
        {
            return setByName("Contact", value);
        }
      if (TMSProjectTaskPeer.WBS.equals(name))
        {
            return setByName("WBS", value);
        }
      if (TMSProjectTaskPeer.OUTLINENUMBER.equals(name))
        {
            return setByName("OutlineNumber", value);
        }
      if (TMSProjectTaskPeer.DURATION.equals(name))
        {
            return setByName("Duration", value);
        }
      if (TMSProjectTaskPeer.DURATIONFORMAT.equals(name))
        {
            return setByName("DurationFormat", value);
        }
      if (TMSProjectTaskPeer.ESTIMATED.equals(name))
        {
            return setByName("Estimated", value);
        }
      if (TMSProjectTaskPeer.MILESTONE.equals(name))
        {
            return setByName("Milestone", value);
        }
      if (TMSProjectTaskPeer.SUMMARY.equals(name))
        {
            return setByName("Summary", value);
        }
      if (TMSProjectTaskPeer.ACTUALDURATION.equals(name))
        {
            return setByName("ActualDuration", value);
        }
      if (TMSProjectTaskPeer.REMAININGDURATION.equals(name))
        {
            return setByName("RemainingDuration", value);
        }
      if (TMSProjectTaskPeer.CONSTRAINTTYPE.equals(name))
        {
            return setByName("ConstraintType", value);
        }
      if (TMSProjectTaskPeer.CONSTRAINTDATE.equals(name))
        {
            return setByName("ConstraintDate", value);
        }
      if (TMSProjectTaskPeer.DEADLINE.equals(name))
        {
            return setByName("Deadline", value);
        }
      if (TMSProjectTaskPeer.TPUUID.equals(name))
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
            return getWorkitem();
        }
        if (pos == 2)
        {
            return getUniqueID();
        }
        if (pos == 3)
        {
            return getTaskType();
        }
        if (pos == 4)
        {
            return getContact();
        }
        if (pos == 5)
        {
            return getWBS();
        }
        if (pos == 6)
        {
            return getOutlineNumber();
        }
        if (pos == 7)
        {
            return getDuration();
        }
        if (pos == 8)
        {
            return getDurationFormat();
        }
        if (pos == 9)
        {
            return getEstimated();
        }
        if (pos == 10)
        {
            return getMilestone();
        }
        if (pos == 11)
        {
            return getSummary();
        }
        if (pos == 12)
        {
            return getActualDuration();
        }
        if (pos == 13)
        {
            return getRemainingDuration();
        }
        if (pos == 14)
        {
            return getConstraintType();
        }
        if (pos == 15)
        {
            return getConstraintDate();
        }
        if (pos == 16)
        {
            return getDeadline();
        }
        if (pos == 17)
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
            return setByName("Workitem", value);
        }
    if (position == 2)
        {
            return setByName("UniqueID", value);
        }
    if (position == 3)
        {
            return setByName("TaskType", value);
        }
    if (position == 4)
        {
            return setByName("Contact", value);
        }
    if (position == 5)
        {
            return setByName("WBS", value);
        }
    if (position == 6)
        {
            return setByName("OutlineNumber", value);
        }
    if (position == 7)
        {
            return setByName("Duration", value);
        }
    if (position == 8)
        {
            return setByName("DurationFormat", value);
        }
    if (position == 9)
        {
            return setByName("Estimated", value);
        }
    if (position == 10)
        {
            return setByName("Milestone", value);
        }
    if (position == 11)
        {
            return setByName("Summary", value);
        }
    if (position == 12)
        {
            return setByName("ActualDuration", value);
        }
    if (position == 13)
        {
            return setByName("RemainingDuration", value);
        }
    if (position == 14)
        {
            return setByName("ConstraintType", value);
        }
    if (position == 15)
        {
            return setByName("ConstraintDate", value);
        }
    if (position == 16)
        {
            return setByName("Deadline", value);
        }
    if (position == 17)
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
        save(TMSProjectTaskPeer.DATABASE_NAME);
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
                    TMSProjectTaskPeer.doInsert((TMSProjectTask) this, con);
                    setNew(false);
                }
                else
                {
                    TMSProjectTaskPeer.doUpdate((TMSProjectTask) this, con);
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
        
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) 
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
    public TMSProjectTask copy() throws TorqueException
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
    public TMSProjectTask copy(Connection con) throws TorqueException
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
    public TMSProjectTask copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TMSProjectTask(), deepcopy);
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
    public TMSProjectTask copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TMSProjectTask(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TMSProjectTask copyInto(TMSProjectTask copyObj) throws TorqueException
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
    protected TMSProjectTask copyInto(TMSProjectTask copyObj, Connection con) throws TorqueException
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
    protected TMSProjectTask copyInto(TMSProjectTask copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkitem(workitem);
        copyObj.setUniqueID(uniqueID);
        copyObj.setTaskType(taskType);
        copyObj.setContact(contact);
        copyObj.setWBS(wBS);
        copyObj.setOutlineNumber(outlineNumber);
        copyObj.setDuration(duration);
        copyObj.setDurationFormat(durationFormat);
        copyObj.setEstimated(estimated);
        copyObj.setMilestone(milestone);
        copyObj.setSummary(summary);
        copyObj.setActualDuration(actualDuration);
        copyObj.setRemainingDuration(remainingDuration);
        copyObj.setConstraintType(constraintType);
        copyObj.setConstraintDate(constraintDate);
        copyObj.setDeadline(deadline);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {
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
    protected TMSProjectTask copyInto(TMSProjectTask copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setWorkitem(workitem);
        copyObj.setUniqueID(uniqueID);
        copyObj.setTaskType(taskType);
        copyObj.setContact(contact);
        copyObj.setWBS(wBS);
        copyObj.setOutlineNumber(outlineNumber);
        copyObj.setDuration(duration);
        copyObj.setDurationFormat(durationFormat);
        copyObj.setEstimated(estimated);
        copyObj.setMilestone(milestone);
        copyObj.setSummary(summary);
        copyObj.setActualDuration(actualDuration);
        copyObj.setRemainingDuration(remainingDuration);
        copyObj.setConstraintType(constraintType);
        copyObj.setConstraintDate(constraintDate);
        copyObj.setDeadline(deadline);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TMSProjectTaskPeer getPeer()
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
        return TMSProjectTaskPeer.getTableMap();
    }

  
    /**
     * Creates a TMSProjectTaskBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TMSProjectTaskBean with the contents of this object
     */
    public TMSProjectTaskBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TMSProjectTaskBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TMSProjectTaskBean with the contents of this object
     */
    public TMSProjectTaskBean getBean(IdentityMap createdBeans)
    {
        TMSProjectTaskBean result = (TMSProjectTaskBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TMSProjectTaskBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setWorkitem(getWorkitem());
        result.setUniqueID(getUniqueID());
        result.setTaskType(getTaskType());
        result.setContact(getContact());
        result.setWBS(getWBS());
        result.setOutlineNumber(getOutlineNumber());
        result.setDuration(getDuration());
        result.setDurationFormat(getDurationFormat());
        result.setEstimated(getEstimated());
        result.setMilestone(getMilestone());
        result.setSummary(getSummary());
        result.setActualDuration(getActualDuration());
        result.setRemainingDuration(getRemainingDuration());
        result.setConstraintType(getConstraintType());
        result.setConstraintDate(getConstraintDate());
        result.setDeadline(getDeadline());
        result.setUuid(getUuid());





        if (aTWorkItem != null)
        {
            TWorkItemBean relatedBean = aTWorkItem.getBean(createdBeans);
            result.setTWorkItemBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TMSProjectTask with the contents
     * of a TMSProjectTaskBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TMSProjectTaskBean which contents are used to create
     *        the resulting class
     * @return an instance of TMSProjectTask with the contents of bean
     */
    public static TMSProjectTask createTMSProjectTask(TMSProjectTaskBean bean)
        throws TorqueException
    {
        return createTMSProjectTask(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TMSProjectTask with the contents
     * of a TMSProjectTaskBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TMSProjectTaskBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TMSProjectTask with the contents of bean
     */

    public static TMSProjectTask createTMSProjectTask(TMSProjectTaskBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TMSProjectTask result = (TMSProjectTask) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TMSProjectTask();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setWorkitem(bean.getWorkitem());
        result.setUniqueID(bean.getUniqueID());
        result.setTaskType(bean.getTaskType());
        result.setContact(bean.getContact());
        result.setWBS(bean.getWBS());
        result.setOutlineNumber(bean.getOutlineNumber());
        result.setDuration(bean.getDuration());
        result.setDurationFormat(bean.getDurationFormat());
        result.setEstimated(bean.getEstimated());
        result.setMilestone(bean.getMilestone());
        result.setSummary(bean.getSummary());
        result.setActualDuration(bean.getActualDuration());
        result.setRemainingDuration(bean.getRemainingDuration());
        result.setConstraintType(bean.getConstraintType());
        result.setConstraintDate(bean.getConstraintDate());
        result.setDeadline(bean.getDeadline());
        result.setUuid(bean.getUuid());





        {
            TWorkItemBean relatedBean = bean.getTWorkItemBean();
            if (relatedBean != null)
            {
                TWorkItem relatedObject = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                result.setTWorkItem(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TMSProjectTask:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Workitem = ")
           .append(getWorkitem())
           .append("\n");
        str.append("UniqueID = ")
           .append(getUniqueID())
           .append("\n");
        str.append("TaskType = ")
           .append(getTaskType())
           .append("\n");
        str.append("Contact = ")
           .append(getContact())
           .append("\n");
        str.append("WBS = ")
           .append(getWBS())
           .append("\n");
        str.append("OutlineNumber = ")
           .append(getOutlineNumber())
           .append("\n");
        str.append("Duration = ")
           .append(getDuration())
           .append("\n");
        str.append("DurationFormat = ")
           .append(getDurationFormat())
           .append("\n");
        str.append("Estimated = ")
           .append(getEstimated())
           .append("\n");
        str.append("Milestone = ")
           .append(getMilestone())
           .append("\n");
        str.append("Summary = ")
           .append(getSummary())
           .append("\n");
        str.append("ActualDuration = ")
           .append(getActualDuration())
           .append("\n");
        str.append("RemainingDuration = ")
           .append(getRemainingDuration())
           .append("\n");
        str.append("ConstraintType = ")
           .append(getConstraintType())
           .append("\n");
        str.append("ConstraintDate = ")
           .append(getConstraintDate())
           .append("\n");
        str.append("Deadline = ")
           .append(getDeadline())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
