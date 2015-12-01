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
 * extended; all references should be to TExportTemplateBean
 */
public abstract class BaseTExportTemplateBean
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

    /** The value for the reportType field */
    private String reportType;

    /** The value for the exportFormat field */
    private String exportFormat;

    /** The value for the repositoryType field */
    private Integer repositoryType;

    /** The value for the description field */
    private String description;

    /** The value for the project field */
    private Integer project;

    /** The value for the person field */
    private Integer person;

    /** The value for the categoryKey field */
    private Integer categoryKey;

    /** The value for the parent field */
    private Integer parent;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the deleted field */
    private String deleted = "N";

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
     * Get the ReportType
     *
     * @return String
     */
    public String getReportType ()
    {
        return reportType;
    }

    /**
     * Set the value of ReportType
     *
     * @param v new value
     */
    public void setReportType(String v)
    {

        this.reportType = v;
        setModified(true);

    }

    /**
     * Get the ExportFormat
     *
     * @return String
     */
    public String getExportFormat ()
    {
        return exportFormat;
    }

    /**
     * Set the value of ExportFormat
     *
     * @param v new value
     */
    public void setExportFormat(String v)
    {

        this.exportFormat = v;
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





    private TReportCategoryBean aTReportCategoryBean;

    /**
     * sets an associated TReportCategoryBean object
     *
     * @param v TReportCategoryBean
     */
    public void setTReportCategoryBean(TReportCategoryBean v)
    {
        if (v == null)
        {
            setCategoryKey((Integer) null);
        }
        else
        {
            setCategoryKey(v.getObjectID());
        }
        aTReportCategoryBean = v;
    }


    /**
     * Get the associated TReportCategoryBean object
     *
     * @return the associated TReportCategoryBean object
     */
    public TReportCategoryBean getTReportCategoryBean()
    {
        return aTReportCategoryBean;
    }





    private TExportTemplateBean aTExportTemplateBeanRelatedByParent;

    /**
     * sets an associated TExportTemplateBean object
     *
     * @param v TExportTemplateBean
     */
    public void setTExportTemplateBeanRelatedByParent(TExportTemplateBean v)
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTExportTemplateBeanRelatedByParent = v;
    }


    /**
     * Get the associated TExportTemplateBean object
     *
     * @return the associated TExportTemplateBean object
     */
    public TExportTemplateBean getTExportTemplateBeanRelatedByParent()
    {
        return aTExportTemplateBeanRelatedByParent;
    }





    /**
     * Collection to store aggregation of collTTemplatePersonBeans
     */
    protected List<TTemplatePersonBean> collTTemplatePersonBeans;

    /**
     * Returns the collection of TTemplatePersonBeans
     */
    public List<TTemplatePersonBean> getTTemplatePersonBeans()
    {
        return collTTemplatePersonBeans;
    }

    /**
     * Sets the collection of TTemplatePersonBeans to the specified value
     */
    public void setTTemplatePersonBeans(List<TTemplatePersonBean> list)
    {
        if (list == null)
        {
            collTTemplatePersonBeans = null;
        }
        else
        {
            collTTemplatePersonBeans = new ArrayList<TTemplatePersonBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTReportPersonSettingsBeans
     */
    protected List<TReportPersonSettingsBean> collTReportPersonSettingsBeans;

    /**
     * Returns the collection of TReportPersonSettingsBeans
     */
    public List<TReportPersonSettingsBean> getTReportPersonSettingsBeans()
    {
        return collTReportPersonSettingsBeans;
    }

    /**
     * Sets the collection of TReportPersonSettingsBeans to the specified value
     */
    public void setTReportPersonSettingsBeans(List<TReportPersonSettingsBean> list)
    {
        if (list == null)
        {
            collTReportPersonSettingsBeans = null;
        }
        else
        {
            collTReportPersonSettingsBeans = new ArrayList<TReportPersonSettingsBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTReportSubscribeBeans
     */
    protected List<TReportSubscribeBean> collTReportSubscribeBeans;

    /**
     * Returns the collection of TReportSubscribeBeans
     */
    public List<TReportSubscribeBean> getTReportSubscribeBeans()
    {
        return collTReportSubscribeBeans;
    }

    /**
     * Sets the collection of TReportSubscribeBeans to the specified value
     */
    public void setTReportSubscribeBeans(List<TReportSubscribeBean> list)
    {
        if (list == null)
        {
            collTReportSubscribeBeans = null;
        }
        else
        {
            collTReportSubscribeBeans = new ArrayList<TReportSubscribeBean>(list);
        }
    }

}
