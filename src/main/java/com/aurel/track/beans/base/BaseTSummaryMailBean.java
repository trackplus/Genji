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
 * This table holds the summary mail parts before they are sent
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TSummaryMailBean
 */
public abstract class BaseTSummaryMailBean
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

    /** The value for the workItem field */
    private Integer workItem;

    /** The value for the pERSONFROM field */
    private Integer pERSONFROM;

    /** The value for the fromAddress field */
    private String fromAddress;

    /** The value for the mailSubject field */
    private String mailSubject;

    /** The value for the workItemLink field */
    private String workItemLink;

    /** The value for the pERSONTO field */
    private Integer pERSONTO;

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
     * Get the PERSONFROM
     *
     * @return Integer
     */
    public Integer getPERSONFROM ()
    {
        return pERSONFROM;
    }

    /**
     * Set the value of PERSONFROM
     *
     * @param v new value
     */
    public void setPERSONFROM(Integer v)
    {

        this.pERSONFROM = v;
        setModified(true);

    }

    /**
     * Get the FromAddress
     *
     * @return String
     */
    public String getFromAddress ()
    {
        return fromAddress;
    }

    /**
     * Set the value of FromAddress
     *
     * @param v new value
     */
    public void setFromAddress(String v)
    {

        this.fromAddress = v;
        setModified(true);

    }

    /**
     * Get the MailSubject
     *
     * @return String
     */
    public String getMailSubject ()
    {
        return mailSubject;
    }

    /**
     * Set the value of MailSubject
     *
     * @param v new value
     */
    public void setMailSubject(String v)
    {

        this.mailSubject = v;
        setModified(true);

    }

    /**
     * Get the WorkItemLink
     *
     * @return String
     */
    public String getWorkItemLink ()
    {
        return workItemLink;
    }

    /**
     * Set the value of WorkItemLink
     *
     * @param v new value
     */
    public void setWorkItemLink(String v)
    {

        this.workItemLink = v;
        setModified(true);

    }

    /**
     * Get the PERSONTO
     *
     * @return Integer
     */
    public Integer getPERSONTO ()
    {
        return pERSONTO;
    }

    /**
     * Set the value of PERSONTO
     *
     * @param v new value
     */
    public void setPERSONTO(Integer v)
    {

        this.pERSONTO = v;
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





    private TPersonBean aTPersonBeanRelatedByPERSONFROM;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByPERSONFROM(TPersonBean v)
    {
        if (v == null)
        {
            setPERSONFROM((Integer) null);
        }
        else
        {
            setPERSONFROM(v.getObjectID());
        }
        aTPersonBeanRelatedByPERSONFROM = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByPERSONFROM()
    {
        return aTPersonBeanRelatedByPERSONFROM;
    }





    private TPersonBean aTPersonBeanRelatedByPERSONTO;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByPERSONTO(TPersonBean v)
    {
        if (v == null)
        {
            setPERSONTO((Integer) null);
        }
        else
        {
            setPERSONTO(v.getObjectID());
        }
        aTPersonBeanRelatedByPERSONTO = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByPERSONTO()
    {
        return aTPersonBeanRelatedByPERSONTO;
    }



}
