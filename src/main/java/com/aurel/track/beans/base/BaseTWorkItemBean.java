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
 * extended; all references should be to TWorkItemBean
 */
public abstract class BaseTWorkItemBean
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

    /** The value for the ownerID field */
    private Integer ownerID;

    /** The value for the changedByID field */
    private Integer changedByID;

    /** The value for the originatorID field */
    private Integer originatorID;

    /** The value for the responsibleID field */
    private Integer responsibleID;

    /** The value for the projectID field */
    private Integer projectID;

    /** The value for the projectCategoryID field */
    private Integer projectCategoryID;

    /** The value for the listTypeID field */
    private Integer listTypeID;

    /** The value for the classID field */
    private Integer classID;

    /** The value for the priorityID field */
    private Integer priorityID;

    /** The value for the severityID field */
    private Integer severityID;

    /** The value for the superiorworkitem field */
    private Integer superiorworkitem;

    /** The value for the synopsis field */
    private String synopsis;

    /** The value for the description field */
    private String description;

    /** The value for the reference field */
    private String reference;

    /** The value for the lastEdit field */
    private Date lastEdit;

    /** The value for the releaseNoticedID field */
    private Integer releaseNoticedID;

    /** The value for the releaseScheduledID field */
    private Integer releaseScheduledID;

    /** The value for the build field */
    private String build;

    /** The value for the stateID field */
    private Integer stateID;

    /** The value for the startDate field */
    private Date startDate;

    /** The value for the endDate field */
    private Date endDate;

    /** The value for the submitterEmail field */
    private String submitterEmail;

    /** The value for the created field */
    private Date created;

    /** The value for the actualStartDate field */
    private Date actualStartDate;

    /** The value for the actualEndDate field */
    private Date actualEndDate;

    /** The value for the level field */
    private String level;

    /** The value for the accessLevel field */
    private Integer accessLevel = new Integer(0);

    /** The value for the archiveLevel field */
    private Integer archiveLevel = new Integer(0);

    /** The value for the escalationLevel field */
    private Integer escalationLevel;

    /** The value for the taskIsMilestone field */
    private String taskIsMilestone = "N";

    /** The value for the taskIsSubproject field */
    private String taskIsSubproject = "N";

    /** The value for the taskIsSummary field */
    private String taskIsSummary = "N";

    /** The value for the taskConstraint field */
    private Integer taskConstraint;

    /** The value for the taskConstraintDate field */
    private Date taskConstraintDate;

    /** The value for the pSPCode field */
    private String pSPCode;

    /** The value for the iDNumber field */
    private Integer iDNumber;

    /** The value for the wBSOnLevel field */
    private Integer wBSOnLevel;

    /** The value for the reminderDate field */
    private Date reminderDate;

    /** The value for the topDownStartDate field */
    private Date topDownStartDate;

    /** The value for the topDownEndDate field */
    private Date topDownEndDate;

    /** The value for the overBudget field */
    private String overBudget = "N";

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
     * Get the OwnerID
     *
     * @return Integer
     */
    public Integer getOwnerID ()
    {
        return ownerID;
    }

    /**
     * Set the value of OwnerID
     *
     * @param v new value
     */
    public void setOwnerID(Integer v)
    {

        this.ownerID = v;
        setModified(true);

    }

    /**
     * Get the ChangedByID
     *
     * @return Integer
     */
    public Integer getChangedByID ()
    {
        return changedByID;
    }

    /**
     * Set the value of ChangedByID
     *
     * @param v new value
     */
    public void setChangedByID(Integer v)
    {

        this.changedByID = v;
        setModified(true);

    }

    /**
     * Get the OriginatorID
     *
     * @return Integer
     */
    public Integer getOriginatorID ()
    {
        return originatorID;
    }

    /**
     * Set the value of OriginatorID
     *
     * @param v new value
     */
    public void setOriginatorID(Integer v)
    {

        this.originatorID = v;
        setModified(true);

    }

    /**
     * Get the ResponsibleID
     *
     * @return Integer
     */
    public Integer getResponsibleID ()
    {
        return responsibleID;
    }

    /**
     * Set the value of ResponsibleID
     *
     * @param v new value
     */
    public void setResponsibleID(Integer v)
    {

        this.responsibleID = v;
        setModified(true);

    }

    /**
     * Get the ProjectID
     *
     * @return Integer
     */
    public Integer getProjectID ()
    {
        return projectID;
    }

    /**
     * Set the value of ProjectID
     *
     * @param v new value
     */
    public void setProjectID(Integer v)
    {

        this.projectID = v;
        setModified(true);

    }

    /**
     * Get the ProjectCategoryID
     *
     * @return Integer
     */
    public Integer getProjectCategoryID ()
    {
        return projectCategoryID;
    }

    /**
     * Set the value of ProjectCategoryID
     *
     * @param v new value
     */
    public void setProjectCategoryID(Integer v)
    {

        this.projectCategoryID = v;
        setModified(true);

    }

    /**
     * Get the ListTypeID
     *
     * @return Integer
     */
    public Integer getListTypeID ()
    {
        return listTypeID;
    }

    /**
     * Set the value of ListTypeID
     *
     * @param v new value
     */
    public void setListTypeID(Integer v)
    {

        this.listTypeID = v;
        setModified(true);

    }

    /**
     * Get the ClassID
     *
     * @return Integer
     */
    public Integer getClassID ()
    {
        return classID;
    }

    /**
     * Set the value of ClassID
     *
     * @param v new value
     */
    public void setClassID(Integer v)
    {

        this.classID = v;
        setModified(true);

    }

    /**
     * Get the PriorityID
     *
     * @return Integer
     */
    public Integer getPriorityID ()
    {
        return priorityID;
    }

    /**
     * Set the value of PriorityID
     *
     * @param v new value
     */
    public void setPriorityID(Integer v)
    {

        this.priorityID = v;
        setModified(true);

    }

    /**
     * Get the SeverityID
     *
     * @return Integer
     */
    public Integer getSeverityID ()
    {
        return severityID;
    }

    /**
     * Set the value of SeverityID
     *
     * @param v new value
     */
    public void setSeverityID(Integer v)
    {

        this.severityID = v;
        setModified(true);

    }

    /**
     * Get the Superiorworkitem
     *
     * @return Integer
     */
    public Integer getSuperiorworkitem ()
    {
        return superiorworkitem;
    }

    /**
     * Set the value of Superiorworkitem
     *
     * @param v new value
     */
    public void setSuperiorworkitem(Integer v)
    {

        this.superiorworkitem = v;
        setModified(true);

    }

    /**
     * Get the Synopsis
     *
     * @return String
     */
    public String getSynopsis ()
    {
        return synopsis;
    }

    /**
     * Set the value of Synopsis
     *
     * @param v new value
     */
    public void setSynopsis(String v)
    {

        this.synopsis = v;
        setModified(true);

    }

    /**
     * Get the Description
     *
     * @return String
     */
    public String getDescription ()
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

        this.description = v;
        setModified(true);

    }

    /**
     * Get the Reference
     *
     * @return String
     */
    public String getReference ()
    {
        return reference;
    }

    /**
     * Set the value of Reference
     *
     * @param v new value
     */
    public void setReference(String v)
    {

        this.reference = v;
        setModified(true);

    }

    /**
     * Get the LastEdit
     *
     * @return Date
     */
    public Date getLastEdit ()
    {
        return lastEdit;
    }

    /**
     * Set the value of LastEdit
     *
     * @param v new value
     */
    public void setLastEdit(Date v)
    {

        this.lastEdit = v;
        setModified(true);

    }

    /**
     * Get the ReleaseNoticedID
     *
     * @return Integer
     */
    public Integer getReleaseNoticedID ()
    {
        return releaseNoticedID;
    }

    /**
     * Set the value of ReleaseNoticedID
     *
     * @param v new value
     */
    public void setReleaseNoticedID(Integer v)
    {

        this.releaseNoticedID = v;
        setModified(true);

    }

    /**
     * Get the ReleaseScheduledID
     *
     * @return Integer
     */
    public Integer getReleaseScheduledID ()
    {
        return releaseScheduledID;
    }

    /**
     * Set the value of ReleaseScheduledID
     *
     * @param v new value
     */
    public void setReleaseScheduledID(Integer v)
    {

        this.releaseScheduledID = v;
        setModified(true);

    }

    /**
     * Get the Build
     *
     * @return String
     */
    public String getBuild ()
    {
        return build;
    }

    /**
     * Set the value of Build
     *
     * @param v new value
     */
    public void setBuild(String v)
    {

        this.build = v;
        setModified(true);

    }

    /**
     * Get the StateID
     *
     * @return Integer
     */
    public Integer getStateID ()
    {
        return stateID;
    }

    /**
     * Set the value of StateID
     *
     * @param v new value
     */
    public void setStateID(Integer v)
    {

        this.stateID = v;
        setModified(true);

    }

    /**
     * Get the StartDate
     *
     * @return Date
     */
    public Date getStartDate ()
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

        this.startDate = v;
        setModified(true);

    }

    /**
     * Get the EndDate
     *
     * @return Date
     */
    public Date getEndDate ()
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

        this.endDate = v;
        setModified(true);

    }

    /**
     * Get the SubmitterEmail
     *
     * @return String
     */
    public String getSubmitterEmail ()
    {
        return submitterEmail;
    }

    /**
     * Set the value of SubmitterEmail
     *
     * @param v new value
     */
    public void setSubmitterEmail(String v)
    {

        this.submitterEmail = v;
        setModified(true);

    }

    /**
     * Get the Created
     *
     * @return Date
     */
    public Date getCreated ()
    {
        return created;
    }

    /**
     * Set the value of Created
     *
     * @param v new value
     */
    public void setCreated(Date v)
    {

        this.created = v;
        setModified(true);

    }

    /**
     * Get the ActualStartDate
     *
     * @return Date
     */
    public Date getActualStartDate ()
    {
        return actualStartDate;
    }

    /**
     * Set the value of ActualStartDate
     *
     * @param v new value
     */
    public void setActualStartDate(Date v)
    {

        this.actualStartDate = v;
        setModified(true);

    }

    /**
     * Get the ActualEndDate
     *
     * @return Date
     */
    public Date getActualEndDate ()
    {
        return actualEndDate;
    }

    /**
     * Set the value of ActualEndDate
     *
     * @param v new value
     */
    public void setActualEndDate(Date v)
    {

        this.actualEndDate = v;
        setModified(true);

    }

    /**
     * Get the Level
     *
     * @return String
     */
    public String getLevel ()
    {
        return level;
    }

    /**
     * Set the value of Level
     *
     * @param v new value
     */
    public void setLevel(String v)
    {

        this.level = v;
        setModified(true);

    }

    /**
     * Get the AccessLevel
     *
     * @return Integer
     */
    public Integer getAccessLevel ()
    {
        return accessLevel;
    }

    /**
     * Set the value of AccessLevel
     *
     * @param v new value
     */
    public void setAccessLevel(Integer v)
    {

        this.accessLevel = v;
        setModified(true);

    }

    /**
     * Get the ArchiveLevel
     *
     * @return Integer
     */
    public Integer getArchiveLevel ()
    {
        return archiveLevel;
    }

    /**
     * Set the value of ArchiveLevel
     *
     * @param v new value
     */
    public void setArchiveLevel(Integer v)
    {

        this.archiveLevel = v;
        setModified(true);

    }

    /**
     * Get the EscalationLevel
     *
     * @return Integer
     */
    public Integer getEscalationLevel ()
    {
        return escalationLevel;
    }

    /**
     * Set the value of EscalationLevel
     *
     * @param v new value
     */
    public void setEscalationLevel(Integer v)
    {

        this.escalationLevel = v;
        setModified(true);

    }

    /**
     * Get the TaskIsMilestone
     *
     * @return String
     */
    public String getTaskIsMilestone ()
    {
        return taskIsMilestone;
    }

    /**
     * Set the value of TaskIsMilestone
     *
     * @param v new value
     */
    public void setTaskIsMilestone(String v)
    {

        this.taskIsMilestone = v;
        setModified(true);

    }

    /**
     * Get the TaskIsSubproject
     *
     * @return String
     */
    public String getTaskIsSubproject ()
    {
        return taskIsSubproject;
    }

    /**
     * Set the value of TaskIsSubproject
     *
     * @param v new value
     */
    public void setTaskIsSubproject(String v)
    {

        this.taskIsSubproject = v;
        setModified(true);

    }

    /**
     * Get the TaskIsSummary
     *
     * @return String
     */
    public String getTaskIsSummary ()
    {
        return taskIsSummary;
    }

    /**
     * Set the value of TaskIsSummary
     *
     * @param v new value
     */
    public void setTaskIsSummary(String v)
    {

        this.taskIsSummary = v;
        setModified(true);

    }

    /**
     * Get the TaskConstraint
     *
     * @return Integer
     */
    public Integer getTaskConstraint ()
    {
        return taskConstraint;
    }

    /**
     * Set the value of TaskConstraint
     *
     * @param v new value
     */
    public void setTaskConstraint(Integer v)
    {

        this.taskConstraint = v;
        setModified(true);

    }

    /**
     * Get the TaskConstraintDate
     *
     * @return Date
     */
    public Date getTaskConstraintDate ()
    {
        return taskConstraintDate;
    }

    /**
     * Set the value of TaskConstraintDate
     *
     * @param v new value
     */
    public void setTaskConstraintDate(Date v)
    {

        this.taskConstraintDate = v;
        setModified(true);

    }

    /**
     * Get the PSPCode
     *
     * @return String
     */
    public String getPSPCode ()
    {
        return pSPCode;
    }

    /**
     * Set the value of PSPCode
     *
     * @param v new value
     */
    public void setPSPCode(String v)
    {

        this.pSPCode = v;
        setModified(true);

    }

    /**
     * Get the IDNumber
     *
     * @return Integer
     */
    public Integer getIDNumber ()
    {
        return iDNumber;
    }

    /**
     * Set the value of IDNumber
     *
     * @param v new value
     */
    public void setIDNumber(Integer v)
    {

        this.iDNumber = v;
        setModified(true);

    }

    /**
     * Get the WBSOnLevel
     *
     * @return Integer
     */
    public Integer getWBSOnLevel ()
    {
        return wBSOnLevel;
    }

    /**
     * Set the value of WBSOnLevel
     *
     * @param v new value
     */
    public void setWBSOnLevel(Integer v)
    {

        this.wBSOnLevel = v;
        setModified(true);

    }

    /**
     * Get the ReminderDate
     *
     * @return Date
     */
    public Date getReminderDate ()
    {
        return reminderDate;
    }

    /**
     * Set the value of ReminderDate
     *
     * @param v new value
     */
    public void setReminderDate(Date v)
    {

        this.reminderDate = v;
        setModified(true);

    }

    /**
     * Get the TopDownStartDate
     *
     * @return Date
     */
    public Date getTopDownStartDate ()
    {
        return topDownStartDate;
    }

    /**
     * Set the value of TopDownStartDate
     *
     * @param v new value
     */
    public void setTopDownStartDate(Date v)
    {

        this.topDownStartDate = v;
        setModified(true);

    }

    /**
     * Get the TopDownEndDate
     *
     * @return Date
     */
    public Date getTopDownEndDate ()
    {
        return topDownEndDate;
    }

    /**
     * Set the value of TopDownEndDate
     *
     * @param v new value
     */
    public void setTopDownEndDate(Date v)
    {

        this.topDownEndDate = v;
        setModified(true);

    }

    /**
     * Get the OverBudget
     *
     * @return String
     */
    public String getOverBudget ()
    {
        return overBudget;
    }

    /**
     * Set the value of OverBudget
     *
     * @param v new value
     */
    public void setOverBudget(String v)
    {

        this.overBudget = v;
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

    



    private TPersonBean aTPersonBeanRelatedByOwnerID;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByOwnerID(TPersonBean v)
    {
        if (v == null)
        {
            setOwnerID((Integer) null);
        }
        else
        {
            setOwnerID(v.getObjectID());
        }
        aTPersonBeanRelatedByOwnerID = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByOwnerID()
    {
        return aTPersonBeanRelatedByOwnerID;
    }





    private TPersonBean aTPersonBeanRelatedByChangedByID;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByChangedByID(TPersonBean v)
    {
        if (v == null)
        {
            setChangedByID((Integer) null);
        }
        else
        {
            setChangedByID(v.getObjectID());
        }
        aTPersonBeanRelatedByChangedByID = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByChangedByID()
    {
        return aTPersonBeanRelatedByChangedByID;
    }





    private TPersonBean aTPersonBeanRelatedByOriginatorID;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByOriginatorID(TPersonBean v)
    {
        if (v == null)
        {
            setOriginatorID((Integer) null);
        }
        else
        {
            setOriginatorID(v.getObjectID());
        }
        aTPersonBeanRelatedByOriginatorID = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByOriginatorID()
    {
        return aTPersonBeanRelatedByOriginatorID;
    }





    private TPersonBean aTPersonBeanRelatedByResponsibleID;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByResponsibleID(TPersonBean v)
    {
        if (v == null)
        {
            setResponsibleID((Integer) null);
        }
        else
        {
            setResponsibleID(v.getObjectID());
        }
        aTPersonBeanRelatedByResponsibleID = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByResponsibleID()
    {
        return aTPersonBeanRelatedByResponsibleID;
    }





    private TProjectCategoryBean aTProjectCategoryBean;

    /**
     * sets an associated TProjectCategoryBean object
     *
     * @param v TProjectCategoryBean
     */
    public void setTProjectCategoryBean(TProjectCategoryBean v)
    {
        if (v == null)
        {
            setProjectCategoryID((Integer) null);
        }
        else
        {
            setProjectCategoryID(v.getObjectID());
        }
        aTProjectCategoryBean = v;
    }


    /**
     * Get the associated TProjectCategoryBean object
     *
     * @return the associated TProjectCategoryBean object
     */
    public TProjectCategoryBean getTProjectCategoryBean()
    {
        return aTProjectCategoryBean;
    }





    private TListTypeBean aTListTypeBean;

    /**
     * sets an associated TListTypeBean object
     *
     * @param v TListTypeBean
     */
    public void setTListTypeBean(TListTypeBean v)
    {
        if (v == null)
        {
            setListTypeID((Integer) null);
        }
        else
        {
            setListTypeID(v.getObjectID());
        }
        aTListTypeBean = v;
    }


    /**
     * Get the associated TListTypeBean object
     *
     * @return the associated TListTypeBean object
     */
    public TListTypeBean getTListTypeBean()
    {
        return aTListTypeBean;
    }





    private TClassBean aTClassBean;

    /**
     * sets an associated TClassBean object
     *
     * @param v TClassBean
     */
    public void setTClassBean(TClassBean v)
    {
        if (v == null)
        {
            setClassID((Integer) null);
        }
        else
        {
            setClassID(v.getObjectID());
        }
        aTClassBean = v;
    }


    /**
     * Get the associated TClassBean object
     *
     * @return the associated TClassBean object
     */
    public TClassBean getTClassBean()
    {
        return aTClassBean;
    }





    private TPriorityBean aTPriorityBean;

    /**
     * sets an associated TPriorityBean object
     *
     * @param v TPriorityBean
     */
    public void setTPriorityBean(TPriorityBean v)
    {
        if (v == null)
        {
            setPriorityID((Integer) null);
        }
        else
        {
            setPriorityID(v.getObjectID());
        }
        aTPriorityBean = v;
    }


    /**
     * Get the associated TPriorityBean object
     *
     * @return the associated TPriorityBean object
     */
    public TPriorityBean getTPriorityBean()
    {
        return aTPriorityBean;
    }





    private TSeverityBean aTSeverityBean;

    /**
     * sets an associated TSeverityBean object
     *
     * @param v TSeverityBean
     */
    public void setTSeverityBean(TSeverityBean v)
    {
        if (v == null)
        {
            setSeverityID((Integer) null);
        }
        else
        {
            setSeverityID(v.getObjectID());
        }
        aTSeverityBean = v;
    }


    /**
     * Get the associated TSeverityBean object
     *
     * @return the associated TSeverityBean object
     */
    public TSeverityBean getTSeverityBean()
    {
        return aTSeverityBean;
    }





    private TReleaseBean aTReleaseBeanRelatedByReleaseNoticedID;

    /**
     * sets an associated TReleaseBean object
     *
     * @param v TReleaseBean
     */
    public void setTReleaseBeanRelatedByReleaseNoticedID(TReleaseBean v)
    {
        if (v == null)
        {
            setReleaseNoticedID((Integer) null);
        }
        else
        {
            setReleaseNoticedID(v.getObjectID());
        }
        aTReleaseBeanRelatedByReleaseNoticedID = v;
    }


    /**
     * Get the associated TReleaseBean object
     *
     * @return the associated TReleaseBean object
     */
    public TReleaseBean getTReleaseBeanRelatedByReleaseNoticedID()
    {
        return aTReleaseBeanRelatedByReleaseNoticedID;
    }





    private TReleaseBean aTReleaseBeanRelatedByReleaseScheduledID;

    /**
     * sets an associated TReleaseBean object
     *
     * @param v TReleaseBean
     */
    public void setTReleaseBeanRelatedByReleaseScheduledID(TReleaseBean v)
    {
        if (v == null)
        {
            setReleaseScheduledID((Integer) null);
        }
        else
        {
            setReleaseScheduledID(v.getObjectID());
        }
        aTReleaseBeanRelatedByReleaseScheduledID = v;
    }


    /**
     * Get the associated TReleaseBean object
     *
     * @return the associated TReleaseBean object
     */
    public TReleaseBean getTReleaseBeanRelatedByReleaseScheduledID()
    {
        return aTReleaseBeanRelatedByReleaseScheduledID;
    }





    private TStateBean aTStateBean;

    /**
     * sets an associated TStateBean object
     *
     * @param v TStateBean
     */
    public void setTStateBean(TStateBean v)
    {
        if (v == null)
        {
            setStateID((Integer) null);
        }
        else
        {
            setStateID(v.getObjectID());
        }
        aTStateBean = v;
    }


    /**
     * Get the associated TStateBean object
     *
     * @return the associated TStateBean object
     */
    public TStateBean getTStateBean()
    {
        return aTStateBean;
    }





    private TProjectBean aTProjectBean;

    /**
     * sets an associated TProjectBean object
     *
     * @param v TProjectBean
     */
    public void setTProjectBean(TProjectBean v)
    {
        if (v == null)
        {
            setProjectID((Integer) null);
        }
        else
        {
            setProjectID(v.getObjectID());
        }
        aTProjectBean = v;
    }


    /**
     * Get the associated TProjectBean object
     *
     * @return the associated TProjectBean object
     */
    public TProjectBean getTProjectBean()
    {
        return aTProjectBean;
    }





    private TWorkItemBean aTWorkItemBeanRelatedBySuperiorworkitem;

    /**
     * sets an associated TWorkItemBean object
     *
     * @param v TWorkItemBean
     */
    public void setTWorkItemBeanRelatedBySuperiorworkitem(TWorkItemBean v)
    {
        if (v == null)
        {
            setSuperiorworkitem((Integer) null);
        }
        else
        {
            setSuperiorworkitem(v.getObjectID());
        }
        aTWorkItemBeanRelatedBySuperiorworkitem = v;
    }


    /**
     * Get the associated TWorkItemBean object
     *
     * @return the associated TWorkItemBean object
     */
    public TWorkItemBean getTWorkItemBeanRelatedBySuperiorworkitem()
    {
        return aTWorkItemBeanRelatedBySuperiorworkitem;
    }





    /**
     * Collection to store aggregation of collTBaseLineBeans
     */
    protected List<TBaseLineBean> collTBaseLineBeans;

    /**
     * Returns the collection of TBaseLineBeans
     */
    public List<TBaseLineBean> getTBaseLineBeans()
    {
        return collTBaseLineBeans;
    }

    /**
     * Sets the collection of TBaseLineBeans to the specified value
     */
    public void setTBaseLineBeans(List<TBaseLineBean> list)
    {
        if (list == null)
        {
            collTBaseLineBeans = null;
        }
        else
        {
            collTBaseLineBeans = new ArrayList<TBaseLineBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTNotifyBeans
     */
    protected List<TNotifyBean> collTNotifyBeans;

    /**
     * Returns the collection of TNotifyBeans
     */
    public List<TNotifyBean> getTNotifyBeans()
    {
        return collTNotifyBeans;
    }

    /**
     * Sets the collection of TNotifyBeans to the specified value
     */
    public void setTNotifyBeans(List<TNotifyBean> list)
    {
        if (list == null)
        {
            collTNotifyBeans = null;
        }
        else
        {
            collTNotifyBeans = new ArrayList<TNotifyBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTStateChangeBeans
     */
    protected List<TStateChangeBean> collTStateChangeBeans;

    /**
     * Returns the collection of TStateChangeBeans
     */
    public List<TStateChangeBean> getTStateChangeBeans()
    {
        return collTStateChangeBeans;
    }

    /**
     * Sets the collection of TStateChangeBeans to the specified value
     */
    public void setTStateChangeBeans(List<TStateChangeBean> list)
    {
        if (list == null)
        {
            collTStateChangeBeans = null;
        }
        else
        {
            collTStateChangeBeans = new ArrayList<TStateChangeBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTTrailBeans
     */
    protected List<TTrailBean> collTTrailBeans;

    /**
     * Returns the collection of TTrailBeans
     */
    public List<TTrailBean> getTTrailBeans()
    {
        return collTTrailBeans;
    }

    /**
     * Sets the collection of TTrailBeans to the specified value
     */
    public void setTTrailBeans(List<TTrailBean> list)
    {
        if (list == null)
        {
            collTTrailBeans = null;
        }
        else
        {
            collTTrailBeans = new ArrayList<TTrailBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTComputedValuesBeans
     */
    protected List<TComputedValuesBean> collTComputedValuesBeans;

    /**
     * Returns the collection of TComputedValuesBeans
     */
    public List<TComputedValuesBean> getTComputedValuesBeans()
    {
        return collTComputedValuesBeans;
    }

    /**
     * Sets the collection of TComputedValuesBeans to the specified value
     */
    public void setTComputedValuesBeans(List<TComputedValuesBean> list)
    {
        if (list == null)
        {
            collTComputedValuesBeans = null;
        }
        else
        {
            collTComputedValuesBeans = new ArrayList<TComputedValuesBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTAttachmentBeans
     */
    protected List<TAttachmentBean> collTAttachmentBeans;

    /**
     * Returns the collection of TAttachmentBeans
     */
    public List<TAttachmentBean> getTAttachmentBeans()
    {
        return collTAttachmentBeans;
    }

    /**
     * Sets the collection of TAttachmentBeans to the specified value
     */
    public void setTAttachmentBeans(List<TAttachmentBean> list)
    {
        if (list == null)
        {
            collTAttachmentBeans = null;
        }
        else
        {
            collTAttachmentBeans = new ArrayList<TAttachmentBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTCostBeans
     */
    protected List<TCostBean> collTCostBeans;

    /**
     * Returns the collection of TCostBeans
     */
    public List<TCostBean> getTCostBeans()
    {
        return collTCostBeans;
    }

    /**
     * Sets the collection of TCostBeans to the specified value
     */
    public void setTCostBeans(List<TCostBean> list)
    {
        if (list == null)
        {
            collTCostBeans = null;
        }
        else
        {
            collTCostBeans = new ArrayList<TCostBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTIssueAttributeValueBeans
     */
    protected List<TIssueAttributeValueBean> collTIssueAttributeValueBeans;

    /**
     * Returns the collection of TIssueAttributeValueBeans
     */
    public List<TIssueAttributeValueBean> getTIssueAttributeValueBeans()
    {
        return collTIssueAttributeValueBeans;
    }

    /**
     * Sets the collection of TIssueAttributeValueBeans to the specified value
     */
    public void setTIssueAttributeValueBeans(List<TIssueAttributeValueBean> list)
    {
        if (list == null)
        {
            collTIssueAttributeValueBeans = null;
        }
        else
        {
            collTIssueAttributeValueBeans = new ArrayList<TIssueAttributeValueBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTBudgetBeans
     */
    protected List<TBudgetBean> collTBudgetBeans;

    /**
     * Returns the collection of TBudgetBeans
     */
    public List<TBudgetBean> getTBudgetBeans()
    {
        return collTBudgetBeans;
    }

    /**
     * Sets the collection of TBudgetBeans to the specified value
     */
    public void setTBudgetBeans(List<TBudgetBean> list)
    {
        if (list == null)
        {
            collTBudgetBeans = null;
        }
        else
        {
            collTBudgetBeans = new ArrayList<TBudgetBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTActualEstimatedBudgetBeans
     */
    protected List<TActualEstimatedBudgetBean> collTActualEstimatedBudgetBeans;

    /**
     * Returns the collection of TActualEstimatedBudgetBeans
     */
    public List<TActualEstimatedBudgetBean> getTActualEstimatedBudgetBeans()
    {
        return collTActualEstimatedBudgetBeans;
    }

    /**
     * Sets the collection of TActualEstimatedBudgetBeans to the specified value
     */
    public void setTActualEstimatedBudgetBeans(List<TActualEstimatedBudgetBean> list)
    {
        if (list == null)
        {
            collTActualEstimatedBudgetBeans = null;
        }
        else
        {
            collTActualEstimatedBudgetBeans = new ArrayList<TActualEstimatedBudgetBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTAttributeValueBeans
     */
    protected List<TAttributeValueBean> collTAttributeValueBeans;

    /**
     * Returns the collection of TAttributeValueBeans
     */
    public List<TAttributeValueBean> getTAttributeValueBeans()
    {
        return collTAttributeValueBeans;
    }

    /**
     * Sets the collection of TAttributeValueBeans to the specified value
     */
    public void setTAttributeValueBeans(List<TAttributeValueBean> list)
    {
        if (list == null)
        {
            collTAttributeValueBeans = null;
        }
        else
        {
            collTAttributeValueBeans = new ArrayList<TAttributeValueBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkItemLinkBeansRelatedByLinkPred
     */
    protected List<TWorkItemLinkBean> collTWorkItemLinkBeansRelatedByLinkPred;

    /**
     * Returns the collection of TWorkItemLinkBeansRelatedByLinkPred
     */
    public List<TWorkItemLinkBean> getTWorkItemLinkBeansRelatedByLinkPred()
    {
        return collTWorkItemLinkBeansRelatedByLinkPred;
    }

    /**
     * Sets the collection of TWorkItemLinkBeansRelatedByLinkPred to the specified value
     */
    public void setTWorkItemLinkBeansRelatedByLinkPred(List<TWorkItemLinkBean> list)
    {
        if (list == null)
        {
            collTWorkItemLinkBeansRelatedByLinkPred = null;
        }
        else
        {
            collTWorkItemLinkBeansRelatedByLinkPred = new ArrayList<TWorkItemLinkBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkItemLinkBeansRelatedByLinkSucc
     */
    protected List<TWorkItemLinkBean> collTWorkItemLinkBeansRelatedByLinkSucc;

    /**
     * Returns the collection of TWorkItemLinkBeansRelatedByLinkSucc
     */
    public List<TWorkItemLinkBean> getTWorkItemLinkBeansRelatedByLinkSucc()
    {
        return collTWorkItemLinkBeansRelatedByLinkSucc;
    }

    /**
     * Sets the collection of TWorkItemLinkBeansRelatedByLinkSucc to the specified value
     */
    public void setTWorkItemLinkBeansRelatedByLinkSucc(List<TWorkItemLinkBean> list)
    {
        if (list == null)
        {
            collTWorkItemLinkBeansRelatedByLinkSucc = null;
        }
        else
        {
            collTWorkItemLinkBeansRelatedByLinkSucc = new ArrayList<TWorkItemLinkBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkItemLockBeans
     */
    protected List<TWorkItemLockBean> collTWorkItemLockBeans;

    /**
     * Returns the collection of TWorkItemLockBeans
     */
    public List<TWorkItemLockBean> getTWorkItemLockBeans()
    {
        return collTWorkItemLockBeans;
    }

    /**
     * Sets the collection of TWorkItemLockBeans to the specified value
     */
    public void setTWorkItemLockBeans(List<TWorkItemLockBean> list)
    {
        if (list == null)
        {
            collTWorkItemLockBeans = null;
        }
        else
        {
            collTWorkItemLockBeans = new ArrayList<TWorkItemLockBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTSummaryMailBeans
     */
    protected List<TSummaryMailBean> collTSummaryMailBeans;

    /**
     * Returns the collection of TSummaryMailBeans
     */
    public List<TSummaryMailBean> getTSummaryMailBeans()
    {
        return collTSummaryMailBeans;
    }

    /**
     * Sets the collection of TSummaryMailBeans to the specified value
     */
    public void setTSummaryMailBeans(List<TSummaryMailBean> list)
    {
        if (list == null)
        {
            collTSummaryMailBeans = null;
        }
        else
        {
            collTSummaryMailBeans = new ArrayList<TSummaryMailBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTHistoryTransactionBeans
     */
    protected List<THistoryTransactionBean> collTHistoryTransactionBeans;

    /**
     * Returns the collection of THistoryTransactionBeans
     */
    public List<THistoryTransactionBean> getTHistoryTransactionBeans()
    {
        return collTHistoryTransactionBeans;
    }

    /**
     * Sets the collection of THistoryTransactionBeans to the specified value
     */
    public void setTHistoryTransactionBeans(List<THistoryTransactionBean> list)
    {
        if (list == null)
        {
            collTHistoryTransactionBeans = null;
        }
        else
        {
            collTHistoryTransactionBeans = new ArrayList<THistoryTransactionBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTMSProjectTaskBeans
     */
    protected List<TMSProjectTaskBean> collTMSProjectTaskBeans;

    /**
     * Returns the collection of TMSProjectTaskBeans
     */
    public List<TMSProjectTaskBean> getTMSProjectTaskBeans()
    {
        return collTMSProjectTaskBeans;
    }

    /**
     * Sets the collection of TMSProjectTaskBeans to the specified value
     */
    public void setTMSProjectTaskBeans(List<TMSProjectTaskBean> list)
    {
        if (list == null)
        {
            collTMSProjectTaskBeans = null;
        }
        else
        {
            collTMSProjectTaskBeans = new ArrayList<TMSProjectTaskBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTPersonBasketBeans
     */
    protected List<TPersonBasketBean> collTPersonBasketBeans;

    /**
     * Returns the collection of TPersonBasketBeans
     */
    public List<TPersonBasketBean> getTPersonBasketBeans()
    {
        return collTPersonBasketBeans;
    }

    /**
     * Sets the collection of TPersonBasketBeans to the specified value
     */
    public void setTPersonBasketBeans(List<TPersonBasketBean> list)
    {
        if (list == null)
        {
            collTPersonBasketBeans = null;
        }
        else
        {
            collTPersonBasketBeans = new ArrayList<TPersonBasketBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTLastVisitedItemBeans
     */
    protected List<TLastVisitedItemBean> collTLastVisitedItemBeans;

    /**
     * Returns the collection of TLastVisitedItemBeans
     */
    public List<TLastVisitedItemBean> getTLastVisitedItemBeans()
    {
        return collTLastVisitedItemBeans;
    }

    /**
     * Sets the collection of TLastVisitedItemBeans to the specified value
     */
    public void setTLastVisitedItemBeans(List<TLastVisitedItemBean> list)
    {
        if (list == null)
        {
            collTLastVisitedItemBeans = null;
        }
        else
        {
            collTLastVisitedItemBeans = new ArrayList<TLastVisitedItemBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTReadIssueBeans
     */
    protected List<TReadIssueBean> collTReadIssueBeans;

    /**
     * Returns the collection of TReadIssueBeans
     */
    public List<TReadIssueBean> getTReadIssueBeans()
    {
        return collTReadIssueBeans;
    }

    /**
     * Sets the collection of TReadIssueBeans to the specified value
     */
    public void setTReadIssueBeans(List<TReadIssueBean> list)
    {
        if (list == null)
        {
            collTReadIssueBeans = null;
        }
        else
        {
            collTReadIssueBeans = new ArrayList<TReadIssueBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTAttachmentVersionBeans
     */
    protected List<TAttachmentVersionBean> collTAttachmentVersionBeans;

    /**
     * Returns the collection of TAttachmentVersionBeans
     */
    public List<TAttachmentVersionBean> getTAttachmentVersionBeans()
    {
        return collTAttachmentVersionBeans;
    }

    /**
     * Sets the collection of TAttachmentVersionBeans to the specified value
     */
    public void setTAttachmentVersionBeans(List<TAttachmentVersionBean> list)
    {
        if (list == null)
        {
            collTAttachmentVersionBeans = null;
        }
        else
        {
            collTAttachmentVersionBeans = new ArrayList<TAttachmentVersionBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTItemTransitionBeans
     */
    protected List<TItemTransitionBean> collTItemTransitionBeans;

    /**
     * Returns the collection of TItemTransitionBeans
     */
    public List<TItemTransitionBean> getTItemTransitionBeans()
    {
        return collTItemTransitionBeans;
    }

    /**
     * Sets the collection of TItemTransitionBeans to the specified value
     */
    public void setTItemTransitionBeans(List<TItemTransitionBean> list)
    {
        if (list == null)
        {
            collTItemTransitionBeans = null;
        }
        else
        {
            collTItemTransitionBeans = new ArrayList<TItemTransitionBean>(list);
        }
    }

}
