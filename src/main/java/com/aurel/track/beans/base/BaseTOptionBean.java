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
 * extended; all references should be to TOptionBean
 */
public abstract class BaseTOptionBean
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

    /** The value for the list field */
    private Integer list;

    /** The value for the label field */
    private String label;

    /** The value for the tooltip field */
    private String tooltip;

    /** The value for the parentOption field */
    private Integer parentOption;

    /** The value for the sortOrder field */
    private Integer sortOrder;

    /** The value for the isDefault field */
    private String isDefault = "N";

    /** The value for the deleted field */
    private String deleted = "N";

    /** The value for the symbol field */
    private String symbol;

    /** The value for the iconKey field */
    private Integer iconKey;

    /** The value for the iconChanged field */
    private String iconChanged = "N";

    /** The value for the cSSStyle field */
    private String cSSStyle;

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
     * Get the List
     *
     * @return Integer
     */
    public Integer getList ()
    {
        return list;
    }

    /**
     * Set the value of List
     *
     * @param v new value
     */
    public void setList(Integer v)
    {

        this.list = v;
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
     * Get the SortOrder
     *
     * @return Integer
     */
    public Integer getSortOrder ()
    {
        return sortOrder;
    }

    /**
     * Set the value of SortOrder
     *
     * @param v new value
     */
    public void setSortOrder(Integer v)
    {

        this.sortOrder = v;
        setModified(true);

    }

    /**
     * Get the IsDefault
     *
     * @return String
     */
    public String getIsDefault ()
    {
        return isDefault;
    }

    /**
     * Set the value of IsDefault
     *
     * @param v new value
     */
    public void setIsDefault(String v)
    {

        this.isDefault = v;
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
     * Get the Symbol
     *
     * @return String
     */
    public String getSymbol ()
    {
        return symbol;
    }

    /**
     * Set the value of Symbol
     *
     * @param v new value
     */
    public void setSymbol(String v)
    {

        this.symbol = v;
        setModified(true);

    }

    /**
     * Get the IconKey
     *
     * @return Integer
     */
    public Integer getIconKey ()
    {
        return iconKey;
    }

    /**
     * Set the value of IconKey
     *
     * @param v new value
     */
    public void setIconKey(Integer v)
    {

        this.iconKey = v;
        setModified(true);

    }

    /**
     * Get the IconChanged
     *
     * @return String
     */
    public String getIconChanged ()
    {
        return iconChanged;
    }

    /**
     * Set the value of IconChanged
     *
     * @param v new value
     */
    public void setIconChanged(String v)
    {

        this.iconChanged = v;
        setModified(true);

    }

    /**
     * Get the CSSStyle
     *
     * @return String
     */
    public String getCSSStyle ()
    {
        return cSSStyle;
    }

    /**
     * Set the value of CSSStyle
     *
     * @param v new value
     */
    public void setCSSStyle(String v)
    {

        this.cSSStyle = v;
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

    



    private TListBean aTListBean;

    /**
     * sets an associated TListBean object
     *
     * @param v TListBean
     */
    public void setTListBean(TListBean v)
    {
        if (v == null)
        {
            setList((Integer) null);
        }
        else
        {
            setList(v.getObjectID());
        }
        aTListBean = v;
    }


    /**
     * Get the associated TListBean object
     *
     * @return the associated TListBean object
     */
    public TListBean getTListBean()
    {
        return aTListBean;
    }





    private TBLOBBean aTBLOBBean;

    /**
     * sets an associated TBLOBBean object
     *
     * @param v TBLOBBean
     */
    public void setTBLOBBean(TBLOBBean v)
    {
        if (v == null)
        {
            setIconKey((Integer) null);
        }
        else
        {
            setIconKey(v.getObjectID());
        }
        aTBLOBBean = v;
    }


    /**
     * Get the associated TBLOBBean object
     *
     * @return the associated TBLOBBean object
     */
    public TBLOBBean getTBLOBBean()
    {
        return aTBLOBBean;
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
     * Collection to store aggregation of collTFieldChangeBeansRelatedByNewCustomOptionID
     */
    protected List<TFieldChangeBean> collTFieldChangeBeansRelatedByNewCustomOptionID;

    /**
     * Returns the collection of TFieldChangeBeansRelatedByNewCustomOptionID
     */
    public List<TFieldChangeBean> getTFieldChangeBeansRelatedByNewCustomOptionID()
    {
        return collTFieldChangeBeansRelatedByNewCustomOptionID;
    }

    /**
     * Sets the collection of TFieldChangeBeansRelatedByNewCustomOptionID to the specified value
     */
    public void setTFieldChangeBeansRelatedByNewCustomOptionID(List<TFieldChangeBean> list)
    {
        if (list == null)
        {
            collTFieldChangeBeansRelatedByNewCustomOptionID = null;
        }
        else
        {
            collTFieldChangeBeansRelatedByNewCustomOptionID = new ArrayList<TFieldChangeBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTFieldChangeBeansRelatedByOldCustomOptionID
     */
    protected List<TFieldChangeBean> collTFieldChangeBeansRelatedByOldCustomOptionID;

    /**
     * Returns the collection of TFieldChangeBeansRelatedByOldCustomOptionID
     */
    public List<TFieldChangeBean> getTFieldChangeBeansRelatedByOldCustomOptionID()
    {
        return collTFieldChangeBeansRelatedByOldCustomOptionID;
    }

    /**
     * Sets the collection of TFieldChangeBeansRelatedByOldCustomOptionID to the specified value
     */
    public void setTFieldChangeBeansRelatedByOldCustomOptionID(List<TFieldChangeBean> list)
    {
        if (list == null)
        {
            collTFieldChangeBeansRelatedByOldCustomOptionID = null;
        }
        else
        {
            collTFieldChangeBeansRelatedByOldCustomOptionID = new ArrayList<TFieldChangeBean>(list);
        }
    }

}
