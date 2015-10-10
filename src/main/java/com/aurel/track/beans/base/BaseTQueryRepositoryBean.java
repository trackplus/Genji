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
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TQueryRepositoryBean
 */
public abstract class BaseTQueryRepositoryBean
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

    /** The value for the project field */
    private Integer project;

    /** The value for the label field */
    private String label;

    /** The value for the queryType field */
    private Integer queryType;

    /** The value for the repositoryType field */
    private Integer repositoryType;

    /** The value for the queryKey field */
    private Integer queryKey;

    /** The value for the menuItem field */
    private String menuItem = "N";

    /** The value for the categoryKey field */
    private Integer categoryKey;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the viewID field */
    private String viewID;

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
     * Get the MenuItem
     *
     * @return String
     */
    public String getMenuItem ()
    {
        return menuItem;
    }

    /**
     * Set the value of MenuItem
     *
     * @param v new value
     */
    public void setMenuItem(String v)
    {

        this.menuItem = v;
        setModified(true);

    }

    /**
     * Get the CategoryKey
     *
     * @return Integer
     */
    public Integer getCategoryKey ()
    {
        return categoryKey;
    }

    /**
     * Set the value of CategoryKey
     *
     * @param v new value
     */
    public void setCategoryKey(Integer v)
    {

        this.categoryKey = v;
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
     * Get the ViewID
     *
     * @return String
     */
    public String getViewID ()
    {
        return viewID;
    }

    /**
     * Set the value of ViewID
     *
     * @param v new value
     */
    public void setViewID(String v)
    {

        this.viewID = v;
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
            setQueryKey((Integer) null);
        }
        else
        {
            setQueryKey(v.getObjectID());
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





    private TFilterCategoryBean aTFilterCategoryBean;

    /**
     * sets an associated TFilterCategoryBean object
     *
     * @param v TFilterCategoryBean
     */
    public void setTFilterCategoryBean(TFilterCategoryBean v)
    {
        if (v == null)
        {
            setCategoryKey((Integer) null);
        }
        else
        {
            setCategoryKey(v.getObjectID());
        }
        aTFilterCategoryBean = v;
    }


    /**
     * Get the associated TFilterCategoryBean object
     *
     * @return the associated TFilterCategoryBean object
     */
    public TFilterCategoryBean getTFilterCategoryBean()
    {
        return aTFilterCategoryBean;
    }





    /**
     * Collection to store aggregation of collTNotifySettingsBeans
     */
    protected List<TNotifySettingsBean> collTNotifySettingsBeans;

    /**
     * Returns the collection of TNotifySettingsBeans
     */
    public List<TNotifySettingsBean> getTNotifySettingsBeans()
    {
        return collTNotifySettingsBeans;
    }

    /**
     * Sets the collection of TNotifySettingsBeans to the specified value
     */
    public void setTNotifySettingsBeans(List<TNotifySettingsBean> list)
    {
        if (list == null)
        {
            collTNotifySettingsBeans = null;
        }
        else
        {
            collTNotifySettingsBeans = new ArrayList<TNotifySettingsBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTMenuitemQueryBeans
     */
    protected List<TMenuitemQueryBean> collTMenuitemQueryBeans;

    /**
     * Returns the collection of TMenuitemQueryBeans
     */
    public List<TMenuitemQueryBean> getTMenuitemQueryBeans()
    {
        return collTMenuitemQueryBeans;
    }

    /**
     * Sets the collection of TMenuitemQueryBeans to the specified value
     */
    public void setTMenuitemQueryBeans(List<TMenuitemQueryBean> list)
    {
        if (list == null)
        {
            collTMenuitemQueryBeans = null;
        }
        else
        {
            collTMenuitemQueryBeans = new ArrayList<TMenuitemQueryBean>(list);
        }
    }

}
