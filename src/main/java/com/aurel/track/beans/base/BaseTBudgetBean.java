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
 * extended; all references should be to TBudgetBean
 */
public abstract class BaseTBudgetBean
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

    /** The value for the workItemID field */
    private Integer workItemID;

    /** The value for the estimatedHours field */
    private Double estimatedHours;

    /** The value for the timeUnit field */
    private Integer timeUnit;

    /** The value for the estimatedCost field */
    private Double estimatedCost;

    /** The value for the efforttype field */
    private Integer efforttype;

    /** The value for the effortvalue field */
    private Double effortvalue;

    /** The value for the changeDescription field */
    private String changeDescription;

    /** The value for the moreProps field */
    private String moreProps;

    /** The value for the changedByID field */
    private Integer changedByID;

    /** The value for the lastEdit field */
    private Date lastEdit;

    /** The value for the budgetType field */
    private Integer budgetType;

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
     * Get the WorkItemID
     *
     * @return Integer
     */
    public Integer getWorkItemID ()
    {
        return workItemID;
    }

    /**
     * Set the value of WorkItemID
     *
     * @param v new value
     */
    public void setWorkItemID(Integer v)
    {

        this.workItemID = v;
        setModified(true);

    }

    /**
     * Get the EstimatedHours
     *
     * @return Double
     */
    public Double getEstimatedHours ()
    {
        return estimatedHours;
    }

    /**
     * Set the value of EstimatedHours
     *
     * @param v new value
     */
    public void setEstimatedHours(Double v)
    {

        this.estimatedHours = v;
        setModified(true);

    }

    /**
     * Get the TimeUnit
     *
     * @return Integer
     */
    public Integer getTimeUnit ()
    {
        return timeUnit;
    }

    /**
     * Set the value of TimeUnit
     *
     * @param v new value
     */
    public void setTimeUnit(Integer v)
    {

        this.timeUnit = v;
        setModified(true);

    }

    /**
     * Get the EstimatedCost
     *
     * @return Double
     */
    public Double getEstimatedCost ()
    {
        return estimatedCost;
    }

    /**
     * Set the value of EstimatedCost
     *
     * @param v new value
     */
    public void setEstimatedCost(Double v)
    {

        this.estimatedCost = v;
        setModified(true);

    }

    /**
     * Get the Efforttype
     *
     * @return Integer
     */
    public Integer getEfforttype ()
    {
        return efforttype;
    }

    /**
     * Set the value of Efforttype
     *
     * @param v new value
     */
    public void setEfforttype(Integer v)
    {

        this.efforttype = v;
        setModified(true);

    }

    /**
     * Get the Effortvalue
     *
     * @return Double
     */
    public Double getEffortvalue ()
    {
        return effortvalue;
    }

    /**
     * Set the value of Effortvalue
     *
     * @param v new value
     */
    public void setEffortvalue(Double v)
    {

        this.effortvalue = v;
        setModified(true);

    }

    /**
     * Get the ChangeDescription
     *
     * @return String
     */
    public String getChangeDescription ()
    {
        return changeDescription;
    }

    /**
     * Set the value of ChangeDescription
     *
     * @param v new value
     */
    public void setChangeDescription(String v)
    {

        this.changeDescription = v;
        setModified(true);

    }

    /**
     * Get the MoreProps
     *
     * @return String
     */
    public String getMoreProps ()
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

        this.moreProps = v;
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
     * Get the BudgetType
     *
     * @return Integer
     */
    public Integer getBudgetType ()
    {
        return budgetType;
    }

    /**
     * Set the value of BudgetType
     *
     * @param v new value
     */
    public void setBudgetType(Integer v)
    {

        this.budgetType = v;
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
            setWorkItemID((Integer) null);
        }
        else
        {
            setWorkItemID(v.getObjectID());
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





    private TPersonBean aTPersonBean;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBean(TPersonBean v)
    {
        if (v == null)
        {
            setChangedByID((Integer) null);
        }
        else
        {
            setChangedByID(v.getObjectID());
        }
        aTPersonBean = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBean()
    {
        return aTPersonBean;
    }



}
