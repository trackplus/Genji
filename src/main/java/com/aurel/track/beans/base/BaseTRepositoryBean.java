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
 * Version control repositiories defined
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TRepositoryBean
 */
public abstract class BaseTRepositoryBean
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

    /** The value for the repositoryType field */
    private String repositoryType;

    /** The value for the repositoryURL field */
    private String repositoryURL;

    /** The value for the startDate field */
    private Date startDate;

    /** The value for the endDate field */
    private Date endDate;

    /** The value for the statusKey field */
    private Integer statusKey;

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
     * Get the RepositoryType
     *
     * @return String
     */
    public String getRepositoryType ()
    {
        return repositoryType;
    }

    /**
     * Set the value of RepositoryType
     *
     * @param v new value
     */
    public void setRepositoryType(String v)
    {

        this.repositoryType = v;
        setModified(true);

    }

    /**
     * Get the RepositoryURL
     *
     * @return String
     */
    public String getRepositoryURL ()
    {
        return repositoryURL;
    }

    /**
     * Set the value of RepositoryURL
     *
     * @param v new value
     */
    public void setRepositoryURL(String v)
    {

        this.repositoryURL = v;
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
     * Get the StatusKey
     *
     * @return Integer
     */
    public Integer getStatusKey ()
    {
        return statusKey;
    }

    /**
     * Set the value of StatusKey
     *
     * @param v new value
     */
    public void setStatusKey(Integer v)
    {

        this.statusKey = v;
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
     * Collection to store aggregation of collTRevisionBeans
     */
    protected List<TRevisionBean> collTRevisionBeans;

    /**
     * Returns the collection of TRevisionBeans
     */
    public List<TRevisionBean> getTRevisionBeans()
    {
        return collTRevisionBeans;
    }

    /**
     * Sets the collection of TRevisionBeans to the specified value
     */
    public void setTRevisionBeans(List<TRevisionBean> list)
    {
        if (list == null)
        {
            collTRevisionBeans = null;
        }
        else
        {
            collTRevisionBeans = new ArrayList<TRevisionBean>(list);
        }
    }

}
