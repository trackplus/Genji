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
 * service level agreement for a customer in a project
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TOrgProjectSLABean
 */
public abstract class BaseTOrgProjectSLABean
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

    /** The value for the department field */
    private Integer department;

    /** The value for the project field */
    private Integer project;

    /** The value for the sla field */
    private Integer sla;

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
     * Get the Department
     *
     * @return Integer
     */
    public Integer getDepartment ()
    {
        return department;
    }

    /**
     * Set the value of Department
     *
     * @param v new value
     */
    public void setDepartment(Integer v)
    {

        this.department = v;
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
     * Get the Sla
     *
     * @return Integer
     */
    public Integer getSla ()
    {
        return sla;
    }

    /**
     * Set the value of Sla
     *
     * @param v new value
     */
    public void setSla(Integer v)
    {

        this.sla = v;
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

    



    private TDepartmentBean aTDepartmentBean;

    /**
     * sets an associated TDepartmentBean object
     *
     * @param v TDepartmentBean
     */
    public void setTDepartmentBean(TDepartmentBean v)
    {
        if (v == null)
        {
            setDepartment((Integer) null);
        }
        else
        {
            setDepartment(v.getObjectID());
        }
        aTDepartmentBean = v;
    }


    /**
     * Get the associated TDepartmentBean object
     *
     * @return the associated TDepartmentBean object
     */
    public TDepartmentBean getTDepartmentBean()
    {
        return aTDepartmentBean;
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





    private TSLABean aTSLABean;

    /**
     * sets an associated TSLABean object
     *
     * @param v TSLABean
     */
    public void setTSLABean(TSLABean v)
    {
        if (v == null)
        {
            setSla((Integer) null);
        }
        else
        {
            setSla(v.getObjectID());
        }
        aTSLABean = v;
    }


    /**
     * Get the associated TSLABean object
     *
     * @return the associated TSLABean object
     */
    public TSLABean getTSLABean()
    {
        return aTSLABean;
    }



}
