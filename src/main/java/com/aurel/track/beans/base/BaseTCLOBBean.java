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
 * extended; all references should be to TCLOBBean
 */
public abstract class BaseTCLOBBean
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

    /** The value for the cLOBValue field */
    private String cLOBValue;

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
     * Get the CLOBValue
     *
     * @return String
     */
    public String getCLOBValue ()
    {
        return cLOBValue;
    }

    /**
     * Set the value of CLOBValue
     *
     * @param v new value
     */
    public void setCLOBValue(String v)
    {

        this.cLOBValue = v;
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
     * Collection to store aggregation of collTEventBeans
     */
    protected List<TEventBean> collTEventBeans;

    /**
     * Returns the collection of TEventBeans
     */
    public List<TEventBean> getTEventBeans()
    {
        return collTEventBeans;
    }

    /**
     * Sets the collection of TEventBeans to the specified value
     */
    public void setTEventBeans(List<TEventBean> list)
    {
        if (list == null)
        {
            collTEventBeans = null;
        }
        else
        {
            collTEventBeans = new ArrayList<TEventBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTQueryRepositoryBeans
     */
    protected List<TQueryRepositoryBean> collTQueryRepositoryBeans;

    /**
     * Returns the collection of TQueryRepositoryBeans
     */
    public List<TQueryRepositoryBean> getTQueryRepositoryBeans()
    {
        return collTQueryRepositoryBeans;
    }

    /**
     * Sets the collection of TQueryRepositoryBeans to the specified value
     */
    public void setTQueryRepositoryBeans(List<TQueryRepositoryBean> list)
    {
        if (list == null)
        {
            collTQueryRepositoryBeans = null;
        }
        else
        {
            collTQueryRepositoryBeans = new ArrayList<TQueryRepositoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTLastExecutedQueryBeans
     */
    protected List<TLastExecutedQueryBean> collTLastExecutedQueryBeans;

    /**
     * Returns the collection of TLastExecutedQueryBeans
     */
    public List<TLastExecutedQueryBean> getTLastExecutedQueryBeans()
    {
        return collTLastExecutedQueryBeans;
    }

    /**
     * Sets the collection of TLastExecutedQueryBeans to the specified value
     */
    public void setTLastExecutedQueryBeans(List<TLastExecutedQueryBean> list)
    {
        if (list == null)
        {
            collTLastExecutedQueryBeans = null;
        }
        else
        {
            collTLastExecutedQueryBeans = new ArrayList<TLastExecutedQueryBean>(list);
        }
    }

}
