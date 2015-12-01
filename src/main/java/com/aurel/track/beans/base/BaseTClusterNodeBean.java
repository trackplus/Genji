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
 * This table holds properties for each node in the cluster
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TClusterNodeBean
 */
public abstract class BaseTClusterNodeBean
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
    private String nodeAddress;

    /** The value for the nodeURL field */
    private String nodeURL;

    /** The value for the lastUpdate field */
    private Date lastUpdate;

    /** The value for the masterNode field */
    private Integer masterNode = new Integer(0);

    /** The value for the reloadConfig field */
    private Integer reloadConfig = new Integer(0);

    /** The value for the reloadChanges field */
    private String reloadChanges;


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
     * @return String
     */
    public String getNodeAddress ()
    {
        return nodeAddress;
    }

    /**
     * Set the value of NodeAddress
     *
     * @param v new value
     */
    public void setNodeAddress(String v)
    {

        this.nodeAddress = v;
        setModified(true);

    }

    /**
     * Get the NodeURL
     *
     * @return String
     */
    public String getNodeURL ()
    {
        return nodeURL;
    }

    /**
     * Set the value of NodeURL
     *
     * @param v new value
     */
    public void setNodeURL(String v)
    {

        this.nodeURL = v;
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
     * Get the MasterNode
     *
     * @return Integer
     */
    public Integer getMasterNode ()
    {
        return masterNode;
    }

    /**
     * Set the value of MasterNode
     *
     * @param v new value
     */
    public void setMasterNode(Integer v)
    {

        this.masterNode = v;
        setModified(true);

    }

    /**
     * Get the ReloadConfig
     *
     * @return Integer
     */
    public Integer getReloadConfig ()
    {
        return reloadConfig;
    }

    /**
     * Set the value of ReloadConfig
     *
     * @param v new value
     */
    public void setReloadConfig(Integer v)
    {

        this.reloadConfig = v;
        setModified(true);

    }

    /**
     * Get the ReloadChanges
     *
     * @return String
     */
    public String getReloadChanges ()
    {
        return reloadChanges;
    }

    /**
     * Set the value of ReloadChanges
     *
     * @param v new value
     */
    public void setReloadChanges(String v)
    {

        this.reloadChanges = v;
        setModified(true);

    }

    



    /**
     * Collection to store aggregation of collTLoggedInUsersBeans
     */
    protected List<TLoggedInUsersBean> collTLoggedInUsersBeans;

    /**
     * Returns the collection of TLoggedInUsersBeans
     */
    public List<TLoggedInUsersBean> getTLoggedInUsersBeans()
    {
        return collTLoggedInUsersBeans;
    }

    /**
     * Sets the collection of TLoggedInUsersBeans to the specified value
     */
    public void setTLoggedInUsersBeans(List<TLoggedInUsersBean> list)
    {
        if (list == null)
        {
            collTLoggedInUsersBeans = null;
        }
        else
        {
            collTLoggedInUsersBeans = new ArrayList<TLoggedInUsersBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTEntityChangesBeans
     */
    protected List<TEntityChangesBean> collTEntityChangesBeans;

    /**
     * Returns the collection of TEntityChangesBeans
     */
    public List<TEntityChangesBean> getTEntityChangesBeans()
    {
        return collTEntityChangesBeans;
    }

    /**
     * Sets the collection of TEntityChangesBeans to the specified value
     */
    public void setTEntityChangesBeans(List<TEntityChangesBean> list)
    {
        if (list == null)
        {
            collTEntityChangesBeans = null;
        }
        else
        {
            collTEntityChangesBeans = new ArrayList<TEntityChangesBean>(list);
        }
    }

}
