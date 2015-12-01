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
 * extended; all references should be to TLoggedInUsersBean
 */
public abstract class BaseTLoggedInUsersBean
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

    /** The value for the nodeAddress field */
    private Integer nodeAddress;

    /** The value for the sessionId field */
    private String sessionId;

    /** The value for the loggedUser field */
    private Integer loggedUser;

    /** The value for the userLevel field */
    private Integer userLevel;

    /** The value for the lastUpdate field */
    private Date lastUpdate;

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
     * Get the NodeAddress
     *
     * @return Integer
     */
    public Integer getNodeAddress ()
    {
        return nodeAddress;
    }

    /**
     * Set the value of NodeAddress
     *
     * @param v new value
     */
    public void setNodeAddress(Integer v)
    {

        this.nodeAddress = v;
        setModified(true);

    }

    /**
     * Get the SessionId
     *
     * @return String
     */
    public String getSessionId ()
    {
        return sessionId;
    }

    /**
     * Set the value of SessionId
     *
     * @param v new value
     */
    public void setSessionId(String v)
    {

        this.sessionId = v;
        setModified(true);

    }

    /**
     * Get the LoggedUser
     *
     * @return Integer
     */
    public Integer getLoggedUser ()
    {
        return loggedUser;
    }

    /**
     * Set the value of LoggedUser
     *
     * @param v new value
     */
    public void setLoggedUser(Integer v)
    {

        this.loggedUser = v;
        setModified(true);

    }

    /**
     * Get the UserLevel
     *
     * @return Integer
     */
    public Integer getUserLevel ()
    {
        return userLevel;
    }

    /**
     * Set the value of UserLevel
     *
     * @param v new value
     */
    public void setUserLevel(Integer v)
    {

        this.userLevel = v;
        setModified(true);

    }

    /**
     * Get the LastUpdate
     *
     * @return Date
     */
    public Date getLastUpdate ()
    {
        return lastUpdate;
    }

    /**
     * Set the value of LastUpdate
     *
     * @param v new value
     */
    public void setLastUpdate(Date v)
    {

        this.lastUpdate = v;
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
            setLoggedUser((Integer) null);
        }
        else
        {
            setLoggedUser(v.getObjectID());
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





    private TClusterNodeBean aTClusterNodeBean;

    /**
     * sets an associated TClusterNodeBean object
     *
     * @param v TClusterNodeBean
     */
    public void setTClusterNodeBean(TClusterNodeBean v)
    {
        if (v == null)
        {
            setNodeAddress((Integer) null);
        }
        else
        {
            setNodeAddress(v.getObjectID());
        }
        aTClusterNodeBean = v;
    }


    /**
     * Get the associated TClusterNodeBean object
     *
     * @return the associated TClusterNodeBean object
     */
    public TClusterNodeBean getTClusterNodeBean()
    {
        return aTClusterNodeBean;
    }



}
