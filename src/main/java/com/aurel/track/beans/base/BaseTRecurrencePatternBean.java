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
 * extended; all references should be to TRecurrencePatternBean
 */
public abstract class BaseTRecurrencePatternBean
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

    /** The value for the recurrencePeriod field */
    private Integer recurrencePeriod;

    /** The value for the param1 field */
    private Integer param1;

    /** The value for the param2 field */
    private Integer param2;

    /** The value for the param3 field */
    private Integer param3;

    /** The value for the days field */
    private String days;

    /** The value for the dateIsAbsolute field */
    private String dateIsAbsolute = "Y";

    /** The value for the startDate field */
    private Date startDate;

    /** The value for the endDate field */
    private Date endDate;

    /** The value for the occurenceType field */
    private Integer occurenceType;

    /** The value for the noOfOccurences field */
    private Integer noOfOccurences;

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
     * Get the RecurrencePeriod
     *
     * @return Integer
     */
    public Integer getRecurrencePeriod ()
    {
        return recurrencePeriod;
    }

    /**
     * Set the value of RecurrencePeriod
     *
     * @param v new value
     */
    public void setRecurrencePeriod(Integer v)
    {

        this.recurrencePeriod = v;
        setModified(true);

    }

    /**
     * Get the Param1
     *
     * @return Integer
     */
    public Integer getParam1 ()
    {
        return param1;
    }

    /**
     * Set the value of Param1
     *
     * @param v new value
     */
    public void setParam1(Integer v)
    {

        this.param1 = v;
        setModified(true);

    }

    /**
     * Get the Param2
     *
     * @return Integer
     */
    public Integer getParam2 ()
    {
        return param2;
    }

    /**
     * Set the value of Param2
     *
     * @param v new value
     */
    public void setParam2(Integer v)
    {

        this.param2 = v;
        setModified(true);

    }

    /**
     * Get the Param3
     *
     * @return Integer
     */
    public Integer getParam3 ()
    {
        return param3;
    }

    /**
     * Set the value of Param3
     *
     * @param v new value
     */
    public void setParam3(Integer v)
    {

        this.param3 = v;
        setModified(true);

    }

    /**
     * Get the Days
     *
     * @return String
     */
    public String getDays ()
    {
        return days;
    }

    /**
     * Set the value of Days
     *
     * @param v new value
     */
    public void setDays(String v)
    {

        this.days = v;
        setModified(true);

    }

    /**
     * Get the DateIsAbsolute
     *
     * @return String
     */
    public String getDateIsAbsolute ()
    {
        return dateIsAbsolute;
    }

    /**
     * Set the value of DateIsAbsolute
     *
     * @param v new value
     */
    public void setDateIsAbsolute(String v)
    {

        this.dateIsAbsolute = v;
        setModified(true);

    }

    /**
     * Get the StartDate
     *
     * @return Date
     */
    public Date getStartDate ()
    {
        return startDate;
    }

    /**
     * Set the value of StartDate
     *
     * @param v new value
     */
    public void setStartDate(Date v)
    {

        this.startDate = v;
        setModified(true);

    }

    /**
     * Get the EndDate
     *
     * @return Date
     */
    public Date getEndDate ()
    {
        return endDate;
    }

    /**
     * Set the value of EndDate
     *
     * @param v new value
     */
    public void setEndDate(Date v)
    {

        this.endDate = v;
        setModified(true);

    }

    /**
     * Get the OccurenceType
     *
     * @return Integer
     */
    public Integer getOccurenceType ()
    {
        return occurenceType;
    }

    /**
     * Set the value of OccurenceType
     *
     * @param v new value
     */
    public void setOccurenceType(Integer v)
    {

        this.occurenceType = v;
        setModified(true);

    }

    /**
     * Get the NoOfOccurences
     *
     * @return Integer
     */
    public Integer getNoOfOccurences ()
    {
        return noOfOccurences;
    }

    /**
     * Set the value of NoOfOccurences
     *
     * @param v new value
     */
    public void setNoOfOccurences(Integer v)
    {

        this.noOfOccurences = v;
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

    



    /**
     * Collection to store aggregation of collTReportSubscribeBeans
     */
    protected List<TReportSubscribeBean> collTReportSubscribeBeans;

    /**
     * Returns the collection of TReportSubscribeBeans
     */
    public List<TReportSubscribeBean> getTReportSubscribeBeans()
    {
        return collTReportSubscribeBeans;
    }

    /**
     * Sets the collection of TReportSubscribeBeans to the specified value
     */
    public void setTReportSubscribeBeans(List<TReportSubscribeBean> list)
    {
        if (list == null)
        {
            collTReportSubscribeBeans = null;
        }
        else
        {
            collTReportSubscribeBeans = new ArrayList<TReportSubscribeBean>(list);
        }
    }

}
