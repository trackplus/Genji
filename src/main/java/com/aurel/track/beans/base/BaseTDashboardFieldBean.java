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
 * extended; all references should be to TDashboardFieldBean
 */
public abstract class BaseTDashboardFieldBean
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

    /** The value for the description field */
    private String description;

    /** The value for the index field */
    private Integer index;

    /** The value for the colIndex field */
    private Integer colIndex;

    /** The value for the rowIndex field */
    private Integer rowIndex;

    /** The value for the colSpan field */
    private Integer colSpan;

    /** The value for the rowSpan field */
    private Integer rowSpan;

    /** The value for the parent field */
    private Integer parent;

    /** The value for the dashboardID field */
    private String dashboardID;

    /** The value for the theDescription field */
    private String theDescription;

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
     * Get the ColIndex
     *
     * @return Integer
     */
    public Integer getColIndex ()
    {
        return colIndex;
    }

    /**
     * Set the value of ColIndex
     *
     * @param v new value
     */
    public void setColIndex(Integer v)
    {

        this.colIndex = v;
        setModified(true);

    }

    /**
     * Get the RowIndex
     *
     * @return Integer
     */
    public Integer getRowIndex ()
    {
        return rowIndex;
    }

    /**
     * Set the value of RowIndex
     *
     * @param v new value
     */
    public void setRowIndex(Integer v)
    {

        this.rowIndex = v;
        setModified(true);

    }

    /**
     * Get the ColSpan
     *
     * @return Integer
     */
    public Integer getColSpan ()
    {
        return colSpan;
    }

    /**
     * Set the value of ColSpan
     *
     * @param v new value
     */
    public void setColSpan(Integer v)
    {

        this.colSpan = v;
        setModified(true);

    }

    /**
     * Get the RowSpan
     *
     * @return Integer
     */
    public Integer getRowSpan ()
    {
        return rowSpan;
    }

    /**
     * Set the value of RowSpan
     *
     * @param v new value
     */
    public void setRowSpan(Integer v)
    {

        this.rowSpan = v;
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
     * Get the DashboardID
     *
     * @return String
     */
    public String getDashboardID ()
    {
        return dashboardID;
    }

    /**
     * Set the value of DashboardID
     *
     * @param v new value
     */
    public void setDashboardID(String v)
    {

        this.dashboardID = v;
        setModified(true);

    }

    /**
     * Get the TheDescription
     *
     * @return String
     */
    public String getTheDescription ()
    {
        return theDescription;
    }

    /**
     * Set the value of TheDescription
     *
     * @param v new value
     */
    public void setTheDescription(String v)
    {

        this.theDescription = v;
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

    



    private TDashboardPanelBean aTDashboardPanelBean;

    /**
     * sets an associated TDashboardPanelBean object
     *
     * @param v TDashboardPanelBean
     */
    public void setTDashboardPanelBean(TDashboardPanelBean v)
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTDashboardPanelBean = v;
    }


    /**
     * Get the associated TDashboardPanelBean object
     *
     * @return the associated TDashboardPanelBean object
     */
    public TDashboardPanelBean getTDashboardPanelBean()
    {
        return aTDashboardPanelBean;
    }





    /**
     * Collection to store aggregation of collTDashboardParameterBeans
     */
    protected List<TDashboardParameterBean> collTDashboardParameterBeans;

    /**
     * Returns the collection of TDashboardParameterBeans
     */
    public List<TDashboardParameterBean> getTDashboardParameterBeans()
    {
        return collTDashboardParameterBeans;
    }

    /**
     * Sets the collection of TDashboardParameterBeans to the specified value
     */
    public void setTDashboardParameterBeans(List<TDashboardParameterBean> list)
    {
        if (list == null)
        {
            collTDashboardParameterBeans = null;
        }
        else
        {
            collTDashboardParameterBeans = new ArrayList<TDashboardParameterBean>(list);
        }
    }

}
