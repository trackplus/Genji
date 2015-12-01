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



import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TProjectCategory;
import com.aurel.track.persist.TProjectCategoryPeer;
import com.aurel.track.persist.TListType;
import com.aurel.track.persist.TListTypePeer;
import com.aurel.track.persist.TClass;
import com.aurel.track.persist.TClassPeer;
import com.aurel.track.persist.TPriority;
import com.aurel.track.persist.TPriorityPeer;
import com.aurel.track.persist.TSeverity;
import com.aurel.track.persist.TSeverityPeer;
import com.aurel.track.persist.TWorkItem;
import com.aurel.track.persist.TWorkItemPeer;
import com.aurel.track.persist.TRelease;
import com.aurel.track.persist.TReleasePeer;
import com.aurel.track.persist.TRelease;
import com.aurel.track.persist.TReleasePeer;
import com.aurel.track.persist.TState;
import com.aurel.track.persist.TStatePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectCategoryBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TClassBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;

import com.aurel.track.beans.TBaseLineBean;
import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TStateChangeBean;
import com.aurel.track.beans.TTrailBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TIssueAttributeValueBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.beans.TWorkItemLockBean;
import com.aurel.track.beans.TSummaryMailBean;
import com.aurel.track.beans.THistoryTransactionBean;
import com.aurel.track.beans.TMSProjectTaskBean;
import com.aurel.track.beans.TPersonBasketBean;
import com.aurel.track.beans.TLastVisitedItemBean;
import com.aurel.track.beans.TReadIssueBean;
import com.aurel.track.beans.TAttachmentVersionBean;
import com.aurel.track.beans.TItemTransitionBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TWorkItem
 */
public abstract class BaseTWorkItem extends TpBaseObject
{
    /** The Peer class */
    private static final TWorkItemPeer peer =
        new TWorkItemPeer();


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



        // update associated TBaseLine
        if (collTBaseLines != null)
        {
            for (int i = 0; i < collTBaseLines.size(); i++)
            {
                ((TBaseLine) collTBaseLines.get(i))
                        .setWorkItemID(v);
            }
        }

        // update associated TNotify
        if (collTNotifys != null)
        {
            for (int i = 0; i < collTNotifys.size(); i++)
            {
                ((TNotify) collTNotifys.get(i))
                        .setWorkItem(v);
            }
        }

        // update associated TStateChange
        if (collTStateChanges != null)
        {
            for (int i = 0; i < collTStateChanges.size(); i++)
            {
                ((TStateChange) collTStateChanges.get(i))
                        .setWorkItemID(v);
            }
        }

        // update associated TTrail
        if (collTTrails != null)
        {
            for (int i = 0; i < collTTrails.size(); i++)
            {
                ((TTrail) collTTrails.get(i))
                        .setWorkItemID(v);
            }
        }

        // update associated TComputedValues
        if (collTComputedValuess != null)
        {
            for (int i = 0; i < collTComputedValuess.size(); i++)
            {
                ((TComputedValues) collTComputedValuess.get(i))
                        .setWorkitemKey(v);
            }
        }

        // update associated TAttachment
        if (collTAttachments != null)
        {
            for (int i = 0; i < collTAttachments.size(); i++)
            {
                ((TAttachment) collTAttachments.get(i))
                        .setWorkItem(v);
            }
        }

        // update associated TCost
        if (collTCosts != null)
        {
            for (int i = 0; i < collTCosts.size(); i++)
            {
                ((TCost) collTCosts.get(i))
                        .setWorkitem(v);
            }
        }

        // update associated TIssueAttributeValue
        if (collTIssueAttributeValues != null)
        {
            for (int i = 0; i < collTIssueAttributeValues.size(); i++)
            {
                ((TIssueAttributeValue) collTIssueAttributeValues.get(i))
                        .setWorkItem(v);
            }
        }

        // update associated TBudget
        if (collTBudgets != null)
        {
            for (int i = 0; i < collTBudgets.size(); i++)
            {
                ((TBudget) collTBudgets.get(i))
                        .setWorkItemID(v);
            }
        }

        // update associated TActualEstimatedBudget
        if (collTActualEstimatedBudgets != null)
        {
            for (int i = 0; i < collTActualEstimatedBudgets.size(); i++)
            {
                ((TActualEstimatedBudget) collTActualEstimatedBudgets.get(i))
                        .setWorkItemID(v);
            }
        }

        // update associated TAttributeValue
        if (collTAttributeValues != null)
        {
            for (int i = 0; i < collTAttributeValues.size(); i++)
            {
                ((TAttributeValue) collTAttributeValues.get(i))
                        .setWorkItem(v);
            }
        }

        // update associated TWorkItemLink
        if (collTWorkItemLinksRelatedByLinkPred != null)
        {
            for (int i = 0; i < collTWorkItemLinksRelatedByLinkPred.size(); i++)
            {
                ((TWorkItemLink) collTWorkItemLinksRelatedByLinkPred.get(i))
                        .setLinkPred(v);
            }
        }

        // update associated TWorkItemLink
        if (collTWorkItemLinksRelatedByLinkSucc != null)
        {
            for (int i = 0; i < collTWorkItemLinksRelatedByLinkSucc.size(); i++)
            {
                ((TWorkItemLink) collTWorkItemLinksRelatedByLinkSucc.get(i))
                        .setLinkSucc(v);
            }
        }

        // update associated TWorkItemLock
        if (collTWorkItemLocks != null)
        {
            for (int i = 0; i < collTWorkItemLocks.size(); i++)
            {
                ((TWorkItemLock) collTWorkItemLocks.get(i))
                        .setWorkItem(v);
            }
        }

        // update associated TSummaryMail
        if (collTSummaryMails != null)
        {
            for (int i = 0; i < collTSummaryMails.size(); i++)
            {
                ((TSummaryMail) collTSummaryMails.get(i))
                        .setWorkItem(v);
            }
        }

        // update associated THistoryTransaction
        if (collTHistoryTransactions != null)
        {
            for (int i = 0; i < collTHistoryTransactions.size(); i++)
            {
                ((THistoryTransaction) collTHistoryTransactions.get(i))
                        .setWorkItem(v);
            }
        }

        // update associated TMSProjectTask
        if (collTMSProjectTasks != null)
        {
            for (int i = 0; i < collTMSProjectTasks.size(); i++)
            {
                ((TMSProjectTask) collTMSProjectTasks.get(i))
                        .setWorkitem(v);
            }
        }

        // update associated TPersonBasket
        if (collTPersonBaskets != null)
        {
            for (int i = 0; i < collTPersonBaskets.size(); i++)
            {
                ((TPersonBasket) collTPersonBaskets.get(i))
                        .setWorkItem(v);
            }
        }

        // update associated TLastVisitedItem
        if (collTLastVisitedItems != null)
        {
            for (int i = 0; i < collTLastVisitedItems.size(); i++)
            {
                ((TLastVisitedItem) collTLastVisitedItems.get(i))
                        .setWorkItem(v);
            }
        }

        // update associated TReadIssue
        if (collTReadIssues != null)
        {
            for (int i = 0; i < collTReadIssues.size(); i++)
            {
                ((TReadIssue) collTReadIssues.get(i))
                        .setWorkItem(v);
            }
        }

        // update associated TAttachmentVersion
        if (collTAttachmentVersions != null)
        {
            for (int i = 0; i < collTAttachmentVersions.size(); i++)
            {
                ((TAttachmentVersion) collTAttachmentVersions.get(i))
                        .setWorkItem(v);
            }
        }

