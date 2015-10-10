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
 * extended; all references should be to TIssueAttributeValueBean
 */
public abstract class BaseTIssueAttributeValueBean
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

    /** The value for the attributeID field */
    private Integer attributeID;

    /** The value for the deleted field */
    private Integer deleted;

    /** The value for the workItem field */
    private Integer workItem;

    /** The value for the numericValue field */
    private Integer numericValue;

    /** The value for the timeStampValue field */
    private Date timeStampValue;

    /** The value for the longTextValue field */
    private String longTextValue;

    /** The value for the optionID field */
    private Integer optionID;

    /** The value for the person field */
    private Integer person;

    /** The value for the charValue field */
    private String charValue;

    /** The value for the displayValue field */
    private String displayValue;

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
     * Get the AttributeID
     *
     * @return Integer
     */
    public Integer getAttributeID ()
    {
        return attributeID;
    }

    /**
     * Set the value of AttributeID
     *
     * @param v new value
     */
    public void setAttributeID(Integer v)
    {

        this.attributeID = v;
        setModified(true);

    }

    /**
     * Get the Deleted
     *
     * @return Integer
     */
    public Integer getDeleted ()
    {
        return deleted;
    }

    /**
     * Set the value of Deleted
     *
     * @param v new value
     */
    public void setDeleted(Integer v)
    {

        this.deleted = v;
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
     * Get the NumericValue
     *
     * @return Integer
     */
    public Integer getNumericValue ()
    {
        return numericValue;
    }

    /**
     * Set the value of NumericValue
     *
     * @param v new value
     */
    public void setNumericValue(Integer v)
    {

        this.numericValue = v;
        setModified(true);

    }

    /**
     * Get the TimeStampValue
     *
     * @return Date
     */
    public Date getTimeStampValue ()
    {
        return timeStampValue;
    }

    /**
     * Set the value of TimeStampValue
     *
     * @param v new value
     */
    public void setTimeStampValue(Date v)
    {

        this.timeStampValue = v;
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
     * Get the OptionID
     *
     * @return Integer
     */
    public Integer getOptionID ()
    {
        return optionID;
    }

    /**
     * Set the value of OptionID
     *
     * @param v new value
     */
    public void setOptionID(Integer v)
    {

        this.optionID = v;
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
     * Get the CharValue
     *
     * @return String
     */
    public String getCharValue ()
    {
        return charValue;
    }

    /**
     * Set the value of CharValue
     *
     * @param v new value
     */
    public void setCharValue(String v)
    {

        this.charValue = v;
        setModified(true);

    }

    /**
     * Get the DisplayValue
     *
     * @return String
     */
    public String getDisplayValue ()
    {
        return displayValue;
    }

    /**
     * Set the value of DisplayValue
     *
     * @param v new value
     */
    public void setDisplayValue(String v)
    {

        this.displayValue = v;
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





    private TAttributeBean aTAttributeBean;

    /**
     * sets an associated TAttributeBean object
     *
     * @param v TAttributeBean
     */
    public void setTAttributeBean(TAttributeBean v)
    {
        if (v == null)
        {
            setAttributeID((Integer) null);
        }
        else
        {
            setAttributeID(v.getObjectID());
        }
        aTAttributeBean = v;
    }


    /**
     * Get the associated TAttributeBean object
     *
     * @return the associated TAttributeBean object
     */
    public TAttributeBean getTAttributeBean()
    {
        return aTAttributeBean;
    }





    private TAttributeOptionBean aTAttributeOptionBean;

    /**
     * sets an associated TAttributeOptionBean object
     *
     * @param v TAttributeOptionBean
     */
    public void setTAttributeOptionBean(TAttributeOptionBean v)
    {
        if (v == null)
        {
            setOptionID((Integer) null);
        }
        else
        {
            setOptionID(v.getObjectID());
        }
        aTAttributeOptionBean = v;
    }


    /**
     * Get the associated TAttributeOptionBean object
     *
     * @return the associated TAttributeOptionBean object
     */
    public TAttributeOptionBean getTAttributeOptionBean()
    {
        return aTAttributeOptionBean;
    }



}
