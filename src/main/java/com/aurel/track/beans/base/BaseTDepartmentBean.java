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
 * This table holds the departments. Each user belongs to one department.
 *
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TDepartmentBean
 */
public abstract class BaseTDepartmentBean
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

    /** The value for the label field */
    private String label;

    /** The value for the costcenter field */
    private Integer costcenter;

    /** The value for the parent field */
    private Integer parent;

    /** The value for the domain field */
    private Integer domain;

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
     * Get the Costcenter
     *
     * @return Integer
     */
    public Integer getCostcenter ()
    {
        return costcenter;
    }

    /**
     * Set the value of Costcenter
     *
     * @param v new value
     */
    public void setCostcenter(Integer v)
    {

        this.costcenter = v;
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
     * Get the Domain
     *
     * @return Integer
     */
    public Integer getDomain ()
    {
        return domain;
    }

    /**
     * Set the value of Domain
     *
     * @param v new value
     */
    public void setDomain(Integer v)
    {

        this.domain = v;
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

    



    private TCostCenterBean aTCostCenterBean;

    /**
     * sets an associated TCostCenterBean object
     *
     * @param v TCostCenterBean
     */
    public void setTCostCenterBean(TCostCenterBean v)
    {
        if (v == null)
        {
            setCostcenter((Integer) null);
        }
        else
        {
            setCostcenter(v.getObjectID());
        }
        aTCostCenterBean = v;
    }


    /**
     * Get the associated TCostCenterBean object
     *
     * @return the associated TCostCenterBean object
     */
    public TCostCenterBean getTCostCenterBean()
    {
        return aTCostCenterBean;
    }





    private TDomainBean aTDomainBean;

    /**
     * sets an associated TDomainBean object
     *
     * @param v TDomainBean
     */
    public void setTDomainBean(TDomainBean v)
    {
        if (v == null)
        {
            setDomain((Integer) null);
        }
        else
        {
            setDomain(v.getObjectID());
        }
        aTDomainBean = v;
    }


    /**
     * Get the associated TDomainBean object
     *
     * @return the associated TDomainBean object
     */
    public TDomainBean getTDomainBean()
    {
        return aTDomainBean;
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
