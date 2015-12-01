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
 * Describes which project type can have a child project depending on the a project type of parent project
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TChildProjectTypeBean
 */
public abstract class BaseTChildProjectTypeBean
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

    /** The value for the projectTypeParent field */
    private Integer projectTypeParent;

    /** The value for the projectTypeChild field */
    private Integer projectTypeChild;

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
     * Get the ProjectTypeParent
     *
     * @return Integer
     */
    public Integer getProjectTypeParent ()
    {
        return projectTypeParent;
    }

    /**
     * Set the value of ProjectTypeParent
     *
     * @param v new value
     */
    public void setProjectTypeParent(Integer v)
    {

        this.projectTypeParent = v;
        setModified(true);

    }

    /**
     * Get the ProjectTypeChild
     *
     * @return Integer
     */
    public Integer getProjectTypeChild ()
    {
        return projectTypeChild;
    }

    /**
     * Set the value of ProjectTypeChild
     *
     * @param v new value
     */
    public void setProjectTypeChild(Integer v)
    {

        this.projectTypeChild = v;
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

    



    private TProjectTypeBean aTProjectTypeBeanRelatedByProjectTypeParent;

    /**
     * sets an associated TProjectTypeBean object
     *
     * @param v TProjectTypeBean
     */
    public void setTProjectTypeBeanRelatedByProjectTypeParent(TProjectTypeBean v)
    {
        if (v == null)
        {
            setProjectTypeParent((Integer) null);
        }
        else
        {
            setProjectTypeParent(v.getObjectID());
        }
        aTProjectTypeBeanRelatedByProjectTypeParent = v;
    }


    /**
     * Get the associated TProjectTypeBean object
     *
     * @return the associated TProjectTypeBean object
     */
    public TProjectTypeBean getTProjectTypeBeanRelatedByProjectTypeParent()
    {
        return aTProjectTypeBeanRelatedByProjectTypeParent;
    }





    private TProjectTypeBean aTProjectTypeBeanRelatedByProjectTypeChild;

    /**
     * sets an associated TProjectTypeBean object
     *
     * @param v TProjectTypeBean
     */
    public void setTProjectTypeBeanRelatedByProjectTypeChild(TProjectTypeBean v)
    {
        if (v == null)
        {
            setProjectTypeChild((Integer) null);
        }
        else
        {
            setProjectTypeChild(v.getObjectID());
        }
        aTProjectTypeBeanRelatedByProjectTypeChild = v;
    }


    /**
     * Get the associated TProjectTypeBean object
     *
     * @return the associated TProjectTypeBean object
     */
    public TProjectTypeBean getTProjectTypeBeanRelatedByProjectTypeChild()
    {
        return aTProjectTypeBeanRelatedByProjectTypeChild;
    }



}
