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
 * This table holds the changed entities for updating the full text search index in each clusternode
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TEntityChangesBean
 */
public abstract class BaseTEntityChangesBean
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

    /** The value for the entityKey field */
    private Integer entityKey;

    /** The value for the entityType field */
    private Integer entityType;

    /** The value for the clusterNode field */
    private Integer clusterNode;

    /** The value for the list field */
    private Integer list;

    /** The value for the changeType field */
    private Integer changeType;


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
     * Get the EntityKey
     *
     * @return Integer
     */
    public Integer getEntityKey ()
    {
        return entityKey;
    }

    /**
     * Set the value of EntityKey
     *
     * @param v new value
     */
    public void setEntityKey(Integer v)
    {

        this.entityKey = v;
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
     * Get the ClusterNode
     *
     * @return Integer
     */
    public Integer getClusterNode ()
    {
        return clusterNode;
    }

    /**
     * Set the value of ClusterNode
     *
     * @param v new value
     */
    public void setClusterNode(Integer v)
    {

        this.clusterNode = v;
        setModified(true);

    }

    /**
     * Get the List
     *
     * @return Integer
     */
    public Integer getList ()
    {
        return list;
    }

    /**
     * Set the value of List
     *
     * @param v new value
     */
    public void setList(Integer v)
    {

        this.list = v;
        setModified(true);

    }

    /**
     * Get the ChangeType
     *
     * @return Integer
     */
    public Integer getChangeType ()
    {
        return changeType;
    }

    /**
     * Set the value of ChangeType
     *
     * @param v new value
     */
    public void setChangeType(Integer v)
    {

        this.changeType = v;
        setModified(true);

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
            setClusterNode((Integer) null);
        }
        else
        {
            setClusterNode(v.getObjectID());
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
