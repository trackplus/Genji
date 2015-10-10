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
 * service level agreement
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TSLABean
 */
public abstract class BaseTSLABean
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
     * Collection to store aggregation of collTWorkflowActivityBeans
     */
    protected List<TWorkflowActivityBean> collTWorkflowActivityBeans;

    /**
     * Returns the collection of TWorkflowActivityBeans
     */
    public List<TWorkflowActivityBean> getTWorkflowActivityBeans()
    {
        return collTWorkflowActivityBeans;
    }

    /**
     * Sets the collection of TWorkflowActivityBeans to the specified value
     */
    public void setTWorkflowActivityBeans(List<TWorkflowActivityBean> list)
    {
        if (list == null)
        {
            collTWorkflowActivityBeans = null;
        }
        else
        {
            collTWorkflowActivityBeans = new ArrayList<TWorkflowActivityBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTEscalationEntryBeans
     */
    protected List<TEscalationEntryBean> collTEscalationEntryBeans;

    /**
     * Returns the collection of TEscalationEntryBeans
     */
    public List<TEscalationEntryBean> getTEscalationEntryBeans()
    {
        return collTEscalationEntryBeans;
    }

    /**
     * Sets the collection of TEscalationEntryBeans to the specified value
     */
    public void setTEscalationEntryBeans(List<TEscalationEntryBean> list)
    {
        if (list == null)
        {
            collTEscalationEntryBeans = null;
        }
        else
        {
            collTEscalationEntryBeans = new ArrayList<TEscalationEntryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTOrgProjectSLABeans
     */
    protected List<TOrgProjectSLABean> collTOrgProjectSLABeans;

    /**
     * Returns the collection of TOrgProjectSLABeans
     */
    public List<TOrgProjectSLABean> getTOrgProjectSLABeans()
    {
        return collTOrgProjectSLABeans;
    }

    /**
     * Sets the collection of TOrgProjectSLABeans to the specified value
     */
    public void setTOrgProjectSLABeans(List<TOrgProjectSLABean> list)
    {
        if (list == null)
        {
            collTOrgProjectSLABeans = null;
        }
        else
        {
            collTOrgProjectSLABeans = new ArrayList<TOrgProjectSLABean>(list);
        }
    }

}
