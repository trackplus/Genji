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
 * extended; all references should be to TReleaseBean
 */
public abstract class BaseTReleaseBean
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

    /** The value for the label field */
    private String label;

    /** The value for the projectID field */
    private Integer projectID;

    /** The value for the status field */
    private Integer status;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the moreProps field */
    private String moreProps;

    /** The value for the description field */
    private String description;

    /** The value for the dueDate field */
    private Date dueDate;

    /** The value for the parent field */
    private Integer parent;

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
     * Get the Label
     *
     * @return String
     */
    public String getLabel ()
    {
        return label;
    }

    /**
     * Set the value of Label
     *
     * @param v new value
     */
    public void setLabel(String v)
    {

        this.label = v;
        setModified(true);

    }

    /**
     * Get the ProjectID
     *
     * @return Integer
     */
    public Integer getProjectID ()
    {
        return projectID;
    }

    /**
     * Set the value of ProjectID
     *
     * @param v new value
     */
    public void setProjectID(Integer v)
    {

        this.projectID = v;
        setModified(true);

    }

    /**
     * Get the Status
     *
     * @return Integer
     */
    public Integer getStatus ()
    {
        return status;
    }

    /**
     * Set the value of Status
     *
     * @param v new value
     */
    public void setStatus(Integer v)
    {

        this.status = v;
        setModified(true);

    }

    /**
     * Get the Sortorder
     *
     * @return Integer
     */
    public Integer getSortorder ()
    {
        return sortorder;
    }

    /**
     * Set the value of Sortorder
     *
     * @param v new value
     */
    public void setSortorder(Integer v)
    {

        this.sortorder = v;
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
     * Get the DueDate
     *
     * @return Date
     */
    public Date getDueDate ()
    {
        return dueDate;
    }

    /**
     * Set the value of DueDate
     *
     * @param v new value
     */
    public void setDueDate(Date v)
    {

        this.dueDate = v;
        setModified(true);

    }

    /**
     * Get the Parent
     *
     * @return Integer
     */
    public Integer getParent ()
    {
        return parent;
    }

    /**
     * Set the value of Parent
     *
     * @param v new value
     */
    public void setParent(Integer v)
    {

        this.parent = v;
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

    



    private TProjectBean aTProjectBean;

    /**
     * sets an associated TProjectBean object
     *
     * @param v TProjectBean
     */
    public void setTProjectBean(TProjectBean v)
    {
        if (v == null)
        {
            setProjectID((Integer) null);
        }
        else
        {
            setProjectID(v.getObjectID());
        }
        aTProjectBean = v;
    }


    /**
     * Get the associated TProjectBean object
     *
     * @return the associated TProjectBean object
     */
    public TProjectBean getTProjectBean()
    {
        return aTProjectBean;
    }





    private TSystemStateBean aTSystemStateBean;

    /**
     * sets an associated TSystemStateBean object
     *
     * @param v TSystemStateBean
     */
    public void setTSystemStateBean(TSystemStateBean v)
    {
        if (v == null)
        {
            setStatus((Integer) null);
        }
        else
        {
            setStatus(v.getObjectID());
        }
        aTSystemStateBean = v;
    }


    /**
     * Get the associated TSystemStateBean object
     *
     * @return the associated TSystemStateBean object
     */
    public TSystemStateBean getTSystemStateBean()
    {
        return aTSystemStateBean;
    }





    private TReleaseBean aTReleaseBeanRelatedByParent;

    /**
     * sets an associated TReleaseBean object
     *
     * @param v TReleaseBean
     */
    public void setTReleaseBeanRelatedByParent(TReleaseBean v)
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTReleaseBeanRelatedByParent = v;
    }


    /**
     * Get the associated TReleaseBean object
     *
     * @return the associated TReleaseBean object
     */
    public TReleaseBean getTReleaseBeanRelatedByParent()
    {
        return aTReleaseBeanRelatedByParent;
    }





    /**
     * Collection to store aggregation of collTWorkItemBeansRelatedByReleaseNoticedID
     */
    protected List<TWorkItemBean> collTWorkItemBeansRelatedByReleaseNoticedID;

    /**
     * Returns the collection of TWorkItemBeansRelatedByReleaseNoticedID
     */
    public List<TWorkItemBean> getTWorkItemBeansRelatedByReleaseNoticedID()
    {
        return collTWorkItemBeansRelatedByReleaseNoticedID;
    }

    /**
     * Sets the collection of TWorkItemBeansRelatedByReleaseNoticedID to the specified value
     */
    public void setTWorkItemBeansRelatedByReleaseNoticedID(List<TWorkItemBean> list)
    {
        if (list == null)
        {
            collTWorkItemBeansRelatedByReleaseNoticedID = null;
        }
        else
        {
            collTWorkItemBeansRelatedByReleaseNoticedID = new ArrayList<TWorkItemBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkItemBeansRelatedByReleaseScheduledID
     */
    protected List<TWorkItemBean> collTWorkItemBeansRelatedByReleaseScheduledID;

    /**
     * Returns the collection of TWorkItemBeansRelatedByReleaseScheduledID
     */
    public List<TWorkItemBean> getTWorkItemBeansRelatedByReleaseScheduledID()
    {
        return collTWorkItemBeansRelatedByReleaseScheduledID;
    }

    /**
     * Sets the collection of TWorkItemBeansRelatedByReleaseScheduledID to the specified value
     */
    public void setTWorkItemBeansRelatedByReleaseScheduledID(List<TWorkItemBean> list)
    {
        if (list == null)
        {
            collTWorkItemBeansRelatedByReleaseScheduledID = null;
        }
        else
        {
            collTWorkItemBeansRelatedByReleaseScheduledID = new ArrayList<TWorkItemBean>(list);
        }
    }

}
