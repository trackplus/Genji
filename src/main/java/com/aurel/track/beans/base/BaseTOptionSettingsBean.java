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
 * extended; all references should be to TOptionSettingsBean
 */
public abstract class BaseTOptionSettingsBean
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

    /** The value for the list field */
    private Integer list;

    /** The value for the config field */
    private Integer config;

    /** The value for the parameterCode field */
    private Integer parameterCode;

    /** The value for the multiple field */
    private String multiple = "N";

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
     * Get the List
     *
     * @return Integer
     */
    public Integer getList ()
    {
        return list;
    }

    /**
     * Set the value of List
     *
     * @param v new value
     */
    public void setList(Integer v)
    {

        this.list = v;
        setModified(true);

    }

    /**
     * Get the Config
     *
     * @return Integer
     */
    public Integer getConfig ()
    {
        return config;
    }

    /**
     * Set the value of Config
     *
     * @param v new value
     */
    public void setConfig(Integer v)
    {

        this.config = v;
        setModified(true);

    }

    /**
     * Get the ParameterCode
     *
     * @return Integer
     */
    public Integer getParameterCode ()
    {
        return parameterCode;
    }

    /**
     * Set the value of ParameterCode
     *
     * @param v new value
     */
    public void setParameterCode(Integer v)
    {

        this.parameterCode = v;
        setModified(true);

    }

    /**
     * Get the Multiple
     *
     * @return String
     */
    public String getMultiple ()
    {
        return multiple;
    }

    /**
     * Set the value of Multiple
     *
     * @param v new value
     */
    public void setMultiple(String v)
    {

        this.multiple = v;
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

    



    private TListBean aTListBean;

    /**
     * sets an associated TListBean object
     *
     * @param v TListBean
     */
    public void setTListBean(TListBean v)
    {
        if (v == null)
        {
            setList((Integer) null);
        }
        else
        {
            setList(v.getObjectID());
        }
        aTListBean = v;
    }


    /**
     * Get the associated TListBean object
     *
     * @return the associated TListBean object
     */
    public TListBean getTListBean()
    {
        return aTListBean;
    }





    private TFieldConfigBean aTFieldConfigBean;

    /**
     * sets an associated TFieldConfigBean object
     *
     * @param v TFieldConfigBean
     */
    public void setTFieldConfigBean(TFieldConfigBean v)
    {
        if (v == null)
        {
            setConfig((Integer) null);
        }
        else
        {
            setConfig(v.getObjectID());
        }
        aTFieldConfigBean = v;
    }


    /**
     * Get the associated TFieldConfigBean object
     *
     * @return the associated TFieldConfigBean object
     */
    public TFieldConfigBean getTFieldConfigBean()
    {
        return aTFieldConfigBean;
    }



}
