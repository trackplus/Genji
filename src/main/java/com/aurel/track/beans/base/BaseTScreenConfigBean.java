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
 * extended; all references should be to TScreenConfigBean
 */
public abstract class BaseTScreenConfigBean
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

    /** The value for the description field */
    private String description;

    /** The value for the screen field */
    private Integer screen;

    /** The value for the issueType field */
    private Integer issueType;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

    /** The value for the action field */
    private Integer action;

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
     * Get the Screen
     *
     * @return Integer
     */
    public Integer getScreen ()
    {
        return screen;
    }

    /**
     * Set the value of Screen
     *
     * @param v new value
     */
    public void setScreen(Integer v)
    {

        this.screen = v;
        setModified(true);

    }

    /**
     * Get the IssueType
     *
     * @return Integer
     */
    public Integer getIssueType ()
    {
        return issueType;
    }

    /**
     * Set the value of IssueType
     *
     * @param v new value
     */
    public void setIssueType(Integer v)
    {

        this.issueType = v;
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
     * Get the Action
     *
     * @return Integer
     */
    public Integer getAction ()
    {
        return action;
    }

    /**
     * Set the value of Action
     *
     * @param v new value
     */
    public void setAction(Integer v)
    {

        this.action = v;
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

    



    private TScreenBean aTScreenBean;

    /**
     * sets an associated TScreenBean object
     *
     * @param v TScreenBean
     */
    public void setTScreenBean(TScreenBean v)
    {
        if (v == null)
        {
            setScreen((Integer) null);
        }
        else
        {
            setScreen(v.getObjectID());
        }
        aTScreenBean = v;
    }


    /**
     * Get the associated TScreenBean object
     *
     * @return the associated TScreenBean object
     */
    public TScreenBean getTScreenBean()
    {
        return aTScreenBean;
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
            setIssueType((Integer) null);
        }
        else
        {
            setIssueType(v.getObjectID());
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
            setAction((Integer) null);
        }
        else
        {
            setAction(v.getObjectID());
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



}
