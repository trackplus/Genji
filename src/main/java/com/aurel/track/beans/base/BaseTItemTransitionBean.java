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
 * extended; all references should be to TItemTransitionBean
 */
public abstract class BaseTItemTransitionBean
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

    /** The value for the workItem field */
    private Integer workItem;

    /** The value for the transition field */
    private Integer transition;

    /** The value for the transitionTime field */
    private Date transitionTime;

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
     * Get the WorkItem
     *
     * @return Integer
     */
    public Integer getWorkItem ()
    {
        return workItem;
    }

    /**
     * Set the value of WorkItem
     *
     * @param v new value
     */
    public void setWorkItem(Integer v)
    {

        this.workItem = v;
        setModified(true);

    }

    /**
     * Get the Transition
     *
     * @return Integer
     */
    public Integer getTransition ()
    {
        return transition;
    }

    /**
     * Set the value of Transition
     *
     * @param v new value
     */
    public void setTransition(Integer v)
    {

        this.transition = v;
        setModified(true);

    }

    /**
     * Get the TransitionTime
     *
     * @return Date
     */
    public Date getTransitionTime ()
    {
        return transitionTime;
    }

    /**
     * Set the value of TransitionTime
     *
     * @param v new value
     */
    public void setTransitionTime(Date v)
    {

        this.transitionTime = v;
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
            setWorkItem((Integer) null);
        }
        else
        {
            setWorkItem(v.getObjectID());
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





    private TWorkflowTransitionBean aTWorkflowTransitionBean;

    /**
     * sets an associated TWorkflowTransitionBean object
     *
     * @param v TWorkflowTransitionBean
     */
    public void setTWorkflowTransitionBean(TWorkflowTransitionBean v)
    {
        if (v == null)
        {
            setTransition((Integer) null);
        }
        else
        {
            setTransition(v.getObjectID());
        }
        aTWorkflowTransitionBean = v;
    }


    /**
     * Get the associated TWorkflowTransitionBean object
     *
     * @return the associated TWorkflowTransitionBean object
     */
    public TWorkflowTransitionBean getTWorkflowTransitionBean()
    {
        return aTWorkflowTransitionBean;
    }



}
