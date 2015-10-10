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
 * extended; all references should be to TAttributeOptionBean
 */
public abstract class BaseTAttributeOptionBean
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

    /** The value for the attributeID field */
    private Integer attributeID;

    /** The value for the parentOption field */
    private Integer parentOption;

    /** The value for the optionName field */
    private String optionName;

    /** The value for the deleted field */
    private Integer deleted;

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
     * Get the AttributeID
     *
     * @return Integer
     */
    public Integer getAttributeID ()
    {
        return attributeID;
    }

    /**
     * Set the value of AttributeID
     *
     * @param v new value
     */
    public void setAttributeID(Integer v)
    {

        this.attributeID = v;
        setModified(true);

    }

    /**
     * Get the ParentOption
     *
     * @return Integer
     */
    public Integer getParentOption ()
    {
        return parentOption;
    }

    /**
     * Set the value of ParentOption
     *
     * @param v new value
     */
    public void setParentOption(Integer v)
    {

        this.parentOption = v;
        setModified(true);

    }

    /**
     * Get the OptionName
     *
     * @return String
     */
    public String getOptionName ()
    {
        return optionName;
    }

    /**
     * Set the value of OptionName
     *
     * @param v new value
     */
    public void setOptionName(String v)
    {

        this.optionName = v;
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

    



    private TAttributeBean aTAttributeBean;

    /**
     * sets an associated TAttributeBean object
     *
     * @param v TAttributeBean
     */
    public void setTAttributeBean(TAttributeBean v)
    {
        if (v == null)
        {
            setAttributeID((Integer) null);
        }
        else
        {
            setAttributeID(v.getObjectID());
        }
        aTAttributeBean = v;
    }


    /**
     * Get the associated TAttributeBean object
     *
     * @return the associated TAttributeBean object
     */
    public TAttributeBean getTAttributeBean()
    {
        return aTAttributeBean;
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
     * Collection to store aggregation of collTAttributeBeans
     */
    protected List<TAttributeBean> collTAttributeBeans;

    /**
     * Returns the collection of TAttributeBeans
     */
    public List<TAttributeBean> getTAttributeBeans()
    {
        return collTAttributeBeans;
    }

    /**
     * Sets the collection of TAttributeBeans to the specified value
     */
    public void setTAttributeBeans(List<TAttributeBean> list)
    {
        if (list == null)
        {
            collTAttributeBeans = null;
        }
        else
        {
            collTAttributeBeans = new ArrayList<TAttributeBean>(list);
        }
    }

}
