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
 * extended; all references should be to TRoleFieldBean
 */
public abstract class BaseTRoleFieldBean
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

    /** The value for the roleKey field */
    private Integer roleKey;

    /** The value for the fieldKey field */
    private Integer fieldKey;

    /** The value for the accessRight field */
    private Integer accessRight;

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
     * Get the RoleKey
     *
     * @return Integer
     */
    public Integer getRoleKey ()
    {
        return roleKey;
    }

    /**
     * Set the value of RoleKey
     *
     * @param v new value
     */
    public void setRoleKey(Integer v)
    {

        this.roleKey = v;
        setModified(true);

    }

    /**
     * Get the FieldKey
     *
     * @return Integer
     */
    public Integer getFieldKey ()
    {
        return fieldKey;
    }

    /**
     * Set the value of FieldKey
     *
     * @param v new value
     */
    public void setFieldKey(Integer v)
    {

        this.fieldKey = v;
        setModified(true);

    }

    /**
     * Get the AccessRight
     *
     * @return Integer
     */
    public Integer getAccessRight ()
    {
        return accessRight;
    }

    /**
     * Set the value of AccessRight
     *
     * @param v new value
     */
    public void setAccessRight(Integer v)
    {

        this.accessRight = v;
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
            setRoleKey((Integer) null);
        }
        else
        {
            setRoleKey(v.getObjectID());
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



}
