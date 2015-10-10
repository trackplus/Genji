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
 * extended; all references should be to TBLOBBean
 */
public abstract class BaseTBLOBBean
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

    /** The value for the bLOBValue field */
    private byte[] bLOBValue;

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
     * Get the BLOBValue
     *
     * @return byte[]
     */
    public byte[] getBLOBValue ()
    {
        return bLOBValue;
    }

    /**
     * Set the value of BLOBValue
     *
     * @param v new value
     */
    public void setBLOBValue(byte[] v)
    {

        this.bLOBValue = v;
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
     * Collection to store aggregation of collTListTypeBeans
     */
    protected List<TListTypeBean> collTListTypeBeans;

    /**
     * Returns the collection of TListTypeBeans
     */
    public List<TListTypeBean> getTListTypeBeans()
    {
        return collTListTypeBeans;
    }

    /**
     * Sets the collection of TListTypeBeans to the specified value
     */
    public void setTListTypeBeans(List<TListTypeBean> list)
    {
        if (list == null)
        {
            collTListTypeBeans = null;
        }
        else
        {
            collTListTypeBeans = new ArrayList<TListTypeBean>(list);
        }
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
     * Collection to store aggregation of collTPriorityBeans
     */
    protected List<TPriorityBean> collTPriorityBeans;

    /**
     * Returns the collection of TPriorityBeans
     */
    public List<TPriorityBean> getTPriorityBeans()
    {
        return collTPriorityBeans;
    }

    /**
     * Sets the collection of TPriorityBeans to the specified value
     */
    public void setTPriorityBeans(List<TPriorityBean> list)
    {
        if (list == null)
        {
            collTPriorityBeans = null;
        }
        else
        {
            collTPriorityBeans = new ArrayList<TPriorityBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTSeverityBeans
     */
    protected List<TSeverityBean> collTSeverityBeans;

    /**
     * Returns the collection of TSeverityBeans
     */
    public List<TSeverityBean> getTSeverityBeans()
    {
        return collTSeverityBeans;
    }

    /**
     * Sets the collection of TSeverityBeans to the specified value
     */
    public void setTSeverityBeans(List<TSeverityBean> list)
    {
        if (list == null)
        {
            collTSeverityBeans = null;
        }
        else
        {
            collTSeverityBeans = new ArrayList<TSeverityBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTStateBeans
     */
    protected List<TStateBean> collTStateBeans;

    /**
     * Returns the collection of TStateBeans
     */
    public List<TStateBean> getTStateBeans()
    {
        return collTStateBeans;
    }

    /**
     * Sets the collection of TStateBeans to the specified value
     */
    public void setTStateBeans(List<TStateBean> list)
    {
        if (list == null)
        {
            collTStateBeans = null;
        }
        else
        {
            collTStateBeans = new ArrayList<TStateBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTSystemStateBeans
     */
    protected List<TSystemStateBean> collTSystemStateBeans;

    /**
     * Returns the collection of TSystemStateBeans
     */
    public List<TSystemStateBean> getTSystemStateBeans()
    {
        return collTSystemStateBeans;
    }

    /**
     * Sets the collection of TSystemStateBeans to the specified value
     */
    public void setTSystemStateBeans(List<TSystemStateBean> list)
    {
        if (list == null)
        {
            collTSystemStateBeans = null;
        }
        else
        {
            collTSystemStateBeans = new ArrayList<TSystemStateBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTOptionBeans
     */
    protected List<TOptionBean> collTOptionBeans;

    /**
     * Returns the collection of TOptionBeans
     */
    public List<TOptionBean> getTOptionBeans()
    {
        return collTOptionBeans;
    }

    /**
     * Sets the collection of TOptionBeans to the specified value
     */
    public void setTOptionBeans(List<TOptionBean> list)
    {
        if (list == null)
        {
            collTOptionBeans = null;
        }
        else
        {
            collTOptionBeans = new ArrayList<TOptionBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTActionBeans
     */
    protected List<TActionBean> collTActionBeans;

    /**
     * Returns the collection of TActionBeans
     */
    public List<TActionBean> getTActionBeans()
    {
        return collTActionBeans;
    }

    /**
     * Sets the collection of TActionBeans to the specified value
     */
    public void setTActionBeans(List<TActionBean> list)
    {
        if (list == null)
        {
            collTActionBeans = null;
        }
        else
        {
            collTActionBeans = new ArrayList<TActionBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTLinkTypeBeansRelatedByOutwardIconKey
     */
    protected List<TLinkTypeBean> collTLinkTypeBeansRelatedByOutwardIconKey;

    /**
     * Returns the collection of TLinkTypeBeansRelatedByOutwardIconKey
     */
    public List<TLinkTypeBean> getTLinkTypeBeansRelatedByOutwardIconKey()
    {
        return collTLinkTypeBeansRelatedByOutwardIconKey;
    }

    /**
     * Sets the collection of TLinkTypeBeansRelatedByOutwardIconKey to the specified value
     */
    public void setTLinkTypeBeansRelatedByOutwardIconKey(List<TLinkTypeBean> list)
    {
        if (list == null)
        {
            collTLinkTypeBeansRelatedByOutwardIconKey = null;
        }
        else
        {
            collTLinkTypeBeansRelatedByOutwardIconKey = new ArrayList<TLinkTypeBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTLinkTypeBeansRelatedByInwardIconKey
     */
    protected List<TLinkTypeBean> collTLinkTypeBeansRelatedByInwardIconKey;

    /**
     * Returns the collection of TLinkTypeBeansRelatedByInwardIconKey
     */
    public List<TLinkTypeBean> getTLinkTypeBeansRelatedByInwardIconKey()
    {
        return collTLinkTypeBeansRelatedByInwardIconKey;
    }

    /**
     * Sets the collection of TLinkTypeBeansRelatedByInwardIconKey to the specified value
     */
    public void setTLinkTypeBeansRelatedByInwardIconKey(List<TLinkTypeBean> list)
    {
        if (list == null)
        {
            collTLinkTypeBeansRelatedByInwardIconKey = null;
        }
        else
        {
            collTLinkTypeBeansRelatedByInwardIconKey = new ArrayList<TLinkTypeBean>(list);
        }
    }

}
