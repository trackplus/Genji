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
 * Contains Groovy scripts
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TScriptsBean
 */
public abstract class BaseTScriptsBean
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

    /** The value for the lastEdit field */
    private Date lastEdit;

    /** The value for the scriptVersion field */
    private Integer scriptVersion;

    /** The value for the originalVersion field */
    private Integer originalVersion;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

    /** The value for the scriptType field */
    private Integer scriptType;

    /** The value for the scriptRole field */
    private Integer scriptRole;

    /** The value for the clazzName field */
    private String clazzName;

    /** The value for the sourceCode field */
    private String sourceCode;

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
     * Get the LastEdit
     *
     * @return Date
     */
    public Date getLastEdit ()
    {
        return lastEdit;
    }

    /**
     * Set the value of LastEdit
     *
     * @param v new value
     */
    public void setLastEdit(Date v)
    {

        this.lastEdit = v;
        setModified(true);

    }

    /**
     * Get the ScriptVersion
     *
     * @return Integer
     */
    public Integer getScriptVersion ()
    {
        return scriptVersion;
    }

    /**
     * Set the value of ScriptVersion
     *
     * @param v new value
     */
    public void setScriptVersion(Integer v)
    {

        this.scriptVersion = v;
        setModified(true);

    }

    /**
     * Get the OriginalVersion
     *
     * @return Integer
     */
    public Integer getOriginalVersion ()
    {
        return originalVersion;
    }

    /**
     * Set the value of OriginalVersion
     *
     * @param v new value
     */
    public void setOriginalVersion(Integer v)
    {

        this.originalVersion = v;
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
     * Get the ScriptType
     *
     * @return Integer
     */
    public Integer getScriptType ()
    {
        return scriptType;
    }

    /**
     * Set the value of ScriptType
     *
     * @param v new value
     */
    public void setScriptType(Integer v)
    {

        this.scriptType = v;
        setModified(true);

    }

    /**
     * Get the ScriptRole
     *
     * @return Integer
     */
    public Integer getScriptRole ()
    {
        return scriptRole;
    }

    /**
     * Set the value of ScriptRole
     *
     * @param v new value
     */
    public void setScriptRole(Integer v)
    {

        this.scriptRole = v;
        setModified(true);

    }

    /**
     * Get the ClazzName
     *
     * @return String
     */
    public String getClazzName ()
    {
        return clazzName;
    }

    /**
     * Set the value of ClazzName
     *
     * @param v new value
     */
    public void setClazzName(String v)
    {

        this.clazzName = v;
        setModified(true);

    }

    /**
     * Get the SourceCode
     *
     * @return String
     */
    public String getSourceCode ()
    {
        return sourceCode;
    }

    /**
     * Set the value of SourceCode
     *
     * @param v new value
     */
    public void setSourceCode(String v)
    {

        this.sourceCode = v;
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





    /**
     * Collection to store aggregation of collTFieldConfigBeans
     */
    protected List<TFieldConfigBean> collTFieldConfigBeans;

    /**
     * Returns the collection of TFieldConfigBeans
     */
    public List<TFieldConfigBean> getTFieldConfigBeans()
    {
        return collTFieldConfigBeans;
    }

    /**
     * Sets the collection of TFieldConfigBeans to the specified value
     */
    public void setTFieldConfigBeans(List<TFieldConfigBean> list)
    {
        if (list == null)
        {
            collTFieldConfigBeans = null;
        }
        else
        {
            collTFieldConfigBeans = new ArrayList<TFieldConfigBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowActivityBeans
     */
    protected List<TWorkflowActivityBean> collTWorkflowActivityBeans;

    /**
     * Returns the collection of TWorkflowActivityBeans
     */
    public List<TWorkflowActivityBean> getTWorkflowActivityBeans()
    {
        return collTWorkflowActivityBeans;
    }

    /**
     * Sets the collection of TWorkflowActivityBeans to the specified value
     */
    public void setTWorkflowActivityBeans(List<TWorkflowActivityBean> list)
    {
        if (list == null)
        {
            collTWorkflowActivityBeans = null;
        }
        else
        {
            collTWorkflowActivityBeans = new ArrayList<TWorkflowActivityBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowGuardBeans
     */
    protected List<TWorkflowGuardBean> collTWorkflowGuardBeans;

    /**
     * Returns the collection of TWorkflowGuardBeans
     */
    public List<TWorkflowGuardBean> getTWorkflowGuardBeans()
    {
        return collTWorkflowGuardBeans;
    }

    /**
     * Sets the collection of TWorkflowGuardBeans to the specified value
     */
    public void setTWorkflowGuardBeans(List<TWorkflowGuardBean> list)
    {
        if (list == null)
        {
            collTWorkflowGuardBeans = null;
        }
        else
        {
            collTWorkflowGuardBeans = new ArrayList<TWorkflowGuardBean>(list);
        }
    }

}
