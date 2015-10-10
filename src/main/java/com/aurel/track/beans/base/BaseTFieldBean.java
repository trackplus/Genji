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
 * extended; all references should be to TFieldBean
 */
public abstract class BaseTFieldBean
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

    /** The value for the fieldType field */
    private String fieldType;

    /** The value for the deprecated field */
    private String deprecated = "N";

    /** The value for the isCustom field */
    private String isCustom = "Y";

    /** The value for the required field */
    private String required = "N";

    /** The value for the filterField field */
    private String filterField = "N";

    /** The value for the description field */
    private String description;

    /** The value for the owner field */
    private Integer owner;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

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
     * Get the FieldType
     *
     * @return String
     */
    public String getFieldType ()
    {
        return fieldType;
    }

    /**
     * Set the value of FieldType
     *
     * @param v new value
     */
    public void setFieldType(String v)
    {

        this.fieldType = v;
        setModified(true);

    }

    /**
     * Get the Deprecated
     *
     * @return String
     */
    public String getDeprecated ()
    {
        return deprecated;
    }

    /**
     * Set the value of Deprecated
     *
     * @param v new value
     */
    public void setDeprecated(String v)
    {

        this.deprecated = v;
        setModified(true);

    }

    /**
     * Get the IsCustom
     *
     * @return String
     */
    public String getIsCustom ()
    {
        return isCustom;
    }

    /**
     * Set the value of IsCustom
     *
     * @param v new value
     */
    public void setIsCustom(String v)
    {

        this.isCustom = v;
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
     * Get the FilterField
     *
     * @return String
     */
    public String getFilterField ()
    {
        return filterField;
    }

    /**
     * Set the value of FilterField
     *
     * @param v new value
     */
    public void setFilterField(String v)
    {

        this.filterField = v;
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
     * Collection to store aggregation of collTAttributeValueBeans
     */
    protected List<TAttributeValueBean> collTAttributeValueBeans;

    /**
     * Returns the collection of TAttributeValueBeans
     */
    public List<TAttributeValueBean> getTAttributeValueBeans()
    {
        return collTAttributeValueBeans;
    }

    /**
     * Sets the collection of TAttributeValueBeans to the specified value
     */
    public void setTAttributeValueBeans(List<TAttributeValueBean> list)
    {
        if (list == null)
        {
            collTAttributeValueBeans = null;
        }
        else
        {
            collTAttributeValueBeans = new ArrayList<TAttributeValueBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTScreenFieldBeans
     */
    protected List<TScreenFieldBean> collTScreenFieldBeans;

    /**
     * Returns the collection of TScreenFieldBeans
     */
    public List<TScreenFieldBean> getTScreenFieldBeans()
    {
        return collTScreenFieldBeans;
    }

    /**
     * Sets the collection of TScreenFieldBeans to the specified value
     */
    public void setTScreenFieldBeans(List<TScreenFieldBean> list)
    {
        if (list == null)
        {
            collTScreenFieldBeans = null;
        }
        else
        {
            collTScreenFieldBeans = new ArrayList<TScreenFieldBean>(list);
        }
    }

}
