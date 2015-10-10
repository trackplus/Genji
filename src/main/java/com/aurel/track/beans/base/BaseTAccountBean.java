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
 * extended; all references should be to TAccountBean
 */
public abstract class BaseTAccountBean
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

    /** The value for the accountNumber field */
    private String accountNumber;

    /** The value for the accountName field */
    private String accountName;

    /** The value for the status field */
    private Integer status;

    /** The value for the costCenter field */
    private Integer costCenter;

    /** The value for the description field */
    private String description;

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
     * Get the AccountNumber
     *
     * @return String
     */
    public String getAccountNumber ()
    {
        return accountNumber;
    }

    /**
     * Set the value of AccountNumber
     *
     * @param v new value
     */
    public void setAccountNumber(String v)
    {

        this.accountNumber = v;
        setModified(true);

    }

    /**
     * Get the AccountName
     *
     * @return String
     */
    public String getAccountName ()
    {
        return accountName;
    }

    /**
     * Set the value of AccountName
     *
     * @param v new value
     */
    public void setAccountName(String v)
    {

        this.accountName = v;
        setModified(true);

    }

    /**
     * Get the Status
     *
     * @return Integer
     */
    public Integer getStatus ()
    {
        return status;
    }

    /**
     * Set the value of Status
     *
     * @param v new value
     */
    public void setStatus(Integer v)
    {

        this.status = v;
        setModified(true);

    }

    /**
     * Get the CostCenter
     *
     * @return Integer
     */
    public Integer getCostCenter ()
    {
        return costCenter;
    }

    /**
     * Set the value of CostCenter
     *
     * @param v new value
     */
    public void setCostCenter(Integer v)
    {

        this.costCenter = v;
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

    



    private TSystemStateBean aTSystemStateBean;

    /**
     * sets an associated TSystemStateBean object
     *
     * @param v TSystemStateBean
     */
    public void setTSystemStateBean(TSystemStateBean v)
    {
        if (v == null)
        {
            setStatus((Integer) null);
        }
        else
        {
            setStatus(v.getObjectID());
        }
        aTSystemStateBean = v;
    }


    /**
     * Get the associated TSystemStateBean object
     *
     * @return the associated TSystemStateBean object
     */
    public TSystemStateBean getTSystemStateBean()
    {
        return aTSystemStateBean;
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
            setCostCenter((Integer) null);
        }
        else
        {
            setCostCenter(v.getObjectID());
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





    /**
     * Collection to store aggregation of collTCostBeans
     */
    protected List<TCostBean> collTCostBeans;

    /**
     * Returns the collection of TCostBeans
     */
    public List<TCostBean> getTCostBeans()
    {
        return collTCostBeans;
    }

    /**
     * Sets the collection of TCostBeans to the specified value
     */
    public void setTCostBeans(List<TCostBean> list)
    {
        if (list == null)
        {
            collTCostBeans = null;
        }
        else
        {
            collTCostBeans = new ArrayList<TCostBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTProjectAccountBeans
     */
    protected List<TProjectAccountBean> collTProjectAccountBeans;

    /**
     * Returns the collection of TProjectAccountBeans
     */
    public List<TProjectAccountBean> getTProjectAccountBeans()
    {
        return collTProjectAccountBeans;
    }

    /**
     * Sets the collection of TProjectAccountBeans to the specified value
     */
    public void setTProjectAccountBeans(List<TProjectAccountBean> list)
    {
        if (list == null)
        {
            collTProjectAccountBeans = null;
        }
        else
        {
            collTProjectAccountBeans = new ArrayList<TProjectAccountBean>(list);
        }
    }

}
