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
 * extended; all references should be to TComputedValuesBean
 */
public abstract class BaseTComputedValuesBean
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

    /** The value for the workitemKey field */
    private Integer workitemKey;

    /** The value for the effortType field */
    private Integer effortType;

    /** The value for the computedValueType field */
    private Integer computedValueType;

    /** The value for the computedValue field */
    private Double computedValue;

    /** The value for the measurementUnit field */
    private Integer measurementUnit;

    /** The value for the person field */
    private Integer person;

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
     * Get the WorkitemKey
     *
     * @return Integer
     */
    public Integer getWorkitemKey ()
    {
        return workitemKey;
    }

    /**
     * Set the value of WorkitemKey
     *
     * @param v new value
     */
    public void setWorkitemKey(Integer v)
    {

        this.workitemKey = v;
        setModified(true);

    }

    /**
     * Get the EffortType
     *
     * @return Integer
     */
    public Integer getEffortType ()
    {
        return effortType;
    }

    /**
     * Set the value of EffortType
     *
     * @param v new value
     */
    public void setEffortType(Integer v)
    {

        this.effortType = v;
        setModified(true);

    }

    /**
     * Get the ComputedValueType
     *
     * @return Integer
     */
    public Integer getComputedValueType ()
    {
        return computedValueType;
    }

    /**
     * Set the value of ComputedValueType
     *
     * @param v new value
     */
    public void setComputedValueType(Integer v)
    {

        this.computedValueType = v;
        setModified(true);

    }

    /**
     * Get the ComputedValue
     *
     * @return Double
     */
    public Double getComputedValue ()
    {
        return computedValue;
    }

    /**
     * Set the value of ComputedValue
     *
     * @param v new value
     */
    public void setComputedValue(Double v)
    {

        this.computedValue = v;
        setModified(true);

    }

    /**
     * Get the MeasurementUnit
     *
     * @return Integer
     */
    public Integer getMeasurementUnit ()
    {
        return measurementUnit;
    }

    /**
     * Set the value of MeasurementUnit
     *
     * @param v new value
     */
    public void setMeasurementUnit(Integer v)
    {

        this.measurementUnit = v;
        setModified(true);

    }

    /**
     * Get the Person
     *
     * @return Integer
     */
    public Integer getPerson ()
    {
        return person;
    }

    /**
     * Set the value of Person
     *
     * @param v new value
     */
    public void setPerson(Integer v)
    {

        this.person = v;
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
            setWorkitemKey((Integer) null);
        }
        else
        {
            setWorkitemKey(v.getObjectID());
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
            setPerson((Integer) null);
        }
        else
        {
            setPerson(v.getObjectID());
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



}