        // update associated TItemTransition
        if (collTItemTransitions != null)
        {
            for (int i = 0; i < collTItemTransitions.size(); i++)
            {
                ((TItemTransition) collTItemTransitions.get(i))
                        .setWorkItem(v);
            }
        }
    }

    /**
     * Get the OwnerID
     *
     * @return Integer
     */
    public Integer getOwnerID()
    {
        return ownerID;
    }


    /**
     * Set the value of OwnerID
     *
     * @param v new value
     */
    public void setOwnerID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.ownerID, v))
        {
            this.ownerID = v;
            setModified(true);
        }


        if (aTPersonRelatedByOwnerID != null && !ObjectUtils.equals(aTPersonRelatedByOwnerID.getObjectID(), v))
        {
            aTPersonRelatedByOwnerID = null;
        }

    }

    /**
     * Get the ChangedByID
     *
     * @return Integer
     */
    public Integer getChangedByID()
    {
        return changedByID;
    }


    /**
     * Set the value of ChangedByID
     *
     * @param v new value
     */
    public void setChangedByID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.changedByID, v))
        {
            this.changedByID = v;
            setModified(true);
        }


        if (aTPersonRelatedByChangedByID != null && !ObjectUtils.equals(aTPersonRelatedByChangedByID.getObjectID(), v))
        {
            aTPersonRelatedByChangedByID = null;
        }

    }

    /**
     * Get the OriginatorID
     *
     * @return Integer
     */
    public Integer getOriginatorID()
    {
        return originatorID;
    }


    /**
     * Set the value of OriginatorID
     *
     * @param v new value
     */
    public void setOriginatorID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.originatorID, v))
        {
            this.originatorID = v;
            setModified(true);
        }


        if (aTPersonRelatedByOriginatorID != null && !ObjectUtils.equals(aTPersonRelatedByOriginatorID.getObjectID(), v))
        {
            aTPersonRelatedByOriginatorID = null;
        }

    }

    /**
     * Get the ResponsibleID
     *
     * @return Integer
     */
    public Integer getResponsibleID()
    {
        return responsibleID;
    }


    /**
     * Set the value of ResponsibleID
     *
     * @param v new value
     */
    public void setResponsibleID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.responsibleID, v))
        {
            this.responsibleID = v;
            setModified(true);
        }


        if (aTPersonRelatedByResponsibleID != null && !ObjectUtils.equals(aTPersonRelatedByResponsibleID.getObjectID(), v))
        {
            aTPersonRelatedByResponsibleID = null;
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
     * Get the ProjectCategoryID
     *
     * @return Integer
     */
    public Integer getProjectCategoryID()
    {
        return projectCategoryID;
    }


    /**
     * Set the value of ProjectCategoryID
     *
     * @param v new value
     */
    public void setProjectCategoryID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.projectCategoryID, v))
        {
            this.projectCategoryID = v;
            setModified(true);
        }


        if (aTProjectCategory != null && !ObjectUtils.equals(aTProjectCategory.getObjectID(), v))
        {
            aTProjectCategory = null;
        }

    }

    /**
     * Get the ListTypeID
     *
     * @return Integer
     */
    public Integer getListTypeID()
    {
        return listTypeID;
    }


    /**
     * Set the value of ListTypeID
     *
     * @param v new value
     */
    public void setListTypeID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.listTypeID, v))
        {
            this.listTypeID = v;
            setModified(true);
        }


        if (aTListType != null && !ObjectUtils.equals(aTListType.getObjectID(), v))
        {
            aTListType = null;
        }

    }

    /**
     * Get the ClassID
     *
     * @return Integer
     */
    public Integer getClassID()
    {
        return classID;
    }


    /**
     * Set the value of ClassID
     *
     * @param v new value
     */
    public void setClassID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.classID, v))
        {
            this.classID = v;
            setModified(true);
        }


        if (aTClass != null && !ObjectUtils.equals(aTClass.getObjectID(), v))
        {
            aTClass = null;
        }

    }

    /**
     * Get the PriorityID
     *
     * @return Integer
     */
    public Integer getPriorityID()
    {
        return priorityID;
    }


    /**
     * Set the value of PriorityID
     *
     * @param v new value
     */
    public void setPriorityID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.priorityID, v))
        {
            this.priorityID = v;
            setModified(true);
        }


        if (aTPriority != null && !ObjectUtils.equals(aTPriority.getObjectID(), v))
        {
            aTPriority = null;
        }

    }

    /**
     * Get the SeverityID
     *
     * @return Integer
     */
    public Integer getSeverityID()
    {
        return severityID;
    }


    /**
     * Set the value of SeverityID
     *
     * @param v new value
     */
    public void setSeverityID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.severityID, v))
        {
            this.severityID = v;
            setModified(true);
        }


        if (aTSeverity != null && !ObjectUtils.equals(aTSeverity.getObjectID(), v))
        {
            aTSeverity = null;
        }

    }

    /**
     * Get the Superiorworkitem
     *
     * @return Integer
     */
    public Integer getSuperiorworkitem()
    {
        return superiorworkitem;
    }


    /**
     * Set the value of Superiorworkitem
     *
     * @param v new value
     */
    public void setSuperiorworkitem(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.superiorworkitem, v))
        {
            this.superiorworkitem = v;
            setModified(true);
        }


        if (aTWorkItemRelatedBySuperiorworkitem != null && !ObjectUtils.equals(aTWorkItemRelatedBySuperiorworkitem.getObjectID(), v))
        {
            aTWorkItemRelatedBySuperiorworkitem = null;
        }

    }

    /**
     * Get the Synopsis
     *
     * @return String
     */
    public String getSynopsis()
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

        if (!ObjectUtils.equals(this.synopsis, v))
        {
            this.synopsis = v;
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
     * Get the Reference
     *
     * @return String
     */
    public String getReference()
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

        if (!ObjectUtils.equals(this.reference, v))
        {
            this.reference = v;
            setModified(true);
        }


    }

    /**
     * Get the LastEdit
     *
     * @return Date
     */
    public Date getLastEdit()
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

        if (!ObjectUtils.equals(this.lastEdit, v))
        {
            this.lastEdit = v;
            setModified(true);
        }


    }

    /**
     * Get the ReleaseNoticedID
     *
     * @return Integer
     */
    public Integer getReleaseNoticedID()
    {
        return releaseNoticedID;
    }


    /**
     * Set the value of ReleaseNoticedID
     *
     * @param v new value
     */
    public void setReleaseNoticedID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.releaseNoticedID, v))
        {
            this.releaseNoticedID = v;
            setModified(true);
        }


        if (aTReleaseRelatedByReleaseNoticedID != null && !ObjectUtils.equals(aTReleaseRelatedByReleaseNoticedID.getObjectID(), v))
        {
            aTReleaseRelatedByReleaseNoticedID = null;
        }

    }

    /**
     * Get the ReleaseScheduledID
     *
     * @return Integer
     */
    public Integer getReleaseScheduledID()
    {
        return releaseScheduledID;
    }


    /**
     * Set the value of ReleaseScheduledID
     *
     * @param v new value
     */
    public void setReleaseScheduledID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.releaseScheduledID, v))
        {
            this.releaseScheduledID = v;
            setModified(true);
        }


        if (aTReleaseRelatedByReleaseScheduledID != null && !ObjectUtils.equals(aTReleaseRelatedByReleaseScheduledID.getObjectID(), v))
        {
            aTReleaseRelatedByReleaseScheduledID = null;
        }

    }

    /**
     * Get the Build
     *
     * @return String
     */
    public String getBuild()
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

        if (!ObjectUtils.equals(this.build, v))
        {
            this.build = v;
            setModified(true);
        }


    }

    /**
     * Get the StateID
     *
     * @return Integer
     */
    public Integer getStateID()
    {
        return stateID;
    }


    /**
     * Set the value of StateID
     *
     * @param v new value
     */
    public void setStateID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.stateID, v))
        {
            this.stateID = v;
            setModified(true);
        }


        if (aTState != null && !ObjectUtils.equals(aTState.getObjectID(), v))
        {
            aTState = null;
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
     * Get the SubmitterEmail
     *
     * @return String
     */
    public String getSubmitterEmail()
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

        if (!ObjectUtils.equals(this.submitterEmail, v))
        {
            this.submitterEmail = v;
            setModified(true);
        }


    }

    /**
     * Get the Created
     *
     * @return Date
     */
    public Date getCreated()
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

        if (!ObjectUtils.equals(this.created, v))
        {
            this.created = v;
            setModified(true);
        }


    }

    /**
     * Get the ActualStartDate
     *
     * @return Date
     */
    public Date getActualStartDate()
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

        if (!ObjectUtils.equals(this.actualStartDate, v))
        {
            this.actualStartDate = v;
            setModified(true);
        }


    }

    /**
     * Get the ActualEndDate
     *
     * @return Date
     */
    public Date getActualEndDate()
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

        if (!ObjectUtils.equals(this.actualEndDate, v))
        {
            this.actualEndDate = v;
            setModified(true);
        }


    }

    /**
     * Get the Level
     *
     * @return String
     */
    public String getLevel()
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

        if (!ObjectUtils.equals(this.level, v))
        {
            this.level = v;
            setModified(true);
        }


    }

    /**
     * Get the AccessLevel
     *
     * @return Integer
     */
    public Integer getAccessLevel()
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

        if (!ObjectUtils.equals(this.accessLevel, v))
        {
            this.accessLevel = v;
            setModified(true);
        }


    }

    /**
     * Get the ArchiveLevel
     *
     * @return Integer
     */
    public Integer getArchiveLevel()
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

        if (!ObjectUtils.equals(this.archiveLevel, v))
        {
            this.archiveLevel = v;
            setModified(true);
        }


    }

    /**
     * Get the EscalationLevel
     *
     * @return Integer
     */
    public Integer getEscalationLevel()
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

        if (!ObjectUtils.equals(this.escalationLevel, v))
        {
            this.escalationLevel = v;
            setModified(true);
        }


    }

    /**
     * Get the TaskIsMilestone
     *
     * @return String
     */
    public String getTaskIsMilestone()
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

        if (!ObjectUtils.equals(this.taskIsMilestone, v))
        {
            this.taskIsMilestone = v;
            setModified(true);
        }


    }

    /**
     * Get the TaskIsSubproject
     *
     * @return String
     */
    public String getTaskIsSubproject()
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

        if (!ObjectUtils.equals(this.taskIsSubproject, v))
        {
            this.taskIsSubproject = v;
            setModified(true);
        }


    }

    /**
     * Get the TaskIsSummary
     *
     * @return String
     */
    public String getTaskIsSummary()
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

        if (!ObjectUtils.equals(this.taskIsSummary, v))
        {
            this.taskIsSummary = v;
            setModified(true);
        }


    }

    /**
     * Get the TaskConstraint
     *
     * @return Integer
     */
    public Integer getTaskConstraint()
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

        if (!ObjectUtils.equals(this.taskConstraint, v))
        {
            this.taskConstraint = v;
            setModified(true);
        }


    }

    /**
     * Get the TaskConstraintDate
     *
     * @return Date
     */
    public Date getTaskConstraintDate()
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

        if (!ObjectUtils.equals(this.taskConstraintDate, v))
        {
            this.taskConstraintDate = v;
            setModified(true);
        }


    }

    /**
     * Get the PSPCode
     *
     * @return String
     */
    public String getPSPCode()
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

        if (!ObjectUtils.equals(this.pSPCode, v))
        {
            this.pSPCode = v;
            setModified(true);
        }


    }

    /**
     * Get the IDNumber
     *
     * @return Integer
     */
    public Integer getIDNumber()
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

        if (!ObjectUtils.equals(this.iDNumber, v))
        {
            this.iDNumber = v;
            setModified(true);
        }


    }

    /**
     * Get the WBSOnLevel
     *
     * @return Integer
     */
    public Integer getWBSOnLevel()
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

        if (!ObjectUtils.equals(this.wBSOnLevel, v))
        {
            this.wBSOnLevel = v;
            setModified(true);
        }


    }

    /**
     * Get the ReminderDate
     *
     * @return Date
     */
    public Date getReminderDate()
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

        if (!ObjectUtils.equals(this.reminderDate, v))
        {
            this.reminderDate = v;
            setModified(true);
        }


    }

    /**
     * Get the TopDownStartDate
     *
     * @return Date
     */
    public Date getTopDownStartDate()
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

        if (!ObjectUtils.equals(this.topDownStartDate, v))
        {
            this.topDownStartDate = v;
            setModified(true);
        }


    }

    /**
     * Get the TopDownEndDate
     *
     * @return Date
     */
    public Date getTopDownEndDate()
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

        if (!ObjectUtils.equals(this.topDownEndDate, v))
        {
            this.topDownEndDate = v;
            setModified(true);
        }


    }

    /**
     * Get the OverBudget
     *
     * @return String
     */
    public String getOverBudget()
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

        if (!ObjectUtils.equals(this.overBudget, v))
        {
            this.overBudget = v;
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

    



    private TPerson aTPersonRelatedByOwnerID;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByOwnerID(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setOwnerID((Integer) null);
        }
        else
        {
            setOwnerID(v.getObjectID());
        }
        aTPersonRelatedByOwnerID = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByOwnerID()
        throws TorqueException
    {
        if (aTPersonRelatedByOwnerID == null && (!ObjectUtils.equals(this.ownerID, null)))
        {
            aTPersonRelatedByOwnerID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.ownerID));
        }
        return aTPersonRelatedByOwnerID;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByOwnerID(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByOwnerID == null && (!ObjectUtils.equals(this.ownerID, null)))
        {
            aTPersonRelatedByOwnerID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.ownerID), connection);
        }
        return aTPersonRelatedByOwnerID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByOwnerIDKey(ObjectKey key) throws TorqueException
    {

        setOwnerID(new Integer(((NumberKey) key).intValue()));
    }




    private TPerson aTPersonRelatedByChangedByID;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByChangedByID(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setChangedByID((Integer) null);
        }
        else
        {
            setChangedByID(v.getObjectID());
        }
        aTPersonRelatedByChangedByID = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByChangedByID()
        throws TorqueException
    {
        if (aTPersonRelatedByChangedByID == null && (!ObjectUtils.equals(this.changedByID, null)))
        {
            aTPersonRelatedByChangedByID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.changedByID));
        }
        return aTPersonRelatedByChangedByID;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByChangedByID(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByChangedByID == null && (!ObjectUtils.equals(this.changedByID, null)))
        {
            aTPersonRelatedByChangedByID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.changedByID), connection);
        }
        return aTPersonRelatedByChangedByID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByChangedByIDKey(ObjectKey key) throws TorqueException
    {

        setChangedByID(new Integer(((NumberKey) key).intValue()));
    }




    private TPerson aTPersonRelatedByOriginatorID;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByOriginatorID(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setOriginatorID((Integer) null);
        }
        else
        {
            setOriginatorID(v.getObjectID());
        }
        aTPersonRelatedByOriginatorID = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByOriginatorID()
        throws TorqueException
    {
        if (aTPersonRelatedByOriginatorID == null && (!ObjectUtils.equals(this.originatorID, null)))
        {
            aTPersonRelatedByOriginatorID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.originatorID));
        }
        return aTPersonRelatedByOriginatorID;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByOriginatorID(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByOriginatorID == null && (!ObjectUtils.equals(this.originatorID, null)))
        {
            aTPersonRelatedByOriginatorID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.originatorID), connection);
        }
        return aTPersonRelatedByOriginatorID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByOriginatorIDKey(ObjectKey key) throws TorqueException
    {

        setOriginatorID(new Integer(((NumberKey) key).intValue()));
    }




    private TPerson aTPersonRelatedByResponsibleID;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByResponsibleID(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setResponsibleID((Integer) null);
        }
        else
        {
            setResponsibleID(v.getObjectID());
        }
        aTPersonRelatedByResponsibleID = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByResponsibleID()
        throws TorqueException
    {
        if (aTPersonRelatedByResponsibleID == null && (!ObjectUtils.equals(this.responsibleID, null)))
        {
            aTPersonRelatedByResponsibleID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.responsibleID));
        }
        return aTPersonRelatedByResponsibleID;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByResponsibleID(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByResponsibleID == null && (!ObjectUtils.equals(this.responsibleID, null)))
        {
            aTPersonRelatedByResponsibleID = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.responsibleID), connection);
        }
        return aTPersonRelatedByResponsibleID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByResponsibleIDKey(ObjectKey key) throws TorqueException
    {

        setResponsibleID(new Integer(((NumberKey) key).intValue()));
    }




    private TProjectCategory aTProjectCategory;

    /**
     * Declares an association between this object and a TProjectCategory object
     *
     * @param v TProjectCategory
     * @throws TorqueException
     */
    public void setTProjectCategory(TProjectCategory v) throws TorqueException
    {
        if (v == null)
        {
            setProjectCategoryID((Integer) null);
        }
        else
        {
            setProjectCategoryID(v.getObjectID());
        }
        aTProjectCategory = v;
    }


    /**
     * Returns the associated TProjectCategory object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProjectCategory object
     * @throws TorqueException
     */
    public TProjectCategory getTProjectCategory()
        throws TorqueException
    {
        if (aTProjectCategory == null && (!ObjectUtils.equals(this.projectCategoryID, null)))
        {
            aTProjectCategory = TProjectCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.projectCategoryID));
        }
        return aTProjectCategory;
    }

    /**
     * Return the associated TProjectCategory object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProjectCategory object
     * @throws TorqueException
     */
    public TProjectCategory getTProjectCategory(Connection connection)
        throws TorqueException
    {
        if (aTProjectCategory == null && (!ObjectUtils.equals(this.projectCategoryID, null)))
        {
            aTProjectCategory = TProjectCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.projectCategoryID), connection);
        }
        return aTProjectCategory;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectCategoryKey(ObjectKey key) throws TorqueException
    {

        setProjectCategoryID(new Integer(((NumberKey) key).intValue()));
    }




    private TListType aTListType;

    /**
     * Declares an association between this object and a TListType object
     *
     * @param v TListType
     * @throws TorqueException
     */
    public void setTListType(TListType v) throws TorqueException
    {
        if (v == null)
        {
            setListTypeID((Integer) null);
        }
        else
        {
            setListTypeID(v.getObjectID());
        }
        aTListType = v;
    }


    /**
     * Returns the associated TListType object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TListType object
     * @throws TorqueException
     */
    public TListType getTListType()
        throws TorqueException
    {
        if (aTListType == null && (!ObjectUtils.equals(this.listTypeID, null)))
        {
            aTListType = TListTypePeer.retrieveByPK(SimpleKey.keyFor(this.listTypeID));
        }
        return aTListType;
    }

    /**
     * Return the associated TListType object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TListType object
     * @throws TorqueException
     */
    public TListType getTListType(Connection connection)
        throws TorqueException
    {
        if (aTListType == null && (!ObjectUtils.equals(this.listTypeID, null)))
        {
            aTListType = TListTypePeer.retrieveByPK(SimpleKey.keyFor(this.listTypeID), connection);
        }
        return aTListType;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTListTypeKey(ObjectKey key) throws TorqueException
    {

        setListTypeID(new Integer(((NumberKey) key).intValue()));
    }




    private TClass aTClass;

    /**
     * Declares an association between this object and a TClass object
     *
     * @param v TClass
     * @throws TorqueException
     */
    public void setTClass(TClass v) throws TorqueException
    {
        if (v == null)
        {
            setClassID((Integer) null);
        }
        else
        {
            setClassID(v.getObjectID());
        }
        aTClass = v;
    }


    /**
     * Returns the associated TClass object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TClass object
     * @throws TorqueException
     */
    public TClass getTClass()
        throws TorqueException
    {
        if (aTClass == null && (!ObjectUtils.equals(this.classID, null)))
        {
            aTClass = TClassPeer.retrieveByPK(SimpleKey.keyFor(this.classID));
        }
        return aTClass;
    }

    /**
     * Return the associated TClass object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TClass object
     * @throws TorqueException
     */
    public TClass getTClass(Connection connection)
        throws TorqueException
    {
        if (aTClass == null && (!ObjectUtils.equals(this.classID, null)))
        {
            aTClass = TClassPeer.retrieveByPK(SimpleKey.keyFor(this.classID), connection);
        }
        return aTClass;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTClassKey(ObjectKey key) throws TorqueException
    {

        setClassID(new Integer(((NumberKey) key).intValue()));
    }




    private TPriority aTPriority;

    /**
     * Declares an association between this object and a TPriority object
     *
     * @param v TPriority
     * @throws TorqueException
     */
    public void setTPriority(TPriority v) throws TorqueException
    {
        if (v == null)
        {
            setPriorityID((Integer) null);
        }
        else
        {
            setPriorityID(v.getObjectID());
        }
        aTPriority = v;
    }


    /**
     * Returns the associated TPriority object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPriority object
     * @throws TorqueException
     */
    public TPriority getTPriority()
        throws TorqueException
    {
        if (aTPriority == null && (!ObjectUtils.equals(this.priorityID, null)))
        {
            aTPriority = TPriorityPeer.retrieveByPK(SimpleKey.keyFor(this.priorityID));
        }
        return aTPriority;
    }

    /**
     * Return the associated TPriority object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPriority object
     * @throws TorqueException
     */
    public TPriority getTPriority(Connection connection)
        throws TorqueException
    {
        if (aTPriority == null && (!ObjectUtils.equals(this.priorityID, null)))
        {
            aTPriority = TPriorityPeer.retrieveByPK(SimpleKey.keyFor(this.priorityID), connection);
        }
        return aTPriority;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPriorityKey(ObjectKey key) throws TorqueException
    {

        setPriorityID(new Integer(((NumberKey) key).intValue()));
    }




    private TSeverity aTSeverity;

    /**
     * Declares an association between this object and a TSeverity object
     *
     * @param v TSeverity
     * @throws TorqueException
     */
    public void setTSeverity(TSeverity v) throws TorqueException
    {
        if (v == null)
        {
            setSeverityID((Integer) null);
        }
        else
        {
            setSeverityID(v.getObjectID());
        }
        aTSeverity = v;
    }


    /**
     * Returns the associated TSeverity object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TSeverity object
     * @throws TorqueException
     */
    public TSeverity getTSeverity()
        throws TorqueException
    {
        if (aTSeverity == null && (!ObjectUtils.equals(this.severityID, null)))
        {
            aTSeverity = TSeverityPeer.retrieveByPK(SimpleKey.keyFor(this.severityID));
        }
        return aTSeverity;
    }

    /**
     * Return the associated TSeverity object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TSeverity object
     * @throws TorqueException
     */
    public TSeverity getTSeverity(Connection connection)
        throws TorqueException
    {
        if (aTSeverity == null && (!ObjectUtils.equals(this.severityID, null)))
        {
            aTSeverity = TSeverityPeer.retrieveByPK(SimpleKey.keyFor(this.severityID), connection);
        }
        return aTSeverity;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTSeverityKey(ObjectKey key) throws TorqueException
    {

        setSeverityID(new Integer(((NumberKey) key).intValue()));
    }




    private TRelease aTReleaseRelatedByReleaseNoticedID;

    /**
     * Declares an association between this object and a TRelease object
     *
     * @param v TRelease
     * @throws TorqueException
     */
    public void setTReleaseRelatedByReleaseNoticedID(TRelease v) throws TorqueException
    {
        if (v == null)
        {
            setReleaseNoticedID((Integer) null);
        }
        else
        {
            setReleaseNoticedID(v.getObjectID());
        }
        aTReleaseRelatedByReleaseNoticedID = v;
    }


    /**
     * Returns the associated TRelease object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TRelease object
     * @throws TorqueException
     */
    public TRelease getTReleaseRelatedByReleaseNoticedID()
        throws TorqueException
    {
        if (aTReleaseRelatedByReleaseNoticedID == null && (!ObjectUtils.equals(this.releaseNoticedID, null)))
        {
            aTReleaseRelatedByReleaseNoticedID = TReleasePeer.retrieveByPK(SimpleKey.keyFor(this.releaseNoticedID));
        }
        return aTReleaseRelatedByReleaseNoticedID;
    }

    /**
     * Return the associated TRelease object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TRelease object
     * @throws TorqueException
     */
    public TRelease getTReleaseRelatedByReleaseNoticedID(Connection connection)
        throws TorqueException
    {
        if (aTReleaseRelatedByReleaseNoticedID == null && (!ObjectUtils.equals(this.releaseNoticedID, null)))
        {
            aTReleaseRelatedByReleaseNoticedID = TReleasePeer.retrieveByPK(SimpleKey.keyFor(this.releaseNoticedID), connection);
        }
        return aTReleaseRelatedByReleaseNoticedID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTReleaseRelatedByReleaseNoticedIDKey(ObjectKey key) throws TorqueException
    {

        setReleaseNoticedID(new Integer(((NumberKey) key).intValue()));
    }




    private TRelease aTReleaseRelatedByReleaseScheduledID;

    /**
     * Declares an association between this object and a TRelease object
     *
     * @param v TRelease
     * @throws TorqueException
     */
    public void setTReleaseRelatedByReleaseScheduledID(TRelease v) throws TorqueException
    {
        if (v == null)
        {
            setReleaseScheduledID((Integer) null);
        }
        else
        {
            setReleaseScheduledID(v.getObjectID());
        }
        aTReleaseRelatedByReleaseScheduledID = v;
    }


    /**
     * Returns the associated TRelease object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TRelease object
     * @throws TorqueException
     */
    public TRelease getTReleaseRelatedByReleaseScheduledID()
        throws TorqueException
    {
        if (aTReleaseRelatedByReleaseScheduledID == null && (!ObjectUtils.equals(this.releaseScheduledID, null)))
        {
            aTReleaseRelatedByReleaseScheduledID = TReleasePeer.retrieveByPK(SimpleKey.keyFor(this.releaseScheduledID));
        }
        return aTReleaseRelatedByReleaseScheduledID;
    }

    /**
     * Return the associated TRelease object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TRelease object
     * @throws TorqueException
     */
    public TRelease getTReleaseRelatedByReleaseScheduledID(Connection connection)
        throws TorqueException
    {
        if (aTReleaseRelatedByReleaseScheduledID == null && (!ObjectUtils.equals(this.releaseScheduledID, null)))
        {
            aTReleaseRelatedByReleaseScheduledID = TReleasePeer.retrieveByPK(SimpleKey.keyFor(this.releaseScheduledID), connection);
        }
        return aTReleaseRelatedByReleaseScheduledID;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTReleaseRelatedByReleaseScheduledIDKey(ObjectKey key) throws TorqueException
    {

        setReleaseScheduledID(new Integer(((NumberKey) key).intValue()));
    }




    private TState aTState;

    /**
     * Declares an association between this object and a TState object
     *
     * @param v TState
     * @throws TorqueException
     */
    public void setTState(TState v) throws TorqueException
    {
        if (v == null)
        {
            setStateID((Integer) null);
        }
        else
        {
            setStateID(v.getObjectID());
        }
        aTState = v;
    }


    /**
     * Returns the associated TState object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TState object
     * @throws TorqueException
     */
    public TState getTState()
        throws TorqueException
    {
        if (aTState == null && (!ObjectUtils.equals(this.stateID, null)))
        {
            aTState = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.stateID));
        }
        return aTState;
    }

    /**
     * Return the associated TState object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TState object
     * @throws TorqueException
     */
    public TState getTState(Connection connection)
        throws TorqueException
    {
        if (aTState == null && (!ObjectUtils.equals(this.stateID, null)))
        {
            aTState = TStatePeer.retrieveByPK(SimpleKey.keyFor(this.stateID), connection);
        }
        return aTState;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTStateKey(ObjectKey key) throws TorqueException
    {

        setStateID(new Integer(((NumberKey) key).intValue()));
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




    private TWorkItem aTWorkItemRelatedBySuperiorworkitem;

    /**
     * Declares an association between this object and a TWorkItem object
     *
     * @param v TWorkItem
     * @throws TorqueException
     */
    public void setTWorkItemRelatedBySuperiorworkitem(TWorkItem v) throws TorqueException
    {
        if (v == null)
        {
            setSuperiorworkitem((Integer) null);
        }
        else
        {
            setSuperiorworkitem(v.getObjectID());
        }
        aTWorkItemRelatedBySuperiorworkitem = v;
    }


    /**
     * Returns the associated TWorkItem object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkItem object
     * @throws TorqueException
     */
    public TWorkItem getTWorkItemRelatedBySuperiorworkitem()
        throws TorqueException
    {
        if (aTWorkItemRelatedBySuperiorworkitem == null && (!ObjectUtils.equals(this.superiorworkitem, null)))
        {
            aTWorkItemRelatedBySuperiorworkitem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.superiorworkitem));
        }
        return aTWorkItemRelatedBySuperiorworkitem;
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
    public TWorkItem getTWorkItemRelatedBySuperiorworkitem(Connection connection)
        throws TorqueException
    {
        if (aTWorkItemRelatedBySuperiorworkitem == null && (!ObjectUtils.equals(this.superiorworkitem, null)))
        {
            aTWorkItemRelatedBySuperiorworkitem = TWorkItemPeer.retrieveByPK(SimpleKey.keyFor(this.superiorworkitem), connection);
        }
        return aTWorkItemRelatedBySuperiorworkitem;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkItemRelatedBySuperiorworkitemKey(ObjectKey key) throws TorqueException
    {

        setSuperiorworkitem(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTBaseLines
     */
    protected List<TBaseLine> collTBaseLines;

    /**
     * Temporary storage of collTBaseLines to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTBaseLines()
    {
        if (collTBaseLines == null)
        {
            collTBaseLines = new ArrayList<TBaseLine>();
        }
    }


    /**
     * Method called to associate a TBaseLine object to this object
     * through the TBaseLine foreign key attribute
     *
     * @param l TBaseLine
     * @throws TorqueException
     */
    public void addTBaseLine(TBaseLine l) throws TorqueException
    {
        getTBaseLines().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TBaseLine object to this object
     * through the TBaseLine foreign key attribute using connection.
     *
     * @param l TBaseLine
     * @throws TorqueException
     */
    public void addTBaseLine(TBaseLine l, Connection con) throws TorqueException
    {
        getTBaseLines(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTBaseLines
     */
    private Criteria lastTBaseLinesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTBaseLines(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TBaseLine> getTBaseLines()
        throws TorqueException
    {
        if (collTBaseLines == null)
        {
            collTBaseLines = getTBaseLines(new Criteria(10));
        }
        return collTBaseLines;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TBaseLines from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TBaseLine> getTBaseLines(Criteria criteria) throws TorqueException
    {
        if (collTBaseLines == null)
        {
            if (isNew())
            {
               collTBaseLines = new ArrayList<TBaseLine>();
            }
            else
            {
                criteria.add(TBaseLinePeer.WORKITEMKEY, getObjectID() );
                collTBaseLines = TBaseLinePeer.doSelect(criteria);
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
                criteria.add(TBaseLinePeer.WORKITEMKEY, getObjectID());
                if (!lastTBaseLinesCriteria.equals(criteria))
                {
                    collTBaseLines = TBaseLinePeer.doSelect(criteria);
                }
            }
        }
        lastTBaseLinesCriteria = criteria;

        return collTBaseLines;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTBaseLines(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TBaseLine> getTBaseLines(Connection con) throws TorqueException
    {
        if (collTBaseLines == null)
        {
            collTBaseLines = getTBaseLines(new Criteria(10), con);
        }
        return collTBaseLines;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TBaseLines from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TBaseLine> getTBaseLines(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTBaseLines == null)
        {
            if (isNew())
            {
               collTBaseLines = new ArrayList<TBaseLine>();
            }
            else
            {
                 criteria.add(TBaseLinePeer.WORKITEMKEY, getObjectID());
                 collTBaseLines = TBaseLinePeer.doSelect(criteria, con);
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
                 criteria.add(TBaseLinePeer.WORKITEMKEY, getObjectID());
                 if (!lastTBaseLinesCriteria.equals(criteria))
                 {
                     collTBaseLines = TBaseLinePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTBaseLinesCriteria = criteria;

         return collTBaseLines;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TBaseLines from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TBaseLine> getTBaseLinesJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTBaseLines == null)
        {
            if (isNew())
            {
               collTBaseLines = new ArrayList<TBaseLine>();
            }
            else
            {
                criteria.add(TBaseLinePeer.WORKITEMKEY, getObjectID());
                collTBaseLines = TBaseLinePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TBaseLinePeer.WORKITEMKEY, getObjectID());
            if (!lastTBaseLinesCriteria.equals(criteria))
            {
                collTBaseLines = TBaseLinePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTBaseLinesCriteria = criteria;

        return collTBaseLines;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TBaseLines from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TBaseLine> getTBaseLinesJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTBaseLines == null)
        {
            if (isNew())
            {
               collTBaseLines = new ArrayList<TBaseLine>();
            }
            else
            {
                criteria.add(TBaseLinePeer.WORKITEMKEY, getObjectID());
                collTBaseLines = TBaseLinePeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TBaseLinePeer.WORKITEMKEY, getObjectID());
            if (!lastTBaseLinesCriteria.equals(criteria))
            {
                collTBaseLines = TBaseLinePeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTBaseLinesCriteria = criteria;

        return collTBaseLines;
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
        l.setTWorkItem((TWorkItem) this);
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
        l.setTWorkItem((TWorkItem) this);
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
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TNotifys from storage.
     * If this TWorkItem is new, it will return
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
                criteria.add(TNotifyPeer.WORKITEM, getObjectID() );
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
                criteria.add(TNotifyPeer.WORKITEM, getObjectID());
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
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TNotifys from storage.
     * If this TWorkItem is new, it will return
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
                 criteria.add(TNotifyPeer.WORKITEM, getObjectID());
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
                 criteria.add(TNotifyPeer.WORKITEM, getObjectID());
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
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
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
                criteria.add(TNotifyPeer.WORKITEM, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTProjectCategory(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.WORKITEM, getObjectID());
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
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
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
                criteria.add(TNotifyPeer.WORKITEM, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.WORKITEM, getObjectID());
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
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
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
                criteria.add(TNotifyPeer.WORKITEM, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.WORKITEM, getObjectID());
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
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TNotifys from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
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
                criteria.add(TNotifyPeer.WORKITEM, getObjectID());
                collTNotifys = TNotifyPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TNotifyPeer.WORKITEM, getObjectID());
            if (!lastTNotifysCriteria.equals(criteria))
            {
                collTNotifys = TNotifyPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTNotifysCriteria = criteria;

        return collTNotifys;
    }





    /**
     * Collection to store aggregation of collTStateChanges
     */
    protected List<TStateChange> collTStateChanges;

    /**
     * Temporary storage of collTStateChanges to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTStateChanges()
    {
        if (collTStateChanges == null)
        {
            collTStateChanges = new ArrayList<TStateChange>();
        }
    }


    /**
     * Method called to associate a TStateChange object to this object
     * through the TStateChange foreign key attribute
     *
     * @param l TStateChange
     * @throws TorqueException
     */
    public void addTStateChange(TStateChange l) throws TorqueException
    {
        getTStateChanges().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TStateChange object to this object
     * through the TStateChange foreign key attribute using connection.
     *
     * @param l TStateChange
     * @throws TorqueException
     */
    public void addTStateChange(TStateChange l, Connection con) throws TorqueException
    {
        getTStateChanges(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTStateChanges
     */
    private Criteria lastTStateChangesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTStateChanges(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TStateChange> getTStateChanges()
        throws TorqueException
    {
        if (collTStateChanges == null)
        {
            collTStateChanges = getTStateChanges(new Criteria(10));
        }
        return collTStateChanges;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TStateChanges from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TStateChange> getTStateChanges(Criteria criteria) throws TorqueException
    {
        if (collTStateChanges == null)
        {
            if (isNew())
            {
               collTStateChanges = new ArrayList<TStateChange>();
            }
            else
            {
                criteria.add(TStateChangePeer.WORKITEMKEY, getObjectID() );
                collTStateChanges = TStateChangePeer.doSelect(criteria);
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
                criteria.add(TStateChangePeer.WORKITEMKEY, getObjectID());
                if (!lastTStateChangesCriteria.equals(criteria))
                {
                    collTStateChanges = TStateChangePeer.doSelect(criteria);
                }
            }
        }
        lastTStateChangesCriteria = criteria;

        return collTStateChanges;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTStateChanges(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TStateChange> getTStateChanges(Connection con) throws TorqueException
    {
        if (collTStateChanges == null)
        {
            collTStateChanges = getTStateChanges(new Criteria(10), con);
        }
        return collTStateChanges;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TStateChanges from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TStateChange> getTStateChanges(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTStateChanges == null)
        {
            if (isNew())
            {
               collTStateChanges = new ArrayList<TStateChange>();
            }
            else
            {
                 criteria.add(TStateChangePeer.WORKITEMKEY, getObjectID());
                 collTStateChanges = TStateChangePeer.doSelect(criteria, con);
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
                 criteria.add(TStateChangePeer.WORKITEMKEY, getObjectID());
                 if (!lastTStateChangesCriteria.equals(criteria))
                 {
                     collTStateChanges = TStateChangePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTStateChangesCriteria = criteria;

         return collTStateChanges;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TStateChanges from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TStateChange> getTStateChangesJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTStateChanges == null)
        {
            if (isNew())
            {
               collTStateChanges = new ArrayList<TStateChange>();
            }
            else
            {
                criteria.add(TStateChangePeer.WORKITEMKEY, getObjectID());
                collTStateChanges = TStateChangePeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TStateChangePeer.WORKITEMKEY, getObjectID());
            if (!lastTStateChangesCriteria.equals(criteria))
            {
                collTStateChanges = TStateChangePeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTStateChangesCriteria = criteria;

        return collTStateChanges;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TStateChanges from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TStateChange> getTStateChangesJoinTState(Criteria criteria)
        throws TorqueException
    {
        if (collTStateChanges == null)
        {
            if (isNew())
            {
               collTStateChanges = new ArrayList<TStateChange>();
            }
            else
            {
                criteria.add(TStateChangePeer.WORKITEMKEY, getObjectID());
                collTStateChanges = TStateChangePeer.doSelectJoinTState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TStateChangePeer.WORKITEMKEY, getObjectID());
            if (!lastTStateChangesCriteria.equals(criteria))
            {
                collTStateChanges = TStateChangePeer.doSelectJoinTState(criteria);
            }
        }
        lastTStateChangesCriteria = criteria;

        return collTStateChanges;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TStateChanges from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TStateChange> getTStateChangesJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTStateChanges == null)
        {
            if (isNew())
            {
               collTStateChanges = new ArrayList<TStateChange>();
            }
            else
            {
                criteria.add(TStateChangePeer.WORKITEMKEY, getObjectID());
                collTStateChanges = TStateChangePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TStateChangePeer.WORKITEMKEY, getObjectID());
            if (!lastTStateChangesCriteria.equals(criteria))
            {
                collTStateChanges = TStateChangePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTStateChangesCriteria = criteria;

        return collTStateChanges;
    }





    /**
     * Collection to store aggregation of collTTrails
     */
    protected List<TTrail> collTTrails;

    /**
     * Temporary storage of collTTrails to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTTrails()
    {
        if (collTTrails == null)
        {
            collTTrails = new ArrayList<TTrail>();
        }
    }


    /**
     * Method called to associate a TTrail object to this object
     * through the TTrail foreign key attribute
     *
     * @param l TTrail
     * @throws TorqueException
     */
    public void addTTrail(TTrail l) throws TorqueException
    {
        getTTrails().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TTrail object to this object
     * through the TTrail foreign key attribute using connection.
     *
     * @param l TTrail
     * @throws TorqueException
     */
    public void addTTrail(TTrail l, Connection con) throws TorqueException
    {
        getTTrails(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTTrails
     */
    private Criteria lastTTrailsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTTrails(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TTrail> getTTrails()
        throws TorqueException
    {
        if (collTTrails == null)
        {
            collTTrails = getTTrails(new Criteria(10));
        }
        return collTTrails;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TTrails from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TTrail> getTTrails(Criteria criteria) throws TorqueException
    {
        if (collTTrails == null)
        {
            if (isNew())
            {
               collTTrails = new ArrayList<TTrail>();
            }
            else
            {
                criteria.add(TTrailPeer.WORKITEMKEY, getObjectID() );
                collTTrails = TTrailPeer.doSelect(criteria);
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
                criteria.add(TTrailPeer.WORKITEMKEY, getObjectID());
                if (!lastTTrailsCriteria.equals(criteria))
                {
                    collTTrails = TTrailPeer.doSelect(criteria);
                }
            }
        }
        lastTTrailsCriteria = criteria;

        return collTTrails;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTTrails(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TTrail> getTTrails(Connection con) throws TorqueException
    {
        if (collTTrails == null)
        {
            collTTrails = getTTrails(new Criteria(10), con);
        }
        return collTTrails;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TTrails from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TTrail> getTTrails(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTTrails == null)
        {
            if (isNew())
            {
               collTTrails = new ArrayList<TTrail>();
            }
            else
            {
                 criteria.add(TTrailPeer.WORKITEMKEY, getObjectID());
                 collTTrails = TTrailPeer.doSelect(criteria, con);
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
                 criteria.add(TTrailPeer.WORKITEMKEY, getObjectID());
                 if (!lastTTrailsCriteria.equals(criteria))
                 {
                     collTTrails = TTrailPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTTrailsCriteria = criteria;

         return collTTrails;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TTrails from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TTrail> getTTrailsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTTrails == null)
        {
            if (isNew())
            {
               collTTrails = new ArrayList<TTrail>();
            }
            else
            {
                criteria.add(TTrailPeer.WORKITEMKEY, getObjectID());
                collTTrails = TTrailPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TTrailPeer.WORKITEMKEY, getObjectID());
            if (!lastTTrailsCriteria.equals(criteria))
            {
                collTTrails = TTrailPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTTrailsCriteria = criteria;

        return collTTrails;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TTrails from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TTrail> getTTrailsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTTrails == null)
        {
            if (isNew())
            {
               collTTrails = new ArrayList<TTrail>();
            }
            else
            {
                criteria.add(TTrailPeer.WORKITEMKEY, getObjectID());
                collTTrails = TTrailPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TTrailPeer.WORKITEMKEY, getObjectID());
            if (!lastTTrailsCriteria.equals(criteria))
            {
                collTTrails = TTrailPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTTrailsCriteria = criteria;

        return collTTrails;
    }








    /**
     * Collection to store aggregation of collTComputedValuess
     */
    protected List<TComputedValues> collTComputedValuess;

    /**
     * Temporary storage of collTComputedValuess to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTComputedValuess()
    {
        if (collTComputedValuess == null)
        {
            collTComputedValuess = new ArrayList<TComputedValues>();
        }
    }


    /**
     * Method called to associate a TComputedValues object to this object
     * through the TComputedValues foreign key attribute
     *
     * @param l TComputedValues
     * @throws TorqueException
     */
    public void addTComputedValues(TComputedValues l) throws TorqueException
    {
        getTComputedValuess().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TComputedValues object to this object
     * through the TComputedValues foreign key attribute using connection.
     *
     * @param l TComputedValues
     * @throws TorqueException
     */
    public void addTComputedValues(TComputedValues l, Connection con) throws TorqueException
    {
        getTComputedValuess(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTComputedValuess
     */
    private Criteria lastTComputedValuessCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTComputedValuess(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TComputedValues> getTComputedValuess()
        throws TorqueException
    {
        if (collTComputedValuess == null)
        {
            collTComputedValuess = getTComputedValuess(new Criteria(10));
        }
        return collTComputedValuess;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TComputedValuess from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TComputedValues> getTComputedValuess(Criteria criteria) throws TorqueException
    {
        if (collTComputedValuess == null)
        {
            if (isNew())
            {
               collTComputedValuess = new ArrayList<TComputedValues>();
            }
            else
            {
                criteria.add(TComputedValuesPeer.WORKITEMKEY, getObjectID() );
                collTComputedValuess = TComputedValuesPeer.doSelect(criteria);
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
                criteria.add(TComputedValuesPeer.WORKITEMKEY, getObjectID());
                if (!lastTComputedValuessCriteria.equals(criteria))
                {
                    collTComputedValuess = TComputedValuesPeer.doSelect(criteria);
                }
            }
        }
        lastTComputedValuessCriteria = criteria;

        return collTComputedValuess;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTComputedValuess(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TComputedValues> getTComputedValuess(Connection con) throws TorqueException
    {
        if (collTComputedValuess == null)
        {
            collTComputedValuess = getTComputedValuess(new Criteria(10), con);
        }
        return collTComputedValuess;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TComputedValuess from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TComputedValues> getTComputedValuess(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTComputedValuess == null)
        {
            if (isNew())
            {
               collTComputedValuess = new ArrayList<TComputedValues>();
            }
            else
            {
                 criteria.add(TComputedValuesPeer.WORKITEMKEY, getObjectID());
                 collTComputedValuess = TComputedValuesPeer.doSelect(criteria, con);
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
                 criteria.add(TComputedValuesPeer.WORKITEMKEY, getObjectID());
                 if (!lastTComputedValuessCriteria.equals(criteria))
                 {
                     collTComputedValuess = TComputedValuesPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTComputedValuessCriteria = criteria;

         return collTComputedValuess;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TComputedValuess from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TComputedValues> getTComputedValuessJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTComputedValuess == null)
        {
            if (isNew())
            {
               collTComputedValuess = new ArrayList<TComputedValues>();
            }
            else
            {
                criteria.add(TComputedValuesPeer.WORKITEMKEY, getObjectID());
                collTComputedValuess = TComputedValuesPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TComputedValuesPeer.WORKITEMKEY, getObjectID());
            if (!lastTComputedValuessCriteria.equals(criteria))
            {
                collTComputedValuess = TComputedValuesPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTComputedValuessCriteria = criteria;

        return collTComputedValuess;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TComputedValuess from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TComputedValues> getTComputedValuessJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTComputedValuess == null)
        {
            if (isNew())
            {
               collTComputedValuess = new ArrayList<TComputedValues>();
            }
            else
            {
                criteria.add(TComputedValuesPeer.WORKITEMKEY, getObjectID());
                collTComputedValuess = TComputedValuesPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TComputedValuesPeer.WORKITEMKEY, getObjectID());
            if (!lastTComputedValuessCriteria.equals(criteria))
            {
                collTComputedValuess = TComputedValuesPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTComputedValuessCriteria = criteria;

        return collTComputedValuess;
    }





    /**
     * Collection to store aggregation of collTAttachments
     */
    protected List<TAttachment> collTAttachments;

    /**
     * Temporary storage of collTAttachments to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAttachments()
    {
        if (collTAttachments == null)
        {
            collTAttachments = new ArrayList<TAttachment>();
        }
    }


    /**
     * Method called to associate a TAttachment object to this object
     * through the TAttachment foreign key attribute
     *
     * @param l TAttachment
     * @throws TorqueException
     */
    public void addTAttachment(TAttachment l) throws TorqueException
    {
        getTAttachments().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TAttachment object to this object
     * through the TAttachment foreign key attribute using connection.
     *
     * @param l TAttachment
     * @throws TorqueException
     */
    public void addTAttachment(TAttachment l, Connection con) throws TorqueException
    {
        getTAttachments(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTAttachments
     */
    private Criteria lastTAttachmentsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttachments(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAttachment> getTAttachments()
        throws TorqueException
    {
        if (collTAttachments == null)
        {
            collTAttachments = getTAttachments(new Criteria(10));
        }
        return collTAttachments;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TAttachments from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAttachment> getTAttachments(Criteria criteria) throws TorqueException
    {
        if (collTAttachments == null)
        {
            if (isNew())
            {
               collTAttachments = new ArrayList<TAttachment>();
            }
            else
            {
                criteria.add(TAttachmentPeer.WORKITEM, getObjectID() );
                collTAttachments = TAttachmentPeer.doSelect(criteria);
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
                criteria.add(TAttachmentPeer.WORKITEM, getObjectID());
                if (!lastTAttachmentsCriteria.equals(criteria))
                {
                    collTAttachments = TAttachmentPeer.doSelect(criteria);
                }
            }
        }
        lastTAttachmentsCriteria = criteria;

        return collTAttachments;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttachments(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttachment> getTAttachments(Connection con) throws TorqueException
    {
        if (collTAttachments == null)
        {
            collTAttachments = getTAttachments(new Criteria(10), con);
        }
        return collTAttachments;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TAttachments from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttachment> getTAttachments(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAttachments == null)
        {
            if (isNew())
            {
               collTAttachments = new ArrayList<TAttachment>();
            }
            else
            {
                 criteria.add(TAttachmentPeer.WORKITEM, getObjectID());
                 collTAttachments = TAttachmentPeer.doSelect(criteria, con);
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
                 criteria.add(TAttachmentPeer.WORKITEM, getObjectID());
                 if (!lastTAttachmentsCriteria.equals(criteria))
                 {
                     collTAttachments = TAttachmentPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAttachmentsCriteria = criteria;

         return collTAttachments;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TAttachments from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TAttachment> getTAttachmentsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachments == null)
        {
            if (isNew())
            {
               collTAttachments = new ArrayList<TAttachment>();
            }
            else
            {
                criteria.add(TAttachmentPeer.WORKITEM, getObjectID());
                collTAttachments = TAttachmentPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentPeer.WORKITEM, getObjectID());
            if (!lastTAttachmentsCriteria.equals(criteria))
            {
                collTAttachments = TAttachmentPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTAttachmentsCriteria = criteria;

        return collTAttachments;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TAttachments from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TAttachment> getTAttachmentsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachments == null)
        {
            if (isNew())
            {
               collTAttachments = new ArrayList<TAttachment>();
            }
            else
            {
                criteria.add(TAttachmentPeer.WORKITEM, getObjectID());
                collTAttachments = TAttachmentPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentPeer.WORKITEM, getObjectID());
            if (!lastTAttachmentsCriteria.equals(criteria))
            {
                collTAttachments = TAttachmentPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTAttachmentsCriteria = criteria;

        return collTAttachments;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TAttachments from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TAttachment> getTAttachmentsJoinTDocState(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachments == null)
        {
            if (isNew())
            {
               collTAttachments = new ArrayList<TAttachment>();
            }
            else
            {
                criteria.add(TAttachmentPeer.WORKITEM, getObjectID());
                collTAttachments = TAttachmentPeer.doSelectJoinTDocState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentPeer.WORKITEM, getObjectID());
            if (!lastTAttachmentsCriteria.equals(criteria))
            {
                collTAttachments = TAttachmentPeer.doSelectJoinTDocState(criteria);
            }
        }
        lastTAttachmentsCriteria = criteria;

        return collTAttachments;
    }





    /**
     * Collection to store aggregation of collTCosts
     */
    protected List<TCost> collTCosts;

    /**
     * Temporary storage of collTCosts to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTCosts()
    {
        if (collTCosts == null)
        {
            collTCosts = new ArrayList<TCost>();
        }
    }


    /**
     * Method called to associate a TCost object to this object
     * through the TCost foreign key attribute
     *
     * @param l TCost
     * @throws TorqueException
     */
    public void addTCost(TCost l) throws TorqueException
    {
        getTCosts().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TCost object to this object
     * through the TCost foreign key attribute using connection.
     *
     * @param l TCost
     * @throws TorqueException
     */
    public void addTCost(TCost l, Connection con) throws TorqueException
    {
        getTCosts(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTCosts
     */
    private Criteria lastTCostsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCosts(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TCost> getTCosts()
        throws TorqueException
    {
        if (collTCosts == null)
        {
            collTCosts = getTCosts(new Criteria(10));
        }
        return collTCosts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TCosts from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TCost> getTCosts(Criteria criteria) throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.WORKITEM, getObjectID() );
                collTCosts = TCostPeer.doSelect(criteria);
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
                criteria.add(TCostPeer.WORKITEM, getObjectID());
                if (!lastTCostsCriteria.equals(criteria))
                {
                    collTCosts = TCostPeer.doSelect(criteria);
                }
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTCosts(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCost> getTCosts(Connection con) throws TorqueException
    {
        if (collTCosts == null)
        {
            collTCosts = getTCosts(new Criteria(10), con);
        }
        return collTCosts;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TCosts from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TCost> getTCosts(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                 criteria.add(TCostPeer.WORKITEM, getObjectID());
                 collTCosts = TCostPeer.doSelect(criteria, con);
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
                 criteria.add(TCostPeer.WORKITEM, getObjectID());
                 if (!lastTCostsCriteria.equals(criteria))
                 {
                     collTCosts = TCostPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTCostsCriteria = criteria;

         return collTCosts;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TCost> getTCostsJoinTAccount(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.WORKITEM, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTAccount(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.WORKITEM, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTAccount(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TCost> getTCostsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.WORKITEM, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.WORKITEM, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TCost> getTCostsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.WORKITEM, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.WORKITEM, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TCosts from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TCost> getTCostsJoinTEffortType(Criteria criteria)
        throws TorqueException
    {
        if (collTCosts == null)
        {
            if (isNew())
            {
               collTCosts = new ArrayList<TCost>();
            }
            else
            {
                criteria.add(TCostPeer.WORKITEM, getObjectID());
                collTCosts = TCostPeer.doSelectJoinTEffortType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TCostPeer.WORKITEM, getObjectID());
            if (!lastTCostsCriteria.equals(criteria))
            {
                collTCosts = TCostPeer.doSelectJoinTEffortType(criteria);
            }
        }
        lastTCostsCriteria = criteria;

        return collTCosts;
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
        l.setTWorkItem((TWorkItem) this);
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
        l.setTWorkItem((TWorkItem) this);
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
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     * If this TWorkItem is new, it will return
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
                criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID() );
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
                criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID());
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
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     * If this TWorkItem is new, it will return
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
                 criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID());
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
                 criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID());
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
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
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
                criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID());
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
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
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
                criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID());
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
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
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
                criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTAttribute(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID());
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
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TIssueAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
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
                criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID());
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTAttributeOption(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TIssueAttributeValuePeer.ISSUE, getObjectID());
            if (!lastTIssueAttributeValuesCriteria.equals(criteria))
            {
                collTIssueAttributeValues = TIssueAttributeValuePeer.doSelectJoinTAttributeOption(criteria);
            }
        }
        lastTIssueAttributeValuesCriteria = criteria;

        return collTIssueAttributeValues;
    }





    /**
     * Collection to store aggregation of collTBudgets
     */
    protected List<TBudget> collTBudgets;

    /**
     * Temporary storage of collTBudgets to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTBudgets()
    {
        if (collTBudgets == null)
        {
            collTBudgets = new ArrayList<TBudget>();
        }
    }


    /**
     * Method called to associate a TBudget object to this object
     * through the TBudget foreign key attribute
     *
     * @param l TBudget
     * @throws TorqueException
     */
    public void addTBudget(TBudget l) throws TorqueException
    {
        getTBudgets().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TBudget object to this object
     * through the TBudget foreign key attribute using connection.
     *
     * @param l TBudget
     * @throws TorqueException
     */
    public void addTBudget(TBudget l, Connection con) throws TorqueException
    {
        getTBudgets(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTBudgets
     */
    private Criteria lastTBudgetsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTBudgets(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TBudget> getTBudgets()
        throws TorqueException
    {
        if (collTBudgets == null)
        {
            collTBudgets = getTBudgets(new Criteria(10));
        }
        return collTBudgets;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TBudgets from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TBudget> getTBudgets(Criteria criteria) throws TorqueException
    {
        if (collTBudgets == null)
        {
            if (isNew())
            {
               collTBudgets = new ArrayList<TBudget>();
            }
            else
            {
                criteria.add(TBudgetPeer.WORKITEMKEY, getObjectID() );
                collTBudgets = TBudgetPeer.doSelect(criteria);
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
                criteria.add(TBudgetPeer.WORKITEMKEY, getObjectID());
                if (!lastTBudgetsCriteria.equals(criteria))
                {
                    collTBudgets = TBudgetPeer.doSelect(criteria);
                }
            }
        }
        lastTBudgetsCriteria = criteria;

        return collTBudgets;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTBudgets(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TBudget> getTBudgets(Connection con) throws TorqueException
    {
        if (collTBudgets == null)
        {
            collTBudgets = getTBudgets(new Criteria(10), con);
        }
        return collTBudgets;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TBudgets from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TBudget> getTBudgets(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTBudgets == null)
        {
            if (isNew())
            {
               collTBudgets = new ArrayList<TBudget>();
            }
            else
            {
                 criteria.add(TBudgetPeer.WORKITEMKEY, getObjectID());
                 collTBudgets = TBudgetPeer.doSelect(criteria, con);
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
                 criteria.add(TBudgetPeer.WORKITEMKEY, getObjectID());
                 if (!lastTBudgetsCriteria.equals(criteria))
                 {
                     collTBudgets = TBudgetPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTBudgetsCriteria = criteria;

         return collTBudgets;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TBudgets from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TBudget> getTBudgetsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTBudgets == null)
        {
            if (isNew())
            {
               collTBudgets = new ArrayList<TBudget>();
            }
            else
            {
                criteria.add(TBudgetPeer.WORKITEMKEY, getObjectID());
                collTBudgets = TBudgetPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TBudgetPeer.WORKITEMKEY, getObjectID());
            if (!lastTBudgetsCriteria.equals(criteria))
            {
                collTBudgets = TBudgetPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTBudgetsCriteria = criteria;

        return collTBudgets;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TBudgets from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TBudget> getTBudgetsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTBudgets == null)
        {
            if (isNew())
            {
               collTBudgets = new ArrayList<TBudget>();
            }
            else
            {
                criteria.add(TBudgetPeer.WORKITEMKEY, getObjectID());
                collTBudgets = TBudgetPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TBudgetPeer.WORKITEMKEY, getObjectID());
            if (!lastTBudgetsCriteria.equals(criteria))
            {
                collTBudgets = TBudgetPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTBudgetsCriteria = criteria;

        return collTBudgets;
    }





    /**
     * Collection to store aggregation of collTActualEstimatedBudgets
     */
    protected List<TActualEstimatedBudget> collTActualEstimatedBudgets;

    /**
     * Temporary storage of collTActualEstimatedBudgets to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTActualEstimatedBudgets()
    {
        if (collTActualEstimatedBudgets == null)
        {
            collTActualEstimatedBudgets = new ArrayList<TActualEstimatedBudget>();
        }
    }


    /**
     * Method called to associate a TActualEstimatedBudget object to this object
     * through the TActualEstimatedBudget foreign key attribute
     *
     * @param l TActualEstimatedBudget
     * @throws TorqueException
     */
    public void addTActualEstimatedBudget(TActualEstimatedBudget l) throws TorqueException
    {
        getTActualEstimatedBudgets().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TActualEstimatedBudget object to this object
     * through the TActualEstimatedBudget foreign key attribute using connection.
     *
     * @param l TActualEstimatedBudget
     * @throws TorqueException
     */
    public void addTActualEstimatedBudget(TActualEstimatedBudget l, Connection con) throws TorqueException
    {
        getTActualEstimatedBudgets(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTActualEstimatedBudgets
     */
    private Criteria lastTActualEstimatedBudgetsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTActualEstimatedBudgets(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TActualEstimatedBudget> getTActualEstimatedBudgets()
        throws TorqueException
    {
        if (collTActualEstimatedBudgets == null)
        {
            collTActualEstimatedBudgets = getTActualEstimatedBudgets(new Criteria(10));
        }
        return collTActualEstimatedBudgets;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TActualEstimatedBudgets from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TActualEstimatedBudget> getTActualEstimatedBudgets(Criteria criteria) throws TorqueException
    {
        if (collTActualEstimatedBudgets == null)
        {
            if (isNew())
            {
               collTActualEstimatedBudgets = new ArrayList<TActualEstimatedBudget>();
            }
            else
            {
                criteria.add(TActualEstimatedBudgetPeer.WORKITEMKEY, getObjectID() );
                collTActualEstimatedBudgets = TActualEstimatedBudgetPeer.doSelect(criteria);
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
                criteria.add(TActualEstimatedBudgetPeer.WORKITEMKEY, getObjectID());
                if (!lastTActualEstimatedBudgetsCriteria.equals(criteria))
                {
                    collTActualEstimatedBudgets = TActualEstimatedBudgetPeer.doSelect(criteria);
                }
            }
        }
        lastTActualEstimatedBudgetsCriteria = criteria;

        return collTActualEstimatedBudgets;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTActualEstimatedBudgets(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TActualEstimatedBudget> getTActualEstimatedBudgets(Connection con) throws TorqueException
    {
        if (collTActualEstimatedBudgets == null)
        {
            collTActualEstimatedBudgets = getTActualEstimatedBudgets(new Criteria(10), con);
        }
        return collTActualEstimatedBudgets;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TActualEstimatedBudgets from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TActualEstimatedBudget> getTActualEstimatedBudgets(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTActualEstimatedBudgets == null)
        {
            if (isNew())
            {
               collTActualEstimatedBudgets = new ArrayList<TActualEstimatedBudget>();
            }
            else
            {
                 criteria.add(TActualEstimatedBudgetPeer.WORKITEMKEY, getObjectID());
                 collTActualEstimatedBudgets = TActualEstimatedBudgetPeer.doSelect(criteria, con);
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
                 criteria.add(TActualEstimatedBudgetPeer.WORKITEMKEY, getObjectID());
                 if (!lastTActualEstimatedBudgetsCriteria.equals(criteria))
                 {
                     collTActualEstimatedBudgets = TActualEstimatedBudgetPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTActualEstimatedBudgetsCriteria = criteria;

         return collTActualEstimatedBudgets;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TActualEstimatedBudgets from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TActualEstimatedBudget> getTActualEstimatedBudgetsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTActualEstimatedBudgets == null)
        {
            if (isNew())
            {
               collTActualEstimatedBudgets = new ArrayList<TActualEstimatedBudget>();
            }
            else
            {
                criteria.add(TActualEstimatedBudgetPeer.WORKITEMKEY, getObjectID());
                collTActualEstimatedBudgets = TActualEstimatedBudgetPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TActualEstimatedBudgetPeer.WORKITEMKEY, getObjectID());
            if (!lastTActualEstimatedBudgetsCriteria.equals(criteria))
            {
                collTActualEstimatedBudgets = TActualEstimatedBudgetPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTActualEstimatedBudgetsCriteria = criteria;

        return collTActualEstimatedBudgets;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TActualEstimatedBudgets from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TActualEstimatedBudget> getTActualEstimatedBudgetsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTActualEstimatedBudgets == null)
        {
            if (isNew())
            {
               collTActualEstimatedBudgets = new ArrayList<TActualEstimatedBudget>();
            }
            else
            {
                criteria.add(TActualEstimatedBudgetPeer.WORKITEMKEY, getObjectID());
                collTActualEstimatedBudgets = TActualEstimatedBudgetPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TActualEstimatedBudgetPeer.WORKITEMKEY, getObjectID());
            if (!lastTActualEstimatedBudgetsCriteria.equals(criteria))
            {
                collTActualEstimatedBudgets = TActualEstimatedBudgetPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTActualEstimatedBudgetsCriteria = criteria;

        return collTActualEstimatedBudgets;
    }





    /**
     * Collection to store aggregation of collTAttributeValues
     */
    protected List<TAttributeValue> collTAttributeValues;

    /**
     * Temporary storage of collTAttributeValues to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAttributeValues()
    {
        if (collTAttributeValues == null)
        {
            collTAttributeValues = new ArrayList<TAttributeValue>();
        }
    }


    /**
     * Method called to associate a TAttributeValue object to this object
     * through the TAttributeValue foreign key attribute
     *
     * @param l TAttributeValue
     * @throws TorqueException
     */
    public void addTAttributeValue(TAttributeValue l) throws TorqueException
    {
        getTAttributeValues().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TAttributeValue object to this object
     * through the TAttributeValue foreign key attribute using connection.
     *
     * @param l TAttributeValue
     * @throws TorqueException
     */
    public void addTAttributeValue(TAttributeValue l, Connection con) throws TorqueException
    {
        getTAttributeValues(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTAttributeValues
     */
    private Criteria lastTAttributeValuesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttributeValues(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAttributeValue> getTAttributeValues()
        throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            collTAttributeValues = getTAttributeValues(new Criteria(10));
        }
        return collTAttributeValues;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TAttributeValues from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAttributeValue> getTAttributeValues(Criteria criteria) throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            if (isNew())
            {
               collTAttributeValues = new ArrayList<TAttributeValue>();
            }
            else
            {
                criteria.add(TAttributeValuePeer.WORKITEM, getObjectID() );
                collTAttributeValues = TAttributeValuePeer.doSelect(criteria);
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
                criteria.add(TAttributeValuePeer.WORKITEM, getObjectID());
                if (!lastTAttributeValuesCriteria.equals(criteria))
                {
                    collTAttributeValues = TAttributeValuePeer.doSelect(criteria);
                }
            }
        }
        lastTAttributeValuesCriteria = criteria;

        return collTAttributeValues;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttributeValues(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttributeValue> getTAttributeValues(Connection con) throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            collTAttributeValues = getTAttributeValues(new Criteria(10), con);
        }
        return collTAttributeValues;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TAttributeValues from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttributeValue> getTAttributeValues(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            if (isNew())
            {
               collTAttributeValues = new ArrayList<TAttributeValue>();
            }
            else
            {
                 criteria.add(TAttributeValuePeer.WORKITEM, getObjectID());
                 collTAttributeValues = TAttributeValuePeer.doSelect(criteria, con);
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
                 criteria.add(TAttributeValuePeer.WORKITEM, getObjectID());
                 if (!lastTAttributeValuesCriteria.equals(criteria))
                 {
                     collTAttributeValues = TAttributeValuePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAttributeValuesCriteria = criteria;

         return collTAttributeValues;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TAttributeValue> getTAttributeValuesJoinTField(Criteria criteria)
        throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            if (isNew())
            {
               collTAttributeValues = new ArrayList<TAttributeValue>();
            }
            else
            {
                criteria.add(TAttributeValuePeer.WORKITEM, getObjectID());
                collTAttributeValues = TAttributeValuePeer.doSelectJoinTField(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttributeValuePeer.WORKITEM, getObjectID());
            if (!lastTAttributeValuesCriteria.equals(criteria))
            {
                collTAttributeValues = TAttributeValuePeer.doSelectJoinTField(criteria);
            }
        }
        lastTAttributeValuesCriteria = criteria;

        return collTAttributeValues;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TAttributeValues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TAttributeValue> getTAttributeValuesJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTAttributeValues == null)
        {
            if (isNew())
            {
               collTAttributeValues = new ArrayList<TAttributeValue>();
            }
            else
            {
                criteria.add(TAttributeValuePeer.WORKITEM, getObjectID());
                collTAttributeValues = TAttributeValuePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttributeValuePeer.WORKITEM, getObjectID());
            if (!lastTAttributeValuesCriteria.equals(criteria))
            {
                collTAttributeValues = TAttributeValuePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTAttributeValuesCriteria = criteria;

        return collTAttributeValues;
    }





    /**
     * Collection to store aggregation of collTWorkItemLinksRelatedByLinkPred
     */
    protected List<TWorkItemLink> collTWorkItemLinksRelatedByLinkPred;

    /**
     * Temporary storage of collTWorkItemLinksRelatedByLinkPred to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkItemLinksRelatedByLinkPred()
    {
        if (collTWorkItemLinksRelatedByLinkPred == null)
        {
            collTWorkItemLinksRelatedByLinkPred = new ArrayList<TWorkItemLink>();
        }
    }


    /**
     * Method called to associate a TWorkItemLink object to this object
     * through the TWorkItemLink foreign key attribute
     *
     * @param l TWorkItemLink
     * @throws TorqueException
     */
    public void addTWorkItemLinkRelatedByLinkPred(TWorkItemLink l) throws TorqueException
    {
        getTWorkItemLinksRelatedByLinkPred().add(l);
        l.setTWorkItemRelatedByLinkPred((TWorkItem) this);
    }

    /**
     * Method called to associate a TWorkItemLink object to this object
     * through the TWorkItemLink foreign key attribute using connection.
     *
     * @param l TWorkItemLink
     * @throws TorqueException
     */
    public void addTWorkItemLinkRelatedByLinkPred(TWorkItemLink l, Connection con) throws TorqueException
    {
        getTWorkItemLinksRelatedByLinkPred(con).add(l);
        l.setTWorkItemRelatedByLinkPred((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkItemLinksRelatedByLinkPred
     */
    private Criteria lastTWorkItemLinksRelatedByLinkPredCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemLinksRelatedByLinkPred(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkItemLink> getTWorkItemLinksRelatedByLinkPred()
        throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkPred == null)
        {
            collTWorkItemLinksRelatedByLinkPred = getTWorkItemLinksRelatedByLinkPred(new Criteria(10));
        }
        return collTWorkItemLinksRelatedByLinkPred;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLinksRelatedByLinkPred from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkItemLink> getTWorkItemLinksRelatedByLinkPred(Criteria criteria) throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkPred == null)
        {
            if (isNew())
            {
               collTWorkItemLinksRelatedByLinkPred = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKPRED, getObjectID() );
                collTWorkItemLinksRelatedByLinkPred = TWorkItemLinkPeer.doSelect(criteria);
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
                criteria.add(TWorkItemLinkPeer.LINKPRED, getObjectID());
                if (!lastTWorkItemLinksRelatedByLinkPredCriteria.equals(criteria))
                {
                    collTWorkItemLinksRelatedByLinkPred = TWorkItemLinkPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkItemLinksRelatedByLinkPredCriteria = criteria;

        return collTWorkItemLinksRelatedByLinkPred;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemLinksRelatedByLinkPred(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItemLink> getTWorkItemLinksRelatedByLinkPred(Connection con) throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkPred == null)
        {
            collTWorkItemLinksRelatedByLinkPred = getTWorkItemLinksRelatedByLinkPred(new Criteria(10), con);
        }
        return collTWorkItemLinksRelatedByLinkPred;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLinksRelatedByLinkPred from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItemLink> getTWorkItemLinksRelatedByLinkPred(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkPred == null)
        {
            if (isNew())
            {
               collTWorkItemLinksRelatedByLinkPred = new ArrayList<TWorkItemLink>();
            }
            else
            {
                 criteria.add(TWorkItemLinkPeer.LINKPRED, getObjectID());
                 collTWorkItemLinksRelatedByLinkPred = TWorkItemLinkPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkItemLinkPeer.LINKPRED, getObjectID());
                 if (!lastTWorkItemLinksRelatedByLinkPredCriteria.equals(criteria))
                 {
                     collTWorkItemLinksRelatedByLinkPred = TWorkItemLinkPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkItemLinksRelatedByLinkPredCriteria = criteria;

         return collTWorkItemLinksRelatedByLinkPred;
     }



















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLinksRelatedByLinkPred from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TWorkItemLink> getTWorkItemLinksRelatedByLinkPredJoinTWorkItemRelatedByLinkSucc(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkPred == null)
        {
            if (isNew())
            {
               collTWorkItemLinksRelatedByLinkPred = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKPRED, getObjectID());
                collTWorkItemLinksRelatedByLinkPred = TWorkItemLinkPeer.doSelectJoinTWorkItemRelatedByLinkSucc(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLinkPeer.LINKPRED, getObjectID());
            if (!lastTWorkItemLinksRelatedByLinkPredCriteria.equals(criteria))
            {
                collTWorkItemLinksRelatedByLinkPred = TWorkItemLinkPeer.doSelectJoinTWorkItemRelatedByLinkSucc(criteria);
            }
        }
        lastTWorkItemLinksRelatedByLinkPredCriteria = criteria;

        return collTWorkItemLinksRelatedByLinkPred;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLinksRelatedByLinkPred from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TWorkItemLink> getTWorkItemLinksRelatedByLinkPredJoinTLinkType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkPred == null)
        {
            if (isNew())
            {
               collTWorkItemLinksRelatedByLinkPred = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKPRED, getObjectID());
                collTWorkItemLinksRelatedByLinkPred = TWorkItemLinkPeer.doSelectJoinTLinkType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLinkPeer.LINKPRED, getObjectID());
            if (!lastTWorkItemLinksRelatedByLinkPredCriteria.equals(criteria))
            {
                collTWorkItemLinksRelatedByLinkPred = TWorkItemLinkPeer.doSelectJoinTLinkType(criteria);
            }
        }
        lastTWorkItemLinksRelatedByLinkPredCriteria = criteria;

        return collTWorkItemLinksRelatedByLinkPred;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLinksRelatedByLinkPred from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TWorkItemLink> getTWorkItemLinksRelatedByLinkPredJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkPred == null)
        {
            if (isNew())
            {
               collTWorkItemLinksRelatedByLinkPred = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKPRED, getObjectID());
                collTWorkItemLinksRelatedByLinkPred = TWorkItemLinkPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLinkPeer.LINKPRED, getObjectID());
            if (!lastTWorkItemLinksRelatedByLinkPredCriteria.equals(criteria))
            {
                collTWorkItemLinksRelatedByLinkPred = TWorkItemLinkPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTWorkItemLinksRelatedByLinkPredCriteria = criteria;

        return collTWorkItemLinksRelatedByLinkPred;
    }





    /**
     * Collection to store aggregation of collTWorkItemLinksRelatedByLinkSucc
     */
    protected List<TWorkItemLink> collTWorkItemLinksRelatedByLinkSucc;

    /**
     * Temporary storage of collTWorkItemLinksRelatedByLinkSucc to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkItemLinksRelatedByLinkSucc()
    {
        if (collTWorkItemLinksRelatedByLinkSucc == null)
        {
            collTWorkItemLinksRelatedByLinkSucc = new ArrayList<TWorkItemLink>();
        }
    }


    /**
     * Method called to associate a TWorkItemLink object to this object
     * through the TWorkItemLink foreign key attribute
     *
     * @param l TWorkItemLink
     * @throws TorqueException
     */
    public void addTWorkItemLinkRelatedByLinkSucc(TWorkItemLink l) throws TorqueException
    {
        getTWorkItemLinksRelatedByLinkSucc().add(l);
        l.setTWorkItemRelatedByLinkSucc((TWorkItem) this);
    }

    /**
     * Method called to associate a TWorkItemLink object to this object
     * through the TWorkItemLink foreign key attribute using connection.
     *
     * @param l TWorkItemLink
     * @throws TorqueException
     */
    public void addTWorkItemLinkRelatedByLinkSucc(TWorkItemLink l, Connection con) throws TorqueException
    {
        getTWorkItemLinksRelatedByLinkSucc(con).add(l);
        l.setTWorkItemRelatedByLinkSucc((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkItemLinksRelatedByLinkSucc
     */
    private Criteria lastTWorkItemLinksRelatedByLinkSuccCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemLinksRelatedByLinkSucc(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkItemLink> getTWorkItemLinksRelatedByLinkSucc()
        throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkSucc == null)
        {
            collTWorkItemLinksRelatedByLinkSucc = getTWorkItemLinksRelatedByLinkSucc(new Criteria(10));
        }
        return collTWorkItemLinksRelatedByLinkSucc;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLinksRelatedByLinkSucc from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkItemLink> getTWorkItemLinksRelatedByLinkSucc(Criteria criteria) throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkSucc == null)
        {
            if (isNew())
            {
               collTWorkItemLinksRelatedByLinkSucc = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKSUCC, getObjectID() );
                collTWorkItemLinksRelatedByLinkSucc = TWorkItemLinkPeer.doSelect(criteria);
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
                criteria.add(TWorkItemLinkPeer.LINKSUCC, getObjectID());
                if (!lastTWorkItemLinksRelatedByLinkSuccCriteria.equals(criteria))
                {
                    collTWorkItemLinksRelatedByLinkSucc = TWorkItemLinkPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkItemLinksRelatedByLinkSuccCriteria = criteria;

        return collTWorkItemLinksRelatedByLinkSucc;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemLinksRelatedByLinkSucc(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItemLink> getTWorkItemLinksRelatedByLinkSucc(Connection con) throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkSucc == null)
        {
            collTWorkItemLinksRelatedByLinkSucc = getTWorkItemLinksRelatedByLinkSucc(new Criteria(10), con);
        }
        return collTWorkItemLinksRelatedByLinkSucc;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLinksRelatedByLinkSucc from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItemLink> getTWorkItemLinksRelatedByLinkSucc(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkSucc == null)
        {
            if (isNew())
            {
               collTWorkItemLinksRelatedByLinkSucc = new ArrayList<TWorkItemLink>();
            }
            else
            {
                 criteria.add(TWorkItemLinkPeer.LINKSUCC, getObjectID());
                 collTWorkItemLinksRelatedByLinkSucc = TWorkItemLinkPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkItemLinkPeer.LINKSUCC, getObjectID());
                 if (!lastTWorkItemLinksRelatedByLinkSuccCriteria.equals(criteria))
                 {
                     collTWorkItemLinksRelatedByLinkSucc = TWorkItemLinkPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkItemLinksRelatedByLinkSuccCriteria = criteria;

         return collTWorkItemLinksRelatedByLinkSucc;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLinksRelatedByLinkSucc from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TWorkItemLink> getTWorkItemLinksRelatedByLinkSuccJoinTWorkItemRelatedByLinkPred(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkSucc == null)
        {
            if (isNew())
            {
               collTWorkItemLinksRelatedByLinkSucc = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKSUCC, getObjectID());
                collTWorkItemLinksRelatedByLinkSucc = TWorkItemLinkPeer.doSelectJoinTWorkItemRelatedByLinkPred(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLinkPeer.LINKSUCC, getObjectID());
            if (!lastTWorkItemLinksRelatedByLinkSuccCriteria.equals(criteria))
            {
                collTWorkItemLinksRelatedByLinkSucc = TWorkItemLinkPeer.doSelectJoinTWorkItemRelatedByLinkPred(criteria);
            }
        }
        lastTWorkItemLinksRelatedByLinkSuccCriteria = criteria;

        return collTWorkItemLinksRelatedByLinkSucc;
    }

















    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLinksRelatedByLinkSucc from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TWorkItemLink> getTWorkItemLinksRelatedByLinkSuccJoinTLinkType(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkSucc == null)
        {
            if (isNew())
            {
               collTWorkItemLinksRelatedByLinkSucc = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKSUCC, getObjectID());
                collTWorkItemLinksRelatedByLinkSucc = TWorkItemLinkPeer.doSelectJoinTLinkType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLinkPeer.LINKSUCC, getObjectID());
            if (!lastTWorkItemLinksRelatedByLinkSuccCriteria.equals(criteria))
            {
                collTWorkItemLinksRelatedByLinkSucc = TWorkItemLinkPeer.doSelectJoinTLinkType(criteria);
            }
        }
        lastTWorkItemLinksRelatedByLinkSuccCriteria = criteria;

        return collTWorkItemLinksRelatedByLinkSucc;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLinksRelatedByLinkSucc from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TWorkItemLink> getTWorkItemLinksRelatedByLinkSuccJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLinksRelatedByLinkSucc == null)
        {
            if (isNew())
            {
               collTWorkItemLinksRelatedByLinkSucc = new ArrayList<TWorkItemLink>();
            }
            else
            {
                criteria.add(TWorkItemLinkPeer.LINKSUCC, getObjectID());
                collTWorkItemLinksRelatedByLinkSucc = TWorkItemLinkPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLinkPeer.LINKSUCC, getObjectID());
            if (!lastTWorkItemLinksRelatedByLinkSuccCriteria.equals(criteria))
            {
                collTWorkItemLinksRelatedByLinkSucc = TWorkItemLinkPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTWorkItemLinksRelatedByLinkSuccCriteria = criteria;

        return collTWorkItemLinksRelatedByLinkSucc;
    }





    /**
     * Collection to store aggregation of collTWorkItemLocks
     */
    protected List<TWorkItemLock> collTWorkItemLocks;

    /**
     * Temporary storage of collTWorkItemLocks to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWorkItemLocks()
    {
        if (collTWorkItemLocks == null)
        {
            collTWorkItemLocks = new ArrayList<TWorkItemLock>();
        }
    }


    /**
     * Method called to associate a TWorkItemLock object to this object
     * through the TWorkItemLock foreign key attribute
     *
     * @param l TWorkItemLock
     * @throws TorqueException
     */
    public void addTWorkItemLock(TWorkItemLock l) throws TorqueException
    {
        getTWorkItemLocks().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TWorkItemLock object to this object
     * through the TWorkItemLock foreign key attribute using connection.
     *
     * @param l TWorkItemLock
     * @throws TorqueException
     */
    public void addTWorkItemLock(TWorkItemLock l, Connection con) throws TorqueException
    {
        getTWorkItemLocks(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTWorkItemLocks
     */
    private Criteria lastTWorkItemLocksCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemLocks(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWorkItemLock> getTWorkItemLocks()
        throws TorqueException
    {
        if (collTWorkItemLocks == null)
        {
            collTWorkItemLocks = getTWorkItemLocks(new Criteria(10));
        }
        return collTWorkItemLocks;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLocks from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWorkItemLock> getTWorkItemLocks(Criteria criteria) throws TorqueException
    {
        if (collTWorkItemLocks == null)
        {
            if (isNew())
            {
               collTWorkItemLocks = new ArrayList<TWorkItemLock>();
            }
            else
            {
                criteria.add(TWorkItemLockPeer.WORKITEM, getObjectID() );
                collTWorkItemLocks = TWorkItemLockPeer.doSelect(criteria);
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
                criteria.add(TWorkItemLockPeer.WORKITEM, getObjectID());
                if (!lastTWorkItemLocksCriteria.equals(criteria))
                {
                    collTWorkItemLocks = TWorkItemLockPeer.doSelect(criteria);
                }
            }
        }
        lastTWorkItemLocksCriteria = criteria;

        return collTWorkItemLocks;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWorkItemLocks(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItemLock> getTWorkItemLocks(Connection con) throws TorqueException
    {
        if (collTWorkItemLocks == null)
        {
            collTWorkItemLocks = getTWorkItemLocks(new Criteria(10), con);
        }
        return collTWorkItemLocks;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLocks from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWorkItemLock> getTWorkItemLocks(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWorkItemLocks == null)
        {
            if (isNew())
            {
               collTWorkItemLocks = new ArrayList<TWorkItemLock>();
            }
            else
            {
                 criteria.add(TWorkItemLockPeer.WORKITEM, getObjectID());
                 collTWorkItemLocks = TWorkItemLockPeer.doSelect(criteria, con);
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
                 criteria.add(TWorkItemLockPeer.WORKITEM, getObjectID());
                 if (!lastTWorkItemLocksCriteria.equals(criteria))
                 {
                     collTWorkItemLocks = TWorkItemLockPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWorkItemLocksCriteria = criteria;

         return collTWorkItemLocks;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLocks from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TWorkItemLock> getTWorkItemLocksJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLocks == null)
        {
            if (isNew())
            {
               collTWorkItemLocks = new ArrayList<TWorkItemLock>();
            }
            else
            {
                criteria.add(TWorkItemLockPeer.WORKITEM, getObjectID());
                collTWorkItemLocks = TWorkItemLockPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLockPeer.WORKITEM, getObjectID());
            if (!lastTWorkItemLocksCriteria.equals(criteria))
            {
                collTWorkItemLocks = TWorkItemLockPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTWorkItemLocksCriteria = criteria;

        return collTWorkItemLocks;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TWorkItemLocks from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TWorkItemLock> getTWorkItemLocksJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTWorkItemLocks == null)
        {
            if (isNew())
            {
               collTWorkItemLocks = new ArrayList<TWorkItemLock>();
            }
            else
            {
                criteria.add(TWorkItemLockPeer.WORKITEM, getObjectID());
                collTWorkItemLocks = TWorkItemLockPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWorkItemLockPeer.WORKITEM, getObjectID());
            if (!lastTWorkItemLocksCriteria.equals(criteria))
            {
                collTWorkItemLocks = TWorkItemLockPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTWorkItemLocksCriteria = criteria;

        return collTWorkItemLocks;
    }





    /**
     * Collection to store aggregation of collTSummaryMails
     */
    protected List<TSummaryMail> collTSummaryMails;

    /**
     * Temporary storage of collTSummaryMails to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTSummaryMails()
    {
        if (collTSummaryMails == null)
        {
            collTSummaryMails = new ArrayList<TSummaryMail>();
        }
    }


    /**
     * Method called to associate a TSummaryMail object to this object
     * through the TSummaryMail foreign key attribute
     *
     * @param l TSummaryMail
     * @throws TorqueException
     */
    public void addTSummaryMail(TSummaryMail l) throws TorqueException
    {
        getTSummaryMails().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TSummaryMail object to this object
     * through the TSummaryMail foreign key attribute using connection.
     *
     * @param l TSummaryMail
     * @throws TorqueException
     */
    public void addTSummaryMail(TSummaryMail l, Connection con) throws TorqueException
    {
        getTSummaryMails(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTSummaryMails
     */
    private Criteria lastTSummaryMailsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTSummaryMails(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TSummaryMail> getTSummaryMails()
        throws TorqueException
    {
        if (collTSummaryMails == null)
        {
            collTSummaryMails = getTSummaryMails(new Criteria(10));
        }
        return collTSummaryMails;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TSummaryMails from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TSummaryMail> getTSummaryMails(Criteria criteria) throws TorqueException
    {
        if (collTSummaryMails == null)
        {
            if (isNew())
            {
               collTSummaryMails = new ArrayList<TSummaryMail>();
            }
            else
            {
                criteria.add(TSummaryMailPeer.WORKITEM, getObjectID() );
                collTSummaryMails = TSummaryMailPeer.doSelect(criteria);
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
                criteria.add(TSummaryMailPeer.WORKITEM, getObjectID());
                if (!lastTSummaryMailsCriteria.equals(criteria))
                {
                    collTSummaryMails = TSummaryMailPeer.doSelect(criteria);
                }
            }
        }
        lastTSummaryMailsCriteria = criteria;

        return collTSummaryMails;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTSummaryMails(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TSummaryMail> getTSummaryMails(Connection con) throws TorqueException
    {
        if (collTSummaryMails == null)
        {
            collTSummaryMails = getTSummaryMails(new Criteria(10), con);
        }
        return collTSummaryMails;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TSummaryMails from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TSummaryMail> getTSummaryMails(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTSummaryMails == null)
        {
            if (isNew())
            {
               collTSummaryMails = new ArrayList<TSummaryMail>();
            }
            else
            {
                 criteria.add(TSummaryMailPeer.WORKITEM, getObjectID());
                 collTSummaryMails = TSummaryMailPeer.doSelect(criteria, con);
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
                 criteria.add(TSummaryMailPeer.WORKITEM, getObjectID());
                 if (!lastTSummaryMailsCriteria.equals(criteria))
                 {
                     collTSummaryMails = TSummaryMailPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTSummaryMailsCriteria = criteria;

         return collTSummaryMails;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TSummaryMails from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TSummaryMail> getTSummaryMailsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTSummaryMails == null)
        {
            if (isNew())
            {
               collTSummaryMails = new ArrayList<TSummaryMail>();
            }
            else
            {
                criteria.add(TSummaryMailPeer.WORKITEM, getObjectID());
                collTSummaryMails = TSummaryMailPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TSummaryMailPeer.WORKITEM, getObjectID());
            if (!lastTSummaryMailsCriteria.equals(criteria))
            {
                collTSummaryMails = TSummaryMailPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTSummaryMailsCriteria = criteria;

        return collTSummaryMails;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TSummaryMails from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TSummaryMail> getTSummaryMailsJoinTPersonRelatedByPERSONFROM(Criteria criteria)
        throws TorqueException
    {
        if (collTSummaryMails == null)
        {
            if (isNew())
            {
               collTSummaryMails = new ArrayList<TSummaryMail>();
            }
            else
            {
                criteria.add(TSummaryMailPeer.WORKITEM, getObjectID());
                collTSummaryMails = TSummaryMailPeer.doSelectJoinTPersonRelatedByPERSONFROM(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TSummaryMailPeer.WORKITEM, getObjectID());
            if (!lastTSummaryMailsCriteria.equals(criteria))
            {
                collTSummaryMails = TSummaryMailPeer.doSelectJoinTPersonRelatedByPERSONFROM(criteria);
            }
        }
        lastTSummaryMailsCriteria = criteria;

        return collTSummaryMails;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TSummaryMails from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TSummaryMail> getTSummaryMailsJoinTPersonRelatedByPERSONTO(Criteria criteria)
        throws TorqueException
    {
        if (collTSummaryMails == null)
        {
            if (isNew())
            {
               collTSummaryMails = new ArrayList<TSummaryMail>();
            }
            else
            {
                criteria.add(TSummaryMailPeer.WORKITEM, getObjectID());
                collTSummaryMails = TSummaryMailPeer.doSelectJoinTPersonRelatedByPERSONTO(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TSummaryMailPeer.WORKITEM, getObjectID());
            if (!lastTSummaryMailsCriteria.equals(criteria))
            {
                collTSummaryMails = TSummaryMailPeer.doSelectJoinTPersonRelatedByPERSONTO(criteria);
            }
        }
        lastTSummaryMailsCriteria = criteria;

        return collTSummaryMails;
    }





    /**
     * Collection to store aggregation of collTHistoryTransactions
     */
    protected List<THistoryTransaction> collTHistoryTransactions;

    /**
     * Temporary storage of collTHistoryTransactions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTHistoryTransactions()
    {
        if (collTHistoryTransactions == null)
        {
            collTHistoryTransactions = new ArrayList<THistoryTransaction>();
        }
    }


    /**
     * Method called to associate a THistoryTransaction object to this object
     * through the THistoryTransaction foreign key attribute
     *
     * @param l THistoryTransaction
     * @throws TorqueException
     */
    public void addTHistoryTransaction(THistoryTransaction l) throws TorqueException
    {
        getTHistoryTransactions().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a THistoryTransaction object to this object
     * through the THistoryTransaction foreign key attribute using connection.
     *
     * @param l THistoryTransaction
     * @throws TorqueException
     */
    public void addTHistoryTransaction(THistoryTransaction l, Connection con) throws TorqueException
    {
        getTHistoryTransactions(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTHistoryTransactions
     */
    private Criteria lastTHistoryTransactionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTHistoryTransactions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<THistoryTransaction> getTHistoryTransactions()
        throws TorqueException
    {
        if (collTHistoryTransactions == null)
        {
            collTHistoryTransactions = getTHistoryTransactions(new Criteria(10));
        }
        return collTHistoryTransactions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related THistoryTransactions from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<THistoryTransaction> getTHistoryTransactions(Criteria criteria) throws TorqueException
    {
        if (collTHistoryTransactions == null)
        {
            if (isNew())
            {
               collTHistoryTransactions = new ArrayList<THistoryTransaction>();
            }
            else
            {
                criteria.add(THistoryTransactionPeer.WORKITEM, getObjectID() );
                collTHistoryTransactions = THistoryTransactionPeer.doSelect(criteria);
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
                criteria.add(THistoryTransactionPeer.WORKITEM, getObjectID());
                if (!lastTHistoryTransactionsCriteria.equals(criteria))
                {
                    collTHistoryTransactions = THistoryTransactionPeer.doSelect(criteria);
                }
            }
        }
        lastTHistoryTransactionsCriteria = criteria;

        return collTHistoryTransactions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTHistoryTransactions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<THistoryTransaction> getTHistoryTransactions(Connection con) throws TorqueException
    {
        if (collTHistoryTransactions == null)
        {
            collTHistoryTransactions = getTHistoryTransactions(new Criteria(10), con);
        }
        return collTHistoryTransactions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related THistoryTransactions from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<THistoryTransaction> getTHistoryTransactions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTHistoryTransactions == null)
        {
            if (isNew())
            {
               collTHistoryTransactions = new ArrayList<THistoryTransaction>();
            }
            else
            {
                 criteria.add(THistoryTransactionPeer.WORKITEM, getObjectID());
                 collTHistoryTransactions = THistoryTransactionPeer.doSelect(criteria, con);
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
                 criteria.add(THistoryTransactionPeer.WORKITEM, getObjectID());
                 if (!lastTHistoryTransactionsCriteria.equals(criteria))
                 {
                     collTHistoryTransactions = THistoryTransactionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTHistoryTransactionsCriteria = criteria;

         return collTHistoryTransactions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related THistoryTransactions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<THistoryTransaction> getTHistoryTransactionsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTHistoryTransactions == null)
        {
            if (isNew())
            {
               collTHistoryTransactions = new ArrayList<THistoryTransaction>();
            }
            else
            {
                criteria.add(THistoryTransactionPeer.WORKITEM, getObjectID());
                collTHistoryTransactions = THistoryTransactionPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(THistoryTransactionPeer.WORKITEM, getObjectID());
            if (!lastTHistoryTransactionsCriteria.equals(criteria))
            {
                collTHistoryTransactions = THistoryTransactionPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTHistoryTransactionsCriteria = criteria;

        return collTHistoryTransactions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related THistoryTransactions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<THistoryTransaction> getTHistoryTransactionsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTHistoryTransactions == null)
        {
            if (isNew())
            {
               collTHistoryTransactions = new ArrayList<THistoryTransaction>();
            }
            else
            {
                criteria.add(THistoryTransactionPeer.WORKITEM, getObjectID());
                collTHistoryTransactions = THistoryTransactionPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(THistoryTransactionPeer.WORKITEM, getObjectID());
            if (!lastTHistoryTransactionsCriteria.equals(criteria))
            {
                collTHistoryTransactions = THistoryTransactionPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTHistoryTransactionsCriteria = criteria;

        return collTHistoryTransactions;
    }





    /**
     * Collection to store aggregation of collTMSProjectTasks
     */
    protected List<TMSProjectTask> collTMSProjectTasks;

    /**
     * Temporary storage of collTMSProjectTasks to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTMSProjectTasks()
    {
        if (collTMSProjectTasks == null)
        {
            collTMSProjectTasks = new ArrayList<TMSProjectTask>();
        }
    }


    /**
     * Method called to associate a TMSProjectTask object to this object
     * through the TMSProjectTask foreign key attribute
     *
     * @param l TMSProjectTask
     * @throws TorqueException
     */
    public void addTMSProjectTask(TMSProjectTask l) throws TorqueException
    {
        getTMSProjectTasks().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TMSProjectTask object to this object
     * through the TMSProjectTask foreign key attribute using connection.
     *
     * @param l TMSProjectTask
     * @throws TorqueException
     */
    public void addTMSProjectTask(TMSProjectTask l, Connection con) throws TorqueException
    {
        getTMSProjectTasks(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTMSProjectTasks
     */
    private Criteria lastTMSProjectTasksCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMSProjectTasks(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TMSProjectTask> getTMSProjectTasks()
        throws TorqueException
    {
        if (collTMSProjectTasks == null)
        {
            collTMSProjectTasks = getTMSProjectTasks(new Criteria(10));
        }
        return collTMSProjectTasks;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TMSProjectTasks from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TMSProjectTask> getTMSProjectTasks(Criteria criteria) throws TorqueException
    {
        if (collTMSProjectTasks == null)
        {
            if (isNew())
            {
               collTMSProjectTasks = new ArrayList<TMSProjectTask>();
            }
            else
            {
                criteria.add(TMSProjectTaskPeer.WORKITEM, getObjectID() );
                collTMSProjectTasks = TMSProjectTaskPeer.doSelect(criteria);
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
                criteria.add(TMSProjectTaskPeer.WORKITEM, getObjectID());
                if (!lastTMSProjectTasksCriteria.equals(criteria))
                {
                    collTMSProjectTasks = TMSProjectTaskPeer.doSelect(criteria);
                }
            }
        }
        lastTMSProjectTasksCriteria = criteria;

        return collTMSProjectTasks;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTMSProjectTasks(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMSProjectTask> getTMSProjectTasks(Connection con) throws TorqueException
    {
        if (collTMSProjectTasks == null)
        {
            collTMSProjectTasks = getTMSProjectTasks(new Criteria(10), con);
        }
        return collTMSProjectTasks;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TMSProjectTasks from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TMSProjectTask> getTMSProjectTasks(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTMSProjectTasks == null)
        {
            if (isNew())
            {
               collTMSProjectTasks = new ArrayList<TMSProjectTask>();
            }
            else
            {
                 criteria.add(TMSProjectTaskPeer.WORKITEM, getObjectID());
                 collTMSProjectTasks = TMSProjectTaskPeer.doSelect(criteria, con);
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
                 criteria.add(TMSProjectTaskPeer.WORKITEM, getObjectID());
                 if (!lastTMSProjectTasksCriteria.equals(criteria))
                 {
                     collTMSProjectTasks = TMSProjectTaskPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTMSProjectTasksCriteria = criteria;

         return collTMSProjectTasks;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TMSProjectTasks from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TMSProjectTask> getTMSProjectTasksJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTMSProjectTasks == null)
        {
            if (isNew())
            {
               collTMSProjectTasks = new ArrayList<TMSProjectTask>();
            }
            else
            {
                criteria.add(TMSProjectTaskPeer.WORKITEM, getObjectID());
                collTMSProjectTasks = TMSProjectTaskPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TMSProjectTaskPeer.WORKITEM, getObjectID());
            if (!lastTMSProjectTasksCriteria.equals(criteria))
            {
                collTMSProjectTasks = TMSProjectTaskPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTMSProjectTasksCriteria = criteria;

        return collTMSProjectTasks;
    }





    /**
     * Collection to store aggregation of collTPersonBaskets
     */
    protected List<TPersonBasket> collTPersonBaskets;

    /**
     * Temporary storage of collTPersonBaskets to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTPersonBaskets()
    {
        if (collTPersonBaskets == null)
        {
            collTPersonBaskets = new ArrayList<TPersonBasket>();
        }
    }


    /**
     * Method called to associate a TPersonBasket object to this object
     * through the TPersonBasket foreign key attribute
     *
     * @param l TPersonBasket
     * @throws TorqueException
     */
    public void addTPersonBasket(TPersonBasket l) throws TorqueException
    {
        getTPersonBaskets().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TPersonBasket object to this object
     * through the TPersonBasket foreign key attribute using connection.
     *
     * @param l TPersonBasket
     * @throws TorqueException
     */
    public void addTPersonBasket(TPersonBasket l, Connection con) throws TorqueException
    {
        getTPersonBaskets(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTPersonBaskets
     */
    private Criteria lastTPersonBasketsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPersonBaskets(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TPersonBasket> getTPersonBaskets()
        throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            collTPersonBaskets = getTPersonBaskets(new Criteria(10));
        }
        return collTPersonBaskets;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TPersonBaskets from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TPersonBasket> getTPersonBaskets(Criteria criteria) throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            if (isNew())
            {
               collTPersonBaskets = new ArrayList<TPersonBasket>();
            }
            else
            {
                criteria.add(TPersonBasketPeer.WORKITEM, getObjectID() );
                collTPersonBaskets = TPersonBasketPeer.doSelect(criteria);
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
                criteria.add(TPersonBasketPeer.WORKITEM, getObjectID());
                if (!lastTPersonBasketsCriteria.equals(criteria))
                {
                    collTPersonBaskets = TPersonBasketPeer.doSelect(criteria);
                }
            }
        }
        lastTPersonBasketsCriteria = criteria;

        return collTPersonBaskets;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTPersonBaskets(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPersonBasket> getTPersonBaskets(Connection con) throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            collTPersonBaskets = getTPersonBaskets(new Criteria(10), con);
        }
        return collTPersonBaskets;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TPersonBaskets from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TPersonBasket> getTPersonBaskets(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            if (isNew())
            {
               collTPersonBaskets = new ArrayList<TPersonBasket>();
            }
            else
            {
                 criteria.add(TPersonBasketPeer.WORKITEM, getObjectID());
                 collTPersonBaskets = TPersonBasketPeer.doSelect(criteria, con);
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
                 criteria.add(TPersonBasketPeer.WORKITEM, getObjectID());
                 if (!lastTPersonBasketsCriteria.equals(criteria))
                 {
                     collTPersonBaskets = TPersonBasketPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTPersonBasketsCriteria = criteria;

         return collTPersonBaskets;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TPersonBaskets from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TPersonBasket> getTPersonBasketsJoinTBasket(Criteria criteria)
        throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            if (isNew())
            {
               collTPersonBaskets = new ArrayList<TPersonBasket>();
            }
            else
            {
                criteria.add(TPersonBasketPeer.WORKITEM, getObjectID());
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTBasket(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonBasketPeer.WORKITEM, getObjectID());
            if (!lastTPersonBasketsCriteria.equals(criteria))
            {
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTBasket(criteria);
            }
        }
        lastTPersonBasketsCriteria = criteria;

        return collTPersonBaskets;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TPersonBaskets from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TPersonBasket> getTPersonBasketsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            if (isNew())
            {
               collTPersonBaskets = new ArrayList<TPersonBasket>();
            }
            else
            {
                criteria.add(TPersonBasketPeer.WORKITEM, getObjectID());
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonBasketPeer.WORKITEM, getObjectID());
            if (!lastTPersonBasketsCriteria.equals(criteria))
            {
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTPersonBasketsCriteria = criteria;

        return collTPersonBaskets;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TPersonBaskets from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TPersonBasket> getTPersonBasketsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTPersonBaskets == null)
        {
            if (isNew())
            {
               collTPersonBaskets = new ArrayList<TPersonBasket>();
            }
            else
            {
                criteria.add(TPersonBasketPeer.WORKITEM, getObjectID());
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TPersonBasketPeer.WORKITEM, getObjectID());
            if (!lastTPersonBasketsCriteria.equals(criteria))
            {
                collTPersonBaskets = TPersonBasketPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTPersonBasketsCriteria = criteria;

        return collTPersonBaskets;
    }





    /**
     * Collection to store aggregation of collTLastVisitedItems
     */
    protected List<TLastVisitedItem> collTLastVisitedItems;

    /**
     * Temporary storage of collTLastVisitedItems to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTLastVisitedItems()
    {
        if (collTLastVisitedItems == null)
        {
            collTLastVisitedItems = new ArrayList<TLastVisitedItem>();
        }
    }


    /**
     * Method called to associate a TLastVisitedItem object to this object
     * through the TLastVisitedItem foreign key attribute
     *
     * @param l TLastVisitedItem
     * @throws TorqueException
     */
    public void addTLastVisitedItem(TLastVisitedItem l) throws TorqueException
    {
        getTLastVisitedItems().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TLastVisitedItem object to this object
     * through the TLastVisitedItem foreign key attribute using connection.
     *
     * @param l TLastVisitedItem
     * @throws TorqueException
     */
    public void addTLastVisitedItem(TLastVisitedItem l, Connection con) throws TorqueException
    {
        getTLastVisitedItems(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTLastVisitedItems
     */
    private Criteria lastTLastVisitedItemsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLastVisitedItems(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TLastVisitedItem> getTLastVisitedItems()
        throws TorqueException
    {
        if (collTLastVisitedItems == null)
        {
            collTLastVisitedItems = getTLastVisitedItems(new Criteria(10));
        }
        return collTLastVisitedItems;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TLastVisitedItems from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TLastVisitedItem> getTLastVisitedItems(Criteria criteria) throws TorqueException
    {
        if (collTLastVisitedItems == null)
        {
            if (isNew())
            {
               collTLastVisitedItems = new ArrayList<TLastVisitedItem>();
            }
            else
            {
                criteria.add(TLastVisitedItemPeer.WORKITEM, getObjectID() );
                collTLastVisitedItems = TLastVisitedItemPeer.doSelect(criteria);
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
                criteria.add(TLastVisitedItemPeer.WORKITEM, getObjectID());
                if (!lastTLastVisitedItemsCriteria.equals(criteria))
                {
                    collTLastVisitedItems = TLastVisitedItemPeer.doSelect(criteria);
                }
            }
        }
        lastTLastVisitedItemsCriteria = criteria;

        return collTLastVisitedItems;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTLastVisitedItems(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TLastVisitedItem> getTLastVisitedItems(Connection con) throws TorqueException
    {
        if (collTLastVisitedItems == null)
        {
            collTLastVisitedItems = getTLastVisitedItems(new Criteria(10), con);
        }
        return collTLastVisitedItems;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TLastVisitedItems from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TLastVisitedItem> getTLastVisitedItems(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTLastVisitedItems == null)
        {
            if (isNew())
            {
               collTLastVisitedItems = new ArrayList<TLastVisitedItem>();
            }
            else
            {
                 criteria.add(TLastVisitedItemPeer.WORKITEM, getObjectID());
                 collTLastVisitedItems = TLastVisitedItemPeer.doSelect(criteria, con);
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
                 criteria.add(TLastVisitedItemPeer.WORKITEM, getObjectID());
                 if (!lastTLastVisitedItemsCriteria.equals(criteria))
                 {
                     collTLastVisitedItems = TLastVisitedItemPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTLastVisitedItemsCriteria = criteria;

         return collTLastVisitedItems;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TLastVisitedItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TLastVisitedItem> getTLastVisitedItemsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTLastVisitedItems == null)
        {
            if (isNew())
            {
               collTLastVisitedItems = new ArrayList<TLastVisitedItem>();
            }
            else
            {
                criteria.add(TLastVisitedItemPeer.WORKITEM, getObjectID());
                collTLastVisitedItems = TLastVisitedItemPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TLastVisitedItemPeer.WORKITEM, getObjectID());
            if (!lastTLastVisitedItemsCriteria.equals(criteria))
            {
                collTLastVisitedItems = TLastVisitedItemPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTLastVisitedItemsCriteria = criteria;

        return collTLastVisitedItems;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TLastVisitedItems from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TLastVisitedItem> getTLastVisitedItemsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTLastVisitedItems == null)
        {
            if (isNew())
            {
               collTLastVisitedItems = new ArrayList<TLastVisitedItem>();
            }
            else
            {
                criteria.add(TLastVisitedItemPeer.WORKITEM, getObjectID());
                collTLastVisitedItems = TLastVisitedItemPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TLastVisitedItemPeer.WORKITEM, getObjectID());
            if (!lastTLastVisitedItemsCriteria.equals(criteria))
            {
                collTLastVisitedItems = TLastVisitedItemPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTLastVisitedItemsCriteria = criteria;

        return collTLastVisitedItems;
    }





    /**
     * Collection to store aggregation of collTReadIssues
     */
    protected List<TReadIssue> collTReadIssues;

    /**
     * Temporary storage of collTReadIssues to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTReadIssues()
    {
        if (collTReadIssues == null)
        {
            collTReadIssues = new ArrayList<TReadIssue>();
        }
    }


    /**
     * Method called to associate a TReadIssue object to this object
     * through the TReadIssue foreign key attribute
     *
     * @param l TReadIssue
     * @throws TorqueException
     */
    public void addTReadIssue(TReadIssue l) throws TorqueException
    {
        getTReadIssues().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TReadIssue object to this object
     * through the TReadIssue foreign key attribute using connection.
     *
     * @param l TReadIssue
     * @throws TorqueException
     */
    public void addTReadIssue(TReadIssue l, Connection con) throws TorqueException
    {
        getTReadIssues(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTReadIssues
     */
    private Criteria lastTReadIssuesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReadIssues(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TReadIssue> getTReadIssues()
        throws TorqueException
    {
        if (collTReadIssues == null)
        {
            collTReadIssues = getTReadIssues(new Criteria(10));
        }
        return collTReadIssues;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TReadIssues from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TReadIssue> getTReadIssues(Criteria criteria) throws TorqueException
    {
        if (collTReadIssues == null)
        {
            if (isNew())
            {
               collTReadIssues = new ArrayList<TReadIssue>();
            }
            else
            {
                criteria.add(TReadIssuePeer.WORKITEM, getObjectID() );
                collTReadIssues = TReadIssuePeer.doSelect(criteria);
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
                criteria.add(TReadIssuePeer.WORKITEM, getObjectID());
                if (!lastTReadIssuesCriteria.equals(criteria))
                {
                    collTReadIssues = TReadIssuePeer.doSelect(criteria);
                }
            }
        }
        lastTReadIssuesCriteria = criteria;

        return collTReadIssues;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReadIssues(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReadIssue> getTReadIssues(Connection con) throws TorqueException
    {
        if (collTReadIssues == null)
        {
            collTReadIssues = getTReadIssues(new Criteria(10), con);
        }
        return collTReadIssues;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TReadIssues from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReadIssue> getTReadIssues(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTReadIssues == null)
        {
            if (isNew())
            {
               collTReadIssues = new ArrayList<TReadIssue>();
            }
            else
            {
                 criteria.add(TReadIssuePeer.WORKITEM, getObjectID());
                 collTReadIssues = TReadIssuePeer.doSelect(criteria, con);
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
                 criteria.add(TReadIssuePeer.WORKITEM, getObjectID());
                 if (!lastTReadIssuesCriteria.equals(criteria))
                 {
                     collTReadIssues = TReadIssuePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTReadIssuesCriteria = criteria;

         return collTReadIssues;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TReadIssues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TReadIssue> getTReadIssuesJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTReadIssues == null)
        {
            if (isNew())
            {
               collTReadIssues = new ArrayList<TReadIssue>();
            }
            else
            {
                criteria.add(TReadIssuePeer.WORKITEM, getObjectID());
                collTReadIssues = TReadIssuePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReadIssuePeer.WORKITEM, getObjectID());
            if (!lastTReadIssuesCriteria.equals(criteria))
            {
                collTReadIssues = TReadIssuePeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTReadIssuesCriteria = criteria;

        return collTReadIssues;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TReadIssues from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TReadIssue> getTReadIssuesJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTReadIssues == null)
        {
            if (isNew())
            {
               collTReadIssues = new ArrayList<TReadIssue>();
            }
            else
            {
                criteria.add(TReadIssuePeer.WORKITEM, getObjectID());
                collTReadIssues = TReadIssuePeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReadIssuePeer.WORKITEM, getObjectID());
            if (!lastTReadIssuesCriteria.equals(criteria))
            {
                collTReadIssues = TReadIssuePeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTReadIssuesCriteria = criteria;

        return collTReadIssues;
    }





    /**
     * Collection to store aggregation of collTAttachmentVersions
     */
    protected List<TAttachmentVersion> collTAttachmentVersions;

    /**
     * Temporary storage of collTAttachmentVersions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTAttachmentVersions()
    {
        if (collTAttachmentVersions == null)
        {
            collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
        }
    }


    /**
     * Method called to associate a TAttachmentVersion object to this object
     * through the TAttachmentVersion foreign key attribute
     *
     * @param l TAttachmentVersion
     * @throws TorqueException
     */
    public void addTAttachmentVersion(TAttachmentVersion l) throws TorqueException
    {
        getTAttachmentVersions().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TAttachmentVersion object to this object
     * through the TAttachmentVersion foreign key attribute using connection.
     *
     * @param l TAttachmentVersion
     * @throws TorqueException
     */
    public void addTAttachmentVersion(TAttachmentVersion l, Connection con) throws TorqueException
    {
        getTAttachmentVersions(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTAttachmentVersions
     */
    private Criteria lastTAttachmentVersionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttachmentVersions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TAttachmentVersion> getTAttachmentVersions()
        throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            collTAttachmentVersions = getTAttachmentVersions(new Criteria(10));
        }
        return collTAttachmentVersions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TAttachmentVersions from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TAttachmentVersion> getTAttachmentVersions(Criteria criteria) throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            if (isNew())
            {
               collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
            }
            else
            {
                criteria.add(TAttachmentVersionPeer.WORKITEM, getObjectID() );
                collTAttachmentVersions = TAttachmentVersionPeer.doSelect(criteria);
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
                criteria.add(TAttachmentVersionPeer.WORKITEM, getObjectID());
                if (!lastTAttachmentVersionsCriteria.equals(criteria))
                {
                    collTAttachmentVersions = TAttachmentVersionPeer.doSelect(criteria);
                }
            }
        }
        lastTAttachmentVersionsCriteria = criteria;

        return collTAttachmentVersions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTAttachmentVersions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttachmentVersion> getTAttachmentVersions(Connection con) throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            collTAttachmentVersions = getTAttachmentVersions(new Criteria(10), con);
        }
        return collTAttachmentVersions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TAttachmentVersions from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TAttachmentVersion> getTAttachmentVersions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            if (isNew())
            {
               collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
            }
            else
            {
                 criteria.add(TAttachmentVersionPeer.WORKITEM, getObjectID());
                 collTAttachmentVersions = TAttachmentVersionPeer.doSelect(criteria, con);
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
                 criteria.add(TAttachmentVersionPeer.WORKITEM, getObjectID());
                 if (!lastTAttachmentVersionsCriteria.equals(criteria))
                 {
                     collTAttachmentVersions = TAttachmentVersionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTAttachmentVersionsCriteria = criteria;

         return collTAttachmentVersions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TAttachmentVersions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TAttachmentVersion> getTAttachmentVersionsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            if (isNew())
            {
               collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
            }
            else
            {
                criteria.add(TAttachmentVersionPeer.WORKITEM, getObjectID());
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentVersionPeer.WORKITEM, getObjectID());
            if (!lastTAttachmentVersionsCriteria.equals(criteria))
            {
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTAttachmentVersionsCriteria = criteria;

        return collTAttachmentVersions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TAttachmentVersions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TAttachmentVersion> getTAttachmentVersionsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            if (isNew())
            {
               collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
            }
            else
            {
                criteria.add(TAttachmentVersionPeer.WORKITEM, getObjectID());
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentVersionPeer.WORKITEM, getObjectID());
            if (!lastTAttachmentVersionsCriteria.equals(criteria))
            {
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTAttachmentVersionsCriteria = criteria;

        return collTAttachmentVersions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TAttachmentVersions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TAttachmentVersion> getTAttachmentVersionsJoinTDocState(Criteria criteria)
        throws TorqueException
    {
        if (collTAttachmentVersions == null)
        {
            if (isNew())
            {
               collTAttachmentVersions = new ArrayList<TAttachmentVersion>();
            }
            else
            {
                criteria.add(TAttachmentVersionPeer.WORKITEM, getObjectID());
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTDocState(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TAttachmentVersionPeer.WORKITEM, getObjectID());
            if (!lastTAttachmentVersionsCriteria.equals(criteria))
            {
                collTAttachmentVersions = TAttachmentVersionPeer.doSelectJoinTDocState(criteria);
            }
        }
        lastTAttachmentVersionsCriteria = criteria;

        return collTAttachmentVersions;
    }





    /**
     * Collection to store aggregation of collTItemTransitions
     */
    protected List<TItemTransition> collTItemTransitions;

    /**
     * Temporary storage of collTItemTransitions to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTItemTransitions()
    {
        if (collTItemTransitions == null)
        {
            collTItemTransitions = new ArrayList<TItemTransition>();
        }
    }


    /**
     * Method called to associate a TItemTransition object to this object
     * through the TItemTransition foreign key attribute
     *
     * @param l TItemTransition
     * @throws TorqueException
     */
    public void addTItemTransition(TItemTransition l) throws TorqueException
    {
        getTItemTransitions().add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * Method called to associate a TItemTransition object to this object
     * through the TItemTransition foreign key attribute using connection.
     *
     * @param l TItemTransition
     * @throws TorqueException
     */
    public void addTItemTransition(TItemTransition l, Connection con) throws TorqueException
    {
        getTItemTransitions(con).add(l);
        l.setTWorkItem((TWorkItem) this);
    }

    /**
     * The criteria used to select the current contents of collTItemTransitions
     */
    private Criteria lastTItemTransitionsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTItemTransitions(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TItemTransition> getTItemTransitions()
        throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            collTItemTransitions = getTItemTransitions(new Criteria(10));
        }
        return collTItemTransitions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TItemTransitions from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TItemTransition> getTItemTransitions(Criteria criteria) throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            if (isNew())
            {
               collTItemTransitions = new ArrayList<TItemTransition>();
            }
            else
            {
                criteria.add(TItemTransitionPeer.WORKITEM, getObjectID() );
                collTItemTransitions = TItemTransitionPeer.doSelect(criteria);
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
                criteria.add(TItemTransitionPeer.WORKITEM, getObjectID());
                if (!lastTItemTransitionsCriteria.equals(criteria))
                {
                    collTItemTransitions = TItemTransitionPeer.doSelect(criteria);
                }
            }
        }
        lastTItemTransitionsCriteria = criteria;

        return collTItemTransitions;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTItemTransitions(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TItemTransition> getTItemTransitions(Connection con) throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            collTItemTransitions = getTItemTransitions(new Criteria(10), con);
        }
        return collTItemTransitions;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem has previously
     * been saved, it will retrieve related TItemTransitions from storage.
     * If this TWorkItem is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TItemTransition> getTItemTransitions(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            if (isNew())
            {
               collTItemTransitions = new ArrayList<TItemTransition>();
            }
            else
            {
                 criteria.add(TItemTransitionPeer.WORKITEM, getObjectID());
                 collTItemTransitions = TItemTransitionPeer.doSelect(criteria, con);
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
                 criteria.add(TItemTransitionPeer.WORKITEM, getObjectID());
                 if (!lastTItemTransitionsCriteria.equals(criteria))
                 {
                     collTItemTransitions = TItemTransitionPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTItemTransitionsCriteria = criteria;

         return collTItemTransitions;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TItemTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TItemTransition> getTItemTransitionsJoinTWorkItem(Criteria criteria)
        throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            if (isNew())
            {
               collTItemTransitions = new ArrayList<TItemTransition>();
            }
            else
            {
                criteria.add(TItemTransitionPeer.WORKITEM, getObjectID());
                collTItemTransitions = TItemTransitionPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TItemTransitionPeer.WORKITEM, getObjectID());
            if (!lastTItemTransitionsCriteria.equals(criteria))
            {
                collTItemTransitions = TItemTransitionPeer.doSelectJoinTWorkItem(criteria);
            }
        }
        lastTItemTransitionsCriteria = criteria;

        return collTItemTransitions;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkItem is new, it will return
     * an empty collection; or if this TWorkItem has previously
     * been saved, it will retrieve related TItemTransitions from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkItem.
     */
    protected List<TItemTransition> getTItemTransitionsJoinTWorkflowTransition(Criteria criteria)
        throws TorqueException
    {
        if (collTItemTransitions == null)
        {
            if (isNew())
            {
               collTItemTransitions = new ArrayList<TItemTransition>();
            }
            else
            {
                criteria.add(TItemTransitionPeer.WORKITEM, getObjectID());
                collTItemTransitions = TItemTransitionPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TItemTransitionPeer.WORKITEM, getObjectID());
            if (!lastTItemTransitionsCriteria.equals(criteria))
            {
                collTItemTransitions = TItemTransitionPeer.doSelectJoinTWorkflowTransition(criteria);
            }
        }
        lastTItemTransitionsCriteria = criteria;

        return collTItemTransitions;
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
            fieldNames.add("OwnerID");
            fieldNames.add("ChangedByID");
            fieldNames.add("OriginatorID");
            fieldNames.add("ResponsibleID");
            fieldNames.add("ProjectID");
            fieldNames.add("ProjectCategoryID");
            fieldNames.add("ListTypeID");
            fieldNames.add("ClassID");
            fieldNames.add("PriorityID");
            fieldNames.add("SeverityID");
            fieldNames.add("Superiorworkitem");
            fieldNames.add("Synopsis");
            fieldNames.add("Description");
            fieldNames.add("Reference");
            fieldNames.add("LastEdit");
            fieldNames.add("ReleaseNoticedID");
            fieldNames.add("ReleaseScheduledID");
            fieldNames.add("Build");
            fieldNames.add("StateID");
            fieldNames.add("StartDate");
            fieldNames.add("EndDate");
            fieldNames.add("SubmitterEmail");
            fieldNames.add("Created");
            fieldNames.add("ActualStartDate");
            fieldNames.add("ActualEndDate");
            fieldNames.add("Level");
            fieldNames.add("AccessLevel");
            fieldNames.add("ArchiveLevel");
            fieldNames.add("EscalationLevel");
            fieldNames.add("TaskIsMilestone");
            fieldNames.add("TaskIsSubproject");
            fieldNames.add("TaskIsSummary");
            fieldNames.add("TaskConstraint");
            fieldNames.add("TaskConstraintDate");
            fieldNames.add("PSPCode");
            fieldNames.add("IDNumber");
            fieldNames.add("WBSOnLevel");
            fieldNames.add("ReminderDate");
            fieldNames.add("TopDownStartDate");
            fieldNames.add("TopDownEndDate");
            fieldNames.add("OverBudget");
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
        if (name.equals("OwnerID"))
        {
            return getOwnerID();
        }
        if (name.equals("ChangedByID"))
        {
            return getChangedByID();
        }
        if (name.equals("OriginatorID"))
        {
            return getOriginatorID();
        }
        if (name.equals("ResponsibleID"))
        {
            return getResponsibleID();
        }
        if (name.equals("ProjectID"))
        {
            return getProjectID();
        }
        if (name.equals("ProjectCategoryID"))
        {
            return getProjectCategoryID();
        }
        if (name.equals("ListTypeID"))
        {
            return getListTypeID();
        }
        if (name.equals("ClassID"))
        {
            return getClassID();
        }
        if (name.equals("PriorityID"))
        {
            return getPriorityID();
        }
        if (name.equals("SeverityID"))
        {
            return getSeverityID();
        }
        if (name.equals("Superiorworkitem"))
        {
            return getSuperiorworkitem();
        }
        if (name.equals("Synopsis"))
        {
            return getSynopsis();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("Reference"))
        {
            return getReference();
        }
        if (name.equals("LastEdit"))
        {
            return getLastEdit();
        }
        if (name.equals("ReleaseNoticedID"))
        {
            return getReleaseNoticedID();
        }
        if (name.equals("ReleaseScheduledID"))
        {
            return getReleaseScheduledID();
        }
        if (name.equals("Build"))
        {
            return getBuild();
        }
        if (name.equals("StateID"))
        {
            return getStateID();
        }
        if (name.equals("StartDate"))
        {
            return getStartDate();
        }
        if (name.equals("EndDate"))
        {
            return getEndDate();
        }
        if (name.equals("SubmitterEmail"))
        {
            return getSubmitterEmail();
        }
        if (name.equals("Created"))
        {
            return getCreated();
        }
        if (name.equals("ActualStartDate"))
        {
            return getActualStartDate();
        }
        if (name.equals("ActualEndDate"))
        {
            return getActualEndDate();
        }
        if (name.equals("Level"))
        {
            return getLevel();
        }
        if (name.equals("AccessLevel"))
        {
            return getAccessLevel();
        }
        if (name.equals("ArchiveLevel"))
        {
            return getArchiveLevel();
        }
        if (name.equals("EscalationLevel"))
        {
            return getEscalationLevel();
        }
        if (name.equals("TaskIsMilestone"))
        {
            return getTaskIsMilestone();
        }
        if (name.equals("TaskIsSubproject"))
        {
            return getTaskIsSubproject();
        }
        if (name.equals("TaskIsSummary"))
        {
            return getTaskIsSummary();
        }
        if (name.equals("TaskConstraint"))
        {
            return getTaskConstraint();
        }
        if (name.equals("TaskConstraintDate"))
        {
            return getTaskConstraintDate();
        }
        if (name.equals("PSPCode"))
        {
            return getPSPCode();
        }
        if (name.equals("IDNumber"))
        {
            return getIDNumber();
        }
        if (name.equals("WBSOnLevel"))
        {
            return getWBSOnLevel();
        }
        if (name.equals("ReminderDate"))
        {
            return getReminderDate();
        }
        if (name.equals("TopDownStartDate"))
        {
            return getTopDownStartDate();
        }
        if (name.equals("TopDownEndDate"))
        {
            return getTopDownEndDate();
        }
        if (name.equals("OverBudget"))
        {
            return getOverBudget();
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
        if (name.equals("OwnerID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOwnerID((Integer) value);
            return true;
        }
        if (name.equals("ChangedByID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setChangedByID((Integer) value);
            return true;
        }
        if (name.equals("OriginatorID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOriginatorID((Integer) value);
            return true;
        }
        if (name.equals("ResponsibleID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setResponsibleID((Integer) value);
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
        if (name.equals("ProjectCategoryID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProjectCategoryID((Integer) value);
            return true;
        }
        if (name.equals("ListTypeID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setListTypeID((Integer) value);
            return true;
        }
        if (name.equals("ClassID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setClassID((Integer) value);
            return true;
        }
        if (name.equals("PriorityID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPriorityID((Integer) value);
            return true;
        }
        if (name.equals("SeverityID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSeverityID((Integer) value);
            return true;
        }
        if (name.equals("Superiorworkitem"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSuperiorworkitem((Integer) value);
            return true;
        }
        if (name.equals("Synopsis"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSynopsis((String) value);
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
        if (name.equals("Reference"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReference((String) value);
            return true;
        }
        if (name.equals("LastEdit"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLastEdit((Date) value);
            return true;
        }
        if (name.equals("ReleaseNoticedID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReleaseNoticedID((Integer) value);
            return true;
        }
        if (name.equals("ReleaseScheduledID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReleaseScheduledID((Integer) value);
            return true;
        }
        if (name.equals("Build"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setBuild((String) value);
            return true;
        }
        if (name.equals("StateID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStateID((Integer) value);
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
        if (name.equals("SubmitterEmail"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSubmitterEmail((String) value);
            return true;
        }
        if (name.equals("Created"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCreated((Date) value);
            return true;
        }
        if (name.equals("ActualStartDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setActualStartDate((Date) value);
            return true;
        }
        if (name.equals("ActualEndDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setActualEndDate((Date) value);
            return true;
        }
        if (name.equals("Level"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLevel((String) value);
            return true;
        }
        if (name.equals("AccessLevel"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setAccessLevel((Integer) value);
            return true;
        }
        if (name.equals("ArchiveLevel"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setArchiveLevel((Integer) value);
            return true;
        }
        if (name.equals("EscalationLevel"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setEscalationLevel((Integer) value);
            return true;
        }
        if (name.equals("TaskIsMilestone"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTaskIsMilestone((String) value);
            return true;
        }
        if (name.equals("TaskIsSubproject"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTaskIsSubproject((String) value);
            return true;
        }
        if (name.equals("TaskIsSummary"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTaskIsSummary((String) value);
            return true;
        }
        if (name.equals("TaskConstraint"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTaskConstraint((Integer) value);
            return true;
        }
        if (name.equals("TaskConstraintDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTaskConstraintDate((Date) value);
            return true;
        }
        if (name.equals("PSPCode"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPSPCode((String) value);
            return true;
        }
        if (name.equals("IDNumber"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIDNumber((Integer) value);
            return true;
        }
        if (name.equals("WBSOnLevel"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setWBSOnLevel((Integer) value);
            return true;
        }
        if (name.equals("ReminderDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReminderDate((Date) value);
            return true;
        }
        if (name.equals("TopDownStartDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTopDownStartDate((Date) value);
            return true;
        }
        if (name.equals("TopDownEndDate"))
        {
            // Object fields can be null
            if (value != null && ! Date.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTopDownEndDate((Date) value);
            return true;
        }
        if (name.equals("OverBudget"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setOverBudget((String) value);
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
        if (name.equals(TWorkItemPeer.WORKITEMKEY))
        {
            return getObjectID();
        }
        if (name.equals(TWorkItemPeer.OWNER))
        {
            return getOwnerID();
        }
        if (name.equals(TWorkItemPeer.CHANGEDBY))
        {
            return getChangedByID();
        }
        if (name.equals(TWorkItemPeer.ORIGINATOR))
        {
            return getOriginatorID();
        }
        if (name.equals(TWorkItemPeer.RESPONSIBLE))
        {
            return getResponsibleID();
        }
        if (name.equals(TWorkItemPeer.PROJECTKEY))
        {
            return getProjectID();
        }
        if (name.equals(TWorkItemPeer.PROJCATKEY))
        {
            return getProjectCategoryID();
        }
        if (name.equals(TWorkItemPeer.CATEGORYKEY))
        {
            return getListTypeID();
        }
        if (name.equals(TWorkItemPeer.CLASSKEY))
        {
            return getClassID();
        }
        if (name.equals(TWorkItemPeer.PRIORITYKEY))
        {
            return getPriorityID();
        }
        if (name.equals(TWorkItemPeer.SEVERITYKEY))
        {
            return getSeverityID();
        }
        if (name.equals(TWorkItemPeer.SUPERIORWORKITEM))
        {
            return getSuperiorworkitem();
        }
        if (name.equals(TWorkItemPeer.PACKAGESYNOPSYS))
        {
            return getSynopsis();
        }
        if (name.equals(TWorkItemPeer.PACKAGEDESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TWorkItemPeer.REFERENCE))
        {
            return getReference();
        }
        if (name.equals(TWorkItemPeer.LASTEDIT))
        {
            return getLastEdit();
        }
        if (name.equals(TWorkItemPeer.RELNOTICEDKEY))
        {
            return getReleaseNoticedID();
        }
        if (name.equals(TWorkItemPeer.RELSCHEDULEDKEY))
        {
            return getReleaseScheduledID();
        }
        if (name.equals(TWorkItemPeer.BUILD))
        {
            return getBuild();
        }
        if (name.equals(TWorkItemPeer.STATE))
        {
            return getStateID();
        }
        if (name.equals(TWorkItemPeer.STARTDATE))
        {
            return getStartDate();
        }
        if (name.equals(TWorkItemPeer.ENDDATE))
        {
            return getEndDate();
        }
        if (name.equals(TWorkItemPeer.SUBMITTEREMAIL))
        {
            return getSubmitterEmail();
        }
        if (name.equals(TWorkItemPeer.CREATED))
        {
            return getCreated();
        }
        if (name.equals(TWorkItemPeer.ACTUALSTARTDATE))
        {
            return getActualStartDate();
        }
        if (name.equals(TWorkItemPeer.ACTUALENDDATE))
        {
            return getActualEndDate();
        }
        if (name.equals(TWorkItemPeer.WLEVEL))
        {
            return getLevel();
        }
        if (name.equals(TWorkItemPeer.ACCESSLEVEL))
        {
            return getAccessLevel();
        }
        if (name.equals(TWorkItemPeer.ARCHIVELEVEL))
        {
            return getArchiveLevel();
        }
        if (name.equals(TWorkItemPeer.ESCALATIONLEVEL))
        {
            return getEscalationLevel();
        }
        if (name.equals(TWorkItemPeer.TASKISMILESTONE))
        {
            return getTaskIsMilestone();
        }
        if (name.equals(TWorkItemPeer.TASKISSUBPROJECT))
        {
            return getTaskIsSubproject();
        }
        if (name.equals(TWorkItemPeer.TASKISSUMMARY))
        {
            return getTaskIsSummary();
        }
        if (name.equals(TWorkItemPeer.TASKCONSTRAINT))
        {
            return getTaskConstraint();
        }
        if (name.equals(TWorkItemPeer.TASKCONSTRAINTDATE))
        {
            return getTaskConstraintDate();
        }
        if (name.equals(TWorkItemPeer.PSPCODE))
        {
            return getPSPCode();
        }
        if (name.equals(TWorkItemPeer.IDNUMBER))
        {
            return getIDNumber();
        }
        if (name.equals(TWorkItemPeer.WBSONLEVEL))
        {
            return getWBSOnLevel();
        }
        if (name.equals(TWorkItemPeer.REMINDERDATE))
        {
            return getReminderDate();
        }
        if (name.equals(TWorkItemPeer.TOPDOWNSTARTDATE))
        {
            return getTopDownStartDate();
        }
        if (name.equals(TWorkItemPeer.TOPDOWNENDDATE))
        {
            return getTopDownEndDate();
        }
        if (name.equals(TWorkItemPeer.OVERBUDGET))
        {
            return getOverBudget();
        }
        if (name.equals(TWorkItemPeer.TPUUID))
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
      if (TWorkItemPeer.WORKITEMKEY.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TWorkItemPeer.OWNER.equals(name))
        {
            return setByName("OwnerID", value);
        }
      if (TWorkItemPeer.CHANGEDBY.equals(name))
        {
            return setByName("ChangedByID", value);
        }
      if (TWorkItemPeer.ORIGINATOR.equals(name))
        {
            return setByName("OriginatorID", value);
        }
      if (TWorkItemPeer.RESPONSIBLE.equals(name))
        {
            return setByName("ResponsibleID", value);
        }
      if (TWorkItemPeer.PROJECTKEY.equals(name))
        {
            return setByName("ProjectID", value);
        }
      if (TWorkItemPeer.PROJCATKEY.equals(name))
        {
            return setByName("ProjectCategoryID", value);
        }
      if (TWorkItemPeer.CATEGORYKEY.equals(name))
        {
            return setByName("ListTypeID", value);
        }
      if (TWorkItemPeer.CLASSKEY.equals(name))
        {
            return setByName("ClassID", value);
        }
      if (TWorkItemPeer.PRIORITYKEY.equals(name))
        {
            return setByName("PriorityID", value);
        }
      if (TWorkItemPeer.SEVERITYKEY.equals(name))
        {
            return setByName("SeverityID", value);
        }
      if (TWorkItemPeer.SUPERIORWORKITEM.equals(name))
        {
            return setByName("Superiorworkitem", value);
        }
      if (TWorkItemPeer.PACKAGESYNOPSYS.equals(name))
        {
            return setByName("Synopsis", value);
        }
      if (TWorkItemPeer.PACKAGEDESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TWorkItemPeer.REFERENCE.equals(name))
        {
            return setByName("Reference", value);
        }
      if (TWorkItemPeer.LASTEDIT.equals(name))
        {
            return setByName("LastEdit", value);
        }
      if (TWorkItemPeer.RELNOTICEDKEY.equals(name))
        {
            return setByName("ReleaseNoticedID", value);
        }
      if (TWorkItemPeer.RELSCHEDULEDKEY.equals(name))
        {
            return setByName("ReleaseScheduledID", value);
        }
      if (TWorkItemPeer.BUILD.equals(name))
        {
            return setByName("Build", value);
        }
      if (TWorkItemPeer.STATE.equals(name))
        {
            return setByName("StateID", value);
        }
      if (TWorkItemPeer.STARTDATE.equals(name))
        {
            return setByName("StartDate", value);
        }
      if (TWorkItemPeer.ENDDATE.equals(name))
        {
            return setByName("EndDate", value);
        }
      if (TWorkItemPeer.SUBMITTEREMAIL.equals(name))
        {
            return setByName("SubmitterEmail", value);
        }
      if (TWorkItemPeer.CREATED.equals(name))
        {
            return setByName("Created", value);
        }
      if (TWorkItemPeer.ACTUALSTARTDATE.equals(name))
        {
            return setByName("ActualStartDate", value);
        }
      if (TWorkItemPeer.ACTUALENDDATE.equals(name))
        {
            return setByName("ActualEndDate", value);
        }
      if (TWorkItemPeer.WLEVEL.equals(name))
        {
            return setByName("Level", value);
        }
      if (TWorkItemPeer.ACCESSLEVEL.equals(name))
        {
            return setByName("AccessLevel", value);
        }
      if (TWorkItemPeer.ARCHIVELEVEL.equals(name))
        {
            return setByName("ArchiveLevel", value);
        }
      if (TWorkItemPeer.ESCALATIONLEVEL.equals(name))
        {
            return setByName("EscalationLevel", value);
        }
      if (TWorkItemPeer.TASKISMILESTONE.equals(name))
        {
            return setByName("TaskIsMilestone", value);
        }
      if (TWorkItemPeer.TASKISSUBPROJECT.equals(name))
        {
            return setByName("TaskIsSubproject", value);
        }
      if (TWorkItemPeer.TASKISSUMMARY.equals(name))
        {
            return setByName("TaskIsSummary", value);
        }
      if (TWorkItemPeer.TASKCONSTRAINT.equals(name))
        {
            return setByName("TaskConstraint", value);
        }
      if (TWorkItemPeer.TASKCONSTRAINTDATE.equals(name))
        {
            return setByName("TaskConstraintDate", value);
        }
      if (TWorkItemPeer.PSPCODE.equals(name))
        {
            return setByName("PSPCode", value);
        }
      if (TWorkItemPeer.IDNUMBER.equals(name))
        {
            return setByName("IDNumber", value);
        }
      if (TWorkItemPeer.WBSONLEVEL.equals(name))
        {
            return setByName("WBSOnLevel", value);
        }
      if (TWorkItemPeer.REMINDERDATE.equals(name))
        {
            return setByName("ReminderDate", value);
        }
      if (TWorkItemPeer.TOPDOWNSTARTDATE.equals(name))
        {
            return setByName("TopDownStartDate", value);
        }
      if (TWorkItemPeer.TOPDOWNENDDATE.equals(name))
        {
            return setByName("TopDownEndDate", value);
        }
      if (TWorkItemPeer.OVERBUDGET.equals(name))
        {
            return setByName("OverBudget", value);
        }
      if (TWorkItemPeer.TPUUID.equals(name))
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
            return getOwnerID();
        }
        if (pos == 2)
        {
            return getChangedByID();
        }
        if (pos == 3)
        {
            return getOriginatorID();
        }
        if (pos == 4)
        {
            return getResponsibleID();
        }
        if (pos == 5)
        {
            return getProjectID();
        }
        if (pos == 6)
        {
            return getProjectCategoryID();
        }
        if (pos == 7)
        {
            return getListTypeID();
        }
        if (pos == 8)
        {
            return getClassID();
        }
        if (pos == 9)
        {
            return getPriorityID();
        }
        if (pos == 10)
        {
            return getSeverityID();
        }
        if (pos == 11)
        {
            return getSuperiorworkitem();
        }
        if (pos == 12)
        {
            return getSynopsis();
        }
        if (pos == 13)
        {
            return getDescription();
        }
        if (pos == 14)
        {
            return getReference();
        }
        if (pos == 15)
        {
            return getLastEdit();
        }
        if (pos == 16)
        {
            return getReleaseNoticedID();
        }
        if (pos == 17)
        {
            return getReleaseScheduledID();
        }
        if (pos == 18)
        {
            return getBuild();
        }
        if (pos == 19)
        {
            return getStateID();
        }
        if (pos == 20)
        {
            return getStartDate();
        }
        if (pos == 21)
        {
            return getEndDate();
        }
        if (pos == 22)
        {
            return getSubmitterEmail();
        }
        if (pos == 23)
        {
            return getCreated();
        }
        if (pos == 24)
        {
            return getActualStartDate();
        }
        if (pos == 25)
        {
            return getActualEndDate();
        }
        if (pos == 26)
        {
            return getLevel();
        }
        if (pos == 27)
        {
            return getAccessLevel();
        }
        if (pos == 28)
        {
            return getArchiveLevel();
        }
        if (pos == 29)
        {
            return getEscalationLevel();
        }
        if (pos == 30)
        {
            return getTaskIsMilestone();
        }
        if (pos == 31)
        {
            return getTaskIsSubproject();
        }
        if (pos == 32)
        {
            return getTaskIsSummary();
        }
        if (pos == 33)
        {
            return getTaskConstraint();
        }
        if (pos == 34)
        {
            return getTaskConstraintDate();
        }
        if (pos == 35)
        {
            return getPSPCode();
        }
        if (pos == 36)
        {
            return getIDNumber();
        }
        if (pos == 37)
        {
            return getWBSOnLevel();
        }
        if (pos == 38)
        {
            return getReminderDate();
        }
        if (pos == 39)
        {
            return getTopDownStartDate();
        }
        if (pos == 40)
        {
            return getTopDownEndDate();
        }
        if (pos == 41)
        {
            return getOverBudget();
        }
        if (pos == 42)
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
            return setByName("OwnerID", value);
        }
    if (position == 2)
        {
            return setByName("ChangedByID", value);
        }
    if (position == 3)
        {
            return setByName("OriginatorID", value);
        }
    if (position == 4)
        {
            return setByName("ResponsibleID", value);
        }
    if (position == 5)
        {
            return setByName("ProjectID", value);
        }
    if (position == 6)
        {
            return setByName("ProjectCategoryID", value);
        }
    if (position == 7)
        {
            return setByName("ListTypeID", value);
        }
    if (position == 8)
        {
            return setByName("ClassID", value);
        }
    if (position == 9)
        {
            return setByName("PriorityID", value);
        }
    if (position == 10)
        {
            return setByName("SeverityID", value);
        }
    if (position == 11)
        {
            return setByName("Superiorworkitem", value);
        }
    if (position == 12)
        {
            return setByName("Synopsis", value);
        }
    if (position == 13)
        {
            return setByName("Description", value);
        }
    if (position == 14)
        {
            return setByName("Reference", value);
        }
    if (position == 15)
        {
            return setByName("LastEdit", value);
        }
    if (position == 16)
        {
            return setByName("ReleaseNoticedID", value);
        }
    if (position == 17)
        {
            return setByName("ReleaseScheduledID", value);
        }
    if (position == 18)
        {
            return setByName("Build", value);
        }
    if (position == 19)
        {
            return setByName("StateID", value);
        }
    if (position == 20)
        {
            return setByName("StartDate", value);
        }
    if (position == 21)
        {
            return setByName("EndDate", value);
        }
    if (position == 22)
        {
            return setByName("SubmitterEmail", value);
        }
    if (position == 23)
        {
            return setByName("Created", value);
        }
    if (position == 24)
        {
            return setByName("ActualStartDate", value);
        }
    if (position == 25)
        {
            return setByName("ActualEndDate", value);
        }
    if (position == 26)
        {
            return setByName("Level", value);
        }
    if (position == 27)
        {
            return setByName("AccessLevel", value);
        }
    if (position == 28)
        {
            return setByName("ArchiveLevel", value);
        }
    if (position == 29)
        {
            return setByName("EscalationLevel", value);
        }
    if (position == 30)
        {
            return setByName("TaskIsMilestone", value);
        }
    if (position == 31)
        {
            return setByName("TaskIsSubproject", value);
        }
    if (position == 32)
        {
            return setByName("TaskIsSummary", value);
        }
    if (position == 33)
        {
            return setByName("TaskConstraint", value);
        }
    if (position == 34)
        {
            return setByName("TaskConstraintDate", value);
        }
    if (position == 35)
        {
            return setByName("PSPCode", value);
        }
    if (position == 36)
        {
            return setByName("IDNumber", value);
        }
    if (position == 37)
        {
            return setByName("WBSOnLevel", value);
        }
    if (position == 38)
        {
            return setByName("ReminderDate", value);
        }
    if (position == 39)
        {
            return setByName("TopDownStartDate", value);
        }
    if (position == 40)
        {
            return setByName("TopDownEndDate", value);
        }
    if (position == 41)
        {
            return setByName("OverBudget", value);
        }
    if (position == 42)
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
        save(TWorkItemPeer.DATABASE_NAME);
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
                    TWorkItemPeer.doInsert((TWorkItem) this, con);
                    setNew(false);
                }
                else
                {
                    TWorkItemPeer.doUpdate((TWorkItem) this, con);
                }
            }


            if (collTBaseLines != null)
            {
                for (int i = 0; i < collTBaseLines.size(); i++)
                {
                    ((TBaseLine) collTBaseLines.get(i)).save(con);
                }
            }

            if (collTNotifys != null)
            {
                for (int i = 0; i < collTNotifys.size(); i++)
                {
                    ((TNotify) collTNotifys.get(i)).save(con);
                }
            }

            if (collTStateChanges != null)
            {
                for (int i = 0; i < collTStateChanges.size(); i++)
                {
                    ((TStateChange) collTStateChanges.get(i)).save(con);
                }
            }

            if (collTTrails != null)
            {
                for (int i = 0; i < collTTrails.size(); i++)
                {
                    ((TTrail) collTTrails.get(i)).save(con);
                }
            }

            if (collTComputedValuess != null)
            {
                for (int i = 0; i < collTComputedValuess.size(); i++)
                {
                    ((TComputedValues) collTComputedValuess.get(i)).save(con);
                }
            }

            if (collTAttachments != null)
            {
                for (int i = 0; i < collTAttachments.size(); i++)
                {
                    ((TAttachment) collTAttachments.get(i)).save(con);
                }
            }

            if (collTCosts != null)
            {
                for (int i = 0; i < collTCosts.size(); i++)
                {
                    ((TCost) collTCosts.get(i)).save(con);
                }
            }

            if (collTIssueAttributeValues != null)
            {
                for (int i = 0; i < collTIssueAttributeValues.size(); i++)
                {
                    ((TIssueAttributeValue) collTIssueAttributeValues.get(i)).save(con);
                }
            }

            if (collTBudgets != null)
            {
                for (int i = 0; i < collTBudgets.size(); i++)
                {
                    ((TBudget) collTBudgets.get(i)).save(con);
                }
            }

            if (collTActualEstimatedBudgets != null)
            {
                for (int i = 0; i < collTActualEstimatedBudgets.size(); i++)
                {
                    ((TActualEstimatedBudget) collTActualEstimatedBudgets.get(i)).save(con);
                }
            }

            if (collTAttributeValues != null)
            {
                for (int i = 0; i < collTAttributeValues.size(); i++)
                {
                    ((TAttributeValue) collTAttributeValues.get(i)).save(con);
                }
            }

            if (collTWorkItemLinksRelatedByLinkPred != null)
            {
                for (int i = 0; i < collTWorkItemLinksRelatedByLinkPred.size(); i++)
                {
                    ((TWorkItemLink) collTWorkItemLinksRelatedByLinkPred.get(i)).save(con);
                }
            }

            if (collTWorkItemLinksRelatedByLinkSucc != null)
            {
                for (int i = 0; i < collTWorkItemLinksRelatedByLinkSucc.size(); i++)
                {
                    ((TWorkItemLink) collTWorkItemLinksRelatedByLinkSucc.get(i)).save(con);
                }
            }

            if (collTWorkItemLocks != null)
            {
                for (int i = 0; i < collTWorkItemLocks.size(); i++)
                {
                    ((TWorkItemLock) collTWorkItemLocks.get(i)).save(con);
                }
            }

            if (collTSummaryMails != null)
            {
                for (int i = 0; i < collTSummaryMails.size(); i++)
                {
                    ((TSummaryMail) collTSummaryMails.get(i)).save(con);
                }
            }

            if (collTHistoryTransactions != null)
            {
                for (int i = 0; i < collTHistoryTransactions.size(); i++)
                {
                    ((THistoryTransaction) collTHistoryTransactions.get(i)).save(con);
                }
            }

            if (collTMSProjectTasks != null)
            {
                for (int i = 0; i < collTMSProjectTasks.size(); i++)
                {
                    ((TMSProjectTask) collTMSProjectTasks.get(i)).save(con);
                }
            }

            if (collTPersonBaskets != null)
            {
                for (int i = 0; i < collTPersonBaskets.size(); i++)
                {
                    ((TPersonBasket) collTPersonBaskets.get(i)).save(con);
                }
            }

            if (collTLastVisitedItems != null)
            {
                for (int i = 0; i < collTLastVisitedItems.size(); i++)
                {
                    ((TLastVisitedItem) collTLastVisitedItems.get(i)).save(con);
                }
            }

            if (collTReadIssues != null)
            {
                for (int i = 0; i < collTReadIssues.size(); i++)
                {
                    ((TReadIssue) collTReadIssues.get(i)).save(con);
                }
            }

            if (collTAttachmentVersions != null)
            {
                for (int i = 0; i < collTAttachmentVersions.size(); i++)
                {
                    ((TAttachmentVersion) collTAttachmentVersions.get(i)).save(con);
                }
            }

            if (collTItemTransitions != null)
            {
                for (int i = 0; i < collTItemTransitions.size(); i++)
                {
                    ((TItemTransition) collTItemTransitions.get(i)).save(con);
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
    public TWorkItem copy() throws TorqueException
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
    public TWorkItem copy(Connection con) throws TorqueException
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
    public TWorkItem copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TWorkItem(), deepcopy);
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
    public TWorkItem copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TWorkItem(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TWorkItem copyInto(TWorkItem copyObj) throws TorqueException
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
    protected TWorkItem copyInto(TWorkItem copyObj, Connection con) throws TorqueException
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
    protected TWorkItem copyInto(TWorkItem copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setOwnerID(ownerID);
        copyObj.setChangedByID(changedByID);
        copyObj.setOriginatorID(originatorID);
        copyObj.setResponsibleID(responsibleID);
        copyObj.setProjectID(projectID);
        copyObj.setProjectCategoryID(projectCategoryID);
        copyObj.setListTypeID(listTypeID);
        copyObj.setClassID(classID);
        copyObj.setPriorityID(priorityID);
        copyObj.setSeverityID(severityID);
        copyObj.setSuperiorworkitem(superiorworkitem);
        copyObj.setSynopsis(synopsis);
        copyObj.setDescription(description);
        copyObj.setReference(reference);
        copyObj.setLastEdit(lastEdit);
        copyObj.setReleaseNoticedID(releaseNoticedID);
        copyObj.setReleaseScheduledID(releaseScheduledID);
        copyObj.setBuild(build);
        copyObj.setStateID(stateID);
        copyObj.setStartDate(startDate);
        copyObj.setEndDate(endDate);
        copyObj.setSubmitterEmail(submitterEmail);
        copyObj.setCreated(created);
        copyObj.setActualStartDate(actualStartDate);
        copyObj.setActualEndDate(actualEndDate);
        copyObj.setLevel(level);
        copyObj.setAccessLevel(accessLevel);
        copyObj.setArchiveLevel(archiveLevel);
        copyObj.setEscalationLevel(escalationLevel);
        copyObj.setTaskIsMilestone(taskIsMilestone);
        copyObj.setTaskIsSubproject(taskIsSubproject);
        copyObj.setTaskIsSummary(taskIsSummary);
        copyObj.setTaskConstraint(taskConstraint);
        copyObj.setTaskConstraintDate(taskConstraintDate);
        copyObj.setPSPCode(pSPCode);
        copyObj.setIDNumber(iDNumber);
        copyObj.setWBSOnLevel(wBSOnLevel);
        copyObj.setReminderDate(reminderDate);
        copyObj.setTopDownStartDate(topDownStartDate);
        copyObj.setTopDownEndDate(topDownEndDate);
        copyObj.setOverBudget(overBudget);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TBaseLine> vTBaseLines = getTBaseLines();
        if (vTBaseLines != null)
        {
            for (int i = 0; i < vTBaseLines.size(); i++)
            {
                TBaseLine obj =  vTBaseLines.get(i);
                copyObj.addTBaseLine(obj.copy());
            }
        }
        else
        {
            copyObj.collTBaseLines = null;
        }


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


        List<TStateChange> vTStateChanges = getTStateChanges();
        if (vTStateChanges != null)
        {
            for (int i = 0; i < vTStateChanges.size(); i++)
            {
                TStateChange obj =  vTStateChanges.get(i);
                copyObj.addTStateChange(obj.copy());
            }
        }
        else
        {
            copyObj.collTStateChanges = null;
        }


        List<TTrail> vTTrails = getTTrails();
        if (vTTrails != null)
        {
            for (int i = 0; i < vTTrails.size(); i++)
            {
                TTrail obj =  vTTrails.get(i);
                copyObj.addTTrail(obj.copy());
            }
        }
        else
        {
            copyObj.collTTrails = null;
        }


        List<TComputedValues> vTComputedValuess = getTComputedValuess();
        if (vTComputedValuess != null)
        {
            for (int i = 0; i < vTComputedValuess.size(); i++)
            {
                TComputedValues obj =  vTComputedValuess.get(i);
                copyObj.addTComputedValues(obj.copy());
            }
        }
        else
        {
            copyObj.collTComputedValuess = null;
        }


        List<TAttachment> vTAttachments = getTAttachments();
        if (vTAttachments != null)
        {
            for (int i = 0; i < vTAttachments.size(); i++)
            {
                TAttachment obj =  vTAttachments.get(i);
                copyObj.addTAttachment(obj.copy());
            }
        }
        else
        {
            copyObj.collTAttachments = null;
        }


        List<TCost> vTCosts = getTCosts();
        if (vTCosts != null)
        {
            for (int i = 0; i < vTCosts.size(); i++)
            {
                TCost obj =  vTCosts.get(i);
                copyObj.addTCost(obj.copy());
            }
        }
        else
        {
            copyObj.collTCosts = null;
        }


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


        List<TBudget> vTBudgets = getTBudgets();
        if (vTBudgets != null)
        {
            for (int i = 0; i < vTBudgets.size(); i++)
            {
                TBudget obj =  vTBudgets.get(i);
                copyObj.addTBudget(obj.copy());
            }
        }
        else
        {
            copyObj.collTBudgets = null;
        }


        List<TActualEstimatedBudget> vTActualEstimatedBudgets = getTActualEstimatedBudgets();
        if (vTActualEstimatedBudgets != null)
        {
            for (int i = 0; i < vTActualEstimatedBudgets.size(); i++)
            {
                TActualEstimatedBudget obj =  vTActualEstimatedBudgets.get(i);
                copyObj.addTActualEstimatedBudget(obj.copy());
            }
        }
        else
        {
            copyObj.collTActualEstimatedBudgets = null;
        }


        List<TAttributeValue> vTAttributeValues = getTAttributeValues();
        if (vTAttributeValues != null)
        {
            for (int i = 0; i < vTAttributeValues.size(); i++)
            {
                TAttributeValue obj =  vTAttributeValues.get(i);
                copyObj.addTAttributeValue(obj.copy());
            }
        }
        else
        {
            copyObj.collTAttributeValues = null;
        }


        List<TWorkItemLink> vTWorkItemLinksRelatedByLinkPred = getTWorkItemLinksRelatedByLinkPred();
        if (vTWorkItemLinksRelatedByLinkPred != null)
        {
            for (int i = 0; i < vTWorkItemLinksRelatedByLinkPred.size(); i++)
            {
                TWorkItemLink obj =  vTWorkItemLinksRelatedByLinkPred.get(i);
                copyObj.addTWorkItemLinkRelatedByLinkPred(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkItemLinksRelatedByLinkPred = null;
        }


        List<TWorkItemLink> vTWorkItemLinksRelatedByLinkSucc = getTWorkItemLinksRelatedByLinkSucc();
        if (vTWorkItemLinksRelatedByLinkSucc != null)
        {
            for (int i = 0; i < vTWorkItemLinksRelatedByLinkSucc.size(); i++)
            {
                TWorkItemLink obj =  vTWorkItemLinksRelatedByLinkSucc.get(i);
                copyObj.addTWorkItemLinkRelatedByLinkSucc(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkItemLinksRelatedByLinkSucc = null;
        }


        List<TWorkItemLock> vTWorkItemLocks = getTWorkItemLocks();
        if (vTWorkItemLocks != null)
        {
            for (int i = 0; i < vTWorkItemLocks.size(); i++)
            {
                TWorkItemLock obj =  vTWorkItemLocks.get(i);
                copyObj.addTWorkItemLock(obj.copy());
            }
        }
        else
        {
            copyObj.collTWorkItemLocks = null;
        }


        List<TSummaryMail> vTSummaryMails = getTSummaryMails();
        if (vTSummaryMails != null)
        {
            for (int i = 0; i < vTSummaryMails.size(); i++)
            {
                TSummaryMail obj =  vTSummaryMails.get(i);
                copyObj.addTSummaryMail(obj.copy());
            }
        }
        else
        {
            copyObj.collTSummaryMails = null;
        }


        List<THistoryTransaction> vTHistoryTransactions = getTHistoryTransactions();
        if (vTHistoryTransactions != null)
        {
            for (int i = 0; i < vTHistoryTransactions.size(); i++)
            {
                THistoryTransaction obj =  vTHistoryTransactions.get(i);
                copyObj.addTHistoryTransaction(obj.copy());
            }
        }
        else
        {
            copyObj.collTHistoryTransactions = null;
        }


        List<TMSProjectTask> vTMSProjectTasks = getTMSProjectTasks();
        if (vTMSProjectTasks != null)
        {
            for (int i = 0; i < vTMSProjectTasks.size(); i++)
            {
                TMSProjectTask obj =  vTMSProjectTasks.get(i);
                copyObj.addTMSProjectTask(obj.copy());
            }
        }
        else
        {
            copyObj.collTMSProjectTasks = null;
        }


        List<TPersonBasket> vTPersonBaskets = getTPersonBaskets();
        if (vTPersonBaskets != null)
        {
            for (int i = 0; i < vTPersonBaskets.size(); i++)
            {
                TPersonBasket obj =  vTPersonBaskets.get(i);
                copyObj.addTPersonBasket(obj.copy());
            }
        }
        else
        {
            copyObj.collTPersonBaskets = null;
        }


        List<TLastVisitedItem> vTLastVisitedItems = getTLastVisitedItems();
        if (vTLastVisitedItems != null)
        {
            for (int i = 0; i < vTLastVisitedItems.size(); i++)
            {
                TLastVisitedItem obj =  vTLastVisitedItems.get(i);
                copyObj.addTLastVisitedItem(obj.copy());
            }
        }
        else
        {
            copyObj.collTLastVisitedItems = null;
        }


        List<TReadIssue> vTReadIssues = getTReadIssues();
        if (vTReadIssues != null)
        {
            for (int i = 0; i < vTReadIssues.size(); i++)
            {
                TReadIssue obj =  vTReadIssues.get(i);
                copyObj.addTReadIssue(obj.copy());
            }
        }
        else
        {
            copyObj.collTReadIssues = null;
        }


        List<TAttachmentVersion> vTAttachmentVersions = getTAttachmentVersions();
        if (vTAttachmentVersions != null)
        {
            for (int i = 0; i < vTAttachmentVersions.size(); i++)
            {
                TAttachmentVersion obj =  vTAttachmentVersions.get(i);
                copyObj.addTAttachmentVersion(obj.copy());
            }
        }
        else
        {
            copyObj.collTAttachmentVersions = null;
        }


        List<TItemTransition> vTItemTransitions = getTItemTransitions();
        if (vTItemTransitions != null)
        {
            for (int i = 0; i < vTItemTransitions.size(); i++)
            {
                TItemTransition obj =  vTItemTransitions.get(i);
                copyObj.addTItemTransition(obj.copy());
            }
        }
        else
        {
            copyObj.collTItemTransitions = null;
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
    protected TWorkItem copyInto(TWorkItem copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setOwnerID(ownerID);
        copyObj.setChangedByID(changedByID);
        copyObj.setOriginatorID(originatorID);
        copyObj.setResponsibleID(responsibleID);
        copyObj.setProjectID(projectID);
        copyObj.setProjectCategoryID(projectCategoryID);
        copyObj.setListTypeID(listTypeID);
        copyObj.setClassID(classID);
        copyObj.setPriorityID(priorityID);
        copyObj.setSeverityID(severityID);
        copyObj.setSuperiorworkitem(superiorworkitem);
        copyObj.setSynopsis(synopsis);
        copyObj.setDescription(description);
        copyObj.setReference(reference);
        copyObj.setLastEdit(lastEdit);
        copyObj.setReleaseNoticedID(releaseNoticedID);
        copyObj.setReleaseScheduledID(releaseScheduledID);
        copyObj.setBuild(build);
        copyObj.setStateID(stateID);
        copyObj.setStartDate(startDate);
        copyObj.setEndDate(endDate);
        copyObj.setSubmitterEmail(submitterEmail);
        copyObj.setCreated(created);
        copyObj.setActualStartDate(actualStartDate);
        copyObj.setActualEndDate(actualEndDate);
        copyObj.setLevel(level);
        copyObj.setAccessLevel(accessLevel);
        copyObj.setArchiveLevel(archiveLevel);
        copyObj.setEscalationLevel(escalationLevel);
        copyObj.setTaskIsMilestone(taskIsMilestone);
        copyObj.setTaskIsSubproject(taskIsSubproject);
        copyObj.setTaskIsSummary(taskIsSummary);
        copyObj.setTaskConstraint(taskConstraint);
        copyObj.setTaskConstraintDate(taskConstraintDate);
        copyObj.setPSPCode(pSPCode);
        copyObj.setIDNumber(iDNumber);
        copyObj.setWBSOnLevel(wBSOnLevel);
        copyObj.setReminderDate(reminderDate);
        copyObj.setTopDownStartDate(topDownStartDate);
        copyObj.setTopDownEndDate(topDownEndDate);
        copyObj.setOverBudget(overBudget);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TBaseLine> vTBaseLines = getTBaseLines(con);
        if (vTBaseLines != null)
        {
            for (int i = 0; i < vTBaseLines.size(); i++)
            {
                TBaseLine obj =  vTBaseLines.get(i);
                copyObj.addTBaseLine(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTBaseLines = null;
        }


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


        List<TStateChange> vTStateChanges = getTStateChanges(con);
        if (vTStateChanges != null)
        {
            for (int i = 0; i < vTStateChanges.size(); i++)
            {
                TStateChange obj =  vTStateChanges.get(i);
                copyObj.addTStateChange(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTStateChanges = null;
        }


        List<TTrail> vTTrails = getTTrails(con);
        if (vTTrails != null)
        {
            for (int i = 0; i < vTTrails.size(); i++)
            {
                TTrail obj =  vTTrails.get(i);
                copyObj.addTTrail(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTTrails = null;
        }


        List<TComputedValues> vTComputedValuess = getTComputedValuess(con);
        if (vTComputedValuess != null)
        {
            for (int i = 0; i < vTComputedValuess.size(); i++)
            {
                TComputedValues obj =  vTComputedValuess.get(i);
                copyObj.addTComputedValues(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTComputedValuess = null;
        }


        List<TAttachment> vTAttachments = getTAttachments(con);
        if (vTAttachments != null)
        {
            for (int i = 0; i < vTAttachments.size(); i++)
            {
                TAttachment obj =  vTAttachments.get(i);
                copyObj.addTAttachment(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAttachments = null;
        }


        List<TCost> vTCosts = getTCosts(con);
        if (vTCosts != null)
        {
            for (int i = 0; i < vTCosts.size(); i++)
            {
                TCost obj =  vTCosts.get(i);
                copyObj.addTCost(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTCosts = null;
        }


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


        List<TBudget> vTBudgets = getTBudgets(con);
        if (vTBudgets != null)
        {
            for (int i = 0; i < vTBudgets.size(); i++)
            {
                TBudget obj =  vTBudgets.get(i);
                copyObj.addTBudget(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTBudgets = null;
        }


        List<TActualEstimatedBudget> vTActualEstimatedBudgets = getTActualEstimatedBudgets(con);
        if (vTActualEstimatedBudgets != null)
        {
            for (int i = 0; i < vTActualEstimatedBudgets.size(); i++)
            {
                TActualEstimatedBudget obj =  vTActualEstimatedBudgets.get(i);
                copyObj.addTActualEstimatedBudget(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTActualEstimatedBudgets = null;
        }


        List<TAttributeValue> vTAttributeValues = getTAttributeValues(con);
        if (vTAttributeValues != null)
        {
            for (int i = 0; i < vTAttributeValues.size(); i++)
            {
                TAttributeValue obj =  vTAttributeValues.get(i);
                copyObj.addTAttributeValue(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAttributeValues = null;
        }


        List<TWorkItemLink> vTWorkItemLinksRelatedByLinkPred = getTWorkItemLinksRelatedByLinkPred(con);
        if (vTWorkItemLinksRelatedByLinkPred != null)
        {
            for (int i = 0; i < vTWorkItemLinksRelatedByLinkPred.size(); i++)
            {
                TWorkItemLink obj =  vTWorkItemLinksRelatedByLinkPred.get(i);
                copyObj.addTWorkItemLinkRelatedByLinkPred(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkItemLinksRelatedByLinkPred = null;
        }


        List<TWorkItemLink> vTWorkItemLinksRelatedByLinkSucc = getTWorkItemLinksRelatedByLinkSucc(con);
        if (vTWorkItemLinksRelatedByLinkSucc != null)
        {
            for (int i = 0; i < vTWorkItemLinksRelatedByLinkSucc.size(); i++)
            {
                TWorkItemLink obj =  vTWorkItemLinksRelatedByLinkSucc.get(i);
                copyObj.addTWorkItemLinkRelatedByLinkSucc(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkItemLinksRelatedByLinkSucc = null;
        }


        List<TWorkItemLock> vTWorkItemLocks = getTWorkItemLocks(con);
        if (vTWorkItemLocks != null)
        {
            for (int i = 0; i < vTWorkItemLocks.size(); i++)
            {
                TWorkItemLock obj =  vTWorkItemLocks.get(i);
                copyObj.addTWorkItemLock(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWorkItemLocks = null;
        }


        List<TSummaryMail> vTSummaryMails = getTSummaryMails(con);
        if (vTSummaryMails != null)
        {
            for (int i = 0; i < vTSummaryMails.size(); i++)
            {
                TSummaryMail obj =  vTSummaryMails.get(i);
                copyObj.addTSummaryMail(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTSummaryMails = null;
        }


        List<THistoryTransaction> vTHistoryTransactions = getTHistoryTransactions(con);
        if (vTHistoryTransactions != null)
        {
            for (int i = 0; i < vTHistoryTransactions.size(); i++)
            {
                THistoryTransaction obj =  vTHistoryTransactions.get(i);
                copyObj.addTHistoryTransaction(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTHistoryTransactions = null;
        }


        List<TMSProjectTask> vTMSProjectTasks = getTMSProjectTasks(con);
        if (vTMSProjectTasks != null)
        {
            for (int i = 0; i < vTMSProjectTasks.size(); i++)
            {
                TMSProjectTask obj =  vTMSProjectTasks.get(i);
                copyObj.addTMSProjectTask(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTMSProjectTasks = null;
        }


        List<TPersonBasket> vTPersonBaskets = getTPersonBaskets(con);
        if (vTPersonBaskets != null)
        {
            for (int i = 0; i < vTPersonBaskets.size(); i++)
            {
                TPersonBasket obj =  vTPersonBaskets.get(i);
                copyObj.addTPersonBasket(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTPersonBaskets = null;
        }


        List<TLastVisitedItem> vTLastVisitedItems = getTLastVisitedItems(con);
        if (vTLastVisitedItems != null)
        {
            for (int i = 0; i < vTLastVisitedItems.size(); i++)
            {
                TLastVisitedItem obj =  vTLastVisitedItems.get(i);
                copyObj.addTLastVisitedItem(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTLastVisitedItems = null;
        }


        List<TReadIssue> vTReadIssues = getTReadIssues(con);
        if (vTReadIssues != null)
        {
            for (int i = 0; i < vTReadIssues.size(); i++)
            {
                TReadIssue obj =  vTReadIssues.get(i);
                copyObj.addTReadIssue(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTReadIssues = null;
        }


        List<TAttachmentVersion> vTAttachmentVersions = getTAttachmentVersions(con);
        if (vTAttachmentVersions != null)
        {
            for (int i = 0; i < vTAttachmentVersions.size(); i++)
            {
                TAttachmentVersion obj =  vTAttachmentVersions.get(i);
                copyObj.addTAttachmentVersion(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTAttachmentVersions = null;
        }


        List<TItemTransition> vTItemTransitions = getTItemTransitions(con);
        if (vTItemTransitions != null)
        {
            for (int i = 0; i < vTItemTransitions.size(); i++)
            {
                TItemTransition obj =  vTItemTransitions.get(i);
                copyObj.addTItemTransition(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTItemTransitions = null;
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
    public TWorkItemPeer getPeer()
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
        return TWorkItemPeer.getTableMap();
    }

  
    /**
     * Creates a TWorkItemBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TWorkItemBean with the contents of this object
     */
    public TWorkItemBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TWorkItemBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TWorkItemBean with the contents of this object
     */
    public TWorkItemBean getBean(IdentityMap createdBeans)
    {
        TWorkItemBean result = (TWorkItemBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TWorkItemBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setOwnerID(getOwnerID());
        result.setChangedByID(getChangedByID());
        result.setOriginatorID(getOriginatorID());
        result.setResponsibleID(getResponsibleID());
        result.setProjectID(getProjectID());
        result.setProjectCategoryID(getProjectCategoryID());
        result.setListTypeID(getListTypeID());
        result.setClassID(getClassID());
        result.setPriorityID(getPriorityID());
        result.setSeverityID(getSeverityID());
        result.setSuperiorworkitem(getSuperiorworkitem());
        result.setSynopsis(getSynopsis());
        result.setDescription(getDescription());
        result.setReference(getReference());
        result.setLastEdit(getLastEdit());
        result.setReleaseNoticedID(getReleaseNoticedID());
        result.setReleaseScheduledID(getReleaseScheduledID());
        result.setBuild(getBuild());
        result.setStateID(getStateID());
        result.setStartDate(getStartDate());
        result.setEndDate(getEndDate());
        result.setSubmitterEmail(getSubmitterEmail());
        result.setCreated(getCreated());
        result.setActualStartDate(getActualStartDate());
        result.setActualEndDate(getActualEndDate());
        result.setLevel(getLevel());
        result.setAccessLevel(getAccessLevel());
        result.setArchiveLevel(getArchiveLevel());
        result.setEscalationLevel(getEscalationLevel());
        result.setTaskIsMilestone(getTaskIsMilestone());
        result.setTaskIsSubproject(getTaskIsSubproject());
        result.setTaskIsSummary(getTaskIsSummary());
        result.setTaskConstraint(getTaskConstraint());
        result.setTaskConstraintDate(getTaskConstraintDate());
        result.setPSPCode(getPSPCode());
        result.setIDNumber(getIDNumber());
        result.setWBSOnLevel(getWBSOnLevel());
        result.setReminderDate(getReminderDate());
        result.setTopDownStartDate(getTopDownStartDate());
        result.setTopDownEndDate(getTopDownEndDate());
        result.setOverBudget(getOverBudget());
        result.setUuid(getUuid());



        if (collTBaseLines != null)
        {
            List<TBaseLineBean> relatedBeans = new ArrayList<TBaseLineBean>(collTBaseLines.size());
            for (Iterator<TBaseLine> collTBaseLinesIt = collTBaseLines.iterator(); collTBaseLinesIt.hasNext(); )
            {
                TBaseLine related = (TBaseLine) collTBaseLinesIt.next();
                TBaseLineBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTBaseLineBeans(relatedBeans);
        }


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


        if (collTStateChanges != null)
        {
            List<TStateChangeBean> relatedBeans = new ArrayList<TStateChangeBean>(collTStateChanges.size());
            for (Iterator<TStateChange> collTStateChangesIt = collTStateChanges.iterator(); collTStateChangesIt.hasNext(); )
            {
                TStateChange related = (TStateChange) collTStateChangesIt.next();
                TStateChangeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTStateChangeBeans(relatedBeans);
        }


        if (collTTrails != null)
        {
            List<TTrailBean> relatedBeans = new ArrayList<TTrailBean>(collTTrails.size());
            for (Iterator<TTrail> collTTrailsIt = collTTrails.iterator(); collTTrailsIt.hasNext(); )
            {
                TTrail related = (TTrail) collTTrailsIt.next();
                TTrailBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTTrailBeans(relatedBeans);
        }


        if (collTComputedValuess != null)
        {
            List<TComputedValuesBean> relatedBeans = new ArrayList<TComputedValuesBean>(collTComputedValuess.size());
            for (Iterator<TComputedValues> collTComputedValuessIt = collTComputedValuess.iterator(); collTComputedValuessIt.hasNext(); )
            {
                TComputedValues related = (TComputedValues) collTComputedValuessIt.next();
                TComputedValuesBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTComputedValuesBeans(relatedBeans);
        }


        if (collTAttachments != null)
        {
            List<TAttachmentBean> relatedBeans = new ArrayList<TAttachmentBean>(collTAttachments.size());
            for (Iterator<TAttachment> collTAttachmentsIt = collTAttachments.iterator(); collTAttachmentsIt.hasNext(); )
            {
                TAttachment related = (TAttachment) collTAttachmentsIt.next();
                TAttachmentBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAttachmentBeans(relatedBeans);
        }


        if (collTCosts != null)
        {
            List<TCostBean> relatedBeans = new ArrayList<TCostBean>(collTCosts.size());
            for (Iterator<TCost> collTCostsIt = collTCosts.iterator(); collTCostsIt.hasNext(); )
            {
                TCost related = (TCost) collTCostsIt.next();
                TCostBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTCostBeans(relatedBeans);
        }


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


        if (collTBudgets != null)
        {
            List<TBudgetBean> relatedBeans = new ArrayList<TBudgetBean>(collTBudgets.size());
            for (Iterator<TBudget> collTBudgetsIt = collTBudgets.iterator(); collTBudgetsIt.hasNext(); )
            {
                TBudget related = (TBudget) collTBudgetsIt.next();
                TBudgetBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTBudgetBeans(relatedBeans);
        }


        if (collTActualEstimatedBudgets != null)
        {
            List<TActualEstimatedBudgetBean> relatedBeans = new ArrayList<TActualEstimatedBudgetBean>(collTActualEstimatedBudgets.size());
            for (Iterator<TActualEstimatedBudget> collTActualEstimatedBudgetsIt = collTActualEstimatedBudgets.iterator(); collTActualEstimatedBudgetsIt.hasNext(); )
            {
                TActualEstimatedBudget related = (TActualEstimatedBudget) collTActualEstimatedBudgetsIt.next();
                TActualEstimatedBudgetBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTActualEstimatedBudgetBeans(relatedBeans);
        }


        if (collTAttributeValues != null)
        {
            List<TAttributeValueBean> relatedBeans = new ArrayList<TAttributeValueBean>(collTAttributeValues.size());
            for (Iterator<TAttributeValue> collTAttributeValuesIt = collTAttributeValues.iterator(); collTAttributeValuesIt.hasNext(); )
            {
                TAttributeValue related = (TAttributeValue) collTAttributeValuesIt.next();
                TAttributeValueBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAttributeValueBeans(relatedBeans);
        }


        if (collTWorkItemLinksRelatedByLinkPred != null)
        {
            List<TWorkItemLinkBean> relatedBeans = new ArrayList<TWorkItemLinkBean>(collTWorkItemLinksRelatedByLinkPred.size());
            for (Iterator<TWorkItemLink> collTWorkItemLinksRelatedByLinkPredIt = collTWorkItemLinksRelatedByLinkPred.iterator(); collTWorkItemLinksRelatedByLinkPredIt.hasNext(); )
            {
                TWorkItemLink related = (TWorkItemLink) collTWorkItemLinksRelatedByLinkPredIt.next();
                TWorkItemLinkBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkItemLinkBeansRelatedByLinkPred(relatedBeans);
        }


        if (collTWorkItemLinksRelatedByLinkSucc != null)
        {
            List<TWorkItemLinkBean> relatedBeans = new ArrayList<TWorkItemLinkBean>(collTWorkItemLinksRelatedByLinkSucc.size());
            for (Iterator<TWorkItemLink> collTWorkItemLinksRelatedByLinkSuccIt = collTWorkItemLinksRelatedByLinkSucc.iterator(); collTWorkItemLinksRelatedByLinkSuccIt.hasNext(); )
            {
                TWorkItemLink related = (TWorkItemLink) collTWorkItemLinksRelatedByLinkSuccIt.next();
                TWorkItemLinkBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkItemLinkBeansRelatedByLinkSucc(relatedBeans);
        }


        if (collTWorkItemLocks != null)
        {
            List<TWorkItemLockBean> relatedBeans = new ArrayList<TWorkItemLockBean>(collTWorkItemLocks.size());
            for (Iterator<TWorkItemLock> collTWorkItemLocksIt = collTWorkItemLocks.iterator(); collTWorkItemLocksIt.hasNext(); )
            {
                TWorkItemLock related = (TWorkItemLock) collTWorkItemLocksIt.next();
                TWorkItemLockBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWorkItemLockBeans(relatedBeans);
        }


        if (collTSummaryMails != null)
        {
            List<TSummaryMailBean> relatedBeans = new ArrayList<TSummaryMailBean>(collTSummaryMails.size());
            for (Iterator<TSummaryMail> collTSummaryMailsIt = collTSummaryMails.iterator(); collTSummaryMailsIt.hasNext(); )
            {
                TSummaryMail related = (TSummaryMail) collTSummaryMailsIt.next();
                TSummaryMailBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTSummaryMailBeans(relatedBeans);
        }


        if (collTHistoryTransactions != null)
        {
            List<THistoryTransactionBean> relatedBeans = new ArrayList<THistoryTransactionBean>(collTHistoryTransactions.size());
            for (Iterator<THistoryTransaction> collTHistoryTransactionsIt = collTHistoryTransactions.iterator(); collTHistoryTransactionsIt.hasNext(); )
            {
                THistoryTransaction related = (THistoryTransaction) collTHistoryTransactionsIt.next();
                THistoryTransactionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTHistoryTransactionBeans(relatedBeans);
        }


        if (collTMSProjectTasks != null)
        {
            List<TMSProjectTaskBean> relatedBeans = new ArrayList<TMSProjectTaskBean>(collTMSProjectTasks.size());
            for (Iterator<TMSProjectTask> collTMSProjectTasksIt = collTMSProjectTasks.iterator(); collTMSProjectTasksIt.hasNext(); )
            {
                TMSProjectTask related = (TMSProjectTask) collTMSProjectTasksIt.next();
                TMSProjectTaskBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTMSProjectTaskBeans(relatedBeans);
        }


        if (collTPersonBaskets != null)
        {
            List<TPersonBasketBean> relatedBeans = new ArrayList<TPersonBasketBean>(collTPersonBaskets.size());
            for (Iterator<TPersonBasket> collTPersonBasketsIt = collTPersonBaskets.iterator(); collTPersonBasketsIt.hasNext(); )
            {
                TPersonBasket related = (TPersonBasket) collTPersonBasketsIt.next();
                TPersonBasketBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTPersonBasketBeans(relatedBeans);
        }


        if (collTLastVisitedItems != null)
        {
            List<TLastVisitedItemBean> relatedBeans = new ArrayList<TLastVisitedItemBean>(collTLastVisitedItems.size());
            for (Iterator<TLastVisitedItem> collTLastVisitedItemsIt = collTLastVisitedItems.iterator(); collTLastVisitedItemsIt.hasNext(); )
            {
                TLastVisitedItem related = (TLastVisitedItem) collTLastVisitedItemsIt.next();
                TLastVisitedItemBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTLastVisitedItemBeans(relatedBeans);
        }


        if (collTReadIssues != null)
        {
            List<TReadIssueBean> relatedBeans = new ArrayList<TReadIssueBean>(collTReadIssues.size());
            for (Iterator<TReadIssue> collTReadIssuesIt = collTReadIssues.iterator(); collTReadIssuesIt.hasNext(); )
            {
                TReadIssue related = (TReadIssue) collTReadIssuesIt.next();
                TReadIssueBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTReadIssueBeans(relatedBeans);
        }


        if (collTAttachmentVersions != null)
        {
            List<TAttachmentVersionBean> relatedBeans = new ArrayList<TAttachmentVersionBean>(collTAttachmentVersions.size());
            for (Iterator<TAttachmentVersion> collTAttachmentVersionsIt = collTAttachmentVersions.iterator(); collTAttachmentVersionsIt.hasNext(); )
            {
                TAttachmentVersion related = (TAttachmentVersion) collTAttachmentVersionsIt.next();
                TAttachmentVersionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTAttachmentVersionBeans(relatedBeans);
        }


        if (collTItemTransitions != null)
        {
            List<TItemTransitionBean> relatedBeans = new ArrayList<TItemTransitionBean>(collTItemTransitions.size());
            for (Iterator<TItemTransition> collTItemTransitionsIt = collTItemTransitions.iterator(); collTItemTransitionsIt.hasNext(); )
            {
                TItemTransition related = (TItemTransition) collTItemTransitionsIt.next();
                TItemTransitionBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTItemTransitionBeans(relatedBeans);
        }




        if (aTPersonRelatedByOwnerID != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByOwnerID.getBean(createdBeans);
            result.setTPersonBeanRelatedByOwnerID(relatedBean);
        }



        if (aTPersonRelatedByChangedByID != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByChangedByID.getBean(createdBeans);
            result.setTPersonBeanRelatedByChangedByID(relatedBean);
        }



        if (aTPersonRelatedByOriginatorID != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByOriginatorID.getBean(createdBeans);
            result.setTPersonBeanRelatedByOriginatorID(relatedBean);
        }



        if (aTPersonRelatedByResponsibleID != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByResponsibleID.getBean(createdBeans);
            result.setTPersonBeanRelatedByResponsibleID(relatedBean);
        }



        if (aTProjectCategory != null)
        {
            TProjectCategoryBean relatedBean = aTProjectCategory.getBean(createdBeans);
            result.setTProjectCategoryBean(relatedBean);
        }



        if (aTListType != null)
        {
            TListTypeBean relatedBean = aTListType.getBean(createdBeans);
            result.setTListTypeBean(relatedBean);
        }



        if (aTClass != null)
        {
            TClassBean relatedBean = aTClass.getBean(createdBeans);
            result.setTClassBean(relatedBean);
        }



        if (aTPriority != null)
        {
            TPriorityBean relatedBean = aTPriority.getBean(createdBeans);
            result.setTPriorityBean(relatedBean);
        }



        if (aTSeverity != null)
        {
            TSeverityBean relatedBean = aTSeverity.getBean(createdBeans);
            result.setTSeverityBean(relatedBean);
        }



        if (aTReleaseRelatedByReleaseNoticedID != null)
        {
            TReleaseBean relatedBean = aTReleaseRelatedByReleaseNoticedID.getBean(createdBeans);
            result.setTReleaseBeanRelatedByReleaseNoticedID(relatedBean);
        }



        if (aTReleaseRelatedByReleaseScheduledID != null)
        {
            TReleaseBean relatedBean = aTReleaseRelatedByReleaseScheduledID.getBean(createdBeans);
            result.setTReleaseBeanRelatedByReleaseScheduledID(relatedBean);
        }



        if (aTState != null)
        {
            TStateBean relatedBean = aTState.getBean(createdBeans);
            result.setTStateBean(relatedBean);
        }



        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }



        if (aTWorkItemRelatedBySuperiorworkitem != null)
        {
            TWorkItemBean relatedBean = aTWorkItemRelatedBySuperiorworkitem.getBean(createdBeans);
            result.setTWorkItemBeanRelatedBySuperiorworkitem(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TWorkItem with the contents
     * of a TWorkItemBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TWorkItemBean which contents are used to create
     *        the resulting class
     * @return an instance of TWorkItem with the contents of bean
     */
    public static TWorkItem createTWorkItem(TWorkItemBean bean)
        throws TorqueException
    {
        return createTWorkItem(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TWorkItem with the contents
     * of a TWorkItemBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TWorkItemBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TWorkItem with the contents of bean
     */

    public static TWorkItem createTWorkItem(TWorkItemBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TWorkItem result = (TWorkItem) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TWorkItem();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setOwnerID(bean.getOwnerID());
        result.setChangedByID(bean.getChangedByID());
        result.setOriginatorID(bean.getOriginatorID());
        result.setResponsibleID(bean.getResponsibleID());
        result.setProjectID(bean.getProjectID());
        result.setProjectCategoryID(bean.getProjectCategoryID());
        result.setListTypeID(bean.getListTypeID());
        result.setClassID(bean.getClassID());
        result.setPriorityID(bean.getPriorityID());
        result.setSeverityID(bean.getSeverityID());
        result.setSuperiorworkitem(bean.getSuperiorworkitem());
        result.setSynopsis(bean.getSynopsis());
        result.setDescription(bean.getDescription());
        result.setReference(bean.getReference());
        result.setLastEdit(bean.getLastEdit());
        result.setReleaseNoticedID(bean.getReleaseNoticedID());
        result.setReleaseScheduledID(bean.getReleaseScheduledID());
        result.setBuild(bean.getBuild());
        result.setStateID(bean.getStateID());
        result.setStartDate(bean.getStartDate());
        result.setEndDate(bean.getEndDate());
        result.setSubmitterEmail(bean.getSubmitterEmail());
        result.setCreated(bean.getCreated());
        result.setActualStartDate(bean.getActualStartDate());
        result.setActualEndDate(bean.getActualEndDate());
        result.setLevel(bean.getLevel());
        result.setAccessLevel(bean.getAccessLevel());
        result.setArchiveLevel(bean.getArchiveLevel());
        result.setEscalationLevel(bean.getEscalationLevel());
        result.setTaskIsMilestone(bean.getTaskIsMilestone());
        result.setTaskIsSubproject(bean.getTaskIsSubproject());
        result.setTaskIsSummary(bean.getTaskIsSummary());
        result.setTaskConstraint(bean.getTaskConstraint());
        result.setTaskConstraintDate(bean.getTaskConstraintDate());
        result.setPSPCode(bean.getPSPCode());
        result.setIDNumber(bean.getIDNumber());
        result.setWBSOnLevel(bean.getWBSOnLevel());
        result.setReminderDate(bean.getReminderDate());
        result.setTopDownStartDate(bean.getTopDownStartDate());
        result.setTopDownEndDate(bean.getTopDownEndDate());
        result.setOverBudget(bean.getOverBudget());
        result.setUuid(bean.getUuid());



        {
            List<TBaseLineBean> relatedBeans = bean.getTBaseLineBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TBaseLineBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TBaseLineBean relatedBean =  relatedBeansIt.next();
                    TBaseLine related = TBaseLine.createTBaseLine(relatedBean, createdObjects);
                    result.addTBaseLineFromBean(related);
                }
            }
        }


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
            List<TStateChangeBean> relatedBeans = bean.getTStateChangeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TStateChangeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TStateChangeBean relatedBean =  relatedBeansIt.next();
                    TStateChange related = TStateChange.createTStateChange(relatedBean, createdObjects);
                    result.addTStateChangeFromBean(related);
                }
            }
        }


        {
            List<TTrailBean> relatedBeans = bean.getTTrailBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TTrailBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TTrailBean relatedBean =  relatedBeansIt.next();
                    TTrail related = TTrail.createTTrail(relatedBean, createdObjects);
                    result.addTTrailFromBean(related);
                }
            }
        }


        {
            List<TComputedValuesBean> relatedBeans = bean.getTComputedValuesBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TComputedValuesBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TComputedValuesBean relatedBean =  relatedBeansIt.next();
                    TComputedValues related = TComputedValues.createTComputedValues(relatedBean, createdObjects);
                    result.addTComputedValuesFromBean(related);
                }
            }
        }


        {
            List<TAttachmentBean> relatedBeans = bean.getTAttachmentBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAttachmentBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAttachmentBean relatedBean =  relatedBeansIt.next();
                    TAttachment related = TAttachment.createTAttachment(relatedBean, createdObjects);
                    result.addTAttachmentFromBean(related);
                }
            }
        }


        {
            List<TCostBean> relatedBeans = bean.getTCostBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TCostBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TCostBean relatedBean =  relatedBeansIt.next();
                    TCost related = TCost.createTCost(relatedBean, createdObjects);
                    result.addTCostFromBean(related);
                }
            }
        }


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
            List<TBudgetBean> relatedBeans = bean.getTBudgetBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TBudgetBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TBudgetBean relatedBean =  relatedBeansIt.next();
                    TBudget related = TBudget.createTBudget(relatedBean, createdObjects);
                    result.addTBudgetFromBean(related);
                }
            }
        }


        {
            List<TActualEstimatedBudgetBean> relatedBeans = bean.getTActualEstimatedBudgetBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TActualEstimatedBudgetBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TActualEstimatedBudgetBean relatedBean =  relatedBeansIt.next();
                    TActualEstimatedBudget related = TActualEstimatedBudget.createTActualEstimatedBudget(relatedBean, createdObjects);
                    result.addTActualEstimatedBudgetFromBean(related);
                }
            }
        }


        {
            List<TAttributeValueBean> relatedBeans = bean.getTAttributeValueBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAttributeValueBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAttributeValueBean relatedBean =  relatedBeansIt.next();
                    TAttributeValue related = TAttributeValue.createTAttributeValue(relatedBean, createdObjects);
                    result.addTAttributeValueFromBean(related);
                }
            }
        }


        {
            List<TWorkItemLinkBean> relatedBeans = bean.getTWorkItemLinkBeansRelatedByLinkPred();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkItemLinkBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkItemLinkBean relatedBean =  relatedBeansIt.next();
                    TWorkItemLink related = TWorkItemLink.createTWorkItemLink(relatedBean, createdObjects);
                    result.addTWorkItemLinkRelatedByLinkPredFromBean(related);
                }
            }
        }


        {
            List<TWorkItemLinkBean> relatedBeans = bean.getTWorkItemLinkBeansRelatedByLinkSucc();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkItemLinkBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkItemLinkBean relatedBean =  relatedBeansIt.next();
                    TWorkItemLink related = TWorkItemLink.createTWorkItemLink(relatedBean, createdObjects);
                    result.addTWorkItemLinkRelatedByLinkSuccFromBean(related);
                }
            }
        }


        {
            List<TWorkItemLockBean> relatedBeans = bean.getTWorkItemLockBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWorkItemLockBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWorkItemLockBean relatedBean =  relatedBeansIt.next();
                    TWorkItemLock related = TWorkItemLock.createTWorkItemLock(relatedBean, createdObjects);
                    result.addTWorkItemLockFromBean(related);
                }
            }
        }


        {
            List<TSummaryMailBean> relatedBeans = bean.getTSummaryMailBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TSummaryMailBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TSummaryMailBean relatedBean =  relatedBeansIt.next();
                    TSummaryMail related = TSummaryMail.createTSummaryMail(relatedBean, createdObjects);
                    result.addTSummaryMailFromBean(related);
                }
            }
        }


        {
            List<THistoryTransactionBean> relatedBeans = bean.getTHistoryTransactionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<THistoryTransactionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    THistoryTransactionBean relatedBean =  relatedBeansIt.next();
                    THistoryTransaction related = THistoryTransaction.createTHistoryTransaction(relatedBean, createdObjects);
                    result.addTHistoryTransactionFromBean(related);
                }
            }
        }


        {
            List<TMSProjectTaskBean> relatedBeans = bean.getTMSProjectTaskBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TMSProjectTaskBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TMSProjectTaskBean relatedBean =  relatedBeansIt.next();
                    TMSProjectTask related = TMSProjectTask.createTMSProjectTask(relatedBean, createdObjects);
                    result.addTMSProjectTaskFromBean(related);
                }
            }
        }


        {
            List<TPersonBasketBean> relatedBeans = bean.getTPersonBasketBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TPersonBasketBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TPersonBasketBean relatedBean =  relatedBeansIt.next();
                    TPersonBasket related = TPersonBasket.createTPersonBasket(relatedBean, createdObjects);
                    result.addTPersonBasketFromBean(related);
                }
            }
        }


        {
            List<TLastVisitedItemBean> relatedBeans = bean.getTLastVisitedItemBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TLastVisitedItemBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TLastVisitedItemBean relatedBean =  relatedBeansIt.next();
                    TLastVisitedItem related = TLastVisitedItem.createTLastVisitedItem(relatedBean, createdObjects);
                    result.addTLastVisitedItemFromBean(related);
                }
            }
        }


        {
            List<TReadIssueBean> relatedBeans = bean.getTReadIssueBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TReadIssueBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TReadIssueBean relatedBean =  relatedBeansIt.next();
                    TReadIssue related = TReadIssue.createTReadIssue(relatedBean, createdObjects);
                    result.addTReadIssueFromBean(related);
                }
            }
        }


        {
            List<TAttachmentVersionBean> relatedBeans = bean.getTAttachmentVersionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TAttachmentVersionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TAttachmentVersionBean relatedBean =  relatedBeansIt.next();
                    TAttachmentVersion related = TAttachmentVersion.createTAttachmentVersion(relatedBean, createdObjects);
                    result.addTAttachmentVersionFromBean(related);
                }
            }
        }


        {
            List<TItemTransitionBean> relatedBeans = bean.getTItemTransitionBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TItemTransitionBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TItemTransitionBean relatedBean =  relatedBeansIt.next();
                    TItemTransition related = TItemTransition.createTItemTransition(relatedBean, createdObjects);
                    result.addTItemTransitionFromBean(related);
                }
            }
        }




        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByOwnerID();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByOwnerID(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByChangedByID();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByChangedByID(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByOriginatorID();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByOriginatorID(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByResponsibleID();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByResponsibleID(relatedObject);
            }
        }



        {
            TProjectCategoryBean relatedBean = bean.getTProjectCategoryBean();
            if (relatedBean != null)
            {
                TProjectCategory relatedObject = TProjectCategory.createTProjectCategory(relatedBean, createdObjects);
                result.setTProjectCategory(relatedObject);
            }
        }



        {
            TListTypeBean relatedBean = bean.getTListTypeBean();
            if (relatedBean != null)
            {
                TListType relatedObject = TListType.createTListType(relatedBean, createdObjects);
                result.setTListType(relatedObject);
            }
        }



        {
            TClassBean relatedBean = bean.getTClassBean();
            if (relatedBean != null)
            {
                TClass relatedObject = TClass.createTClass(relatedBean, createdObjects);
                result.setTClass(relatedObject);
            }
        }



        {
            TPriorityBean relatedBean = bean.getTPriorityBean();
            if (relatedBean != null)
            {
                TPriority relatedObject = TPriority.createTPriority(relatedBean, createdObjects);
                result.setTPriority(relatedObject);
            }
        }



        {
            TSeverityBean relatedBean = bean.getTSeverityBean();
            if (relatedBean != null)
            {
                TSeverity relatedObject = TSeverity.createTSeverity(relatedBean, createdObjects);
                result.setTSeverity(relatedObject);
            }
        }



        {
            TReleaseBean relatedBean = bean.getTReleaseBeanRelatedByReleaseNoticedID();
            if (relatedBean != null)
            {
                TRelease relatedObject = TRelease.createTRelease(relatedBean, createdObjects);
                result.setTReleaseRelatedByReleaseNoticedID(relatedObject);
            }
        }



        {
            TReleaseBean relatedBean = bean.getTReleaseBeanRelatedByReleaseScheduledID();
            if (relatedBean != null)
            {
                TRelease relatedObject = TRelease.createTRelease(relatedBean, createdObjects);
                result.setTReleaseRelatedByReleaseScheduledID(relatedObject);
            }
        }



        {
            TStateBean relatedBean = bean.getTStateBean();
            if (relatedBean != null)
            {
                TState relatedObject = TState.createTState(relatedBean, createdObjects);
                result.setTState(relatedObject);
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



        {
            TWorkItemBean relatedBean = bean.getTWorkItemBeanRelatedBySuperiorworkitem();
            if (relatedBean != null)
            {
                TWorkItem relatedObject = TWorkItem.createTWorkItem(relatedBean, createdObjects);
                result.setTWorkItemRelatedBySuperiorworkitem(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TBaseLine object to this object.
     * through the TBaseLine foreign key attribute
     *
     * @param toAdd TBaseLine
     */
    protected void addTBaseLineFromBean(TBaseLine toAdd)
    {
        initTBaseLines();
        collTBaseLines.add(toAdd);
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
     * Method called to associate a TStateChange object to this object.
     * through the TStateChange foreign key attribute
     *
     * @param toAdd TStateChange
     */
    protected void addTStateChangeFromBean(TStateChange toAdd)
    {
        initTStateChanges();
        collTStateChanges.add(toAdd);
    }


    /**
     * Method called to associate a TTrail object to this object.
     * through the TTrail foreign key attribute
     *
     * @param toAdd TTrail
     */
    protected void addTTrailFromBean(TTrail toAdd)
    {
        initTTrails();
        collTTrails.add(toAdd);
    }


    /**
     * Method called to associate a TComputedValues object to this object.
     * through the TComputedValues foreign key attribute
     *
     * @param toAdd TComputedValues
     */
    protected void addTComputedValuesFromBean(TComputedValues toAdd)
    {
        initTComputedValuess();
        collTComputedValuess.add(toAdd);
    }


    /**
     * Method called to associate a TAttachment object to this object.
     * through the TAttachment foreign key attribute
     *
     * @param toAdd TAttachment
     */
    protected void addTAttachmentFromBean(TAttachment toAdd)
    {
        initTAttachments();
        collTAttachments.add(toAdd);
    }


    /**
     * Method called to associate a TCost object to this object.
     * through the TCost foreign key attribute
     *
     * @param toAdd TCost
     */
    protected void addTCostFromBean(TCost toAdd)
    {
        initTCosts();
        collTCosts.add(toAdd);
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
     * Method called to associate a TBudget object to this object.
     * through the TBudget foreign key attribute
     *
     * @param toAdd TBudget
     */
    protected void addTBudgetFromBean(TBudget toAdd)
    {
        initTBudgets();
        collTBudgets.add(toAdd);
    }


    /**
     * Method called to associate a TActualEstimatedBudget object to this object.
     * through the TActualEstimatedBudget foreign key attribute
     *
     * @param toAdd TActualEstimatedBudget
     */
    protected void addTActualEstimatedBudgetFromBean(TActualEstimatedBudget toAdd)
    {
        initTActualEstimatedBudgets();
        collTActualEstimatedBudgets.add(toAdd);
    }


    /**
     * Method called to associate a TAttributeValue object to this object.
     * through the TAttributeValue foreign key attribute
     *
     * @param toAdd TAttributeValue
     */
    protected void addTAttributeValueFromBean(TAttributeValue toAdd)
    {
        initTAttributeValues();
        collTAttributeValues.add(toAdd);
    }


    /**
     * Method called to associate a TWorkItemLink object to this object.
     * through the TWorkItemLink foreign key attribute
     *
     * @param toAdd TWorkItemLink
     */
    protected void addTWorkItemLinkRelatedByLinkPredFromBean(TWorkItemLink toAdd)
    {
        initTWorkItemLinksRelatedByLinkPred();
        collTWorkItemLinksRelatedByLinkPred.add(toAdd);
    }


    /**
     * Method called to associate a TWorkItemLink object to this object.
     * through the TWorkItemLink foreign key attribute
     *
     * @param toAdd TWorkItemLink
     */
    protected void addTWorkItemLinkRelatedByLinkSuccFromBean(TWorkItemLink toAdd)
    {
        initTWorkItemLinksRelatedByLinkSucc();
        collTWorkItemLinksRelatedByLinkSucc.add(toAdd);
    }


    /**
     * Method called to associate a TWorkItemLock object to this object.
     * through the TWorkItemLock foreign key attribute
     *
     * @param toAdd TWorkItemLock
     */
    protected void addTWorkItemLockFromBean(TWorkItemLock toAdd)
    {
        initTWorkItemLocks();
        collTWorkItemLocks.add(toAdd);
    }


    /**
     * Method called to associate a TSummaryMail object to this object.
     * through the TSummaryMail foreign key attribute
     *
     * @param toAdd TSummaryMail
     */
    protected void addTSummaryMailFromBean(TSummaryMail toAdd)
    {
        initTSummaryMails();
        collTSummaryMails.add(toAdd);
    }


    /**
     * Method called to associate a THistoryTransaction object to this object.
     * through the THistoryTransaction foreign key attribute
     *
     * @param toAdd THistoryTransaction
     */
    protected void addTHistoryTransactionFromBean(THistoryTransaction toAdd)
    {
        initTHistoryTransactions();
        collTHistoryTransactions.add(toAdd);
    }


    /**
     * Method called to associate a TMSProjectTask object to this object.
     * through the TMSProjectTask foreign key attribute
     *
     * @param toAdd TMSProjectTask
     */
    protected void addTMSProjectTaskFromBean(TMSProjectTask toAdd)
    {
        initTMSProjectTasks();
        collTMSProjectTasks.add(toAdd);
    }


    /**
     * Method called to associate a TPersonBasket object to this object.
     * through the TPersonBasket foreign key attribute
     *
     * @param toAdd TPersonBasket
     */
    protected void addTPersonBasketFromBean(TPersonBasket toAdd)
    {
        initTPersonBaskets();
        collTPersonBaskets.add(toAdd);
    }


    /**
     * Method called to associate a TLastVisitedItem object to this object.
     * through the TLastVisitedItem foreign key attribute
     *
     * @param toAdd TLastVisitedItem
     */
    protected void addTLastVisitedItemFromBean(TLastVisitedItem toAdd)
    {
        initTLastVisitedItems();
        collTLastVisitedItems.add(toAdd);
    }


    /**
     * Method called to associate a TReadIssue object to this object.
     * through the TReadIssue foreign key attribute
     *
     * @param toAdd TReadIssue
     */
    protected void addTReadIssueFromBean(TReadIssue toAdd)
    {
        initTReadIssues();
        collTReadIssues.add(toAdd);
    }


    /**
     * Method called to associate a TAttachmentVersion object to this object.
     * through the TAttachmentVersion foreign key attribute
     *
     * @param toAdd TAttachmentVersion
     */
    protected void addTAttachmentVersionFromBean(TAttachmentVersion toAdd)
    {
        initTAttachmentVersions();
        collTAttachmentVersions.add(toAdd);
    }


    /**
     * Method called to associate a TItemTransition object to this object.
     * through the TItemTransition foreign key attribute
     *
     * @param toAdd TItemTransition
     */
    protected void addTItemTransitionFromBean(TItemTransition toAdd)
    {
        initTItemTransitions();
        collTItemTransitions.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TWorkItem:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("OwnerID = ")
           .append(getOwnerID())
           .append("\n");
        str.append("ChangedByID = ")
           .append(getChangedByID())
           .append("\n");
        str.append("OriginatorID = ")
           .append(getOriginatorID())
           .append("\n");
        str.append("ResponsibleID = ")
           .append(getResponsibleID())
           .append("\n");
        str.append("ProjectID = ")
           .append(getProjectID())
           .append("\n");
        str.append("ProjectCategoryID = ")
           .append(getProjectCategoryID())
           .append("\n");
        str.append("ListTypeID = ")
           .append(getListTypeID())
           .append("\n");
        str.append("ClassID = ")
           .append(getClassID())
           .append("\n");
        str.append("PriorityID = ")
           .append(getPriorityID())
           .append("\n");
        str.append("SeverityID = ")
           .append(getSeverityID())
           .append("\n");
        str.append("Superiorworkitem = ")
           .append(getSuperiorworkitem())
           .append("\n");
        str.append("Synopsis = ")
           .append(getSynopsis())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Reference = ")
           .append(getReference())
           .append("\n");
        str.append("LastEdit = ")
           .append(getLastEdit())
           .append("\n");
        str.append("ReleaseNoticedID = ")
           .append(getReleaseNoticedID())
           .append("\n");
        str.append("ReleaseScheduledID = ")
           .append(getReleaseScheduledID())
           .append("\n");
        str.append("Build = ")
           .append(getBuild())
           .append("\n");
        str.append("StateID = ")
           .append(getStateID())
           .append("\n");
        str.append("StartDate = ")
           .append(getStartDate())
           .append("\n");
        str.append("EndDate = ")
           .append(getEndDate())
           .append("\n");
        str.append("SubmitterEmail = ")
           .append(getSubmitterEmail())
           .append("\n");
        str.append("Created = ")
           .append(getCreated())
           .append("\n");
        str.append("ActualStartDate = ")
           .append(getActualStartDate())
           .append("\n");
        str.append("ActualEndDate = ")
           .append(getActualEndDate())
           .append("\n");
        str.append("Level = ")
           .append(getLevel())
           .append("\n");
        str.append("AccessLevel = ")
           .append(getAccessLevel())
           .append("\n");
        str.append("ArchiveLevel = ")
           .append(getArchiveLevel())
           .append("\n");
        str.append("EscalationLevel = ")
           .append(getEscalationLevel())
           .append("\n");
        str.append("TaskIsMilestone = ")
           .append(getTaskIsMilestone())
           .append("\n");
        str.append("TaskIsSubproject = ")
           .append(getTaskIsSubproject())
           .append("\n");
        str.append("TaskIsSummary = ")
           .append(getTaskIsSummary())
           .append("\n");
        str.append("TaskConstraint = ")
           .append(getTaskConstraint())
           .append("\n");
        str.append("TaskConstraintDate = ")
           .append(getTaskConstraintDate())
           .append("\n");
        str.append("PSPCode = ")
           .append(getPSPCode())
           .append("\n");
        str.append("IDNumber = ")
           .append(getIDNumber())
           .append("\n");
        str.append("WBSOnLevel = ")
           .append(getWBSOnLevel())
           .append("\n");
        str.append("ReminderDate = ")
           .append(getReminderDate())
           .append("\n");
        str.append("TopDownStartDate = ")
           .append(getTopDownStartDate())
           .append("\n");
        str.append("TopDownEndDate = ")
           .append(getTopDownEndDate())
           .append("\n");
        str.append("OverBudget = ")
           .append(getOverBudget())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
