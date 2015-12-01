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
 * extended; all references should be to TAttributeValueBean
 */
public abstract class BaseTAttributeValueBean
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

    /** The value for the field field */
    private Integer field;

    /** The value for the workItem field */
    private Integer workItem;

    /** The value for the textValue field */
    private String textValue;

    /** The value for the integerValue field */
    private Integer integerValue;

    /** The value for the doubleValue field */
    private Double doubleValue;

    /** The value for the dateValue field */
    private Date dateValue;

    /** The value for the characterValue field */
    private String characterValue;

    /** The value for the longTextValue field */
    private String longTextValue;

    /** The value for the systemOptionID field */
    private Integer systemOptionID;

    /** The value for the systemOptionType field */
    private Integer systemOptionType;

    /** The value for the customOptionID field */
    private Integer customOptionID;

    /** The value for the parameterCode field */
    private Integer parameterCode;

    /** The value for the validValue field */
    private Integer validValue;

    /** The value for the lastEdit field */
    private Date lastEdit;

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
     * Get the Field
     *
     * @return Integer
     */
    public Integer getField ()
    {
        return field;
    }

    /**
     * Set the value of Field
     *
     * @param v new value
     */
    public void setField(Integer v)
    {

        this.field = v;
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
     * Get the LongTextValue
     *
     * @return String
     */
    public String getLongTextValue ()
    {
        return longTextValue;
    }

    /**
     * Set the value of LongTextValue
     *
     * @param v new value
     */
    public void setLongTextValue(String v)
    {

        this.longTextValue = v;
        setModified(true);

    }

    /**
     * Get the SystemOptionID
     *
     * @return Integer
     */
    public Integer getSystemOptionID ()
    {
        return systemOptionID;
    }

    /**
     * Set the value of SystemOptionID
     *
     * @param v new value
     */
    public void setSystemOptionID(Integer v)
    {

        this.systemOptionID = v;
        setModified(true);

    }

    /**
     * Get the SystemOptionType
     *
     * @return Integer
     */
    public Integer getSystemOptionType ()
    {
        return systemOptionType;
    }

    /**
     * Set the value of SystemOptionType
     *
     * @param v new value
     */
    public void setSystemOptionType(Integer v)
    {

        this.systemOptionType = v;
        setModified(true);

    }

    /**
     * Get the CustomOptionID
     *
     * @return Integer
     */
    public Integer getCustomOptionID ()
    {
        return customOptionID;
    }

    /**
     * Set the value of CustomOptionID
     *
     * @param v new value
     */
    public void setCustomOptionID(Integer v)
    {

        this.customOptionID = v;
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
     * Get the LastEdit
     *
     * @return Date
     */
    public Date getLastEdit ()
    {
        return lastEdit;
    }

    /**
     * Set the value of LastEdit
     *
     * @param v new value
     */
    public void setLastEdit(Date v)
    {

        this.lastEdit = v;
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

    



    private TFieldBean aTFieldBean;

    /**
     * sets an associated TFieldBean object
     *
     * @param v TFieldBean
     */
    public void setTFieldBean(TFieldBean v)
    {
        if (v == null)
        {
            setField((Integer) null);
        }
        else
        {
            setField(v.getObjectID());
        }
        aTFieldBean = v;
    }


    /**
     * Get the associated TFieldBean object
     *
     * @return the associated TFieldBean object
     */
    public TFieldBean getTFieldBean()
    {
        return aTFieldBean;
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
