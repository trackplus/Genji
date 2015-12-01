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
 * extended; all references should be to TFieldChangeBean
 */
public abstract class BaseTFieldChangeBean
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

    /** The value for the fieldKey field */
    private Integer fieldKey;

    /** The value for the historyTransaction field */
    private Integer historyTransaction;

    /** The value for the newTextValue field */
    private String newTextValue;

    /** The value for the oldTextValue field */
    private String oldTextValue;

    /** The value for the newIntegerValue field */
    private Integer newIntegerValue;

    /** The value for the oldIntegerValue field */
    private Integer oldIntegerValue;

    /** The value for the newDoubleValue field */
    private Double newDoubleValue;

    /** The value for the oldDoubleValue field */
    private Double oldDoubleValue;

    /** The value for the newDateValue field */
    private Date newDateValue;

    /** The value for the oldDateValue field */
    private Date oldDateValue;

    /** The value for the newCharacterValue field */
    private String newCharacterValue;

    /** The value for the oldCharacterValue field */
    private String oldCharacterValue;

    /** The value for the newLongTextValue field */
    private String newLongTextValue;

    /** The value for the oldLongTextValue field */
    private String oldLongTextValue;

    /** The value for the newSystemOptionID field */
    private Integer newSystemOptionID;

    /** The value for the oldSystemOptionID field */
    private Integer oldSystemOptionID;

    /** The value for the systemOptionType field */
    private Integer systemOptionType;

    /** The value for the newCustomOptionID field */
    private Integer newCustomOptionID;

    /** The value for the oldCustomOptionID field */
    private Integer oldCustomOptionID;

    /** The value for the parameterCode field */
    private Integer parameterCode;

    /** The value for the validValue field */
    private Integer validValue;

    /** The value for the parentComment field */
    private Integer parentComment;

    /** The value for the timesEdited field */
    private Integer timesEdited;

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
     * Get the HistoryTransaction
     *
     * @return Integer
     */
    public Integer getHistoryTransaction ()
    {
        return historyTransaction;
    }

    /**
     * Set the value of HistoryTransaction
     *
     * @param v new value
     */
    public void setHistoryTransaction(Integer v)
    {

        this.historyTransaction = v;
        setModified(true);

    }

    /**
     * Get the NewTextValue
     *
     * @return String
     */
    public String getNewTextValue ()
    {
        return newTextValue;
    }

    /**
     * Set the value of NewTextValue
     *
     * @param v new value
     */
    public void setNewTextValue(String v)
    {

        this.newTextValue = v;
        setModified(true);

    }

    /**
     * Get the OldTextValue
     *
     * @return String
     */
    public String getOldTextValue ()
    {
        return oldTextValue;
    }

    /**
     * Set the value of OldTextValue
     *
     * @param v new value
     */
    public void setOldTextValue(String v)
    {

        this.oldTextValue = v;
        setModified(true);

    }

    /**
     * Get the NewIntegerValue
     *
     * @return Integer
     */
    public Integer getNewIntegerValue ()
    {
        return newIntegerValue;
    }

    /**
     * Set the value of NewIntegerValue
     *
     * @param v new value
     */
    public void setNewIntegerValue(Integer v)
    {

        this.newIntegerValue = v;
        setModified(true);

    }

    /**
     * Get the OldIntegerValue
     *
     * @return Integer
     */
    public Integer getOldIntegerValue ()
    {
        return oldIntegerValue;
    }

    /**
     * Set the value of OldIntegerValue
     *
     * @param v new value
     */
    public void setOldIntegerValue(Integer v)
    {

        this.oldIntegerValue = v;
        setModified(true);

    }

    /**
     * Get the NewDoubleValue
     *
     * @return Double
     */
    public Double getNewDoubleValue ()
    {
        return newDoubleValue;
    }

    /**
     * Set the value of NewDoubleValue
     *
     * @param v new value
     */
    public void setNewDoubleValue(Double v)
    {

        this.newDoubleValue = v;
        setModified(true);

    }

    /**
     * Get the OldDoubleValue
     *
     * @return Double
     */
    public Double getOldDoubleValue ()
    {
        return oldDoubleValue;
    }

    /**
     * Set the value of OldDoubleValue
     *
     * @param v new value
     */
    public void setOldDoubleValue(Double v)
    {

        this.oldDoubleValue = v;
        setModified(true);

    }

    /**
     * Get the NewDateValue
     *
     * @return Date
     */
    public Date getNewDateValue ()
    {
        return newDateValue;
    }

    /**
     * Set the value of NewDateValue
     *
     * @param v new value
     */
    public void setNewDateValue(Date v)
    {

        this.newDateValue = v;
        setModified(true);

    }

    /**
     * Get the OldDateValue
     *
     * @return Date
     */
    public Date getOldDateValue ()
    {
        return oldDateValue;
    }

    /**
     * Set the value of OldDateValue
     *
     * @param v new value
     */
    public void setOldDateValue(Date v)
    {

        this.oldDateValue = v;
        setModified(true);

    }

    /**
     * Get the NewCharacterValue
     *
     * @return String
     */
    public String getNewCharacterValue ()
    {
        return newCharacterValue;
    }

    /**
     * Set the value of NewCharacterValue
     *
     * @param v new value
     */
    public void setNewCharacterValue(String v)
    {

        this.newCharacterValue = v;
        setModified(true);

    }

    /**
     * Get the OldCharacterValue
     *
     * @return String
     */
    public String getOldCharacterValue ()
    {
        return oldCharacterValue;
    }

    /**
     * Set the value of OldCharacterValue
     *
     * @param v new value
     */
    public void setOldCharacterValue(String v)
    {

        this.oldCharacterValue = v;
        setModified(true);

    }

    /**
     * Get the NewLongTextValue
     *
     * @return String
     */
    public String getNewLongTextValue ()
    {
        return newLongTextValue;
    }

    /**
     * Set the value of NewLongTextValue
     *
     * @param v new value
     */
    public void setNewLongTextValue(String v)
    {

        this.newLongTextValue = v;
        setModified(true);

    }

    /**
     * Get the OldLongTextValue
     *
     * @return String
     */
    public String getOldLongTextValue ()
    {
        return oldLongTextValue;
    }

    /**
     * Set the value of OldLongTextValue
     *
     * @param v new value
     */
    public void setOldLongTextValue(String v)
    {

        this.oldLongTextValue = v;
        setModified(true);

    }

    /**
     * Get the NewSystemOptionID
     *
     * @return Integer
     */
    public Integer getNewSystemOptionID ()
    {
        return newSystemOptionID;
    }

    /**
     * Set the value of NewSystemOptionID
     *
     * @param v new value
     */
    public void setNewSystemOptionID(Integer v)
    {

        this.newSystemOptionID = v;
        setModified(true);

    }

    /**
     * Get the OldSystemOptionID
     *
     * @return Integer
     */
    public Integer getOldSystemOptionID ()
    {
        return oldSystemOptionID;
    }

    /**
     * Set the value of OldSystemOptionID
     *
     * @param v new value
     */
    public void setOldSystemOptionID(Integer v)
    {

        this.oldSystemOptionID = v;
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
     * Get the NewCustomOptionID
     *
     * @return Integer
     */
    public Integer getNewCustomOptionID ()
    {
        return newCustomOptionID;
    }

    /**
     * Set the value of NewCustomOptionID
     *
     * @param v new value
     */
    public void setNewCustomOptionID(Integer v)
    {

        this.newCustomOptionID = v;
        setModified(true);

    }

    /**
     * Get the OldCustomOptionID
     *
     * @return Integer
     */
    public Integer getOldCustomOptionID ()
    {
        return oldCustomOptionID;
    }

    /**
     * Set the value of OldCustomOptionID
     *
     * @param v new value
     */
    public void setOldCustomOptionID(Integer v)
    {

        this.oldCustomOptionID = v;
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
     * Get the ParentComment
     *
     * @return Integer
     */
    public Integer getParentComment ()
    {
        return parentComment;
    }

    /**
     * Set the value of ParentComment
     *
     * @param v new value
     */
    public void setParentComment(Integer v)
    {

        this.parentComment = v;
        setModified(true);

    }

    /**
     * Get the TimesEdited
     *
     * @return Integer
     */
    public Integer getTimesEdited ()
    {
        return timesEdited;
    }

    /**
     * Set the value of TimesEdited
     *
     * @param v new value
     */
    public void setTimesEdited(Integer v)
    {

        this.timesEdited = v;
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

    



    private THistoryTransactionBean aTHistoryTransactionBean;

    /**
     * sets an associated THistoryTransactionBean object
     *
     * @param v THistoryTransactionBean
     */
    public void setTHistoryTransactionBean(THistoryTransactionBean v)
    {
        if (v == null)
        {
            setHistoryTransaction((Integer) null);
        }
        else
        {
            setHistoryTransaction(v.getObjectID());
        }
        aTHistoryTransactionBean = v;
    }


    /**
     * Get the associated THistoryTransactionBean object
     *
     * @return the associated THistoryTransactionBean object
     */
    public THistoryTransactionBean getTHistoryTransactionBean()
    {
        return aTHistoryTransactionBean;
    }





    private TFieldChangeBean aTFieldChangeBeanRelatedByParentComment;

    /**
     * sets an associated TFieldChangeBean object
     *
     * @param v TFieldChangeBean
     */
    public void setTFieldChangeBeanRelatedByParentComment(TFieldChangeBean v)
    {
        if (v == null)
        {
            setParentComment((Integer) null);
        }
        else
        {
            setParentComment(v.getObjectID());
        }
        aTFieldChangeBeanRelatedByParentComment = v;
    }


    /**
     * Get the associated TFieldChangeBean object
     *
     * @return the associated TFieldChangeBean object
     */
    public TFieldChangeBean getTFieldChangeBeanRelatedByParentComment()
    {
        return aTFieldChangeBeanRelatedByParentComment;
    }





    private TOptionBean aTOptionBeanRelatedByNewCustomOptionID;

    /**
     * sets an associated TOptionBean object
     *
     * @param v TOptionBean
     */
    public void setTOptionBeanRelatedByNewCustomOptionID(TOptionBean v)
    {
        if (v == null)
        {
            setNewCustomOptionID((Integer) null);
        }
        else
        {
            setNewCustomOptionID(v.getObjectID());
        }
        aTOptionBeanRelatedByNewCustomOptionID = v;
    }


    /**
     * Get the associated TOptionBean object
     *
     * @return the associated TOptionBean object
     */
    public TOptionBean getTOptionBeanRelatedByNewCustomOptionID()
    {
        return aTOptionBeanRelatedByNewCustomOptionID;
    }





    private TOptionBean aTOptionBeanRelatedByOldCustomOptionID;

    /**
     * sets an associated TOptionBean object
     *
     * @param v TOptionBean
     */
    public void setTOptionBeanRelatedByOldCustomOptionID(TOptionBean v)
    {
        if (v == null)
        {
            setOldCustomOptionID((Integer) null);
        }
        else
        {
            setOldCustomOptionID(v.getObjectID());
        }
        aTOptionBeanRelatedByOldCustomOptionID = v;
    }


    /**
     * Get the associated TOptionBean object
     *
     * @return the associated TOptionBean object
     */
    public TOptionBean getTOptionBeanRelatedByOldCustomOptionID()
    {
        return aTOptionBeanRelatedByOldCustomOptionID;
    }



}
