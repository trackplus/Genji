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
 * Describes which person has which role in which project.
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TAccessControlListBean
 */
public abstract class BaseTAccessControlListBean
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


    /** The value for the personID field */
    private Integer personID;

    /** The value for the roleID field */
    private Integer roleID;

    /** The value for the projectID field */
    private Integer projectID;

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
     * Get the RoleID
     *
     * @return Integer
     */
    public Integer getRoleID ()
    {
        return roleID;
    }

    /**
     * Set the value of RoleID
     *
     * @param v new value
     */
    public void setRoleID(Integer v)
    {

        this.roleID = v;
        setModified(true);

    }

    /**
     * Get the ProjectID
     *
     * @return Integer
     */
    public Integer getProjectID ()
    {
        return projectID;
    }

    /**
     * Set the value of ProjectID
     *
     * @param v new value
     */
    public void setProjectID(Integer v)
    {

        this.projectID = v;
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





    private TRoleBean aTRoleBean;

    /**
     * sets an associated TRoleBean object
     *
     * @param v TRoleBean
     */
    public void setTRoleBean(TRoleBean v)
    {
        if (v == null)
        {
            setRoleID((Integer) null);
        }
        else
        {
            setRoleID(v.getObjectID());
        }
        aTRoleBean = v;
    }


    /**
     * Get the associated TRoleBean object
     *
     * @return the associated TRoleBean object
     */
    public TRoleBean getTRoleBean()
    {
        return aTRoleBean;
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
            setProjectID((Integer) null);
        }
        else
        {
            setProjectID(v.getObjectID());
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



}
