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
 * extended; all references should be to TTextBoxSettingsBean
 */
public abstract class BaseTTextBoxSettingsBean
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

    /** The value for the required field */
    private String required = "N";

    /** The value for the defaultText field */
    private String defaultText;

    /** The value for the defaultInteger field */
    private Integer defaultInteger;

    /** The value for the defaultDouble field */
    private Double defaultDouble;

    /** The value for the defaultDate field */
    private Date defaultDate;

    /** The value for the defaultChar field */
    private String defaultChar;

    /** The value for the dateIsWithTime field */
    private String dateIsWithTime = "N";

    /** The value for the defaultOption field */
    private Integer defaultOption;

    /** The value for the minOption field */
    private Integer minOption;

    /** The value for the maxOption field */
    private Integer maxOption;

    /** The value for the minTextLength field */
    private Integer minTextLength;

    /** The value for the maxTextLength field */
    private Integer maxTextLength;

    /** The value for the minDate field */
    private Date minDate;

    /** The value for the maxDate field */
    private Date maxDate;

    /** The value for the minInteger field */
    private Integer minInteger;

    /** The value for the maxInteger field */
    private Integer maxInteger;

    /** The value for the minDouble field */
    private Double minDouble;

    /** The value for the maxDouble field */
    private Double maxDouble;

    /** The value for the maxDecimalDigit field */
    private Integer maxDecimalDigit;

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
     * Get the Required
     *
     * @return String
     */
    public String getRequired ()
    {
        return required;
    }

    /**
     * Set the value of Required
     *
     * @param v new value
     */
    public void setRequired(String v)
    {

        this.required = v;
        setModified(true);

    }

    /**
     * Get the DefaultText
     *
     * @return String
     */
    public String getDefaultText ()
    {
        return defaultText;
    }

    /**
     * Set the value of DefaultText
     *
     * @param v new value
     */
    public void setDefaultText(String v)
    {

        this.defaultText = v;
        setModified(true);

    }

    /**
     * Get the DefaultInteger
     *
     * @return Integer
     */
    public Integer getDefaultInteger ()
    {
        return defaultInteger;
    }

    /**
     * Set the value of DefaultInteger
     *
     * @param v new value
     */
    public void setDefaultInteger(Integer v)
    {

        this.defaultInteger = v;
        setModified(true);

    }

    /**
     * Get the DefaultDouble
     *
     * @return Double
     */
    public Double getDefaultDouble ()
    {
        return defaultDouble;
    }

    /**
     * Set the value of DefaultDouble
     *
     * @param v new value
     */
    public void setDefaultDouble(Double v)
    {

        this.defaultDouble = v;
        setModified(true);

    }

    /**
     * Get the DefaultDate
     *
     * @return Date
     */
    public Date getDefaultDate ()
    {
        return defaultDate;
    }

    /**
     * Set the value of DefaultDate
     *
     * @param v new value
     */
    public void setDefaultDate(Date v)
    {

        this.defaultDate = v;
        setModified(true);

    }

    /**
     * Get the DefaultChar
     *
     * @return String
     */
    public String getDefaultChar ()
    {
        return defaultChar;
    }

    /**
     * Set the value of DefaultChar
     *
     * @param v new value
     */
    public void setDefaultChar(String v)
    {

        this.defaultChar = v;
        setModified(true);

    }

    /**
     * Get the DateIsWithTime
     *
     * @return String
     */
    public String getDateIsWithTime ()
    {
        return dateIsWithTime;
    }

    /**
     * Set the value of DateIsWithTime
     *
     * @param v new value
     */
    public void setDateIsWithTime(String v)
    {

        this.dateIsWithTime = v;
        setModified(true);

    }

    /**
     * Get the DefaultOption
     *
     * @return Integer
     */
    public Integer getDefaultOption ()
    {
        return defaultOption;
    }

    /**
     * Set the value of DefaultOption
     *
     * @param v new value
     */
    public void setDefaultOption(Integer v)
    {

        this.defaultOption = v;
        setModified(true);

    }

    /**
     * Get the MinOption
     *
     * @return Integer
     */
    public Integer getMinOption ()
    {
        return minOption;
    }

    /**
     * Set the value of MinOption
     *
     * @param v new value
     */
    public void setMinOption(Integer v)
    {

        this.minOption = v;
        setModified(true);

    }

    /**
     * Get the MaxOption
     *
     * @return Integer
     */
    public Integer getMaxOption ()
    {
        return maxOption;
    }

    /**
     * Set the value of MaxOption
     *
     * @param v new value
     */
    public void setMaxOption(Integer v)
    {

        this.maxOption = v;
        setModified(true);

    }

    /**
     * Get the MinTextLength
     *
     * @return Integer
     */
    public Integer getMinTextLength ()
    {
        return minTextLength;
    }

    /**
     * Set the value of MinTextLength
     *
     * @param v new value
     */
    public void setMinTextLength(Integer v)
    {

        this.minTextLength = v;
        setModified(true);

    }

    /**
     * Get the MaxTextLength
     *
     * @return Integer
     */
    public Integer getMaxTextLength ()
    {
        return maxTextLength;
    }

    /**
     * Set the value of MaxTextLength
     *
     * @param v new value
     */
    public void setMaxTextLength(Integer v)
    {

        this.maxTextLength = v;
        setModified(true);

    }

    /**
     * Get the MinDate
     *
     * @return Date
     */
    public Date getMinDate ()
    {
        return minDate;
    }

    /**
     * Set the value of MinDate
     *
     * @param v new value
     */
    public void setMinDate(Date v)
    {

        this.minDate = v;
        setModified(true);

    }

    /**
     * Get the MaxDate
     *
     * @return Date
     */
    public Date getMaxDate ()
    {
        return maxDate;
    }

    /**
     * Set the value of MaxDate
     *
     * @param v new value
     */
    public void setMaxDate(Date v)
    {

        this.maxDate = v;
        setModified(true);

    }

    /**
     * Get the MinInteger
     *
     * @return Integer
     */
    public Integer getMinInteger ()
    {
        return minInteger;
    }

    /**
     * Set the value of MinInteger
     *
     * @param v new value
     */
    public void setMinInteger(Integer v)
    {

        this.minInteger = v;
        setModified(true);

    }

    /**
     * Get the MaxInteger
     *
     * @return Integer
     */
    public Integer getMaxInteger ()
    {
        return maxInteger;
    }

    /**
     * Set the value of MaxInteger
     *
     * @param v new value
     */
    public void setMaxInteger(Integer v)
    {

        this.maxInteger = v;
        setModified(true);

    }

    /**
     * Get the MinDouble
     *
     * @return Double
     */
    public Double getMinDouble ()
    {
        return minDouble;
    }

    /**
     * Set the value of MinDouble
     *
     * @param v new value
     */
    public void setMinDouble(Double v)
    {

        this.minDouble = v;
        setModified(true);

    }

    /**
     * Get the MaxDouble
     *
     * @return Double
     */
    public Double getMaxDouble ()
    {
        return maxDouble;
    }

    /**
     * Set the value of MaxDouble
     *
     * @param v new value
     */
    public void setMaxDouble(Double v)
    {

        this.maxDouble = v;
        setModified(true);

    }

    /**
     * Get the MaxDecimalDigit
     *
     * @return Integer
     */
    public Integer getMaxDecimalDigit ()
    {
        return maxDecimalDigit;
    }

    /**
     * Set the value of MaxDecimalDigit
     *
     * @param v new value
     */
    public void setMaxDecimalDigit(Integer v)
    {

        this.maxDecimalDigit = v;
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
