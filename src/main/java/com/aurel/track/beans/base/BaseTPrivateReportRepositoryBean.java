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
 * extended; all references should be to TPrivateReportRepositoryBean
 */
public abstract class BaseTPrivateReportRepositoryBean
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

    /** The value for the ownerID field */
    private Integer ownerID;

    /** The value for the name field */
    private String name;

    /** The value for the query field */
    private String query;

    /** The value for the menuItem field */
    private String menuItem = "N";

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
     * Get the OwnerID
     *
     * @return Integer
     */
    public Integer getOwnerID ()
    {
        return ownerID;
    }

    /**
     * Set the value of OwnerID
     *
     * @param v new value
     */
    public void setOwnerID(Integer v)
    {

        this.ownerID = v;
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
     * Get the Query
     *
     * @return String
     */
    public String getQuery ()
    {
        return query;
    }

    /**
     * Set the value of Query
     *
     * @param v new value
     */
    public void setQuery(String v)
    {

        this.query = v;
        setModified(true);

    }

    /**
     * Get the MenuItem
     *
     * @return String
     */
    public String getMenuItem ()
    {
        return menuItem;
    }

    /**
     * Set the value of MenuItem
     *
     * @param v new value
     */
    public void setMenuItem(String v)
    {

        this.menuItem = v;
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
            setOwnerID((Integer) null);
        }
        else
        {
            setOwnerID(v.getObjectID());
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





    /**
     * Collection to store aggregation of collTPersonBeans
     */
    protected List<TPersonBean> collTPersonBeans;

    /**
     * Returns the collection of TPersonBeans
     */
    public List<TPersonBean> getTPersonBeans()
    {
        return collTPersonBeans;
    }

    /**
     * Sets the collection of TPersonBeans to the specified value
     */
    public void setTPersonBeans(List<TPersonBean> list)
    {
        if (list == null)
        {
            collTPersonBeans = null;
        }
        else
        {
            collTPersonBeans = new ArrayList<TPersonBean>(list);
        }
    }

}
