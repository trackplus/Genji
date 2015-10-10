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
 * extended; all references should be to TCostCenterBean
 */
public abstract class BaseTCostCenterBean
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

    /** The value for the costcenterNumber field */
    private String costcenterNumber;

    /** The value for the costcenterName field */
    private String costcenterName;

    /** The value for the moreProps field */
    private String moreProps;

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
     * Get the CostcenterNumber
     *
     * @return String
     */
    public String getCostcenterNumber ()
    {
        return costcenterNumber;
    }

    /**
     * Set the value of CostcenterNumber
     *
     * @param v new value
     */
    public void setCostcenterNumber(String v)
    {

        this.costcenterNumber = v;
        setModified(true);

    }

    /**
     * Get the CostcenterName
     *
     * @return String
     */
    public String getCostcenterName ()
    {
        return costcenterName;
    }

    /**
     * Set the value of CostcenterName
     *
     * @param v new value
     */
    public void setCostcenterName(String v)
    {

        this.costcenterName = v;
        setModified(true);

    }

    /**
     * Get the MoreProps
     *
     * @return String
     */
    public String getMoreProps ()
    {
        return moreProps;
    }

    /**
     * Set the value of MoreProps
     *
     * @param v new value
     */
    public void setMoreProps(String v)
    {

        this.moreProps = v;
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

    



    /**
     * Collection to store aggregation of collTDepartmentBeans
     */
    protected List<TDepartmentBean> collTDepartmentBeans;

    /**
     * Returns the collection of TDepartmentBeans
     */
    public List<TDepartmentBean> getTDepartmentBeans()
    {
        return collTDepartmentBeans;
    }

    /**
     * Sets the collection of TDepartmentBeans to the specified value
     */
    public void setTDepartmentBeans(List<TDepartmentBean> list)
    {
        if (list == null)
        {
            collTDepartmentBeans = null;
        }
        else
        {
            collTDepartmentBeans = new ArrayList<TDepartmentBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTAccountBeans
     */
    protected List<TAccountBean> collTAccountBeans;

    /**
     * Returns the collection of TAccountBeans
     */
    public List<TAccountBean> getTAccountBeans()
    {
        return collTAccountBeans;
    }

    /**
     * Sets the collection of TAccountBeans to the specified value
     */
    public void setTAccountBeans(List<TAccountBean> list)
    {
        if (list == null)
        {
            collTAccountBeans = null;
        }
        else
        {
            collTAccountBeans = new ArrayList<TAccountBean>(list);
        }
    }

}
