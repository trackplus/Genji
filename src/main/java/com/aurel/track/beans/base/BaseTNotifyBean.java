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
 * extended; all references should be to TNotifyBean
 */
public abstract class BaseTNotifyBean
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

    /** The value for the projectCategoryID field */
    private Integer projectCategoryID;

    /** The value for the stateID field */
    private Integer stateID;

    /** The value for the personID field */
    private Integer personID;

    /** The value for the workItem field */
    private Integer workItem;

    /** The value for the raciRole field */
    private String raciRole;

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
     * Get the PersonID
     *
     * @return Integer
     */
    public Integer getPersonID ()
    {
        return personID;
    }

    /**
     * Set the value of PersonID
     *
     * @param v new value
     */
    public void setPersonID(Integer v)
    {

        this.personID = v;
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
     * Get the RaciRole
     *
     * @return String
     */
    public String getRaciRole ()
    {
        return raciRole;
    }

    /**
     * Set the value of RaciRole
     *
     * @param v new value
     */
    public void setRaciRole(String v)
    {

        this.raciRole = v;
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
            setPersonID((Integer) null);
        }
        else
        {
            setPersonID(v.getObjectID());
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



}
