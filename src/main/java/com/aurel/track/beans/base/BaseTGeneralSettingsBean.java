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
 * extended; all references should be to TGeneralSettingsBean
 */
public abstract class BaseTGeneralSettingsBean
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

    /** The value for the config field */
    private Integer config;

    /** The value for the integerValue field */
    private Integer integerValue;

    /** The value for the doubleValue field */
    private Double doubleValue;

    /** The value for the textValue field */
    private String textValue;

    /** The value for the dateValue field */
    private Date dateValue;

    /** The value for the characterValue field */
    private String characterValue;

    /** The value for the parameterCode field */
    private Integer parameterCode;

    /** The value for the validValue field */
    private Integer validValue;

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
     * Get the IntegerValue
     *
     * @return Integer
     */
    public Integer getIntegerValue ()
    {
        return integerValue;
    }

    /**
     * Set the value of IntegerValue
     *
     * @param v new value
     */
    public void setIntegerValue(Integer v)
    {

        this.integerValue = v;
        setModified(true);

    }

    /**
     * Get the DoubleValue
     *
     * @return Double
     */
    public Double getDoubleValue ()
    {
        return doubleValue;
    }

    /**
     * Set the value of DoubleValue
     *
     * @param v new value
     */
    public void setDoubleValue(Double v)
    {

        this.doubleValue = v;
        setModified(true);

    }

    /**
     * Get the TextValue
     *
     * @return String
     */
    public String getTextValue ()
    {
        return textValue;
    }

    /**
     * Set the value of TextValue
     *
     * @param v new value
     */
    public void setTextValue(String v)
    {

        this.textValue = v;
        setModified(true);

    }

    /**
     * Get the DateValue
     *
     * @return Date
     */
    public Date getDateValue ()
    {
        return dateValue;
    }

    /**
     * Set the value of DateValue
     *
     * @param v new value
     */
    public void setDateValue(Date v)
    {

        this.dateValue = v;
        setModified(true);

    }

    /**
     * Get the CharacterValue
     *
     * @return String
     */
    public String getCharacterValue ()
    {
        return characterValue;
    }

    /**
     * Set the value of CharacterValue
     *
     * @param v new value
     */
    public void setCharacterValue(String v)
    {

        this.characterValue = v;
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
     * Get the ValidValue
     *
     * @return Integer
     */
    public Integer getValidValue ()
    {
        return validValue;
    }

    /**
     * Set the value of ValidValue
     *
     * @param v new value
     */
    public void setValidValue(Integer v)
    {

        this.validValue = v;
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
