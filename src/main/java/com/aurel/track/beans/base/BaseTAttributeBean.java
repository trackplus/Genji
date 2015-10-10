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
 * extended; all references should be to TAttributeBean
 */
public abstract class BaseTAttributeBean
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

    /** The value for the attributeName field */
    private String attributeName;

    /** The value for the attributeType field */
    private Integer attributeType;

    /** The value for the deleted field */
    private Integer deleted;

    /** The value for the description field */
    private String description;

    /** The value for the permission field */
    private String permission;

    /** The value for the requiredOption field */
    private Integer requiredOption;

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
     * Get the AttributeName
     *
     * @return String
     */
    public String getAttributeName ()
    {
        return attributeName;
    }

    /**
     * Set the value of AttributeName
     *
     * @param v new value
     */
    public void setAttributeName(String v)
    {

        this.attributeName = v;
        setModified(true);

    }

    /**
     * Get the AttributeType
     *
     * @return Integer
     */
    public Integer getAttributeType ()
    {
        return attributeType;
    }

    /**
     * Set the value of AttributeType
     *
     * @param v new value
     */
    public void setAttributeType(Integer v)
    {

        this.attributeType = v;
        setModified(true);

    }

    /**
     * Get the Deleted
     *
     * @return Integer
     */
    public Integer getDeleted ()
    {
        return deleted;
    }

    /**
     * Set the value of Deleted
     *
     * @param v new value
     */
    public void setDeleted(Integer v)
    {

        this.deleted = v;
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
     * Get the Permission
     *
     * @return String
     */
    public String getPermission ()
    {
        return permission;
    }

    /**
     * Set the value of Permission
     *
     * @param v new value
     */
    public void setPermission(String v)
    {

        this.permission = v;
        setModified(true);

    }

    /**
     * Get the RequiredOption
     *
     * @return Integer
     */
    public Integer getRequiredOption ()
    {
        return requiredOption;
    }

    /**
     * Set the value of RequiredOption
     *
     * @param v new value
     */
    public void setRequiredOption(Integer v)
    {

        this.requiredOption = v;
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

    



    private TAttributeOptionBean aTAttributeOptionBean;

    /**
     * sets an associated TAttributeOptionBean object
     *
     * @param v TAttributeOptionBean
     */
    public void setTAttributeOptionBean(TAttributeOptionBean v)
    {
        if (v == null)
        {
            setRequiredOption((Integer) null);
        }
        else
        {
            setRequiredOption(v.getObjectID());
        }
        aTAttributeOptionBean = v;
    }


    /**
     * Get the associated TAttributeOptionBean object
     *
     * @return the associated TAttributeOptionBean object
     */
    public TAttributeOptionBean getTAttributeOptionBean()
    {
        return aTAttributeOptionBean;
    }





    /**
     * Collection to store aggregation of collTIssueAttributeValueBeans
     */
    protected List<TIssueAttributeValueBean> collTIssueAttributeValueBeans;

    /**
     * Returns the collection of TIssueAttributeValueBeans
     */
    public List<TIssueAttributeValueBean> getTIssueAttributeValueBeans()
    {
        return collTIssueAttributeValueBeans;
    }

    /**
     * Sets the collection of TIssueAttributeValueBeans to the specified value
     */
    public void setTIssueAttributeValueBeans(List<TIssueAttributeValueBean> list)
    {
        if (list == null)
        {
            collTIssueAttributeValueBeans = null;
        }
        else
        {
            collTIssueAttributeValueBeans = new ArrayList<TIssueAttributeValueBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTAttributeOptionBeans
     */
    protected List<TAttributeOptionBean> collTAttributeOptionBeans;

    /**
     * Returns the collection of TAttributeOptionBeans
     */
    public List<TAttributeOptionBean> getTAttributeOptionBeans()
    {
        return collTAttributeOptionBeans;
    }

    /**
     * Sets the collection of TAttributeOptionBeans to the specified value
     */
    public void setTAttributeOptionBeans(List<TAttributeOptionBean> list)
    {
        if (list == null)
        {
            collTAttributeOptionBeans = null;
        }
        else
        {
            collTAttributeOptionBeans = new ArrayList<TAttributeOptionBean>(list);
        }
    }

}
