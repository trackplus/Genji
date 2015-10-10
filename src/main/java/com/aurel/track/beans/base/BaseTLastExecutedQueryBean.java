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
 * store the last n executed queries with all data needed to execute them again
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TLastExecutedQueryBean
 */
public abstract class BaseTLastExecutedQueryBean
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

    /** The value for the person field */
    private Integer person;

    /** The value for the queryType field */
    private Integer queryType;

    /** The value for the queryClob field */
    private Integer queryClob;

    /** The value for the queryKey field */
    private Integer queryKey;

    /** The value for the lastExecutedTime field */
    private Date lastExecutedTime;

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
     * Get the QueryType
     *
     * @return Integer
     */
    public Integer getQueryType ()
    {
        return queryType;
    }

    /**
     * Set the value of QueryType
     *
     * @param v new value
     */
    public void setQueryType(Integer v)
    {

        this.queryType = v;
        setModified(true);

    }

    /**
     * Get the QueryClob
     *
     * @return Integer
     */
    public Integer getQueryClob ()
    {
        return queryClob;
    }

    /**
     * Set the value of QueryClob
     *
     * @param v new value
     */
    public void setQueryClob(Integer v)
    {

        this.queryClob = v;
        setModified(true);

    }

    /**
     * Get the QueryKey
     *
     * @return Integer
     */
    public Integer getQueryKey ()
    {
        return queryKey;
    }

    /**
     * Set the value of QueryKey
     *
     * @param v new value
     */
    public void setQueryKey(Integer v)
    {

        this.queryKey = v;
        setModified(true);

    }

    /**
     * Get the LastExecutedTime
     *
     * @return Date
     */
    public Date getLastExecutedTime ()
    {
        return lastExecutedTime;
    }

    /**
     * Set the value of LastExecutedTime
     *
     * @param v new value
     */
    public void setLastExecutedTime(Date v)
    {

        this.lastExecutedTime = v;
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





    private TCLOBBean aTCLOBBean;

    /**
     * sets an associated TCLOBBean object
     *
     * @param v TCLOBBean
     */
    public void setTCLOBBean(TCLOBBean v)
    {
        if (v == null)
        {
            setQueryClob((Integer) null);
        }
        else
        {
            setQueryClob(v.getObjectID());
        }
        aTCLOBBean = v;
    }


    /**
     * Get the associated TCLOBBean object
     *
     * @return the associated TCLOBBean object
     */
    public TCLOBBean getTCLOBBean()
    {
        return aTCLOBBean;
    }



}
