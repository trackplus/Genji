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
 * extended; all references should be to TEffortTypeBean
 */
public abstract class BaseTEffortTypeBean
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

    /** The value for the label field */
    private String label;

    /** The value for the effortUnit field */
    private Integer effortUnit;

    /** The value for the description field */
    private String description;

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
     * Get the Label
     *
     * @return String
     */
    public String getLabel ()
    {
        return label;
    }

    /**
     * Set the value of Label
     *
     * @param v new value
     */
    public void setLabel(String v)
    {

        this.label = v;
        setModified(true);

    }

    /**
     * Get the EffortUnit
     *
     * @return Integer
     */
    public Integer getEffortUnit ()
    {
        return effortUnit;
    }

    /**
     * Set the value of EffortUnit
     *
     * @param v new value
     */
    public void setEffortUnit(Integer v)
    {

        this.effortUnit = v;
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

    



    private TEffortUnitBean aTEffortUnitBean;

    /**
     * sets an associated TEffortUnitBean object
     *
     * @param v TEffortUnitBean
     */
    public void setTEffortUnitBean(TEffortUnitBean v)
    {
        if (v == null)
        {
            setEffortUnit((Integer) null);
        }
        else
        {
            setEffortUnit(v.getObjectID());
        }
        aTEffortUnitBean = v;
    }


    /**
     * Get the associated TEffortUnitBean object
     *
     * @return the associated TEffortUnitBean object
     */
    public TEffortUnitBean getTEffortUnitBean()
    {
        return aTEffortUnitBean;
    }





    /**
     * Collection to store aggregation of collTCostBeans
     */
    protected List<TCostBean> collTCostBeans;

    /**
     * Returns the collection of TCostBeans
     */
    public List<TCostBean> getTCostBeans()
    {
        return collTCostBeans;
    }

    /**
     * Sets the collection of TCostBeans to the specified value
     */
    public void setTCostBeans(List<TCostBean> list)
    {
        if (list == null)
        {
            collTCostBeans = null;
        }
        else
        {
            collTCostBeans = new ArrayList<TCostBean>(list);
        }
    }

}
