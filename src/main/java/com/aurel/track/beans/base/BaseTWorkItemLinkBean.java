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
 * extended; all references should be to TWorkItemLinkBean
 */
public abstract class BaseTWorkItemLinkBean
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

    /** The value for the linkIsCrossProject field */
    private String linkIsCrossProject = "N";

    /** The value for the linkPred field */
    private Integer linkPred;

    /** The value for the linkSucc field */
    private Integer linkSucc;

    /** The value for the linkType field */
    private Integer linkType;

    /** The value for the linkDirection field */
    private Integer linkDirection;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the linkLag field */
    private Double linkLag;

    /** The value for the linkLagFormat field */
    private Integer linkLagFormat;

    /** The value for the stringValue1 field */
    private String stringValue1;

    /** The value for the stringValue2 field */
    private String stringValue2;

    /** The value for the stringValue3 field */
    private String stringValue3;

    /** The value for the integerValue1 field */
    private Integer integerValue1;

    /** The value for the integerValue2 field */
    private Integer integerValue2;

    /** The value for the integerValue3 field */
    private Integer integerValue3;

    /** The value for the dATEVALUE field */
    private Date dATEVALUE;

    /** The value for the description field */
    private String description;

    /** The value for the externalLink field */
    private String externalLink;

    /** The value for the changedBy field */
    private Integer changedBy;

    /** The value for the lastEdit field */
    private Date lastEdit;

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
     * Get the LinkIsCrossProject
     *
     * @return String
     */
    public String getLinkIsCrossProject ()
    {
        return linkIsCrossProject;
    }

    /**
     * Set the value of LinkIsCrossProject
     *
     * @param v new value
     */
    public void setLinkIsCrossProject(String v)
    {

        this.linkIsCrossProject = v;
        setModified(true);

    }

    /**
     * Get the LinkPred
     *
     * @return Integer
     */
    public Integer getLinkPred ()
    {
        return linkPred;
    }

    /**
     * Set the value of LinkPred
     *
     * @param v new value
     */
    public void setLinkPred(Integer v)
    {

        this.linkPred = v;
        setModified(true);

    }

    /**
     * Get the LinkSucc
     *
     * @return Integer
     */
    public Integer getLinkSucc ()
    {
        return linkSucc;
    }

    /**
     * Set the value of LinkSucc
     *
     * @param v new value
     */
    public void setLinkSucc(Integer v)
    {

        this.linkSucc = v;
        setModified(true);

    }

    /**
     * Get the LinkType
     *
     * @return Integer
     */
    public Integer getLinkType ()
    {
        return linkType;
    }

    /**
     * Set the value of LinkType
     *
     * @param v new value
     */
    public void setLinkType(Integer v)
    {

        this.linkType = v;
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
     * Get the Sortorder
     *
     * @return Integer
     */
    public Integer getSortorder ()
    {
        return sortorder;
    }

    /**
     * Set the value of Sortorder
     *
     * @param v new value
     */
    public void setSortorder(Integer v)
    {

        this.sortorder = v;
        setModified(true);

    }

    /**
     * Get the LinkLag
     *
     * @return Double
     */
    public Double getLinkLag ()
    {
        return linkLag;
    }

    /**
     * Set the value of LinkLag
     *
     * @param v new value
     */
    public void setLinkLag(Double v)
    {

        this.linkLag = v;
        setModified(true);

    }

    /**
     * Get the LinkLagFormat
     *
     * @return Integer
     */
    public Integer getLinkLagFormat ()
    {
        return linkLagFormat;
    }

    /**
     * Set the value of LinkLagFormat
     *
     * @param v new value
     */
    public void setLinkLagFormat(Integer v)
    {

        this.linkLagFormat = v;
        setModified(true);

    }

    /**
     * Get the StringValue1
     *
     * @return String
     */
    public String getStringValue1 ()
    {
        return stringValue1;
    }

    /**
     * Set the value of StringValue1
     *
     * @param v new value
     */
    public void setStringValue1(String v)
    {

        this.stringValue1 = v;
        setModified(true);

    }

    /**
     * Get the StringValue2
     *
     * @return String
     */
    public String getStringValue2 ()
    {
        return stringValue2;
    }

    /**
     * Set the value of StringValue2
     *
     * @param v new value
     */
    public void setStringValue2(String v)
    {

        this.stringValue2 = v;
        setModified(true);

    }

    /**
     * Get the StringValue3
     *
     * @return String
     */
    public String getStringValue3 ()
    {
        return stringValue3;
    }

    /**
     * Set the value of StringValue3
     *
     * @param v new value
     */
    public void setStringValue3(String v)
    {

        this.stringValue3 = v;
        setModified(true);

    }

    /**
     * Get the IntegerValue1
     *
     * @return Integer
     */
    public Integer getIntegerValue1 ()
    {
        return integerValue1;
    }

    /**
     * Set the value of IntegerValue1
     *
     * @param v new value
     */
    public void setIntegerValue1(Integer v)
    {

        this.integerValue1 = v;
        setModified(true);

    }

    /**
     * Get the IntegerValue2
     *
     * @return Integer
     */
    public Integer getIntegerValue2 ()
    {
        return integerValue2;
    }

    /**
     * Set the value of IntegerValue2
     *
     * @param v new value
     */
    public void setIntegerValue2(Integer v)
    {

        this.integerValue2 = v;
        setModified(true);

    }

    /**
     * Get the IntegerValue3
     *
     * @return Integer
     */
    public Integer getIntegerValue3 ()
    {
        return integerValue3;
    }

    /**
     * Set the value of IntegerValue3
     *
     * @param v new value
     */
    public void setIntegerValue3(Integer v)
    {

        this.integerValue3 = v;
        setModified(true);

    }

    /**
     * Get the DATEVALUE
     *
     * @return Date
     */
    public Date getDATEVALUE ()
    {
        return dATEVALUE;
    }

    /**
     * Set the value of DATEVALUE
     *
     * @param v new value
     */
    public void setDATEVALUE(Date v)
    {

        this.dATEVALUE = v;
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
     * Get the ExternalLink
     *
     * @return String
     */
    public String getExternalLink ()
    {
        return externalLink;
    }

    /**
     * Set the value of ExternalLink
     *
     * @param v new value
     */
    public void setExternalLink(String v)
    {

        this.externalLink = v;
        setModified(true);

    }

    /**
     * Get the ChangedBy
     *
     * @return Integer
     */
    public Integer getChangedBy ()
    {
        return changedBy;
    }

    /**
     * Set the value of ChangedBy
     *
     * @param v new value
     */
    public void setChangedBy(Integer v)
    {

        this.changedBy = v;
        setModified(true);

    }

    /**
     * Get the LastEdit
     *
     * @return Date
     */
    public Date getLastEdit ()
    {
        return lastEdit;
    }

    /**
     * Set the value of LastEdit
     *
     * @param v new value
     */
    public void setLastEdit(Date v)
    {

        this.lastEdit = v;
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

    



    private TWorkItemBean aTWorkItemBeanRelatedByLinkPred;

    /**
     * sets an associated TWorkItemBean object
     *
     * @param v TWorkItemBean
     */
    public void setTWorkItemBeanRelatedByLinkPred(TWorkItemBean v)
    {
        if (v == null)
        {
            setLinkPred((Integer) null);
        }
        else
        {
            setLinkPred(v.getObjectID());
        }
        aTWorkItemBeanRelatedByLinkPred = v;
    }


    /**
     * Get the associated TWorkItemBean object
     *
     * @return the associated TWorkItemBean object
     */
    public TWorkItemBean getTWorkItemBeanRelatedByLinkPred()
    {
        return aTWorkItemBeanRelatedByLinkPred;
    }





    private TWorkItemBean aTWorkItemBeanRelatedByLinkSucc;

    /**
     * sets an associated TWorkItemBean object
     *
     * @param v TWorkItemBean
     */
    public void setTWorkItemBeanRelatedByLinkSucc(TWorkItemBean v)
    {
        if (v == null)
        {
            setLinkSucc((Integer) null);
        }
        else
        {
            setLinkSucc(v.getObjectID());
        }
        aTWorkItemBeanRelatedByLinkSucc = v;
    }


    /**
     * Get the associated TWorkItemBean object
     *
     * @return the associated TWorkItemBean object
     */
    public TWorkItemBean getTWorkItemBeanRelatedByLinkSucc()
    {
        return aTWorkItemBeanRelatedByLinkSucc;
    }





    private TLinkTypeBean aTLinkTypeBean;

    /**
     * sets an associated TLinkTypeBean object
     *
     * @param v TLinkTypeBean
     */
    public void setTLinkTypeBean(TLinkTypeBean v)
    {
        if (v == null)
        {
            setLinkType((Integer) null);
        }
        else
        {
            setLinkType(v.getObjectID());
        }
        aTLinkTypeBean = v;
    }


    /**
     * Get the associated TLinkTypeBean object
     *
     * @return the associated TLinkTypeBean object
     */
    public TLinkTypeBean getTLinkTypeBean()
    {
        return aTLinkTypeBean;
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
            setChangedBy((Integer) null);
        }
        else
        {
            setChangedBy(v.getObjectID());
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



}
