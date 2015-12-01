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
 * extended; all references should be to TListBean
 */
public abstract class BaseTListBean
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

    /** The value for the parentList field */
    private Integer parentList;

    /** The value for the listType field */
    private Integer listType;

    /** The value for the childNumber field */
    private Integer childNumber;

    /** The value for the deleted field */
    private String deleted = "N";

    /** The value for the repositoryType field */
    private Integer repositoryType;

    /** The value for the project field */
    private Integer project;

    /** The value for the owner field */
    private Integer owner;

    /** The value for the moreProps field */
    private String moreProps;

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
     * Get the ParentList
     *
     * @return Integer
     */
    public Integer getParentList ()
    {
        return parentList;
    }

    /**
     * Set the value of ParentList
     *
     * @param v new value
     */
    public void setParentList(Integer v)
    {

        this.parentList = v;
        setModified(true);

    }

    /**
     * Get the ListType
     *
     * @return Integer
     */
    public Integer getListType ()
    {
        return listType;
    }

    /**
     * Set the value of ListType
     *
     * @param v new value
     */
    public void setListType(Integer v)
    {

        this.listType = v;
        setModified(true);

    }

    /**
     * Get the ChildNumber
     *
     * @return Integer
     */
    public Integer getChildNumber ()
    {
        return childNumber;
    }

    /**
     * Set the value of ChildNumber
     *
     * @param v new value
     */
    public void setChildNumber(Integer v)
    {

        this.childNumber = v;
        setModified(true);

    }

    /**
     * Get the Deleted
     *
     * @return String
     */
    public String getDeleted ()
    {
        return deleted;
    }

    /**
     * Set the value of Deleted
     *
     * @param v new value
     */
    public void setDeleted(String v)
    {

        this.deleted = v;
        setModified(true);

    }

    /**
     * Get the RepositoryType
     *
     * @return Integer
     */
    public Integer getRepositoryType ()
    {
        return repositoryType;
    }

    /**
     * Set the value of RepositoryType
     *
     * @param v new value
     */
    public void setRepositoryType(Integer v)
    {

        this.repositoryType = v;
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

    



    private TListBean aTListBeanRelatedByParentList;

    /**
     * sets an associated TListBean object
     *
     * @param v TListBean
     */
    public void setTListBeanRelatedByParentList(TListBean v)
    {
        if (v == null)
        {
            setParentList((Integer) null);
        }
        else
        {
            setParentList(v.getObjectID());
        }
        aTListBeanRelatedByParentList = v;
    }


    /**
     * Get the associated TListBean object
     *
     * @return the associated TListBean object
     */
    public TListBean getTListBeanRelatedByParentList()
    {
        return aTListBeanRelatedByParentList;
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
     * Collection to store aggregation of collTOptionBeans
     */
    protected List<TOptionBean> collTOptionBeans;

    /**
     * Returns the collection of TOptionBeans
     */
    public List<TOptionBean> getTOptionBeans()
    {
        return collTOptionBeans;
    }

    /**
     * Sets the collection of TOptionBeans to the specified value
     */
    public void setTOptionBeans(List<TOptionBean> list)
    {
        if (list == null)
        {
            collTOptionBeans = null;
        }
        else
        {
            collTOptionBeans = new ArrayList<TOptionBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTOptionSettingsBeans
     */
    protected List<TOptionSettingsBean> collTOptionSettingsBeans;

    /**
     * Returns the collection of TOptionSettingsBeans
     */
    public List<TOptionSettingsBean> getTOptionSettingsBeans()
    {
        return collTOptionSettingsBeans;
    }

    /**
     * Sets the collection of TOptionSettingsBeans to the specified value
     */
    public void setTOptionSettingsBeans(List<TOptionSettingsBean> list)
    {
        if (list == null)
        {
            collTOptionSettingsBeans = null;
        }
        else
        {
            collTOptionSettingsBeans = new ArrayList<TOptionSettingsBean>(list);
        }
    }

}
