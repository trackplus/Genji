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
 * extended; all references should be to TEventBean
 */
public abstract class BaseTEventBean
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

    /** The value for the eventName field */
    private String eventName;

    /** The value for the eventType field */
    private Integer eventType;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

    /** The value for the eventScript field */
    private Integer eventScript;

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
     * Get the EventName
     *
     * @return String
     */
    public String getEventName ()
    {
        return eventName;
    }

    /**
     * Set the value of EventName
     *
     * @param v new value
     */
    public void setEventName(String v)
    {

        this.eventName = v;
        setModified(true);

    }

    /**
     * Get the EventType
     *
     * @return Integer
     */
    public Integer getEventType ()
    {
        return eventType;
    }

    /**
     * Set the value of EventType
     *
     * @param v new value
     */
    public void setEventType(Integer v)
    {

        this.eventType = v;
        setModified(true);

    }

    /**
     * Get the ProjectType
     *
     * @return Integer
     */
    public Integer getProjectType ()
    {
        return projectType;
    }

    /**
     * Set the value of ProjectType
     *
     * @param v new value
     */
    public void setProjectType(Integer v)
    {

        this.projectType = v;
        setModified(true);

    }

    /**
     * Get the Project
     *
     * @return Integer
     */
    public Integer getProject ()
    {
        return project;
    }

    /**
     * Set the value of Project
     *
     * @param v new value
     */
    public void setProject(Integer v)
    {

        this.project = v;
        setModified(true);

    }

    /**
     * Get the EventScript
     *
     * @return Integer
     */
    public Integer getEventScript ()
    {
        return eventScript;
    }

    /**
     * Set the value of EventScript
     *
     * @param v new value
     */
    public void setEventScript(Integer v)
    {

        this.eventScript = v;
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

    



    private TProjectTypeBean aTProjectTypeBean;

    /**
     * sets an associated TProjectTypeBean object
     *
     * @param v TProjectTypeBean
     */
    public void setTProjectTypeBean(TProjectTypeBean v)
    {
        if (v == null)
        {
            setProjectType((Integer) null);
        }
        else
        {
            setProjectType(v.getObjectID());
        }
        aTProjectTypeBean = v;
    }


    /**
     * Get the associated TProjectTypeBean object
     *
     * @return the associated TProjectTypeBean object
     */
    public TProjectTypeBean getTProjectTypeBean()
    {
        return aTProjectTypeBean;
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
            setProject((Integer) null);
        }
        else
        {
            setProject(v.getObjectID());
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





    private TCLOBBean aTCLOBBean;

    /**
     * sets an associated TCLOBBean object
     *
     * @param v TCLOBBean
     */
    public void setTCLOBBean(TCLOBBean v)
    {
        if (v == null)
        {
            setEventScript((Integer) null);
        }
        else
        {
            setEventScript(v.getObjectID());
        }
        aTCLOBBean = v;
    }


    /**
     * Get the associated TCLOBBean object
     *
     * @return the associated TCLOBBean object
     */
    public TCLOBBean getTCLOBBean()
    {
        return aTCLOBBean;
    }



}
