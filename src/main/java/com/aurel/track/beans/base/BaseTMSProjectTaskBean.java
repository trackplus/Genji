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


package com.aurel.track.beans.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

  import com.aurel.track.beans.*;


/**
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TMSProjectTaskBean
 */
public abstract class BaseTMSProjectTaskBean
    implements Serializable
{

    /**
     * whether the bean or its underlying object has changed
     * since last reading from the database
     */
    private boolean modified = true;

    /**
     * false if the underlying object has been read from the database,
     * true otherwise
     */
    private boolean isNew = true;


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
     * sets whether the bean exists in the database
     */
    public void setNew(boolean isNew)
    {
        this.isNew = isNew;
    }

    /**
     * returns whether the bean exists in the database
     */
    public boolean isNew()
    {
        return this.isNew;
    }

    /**
     * sets whether the bean or the object it was created from
     * was modified since the object was last read from the database
     */
    public void setModified(boolean isModified)
    {
        this.modified = isModified;
    }

    /**
     * returns whether the bean or the object it was created from
     * was modified since the object was last read from the database
     */
    public boolean isModified()
    {
        return this.modified;
    }


    /**
     * Get the ObjectID
     *
     * @return Integer
     */
    public Integer getObjectID ()
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

        this.objectID = v;
        setModified(true);

    }

    /**
     * Get the Workitem
     *
     * @return Integer
     */
    public Integer getWorkitem ()
    {
        return workitem;
    }

    /**
     * Set the value of Workitem
     *
     * @param v new value
     */
    public void setWorkitem(Integer v)
    {

        this.workitem = v;
        setModified(true);

    }

    /**
     * Get the UniqueID
     *
     * @return Integer
     */
    public Integer getUniqueID ()
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

        this.uniqueID = v;
        setModified(true);

    }

    /**
     * Get the TaskType
     *
     * @return Integer
     */
    public Integer getTaskType ()
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

        this.taskType = v;
        setModified(true);

    }

    /**
     * Get the Contact
     *
     * @return String
     */
    public String getContact ()
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

        this.contact = v;
        setModified(true);

    }

    /**
     * Get the WBS
     *
     * @return String
     */
    public String getWBS ()
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

        this.wBS = v;
        setModified(true);

    }

    /**
     * Get the OutlineNumber
     *
     * @return String
     */
    public String getOutlineNumber ()
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

        this.outlineNumber = v;
        setModified(true);

    }

    /**
     * Get the Duration
     *
     * @return String
     */
    public String getDuration ()
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

        this.duration = v;
        setModified(true);

    }

    /**
     * Get the DurationFormat
     *
     * @return Integer
     */
    public Integer getDurationFormat ()
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

        this.durationFormat = v;
        setModified(true);

    }

    /**
     * Get the Estimated
     *
     * @return String
     */
    public String getEstimated ()
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

        this.estimated = v;
        setModified(true);

    }

    /**
     * Get the Milestone
     *
     * @return String
     */
    public String getMilestone ()
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

        this.milestone = v;
        setModified(true);

    }

    /**
     * Get the Summary
     *
     * @return String
     */
    public String getSummary ()
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

        this.summary = v;
        setModified(true);

    }

    /**
     * Get the ActualDuration
     *
     * @return String
     */
    public String getActualDuration ()
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

        this.actualDuration = v;
        setModified(true);

    }

    /**
     * Get the RemainingDuration
     *
     * @return String
     */
    public String getRemainingDuration ()
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

        this.remainingDuration = v;
        setModified(true);

    }

    /**
     * Get the ConstraintType
     *
     * @return Integer
     */
    public Integer getConstraintType ()
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

        this.constraintType = v;
        setModified(true);

    }

    /**
     * Get the ConstraintDate
     *
     * @return Date
     */
    public Date getConstraintDate ()
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

        this.constraintDate = v;
        setModified(true);

    }

    /**
     * Get the Deadline
     *
     * @return Date
     */
    public Date getDeadline ()
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

        this.deadline = v;
        setModified(true);

    }

    /**
     * Get the Uuid
     *
     * @return String
     */
    public String getUuid ()
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

        this.uuid = v;
        setModified(true);

    }

    



    private TWorkItemBean aTWorkItemBean;

    /**
     * sets an associated TWorkItemBean object
     *
     * @param v TWorkItemBean
     */
    public void setTWorkItemBean(TWorkItemBean v)
    {
        if (v == null)
        {
            setWorkitem((Integer) null);
        }
        else
        {
            setWorkitem(v.getObjectID());
        }
        aTWorkItemBean = v;
    }


    /**
     * Get the associated TWorkItemBean object
     *
     * @return the associated TWorkItemBean object
     */
    public TWorkItemBean getTWorkItemBean()
    {
        return aTWorkItemBean;
    }



}
