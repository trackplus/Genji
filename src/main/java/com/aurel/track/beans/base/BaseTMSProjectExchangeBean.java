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
 * extended; all references should be to TMSProjectExchangeBean
 */
public abstract class BaseTMSProjectExchangeBean
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

    /** The value for the exchangeDirection field */
    private Integer exchangeDirection;

    /** The value for the entityID field */
    private Integer entityID;

    /** The value for the entityType field */
    private Integer entityType;

    /** The value for the fileName field */
    private String fileName;

    /** The value for the fileContent field */
    private String fileContent;

    /** The value for the changedBy field */
    private Integer changedBy;

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
     * Get the ExchangeDirection
     *
     * @return Integer
     */
    public Integer getExchangeDirection ()
    {
        return exchangeDirection;
    }

    /**
     * Set the value of ExchangeDirection
     *
     * @param v new value
     */
    public void setExchangeDirection(Integer v)
    {

        this.exchangeDirection = v;
        setModified(true);

    }

    /**
     * Get the EntityID
     *
     * @return Integer
     */
    public Integer getEntityID ()
    {
        return entityID;
    }

    /**
     * Set the value of EntityID
     *
     * @param v new value
     */
    public void setEntityID(Integer v)
    {

        this.entityID = v;
        setModified(true);

    }

    /**
     * Get the EntityType
     *
     * @return Integer
     */
    public Integer getEntityType ()
    {
        return entityType;
    }

    /**
     * Set the value of EntityType
     *
     * @param v new value
     */
    public void setEntityType(Integer v)
    {

        this.entityType = v;
        setModified(true);

    }

    /**
     * Get the FileName
     *
     * @return String
     */
    public String getFileName ()
    {
        return fileName;
    }

    /**
     * Set the value of FileName
     *
     * @param v new value
     */
    public void setFileName(String v)
    {

        this.fileName = v;
        setModified(true);

    }

    /**
     * Get the FileContent
     *
     * @return String
     */
    public String getFileContent ()
    {
        return fileContent;
    }

    /**
     * Set the value of FileContent
     *
     * @param v new value
     */
    public void setFileContent(String v)
    {

        this.fileContent = v;
        setModified(true);

    }

    /**
     * Get the ChangedBy
     *
     * @return Integer
     */
    public Integer getChangedBy ()
    {
        return changedBy;
    }

    /**
     * Set the value of ChangedBy
     *
     * @param v new value
     */
    public void setChangedBy(Integer v)
    {

        this.changedBy = v;
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
            setChangedBy((Integer) null);
        }
        else
        {
            setChangedBy(v.getObjectID());
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
