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
 * Hierarchical categorization of the queries
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TFilterCategoryBean
 */
public abstract class BaseTFilterCategoryBean
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

    /** The value for the repository field */
    private Integer repository;

    /** The value for the filterType field */
    private Integer filterType;

    /** The value for the createdBy field */
    private Integer createdBy;

    /** The value for the project field */
    private Integer project;

    /** The value for the parentID field */
    private Integer parentID;

    /** The value for the sortorder field */
    private Integer sortorder;

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
     * Get the Repository
     *
     * @return Integer
     */
    public Integer getRepository ()
    {
        return repository;
    }

    /**
     * Set the value of Repository
     *
     * @param v new value
     */
    public void setRepository(Integer v)
    {

        this.repository = v;
        setModified(true);

    }

    /**
     * Get the FilterType
     *
     * @return Integer
     */
    public Integer getFilterType ()
    {
        return filterType;
    }

    /**
     * Set the value of FilterType
     *
     * @param v new value
     */
    public void setFilterType(Integer v)
    {

        this.filterType = v;
        setModified(true);

    }

    /**
     * Get the CreatedBy
     *
     * @return Integer
     */
    public Integer getCreatedBy ()
    {
        return createdBy;
    }

    /**
     * Set the value of CreatedBy
     *
     * @param v new value
     */
    public void setCreatedBy(Integer v)
    {

        this.createdBy = v;
        setModified(true);

    }

    /**
     * Get the Project
     *
     * @return Integer
     */
    public Integer getProject ()
    {
        return project;
    }

    /**
     * Set the value of Project
     *
     * @param v new value
     */
    public void setProject(Integer v)
    {

        this.project = v;
        setModified(true);

    }

    /**
     * Get the ParentID
     *
     * @return Integer
     */
    public Integer getParentID ()
    {
        return parentID;
    }

    /**
     * Set the value of ParentID
     *
     * @param v new value
     */
    public void setParentID(Integer v)
    {

        this.parentID = v;
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
            setProject((Integer) null);
        }
        else
        {
            setProject(v.getObjectID());
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
            setCreatedBy((Integer) null);
        }
        else
        {
            setCreatedBy(v.getObjectID());
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





    private TFilterCategoryBean aTFilterCategoryBeanRelatedByParentID;

    /**
     * sets an associated TFilterCategoryBean object
     *
     * @param v TFilterCategoryBean
     */
    public void setTFilterCategoryBeanRelatedByParentID(TFilterCategoryBean v)
    {
        if (v == null)
        {
            setParentID((Integer) null);
        }
        else
        {
            setParentID(v.getObjectID());
        }
        aTFilterCategoryBeanRelatedByParentID = v;
    }


    /**
     * Get the associated TFilterCategoryBean object
     *
     * @return the associated TFilterCategoryBean object
     */
    public TFilterCategoryBean getTFilterCategoryBeanRelatedByParentID()
    {
        return aTFilterCategoryBeanRelatedByParentID;
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

}
