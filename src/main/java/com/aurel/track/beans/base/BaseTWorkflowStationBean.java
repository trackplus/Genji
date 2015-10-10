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
 * extended; all references should be to TWorkflowStationBean
 */
public abstract class BaseTWorkflowStationBean
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

    /** The value for the name field */
    private String name;

    /** The value for the status field */
    private Integer status;

    /** The value for the workflow field */
    private Integer workflow;

    /** The value for the nodeX field */
    private Integer nodeX;

    /** The value for the nodeY field */
    private Integer nodeY;

    /** The value for the stationType field */
    private Integer stationType;

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
     * Get the Name
     *
     * @return String
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Set the value of Name
     *
     * @param v new value
     */
    public void setName(String v)
    {

        this.name = v;
        setModified(true);

    }

    /**
     * Get the Status
     *
     * @return Integer
     */
    public Integer getStatus ()
    {
        return status;
    }

    /**
     * Set the value of Status
     *
     * @param v new value
     */
    public void setStatus(Integer v)
    {

        this.status = v;
        setModified(true);

    }

    /**
     * Get the Workflow
     *
     * @return Integer
     */
    public Integer getWorkflow ()
    {
        return workflow;
    }

    /**
     * Set the value of Workflow
     *
     * @param v new value
     */
    public void setWorkflow(Integer v)
    {

        this.workflow = v;
        setModified(true);

    }

    /**
     * Get the NodeX
     *
     * @return Integer
     */
    public Integer getNodeX ()
    {
        return nodeX;
    }

    /**
     * Set the value of NodeX
     *
     * @param v new value
     */
    public void setNodeX(Integer v)
    {

        this.nodeX = v;
        setModified(true);

    }

    /**
     * Get the NodeY
     *
     * @return Integer
     */
    public Integer getNodeY ()
    {
        return nodeY;
    }

    /**
     * Set the value of NodeY
     *
     * @param v new value
     */
    public void setNodeY(Integer v)
    {

        this.nodeY = v;
        setModified(true);

    }

    /**
     * Get the StationType
     *
     * @return Integer
     */
    public Integer getStationType ()
    {
        return stationType;
    }

    /**
     * Set the value of StationType
     *
     * @param v new value
     */
    public void setStationType(Integer v)
    {

        this.stationType = v;
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
            setStatus((Integer) null);
        }
        else
        {
            setStatus(v.getObjectID());
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





    private TWorkflowDefBean aTWorkflowDefBean;

    /**
     * sets an associated TWorkflowDefBean object
     *
     * @param v TWorkflowDefBean
     */
    public void setTWorkflowDefBean(TWorkflowDefBean v)
    {
        if (v == null)
        {
            setWorkflow((Integer) null);
        }
        else
        {
            setWorkflow(v.getObjectID());
        }
        aTWorkflowDefBean = v;
    }


    /**
     * Get the associated TWorkflowDefBean object
     *
     * @return the associated TWorkflowDefBean object
     */
    public TWorkflowDefBean getTWorkflowDefBean()
    {
        return aTWorkflowDefBean;
    }





    /**
     * Collection to store aggregation of collTWorkflowTransitionBeansRelatedByStationFrom
     */
    protected List<TWorkflowTransitionBean> collTWorkflowTransitionBeansRelatedByStationFrom;

    /**
     * Returns the collection of TWorkflowTransitionBeansRelatedByStationFrom
     */
    public List<TWorkflowTransitionBean> getTWorkflowTransitionBeansRelatedByStationFrom()
    {
        return collTWorkflowTransitionBeansRelatedByStationFrom;
    }

    /**
     * Sets the collection of TWorkflowTransitionBeansRelatedByStationFrom to the specified value
     */
    public void setTWorkflowTransitionBeansRelatedByStationFrom(List<TWorkflowTransitionBean> list)
    {
        if (list == null)
        {
            collTWorkflowTransitionBeansRelatedByStationFrom = null;
        }
        else
        {
            collTWorkflowTransitionBeansRelatedByStationFrom = new ArrayList<TWorkflowTransitionBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowTransitionBeansRelatedByStationTo
     */
    protected List<TWorkflowTransitionBean> collTWorkflowTransitionBeansRelatedByStationTo;

    /**
     * Returns the collection of TWorkflowTransitionBeansRelatedByStationTo
     */
    public List<TWorkflowTransitionBean> getTWorkflowTransitionBeansRelatedByStationTo()
    {
        return collTWorkflowTransitionBeansRelatedByStationTo;
    }

    /**
     * Sets the collection of TWorkflowTransitionBeansRelatedByStationTo to the specified value
     */
    public void setTWorkflowTransitionBeansRelatedByStationTo(List<TWorkflowTransitionBean> list)
    {
        if (list == null)
        {
            collTWorkflowTransitionBeansRelatedByStationTo = null;
        }
        else
        {
            collTWorkflowTransitionBeansRelatedByStationTo = new ArrayList<TWorkflowTransitionBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowActivityBeansRelatedByStationEntryActivity
     */
    protected List<TWorkflowActivityBean> collTWorkflowActivityBeansRelatedByStationEntryActivity;

    /**
     * Returns the collection of TWorkflowActivityBeansRelatedByStationEntryActivity
     */
    public List<TWorkflowActivityBean> getTWorkflowActivityBeansRelatedByStationEntryActivity()
    {
        return collTWorkflowActivityBeansRelatedByStationEntryActivity;
    }

    /**
     * Sets the collection of TWorkflowActivityBeansRelatedByStationEntryActivity to the specified value
     */
    public void setTWorkflowActivityBeansRelatedByStationEntryActivity(List<TWorkflowActivityBean> list)
    {
        if (list == null)
        {
            collTWorkflowActivityBeansRelatedByStationEntryActivity = null;
        }
        else
        {
            collTWorkflowActivityBeansRelatedByStationEntryActivity = new ArrayList<TWorkflowActivityBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowActivityBeansRelatedByStationExitActivity
     */
    protected List<TWorkflowActivityBean> collTWorkflowActivityBeansRelatedByStationExitActivity;

    /**
     * Returns the collection of TWorkflowActivityBeansRelatedByStationExitActivity
     */
    public List<TWorkflowActivityBean> getTWorkflowActivityBeansRelatedByStationExitActivity()
    {
        return collTWorkflowActivityBeansRelatedByStationExitActivity;
    }

    /**
     * Sets the collection of TWorkflowActivityBeansRelatedByStationExitActivity to the specified value
     */
    public void setTWorkflowActivityBeansRelatedByStationExitActivity(List<TWorkflowActivityBean> list)
    {
        if (list == null)
        {
            collTWorkflowActivityBeansRelatedByStationExitActivity = null;
        }
        else
        {
            collTWorkflowActivityBeansRelatedByStationExitActivity = new ArrayList<TWorkflowActivityBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowActivityBeansRelatedByStationDoActivity
     */
    protected List<TWorkflowActivityBean> collTWorkflowActivityBeansRelatedByStationDoActivity;

    /**
     * Returns the collection of TWorkflowActivityBeansRelatedByStationDoActivity
     */
    public List<TWorkflowActivityBean> getTWorkflowActivityBeansRelatedByStationDoActivity()
    {
        return collTWorkflowActivityBeansRelatedByStationDoActivity;
    }

    /**
     * Sets the collection of TWorkflowActivityBeansRelatedByStationDoActivity to the specified value
     */
    public void setTWorkflowActivityBeansRelatedByStationDoActivity(List<TWorkflowActivityBean> list)
    {
        if (list == null)
        {
            collTWorkflowActivityBeansRelatedByStationDoActivity = null;
        }
        else
        {
            collTWorkflowActivityBeansRelatedByStationDoActivity = new ArrayList<TWorkflowActivityBean>(list);
        }
    }

}
