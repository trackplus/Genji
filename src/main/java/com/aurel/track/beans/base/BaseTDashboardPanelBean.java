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
 * extended; all references should be to TDashboardPanelBean
 */
public abstract class BaseTDashboardPanelBean
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

    /** The value for the label field */
    private String label;

    /** The value for the description field */
    private String description;

    /** The value for the index field */
    private Integer index;

    /** The value for the rowsNo field */
    private Integer rowsNo;

    /** The value for the colsNo field */
    private Integer colsNo;

    /** The value for the parent field */
    private Integer parent;

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
     * Get the Index
     *
     * @return Integer
     */
    public Integer getIndex ()
    {
        return index;
    }

    /**
     * Set the value of Index
     *
     * @param v new value
     */
    public void setIndex(Integer v)
    {

        this.index = v;
        setModified(true);

    }

    /**
     * Get the RowsNo
     *
     * @return Integer
     */
    public Integer getRowsNo ()
    {
        return rowsNo;
    }

    /**
     * Set the value of RowsNo
     *
     * @param v new value
     */
    public void setRowsNo(Integer v)
    {

        this.rowsNo = v;
        setModified(true);

    }

    /**
     * Get the ColsNo
     *
     * @return Integer
     */
    public Integer getColsNo ()
    {
        return colsNo;
    }

    /**
     * Set the value of ColsNo
     *
     * @param v new value
     */
    public void setColsNo(Integer v)
    {

        this.colsNo = v;
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

    



    private TDashboardTabBean aTDashboardTabBean;

    /**
     * sets an associated TDashboardTabBean object
     *
     * @param v TDashboardTabBean
     */
    public void setTDashboardTabBean(TDashboardTabBean v)
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTDashboardTabBean = v;
    }


    /**
     * Get the associated TDashboardTabBean object
     *
     * @return the associated TDashboardTabBean object
     */
    public TDashboardTabBean getTDashboardTabBean()
    {
        return aTDashboardTabBean;
    }





    /**
     * Collection to store aggregation of collTDashboardFieldBeans
     */
    protected List<TDashboardFieldBean> collTDashboardFieldBeans;

    /**
     * Returns the collection of TDashboardFieldBeans
     */
    public List<TDashboardFieldBean> getTDashboardFieldBeans()
    {
        return collTDashboardFieldBeans;
    }

    /**
     * Sets the collection of TDashboardFieldBeans to the specified value
     */
    public void setTDashboardFieldBeans(List<TDashboardFieldBean> list)
    {
        if (list == null)
        {
            collTDashboardFieldBeans = null;
        }
        else
        {
            collTDashboardFieldBeans = new ArrayList<TDashboardFieldBean>(list);
        }
    }

}
