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
 * extended; all references should be to TCostBean
 */
public abstract class BaseTCostBean
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

    /** The value for the account field */
    private Integer account;

    /** The value for the person field */
    private Integer person;

    /** The value for the workitem field */
    private Integer workitem;

    /** The value for the hours field */
    private Double hours;

    /** The value for the cost field */
    private Double cost;

    /** The value for the subject field */
    private String subject;

    /** The value for the efforttype field */
    private Integer efforttype;

    /** The value for the effortvalue field */
    private Double effortvalue;

    /** The value for the effortdate field */
    private Date effortdate;

    /** The value for the invoicenumber field */
    private String invoicenumber;

    /** The value for the invoicedate field */
    private Date invoicedate;

    /** The value for the invoicepath field */
    private String invoicepath;

    /** The value for the description field */
    private String description;

    /** The value for the moreProps field */
    private String moreProps;

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
     * Get the Account
     *
     * @return Integer
     */
    public Integer getAccount ()
    {
        return account;
    }

    /**
     * Set the value of Account
     *
     * @param v new value
     */
    public void setAccount(Integer v)
    {

        this.account = v;
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
     * Get the Workitem
     *
     * @return Integer
     */
    public Integer getWorkitem ()
    {
        return workitem;
    }

    /**
     * Set the value of Workitem
     *
     * @param v new value
     */
    public void setWorkitem(Integer v)
    {

        this.workitem = v;
        setModified(true);

    }

    /**
     * Get the Hours
     *
     * @return Double
     */
    public Double getHours ()
    {
        return hours;
    }

    /**
     * Set the value of Hours
     *
     * @param v new value
     */
    public void setHours(Double v)
    {

        this.hours = v;
        setModified(true);

    }

    /**
     * Get the Cost
     *
     * @return Double
     */
    public Double getCost ()
    {
        return cost;
    }

    /**
     * Set the value of Cost
     *
     * @param v new value
     */
    public void setCost(Double v)
    {

        this.cost = v;
        setModified(true);

    }

    /**
     * Get the Subject
     *
     * @return String
     */
    public String getSubject ()
    {
        return subject;
    }

    /**
     * Set the value of Subject
     *
     * @param v new value
     */
    public void setSubject(String v)
    {

        this.subject = v;
        setModified(true);

    }

    /**
     * Get the Efforttype
     *
     * @return Integer
     */
    public Integer getEfforttype ()
    {
        return efforttype;
    }

    /**
     * Set the value of Efforttype
     *
     * @param v new value
     */
    public void setEfforttype(Integer v)
    {

        this.efforttype = v;
        setModified(true);

    }

    /**
     * Get the Effortvalue
     *
     * @return Double
     */
    public Double getEffortvalue ()
    {
        return effortvalue;
    }

    /**
     * Set the value of Effortvalue
     *
     * @param v new value
     */
    public void setEffortvalue(Double v)
    {

        this.effortvalue = v;
        setModified(true);

    }

    /**
     * Get the Effortdate
     *
     * @return Date
     */
    public Date getEffortdate ()
    {
        return effortdate;
    }

    /**
     * Set the value of Effortdate
     *
     * @param v new value
     */
    public void setEffortdate(Date v)
    {

        this.effortdate = v;
        setModified(true);

    }

    /**
     * Get the Invoicenumber
     *
     * @return String
     */
    public String getInvoicenumber ()
    {
        return invoicenumber;
    }

    /**
     * Set the value of Invoicenumber
     *
     * @param v new value
     */
    public void setInvoicenumber(String v)
    {

        this.invoicenumber = v;
        setModified(true);

    }

    /**
     * Get the Invoicedate
     *
     * @return Date
     */
    public Date getInvoicedate ()
    {
        return invoicedate;
    }

    /**
     * Set the value of Invoicedate
     *
     * @param v new value
     */
    public void setInvoicedate(Date v)
    {

        this.invoicedate = v;
        setModified(true);

    }

    /**
     * Get the Invoicepath
     *
     * @return String
     */
    public String getInvoicepath ()
    {
        return invoicepath;
    }

    /**
     * Set the value of Invoicepath
     *
     * @param v new value
     */
    public void setInvoicepath(String v)
    {

        this.invoicepath = v;
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
     * Get the MoreProps
     *
     * @return String
     */
    public String getMoreProps ()
    {
        return moreProps;
    }

    /**
     * Set the value of MoreProps
     *
     * @param v new value
     */
    public void setMoreProps(String v)
    {

        this.moreProps = v;
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

    



    private TAccountBean aTAccountBean;

    /**
     * sets an associated TAccountBean object
     *
     * @param v TAccountBean
     */
    public void setTAccountBean(TAccountBean v)
    {
        if (v == null)
        {
            setAccount((Integer) null);
        }
        else
        {
            setAccount(v.getObjectID());
        }
        aTAccountBean = v;
    }


    /**
     * Get the associated TAccountBean object
     *
     * @return the associated TAccountBean object
     */
    public TAccountBean getTAccountBean()
    {
        return aTAccountBean;
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
            setWorkitem((Integer) null);
        }
        else
        {
            setWorkitem(v.getObjectID());
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





    private TEffortTypeBean aTEffortTypeBean;

    /**
     * sets an associated TEffortTypeBean object
     *
     * @param v TEffortTypeBean
     */
    public void setTEffortTypeBean(TEffortTypeBean v)
    {
        if (v == null)
        {
            setEfforttype((Integer) null);
        }
        else
        {
            setEfforttype(v.getObjectID());
        }
        aTEffortTypeBean = v;
    }


    /**
     * Get the associated TEffortTypeBean object
     *
     * @return the associated TEffortTypeBean object
     */
    public TEffortTypeBean getTEffortTypeBean()
    {
        return aTEffortTypeBean;
    }



}
