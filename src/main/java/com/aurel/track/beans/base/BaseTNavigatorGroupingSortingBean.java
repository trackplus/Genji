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
 * grouping and sorting in item navigator
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TNavigatorGroupingSortingBean
 */
public abstract class BaseTNavigatorGroupingSortingBean
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

    /** The value for the navigatorLayout field */
    private Integer navigatorLayout;

    /** The value for the field field */
    private Integer field;

    /** The value for the sortPosition field */
    private Integer sortPosition;

    /** The value for the isGrouping field */
    private String isGrouping = "N";

    /** The value for the isDescending field */
    private String isDescending = "N";

    /** The value for the isCollapsed field */
    private String isCollapsed = "N";

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
     * Get the NavigatorLayout
     *
     * @return Integer
     */
    public Integer getNavigatorLayout ()
    {
        return navigatorLayout;
    }

    /**
     * Set the value of NavigatorLayout
     *
     * @param v new value
     */
    public void setNavigatorLayout(Integer v)
    {

        this.navigatorLayout = v;
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
     * Get the SortPosition
     *
     * @return Integer
     */
    public Integer getSortPosition ()
    {
        return sortPosition;
    }

    /**
     * Set the value of SortPosition
     *
     * @param v new value
     */
    public void setSortPosition(Integer v)
    {

        this.sortPosition = v;
        setModified(true);

    }

    /**
     * Get the IsGrouping
     *
     * @return String
     */
    public String getIsGrouping ()
    {
        return isGrouping;
    }

    /**
     * Set the value of IsGrouping
     *
     * @param v new value
     */
    public void setIsGrouping(String v)
    {

        this.isGrouping = v;
        setModified(true);

    }

    /**
     * Get the IsDescending
     *
     * @return String
     */
    public String getIsDescending ()
    {
        return isDescending;
    }

    /**
     * Set the value of IsDescending
     *
     * @param v new value
     */
    public void setIsDescending(String v)
    {

        this.isDescending = v;
        setModified(true);

    }

    /**
     * Get the IsCollapsed
     *
     * @return String
     */
    public String getIsCollapsed ()
    {
        return isCollapsed;
    }

    /**
     * Set the value of IsCollapsed
     *
     * @param v new value
     */
    public void setIsCollapsed(String v)
    {

        this.isCollapsed = v;
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

    



    private TNavigatorLayoutBean aTNavigatorLayoutBean;

    /**
     * sets an associated TNavigatorLayoutBean object
     *
     * @param v TNavigatorLayoutBean
     */
    public void setTNavigatorLayoutBean(TNavigatorLayoutBean v)
    {
        if (v == null)
        {
            setNavigatorLayout((Integer) null);
        }
        else
        {
            setNavigatorLayout(v.getObjectID());
        }
        aTNavigatorLayoutBean = v;
    }


    /**
     * Get the associated TNavigatorLayoutBean object
     *
     * @return the associated TNavigatorLayoutBean object
     */
    public TNavigatorLayoutBean getTNavigatorLayoutBean()
    {
        return aTNavigatorLayoutBean;
    }



}
