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
 * extended; all references should be to TLinkTypeBean
 */
public abstract class BaseTLinkTypeBean
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

    /** The value for the reverseName field */
    private String reverseName;

    /** The value for the leftToRightFirst field */
    private String leftToRightFirst;

    /** The value for the leftToRightLevel field */
    private String leftToRightLevel;

    /** The value for the leftToRightAll field */
    private String leftToRightAll;

    /** The value for the rightToLeftFirst field */
    private String rightToLeftFirst;

    /** The value for the rightToLeftLevel field */
    private String rightToLeftLevel;

    /** The value for the rightToLeftAll field */
    private String rightToLeftAll;

    /** The value for the linkDirection field */
    private Integer linkDirection;

    /** The value for the outwardIconKey field */
    private Integer outwardIconKey;

    /** The value for the inwardIconKey field */
    private Integer inwardIconKey;

    /** The value for the linkTypePlugin field */
    private String linkTypePlugin;

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
     * Get the ReverseName
     *
     * @return String
     */
    public String getReverseName ()
    {
        return reverseName;
    }

    /**
     * Set the value of ReverseName
     *
     * @param v new value
     */
    public void setReverseName(String v)
    {

        this.reverseName = v;
        setModified(true);

    }

    /**
     * Get the LeftToRightFirst
     *
     * @return String
     */
    public String getLeftToRightFirst ()
    {
        return leftToRightFirst;
    }

    /**
     * Set the value of LeftToRightFirst
     *
     * @param v new value
     */
    public void setLeftToRightFirst(String v)
    {

        this.leftToRightFirst = v;
        setModified(true);

    }

    /**
     * Get the LeftToRightLevel
     *
     * @return String
     */
    public String getLeftToRightLevel ()
    {
        return leftToRightLevel;
    }

    /**
     * Set the value of LeftToRightLevel
     *
     * @param v new value
     */
    public void setLeftToRightLevel(String v)
    {

        this.leftToRightLevel = v;
        setModified(true);

    }

    /**
     * Get the LeftToRightAll
     *
     * @return String
     */
    public String getLeftToRightAll ()
    {
        return leftToRightAll;
    }

    /**
     * Set the value of LeftToRightAll
     *
     * @param v new value
     */
    public void setLeftToRightAll(String v)
    {

        this.leftToRightAll = v;
        setModified(true);

    }

    /**
     * Get the RightToLeftFirst
     *
     * @return String
     */
    public String getRightToLeftFirst ()
    {
        return rightToLeftFirst;
    }

    /**
     * Set the value of RightToLeftFirst
     *
     * @param v new value
     */
    public void setRightToLeftFirst(String v)
    {

        this.rightToLeftFirst = v;
        setModified(true);

    }

    /**
     * Get the RightToLeftLevel
     *
     * @return String
     */
    public String getRightToLeftLevel ()
    {
        return rightToLeftLevel;
    }

    /**
     * Set the value of RightToLeftLevel
     *
     * @param v new value
     */
    public void setRightToLeftLevel(String v)
    {

        this.rightToLeftLevel = v;
        setModified(true);

    }

    /**
     * Get the RightToLeftAll
     *
     * @return String
     */
    public String getRightToLeftAll ()
    {
        return rightToLeftAll;
    }

    /**
     * Set the value of RightToLeftAll
     *
     * @param v new value
     */
    public void setRightToLeftAll(String v)
    {

        this.rightToLeftAll = v;
        setModified(true);

    }

    /**
     * Get the LinkDirection
     *
     * @return Integer
     */
    public Integer getLinkDirection ()
    {
        return linkDirection;
    }

    /**
     * Set the value of LinkDirection
     *
     * @param v new value
     */
    public void setLinkDirection(Integer v)
    {

        this.linkDirection = v;
        setModified(true);

    }

    /**
     * Get the OutwardIconKey
     *
     * @return Integer
     */
    public Integer getOutwardIconKey ()
    {
        return outwardIconKey;
    }

    /**
     * Set the value of OutwardIconKey
     *
     * @param v new value
     */
    public void setOutwardIconKey(Integer v)
    {

        this.outwardIconKey = v;
        setModified(true);

    }

    /**
     * Get the InwardIconKey
     *
     * @return Integer
     */
    public Integer getInwardIconKey ()
    {
        return inwardIconKey;
    }

    /**
     * Set the value of InwardIconKey
     *
     * @param v new value
     */
    public void setInwardIconKey(Integer v)
    {

        this.inwardIconKey = v;
        setModified(true);

    }

    /**
     * Get the LinkTypePlugin
     *
     * @return String
     */
    public String getLinkTypePlugin ()
    {
        return linkTypePlugin;
    }

    /**
     * Set the value of LinkTypePlugin
     *
     * @param v new value
     */
    public void setLinkTypePlugin(String v)
    {

        this.linkTypePlugin = v;
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

    



    private TBLOBBean aTBLOBBeanRelatedByOutwardIconKey;

    /**
     * sets an associated TBLOBBean object
     *
     * @param v TBLOBBean
     */
    public void setTBLOBBeanRelatedByOutwardIconKey(TBLOBBean v)
    {
        if (v == null)
        {
            setOutwardIconKey((Integer) null);
        }
        else
        {
            setOutwardIconKey(v.getObjectID());
        }
        aTBLOBBeanRelatedByOutwardIconKey = v;
    }


    /**
     * Get the associated TBLOBBean object
     *
     * @return the associated TBLOBBean object
     */
    public TBLOBBean getTBLOBBeanRelatedByOutwardIconKey()
    {
        return aTBLOBBeanRelatedByOutwardIconKey;
    }





    private TBLOBBean aTBLOBBeanRelatedByInwardIconKey;

    /**
     * sets an associated TBLOBBean object
     *
     * @param v TBLOBBean
     */
    public void setTBLOBBeanRelatedByInwardIconKey(TBLOBBean v)
    {
        if (v == null)
        {
            setInwardIconKey((Integer) null);
        }
        else
        {
            setInwardIconKey(v.getObjectID());
        }
        aTBLOBBeanRelatedByInwardIconKey = v;
    }


    /**
     * Get the associated TBLOBBean object
     *
     * @return the associated TBLOBBean object
     */
    public TBLOBBean getTBLOBBeanRelatedByInwardIconKey()
    {
        return aTBLOBBeanRelatedByInwardIconKey;
    }





    /**
     * Collection to store aggregation of collTWorkItemLinkBeans
     */
    protected List<TWorkItemLinkBean> collTWorkItemLinkBeans;

    /**
     * Returns the collection of TWorkItemLinkBeans
     */
    public List<TWorkItemLinkBean> getTWorkItemLinkBeans()
    {
        return collTWorkItemLinkBeans;
    }

    /**
     * Sets the collection of TWorkItemLinkBeans to the specified value
     */
    public void setTWorkItemLinkBeans(List<TWorkItemLinkBean> list)
    {
        if (list == null)
        {
            collTWorkItemLinkBeans = null;
        }
        else
        {
            collTWorkItemLinkBeans = new ArrayList<TWorkItemLinkBean>(list);
        }
    }

}
