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
 * extended; all references should be to TReportLayoutBean
 */
public abstract class BaseTReportLayoutBean
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

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the project field */
    private Integer project;

    /** The value for the person field */
    private Integer person;

    /** The value for the reportField field */
    private Integer reportField;

    /** The value for the fieldPosition field */
    private Integer fieldPosition;

    /** The value for the fieldWidth field */
    private Integer fieldWidth;

    /** The value for the fieldSortOrder field */
    private Integer fieldSortOrder;

    /** The value for the fieldSortDirection field */
    private String fieldSortDirection;

    /** The value for the fieldType field */
    private Integer fieldType;

    /** The value for the expanding field */
    private String expanding;

    /** The value for the layoutKey field */
    private Integer layoutKey;

    /** The value for the queryID field */
    private Integer queryID;

    /** The value for the queryType field */
    private Integer queryType;

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
     * Get the ReportField
     *
     * @return Integer
     */
    public Integer getReportField ()
    {
        return reportField;
    }

    /**
     * Set the value of ReportField
     *
     * @param v new value
     */
    public void setReportField(Integer v)
    {

        this.reportField = v;
        setModified(true);

    }

    /**
     * Get the FieldPosition
     *
     * @return Integer
     */
    public Integer getFieldPosition ()
    {
        return fieldPosition;
    }

    /**
     * Set the value of FieldPosition
     *
     * @param v new value
     */
    public void setFieldPosition(Integer v)
    {

        this.fieldPosition = v;
        setModified(true);

    }

    /**
     * Get the FieldWidth
     *
     * @return Integer
     */
    public Integer getFieldWidth ()
    {
        return fieldWidth;
    }

    /**
     * Set the value of FieldWidth
     *
     * @param v new value
     */
    public void setFieldWidth(Integer v)
    {

        this.fieldWidth = v;
        setModified(true);

    }

    /**
     * Get the FieldSortOrder
     *
     * @return Integer
     */
    public Integer getFieldSortOrder ()
    {
        return fieldSortOrder;
    }

    /**
     * Set the value of FieldSortOrder
     *
     * @param v new value
     */
    public void setFieldSortOrder(Integer v)
    {

        this.fieldSortOrder = v;
        setModified(true);

    }

    /**
     * Get the FieldSortDirection
     *
     * @return String
     */
    public String getFieldSortDirection ()
    {
        return fieldSortDirection;
    }

    /**
     * Set the value of FieldSortDirection
     *
     * @param v new value
     */
    public void setFieldSortDirection(String v)
    {

        this.fieldSortDirection = v;
        setModified(true);

    }

    /**
     * Get the FieldType
     *
     * @return Integer
     */
    public Integer getFieldType ()
    {
        return fieldType;
    }

    /**
     * Set the value of FieldType
     *
     * @param v new value
     */
    public void setFieldType(Integer v)
    {

        this.fieldType = v;
        setModified(true);

    }

    /**
     * Get the Expanding
     *
     * @return String
     */
    public String getExpanding ()
    {
        return expanding;
    }

    /**
     * Set the value of Expanding
     *
     * @param v new value
     */
    public void setExpanding(String v)
    {

        this.expanding = v;
        setModified(true);

    }

    /**
     * Get the LayoutKey
     *
     * @return Integer
     */
    public Integer getLayoutKey ()
    {
        return layoutKey;
    }

    /**
     * Set the value of LayoutKey
     *
     * @param v new value
     */
    public void setLayoutKey(Integer v)
    {

        this.layoutKey = v;
        setModified(true);

    }

    /**
     * Get the QueryID
     *
     * @return Integer
     */
    public Integer getQueryID ()
    {
        return queryID;
    }

    /**
     * Set the value of QueryID
     *
     * @param v new value
     */
    public void setQueryID(Integer v)
    {

        this.queryID = v;
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



}
