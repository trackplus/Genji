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
 * extended; all references should be to TAttributeTypeBean
 */
public abstract class BaseTAttributeTypeBean
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

    /** The value for the attributeTypeName field */
    private String attributeTypeName;

    /** The value for the attributeClass field */
    private Integer attributeClass;

    /** The value for the javaClassName field */
    private String javaClassName;

    /** The value for the validationKey field */
    private String validationKey;

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
     * Get the AttributeTypeName
     *
     * @return String
     */
    public String getAttributeTypeName ()
    {
        return attributeTypeName;
    }

    /**
     * Set the value of AttributeTypeName
     *
     * @param v new value
     */
    public void setAttributeTypeName(String v)
    {

        this.attributeTypeName = v;
        setModified(true);

    }

    /**
     * Get the AttributeClass
     *
     * @return Integer
     */
    public Integer getAttributeClass ()
    {
        return attributeClass;
    }

    /**
     * Set the value of AttributeClass
     *
     * @param v new value
     */
    public void setAttributeClass(Integer v)
    {

        this.attributeClass = v;
        setModified(true);

    }

    /**
     * Get the JavaClassName
     *
     * @return String
     */
    public String getJavaClassName ()
    {
        return javaClassName;
    }

    /**
     * Set the value of JavaClassName
     *
     * @param v new value
     */
    public void setJavaClassName(String v)
    {

        this.javaClassName = v;
        setModified(true);

    }

    /**
     * Get the ValidationKey
     *
     * @return String
     */
    public String getValidationKey ()
    {
        return validationKey;
    }

    /**
     * Set the value of ValidationKey
     *
     * @param v new value
     */
    public void setValidationKey(String v)
    {

        this.validationKey = v;
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

    



    private TAttributeClassBean aTAttributeClassBean;

    /**
     * sets an associated TAttributeClassBean object
     *
     * @param v TAttributeClassBean
     */
    public void setTAttributeClassBean(TAttributeClassBean v)
    {
        if (v == null)
        {
            setAttributeClass((Integer) null);
        }
        else
        {
            setAttributeClass(v.getObjectID());
        }
        aTAttributeClassBean = v;
    }


    /**
     * Get the associated TAttributeClassBean object
     *
     * @return the associated TAttributeClassBean object
     */
    public TAttributeClassBean getTAttributeClassBean()
    {
        return aTAttributeClassBean;
    }



}
