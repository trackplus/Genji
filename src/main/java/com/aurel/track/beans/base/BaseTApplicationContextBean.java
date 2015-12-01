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
 * extended; all references should be to TApplicationContextBean
 */
public abstract class BaseTApplicationContextBean
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

    /** The value for the loggedFullUsers field */
    private Integer loggedFullUsers;

    /** The value for the loggedLimitedUsers field */
    private Integer loggedLimitedUsers;

    /** The value for the refreshConfiguration field */
    private Integer refreshConfiguration = new Integer(0);

    /** The value for the firstTime field */
    private Integer firstTime = new Integer(0);

    /** The value for the moreProps field */
    private String moreProps;


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
     * Get the LoggedFullUsers
     *
     * @return Integer
     */
    public Integer getLoggedFullUsers ()
    {
        return loggedFullUsers;
    }

    /**
     * Set the value of LoggedFullUsers
     *
     * @param v new value
     */
    public void setLoggedFullUsers(Integer v)
    {

        this.loggedFullUsers = v;
        setModified(true);

    }

    /**
     * Get the LoggedLimitedUsers
     *
     * @return Integer
     */
    public Integer getLoggedLimitedUsers ()
    {
        return loggedLimitedUsers;
    }

    /**
     * Set the value of LoggedLimitedUsers
     *
     * @param v new value
     */
    public void setLoggedLimitedUsers(Integer v)
    {

        this.loggedLimitedUsers = v;
        setModified(true);

    }

    /**
     * Get the RefreshConfiguration
     *
     * @return Integer
     */
    public Integer getRefreshConfiguration ()
    {
        return refreshConfiguration;
    }

    /**
     * Set the value of RefreshConfiguration
     *
     * @param v new value
     */
    public void setRefreshConfiguration(Integer v)
    {

        this.refreshConfiguration = v;
        setModified(true);

    }

    /**
     * Get the FirstTime
     *
     * @return Integer
     */
    public Integer getFirstTime ()
    {
        return firstTime;
    }

    /**
     * Set the value of FirstTime
     *
     * @param v new value
     */
    public void setFirstTime(Integer v)
    {

        this.firstTime = v;
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

    

}
