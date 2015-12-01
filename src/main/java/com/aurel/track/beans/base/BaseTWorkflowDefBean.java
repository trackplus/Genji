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
 * extended; all references should be to TWorkflowDefBean
 */
public abstract class BaseTWorkflowDefBean
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

    /** The value for the name field */
    private String name;

    /** The value for the description field */
    private String description;

    /** The value for the tagLabel field */
    private String tagLabel;

    /** The value for the owner field */
    private Integer owner;

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
     * Get the Name
     *
     * @return String
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Set the value of Name
     *
     * @param v new value
     */
    public void setName(String v)
    {

        this.name = v;
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
     * Get the TagLabel
     *
     * @return String
     */
    public String getTagLabel ()
    {
        return tagLabel;
    }

    /**
     * Set the value of TagLabel
     *
     * @param v new value
     */
    public void setTagLabel(String v)
    {

        this.tagLabel = v;
        setModified(true);

    }

    /**
     * Get the Owner
     *
     * @return Integer
     */
    public Integer getOwner ()
    {
        return owner;
    }

    /**
     * Set the value of Owner
     *
     * @param v new value
     */
    public void setOwner(Integer v)
    {

        this.owner = v;
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
            setOwner((Integer) null);
        }
        else
        {
            setOwner(v.getObjectID());
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





    /**
     * Collection to store aggregation of collTWorkflowTransitionBeans
     */
    protected List<TWorkflowTransitionBean> collTWorkflowTransitionBeans;

    /**
     * Returns the collection of TWorkflowTransitionBeans
     */
    public List<TWorkflowTransitionBean> getTWorkflowTransitionBeans()
    {
        return collTWorkflowTransitionBeans;
    }

    /**
     * Sets the collection of TWorkflowTransitionBeans to the specified value
     */
    public void setTWorkflowTransitionBeans(List<TWorkflowTransitionBean> list)
    {
        if (list == null)
        {
            collTWorkflowTransitionBeans = null;
        }
        else
        {
            collTWorkflowTransitionBeans = new ArrayList<TWorkflowTransitionBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowStationBeans
     */
    protected List<TWorkflowStationBean> collTWorkflowStationBeans;

    /**
     * Returns the collection of TWorkflowStationBeans
     */
    public List<TWorkflowStationBean> getTWorkflowStationBeans()
    {
        return collTWorkflowStationBeans;
    }

    /**
     * Sets the collection of TWorkflowStationBeans to the specified value
     */
    public void setTWorkflowStationBeans(List<TWorkflowStationBean> list)
    {
        if (list == null)
        {
            collTWorkflowStationBeans = null;
        }
        else
        {
            collTWorkflowStationBeans = new ArrayList<TWorkflowStationBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowConnectBeans
     */
    protected List<TWorkflowConnectBean> collTWorkflowConnectBeans;

    /**
     * Returns the collection of TWorkflowConnectBeans
     */
    public List<TWorkflowConnectBean> getTWorkflowConnectBeans()
    {
        return collTWorkflowConnectBeans;
    }

    /**
     * Sets the collection of TWorkflowConnectBeans to the specified value
     */
    public void setTWorkflowConnectBeans(List<TWorkflowConnectBean> list)
    {
        if (list == null)
        {
            collTWorkflowConnectBeans = null;
        }
        else
        {
            collTWorkflowConnectBeans = new ArrayList<TWorkflowConnectBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowCommentBeans
     */
    protected List<TWorkflowCommentBean> collTWorkflowCommentBeans;

    /**
     * Returns the collection of TWorkflowCommentBeans
     */
    public List<TWorkflowCommentBean> getTWorkflowCommentBeans()
    {
        return collTWorkflowCommentBeans;
    }

    /**
     * Sets the collection of TWorkflowCommentBeans to the specified value
     */
    public void setTWorkflowCommentBeans(List<TWorkflowCommentBean> list)
    {
        if (list == null)
        {
            collTWorkflowCommentBeans = null;
        }
        else
        {
            collTWorkflowCommentBeans = new ArrayList<TWorkflowCommentBean>(list);
        }
    }

}
