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
 * extended; all references should be to TProjectBean
 */
public abstract class BaseTProjectBean
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

    /** The value for the defaultOwnerID field */
    private Integer defaultOwnerID;

    /** The value for the defaultManagerID field */
    private Integer defaultManagerID;

    /** The value for the defaultInitStateID field */
    private Integer defaultInitStateID;

    /** The value for the projectType field */
    private Integer projectType;

    /** The value for the versionSystemField0 field */
    private String versionSystemField0;

    /** The value for the versionSystemField1 field */
    private String versionSystemField1;

    /** The value for the versionSystemField2 field */
    private String versionSystemField2;

    /** The value for the versionSystemField3 field */
    private String versionSystemField3;

    /** The value for the deleted field */
    private String deleted = "N";

    /** The value for the status field */
    private Integer status;

    /** The value for the currencyName field */
    private String currencyName;

    /** The value for the currencySymbol field */
    private String currencySymbol;

    /** The value for the hoursPerWorkDay field */
    private Double hoursPerWorkDay;

    /** The value for the moreProps field */
    private String moreProps;

    /** The value for the tagLabel field */
    private String tagLabel;

    /** The value for the description field */
    private String description;

    /** The value for the prefix field */
    private String prefix;

    /** The value for the nextItemID field */
    private Integer nextItemID;

    /** The value for the lastID field */
    private Integer lastID;

    /** The value for the parent field */
    private Integer parent;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the isPrivate field */
    private String isPrivate = "N";

    /** The value for the isTemplate field */
    private String isTemplate = "N";

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
     * Get the DefaultOwnerID
     *
     * @return Integer
     */
    public Integer getDefaultOwnerID ()
    {
        return defaultOwnerID;
    }

    /**
     * Set the value of DefaultOwnerID
     *
     * @param v new value
     */
    public void setDefaultOwnerID(Integer v)
    {

        this.defaultOwnerID = v;
        setModified(true);

    }

    /**
     * Get the DefaultManagerID
     *
     * @return Integer
     */
    public Integer getDefaultManagerID ()
    {
        return defaultManagerID;
    }

    /**
     * Set the value of DefaultManagerID
     *
     * @param v new value
     */
    public void setDefaultManagerID(Integer v)
    {

        this.defaultManagerID = v;
        setModified(true);

    }

    /**
     * Get the DefaultInitStateID
     *
     * @return Integer
     */
    public Integer getDefaultInitStateID ()
    {
        return defaultInitStateID;
    }

    /**
     * Set the value of DefaultInitStateID
     *
     * @param v new value
     */
    public void setDefaultInitStateID(Integer v)
    {

        this.defaultInitStateID = v;
        setModified(true);

    }

    /**
     * Get the ProjectType
     *
     * @return Integer
     */
    public Integer getProjectType ()
    {
        return projectType;
    }

    /**
     * Set the value of ProjectType
     *
     * @param v new value
     */
    public void setProjectType(Integer v)
    {

        this.projectType = v;
        setModified(true);

    }

    /**
     * Get the VersionSystemField0
     *
     * @return String
     */
    public String getVersionSystemField0 ()
    {
        return versionSystemField0;
    }

    /**
     * Set the value of VersionSystemField0
     *
     * @param v new value
     */
    public void setVersionSystemField0(String v)
    {

        this.versionSystemField0 = v;
        setModified(true);

    }

    /**
     * Get the VersionSystemField1
     *
     * @return String
     */
    public String getVersionSystemField1 ()
    {
        return versionSystemField1;
    }

    /**
     * Set the value of VersionSystemField1
     *
     * @param v new value
     */
    public void setVersionSystemField1(String v)
    {

        this.versionSystemField1 = v;
        setModified(true);

    }

    /**
     * Get the VersionSystemField2
     *
     * @return String
     */
    public String getVersionSystemField2 ()
    {
        return versionSystemField2;
    }

    /**
     * Set the value of VersionSystemField2
     *
     * @param v new value
     */
    public void setVersionSystemField2(String v)
    {

        this.versionSystemField2 = v;
        setModified(true);

    }

    /**
     * Get the VersionSystemField3
     *
     * @return String
     */
    public String getVersionSystemField3 ()
    {
        return versionSystemField3;
    }

    /**
     * Set the value of VersionSystemField3
     *
     * @param v new value
     */
    public void setVersionSystemField3(String v)
    {

        this.versionSystemField3 = v;
        setModified(true);

    }

    /**
     * Get the Deleted
     *
     * @return String
     */
    public String getDeleted ()
    {
        return deleted;
    }

    /**
     * Set the value of Deleted
     *
     * @param v new value
     */
    public void setDeleted(String v)
    {

        this.deleted = v;
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
     * Get the CurrencyName
     *
     * @return String
     */
    public String getCurrencyName ()
    {
        return currencyName;
    }

    /**
     * Set the value of CurrencyName
     *
     * @param v new value
     */
    public void setCurrencyName(String v)
    {

        this.currencyName = v;
        setModified(true);

    }

    /**
     * Get the CurrencySymbol
     *
     * @return String
     */
    public String getCurrencySymbol ()
    {
        return currencySymbol;
    }

    /**
     * Set the value of CurrencySymbol
     *
     * @param v new value
     */
    public void setCurrencySymbol(String v)
    {

        this.currencySymbol = v;
        setModified(true);

    }

    /**
     * Get the HoursPerWorkDay
     *
     * @return Double
     */
    public Double getHoursPerWorkDay ()
    {
        return hoursPerWorkDay;
    }

    /**
     * Set the value of HoursPerWorkDay
     *
     * @param v new value
     */
    public void setHoursPerWorkDay(Double v)
    {

        this.hoursPerWorkDay = v;
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
     * Get the TagLabel
     *
     * @return String
     */
    public String getTagLabel ()
    {
        return tagLabel;
    }

    /**
     * Set the value of TagLabel
     *
     * @param v new value
     */
    public void setTagLabel(String v)
    {

        this.tagLabel = v;
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
     * Get the Prefix
     *
     * @return String
     */
    public String getPrefix ()
    {
        return prefix;
    }

    /**
     * Set the value of Prefix
     *
     * @param v new value
     */
    public void setPrefix(String v)
    {

        this.prefix = v;
        setModified(true);

    }

    /**
     * Get the NextItemID
     *
     * @return Integer
     */
    public Integer getNextItemID ()
    {
        return nextItemID;
    }

    /**
     * Set the value of NextItemID
     *
     * @param v new value
     */
    public void setNextItemID(Integer v)
    {

        this.nextItemID = v;
        setModified(true);

    }

    /**
     * Get the LastID
     *
     * @return Integer
     */
    public Integer getLastID ()
    {
        return lastID;
    }

    /**
     * Set the value of LastID
     *
     * @param v new value
     */
    public void setLastID(Integer v)
    {

        this.lastID = v;
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
     * Get the IsPrivate
     *
     * @return String
     */
    public String getIsPrivate ()
    {
        return isPrivate;
    }

    /**
     * Set the value of IsPrivate
     *
     * @param v new value
     */
    public void setIsPrivate(String v)
    {

        this.isPrivate = v;
        setModified(true);

    }

    /**
     * Get the IsTemplate
     *
     * @return String
     */
    public String getIsTemplate ()
    {
        return isTemplate;
    }

    /**
     * Set the value of IsTemplate
     *
     * @param v new value
     */
    public void setIsTemplate(String v)
    {

        this.isTemplate = v;
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

    



    private TPersonBean aTPersonBeanRelatedByDefaultOwnerID;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByDefaultOwnerID(TPersonBean v)
    {
        if (v == null)
        {
            setDefaultOwnerID((Integer) null);
        }
        else
        {
            setDefaultOwnerID(v.getObjectID());
        }
        aTPersonBeanRelatedByDefaultOwnerID = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByDefaultOwnerID()
    {
        return aTPersonBeanRelatedByDefaultOwnerID;
    }





    private TPersonBean aTPersonBeanRelatedByDefaultManagerID;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByDefaultManagerID(TPersonBean v)
    {
        if (v == null)
        {
            setDefaultManagerID((Integer) null);
        }
        else
        {
            setDefaultManagerID(v.getObjectID());
        }
        aTPersonBeanRelatedByDefaultManagerID = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByDefaultManagerID()
    {
        return aTPersonBeanRelatedByDefaultManagerID;
    }





    private TStateBean aTStateBean;

    /**
     * sets an associated TStateBean object
     *
     * @param v TStateBean
     */
    public void setTStateBean(TStateBean v)
    {
        if (v == null)
        {
            setDefaultInitStateID((Integer) null);
        }
        else
        {
            setDefaultInitStateID(v.getObjectID());
        }
        aTStateBean = v;
    }


    /**
     * Get the associated TStateBean object
     *
     * @return the associated TStateBean object
     */
    public TStateBean getTStateBean()
    {
        return aTStateBean;
    }





    private TProjectTypeBean aTProjectTypeBean;

    /**
     * sets an associated TProjectTypeBean object
     *
     * @param v TProjectTypeBean
     */
    public void setTProjectTypeBean(TProjectTypeBean v)
    {
        if (v == null)
        {
            setProjectType((Integer) null);
        }
        else
        {
            setProjectType(v.getObjectID());
        }
        aTProjectTypeBean = v;
    }


    /**
     * Get the associated TProjectTypeBean object
     *
     * @return the associated TProjectTypeBean object
     */
    public TProjectTypeBean getTProjectTypeBean()
    {
        return aTProjectTypeBean;
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





    private TProjectBean aTProjectBeanRelatedByParent;

    /**
     * sets an associated TProjectBean object
     *
     * @param v TProjectBean
     */
    public void setTProjectBeanRelatedByParent(TProjectBean v)
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTProjectBeanRelatedByParent = v;
    }


    /**
     * Get the associated TProjectBean object
     *
     * @return the associated TProjectBean object
     */
    public TProjectBean getTProjectBeanRelatedByParent()
    {
        return aTProjectBeanRelatedByParent;
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
     * Collection to store aggregation of collTAccessControlListBeans
     */
    protected List<TAccessControlListBean> collTAccessControlListBeans;

    /**
     * Returns the collection of TAccessControlListBeans
     */
    public List<TAccessControlListBean> getTAccessControlListBeans()
    {
        return collTAccessControlListBeans;
    }

    /**
     * Sets the collection of TAccessControlListBeans to the specified value
     */
    public void setTAccessControlListBeans(List<TAccessControlListBean> list)
    {
        if (list == null)
        {
            collTAccessControlListBeans = null;
        }
        else
        {
            collTAccessControlListBeans = new ArrayList<TAccessControlListBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTClassBeans
     */
    protected List<TClassBean> collTClassBeans;

    /**
     * Returns the collection of TClassBeans
     */
    public List<TClassBean> getTClassBeans()
    {
        return collTClassBeans;
    }

    /**
     * Sets the collection of TClassBeans to the specified value
     */
    public void setTClassBeans(List<TClassBean> list)
    {
        if (list == null)
        {
            collTClassBeans = null;
        }
        else
        {
            collTClassBeans = new ArrayList<TClassBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTProjectCategoryBeans
     */
    protected List<TProjectCategoryBean> collTProjectCategoryBeans;

    /**
     * Returns the collection of TProjectCategoryBeans
     */
    public List<TProjectCategoryBean> getTProjectCategoryBeans()
    {
        return collTProjectCategoryBeans;
    }

    /**
     * Sets the collection of TProjectCategoryBeans to the specified value
     */
    public void setTProjectCategoryBeans(List<TProjectCategoryBean> list)
    {
        if (list == null)
        {
            collTProjectCategoryBeans = null;
        }
        else
        {
            collTProjectCategoryBeans = new ArrayList<TProjectCategoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTReleaseBeans
     */
    protected List<TReleaseBean> collTReleaseBeans;

    /**
     * Returns the collection of TReleaseBeans
     */
    public List<TReleaseBean> getTReleaseBeans()
    {
        return collTReleaseBeans;
    }

    /**
     * Sets the collection of TReleaseBeans to the specified value
     */
    public void setTReleaseBeans(List<TReleaseBean> list)
    {
        if (list == null)
        {
            collTReleaseBeans = null;
        }
        else
        {
            collTReleaseBeans = new ArrayList<TReleaseBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkItemBeans
     */
    protected List<TWorkItemBean> collTWorkItemBeans;

    /**
     * Returns the collection of TWorkItemBeans
     */
    public List<TWorkItemBean> getTWorkItemBeans()
    {
        return collTWorkItemBeans;
    }

    /**
     * Sets the collection of TWorkItemBeans to the specified value
     */
    public void setTWorkItemBeans(List<TWorkItemBean> list)
    {
        if (list == null)
        {
            collTWorkItemBeans = null;
        }
        else
        {
            collTWorkItemBeans = new ArrayList<TWorkItemBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTProjectReportRepositoryBeans
     */
    protected List<TProjectReportRepositoryBean> collTProjectReportRepositoryBeans;

    /**
     * Returns the collection of TProjectReportRepositoryBeans
     */
    public List<TProjectReportRepositoryBean> getTProjectReportRepositoryBeans()
    {
        return collTProjectReportRepositoryBeans;
    }

    /**
     * Sets the collection of TProjectReportRepositoryBeans to the specified value
     */
    public void setTProjectReportRepositoryBeans(List<TProjectReportRepositoryBean> list)
    {
        if (list == null)
        {
            collTProjectReportRepositoryBeans = null;
        }
        else
        {
            collTProjectReportRepositoryBeans = new ArrayList<TProjectReportRepositoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTReportLayoutBeans
     */
    protected List<TReportLayoutBean> collTReportLayoutBeans;

    /**
     * Returns the collection of TReportLayoutBeans
     */
    public List<TReportLayoutBean> getTReportLayoutBeans()
    {
        return collTReportLayoutBeans;
    }

    /**
     * Sets the collection of TReportLayoutBeans to the specified value
     */
    public void setTReportLayoutBeans(List<TReportLayoutBean> list)
    {
        if (list == null)
        {
            collTReportLayoutBeans = null;
        }
        else
        {
            collTReportLayoutBeans = new ArrayList<TReportLayoutBean>(list);
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



    /**
     * Collection to store aggregation of collTDashboardScreenBeans
     */
    protected List<TDashboardScreenBean> collTDashboardScreenBeans;

    /**
     * Returns the collection of TDashboardScreenBeans
     */
    public List<TDashboardScreenBean> getTDashboardScreenBeans()
    {
        return collTDashboardScreenBeans;
    }

    /**
     * Sets the collection of TDashboardScreenBeans to the specified value
     */
    public void setTDashboardScreenBeans(List<TDashboardScreenBean> list)
    {
        if (list == null)
        {
            collTDashboardScreenBeans = null;
        }
        else
        {
            collTDashboardScreenBeans = new ArrayList<TDashboardScreenBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTVersionControlParameterBeans
     */
    protected List<TVersionControlParameterBean> collTVersionControlParameterBeans;

    /**
     * Returns the collection of TVersionControlParameterBeans
     */
    public List<TVersionControlParameterBean> getTVersionControlParameterBeans()
    {
        return collTVersionControlParameterBeans;
    }

    /**
     * Sets the collection of TVersionControlParameterBeans to the specified value
     */
    public void setTVersionControlParameterBeans(List<TVersionControlParameterBean> list)
    {
        if (list == null)
        {
            collTVersionControlParameterBeans = null;
        }
        else
        {
            collTVersionControlParameterBeans = new ArrayList<TVersionControlParameterBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTFieldBeans
     */
    protected List<TFieldBean> collTFieldBeans;

    /**
     * Returns the collection of TFieldBeans
     */
    public List<TFieldBean> getTFieldBeans()
    {
        return collTFieldBeans;
    }

    /**
     * Sets the collection of TFieldBeans to the specified value
     */
    public void setTFieldBeans(List<TFieldBean> list)
    {
        if (list == null)
        {
            collTFieldBeans = null;
        }
        else
        {
            collTFieldBeans = new ArrayList<TFieldBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTFieldConfigBeans
     */
    protected List<TFieldConfigBean> collTFieldConfigBeans;

    /**
     * Returns the collection of TFieldConfigBeans
     */
    public List<TFieldConfigBean> getTFieldConfigBeans()
    {
        return collTFieldConfigBeans;
    }

    /**
     * Sets the collection of TFieldConfigBeans to the specified value
     */
    public void setTFieldConfigBeans(List<TFieldConfigBean> list)
    {
        if (list == null)
        {
            collTFieldConfigBeans = null;
        }
        else
        {
            collTFieldConfigBeans = new ArrayList<TFieldConfigBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTListBeans
     */
    protected List<TListBean> collTListBeans;

    /**
     * Returns the collection of TListBeans
     */
    public List<TListBean> getTListBeans()
    {
        return collTListBeans;
    }

    /**
     * Sets the collection of TListBeans to the specified value
     */
    public void setTListBeans(List<TListBean> list)
    {
        if (list == null)
        {
            collTListBeans = null;
        }
        else
        {
            collTListBeans = new ArrayList<TListBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTScreenBeans
     */
    protected List<TScreenBean> collTScreenBeans;

    /**
     * Returns the collection of TScreenBeans
     */
    public List<TScreenBean> getTScreenBeans()
    {
        return collTScreenBeans;
    }

    /**
     * Sets the collection of TScreenBeans to the specified value
     */
    public void setTScreenBeans(List<TScreenBean> list)
    {
        if (list == null)
        {
            collTScreenBeans = null;
        }
        else
        {
            collTScreenBeans = new ArrayList<TScreenBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTScreenConfigBeans
     */
    protected List<TScreenConfigBean> collTScreenConfigBeans;

    /**
     * Returns the collection of TScreenConfigBeans
     */
    public List<TScreenConfigBean> getTScreenConfigBeans()
    {
        return collTScreenConfigBeans;
    }

    /**
     * Sets the collection of TScreenConfigBeans to the specified value
     */
    public void setTScreenConfigBeans(List<TScreenConfigBean> list)
    {
        if (list == null)
        {
            collTScreenConfigBeans = null;
        }
        else
        {
            collTScreenConfigBeans = new ArrayList<TScreenConfigBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTInitStateBeans
     */
    protected List<TInitStateBean> collTInitStateBeans;

    /**
     * Returns the collection of TInitStateBeans
     */
    public List<TInitStateBean> getTInitStateBeans()
    {
        return collTInitStateBeans;
    }

    /**
     * Sets the collection of TInitStateBeans to the specified value
     */
    public void setTInitStateBeans(List<TInitStateBean> list)
    {
        if (list == null)
        {
            collTInitStateBeans = null;
        }
        else
        {
            collTInitStateBeans = new ArrayList<TInitStateBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTEventBeans
     */
    protected List<TEventBean> collTEventBeans;

    /**
     * Returns the collection of TEventBeans
     */
    public List<TEventBean> getTEventBeans()
    {
        return collTEventBeans;
    }

    /**
     * Sets the collection of TEventBeans to the specified value
     */
    public void setTEventBeans(List<TEventBean> list)
    {
        if (list == null)
        {
            collTEventBeans = null;
        }
        else
        {
            collTEventBeans = new ArrayList<TEventBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTNotifySettingsBeans
     */
    protected List<TNotifySettingsBean> collTNotifySettingsBeans;

    /**
     * Returns the collection of TNotifySettingsBeans
     */
    public List<TNotifySettingsBean> getTNotifySettingsBeans()
    {
        return collTNotifySettingsBeans;
    }

    /**
     * Sets the collection of TNotifySettingsBeans to the specified value
     */
    public void setTNotifySettingsBeans(List<TNotifySettingsBean> list)
    {
        if (list == null)
        {
            collTNotifySettingsBeans = null;
        }
        else
        {
            collTNotifySettingsBeans = new ArrayList<TNotifySettingsBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTQueryRepositoryBeans
     */
    protected List<TQueryRepositoryBean> collTQueryRepositoryBeans;

    /**
     * Returns the collection of TQueryRepositoryBeans
     */
    public List<TQueryRepositoryBean> getTQueryRepositoryBeans()
    {
        return collTQueryRepositoryBeans;
    }

    /**
     * Sets the collection of TQueryRepositoryBeans to the specified value
     */
    public void setTQueryRepositoryBeans(List<TQueryRepositoryBean> list)
    {
        if (list == null)
        {
            collTQueryRepositoryBeans = null;
        }
        else
        {
            collTQueryRepositoryBeans = new ArrayList<TQueryRepositoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTExportTemplateBeans
     */
    protected List<TExportTemplateBean> collTExportTemplateBeans;

    /**
     * Returns the collection of TExportTemplateBeans
     */
    public List<TExportTemplateBean> getTExportTemplateBeans()
    {
        return collTExportTemplateBeans;
    }

    /**
     * Sets the collection of TExportTemplateBeans to the specified value
     */
    public void setTExportTemplateBeans(List<TExportTemplateBean> list)
    {
        if (list == null)
        {
            collTExportTemplateBeans = null;
        }
        else
        {
            collTExportTemplateBeans = new ArrayList<TExportTemplateBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTScriptsBeans
     */
    protected List<TScriptsBean> collTScriptsBeans;

    /**
     * Returns the collection of TScriptsBeans
     */
    public List<TScriptsBean> getTScriptsBeans()
    {
        return collTScriptsBeans;
    }

    /**
     * Sets the collection of TScriptsBeans to the specified value
     */
    public void setTScriptsBeans(List<TScriptsBean> list)
    {
        if (list == null)
        {
            collTScriptsBeans = null;
        }
        else
        {
            collTScriptsBeans = new ArrayList<TScriptsBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTFilterCategoryBeans
     */
    protected List<TFilterCategoryBean> collTFilterCategoryBeans;

    /**
     * Returns the collection of TFilterCategoryBeans
     */
    public List<TFilterCategoryBean> getTFilterCategoryBeans()
    {
        return collTFilterCategoryBeans;
    }

    /**
     * Sets the collection of TFilterCategoryBeans to the specified value
     */
    public void setTFilterCategoryBeans(List<TFilterCategoryBean> list)
    {
        if (list == null)
        {
            collTFilterCategoryBeans = null;
        }
        else
        {
            collTFilterCategoryBeans = new ArrayList<TFilterCategoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTReportCategoryBeans
     */
    protected List<TReportCategoryBean> collTReportCategoryBeans;

    /**
     * Returns the collection of TReportCategoryBeans
     */
    public List<TReportCategoryBean> getTReportCategoryBeans()
    {
        return collTReportCategoryBeans;
    }

    /**
     * Sets the collection of TReportCategoryBeans to the specified value
     */
    public void setTReportCategoryBeans(List<TReportCategoryBean> list)
    {
        if (list == null)
        {
            collTReportCategoryBeans = null;
        }
        else
        {
            collTReportCategoryBeans = new ArrayList<TReportCategoryBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTMailTemplateConfigBeans
     */
    protected List<TMailTemplateConfigBean> collTMailTemplateConfigBeans;

    /**
     * Returns the collection of TMailTemplateConfigBeans
     */
    public List<TMailTemplateConfigBean> getTMailTemplateConfigBeans()
    {
        return collTMailTemplateConfigBeans;
    }

    /**
     * Sets the collection of TMailTemplateConfigBeans to the specified value
     */
    public void setTMailTemplateConfigBeans(List<TMailTemplateConfigBean> list)
    {
        if (list == null)
        {
            collTMailTemplateConfigBeans = null;
        }
        else
        {
            collTMailTemplateConfigBeans = new ArrayList<TMailTemplateConfigBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWfActivityContextParamsBeans
     */
    protected List<TWfActivityContextParamsBean> collTWfActivityContextParamsBeans;

    /**
     * Returns the collection of TWfActivityContextParamsBeans
     */
    public List<TWfActivityContextParamsBean> getTWfActivityContextParamsBeans()
    {
        return collTWfActivityContextParamsBeans;
    }

    /**
     * Sets the collection of TWfActivityContextParamsBeans to the specified value
     */
    public void setTWfActivityContextParamsBeans(List<TWfActivityContextParamsBean> list)
    {
        if (list == null)
        {
            collTWfActivityContextParamsBeans = null;
        }
        else
        {
            collTWfActivityContextParamsBeans = new ArrayList<TWfActivityContextParamsBean>(list);
        }
    }



    /**
     * Collection to store aggregation of collTWorkflowConnectBeans
     */
    protected List<TWorkflowConnectBean> collTWorkflowConnectBeans;

    /**
     * Returns the collection of TWorkflowConnectBeans
     */
    public List<TWorkflowConnectBean> getTWorkflowConnectBeans()
    {
        return collTWorkflowConnectBeans;
    }

    /**
     * Sets the collection of TWorkflowConnectBeans to the specified value
     */
    public void setTWorkflowConnectBeans(List<TWorkflowConnectBean> list)
    {
        if (list == null)
        {
            collTWorkflowConnectBeans = null;
        }
        else
        {
            collTWorkflowConnectBeans = new ArrayList<TWorkflowConnectBean>(list);
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



    /**
     * Collection to store aggregation of collTMailTextBlockBeans
     */
    protected List<TMailTextBlockBean> collTMailTextBlockBeans;

    /**
     * Returns the collection of TMailTextBlockBeans
     */
    public List<TMailTextBlockBean> getTMailTextBlockBeans()
    {
        return collTMailTextBlockBeans;
    }

    /**
     * Sets the collection of TMailTextBlockBeans to the specified value
     */
    public void setTMailTextBlockBeans(List<TMailTextBlockBean> list)
    {
        if (list == null)
        {
            collTMailTextBlockBeans = null;
        }
        else
        {
            collTMailTextBlockBeans = new ArrayList<TMailTextBlockBean>(list);
        }
    }

}
