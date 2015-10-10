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
 * extended; all references should be to TFieldConfigBean
 */
public abstract class BaseTFieldConfigBean
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

    /** The value for the field field */
    private Integer field;

    /** The value for the label field */
    private String label;

    /** The value for the tooltip field */
    private String tooltip;

    /** The value for the required field */
    private String required = "N";

    /** The value for the history field */
    private String history = "N";

    /** The value for the issueType field */
    private Integer issueType;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

    /** The value for the description field */
    private String description;

    /** The value for the groovyScript field */
    private Integer groovyScript;

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
     * Get the Field
     *
     * @return Integer
     */
    public Integer getField ()
    {
        return field;
    }

    /**
     * Set the value of Field
     *
     * @param v new value
     */
    public void setField(Integer v)
    {

        this.field = v;
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
     * Get the Tooltip
     *
     * @return String
     */
    public String getTooltip ()
    {
        return tooltip;
    }

    /**
     * Set the value of Tooltip
     *
     * @param v new value
     */
    public void setTooltip(String v)
    {

        this.tooltip = v;
        setModified(true);

    }

    /**
     * Get the Required
     *
     * @return String
     */
    public String getRequired ()
    {
        return required;
    }

    /**
     * Set the value of Required
     *
     * @param v new value
     */
    public void setRequired(String v)
    {

        this.required = v;
        setModified(true);

    }

    /**
     * Get the History
     *
     * @return String
     */
    public String getHistory ()
    {
        return history;
    }

    /**
     * Set the value of History
     *
     * @param v new value
     */
    public void setHistory(String v)
    {

        this.history = v;
        setModified(true);

    }

    /**
     * Get the IssueType
     *
     * @return Integer
     */
    public Integer getIssueType ()
    {
        return issueType;
    }

    /**
     * Set the value of IssueType
     *
     * @param v new value
     */
    public void setIssueType(Integer v)
    {

        this.issueType = v;
        setModified(true);

    }

    /**
     * Get the ProjectType
     *
     * @return Integer
     */
    public Integer getProjectType ()
    {
        return projectType;
    }

    /**
     * Set the value of ProjectType
     *
     * @param v new value
     */
    public void setProjectType(Integer v)
    {

        this.projectType = v;
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
     * Get the GroovyScript
     *
     * @return Integer
     */
    public Integer getGroovyScript ()
    {
        return groovyScript;
    }

    /**
     * Set the value of GroovyScript
     *
     * @param v new value
     */
    public void setGroovyScript(Integer v)
    {

        this.groovyScript = v;
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

    



    private TFieldBean aTFieldBean;

    /**
     * sets an associated TFieldBean object
     *
     * @param v TFieldBean
     */
    public void setTFieldBean(TFieldBean v)
    {
        if (v == null)
        {
            setField((Integer) null);
        }
        else
        {
            setField(v.getObjectID());
        }
        aTFieldBean = v;
    }


    /**
     * Get the associated TFieldBean object
     *
     * @return the associated TFieldBean object
     */
    public TFieldBean getTFieldBean()
    {
        return aTFieldBean;
    }





    private TListTypeBean aTListTypeBean;

    /**
     * sets an associated TListTypeBean object
     *
     * @param v TListTypeBean
     */
    public void setTListTypeBean(TListTypeBean v)
    {
        if (v == null)
        {
            setIssueType((Integer) null);
        }
        else
        {
            setIssueType(v.getObjectID());
        }
        aTListTypeBean = v;
    }


    /**
     * Get the associated TListTypeBean object
     *
     * @return the associated TListTypeBean object
     */
    public TListTypeBean getTListTypeBean()
    {
        return aTListTypeBean;
    }





    private TProjectTypeBean aTProjectTypeBean;

    /**
     * sets an associated TProjectTypeBean object
     *
     * @param v TProjectTypeBean
     */
    public void setTProjectTypeBean(TProjectTypeBean v)
    {
        if (v == null)
        {
            setProjectType((Integer) null);
        }
        else
        {
            setProjectType(v.getObjectID());
        }
        aTProjectTypeBean = v;
    }


    /**
     * Get the associated TProjectTypeBean object
     *
     * @return the associated TProjectTypeBean object
     */
    public TProjectTypeBean getTProjectTypeBean()
    {
        return aTProjectTypeBean;
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





    private TScriptsBean aTScriptsBean;

    /**
     * sets an associated TScriptsBean object
     *
     * @param v TScriptsBean
     */
    public void setTScriptsBean(TScriptsBean v)
    {
        if (v == null)
        {
            setGroovyScript((Integer) null);
        }
        else
        {
            setGroovyScript(v.getObjectID());
        }
        aTScriptsBean = v;
    }


    /**
     * Get the associated TScriptsBean object
     *
     * @return the associated TScriptsBean object
     */
    public TScriptsBean getTScriptsBean()
    {
        return aTScriptsBean;
    }





    /**
     * Collection to store aggregation of collTConfigOptionsRoleBeans
     */
    protected List<TConfigOptionsRoleBean> collTConfigOptionsRoleBeans;

    /**
     * Returns the collection of TConfigOptionsRoleBeans
     */
    public List<TConfigOptionsRoleBean> getTConfigOptionsRoleBeans()
    {
        return collTConfigOptionsRoleBeans;
    }

    /**
     * Sets the collection of TConfigOptionsRoleBeans to the specified value
     */
    public void setTConfigOptionsRoleBeans(List<TConfigOptionsRoleBean> list)
    {
        if (list == null)
        {
            collTConfigOptionsRoleBeans = null;
        }
        else
        {
            collTConfigOptionsRoleBeans = new ArrayList<TConfigOptionsRoleBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTTextBoxSettingsBeans
     */
    protected List<TTextBoxSettingsBean> collTTextBoxSettingsBeans;

    /**
     * Returns the collection of TTextBoxSettingsBeans
     */
    public List<TTextBoxSettingsBean> getTTextBoxSettingsBeans()
    {
        return collTTextBoxSettingsBeans;
    }

    /**
     * Sets the collection of TTextBoxSettingsBeans to the specified value
     */
    public void setTTextBoxSettingsBeans(List<TTextBoxSettingsBean> list)
    {
        if (list == null)
        {
            collTTextBoxSettingsBeans = null;
        }
        else
        {
            collTTextBoxSettingsBeans = new ArrayList<TTextBoxSettingsBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTGeneralSettingsBeans
     */
    protected List<TGeneralSettingsBean> collTGeneralSettingsBeans;

    /**
     * Returns the collection of TGeneralSettingsBeans
     */
    public List<TGeneralSettingsBean> getTGeneralSettingsBeans()
    {
        return collTGeneralSettingsBeans;
    }

    /**
     * Sets the collection of TGeneralSettingsBeans to the specified value
     */
    public void setTGeneralSettingsBeans(List<TGeneralSettingsBean> list)
    {
        if (list == null)
        {
            collTGeneralSettingsBeans = null;
        }
        else
        {
            collTGeneralSettingsBeans = new ArrayList<TGeneralSettingsBean>(list);
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
