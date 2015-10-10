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
 * extended; all references should be to TWorkflowTransitionBean
 */
public abstract class BaseTWorkflowTransitionBean
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

    /** The value for the stationFrom field */
    private Integer stationFrom;

    /** The value for the stationTo field */
    private Integer stationTo;

    /** The value for the actionKey field */
    private Integer actionKey;

    /** The value for the workflow field */
    private Integer workflow;

    /** The value for the textPositionX field */
    private Integer textPositionX;

    /** The value for the textPositionY field */
    private Integer textPositionY;

    /** The value for the transitionType field */
    private Integer transitionType;

    /** The value for the controlPoints field */
    private String controlPoints;

    /** The value for the elapsedTime field */
    private Integer elapsedTime;

    /** The value for the timeUnit field */
    private Integer timeUnit;

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
     * Get the StationFrom
     *
     * @return Integer
     */
    public Integer getStationFrom ()
    {
        return stationFrom;
    }

    /**
     * Set the value of StationFrom
     *
     * @param v new value
     */
    public void setStationFrom(Integer v)
    {

        this.stationFrom = v;
        setModified(true);

    }

    /**
     * Get the StationTo
     *
     * @return Integer
     */
    public Integer getStationTo ()
    {
        return stationTo;
    }

    /**
     * Set the value of StationTo
     *
     * @param v new value
     */
    public void setStationTo(Integer v)
    {

        this.stationTo = v;
        setModified(true);

    }

    /**
     * Get the ActionKey
     *
     * @return Integer
     */
    public Integer getActionKey ()
    {
        return actionKey;
    }

    /**
     * Set the value of ActionKey
     *
     * @param v new value
     */
    public void setActionKey(Integer v)
    {

        this.actionKey = v;
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
     * Get the TextPositionX
     *
     * @return Integer
     */
    public Integer getTextPositionX ()
    {
        return textPositionX;
    }

    /**
     * Set the value of TextPositionX
     *
     * @param v new value
     */
    public void setTextPositionX(Integer v)
    {

        this.textPositionX = v;
        setModified(true);

    }

    /**
     * Get the TextPositionY
     *
     * @return Integer
     */
    public Integer getTextPositionY ()
    {
        return textPositionY;
    }

    /**
     * Set the value of TextPositionY
     *
     * @param v new value
     */
    public void setTextPositionY(Integer v)
    {

        this.textPositionY = v;
        setModified(true);

    }

    /**
     * Get the TransitionType
     *
     * @return Integer
     */
    public Integer getTransitionType ()
    {
        return transitionType;
    }

    /**
     * Set the value of TransitionType
     *
     * @param v new value
     */
    public void setTransitionType(Integer v)
    {

        this.transitionType = v;
        setModified(true);

    }

    /**
     * Get the ControlPoints
     *
     * @return String
     */
    public String getControlPoints ()
    {
        return controlPoints;
    }

    /**
     * Set the value of ControlPoints
     *
     * @param v new value
     */
    public void setControlPoints(String v)
    {

        this.controlPoints = v;
        setModified(true);

    }

    /**
     * Get the ElapsedTime
     *
     * @return Integer
     */
    public Integer getElapsedTime ()
    {
        return elapsedTime;
    }

    /**
     * Set the value of ElapsedTime
     *
     * @param v new value
     */
    public void setElapsedTime(Integer v)
    {

        this.elapsedTime = v;
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

    



    private TWorkflowStationBean aTWorkflowStationBeanRelatedByStationFrom;

    /**
     * sets an associated TWorkflowStationBean object
     *
     * @param v TWorkflowStationBean
     */
    public void setTWorkflowStationBeanRelatedByStationFrom(TWorkflowStationBean v)
    {
        if (v == null)
        {
            setStationFrom((Integer) null);
        }
        else
        {
            setStationFrom(v.getObjectID());
        }
        aTWorkflowStationBeanRelatedByStationFrom = v;
    }


    /**
     * Get the associated TWorkflowStationBean object
     *
     * @return the associated TWorkflowStationBean object
     */
    public TWorkflowStationBean getTWorkflowStationBeanRelatedByStationFrom()
    {
        return aTWorkflowStationBeanRelatedByStationFrom;
    }





    private TWorkflowStationBean aTWorkflowStationBeanRelatedByStationTo;

    /**
     * sets an associated TWorkflowStationBean object
     *
     * @param v TWorkflowStationBean
     */
    public void setTWorkflowStationBeanRelatedByStationTo(TWorkflowStationBean v)
    {
        if (v == null)
        {
            setStationTo((Integer) null);
        }
        else
        {
            setStationTo(v.getObjectID());
        }
        aTWorkflowStationBeanRelatedByStationTo = v;
    }


    /**
     * Get the associated TWorkflowStationBean object
     *
     * @return the associated TWorkflowStationBean object
     */
    public TWorkflowStationBean getTWorkflowStationBeanRelatedByStationTo()
    {
        return aTWorkflowStationBeanRelatedByStationTo;
    }





    private TActionBean aTActionBean;

    /**
     * sets an associated TActionBean object
     *
     * @param v TActionBean
     */
    public void setTActionBean(TActionBean v)
    {
        if (v == null)
        {
            setActionKey((Integer) null);
        }
        else
        {
            setActionKey(v.getObjectID());
        }
        aTActionBean = v;
    }


    /**
     * Get the associated TActionBean object
     *
     * @return the associated TActionBean object
     */
    public TActionBean getTActionBean()
    {
        return aTActionBean;
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
     * Collection to store aggregation of collTWorkflowActivityBeans
     */
    protected List<TWorkflowActivityBean> collTWorkflowActivityBeans;

    /**
     * Returns the collection of TWorkflowActivityBeans
     */
    public List<TWorkflowActivityBean> getTWorkflowActivityBeans()
    {
        return collTWorkflowActivityBeans;
    }

    /**
     * Sets the collection of TWorkflowActivityBeans to the specified value
     */
    public void setTWorkflowActivityBeans(List<TWorkflowActivityBean> list)
    {
        if (list == null)
        {
            collTWorkflowActivityBeans = null;
        }
        else
        {
            collTWorkflowActivityBeans = new ArrayList<TWorkflowActivityBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowGuardBeans
     */
    protected List<TWorkflowGuardBean> collTWorkflowGuardBeans;

    /**
     * Returns the collection of TWorkflowGuardBeans
     */
    public List<TWorkflowGuardBean> getTWorkflowGuardBeans()
    {
        return collTWorkflowGuardBeans;
    }

    /**
     * Sets the collection of TWorkflowGuardBeans to the specified value
     */
    public void setTWorkflowGuardBeans(List<TWorkflowGuardBean> list)
    {
        if (list == null)
        {
            collTWorkflowGuardBeans = null;
        }
        else
        {
            collTWorkflowGuardBeans = new ArrayList<TWorkflowGuardBean>(list);
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
